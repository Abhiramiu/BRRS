package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_M_CA2_Summary_Repo extends JpaRepository<M_CA2_Summary_Entity, Date> {

	

	
	@Query(value = "SELECT * FROM BRRS_M_CA2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(:reportdate)", nativeQuery = true)
    List<M_CA2_Summary_Entity> getdatabydateList(@Param("reportdate") Date reportdate);
}
