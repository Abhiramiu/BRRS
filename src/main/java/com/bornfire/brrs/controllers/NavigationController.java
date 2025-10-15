package com.bornfire.brrs.controllers;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
import com.bornfire.brrs.entities.BDGF_Entity;
import com.bornfire.brrs.entities.BDGF_Rep;
import com.bornfire.brrs.entities.BFDB_Entity;
import com.bornfire.brrs.entities.BFDB_Rep;
import com.bornfire.brrs.entities.BLBF_Entity;
import com.bornfire.brrs.entities.BLBF_Rep;
import com.bornfire.brrs.entities.BRRSValidationsRepo;
import com.bornfire.brrs.entities.BRRS_Report_Mast_Rep;
import com.bornfire.brrs.entities.BankBranchMaster;
import com.bornfire.brrs.entities.BankBranchMasterRepo;
import com.bornfire.brrs.entities.BrrsGeneralMasterEntity;
import com.bornfire.brrs.entities.BrrsGeneralMasterRepo;
import com.bornfire.brrs.entities.MCBL_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Rep;
import com.bornfire.brrs.entities.MCBL_Rep;
import com.bornfire.brrs.entities.RRReport;
import com.bornfire.brrs.entities.RRReportRepo;
import com.bornfire.brrs.entities.UserProfile;
import com.bornfire.brrs.entities.UserProfileRep;
import com.bornfire.brrs.services.AccessAndRolesServices;
import com.bornfire.brrs.services.BDGF_Services;
import com.bornfire.brrs.services.BFDB_Services;
import com.bornfire.brrs.services.BLBF_Services;
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
	BRRS_Report_Mast_Rep BRRS_Report_Mast_Reps;
	@Autowired
	MCBL_Main_Rep MCBL_Main_Reps;
	@Autowired
	UserProfileRep UserProfileReps;


	@Autowired
	BDGF_Services BDGF_Servicess;
	

	@Autowired
	BDGF_Rep bdgfRep;

	@Autowired
	BFDB_Rep bfdbRep;

	@Autowired
	BLBF_Rep blbfRep;


	@Autowired
	BFDB_Services BFDB_Servicess;

	@Autowired
	BrrsGeneralMasterRepo BrrsGeneralMasterRepos;
	 @Autowired
	  MCBL_Rep mcblRep;
	 
	 
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
		// System.out.println("role id is : " + roleId);
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

		String msg = AccessRoleService.addPARAMETER(alertparam, formmode, adminValue, BRRS_ReportsValue, Archival,
				auditUsValue, finalString, userid);

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

		} else if (formmode.equals("view")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", loginServices.getUser(userid));

		} else if (formmode.equals("delete")) {

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

	@RequestMapping(value = "BRRS", method = { RequestMethod.GET, RequestMethod.POST })
	public String brrs(Model md, HttpServletRequest req)

	{
		// String roleId = (String) req.getSession().getAttribute("ROLEID");
		// String domainid = (String) req.getSession().getAttribute("DOMAINID");

		md.addAttribute("menu", " Basel Returns- Monthly Reports");
		System.out.println("nisha1");
		// System.out.println("count"+rrReportlist.getReportListbrrs().size());
		md.addAttribute("reportlist", rrReportlist.getReportListbrrs());

		return "BRRS/RRReports";

	}

	@RequestMapping(value = "Quarterly", method = { RequestMethod.GET, RequestMethod.POST })
	public String Quarterly(Model md, HttpServletRequest req) {
		// String roleId = (String) req.getSession().getAttribute("ROLEID");
		// String domainid = (String) req.getSession().getAttribute("DOMAINID");
		md.addAttribute("menu", "Basel Returns- Quarterly Reports");
		System.out.println("count" + rrReportlist.getReportListbrrsQ().size());
		md.addAttribute("reportlist", rrReportlist.getReportListbrrsQ());

		return "BRRS/RRReports";

	}

	@RequestMapping(value = "MonthlyArchival", method = { RequestMethod.GET, RequestMethod.POST })
	public String brrsArchival(Model md, HttpServletRequest req) {
		// String roleId = (String) req.getSession().getAttribute("ROLEID");
		// String domainid = (String) req.getSession().getAttribute("DOMAINID");
		md.addAttribute("menu", "BRRS - BRRS ARCHIVAL");
		System.out.println("count" + rrReportlist.getReportListbrrs().size());
		md.addAttribute("reportlist", rrReportlist.getReportListbrrs());

		return "BRRS/BRRSArchival";

	}

	@RequestMapping(value = "Archival", method = { RequestMethod.GET, RequestMethod.POST })
	public String Archival(Model md, @RequestParam(value = "rptcode", required = false) String rptcode,
			HttpServletRequest req) {
		// String roleId = (String) req.getSession().getAttribute("ROLEID");
		// String domainid = (String) req.getSession().getAttribute("DOMAINID");
		RRReport data = rrReportlist.getReportbyrptcode(rptcode);
		md.addAttribute("reportlist", data);
		md.addAttribute("menu", data.getRptDescription());
		md.addAttribute("domain", data.getDomainId());
		md.addAttribute("rptcode", data.getRptCode());
		List<Object> Archivaldata = regulatoryreportservices.getArchival(rptcode);
		md.addAttribute("Archivaldata", Archivaldata);
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
			@RequestParam(required = false) String solId, Model md, HttpServletRequest req) {

		String userid = (String) req.getSession().getAttribute("USERID");
		loginServices.SessionLogging("BRANCHMAST", "M3", req.getSession().getId(), userid, req.getRemoteAddr(),
				"ACTIVE");

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
		} catch (Exception e) {
			return "ERROR: " + e.getMessage();
		}
	}

	@RequestMapping(value = "fort", method = { RequestMethod.GET, RequestMethod.POST })
	public String fort(Model md, HttpServletRequest req) {
		// String roleId = (String) req.getSession().getAttribute("ROLEID");
		// String domainid = (String) req.getSession().getAttribute("DOMAINID");
		md.addAttribute("menu", "Fortnightly - BRF Report");
		System.out.println("count" + rrReportlist.getReportListFORTNIGHTLY().size());
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

	@RequestMapping(value = "SourceDataMap", method = { RequestMethod.GET, RequestMethod.POST })

	/*
	 * public String SourceDataMap(@RequestParam(required = false) String formmode,
	 * 
	 * @RequestParam(required = false) String id,
	 * 
	 * @RequestParam(value = "page", required = false, defaultValue = "0") int page,
	 * 
	 * @RequestParam(value = "size", required = false, defaultValue = "100") int
	 * size, Model md) {
	 */
	public String SourceDataMap(
	        @RequestParam(required = false) String formmode,
	        @RequestParam(required = false) String id,
	        @RequestParam(required = false) String fileType,
	        @RequestParam(required = false) String reportDate,
	        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
	        @RequestParam(value = "size", required = false, defaultValue = "100") int size,
	        Model md) {

	    md.addAttribute("menu", "General Master Table");
	    md.addAttribute("menuname", "General Master Table");
	    md.addAttribute("formmode", formmode != null ? formmode : "list");

	    if (formmode == null || formmode.equals("list")) {
	        int offset = page * size;
	        List<BrrsGeneralMasterEntity> list;

	        if ((fileType != null && !fileType.isEmpty()) && (reportDate != null && !reportDate.isEmpty())) {
	            list = BrrsGeneralMasterRepos.findByFileTypeAndReportDate(fileType, reportDate, offset, size);
	        } else if (fileType != null && !fileType.isEmpty()) {
	            list = BrrsGeneralMasterRepos.findByFileType(fileType, offset, size);
	        } else if (reportDate != null && !reportDate.isEmpty()) {
	            list = BrrsGeneralMasterRepos.findByReportDate(reportDate, offset, size);
	        } else {
	            list = BrrsGeneralMasterRepos.getdatabydateList(offset, size);
	        }

	        int totalRecords;
	        if ((fileType != null && !fileType.isEmpty()) && (reportDate != null && !reportDate.isEmpty())) {
	            totalRecords = BrrsGeneralMasterRepos.countByFileTypeAndReportDate(fileType, reportDate);
	        } else if (fileType != null && !fileType.isEmpty()) {
	            totalRecords = BrrsGeneralMasterRepos.countByFileType(fileType);
	        } else if (reportDate != null && !reportDate.isEmpty()) {
	            totalRecords = BrrsGeneralMasterRepos.countByReportDate(reportDate);
	        } else {
	            totalRecords = BrrsGeneralMasterRepos.countAll();
	        }

	        int totalPages = (int) Math.ceil((double) totalRecords / size);

	        md.addAttribute("list", list);
	        md.addAttribute("pagination", "YES");
	        md.addAttribute("currentPage", page);
	        md.addAttribute("totalPages", totalPages);
	        md.addAttribute("fileType", fileType);
	        md.addAttribute("reportDate", reportDate);

	    } else if (formmode.equals("add")) {
            md.addAttribute("menuname", "MCBL Main- Add");
            md.addAttribute("formmode", "add");

        } else { // edit/view/delete
	        BrrsGeneralMasterEntity entity = BrrsGeneralMasterRepos.findById(id).orElse(null);
	        md.addAttribute("list", entity);
	        md.addAttribute("formmode", formmode);
	        md.addAttribute("menuname", "General Master Table - " + formmode.toUpperCase());
	    }

	    return "Source_Data_Mapping";
	}

    
        @RequestMapping(value = "ReferCodeMast", method = { RequestMethod.GET, RequestMethod.POST })
        public String ReferCodeMast(
                @RequestParam(required = false) String formmode,
                @RequestParam(required = false) String id,
                @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                @RequestParam(value = "size", required = false, defaultValue = "100") int size,
                Model md) {

                md.addAttribute("menu", "Reference Code Master");
                md.addAttribute("menuname", "Referance Code Master");
                md.addAttribute("formmode", "list");

                int offset = page * size;
                List<MCBL_Main_Entity> lists = MCBL_Main_Reps.getdatabydateList(offset, size);

                // ✅ Declare totalRecords here
                int totalRecords = MCBL_Main_Reps.countAll();
                int totalPages = (int) Math.ceil((double) totalRecords / size);

                List<String> RptCodes = BRRS_Report_Mast_Reps.getRptCode();
                

                md.addAttribute("RptCodes", RptCodes);
               
                
                md.addAttribute("MCBL_List", lists);
                md.addAttribute("pagination", "YES");
                md.addAttribute("currentPage", page);
                md.addAttribute("totalPages", totalPages);
            
            

            return "Reference_Code_Master.html";
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
    			if (formmode == null || formmode.equals("add")) {
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
    			} else if (formmode.equals("list")) {
    				List<Date> reportDates = mcblRep.findDistinctReportDates();
    			    md.addAttribute("reportDates", reportDates);
    				md.addAttribute("menu", "File Upload - List");
    				md.addAttribute("formmode", "list");
    			}  

    		} catch (Exception e) {
    			logger.error("Error in  MCBL controller: {}", e.getMessage(), e);
    			md.addAttribute("errorMessage", "Error loading MCBL page. Please contact administrator.");
    		}

    		logger.info("<== Exiting MCBL controller");
    		return "MCBL";
    	}



    	//Getting Details by report date for view and download for MCBL IN FILE UPLOAD
    	@GetMapping("/fetchMCBLRecords")
    	public String fetchMCBLRecords(@RequestParam String reportDate, Model md) {
    	    System.out.println("Came to controller with report date: " + reportDate);

    	    // Fetch records from repository
    	    List<MCBL_Entity> lists = mcblRep.findRecordsByReportDate(reportDate);

    	    // Add data to model
    	    md.addAttribute("MCBL_List", lists);
    	    md.addAttribute("selectedReportDate", reportDate);
    	    
    	    List<Date> reportDates = mcblRep.findDistinctReportDates();
		    md.addAttribute("reportDates", reportDates);
		    
		    
    	    md.addAttribute("formmode", "list");
    	    // Return the same view (page name)
    	    return "MCBL"; // change this to your actual HTML/Thymeleaf page name
    	}


    	@Autowired
    	MCBL_Services MCBL_Servicess;
    	
    	
   /* 	@PostMapping("addmcbl")
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

    	
   /* 	//BDGF
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



    	
    /*	@PostMapping("addBDGF")
    	@ResponseBody
    	public String addBDGF(@ModelAttribute MultipartFile file, Model md, HttpServletRequest rq) {
    	    logger.info("==> Entered BDGF method");

    	    String userid = (String) rq.getSession().getAttribute("USERID");
    	    String username = (String) rq.getSession().getAttribute("USERNAME");

    	    try {
    	        String msg = BDGF_Servicess.addBDGF(file, userid, username);
    	        logger.info("BDGF result: {}", msg);
    	        return msg;
    	    } catch (Exception e) {
    	        logger.error("Error occurred while Add BDGF: {}", e.getMessage(), e);
    	        return "Error Occurred. Please contact Administrator.";
    	    }
    	}
    	
    	*/

	//BFDB
