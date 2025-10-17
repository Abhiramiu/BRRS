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
import com.bornfire.brrs.entities.BrrsGeneralMasterEntity;
import com.bornfire.brrs.entities.BrrsGeneralMasterRepo;
import com.bornfire.brrs.entities.MCBL_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Rep;
import com.bornfire.brrs.entities.MCBL_Rep;

@Service
public class AuditService {
	@Autowired
	BrrsGeneralMasterRepo BrrsGeneralMasterRepos;
	@Autowired
	private AuditServicesRep auditServicesRep;
	@Autowired
	MCBL_Main_Rep MCBL_Main_Reps;
	@Autowired
	MCBL_Rep MCBL_Reps;

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
	
	
	 public String createAccount(String formmode,String type, BrrsGeneralMasterEntity old, String userid) {
	        String msg = null;

	        try {
	        	if(type.equals("MCBL")) {
	             if ("edit".equalsIgnoreCase(formmode)) {
	                Optional<BrrsGeneralMasterEntity> existingOpt = BrrsGeneralMasterRepos.findById(old.getId());
	                if (existingOpt.isPresent()) {
	                	BrrsGeneralMasterEntity existing = existingOpt.get();

	                    // Update Master Table
	                    existing.setGl_code(old.getGl_code());
	                    existing.setGl_sub_code(old.getGl_sub_code());
	                    existing.setHead_acc_no(old.getHead_acc_no());
	                    existing.setCurrency(old.getCurrency());
	                    existing.setDescription(old.getDescription());
	                    existing.setDebit_balance(old.getDebit_balance());
	                    existing.setCredit_balance(old.getCredit_balance());
	                    existing.setDebit_equivalent(old.getDebit_equivalent());
	                    existing.setCredit_equivalent(old.getCredit_equivalent());
	                    existing.setReport_date(old.getReport_date());
	                    existing.setModify_user(userid);
	                    existing.setModify_date(new Date());
	                    existing.setModify_flg("Y");
	                    existing.setDel_flg("N");

	                    BrrsGeneralMasterRepos.save(existing);
	                    
	                    //Update MCBl Table
	                    
	                    Optional<MCBL_Entity> existingOpt1 = MCBL_Reps.findById(old.getId());
		                if (existingOpt1.isPresent()) {
		                	MCBL_Entity existing1 = existingOpt1.get();

		                    // Update Master Table
		                    existing1.setMcbl_gl_code(old.getGl_code());
		                    existing1.setMcbl_gl_sub_code(old.getGl_sub_code());
		                    existing1.setMcbl_head_acc_no(old.getHead_acc_no());
		                    existing1.setMcbl_currency(old.getCurrency());
		                    existing1.setMcbl_description(old.getDescription());
		                    existing1.setMcbl_debit_balance(old.getDebit_balance());
		                    existing1.setMcbl_credit_balance(old.getCredit_balance());
		                    existing1.setMcbl_debit_equivalent(old.getDebit_equivalent());
		                    existing1.setMcbl_credit_equivalent(old.getCredit_equivalent());
		                    existing1.setReport_date(old.getReport_date());

		                    MCBL_Reps.save(existing1);
		                    
		                    //Update MCBl Table
		                    msg = "MCBL updated successfully";
		                } else {
		                }
	                } else {
	                    msg = "Error: MCBL not found for ID " + old.getId();
	                }

	            } else if ("delete".equalsIgnoreCase(formmode)) {
	                Optional<BrrsGeneralMasterEntity> existingOpt = BrrsGeneralMasterRepos.findById(old.getId());
	                if (existingOpt.isPresent()) {
	                	BrrsGeneralMasterEntity existing = existingOpt.get();
	                    existing.setDel_flg("Y");
	                    BrrsGeneralMasterRepos.save(existing);
	                    msg = "MCBL deleted successfully";
	                } else {
	                    msg = "Error: MCBL not found for ID " + old.getId();
	                }
	                Optional<MCBL_Entity> existingOpt1 = MCBL_Reps.findById(old.getId());
	                if (existingOpt1.isPresent()) {
	                	MCBL_Entity existing = existingOpt1.get();
	                    MCBL_Reps.delete(existing);
	                    msg = "MCBL deleted successfully";
	                } else {
	                }

	            } else {
	                msg = "Invalid formmode: " + formmode;
	            }
	             
	        	}else if(type.equals("BDGF")) {
	        		
	        		
	        		
	        		
	        	}
	        } catch (Exception e) {
	            msg = "Error: " + e.getMessage();
	        }

	        return msg;
	    }

}
