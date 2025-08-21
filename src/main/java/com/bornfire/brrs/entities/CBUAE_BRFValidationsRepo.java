package com.bornfire.brrs.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CBUAE_BRFValidationsRepo extends JpaRepository<CBUAE_BRFValidations, String> {

	@Query(value = "select * from CBUAE_BRF_REPORT_VALIDATION_TABLE where rpt_code=?1 ORDER BY srl_no ", nativeQuery = true)
	List<CBUAE_BRFValidations> getValidationList(String rpt_code);
}
