package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_DBS10_FINCON_II_1A_Summary_Repo extends JpaRepository<DBS10_FINCON_II_1A_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_DBS10_FINCON_II_1A_SUMMARYTABLE ", nativeQuery = true)
	List<DBS10_FINCON_II_1A_Summary_Entity> getdatabydateList(Date reportdate);

}