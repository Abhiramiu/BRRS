package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_FORMAT_III_Summary_Repo extends JpaRepository<FORMAT_III_Summary_Entity, Date> {

	@Query(value = "SELECT * FROM BRRS_FORMAT_III_SUMMARYTABLE WHERE REPORT_DATE = ?1", nativeQuery = true)
	List<FORMAT_III_Summary_Entity> getdatabydateList(Date rpt_date);

}
