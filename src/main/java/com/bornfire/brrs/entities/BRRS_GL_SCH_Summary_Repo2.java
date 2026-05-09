package com.bornfire.brrs.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_GL_SCH_Summary_Repo2 extends JpaRepository<GL_SCH_Summary_Entity2, Date> {

	@Query(value = "SELECT * FROM BRRS_GL_SCH_SUMMARYTABLE2 WHERE REPORT_DATE = ?1", nativeQuery = true)
	List<GL_SCH_Summary_Entity2> getdatabydateList(Date reportdate);

}
