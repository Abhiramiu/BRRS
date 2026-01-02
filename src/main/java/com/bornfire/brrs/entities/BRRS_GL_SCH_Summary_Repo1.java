package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_GL_SCH_Summary_Repo1 extends JpaRepository<GL_SCH_Summary_Entity1 , Date> {
    
@Query(value = "select * from BRRS_GL_SCH_SUMMARYTABLE1 ", nativeQuery = true)
	List<GL_SCH_Summary_Entity1> getdatabydateList(Date reportdate);

}
