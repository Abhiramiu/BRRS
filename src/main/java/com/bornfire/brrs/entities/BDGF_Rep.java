package com.bornfire.brrs.entities;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface BDGF_Rep extends JpaRepository<BDGF_Entity, BigDecimal> {
	
	@Query(value = "SELECT * FROM BRRS_BDGF WHERE report_date=? ORDER BY id", nativeQuery = true)
	List<BDGF_Entity> Getcurrentdaydetail(Date Report_date);


}


