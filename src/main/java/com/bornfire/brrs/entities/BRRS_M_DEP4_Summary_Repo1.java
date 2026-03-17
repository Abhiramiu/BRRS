package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_DEP4_Summary_Repo1  extends JpaRepository<M_DEP4_Summary_Entity1, Date> {

	

    // Fetch all rows for a specific report date
    @Query(value = "SELECT * FROM BRRS_M_DEP4_SUMMARYTABLE1 WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<M_DEP4_Summary_Entity1> getdatabydateList(Date report_date);

    
}