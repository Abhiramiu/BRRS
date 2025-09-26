package com.bornfire.brrs.services;

import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bornfire.brrs.entities.AuditServicesEntity;
import com.bornfire.brrs.entities.AuditServicesRep;
import com.bornfire.brrs.entities.MCBL_Main_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Rep;

@Service
public class AuditService {

	@Autowired
	private AuditServicesRep auditServicesRep;
	@Autowired
	MCBL_Main_Rep MCBL_Main_Reps;

	public List<AuditServicesEntity> getUserServices() {
		System.out.println(auditServicesRep.getUserAudit());
		return auditServicesRep.getUserAudit();
	}

	public List<AuditServicesEntity> getAuditServices() {
		System.out.println(auditServicesRep.getServiceAudit());
		return auditServicesRep.getServiceAudit();	
	}

	public void createBusinessAudit(final String customerId, final String functionCode, final String screenName,
			final Map<String, String> changeDetails, final String tableName) {
		try {
			final UUID auditID = UUID.randomUUID();
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			String userId = null;
			String username = null;
			if (attr != null) {
				HttpServletRequest request = attr.getRequest();
				userId = (String) request.getSession().getAttribute("USERID");
				username = (String) request.getSession().getAttribute("USERNAME");
			}
			final Date currentDate = new Date();

			AuditServicesEntity audit = new AuditServicesEntity();
			audit.setAudit_ref_no(auditID.toString());
			audit.setAudit_date(currentDate);
			audit.setEntry_time(currentDate);
			audit.setEntry_user(userId);
			audit.setEntry_user_name(username);
			audit.setFunc_code(functionCode);
			audit.setAudit_table(tableName);
			audit.setAudit_screen(screenName);
			audit.setEvent_id(userId);
			audit.setEvent_name(username);

			if (changeDetails != null && !changeDetails.isEmpty()) {
				StringBuilder changes = new StringBuilder();
				changeDetails
						.forEach((field, value) -> changes.append(field).append(": ").append(value).append("||| "));
			
	            audit.setChange_details(changes.toString()); 
			}

			if ("VERIFY".equalsIgnoreCase(functionCode)) {
				audit.setAuth_user(userId);
				audit.setAuth_user_name(username);
				audit.setAuth_time(currentDate);
			}

			audit.setReport_id(customerId);

			System.out.println(audit);
			auditServicesRep.save(audit);

		} catch (Exception e) {
			System.err.println("Error creating business audit: " + e.getMessage());
			e.printStackTrace();
		}
	}
	

	public String fetchChanges(@RequestParam(required = false) String audit_ref_no) {

	return auditServicesRep.getchanges(audit_ref_no); 
	}
	
	
	 public String createAccount(String formmode, MCBL_Main_Entity mcblMainEntity, String userid) {
	        String msg = null;

	        try {
	            if ("add".equalsIgnoreCase(formmode)) {
	                // Generate next ID
	                String maxId = MCBL_Main_Reps.getMaxId();
	                int id = (maxId != null) ? Integer.parseInt(maxId) + 1 : 1;

	                mcblMainEntity.setId(String.valueOf(id));
	                mcblMainEntity.setCreate_user(userid);
	                mcblMainEntity.setCreate_date(new Date());
	                mcblMainEntity.setModify_flg("N");
	                mcblMainEntity.setDel_flg("N");

	                MCBL_Main_Reps.save(mcblMainEntity);
	                msg = "Account added successfully";

	            } else if ("edit".equalsIgnoreCase(formmode)) {
	                Optional<MCBL_Main_Entity> existingOpt = MCBL_Main_Reps.findById(mcblMainEntity.getId());
	                if (existingOpt.isPresent()) {
	                    MCBL_Main_Entity existing = existingOpt.get();

	                    // Update only editable fields
	                    existing.setGl_code(mcblMainEntity.getGl_code());
	                    existing.setGl_sub_code(mcblMainEntity.getGl_sub_code());
	                    existing.setHead_acc_no(mcblMainEntity.getHead_acc_no());
	                    existing.setCurrency(mcblMainEntity.getCurrency());

	                    existing.setModify_user(userid);
	                    existing.setModify_date(new Date());
	                    existing.setModify_flg("Y");
	                    existing.setDel_flg("N");

	                    MCBL_Main_Reps.save(existing);
	                    msg = "Account updated successfully";
	                } else {
	                    msg = "Error: Account not found for ID " + mcblMainEntity.getId();
	                }

	            } else if ("delete".equalsIgnoreCase(formmode)) {
	                Optional<MCBL_Main_Entity> existingOpt = MCBL_Main_Reps.findById(mcblMainEntity.getId());
	                if (existingOpt.isPresent()) {
	                    MCBL_Main_Entity existing = existingOpt.get();

	                    existing.setDel_user(userid);
	                    existing.setDel_date(new Date());
	                    existing.setDel_flg("Y");

	                    MCBL_Main_Reps.save(existing);
	                    msg = "Account deleted successfully";
	                } else {
	                    msg = "Error: Account not found for ID " + mcblMainEntity.getId();
	                }

	            } else {
	                msg = "Invalid formmode: " + formmode;
	            }
	        } catch (Exception e) {
	            msg = "Error: " + e.getMessage();
	        }

	        return msg;
	    }

}
