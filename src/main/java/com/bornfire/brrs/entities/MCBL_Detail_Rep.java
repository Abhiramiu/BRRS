package com.bornfire.brrs.entities;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface MCBL_Detail_Rep extends JpaRepository<MCBL_Detail_Entity, String> {
	
	/*@Query(value = "SELECT * FROM BTDES_INR_REPORTING_BRANCH WHERE report_date=? ORDER BY id", nativeQuery = true)
	List<MCBL_Detail_Entity> Getcurrentdaydetail(Date Report_date);

	@Query(value = "SELECT MAX(id) FROM BTDES_INR_REPORTING_BRANCH", nativeQuery = true)
	String getMaxId();
	*/


	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
            "FROM BRRS_MCBL_DETAIL " +
            "WHERE TRUNC(REPORT_DATE) = TRUNC(TO_DATE(:report_date, 'YYYY-MM-DD'))",
    nativeQuery = true)
int checkIfReportDateExists(@Param("report_date") String report_date);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM MCBL_Detail_Entity d " +
	       "WHERE d.gl_code = :glCode " +
	       "AND d.gl_sub_code = :glSubCode " +
	       "AND d.head_acc_no = :headAccNo " +
	       "AND d.currency = :currency " +
	       "AND d.report_date = :reportDate")
	void deleteByKeysAndReportDate(String glCode,
	                               String glSubCode,
	                               String headAccNo,
	                               String currency,
	                               Date reportDate);

	@Modifying
	@Transactional
	@Query("DELETE FROM MCBL_Detail_Entity d " +
	       "WHERE d.report_date = :reportDate " +
	       "AND (d.gl_code, d.gl_sub_code, d.head_acc_no, d.currency) IN :keys")
	void deleteByKeysAndReportDateBatch(@Param("keys") List<Object[]> keys,
	                                    @Param("reportDate") Date reportDate);

}