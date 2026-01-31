
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Q_BRANCHNET_Archival_Summary_Repo2
        extends JpaRepository<Q_BRANCHNET_Archival_Summary_Entity2, Q_BRANCHNET_Archival_Summary2_PK> {

    // Fetch specific archival data by report date & version
    @Query(value = "SELECT * FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY2 WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2", nativeQuery = true)
    List<Q_BRANCHNET_Archival_Summary_Entity2> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);

    // Fetch latest archival version for given date (no version input)
    @Query(value = "SELECT * FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY2 "
            + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
            + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<Q_BRANCHNET_Archival_Summary_Entity2> getLatestArchivalVersionByDate(Date reportDate);

    // Fetch by primary key (used internally by Spring Data JPA)
    Optional<Q_BRANCHNET_Summary_Entity2> findByReportDateAndReportVersion(Date reportDate, BigDecimal reportVersion);

    @Query(value = "SELECT * FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY2 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<Q_BRANCHNET_Archival_Summary_Entity2> getdatabydateListWithVersion();

}
