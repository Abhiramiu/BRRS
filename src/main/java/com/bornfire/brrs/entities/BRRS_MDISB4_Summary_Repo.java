package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_MDISB4_Summary_Repo extends JpaRepository<MDISB4_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_MDISB4_SUMMARYTABLE ", nativeQuery = true)
	List<MDISB4_Summary_Entity> getdatabydateList(Date reportdate);

}