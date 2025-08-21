package com.bornfire.brrs.controllers;

import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bornfire.brrs.services.AuditService;

@Controller
public class AuditController {
	
	@Autowired
	AuditService auditService;

	@RequestMapping(value = "User_Audit", method = RequestMethod.GET)
	public String userAudit(Model md, HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		System.out.println("The login userid is : " + userid);

		LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		System.out.println("The time is " + localDateTime);

		md.addAttribute("menu", "Audit");

		// Add both lists to the model
		md.addAttribute("auditlogs", auditService.getUserServices());
		//md.addAttribute("userAuditLevels", auditService.getUserAuditLevelList());

		return "User_Audit";
	}
	
	@RequestMapping(value = "Service_Audit", method = RequestMethod.GET)
	public String ServiceAudit(Model md, HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		System.out.println("The login userid is : " + userid);

		LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		System.out.println("The time is " + localDateTime);

		md.addAttribute("menu", "Audit");

		// Add both lists to the model
		md.addAttribute("AuditList", auditService.getAuditServices());
		//md.addAttribute("userAuditLevels", auditService.getUserAuditLevelList());

		return "Service_Audit";
	}
	
	@RequestMapping(value = "getchanges", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String fetchChanges(@RequestParam(required = false) String audit_ref_no) {

		// Fetch data from the database using the repository
		String changeDetails = auditService.fetchChanges(audit_ref_no); // Example of getting data
		System.out.println(changeDetails);
		// Process the change details to format as required

		return changeDetails; // Return the formatted changes
	}

}
