package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_SRWA_12B_Archival_Detail_Repo5
        extends JpaRepository<M_SRWA_12B_Archival_Detail_Entity5, M_SRWA_12B_Archival_Summary1_PK> {

    // Fetch specific archival data by report date & version
    @Query(value = "SELECT * FROM BRRS_M_SRWA_12B_ARCHIVALTABLE_DETAIL5 WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2", nativeQuery = true)
    List<M_SRWA_12B_Archival_Detail_Entity5> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);

    // Fetch latest archival version for given date (no version input)
    @Query(value = "SELECT * FROM BRRS_M_SRWA_12B_ARCHIVALTABLE_DETAIL5 "
            + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL "
            + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
            + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<M_SRWA_12B_Archival_Detail_Entity5> getLatestArchivalVersionByDate(Date reportDate);

    // Fetch by primary key (used internally by Spring Data JPA)
    Optional<M_SRWA_12B_Archival_Detail_Entity5> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

    @Query(value = "SELECT * FROM BRRS_M_SRWA_12B_ARCHIVALTABLE_DETAIL5 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<M_SRWA_12B_Archival_Detail_Entity5> getdatabydateListWithVersion();
}
