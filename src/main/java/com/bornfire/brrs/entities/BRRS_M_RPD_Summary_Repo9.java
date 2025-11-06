package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_RPD_Summary_Repo9  extends JpaRepository<M_RPD_Summary_Entity9, Date>{
	
@Query(value = "select * from BRRS_M_RPD_SUMMARYTABLE9 where report_date=?1", nativeQuery=true)
List<M_RPD_Summary_Entity9> getdatabydateList(Date report_date);
}