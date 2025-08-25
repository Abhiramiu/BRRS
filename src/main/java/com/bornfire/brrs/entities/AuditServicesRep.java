package com.bornfire.brrs.entities;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface AuditServicesRep extends JpaRepository<AuditServicesEntity , String>{
	@Query(value = "select * from BRRS_AUDIT ", nativeQuery = true)
	List<AuditServicesEntity> getauditService();

		
		@Query(value = "SELECT * FROM BRRS_AUDIT WHERE FUNC_CODE  != 'Login'", nativeQuery = true)
		List<AuditServicesEntity> getServiceAudit();
		@Query(value = "SELECT * FROM BRRS_AUDIT WHERE FUNC_CODE  = 'Login'", nativeQuery = true)
		List<AuditServicesEntity> getUserAudit();

		@Query(value = "SELECT change_details FROM BRRS_AUDIT  WHERE audit_ref_no = ?1", nativeQuery = true)
		String getchanges(String audit_ref_no);


}
