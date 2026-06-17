package com.bornfire.brrs.controllers;

import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/details")
    public ResponseEntity<List<Map<String, String>>> getModifyDetails(@RequestParam("id") String id) {
        
		Optional<AuditServicesEntity> entity=Service_audit_table_Reps.findById(id);
		System.out.println(entity.get().getModi_details());
        String modify_details = entity.get().getModi_details(); 
        List<Map<String, String>> parsedList = new ArrayList<>();

        if (modify_details != null && !modify_details.trim().isEmpty()) {
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
}
