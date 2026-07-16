package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_LARADV_Archival_Summary_Repo  extends JpaRepository<Q_LARADV_Archival_Summary_Entity, Long> {

	 @Query(value = "SELECT * FROM BRRS_Q_LARADV_ARCHIVAL_SUMMARYTABLE a WHERE a.REPORT_DATE = ?1 AND REPORT_VERSION = ?2",
	            nativeQuery = true)
	  List<Q_LARADV_Archival_Summary_Entity> getdatabydateList(Date Report_Date, BigDecimal version);
		
	 @Query(value = "SELECT REPORT_DATE, report_version, REPORT_RESUBDATE FROM BRRS_Q_LARADV_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_VERSION IS NOT NULL " + "ORDER BY REPORT_VERSION", nativeQuery = true)
	List<Object[]> getResubData();
	
}
