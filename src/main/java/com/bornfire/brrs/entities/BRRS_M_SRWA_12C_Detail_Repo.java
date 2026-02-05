package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_M_SRWA_12C_Detail_Repo extends JpaRepository<M_SRWA_12C_Detail_Entity, Date> {

	// ✅ Fetch record(s) by specific REPORT_DATE
	@Query(value = "SELECT * FROM BRRS_M_SRWA_12C_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(:rpt_code)", nativeQuery = true)
	List<M_SRWA_12C_Detail_Entity> getdatabydateList(@Param("rpt_code") Date rpt_code);

	@Query(value = "SELECT *  FROM BRRS_M_SRWA_12C_DETAILTABLE WHERE REPORT_DATE = ?1   AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	List<M_SRWA_12C_Detail_Entity> getdatabydateListWithVersion(String todate);

	// ✅ Find the latest version for a report date
	@Query(value = "SELECT * FROM BRRS_M_SRWA_12C_DETAILTABLE " + "WHERE REPORT_DATE = ?1 "
			+ "ORDER BY TO_NUMBER(REPORT_VERSION) DESC " + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	Optional<M_SRWA_12C_Detail_Entity> findTopByReport_dateOrderByReport_versionDesc(Date report_date);

	// ✅ Check if a version exists for a report date
	@Query(value = "SELECT * FROM BRRS_M_SRWA_12C_DETAILTABLE "
			+ "WHERE REPORT_DATE = ?1 AND REPORT_VERSION = ?2", nativeQuery = true)
	Optional<M_SRWA_12C_Detail_Entity> findByReport_dateAndReport_version(Date report_date, BigDecimal report_version);

	@Query(value = "SELECT *  FROM BRRS_M_SRWA_12C_DETAILTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	List<M_SRWA_12C_Detail_Entity> getdatabydateListWithVersion();

}
