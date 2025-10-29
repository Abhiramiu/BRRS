package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_M_SRWA_12C_Summary_Repo extends JpaRepository<M_SRWA_12C_Summary_Entity, Date> {

    // ✅ Fetch record(s) by specific REPORT_DATE
    @Query(value = "SELECT * FROM BRRS_M_SRWA_12C_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(:rpt_code)", nativeQuery = true)
    List<M_SRWA_12C_Summary_Entity> getdatabydateList(@Param("rpt_code") Date rpt_code);
}






	


