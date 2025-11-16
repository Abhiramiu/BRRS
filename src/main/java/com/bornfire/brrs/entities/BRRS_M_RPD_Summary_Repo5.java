package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_RPD_Summary_Repo5  extends JpaRepository<M_RPD_Summary_Entity5, Date> {

	

    // Fetch all rows for a specific report date
    @Query(value = "SELECT * FROM BRRS_M_RPD_SUMMARYTABLE5 WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<M_RPD_Summary_Entity5> getdatabydateList(Date reportDate);

    @Query(value = "SELECT *  FROM BRRS_M_RPD_SUMMARYTABLE5 WHERE REPORT_DATE = ?1   AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<M_RPD_Summary_Entity5> getdatabydateListWithVersion(String todate);

    // Find the latest version for a report date
    Optional<M_RPD_Summary_Entity5> findTopByReportDateOrderByReportVersionDesc(Date reportDate);

    // Check if a version exists for a report date
    Optional<M_RPD_Summary_Entity5> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

    @Query(value = "SELECT *  FROM BRRS_M_RPD_SUMMARYTABLE5 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<M_RPD_Summary_Entity5> getdatabydateListWithVersion();
}