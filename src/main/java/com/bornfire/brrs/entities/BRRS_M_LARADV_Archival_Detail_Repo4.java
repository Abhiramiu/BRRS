package com.bornfire.brrs.entities;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_LARADV_Archival_Detail_Repo4  extends JpaRepository<M_LARADV_Archival_Detail_Entity4,M_LARADV_PK>{
	// Fetch specific archival data by report date & version
    @Query(value = "SELECT * FROM BRRS_M_LARADV_ARCHIVALTABLE_DETAIL4 WHERE REPORT_DATE = ?1 AND report_version = ?2", nativeQuery = true)
    List<M_LARADV_Archival_Detail_Entity4> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);

    // Fetch latest archival version for given date (no version input)
    @Query(value = "SELECT * FROM BRRS_M_LARADV_ARCHIVALTABLE_DETAIL4 "
            + "WHERE REPORT_DATE = ?1 AND report_version IS NOT NULL " + "ORDER BY TO_NUMBER(report_version) DESC "
            + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<M_LARADV_Archival_Detail_Entity4> getLatestArchivalVersionByDate(Date reportDate);

    // Fetch by primary key (used internally by Spring Data JPA)
    @Query("SELECT e FROM M_LARADV_Archival_Detail_Entity4 e " + "WHERE e.report_date = :reportDate "
 			+ "AND e.report_version = :reportVersion")
 	Optional<M_LARADV_Archival_Detail_Entity4> checkVersion(@Param("reportDate") Date reportDate,
 			@Param("reportVersion") BigDecimal reportVersion);

    @Query(value = "SELECT * FROM BRRS_M_LARADV_ARCHIVALTABLE_DETAIL4 WHERE report_version IS NOT NULL ORDER BY report_version ASC", nativeQuery = true)
    List<M_LARADV_Archival_Detail_Entity4> getdatabydateListWithVersion();
}
