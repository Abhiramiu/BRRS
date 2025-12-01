package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_PD_Summary_Repo1 extends JpaRepository<M_PD_Summary_Entity1, Date> {
    
    @Query(value = "select * from BRRS_M_PD_SUMMARYTABLE1", nativeQuery = true)
	List<M_PD_Summary_Entity1> getdatabydateList(Date rpt_code);
}
