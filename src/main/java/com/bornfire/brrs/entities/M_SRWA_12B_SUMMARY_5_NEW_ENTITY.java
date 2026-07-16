package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BRRS_M_SRWA_12B_SUMMARY_TABLE_5_NEW")
public class M_SRWA_12B_SUMMARY_5_NEW_ENTITY {
	
	@Id
	private Date REPORT_DATE;
	private String REPORT_VERSION;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	private BigDecimal R123_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R123_CCF_PCT;
	private BigDecimal R123_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R123_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R123_CEA_AFTER_NETTING;
	private BigDecimal R123_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R123_GUARANTEE_ELIGIBLE;
	private BigDecimal R123_CREDIT_DERIVATIVES;
	private BigDecimal R123_EXPOSURE_COVERED_CRM;
	private BigDecimal R123_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R123_RWA_TOTAL;
	private BigDecimal R123_CRM_RISK_WEIGHT;
	private BigDecimal R123_RWA_CRM_COVERED;
	private BigDecimal R123_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R123_RWA_NOT_COVERED;
	private BigDecimal R123_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R123_CEA_AFTER_VOL_ADJ;
	private BigDecimal R123_COLL_CASH;
	private BigDecimal R123_COLL_TBILLS;
	private BigDecimal R123_COLL_DEBT_SEC;
	private BigDecimal R123_COLL_EQUITIES;
	private BigDecimal R123_COLL_MUTUAL_FUNDS;
	private BigDecimal R123_COLL_TOTAL;
	private BigDecimal R123_CEA_AFTER_CRM;
	private BigDecimal R123_RWA_CEA_NOT_COVERED;
	private BigDecimal R123_UNSECURED_CEA;
	private BigDecimal R123_RWA_UNSECURED_CEA;
	private BigDecimal R124_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R124_CCF_PCT;
	private BigDecimal R124_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R124_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R124_CEA_AFTER_NETTING;
	private BigDecimal R124_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R124_GUARANTEE_ELIGIBLE;
	private BigDecimal R124_CREDIT_DERIVATIVES;
	private BigDecimal R124_EXPOSURE_COVERED_CRM;
	private BigDecimal R124_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R124_RWA_TOTAL;
	private BigDecimal R124_CRM_RISK_WEIGHT;
	private BigDecimal R124_RWA_CRM_COVERED;
	private BigDecimal R124_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R124_RWA_NOT_COVERED;
	private BigDecimal R124_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R124_CEA_AFTER_VOL_ADJ;
	private BigDecimal R124_COLL_CASH;
	private BigDecimal R124_COLL_TBILLS;
	private BigDecimal R124_COLL_DEBT_SEC;
	private BigDecimal R124_COLL_EQUITIES;
	private BigDecimal R124_COLL_MUTUAL_FUNDS;
	private BigDecimal R124_COLL_TOTAL;
	private BigDecimal R124_CEA_AFTER_CRM;
	private BigDecimal R124_RWA_CEA_NOT_COVERED;
	private BigDecimal R124_UNSECURED_CEA;
	private BigDecimal R124_RWA_UNSECURED_CEA;
	private BigDecimal R125_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R125_CCF_PCT;
	private BigDecimal R125_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R125_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R125_CEA_AFTER_NETTING;
	private BigDecimal R125_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R125_GUARANTEE_ELIGIBLE;
	private BigDecimal R125_CREDIT_DERIVATIVES;
	private BigDecimal R125_EXPOSURE_COVERED_CRM;
	private BigDecimal R125_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R125_RWA_TOTAL;
	private BigDecimal R125_CRM_RISK_WEIGHT;
	private BigDecimal R125_RWA_CRM_COVERED;
	private BigDecimal R125_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R125_RWA_NOT_COVERED;
	private BigDecimal R125_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R125_CEA_AFTER_VOL_ADJ;
	private BigDecimal R125_COLL_CASH;
	private BigDecimal R125_COLL_TBILLS;
	private BigDecimal R125_COLL_DEBT_SEC;
	private BigDecimal R125_COLL_EQUITIES;
	private BigDecimal R125_COLL_MUTUAL_FUNDS;
	private BigDecimal R125_COLL_TOTAL;
	private BigDecimal R125_CEA_AFTER_CRM;
	private BigDecimal R125_RWA_CEA_NOT_COVERED;
	private BigDecimal R125_UNSECURED_CEA;
	private BigDecimal R125_RWA_UNSECURED_CEA;
	private BigDecimal R126_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R126_CCF_PCT;
	private BigDecimal R126_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R126_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R126_CEA_AFTER_NETTING;
	private BigDecimal R126_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R126_GUARANTEE_ELIGIBLE;
	private BigDecimal R126_CREDIT_DERIVATIVES;
	private BigDecimal R126_EXPOSURE_COVERED_CRM;
	private BigDecimal R126_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R126_RWA_TOTAL;
	private BigDecimal R126_CRM_RISK_WEIGHT;
	private BigDecimal R126_RWA_CRM_COVERED;
	private BigDecimal R126_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R126_RWA_NOT_COVERED;
	private BigDecimal R126_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R126_CEA_AFTER_VOL_ADJ;
	private BigDecimal R126_COLL_CASH;
	private BigDecimal R126_COLL_TBILLS;
	private BigDecimal R126_COLL_DEBT_SEC;
	private BigDecimal R126_COLL_EQUITIES;
	private BigDecimal R126_COLL_MUTUAL_FUNDS;
	private BigDecimal R126_COLL_TOTAL;
	private BigDecimal R126_CEA_AFTER_CRM;
	private BigDecimal R126_RWA_CEA_NOT_COVERED;
	private BigDecimal R126_UNSECURED_CEA;
	private BigDecimal R126_RWA_UNSECURED_CEA;
	private BigDecimal R127_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R127_CCF_PCT;
	private BigDecimal R127_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R127_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R127_CEA_AFTER_NETTING;
	private BigDecimal R127_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R127_GUARANTEE_ELIGIBLE;
	private BigDecimal R127_CREDIT_DERIVATIVES;
	private BigDecimal R127_EXPOSURE_COVERED_CRM;
	private BigDecimal R127_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R127_RWA_TOTAL;
	private BigDecimal R127_CRM_RISK_WEIGHT;
	private BigDecimal R127_RWA_CRM_COVERED;
	private BigDecimal R127_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R127_RWA_NOT_COVERED;
	private BigDecimal R127_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R127_CEA_AFTER_VOL_ADJ;
	private BigDecimal R127_COLL_CASH;
	private BigDecimal R127_COLL_TBILLS;
	private BigDecimal R127_COLL_DEBT_SEC;
	private BigDecimal R127_COLL_EQUITIES;
	private BigDecimal R127_COLL_MUTUAL_FUNDS;
	private BigDecimal R127_COLL_TOTAL;
	private BigDecimal R127_CEA_AFTER_CRM;
	private BigDecimal R127_RWA_CEA_NOT_COVERED;
	private BigDecimal R127_UNSECURED_CEA;
	private BigDecimal R127_RWA_UNSECURED_CEA;
	private BigDecimal R128_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R128_CCF_PCT;
	private BigDecimal R128_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R128_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R128_CEA_AFTER_NETTING;
	private BigDecimal R128_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R128_GUARANTEE_ELIGIBLE;
	private BigDecimal R128_CREDIT_DERIVATIVES;
	private BigDecimal R128_EXPOSURE_COVERED_CRM;
	private BigDecimal R128_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R128_RWA_TOTAL;
	private BigDecimal R128_CRM_RISK_WEIGHT;
	private BigDecimal R128_RWA_CRM_COVERED;
	private BigDecimal R128_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R128_RWA_NOT_COVERED;
	private BigDecimal R128_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R128_CEA_AFTER_VOL_ADJ;
	private BigDecimal R128_COLL_CASH;
	private BigDecimal R128_COLL_TBILLS;
	private BigDecimal R128_COLL_DEBT_SEC;
	private BigDecimal R128_COLL_EQUITIES;
	private BigDecimal R128_COLL_MUTUAL_FUNDS;
	private BigDecimal R128_COLL_TOTAL;
	private BigDecimal R128_CEA_AFTER_CRM;
	private BigDecimal R128_RWA_CEA_NOT_COVERED;
	private BigDecimal R128_UNSECURED_CEA;
	private BigDecimal R128_RWA_UNSECURED_CEA;
	private BigDecimal R129_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R129_CCF_PCT;
	private BigDecimal R129_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R129_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R129_CEA_AFTER_NETTING;
	private BigDecimal R129_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R129_GUARANTEE_ELIGIBLE;
	private BigDecimal R129_CREDIT_DERIVATIVES;
	private BigDecimal R129_EXPOSURE_COVERED_CRM;
	private BigDecimal R129_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R129_RWA_TOTAL;
	private BigDecimal R129_CRM_RISK_WEIGHT;
	private BigDecimal R129_RWA_CRM_COVERED;
	private BigDecimal R129_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R129_RWA_NOT_COVERED;
	private BigDecimal R129_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R129_CEA_AFTER_VOL_ADJ;
	private BigDecimal R129_COLL_CASH;
	private BigDecimal R129_COLL_TBILLS;
	private BigDecimal R129_COLL_DEBT_SEC;
	private BigDecimal R129_COLL_EQUITIES;
	private BigDecimal R129_COLL_MUTUAL_FUNDS;
	private BigDecimal R129_COLL_TOTAL;
	private BigDecimal R129_CEA_AFTER_CRM;
	private BigDecimal R129_RWA_CEA_NOT_COVERED;
	private BigDecimal R129_UNSECURED_CEA;
	private BigDecimal R129_RWA_UNSECURED_CEA;
	private BigDecimal R130_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R130_CCF_PCT;
	private BigDecimal R130_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R130_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R130_CEA_AFTER_NETTING;
	private BigDecimal R130_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R130_GUARANTEE_ELIGIBLE;
	private BigDecimal R130_CREDIT_DERIVATIVES;
	private BigDecimal R130_EXPOSURE_COVERED_CRM;
	private BigDecimal R130_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R130_RWA_TOTAL;
	private BigDecimal R130_CRM_RISK_WEIGHT;
	private BigDecimal R130_RWA_CRM_COVERED;
	private BigDecimal R130_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R130_RWA_NOT_COVERED;
	private BigDecimal R130_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R130_CEA_AFTER_VOL_ADJ;
	private BigDecimal R130_COLL_CASH;
	private BigDecimal R130_COLL_TBILLS;
	private BigDecimal R130_COLL_DEBT_SEC;
	private BigDecimal R130_COLL_EQUITIES;
	private BigDecimal R130_COLL_MUTUAL_FUNDS;
	private BigDecimal R130_COLL_TOTAL;
	private BigDecimal R130_CEA_AFTER_CRM;
	private BigDecimal R130_RWA_CEA_NOT_COVERED;
	private BigDecimal R130_UNSECURED_CEA;
	private BigDecimal R130_RWA_UNSECURED_CEA;
	private BigDecimal R131_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R131_CCF_PCT;
	private BigDecimal R131_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R131_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R131_CEA_AFTER_NETTING;
	private BigDecimal R131_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R131_GUARANTEE_ELIGIBLE;
	private BigDecimal R131_CREDIT_DERIVATIVES;
	private BigDecimal R131_EXPOSURE_COVERED_CRM;
	private BigDecimal R131_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R131_RWA_TOTAL;
	private BigDecimal R131_CRM_RISK_WEIGHT;
	private BigDecimal R131_RWA_CRM_COVERED;
	private BigDecimal R131_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R131_RWA_NOT_COVERED;
	private BigDecimal R131_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R131_CEA_AFTER_VOL_ADJ;
	private BigDecimal R131_COLL_CASH;
	private BigDecimal R131_COLL_TBILLS;
	private BigDecimal R131_COLL_DEBT_SEC;
	private BigDecimal R131_COLL_EQUITIES;
	private BigDecimal R131_COLL_MUTUAL_FUNDS;
	private BigDecimal R131_COLL_TOTAL;
	private BigDecimal R131_CEA_AFTER_CRM;
	private BigDecimal R131_RWA_CEA_NOT_COVERED;
	private BigDecimal R131_UNSECURED_CEA;
	private BigDecimal R131_RWA_UNSECURED_CEA;
	private BigDecimal R132_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R132_CCF_PCT;
	private BigDecimal R132_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R132_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R132_CEA_AFTER_NETTING;
	private BigDecimal R132_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R132_GUARANTEE_ELIGIBLE;
	private BigDecimal R132_CREDIT_DERIVATIVES;
	private BigDecimal R132_EXPOSURE_COVERED_CRM;
	private BigDecimal R132_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R132_RWA_TOTAL;
	private BigDecimal R132_CRM_RISK_WEIGHT;
	private BigDecimal R132_RWA_CRM_COVERED;
	private BigDecimal R132_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R132_RWA_NOT_COVERED;
	private BigDecimal R132_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R132_CEA_AFTER_VOL_ADJ;
	private BigDecimal R132_COLL_CASH;
	private BigDecimal R132_COLL_TBILLS;
	private BigDecimal R132_COLL_DEBT_SEC;
	private BigDecimal R132_COLL_EQUITIES;
	private BigDecimal R132_COLL_MUTUAL_FUNDS;
	private BigDecimal R132_COLL_TOTAL;
	private BigDecimal R132_CEA_AFTER_CRM;
	private BigDecimal R132_RWA_CEA_NOT_COVERED;
	private BigDecimal R132_UNSECURED_CEA;
	private BigDecimal R132_RWA_UNSECURED_CEA;
	private BigDecimal R133_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R133_CCF_PCT;
	private BigDecimal R133_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R133_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R133_CEA_AFTER_NETTING;
	private BigDecimal R133_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R133_GUARANTEE_ELIGIBLE;
	private BigDecimal R133_CREDIT_DERIVATIVES;
	private BigDecimal R133_EXPOSURE_COVERED_CRM;
	private BigDecimal R133_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R133_RWA_TOTAL;
	private BigDecimal R133_CRM_RISK_WEIGHT;
	private BigDecimal R133_RWA_CRM_COVERED;
	private BigDecimal R133_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R133_RWA_NOT_COVERED;
	private BigDecimal R133_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R133_CEA_AFTER_VOL_ADJ;
	private BigDecimal R133_COLL_CASH;
	private BigDecimal R133_COLL_TBILLS;
	private BigDecimal R133_COLL_DEBT_SEC;
	private BigDecimal R133_COLL_EQUITIES;
	private BigDecimal R133_COLL_MUTUAL_FUNDS;
	private BigDecimal R133_COLL_TOTAL;
	private BigDecimal R133_CEA_AFTER_CRM;
	private BigDecimal R133_RWA_CEA_NOT_COVERED;
	private BigDecimal R133_UNSECURED_CEA;
	private BigDecimal R133_RWA_UNSECURED_CEA;
	private BigDecimal R134_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R134_CCF_PCT;
	private BigDecimal R134_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R134_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R134_CEA_AFTER_NETTING;
	private BigDecimal R134_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R134_GUARANTEE_ELIGIBLE;
	private BigDecimal R134_CREDIT_DERIVATIVES;
	private BigDecimal R134_EXPOSURE_COVERED_CRM;
	private BigDecimal R134_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R134_RWA_TOTAL;
	private BigDecimal R134_CRM_RISK_WEIGHT;
	private BigDecimal R134_RWA_CRM_COVERED;
	private BigDecimal R134_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R134_RWA_NOT_COVERED;
	private BigDecimal R134_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R134_CEA_AFTER_VOL_ADJ;
	private BigDecimal R134_COLL_CASH;
	private BigDecimal R134_COLL_TBILLS;
	private BigDecimal R134_COLL_DEBT_SEC;
	private BigDecimal R134_COLL_EQUITIES;
	private BigDecimal R134_COLL_MUTUAL_FUNDS;
	private BigDecimal R134_COLL_TOTAL;
	private BigDecimal R134_CEA_AFTER_CRM;
	private BigDecimal R134_RWA_CEA_NOT_COVERED;
	private BigDecimal R134_UNSECURED_CEA;
	private BigDecimal R134_RWA_UNSECURED_CEA;
	private BigDecimal R135_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R135_CCF_PCT;
	private BigDecimal R135_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R135_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R135_CEA_AFTER_NETTING;
	private BigDecimal R135_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R135_GUARANTEE_ELIGIBLE;
	private BigDecimal R135_CREDIT_DERIVATIVES;
	private BigDecimal R135_EXPOSURE_COVERED_CRM;
	private BigDecimal R135_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R135_RWA_TOTAL;
	private BigDecimal R135_CRM_RISK_WEIGHT;
	private BigDecimal R135_RWA_CRM_COVERED;
	private BigDecimal R135_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R135_RWA_NOT_COVERED;
	private BigDecimal R135_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R135_CEA_AFTER_VOL_ADJ;
	private BigDecimal R135_COLL_CASH;
	private BigDecimal R135_COLL_TBILLS;
	private BigDecimal R135_COLL_DEBT_SEC;
	private BigDecimal R135_COLL_EQUITIES;
	private BigDecimal R135_COLL_MUTUAL_FUNDS;
	private BigDecimal R135_COLL_TOTAL;
	private BigDecimal R135_CEA_AFTER_CRM;
	private BigDecimal R135_RWA_CEA_NOT_COVERED;
	private BigDecimal R135_UNSECURED_CEA;
	private BigDecimal R135_RWA_UNSECURED_CEA;
	private BigDecimal R136_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R136_CCF_PCT;
	private BigDecimal R136_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R136_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R136_CEA_AFTER_NETTING;
	private BigDecimal R136_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R136_GUARANTEE_ELIGIBLE;
	private BigDecimal R136_CREDIT_DERIVATIVES;
	private BigDecimal R136_EXPOSURE_COVERED_CRM;
	private BigDecimal R136_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R136_RWA_TOTAL;
	private BigDecimal R136_CRM_RISK_WEIGHT;
	private BigDecimal R136_RWA_CRM_COVERED;
	private BigDecimal R136_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R136_RWA_NOT_COVERED;
	private BigDecimal R136_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R136_CEA_AFTER_VOL_ADJ;
	private BigDecimal R136_COLL_CASH;
	private BigDecimal R136_COLL_TBILLS;
	private BigDecimal R136_COLL_DEBT_SEC;
	private BigDecimal R136_COLL_EQUITIES;
	private BigDecimal R136_COLL_MUTUAL_FUNDS;
	private BigDecimal R136_COLL_TOTAL;
	private BigDecimal R136_CEA_AFTER_CRM;
	private BigDecimal R136_RWA_CEA_NOT_COVERED;
	private BigDecimal R136_UNSECURED_CEA;
	private BigDecimal R136_RWA_UNSECURED_CEA;
	private BigDecimal R137_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R137_CCF_PCT;
	private BigDecimal R137_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R137_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R137_CEA_AFTER_NETTING;
	private BigDecimal R137_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R137_GUARANTEE_ELIGIBLE;
	private BigDecimal R137_CREDIT_DERIVATIVES;
	private BigDecimal R137_EXPOSURE_COVERED_CRM;
	private BigDecimal R137_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R137_RWA_TOTAL;
	private BigDecimal R137_CRM_RISK_WEIGHT;
	private BigDecimal R137_RWA_CRM_COVERED;
	private BigDecimal R137_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R137_RWA_NOT_COVERED;
	private BigDecimal R137_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R137_CEA_AFTER_VOL_ADJ;
	private BigDecimal R137_COLL_CASH;
	private BigDecimal R137_COLL_TBILLS;
	private BigDecimal R137_COLL_DEBT_SEC;
	private BigDecimal R137_COLL_EQUITIES;
	private BigDecimal R137_COLL_MUTUAL_FUNDS;
	private BigDecimal R137_COLL_TOTAL;
	private BigDecimal R137_CEA_AFTER_CRM;
	private BigDecimal R137_RWA_CEA_NOT_COVERED;
	private BigDecimal R137_UNSECURED_CEA;
	private BigDecimal R137_RWA_UNSECURED_CEA;
	private BigDecimal R138_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R138_CCF_PCT;
	private BigDecimal R138_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R138_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R138_CEA_AFTER_NETTING;
	private BigDecimal R138_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R138_GUARANTEE_ELIGIBLE;
	private BigDecimal R138_CREDIT_DERIVATIVES;
	private BigDecimal R138_EXPOSURE_COVERED_CRM;
	private BigDecimal R138_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R138_RWA_TOTAL;
	private BigDecimal R138_CRM_RISK_WEIGHT;
	private BigDecimal R138_RWA_CRM_COVERED;
	private BigDecimal R138_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R138_RWA_NOT_COVERED;
	private BigDecimal R138_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R138_CEA_AFTER_VOL_ADJ;
	private BigDecimal R138_COLL_CASH;
	private BigDecimal R138_COLL_TBILLS;
	private BigDecimal R138_COLL_DEBT_SEC;
	private BigDecimal R138_COLL_EQUITIES;
	private BigDecimal R138_COLL_MUTUAL_FUNDS;
	private BigDecimal R138_COLL_TOTAL;
	private BigDecimal R138_CEA_AFTER_CRM;
	private BigDecimal R138_RWA_CEA_NOT_COVERED;
	private BigDecimal R138_UNSECURED_CEA;
	private BigDecimal R138_RWA_UNSECURED_CEA;
	private BigDecimal R139_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R139_CCF_PCT;
	private BigDecimal R139_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R139_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R139_CEA_AFTER_NETTING;
	private BigDecimal R139_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R139_GUARANTEE_ELIGIBLE;
	private BigDecimal R139_CREDIT_DERIVATIVES;
	private BigDecimal R139_EXPOSURE_COVERED_CRM;
	private BigDecimal R139_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R139_RWA_TOTAL;
	private BigDecimal R139_CRM_RISK_WEIGHT;
	private BigDecimal R139_RWA_CRM_COVERED;
	private BigDecimal R139_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R139_RWA_NOT_COVERED;
	private BigDecimal R139_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R139_CEA_AFTER_VOL_ADJ;
	private BigDecimal R139_COLL_CASH;
	private BigDecimal R139_COLL_TBILLS;
	private BigDecimal R139_COLL_DEBT_SEC;
	private BigDecimal R139_COLL_EQUITIES;
	private BigDecimal R139_COLL_MUTUAL_FUNDS;
	private BigDecimal R139_COLL_TOTAL;
	private BigDecimal R139_CEA_AFTER_CRM;
	private BigDecimal R139_RWA_CEA_NOT_COVERED;
	private BigDecimal R139_UNSECURED_CEA;
	private BigDecimal R139_RWA_UNSECURED_CEA;
	private BigDecimal R140_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R140_CCF_PCT;
	private BigDecimal R140_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R140_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R140_CEA_AFTER_NETTING;
	private BigDecimal R140_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R140_GUARANTEE_ELIGIBLE;
	private BigDecimal R140_CREDIT_DERIVATIVES;
	private BigDecimal R140_EXPOSURE_COVERED_CRM;
	private BigDecimal R140_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R140_RWA_TOTAL;
	private BigDecimal R140_CRM_RISK_WEIGHT;
	private BigDecimal R140_RWA_CRM_COVERED;
	private BigDecimal R140_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R140_RWA_NOT_COVERED;
	private BigDecimal R140_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R140_CEA_AFTER_VOL_ADJ;
	private BigDecimal R140_COLL_CASH;
	private BigDecimal R140_COLL_TBILLS;
	private BigDecimal R140_COLL_DEBT_SEC;
	private BigDecimal R140_COLL_EQUITIES;
	private BigDecimal R140_COLL_MUTUAL_FUNDS;
	private BigDecimal R140_COLL_TOTAL;
	private BigDecimal R140_CEA_AFTER_CRM;
	private BigDecimal R140_RWA_CEA_NOT_COVERED;
	private BigDecimal R140_UNSECURED_CEA;
	private BigDecimal R140_RWA_UNSECURED_CEA;
	private BigDecimal R141_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R141_CCF_PCT;
	private BigDecimal R141_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R141_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R141_CEA_AFTER_NETTING;
	private BigDecimal R141_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R141_GUARANTEE_ELIGIBLE;
	private BigDecimal R141_CREDIT_DERIVATIVES;
	private BigDecimal R141_EXPOSURE_COVERED_CRM;
	private BigDecimal R141_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R141_RWA_TOTAL;
	private BigDecimal R141_CRM_RISK_WEIGHT;
	private BigDecimal R141_RWA_CRM_COVERED;
	private BigDecimal R141_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R141_RWA_NOT_COVERED;
	private BigDecimal R141_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R141_CEA_AFTER_VOL_ADJ;
	private BigDecimal R141_COLL_CASH;
	private BigDecimal R141_COLL_TBILLS;
	private BigDecimal R141_COLL_DEBT_SEC;
	private BigDecimal R141_COLL_EQUITIES;
	private BigDecimal R141_COLL_MUTUAL_FUNDS;
	private BigDecimal R141_COLL_TOTAL;
	private BigDecimal R141_CEA_AFTER_CRM;
	private BigDecimal R141_RWA_CEA_NOT_COVERED;
	private BigDecimal R141_UNSECURED_CEA;
	private BigDecimal R141_RWA_UNSECURED_CEA;
	private BigDecimal R142_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R142_CCF_PCT;
	private BigDecimal R142_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R142_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R142_CEA_AFTER_NETTING;
	private BigDecimal R142_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R142_GUARANTEE_ELIGIBLE;
	private BigDecimal R142_CREDIT_DERIVATIVES;
	private BigDecimal R142_EXPOSURE_COVERED_CRM;
	private BigDecimal R142_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R142_RWA_TOTAL;
	private BigDecimal R142_CRM_RISK_WEIGHT;
	private BigDecimal R142_RWA_CRM_COVERED;
	private BigDecimal R142_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R142_RWA_NOT_COVERED;
	private BigDecimal R142_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R142_CEA_AFTER_VOL_ADJ;
	private BigDecimal R142_COLL_CASH;
	private BigDecimal R142_COLL_TBILLS;
	private BigDecimal R142_COLL_DEBT_SEC;
	private BigDecimal R142_COLL_EQUITIES;
	private BigDecimal R142_COLL_MUTUAL_FUNDS;
	private BigDecimal R142_COLL_TOTAL;
	private BigDecimal R142_CEA_AFTER_CRM;
	private BigDecimal R142_RWA_CEA_NOT_COVERED;
	private BigDecimal R142_UNSECURED_CEA;
	private BigDecimal R142_RWA_UNSECURED_CEA;
	private BigDecimal R143_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R143_CCF_PCT;
	private BigDecimal R143_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R143_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R143_CEA_AFTER_NETTING;
	private BigDecimal R143_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R143_GUARANTEE_ELIGIBLE;
	private BigDecimal R143_CREDIT_DERIVATIVES;
	private BigDecimal R143_EXPOSURE_COVERED_CRM;
	private BigDecimal R143_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R143_RWA_TOTAL;
	private BigDecimal R143_CRM_RISK_WEIGHT;
	private BigDecimal R143_RWA_CRM_COVERED;
	private BigDecimal R143_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R143_RWA_NOT_COVERED;
	private BigDecimal R143_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R143_CEA_AFTER_VOL_ADJ;
	private BigDecimal R143_COLL_CASH;
	private BigDecimal R143_COLL_TBILLS;
	private BigDecimal R143_COLL_DEBT_SEC;
	private BigDecimal R143_COLL_EQUITIES;
	private BigDecimal R143_COLL_MUTUAL_FUNDS;
	private BigDecimal R143_COLL_TOTAL;
	private BigDecimal R143_CEA_AFTER_CRM;
	private BigDecimal R143_RWA_CEA_NOT_COVERED;
	private BigDecimal R143_UNSECURED_CEA;
	private BigDecimal R143_RWA_UNSECURED_CEA;
	private BigDecimal R144_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R144_CCF_PCT;
	private BigDecimal R144_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R144_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R144_CEA_AFTER_NETTING;
	private BigDecimal R144_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R144_GUARANTEE_ELIGIBLE;
	private BigDecimal R144_CREDIT_DERIVATIVES;
	private BigDecimal R144_EXPOSURE_COVERED_CRM;
	private BigDecimal R144_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R144_RWA_TOTAL;
	private BigDecimal R144_CRM_RISK_WEIGHT;
	private BigDecimal R144_RWA_CRM_COVERED;
	private BigDecimal R144_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R144_RWA_NOT_COVERED;
	private BigDecimal R144_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R144_CEA_AFTER_VOL_ADJ;
	private BigDecimal R144_COLL_CASH;
	private BigDecimal R144_COLL_TBILLS;
	private BigDecimal R144_COLL_DEBT_SEC;
	private BigDecimal R144_COLL_EQUITIES;
	private BigDecimal R144_COLL_MUTUAL_FUNDS;
	private BigDecimal R144_COLL_TOTAL;
	private BigDecimal R144_CEA_AFTER_CRM;
	private BigDecimal R144_RWA_CEA_NOT_COVERED;
	private BigDecimal R144_UNSECURED_CEA;
	private BigDecimal R144_RWA_UNSECURED_CEA;
	private BigDecimal R145_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R145_CCF_PCT;
	private BigDecimal R145_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R145_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R145_CEA_AFTER_NETTING;
	private BigDecimal R145_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R145_GUARANTEE_ELIGIBLE;
	private BigDecimal R145_CREDIT_DERIVATIVES;
	private BigDecimal R145_EXPOSURE_COVERED_CRM;
	private BigDecimal R145_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R145_RWA_TOTAL;
	private BigDecimal R145_CRM_RISK_WEIGHT;
	private BigDecimal R145_RWA_CRM_COVERED;
	private BigDecimal R145_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R145_RWA_NOT_COVERED;
	private BigDecimal R145_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R145_CEA_AFTER_VOL_ADJ;
	private BigDecimal R145_COLL_CASH;
	private BigDecimal R145_COLL_TBILLS;
	private BigDecimal R145_COLL_DEBT_SEC;
	private BigDecimal R145_COLL_EQUITIES;
	private BigDecimal R145_COLL_MUTUAL_FUNDS;
	private BigDecimal R145_COLL_TOTAL;
	private BigDecimal R145_CEA_AFTER_CRM;
	private BigDecimal R145_RWA_CEA_NOT_COVERED;
	private BigDecimal R145_UNSECURED_CEA;
	private BigDecimal R145_RWA_UNSECURED_CEA;
	private BigDecimal R146_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R146_CCF_PCT;
	private BigDecimal R146_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R146_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R146_CEA_AFTER_NETTING;
	private BigDecimal R146_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R146_GUARANTEE_ELIGIBLE;
	private BigDecimal R146_CREDIT_DERIVATIVES;
	private BigDecimal R146_EXPOSURE_COVERED_CRM;
	private BigDecimal R146_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R146_RWA_TOTAL;
	private BigDecimal R146_CRM_RISK_WEIGHT;
	private BigDecimal R146_RWA_CRM_COVERED;
	private BigDecimal R146_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R146_RWA_NOT_COVERED;
	private BigDecimal R146_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R146_CEA_AFTER_VOL_ADJ;
	private BigDecimal R146_COLL_CASH;
	private BigDecimal R146_COLL_TBILLS;
	private BigDecimal R146_COLL_DEBT_SEC;
	private BigDecimal R146_COLL_EQUITIES;
	private BigDecimal R146_COLL_MUTUAL_FUNDS;
	private BigDecimal R146_COLL_TOTAL;
	private BigDecimal R146_CEA_AFTER_CRM;
	private BigDecimal R146_RWA_CEA_NOT_COVERED;
	private BigDecimal R146_UNSECURED_CEA;
	private BigDecimal R146_RWA_UNSECURED_CEA;
	private BigDecimal R147_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R147_CCF_PCT;
	private BigDecimal R147_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R147_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R147_CEA_AFTER_NETTING;
	private BigDecimal R147_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R147_GUARANTEE_ELIGIBLE;
	private BigDecimal R147_CREDIT_DERIVATIVES;
	private BigDecimal R147_EXPOSURE_COVERED_CRM;
	private BigDecimal R147_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R147_RWA_TOTAL;
	private BigDecimal R147_CRM_RISK_WEIGHT;
	private BigDecimal R147_RWA_CRM_COVERED;
	private BigDecimal R147_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R147_RWA_NOT_COVERED;
	private BigDecimal R147_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R147_CEA_AFTER_VOL_ADJ;
	private BigDecimal R147_COLL_CASH;
	private BigDecimal R147_COLL_TBILLS;
	private BigDecimal R147_COLL_DEBT_SEC;
	private BigDecimal R147_COLL_EQUITIES;
	private BigDecimal R147_COLL_MUTUAL_FUNDS;
	private BigDecimal R147_COLL_TOTAL;
	private BigDecimal R147_CEA_AFTER_CRM;
	private BigDecimal R147_RWA_CEA_NOT_COVERED;
	private BigDecimal R147_UNSECURED_CEA;
	private BigDecimal R147_RWA_UNSECURED_CEA;
	private BigDecimal R148_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R148_CCF_PCT;
	private BigDecimal R148_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R148_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R148_CEA_AFTER_NETTING;
	private BigDecimal R148_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R148_GUARANTEE_ELIGIBLE;
	private BigDecimal R148_CREDIT_DERIVATIVES;
	private BigDecimal R148_EXPOSURE_COVERED_CRM;
	private BigDecimal R148_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R148_RWA_TOTAL;
	private BigDecimal R148_CRM_RISK_WEIGHT;
	private BigDecimal R148_RWA_CRM_COVERED;
	private BigDecimal R148_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R148_RWA_NOT_COVERED;
	private BigDecimal R148_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R148_CEA_AFTER_VOL_ADJ;
	private BigDecimal R148_COLL_CASH;
	private BigDecimal R148_COLL_TBILLS;
	private BigDecimal R148_COLL_DEBT_SEC;
	private BigDecimal R148_COLL_EQUITIES;
	private BigDecimal R148_COLL_MUTUAL_FUNDS;
	private BigDecimal R148_COLL_TOTAL;
	private BigDecimal R148_CEA_AFTER_CRM;
	private BigDecimal R148_RWA_CEA_NOT_COVERED;
	private BigDecimal R148_UNSECURED_CEA;
	private BigDecimal R148_RWA_UNSECURED_CEA;
	private BigDecimal R149_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R149_CCF_PCT;
	private BigDecimal R149_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R149_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R149_CEA_AFTER_NETTING;
	private BigDecimal R149_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R149_GUARANTEE_ELIGIBLE;
	private BigDecimal R149_CREDIT_DERIVATIVES;
	private BigDecimal R149_EXPOSURE_COVERED_CRM;
	private BigDecimal R149_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R149_RWA_TOTAL;
	private BigDecimal R149_CRM_RISK_WEIGHT;
	private BigDecimal R149_RWA_CRM_COVERED;
	private BigDecimal R149_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R149_RWA_NOT_COVERED;
	private BigDecimal R149_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R149_CEA_AFTER_VOL_ADJ;
	private BigDecimal R149_COLL_CASH;
	private BigDecimal R149_COLL_TBILLS;
	private BigDecimal R149_COLL_DEBT_SEC;
	private BigDecimal R149_COLL_EQUITIES;
	private BigDecimal R149_COLL_MUTUAL_FUNDS;
	private BigDecimal R149_COLL_TOTAL;
	private BigDecimal R149_CEA_AFTER_CRM;
	private BigDecimal R149_RWA_CEA_NOT_COVERED;
	private BigDecimal R149_UNSECURED_CEA;
	private BigDecimal R149_RWA_UNSECURED_CEA;
	private BigDecimal R150_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R150_CCF_PCT;
	private BigDecimal R150_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R150_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R150_CEA_AFTER_NETTING;
	private BigDecimal R150_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R150_GUARANTEE_ELIGIBLE;
	private BigDecimal R150_CREDIT_DERIVATIVES;
	private BigDecimal R150_EXPOSURE_COVERED_CRM;
	private BigDecimal R150_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R150_RWA_TOTAL;
	private BigDecimal R150_CRM_RISK_WEIGHT;
	private BigDecimal R150_RWA_CRM_COVERED;
	private BigDecimal R150_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R150_RWA_NOT_COVERED;
	private BigDecimal R150_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R150_CEA_AFTER_VOL_ADJ;
	private BigDecimal R150_COLL_CASH;
	private BigDecimal R150_COLL_TBILLS;
	private BigDecimal R150_COLL_DEBT_SEC;
	private BigDecimal R150_COLL_EQUITIES;
	private BigDecimal R150_COLL_MUTUAL_FUNDS;
	private BigDecimal R150_COLL_TOTAL;
	private BigDecimal R150_CEA_AFTER_CRM;
	private BigDecimal R150_RWA_CEA_NOT_COVERED;
	private BigDecimal R150_UNSECURED_CEA;
	private BigDecimal R150_RWA_UNSECURED_CEA;
	private BigDecimal R151_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R151_CCF_PCT;
	private BigDecimal R151_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R151_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R151_CEA_AFTER_NETTING;
	private BigDecimal R151_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R151_GUARANTEE_ELIGIBLE;
	private BigDecimal R151_CREDIT_DERIVATIVES;
	private BigDecimal R151_EXPOSURE_COVERED_CRM;
	private BigDecimal R151_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R151_RWA_TOTAL;
	private BigDecimal R151_CRM_RISK_WEIGHT;
	private BigDecimal R151_RWA_CRM_COVERED;
	private BigDecimal R151_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R151_RWA_NOT_COVERED;
	private BigDecimal R151_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R151_CEA_AFTER_VOL_ADJ;
	private BigDecimal R151_COLL_CASH;
	private BigDecimal R151_COLL_TBILLS;
	private BigDecimal R151_COLL_DEBT_SEC;
	private BigDecimal R151_COLL_EQUITIES;
	private BigDecimal R151_COLL_MUTUAL_FUNDS;
	private BigDecimal R151_COLL_TOTAL;
	private BigDecimal R151_CEA_AFTER_CRM;
	private BigDecimal R151_RWA_CEA_NOT_COVERED;
	private BigDecimal R151_UNSECURED_CEA;
	private BigDecimal R151_RWA_UNSECURED_CEA;
	private BigDecimal R152_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R152_CCF_PCT;
	private BigDecimal R152_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R152_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R152_CEA_AFTER_NETTING;
	private BigDecimal R152_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R152_GUARANTEE_ELIGIBLE;
	private BigDecimal R152_CREDIT_DERIVATIVES;
	private BigDecimal R152_EXPOSURE_COVERED_CRM;
	private BigDecimal R152_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R152_RWA_TOTAL;
	private BigDecimal R152_CRM_RISK_WEIGHT;
	private BigDecimal R152_RWA_CRM_COVERED;
	private BigDecimal R152_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R152_RWA_NOT_COVERED;
	private BigDecimal R152_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R152_CEA_AFTER_VOL_ADJ;
	private BigDecimal R152_COLL_CASH;
	private BigDecimal R152_COLL_TBILLS;
	private BigDecimal R152_COLL_DEBT_SEC;
	private BigDecimal R152_COLL_EQUITIES;
	private BigDecimal R152_COLL_MUTUAL_FUNDS;
	private BigDecimal R152_COLL_TOTAL;
	private BigDecimal R152_CEA_AFTER_CRM;
	private BigDecimal R152_RWA_CEA_NOT_COVERED;
	private BigDecimal R152_UNSECURED_CEA;
	private BigDecimal R152_RWA_UNSECURED_CEA;
	private BigDecimal R153_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R153_CCF_PCT;
	private BigDecimal R153_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R153_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R153_CEA_AFTER_NETTING;
	private BigDecimal R153_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R153_GUARANTEE_ELIGIBLE;
	private BigDecimal R153_CREDIT_DERIVATIVES;
	private BigDecimal R153_EXPOSURE_COVERED_CRM;
	private BigDecimal R153_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R153_RWA_TOTAL;
	private BigDecimal R153_CRM_RISK_WEIGHT;
	private BigDecimal R153_RWA_CRM_COVERED;
	private BigDecimal R153_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R153_RWA_NOT_COVERED;
	private BigDecimal R153_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R153_CEA_AFTER_VOL_ADJ;
	private BigDecimal R153_COLL_CASH;
	private BigDecimal R153_COLL_TBILLS;
	private BigDecimal R153_COLL_DEBT_SEC;
	private BigDecimal R153_COLL_EQUITIES;
	private BigDecimal R153_COLL_MUTUAL_FUNDS;
	private BigDecimal R153_COLL_TOTAL;
	private BigDecimal R153_CEA_AFTER_CRM;
	private BigDecimal R153_RWA_CEA_NOT_COVERED;
	private BigDecimal R153_UNSECURED_CEA;
	private BigDecimal R153_RWA_UNSECURED_CEA;
	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}
	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}
	public String getREPORT_VERSION() {
		return REPORT_VERSION;
	}
	public void setREPORT_VERSION(String rEPORT_VERSION) {
		REPORT_VERSION = rEPORT_VERSION;
	}
	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}
	public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
		REPORT_FREQUENCY = rEPORT_FREQUENCY;
	}
	public String getREPORT_CODE() {
		return REPORT_CODE;
	}
	public void setREPORT_CODE(String rEPORT_CODE) {
		REPORT_CODE = rEPORT_CODE;
	}
	public String getREPORT_DESC() {
		return REPORT_DESC;
	}
	public void setREPORT_DESC(String rEPORT_DESC) {
		REPORT_DESC = rEPORT_DESC;
	}
	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}
	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}
	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}
	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}
	public String getDEL_FLG() {
		return DEL_FLG;
	}
	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}
	public BigDecimal getR123_NOMINAL_PRINCIPAL_AMT() {
		return R123_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR123_NOMINAL_PRINCIPAL_AMT(BigDecimal r123_NOMINAL_PRINCIPAL_AMT) {
		R123_NOMINAL_PRINCIPAL_AMT = r123_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR123_CCF_PCT() {
		return R123_CCF_PCT;
	}
	public void setR123_CCF_PCT(BigDecimal r123_CCF_PCT) {
		R123_CCF_PCT = r123_CCF_PCT;
	}
	public BigDecimal getR123_CREDIT_EQUIVALENT_AMT() {
		return R123_CREDIT_EQUIVALENT_AMT;
	}
	public void setR123_CREDIT_EQUIVALENT_AMT(BigDecimal r123_CREDIT_EQUIVALENT_AMT) {
		R123_CREDIT_EQUIVALENT_AMT = r123_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR123_CEA_ELIGIBLE_NETTING_CP() {
		return R123_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR123_CEA_ELIGIBLE_NETTING_CP(BigDecimal r123_CEA_ELIGIBLE_NETTING_CP) {
		R123_CEA_ELIGIBLE_NETTING_CP = r123_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR123_CEA_AFTER_NETTING() {
		return R123_CEA_AFTER_NETTING;
	}
	public void setR123_CEA_AFTER_NETTING(BigDecimal r123_CEA_AFTER_NETTING) {
		R123_CEA_AFTER_NETTING = r123_CEA_AFTER_NETTING;
	}
	public BigDecimal getR123_CEA_ELIGIBLE_CRM_SUB() {
		return R123_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR123_CEA_ELIGIBLE_CRM_SUB(BigDecimal r123_CEA_ELIGIBLE_CRM_SUB) {
		R123_CEA_ELIGIBLE_CRM_SUB = r123_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR123_GUARANTEE_ELIGIBLE() {
		return R123_GUARANTEE_ELIGIBLE;
	}
	public void setR123_GUARANTEE_ELIGIBLE(BigDecimal r123_GUARANTEE_ELIGIBLE) {
		R123_GUARANTEE_ELIGIBLE = r123_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR123_CREDIT_DERIVATIVES() {
		return R123_CREDIT_DERIVATIVES;
	}
	public void setR123_CREDIT_DERIVATIVES(BigDecimal r123_CREDIT_DERIVATIVES) {
		R123_CREDIT_DERIVATIVES = r123_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR123_EXPOSURE_COVERED_CRM() {
		return R123_EXPOSURE_COVERED_CRM;
	}
	public void setR123_EXPOSURE_COVERED_CRM(BigDecimal r123_EXPOSURE_COVERED_CRM) {
		R123_EXPOSURE_COVERED_CRM = r123_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR123_EXPOSURE_NOT_COVERED_CRM() {
		return R123_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR123_EXPOSURE_NOT_COVERED_CRM(BigDecimal r123_EXPOSURE_NOT_COVERED_CRM) {
		R123_EXPOSURE_NOT_COVERED_CRM = r123_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR123_RWA_TOTAL() {
		return R123_RWA_TOTAL;
	}
	public void setR123_RWA_TOTAL(BigDecimal r123_RWA_TOTAL) {
		R123_RWA_TOTAL = r123_RWA_TOTAL;
	}
	public BigDecimal getR123_CRM_RISK_WEIGHT() {
		return R123_CRM_RISK_WEIGHT;
	}
	public void setR123_CRM_RISK_WEIGHT(BigDecimal r123_CRM_RISK_WEIGHT) {
		R123_CRM_RISK_WEIGHT = r123_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR123_RWA_CRM_COVERED() {
		return R123_RWA_CRM_COVERED;
	}
	public void setR123_RWA_CRM_COVERED(BigDecimal r123_RWA_CRM_COVERED) {
		R123_RWA_CRM_COVERED = r123_RWA_CRM_COVERED;
	}
	public BigDecimal getR123_ORIGINAL_CP_RISK_WEIGHT() {
		return R123_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR123_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r123_ORIGINAL_CP_RISK_WEIGHT) {
		R123_ORIGINAL_CP_RISK_WEIGHT = r123_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR123_RWA_NOT_COVERED() {
		return R123_RWA_NOT_COVERED;
	}
	public void setR123_RWA_NOT_COVERED(BigDecimal r123_RWA_NOT_COVERED) {
		R123_RWA_NOT_COVERED = r123_RWA_NOT_COVERED;
	}
	public BigDecimal getR123_COLLATERAL_CEA_ELIGIBLE() {
		return R123_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR123_COLLATERAL_CEA_ELIGIBLE(BigDecimal r123_COLLATERAL_CEA_ELIGIBLE) {
		R123_COLLATERAL_CEA_ELIGIBLE = r123_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR123_CEA_AFTER_VOL_ADJ() {
		return R123_CEA_AFTER_VOL_ADJ;
	}
	public void setR123_CEA_AFTER_VOL_ADJ(BigDecimal r123_CEA_AFTER_VOL_ADJ) {
		R123_CEA_AFTER_VOL_ADJ = r123_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR123_COLL_CASH() {
		return R123_COLL_CASH;
	}
	public void setR123_COLL_CASH(BigDecimal r123_COLL_CASH) {
		R123_COLL_CASH = r123_COLL_CASH;
	}
	public BigDecimal getR123_COLL_TBILLS() {
		return R123_COLL_TBILLS;
	}
	public void setR123_COLL_TBILLS(BigDecimal r123_COLL_TBILLS) {
		R123_COLL_TBILLS = r123_COLL_TBILLS;
	}
	public BigDecimal getR123_COLL_DEBT_SEC() {
		return R123_COLL_DEBT_SEC;
	}
	public void setR123_COLL_DEBT_SEC(BigDecimal r123_COLL_DEBT_SEC) {
		R123_COLL_DEBT_SEC = r123_COLL_DEBT_SEC;
	}
	public BigDecimal getR123_COLL_EQUITIES() {
		return R123_COLL_EQUITIES;
	}
	public void setR123_COLL_EQUITIES(BigDecimal r123_COLL_EQUITIES) {
		R123_COLL_EQUITIES = r123_COLL_EQUITIES;
	}
	public BigDecimal getR123_COLL_MUTUAL_FUNDS() {
		return R123_COLL_MUTUAL_FUNDS;
	}
	public void setR123_COLL_MUTUAL_FUNDS(BigDecimal r123_COLL_MUTUAL_FUNDS) {
		R123_COLL_MUTUAL_FUNDS = r123_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR123_COLL_TOTAL() {
		return R123_COLL_TOTAL;
	}
	public void setR123_COLL_TOTAL(BigDecimal r123_COLL_TOTAL) {
		R123_COLL_TOTAL = r123_COLL_TOTAL;
	}
	public BigDecimal getR123_CEA_AFTER_CRM() {
		return R123_CEA_AFTER_CRM;
	}
	public void setR123_CEA_AFTER_CRM(BigDecimal r123_CEA_AFTER_CRM) {
		R123_CEA_AFTER_CRM = r123_CEA_AFTER_CRM;
	}
	public BigDecimal getR123_RWA_CEA_NOT_COVERED() {
		return R123_RWA_CEA_NOT_COVERED;
	}
	public void setR123_RWA_CEA_NOT_COVERED(BigDecimal r123_RWA_CEA_NOT_COVERED) {
		R123_RWA_CEA_NOT_COVERED = r123_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR123_UNSECURED_CEA() {
		return R123_UNSECURED_CEA;
	}
	public void setR123_UNSECURED_CEA(BigDecimal r123_UNSECURED_CEA) {
		R123_UNSECURED_CEA = r123_UNSECURED_CEA;
	}
	public BigDecimal getR123_RWA_UNSECURED_CEA() {
		return R123_RWA_UNSECURED_CEA;
	}
	public void setR123_RWA_UNSECURED_CEA(BigDecimal r123_RWA_UNSECURED_CEA) {
		R123_RWA_UNSECURED_CEA = r123_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR124_NOMINAL_PRINCIPAL_AMT() {
		return R124_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR124_NOMINAL_PRINCIPAL_AMT(BigDecimal r124_NOMINAL_PRINCIPAL_AMT) {
		R124_NOMINAL_PRINCIPAL_AMT = r124_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR124_CCF_PCT() {
		return R124_CCF_PCT;
	}
	public void setR124_CCF_PCT(BigDecimal r124_CCF_PCT) {
		R124_CCF_PCT = r124_CCF_PCT;
	}
	public BigDecimal getR124_CREDIT_EQUIVALENT_AMT() {
		return R124_CREDIT_EQUIVALENT_AMT;
	}
	public void setR124_CREDIT_EQUIVALENT_AMT(BigDecimal r124_CREDIT_EQUIVALENT_AMT) {
		R124_CREDIT_EQUIVALENT_AMT = r124_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR124_CEA_ELIGIBLE_NETTING_CP() {
		return R124_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR124_CEA_ELIGIBLE_NETTING_CP(BigDecimal r124_CEA_ELIGIBLE_NETTING_CP) {
		R124_CEA_ELIGIBLE_NETTING_CP = r124_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR124_CEA_AFTER_NETTING() {
		return R124_CEA_AFTER_NETTING;
	}
	public void setR124_CEA_AFTER_NETTING(BigDecimal r124_CEA_AFTER_NETTING) {
		R124_CEA_AFTER_NETTING = r124_CEA_AFTER_NETTING;
	}
	public BigDecimal getR124_CEA_ELIGIBLE_CRM_SUB() {
		return R124_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR124_CEA_ELIGIBLE_CRM_SUB(BigDecimal r124_CEA_ELIGIBLE_CRM_SUB) {
		R124_CEA_ELIGIBLE_CRM_SUB = r124_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR124_GUARANTEE_ELIGIBLE() {
		return R124_GUARANTEE_ELIGIBLE;
	}
	public void setR124_GUARANTEE_ELIGIBLE(BigDecimal r124_GUARANTEE_ELIGIBLE) {
		R124_GUARANTEE_ELIGIBLE = r124_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR124_CREDIT_DERIVATIVES() {
		return R124_CREDIT_DERIVATIVES;
	}
	public void setR124_CREDIT_DERIVATIVES(BigDecimal r124_CREDIT_DERIVATIVES) {
		R124_CREDIT_DERIVATIVES = r124_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR124_EXPOSURE_COVERED_CRM() {
		return R124_EXPOSURE_COVERED_CRM;
	}
	public void setR124_EXPOSURE_COVERED_CRM(BigDecimal r124_EXPOSURE_COVERED_CRM) {
		R124_EXPOSURE_COVERED_CRM = r124_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR124_EXPOSURE_NOT_COVERED_CRM() {
		return R124_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR124_EXPOSURE_NOT_COVERED_CRM(BigDecimal r124_EXPOSURE_NOT_COVERED_CRM) {
		R124_EXPOSURE_NOT_COVERED_CRM = r124_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR124_RWA_TOTAL() {
		return R124_RWA_TOTAL;
	}
	public void setR124_RWA_TOTAL(BigDecimal r124_RWA_TOTAL) {
		R124_RWA_TOTAL = r124_RWA_TOTAL;
	}
	public BigDecimal getR124_CRM_RISK_WEIGHT() {
		return R124_CRM_RISK_WEIGHT;
	}
	public void setR124_CRM_RISK_WEIGHT(BigDecimal r124_CRM_RISK_WEIGHT) {
		R124_CRM_RISK_WEIGHT = r124_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR124_RWA_CRM_COVERED() {
		return R124_RWA_CRM_COVERED;
	}
	public void setR124_RWA_CRM_COVERED(BigDecimal r124_RWA_CRM_COVERED) {
		R124_RWA_CRM_COVERED = r124_RWA_CRM_COVERED;
	}
	public BigDecimal getR124_ORIGINAL_CP_RISK_WEIGHT() {
		return R124_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR124_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r124_ORIGINAL_CP_RISK_WEIGHT) {
		R124_ORIGINAL_CP_RISK_WEIGHT = r124_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR124_RWA_NOT_COVERED() {
		return R124_RWA_NOT_COVERED;
	}
	public void setR124_RWA_NOT_COVERED(BigDecimal r124_RWA_NOT_COVERED) {
		R124_RWA_NOT_COVERED = r124_RWA_NOT_COVERED;
	}
	public BigDecimal getR124_COLLATERAL_CEA_ELIGIBLE() {
		return R124_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR124_COLLATERAL_CEA_ELIGIBLE(BigDecimal r124_COLLATERAL_CEA_ELIGIBLE) {
		R124_COLLATERAL_CEA_ELIGIBLE = r124_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR124_CEA_AFTER_VOL_ADJ() {
		return R124_CEA_AFTER_VOL_ADJ;
	}
	public void setR124_CEA_AFTER_VOL_ADJ(BigDecimal r124_CEA_AFTER_VOL_ADJ) {
		R124_CEA_AFTER_VOL_ADJ = r124_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR124_COLL_CASH() {
		return R124_COLL_CASH;
	}
	public void setR124_COLL_CASH(BigDecimal r124_COLL_CASH) {
		R124_COLL_CASH = r124_COLL_CASH;
	}
	public BigDecimal getR124_COLL_TBILLS() {
		return R124_COLL_TBILLS;
	}
	public void setR124_COLL_TBILLS(BigDecimal r124_COLL_TBILLS) {
		R124_COLL_TBILLS = r124_COLL_TBILLS;
	}
	public BigDecimal getR124_COLL_DEBT_SEC() {
		return R124_COLL_DEBT_SEC;
	}
	public void setR124_COLL_DEBT_SEC(BigDecimal r124_COLL_DEBT_SEC) {
		R124_COLL_DEBT_SEC = r124_COLL_DEBT_SEC;
	}
	public BigDecimal getR124_COLL_EQUITIES() {
		return R124_COLL_EQUITIES;
	}
	public void setR124_COLL_EQUITIES(BigDecimal r124_COLL_EQUITIES) {
		R124_COLL_EQUITIES = r124_COLL_EQUITIES;
	}
	public BigDecimal getR124_COLL_MUTUAL_FUNDS() {
		return R124_COLL_MUTUAL_FUNDS;
	}
	public void setR124_COLL_MUTUAL_FUNDS(BigDecimal r124_COLL_MUTUAL_FUNDS) {
		R124_COLL_MUTUAL_FUNDS = r124_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR124_COLL_TOTAL() {
		return R124_COLL_TOTAL;
	}
	public void setR124_COLL_TOTAL(BigDecimal r124_COLL_TOTAL) {
		R124_COLL_TOTAL = r124_COLL_TOTAL;
	}
	public BigDecimal getR124_CEA_AFTER_CRM() {
		return R124_CEA_AFTER_CRM;
	}
	public void setR124_CEA_AFTER_CRM(BigDecimal r124_CEA_AFTER_CRM) {
		R124_CEA_AFTER_CRM = r124_CEA_AFTER_CRM;
	}
	public BigDecimal getR124_RWA_CEA_NOT_COVERED() {
		return R124_RWA_CEA_NOT_COVERED;
	}
	public void setR124_RWA_CEA_NOT_COVERED(BigDecimal r124_RWA_CEA_NOT_COVERED) {
		R124_RWA_CEA_NOT_COVERED = r124_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR124_UNSECURED_CEA() {
		return R124_UNSECURED_CEA;
	}
	public void setR124_UNSECURED_CEA(BigDecimal r124_UNSECURED_CEA) {
		R124_UNSECURED_CEA = r124_UNSECURED_CEA;
	}
	public BigDecimal getR124_RWA_UNSECURED_CEA() {
		return R124_RWA_UNSECURED_CEA;
	}
	public void setR124_RWA_UNSECURED_CEA(BigDecimal r124_RWA_UNSECURED_CEA) {
		R124_RWA_UNSECURED_CEA = r124_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR125_NOMINAL_PRINCIPAL_AMT() {
		return R125_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR125_NOMINAL_PRINCIPAL_AMT(BigDecimal r125_NOMINAL_PRINCIPAL_AMT) {
		R125_NOMINAL_PRINCIPAL_AMT = r125_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR125_CCF_PCT() {
		return R125_CCF_PCT;
	}
	public void setR125_CCF_PCT(BigDecimal r125_CCF_PCT) {
		R125_CCF_PCT = r125_CCF_PCT;
	}
	public BigDecimal getR125_CREDIT_EQUIVALENT_AMT() {
		return R125_CREDIT_EQUIVALENT_AMT;
	}
	public void setR125_CREDIT_EQUIVALENT_AMT(BigDecimal r125_CREDIT_EQUIVALENT_AMT) {
		R125_CREDIT_EQUIVALENT_AMT = r125_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR125_CEA_ELIGIBLE_NETTING_CP() {
		return R125_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR125_CEA_ELIGIBLE_NETTING_CP(BigDecimal r125_CEA_ELIGIBLE_NETTING_CP) {
		R125_CEA_ELIGIBLE_NETTING_CP = r125_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR125_CEA_AFTER_NETTING() {
		return R125_CEA_AFTER_NETTING;
	}
	public void setR125_CEA_AFTER_NETTING(BigDecimal r125_CEA_AFTER_NETTING) {
		R125_CEA_AFTER_NETTING = r125_CEA_AFTER_NETTING;
	}
	public BigDecimal getR125_CEA_ELIGIBLE_CRM_SUB() {
		return R125_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR125_CEA_ELIGIBLE_CRM_SUB(BigDecimal r125_CEA_ELIGIBLE_CRM_SUB) {
		R125_CEA_ELIGIBLE_CRM_SUB = r125_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR125_GUARANTEE_ELIGIBLE() {
		return R125_GUARANTEE_ELIGIBLE;
	}
	public void setR125_GUARANTEE_ELIGIBLE(BigDecimal r125_GUARANTEE_ELIGIBLE) {
		R125_GUARANTEE_ELIGIBLE = r125_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR125_CREDIT_DERIVATIVES() {
		return R125_CREDIT_DERIVATIVES;
	}
	public void setR125_CREDIT_DERIVATIVES(BigDecimal r125_CREDIT_DERIVATIVES) {
		R125_CREDIT_DERIVATIVES = r125_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR125_EXPOSURE_COVERED_CRM() {
		return R125_EXPOSURE_COVERED_CRM;
	}
	public void setR125_EXPOSURE_COVERED_CRM(BigDecimal r125_EXPOSURE_COVERED_CRM) {
		R125_EXPOSURE_COVERED_CRM = r125_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR125_EXPOSURE_NOT_COVERED_CRM() {
		return R125_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR125_EXPOSURE_NOT_COVERED_CRM(BigDecimal r125_EXPOSURE_NOT_COVERED_CRM) {
		R125_EXPOSURE_NOT_COVERED_CRM = r125_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR125_RWA_TOTAL() {
		return R125_RWA_TOTAL;
	}
	public void setR125_RWA_TOTAL(BigDecimal r125_RWA_TOTAL) {
		R125_RWA_TOTAL = r125_RWA_TOTAL;
	}
	public BigDecimal getR125_CRM_RISK_WEIGHT() {
		return R125_CRM_RISK_WEIGHT;
	}
	public void setR125_CRM_RISK_WEIGHT(BigDecimal r125_CRM_RISK_WEIGHT) {
		R125_CRM_RISK_WEIGHT = r125_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR125_RWA_CRM_COVERED() {
		return R125_RWA_CRM_COVERED;
	}
	public void setR125_RWA_CRM_COVERED(BigDecimal r125_RWA_CRM_COVERED) {
		R125_RWA_CRM_COVERED = r125_RWA_CRM_COVERED;
	}
	public BigDecimal getR125_ORIGINAL_CP_RISK_WEIGHT() {
		return R125_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR125_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r125_ORIGINAL_CP_RISK_WEIGHT) {
		R125_ORIGINAL_CP_RISK_WEIGHT = r125_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR125_RWA_NOT_COVERED() {
		return R125_RWA_NOT_COVERED;
	}
	public void setR125_RWA_NOT_COVERED(BigDecimal r125_RWA_NOT_COVERED) {
		R125_RWA_NOT_COVERED = r125_RWA_NOT_COVERED;
	}
	public BigDecimal getR125_COLLATERAL_CEA_ELIGIBLE() {
		return R125_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR125_COLLATERAL_CEA_ELIGIBLE(BigDecimal r125_COLLATERAL_CEA_ELIGIBLE) {
		R125_COLLATERAL_CEA_ELIGIBLE = r125_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR125_CEA_AFTER_VOL_ADJ() {
		return R125_CEA_AFTER_VOL_ADJ;
	}
	public void setR125_CEA_AFTER_VOL_ADJ(BigDecimal r125_CEA_AFTER_VOL_ADJ) {
		R125_CEA_AFTER_VOL_ADJ = r125_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR125_COLL_CASH() {
		return R125_COLL_CASH;
	}
	public void setR125_COLL_CASH(BigDecimal r125_COLL_CASH) {
		R125_COLL_CASH = r125_COLL_CASH;
	}
	public BigDecimal getR125_COLL_TBILLS() {
		return R125_COLL_TBILLS;
	}
	public void setR125_COLL_TBILLS(BigDecimal r125_COLL_TBILLS) {
		R125_COLL_TBILLS = r125_COLL_TBILLS;
	}
	public BigDecimal getR125_COLL_DEBT_SEC() {
		return R125_COLL_DEBT_SEC;
	}
	public void setR125_COLL_DEBT_SEC(BigDecimal r125_COLL_DEBT_SEC) {
		R125_COLL_DEBT_SEC = r125_COLL_DEBT_SEC;
	}
	public BigDecimal getR125_COLL_EQUITIES() {
		return R125_COLL_EQUITIES;
	}
	public void setR125_COLL_EQUITIES(BigDecimal r125_COLL_EQUITIES) {
		R125_COLL_EQUITIES = r125_COLL_EQUITIES;
	}
	public BigDecimal getR125_COLL_MUTUAL_FUNDS() {
		return R125_COLL_MUTUAL_FUNDS;
	}
	public void setR125_COLL_MUTUAL_FUNDS(BigDecimal r125_COLL_MUTUAL_FUNDS) {
		R125_COLL_MUTUAL_FUNDS = r125_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR125_COLL_TOTAL() {
		return R125_COLL_TOTAL;
	}
	public void setR125_COLL_TOTAL(BigDecimal r125_COLL_TOTAL) {
		R125_COLL_TOTAL = r125_COLL_TOTAL;
	}
	public BigDecimal getR125_CEA_AFTER_CRM() {
		return R125_CEA_AFTER_CRM;
	}
	public void setR125_CEA_AFTER_CRM(BigDecimal r125_CEA_AFTER_CRM) {
		R125_CEA_AFTER_CRM = r125_CEA_AFTER_CRM;
	}
	public BigDecimal getR125_RWA_CEA_NOT_COVERED() {
		return R125_RWA_CEA_NOT_COVERED;
	}
	public void setR125_RWA_CEA_NOT_COVERED(BigDecimal r125_RWA_CEA_NOT_COVERED) {
		R125_RWA_CEA_NOT_COVERED = r125_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR125_UNSECURED_CEA() {
		return R125_UNSECURED_CEA;
	}
	public void setR125_UNSECURED_CEA(BigDecimal r125_UNSECURED_CEA) {
		R125_UNSECURED_CEA = r125_UNSECURED_CEA;
	}
	public BigDecimal getR125_RWA_UNSECURED_CEA() {
		return R125_RWA_UNSECURED_CEA;
	}
	public void setR125_RWA_UNSECURED_CEA(BigDecimal r125_RWA_UNSECURED_CEA) {
		R125_RWA_UNSECURED_CEA = r125_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR126_NOMINAL_PRINCIPAL_AMT() {
		return R126_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR126_NOMINAL_PRINCIPAL_AMT(BigDecimal r126_NOMINAL_PRINCIPAL_AMT) {
		R126_NOMINAL_PRINCIPAL_AMT = r126_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR126_CCF_PCT() {
		return R126_CCF_PCT;
	}
	public void setR126_CCF_PCT(BigDecimal r126_CCF_PCT) {
		R126_CCF_PCT = r126_CCF_PCT;
	}
	public BigDecimal getR126_CREDIT_EQUIVALENT_AMT() {
		return R126_CREDIT_EQUIVALENT_AMT;
	}
	public void setR126_CREDIT_EQUIVALENT_AMT(BigDecimal r126_CREDIT_EQUIVALENT_AMT) {
		R126_CREDIT_EQUIVALENT_AMT = r126_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR126_CEA_ELIGIBLE_NETTING_CP() {
		return R126_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR126_CEA_ELIGIBLE_NETTING_CP(BigDecimal r126_CEA_ELIGIBLE_NETTING_CP) {
		R126_CEA_ELIGIBLE_NETTING_CP = r126_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR126_CEA_AFTER_NETTING() {
		return R126_CEA_AFTER_NETTING;
	}
	public void setR126_CEA_AFTER_NETTING(BigDecimal r126_CEA_AFTER_NETTING) {
		R126_CEA_AFTER_NETTING = r126_CEA_AFTER_NETTING;
	}
	public BigDecimal getR126_CEA_ELIGIBLE_CRM_SUB() {
		return R126_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR126_CEA_ELIGIBLE_CRM_SUB(BigDecimal r126_CEA_ELIGIBLE_CRM_SUB) {
		R126_CEA_ELIGIBLE_CRM_SUB = r126_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR126_GUARANTEE_ELIGIBLE() {
		return R126_GUARANTEE_ELIGIBLE;
	}
	public void setR126_GUARANTEE_ELIGIBLE(BigDecimal r126_GUARANTEE_ELIGIBLE) {
		R126_GUARANTEE_ELIGIBLE = r126_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR126_CREDIT_DERIVATIVES() {
		return R126_CREDIT_DERIVATIVES;
	}
	public void setR126_CREDIT_DERIVATIVES(BigDecimal r126_CREDIT_DERIVATIVES) {
		R126_CREDIT_DERIVATIVES = r126_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR126_EXPOSURE_COVERED_CRM() {
		return R126_EXPOSURE_COVERED_CRM;
	}
	public void setR126_EXPOSURE_COVERED_CRM(BigDecimal r126_EXPOSURE_COVERED_CRM) {
		R126_EXPOSURE_COVERED_CRM = r126_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR126_EXPOSURE_NOT_COVERED_CRM() {
		return R126_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR126_EXPOSURE_NOT_COVERED_CRM(BigDecimal r126_EXPOSURE_NOT_COVERED_CRM) {
		R126_EXPOSURE_NOT_COVERED_CRM = r126_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR126_RWA_TOTAL() {
		return R126_RWA_TOTAL;
	}
	public void setR126_RWA_TOTAL(BigDecimal r126_RWA_TOTAL) {
		R126_RWA_TOTAL = r126_RWA_TOTAL;
	}
	public BigDecimal getR126_CRM_RISK_WEIGHT() {
		return R126_CRM_RISK_WEIGHT;
	}
	public void setR126_CRM_RISK_WEIGHT(BigDecimal r126_CRM_RISK_WEIGHT) {
		R126_CRM_RISK_WEIGHT = r126_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR126_RWA_CRM_COVERED() {
		return R126_RWA_CRM_COVERED;
	}
	public void setR126_RWA_CRM_COVERED(BigDecimal r126_RWA_CRM_COVERED) {
		R126_RWA_CRM_COVERED = r126_RWA_CRM_COVERED;
	}
	public BigDecimal getR126_ORIGINAL_CP_RISK_WEIGHT() {
		return R126_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR126_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r126_ORIGINAL_CP_RISK_WEIGHT) {
		R126_ORIGINAL_CP_RISK_WEIGHT = r126_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR126_RWA_NOT_COVERED() {
		return R126_RWA_NOT_COVERED;
	}
	public void setR126_RWA_NOT_COVERED(BigDecimal r126_RWA_NOT_COVERED) {
		R126_RWA_NOT_COVERED = r126_RWA_NOT_COVERED;
	}
	public BigDecimal getR126_COLLATERAL_CEA_ELIGIBLE() {
		return R126_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR126_COLLATERAL_CEA_ELIGIBLE(BigDecimal r126_COLLATERAL_CEA_ELIGIBLE) {
		R126_COLLATERAL_CEA_ELIGIBLE = r126_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR126_CEA_AFTER_VOL_ADJ() {
		return R126_CEA_AFTER_VOL_ADJ;
	}
	public void setR126_CEA_AFTER_VOL_ADJ(BigDecimal r126_CEA_AFTER_VOL_ADJ) {
		R126_CEA_AFTER_VOL_ADJ = r126_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR126_COLL_CASH() {
		return R126_COLL_CASH;
	}
	public void setR126_COLL_CASH(BigDecimal r126_COLL_CASH) {
		R126_COLL_CASH = r126_COLL_CASH;
	}
	public BigDecimal getR126_COLL_TBILLS() {
		return R126_COLL_TBILLS;
	}
	public void setR126_COLL_TBILLS(BigDecimal r126_COLL_TBILLS) {
		R126_COLL_TBILLS = r126_COLL_TBILLS;
	}
	public BigDecimal getR126_COLL_DEBT_SEC() {
		return R126_COLL_DEBT_SEC;
	}
	public void setR126_COLL_DEBT_SEC(BigDecimal r126_COLL_DEBT_SEC) {
		R126_COLL_DEBT_SEC = r126_COLL_DEBT_SEC;
	}
	public BigDecimal getR126_COLL_EQUITIES() {
		return R126_COLL_EQUITIES;
	}
	public void setR126_COLL_EQUITIES(BigDecimal r126_COLL_EQUITIES) {
		R126_COLL_EQUITIES = r126_COLL_EQUITIES;
	}
	public BigDecimal getR126_COLL_MUTUAL_FUNDS() {
		return R126_COLL_MUTUAL_FUNDS;
	}
	public void setR126_COLL_MUTUAL_FUNDS(BigDecimal r126_COLL_MUTUAL_FUNDS) {
		R126_COLL_MUTUAL_FUNDS = r126_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR126_COLL_TOTAL() {
		return R126_COLL_TOTAL;
	}
	public void setR126_COLL_TOTAL(BigDecimal r126_COLL_TOTAL) {
		R126_COLL_TOTAL = r126_COLL_TOTAL;
	}
	public BigDecimal getR126_CEA_AFTER_CRM() {
		return R126_CEA_AFTER_CRM;
	}
	public void setR126_CEA_AFTER_CRM(BigDecimal r126_CEA_AFTER_CRM) {
		R126_CEA_AFTER_CRM = r126_CEA_AFTER_CRM;
	}
	public BigDecimal getR126_RWA_CEA_NOT_COVERED() {
		return R126_RWA_CEA_NOT_COVERED;
	}
	public void setR126_RWA_CEA_NOT_COVERED(BigDecimal r126_RWA_CEA_NOT_COVERED) {
		R126_RWA_CEA_NOT_COVERED = r126_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR126_UNSECURED_CEA() {
		return R126_UNSECURED_CEA;
	}
	public void setR126_UNSECURED_CEA(BigDecimal r126_UNSECURED_CEA) {
		R126_UNSECURED_CEA = r126_UNSECURED_CEA;
	}
	public BigDecimal getR126_RWA_UNSECURED_CEA() {
		return R126_RWA_UNSECURED_CEA;
	}
	public void setR126_RWA_UNSECURED_CEA(BigDecimal r126_RWA_UNSECURED_CEA) {
		R126_RWA_UNSECURED_CEA = r126_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR127_NOMINAL_PRINCIPAL_AMT() {
		return R127_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR127_NOMINAL_PRINCIPAL_AMT(BigDecimal r127_NOMINAL_PRINCIPAL_AMT) {
		R127_NOMINAL_PRINCIPAL_AMT = r127_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR127_CCF_PCT() {
		return R127_CCF_PCT;
	}
	public void setR127_CCF_PCT(BigDecimal r127_CCF_PCT) {
		R127_CCF_PCT = r127_CCF_PCT;
	}
	public BigDecimal getR127_CREDIT_EQUIVALENT_AMT() {
		return R127_CREDIT_EQUIVALENT_AMT;
	}
	public void setR127_CREDIT_EQUIVALENT_AMT(BigDecimal r127_CREDIT_EQUIVALENT_AMT) {
		R127_CREDIT_EQUIVALENT_AMT = r127_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR127_CEA_ELIGIBLE_NETTING_CP() {
		return R127_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR127_CEA_ELIGIBLE_NETTING_CP(BigDecimal r127_CEA_ELIGIBLE_NETTING_CP) {
		R127_CEA_ELIGIBLE_NETTING_CP = r127_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR127_CEA_AFTER_NETTING() {
		return R127_CEA_AFTER_NETTING;
	}
	public void setR127_CEA_AFTER_NETTING(BigDecimal r127_CEA_AFTER_NETTING) {
		R127_CEA_AFTER_NETTING = r127_CEA_AFTER_NETTING;
	}
	public BigDecimal getR127_CEA_ELIGIBLE_CRM_SUB() {
		return R127_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR127_CEA_ELIGIBLE_CRM_SUB(BigDecimal r127_CEA_ELIGIBLE_CRM_SUB) {
		R127_CEA_ELIGIBLE_CRM_SUB = r127_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR127_GUARANTEE_ELIGIBLE() {
		return R127_GUARANTEE_ELIGIBLE;
	}
	public void setR127_GUARANTEE_ELIGIBLE(BigDecimal r127_GUARANTEE_ELIGIBLE) {
		R127_GUARANTEE_ELIGIBLE = r127_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR127_CREDIT_DERIVATIVES() {
		return R127_CREDIT_DERIVATIVES;
	}
	public void setR127_CREDIT_DERIVATIVES(BigDecimal r127_CREDIT_DERIVATIVES) {
		R127_CREDIT_DERIVATIVES = r127_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR127_EXPOSURE_COVERED_CRM() {
		return R127_EXPOSURE_COVERED_CRM;
	}
	public void setR127_EXPOSURE_COVERED_CRM(BigDecimal r127_EXPOSURE_COVERED_CRM) {
		R127_EXPOSURE_COVERED_CRM = r127_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR127_EXPOSURE_NOT_COVERED_CRM() {
		return R127_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR127_EXPOSURE_NOT_COVERED_CRM(BigDecimal r127_EXPOSURE_NOT_COVERED_CRM) {
		R127_EXPOSURE_NOT_COVERED_CRM = r127_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR127_RWA_TOTAL() {
		return R127_RWA_TOTAL;
	}
	public void setR127_RWA_TOTAL(BigDecimal r127_RWA_TOTAL) {
		R127_RWA_TOTAL = r127_RWA_TOTAL;
	}
	public BigDecimal getR127_CRM_RISK_WEIGHT() {
		return R127_CRM_RISK_WEIGHT;
	}
	public void setR127_CRM_RISK_WEIGHT(BigDecimal r127_CRM_RISK_WEIGHT) {
		R127_CRM_RISK_WEIGHT = r127_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR127_RWA_CRM_COVERED() {
		return R127_RWA_CRM_COVERED;
	}
	public void setR127_RWA_CRM_COVERED(BigDecimal r127_RWA_CRM_COVERED) {
		R127_RWA_CRM_COVERED = r127_RWA_CRM_COVERED;
	}
	public BigDecimal getR127_ORIGINAL_CP_RISK_WEIGHT() {
		return R127_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR127_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r127_ORIGINAL_CP_RISK_WEIGHT) {
		R127_ORIGINAL_CP_RISK_WEIGHT = r127_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR127_RWA_NOT_COVERED() {
		return R127_RWA_NOT_COVERED;
	}
	public void setR127_RWA_NOT_COVERED(BigDecimal r127_RWA_NOT_COVERED) {
		R127_RWA_NOT_COVERED = r127_RWA_NOT_COVERED;
	}
	public BigDecimal getR127_COLLATERAL_CEA_ELIGIBLE() {
		return R127_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR127_COLLATERAL_CEA_ELIGIBLE(BigDecimal r127_COLLATERAL_CEA_ELIGIBLE) {
		R127_COLLATERAL_CEA_ELIGIBLE = r127_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR127_CEA_AFTER_VOL_ADJ() {
		return R127_CEA_AFTER_VOL_ADJ;
	}
	public void setR127_CEA_AFTER_VOL_ADJ(BigDecimal r127_CEA_AFTER_VOL_ADJ) {
		R127_CEA_AFTER_VOL_ADJ = r127_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR127_COLL_CASH() {
		return R127_COLL_CASH;
	}
	public void setR127_COLL_CASH(BigDecimal r127_COLL_CASH) {
		R127_COLL_CASH = r127_COLL_CASH;
	}
	public BigDecimal getR127_COLL_TBILLS() {
		return R127_COLL_TBILLS;
	}
	public void setR127_COLL_TBILLS(BigDecimal r127_COLL_TBILLS) {
		R127_COLL_TBILLS = r127_COLL_TBILLS;
	}
	public BigDecimal getR127_COLL_DEBT_SEC() {
		return R127_COLL_DEBT_SEC;
	}
	public void setR127_COLL_DEBT_SEC(BigDecimal r127_COLL_DEBT_SEC) {
		R127_COLL_DEBT_SEC = r127_COLL_DEBT_SEC;
	}
	public BigDecimal getR127_COLL_EQUITIES() {
		return R127_COLL_EQUITIES;
	}
	public void setR127_COLL_EQUITIES(BigDecimal r127_COLL_EQUITIES) {
		R127_COLL_EQUITIES = r127_COLL_EQUITIES;
	}
	public BigDecimal getR127_COLL_MUTUAL_FUNDS() {
		return R127_COLL_MUTUAL_FUNDS;
	}
	public void setR127_COLL_MUTUAL_FUNDS(BigDecimal r127_COLL_MUTUAL_FUNDS) {
		R127_COLL_MUTUAL_FUNDS = r127_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR127_COLL_TOTAL() {
		return R127_COLL_TOTAL;
	}
	public void setR127_COLL_TOTAL(BigDecimal r127_COLL_TOTAL) {
		R127_COLL_TOTAL = r127_COLL_TOTAL;
	}
	public BigDecimal getR127_CEA_AFTER_CRM() {
		return R127_CEA_AFTER_CRM;
	}
	public void setR127_CEA_AFTER_CRM(BigDecimal r127_CEA_AFTER_CRM) {
		R127_CEA_AFTER_CRM = r127_CEA_AFTER_CRM;
	}
	public BigDecimal getR127_RWA_CEA_NOT_COVERED() {
		return R127_RWA_CEA_NOT_COVERED;
	}
	public void setR127_RWA_CEA_NOT_COVERED(BigDecimal r127_RWA_CEA_NOT_COVERED) {
		R127_RWA_CEA_NOT_COVERED = r127_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR127_UNSECURED_CEA() {
		return R127_UNSECURED_CEA;
	}
	public void setR127_UNSECURED_CEA(BigDecimal r127_UNSECURED_CEA) {
		R127_UNSECURED_CEA = r127_UNSECURED_CEA;
	}
	public BigDecimal getR127_RWA_UNSECURED_CEA() {
		return R127_RWA_UNSECURED_CEA;
	}
	public void setR127_RWA_UNSECURED_CEA(BigDecimal r127_RWA_UNSECURED_CEA) {
		R127_RWA_UNSECURED_CEA = r127_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR128_NOMINAL_PRINCIPAL_AMT() {
		return R128_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR128_NOMINAL_PRINCIPAL_AMT(BigDecimal r128_NOMINAL_PRINCIPAL_AMT) {
		R128_NOMINAL_PRINCIPAL_AMT = r128_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR128_CCF_PCT() {
		return R128_CCF_PCT;
	}
	public void setR128_CCF_PCT(BigDecimal r128_CCF_PCT) {
		R128_CCF_PCT = r128_CCF_PCT;
	}
	public BigDecimal getR128_CREDIT_EQUIVALENT_AMT() {
		return R128_CREDIT_EQUIVALENT_AMT;
	}
	public void setR128_CREDIT_EQUIVALENT_AMT(BigDecimal r128_CREDIT_EQUIVALENT_AMT) {
		R128_CREDIT_EQUIVALENT_AMT = r128_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR128_CEA_ELIGIBLE_NETTING_CP() {
		return R128_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR128_CEA_ELIGIBLE_NETTING_CP(BigDecimal r128_CEA_ELIGIBLE_NETTING_CP) {
		R128_CEA_ELIGIBLE_NETTING_CP = r128_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR128_CEA_AFTER_NETTING() {
		return R128_CEA_AFTER_NETTING;
	}
	public void setR128_CEA_AFTER_NETTING(BigDecimal r128_CEA_AFTER_NETTING) {
		R128_CEA_AFTER_NETTING = r128_CEA_AFTER_NETTING;
	}
	public BigDecimal getR128_CEA_ELIGIBLE_CRM_SUB() {
		return R128_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR128_CEA_ELIGIBLE_CRM_SUB(BigDecimal r128_CEA_ELIGIBLE_CRM_SUB) {
		R128_CEA_ELIGIBLE_CRM_SUB = r128_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR128_GUARANTEE_ELIGIBLE() {
		return R128_GUARANTEE_ELIGIBLE;
	}
	public void setR128_GUARANTEE_ELIGIBLE(BigDecimal r128_GUARANTEE_ELIGIBLE) {
		R128_GUARANTEE_ELIGIBLE = r128_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR128_CREDIT_DERIVATIVES() {
		return R128_CREDIT_DERIVATIVES;
	}
	public void setR128_CREDIT_DERIVATIVES(BigDecimal r128_CREDIT_DERIVATIVES) {
		R128_CREDIT_DERIVATIVES = r128_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR128_EXPOSURE_COVERED_CRM() {
		return R128_EXPOSURE_COVERED_CRM;
	}
	public void setR128_EXPOSURE_COVERED_CRM(BigDecimal r128_EXPOSURE_COVERED_CRM) {
		R128_EXPOSURE_COVERED_CRM = r128_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR128_EXPOSURE_NOT_COVERED_CRM() {
		return R128_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR128_EXPOSURE_NOT_COVERED_CRM(BigDecimal r128_EXPOSURE_NOT_COVERED_CRM) {
		R128_EXPOSURE_NOT_COVERED_CRM = r128_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR128_RWA_TOTAL() {
		return R128_RWA_TOTAL;
	}
	public void setR128_RWA_TOTAL(BigDecimal r128_RWA_TOTAL) {
		R128_RWA_TOTAL = r128_RWA_TOTAL;
	}
	public BigDecimal getR128_CRM_RISK_WEIGHT() {
		return R128_CRM_RISK_WEIGHT;
	}
	public void setR128_CRM_RISK_WEIGHT(BigDecimal r128_CRM_RISK_WEIGHT) {
		R128_CRM_RISK_WEIGHT = r128_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR128_RWA_CRM_COVERED() {
		return R128_RWA_CRM_COVERED;
	}
	public void setR128_RWA_CRM_COVERED(BigDecimal r128_RWA_CRM_COVERED) {
		R128_RWA_CRM_COVERED = r128_RWA_CRM_COVERED;
	}
	public BigDecimal getR128_ORIGINAL_CP_RISK_WEIGHT() {
		return R128_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR128_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r128_ORIGINAL_CP_RISK_WEIGHT) {
		R128_ORIGINAL_CP_RISK_WEIGHT = r128_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR128_RWA_NOT_COVERED() {
		return R128_RWA_NOT_COVERED;
	}
	public void setR128_RWA_NOT_COVERED(BigDecimal r128_RWA_NOT_COVERED) {
		R128_RWA_NOT_COVERED = r128_RWA_NOT_COVERED;
	}
	public BigDecimal getR128_COLLATERAL_CEA_ELIGIBLE() {
		return R128_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR128_COLLATERAL_CEA_ELIGIBLE(BigDecimal r128_COLLATERAL_CEA_ELIGIBLE) {
		R128_COLLATERAL_CEA_ELIGIBLE = r128_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR128_CEA_AFTER_VOL_ADJ() {
		return R128_CEA_AFTER_VOL_ADJ;
	}
	public void setR128_CEA_AFTER_VOL_ADJ(BigDecimal r128_CEA_AFTER_VOL_ADJ) {
		R128_CEA_AFTER_VOL_ADJ = r128_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR128_COLL_CASH() {
		return R128_COLL_CASH;
	}
	public void setR128_COLL_CASH(BigDecimal r128_COLL_CASH) {
		R128_COLL_CASH = r128_COLL_CASH;
	}
	public BigDecimal getR128_COLL_TBILLS() {
		return R128_COLL_TBILLS;
	}
	public void setR128_COLL_TBILLS(BigDecimal r128_COLL_TBILLS) {
		R128_COLL_TBILLS = r128_COLL_TBILLS;
	}
	public BigDecimal getR128_COLL_DEBT_SEC() {
		return R128_COLL_DEBT_SEC;
	}
	public void setR128_COLL_DEBT_SEC(BigDecimal r128_COLL_DEBT_SEC) {
		R128_COLL_DEBT_SEC = r128_COLL_DEBT_SEC;
	}
	public BigDecimal getR128_COLL_EQUITIES() {
		return R128_COLL_EQUITIES;
	}
	public void setR128_COLL_EQUITIES(BigDecimal r128_COLL_EQUITIES) {
		R128_COLL_EQUITIES = r128_COLL_EQUITIES;
	}
	public BigDecimal getR128_COLL_MUTUAL_FUNDS() {
		return R128_COLL_MUTUAL_FUNDS;
	}
	public void setR128_COLL_MUTUAL_FUNDS(BigDecimal r128_COLL_MUTUAL_FUNDS) {
		R128_COLL_MUTUAL_FUNDS = r128_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR128_COLL_TOTAL() {
		return R128_COLL_TOTAL;
	}
	public void setR128_COLL_TOTAL(BigDecimal r128_COLL_TOTAL) {
		R128_COLL_TOTAL = r128_COLL_TOTAL;
	}
	public BigDecimal getR128_CEA_AFTER_CRM() {
		return R128_CEA_AFTER_CRM;
	}
	public void setR128_CEA_AFTER_CRM(BigDecimal r128_CEA_AFTER_CRM) {
		R128_CEA_AFTER_CRM = r128_CEA_AFTER_CRM;
	}
	public BigDecimal getR128_RWA_CEA_NOT_COVERED() {
		return R128_RWA_CEA_NOT_COVERED;
	}
	public void setR128_RWA_CEA_NOT_COVERED(BigDecimal r128_RWA_CEA_NOT_COVERED) {
		R128_RWA_CEA_NOT_COVERED = r128_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR128_UNSECURED_CEA() {
		return R128_UNSECURED_CEA;
	}
	public void setR128_UNSECURED_CEA(BigDecimal r128_UNSECURED_CEA) {
		R128_UNSECURED_CEA = r128_UNSECURED_CEA;
	}
	public BigDecimal getR128_RWA_UNSECURED_CEA() {
		return R128_RWA_UNSECURED_CEA;
	}
	public void setR128_RWA_UNSECURED_CEA(BigDecimal r128_RWA_UNSECURED_CEA) {
		R128_RWA_UNSECURED_CEA = r128_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR129_NOMINAL_PRINCIPAL_AMT() {
		return R129_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR129_NOMINAL_PRINCIPAL_AMT(BigDecimal r129_NOMINAL_PRINCIPAL_AMT) {
		R129_NOMINAL_PRINCIPAL_AMT = r129_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR129_CCF_PCT() {
		return R129_CCF_PCT;
	}
	public void setR129_CCF_PCT(BigDecimal r129_CCF_PCT) {
		R129_CCF_PCT = r129_CCF_PCT;
	}
	public BigDecimal getR129_CREDIT_EQUIVALENT_AMT() {
		return R129_CREDIT_EQUIVALENT_AMT;
	}
	public void setR129_CREDIT_EQUIVALENT_AMT(BigDecimal r129_CREDIT_EQUIVALENT_AMT) {
		R129_CREDIT_EQUIVALENT_AMT = r129_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR129_CEA_ELIGIBLE_NETTING_CP() {
		return R129_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR129_CEA_ELIGIBLE_NETTING_CP(BigDecimal r129_CEA_ELIGIBLE_NETTING_CP) {
		R129_CEA_ELIGIBLE_NETTING_CP = r129_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR129_CEA_AFTER_NETTING() {
		return R129_CEA_AFTER_NETTING;
	}
	public void setR129_CEA_AFTER_NETTING(BigDecimal r129_CEA_AFTER_NETTING) {
		R129_CEA_AFTER_NETTING = r129_CEA_AFTER_NETTING;
	}
	public BigDecimal getR129_CEA_ELIGIBLE_CRM_SUB() {
		return R129_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR129_CEA_ELIGIBLE_CRM_SUB(BigDecimal r129_CEA_ELIGIBLE_CRM_SUB) {
		R129_CEA_ELIGIBLE_CRM_SUB = r129_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR129_GUARANTEE_ELIGIBLE() {
		return R129_GUARANTEE_ELIGIBLE;
	}
	public void setR129_GUARANTEE_ELIGIBLE(BigDecimal r129_GUARANTEE_ELIGIBLE) {
		R129_GUARANTEE_ELIGIBLE = r129_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR129_CREDIT_DERIVATIVES() {
		return R129_CREDIT_DERIVATIVES;
	}
	public void setR129_CREDIT_DERIVATIVES(BigDecimal r129_CREDIT_DERIVATIVES) {
		R129_CREDIT_DERIVATIVES = r129_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR129_EXPOSURE_COVERED_CRM() {
		return R129_EXPOSURE_COVERED_CRM;
	}
	public void setR129_EXPOSURE_COVERED_CRM(BigDecimal r129_EXPOSURE_COVERED_CRM) {
		R129_EXPOSURE_COVERED_CRM = r129_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR129_EXPOSURE_NOT_COVERED_CRM() {
		return R129_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR129_EXPOSURE_NOT_COVERED_CRM(BigDecimal r129_EXPOSURE_NOT_COVERED_CRM) {
		R129_EXPOSURE_NOT_COVERED_CRM = r129_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR129_RWA_TOTAL() {
		return R129_RWA_TOTAL;
	}
	public void setR129_RWA_TOTAL(BigDecimal r129_RWA_TOTAL) {
		R129_RWA_TOTAL = r129_RWA_TOTAL;
	}
	public BigDecimal getR129_CRM_RISK_WEIGHT() {
		return R129_CRM_RISK_WEIGHT;
	}
	public void setR129_CRM_RISK_WEIGHT(BigDecimal r129_CRM_RISK_WEIGHT) {
		R129_CRM_RISK_WEIGHT = r129_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR129_RWA_CRM_COVERED() {
		return R129_RWA_CRM_COVERED;
	}
	public void setR129_RWA_CRM_COVERED(BigDecimal r129_RWA_CRM_COVERED) {
		R129_RWA_CRM_COVERED = r129_RWA_CRM_COVERED;
	}
	public BigDecimal getR129_ORIGINAL_CP_RISK_WEIGHT() {
		return R129_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR129_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r129_ORIGINAL_CP_RISK_WEIGHT) {
		R129_ORIGINAL_CP_RISK_WEIGHT = r129_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR129_RWA_NOT_COVERED() {
		return R129_RWA_NOT_COVERED;
	}
	public void setR129_RWA_NOT_COVERED(BigDecimal r129_RWA_NOT_COVERED) {
		R129_RWA_NOT_COVERED = r129_RWA_NOT_COVERED;
	}
	public BigDecimal getR129_COLLATERAL_CEA_ELIGIBLE() {
		return R129_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR129_COLLATERAL_CEA_ELIGIBLE(BigDecimal r129_COLLATERAL_CEA_ELIGIBLE) {
		R129_COLLATERAL_CEA_ELIGIBLE = r129_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR129_CEA_AFTER_VOL_ADJ() {
		return R129_CEA_AFTER_VOL_ADJ;
	}
	public void setR129_CEA_AFTER_VOL_ADJ(BigDecimal r129_CEA_AFTER_VOL_ADJ) {
		R129_CEA_AFTER_VOL_ADJ = r129_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR129_COLL_CASH() {
		return R129_COLL_CASH;
	}
	public void setR129_COLL_CASH(BigDecimal r129_COLL_CASH) {
		R129_COLL_CASH = r129_COLL_CASH;
	}
	public BigDecimal getR129_COLL_TBILLS() {
		return R129_COLL_TBILLS;
	}
	public void setR129_COLL_TBILLS(BigDecimal r129_COLL_TBILLS) {
		R129_COLL_TBILLS = r129_COLL_TBILLS;
	}
	public BigDecimal getR129_COLL_DEBT_SEC() {
		return R129_COLL_DEBT_SEC;
	}
	public void setR129_COLL_DEBT_SEC(BigDecimal r129_COLL_DEBT_SEC) {
		R129_COLL_DEBT_SEC = r129_COLL_DEBT_SEC;
	}
	public BigDecimal getR129_COLL_EQUITIES() {
		return R129_COLL_EQUITIES;
	}
	public void setR129_COLL_EQUITIES(BigDecimal r129_COLL_EQUITIES) {
		R129_COLL_EQUITIES = r129_COLL_EQUITIES;
	}
	public BigDecimal getR129_COLL_MUTUAL_FUNDS() {
		return R129_COLL_MUTUAL_FUNDS;
	}
	public void setR129_COLL_MUTUAL_FUNDS(BigDecimal r129_COLL_MUTUAL_FUNDS) {
		R129_COLL_MUTUAL_FUNDS = r129_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR129_COLL_TOTAL() {
		return R129_COLL_TOTAL;
	}
	public void setR129_COLL_TOTAL(BigDecimal r129_COLL_TOTAL) {
		R129_COLL_TOTAL = r129_COLL_TOTAL;
	}
	public BigDecimal getR129_CEA_AFTER_CRM() {
		return R129_CEA_AFTER_CRM;
	}
	public void setR129_CEA_AFTER_CRM(BigDecimal r129_CEA_AFTER_CRM) {
		R129_CEA_AFTER_CRM = r129_CEA_AFTER_CRM;
	}
	public BigDecimal getR129_RWA_CEA_NOT_COVERED() {
		return R129_RWA_CEA_NOT_COVERED;
	}
	public void setR129_RWA_CEA_NOT_COVERED(BigDecimal r129_RWA_CEA_NOT_COVERED) {
		R129_RWA_CEA_NOT_COVERED = r129_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR129_UNSECURED_CEA() {
		return R129_UNSECURED_CEA;
	}
	public void setR129_UNSECURED_CEA(BigDecimal r129_UNSECURED_CEA) {
		R129_UNSECURED_CEA = r129_UNSECURED_CEA;
	}
	public BigDecimal getR129_RWA_UNSECURED_CEA() {
		return R129_RWA_UNSECURED_CEA;
	}
	public void setR129_RWA_UNSECURED_CEA(BigDecimal r129_RWA_UNSECURED_CEA) {
		R129_RWA_UNSECURED_CEA = r129_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR130_NOMINAL_PRINCIPAL_AMT() {
		return R130_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR130_NOMINAL_PRINCIPAL_AMT(BigDecimal r130_NOMINAL_PRINCIPAL_AMT) {
		R130_NOMINAL_PRINCIPAL_AMT = r130_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR130_CCF_PCT() {
		return R130_CCF_PCT;
	}
	public void setR130_CCF_PCT(BigDecimal r130_CCF_PCT) {
		R130_CCF_PCT = r130_CCF_PCT;
	}
	public BigDecimal getR130_CREDIT_EQUIVALENT_AMT() {
		return R130_CREDIT_EQUIVALENT_AMT;
	}
	public void setR130_CREDIT_EQUIVALENT_AMT(BigDecimal r130_CREDIT_EQUIVALENT_AMT) {
		R130_CREDIT_EQUIVALENT_AMT = r130_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR130_CEA_ELIGIBLE_NETTING_CP() {
		return R130_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR130_CEA_ELIGIBLE_NETTING_CP(BigDecimal r130_CEA_ELIGIBLE_NETTING_CP) {
		R130_CEA_ELIGIBLE_NETTING_CP = r130_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR130_CEA_AFTER_NETTING() {
		return R130_CEA_AFTER_NETTING;
	}
	public void setR130_CEA_AFTER_NETTING(BigDecimal r130_CEA_AFTER_NETTING) {
		R130_CEA_AFTER_NETTING = r130_CEA_AFTER_NETTING;
	}
	public BigDecimal getR130_CEA_ELIGIBLE_CRM_SUB() {
		return R130_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR130_CEA_ELIGIBLE_CRM_SUB(BigDecimal r130_CEA_ELIGIBLE_CRM_SUB) {
		R130_CEA_ELIGIBLE_CRM_SUB = r130_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR130_GUARANTEE_ELIGIBLE() {
		return R130_GUARANTEE_ELIGIBLE;
	}
	public void setR130_GUARANTEE_ELIGIBLE(BigDecimal r130_GUARANTEE_ELIGIBLE) {
		R130_GUARANTEE_ELIGIBLE = r130_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR130_CREDIT_DERIVATIVES() {
		return R130_CREDIT_DERIVATIVES;
	}
	public void setR130_CREDIT_DERIVATIVES(BigDecimal r130_CREDIT_DERIVATIVES) {
		R130_CREDIT_DERIVATIVES = r130_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR130_EXPOSURE_COVERED_CRM() {
		return R130_EXPOSURE_COVERED_CRM;
	}
	public void setR130_EXPOSURE_COVERED_CRM(BigDecimal r130_EXPOSURE_COVERED_CRM) {
		R130_EXPOSURE_COVERED_CRM = r130_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR130_EXPOSURE_NOT_COVERED_CRM() {
		return R130_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR130_EXPOSURE_NOT_COVERED_CRM(BigDecimal r130_EXPOSURE_NOT_COVERED_CRM) {
		R130_EXPOSURE_NOT_COVERED_CRM = r130_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR130_RWA_TOTAL() {
		return R130_RWA_TOTAL;
	}
	public void setR130_RWA_TOTAL(BigDecimal r130_RWA_TOTAL) {
		R130_RWA_TOTAL = r130_RWA_TOTAL;
	}
	public BigDecimal getR130_CRM_RISK_WEIGHT() {
		return R130_CRM_RISK_WEIGHT;
	}
	public void setR130_CRM_RISK_WEIGHT(BigDecimal r130_CRM_RISK_WEIGHT) {
		R130_CRM_RISK_WEIGHT = r130_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR130_RWA_CRM_COVERED() {
		return R130_RWA_CRM_COVERED;
	}
	public void setR130_RWA_CRM_COVERED(BigDecimal r130_RWA_CRM_COVERED) {
		R130_RWA_CRM_COVERED = r130_RWA_CRM_COVERED;
	}
	public BigDecimal getR130_ORIGINAL_CP_RISK_WEIGHT() {
		return R130_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR130_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r130_ORIGINAL_CP_RISK_WEIGHT) {
		R130_ORIGINAL_CP_RISK_WEIGHT = r130_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR130_RWA_NOT_COVERED() {
		return R130_RWA_NOT_COVERED;
	}
	public void setR130_RWA_NOT_COVERED(BigDecimal r130_RWA_NOT_COVERED) {
		R130_RWA_NOT_COVERED = r130_RWA_NOT_COVERED;
	}
	public BigDecimal getR130_COLLATERAL_CEA_ELIGIBLE() {
		return R130_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR130_COLLATERAL_CEA_ELIGIBLE(BigDecimal r130_COLLATERAL_CEA_ELIGIBLE) {
		R130_COLLATERAL_CEA_ELIGIBLE = r130_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR130_CEA_AFTER_VOL_ADJ() {
		return R130_CEA_AFTER_VOL_ADJ;
	}
	public void setR130_CEA_AFTER_VOL_ADJ(BigDecimal r130_CEA_AFTER_VOL_ADJ) {
		R130_CEA_AFTER_VOL_ADJ = r130_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR130_COLL_CASH() {
		return R130_COLL_CASH;
	}
	public void setR130_COLL_CASH(BigDecimal r130_COLL_CASH) {
		R130_COLL_CASH = r130_COLL_CASH;
	}
	public BigDecimal getR130_COLL_TBILLS() {
		return R130_COLL_TBILLS;
	}
	public void setR130_COLL_TBILLS(BigDecimal r130_COLL_TBILLS) {
		R130_COLL_TBILLS = r130_COLL_TBILLS;
	}
	public BigDecimal getR130_COLL_DEBT_SEC() {
		return R130_COLL_DEBT_SEC;
	}
	public void setR130_COLL_DEBT_SEC(BigDecimal r130_COLL_DEBT_SEC) {
		R130_COLL_DEBT_SEC = r130_COLL_DEBT_SEC;
	}
	public BigDecimal getR130_COLL_EQUITIES() {
		return R130_COLL_EQUITIES;
	}
	public void setR130_COLL_EQUITIES(BigDecimal r130_COLL_EQUITIES) {
		R130_COLL_EQUITIES = r130_COLL_EQUITIES;
	}
	public BigDecimal getR130_COLL_MUTUAL_FUNDS() {
		return R130_COLL_MUTUAL_FUNDS;
	}
	public void setR130_COLL_MUTUAL_FUNDS(BigDecimal r130_COLL_MUTUAL_FUNDS) {
		R130_COLL_MUTUAL_FUNDS = r130_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR130_COLL_TOTAL() {
		return R130_COLL_TOTAL;
	}
	public void setR130_COLL_TOTAL(BigDecimal r130_COLL_TOTAL) {
		R130_COLL_TOTAL = r130_COLL_TOTAL;
	}
	public BigDecimal getR130_CEA_AFTER_CRM() {
		return R130_CEA_AFTER_CRM;
	}
	public void setR130_CEA_AFTER_CRM(BigDecimal r130_CEA_AFTER_CRM) {
		R130_CEA_AFTER_CRM = r130_CEA_AFTER_CRM;
	}
	public BigDecimal getR130_RWA_CEA_NOT_COVERED() {
		return R130_RWA_CEA_NOT_COVERED;
	}
	public void setR130_RWA_CEA_NOT_COVERED(BigDecimal r130_RWA_CEA_NOT_COVERED) {
		R130_RWA_CEA_NOT_COVERED = r130_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR130_UNSECURED_CEA() {
		return R130_UNSECURED_CEA;
	}
	public void setR130_UNSECURED_CEA(BigDecimal r130_UNSECURED_CEA) {
		R130_UNSECURED_CEA = r130_UNSECURED_CEA;
	}
	public BigDecimal getR130_RWA_UNSECURED_CEA() {
		return R130_RWA_UNSECURED_CEA;
	}
	public void setR130_RWA_UNSECURED_CEA(BigDecimal r130_RWA_UNSECURED_CEA) {
		R130_RWA_UNSECURED_CEA = r130_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR131_NOMINAL_PRINCIPAL_AMT() {
		return R131_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR131_NOMINAL_PRINCIPAL_AMT(BigDecimal r131_NOMINAL_PRINCIPAL_AMT) {
		R131_NOMINAL_PRINCIPAL_AMT = r131_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR131_CCF_PCT() {
		return R131_CCF_PCT;
	}
	public void setR131_CCF_PCT(BigDecimal r131_CCF_PCT) {
		R131_CCF_PCT = r131_CCF_PCT;
	}
	public BigDecimal getR131_CREDIT_EQUIVALENT_AMT() {
		return R131_CREDIT_EQUIVALENT_AMT;
	}
	public void setR131_CREDIT_EQUIVALENT_AMT(BigDecimal r131_CREDIT_EQUIVALENT_AMT) {
		R131_CREDIT_EQUIVALENT_AMT = r131_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR131_CEA_ELIGIBLE_NETTING_CP() {
		return R131_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR131_CEA_ELIGIBLE_NETTING_CP(BigDecimal r131_CEA_ELIGIBLE_NETTING_CP) {
		R131_CEA_ELIGIBLE_NETTING_CP = r131_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR131_CEA_AFTER_NETTING() {
		return R131_CEA_AFTER_NETTING;
	}
	public void setR131_CEA_AFTER_NETTING(BigDecimal r131_CEA_AFTER_NETTING) {
		R131_CEA_AFTER_NETTING = r131_CEA_AFTER_NETTING;
	}
	public BigDecimal getR131_CEA_ELIGIBLE_CRM_SUB() {
		return R131_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR131_CEA_ELIGIBLE_CRM_SUB(BigDecimal r131_CEA_ELIGIBLE_CRM_SUB) {
		R131_CEA_ELIGIBLE_CRM_SUB = r131_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR131_GUARANTEE_ELIGIBLE() {
		return R131_GUARANTEE_ELIGIBLE;
	}
	public void setR131_GUARANTEE_ELIGIBLE(BigDecimal r131_GUARANTEE_ELIGIBLE) {
		R131_GUARANTEE_ELIGIBLE = r131_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR131_CREDIT_DERIVATIVES() {
		return R131_CREDIT_DERIVATIVES;
	}
	public void setR131_CREDIT_DERIVATIVES(BigDecimal r131_CREDIT_DERIVATIVES) {
		R131_CREDIT_DERIVATIVES = r131_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR131_EXPOSURE_COVERED_CRM() {
		return R131_EXPOSURE_COVERED_CRM;
	}
	public void setR131_EXPOSURE_COVERED_CRM(BigDecimal r131_EXPOSURE_COVERED_CRM) {
		R131_EXPOSURE_COVERED_CRM = r131_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR131_EXPOSURE_NOT_COVERED_CRM() {
		return R131_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR131_EXPOSURE_NOT_COVERED_CRM(BigDecimal r131_EXPOSURE_NOT_COVERED_CRM) {
		R131_EXPOSURE_NOT_COVERED_CRM = r131_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR131_RWA_TOTAL() {
		return R131_RWA_TOTAL;
	}
	public void setR131_RWA_TOTAL(BigDecimal r131_RWA_TOTAL) {
		R131_RWA_TOTAL = r131_RWA_TOTAL;
	}
	public BigDecimal getR131_CRM_RISK_WEIGHT() {
		return R131_CRM_RISK_WEIGHT;
	}
	public void setR131_CRM_RISK_WEIGHT(BigDecimal r131_CRM_RISK_WEIGHT) {
		R131_CRM_RISK_WEIGHT = r131_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR131_RWA_CRM_COVERED() {
		return R131_RWA_CRM_COVERED;
	}
	public void setR131_RWA_CRM_COVERED(BigDecimal r131_RWA_CRM_COVERED) {
		R131_RWA_CRM_COVERED = r131_RWA_CRM_COVERED;
	}
	public BigDecimal getR131_ORIGINAL_CP_RISK_WEIGHT() {
		return R131_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR131_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r131_ORIGINAL_CP_RISK_WEIGHT) {
		R131_ORIGINAL_CP_RISK_WEIGHT = r131_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR131_RWA_NOT_COVERED() {
		return R131_RWA_NOT_COVERED;
	}
	public void setR131_RWA_NOT_COVERED(BigDecimal r131_RWA_NOT_COVERED) {
		R131_RWA_NOT_COVERED = r131_RWA_NOT_COVERED;
	}
	public BigDecimal getR131_COLLATERAL_CEA_ELIGIBLE() {
		return R131_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR131_COLLATERAL_CEA_ELIGIBLE(BigDecimal r131_COLLATERAL_CEA_ELIGIBLE) {
		R131_COLLATERAL_CEA_ELIGIBLE = r131_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR131_CEA_AFTER_VOL_ADJ() {
		return R131_CEA_AFTER_VOL_ADJ;
	}
	public void setR131_CEA_AFTER_VOL_ADJ(BigDecimal r131_CEA_AFTER_VOL_ADJ) {
		R131_CEA_AFTER_VOL_ADJ = r131_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR131_COLL_CASH() {
		return R131_COLL_CASH;
	}
	public void setR131_COLL_CASH(BigDecimal r131_COLL_CASH) {
		R131_COLL_CASH = r131_COLL_CASH;
	}
	public BigDecimal getR131_COLL_TBILLS() {
		return R131_COLL_TBILLS;
	}
	public void setR131_COLL_TBILLS(BigDecimal r131_COLL_TBILLS) {
		R131_COLL_TBILLS = r131_COLL_TBILLS;
	}
	public BigDecimal getR131_COLL_DEBT_SEC() {
		return R131_COLL_DEBT_SEC;
	}
	public void setR131_COLL_DEBT_SEC(BigDecimal r131_COLL_DEBT_SEC) {
		R131_COLL_DEBT_SEC = r131_COLL_DEBT_SEC;
	}
	public BigDecimal getR131_COLL_EQUITIES() {
		return R131_COLL_EQUITIES;
	}
	public void setR131_COLL_EQUITIES(BigDecimal r131_COLL_EQUITIES) {
		R131_COLL_EQUITIES = r131_COLL_EQUITIES;
	}
	public BigDecimal getR131_COLL_MUTUAL_FUNDS() {
		return R131_COLL_MUTUAL_FUNDS;
	}
	public void setR131_COLL_MUTUAL_FUNDS(BigDecimal r131_COLL_MUTUAL_FUNDS) {
		R131_COLL_MUTUAL_FUNDS = r131_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR131_COLL_TOTAL() {
		return R131_COLL_TOTAL;
	}
	public void setR131_COLL_TOTAL(BigDecimal r131_COLL_TOTAL) {
		R131_COLL_TOTAL = r131_COLL_TOTAL;
	}
	public BigDecimal getR131_CEA_AFTER_CRM() {
		return R131_CEA_AFTER_CRM;
	}
	public void setR131_CEA_AFTER_CRM(BigDecimal r131_CEA_AFTER_CRM) {
		R131_CEA_AFTER_CRM = r131_CEA_AFTER_CRM;
	}
	public BigDecimal getR131_RWA_CEA_NOT_COVERED() {
		return R131_RWA_CEA_NOT_COVERED;
	}
	public void setR131_RWA_CEA_NOT_COVERED(BigDecimal r131_RWA_CEA_NOT_COVERED) {
		R131_RWA_CEA_NOT_COVERED = r131_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR131_UNSECURED_CEA() {
		return R131_UNSECURED_CEA;
	}
	public void setR131_UNSECURED_CEA(BigDecimal r131_UNSECURED_CEA) {
		R131_UNSECURED_CEA = r131_UNSECURED_CEA;
	}
	public BigDecimal getR131_RWA_UNSECURED_CEA() {
		return R131_RWA_UNSECURED_CEA;
	}
	public void setR131_RWA_UNSECURED_CEA(BigDecimal r131_RWA_UNSECURED_CEA) {
		R131_RWA_UNSECURED_CEA = r131_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR132_NOMINAL_PRINCIPAL_AMT() {
		return R132_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR132_NOMINAL_PRINCIPAL_AMT(BigDecimal r132_NOMINAL_PRINCIPAL_AMT) {
		R132_NOMINAL_PRINCIPAL_AMT = r132_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR132_CCF_PCT() {
		return R132_CCF_PCT;
	}
	public void setR132_CCF_PCT(BigDecimal r132_CCF_PCT) {
		R132_CCF_PCT = r132_CCF_PCT;
	}
	public BigDecimal getR132_CREDIT_EQUIVALENT_AMT() {
		return R132_CREDIT_EQUIVALENT_AMT;
	}
	public void setR132_CREDIT_EQUIVALENT_AMT(BigDecimal r132_CREDIT_EQUIVALENT_AMT) {
		R132_CREDIT_EQUIVALENT_AMT = r132_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR132_CEA_ELIGIBLE_NETTING_CP() {
		return R132_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR132_CEA_ELIGIBLE_NETTING_CP(BigDecimal r132_CEA_ELIGIBLE_NETTING_CP) {
		R132_CEA_ELIGIBLE_NETTING_CP = r132_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR132_CEA_AFTER_NETTING() {
		return R132_CEA_AFTER_NETTING;
	}
	public void setR132_CEA_AFTER_NETTING(BigDecimal r132_CEA_AFTER_NETTING) {
		R132_CEA_AFTER_NETTING = r132_CEA_AFTER_NETTING;
	}
	public BigDecimal getR132_CEA_ELIGIBLE_CRM_SUB() {
		return R132_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR132_CEA_ELIGIBLE_CRM_SUB(BigDecimal r132_CEA_ELIGIBLE_CRM_SUB) {
		R132_CEA_ELIGIBLE_CRM_SUB = r132_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR132_GUARANTEE_ELIGIBLE() {
		return R132_GUARANTEE_ELIGIBLE;
	}
	public void setR132_GUARANTEE_ELIGIBLE(BigDecimal r132_GUARANTEE_ELIGIBLE) {
		R132_GUARANTEE_ELIGIBLE = r132_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR132_CREDIT_DERIVATIVES() {
		return R132_CREDIT_DERIVATIVES;
	}
	public void setR132_CREDIT_DERIVATIVES(BigDecimal r132_CREDIT_DERIVATIVES) {
		R132_CREDIT_DERIVATIVES = r132_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR132_EXPOSURE_COVERED_CRM() {
		return R132_EXPOSURE_COVERED_CRM;
	}
	public void setR132_EXPOSURE_COVERED_CRM(BigDecimal r132_EXPOSURE_COVERED_CRM) {
		R132_EXPOSURE_COVERED_CRM = r132_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR132_EXPOSURE_NOT_COVERED_CRM() {
		return R132_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR132_EXPOSURE_NOT_COVERED_CRM(BigDecimal r132_EXPOSURE_NOT_COVERED_CRM) {
		R132_EXPOSURE_NOT_COVERED_CRM = r132_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR132_RWA_TOTAL() {
		return R132_RWA_TOTAL;
	}
	public void setR132_RWA_TOTAL(BigDecimal r132_RWA_TOTAL) {
		R132_RWA_TOTAL = r132_RWA_TOTAL;
	}
	public BigDecimal getR132_CRM_RISK_WEIGHT() {
		return R132_CRM_RISK_WEIGHT;
	}
	public void setR132_CRM_RISK_WEIGHT(BigDecimal r132_CRM_RISK_WEIGHT) {
		R132_CRM_RISK_WEIGHT = r132_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR132_RWA_CRM_COVERED() {
		return R132_RWA_CRM_COVERED;
	}
	public void setR132_RWA_CRM_COVERED(BigDecimal r132_RWA_CRM_COVERED) {
		R132_RWA_CRM_COVERED = r132_RWA_CRM_COVERED;
	}
	public BigDecimal getR132_ORIGINAL_CP_RISK_WEIGHT() {
		return R132_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR132_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r132_ORIGINAL_CP_RISK_WEIGHT) {
		R132_ORIGINAL_CP_RISK_WEIGHT = r132_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR132_RWA_NOT_COVERED() {
		return R132_RWA_NOT_COVERED;
	}
	public void setR132_RWA_NOT_COVERED(BigDecimal r132_RWA_NOT_COVERED) {
		R132_RWA_NOT_COVERED = r132_RWA_NOT_COVERED;
	}
	public BigDecimal getR132_COLLATERAL_CEA_ELIGIBLE() {
		return R132_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR132_COLLATERAL_CEA_ELIGIBLE(BigDecimal r132_COLLATERAL_CEA_ELIGIBLE) {
		R132_COLLATERAL_CEA_ELIGIBLE = r132_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR132_CEA_AFTER_VOL_ADJ() {
		return R132_CEA_AFTER_VOL_ADJ;
	}
	public void setR132_CEA_AFTER_VOL_ADJ(BigDecimal r132_CEA_AFTER_VOL_ADJ) {
		R132_CEA_AFTER_VOL_ADJ = r132_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR132_COLL_CASH() {
		return R132_COLL_CASH;
	}
	public void setR132_COLL_CASH(BigDecimal r132_COLL_CASH) {
		R132_COLL_CASH = r132_COLL_CASH;
	}
	public BigDecimal getR132_COLL_TBILLS() {
		return R132_COLL_TBILLS;
	}
	public void setR132_COLL_TBILLS(BigDecimal r132_COLL_TBILLS) {
		R132_COLL_TBILLS = r132_COLL_TBILLS;
	}
	public BigDecimal getR132_COLL_DEBT_SEC() {
		return R132_COLL_DEBT_SEC;
	}
	public void setR132_COLL_DEBT_SEC(BigDecimal r132_COLL_DEBT_SEC) {
		R132_COLL_DEBT_SEC = r132_COLL_DEBT_SEC;
	}
	public BigDecimal getR132_COLL_EQUITIES() {
		return R132_COLL_EQUITIES;
	}
	public void setR132_COLL_EQUITIES(BigDecimal r132_COLL_EQUITIES) {
		R132_COLL_EQUITIES = r132_COLL_EQUITIES;
	}
	public BigDecimal getR132_COLL_MUTUAL_FUNDS() {
		return R132_COLL_MUTUAL_FUNDS;
	}
	public void setR132_COLL_MUTUAL_FUNDS(BigDecimal r132_COLL_MUTUAL_FUNDS) {
		R132_COLL_MUTUAL_FUNDS = r132_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR132_COLL_TOTAL() {
		return R132_COLL_TOTAL;
	}
	public void setR132_COLL_TOTAL(BigDecimal r132_COLL_TOTAL) {
		R132_COLL_TOTAL = r132_COLL_TOTAL;
	}
	public BigDecimal getR132_CEA_AFTER_CRM() {
		return R132_CEA_AFTER_CRM;
	}
	public void setR132_CEA_AFTER_CRM(BigDecimal r132_CEA_AFTER_CRM) {
		R132_CEA_AFTER_CRM = r132_CEA_AFTER_CRM;
	}
	public BigDecimal getR132_RWA_CEA_NOT_COVERED() {
		return R132_RWA_CEA_NOT_COVERED;
	}
	public void setR132_RWA_CEA_NOT_COVERED(BigDecimal r132_RWA_CEA_NOT_COVERED) {
		R132_RWA_CEA_NOT_COVERED = r132_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR132_UNSECURED_CEA() {
		return R132_UNSECURED_CEA;
	}
	public void setR132_UNSECURED_CEA(BigDecimal r132_UNSECURED_CEA) {
		R132_UNSECURED_CEA = r132_UNSECURED_CEA;
	}
	public BigDecimal getR132_RWA_UNSECURED_CEA() {
		return R132_RWA_UNSECURED_CEA;
	}
	public void setR132_RWA_UNSECURED_CEA(BigDecimal r132_RWA_UNSECURED_CEA) {
		R132_RWA_UNSECURED_CEA = r132_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR133_NOMINAL_PRINCIPAL_AMT() {
		return R133_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR133_NOMINAL_PRINCIPAL_AMT(BigDecimal r133_NOMINAL_PRINCIPAL_AMT) {
		R133_NOMINAL_PRINCIPAL_AMT = r133_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR133_CCF_PCT() {
		return R133_CCF_PCT;
	}
	public void setR133_CCF_PCT(BigDecimal r133_CCF_PCT) {
		R133_CCF_PCT = r133_CCF_PCT;
	}
	public BigDecimal getR133_CREDIT_EQUIVALENT_AMT() {
		return R133_CREDIT_EQUIVALENT_AMT;
	}
	public void setR133_CREDIT_EQUIVALENT_AMT(BigDecimal r133_CREDIT_EQUIVALENT_AMT) {
		R133_CREDIT_EQUIVALENT_AMT = r133_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR133_CEA_ELIGIBLE_NETTING_CP() {
		return R133_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR133_CEA_ELIGIBLE_NETTING_CP(BigDecimal r133_CEA_ELIGIBLE_NETTING_CP) {
		R133_CEA_ELIGIBLE_NETTING_CP = r133_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR133_CEA_AFTER_NETTING() {
		return R133_CEA_AFTER_NETTING;
	}
	public void setR133_CEA_AFTER_NETTING(BigDecimal r133_CEA_AFTER_NETTING) {
		R133_CEA_AFTER_NETTING = r133_CEA_AFTER_NETTING;
	}
	public BigDecimal getR133_CEA_ELIGIBLE_CRM_SUB() {
		return R133_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR133_CEA_ELIGIBLE_CRM_SUB(BigDecimal r133_CEA_ELIGIBLE_CRM_SUB) {
		R133_CEA_ELIGIBLE_CRM_SUB = r133_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR133_GUARANTEE_ELIGIBLE() {
		return R133_GUARANTEE_ELIGIBLE;
	}
	public void setR133_GUARANTEE_ELIGIBLE(BigDecimal r133_GUARANTEE_ELIGIBLE) {
		R133_GUARANTEE_ELIGIBLE = r133_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR133_CREDIT_DERIVATIVES() {
		return R133_CREDIT_DERIVATIVES;
	}
	public void setR133_CREDIT_DERIVATIVES(BigDecimal r133_CREDIT_DERIVATIVES) {
		R133_CREDIT_DERIVATIVES = r133_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR133_EXPOSURE_COVERED_CRM() {
		return R133_EXPOSURE_COVERED_CRM;
	}
	public void setR133_EXPOSURE_COVERED_CRM(BigDecimal r133_EXPOSURE_COVERED_CRM) {
		R133_EXPOSURE_COVERED_CRM = r133_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR133_EXPOSURE_NOT_COVERED_CRM() {
		return R133_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR133_EXPOSURE_NOT_COVERED_CRM(BigDecimal r133_EXPOSURE_NOT_COVERED_CRM) {
		R133_EXPOSURE_NOT_COVERED_CRM = r133_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR133_RWA_TOTAL() {
		return R133_RWA_TOTAL;
	}
	public void setR133_RWA_TOTAL(BigDecimal r133_RWA_TOTAL) {
		R133_RWA_TOTAL = r133_RWA_TOTAL;
	}
	public BigDecimal getR133_CRM_RISK_WEIGHT() {
		return R133_CRM_RISK_WEIGHT;
	}
	public void setR133_CRM_RISK_WEIGHT(BigDecimal r133_CRM_RISK_WEIGHT) {
		R133_CRM_RISK_WEIGHT = r133_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR133_RWA_CRM_COVERED() {
		return R133_RWA_CRM_COVERED;
	}
	public void setR133_RWA_CRM_COVERED(BigDecimal r133_RWA_CRM_COVERED) {
		R133_RWA_CRM_COVERED = r133_RWA_CRM_COVERED;
	}
	public BigDecimal getR133_ORIGINAL_CP_RISK_WEIGHT() {
		return R133_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR133_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r133_ORIGINAL_CP_RISK_WEIGHT) {
		R133_ORIGINAL_CP_RISK_WEIGHT = r133_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR133_RWA_NOT_COVERED() {
		return R133_RWA_NOT_COVERED;
	}
	public void setR133_RWA_NOT_COVERED(BigDecimal r133_RWA_NOT_COVERED) {
		R133_RWA_NOT_COVERED = r133_RWA_NOT_COVERED;
	}
	public BigDecimal getR133_COLLATERAL_CEA_ELIGIBLE() {
		return R133_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR133_COLLATERAL_CEA_ELIGIBLE(BigDecimal r133_COLLATERAL_CEA_ELIGIBLE) {
		R133_COLLATERAL_CEA_ELIGIBLE = r133_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR133_CEA_AFTER_VOL_ADJ() {
		return R133_CEA_AFTER_VOL_ADJ;
	}
	public void setR133_CEA_AFTER_VOL_ADJ(BigDecimal r133_CEA_AFTER_VOL_ADJ) {
		R133_CEA_AFTER_VOL_ADJ = r133_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR133_COLL_CASH() {
		return R133_COLL_CASH;
	}
	public void setR133_COLL_CASH(BigDecimal r133_COLL_CASH) {
		R133_COLL_CASH = r133_COLL_CASH;
	}
	public BigDecimal getR133_COLL_TBILLS() {
		return R133_COLL_TBILLS;
	}
	public void setR133_COLL_TBILLS(BigDecimal r133_COLL_TBILLS) {
		R133_COLL_TBILLS = r133_COLL_TBILLS;
	}
	public BigDecimal getR133_COLL_DEBT_SEC() {
		return R133_COLL_DEBT_SEC;
	}
	public void setR133_COLL_DEBT_SEC(BigDecimal r133_COLL_DEBT_SEC) {
		R133_COLL_DEBT_SEC = r133_COLL_DEBT_SEC;
	}
	public BigDecimal getR133_COLL_EQUITIES() {
		return R133_COLL_EQUITIES;
	}
	public void setR133_COLL_EQUITIES(BigDecimal r133_COLL_EQUITIES) {
		R133_COLL_EQUITIES = r133_COLL_EQUITIES;
	}
	public BigDecimal getR133_COLL_MUTUAL_FUNDS() {
		return R133_COLL_MUTUAL_FUNDS;
	}
	public void setR133_COLL_MUTUAL_FUNDS(BigDecimal r133_COLL_MUTUAL_FUNDS) {
		R133_COLL_MUTUAL_FUNDS = r133_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR133_COLL_TOTAL() {
		return R133_COLL_TOTAL;
	}
	public void setR133_COLL_TOTAL(BigDecimal r133_COLL_TOTAL) {
		R133_COLL_TOTAL = r133_COLL_TOTAL;
	}
	public BigDecimal getR133_CEA_AFTER_CRM() {
		return R133_CEA_AFTER_CRM;
	}
	public void setR133_CEA_AFTER_CRM(BigDecimal r133_CEA_AFTER_CRM) {
		R133_CEA_AFTER_CRM = r133_CEA_AFTER_CRM;
	}
	public BigDecimal getR133_RWA_CEA_NOT_COVERED() {
		return R133_RWA_CEA_NOT_COVERED;
	}
	public void setR133_RWA_CEA_NOT_COVERED(BigDecimal r133_RWA_CEA_NOT_COVERED) {
		R133_RWA_CEA_NOT_COVERED = r133_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR133_UNSECURED_CEA() {
		return R133_UNSECURED_CEA;
	}
	public void setR133_UNSECURED_CEA(BigDecimal r133_UNSECURED_CEA) {
		R133_UNSECURED_CEA = r133_UNSECURED_CEA;
	}
	public BigDecimal getR133_RWA_UNSECURED_CEA() {
		return R133_RWA_UNSECURED_CEA;
	}
	public void setR133_RWA_UNSECURED_CEA(BigDecimal r133_RWA_UNSECURED_CEA) {
		R133_RWA_UNSECURED_CEA = r133_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR134_NOMINAL_PRINCIPAL_AMT() {
		return R134_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR134_NOMINAL_PRINCIPAL_AMT(BigDecimal r134_NOMINAL_PRINCIPAL_AMT) {
		R134_NOMINAL_PRINCIPAL_AMT = r134_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR134_CCF_PCT() {
		return R134_CCF_PCT;
	}
	public void setR134_CCF_PCT(BigDecimal r134_CCF_PCT) {
		R134_CCF_PCT = r134_CCF_PCT;
	}
	public BigDecimal getR134_CREDIT_EQUIVALENT_AMT() {
		return R134_CREDIT_EQUIVALENT_AMT;
	}
	public void setR134_CREDIT_EQUIVALENT_AMT(BigDecimal r134_CREDIT_EQUIVALENT_AMT) {
		R134_CREDIT_EQUIVALENT_AMT = r134_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR134_CEA_ELIGIBLE_NETTING_CP() {
		return R134_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR134_CEA_ELIGIBLE_NETTING_CP(BigDecimal r134_CEA_ELIGIBLE_NETTING_CP) {
		R134_CEA_ELIGIBLE_NETTING_CP = r134_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR134_CEA_AFTER_NETTING() {
		return R134_CEA_AFTER_NETTING;
	}
	public void setR134_CEA_AFTER_NETTING(BigDecimal r134_CEA_AFTER_NETTING) {
		R134_CEA_AFTER_NETTING = r134_CEA_AFTER_NETTING;
	}
	public BigDecimal getR134_CEA_ELIGIBLE_CRM_SUB() {
		return R134_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR134_CEA_ELIGIBLE_CRM_SUB(BigDecimal r134_CEA_ELIGIBLE_CRM_SUB) {
		R134_CEA_ELIGIBLE_CRM_SUB = r134_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR134_GUARANTEE_ELIGIBLE() {
		return R134_GUARANTEE_ELIGIBLE;
	}
	public void setR134_GUARANTEE_ELIGIBLE(BigDecimal r134_GUARANTEE_ELIGIBLE) {
		R134_GUARANTEE_ELIGIBLE = r134_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR134_CREDIT_DERIVATIVES() {
		return R134_CREDIT_DERIVATIVES;
	}
	public void setR134_CREDIT_DERIVATIVES(BigDecimal r134_CREDIT_DERIVATIVES) {
		R134_CREDIT_DERIVATIVES = r134_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR134_EXPOSURE_COVERED_CRM() {
		return R134_EXPOSURE_COVERED_CRM;
	}
	public void setR134_EXPOSURE_COVERED_CRM(BigDecimal r134_EXPOSURE_COVERED_CRM) {
		R134_EXPOSURE_COVERED_CRM = r134_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR134_EXPOSURE_NOT_COVERED_CRM() {
		return R134_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR134_EXPOSURE_NOT_COVERED_CRM(BigDecimal r134_EXPOSURE_NOT_COVERED_CRM) {
		R134_EXPOSURE_NOT_COVERED_CRM = r134_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR134_RWA_TOTAL() {
		return R134_RWA_TOTAL;
	}
	public void setR134_RWA_TOTAL(BigDecimal r134_RWA_TOTAL) {
		R134_RWA_TOTAL = r134_RWA_TOTAL;
	}
	public BigDecimal getR134_CRM_RISK_WEIGHT() {
		return R134_CRM_RISK_WEIGHT;
	}
	public void setR134_CRM_RISK_WEIGHT(BigDecimal r134_CRM_RISK_WEIGHT) {
		R134_CRM_RISK_WEIGHT = r134_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR134_RWA_CRM_COVERED() {
		return R134_RWA_CRM_COVERED;
	}
	public void setR134_RWA_CRM_COVERED(BigDecimal r134_RWA_CRM_COVERED) {
		R134_RWA_CRM_COVERED = r134_RWA_CRM_COVERED;
	}
	public BigDecimal getR134_ORIGINAL_CP_RISK_WEIGHT() {
		return R134_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR134_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r134_ORIGINAL_CP_RISK_WEIGHT) {
		R134_ORIGINAL_CP_RISK_WEIGHT = r134_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR134_RWA_NOT_COVERED() {
		return R134_RWA_NOT_COVERED;
	}
	public void setR134_RWA_NOT_COVERED(BigDecimal r134_RWA_NOT_COVERED) {
		R134_RWA_NOT_COVERED = r134_RWA_NOT_COVERED;
	}
	public BigDecimal getR134_COLLATERAL_CEA_ELIGIBLE() {
		return R134_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR134_COLLATERAL_CEA_ELIGIBLE(BigDecimal r134_COLLATERAL_CEA_ELIGIBLE) {
		R134_COLLATERAL_CEA_ELIGIBLE = r134_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR134_CEA_AFTER_VOL_ADJ() {
		return R134_CEA_AFTER_VOL_ADJ;
	}
	public void setR134_CEA_AFTER_VOL_ADJ(BigDecimal r134_CEA_AFTER_VOL_ADJ) {
		R134_CEA_AFTER_VOL_ADJ = r134_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR134_COLL_CASH() {
		return R134_COLL_CASH;
	}
	public void setR134_COLL_CASH(BigDecimal r134_COLL_CASH) {
		R134_COLL_CASH = r134_COLL_CASH;
	}
	public BigDecimal getR134_COLL_TBILLS() {
		return R134_COLL_TBILLS;
	}
	public void setR134_COLL_TBILLS(BigDecimal r134_COLL_TBILLS) {
		R134_COLL_TBILLS = r134_COLL_TBILLS;
	}
	public BigDecimal getR134_COLL_DEBT_SEC() {
		return R134_COLL_DEBT_SEC;
	}
	public void setR134_COLL_DEBT_SEC(BigDecimal r134_COLL_DEBT_SEC) {
		R134_COLL_DEBT_SEC = r134_COLL_DEBT_SEC;
	}
	public BigDecimal getR134_COLL_EQUITIES() {
		return R134_COLL_EQUITIES;
	}
	public void setR134_COLL_EQUITIES(BigDecimal r134_COLL_EQUITIES) {
		R134_COLL_EQUITIES = r134_COLL_EQUITIES;
	}
	public BigDecimal getR134_COLL_MUTUAL_FUNDS() {
		return R134_COLL_MUTUAL_FUNDS;
	}
	public void setR134_COLL_MUTUAL_FUNDS(BigDecimal r134_COLL_MUTUAL_FUNDS) {
		R134_COLL_MUTUAL_FUNDS = r134_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR134_COLL_TOTAL() {
		return R134_COLL_TOTAL;
	}
	public void setR134_COLL_TOTAL(BigDecimal r134_COLL_TOTAL) {
		R134_COLL_TOTAL = r134_COLL_TOTAL;
	}
	public BigDecimal getR134_CEA_AFTER_CRM() {
		return R134_CEA_AFTER_CRM;
	}
	public void setR134_CEA_AFTER_CRM(BigDecimal r134_CEA_AFTER_CRM) {
		R134_CEA_AFTER_CRM = r134_CEA_AFTER_CRM;
	}
	public BigDecimal getR134_RWA_CEA_NOT_COVERED() {
		return R134_RWA_CEA_NOT_COVERED;
	}
	public void setR134_RWA_CEA_NOT_COVERED(BigDecimal r134_RWA_CEA_NOT_COVERED) {
		R134_RWA_CEA_NOT_COVERED = r134_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR134_UNSECURED_CEA() {
		return R134_UNSECURED_CEA;
	}
	public void setR134_UNSECURED_CEA(BigDecimal r134_UNSECURED_CEA) {
		R134_UNSECURED_CEA = r134_UNSECURED_CEA;
	}
	public BigDecimal getR134_RWA_UNSECURED_CEA() {
		return R134_RWA_UNSECURED_CEA;
	}
	public void setR134_RWA_UNSECURED_CEA(BigDecimal r134_RWA_UNSECURED_CEA) {
		R134_RWA_UNSECURED_CEA = r134_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR135_NOMINAL_PRINCIPAL_AMT() {
		return R135_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR135_NOMINAL_PRINCIPAL_AMT(BigDecimal r135_NOMINAL_PRINCIPAL_AMT) {
		R135_NOMINAL_PRINCIPAL_AMT = r135_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR135_CCF_PCT() {
		return R135_CCF_PCT;
	}
	public void setR135_CCF_PCT(BigDecimal r135_CCF_PCT) {
		R135_CCF_PCT = r135_CCF_PCT;
	}
	public BigDecimal getR135_CREDIT_EQUIVALENT_AMT() {
		return R135_CREDIT_EQUIVALENT_AMT;
	}
	public void setR135_CREDIT_EQUIVALENT_AMT(BigDecimal r135_CREDIT_EQUIVALENT_AMT) {
		R135_CREDIT_EQUIVALENT_AMT = r135_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR135_CEA_ELIGIBLE_NETTING_CP() {
		return R135_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR135_CEA_ELIGIBLE_NETTING_CP(BigDecimal r135_CEA_ELIGIBLE_NETTING_CP) {
		R135_CEA_ELIGIBLE_NETTING_CP = r135_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR135_CEA_AFTER_NETTING() {
		return R135_CEA_AFTER_NETTING;
	}
	public void setR135_CEA_AFTER_NETTING(BigDecimal r135_CEA_AFTER_NETTING) {
		R135_CEA_AFTER_NETTING = r135_CEA_AFTER_NETTING;
	}
	public BigDecimal getR135_CEA_ELIGIBLE_CRM_SUB() {
		return R135_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR135_CEA_ELIGIBLE_CRM_SUB(BigDecimal r135_CEA_ELIGIBLE_CRM_SUB) {
		R135_CEA_ELIGIBLE_CRM_SUB = r135_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR135_GUARANTEE_ELIGIBLE() {
		return R135_GUARANTEE_ELIGIBLE;
	}
	public void setR135_GUARANTEE_ELIGIBLE(BigDecimal r135_GUARANTEE_ELIGIBLE) {
		R135_GUARANTEE_ELIGIBLE = r135_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR135_CREDIT_DERIVATIVES() {
		return R135_CREDIT_DERIVATIVES;
	}
	public void setR135_CREDIT_DERIVATIVES(BigDecimal r135_CREDIT_DERIVATIVES) {
		R135_CREDIT_DERIVATIVES = r135_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR135_EXPOSURE_COVERED_CRM() {
		return R135_EXPOSURE_COVERED_CRM;
	}
	public void setR135_EXPOSURE_COVERED_CRM(BigDecimal r135_EXPOSURE_COVERED_CRM) {
		R135_EXPOSURE_COVERED_CRM = r135_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR135_EXPOSURE_NOT_COVERED_CRM() {
		return R135_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR135_EXPOSURE_NOT_COVERED_CRM(BigDecimal r135_EXPOSURE_NOT_COVERED_CRM) {
		R135_EXPOSURE_NOT_COVERED_CRM = r135_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR135_RWA_TOTAL() {
		return R135_RWA_TOTAL;
	}
	public void setR135_RWA_TOTAL(BigDecimal r135_RWA_TOTAL) {
		R135_RWA_TOTAL = r135_RWA_TOTAL;
	}
	public BigDecimal getR135_CRM_RISK_WEIGHT() {
		return R135_CRM_RISK_WEIGHT;
	}
	public void setR135_CRM_RISK_WEIGHT(BigDecimal r135_CRM_RISK_WEIGHT) {
		R135_CRM_RISK_WEIGHT = r135_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR135_RWA_CRM_COVERED() {
		return R135_RWA_CRM_COVERED;
	}
	public void setR135_RWA_CRM_COVERED(BigDecimal r135_RWA_CRM_COVERED) {
		R135_RWA_CRM_COVERED = r135_RWA_CRM_COVERED;
	}
	public BigDecimal getR135_ORIGINAL_CP_RISK_WEIGHT() {
		return R135_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR135_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r135_ORIGINAL_CP_RISK_WEIGHT) {
		R135_ORIGINAL_CP_RISK_WEIGHT = r135_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR135_RWA_NOT_COVERED() {
		return R135_RWA_NOT_COVERED;
	}
	public void setR135_RWA_NOT_COVERED(BigDecimal r135_RWA_NOT_COVERED) {
		R135_RWA_NOT_COVERED = r135_RWA_NOT_COVERED;
	}
	public BigDecimal getR135_COLLATERAL_CEA_ELIGIBLE() {
		return R135_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR135_COLLATERAL_CEA_ELIGIBLE(BigDecimal r135_COLLATERAL_CEA_ELIGIBLE) {
		R135_COLLATERAL_CEA_ELIGIBLE = r135_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR135_CEA_AFTER_VOL_ADJ() {
		return R135_CEA_AFTER_VOL_ADJ;
	}
	public void setR135_CEA_AFTER_VOL_ADJ(BigDecimal r135_CEA_AFTER_VOL_ADJ) {
		R135_CEA_AFTER_VOL_ADJ = r135_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR135_COLL_CASH() {
		return R135_COLL_CASH;
	}
	public void setR135_COLL_CASH(BigDecimal r135_COLL_CASH) {
		R135_COLL_CASH = r135_COLL_CASH;
	}
	public BigDecimal getR135_COLL_TBILLS() {
		return R135_COLL_TBILLS;
	}
	public void setR135_COLL_TBILLS(BigDecimal r135_COLL_TBILLS) {
		R135_COLL_TBILLS = r135_COLL_TBILLS;
	}
	public BigDecimal getR135_COLL_DEBT_SEC() {
		return R135_COLL_DEBT_SEC;
	}
	public void setR135_COLL_DEBT_SEC(BigDecimal r135_COLL_DEBT_SEC) {
		R135_COLL_DEBT_SEC = r135_COLL_DEBT_SEC;
	}
	public BigDecimal getR135_COLL_EQUITIES() {
		return R135_COLL_EQUITIES;
	}
	public void setR135_COLL_EQUITIES(BigDecimal r135_COLL_EQUITIES) {
		R135_COLL_EQUITIES = r135_COLL_EQUITIES;
	}
	public BigDecimal getR135_COLL_MUTUAL_FUNDS() {
		return R135_COLL_MUTUAL_FUNDS;
	}
	public void setR135_COLL_MUTUAL_FUNDS(BigDecimal r135_COLL_MUTUAL_FUNDS) {
		R135_COLL_MUTUAL_FUNDS = r135_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR135_COLL_TOTAL() {
		return R135_COLL_TOTAL;
	}
	public void setR135_COLL_TOTAL(BigDecimal r135_COLL_TOTAL) {
		R135_COLL_TOTAL = r135_COLL_TOTAL;
	}
	public BigDecimal getR135_CEA_AFTER_CRM() {
		return R135_CEA_AFTER_CRM;
	}
	public void setR135_CEA_AFTER_CRM(BigDecimal r135_CEA_AFTER_CRM) {
		R135_CEA_AFTER_CRM = r135_CEA_AFTER_CRM;
	}
	public BigDecimal getR135_RWA_CEA_NOT_COVERED() {
		return R135_RWA_CEA_NOT_COVERED;
	}
	public void setR135_RWA_CEA_NOT_COVERED(BigDecimal r135_RWA_CEA_NOT_COVERED) {
		R135_RWA_CEA_NOT_COVERED = r135_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR135_UNSECURED_CEA() {
		return R135_UNSECURED_CEA;
	}
	public void setR135_UNSECURED_CEA(BigDecimal r135_UNSECURED_CEA) {
		R135_UNSECURED_CEA = r135_UNSECURED_CEA;
	}
	public BigDecimal getR135_RWA_UNSECURED_CEA() {
		return R135_RWA_UNSECURED_CEA;
	}
	public void setR135_RWA_UNSECURED_CEA(BigDecimal r135_RWA_UNSECURED_CEA) {
		R135_RWA_UNSECURED_CEA = r135_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR136_NOMINAL_PRINCIPAL_AMT() {
		return R136_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR136_NOMINAL_PRINCIPAL_AMT(BigDecimal r136_NOMINAL_PRINCIPAL_AMT) {
		R136_NOMINAL_PRINCIPAL_AMT = r136_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR136_CCF_PCT() {
		return R136_CCF_PCT;
	}
	public void setR136_CCF_PCT(BigDecimal r136_CCF_PCT) {
		R136_CCF_PCT = r136_CCF_PCT;
	}
	public BigDecimal getR136_CREDIT_EQUIVALENT_AMT() {
		return R136_CREDIT_EQUIVALENT_AMT;
	}
	public void setR136_CREDIT_EQUIVALENT_AMT(BigDecimal r136_CREDIT_EQUIVALENT_AMT) {
		R136_CREDIT_EQUIVALENT_AMT = r136_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR136_CEA_ELIGIBLE_NETTING_CP() {
		return R136_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR136_CEA_ELIGIBLE_NETTING_CP(BigDecimal r136_CEA_ELIGIBLE_NETTING_CP) {
		R136_CEA_ELIGIBLE_NETTING_CP = r136_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR136_CEA_AFTER_NETTING() {
		return R136_CEA_AFTER_NETTING;
	}
	public void setR136_CEA_AFTER_NETTING(BigDecimal r136_CEA_AFTER_NETTING) {
		R136_CEA_AFTER_NETTING = r136_CEA_AFTER_NETTING;
	}
	public BigDecimal getR136_CEA_ELIGIBLE_CRM_SUB() {
		return R136_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR136_CEA_ELIGIBLE_CRM_SUB(BigDecimal r136_CEA_ELIGIBLE_CRM_SUB) {
		R136_CEA_ELIGIBLE_CRM_SUB = r136_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR136_GUARANTEE_ELIGIBLE() {
		return R136_GUARANTEE_ELIGIBLE;
	}
	public void setR136_GUARANTEE_ELIGIBLE(BigDecimal r136_GUARANTEE_ELIGIBLE) {
		R136_GUARANTEE_ELIGIBLE = r136_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR136_CREDIT_DERIVATIVES() {
		return R136_CREDIT_DERIVATIVES;
	}
	public void setR136_CREDIT_DERIVATIVES(BigDecimal r136_CREDIT_DERIVATIVES) {
		R136_CREDIT_DERIVATIVES = r136_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR136_EXPOSURE_COVERED_CRM() {
		return R136_EXPOSURE_COVERED_CRM;
	}
	public void setR136_EXPOSURE_COVERED_CRM(BigDecimal r136_EXPOSURE_COVERED_CRM) {
		R136_EXPOSURE_COVERED_CRM = r136_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR136_EXPOSURE_NOT_COVERED_CRM() {
		return R136_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR136_EXPOSURE_NOT_COVERED_CRM(BigDecimal r136_EXPOSURE_NOT_COVERED_CRM) {
		R136_EXPOSURE_NOT_COVERED_CRM = r136_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR136_RWA_TOTAL() {
		return R136_RWA_TOTAL;
	}
	public void setR136_RWA_TOTAL(BigDecimal r136_RWA_TOTAL) {
		R136_RWA_TOTAL = r136_RWA_TOTAL;
	}
	public BigDecimal getR136_CRM_RISK_WEIGHT() {
		return R136_CRM_RISK_WEIGHT;
	}
	public void setR136_CRM_RISK_WEIGHT(BigDecimal r136_CRM_RISK_WEIGHT) {
		R136_CRM_RISK_WEIGHT = r136_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR136_RWA_CRM_COVERED() {
		return R136_RWA_CRM_COVERED;
	}
	public void setR136_RWA_CRM_COVERED(BigDecimal r136_RWA_CRM_COVERED) {
		R136_RWA_CRM_COVERED = r136_RWA_CRM_COVERED;
	}
	public BigDecimal getR136_ORIGINAL_CP_RISK_WEIGHT() {
		return R136_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR136_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r136_ORIGINAL_CP_RISK_WEIGHT) {
		R136_ORIGINAL_CP_RISK_WEIGHT = r136_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR136_RWA_NOT_COVERED() {
		return R136_RWA_NOT_COVERED;
	}
	public void setR136_RWA_NOT_COVERED(BigDecimal r136_RWA_NOT_COVERED) {
		R136_RWA_NOT_COVERED = r136_RWA_NOT_COVERED;
	}
	public BigDecimal getR136_COLLATERAL_CEA_ELIGIBLE() {
		return R136_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR136_COLLATERAL_CEA_ELIGIBLE(BigDecimal r136_COLLATERAL_CEA_ELIGIBLE) {
		R136_COLLATERAL_CEA_ELIGIBLE = r136_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR136_CEA_AFTER_VOL_ADJ() {
		return R136_CEA_AFTER_VOL_ADJ;
	}
	public void setR136_CEA_AFTER_VOL_ADJ(BigDecimal r136_CEA_AFTER_VOL_ADJ) {
		R136_CEA_AFTER_VOL_ADJ = r136_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR136_COLL_CASH() {
		return R136_COLL_CASH;
	}
	public void setR136_COLL_CASH(BigDecimal r136_COLL_CASH) {
		R136_COLL_CASH = r136_COLL_CASH;
	}
	public BigDecimal getR136_COLL_TBILLS() {
		return R136_COLL_TBILLS;
	}
	public void setR136_COLL_TBILLS(BigDecimal r136_COLL_TBILLS) {
		R136_COLL_TBILLS = r136_COLL_TBILLS;
	}
	public BigDecimal getR136_COLL_DEBT_SEC() {
		return R136_COLL_DEBT_SEC;
	}
	public void setR136_COLL_DEBT_SEC(BigDecimal r136_COLL_DEBT_SEC) {
		R136_COLL_DEBT_SEC = r136_COLL_DEBT_SEC;
	}
	public BigDecimal getR136_COLL_EQUITIES() {
		return R136_COLL_EQUITIES;
	}
	public void setR136_COLL_EQUITIES(BigDecimal r136_COLL_EQUITIES) {
		R136_COLL_EQUITIES = r136_COLL_EQUITIES;
	}
	public BigDecimal getR136_COLL_MUTUAL_FUNDS() {
		return R136_COLL_MUTUAL_FUNDS;
	}
	public void setR136_COLL_MUTUAL_FUNDS(BigDecimal r136_COLL_MUTUAL_FUNDS) {
		R136_COLL_MUTUAL_FUNDS = r136_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR136_COLL_TOTAL() {
		return R136_COLL_TOTAL;
	}
	public void setR136_COLL_TOTAL(BigDecimal r136_COLL_TOTAL) {
		R136_COLL_TOTAL = r136_COLL_TOTAL;
	}
	public BigDecimal getR136_CEA_AFTER_CRM() {
		return R136_CEA_AFTER_CRM;
	}
	public void setR136_CEA_AFTER_CRM(BigDecimal r136_CEA_AFTER_CRM) {
		R136_CEA_AFTER_CRM = r136_CEA_AFTER_CRM;
	}
	public BigDecimal getR136_RWA_CEA_NOT_COVERED() {
		return R136_RWA_CEA_NOT_COVERED;
	}
	public void setR136_RWA_CEA_NOT_COVERED(BigDecimal r136_RWA_CEA_NOT_COVERED) {
		R136_RWA_CEA_NOT_COVERED = r136_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR136_UNSECURED_CEA() {
		return R136_UNSECURED_CEA;
	}
	public void setR136_UNSECURED_CEA(BigDecimal r136_UNSECURED_CEA) {
		R136_UNSECURED_CEA = r136_UNSECURED_CEA;
	}
	public BigDecimal getR136_RWA_UNSECURED_CEA() {
		return R136_RWA_UNSECURED_CEA;
	}
	public void setR136_RWA_UNSECURED_CEA(BigDecimal r136_RWA_UNSECURED_CEA) {
		R136_RWA_UNSECURED_CEA = r136_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR137_NOMINAL_PRINCIPAL_AMT() {
		return R137_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR137_NOMINAL_PRINCIPAL_AMT(BigDecimal r137_NOMINAL_PRINCIPAL_AMT) {
		R137_NOMINAL_PRINCIPAL_AMT = r137_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR137_CCF_PCT() {
		return R137_CCF_PCT;
	}
	public void setR137_CCF_PCT(BigDecimal r137_CCF_PCT) {
		R137_CCF_PCT = r137_CCF_PCT;
	}
	public BigDecimal getR137_CREDIT_EQUIVALENT_AMT() {
		return R137_CREDIT_EQUIVALENT_AMT;
	}
	public void setR137_CREDIT_EQUIVALENT_AMT(BigDecimal r137_CREDIT_EQUIVALENT_AMT) {
		R137_CREDIT_EQUIVALENT_AMT = r137_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR137_CEA_ELIGIBLE_NETTING_CP() {
		return R137_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR137_CEA_ELIGIBLE_NETTING_CP(BigDecimal r137_CEA_ELIGIBLE_NETTING_CP) {
		R137_CEA_ELIGIBLE_NETTING_CP = r137_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR137_CEA_AFTER_NETTING() {
		return R137_CEA_AFTER_NETTING;
	}
	public void setR137_CEA_AFTER_NETTING(BigDecimal r137_CEA_AFTER_NETTING) {
		R137_CEA_AFTER_NETTING = r137_CEA_AFTER_NETTING;
	}
	public BigDecimal getR137_CEA_ELIGIBLE_CRM_SUB() {
		return R137_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR137_CEA_ELIGIBLE_CRM_SUB(BigDecimal r137_CEA_ELIGIBLE_CRM_SUB) {
		R137_CEA_ELIGIBLE_CRM_SUB = r137_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR137_GUARANTEE_ELIGIBLE() {
		return R137_GUARANTEE_ELIGIBLE;
	}
	public void setR137_GUARANTEE_ELIGIBLE(BigDecimal r137_GUARANTEE_ELIGIBLE) {
		R137_GUARANTEE_ELIGIBLE = r137_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR137_CREDIT_DERIVATIVES() {
		return R137_CREDIT_DERIVATIVES;
	}
	public void setR137_CREDIT_DERIVATIVES(BigDecimal r137_CREDIT_DERIVATIVES) {
		R137_CREDIT_DERIVATIVES = r137_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR137_EXPOSURE_COVERED_CRM() {
		return R137_EXPOSURE_COVERED_CRM;
	}
	public void setR137_EXPOSURE_COVERED_CRM(BigDecimal r137_EXPOSURE_COVERED_CRM) {
		R137_EXPOSURE_COVERED_CRM = r137_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR137_EXPOSURE_NOT_COVERED_CRM() {
		return R137_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR137_EXPOSURE_NOT_COVERED_CRM(BigDecimal r137_EXPOSURE_NOT_COVERED_CRM) {
		R137_EXPOSURE_NOT_COVERED_CRM = r137_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR137_RWA_TOTAL() {
		return R137_RWA_TOTAL;
	}
	public void setR137_RWA_TOTAL(BigDecimal r137_RWA_TOTAL) {
		R137_RWA_TOTAL = r137_RWA_TOTAL;
	}
	public BigDecimal getR137_CRM_RISK_WEIGHT() {
		return R137_CRM_RISK_WEIGHT;
	}
	public void setR137_CRM_RISK_WEIGHT(BigDecimal r137_CRM_RISK_WEIGHT) {
		R137_CRM_RISK_WEIGHT = r137_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR137_RWA_CRM_COVERED() {
		return R137_RWA_CRM_COVERED;
	}
	public void setR137_RWA_CRM_COVERED(BigDecimal r137_RWA_CRM_COVERED) {
		R137_RWA_CRM_COVERED = r137_RWA_CRM_COVERED;
	}
	public BigDecimal getR137_ORIGINAL_CP_RISK_WEIGHT() {
		return R137_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR137_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r137_ORIGINAL_CP_RISK_WEIGHT) {
		R137_ORIGINAL_CP_RISK_WEIGHT = r137_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR137_RWA_NOT_COVERED() {
		return R137_RWA_NOT_COVERED;
	}
	public void setR137_RWA_NOT_COVERED(BigDecimal r137_RWA_NOT_COVERED) {
		R137_RWA_NOT_COVERED = r137_RWA_NOT_COVERED;
	}
	public BigDecimal getR137_COLLATERAL_CEA_ELIGIBLE() {
		return R137_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR137_COLLATERAL_CEA_ELIGIBLE(BigDecimal r137_COLLATERAL_CEA_ELIGIBLE) {
		R137_COLLATERAL_CEA_ELIGIBLE = r137_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR137_CEA_AFTER_VOL_ADJ() {
		return R137_CEA_AFTER_VOL_ADJ;
	}
	public void setR137_CEA_AFTER_VOL_ADJ(BigDecimal r137_CEA_AFTER_VOL_ADJ) {
		R137_CEA_AFTER_VOL_ADJ = r137_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR137_COLL_CASH() {
		return R137_COLL_CASH;
	}
	public void setR137_COLL_CASH(BigDecimal r137_COLL_CASH) {
		R137_COLL_CASH = r137_COLL_CASH;
	}
	public BigDecimal getR137_COLL_TBILLS() {
		return R137_COLL_TBILLS;
	}
	public void setR137_COLL_TBILLS(BigDecimal r137_COLL_TBILLS) {
		R137_COLL_TBILLS = r137_COLL_TBILLS;
	}
	public BigDecimal getR137_COLL_DEBT_SEC() {
		return R137_COLL_DEBT_SEC;
	}
	public void setR137_COLL_DEBT_SEC(BigDecimal r137_COLL_DEBT_SEC) {
		R137_COLL_DEBT_SEC = r137_COLL_DEBT_SEC;
	}
	public BigDecimal getR137_COLL_EQUITIES() {
		return R137_COLL_EQUITIES;
	}
	public void setR137_COLL_EQUITIES(BigDecimal r137_COLL_EQUITIES) {
		R137_COLL_EQUITIES = r137_COLL_EQUITIES;
	}
	public BigDecimal getR137_COLL_MUTUAL_FUNDS() {
		return R137_COLL_MUTUAL_FUNDS;
	}
	public void setR137_COLL_MUTUAL_FUNDS(BigDecimal r137_COLL_MUTUAL_FUNDS) {
		R137_COLL_MUTUAL_FUNDS = r137_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR137_COLL_TOTAL() {
		return R137_COLL_TOTAL;
	}
	public void setR137_COLL_TOTAL(BigDecimal r137_COLL_TOTAL) {
		R137_COLL_TOTAL = r137_COLL_TOTAL;
	}
	public BigDecimal getR137_CEA_AFTER_CRM() {
		return R137_CEA_AFTER_CRM;
	}
	public void setR137_CEA_AFTER_CRM(BigDecimal r137_CEA_AFTER_CRM) {
		R137_CEA_AFTER_CRM = r137_CEA_AFTER_CRM;
	}
	public BigDecimal getR137_RWA_CEA_NOT_COVERED() {
		return R137_RWA_CEA_NOT_COVERED;
	}
	public void setR137_RWA_CEA_NOT_COVERED(BigDecimal r137_RWA_CEA_NOT_COVERED) {
		R137_RWA_CEA_NOT_COVERED = r137_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR137_UNSECURED_CEA() {
		return R137_UNSECURED_CEA;
	}
	public void setR137_UNSECURED_CEA(BigDecimal r137_UNSECURED_CEA) {
		R137_UNSECURED_CEA = r137_UNSECURED_CEA;
	}
	public BigDecimal getR137_RWA_UNSECURED_CEA() {
		return R137_RWA_UNSECURED_CEA;
	}
	public void setR137_RWA_UNSECURED_CEA(BigDecimal r137_RWA_UNSECURED_CEA) {
		R137_RWA_UNSECURED_CEA = r137_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR138_NOMINAL_PRINCIPAL_AMT() {
		return R138_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR138_NOMINAL_PRINCIPAL_AMT(BigDecimal r138_NOMINAL_PRINCIPAL_AMT) {
		R138_NOMINAL_PRINCIPAL_AMT = r138_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR138_CCF_PCT() {
		return R138_CCF_PCT;
	}
	public void setR138_CCF_PCT(BigDecimal r138_CCF_PCT) {
		R138_CCF_PCT = r138_CCF_PCT;
	}
	public BigDecimal getR138_CREDIT_EQUIVALENT_AMT() {
		return R138_CREDIT_EQUIVALENT_AMT;
	}
	public void setR138_CREDIT_EQUIVALENT_AMT(BigDecimal r138_CREDIT_EQUIVALENT_AMT) {
		R138_CREDIT_EQUIVALENT_AMT = r138_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR138_CEA_ELIGIBLE_NETTING_CP() {
		return R138_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR138_CEA_ELIGIBLE_NETTING_CP(BigDecimal r138_CEA_ELIGIBLE_NETTING_CP) {
		R138_CEA_ELIGIBLE_NETTING_CP = r138_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR138_CEA_AFTER_NETTING() {
		return R138_CEA_AFTER_NETTING;
	}
	public void setR138_CEA_AFTER_NETTING(BigDecimal r138_CEA_AFTER_NETTING) {
		R138_CEA_AFTER_NETTING = r138_CEA_AFTER_NETTING;
	}
	public BigDecimal getR138_CEA_ELIGIBLE_CRM_SUB() {
		return R138_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR138_CEA_ELIGIBLE_CRM_SUB(BigDecimal r138_CEA_ELIGIBLE_CRM_SUB) {
		R138_CEA_ELIGIBLE_CRM_SUB = r138_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR138_GUARANTEE_ELIGIBLE() {
		return R138_GUARANTEE_ELIGIBLE;
	}
	public void setR138_GUARANTEE_ELIGIBLE(BigDecimal r138_GUARANTEE_ELIGIBLE) {
		R138_GUARANTEE_ELIGIBLE = r138_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR138_CREDIT_DERIVATIVES() {
		return R138_CREDIT_DERIVATIVES;
	}
	public void setR138_CREDIT_DERIVATIVES(BigDecimal r138_CREDIT_DERIVATIVES) {
		R138_CREDIT_DERIVATIVES = r138_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR138_EXPOSURE_COVERED_CRM() {
		return R138_EXPOSURE_COVERED_CRM;
	}
	public void setR138_EXPOSURE_COVERED_CRM(BigDecimal r138_EXPOSURE_COVERED_CRM) {
		R138_EXPOSURE_COVERED_CRM = r138_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR138_EXPOSURE_NOT_COVERED_CRM() {
		return R138_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR138_EXPOSURE_NOT_COVERED_CRM(BigDecimal r138_EXPOSURE_NOT_COVERED_CRM) {
		R138_EXPOSURE_NOT_COVERED_CRM = r138_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR138_RWA_TOTAL() {
		return R138_RWA_TOTAL;
	}
	public void setR138_RWA_TOTAL(BigDecimal r138_RWA_TOTAL) {
		R138_RWA_TOTAL = r138_RWA_TOTAL;
	}
	public BigDecimal getR138_CRM_RISK_WEIGHT() {
		return R138_CRM_RISK_WEIGHT;
	}
	public void setR138_CRM_RISK_WEIGHT(BigDecimal r138_CRM_RISK_WEIGHT) {
		R138_CRM_RISK_WEIGHT = r138_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR138_RWA_CRM_COVERED() {
		return R138_RWA_CRM_COVERED;
	}
	public void setR138_RWA_CRM_COVERED(BigDecimal r138_RWA_CRM_COVERED) {
		R138_RWA_CRM_COVERED = r138_RWA_CRM_COVERED;
	}
	public BigDecimal getR138_ORIGINAL_CP_RISK_WEIGHT() {
		return R138_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR138_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r138_ORIGINAL_CP_RISK_WEIGHT) {
		R138_ORIGINAL_CP_RISK_WEIGHT = r138_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR138_RWA_NOT_COVERED() {
		return R138_RWA_NOT_COVERED;
	}
	public void setR138_RWA_NOT_COVERED(BigDecimal r138_RWA_NOT_COVERED) {
		R138_RWA_NOT_COVERED = r138_RWA_NOT_COVERED;
	}
	public BigDecimal getR138_COLLATERAL_CEA_ELIGIBLE() {
		return R138_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR138_COLLATERAL_CEA_ELIGIBLE(BigDecimal r138_COLLATERAL_CEA_ELIGIBLE) {
		R138_COLLATERAL_CEA_ELIGIBLE = r138_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR138_CEA_AFTER_VOL_ADJ() {
		return R138_CEA_AFTER_VOL_ADJ;
	}
	public void setR138_CEA_AFTER_VOL_ADJ(BigDecimal r138_CEA_AFTER_VOL_ADJ) {
		R138_CEA_AFTER_VOL_ADJ = r138_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR138_COLL_CASH() {
		return R138_COLL_CASH;
	}
	public void setR138_COLL_CASH(BigDecimal r138_COLL_CASH) {
		R138_COLL_CASH = r138_COLL_CASH;
	}
	public BigDecimal getR138_COLL_TBILLS() {
		return R138_COLL_TBILLS;
	}
	public void setR138_COLL_TBILLS(BigDecimal r138_COLL_TBILLS) {
		R138_COLL_TBILLS = r138_COLL_TBILLS;
	}
	public BigDecimal getR138_COLL_DEBT_SEC() {
		return R138_COLL_DEBT_SEC;
	}
	public void setR138_COLL_DEBT_SEC(BigDecimal r138_COLL_DEBT_SEC) {
		R138_COLL_DEBT_SEC = r138_COLL_DEBT_SEC;
	}
	public BigDecimal getR138_COLL_EQUITIES() {
		return R138_COLL_EQUITIES;
	}
	public void setR138_COLL_EQUITIES(BigDecimal r138_COLL_EQUITIES) {
		R138_COLL_EQUITIES = r138_COLL_EQUITIES;
	}
	public BigDecimal getR138_COLL_MUTUAL_FUNDS() {
		return R138_COLL_MUTUAL_FUNDS;
	}
	public void setR138_COLL_MUTUAL_FUNDS(BigDecimal r138_COLL_MUTUAL_FUNDS) {
		R138_COLL_MUTUAL_FUNDS = r138_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR138_COLL_TOTAL() {
		return R138_COLL_TOTAL;
	}
	public void setR138_COLL_TOTAL(BigDecimal r138_COLL_TOTAL) {
		R138_COLL_TOTAL = r138_COLL_TOTAL;
	}
	public BigDecimal getR138_CEA_AFTER_CRM() {
		return R138_CEA_AFTER_CRM;
	}
	public void setR138_CEA_AFTER_CRM(BigDecimal r138_CEA_AFTER_CRM) {
		R138_CEA_AFTER_CRM = r138_CEA_AFTER_CRM;
	}
	public BigDecimal getR138_RWA_CEA_NOT_COVERED() {
		return R138_RWA_CEA_NOT_COVERED;
	}
	public void setR138_RWA_CEA_NOT_COVERED(BigDecimal r138_RWA_CEA_NOT_COVERED) {
		R138_RWA_CEA_NOT_COVERED = r138_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR138_UNSECURED_CEA() {
		return R138_UNSECURED_CEA;
	}
	public void setR138_UNSECURED_CEA(BigDecimal r138_UNSECURED_CEA) {
		R138_UNSECURED_CEA = r138_UNSECURED_CEA;
	}
	public BigDecimal getR138_RWA_UNSECURED_CEA() {
		return R138_RWA_UNSECURED_CEA;
	}
	public void setR138_RWA_UNSECURED_CEA(BigDecimal r138_RWA_UNSECURED_CEA) {
		R138_RWA_UNSECURED_CEA = r138_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR139_NOMINAL_PRINCIPAL_AMT() {
		return R139_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR139_NOMINAL_PRINCIPAL_AMT(BigDecimal r139_NOMINAL_PRINCIPAL_AMT) {
		R139_NOMINAL_PRINCIPAL_AMT = r139_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR139_CCF_PCT() {
		return R139_CCF_PCT;
	}
	public void setR139_CCF_PCT(BigDecimal r139_CCF_PCT) {
		R139_CCF_PCT = r139_CCF_PCT;
	}
	public BigDecimal getR139_CREDIT_EQUIVALENT_AMT() {
		return R139_CREDIT_EQUIVALENT_AMT;
	}
	public void setR139_CREDIT_EQUIVALENT_AMT(BigDecimal r139_CREDIT_EQUIVALENT_AMT) {
		R139_CREDIT_EQUIVALENT_AMT = r139_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR139_CEA_ELIGIBLE_NETTING_CP() {
		return R139_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR139_CEA_ELIGIBLE_NETTING_CP(BigDecimal r139_CEA_ELIGIBLE_NETTING_CP) {
		R139_CEA_ELIGIBLE_NETTING_CP = r139_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR139_CEA_AFTER_NETTING() {
		return R139_CEA_AFTER_NETTING;
	}
	public void setR139_CEA_AFTER_NETTING(BigDecimal r139_CEA_AFTER_NETTING) {
		R139_CEA_AFTER_NETTING = r139_CEA_AFTER_NETTING;
	}
	public BigDecimal getR139_CEA_ELIGIBLE_CRM_SUB() {
		return R139_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR139_CEA_ELIGIBLE_CRM_SUB(BigDecimal r139_CEA_ELIGIBLE_CRM_SUB) {
		R139_CEA_ELIGIBLE_CRM_SUB = r139_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR139_GUARANTEE_ELIGIBLE() {
		return R139_GUARANTEE_ELIGIBLE;
	}
	public void setR139_GUARANTEE_ELIGIBLE(BigDecimal r139_GUARANTEE_ELIGIBLE) {
		R139_GUARANTEE_ELIGIBLE = r139_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR139_CREDIT_DERIVATIVES() {
		return R139_CREDIT_DERIVATIVES;
	}
	public void setR139_CREDIT_DERIVATIVES(BigDecimal r139_CREDIT_DERIVATIVES) {
		R139_CREDIT_DERIVATIVES = r139_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR139_EXPOSURE_COVERED_CRM() {
		return R139_EXPOSURE_COVERED_CRM;
	}
	public void setR139_EXPOSURE_COVERED_CRM(BigDecimal r139_EXPOSURE_COVERED_CRM) {
		R139_EXPOSURE_COVERED_CRM = r139_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR139_EXPOSURE_NOT_COVERED_CRM() {
		return R139_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR139_EXPOSURE_NOT_COVERED_CRM(BigDecimal r139_EXPOSURE_NOT_COVERED_CRM) {
		R139_EXPOSURE_NOT_COVERED_CRM = r139_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR139_RWA_TOTAL() {
		return R139_RWA_TOTAL;
	}
	public void setR139_RWA_TOTAL(BigDecimal r139_RWA_TOTAL) {
		R139_RWA_TOTAL = r139_RWA_TOTAL;
	}
	public BigDecimal getR139_CRM_RISK_WEIGHT() {
		return R139_CRM_RISK_WEIGHT;
	}
	public void setR139_CRM_RISK_WEIGHT(BigDecimal r139_CRM_RISK_WEIGHT) {
		R139_CRM_RISK_WEIGHT = r139_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR139_RWA_CRM_COVERED() {
		return R139_RWA_CRM_COVERED;
	}
	public void setR139_RWA_CRM_COVERED(BigDecimal r139_RWA_CRM_COVERED) {
		R139_RWA_CRM_COVERED = r139_RWA_CRM_COVERED;
	}
	public BigDecimal getR139_ORIGINAL_CP_RISK_WEIGHT() {
		return R139_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR139_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r139_ORIGINAL_CP_RISK_WEIGHT) {
		R139_ORIGINAL_CP_RISK_WEIGHT = r139_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR139_RWA_NOT_COVERED() {
		return R139_RWA_NOT_COVERED;
	}
	public void setR139_RWA_NOT_COVERED(BigDecimal r139_RWA_NOT_COVERED) {
		R139_RWA_NOT_COVERED = r139_RWA_NOT_COVERED;
	}
	public BigDecimal getR139_COLLATERAL_CEA_ELIGIBLE() {
		return R139_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR139_COLLATERAL_CEA_ELIGIBLE(BigDecimal r139_COLLATERAL_CEA_ELIGIBLE) {
		R139_COLLATERAL_CEA_ELIGIBLE = r139_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR139_CEA_AFTER_VOL_ADJ() {
		return R139_CEA_AFTER_VOL_ADJ;
	}
	public void setR139_CEA_AFTER_VOL_ADJ(BigDecimal r139_CEA_AFTER_VOL_ADJ) {
		R139_CEA_AFTER_VOL_ADJ = r139_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR139_COLL_CASH() {
		return R139_COLL_CASH;
	}
	public void setR139_COLL_CASH(BigDecimal r139_COLL_CASH) {
		R139_COLL_CASH = r139_COLL_CASH;
	}
	public BigDecimal getR139_COLL_TBILLS() {
		return R139_COLL_TBILLS;
	}
	public void setR139_COLL_TBILLS(BigDecimal r139_COLL_TBILLS) {
		R139_COLL_TBILLS = r139_COLL_TBILLS;
	}
	public BigDecimal getR139_COLL_DEBT_SEC() {
		return R139_COLL_DEBT_SEC;
	}
	public void setR139_COLL_DEBT_SEC(BigDecimal r139_COLL_DEBT_SEC) {
		R139_COLL_DEBT_SEC = r139_COLL_DEBT_SEC;
	}
	public BigDecimal getR139_COLL_EQUITIES() {
		return R139_COLL_EQUITIES;
	}
	public void setR139_COLL_EQUITIES(BigDecimal r139_COLL_EQUITIES) {
		R139_COLL_EQUITIES = r139_COLL_EQUITIES;
	}
	public BigDecimal getR139_COLL_MUTUAL_FUNDS() {
		return R139_COLL_MUTUAL_FUNDS;
	}
	public void setR139_COLL_MUTUAL_FUNDS(BigDecimal r139_COLL_MUTUAL_FUNDS) {
		R139_COLL_MUTUAL_FUNDS = r139_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR139_COLL_TOTAL() {
		return R139_COLL_TOTAL;
	}
	public void setR139_COLL_TOTAL(BigDecimal r139_COLL_TOTAL) {
		R139_COLL_TOTAL = r139_COLL_TOTAL;
	}
	public BigDecimal getR139_CEA_AFTER_CRM() {
		return R139_CEA_AFTER_CRM;
	}
	public void setR139_CEA_AFTER_CRM(BigDecimal r139_CEA_AFTER_CRM) {
		R139_CEA_AFTER_CRM = r139_CEA_AFTER_CRM;
	}
	public BigDecimal getR139_RWA_CEA_NOT_COVERED() {
		return R139_RWA_CEA_NOT_COVERED;
	}
	public void setR139_RWA_CEA_NOT_COVERED(BigDecimal r139_RWA_CEA_NOT_COVERED) {
		R139_RWA_CEA_NOT_COVERED = r139_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR139_UNSECURED_CEA() {
		return R139_UNSECURED_CEA;
	}
	public void setR139_UNSECURED_CEA(BigDecimal r139_UNSECURED_CEA) {
		R139_UNSECURED_CEA = r139_UNSECURED_CEA;
	}
	public BigDecimal getR139_RWA_UNSECURED_CEA() {
		return R139_RWA_UNSECURED_CEA;
	}
	public void setR139_RWA_UNSECURED_CEA(BigDecimal r139_RWA_UNSECURED_CEA) {
		R139_RWA_UNSECURED_CEA = r139_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR140_NOMINAL_PRINCIPAL_AMT() {
		return R140_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR140_NOMINAL_PRINCIPAL_AMT(BigDecimal r140_NOMINAL_PRINCIPAL_AMT) {
		R140_NOMINAL_PRINCIPAL_AMT = r140_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR140_CCF_PCT() {
		return R140_CCF_PCT;
	}
	public void setR140_CCF_PCT(BigDecimal r140_CCF_PCT) {
		R140_CCF_PCT = r140_CCF_PCT;
	}
	public BigDecimal getR140_CREDIT_EQUIVALENT_AMT() {
		return R140_CREDIT_EQUIVALENT_AMT;
	}
	public void setR140_CREDIT_EQUIVALENT_AMT(BigDecimal r140_CREDIT_EQUIVALENT_AMT) {
		R140_CREDIT_EQUIVALENT_AMT = r140_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR140_CEA_ELIGIBLE_NETTING_CP() {
		return R140_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR140_CEA_ELIGIBLE_NETTING_CP(BigDecimal r140_CEA_ELIGIBLE_NETTING_CP) {
		R140_CEA_ELIGIBLE_NETTING_CP = r140_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR140_CEA_AFTER_NETTING() {
		return R140_CEA_AFTER_NETTING;
	}
	public void setR140_CEA_AFTER_NETTING(BigDecimal r140_CEA_AFTER_NETTING) {
		R140_CEA_AFTER_NETTING = r140_CEA_AFTER_NETTING;
	}
	public BigDecimal getR140_CEA_ELIGIBLE_CRM_SUB() {
		return R140_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR140_CEA_ELIGIBLE_CRM_SUB(BigDecimal r140_CEA_ELIGIBLE_CRM_SUB) {
		R140_CEA_ELIGIBLE_CRM_SUB = r140_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR140_GUARANTEE_ELIGIBLE() {
		return R140_GUARANTEE_ELIGIBLE;
	}
	public void setR140_GUARANTEE_ELIGIBLE(BigDecimal r140_GUARANTEE_ELIGIBLE) {
		R140_GUARANTEE_ELIGIBLE = r140_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR140_CREDIT_DERIVATIVES() {
		return R140_CREDIT_DERIVATIVES;
	}
	public void setR140_CREDIT_DERIVATIVES(BigDecimal r140_CREDIT_DERIVATIVES) {
		R140_CREDIT_DERIVATIVES = r140_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR140_EXPOSURE_COVERED_CRM() {
		return R140_EXPOSURE_COVERED_CRM;
	}
	public void setR140_EXPOSURE_COVERED_CRM(BigDecimal r140_EXPOSURE_COVERED_CRM) {
		R140_EXPOSURE_COVERED_CRM = r140_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR140_EXPOSURE_NOT_COVERED_CRM() {
		return R140_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR140_EXPOSURE_NOT_COVERED_CRM(BigDecimal r140_EXPOSURE_NOT_COVERED_CRM) {
		R140_EXPOSURE_NOT_COVERED_CRM = r140_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR140_RWA_TOTAL() {
		return R140_RWA_TOTAL;
	}
	public void setR140_RWA_TOTAL(BigDecimal r140_RWA_TOTAL) {
		R140_RWA_TOTAL = r140_RWA_TOTAL;
	}
	public BigDecimal getR140_CRM_RISK_WEIGHT() {
		return R140_CRM_RISK_WEIGHT;
	}
	public void setR140_CRM_RISK_WEIGHT(BigDecimal r140_CRM_RISK_WEIGHT) {
		R140_CRM_RISK_WEIGHT = r140_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR140_RWA_CRM_COVERED() {
		return R140_RWA_CRM_COVERED;
	}
	public void setR140_RWA_CRM_COVERED(BigDecimal r140_RWA_CRM_COVERED) {
		R140_RWA_CRM_COVERED = r140_RWA_CRM_COVERED;
	}
	public BigDecimal getR140_ORIGINAL_CP_RISK_WEIGHT() {
		return R140_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR140_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r140_ORIGINAL_CP_RISK_WEIGHT) {
		R140_ORIGINAL_CP_RISK_WEIGHT = r140_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR140_RWA_NOT_COVERED() {
		return R140_RWA_NOT_COVERED;
	}
	public void setR140_RWA_NOT_COVERED(BigDecimal r140_RWA_NOT_COVERED) {
		R140_RWA_NOT_COVERED = r140_RWA_NOT_COVERED;
	}
	public BigDecimal getR140_COLLATERAL_CEA_ELIGIBLE() {
		return R140_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR140_COLLATERAL_CEA_ELIGIBLE(BigDecimal r140_COLLATERAL_CEA_ELIGIBLE) {
		R140_COLLATERAL_CEA_ELIGIBLE = r140_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR140_CEA_AFTER_VOL_ADJ() {
		return R140_CEA_AFTER_VOL_ADJ;
	}
	public void setR140_CEA_AFTER_VOL_ADJ(BigDecimal r140_CEA_AFTER_VOL_ADJ) {
		R140_CEA_AFTER_VOL_ADJ = r140_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR140_COLL_CASH() {
		return R140_COLL_CASH;
	}
	public void setR140_COLL_CASH(BigDecimal r140_COLL_CASH) {
		R140_COLL_CASH = r140_COLL_CASH;
	}
	public BigDecimal getR140_COLL_TBILLS() {
		return R140_COLL_TBILLS;
	}
	public void setR140_COLL_TBILLS(BigDecimal r140_COLL_TBILLS) {
		R140_COLL_TBILLS = r140_COLL_TBILLS;
	}
	public BigDecimal getR140_COLL_DEBT_SEC() {
		return R140_COLL_DEBT_SEC;
	}
	public void setR140_COLL_DEBT_SEC(BigDecimal r140_COLL_DEBT_SEC) {
		R140_COLL_DEBT_SEC = r140_COLL_DEBT_SEC;
	}
	public BigDecimal getR140_COLL_EQUITIES() {
		return R140_COLL_EQUITIES;
	}
	public void setR140_COLL_EQUITIES(BigDecimal r140_COLL_EQUITIES) {
		R140_COLL_EQUITIES = r140_COLL_EQUITIES;
	}
	public BigDecimal getR140_COLL_MUTUAL_FUNDS() {
		return R140_COLL_MUTUAL_FUNDS;
	}
	public void setR140_COLL_MUTUAL_FUNDS(BigDecimal r140_COLL_MUTUAL_FUNDS) {
		R140_COLL_MUTUAL_FUNDS = r140_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR140_COLL_TOTAL() {
		return R140_COLL_TOTAL;
	}
	public void setR140_COLL_TOTAL(BigDecimal r140_COLL_TOTAL) {
		R140_COLL_TOTAL = r140_COLL_TOTAL;
	}
	public BigDecimal getR140_CEA_AFTER_CRM() {
		return R140_CEA_AFTER_CRM;
	}
	public void setR140_CEA_AFTER_CRM(BigDecimal r140_CEA_AFTER_CRM) {
		R140_CEA_AFTER_CRM = r140_CEA_AFTER_CRM;
	}
	public BigDecimal getR140_RWA_CEA_NOT_COVERED() {
		return R140_RWA_CEA_NOT_COVERED;
	}
	public void setR140_RWA_CEA_NOT_COVERED(BigDecimal r140_RWA_CEA_NOT_COVERED) {
		R140_RWA_CEA_NOT_COVERED = r140_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR140_UNSECURED_CEA() {
		return R140_UNSECURED_CEA;
	}
	public void setR140_UNSECURED_CEA(BigDecimal r140_UNSECURED_CEA) {
		R140_UNSECURED_CEA = r140_UNSECURED_CEA;
	}
	public BigDecimal getR140_RWA_UNSECURED_CEA() {
		return R140_RWA_UNSECURED_CEA;
	}
	public void setR140_RWA_UNSECURED_CEA(BigDecimal r140_RWA_UNSECURED_CEA) {
		R140_RWA_UNSECURED_CEA = r140_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR141_NOMINAL_PRINCIPAL_AMT() {
		return R141_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR141_NOMINAL_PRINCIPAL_AMT(BigDecimal r141_NOMINAL_PRINCIPAL_AMT) {
		R141_NOMINAL_PRINCIPAL_AMT = r141_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR141_CCF_PCT() {
		return R141_CCF_PCT;
	}
	public void setR141_CCF_PCT(BigDecimal r141_CCF_PCT) {
		R141_CCF_PCT = r141_CCF_PCT;
	}
	public BigDecimal getR141_CREDIT_EQUIVALENT_AMT() {
		return R141_CREDIT_EQUIVALENT_AMT;
	}
	public void setR141_CREDIT_EQUIVALENT_AMT(BigDecimal r141_CREDIT_EQUIVALENT_AMT) {
		R141_CREDIT_EQUIVALENT_AMT = r141_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR141_CEA_ELIGIBLE_NETTING_CP() {
		return R141_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR141_CEA_ELIGIBLE_NETTING_CP(BigDecimal r141_CEA_ELIGIBLE_NETTING_CP) {
		R141_CEA_ELIGIBLE_NETTING_CP = r141_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR141_CEA_AFTER_NETTING() {
		return R141_CEA_AFTER_NETTING;
	}
	public void setR141_CEA_AFTER_NETTING(BigDecimal r141_CEA_AFTER_NETTING) {
		R141_CEA_AFTER_NETTING = r141_CEA_AFTER_NETTING;
	}
	public BigDecimal getR141_CEA_ELIGIBLE_CRM_SUB() {
		return R141_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR141_CEA_ELIGIBLE_CRM_SUB(BigDecimal r141_CEA_ELIGIBLE_CRM_SUB) {
		R141_CEA_ELIGIBLE_CRM_SUB = r141_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR141_GUARANTEE_ELIGIBLE() {
		return R141_GUARANTEE_ELIGIBLE;
	}
	public void setR141_GUARANTEE_ELIGIBLE(BigDecimal r141_GUARANTEE_ELIGIBLE) {
		R141_GUARANTEE_ELIGIBLE = r141_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR141_CREDIT_DERIVATIVES() {
		return R141_CREDIT_DERIVATIVES;
	}
	public void setR141_CREDIT_DERIVATIVES(BigDecimal r141_CREDIT_DERIVATIVES) {
		R141_CREDIT_DERIVATIVES = r141_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR141_EXPOSURE_COVERED_CRM() {
		return R141_EXPOSURE_COVERED_CRM;
	}
	public void setR141_EXPOSURE_COVERED_CRM(BigDecimal r141_EXPOSURE_COVERED_CRM) {
		R141_EXPOSURE_COVERED_CRM = r141_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR141_EXPOSURE_NOT_COVERED_CRM() {
		return R141_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR141_EXPOSURE_NOT_COVERED_CRM(BigDecimal r141_EXPOSURE_NOT_COVERED_CRM) {
		R141_EXPOSURE_NOT_COVERED_CRM = r141_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR141_RWA_TOTAL() {
		return R141_RWA_TOTAL;
	}
	public void setR141_RWA_TOTAL(BigDecimal r141_RWA_TOTAL) {
		R141_RWA_TOTAL = r141_RWA_TOTAL;
	}
	public BigDecimal getR141_CRM_RISK_WEIGHT() {
		return R141_CRM_RISK_WEIGHT;
	}
	public void setR141_CRM_RISK_WEIGHT(BigDecimal r141_CRM_RISK_WEIGHT) {
		R141_CRM_RISK_WEIGHT = r141_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR141_RWA_CRM_COVERED() {
		return R141_RWA_CRM_COVERED;
	}
	public void setR141_RWA_CRM_COVERED(BigDecimal r141_RWA_CRM_COVERED) {
		R141_RWA_CRM_COVERED = r141_RWA_CRM_COVERED;
	}
	public BigDecimal getR141_ORIGINAL_CP_RISK_WEIGHT() {
		return R141_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR141_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r141_ORIGINAL_CP_RISK_WEIGHT) {
		R141_ORIGINAL_CP_RISK_WEIGHT = r141_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR141_RWA_NOT_COVERED() {
		return R141_RWA_NOT_COVERED;
	}
	public void setR141_RWA_NOT_COVERED(BigDecimal r141_RWA_NOT_COVERED) {
		R141_RWA_NOT_COVERED = r141_RWA_NOT_COVERED;
	}
	public BigDecimal getR141_COLLATERAL_CEA_ELIGIBLE() {
		return R141_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR141_COLLATERAL_CEA_ELIGIBLE(BigDecimal r141_COLLATERAL_CEA_ELIGIBLE) {
		R141_COLLATERAL_CEA_ELIGIBLE = r141_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR141_CEA_AFTER_VOL_ADJ() {
		return R141_CEA_AFTER_VOL_ADJ;
	}
	public void setR141_CEA_AFTER_VOL_ADJ(BigDecimal r141_CEA_AFTER_VOL_ADJ) {
		R141_CEA_AFTER_VOL_ADJ = r141_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR141_COLL_CASH() {
		return R141_COLL_CASH;
	}
	public void setR141_COLL_CASH(BigDecimal r141_COLL_CASH) {
		R141_COLL_CASH = r141_COLL_CASH;
	}
	public BigDecimal getR141_COLL_TBILLS() {
		return R141_COLL_TBILLS;
	}
	public void setR141_COLL_TBILLS(BigDecimal r141_COLL_TBILLS) {
		R141_COLL_TBILLS = r141_COLL_TBILLS;
	}
	public BigDecimal getR141_COLL_DEBT_SEC() {
		return R141_COLL_DEBT_SEC;
	}
	public void setR141_COLL_DEBT_SEC(BigDecimal r141_COLL_DEBT_SEC) {
		R141_COLL_DEBT_SEC = r141_COLL_DEBT_SEC;
	}
	public BigDecimal getR141_COLL_EQUITIES() {
		return R141_COLL_EQUITIES;
	}
	public void setR141_COLL_EQUITIES(BigDecimal r141_COLL_EQUITIES) {
		R141_COLL_EQUITIES = r141_COLL_EQUITIES;
	}
	public BigDecimal getR141_COLL_MUTUAL_FUNDS() {
		return R141_COLL_MUTUAL_FUNDS;
	}
	public void setR141_COLL_MUTUAL_FUNDS(BigDecimal r141_COLL_MUTUAL_FUNDS) {
		R141_COLL_MUTUAL_FUNDS = r141_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR141_COLL_TOTAL() {
		return R141_COLL_TOTAL;
	}
	public void setR141_COLL_TOTAL(BigDecimal r141_COLL_TOTAL) {
		R141_COLL_TOTAL = r141_COLL_TOTAL;
	}
	public BigDecimal getR141_CEA_AFTER_CRM() {
		return R141_CEA_AFTER_CRM;
	}
	public void setR141_CEA_AFTER_CRM(BigDecimal r141_CEA_AFTER_CRM) {
		R141_CEA_AFTER_CRM = r141_CEA_AFTER_CRM;
	}
	public BigDecimal getR141_RWA_CEA_NOT_COVERED() {
		return R141_RWA_CEA_NOT_COVERED;
	}
	public void setR141_RWA_CEA_NOT_COVERED(BigDecimal r141_RWA_CEA_NOT_COVERED) {
		R141_RWA_CEA_NOT_COVERED = r141_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR141_UNSECURED_CEA() {
		return R141_UNSECURED_CEA;
	}
	public void setR141_UNSECURED_CEA(BigDecimal r141_UNSECURED_CEA) {
		R141_UNSECURED_CEA = r141_UNSECURED_CEA;
	}
	public BigDecimal getR141_RWA_UNSECURED_CEA() {
		return R141_RWA_UNSECURED_CEA;
	}
	public void setR141_RWA_UNSECURED_CEA(BigDecimal r141_RWA_UNSECURED_CEA) {
		R141_RWA_UNSECURED_CEA = r141_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR142_NOMINAL_PRINCIPAL_AMT() {
		return R142_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR142_NOMINAL_PRINCIPAL_AMT(BigDecimal r142_NOMINAL_PRINCIPAL_AMT) {
		R142_NOMINAL_PRINCIPAL_AMT = r142_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR142_CCF_PCT() {
		return R142_CCF_PCT;
	}
	public void setR142_CCF_PCT(BigDecimal r142_CCF_PCT) {
		R142_CCF_PCT = r142_CCF_PCT;
	}
	public BigDecimal getR142_CREDIT_EQUIVALENT_AMT() {
		return R142_CREDIT_EQUIVALENT_AMT;
	}
	public void setR142_CREDIT_EQUIVALENT_AMT(BigDecimal r142_CREDIT_EQUIVALENT_AMT) {
		R142_CREDIT_EQUIVALENT_AMT = r142_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR142_CEA_ELIGIBLE_NETTING_CP() {
		return R142_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR142_CEA_ELIGIBLE_NETTING_CP(BigDecimal r142_CEA_ELIGIBLE_NETTING_CP) {
		R142_CEA_ELIGIBLE_NETTING_CP = r142_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR142_CEA_AFTER_NETTING() {
		return R142_CEA_AFTER_NETTING;
	}
	public void setR142_CEA_AFTER_NETTING(BigDecimal r142_CEA_AFTER_NETTING) {
		R142_CEA_AFTER_NETTING = r142_CEA_AFTER_NETTING;
	}
	public BigDecimal getR142_CEA_ELIGIBLE_CRM_SUB() {
		return R142_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR142_CEA_ELIGIBLE_CRM_SUB(BigDecimal r142_CEA_ELIGIBLE_CRM_SUB) {
		R142_CEA_ELIGIBLE_CRM_SUB = r142_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR142_GUARANTEE_ELIGIBLE() {
		return R142_GUARANTEE_ELIGIBLE;
	}
	public void setR142_GUARANTEE_ELIGIBLE(BigDecimal r142_GUARANTEE_ELIGIBLE) {
		R142_GUARANTEE_ELIGIBLE = r142_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR142_CREDIT_DERIVATIVES() {
		return R142_CREDIT_DERIVATIVES;
	}
	public void setR142_CREDIT_DERIVATIVES(BigDecimal r142_CREDIT_DERIVATIVES) {
		R142_CREDIT_DERIVATIVES = r142_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR142_EXPOSURE_COVERED_CRM() {
		return R142_EXPOSURE_COVERED_CRM;
	}
	public void setR142_EXPOSURE_COVERED_CRM(BigDecimal r142_EXPOSURE_COVERED_CRM) {
		R142_EXPOSURE_COVERED_CRM = r142_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR142_EXPOSURE_NOT_COVERED_CRM() {
		return R142_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR142_EXPOSURE_NOT_COVERED_CRM(BigDecimal r142_EXPOSURE_NOT_COVERED_CRM) {
		R142_EXPOSURE_NOT_COVERED_CRM = r142_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR142_RWA_TOTAL() {
		return R142_RWA_TOTAL;
	}
	public void setR142_RWA_TOTAL(BigDecimal r142_RWA_TOTAL) {
		R142_RWA_TOTAL = r142_RWA_TOTAL;
	}
	public BigDecimal getR142_CRM_RISK_WEIGHT() {
		return R142_CRM_RISK_WEIGHT;
	}
	public void setR142_CRM_RISK_WEIGHT(BigDecimal r142_CRM_RISK_WEIGHT) {
		R142_CRM_RISK_WEIGHT = r142_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR142_RWA_CRM_COVERED() {
		return R142_RWA_CRM_COVERED;
	}
	public void setR142_RWA_CRM_COVERED(BigDecimal r142_RWA_CRM_COVERED) {
		R142_RWA_CRM_COVERED = r142_RWA_CRM_COVERED;
	}
	public BigDecimal getR142_ORIGINAL_CP_RISK_WEIGHT() {
		return R142_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR142_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r142_ORIGINAL_CP_RISK_WEIGHT) {
		R142_ORIGINAL_CP_RISK_WEIGHT = r142_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR142_RWA_NOT_COVERED() {
		return R142_RWA_NOT_COVERED;
	}
	public void setR142_RWA_NOT_COVERED(BigDecimal r142_RWA_NOT_COVERED) {
		R142_RWA_NOT_COVERED = r142_RWA_NOT_COVERED;
	}
	public BigDecimal getR142_COLLATERAL_CEA_ELIGIBLE() {
		return R142_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR142_COLLATERAL_CEA_ELIGIBLE(BigDecimal r142_COLLATERAL_CEA_ELIGIBLE) {
		R142_COLLATERAL_CEA_ELIGIBLE = r142_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR142_CEA_AFTER_VOL_ADJ() {
		return R142_CEA_AFTER_VOL_ADJ;
	}
	public void setR142_CEA_AFTER_VOL_ADJ(BigDecimal r142_CEA_AFTER_VOL_ADJ) {
		R142_CEA_AFTER_VOL_ADJ = r142_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR142_COLL_CASH() {
		return R142_COLL_CASH;
	}
	public void setR142_COLL_CASH(BigDecimal r142_COLL_CASH) {
		R142_COLL_CASH = r142_COLL_CASH;
	}
	public BigDecimal getR142_COLL_TBILLS() {
		return R142_COLL_TBILLS;
	}
	public void setR142_COLL_TBILLS(BigDecimal r142_COLL_TBILLS) {
		R142_COLL_TBILLS = r142_COLL_TBILLS;
	}
	public BigDecimal getR142_COLL_DEBT_SEC() {
		return R142_COLL_DEBT_SEC;
	}
	public void setR142_COLL_DEBT_SEC(BigDecimal r142_COLL_DEBT_SEC) {
		R142_COLL_DEBT_SEC = r142_COLL_DEBT_SEC;
	}
	public BigDecimal getR142_COLL_EQUITIES() {
		return R142_COLL_EQUITIES;
	}
	public void setR142_COLL_EQUITIES(BigDecimal r142_COLL_EQUITIES) {
		R142_COLL_EQUITIES = r142_COLL_EQUITIES;
	}
	public BigDecimal getR142_COLL_MUTUAL_FUNDS() {
		return R142_COLL_MUTUAL_FUNDS;
	}
	public void setR142_COLL_MUTUAL_FUNDS(BigDecimal r142_COLL_MUTUAL_FUNDS) {
		R142_COLL_MUTUAL_FUNDS = r142_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR142_COLL_TOTAL() {
		return R142_COLL_TOTAL;
	}
	public void setR142_COLL_TOTAL(BigDecimal r142_COLL_TOTAL) {
		R142_COLL_TOTAL = r142_COLL_TOTAL;
	}
	public BigDecimal getR142_CEA_AFTER_CRM() {
		return R142_CEA_AFTER_CRM;
	}
	public void setR142_CEA_AFTER_CRM(BigDecimal r142_CEA_AFTER_CRM) {
		R142_CEA_AFTER_CRM = r142_CEA_AFTER_CRM;
	}
	public BigDecimal getR142_RWA_CEA_NOT_COVERED() {
		return R142_RWA_CEA_NOT_COVERED;
	}
	public void setR142_RWA_CEA_NOT_COVERED(BigDecimal r142_RWA_CEA_NOT_COVERED) {
		R142_RWA_CEA_NOT_COVERED = r142_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR142_UNSECURED_CEA() {
		return R142_UNSECURED_CEA;
	}
	public void setR142_UNSECURED_CEA(BigDecimal r142_UNSECURED_CEA) {
		R142_UNSECURED_CEA = r142_UNSECURED_CEA;
	}
	public BigDecimal getR142_RWA_UNSECURED_CEA() {
		return R142_RWA_UNSECURED_CEA;
	}
	public void setR142_RWA_UNSECURED_CEA(BigDecimal r142_RWA_UNSECURED_CEA) {
		R142_RWA_UNSECURED_CEA = r142_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR143_NOMINAL_PRINCIPAL_AMT() {
		return R143_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR143_NOMINAL_PRINCIPAL_AMT(BigDecimal r143_NOMINAL_PRINCIPAL_AMT) {
		R143_NOMINAL_PRINCIPAL_AMT = r143_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR143_CCF_PCT() {
		return R143_CCF_PCT;
	}
	public void setR143_CCF_PCT(BigDecimal r143_CCF_PCT) {
		R143_CCF_PCT = r143_CCF_PCT;
	}
	public BigDecimal getR143_CREDIT_EQUIVALENT_AMT() {
		return R143_CREDIT_EQUIVALENT_AMT;
	}
	public void setR143_CREDIT_EQUIVALENT_AMT(BigDecimal r143_CREDIT_EQUIVALENT_AMT) {
		R143_CREDIT_EQUIVALENT_AMT = r143_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR143_CEA_ELIGIBLE_NETTING_CP() {
		return R143_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR143_CEA_ELIGIBLE_NETTING_CP(BigDecimal r143_CEA_ELIGIBLE_NETTING_CP) {
		R143_CEA_ELIGIBLE_NETTING_CP = r143_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR143_CEA_AFTER_NETTING() {
		return R143_CEA_AFTER_NETTING;
	}
	public void setR143_CEA_AFTER_NETTING(BigDecimal r143_CEA_AFTER_NETTING) {
		R143_CEA_AFTER_NETTING = r143_CEA_AFTER_NETTING;
	}
	public BigDecimal getR143_CEA_ELIGIBLE_CRM_SUB() {
		return R143_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR143_CEA_ELIGIBLE_CRM_SUB(BigDecimal r143_CEA_ELIGIBLE_CRM_SUB) {
		R143_CEA_ELIGIBLE_CRM_SUB = r143_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR143_GUARANTEE_ELIGIBLE() {
		return R143_GUARANTEE_ELIGIBLE;
	}
	public void setR143_GUARANTEE_ELIGIBLE(BigDecimal r143_GUARANTEE_ELIGIBLE) {
		R143_GUARANTEE_ELIGIBLE = r143_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR143_CREDIT_DERIVATIVES() {
		return R143_CREDIT_DERIVATIVES;
	}
	public void setR143_CREDIT_DERIVATIVES(BigDecimal r143_CREDIT_DERIVATIVES) {
		R143_CREDIT_DERIVATIVES = r143_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR143_EXPOSURE_COVERED_CRM() {
		return R143_EXPOSURE_COVERED_CRM;
	}
	public void setR143_EXPOSURE_COVERED_CRM(BigDecimal r143_EXPOSURE_COVERED_CRM) {
		R143_EXPOSURE_COVERED_CRM = r143_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR143_EXPOSURE_NOT_COVERED_CRM() {
		return R143_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR143_EXPOSURE_NOT_COVERED_CRM(BigDecimal r143_EXPOSURE_NOT_COVERED_CRM) {
		R143_EXPOSURE_NOT_COVERED_CRM = r143_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR143_RWA_TOTAL() {
		return R143_RWA_TOTAL;
	}
	public void setR143_RWA_TOTAL(BigDecimal r143_RWA_TOTAL) {
		R143_RWA_TOTAL = r143_RWA_TOTAL;
	}
	public BigDecimal getR143_CRM_RISK_WEIGHT() {
		return R143_CRM_RISK_WEIGHT;
	}
	public void setR143_CRM_RISK_WEIGHT(BigDecimal r143_CRM_RISK_WEIGHT) {
		R143_CRM_RISK_WEIGHT = r143_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR143_RWA_CRM_COVERED() {
		return R143_RWA_CRM_COVERED;
	}
	public void setR143_RWA_CRM_COVERED(BigDecimal r143_RWA_CRM_COVERED) {
		R143_RWA_CRM_COVERED = r143_RWA_CRM_COVERED;
	}
	public BigDecimal getR143_ORIGINAL_CP_RISK_WEIGHT() {
		return R143_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR143_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r143_ORIGINAL_CP_RISK_WEIGHT) {
		R143_ORIGINAL_CP_RISK_WEIGHT = r143_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR143_RWA_NOT_COVERED() {
		return R143_RWA_NOT_COVERED;
	}
	public void setR143_RWA_NOT_COVERED(BigDecimal r143_RWA_NOT_COVERED) {
		R143_RWA_NOT_COVERED = r143_RWA_NOT_COVERED;
	}
	public BigDecimal getR143_COLLATERAL_CEA_ELIGIBLE() {
		return R143_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR143_COLLATERAL_CEA_ELIGIBLE(BigDecimal r143_COLLATERAL_CEA_ELIGIBLE) {
		R143_COLLATERAL_CEA_ELIGIBLE = r143_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR143_CEA_AFTER_VOL_ADJ() {
		return R143_CEA_AFTER_VOL_ADJ;
	}
	public void setR143_CEA_AFTER_VOL_ADJ(BigDecimal r143_CEA_AFTER_VOL_ADJ) {
		R143_CEA_AFTER_VOL_ADJ = r143_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR143_COLL_CASH() {
		return R143_COLL_CASH;
	}
	public void setR143_COLL_CASH(BigDecimal r143_COLL_CASH) {
		R143_COLL_CASH = r143_COLL_CASH;
	}
	public BigDecimal getR143_COLL_TBILLS() {
		return R143_COLL_TBILLS;
	}
	public void setR143_COLL_TBILLS(BigDecimal r143_COLL_TBILLS) {
		R143_COLL_TBILLS = r143_COLL_TBILLS;
	}
	public BigDecimal getR143_COLL_DEBT_SEC() {
		return R143_COLL_DEBT_SEC;
	}
	public void setR143_COLL_DEBT_SEC(BigDecimal r143_COLL_DEBT_SEC) {
		R143_COLL_DEBT_SEC = r143_COLL_DEBT_SEC;
	}
	public BigDecimal getR143_COLL_EQUITIES() {
		return R143_COLL_EQUITIES;
	}
	public void setR143_COLL_EQUITIES(BigDecimal r143_COLL_EQUITIES) {
		R143_COLL_EQUITIES = r143_COLL_EQUITIES;
	}
	public BigDecimal getR143_COLL_MUTUAL_FUNDS() {
		return R143_COLL_MUTUAL_FUNDS;
	}
	public void setR143_COLL_MUTUAL_FUNDS(BigDecimal r143_COLL_MUTUAL_FUNDS) {
		R143_COLL_MUTUAL_FUNDS = r143_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR143_COLL_TOTAL() {
		return R143_COLL_TOTAL;
	}
	public void setR143_COLL_TOTAL(BigDecimal r143_COLL_TOTAL) {
		R143_COLL_TOTAL = r143_COLL_TOTAL;
	}
	public BigDecimal getR143_CEA_AFTER_CRM() {
		return R143_CEA_AFTER_CRM;
	}
	public void setR143_CEA_AFTER_CRM(BigDecimal r143_CEA_AFTER_CRM) {
		R143_CEA_AFTER_CRM = r143_CEA_AFTER_CRM;
	}
	public BigDecimal getR143_RWA_CEA_NOT_COVERED() {
		return R143_RWA_CEA_NOT_COVERED;
	}
	public void setR143_RWA_CEA_NOT_COVERED(BigDecimal r143_RWA_CEA_NOT_COVERED) {
		R143_RWA_CEA_NOT_COVERED = r143_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR143_UNSECURED_CEA() {
		return R143_UNSECURED_CEA;
	}
	public void setR143_UNSECURED_CEA(BigDecimal r143_UNSECURED_CEA) {
		R143_UNSECURED_CEA = r143_UNSECURED_CEA;
	}
	public BigDecimal getR143_RWA_UNSECURED_CEA() {
		return R143_RWA_UNSECURED_CEA;
	}
	public void setR143_RWA_UNSECURED_CEA(BigDecimal r143_RWA_UNSECURED_CEA) {
		R143_RWA_UNSECURED_CEA = r143_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR144_NOMINAL_PRINCIPAL_AMT() {
		return R144_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR144_NOMINAL_PRINCIPAL_AMT(BigDecimal r144_NOMINAL_PRINCIPAL_AMT) {
		R144_NOMINAL_PRINCIPAL_AMT = r144_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR144_CCF_PCT() {
		return R144_CCF_PCT;
	}
	public void setR144_CCF_PCT(BigDecimal r144_CCF_PCT) {
		R144_CCF_PCT = r144_CCF_PCT;
	}
	public BigDecimal getR144_CREDIT_EQUIVALENT_AMT() {
		return R144_CREDIT_EQUIVALENT_AMT;
	}
	public void setR144_CREDIT_EQUIVALENT_AMT(BigDecimal r144_CREDIT_EQUIVALENT_AMT) {
		R144_CREDIT_EQUIVALENT_AMT = r144_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR144_CEA_ELIGIBLE_NETTING_CP() {
		return R144_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR144_CEA_ELIGIBLE_NETTING_CP(BigDecimal r144_CEA_ELIGIBLE_NETTING_CP) {
		R144_CEA_ELIGIBLE_NETTING_CP = r144_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR144_CEA_AFTER_NETTING() {
		return R144_CEA_AFTER_NETTING;
	}
	public void setR144_CEA_AFTER_NETTING(BigDecimal r144_CEA_AFTER_NETTING) {
		R144_CEA_AFTER_NETTING = r144_CEA_AFTER_NETTING;
	}
	public BigDecimal getR144_CEA_ELIGIBLE_CRM_SUB() {
		return R144_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR144_CEA_ELIGIBLE_CRM_SUB(BigDecimal r144_CEA_ELIGIBLE_CRM_SUB) {
		R144_CEA_ELIGIBLE_CRM_SUB = r144_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR144_GUARANTEE_ELIGIBLE() {
		return R144_GUARANTEE_ELIGIBLE;
	}
	public void setR144_GUARANTEE_ELIGIBLE(BigDecimal r144_GUARANTEE_ELIGIBLE) {
		R144_GUARANTEE_ELIGIBLE = r144_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR144_CREDIT_DERIVATIVES() {
		return R144_CREDIT_DERIVATIVES;
	}
	public void setR144_CREDIT_DERIVATIVES(BigDecimal r144_CREDIT_DERIVATIVES) {
		R144_CREDIT_DERIVATIVES = r144_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR144_EXPOSURE_COVERED_CRM() {
		return R144_EXPOSURE_COVERED_CRM;
	}
	public void setR144_EXPOSURE_COVERED_CRM(BigDecimal r144_EXPOSURE_COVERED_CRM) {
		R144_EXPOSURE_COVERED_CRM = r144_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR144_EXPOSURE_NOT_COVERED_CRM() {
		return R144_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR144_EXPOSURE_NOT_COVERED_CRM(BigDecimal r144_EXPOSURE_NOT_COVERED_CRM) {
		R144_EXPOSURE_NOT_COVERED_CRM = r144_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR144_RWA_TOTAL() {
		return R144_RWA_TOTAL;
	}
	public void setR144_RWA_TOTAL(BigDecimal r144_RWA_TOTAL) {
		R144_RWA_TOTAL = r144_RWA_TOTAL;
	}
	public BigDecimal getR144_CRM_RISK_WEIGHT() {
		return R144_CRM_RISK_WEIGHT;
	}
	public void setR144_CRM_RISK_WEIGHT(BigDecimal r144_CRM_RISK_WEIGHT) {
		R144_CRM_RISK_WEIGHT = r144_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR144_RWA_CRM_COVERED() {
		return R144_RWA_CRM_COVERED;
	}
	public void setR144_RWA_CRM_COVERED(BigDecimal r144_RWA_CRM_COVERED) {
		R144_RWA_CRM_COVERED = r144_RWA_CRM_COVERED;
	}
	public BigDecimal getR144_ORIGINAL_CP_RISK_WEIGHT() {
		return R144_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR144_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r144_ORIGINAL_CP_RISK_WEIGHT) {
		R144_ORIGINAL_CP_RISK_WEIGHT = r144_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR144_RWA_NOT_COVERED() {
		return R144_RWA_NOT_COVERED;
	}
	public void setR144_RWA_NOT_COVERED(BigDecimal r144_RWA_NOT_COVERED) {
		R144_RWA_NOT_COVERED = r144_RWA_NOT_COVERED;
	}
	public BigDecimal getR144_COLLATERAL_CEA_ELIGIBLE() {
		return R144_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR144_COLLATERAL_CEA_ELIGIBLE(BigDecimal r144_COLLATERAL_CEA_ELIGIBLE) {
		R144_COLLATERAL_CEA_ELIGIBLE = r144_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR144_CEA_AFTER_VOL_ADJ() {
		return R144_CEA_AFTER_VOL_ADJ;
	}
	public void setR144_CEA_AFTER_VOL_ADJ(BigDecimal r144_CEA_AFTER_VOL_ADJ) {
		R144_CEA_AFTER_VOL_ADJ = r144_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR144_COLL_CASH() {
		return R144_COLL_CASH;
	}
	public void setR144_COLL_CASH(BigDecimal r144_COLL_CASH) {
		R144_COLL_CASH = r144_COLL_CASH;
	}
	public BigDecimal getR144_COLL_TBILLS() {
		return R144_COLL_TBILLS;
	}
	public void setR144_COLL_TBILLS(BigDecimal r144_COLL_TBILLS) {
		R144_COLL_TBILLS = r144_COLL_TBILLS;
	}
	public BigDecimal getR144_COLL_DEBT_SEC() {
		return R144_COLL_DEBT_SEC;
	}
	public void setR144_COLL_DEBT_SEC(BigDecimal r144_COLL_DEBT_SEC) {
		R144_COLL_DEBT_SEC = r144_COLL_DEBT_SEC;
	}
	public BigDecimal getR144_COLL_EQUITIES() {
		return R144_COLL_EQUITIES;
	}
	public void setR144_COLL_EQUITIES(BigDecimal r144_COLL_EQUITIES) {
		R144_COLL_EQUITIES = r144_COLL_EQUITIES;
	}
	public BigDecimal getR144_COLL_MUTUAL_FUNDS() {
		return R144_COLL_MUTUAL_FUNDS;
	}
	public void setR144_COLL_MUTUAL_FUNDS(BigDecimal r144_COLL_MUTUAL_FUNDS) {
		R144_COLL_MUTUAL_FUNDS = r144_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR144_COLL_TOTAL() {
		return R144_COLL_TOTAL;
	}
	public void setR144_COLL_TOTAL(BigDecimal r144_COLL_TOTAL) {
		R144_COLL_TOTAL = r144_COLL_TOTAL;
	}
	public BigDecimal getR144_CEA_AFTER_CRM() {
		return R144_CEA_AFTER_CRM;
	}
	public void setR144_CEA_AFTER_CRM(BigDecimal r144_CEA_AFTER_CRM) {
		R144_CEA_AFTER_CRM = r144_CEA_AFTER_CRM;
	}
	public BigDecimal getR144_RWA_CEA_NOT_COVERED() {
		return R144_RWA_CEA_NOT_COVERED;
	}
	public void setR144_RWA_CEA_NOT_COVERED(BigDecimal r144_RWA_CEA_NOT_COVERED) {
		R144_RWA_CEA_NOT_COVERED = r144_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR144_UNSECURED_CEA() {
		return R144_UNSECURED_CEA;
	}
	public void setR144_UNSECURED_CEA(BigDecimal r144_UNSECURED_CEA) {
		R144_UNSECURED_CEA = r144_UNSECURED_CEA;
	}
	public BigDecimal getR144_RWA_UNSECURED_CEA() {
		return R144_RWA_UNSECURED_CEA;
	}
	public void setR144_RWA_UNSECURED_CEA(BigDecimal r144_RWA_UNSECURED_CEA) {
		R144_RWA_UNSECURED_CEA = r144_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR145_NOMINAL_PRINCIPAL_AMT() {
		return R145_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR145_NOMINAL_PRINCIPAL_AMT(BigDecimal r145_NOMINAL_PRINCIPAL_AMT) {
		R145_NOMINAL_PRINCIPAL_AMT = r145_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR145_CCF_PCT() {
		return R145_CCF_PCT;
	}
	public void setR145_CCF_PCT(BigDecimal r145_CCF_PCT) {
		R145_CCF_PCT = r145_CCF_PCT;
	}
	public BigDecimal getR145_CREDIT_EQUIVALENT_AMT() {
		return R145_CREDIT_EQUIVALENT_AMT;
	}
	public void setR145_CREDIT_EQUIVALENT_AMT(BigDecimal r145_CREDIT_EQUIVALENT_AMT) {
		R145_CREDIT_EQUIVALENT_AMT = r145_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR145_CEA_ELIGIBLE_NETTING_CP() {
		return R145_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR145_CEA_ELIGIBLE_NETTING_CP(BigDecimal r145_CEA_ELIGIBLE_NETTING_CP) {
		R145_CEA_ELIGIBLE_NETTING_CP = r145_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR145_CEA_AFTER_NETTING() {
		return R145_CEA_AFTER_NETTING;
	}
	public void setR145_CEA_AFTER_NETTING(BigDecimal r145_CEA_AFTER_NETTING) {
		R145_CEA_AFTER_NETTING = r145_CEA_AFTER_NETTING;
	}
	public BigDecimal getR145_CEA_ELIGIBLE_CRM_SUB() {
		return R145_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR145_CEA_ELIGIBLE_CRM_SUB(BigDecimal r145_CEA_ELIGIBLE_CRM_SUB) {
		R145_CEA_ELIGIBLE_CRM_SUB = r145_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR145_GUARANTEE_ELIGIBLE() {
		return R145_GUARANTEE_ELIGIBLE;
	}
	public void setR145_GUARANTEE_ELIGIBLE(BigDecimal r145_GUARANTEE_ELIGIBLE) {
		R145_GUARANTEE_ELIGIBLE = r145_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR145_CREDIT_DERIVATIVES() {
		return R145_CREDIT_DERIVATIVES;
	}
	public void setR145_CREDIT_DERIVATIVES(BigDecimal r145_CREDIT_DERIVATIVES) {
		R145_CREDIT_DERIVATIVES = r145_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR145_EXPOSURE_COVERED_CRM() {
		return R145_EXPOSURE_COVERED_CRM;
	}
	public void setR145_EXPOSURE_COVERED_CRM(BigDecimal r145_EXPOSURE_COVERED_CRM) {
		R145_EXPOSURE_COVERED_CRM = r145_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR145_EXPOSURE_NOT_COVERED_CRM() {
		return R145_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR145_EXPOSURE_NOT_COVERED_CRM(BigDecimal r145_EXPOSURE_NOT_COVERED_CRM) {
		R145_EXPOSURE_NOT_COVERED_CRM = r145_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR145_RWA_TOTAL() {
		return R145_RWA_TOTAL;
	}
	public void setR145_RWA_TOTAL(BigDecimal r145_RWA_TOTAL) {
		R145_RWA_TOTAL = r145_RWA_TOTAL;
	}
	public BigDecimal getR145_CRM_RISK_WEIGHT() {
		return R145_CRM_RISK_WEIGHT;
	}
	public void setR145_CRM_RISK_WEIGHT(BigDecimal r145_CRM_RISK_WEIGHT) {
		R145_CRM_RISK_WEIGHT = r145_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR145_RWA_CRM_COVERED() {
		return R145_RWA_CRM_COVERED;
	}
	public void setR145_RWA_CRM_COVERED(BigDecimal r145_RWA_CRM_COVERED) {
		R145_RWA_CRM_COVERED = r145_RWA_CRM_COVERED;
	}
	public BigDecimal getR145_ORIGINAL_CP_RISK_WEIGHT() {
		return R145_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR145_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r145_ORIGINAL_CP_RISK_WEIGHT) {
		R145_ORIGINAL_CP_RISK_WEIGHT = r145_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR145_RWA_NOT_COVERED() {
		return R145_RWA_NOT_COVERED;
	}
	public void setR145_RWA_NOT_COVERED(BigDecimal r145_RWA_NOT_COVERED) {
		R145_RWA_NOT_COVERED = r145_RWA_NOT_COVERED;
	}
	public BigDecimal getR145_COLLATERAL_CEA_ELIGIBLE() {
		return R145_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR145_COLLATERAL_CEA_ELIGIBLE(BigDecimal r145_COLLATERAL_CEA_ELIGIBLE) {
		R145_COLLATERAL_CEA_ELIGIBLE = r145_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR145_CEA_AFTER_VOL_ADJ() {
		return R145_CEA_AFTER_VOL_ADJ;
	}
	public void setR145_CEA_AFTER_VOL_ADJ(BigDecimal r145_CEA_AFTER_VOL_ADJ) {
		R145_CEA_AFTER_VOL_ADJ = r145_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR145_COLL_CASH() {
		return R145_COLL_CASH;
	}
	public void setR145_COLL_CASH(BigDecimal r145_COLL_CASH) {
		R145_COLL_CASH = r145_COLL_CASH;
	}
	public BigDecimal getR145_COLL_TBILLS() {
		return R145_COLL_TBILLS;
	}
	public void setR145_COLL_TBILLS(BigDecimal r145_COLL_TBILLS) {
		R145_COLL_TBILLS = r145_COLL_TBILLS;
	}
	public BigDecimal getR145_COLL_DEBT_SEC() {
		return R145_COLL_DEBT_SEC;
	}
	public void setR145_COLL_DEBT_SEC(BigDecimal r145_COLL_DEBT_SEC) {
		R145_COLL_DEBT_SEC = r145_COLL_DEBT_SEC;
	}
	public BigDecimal getR145_COLL_EQUITIES() {
		return R145_COLL_EQUITIES;
	}
	public void setR145_COLL_EQUITIES(BigDecimal r145_COLL_EQUITIES) {
		R145_COLL_EQUITIES = r145_COLL_EQUITIES;
	}
	public BigDecimal getR145_COLL_MUTUAL_FUNDS() {
		return R145_COLL_MUTUAL_FUNDS;
	}
	public void setR145_COLL_MUTUAL_FUNDS(BigDecimal r145_COLL_MUTUAL_FUNDS) {
		R145_COLL_MUTUAL_FUNDS = r145_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR145_COLL_TOTAL() {
		return R145_COLL_TOTAL;
	}
	public void setR145_COLL_TOTAL(BigDecimal r145_COLL_TOTAL) {
		R145_COLL_TOTAL = r145_COLL_TOTAL;
	}
	public BigDecimal getR145_CEA_AFTER_CRM() {
		return R145_CEA_AFTER_CRM;
	}
	public void setR145_CEA_AFTER_CRM(BigDecimal r145_CEA_AFTER_CRM) {
		R145_CEA_AFTER_CRM = r145_CEA_AFTER_CRM;
	}
	public BigDecimal getR145_RWA_CEA_NOT_COVERED() {
		return R145_RWA_CEA_NOT_COVERED;
	}
	public void setR145_RWA_CEA_NOT_COVERED(BigDecimal r145_RWA_CEA_NOT_COVERED) {
		R145_RWA_CEA_NOT_COVERED = r145_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR145_UNSECURED_CEA() {
		return R145_UNSECURED_CEA;
	}
	public void setR145_UNSECURED_CEA(BigDecimal r145_UNSECURED_CEA) {
		R145_UNSECURED_CEA = r145_UNSECURED_CEA;
	}
	public BigDecimal getR145_RWA_UNSECURED_CEA() {
		return R145_RWA_UNSECURED_CEA;
	}
	public void setR145_RWA_UNSECURED_CEA(BigDecimal r145_RWA_UNSECURED_CEA) {
		R145_RWA_UNSECURED_CEA = r145_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR146_NOMINAL_PRINCIPAL_AMT() {
		return R146_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR146_NOMINAL_PRINCIPAL_AMT(BigDecimal r146_NOMINAL_PRINCIPAL_AMT) {
		R146_NOMINAL_PRINCIPAL_AMT = r146_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR146_CCF_PCT() {
		return R146_CCF_PCT;
	}
	public void setR146_CCF_PCT(BigDecimal r146_CCF_PCT) {
		R146_CCF_PCT = r146_CCF_PCT;
	}
	public BigDecimal getR146_CREDIT_EQUIVALENT_AMT() {
		return R146_CREDIT_EQUIVALENT_AMT;
	}
	public void setR146_CREDIT_EQUIVALENT_AMT(BigDecimal r146_CREDIT_EQUIVALENT_AMT) {
		R146_CREDIT_EQUIVALENT_AMT = r146_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR146_CEA_ELIGIBLE_NETTING_CP() {
		return R146_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR146_CEA_ELIGIBLE_NETTING_CP(BigDecimal r146_CEA_ELIGIBLE_NETTING_CP) {
		R146_CEA_ELIGIBLE_NETTING_CP = r146_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR146_CEA_AFTER_NETTING() {
		return R146_CEA_AFTER_NETTING;
	}
	public void setR146_CEA_AFTER_NETTING(BigDecimal r146_CEA_AFTER_NETTING) {
		R146_CEA_AFTER_NETTING = r146_CEA_AFTER_NETTING;
	}
	public BigDecimal getR146_CEA_ELIGIBLE_CRM_SUB() {
		return R146_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR146_CEA_ELIGIBLE_CRM_SUB(BigDecimal r146_CEA_ELIGIBLE_CRM_SUB) {
		R146_CEA_ELIGIBLE_CRM_SUB = r146_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR146_GUARANTEE_ELIGIBLE() {
		return R146_GUARANTEE_ELIGIBLE;
	}
	public void setR146_GUARANTEE_ELIGIBLE(BigDecimal r146_GUARANTEE_ELIGIBLE) {
		R146_GUARANTEE_ELIGIBLE = r146_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR146_CREDIT_DERIVATIVES() {
		return R146_CREDIT_DERIVATIVES;
	}
	public void setR146_CREDIT_DERIVATIVES(BigDecimal r146_CREDIT_DERIVATIVES) {
		R146_CREDIT_DERIVATIVES = r146_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR146_EXPOSURE_COVERED_CRM() {
		return R146_EXPOSURE_COVERED_CRM;
	}
	public void setR146_EXPOSURE_COVERED_CRM(BigDecimal r146_EXPOSURE_COVERED_CRM) {
		R146_EXPOSURE_COVERED_CRM = r146_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR146_EXPOSURE_NOT_COVERED_CRM() {
		return R146_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR146_EXPOSURE_NOT_COVERED_CRM(BigDecimal r146_EXPOSURE_NOT_COVERED_CRM) {
		R146_EXPOSURE_NOT_COVERED_CRM = r146_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR146_RWA_TOTAL() {
		return R146_RWA_TOTAL;
	}
	public void setR146_RWA_TOTAL(BigDecimal r146_RWA_TOTAL) {
		R146_RWA_TOTAL = r146_RWA_TOTAL;
	}
	public BigDecimal getR146_CRM_RISK_WEIGHT() {
		return R146_CRM_RISK_WEIGHT;
	}
	public void setR146_CRM_RISK_WEIGHT(BigDecimal r146_CRM_RISK_WEIGHT) {
		R146_CRM_RISK_WEIGHT = r146_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR146_RWA_CRM_COVERED() {
		return R146_RWA_CRM_COVERED;
	}
	public void setR146_RWA_CRM_COVERED(BigDecimal r146_RWA_CRM_COVERED) {
		R146_RWA_CRM_COVERED = r146_RWA_CRM_COVERED;
	}
	public BigDecimal getR146_ORIGINAL_CP_RISK_WEIGHT() {
		return R146_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR146_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r146_ORIGINAL_CP_RISK_WEIGHT) {
		R146_ORIGINAL_CP_RISK_WEIGHT = r146_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR146_RWA_NOT_COVERED() {
		return R146_RWA_NOT_COVERED;
	}
	public void setR146_RWA_NOT_COVERED(BigDecimal r146_RWA_NOT_COVERED) {
		R146_RWA_NOT_COVERED = r146_RWA_NOT_COVERED;
	}
	public BigDecimal getR146_COLLATERAL_CEA_ELIGIBLE() {
		return R146_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR146_COLLATERAL_CEA_ELIGIBLE(BigDecimal r146_COLLATERAL_CEA_ELIGIBLE) {
		R146_COLLATERAL_CEA_ELIGIBLE = r146_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR146_CEA_AFTER_VOL_ADJ() {
		return R146_CEA_AFTER_VOL_ADJ;
	}
	public void setR146_CEA_AFTER_VOL_ADJ(BigDecimal r146_CEA_AFTER_VOL_ADJ) {
		R146_CEA_AFTER_VOL_ADJ = r146_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR146_COLL_CASH() {
		return R146_COLL_CASH;
	}
	public void setR146_COLL_CASH(BigDecimal r146_COLL_CASH) {
		R146_COLL_CASH = r146_COLL_CASH;
	}
	public BigDecimal getR146_COLL_TBILLS() {
		return R146_COLL_TBILLS;
	}
	public void setR146_COLL_TBILLS(BigDecimal r146_COLL_TBILLS) {
		R146_COLL_TBILLS = r146_COLL_TBILLS;
	}
	public BigDecimal getR146_COLL_DEBT_SEC() {
		return R146_COLL_DEBT_SEC;
	}
	public void setR146_COLL_DEBT_SEC(BigDecimal r146_COLL_DEBT_SEC) {
		R146_COLL_DEBT_SEC = r146_COLL_DEBT_SEC;
	}
	public BigDecimal getR146_COLL_EQUITIES() {
		return R146_COLL_EQUITIES;
	}
	public void setR146_COLL_EQUITIES(BigDecimal r146_COLL_EQUITIES) {
		R146_COLL_EQUITIES = r146_COLL_EQUITIES;
	}
	public BigDecimal getR146_COLL_MUTUAL_FUNDS() {
		return R146_COLL_MUTUAL_FUNDS;
	}
	public void setR146_COLL_MUTUAL_FUNDS(BigDecimal r146_COLL_MUTUAL_FUNDS) {
		R146_COLL_MUTUAL_FUNDS = r146_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR146_COLL_TOTAL() {
		return R146_COLL_TOTAL;
	}
	public void setR146_COLL_TOTAL(BigDecimal r146_COLL_TOTAL) {
		R146_COLL_TOTAL = r146_COLL_TOTAL;
	}
	public BigDecimal getR146_CEA_AFTER_CRM() {
		return R146_CEA_AFTER_CRM;
	}
	public void setR146_CEA_AFTER_CRM(BigDecimal r146_CEA_AFTER_CRM) {
		R146_CEA_AFTER_CRM = r146_CEA_AFTER_CRM;
	}
	public BigDecimal getR146_RWA_CEA_NOT_COVERED() {
		return R146_RWA_CEA_NOT_COVERED;
	}
	public void setR146_RWA_CEA_NOT_COVERED(BigDecimal r146_RWA_CEA_NOT_COVERED) {
		R146_RWA_CEA_NOT_COVERED = r146_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR146_UNSECURED_CEA() {
		return R146_UNSECURED_CEA;
	}
	public void setR146_UNSECURED_CEA(BigDecimal r146_UNSECURED_CEA) {
		R146_UNSECURED_CEA = r146_UNSECURED_CEA;
	}
	public BigDecimal getR146_RWA_UNSECURED_CEA() {
		return R146_RWA_UNSECURED_CEA;
	}
	public void setR146_RWA_UNSECURED_CEA(BigDecimal r146_RWA_UNSECURED_CEA) {
		R146_RWA_UNSECURED_CEA = r146_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR147_NOMINAL_PRINCIPAL_AMT() {
		return R147_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR147_NOMINAL_PRINCIPAL_AMT(BigDecimal r147_NOMINAL_PRINCIPAL_AMT) {
		R147_NOMINAL_PRINCIPAL_AMT = r147_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR147_CCF_PCT() {
		return R147_CCF_PCT;
	}
	public void setR147_CCF_PCT(BigDecimal r147_CCF_PCT) {
		R147_CCF_PCT = r147_CCF_PCT;
	}
	public BigDecimal getR147_CREDIT_EQUIVALENT_AMT() {
		return R147_CREDIT_EQUIVALENT_AMT;
	}
	public void setR147_CREDIT_EQUIVALENT_AMT(BigDecimal r147_CREDIT_EQUIVALENT_AMT) {
		R147_CREDIT_EQUIVALENT_AMT = r147_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR147_CEA_ELIGIBLE_NETTING_CP() {
		return R147_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR147_CEA_ELIGIBLE_NETTING_CP(BigDecimal r147_CEA_ELIGIBLE_NETTING_CP) {
		R147_CEA_ELIGIBLE_NETTING_CP = r147_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR147_CEA_AFTER_NETTING() {
		return R147_CEA_AFTER_NETTING;
	}
	public void setR147_CEA_AFTER_NETTING(BigDecimal r147_CEA_AFTER_NETTING) {
		R147_CEA_AFTER_NETTING = r147_CEA_AFTER_NETTING;
	}
	public BigDecimal getR147_CEA_ELIGIBLE_CRM_SUB() {
		return R147_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR147_CEA_ELIGIBLE_CRM_SUB(BigDecimal r147_CEA_ELIGIBLE_CRM_SUB) {
		R147_CEA_ELIGIBLE_CRM_SUB = r147_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR147_GUARANTEE_ELIGIBLE() {
		return R147_GUARANTEE_ELIGIBLE;
	}
	public void setR147_GUARANTEE_ELIGIBLE(BigDecimal r147_GUARANTEE_ELIGIBLE) {
		R147_GUARANTEE_ELIGIBLE = r147_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR147_CREDIT_DERIVATIVES() {
		return R147_CREDIT_DERIVATIVES;
	}
	public void setR147_CREDIT_DERIVATIVES(BigDecimal r147_CREDIT_DERIVATIVES) {
		R147_CREDIT_DERIVATIVES = r147_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR147_EXPOSURE_COVERED_CRM() {
		return R147_EXPOSURE_COVERED_CRM;
	}
	public void setR147_EXPOSURE_COVERED_CRM(BigDecimal r147_EXPOSURE_COVERED_CRM) {
		R147_EXPOSURE_COVERED_CRM = r147_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR147_EXPOSURE_NOT_COVERED_CRM() {
		return R147_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR147_EXPOSURE_NOT_COVERED_CRM(BigDecimal r147_EXPOSURE_NOT_COVERED_CRM) {
		R147_EXPOSURE_NOT_COVERED_CRM = r147_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR147_RWA_TOTAL() {
		return R147_RWA_TOTAL;
	}
	public void setR147_RWA_TOTAL(BigDecimal r147_RWA_TOTAL) {
		R147_RWA_TOTAL = r147_RWA_TOTAL;
	}
	public BigDecimal getR147_CRM_RISK_WEIGHT() {
		return R147_CRM_RISK_WEIGHT;
	}
	public void setR147_CRM_RISK_WEIGHT(BigDecimal r147_CRM_RISK_WEIGHT) {
		R147_CRM_RISK_WEIGHT = r147_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR147_RWA_CRM_COVERED() {
		return R147_RWA_CRM_COVERED;
	}
	public void setR147_RWA_CRM_COVERED(BigDecimal r147_RWA_CRM_COVERED) {
		R147_RWA_CRM_COVERED = r147_RWA_CRM_COVERED;
	}
	public BigDecimal getR147_ORIGINAL_CP_RISK_WEIGHT() {
		return R147_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR147_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r147_ORIGINAL_CP_RISK_WEIGHT) {
		R147_ORIGINAL_CP_RISK_WEIGHT = r147_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR147_RWA_NOT_COVERED() {
		return R147_RWA_NOT_COVERED;
	}
	public void setR147_RWA_NOT_COVERED(BigDecimal r147_RWA_NOT_COVERED) {
		R147_RWA_NOT_COVERED = r147_RWA_NOT_COVERED;
	}
	public BigDecimal getR147_COLLATERAL_CEA_ELIGIBLE() {
		return R147_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR147_COLLATERAL_CEA_ELIGIBLE(BigDecimal r147_COLLATERAL_CEA_ELIGIBLE) {
		R147_COLLATERAL_CEA_ELIGIBLE = r147_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR147_CEA_AFTER_VOL_ADJ() {
		return R147_CEA_AFTER_VOL_ADJ;
	}
	public void setR147_CEA_AFTER_VOL_ADJ(BigDecimal r147_CEA_AFTER_VOL_ADJ) {
		R147_CEA_AFTER_VOL_ADJ = r147_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR147_COLL_CASH() {
		return R147_COLL_CASH;
	}
	public void setR147_COLL_CASH(BigDecimal r147_COLL_CASH) {
		R147_COLL_CASH = r147_COLL_CASH;
	}
	public BigDecimal getR147_COLL_TBILLS() {
		return R147_COLL_TBILLS;
	}
	public void setR147_COLL_TBILLS(BigDecimal r147_COLL_TBILLS) {
		R147_COLL_TBILLS = r147_COLL_TBILLS;
	}
	public BigDecimal getR147_COLL_DEBT_SEC() {
		return R147_COLL_DEBT_SEC;
	}
	public void setR147_COLL_DEBT_SEC(BigDecimal r147_COLL_DEBT_SEC) {
		R147_COLL_DEBT_SEC = r147_COLL_DEBT_SEC;
	}
	public BigDecimal getR147_COLL_EQUITIES() {
		return R147_COLL_EQUITIES;
	}
	public void setR147_COLL_EQUITIES(BigDecimal r147_COLL_EQUITIES) {
		R147_COLL_EQUITIES = r147_COLL_EQUITIES;
	}
	public BigDecimal getR147_COLL_MUTUAL_FUNDS() {
		return R147_COLL_MUTUAL_FUNDS;
	}
	public void setR147_COLL_MUTUAL_FUNDS(BigDecimal r147_COLL_MUTUAL_FUNDS) {
		R147_COLL_MUTUAL_FUNDS = r147_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR147_COLL_TOTAL() {
		return R147_COLL_TOTAL;
	}
	public void setR147_COLL_TOTAL(BigDecimal r147_COLL_TOTAL) {
		R147_COLL_TOTAL = r147_COLL_TOTAL;
	}
	public BigDecimal getR147_CEA_AFTER_CRM() {
		return R147_CEA_AFTER_CRM;
	}
	public void setR147_CEA_AFTER_CRM(BigDecimal r147_CEA_AFTER_CRM) {
		R147_CEA_AFTER_CRM = r147_CEA_AFTER_CRM;
	}
	public BigDecimal getR147_RWA_CEA_NOT_COVERED() {
		return R147_RWA_CEA_NOT_COVERED;
	}
	public void setR147_RWA_CEA_NOT_COVERED(BigDecimal r147_RWA_CEA_NOT_COVERED) {
		R147_RWA_CEA_NOT_COVERED = r147_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR147_UNSECURED_CEA() {
		return R147_UNSECURED_CEA;
	}
	public void setR147_UNSECURED_CEA(BigDecimal r147_UNSECURED_CEA) {
		R147_UNSECURED_CEA = r147_UNSECURED_CEA;
	}
	public BigDecimal getR147_RWA_UNSECURED_CEA() {
		return R147_RWA_UNSECURED_CEA;
	}
	public void setR147_RWA_UNSECURED_CEA(BigDecimal r147_RWA_UNSECURED_CEA) {
		R147_RWA_UNSECURED_CEA = r147_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR148_NOMINAL_PRINCIPAL_AMT() {
		return R148_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR148_NOMINAL_PRINCIPAL_AMT(BigDecimal r148_NOMINAL_PRINCIPAL_AMT) {
		R148_NOMINAL_PRINCIPAL_AMT = r148_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR148_CCF_PCT() {
		return R148_CCF_PCT;
	}
	public void setR148_CCF_PCT(BigDecimal r148_CCF_PCT) {
		R148_CCF_PCT = r148_CCF_PCT;
	}
	public BigDecimal getR148_CREDIT_EQUIVALENT_AMT() {
		return R148_CREDIT_EQUIVALENT_AMT;
	}
	public void setR148_CREDIT_EQUIVALENT_AMT(BigDecimal r148_CREDIT_EQUIVALENT_AMT) {
		R148_CREDIT_EQUIVALENT_AMT = r148_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR148_CEA_ELIGIBLE_NETTING_CP() {
		return R148_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR148_CEA_ELIGIBLE_NETTING_CP(BigDecimal r148_CEA_ELIGIBLE_NETTING_CP) {
		R148_CEA_ELIGIBLE_NETTING_CP = r148_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR148_CEA_AFTER_NETTING() {
		return R148_CEA_AFTER_NETTING;
	}
	public void setR148_CEA_AFTER_NETTING(BigDecimal r148_CEA_AFTER_NETTING) {
		R148_CEA_AFTER_NETTING = r148_CEA_AFTER_NETTING;
	}
	public BigDecimal getR148_CEA_ELIGIBLE_CRM_SUB() {
		return R148_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR148_CEA_ELIGIBLE_CRM_SUB(BigDecimal r148_CEA_ELIGIBLE_CRM_SUB) {
		R148_CEA_ELIGIBLE_CRM_SUB = r148_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR148_GUARANTEE_ELIGIBLE() {
		return R148_GUARANTEE_ELIGIBLE;
	}
	public void setR148_GUARANTEE_ELIGIBLE(BigDecimal r148_GUARANTEE_ELIGIBLE) {
		R148_GUARANTEE_ELIGIBLE = r148_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR148_CREDIT_DERIVATIVES() {
		return R148_CREDIT_DERIVATIVES;
	}
	public void setR148_CREDIT_DERIVATIVES(BigDecimal r148_CREDIT_DERIVATIVES) {
		R148_CREDIT_DERIVATIVES = r148_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR148_EXPOSURE_COVERED_CRM() {
		return R148_EXPOSURE_COVERED_CRM;
	}
	public void setR148_EXPOSURE_COVERED_CRM(BigDecimal r148_EXPOSURE_COVERED_CRM) {
		R148_EXPOSURE_COVERED_CRM = r148_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR148_EXPOSURE_NOT_COVERED_CRM() {
		return R148_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR148_EXPOSURE_NOT_COVERED_CRM(BigDecimal r148_EXPOSURE_NOT_COVERED_CRM) {
		R148_EXPOSURE_NOT_COVERED_CRM = r148_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR148_RWA_TOTAL() {
		return R148_RWA_TOTAL;
	}
	public void setR148_RWA_TOTAL(BigDecimal r148_RWA_TOTAL) {
		R148_RWA_TOTAL = r148_RWA_TOTAL;
	}
	public BigDecimal getR148_CRM_RISK_WEIGHT() {
		return R148_CRM_RISK_WEIGHT;
	}
	public void setR148_CRM_RISK_WEIGHT(BigDecimal r148_CRM_RISK_WEIGHT) {
		R148_CRM_RISK_WEIGHT = r148_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR148_RWA_CRM_COVERED() {
		return R148_RWA_CRM_COVERED;
	}
	public void setR148_RWA_CRM_COVERED(BigDecimal r148_RWA_CRM_COVERED) {
		R148_RWA_CRM_COVERED = r148_RWA_CRM_COVERED;
	}
	public BigDecimal getR148_ORIGINAL_CP_RISK_WEIGHT() {
		return R148_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR148_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r148_ORIGINAL_CP_RISK_WEIGHT) {
		R148_ORIGINAL_CP_RISK_WEIGHT = r148_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR148_RWA_NOT_COVERED() {
		return R148_RWA_NOT_COVERED;
	}
	public void setR148_RWA_NOT_COVERED(BigDecimal r148_RWA_NOT_COVERED) {
		R148_RWA_NOT_COVERED = r148_RWA_NOT_COVERED;
	}
	public BigDecimal getR148_COLLATERAL_CEA_ELIGIBLE() {
		return R148_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR148_COLLATERAL_CEA_ELIGIBLE(BigDecimal r148_COLLATERAL_CEA_ELIGIBLE) {
		R148_COLLATERAL_CEA_ELIGIBLE = r148_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR148_CEA_AFTER_VOL_ADJ() {
		return R148_CEA_AFTER_VOL_ADJ;
	}
	public void setR148_CEA_AFTER_VOL_ADJ(BigDecimal r148_CEA_AFTER_VOL_ADJ) {
		R148_CEA_AFTER_VOL_ADJ = r148_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR148_COLL_CASH() {
		return R148_COLL_CASH;
	}
	public void setR148_COLL_CASH(BigDecimal r148_COLL_CASH) {
		R148_COLL_CASH = r148_COLL_CASH;
	}
	public BigDecimal getR148_COLL_TBILLS() {
		return R148_COLL_TBILLS;
	}
	public void setR148_COLL_TBILLS(BigDecimal r148_COLL_TBILLS) {
		R148_COLL_TBILLS = r148_COLL_TBILLS;
	}
	public BigDecimal getR148_COLL_DEBT_SEC() {
		return R148_COLL_DEBT_SEC;
	}
	public void setR148_COLL_DEBT_SEC(BigDecimal r148_COLL_DEBT_SEC) {
		R148_COLL_DEBT_SEC = r148_COLL_DEBT_SEC;
	}
	public BigDecimal getR148_COLL_EQUITIES() {
		return R148_COLL_EQUITIES;
	}
	public void setR148_COLL_EQUITIES(BigDecimal r148_COLL_EQUITIES) {
		R148_COLL_EQUITIES = r148_COLL_EQUITIES;
	}
	public BigDecimal getR148_COLL_MUTUAL_FUNDS() {
		return R148_COLL_MUTUAL_FUNDS;
	}
	public void setR148_COLL_MUTUAL_FUNDS(BigDecimal r148_COLL_MUTUAL_FUNDS) {
		R148_COLL_MUTUAL_FUNDS = r148_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR148_COLL_TOTAL() {
		return R148_COLL_TOTAL;
	}
	public void setR148_COLL_TOTAL(BigDecimal r148_COLL_TOTAL) {
		R148_COLL_TOTAL = r148_COLL_TOTAL;
	}
	public BigDecimal getR148_CEA_AFTER_CRM() {
		return R148_CEA_AFTER_CRM;
	}
	public void setR148_CEA_AFTER_CRM(BigDecimal r148_CEA_AFTER_CRM) {
		R148_CEA_AFTER_CRM = r148_CEA_AFTER_CRM;
	}
	public BigDecimal getR148_RWA_CEA_NOT_COVERED() {
		return R148_RWA_CEA_NOT_COVERED;
	}
	public void setR148_RWA_CEA_NOT_COVERED(BigDecimal r148_RWA_CEA_NOT_COVERED) {
		R148_RWA_CEA_NOT_COVERED = r148_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR148_UNSECURED_CEA() {
		return R148_UNSECURED_CEA;
	}
	public void setR148_UNSECURED_CEA(BigDecimal r148_UNSECURED_CEA) {
		R148_UNSECURED_CEA = r148_UNSECURED_CEA;
	}
	public BigDecimal getR148_RWA_UNSECURED_CEA() {
		return R148_RWA_UNSECURED_CEA;
	}
	public void setR148_RWA_UNSECURED_CEA(BigDecimal r148_RWA_UNSECURED_CEA) {
		R148_RWA_UNSECURED_CEA = r148_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR149_NOMINAL_PRINCIPAL_AMT() {
		return R149_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR149_NOMINAL_PRINCIPAL_AMT(BigDecimal r149_NOMINAL_PRINCIPAL_AMT) {
		R149_NOMINAL_PRINCIPAL_AMT = r149_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR149_CCF_PCT() {
		return R149_CCF_PCT;
	}
	public void setR149_CCF_PCT(BigDecimal r149_CCF_PCT) {
		R149_CCF_PCT = r149_CCF_PCT;
	}
	public BigDecimal getR149_CREDIT_EQUIVALENT_AMT() {
		return R149_CREDIT_EQUIVALENT_AMT;
	}
	public void setR149_CREDIT_EQUIVALENT_AMT(BigDecimal r149_CREDIT_EQUIVALENT_AMT) {
		R149_CREDIT_EQUIVALENT_AMT = r149_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR149_CEA_ELIGIBLE_NETTING_CP() {
		return R149_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR149_CEA_ELIGIBLE_NETTING_CP(BigDecimal r149_CEA_ELIGIBLE_NETTING_CP) {
		R149_CEA_ELIGIBLE_NETTING_CP = r149_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR149_CEA_AFTER_NETTING() {
		return R149_CEA_AFTER_NETTING;
	}
	public void setR149_CEA_AFTER_NETTING(BigDecimal r149_CEA_AFTER_NETTING) {
		R149_CEA_AFTER_NETTING = r149_CEA_AFTER_NETTING;
	}
	public BigDecimal getR149_CEA_ELIGIBLE_CRM_SUB() {
		return R149_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR149_CEA_ELIGIBLE_CRM_SUB(BigDecimal r149_CEA_ELIGIBLE_CRM_SUB) {
		R149_CEA_ELIGIBLE_CRM_SUB = r149_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR149_GUARANTEE_ELIGIBLE() {
		return R149_GUARANTEE_ELIGIBLE;
	}
	public void setR149_GUARANTEE_ELIGIBLE(BigDecimal r149_GUARANTEE_ELIGIBLE) {
		R149_GUARANTEE_ELIGIBLE = r149_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR149_CREDIT_DERIVATIVES() {
		return R149_CREDIT_DERIVATIVES;
	}
	public void setR149_CREDIT_DERIVATIVES(BigDecimal r149_CREDIT_DERIVATIVES) {
		R149_CREDIT_DERIVATIVES = r149_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR149_EXPOSURE_COVERED_CRM() {
		return R149_EXPOSURE_COVERED_CRM;
	}
	public void setR149_EXPOSURE_COVERED_CRM(BigDecimal r149_EXPOSURE_COVERED_CRM) {
		R149_EXPOSURE_COVERED_CRM = r149_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR149_EXPOSURE_NOT_COVERED_CRM() {
		return R149_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR149_EXPOSURE_NOT_COVERED_CRM(BigDecimal r149_EXPOSURE_NOT_COVERED_CRM) {
		R149_EXPOSURE_NOT_COVERED_CRM = r149_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR149_RWA_TOTAL() {
		return R149_RWA_TOTAL;
	}
	public void setR149_RWA_TOTAL(BigDecimal r149_RWA_TOTAL) {
		R149_RWA_TOTAL = r149_RWA_TOTAL;
	}
	public BigDecimal getR149_CRM_RISK_WEIGHT() {
		return R149_CRM_RISK_WEIGHT;
	}
	public void setR149_CRM_RISK_WEIGHT(BigDecimal r149_CRM_RISK_WEIGHT) {
		R149_CRM_RISK_WEIGHT = r149_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR149_RWA_CRM_COVERED() {
		return R149_RWA_CRM_COVERED;
	}
	public void setR149_RWA_CRM_COVERED(BigDecimal r149_RWA_CRM_COVERED) {
		R149_RWA_CRM_COVERED = r149_RWA_CRM_COVERED;
	}
	public BigDecimal getR149_ORIGINAL_CP_RISK_WEIGHT() {
		return R149_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR149_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r149_ORIGINAL_CP_RISK_WEIGHT) {
		R149_ORIGINAL_CP_RISK_WEIGHT = r149_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR149_RWA_NOT_COVERED() {
		return R149_RWA_NOT_COVERED;
	}
	public void setR149_RWA_NOT_COVERED(BigDecimal r149_RWA_NOT_COVERED) {
		R149_RWA_NOT_COVERED = r149_RWA_NOT_COVERED;
	}
	public BigDecimal getR149_COLLATERAL_CEA_ELIGIBLE() {
		return R149_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR149_COLLATERAL_CEA_ELIGIBLE(BigDecimal r149_COLLATERAL_CEA_ELIGIBLE) {
		R149_COLLATERAL_CEA_ELIGIBLE = r149_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR149_CEA_AFTER_VOL_ADJ() {
		return R149_CEA_AFTER_VOL_ADJ;
	}
	public void setR149_CEA_AFTER_VOL_ADJ(BigDecimal r149_CEA_AFTER_VOL_ADJ) {
		R149_CEA_AFTER_VOL_ADJ = r149_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR149_COLL_CASH() {
		return R149_COLL_CASH;
	}
	public void setR149_COLL_CASH(BigDecimal r149_COLL_CASH) {
		R149_COLL_CASH = r149_COLL_CASH;
	}
	public BigDecimal getR149_COLL_TBILLS() {
		return R149_COLL_TBILLS;
	}
	public void setR149_COLL_TBILLS(BigDecimal r149_COLL_TBILLS) {
		R149_COLL_TBILLS = r149_COLL_TBILLS;
	}
	public BigDecimal getR149_COLL_DEBT_SEC() {
		return R149_COLL_DEBT_SEC;
	}
	public void setR149_COLL_DEBT_SEC(BigDecimal r149_COLL_DEBT_SEC) {
		R149_COLL_DEBT_SEC = r149_COLL_DEBT_SEC;
	}
	public BigDecimal getR149_COLL_EQUITIES() {
		return R149_COLL_EQUITIES;
	}
	public void setR149_COLL_EQUITIES(BigDecimal r149_COLL_EQUITIES) {
		R149_COLL_EQUITIES = r149_COLL_EQUITIES;
	}
	public BigDecimal getR149_COLL_MUTUAL_FUNDS() {
		return R149_COLL_MUTUAL_FUNDS;
	}
	public void setR149_COLL_MUTUAL_FUNDS(BigDecimal r149_COLL_MUTUAL_FUNDS) {
		R149_COLL_MUTUAL_FUNDS = r149_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR149_COLL_TOTAL() {
		return R149_COLL_TOTAL;
	}
	public void setR149_COLL_TOTAL(BigDecimal r149_COLL_TOTAL) {
		R149_COLL_TOTAL = r149_COLL_TOTAL;
	}
	public BigDecimal getR149_CEA_AFTER_CRM() {
		return R149_CEA_AFTER_CRM;
	}
	public void setR149_CEA_AFTER_CRM(BigDecimal r149_CEA_AFTER_CRM) {
		R149_CEA_AFTER_CRM = r149_CEA_AFTER_CRM;
	}
	public BigDecimal getR149_RWA_CEA_NOT_COVERED() {
		return R149_RWA_CEA_NOT_COVERED;
	}
	public void setR149_RWA_CEA_NOT_COVERED(BigDecimal r149_RWA_CEA_NOT_COVERED) {
		R149_RWA_CEA_NOT_COVERED = r149_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR149_UNSECURED_CEA() {
		return R149_UNSECURED_CEA;
	}
	public void setR149_UNSECURED_CEA(BigDecimal r149_UNSECURED_CEA) {
		R149_UNSECURED_CEA = r149_UNSECURED_CEA;
	}
	public BigDecimal getR149_RWA_UNSECURED_CEA() {
		return R149_RWA_UNSECURED_CEA;
	}
	public void setR149_RWA_UNSECURED_CEA(BigDecimal r149_RWA_UNSECURED_CEA) {
		R149_RWA_UNSECURED_CEA = r149_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR150_NOMINAL_PRINCIPAL_AMT() {
		return R150_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR150_NOMINAL_PRINCIPAL_AMT(BigDecimal r150_NOMINAL_PRINCIPAL_AMT) {
		R150_NOMINAL_PRINCIPAL_AMT = r150_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR150_CCF_PCT() {
		return R150_CCF_PCT;
	}
	public void setR150_CCF_PCT(BigDecimal r150_CCF_PCT) {
		R150_CCF_PCT = r150_CCF_PCT;
	}
	public BigDecimal getR150_CREDIT_EQUIVALENT_AMT() {
		return R150_CREDIT_EQUIVALENT_AMT;
	}
	public void setR150_CREDIT_EQUIVALENT_AMT(BigDecimal r150_CREDIT_EQUIVALENT_AMT) {
		R150_CREDIT_EQUIVALENT_AMT = r150_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR150_CEA_ELIGIBLE_NETTING_CP() {
		return R150_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR150_CEA_ELIGIBLE_NETTING_CP(BigDecimal r150_CEA_ELIGIBLE_NETTING_CP) {
		R150_CEA_ELIGIBLE_NETTING_CP = r150_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR150_CEA_AFTER_NETTING() {
		return R150_CEA_AFTER_NETTING;
	}
	public void setR150_CEA_AFTER_NETTING(BigDecimal r150_CEA_AFTER_NETTING) {
		R150_CEA_AFTER_NETTING = r150_CEA_AFTER_NETTING;
	}
	public BigDecimal getR150_CEA_ELIGIBLE_CRM_SUB() {
		return R150_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR150_CEA_ELIGIBLE_CRM_SUB(BigDecimal r150_CEA_ELIGIBLE_CRM_SUB) {
		R150_CEA_ELIGIBLE_CRM_SUB = r150_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR150_GUARANTEE_ELIGIBLE() {
		return R150_GUARANTEE_ELIGIBLE;
	}
	public void setR150_GUARANTEE_ELIGIBLE(BigDecimal r150_GUARANTEE_ELIGIBLE) {
		R150_GUARANTEE_ELIGIBLE = r150_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR150_CREDIT_DERIVATIVES() {
		return R150_CREDIT_DERIVATIVES;
	}
	public void setR150_CREDIT_DERIVATIVES(BigDecimal r150_CREDIT_DERIVATIVES) {
		R150_CREDIT_DERIVATIVES = r150_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR150_EXPOSURE_COVERED_CRM() {
		return R150_EXPOSURE_COVERED_CRM;
	}
	public void setR150_EXPOSURE_COVERED_CRM(BigDecimal r150_EXPOSURE_COVERED_CRM) {
		R150_EXPOSURE_COVERED_CRM = r150_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR150_EXPOSURE_NOT_COVERED_CRM() {
		return R150_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR150_EXPOSURE_NOT_COVERED_CRM(BigDecimal r150_EXPOSURE_NOT_COVERED_CRM) {
		R150_EXPOSURE_NOT_COVERED_CRM = r150_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR150_RWA_TOTAL() {
		return R150_RWA_TOTAL;
	}
	public void setR150_RWA_TOTAL(BigDecimal r150_RWA_TOTAL) {
		R150_RWA_TOTAL = r150_RWA_TOTAL;
	}
	public BigDecimal getR150_CRM_RISK_WEIGHT() {
		return R150_CRM_RISK_WEIGHT;
	}
	public void setR150_CRM_RISK_WEIGHT(BigDecimal r150_CRM_RISK_WEIGHT) {
		R150_CRM_RISK_WEIGHT = r150_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR150_RWA_CRM_COVERED() {
		return R150_RWA_CRM_COVERED;
	}
	public void setR150_RWA_CRM_COVERED(BigDecimal r150_RWA_CRM_COVERED) {
		R150_RWA_CRM_COVERED = r150_RWA_CRM_COVERED;
	}
	public BigDecimal getR150_ORIGINAL_CP_RISK_WEIGHT() {
		return R150_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR150_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r150_ORIGINAL_CP_RISK_WEIGHT) {
		R150_ORIGINAL_CP_RISK_WEIGHT = r150_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR150_RWA_NOT_COVERED() {
		return R150_RWA_NOT_COVERED;
	}
	public void setR150_RWA_NOT_COVERED(BigDecimal r150_RWA_NOT_COVERED) {
		R150_RWA_NOT_COVERED = r150_RWA_NOT_COVERED;
	}
	public BigDecimal getR150_COLLATERAL_CEA_ELIGIBLE() {
		return R150_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR150_COLLATERAL_CEA_ELIGIBLE(BigDecimal r150_COLLATERAL_CEA_ELIGIBLE) {
		R150_COLLATERAL_CEA_ELIGIBLE = r150_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR150_CEA_AFTER_VOL_ADJ() {
		return R150_CEA_AFTER_VOL_ADJ;
	}
	public void setR150_CEA_AFTER_VOL_ADJ(BigDecimal r150_CEA_AFTER_VOL_ADJ) {
		R150_CEA_AFTER_VOL_ADJ = r150_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR150_COLL_CASH() {
		return R150_COLL_CASH;
	}
	public void setR150_COLL_CASH(BigDecimal r150_COLL_CASH) {
		R150_COLL_CASH = r150_COLL_CASH;
	}
	public BigDecimal getR150_COLL_TBILLS() {
		return R150_COLL_TBILLS;
	}
	public void setR150_COLL_TBILLS(BigDecimal r150_COLL_TBILLS) {
		R150_COLL_TBILLS = r150_COLL_TBILLS;
	}
	public BigDecimal getR150_COLL_DEBT_SEC() {
		return R150_COLL_DEBT_SEC;
	}
	public void setR150_COLL_DEBT_SEC(BigDecimal r150_COLL_DEBT_SEC) {
		R150_COLL_DEBT_SEC = r150_COLL_DEBT_SEC;
	}
	public BigDecimal getR150_COLL_EQUITIES() {
		return R150_COLL_EQUITIES;
	}
	public void setR150_COLL_EQUITIES(BigDecimal r150_COLL_EQUITIES) {
		R150_COLL_EQUITIES = r150_COLL_EQUITIES;
	}
	public BigDecimal getR150_COLL_MUTUAL_FUNDS() {
		return R150_COLL_MUTUAL_FUNDS;
	}
	public void setR150_COLL_MUTUAL_FUNDS(BigDecimal r150_COLL_MUTUAL_FUNDS) {
		R150_COLL_MUTUAL_FUNDS = r150_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR150_COLL_TOTAL() {
		return R150_COLL_TOTAL;
	}
	public void setR150_COLL_TOTAL(BigDecimal r150_COLL_TOTAL) {
		R150_COLL_TOTAL = r150_COLL_TOTAL;
	}
	public BigDecimal getR150_CEA_AFTER_CRM() {
		return R150_CEA_AFTER_CRM;
	}
	public void setR150_CEA_AFTER_CRM(BigDecimal r150_CEA_AFTER_CRM) {
		R150_CEA_AFTER_CRM = r150_CEA_AFTER_CRM;
	}
	public BigDecimal getR150_RWA_CEA_NOT_COVERED() {
		return R150_RWA_CEA_NOT_COVERED;
	}
	public void setR150_RWA_CEA_NOT_COVERED(BigDecimal r150_RWA_CEA_NOT_COVERED) {
		R150_RWA_CEA_NOT_COVERED = r150_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR150_UNSECURED_CEA() {
		return R150_UNSECURED_CEA;
	}
	public void setR150_UNSECURED_CEA(BigDecimal r150_UNSECURED_CEA) {
		R150_UNSECURED_CEA = r150_UNSECURED_CEA;
	}
	public BigDecimal getR150_RWA_UNSECURED_CEA() {
		return R150_RWA_UNSECURED_CEA;
	}
	public void setR150_RWA_UNSECURED_CEA(BigDecimal r150_RWA_UNSECURED_CEA) {
		R150_RWA_UNSECURED_CEA = r150_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR151_NOMINAL_PRINCIPAL_AMT() {
		return R151_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR151_NOMINAL_PRINCIPAL_AMT(BigDecimal r151_NOMINAL_PRINCIPAL_AMT) {
		R151_NOMINAL_PRINCIPAL_AMT = r151_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR151_CCF_PCT() {
		return R151_CCF_PCT;
	}
	public void setR151_CCF_PCT(BigDecimal r151_CCF_PCT) {
		R151_CCF_PCT = r151_CCF_PCT;
	}
	public BigDecimal getR151_CREDIT_EQUIVALENT_AMT() {
		return R151_CREDIT_EQUIVALENT_AMT;
	}
	public void setR151_CREDIT_EQUIVALENT_AMT(BigDecimal r151_CREDIT_EQUIVALENT_AMT) {
		R151_CREDIT_EQUIVALENT_AMT = r151_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR151_CEA_ELIGIBLE_NETTING_CP() {
		return R151_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR151_CEA_ELIGIBLE_NETTING_CP(BigDecimal r151_CEA_ELIGIBLE_NETTING_CP) {
		R151_CEA_ELIGIBLE_NETTING_CP = r151_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR151_CEA_AFTER_NETTING() {
		return R151_CEA_AFTER_NETTING;
	}
	public void setR151_CEA_AFTER_NETTING(BigDecimal r151_CEA_AFTER_NETTING) {
		R151_CEA_AFTER_NETTING = r151_CEA_AFTER_NETTING;
	}
	public BigDecimal getR151_CEA_ELIGIBLE_CRM_SUB() {
		return R151_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR151_CEA_ELIGIBLE_CRM_SUB(BigDecimal r151_CEA_ELIGIBLE_CRM_SUB) {
		R151_CEA_ELIGIBLE_CRM_SUB = r151_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR151_GUARANTEE_ELIGIBLE() {
		return R151_GUARANTEE_ELIGIBLE;
	}
	public void setR151_GUARANTEE_ELIGIBLE(BigDecimal r151_GUARANTEE_ELIGIBLE) {
		R151_GUARANTEE_ELIGIBLE = r151_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR151_CREDIT_DERIVATIVES() {
		return R151_CREDIT_DERIVATIVES;
	}
	public void setR151_CREDIT_DERIVATIVES(BigDecimal r151_CREDIT_DERIVATIVES) {
		R151_CREDIT_DERIVATIVES = r151_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR151_EXPOSURE_COVERED_CRM() {
		return R151_EXPOSURE_COVERED_CRM;
	}
	public void setR151_EXPOSURE_COVERED_CRM(BigDecimal r151_EXPOSURE_COVERED_CRM) {
		R151_EXPOSURE_COVERED_CRM = r151_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR151_EXPOSURE_NOT_COVERED_CRM() {
		return R151_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR151_EXPOSURE_NOT_COVERED_CRM(BigDecimal r151_EXPOSURE_NOT_COVERED_CRM) {
		R151_EXPOSURE_NOT_COVERED_CRM = r151_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR151_RWA_TOTAL() {
		return R151_RWA_TOTAL;
	}
	public void setR151_RWA_TOTAL(BigDecimal r151_RWA_TOTAL) {
		R151_RWA_TOTAL = r151_RWA_TOTAL;
	}
	public BigDecimal getR151_CRM_RISK_WEIGHT() {
		return R151_CRM_RISK_WEIGHT;
	}
	public void setR151_CRM_RISK_WEIGHT(BigDecimal r151_CRM_RISK_WEIGHT) {
		R151_CRM_RISK_WEIGHT = r151_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR151_RWA_CRM_COVERED() {
		return R151_RWA_CRM_COVERED;
	}
	public void setR151_RWA_CRM_COVERED(BigDecimal r151_RWA_CRM_COVERED) {
		R151_RWA_CRM_COVERED = r151_RWA_CRM_COVERED;
	}
	public BigDecimal getR151_ORIGINAL_CP_RISK_WEIGHT() {
		return R151_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR151_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r151_ORIGINAL_CP_RISK_WEIGHT) {
		R151_ORIGINAL_CP_RISK_WEIGHT = r151_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR151_RWA_NOT_COVERED() {
		return R151_RWA_NOT_COVERED;
	}
	public void setR151_RWA_NOT_COVERED(BigDecimal r151_RWA_NOT_COVERED) {
		R151_RWA_NOT_COVERED = r151_RWA_NOT_COVERED;
	}
	public BigDecimal getR151_COLLATERAL_CEA_ELIGIBLE() {
		return R151_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR151_COLLATERAL_CEA_ELIGIBLE(BigDecimal r151_COLLATERAL_CEA_ELIGIBLE) {
		R151_COLLATERAL_CEA_ELIGIBLE = r151_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR151_CEA_AFTER_VOL_ADJ() {
		return R151_CEA_AFTER_VOL_ADJ;
	}
	public void setR151_CEA_AFTER_VOL_ADJ(BigDecimal r151_CEA_AFTER_VOL_ADJ) {
		R151_CEA_AFTER_VOL_ADJ = r151_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR151_COLL_CASH() {
		return R151_COLL_CASH;
	}
	public void setR151_COLL_CASH(BigDecimal r151_COLL_CASH) {
		R151_COLL_CASH = r151_COLL_CASH;
	}
	public BigDecimal getR151_COLL_TBILLS() {
		return R151_COLL_TBILLS;
	}
	public void setR151_COLL_TBILLS(BigDecimal r151_COLL_TBILLS) {
		R151_COLL_TBILLS = r151_COLL_TBILLS;
	}
	public BigDecimal getR151_COLL_DEBT_SEC() {
		return R151_COLL_DEBT_SEC;
	}
	public void setR151_COLL_DEBT_SEC(BigDecimal r151_COLL_DEBT_SEC) {
		R151_COLL_DEBT_SEC = r151_COLL_DEBT_SEC;
	}
	public BigDecimal getR151_COLL_EQUITIES() {
		return R151_COLL_EQUITIES;
	}
	public void setR151_COLL_EQUITIES(BigDecimal r151_COLL_EQUITIES) {
		R151_COLL_EQUITIES = r151_COLL_EQUITIES;
	}
	public BigDecimal getR151_COLL_MUTUAL_FUNDS() {
		return R151_COLL_MUTUAL_FUNDS;
	}
	public void setR151_COLL_MUTUAL_FUNDS(BigDecimal r151_COLL_MUTUAL_FUNDS) {
		R151_COLL_MUTUAL_FUNDS = r151_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR151_COLL_TOTAL() {
		return R151_COLL_TOTAL;
	}
	public void setR151_COLL_TOTAL(BigDecimal r151_COLL_TOTAL) {
		R151_COLL_TOTAL = r151_COLL_TOTAL;
	}
	public BigDecimal getR151_CEA_AFTER_CRM() {
		return R151_CEA_AFTER_CRM;
	}
	public void setR151_CEA_AFTER_CRM(BigDecimal r151_CEA_AFTER_CRM) {
		R151_CEA_AFTER_CRM = r151_CEA_AFTER_CRM;
	}
	public BigDecimal getR151_RWA_CEA_NOT_COVERED() {
		return R151_RWA_CEA_NOT_COVERED;
	}
	public void setR151_RWA_CEA_NOT_COVERED(BigDecimal r151_RWA_CEA_NOT_COVERED) {
		R151_RWA_CEA_NOT_COVERED = r151_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR151_UNSECURED_CEA() {
		return R151_UNSECURED_CEA;
	}
	public void setR151_UNSECURED_CEA(BigDecimal r151_UNSECURED_CEA) {
		R151_UNSECURED_CEA = r151_UNSECURED_CEA;
	}
	public BigDecimal getR151_RWA_UNSECURED_CEA() {
		return R151_RWA_UNSECURED_CEA;
	}
	public void setR151_RWA_UNSECURED_CEA(BigDecimal r151_RWA_UNSECURED_CEA) {
		R151_RWA_UNSECURED_CEA = r151_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR152_NOMINAL_PRINCIPAL_AMT() {
		return R152_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR152_NOMINAL_PRINCIPAL_AMT(BigDecimal r152_NOMINAL_PRINCIPAL_AMT) {
		R152_NOMINAL_PRINCIPAL_AMT = r152_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR152_CCF_PCT() {
		return R152_CCF_PCT;
	}
	public void setR152_CCF_PCT(BigDecimal r152_CCF_PCT) {
		R152_CCF_PCT = r152_CCF_PCT;
	}
	public BigDecimal getR152_CREDIT_EQUIVALENT_AMT() {
		return R152_CREDIT_EQUIVALENT_AMT;
	}
	public void setR152_CREDIT_EQUIVALENT_AMT(BigDecimal r152_CREDIT_EQUIVALENT_AMT) {
		R152_CREDIT_EQUIVALENT_AMT = r152_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR152_CEA_ELIGIBLE_NETTING_CP() {
		return R152_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR152_CEA_ELIGIBLE_NETTING_CP(BigDecimal r152_CEA_ELIGIBLE_NETTING_CP) {
		R152_CEA_ELIGIBLE_NETTING_CP = r152_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR152_CEA_AFTER_NETTING() {
		return R152_CEA_AFTER_NETTING;
	}
	public void setR152_CEA_AFTER_NETTING(BigDecimal r152_CEA_AFTER_NETTING) {
		R152_CEA_AFTER_NETTING = r152_CEA_AFTER_NETTING;
	}
	public BigDecimal getR152_CEA_ELIGIBLE_CRM_SUB() {
		return R152_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR152_CEA_ELIGIBLE_CRM_SUB(BigDecimal r152_CEA_ELIGIBLE_CRM_SUB) {
		R152_CEA_ELIGIBLE_CRM_SUB = r152_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR152_GUARANTEE_ELIGIBLE() {
		return R152_GUARANTEE_ELIGIBLE;
	}
	public void setR152_GUARANTEE_ELIGIBLE(BigDecimal r152_GUARANTEE_ELIGIBLE) {
		R152_GUARANTEE_ELIGIBLE = r152_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR152_CREDIT_DERIVATIVES() {
		return R152_CREDIT_DERIVATIVES;
	}
	public void setR152_CREDIT_DERIVATIVES(BigDecimal r152_CREDIT_DERIVATIVES) {
		R152_CREDIT_DERIVATIVES = r152_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR152_EXPOSURE_COVERED_CRM() {
		return R152_EXPOSURE_COVERED_CRM;
	}
	public void setR152_EXPOSURE_COVERED_CRM(BigDecimal r152_EXPOSURE_COVERED_CRM) {
		R152_EXPOSURE_COVERED_CRM = r152_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR152_EXPOSURE_NOT_COVERED_CRM() {
		return R152_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR152_EXPOSURE_NOT_COVERED_CRM(BigDecimal r152_EXPOSURE_NOT_COVERED_CRM) {
		R152_EXPOSURE_NOT_COVERED_CRM = r152_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR152_RWA_TOTAL() {
		return R152_RWA_TOTAL;
	}
	public void setR152_RWA_TOTAL(BigDecimal r152_RWA_TOTAL) {
		R152_RWA_TOTAL = r152_RWA_TOTAL;
	}
	public BigDecimal getR152_CRM_RISK_WEIGHT() {
		return R152_CRM_RISK_WEIGHT;
	}
	public void setR152_CRM_RISK_WEIGHT(BigDecimal r152_CRM_RISK_WEIGHT) {
		R152_CRM_RISK_WEIGHT = r152_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR152_RWA_CRM_COVERED() {
		return R152_RWA_CRM_COVERED;
	}
	public void setR152_RWA_CRM_COVERED(BigDecimal r152_RWA_CRM_COVERED) {
		R152_RWA_CRM_COVERED = r152_RWA_CRM_COVERED;
	}
	public BigDecimal getR152_ORIGINAL_CP_RISK_WEIGHT() {
		return R152_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR152_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r152_ORIGINAL_CP_RISK_WEIGHT) {
		R152_ORIGINAL_CP_RISK_WEIGHT = r152_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR152_RWA_NOT_COVERED() {
		return R152_RWA_NOT_COVERED;
	}
	public void setR152_RWA_NOT_COVERED(BigDecimal r152_RWA_NOT_COVERED) {
		R152_RWA_NOT_COVERED = r152_RWA_NOT_COVERED;
	}
	public BigDecimal getR152_COLLATERAL_CEA_ELIGIBLE() {
		return R152_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR152_COLLATERAL_CEA_ELIGIBLE(BigDecimal r152_COLLATERAL_CEA_ELIGIBLE) {
		R152_COLLATERAL_CEA_ELIGIBLE = r152_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR152_CEA_AFTER_VOL_ADJ() {
		return R152_CEA_AFTER_VOL_ADJ;
	}
	public void setR152_CEA_AFTER_VOL_ADJ(BigDecimal r152_CEA_AFTER_VOL_ADJ) {
		R152_CEA_AFTER_VOL_ADJ = r152_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR152_COLL_CASH() {
		return R152_COLL_CASH;
	}
	public void setR152_COLL_CASH(BigDecimal r152_COLL_CASH) {
		R152_COLL_CASH = r152_COLL_CASH;
	}
	public BigDecimal getR152_COLL_TBILLS() {
		return R152_COLL_TBILLS;
	}
	public void setR152_COLL_TBILLS(BigDecimal r152_COLL_TBILLS) {
		R152_COLL_TBILLS = r152_COLL_TBILLS;
	}
	public BigDecimal getR152_COLL_DEBT_SEC() {
		return R152_COLL_DEBT_SEC;
	}
	public void setR152_COLL_DEBT_SEC(BigDecimal r152_COLL_DEBT_SEC) {
		R152_COLL_DEBT_SEC = r152_COLL_DEBT_SEC;
	}
	public BigDecimal getR152_COLL_EQUITIES() {
		return R152_COLL_EQUITIES;
	}
	public void setR152_COLL_EQUITIES(BigDecimal r152_COLL_EQUITIES) {
		R152_COLL_EQUITIES = r152_COLL_EQUITIES;
	}
	public BigDecimal getR152_COLL_MUTUAL_FUNDS() {
		return R152_COLL_MUTUAL_FUNDS;
	}
	public void setR152_COLL_MUTUAL_FUNDS(BigDecimal r152_COLL_MUTUAL_FUNDS) {
		R152_COLL_MUTUAL_FUNDS = r152_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR152_COLL_TOTAL() {
		return R152_COLL_TOTAL;
	}
	public void setR152_COLL_TOTAL(BigDecimal r152_COLL_TOTAL) {
		R152_COLL_TOTAL = r152_COLL_TOTAL;
	}
	public BigDecimal getR152_CEA_AFTER_CRM() {
		return R152_CEA_AFTER_CRM;
	}
	public void setR152_CEA_AFTER_CRM(BigDecimal r152_CEA_AFTER_CRM) {
		R152_CEA_AFTER_CRM = r152_CEA_AFTER_CRM;
	}
	public BigDecimal getR152_RWA_CEA_NOT_COVERED() {
		return R152_RWA_CEA_NOT_COVERED;
	}
	public void setR152_RWA_CEA_NOT_COVERED(BigDecimal r152_RWA_CEA_NOT_COVERED) {
		R152_RWA_CEA_NOT_COVERED = r152_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR152_UNSECURED_CEA() {
		return R152_UNSECURED_CEA;
	}
	public void setR152_UNSECURED_CEA(BigDecimal r152_UNSECURED_CEA) {
		R152_UNSECURED_CEA = r152_UNSECURED_CEA;
	}
	public BigDecimal getR152_RWA_UNSECURED_CEA() {
		return R152_RWA_UNSECURED_CEA;
	}
	public void setR152_RWA_UNSECURED_CEA(BigDecimal r152_RWA_UNSECURED_CEA) {
		R152_RWA_UNSECURED_CEA = r152_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR153_NOMINAL_PRINCIPAL_AMT() {
		return R153_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR153_NOMINAL_PRINCIPAL_AMT(BigDecimal r153_NOMINAL_PRINCIPAL_AMT) {
		R153_NOMINAL_PRINCIPAL_AMT = r153_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR153_CCF_PCT() {
		return R153_CCF_PCT;
	}
	public void setR153_CCF_PCT(BigDecimal r153_CCF_PCT) {
		R153_CCF_PCT = r153_CCF_PCT;
	}
	public BigDecimal getR153_CREDIT_EQUIVALENT_AMT() {
		return R153_CREDIT_EQUIVALENT_AMT;
	}
	public void setR153_CREDIT_EQUIVALENT_AMT(BigDecimal r153_CREDIT_EQUIVALENT_AMT) {
		R153_CREDIT_EQUIVALENT_AMT = r153_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR153_CEA_ELIGIBLE_NETTING_CP() {
		return R153_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR153_CEA_ELIGIBLE_NETTING_CP(BigDecimal r153_CEA_ELIGIBLE_NETTING_CP) {
		R153_CEA_ELIGIBLE_NETTING_CP = r153_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR153_CEA_AFTER_NETTING() {
		return R153_CEA_AFTER_NETTING;
	}
	public void setR153_CEA_AFTER_NETTING(BigDecimal r153_CEA_AFTER_NETTING) {
		R153_CEA_AFTER_NETTING = r153_CEA_AFTER_NETTING;
	}
	public BigDecimal getR153_CEA_ELIGIBLE_CRM_SUB() {
		return R153_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR153_CEA_ELIGIBLE_CRM_SUB(BigDecimal r153_CEA_ELIGIBLE_CRM_SUB) {
		R153_CEA_ELIGIBLE_CRM_SUB = r153_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR153_GUARANTEE_ELIGIBLE() {
		return R153_GUARANTEE_ELIGIBLE;
	}
	public void setR153_GUARANTEE_ELIGIBLE(BigDecimal r153_GUARANTEE_ELIGIBLE) {
		R153_GUARANTEE_ELIGIBLE = r153_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR153_CREDIT_DERIVATIVES() {
		return R153_CREDIT_DERIVATIVES;
	}
	public void setR153_CREDIT_DERIVATIVES(BigDecimal r153_CREDIT_DERIVATIVES) {
		R153_CREDIT_DERIVATIVES = r153_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR153_EXPOSURE_COVERED_CRM() {
		return R153_EXPOSURE_COVERED_CRM;
	}
	public void setR153_EXPOSURE_COVERED_CRM(BigDecimal r153_EXPOSURE_COVERED_CRM) {
		R153_EXPOSURE_COVERED_CRM = r153_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR153_EXPOSURE_NOT_COVERED_CRM() {
		return R153_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR153_EXPOSURE_NOT_COVERED_CRM(BigDecimal r153_EXPOSURE_NOT_COVERED_CRM) {
		R153_EXPOSURE_NOT_COVERED_CRM = r153_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR153_RWA_TOTAL() {
		return R153_RWA_TOTAL;
	}
	public void setR153_RWA_TOTAL(BigDecimal r153_RWA_TOTAL) {
		R153_RWA_TOTAL = r153_RWA_TOTAL;
	}
	public BigDecimal getR153_CRM_RISK_WEIGHT() {
		return R153_CRM_RISK_WEIGHT;
	}
	public void setR153_CRM_RISK_WEIGHT(BigDecimal r153_CRM_RISK_WEIGHT) {
		R153_CRM_RISK_WEIGHT = r153_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR153_RWA_CRM_COVERED() {
		return R153_RWA_CRM_COVERED;
	}
	public void setR153_RWA_CRM_COVERED(BigDecimal r153_RWA_CRM_COVERED) {
		R153_RWA_CRM_COVERED = r153_RWA_CRM_COVERED;
	}
	public BigDecimal getR153_ORIGINAL_CP_RISK_WEIGHT() {
		return R153_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR153_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r153_ORIGINAL_CP_RISK_WEIGHT) {
		R153_ORIGINAL_CP_RISK_WEIGHT = r153_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR153_RWA_NOT_COVERED() {
		return R153_RWA_NOT_COVERED;
	}
	public void setR153_RWA_NOT_COVERED(BigDecimal r153_RWA_NOT_COVERED) {
		R153_RWA_NOT_COVERED = r153_RWA_NOT_COVERED;
	}
	public BigDecimal getR153_COLLATERAL_CEA_ELIGIBLE() {
		return R153_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR153_COLLATERAL_CEA_ELIGIBLE(BigDecimal r153_COLLATERAL_CEA_ELIGIBLE) {
		R153_COLLATERAL_CEA_ELIGIBLE = r153_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR153_CEA_AFTER_VOL_ADJ() {
		return R153_CEA_AFTER_VOL_ADJ;
	}
	public void setR153_CEA_AFTER_VOL_ADJ(BigDecimal r153_CEA_AFTER_VOL_ADJ) {
		R153_CEA_AFTER_VOL_ADJ = r153_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR153_COLL_CASH() {
		return R153_COLL_CASH;
	}
	public void setR153_COLL_CASH(BigDecimal r153_COLL_CASH) {
		R153_COLL_CASH = r153_COLL_CASH;
	}
	public BigDecimal getR153_COLL_TBILLS() {
		return R153_COLL_TBILLS;
	}
	public void setR153_COLL_TBILLS(BigDecimal r153_COLL_TBILLS) {
		R153_COLL_TBILLS = r153_COLL_TBILLS;
	}
	public BigDecimal getR153_COLL_DEBT_SEC() {
		return R153_COLL_DEBT_SEC;
	}
	public void setR153_COLL_DEBT_SEC(BigDecimal r153_COLL_DEBT_SEC) {
		R153_COLL_DEBT_SEC = r153_COLL_DEBT_SEC;
	}
	public BigDecimal getR153_COLL_EQUITIES() {
		return R153_COLL_EQUITIES;
	}
	public void setR153_COLL_EQUITIES(BigDecimal r153_COLL_EQUITIES) {
		R153_COLL_EQUITIES = r153_COLL_EQUITIES;
	}
	public BigDecimal getR153_COLL_MUTUAL_FUNDS() {
		return R153_COLL_MUTUAL_FUNDS;
	}
	public void setR153_COLL_MUTUAL_FUNDS(BigDecimal r153_COLL_MUTUAL_FUNDS) {
		R153_COLL_MUTUAL_FUNDS = r153_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR153_COLL_TOTAL() {
		return R153_COLL_TOTAL;
	}
	public void setR153_COLL_TOTAL(BigDecimal r153_COLL_TOTAL) {
		R153_COLL_TOTAL = r153_COLL_TOTAL;
	}
	public BigDecimal getR153_CEA_AFTER_CRM() {
		return R153_CEA_AFTER_CRM;
	}
	public void setR153_CEA_AFTER_CRM(BigDecimal r153_CEA_AFTER_CRM) {
		R153_CEA_AFTER_CRM = r153_CEA_AFTER_CRM;
	}
	public BigDecimal getR153_RWA_CEA_NOT_COVERED() {
		return R153_RWA_CEA_NOT_COVERED;
	}
	public void setR153_RWA_CEA_NOT_COVERED(BigDecimal r153_RWA_CEA_NOT_COVERED) {
		R153_RWA_CEA_NOT_COVERED = r153_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR153_UNSECURED_CEA() {
		return R153_UNSECURED_CEA;
	}
	public void setR153_UNSECURED_CEA(BigDecimal r153_UNSECURED_CEA) {
		R153_UNSECURED_CEA = r153_UNSECURED_CEA;
	}
	public BigDecimal getR153_RWA_UNSECURED_CEA() {
		return R153_RWA_UNSECURED_CEA;
	}
	public void setR153_RWA_UNSECURED_CEA(BigDecimal r153_RWA_UNSECURED_CEA) {
		R153_RWA_UNSECURED_CEA = r153_RWA_UNSECURED_CEA;
	}
	public M_SRWA_12B_SUMMARY_5_NEW_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
