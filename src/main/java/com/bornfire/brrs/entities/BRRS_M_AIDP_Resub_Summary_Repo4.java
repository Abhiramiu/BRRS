package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_AIDP_Resub_Summary_Repo4 extends JpaRepository<M_AIDP_Resub_Summary_Entity4, M_AIDP_PK> {
	@Query(value = "SELECT 4 AS SECTION_ID, REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_AIDP_RESUBTABLE_SUMMARY4 "
			+ "ORDER BY REPORT_VERSION", nativeQuery = true)
	List<Object[]> getM_AIDParchival();

	@Query(value = "select * from BRRS_M_AIDP_RESUBTABLE_SUMMARY4 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	List<M_AIDP_Resub_Summary_Entity4> getdatabydateListarchival(Date report_date, BigDecimal report_version);

	@Query(value = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE " + "FROM BRRS_M_AIDP_RESUBTABLE_SUMMARY4 "
			+ "WHERE REPORT_VERSION IS NOT NULL " + "ORDER BY REPORT_VERSION", nativeQuery = true)
	List<Object[]> getResubData();

	@Query("SELECT COALESCE(MAX(e.id.report_version), 0) FROM M_AIDP_Resub_Summary_Entity4 e")
	BigDecimal findGlobalMaxReportVersion();

}
