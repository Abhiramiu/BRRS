package com.bornfire.brrs.controllers;

import java.util.Date;
import java.util.List;


import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bornfire.brrs.entities.AccessAndRoles;
import com.bornfire.brrs.entities.AccessandRolesRepository;

import com.bornfire.brrs.entities.BankBranchMasterRepo;
import com.bornfire.brrs.entities.BRRSValidationsRepo;
import com.bornfire.brrs.entities.BankBranchMaster;
import com.bornfire.brrs.entities.RRReport;
import com.bornfire.brrs.entities.RRReportRepo;
import com.bornfire.brrs.entities.UserProfile;
import com.bornfire.brrs.entities.UserProfileRep;
import com.bornfire.brrs.services.AccessAndRolesServices;
import com.bornfire.brrs.services.BankBranchService;
import com.bornfire.brrs.services.LoginServices;
import com.bornfire.brrs.services.RegulatoryReportServices;
import com.bornfire.brrs.services.ReportServices;


@Controller
@ConfigurationProperties("default")
public class NavigationController {

	private static final Logger logger = LoggerFactory.getLogger(NavigationController.class);
	/*
	 * @PersistenceContext private EntityManager entityManager;
	 */

	UserProfileRep UserProfileReps;
	
	 @Autowired
	ReportServices reportServices;
	 
	@Autowired
	RegulatoryReportServices regulatoryreportservices;
	
	@Autowired
	BRRSValidationsRepo brrsvalidationsRepo;
	
	@Autowired
	RRReportRepo rrReportlist;

	@Autowired
	LoginServices loginServices;
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AccessAndRolesServices AccessRoleService;

	@Autowired
	AccessandRolesRepository accessandrolesrepository;


	@Autowired
	BankBranchMasterRepo bankBranchMasterRepo;
	
	@Autowired
    private BankBranchService bankBranchService;

	private String pagesize;

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	@RequestMapping(value = "Dashboard", method = { RequestMethod.GET, RequestMethod.POST })
	public String dashboard(Model md, HttpServletRequest req) {

		String domainid = (String) req.getSession().getAttribute("DOMAINID");
		String userid = (String) req.getSession().getAttribute("USERID");

		md.addAttribute("changepassword", loginServices.checkPasswordChangeReq(userid));
		md.addAttribute("checkpassExpiry", loginServices.checkpassexpirty(userid));
		md.addAttribute("checkAcctExpiry", loginServices.checkAcctexpirty(userid));
		int completed = 0;
		int uncompleted = 0;

		md.addAttribute("completed", completed);
		md.addAttribute("uncompleted", uncompleted);
		md.addAttribute("menu", "Dashboard");
		return "Dashboard";
	}

