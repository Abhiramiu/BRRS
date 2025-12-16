package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_SCH_17_Summary_Repo extends JpaRepository<SCH_17_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_SCH_17_SUMMARYTABLE ", nativeQuery = true)
	List<SCH_17_Summary_Entity> getdatabydateList(Date reportdate);

}
