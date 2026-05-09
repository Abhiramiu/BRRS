package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrrsMNosvosP5DetailRepository extends JpaRepository<BrrsMNosvosP5Detail, Date> {
	

	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_DETAIL_P5 WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP5Detail> getDataByDate(Date report_date);
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_DETAIL_P5", nativeQuery = true)
	List<BrrsMNosvosP5Detail> getData();

}
