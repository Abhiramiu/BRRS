package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_RPD_Summary_Repo3  extends JpaRepository<M_RPD_Summary_Entity3, Date>{
	
@Query(value = "select * from BRRS_M_RPD_SUMMARYTABLE3 where report_date=?1", nativeQuery=true)
List<M_RPD_Summary_Entity3> getdatabydateList(Date report_date);
}