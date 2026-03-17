package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_DEP4_Archival_Summary_Repo1   extends JpaRepository<M_DEP4_Archival_Summary_Entity1, M_DEP4_PK> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_DEP4_ARCHIVALTABLE_SUMMARY1 order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_DEP4archival();
    

    // -------------------------------------------------------
    // 2️⃣  Get Full Record By Date & Version
    // -------------------------------------------------------
    @Query(value = "SELECT * FROM BRRS_M_DEP4_ARCHIVALTABLE_SUMMARY1 " +
                   "WHERE REPORT_DATE = ?1 " +
                   "AND REPORT_VERSION = ?2",
           nativeQuery = true)
    List<M_DEP4_Archival_Summary_Entity1> 
        getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);


    // -------------------------------------------------------
    // 3️⃣  Get All Versions (Sorted)
    // -------------------------------------------------------
    @Query(value = "SELECT * FROM BRRS_M_DEP4_ARCHIVALTABLE_SUMMARY1 " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY REPORT_VERSION ASC",
           nativeQuery = true)
    List<M_DEP4_Archival_Summary_Entity1> 
        getdatabydateListWithVersion();

  }