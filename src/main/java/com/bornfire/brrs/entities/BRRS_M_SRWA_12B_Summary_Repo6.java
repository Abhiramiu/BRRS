package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_SRWA_12B_Summary_Repo6 extends JpaRepository<M_SRWA_12B_Summary_Entity6, Date> {

	@Query(value = "select * from BRRS_M_SRWA_12B_SUMMARYTABLE6 WHERE REPORT_DATE = ?1", nativeQuery = true)
	List<M_SRWA_12B_Summary_Entity6> getdatabydateList(Date REPORTDATE);

}
