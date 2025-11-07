package com.bornfire.brrs.controllers;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.services.BRRS_M_RPD_ReportService;
import com.bornfire.brrs.dto.ReportLineItemDTO;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity1;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity3;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity4;
import com.bornfire.brrs.entities.BRRS_M_LA1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA3_Detail_Repo;
import com.bornfire.brrs.entities.M_CA2_Manual_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_PLL_Detail_Repo;
import com.bornfire.brrs.entities.M_CA2_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_CA3_Summary_Entity;
import com.bornfire.brrs.entities.M_CA4_Summary_Entity;
import com.bornfire.brrs.entities.M_CA5_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA5_Summary_Entity2;
import com.bornfire.brrs.entities.M_CA6_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA6_Summary_Entity2;
import com.bornfire.brrs.entities.M_CA7_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA7_Summary_Entity;
import com.bornfire.brrs.entities.M_EPR_Summary_Entity;
import com.bornfire.brrs.entities.M_FXR_Summary_Entity1;
import com.bornfire.brrs.entities.M_FXR_Summary_Entity2;
import com.bornfire.brrs.entities.M_FXR_Summary_Entity3;
import com.bornfire.brrs.entities.M_OPTR_Summary_Entity;

import com.bornfire.brrs.entities.M_LA1_Detail_Entity;

import com.bornfire.brrs.entities.M_INT_RATES_Summary_Entity;

import com.bornfire.brrs.entities.M_LA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA2_Summary_Entity;
import com.bornfire.brrs.entities.M_LA3_Detail_Entity;
import com.bornfire.brrs.entities.M_LA3_Summary_Entity2;
import com.bornfire.brrs.entities.M_LA4_Summary_Entity2;
import com.bornfire.brrs.entities.M_LIQ_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Summary_Entity;
import com.bornfire.brrs.entities.M_PLL_Detail_Entity;
import com.bornfire.brrs.entities.M_SECL_Summary_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Summary_Manual_Entity;
import com.bornfire.brrs.entities.M_SIR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SIR_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12A_Summary_Entity1;
import com.bornfire.brrs.entities.M_SRWA_12A_Summary_Entity2;
import com.bornfire.brrs.entities.M_SRWA_12A_Summary_Entity3;
import com.bornfire.brrs.entities.M_SRWA_12A_Summary_Entity4;
import com.bornfire.brrs.entities.M_SRWA_12A_Summary_Entity5;
import com.bornfire.brrs.entities.M_SRWA_12A_Summary_Entity6;
import com.bornfire.brrs.entities.M_SRWA_12A_Summary_Entity7;
import com.bornfire.brrs.entities.M_SRWA_12B_Summary_Entity1;
import com.bornfire.brrs.entities.M_SRWA_12B_Summary_Entity2;
import com.bornfire.brrs.entities.M_SRWA_12B_Summary_Entity3;
import com.bornfire.brrs.entities.M_SRWA_12B_Summary_Entity4;
import com.bornfire.brrs.entities.M_SRWA_12B_Summary_Entity5;
import com.bornfire.brrs.entities.M_SRWA_12B_Summary_Entity6;
import com.bornfire.brrs.entities.M_SRWA_12B_Summary_Entity7;
import com.bornfire.brrs.entities.M_SRWA_12C_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Summary_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity4;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity1;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity2;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity3;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity4;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity1;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity2;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity3;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity4;
import com.bornfire.brrs.entities.Q_RLFA1_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA2_Summary_Entity;
import com.bornfire.brrs.entities.Q_STAFF_Summary_Entity1;
import com.bornfire.brrs.entities.Q_STAFF_Summary_Entity2;
import com.bornfire.brrs.entities.Q_STAFF_Summary_Entity3;
import com.bornfire.brrs.services.BRRS_M_AIDP_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA2_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA3_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA4_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA5_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA6_ReportService;
import com.bornfire.brrs.services.BRRS_M_CA7_ReportService;
import com.bornfire.brrs.services.BRRS_M_EPR_ReportService;
import com.bornfire.brrs.services.BRRS_M_FXR_ReportService;

import com.bornfire.brrs.services.BRRS_M_LA1_ReportService;

import com.bornfire.brrs.services.BRRS_M_INT_RATES_ReportService;

import com.bornfire.brrs.services.BRRS_M_LA2_ReportService;
import com.bornfire.brrs.services.BRRS_M_LA3_ReportService;
import com.bornfire.brrs.services.BRRS_M_LA4_ReportService;
import com.bornfire.brrs.services.BRRS_M_LIQ_ReportService;
import com.bornfire.brrs.services.BRRS_M_OB_ReportService;
import com.bornfire.brrs.services.BRRS_M_PLL_ReportService;
import com.bornfire.brrs.services.BRRS_M_SECL_ReportService;
import com.bornfire.brrs.services.BRRS_M_SFINP1_ReportService;
import com.bornfire.brrs.services.BRRS_M_SIR_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12A_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12B_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12C_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12F_ReportService;
import com.bornfire.brrs.services.BRRS_M_SRWA_12G_ReportService;
import com.bornfire.brrs.services.BRRS_M_UNCONS_INVEST_ReportService;
import com.bornfire.brrs.services.BRRS_Q_BRANCHNET_ReportService;
import com.bornfire.brrs.services.BRRS_Q_RLFA1_ReportService;
import com.bornfire.brrs.services.BRRS_Q_RLFA2_ReportService;
import com.bornfire.brrs.services.BRRS_Q_STAFF_ReportService;
import com.bornfire.brrs.services.BRRS_M_OPTR_ReportService;
import com.bornfire.brrs.services.RegulatoryReportServices;

import com.bornfire.brrs.entities.M_RPD_Summary_Entity1;
import com.bornfire.brrs.entities.M_RPD_Summary_Entity2;


