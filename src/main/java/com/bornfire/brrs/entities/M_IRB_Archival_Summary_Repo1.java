package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface M_IRB_Archival_Summary_Repo1 extends JpaRepository<M_IRB_Archival_Summary_Entity1, Date>{
    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_IRB_ARCHIVALTABLE_SUMMARY1 order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_PIarchival();

    @Query(value = "select * from BRRS_M_IRB_ARCHIVALTABLE_SUMMARY1 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<M_IRB_Archival_Summary_Entity1> getdatabydateListarchival(Date report_date, String report_version);
}