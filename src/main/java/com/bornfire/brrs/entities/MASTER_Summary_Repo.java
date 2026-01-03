package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MASTER_Summary_Repo extends JpaRepository<MASTER_Summary_Entity, Date>{	
	@Query(value = "select * from BRRS_MASTER_SUMMARYTABLE where REPORT_DATE =?1  ", nativeQuery = true)
    List<MASTER_Summary_Entity> getdatabydateList(Date reportdate);
}
