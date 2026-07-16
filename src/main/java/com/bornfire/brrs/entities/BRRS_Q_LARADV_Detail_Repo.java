package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_LARADV_Detail_Repo  extends JpaRepository<Q_LARADV_Detail_Entity, Long> {

	 @Query(value = "SELECT * FROM BRRS_Q_LARADV_DETAILTABLE a WHERE a.REPORT_DATE = ?1",
	            nativeQuery = true)
	  List<Q_LARADV_Detail_Entity> getdatabydateList(Date Report_date);
}
