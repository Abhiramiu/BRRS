package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bornfire.brrs.entities.BrrsMNosvosP1Detail;
import java.util.Date;
import java.util.List;

public interface BrrsMNosvosP1DetailRepository extends JpaRepository<BrrsMNosvosP1Detail, Date> {
	
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_DETAIL_P1 WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP1Detail> getDataByDate(Date report_date);
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_DETAIL_P1", nativeQuery = true)
	List<BrrsMNosvosP1Detail> getData();

}
