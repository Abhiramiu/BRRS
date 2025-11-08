package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;




@Repository
public interface BRRS_Q_SMME_DEP_Archival_Summary_Repo extends JpaRepository<Q_SMME_DEP_Archival_Summary_Entity, Date> {

	 @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_Q_SMME_DEP_ARCHIVAL_SUMMARYTABLE order by REPORT_VERSION", nativeQuery = true)
	    List<Object> getQ_SMME_DEParchival();

	    @Query(value = "select * from BRRS_Q_SMME_DEP_ARCHIVAL_SUMMARYTABLE where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
	    List<Q_SMME_DEP_Archival_Summary_Entity> getdatabydateListarchival(Date report_date, String report_version);
}
