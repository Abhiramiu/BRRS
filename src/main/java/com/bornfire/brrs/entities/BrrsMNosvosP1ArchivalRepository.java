package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bornfire.brrs.entities.BrrsMNosvosP1;
import java.util.Date;
import java.util.List;

public interface BrrsMNosvosP1ArchivalRepository extends JpaRepository<BrrsMNosvosP1Archival, Date> {
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P1_ARCHIVAL WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP1> getDataByDate(Date report_date);
	
	 @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_NOSVOS_P1_ARCHIVAL order by REPORT_VERSION", nativeQuery = true)
	    List<Object> getM_NOSVOSarchival();

	    @Query(value = "select * from BRRS_M_NOSVOS_P1_ARCHIVAL where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	    List<BrrsMNosvosP1Archival> getdatabydateListarchival(Date report_date, String report_version);

}
