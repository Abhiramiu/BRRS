package com.bornfire.brrs.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bornfire.brrs.entities.AuditServicesEntity;
import com.bornfire.brrs.entities.AuditServicesRep;
import com.bornfire.brrs.services.AuditService;

@Controller
public class AuditController {
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	AuditServicesRep Service_audit_table_Reps;

	@RequestMapping(value = "User_Audit", method = RequestMethod.GET)
	public String userAudit(Model md, HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		System.out.println("The login userid is : " + userid);

		LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		System.out.println("The time is " + localDateTime);

		md.addAttribute("menu", "Audit");

		// Add list to the model
		md.addAttribute("auditlogs", auditService.getUserServices());

		return "User_Audit";
	}
	
	@RequestMapping(value = "Service_Audit", method = RequestMethod.GET)
	public String ServiceAudit(Model md, HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		System.out.println("The login userid is : " + userid);

		LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		System.out.println("The time is " + localDateTime);

		md.addAttribute("menu", "Audit");

		// Add list to the model
		md.addAttribute("AuditList", auditService.getAuditServices());

		return "Service_Audit";
	}

	@RequestMapping(value = "Modify_status", method = RequestMethod.GET)
	public String ModifyStatus(Model md, HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		System.out.println("The login userid is : " + userid);

		LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		System.out.println("The time is " + localDateTime);

		md.addAttribute("menu", "Audit");

		// Add list to the model
		md.addAttribute("AuditListModify", auditService.getModifyStatus());

		return "Modify_status";
	}
	
	@RequestMapping(value = "getchanges", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String fetchChanges(@RequestParam(required = false) String audit_ref_no) {
		String changeDetails = auditService.fetchChanges(audit_ref_no);
		System.out.println(changeDetails);
		return changeDetails;
	}


	@GetMapping("/details")
	@ResponseBody
	public ResponseEntity<List<Map<String, String>>> getModifyDetails(@RequestParam("id") Long id) {
		AuditServicesEntity entity = Service_audit_table_Reps.getdata(id);
		
		if (entity == null || entity.getModi_details() == null) {
			return ResponseEntity.ok(new ArrayList<>());
		}

		System.out.println(entity.getModi_details());
		String modify_details = entity.getModi_details(); 
		List<Map<String, String>> parsedList = new ArrayList<>();

		if (!modify_details.trim().isEmpty()) {
			String[] records = modify_details.split("\\|\\|\\|");
			
			for (String record : records) {
				if (record.trim().isEmpty()) continue;
				
				try {
					int oldValIdx = record.indexOf(": OldValue: ");
					int newValIdx = record.indexOf(", NewValue: ");
					
					String columnName = record.substring(0, oldValIdx).trim();
					String oldValue = record.substring(oldValIdx + 12, newValIdx).trim();
					String newValue = record.substring(newValIdx + 12).trim();
					
					Map<String, String> rowMap = new HashMap<>();
					rowMap.put("columnName", columnName);
					rowMap.put("oldValue", oldValue);
					rowMap.put("newValue", newValue);
					
					parsedList.add(rowMap);
				} catch (Exception e) {
					System.err.println("Could not parse record fragment: " + record);
				}
			}
		}
		
		return ResponseEntity.ok(parsedList);
	}

	@PostMapping("/approveAuditRecord")
	@ResponseBody
	public ResponseEntity<String> approveAuditRecord(@RequestParam("id") Long id) {
		try {
			auditService.verifyAndTransferReport(id);
			return ResponseEntity.ok("Report Verified and Data Transferred Successfully!");
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			// Replaced internalServerError() with status(HttpStatus.INTERNAL_SERVER_ERROR)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during verification.");
		}
	}
}