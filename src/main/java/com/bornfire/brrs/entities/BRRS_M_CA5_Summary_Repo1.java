package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface BRRS_M_CA5_Summary_Repo1 extends JpaRepository <M_CA5_Summary_Entity1, Date> {

	@Query(value = "select * from BRRS_M_CA5_SUMMARYTABLE1 where report_date = ?1", nativeQuery = true)
	List<M_CA5_Summary_Entity1> getdatabydateList(Date rpt_date);

}
