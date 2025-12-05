package com.bornfire.brrs.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
	BRRS_M_AIDP_ReportService BRRS_M_AIDP_reportservice;

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
	BRRS_M_LCR_ReportService BRRS_M_LCR_reportservice;

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
	BRRS_M_OR2_ReportService brrs_m_or2_reportservice;

	@Autowired
	BRRS_M_OR1_ReportService brrs_m_or1_reportservice;

	@Autowired
	BRRS_M_LIQGAP_ReportService brrs_m_liqgap_reportservice;

	@Autowired
	BRRS_Q_SMME_DEP_ReportService BRRS_Q_SMME_DEP_ReportService;

	@Autowired
	BRRS_M_BOP_ReportService BRRS_M_BOP_ReportService;

	@Autowired
	BRRS_M_SECA_ReportService BRRS_M_SECA_ReportService;

	@Autowired
	BRRS_M_GP_ReportService BRRS_M_GP_ReportService;
	@Autowired
	BRRS_M_TBS_ReportService BRRS_M_TBS_ReportService;

	@Autowired
	BRRS_M_NOSVOS_ReportService BRRS_M_NOSVOS_reportservice;
	@Autowired
	BRRS_M_CALOC_ReportService BRRS_M_CALOC_reportService;

	@Autowired
	BRRS_M_GMIRT_ReportService brrs_m_gmirt_reportservice;

	@Autowired
	BRRS_M_GALOR_ReportService BRRS_m_galor_ReportService;

	@Autowired
	BRRS_M_SRWA_12E_ReportService BRRS_M_SRWA_12E_ReportService;

	@Autowired
	BRRS_Q_SMME_loans_Advances_ReportService BRRS_Q_SMME_loans_Advances_reportService;

	@Autowired
	BRRS_M_FAS_ReportService BRRS_M_FAS_reportservice;

	@Autowired
	BRRS_M_PD_ReportService BRRS_M_PD_ReportService;

	@Autowired
	BRRS_M_DEP4_ReportService BRRS_M_DEP4_ReportService;

	@Autowired
	BRRS_M_SCI_E_ReportService brrs_m_sci_e_reportservice;

	@Autowired
	BRRS_Q_ATF_ReportService brrs_q_atf_reportservice;
	

	@Autowired
	BRRS_M_I_S_CA_ReportService brrs_m_i_s_ca_reportservice;

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
				repsummary = BRRS_M_LA4_reportservice.getM_LA4View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_IS":
				repsummary = BRRS_M_IS_reportservice.getM_ISView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_CA4":
				repsummary = BRRS_M_CA4_reportservice.getBRRS_M_CA4View(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_CA2":
				repsummary = BRRS_M_CA2_reportservice.getM_CA2View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_FAS":
				repsummary = BRRS_M_FAS_reportservice.getBRRS_M_FASView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "Q_SMME_LA":
				repsummary = BRRS_Q_SMME_loans_Advances_reportService.getBRRS_Q_SMMEView(reportId, fromdate, todate,
						currency, dtltype,
						pageable, type, version);
				break;

			case "M_OR1":
				repsummary = brrs_m_or1_reportservice.getM_OR1View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_CA6":
				repsummary = BRRS_M_CA6_reportservice.getM_CA6View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;
			case "M_CALOC":
				repsummary = BRRS_M_CALOC_reportService.getBRRS_M_CALOCview(reportId, fromdate, todate, currency,
						dtltype, pageable, type, version);
				break;

			case "M_CA5":
				repsummary = BRRS_M_CA5_reportservice.getM_CA5View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_CR":
				repsummary = BRRS_M_CR_reportservice.getM_CRView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_SRWA_12F":
				repsummary = BRRS_M_SRWA_12F_reportservice.getM_SRWA12FView(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);
				break;

			case "M_SRWA_12C":
				repsummary = BRRS_M_SRWA_12C_reportservice.getBRRS_M_SRWA_12CView(reportId, fromdate, todate, currency,
						dtltype, pageable, type, version);
				break;

			case "M_SRWA_12G":
				repsummary = BRRS_M_SRWA_12G_reportservice.getM_SRWA_12GView(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);
				break;

			case "M_SRWA_12H":
				repsummary = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HView(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);
				break;

			case "M_MRC":
				repsummary = BRRS_M_MRC_reportservice.getM_MRCview(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_CA1":
				repsummary = BRRS_M_CA1_reportservice.getM_CA1View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_CA3":
				repsummary = BRRS_M_CA3_reportservice.getM_CA3View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_CA7":
				repsummary = BRRS_M_CA7_reportservice.getM_CA7View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_OR2":
				repsummary = brrs_m_or2_reportservice.getM_OR2View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_LIQGAP":
				repsummary = brrs_m_liqgap_reportservice.getM_LIQGAPView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_SP":
				repsummary = BRRS_M_SP_reportservice.getM_SPView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_PI":
				repsummary = BRRS_M_PI_reportservice.getM_PIView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_LA1":
				repsummary = BRRS_M_LA1_reportservice.getM_LA1View(reportId, fromdate, todate, currency, dtltype,
						pageable,
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
				repsummary = BRRS_M_UNCONS_INVEST_reportservice.getM_UNCONS_INVESTView(reportId, fromdate, todate,
						currency,
						dtltype, pageable, type, version);
				break;

			case "M_LA2":
				repsummary = BRRS_M_LA2_reportservice.getM_LA2View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_LA3":
				repsummary = BRRS_M_LA3_reportservice.getM_LA3View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_LA5":
				repsummary = BRRS_M_LA5_reportservice.getM_LA5View(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_DEP2":
				repsummary = BRRS_M_DEP2_reportservice.getM_DEP2View(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_PLL":
				repsummary = BRRS_M_PLL_reportservice.getM_PLLView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_DEP3":
				repsummary = BRRS_M_DEP3_reportservice.getM_DEP3View(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_IRB":
				repsummary = brrs_m_irb_reportService.getM_IRBView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_LIQ":
				repsummary = BRRS_M_LIQ_reportservice.getM_LIQView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_LCR":
				repsummary = BRRS_M_LCR_reportservice.getM_LCRView(reportId, fromdate, todate, currency, dtltype,
						pageable,
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
				repsummary = BRRS_M_FXR_reportservice.getM_FXRView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_SRWA_12B":

				repsummary = brrs_m_srwa_12b_reportservice.getM_SRWA_12BView(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);
				break;

			case "M_SECL":
				repsummary = brrs_m_secl_reportservice.getM_SECLView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_INT_RATES":
				repsummary = brrs_m_int_rates_reportservice.getM_INTRATESView(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);
				break;

			case "M_LARADV":
				repsummary = brrs_m_laradv_reportservice.getM_LARADVView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "Q_SMME":

				repsummary = BRRS_Q_SMME_Intrest_Income_ReportService.getBRRS_Q_SMMEView(reportId, fromdate, todate,
						currency, dtltype, pageable, type, version);
				break;

			case "M_SIR":

				repsummary = BRRS_M_SIR_ReportService.getM_SIRView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_EPR":

				repsummary = brrs_m_epr_reportservice.getM_EPRView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_SRWA_12A":

				repsummary = brrs_m_srwa_12a_reportservice.getM_SRWA_12AView(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);
				break;

			case "M_OB":
				repsummary = BRRS_M_OB_ReportService.getM_OBview(reportId, fromdate, todate, currency, dtltype,
						pageable,
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
				repsummary = BRRS_M_RPD_ReportService.getM_RPDView(reportId, fromdate, todate, currency, dtltype,
						pageable,
						type, version);
				break;

			case "M_OPTR":
				repsummary = BRRS_M_OPTR_ReportService.getM_OPTRView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_INT_RATES_FCA":
				repsummary = brrs_m_int_rates_fca_reportservice.getM_INTRATESFCAView(reportId, fromdate, todate,
						currency, dtltype, pageable, type, version);
				break;

			case "M_SEC":
				repsummary = brrs_m_sec_reportservice.getM_SECView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "Q_SMME_DEP":

				repsummary = BRRS_Q_SMME_DEP_ReportService.getQ_SMME_DEPview(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);
				break;

			case "M_BOP":

				repsummary = BRRS_M_BOP_ReportService.getBRRS_M_BOPview(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_SECA":
				repsummary = BRRS_M_SECA_ReportService.getM_SECAview(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_GP":
				repsummary = BRRS_M_GP_ReportService.getM_GPView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_TBS":
				repsummary = BRRS_M_TBS_ReportService.getM_TBSView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_NOSVOS":
				repsummary = BRRS_M_NOSVOS_reportservice.getM_NOSVOSView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_GMIRT":

				repsummary = brrs_m_gmirt_reportservice.getM_GMIRTView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_GALOR":

				repsummary = BRRS_m_galor_ReportService.getM_GALORView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "M_SRWA_12E":
				repsummary = BRRS_M_SRWA_12E_ReportService.getM_SRWA_12EView(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);

				break;

			case "M_PD":
				repsummary = BRRS_M_PD_ReportService.getM_PDview(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);

				break;
			case "M_DEP4":
				repsummary = BRRS_M_DEP4_ReportService.getM_DEP4View(reportId, fromdate, todate, currency,
						dtltype,
						pageable, type, version);

				break;

			case "M_SCI_E":

				repsummary = brrs_m_sci_e_reportservice.getM_SCI_EView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);
				break;

			case "Q_ATF":

				repsummary = brrs_q_atf_reportservice.getQ_ATFView(reportId, fromdate, todate, currency, dtltype,
						pageable, type, version);

				break;
				
				
				
			case "M_I_S_CA":

				repsummary = brrs_m_i_s_ca_reportservice.getM_I_S_CAView(reportId, fromdate, todate, currency, dtltype,
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
				repdetail = BRRS_M_SFINP2_reportservice.getM_SFINP2currentDtl(reportId, fromdate, todate, currency,
						dtltype,
						pageable, Filter, type, version);
				break;

			case "M_SFINP1":
				repdetail = BRRS_M_SFINP1_reportservice.getM_SFINP1currentDtl(reportId, fromdate, todate, currency,
						dtltype,
						pageable, Filter, type, version);
				break;

			case "M_LA4":
				repdetail = BRRS_M_LA4_reportservice.getM_LA4currentDtl(reportId, fromdate, todate, currency, dtltype,
						pageable, Filter, type, version);
				break;
			case "M_CALOC":

				repdetail = BRRS_M_CALOC_reportService.getM_CALOCcurrentDtl(reportId, fromdate, todate, currency,
						dtltype, pageable, Filter, type, version);
				break;

			case "M_IS":
				repdetail = BRRS_M_IS_reportservice.getM_IScurrentDtl(reportId, fromdate, todate, currency, dtltype,
						pageable, Filter, type, version);
				break;

			case "M_CA2":
				repdetail = BRRS_M_CA2_reportservice.getM_CA2currentDtl(reportId, fromdate, todate, currency, dtltype,
						pageable, Filter, type, version);
				break;

			case "M_OR1":
				repdetail = brrs_m_or1_reportservice.getM_OR1currentDtl(reportId, fromdate, todate, currency, dtltype,
						pageable, Filter, type, version);
				break;

			case "M_LIQGAP":
				repdetail = brrs_m_liqgap_reportservice.getM_LIQGAPcurrentDtl(reportId, fromdate, todate, currency,
						dtltype,
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

			case "M_FAS":
				repdetail = BRRS_M_FAS_reportservice.getBRRS_M_FAScurrentDtl(reportId, fromdate, todate, currency,
						dtltype, pageable, Filter, type, version);
				break;

			case "Q_SMME_LA":
				repdetail = BRRS_Q_SMME_loans_Advances_reportService.getBRRS_Q_SMMEcurrentDtl(reportId, fromdate,
						todate, currency,
						dtltype,
						pageable, Filter, type, version);
				break;

			// case "M_SRWA_12H":
			// repdetail = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HcurrentDtl(reportId,
			// fromdate, todate, currency,
			// dtltype, pageable, Filter, type, version);
			// break;

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

			case "M_LCR":
				repdetail = BRRS_M_LCR_reportservice.getM_LCRcurrentDtl(reportId, fromdate, todate, currency, dtltype,
						pageable, Filter, type, version);
				break;

			case "Q_SMME":
				repdetail = BRRS_Q_SMME_Intrest_Income_ReportService.getBRRS_Q_SMMEcurrentDtl(reportId, fromdate,
						todate,
						currency, dtltype, pageable, Filter, type, version);
				break;

			case "M_IRB":
				repdetail = brrs_m_irb_reportService.getM_IRBcurrentDtl(reportId, fromdate, todate, currency, dtltype,
						pageable, Filter, type, version);

			case "M_SRWA_12A":

				repdetail = brrs_m_srwa_12a_reportservice.getM_SRWA_12AcurrentDtl(reportId, fromdate, todate, currency,
						dtltype, pageable, Filter, type, version);
				break;

			case "M_GMIRT":

				repdetail = brrs_m_gmirt_reportservice.getM_GMIRTcurrentDtl(reportId, fromdate, todate, currency,
						dtltype,
						pageable, Filter, type, version);
				break;

			case "M_GALOR":

				repdetail = BRRS_m_galor_ReportService.getM_GALORcurrentDtl(reportId, fromdate, todate, currency,
						dtltype, pageable, Filter, type, version);
				break;

			case "M_PD":

				repdetail = BRRS_M_PD_ReportService.getM_PDcurrentDtl(reportId, fromdate, todate, currency,
						dtltype, pageable, Filter, type, version);
				break;

			case "Q_ATF":

				repdetail = brrs_q_atf_reportservice.getQ_ATFcurrentDtl(reportId, fromdate, todate, currency, dtltype,
						pageable, Filter, type, version);
				break;

			case "M_SCI_E":

				repdetail = brrs_m_sci_e_reportservice.getM_SCI_EcurrentDtl(reportId, fromdate, todate, currency,
						dtltype,
						pageable, Filter, type, version);
				break;
				
				
			case "M_I_S_CA":

					repdetail = brrs_m_i_s_ca_reportservice.getM_I_S_CAcurrentDtl(reportId, fromdate, todate, currency,
							dtltype,
							pageable, Filter, type, version);
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
					repfile = BRRS_M_SFINP2_reportservice.BRRS_M_SFINP2Excel(filename, reportId, fromdate, todate,
							currency,
							dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_SFINP1":
				try {
					repfile = BRRS_M_SFINP1_reportservice.getM_SFINP1Excel(filename, reportId, fromdate, todate,
							currency,
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

			case "M_OR1":
				try {
					repfile = brrs_m_or1_reportservice.BRRS_M_OR1Excel(filename, reportId, fromdate, todate, currency,
							dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_LIQGAP":
				try {
					repfile = brrs_m_liqgap_reportservice.getM_LIQGAPExcel(filename, reportId, fromdate, todate,
							currency,
							dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_CA4":
				try {
					repfile = BRRS_M_CA4_reportservice.getBRRS_M_CA4Excel(filename, reportId, fromdate, todate,
							currency,
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
					repfile = brrs_m_sec_reportservice.getM_SECExcel(filename, reportId, fromdate, todate, currency,
							dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_INT_RATES_FCA":
				try {
					repfile = brrs_m_int_rates_fca_reportservice.getM_INTRATESFCAExcel(filename, reportId, fromdate,
							todate, currency, dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "M_FAS":
				try {
					repfile = BRRS_M_FAS_reportservice.getM_FASExcel(filename, reportId, fromdate, todate, currency,
							version, type,
							dtltype);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "Q_SMME_LA":
				try {
					repfile = BRRS_Q_SMME_loans_Advances_reportService.getQ_SMMEExcel(filename, reportId, fromdate,
							todate,
							currency,
							dtltype, type, version);
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
					repfile = BRRS_M_SP_reportservice.getM_SPExcel(filename, reportId, fromdate, todate, currency,
							dtltype,
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

			case "M_CALOC":
				try {
					repfile = BRRS_M_CALOC_reportService.getBRRS_M_CALOCExcel(filename, reportId, fromdate, todate,
							currency,
							dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_SRWA_12C":
				try {
					repfile = BRRS_M_SRWA_12C_reportservice.getBRRS_M_SRWA_12CExcel(filename, reportId, fromdate,
							todate,
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
							dtltype, type, version);
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

			case "M_LCR":
				try {
					repfile = BRRS_M_LCR_reportservice.getM_LCRExcel(filename, reportId, fromdate, todate, currency,
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

			case "M_OR2":
				try {
					repfile = brrs_m_or2_reportservice.getM_OR2Excel(filename, reportId, fromdate, todate,
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
					repfile = BRRS_Q_SMME_Intrest_Income_ReportService.getQ_SMMEExcel(filename, reportId, fromdate,
							todate,
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
					repfile = BRRS_M_OB_ReportService.getM_OBExcel(filename, reportId, fromdate, todate, currency,
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

					repfile = BRRS_Q_SMME_DEP_ReportService.getQ_SMME_DEPExcel(filename, reportId, fromdate, todate,
							currency, dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_BOP":
				try {

					repfile = BRRS_M_BOP_ReportService.getBRRS_M_BOPExcel(filename, reportId, fromdate, todate,
							currency, dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_SECA":
				try {

					repfile = BRRS_M_SECA_ReportService.BRRS_M_SECAExcel(filename, reportId, fromdate, todate, currency,
							dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "M_GP":
				try {

					repfile = BRRS_M_GP_ReportService.getM_GPExcel(filename, reportId, fromdate, todate, currency,
							dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_TBS":
				try {

					repfile = BRRS_M_TBS_ReportService.getM_TBSExcel(filename, reportId, fromdate, todate, currency,
							dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_GMIRT":
				try {

					repfile = brrs_m_gmirt_reportservice.getM_GMIRTExcel(filename, reportId, fromdate, todate, currency,
							dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_GALOR":
				try {

					repfile = BRRS_m_galor_ReportService.getM_GALORExcel(filename, reportId, fromdate, todate, currency,
							dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_NOSVOS":
				try {
					repfile = BRRS_M_NOSVOS_reportservice.getM_NOSVOSExcel(filename, reportId, fromdate, todate,
							currency,
							dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_SRWA_12E":
				try {
					repfile = BRRS_M_SRWA_12E_ReportService.BRRS_M_SRWA_12EExcel(filename, reportId, fromdate, todate,
							currency, dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_PD":
				try {
					repfile = BRRS_M_PD_ReportService.BRRS_M_PDExcel(filename, reportId, fromdate, todate,
							currency, dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "M_DEP4":
				try {
					repfile = BRRS_M_DEP4_ReportService.getM_dep4Excel(filename, reportId, fromdate, todate,
							currency, dtltype, type, version);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "Q_ATF":
				try {

					repfile = brrs_q_atf_reportservice.getBRRS_Q_ATFExcel(filename, reportId, fromdate, todate,
							currency, dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_SCI_E":
				try {

					repfile = brrs_m_sci_e_reportservice.getM_SCI_EExcel(filename, reportId, fromdate, todate, currency,
							dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
				
				
			case "M_I_S_CA":
				try {

					repfile = brrs_m_i_s_ca_reportservice.getM_I_S_CAExcel(filename, reportId, fromdate, todate, currency,
							dtltype, type, version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
								
				
				
				
				

		}
		return repfile;
	}

	public byte[] getDownloadDetailFile(String filename, String fromdate, String todate,
			String currency, String dtltype, String type, String version) {

		System.out.println("came to common service");

		if ("MSFinP2Detail".equals(filename)) {
			return BRRS_M_SFINP2_reportservice.BRRS_M_SFINP2DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("MSFinP1Detail".equals(filename)) {
			return BRRS_M_SFINP1_reportservice.getM_SFINP1DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("MLA4Detail".equals(filename)) {
			return BRRS_M_LA4_reportservice.BRRS_M_LA4DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("MISDetail".equals(filename)) {
			return BRRS_M_IS_reportservice.BRRS_M_ISDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("MCA2Detail".equals(filename)) {
			return BRRS_M_CA2_reportservice.getM_CA2DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("MOR1Detail".equals(filename)) {
			return brrs_m_or1_reportservice.BRRS_M_OR1DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("MLIQGAPDetail".equals(filename)) {
			return brrs_m_liqgap_reportservice.getM_LIQGAPDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_SPDetail".equals(filename)) {
			return BRRS_M_SP_reportservice.getM_SPDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_MRCDetail".equals(filename)) {
			return BRRS_M_MRC_reportservice.getM_MRCDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_CA1Detail".equals(filename)) {
			return BRRS_M_CA1_reportservice.BRRS_M_CA1DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_PIDetail".equals(filename)) {
			return BRRS_M_PI_reportservice.BRRS_M_PIDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_LA1Detail".equals(filename)) {
			return BRRS_M_LA1_reportservice.BRRS_M_LA1DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_DEP1Detail".equals(filename)) {
			return BRRS_M_DEP1_reportservice.BRRS_M_DEP1DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_LA3Detail".equals(filename)) {
			return BRRS_M_LA3_reportservice.BRRS_M_LA3DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_LA5Detail".equals(filename)) {
			return BRRS_M_LA5_reportservice.BRRS_M_LA5DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_DEP2Detail".equals(filename)) {
			return BRRS_M_DEP2_reportservice.BRRS_M_DEP2DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_PLLDetail".equals(filename)) {
			return BRRS_M_PLL_reportservice.getM_PLLDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_DEP3Detail".equals(filename)) {
			return BRRS_M_DEP3_reportservice.getM_DEP3DetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_IRBDetail".equals(filename)) {
			return brrs_m_irb_reportService.BRRS_M_IRBDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_LIQDetail".equals(filename)) {
			return BRRS_M_LIQ_reportservice.getM_LIQDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("Q_SUMMEDetail".equals(filename)) {
			return BRRS_Q_SMME_Intrest_Income_ReportService.BRRS_Q_SMMEDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_LIQ".equals(filename)) {
			return BRRS_M_LIQ_reportservice.getM_LIQDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_LCRDetail".equals(filename)) {
			return BRRS_M_LCR_reportservice.getM_LCRDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		} else if ("M_FASDetail".equals(filename)) {
			return BRRS_M_FAS_reportservice.BRRS_M_FASDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		} else if ("Q_SMMEDetail".equals(filename)) {
			return BRRS_Q_SMME_Intrest_Income_ReportService.BRRS_Q_SMMEDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
		}

		else if ("M_PDDetail".equals(filename)) {
			return BRRS_M_PD_ReportService.BRRS_M_PDDetailExcel(
					filename, fromdate, todate, currency, dtltype, type, version);
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

			// case "M_IS":
			// try {
			// archivalData = BRRS_M_IS_reportservice.getM_ISArchival();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;

			case "M_CA2":
				try {
					archivalData = BRRS_M_CA2_reportservice.getM_CA2Archival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_OR1":
				try {
					archivalData = brrs_m_or1_reportservice.getM_OR1Archival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_LIQGAP":
				try {
					archivalData = brrs_m_liqgap_reportservice.getM_LIQGAPArchival();
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

			// case "M_SRWA_12H":
			// try {
			// archivalData = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HArchival();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;

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

			case "M_CALOC":
				try {
					archivalData = BRRS_M_CALOC_reportService.getM_CALOCArchival();
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
				List<Object[]> fxrList = BRRS_M_FXR_reportservice.getM_FXRArchival();
				archivalData.addAll(fxrList);
				System.out.println("Fetched M_SRWA_12H archival data: " + fxrList.size());
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

			/*
			 * case "M_CA3": try { archivalData =
			 * BRRS_M_CA3_reportservice.getM_CA3Archival(); } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } break;
			 */

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

			case "M_LCR":
				try {
					archivalData = BRRS_M_LCR_reportservice.getM_LCRArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			// case "M_LARADV":
			// try {
			// archivalData = brrs_m_laradv_reportservice.getM_LARADVArchival();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;

			case "M_INT_RATES_FCA":
				List<Object[]> intratesfcaList = brrs_m_int_rates_fca_reportservice.getM_INT_RATES_FCAArchival();
				archivalData.addAll(intratesfcaList);
				System.out.println("Fetched M_INT_RATES_FCA archival data: " + intratesfcaList.size());
				break;

			// case "Q_STAFF":
			// try {
			// archivalData = BRRS_Q_STAFF_reportservice.getQ_STAFFArchival();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			case "M_SRWA_12B":
				List<Object[]> srwabList = brrs_m_srwa_12b_reportservice.getM_SRWA_12BArchival();
				archivalData.addAll(srwabList);
				System.out.println("Fetched M_SRWA_12B archival data: " + srwabList.size());
				break;

			// case "Q_BRANCHNET":
			// try {
			// archivalData = BRRS_Q_BRANCHNET_reportservice.getQ_BRANCHNETArchival();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;

			case "Q_SMME":
				try {
					archivalData = BRRS_Q_SMME_Intrest_Income_ReportService.getQ_SMMEArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "Q_SMME_LA":
				try {
					archivalData = BRRS_Q_SMME_loans_Advances_reportService.getQ_SMMEArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			/*
			 * case "M_SIR": try { archivalData =
			 * BRRS_M_SIR_ReportService.getM_SIRArchival(); } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } break;
			 */

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

			case "M_GALOR":
				try {
					archivalData = BRRS_m_galor_ReportService.getM_GALORArchival();
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

			// case "M_OB":
			// try {
			// archivalData = BRRS_M_OB_ReportService.getM_OBArchival();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;

			case "M_OB":
				List<Object[]> obList = BRRS_M_OB_ReportService.getM_OBArchival();
				archivalData.addAll(obList);
				System.out.println("Fetched M_OB archival data: " + obList.size());
				break;

			/*
			 * case "Q_RLFA1": try { archivalData =
			 * brrs_q_rlfa1_reportservice.getQ_RLFA1Archival(); } catch (Exception e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); } break;
			 */

			// case "M_OPTR":
			// try {
			// archivalData = BRRS_M_OPTR_ReportService.getM_OPTRArchival();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;

			case "M_OPTR":
				List<Object[]> optrList = BRRS_M_OPTR_ReportService.getM_OPTRArchival();
				archivalData.addAll(optrList);
				System.out.println("Fetched M_OPTR archival data: " + optrList.size());
				break;

			/*
			 * case "Q_SMME_DEP": try { archivalData =
			 * BRRS_Q_SMME_DEP_ReportService.getQ_SMME_DEPArchival(); } catch (Exception e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); } break;
			 * 
			 * case "M_BOP": try { archivalData =
			 * BRRS_M_BOP_ReportService.getM_BOPArchival(); } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } break;
			 */

			case "M_SECA":
				try {
					archivalData = BRRS_M_SECA_ReportService.getM_SECAArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_GMIRT":
				try {
					archivalData = brrs_m_gmirt_reportservice.getM_GMIRTArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "Q_ATF":
				try {
					archivalData = brrs_q_atf_reportservice.getQ_ATFArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_SCI_E":
				try {
					archivalData = brrs_m_sci_e_reportservice.getM_SCI_EArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
				
			case "M_I_S_CA":
				try {
					archivalData = brrs_m_i_s_ca_reportservice.getM_I_S_CAArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				

			// New Archival
			case "M_SRWA_12H":
				List<Object[]> srwaList1 = BRRS_M_SRWA_12H_reportservice.getM_SRWA_12HArchival();
				archivalData.addAll(srwaList1);
				System.out.println("Fetched M_SRWA_12H archival data: " + srwaList1.size());
				break;

			case "M_SRWA_12E":
				List<Object[]> srweList1 = BRRS_M_SRWA_12E_ReportService.getM_SRWA_12EArchival();
				archivalData.addAll(srweList1);
				System.out.println("Fetched M_SRWA_12E archival data: " + srweList1.size());
				break;
			case "M_LARADV":
				List<Object[]> LAList = brrs_m_laradv_reportservice.getM_LARADVArchival();
				archivalData.addAll(LAList);
				System.out.println("Fetched M_LARADV archival data: " + LAList.size());
				break;

			case "M_GP":
				List<Object[]> GPList = BRRS_M_GP_ReportService.getM_GPArchival();
				archivalData.addAll(GPList);
				System.out.println("Fetched M_SRWA_12H archival data: " + GPList.size());
				break;

			case "Q_STAFF":
				List<Object[]> QSList = BRRS_Q_STAFF_report_service.getQ_STAFFArchival();
				archivalData.addAll(QSList);
				System.out.println("Fetched M_SRWA_12H archival data: " + QSList.size());
				break;
			case "M_IS":
				List<Object[]> MISList = BRRS_M_IS_reportservice.getM_ISArchival();
				archivalData.addAll(MISList);
				System.out.println("Fetched M_IS archival data: " + MISList.size());
				break;

			case "Q_BRANCHNET":
				List<Object[]> QBList = BRRS_Q_BRANCHNET_reportservice.getQ_BRANCHNETArchival();
				archivalData.addAll(QBList);
				System.out.println("Fetched M_SRWA_12H archival data: " + QBList.size());
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

			case "M_SRWA_12F":
				List<Object[]> srwafList = BRRS_M_SRWA_12F_reportservice.getM_SRWA_12FArchival();
				archivalData.addAll(srwafList);
				System.out.println("Fetched M_SRWA_12F archival data: " + srwafList.size());
				break;

			case "M_SECL":
				List<Object[]> seclList = brrs_m_secl_reportservice.getM_SECLArchival();
				archivalData.addAll(seclList);
				System.out.println("Fetched M_SECL archival data: " + seclList.size());
				break;

			case "M_OR2":
				List<Object[]> or2List = brrs_m_or2_reportservice.getM_OR2Archival();
				archivalData.addAll(or2List);
				System.out.println("Fetched M_OR2 archival data: " + or2List.size());
				break;

			case "M_SEC":
				List<Object[]> secList = brrs_m_sec_reportservice.getM_SECArchival();
				archivalData.addAll(secList);
				System.out.println("Fetched M_SECL archival data: " + secList.size());
				break;

			case "M_CA5":
				List<Object[]> ca5List = BRRS_M_CA5_reportservice.getM_CA5Archival();
				archivalData.addAll(ca5List);
				System.out.println("Fetched M_CA6 archival data: " + ca5List.size());
				break;

			case "M_CA7":
				List<Object[]> CA7List = BRRS_M_CA7_reportservice.getM_CA7Archival();
				archivalData.addAll(CA7List);
				System.out.println("Fetched M_CA6 archival data: " + CA7List.size());
				break;

			case "M_CA6":
				List<Object[]> CA6List = BRRS_M_CA6_reportservice.getM_CA6Archival();
				archivalData.addAll(CA6List);
				System.out.println("Fetched M_CA6 archival data: " + CA6List.size());
				break;

			case "Q_RLFA1":
				List<Object[]> qrlfa1List = brrs_q_rlfa1_reportservice.getQ_RLFA1Archival();
				archivalData.addAll(qrlfa1List);
				System.out.println("Fetched Q_RLFA1 archival data: " + qrlfa1List.size());
				break;

			case "M_BOP":
				List<Object[]> BOPList = BRRS_M_BOP_ReportService.getM_BOPArchival();
				archivalData.addAll(BOPList);
				System.out.println("Fetched M_BOP archival data: " + BOPList.size());
				break;

			case "Q_SMME_DEP":
				List<Object[]> QSMMEDEPList = BRRS_Q_SMME_DEP_ReportService.getQ_SMME_DEPArchival();
				archivalData.addAll(QSMMEDEPList);
				System.out.println("Fetched Q_SMME_DEP archival data: " + QSMMEDEPList.size());
				break;

			case "M_SRWA_12C":
				List<Object[]> MSRWA12CList = BRRS_M_SRWA_12C_reportservice.getM_SRWA_12CArchival();
				archivalData.addAll(MSRWA12CList);
				System.out.println("Fetched Q_SMME_DEP archival data: " + MSRWA12CList.size());
				break;

			case "M_SIR":
				List<Object[]> SIRList = BRRS_M_SIR_ReportService.getM_SIRArchival();
				archivalData.addAll(SIRList);
				System.out.println("Fetched M_SIR archival data: " + SIRList.size());
				break;

			// case "M_GP":
			// try {
			// archivalData = BRRS_M_GP_ReportService.getM_GPArchival();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;

			case "M_INT_RATES":
				List<Object[]> intratesList = brrs_m_int_rates_reportservice.getM_INTRATESArchival();
				archivalData.addAll(intratesList);
				System.out.println("Fetched M_INT_RATES archival data: " + intratesList.size());
				break;

			case "M_RPD":
				List<Object[]> RPDList = BRRS_M_RPD_ReportService.getM_RPDArchival();
				archivalData.addAll(RPDList);
				System.out.println("Fetched M_SIR archival data: " + RPDList.size());
				break;

			case "M_TBS":
				List<Object[]> TBSList = BRRS_M_TBS_ReportService.getM_TBSArchival();
				archivalData.addAll(TBSList);
				System.out.println("Fetched TBS archival data: " + TBSList.size());
				break;

			case "M_CA3":
				List<Object[]> ca3List = BRRS_M_CA3_reportservice.getM_CA3Archival();
				archivalData.addAll(ca3List);
				System.out.println("Fetched TBS archival data: " + ca3List.size());
				break;

			case "M_NOSVOS":
				try {
					archivalData = BRRS_M_NOSVOS_reportservice.getM_NOSVOSArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "M_CA1":
				try {
					archivalData = BRRS_M_CA1_reportservice.getM_CA1Archival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_PD":
				try {
					archivalData = BRRS_M_PD_ReportService.getM_PDArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "M_FAS":
				try {
					archivalData = BRRS_M_FAS_reportservice.getM_FASArchival();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "M_DEP4":
				List<Object[]> DEP4List = BRRS_M_DEP4_ReportService.getM_DEP4Archival();
				archivalData.addAll(DEP4List);
				System.out.println("Fetched DEP4 archival data: " + DEP4List.size());
				break;
			default:
				System.out.println("No archival logic defined for report: " + rptcode);
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
			// fileData = BRRS_M_SRWA_12H_reportservice.BRRS_M_SRWA_12HDetailExcel(filename,
			// fromdate, todate, currency,
			// dtltype, type, version);
		} else if (filename.equals("M_MRCDetail")) {
			fileData = BRRS_M_MRC_reportservice.getM_MRCDetailExcel(filename, fromdate, todate, currency, dtltype,
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
			fileData = BRRS_M_DEP3_reportservice.getM_DEP3DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_IRBDetail")) {
			fileData = brrs_m_irb_reportService.BRRS_M_IRBDetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_LIQDetail")) {
			fileData = BRRS_M_LIQ_reportservice.getM_LIQDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		} else if (filename.equals("M_LCRDetail")) {
			fileData = BRRS_M_LCR_reportservice.getM_LCRDetailExcel(filename, fromdate, todate, currency, dtltype, type,
					version);
		} else if (filename.equals("Q_SMME_LA")) {
			fileData = BRRS_Q_SMME_loans_Advances_reportService.BRRS_Q_SMMEDetailExcel(filename, fromdate, todate,
					currency, dtltype, type, version);
		} else if (filename.equals("M_CA2Detail")) {
			fileData = BRRS_M_CA2_reportservice.getM_CA2DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_CA2Detail")) {
			fileData = brrs_m_or1_reportservice.BRRS_M_OR1DetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_LIQGAPDetail")) {
			fileData = brrs_m_liqgap_reportservice.getM_LIQGAPDetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if (filename.equals("M_SPDetail")) {
			fileData = BRRS_M_SP_reportservice.getM_SPDetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if ("M_CALOC".equals(filename)) {

			fileData = BRRS_M_CALOC_reportService.getBRRSM_CALOCDetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);

		} else if (filename.equals("Q_SMMEDetail")) {
			fileData = BRRS_Q_SMME_Intrest_Income_ReportService.BRRS_Q_SMMEDetailExcel(filename, fromdate, todate,
					currency, dtltype, type, version);
		} else if ("M_FASDetail".equals(filename)) {
			fileData = BRRS_M_FAS_reportservice.BRRS_M_FASDetailExcel(filename, fromdate, todate, currency, dtltype,
					type, version);
		} else if ("M_SRWA_12A".equals(filename)) {

			fileData = brrs_m_srwa_12a_reportservice.getM_SRWA_12ADetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);

		}

		else if ("M_GMIRT_Detail".equals(filename)) {

			fileData = brrs_m_gmirt_reportservice.getM_GMIRTDetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);
		}

		else if ("M_GALOR_Detail".equals(filename)) {

			fileData = BRRS_m_galor_ReportService.getM_GALORDetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);
		} else if ("M_LIQ".equals(filename)) {

			fileData = BRRS_M_LIQ_reportservice.getM_LIQDetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);

		} else if (filename.equals("M_PDDetail")) {

			fileData = BRRS_M_PD_ReportService.BRRS_M_PDDetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);

		}

		else if ("Q_ATF_Detail".equals(filename)) {

			fileData = brrs_q_atf_reportservice.getQ_ATFDetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);
		}

		else if ("M_SCI_E".equals(filename)) {

			fileData = brrs_m_sci_e_reportservice.getM_SCI_EDetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);
		}
		
		
		else if ("M_I_S_CA".equals(filename)) {

			fileData = brrs_m_i_s_ca_reportservice.getM_I_S_CADetailExcel(filename, fromdate, todate, currency,
					dtltype, type, version);
		}
		
		
		
		
		

		if (fileData == null) {
			// logger.warn("Excel generation failed or no data for jobId: {}", jobId);
			// store error flag
			jobStorage.put(jobId, new byte[0]);

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

				case "M_LA1":
					modelAndView = BRRS_M_LA1_reportservice.getViewOrEditPage(
							request.getParameter("acctNo"), request.getParameter("formmode"));
					break;

				case "M_LA3":
					modelAndView = BRRS_M_LA3_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_LA4":
					modelAndView = BRRS_M_LA4_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_LA5":
					modelAndView = BRRS_M_LA5_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_CA2":
					modelAndView = BRRS_M_CA2_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_OR1":
					modelAndView = brrs_m_or1_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_LIQGAP":
					modelAndView = brrs_m_liqgap_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_SP":
					modelAndView = BRRS_M_SP_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_LIQ":
					modelAndView = BRRS_M_LIQ_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_LCR":
					modelAndView = BRRS_M_LCR_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_DEP1":
					modelAndView = BRRS_M_DEP1_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_DEP2":
					modelAndView = BRRS_M_DEP2_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_SRWA_12A":
					modelAndView = brrs_m_srwa_12a_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_DEP3":
					modelAndView = BRRS_M_DEP3_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_GALOR":
					modelAndView = BRRS_m_galor_ReportService.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;
					

					
					
				  case "M_SCI_E":
						modelAndView = brrs_m_sci_e_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
								request.getParameter("formmode"));
						break;	
						
				  case "Q_ATF":
						modelAndView = brrs_q_atf_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
								request.getParameter("formmode"));
						break;	
		
				case "M_FAS":
					modelAndView = BRRS_M_FAS_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "Q_SMME_LA":
					modelAndView = BRRS_Q_SMME_loans_Advances_reportService.getViewOrEditPage(
							request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;


					case "M_CALOC":
					modelAndView = BRRS_M_CALOC_reportService.getViewOrEditPage(
							request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "Q_SMME":
					modelAndView = BRRS_Q_SMME_Intrest_Income_ReportService.getViewOrEditPage(
							request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;

				case "M_PD":
					modelAndView = BRRS_M_PD_ReportService.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;
					
				case "M_SFINP1":
					modelAndView = BRRS_M_SFINP1_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;
	     
				case "M_PI":
					modelAndView = BRRS_M_PI_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
							request.getParameter("formmode"));
					break;
	
				case "M_I_S_CA":
					modelAndView = brrs_m_i_s_ca_reportservice.getViewOrEditPage(request.getParameter("acctNo"),
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

				case "M_LA1":
					response = BRRS_M_LA1_reportservice.updateDetailEdit(request);
					break;

				case "M_LA3":
					response = BRRS_M_LA3_reportservice.updateDetailEdit(request);
					break;

				case "M_LA4":
					response = BRRS_M_LA4_reportservice.updateDetailEdit(request);
					break;

				case "M_LA5":
					response = BRRS_M_LA5_reportservice.updateDetailEdit(request);
					break;

				case "M_CA2":
					response = BRRS_M_CA2_reportservice.updateDetailEdit(request);
					break;

				case "M_OR1":
					response = brrs_m_or1_reportservice.updateDetailEdit(request);
					break;

				case "M_LIQGAP":
					response = brrs_m_liqgap_reportservice.updateDetailEdit(request);
					break;

				case "M_SP":
					response = BRRS_M_SP_reportservice.updateDetailEdit(request);
					break;

				case "M_LIQ":
					response = BRRS_M_LIQ_reportservice.updateDetailEdit(request);
					break;

				case "M_LCR":
					response = BRRS_M_LCR_reportservice.updateDetailEdit(request);
					break;

				case "M_DEP1":
					response = BRRS_M_DEP1_reportservice.updateDetailEdit(request);
					break;

				case "M_DEP2":
					response = BRRS_M_DEP2_reportservice.updateDetailEdit(request);
					break;

				case "M_SRWA_12A":
					response = brrs_m_srwa_12a_reportservice.updateDetailEdit(request);
					break;

				case "M_DEP3":
					response = BRRS_M_DEP3_reportservice.updateDetailEdit(request);
					break;

				case "M_GALOR":
					response = BRRS_m_galor_ReportService.updateDetailEdit(request);
					break;



				case "M_SCI_E":
					response = brrs_m_sci_e_reportservice.updateDetailEdit(request);
					break;

				case "Q_ATF":
					response = brrs_q_atf_reportservice.updateDetailEdit(request);
					break;

					
				case "M_PD":
					response = BRRS_M_PD_ReportService.updateDetailEdit(request);
					break;
				case "M_PI":
					response = BRRS_M_PI_reportservice.updateDetailEdit(request);
					break;


					
				case "M_I_S_CA":
					response = brrs_m_i_s_ca_reportservice.updateDetailEdit(request);
					break;
					
					

				case "M_FAS":
					response = BRRS_M_FAS_reportservice.updateDetailEdit(request);
					break;
					
				case "M_SFINP1":
					response = BRRS_M_SFINP1_reportservice.updateDetailEdit(request);
					break;

				case "Q_SMME_LA":
					response = BRRS_Q_SMME_loans_Advances_reportService.updateDetailEdit(request);
					break;

					case "M_CALOC":
					response = BRRS_M_CALOC_reportService.updateDetailEdit(request);
					break;
					
				case "Q_SMME":
					response = BRRS_Q_SMME_Intrest_Income_ReportService.updateDetailEdit(request);
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

	// Resubmission Add Ur Case Here
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

			case "M_SRWA_12F":
				try {
					List<Object[]> resubList = BRRS_M_SRWA_12F_reportservice.getM_SRWA_12FResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_SRWA_12F: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SRWA_12F: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_SECL":
				try {
					List<Object[]> resubList = brrs_m_secl_reportservice.getM_SECLResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_SECL: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SECL: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_SEC":
				try {
					List<Object[]> resubList = brrs_m_sec_reportservice.getM_SECResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_SEC: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SEC: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_INT_RATES":
				try {
					List<Object[]> resubList = brrs_m_int_rates_reportservice.getM_INTRATESResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_INT_RATES: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_INT_RATES: " + e.getMessage());
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

			case "Q_BRANCHNET":
				try {
					List<Object[]> resubList = BRRS_Q_BRANCHNET_reportservice.getQ_BRANCHNETResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for Q_BRANCHNET: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for Q_BRANCHNET: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "M_IS":
				try {
					List<Object[]> resubList = BRRS_M_IS_reportservice.getM_ISResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_IS: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_IS: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "M_GP":
				try {
					List<Object[]> resubList = BRRS_M_GP_ReportService.getM_GPResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_GP: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_GP: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_LARADV":
				try {
					List<Object[]> resubList = brrs_m_laradv_reportservice.getM_LARADVResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_LARADV: " +
							resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_LARADV: " +
							e.getMessage());
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

			case "M_OB":
				try {
					List<Object[]> resubList = BRRS_M_OB_ReportService.getM_OBResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_OB: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_OB: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_OPTR":
				try {
					List<Object[]> resubList = BRRS_M_OPTR_ReportService.getM_OPTRResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_OPTR: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_OPTR: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_SRWA_12E":
				try {
					List<Object[]> resubList = BRRS_M_SRWA_12E_ReportService.getM_SRWA_12EResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_SRWA_12E: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SRWA_12E: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "M_INT_RATES_FCA":
				try {
					List<Object[]> resubList = brrs_m_int_rates_fca_reportservice.getM_INT_RATES_FCAResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_INT_RATES_FCA: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_INT_RATES_FCA: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_CA5":
				try {
					List<Object[]> resubList = BRRS_M_CA5_reportservice.getM_CA5Resub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_INT_RATES_FCA: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_INT_RATES_FCA: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_OR2":
				try {
					List<Object[]> resubList = brrs_m_or2_reportservice.getM_OR2Resub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_OR2: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_OR2: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_FXR":
				try {
					List<Object[]> resubList = BRRS_M_FXR_reportservice.getM_FXRResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_SRWA_12H: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SRWA_12H: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_CA7":
				try {
					List<Object[]> resubList = BRRS_M_CA7_reportservice.getM_CA7Resub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for CA7: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_CA7: " + e.getMessage());
					e.printStackTrace();
				}

				break;
			case "M_CA6":
				try {
					List<Object[]> resubList = BRRS_M_CA6_reportservice.getM_CA6Resub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for CA6: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_CA6: " + e.getMessage());
					e.printStackTrace();
				}

				break;

			case "Q_RLFA1":
				try {
					List<Object[]> resubList = brrs_q_rlfa1_reportservice.getQ_RLFA1Resub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for Q_RLFA1: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for Q_RLFA1: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_BOP":
				try {
					List<Object[]> resubList = BRRS_M_BOP_ReportService.getM_BOPResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_BOP: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_BOP: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "Q_SMME_DEP":
				try {
					List<Object[]> resubList = BRRS_Q_SMME_DEP_ReportService.getQ_SMME_DEPResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for Q_SMME_DEP: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for Q_SMME_DEP: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_SRWA_12C":
				try {
					List<Object[]> resubList = BRRS_M_SRWA_12C_reportservice.getM_SRWA_12CResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_SRWA_12C: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SRWA_12C: " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case "M_SIR":
				try {
					List<Object[]> resubList = BRRS_M_SIR_ReportService.getM_SIRResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for SIR: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SIR: " + e.getMessage());
					e.printStackTrace();
				}

				break;
			case "M_RPD":
				try {
					List<Object[]> resubList = BRRS_M_RPD_ReportService.getM_RPDResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for RPD: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_RPD: " + e.getMessage());
					e.printStackTrace();
				}

				break;

			case "M_TBS":
				try {
					List<Object[]> resubList = BRRS_M_TBS_ReportService.getM_TBSResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for TBS: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_TBS: " + e.getMessage());
					e.printStackTrace();
				}

				break;

			case "M_SRWA_12B":
				try {
					List<Object[]> resubList = brrs_m_srwa_12b_reportservice.getM_SRWA_12BResub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_SRWA_12B: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_SRWA_12B: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "M_CA3":
				try {
					List<Object[]> resubList = BRRS_M_CA3_reportservice.getM_CA3Resub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for M_CA3: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_CA3: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "M_DEP4":
				try {
					List<Object[]> resubList = BRRS_M_DEP4_ReportService.getM_DEP4Resub();
					resubmissionData.addAll(resubList);
					System.out.println("Resubmission data fetched for DEP4: " + resubList.size());
				} catch (Exception e) {
					System.err.println("Error fetching resubmission data for M_DEP4: " + e.getMessage());
					e.printStackTrace();
				}

				break;
			default:
				System.out.println("Unsupported report code: " + rptcode);
		}

		return resubmissionData;
	}

	public byte[] getConsolidatedDownloadFile(String filename, String asondate, String fromdate, String todate,
			String currency, String type, String version) throws ParseException {

		// List of all reports you want to include
		List<String> reportList = Arrays.asList("M_SFINP1", "M_SFINP2", "M_LA1", "M_LA2", "M_LA3", "M_LA4", "M_LA5",
				"M_CA1", "M_CA3", "M_CA4", "M_CA5", "M_CA7", "M_SP", "M_PI", "M_MRC", "M_IRB", "M_DEP1", "M_DEP2",
				"M_DEP3", "M_PLL", "M_UNCONS_INVEST", "M_SRWA_12F", "M_SRWA_12C", "M_SRWA_12H", "M_AIDP", "M_LIQ");
		System.out.println(todate);

		SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = inputFormat.parse(todate);

		// 2️⃣ Format to required pattern (Date → String)
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = outputFormat.format(date);
		// Workbook for consolidated report
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet errorSheet = workbook.createSheet("Status");
		Row headerRow = errorSheet.createRow(0);
		headerRow.createCell(0).setCellValue("Report Name");
		headerRow.createCell(1).setCellValue("Status");
		headerRow.createCell(2).setCellValue("Remarks");

		int errorRowNum = 1;

		for (String report : reportList) {
			try {
				byte[] fileData = generateReport(report, filename, asondate, fromdate, formattedDate, currency, type,
						version);

				if (fileData != null && fileData.length > 0) {
					try (Workbook reportWorkbook = new XSSFWorkbook(new java.io.ByteArrayInputStream(fileData))) {
						// Copy first sheet from each report into consolidated workbook
						for (int i = 0; i < reportWorkbook.getNumberOfSheets(); i++) {
							Sheet srcSheet = reportWorkbook.getSheetAt(i);
							Sheet newSheet = workbook.createSheet(report + "_" + srcSheet.getSheetName());
							copySheet(srcSheet, newSheet);
						}

						// Log success
						Row successRow = errorSheet.createRow(errorRowNum++);
						successRow.createCell(0).setCellValue(report);
						successRow.createCell(1).setCellValue("SUCCESS");
						successRow.createCell(2).setCellValue("Report added successfully");
					}
				} else {
					// Log missing data
					Row failRow = errorSheet.createRow(errorRowNum++);
					failRow.createCell(0).setCellValue(report);
					failRow.createCell(1).setCellValue("FAILED");
					failRow.createCell(2).setCellValue("No data found");
				}

			} catch (Exception e) {
				Row failRow = errorSheet.createRow(errorRowNum++);
				failRow.createCell(0).setCellValue(report);
				failRow.createCell(1).setCellValue("FAILED");
				failRow.createCell(2).setCellValue("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}

		// Finalize workbook
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			workbook.write(bos);
			workbook.close();
			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private byte[] generateReport(String reportName, String filename, String asondate, String fromdate, String todate,
			String currency, String type, String version) {

		try {
			// ✅ Convert date formats if needed (example: 30/09/2025 → 30-Sep-2025)
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

			if (fromdate != null && !fromdate.isEmpty()) {
				fromdate = outputFormat.format(inputFormat.parse(fromdate));
			}
			if (todate != null && !todate.isEmpty()) {
				todate = outputFormat.format(inputFormat.parse(todate));
			}
			if (asondate != null && !asondate.isEmpty()) {
				asondate = outputFormat.format(inputFormat.parse(asondate));
			}

			System.out.println("Service: Generating report for " + reportName);
			System.out.println("Converted Dates: From " + fromdate + " To " + todate + " Ason " + asondate);

			// ✅ Switch case for all reports
			switch (reportName) {
				case "M_SFINP2":
					return BRRS_M_SFINP2_reportservice.BRRS_M_SFINP2Excel(filename, reportName, fromdate, todate,
							currency,
							"DETAIL", type, version);

				case "M_SFINP1":
					return BRRS_M_SFINP1_reportservice.getM_SFINP1Excel(filename, reportName, fromdate, todate,
							currency,
							"DETAIL", type, version);

				case "M_LA1":
					return BRRS_M_LA1_reportservice.BRRS_M_LA1Excel(filename, reportName, fromdate, todate, currency,
							"DETAIL", type, version);

				case "M_LA2":
					return BRRS_M_LA2_reportservice.BRRS_M_LA2Excel(filename, reportName, fromdate, todate, currency,
							"DETAIL", type, version);

				case "M_LA3":
					return BRRS_M_LA3_reportservice.BRRS_M_LA3Excel(filename, reportName, fromdate, todate, currency,
							"DETAIL", type, version);

				case "M_LA4":
					return BRRS_M_LA4_reportservice.BRRS_M_LA4Excel(filename, reportName, fromdate, todate, currency,
							"DETAIL", type, version);

				case "M_CA4":
					return BRRS_M_CA4_reportservice.getBRRS_M_CA4Excel(filename, reportName, fromdate, todate, currency,
							"DETAIL", type, version);

				// 🟩 Add more report cases as needed...
				// case "M_SOMETHING":
				// return someService.someExcel(...);

				default:
					System.out.println("Service: Unknown report name: " + reportName);
					return null;
			}

		} catch (ParseException pe) {
			System.err.println("Date parse error: " + pe.getMessage());
			pe.printStackTrace();
			return null;

		} catch (FileNotFoundException fe) {
			System.err.println("Template file not found: " + fe.getMessage());
			fe.printStackTrace();
			return null;

		} catch (Exception e) {
			System.err.println("Error generating report: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private void copySheet(Sheet src, Sheet dest) {
		if (src == null || dest == null)
			return;

		int rowCount = 0;
		for (Row srcRow : src) {
			if (srcRow == null)
				continue;
			Row destRow = dest.createRow(rowCount++);
			short lastCell = srcRow.getLastCellNum();
			if (lastCell < 0)
				continue; // no cells in this row

			for (int i = 0; i < lastCell; i++) {
				Cell srcCell = srcRow.getCell(i);
				if (srcCell == null)
					continue;

				Cell destCell = destRow.createCell(i);

				// Copy cell type and value
				CellType cellType = srcCell.getCellType();
				if (cellType == null) {
					destCell.setCellValue(""); // fallback
					continue;
				}

				switch (cellType) {
					case STRING:
						destCell.setCellValue(srcCell.getStringCellValue());
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(srcCell)) {
							destCell.setCellValue(srcCell.getDateCellValue());
							// Optionally copy data format:
							CellStyle newStyle = dest.getWorkbook().createCellStyle();
							newStyle.cloneStyleFrom(srcCell.getCellStyle());
							destCell.setCellStyle(newStyle);
						} else {
							destCell.setCellValue(srcCell.getNumericCellValue());
						}
						break;
					case BOOLEAN:
						destCell.setCellValue(srcCell.getBooleanCellValue());
						break;
					case FORMULA:
						// copy formula (note: formula may refer to sheet names that change)
						destCell.setCellFormula(srcCell.getCellFormula());
						break;
					case BLANK:
						destCell.setBlank();
						break;
					case ERROR:
						destCell.setCellErrorValue(srcCell.getErrorCellValue());
						break;
					default:
						// fallback to string representation
						destCell.setCellValue(srcCell.toString());
				}

				// Optionally copy style (recommended if you want formatting preserved)
				CellStyle srcStyle = srcCell.getCellStyle();
				if (srcStyle != null) {
					try {
						CellStyle newStyle = dest.getWorkbook().createCellStyle();
						newStyle.cloneStyleFrom(srcStyle);
						destCell.setCellStyle(newStyle);
					} catch (Exception e) {
						// If style cloning fails, ignore and continue
					}
				}

				// Optionally copy comment
				if (srcCell.getCellComment() != null) {
					destCell.setCellComment(srcCell.getCellComment());
				}
			}
		}

		// Copy merged regions from src to dest
		for (int i = 0; i < src.getNumMergedRegions(); i++) {
			dest.addMergedRegion(src.getMergedRegion(i));
		}

		// Optionally copy column widths
		int maxCol = 0;
		for (Row row : src) {
			if (row != null && row.getLastCellNum() > maxCol) {
				maxCol = row.getLastCellNum();
			}
		}
		for (int c = 0; c < maxCol; c++) {
			dest.setColumnWidth(c, src.getColumnWidth(c));
		}
	}

	public byte[] generateConsolidatedExcel(String asondate, String fromdate, String todate,
			String currency, String type, String version) {
		logger.info("Starting consolidated Excel generation for all 70 reports.");

		// Create final workbook
		try (Workbook consolidatedWorkbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			// List of all report names (update with all 70)
			List<String> reportNames = Arrays.asList(
					"M_LA1", "M_LA2", "M_LA3", "M_LA4",
					"M_CA4", "M_PLL", "M_DEP3", "M_AIDP", "M_IRB", "M_LIQ"
			// ... add all remaining report codes
			);

			for (String reportName : reportNames) {
				logger.info("Processing report: {}", reportName);

				// Generate individual report byte[]
				byte[] reportBytes = generateReport(reportName, reportName, asondate, fromdate, todate, currency, type,
						version);

				if (reportBytes == null || reportBytes.length == 0) {
					logger.warn("Skipping {} — no data found or generation failed.", reportName);
					continue;
				}

				// Convert the byte[] to workbook
				try (InputStream in = new ByteArrayInputStream(reportBytes);
						Workbook tempWorkbook = WorkbookFactory.create(in)) {

					// Get first sheet from this workbook
					Sheet srcSheet = tempWorkbook.getSheetAt(0);

					// Create new sheet in the main workbook
					Sheet destSheet = consolidatedWorkbook.createSheet(reportName);

					// Copy content
					copySheetContent(srcSheet, destSheet);
				} catch (Exception e) {
					logger.error("Error processing report: {}", reportName, e);
				}
			}

			// Write the final consolidated workbook
			consolidatedWorkbook.write(out);
			logger.info("All reports successfully consolidated into one Excel file.");

			return out.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating consolidated Excel.", e);
			return null;
		}
	}

	private void copySheetContent(Sheet src, Sheet dest) {
		int rowCount = 0;

		for (Row srcRow : src) {
			Row destRow = dest.createRow(rowCount++);

			if (srcRow == null)
				continue;

			for (int i = 0; i < srcRow.getLastCellNum(); i++) {
				Cell srcCell = srcRow.getCell(i);
				if (srcCell == null)
					continue;

				Cell destCell = destRow.createCell(i);

				// Copy style if needed
				if (srcCell.getCellStyle() != null) {
					destCell.setCellStyle(srcCell.getCellStyle());
				}

				// ✅ Safe switch for Apache POI 4.x / 5.x
				switch (srcCell.getCellType()) {
					case STRING:
						destCell.setCellValue(srcCell.getRichStringCellValue().getString());
						break;

					case NUMERIC:
						if (DateUtil.isCellDateFormatted(srcCell)) {
							destCell.setCellValue(srcCell.getDateCellValue());
						} else {
							destCell.setCellValue(srcCell.getNumericCellValue());
						}
						break;

					case BOOLEAN:
						destCell.setCellValue(srcCell.getBooleanCellValue());
						break;

					case FORMULA:
						// Copy the formula text directly
						destCell.setCellFormula(srcCell.getCellFormula());
						break;

					case BLANK:
						destCell.setBlank();
						break;

					default:
						destCell.setCellValue("");
						break;
				}
			}
		}
	}

	// download pdf ->
	public byte[] getPdfDownloadFile(String reportId, String filename, String asondate, String fromdate, String todate,
			String currency, String subreportid, String secid, String dtltype, String reportingTime,
			String instancecode, String filter, String type, String version) {

		byte[] pdffile = null;

		switch (reportId) {

			case "M_AIDP":
				try {
					byte[] excelBytes = BRRS_M_AIDP_reportservice.getM_AIDPExcel(filename, reportId, fromdate,
							todate, currency,
							dtltype, type, version);

					pdffile = BRRS_M_AIDP_reportservice.convertExcelBytesToPdf(excelBytes);

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "M_SFINP1":
				try {
					byte[] excelBytes = BRRS_M_SFINP1_reportservice.getM_SFINP1Excel(filename, reportId, fromdate,
							todate, currency,
							dtltype, type, version);

					pdffile = BRRS_M_SFINP1_reportservice.convertExcelBytesToPdf(excelBytes);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
		}
		return pdffile;
	}

}
