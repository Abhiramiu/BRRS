package com.bornfire.brrs.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.dto.ReportLineItemDTO;
import com.bornfire.brrs.entities.*;
import com.bornfire.brrs.services.BRRS_ADISB1_ReportService;
import com.bornfire.brrs.services.BRRS_BASEL_III_COM_EQUITY_DISC_ReportService;
import com.bornfire.brrs.services.BRRS_BDISB1_ReportService;
import com.bornfire.brrs.services.BRRS_BDISB2_ReportService;
import com.bornfire.brrs.services.BRRS_BDISB3_ReportService;
import com.bornfire.brrs.services.BRRS_FORMAT_II_ReportService;
import com.bornfire.brrs.services.BRRS_GL_SCH_ReportService;
import com.bornfire.brrs.services.BRRS_MDISB1_ReportService;
import com.bornfire.brrs.services.BRRS_MDISB2_ReportService;
import com.bornfire.brrs.services.BRRS_MDISB3_ReportService;
import com.bornfire.brrs.services.BRRS_MDISB5_ReportService;
import com.bornfire.brrs.services.BRRS_M_AIDP_ReportService;
import com.bornfire.brrs.services.BRRS_M_BOP_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA2_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA3_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA4_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA5_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA6_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA7_ReportService;
import com.bornfire.brrs.services.BRRS_M_CR_ReportService;
import com.bornfire.brrs.services.BRRS_M_DEP3_ReportService;
import com.bornfire.brrs.services.BRRS_M_DEP4_ReportService;
import com.bornfire.brrs.services.BRRS_M_EPR_ReportService;
import com.bornfire.brrs.services.BRRS_M_FAS_ReportService;
import com.bornfire.brrs.services.BRRS_M_FXR_ReportService;
import com.bornfire.brrs.services.BRRS_M_GALOR_ReportService;
import com.bornfire.brrs.services.BRRS_M_GMIRT_ReportService;
import com.bornfire.brrs.services.BRRS_M_GP_ReportService;
import com.bornfire.brrs.services.BRRS_M_INT_RATES_FCA_NEW_ReportService;
import com.bornfire.brrs.services.BRRS_M_INT_RATES_FCA_ReportService;
import com.bornfire.brrs.services.BRRS_M_INT_RATES_NEW_ReportService;
import com.bornfire.brrs.services.BRRS_M_INT_RATES_ReportService;
import com.bornfire.brrs.services.BRRS_M_IS_ReportService;
import com.bornfire.brrs.services.BRRS_M_LA1_ReportService;
import com.bornfire.brrs.services.BRRS_M_LA2_ReportService;
import com.bornfire.brrs.services.BRRS_M_LA3_ReportService;
import com.bornfire.brrs.services.BRRS_M_LA4_ReportService;
import com.bornfire.brrs.services.BRRS_M_LA5_ReportService;
import com.bornfire.brrs.services.BRRS_M_LARADV_ReportService;
import com.bornfire.brrs.services.BRRS_M_LIQGAP_ReportService;
import com.bornfire.brrs.services.BRRS_M_LIQ_ReportService;
import com.bornfire.brrs.services.BRRS_M_MRC_ReportService;
import com.bornfire.brrs.services.BRRS_M_NOSVOS_ReportService;
import com.bornfire.brrs.services.BRRS_M_OB_ReportService;
import com.bornfire.brrs.services.BRRS_M_OPTR_NEW_ReportService;
import com.bornfire.brrs.services.BRRS_M_OPTR_ReportService;
import com.bornfire.brrs.services.BRRS_M_OR1_ReportService;
import com.bornfire.brrs.services.BRRS_M_OR2_ReportService;
import com.bornfire.brrs.services.BRRS_M_PD_ReportService;
import com.bornfire.brrs.services.BRRS_M_PI_ReportService;
import com.bornfire.brrs.services.BRRS_M_PLL_ReportService;
import com.bornfire.brrs.services.BRRS_M_RPD_ReportService;
import com.bornfire.brrs.services.BRRS_M_SCI_E_ReportService;
import com.bornfire.brrs.services.BRRS_M_SECA_ReportService;
import com.bornfire.brrs.services.BRRS_M_SECL_ReportService;
import com.bornfire.brrs.services.BRRS_M_SEC_ReportService;
import com.bornfire.brrs.services.BRRS_M_SFINP1_ReportService;
import com.bornfire.brrs.services.BRRS_M_SIR_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12A_New_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12A_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12B_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12C_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12D_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12F_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12G_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12H_New_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12H_ReportService;
import com.bornfire.brrs.services.BRRS_M_TBS_ReportService;
import com.bornfire.brrs.services.BRRS_M_TOP_100_BORROWER_ReportService;
import com.bornfire.brrs.services.BRRS_M_UNCONS_INVEST_ReportService;
import com.bornfire.brrs.services.BRRS_PL_SCHS_ReportService;
import com.bornfire.brrs.services.BRRS_Q_BRANCHNET_ReportService;
import com.bornfire.brrs.services.BRRS_Q_RLFA1_ReportService;
import com.bornfire.brrs.services.BRRS_Q_RLFA2_ReportService;
import com.bornfire.brrs.services.BRRS_Q_SMME_DEP_ReportService;
import com.bornfire.brrs.services.BRRS_Q_STAFF_New_Report_Service;
import com.bornfire.brrs.services.BRRS_Q_STAFF_Report_Service;
import com.bornfire.brrs.services.BRRS_SCH_17_ReportService;
import com.bornfire.brrs.services.RegulatoryReportServices;
import com.bornfire.brrs.services.ReportCodeMappingService;

