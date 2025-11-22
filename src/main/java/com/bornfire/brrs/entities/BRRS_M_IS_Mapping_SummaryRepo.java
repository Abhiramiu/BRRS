
package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_IS_Mapping_SummaryRepo
        extends JpaRepository<M_IS_Mapping_SummaryEntity, Date> {

    // Fetch all rows for a specific report date
    @Query(value = "SELECT * FROM BRRS_M_IS_MAPPING_SUMMARYTABLE WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<M_IS_Mapping_SummaryEntity> getdatabydateList(Date rpt_date);

    @Query(value = "SELECT *  FROM BRRS_M_IS_MAPPING_SUMMARYTABLE WHERE REPORT_DATE = ?1   AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<M_IS_Mapping_SummaryEntity> getdatabydateListWithVersion(String todate);

    // Find the latest version for a report date
    Optional<M_IS_Mapping_SummaryEntity> findTopByReportDateOrderByReportVersionDesc(Date reportDate);

    // Check if a version exists for a report date
    Optional<M_IS_Mapping_SummaryEntity> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

    @Query(value = "SELECT *  FROM BRRS_M_IS_MAPPING_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<M_IS_Mapping_SummaryEntity> getdatabydateListWithVersion();
}
