package com.bornfire.brrs.entities;



import java.util.Date;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrrsCommonMappingRepo 
       extends JpaRepository<BrrsCommonMappingEntity, Long>, BRRSCommonMappingRepositoryCustom {
	
	
	@Query(value = "select * from BRRS_COMMON_MAPPING_TABLE where DEL_FLG != 'Y'", nativeQuery = true)
	List<BrrsCommonMappingEntity> getAllData();
	
	
	@Query(value = "select * from BRRS_COMMON_MAPPING_TABLE where ACCOUNT_NO = ?1", nativeQuery = true)
	BrrsCommonMappingEntity getdatabyAcctNo(String ACCOUNT_NO);

	   
}

