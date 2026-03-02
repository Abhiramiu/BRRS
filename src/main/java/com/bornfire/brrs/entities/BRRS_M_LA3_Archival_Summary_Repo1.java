package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_LA3_Archival_Summary_Repo1 
        extends JpaRepository<M_LA3_Archival_Summary_Entity1, M_LA3_PK> {

    // -------------------------------------------------------
    // 1️⃣  Archival View (Only Report Date & Version)
    // -------------------------------------------------------
    @Query(value = "SELECT REPORT_DATE, REPORT_VERSION " +
                   "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1 " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY REPORT_VERSION ASC",
           nativeQuery = true)
    List<Object[]> getM_LA3archival();


    // -------------------------------------------------------
    // 2️⃣  Get Full Record By Date & Version
    // -------------------------------------------------------
    @Query(value = "SELECT * FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1 " +
                   "WHERE REPORT_DATE = ?1 " +
                   "AND REPORT_VERSION = ?2",
           nativeQuery = true)
    List<M_LA3_Archival_Summary_Entity1> 
        getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);


    // -------------------------------------------------------
    // 3️⃣  Get All Versions (Sorted)
    // -------------------------------------------------------
    @Query(value = "SELECT * FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1 " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY REPORT_VERSION ASC",
           nativeQuery = true)
    List<M_LA3_Archival_Summary_Entity1> 
        getdatabydateListWithVersion();

}