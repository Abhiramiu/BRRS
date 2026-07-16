package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_GALOR_Archival_Summary2_Repo
        extends JpaRepository<M_GALOR_Archival_Summary_Entity2, M_GALOR_PK> {

    // -------------------------------------------------------
    // 1️⃣  Archival View (Only Date & Version)
    // -------------------------------------------------------
    @Query(value = "SELECT REPORT_DATE, REPORT_VERSION " +
                   "FROM BRRS_M_GALOR_ARCHIVALTABLE_SUMMARY2 " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY REPORT_VERSION ASC",
           nativeQuery = true)
    List<Object[]> getM_GALORarchival();


    // -------------------------------------------------------
    // 2️⃣  Get Full Data By Date & Version
    // -------------------------------------------------------
    @Query(value = "SELECT * FROM BRRS_M_GALOR_ARCHIVALTABLE_SUMMARY2 " +
                   "WHERE REPORT_DATE = ?1 " +
                   "AND REPORT_VERSION = ?2",
           nativeQuery = true)
    List<M_GALOR_Archival_Summary_Entity2> 
        getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);


    // -------------------------------------------------------
    // 3️⃣  Get All Versions (Sorted)
    // -------------------------------------------------------
    @Query(value = "SELECT * FROM BRRS_M_GALOR_ARCHIVALTABLE_SUMMARY2 " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY REPORT_VERSION ASC",
           nativeQuery = true)
    List<M_GALOR_Archival_Summary_Entity2> 
        getdatabydateListWithVersion();
}