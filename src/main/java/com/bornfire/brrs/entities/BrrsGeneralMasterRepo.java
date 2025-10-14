package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrrsGeneralMasterRepo extends JpaRepository<BrrsGeneralMasterEntity, String> {

		@Query(value = "select * from BRRS_GENERAL_MASTER_TABLE", nativeQuery = true)
		List<BrrsGeneralMasterEntity> GetAllData();

		  @Query(value = "select count(*) from BRRS_GENERAL_MASTER_TABLE", nativeQuery = true)
		    int countAll();

		    // Paginated fetch
		    @Query(value = "select * from BRRS_GENERAL_MASTER_TABLE offset ?1 rows fetch next ?2 rows only", nativeQuery = true)
		    List<BrrsGeneralMasterEntity> getdatabydateList(int offset, int limit);
}