package com.bornfire.brrs.entities;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_SRWA_12C_Summary_Repo extends JpaRepository<M_SRWA_12C_Summary_Entity, Date> {
	
	
	@Query(value = "select * from BRRS_M_SRWA_12C_SUMMARYTABLE  ", nativeQuery = true)
	List<M_SRWA_12C_Summary_Entity> getdatabydateList(Date report_date);

}



	


