
package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_SEC_Archival_Summary_Repo2
        extends JpaRepository<BRRS_M_SEC_Archival_Summary_Entity2,M_SEC_Archival_Summary2_PK> {

    // Fetch specific archival data by report date & version
    @Query(value = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2", nativeQuery = true)
    List<BRRS_M_SEC_Archival_Summary_Entity2> getdatabydateListarchival(Date reportDate, String reportVersion);

    // Fetch latest archival version for given date (no version input)
    @Query(value = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 "
            + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
            + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<BRRS_M_SEC_Archival_Summary_Entity2> getLatestArchivalVersionByDate(Date reportDate);

    // Fetch by primary key (used internally by Spring Data JPA)
    Optional<BRRS_M_SEC_Archival_Summary_Entity2> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

    @Query(value = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<BRRS_M_SEC_Archival_Summary_Entity2> getdatabydateListWithVersion();

}
