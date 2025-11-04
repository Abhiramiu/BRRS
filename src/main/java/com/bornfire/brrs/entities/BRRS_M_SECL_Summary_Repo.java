package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_SECL_Summary_Repo extends JpaRepository<M_SECL_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_M_SECL_SUMMARYTABLE ", nativeQuery = true)
	List<M_SECL_Summary_Entity> getdatabydateList(Date rpt_code);
	
}

