
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Q_STAFF_New_Archival_Summary_Repo3
                extends JpaRepository<Q_STAFF_New_Archival_Summary_Entity3, Q_STAFF_New_Archival_Summary3_PK> {

        @Query(value = "SELECT * FROM BRRS_Q_STAFF_New_ARCHIVALTABLE_SUMMARY3 "
                        + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2 ", nativeQuery = true)
        List<Q_STAFF_New_Archival_Summary_Entity3> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);

        // Fetch latest archival version for given date (no version input)
        @Query(value = "SELECT * FROM BRRS_Q_STAFF_New_ARCHIVALTABLE_SUMMARY3 "
                        + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL "
                        + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
                        + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
        Optional<Q_STAFF_New_Archival_Summary_Entity3> getLatestArchivalVersionByDate(Date reportDate);

        // Fetch by primary key (used internally by Spring Data JPA)
        Optional<Q_STAFF_New_Summary_Entity3> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

        // @Query(value = "SELECT * FROM BRRS_Q_STAFF_New_ARCHIVALTABLE_SUMMARY3 " +
        // "WHERE REPORT_VERSION IS NOT NULL " +
        // "ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
        // List<Q_STAFF_New_Archival_Summary_Entity3> getdatabydateListWithVersionAll();

        @Query(value = "SELECT * FROM BRRS_Q_STAFF_New_ARCHIVALTABLE_SUMMARY3 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
        List<Q_STAFF_New_Archival_Summary_Entity3> getdatabydateListWithVersion();

        @Query(value = "SELECT *  FROM BRRS_Q_STAFF_New_ARCHIVALTABLE_SUMMARY3 WHERE REPORT_VERSION IS NOT NULL ", nativeQuery = true)
        List<Q_STAFF_New_Archival_Summary_Entity3> getdatabydateListWithVersionAll();
}
