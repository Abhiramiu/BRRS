package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_OR2_Archival_Summary_Repo 
        extends JpaRepository<M_OR2_Archival_Summary_Entity, M_OR2_Archival_Summary_PK> {

    // Fetch records for given date + version
    @Query(value = "SELECT * FROM BRRS_M_OR2_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2",
           nativeQuery = true)
    List<M_OR2_Archival_Summary_Entity> getdatabydateListarchival(
            Date reportDate, String reportVersion);

    // Get latest version for a given date
    @Query(value = "SELECT * FROM BRRS_M_OR2_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " +
                   "ORDER BY TO_NUMBER(REPORT_VERSION) DESC " +
                   "FETCH FIRST 1 ROWS ONLY",
           nativeQuery = true)
    Optional<M_OR2_Archival_Summary_Entity> getLatestArchivalVersionByDate(Date reportDate);

    // Fetch by primary key
    Optional<M_OR2_Archival_Summary_Entity> findByReportDateAndReportVersion(
            Date reportDate, String reportVersion);

    // Get only the newest version (but sorted ASC if needed)
    @Query(value = "SELECT * FROM BRRS_M_OR2_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY TO_NUMBER(REPORT_VERSION) ASC " +
                   "FETCH FIRST 1 ROWS ONLY",
           nativeQuery = true)
    List<M_OR2_Archival_Summary_Entity> getdatabydateListWithVersion();

    // Get all versions in ASCENDING order → 1,2,3,4…
    @Query(value = "SELECT * FROM BRRS_M_OR2_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY TO_NUMBER(REPORT_VERSION) ASC",
           nativeQuery = true)
    List<M_OR2_Archival_Summary_Entity> getdatabydateListWithVersionAll();
}
