package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Common_Disclosure_Summary_Repo extends JpaRepository<Common_Disclosure_Summary_Entity , Date> {
    
@Query(value = "select * from BRRS_COMMON_DISCLOSURE_SUMMARYTABLE ", nativeQuery = true)
	List<Common_Disclosure_Summary_Entity> getdatabydateList(Date reportdate);

}
