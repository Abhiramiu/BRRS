package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_DEP4_Summary_Repo2  extends JpaRepository<M_DEP4_Summary_Entity2, Date> {

	

    // Fetch all rows for a specific report date
    @Query(value = "SELECT * FROM BRRS_M_DEP4_SUMMARYTABLE2 WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<M_DEP4_Summary_Entity2> getdatabydateList(Date report_date);
}