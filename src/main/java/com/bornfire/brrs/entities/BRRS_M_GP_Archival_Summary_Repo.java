package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_GP_Archival_Summary_Repo
    extends JpaRepository<M_GP_Archival_Summary_Entity, M_GP_Archival_Summary_PK> {

  // Fetch specific archival data by report date & version
  @Query(value = "SELECT * FROM BRRS_M_GP_ARCHIVALTABLE_SUMMARY "
      + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2", nativeQuery = true)
  List<M_GP_Archival_Summary_Entity> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);
  
  
  @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_GP_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
  List<Object> getM_GP_archival();

  // Fetch latest archival version for given date (no version input)
  @Query(value = "SELECT * FROM BRRS_M_GP_ARCHIVALTABLE_SUMMARY "
      + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
      + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
  Optional<M_GP_Archival_Summary_Entity> getLatestArchivalVersionByDate(Date reportDate);

  // Fetch by primary key (used internally by Spring Data JPA)
  Optional<M_GP_Summary_Entity> findByReportDateAndReportVersion(Date reportDate, BigDecimal reportVersion);

  // Current Report Version Only Shown
  @Query(value = "SELECT * FROM BRRS_M_GP_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
  List<M_GP_Archival_Summary_Entity> getdatabydateListWithVersion();

  @Query(value = "SELECT * FROM BRRS_M_GP_ARCHIVALTABLE_SUMMARY " +
      "WHERE REPORT_VERSION IS NOT NULL " +
      "ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
  List<M_GP_Archival_Summary_Entity> getdatabydateListWithVersionAll();

}
