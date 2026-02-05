package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_SRWA_12C_Archival_Detail_Repo
		extends JpaRepository<M_SRWA_12C_Archival_Detail_Entity, M_SRWA_12C_Archival_Detail_PK> {

	@Query(value = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE >= TRUNC(?1) "
			+ "AND REPORT_DATE < TRUNC(?1) + 1 " + "AND REPORT_VERSION = ?2", nativeQuery = true)
	List<M_SRWA_12C_Archival_Detail_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);

	// Fetch latest archival version for given date (no version input)
	@Query(value = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL "
			+ "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
			+ "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	Optional<M_SRWA_12C_Archival_Detail_Entity> getLatestArchivalVersionByDate(Date report_date);

	@Query(value = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2", nativeQuery = true)
	Optional<M_SRWA_12C_Archival_Detail_Entity> findByReport_dateAndReport_version(Date report_date,
			BigDecimal report_version);

	// Current Report Version Only Shown
	@Query(value = "SELECT *  FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	List<M_SRWA_12C_Archival_Detail_Entity> getdatabydateListWithVersion();

	@Query(value = "SELECT *  FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL WHERE REPORT_VERSION IS NOT NULL ", nativeQuery = true)
	List<M_SRWA_12C_Archival_Detail_Entity> getdatabydateListWithVersionAll();

}
