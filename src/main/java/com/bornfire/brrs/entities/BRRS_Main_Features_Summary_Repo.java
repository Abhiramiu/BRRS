package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Main_Features_Summary_Repo extends JpaRepository<Main_Features_Summary_Entity , Date> {
    
@Query(value = "select * from BRRS_MAIN_FEATURES_SUMMARYTABLE ", nativeQuery = true)
	List<Main_Features_Summary_Entity> getdatabydateList(Date reportdate);

}
