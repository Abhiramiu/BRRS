package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_AML_Summary_Repo extends JpaRepository<AML_Summary_Entity , Date> {
    
@Query(value = "select * from BRRS_AML_SUMMARYTABLE ", nativeQuery = true)
	List<AML_Summary_Entity> getdatabydateList(Date reportdate);

}
