package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Expanded_Regu_BS_Archival_Summary_Repo
        extends JpaRepository<Expanded_Regu_BS_Archival_Summary_Entity, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_EXPANDED_REGU_BS_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getExpanded_Regu_BS_archival();

    @Query(value = "select * from BRRS_EXPANDED_REGU_BS_ARCHIVALTABLE_SUMMARY where REPORT_DATE = to_date(?1,'dd-MM-yyyy') and REPORT_VERSION = ?2", nativeQuery = true)
    List<Expanded_Regu_BS_Archival_Summary_Entity> getdatabydateListarchival(String report_date, Object version);

}
