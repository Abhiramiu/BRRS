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
public interface BRRS_M_SRWA_12D_Resub_Summary_Repo
		extends JpaRepository<M_SRWA_12D_Resub_Summary_Entity, M_SRWA_12D_PK> {
	@Query(value = "select REPORT_DATE " + "from BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY "
			+ "order by REPORT_VERSION", nativeQuery = true)
	List<Date> getM_SECLParchival();

	@Query(value = "SELECT * FROM BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
	List<M_SRWA_12D_Resub_Summary_Entity> getdatabydateListWithVersion1();

	@Query(value = "select * from BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	List<M_SRWA_12D_Resub_Summary_Entity> getdatabydateListarchival(Date report_date, BigDecimal version);

	@Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY order by REPORT_VERSION", nativeQuery = true)
	List<Object> getM_SRWA_12Darchival();

	// Fetch latest archival version for given date (no version input)
	@Query(value = "SELECT * FROM BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY "
			+ "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
			+ "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	Optional<M_SRWA_12D_Resub_Summary_Entity> getLatestArchivalVersionByDate(Date report_date);

	@Query(value = "SELECT * FROM BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2", nativeQuery = true)
	Optional<M_SRWA_12D_Resub_Summary_Entity> findByReport_dateAndReport_version(Date report_date,
			BigDecimal report_version);

	// Current Report Version Only Shown
	@Query(value = "SELECT *  FROM BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	List<M_SRWA_12D_Resub_Summary_Entity> getdatabydateListWithVersion();

	@Query(value = "SELECT *  FROM BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ", nativeQuery = true)
	List<M_SRWA_12D_Resub_Summary_Entity> getdatabydateListWithVersionAll();

	@Query(value = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
			+ "FROM BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY", nativeQuery = true)
	List<Object[]> getResubData();

	@Query(value = "select * from BRRS_M_SRWA_12D_RESUBTABLE_SUMMARY ", nativeQuery = true)
	List<M_SRWA_12D_Resub_Summary_Entity> getdatabydateList(Date rpt_code);

	@Query("SELECT COALESCE(MAX(e.report_version), 0) " + "FROM M_SRWA_12D_Resub_Summary_Entity e")
	BigDecimal findGlobalMaxReportVersion();

	@Query("SELECT COALESCE(MAX(e.report_version),0) " + "FROM M_SRWA_12D_Resub_Summary_Entity e "
			+ "WHERE e.report_date = :reportDate")
	BigDecimal findMaxVersionByDate(@Param("reportDate") Date reportDate);

}
