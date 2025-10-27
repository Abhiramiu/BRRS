package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Q_BRANCHNET_Summay_Repo3 extends JpaRepository<Q_BRANCHNET_Summary_Entity3, Date> {

	@Query(value = "select * from BRRS_Q_BRANCHNET_SUMMARYTABLE3 where report_date = :rpt_code", nativeQuery = true)
List<Q_BRANCHNET_Summary_Entity3> getdatabydateList(@Param("rpt_code") Date rpt_code);

}








