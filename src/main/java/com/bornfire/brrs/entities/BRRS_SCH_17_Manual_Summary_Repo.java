package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface BRRS_SCH_17_Manual_Summary_Repo extends JpaRepository<SCH_17_Manual_Summary_Entity , Date> {

	
    
    @Query(value = "SELECT * FROM BRRS_SCH_17_MANUAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(:reportdate)", nativeQuery = true)
    List<SCH_17_Manual_Summary_Entity> getdatabydateList(@Param("reportdate") Date reportdate);


}
