package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface BRRS_M_GALOR_Manual_Summary_Repo extends JpaRepository<M_GALOR_Manual_Summary_Entity , Date> {

	
	/*
	 * @Query(value = "select * from BRRS_M_GALOR_MANUAL_SUMMARYTABLE", nativeQuery
	 * = true) List<M_GALOR_Manual_Summary_Entity> getdatabydateList(Date
	 * reportdate);
	 */
    

	 @Query(value = "SELECT * FROM BRRS_M_GALOR_MANUAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(:reportdate)", nativeQuery = true)
	    List<M_GALOR_Manual_Summary_Entity> getdatabydateList(@Param("reportdate") Date reportdate);

}
