package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_MDISB5_RESUB_Summary_Repo3 extends JpaRepository<MDISB5_RESUB_Summary_Entity3, MDISB5_PK> {

	@Query(value = "select * from BRRS_MDISB5_RESUB_SUMMARYTABLE3 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	List<MDISB5_RESUB_Summary_Entity3> getdatabydateListarchival(Date report_date, BigDecimal report_version);

	@Query("SELECT MAX(e.reportVersion) FROM MDISB5_RESUB_Summary_Entity3 e WHERE e.reportDate = :date")
	BigDecimal findMaxVersion(@Param("date") Date date);

	@Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_MDISB5_RESUB_SUMMARYTABLE3 order by REPORT_VERSION", nativeQuery = true)
	List<Object> getMDISB5archival();

	// Current Report Version Only Shown
	@Query(value = "SELECT *  FROM BRRS_MDISB5_RESUB_SUMMARYTABLE3 WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	List<MDISB5_RESUB_Summary_Entity3> getdatabydateListWithVersion();

}
