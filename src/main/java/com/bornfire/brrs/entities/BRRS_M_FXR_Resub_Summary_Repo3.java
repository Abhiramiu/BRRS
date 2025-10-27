package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_FXR_Resub_Summary_Repo3 extends JpaRepository<M_FXR_Resub_Summary_Entity3, Date> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_M_FXR_RESUB_SUMMARYTABLE3 order by REPORT_VERSION", nativeQuery = true)
    List<Object> getM_FXRResub();

    @Query(value = "select * from BRRS_M_FXR_RESUB_SUMMARYTABLE3 where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<M_FXR_Resub_Summary_Entity3> getdatabydateListResub(Date report_date, String report_version);
}
