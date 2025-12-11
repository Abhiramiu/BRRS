
package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_ADISB2_Summary_Repo extends JpaRepository<ADISB2_Summary_Entity, Date> {

    @Query(value = "SELECT * FROM BRRS_ADISB2_SUMMARYTABLE WHERE REPORT_DATE=?1", nativeQuery = true)
    List<ADISB2_Summary_Entity> getdatabydateList(Date reportdate);
}














