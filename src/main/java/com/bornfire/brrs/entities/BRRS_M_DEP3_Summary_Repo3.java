
package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_M_DEP3_Summary_Repo3 extends JpaRepository<M_DEP3_Summary_Entity3, Date> {

    @Query(value = "SELECT * FROM BRRS_M_DEP3_SUMMARYTABLE3 WHERE REPORT_DATE=?1", nativeQuery = true)
    List<M_DEP3_Summary_Entity3> getdatabydateList(Date reportdate);
}







