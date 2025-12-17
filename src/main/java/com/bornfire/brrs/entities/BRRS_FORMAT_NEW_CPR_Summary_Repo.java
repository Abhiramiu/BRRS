
package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_FORMAT_NEW_CPR_Summary_Repo extends JpaRepository<FORMAT_NEW_CPR_Summary_Entity, Date> {

    @Query(value = "SELECT * FROM BRRS_FORMAT_NEW_CPR_SUMMARYTABLE WHERE REPORT_DATE=?1", nativeQuery = true)
    List<FORMAT_NEW_CPR_Summary_Entity> getdatabydateList(Date reportdate);
}














