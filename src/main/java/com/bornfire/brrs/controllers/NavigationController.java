package com.bornfire.brrs.controllers;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.entities.AccessAndRoles;
import com.bornfire.brrs.entities.AccessandRolesRepository;

import com.bornfire.brrs.entities.BankBranchMasterRepo;
import com.bornfire.brrs.entities.MCBL_Main_Rep;
import com.bornfire.brrs.entities.BRRSValidationsRepo;
import com.bornfire.brrs.entities.BankBranchMaster;
import com.bornfire.brrs.entities.RRReport;
import com.bornfire.brrs.entities.RRReportRepo;
import com.bornfire.brrs.entities.UserProfile;
import com.bornfire.brrs.entities.UserProfileRep;
import com.bornfire.brrs.services.AccessAndRolesServices;
import com.bornfire.brrs.services.BDGF_Services;
import com.bornfire.brrs.services.BankBranchService;
import com.bornfire.brrs.services.LoginServices;
import com.bornfire.brrs.services.MCBL_Services;
import com.bornfire.brrs.services.RegulatoryReportServices;
import com.bornfire.brrs.services.ReportServices;


@Controller
@ConfigurationProperties("default")
public class NavigationController {

	private static final Logger logger = LoggerFactory.getLogger(NavigationController.class);
	/*
	 * @PersistenceContext private EntityManager entityManager;
	 */
	@Autowired
	MCBL_Main_Rep MCBL_Main_Reps;
	@Autowired
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
	
	
	@RequestMapping(value = "MCBL", method = { RequestMethod.GET, RequestMethod.POST })
	public String MCBL(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String tranid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req,
			@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		md.addAttribute("activeMenu", "Reports");
		md.addAttribute("activePage", "CentralBank");
		 String USERID = (String) req.getSession().getAttribute("USERID");
	   md.addAttribute("USERID", USERID);
		logger.info("==> Entered MCBL controller || Formmode: {}", formmode);

		LocalDate today = LocalDate.now();
		Date defaultDate = java.sql.Date.valueOf(today);

		
		
		try {
			if (formmode == null || formmode.equals("list")) {
				//List<INR_Reporting_Branch_Entity> customerList = new ArrayList<>();
				String currentDateString = null;
				if (date == null) {
					// If no date provided → use today's date
					//customerList = INR_Reporting_Branch_Reps.Getcurrentdaydetail(defaultDate);
					//logger.info("Fetched {} records for default date: {}", customerList.size(), defaultDate);
					currentDateString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				} else {
					// Convert LocalDate param → SQL Date
					Date sqlDate = java.sql.Date.valueOf(date);
					//customerList = INR_Reporting_Branch_Reps.Getcurrentdaydetail(sqlDate);
					//logger.info("Fetched {} records for provided date: {}", customerList.size(), sqlDate);

					currentDateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				}
				md.addAttribute("currentdate", currentDateString);
				md.addAttribute("menu", "MCBL - List");
				//md.addAttribute("customersplratedetail", customerList);
				md.addAttribute("currentdate", currentDateString);
				md.addAttribute("formmode", "add");
			} else if (formmode.equals("add")) {
				md.addAttribute("menu", "MCBL - Add");
				md.addAttribute("formmode", "add");
			}  

		} catch (Exception e) {
			logger.error("Error in  MCBL controller: {}", e.getMessage(), e);
			md.addAttribute("errorMessage", "Error loading MCBL page. Please contact administrator.");
		}

		logger.info("<== Exiting MCBL controller");
		return "MCBL";
	}



