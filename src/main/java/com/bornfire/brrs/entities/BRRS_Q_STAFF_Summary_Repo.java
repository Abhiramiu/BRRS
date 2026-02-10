package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_STAFF_Summary_Repo
        extends JpaRepository<Q_STAFF_Summary_Entity, Date> {

    // Fetch all rows for a specific report date
    @Query(value = "SELECT * FROM BRRS_Q_STAFF_SUMMARYTABLE WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<Q_STAFF_Summary_Entity> getdatabydateList(Date rpt_date);


}
