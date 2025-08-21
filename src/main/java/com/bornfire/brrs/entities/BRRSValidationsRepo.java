package com.bornfire.brrs.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRSValidationsRepo extends JpaRepository<BRRSValidations, String> {

	@Query(value = "select * from BRRS_REPORT_VALIDATION_TABLE where rpt_code=?1 ORDER BY srl_no ", nativeQuery = true)
	List<BRRSValidations> getValidationList(String rpt_code);
}
