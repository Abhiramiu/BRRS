package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_LARADV_Summary_Repo  extends JpaRepository<Q_LARADV_Summary_Entity, Long> {

	@Query(value = "SELECT * FROM BRRS_Q_LARADV_SUMMARYTABLE a WHERE a.REPORT_DATE = ?1 ORDER BY a.SNO",
    nativeQuery = true)
	List<Q_LARADV_Summary_Entity> getdatabydateList(Date reportDate);
}
