package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_CASH_FLOW_Summary_Repo extends JpaRepository<CASH_FLOW_Summary_Entity, Date> {

	@Query(value = "SELECT * FROM BRRS_CASH_FLOW_SUMMARYTABLE WHERE REPORT_DATE = ?1", nativeQuery = true)
	List<CASH_FLOW_Summary_Entity> getdatabydateList(Date rpt_date);

}
