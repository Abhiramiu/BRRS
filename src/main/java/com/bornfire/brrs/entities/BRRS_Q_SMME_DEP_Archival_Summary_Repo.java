package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;




@Repository
public interface BRRS_Q_SMME_DEP_Archival_Summary_Repo extends JpaRepository<Q_SMME_DEP_Archival_Summary_Entity, Q_SMME_DEP_Archival_Summary_PK> {

	/*
	 * @Query(value =
	 * "select REPORT_DATE, REPORT_VERSION from BRRS_Q_SMME_DEP_ARCHIVAL_SUMMARYTABLE order by REPORT_VERSION"
	 * , nativeQuery = true) List<Object> getQ_SMME_DEParchival();
	 */

	    
	    
	    
	    @Query(value = "select * from BRRS_Q_SMME_DEP_ARCHIVAL_SUMMARYTABLE where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	    List<Q_SMME_DEP_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, String report_version);
	    
	    
	  
	    
	    
	 
	    
	  
	    
	    
		//  Fetch latest archival version for given date (no version input)
	    @Query(value = "SELECT * FROM BRRS_Q_SMME_DEP_ARCHIVAL_SUMMARYTABLE " +"WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC " + "FETCH FIRST 1 ROWS ONLY",nativeQuery = true)
	    Optional<Q_SMME_DEP_Archival_Summary_Entity> getLatestArchivalVersionByDate(Date report_date);

	    @Query(value = "SELECT * FROM BRRS_Q_SMME_DEP_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2",
	            nativeQuery = true)
	     Optional<Q_SMME_DEP_Archival_Summary_Entity> findByReport_dateAndReport_version(Date report_date, String report_version);
	    
	    //Current Report Version Only Shown 
	    @Query(value = "SELECT *  FROM BRRS_Q_SMME_DEP_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	    List<Q_SMME_DEP_Archival_Summary_Entity> getdatabydateListWithVersion();

	    @Query(value = "SELECT *  FROM BRRS_Q_SMME_DEP_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ", nativeQuery = true)
	    List<Q_SMME_DEP_Archival_Summary_Entity> getdatabydateListWithVersionAll();
	    
}
