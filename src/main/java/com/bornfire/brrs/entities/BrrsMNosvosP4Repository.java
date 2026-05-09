package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrrsMNosvosP4Repository extends JpaRepository<BrrsMNosvosP4, Date> {
	
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P4 WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP4> getDataByDate(Date report_date);
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P4", nativeQuery = true)
	List<BrrsMNosvosP4> getData();
	
}
