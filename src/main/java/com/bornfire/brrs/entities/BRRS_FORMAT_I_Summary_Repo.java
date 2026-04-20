package com.bornfire.brrs.entities;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BRRS_FORMAT_I_Summary_Repo extends JpaRepository<FORMAT_I_Summary_Entity, Date> {
	 @Query(value = "SELECT * FROM BRRS_FORMAT_I_SUMMARYTABLE WHERE REPORT_DATE = :report_date", nativeQuery = true)
		List<FORMAT_I_Summary_Entity> getdatabydateList(@Param("report_date") Date report_date);
	

    @Query(value = "SELECT * FROM BRRS_FORMAT_I_SUMMARYTABLE WHERE REPORT_CODE = :reportCode", nativeQuery = true)
    List<FORMAT_I_Summary_Entity> findByReportCode(@Param("reportCode") String reportCode);
    
   
	
}