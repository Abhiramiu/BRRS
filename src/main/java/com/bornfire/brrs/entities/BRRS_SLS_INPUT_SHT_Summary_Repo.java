package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import oracle.sql.DATE;

public interface BRRS_SLS_INPUT_SHT_Summary_Repo extends JpaRepository<SLS_INPUT_SHT_Summary_Entity, String> {
	 
	@Query(value = "select * from BRRS_SLS_INPUT_SHT_SUMMARYTABLE order by REPORT_DATE", nativeQuery = true)
	 List<SLS_INPUT_SHT_Summary_Entity> rtslslist();
	 
	 @Query(value = "select * from BRRS_SLS_INPUT_SHT_SUMMARYTABLE where REPORT_DATE=?1 AND REPORT_CURRENCY=?2 ", nativeQuery = true)
	 List<SLS_INPUT_SHT_Summary_Entity> rtslslistbydate(Date reportdate,String currency);
	 
	 @Query(value = "select * from BRRS_SLS_INPUT_SHT_SUMMARYTABLE where REPORT_DATE=?1", nativeQuery = true)
	 List<SLS_INPUT_SHT_Summary_Entity> rtslslistonlydate(Date reportdate);
	 
	 @Query(value = "select * from BRRS_SLS_INPUT_SHT_SUMMARYTABLE ", nativeQuery = true)
		List<SLS_INPUT_SHT_Summary_Entity> getdatabydateList(Date reportdate);
}