/*
@RequestMapping(value = "BFDB", method = { RequestMethod.GET, RequestMethod.POST })
public String BFDB(@RequestParam(required = false) String formmode,
		@RequestParam(required = false) String tranid, @RequestParam(required = false) Optional<Integer> page,
		@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req,
		@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
	md.addAttribute("activeMenu", "Reports");
	md.addAttribute("activePage", "CentralBank");
	 String USERID = (String) req.getSession().getAttribute("USERID");
   md.addAttribute("USERID", USERID);
	logger.info("==> Entered BFDB controller || Formmode: {}", formmode);

	LocalDate today = LocalDate.now();

	try {
		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("menu", "MCBL Main");
			md.addAttribute("menuname", "MCBL Main");
			md.addAttribute("formmode", "list");

			int offset = page * size;
			List<MCBL_Main_Entity> lists = MCBL_Main_Reps.getdatabydateList(offset, size);

			// ✅ Declare totalRecords here
			int totalRecords = MCBL_Main_Reps.countAll();
			int totalPages = (int) Math.ceil((double) totalRecords / size);

			md.addAttribute("MCBL_List", lists);
			md.addAttribute("pagination", "YES");
			md.addAttribute("currentPage", page);
			md.addAttribute("totalPages", totalPages);
		} else if (formmode.equals("add")) {
			md.addAttribute("menuname", "MCBL Main- Add");
			md.addAttribute("formmode", "add");

		} else if (formmode.equals("edit")) {
			md.addAttribute("menuname", "MCBL Main- Edit");
			md.addAttribute("formmode", "edit");
			md.addAttribute("MCBL_List", MCBL_Main_Reps.findById(id).orElse(null));

		} else if (formmode.equals("view")) {
			md.addAttribute("menuname", "MCBL Main- Inquiry");
			md.addAttribute("formmode", "view");
			md.addAttribute("MCBL_List", MCBL_Main_Reps.findById(id).orElse(null));

		} else if (formmode.equals("delete")) {
			md.addAttribute("menuname", "MCBL Main- Delete");
			md.addAttribute("formmode", "delete");
			md.addAttribute("MCBL_List", MCBL_Main_Reps.findById(id).orElse(null));
		}

		return "Source_Data_Mapping";
	}*/

	/*@RequestMapping(value = "ReferCodeMast", method = { RequestMethod.GET, RequestMethod.POST })
	public String ReferCodeMast(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String id,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "100") int size, Model md) {

		md.addAttribute("menu", "Reference Code Master");
		md.addAttribute("menuname", "Referance Code Master");
		md.addAttribute("formmode", "list");

		int offset = page * size;
		List<MCBL_Main_Entity> lists = MCBL_Main_Reps.getdatabydateList(offset, size);

		// ✅ Declare totalRecords here
		int totalRecords = MCBL_Main_Reps.countAll();
		int totalPages = (int) Math.ceil((double) totalRecords / size);

		List<String> RptCodes = BRRS_Report_Mast_Reps.getRptCode();

		md.addAttribute("RptCodes", RptCodes);

		md.addAttribute("MCBL_List", lists);
		md.addAttribute("pagination", "YES");
		md.addAttribute("currentPage", page);
		md.addAttribute("totalPages", totalPages);

		return "Reference_Code_Master.html";
	}
*/
	/*@RequestMapping(value = "MCBL", method = { RequestMethod.GET, RequestMethod.POST })
	public String MCBL(@RequestParam(required = false) String formmode, @RequestParam(required = false) String tranid,
			@RequestParam(required = false) Optional<Integer> page,
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
			if (formmode == null || formmode.equals("add")) {
				// List<INR_Reporting_Branch_Entity> customerList = new ArrayList<>();
				String currentDateString = null;
				if (date == null) {
					// If no date provided → use today's date
					// customerList = INR_Reporting_Branch_Reps.Getcurrentdaydetail(defaultDate);
					// logger.info("Fetched {} records for default date: {}", customerList.size(),
					// defaultDate);
					currentDateString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				} else {
					// Convert LocalDate param → SQL Date
					Date sqlDate = java.sql.Date.valueOf(date);
					// customerList = INR_Reporting_Branch_Reps.Getcurrentdaydetail(sqlDate);
					// logger.info("Fetched {} records for provided date: {}", customerList.size(),
					// sqlDate);

					currentDateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				}
				md.addAttribute("currentdate", currentDateString);
				md.addAttribute("menu", "MCBL - List");
				// md.addAttribute("customersplratedetail", customerList);
				md.addAttribute("currentdate", currentDateString);
				md.addAttribute("formmode", "add");
			} else if (formmode.equals("list")) {
				List<Date> reportDates = mcblRep.findDistinctReportDates();
				md.addAttribute("reportDates", reportDates);
				md.addAttribute("menu", "File Upload - List");
				md.addAttribute("formmode", "list");
			}

		} catch (Exception e) {
			logger.error("Error in  MCBL controller: {}", e.getMessage(), e);
			md.addAttribute("errorMessage", "Error loading MCBL page. Please contact administrator.");
		}

		logger.info("<== Exiting MCBL controller");
		return "MCBL";
	}*/

	@GetMapping("/getReportDatesByFileType")
	@ResponseBody
	public List<String> getReportDatesByFileType(@RequestParam String fileType) {
		List<Date> reportDates = new ArrayList<>();

		switch (fileType) {
		case "MCBL":
			reportDates = mcblRep.findDistinctReportDates();
			break;
		case "DEPOSIT_GENERAL":
			reportDates = bdgfRep.findDistinctReportDates();
			break;
		case "LOAN_BOOK":
			reportDates = blbfRep.findDistinctReportDates();
			break;

		case "DEPOSIT_BOOK":
			reportDates = bfdbRep.findDistinctReportDates();
			break;
		}

		// Convert Date -> formatted String
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return reportDates.stream().map(sdf::format).collect(Collectors.toList());
	}

	// Getting Details by report date for view and download for MCBL IN FILE UPLOAD
	/*
	 * @GetMapping("/fetchMCBLRecords") public String fetchMCBLRecords(@RequestParam
	 * String reportDate,
	 * 
	 * @RequestParam(required = false) String fileType, Model md) {
	 * System.out.println("Came to controller with report date: " + reportDate);
	 * 
	 * // Fetch records from repository List<MCBL_Entity> lists =
	 * mcblRep.findRecordsByReportDate(reportDate);
	 * 
	 * // Add data to model md.addAttribute("MCBL_List", lists);
	 * md.addAttribute("selectedReportDate", reportDate);
	 * 
	 * List<Date> reportDates = mcblRep.findDistinctReportDates();
	 * md.addAttribute("reportDates", reportDates);
	 * 
	 * md.addAttribute("selectedFileType", fileType); // add this
	 * 
	 * md.addAttribute("formmode", "list"); // Return the same view (page name)
	 * return "MCBL"; // change this to your actual HTML/Thymeleaf page name }
	 */

	@GetMapping("/fetchRecords")
	public String fetchRecords(@RequestParam String reportDate, @RequestParam String fileType, Model md) {
		System.out.println("Fetching records for type: " + fileType + " and date: " + reportDate);

		md.addAttribute("selectedReportDate", reportDate);
		md.addAttribute("selectedFileType", fileType);
		md.addAttribute("formmode", "list");

		switch (fileType) {
		case "MCBL":
			List<MCBL_Entity> mcblList = mcblRep.findRecordsByReportDate(reportDate);
			md.addAttribute("MCBL_List", mcblList);
			List<Date> mcblDates = mcblRep.findDistinctReportDates();
			md.addAttribute("reportDates", mcblDates);
			break;

		case "DEPOSIT_GENERAL":
			List<BDGF_Entity> bdgfList = bdgfRep.findRecordsByReportDate(reportDate);
			md.addAttribute("BDGF_List", bdgfList);
			List<Date> bdgfDates = bdgfRep.findDistinctReportDates();
			md.addAttribute("reportDates", bdgfDates);
			break;

		case "DEPOSIT_BOOK":
			List<BFDB_Entity> bfdbList = bfdbRep.findRecordsByReportDate(reportDate);
			md.addAttribute("BFDB_List", bfdbList);
			List<Date> bfdbDates = bfdbRep.findDistinctReportDates();
			md.addAttribute("reportDates", bfdbDates);
			break;

		case "LOAN_BOOK":
			List<BLBF_Entity> blbfList = blbfRep.findRecordsByReportDate(reportDate);
			md.addAttribute("BLBF_List", blbfList);
			List<Date> blbfDates = blbfRep.findDistinctReportDates();
			md.addAttribute("reportDates", blbfDates);
			break;
		}

		return "MCBL"; // all tables in one page
	}

	// =================== START REPORT ===================
	@GetMapping("/startreport")
	@ResponseBody
	public String startReport(@RequestParam String filename, @RequestParam String todate) {
		String jobId = UUID.randomUUID().toString();
		System.out.println("Starting async report generation for: " + filename);

		if ("MCBL".equalsIgnoreCase(filename)) {
			MCBL_Servicess.generateReportAsync(jobId, filename, todate);
		} else if ("LOAN_BOOK".equalsIgnoreCase(filename)) {
			BLBF_Servicess.generateLoanBookReportAsync(jobId, filename, todate);
		} else if ("DEPOSIT_GENERAL".equalsIgnoreCase(filename)) {
			BDGF_Servicess.generateDepositGeneralReportAsync(jobId, filename, todate);
		} else if ("DEPOSIT_BOOK".equalsIgnoreCase(filename)) {
			BFDB_Servicess.generateDepositBookReportAsync(jobId, filename, todate);
		} else {
			return "INVALID_FILENAME";
		}

		return jobId; // Return jobId to track progress
	}

	// =================== CHECK REPORT STATUS ===================
	@GetMapping("/checkreport")
	public ResponseEntity<String> checkReport(@RequestParam String jobId, @RequestParam String filename) {

		byte[] report = null;

		if ("MCBL".equalsIgnoreCase(filename)) {
			report = MCBL_Servicess.getReport(jobId);
		} else if ("LOAN_BOOK".equalsIgnoreCase(filename)) {
			report = BLBF_Servicess.getReport(jobId);
		} else if ("DEPOSIT_GENERAL".equalsIgnoreCase(filename)) {
			report = BDGF_Servicess.getReport(jobId);
		} else if ("DEPOSIT_BOOK".equalsIgnoreCase(filename)) {
			report = BFDB_Servicess.getReport(jobId);
		} else {
			return ResponseEntity.badRequest().body("INVALID_FILENAME");
		}

		if (report == null) {
			return ResponseEntity.ok("PROCESSING");
		}
		return ResponseEntity.ok("READY");
	}

	// =================== DOWNLOAD REPORT ===================
	@GetMapping("/downloaddetailExcel")
	public ResponseEntity<byte[]> downloadDetailExcel(@RequestParam String jobId, @RequestParam String filename) {

		byte[] data = null;

		if ("MCBL".equalsIgnoreCase(filename)) {
			data = MCBL_Servicess.getReport(jobId);
		} else if ("LOAN_BOOK".equalsIgnoreCase(filename)) {
			data = BLBF_Servicess.getReport(jobId);
		} else if ("DEPOSIT_GENERAL".equalsIgnoreCase(filename)) {
			data = BDGF_Servicess.getReport(jobId);
		} else if ("DEPOSIT_BOOK".equalsIgnoreCase(filename)) {
			data = BFDB_Servicess.getReport(jobId);
		} else {
			return ResponseEntity.badRequest().build();
		}

		if (data == null || data.length == 0) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".xlsx")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(data);
	}

	@PostMapping("addmcbl")
	@ResponseBody
	public String addmcbl(@ModelAttribute MultipartFile file, Model md, String reportDate, HttpServletRequest rq) {
		logger.info("==> Entered MCBL method");
		String userid = (String) rq.getSession().getAttribute("USERID");
		String username = (String) rq.getSession().getAttribute("USERNAME");
		try {
			String msg = MCBL_Servicess.addMCBL(file, userid, username, reportDate);
			logger.info("MCBL result: {}", msg);
			return msg;
		} catch (Exception e) {
			logger.error("Error occurred while Add MCBL: {}", e.getMessage(), e);
			return "Error Occurred. Please contact Administrator.";
		}
	}

	// BDGF
	@RequestMapping(value = "BDGF", method = { RequestMethod.GET, RequestMethod.POST })
	public String BDGF(@RequestParam(required = false) String formmode, @RequestParam(required = false) String tranid,
			@RequestParam(required = false) Optional<Integer> page,
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
				// List<INR_Reporting_Branch_Entity> customerList = new ArrayList<>();
				String currentDateString = null;
				if (date == null) {
					// If no date provided → use today's date
					// customerList = INR_Reporting_Branch_Reps.Getcurrentdaydetail(defaultDate);
					// logger.info("Fetched {} records for default date: {}", customerList.size(),
					// defaultDate);
					currentDateString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				} else {
					// Convert LocalDate param → SQL Date
					Date sqlDate = java.sql.Date.valueOf(date);
					// customerList = INR_Reporting_Branch_Reps.Getcurrentdaydetail(sqlDate);
					// logger.info("Fetched {} records for provided date: {}", customerList.size(),
					// sqlDate);

					currentDateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				}
				md.addAttribute("currentdate", currentDateString);
				md.addAttribute("menu", "Blank Deposit General Format - List");
				// md.addAttribute("customersplratedetail", customerList);
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

	@PostMapping("addBDGF")
	@ResponseBody
	public String addBDGF(@ModelAttribute MultipartFile file, Model md, HttpServletRequest rq) {
		logger.info("==> Entered BDGF method");

		String userid = (String) rq.getSession().getAttribute("USERID");
		String username = (String) rq.getSession().getAttribute("USERNAME");

		try {
			String msg = BDGF_Servicess.addBDGF(file, userid, username);
			logger.info("BDGF result: {}", msg);
			return msg;
		} catch (Exception e) {
			logger.error("Error occurred while Add BDGF: {}", e.getMessage(), e);
			return "Error Occurred. Please contact Administrator.";
		}
	}

	// BFDB

	@RequestMapping(value = "BFDB", method = { RequestMethod.GET, RequestMethod.POST })
	public String BFDB(@RequestParam(required = false) String formmode, @RequestParam(required = false) String tranid,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req,
			@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		md.addAttribute("activeMenu", "Reports");
		md.addAttribute("activePage", "CentralBank");
		String USERID = (String) req.getSession().getAttribute("USERID");
		md.addAttribute("USERID", USERID);
		logger.info("==> Entered BFDB controller || Formmode: {}", formmode);

		LocalDate today = LocalDate.now();

		try {
			if (formmode == null || formmode.equals("list")) {

				md.addAttribute("formmode", "list");
			} else if (formmode.equals("add")) {
				md.addAttribute("menu", "Blank Deposit Format Book  - Add");
				md.addAttribute("formmode", "add");
			}

		} catch (Exception e) {
			logger.error("Error in  BFDB controller: {}", e.getMessage(), e);
			md.addAttribute("errorMessage", "Error loading BFDB page. Please contact administrator.");
		}

		logger.info("<== Exiting BFDB controller");
		return "BFDB";
	}

	@PostMapping("addBFDB")
	@ResponseBody
	public String addBFDB(@ModelAttribute MultipartFile file, Model md, HttpServletRequest rq) {
		logger.info("==> Entered BFDB method");

		String userid = (String) rq.getSession().getAttribute("USERID");
		String username = (String) rq.getSession().getAttribute("USERNAME");

		try {
			String msg = BFDB_Servicess.addBFDB(file, userid, username);
			logger.info("BFDB result: {}", msg);
			return msg;
		} catch (Exception e) {
			logger.error("Error occurred while Add BFDB: {}", e.getMessage(), e);
			return "Error Occurred. Please contact Administrator.";
		}
	}

//BLBF

	@RequestMapping(value = "BLBF", method = { RequestMethod.GET, RequestMethod.POST })
	public String BLBF(@RequestParam(required = false) String formmode, @RequestParam(required = false) String tranid,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req,
			@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		md.addAttribute("activeMenu", "Reports");
		md.addAttribute("activePage", "CentralBank");
		String USERID = (String) req.getSession().getAttribute("USERID");
		md.addAttribute("USERID", USERID);
		logger.info("==> Entered BLBF controller || Formmode: {}", formmode);

		LocalDate today = LocalDate.now();

		try {
			if (formmode == null || formmode.equals("list")) {

				md.addAttribute("formmode", "list");
			} else if (formmode.equals("add")) {
				md.addAttribute("menu", "Blank Loan Book Format - Add");
				md.addAttribute("formmode", "add");
			}

		} catch (Exception e) {
			logger.error("Error in  BLBF controller: {}", e.getMessage(), e);
			md.addAttribute("errorMessage", "Error loading BLBF page. Please contact administrator.");
		}

		logger.info("<== Exiting BLBF controller");
		return "BLBF";
	}

	@Autowired
	BLBF_Services BLBF_Servicess;

	@PostMapping("addBLBF")
	@ResponseBody
	public String addBLBF(@ModelAttribute MultipartFile file, Model md, HttpServletRequest rq) {
		logger.info("==> Entered BLBF method");

		String userid = (String) rq.getSession().getAttribute("USERID");
		String username = (String) rq.getSession().getAttribute("USERNAME");

		try {
			String msg = BLBF_Servicess.addBLBF(file, userid, username);
			logger.info("BLBF result: {}", msg);
			return msg;
		} catch (Exception e) {
			logger.error("Error occurred while Add BLBF: {}", e.getMessage(), e);
			return "Error Occurred. Please contact Administrator.";
		}
	}

	private ResponseEntity<byte[]> createExcelTemplate(String sheetName, String fileName, List<String> headers)
			throws Exception {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(sheetName);

		// 🔹 Header style (bold, centered, grey background)
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setLocked(true);

		// 🔹 Unlocked style for data cells
		CellStyle unlockedStyle = workbook.createCellStyle();
		unlockedStyle.setLocked(false);

		// 🔹 Create header row
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers.get(i));
			cell.setCellStyle(headerStyle);
			sheet.autoSizeColumn(i);
		}

		// 🔹 Create a few editable rows for user entry
		for (int r = 1; r <= 100; r++) { // 100 editable rows
			Row row = sheet.createRow(r);
			for (int c = 0; c < headers.size(); c++) {
				Cell cell = row.createCell(c);
				cell.setCellStyle(unlockedStyle);
			}
		}

		// 🔹 Freeze header row
		sheet.createFreezePane(0, 1);

		// 🔹 Protect sheet to enforce lock/unlock
		sheet.protectSheet("123");

		// 🔹 Write workbook to byte array
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return ResponseEntity.ok().headers(headersResponse).body(out.toByteArray());
	}

	@GetMapping("/download-templateBDGF")
	public ResponseEntity<byte[]> downloadTemplateBDGF() throws Exception {
		List<String> headers = Arrays.asList("SOL ID", "S No", "A/C No", "Customer ID", "Customer Name", "Open Date",
				"Amount Deposited", "Currency", "Period", "Rate of Interest", "100", "BAL EQUI TO BWP",
				"Outstanding Balance", "Oustndng Bal UGX", "Maturity Date", "Maturity Amount", "Scheme",
				"Cr Pref Int Rate", "SEGMENT", "REFERENCE DATE", "DIFFERENCE", "DAYS", "PERIOD",
				"EFFECTIVE INTEREST RATE", "REPORT DATE");
		return createExcelTemplate("DEPOSIT GENERAL", "DEPOSIT GENERAL.xls", headers);
	}

	@GetMapping("/download-templateBFDB")
	public ResponseEntity<byte[]> downloadTemplateBFDB() throws Exception {
		List<String> headers = Arrays.asList("SOL ID", "CUST ID", "GENDER", "ACCOUNT NO", "ACCT NAME", "SCHM_CODE",
				"SCHM DESC", "ACCT OPN DATE", "ACCT CLS DATE", "BALANCE AS ON", "CCY", "BAL EQUI TO BWP", "INT RATE",
				"100", "STATUS", "MATURITY DATE", "GL SUB HEAD CODE", "GL SUB HEAD DESC", "TYPE OF ACCOUNTS", "SEGMENT",
				"PERIOD", "EFFECTIVE INTEREST RATE", "REPORT DATE");
		return createExcelTemplate("DEPOSIT BOOK", "DEPOSIT BOOK.xls", headers);
	}

	@GetMapping("/download-templateBLBF")
	public ResponseEntity<byte[]> downloadTemplateBLBF() throws Exception {
		List<String> headers = Arrays.asList("SOL ID", "CUST ID", "ACCOUNT NO", "ACCT NAME", "SCHM_CODE", "SCHM DESC",
				"ACCT OPN DATE", "APPROVED LIMIT", "SANCTION LIMIT", "DISBURSED AMT", "BALANCE AS ON", "CCY",
				"BAL EQUI TO BWP", "INT RATE", "100", "ACCRUED INT AMT", "MONTHLY INTEREST", "LAST INTEREST DEBIT DATE",
				"ACCT CLS FLG", "CLOSE DATE", "GENDER", "CLASSFICATION CODE", "CONSTITUTION CODE", "MATURITY DATE",
				"GL SUB HEAD CODE", "GL SUB HEAD DESC", "TENOR(MONTH)", "EMI", "SEGMENT", "FACILITY", "PAST DUE",
				"PAST DUE DAYS", "ASSET", "PROVISION", "UNSECURED", "INT BUCKET", "STAFF", "SMME", "LABOD", "NEW A/C",
				"UNDRAWN", "SECTOR", "Period", "Effective Interest Rate", "STAGE", "ECL PROVISION", "REPORT DATE");
		return createExcelTemplate("LOAN BOOK", "LOAN BOOK.xls", headers);
	}

}
