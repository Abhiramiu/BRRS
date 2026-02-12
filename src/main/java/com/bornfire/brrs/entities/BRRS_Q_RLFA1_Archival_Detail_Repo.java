package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Q_RLFA1_Archival_Detail_Repo extends JpaRepository<Q_RLFA1_Archival_Detail_Entity, Q_RLFA1_Archival_Detail_PK> {

	/*
	 * @Query(value =
	 * "select REPORT_DATE, REPORT_VERSION from BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL_NEW order by REPORT_VERSION"
	 * , nativeQuery = true) List<Object> getQ_RLFA1archival();
	 */
    @Query(value = "select * from BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL_NEW where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<Q_RLFA1_Archival_Detail_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);
    
    
	//  Fetch latest archival version for given date (no version input)
    @Query(value = "SELECT * FROM BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL_NEW " +"WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC " + "FETCH FIRST 1 ROWS ONLY",nativeQuery = true)
    Optional<Q_RLFA1_Archival_Detail_Entity> getLatestArchivalVersionByDate(Date report_date);

    @Query(value = "SELECT * FROM BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL_NEW WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2",
            nativeQuery = true)
     Optional<Q_RLFA1_Archival_Detail_Entity> findByReport_dateAndReport_version(Date report_date, BigDecimal report_version);
    
    //Current Report Version Only Shown 
    @Query(value = "SELECT *  FROM BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL_NEW WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<Q_RLFA1_Archival_Detail_Entity> getdatabydateListWithVersion();

    @Query(value = "SELECT *  FROM BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL_NEW WHERE REPORT_VERSION IS NOT NULL ", nativeQuery = true)
    List<Q_RLFA1_Archival_Detail_Entity> getdatabydateListWithVersionAll();
}
