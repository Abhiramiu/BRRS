package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MDISB3_Summary_Repo2 extends JpaRepository<MDISB3_Summary_Entity2, Date>{	
	@Query(value = "select * from BRRS_MDISB3_SUMMARYTABLE2 where REPORT_DATE =?1  ", nativeQuery = true)
    List<MDISB3_Summary_Entity2> getdatabydateList(Date reportdate);
}

