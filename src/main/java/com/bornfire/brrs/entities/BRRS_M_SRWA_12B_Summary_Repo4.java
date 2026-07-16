package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_M_SRWA_12B_Summary_Repo4
        extends JpaRepository<M_SRWA_12B_Summary_Entity4, Date> {

    // Fetch all rows for a specific report date
    @Query(value = "SELECT * FROM BRRS_M_SRWA_12B_SUMMARYTABLE4 WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<M_SRWA_12B_Summary_Entity4> getdatabydateList(Date rpt_date);

    @Query(value = "SELECT * FROM BRRS_M_SRWA_12B_SUMMARYTABLE4 WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    List<M_SRWA_12B_Summary_Entity4> getdatabydateListWithVersion(String todate);

    // Find the latest version for a report date
    Optional<M_SRWA_12B_Summary_Entity4> findTopByReportDateOrderByReportVersionDesc(Date reportDate);

    // Check if a version exists for a report date
    Optional<M_SRWA_12B_Summary_Entity4> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

    @Query(value = "SELECT * FROM BRRS_M_SRWA_12B_SUMMARYTABLE4 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    List<M_SRWA_12B_Summary_Entity4> getdatabydateListWithVersion();
}
