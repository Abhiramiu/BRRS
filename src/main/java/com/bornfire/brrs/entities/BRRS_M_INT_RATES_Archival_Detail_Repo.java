package com.bornfire.brrs.entities;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_INT_RATES_Archival_Detail_Repo extends JpaRepository<M_INT_RATES_Archival_Detail_Entity,M_INT_RATES_PK> {

	  
	    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_INT_RATES_ARCHIVALTABLE_DETAIL order by REPORT_VERSION", nativeQuery = true)
	    List<Object> getM_INTRATESarchival();

	    @Query(value = "select * from BRRS_M_INT_RATES_ARCHIVALTABLE_DETAIL where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	    List<M_INT_RATES_Archival_Detail_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);

	    @Query("SELECT MAX(e.report_version) " +
	    	       "FROM M_INT_RATES_Archival_Detail_Entity e " +
	    	       "WHERE e.report_date = :date")
	    	BigDecimal findMaxVersion(@Param("date") Date date);
 
	    //Current Report Version Only Shown 
	    @Query(value = "SELECT *  FROM BRRS_M_INT_RATES_ARCHIVALTABLE_DETAIL WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	    List<M_INT_RATES_Archival_Detail_Entity> getdatabydateListWithVersion();
	    
}
