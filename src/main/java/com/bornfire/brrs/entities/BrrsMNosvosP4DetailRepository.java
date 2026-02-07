package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bornfire.brrs.entities.BrrsMNosvosP4Detail;
import java.util.Date;
import java.util.List;

public interface BrrsMNosvosP4DetailRepository extends JpaRepository<BrrsMNosvosP4Detail, Date> {
	
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_DETAIL_P4 WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP4Detail> getDataByDate(Date report_date);
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_DETAIL_P4", nativeQuery = true)
	List<BrrsMNosvosP4Detail> getData();
	
}
