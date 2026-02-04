package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface M_SRWA_12B_SUMMARY_6_NEW_REPO  extends JpaRepository<M_SRWA_12B_SUMMARY_6_NEW_ENTITY, Date>  {
	
	@Query(value = "SELECT *  FROM BRRS_M_SRWA_12B_SUMMARY_TABLE_6_NEW WHERE REPORT_DATE = ?1   AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    M_SRWA_12B_SUMMARY_6_NEW_ENTITY getdatabydateListWithVersion(String todate);

}