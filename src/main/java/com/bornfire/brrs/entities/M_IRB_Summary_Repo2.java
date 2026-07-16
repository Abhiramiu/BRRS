package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface M_IRB_Summary_Repo2 extends JpaRepository<M_IRB_Summary_Entity2, Date>{	
	@Query(value = "select * from BRRS_M_IRB_SUMMARYTABLE2 where REPORT_DATE =?1  ", nativeQuery = true)
    List<M_IRB_Summary_Entity2> getdatabydateList(Date reportdate);
}
