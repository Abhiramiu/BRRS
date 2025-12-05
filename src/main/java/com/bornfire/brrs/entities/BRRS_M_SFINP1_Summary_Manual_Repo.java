package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_M_SFINP1_Summary_Manual_Repo extends JpaRepository<M_SFINP1_Summary_Manual_Entity, Date> {

    // ✅ Fetch all by specific report date
	@Query(value = "SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = :reportDate", nativeQuery = true)
    List<M_SFINP1_Summary_Manual_Entity> getdatabydateList(@Param("reportDate") Date reportDate);

    @Query(value = "SELECT R34_MONTH_END FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = :reportDate", nativeQuery = true)
    BigDecimal getCurrentR34MonthEnd(@Param("reportDate") Date reportDate);
    
    @Query(value = "SELECT * "+
    		"FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL "+
    		"WHERE TRUNC(REPORT_DATE) = TO_DATE(?1, 'DD-MON-YYYY')", nativeQuery = true)
    List<M_SFINP1_Summary_Manual_Entity> getdatabydateList1(String reportDate);
}