	@Autowired
	MCBL_Services MCBL_Servicess;
	
	
	@PostMapping("addmcbl")
	@ResponseBody
	public String addmcbl(@ModelAttribute MultipartFile file,
	                                    Model md,String reportDate,
	                                    HttpServletRequest rq ) {
	    logger.info("==> Entered MCBL method");
	    String userid = (String) rq.getSession().getAttribute("USERID");
	    String username = (String) rq.getSession().getAttribute("USERNAME");
	    try {
	        String msg = MCBL_Servicess.addMCBL( file, userid, username,reportDate);
	        logger.info("MCBL result: {}", msg);
	        return msg;
	    } catch (Exception e) {
	        logger.error("Error occurred while Add MCBL: {}", e.getMessage(), e);
	        return "Error Occurred. Please contact Administrator.";
	    }
	}

	
	//BDGF
	@RequestMapping(value = "BDGF", method = { RequestMethod.GET, RequestMethod.POST })
	public String BDGF(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String tranid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req,
			@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		md.addAttribute("activeMenu", "Reports");
		md.addAttribute("activePage", "CentralBank");
		 String USERID = (String) req.getSession().getAttribute("USERID");
	   md.addAttribute("USERID", USERID);
		logger.info("==> Entered BDGF controller || Formmode: {}", formmode);

		LocalDate today = LocalDate.now();
		Date defaultDate = java.sql.Date.valueOf(today);

		try {
			if (formmode == null || formmode.equals("list")) {
				//List<INR_Reporting_Branch_Entity> customerList = new ArrayList<>();
				String currentDateString = null;
				if (date == null) {
					// If no date provided → use today's date
					//customerList = INR_Reporting_Branch_Reps.Getcurrentdaydetail(defaultDate);
					//logger.info("Fetched {} records for default date: {}", customerList.size(), defaultDate);
					currentDateString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				} else {
					// Convert LocalDate param → SQL Date
					Date sqlDate = java.sql.Date.valueOf(date);
					//customerList = INR_Reporting_Branch_Reps.Getcurrentdaydetail(sqlDate);
					//logger.info("Fetched {} records for provided date: {}", customerList.size(), sqlDate);

					currentDateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				}
				md.addAttribute("currentdate", currentDateString);
				md.addAttribute("menu", "Blank Deposit General Format - List");
				//md.addAttribute("customersplratedetail", customerList);
				md.addAttribute("currentdate", currentDateString);
				md.addAttribute("formmode", "list");
			} else if (formmode.equals("add")) {
				md.addAttribute("menu", "Blank Deposit General Format - Add");
				md.addAttribute("formmode", "add");
			}  

		} catch (Exception e) {
			logger.error("Error in  BDGF controller: {}", e.getMessage(), e);
			md.addAttribute("errorMessage", "Error loading BDGF page. Please contact administrator.");
		}

		logger.info("<== Exiting BDGF controller");
		return "BDGF";
	}




	@Autowired
	BDGF_Services BDGF_Servicess;
	
	
	  @PostMapping("addBDGF")
	  
	  @ResponseBody public String addBDGF(@ModelAttribute MultipartFile file, Model
	  md,String reportDate, HttpServletRequest rq ) {
	  logger.info("==> Entered BDGF method"); String userid = (String)
	  rq.getSession().getAttribute("USERID"); String username = (String)
	  rq.getSession().getAttribute("USERNAME"); try { String msg =
	  BDGF_Servicess.addBDGF( file, userid, username,reportDate);
	  logger.info("BDGF result: {}", msg); return msg; } catch (Exception e) {
	  logger.error("Error occurred while Add BDGF: {}", e.getMessage(), e); return
	  "Error Occurred. Please contact Administrator."; } }
	 
	
	@GetMapping("/download-template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        List<String> headers = Arrays.asList(
                "SOL ID","S No","A/C No","Customer ID","Customer Name","Open Date",
                "Amount Deposited","Currency","Period","Rate of Interest","100",
                "BAL EQUI TO BWP","Outstanding Balance","Oustndng Bal UGX","Maturity Date",
                "Maturity Amount","Scheme","Cr Pref Int Rate","SEGMENT","REFERENCE DATE",
                "DIFFERENCE","DAYS","PERIOD","EFFECTIVE INTEREST RATE"
        );

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BDGF_Template");

        // Create header row
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }

        // Freeze header row
        sheet.createFreezePane(0, 1);

        // Write to byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BDGF_Template.xlsx");
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity
                .ok()
                .headers(headersResponse)
                .body(out.toByteArray());
    
}
    

@RequestMapping(value = "SourceDataMap", method = { RequestMethod.GET, RequestMethod.POST })
	public String SourceDataMap(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String id, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("menu", "Source Data Mapping");
			md.addAttribute("menuname", "Source Data Mapping");
			md.addAttribute("formmode", "list");
			md.addAttribute("MCBL_List", MCBL_Main_Reps.getall());
		} else if (formmode.equals("add")) {
			md.addAttribute("menuname", "Source Data Mapping - Add");
			md.addAttribute("formmode", "add");
		} else if (formmode.equals("edit")) {
			md.addAttribute("menuname", "Source Data Mapping - Edit");
			md.addAttribute("formmode", formmode);
			md.addAttribute("MCBL_List", MCBL_Main_Reps.getbyid(id));
		} else if (formmode.equals("view")) {
			md.addAttribute("menuname", "Source Data Mapping - Inquiry");
			md.addAttribute("formmode", formmode);
			md.addAttribute("MCBL_List", MCBL_Main_Reps.getbyid(id));

		}else if (formmode.equals("delete")) {
			md.addAttribute("menuname", "Source Data Mapping - Delete");
			md.addAttribute("formmode", formmode);
			md.addAttribute("MCBL_List", MCBL_Main_Reps.getbyid(id));
		}


		return "Source_Data_Mapping";
	}

}
