package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface MCBL_Main_Rep extends JpaRepository<MCBL_Main_Entity, String> {
	
	@Query(value = "SELECT * FROM BRRS_MCBL_MAIN ORDER BY ID", nativeQuery = true)
	List<MCBL_Main_Entity> getall();
	
	@Query(value = "SELECT MAX(id) FROM BRRS_MCBL_MAIN", nativeQuery = true)
	String getMaxId();

}