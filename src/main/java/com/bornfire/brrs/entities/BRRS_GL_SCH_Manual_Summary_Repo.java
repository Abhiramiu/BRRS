package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_GL_SCH_Manual_Summary_Repo extends JpaRepository<GL_SCH_Manual_Summary_Entity , Date> {
    
@Query(value = "select * from BRRS_GL_SCH_MANUAL_SUMMARYTABLE ", nativeQuery = true)
	List<GL_SCH_Manual_Summary_Entity> getdatabydateList(Date reportdate);

}
