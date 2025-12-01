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
@Table(name = "BRRS_M_PD_SUMMARYTABLE2")
public class M_PD_Summary_Entity2 {

	private	String	R38_PRODUCT;
	private	BigDecimal	R38_30D_90D_PASTDUE;
	private	BigDecimal	R38_NON_PERFORM_LOANS;
	private	BigDecimal	R38_NON_ACCRUALS1;
	private	BigDecimal	R38_SPECIFIC_PROV1;
	private	BigDecimal	R38_NO_OF_ACC1;
	private	BigDecimal	R38_90D_180D_PASTDUE;
	private	BigDecimal	R38_NON_ACCRUALS2;
	private	BigDecimal	R38_SPECIFIC_PROV2;
	private	BigDecimal	R38_NO_OF_ACC2;
	private	BigDecimal	R38_180D_ABOVE_PASTDUE;
	private	BigDecimal	R38_NON_ACCRUALS3;
	private	BigDecimal	R38_SPECIFIC_PROV3;
	private	BigDecimal	R38_NO_OF_ACC3;
	private	BigDecimal	R38_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R38_TOTAL_DUE_LOANS;
	private	BigDecimal	R38_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R38_VALUE_OF_COLLATERAL;
	private	BigDecimal	R38_TOTAL_VALUE_NPL;
	private	BigDecimal	R38_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R38_SPECIFIC_PROV_NPL;
	private	String	R39_PRODUCT;
	private	BigDecimal	R39_30D_90D_PASTDUE;
	private	BigDecimal	R39_NON_PERFORM_LOANS;
	private	BigDecimal	R39_NON_ACCRUALS1;
	private	BigDecimal	R39_SPECIFIC_PROV1;
	private	BigDecimal	R39_NO_OF_ACC1;
	private	BigDecimal	R39_90D_180D_PASTDUE;
	private	BigDecimal	R39_NON_ACCRUALS2;
	private	BigDecimal	R39_SPECIFIC_PROV2;
	private	BigDecimal	R39_NO_OF_ACC2;
	private	BigDecimal	R39_180D_ABOVE_PASTDUE;
	private	BigDecimal	R39_NON_ACCRUALS3;
	private	BigDecimal	R39_SPECIFIC_PROV3;
	private	BigDecimal	R39_NO_OF_ACC3;
	private	BigDecimal	R39_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R39_TOTAL_DUE_LOANS;
	private	BigDecimal	R39_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R39_VALUE_OF_COLLATERAL;
	private	BigDecimal	R39_TOTAL_VALUE_NPL;
	private	BigDecimal	R39_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R39_SPECIFIC_PROV_NPL;
	private	String	R40_PRODUCT;
	private	BigDecimal	R40_30D_90D_PASTDUE;
	private	BigDecimal	R40_NON_PERFORM_LOANS;
	private	BigDecimal	R40_NON_ACCRUALS1;
	private	BigDecimal	R40_SPECIFIC_PROV1;
	private	BigDecimal	R40_NO_OF_ACC1;
	private	BigDecimal	R40_90D_180D_PASTDUE;
	private	BigDecimal	R40_NON_ACCRUALS2;
	private	BigDecimal	R40_SPECIFIC_PROV2;
	private	BigDecimal	R40_NO_OF_ACC2;
	private	BigDecimal	R40_180D_ABOVE_PASTDUE;
	private	BigDecimal	R40_NON_ACCRUALS3;
	private	BigDecimal	R40_SPECIFIC_PROV3;
	private	BigDecimal	R40_NO_OF_ACC3;
	private	BigDecimal	R40_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R40_TOTAL_DUE_LOANS;
	private	BigDecimal	R40_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R40_VALUE_OF_COLLATERAL;
	private	BigDecimal	R40_TOTAL_VALUE_NPL;
	private	BigDecimal	R40_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R40_SPECIFIC_PROV_NPL;
	private	String	R41_PRODUCT;
	private	BigDecimal	R41_30D_90D_PASTDUE;
	private	BigDecimal	R41_NON_PERFORM_LOANS;
	private	BigDecimal	R41_NON_ACCRUALS1;
	private	BigDecimal	R41_SPECIFIC_PROV1;
	private	BigDecimal	R41_NO_OF_ACC1;
	private	BigDecimal	R41_90D_180D_PASTDUE;
	private	BigDecimal	R41_NON_ACCRUALS2;
	private	BigDecimal	R41_SPECIFIC_PROV2;
	private	BigDecimal	R41_NO_OF_ACC2;
	private	BigDecimal	R41_180D_ABOVE_PASTDUE;
	private	BigDecimal	R41_NON_ACCRUALS3;
	private	BigDecimal	R41_SPECIFIC_PROV3;
	private	BigDecimal	R41_NO_OF_ACC3;
	private	BigDecimal	R41_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R41_TOTAL_DUE_LOANS;
	private	BigDecimal	R41_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R41_VALUE_OF_COLLATERAL;
	private	BigDecimal	R41_TOTAL_VALUE_NPL;
	private	BigDecimal	R41_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R41_SPECIFIC_PROV_NPL;
	private	String	R42_PRODUCT;
	private	BigDecimal	R42_30D_90D_PASTDUE;
	private	BigDecimal	R42_NON_PERFORM_LOANS;
	private	BigDecimal	R42_NON_ACCRUALS1;
	private	BigDecimal	R42_SPECIFIC_PROV1;
	private	BigDecimal	R42_NO_OF_ACC1;
	private	BigDecimal	R42_90D_180D_PASTDUE;
	private	BigDecimal	R42_NON_ACCRUALS2;
	private	BigDecimal	R42_SPECIFIC_PROV2;
	private	BigDecimal	R42_NO_OF_ACC2;
	private	BigDecimal	R42_180D_ABOVE_PASTDUE;
	private	BigDecimal	R42_NON_ACCRUALS3;
	private	BigDecimal	R42_SPECIFIC_PROV3;
	private	BigDecimal	R42_NO_OF_ACC3;
	private	BigDecimal	R42_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R42_TOTAL_DUE_LOANS;
	private	BigDecimal	R42_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R42_VALUE_OF_COLLATERAL;
	private	BigDecimal	R42_TOTAL_VALUE_NPL;
	private	BigDecimal	R42_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R42_SPECIFIC_PROV_NPL;
	private	String	R43_PRODUCT;
	private	BigDecimal	R43_30D_90D_PASTDUE;
	private	BigDecimal	R43_NON_PERFORM_LOANS;
	private	BigDecimal	R43_NON_ACCRUALS1;
	private	BigDecimal	R43_SPECIFIC_PROV1;
	private	BigDecimal	R43_NO_OF_ACC1;
	private	BigDecimal	R43_90D_180D_PASTDUE;
	private	BigDecimal	R43_NON_ACCRUALS2;
	private	BigDecimal	R43_SPECIFIC_PROV2;
	private	BigDecimal	R43_NO_OF_ACC2;
	private	BigDecimal	R43_180D_ABOVE_PASTDUE;
	private	BigDecimal	R43_NON_ACCRUALS3;
	private	BigDecimal	R43_SPECIFIC_PROV3;
	private	BigDecimal	R43_NO_OF_ACC3;
	private	BigDecimal	R43_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R43_TOTAL_DUE_LOANS;
	private	BigDecimal	R43_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R43_VALUE_OF_COLLATERAL;
	private	BigDecimal	R43_TOTAL_VALUE_NPL;
	private	BigDecimal	R43_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R43_SPECIFIC_PROV_NPL;
	private	String	R44_PRODUCT;
	private	BigDecimal	R44_30D_90D_PASTDUE;
	private	BigDecimal	R44_NON_PERFORM_LOANS;
	private	BigDecimal	R44_NON_ACCRUALS1;
	private	BigDecimal	R44_SPECIFIC_PROV1;
	private	BigDecimal	R44_NO_OF_ACC1;
	private	BigDecimal	R44_90D_180D_PASTDUE;
	private	BigDecimal	R44_NON_ACCRUALS2;
	private	BigDecimal	R44_SPECIFIC_PROV2;
	private	BigDecimal	R44_NO_OF_ACC2;
	private	BigDecimal	R44_180D_ABOVE_PASTDUE;
	private	BigDecimal	R44_NON_ACCRUALS3;
	private	BigDecimal	R44_SPECIFIC_PROV3;
	private	BigDecimal	R44_NO_OF_ACC3;
	private	BigDecimal	R44_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R44_TOTAL_DUE_LOANS;
	private	BigDecimal	R44_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R44_VALUE_OF_COLLATERAL;
	private	BigDecimal	R44_TOTAL_VALUE_NPL;
	private	BigDecimal	R44_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R44_SPECIFIC_PROV_NPL;
	private	String	R45_PRODUCT;
	private	BigDecimal	R45_30D_90D_PASTDUE;
	private	BigDecimal	R45_NON_PERFORM_LOANS;
	private	BigDecimal	R45_NON_ACCRUALS1;
	private	BigDecimal	R45_SPECIFIC_PROV1;
	private	BigDecimal	R45_NO_OF_ACC1;
	private	BigDecimal	R45_90D_180D_PASTDUE;
	private	BigDecimal	R45_NON_ACCRUALS2;
	private	BigDecimal	R45_SPECIFIC_PROV2;
	private	BigDecimal	R45_NO_OF_ACC2;
	private	BigDecimal	R45_180D_ABOVE_PASTDUE;
	private	BigDecimal	R45_NON_ACCRUALS3;
	private	BigDecimal	R45_SPECIFIC_PROV3;
	private	BigDecimal	R45_NO_OF_ACC3;
	private	BigDecimal	R45_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R45_TOTAL_DUE_LOANS;
	private	BigDecimal	R45_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R45_VALUE_OF_COLLATERAL;
	private	BigDecimal	R45_TOTAL_VALUE_NPL;
	private	BigDecimal	R45_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R45_SPECIFIC_PROV_NPL;
	private	String	R46_PRODUCT;
	private	BigDecimal	R46_30D_90D_PASTDUE;
	private	BigDecimal	R46_NON_PERFORM_LOANS;
	private	BigDecimal	R46_NON_ACCRUALS1;
	private	BigDecimal	R46_SPECIFIC_PROV1;
	private	BigDecimal	R46_NO_OF_ACC1;
	private	BigDecimal	R46_90D_180D_PASTDUE;
	private	BigDecimal	R46_NON_ACCRUALS2;
	private	BigDecimal	R46_SPECIFIC_PROV2;
	private	BigDecimal	R46_NO_OF_ACC2;
	private	BigDecimal	R46_180D_ABOVE_PASTDUE;
	private	BigDecimal	R46_NON_ACCRUALS3;
	private	BigDecimal	R46_SPECIFIC_PROV3;
	private	BigDecimal	R46_NO_OF_ACC3;
	private	BigDecimal	R46_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R46_TOTAL_DUE_LOANS;
	private	BigDecimal	R46_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R46_VALUE_OF_COLLATERAL;
	private	BigDecimal	R46_TOTAL_VALUE_NPL;
	private	BigDecimal	R46_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R46_SPECIFIC_PROV_NPL;
	private	String	R47_PRODUCT;
	private	BigDecimal	R47_30D_90D_PASTDUE;
	private	BigDecimal	R47_NON_PERFORM_LOANS;
	private	BigDecimal	R47_NON_ACCRUALS1;
	private	BigDecimal	R47_SPECIFIC_PROV1;
	private	BigDecimal	R47_NO_OF_ACC1;
	private	BigDecimal	R47_90D_180D_PASTDUE;
	private	BigDecimal	R47_NON_ACCRUALS2;
	private	BigDecimal	R47_SPECIFIC_PROV2;
	private	BigDecimal	R47_NO_OF_ACC2;
	private	BigDecimal	R47_180D_ABOVE_PASTDUE;
	private	BigDecimal	R47_NON_ACCRUALS3;
	private	BigDecimal	R47_SPECIFIC_PROV3;
	private	BigDecimal	R47_NO_OF_ACC3;
	private	BigDecimal	R47_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R47_TOTAL_DUE_LOANS;
	private	BigDecimal	R47_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R47_VALUE_OF_COLLATERAL;
	private	BigDecimal	R47_TOTAL_VALUE_NPL;
	private	BigDecimal	R47_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R47_SPECIFIC_PROV_NPL;
	private	String	R48_PRODUCT;
	private	BigDecimal	R48_30D_90D_PASTDUE;
	private	BigDecimal	R48_NON_PERFORM_LOANS;
	private	BigDecimal	R48_NON_ACCRUALS1;
	private	BigDecimal	R48_SPECIFIC_PROV1;
	private	BigDecimal	R48_NO_OF_ACC1;
	private	BigDecimal	R48_90D_180D_PASTDUE;
	private	BigDecimal	R48_NON_ACCRUALS2;
	private	BigDecimal	R48_SPECIFIC_PROV2;
	private	BigDecimal	R48_NO_OF_ACC2;
	private	BigDecimal	R48_180D_ABOVE_PASTDUE;
	private	BigDecimal	R48_NON_ACCRUALS3;
	private	BigDecimal	R48_SPECIFIC_PROV3;
	private	BigDecimal	R48_NO_OF_ACC3;
	private	BigDecimal	R48_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R48_TOTAL_DUE_LOANS;
	private	BigDecimal	R48_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R48_VALUE_OF_COLLATERAL;
	private	BigDecimal	R48_TOTAL_VALUE_NPL;
	private	BigDecimal	R48_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R48_SPECIFIC_PROV_NPL;
	private	String	R49_PRODUCT;
	private	BigDecimal	R49_30D_90D_PASTDUE;
	private	BigDecimal	R49_NON_PERFORM_LOANS;
	private	BigDecimal	R49_NON_ACCRUALS1;
	private	BigDecimal	R49_SPECIFIC_PROV1;
	private	BigDecimal	R49_NO_OF_ACC1;
	private	BigDecimal	R49_90D_180D_PASTDUE;
	private	BigDecimal	R49_NON_ACCRUALS2;
	private	BigDecimal	R49_SPECIFIC_PROV2;
	private	BigDecimal	R49_NO_OF_ACC2;
	private	BigDecimal	R49_180D_ABOVE_PASTDUE;
	private	BigDecimal	R49_NON_ACCRUALS3;
	private	BigDecimal	R49_SPECIFIC_PROV3;
	private	BigDecimal	R49_NO_OF_ACC3;
	private	BigDecimal	R49_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R49_TOTAL_DUE_LOANS;
	private	BigDecimal	R49_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R49_VALUE_OF_COLLATERAL;
	private	BigDecimal	R49_TOTAL_VALUE_NPL;
	private	BigDecimal	R49_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R49_SPECIFIC_PROV_NPL;
	private	String	R50_PRODUCT;
	private	BigDecimal	R50_30D_90D_PASTDUE;
	private	BigDecimal	R50_NON_PERFORM_LOANS;
	private	BigDecimal	R50_NON_ACCRUALS1;
	private	BigDecimal	R50_SPECIFIC_PROV1;
	private	BigDecimal	R50_NO_OF_ACC1;
	private	BigDecimal	R50_90D_180D_PASTDUE;
	private	BigDecimal	R50_NON_ACCRUALS2;
	private	BigDecimal	R50_SPECIFIC_PROV2;
	private	BigDecimal	R50_NO_OF_ACC2;
	private	BigDecimal	R50_180D_ABOVE_PASTDUE;
	private	BigDecimal	R50_NON_ACCRUALS3;
	private	BigDecimal	R50_SPECIFIC_PROV3;
	private	BigDecimal	R50_NO_OF_ACC3;
	private	BigDecimal	R50_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R50_TOTAL_DUE_LOANS;
	private	BigDecimal	R50_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R50_VALUE_OF_COLLATERAL;
	private	BigDecimal	R50_TOTAL_VALUE_NPL;
	private	BigDecimal	R50_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R50_SPECIFIC_PROV_NPL;
	private	String	R51_PRODUCT;
	private	BigDecimal	R51_30D_90D_PASTDUE;
	private	BigDecimal	R51_NON_PERFORM_LOANS;
	private	BigDecimal	R51_NON_ACCRUALS1;
	private	BigDecimal	R51_SPECIFIC_PROV1;
	private	BigDecimal	R51_NO_OF_ACC1;
	private	BigDecimal	R51_90D_180D_PASTDUE;
	private	BigDecimal	R51_NON_ACCRUALS2;
	private	BigDecimal	R51_SPECIFIC_PROV2;
	private	BigDecimal	R51_NO_OF_ACC2;
	private	BigDecimal	R51_180D_ABOVE_PASTDUE;
	private	BigDecimal	R51_NON_ACCRUALS3;
	private	BigDecimal	R51_SPECIFIC_PROV3;
	private	BigDecimal	R51_NO_OF_ACC3;
	private	BigDecimal	R51_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R51_TOTAL_DUE_LOANS;
	private	BigDecimal	R51_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R51_VALUE_OF_COLLATERAL;
	private	BigDecimal	R51_TOTAL_VALUE_NPL;
	private	BigDecimal	R51_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R51_SPECIFIC_PROV_NPL;
	private	String	R52_PRODUCT;
	private	BigDecimal	R52_30D_90D_PASTDUE;
	private	BigDecimal	R52_NON_PERFORM_LOANS;
	private	BigDecimal	R52_NON_ACCRUALS1;
	private	BigDecimal	R52_SPECIFIC_PROV1;
	private	BigDecimal	R52_NO_OF_ACC1;
	private	BigDecimal	R52_90D_180D_PASTDUE;
	private	BigDecimal	R52_NON_ACCRUALS2;
	private	BigDecimal	R52_SPECIFIC_PROV2;
	private	BigDecimal	R52_NO_OF_ACC2;
	private	BigDecimal	R52_180D_ABOVE_PASTDUE;
	private	BigDecimal	R52_NON_ACCRUALS3;
	private	BigDecimal	R52_SPECIFIC_PROV3;
	private	BigDecimal	R52_NO_OF_ACC3;
	private	BigDecimal	R52_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R52_TOTAL_DUE_LOANS;
	private	BigDecimal	R52_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R52_VALUE_OF_COLLATERAL;
	private	BigDecimal	R52_TOTAL_VALUE_NPL;
	private	BigDecimal	R52_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R52_SPECIFIC_PROV_NPL;
	private	String	R53_PRODUCT;
	private	BigDecimal	R53_30D_90D_PASTDUE;
	private	BigDecimal	R53_NON_PERFORM_LOANS;
	private	BigDecimal	R53_NON_ACCRUALS1;
	private	BigDecimal	R53_SPECIFIC_PROV1;
	private	BigDecimal	R53_NO_OF_ACC1;
	private	BigDecimal	R53_90D_180D_PASTDUE;
	private	BigDecimal	R53_NON_ACCRUALS2;
	private	BigDecimal	R53_SPECIFIC_PROV2;
	private	BigDecimal	R53_NO_OF_ACC2;
	private	BigDecimal	R53_180D_ABOVE_PASTDUE;
	private	BigDecimal	R53_NON_ACCRUALS3;
	private	BigDecimal	R53_SPECIFIC_PROV3;
	private	BigDecimal	R53_NO_OF_ACC3;
	private	BigDecimal	R53_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R53_TOTAL_DUE_LOANS;
	private	BigDecimal	R53_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R53_VALUE_OF_COLLATERAL;
	private	BigDecimal	R53_TOTAL_VALUE_NPL;
	private	BigDecimal	R53_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R53_SPECIFIC_PROV_NPL;
	private	String	R54_PRODUCT;
	private	BigDecimal	R54_30D_90D_PASTDUE;
	private	BigDecimal	R54_NON_PERFORM_LOANS;
	private	BigDecimal	R54_NON_ACCRUALS1;
	private	BigDecimal	R54_SPECIFIC_PROV1;
	private	BigDecimal	R54_NO_OF_ACC1;
	private	BigDecimal	R54_90D_180D_PASTDUE;
	private	BigDecimal	R54_NON_ACCRUALS2;
	private	BigDecimal	R54_SPECIFIC_PROV2;
	private	BigDecimal	R54_NO_OF_ACC2;
	private	BigDecimal	R54_180D_ABOVE_PASTDUE;
	private	BigDecimal	R54_NON_ACCRUALS3;
	private	BigDecimal	R54_SPECIFIC_PROV3;
	private	BigDecimal	R54_NO_OF_ACC3;
	private	BigDecimal	R54_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R54_TOTAL_DUE_LOANS;
	private	BigDecimal	R54_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R54_VALUE_OF_COLLATERAL;
	private	BigDecimal	R54_TOTAL_VALUE_NPL;
	private	BigDecimal	R54_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R54_SPECIFIC_PROV_NPL;
	private	String	R55_PRODUCT;
	private	BigDecimal	R55_30D_90D_PASTDUE;
	private	BigDecimal	R55_NON_PERFORM_LOANS;
	private	BigDecimal	R55_NON_ACCRUALS1;
	private	BigDecimal	R55_SPECIFIC_PROV1;
	private	BigDecimal	R55_NO_OF_ACC1;
	private	BigDecimal	R55_90D_180D_PASTDUE;
	private	BigDecimal	R55_NON_ACCRUALS2;
	private	BigDecimal	R55_SPECIFIC_PROV2;
	private	BigDecimal	R55_NO_OF_ACC2;
	private	BigDecimal	R55_180D_ABOVE_PASTDUE;
	private	BigDecimal	R55_NON_ACCRUALS3;
	private	BigDecimal	R55_SPECIFIC_PROV3;
	private	BigDecimal	R55_NO_OF_ACC3;
	private	BigDecimal	R55_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R55_TOTAL_DUE_LOANS;
	private	BigDecimal	R55_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R55_VALUE_OF_COLLATERAL;
	private	BigDecimal	R55_TOTAL_VALUE_NPL;
	private	BigDecimal	R55_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R55_SPECIFIC_PROV_NPL;
	private	String	R56_PRODUCT;
	private	BigDecimal	R56_30D_90D_PASTDUE;
	private	BigDecimal	R56_NON_PERFORM_LOANS;
	private	BigDecimal	R56_NON_ACCRUALS1;
	private	BigDecimal	R56_SPECIFIC_PROV1;
	private	BigDecimal	R56_NO_OF_ACC1;
	private	BigDecimal	R56_90D_180D_PASTDUE;
	private	BigDecimal	R56_NON_ACCRUALS2;
	private	BigDecimal	R56_SPECIFIC_PROV2;
	private	BigDecimal	R56_NO_OF_ACC2;
	private	BigDecimal	R56_180D_ABOVE_PASTDUE;
	private	BigDecimal	R56_NON_ACCRUALS3;
	private	BigDecimal	R56_SPECIFIC_PROV3;
	private	BigDecimal	R56_NO_OF_ACC3;
	private	BigDecimal	R56_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R56_TOTAL_DUE_LOANS;
	private	BigDecimal	R56_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R56_VALUE_OF_COLLATERAL;
	private	BigDecimal	R56_TOTAL_VALUE_NPL;
	private	BigDecimal	R56_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R56_SPECIFIC_PROV_NPL;
	private	String	R57_PRODUCT;
	private	BigDecimal	R57_30D_90D_PASTDUE;
	private	BigDecimal	R57_NON_PERFORM_LOANS;
	private	BigDecimal	R57_NON_ACCRUALS1;
	private	BigDecimal	R57_SPECIFIC_PROV1;
	private	BigDecimal	R57_NO_OF_ACC1;
	private	BigDecimal	R57_90D_180D_PASTDUE;
	private	BigDecimal	R57_NON_ACCRUALS2;
	private	BigDecimal	R57_SPECIFIC_PROV2;
	private	BigDecimal	R57_NO_OF_ACC2;
	private	BigDecimal	R57_180D_ABOVE_PASTDUE;
	private	BigDecimal	R57_NON_ACCRUALS3;
	private	BigDecimal	R57_SPECIFIC_PROV3;
	private	BigDecimal	R57_NO_OF_ACC3;
	private	BigDecimal	R57_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R57_TOTAL_DUE_LOANS;
	private	BigDecimal	R57_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R57_VALUE_OF_COLLATERAL;
	private	BigDecimal	R57_TOTAL_VALUE_NPL;
	private	BigDecimal	R57_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R57_SPECIFIC_PROV_NPL;
	private	String	R58_PRODUCT;
	private	BigDecimal	R58_30D_90D_PASTDUE;
	private	BigDecimal	R58_NON_PERFORM_LOANS;
	private	BigDecimal	R58_NON_ACCRUALS1;
	private	BigDecimal	R58_SPECIFIC_PROV1;
	private	BigDecimal	R58_NO_OF_ACC1;
	private	BigDecimal	R58_90D_180D_PASTDUE;
	private	BigDecimal	R58_NON_ACCRUALS2;
	private	BigDecimal	R58_SPECIFIC_PROV2;
	private	BigDecimal	R58_NO_OF_ACC2;
	private	BigDecimal	R58_180D_ABOVE_PASTDUE;
	private	BigDecimal	R58_NON_ACCRUALS3;
	private	BigDecimal	R58_SPECIFIC_PROV3;
	private	BigDecimal	R58_NO_OF_ACC3;
	private	BigDecimal	R58_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R58_TOTAL_DUE_LOANS;
	private	BigDecimal	R58_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R58_VALUE_OF_COLLATERAL;
	private	BigDecimal	R58_TOTAL_VALUE_NPL;
	private	BigDecimal	R58_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R58_SPECIFIC_PROV_NPL;
	private	String	R59_PRODUCT;
	private	BigDecimal	R59_30D_90D_PASTDUE;
	private	BigDecimal	R59_NON_PERFORM_LOANS;
	private	BigDecimal	R59_NON_ACCRUALS1;
	private	BigDecimal	R59_SPECIFIC_PROV1;
	private	BigDecimal	R59_NO_OF_ACC1;
	private	BigDecimal	R59_90D_180D_PASTDUE;
	private	BigDecimal	R59_NON_ACCRUALS2;
	private	BigDecimal	R59_SPECIFIC_PROV2;
	private	BigDecimal	R59_NO_OF_ACC2;
	private	BigDecimal	R59_180D_ABOVE_PASTDUE;
	private	BigDecimal	R59_NON_ACCRUALS3;
	private	BigDecimal	R59_SPECIFIC_PROV3;
	private	BigDecimal	R59_NO_OF_ACC3;
	private	BigDecimal	R59_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R59_TOTAL_DUE_LOANS;
	private	BigDecimal	R59_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R59_VALUE_OF_COLLATERAL;
	private	BigDecimal	R59_TOTAL_VALUE_NPL;
	private	BigDecimal	R59_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R59_SPECIFIC_PROV_NPL;
	private	String	R60_PRODUCT;
	private	BigDecimal	R60_30D_90D_PASTDUE;
	private	BigDecimal	R60_NON_PERFORM_LOANS;
	private	BigDecimal	R60_NON_ACCRUALS1;
	private	BigDecimal	R60_SPECIFIC_PROV1;
	private	BigDecimal	R60_NO_OF_ACC1;
	private	BigDecimal	R60_90D_180D_PASTDUE;
	private	BigDecimal	R60_NON_ACCRUALS2;
	private	BigDecimal	R60_SPECIFIC_PROV2;
	private	BigDecimal	R60_NO_OF_ACC2;
	private	BigDecimal	R60_180D_ABOVE_PASTDUE;
	private	BigDecimal	R60_NON_ACCRUALS3;
	private	BigDecimal	R60_SPECIFIC_PROV3;
	private	BigDecimal	R60_NO_OF_ACC3;
	private	BigDecimal	R60_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R60_TOTAL_DUE_LOANS;
	private	BigDecimal	R60_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R60_VALUE_OF_COLLATERAL;
	private	BigDecimal	R60_TOTAL_VALUE_NPL;
	private	BigDecimal	R60_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R60_SPECIFIC_PROV_NPL;
	private	String	R61_PRODUCT;
	private	BigDecimal	R61_30D_90D_PASTDUE;
	private	BigDecimal	R61_NON_PERFORM_LOANS;
	private	BigDecimal	R61_NON_ACCRUALS1;
	private	BigDecimal	R61_SPECIFIC_PROV1;
	private	BigDecimal	R61_NO_OF_ACC1;
	private	BigDecimal	R61_90D_180D_PASTDUE;
	private	BigDecimal	R61_NON_ACCRUALS2;
	private	BigDecimal	R61_SPECIFIC_PROV2;
	private	BigDecimal	R61_NO_OF_ACC2;
	private	BigDecimal	R61_180D_ABOVE_PASTDUE;
	private	BigDecimal	R61_NON_ACCRUALS3;
	private	BigDecimal	R61_SPECIFIC_PROV3;
	private	BigDecimal	R61_NO_OF_ACC3;
	private	BigDecimal	R61_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R61_TOTAL_DUE_LOANS;
	private	BigDecimal	R61_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R61_VALUE_OF_COLLATERAL;
	private	BigDecimal	R61_TOTAL_VALUE_NPL;
	private	BigDecimal	R61_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R61_SPECIFIC_PROV_NPL;


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


    
    
   
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}





	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}





	public BigDecimal getR38_30D_90D_PASTDUE() {
		return R38_30D_90D_PASTDUE;
	}





	public void setR38_30D_90D_PASTDUE(BigDecimal r38_30d_90d_PASTDUE) {
		R38_30D_90D_PASTDUE = r38_30d_90d_PASTDUE;
	}





	public BigDecimal getR38_NON_PERFORM_LOANS() {
		return R38_NON_PERFORM_LOANS;
	}





	public void setR38_NON_PERFORM_LOANS(BigDecimal r38_NON_PERFORM_LOANS) {
		R38_NON_PERFORM_LOANS = r38_NON_PERFORM_LOANS;
	}





	public BigDecimal getR38_NON_ACCRUALS1() {
		return R38_NON_ACCRUALS1;
	}





	public void setR38_NON_ACCRUALS1(BigDecimal r38_NON_ACCRUALS1) {
		R38_NON_ACCRUALS1 = r38_NON_ACCRUALS1;
	}





	public BigDecimal getR38_SPECIFIC_PROV1() {
		return R38_SPECIFIC_PROV1;
	}





	public void setR38_SPECIFIC_PROV1(BigDecimal r38_SPECIFIC_PROV1) {
		R38_SPECIFIC_PROV1 = r38_SPECIFIC_PROV1;
	}





	public BigDecimal getR38_NO_OF_ACC1() {
		return R38_NO_OF_ACC1;
	}





	public void setR38_NO_OF_ACC1(BigDecimal r38_NO_OF_ACC1) {
		R38_NO_OF_ACC1 = r38_NO_OF_ACC1;
	}





	public BigDecimal getR38_90D_180D_PASTDUE() {
		return R38_90D_180D_PASTDUE;
	}





	public void setR38_90D_180D_PASTDUE(BigDecimal r38_90d_180d_PASTDUE) {
		R38_90D_180D_PASTDUE = r38_90d_180d_PASTDUE;
	}





	public BigDecimal getR38_NON_ACCRUALS2() {
		return R38_NON_ACCRUALS2;
	}





	public void setR38_NON_ACCRUALS2(BigDecimal r38_NON_ACCRUALS2) {
		R38_NON_ACCRUALS2 = r38_NON_ACCRUALS2;
	}





	public BigDecimal getR38_SPECIFIC_PROV2() {
		return R38_SPECIFIC_PROV2;
	}





	public void setR38_SPECIFIC_PROV2(BigDecimal r38_SPECIFIC_PROV2) {
		R38_SPECIFIC_PROV2 = r38_SPECIFIC_PROV2;
	}





	public BigDecimal getR38_NO_OF_ACC2() {
		return R38_NO_OF_ACC2;
	}





	public void setR38_NO_OF_ACC2(BigDecimal r38_NO_OF_ACC2) {
		R38_NO_OF_ACC2 = r38_NO_OF_ACC2;
	}





	public BigDecimal getR38_180D_ABOVE_PASTDUE() {
		return R38_180D_ABOVE_PASTDUE;
	}





	public void setR38_180D_ABOVE_PASTDUE(BigDecimal r38_180d_ABOVE_PASTDUE) {
		R38_180D_ABOVE_PASTDUE = r38_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR38_NON_ACCRUALS3() {
		return R38_NON_ACCRUALS3;
	}





	public void setR38_NON_ACCRUALS3(BigDecimal r38_NON_ACCRUALS3) {
		R38_NON_ACCRUALS3 = r38_NON_ACCRUALS3;
	}





	public BigDecimal getR38_SPECIFIC_PROV3() {
		return R38_SPECIFIC_PROV3;
	}





	public void setR38_SPECIFIC_PROV3(BigDecimal r38_SPECIFIC_PROV3) {
		R38_SPECIFIC_PROV3 = r38_SPECIFIC_PROV3;
	}





	public BigDecimal getR38_NO_OF_ACC3() {
		return R38_NO_OF_ACC3;
	}





	public void setR38_NO_OF_ACC3(BigDecimal r38_NO_OF_ACC3) {
		R38_NO_OF_ACC3 = r38_NO_OF_ACC3;
	}





	public BigDecimal getR38_TOTAL_NON_ACCRUAL() {
		return R38_TOTAL_NON_ACCRUAL;
	}





	public void setR38_TOTAL_NON_ACCRUAL(BigDecimal r38_TOTAL_NON_ACCRUAL) {
		R38_TOTAL_NON_ACCRUAL = r38_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR38_TOTAL_DUE_LOANS() {
		return R38_TOTAL_DUE_LOANS;
	}





	public void setR38_TOTAL_DUE_LOANS(BigDecimal r38_TOTAL_DUE_LOANS) {
		R38_TOTAL_DUE_LOANS = r38_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR38_TOTAL_PERFORMING_LOAN() {
		return R38_TOTAL_PERFORMING_LOAN;
	}





	public void setR38_TOTAL_PERFORMING_LOAN(BigDecimal r38_TOTAL_PERFORMING_LOAN) {
		R38_TOTAL_PERFORMING_LOAN = r38_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR38_VALUE_OF_COLLATERAL() {
		return R38_VALUE_OF_COLLATERAL;
	}





	public void setR38_VALUE_OF_COLLATERAL(BigDecimal r38_VALUE_OF_COLLATERAL) {
		R38_VALUE_OF_COLLATERAL = r38_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR38_TOTAL_VALUE_NPL() {
		return R38_TOTAL_VALUE_NPL;
	}





	public void setR38_TOTAL_VALUE_NPL(BigDecimal r38_TOTAL_VALUE_NPL) {
		R38_TOTAL_VALUE_NPL = r38_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR38_TOTAL_SPECIFIC_PROV() {
		return R38_TOTAL_SPECIFIC_PROV;
	}





	public void setR38_TOTAL_SPECIFIC_PROV(BigDecimal r38_TOTAL_SPECIFIC_PROV) {
		R38_TOTAL_SPECIFIC_PROV = r38_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR38_SPECIFIC_PROV_NPL() {
		return R38_SPECIFIC_PROV_NPL;
	}





	public void setR38_SPECIFIC_PROV_NPL(BigDecimal r38_SPECIFIC_PROV_NPL) {
		R38_SPECIFIC_PROV_NPL = r38_SPECIFIC_PROV_NPL;
	}





	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}





	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}





	public BigDecimal getR39_30D_90D_PASTDUE() {
		return R39_30D_90D_PASTDUE;
	}





	public void setR39_30D_90D_PASTDUE(BigDecimal r39_30d_90d_PASTDUE) {
		R39_30D_90D_PASTDUE = r39_30d_90d_PASTDUE;
	}





	public BigDecimal getR39_NON_PERFORM_LOANS() {
		return R39_NON_PERFORM_LOANS;
	}





	public void setR39_NON_PERFORM_LOANS(BigDecimal r39_NON_PERFORM_LOANS) {
		R39_NON_PERFORM_LOANS = r39_NON_PERFORM_LOANS;
	}





	public BigDecimal getR39_NON_ACCRUALS1() {
		return R39_NON_ACCRUALS1;
	}





	public void setR39_NON_ACCRUALS1(BigDecimal r39_NON_ACCRUALS1) {
		R39_NON_ACCRUALS1 = r39_NON_ACCRUALS1;
	}





	public BigDecimal getR39_SPECIFIC_PROV1() {
		return R39_SPECIFIC_PROV1;
	}





	public void setR39_SPECIFIC_PROV1(BigDecimal r39_SPECIFIC_PROV1) {
		R39_SPECIFIC_PROV1 = r39_SPECIFIC_PROV1;
	}





	public BigDecimal getR39_NO_OF_ACC1() {
		return R39_NO_OF_ACC1;
	}





	public void setR39_NO_OF_ACC1(BigDecimal r39_NO_OF_ACC1) {
		R39_NO_OF_ACC1 = r39_NO_OF_ACC1;
	}





	public BigDecimal getR39_90D_180D_PASTDUE() {
		return R39_90D_180D_PASTDUE;
	}





	public void setR39_90D_180D_PASTDUE(BigDecimal r39_90d_180d_PASTDUE) {
		R39_90D_180D_PASTDUE = r39_90d_180d_PASTDUE;
	}





	public BigDecimal getR39_NON_ACCRUALS2() {
		return R39_NON_ACCRUALS2;
	}





	public void setR39_NON_ACCRUALS2(BigDecimal r39_NON_ACCRUALS2) {
		R39_NON_ACCRUALS2 = r39_NON_ACCRUALS2;
	}





	public BigDecimal getR39_SPECIFIC_PROV2() {
		return R39_SPECIFIC_PROV2;
	}





	public void setR39_SPECIFIC_PROV2(BigDecimal r39_SPECIFIC_PROV2) {
		R39_SPECIFIC_PROV2 = r39_SPECIFIC_PROV2;
	}





	public BigDecimal getR39_NO_OF_ACC2() {
		return R39_NO_OF_ACC2;
	}





	public void setR39_NO_OF_ACC2(BigDecimal r39_NO_OF_ACC2) {
		R39_NO_OF_ACC2 = r39_NO_OF_ACC2;
	}





	public BigDecimal getR39_180D_ABOVE_PASTDUE() {
		return R39_180D_ABOVE_PASTDUE;
	}





	public void setR39_180D_ABOVE_PASTDUE(BigDecimal r39_180d_ABOVE_PASTDUE) {
		R39_180D_ABOVE_PASTDUE = r39_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR39_NON_ACCRUALS3() {
		return R39_NON_ACCRUALS3;
	}





	public void setR39_NON_ACCRUALS3(BigDecimal r39_NON_ACCRUALS3) {
		R39_NON_ACCRUALS3 = r39_NON_ACCRUALS3;
	}





	public BigDecimal getR39_SPECIFIC_PROV3() {
		return R39_SPECIFIC_PROV3;
	}





	public void setR39_SPECIFIC_PROV3(BigDecimal r39_SPECIFIC_PROV3) {
		R39_SPECIFIC_PROV3 = r39_SPECIFIC_PROV3;
	}





	public BigDecimal getR39_NO_OF_ACC3() {
		return R39_NO_OF_ACC3;
	}





	public void setR39_NO_OF_ACC3(BigDecimal r39_NO_OF_ACC3) {
		R39_NO_OF_ACC3 = r39_NO_OF_ACC3;
	}





	public BigDecimal getR39_TOTAL_NON_ACCRUAL() {
		return R39_TOTAL_NON_ACCRUAL;
	}





	public void setR39_TOTAL_NON_ACCRUAL(BigDecimal r39_TOTAL_NON_ACCRUAL) {
		R39_TOTAL_NON_ACCRUAL = r39_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR39_TOTAL_DUE_LOANS() {
		return R39_TOTAL_DUE_LOANS;
	}





	public void setR39_TOTAL_DUE_LOANS(BigDecimal r39_TOTAL_DUE_LOANS) {
		R39_TOTAL_DUE_LOANS = r39_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR39_TOTAL_PERFORMING_LOAN() {
		return R39_TOTAL_PERFORMING_LOAN;
	}





	public void setR39_TOTAL_PERFORMING_LOAN(BigDecimal r39_TOTAL_PERFORMING_LOAN) {
		R39_TOTAL_PERFORMING_LOAN = r39_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR39_VALUE_OF_COLLATERAL() {
		return R39_VALUE_OF_COLLATERAL;
	}





	public void setR39_VALUE_OF_COLLATERAL(BigDecimal r39_VALUE_OF_COLLATERAL) {
		R39_VALUE_OF_COLLATERAL = r39_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR39_TOTAL_VALUE_NPL() {
		return R39_TOTAL_VALUE_NPL;
	}





	public void setR39_TOTAL_VALUE_NPL(BigDecimal r39_TOTAL_VALUE_NPL) {
		R39_TOTAL_VALUE_NPL = r39_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR39_TOTAL_SPECIFIC_PROV() {
		return R39_TOTAL_SPECIFIC_PROV;
	}





	public void setR39_TOTAL_SPECIFIC_PROV(BigDecimal r39_TOTAL_SPECIFIC_PROV) {
		R39_TOTAL_SPECIFIC_PROV = r39_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR39_SPECIFIC_PROV_NPL() {
		return R39_SPECIFIC_PROV_NPL;
	}





	public void setR39_SPECIFIC_PROV_NPL(BigDecimal r39_SPECIFIC_PROV_NPL) {
		R39_SPECIFIC_PROV_NPL = r39_SPECIFIC_PROV_NPL;
	}





	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}





	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}





	public BigDecimal getR40_30D_90D_PASTDUE() {
		return R40_30D_90D_PASTDUE;
	}





	public void setR40_30D_90D_PASTDUE(BigDecimal r40_30d_90d_PASTDUE) {
		R40_30D_90D_PASTDUE = r40_30d_90d_PASTDUE;
	}





	public BigDecimal getR40_NON_PERFORM_LOANS() {
		return R40_NON_PERFORM_LOANS;
	}





	public void setR40_NON_PERFORM_LOANS(BigDecimal r40_NON_PERFORM_LOANS) {
		R40_NON_PERFORM_LOANS = r40_NON_PERFORM_LOANS;
	}





	public BigDecimal getR40_NON_ACCRUALS1() {
		return R40_NON_ACCRUALS1;
	}





	public void setR40_NON_ACCRUALS1(BigDecimal r40_NON_ACCRUALS1) {
		R40_NON_ACCRUALS1 = r40_NON_ACCRUALS1;
	}





	public BigDecimal getR40_SPECIFIC_PROV1() {
		return R40_SPECIFIC_PROV1;
	}





	public void setR40_SPECIFIC_PROV1(BigDecimal r40_SPECIFIC_PROV1) {
		R40_SPECIFIC_PROV1 = r40_SPECIFIC_PROV1;
	}





	public BigDecimal getR40_NO_OF_ACC1() {
		return R40_NO_OF_ACC1;
	}





	public void setR40_NO_OF_ACC1(BigDecimal r40_NO_OF_ACC1) {
		R40_NO_OF_ACC1 = r40_NO_OF_ACC1;
	}





	public BigDecimal getR40_90D_180D_PASTDUE() {
		return R40_90D_180D_PASTDUE;
	}





	public void setR40_90D_180D_PASTDUE(BigDecimal r40_90d_180d_PASTDUE) {
		R40_90D_180D_PASTDUE = r40_90d_180d_PASTDUE;
	}





	public BigDecimal getR40_NON_ACCRUALS2() {
		return R40_NON_ACCRUALS2;
	}





	public void setR40_NON_ACCRUALS2(BigDecimal r40_NON_ACCRUALS2) {
		R40_NON_ACCRUALS2 = r40_NON_ACCRUALS2;
	}





	public BigDecimal getR40_SPECIFIC_PROV2() {
		return R40_SPECIFIC_PROV2;
	}





	public void setR40_SPECIFIC_PROV2(BigDecimal r40_SPECIFIC_PROV2) {
		R40_SPECIFIC_PROV2 = r40_SPECIFIC_PROV2;
	}





	public BigDecimal getR40_NO_OF_ACC2() {
		return R40_NO_OF_ACC2;
	}





	public void setR40_NO_OF_ACC2(BigDecimal r40_NO_OF_ACC2) {
		R40_NO_OF_ACC2 = r40_NO_OF_ACC2;
	}





	public BigDecimal getR40_180D_ABOVE_PASTDUE() {
		return R40_180D_ABOVE_PASTDUE;
	}





	public void setR40_180D_ABOVE_PASTDUE(BigDecimal r40_180d_ABOVE_PASTDUE) {
		R40_180D_ABOVE_PASTDUE = r40_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR40_NON_ACCRUALS3() {
		return R40_NON_ACCRUALS3;
	}





	public void setR40_NON_ACCRUALS3(BigDecimal r40_NON_ACCRUALS3) {
		R40_NON_ACCRUALS3 = r40_NON_ACCRUALS3;
	}





	public BigDecimal getR40_SPECIFIC_PROV3() {
		return R40_SPECIFIC_PROV3;
	}





	public void setR40_SPECIFIC_PROV3(BigDecimal r40_SPECIFIC_PROV3) {
		R40_SPECIFIC_PROV3 = r40_SPECIFIC_PROV3;
	}





	public BigDecimal getR40_NO_OF_ACC3() {
		return R40_NO_OF_ACC3;
	}





	public void setR40_NO_OF_ACC3(BigDecimal r40_NO_OF_ACC3) {
		R40_NO_OF_ACC3 = r40_NO_OF_ACC3;
	}





	public BigDecimal getR40_TOTAL_NON_ACCRUAL() {
		return R40_TOTAL_NON_ACCRUAL;
	}





	public void setR40_TOTAL_NON_ACCRUAL(BigDecimal r40_TOTAL_NON_ACCRUAL) {
		R40_TOTAL_NON_ACCRUAL = r40_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR40_TOTAL_DUE_LOANS() {
		return R40_TOTAL_DUE_LOANS;
	}





	public void setR40_TOTAL_DUE_LOANS(BigDecimal r40_TOTAL_DUE_LOANS) {
		R40_TOTAL_DUE_LOANS = r40_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR40_TOTAL_PERFORMING_LOAN() {
		return R40_TOTAL_PERFORMING_LOAN;
	}





	public void setR40_TOTAL_PERFORMING_LOAN(BigDecimal r40_TOTAL_PERFORMING_LOAN) {
		R40_TOTAL_PERFORMING_LOAN = r40_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR40_VALUE_OF_COLLATERAL() {
		return R40_VALUE_OF_COLLATERAL;
	}





	public void setR40_VALUE_OF_COLLATERAL(BigDecimal r40_VALUE_OF_COLLATERAL) {
		R40_VALUE_OF_COLLATERAL = r40_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR40_TOTAL_VALUE_NPL() {
		return R40_TOTAL_VALUE_NPL;
	}





	public void setR40_TOTAL_VALUE_NPL(BigDecimal r40_TOTAL_VALUE_NPL) {
		R40_TOTAL_VALUE_NPL = r40_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR40_TOTAL_SPECIFIC_PROV() {
		return R40_TOTAL_SPECIFIC_PROV;
	}





	public void setR40_TOTAL_SPECIFIC_PROV(BigDecimal r40_TOTAL_SPECIFIC_PROV) {
		R40_TOTAL_SPECIFIC_PROV = r40_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR40_SPECIFIC_PROV_NPL() {
		return R40_SPECIFIC_PROV_NPL;
	}





	public void setR40_SPECIFIC_PROV_NPL(BigDecimal r40_SPECIFIC_PROV_NPL) {
		R40_SPECIFIC_PROV_NPL = r40_SPECIFIC_PROV_NPL;
	}





	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}





	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}





	public BigDecimal getR41_30D_90D_PASTDUE() {
		return R41_30D_90D_PASTDUE;
	}





	public void setR41_30D_90D_PASTDUE(BigDecimal r41_30d_90d_PASTDUE) {
		R41_30D_90D_PASTDUE = r41_30d_90d_PASTDUE;
	}





	public BigDecimal getR41_NON_PERFORM_LOANS() {
		return R41_NON_PERFORM_LOANS;
	}





	public void setR41_NON_PERFORM_LOANS(BigDecimal r41_NON_PERFORM_LOANS) {
		R41_NON_PERFORM_LOANS = r41_NON_PERFORM_LOANS;
	}





	public BigDecimal getR41_NON_ACCRUALS1() {
		return R41_NON_ACCRUALS1;
	}





	public void setR41_NON_ACCRUALS1(BigDecimal r41_NON_ACCRUALS1) {
		R41_NON_ACCRUALS1 = r41_NON_ACCRUALS1;
	}





	public BigDecimal getR41_SPECIFIC_PROV1() {
		return R41_SPECIFIC_PROV1;
	}





	public void setR41_SPECIFIC_PROV1(BigDecimal r41_SPECIFIC_PROV1) {
		R41_SPECIFIC_PROV1 = r41_SPECIFIC_PROV1;
	}





	public BigDecimal getR41_NO_OF_ACC1() {
		return R41_NO_OF_ACC1;
	}





	public void setR41_NO_OF_ACC1(BigDecimal r41_NO_OF_ACC1) {
		R41_NO_OF_ACC1 = r41_NO_OF_ACC1;
	}





	public BigDecimal getR41_90D_180D_PASTDUE() {
		return R41_90D_180D_PASTDUE;
	}





	public void setR41_90D_180D_PASTDUE(BigDecimal r41_90d_180d_PASTDUE) {
		R41_90D_180D_PASTDUE = r41_90d_180d_PASTDUE;
	}





	public BigDecimal getR41_NON_ACCRUALS2() {
		return R41_NON_ACCRUALS2;
	}





	public void setR41_NON_ACCRUALS2(BigDecimal r41_NON_ACCRUALS2) {
		R41_NON_ACCRUALS2 = r41_NON_ACCRUALS2;
	}





	public BigDecimal getR41_SPECIFIC_PROV2() {
		return R41_SPECIFIC_PROV2;
	}





	public void setR41_SPECIFIC_PROV2(BigDecimal r41_SPECIFIC_PROV2) {
		R41_SPECIFIC_PROV2 = r41_SPECIFIC_PROV2;
	}





	public BigDecimal getR41_NO_OF_ACC2() {
		return R41_NO_OF_ACC2;
	}





	public void setR41_NO_OF_ACC2(BigDecimal r41_NO_OF_ACC2) {
		R41_NO_OF_ACC2 = r41_NO_OF_ACC2;
	}





	public BigDecimal getR41_180D_ABOVE_PASTDUE() {
		return R41_180D_ABOVE_PASTDUE;
	}





	public void setR41_180D_ABOVE_PASTDUE(BigDecimal r41_180d_ABOVE_PASTDUE) {
		R41_180D_ABOVE_PASTDUE = r41_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR41_NON_ACCRUALS3() {
		return R41_NON_ACCRUALS3;
	}





	public void setR41_NON_ACCRUALS3(BigDecimal r41_NON_ACCRUALS3) {
		R41_NON_ACCRUALS3 = r41_NON_ACCRUALS3;
	}





	public BigDecimal getR41_SPECIFIC_PROV3() {
		return R41_SPECIFIC_PROV3;
	}





	public void setR41_SPECIFIC_PROV3(BigDecimal r41_SPECIFIC_PROV3) {
		R41_SPECIFIC_PROV3 = r41_SPECIFIC_PROV3;
	}





	public BigDecimal getR41_NO_OF_ACC3() {
		return R41_NO_OF_ACC3;
	}





	public void setR41_NO_OF_ACC3(BigDecimal r41_NO_OF_ACC3) {
		R41_NO_OF_ACC3 = r41_NO_OF_ACC3;
	}





	public BigDecimal getR41_TOTAL_NON_ACCRUAL() {
		return R41_TOTAL_NON_ACCRUAL;
	}





	public void setR41_TOTAL_NON_ACCRUAL(BigDecimal r41_TOTAL_NON_ACCRUAL) {
		R41_TOTAL_NON_ACCRUAL = r41_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR41_TOTAL_DUE_LOANS() {
		return R41_TOTAL_DUE_LOANS;
	}





	public void setR41_TOTAL_DUE_LOANS(BigDecimal r41_TOTAL_DUE_LOANS) {
		R41_TOTAL_DUE_LOANS = r41_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR41_TOTAL_PERFORMING_LOAN() {
		return R41_TOTAL_PERFORMING_LOAN;
	}





	public void setR41_TOTAL_PERFORMING_LOAN(BigDecimal r41_TOTAL_PERFORMING_LOAN) {
		R41_TOTAL_PERFORMING_LOAN = r41_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR41_VALUE_OF_COLLATERAL() {
		return R41_VALUE_OF_COLLATERAL;
	}





	public void setR41_VALUE_OF_COLLATERAL(BigDecimal r41_VALUE_OF_COLLATERAL) {
		R41_VALUE_OF_COLLATERAL = r41_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR41_TOTAL_VALUE_NPL() {
		return R41_TOTAL_VALUE_NPL;
	}





	public void setR41_TOTAL_VALUE_NPL(BigDecimal r41_TOTAL_VALUE_NPL) {
		R41_TOTAL_VALUE_NPL = r41_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR41_TOTAL_SPECIFIC_PROV() {
		return R41_TOTAL_SPECIFIC_PROV;
	}





	public void setR41_TOTAL_SPECIFIC_PROV(BigDecimal r41_TOTAL_SPECIFIC_PROV) {
		R41_TOTAL_SPECIFIC_PROV = r41_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR41_SPECIFIC_PROV_NPL() {
		return R41_SPECIFIC_PROV_NPL;
	}





	public void setR41_SPECIFIC_PROV_NPL(BigDecimal r41_SPECIFIC_PROV_NPL) {
		R41_SPECIFIC_PROV_NPL = r41_SPECIFIC_PROV_NPL;
	}





	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}





	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}





	public BigDecimal getR42_30D_90D_PASTDUE() {
		return R42_30D_90D_PASTDUE;
	}





	public void setR42_30D_90D_PASTDUE(BigDecimal r42_30d_90d_PASTDUE) {
		R42_30D_90D_PASTDUE = r42_30d_90d_PASTDUE;
	}





	public BigDecimal getR42_NON_PERFORM_LOANS() {
		return R42_NON_PERFORM_LOANS;
	}





	public void setR42_NON_PERFORM_LOANS(BigDecimal r42_NON_PERFORM_LOANS) {
		R42_NON_PERFORM_LOANS = r42_NON_PERFORM_LOANS;
	}





	public BigDecimal getR42_NON_ACCRUALS1() {
		return R42_NON_ACCRUALS1;
	}





	public void setR42_NON_ACCRUALS1(BigDecimal r42_NON_ACCRUALS1) {
		R42_NON_ACCRUALS1 = r42_NON_ACCRUALS1;
	}





	public BigDecimal getR42_SPECIFIC_PROV1() {
		return R42_SPECIFIC_PROV1;
	}





	public void setR42_SPECIFIC_PROV1(BigDecimal r42_SPECIFIC_PROV1) {
		R42_SPECIFIC_PROV1 = r42_SPECIFIC_PROV1;
	}





	public BigDecimal getR42_NO_OF_ACC1() {
		return R42_NO_OF_ACC1;
	}





	public void setR42_NO_OF_ACC1(BigDecimal r42_NO_OF_ACC1) {
		R42_NO_OF_ACC1 = r42_NO_OF_ACC1;
	}





	public BigDecimal getR42_90D_180D_PASTDUE() {
		return R42_90D_180D_PASTDUE;
	}





	public void setR42_90D_180D_PASTDUE(BigDecimal r42_90d_180d_PASTDUE) {
		R42_90D_180D_PASTDUE = r42_90d_180d_PASTDUE;
	}





	public BigDecimal getR42_NON_ACCRUALS2() {
		return R42_NON_ACCRUALS2;
	}





	public void setR42_NON_ACCRUALS2(BigDecimal r42_NON_ACCRUALS2) {
		R42_NON_ACCRUALS2 = r42_NON_ACCRUALS2;
	}





	public BigDecimal getR42_SPECIFIC_PROV2() {
		return R42_SPECIFIC_PROV2;
	}





	public void setR42_SPECIFIC_PROV2(BigDecimal r42_SPECIFIC_PROV2) {
		R42_SPECIFIC_PROV2 = r42_SPECIFIC_PROV2;
	}





	public BigDecimal getR42_NO_OF_ACC2() {
		return R42_NO_OF_ACC2;
	}





	public void setR42_NO_OF_ACC2(BigDecimal r42_NO_OF_ACC2) {
		R42_NO_OF_ACC2 = r42_NO_OF_ACC2;
	}





	public BigDecimal getR42_180D_ABOVE_PASTDUE() {
		return R42_180D_ABOVE_PASTDUE;
	}





	public void setR42_180D_ABOVE_PASTDUE(BigDecimal r42_180d_ABOVE_PASTDUE) {
		R42_180D_ABOVE_PASTDUE = r42_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR42_NON_ACCRUALS3() {
		return R42_NON_ACCRUALS3;
	}





	public void setR42_NON_ACCRUALS3(BigDecimal r42_NON_ACCRUALS3) {
		R42_NON_ACCRUALS3 = r42_NON_ACCRUALS3;
	}





	public BigDecimal getR42_SPECIFIC_PROV3() {
		return R42_SPECIFIC_PROV3;
	}





	public void setR42_SPECIFIC_PROV3(BigDecimal r42_SPECIFIC_PROV3) {
		R42_SPECIFIC_PROV3 = r42_SPECIFIC_PROV3;
	}





	public BigDecimal getR42_NO_OF_ACC3() {
		return R42_NO_OF_ACC3;
	}





	public void setR42_NO_OF_ACC3(BigDecimal r42_NO_OF_ACC3) {
		R42_NO_OF_ACC3 = r42_NO_OF_ACC3;
	}





	public BigDecimal getR42_TOTAL_NON_ACCRUAL() {
		return R42_TOTAL_NON_ACCRUAL;
	}





	public void setR42_TOTAL_NON_ACCRUAL(BigDecimal r42_TOTAL_NON_ACCRUAL) {
		R42_TOTAL_NON_ACCRUAL = r42_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR42_TOTAL_DUE_LOANS() {
		return R42_TOTAL_DUE_LOANS;
	}





	public void setR42_TOTAL_DUE_LOANS(BigDecimal r42_TOTAL_DUE_LOANS) {
		R42_TOTAL_DUE_LOANS = r42_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR42_TOTAL_PERFORMING_LOAN() {
		return R42_TOTAL_PERFORMING_LOAN;
	}





	public void setR42_TOTAL_PERFORMING_LOAN(BigDecimal r42_TOTAL_PERFORMING_LOAN) {
		R42_TOTAL_PERFORMING_LOAN = r42_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR42_VALUE_OF_COLLATERAL() {
		return R42_VALUE_OF_COLLATERAL;
	}





	public void setR42_VALUE_OF_COLLATERAL(BigDecimal r42_VALUE_OF_COLLATERAL) {
		R42_VALUE_OF_COLLATERAL = r42_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR42_TOTAL_VALUE_NPL() {
		return R42_TOTAL_VALUE_NPL;
	}





	public void setR42_TOTAL_VALUE_NPL(BigDecimal r42_TOTAL_VALUE_NPL) {
		R42_TOTAL_VALUE_NPL = r42_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR42_TOTAL_SPECIFIC_PROV() {
		return R42_TOTAL_SPECIFIC_PROV;
	}





	public void setR42_TOTAL_SPECIFIC_PROV(BigDecimal r42_TOTAL_SPECIFIC_PROV) {
		R42_TOTAL_SPECIFIC_PROV = r42_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR42_SPECIFIC_PROV_NPL() {
		return R42_SPECIFIC_PROV_NPL;
	}





	public void setR42_SPECIFIC_PROV_NPL(BigDecimal r42_SPECIFIC_PROV_NPL) {
		R42_SPECIFIC_PROV_NPL = r42_SPECIFIC_PROV_NPL;
	}





	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}





	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}





	public BigDecimal getR43_30D_90D_PASTDUE() {
		return R43_30D_90D_PASTDUE;
	}





	public void setR43_30D_90D_PASTDUE(BigDecimal r43_30d_90d_PASTDUE) {
		R43_30D_90D_PASTDUE = r43_30d_90d_PASTDUE;
	}





	public BigDecimal getR43_NON_PERFORM_LOANS() {
		return R43_NON_PERFORM_LOANS;
	}





	public void setR43_NON_PERFORM_LOANS(BigDecimal r43_NON_PERFORM_LOANS) {
		R43_NON_PERFORM_LOANS = r43_NON_PERFORM_LOANS;
	}





	public BigDecimal getR43_NON_ACCRUALS1() {
		return R43_NON_ACCRUALS1;
	}





	public void setR43_NON_ACCRUALS1(BigDecimal r43_NON_ACCRUALS1) {
		R43_NON_ACCRUALS1 = r43_NON_ACCRUALS1;
	}





	public BigDecimal getR43_SPECIFIC_PROV1() {
		return R43_SPECIFIC_PROV1;
	}





	public void setR43_SPECIFIC_PROV1(BigDecimal r43_SPECIFIC_PROV1) {
		R43_SPECIFIC_PROV1 = r43_SPECIFIC_PROV1;
	}





	public BigDecimal getR43_NO_OF_ACC1() {
		return R43_NO_OF_ACC1;
	}





	public void setR43_NO_OF_ACC1(BigDecimal r43_NO_OF_ACC1) {
		R43_NO_OF_ACC1 = r43_NO_OF_ACC1;
	}





	public BigDecimal getR43_90D_180D_PASTDUE() {
		return R43_90D_180D_PASTDUE;
	}





	public void setR43_90D_180D_PASTDUE(BigDecimal r43_90d_180d_PASTDUE) {
		R43_90D_180D_PASTDUE = r43_90d_180d_PASTDUE;
	}





	public BigDecimal getR43_NON_ACCRUALS2() {
		return R43_NON_ACCRUALS2;
	}





	public void setR43_NON_ACCRUALS2(BigDecimal r43_NON_ACCRUALS2) {
		R43_NON_ACCRUALS2 = r43_NON_ACCRUALS2;
	}





	public BigDecimal getR43_SPECIFIC_PROV2() {
		return R43_SPECIFIC_PROV2;
	}





	public void setR43_SPECIFIC_PROV2(BigDecimal r43_SPECIFIC_PROV2) {
		R43_SPECIFIC_PROV2 = r43_SPECIFIC_PROV2;
	}





	public BigDecimal getR43_NO_OF_ACC2() {
		return R43_NO_OF_ACC2;
	}





	public void setR43_NO_OF_ACC2(BigDecimal r43_NO_OF_ACC2) {
		R43_NO_OF_ACC2 = r43_NO_OF_ACC2;
	}





	public BigDecimal getR43_180D_ABOVE_PASTDUE() {
		return R43_180D_ABOVE_PASTDUE;
	}





	public void setR43_180D_ABOVE_PASTDUE(BigDecimal r43_180d_ABOVE_PASTDUE) {
		R43_180D_ABOVE_PASTDUE = r43_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR43_NON_ACCRUALS3() {
		return R43_NON_ACCRUALS3;
	}





	public void setR43_NON_ACCRUALS3(BigDecimal r43_NON_ACCRUALS3) {
		R43_NON_ACCRUALS3 = r43_NON_ACCRUALS3;
	}





	public BigDecimal getR43_SPECIFIC_PROV3() {
		return R43_SPECIFIC_PROV3;
	}





	public void setR43_SPECIFIC_PROV3(BigDecimal r43_SPECIFIC_PROV3) {
		R43_SPECIFIC_PROV3 = r43_SPECIFIC_PROV3;
	}





	public BigDecimal getR43_NO_OF_ACC3() {
		return R43_NO_OF_ACC3;
	}





	public void setR43_NO_OF_ACC3(BigDecimal r43_NO_OF_ACC3) {
		R43_NO_OF_ACC3 = r43_NO_OF_ACC3;
	}





	public BigDecimal getR43_TOTAL_NON_ACCRUAL() {
		return R43_TOTAL_NON_ACCRUAL;
	}





	public void setR43_TOTAL_NON_ACCRUAL(BigDecimal r43_TOTAL_NON_ACCRUAL) {
		R43_TOTAL_NON_ACCRUAL = r43_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR43_TOTAL_DUE_LOANS() {
		return R43_TOTAL_DUE_LOANS;
	}





	public void setR43_TOTAL_DUE_LOANS(BigDecimal r43_TOTAL_DUE_LOANS) {
		R43_TOTAL_DUE_LOANS = r43_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR43_TOTAL_PERFORMING_LOAN() {
		return R43_TOTAL_PERFORMING_LOAN;
	}





	public void setR43_TOTAL_PERFORMING_LOAN(BigDecimal r43_TOTAL_PERFORMING_LOAN) {
		R43_TOTAL_PERFORMING_LOAN = r43_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR43_VALUE_OF_COLLATERAL() {
		return R43_VALUE_OF_COLLATERAL;
	}





	public void setR43_VALUE_OF_COLLATERAL(BigDecimal r43_VALUE_OF_COLLATERAL) {
		R43_VALUE_OF_COLLATERAL = r43_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR43_TOTAL_VALUE_NPL() {
		return R43_TOTAL_VALUE_NPL;
	}





	public void setR43_TOTAL_VALUE_NPL(BigDecimal r43_TOTAL_VALUE_NPL) {
		R43_TOTAL_VALUE_NPL = r43_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR43_TOTAL_SPECIFIC_PROV() {
		return R43_TOTAL_SPECIFIC_PROV;
	}





	public void setR43_TOTAL_SPECIFIC_PROV(BigDecimal r43_TOTAL_SPECIFIC_PROV) {
		R43_TOTAL_SPECIFIC_PROV = r43_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR43_SPECIFIC_PROV_NPL() {
		return R43_SPECIFIC_PROV_NPL;
	}





	public void setR43_SPECIFIC_PROV_NPL(BigDecimal r43_SPECIFIC_PROV_NPL) {
		R43_SPECIFIC_PROV_NPL = r43_SPECIFIC_PROV_NPL;
	}





	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}





	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}





	public BigDecimal getR44_30D_90D_PASTDUE() {
		return R44_30D_90D_PASTDUE;
	}





	public void setR44_30D_90D_PASTDUE(BigDecimal r44_30d_90d_PASTDUE) {
		R44_30D_90D_PASTDUE = r44_30d_90d_PASTDUE;
	}





	public BigDecimal getR44_NON_PERFORM_LOANS() {
		return R44_NON_PERFORM_LOANS;
	}





	public void setR44_NON_PERFORM_LOANS(BigDecimal r44_NON_PERFORM_LOANS) {
		R44_NON_PERFORM_LOANS = r44_NON_PERFORM_LOANS;
	}





	public BigDecimal getR44_NON_ACCRUALS1() {
		return R44_NON_ACCRUALS1;
	}





	public void setR44_NON_ACCRUALS1(BigDecimal r44_NON_ACCRUALS1) {
		R44_NON_ACCRUALS1 = r44_NON_ACCRUALS1;
	}





	public BigDecimal getR44_SPECIFIC_PROV1() {
		return R44_SPECIFIC_PROV1;
	}





	public void setR44_SPECIFIC_PROV1(BigDecimal r44_SPECIFIC_PROV1) {
		R44_SPECIFIC_PROV1 = r44_SPECIFIC_PROV1;
	}





	public BigDecimal getR44_NO_OF_ACC1() {
		return R44_NO_OF_ACC1;
	}





	public void setR44_NO_OF_ACC1(BigDecimal r44_NO_OF_ACC1) {
		R44_NO_OF_ACC1 = r44_NO_OF_ACC1;
	}





	public BigDecimal getR44_90D_180D_PASTDUE() {
		return R44_90D_180D_PASTDUE;
	}





	public void setR44_90D_180D_PASTDUE(BigDecimal r44_90d_180d_PASTDUE) {
		R44_90D_180D_PASTDUE = r44_90d_180d_PASTDUE;
	}





	public BigDecimal getR44_NON_ACCRUALS2() {
		return R44_NON_ACCRUALS2;
	}





	public void setR44_NON_ACCRUALS2(BigDecimal r44_NON_ACCRUALS2) {
		R44_NON_ACCRUALS2 = r44_NON_ACCRUALS2;
	}





	public BigDecimal getR44_SPECIFIC_PROV2() {
		return R44_SPECIFIC_PROV2;
	}





	public void setR44_SPECIFIC_PROV2(BigDecimal r44_SPECIFIC_PROV2) {
		R44_SPECIFIC_PROV2 = r44_SPECIFIC_PROV2;
	}





	public BigDecimal getR44_NO_OF_ACC2() {
		return R44_NO_OF_ACC2;
	}





	public void setR44_NO_OF_ACC2(BigDecimal r44_NO_OF_ACC2) {
		R44_NO_OF_ACC2 = r44_NO_OF_ACC2;
	}





	public BigDecimal getR44_180D_ABOVE_PASTDUE() {
		return R44_180D_ABOVE_PASTDUE;
	}





	public void setR44_180D_ABOVE_PASTDUE(BigDecimal r44_180d_ABOVE_PASTDUE) {
		R44_180D_ABOVE_PASTDUE = r44_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR44_NON_ACCRUALS3() {
		return R44_NON_ACCRUALS3;
	}





	public void setR44_NON_ACCRUALS3(BigDecimal r44_NON_ACCRUALS3) {
		R44_NON_ACCRUALS3 = r44_NON_ACCRUALS3;
	}





	public BigDecimal getR44_SPECIFIC_PROV3() {
		return R44_SPECIFIC_PROV3;
	}





	public void setR44_SPECIFIC_PROV3(BigDecimal r44_SPECIFIC_PROV3) {
		R44_SPECIFIC_PROV3 = r44_SPECIFIC_PROV3;
	}





	public BigDecimal getR44_NO_OF_ACC3() {
		return R44_NO_OF_ACC3;
	}





	public void setR44_NO_OF_ACC3(BigDecimal r44_NO_OF_ACC3) {
		R44_NO_OF_ACC3 = r44_NO_OF_ACC3;
	}





	public BigDecimal getR44_TOTAL_NON_ACCRUAL() {
		return R44_TOTAL_NON_ACCRUAL;
	}





	public void setR44_TOTAL_NON_ACCRUAL(BigDecimal r44_TOTAL_NON_ACCRUAL) {
		R44_TOTAL_NON_ACCRUAL = r44_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR44_TOTAL_DUE_LOANS() {
		return R44_TOTAL_DUE_LOANS;
	}





	public void setR44_TOTAL_DUE_LOANS(BigDecimal r44_TOTAL_DUE_LOANS) {
		R44_TOTAL_DUE_LOANS = r44_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR44_TOTAL_PERFORMING_LOAN() {
		return R44_TOTAL_PERFORMING_LOAN;
	}





	public void setR44_TOTAL_PERFORMING_LOAN(BigDecimal r44_TOTAL_PERFORMING_LOAN) {
		R44_TOTAL_PERFORMING_LOAN = r44_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR44_VALUE_OF_COLLATERAL() {
		return R44_VALUE_OF_COLLATERAL;
	}





	public void setR44_VALUE_OF_COLLATERAL(BigDecimal r44_VALUE_OF_COLLATERAL) {
		R44_VALUE_OF_COLLATERAL = r44_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR44_TOTAL_VALUE_NPL() {
		return R44_TOTAL_VALUE_NPL;
	}





	public void setR44_TOTAL_VALUE_NPL(BigDecimal r44_TOTAL_VALUE_NPL) {
		R44_TOTAL_VALUE_NPL = r44_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR44_TOTAL_SPECIFIC_PROV() {
		return R44_TOTAL_SPECIFIC_PROV;
	}





	public void setR44_TOTAL_SPECIFIC_PROV(BigDecimal r44_TOTAL_SPECIFIC_PROV) {
		R44_TOTAL_SPECIFIC_PROV = r44_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR44_SPECIFIC_PROV_NPL() {
		return R44_SPECIFIC_PROV_NPL;
	}





	public void setR44_SPECIFIC_PROV_NPL(BigDecimal r44_SPECIFIC_PROV_NPL) {
		R44_SPECIFIC_PROV_NPL = r44_SPECIFIC_PROV_NPL;
	}





	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}





	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}





	public BigDecimal getR45_30D_90D_PASTDUE() {
		return R45_30D_90D_PASTDUE;
	}





	public void setR45_30D_90D_PASTDUE(BigDecimal r45_30d_90d_PASTDUE) {
		R45_30D_90D_PASTDUE = r45_30d_90d_PASTDUE;
	}





	public BigDecimal getR45_NON_PERFORM_LOANS() {
		return R45_NON_PERFORM_LOANS;
	}





	public void setR45_NON_PERFORM_LOANS(BigDecimal r45_NON_PERFORM_LOANS) {
		R45_NON_PERFORM_LOANS = r45_NON_PERFORM_LOANS;
	}





	public BigDecimal getR45_NON_ACCRUALS1() {
		return R45_NON_ACCRUALS1;
	}





	public void setR45_NON_ACCRUALS1(BigDecimal r45_NON_ACCRUALS1) {
		R45_NON_ACCRUALS1 = r45_NON_ACCRUALS1;
	}





	public BigDecimal getR45_SPECIFIC_PROV1() {
		return R45_SPECIFIC_PROV1;
	}





	public void setR45_SPECIFIC_PROV1(BigDecimal r45_SPECIFIC_PROV1) {
		R45_SPECIFIC_PROV1 = r45_SPECIFIC_PROV1;
	}





	public BigDecimal getR45_NO_OF_ACC1() {
		return R45_NO_OF_ACC1;
	}





	public void setR45_NO_OF_ACC1(BigDecimal r45_NO_OF_ACC1) {
		R45_NO_OF_ACC1 = r45_NO_OF_ACC1;
	}





	public BigDecimal getR45_90D_180D_PASTDUE() {
		return R45_90D_180D_PASTDUE;
	}





	public void setR45_90D_180D_PASTDUE(BigDecimal r45_90d_180d_PASTDUE) {
		R45_90D_180D_PASTDUE = r45_90d_180d_PASTDUE;
	}





	public BigDecimal getR45_NON_ACCRUALS2() {
		return R45_NON_ACCRUALS2;
	}





	public void setR45_NON_ACCRUALS2(BigDecimal r45_NON_ACCRUALS2) {
		R45_NON_ACCRUALS2 = r45_NON_ACCRUALS2;
	}





	public BigDecimal getR45_SPECIFIC_PROV2() {
		return R45_SPECIFIC_PROV2;
	}





	public void setR45_SPECIFIC_PROV2(BigDecimal r45_SPECIFIC_PROV2) {
		R45_SPECIFIC_PROV2 = r45_SPECIFIC_PROV2;
	}





	public BigDecimal getR45_NO_OF_ACC2() {
		return R45_NO_OF_ACC2;
	}





	public void setR45_NO_OF_ACC2(BigDecimal r45_NO_OF_ACC2) {
		R45_NO_OF_ACC2 = r45_NO_OF_ACC2;
	}





	public BigDecimal getR45_180D_ABOVE_PASTDUE() {
		return R45_180D_ABOVE_PASTDUE;
	}





	public void setR45_180D_ABOVE_PASTDUE(BigDecimal r45_180d_ABOVE_PASTDUE) {
		R45_180D_ABOVE_PASTDUE = r45_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR45_NON_ACCRUALS3() {
		return R45_NON_ACCRUALS3;
	}





	public void setR45_NON_ACCRUALS3(BigDecimal r45_NON_ACCRUALS3) {
		R45_NON_ACCRUALS3 = r45_NON_ACCRUALS3;
	}





	public BigDecimal getR45_SPECIFIC_PROV3() {
		return R45_SPECIFIC_PROV3;
	}





	public void setR45_SPECIFIC_PROV3(BigDecimal r45_SPECIFIC_PROV3) {
		R45_SPECIFIC_PROV3 = r45_SPECIFIC_PROV3;
	}





	public BigDecimal getR45_NO_OF_ACC3() {
		return R45_NO_OF_ACC3;
	}





	public void setR45_NO_OF_ACC3(BigDecimal r45_NO_OF_ACC3) {
		R45_NO_OF_ACC3 = r45_NO_OF_ACC3;
	}





	public BigDecimal getR45_TOTAL_NON_ACCRUAL() {
		return R45_TOTAL_NON_ACCRUAL;
	}





	public void setR45_TOTAL_NON_ACCRUAL(BigDecimal r45_TOTAL_NON_ACCRUAL) {
		R45_TOTAL_NON_ACCRUAL = r45_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR45_TOTAL_DUE_LOANS() {
		return R45_TOTAL_DUE_LOANS;
	}





	public void setR45_TOTAL_DUE_LOANS(BigDecimal r45_TOTAL_DUE_LOANS) {
		R45_TOTAL_DUE_LOANS = r45_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR45_TOTAL_PERFORMING_LOAN() {
		return R45_TOTAL_PERFORMING_LOAN;
	}





	public void setR45_TOTAL_PERFORMING_LOAN(BigDecimal r45_TOTAL_PERFORMING_LOAN) {
		R45_TOTAL_PERFORMING_LOAN = r45_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR45_VALUE_OF_COLLATERAL() {
		return R45_VALUE_OF_COLLATERAL;
	}





	public void setR45_VALUE_OF_COLLATERAL(BigDecimal r45_VALUE_OF_COLLATERAL) {
		R45_VALUE_OF_COLLATERAL = r45_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR45_TOTAL_VALUE_NPL() {
		return R45_TOTAL_VALUE_NPL;
	}





	public void setR45_TOTAL_VALUE_NPL(BigDecimal r45_TOTAL_VALUE_NPL) {
		R45_TOTAL_VALUE_NPL = r45_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR45_TOTAL_SPECIFIC_PROV() {
		return R45_TOTAL_SPECIFIC_PROV;
	}





	public void setR45_TOTAL_SPECIFIC_PROV(BigDecimal r45_TOTAL_SPECIFIC_PROV) {
		R45_TOTAL_SPECIFIC_PROV = r45_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR45_SPECIFIC_PROV_NPL() {
		return R45_SPECIFIC_PROV_NPL;
	}





	public void setR45_SPECIFIC_PROV_NPL(BigDecimal r45_SPECIFIC_PROV_NPL) {
		R45_SPECIFIC_PROV_NPL = r45_SPECIFIC_PROV_NPL;
	}





	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}





	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}





	public BigDecimal getR46_30D_90D_PASTDUE() {
		return R46_30D_90D_PASTDUE;
	}





	public void setR46_30D_90D_PASTDUE(BigDecimal r46_30d_90d_PASTDUE) {
		R46_30D_90D_PASTDUE = r46_30d_90d_PASTDUE;
	}





	public BigDecimal getR46_NON_PERFORM_LOANS() {
		return R46_NON_PERFORM_LOANS;
	}





	public void setR46_NON_PERFORM_LOANS(BigDecimal r46_NON_PERFORM_LOANS) {
		R46_NON_PERFORM_LOANS = r46_NON_PERFORM_LOANS;
	}





	public BigDecimal getR46_NON_ACCRUALS1() {
		return R46_NON_ACCRUALS1;
	}





	public void setR46_NON_ACCRUALS1(BigDecimal r46_NON_ACCRUALS1) {
		R46_NON_ACCRUALS1 = r46_NON_ACCRUALS1;
	}





	public BigDecimal getR46_SPECIFIC_PROV1() {
		return R46_SPECIFIC_PROV1;
	}





	public void setR46_SPECIFIC_PROV1(BigDecimal r46_SPECIFIC_PROV1) {
		R46_SPECIFIC_PROV1 = r46_SPECIFIC_PROV1;
	}





	public BigDecimal getR46_NO_OF_ACC1() {
		return R46_NO_OF_ACC1;
	}





	public void setR46_NO_OF_ACC1(BigDecimal r46_NO_OF_ACC1) {
		R46_NO_OF_ACC1 = r46_NO_OF_ACC1;
	}





	public BigDecimal getR46_90D_180D_PASTDUE() {
		return R46_90D_180D_PASTDUE;
	}





	public void setR46_90D_180D_PASTDUE(BigDecimal r46_90d_180d_PASTDUE) {
		R46_90D_180D_PASTDUE = r46_90d_180d_PASTDUE;
	}





	public BigDecimal getR46_NON_ACCRUALS2() {
		return R46_NON_ACCRUALS2;
	}





	public void setR46_NON_ACCRUALS2(BigDecimal r46_NON_ACCRUALS2) {
		R46_NON_ACCRUALS2 = r46_NON_ACCRUALS2;
	}





	public BigDecimal getR46_SPECIFIC_PROV2() {
		return R46_SPECIFIC_PROV2;
	}





	public void setR46_SPECIFIC_PROV2(BigDecimal r46_SPECIFIC_PROV2) {
		R46_SPECIFIC_PROV2 = r46_SPECIFIC_PROV2;
	}





	public BigDecimal getR46_NO_OF_ACC2() {
		return R46_NO_OF_ACC2;
	}





	public void setR46_NO_OF_ACC2(BigDecimal r46_NO_OF_ACC2) {
		R46_NO_OF_ACC2 = r46_NO_OF_ACC2;
	}





	public BigDecimal getR46_180D_ABOVE_PASTDUE() {
		return R46_180D_ABOVE_PASTDUE;
	}





	public void setR46_180D_ABOVE_PASTDUE(BigDecimal r46_180d_ABOVE_PASTDUE) {
		R46_180D_ABOVE_PASTDUE = r46_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR46_NON_ACCRUALS3() {
		return R46_NON_ACCRUALS3;
	}





	public void setR46_NON_ACCRUALS3(BigDecimal r46_NON_ACCRUALS3) {
		R46_NON_ACCRUALS3 = r46_NON_ACCRUALS3;
	}





	public BigDecimal getR46_SPECIFIC_PROV3() {
		return R46_SPECIFIC_PROV3;
	}





	public void setR46_SPECIFIC_PROV3(BigDecimal r46_SPECIFIC_PROV3) {
		R46_SPECIFIC_PROV3 = r46_SPECIFIC_PROV3;
	}





	public BigDecimal getR46_NO_OF_ACC3() {
		return R46_NO_OF_ACC3;
	}





	public void setR46_NO_OF_ACC3(BigDecimal r46_NO_OF_ACC3) {
		R46_NO_OF_ACC3 = r46_NO_OF_ACC3;
	}





	public BigDecimal getR46_TOTAL_NON_ACCRUAL() {
		return R46_TOTAL_NON_ACCRUAL;
	}





	public void setR46_TOTAL_NON_ACCRUAL(BigDecimal r46_TOTAL_NON_ACCRUAL) {
		R46_TOTAL_NON_ACCRUAL = r46_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR46_TOTAL_DUE_LOANS() {
		return R46_TOTAL_DUE_LOANS;
	}





	public void setR46_TOTAL_DUE_LOANS(BigDecimal r46_TOTAL_DUE_LOANS) {
		R46_TOTAL_DUE_LOANS = r46_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR46_TOTAL_PERFORMING_LOAN() {
		return R46_TOTAL_PERFORMING_LOAN;
	}





	public void setR46_TOTAL_PERFORMING_LOAN(BigDecimal r46_TOTAL_PERFORMING_LOAN) {
		R46_TOTAL_PERFORMING_LOAN = r46_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR46_VALUE_OF_COLLATERAL() {
		return R46_VALUE_OF_COLLATERAL;
	}





	public void setR46_VALUE_OF_COLLATERAL(BigDecimal r46_VALUE_OF_COLLATERAL) {
		R46_VALUE_OF_COLLATERAL = r46_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR46_TOTAL_VALUE_NPL() {
		return R46_TOTAL_VALUE_NPL;
	}





	public void setR46_TOTAL_VALUE_NPL(BigDecimal r46_TOTAL_VALUE_NPL) {
		R46_TOTAL_VALUE_NPL = r46_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR46_TOTAL_SPECIFIC_PROV() {
		return R46_TOTAL_SPECIFIC_PROV;
	}





	public void setR46_TOTAL_SPECIFIC_PROV(BigDecimal r46_TOTAL_SPECIFIC_PROV) {
		R46_TOTAL_SPECIFIC_PROV = r46_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR46_SPECIFIC_PROV_NPL() {
		return R46_SPECIFIC_PROV_NPL;
	}





	public void setR46_SPECIFIC_PROV_NPL(BigDecimal r46_SPECIFIC_PROV_NPL) {
		R46_SPECIFIC_PROV_NPL = r46_SPECIFIC_PROV_NPL;
	}





	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}





	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}





	public BigDecimal getR47_30D_90D_PASTDUE() {
		return R47_30D_90D_PASTDUE;
	}





	public void setR47_30D_90D_PASTDUE(BigDecimal r47_30d_90d_PASTDUE) {
		R47_30D_90D_PASTDUE = r47_30d_90d_PASTDUE;
	}





	public BigDecimal getR47_NON_PERFORM_LOANS() {
		return R47_NON_PERFORM_LOANS;
	}





	public void setR47_NON_PERFORM_LOANS(BigDecimal r47_NON_PERFORM_LOANS) {
		R47_NON_PERFORM_LOANS = r47_NON_PERFORM_LOANS;
	}





	public BigDecimal getR47_NON_ACCRUALS1() {
		return R47_NON_ACCRUALS1;
	}





	public void setR47_NON_ACCRUALS1(BigDecimal r47_NON_ACCRUALS1) {
		R47_NON_ACCRUALS1 = r47_NON_ACCRUALS1;
	}





	public BigDecimal getR47_SPECIFIC_PROV1() {
		return R47_SPECIFIC_PROV1;
	}





	public void setR47_SPECIFIC_PROV1(BigDecimal r47_SPECIFIC_PROV1) {
		R47_SPECIFIC_PROV1 = r47_SPECIFIC_PROV1;
	}





	public BigDecimal getR47_NO_OF_ACC1() {
		return R47_NO_OF_ACC1;
	}





	public void setR47_NO_OF_ACC1(BigDecimal r47_NO_OF_ACC1) {
		R47_NO_OF_ACC1 = r47_NO_OF_ACC1;
	}





	public BigDecimal getR47_90D_180D_PASTDUE() {
		return R47_90D_180D_PASTDUE;
	}





	public void setR47_90D_180D_PASTDUE(BigDecimal r47_90d_180d_PASTDUE) {
		R47_90D_180D_PASTDUE = r47_90d_180d_PASTDUE;
	}





	public BigDecimal getR47_NON_ACCRUALS2() {
		return R47_NON_ACCRUALS2;
	}





	public void setR47_NON_ACCRUALS2(BigDecimal r47_NON_ACCRUALS2) {
		R47_NON_ACCRUALS2 = r47_NON_ACCRUALS2;
	}





	public BigDecimal getR47_SPECIFIC_PROV2() {
		return R47_SPECIFIC_PROV2;
	}





	public void setR47_SPECIFIC_PROV2(BigDecimal r47_SPECIFIC_PROV2) {
		R47_SPECIFIC_PROV2 = r47_SPECIFIC_PROV2;
	}





	public BigDecimal getR47_NO_OF_ACC2() {
		return R47_NO_OF_ACC2;
	}





	public void setR47_NO_OF_ACC2(BigDecimal r47_NO_OF_ACC2) {
		R47_NO_OF_ACC2 = r47_NO_OF_ACC2;
	}





	public BigDecimal getR47_180D_ABOVE_PASTDUE() {
		return R47_180D_ABOVE_PASTDUE;
	}





	public void setR47_180D_ABOVE_PASTDUE(BigDecimal r47_180d_ABOVE_PASTDUE) {
		R47_180D_ABOVE_PASTDUE = r47_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR47_NON_ACCRUALS3() {
		return R47_NON_ACCRUALS3;
	}





	public void setR47_NON_ACCRUALS3(BigDecimal r47_NON_ACCRUALS3) {
		R47_NON_ACCRUALS3 = r47_NON_ACCRUALS3;
	}





	public BigDecimal getR47_SPECIFIC_PROV3() {
		return R47_SPECIFIC_PROV3;
	}





	public void setR47_SPECIFIC_PROV3(BigDecimal r47_SPECIFIC_PROV3) {
		R47_SPECIFIC_PROV3 = r47_SPECIFIC_PROV3;
	}





	public BigDecimal getR47_NO_OF_ACC3() {
		return R47_NO_OF_ACC3;
	}





	public void setR47_NO_OF_ACC3(BigDecimal r47_NO_OF_ACC3) {
		R47_NO_OF_ACC3 = r47_NO_OF_ACC3;
	}





	public BigDecimal getR47_TOTAL_NON_ACCRUAL() {
		return R47_TOTAL_NON_ACCRUAL;
	}





	public void setR47_TOTAL_NON_ACCRUAL(BigDecimal r47_TOTAL_NON_ACCRUAL) {
		R47_TOTAL_NON_ACCRUAL = r47_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR47_TOTAL_DUE_LOANS() {
		return R47_TOTAL_DUE_LOANS;
	}





	public void setR47_TOTAL_DUE_LOANS(BigDecimal r47_TOTAL_DUE_LOANS) {
		R47_TOTAL_DUE_LOANS = r47_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR47_TOTAL_PERFORMING_LOAN() {
		return R47_TOTAL_PERFORMING_LOAN;
	}





	public void setR47_TOTAL_PERFORMING_LOAN(BigDecimal r47_TOTAL_PERFORMING_LOAN) {
		R47_TOTAL_PERFORMING_LOAN = r47_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR47_VALUE_OF_COLLATERAL() {
		return R47_VALUE_OF_COLLATERAL;
	}





	public void setR47_VALUE_OF_COLLATERAL(BigDecimal r47_VALUE_OF_COLLATERAL) {
		R47_VALUE_OF_COLLATERAL = r47_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR47_TOTAL_VALUE_NPL() {
		return R47_TOTAL_VALUE_NPL;
	}





	public void setR47_TOTAL_VALUE_NPL(BigDecimal r47_TOTAL_VALUE_NPL) {
		R47_TOTAL_VALUE_NPL = r47_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR47_TOTAL_SPECIFIC_PROV() {
		return R47_TOTAL_SPECIFIC_PROV;
	}





	public void setR47_TOTAL_SPECIFIC_PROV(BigDecimal r47_TOTAL_SPECIFIC_PROV) {
		R47_TOTAL_SPECIFIC_PROV = r47_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR47_SPECIFIC_PROV_NPL() {
		return R47_SPECIFIC_PROV_NPL;
	}





	public void setR47_SPECIFIC_PROV_NPL(BigDecimal r47_SPECIFIC_PROV_NPL) {
		R47_SPECIFIC_PROV_NPL = r47_SPECIFIC_PROV_NPL;
	}





	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}





	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}





	public BigDecimal getR48_30D_90D_PASTDUE() {
		return R48_30D_90D_PASTDUE;
	}





	public void setR48_30D_90D_PASTDUE(BigDecimal r48_30d_90d_PASTDUE) {
		R48_30D_90D_PASTDUE = r48_30d_90d_PASTDUE;
	}





	public BigDecimal getR48_NON_PERFORM_LOANS() {
		return R48_NON_PERFORM_LOANS;
	}





	public void setR48_NON_PERFORM_LOANS(BigDecimal r48_NON_PERFORM_LOANS) {
		R48_NON_PERFORM_LOANS = r48_NON_PERFORM_LOANS;
	}





	public BigDecimal getR48_NON_ACCRUALS1() {
		return R48_NON_ACCRUALS1;
	}





	public void setR48_NON_ACCRUALS1(BigDecimal r48_NON_ACCRUALS1) {
		R48_NON_ACCRUALS1 = r48_NON_ACCRUALS1;
	}





	public BigDecimal getR48_SPECIFIC_PROV1() {
		return R48_SPECIFIC_PROV1;
	}





	public void setR48_SPECIFIC_PROV1(BigDecimal r48_SPECIFIC_PROV1) {
		R48_SPECIFIC_PROV1 = r48_SPECIFIC_PROV1;
	}





	public BigDecimal getR48_NO_OF_ACC1() {
		return R48_NO_OF_ACC1;
	}





	public void setR48_NO_OF_ACC1(BigDecimal r48_NO_OF_ACC1) {
		R48_NO_OF_ACC1 = r48_NO_OF_ACC1;
	}





	public BigDecimal getR48_90D_180D_PASTDUE() {
		return R48_90D_180D_PASTDUE;
	}





	public void setR48_90D_180D_PASTDUE(BigDecimal r48_90d_180d_PASTDUE) {
		R48_90D_180D_PASTDUE = r48_90d_180d_PASTDUE;
	}





	public BigDecimal getR48_NON_ACCRUALS2() {
		return R48_NON_ACCRUALS2;
	}





	public void setR48_NON_ACCRUALS2(BigDecimal r48_NON_ACCRUALS2) {
		R48_NON_ACCRUALS2 = r48_NON_ACCRUALS2;
	}





	public BigDecimal getR48_SPECIFIC_PROV2() {
		return R48_SPECIFIC_PROV2;
	}





	public void setR48_SPECIFIC_PROV2(BigDecimal r48_SPECIFIC_PROV2) {
		R48_SPECIFIC_PROV2 = r48_SPECIFIC_PROV2;
	}





	public BigDecimal getR48_NO_OF_ACC2() {
		return R48_NO_OF_ACC2;
	}





	public void setR48_NO_OF_ACC2(BigDecimal r48_NO_OF_ACC2) {
		R48_NO_OF_ACC2 = r48_NO_OF_ACC2;
	}





	public BigDecimal getR48_180D_ABOVE_PASTDUE() {
		return R48_180D_ABOVE_PASTDUE;
	}





	public void setR48_180D_ABOVE_PASTDUE(BigDecimal r48_180d_ABOVE_PASTDUE) {
		R48_180D_ABOVE_PASTDUE = r48_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR48_NON_ACCRUALS3() {
		return R48_NON_ACCRUALS3;
	}





	public void setR48_NON_ACCRUALS3(BigDecimal r48_NON_ACCRUALS3) {
		R48_NON_ACCRUALS3 = r48_NON_ACCRUALS3;
	}





	public BigDecimal getR48_SPECIFIC_PROV3() {
		return R48_SPECIFIC_PROV3;
	}





	public void setR48_SPECIFIC_PROV3(BigDecimal r48_SPECIFIC_PROV3) {
		R48_SPECIFIC_PROV3 = r48_SPECIFIC_PROV3;
	}





	public BigDecimal getR48_NO_OF_ACC3() {
		return R48_NO_OF_ACC3;
	}





	public void setR48_NO_OF_ACC3(BigDecimal r48_NO_OF_ACC3) {
		R48_NO_OF_ACC3 = r48_NO_OF_ACC3;
	}





	public BigDecimal getR48_TOTAL_NON_ACCRUAL() {
		return R48_TOTAL_NON_ACCRUAL;
	}





	public void setR48_TOTAL_NON_ACCRUAL(BigDecimal r48_TOTAL_NON_ACCRUAL) {
		R48_TOTAL_NON_ACCRUAL = r48_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR48_TOTAL_DUE_LOANS() {
		return R48_TOTAL_DUE_LOANS;
	}





	public void setR48_TOTAL_DUE_LOANS(BigDecimal r48_TOTAL_DUE_LOANS) {
		R48_TOTAL_DUE_LOANS = r48_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR48_TOTAL_PERFORMING_LOAN() {
		return R48_TOTAL_PERFORMING_LOAN;
	}





	public void setR48_TOTAL_PERFORMING_LOAN(BigDecimal r48_TOTAL_PERFORMING_LOAN) {
		R48_TOTAL_PERFORMING_LOAN = r48_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR48_VALUE_OF_COLLATERAL() {
		return R48_VALUE_OF_COLLATERAL;
	}





	public void setR48_VALUE_OF_COLLATERAL(BigDecimal r48_VALUE_OF_COLLATERAL) {
		R48_VALUE_OF_COLLATERAL = r48_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR48_TOTAL_VALUE_NPL() {
		return R48_TOTAL_VALUE_NPL;
	}





	public void setR48_TOTAL_VALUE_NPL(BigDecimal r48_TOTAL_VALUE_NPL) {
		R48_TOTAL_VALUE_NPL = r48_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR48_TOTAL_SPECIFIC_PROV() {
		return R48_TOTAL_SPECIFIC_PROV;
	}





	public void setR48_TOTAL_SPECIFIC_PROV(BigDecimal r48_TOTAL_SPECIFIC_PROV) {
		R48_TOTAL_SPECIFIC_PROV = r48_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR48_SPECIFIC_PROV_NPL() {
		return R48_SPECIFIC_PROV_NPL;
	}





	public void setR48_SPECIFIC_PROV_NPL(BigDecimal r48_SPECIFIC_PROV_NPL) {
		R48_SPECIFIC_PROV_NPL = r48_SPECIFIC_PROV_NPL;
	}





	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}





	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}





	public BigDecimal getR49_30D_90D_PASTDUE() {
		return R49_30D_90D_PASTDUE;
	}





	public void setR49_30D_90D_PASTDUE(BigDecimal r49_30d_90d_PASTDUE) {
		R49_30D_90D_PASTDUE = r49_30d_90d_PASTDUE;
	}





	public BigDecimal getR49_NON_PERFORM_LOANS() {
		return R49_NON_PERFORM_LOANS;
	}





	public void setR49_NON_PERFORM_LOANS(BigDecimal r49_NON_PERFORM_LOANS) {
		R49_NON_PERFORM_LOANS = r49_NON_PERFORM_LOANS;
	}





	public BigDecimal getR49_NON_ACCRUALS1() {
		return R49_NON_ACCRUALS1;
	}





	public void setR49_NON_ACCRUALS1(BigDecimal r49_NON_ACCRUALS1) {
		R49_NON_ACCRUALS1 = r49_NON_ACCRUALS1;
	}





	public BigDecimal getR49_SPECIFIC_PROV1() {
		return R49_SPECIFIC_PROV1;
	}





	public void setR49_SPECIFIC_PROV1(BigDecimal r49_SPECIFIC_PROV1) {
		R49_SPECIFIC_PROV1 = r49_SPECIFIC_PROV1;
	}





	public BigDecimal getR49_NO_OF_ACC1() {
		return R49_NO_OF_ACC1;
	}





	public void setR49_NO_OF_ACC1(BigDecimal r49_NO_OF_ACC1) {
		R49_NO_OF_ACC1 = r49_NO_OF_ACC1;
	}





	public BigDecimal getR49_90D_180D_PASTDUE() {
		return R49_90D_180D_PASTDUE;
	}





	public void setR49_90D_180D_PASTDUE(BigDecimal r49_90d_180d_PASTDUE) {
		R49_90D_180D_PASTDUE = r49_90d_180d_PASTDUE;
	}





	public BigDecimal getR49_NON_ACCRUALS2() {
		return R49_NON_ACCRUALS2;
	}





	public void setR49_NON_ACCRUALS2(BigDecimal r49_NON_ACCRUALS2) {
		R49_NON_ACCRUALS2 = r49_NON_ACCRUALS2;
	}





	public BigDecimal getR49_SPECIFIC_PROV2() {
		return R49_SPECIFIC_PROV2;
	}





	public void setR49_SPECIFIC_PROV2(BigDecimal r49_SPECIFIC_PROV2) {
		R49_SPECIFIC_PROV2 = r49_SPECIFIC_PROV2;
	}





	public BigDecimal getR49_NO_OF_ACC2() {
		return R49_NO_OF_ACC2;
	}





	public void setR49_NO_OF_ACC2(BigDecimal r49_NO_OF_ACC2) {
		R49_NO_OF_ACC2 = r49_NO_OF_ACC2;
	}





	public BigDecimal getR49_180D_ABOVE_PASTDUE() {
		return R49_180D_ABOVE_PASTDUE;
	}





	public void setR49_180D_ABOVE_PASTDUE(BigDecimal r49_180d_ABOVE_PASTDUE) {
		R49_180D_ABOVE_PASTDUE = r49_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR49_NON_ACCRUALS3() {
		return R49_NON_ACCRUALS3;
	}





	public void setR49_NON_ACCRUALS3(BigDecimal r49_NON_ACCRUALS3) {
		R49_NON_ACCRUALS3 = r49_NON_ACCRUALS3;
	}





	public BigDecimal getR49_SPECIFIC_PROV3() {
		return R49_SPECIFIC_PROV3;
	}





	public void setR49_SPECIFIC_PROV3(BigDecimal r49_SPECIFIC_PROV3) {
		R49_SPECIFIC_PROV3 = r49_SPECIFIC_PROV3;
	}





	public BigDecimal getR49_NO_OF_ACC3() {
		return R49_NO_OF_ACC3;
	}





	public void setR49_NO_OF_ACC3(BigDecimal r49_NO_OF_ACC3) {
		R49_NO_OF_ACC3 = r49_NO_OF_ACC3;
	}





	public BigDecimal getR49_TOTAL_NON_ACCRUAL() {
		return R49_TOTAL_NON_ACCRUAL;
	}





	public void setR49_TOTAL_NON_ACCRUAL(BigDecimal r49_TOTAL_NON_ACCRUAL) {
		R49_TOTAL_NON_ACCRUAL = r49_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR49_TOTAL_DUE_LOANS() {
		return R49_TOTAL_DUE_LOANS;
	}





	public void setR49_TOTAL_DUE_LOANS(BigDecimal r49_TOTAL_DUE_LOANS) {
		R49_TOTAL_DUE_LOANS = r49_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR49_TOTAL_PERFORMING_LOAN() {
		return R49_TOTAL_PERFORMING_LOAN;
	}





	public void setR49_TOTAL_PERFORMING_LOAN(BigDecimal r49_TOTAL_PERFORMING_LOAN) {
		R49_TOTAL_PERFORMING_LOAN = r49_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR49_VALUE_OF_COLLATERAL() {
		return R49_VALUE_OF_COLLATERAL;
	}





	public void setR49_VALUE_OF_COLLATERAL(BigDecimal r49_VALUE_OF_COLLATERAL) {
		R49_VALUE_OF_COLLATERAL = r49_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR49_TOTAL_VALUE_NPL() {
		return R49_TOTAL_VALUE_NPL;
	}





	public void setR49_TOTAL_VALUE_NPL(BigDecimal r49_TOTAL_VALUE_NPL) {
		R49_TOTAL_VALUE_NPL = r49_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR49_TOTAL_SPECIFIC_PROV() {
		return R49_TOTAL_SPECIFIC_PROV;
	}





	public void setR49_TOTAL_SPECIFIC_PROV(BigDecimal r49_TOTAL_SPECIFIC_PROV) {
		R49_TOTAL_SPECIFIC_PROV = r49_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR49_SPECIFIC_PROV_NPL() {
		return R49_SPECIFIC_PROV_NPL;
	}





	public void setR49_SPECIFIC_PROV_NPL(BigDecimal r49_SPECIFIC_PROV_NPL) {
		R49_SPECIFIC_PROV_NPL = r49_SPECIFIC_PROV_NPL;
	}





	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}





	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}





	public BigDecimal getR50_30D_90D_PASTDUE() {
		return R50_30D_90D_PASTDUE;
	}





	public void setR50_30D_90D_PASTDUE(BigDecimal r50_30d_90d_PASTDUE) {
		R50_30D_90D_PASTDUE = r50_30d_90d_PASTDUE;
	}





	public BigDecimal getR50_NON_PERFORM_LOANS() {
		return R50_NON_PERFORM_LOANS;
	}





	public void setR50_NON_PERFORM_LOANS(BigDecimal r50_NON_PERFORM_LOANS) {
		R50_NON_PERFORM_LOANS = r50_NON_PERFORM_LOANS;
	}





	public BigDecimal getR50_NON_ACCRUALS1() {
		return R50_NON_ACCRUALS1;
	}





	public void setR50_NON_ACCRUALS1(BigDecimal r50_NON_ACCRUALS1) {
		R50_NON_ACCRUALS1 = r50_NON_ACCRUALS1;
	}





	public BigDecimal getR50_SPECIFIC_PROV1() {
		return R50_SPECIFIC_PROV1;
	}





	public void setR50_SPECIFIC_PROV1(BigDecimal r50_SPECIFIC_PROV1) {
		R50_SPECIFIC_PROV1 = r50_SPECIFIC_PROV1;
	}





	public BigDecimal getR50_NO_OF_ACC1() {
		return R50_NO_OF_ACC1;
	}





	public void setR50_NO_OF_ACC1(BigDecimal r50_NO_OF_ACC1) {
		R50_NO_OF_ACC1 = r50_NO_OF_ACC1;
	}





	public BigDecimal getR50_90D_180D_PASTDUE() {
		return R50_90D_180D_PASTDUE;
	}





	public void setR50_90D_180D_PASTDUE(BigDecimal r50_90d_180d_PASTDUE) {
		R50_90D_180D_PASTDUE = r50_90d_180d_PASTDUE;
	}





	public BigDecimal getR50_NON_ACCRUALS2() {
		return R50_NON_ACCRUALS2;
	}





	public void setR50_NON_ACCRUALS2(BigDecimal r50_NON_ACCRUALS2) {
		R50_NON_ACCRUALS2 = r50_NON_ACCRUALS2;
	}





	public BigDecimal getR50_SPECIFIC_PROV2() {
		return R50_SPECIFIC_PROV2;
	}





	public void setR50_SPECIFIC_PROV2(BigDecimal r50_SPECIFIC_PROV2) {
		R50_SPECIFIC_PROV2 = r50_SPECIFIC_PROV2;
	}





	public BigDecimal getR50_NO_OF_ACC2() {
		return R50_NO_OF_ACC2;
	}





	public void setR50_NO_OF_ACC2(BigDecimal r50_NO_OF_ACC2) {
		R50_NO_OF_ACC2 = r50_NO_OF_ACC2;
	}





	public BigDecimal getR50_180D_ABOVE_PASTDUE() {
		return R50_180D_ABOVE_PASTDUE;
	}





	public void setR50_180D_ABOVE_PASTDUE(BigDecimal r50_180d_ABOVE_PASTDUE) {
		R50_180D_ABOVE_PASTDUE = r50_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR50_NON_ACCRUALS3() {
		return R50_NON_ACCRUALS3;
	}





	public void setR50_NON_ACCRUALS3(BigDecimal r50_NON_ACCRUALS3) {
		R50_NON_ACCRUALS3 = r50_NON_ACCRUALS3;
	}





	public BigDecimal getR50_SPECIFIC_PROV3() {
		return R50_SPECIFIC_PROV3;
	}





	public void setR50_SPECIFIC_PROV3(BigDecimal r50_SPECIFIC_PROV3) {
		R50_SPECIFIC_PROV3 = r50_SPECIFIC_PROV3;
	}





	public BigDecimal getR50_NO_OF_ACC3() {
		return R50_NO_OF_ACC3;
	}





	public void setR50_NO_OF_ACC3(BigDecimal r50_NO_OF_ACC3) {
		R50_NO_OF_ACC3 = r50_NO_OF_ACC3;
	}





	public BigDecimal getR50_TOTAL_NON_ACCRUAL() {
		return R50_TOTAL_NON_ACCRUAL;
	}





	public void setR50_TOTAL_NON_ACCRUAL(BigDecimal r50_TOTAL_NON_ACCRUAL) {
		R50_TOTAL_NON_ACCRUAL = r50_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR50_TOTAL_DUE_LOANS() {
		return R50_TOTAL_DUE_LOANS;
	}





	public void setR50_TOTAL_DUE_LOANS(BigDecimal r50_TOTAL_DUE_LOANS) {
		R50_TOTAL_DUE_LOANS = r50_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR50_TOTAL_PERFORMING_LOAN() {
		return R50_TOTAL_PERFORMING_LOAN;
	}





	public void setR50_TOTAL_PERFORMING_LOAN(BigDecimal r50_TOTAL_PERFORMING_LOAN) {
		R50_TOTAL_PERFORMING_LOAN = r50_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR50_VALUE_OF_COLLATERAL() {
		return R50_VALUE_OF_COLLATERAL;
	}





	public void setR50_VALUE_OF_COLLATERAL(BigDecimal r50_VALUE_OF_COLLATERAL) {
		R50_VALUE_OF_COLLATERAL = r50_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR50_TOTAL_VALUE_NPL() {
		return R50_TOTAL_VALUE_NPL;
	}





	public void setR50_TOTAL_VALUE_NPL(BigDecimal r50_TOTAL_VALUE_NPL) {
		R50_TOTAL_VALUE_NPL = r50_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR50_TOTAL_SPECIFIC_PROV() {
		return R50_TOTAL_SPECIFIC_PROV;
	}





	public void setR50_TOTAL_SPECIFIC_PROV(BigDecimal r50_TOTAL_SPECIFIC_PROV) {
		R50_TOTAL_SPECIFIC_PROV = r50_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR50_SPECIFIC_PROV_NPL() {
		return R50_SPECIFIC_PROV_NPL;
	}





	public void setR50_SPECIFIC_PROV_NPL(BigDecimal r50_SPECIFIC_PROV_NPL) {
		R50_SPECIFIC_PROV_NPL = r50_SPECIFIC_PROV_NPL;
	}





	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}





	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}





	public BigDecimal getR51_30D_90D_PASTDUE() {
		return R51_30D_90D_PASTDUE;
	}





	public void setR51_30D_90D_PASTDUE(BigDecimal r51_30d_90d_PASTDUE) {
		R51_30D_90D_PASTDUE = r51_30d_90d_PASTDUE;
	}





	public BigDecimal getR51_NON_PERFORM_LOANS() {
		return R51_NON_PERFORM_LOANS;
	}





	public void setR51_NON_PERFORM_LOANS(BigDecimal r51_NON_PERFORM_LOANS) {
		R51_NON_PERFORM_LOANS = r51_NON_PERFORM_LOANS;
	}





	public BigDecimal getR51_NON_ACCRUALS1() {
		return R51_NON_ACCRUALS1;
	}





	public void setR51_NON_ACCRUALS1(BigDecimal r51_NON_ACCRUALS1) {
		R51_NON_ACCRUALS1 = r51_NON_ACCRUALS1;
	}





	public BigDecimal getR51_SPECIFIC_PROV1() {
		return R51_SPECIFIC_PROV1;
	}





	public void setR51_SPECIFIC_PROV1(BigDecimal r51_SPECIFIC_PROV1) {
		R51_SPECIFIC_PROV1 = r51_SPECIFIC_PROV1;
	}





	public BigDecimal getR51_NO_OF_ACC1() {
		return R51_NO_OF_ACC1;
	}





	public void setR51_NO_OF_ACC1(BigDecimal r51_NO_OF_ACC1) {
		R51_NO_OF_ACC1 = r51_NO_OF_ACC1;
	}





	public BigDecimal getR51_90D_180D_PASTDUE() {
		return R51_90D_180D_PASTDUE;
	}





	public void setR51_90D_180D_PASTDUE(BigDecimal r51_90d_180d_PASTDUE) {
		R51_90D_180D_PASTDUE = r51_90d_180d_PASTDUE;
	}





	public BigDecimal getR51_NON_ACCRUALS2() {
		return R51_NON_ACCRUALS2;
	}





	public void setR51_NON_ACCRUALS2(BigDecimal r51_NON_ACCRUALS2) {
		R51_NON_ACCRUALS2 = r51_NON_ACCRUALS2;
	}





	public BigDecimal getR51_SPECIFIC_PROV2() {
		return R51_SPECIFIC_PROV2;
	}





	public void setR51_SPECIFIC_PROV2(BigDecimal r51_SPECIFIC_PROV2) {
		R51_SPECIFIC_PROV2 = r51_SPECIFIC_PROV2;
	}





	public BigDecimal getR51_NO_OF_ACC2() {
		return R51_NO_OF_ACC2;
	}





	public void setR51_NO_OF_ACC2(BigDecimal r51_NO_OF_ACC2) {
		R51_NO_OF_ACC2 = r51_NO_OF_ACC2;
	}





	public BigDecimal getR51_180D_ABOVE_PASTDUE() {
		return R51_180D_ABOVE_PASTDUE;
	}





	public void setR51_180D_ABOVE_PASTDUE(BigDecimal r51_180d_ABOVE_PASTDUE) {
		R51_180D_ABOVE_PASTDUE = r51_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR51_NON_ACCRUALS3() {
		return R51_NON_ACCRUALS3;
	}





	public void setR51_NON_ACCRUALS3(BigDecimal r51_NON_ACCRUALS3) {
		R51_NON_ACCRUALS3 = r51_NON_ACCRUALS3;
	}





	public BigDecimal getR51_SPECIFIC_PROV3() {
		return R51_SPECIFIC_PROV3;
	}





	public void setR51_SPECIFIC_PROV3(BigDecimal r51_SPECIFIC_PROV3) {
		R51_SPECIFIC_PROV3 = r51_SPECIFIC_PROV3;
	}





	public BigDecimal getR51_NO_OF_ACC3() {
		return R51_NO_OF_ACC3;
	}





	public void setR51_NO_OF_ACC3(BigDecimal r51_NO_OF_ACC3) {
		R51_NO_OF_ACC3 = r51_NO_OF_ACC3;
	}





	public BigDecimal getR51_TOTAL_NON_ACCRUAL() {
		return R51_TOTAL_NON_ACCRUAL;
	}





	public void setR51_TOTAL_NON_ACCRUAL(BigDecimal r51_TOTAL_NON_ACCRUAL) {
		R51_TOTAL_NON_ACCRUAL = r51_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR51_TOTAL_DUE_LOANS() {
		return R51_TOTAL_DUE_LOANS;
	}





	public void setR51_TOTAL_DUE_LOANS(BigDecimal r51_TOTAL_DUE_LOANS) {
		R51_TOTAL_DUE_LOANS = r51_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR51_TOTAL_PERFORMING_LOAN() {
		return R51_TOTAL_PERFORMING_LOAN;
	}





	public void setR51_TOTAL_PERFORMING_LOAN(BigDecimal r51_TOTAL_PERFORMING_LOAN) {
		R51_TOTAL_PERFORMING_LOAN = r51_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR51_VALUE_OF_COLLATERAL() {
		return R51_VALUE_OF_COLLATERAL;
	}





	public void setR51_VALUE_OF_COLLATERAL(BigDecimal r51_VALUE_OF_COLLATERAL) {
		R51_VALUE_OF_COLLATERAL = r51_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR51_TOTAL_VALUE_NPL() {
		return R51_TOTAL_VALUE_NPL;
	}





	public void setR51_TOTAL_VALUE_NPL(BigDecimal r51_TOTAL_VALUE_NPL) {
		R51_TOTAL_VALUE_NPL = r51_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR51_TOTAL_SPECIFIC_PROV() {
		return R51_TOTAL_SPECIFIC_PROV;
	}





	public void setR51_TOTAL_SPECIFIC_PROV(BigDecimal r51_TOTAL_SPECIFIC_PROV) {
		R51_TOTAL_SPECIFIC_PROV = r51_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR51_SPECIFIC_PROV_NPL() {
		return R51_SPECIFIC_PROV_NPL;
	}





	public void setR51_SPECIFIC_PROV_NPL(BigDecimal r51_SPECIFIC_PROV_NPL) {
		R51_SPECIFIC_PROV_NPL = r51_SPECIFIC_PROV_NPL;
	}





	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}





	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}





	public BigDecimal getR52_30D_90D_PASTDUE() {
		return R52_30D_90D_PASTDUE;
	}





	public void setR52_30D_90D_PASTDUE(BigDecimal r52_30d_90d_PASTDUE) {
		R52_30D_90D_PASTDUE = r52_30d_90d_PASTDUE;
	}





	public BigDecimal getR52_NON_PERFORM_LOANS() {
		return R52_NON_PERFORM_LOANS;
	}





	public void setR52_NON_PERFORM_LOANS(BigDecimal r52_NON_PERFORM_LOANS) {
		R52_NON_PERFORM_LOANS = r52_NON_PERFORM_LOANS;
	}





	public BigDecimal getR52_NON_ACCRUALS1() {
		return R52_NON_ACCRUALS1;
	}





	public void setR52_NON_ACCRUALS1(BigDecimal r52_NON_ACCRUALS1) {
		R52_NON_ACCRUALS1 = r52_NON_ACCRUALS1;
	}





	public BigDecimal getR52_SPECIFIC_PROV1() {
		return R52_SPECIFIC_PROV1;
	}





	public void setR52_SPECIFIC_PROV1(BigDecimal r52_SPECIFIC_PROV1) {
		R52_SPECIFIC_PROV1 = r52_SPECIFIC_PROV1;
	}





	public BigDecimal getR52_NO_OF_ACC1() {
		return R52_NO_OF_ACC1;
	}





	public void setR52_NO_OF_ACC1(BigDecimal r52_NO_OF_ACC1) {
		R52_NO_OF_ACC1 = r52_NO_OF_ACC1;
	}





	public BigDecimal getR52_90D_180D_PASTDUE() {
		return R52_90D_180D_PASTDUE;
	}





	public void setR52_90D_180D_PASTDUE(BigDecimal r52_90d_180d_PASTDUE) {
		R52_90D_180D_PASTDUE = r52_90d_180d_PASTDUE;
	}





	public BigDecimal getR52_NON_ACCRUALS2() {
		return R52_NON_ACCRUALS2;
	}





	public void setR52_NON_ACCRUALS2(BigDecimal r52_NON_ACCRUALS2) {
		R52_NON_ACCRUALS2 = r52_NON_ACCRUALS2;
	}





	public BigDecimal getR52_SPECIFIC_PROV2() {
		return R52_SPECIFIC_PROV2;
	}





	public void setR52_SPECIFIC_PROV2(BigDecimal r52_SPECIFIC_PROV2) {
		R52_SPECIFIC_PROV2 = r52_SPECIFIC_PROV2;
	}





	public BigDecimal getR52_NO_OF_ACC2() {
		return R52_NO_OF_ACC2;
	}





	public void setR52_NO_OF_ACC2(BigDecimal r52_NO_OF_ACC2) {
		R52_NO_OF_ACC2 = r52_NO_OF_ACC2;
	}





	public BigDecimal getR52_180D_ABOVE_PASTDUE() {
		return R52_180D_ABOVE_PASTDUE;
	}





	public void setR52_180D_ABOVE_PASTDUE(BigDecimal r52_180d_ABOVE_PASTDUE) {
		R52_180D_ABOVE_PASTDUE = r52_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR52_NON_ACCRUALS3() {
		return R52_NON_ACCRUALS3;
	}





	public void setR52_NON_ACCRUALS3(BigDecimal r52_NON_ACCRUALS3) {
		R52_NON_ACCRUALS3 = r52_NON_ACCRUALS3;
	}





	public BigDecimal getR52_SPECIFIC_PROV3() {
		return R52_SPECIFIC_PROV3;
	}





	public void setR52_SPECIFIC_PROV3(BigDecimal r52_SPECIFIC_PROV3) {
		R52_SPECIFIC_PROV3 = r52_SPECIFIC_PROV3;
	}





	public BigDecimal getR52_NO_OF_ACC3() {
		return R52_NO_OF_ACC3;
	}





	public void setR52_NO_OF_ACC3(BigDecimal r52_NO_OF_ACC3) {
		R52_NO_OF_ACC3 = r52_NO_OF_ACC3;
	}





	public BigDecimal getR52_TOTAL_NON_ACCRUAL() {
		return R52_TOTAL_NON_ACCRUAL;
	}





	public void setR52_TOTAL_NON_ACCRUAL(BigDecimal r52_TOTAL_NON_ACCRUAL) {
		R52_TOTAL_NON_ACCRUAL = r52_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR52_TOTAL_DUE_LOANS() {
		return R52_TOTAL_DUE_LOANS;
	}





	public void setR52_TOTAL_DUE_LOANS(BigDecimal r52_TOTAL_DUE_LOANS) {
		R52_TOTAL_DUE_LOANS = r52_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR52_TOTAL_PERFORMING_LOAN() {
		return R52_TOTAL_PERFORMING_LOAN;
	}





	public void setR52_TOTAL_PERFORMING_LOAN(BigDecimal r52_TOTAL_PERFORMING_LOAN) {
		R52_TOTAL_PERFORMING_LOAN = r52_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR52_VALUE_OF_COLLATERAL() {
		return R52_VALUE_OF_COLLATERAL;
	}





	public void setR52_VALUE_OF_COLLATERAL(BigDecimal r52_VALUE_OF_COLLATERAL) {
		R52_VALUE_OF_COLLATERAL = r52_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR52_TOTAL_VALUE_NPL() {
		return R52_TOTAL_VALUE_NPL;
	}





	public void setR52_TOTAL_VALUE_NPL(BigDecimal r52_TOTAL_VALUE_NPL) {
		R52_TOTAL_VALUE_NPL = r52_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR52_TOTAL_SPECIFIC_PROV() {
		return R52_TOTAL_SPECIFIC_PROV;
	}





	public void setR52_TOTAL_SPECIFIC_PROV(BigDecimal r52_TOTAL_SPECIFIC_PROV) {
		R52_TOTAL_SPECIFIC_PROV = r52_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR52_SPECIFIC_PROV_NPL() {
		return R52_SPECIFIC_PROV_NPL;
	}





	public void setR52_SPECIFIC_PROV_NPL(BigDecimal r52_SPECIFIC_PROV_NPL) {
		R52_SPECIFIC_PROV_NPL = r52_SPECIFIC_PROV_NPL;
	}





	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}





	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}





	public BigDecimal getR53_30D_90D_PASTDUE() {
		return R53_30D_90D_PASTDUE;
	}





	public void setR53_30D_90D_PASTDUE(BigDecimal r53_30d_90d_PASTDUE) {
		R53_30D_90D_PASTDUE = r53_30d_90d_PASTDUE;
	}





	public BigDecimal getR53_NON_PERFORM_LOANS() {
		return R53_NON_PERFORM_LOANS;
	}





	public void setR53_NON_PERFORM_LOANS(BigDecimal r53_NON_PERFORM_LOANS) {
		R53_NON_PERFORM_LOANS = r53_NON_PERFORM_LOANS;
	}





	public BigDecimal getR53_NON_ACCRUALS1() {
		return R53_NON_ACCRUALS1;
	}





	public void setR53_NON_ACCRUALS1(BigDecimal r53_NON_ACCRUALS1) {
		R53_NON_ACCRUALS1 = r53_NON_ACCRUALS1;
	}





	public BigDecimal getR53_SPECIFIC_PROV1() {
		return R53_SPECIFIC_PROV1;
	}





	public void setR53_SPECIFIC_PROV1(BigDecimal r53_SPECIFIC_PROV1) {
		R53_SPECIFIC_PROV1 = r53_SPECIFIC_PROV1;
	}





	public BigDecimal getR53_NO_OF_ACC1() {
		return R53_NO_OF_ACC1;
	}





	public void setR53_NO_OF_ACC1(BigDecimal r53_NO_OF_ACC1) {
		R53_NO_OF_ACC1 = r53_NO_OF_ACC1;
	}





	public BigDecimal getR53_90D_180D_PASTDUE() {
		return R53_90D_180D_PASTDUE;
	}





	public void setR53_90D_180D_PASTDUE(BigDecimal r53_90d_180d_PASTDUE) {
		R53_90D_180D_PASTDUE = r53_90d_180d_PASTDUE;
	}





	public BigDecimal getR53_NON_ACCRUALS2() {
		return R53_NON_ACCRUALS2;
	}





	public void setR53_NON_ACCRUALS2(BigDecimal r53_NON_ACCRUALS2) {
		R53_NON_ACCRUALS2 = r53_NON_ACCRUALS2;
	}





	public BigDecimal getR53_SPECIFIC_PROV2() {
		return R53_SPECIFIC_PROV2;
	}





	public void setR53_SPECIFIC_PROV2(BigDecimal r53_SPECIFIC_PROV2) {
		R53_SPECIFIC_PROV2 = r53_SPECIFIC_PROV2;
	}





	public BigDecimal getR53_NO_OF_ACC2() {
		return R53_NO_OF_ACC2;
	}





	public void setR53_NO_OF_ACC2(BigDecimal r53_NO_OF_ACC2) {
		R53_NO_OF_ACC2 = r53_NO_OF_ACC2;
	}





	public BigDecimal getR53_180D_ABOVE_PASTDUE() {
		return R53_180D_ABOVE_PASTDUE;
	}





	public void setR53_180D_ABOVE_PASTDUE(BigDecimal r53_180d_ABOVE_PASTDUE) {
		R53_180D_ABOVE_PASTDUE = r53_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR53_NON_ACCRUALS3() {
		return R53_NON_ACCRUALS3;
	}





	public void setR53_NON_ACCRUALS3(BigDecimal r53_NON_ACCRUALS3) {
		R53_NON_ACCRUALS3 = r53_NON_ACCRUALS3;
	}





	public BigDecimal getR53_SPECIFIC_PROV3() {
		return R53_SPECIFIC_PROV3;
	}





	public void setR53_SPECIFIC_PROV3(BigDecimal r53_SPECIFIC_PROV3) {
		R53_SPECIFIC_PROV3 = r53_SPECIFIC_PROV3;
	}





	public BigDecimal getR53_NO_OF_ACC3() {
		return R53_NO_OF_ACC3;
	}





	public void setR53_NO_OF_ACC3(BigDecimal r53_NO_OF_ACC3) {
		R53_NO_OF_ACC3 = r53_NO_OF_ACC3;
	}





	public BigDecimal getR53_TOTAL_NON_ACCRUAL() {
		return R53_TOTAL_NON_ACCRUAL;
	}





	public void setR53_TOTAL_NON_ACCRUAL(BigDecimal r53_TOTAL_NON_ACCRUAL) {
		R53_TOTAL_NON_ACCRUAL = r53_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR53_TOTAL_DUE_LOANS() {
		return R53_TOTAL_DUE_LOANS;
	}





	public void setR53_TOTAL_DUE_LOANS(BigDecimal r53_TOTAL_DUE_LOANS) {
		R53_TOTAL_DUE_LOANS = r53_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR53_TOTAL_PERFORMING_LOAN() {
		return R53_TOTAL_PERFORMING_LOAN;
	}





	public void setR53_TOTAL_PERFORMING_LOAN(BigDecimal r53_TOTAL_PERFORMING_LOAN) {
		R53_TOTAL_PERFORMING_LOAN = r53_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR53_VALUE_OF_COLLATERAL() {
		return R53_VALUE_OF_COLLATERAL;
	}





	public void setR53_VALUE_OF_COLLATERAL(BigDecimal r53_VALUE_OF_COLLATERAL) {
		R53_VALUE_OF_COLLATERAL = r53_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR53_TOTAL_VALUE_NPL() {
		return R53_TOTAL_VALUE_NPL;
	}





	public void setR53_TOTAL_VALUE_NPL(BigDecimal r53_TOTAL_VALUE_NPL) {
		R53_TOTAL_VALUE_NPL = r53_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR53_TOTAL_SPECIFIC_PROV() {
		return R53_TOTAL_SPECIFIC_PROV;
	}





	public void setR53_TOTAL_SPECIFIC_PROV(BigDecimal r53_TOTAL_SPECIFIC_PROV) {
		R53_TOTAL_SPECIFIC_PROV = r53_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR53_SPECIFIC_PROV_NPL() {
		return R53_SPECIFIC_PROV_NPL;
	}





	public void setR53_SPECIFIC_PROV_NPL(BigDecimal r53_SPECIFIC_PROV_NPL) {
		R53_SPECIFIC_PROV_NPL = r53_SPECIFIC_PROV_NPL;
	}





	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}





	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}





	public BigDecimal getR54_30D_90D_PASTDUE() {
		return R54_30D_90D_PASTDUE;
	}





	public void setR54_30D_90D_PASTDUE(BigDecimal r54_30d_90d_PASTDUE) {
		R54_30D_90D_PASTDUE = r54_30d_90d_PASTDUE;
	}





	public BigDecimal getR54_NON_PERFORM_LOANS() {
		return R54_NON_PERFORM_LOANS;
	}





	public void setR54_NON_PERFORM_LOANS(BigDecimal r54_NON_PERFORM_LOANS) {
		R54_NON_PERFORM_LOANS = r54_NON_PERFORM_LOANS;
	}





	public BigDecimal getR54_NON_ACCRUALS1() {
		return R54_NON_ACCRUALS1;
	}





	public void setR54_NON_ACCRUALS1(BigDecimal r54_NON_ACCRUALS1) {
		R54_NON_ACCRUALS1 = r54_NON_ACCRUALS1;
	}





	public BigDecimal getR54_SPECIFIC_PROV1() {
		return R54_SPECIFIC_PROV1;
	}





	public void setR54_SPECIFIC_PROV1(BigDecimal r54_SPECIFIC_PROV1) {
		R54_SPECIFIC_PROV1 = r54_SPECIFIC_PROV1;
	}





	public BigDecimal getR54_NO_OF_ACC1() {
		return R54_NO_OF_ACC1;
	}





	public void setR54_NO_OF_ACC1(BigDecimal r54_NO_OF_ACC1) {
		R54_NO_OF_ACC1 = r54_NO_OF_ACC1;
	}





	public BigDecimal getR54_90D_180D_PASTDUE() {
		return R54_90D_180D_PASTDUE;
	}





	public void setR54_90D_180D_PASTDUE(BigDecimal r54_90d_180d_PASTDUE) {
		R54_90D_180D_PASTDUE = r54_90d_180d_PASTDUE;
	}





	public BigDecimal getR54_NON_ACCRUALS2() {
		return R54_NON_ACCRUALS2;
	}





	public void setR54_NON_ACCRUALS2(BigDecimal r54_NON_ACCRUALS2) {
		R54_NON_ACCRUALS2 = r54_NON_ACCRUALS2;
	}





	public BigDecimal getR54_SPECIFIC_PROV2() {
		return R54_SPECIFIC_PROV2;
	}





	public void setR54_SPECIFIC_PROV2(BigDecimal r54_SPECIFIC_PROV2) {
		R54_SPECIFIC_PROV2 = r54_SPECIFIC_PROV2;
	}





	public BigDecimal getR54_NO_OF_ACC2() {
		return R54_NO_OF_ACC2;
	}





	public void setR54_NO_OF_ACC2(BigDecimal r54_NO_OF_ACC2) {
		R54_NO_OF_ACC2 = r54_NO_OF_ACC2;
	}





	public BigDecimal getR54_180D_ABOVE_PASTDUE() {
		return R54_180D_ABOVE_PASTDUE;
	}





	public void setR54_180D_ABOVE_PASTDUE(BigDecimal r54_180d_ABOVE_PASTDUE) {
		R54_180D_ABOVE_PASTDUE = r54_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR54_NON_ACCRUALS3() {
		return R54_NON_ACCRUALS3;
	}





	public void setR54_NON_ACCRUALS3(BigDecimal r54_NON_ACCRUALS3) {
		R54_NON_ACCRUALS3 = r54_NON_ACCRUALS3;
	}





	public BigDecimal getR54_SPECIFIC_PROV3() {
		return R54_SPECIFIC_PROV3;
	}





	public void setR54_SPECIFIC_PROV3(BigDecimal r54_SPECIFIC_PROV3) {
		R54_SPECIFIC_PROV3 = r54_SPECIFIC_PROV3;
	}





	public BigDecimal getR54_NO_OF_ACC3() {
		return R54_NO_OF_ACC3;
	}





	public void setR54_NO_OF_ACC3(BigDecimal r54_NO_OF_ACC3) {
		R54_NO_OF_ACC3 = r54_NO_OF_ACC3;
	}





	public BigDecimal getR54_TOTAL_NON_ACCRUAL() {
		return R54_TOTAL_NON_ACCRUAL;
	}





	public void setR54_TOTAL_NON_ACCRUAL(BigDecimal r54_TOTAL_NON_ACCRUAL) {
		R54_TOTAL_NON_ACCRUAL = r54_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR54_TOTAL_DUE_LOANS() {
		return R54_TOTAL_DUE_LOANS;
	}





	public void setR54_TOTAL_DUE_LOANS(BigDecimal r54_TOTAL_DUE_LOANS) {
		R54_TOTAL_DUE_LOANS = r54_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR54_TOTAL_PERFORMING_LOAN() {
		return R54_TOTAL_PERFORMING_LOAN;
	}





	public void setR54_TOTAL_PERFORMING_LOAN(BigDecimal r54_TOTAL_PERFORMING_LOAN) {
		R54_TOTAL_PERFORMING_LOAN = r54_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR54_VALUE_OF_COLLATERAL() {
		return R54_VALUE_OF_COLLATERAL;
	}





	public void setR54_VALUE_OF_COLLATERAL(BigDecimal r54_VALUE_OF_COLLATERAL) {
		R54_VALUE_OF_COLLATERAL = r54_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR54_TOTAL_VALUE_NPL() {
		return R54_TOTAL_VALUE_NPL;
	}





	public void setR54_TOTAL_VALUE_NPL(BigDecimal r54_TOTAL_VALUE_NPL) {
		R54_TOTAL_VALUE_NPL = r54_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR54_TOTAL_SPECIFIC_PROV() {
		return R54_TOTAL_SPECIFIC_PROV;
	}





	public void setR54_TOTAL_SPECIFIC_PROV(BigDecimal r54_TOTAL_SPECIFIC_PROV) {
		R54_TOTAL_SPECIFIC_PROV = r54_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR54_SPECIFIC_PROV_NPL() {
		return R54_SPECIFIC_PROV_NPL;
	}





	public void setR54_SPECIFIC_PROV_NPL(BigDecimal r54_SPECIFIC_PROV_NPL) {
		R54_SPECIFIC_PROV_NPL = r54_SPECIFIC_PROV_NPL;
	}





	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}





	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}





	public BigDecimal getR55_30D_90D_PASTDUE() {
		return R55_30D_90D_PASTDUE;
	}





	public void setR55_30D_90D_PASTDUE(BigDecimal r55_30d_90d_PASTDUE) {
		R55_30D_90D_PASTDUE = r55_30d_90d_PASTDUE;
	}





	public BigDecimal getR55_NON_PERFORM_LOANS() {
		return R55_NON_PERFORM_LOANS;
	}





	public void setR55_NON_PERFORM_LOANS(BigDecimal r55_NON_PERFORM_LOANS) {
		R55_NON_PERFORM_LOANS = r55_NON_PERFORM_LOANS;
	}





	public BigDecimal getR55_NON_ACCRUALS1() {
		return R55_NON_ACCRUALS1;
	}





	public void setR55_NON_ACCRUALS1(BigDecimal r55_NON_ACCRUALS1) {
		R55_NON_ACCRUALS1 = r55_NON_ACCRUALS1;
	}





	public BigDecimal getR55_SPECIFIC_PROV1() {
		return R55_SPECIFIC_PROV1;
	}





	public void setR55_SPECIFIC_PROV1(BigDecimal r55_SPECIFIC_PROV1) {
		R55_SPECIFIC_PROV1 = r55_SPECIFIC_PROV1;
	}





	public BigDecimal getR55_NO_OF_ACC1() {
		return R55_NO_OF_ACC1;
	}





	public void setR55_NO_OF_ACC1(BigDecimal r55_NO_OF_ACC1) {
		R55_NO_OF_ACC1 = r55_NO_OF_ACC1;
	}





	public BigDecimal getR55_90D_180D_PASTDUE() {
		return R55_90D_180D_PASTDUE;
	}





	public void setR55_90D_180D_PASTDUE(BigDecimal r55_90d_180d_PASTDUE) {
		R55_90D_180D_PASTDUE = r55_90d_180d_PASTDUE;
	}





	public BigDecimal getR55_NON_ACCRUALS2() {
		return R55_NON_ACCRUALS2;
	}





	public void setR55_NON_ACCRUALS2(BigDecimal r55_NON_ACCRUALS2) {
		R55_NON_ACCRUALS2 = r55_NON_ACCRUALS2;
	}





	public BigDecimal getR55_SPECIFIC_PROV2() {
		return R55_SPECIFIC_PROV2;
	}





	public void setR55_SPECIFIC_PROV2(BigDecimal r55_SPECIFIC_PROV2) {
		R55_SPECIFIC_PROV2 = r55_SPECIFIC_PROV2;
	}





	public BigDecimal getR55_NO_OF_ACC2() {
		return R55_NO_OF_ACC2;
	}





	public void setR55_NO_OF_ACC2(BigDecimal r55_NO_OF_ACC2) {
		R55_NO_OF_ACC2 = r55_NO_OF_ACC2;
	}





	public BigDecimal getR55_180D_ABOVE_PASTDUE() {
		return R55_180D_ABOVE_PASTDUE;
	}





	public void setR55_180D_ABOVE_PASTDUE(BigDecimal r55_180d_ABOVE_PASTDUE) {
		R55_180D_ABOVE_PASTDUE = r55_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR55_NON_ACCRUALS3() {
		return R55_NON_ACCRUALS3;
	}





	public void setR55_NON_ACCRUALS3(BigDecimal r55_NON_ACCRUALS3) {
		R55_NON_ACCRUALS3 = r55_NON_ACCRUALS3;
	}





	public BigDecimal getR55_SPECIFIC_PROV3() {
		return R55_SPECIFIC_PROV3;
	}





	public void setR55_SPECIFIC_PROV3(BigDecimal r55_SPECIFIC_PROV3) {
		R55_SPECIFIC_PROV3 = r55_SPECIFIC_PROV3;
	}





	public BigDecimal getR55_NO_OF_ACC3() {
		return R55_NO_OF_ACC3;
	}





	public void setR55_NO_OF_ACC3(BigDecimal r55_NO_OF_ACC3) {
		R55_NO_OF_ACC3 = r55_NO_OF_ACC3;
	}





	public BigDecimal getR55_TOTAL_NON_ACCRUAL() {
		return R55_TOTAL_NON_ACCRUAL;
	}





	public void setR55_TOTAL_NON_ACCRUAL(BigDecimal r55_TOTAL_NON_ACCRUAL) {
		R55_TOTAL_NON_ACCRUAL = r55_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR55_TOTAL_DUE_LOANS() {
		return R55_TOTAL_DUE_LOANS;
	}





	public void setR55_TOTAL_DUE_LOANS(BigDecimal r55_TOTAL_DUE_LOANS) {
		R55_TOTAL_DUE_LOANS = r55_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR55_TOTAL_PERFORMING_LOAN() {
		return R55_TOTAL_PERFORMING_LOAN;
	}





	public void setR55_TOTAL_PERFORMING_LOAN(BigDecimal r55_TOTAL_PERFORMING_LOAN) {
		R55_TOTAL_PERFORMING_LOAN = r55_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR55_VALUE_OF_COLLATERAL() {
		return R55_VALUE_OF_COLLATERAL;
	}





	public void setR55_VALUE_OF_COLLATERAL(BigDecimal r55_VALUE_OF_COLLATERAL) {
		R55_VALUE_OF_COLLATERAL = r55_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR55_TOTAL_VALUE_NPL() {
		return R55_TOTAL_VALUE_NPL;
	}





	public void setR55_TOTAL_VALUE_NPL(BigDecimal r55_TOTAL_VALUE_NPL) {
		R55_TOTAL_VALUE_NPL = r55_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR55_TOTAL_SPECIFIC_PROV() {
		return R55_TOTAL_SPECIFIC_PROV;
	}





	public void setR55_TOTAL_SPECIFIC_PROV(BigDecimal r55_TOTAL_SPECIFIC_PROV) {
		R55_TOTAL_SPECIFIC_PROV = r55_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR55_SPECIFIC_PROV_NPL() {
		return R55_SPECIFIC_PROV_NPL;
	}





	public void setR55_SPECIFIC_PROV_NPL(BigDecimal r55_SPECIFIC_PROV_NPL) {
		R55_SPECIFIC_PROV_NPL = r55_SPECIFIC_PROV_NPL;
	}





	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}





	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}





	public BigDecimal getR56_30D_90D_PASTDUE() {
		return R56_30D_90D_PASTDUE;
	}





	public void setR56_30D_90D_PASTDUE(BigDecimal r56_30d_90d_PASTDUE) {
		R56_30D_90D_PASTDUE = r56_30d_90d_PASTDUE;
	}





	public BigDecimal getR56_NON_PERFORM_LOANS() {
		return R56_NON_PERFORM_LOANS;
	}





	public void setR56_NON_PERFORM_LOANS(BigDecimal r56_NON_PERFORM_LOANS) {
		R56_NON_PERFORM_LOANS = r56_NON_PERFORM_LOANS;
	}





	public BigDecimal getR56_NON_ACCRUALS1() {
		return R56_NON_ACCRUALS1;
	}





	public void setR56_NON_ACCRUALS1(BigDecimal r56_NON_ACCRUALS1) {
		R56_NON_ACCRUALS1 = r56_NON_ACCRUALS1;
	}





	public BigDecimal getR56_SPECIFIC_PROV1() {
		return R56_SPECIFIC_PROV1;
	}





	public void setR56_SPECIFIC_PROV1(BigDecimal r56_SPECIFIC_PROV1) {
		R56_SPECIFIC_PROV1 = r56_SPECIFIC_PROV1;
	}





	public BigDecimal getR56_NO_OF_ACC1() {
		return R56_NO_OF_ACC1;
	}





	public void setR56_NO_OF_ACC1(BigDecimal r56_NO_OF_ACC1) {
		R56_NO_OF_ACC1 = r56_NO_OF_ACC1;
	}





	public BigDecimal getR56_90D_180D_PASTDUE() {
		return R56_90D_180D_PASTDUE;
	}





	public void setR56_90D_180D_PASTDUE(BigDecimal r56_90d_180d_PASTDUE) {
		R56_90D_180D_PASTDUE = r56_90d_180d_PASTDUE;
	}





	public BigDecimal getR56_NON_ACCRUALS2() {
		return R56_NON_ACCRUALS2;
	}





	public void setR56_NON_ACCRUALS2(BigDecimal r56_NON_ACCRUALS2) {
		R56_NON_ACCRUALS2 = r56_NON_ACCRUALS2;
	}





	public BigDecimal getR56_SPECIFIC_PROV2() {
		return R56_SPECIFIC_PROV2;
	}





	public void setR56_SPECIFIC_PROV2(BigDecimal r56_SPECIFIC_PROV2) {
		R56_SPECIFIC_PROV2 = r56_SPECIFIC_PROV2;
	}





	public BigDecimal getR56_NO_OF_ACC2() {
		return R56_NO_OF_ACC2;
	}





	public void setR56_NO_OF_ACC2(BigDecimal r56_NO_OF_ACC2) {
		R56_NO_OF_ACC2 = r56_NO_OF_ACC2;
	}





	public BigDecimal getR56_180D_ABOVE_PASTDUE() {
		return R56_180D_ABOVE_PASTDUE;
	}





	public void setR56_180D_ABOVE_PASTDUE(BigDecimal r56_180d_ABOVE_PASTDUE) {
		R56_180D_ABOVE_PASTDUE = r56_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR56_NON_ACCRUALS3() {
		return R56_NON_ACCRUALS3;
	}





	public void setR56_NON_ACCRUALS3(BigDecimal r56_NON_ACCRUALS3) {
		R56_NON_ACCRUALS3 = r56_NON_ACCRUALS3;
	}





	public BigDecimal getR56_SPECIFIC_PROV3() {
		return R56_SPECIFIC_PROV3;
	}





	public void setR56_SPECIFIC_PROV3(BigDecimal r56_SPECIFIC_PROV3) {
		R56_SPECIFIC_PROV3 = r56_SPECIFIC_PROV3;
	}





	public BigDecimal getR56_NO_OF_ACC3() {
		return R56_NO_OF_ACC3;
	}





	public void setR56_NO_OF_ACC3(BigDecimal r56_NO_OF_ACC3) {
		R56_NO_OF_ACC3 = r56_NO_OF_ACC3;
	}





	public BigDecimal getR56_TOTAL_NON_ACCRUAL() {
		return R56_TOTAL_NON_ACCRUAL;
	}





	public void setR56_TOTAL_NON_ACCRUAL(BigDecimal r56_TOTAL_NON_ACCRUAL) {
		R56_TOTAL_NON_ACCRUAL = r56_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR56_TOTAL_DUE_LOANS() {
		return R56_TOTAL_DUE_LOANS;
	}





	public void setR56_TOTAL_DUE_LOANS(BigDecimal r56_TOTAL_DUE_LOANS) {
		R56_TOTAL_DUE_LOANS = r56_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR56_TOTAL_PERFORMING_LOAN() {
		return R56_TOTAL_PERFORMING_LOAN;
	}





	public void setR56_TOTAL_PERFORMING_LOAN(BigDecimal r56_TOTAL_PERFORMING_LOAN) {
		R56_TOTAL_PERFORMING_LOAN = r56_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR56_VALUE_OF_COLLATERAL() {
		return R56_VALUE_OF_COLLATERAL;
	}





	public void setR56_VALUE_OF_COLLATERAL(BigDecimal r56_VALUE_OF_COLLATERAL) {
		R56_VALUE_OF_COLLATERAL = r56_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR56_TOTAL_VALUE_NPL() {
		return R56_TOTAL_VALUE_NPL;
	}





	public void setR56_TOTAL_VALUE_NPL(BigDecimal r56_TOTAL_VALUE_NPL) {
		R56_TOTAL_VALUE_NPL = r56_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR56_TOTAL_SPECIFIC_PROV() {
		return R56_TOTAL_SPECIFIC_PROV;
	}





	public void setR56_TOTAL_SPECIFIC_PROV(BigDecimal r56_TOTAL_SPECIFIC_PROV) {
		R56_TOTAL_SPECIFIC_PROV = r56_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR56_SPECIFIC_PROV_NPL() {
		return R56_SPECIFIC_PROV_NPL;
	}





	public void setR56_SPECIFIC_PROV_NPL(BigDecimal r56_SPECIFIC_PROV_NPL) {
		R56_SPECIFIC_PROV_NPL = r56_SPECIFIC_PROV_NPL;
	}





	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}





	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}





	public BigDecimal getR57_30D_90D_PASTDUE() {
		return R57_30D_90D_PASTDUE;
	}





	public void setR57_30D_90D_PASTDUE(BigDecimal r57_30d_90d_PASTDUE) {
		R57_30D_90D_PASTDUE = r57_30d_90d_PASTDUE;
	}





	public BigDecimal getR57_NON_PERFORM_LOANS() {
		return R57_NON_PERFORM_LOANS;
	}





	public void setR57_NON_PERFORM_LOANS(BigDecimal r57_NON_PERFORM_LOANS) {
		R57_NON_PERFORM_LOANS = r57_NON_PERFORM_LOANS;
	}





	public BigDecimal getR57_NON_ACCRUALS1() {
		return R57_NON_ACCRUALS1;
	}





	public void setR57_NON_ACCRUALS1(BigDecimal r57_NON_ACCRUALS1) {
		R57_NON_ACCRUALS1 = r57_NON_ACCRUALS1;
	}





	public BigDecimal getR57_SPECIFIC_PROV1() {
		return R57_SPECIFIC_PROV1;
	}





	public void setR57_SPECIFIC_PROV1(BigDecimal r57_SPECIFIC_PROV1) {
		R57_SPECIFIC_PROV1 = r57_SPECIFIC_PROV1;
	}





	public BigDecimal getR57_NO_OF_ACC1() {
		return R57_NO_OF_ACC1;
	}





	public void setR57_NO_OF_ACC1(BigDecimal r57_NO_OF_ACC1) {
		R57_NO_OF_ACC1 = r57_NO_OF_ACC1;
	}





	public BigDecimal getR57_90D_180D_PASTDUE() {
		return R57_90D_180D_PASTDUE;
	}





	public void setR57_90D_180D_PASTDUE(BigDecimal r57_90d_180d_PASTDUE) {
		R57_90D_180D_PASTDUE = r57_90d_180d_PASTDUE;
	}





	public BigDecimal getR57_NON_ACCRUALS2() {
		return R57_NON_ACCRUALS2;
	}





	public void setR57_NON_ACCRUALS2(BigDecimal r57_NON_ACCRUALS2) {
		R57_NON_ACCRUALS2 = r57_NON_ACCRUALS2;
	}





	public BigDecimal getR57_SPECIFIC_PROV2() {
		return R57_SPECIFIC_PROV2;
	}





	public void setR57_SPECIFIC_PROV2(BigDecimal r57_SPECIFIC_PROV2) {
		R57_SPECIFIC_PROV2 = r57_SPECIFIC_PROV2;
	}





	public BigDecimal getR57_NO_OF_ACC2() {
		return R57_NO_OF_ACC2;
	}





	public void setR57_NO_OF_ACC2(BigDecimal r57_NO_OF_ACC2) {
		R57_NO_OF_ACC2 = r57_NO_OF_ACC2;
	}





	public BigDecimal getR57_180D_ABOVE_PASTDUE() {
		return R57_180D_ABOVE_PASTDUE;
	}





	public void setR57_180D_ABOVE_PASTDUE(BigDecimal r57_180d_ABOVE_PASTDUE) {
		R57_180D_ABOVE_PASTDUE = r57_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR57_NON_ACCRUALS3() {
		return R57_NON_ACCRUALS3;
	}





	public void setR57_NON_ACCRUALS3(BigDecimal r57_NON_ACCRUALS3) {
		R57_NON_ACCRUALS3 = r57_NON_ACCRUALS3;
	}





	public BigDecimal getR57_SPECIFIC_PROV3() {
		return R57_SPECIFIC_PROV3;
	}





	public void setR57_SPECIFIC_PROV3(BigDecimal r57_SPECIFIC_PROV3) {
		R57_SPECIFIC_PROV3 = r57_SPECIFIC_PROV3;
	}





	public BigDecimal getR57_NO_OF_ACC3() {
		return R57_NO_OF_ACC3;
	}





	public void setR57_NO_OF_ACC3(BigDecimal r57_NO_OF_ACC3) {
		R57_NO_OF_ACC3 = r57_NO_OF_ACC3;
	}





	public BigDecimal getR57_TOTAL_NON_ACCRUAL() {
		return R57_TOTAL_NON_ACCRUAL;
	}





	public void setR57_TOTAL_NON_ACCRUAL(BigDecimal r57_TOTAL_NON_ACCRUAL) {
		R57_TOTAL_NON_ACCRUAL = r57_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR57_TOTAL_DUE_LOANS() {
		return R57_TOTAL_DUE_LOANS;
	}





	public void setR57_TOTAL_DUE_LOANS(BigDecimal r57_TOTAL_DUE_LOANS) {
		R57_TOTAL_DUE_LOANS = r57_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR57_TOTAL_PERFORMING_LOAN() {
		return R57_TOTAL_PERFORMING_LOAN;
	}





	public void setR57_TOTAL_PERFORMING_LOAN(BigDecimal r57_TOTAL_PERFORMING_LOAN) {
		R57_TOTAL_PERFORMING_LOAN = r57_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR57_VALUE_OF_COLLATERAL() {
		return R57_VALUE_OF_COLLATERAL;
	}





	public void setR57_VALUE_OF_COLLATERAL(BigDecimal r57_VALUE_OF_COLLATERAL) {
		R57_VALUE_OF_COLLATERAL = r57_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR57_TOTAL_VALUE_NPL() {
		return R57_TOTAL_VALUE_NPL;
	}





	public void setR57_TOTAL_VALUE_NPL(BigDecimal r57_TOTAL_VALUE_NPL) {
		R57_TOTAL_VALUE_NPL = r57_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR57_TOTAL_SPECIFIC_PROV() {
		return R57_TOTAL_SPECIFIC_PROV;
	}





	public void setR57_TOTAL_SPECIFIC_PROV(BigDecimal r57_TOTAL_SPECIFIC_PROV) {
		R57_TOTAL_SPECIFIC_PROV = r57_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR57_SPECIFIC_PROV_NPL() {
		return R57_SPECIFIC_PROV_NPL;
	}





	public void setR57_SPECIFIC_PROV_NPL(BigDecimal r57_SPECIFIC_PROV_NPL) {
		R57_SPECIFIC_PROV_NPL = r57_SPECIFIC_PROV_NPL;
	}





	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}





	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}





	public BigDecimal getR58_30D_90D_PASTDUE() {
		return R58_30D_90D_PASTDUE;
	}





	public void setR58_30D_90D_PASTDUE(BigDecimal r58_30d_90d_PASTDUE) {
		R58_30D_90D_PASTDUE = r58_30d_90d_PASTDUE;
	}





	public BigDecimal getR58_NON_PERFORM_LOANS() {
		return R58_NON_PERFORM_LOANS;
	}





	public void setR58_NON_PERFORM_LOANS(BigDecimal r58_NON_PERFORM_LOANS) {
		R58_NON_PERFORM_LOANS = r58_NON_PERFORM_LOANS;
	}





	public BigDecimal getR58_NON_ACCRUALS1() {
		return R58_NON_ACCRUALS1;
	}





	public void setR58_NON_ACCRUALS1(BigDecimal r58_NON_ACCRUALS1) {
		R58_NON_ACCRUALS1 = r58_NON_ACCRUALS1;
	}





	public BigDecimal getR58_SPECIFIC_PROV1() {
		return R58_SPECIFIC_PROV1;
	}





	public void setR58_SPECIFIC_PROV1(BigDecimal r58_SPECIFIC_PROV1) {
		R58_SPECIFIC_PROV1 = r58_SPECIFIC_PROV1;
	}





	public BigDecimal getR58_NO_OF_ACC1() {
		return R58_NO_OF_ACC1;
	}





	public void setR58_NO_OF_ACC1(BigDecimal r58_NO_OF_ACC1) {
		R58_NO_OF_ACC1 = r58_NO_OF_ACC1;
	}





	public BigDecimal getR58_90D_180D_PASTDUE() {
		return R58_90D_180D_PASTDUE;
	}





	public void setR58_90D_180D_PASTDUE(BigDecimal r58_90d_180d_PASTDUE) {
		R58_90D_180D_PASTDUE = r58_90d_180d_PASTDUE;
	}





	public BigDecimal getR58_NON_ACCRUALS2() {
		return R58_NON_ACCRUALS2;
	}





	public void setR58_NON_ACCRUALS2(BigDecimal r58_NON_ACCRUALS2) {
		R58_NON_ACCRUALS2 = r58_NON_ACCRUALS2;
	}





	public BigDecimal getR58_SPECIFIC_PROV2() {
		return R58_SPECIFIC_PROV2;
	}





	public void setR58_SPECIFIC_PROV2(BigDecimal r58_SPECIFIC_PROV2) {
		R58_SPECIFIC_PROV2 = r58_SPECIFIC_PROV2;
	}





	public BigDecimal getR58_NO_OF_ACC2() {
		return R58_NO_OF_ACC2;
	}





	public void setR58_NO_OF_ACC2(BigDecimal r58_NO_OF_ACC2) {
		R58_NO_OF_ACC2 = r58_NO_OF_ACC2;
	}





	public BigDecimal getR58_180D_ABOVE_PASTDUE() {
		return R58_180D_ABOVE_PASTDUE;
	}





	public void setR58_180D_ABOVE_PASTDUE(BigDecimal r58_180d_ABOVE_PASTDUE) {
		R58_180D_ABOVE_PASTDUE = r58_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR58_NON_ACCRUALS3() {
		return R58_NON_ACCRUALS3;
	}





	public void setR58_NON_ACCRUALS3(BigDecimal r58_NON_ACCRUALS3) {
		R58_NON_ACCRUALS3 = r58_NON_ACCRUALS3;
	}





	public BigDecimal getR58_SPECIFIC_PROV3() {
		return R58_SPECIFIC_PROV3;
	}





	public void setR58_SPECIFIC_PROV3(BigDecimal r58_SPECIFIC_PROV3) {
		R58_SPECIFIC_PROV3 = r58_SPECIFIC_PROV3;
	}





	public BigDecimal getR58_NO_OF_ACC3() {
		return R58_NO_OF_ACC3;
	}





	public void setR58_NO_OF_ACC3(BigDecimal r58_NO_OF_ACC3) {
		R58_NO_OF_ACC3 = r58_NO_OF_ACC3;
	}





	public BigDecimal getR58_TOTAL_NON_ACCRUAL() {
		return R58_TOTAL_NON_ACCRUAL;
	}





	public void setR58_TOTAL_NON_ACCRUAL(BigDecimal r58_TOTAL_NON_ACCRUAL) {
		R58_TOTAL_NON_ACCRUAL = r58_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR58_TOTAL_DUE_LOANS() {
		return R58_TOTAL_DUE_LOANS;
	}





	public void setR58_TOTAL_DUE_LOANS(BigDecimal r58_TOTAL_DUE_LOANS) {
		R58_TOTAL_DUE_LOANS = r58_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR58_TOTAL_PERFORMING_LOAN() {
		return R58_TOTAL_PERFORMING_LOAN;
	}





	public void setR58_TOTAL_PERFORMING_LOAN(BigDecimal r58_TOTAL_PERFORMING_LOAN) {
		R58_TOTAL_PERFORMING_LOAN = r58_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR58_VALUE_OF_COLLATERAL() {
		return R58_VALUE_OF_COLLATERAL;
	}





	public void setR58_VALUE_OF_COLLATERAL(BigDecimal r58_VALUE_OF_COLLATERAL) {
		R58_VALUE_OF_COLLATERAL = r58_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR58_TOTAL_VALUE_NPL() {
		return R58_TOTAL_VALUE_NPL;
	}





	public void setR58_TOTAL_VALUE_NPL(BigDecimal r58_TOTAL_VALUE_NPL) {
		R58_TOTAL_VALUE_NPL = r58_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR58_TOTAL_SPECIFIC_PROV() {
		return R58_TOTAL_SPECIFIC_PROV;
	}





	public void setR58_TOTAL_SPECIFIC_PROV(BigDecimal r58_TOTAL_SPECIFIC_PROV) {
		R58_TOTAL_SPECIFIC_PROV = r58_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR58_SPECIFIC_PROV_NPL() {
		return R58_SPECIFIC_PROV_NPL;
	}





	public void setR58_SPECIFIC_PROV_NPL(BigDecimal r58_SPECIFIC_PROV_NPL) {
		R58_SPECIFIC_PROV_NPL = r58_SPECIFIC_PROV_NPL;
	}





	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}





	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}





	public BigDecimal getR59_30D_90D_PASTDUE() {
		return R59_30D_90D_PASTDUE;
	}





	public void setR59_30D_90D_PASTDUE(BigDecimal r59_30d_90d_PASTDUE) {
		R59_30D_90D_PASTDUE = r59_30d_90d_PASTDUE;
	}





	public BigDecimal getR59_NON_PERFORM_LOANS() {
		return R59_NON_PERFORM_LOANS;
	}





	public void setR59_NON_PERFORM_LOANS(BigDecimal r59_NON_PERFORM_LOANS) {
		R59_NON_PERFORM_LOANS = r59_NON_PERFORM_LOANS;
	}





	public BigDecimal getR59_NON_ACCRUALS1() {
		return R59_NON_ACCRUALS1;
	}





	public void setR59_NON_ACCRUALS1(BigDecimal r59_NON_ACCRUALS1) {
		R59_NON_ACCRUALS1 = r59_NON_ACCRUALS1;
	}





	public BigDecimal getR59_SPECIFIC_PROV1() {
		return R59_SPECIFIC_PROV1;
	}





	public void setR59_SPECIFIC_PROV1(BigDecimal r59_SPECIFIC_PROV1) {
		R59_SPECIFIC_PROV1 = r59_SPECIFIC_PROV1;
	}





	public BigDecimal getR59_NO_OF_ACC1() {
		return R59_NO_OF_ACC1;
	}





	public void setR59_NO_OF_ACC1(BigDecimal r59_NO_OF_ACC1) {
		R59_NO_OF_ACC1 = r59_NO_OF_ACC1;
	}





	public BigDecimal getR59_90D_180D_PASTDUE() {
		return R59_90D_180D_PASTDUE;
	}





	public void setR59_90D_180D_PASTDUE(BigDecimal r59_90d_180d_PASTDUE) {
		R59_90D_180D_PASTDUE = r59_90d_180d_PASTDUE;
	}





	public BigDecimal getR59_NON_ACCRUALS2() {
		return R59_NON_ACCRUALS2;
	}





	public void setR59_NON_ACCRUALS2(BigDecimal r59_NON_ACCRUALS2) {
		R59_NON_ACCRUALS2 = r59_NON_ACCRUALS2;
	}





	public BigDecimal getR59_SPECIFIC_PROV2() {
		return R59_SPECIFIC_PROV2;
	}





	public void setR59_SPECIFIC_PROV2(BigDecimal r59_SPECIFIC_PROV2) {
		R59_SPECIFIC_PROV2 = r59_SPECIFIC_PROV2;
	}





	public BigDecimal getR59_NO_OF_ACC2() {
		return R59_NO_OF_ACC2;
	}





	public void setR59_NO_OF_ACC2(BigDecimal r59_NO_OF_ACC2) {
		R59_NO_OF_ACC2 = r59_NO_OF_ACC2;
	}





	public BigDecimal getR59_180D_ABOVE_PASTDUE() {
		return R59_180D_ABOVE_PASTDUE;
	}





	public void setR59_180D_ABOVE_PASTDUE(BigDecimal r59_180d_ABOVE_PASTDUE) {
		R59_180D_ABOVE_PASTDUE = r59_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR59_NON_ACCRUALS3() {
		return R59_NON_ACCRUALS3;
	}





	public void setR59_NON_ACCRUALS3(BigDecimal r59_NON_ACCRUALS3) {
		R59_NON_ACCRUALS3 = r59_NON_ACCRUALS3;
	}





	public BigDecimal getR59_SPECIFIC_PROV3() {
		return R59_SPECIFIC_PROV3;
	}





	public void setR59_SPECIFIC_PROV3(BigDecimal r59_SPECIFIC_PROV3) {
		R59_SPECIFIC_PROV3 = r59_SPECIFIC_PROV3;
	}





	public BigDecimal getR59_NO_OF_ACC3() {
		return R59_NO_OF_ACC3;
	}





	public void setR59_NO_OF_ACC3(BigDecimal r59_NO_OF_ACC3) {
		R59_NO_OF_ACC3 = r59_NO_OF_ACC3;
	}





	public BigDecimal getR59_TOTAL_NON_ACCRUAL() {
		return R59_TOTAL_NON_ACCRUAL;
	}





	public void setR59_TOTAL_NON_ACCRUAL(BigDecimal r59_TOTAL_NON_ACCRUAL) {
		R59_TOTAL_NON_ACCRUAL = r59_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR59_TOTAL_DUE_LOANS() {
		return R59_TOTAL_DUE_LOANS;
	}





	public void setR59_TOTAL_DUE_LOANS(BigDecimal r59_TOTAL_DUE_LOANS) {
		R59_TOTAL_DUE_LOANS = r59_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR59_TOTAL_PERFORMING_LOAN() {
		return R59_TOTAL_PERFORMING_LOAN;
	}





	public void setR59_TOTAL_PERFORMING_LOAN(BigDecimal r59_TOTAL_PERFORMING_LOAN) {
		R59_TOTAL_PERFORMING_LOAN = r59_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR59_VALUE_OF_COLLATERAL() {
		return R59_VALUE_OF_COLLATERAL;
	}





	public void setR59_VALUE_OF_COLLATERAL(BigDecimal r59_VALUE_OF_COLLATERAL) {
		R59_VALUE_OF_COLLATERAL = r59_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR59_TOTAL_VALUE_NPL() {
		return R59_TOTAL_VALUE_NPL;
	}





	public void setR59_TOTAL_VALUE_NPL(BigDecimal r59_TOTAL_VALUE_NPL) {
		R59_TOTAL_VALUE_NPL = r59_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR59_TOTAL_SPECIFIC_PROV() {
		return R59_TOTAL_SPECIFIC_PROV;
	}





	public void setR59_TOTAL_SPECIFIC_PROV(BigDecimal r59_TOTAL_SPECIFIC_PROV) {
		R59_TOTAL_SPECIFIC_PROV = r59_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR59_SPECIFIC_PROV_NPL() {
		return R59_SPECIFIC_PROV_NPL;
	}





	public void setR59_SPECIFIC_PROV_NPL(BigDecimal r59_SPECIFIC_PROV_NPL) {
		R59_SPECIFIC_PROV_NPL = r59_SPECIFIC_PROV_NPL;
	}





	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}





	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}





	public BigDecimal getR60_30D_90D_PASTDUE() {
		return R60_30D_90D_PASTDUE;
	}





	public void setR60_30D_90D_PASTDUE(BigDecimal r60_30d_90d_PASTDUE) {
		R60_30D_90D_PASTDUE = r60_30d_90d_PASTDUE;
	}





	public BigDecimal getR60_NON_PERFORM_LOANS() {
		return R60_NON_PERFORM_LOANS;
	}





	public void setR60_NON_PERFORM_LOANS(BigDecimal r60_NON_PERFORM_LOANS) {
		R60_NON_PERFORM_LOANS = r60_NON_PERFORM_LOANS;
	}





	public BigDecimal getR60_NON_ACCRUALS1() {
		return R60_NON_ACCRUALS1;
	}





	public void setR60_NON_ACCRUALS1(BigDecimal r60_NON_ACCRUALS1) {
		R60_NON_ACCRUALS1 = r60_NON_ACCRUALS1;
	}





	public BigDecimal getR60_SPECIFIC_PROV1() {
		return R60_SPECIFIC_PROV1;
	}





	public void setR60_SPECIFIC_PROV1(BigDecimal r60_SPECIFIC_PROV1) {
		R60_SPECIFIC_PROV1 = r60_SPECIFIC_PROV1;
	}





	public BigDecimal getR60_NO_OF_ACC1() {
		return R60_NO_OF_ACC1;
	}





	public void setR60_NO_OF_ACC1(BigDecimal r60_NO_OF_ACC1) {
		R60_NO_OF_ACC1 = r60_NO_OF_ACC1;
	}





	public BigDecimal getR60_90D_180D_PASTDUE() {
		return R60_90D_180D_PASTDUE;
	}





	public void setR60_90D_180D_PASTDUE(BigDecimal r60_90d_180d_PASTDUE) {
		R60_90D_180D_PASTDUE = r60_90d_180d_PASTDUE;
	}





	public BigDecimal getR60_NON_ACCRUALS2() {
		return R60_NON_ACCRUALS2;
	}





	public void setR60_NON_ACCRUALS2(BigDecimal r60_NON_ACCRUALS2) {
		R60_NON_ACCRUALS2 = r60_NON_ACCRUALS2;
	}





	public BigDecimal getR60_SPECIFIC_PROV2() {
		return R60_SPECIFIC_PROV2;
	}





	public void setR60_SPECIFIC_PROV2(BigDecimal r60_SPECIFIC_PROV2) {
		R60_SPECIFIC_PROV2 = r60_SPECIFIC_PROV2;
	}





	public BigDecimal getR60_NO_OF_ACC2() {
		return R60_NO_OF_ACC2;
	}





	public void setR60_NO_OF_ACC2(BigDecimal r60_NO_OF_ACC2) {
		R60_NO_OF_ACC2 = r60_NO_OF_ACC2;
	}





	public BigDecimal getR60_180D_ABOVE_PASTDUE() {
		return R60_180D_ABOVE_PASTDUE;
	}





	public void setR60_180D_ABOVE_PASTDUE(BigDecimal r60_180d_ABOVE_PASTDUE) {
		R60_180D_ABOVE_PASTDUE = r60_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR60_NON_ACCRUALS3() {
		return R60_NON_ACCRUALS3;
	}





	public void setR60_NON_ACCRUALS3(BigDecimal r60_NON_ACCRUALS3) {
		R60_NON_ACCRUALS3 = r60_NON_ACCRUALS3;
	}





	public BigDecimal getR60_SPECIFIC_PROV3() {
		return R60_SPECIFIC_PROV3;
	}





	public void setR60_SPECIFIC_PROV3(BigDecimal r60_SPECIFIC_PROV3) {
		R60_SPECIFIC_PROV3 = r60_SPECIFIC_PROV3;
	}





	public BigDecimal getR60_NO_OF_ACC3() {
		return R60_NO_OF_ACC3;
	}





	public void setR60_NO_OF_ACC3(BigDecimal r60_NO_OF_ACC3) {
		R60_NO_OF_ACC3 = r60_NO_OF_ACC3;
	}





	public BigDecimal getR60_TOTAL_NON_ACCRUAL() {
		return R60_TOTAL_NON_ACCRUAL;
	}





	public void setR60_TOTAL_NON_ACCRUAL(BigDecimal r60_TOTAL_NON_ACCRUAL) {
		R60_TOTAL_NON_ACCRUAL = r60_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR60_TOTAL_DUE_LOANS() {
		return R60_TOTAL_DUE_LOANS;
	}





	public void setR60_TOTAL_DUE_LOANS(BigDecimal r60_TOTAL_DUE_LOANS) {
		R60_TOTAL_DUE_LOANS = r60_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR60_TOTAL_PERFORMING_LOAN() {
		return R60_TOTAL_PERFORMING_LOAN;
	}





	public void setR60_TOTAL_PERFORMING_LOAN(BigDecimal r60_TOTAL_PERFORMING_LOAN) {
		R60_TOTAL_PERFORMING_LOAN = r60_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR60_VALUE_OF_COLLATERAL() {
		return R60_VALUE_OF_COLLATERAL;
	}





	public void setR60_VALUE_OF_COLLATERAL(BigDecimal r60_VALUE_OF_COLLATERAL) {
		R60_VALUE_OF_COLLATERAL = r60_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR60_TOTAL_VALUE_NPL() {
		return R60_TOTAL_VALUE_NPL;
	}





	public void setR60_TOTAL_VALUE_NPL(BigDecimal r60_TOTAL_VALUE_NPL) {
		R60_TOTAL_VALUE_NPL = r60_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR60_TOTAL_SPECIFIC_PROV() {
		return R60_TOTAL_SPECIFIC_PROV;
	}





	public void setR60_TOTAL_SPECIFIC_PROV(BigDecimal r60_TOTAL_SPECIFIC_PROV) {
		R60_TOTAL_SPECIFIC_PROV = r60_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR60_SPECIFIC_PROV_NPL() {
		return R60_SPECIFIC_PROV_NPL;
	}





	public void setR60_SPECIFIC_PROV_NPL(BigDecimal r60_SPECIFIC_PROV_NPL) {
		R60_SPECIFIC_PROV_NPL = r60_SPECIFIC_PROV_NPL;
	}





	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}





	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}





	public BigDecimal getR61_30D_90D_PASTDUE() {
		return R61_30D_90D_PASTDUE;
	}





	public void setR61_30D_90D_PASTDUE(BigDecimal r61_30d_90d_PASTDUE) {
		R61_30D_90D_PASTDUE = r61_30d_90d_PASTDUE;
	}





	public BigDecimal getR61_NON_PERFORM_LOANS() {
		return R61_NON_PERFORM_LOANS;
	}





	public void setR61_NON_PERFORM_LOANS(BigDecimal r61_NON_PERFORM_LOANS) {
		R61_NON_PERFORM_LOANS = r61_NON_PERFORM_LOANS;
	}





	public BigDecimal getR61_NON_ACCRUALS1() {
		return R61_NON_ACCRUALS1;
	}





	public void setR61_NON_ACCRUALS1(BigDecimal r61_NON_ACCRUALS1) {
		R61_NON_ACCRUALS1 = r61_NON_ACCRUALS1;
	}





	public BigDecimal getR61_SPECIFIC_PROV1() {
		return R61_SPECIFIC_PROV1;
	}





	public void setR61_SPECIFIC_PROV1(BigDecimal r61_SPECIFIC_PROV1) {
		R61_SPECIFIC_PROV1 = r61_SPECIFIC_PROV1;
	}





	public BigDecimal getR61_NO_OF_ACC1() {
		return R61_NO_OF_ACC1;
	}





	public void setR61_NO_OF_ACC1(BigDecimal r61_NO_OF_ACC1) {
		R61_NO_OF_ACC1 = r61_NO_OF_ACC1;
	}





	public BigDecimal getR61_90D_180D_PASTDUE() {
		return R61_90D_180D_PASTDUE;
	}





	public void setR61_90D_180D_PASTDUE(BigDecimal r61_90d_180d_PASTDUE) {
		R61_90D_180D_PASTDUE = r61_90d_180d_PASTDUE;
	}





	public BigDecimal getR61_NON_ACCRUALS2() {
		return R61_NON_ACCRUALS2;
	}





	public void setR61_NON_ACCRUALS2(BigDecimal r61_NON_ACCRUALS2) {
		R61_NON_ACCRUALS2 = r61_NON_ACCRUALS2;
	}





	public BigDecimal getR61_SPECIFIC_PROV2() {
		return R61_SPECIFIC_PROV2;
	}





	public void setR61_SPECIFIC_PROV2(BigDecimal r61_SPECIFIC_PROV2) {
		R61_SPECIFIC_PROV2 = r61_SPECIFIC_PROV2;
	}





	public BigDecimal getR61_NO_OF_ACC2() {
		return R61_NO_OF_ACC2;
	}





	public void setR61_NO_OF_ACC2(BigDecimal r61_NO_OF_ACC2) {
		R61_NO_OF_ACC2 = r61_NO_OF_ACC2;
	}





	public BigDecimal getR61_180D_ABOVE_PASTDUE() {
		return R61_180D_ABOVE_PASTDUE;
	}





	public void setR61_180D_ABOVE_PASTDUE(BigDecimal r61_180d_ABOVE_PASTDUE) {
		R61_180D_ABOVE_PASTDUE = r61_180d_ABOVE_PASTDUE;
	}





	public BigDecimal getR61_NON_ACCRUALS3() {
		return R61_NON_ACCRUALS3;
	}





	public void setR61_NON_ACCRUALS3(BigDecimal r61_NON_ACCRUALS3) {
		R61_NON_ACCRUALS3 = r61_NON_ACCRUALS3;
	}





	public BigDecimal getR61_SPECIFIC_PROV3() {
		return R61_SPECIFIC_PROV3;
	}





	public void setR61_SPECIFIC_PROV3(BigDecimal r61_SPECIFIC_PROV3) {
		R61_SPECIFIC_PROV3 = r61_SPECIFIC_PROV3;
	}





	public BigDecimal getR61_NO_OF_ACC3() {
		return R61_NO_OF_ACC3;
	}





	public void setR61_NO_OF_ACC3(BigDecimal r61_NO_OF_ACC3) {
		R61_NO_OF_ACC3 = r61_NO_OF_ACC3;
	}





	public BigDecimal getR61_TOTAL_NON_ACCRUAL() {
		return R61_TOTAL_NON_ACCRUAL;
	}





	public void setR61_TOTAL_NON_ACCRUAL(BigDecimal r61_TOTAL_NON_ACCRUAL) {
		R61_TOTAL_NON_ACCRUAL = r61_TOTAL_NON_ACCRUAL;
	}





	public BigDecimal getR61_TOTAL_DUE_LOANS() {
		return R61_TOTAL_DUE_LOANS;
	}





	public void setR61_TOTAL_DUE_LOANS(BigDecimal r61_TOTAL_DUE_LOANS) {
		R61_TOTAL_DUE_LOANS = r61_TOTAL_DUE_LOANS;
	}





	public BigDecimal getR61_TOTAL_PERFORMING_LOAN() {
		return R61_TOTAL_PERFORMING_LOAN;
	}





	public void setR61_TOTAL_PERFORMING_LOAN(BigDecimal r61_TOTAL_PERFORMING_LOAN) {
		R61_TOTAL_PERFORMING_LOAN = r61_TOTAL_PERFORMING_LOAN;
	}





	public BigDecimal getR61_VALUE_OF_COLLATERAL() {
		return R61_VALUE_OF_COLLATERAL;
	}





	public void setR61_VALUE_OF_COLLATERAL(BigDecimal r61_VALUE_OF_COLLATERAL) {
		R61_VALUE_OF_COLLATERAL = r61_VALUE_OF_COLLATERAL;
	}





	public BigDecimal getR61_TOTAL_VALUE_NPL() {
		return R61_TOTAL_VALUE_NPL;
	}





	public void setR61_TOTAL_VALUE_NPL(BigDecimal r61_TOTAL_VALUE_NPL) {
		R61_TOTAL_VALUE_NPL = r61_TOTAL_VALUE_NPL;
	}





	public BigDecimal getR61_TOTAL_SPECIFIC_PROV() {
		return R61_TOTAL_SPECIFIC_PROV;
	}





	public void setR61_TOTAL_SPECIFIC_PROV(BigDecimal r61_TOTAL_SPECIFIC_PROV) {
		R61_TOTAL_SPECIFIC_PROV = r61_TOTAL_SPECIFIC_PROV;
	}





	public BigDecimal getR61_SPECIFIC_PROV_NPL() {
		return R61_SPECIFIC_PROV_NPL;
	}





	public void setR61_SPECIFIC_PROV_NPL(BigDecimal r61_SPECIFIC_PROV_NPL) {
		R61_SPECIFIC_PROV_NPL = r61_SPECIFIC_PROV_NPL;
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





	public M_PD_Summary_Entity2() {
        super();
    }

}