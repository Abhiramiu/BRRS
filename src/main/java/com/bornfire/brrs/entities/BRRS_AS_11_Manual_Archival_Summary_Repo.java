package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface BRRS_AS_11_Manual_Archival_Summary_Repo extends JpaRepository<AS_11_Manual_Archival_Summary_Entity, Date> {

  @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_AS_11_MANUAL_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getAS_11archival();

    @Query(value = "select * from BRRS_AS_11_MANUAL_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<AS_11_Manual_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, Object version);
}


