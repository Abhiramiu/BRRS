package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_INT_RATES_Archival_Summary_Repo 
        extends JpaRepository<M_INT_RATES_Archival_Summary_Entity, M_INT_RATES_Archival_Summary_PK> {

    // Fetch specific archival data by report date & version
    @Query(value = "SELECT * FROM BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2",
           nativeQuery = true)
    List<M_INT_RATES_Archival_Summary_Entity> getdatabydateListarchival(
            Date reportDate, String reportVersion);

    // Fetch latest archival version for given date
    @Query(value = "SELECT * FROM BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " +
                   "ORDER BY TO_NUMBER(REPORT_VERSION) DESC " +
                   "FETCH FIRST 1 ROWS ONLY",
           nativeQuery = true)
    Optional<M_INT_RATES_Archival_Summary_Entity> getLatestArchivalVersionByDate(
            Date reportDate);

    // Fetch by primary key (corrected return type!)
    Optional<M_INT_RATES_Archival_Summary_Entity> findByReportDateAndReportVersion(
            Date reportDate, String reportVersion);

    // Get ONLY the latest version (use DESC but fetching only 1)
    @Query(value = "SELECT * FROM BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY TO_NUMBER(REPORT_VERSION) DESC " +
                   "FETCH FIRST 1 ROWS ONLY",
           nativeQuery = true)
    List<M_INT_RATES_Archival_Summary_Entity> getdatabydateListWithVersion();

    // Get ALL versions → sorted ascending (1,2,3…)
    @Query(value = "SELECT * FROM BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY TO_NUMBER(REPORT_VERSION) ASC",
           nativeQuery = true)
    List<M_INT_RATES_Archival_Summary_Entity> getdatabydateListWithVersionAll();
}
