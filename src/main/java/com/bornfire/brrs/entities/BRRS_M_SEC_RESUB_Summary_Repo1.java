package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface BRRS_M_SEC_RESUB_Summary_Repo1 extends JpaRepository<M_SEC_RESUB_Summary_Entity1, M_SEC_PK> {

    @Query(value =
	  "select * from BRRS_M_SEC_RESUB_SUMMARYTABLE1 where REPORT_DATE = ?1 and REPORT_VERSION = ?2"
	  , nativeQuery = true) List<M_SEC_RESUB_Summary_Entity1>
	  getdatabydateListarchival(Date report_date, BigDecimal report_version);
	  
	  @Query("SELECT MAX(e.report_version) FROM M_SEC_RESUB_Summary_Entity1 e WHERE e.report_date = :date")
		BigDecimal findMaxVersion(@Param("date") Date date);

	  
	  @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_SEC_RESUB_SUMMARYTABLE1 order by REPORT_VERSION", nativeQuery = true)
	    List<Object> getM_SECarchival();

  //Current Report Version Only Shown 
   @Query(value = "SELECT *  FROM BRRS_M_SEC_RESUB_SUMMARYTABLE1 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
   List<M_SEC_RESUB_Summary_Entity1> getdatabydateListWithVersion();

   
}
