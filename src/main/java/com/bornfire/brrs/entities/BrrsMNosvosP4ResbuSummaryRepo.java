package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bornfire.brrs.entities.BrrsMNosvosP1;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BrrsMNosvosP4ResbuSummaryRepo extends JpaRepository<BrrsMNosvosP4ResbuSummaryEntity, BRRS_NOSVOS_Summary_PK> {
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P4_RESUB_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP4ResbuSummaryEntity> getDataByDate(Date report_date);
	
	@Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_NOSVOS_P4_RESUB_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_NOSVOSarchival();

    @Query(value = "select * from BRRS_M_NOSVOS_P4_RESUB_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<BrrsMNosvosP4ResbuSummaryEntity> getdatabydateListarchival(Date report_date, BigDecimal report_version);

    @Query(value = "SELECT * FROM BRRS_M_NOSVOS_P4_RESUB_SUMMARY "
            + "WHERE REPORT_DATE = ?1 AND REPORT_VERSION IS NOT NULL " + "ORDER BY TO_NUMBER(REPORT_VERSION) DESC "
            + "FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<BrrsMNosvosP4ResbuSummaryEntity> getLatestArchivalVersionByDate(Date reportDate);

}
