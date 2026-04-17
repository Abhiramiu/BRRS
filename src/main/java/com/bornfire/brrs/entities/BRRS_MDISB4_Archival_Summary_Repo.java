package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_MDISB4_Archival_Summary_Repo extends JpaRepository<MDISB4_Archival_Summary_Entity, MDISB4_PK> {

	/*
	 * @Query(value =
	 * "select REPORT_DATE, REPORT_VERSION from BRRS_MDISB4_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION"
	 * , nativeQuery = true) List<Object> getMDISB4archival();
	 */
	/*
	 * @Query(value =
	 * "select * from BRRS_MDISB4_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2"
	 * , nativeQuery = true) List<MDISB4_Archival_Summary_Entity>
	 * getdatabydateListarchival(Date report_date, BigDecimal report_version);
	 */
    // -------------------------------------------------------
	@Query(value = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE " +
            "FROM BRRS_MDISB4_ARCHIVALTABLE_SUMMARY " +
            "WHERE REPORT_VERSION IS NOT NULL " +
            "ORDER BY REPORT_VERSION ASC",
    nativeQuery = true)
List<Object[]> getMDISB4archival();

    // -------------------------------------------------------
    // 2️⃣  Get Full Record By Date & Version
    // -------------------------------------------------------
    @Query(value = "SELECT * FROM BRRS_MDISB4_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_DATE = ?1 " +
                   "AND REPORT_VERSION = ?2",
           nativeQuery = true)
    List<MDISB4_Archival_Summary_Entity> 
        getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);


    // -------------------------------------------------------
    // 3️⃣  Get All Versions (Sorted)
    // -------------------------------------------------------
    @Query(value = "SELECT * FROM BRRS_MDISB4_ARCHIVALTABLE_SUMMARY " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY REPORT_VERSION ASC",
           nativeQuery = true)
    List<MDISB4_Archival_Summary_Entity> 
        getdatabydateListWithVersion();

}