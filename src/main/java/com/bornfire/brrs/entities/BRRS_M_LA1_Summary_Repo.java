package com.bornfire.brrs.entities;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_M_LA1_Summary_Repo extends JpaRepository<M_LA1_Summary_Entity, Date> {
	 @Query(value = "SELECT * FROM BRRS_M_LA1_SUMMARYTABLE WHERE REPORT_DATE = :report_date", nativeQuery = true)
		List<M_LA1_Summary_Entity> getdatabydateList(@Param("report_date") Date report_date);
	

    @Query(value = "SELECT * FROM BRRS_M_LA1_SUMMARYTABLE WHERE REPORT_CODE = :reportCode", nativeQuery = true)
    List<M_LA1_Summary_Entity> findByReportCode(@Param("reportCode") String reportCode);
    
   
	
}