	@RequestMapping(value = "AccessandRoles", method = { RequestMethod.GET, RequestMethod.POST })
	public String IPSAccessandRoles(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		//System.out.println("role id is : " + roleId);
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("menu", "ACCESS AND ROLES");
			md.addAttribute("menuname", "ACCESS AND ROLES");
			md.addAttribute("formmode", "list");
			md.addAttribute("AccessandRoles", accessandrolesrepository.rulelist());
		} else if (formmode.equals("add")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - ADD");
			md.addAttribute("formmode", "add");
		} else if (formmode.equals("edit")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - EDIT");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));
		} else if (formmode.equals("view")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - INQUIRY");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));

		} else if (formmode.equals("verify")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - VERIFY");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));

		} else if (formmode.equals("delete")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - DELETE");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));
		}

		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return "AccessandRoles";
	}

	@RequestMapping(value = "createAccessRole", method = RequestMethod.POST)
	@ResponseBody
	public String createAccessRoleEn(@RequestParam("formmode") String formmode,
	        @RequestParam(value = "adminValue", required = false) String adminValue,
	        @RequestParam(value = "BRRS_ReportsValue", required = false) String BRRS_ReportsValue,
	        @RequestParam(value = "Archival", required = false) String Archival,
	        @RequestParam(value = "auditUsValue", required = false) String auditUsValue,
	        @RequestParam(value = "finalString", required = false) String finalString,
	        @ModelAttribute AccessAndRoles alertparam, Model md, HttpServletRequest rq) {

	    String userid = (String) rq.getSession().getAttribute("USERID");
	    String roleId = (String) rq.getSession().getAttribute("ROLEID");
	    md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

	    String msg = AccessRoleService.addPARAMETER(alertparam, formmode, adminValue, BRRS_ReportsValue,
	    		Archival, auditUsValue, finalString, userid);

	    return msg;
	}
	
	@GetMapping("/checkRoleExists")
	@ResponseBody
	public String checkRoleExists(@RequestParam("roleId") String roleId) {
	    boolean exists = accessandrolesrepository.findById(roleId).isPresent();
	    return exists ? "exists" : "not_exists";
	}
	

	@RequestMapping(value = "UserProfile", method = { RequestMethod.GET, RequestMethod.POST })
	public String userprofile(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

		int currentPage = page.orElse(0);
		int pageSize = size.orElse(Integer.parseInt(pagesize));

		String loginuserid = (String) req.getSession().getAttribute("USERID");
		String WORKCLASSAC = (String) req.getSession().getAttribute("WORKCLASS");
		String ROLEIDAC = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("RuleIDType", accessandrolesrepository.roleidtype());

		
		System.out.println("work class is : " + WORKCLASSAC);
		// Logging Navigation
		loginServices.SessionLogging("USERPROFILE", "M2", req.getSession().getId(), loginuserid, req.getRemoteAddr(),
				"ACTIVE");
		Session hs1 = sessionFactory.getCurrentSession();
		md.addAttribute("menu", "USER PROFILE"); // To highlight the menu

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", "list");// to set which form - valid values are "edit" , "add" & "list"
			md.addAttribute("WORKCLASSAC", WORKCLASSAC);
			md.addAttribute("ROLEIDAC", ROLEIDAC);
			md.addAttribute("loginuserid", loginuserid);

			Iterable<UserProfile> user = loginServices.getUsersList();

			md.addAttribute("userProfiles", user);

		} else if (formmode.equals("edit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", loginServices.getUser(userid));

		}else if (formmode.equals("view")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", loginServices.getUser(userid));

		}else if (formmode.equals("delete")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", loginServices.getUser(userid));

		} else if (formmode.equals("add")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", loginServices.getUser(""));
		} else if (formmode.equals("verify")) {
			md.addAttribute("WORKCLASSAC", WORKCLASSAC);
		    md.addAttribute("ROLEIDAC", ROLEIDAC);
	        md.addAttribute("formmode", formmode);
	        md.addAttribute("userProfile", loginServices.getUser(userid));
			

		} else {

			md.addAttribute("formmode", formmode);
			md.addAttribute("FinUserProfiles", loginServices.getFinUsersList());
			md.addAttribute("userProfile", loginServices.getUser(""));

		}
		

		return "Userprofile";
	}

	@GetMapping("/getRoleDetails")
	@ResponseBody
	public AccessAndRoles getRoleDetails(@RequestParam String roleId) {
		System.out.println("role id for fetching is : " + roleId);
		return accessandrolesrepository.findById(roleId).orElse(null);
	}

	@RequestMapping(value = "createUser", method = RequestMethod.POST)
	@ResponseBody
	public String createUser(@RequestParam("formmode") String formmode, @ModelAttribute UserProfile userprofile,
			Model md, HttpServletRequest rq) {

		String mob = (String) rq.getSession().getAttribute("MOBILENUMBER");
		String role = (String) rq.getSession().getAttribute("ROLEDESC");
		String userId = (String) rq.getSession().getAttribute("USERID");
		String userName = (String) rq.getSession().getAttribute("USERNAME");
		System.out.println("came to navigation controller ");
		String msg = loginServices.addUser(userprofile, formmode, userId, userName, mob, role);

		return msg;
	}

	@RequestMapping(value = "deleteuser", method = RequestMethod.POST)
	@ResponseBody
	public String deleteuser(@RequestParam("formmode") String userid, Model md, HttpServletRequest rq) {
		System.out.println("came to Delete user nav controller");
		String msg = loginServices.deleteuser(userid);

		return msg;

	}

	
	@RequestMapping(value = "verifyUser", method = RequestMethod.POST)
	@ResponseBody
	public String verifyUser(@ModelAttribute UserProfile userprofile, Model md, HttpServletRequest rq) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		String msg = loginServices.verifyUser(userprofile, userid);

		return msg;

	}


	@RequestMapping(value = "BRRSValidations", method = { RequestMethod.GET, RequestMethod.POST })
	public String BRRSValidations(Model md, @RequestParam(value = "rptcode", required = false) String rptcode,
			@RequestParam(value = "todate", required = false) String todate, HttpServletRequest req) {
		String roleId = (String) req.getSession().getAttribute("ROLEID");

		// md.addAttribute("reportvalue", "RBS Reports");
		// md.addAttribute("reportid", "RBSReports");

		String domainid = (String) req.getSession().getAttribute("DOMAINID");
		// md.addAttribute("reportsflag", "reportsflag");
		// md.addAttribute("menu", "RBS Data Maintenance");

		md.addAttribute("reportlist", brrsvalidationsRepo.getValidationList(rptcode));
		md.addAttribute("reportlist1", rrReportlist.getReportbyrptcode(rptcode));
		md.addAttribute("RoleId", roleId);

		// md.addAttribute("rpt_date", todate);
		return "BRRS/BRRSValidations";
	}
	

	@GetMapping("/checkDomainFlag")
	@ResponseBody
	public ResponseEntity<String> checkDomainFlag(@RequestParam String rptcode) {
		Optional<RRReport> report = rrReportlist.getParticularReport3(rptcode);

		if (report.isPresent()) {
			String domain = report.get().getDomain(); // Add getter in entity if not already
			if ("Y".equalsIgnoreCase(domain)) {
				return ResponseEntity.ok("ENABLED");
			} else {
				return ResponseEntity.ok("DISABLED");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT_FOUND");
		}
	}
	
	  @RequestMapping(value = "BRRS", method = { RequestMethod.GET,RequestMethod.POST })
	  public String brrs(Model md, HttpServletRequest req)
	  
	  {
	//String roleId = (String) req.getSession().getAttribute("ROLEID");
	  //String domainid = (String) req.getSession().getAttribute("DOMAINID");
		  
	  md.addAttribute("menu", "BRRS - BRRS Report");
	  System.out.println("nisha1");
	//System.out.println("count"+rrReportlist.getReportListbrrs().size());
	 md.addAttribute("reportlist", rrReportlist.getReportListbrrs());
	  
	  return "BRRS/RRReports";
	  
	  }
	  
	  
	 
	  
	  @RequestMapping(value = "MonthlyArchival", method = { RequestMethod.GET,RequestMethod.POST })
	  public String brrsArchival(Model md, HttpServletRequest req)
	  {
	//String roleId = (String) req.getSession().getAttribute("ROLEID");
	  //String domainid = (String) req.getSession().getAttribute("DOMAINID");
	  md.addAttribute("menu", "BRRS - BRRS ARCHIVAL");
	System.out.println("count"+rrReportlist.getReportListbrrs().size());
	  md.addAttribute("reportlist", rrReportlist.getReportListbrrs());
	  
	  return "BRRS/BRRSArchival";
	  
	  }
	 
	  
  
	  
	  @RequestMapping(value = "Archival", method = { RequestMethod.GET,RequestMethod.POST })
	  public String Archival(Model md,@RequestParam(value = "rptcode", required = false) String rptcode, HttpServletRequest req)
	  {
	//String roleId = (String) req.getSession().getAttribute("ROLEID");
	  //String domainid = (String) req.getSession().getAttribute("DOMAINID");
		  RRReport data=rrReportlist.getReportbyrptcode(rptcode);
		  md.addAttribute("reportlist", data);
		  md.addAttribute("menu", data.getRptDescription());
		  md.addAttribute("domain", data.getDomainId());
		  md.addAttribute("rptcode", data.getRptCode());
		  List<Object> Archivaldata=regulatoryreportservices.getArchival(rptcode);
		  md.addAttribute("Archivaldata",Archivaldata);
		  md.addAttribute("reportlist", rrReportlist.getReportListbrrs());
	  
	  return "BRRS/BRRSArchivalform";
	  
	  }
	   	    
	 
		@RequestMapping(value = "ReportMaster", method = RequestMethod.GET)
		public String reportMaster(Model md, HttpServletRequest req) {

			String userid = (String) req.getSession().getAttribute("USERID");
			// Logging Navigation
			loginServices.SessionLogging("REPORTMAST", "M5", req.getSession().getId(), userid, req.getRemoteAddr(),
					"ACTIVE");

			md.addAttribute("menu", "ReportMaster");
			md.addAttribute("reportList", reportServices.getReportsMaster());
			return "BRRS/ReportMaster";
			

		}
		
		@RequestMapping(value = "updateValidity", method = RequestMethod.POST)
		@ResponseBody
		public String updateValidity(@RequestParam("rptCode") String rptCode, String valid, HttpServletRequest rq) {

			String userid = (String) rq.getSession().getAttribute("USERID");
			System.out.println("came to controller");
			return reportServices.updateValidity(rptCode, valid, userid);

		}
		
	@RequestMapping(value = "BranchMaster", method = RequestMethod.GET)
	 public String branchMaster(@RequestParam(required = false) String formmode,
		                               @RequestParam(required = false) String solId,
		                               Model md, HttpServletRequest req) {

		        String userid = (String) req.getSession().getAttribute("USERID");
		        loginServices.SessionLogging("BRANCHMAST", "M3", req.getSession().getId(),
		                userid, req.getRemoteAddr(), "ACTIVE");

		        if (formmode == null || formmode.equals("list")) {
		            md.addAttribute("formmode", "list");
		            md.addAttribute("branchList", bankBranchMasterRepo.GetAllData());
		            md.addAttribute("menu", "BANK AND BRANCH MASTER");

		        } else if (formmode.equals("add")) {
		            md.addAttribute("formmode", "add");
		            md.addAttribute("branch", new BankBranchMaster());

		        } else if (formmode.equals("edit") || formmode.equals("view") || formmode.equals("delete")) {
		            BankBranchMaster branch = bankBranchMasterRepo.findById(solId).orElse(null);
		            md.addAttribute("branch", branch);
		            md.addAttribute("formmode", formmode);
		        }

		        return "BRRS/BankBranchMaster";
		    }

	@PostMapping("/BranchMaster/save")
	public String saveBranch(@ModelAttribute BankBranchMaster branch, HttpServletRequest req) {
		        String userId = (String) req.getSession().getAttribute("USERID");
		        bankBranchService.saveBranch(branch, userId);
		        return "redirect:/BranchMaster?formmode=list";
		    }

	@RequestMapping(value = "BranchMaster/delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteBranch(@RequestParam("solId") String solId) {
	    try {
	        bankBranchService.softDeleteBranch(solId);
	        return "SUCCESS";
	    } catch(Exception e) {
	        return "ERROR: " + e.getMessage();
	    }
	}



		
	  @RequestMapping(value = "fort", method = { RequestMethod.GET,RequestMethod.POST })
	  public String fort(Model md, HttpServletRequest req)
	  {
	//String roleId = (String) req.getSession().getAttribute("ROLEID");
	  //String domainid = (String) req.getSession().getAttribute("DOMAINID");
	 md.addAttribute("menu", "Fortnightly - BRF Report");
	System.out.println("count"+rrReportlist.getReportListFORTNIGHTLY().size());
	  md.addAttribute("reportlist", rrReportlist.getReportListFORTNIGHTLY());
	  
	  return "BRF/RRReports";
	  
	  }
	@RequestMapping(value = "BRFValidations", method = { RequestMethod.GET, RequestMethod.POST })
	public String BRFValidations(Model md, @RequestParam(value = "rptcode", required = false) String rptcode,
			@RequestParam(value = "todate", required = false) String todate, HttpServletRequest req) {
		String roleId = (String) req.getSession().getAttribute("ROLEID");

		// md.addAttribute("reportvalue", "RBS Reports");
		// md.addAttribute("reportid", "RBSReports");

		String domainid = (String) req.getSession().getAttribute("DOMAINID");
		// md.addAttribute("reportsflag", "reportsflag");
		// md.addAttribute("menu", "RBS Data Maintenance");

		md.addAttribute("reportlist", brrsvalidationsRepo.getValidationList(rptcode));
		md.addAttribute("reportlist1", rrReportlist.getReportbyrptcode(rptcode));
		md.addAttribute("RoleId", roleId);

		// md.addAttribute("rpt_date", todate);
		return "BRF/BRFValidations";
	}
	@RequestMapping(value = "Quarterly-2", method = { RequestMethod.GET,RequestMethod.POST })
	  public String Quarterly2(Model md, HttpServletRequest req)
	  {
	//String roleId = (String) req.getSession().getAttribute("ROLEID");
	  //String domainid = (String) req.getSession().getAttribute("DOMAINID");
	  md.addAttribute("menu", "Quarterly 2 - BRF Report");
	System.out.println("count"+rrReportlist.getReportListQuarterly2().size());
	  md.addAttribute("reportlist", rrReportlist.getReportListQuarterly2());
	  
	  return "BRF/RRReports";
	  
	  }
	
	

}
