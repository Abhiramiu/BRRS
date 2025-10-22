package com.bornfire.brrs.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.brrs.entities.BRRS_Report_Mast_Rep;
import com.bornfire.brrs.entities.GeneralMasterEntity;
import com.bornfire.brrs.entities.MCBL_Detail_Rep;
import com.bornfire.brrs.services.AuditService;

@RestController
public class Rest_Controller {
	private static final Logger logger = LoggerFactory.getLogger(Rest_Controller.class);

	@Autowired
	AuditService AuditServices;

	@Autowired
	BRRS_Report_Mast_Rep BRRS_Report_Mast_Reps;
	
	@Autowired
	private MCBL_Detail_Rep mcblDetailRep;

	 
	@GetMapping("/checkReportDate")
    @ResponseBody
    public boolean checkReportDate(@RequestParam("report_date") String reportDate) {
		System.out.println("came to checking report date controller");
        return mcblDetailRep.checkIfReportDateExists(reportDate) > 0;
    }

	
	@PostMapping("createAcc")
	public String createAccessRoleEn(
	        @RequestParam("formmode") String formmode,
	        @RequestParam("FileType") String FileType,
	        @ModelAttribute GeneralMasterEntity generalMasterEntity,
	        Model md,
	        HttpServletRequest rq) {

	    String userid = (String) rq.getSession().getAttribute("USERID");
	    logger.info("Form mode: {}, FileType: {}, User: {}", formmode, FileType, userid);

	    String msg=null;

	    try {
	        switch (FileType) {
	            case "MCBLs":
	                msg = AuditServices.createAccount_MCBL(formmode, generalMasterEntity, userid);
	                break;

	            case "BLBFs":
	                msg = AuditServices.createAccount_BLBF(formmode, generalMasterEntity, userid);
	                break;

	            case "BDGFs":
	                msg = AuditServices.createAccount_BDGF(formmode, generalMasterEntity, userid);
	                break;

	            case "BFDBs":
	                msg = AuditServices.createAccount_BFDB(formmode, generalMasterEntity, userid);
	                break;

	            default:
	                msg = "Invalid FileType provided: " + FileType;
	                logger.warn("Unknown FileType received: {}", FileType);
	                break;
	        }

	    } catch (Exception e) {
	        logger.error("Error while creating account for FileType {}: {}", FileType, e.getMessage(), e);
	        msg = "Error while processing request: " + e.getMessage();
	    }

	    return msg;
	}

    
    @GetMapping("/getRptName")
    @ResponseBody
    public String getRptNameByCode(@RequestParam("rptCode") String rptCode) {
    	List<String> rptNames = BRRS_Report_Mast_Reps.getRptName(rptCode);
        if (rptNames != null && !rptNames.isEmpty()) {
            return rptNames.get(0); // return the first matching name
        }
        return "";
    }


    // to get the role if of the user from session
    @GetMapping("/getUserRole")
    public String getUserRole(HttpSession session) {
        String roleId = (String) session.getAttribute("ROLEID");
        return roleId != null ? roleId : "NO_ROLE";
    }
 


	
}
