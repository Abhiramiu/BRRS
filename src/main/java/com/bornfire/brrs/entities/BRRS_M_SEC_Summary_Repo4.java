package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_SEC_Summary_Repo4 extends JpaRepository<BRRS_M_SEC_Summary_Entity4, Date> {

	@Query(value = "select * from BRRS_M_SEC_SUMMARYTABLE4", nativeQuery = true)
	List<BRRS_M_SEC_Summary_Entity4> getdatabydateList(Date rpt_code);
	
}

