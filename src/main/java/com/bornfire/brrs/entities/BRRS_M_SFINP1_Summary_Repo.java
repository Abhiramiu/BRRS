package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BRRS_M_SFINP1_Summary_Repo extends JpaRepository<M_SFINP1_Summary_Entity, Date> {

    @Query(value = "SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE WHERE REPORT_DATE = :reportDate", nativeQuery = true)
    List<M_SFINP1_Summary_Entity> getdatabydateList(@Param("reportDate") Date reportDate);
    
    @Query(value = "SELECT * "+
    		"FROM BRRS_M_SFINP1_SUMMARYTABLE "+
    		"WHERE TRUNC(REPORT_DATE) = TO_DATE(?1, 'DD-MON-YYYY')", nativeQuery = true)
    List<M_SFINP1_Summary_Entity> getdatabydateList1(String reportDate);

    
}