@Controller
@ConfigurationProperties("default")
@RequestMapping(value = "Reports")
public class BRRS_ReportsController {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_ReportsController.class);
	@Autowired
	RegulatoryReportServices regreportServices;

	@Autowired
	private BRRS_M_AIDP_ReportService AIDPreportService;

	@Autowired
	private BRRS_M_LA2_ReportService LA2reportService;

	@Autowired
	BRRS_M_LA4_ReportService BRRS_M_LA4_ReportService;

	@Autowired
	BRRS_M_LA1_ReportService brrs_M_LA1_ReportService;

	@Autowired
	BRRS_M_LA3_ReportService brrs_M_LA3_ReportService;

	@Autowired
	BRRS_M_LA4_ReportService brrs_M_LA4_ReportService;

	@Autowired
	BRRS_M_LIQ_ReportService brrs_M_LIQ_ReportService;

	@Autowired
	private BRRS_M_PLL_ReportService brrsMpllReportService;

	@Autowired
	private ReportCodeMappingService reportCodeMappingService;

	@Autowired
	BRRS_M_NOSVOS_ReportService BRRS_M_NOSVOS_ReportService;

	@Autowired
	BRRS_MDISB1_ReportService BRRS_MDISB1_ReportService;
	
	@Autowired
	BRRS_MDISB2_ReportService BRRS_MDISB2_ReportService;
	
	@Autowired
	BRRS_MDISB3_ReportService BRRS_MDISB3_ReportService;

	private String pagesize;

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

	// To show the required report at the first stage
	@RequestMapping(value = "{reportid}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView reportView(@PathVariable("reportid") String reportid,
			@RequestParam(value = "function", required = false) String function,
			@RequestParam("asondate") String asondate, @RequestParam(required = false) String fromdate,
			@RequestParam("todate") String todate, @RequestParam(value = "currency", required = false) String currency,
			@RequestParam(value = "subreportid", required = false) String subreportid,
			@RequestParam(value = "secid", required = false) String secid,
			@RequestParam(value = "dtltype", required = false) String dtltype,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "version", required = false) BigDecimal version,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "reportingTime", required = false) String reportingTime, Model md,
			HttpServletRequest req, BigDecimal srl_no) throws ParseException {

		String userid = (String) req.getSession().getAttribute("USERID");
		String roleid = (String) req.getSession().getAttribute("ROLEID");
		String accesscode = (String) req.getSession().getAttribute("ACCESSCODE");

		int currentPage = page.orElse(0);
		int pageSize = size.orElse(Integer.parseInt(pagesize));
		System.out.println("date" + fromdate);
		// Assigning required Modal Attributes
		md.addAttribute("UserId", userid);
		md.addAttribute("RoleId", roleid);
		md.addAttribute("UserCol", accesscode);
		md.addAttribute("reportid", reportid);
		md.addAttribute("asondate", asondate);
		md.addAttribute("fromdate", fromdate);
		md.addAttribute("todate", todate);
		md.addAttribute("currency", currency);
		md.addAttribute("dtltype", dtltype);
		md.addAttribute("type", type);
		md.addAttribute("version", version);
		md.addAttribute("reportingTime", reportingTime);
		// md.addAttribute("reportTitle", reportServices.getReportName(reportid));

		try {
			asondate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(asondate));
			fromdate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(fromdate));
			todate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(todate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ModelAndView mv = new ModelAndView();
		mv = regreportServices.getReportView(reportid, asondate, fromdate, todate, currency, dtltype, subreportid,
				secid, reportingTime, PageRequest.of(currentPage, pageSize), srl_no, userid, type, version);

		return mv;

	}

	@RequestMapping(value = "{reportid}/Details", method = RequestMethod.GET)
	public ModelAndView reportDetail(@PathVariable("reportid") String reportid,
			@RequestParam(value = "instancecode", required = false) String instancecode,
			@RequestParam(value = "filter", required = false) String filter, @RequestParam("asondate") String asondate,
			@RequestParam("fromdate") String fromdate, @RequestParam("todate") String todate,
			@RequestParam("currency") String currency, @RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "subreportid", required = false) String subreportid,
			@RequestParam(value = "secid", required = false) String secid,
			@RequestParam(value = "dtltype", required = false) String dtltype,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size,
			@RequestParam(value = "reportingTime", required = false) String reportingTime, Model md) {

		md.addAttribute("reportid", reportid);
		md.addAttribute("asondate", asondate);
		md.addAttribute("fromdate", fromdate);
		md.addAttribute("todate", todate);
		md.addAttribute("filter", filter);
		md.addAttribute("currency", currency);
		md.addAttribute("dtltype", dtltype);
		md.addAttribute("reportingTime", reportingTime);
		md.addAttribute("type", type);
		md.addAttribute("version", version);
		// md.addAttribute("instancecode", Integer.parseInt(instancecode));
		// md.addAttribute("reportTitle", reportServices.getReportName(reportid));
		md.addAttribute("displaymode", "detail");

		try {
			asondate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(asondate));
			fromdate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(fromdate));
			todate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(todate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// logger.info("Getting ModelandView :" + reportid);
		ModelAndView mv = regreportServices.getReportDetails(reportid, instancecode, asondate, fromdate, todate,
				currency, reportingTime, dtltype, subreportid, secid, PageRequest.of(page, size), filter, type,
				version);

		return mv;
	}

	@RequestMapping(value = "downloadExcel", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<ByteArrayResource> BRFDownload(HttpServletResponse response,
			@RequestParam("reportid") String reportid, @RequestParam("asondate") String asondate,
			@RequestParam("fromdate") String fromdate, @RequestParam("todate") String todate,
			@RequestParam("currency") String currency, @RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "version", required = false) String versionBD,
			@RequestParam(value = "subreportid", required = false) String subreportid,
			@RequestParam(value = "secid", required = false) String secid,
			@RequestParam(value = "dtltype", required = false) String dtltype,
			@RequestParam(value = "reportingTime", required = false) String reportingTime,
			@RequestParam(value = "filename", required = false) String filename,
			@RequestParam(value = "instancecode", required = false) String instancecode,
			@RequestParam(value = "filter", required = false) String filter)
			throws SQLException, FileNotFoundException {

		response.setContentType("application/octet-stream");
		
		BigDecimal version = null;

		if (versionBD != null) {
		    versionBD = versionBD.trim();
		    if (!versionBD.isEmpty()
		            && !"null".equalsIgnoreCase(versionBD)
		            && !"undefined".equalsIgnoreCase(versionBD)) {
		        version = new BigDecimal(versionBD);
		    }
		}
		
		try {
			asondate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(asondate));
			fromdate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(fromdate));
			todate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(todate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			System.out.println("came to controller");
			byte[] excelData = regreportServices.getDownloadFile(reportid, filename, asondate, fromdate, todate,
					currency, subreportid, secid, dtltype, reportingTime, instancecode, filter, type, version);

			if (excelData == null || excelData.length == 0) {
				logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			filename = filename + ".xlsx";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (Exception e) {
			logger.error("Controller ERROR: A critical error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "downloaddetailExcel", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<ByteArrayResource> detailDownload(HttpServletResponse response,
			@RequestParam("jobId") String jobId, @RequestParam("filename") String filename,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "todate", required = false) String todate)
			throws SQLException, FileNotFoundException {

		System.out.println("🔵 [CONTROLLER] DETAIL DOWNLOAD CALLED");
		System.out.println("JobId Passed = " + jobId);
		System.out.println("Filename Passed = " + filename);
		System.out.println("TYPE Passed = " + type);
		System.out.println("VERSION Passed = " + version);
		System.out.println("TODATE Passed = " + todate);

		response.setContentType("application/octet-stream");

		try {
			byte[] excelData = null;

			excelData = regreportServices.getReport(jobId);
			;

			if (excelData == null || excelData.length == 0) {
				logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			filename = filename + ".xls";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (Exception e) {
			logger.error("Controller ERROR: A critical error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "/startreport", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody // forces raw text instead of HTML view
	public String startReport(@RequestParam String filename, @RequestParam("fromdate") String fromdate,
			@RequestParam("todate") String todate, @RequestParam String currency,
			@RequestParam("dtltype") String dtltype, @RequestParam("type") String type,
			@RequestParam(value = "version", required = false) String version) {
		String jobId = UUID.randomUUID().toString();
		System.out.println("jobid" + jobId);
		logger.info("Getting Inside startreport");
		regreportServices.generateReportAsync(jobId, filename, fromdate, todate, dtltype, type, currency, version);
		// RT_SLSServices.generateReportAsync(jobId, filename, reportdate,
		// currency,version);
		return jobId;
	}

	@RequestMapping(value = "/checkreport", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody // forces raw text instead of HTML view
	public ResponseEntity<String> checkReport(@RequestParam String jobId) {
		byte[] report = regreportServices.getReport(jobId);
		// System.out.println("Report generation completed for: " + jobId);
		if (report == null) {
			return ResponseEntity.ok("PROCESSING");
		}
		if (report.length == 0) {
			return ResponseEntity.ok("ERROR");
		}

		return ResponseEntity.ok("READY");

	}

	@RequestMapping(value = "/AIDPupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateLA4(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute BRRS_M_AIDP_Summary_Entity1 request1, @ModelAttribute BRRS_M_AIDP_Summary_Entity2 request2,
			@ModelAttribute BRRS_M_AIDP_Summary_Entity3 request3,
			@ModelAttribute BRRS_M_AIDP_Summary_Entity4 request4) {
		try {

			// set date into all 4 entities
			request1.setREPORT_DATE(asondate);
			request2.setREPORT_DATE(asondate);
			request3.setREPORT_DATE(asondate);
			request4.setREPORT_DATE(asondate);

			// call services
			AIDPreportService.updateReport(request1);
			AIDPreportService.updateReport2(request2);
			AIDPreportService.updateReport3(request3);
			AIDPreportService.updateReport4(request4);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/updateReport", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_LA4_Summary_Entity2 request1

	) {
		try {

			// set date into all 4 entities
			request1.setReportDate(asondate);

			// call services
			BRRS_M_LA4_ReportService.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	/*
	 * @RequestMapping(value = "/updateReportMDISB1", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateAllReports(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute MDISB1_Summary_Entity_Manual request1
	 * 
	 * ) { try {
	 * 
	 * // set date into all 4 entities request1.setReport_date(asondate);
	 * 
	 * // call services BRRS_MDISB1_ReportService.updateReport(request1);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Update Failed: " + e.getMessage()); } }
	 */
	@Autowired
	private BRRS_M_UNCONS_INVEST_ReportService UNCreportService;
	@RequestMapping(value = "/UNCupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateUNCAllReports(

	        @RequestParam(required = false)
	        @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

	        @ModelAttribute M_UNCONS_INVEST_Summary_Entity request1) {

	    try {
	        System.out.println("Came to single controller");

	        request1.setReport_date(asondate);
	        
	    	UNCreportService.updateReport(request1);
			UNCreportService.updateReport2(request1);
			UNCreportService.updateReport3(request1);
			UNCreportService.updateReport4(request1);

	        return ResponseEntity.ok("Modified Successfully.");

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Update Failed: " + e.getMessage());
	    }
	}
//	@RequestMapping(value = "/UNCupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public ResponseEntity<String> updateUNCAllReports(
//			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
//
//			@RequestParam(required = false) String type, @ModelAttribute M_UNCONS_INVEST_Summary_Entity1 request1,
//			@ModelAttribute M_UNCONS_INVEST_Summary_Entity2 request2,
//			@ModelAttribute M_UNCONS_INVEST_Summary_Entity3 request3,
//			@ModelAttribute M_UNCONS_INVEST_Summary_Entity4 request4) {
//		try {
//			System.out.println("Came to single controller");
//			System.out.println(type);
//
//			// set date into all 4 entities
//			request1.setREPORT_DATE(asondate);
//			request2.setREPORT_DATE(asondate);
//			request3.setREPORT_DATE(asondate);
//			request4.setREPORT_DATE(asondate);
//
//			if (type.equals("ARCHIVAL")) {
//				M_UNCONS_INVEST_Archival_Summary_Entity1 Archivalrequest1 = new M_UNCONS_INVEST_Archival_Summary_Entity1();
//				M_UNCONS_INVEST_Archival_Summary_Entity2 Archivalrequest2 = new M_UNCONS_INVEST_Archival_Summary_Entity2();
//				M_UNCONS_INVEST_Archival_Summary_Entity3 Archivalrequest3 = new M_UNCONS_INVEST_Archival_Summary_Entity3();
//				M_UNCONS_INVEST_Archival_Summary_Entity4 Archivalrequest4 = new M_UNCONS_INVEST_Archival_Summary_Entity4();
//				BeanUtils.copyProperties(request1, Archivalrequest1);
//				BeanUtils.copyProperties(request2, Archivalrequest2);
//				BeanUtils.copyProperties(request3, Archivalrequest3);
//				BeanUtils.copyProperties(request4, Archivalrequest4);
//				UNCreportService.updateArchivalReport(Archivalrequest1);
//				UNCreportService.updateArchivalReport2(Archivalrequest2);
//				UNCreportService.updateArchivalReport3(Archivalrequest3);
//				UNCreportService.updateArchivalReport4(Archivalrequest4);
//			} else {
//				// call services
//				UNCreportService.updateReport(request1);
//				UNCreportService.updateReport2(request2);
//				UNCreportService.updateReport3(request3);
//				UNCreportService.updateReport4(request4);
//
//			}
//
//			return ResponseEntity.ok("Updated Successfully.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
//		}
//	}

	@Autowired
	private BRRS_M_CA2_ReportService brrs_m_ca2_reportservice;

	@RequestMapping(value = "/MCA2updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_CA2_Manual_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setReport_date(asondate);

			// call services
			brrs_m_ca2_reportservice.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_TOP_100_BORROWER_ReportService M_TOP_100_BORROWER_ReportService;

	@RequestMapping(value = "/M_TOP_100_BORROWERupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_TOP_100_BORROWER_Manual_Summary_Entity1 request1,
			@ModelAttribute M_TOP_100_BORROWER_Manual_Summary_Entity2 request2

	) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setReport_date(asondate);
			request2.setReport_date(asondate);

			// call services
			M_TOP_100_BORROWER_ReportService.updateReport(request1);
			M_TOP_100_BORROWER_ReportService.updateReport1(request2);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_MRC_ReportService brrs_m_mrc_reportservice;

	@RequestMapping(value = "/MMRCupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_MRC_Manual_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setReport_date(asondate);

			// call services
			brrs_m_mrc_reportservice.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_LIQGAP_ReportService brrs_m_liqgap_reportservice;

	@RequestMapping(value = "/LIQGAPupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_LIQGAP_Manual_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setReport_date(asondate);

			// call services
			brrs_m_liqgap_reportservice.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_SRWA_12D_ReportService SRWA12DreportService;

	@RequestMapping(value = "/SRWA12DupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SRWA_12D_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");
			// set date into all 4 entities
			request1.setReport_date(asondate);

			// call services
			SRWA12DreportService.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_CA6_ReportService BRRS_M_CA6_ReportService;

	@RequestMapping(value = "/MCA6updateAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
	        @RequestParam(required = false)
	        @DateTimeFormat(pattern = "yyyy-MM-dd") Date asondate,

	        @RequestParam(required = false) String type,

	        @ModelAttribute M_CA6_Summary_Entity1 summary1,
	        @ModelAttribute M_CA6_Detail_Entity1 detail1,

	        @ModelAttribute M_CA6_Summary_Entity2 summary2,
	        @ModelAttribute M_CA6_Detail_Entity2 detail2
	) {
	    try {
	        System.out.println("Came to CA6 UPDATE single controller");
	        System.out.println(type);

	        // set date into all entities
	        summary1.setReportDate(asondate);
	        detail1.setReportDate(asondate);
	        summary2.setReportDate(asondate);
	        detail2.setReportDate(asondate);

	        // call services
	        BRRS_M_CA6_ReportService.updateReport1(summary1);
	        BRRS_M_CA6_ReportService.updatedetail1(detail1);

	        BRRS_M_CA6_ReportService.updateReport2(summary2);
	        BRRS_M_CA6_ReportService.updateDetial2(detail2);

	        return ResponseEntity.ok("Modified Successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Update Failed: " + e.getMessage());
	    }
	}


	@Autowired
	BRRS_M_SRWA_12C_ReportService BRRS_M_SRWA_12C_reportservice;

	@RequestMapping(value = "/M_SRWA_12Cupdate", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SRWA_12C_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);

			// call services
			BRRS_M_SRWA_12C_reportservice.updateReport(request);

			return ResponseEntity.ok(" Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_SRWA_12C_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SRWA_12C_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReport_date(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReport_date());
			}

			// Call service to create a new versioned row
			BRRS_M_SRWA_12C_reportservice.updateReportResub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	

	/*
	 * @Autowired private BRRS_M_CA3_ReportService CA3reportService;
	 * 
	 * @RequestMapping(value = "/CA3updateAll", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateAllReports(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_CA3_Summary_Entity request1
	 * 
	 * ) { try { System.out.println("Came to single controller");
	 * 
	 * // set date into all 4 entities request1.setREPORT_DATE(asondate);
	 * 
	 * // call services CA3reportService.updateReport(request1);
	 * CA3reportService.updateReport2(request1);
	 * CA3reportService.updateReport3(request1);
	 * CA3reportService.updateReport4(request1);
	 * CA3reportService.updateReport5(request1);
	 * CA3reportService.updateReport6(request1);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Update Failed: " + e.getMessage()); } }
	 */
	/*
	 * @RequestMapping(value = "/UpdateM_CA3_ReSub", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateReportReSub(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_CA3_Summary_Entity request, HttpServletRequest req) {
	 * 
	 * try { System.out.println("Came to Resub Controller");
	 * 
	 * if (asondate != null) { // Set the asondate into the entity
	 * request.setREPORT_DATE(asondate); System.out.println("Set Report Date: " +
	 * asondate); } else {
	 * System.out.println("Asondate parameter is null; using entity value: " +
	 * request.getREPORT_DATE()); }
	 * 
	 * // Call service to create a new versioned row
	 * CA3reportService.updateReportResub(request);
	 * 
	 * return ResponseEntity.ok("Resubmission Updated Successfully");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Resubmission Update Failed: " + e.getMessage()); } }
	 */

	@Autowired
	private BRRS_M_SRWA_12G_ReportService brrs_m_srwa_12g_reportservice;

	@RequestMapping(value = "/MSRWA12GupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_SRWA_12G_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setReport_date(asondate);

			// call services
			brrs_m_srwa_12g_reportservice.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_SCI_E_ReportService brrs_m_sci_e_reportservice;

	@RequestMapping(value = "/M_SCI_EupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SCI_E_Manual_Summary_Entity request) {
		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);
			// call services
			brrs_m_sci_e_reportservice.updateReport(request);

			return ResponseEntity.ok(" Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}
	
	@Autowired
	BRRS_BASEL_III_COM_EQUITY_DISC_ReportService b_III_cetd_ReportService;

	@RequestMapping(value = "/B_III_CETDupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute BASEL_III_COM_EQUITY_DISC_Manual_Summary_Entity request) {
		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);
			// call services
			b_III_cetd_ReportService.updateReport(request);

			return ResponseEntity.ok(" Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_OR1_ReportService brrs_m_or1_reportservice;

	@RequestMapping(value = "/OR1updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_OR1_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setReport_date(asondate);

			// call services
			brrs_m_or1_reportservice.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_CA4_ReportService brrs_m_ca4_reportservice;

	@RequestMapping(value = "/M_CA4update", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_CA4_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);

			// call services
			brrs_m_ca4_reportservice.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_CA4_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_CA4_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReport_date(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReport_date());
			}

			// Call service to create a new versioned row
			brrs_m_ca4_reportservice.updateReportResub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_LA3_ReportService LA3reportService;

	@RequestMapping(value = "/LA3updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateLA3AllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_LA3_Summary_Entity2 request) {
		try {
			System.out.println("Came to single controller");

			// ✅ set report date
			request.setREPORT_DATE(asondate);

			// ✅ directly update summary report
			LA3reportService.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private RegulatoryReportServices regulatoryReportServices;

	/*
	 * @RequestMapping(value = "/getReportDataByCode", method = RequestMethod.GET)
	 * 
	 * @ResponseBody
	 * public List<ReportLineItemDTO>
	 * getReportDataByCode(@RequestParam("reportCode") String reportCode) throws
	 * Exception {
	 * 
	 * System.out.println("Controller received request for report code = " +
	 * reportCode);
	 * 
	 * return regulatoryReportServices.getReportDataByCode(reportCode);
	 * }
	 */

	@GetMapping("/getReportDataByCode")
	@ResponseBody
	public List<ReportLineItemDTO> getReportDataByCode(@RequestParam("reportCode") String reportCode) {
		System.out.println("Controller received request for report code: " + reportCode);
		System.out.println("reportCodeMappingService object: " + reportCodeMappingService);

		try {
			List<ReportLineItemDTO> data = reportCodeMappingService.getReportDataByCode(reportCode);
			System.out.println("Service call succeeded, records fetched: " + data.size());
			return data;
		} catch (Exception e) {
			System.err.println("Error fetching report data for " + reportCode + ": " + e.getMessage());
			return Collections.emptyList();
		} finally {
			System.out.println("Controller finished processing request for report code: " + reportCode);
		}
	}

	@Autowired
	BRRS_M_LIQ_ReportService brrs_m_liq_reportservice;

	@RequestMapping(value = "/M_LIQupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_LIQ_Manual_Summary_Entity request2

	) {
		try {
			System.out.println("Came to single controller");
			// set date into all 3 entities

			request2.setReport_date(asondate);

			// call services
			/* brrs_m_liq_reportservice.updateReport(request1); */
			brrs_m_liq_reportservice.updateReport1(request2);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_CA5_ReportService CA5reportService;

	@RequestMapping(value = "/CA5updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_CA5_Summary_Entity1 request1, @ModelAttribute M_CA5_Summary_Entity2 request2

	) {
		try {
			System.out.println("Came to single controller");
			// set date into all 4 entities
			request1.setReportDate(asondate);
			request2.setReportDate(asondate);

			// call services
			CA5reportService.updateReport(request1);
			CA5reportService.updateReport2(request2);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_CA5_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSubAll(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_CA5_Summary_Entity1 request1,
			@ModelAttribute M_CA5_Summary_Entity2 request2,
			HttpServletRequest req) {

		try {
			System.out.println("Came to M_CA5 Resub Controller");

			if (asondate != null) {
				request1.setReportDate(asondate);
				request2.setReportDate(asondate);
				System.out.println("🗓 Set Report Date: " + asondate);
			}

			// ✅ Call service
			CA5reportService.updateReportReSub(request1, request2);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("M_FXR Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_BDISB1_ReportService BDISB1reportService;

	@PostMapping("/BDISB1updateAll")
	@ResponseBody
	public ResponseEntity<String> updateBDISB1AllReports(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {
		try {
			System.out.println("Came to BDISB1 controller");

			BDISB1reportService.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateBDISB1_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute BDISB1_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
			BDISB1reportService.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	// @Autowired
	// BRRS_M_SRWA_12F_ReportService M_SRWA_12FreportService;

	// @PostMapping("/SRWA12FupdateAll")
	// @ResponseBody
	// public ResponseEntity<String> updateMSRWA12F1AllReports(
	// 		@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	// 		@RequestParam Map<String, String> allParams) {
	// 	try {
	// 		System.out.println("Came to M_SRWA_12F controller");

	// 		M_SRWA_12FreportService.updateDetailFromForm(asondate, allParams);

	// 		return ResponseEntity.ok("Updated Successfully.");
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		return ResponseEntity
	// 				.status(HttpStatus.INTERNAL_SERVER_ERROR)
	// 				.body("Update Failed: " + e.getMessage());
	// 	}
	// }

	// @RequestMapping(value = "/UpdateM_SRWA_12F_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	// public ResponseEntity<String> updateReportReSub(
	// 		@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	// 		@ModelAttribute M_SRWA_12F_Summary_Entity request,
	// 		HttpServletRequest req) {

	// 	try {
	// 		System.out.println("Came to Resub Controller");

	// 		if (asondate != null) {
	// 			// Set the asondate into the entity
	// 			request.setReportDate(asondate);
	// 			System.out.println("Set Report Date: " + asondate);
	// 		} else {
	// 			System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
	// 		}

	// 		// Call service to create a new versioned row
	// 		// M_SRWA_12FreportService.updateReportReSub(request);

	// 		return ResponseEntity.ok("Resubmission Updated Successfully");

	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	// 				.body("Resubmission Update Failed: " + e.getMessage());
	// 	}
	// }
	

	/*
	 * @Autowired BRRS_M_INT_RATES_FCA_ReportService INT_RATES_FCAreportService;
	 * 
	 * @PostMapping("/INTRATESFCAupdateAll")
	 * 
	 * @ResponseBody public ResponseEntity<String> updateINTRATESFCAAllReports(
	 * 
	 * @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	 * 
	 * @RequestParam Map<String, String> allParams) { try {
	 * System.out.println("Came to INT_RATES_FCA controller");
	 * 
	 * INT_RATES_FCAreportService.updateDetailFromForm(asondate, allParams);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return ResponseEntity
	 * .status(HttpStatus.INTERNAL_SERVER_ERROR) .body("Update Failed: " +
	 * e.getMessage()); } }
	 */

	/*
	 * @RequestMapping(value = "/UpdateM_INT_RATES_FCA_ReSub", method = {
	 * RequestMethod.GET, RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateReportReSub(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_INT_RATES_FCA_Summary_Entity request, HttpServletRequest
	 * req) {
	 * 
	 * try { System.out.println("Came to Resub Controller");
	 * 
	 * if (asondate != null) { // Set the asondate into the entity
	 * request.setReportDate(asondate); System.out.println("Set Report Date: " +
	 * asondate); } else {
	 * System.out.println("Asondate parameter is null; using entity value: " +
	 * request.getReportDate()); }
	 * 
	 * // Call service to create a new versioned row
	 * INT_RATES_FCAreportService.updateReportReSub(request);
	 * 
	 * return ResponseEntity.ok("Resubmission Updated Successfully");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Resubmission Update Failed: " + e.getMessage()); } }
	 */

	@Autowired
	BRRS_M_INT_RATES_FCA_NEW_ReportService INT_RATES_FCA_NEWreportService;

	@PostMapping("/INTRATESFCANEWupdateAll")
	@ResponseBody
	public ResponseEntity<String> updateINTRATESFCANEWAllReports(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {
		try {
			System.out.println("Came to INT_RATES_FCA_NEW controller");

			INT_RATES_FCA_NEWreportService.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}
	
	
	
	
	
	/*
	 * @Autowired BRRS_M_INT_RATES_ReportService INT_RATES_reportService;
	 * 
	 * @PostMapping("/INTRATESupdateAll")
	 * 
	 * @ResponseBody public ResponseEntity<String> updateINTRATESReports(
	 * 
	 * @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	 * 
	 * @RequestParam Map<String, String> allParams) { try {
	 * System.out.println("Came to INT_RATES controller");
	 * 
	 * INT_RATES_reportService.updateDetailFromForm(asondate, allParams);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return ResponseEntity
	 * .status(HttpStatus.INTERNAL_SERVER_ERROR) .body("Update Failed: " +
	 * e.getMessage()); } }
	 */
	
	
	
	

	/*
	 * @RequestMapping(value = "/UpdateM_INT_RATES_FCA_NEWReSub", method = {
	 * RequestMethod.GET, RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateReportReSub(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_INT_RATES_FCA_NEW_Summary_Entity request,
	 * HttpServletRequest req) {
	 * 
	 * try { System.out.println("Came to Resub Controller");
	 * 
	 * if (asondate != null) { // Set the asondate into the entity
	 * request.setReportDate(asondate); System.out.println("Set Report Date: " +
	 * asondate); } else {
	 * System.out.println("Asondate parameter is null; using entity value: " +
	 * request.getReportDate()); }
	 * 
	 * // Call service to create a new versioned row
	 * INT_RATES_FCA_NEWreportService.updateReportReSub(request);
	 * 
	 * return ResponseEntity.ok("Resubmission Updated Successfully");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Resubmission Update Failed: " + e.getMessage()); } }
	 */

	@Autowired
	BRRS_M_OPTR_ReportService M_OPTRreportService;
/*
	@PostMapping("/MOPTRupdateAll")
	@ResponseBody
	public ResponseEntity<String> updateMOPTRAllReports(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {
		try {
			System.out.println("Came to M_OPTR controller");

			M_OPTRreportService.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}
	*/

	@RequestMapping(value = "/MOPTRupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateMOPTRAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_OPTR_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReportDate(asondate);

			// call services
			M_OPTRreportService.updateReport(request);

			return ResponseEntity.ok(" Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_OPTR_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_OPTR_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
			M_OPTRreportService.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_OPTR_NEW_ReportService M_OPTRNEWreportService;

	@PostMapping("/M_OPTR_NEWupdateAll")
	@ResponseBody
	public ResponseEntity<String> updateMOPTRNEWAllReports(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {
		try {
			System.out.println("Came to M_OPTR_NEW controller");

			M_OPTRNEWreportService.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_OPTR_NEW_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_OPTR_NEW_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
			M_OPTRNEWreportService.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_BDISB3_ReportService BDISB3reportService;

	/*
	 * @RequestMapping(value = "/BDISB3updateAll", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody
	 * public ResponseEntity<String> updateAllReports(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute BDISB3_Summary_Entity request1
	 * 
	 * ) {
	 * try {
	 * System.out.println("Came to single controller");
	 * // set date into all 4 entities
	 * request1.setReportDate(asondate);
	 * 
	 * 
	 * // call services
	 * BDISB3reportService.updateReport(request1);
	 * 
	 * return ResponseEntity.ok("Updated Successfully.");
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Update Failed: " + e.getMessage());
	 * }
	 * }
	 */
	@PostMapping("/BDISB3updateAll")
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {
		try {
			System.out.println("came to Controller for updating values");
			BDISB3reportService.updateDetailFromForm(asondate, allParams);
			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateBDISB3_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSubAll(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute BDISB3_Summary_Entity request1,
			HttpServletRequest req) {

		try {
			System.out.println("Came to M_BDISB3 Resub Controller");

			if (asondate != null) {
				request1.setReportDate(asondate);
				System.out.println("🗓 Set Report Date: " + asondate);
			}

			// ✅ Call service
			BDISB3reportService.updateReportReSub(request1);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("M_BDISB3 Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_MDISB5_ReportService BRRS_MDISB5_ReportService;

	@PostMapping("/MDISB5updateAll")
	@ResponseBody
	public ResponseEntity<String> updateMDISB5AllReports(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {

		try {
			System.out.println("Came to MDISB5 Detail Update Controller");

			BRRS_MDISB5_ReportService.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateMDISB5_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute MDISB5_Summary_Entity1 entity1,
			@ModelAttribute MDISB5_Summary_Entity2 entity2,
			@ModelAttribute MDISB5_Summary_Entity3 entity3,

			HttpServletRequest req) {

		try {
			System.out.println("Came to MDISB5 Resub Controller");

			if (asondate != null) {

				entity1.setReportDate(asondate);
				entity2.setReportDate(asondate);
				entity3.setReportDate(asondate);

				System.out.println("Set Report Date: " + asondate);

			} else {
				System.out.println("Using entity dates: "
						+ entity1.getReportDate());
			}

			// Call service → creates versioned archival copies
			BRRS_MDISB5_ReportService
					.updateReportReSub(entity1, entity2, entity3);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	/*
	 * 
	 * 
	 * @RequestMapping(value = "/updateMDISB5All", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateAllReports(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute MDISB5_Summary_Entity1 request1, @ModelAttribute
	 * MDISB5_Summary_Entity2 request2,
	 * 
	 * @ModelAttribute MDISB5_Summary_Entity3 request3
	 * 
	 * ) { try { System.out.println("Came to single controller"); // set date into
	 * all 4 entities request1.setReportDate(asondate);
	 * request2.setReportDate(asondate); request3.setReportDate(asondate);
	 * 
	 * 
	 * 
	 * // call services brrs_MDISB5_ReportService.updateReport1(request1);
	 * brrs_MDISB5_ReportService.updateReport2(request2);
	 * brrs_MDISB5_ReportService.updateReport3(request3);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Update Failed: " + e.getMessage()); } }
	 * 
	 * @RequestMapping(value = "/UpdateMDISB5_ReSub", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateReportReSubAll(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute MDISB5_Summary_Entity1 request1, @ModelAttribute
	 * MDISB5_Summary_Entity2 request2,
	 * 
	 * @ModelAttribute MDISB5_Summary_Entity3 request3, HttpServletRequest req) {
	 * 
	 * try { System.out.println("Came to MDISB5 Resub Controller");
	 * 
	 * if (asondate != null) { request1.setReportDate(asondate);
	 * System.out.println("🗓 Set Report Date: " + asondate); }
	 * 
	 * // ✅ Call service
	 * 
	 * brrs_MDISB5_ReportService.updateReportReSub(request1, request2, request3);
	 * 
	 * return ResponseEntity.ok("Resubmission Updated Successfully");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("MDISB5 Resubmission Update Failed: " + e.getMessage()); } }
	 */

	@Autowired
	private BRRS_M_CR_ReportService BRRS_M_CR_ReportService;

	@RequestMapping(value = "/updateCRAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllCRReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_CR_Summary_Entity request1) {
		try {
			System.out.println("Came to single controller");
			// set date into all 4 entities
			request1.setReport_date(asondate);
			// call services
			BRRS_M_CR_ReportService.updateReport(request1);
			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_SECL_ReportService SECLreportService;

	@RequestMapping(value = "/SECLupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SECL_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");
			// set date into all 4 entities
			request1.setReportDate(asondate);

			// call services
			SECLreportService.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_SECL_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SECL_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
			SECLreportService.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_OR2_ReportService OR2reportService;

	@RequestMapping(value = "/OR2updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_OR2_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");
			// set date into all 4 entities
			request1.setReportDate(asondate);

			// call services
			OR2reportService.updateReport(request1);

			return ResponseEntity.ok("Modifeid Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

//	@RequestMapping(value = "/UpdateM_OR2_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public ResponseEntity<String> updateReportReSub(
//			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
//			@ModelAttribute M_OR2_Summary_Entity request,
//			HttpServletRequest req) {
//
//		try {
//			System.out.println("Came to Resub Controller");
//
//			if (asondate != null) {
//				// Set the asondate into the entity
//				request.setReportDate(asondate);
//				System.out.println("Set Report Date: " + asondate);
//			} else {
//				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
//			}
//
//			// Call service to create a new versioned row
//			OR2reportService.updateReportReSub(request);
//
//			return ResponseEntity.ok("Resubmission Updated Successfully");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Resubmission Update Failed: " + e.getMessage());
//		}
//	}

	
	
	

	
	
	
	  @Autowired private BRRS_M_INT_RATES_ReportService INTRATESreportService;
	  
	  @RequestMapping(value = "/INTRATESupdateAll", method = { RequestMethod.GET,
	  RequestMethod.POST })
	  
	  @ResponseBody public ResponseEntity<String> updateAllReports(
	  
	  @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	  asondate,
	  
	  @ModelAttribute M_INT_RATES_Summary_Entity request1
	  
	  ) { try { System.out.println("Came to single controller"); // set date into all 4 entities
	  request1.setReportDate(asondate);
	  
	  // call services 
	  INTRATESreportService.updateReport(request1);
	  
	  return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	  e.printStackTrace(); return
	  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	  body("Update Failed: " + e.getMessage()); } }
	 
	
	
	
	@Autowired
	BRRS_M_INT_RATES_NEW_ReportService brrs_m_int_new_rates_reportservice;

	@RequestMapping(value = "/INTRATESNEWupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_INT_RATES_NEW_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");
			// set date into all 4 entities
			request1.setReport_date(asondate);

			// call services
			brrs_m_int_new_rates_reportservice.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}
	
	

	/*
	 * @RequestMapping(value = "/UpdateM_INTRATESReSub", method = {
	 * RequestMethod.GET, RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateReportReSub(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_INT_RATES_Summary_Entity request, HttpServletRequest req) {
	 * 
	 * try { System.out.println("Came to Resub Controller");
	 * 
	 * if (asondate != null) { // Set the asondate into the entity
	 * request.setReportDate(asondate); System.out.println("Set Report Date: " +
	 * asondate); } else {
	 * System.out.println("Asondate parameter is null; using entity value: " +
	 * request.getReportDate()); }
	 * 
	 * // Call service to create a new versioned row
	 * INTRATESreportService.updateReportReSub(request);
	 * 
	 * return ResponseEntity.ok("Resubmission Updated Successfully");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Resubmission Update Failed: " + e.getMessage()); } }
	 */

	@Autowired
	BRRS_M_CA7_ReportService M_CA7_ReportService;

	@RequestMapping(value = "/MCA7updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam(required = false) String type, @ModelAttribute M_CA7_Summary_Entity request1) {
		try {
			System.out.println("Came to single controller");
			System.out.println(type);
			// set date into all 4 entities
			request1.setReportDate(asondate);

			if (type.equals("ARCHIVAL")) {
				M_CA7_Archival_Summary_Entity Archivalrequest1 = new M_CA7_Archival_Summary_Entity();
				BeanUtils.copyProperties(request1, Archivalrequest1);
			} else {
				M_CA7_ReportService.updateReport(request1);
			}
			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_SFINP1_ReportService M_SFINP1_ReportService;

	@RequestMapping(value = "/MSFINP1updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_SFINP1_Summary_Manual_Entity request1) {
		try {
			System.out.println("Came to single controller");

			// set date into all 4 entities
			request1.setREPORT_DATE(asondate);

			M_SFINP1_ReportService.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_SEC_ReportService SECreportService;

	@RequestMapping(value = "/SECupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute BRRS_M_SEC_Summary_Entity1 request1,
			@ModelAttribute BRRS_M_SEC_Summary_Entity2 request2,
			@ModelAttribute BRRS_M_SEC_Summary_Entity3 request3,
			@ModelAttribute BRRS_M_SEC_Summary_Entity4 request4) {
		try {

			// set date into all 4 entities
			request1.setReportDate(asondate);
			request2.setReportDate(asondate);
			request3.setReportDate(asondate);
			request4.setReportDate(asondate);

			// call services
			SECreportService.updateReport(request1);
			SECreportService.updateReport1(request2);
			SECreportService.updateReport2(request3);
			SECreportService.updateReport3(request4);

			return ResponseEntity.ok("Modifeid Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

//	@RequestMapping(value = "/UpdateM_SEC_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public ResponseEntity<String> updateReportReSub(
//			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
//			@ModelAttribute BRRS_M_SEC_Summary_Entity1 request1,
//			@ModelAttribute BRRS_M_SEC_Summary_Entity2 request2,
//			@ModelAttribute BRRS_M_SEC_Summary_Entity3 request3,
//			@ModelAttribute BRRS_M_SEC_Summary_Entity4 request4,
//			HttpServletRequest req) {
//
//		try {
//			System.out.println("Came to Resub Controller");
//
//			if (asondate != null) {
//				// Set the asondate into the entity
//				request1.setReportDate(asondate);
//				request2.setReportDate(asondate);
//				request3.setReportDate(asondate);
//				request4.setReportDate(asondate);
//				System.out.println("Set Report Date: " + asondate);
//			} else {
//				System.out.println("Asondate parameter is null; using entity value: " + request1.getReportDate());
//				System.out.println("Asondate parameter is null; using entity value: " + request2.getReportDate());
//				System.out.println("Asondate parameter is null; using entity value: " + request3.getReportDate());
//				System.out.println("Asondate parameter is null; using entity value: " + request4.getReportDate());
//			}
//
//			// Call service to create a new versioned row
//			SECreportService.updateReport(request1);
//			SECreportService.updateReport2(request2);
//			SECreportService.updateReport3(request3);
//			SECreportService.updateReport4(request4);
//
//			return ResponseEntity.ok("Resubmission Updated Successfully");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Resubmission Update Failed: " + e.getMessage());
//		}
//	}

	
	  @Autowired private BRRS_M_INT_RATES_FCA_ReportService INTRATESFCAreportService;
	  
	  @RequestMapping(value = "/INTRATESFCAupdateAll", method = {
	  RequestMethod.GET, RequestMethod.POST })
	  
	  @ResponseBody public ResponseEntity<String> updateAllReports(
	  
	  @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	  asondate,
	  
	  @ModelAttribute M_INT_RATES_FCA_Summary_Entity request1
	  
	  ) { try { System.out.println("Came to single controller");
	  // set date into all  entities
	   request1.setReportDate(asondate);
	  
	  // call services 
	   INTRATESFCAreportService.updateReport(request1);
	  
	  return ResponseEntity.ok("Modified Successfully."); } catch (Exception e) {
	  e.printStackTrace(); return
	  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	  .body("Update Failed: " + e.getMessage()); } }
	 

	/*
	 * @Autowired private BRRS_M_INT_RATES_FCA_NEW_ReportService
	 * INTRATESFCACreportService;
	 */
	/*
	 * @RequestMapping(value = "/INTRATESFCACupdateAll", method = {
	 * RequestMethod.GET, RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateAllReports(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_INT_RATES_FCA_NEW_Summary_Entity request1
	 * 
	 * ) { try { System.out.println("Came to single controller"); // set date into
	 * all 4 entities request1.setReportDate(asondate);
	 * 
	 * // call services INTRATESFCACreportService.updateReport(request1);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Update Failed: " + e.getMessage()); } }
	 */
//	@Autowired
//	private BRRS_M_FXR_ReportService brrs_m_fxr_reportservice;
//
//	@RequestMapping(value = "/FXRupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public ResponseEntity<String> updateAllReports(
//			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
//			@ModelAttribute M_FXR_Summary_Entity1 request1, @ModelAttribute M_FXR_Summary_Entity2 request2,
//			@ModelAttribute M_FXR_Summary_Entity3 request3) {
//		try {
//			System.out.println("Came to single controller");
//			// set date into all 3 entities
//			request1.setReportDate(asondate);
//			request2.setReportDate(asondate);
//			request3.setReportDate(asondate);
//
//			// call services
//			brrs_m_fxr_reportservice.updateReport1(request1);
//			brrs_m_fxr_reportservice.updateReport2(request2);
//			brrs_m_fxr_reportservice.updateReport3(request3);
//			return ResponseEntity.ok("Updated Successfully.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
//		}
//	}
	@Autowired
	BRRS_M_SRWA_12F_ReportService M_SRWA_12FreportService;

	@RequestMapping(value = "/SRWA12FupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SRWA_12F_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("came to First controller");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			// ✅ set the asondate into entity
			request.setReportDate(asondate);

			M_SRWA_12FreportService.updateReport(request);
			return ResponseEntity.ok("Modified Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	/*
	 * @Autowired private BRRS_M_SRWA_12F_ReportService SRWA12FreportService;
	 * 
	 * @RequestMapping(value = "/SRWA12FupdateAll", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateAllReports(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_SRWA_12F_Summary_Entity request1
	 * 
	 * ) { try { System.out.println("Came to single controller"); // set date into
	 * all 4 entities request1.setReportDate(asondate);
	 * 
	 * // call services SRWA12FreportService.updateReport(request1);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Update Failed: " + e.getMessage()); } }
	 */
	/*
	 * @RequestMapping(value = "/UpdateM_SRWA_12F_ReSub", method = {
	 * RequestMethod.GET, RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateReportReSub(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_SRWA_12F_Summary_Entity request, HttpServletRequest req) {
	 * 
	 * try { System.out.println("Came to Resub Controller");
	 * 
	 * if (asondate != null) { // Set the asondate into the entity
	 * request.setReportDate(asondate); System.out.println("Set Report Date: " +
	 * asondate); } else {
	 * System.out.println("Asondate parameter is null; using entity value: " +
	 * request.getReportDate()); }
	 * 
	 * // Call service to create a new versioned row
	 * SRWA12FreportService.updateReportReSub(request);
	 * 
	 * return ResponseEntity.ok("Resubmission Updated Successfully");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Resubmission Update Failed: " + e.getMessage()); } }
	 */
	


	@Autowired
	private BRRS_M_EPR_ReportService brrs_m_epr_reportservice;

	@RequestMapping(value = "/M_EPRupdate", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_EPR_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);

			// call services
			brrs_m_epr_reportservice.updateReport(request);

			return ResponseEntity.ok("Modified Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}
	
	@Autowired
	private BRRS_M_LA2_ReportService BRRS_M_LA2_reportservice;

	@RequestMapping(value = "/M_LA2update", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_LA2_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setREPORT_DATE(asondate);

			// call services
			BRRS_M_LA2_reportservice.updateReport(request);
			

			return ResponseEntity.ok("Modified Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_CA3_ReportService BRRS_M_CA3_reportservice;

	@RequestMapping(value = "/M_CA3update", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_CA3_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setREPORT_DATE(asondate);

			// call services
			BRRS_M_CA3_reportservice.updateReport(request);
			BRRS_M_CA3_reportservice.updateReport2(request);
			BRRS_M_CA3_reportservice.updateReport3(request);
			BRRS_M_CA3_reportservice.updateReport4(request);
			BRRS_M_CA3_reportservice.updateReport5(request);
			BRRS_M_CA3_reportservice.updateReport6(request);

			return ResponseEntity.ok("Modified Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_CR_ReportService BRRS_M_CR_reportservice;

	@RequestMapping(value = "/M_CRupdate", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_CR_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);
			

			// call services
			BRRS_M_CR_reportservice.updateReport(request);

			return ResponseEntity.ok("Modified Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	/*
	 * @RequestMapping(value = "/UpdateM_EPR_ReSub", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateReportResub(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_EPR_Summary_Entity request, HttpServletRequest req) {
	 * 
	 * try { System.out.println("Came to Resub Controller");
	 * 
	 * if (asondate != null) { // Set the asondate into the entity
	 * request.setReport_date(asondate); System.out.println("Set Report Date: " +
	 * asondate); } else {
	 * System.out.println("Asondate parameter is null; using entity value: " +
	 * request.getReport_date()); }
	 * 
	 * // Call service to create a new versioned row
	 * brrs_m_epr_reportservice.updateReportResub(request);
	 * 
	 * return ResponseEntity.ok("Resubmission Updated Successfully");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Resubmission Update Failed: " + e.getMessage()); } }
	 */

	@Autowired
	BRRS_M_SRWA_12A_ReportService brrs_m_srwa_12a_reportservice;

	@RequestMapping(value = "/M_SRWA_12AupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_SRWA_12A_Summary_Entity1 request1, @ModelAttribute M_SRWA_12A_Summary_Entity2 request2,
			@ModelAttribute M_SRWA_12A_Summary_Entity3 request3, @ModelAttribute M_SRWA_12A_Summary_Entity4 request4,
			@ModelAttribute M_SRWA_12A_Summary_Entity5 request5, @ModelAttribute M_SRWA_12A_Summary_Entity6 request6,
			@ModelAttribute M_SRWA_12A_Summary_Entity7 request7) {
		try {
			System.out.println("Came to single controller");

			// set date into all 3 entities
			request1.setReport_date(asondate);
			request2.setReport_date(asondate);
			request3.setReport_date(asondate);
			request4.setReport_date(asondate);
			request5.setReport_date(asondate);
			request6.setReport_date(asondate);
			request7.setReport_date(asondate);

			// call services
			brrs_m_srwa_12a_reportservice.updateReport1(request1);
			brrs_m_srwa_12a_reportservice.updateReport2(request2);
			brrs_m_srwa_12a_reportservice.updateReport3(request3);
			brrs_m_srwa_12a_reportservice.updateReport4(request4);
			brrs_m_srwa_12a_reportservice.updateReport5(request5);
			brrs_m_srwa_12a_reportservice.updateReport6(request6);
			brrs_m_srwa_12a_reportservice.updateReport7(request7);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_SRWA_12A_New_ReportService brrs_M_SRWA_12A_New_reportservice;

	@RequestMapping(value = "/M_SRWA_12ANEWupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_SRWA_12A_New_Summary_Entity1 request1,
			@ModelAttribute M_SRWA_12A_NEW_Summary_Entity2 request2,
			@ModelAttribute M_SRWA_12A_NEW_Summary_Entity3 request3,
			@ModelAttribute M_SRWA_12A_NEW_Summary_Entity4 request4,
			@ModelAttribute M_SRWA_12A_NEW_Summary_Entity5 request5,
			@ModelAttribute M_SRWA_12A_NEW_Summary_Entity6 request6,
			@ModelAttribute M_SRWA_12A_NEW_Summary_Entity7 request7,
			@ModelAttribute M_SRWA_12A_NEW_Summary_Entity8 request8) {
		try {
			System.out.println("Came to single controller");

			// set date into all 3 entities
			request1.setReport_date(asondate);
			request2.setReport_date(asondate);
			request3.setReport_date(asondate);
			request4.setReport_date(asondate);
			request5.setReport_date(asondate);
			request6.setReport_date(asondate);
			request7.setReport_date(asondate);
			request8.setReport_date(asondate);

			// call services
			brrs_M_SRWA_12A_New_reportservice.updateReport1(request1);
			brrs_M_SRWA_12A_New_reportservice.updateReport2(request2);
			brrs_M_SRWA_12A_New_reportservice.updateReport3(request3);
			brrs_M_SRWA_12A_New_reportservice.updateReport4(request4);
			brrs_M_SRWA_12A_New_reportservice.updateReport5(request5);
			brrs_M_SRWA_12A_New_reportservice.updateReport6(request6);
			brrs_M_SRWA_12A_New_reportservice.updateReport7(request7);
			brrs_M_SRWA_12A_New_reportservice.updateReport8(request8);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/{reportid}/Detailspage", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView reportDetail(@PathVariable("reportid") String reportid,
			@RequestParam(value = "asondate", required = false) String asondate, HttpServletRequest request) {

		logger.info("Report Details request: reportId={}, asondate={}", reportid, asondate);
		return regulatoryReportServices.getReportDetails(reportid, request);
	}

	@RequestMapping(value = "/{reportid}/update", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<?> updateDetail(@PathVariable("reportid") String reportid, HttpServletRequest request) {
		return regulatoryReportServices.updateReportDetails(reportid, request);
	}

	@Autowired
	private BRRS_M_SRWA_12B_ReportService brrs_m_srwa_12b_reportservice;

	@RequestMapping(value = "/MSRWA12BupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_SRWA_12B_Summary_Entity1 request1, @ModelAttribute M_SRWA_12B_Summary_Entity2 request2,
			@ModelAttribute M_SRWA_12B_Summary_Entity3 request3, @ModelAttribute M_SRWA_12B_Summary_Entity4 request4,
			@ModelAttribute M_SRWA_12B_Summary_Entity5 request5, @ModelAttribute M_SRWA_12B_Summary_Entity6 request6,
			@ModelAttribute M_SRWA_12B_Summary_Entity7 request7

	) {
		try {
			System.out.println("Came to single controller");

			// set date into all 7 entities
			request1.setReportDate(asondate);
			request2.setReportDate(asondate);
			request3.setReportDate(asondate);
			request4.setReportDate(asondate);
			request5.setReportDate(asondate);
			request6.setReportDate(asondate);
			request7.setReportDate(asondate);

			// call services
			brrs_m_srwa_12b_reportservice.updateReport1(request1);
			brrs_m_srwa_12b_reportservice.updateReport2(request2);
			brrs_m_srwa_12b_reportservice.updateReport3(request3);
			brrs_m_srwa_12b_reportservice.updateReport4(request4);
			brrs_m_srwa_12b_reportservice.updateReport5(request5);
			brrs_m_srwa_12b_reportservice.updateReport6(request6);
			brrs_m_srwa_12b_reportservice.updateReport7(request7);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	// @RequestMapping(value = "/updateMLA1", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// @ResponseBody
	// public String updateMPLL(@ModelAttribute M_LA1_Detail_Entity Data) {
	// System.out.println("Came to Controller ");
	// System.out.println("Received update for ACCT_NO: " + Data.getAcct_number());
	// System.out.println("sanction value: " + Data.getSanction_limit());
	// System.out.println("balance value: " + Data.getAcct_balance_in_pula());
	//
	// boolean updated = brrs_M_LA1_ReportService.updateProvision(Data);
	//
	// if (updated) {
	// return "M_LA1 Detail updated successfully!";
	// } else {
	// return "Record not found for update!";
	// }
	// }
	//
	// @Autowired
	// private BRRS_M_LA1_Detail_Repo M_LA1_Detail_Repo;
	//
	// @RequestMapping(value = "/MLA1_Detail", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String showMLA1Detail(@RequestParam(required = false) String formmode,
	// @RequestParam(required = false) String acctNo, @RequestParam(required =
	// false) BigDecimal sanction_limit,
	// @RequestParam(required = false) BigDecimal acct_balance_in_pula, Model model)
	// {
	//
	// M_LA1_Detail_Entity la1Entity = M_LA1_Detail_Repo.findByAcctnumber(acctNo);
	//
	// if (la1Entity != null) {
	//
	// if (sanction_limit != null) {
	// la1Entity.setSanction_limit(sanction_limit);
	// }
	// if (acct_balance_in_pula != null) {
	// la1Entity.setAcct_balance_in_pula(acct_balance_in_pula);
	// }
	//
	// if (sanction_limit != null || acct_balance_in_pula != null) {
	// M_LA1_Detail_Repo.save(la1Entity);
	// System.out.println("Updated Sanction Limit / Account Balance for ACCT_NO: " +
	// acctNo);
	// }
	//
	// Date reportDate = la1Entity.getReport_date();
	// SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	// String formattedDate = formatter.format(reportDate);
	// model.addAttribute("asondate", formattedDate);
	// }
	//
	// model.addAttribute("displaymode", "edit");
	// model.addAttribute("formmode", "edit");
	// model.addAttribute("Data", la1Entity);
	//
	// return "BRRS/M_LA1";
	// }

	@Autowired
	private BRRS_Q_RLFA2_ReportService q_rlfa2_reportService;

	@RequestMapping(value = "/Q_RLFA2update", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute Q_RLFA2_Summary_Entity request) {
		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);

			// call services
			q_rlfa2_reportService.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateQ_RLFA2_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute Q_RLFA2_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReport_date(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReport_date());
			}

			// Call service to create a new versioned row
			q_rlfa2_reportService.updateReportResub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_Q_RLFA1_ReportService brrs_q_rlfa1_reportservice;

	@RequestMapping(value = "/Q_RLFA1update", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute Q_RLFA1_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);

			// call services
			brrs_q_rlfa1_reportservice.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateQ_RLFA1_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute Q_RLFA1_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReport_date(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReport_date());
			}

			// Call service to create a new versioned row
			brrs_q_rlfa1_reportservice.updateReportResub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_RPD_ReportService BRRS_M_RPD_ReportService;

	@RequestMapping(value = "/M_RPDupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport1(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_RPD_Summary_Entity1 request1, @ModelAttribute M_RPD_Summary_Entity2 request2,
			@ModelAttribute M_RPD_Summary_Entity3 request3, @ModelAttribute M_RPD_Summary_Entity4 request4,
			@ModelAttribute M_RPD_Summary_Entity5 request5, @ModelAttribute M_RPD_Summary_Entity6 request6,
			@ModelAttribute M_RPD_Summary_Entity7 request7, @ModelAttribute M_RPD_Summary_Entity8 request8,
			@ModelAttribute M_RPD_Summary_Entity9 request9

	) {

		try {
			System.out.println("came to single controller");
			request1.setReportDate(asondate);
			request2.setReportDate(asondate);
			request3.setReportDate(asondate);
			request4.setReportDate(asondate);
			request5.setReportDate(asondate);
			request6.setReportDate(asondate);
			request7.setReportDate(asondate);
			request8.setReportDate(asondate);
			request9.setReportDate(asondate);

			BRRS_M_RPD_ReportService.updateReport1(request1);
			BRRS_M_RPD_ReportService.updateReport2(request2);
			BRRS_M_RPD_ReportService.updateReport3(request3);
			BRRS_M_RPD_ReportService.updateReport4(request4);
			BRRS_M_RPD_ReportService.updateReport5(request5);
			BRRS_M_RPD_ReportService.updateReport6(request6);
			BRRS_M_RPD_ReportService.updateReport7(request7);
			BRRS_M_RPD_ReportService.updateReport8(request8);
			BRRS_M_RPD_ReportService.updateReport9(request9);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	
	@RequestMapping(value = "/updateReportMDISB1", method = { RequestMethod.GET, RequestMethod.POST })
	/*
	 * @ResponseBody
	 * 
	 * public ResponseEntity<String> updateAllReports(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute MDISB1_Summary_Entity_Manual request1
	 * 
	 * ) { try {
	 * 
	 * // set date into all 4 entities request1.setReport_date(asondate);
	 * 
	 * // call services BRRS_MDISB1_ReportService.updateReport(request1);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Update Failed: " + e.getMessage()); } }
	 */

	@PostMapping("/MDISB1updateAll")
	@ResponseBody
	public ResponseEntity<String> updateMDISB1(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {

		try {
			System.out.println("Came to MDISB1 controller");

			BRRS_MDISB1_ReportService.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed : " + e.getMessage());
		}
	}

	

	/*
	 * //@RequestMapping(value = "/updateReportLA2", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @PostMapping("/LA2updateAll")
	 * 
	 * @ResponseBody public ResponseEntity<String> updateLA2AllReports(
	 * 
	 * @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	 * 
	 * @RequestParam Map<String, String> allParams) { try {
	 * System.out.println("Came to LA2 controller");
	 * 
	 * LA2reportService.updateDetailFromForm(asondate, allParams);
	 * 
	 * return ResponseEntity.ok("Detail Updated Successfully"); } catch (Exception
	 * e) { e.printStackTrace(); return ResponseEntity
	 * .status(HttpStatus.INTERNAL_SERVER_ERROR) .body("Update Failed: " +
	 * e.getMessage()); } }
	 */
	
	@RequestMapping(value = "/updateReportMDISB2", method = { RequestMethod.GET, RequestMethod.POST })
	/*
	 * @ResponseBody
	 * 
	 * public ResponseEntity<String> updateAllReports(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute MDISB2_Summary_Entity_Manual request1
	 * 
	 * ) { try {
	 * 
	 * // set date into all 4 entities request1.setReport_date(asondate);
	 * 
	 * // call services BRRS_MDISB2_ReportService.updateReport(request1);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Update Failed: " + e.getMessage()); } }
	 */

	@PostMapping("/MDISB2updateAll")
	@ResponseBody
	public ResponseEntity<String> updateMDISB2(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {

		try {
			System.out.println("Came to MDISB2 controller");

			BRRS_MDISB2_ReportService.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed : " + e.getMessage());
		}
	}

	@RequestMapping(value = "/updateReportMDISB3", method = { RequestMethod.GET, RequestMethod.POST })
	/*
	 * @ResponseBody
	 * 
	 * public ResponseEntity<String> updateAllReports(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute MDISB3_Summary_Entity_Manual request1
	 * 
	 * ) { try {
	 * 
	 * // set date into all 4 entities request1.setReport_date(asondate);
	 * 
	 * // call services BRRS_MDISB3_ReportService.updateReport(request1);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Update Failed: " + e.getMessage()); } }
	 */

	@PostMapping("/MDISB3updateAll")
	@ResponseBody
	public ResponseEntity<String> updateMDISB3(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {

		try {
			System.out.println("Came to MDISB3 controller");

			BRRS_MDISB3_ReportService.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed : " + e.getMessage());
		}
	}

	
	@Autowired
	BRRS_SCH_17_ReportService brrs_sch_17_reportservice;

	/*
	 * @RequestMapping(value = "/SCH_17updateAll", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody
	 * public ResponseEntity<String> updateReport(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute SCH_17_Manual_Summary_Entity request) {
	 * 
	 * try {
	 * System.out.println("came to single controller");
	 * 
	 * // ? set the asondate into entity
	 * request.setReport_date(asondate);
	 * 
	 * // call services
	 * brrs_sch_17_reportservice.updateReport(request);
	 * 
	 * return ResponseEntity.ok("Updated Successfully.");
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Update Failed: " + e.getMessage());
	 * }
	 * }
	 */

	@PostMapping("/SCH_17updateAll")
	@ResponseBody
	public ResponseEntity<String> updateSCH17(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {

		try {
			System.out.println("Came to SCH-17 controller");

			brrs_sch_17_reportservice.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed : " + e.getMessage());
		}
	}

	@Autowired
	BRRS_FORMAT_II_ReportService brrs_FORMAT_II_reportservice;

	@RequestMapping(value = "/FORMAT_IIupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute FORMAT_II_Manual_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ? set the asondate into entity
			request.setReport_date(asondate);

			// call services
			brrs_FORMAT_II_reportservice.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_Q_SMME_DEP_ReportService BRRS_Q_SMME_DEP_ReportService;

	@RequestMapping(value = "/Q_SMME_DEPupdate", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute Q_SMME_DEP_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ? set the asondate into entity
			request.setReport_date(asondate);

			// call services
			BRRS_Q_SMME_DEP_ReportService.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateQ_SMME_DEP_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute Q_SMME_DEP_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReport_date(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReport_date());
			}

			// Call service to create a new versioned row
			BRRS_Q_SMME_DEP_ReportService.updateReportResub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_BOP_ReportService BRRS_M_BOP_ReportService;

	@RequestMapping(value = "/M_BOPupdate", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_BOP_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ? set the asondate into entity
			request.setReport_date(asondate);

			// call services
			BRRS_M_BOP_ReportService.updateReport(request);

			return ResponseEntity.ok("Modified Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	/*
	 * @RequestMapping(value = "/UpdateM_BOP_ReSub", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateReportReSub(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute M_BOP_Summary_Entity request, HttpServletRequest req) {
	 * 
	 * try { System.out.println("Came to Resub Controller");
	 * 
	 * if (asondate != null) { // Set the asondate into the entity
	 * request.setReport_date(asondate); System.out.println("Set Report Date: " +
	 * asondate); } else {
	 * System.out.println("Asondate parameter is null; using entity value: " +
	 * request.getReport_date()); }
	 * 
	 * // Call service to create a new versioned row
	 * BRRS_M_BOP_ReportService.updateReportResub(request);
	 * 
	 * return ResponseEntity.ok("Resubmission Updated Successfully");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Resubmission Update Failed: " + e.getMessage()); } }
	 */

	@Autowired
	BRRS_M_SECA_ReportService BRRS_M_SECA_reportservice;

	@RequestMapping(value = "/MSECAupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SECA_Manual_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);

			// call services
			BRRS_M_SECA_reportservice.updateReport1(request);

			return ResponseEntity.ok("Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	// @RequestMapping(value = "/updateMLA4", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// @ResponseBody
	// public String updateMLA4(@ModelAttribute M_LA4_Detail_Entity Data) {
	// System.out.println("Came to Controller ");
	// System.out.println("Received update for ACCT_NO: " + Data.getAcct_number());
	// // System.out.println("sanction value: " + Data.getSanction_limit());
	// System.out.println("balance value: " + Data.getAcct_balance_in_pula());
	//
	// boolean updated = brrs_M_LA4_ReportService.updateProvision(Data);
	//
	// if (updated) {
	// return "M_LA4 Detail updated successfully!";
	// } else {
	// return "Record not found for update!";
	// }
	// }
	//
	// @Autowired
	// private BRRS_M_LA4_Detail_Repo M_LA4_Detail_Repo;
	//
	// @RequestMapping(value = "/MLA4_Detail", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String showMLA4Detail(@RequestParam(required = false) String formmode,
	// @RequestParam(required = false) String acctNo, @RequestParam(required =
	// false) BigDecimal sanction_limit,
	// @RequestParam(required = false) BigDecimal acct_balance_in_pula, Model model)
	// {
	//
	// M_LA4_Detail_Entity la4Entity = M_LA4_Detail_Repo.findByAcctnumber(acctNo);
	//
	// if (la4Entity != null) {
	//
	// if (acct_balance_in_pula != null) {
	// la4Entity.setAcct_balance_in_pula(acct_balance_in_pula);
	// }
	//
	// if (sanction_limit != null || acct_balance_in_pula != null) {
	// M_LA4_Detail_Repo.save(la4Entity);
	// System.out.println("Updated Sanction Limit / Account Balance for ACCT_NO: " +
	// acctNo);
	// }
	//
	// Date reportDate = la4Entity.getReport_date();
	// SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	// String formattedDate = formatter.format(reportDate);
	// model.addAttribute("asondate", formattedDate);
	// }
	//
	// model.addAttribute("displaymode", "edit");
	// model.addAttribute("formmode", "edit");
	// model.addAttribute("Data", la4Entity);
	//
	// return "BRRS/M_LA4";
	// }
	//
	//

	// Resubmission

	@Autowired
	private BRRS_M_SRWA_12H_ReportService M_SRWA_12Hservice;

	@RequestMapping(value = "/UpdateM_SRWA_12H", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SRWA_12H_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("came to First controller");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			// ✅ set the asondate into entity
			request.setReportDate(asondate);

			M_SRWA_12Hservice.updateReport(request);
			return ResponseEntity.ok("Modified Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_SRWA_12H_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SRWA_12H_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
			// M_SRWA_12Hservice.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}
	
	@Autowired
	BRRS_Q_BRANCHNET_ReportService QBRANCHNET_service;

	@RequestMapping(value = "/Q_BRANCHNETupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute Q_BRANCHNET_Summary_Entity request1) {
		try {
			System.out.println("Came to single controller");

			// set date into all 3 entities
			request1.setReportDate(asondate);

			// call services
			QBRANCHNET_service.QBranchnetUpdate1(request1);
			QBRANCHNET_service.QBranchnetUpdate2(request1);
			QBRANCHNET_service.QBranchnetUpdate3(request1);
			QBRANCHNET_service.QBranchnetUpdate4(request1);

			return ResponseEntity.ok("Modified Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}
//	@RequestMapping(value = "/UpdateQ_BRANCHNETReSub", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public ResponseEntity<String> updateReportReSub(
//			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
//			@ModelAttribute Q_BRANCHNET_Summary_Entity1 request1,
//			@ModelAttribute Q_BRANCHNET_Summary_Entity2 request2,
//			@ModelAttribute Q_BRANCHNET_Summary_Entity3 request3,
//			@ModelAttribute Q_BRANCHNET_Summary_Entity4 request4,
//			HttpServletRequest req) {
//
//		try {
//			System.out.println("Came to Q_BRANCHNET Resub Controller");
//
//			if (asondate != null) {
//				request1.setReportDate(asondate);
//				request2.setReportDate(asondate);
//				request3.setReportDate(asondate);
//				request4.setReportDate(asondate);
//				System.out.println("Set Report Date: " + asondate);
//			}
//
//			// Call service
//			// QBRANCHNET_service.updateReportReSub(request1, request2, request3, request4);
//
//			return ResponseEntity.ok("Resubmission Updated Successfully");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Q_BRANCHNET Resubmission Update Failed: " + e.getMessage());
//		}
//	}

	@Autowired
	BRRS_M_IS_ReportService M_IS_Service;

	@RequestMapping(value = "/M_ISupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_IS_Summary_Entity1 request1,
			@ModelAttribute M_IS_Summary_Entity2 request2) {
		try {
			System.out.println("Came to single controller");

			// set date into all 3 entities
			request1.setReportDate(asondate);
			request2.setReportDate(asondate);

			// call services
			M_IS_Service.MISUpdate1(request1);
			M_IS_Service.MISUpdate2(request2);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_ISReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_IS_Summary_Entity1 request1,
			@ModelAttribute M_IS_Summary_Entity2 request2,
			HttpServletRequest req) {

		try {
			System.out.println("Came to M_IS Resub Controller");

			if (asondate != null) {
				request1.setReportDate(asondate);
				request2.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			}

			// Call service
			M_IS_Service.updateReportReSub(request1, request2);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("M_IS Resubmission Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_SRWA_12G_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SRWA_12G_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReport_date(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReport_date());
			}

			// Call service to create a new versioned row
			brrs_m_srwa_12g_reportservice.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_OB_ReportService BRRS_M_OB_reportservice;

	@RequestMapping(value = "/MOBupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_OB_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReportDate(asondate);

			// call services
			BRRS_M_OB_reportservice.updateReport1(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_OB_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_OB_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
			BRRS_M_OB_reportservice.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_TBS_ReportService BRRS_M_TBS_ReportService;

	@RequestMapping(value = "/M_TBSupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport1(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_TBS_Summary_Entity request

	) {

		try {
			System.out.println("came to single controller");
			request.setReportDate(asondate);

			BRRS_M_TBS_ReportService.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_CA7_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_CA7_Summary_Entity request1,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				request1.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request1.getReportDate());
			}

			// Call service to create a new versioned row
			M_CA7_ReportService.updateReportReSub(request1);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_GP_ReportService BRRS_M_GP_ReportService;

	@RequestMapping(value = "/M_GPupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport1(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_GP_Summary_Entity request

	) {

		try {
			System.out.println("came to single controller");
			request.setReportDate(asondate);

			BRRS_M_GP_ReportService.updateReport(request);

			return ResponseEntity.ok("Modified Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

//	@RequestMapping(value = "/UpdateM_GP_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public ResponseEntity<String> updateReportReSub(
//			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
//			@ModelAttribute M_GP_Summary_Entity request,
//			HttpServletRequest req) {
//
//		try {
//			System.out.println("Came to Resub Controller");
//
//			if (asondate != null) {
//				// Set the asondate into the entity
//				request.setReportDate(asondate);
//				System.out.println("Set Report Date: " + asondate);
//			} else {
//				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
//			}
//
//			// Call service to create a new versioned row
//			BRRS_M_GP_ReportService.updateReportReSub(request);
//
//			return ResponseEntity.ok("Resubmission Updated Successfully");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Resubmission Update Failed: " + e.getMessage());
//		}
//	}

	@Autowired
	private BRRS_M_LARADV_ReportService LARADVreportService;

	@RequestMapping(value = "/LARADVupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_LARADV_Summary_Entity1 request1,
			@ModelAttribute M_LARADV_Summary_Entity2 request2,
			@ModelAttribute M_LARADV_Summary_Entity3 request3,
			@ModelAttribute M_LARADV_Summary_Entity4 request4,
			@ModelAttribute M_LARADV_Summary_Entity5 request5) {
		try {

			// set date into all 4 entities
			request1.setReportDate(asondate);
			request2.setReportDate(asondate);
			request3.setReportDate(asondate);
			request4.setReportDate(asondate);
			request5.setReportDate(asondate);

			// call services
			LARADVreportService.updateReport(request1);
			LARADVreportService.updateReport2(request2);
			LARADVreportService.updateReport3(request3);
			LARADVreportService.updateReport4(request4);
			LARADVreportService.updateReport5(request5);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateLARADV_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_LARADV_Summary_Entity1 request1,
			@ModelAttribute M_LARADV_Summary_Entity2 request2,
			@ModelAttribute M_LARADV_Summary_Entity3 request3,
			@ModelAttribute M_LARADV_Summary_Entity4 request4,
			@ModelAttribute M_LARADV_Summary_Entity5 request5,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Q_BRANCHNET Resub Controller");

			if (asondate != null) {
				request1.setReportDate(asondate);
				request2.setReportDate(asondate);
				request3.setReportDate(asondate);
				request4.setReportDate(asondate);
				request5.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			}

			// Call service
			LARADVreportService.updateReportReSub(request1, request2, request3, request4, request5);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Q_LARADV Resubmission Update Failed: " + e.getMessage());
		}
	}

//	@RequestMapping(value = "/UpdateM_FXR_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public ResponseEntity<String> updateReportReSubAll(
//			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
//			@ModelAttribute M_FXR_Summary_Entity1 request1,
//			@ModelAttribute M_FXR_Summary_Entity2 request2,
//			@ModelAttribute M_FXR_Summary_Entity3 request3,
//			HttpServletRequest req) {
//
//		try {
//			System.out.println("Came to M_FXR Resub Controller");
//
//			if (asondate != null) {
//				request1.setReportDate(asondate);
//				request2.setReportDate(asondate);
//				request3.setReportDate(asondate);
//				System.out.println("🗓 Set Report Date: " + asondate);
//			}
//
//			// ✅ Call service
//			brrs_m_fxr_reportservice.updateReportReSub(request1, request2, request3);
//
//			return ResponseEntity.ok("Resubmission Updated Successfully");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("M_FXR Resubmission Update Failed: " + e.getMessage());
//		}
//	}

	@Autowired
	private com.bornfire.brrs.services.BRRS_M_SRWA_12E_ReportService BRRS_M_SRWA_12E_ReportService;

	@RequestMapping(value = "/updateSRWA12EAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllSRWA12EReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute BRRS_M_SRWA_12E_LTV_Summary_Entity request1) {
		try {
			System.out.println("Came to single controller");
			// set date into all 4 entities
			request1.setReportDate(asondate);
			// call services
			BRRS_M_SRWA_12E_ReportService.updateReport(request1);

			return ResponseEntity.ok(" Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_SRWA_12E_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute BRRS_M_SRWA_12E_LTV_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
			BRRS_M_SRWA_12E_ReportService.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_CA6_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(

			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date asondate, // ✅ ISO format
			@RequestParam(required = false) String type, @ModelAttribute M_CA6_Summary_Entity2 request1,
			@ModelAttribute M_CA6_Summary_Entity1 request2,
			HttpServletRequest req) {

		try {
			System.out.println("Came to M_CA6 Resub Controller");

			if (asondate != null) {
				request1.setReportDate(asondate);
				request2.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			}

			// Call service
			BRRS_M_CA6_ReportService.updateReportReSub(request2, request1);
			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("M_CA6 Resubmission Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_SIR_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SIR_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
//			BRRS_M_SIR_ReportService.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_RPD_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_RPD_Summary_Entity1 request1,
			@ModelAttribute M_RPD_Summary_Entity2 request2,
			@ModelAttribute M_RPD_Summary_Entity3 request3,
			@ModelAttribute M_RPD_Summary_Entity4 request4,
			@ModelAttribute M_RPD_Summary_Entity5 request5,

			@ModelAttribute M_RPD_Summary_Entity6 request6,
			@ModelAttribute M_RPD_Summary_Entity7 request7,
			@ModelAttribute M_RPD_Summary_Entity8 request8,
			@ModelAttribute M_RPD_Summary_Entity9 request9,
			HttpServletRequest req) {

		try {
			System.out.println("Came to RPD Resub Controller");

			if (asondate != null) {
				request1.setReportDate(asondate);
				request2.setReportDate(asondate);
				request3.setReportDate(asondate);
				request4.setReportDate(asondate);
				request5.setReportDate(asondate);
				request6.setReportDate(asondate);
				request7.setReportDate(asondate);
				request8.setReportDate(asondate);
				request9.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			}

			// Call service
			BRRS_M_RPD_ReportService.updateReportReSub(request1, request2, request3, request4, request5, request6,
					request7, request8, request9);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("M_RPD Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_GMIRT_ReportService brrs_m_gmirt_reportservice;

	@RequestMapping(value = "/M_GMIRTupdate", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
	        @RequestParam(required = false)
	        @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	        @ModelAttribute M_GMIRT_Summary_Entity summary,
	        @ModelAttribute M_GMIRT_Detail_Entity detail) {

	    summary.setReport_date(asondate);
	    detail.setReport_date(asondate);

	    System.out.println("came to GMIRT MODIFY CONTROLLER");
	    brrs_m_gmirt_reportservice.updateReport(summary);
	    brrs_m_gmirt_reportservice.updateDetail(detail);

	    return ResponseEntity.ok("Updated Successfully");
	}


	@RequestMapping(value = "/UpdateM_TBS_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_TBS_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
			BRRS_M_TBS_ReportService.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateM_SRWA_12B_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSubAll(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SRWA_12B_Summary_Entity1 request1,
			@ModelAttribute M_SRWA_12B_Summary_Entity2 request2,
			@ModelAttribute M_SRWA_12B_Summary_Entity3 request3,
			@ModelAttribute M_SRWA_12B_Summary_Entity4 request4,
			@ModelAttribute M_SRWA_12B_Summary_Entity5 request5,
			@ModelAttribute M_SRWA_12B_Summary_Entity6 request6,
			@ModelAttribute M_SRWA_12B_Summary_Entity7 request7,
			HttpServletRequest req) {

		try {
			System.out.println("Came to M_SRWA_!2B Resub Controller");

			if (asondate != null) {
				request1.setReportDate(asondate);
				request2.setReportDate(asondate);
				request3.setReportDate(asondate);
				request4.setReportDate(asondate);
				request5.setReportDate(asondate);
				request6.setReportDate(asondate);
				request7.setReportDate(asondate);

				System.out.println("🗓 Set Report Date: " + asondate);
			}

			// ✅ Call service
			brrs_m_srwa_12b_reportservice.updateReportReSub(request1, request2, request3, request4,
					request5, request6, request7);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("M_SRWA_12B Resubmission Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/NOSVOSupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> NOSVOSupdateAll(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute BrrsMNosvosP1 request1, @ModelAttribute BrrsMNosvosP2 request2,
			@ModelAttribute BrrsMNosvosP3 request3,
			@ModelAttribute BrrsMNosvosP4 request4) {
		try {

			// set date into all 4 entities
			request1.setREPORT_DATE(asondate);
			request2.setREPORT_DATE(asondate);
			request3.setREPORT_DATE(asondate);
			request4.setREPORT_DATE(asondate);

			// call services
			BRRS_M_NOSVOS_ReportService.updateReport(request1);
			BRRS_M_NOSVOS_ReportService.updateReport2(request2);
			BRRS_M_NOSVOS_ReportService.updateReport3(request3);
			BRRS_M_NOSVOS_ReportService.updateReport4(request4);

			return ResponseEntity.ok("All Reports Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_GALOR_ReportService m_galor_ReportService;

	@RequestMapping(value = "/_M_GALORupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_GALOR_Manual_Summary_Entity request1) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setReport_date(asondate);

			// call services
			m_galor_ReportService.updateReport(request1);

			return ResponseEntity.ok(" Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_DEP3_ReportService BRRS_M_DEP3_reportservice;

	@RequestMapping(value = "/MDEP3updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_DEP3_Manual_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setReport_date(asondate);

			// call services
			BRRS_M_DEP3_reportservice.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_PD_ReportService BRRS_M_PD_reportservice;

	@RequestMapping(value = "/MPDupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_PD_Manual_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setReport_date(asondate);

			// call services
			BRRS_M_PD_reportservice.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	private BRRS_M_PI_ReportService BRRS_M_PI_reportservice;

	@RequestMapping(value = "/MPIupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			@ModelAttribute M_PI_Manual_Summary_Entity request1

	) {
		try {
			System.out.println("Came to single controller");

			// set date into entities
			request1.setREPORT_DATE(asondate);

			// call services
			BRRS_M_PI_reportservice.updateReport(request1);

			return ResponseEntity.ok("Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	// @GetMapping("/downloadExcel1")
	// public void downloadExcel(@RequestParam String asondate,
	// @RequestParam String fromdate,
	// @RequestParam String todate,
	// @RequestParam String currency,
	// @RequestParam String type,
	// @RequestParam String version,
	// @RequestParam String filename,
	// HttpServletResponse response) throws IOException, ParseException {

	// byte[] file = regreportServices.getConsolidatedDownloadFile(filename,
	// asondate, fromdate, todate, currency, type, version);

	// response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	// response.setHeader("Content-Disposition", "attachment; filename=" + filename
	// + ".xlsx");
	// response.getOutputStream().write(file);
	// response.getOutputStream().flush();
	// }

	// @RequestMapping(value = "downloadConsolidatedExcel", method = {
	// RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	// public ResponseEntity<ByteArrayResource> downloadConsolidatedExcel(
	// @RequestParam("asondate") String asondate,
	// @RequestParam("fromdate") String fromdate,
	// @RequestParam("todate") String todate,
	// @RequestParam("currency") String currency,
	// @RequestParam(value = "type", required = false) String type,
	// @RequestParam(value = "version", required = false) String version) {

	// byte[] excelData = regreportServices.generateConsolidatedExcel(asondate,
	// fromdate, todate, currency, type, version);

	// if (excelData == null || excelData.length == 0)
	// return ResponseEntity.noContent().build();

	// ByteArrayResource resource = new ByteArrayResource(excelData);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;
	// filename=Consolidated_Report.xlsx");

	// return ResponseEntity.ok()
	// .headers(headers)
	// .contentLength(excelData.length)
	// .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	// .body(resource);
	// }

	@RequestMapping(value = "downloaddetailpdf", method = { RequestMethod.GET, RequestMethod.POST })
	public void detailDownloadpdf(HttpServletResponse response,
			@RequestParam("reportid") String reportid,
			@RequestParam("asondate") String asondate,
			@RequestParam("fromdate") String fromdate,
			@RequestParam("todate") String todate,
			@RequestParam("currency") String currency,
			@RequestParam(value = "subreportid", required = false) String subreportid,
			@RequestParam(value = "secid", required = false) String secid,
			@RequestParam(value = "dtltype", required = false) String dtltype,
			@RequestParam(value = "reportingTime", required = false) String reportingTime,
			@RequestParam(value = "filename", required = false) String filename,
			@RequestParam(value = "instancecode", required = false) String instancecode,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "version", required = false) BigDecimal version)
			throws SQLException, FileNotFoundException {
		System.out.println("Control");

		try {
			byte[] pdfBytes = regreportServices.getPdfDownloadFile(
					reportid, filename, asondate, fromdate, todate,
					currency, subreportid, secid, dtltype,
					reportingTime, instancecode, filter, type, version);

			// Write PDF to response
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"report.pdf\"");
			response.setContentLength(pdfBytes.length);

			try (ServletOutputStream out = response.getOutputStream()) {
				out.write(pdfBytes);
				out.flush();
			}
		} catch (Exception e) {
			logger.error("Controller ERROR: A critical error occurred during file generation.", e);
		}
	}

	@Autowired
	BRRS_M_DEP4_ReportService BRRS_M_DEP4_ReportService;

	@RequestMapping(value = "/M_DEP4_updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport1(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_DEP4_Summary_Entity1 request1,
			@ModelAttribute M_DEP4_Summary_Entity2 request2,
			@ModelAttribute M_DEP4_Summary_Entity3 request3,
			@ModelAttribute M_DEP4_Summary_Entity4 request4,
			@ModelAttribute M_DEP4_Summary_Entity5 request5,
			@ModelAttribute M_DEP4_Summary_Entity6 request6

	) {

		try {
			System.out.println("came to single controller");
			request1.setReportDate(asondate);
			request2.setReportDate(asondate);
			request3.setReportDate(asondate);
			request4.setReportDate(asondate);
			request5.setReportDate(asondate);
			request6.setReportDate(asondate);

			BRRS_M_DEP4_ReportService.updateReport1(request1);
			BRRS_M_DEP4_ReportService.updateReport2(request2);
			BRRS_M_DEP4_ReportService.updateReport3(request3);
			BRRS_M_DEP4_ReportService.updateReport4(request4);
			BRRS_M_DEP4_ReportService.updateReport5(request5);
			BRRS_M_DEP4_ReportService.updateReport6(request6);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/DEP4_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_DEP4_Summary_Entity1 request1,
			@ModelAttribute M_DEP4_Summary_Entity2 request2,
			@ModelAttribute M_DEP4_Summary_Entity3 request3,
			@ModelAttribute M_DEP4_Summary_Entity4 request4,
			@ModelAttribute M_DEP4_Summary_Entity5 request5,
			@ModelAttribute M_DEP4_Summary_Entity6 request6, HttpServletRequest req) {

		try {
			System.out.println("Came to M_SRWA_!2B Resub Controller");

			if (asondate != null) {
				request1.setReportDate(asondate);
				request2.setReportDate(asondate);
				request3.setReportDate(asondate);
				request4.setReportDate(asondate);
				request5.setReportDate(asondate);
				request6.setReportDate(asondate);

				System.out.println("🗓 Set Report Date: " + asondate);
			}

			// ✅ Call service
			BRRS_M_DEP4_ReportService.updateReportReSub(request1, request2, request3, request4,
					request5, request6);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("M_DEP4 Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_M_FAS_ReportService brrs_M_FAS_reportservice;

	@RequestMapping(value = "/M_FASupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_FAS_Manual_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ? set the asondate into entity
			request.setReport_date(asondate);

			// call services
			brrs_M_FAS_reportservice.updateReport1(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	/*
	 * @Autowired BRRS_BDISB2_ReportService BRRS_BDISB2_ReportService;
	 * 
	 * @RequestMapping(value = "/BDISB2updateAll", method = { RequestMethod.GET,
	 * RequestMethod.POST })
	 * 
	 * @ResponseBody public ResponseEntity<String> updateReport(
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date
	 * asondate,
	 * 
	 * @ModelAttribute BDISB2_Summary_Entity request) {
	 * 
	 * try { System.out.println("came to single controller");
	 * 
	 * // ✅ set the asondate into entity request.setReportDate(asondate);
	 * 
	 * // call services BRRS_BDISB2_ReportService.updateReport(request);
	 * 
	 * return ResponseEntity.ok("Updated Successfully."); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Update Failed: " + e.getMessage()); } }
	 */
	@Autowired
	BRRS_BDISB2_ReportService BRRS_BDISB2_ReportService;

	@PostMapping("/BDISB2updateAll")
	@ResponseBody
	public ResponseEntity<String> updateBDISB2AllReports(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@RequestParam Map<String, String> allParams) {
		try {
			System.out.println("Came to BDISB2 controller");

			BRRS_BDISB2_ReportService.updateDetailFromForm(asondate, allParams);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/UpdateBDISB2_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReportReSub(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute BDISB2_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("Came to Resub Controller");

			if (asondate != null) {
				// Set the asondate into the entity
				request.setReportDate(asondate);
				System.out.println("Set Report Date: " + asondate);
			} else {
				System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
			}

			// Call service to create a new versioned row
			BRRS_BDISB2_ReportService.updateReportReSub(request);

			return ResponseEntity.ok("Resubmission Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Resubmission Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_ADISB1_ReportService BRRS_ADISB1_ReportService;

	@RequestMapping(value = "/ADISB1updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute ADISB1_Manual_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);

			// call services
			BRRS_ADISB1_ReportService.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_PL_SCHS_ReportService BRRS_PL_SCHS_ReportService;

	@RequestMapping(value = "/PL_SCHSupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute PL_SCHS_Manual_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setReport_date(asondate);

			// call services
			BRRS_PL_SCHS_ReportService.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@Autowired
	BRRS_GL_SCH_ReportService brrs_gl_sch_reportservice;

	@RequestMapping(value = "/GL_SCHupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute GL_SCH_Manual_Summary_Entity request) {

		try {
			System.out.println("came to single controller");

			// ✅ set the asondate into entity
			request.setREPORT_DATE(asondate);

			// call services
			brrs_gl_sch_reportservice.updateReport(request);

			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
		}
	}

	@GetMapping("/downloadConsolidatedExcel")
	public void downloadConsolidatedExcel(@RequestParam(required = false) String asondate,
			@RequestParam(required = false) String fromdate,
			@RequestParam(required = false) String todate,
			@RequestParam(required = false) String currency,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) BigDecimal version,
			@RequestParam(required = false) String filename,
			@RequestParam(required = false) String dtltype,
			HttpServletResponse response) throws IOException, ParseException {
		System.out.println("SerdownloadConsolidatedExcelvice: Generating report ");
		byte[] file = regreportServices.getConsolidatedDownloadFile(filename, asondate, fromdate, todate, currency,
				type, version, dtltype);
		System.out.println("filename..."+filename);

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".xlsx\"");
		response.getOutputStream().write(file);
		response.getOutputStream().flush();
	}

//	@Autowired
//
//	BRRS_M_SIR_ReportService BRRS_M_SIR_ReportService;
//
//	@RequestMapping(value = "/MSIRupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public ResponseEntity<String> updateAllReports(
//			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
//			@RequestParam(required = false) String type, @ModelAttribute M_SIR_Summary_Entity request1) {
//		try {
//			System.out.println("Came to single controller");
//			System.out.println(type);
//			// set date into all 4 entities
//			request1.setReportDate(asondate);
//
//			if (type.equals("ARCHIVAL")) {
//				M_SIR_Archival_Summary_Entity Archivalrequest1 = new M_SIR_Archival_Summary_Entity();
//				BeanUtils.copyProperties(request1, Archivalrequest1);
//			} else {
//				BRRS_M_SIR_ReportService.updateReport(request1);
//			}
//			return ResponseEntity.ok("Updated Successfully.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Failed: " + e.getMessage());
//		}
//	}
	@Autowired
	BRRS_M_SIR_ReportService BRRS_M_SIR_ReportService;

	@RequestMapping(value = "/MSIRupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateAllReports(

	        @RequestParam(required = false)
	        @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

	        @ModelAttribute M_SIR_Summary_Entity request1) {

	    try {
	        System.out.println("Came to single controller");

	        request1.setReportDate(asondate);

	        BRRS_M_SIR_ReportService.updateReport(request1);

	        return ResponseEntity.ok("Modified Successfully.");

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Update Failed: " + e.getMessage());
	    }
	}
	
@Autowired
BRRS_Q_STAFF_Report_Service QSTAFF_service;

@RequestMapping(value = "/Q_STAFFupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
@ResponseBody
public ResponseEntity<String> updateAllReports(

        @RequestParam(required = false)
        @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

        @ModelAttribute Q_STAFF_Summary_Entity request1) {

    try {
        System.out.println("Came to single controller");

        request1.setReportDate(asondate);

        QSTAFF_service.updateReport(request1);
        QSTAFF_service.updateReport2(request1);
        QSTAFF_service.updateReport3(request1);

        return ResponseEntity.ok("Modified Successfully.");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Update Failed: " + e.getMessage());
    }
}

@Autowired
BRRS_M_FXR_ReportService M_FXR_service;

@RequestMapping(value = "/FXRupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
@ResponseBody
public ResponseEntity<String> updateAllReports(

        @RequestParam(required = false)
        @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

        @ModelAttribute M_FXR_Summary_Entity request1) {

    try {
        System.out.println("Came to single controller");

        request1.setReportDate(asondate);

        M_FXR_service.updateReport1(request1);
        M_FXR_service.updateReport2(request1);
        M_FXR_service.updateReport3(request1);

        return ResponseEntity.ok("Modified Successfully.");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Update Failed: " + e.getMessage());
    }
}

@Autowired
BRRS_Q_STAFF_New_Report_Service QSTAFF_New_service;

@RequestMapping(value = "/Q_STAFFNewupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
@ResponseBody
public ResponseEntity<String> updateAllReportsnew(

        @RequestParam(required = false)
        @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

        @ModelAttribute Q_STAFF_New_Summary_Entity1 request1,
        @ModelAttribute Q_STAFF_New_Summary_Entity2 request2,
        @ModelAttribute Q_STAFF_New_Summary_Entity3 request3) {

    try {
        System.out.println("Came to single controller");

        request1.setReportDate(asondate);
        request2.setReportDate(asondate);
        request3.setReportDate(asondate);

        QSTAFF_New_service.updateReport(request1);
        QSTAFF_New_service.updateReport2(request2);
        QSTAFF_New_service.updateReport3(request3);

        return ResponseEntity.ok("Updated Successfully.");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Update Failed: " + e.getMessage());
    }
}


// @RequestMapping(value = "/UpdateQ_STAFF_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
// @ResponseBody
// public ResponseEntity<String> updateReportReSubAll(

//         @RequestParam(required = false)
//         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

//         @ModelAttribute Q_STAFF_Summary_Entity1 request1,
//         @ModelAttribute Q_STAFF_Summary_Entity2 request2,
//         @ModelAttribute Q_STAFF_Summary_Entity3 request3,
//         HttpServletRequest req) {

//     try {
//         System.out.println("Came to Q_STAFF Resub Controller");

//         if (asondate != null) {
//             request1.setReportDate(asondate);
//             request2.setReportDate(asondate);
//             request3.setReportDate(asondate);
//             System.out.println("Set Report Date: " + asondate);
//         }

//         QSTAFF_service.updateReportReSub(request1, request2, request3);

//         return ResponseEntity.ok("Resubmission Updated Successfully");

//     } catch (Exception e) {
//         e.printStackTrace();
//         return ResponseEntity
//                 .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                 .body("Q_STAFF Resubmission Update Failed: " + e.getMessage());
//     }
// }
@Autowired
	private BRRS_M_SRWA_12H_New_ReportService M_SRWA_12HNewservice;

	@RequestMapping(value = "/UpdateM_SRWA_12HNew", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> updateReport(
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			@ModelAttribute M_SRWA_12H_New_Summary_Entity request,
			HttpServletRequest req) {

		try {
			System.out.println("came to First controller");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			// ✅ set the asondate into entity
			request.setReportDate(asondate);

			M_SRWA_12HNewservice.updateReport(request);
			return ResponseEntity.ok("Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Update Failed: " + e.getMessage());
		}
	}
}
