package com.bornfire.brrs.entities;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface BLBF_Rep extends JpaRepository<BLBF_Entity, String> {
	
	@Query(value = "SELECT * FROM BRRS_BLBF WHERE report_date=?1", nativeQuery = true)
	List<BLBF_Entity> Getcurrentdaydetail(Date Report_date);


	@Query(value = "SELECT * FROM BRRS_BLBF WHERE account_no=?1 AND TRUNC(REPORT_DATE)=TRUNC(?2)", nativeQuery = true)
	BLBF_Entity GetAll(String account_no,Date Report_date);
	
	@Query("SELECT DISTINCT m.report_date FROM BLBF_Entity m ORDER BY m.report_date DESC")
    List<Date> findDistinctReportDates();
	
	@Query(value = "SELECT * FROM BRRS_BLBF " +
            "WHERE REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD')", 
    nativeQuery = true)
	 List<BLBF_Entity> findRecordsByReportDate(@Param("reportDate") String reportDate);

}
