package com.bornfire.brrs.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Component
@Service
@Transactional
@ConfigurationProperties("output")

public class RegulatoryReportServices {

	@Autowired
	BRRS_M_SFINP2_ReportService BRRS_M_SFINP2_reportservice;

	@Autowired
	BRRS_M_IS_ReportService BRRS_M_IS_reportservice;
	
	@Autowired
	BRRS_M_CA5_ReportService BRRS_M_CA5_reportservice;
	
	@Autowired
	BRRS_M_SRWA_12F_ReportService BRRS_M_SRWA_12F_reportservice;
	
	@Autowired
	BRRS_M_SRWA_12H_ReportService BRRS_M_SRWA_12H_reportservice;
	
	private static final Logger logger = LoggerFactory.getLogger(RegulatoryReportServices.class);

	public ModelAndView getReportView(String reportId, String reportDate, String fromdate, String todate,
			String currency, String dtltype, String subreportid, String secid, String reportingTime, Pageable pageable,
			BigDecimal srl_no, String req, String type, String version) {

		ModelAndView repsummary = new ModelAndView();

		logger.info("Getting View for the Report :" + reportId);
		switch (reportId) {

		case "M_SFINP2":
			repsummary = BRRS_M_SFINP2_reportservice.getM_SFINP2View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_IS":
			repsummary = BRRS_M_IS_reportservice.getM_ISView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_CA5":
			repsummary = BRRS_M_CA5_reportservice.getM_CA5View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
		
		case "M_SRWA_12F":
			repsummary = BRRS_M_SRWA_12F_reportservice.getM_SRWA_12FView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
		
		case "M_SRWA_12H":
			repsummary = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

			
		}
		
		return repsummary;
	}

	public ModelAndView getReportDetails(String reportId, String instanceCode, String asondate, String fromdate,
			String todate, String currency, String reportingTime, String dtltype, String subreportid, String secid,
			Pageable pageable, String Filter, String type, String version) {

		ModelAndView repdetail = new ModelAndView();
		logger.info("Getting Details for the Report :" + reportId);

		switch (reportId) {

		case "M_SFINP2":
			repdetail = BRRS_M_SFINP2_reportservice.getM_SFINP2currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;
			
		case "M_IS":
			repdetail = BRRS_M_IS_reportservice.getM_IScurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;
			
		case "M_CA5":
			repdetail = BRRS_M_CA5_reportservice.getM_CA5currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;
			
		case "M_SRWA_12F":
			repdetail = BRRS_M_SRWA_12F_reportservice.getM_SRWA_12FcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter);
			break;
			
		case "M_SRWA_12H":
			repdetail = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter);
			break;
			
		}
		return repdetail;
	}

	public byte[] getDownloadFile(String reportId, String filename, String asondate, String fromdate, String todate,
			String currency, String subreportid, String secid, String dtltype, String reportingTime,
			String instancecode, String filter, String type, String version) {

		byte[] repfile = null;

		switch (reportId) {

		case "M_SFINP2":
			try {
				repfile = BRRS_M_SFINP2_reportservice.BRRS_M_SFINP2Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_IS":
			try {
				repfile = BRRS_M_IS_reportservice.BRRS_M_ISExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_CA5":
			try {
				repfile = BRRS_M_CA5_reportservice.BRRS_M_CA5Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		case "M_SRWA_12F":
			try {
				repfile = BRRS_M_SRWA_12F_reportservice.BRRS_M_SRWA_12FExcel(filename, reportId, fromdate, todate, currency,
						dtltype);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		case "M_SRWA_12H":
			try {
				repfile = BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HExcel(filename, reportId, fromdate, todate, currency,
						dtltype);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		}
		return repfile;
	}

	public byte[] getDownloadDetailFile(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		System.out.println("came to common service");
		if ("MSFinP2Detail".equals(filename)) {
			return BRRS_M_SFINP2_reportservice.BRRS_M_SFINP2DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		}
		
		if ("MISDetail".equals(filename)) {
			return BRRS_M_IS_reportservice.BRRS_M_ISDetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		}

		if ("MCA5Detail".equals(filename)) {
			return BRRS_M_CA5_reportservice.BRRS_M_CA5DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		}
		
		if ("M_SRWA_12FDetail".equals(filename)) {
			return BRRS_M_SRWA_12F_reportservice.BRRS_M_SRWA_12FDetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		
		if ("M_SRWA_12HDetail".equals(filename)) {
			return BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HDetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		
		return new byte[0];
	}

	public List<Object> getArchival(String rptcode) {

		List<Object> archivalData = new ArrayList<>();
		switch (rptcode) {
		case "M_SFINP2":
			try {
				archivalData = BRRS_M_SFINP2_reportservice.getM_SFINP2Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_IS":
			try {
				archivalData = BRRS_M_IS_reportservice.getM_ISArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_CA5":
			try {
				archivalData = BRRS_M_CA5_reportservice.getM_CA5Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		}
		return archivalData;
	}

	
	
	private final ConcurrentHashMap<String, byte[]> jobStorage = new ConcurrentHashMap<>();
    @Async
    public void generateReportAsync(String jobId, String filename, String fromdate,String todate, String dtltype,String type,String currency, String version) {
        System.out.println("Starting report generation for: " + filename);
		        
		byte[] fileData =null;
				
		if (filename.equals("MSFinP2Detail")) {
		    fileData = BRRS_M_SFINP2_reportservice.BRRS_M_SFINP2DetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		} else if (filename.equals("M_ISDetail")) {
		    fileData = BRRS_M_IS_reportservice.BRRS_M_ISDetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		} else if (filename.equals("M_CA5Detail")) {
		    fileData = BRRS_M_CA5_reportservice.BRRS_M_CA5DetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		} else if (filename.equals("M_SRWA_12FDetail")) {
		    fileData = BRRS_M_SRWA_12F_reportservice.BRRS_M_SRWA_12FDetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_SRWA_12HDetail")) {
		    fileData = BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HDetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}

				
				
				
			
		if (fileData == null) {
		    //logger.warn("Excel generation failed or no data for jobId: {}", jobId);
		    jobStorage.put(jobId, null); 
		} else {
		    jobStorage.put(jobId, fileData);
		}

		System.out.println("Report generation completed for: " + filename);
    }

    
    public byte[] getReport(String jobId) {
    	 //System.out.println("Report generation completed for: " + jobId);
        return jobStorage.get(jobId);
    }
	

}
