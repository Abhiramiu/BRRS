package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrrsMNosvosP5Repository extends JpaRepository<BrrsMNosvosP5, Date> {
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P5 WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP5> getDataByDate(Date report_date);
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P5", nativeQuery = true)
	List<BrrsMNosvosP5> getData();

}
