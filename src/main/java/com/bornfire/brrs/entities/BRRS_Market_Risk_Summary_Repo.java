package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Market_Risk_Summary_Repo extends JpaRepository<Market_Risk_Summary_Entity , Date> {
    
@Query(value = "select * from BRRS_MARKET_RISK_SUMMARYTABLE ", nativeQuery = true)
	List<Market_Risk_Summary_Entity> getdatabydateList(Date reportdate);

}
