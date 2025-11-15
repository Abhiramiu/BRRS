package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_BOP_Summary_Repo extends JpaRepository<M_BOP_Summary_Entity, Date>{
	
	  @Query(value = "SELECT * FROM BRRS_M_BOP_SUMMARYTABLE WHERE REPORT_DATE =?1", nativeQuery = true)
	    List<M_BOP_Summary_Entity> getdatabydateList( Date reportdate); // <-- FIXED PARAM BINDING
	    
	    @Query(value = "SELECT *  FROM BRRS_M_BOP_SUMMARYTABLE WHERE REPORT_DATE = ?1   AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	    List<M_BOP_Summary_Entity> getdatabydateListWithVersion(String todate);

	    
	    // ✅ Find the latest version for a report date
	    @Query(value = "SELECT * FROM BRRS_M_BOP_SUMMARYTABLE " +
	                   "WHERE REPORT_DATE = ?1 " +
	                   "ORDER BY TO_NUMBER(REPORT_VERSION) DESC " +
	                   "FETCH FIRST 1 ROWS ONLY",
	           nativeQuery = true)
	    Optional<M_BOP_Summary_Entity> findTopByReport_dateOrderByReport_versionDesc(Date report_date);

	    // ✅ Check if a version exists for a report date
	    @Query(value = "SELECT * FROM BRRS_M_BOP_SUMMARYTABLE " +
	                   "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2",
	           nativeQuery = true)
	    Optional<M_BOP_Summary_Entity> findByReport_dateAndReport_version(Date report_date, String report_version);


	            @Query(value = "SELECT *  FROM BRRS_M_BOP_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	        List<M_BOP_Summary_Entity> getdatabydateListWithVersion();

}
