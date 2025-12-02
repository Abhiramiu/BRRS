package com.bornfire.brrs.entities;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface BDGF_Rep extends JpaRepository<BDGF_Entity, String> {
	
	@Query(value = "SELECT * FROM BRRS_BDGF WHERE report_date=? ORDER BY id", nativeQuery = true)
	List<BDGF_Entity> Getcurrentdaydetail(Date Report_date);


	@Query("SELECT DISTINCT m.report_date FROM BDGF_Entity m ORDER BY m.report_date DESC")
    List<Date> findDistinctReportDates();
	
	@Query(value = "SELECT * FROM BRRS_BDGF " +
            "WHERE REPORT_DATE = TO_DATE(:reportDate, 'YYYY-MM-DD')", 
    nativeQuery = true)
	 List<BDGF_Entity> findRecordsByReportDate(@Param("reportDate") String reportDate);

     @Query("SELECT b FROM BDGF_Entity b WHERE b.account_no = :accNo AND b.report_date = :reportDate")
     BDGF_Entity getdataByAcc(@Param("accNo") String accNo, @Param("reportDate") Date reportDate);
     
     @Query("SELECT b FROM BDGF_Entity b WHERE b.account_no = :accNo AND b.report_date = :repDate")
     BDGF_Entity getdataBybdgf(@Param("accNo") String accountNo,
                              @Param("repDate") Date reportDate);
}


