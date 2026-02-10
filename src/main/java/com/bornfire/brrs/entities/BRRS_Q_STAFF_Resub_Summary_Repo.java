package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_Q_STAFF_Resub_Summary_Repo extends JpaRepository<Q_STAFF_Resub_Summary_Entity, Q_STAFF_PK> {

	@Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_Q_STAFF_RESUB_SUMMARYTABLE order by REPORT_VERSION", nativeQuery = true)
	List<Object> getQ_STAFFarchival();

	@Query(value = "select * from BRRS_Q_STAFF_RESUB_SUMMARYTABLE where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	List<Q_STAFF_Resub_Summary_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);

	@Query(value = "SELECT * FROM BRRS_Q_STAFF_RESUB_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
	List<Q_STAFF_Resub_Summary_Entity> getdatabydateListWithVersion();
	
	@Query("SELECT MAX(e.reportVersion) FROM Q_STAFF_Resub_Summary_Entity e WHERE e.reportDate = :date")
	BigDecimal findMaxVersion(@Param("date") Date date);


}
