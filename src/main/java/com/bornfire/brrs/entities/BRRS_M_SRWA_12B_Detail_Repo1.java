package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BRRS_M_SRWA_12B_Detail_Repo1
        extends JpaRepository<M_SRWA_12B_Detail_Entity1, Date> {

    // Fetch all rows for a specific report date
    @Query(value = "SELECT * FROM BRRS_M_SRWA_12B_DETAILTABLE1 WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<M_SRWA_12B_Detail_Entity1> getdatabydateList(Date rpt_date);

    @Query(value = "SELECT *  FROM BRRS_M_SRWA_12B_DETAILTABLE1 WHERE REPORT_DATE = ?1   AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<M_SRWA_12B_Detail_Entity1> getdatabydateListWithVersion(String todate);

    // Find the latest version for a report date
    Optional<M_SRWA_12B_Detail_Entity1> findTopByReportDateOrderByReportVersionDesc(Date reportDate);

//     @Query("SELECT q FROM M_SRWA_12B_Detail_Entity1 q WHERE q.reportDate = :reportDate AND q.reportVersion = :reportVersion")
//     Optional<M_SRWA_12B_Detail_Entity1> findByReportDateAndReportVersion(@Param("reportDate") Date reportDate,
//             @Param("reportVersion") String reportVersion);

    // Check if a version exists for a report date
    Optional<M_SRWA_12B_Detail_Entity1> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

    @Query(value = "SELECT *  FROM BRRS_M_SRWA_12B_DETAILTABLE1 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<M_SRWA_12B_Detail_Entity1> getdatabydateListWithVersion();
}
