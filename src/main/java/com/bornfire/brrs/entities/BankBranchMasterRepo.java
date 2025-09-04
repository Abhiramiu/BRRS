package com.bornfire.brrs.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BankBranchMasterRepo extends JpaRepository<BankBranchMaster, String> {

	@Query(value = "select * from BANK_BRANCH_MASTER_TB where DEL_FLG!='Y'", nativeQuery = true)
	List<BankBranchMaster> GetAllData();

}
