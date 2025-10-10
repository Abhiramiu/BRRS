package com.bornfire.brrs.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bornfire.brrs.entities.BRRS_Report_Mast_Rep;
import com.bornfire.brrs.entities.MCBL_Detail_Rep;
import com.bornfire.brrs.entities.MCBL_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Entity;
import com.bornfire.brrs.entities.MCBL_Rep;
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

	 @Autowired
	 private MCBL_Rep mcblRep;
	 
	@GetMapping("/checkReportDate")
    @ResponseBody
    public boolean checkReportDate(@RequestParam("report_date") String reportDate) {
		System.out.println("came to checking report date controller");
        return mcblDetailRep.checkIfReportDateExists(reportDate) > 0;
    }

	
    @PostMapping("createAcc")
    public String createAccessRoleEn(
            @RequestParam("formmode") String formmode,
            @ModelAttribute MCBL_Main_Entity mcblMainEntity,
            Model md,
            HttpServletRequest rq) {

        String userid = (String) rq.getSession().getAttribute("USERID");

        logger.info("Form mode: {}, User: {}", formmode, userid);

        String msg = AuditServices.createAccount(formmode, mcblMainEntity, userid);
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



	
}
