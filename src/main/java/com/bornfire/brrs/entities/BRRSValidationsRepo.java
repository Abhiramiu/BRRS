package com.bornfire.brrs.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRSValidationsRepo extends JpaRepository<BRRSValidations, String> {

	@Query(value = "select * from BRRS_REPORT_VALIDATION_TABLE where rpt_code=?1 ORDER BY srl_no ", nativeQuery = true)
	List<BRRSValidations> getValidationList(String rpt_code);

	/*
	 * /// BRF-1 // Srl_no1 // MCBL
	 * 
	 * @Query(value =
	 * "Select count(*) from GENERAL_MASTER_TABLE where REPORT_DATE=?1 AND REPORT_CODE ='MCBL' "
	 * , nativeQuery = true) Integer getCheckSrlNo1(String report_date);
	 */

	// Srl_no1
	// M_SFINP1 summarytable
	@Query(value = "Select count(*) from BRRS_M_SFINP1_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo1(String report_date);

	/// M_SFINP1
	//// Srl_no2 (Total ASSETS and LIABILITIES(MONTH END IN M_SFINP1,M_SFINP2)
	@Query(value = "SELECT b.R61_MONTH_END, c.R80_MONTH_END "
			+ "FROM BRRS_M_SFINP1_SUMMARYTABLE b, BRRS_M_SFINP2_SUMMARYTABLE c "
			+ "WHERE b.REPORT_DATE = ?1 AND c.REPORT_DATE = ?1 "
			+ "AND b.R61_MONTH_END = c.R80_MONTH_END", nativeQuery = true)
	List<Object[]> getCheckSrlNo2(String report_date);

	/// M_SFINP1
	//// Srl_no3 (Total ASSETS and LIABILITIES(MONTH END IN M_SFINP1,M_SFINP2)
	@Query(value = "SELECT b.R61_AVERAGE, c.R80_AVERAGE "
			+ "FROM BRRS_M_SFINP1_SUMMARYTABLE b, BRRS_M_SFINP2_SUMMARYTABLE c "
			+ "WHERE b.REPORT_DATE = ?1 AND c.REPORT_DATE = ?1 "
			+ "AND b.R61_AVERAGE = c.R80_AVERAGE", nativeQuery = true)
	List<Object[]> getCheckSrlNo3(String report_date);

	// Srl_no4
	// M_SFINP2 summarytable
	@Query(value = "Select count(*) from BRRS_M_SFINP2_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo4(String report_date);

	/// M_SFINP2
	//// Srl_no5 (Total ASSETS and LIABILITIES(MONTH END IN M_SFINP1,M_SFINP2)
	@Query(value = "SELECT b.R61_MONTH_END, c.R80_MONTH_END "
			+ "FROM BRRS_M_SFINP1_SUMMARYTABLE b, BRRS_M_SFINP2_SUMMARYTABLE c "
			+ "WHERE b.REPORT_DATE = ?1 AND c.REPORT_DATE = ?1 "
			+ "AND b.R61_MONTH_END = c.R80_MONTH_END", nativeQuery = true)
	List<Object[]> getCheckSrlNo5(String report_date);

	/// M_SFINP2
	//// Srl_no6 (Total ASSETS and LIABILITIES(MONTH END IN M_SFINP1,M_SFINP2)
	@Query(value = "SELECT b.R61_AVERAGE, c.R80_AVERAGE "
			+ "FROM BRRS_M_SFINP1_SUMMARYTABLE b, BRRS_M_SFINP2_SUMMARYTABLE c "
			+ "WHERE b.REPORT_DATE = ?1 AND c.REPORT_DATE = ?1 "
			+ "AND b.R61_AVERAGE = c.R80_AVERAGE", nativeQuery = true)
	List<Object[]> getCheckSrlNo6(String report_date);

	// Srl_no7
	// M_LIQ summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_LIQ_SUMMARYTABLE WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_LIQ_MANUAL_SUMMARYTABLE WHERE REPORT_DATE=?1) "
			+ "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo7(String report_date);

	// Srl_no8
	// M_SCI_E summarytable
	@Query(value = "Select count(*) from BRRS_M_SCI_E_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo8(String report_date);

	// Srl_no9
	// M_IS summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_IS_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_IS_SUMMARYTABLE2 WHERE REPORT_DATE=?1) " + "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo9(String report_date);

	// Srl_no10
	// M_CA1 summarytable
	@Query(value = "Select count(*) from BRRS_M_CA1_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo10(String report_date);

	// Srl_no11
	// M_CA2 summarytable
	@Query(value = "Select count(*) from BRRS_M_CA2_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo11(String report_date);

	// Srl_no12
	// M_CA3 summarytable
	@Query(value = "Select count(*) from BRRS_M_CA3_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo12(String report_date);

	// Srl_no13
	// M_CA4 summarytable
	@Query(value = "Select count(*) from BRRS_M_CA4_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo13(String report_date);

	// Srl_no14
	// M_CA5 summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_CA5_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_CA5_SUMMARYTABLE2 WHERE REPORT_DATE=?1) " + "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo14(String report_date);

	// Srl_no15
	// M_CA6 summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_CA6_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_CA6_SUMMARYTABLE2 WHERE REPORT_DATE=?1) " + "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo15(String report_date);

	// Srl_no16
	// M_CA7 summarytable
	@Query(value = "Select count(*) from BRRS_M_CA7_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo16(String report_date);

	// Srl_no17
	// M_SRWA_12A summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_SRWA_12A_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12A_SUMMARYTABLE2 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12A_SUMMARYTABLE3 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12A_SUMMARYTABLE4 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12A_SUMMARYTABLE5 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12A_SUMMARYTABLE6 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12A_SUMMARYTABLE7 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12A_SUMMARYTABLE8 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12A_SUMMARYTABLE_M WHERE REPORT_DATE=?1) "
			+ "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo17(String report_date);

	// Srl_no18
	// M_SRWA_12B summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_SRWA_12B_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12B_SUMMARYTABLE2 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12B_SUMMARYTABLE3 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12B_SUMMARYTABLE4 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12B_SUMMARYTABLE5 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12B_SUMMARYTABLE6 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12B_SUMMARYTABLE7 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SRWA_12B_SUMMARYTABLE8 WHERE REPORT_DATE=?1) "
			+ "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo18(String report_date);

	// Srl_no19
	// M_SRWA_12C summarytable
	@Query(value = "Select count(*) from BRRS_M_SRWA_12C_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo19(String report_date);

	// Srl_no20
	// M_SRWA_12D summarytable
	@Query(value = "Select count(*) from BRRS_M_SRWA_12D_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo20(String report_date);

	// Srl_no21
	// M_SRWA_12E summarytable
	@Query(value = "Select count(*) from BRRS_M_SRWA_12E_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo21(String report_date);

	// Srl_no22
	// M_SRWA_12F summarytable
	@Query(value = "Select count(*) from BRRS_M_SRWA_12F_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo22(String report_date);

	// Srl_no23
	// M_SRWA_12G summarytable
	@Query(value = "Select count(*) from BRRS_M_SRWA_12G_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo23(String report_date);

	// Srl_no24
	// M_SRWA_12A summarytable
	@Query(value = "Select count(*) from BRRS_M_SRWA_12H_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo24(String report_date);

	// Srl_no25
	// M_OR1 summarytable
	@Query(value = "Select count(*) from BRRS_M_OR1_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo25(String report_date);

	// Srl_no26
	// M_OR2 summarytable
	@Query(value = "Select count(*) from BRRS_M_OR2_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo26(String report_date);

	// Srl_no27
	// M_MRC summarytable
	@Query(value = "Select count(*) from BRRS_M_MRC_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo27(String report_date);

	// Srl_no28
	// M_SIR summarytable
	@Query(value = "Select count(*) from BRRS_M_SIR_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo28(String report_date);

	// Srl_no29
	// M_GMIRT summarytable
	@Query(value = "Select count(*) from BRRS_M_GMIRT_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo29(String report_date);

	// Srl_no30
	// M_IRB summarytable
	@Query(value = "Select count(*) from BRRS_M_IRB_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo30(String report_date);

	// Srl_no31
	// M_EPR summarytable
	@Query(value = "Select count(*) from BRRS_M_EPR_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo31(String report_date);

	// Srl_no32
	// M_FXR summarytable
	@Query(value = "Select count(*) from BRRS_M_FXR_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo32(String report_date);

	// Srl_no33
	// M_CR summarytable
	@Query(value = "Select count(*) from BRRS_M_CR_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo33(String report_date);

	// Srl_no34
	// M_OPTR summarytable
	@Query(value = "Select count(*) from BRRS_M_OPTR_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo34(String report_date);

	// Srl_no35
	// M_GALOR summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_GALOR_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_GALOR_SUMMARYTABLE2 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_GALOR_MANUAL_SUMMARYTABLE WHERE REPORT_DATE=?1) "
			+ "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo35(String report_date);

	// Srl_no36
	// M_CALOC summarytable
	@Query(value =
		    "SELECT " +
		    "(SELECT COUNT(*) FROM BRRS_M_CALOC_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + " +
		    "(SELECT COUNT(*) FROM BRRS_M_CALOC_SUMMARYTABLE2 WHERE REPORT_DATE=?1) + " +
		    "(SELECT COUNT(*) FROM BRRS_M_CALOC_SUMMARYTABLE3 WHERE REPORT_DATE=?1) "+
		    "FROM dual",
		    nativeQuery = true)
		Integer getCheckSrlNo36(String report_date);

	// Srl_no37
	// M_LA1 summarytable
	@Query(value = "Select count(*) from BRRS_M_LA1_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo37(String report_date);

	// Srl_no38
	// M_LA2 summarytable
	@Query(value = "Select count(*) from BRRS_M_LA2_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo38(String report_date);

	// Srl_no39
	// M_LA3 summarytable
	@Query(value = "SELECT" + "SELECT COUNT (*) FROM BRRS_M_LA3_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "SELECT COUNT (*) FROM BRRS_M_LA3_SUMMARYTABLE2 WHERE REPORT_DATE=?1) " + "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo39(String report_date);

	// Srl_no40
	// M_LA4 summarytable
	@Query(value = "SELECT" + "SELECT COUNT (*) FROM BRRS_M_LA4_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "SELECT COUNT (*) FROM BRRS_M_LA4_SUMMARYTABLE2 WHERE REPORT_DATE=?1) " + "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo40(String report_date);

	// Srl_no41
	// M_LA5 summarytable
	@Query(value = "Select count(*) from BRRS_M_LA5_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo41(String report_date);

	// Srl_no42
	// M_PLL summarytable
	@Query(value = "Select count(*) from BRRS_M_PLL_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo42(String report_date);

	// Srl_no43
	// M_PD summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_PD_SUMMARYTABLE WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_PD_MANUAL_SUMMARYTABLE WHERE REPORT_DATE=?1) "
			+ "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo43(String report_date);

	// Srl_no44
	// M_I_S_CA summarytable
	@Query(value = "Select count(*) from BRRS_M_I_S_CA_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo44(String report_date);

	// Srl_no45
	// M_SP summarytable
	@Query(value = "Select count(*) from BRRS_M_SP_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo45(String report_date);

	// Srl_no46
	// M_GP summarytable
	@Query(value = "Select count(*) from BRRS_M_GP_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo46(String report_date);

	// Srl_no47
	// M_TBS summarytable
	@Query(value = "Select count(*) from BRRS_M_TBS_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo47(String report_date);

	// Srl_no48
	// M_LIQGAP summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_LIQGAP_SUMMARYTABLE WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_LIQGAP_MANUAL_SUMMARYTABLE WHERE REPORT_DATE=?1) "
			+ "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo48(String report_date);

	// Srl_no49
	// M_NOSVOS summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_NOSVOS_P1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_NOSVOS_P2 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_NOSVOS_P3 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_NOSVOS_P4 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_NOSVOS_P5 WHERE REPORT_DATE=?1) " + "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo49(String report_date);

	// Srl_no50
	// AIDP summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_AIDP_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_AIDP_SUMMARYTABLE2 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_AIDP_SUMMARYTABLE3 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_AIDP_SUMMARYTABLE4 WHERE REPORT_DATE=?1) "
			+ "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo50(String report_date);

	// Srl_no51
	// DEP1 summarytable
	@Query(value = "Select count(*) from BRRS_M_DEP1_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo51(String report_date);

	// Srl_no52
	// DEP2 summarytable
	@Query(value = "Select count(*) from BRRS_M_DEP2_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo52(String report_date);

	// Srl_no53
	// DEP3 summarytable
	@Query(value = "Select count(*) from BRRS_M_DEP3_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo53(String report_date);

	// Srl_no54
	// DEP4 summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_DEP4_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_DEP4_SUMMARYTABLE2 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_DEP4_SUMMARYTABLE3 WHERE REPORT_DATE=?1) "
			+ "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo54(String report_date);

	// Srl_no55
	// M_OB summarytable
	@Query(value = "Select count(*) from BRRS_M_OB_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo55(String report_date);

	// Srl_no56
	// BOP summarytable
	@Query(value = "Select count(*) from BRRS_M_BOP_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo56(String report_date);

	// Srl_no57
	// INT_RATES summarytable
	@Query(value = "Select count(*) from BRRS_M_INT_RATES_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo57(String report_date);

	// Srl_no58
	// INT_FATES_FCA summarytable
	@Query(value = "Select count(*) from BRRS_M_INT_RATES_FCA_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo58(String report_date);

	// Srl_no59
	// M_SECA summarytable
	@Query(value = "Select count(*) from BRRS_M_SECA_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo59(String report_date);

	// Srl_no60
	// SECL summarytable
	@Query(value = "Select count(*) from BRRS_M_SECL_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo60(String report_date);

	// Srl_no61
	// RPD summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_RPD_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_RPD_SUMMARYTABLE2 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_RPD_SUMMARYTABLE3 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_RPD_SUMMARYTABLE4 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_RPD_SUMMARYTABLE5 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_RPD_SUMMARYTABLE6 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_RPD_SUMMARYTABLE7 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_RPD_SUMMARYTABLE8 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_RPD_SUMMARYTABLE9 WHERE REPORT_DATE=?1) " + "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo61(String report_date);

	// Srl_no62
	// FAS summarytable
	@Query(value = "Select count(*) from BRRS_M_FAS_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo62(String report_date);

	// Srl_no63
	// SEC summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_SEC_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SEC_SUMMARYTABLE2 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SEC_SUMMARYTABLE3 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_SEC_SUMMARYTABLE4 WHERE REPORT_DATE=?1) " + "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo63(String report_date);

	// Srl_no64
	// UNCONS_INVEST summarytable
	@Query(value = "Select count(*) from BRRS_M_UNCONS_INVEST_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo64(String report_date);

	// Srl_no65
	// Q_ATF summarytable
	@Query(value = "Select count(*) from BRRS_Q_ATF_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo65(String report_date);

	// Srl_no66
	// Q_RLFA1 summarytable
	@Query(value = "Select count(*) from BRRS_Q_RLFA1_SUMMARY_TABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo66(String report_date);

	// Srl_no67
	// Q_RLFA2 summarytable
	@Query(value = "Select count(*) from BRRS_Q_RLFA2_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo67(String report_date);

	// Srl_no68
	// Q_SMME_LA summarytable
	@Query(value = "Select count(*) from BRRS_Q_SMME_LOANS_ADVANCES_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo68(String report_date);

	// Srl_no69
	// Q_SMME_II summarytable
	@Query(value = "Select count(*) from BRRS_Q_SMME_INTREST_INCOME_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo69(String report_date);

	// Srl_no70
	// Q_SMME_DEP summarytable
	@Query(value = "Select count(*) from BRRS_Q_SMME_DEP_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo70(String report_date);

	// Srl_no71
	// Q_STAFF summarytable
	@Query(value = "Select count(*) from BRRS_Q_STAFF_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo71(String report_date);

	// Srl_no72
	// Q_LARADV summarytable
	@Query(value = "SELECT " + "(SELECT COUNT(*) FROM BRRS_M_LARADV_SUMMARYTABLE1 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_LARADV_SUMMARYTABLE2 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_LARADV_SUMMARYTABLE3 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_LARADV_SUMMARYTABLE4 WHERE REPORT_DATE=?1) + "
			+ "(SELECT COUNT(*) FROM BRRS_M_LARADV_SUMMARYTABLE5 WHERE REPORT_DATE=?1) "
			+ "FROM dual", nativeQuery = true)
	Integer getCheckSrlNo72(String report_date);

	// Srl_no73
	// Q_BRANCHNET summarytable
	@Query(value = "Select count(*) from BRRS_Q_BRANCHNET_SUMMARYTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	Integer getCheckSrlNo73(String report_date);
}
