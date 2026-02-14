package com.bornfire.brrs.entities;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_Q_RLFA2_RESUB_Detail_Repo extends JpaRepository<Q_RLFA2_RESUB_Detail_Entity , Q_RLFA2_PK> {

    @Query(value = "select REPORT_DATE, REPORT_VERSION from BRRS_Q_RLFA2_RESUB_DETAILTABLE order by REPORT_VERSION", nativeQuery = true)
    List<Object> getQ_RLFA2archival();

    @Query(value = "select * from BRRS_Q_RLFA2_RESUB_DETAILTABLE where REPORT_DATE = ?1 and REPORT_VERSION = ?2", nativeQuery = true)
    List<Q_RLFA2_RESUB_Detail_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);

    @Query("SELECT MAX(e.report_version) FROM Q_RLFA2_RESUB_Detail_Entity e WHERE e.report_date = :date")
	BigDecimal findMaxVersion(@Param("date") Date date);

	 //Current Report Version Only Shown 
    @Query(value = "SELECT *  FROM BRRS_Q_RLFA2_RESUB_DETAILTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    List<Q_RLFA2_RESUB_Detail_Entity> getdatabydateListWithVersion();
    
}