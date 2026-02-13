package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bornfire.brrs.entities.BrrsMNosvosP2ResbuDetailEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BrrsMNosvosP2ResbuDetailRepo extends JpaRepository<BrrsMNosvosP2ResbuDetailEntity, BRRS_NOSVOS_Summary_PK> {
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P2_RESUB_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP2ResbuDetailEntity> getDataByDate(Date report_date);
	
	@Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_NOSVOS_P2_RESUB_SUMMARY order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_NOSVOSarchival();

    @Query(value = "select * from BRRS_M_NOSVOS_P2_RESUB_SUMMARY where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<BrrsMNosvosP2ResbuDetailEntity> getdatabydateListarchival(Date report_date, BigDecimal report_version);


}
