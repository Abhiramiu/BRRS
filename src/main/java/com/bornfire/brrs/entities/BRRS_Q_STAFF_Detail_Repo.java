package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_STAFF_Detail_Repo extends JpaRepository<Q_STAFF_Detail_Entity, Date> {

	@Query(value = "select * from BRRS_Q_STAFF_DETAILTABLE where REPORT_DATE =?1  ", nativeQuery = true)
	List<Q_STAFF_Detail_Entity> getdatabydateList(Date reportdate);

}
