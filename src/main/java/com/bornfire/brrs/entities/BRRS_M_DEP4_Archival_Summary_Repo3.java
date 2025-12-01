package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_DEP4_Archival_Summary_Repo3  extends JpaRepository<M_DEP4_Archival_Summary_Entity3,M_DEP4_Archival_Summary3_PK> {
	 @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_DEP4_ARCHIVALTABLE_SUMMARY3 order by REPORT_VERSION", nativeQuery = true)
	    List<Object> getM_DEP4archival();
    // Fetch specific archival data by report date & version
    @Query(value = "SELECT * FROM BRRS_M_DEP4_ARCHIVALTABLE_SUMMARY3 WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2", nativeQuery = true)
    List<M_DEP4_Archival_Summary_Entity3> getdatabydateListarchival(Date reportDate, String reportVersion);

    // Fetch latest archival version for given date (no version input)
    @Query(value = "SELECT * FROM BRRS_M_DEP4_ARCHIVALTABLE_SUMMARY3 "
            + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
            + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<M_DEP4_Archival_Summary_Entity3> getLatestArchivalVersionByDate(Date reportDate);

    // Fetch by primary key (used internally by Spring Data JPA)
    Optional<M_DEP4_Summary_Entity3> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

    @Query(value = "SELECT * FROM BRRS_M_DEP4_ARCHIVALTABLE_SUMMARY3 " +
            "WHERE REPORT_VERSION IS NOT NULL " +
            "ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    List<M_DEP4_Archival_Summary_Entity3> getdatabydateListWithVersionAll();

    @Query(value = "SELECT * FROM BRRS_M_DEP4_ARCHIVALTABLE_SUMMARY3 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<M_DEP4_Archival_Summary_Entity3> getdatabydateListWithVersion();

  }