import com.bornfire.brrs.entities.M_RPD_Summary_Entity3;
import com.bornfire.brrs.entities.M_RPD_Summary_Entity4;
import com.bornfire.brrs.entities.M_RPD_Summary_Entity5;
import com.bornfire.brrs.entities.M_RPD_Summary_Entity6;
import com.bornfire.brrs.entities.M_RPD_Summary_Entity7;
import com.bornfire.brrs.entities.M_RPD_Summary_Entity8;
import com.bornfire.brrs.entities.M_RPD_Summary_Entity9;
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
	 BRRS_M_LA4_ReportService BRRS_M_LA4_ReportService;
	 
	 @Autowired
	 BRRS_M_LA1_ReportService brrs_M_LA1_ReportService;
	 
	 @Autowired
	 BRRS_M_LA3_ReportService brrs_M_LA3_ReportService;
	 
	 @Autowired
	 private BRRS_M_PLL_ReportService brrsMpllReportService;
	 
	 
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
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "reportingTime", required = false) String reportingTime, Model md,
			HttpServletRequest req, BigDecimal srl_no) {

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
			@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
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
				currency, reportingTime, dtltype, subreportid, secid, PageRequest.of(page, size), filter,
				type, version);

		return mv;
	}

	@RequestMapping(value = "downloadExcel", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<ByteArrayResource> BRFDownload(HttpServletResponse response,
			@RequestParam("reportid") String reportid, @RequestParam("asondate") String asondate,
			@RequestParam("fromdate") String fromdate, @RequestParam("todate") String todate,
			@RequestParam("currency") String currency, @RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "subreportid", required = false) String subreportid,
			@RequestParam(value = "secid", required = false) String secid,
			@RequestParam(value = "dtltype", required = false) String dtltype,
			@RequestParam(value = "reportingTime", required = false) String reportingTime,
			@RequestParam(value = "filename", required = false) String filename,
			@RequestParam(value = "instancecode", required = false) String instancecode,
			@RequestParam(value = "filter", required = false) String filter)
			throws SQLException, FileNotFoundException {

		response.setContentType("application/octet-stream");
		try {
			asondate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(asondate));
			fromdate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(fromdate));
			todate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(todate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			byte[] excelData = regreportServices.getDownloadFile(reportid, filename, asondate, fromdate, todate,
					currency, subreportid, secid, dtltype, reportingTime, instancecode, filter, type, version);

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

	 @RequestMapping(value = "downloaddetailExcel", method = { RequestMethod.GET, RequestMethod.POST })
		@ResponseBody
		public ResponseEntity<ByteArrayResource> detailDownload(HttpServletResponse response,
				@RequestParam("jobId") String jobId,
				@RequestParam("filename") String filename
				)
				throws SQLException, FileNotFoundException {

			response.setContentType("application/octet-stream");

			
			try {
				byte[] excelData=null;
				
					excelData = regreportServices.getReport(jobId);;
				
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
    @ResponseBody  // forces raw text instead of HTML view
    public String startReport(@RequestParam String filename,
    						@RequestParam("fromdate") String fromdate,
    						@RequestParam("todate") String todate,
                            @RequestParam String currency,
                            @RequestParam("dtltype") String dtltype,
                            @RequestParam("type") String type, 
                            @RequestParam(value = "version", required = false) String version) 	
   {
        String jobId = UUID.randomUUID().toString();
        System.out.println("jobid"+jobId);
        
		regreportServices.generateReportAsync(jobId, filename, fromdate, todate, dtltype, type, currency, version);
        //RT_SLSServices.generateReportAsync(jobId, filename, reportdate, currency,version);
        return jobId;
    }
	
	 @RequestMapping(value = "/checkreport", method = { RequestMethod.GET, RequestMethod.POST })
	    @ResponseBody  // forces raw text instead of HTML view
	    public ResponseEntity<String> checkReport(@RequestParam String jobId) {
	        byte[] report = regreportServices.getReport(jobId);
	        //System.out.println("Report generation completed for: " + jobId);
	        if (report == null) {
	            return ResponseEntity.ok("PROCESSING");
	        }
	        return ResponseEntity.ok("READY");
	    }
	 
	 
	
	 @RequestMapping(value = "/AIDPupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateLA4(
	         @RequestParam(required = false)
	         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

	         @ModelAttribute BRRS_M_AIDP_Summary_Entity1 request1,
	         @ModelAttribute BRRS_M_AIDP_Summary_Entity2 request2,
	         @ModelAttribute BRRS_M_AIDP_Summary_Entity3 request3,
	         @ModelAttribute BRRS_M_AIDP_Summary_Entity4 request4
	 ) {
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

	         return ResponseEntity.ok("All Reports Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }	 
	 
	 @RequestMapping(value = "/updateReport", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateAllReports(
	         @RequestParam(required = false)
	         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

	         @ModelAttribute M_LA4_Summary_Entity2 request1
	         
	 ) {
	     try {
	         
	         // set date into all 4 entities
	         request1.setReportDate(asondate);
	         
	         // call services
	         BRRS_M_LA4_ReportService.updateReport(request1);
	         

	         return ResponseEntity.ok("Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }	 
	 
	 
	 @Autowired
	 private BRRS_M_UNCONS_INVEST_ReportService UNCreportService;
	 
	 @RequestMapping(value = "/UNCupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateUNCAllReports(
	         @RequestParam(required = false)
	         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	         
	         @RequestParam(required = false) String type,
	         @ModelAttribute M_UNCONS_INVEST_Summary_Entity1 request1,
	         @ModelAttribute M_UNCONS_INVEST_Summary_Entity2 request2,
	         @ModelAttribute M_UNCONS_INVEST_Summary_Entity3 request3,
	         @ModelAttribute M_UNCONS_INVEST_Summary_Entity4 request4
	 ) {
	     try {
	         System.out.println("Came to single controller");
	         System.out.println(type);

	         // set date into all 4 entities
	         request1.setREPORT_DATE(asondate);
	         request2.setREPORT_DATE(asondate);
	         request3.setREPORT_DATE(asondate);
	         request4.setREPORT_DATE(asondate);

	         if(type.equals("ARCHIVAL")) {
	        	 M_UNCONS_INVEST_Archival_Summary_Entity1 Archivalrequest1 = new M_UNCONS_INVEST_Archival_Summary_Entity1();
	        	 M_UNCONS_INVEST_Archival_Summary_Entity2 Archivalrequest2 = new M_UNCONS_INVEST_Archival_Summary_Entity2();
	        	 M_UNCONS_INVEST_Archival_Summary_Entity3 Archivalrequest3 = new M_UNCONS_INVEST_Archival_Summary_Entity3();
	        	 M_UNCONS_INVEST_Archival_Summary_Entity4 Archivalrequest4 = new M_UNCONS_INVEST_Archival_Summary_Entity4();
	        	 BeanUtils.copyProperties(request1,Archivalrequest1);
	        	 BeanUtils.copyProperties(request2,Archivalrequest2);
	        	 BeanUtils.copyProperties(request3,Archivalrequest3);
	        	 BeanUtils.copyProperties(request4,Archivalrequest4);
	        	 UNCreportService.updateArchivalReport(Archivalrequest1);
	        	 UNCreportService.updateArchivalReport2(Archivalrequest2);
	        	 UNCreportService.updateArchivalReport3(Archivalrequest3);
	        	 UNCreportService.updateArchivalReport4(Archivalrequest4);
	         }
	         else {
	        	// call services
		         UNCreportService.updateReport(request1);
		         UNCreportService.updateReport2(request2);
		         UNCreportService.updateReport3(request3);
		         UNCreportService.updateReport4(request4);
	        	 
	         }
	         

	         return ResponseEntity.ok("All Reports Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }


	 @Autowired
	 private BRRS_M_CA2_ReportService brrs_m_ca2_reportservice;
	 

	 @RequestMapping(value = "/MCA2updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateAllReports(
	         @RequestParam(required = false)
	         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

	         @ModelAttribute M_CA2_Manual_Summary_Entity request1
	        
	 ) {
	     try {
	         System.out.println("Came to single controller");

	         // set date into  entities
	         request1.setReport_date(asondate);
	       
	         // call services
	         brrs_m_ca2_reportservice.updateReport(request1);
	        

	         return ResponseEntity.ok("Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }	 



	 @Autowired
	 BRRS_M_CA6_ReportService BRRS_M_CA6_ReportService;
	 @RequestMapping(value = "/MCA6updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateAllReports(
	         @RequestParam(required = false)
	         @DateTimeFormat(pattern = "yyyy-MM-dd") Date asondate,  // ✅ ISO format
	         @RequestParam(required = false) String type,
	         @ModelAttribute M_CA6_Summary_Entity2 request1,
	         @ModelAttribute M_CA6_Summary_Entity1 request2
	 ) {
	     try {
	         System.out.println("Came to single controller");
	         System.out.println(type);
	         // set date into all 4 entities
	         request1.setREPORT_DATE(asondate);
	         request2.setREPORT_DATE(asondate);
	  
	     
	    	 BRRS_M_CA6_ReportService.updateReport(request1);
	    	 BRRS_M_CA6_ReportService.updateReport1(request2);
	    
	   return ResponseEntity.ok("Updated Successfully");
	     }
	     catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }
	 
	 
	 @Autowired
		BRRS_M_SRWA_12C_ReportService BRRS_M_SRWA_12C_reportservice;
	 
	 
	 @RequestMapping(value = "/M_SRWA_12CupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateReport(
	     @RequestParam(required = false) 
	     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	     @ModelAttribute M_SRWA_12C_Summary_Entity request
	    ) {

	     try {
	         System.out.println("came to single controller");
	         
	         // ✅ set the asondate into entity
	         request.setREPORT_DATE(asondate);
	         
	         
	      // call services
	         BRRS_M_SRWA_12C_reportservice.updateReport(request);
	         
	         
	         return ResponseEntity.ok(" Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }
	 

	 @Autowired
	 private BRRS_M_LA2_ReportService LA2reportService;

	 @RequestMapping(value = "/LA2updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateLA2AllReports(
	         @RequestParam(required = false)
	         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	         
	         @RequestParam(required = false) String type,
	         @ModelAttribute M_LA2_Summary_Entity request
	 ) {
	     try {
	         System.out.println("Came to single controller");
	         System.out.println(type);

	         // set date into all 4 entities
	         request.setREPORT_DATE(asondate);
	        

	         if(type.equals("ARCHIVAL")) {
	        	 M_LA2_Archival_Summary_Entity Archivalrequest = new M_LA2_Archival_Summary_Entity();
	        	 BeanUtils.copyProperties(request,Archivalrequest);
	        	
	        	 LA2reportService.updateArchivalReport(Archivalrequest);
	        	
	         }
	         else {
	        	// call services
	        	 LA2reportService.updateReport(request);
		                	 
	         }
	         

	         return ResponseEntity.ok("All Reports Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }
	 
	 @Autowired
	 private BRRS_M_CA3_ReportService CA3reportService;
	 
	 @RequestMapping(value = "/CA3updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateAllReports(
	         @RequestParam(required = false)
	         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

	         @ModelAttribute M_CA3_Summary_Entity request1
	         
	 ) {
	     try {
	         System.out.println("Came to single controller");

	         // set date into all 4 entities
	         request1.setREPORT_DATE(asondate);
	         

	         // call services
	         CA3reportService.updateReport(request1);
	         CA3reportService.updateReport2(request1);
	         CA3reportService.updateReport3(request1);
	         CA3reportService.updateReport4(request1);
	         CA3reportService.updateReport5(request1);
	         CA3reportService.updateReport6(request1);
	         

	         return ResponseEntity.ok("All Reports Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }	 
	 
	 @Autowired
	 private BRRS_M_SRWA_12G_ReportService brrs_m_srwa_12g_reportservice;
	 

	 @RequestMapping(value = "/MSRWA12GupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateAllReports(
	         @RequestParam(required = false)
	         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

	         @ModelAttribute M_SRWA_12G_Summary_Entity request1
	        
	 ) {
	     try {
	         System.out.println("Came to single controller");

	         // set date into  entities
	         request1.setReport_date(asondate);
	       
	         // call services
	         brrs_m_srwa_12g_reportservice.updateReport(request1);
	        

	         return ResponseEntity.ok("Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }
	 
	 @Autowired
	 private BRRS_M_CA4_ReportService  brrs_m_ca4_reportservice;
	 
	 
	 @RequestMapping(value = "/M_CA4update", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateReport(
	     @RequestParam(required = false) 
	     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
	     @ModelAttribute M_CA4_Summary_Entity request
	    ) {

	     try {
	         System.out.println("came to single controller");
	         
	         // ✅ set the asondate into entity
	         request.setReport_date(asondate);
	         
	         
	      // call services
	         brrs_m_ca4_reportservice.updateReport(request);
	         
	         
	         return ResponseEntity.ok(" Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 } 
	 
	 

	 
	
	 @Autowired
	 private BRRS_M_LA3_ReportService LA3reportService;

	 @RequestMapping(value = "/LA3updateAll", method = { RequestMethod.GET, RequestMethod.POST })
	 @ResponseBody
	 public ResponseEntity<String> updateLA3AllReports(
	         @RequestParam(required = false)
	         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

	         @ModelAttribute M_LA3_Summary_Entity2 request
	 ) {
	     try {
	         System.out.println("Came to single controller");

	         // ✅ set report date
	         request.setREPORT_DATE(asondate);

	         // ✅ directly update summary report
	         LA3reportService.updateReport(request);

	         return ResponseEntity.ok("All Reports Updated Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Update Failed: " + e.getMessage());
	     }
	 }

	 @Autowired
		private RegulatoryReportServices regulatoryReportServices;

		@RequestMapping(value = "/getReportDataByCode", method = RequestMethod.GET)
		@ResponseBody
		public List<ReportLineItemDTO> getReportDataByCode(@RequestParam("reportCode") String reportCode) throws Exception {

			System.out.println("Controller received request for report code = " + reportCode);

			return regulatoryReportServices.getReportDataByCode(reportCode);
		}
		
		@Autowired
		 BRRS_M_LIQ_ReportService brrs_m_liq_reportservice;
		
		 @RequestMapping(value = "/M_LIQupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateAllReports(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		       
		         @ModelAttribute M_LIQ_Manual_Summary_Entity request2
		      
		 ) {
		     try {
		         System.out.println("Came to single controller");
		         // set date into all 3 entities
		        
		         request2.setReport_date(asondate);
		       
		    
		         // call services
					/* brrs_m_liq_reportservice.updateReport(request1); */
		         brrs_m_liq_reportservice.updateReport1(request2);
		       
		         return ResponseEntity.ok("All Reports Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 @Autowired
		 private BRRS_M_CA5_ReportService CA5reportService;
		
		
		 @RequestMapping(value = "/CA5updateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateAllReports(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		         @ModelAttribute M_CA5_Summary_Entity1 request1,
		         @ModelAttribute M_CA5_Summary_Entity2 request2
		         
		 ) {
		     try {
		         System.out.println("Came to single controller");
		         // set date into all 4 entities
		         request1.setReport_date(asondate);
		         request2.setReport_date(asondate);
		         
		         // call services
		         CA5reportService.updateReport(request1);
		         CA5reportService.updateReport2(request2);
		         
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 @Autowired
		 private BRRS_M_SECL_ReportService SECLreportService;
		
		
		 @RequestMapping(value = "/SECLupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateAllReports(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		         @ModelAttribute M_SECL_Summary_Entity request1
		         
		 ) {
		     try {
		         System.out.println("Came to single controller");
		         // set date into all 4 entities
		         request1.setReport_date(asondate);
		         
		         // call services
		         SECLreportService.updateReport(request1);
		         
		         
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 
		 @Autowired
		 private BRRS_M_INT_RATES_ReportService INTRATESreportService;
		
		
		 @RequestMapping(value = "/INTRATESupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateAllReports(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		         @ModelAttribute M_INT_RATES_Summary_Entity request1
		         
		 ) {
		     try {
		         System.out.println("Came to single controller");
		         // set date into all 4 entities
		         request1.setReport_date(asondate);
		         
		         // call services
		         INTRATESreportService.updateReport(request1);
		         
		         
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 @Autowired
		 BRRS_M_CA7_ReportService M_CA7_ReportService;
		 @RequestMapping(value = "/MCA7updateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateAllReports(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		         @RequestParam(required = false) String type,
		         @ModelAttribute M_CA7_Summary_Entity request1
		 ) {
		     try {
		         System.out.println("Came to single controller");
		         System.out.println(type);
		         // set date into all 4 entities
		         request1.setReport_date(asondate);
		        
		     if(type.equals("ARCHIVAL")) {
		    	 M_CA7_Archival_Summary_Entity Archivalrequest1 = new M_CA7_Archival_Summary_Entity();
		         BeanUtils.copyProperties(request1,Archivalrequest1);	
		     }
		     else {
		    	 M_CA7_ReportService.updateReport(request1);
		     }
		     return ResponseEntity.ok("All Reports Updated Successfully");
		     }
		     catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 
		 @Autowired
		 BRRS_M_SFINP1_ReportService M_SFINP1_ReportService;
		 @RequestMapping(value = "/MSFINP1updateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateReport(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		         
		         @ModelAttribute M_SFINP1_Summary_Manual_Entity request1
		 ) {
		     try {
		         System.out.println("Came to single controller");
		        
		         // set date into all 4 entities
		         request1.setREPORT_DATE(asondate);
		        
		     
		         M_SFINP1_ReportService.updateReport(request1);
		     
		     return ResponseEntity.ok("All Reports Updated Successfully");
		     }
		     catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 @Autowired
		 private BRRS_Q_STAFF_ReportService QSTAFF_service;
		
		 @RequestMapping(value = "/QSTAFF1", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateReport(
		     @RequestParam(required = false)
		     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		     @ModelAttribute Q_STAFF_Summary_Entity1 request,
		     HttpServletRequest req) {
		     try {
		         System.out.println("came to First controller");
		         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		       
		         // ✅ set the asondate into entity
		         request.setREPORT_DATE(asondate);
		         QSTAFF_service.updateReport(request);
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 @RequestMapping(value = "/QSTAFF2", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateReport2(
		     @RequestParam(required = false)
		     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		     @ModelAttribute Q_STAFF_Summary_Entity2 request,
		     HttpServletRequest req) {
		     try {
		         System.out.println("came to Second controller");
		         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		       
		         // ✅ set the asondate into entity
		         request.setREPORT_DATE(asondate);
		         QSTAFF_service.updateReport2(request);
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		
		 @RequestMapping(value = "/QSTAFF3", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateReport3(
		     @RequestParam(required = false)
		     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		     @ModelAttribute Q_STAFF_Summary_Entity3 request,
		     HttpServletRequest req) {
		     try {
		         System.out.println("came to Third controller");
		         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		      
		         // ✅ set the asondate into entity
		         request.setREPORT_DATE(asondate);
		         QSTAFF_service.updateReport3(request);
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 @Autowired
		 private BRRS_Q_BRANCHNET_ReportService Q_BRANCHNETservice;
		
		 @RequestMapping(value = "/QBRANCHNET1", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> QBranchnetUpdate1(
		     @RequestParam(required = false)
		     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		     @ModelAttribute Q_BRANCHNET_Summary_Entity1 request,
		     HttpServletRequest req) {
		     try {
		         System.out.println("came to First controller");
		         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		       
		         // ✅ set the asondate into entity
		         request.setREPORT_DATE(asondate);
		         Q_BRANCHNETservice.QBranchnetUpdate1(request);
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }

		 
// 		 @RequestMapping(value = "/UpdateQ_BRANCHNET_ReSub", method = { RequestMethod.GET, RequestMethod.POST })
// @ResponseBody
// public ResponseEntity<String> updateQBranchnet(
//         @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
//         @ModelAttribute Q_BRANCHNET_Summary_Entity1 request,
//         HttpServletRequest req) {

//     try {
//         System.out.println("Came to Resub Controller");

//         if (asondate != null) {
//             // Set the asondate into the entity
//             request.setReportDate(asondate);
//             System.out.println("Set Report Date: " + asondate);
//         } else {
//             System.out.println("Asondate parameter is null; using entity value: " + request.getReportDate());
//         }

//         // Call service to create a new versioned row
//         Q_BRANCHNETservice.updateQBranchnet(request);

//         return ResponseEntity.ok("Resubmission Updated Successfully");

//     } catch (Exception e) {
//         e.printStackTrace();
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                 .body("Resubmission Update Failed: " + e.getMessage());
//     }
// }
		 @RequestMapping(value = "/QBRANCHNET2", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> QBranchnetUpdate2(
		     @RequestParam(required = false)
		     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		     @ModelAttribute Q_BRANCHNET_Summary_Entity2 request,
		     HttpServletRequest req) {
		     try {
		         System.out.println("came to Second controller");
		         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		       
		         // ✅ set the asondate into entity
		         request.setREPORT_DATE(asondate);
		         Q_BRANCHNETservice.QBranchnetUpdate2(request);
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		
		 @RequestMapping(value = "/QBRANCHNET3", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> QBranchnetUpdate3(
		     @RequestParam(required = false)
		     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		     @ModelAttribute Q_BRANCHNET_Summary_Entity3 request,
		     HttpServletRequest req) {
		     try {
		         System.out.println("came to Third controller");
		         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		      
		         // ✅ set the asondate into entity
		         request.setREPORT_DATE(asondate);
		         Q_BRANCHNETservice.QBranchnetUpdate3(request);
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		
		 @RequestMapping(value = "/QBRANCHNET4", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> QBranchnetUpdate4(
		     @RequestParam(required = false)
		     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		     @ModelAttribute Q_BRANCHNET_Summary_Entity4 request,
		     HttpServletRequest req) {
		     try {
		         System.out.println("came to Fourth controller");
		         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        
		         // ✅ set the asondate into entity
		         request.setREPORT_DATE(asondate);
		         Q_BRANCHNETservice.QBranchnetUpdate4(request);
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 @Autowired
		 private BRRS_M_FXR_ReportService brrs_m_fxr_reportservice;
		
		 @RequestMapping(value = "/FXRupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateAllReports(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		         @ModelAttribute M_FXR_Summary_Entity1 request1,
		         @ModelAttribute M_FXR_Summary_Entity2 request2,
		         @ModelAttribute M_FXR_Summary_Entity3 request3
		 ) {
		     try {
		         System.out.println("Came to single controller");
		         // set date into all 3 entities
		         request1.setReport_date(asondate);
		         request2.setReport_date(asondate);
		         request3.setReport_date(asondate);
		    
		         // call services
		         brrs_m_fxr_reportservice.updateReport1(request1);
		         brrs_m_fxr_reportservice.updateReport2(request2);
		         brrs_m_fxr_reportservice.updateReport3(request3);
		         return ResponseEntity.ok("All Reports Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 @Autowired
		 private BRRS_M_SRWA_12F_ReportService SRWA12FreportService;
		
		
		 @RequestMapping(value = "/SRWA12FupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateAllReports(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		         @ModelAttribute M_SRWA_12F_Summary_Entity request1
		         
		 ) {
		     try {
		         System.out.println("Came to single controller");
		         // set date into all 4 entities
		         request1.setREPORT_DATE(asondate);
		         
		         // call services
		         SRWA12FreportService.updateReport(request1);
		         
		         return ResponseEntity.ok("Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		 @Autowired

		 BRRS_M_SIR_ReportService BRRS_M_SIR_ReportService;
		 @RequestMapping(value = "/MSIRupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateAllReports(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		         @RequestParam(required = false) String type,
		         @ModelAttribute M_SIR_Summary_Entity request1
		 ) {
		     try {
		         System.out.println("Came to single controller");
		         System.out.println(type);
		         // set date into all 4 entities
		         request1.setReport_date(asondate);
		        
		     if(type.equals("ARCHIVAL")) {
		         M_SIR_Archival_Summary_Entity Archivalrequest1 = new M_SIR_Archival_Summary_Entity();
		         BeanUtils.copyProperties(request1,Archivalrequest1);	
		     }
		     else {
		         BRRS_M_SIR_ReportService.updateReport(request1);
		     }
		     return ResponseEntity.ok("All Reports Updated Successfully");
		     }
		     catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		
		 
		 @Autowired
		 private BRRS_M_EPR_ReportService  brrs_m_epr_reportservice;
		 
		 
		 @RequestMapping(value = "/M_EPRupdate", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateReport(
		     @RequestParam(required = false) 
		     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
		     @ModelAttribute M_EPR_Summary_Entity request
		    ) {

		     try {
		         System.out.println("came to single controller");
		         
		         // ✅ set the asondate into entity
		         request.setReport_date(asondate);
		         
		         
		      // call services
		         brrs_m_epr_reportservice.updateReport(request);
		         
		         
		         return ResponseEntity.ok(" Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }
		 
		
		 
		 @Autowired
			BRRS_M_SRWA_12A_ReportService brrs_m_srwa_12a_reportservice;
		

		 @RequestMapping(value = "/M_SRWA_12AupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
		 @ResponseBody
		 public ResponseEntity<String> updateAllReports(
		         @RequestParam(required = false)
		         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

		         @ModelAttribute M_SRWA_12A_Summary_Entity1 request1,
		         @ModelAttribute M_SRWA_12A_Summary_Entity2 request2,
		         @ModelAttribute M_SRWA_12A_Summary_Entity3 request3,
		         @ModelAttribute M_SRWA_12A_Summary_Entity4 request4,
		         @ModelAttribute M_SRWA_12A_Summary_Entity5 request5,
		         @ModelAttribute M_SRWA_12A_Summary_Entity6 request6,
		         @ModelAttribute M_SRWA_12A_Summary_Entity7 request7
		 ) {
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


		         return ResponseEntity.ok("All Reports Updated Successfully");
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                              .body("Update Failed: " + e.getMessage());
		     }
		 }

		 
		    
		 
		 
		 	@RequestMapping(value = "/updateMPLL", method = { RequestMethod.GET, RequestMethod.POST })
		 	@ResponseBody
		    public String updateMPLL(@ModelAttribute M_PLL_Detail_Entity mpllData) {
		    	System.out.println("Came to Controller ");
		        System.out.println("Received update for ACCT_NO: " + mpllData.getAcctNumber());
		        System.out.println("Provision value: " + mpllData.getProvision());

		        boolean updated = brrsMpllReportService.updateProvision(mpllData);

		        if (updated) {
		            return "Provision updated successfully!";
		        } else {
		            return "Record not found for update!";
		        }
		    }
		 	
		 	@Autowired
		 	private BRRS_M_PLL_Detail_Repo M_PLL_Detail_Repo;

		 	@RequestMapping(value = "/MPLL_Detail", method = {RequestMethod.GET, RequestMethod.POST})
		 	public String showMPLLDetail(@RequestParam(required = false) String formmode,
		 	                             @RequestParam(required = false) String acctNo,
		 	                             Model model) {

		 	    // 1. Fetch the entity from the database
		 	    M_PLL_Detail_Entity mpllEntity = M_PLL_Detail_Repo.findByAcctNumber(acctNo);

		 	    // 2. A good practice is to check if the entity was found
		 	    if (mpllEntity != null) {
		 	        // 3. Get the report date from the entity
		 	        Date reportDate = mpllEntity.getReportDate(); // Assuming the method is getReportDate()

		 	        // 4. Format the date into a String (e.g., "dd/MM/yyyy")
		 	        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		 	        String formattedDate = formatter.format(reportDate);

		 	        // 5. Add the formatted date to the model so the header can use it
		 	        model.addAttribute("asondate", formattedDate);
		 	    }
		 	    
		 	    // Add the other attributes as before
		 	    model.addAttribute("displaymode", "edit");
		 	    model.addAttribute("formmode", "edit");
		 	    model.addAttribute("mpllData", M_PLL_Detail_Repo .findByAcctNumber(acctNo)); // Pass the fetched entity to the form

		 	    return "BRRS/M_PLL"; // your Thymeleaf HTML page
		 	}		 

			 @Autowired
			 private BRRS_M_SRWA_12B_ReportService brrs_m_srwa_12b_reportservice;
			 

			 @RequestMapping(value = "/MSRWA12BupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
			 @ResponseBody
			 public ResponseEntity<String> updateAllReports(
			         @RequestParam(required = false)
			         @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,

			         @ModelAttribute M_SRWA_12B_Summary_Entity1 request1,
			         @ModelAttribute M_SRWA_12B_Summary_Entity2 request2,
			         @ModelAttribute M_SRWA_12B_Summary_Entity3 request3,
			         @ModelAttribute M_SRWA_12B_Summary_Entity4 request4,
			         @ModelAttribute M_SRWA_12B_Summary_Entity5 request5,
			         @ModelAttribute M_SRWA_12B_Summary_Entity6 request6,
			         @ModelAttribute M_SRWA_12B_Summary_Entity7 request7
			        
			 ) {
			     try {
			         System.out.println("Came to single controller");

			         // set date into all 7 entities
			         request1.setReport_date(asondate);
			         request2.setReport_date(asondate);
			         request3.setReport_date(asondate);
				 request4.setReport_date(asondate);
				 request5.setReport_date(asondate);
				 request6.setReport_date(asondate);
				 request7.setReport_date(asondate);

			     

			         // call services
			         brrs_m_srwa_12b_reportservice.updateReport1(request1);
			         brrs_m_srwa_12b_reportservice.updateReport2(request2);
			         brrs_m_srwa_12b_reportservice.updateReport3(request3);
			         brrs_m_srwa_12b_reportservice.updateReport4(request4);
			         brrs_m_srwa_12b_reportservice.updateReport5(request5);
			         brrs_m_srwa_12b_reportservice.updateReport6(request6);
			         brrs_m_srwa_12b_reportservice.updateReport7(request7);

			         return ResponseEntity.ok("Updated Successfully");
			     } catch (Exception e) {
			         e.printStackTrace();
			         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                              .body("Update Failed: " + e.getMessage());
			     }
			 }	 
		
			 
			 
			 @RequestMapping(value = "/updateMLA1", method = { RequestMethod.GET, RequestMethod.POST })
			 @ResponseBody
			 public String updateMPLL(@ModelAttribute M_LA1_Detail_Entity Data) {
			     System.out.println("Came to Controller ");
			     System.out.println("Received update for ACCT_NO: " + Data.getAcct_number());
			     System.out.println("sanction value: " + Data.getSanction_limit());
			     System.out.println("balance value: " + Data.getAcct_balance_in_pula());

			     boolean updated = brrs_M_LA1_ReportService.updateProvision(Data);

			     if (updated) {
			         return "M_LA1 Detail updated successfully!";
			     } else {
			         return "Record not found for update!";
			     }
			 }

			 @Autowired
			 private BRRS_M_LA1_Detail_Repo M_LA1_Detail_Repo;

			 @RequestMapping(value = "/MLA1_Detail", method = {RequestMethod.GET, RequestMethod.POST})
			 public String showMLA1Detail(@RequestParam(required = false) String formmode,
			                              @RequestParam(required = false) String acctNo,
			                              @RequestParam(required = false) BigDecimal sanction_limit,
			                              @RequestParam(required = false) BigDecimal acct_balance_in_pula,
			                              Model model) {

			     M_LA1_Detail_Entity la1Entity = M_LA1_Detail_Repo.findByAcctnumber(acctNo);

			     if (la1Entity != null) {

			         if (sanction_limit != null) {
			             la1Entity.setSanction_limit(sanction_limit);
			         }
			         if (acct_balance_in_pula != null) {
			             la1Entity.setAcct_balance_in_pula(acct_balance_in_pula);
			         }

			         if (sanction_limit != null || acct_balance_in_pula != null) {
			             M_LA1_Detail_Repo.save(la1Entity);
			             System.out.println("Updated Sanction Limit / Account Balance for ACCT_NO: " + acctNo);
			         }

			         Date reportDate = la1Entity.getReport_date();
			         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			         String formattedDate = formatter.format(reportDate);
			         model.addAttribute("asondate", formattedDate);
			     }

			     model.addAttribute("displaymode", "edit");
			     model.addAttribute("formmode", "edit");
			     model.addAttribute("Data", la1Entity);

			     return "BRRS/M_LA1";
			 }
			 
			 @Autowired
				BRRS_M_OB_ReportService BRRS_M_OB_reportservice;
			 
			 
			 @RequestMapping(value = "/MOBupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
			 @ResponseBody
			 public ResponseEntity<String> updateReport(
			     @RequestParam(required = false) 
			     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			     @ModelAttribute M_OB_Summary_Entity request
			    ) {

			     try {
			         System.out.println("came to single controller");
			         
			         // ✅ set the asondate into entity
			         request.setReport_date(asondate);
			         
			         
			      // call services
			         BRRS_M_OB_reportservice.updateReport1(request);
			         
			         
			         return ResponseEntity.ok(" Updated Successfully");
			     } catch (Exception e) {
			         e.printStackTrace();
			         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                              .body("Update Failed: " + e.getMessage());
			     }
			 }

			 
			 
			 @Autowired
			 private BRRS_Q_RLFA2_ReportService q_rlfa2_reportService;
			
			 @RequestMapping(value = "/Q_RLFA2update", method = { RequestMethod.GET, RequestMethod.POST })
			 @ResponseBody
			 public ResponseEntity<String> updateReport(
			     @RequestParam(required = false)
			     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			     @ModelAttribute Q_RLFA2_Summary_Entity request
			    ) {
			     try {
			         System.out.println("came to single controller");
			        
			         // ✅ set the asondate into entity
			         request.setReport_date(asondate);
			        
			        
			      // call services
			         q_rlfa2_reportService.updateReport(request);
			        
			        
			         return ResponseEntity.ok("All Reports Updated Successfully");
			     } catch (Exception e) {
			         e.printStackTrace();
			         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                              .body("Update Failed: " + e.getMessage());
			     }
			 }
			 
			 
			 @Autowired
				BRRS_Q_RLFA1_ReportService brrs_q_rlfa1_reportservice;
			 
			 
			 @RequestMapping(value = "/Q_RLFA1update", method = { RequestMethod.GET, RequestMethod.POST })
			 @ResponseBody
			 public ResponseEntity<String> updateReport(
			     @RequestParam(required = false) 
			     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			     @ModelAttribute Q_RLFA1_Summary_Entity request
			    ) {

			     try {
			         System.out.println("came to single controller");
			         
			         // ✅ set the asondate into entity
			         request.setReport_date(asondate);
			         
			         
			      // call services
			         brrs_q_rlfa1_reportservice.updateReport(request);
			         
			         
			         return ResponseEntity.ok(" Updated Successfully");
			     } catch (Exception e) {
			         e.printStackTrace();
			         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                              .body("Update Failed: " + e.getMessage());
			     }
			 }
			 
			 @Autowired
			 BRRS_M_RPD_ReportService BRRS_M_RPD_ReportService;
			 
			 
			 @RequestMapping(value = "/M_RPDupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
			 @ResponseBody
			 public ResponseEntity<String> updateReport1(
			     @RequestParam(required = false) 
			     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			     @ModelAttribute M_RPD_Summary_Entity1 request1,
			     @ModelAttribute M_RPD_Summary_Entity2 request2,
			     @ModelAttribute M_RPD_Summary_Entity3 request3,
			     @ModelAttribute M_RPD_Summary_Entity4 request4,
			     @ModelAttribute M_RPD_Summary_Entity5 request5,
			     @ModelAttribute M_RPD_Summary_Entity6 request6,
			     @ModelAttribute M_RPD_Summary_Entity7 request7,
			     @ModelAttribute M_RPD_Summary_Entity8 request8,
			     @ModelAttribute M_RPD_Summary_Entity9 request9
			    
			    ) {

			     try {
			         System.out.println("came to single controller");
			         request1.setREPORT_DATE(asondate);
			         request2.setREPORT_DATE(asondate);
			         request3.setREPORT_DATE(asondate);
			         request4.setREPORT_DATE(asondate);
			         request5.setREPORT_DATE(asondate);
			         request6.setREPORT_DATE(asondate);
			         request7.setREPORT_DATE(asondate);
			         request8.setREPORT_DATE(asondate);
			         request9.setREPORT_DATE(asondate);
			         
			     	 BRRS_M_RPD_ReportService.updateReport1(request1);
			         BRRS_M_RPD_ReportService.updateReport2(request2);
			         BRRS_M_RPD_ReportService.updateReport3(request3);
			         BRRS_M_RPD_ReportService.updateReport4(request4);
			         BRRS_M_RPD_ReportService.updateReport5(request5);
			         BRRS_M_RPD_ReportService.updateReport6(request6);
			         BRRS_M_RPD_ReportService.updateReport7(request7);
			         BRRS_M_RPD_ReportService.updateReport8(request8);
			         BRRS_M_RPD_ReportService.updateReport9(request9);
			         
			         return ResponseEntity.ok("Updated Successfully");
			     } catch (Exception e) {
			         e.printStackTrace();
			         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                              .body("Update Failed: " + e.getMessage());
			     }
			 }
		
			 @RequestMapping(value = "/updateMLA3", method = { RequestMethod.GET, RequestMethod.POST })
			 @ResponseBody
			 public String updateMPLL(@ModelAttribute M_LA3_Detail_Entity Data) {
			     System.out.println("Came to Controller ");
			     System.out.println("Received update for ACCT_NO: " + Data.getAcct_number());
			     System.out.println("sanction value: " + Data.getSanction_limit());
			     System.out.println("balance value: " + Data.getAcct_balance_in_pula());

			     boolean updated = brrs_M_LA3_ReportService.updatedetail(Data);

			     if (updated) {
			         return "M_LA3 updated successfully!";
			     } else {
			         return "Record not found for update!";
			     }
			 }

			 @Autowired
			 private BRRS_M_LA3_Detail_Repo M_LA3_Detail_Repo;

			 @RequestMapping(value = "/MLA3_Detail", method = {RequestMethod.GET, RequestMethod.POST})
			 public String showMLA3Detail(@RequestParam(required = false) String formmode,
			                              @RequestParam(required = false) String acctNo,
			                              @RequestParam(required = false) BigDecimal sanction_limit,
			                              @RequestParam(required = false) BigDecimal acct_balance_in_pula,
			                              Model model) {

			     M_LA3_Detail_Entity la3Entity = M_LA3_Detail_Repo.findByAcctnumber(acctNo);

			     if (la3Entity != null) {

			         if (sanction_limit != null) {
			        	 la3Entity.setSanction_limit(sanction_limit);
			         }
			         if (acct_balance_in_pula != null) {
			        	 la3Entity.setAcct_balance_in_pula(acct_balance_in_pula);
			         }

			         if (sanction_limit != null || acct_balance_in_pula != null) {
			             M_LA3_Detail_Repo.save(la3Entity);
			             System.out.println("Updated Sanction Limit / Account Balance for ACCT_NO: " + acctNo);
			         }

			         Date reportDate = la3Entity.getReport_date();
			         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			         String formattedDate = formatter.format(reportDate);
			         model.addAttribute("asondate", formattedDate);
			     }

			     model.addAttribute("displaymode", "edit");
			     model.addAttribute("formmode", "edit");
			     model.addAttribute("Data", la3Entity);

			     return "BRRS/M_LA3";
			 }

			 @Autowired
				BRRS_M_OPTR_ReportService BRRS_M_OPTR_reportservice;
			 
			 
			 @RequestMapping(value = "/MOPTRupdateAll", method = { RequestMethod.GET, RequestMethod.POST })
			 @ResponseBody
			 public ResponseEntity<String> updateReport(
			     @RequestParam(required = false) 
			     @DateTimeFormat(pattern = "dd/MM/yyyy") Date asondate,
			     @ModelAttribute M_OPTR_Summary_Entity request
			    ) {

			     try {
			         System.out.println("came to single controller");
			         
			         // ✅ set the asondate into entity
			         request.setReport_date(asondate);
			         
			         
			      // call services
			         BRRS_M_OPTR_reportservice.updateReport1(request);
			         
			         
			         return ResponseEntity.ok("Updated Successfully");
			     } catch (Exception e) {
			         e.printStackTrace();
			         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                              .body("Update Failed: " + e.getMessage());
			     }
			 }

		
}
