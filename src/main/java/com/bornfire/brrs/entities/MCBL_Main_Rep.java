package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface MCBL_Main_Rep extends JpaRepository<MCBL_Main_Entity, String> {
	


    // Count total records
    @Query(value = "select count(*) from BRRS_MCBL_MAIN", nativeQuery = true)
    int countAll();
    // Paginated fetch
    @Query(value = "select * from BRRS_MCBL_MAIN offset ?1 rows fetch next ?2 rows only", nativeQuery = true)
    List<MCBL_Main_Entity> getdatabydateList(int offset, int limit);
	
	@Query(value = "SELECT * FROM BRRS_MCBL_MAIN where DEL_FLG='N' ORDER BY ID", nativeQuery = true)
	List<MCBL_Main_Entity> getall();
	

	@Query(value = "SELECT * FROM BRRS_MCBL_MAIN where id=?1", nativeQuery = true)
	MCBL_Main_Entity getbyid(String id);
	
	@Query(value = "SELECT MAX(id) FROM BRRS_MCBL_MAIN", nativeQuery = true)
	String getMaxId();

}