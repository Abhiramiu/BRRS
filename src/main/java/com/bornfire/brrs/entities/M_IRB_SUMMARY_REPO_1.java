package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface M_IRB_SUMMARY_REPO_1 extends JpaRepository<M_IRB_SUMMARY_ENTITY_1, Date>{	
	@Query(value = "select * from BRRS_M_IRB_SUMMARY_TABLE where REPORT_DATE =?1  ", nativeQuery = true)
    List<M_IRB_SUMMARY_ENTITY_1> getdatabydateList(Date reportdate);
}
