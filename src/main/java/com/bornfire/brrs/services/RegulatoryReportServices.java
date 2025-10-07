package com.bornfire.brrs.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.dto.ReportLineItemDTO;

@Component
@Service
@Transactional
@ConfigurationProperties("output")

public class RegulatoryReportServices {

	@Autowired
	BRRS_M_SFINP2_ReportService BRRS_M_SFINP2_reportservice;

	@Autowired
	BRRS_M_LA4_ReportService BRRS_M_LA4_reportservice; 
	
	@Autowired
	BRRS_M_IS_ReportService BRRS_M_IS_reportservice;
	
	@Autowired
	BRRS_M_CA5_ReportService BRRS_M_CA5_reportservice;
	
	@Autowired
	BRRS_M_SRWA_12F_ReportService BRRS_M_SRWA_12F_reportservice;
	
	@Autowired
	BRRS_M_SRWA_12C_ReportService BRRS_M_SRWA_12C_reportservice;
	
	@Autowired
	BRRS_M_SRWA_12H_ReportService BRRS_M_SRWA_12H_reportservice;
	
	@Autowired
	BRRS_M_MRC_ReportService BRRS_M_MRC_reportservice;
	
	@Autowired
	BRRS_M_CA1_ReportService BRRS_M_CA1_reportservice;
	
	@Autowired
	BRRS_M_CA3_ReportService BRRS_M_CA3_reportservice;
	
	@Autowired
	BRRS_M_PI_ReportService BRRS_M_PI_reportservice;
	
	@Autowired
	BRRS_M_AIDP_ReportService	BRRS_M_AIDP_ReportService;
	
	@Autowired
	BRRS_M_LA1_ReportService BRRS_M_LA1_reportservice;
	
	@Autowired
	BRRS_M_DEP1_ReportService BRRS_M_DEP1_reportservice;
	
	@Autowired
	BRRS_M_UNCONS_INVEST_ReportService BRRS_M_UNCONS_INVEST_reportservice;

	@Autowired
	BRRS_M_LA2_ReportService BRRS_M_LA2_reportservice;

	@Autowired
	BRRS_M_LA3_ReportService BRRS_M_LA3_reportservice;
	
	@Autowired
	BRRS_M_DEP2_ReportService BRRS_M_DEP2_reportservice;
	
	@Autowired
	BRRS_M_PLL_ReportService BRRS_M_PLL_reportservice;
	
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
			
		case "M_LA4":
			repsummary = BRRS_M_LA4_reportservice.getM_LA4View(reportId, fromdate, todate, currency, dtltype,
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
		
		case "M_SRWA_12C":
			repsummary = BRRS_M_SRWA_12C_reportservice.getM_SRWA_12CView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
		
		case "M_SRWA_12H":
			repsummary = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_MRC":
			repsummary = BRRS_M_MRC_reportservice.getM_MRCView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_CA1":
			repsummary = BRRS_M_CA1_reportservice.getM_CA1View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_CA3":
			repsummary = BRRS_M_CA3_reportservice.getM_CA3View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_PI":
			repsummary = BRRS_M_PI_reportservice.getM_PIView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_LA1":
			repsummary = BRRS_M_LA1_reportservice.getM_LA1View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_AIDP":
			repsummary = BRRS_M_AIDP_ReportService.getM_AIDPView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			

		case "M_DEP1":
			repsummary = BRRS_M_DEP1_reportservice.getM_DEP1View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_UNCONS_INVEST":
			repsummary = BRRS_M_UNCONS_INVEST_reportservice.getM_UNCONS_INVESTView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_LA2":
			repsummary = BRRS_M_LA2_reportservice.getM_LA2View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_LA3":
			repsummary = BRRS_M_LA3_reportservice.getM_LA3View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_DEP2":
			repsummary = BRRS_M_DEP2_reportservice.getM_DEP2View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_PLL":
			repsummary = BRRS_M_PLL_reportservice.getM_PLLView(reportId, fromdate, todate, currency, dtltype,
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
		
					
		case "M_LA4":
			repdetail = BRRS_M_LA4_reportservice.getM_LA4currentDtl(reportId, fromdate, todate, currency, dtltype,
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
					pageable, Filter, type, version);
			break;
			
		case "M_SRWA_12C":
			repdetail = BRRS_M_SRWA_12C_reportservice.getM_SRWA_12CcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;
			
		case "M_SRWA_12H":
			repdetail = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;
			
		case "M_MRC":
			repdetail = BRRS_M_MRC_reportservice.getM_MRCcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;
			
		case "M_CA1":
			repdetail = BRRS_M_CA1_reportservice.getM_CA1currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter);
			break;
			
			
		case "M_PI":
			repdetail = BRRS_M_PI_reportservice.getM_PIcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter,type,version );
			break;
			
