package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_ADISB1_Manual_Summary_Repo extends JpaRepository<ADISB1_Manual_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_ADISB1_MANUAL_SUMMARYTABLE", nativeQuery = true)
	List<ADISB1_Manual_Summary_Entity> getdatabydateList(Date rpt_code);

}
