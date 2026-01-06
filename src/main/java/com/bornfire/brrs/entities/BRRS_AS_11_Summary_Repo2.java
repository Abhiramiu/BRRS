package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_AS_11_Summary_Repo2 extends JpaRepository<AS_11_Summary_Entity2 , Date> {
    
@Query(value = "select * from BRRS_AS_11_SUMMARYTABLE2 ", nativeQuery = true)
	List<AS_11_Summary_Entity2> getdatabydateList(Date reportdate);

}