		case "M_LA1":
			repdetail = BRRS_M_LA1_reportservice.getM_LA1currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter,type,version );
			break;
			
		case "M_DEP1":
			repdetail = BRRS_M_DEP1_reportservice.getM_DEP1currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter,type,version );
			break;
			
		case "M_LA3":
			repdetail = BRRS_M_LA3_reportservice.getM_LA3currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter,type,version );
			break;
			
		case "M_DEP2":
			repdetail = BRRS_M_DEP2_reportservice.getM_DEP2currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter,type,version );
			break;
			
		case "M_PLL":
			repdetail = BRRS_M_PLL_reportservice.getM_PLLcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter,type,version );
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
			
		case "M_LA4":
			try {
				repfile = BRRS_M_LA4_reportservice.BRRS_M_LA4Excel(filename, reportId, fromdate, todate, currency,
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
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_SRWA_12C":
			try {
				repfile = BRRS_M_SRWA_12C_reportservice.BRRS_M_SRWA_12CExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		case "M_SRWA_12H":
			try {
				repfile = BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_MRC":
			try {
				repfile = BRRS_M_MRC_reportservice.BRRS_M_MRCExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_CA1":
			try {
				repfile = BRRS_M_CA1_reportservice.BRRS_M_CA1Excel(filename, reportId, fromdate, todate, currency,
						dtltype);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_CA3":
			try {
				repfile = BRRS_M_CA3_reportservice.BRRS_M_CA3Excel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_PI":
			try {
				repfile = BRRS_M_PI_reportservice.BRRS_M_PIExcel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_LA1":
			try {
				repfile = BRRS_M_LA1_reportservice.BRRS_M_LA1Excel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_LA2":
			try {
				repfile = BRRS_M_LA2_reportservice.BRRS_M_LA2Excel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_LA3":
			try {
				repfile = BRRS_M_LA3_reportservice.BRRS_M_LA3Excel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_DEP1":
			try {
				repfile = BRRS_M_DEP1_reportservice.BRRS_M_DEP1Excel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_UNCONS_INVEST":
			try {
				repfile = BRRS_M_UNCONS_INVEST_reportservice.BRRS_M_UNCONS_INVESTExcel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_DEP2":
			try {
				repfile = BRRS_M_DEP2_reportservice.BRRS_M_DEP2Excel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_PLL":
			try {
				repfile = BRRS_M_PLL_reportservice.getM_PLLExcel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		
		case "M_AIDP":
			try {
				repfile = BRRS_M_AIDP_ReportService.getM_AIDPExcel(filename, reportId, fromdate, todate, currency,
						dtltype,type,version);
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
		if ("MLA4Detail".equals(filename)) {
			return BRRS_M_LA4_reportservice.BRRS_M_LA4DetailExcel(filename, fromdate, todate, currency, dtltype,
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
		
		if ("M_SRWA_12CDetail".equals(filename)) {
			return BRRS_M_SRWA_12C_reportservice.BRRS_M_SRWA_12CDetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		
		if ("M_SRWA_12HDetail".equals(filename)) {
			return BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HDetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		
		if ("M_MRCDetail".equals(filename)) {
			return BRRS_M_MRC_reportservice.BRRS_M_MRCDetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		
		if ("M_CA1Detail".equals(filename)) {
			return BRRS_M_CA1_reportservice.BRRS_M_CA1DetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		
		
		if ("M_PIDetail".equals(filename)) {
			return BRRS_M_PI_reportservice.BRRS_M_PIDetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		
		if ("M_LA1Detail".equals(filename)) {
			return BRRS_M_LA1_reportservice.BRRS_M_LA1DetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		
		if ("M_DEP1Detail".equals(filename)) {
			return BRRS_M_DEP1_reportservice.BRRS_M_DEP1DetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		if ("M_LA3Detail".equals(filename)) {
			return BRRS_M_LA3_reportservice.BRRS_M_LA3DetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		if ("M_DEP2Detail".equals(filename)) {
			return BRRS_M_DEP2_reportservice.BRRS_M_DEP2DetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		}
		if ("M_PLLDetail".equals(filename)) {
			return BRRS_M_PLL_reportservice.getM_PLLDetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
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
			
		case "M_LA4":
			try {
				archivalData = BRRS_M_LA4_reportservice.getM_LA4Archival();
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
			
		case "M_SRWA_12C":
			try {
				archivalData = BRRS_M_SRWA_12C_reportservice.getM_SRWA_12CArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_SRWA_12F":
			try {
				archivalData = BRRS_M_SRWA_12F_reportservice.getM_SRWA_12FArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_SRWA_12H":
			try {
				archivalData = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_PI":
			try {
				archivalData = BRRS_M_PI_reportservice.getM_PIArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_LA1":
			try {
				archivalData = BRRS_M_LA1_reportservice.getM_LA1Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			

		case "M_DEP1":
			try {
				archivalData = BRRS_M_DEP1_reportservice.getM_DEP1Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_UNCONS_INVEST":
			try {
				archivalData = BRRS_M_UNCONS_INVEST_reportservice.getM_UNCONS_INVESTArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_LA2":
			try {
				archivalData = BRRS_M_LA2_reportservice.getM_LA2Archival();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_LA3":
			try {
				archivalData = BRRS_M_LA3_reportservice.getM_LA3Archival();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_DEP2":
			try {
				archivalData = BRRS_M_DEP2_reportservice.getM_DEP2Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_CA3":
			try {
				archivalData = BRRS_M_CA3_reportservice.getM_CA3Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_AIDP":
			try {
				archivalData = BRRS_M_AIDP_ReportService.getM_AIDPArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_PLL":
			try {
				archivalData = BRRS_M_PLL_reportservice.getM_PLLArchival();
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
		} else if (filename.equals("MLA4Detail")) {
		    fileData = BRRS_M_LA4_reportservice.BRRS_M_LA4DetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		} else if (filename.equals("M_ISDetail")) {
		    fileData = BRRS_M_IS_reportservice.BRRS_M_ISDetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		} else if (filename.equals("M_CA5Detail")) {
		    fileData = BRRS_M_CA5_reportservice.BRRS_M_CA5DetailExcel(filename, fromdate, todate, currency, dtltype, type, version);
		} else if (filename.equals("M_SRWA_12FDetail")) {
		    fileData = BRRS_M_SRWA_12F_reportservice.BRRS_M_SRWA_12FDetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_SRWA_12HDetail")) {
		    fileData = BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HDetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_MRCDetail")) {
		    fileData = BRRS_M_MRC_reportservice.BRRS_M_MRCDetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_SRWA_12CDetail")) {
		    fileData = BRRS_M_SRWA_12C_reportservice.BRRS_M_SRWA_12CDetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_CA1Detail")) {
		    fileData = BRRS_M_CA1_reportservice.BRRS_M_CA1DetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_PIDetail")) {
		    fileData = BRRS_M_PI_reportservice.BRRS_M_PIDetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_LA1Detail")) {
		    fileData = BRRS_M_LA1_reportservice.BRRS_M_LA1DetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_DEP1Detail")) {
		    fileData = BRRS_M_DEP1_reportservice.BRRS_M_DEP1DetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_LA3Detail")) {
		    fileData = BRRS_M_LA3_reportservice.BRRS_M_LA3DetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_DEP2Detail")) {
		    fileData = BRRS_M_DEP2_reportservice.BRRS_M_DEP2DetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
		}else if (filename.equals("M_PLLDetail")) {
		    fileData = BRRS_M_PLL_reportservice.getM_PLLDetailExcel(filename, fromdate, todate, currency, dtltype, type ,version);
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
	

    @Value("${output.exportpathtemp}")
	private String baseExportPath;

	public List<ReportLineItemDTO> getReportDataByCode(String reportCode) throws Exception {
		System.out.println("RegulatoryReportServices received request for report code = " + reportCode);

		String specificFilePath = ""; 
		List<ReportLineItemDTO> reportData = new ArrayList<>();

		switch (reportCode.toUpperCase()) {
		case "M_LA1":
			specificFilePath = baseExportPath + "M_LA1.xlsx";
			System.out.println("Fetching M_LA1 data from: " + specificFilePath);
			reportData = BRRS_M_LA1_reportservice.getReportData(specificFilePath);
			break;
		case "M_SFINP2":
			System.out.println(reportCode);
			specificFilePath = baseExportPath + "M_SFINP2.xlsx";
			System.out.println("Fetching M_SFINP2 data from: " + specificFilePath);
			reportData = BRRS_M_SFINP2_reportservice.getReportData(specificFilePath);
			break;

		

		default:
			System.out.println("No handler found or file path configured for report code: " + reportCode);
			
			break;
		}

		return reportData;
	}
}
