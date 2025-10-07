package com.bornfire.brrs.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bornfire.brrs.entities.AccessAndRoles;
import com.bornfire.brrs.entities.MCBL_Detail_Rep;
import com.bornfire.brrs.entities.MCBL_Main_Entity;
import com.bornfire.brrs.entities.ReportLineItemDTO;
import com.bornfire.brrs.services.AuditService;
import com.bornfire.brrs.services.BRRS_M_LA1_ReportService;

@RestController
public class Rest_Controller {
	private static final Logger logger = LoggerFactory.getLogger(Rest_Controller.class);

    @Autowired
    private BRRS_M_LA1_ReportService brrs_M_LA1_ReportService;
	@Autowired
	AuditService AuditServices;

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
            @ModelAttribute MCBL_Main_Entity mcblMainEntity,
            Model md,
            HttpServletRequest rq) {

        String userid = (String) rq.getSession().getAttribute("USERID");

        logger.info("Form mode: {}, User: {}", formmode, userid);

        String msg = AuditServices.createAccount(formmode, mcblMainEntity, userid);
        return msg;
    }
    

    @PostMapping("getReportDataByCode")
    public List<ReportLineItemDTO> getReportDataByCode(@RequestParam("reportCode") String reportCode) {
        System.out.println("Controller called with reportCode: " + reportCode);

        switch (reportCode) {
            case "M_LA1":
                return brrs_M_LA1_ReportService.getReportData(reportCode);
            // Add other report codes here
            default:
                System.out.println("No service found for reportCode: " + reportCode);
                return new ArrayList<>();
        }
    }
	
}
