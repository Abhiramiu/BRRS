package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;






public interface BRRS_Q_SMME_DEP_Summary_Repo extends JpaRepository<Q_SMME_DEP_Summary_Entity, Date> {

	
	
	/*
	 * @Query(value = "select * from BRRS_Q_SMME_DEP_SUMMARYTABLE", nativeQuery =
	 * true) List<Q_SMME_DEP_Summary_Entity> getdatabydateList(Date rpt_code);
	 */
	 
	 
	  // ✅ Fetch record(s) by specific REPORT_DATE
	    @Query(value = "SELECT * FROM BRRS_Q_SMME_DEP_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(:rpt_code)", nativeQuery = true)
	    List<Q_SMME_DEP_Summary_Entity> getdatabydateList(@Param("rpt_code") Date rpt_code);
	
	

}
