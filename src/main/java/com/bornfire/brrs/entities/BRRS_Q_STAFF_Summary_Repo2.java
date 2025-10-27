
package com.bornfire.brrs.entities;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BRRS_Q_STAFF_Summary_Repo2 extends JpaRepository<Q_STAFF_Summary_Entity2, Date> {
	@Query(value = "select * from BRRS_Q_STAFF_SUMMARYTABLE2  ", nativeQuery = true)
	List<Q_STAFF_Summary_Entity2> getdatabydateList(Date rpt_code);
	
}