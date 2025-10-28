package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_SRWA_12B_Summary_Repo2 extends JpaRepository<M_SRWA_12B_Summary_Entity2, Date> {

	@Query(value = "select * from BRRS_M_SRWA_12B_SUMMARYTABLE2", nativeQuery = true)
	List<M_SRWA_12B_Summary_Entity2> getdatabydateList(Date rpt_code);

}
