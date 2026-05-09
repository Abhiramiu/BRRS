package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrrsMNosvosP3Repository extends JpaRepository<BrrsMNosvosP3, Date> {
	
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P3 WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP3> getDataByDate(Date report_date);
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P3", nativeQuery = true)
	List<BrrsMNosvosP3> getData();
	
}
