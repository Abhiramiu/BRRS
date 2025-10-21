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
public interface BFDB_Rep extends JpaRepository<BFDB_Entity, String> {
	
	@Query(value = "SELECT * FROM BRRS_BFDB WHERE report_date=? ORDER BY id", nativeQuery = true)
	List<BFDB_Entity> Getcurrentdaydetail(Date Report_date);

	@Query("SELECT DISTINCT m.report_date FROM BFDB_Entity m ORDER BY m.report_date DESC")
    List<Date> findDistinctReportDates();
	
	@Query(value = "SELECT * FROM BRRS_BFDB " +
            "WHERE REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD')", 
    nativeQuery = true)
	 List<BFDB_Entity> findRecordsByReportDate(@Param("reportDate") String reportDate);

	 @Query("SELECT b FROM BFDB_Entity b WHERE b.account_no = :accNo AND b.report_date = :reportDate")
	 BFDB_Entity getdataByAcc(@Param("accNo") String accNo, @Param("reportDate") Date reportDate);

}



