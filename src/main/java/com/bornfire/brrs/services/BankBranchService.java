package com.bornfire.brrs.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bornfire.brrs.entities.BankBranchMaster;
import com.bornfire.brrs.entities.BankBranchMasterRepo;

@Service
public class BankBranchService {

	 @Autowired
	    private BankBranchMasterRepo bankBranchMasterRepo;

	    // Save or Update Branch
	    public void saveBranch(BankBranchMaster branch, String userId) {
	        branch.setEntryTime(new Date());
	        branch.setEntryUser(userId);
	        branch.setDelFlg("N");
	        bankBranchMasterRepo.save(branch);
	    }

	    // Soft Delete Branch
	    public void softDeleteBranch(String solId) {
	        Optional<BankBranchMaster> branchOpt = bankBranchMasterRepo.findById(solId);
	        if (branchOpt.isPresent()) {
	            BankBranchMaster branch = branchOpt.get();
	            branch.setDelFlg("Y");
	            bankBranchMasterRepo.save(branch); // update instead of delete
	        }
	    }
}

