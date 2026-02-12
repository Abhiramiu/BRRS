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
public interface BRRS_M_LARADV_Resub_Summary_Repo1 extends JpaRepository<M_LARADV_Resub_Summary_Entity1, M_LARADV_PK> {
	// Fetch specific archival data by report date & version
	@Query(value = "SELECT * FROM BRRS_M_LARADV_RESUBTABLE_SUMMARY1 WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2", nativeQuery = true)
	List<M_LARADV_Resub_Summary_Entity1> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion);

	// Fetch latest archival version for given date (no version input)
	@Query(value = "SELECT * FROM BRRS_M_LARADV_RESUBTABLE_SUMMARY1 "
			+ "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
			+ "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	Optional<M_LARADV_Resub_Summary_Entity1> getLatestArchivalVersionByDate(Date reportDate);

	// Fetch by primary key (used internally by Spring Data JPA)
	@Query("SELECT e FROM M_LARADV_Resub_Summary_Entity1 e " + "WHERE e.report_date = :reportDate "
			+ "AND e.report_version = :reportVersion")
	Optional<M_LARADV_Resub_Summary_Entity1> checkVersion(@Param("reportDate") Date reportDate,
			@Param("reportVersion") BigDecimal reportVersion);

	@Query(value = "SELECT * FROM BRRS_M_LARADV_RESUBTABLE_SUMMARY1 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
	List<M_LARADV_Resub_Summary_Entity1> getdatabydateListWithVersion();
}
