package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_SCOPE_OF_APP_Summary_Repo extends JpaRepository<SCOPE_OF_APP_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_SCOPE_OF_APP_SUMMARYTABLE ", nativeQuery = true)
	List<SCOPE_OF_APP_Summary_Entity> getdatabydateList(Date reportdate);

}
