package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface BRRS_M_GALOR_Summary2_Repo extends JpaRepository<M_GALOR_Summary_Entity2 , Date> {

	
    
    
    
    @Query(value = "SELECT * FROM BRRS_M_GALOR_SUMMARYTABLE2 WHERE TRUNC(REPORT_DATE) = TRUNC(:reportdate)", nativeQuery = true)
    List<M_GALOR_Summary_Entity2> getdatabydateList(@Param("reportdate") Date reportdate);


}


