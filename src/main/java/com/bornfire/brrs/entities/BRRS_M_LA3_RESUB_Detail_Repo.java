package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BRRS_M_LA3_RESUB_Detail_Repo extends JpaRepository<M_LA3_Resub_Detail_Entity, M_LA3_PK> {

    
    @Query(value = "select REPORT_DATE, DATA_ENTRY_VERSION from BRRS_M_LA3_RESUB_DETAILTABLE order by DATA_ENTRY_VERSION", nativeQuery = true)
    List<Object> getM_LA3archival();

    @Query(value = "select * from BRRS_M_LA3_RESUB_DETAILTABLE where REPORT_DATE = ?1 and DATA_ENTRY_VERSION = ?2", nativeQuery = true)
    List<M_LA3_Resub_Detail_Entity> getdatabydateListarchival(Date report_date, BigDecimal report_version);

    @Query(value = "SELECT * FROM BRRS_M_LA3_RESUB_DETAILTABLE WHERE DATA_ENTRY_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC", nativeQuery = true)
    List<M_LA3_Resub_Detail_Entity> getdatabydateListWithVersion();
    
    //Resub
    @Query("SELECT MAX(e.reportVersion) FROM M_LA3_Resub_Detail_Entity e WHERE e.reportDate = :date")
    BigDecimal findMaxVersion(@Param("date") Date date);


}
