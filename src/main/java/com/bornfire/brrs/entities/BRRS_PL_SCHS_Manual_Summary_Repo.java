package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface BRRS_PL_SCHS_Manual_Summary_Repo extends JpaRepository<PL_SCHS_Manual_Summary_Entity, Date> {

    @Query(value = "SELECT * FROM BRRS_PL_SCHS_MANUAL_SUMMARYTABLE WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<PL_SCHS_Manual_Summary_Entity> getdatabydateList(Date rpt_date);

    

}