package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_SRWA_12E_LTV_Summary_Repo
        extends JpaRepository<M_SRWA_12E_LTV_Summary_Entity, Date> {

    // Fetch all rows for a specific report date
    @Query(value = "SELECT * FROM BRRS_M_SRWA_12E_SUMMARYTABLE WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<M_SRWA_12E_LTV_Summary_Entity> getdatabydateList(Date rpt_date);


}
