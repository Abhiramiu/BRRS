package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_CREDIT_RISK_Summary_Repo extends JpaRepository<CREDIT_RISK_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_CREDIT_RISK_SUMMARYTABLE ", nativeQuery = true)
	List<CREDIT_RISK_Summary_Entity> getdatabydateList(Date reportdate);

}
