package com.bornfire.brrs.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	BRRS_M_SFINP1_ReportService BRRS_M_SFINP1_reportservice;

	@Autowired
	BRRS_M_LA4_ReportService BRRS_M_LA4_reportservice;

	@Autowired
	BRRS_M_IS_ReportService BRRS_M_IS_reportservice;

	@Autowired
	BRRS_M_CA4_ReportService BRRS_M_CA4_reportservice;

	@Autowired
	BRRS_M_CA2_ReportService BRRS_M_CA2_reportservice;

	@Autowired
	BRRS_M_CA5_ReportService BRRS_M_CA5_reportservice;

	@Autowired
	BRRS_M_CA6_ReportService BRRS_M_CA6_reportservice;
	
	@Autowired
	BRRS_M_CR_ReportService BRRS_M_CR_reportservice;

	@Autowired
	BRRS_M_SP_ReportService BRRS_M_SP_reportservice;

	@Autowired
	BRRS_M_SRWA_12F_ReportService BRRS_M_SRWA_12F_reportservice;

	@Autowired
	BRRS_M_SRWA_12C_ReportService BRRS_M_SRWA_12C_reportservice;

	@Autowired
	BRRS_M_SRWA_12G_ReportService BRRS_M_SRWA_12G_reportservice;

	@Autowired
	BRRS_M_SRWA_12H_ReportService BRRS_M_SRWA_12H_reportservice;

	@Autowired
	BRRS_M_MRC_ReportService BRRS_M_MRC_reportservice;

	@Autowired
	BRRS_M_CA1_ReportService BRRS_M_CA1_reportservice;

	@Autowired
	BRRS_M_CA3_ReportService BRRS_M_CA3_reportservice;

	@Autowired
	BRRS_M_CA7_ReportService BRRS_M_CA7_reportservice;

	@Autowired
	BRRS_M_PI_ReportService BRRS_M_PI_reportservice;

	@Autowired
	BRRS_M_AIDP_ReportService BRRS_M_AIDP_ReportService;

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
	BRRS_M_LA5_ReportService BRRS_M_LA5_reportservice;

	@Autowired
	BRRS_M_DEP2_ReportService BRRS_M_DEP2_reportservice;

	@Autowired
	BRRS_M_DEP3_ReportService BRRS_M_DEP3_reportservice;

	@Autowired
	BRRS_M_PLL_ReportService BRRS_M_PLL_reportservice;

	@Autowired
	BRRS_M_IRB_ReportService brrs_m_irb_reportService;

	@Autowired
	BRRS_M_LIQ_ReportService BRRS_M_LIQ_reportservice;

	@Autowired
	BRRS_Q_STAFF_Report_Service BRRS_Q_STAFF_report_service;

	@Autowired
	BRRS_Q_BRANCHNET_ReportService BRRS_Q_BRANCHNET_reportservice;

	@Autowired
	BRRS_M_FXR_ReportService BRRS_M_FXR_reportservice;

	@Autowired
	BRRS_M_SRWA_12B_ReportService brrs_m_srwa_12b_reportservice;

	@Autowired
	BRRS_M_SECL_ReportService brrs_m_secl_reportservice;
	
	@Autowired
	BRRS_M_SEC_ReportService brrs_m_sec_reportservice;

	@Autowired
	BRRS_Q_SMME_Intrest_Income_ReportService BRRS_Q_SMME_Intrest_Income_ReportService;

	@Autowired
	BRRS_M_SIR_ReportService BRRS_M_SIR_ReportService;

	@Autowired
	BRRS_M_EPR_ReportService brrs_m_epr_reportservice;

	@Autowired
	BRRS_M_SRWA_12A_ReportService brrs_m_srwa_12a_reportservice;

	@Autowired
	BRRS_M_OB_ReportService BRRS_M_OB_ReportService;

	@Autowired
	BRRS_Q_RLFA1_ReportService brrs_q_rlfa1_reportservice;

	@Autowired
	BRRS_Q_RLFA2_ReportService brrs_q_rlfa2_reportservice;

	@Autowired
	BRRS_M_INT_RATES_ReportService brrs_m_int_rates_reportservice;

	@Autowired
	BRRS_M_RPD_ReportService BRRS_M_RPD_ReportService;

	@Autowired
	BRRS_M_OPTR_ReportService BRRS_M_OPTR_ReportService;
	
	@Autowired
	BRRS_M_INT_RATES_FCA_ReportService brrs_m_int_rates_fca_reportservice;
	
	@Autowired
	BRRS_M_LARADV_ReportService brrs_m_laradv_reportservice;
	
	
	@Autowired
	BRRS_Q_SMME_DEP_ReportService BRRS_Q_SMME_DEP_ReportService;
	
	@Autowired
	BRRS_M_BOP_ReportService BRRS_M_BOP_ReportService;
	
	@Autowired
	BRRS_M_SECA_ReportService BRRS_M_SECA_ReportService;
	
	@Autowired
	BRRS_M_GP_ReportService BRRS_M_GP_ReportService;

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

		case "M_SFINP1":
			repsummary = BRRS_M_SFINP1_reportservice.getM_SFINP1View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_LA4":
			repsummary = BRRS_M_LA4_reportservice.getM_LA4View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_IS":
			repsummary = BRRS_M_IS_reportservice.getM_ISView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_CA4":
			repsummary = BRRS_M_CA4_reportservice.getBRRS_M_CA4View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_CA2":
			repsummary = BRRS_M_CA2_reportservice.getM_CA2View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_CA6":
			repsummary = BRRS_M_CA6_reportservice.getM_CA6View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_CA5":
			repsummary = BRRS_M_CA5_reportservice.getM_CA5View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;
			
		case "M_CR":
			repsummary = BRRS_M_CR_reportservice.getM_CRView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_SRWA_12F":
			repsummary = BRRS_M_SRWA_12F_reportservice.getM_SRWA_12FView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_SRWA_12C":
			repsummary = BRRS_M_SRWA_12C_reportservice.getBRRS_M_SRWA_12CView(reportId, fromdate, todate, currency,
					dtltype, pageable, type, version);
			break;

		case "M_SRWA_12G":
			repsummary = BRRS_M_SRWA_12G_reportservice.getM_SRWA_12GView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_SRWA_12H":
			repsummary = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_MRC":
			repsummary = BRRS_M_MRC_reportservice.getM_MRCView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_CA1":
			repsummary = BRRS_M_CA1_reportservice.getM_CA1View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_CA3":
			repsummary = BRRS_M_CA3_reportservice.getM_CA3View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_CA7":
			repsummary = BRRS_M_CA7_reportservice.getM_CA7View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_SP":
			repsummary = BRRS_M_SP_reportservice.getM_SPView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_PI":
			repsummary = BRRS_M_PI_reportservice.getM_PIView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_LA1":
			repsummary = BRRS_M_LA1_reportservice.getM_LA1View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
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
			repsummary = BRRS_M_UNCONS_INVEST_reportservice.getM_UNCONS_INVESTView(reportId, fromdate, todate, currency,
					dtltype, pageable, type, version);
			break;

		case "M_LA2":
			repsummary = BRRS_M_LA2_reportservice.getM_LA2View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_LA3":
			repsummary = BRRS_M_LA3_reportservice.getM_LA3View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_LA5":
			repsummary = BRRS_M_LA5_reportservice.getM_LA5View(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_DEP2":
			repsummary = BRRS_M_DEP2_reportservice.getM_DEP2View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_PLL":
			repsummary = BRRS_M_PLL_reportservice.getM_PLLView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_DEP3":
			repsummary = BRRS_M_DEP3_reportservice.getM_DEP3View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_IRB":
			repsummary = brrs_m_irb_reportService.getM_IRBView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_LIQ":
			repsummary = BRRS_M_LIQ_reportservice.getM_LIQView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

			case "Q_STAFF":
				repsummary = BRRS_Q_STAFF_report_service.getQ_STAFFView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

		case "Q_BRANCHNET":
			repsummary = BRRS_Q_BRANCHNET_reportservice.getQ_BRANCHNETView(reportId, fromdate, todate, currency,
					dtltype, pageable, type, version);
			break;

		case "M_FXR":
			repsummary = BRRS_M_FXR_reportservice.getM_FXRView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_SRWA_12B":

			repsummary = brrs_m_srwa_12b_reportservice.getM_SRWA_12BView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_SECL":
			repsummary = brrs_m_secl_reportservice.getM_SECLView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_INT_RATES":
			repsummary = brrs_m_int_rates_reportservice.getM_INTRATESView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_LARADV":
			repsummary = brrs_m_laradv_reportservice.getM_LARADVView(reportId, fromdate, todate, currency, dtltype, pageable,type, version);
			break;

		case "Q_SMME":

			repsummary = BRRS_Q_SMME_Intrest_Income_ReportService.getBRRS_Q_SMMEView(reportId, fromdate, todate,
					currency, dtltype, pageable, type, version);
			break;

		case "M_SIR":

			repsummary = BRRS_M_SIR_ReportService.getM_SIRView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_EPR":

			repsummary = brrs_m_epr_reportservice.getM_EPRView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_SRWA_12A":

			repsummary = brrs_m_srwa_12a_reportservice.getM_SRWA_12AView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "M_OB":
			repsummary = BRRS_M_OB_ReportService.getM_OBview(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "Q_RLFA1":

			repsummary = brrs_q_rlfa1_reportservice.getQ_RLFA1View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;

		case "Q_RLFA2":

			repsummary = brrs_q_rlfa2_reportservice.getQ_RLFA2View(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
		case "M_RPD":
			repsummary = BRRS_M_RPD_ReportService.getM_RPDView(reportId, fromdate, todate, currency, dtltype, pageable,
					type, version);
			break;

		case "M_OPTR":
			repsummary = BRRS_M_OPTR_ReportService.getM_OPTRView(reportId, fromdate, todate, currency, dtltype,
					pageable, type, version);
			break;
			
		case "M_INT_RATES_FCA":
			repsummary = brrs_m_int_rates_fca_reportservice.getM_INTRATESFCAView(reportId, fromdate, todate, currency, dtltype, pageable,type, version);
			break;
			
		case "M_SEC":
			repsummary = brrs_m_sec_reportservice.getM_SECView(reportId, fromdate, todate, currency, dtltype, pageable,type, version);
			break;
			
			
		case "Q_SMME_DEP":
  			
  			repsummary = BRRS_Q_SMME_DEP_ReportService.getQ_SMME_DEPview(reportId, fromdate, todate, currency, dtltype,
  					pageable, type, version);
  			break;
  			
	 case "M_BOP":
  			
  			repsummary = BRRS_M_BOP_ReportService.getBRRS_M_BOPview(reportId, fromdate, todate, currency, dtltype,
  					pageable, type, version);
  			break;
  			
			
		case "M_SECA":
			repsummary = BRRS_M_SECA_ReportService.getM_SECAview(reportId, fromdate, todate, currency, dtltype, pageable,type, version);
			break;
			
		
		case "M_GP":
			repsummary = BRRS_M_GP_ReportService.getM_GPView(reportId, fromdate, todate, currency, dtltype, pageable,type, version);
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

		case "M_SFINP1":
			repdetail = BRRS_M_SFINP1_reportservice.getM_SFINP1currentDtl(reportId, fromdate, todate, currency, dtltype,
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

		case "M_CA2":
			repdetail = BRRS_M_CA2_reportservice.getM_CA2currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		/*
		 * case "M_CA5": repdetail =
		 * BRRS_M_CA5_reportservice.getM_CA5currentDtl(reportId, fromdate, todate,
		 * currency, dtltype, pageable, Filter, type, version); break;
		 */

		case "M_SP":
			repdetail = BRRS_M_SP_reportservice.getM_SPcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		// case "M_SRWA_12H":
		// 	repdetail = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HcurrentDtl(reportId, fromdate, todate, currency,
		// 			dtltype, pageable, Filter, type, version);
		// 	break;

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
					pageable, Filter, type, version);
			break;

		case "M_LA1":
			repdetail = BRRS_M_LA1_reportservice.getM_LA1currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		case "M_DEP1":
			repdetail = BRRS_M_DEP1_reportservice.getM_DEP1currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		case "M_LA3":
			repdetail = BRRS_M_LA3_reportservice.getM_LA3currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		case "M_LA5":
			repdetail = BRRS_M_LA5_reportservice.getM_LA5currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		case "M_DEP2":
			repdetail = BRRS_M_DEP2_reportservice.getM_DEP2currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		case "M_PLL":
			repdetail = BRRS_M_PLL_reportservice.getM_PLLcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		case "M_DEP3":
			repdetail = BRRS_M_DEP3_reportservice.getM_DEP3currentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		case "M_LIQ":
			repdetail = BRRS_M_LIQ_reportservice.getM_LIQcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);
			break;

		case "Q_SMME":
			repdetail = BRRS_Q_SMME_Intrest_Income_ReportService.getBRRS_Q_SMMEcurrentDtl(reportId, fromdate, todate,
					currency, dtltype, pageable, Filter, type, version);
			break;

		case "M_IRB":
			repdetail = brrs_m_irb_reportService.getM_IRBcurrentDtl(reportId, fromdate, todate, currency, dtltype,
					pageable, Filter, type, version);

		case "M_SRWA_12A":

			repdetail = brrs_m_srwa_12a_reportservice.getM_SRWA_12AcurrentDtl(reportId, fromdate, todate, currency,
					dtltype, pageable, Filter, type, version);
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

		case "M_SFINP1":
			try {
				repfile = BRRS_M_SFINP1_reportservice.getM_SFINP1Excel(filename, reportId, fromdate, todate, currency,
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

		case "M_CA2":
			try {
				repfile = BRRS_M_CA2_reportservice.getM_CA2Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_CA4":
			try {
				repfile = BRRS_M_CA4_reportservice.getBRRS_M_CA4Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_CR":
			try {
				repfile = BRRS_M_CR_reportservice.BRRS_M_CRExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_SEC":
			try {
				repfile = brrs_m_sec_reportservice.getM_SECExcel(filename, reportId, fromdate, todate, currency, dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_INT_RATES_FCA":
			try {
				repfile = brrs_m_int_rates_fca_reportservice.getM_INTRATESFCAExcel(filename, reportId, fromdate, todate, currency, dtltype,type,version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_CA5":
			try {
				repfile = BRRS_M_CA5_reportservice.getM_CA5Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_CA6":
			try {
				repfile = BRRS_M_CA6_reportservice.getM_CA6Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_CA7":
			try {
				repfile = BRRS_M_CA7_reportservice.getM_CA7Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SP":
			try {
				repfile = BRRS_M_SP_reportservice.getM_SPExcel(filename, reportId, fromdate, todate, currency, dtltype,
						type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SRWA_12F":
			try {
				repfile = BRRS_M_SRWA_12F_reportservice.getM_SRWA_12FExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SRWA_12B":
			try {
				repfile = brrs_m_srwa_12b_reportservice.getM_SRWA_12BExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SRWA_12C":
			try {
				repfile = BRRS_M_SRWA_12C_reportservice.getBRRS_M_SRWA_12CExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SRWA_12G":
			try {
				repfile = BRRS_M_SRWA_12G_reportservice.getM_SRWA_12GExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SRWA_12H":
			try {
				repfile = BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
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
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_PI":
			try {
				repfile = BRRS_M_PI_reportservice.BRRS_M_PIExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_LA1":
			try {
				repfile = BRRS_M_LA1_reportservice.BRRS_M_LA1Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_LA2":
			try {
				repfile = BRRS_M_LA2_reportservice.BRRS_M_LA2Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_LA3":
			try {
				repfile = BRRS_M_LA3_reportservice.BRRS_M_LA3Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_LA5":
			try {
				repfile = BRRS_M_LA5_reportservice.BRRS_M_LA5Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_DEP1":
			try {
				repfile = BRRS_M_DEP1_reportservice.BRRS_M_DEP1Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_UNCONS_INVEST":
			try {
				repfile = BRRS_M_UNCONS_INVEST_reportservice.BRRS_M_UNCONS_INVESTExcel(filename, reportId, fromdate,
						todate, currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_DEP2":
			try {
				repfile = BRRS_M_DEP2_reportservice.BRRS_M_DEP2Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_PLL":
			try {
				repfile = BRRS_M_PLL_reportservice.getM_PLLExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_DEP3":
			try {
				repfile = BRRS_M_DEP3_reportservice.BRRS_M_DEP3Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_AIDP":
			try {
				repfile = BRRS_M_AIDP_ReportService.getM_AIDPExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_IRB":
			try {
				repfile = brrs_m_irb_reportService.BRRS_M_PIExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_LIQ":
			try {
				repfile = BRRS_M_LIQ_reportservice.getM_LIQExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

			case "Q_STAFF":
				try {
					repfile = BRRS_Q_STAFF_report_service.BRRS_Q_STAFFExcel(filename, reportId, fromdate, todate,
							currency, dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

		case "Q_BRANCHNET":
			try {
				repfile = BRRS_Q_BRANCHNET_reportservice.BRRS_Q_BRANCHNETExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SECL":
			try {
				repfile = brrs_m_secl_reportservice.getM_SECLExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_INT_RATES":
			try {
				repfile = brrs_m_int_rates_reportservice.getM_INTRATESExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_LARADV":
			try {
				repfile = brrs_m_laradv_reportservice.getM_LARADVExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_FXR":
			try {
				repfile = BRRS_M_FXR_reportservice.getM_FXRExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "Q_SMME":
			try {
				repfile = BRRS_Q_SMME_Intrest_Income_ReportService.getQ_SMMEExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "M_SIR":
			try {
				repfile = BRRS_M_SIR_ReportService.getM_SIRExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_EPR":
			try {

				repfile = brrs_m_epr_reportservice.getM_EPRExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SRWA_12A":
			try {

				repfile = brrs_m_srwa_12a_reportservice.getM_SRWA_12AExcel(filename, reportId, fromdate, todate,
						currency, dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_OB":
			try {
				repfile = BRRS_M_OB_ReportService.BRRS_M_OBExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "Q_RLFA1":
			try {

				repfile = brrs_q_rlfa1_reportservice.getQ_RLFA1Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "Q_RLFA2":
			try {

				repfile = brrs_q_rlfa2_reportservice.getQ_RLFA2Excel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_RPD":
			try {
				repfile = BRRS_M_RPD_ReportService.getM_RPDExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_OPTR":
			try {
				repfile = BRRS_M_OPTR_ReportService.getM_OPTRExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
			
			
			
			
		case "Q_SMME_DEP":
			try {
				
				repfile = BRRS_Q_SMME_DEP_ReportService.getQ_SMME_DEPExcel(filename, reportId, fromdate, todate, currency, dtltype,type,version);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_BOP":
			try {
				
				repfile = BRRS_M_BOP_ReportService.getBRRS_M_BOPExcel(filename, reportId, fromdate, todate, currency, dtltype,type,version);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_SECA":
			try {
				
				repfile = BRRS_M_SECA_ReportService.BRRS_M_SECAExcel(filename, reportId, fromdate, todate, currency, dtltype,type,version);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "M_GP":
			try {
				
				repfile = BRRS_M_GP_ReportService.getM_GPExcel(filename, reportId, fromdate, todate, currency, dtltype,type,version);

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

		if ("MSFinP1Detail".equals(filename)) {
			return BRRS_M_SFINP1_reportservice.getM_SFINP1DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		}

		if ("MLA4Detail".equals(filename)) {
			return BRRS_M_LA4_reportservice.BRRS_M_LA4DetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}

		if ("MISDetail".equals(filename)) {
			return BRRS_M_IS_reportservice.BRRS_M_ISDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}

		if ("MCA2Detail".equals(filename)) {
			return BRRS_M_CA2_reportservice.getM_CA2DetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}

		/*
		 * if ("MCA5Detail".equals(filename)) { return
		 * BRRS_M_CA5_reportservice.BRRS_M_CA5DetailExcel(filename, fromdate, todate,
		 * currency, dtltype, type, version); }
		 */

		if ("M_SPDetail".equals(filename)) {
			return BRRS_M_SP_reportservice.getM_SPDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}

		// if ("M_SRWA_12HDetail".equals(filename)) {
		// 	return BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HDetailExcel(filename, fromdate, todate, currency,
		// 			dtltype, type, version);
		// }

		if ("M_MRCDetail".equals(filename)) {
			return BRRS_M_MRC_reportservice.BRRS_M_MRCDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}

		if ("M_CA1Detail".equals(filename)) {
			return BRRS_M_CA1_reportservice.BRRS_M_CA1DetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}

		if ("M_PIDetail".equals(filename)) {
			return BRRS_M_PI_reportservice.BRRS_M_PIDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}

		if ("M_LA1Detail".equals(filename)) {
			return BRRS_M_LA1_reportservice.BRRS_M_LA1DetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}

		if ("M_DEP1Detail".equals(filename)) {
			return BRRS_M_DEP1_reportservice.BRRS_M_DEP1DetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}
		if ("M_LA3Detail".equals(filename)) {
			return BRRS_M_LA3_reportservice.BRRS_M_LA3DetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}
		if ("M_LA5Detail".equals(filename)) {
			return BRRS_M_LA5_reportservice.BRRS_M_LA5DetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}
		if ("M_DEP2Detail".equals(filename)) {
			return BRRS_M_DEP2_reportservice.BRRS_M_DEP2DetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}
		if ("M_PLLDetail".equals(filename)) {
			return BRRS_M_PLL_reportservice.getM_PLLDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}
		if ("M_DEP3Detail".equals(filename)) {
			return BRRS_M_DEP3_reportservice.BRRS_M_DEP3DetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}
		if ("M_IRBDetail".equals(filename)) {
			return brrs_m_irb_reportService.BRRS_M_IRBDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}
		if ("M_LIQDetail".equals(filename)) {
			return BRRS_M_LIQ_reportservice.getM_LIQDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		}
		if ("Q_SUMMEDetail".equals(filename)) {
			return BRRS_Q_SMME_Intrest_Income_ReportService.BRRS_Q_SMMEDetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);
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

		case "M_SFINP1":
			try {
				archivalData = BRRS_M_SFINP1_reportservice.getM_SFINP1Archival();
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

		case "M_CA2":
			try {
				archivalData = BRRS_M_CA2_reportservice.getM_CA2Archival();
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

		case "M_CA6":
			try {
				archivalData = BRRS_M_CA6_reportservice.getM_CA6Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_CA7":
			try {
				archivalData = BRRS_M_CA7_reportservice.getM_CA7Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_CR":
			try {
				archivalData = BRRS_M_CR_reportservice.getM_CRArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SECL":
			try {
				archivalData = brrs_m_secl_reportservice.getM_SECLArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_INT_RATES":
			try {
				archivalData = brrs_m_int_rates_reportservice.getM_INTRATESArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SP":
			try {
				archivalData = BRRS_M_SP_reportservice.getM_SPArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		/*
		 * case "M_SRWA_12C": try { archivalData =
		 * BRRS_M_SRWA_12C_reportservice.getM_SRWA_12CArchival(); } catch (Exception e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); } break;
		 */

		case "M_SRWA_12F":
			try {
				archivalData = BRRS_M_SRWA_12F_reportservice.getM_SRWA_12FArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		// case "M_SRWA_12H":
		// 	try {
		// 		archivalData = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HArchival();
		// 	} catch (Exception e) {
		// 		// TODO Auto-generated catch block
		// 		e.printStackTrace();
		// 	}
		// 	break;

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

		case "M_FXR":
			try {
				archivalData = BRRS_M_FXR_reportservice.getM_FXRArchival();
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

		case "M_LA5":
			try {
				archivalData = BRRS_M_LA5_reportservice.getM_LA5Archival();

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

		case "M_DEP3":
			try {
				archivalData = BRRS_M_DEP3_reportservice.getM_DEP3Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_IRB":
			try {
				archivalData = brrs_m_irb_reportService.getM_IRBArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_LIQ":
			try {
				archivalData = BRRS_M_LIQ_reportservice.getM_LIQArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_SEC":
			try {
				archivalData = brrs_m_sec_reportservice.getM_SECArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_LARADV":
			try {
				archivalData = brrs_m_laradv_reportservice.getM_LARADVArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_INT_RATES_FCA":
			try {
				archivalData = brrs_m_int_rates_fca_reportservice.getM_INTRATESFCAArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			

		// case "Q_STAFF":
		// 	try {
		// 		archivalData = BRRS_Q_STAFF_reportservice.getQ_STAFFArchival();
		// 	} catch (Exception e) {
		// 		// TODO Auto-generated catch block
		// 		e.printStackTrace();
		// 	}

		case "M_SRWA_12B":
			try {
				archivalData = brrs_m_srwa_12b_reportservice.getM_SRWA_12BArchival();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;

		case "Q_BRANCHNET":
			try {
				archivalData = BRRS_Q_BRANCHNET_reportservice.getQ_BRANCHNETArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "Q_SMME":
			try {
				archivalData = BRRS_Q_SMME_Intrest_Income_ReportService.getQ_SMMEArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_SIR":
			try {
				archivalData = BRRS_M_SIR_ReportService.getM_SIRArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		/*
		 * case "M_EPR": try { archivalData =
		 * brrs_m_epr_reportservice.getM_EPRArchival(); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } break;
		 */

		case "M_SRWA_12A":
			try {
				archivalData = brrs_m_srwa_12a_reportservice.getM_SRWA_12AArchival();
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

		case "M_OB":
			try {
				archivalData = BRRS_M_OB_ReportService.getM_OBArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "Q_RLFA1":
			try {
				archivalData = brrs_q_rlfa1_reportservice.getQ_RLFA1Archival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		

		case "M_RPD":
			try {
				archivalData = BRRS_M_RPD_ReportService.getM_RPDarchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "M_OPTR":
			try {
				archivalData = BRRS_M_OPTR_ReportService.getM_OPTRArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "Q_SMME_DEP":
			try {
				archivalData = BRRS_Q_SMME_DEP_ReportService.getQ_SMME_DEPArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "M_BOP":
			try {
				archivalData = BRRS_M_BOP_ReportService.getM_BOPArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		case "M_SECA":
			try {
				archivalData = BRRS_M_SECA_ReportService.getM_SECAArchival();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		
	//New Archival 
				case "M_SRWA_12H":
				List<Object[]> srwaList = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HArchival();
				archivalData.addAll(srwaList);
				System.out.println("Fetched M_SRWA_12H archival data: " + srwaList.size());
				break;

			case "Q_STAFF":
				List<Object[]> QSList = BRRS_Q_STAFF_report_service.getQ_STAFFArchival();
				archivalData.addAll(QSList);
				System.out.println("Fetched M_SRWA_12H archival data: " + QSList.size());
				break;
				
			case "M_CA4":
				List<Object[]> ca4List = BRRS_M_CA4_reportservice.getM_CA4Archival();
				archivalData.addAll(ca4List);
				System.out.println("Fetched M_CA4 archival data: " + ca4List.size());
				break;
				
				
			case "Q_RLFA2":
				List<Object[]> rlfa2List = brrs_q_rlfa2_reportservice.getQ_RLFA2Archival();
				archivalData.addAll(rlfa2List);
				System.out.println("Fetched M_CA4 archival data: " + rlfa2List.size());
				break;
				
			
			 case "M_EPR":
		          List<Object[]> eprList = brrs_m_epr_reportservice.getM_EPRArchival();
		          archivalData.addAll(eprList);
		          System.out.println("Fetched M_EPR archival data: " + eprList.size());
		          break;

				
			 case "M_SRWA_12G":
	              List<Object[]> srwagList = BRRS_M_SRWA_12G_reportservice.getM_SRWA_12GArchival();
	              archivalData.addAll(srwagList);
	              System.out.println("Fetched M_SRWA_12G archival data: " + srwagList.size());
	              break;
	          default:
	              System.out.println("No archival logic defined for report: " + rptcode);
	              break;


			case "M_GP":
				try {
					archivalData = BRRS_M_GP_ReportService.getM_GPArchival();
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
	public void generateReportAsync(String jobId, String filename, String fromdate, String todate, String dtltype,
			String type, String currency, String version) {
		System.out.println("Starting report generation for: " + filename);

		byte[] fileData = null;

		if (filename.equals("MSFinP2Detail")) {
			fileData = BRRS_M_SFINP2_reportservice.BRRS_M_SFINP2DetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);
		} else if (filename.equals("MSFinP1Detail")) {
			fileData = BRRS_M_SFINP1_reportservice.getM_SFINP1DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("MLA4Detail")) {
			fileData = BRRS_M_LA4_reportservice.BRRS_M_LA4DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_ISDetail")) {
			fileData = BRRS_M_IS_reportservice.BRRS_M_ISDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		// } else if (filename.equals("M_SRWA_12HDetail")) {
		// 	fileData = BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HDetailExcel(filename, fromdate, todate, currency,
		// 			dtltype, type, version);
		} else if (filename.equals("M_MRCDetail")) {
			fileData = BRRS_M_MRC_reportservice.BRRS_M_MRCDetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} /*
			 * else if (filename.equals("M_SRWA_12CDetail")) { fileData =
			 * BRRS_M_SRWA_12C_reportservice.BRRS_M_SRWA_12CDetailExcel(filename, fromdate,
			 * todate, currency, dtltype, type ,version); }
			 */
		else if (filename.equals("M_CA1Detail")) {
			fileData = BRRS_M_CA1_reportservice.BRRS_M_CA1DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_PIDetail")) {
			fileData = BRRS_M_PI_reportservice.BRRS_M_PIDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		} else if (filename.equals("M_LA1Detail")) {
			fileData = BRRS_M_LA1_reportservice.BRRS_M_LA1DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_DEP1Detail")) {
			fileData = BRRS_M_DEP1_reportservice.BRRS_M_DEP1DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_LA3Detail")) {
			fileData = BRRS_M_LA3_reportservice.BRRS_M_LA3DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_LA5Detail")) {
			fileData = BRRS_M_LA5_reportservice.BRRS_M_LA5DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_DEP2Detail")) {
			fileData = BRRS_M_DEP2_reportservice.BRRS_M_DEP2DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_PLLDetail")) {
			fileData = BRRS_M_PLL_reportservice.getM_PLLDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		} else if (filename.equals("M_DEP3Detail")) {
			fileData = BRRS_M_DEP3_reportservice.BRRS_M_DEP3DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_IRBDetail")) {
			fileData = brrs_m_irb_reportService.BRRS_M_IRBDetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_LIQDetail")) {
			fileData = BRRS_M_LIQ_reportservice.getM_LIQDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		} else if (filename.equals("M_SMMEDetail")) {
			fileData = BRRS_Q_SMME_Intrest_Income_ReportService.BRRS_Q_SMMEDetailExcel(filename, fromdate, todate,
					currency, dtltype, type, version);
		}else if (filename.equals("M_CA2Detail")) {
			fileData = BRRS_M_CA2_reportservice.getM_CA2DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} 

		else if ("M_SRWA_12A".equals(filename)) {

			fileData = brrs_m_srwa_12a_reportservice.getM_SRWA_12ADetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);

		}

		if (fileData == null) {
			// logger.warn("Excel generation failed or no data for jobId: {}", jobId);
			jobStorage.put(jobId, null);
		} else {
			jobStorage.put(jobId, fileData);
		}

		System.out.println("Report generation completed for: " + filename);
	}

	public byte[] getReport(String jobId) {
		// System.out.println("Report generation completed for: " + jobId);
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

	public ModelAndView getReportDetails(String reportId, HttpServletRequest request) {
		logger.info("Routing GET detail request for Report ID: {}", reportId);

		ModelAndView modelAndView; // declare once

		try {
			switch (reportId) {
			case "M_PLL":
				modelAndView = BRRS_M_PLL_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
						request.getParameter("formmode"));
				break;

			
			  case "M_LA1": modelAndView = BRRS_M_LA1_reportservice.getViewOrEditPage(
			  request.getParameter("acctNo"), request.getParameter("formmode") );
			  break;
			  
			  case "M_LA3":
					modelAndView = BRRS_M_LA3_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;
					
			  case "M_CA2":
					modelAndView = BRRS_M_CA2_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;
			 

			default:
				logger.warn("No detail service found for reportId: {}", reportId);
				modelAndView = new ModelAndView("error/report_not_found");
				modelAndView.addObject("errorMessage",
						"Details view for report '" + reportId + "' is not implemented.");
				break;
			}
		} catch (Exception e) {
			logger.error("Error processing details for reportId: {}", reportId, e);
			modelAndView = new ModelAndView("error/internal_error");
			modelAndView.addObject("errorMessage",
					"An internal server error occurred while processing details: " + e.getMessage());
		}

		return modelAndView;
	}

	public ResponseEntity<?> updateReportDetails(String reportId, HttpServletRequest request) {
		logger.info("Routing POST update request for Report ID: {}", reportId);

		ResponseEntity<?> response; // Declare once

		try {
			switch (reportId) {
			case "M_PLL":
				response = BRRS_M_PLL_reportservice.updateDetailEdit(request);
				break;

			
			  case "M_LA1": response = BRRS_M_LA1_reportservice.updateDetailEdit(request);
			  break;
			  
			  case "M_LA3":
					response = BRRS_M_LA3_reportservice.updateDetailEdit(request);
					break;
					
			  case "M_CA2":
					response = BRRS_M_CA2_reportservice.updateDetailEdit(request);
					break;

			 

			default:
				logger.warn("Unsupported report ID: {}", reportId);
				response = ResponseEntity.badRequest()
						.body("Update functionality is not implemented for this report ID: " + reportId);
				break;
			}

		} catch (Exception e) {
			logger.error("Error processing update for reportId: {}", reportId, e);
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An internal server error occurred during the update: " + e.getMessage());
		}

		return response;
	}
	
	

	
	
	

//Resubmission Add Ur Case Here
public List<Object[]> getResub(String rptcode) {
		List<Object[]> resubmissionData = new ArrayList<>();

		switch (rptcode) {
			case "M_SRWA_12H":
				try {
					List<Object[]> resubList = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_SRWA_12H: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SRWA_12H: " + e.getMessage());
					e.printStackTrace();
				}
				break;

				case "Q_STAFF":
				try {
					List<Object[]> resubList = BRRS_Q_STAFF_report_service.getQ_STAFFResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_SRWA_12H: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SRWA_12H: " + e.getMessage());
					e.printStackTrace();
				}
				break;
				
				 case "M_SRWA_12G":
			            try {
			                List<Object[]> resubList = BRRS_M_SRWA_12G_reportservice.getM_SRWA_12GResub();
			                resubmissionData.addAll(resubList);
			                System.out.println("Resubmission data fetched for M_SRWA_12G: " + resubList.size());
			            } catch (Exception e) {
			                System.err.println("Error fetching resubmission data for M_SRWA_12G: " + e.getMessage());
			                e.printStackTrace();
			            }
			         
			            
			            break;
			            
				 case "M_CA4":
			            try {
			                List<Object[]> resubList = BRRS_M_CA4_reportservice.getM_CA4Resub();
			                resubmissionData.addAll(resubList);
			                System.out.println("Resubmission data fetched for M_CA4: " + resubList.size());
			            } catch (Exception e) {
			                System.err.println("Error fetching resubmission data for M_CA4: " + e.getMessage());
			                e.printStackTrace();
			            }
			            break;
			            
			            
				 case "Q_RLFA2":
			            try {
			                List<Object[]> resubList = brrs_q_rlfa2_reportservice.getQ_RLFA2Resub();
			                resubmissionData.addAll(resubList);
			                System.out.println("Resubmission data fetched for M_CA4: " + resubList.size());
			            } catch (Exception e) {
			                System.err.println("Error fetching resubmission data for M_CA4: " + e.getMessage());
			                e.printStackTrace();
			            }
			            break;

			            
				  case "M_EPR":
			            try {
			                List<Object[]> resubList = brrs_m_epr_reportservice.getM_EPRResub();
			                resubmissionData.addAll(resubList);
			                System.out.println("Resubmission data fetched for M_EPR: " + resubList.size());
			            } catch (Exception e) {
			                System.err.println("Error fetching resubmission data for M_EPR: " + e.getMessage());
			                e.printStackTrace();
			            }
			            break;



			default:
				System.out.println("Unsupported report code: " + rptcode);
		}

		return resubmissionData;
	}




}
