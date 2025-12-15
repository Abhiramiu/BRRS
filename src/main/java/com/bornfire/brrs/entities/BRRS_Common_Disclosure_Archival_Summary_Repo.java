package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Common_Disclosure_Archival_Summary_Repo extends JpaRepository<Common_Disclosure_Archival_Summary_Entity, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_COMMON_DISCLOSURE_ARCHIVAL_SUMMARYTABLE order by REPORT_VERSION", nativeQuery = true)
    List<Object> getCommon_Disclosurearchival();

    @Query(value = "select * from BRRS_COMMON_DISCLOSURE_ARCHIVAL_SUMMARYTABLE where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<Common_Disclosure_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, String report_version);
}
