package com.bornfire.brrs.entities;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface BRRS_BASEL_III_COM_EQUITY_DISC_Manual_Summary_Repo extends JpaRepository<BASEL_III_COM_EQUITY_DISC_Manual_Summary_Entity, Date> {

	@Query(value = "select * from BRRS_BASEL_III_COM_EQUITY_DISC_MANUAL_SUMMARYTABLE", nativeQuery = true)
	List<BASEL_III_COM_EQUITY_DISC_Manual_Summary_Entity> getdatabydateList(Date rpt_code);
}