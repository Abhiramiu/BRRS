package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_DEFERRED_TAX_Summary_Repo extends JpaRepository<DEFERRED_TAX_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_DEFERRED_TAX_SUMMARYTABLE ", nativeQuery = true)
	List<DEFERRED_TAX_Summary_Entity> getdatabydateList(Date reportdate);

}
