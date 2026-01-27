
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Q_STAFF_New_Archival_Summary_Repo1
                extends JpaRepository<Q_STAFF_New_Archival_Summary_Entity1, Q_STAFF_New_Archival_Summary1_PK> {

        @Query(value = "SELECT * FROM BRRS_Q_STAFF_NEW_ARCHIVALTABLE_SUMMARY1 "
                        + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2 ", nativeQuery = true)
        List<Q_STAFF_New_Archival_Summary_Entity1> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);

        // Fetch latest archival version for given date (no version input)
        @Query(value = "SELECT * FROM BRRS_Q_STAFF_NEW_ARCHIVALTABLE_SUMMARY1 "
                        + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL "
                        + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
                        + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
        Optional<Q_STAFF_New_Archival_Summary_Entity1> getLatestArchivalVersionByDate(Date reportDate);

        // Fetch by primary key (used internally by Spring Data JPA)
        Optional<Q_STAFF_New_Summary_Entity1> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

        @Query(value = "SELECT * FROM BRRS_Q_STAFF_NEW_ARCHIVALTABLE_SUMMARY1 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
        List<Q_STAFF_New_Archival_Summary_Entity1> getdatabydateListWithVersion();

        
    @Query(value = "SELECT *  FROM BRRS_Q_STAFF_NEW_ARCHIVALTABLE_SUMMARY1 WHERE REPORT_VERSION IS NOT NULL ", nativeQuery = true)
    List<Q_STAFF_New_Archival_Summary_Entity1> getdatabydateListWithVersionAll();

}
