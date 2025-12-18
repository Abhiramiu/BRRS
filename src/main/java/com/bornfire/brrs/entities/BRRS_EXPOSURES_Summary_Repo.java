
package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_EXPOSURES_Summary_Repo extends JpaRepository<EXPOSURES_Summary_Entity, Date> {

    @Query(value = "SELECT * FROM BRRS_EXPOSURES_SUMMARYTABLE WHERE REPORT_DATE=?1", nativeQuery = true)
    List<EXPOSURES_Summary_Entity> getdatabydateList(Date reportdate);
}














