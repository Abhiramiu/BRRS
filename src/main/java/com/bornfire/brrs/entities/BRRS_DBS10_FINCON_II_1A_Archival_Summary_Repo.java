package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_DBS10_FINCON_II_1A_Archival_Summary_Repo extends JpaRepository<DBS10_FINCON_II_1A_Archival_Summary_Entity, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getDBS10_FINCON_II_1Aarchival();

    @Query(value = "select * from BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<DBS10_FINCON_II_1A_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, String report_version);
}
