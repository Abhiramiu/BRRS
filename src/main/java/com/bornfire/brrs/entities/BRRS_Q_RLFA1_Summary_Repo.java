package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_RLFA1_Summary_Repo extends JpaRepository<Q_RLFA1_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_Q_RLFA1_SUMMARY_TABLE where REPORT_DATE =?1  ", nativeQuery = true)
    List<Q_RLFA1_Summary_Entity> getdatabydateList(Date reportdate);

}

