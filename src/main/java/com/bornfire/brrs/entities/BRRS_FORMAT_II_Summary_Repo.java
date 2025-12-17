package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_FORMAT_II_Summary_Repo extends JpaRepository<FORMAT_II_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_FORMAT_II_SUMMARYTABLE ", nativeQuery = true)
	List<FORMAT_II_Summary_Entity> getdatabydateList(Date reportdate);

}
