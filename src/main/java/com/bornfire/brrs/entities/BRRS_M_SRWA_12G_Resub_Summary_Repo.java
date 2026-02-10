package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_M_SRWA_12G_Resub_Summary_Repo
        extends JpaRepository<M_SRWA_12G_Resub_Summary_Entity, M_SRWA_12G_PK> {

    // 1️⃣ Get available report dates and versions (archival list)
    // Better to return Object[] instead of Object
    @Query(value = "SELECT REPORT_DATE, REPORT_VERSION " +
                   "FROM BRRS_M_SRWA_12G_RESUB_SUMMARYTABLE " +
                   "ORDER BY REPORT_VERSION", nativeQuery = true)
    List<Object[]> getM_SRWA_12Garchival();


    // 2️⃣ Get data by date and version (AVOID select *)
    @Query(value = "SELECT * " +
                   "FROM BRRS_M_SRWA_12G_RESUB_SUMMARYTABLE " +
                   "WHERE REPORT_DATE = :reportDate " +
                   "AND REPORT_VERSION = :reportVersion", nativeQuery = true)
    List<M_SRWA_12G_Resub_Summary_Entity> getdatabydateListarchival(
            @Param("reportDate") Date reportDate,
            @Param("reportVersion") BigDecimal reportVersion
    );


    // 3️⃣ Get all rows where version is not null
    @Query(value = "SELECT * " +
                   "FROM BRRS_M_SRWA_12G_RESUB_SUMMARYTABLE " +
                   "WHERE REPORT_VERSION IS NOT NULL " +
                   "ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<M_SRWA_12G_Resub_Summary_Entity> getdatabydateListWithVersion();


    // 4️⃣ Get max version for a given date (JPQL, safer 👍)
    @Query("SELECT MAX(e.reportVersion) " +
           "FROM M_SRWA_12G_Resub_Summary_Entity e " +
           "WHERE e.reportDate = :date")
    BigDecimal findMaxVersion(@Param("date") Date date);

}
