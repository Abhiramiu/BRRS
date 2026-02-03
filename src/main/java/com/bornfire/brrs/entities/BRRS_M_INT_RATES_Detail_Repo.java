package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_INT_RATES_Detail_Repo extends JpaRepository<M_INT_RATES_Detail_Entity,Date> {

	 @Query(value = "SELECT * FROM BRRS_M_INT_RATES_DETAILTABLE WHERE REPORT_DATE = ?1", nativeQuery = true)
	    List<M_INT_RATES_Detail_Entity> getdatabydateList(Date reportdate);
    		  
}
