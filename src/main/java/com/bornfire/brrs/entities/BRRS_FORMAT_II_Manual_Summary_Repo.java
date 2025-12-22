package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface BRRS_FORMAT_II_Manual_Summary_Repo extends JpaRepository<FORMAT_II_Manual_Summary_Entity , Date> {

	
    
    @Query(value = "SELECT * FROM BRRS_FORMAT_II_MANUAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(:reportdate)", nativeQuery = true)
    List<FORMAT_II_Manual_Summary_Entity> getdatabydateList(@Param("reportdate") Date reportdate);


}
