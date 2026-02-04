package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_RLFA1_Detail_Repo_New extends JpaRepository<Q_RLFA1_Detail_Entity_New, Date> {

	@Query(value = "select * from BRRS_Q_RLFA1_DETAIL_TABLE_NEW where REPORT_DATE =?1  ", nativeQuery = true)
    List<Q_RLFA1_Detail_Entity_New> getdatabydateList(Date reportdate);
    
    
    
    @Query(value = "SELECT *  FROM BRRS_Q_RLFA1_DETAIL_TABLE_NEW WHERE REPORT_DATE = ?1   AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<Q_RLFA1_Detail_Entity_New> getdatabydateListWithVersion(String todate);

    
    // ✅ Find the latest version for a report date
    @Query(value = "SELECT * FROM BRRS_Q_RLFA1_DETAIL_TABLE_NEW " +
                   "WHERE REPORT_DATE = ?1 " +
                   "ORDER BY TO_NUMBER(REPORT_VERSION) DESC " +
                   "FETCH FIRST 1 ROWS ONLY",
           nativeQuery = true)
    Optional<Q_RLFA1_Detail_Entity_New> findTopByReport_dateOrderByReport_versionDesc(Date report_date);

    // ✅ Check if a version exists for a report date
    @Query(value = "SELECT * FROM BRRS_Q_RLFA1_DETAIL_TABLE_NEW " +
                   "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2",
           nativeQuery = true)
    Optional<Q_RLFA1_Detail_Entity_New> findByReport_dateAndReport_version(Date report_date, String report_version);


            @Query(value = "SELECT *  FROM BRRS_Q_RLFA1_DETAIL_TABLE_NEW WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
        List<Q_RLFA1_Detail_Entity_New> getdatabydateListWithVersion();

}

