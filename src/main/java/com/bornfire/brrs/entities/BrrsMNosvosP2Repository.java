package com.bornfire.brrs.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bornfire.brrs.entities.BrrsMNosvosP2;
import java.util.Date;
import java.util.List;

public interface BrrsMNosvosP2Repository extends JpaRepository<BrrsMNosvosP2, Date> {
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P2 WHERE TRUNC(REPORT_DATE) = TRUNC(?1)", nativeQuery = true)
	List<BrrsMNosvosP2> getDataByDate(Date report_date);
	
	@Query(value = "SELECT * FROM BRRS_M_NOSVOS_P2", nativeQuery = true)
	List<BrrsMNosvosP2> getData();
	
}
