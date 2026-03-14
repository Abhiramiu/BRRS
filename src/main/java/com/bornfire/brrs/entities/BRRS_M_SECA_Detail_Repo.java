package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bornfire.brrs.entities.M_SECA_Detail_Entity;

public interface BRRS_M_SECA_Detail_Repo extends JpaRepository<M_SECA_Detail_Entity, Date>
{
	@Query(value = "SELECT * FROM BRRS_M_SECA_DETAILTABLE WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<M_SECA_Detail_Entity> getdatabydateList(Date reportDate);
    
@Query(value = "SELECT *  FROM BRRS_M_SECA_DETAILTABLE WHERE REPORT_DATE = ?1   AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
List<M_SECA_Detail_Entity> getdatabydateListWithVersion(String todate);

    // Find the latest version for a report date
    Optional<M_SECA_Detail_Entity> findTopByReportDateOrderByReportVersionDesc(Date reportDate);

    // Check if a version exists for a report date
    Optional<M_SECA_Detail_Entity> findByReportDateAndReportVersion(Date reportDate, String reportVersion);

        @Query(value = "SELECT *  FROM BRRS_M_SECA_DETAILTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<M_SECA_Detail_Entity> getdatabydateListWithVersion();
}
