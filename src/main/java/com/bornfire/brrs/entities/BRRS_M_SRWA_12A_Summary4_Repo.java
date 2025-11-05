package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface BRRS_M_SRWA_12A_Summary4_Repo extends JpaRepository<M_SRWA_12A_Summary_Entity4 , Date> {

	 @Query(value = "SELECT * FROM BRRS_M_SRWA_12A_SUMMARYTABLE4 WHERE REPORT_DATE = ?1", nativeQuery = true)
	    List<M_SRWA_12A_Summary_Entity4> getdatabydateList(Date reportdate);

}

