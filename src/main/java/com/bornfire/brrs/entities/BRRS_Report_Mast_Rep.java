package com.bornfire.brrs.entities;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
public interface BRRS_Report_Mast_Rep extends JpaRepository<BRRS_Report_Mast_Entity, BigDecimal> {
	
	@Query(value = "SELECT rpt_code FROM BRRS_REPORT_MAST WHERE WORK_FLG = 'Y' ORDER BY srl_no ", nativeQuery = true)
	List<String> getRptCode();

	@Query(value = "SELECT rpt_description FROM BRRS_REPORT_MAST where rpt_code=?1 ", nativeQuery = true)
	List<String> getRptName(String rpdCode);

}