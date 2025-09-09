package com.bornfire.brrs.services;

import java.math.BigDecimal;



import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.brrs.entities.AccessAndRoles;
import com.bornfire.brrs.entities.AccessandRolesRepository;



@Service
@ConfigurationProperties("output")
@Transactional
public class AccessAndRolesServices {

	@Autowired
	AccessandRolesRepository accessandrolesrepository;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	Environment env;
	
	@Autowired
	DataSource srcdataSource;
	
	@SuppressWarnings("unchecked")
	public List<AccessAndRoles> gettingaccessDetails(String roleid) {

		List<AccessAndRoles> list = (List<AccessAndRoles>) sessionFactory.getCurrentSession()
				.createQuery("from AccessAndRoles where role_id ='" + roleid + "'").getResultList();
		System.out.print("df" + list);
		return list;

	}

	public String addPARAMETER(AccessAndRoles alertparam, String formmode,
	        String adminValue, String BRRS_ReportsValue, String Archival, 
	        String auditUsValue, String finalString, String USERID) {

	    String msg = "";
	    System.out.println("formmode is "+formmode);
	    System.out.println(alertparam.getRoleId());
	    
	    if ("add".equalsIgnoreCase(formmode)) {

	        alertparam.setDelFlg("N");
	        alertparam.setModifyFlg("N");
	        alertparam.setEntityFlg("N");
	        alertparam.setAdmin(adminValue);
	        alertparam.setBrrsReports(BRRS_ReportsValue);
	        alertparam.setARCHIVALS(Archival);
	        alertparam.setAudit_us(auditUsValue);
	        alertparam.setEntryUser(USERID);
	        alertparam.setEntryTime(new Date());
	        alertparam.setMenulist(finalString);

	        accessandrolesrepository.save(alertparam);
	        msg = "Role Created Successfully";

	    } else if ("edit".equalsIgnoreCase(formmode)) {

	        if (alertparam.getRoleId() == null || alertparam.getRoleId().trim().isEmpty()) {
	            return "Role ID is missing. Cannot edit.";
	        }

	        Optional<AccessAndRoles> user = accessandrolesrepository.findById(alertparam.getRoleId());
	        if (!user.isPresent()) {
	            return "Role not found. Cannot edit.";
	        }

	        AccessAndRoles existing = user.get();

	        alertparam.setAdmin(adminValue);
	        alertparam.setBrrsReports(BRRS_ReportsValue);
	        alertparam.setARCHIVALS(Archival);
	        alertparam.setAudit_us(auditUsValue);
	        alertparam.setMenulist(finalString.isEmpty() ? existing.getMenulist() : finalString);

	        alertparam.setDelFlg("N");
	        alertparam.setModifyFlg("Y");
	        alertparam.setEntityFlg("N");
	        alertparam.setEntryUser(existing.getEntryUser());
	        alertparam.setEntryTime(existing.getEntryTime());
	        alertparam.setModifyUser(USERID);
	        alertparam.setModifyTime(new Date());

	        accessandrolesrepository.save(alertparam);
	        msg = "Role Edited Successfully";

	    }else if ("delete".equalsIgnoreCase(formmode)) {

	        if (alertparam.getRoleId() == null || alertparam.getRoleId().trim().isEmpty()) {
	            return "Role ID is missing. Cannot delete.";
	        }

	        String[] roleIds = alertparam.getRoleId().split(",");
	        for (String rid : roleIds) {
	            String trimmedId = rid.trim();
	            if (trimmedId.isEmpty()) continue;

	            System.out.println("Deleting Role ID -> " + trimmedId);

	            Optional<AccessAndRoles> user = accessandrolesrepository.findById(trimmedId);
	            if (user.isPresent()) {
	                accessandrolesrepository.deleteById(trimmedId);
	                System.out.println("Deleted successfully: " + trimmedId);
	            } else {
	                System.out.println("Role not found (already deleted?): " + trimmedId);
	                // Don’t return, just continue
	            }
	        }

	        msg = "Role Deleted Successfully";
	    }


	    else if ("verify".equalsIgnoreCase(formmode)) {

	        if (alertparam.getRoleId() == null || alertparam.getRoleId().trim().isEmpty()) {
	            return "Role ID is missing. Cannot verify.";
	        }

	        Optional<AccessAndRoles> user = accessandrolesrepository.findById(alertparam.getRoleId());
	        if (!user.isPresent()) {
	            return "Role not found. Cannot verify.";
	        }

	        AccessAndRoles accessRole = user.get();
	        accessRole.setDelFlg("N");
	        accessRole.setModifyFlg("N");
	        accessRole.setEntityFlg("Y");
	        accessRole.setAuthUser(USERID);
	        accessRole.setAuthTime(new Date());

	        accessandrolesrepository.save(accessRole);
	        msg = "Role Verified Successfully";
	    }

	    return msg;
	}


	public AccessAndRoles getRoleId(String id) {
		Session session = sessionFactory.getCurrentSession();
		Query<AccessAndRoles> query = session
				.createQuery(" from AccessAndRoles where role_id=?1 and NVL(DEL_FLG,1) <> 'Y'", AccessAndRoles.class);
		query.setParameter(1, id);
		List<AccessAndRoles> result = query.getResultList();
		if (!result.isEmpty()) {
			System.out.println(result.get(0).toString());
			return result.get(0);
		} else {
			return new AccessAndRoles();
		}

	}

	public AccessAndRoles getRoleMenu(String id) {
		Session session = sessionFactory.getCurrentSession();
		Query<AccessAndRoles> query = session.createQuery(" from AccessAndRoles where role_id=?1", AccessAndRoles.class);
		query.setParameter(1, id);
		List<AccessAndRoles> result = query.getResultList();
		if (!result.isEmpty()) {
			return result.get(0);
		} else {
			return new AccessAndRoles();
		}

	}

	public String deleteRole(String userid) {
		Session hs = sessionFactory.getCurrentSession();
		Query qr;
		qr = hs.createQuery("select count(*) from UserProfile where role_id= ?1");
		qr.setParameter(1, userid);
		long count = (long) qr.getSingleResult();
		String msg = "";
		if (count == 0) {
			Optional<AccessAndRoles> user = accessandrolesrepository.findById(userid);
			AccessAndRoles reg = user.get();
			reg.setDelFlg("Y");
			accessandrolesrepository.save(reg);
			msg = "Role Deleted Successfully";
		} else {
			msg = "This role has been assigned to an User.Cannot Delete ";
		}
		return msg;
	}
}