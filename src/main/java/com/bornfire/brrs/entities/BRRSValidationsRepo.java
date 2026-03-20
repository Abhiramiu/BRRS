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
	
	
	

}
