package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_CAP_ADEQ_Summary_Repo extends JpaRepository<CAP_ADEQ_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_CAP_ADEQ_SUMMARYTABLE ", nativeQuery = true)
	List<CAP_ADEQ_Summary_Entity> getdatabydateList(Date reportdate);

}
