package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BRRS_M_SRWA_12B_SUMMARY_TABLE_8_NEW")
public class M_SRWA_12B_SUMMARY_8_NEW_ENTITY {
	
	@Id
	private Date REPORT_DATE;
	private String REPORT_VERSION;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;
	
	private BigDecimal R216_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R216_CCF_PCT;
	private BigDecimal R216_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R216_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R216_CEA_AFTER_NETTING;
	private BigDecimal R216_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R216_GUARANTEE_ELIGIBLE;
	private BigDecimal R216_CREDIT_DERIVATIVES;
	private BigDecimal R216_EXPOSURE_COVERED_CRM;
	private BigDecimal R216_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R216_RWA_TOTAL;
	private BigDecimal R216_CRM_RISK_WEIGHT;
	private BigDecimal R216_RWA_CRM_COVERED;
	private BigDecimal R216_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R216_RWA_NOT_COVERED;
	private BigDecimal R216_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R216_CEA_AFTER_VOL_ADJ;
	private BigDecimal R216_COLL_CASH;
	private BigDecimal R216_COLL_TBILLS;
	private BigDecimal R216_COLL_DEBT_SEC;
	private BigDecimal R216_COLL_EQUITIES;
	private BigDecimal R216_COLL_MUTUAL_FUNDS;
	private BigDecimal R216_COLL_TOTAL;
	private BigDecimal R216_CEA_AFTER_CRM;
	private BigDecimal R216_RWA_CEA_NOT_COVERED;
	private BigDecimal R216_UNSECURED_CEA;
	private BigDecimal R216_RWA_UNSECURED_CEA;
	private BigDecimal R217_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R217_CCF_PCT;
	private BigDecimal R217_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R217_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R217_CEA_AFTER_NETTING;
	private BigDecimal R217_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R217_GUARANTEE_ELIGIBLE;
	private BigDecimal R217_CREDIT_DERIVATIVES;
	private BigDecimal R217_EXPOSURE_COVERED_CRM;
	private BigDecimal R217_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R217_RWA_TOTAL;
	private BigDecimal R217_CRM_RISK_WEIGHT;
	private BigDecimal R217_RWA_CRM_COVERED;
	private BigDecimal R217_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R217_RWA_NOT_COVERED;
	private BigDecimal R217_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R217_CEA_AFTER_VOL_ADJ;
	private BigDecimal R217_COLL_CASH;
	private BigDecimal R217_COLL_TBILLS;
	private BigDecimal R217_COLL_DEBT_SEC;
	private BigDecimal R217_COLL_EQUITIES;
	private BigDecimal R217_COLL_MUTUAL_FUNDS;
	private BigDecimal R217_COLL_TOTAL;
	private BigDecimal R217_CEA_AFTER_CRM;
	private BigDecimal R217_RWA_CEA_NOT_COVERED;
	private BigDecimal R217_UNSECURED_CEA;
	private BigDecimal R217_RWA_UNSECURED_CEA;
	private BigDecimal R218_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R218_CCF_PCT;
	private BigDecimal R218_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R218_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R218_CEA_AFTER_NETTING;
	private BigDecimal R218_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R218_GUARANTEE_ELIGIBLE;
	private BigDecimal R218_CREDIT_DERIVATIVES;
	private BigDecimal R218_EXPOSURE_COVERED_CRM;
	private BigDecimal R218_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R218_RWA_TOTAL;
	private BigDecimal R218_CRM_RISK_WEIGHT;
	private BigDecimal R218_RWA_CRM_COVERED;
	private BigDecimal R218_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R218_RWA_NOT_COVERED;
	private BigDecimal R218_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R218_CEA_AFTER_VOL_ADJ;
	private BigDecimal R218_COLL_CASH;
	private BigDecimal R218_COLL_TBILLS;
	private BigDecimal R218_COLL_DEBT_SEC;
	private BigDecimal R218_COLL_EQUITIES;
	private BigDecimal R218_COLL_MUTUAL_FUNDS;
	private BigDecimal R218_COLL_TOTAL;
	private BigDecimal R218_CEA_AFTER_CRM;
	private BigDecimal R218_RWA_CEA_NOT_COVERED;
	private BigDecimal R218_UNSECURED_CEA;
	private BigDecimal R218_RWA_UNSECURED_CEA;
	private BigDecimal R219_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R219_CCF_PCT;
	private BigDecimal R219_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R219_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R219_CEA_AFTER_NETTING;
	private BigDecimal R219_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R219_GUARANTEE_ELIGIBLE;
	private BigDecimal R219_CREDIT_DERIVATIVES;
	private BigDecimal R219_EXPOSURE_COVERED_CRM;
	private BigDecimal R219_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R219_RWA_TOTAL;
	private BigDecimal R219_CRM_RISK_WEIGHT;
	private BigDecimal R219_RWA_CRM_COVERED;
	private BigDecimal R219_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R219_RWA_NOT_COVERED;
	private BigDecimal R219_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R219_CEA_AFTER_VOL_ADJ;
	private BigDecimal R219_COLL_CASH;
	private BigDecimal R219_COLL_TBILLS;
	private BigDecimal R219_COLL_DEBT_SEC;
	private BigDecimal R219_COLL_EQUITIES;
	private BigDecimal R219_COLL_MUTUAL_FUNDS;
	private BigDecimal R219_COLL_TOTAL;
	private BigDecimal R219_CEA_AFTER_CRM;
	private BigDecimal R219_RWA_CEA_NOT_COVERED;
	private BigDecimal R219_UNSECURED_CEA;
	private BigDecimal R219_RWA_UNSECURED_CEA;
	private BigDecimal R220_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R220_CCF_PCT;
	private BigDecimal R220_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R220_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R220_CEA_AFTER_NETTING;
	private BigDecimal R220_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R220_GUARANTEE_ELIGIBLE;
	private BigDecimal R220_CREDIT_DERIVATIVES;
	private BigDecimal R220_EXPOSURE_COVERED_CRM;
	private BigDecimal R220_EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R220_RWA_TOTAL;
	private BigDecimal R220_CRM_RISK_WEIGHT;
	private BigDecimal R220_RWA_CRM_COVERED;
	private BigDecimal R220_ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R220_RWA_NOT_COVERED;
	private BigDecimal R220_COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R220_CEA_AFTER_VOL_ADJ;
	private BigDecimal R220_COLL_CASH;
	private BigDecimal R220_COLL_TBILLS;
	private BigDecimal R220_COLL_DEBT_SEC;
	private BigDecimal R220_COLL_EQUITIES;
	private BigDecimal R220_COLL_MUTUAL_FUNDS;
	private BigDecimal R220_COLL_TOTAL;
	private BigDecimal R220_CEA_AFTER_CRM;
	private BigDecimal R220_RWA_CEA_NOT_COVERED;
	private BigDecimal R220_UNSECURED_CEA;
	private BigDecimal R220_RWA_UNSECURED_CEA;
	private BigDecimal R221_NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R221_CCF_PCT;
	private BigDecimal R221_CREDIT_EQUIVALENT_AMT;
	private BigDecimal R221_CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R221_CEA_AFTER_NETTING;
	private BigDecimal R221_CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R221GUARANTEE_ELIGIBLE;
	private BigDecimal R221CREDIT_DERIVATIVES;
	private BigDecimal R221EXPOSURE_COVERED_CRM;
	private BigDecimal R221EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R221RWA_TOTAL;
	private BigDecimal R221CRM_RISK_WEIGHT;
	private BigDecimal R221RWA_CRM_COVERED;
	private BigDecimal R221ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R221RWA_NOT_COVERED;
	private BigDecimal R221COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R221CEA_AFTER_VOL_ADJ;
	private BigDecimal R221COLL_CASH;
	private BigDecimal R221COLL_TBILLS;
	private BigDecimal R221COLL_DEBT_SEC;
	private BigDecimal R221COLL_EQUITIES;
	private BigDecimal R221COLL_MUTUAL_FUNDS;
	private BigDecimal R221COLL_TOTAL;
	private BigDecimal R221CEA_AFTER_CRM;
	private BigDecimal R221RWA_CEA_NOT_COVERED;
	private BigDecimal R221UNSECURED_CEA;
	private BigDecimal R221RWA_UNSECURED_CEA;
	private BigDecimal R222NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R222CCF_PCT;
	private BigDecimal R222CREDIT_EQUIVALENT_AMT;
	private BigDecimal R222CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R222CEA_AFTER_NETTING;
	private BigDecimal R222CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R222GUARANTEE_ELIGIBLE;
	private BigDecimal R222CREDIT_DERIVATIVES;
	private BigDecimal R222EXPOSURE_COVERED_CRM;
	private BigDecimal R222EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R222RWA_TOTAL;
	private BigDecimal R222CRM_RISK_WEIGHT;
	private BigDecimal R222RWA_CRM_COVERED;
	private BigDecimal R222ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R222RWA_NOT_COVERED;
	private BigDecimal R222COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R222CEA_AFTER_VOL_ADJ;
	private BigDecimal R222COLL_CASH;
	private BigDecimal R222COLL_TBILLS;
	private BigDecimal R222COLL_DEBT_SEC;
	private BigDecimal R222COLL_EQUITIES;
	private BigDecimal R222COLL_MUTUAL_FUNDS;
	private BigDecimal R222COLL_TOTAL;
	private BigDecimal R222CEA_AFTER_CRM;
	private BigDecimal R222RWA_CEA_NOT_COVERED;
	private BigDecimal R222UNSECURED_CEA;
	private BigDecimal R222RWA_UNSECURED_CEA;
	private BigDecimal R223NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R223CCF_PCT;
	private BigDecimal R223CREDIT_EQUIVALENT_AMT;
	private BigDecimal R223CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R223CEA_AFTER_NETTING;
	private BigDecimal R223CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R223GUARANTEE_ELIGIBLE;
	private BigDecimal R223CREDIT_DERIVATIVES;
	private BigDecimal R223EXPOSURE_COVERED_CRM;
	private BigDecimal R223EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R223RWA_TOTAL;
	private BigDecimal R223CRM_RISK_WEIGHT;
	private BigDecimal R223RWA_CRM_COVERED;
	private BigDecimal R223ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R223RWA_NOT_COVERED;
	private BigDecimal R223COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R223CEA_AFTER_VOL_ADJ;
	private BigDecimal R223COLL_CASH;
	private BigDecimal R223COLL_TBILLS;
	private BigDecimal R223COLL_DEBT_SEC;
	private BigDecimal R223COLL_EQUITIES;
	private BigDecimal R223COLL_MUTUAL_FUNDS;
	private BigDecimal R223COLL_TOTAL;
	private BigDecimal R223CEA_AFTER_CRM;
	private BigDecimal R223RWA_CEA_NOT_COVERED;
	private BigDecimal R223UNSECURED_CEA;
	private BigDecimal R223RWA_UNSECURED_CEA;
	private BigDecimal R224NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R224CCF_PCT;
	private BigDecimal R224CREDIT_EQUIVALENT_AMT;
	private BigDecimal R224CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R224CEA_AFTER_NETTING;
	private BigDecimal R224CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R224GUARANTEE_ELIGIBLE;
	private BigDecimal R224CREDIT_DERIVATIVES;
	private BigDecimal R224EXPOSURE_COVERED_CRM;
	private BigDecimal R224EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R224RWA_TOTAL;
	private BigDecimal R224CRM_RISK_WEIGHT;
	private BigDecimal R224RWA_CRM_COVERED;
	private BigDecimal R224ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R224RWA_NOT_COVERED;
	private BigDecimal R224COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R224CEA_AFTER_VOL_ADJ;
	private BigDecimal R224COLL_CASH;
	private BigDecimal R224COLL_TBILLS;
	private BigDecimal R224COLL_DEBT_SEC;
	private BigDecimal R224COLL_EQUITIES;
	private BigDecimal R224COLL_MUTUAL_FUNDS;
	private BigDecimal R224COLL_TOTAL;
	private BigDecimal R224CEA_AFTER_CRM;
	private BigDecimal R224RWA_CEA_NOT_COVERED;
	private BigDecimal R224UNSECURED_CEA;
	private BigDecimal R224RWA_UNSECURED_CEA;
	private BigDecimal R225NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R225CCF_PCT;
	private BigDecimal R225CREDIT_EQUIVALENT_AMT;
	private BigDecimal R225CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R225CEA_AFTER_NETTING;
	private BigDecimal R225CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R225GUARANTEE_ELIGIBLE;
	private BigDecimal R225CREDIT_DERIVATIVES;
	private BigDecimal R225EXPOSURE_COVERED_CRM;
	private BigDecimal R225EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R225RWA_TOTAL;
	private BigDecimal R225CRM_RISK_WEIGHT;
	private BigDecimal R225RWA_CRM_COVERED;
	private BigDecimal R225ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R225RWA_NOT_COVERED;
	private BigDecimal R225COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R225CEA_AFTER_VOL_ADJ;
	private BigDecimal R225COLL_CASH;
	private BigDecimal R225COLL_TBILLS;
	private BigDecimal R225COLL_DEBT_SEC;
	private BigDecimal R225COLL_EQUITIES;
	private BigDecimal R225COLL_MUTUAL_FUNDS;
	private BigDecimal R225COLL_TOTAL;
	private BigDecimal R225CEA_AFTER_CRM;
	private BigDecimal R225RWA_CEA_NOT_COVERED;
	private BigDecimal R225UNSECURED_CEA;
	private BigDecimal R225RWA_UNSECURED_CEA;
	private BigDecimal R226NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R226CCF_PCT;
	private BigDecimal R226CREDIT_EQUIVALENT_AMT;
	private BigDecimal R226CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R226CEA_AFTER_NETTING;
	private BigDecimal R226CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R226GUARANTEE_ELIGIBLE;
	private BigDecimal R226CREDIT_DERIVATIVES;
	private BigDecimal R226EXPOSURE_COVERED_CRM;
	private BigDecimal R226EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R226RWA_TOTAL;
	private BigDecimal R226CRM_RISK_WEIGHT;
	private BigDecimal R226RWA_CRM_COVERED;
	private BigDecimal R226ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R226RWA_NOT_COVERED;
	private BigDecimal R226COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R226CEA_AFTER_VOL_ADJ;
	private BigDecimal R226COLL_CASH;
	private BigDecimal R226COLL_TBILLS;
	private BigDecimal R226COLL_DEBT_SEC;
	private BigDecimal R226COLL_EQUITIES;
	private BigDecimal R226COLL_MUTUAL_FUNDS;
	private BigDecimal R226COLL_TOTAL;
	private BigDecimal R226CEA_AFTER_CRM;
	private BigDecimal R226RWA_CEA_NOT_COVERED;
	private BigDecimal R226UNSECURED_CEA;
	private BigDecimal R226RWA_UNSECURED_CEA;
	private BigDecimal R227NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R227CCF_PCT;
	private BigDecimal R227CREDIT_EQUIVALENT_AMT;
	private BigDecimal R227CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R227CEA_AFTER_NETTING;
	private BigDecimal R227CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R227GUARANTEE_ELIGIBLE;
	private BigDecimal R227CREDIT_DERIVATIVES;
	private BigDecimal R227EXPOSURE_COVERED_CRM;
	private BigDecimal R227EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R227RWA_TOTAL;
	private BigDecimal R227CRM_RISK_WEIGHT;
	private BigDecimal R227RWA_CRM_COVERED;
	private BigDecimal R227ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R227RWA_NOT_COVERED;
	private BigDecimal R227COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R227CEA_AFTER_VOL_ADJ;
	private BigDecimal R227COLL_CASH;
	private BigDecimal R227COLL_TBILLS;
	private BigDecimal R227COLL_DEBT_SEC;
	private BigDecimal R227COLL_EQUITIES;
	private BigDecimal R227COLL_MUTUAL_FUNDS;
	private BigDecimal R227COLL_TOTAL;
	private BigDecimal R227CEA_AFTER_CRM;
	private BigDecimal R227RWA_CEA_NOT_COVERED;
	private BigDecimal R227UNSECURED_CEA;
	private BigDecimal R227RWA_UNSECURED_CEA;
	private BigDecimal R228NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R228CCF_PCT;
	private BigDecimal R228CREDIT_EQUIVALENT_AMT;
	private BigDecimal R228CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R228CEA_AFTER_NETTING;
	private BigDecimal R228CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R228GUARANTEE_ELIGIBLE;
	private BigDecimal R228CREDIT_DERIVATIVES;
	private BigDecimal R228EXPOSURE_COVERED_CRM;
	private BigDecimal R228EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R228RWA_TOTAL;
	private BigDecimal R228CRM_RISK_WEIGHT;
	private BigDecimal R228RWA_CRM_COVERED;
	private BigDecimal R228ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R228RWA_NOT_COVERED;
	private BigDecimal R228COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R228CEA_AFTER_VOL_ADJ;
	private BigDecimal R228COLL_CASH;
	private BigDecimal R228COLL_TBILLS;
	private BigDecimal R228COLL_DEBT_SEC;
	private BigDecimal R228COLL_EQUITIES;
	private BigDecimal R228COLL_MUTUAL_FUNDS;
	private BigDecimal R228COLL_TOTAL;
	private BigDecimal R228CEA_AFTER_CRM;
	private BigDecimal R228RWA_CEA_NOT_COVERED;
	private BigDecimal R228UNSECURED_CEA;
	private BigDecimal R228RWA_UNSECURED_CEA;
	private BigDecimal R229NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R229CCF_PCT;
	private BigDecimal R229CREDIT_EQUIVALENT_AMT;
	private BigDecimal R229CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R229CEA_AFTER_NETTING;
	private BigDecimal R229CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R229GUARANTEE_ELIGIBLE;
	private BigDecimal R229CREDIT_DERIVATIVES;
	private BigDecimal R229EXPOSURE_COVERED_CRM;
	private BigDecimal R229EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R229RWA_TOTAL;
	private BigDecimal R229CRM_RISK_WEIGHT;
	private BigDecimal R229RWA_CRM_COVERED;
	private BigDecimal R229ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R229RWA_NOT_COVERED;
	private BigDecimal R229COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R229CEA_AFTER_VOL_ADJ;
	private BigDecimal R229COLL_CASH;
	private BigDecimal R229COLL_TBILLS;
	private BigDecimal R229COLL_DEBT_SEC;
	private BigDecimal R229COLL_EQUITIES;
	private BigDecimal R229COLL_MUTUAL_FUNDS;
	private BigDecimal R229COLL_TOTAL;
	private BigDecimal R229CEA_AFTER_CRM;
	private BigDecimal R229RWA_CEA_NOT_COVERED;
	private BigDecimal R229UNSECURED_CEA;
	private BigDecimal R229RWA_UNSECURED_CEA;
	private BigDecimal R230NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R230CCF_PCT;
	private BigDecimal R230CREDIT_EQUIVALENT_AMT;
	private BigDecimal R230CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R230CEA_AFTER_NETTING;
	private BigDecimal R230CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R230GUARANTEE_ELIGIBLE;
	private BigDecimal R230CREDIT_DERIVATIVES;
	private BigDecimal R230EXPOSURE_COVERED_CRM;
	private BigDecimal R230EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R230RWA_TOTAL;
	private BigDecimal R230CRM_RISK_WEIGHT;
	private BigDecimal R230RWA_CRM_COVERED;
	private BigDecimal R230ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R230RWA_NOT_COVERED;
	private BigDecimal R230COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R230CEA_AFTER_VOL_ADJ;
	private BigDecimal R230COLL_CASH;
	private BigDecimal R230COLL_TBILLS;
	private BigDecimal R230COLL_DEBT_SEC;
	private BigDecimal R230COLL_EQUITIES;
	private BigDecimal R230COLL_MUTUAL_FUNDS;
	private BigDecimal R230COLL_TOTAL;
	private BigDecimal R230CEA_AFTER_CRM;
	private BigDecimal R230RWA_CEA_NOT_COVERED;
	private BigDecimal R230UNSECURED_CEA;
	private BigDecimal R230RWA_UNSECURED_CEA;
	private BigDecimal R231NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R231CCF_PCT;
	private BigDecimal R231CREDIT_EQUIVALENT_AMT;
	private BigDecimal R231CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R231CEA_AFTER_NETTING;
	private BigDecimal R231CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R231GUARANTEE_ELIGIBLE;
	private BigDecimal R231CREDIT_DERIVATIVES;
	private BigDecimal R231EXPOSURE_COVERED_CRM;
	private BigDecimal R231EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R231RWA_TOTAL;
	private BigDecimal R231CRM_RISK_WEIGHT;
	private BigDecimal R231RWA_CRM_COVERED;
	private BigDecimal R231ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R231RWA_NOT_COVERED;
	private BigDecimal R231COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R231CEA_AFTER_VOL_ADJ;
	private BigDecimal R231COLL_CASH;
	private BigDecimal R231COLL_TBILLS;
	private BigDecimal R231COLL_DEBT_SEC;
	private BigDecimal R231COLL_EQUITIES;
	private BigDecimal R231COLL_MUTUAL_FUNDS;
	private BigDecimal R231COLL_TOTAL;
	private BigDecimal R231CEA_AFTER_CRM;
	private BigDecimal R231RWA_CEA_NOT_COVERED;
	private BigDecimal R231UNSECURED_CEA;
	private BigDecimal R231RWA_UNSECURED_CEA;
	private BigDecimal R232NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R232CCF_PCT;
	private BigDecimal R232CREDIT_EQUIVALENT_AMT;
	private BigDecimal R232CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R232CEA_AFTER_NETTING;
	private BigDecimal R232CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R232GUARANTEE_ELIGIBLE;
	private BigDecimal R232CREDIT_DERIVATIVES;
	private BigDecimal R232EXPOSURE_COVERED_CRM;
	private BigDecimal R232EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R232RWA_TOTAL;
	private BigDecimal R232CRM_RISK_WEIGHT;
	private BigDecimal R232RWA_CRM_COVERED;
	private BigDecimal R232ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R232RWA_NOT_COVERED;
	private BigDecimal R232COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R232CEA_AFTER_VOL_ADJ;
	private BigDecimal R232COLL_CASH;
	private BigDecimal R232COLL_TBILLS;
	private BigDecimal R232COLL_DEBT_SEC;
	private BigDecimal R232COLL_EQUITIES;
	private BigDecimal R232COLL_MUTUAL_FUNDS;
	private BigDecimal R232COLL_TOTAL;
	private BigDecimal R232CEA_AFTER_CRM;
	private BigDecimal R232RWA_CEA_NOT_COVERED;
	private BigDecimal R232UNSECURED_CEA;
	private BigDecimal R232RWA_UNSECURED_CEA;
	private BigDecimal R233NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R233CCF_PCT;
	private BigDecimal R233CREDIT_EQUIVALENT_AMT;
	private BigDecimal R233CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R233CEA_AFTER_NETTING;
	private BigDecimal R233CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R233GUARANTEE_ELIGIBLE;
	private BigDecimal R233CREDIT_DERIVATIVES;
	private BigDecimal R233EXPOSURE_COVERED_CRM;
	private BigDecimal R233EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R233RWA_TOTAL;
	private BigDecimal R233CRM_RISK_WEIGHT;
	private BigDecimal R233RWA_CRM_COVERED;
	private BigDecimal R233ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R233RWA_NOT_COVERED;
	private BigDecimal R233COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R233CEA_AFTER_VOL_ADJ;
	private BigDecimal R233COLL_CASH;
	private BigDecimal R233COLL_TBILLS;
	private BigDecimal R233COLL_DEBT_SEC;
	private BigDecimal R233COLL_EQUITIES;
	private BigDecimal R233COLL_MUTUAL_FUNDS;
	private BigDecimal R233COLL_TOTAL;
	private BigDecimal R233CEA_AFTER_CRM;
	private BigDecimal R233RWA_CEA_NOT_COVERED;
	private BigDecimal R233UNSECURED_CEA;
	private BigDecimal R233RWA_UNSECURED_CEA;
	private BigDecimal R234NOMINAL_PRINCIPAL_AMT;
	private BigDecimal R234CCF_PCT;
	private BigDecimal R234CREDIT_EQUIVALENT_AMT;
	private BigDecimal R234CEA_ELIGIBLE_NETTING_CP;
	private BigDecimal R234CEA_AFTER_NETTING;
	private BigDecimal R234CEA_ELIGIBLE_CRM_SUB;
	private BigDecimal R234GUARANTEE_ELIGIBLE;
	private BigDecimal R234CREDIT_DERIVATIVES;
	private BigDecimal R234EXPOSURE_COVERED_CRM;
	private BigDecimal R234EXPOSURE_NOT_COVERED_CRM;
	private BigDecimal R234RWA_TOTAL;
	private BigDecimal R234CRM_RISK_WEIGHT;
	private BigDecimal R234RWA_CRM_COVERED;
	private BigDecimal R234ORIGINAL_CP_RISK_WEIGHT;
	private BigDecimal R234RWA_NOT_COVERED;
	private BigDecimal R234COLLATERAL_CEA_ELIGIBLE;
	private BigDecimal R234CEA_AFTER_VOL_ADJ;
	private BigDecimal R234COLL_CASH;
	private BigDecimal R234COLL_TBILLS;
	private BigDecimal R234COLL_DEBT_SEC;
	private BigDecimal R234COLL_EQUITIES;
	private BigDecimal R234COLL_MUTUAL_FUNDS;
	private BigDecimal R234COLL_TOTAL;
	private BigDecimal R234CEA_AFTER_CRM;
	private BigDecimal R234RWA_CEA_NOT_COVERED;
	private BigDecimal R234UNSECURED_CEA;
	private BigDecimal R234RWA_UNSECURED_CEA;
	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}
	public String getREPORT_VERSION() {
		return REPORT_VERSION;
	}
	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}
	public String getREPORT_CODE() {
		return REPORT_CODE;
	}
	public String getREPORT_DESC() {
		return REPORT_DESC;
	}
	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}
	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}
	public String getDEL_FLG() {
		return DEL_FLG;
	}
	public BigDecimal getR216_NOMINAL_PRINCIPAL_AMT() {
		return R216_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR216_CCF_PCT() {
		return R216_CCF_PCT;
	}
	public BigDecimal getR216_CREDIT_EQUIVALENT_AMT() {
		return R216_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR216_CEA_ELIGIBLE_NETTING_CP() {
		return R216_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR216_CEA_AFTER_NETTING() {
		return R216_CEA_AFTER_NETTING;
	}
	public BigDecimal getR216_CEA_ELIGIBLE_CRM_SUB() {
		return R216_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR216_GUARANTEE_ELIGIBLE() {
		return R216_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR216_CREDIT_DERIVATIVES() {
		return R216_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR216_EXPOSURE_COVERED_CRM() {
		return R216_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR216_EXPOSURE_NOT_COVERED_CRM() {
		return R216_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR216_RWA_TOTAL() {
		return R216_RWA_TOTAL;
	}
	public BigDecimal getR216_CRM_RISK_WEIGHT() {
		return R216_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR216_RWA_CRM_COVERED() {
		return R216_RWA_CRM_COVERED;
	}
	public BigDecimal getR216_ORIGINAL_CP_RISK_WEIGHT() {
		return R216_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR216_RWA_NOT_COVERED() {
		return R216_RWA_NOT_COVERED;
	}
	public BigDecimal getR216_COLLATERAL_CEA_ELIGIBLE() {
		return R216_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR216_CEA_AFTER_VOL_ADJ() {
		return R216_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR216_COLL_CASH() {
		return R216_COLL_CASH;
	}
	public BigDecimal getR216_COLL_TBILLS() {
		return R216_COLL_TBILLS;
	}
	public BigDecimal getR216_COLL_DEBT_SEC() {
		return R216_COLL_DEBT_SEC;
	}
	public BigDecimal getR216_COLL_EQUITIES() {
		return R216_COLL_EQUITIES;
	}
	public BigDecimal getR216_COLL_MUTUAL_FUNDS() {
		return R216_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR216_COLL_TOTAL() {
		return R216_COLL_TOTAL;
	}
	public BigDecimal getR216_CEA_AFTER_CRM() {
		return R216_CEA_AFTER_CRM;
	}
	public BigDecimal getR216_RWA_CEA_NOT_COVERED() {
		return R216_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR216_UNSECURED_CEA() {
		return R216_UNSECURED_CEA;
	}
	public BigDecimal getR216_RWA_UNSECURED_CEA() {
		return R216_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR217_NOMINAL_PRINCIPAL_AMT() {
		return R217_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR217_CCF_PCT() {
		return R217_CCF_PCT;
	}
	public BigDecimal getR217_CREDIT_EQUIVALENT_AMT() {
		return R217_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR217_CEA_ELIGIBLE_NETTING_CP() {
		return R217_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR217_CEA_AFTER_NETTING() {
		return R217_CEA_AFTER_NETTING;
	}
	public BigDecimal getR217_CEA_ELIGIBLE_CRM_SUB() {
		return R217_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR217_GUARANTEE_ELIGIBLE() {
		return R217_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR217_CREDIT_DERIVATIVES() {
		return R217_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR217_EXPOSURE_COVERED_CRM() {
		return R217_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR217_EXPOSURE_NOT_COVERED_CRM() {
		return R217_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR217_RWA_TOTAL() {
		return R217_RWA_TOTAL;
	}
	public BigDecimal getR217_CRM_RISK_WEIGHT() {
		return R217_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR217_RWA_CRM_COVERED() {
		return R217_RWA_CRM_COVERED;
	}
	public BigDecimal getR217_ORIGINAL_CP_RISK_WEIGHT() {
		return R217_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR217_RWA_NOT_COVERED() {
		return R217_RWA_NOT_COVERED;
	}
	public BigDecimal getR217_COLLATERAL_CEA_ELIGIBLE() {
		return R217_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR217_CEA_AFTER_VOL_ADJ() {
		return R217_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR217_COLL_CASH() {
		return R217_COLL_CASH;
	}
	public BigDecimal getR217_COLL_TBILLS() {
		return R217_COLL_TBILLS;
	}
	public BigDecimal getR217_COLL_DEBT_SEC() {
		return R217_COLL_DEBT_SEC;
	}
	public BigDecimal getR217_COLL_EQUITIES() {
		return R217_COLL_EQUITIES;
	}
	public BigDecimal getR217_COLL_MUTUAL_FUNDS() {
		return R217_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR217_COLL_TOTAL() {
		return R217_COLL_TOTAL;
	}
	public BigDecimal getR217_CEA_AFTER_CRM() {
		return R217_CEA_AFTER_CRM;
	}
	public BigDecimal getR217_RWA_CEA_NOT_COVERED() {
		return R217_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR217_UNSECURED_CEA() {
		return R217_UNSECURED_CEA;
	}
	public BigDecimal getR217_RWA_UNSECURED_CEA() {
		return R217_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR218_NOMINAL_PRINCIPAL_AMT() {
		return R218_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR218_CCF_PCT() {
		return R218_CCF_PCT;
	}
	public BigDecimal getR218_CREDIT_EQUIVALENT_AMT() {
		return R218_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR218_CEA_ELIGIBLE_NETTING_CP() {
		return R218_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR218_CEA_AFTER_NETTING() {
		return R218_CEA_AFTER_NETTING;
	}
	public BigDecimal getR218_CEA_ELIGIBLE_CRM_SUB() {
		return R218_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR218_GUARANTEE_ELIGIBLE() {
		return R218_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR218_CREDIT_DERIVATIVES() {
		return R218_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR218_EXPOSURE_COVERED_CRM() {
		return R218_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR218_EXPOSURE_NOT_COVERED_CRM() {
		return R218_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR218_RWA_TOTAL() {
		return R218_RWA_TOTAL;
	}
	public BigDecimal getR218_CRM_RISK_WEIGHT() {
		return R218_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR218_RWA_CRM_COVERED() {
		return R218_RWA_CRM_COVERED;
	}
	public BigDecimal getR218_ORIGINAL_CP_RISK_WEIGHT() {
		return R218_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR218_RWA_NOT_COVERED() {
		return R218_RWA_NOT_COVERED;
	}
	public BigDecimal getR218_COLLATERAL_CEA_ELIGIBLE() {
		return R218_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR218_CEA_AFTER_VOL_ADJ() {
		return R218_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR218_COLL_CASH() {
		return R218_COLL_CASH;
	}
	public BigDecimal getR218_COLL_TBILLS() {
		return R218_COLL_TBILLS;
	}
	public BigDecimal getR218_COLL_DEBT_SEC() {
		return R218_COLL_DEBT_SEC;
	}
	public BigDecimal getR218_COLL_EQUITIES() {
		return R218_COLL_EQUITIES;
	}
	public BigDecimal getR218_COLL_MUTUAL_FUNDS() {
		return R218_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR218_COLL_TOTAL() {
		return R218_COLL_TOTAL;
	}
	public BigDecimal getR218_CEA_AFTER_CRM() {
		return R218_CEA_AFTER_CRM;
	}
	public BigDecimal getR218_RWA_CEA_NOT_COVERED() {
		return R218_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR218_UNSECURED_CEA() {
		return R218_UNSECURED_CEA;
	}
	public BigDecimal getR218_RWA_UNSECURED_CEA() {
		return R218_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR219_NOMINAL_PRINCIPAL_AMT() {
		return R219_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR219_CCF_PCT() {
		return R219_CCF_PCT;
	}
	public BigDecimal getR219_CREDIT_EQUIVALENT_AMT() {
		return R219_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR219_CEA_ELIGIBLE_NETTING_CP() {
		return R219_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR219_CEA_AFTER_NETTING() {
		return R219_CEA_AFTER_NETTING;
	}
	public BigDecimal getR219_CEA_ELIGIBLE_CRM_SUB() {
		return R219_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR219_GUARANTEE_ELIGIBLE() {
		return R219_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR219_CREDIT_DERIVATIVES() {
		return R219_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR219_EXPOSURE_COVERED_CRM() {
		return R219_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR219_EXPOSURE_NOT_COVERED_CRM() {
		return R219_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR219_RWA_TOTAL() {
		return R219_RWA_TOTAL;
	}
	public BigDecimal getR219_CRM_RISK_WEIGHT() {
		return R219_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR219_RWA_CRM_COVERED() {
		return R219_RWA_CRM_COVERED;
	}
	public BigDecimal getR219_ORIGINAL_CP_RISK_WEIGHT() {
		return R219_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR219_RWA_NOT_COVERED() {
		return R219_RWA_NOT_COVERED;
	}
	public BigDecimal getR219_COLLATERAL_CEA_ELIGIBLE() {
		return R219_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR219_CEA_AFTER_VOL_ADJ() {
		return R219_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR219_COLL_CASH() {
		return R219_COLL_CASH;
	}
	public BigDecimal getR219_COLL_TBILLS() {
		return R219_COLL_TBILLS;
	}
	public BigDecimal getR219_COLL_DEBT_SEC() {
		return R219_COLL_DEBT_SEC;
	}
	public BigDecimal getR219_COLL_EQUITIES() {
		return R219_COLL_EQUITIES;
	}
	public BigDecimal getR219_COLL_MUTUAL_FUNDS() {
		return R219_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR219_COLL_TOTAL() {
		return R219_COLL_TOTAL;
	}
	public BigDecimal getR219_CEA_AFTER_CRM() {
		return R219_CEA_AFTER_CRM;
	}
	public BigDecimal getR219_RWA_CEA_NOT_COVERED() {
		return R219_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR219_UNSECURED_CEA() {
		return R219_UNSECURED_CEA;
	}
	public BigDecimal getR219_RWA_UNSECURED_CEA() {
		return R219_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR220_NOMINAL_PRINCIPAL_AMT() {
		return R220_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR220_CCF_PCT() {
		return R220_CCF_PCT;
	}
	public BigDecimal getR220_CREDIT_EQUIVALENT_AMT() {
		return R220_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR220_CEA_ELIGIBLE_NETTING_CP() {
		return R220_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR220_CEA_AFTER_NETTING() {
		return R220_CEA_AFTER_NETTING;
	}
	public BigDecimal getR220_CEA_ELIGIBLE_CRM_SUB() {
		return R220_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR220_GUARANTEE_ELIGIBLE() {
		return R220_GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR220_CREDIT_DERIVATIVES() {
		return R220_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR220_EXPOSURE_COVERED_CRM() {
		return R220_EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR220_EXPOSURE_NOT_COVERED_CRM() {
		return R220_EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR220_RWA_TOTAL() {
		return R220_RWA_TOTAL;
	}
	public BigDecimal getR220_CRM_RISK_WEIGHT() {
		return R220_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR220_RWA_CRM_COVERED() {
		return R220_RWA_CRM_COVERED;
	}
	public BigDecimal getR220_ORIGINAL_CP_RISK_WEIGHT() {
		return R220_ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR220_RWA_NOT_COVERED() {
		return R220_RWA_NOT_COVERED;
	}
	public BigDecimal getR220_COLLATERAL_CEA_ELIGIBLE() {
		return R220_COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR220_CEA_AFTER_VOL_ADJ() {
		return R220_CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR220_COLL_CASH() {
		return R220_COLL_CASH;
	}
	public BigDecimal getR220_COLL_TBILLS() {
		return R220_COLL_TBILLS;
	}
	public BigDecimal getR220_COLL_DEBT_SEC() {
		return R220_COLL_DEBT_SEC;
	}
	public BigDecimal getR220_COLL_EQUITIES() {
		return R220_COLL_EQUITIES;
	}
	public BigDecimal getR220_COLL_MUTUAL_FUNDS() {
		return R220_COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR220_COLL_TOTAL() {
		return R220_COLL_TOTAL;
	}
	public BigDecimal getR220_CEA_AFTER_CRM() {
		return R220_CEA_AFTER_CRM;
	}
	public BigDecimal getR220_RWA_CEA_NOT_COVERED() {
		return R220_RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR220_UNSECURED_CEA() {
		return R220_UNSECURED_CEA;
	}
	public BigDecimal getR220_RWA_UNSECURED_CEA() {
		return R220_RWA_UNSECURED_CEA;
	}
	public BigDecimal getR221_NOMINAL_PRINCIPAL_AMT() {
		return R221_NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR221_CCF_PCT() {
		return R221_CCF_PCT;
	}
	public BigDecimal getR221_CREDIT_EQUIVALENT_AMT() {
		return R221_CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR221_CEA_ELIGIBLE_NETTING_CP() {
		return R221_CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR221_CEA_AFTER_NETTING() {
		return R221_CEA_AFTER_NETTING;
	}
	public BigDecimal getR221_CEA_ELIGIBLE_CRM_SUB() {
		return R221_CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR221GUARANTEE_ELIGIBLE() {
		return R221GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR221CREDIT_DERIVATIVES() {
		return R221CREDIT_DERIVATIVES;
	}
	public BigDecimal getR221EXPOSURE_COVERED_CRM() {
		return R221EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR221EXPOSURE_NOT_COVERED_CRM() {
		return R221EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR221RWA_TOTAL() {
		return R221RWA_TOTAL;
	}
	public BigDecimal getR221CRM_RISK_WEIGHT() {
		return R221CRM_RISK_WEIGHT;
	}
	public BigDecimal getR221RWA_CRM_COVERED() {
		return R221RWA_CRM_COVERED;
	}
	public BigDecimal getR221ORIGINAL_CP_RISK_WEIGHT() {
		return R221ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR221RWA_NOT_COVERED() {
		return R221RWA_NOT_COVERED;
	}
	public BigDecimal getR221COLLATERAL_CEA_ELIGIBLE() {
		return R221COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR221CEA_AFTER_VOL_ADJ() {
		return R221CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR221COLL_CASH() {
		return R221COLL_CASH;
	}
	public BigDecimal getR221COLL_TBILLS() {
		return R221COLL_TBILLS;
	}
	public BigDecimal getR221COLL_DEBT_SEC() {
		return R221COLL_DEBT_SEC;
	}
	public BigDecimal getR221COLL_EQUITIES() {
		return R221COLL_EQUITIES;
	}
	public BigDecimal getR221COLL_MUTUAL_FUNDS() {
		return R221COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR221COLL_TOTAL() {
		return R221COLL_TOTAL;
	}
	public BigDecimal getR221CEA_AFTER_CRM() {
		return R221CEA_AFTER_CRM;
	}
	public BigDecimal getR221RWA_CEA_NOT_COVERED() {
		return R221RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR221UNSECURED_CEA() {
		return R221UNSECURED_CEA;
	}
	public BigDecimal getR221RWA_UNSECURED_CEA() {
		return R221RWA_UNSECURED_CEA;
	}
	public BigDecimal getR222NOMINAL_PRINCIPAL_AMT() {
		return R222NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR222CCF_PCT() {
		return R222CCF_PCT;
	}
	public BigDecimal getR222CREDIT_EQUIVALENT_AMT() {
		return R222CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR222CEA_ELIGIBLE_NETTING_CP() {
		return R222CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR222CEA_AFTER_NETTING() {
		return R222CEA_AFTER_NETTING;
	}
	public BigDecimal getR222CEA_ELIGIBLE_CRM_SUB() {
		return R222CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR222GUARANTEE_ELIGIBLE() {
		return R222GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR222CREDIT_DERIVATIVES() {
		return R222CREDIT_DERIVATIVES;
	}
	public BigDecimal getR222EXPOSURE_COVERED_CRM() {
		return R222EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR222EXPOSURE_NOT_COVERED_CRM() {
		return R222EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR222RWA_TOTAL() {
		return R222RWA_TOTAL;
	}
	public BigDecimal getR222CRM_RISK_WEIGHT() {
		return R222CRM_RISK_WEIGHT;
	}
	public BigDecimal getR222RWA_CRM_COVERED() {
		return R222RWA_CRM_COVERED;
	}
	public BigDecimal getR222ORIGINAL_CP_RISK_WEIGHT() {
		return R222ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR222RWA_NOT_COVERED() {
		return R222RWA_NOT_COVERED;
	}
	public BigDecimal getR222COLLATERAL_CEA_ELIGIBLE() {
		return R222COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR222CEA_AFTER_VOL_ADJ() {
		return R222CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR222COLL_CASH() {
		return R222COLL_CASH;
	}
	public BigDecimal getR222COLL_TBILLS() {
		return R222COLL_TBILLS;
	}
	public BigDecimal getR222COLL_DEBT_SEC() {
		return R222COLL_DEBT_SEC;
	}
	public BigDecimal getR222COLL_EQUITIES() {
		return R222COLL_EQUITIES;
	}
	public BigDecimal getR222COLL_MUTUAL_FUNDS() {
		return R222COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR222COLL_TOTAL() {
		return R222COLL_TOTAL;
	}
	public BigDecimal getR222CEA_AFTER_CRM() {
		return R222CEA_AFTER_CRM;
	}
	public BigDecimal getR222RWA_CEA_NOT_COVERED() {
		return R222RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR222UNSECURED_CEA() {
		return R222UNSECURED_CEA;
	}
	public BigDecimal getR222RWA_UNSECURED_CEA() {
		return R222RWA_UNSECURED_CEA;
	}
	public BigDecimal getR223NOMINAL_PRINCIPAL_AMT() {
		return R223NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR223CCF_PCT() {
		return R223CCF_PCT;
	}
	public BigDecimal getR223CREDIT_EQUIVALENT_AMT() {
		return R223CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR223CEA_ELIGIBLE_NETTING_CP() {
		return R223CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR223CEA_AFTER_NETTING() {
		return R223CEA_AFTER_NETTING;
	}
	public BigDecimal getR223CEA_ELIGIBLE_CRM_SUB() {
		return R223CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR223GUARANTEE_ELIGIBLE() {
		return R223GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR223CREDIT_DERIVATIVES() {
		return R223CREDIT_DERIVATIVES;
	}
	public BigDecimal getR223EXPOSURE_COVERED_CRM() {
		return R223EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR223EXPOSURE_NOT_COVERED_CRM() {
		return R223EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR223RWA_TOTAL() {
		return R223RWA_TOTAL;
	}
	public BigDecimal getR223CRM_RISK_WEIGHT() {
		return R223CRM_RISK_WEIGHT;
	}
	public BigDecimal getR223RWA_CRM_COVERED() {
		return R223RWA_CRM_COVERED;
	}
	public BigDecimal getR223ORIGINAL_CP_RISK_WEIGHT() {
		return R223ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR223RWA_NOT_COVERED() {
		return R223RWA_NOT_COVERED;
	}
	public BigDecimal getR223COLLATERAL_CEA_ELIGIBLE() {
		return R223COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR223CEA_AFTER_VOL_ADJ() {
		return R223CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR223COLL_CASH() {
		return R223COLL_CASH;
	}
	public BigDecimal getR223COLL_TBILLS() {
		return R223COLL_TBILLS;
	}
	public BigDecimal getR223COLL_DEBT_SEC() {
		return R223COLL_DEBT_SEC;
	}
	public BigDecimal getR223COLL_EQUITIES() {
		return R223COLL_EQUITIES;
	}
	public BigDecimal getR223COLL_MUTUAL_FUNDS() {
		return R223COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR223COLL_TOTAL() {
		return R223COLL_TOTAL;
	}
	public BigDecimal getR223CEA_AFTER_CRM() {
		return R223CEA_AFTER_CRM;
	}
	public BigDecimal getR223RWA_CEA_NOT_COVERED() {
		return R223RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR223UNSECURED_CEA() {
		return R223UNSECURED_CEA;
	}
	public BigDecimal getR223RWA_UNSECURED_CEA() {
		return R223RWA_UNSECURED_CEA;
	}
	public BigDecimal getR224NOMINAL_PRINCIPAL_AMT() {
		return R224NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR224CCF_PCT() {
		return R224CCF_PCT;
	}
	public BigDecimal getR224CREDIT_EQUIVALENT_AMT() {
		return R224CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR224CEA_ELIGIBLE_NETTING_CP() {
		return R224CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR224CEA_AFTER_NETTING() {
		return R224CEA_AFTER_NETTING;
	}
	public BigDecimal getR224CEA_ELIGIBLE_CRM_SUB() {
		return R224CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR224GUARANTEE_ELIGIBLE() {
		return R224GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR224CREDIT_DERIVATIVES() {
		return R224CREDIT_DERIVATIVES;
	}
	public BigDecimal getR224EXPOSURE_COVERED_CRM() {
		return R224EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR224EXPOSURE_NOT_COVERED_CRM() {
		return R224EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR224RWA_TOTAL() {
		return R224RWA_TOTAL;
	}
	public BigDecimal getR224CRM_RISK_WEIGHT() {
		return R224CRM_RISK_WEIGHT;
	}
	public BigDecimal getR224RWA_CRM_COVERED() {
		return R224RWA_CRM_COVERED;
	}
	public BigDecimal getR224ORIGINAL_CP_RISK_WEIGHT() {
		return R224ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR224RWA_NOT_COVERED() {
		return R224RWA_NOT_COVERED;
	}
	public BigDecimal getR224COLLATERAL_CEA_ELIGIBLE() {
		return R224COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR224CEA_AFTER_VOL_ADJ() {
		return R224CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR224COLL_CASH() {
		return R224COLL_CASH;
	}
	public BigDecimal getR224COLL_TBILLS() {
		return R224COLL_TBILLS;
	}
	public BigDecimal getR224COLL_DEBT_SEC() {
		return R224COLL_DEBT_SEC;
	}
	public BigDecimal getR224COLL_EQUITIES() {
		return R224COLL_EQUITIES;
	}
	public BigDecimal getR224COLL_MUTUAL_FUNDS() {
		return R224COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR224COLL_TOTAL() {
		return R224COLL_TOTAL;
	}
	public BigDecimal getR224CEA_AFTER_CRM() {
		return R224CEA_AFTER_CRM;
	}
	public BigDecimal getR224RWA_CEA_NOT_COVERED() {
		return R224RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR224UNSECURED_CEA() {
		return R224UNSECURED_CEA;
	}
	public BigDecimal getR224RWA_UNSECURED_CEA() {
		return R224RWA_UNSECURED_CEA;
	}
	public BigDecimal getR225NOMINAL_PRINCIPAL_AMT() {
		return R225NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR225CCF_PCT() {
		return R225CCF_PCT;
	}
	public BigDecimal getR225CREDIT_EQUIVALENT_AMT() {
		return R225CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR225CEA_ELIGIBLE_NETTING_CP() {
		return R225CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR225CEA_AFTER_NETTING() {
		return R225CEA_AFTER_NETTING;
	}
	public BigDecimal getR225CEA_ELIGIBLE_CRM_SUB() {
		return R225CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR225GUARANTEE_ELIGIBLE() {
		return R225GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR225CREDIT_DERIVATIVES() {
		return R225CREDIT_DERIVATIVES;
	}
	public BigDecimal getR225EXPOSURE_COVERED_CRM() {
		return R225EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR225EXPOSURE_NOT_COVERED_CRM() {
		return R225EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR225RWA_TOTAL() {
		return R225RWA_TOTAL;
	}
	public BigDecimal getR225CRM_RISK_WEIGHT() {
		return R225CRM_RISK_WEIGHT;
	}
	public BigDecimal getR225RWA_CRM_COVERED() {
		return R225RWA_CRM_COVERED;
	}
	public BigDecimal getR225ORIGINAL_CP_RISK_WEIGHT() {
		return R225ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR225RWA_NOT_COVERED() {
		return R225RWA_NOT_COVERED;
	}
	public BigDecimal getR225COLLATERAL_CEA_ELIGIBLE() {
		return R225COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR225CEA_AFTER_VOL_ADJ() {
		return R225CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR225COLL_CASH() {
		return R225COLL_CASH;
	}
	public BigDecimal getR225COLL_TBILLS() {
		return R225COLL_TBILLS;
	}
	public BigDecimal getR225COLL_DEBT_SEC() {
		return R225COLL_DEBT_SEC;
	}
	public BigDecimal getR225COLL_EQUITIES() {
		return R225COLL_EQUITIES;
	}
	public BigDecimal getR225COLL_MUTUAL_FUNDS() {
		return R225COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR225COLL_TOTAL() {
		return R225COLL_TOTAL;
	}
	public BigDecimal getR225CEA_AFTER_CRM() {
		return R225CEA_AFTER_CRM;
	}
	public BigDecimal getR225RWA_CEA_NOT_COVERED() {
		return R225RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR225UNSECURED_CEA() {
		return R225UNSECURED_CEA;
	}
	public BigDecimal getR225RWA_UNSECURED_CEA() {
		return R225RWA_UNSECURED_CEA;
	}
	public BigDecimal getR226NOMINAL_PRINCIPAL_AMT() {
		return R226NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR226CCF_PCT() {
		return R226CCF_PCT;
	}
	public BigDecimal getR226CREDIT_EQUIVALENT_AMT() {
		return R226CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR226CEA_ELIGIBLE_NETTING_CP() {
		return R226CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR226CEA_AFTER_NETTING() {
		return R226CEA_AFTER_NETTING;
	}
	public BigDecimal getR226CEA_ELIGIBLE_CRM_SUB() {
		return R226CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR226GUARANTEE_ELIGIBLE() {
		return R226GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR226CREDIT_DERIVATIVES() {
		return R226CREDIT_DERIVATIVES;
	}
	public BigDecimal getR226EXPOSURE_COVERED_CRM() {
		return R226EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR226EXPOSURE_NOT_COVERED_CRM() {
		return R226EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR226RWA_TOTAL() {
		return R226RWA_TOTAL;
	}
	public BigDecimal getR226CRM_RISK_WEIGHT() {
		return R226CRM_RISK_WEIGHT;
	}
	public BigDecimal getR226RWA_CRM_COVERED() {
		return R226RWA_CRM_COVERED;
	}
	public BigDecimal getR226ORIGINAL_CP_RISK_WEIGHT() {
		return R226ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR226RWA_NOT_COVERED() {
		return R226RWA_NOT_COVERED;
	}
	public BigDecimal getR226COLLATERAL_CEA_ELIGIBLE() {
		return R226COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR226CEA_AFTER_VOL_ADJ() {
		return R226CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR226COLL_CASH() {
		return R226COLL_CASH;
	}
	public BigDecimal getR226COLL_TBILLS() {
		return R226COLL_TBILLS;
	}
	public BigDecimal getR226COLL_DEBT_SEC() {
		return R226COLL_DEBT_SEC;
	}
	public BigDecimal getR226COLL_EQUITIES() {
		return R226COLL_EQUITIES;
	}
	public BigDecimal getR226COLL_MUTUAL_FUNDS() {
		return R226COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR226COLL_TOTAL() {
		return R226COLL_TOTAL;
	}
	public BigDecimal getR226CEA_AFTER_CRM() {
		return R226CEA_AFTER_CRM;
	}
	public BigDecimal getR226RWA_CEA_NOT_COVERED() {
		return R226RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR226UNSECURED_CEA() {
		return R226UNSECURED_CEA;
	}
	public BigDecimal getR226RWA_UNSECURED_CEA() {
		return R226RWA_UNSECURED_CEA;
	}
	public BigDecimal getR227NOMINAL_PRINCIPAL_AMT() {
		return R227NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR227CCF_PCT() {
		return R227CCF_PCT;
	}
	public BigDecimal getR227CREDIT_EQUIVALENT_AMT() {
		return R227CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR227CEA_ELIGIBLE_NETTING_CP() {
		return R227CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR227CEA_AFTER_NETTING() {
		return R227CEA_AFTER_NETTING;
	}
	public BigDecimal getR227CEA_ELIGIBLE_CRM_SUB() {
		return R227CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR227GUARANTEE_ELIGIBLE() {
		return R227GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR227CREDIT_DERIVATIVES() {
		return R227CREDIT_DERIVATIVES;
	}
	public BigDecimal getR227EXPOSURE_COVERED_CRM() {
		return R227EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR227EXPOSURE_NOT_COVERED_CRM() {
		return R227EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR227RWA_TOTAL() {
		return R227RWA_TOTAL;
	}
	public BigDecimal getR227CRM_RISK_WEIGHT() {
		return R227CRM_RISK_WEIGHT;
	}
	public BigDecimal getR227RWA_CRM_COVERED() {
		return R227RWA_CRM_COVERED;
	}
	public BigDecimal getR227ORIGINAL_CP_RISK_WEIGHT() {
		return R227ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR227RWA_NOT_COVERED() {
		return R227RWA_NOT_COVERED;
	}
	public BigDecimal getR227COLLATERAL_CEA_ELIGIBLE() {
		return R227COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR227CEA_AFTER_VOL_ADJ() {
		return R227CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR227COLL_CASH() {
		return R227COLL_CASH;
	}
	public BigDecimal getR227COLL_TBILLS() {
		return R227COLL_TBILLS;
	}
	public BigDecimal getR227COLL_DEBT_SEC() {
		return R227COLL_DEBT_SEC;
	}
	public BigDecimal getR227COLL_EQUITIES() {
		return R227COLL_EQUITIES;
	}
	public BigDecimal getR227COLL_MUTUAL_FUNDS() {
		return R227COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR227COLL_TOTAL() {
		return R227COLL_TOTAL;
	}
	public BigDecimal getR227CEA_AFTER_CRM() {
		return R227CEA_AFTER_CRM;
	}
	public BigDecimal getR227RWA_CEA_NOT_COVERED() {
		return R227RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR227UNSECURED_CEA() {
		return R227UNSECURED_CEA;
	}
	public BigDecimal getR227RWA_UNSECURED_CEA() {
		return R227RWA_UNSECURED_CEA;
	}
	public BigDecimal getR228NOMINAL_PRINCIPAL_AMT() {
		return R228NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR228CCF_PCT() {
		return R228CCF_PCT;
	}
	public BigDecimal getR228CREDIT_EQUIVALENT_AMT() {
		return R228CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR228CEA_ELIGIBLE_NETTING_CP() {
		return R228CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR228CEA_AFTER_NETTING() {
		return R228CEA_AFTER_NETTING;
	}
	public BigDecimal getR228CEA_ELIGIBLE_CRM_SUB() {
		return R228CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR228GUARANTEE_ELIGIBLE() {
		return R228GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR228CREDIT_DERIVATIVES() {
		return R228CREDIT_DERIVATIVES;
	}
	public BigDecimal getR228EXPOSURE_COVERED_CRM() {
		return R228EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR228EXPOSURE_NOT_COVERED_CRM() {
		return R228EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR228RWA_TOTAL() {
		return R228RWA_TOTAL;
	}
	public BigDecimal getR228CRM_RISK_WEIGHT() {
		return R228CRM_RISK_WEIGHT;
	}
	public BigDecimal getR228RWA_CRM_COVERED() {
		return R228RWA_CRM_COVERED;
	}
	public BigDecimal getR228ORIGINAL_CP_RISK_WEIGHT() {
		return R228ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR228RWA_NOT_COVERED() {
		return R228RWA_NOT_COVERED;
	}
	public BigDecimal getR228COLLATERAL_CEA_ELIGIBLE() {
		return R228COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR228CEA_AFTER_VOL_ADJ() {
		return R228CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR228COLL_CASH() {
		return R228COLL_CASH;
	}
	public BigDecimal getR228COLL_TBILLS() {
		return R228COLL_TBILLS;
	}
	public BigDecimal getR228COLL_DEBT_SEC() {
		return R228COLL_DEBT_SEC;
	}
	public BigDecimal getR228COLL_EQUITIES() {
		return R228COLL_EQUITIES;
	}
	public BigDecimal getR228COLL_MUTUAL_FUNDS() {
		return R228COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR228COLL_TOTAL() {
		return R228COLL_TOTAL;
	}
	public BigDecimal getR228CEA_AFTER_CRM() {
		return R228CEA_AFTER_CRM;
	}
	public BigDecimal getR228RWA_CEA_NOT_COVERED() {
		return R228RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR228UNSECURED_CEA() {
		return R228UNSECURED_CEA;
	}
	public BigDecimal getR228RWA_UNSECURED_CEA() {
		return R228RWA_UNSECURED_CEA;
	}
	public BigDecimal getR229NOMINAL_PRINCIPAL_AMT() {
		return R229NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR229CCF_PCT() {
		return R229CCF_PCT;
	}
	public BigDecimal getR229CREDIT_EQUIVALENT_AMT() {
		return R229CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR229CEA_ELIGIBLE_NETTING_CP() {
		return R229CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR229CEA_AFTER_NETTING() {
		return R229CEA_AFTER_NETTING;
	}
	public BigDecimal getR229CEA_ELIGIBLE_CRM_SUB() {
		return R229CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR229GUARANTEE_ELIGIBLE() {
		return R229GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR229CREDIT_DERIVATIVES() {
		return R229CREDIT_DERIVATIVES;
	}
	public BigDecimal getR229EXPOSURE_COVERED_CRM() {
		return R229EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR229EXPOSURE_NOT_COVERED_CRM() {
		return R229EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR229RWA_TOTAL() {
		return R229RWA_TOTAL;
	}
	public BigDecimal getR229CRM_RISK_WEIGHT() {
		return R229CRM_RISK_WEIGHT;
	}
	public BigDecimal getR229RWA_CRM_COVERED() {
		return R229RWA_CRM_COVERED;
	}
	public BigDecimal getR229ORIGINAL_CP_RISK_WEIGHT() {
		return R229ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR229RWA_NOT_COVERED() {
		return R229RWA_NOT_COVERED;
	}
	public BigDecimal getR229COLLATERAL_CEA_ELIGIBLE() {
		return R229COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR229CEA_AFTER_VOL_ADJ() {
		return R229CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR229COLL_CASH() {
		return R229COLL_CASH;
	}
	public BigDecimal getR229COLL_TBILLS() {
		return R229COLL_TBILLS;
	}
	public BigDecimal getR229COLL_DEBT_SEC() {
		return R229COLL_DEBT_SEC;
	}
	public BigDecimal getR229COLL_EQUITIES() {
		return R229COLL_EQUITIES;
	}
	public BigDecimal getR229COLL_MUTUAL_FUNDS() {
		return R229COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR229COLL_TOTAL() {
		return R229COLL_TOTAL;
	}
	public BigDecimal getR229CEA_AFTER_CRM() {
		return R229CEA_AFTER_CRM;
	}
	public BigDecimal getR229RWA_CEA_NOT_COVERED() {
		return R229RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR229UNSECURED_CEA() {
		return R229UNSECURED_CEA;
	}
	public BigDecimal getR229RWA_UNSECURED_CEA() {
		return R229RWA_UNSECURED_CEA;
	}
	public BigDecimal getR230NOMINAL_PRINCIPAL_AMT() {
		return R230NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR230CCF_PCT() {
		return R230CCF_PCT;
	}
	public BigDecimal getR230CREDIT_EQUIVALENT_AMT() {
		return R230CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR230CEA_ELIGIBLE_NETTING_CP() {
		return R230CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR230CEA_AFTER_NETTING() {
		return R230CEA_AFTER_NETTING;
	}
	public BigDecimal getR230CEA_ELIGIBLE_CRM_SUB() {
		return R230CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR230GUARANTEE_ELIGIBLE() {
		return R230GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR230CREDIT_DERIVATIVES() {
		return R230CREDIT_DERIVATIVES;
	}
	public BigDecimal getR230EXPOSURE_COVERED_CRM() {
		return R230EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR230EXPOSURE_NOT_COVERED_CRM() {
		return R230EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR230RWA_TOTAL() {
		return R230RWA_TOTAL;
	}
	public BigDecimal getR230CRM_RISK_WEIGHT() {
		return R230CRM_RISK_WEIGHT;
	}
	public BigDecimal getR230RWA_CRM_COVERED() {
		return R230RWA_CRM_COVERED;
	}
	public BigDecimal getR230ORIGINAL_CP_RISK_WEIGHT() {
		return R230ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR230RWA_NOT_COVERED() {
		return R230RWA_NOT_COVERED;
	}
	public BigDecimal getR230COLLATERAL_CEA_ELIGIBLE() {
		return R230COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR230CEA_AFTER_VOL_ADJ() {
		return R230CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR230COLL_CASH() {
		return R230COLL_CASH;
	}
	public BigDecimal getR230COLL_TBILLS() {
		return R230COLL_TBILLS;
	}
	public BigDecimal getR230COLL_DEBT_SEC() {
		return R230COLL_DEBT_SEC;
	}
	public BigDecimal getR230COLL_EQUITIES() {
		return R230COLL_EQUITIES;
	}
	public BigDecimal getR230COLL_MUTUAL_FUNDS() {
		return R230COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR230COLL_TOTAL() {
		return R230COLL_TOTAL;
	}
	public BigDecimal getR230CEA_AFTER_CRM() {
		return R230CEA_AFTER_CRM;
	}
	public BigDecimal getR230RWA_CEA_NOT_COVERED() {
		return R230RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR230UNSECURED_CEA() {
		return R230UNSECURED_CEA;
	}
	public BigDecimal getR230RWA_UNSECURED_CEA() {
		return R230RWA_UNSECURED_CEA;
	}
	public BigDecimal getR231NOMINAL_PRINCIPAL_AMT() {
		return R231NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR231CCF_PCT() {
		return R231CCF_PCT;
	}
	public BigDecimal getR231CREDIT_EQUIVALENT_AMT() {
		return R231CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR231CEA_ELIGIBLE_NETTING_CP() {
		return R231CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR231CEA_AFTER_NETTING() {
		return R231CEA_AFTER_NETTING;
	}
	public BigDecimal getR231CEA_ELIGIBLE_CRM_SUB() {
		return R231CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR231GUARANTEE_ELIGIBLE() {
		return R231GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR231CREDIT_DERIVATIVES() {
		return R231CREDIT_DERIVATIVES;
	}
	public BigDecimal getR231EXPOSURE_COVERED_CRM() {
		return R231EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR231EXPOSURE_NOT_COVERED_CRM() {
		return R231EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR231RWA_TOTAL() {
		return R231RWA_TOTAL;
	}
	public BigDecimal getR231CRM_RISK_WEIGHT() {
		return R231CRM_RISK_WEIGHT;
	}
	public BigDecimal getR231RWA_CRM_COVERED() {
		return R231RWA_CRM_COVERED;
	}
	public BigDecimal getR231ORIGINAL_CP_RISK_WEIGHT() {
		return R231ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR231RWA_NOT_COVERED() {
		return R231RWA_NOT_COVERED;
	}
	public BigDecimal getR231COLLATERAL_CEA_ELIGIBLE() {
		return R231COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR231CEA_AFTER_VOL_ADJ() {
		return R231CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR231COLL_CASH() {
		return R231COLL_CASH;
	}
	public BigDecimal getR231COLL_TBILLS() {
		return R231COLL_TBILLS;
	}
	public BigDecimal getR231COLL_DEBT_SEC() {
		return R231COLL_DEBT_SEC;
	}
	public BigDecimal getR231COLL_EQUITIES() {
		return R231COLL_EQUITIES;
	}
	public BigDecimal getR231COLL_MUTUAL_FUNDS() {
		return R231COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR231COLL_TOTAL() {
		return R231COLL_TOTAL;
	}
	public BigDecimal getR231CEA_AFTER_CRM() {
		return R231CEA_AFTER_CRM;
	}
	public BigDecimal getR231RWA_CEA_NOT_COVERED() {
		return R231RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR231UNSECURED_CEA() {
		return R231UNSECURED_CEA;
	}
	public BigDecimal getR231RWA_UNSECURED_CEA() {
		return R231RWA_UNSECURED_CEA;
	}
	public BigDecimal getR232NOMINAL_PRINCIPAL_AMT() {
		return R232NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR232CCF_PCT() {
		return R232CCF_PCT;
	}
	public BigDecimal getR232CREDIT_EQUIVALENT_AMT() {
		return R232CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR232CEA_ELIGIBLE_NETTING_CP() {
		return R232CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR232CEA_AFTER_NETTING() {
		return R232CEA_AFTER_NETTING;
	}
	public BigDecimal getR232CEA_ELIGIBLE_CRM_SUB() {
		return R232CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR232GUARANTEE_ELIGIBLE() {
		return R232GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR232CREDIT_DERIVATIVES() {
		return R232CREDIT_DERIVATIVES;
	}
	public BigDecimal getR232EXPOSURE_COVERED_CRM() {
		return R232EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR232EXPOSURE_NOT_COVERED_CRM() {
		return R232EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR232RWA_TOTAL() {
		return R232RWA_TOTAL;
	}
	public BigDecimal getR232CRM_RISK_WEIGHT() {
		return R232CRM_RISK_WEIGHT;
	}
	public BigDecimal getR232RWA_CRM_COVERED() {
		return R232RWA_CRM_COVERED;
	}
	public BigDecimal getR232ORIGINAL_CP_RISK_WEIGHT() {
		return R232ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR232RWA_NOT_COVERED() {
		return R232RWA_NOT_COVERED;
	}
	public BigDecimal getR232COLLATERAL_CEA_ELIGIBLE() {
		return R232COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR232CEA_AFTER_VOL_ADJ() {
		return R232CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR232COLL_CASH() {
		return R232COLL_CASH;
	}
	public BigDecimal getR232COLL_TBILLS() {
		return R232COLL_TBILLS;
	}
	public BigDecimal getR232COLL_DEBT_SEC() {
		return R232COLL_DEBT_SEC;
	}
	public BigDecimal getR232COLL_EQUITIES() {
		return R232COLL_EQUITIES;
	}
	public BigDecimal getR232COLL_MUTUAL_FUNDS() {
		return R232COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR232COLL_TOTAL() {
		return R232COLL_TOTAL;
	}
	public BigDecimal getR232CEA_AFTER_CRM() {
		return R232CEA_AFTER_CRM;
	}
	public BigDecimal getR232RWA_CEA_NOT_COVERED() {
		return R232RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR232UNSECURED_CEA() {
		return R232UNSECURED_CEA;
	}
	public BigDecimal getR232RWA_UNSECURED_CEA() {
		return R232RWA_UNSECURED_CEA;
	}
	public BigDecimal getR233NOMINAL_PRINCIPAL_AMT() {
		return R233NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR233CCF_PCT() {
		return R233CCF_PCT;
	}
	public BigDecimal getR233CREDIT_EQUIVALENT_AMT() {
		return R233CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR233CEA_ELIGIBLE_NETTING_CP() {
		return R233CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR233CEA_AFTER_NETTING() {
		return R233CEA_AFTER_NETTING;
	}
	public BigDecimal getR233CEA_ELIGIBLE_CRM_SUB() {
		return R233CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR233GUARANTEE_ELIGIBLE() {
		return R233GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR233CREDIT_DERIVATIVES() {
		return R233CREDIT_DERIVATIVES;
	}
	public BigDecimal getR233EXPOSURE_COVERED_CRM() {
		return R233EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR233EXPOSURE_NOT_COVERED_CRM() {
		return R233EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR233RWA_TOTAL() {
		return R233RWA_TOTAL;
	}
	public BigDecimal getR233CRM_RISK_WEIGHT() {
		return R233CRM_RISK_WEIGHT;
	}
	public BigDecimal getR233RWA_CRM_COVERED() {
		return R233RWA_CRM_COVERED;
	}
	public BigDecimal getR233ORIGINAL_CP_RISK_WEIGHT() {
		return R233ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR233RWA_NOT_COVERED() {
		return R233RWA_NOT_COVERED;
	}
	public BigDecimal getR233COLLATERAL_CEA_ELIGIBLE() {
		return R233COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR233CEA_AFTER_VOL_ADJ() {
		return R233CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR233COLL_CASH() {
		return R233COLL_CASH;
	}
	public BigDecimal getR233COLL_TBILLS() {
		return R233COLL_TBILLS;
	}
	public BigDecimal getR233COLL_DEBT_SEC() {
		return R233COLL_DEBT_SEC;
	}
	public BigDecimal getR233COLL_EQUITIES() {
		return R233COLL_EQUITIES;
	}
	public BigDecimal getR233COLL_MUTUAL_FUNDS() {
		return R233COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR233COLL_TOTAL() {
		return R233COLL_TOTAL;
	}
	public BigDecimal getR233CEA_AFTER_CRM() {
		return R233CEA_AFTER_CRM;
	}
	public BigDecimal getR233RWA_CEA_NOT_COVERED() {
		return R233RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR233UNSECURED_CEA() {
		return R233UNSECURED_CEA;
	}
	public BigDecimal getR233RWA_UNSECURED_CEA() {
		return R233RWA_UNSECURED_CEA;
	}
	public BigDecimal getR234NOMINAL_PRINCIPAL_AMT() {
		return R234NOMINAL_PRINCIPAL_AMT;
	}
	public BigDecimal getR234CCF_PCT() {
		return R234CCF_PCT;
	}
	public BigDecimal getR234CREDIT_EQUIVALENT_AMT() {
		return R234CREDIT_EQUIVALENT_AMT;
	}
	public BigDecimal getR234CEA_ELIGIBLE_NETTING_CP() {
		return R234CEA_ELIGIBLE_NETTING_CP;
	}
	public BigDecimal getR234CEA_AFTER_NETTING() {
		return R234CEA_AFTER_NETTING;
	}
	public BigDecimal getR234CEA_ELIGIBLE_CRM_SUB() {
		return R234CEA_ELIGIBLE_CRM_SUB;
	}
	public BigDecimal getR234GUARANTEE_ELIGIBLE() {
		return R234GUARANTEE_ELIGIBLE;
	}
	public BigDecimal getR234CREDIT_DERIVATIVES() {
		return R234CREDIT_DERIVATIVES;
	}
	public BigDecimal getR234EXPOSURE_COVERED_CRM() {
		return R234EXPOSURE_COVERED_CRM;
	}
	public BigDecimal getR234EXPOSURE_NOT_COVERED_CRM() {
		return R234EXPOSURE_NOT_COVERED_CRM;
	}
	public BigDecimal getR234RWA_TOTAL() {
		return R234RWA_TOTAL;
	}
	public BigDecimal getR234CRM_RISK_WEIGHT() {
		return R234CRM_RISK_WEIGHT;
	}
	public BigDecimal getR234RWA_CRM_COVERED() {
		return R234RWA_CRM_COVERED;
	}
	public BigDecimal getR234ORIGINAL_CP_RISK_WEIGHT() {
		return R234ORIGINAL_CP_RISK_WEIGHT;
	}
	public BigDecimal getR234RWA_NOT_COVERED() {
		return R234RWA_NOT_COVERED;
	}
	public BigDecimal getR234COLLATERAL_CEA_ELIGIBLE() {
		return R234COLLATERAL_CEA_ELIGIBLE;
	}
	public BigDecimal getR234CEA_AFTER_VOL_ADJ() {
		return R234CEA_AFTER_VOL_ADJ;
	}
	public BigDecimal getR234COLL_CASH() {
		return R234COLL_CASH;
	}
	public BigDecimal getR234COLL_TBILLS() {
		return R234COLL_TBILLS;
	}
	public BigDecimal getR234COLL_DEBT_SEC() {
		return R234COLL_DEBT_SEC;
	}
	public BigDecimal getR234COLL_EQUITIES() {
		return R234COLL_EQUITIES;
	}
	public BigDecimal getR234COLL_MUTUAL_FUNDS() {
		return R234COLL_MUTUAL_FUNDS;
	}
	public BigDecimal getR234COLL_TOTAL() {
		return R234COLL_TOTAL;
	}
	public BigDecimal getR234CEA_AFTER_CRM() {
		return R234CEA_AFTER_CRM;
	}
	public BigDecimal getR234RWA_CEA_NOT_COVERED() {
		return R234RWA_CEA_NOT_COVERED;
	}
	public BigDecimal getR234UNSECURED_CEA() {
		return R234UNSECURED_CEA;
	}
	public BigDecimal getR234RWA_UNSECURED_CEA() {
		return R234RWA_UNSECURED_CEA;
	}
	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}
	public void setREPORT_VERSION(String rEPORT_VERSION) {
		REPORT_VERSION = rEPORT_VERSION;
	}
	public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
		REPORT_FREQUENCY = rEPORT_FREQUENCY;
	}
	public void setREPORT_CODE(String rEPORT_CODE) {
		REPORT_CODE = rEPORT_CODE;
	}
	public void setREPORT_DESC(String rEPORT_DESC) {
		REPORT_DESC = rEPORT_DESC;
	}
	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}
	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}
	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}
	public void setR216_NOMINAL_PRINCIPAL_AMT(BigDecimal r216_NOMINAL_PRINCIPAL_AMT) {
		R216_NOMINAL_PRINCIPAL_AMT = r216_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR216_CCF_PCT(BigDecimal r216_CCF_PCT) {
		R216_CCF_PCT = r216_CCF_PCT;
	}
	public void setR216_CREDIT_EQUIVALENT_AMT(BigDecimal r216_CREDIT_EQUIVALENT_AMT) {
		R216_CREDIT_EQUIVALENT_AMT = r216_CREDIT_EQUIVALENT_AMT;
	}
	public void setR216_CEA_ELIGIBLE_NETTING_CP(BigDecimal r216_CEA_ELIGIBLE_NETTING_CP) {
		R216_CEA_ELIGIBLE_NETTING_CP = r216_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR216_CEA_AFTER_NETTING(BigDecimal r216_CEA_AFTER_NETTING) {
		R216_CEA_AFTER_NETTING = r216_CEA_AFTER_NETTING;
	}
	public void setR216_CEA_ELIGIBLE_CRM_SUB(BigDecimal r216_CEA_ELIGIBLE_CRM_SUB) {
		R216_CEA_ELIGIBLE_CRM_SUB = r216_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR216_GUARANTEE_ELIGIBLE(BigDecimal r216_GUARANTEE_ELIGIBLE) {
		R216_GUARANTEE_ELIGIBLE = r216_GUARANTEE_ELIGIBLE;
	}
	public void setR216_CREDIT_DERIVATIVES(BigDecimal r216_CREDIT_DERIVATIVES) {
		R216_CREDIT_DERIVATIVES = r216_CREDIT_DERIVATIVES;
	}
	public void setR216_EXPOSURE_COVERED_CRM(BigDecimal r216_EXPOSURE_COVERED_CRM) {
		R216_EXPOSURE_COVERED_CRM = r216_EXPOSURE_COVERED_CRM;
	}
	public void setR216_EXPOSURE_NOT_COVERED_CRM(BigDecimal r216_EXPOSURE_NOT_COVERED_CRM) {
		R216_EXPOSURE_NOT_COVERED_CRM = r216_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR216_RWA_TOTAL(BigDecimal r216_RWA_TOTAL) {
		R216_RWA_TOTAL = r216_RWA_TOTAL;
	}
	public void setR216_CRM_RISK_WEIGHT(BigDecimal r216_CRM_RISK_WEIGHT) {
		R216_CRM_RISK_WEIGHT = r216_CRM_RISK_WEIGHT;
	}
	public void setR216_RWA_CRM_COVERED(BigDecimal r216_RWA_CRM_COVERED) {
		R216_RWA_CRM_COVERED = r216_RWA_CRM_COVERED;
	}
	public void setR216_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r216_ORIGINAL_CP_RISK_WEIGHT) {
		R216_ORIGINAL_CP_RISK_WEIGHT = r216_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR216_RWA_NOT_COVERED(BigDecimal r216_RWA_NOT_COVERED) {
		R216_RWA_NOT_COVERED = r216_RWA_NOT_COVERED;
	}
	public void setR216_COLLATERAL_CEA_ELIGIBLE(BigDecimal r216_COLLATERAL_CEA_ELIGIBLE) {
		R216_COLLATERAL_CEA_ELIGIBLE = r216_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR216_CEA_AFTER_VOL_ADJ(BigDecimal r216_CEA_AFTER_VOL_ADJ) {
		R216_CEA_AFTER_VOL_ADJ = r216_CEA_AFTER_VOL_ADJ;
	}
	public void setR216_COLL_CASH(BigDecimal r216_COLL_CASH) {
		R216_COLL_CASH = r216_COLL_CASH;
	}
	public void setR216_COLL_TBILLS(BigDecimal r216_COLL_TBILLS) {
		R216_COLL_TBILLS = r216_COLL_TBILLS;
	}
	public void setR216_COLL_DEBT_SEC(BigDecimal r216_COLL_DEBT_SEC) {
		R216_COLL_DEBT_SEC = r216_COLL_DEBT_SEC;
	}
	public void setR216_COLL_EQUITIES(BigDecimal r216_COLL_EQUITIES) {
		R216_COLL_EQUITIES = r216_COLL_EQUITIES;
	}
	public void setR216_COLL_MUTUAL_FUNDS(BigDecimal r216_COLL_MUTUAL_FUNDS) {
		R216_COLL_MUTUAL_FUNDS = r216_COLL_MUTUAL_FUNDS;
	}
	public void setR216_COLL_TOTAL(BigDecimal r216_COLL_TOTAL) {
		R216_COLL_TOTAL = r216_COLL_TOTAL;
	}
	public void setR216_CEA_AFTER_CRM(BigDecimal r216_CEA_AFTER_CRM) {
		R216_CEA_AFTER_CRM = r216_CEA_AFTER_CRM;
	}
	public void setR216_RWA_CEA_NOT_COVERED(BigDecimal r216_RWA_CEA_NOT_COVERED) {
		R216_RWA_CEA_NOT_COVERED = r216_RWA_CEA_NOT_COVERED;
	}
	public void setR216_UNSECURED_CEA(BigDecimal r216_UNSECURED_CEA) {
		R216_UNSECURED_CEA = r216_UNSECURED_CEA;
	}
	public void setR216_RWA_UNSECURED_CEA(BigDecimal r216_RWA_UNSECURED_CEA) {
		R216_RWA_UNSECURED_CEA = r216_RWA_UNSECURED_CEA;
	}
	public void setR217_NOMINAL_PRINCIPAL_AMT(BigDecimal r217_NOMINAL_PRINCIPAL_AMT) {
		R217_NOMINAL_PRINCIPAL_AMT = r217_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR217_CCF_PCT(BigDecimal r217_CCF_PCT) {
		R217_CCF_PCT = r217_CCF_PCT;
	}
	public void setR217_CREDIT_EQUIVALENT_AMT(BigDecimal r217_CREDIT_EQUIVALENT_AMT) {
		R217_CREDIT_EQUIVALENT_AMT = r217_CREDIT_EQUIVALENT_AMT;
	}
	public void setR217_CEA_ELIGIBLE_NETTING_CP(BigDecimal r217_CEA_ELIGIBLE_NETTING_CP) {
		R217_CEA_ELIGIBLE_NETTING_CP = r217_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR217_CEA_AFTER_NETTING(BigDecimal r217_CEA_AFTER_NETTING) {
		R217_CEA_AFTER_NETTING = r217_CEA_AFTER_NETTING;
	}
	public void setR217_CEA_ELIGIBLE_CRM_SUB(BigDecimal r217_CEA_ELIGIBLE_CRM_SUB) {
		R217_CEA_ELIGIBLE_CRM_SUB = r217_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR217_GUARANTEE_ELIGIBLE(BigDecimal r217_GUARANTEE_ELIGIBLE) {
		R217_GUARANTEE_ELIGIBLE = r217_GUARANTEE_ELIGIBLE;
	}
	public void setR217_CREDIT_DERIVATIVES(BigDecimal r217_CREDIT_DERIVATIVES) {
		R217_CREDIT_DERIVATIVES = r217_CREDIT_DERIVATIVES;
	}
	public void setR217_EXPOSURE_COVERED_CRM(BigDecimal r217_EXPOSURE_COVERED_CRM) {
		R217_EXPOSURE_COVERED_CRM = r217_EXPOSURE_COVERED_CRM;
	}
	public void setR217_EXPOSURE_NOT_COVERED_CRM(BigDecimal r217_EXPOSURE_NOT_COVERED_CRM) {
		R217_EXPOSURE_NOT_COVERED_CRM = r217_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR217_RWA_TOTAL(BigDecimal r217_RWA_TOTAL) {
		R217_RWA_TOTAL = r217_RWA_TOTAL;
	}
	public void setR217_CRM_RISK_WEIGHT(BigDecimal r217_CRM_RISK_WEIGHT) {
		R217_CRM_RISK_WEIGHT = r217_CRM_RISK_WEIGHT;
	}
	public void setR217_RWA_CRM_COVERED(BigDecimal r217_RWA_CRM_COVERED) {
		R217_RWA_CRM_COVERED = r217_RWA_CRM_COVERED;
	}
	public void setR217_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r217_ORIGINAL_CP_RISK_WEIGHT) {
		R217_ORIGINAL_CP_RISK_WEIGHT = r217_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR217_RWA_NOT_COVERED(BigDecimal r217_RWA_NOT_COVERED) {
		R217_RWA_NOT_COVERED = r217_RWA_NOT_COVERED;
	}
	public void setR217_COLLATERAL_CEA_ELIGIBLE(BigDecimal r217_COLLATERAL_CEA_ELIGIBLE) {
		R217_COLLATERAL_CEA_ELIGIBLE = r217_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR217_CEA_AFTER_VOL_ADJ(BigDecimal r217_CEA_AFTER_VOL_ADJ) {
		R217_CEA_AFTER_VOL_ADJ = r217_CEA_AFTER_VOL_ADJ;
	}
	public void setR217_COLL_CASH(BigDecimal r217_COLL_CASH) {
		R217_COLL_CASH = r217_COLL_CASH;
	}
	public void setR217_COLL_TBILLS(BigDecimal r217_COLL_TBILLS) {
		R217_COLL_TBILLS = r217_COLL_TBILLS;
	}
	public void setR217_COLL_DEBT_SEC(BigDecimal r217_COLL_DEBT_SEC) {
		R217_COLL_DEBT_SEC = r217_COLL_DEBT_SEC;
	}
	public void setR217_COLL_EQUITIES(BigDecimal r217_COLL_EQUITIES) {
		R217_COLL_EQUITIES = r217_COLL_EQUITIES;
	}
	public void setR217_COLL_MUTUAL_FUNDS(BigDecimal r217_COLL_MUTUAL_FUNDS) {
		R217_COLL_MUTUAL_FUNDS = r217_COLL_MUTUAL_FUNDS;
	}
	public void setR217_COLL_TOTAL(BigDecimal r217_COLL_TOTAL) {
		R217_COLL_TOTAL = r217_COLL_TOTAL;
	}
	public void setR217_CEA_AFTER_CRM(BigDecimal r217_CEA_AFTER_CRM) {
		R217_CEA_AFTER_CRM = r217_CEA_AFTER_CRM;
	}
	public void setR217_RWA_CEA_NOT_COVERED(BigDecimal r217_RWA_CEA_NOT_COVERED) {
		R217_RWA_CEA_NOT_COVERED = r217_RWA_CEA_NOT_COVERED;
	}
	public void setR217_UNSECURED_CEA(BigDecimal r217_UNSECURED_CEA) {
		R217_UNSECURED_CEA = r217_UNSECURED_CEA;
	}
	public void setR217_RWA_UNSECURED_CEA(BigDecimal r217_RWA_UNSECURED_CEA) {
		R217_RWA_UNSECURED_CEA = r217_RWA_UNSECURED_CEA;
	}
	public void setR218_NOMINAL_PRINCIPAL_AMT(BigDecimal r218_NOMINAL_PRINCIPAL_AMT) {
		R218_NOMINAL_PRINCIPAL_AMT = r218_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR218_CCF_PCT(BigDecimal r218_CCF_PCT) {
		R218_CCF_PCT = r218_CCF_PCT;
	}
	public void setR218_CREDIT_EQUIVALENT_AMT(BigDecimal r218_CREDIT_EQUIVALENT_AMT) {
		R218_CREDIT_EQUIVALENT_AMT = r218_CREDIT_EQUIVALENT_AMT;
	}
	public void setR218_CEA_ELIGIBLE_NETTING_CP(BigDecimal r218_CEA_ELIGIBLE_NETTING_CP) {
		R218_CEA_ELIGIBLE_NETTING_CP = r218_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR218_CEA_AFTER_NETTING(BigDecimal r218_CEA_AFTER_NETTING) {
		R218_CEA_AFTER_NETTING = r218_CEA_AFTER_NETTING;
	}
	public void setR218_CEA_ELIGIBLE_CRM_SUB(BigDecimal r218_CEA_ELIGIBLE_CRM_SUB) {
		R218_CEA_ELIGIBLE_CRM_SUB = r218_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR218_GUARANTEE_ELIGIBLE(BigDecimal r218_GUARANTEE_ELIGIBLE) {
		R218_GUARANTEE_ELIGIBLE = r218_GUARANTEE_ELIGIBLE;
	}
	public void setR218_CREDIT_DERIVATIVES(BigDecimal r218_CREDIT_DERIVATIVES) {
		R218_CREDIT_DERIVATIVES = r218_CREDIT_DERIVATIVES;
	}
	public void setR218_EXPOSURE_COVERED_CRM(BigDecimal r218_EXPOSURE_COVERED_CRM) {
		R218_EXPOSURE_COVERED_CRM = r218_EXPOSURE_COVERED_CRM;
	}
	public void setR218_EXPOSURE_NOT_COVERED_CRM(BigDecimal r218_EXPOSURE_NOT_COVERED_CRM) {
		R218_EXPOSURE_NOT_COVERED_CRM = r218_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR218_RWA_TOTAL(BigDecimal r218_RWA_TOTAL) {
		R218_RWA_TOTAL = r218_RWA_TOTAL;
	}
	public void setR218_CRM_RISK_WEIGHT(BigDecimal r218_CRM_RISK_WEIGHT) {
		R218_CRM_RISK_WEIGHT = r218_CRM_RISK_WEIGHT;
	}
	public void setR218_RWA_CRM_COVERED(BigDecimal r218_RWA_CRM_COVERED) {
		R218_RWA_CRM_COVERED = r218_RWA_CRM_COVERED;
	}
	public void setR218_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r218_ORIGINAL_CP_RISK_WEIGHT) {
		R218_ORIGINAL_CP_RISK_WEIGHT = r218_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR218_RWA_NOT_COVERED(BigDecimal r218_RWA_NOT_COVERED) {
		R218_RWA_NOT_COVERED = r218_RWA_NOT_COVERED;
	}
	public void setR218_COLLATERAL_CEA_ELIGIBLE(BigDecimal r218_COLLATERAL_CEA_ELIGIBLE) {
		R218_COLLATERAL_CEA_ELIGIBLE = r218_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR218_CEA_AFTER_VOL_ADJ(BigDecimal r218_CEA_AFTER_VOL_ADJ) {
		R218_CEA_AFTER_VOL_ADJ = r218_CEA_AFTER_VOL_ADJ;
	}
	public void setR218_COLL_CASH(BigDecimal r218_COLL_CASH) {
		R218_COLL_CASH = r218_COLL_CASH;
	}
	public void setR218_COLL_TBILLS(BigDecimal r218_COLL_TBILLS) {
		R218_COLL_TBILLS = r218_COLL_TBILLS;
	}
	public void setR218_COLL_DEBT_SEC(BigDecimal r218_COLL_DEBT_SEC) {
		R218_COLL_DEBT_SEC = r218_COLL_DEBT_SEC;
	}
	public void setR218_COLL_EQUITIES(BigDecimal r218_COLL_EQUITIES) {
		R218_COLL_EQUITIES = r218_COLL_EQUITIES;
	}
	public void setR218_COLL_MUTUAL_FUNDS(BigDecimal r218_COLL_MUTUAL_FUNDS) {
		R218_COLL_MUTUAL_FUNDS = r218_COLL_MUTUAL_FUNDS;
	}
	public void setR218_COLL_TOTAL(BigDecimal r218_COLL_TOTAL) {
		R218_COLL_TOTAL = r218_COLL_TOTAL;
	}
	public void setR218_CEA_AFTER_CRM(BigDecimal r218_CEA_AFTER_CRM) {
		R218_CEA_AFTER_CRM = r218_CEA_AFTER_CRM;
	}
	public void setR218_RWA_CEA_NOT_COVERED(BigDecimal r218_RWA_CEA_NOT_COVERED) {
		R218_RWA_CEA_NOT_COVERED = r218_RWA_CEA_NOT_COVERED;
	}
	public void setR218_UNSECURED_CEA(BigDecimal r218_UNSECURED_CEA) {
		R218_UNSECURED_CEA = r218_UNSECURED_CEA;
	}
	public void setR218_RWA_UNSECURED_CEA(BigDecimal r218_RWA_UNSECURED_CEA) {
		R218_RWA_UNSECURED_CEA = r218_RWA_UNSECURED_CEA;
	}
	public void setR219_NOMINAL_PRINCIPAL_AMT(BigDecimal r219_NOMINAL_PRINCIPAL_AMT) {
		R219_NOMINAL_PRINCIPAL_AMT = r219_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR219_CCF_PCT(BigDecimal r219_CCF_PCT) {
		R219_CCF_PCT = r219_CCF_PCT;
	}
	public void setR219_CREDIT_EQUIVALENT_AMT(BigDecimal r219_CREDIT_EQUIVALENT_AMT) {
		R219_CREDIT_EQUIVALENT_AMT = r219_CREDIT_EQUIVALENT_AMT;
	}
	public void setR219_CEA_ELIGIBLE_NETTING_CP(BigDecimal r219_CEA_ELIGIBLE_NETTING_CP) {
		R219_CEA_ELIGIBLE_NETTING_CP = r219_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR219_CEA_AFTER_NETTING(BigDecimal r219_CEA_AFTER_NETTING) {
		R219_CEA_AFTER_NETTING = r219_CEA_AFTER_NETTING;
	}
	public void setR219_CEA_ELIGIBLE_CRM_SUB(BigDecimal r219_CEA_ELIGIBLE_CRM_SUB) {
		R219_CEA_ELIGIBLE_CRM_SUB = r219_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR219_GUARANTEE_ELIGIBLE(BigDecimal r219_GUARANTEE_ELIGIBLE) {
		R219_GUARANTEE_ELIGIBLE = r219_GUARANTEE_ELIGIBLE;
	}
	public void setR219_CREDIT_DERIVATIVES(BigDecimal r219_CREDIT_DERIVATIVES) {
		R219_CREDIT_DERIVATIVES = r219_CREDIT_DERIVATIVES;
	}
	public void setR219_EXPOSURE_COVERED_CRM(BigDecimal r219_EXPOSURE_COVERED_CRM) {
		R219_EXPOSURE_COVERED_CRM = r219_EXPOSURE_COVERED_CRM;
	}
	public void setR219_EXPOSURE_NOT_COVERED_CRM(BigDecimal r219_EXPOSURE_NOT_COVERED_CRM) {
		R219_EXPOSURE_NOT_COVERED_CRM = r219_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR219_RWA_TOTAL(BigDecimal r219_RWA_TOTAL) {
		R219_RWA_TOTAL = r219_RWA_TOTAL;
	}
	public void setR219_CRM_RISK_WEIGHT(BigDecimal r219_CRM_RISK_WEIGHT) {
		R219_CRM_RISK_WEIGHT = r219_CRM_RISK_WEIGHT;
	}
	public void setR219_RWA_CRM_COVERED(BigDecimal r219_RWA_CRM_COVERED) {
		R219_RWA_CRM_COVERED = r219_RWA_CRM_COVERED;
	}
	public void setR219_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r219_ORIGINAL_CP_RISK_WEIGHT) {
		R219_ORIGINAL_CP_RISK_WEIGHT = r219_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR219_RWA_NOT_COVERED(BigDecimal r219_RWA_NOT_COVERED) {
		R219_RWA_NOT_COVERED = r219_RWA_NOT_COVERED;
	}
	public void setR219_COLLATERAL_CEA_ELIGIBLE(BigDecimal r219_COLLATERAL_CEA_ELIGIBLE) {
		R219_COLLATERAL_CEA_ELIGIBLE = r219_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR219_CEA_AFTER_VOL_ADJ(BigDecimal r219_CEA_AFTER_VOL_ADJ) {
		R219_CEA_AFTER_VOL_ADJ = r219_CEA_AFTER_VOL_ADJ;
	}
	public void setR219_COLL_CASH(BigDecimal r219_COLL_CASH) {
		R219_COLL_CASH = r219_COLL_CASH;
	}
	public void setR219_COLL_TBILLS(BigDecimal r219_COLL_TBILLS) {
		R219_COLL_TBILLS = r219_COLL_TBILLS;
	}
	public void setR219_COLL_DEBT_SEC(BigDecimal r219_COLL_DEBT_SEC) {
		R219_COLL_DEBT_SEC = r219_COLL_DEBT_SEC;
	}
	public void setR219_COLL_EQUITIES(BigDecimal r219_COLL_EQUITIES) {
		R219_COLL_EQUITIES = r219_COLL_EQUITIES;
	}
	public void setR219_COLL_MUTUAL_FUNDS(BigDecimal r219_COLL_MUTUAL_FUNDS) {
		R219_COLL_MUTUAL_FUNDS = r219_COLL_MUTUAL_FUNDS;
	}
	public void setR219_COLL_TOTAL(BigDecimal r219_COLL_TOTAL) {
		R219_COLL_TOTAL = r219_COLL_TOTAL;
	}
	public void setR219_CEA_AFTER_CRM(BigDecimal r219_CEA_AFTER_CRM) {
		R219_CEA_AFTER_CRM = r219_CEA_AFTER_CRM;
	}
	public void setR219_RWA_CEA_NOT_COVERED(BigDecimal r219_RWA_CEA_NOT_COVERED) {
		R219_RWA_CEA_NOT_COVERED = r219_RWA_CEA_NOT_COVERED;
	}
	public void setR219_UNSECURED_CEA(BigDecimal r219_UNSECURED_CEA) {
		R219_UNSECURED_CEA = r219_UNSECURED_CEA;
	}
	public void setR219_RWA_UNSECURED_CEA(BigDecimal r219_RWA_UNSECURED_CEA) {
		R219_RWA_UNSECURED_CEA = r219_RWA_UNSECURED_CEA;
	}
	public void setR220_NOMINAL_PRINCIPAL_AMT(BigDecimal r220_NOMINAL_PRINCIPAL_AMT) {
		R220_NOMINAL_PRINCIPAL_AMT = r220_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR220_CCF_PCT(BigDecimal r220_CCF_PCT) {
		R220_CCF_PCT = r220_CCF_PCT;
	}
	public void setR220_CREDIT_EQUIVALENT_AMT(BigDecimal r220_CREDIT_EQUIVALENT_AMT) {
		R220_CREDIT_EQUIVALENT_AMT = r220_CREDIT_EQUIVALENT_AMT;
	}
	public void setR220_CEA_ELIGIBLE_NETTING_CP(BigDecimal r220_CEA_ELIGIBLE_NETTING_CP) {
		R220_CEA_ELIGIBLE_NETTING_CP = r220_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR220_CEA_AFTER_NETTING(BigDecimal r220_CEA_AFTER_NETTING) {
		R220_CEA_AFTER_NETTING = r220_CEA_AFTER_NETTING;
	}
	public void setR220_CEA_ELIGIBLE_CRM_SUB(BigDecimal r220_CEA_ELIGIBLE_CRM_SUB) {
		R220_CEA_ELIGIBLE_CRM_SUB = r220_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR220_GUARANTEE_ELIGIBLE(BigDecimal r220_GUARANTEE_ELIGIBLE) {
		R220_GUARANTEE_ELIGIBLE = r220_GUARANTEE_ELIGIBLE;
	}
	public void setR220_CREDIT_DERIVATIVES(BigDecimal r220_CREDIT_DERIVATIVES) {
		R220_CREDIT_DERIVATIVES = r220_CREDIT_DERIVATIVES;
	}
	public void setR220_EXPOSURE_COVERED_CRM(BigDecimal r220_EXPOSURE_COVERED_CRM) {
		R220_EXPOSURE_COVERED_CRM = r220_EXPOSURE_COVERED_CRM;
	}
	public void setR220_EXPOSURE_NOT_COVERED_CRM(BigDecimal r220_EXPOSURE_NOT_COVERED_CRM) {
		R220_EXPOSURE_NOT_COVERED_CRM = r220_EXPOSURE_NOT_COVERED_CRM;
	}
	public void setR220_RWA_TOTAL(BigDecimal r220_RWA_TOTAL) {
		R220_RWA_TOTAL = r220_RWA_TOTAL;
	}
	public void setR220_CRM_RISK_WEIGHT(BigDecimal r220_CRM_RISK_WEIGHT) {
		R220_CRM_RISK_WEIGHT = r220_CRM_RISK_WEIGHT;
	}
	public void setR220_RWA_CRM_COVERED(BigDecimal r220_RWA_CRM_COVERED) {
		R220_RWA_CRM_COVERED = r220_RWA_CRM_COVERED;
	}
	public void setR220_ORIGINAL_CP_RISK_WEIGHT(BigDecimal r220_ORIGINAL_CP_RISK_WEIGHT) {
		R220_ORIGINAL_CP_RISK_WEIGHT = r220_ORIGINAL_CP_RISK_WEIGHT;
	}
	public void setR220_RWA_NOT_COVERED(BigDecimal r220_RWA_NOT_COVERED) {
		R220_RWA_NOT_COVERED = r220_RWA_NOT_COVERED;
	}
	public void setR220_COLLATERAL_CEA_ELIGIBLE(BigDecimal r220_COLLATERAL_CEA_ELIGIBLE) {
		R220_COLLATERAL_CEA_ELIGIBLE = r220_COLLATERAL_CEA_ELIGIBLE;
	}
	public void setR220_CEA_AFTER_VOL_ADJ(BigDecimal r220_CEA_AFTER_VOL_ADJ) {
		R220_CEA_AFTER_VOL_ADJ = r220_CEA_AFTER_VOL_ADJ;
	}
	public void setR220_COLL_CASH(BigDecimal r220_COLL_CASH) {
		R220_COLL_CASH = r220_COLL_CASH;
	}
	public void setR220_COLL_TBILLS(BigDecimal r220_COLL_TBILLS) {
		R220_COLL_TBILLS = r220_COLL_TBILLS;
	}
	public void setR220_COLL_DEBT_SEC(BigDecimal r220_COLL_DEBT_SEC) {
		R220_COLL_DEBT_SEC = r220_COLL_DEBT_SEC;
	}
	public void setR220_COLL_EQUITIES(BigDecimal r220_COLL_EQUITIES) {
		R220_COLL_EQUITIES = r220_COLL_EQUITIES;
	}
	public void setR220_COLL_MUTUAL_FUNDS(BigDecimal r220_COLL_MUTUAL_FUNDS) {
		R220_COLL_MUTUAL_FUNDS = r220_COLL_MUTUAL_FUNDS;
	}
	public void setR220_COLL_TOTAL(BigDecimal r220_COLL_TOTAL) {
		R220_COLL_TOTAL = r220_COLL_TOTAL;
	}
	public void setR220_CEA_AFTER_CRM(BigDecimal r220_CEA_AFTER_CRM) {
		R220_CEA_AFTER_CRM = r220_CEA_AFTER_CRM;
	}
	public void setR220_RWA_CEA_NOT_COVERED(BigDecimal r220_RWA_CEA_NOT_COVERED) {
		R220_RWA_CEA_NOT_COVERED = r220_RWA_CEA_NOT_COVERED;
	}
	public void setR220_UNSECURED_CEA(BigDecimal r220_UNSECURED_CEA) {
		R220_UNSECURED_CEA = r220_UNSECURED_CEA;
	}
	public void setR220_RWA_UNSECURED_CEA(BigDecimal r220_RWA_UNSECURED_CEA) {
		R220_RWA_UNSECURED_CEA = r220_RWA_UNSECURED_CEA;
	}
	public void setR221_NOMINAL_PRINCIPAL_AMT(BigDecimal r221_NOMINAL_PRINCIPAL_AMT) {
		R221_NOMINAL_PRINCIPAL_AMT = r221_NOMINAL_PRINCIPAL_AMT;
	}
	public void setR221_CCF_PCT(BigDecimal r221_CCF_PCT) {
		R221_CCF_PCT = r221_CCF_PCT;
	}
	public void setR221_CREDIT_EQUIVALENT_AMT(BigDecimal r221_CREDIT_EQUIVALENT_AMT) {
		R221_CREDIT_EQUIVALENT_AMT = r221_CREDIT_EQUIVALENT_AMT;
	}
	public void setR221_CEA_ELIGIBLE_NETTING_CP(BigDecimal r221_CEA_ELIGIBLE_NETTING_CP) {
		R221_CEA_ELIGIBLE_NETTING_CP = r221_CEA_ELIGIBLE_NETTING_CP;
	}
	public void setR221_CEA_AFTER_NETTING(BigDecimal r221_CEA_AFTER_NETTING) {
		R221_CEA_AFTER_NETTING = r221_CEA_AFTER_NETTING;
	}
	public void setR221_CEA_ELIGIBLE_CRM_SUB(BigDecimal r221_CEA_ELIGIBLE_CRM_SUB) {
		R221_CEA_ELIGIBLE_CRM_SUB = r221_CEA_ELIGIBLE_CRM_SUB;
	}
	public void setR221GUARANTEE_ELIGIBLE(BigDecimal r221guarantee_ELIGIBLE) {
		R221GUARANTEE_ELIGIBLE = r221guarantee_ELIGIBLE;
	}
	public void setR221CREDIT_DERIVATIVES(BigDecimal r221credit_DERIVATIVES) {
		R221CREDIT_DERIVATIVES = r221credit_DERIVATIVES;
	}
	public void setR221EXPOSURE_COVERED_CRM(BigDecimal r221exposure_COVERED_CRM) {
		R221EXPOSURE_COVERED_CRM = r221exposure_COVERED_CRM;
	}
	public void setR221EXPOSURE_NOT_COVERED_CRM(BigDecimal r221exposure_NOT_COVERED_CRM) {
		R221EXPOSURE_NOT_COVERED_CRM = r221exposure_NOT_COVERED_CRM;
	}
	public void setR221RWA_TOTAL(BigDecimal r221rwa_TOTAL) {
		R221RWA_TOTAL = r221rwa_TOTAL;
	}
	public void setR221CRM_RISK_WEIGHT(BigDecimal r221crm_RISK_WEIGHT) {
		R221CRM_RISK_WEIGHT = r221crm_RISK_WEIGHT;
	}
	public void setR221RWA_CRM_COVERED(BigDecimal r221rwa_CRM_COVERED) {
		R221RWA_CRM_COVERED = r221rwa_CRM_COVERED;
	}
	public void setR221ORIGINAL_CP_RISK_WEIGHT(BigDecimal r221original_CP_RISK_WEIGHT) {
		R221ORIGINAL_CP_RISK_WEIGHT = r221original_CP_RISK_WEIGHT;
	}
	public void setR221RWA_NOT_COVERED(BigDecimal r221rwa_NOT_COVERED) {
		R221RWA_NOT_COVERED = r221rwa_NOT_COVERED;
	}
	public void setR221COLLATERAL_CEA_ELIGIBLE(BigDecimal r221collateral_CEA_ELIGIBLE) {
		R221COLLATERAL_CEA_ELIGIBLE = r221collateral_CEA_ELIGIBLE;
	}
	public void setR221CEA_AFTER_VOL_ADJ(BigDecimal r221cea_AFTER_VOL_ADJ) {
		R221CEA_AFTER_VOL_ADJ = r221cea_AFTER_VOL_ADJ;
	}
	public void setR221COLL_CASH(BigDecimal r221coll_CASH) {
		R221COLL_CASH = r221coll_CASH;
	}
	public void setR221COLL_TBILLS(BigDecimal r221coll_TBILLS) {
		R221COLL_TBILLS = r221coll_TBILLS;
	}
	public void setR221COLL_DEBT_SEC(BigDecimal r221coll_DEBT_SEC) {
		R221COLL_DEBT_SEC = r221coll_DEBT_SEC;
	}
	public void setR221COLL_EQUITIES(BigDecimal r221coll_EQUITIES) {
		R221COLL_EQUITIES = r221coll_EQUITIES;
	}
	public void setR221COLL_MUTUAL_FUNDS(BigDecimal r221coll_MUTUAL_FUNDS) {
		R221COLL_MUTUAL_FUNDS = r221coll_MUTUAL_FUNDS;
	}
	public void setR221COLL_TOTAL(BigDecimal r221coll_TOTAL) {
		R221COLL_TOTAL = r221coll_TOTAL;
	}
	public void setR221CEA_AFTER_CRM(BigDecimal r221cea_AFTER_CRM) {
		R221CEA_AFTER_CRM = r221cea_AFTER_CRM;
	}
	public void setR221RWA_CEA_NOT_COVERED(BigDecimal r221rwa_CEA_NOT_COVERED) {
		R221RWA_CEA_NOT_COVERED = r221rwa_CEA_NOT_COVERED;
	}
	public void setR221UNSECURED_CEA(BigDecimal r221unsecured_CEA) {
		R221UNSECURED_CEA = r221unsecured_CEA;
	}
	public void setR221RWA_UNSECURED_CEA(BigDecimal r221rwa_UNSECURED_CEA) {
		R221RWA_UNSECURED_CEA = r221rwa_UNSECURED_CEA;
	}
	public void setR222NOMINAL_PRINCIPAL_AMT(BigDecimal r222nominal_PRINCIPAL_AMT) {
		R222NOMINAL_PRINCIPAL_AMT = r222nominal_PRINCIPAL_AMT;
	}
	public void setR222CCF_PCT(BigDecimal r222ccf_PCT) {
		R222CCF_PCT = r222ccf_PCT;
	}
	public void setR222CREDIT_EQUIVALENT_AMT(BigDecimal r222credit_EQUIVALENT_AMT) {
		R222CREDIT_EQUIVALENT_AMT = r222credit_EQUIVALENT_AMT;
	}
	public void setR222CEA_ELIGIBLE_NETTING_CP(BigDecimal r222cea_ELIGIBLE_NETTING_CP) {
		R222CEA_ELIGIBLE_NETTING_CP = r222cea_ELIGIBLE_NETTING_CP;
	}
	public void setR222CEA_AFTER_NETTING(BigDecimal r222cea_AFTER_NETTING) {
		R222CEA_AFTER_NETTING = r222cea_AFTER_NETTING;
	}
	public void setR222CEA_ELIGIBLE_CRM_SUB(BigDecimal r222cea_ELIGIBLE_CRM_SUB) {
		R222CEA_ELIGIBLE_CRM_SUB = r222cea_ELIGIBLE_CRM_SUB;
	}
	public void setR222GUARANTEE_ELIGIBLE(BigDecimal r222guarantee_ELIGIBLE) {
		R222GUARANTEE_ELIGIBLE = r222guarantee_ELIGIBLE;
	}
	public void setR222CREDIT_DERIVATIVES(BigDecimal r222credit_DERIVATIVES) {
		R222CREDIT_DERIVATIVES = r222credit_DERIVATIVES;
	}
	public void setR222EXPOSURE_COVERED_CRM(BigDecimal r222exposure_COVERED_CRM) {
		R222EXPOSURE_COVERED_CRM = r222exposure_COVERED_CRM;
	}
	public void setR222EXPOSURE_NOT_COVERED_CRM(BigDecimal r222exposure_NOT_COVERED_CRM) {
		R222EXPOSURE_NOT_COVERED_CRM = r222exposure_NOT_COVERED_CRM;
	}
	public void setR222RWA_TOTAL(BigDecimal r222rwa_TOTAL) {
		R222RWA_TOTAL = r222rwa_TOTAL;
	}
	public void setR222CRM_RISK_WEIGHT(BigDecimal r222crm_RISK_WEIGHT) {
		R222CRM_RISK_WEIGHT = r222crm_RISK_WEIGHT;
	}
	public void setR222RWA_CRM_COVERED(BigDecimal r222rwa_CRM_COVERED) {
		R222RWA_CRM_COVERED = r222rwa_CRM_COVERED;
	}
	public void setR222ORIGINAL_CP_RISK_WEIGHT(BigDecimal r222original_CP_RISK_WEIGHT) {
		R222ORIGINAL_CP_RISK_WEIGHT = r222original_CP_RISK_WEIGHT;
	}
	public void setR222RWA_NOT_COVERED(BigDecimal r222rwa_NOT_COVERED) {
		R222RWA_NOT_COVERED = r222rwa_NOT_COVERED;
	}
	public void setR222COLLATERAL_CEA_ELIGIBLE(BigDecimal r222collateral_CEA_ELIGIBLE) {
		R222COLLATERAL_CEA_ELIGIBLE = r222collateral_CEA_ELIGIBLE;
	}
	public void setR222CEA_AFTER_VOL_ADJ(BigDecimal r222cea_AFTER_VOL_ADJ) {
		R222CEA_AFTER_VOL_ADJ = r222cea_AFTER_VOL_ADJ;
	}
	public void setR222COLL_CASH(BigDecimal r222coll_CASH) {
		R222COLL_CASH = r222coll_CASH;
	}
	public void setR222COLL_TBILLS(BigDecimal r222coll_TBILLS) {
		R222COLL_TBILLS = r222coll_TBILLS;
	}
	public void setR222COLL_DEBT_SEC(BigDecimal r222coll_DEBT_SEC) {
		R222COLL_DEBT_SEC = r222coll_DEBT_SEC;
	}
	public void setR222COLL_EQUITIES(BigDecimal r222coll_EQUITIES) {
		R222COLL_EQUITIES = r222coll_EQUITIES;
	}
	public void setR222COLL_MUTUAL_FUNDS(BigDecimal r222coll_MUTUAL_FUNDS) {
		R222COLL_MUTUAL_FUNDS = r222coll_MUTUAL_FUNDS;
	}
	public void setR222COLL_TOTAL(BigDecimal r222coll_TOTAL) {
		R222COLL_TOTAL = r222coll_TOTAL;
	}
	public void setR222CEA_AFTER_CRM(BigDecimal r222cea_AFTER_CRM) {
		R222CEA_AFTER_CRM = r222cea_AFTER_CRM;
	}
	public void setR222RWA_CEA_NOT_COVERED(BigDecimal r222rwa_CEA_NOT_COVERED) {
		R222RWA_CEA_NOT_COVERED = r222rwa_CEA_NOT_COVERED;
	}
	public void setR222UNSECURED_CEA(BigDecimal r222unsecured_CEA) {
		R222UNSECURED_CEA = r222unsecured_CEA;
	}
	public void setR222RWA_UNSECURED_CEA(BigDecimal r222rwa_UNSECURED_CEA) {
		R222RWA_UNSECURED_CEA = r222rwa_UNSECURED_CEA;
	}
	public void setR223NOMINAL_PRINCIPAL_AMT(BigDecimal r223nominal_PRINCIPAL_AMT) {
		R223NOMINAL_PRINCIPAL_AMT = r223nominal_PRINCIPAL_AMT;
	}
	public void setR223CCF_PCT(BigDecimal r223ccf_PCT) {
		R223CCF_PCT = r223ccf_PCT;
	}
	public void setR223CREDIT_EQUIVALENT_AMT(BigDecimal r223credit_EQUIVALENT_AMT) {
		R223CREDIT_EQUIVALENT_AMT = r223credit_EQUIVALENT_AMT;
	}
	public void setR223CEA_ELIGIBLE_NETTING_CP(BigDecimal r223cea_ELIGIBLE_NETTING_CP) {
		R223CEA_ELIGIBLE_NETTING_CP = r223cea_ELIGIBLE_NETTING_CP;
	}
	public void setR223CEA_AFTER_NETTING(BigDecimal r223cea_AFTER_NETTING) {
		R223CEA_AFTER_NETTING = r223cea_AFTER_NETTING;
	}
	public void setR223CEA_ELIGIBLE_CRM_SUB(BigDecimal r223cea_ELIGIBLE_CRM_SUB) {
		R223CEA_ELIGIBLE_CRM_SUB = r223cea_ELIGIBLE_CRM_SUB;
	}
	public void setR223GUARANTEE_ELIGIBLE(BigDecimal r223guarantee_ELIGIBLE) {
		R223GUARANTEE_ELIGIBLE = r223guarantee_ELIGIBLE;
	}
	public void setR223CREDIT_DERIVATIVES(BigDecimal r223credit_DERIVATIVES) {
		R223CREDIT_DERIVATIVES = r223credit_DERIVATIVES;
	}
	public void setR223EXPOSURE_COVERED_CRM(BigDecimal r223exposure_COVERED_CRM) {
		R223EXPOSURE_COVERED_CRM = r223exposure_COVERED_CRM;
	}
	public void setR223EXPOSURE_NOT_COVERED_CRM(BigDecimal r223exposure_NOT_COVERED_CRM) {
		R223EXPOSURE_NOT_COVERED_CRM = r223exposure_NOT_COVERED_CRM;
	}
	public void setR223RWA_TOTAL(BigDecimal r223rwa_TOTAL) {
		R223RWA_TOTAL = r223rwa_TOTAL;
	}
	public void setR223CRM_RISK_WEIGHT(BigDecimal r223crm_RISK_WEIGHT) {
		R223CRM_RISK_WEIGHT = r223crm_RISK_WEIGHT;
	}
	public void setR223RWA_CRM_COVERED(BigDecimal r223rwa_CRM_COVERED) {
		R223RWA_CRM_COVERED = r223rwa_CRM_COVERED;
	}
	public void setR223ORIGINAL_CP_RISK_WEIGHT(BigDecimal r223original_CP_RISK_WEIGHT) {
		R223ORIGINAL_CP_RISK_WEIGHT = r223original_CP_RISK_WEIGHT;
	}
	public void setR223RWA_NOT_COVERED(BigDecimal r223rwa_NOT_COVERED) {
		R223RWA_NOT_COVERED = r223rwa_NOT_COVERED;
	}
	public void setR223COLLATERAL_CEA_ELIGIBLE(BigDecimal r223collateral_CEA_ELIGIBLE) {
		R223COLLATERAL_CEA_ELIGIBLE = r223collateral_CEA_ELIGIBLE;
	}
	public void setR223CEA_AFTER_VOL_ADJ(BigDecimal r223cea_AFTER_VOL_ADJ) {
		R223CEA_AFTER_VOL_ADJ = r223cea_AFTER_VOL_ADJ;
	}
	public void setR223COLL_CASH(BigDecimal r223coll_CASH) {
		R223COLL_CASH = r223coll_CASH;
	}
	public void setR223COLL_TBILLS(BigDecimal r223coll_TBILLS) {
		R223COLL_TBILLS = r223coll_TBILLS;
	}
	public void setR223COLL_DEBT_SEC(BigDecimal r223coll_DEBT_SEC) {
		R223COLL_DEBT_SEC = r223coll_DEBT_SEC;
	}
	public void setR223COLL_EQUITIES(BigDecimal r223coll_EQUITIES) {
		R223COLL_EQUITIES = r223coll_EQUITIES;
	}
	public void setR223COLL_MUTUAL_FUNDS(BigDecimal r223coll_MUTUAL_FUNDS) {
		R223COLL_MUTUAL_FUNDS = r223coll_MUTUAL_FUNDS;
	}
	public void setR223COLL_TOTAL(BigDecimal r223coll_TOTAL) {
		R223COLL_TOTAL = r223coll_TOTAL;
	}
	public void setR223CEA_AFTER_CRM(BigDecimal r223cea_AFTER_CRM) {
		R223CEA_AFTER_CRM = r223cea_AFTER_CRM;
	}
	public void setR223RWA_CEA_NOT_COVERED(BigDecimal r223rwa_CEA_NOT_COVERED) {
		R223RWA_CEA_NOT_COVERED = r223rwa_CEA_NOT_COVERED;
	}
	public void setR223UNSECURED_CEA(BigDecimal r223unsecured_CEA) {
		R223UNSECURED_CEA = r223unsecured_CEA;
	}
	public void setR223RWA_UNSECURED_CEA(BigDecimal r223rwa_UNSECURED_CEA) {
		R223RWA_UNSECURED_CEA = r223rwa_UNSECURED_CEA;
	}
	public void setR224NOMINAL_PRINCIPAL_AMT(BigDecimal r224nominal_PRINCIPAL_AMT) {
		R224NOMINAL_PRINCIPAL_AMT = r224nominal_PRINCIPAL_AMT;
	}
	public void setR224CCF_PCT(BigDecimal r224ccf_PCT) {
		R224CCF_PCT = r224ccf_PCT;
	}
	public void setR224CREDIT_EQUIVALENT_AMT(BigDecimal r224credit_EQUIVALENT_AMT) {
		R224CREDIT_EQUIVALENT_AMT = r224credit_EQUIVALENT_AMT;
	}
	public void setR224CEA_ELIGIBLE_NETTING_CP(BigDecimal r224cea_ELIGIBLE_NETTING_CP) {
		R224CEA_ELIGIBLE_NETTING_CP = r224cea_ELIGIBLE_NETTING_CP;
	}
	public void setR224CEA_AFTER_NETTING(BigDecimal r224cea_AFTER_NETTING) {
		R224CEA_AFTER_NETTING = r224cea_AFTER_NETTING;
	}
	public void setR224CEA_ELIGIBLE_CRM_SUB(BigDecimal r224cea_ELIGIBLE_CRM_SUB) {
		R224CEA_ELIGIBLE_CRM_SUB = r224cea_ELIGIBLE_CRM_SUB;
	}
	public void setR224GUARANTEE_ELIGIBLE(BigDecimal r224guarantee_ELIGIBLE) {
		R224GUARANTEE_ELIGIBLE = r224guarantee_ELIGIBLE;
	}
	public void setR224CREDIT_DERIVATIVES(BigDecimal r224credit_DERIVATIVES) {
		R224CREDIT_DERIVATIVES = r224credit_DERIVATIVES;
	}
	public void setR224EXPOSURE_COVERED_CRM(BigDecimal r224exposure_COVERED_CRM) {
		R224EXPOSURE_COVERED_CRM = r224exposure_COVERED_CRM;
	}
	public void setR224EXPOSURE_NOT_COVERED_CRM(BigDecimal r224exposure_NOT_COVERED_CRM) {
		R224EXPOSURE_NOT_COVERED_CRM = r224exposure_NOT_COVERED_CRM;
	}
	public void setR224RWA_TOTAL(BigDecimal r224rwa_TOTAL) {
		R224RWA_TOTAL = r224rwa_TOTAL;
	}
	public void setR224CRM_RISK_WEIGHT(BigDecimal r224crm_RISK_WEIGHT) {
		R224CRM_RISK_WEIGHT = r224crm_RISK_WEIGHT;
	}
	public void setR224RWA_CRM_COVERED(BigDecimal r224rwa_CRM_COVERED) {
		R224RWA_CRM_COVERED = r224rwa_CRM_COVERED;
	}
	public void setR224ORIGINAL_CP_RISK_WEIGHT(BigDecimal r224original_CP_RISK_WEIGHT) {
		R224ORIGINAL_CP_RISK_WEIGHT = r224original_CP_RISK_WEIGHT;
	}
	public void setR224RWA_NOT_COVERED(BigDecimal r224rwa_NOT_COVERED) {
		R224RWA_NOT_COVERED = r224rwa_NOT_COVERED;
	}
	public void setR224COLLATERAL_CEA_ELIGIBLE(BigDecimal r224collateral_CEA_ELIGIBLE) {
		R224COLLATERAL_CEA_ELIGIBLE = r224collateral_CEA_ELIGIBLE;
	}
	public void setR224CEA_AFTER_VOL_ADJ(BigDecimal r224cea_AFTER_VOL_ADJ) {
		R224CEA_AFTER_VOL_ADJ = r224cea_AFTER_VOL_ADJ;
	}
	public void setR224COLL_CASH(BigDecimal r224coll_CASH) {
		R224COLL_CASH = r224coll_CASH;
	}
	public void setR224COLL_TBILLS(BigDecimal r224coll_TBILLS) {
		R224COLL_TBILLS = r224coll_TBILLS;
	}
	public void setR224COLL_DEBT_SEC(BigDecimal r224coll_DEBT_SEC) {
		R224COLL_DEBT_SEC = r224coll_DEBT_SEC;
	}
	public void setR224COLL_EQUITIES(BigDecimal r224coll_EQUITIES) {
		R224COLL_EQUITIES = r224coll_EQUITIES;
	}
	public void setR224COLL_MUTUAL_FUNDS(BigDecimal r224coll_MUTUAL_FUNDS) {
		R224COLL_MUTUAL_FUNDS = r224coll_MUTUAL_FUNDS;
	}
	public void setR224COLL_TOTAL(BigDecimal r224coll_TOTAL) {
		R224COLL_TOTAL = r224coll_TOTAL;
	}
	public void setR224CEA_AFTER_CRM(BigDecimal r224cea_AFTER_CRM) {
		R224CEA_AFTER_CRM = r224cea_AFTER_CRM;
	}
	public void setR224RWA_CEA_NOT_COVERED(BigDecimal r224rwa_CEA_NOT_COVERED) {
		R224RWA_CEA_NOT_COVERED = r224rwa_CEA_NOT_COVERED;
	}
	public void setR224UNSECURED_CEA(BigDecimal r224unsecured_CEA) {
		R224UNSECURED_CEA = r224unsecured_CEA;
	}
	public void setR224RWA_UNSECURED_CEA(BigDecimal r224rwa_UNSECURED_CEA) {
		R224RWA_UNSECURED_CEA = r224rwa_UNSECURED_CEA;
	}
	public void setR225NOMINAL_PRINCIPAL_AMT(BigDecimal r225nominal_PRINCIPAL_AMT) {
		R225NOMINAL_PRINCIPAL_AMT = r225nominal_PRINCIPAL_AMT;
	}
	public void setR225CCF_PCT(BigDecimal r225ccf_PCT) {
		R225CCF_PCT = r225ccf_PCT;
	}
	public void setR225CREDIT_EQUIVALENT_AMT(BigDecimal r225credit_EQUIVALENT_AMT) {
		R225CREDIT_EQUIVALENT_AMT = r225credit_EQUIVALENT_AMT;
	}
	public void setR225CEA_ELIGIBLE_NETTING_CP(BigDecimal r225cea_ELIGIBLE_NETTING_CP) {
		R225CEA_ELIGIBLE_NETTING_CP = r225cea_ELIGIBLE_NETTING_CP;
	}
	public void setR225CEA_AFTER_NETTING(BigDecimal r225cea_AFTER_NETTING) {
		R225CEA_AFTER_NETTING = r225cea_AFTER_NETTING;
	}
	public void setR225CEA_ELIGIBLE_CRM_SUB(BigDecimal r225cea_ELIGIBLE_CRM_SUB) {
		R225CEA_ELIGIBLE_CRM_SUB = r225cea_ELIGIBLE_CRM_SUB;
	}
	public void setR225GUARANTEE_ELIGIBLE(BigDecimal r225guarantee_ELIGIBLE) {
		R225GUARANTEE_ELIGIBLE = r225guarantee_ELIGIBLE;
	}
	public void setR225CREDIT_DERIVATIVES(BigDecimal r225credit_DERIVATIVES) {
		R225CREDIT_DERIVATIVES = r225credit_DERIVATIVES;
	}
	public void setR225EXPOSURE_COVERED_CRM(BigDecimal r225exposure_COVERED_CRM) {
		R225EXPOSURE_COVERED_CRM = r225exposure_COVERED_CRM;
	}
	public void setR225EXPOSURE_NOT_COVERED_CRM(BigDecimal r225exposure_NOT_COVERED_CRM) {
		R225EXPOSURE_NOT_COVERED_CRM = r225exposure_NOT_COVERED_CRM;
	}
	public void setR225RWA_TOTAL(BigDecimal r225rwa_TOTAL) {
		R225RWA_TOTAL = r225rwa_TOTAL;
	}
	public void setR225CRM_RISK_WEIGHT(BigDecimal r225crm_RISK_WEIGHT) {
		R225CRM_RISK_WEIGHT = r225crm_RISK_WEIGHT;
	}
	public void setR225RWA_CRM_COVERED(BigDecimal r225rwa_CRM_COVERED) {
		R225RWA_CRM_COVERED = r225rwa_CRM_COVERED;
	}
	public void setR225ORIGINAL_CP_RISK_WEIGHT(BigDecimal r225original_CP_RISK_WEIGHT) {
		R225ORIGINAL_CP_RISK_WEIGHT = r225original_CP_RISK_WEIGHT;
	}
	public void setR225RWA_NOT_COVERED(BigDecimal r225rwa_NOT_COVERED) {
		R225RWA_NOT_COVERED = r225rwa_NOT_COVERED;
	}
	public void setR225COLLATERAL_CEA_ELIGIBLE(BigDecimal r225collateral_CEA_ELIGIBLE) {
		R225COLLATERAL_CEA_ELIGIBLE = r225collateral_CEA_ELIGIBLE;
	}
	public void setR225CEA_AFTER_VOL_ADJ(BigDecimal r225cea_AFTER_VOL_ADJ) {
		R225CEA_AFTER_VOL_ADJ = r225cea_AFTER_VOL_ADJ;
	}
	public void setR225COLL_CASH(BigDecimal r225coll_CASH) {
		R225COLL_CASH = r225coll_CASH;
	}
	public void setR225COLL_TBILLS(BigDecimal r225coll_TBILLS) {
		R225COLL_TBILLS = r225coll_TBILLS;
	}
	public void setR225COLL_DEBT_SEC(BigDecimal r225coll_DEBT_SEC) {
		R225COLL_DEBT_SEC = r225coll_DEBT_SEC;
	}
	public void setR225COLL_EQUITIES(BigDecimal r225coll_EQUITIES) {
		R225COLL_EQUITIES = r225coll_EQUITIES;
	}
	public void setR225COLL_MUTUAL_FUNDS(BigDecimal r225coll_MUTUAL_FUNDS) {
		R225COLL_MUTUAL_FUNDS = r225coll_MUTUAL_FUNDS;
	}
	public void setR225COLL_TOTAL(BigDecimal r225coll_TOTAL) {
		R225COLL_TOTAL = r225coll_TOTAL;
	}
	public void setR225CEA_AFTER_CRM(BigDecimal r225cea_AFTER_CRM) {
		R225CEA_AFTER_CRM = r225cea_AFTER_CRM;
	}
	public void setR225RWA_CEA_NOT_COVERED(BigDecimal r225rwa_CEA_NOT_COVERED) {
		R225RWA_CEA_NOT_COVERED = r225rwa_CEA_NOT_COVERED;
	}
	public void setR225UNSECURED_CEA(BigDecimal r225unsecured_CEA) {
		R225UNSECURED_CEA = r225unsecured_CEA;
	}
	public void setR225RWA_UNSECURED_CEA(BigDecimal r225rwa_UNSECURED_CEA) {
		R225RWA_UNSECURED_CEA = r225rwa_UNSECURED_CEA;
	}
	public void setR226NOMINAL_PRINCIPAL_AMT(BigDecimal r226nominal_PRINCIPAL_AMT) {
		R226NOMINAL_PRINCIPAL_AMT = r226nominal_PRINCIPAL_AMT;
	}
	public void setR226CCF_PCT(BigDecimal r226ccf_PCT) {
		R226CCF_PCT = r226ccf_PCT;
	}
	public void setR226CREDIT_EQUIVALENT_AMT(BigDecimal r226credit_EQUIVALENT_AMT) {
		R226CREDIT_EQUIVALENT_AMT = r226credit_EQUIVALENT_AMT;
	}
	public void setR226CEA_ELIGIBLE_NETTING_CP(BigDecimal r226cea_ELIGIBLE_NETTING_CP) {
		R226CEA_ELIGIBLE_NETTING_CP = r226cea_ELIGIBLE_NETTING_CP;
	}
	public void setR226CEA_AFTER_NETTING(BigDecimal r226cea_AFTER_NETTING) {
		R226CEA_AFTER_NETTING = r226cea_AFTER_NETTING;
	}
	public void setR226CEA_ELIGIBLE_CRM_SUB(BigDecimal r226cea_ELIGIBLE_CRM_SUB) {
		R226CEA_ELIGIBLE_CRM_SUB = r226cea_ELIGIBLE_CRM_SUB;
	}
	public void setR226GUARANTEE_ELIGIBLE(BigDecimal r226guarantee_ELIGIBLE) {
		R226GUARANTEE_ELIGIBLE = r226guarantee_ELIGIBLE;
	}
	public void setR226CREDIT_DERIVATIVES(BigDecimal r226credit_DERIVATIVES) {
		R226CREDIT_DERIVATIVES = r226credit_DERIVATIVES;
	}
	public void setR226EXPOSURE_COVERED_CRM(BigDecimal r226exposure_COVERED_CRM) {
		R226EXPOSURE_COVERED_CRM = r226exposure_COVERED_CRM;
	}
	public void setR226EXPOSURE_NOT_COVERED_CRM(BigDecimal r226exposure_NOT_COVERED_CRM) {
		R226EXPOSURE_NOT_COVERED_CRM = r226exposure_NOT_COVERED_CRM;
	}
	public void setR226RWA_TOTAL(BigDecimal r226rwa_TOTAL) {
		R226RWA_TOTAL = r226rwa_TOTAL;
	}
	public void setR226CRM_RISK_WEIGHT(BigDecimal r226crm_RISK_WEIGHT) {
		R226CRM_RISK_WEIGHT = r226crm_RISK_WEIGHT;
	}
	public void setR226RWA_CRM_COVERED(BigDecimal r226rwa_CRM_COVERED) {
		R226RWA_CRM_COVERED = r226rwa_CRM_COVERED;
	}
	public void setR226ORIGINAL_CP_RISK_WEIGHT(BigDecimal r226original_CP_RISK_WEIGHT) {
		R226ORIGINAL_CP_RISK_WEIGHT = r226original_CP_RISK_WEIGHT;
	}
	public void setR226RWA_NOT_COVERED(BigDecimal r226rwa_NOT_COVERED) {
		R226RWA_NOT_COVERED = r226rwa_NOT_COVERED;
	}
	public void setR226COLLATERAL_CEA_ELIGIBLE(BigDecimal r226collateral_CEA_ELIGIBLE) {
		R226COLLATERAL_CEA_ELIGIBLE = r226collateral_CEA_ELIGIBLE;
	}
	public void setR226CEA_AFTER_VOL_ADJ(BigDecimal r226cea_AFTER_VOL_ADJ) {
		R226CEA_AFTER_VOL_ADJ = r226cea_AFTER_VOL_ADJ;
	}
	public void setR226COLL_CASH(BigDecimal r226coll_CASH) {
		R226COLL_CASH = r226coll_CASH;
	}
	public void setR226COLL_TBILLS(BigDecimal r226coll_TBILLS) {
		R226COLL_TBILLS = r226coll_TBILLS;
	}
	public void setR226COLL_DEBT_SEC(BigDecimal r226coll_DEBT_SEC) {
		R226COLL_DEBT_SEC = r226coll_DEBT_SEC;
	}
	public void setR226COLL_EQUITIES(BigDecimal r226coll_EQUITIES) {
		R226COLL_EQUITIES = r226coll_EQUITIES;
	}
	public void setR226COLL_MUTUAL_FUNDS(BigDecimal r226coll_MUTUAL_FUNDS) {
		R226COLL_MUTUAL_FUNDS = r226coll_MUTUAL_FUNDS;
	}
	public void setR226COLL_TOTAL(BigDecimal r226coll_TOTAL) {
		R226COLL_TOTAL = r226coll_TOTAL;
	}
	public void setR226CEA_AFTER_CRM(BigDecimal r226cea_AFTER_CRM) {
		R226CEA_AFTER_CRM = r226cea_AFTER_CRM;
	}
	public void setR226RWA_CEA_NOT_COVERED(BigDecimal r226rwa_CEA_NOT_COVERED) {
		R226RWA_CEA_NOT_COVERED = r226rwa_CEA_NOT_COVERED;
	}
	public void setR226UNSECURED_CEA(BigDecimal r226unsecured_CEA) {
		R226UNSECURED_CEA = r226unsecured_CEA;
	}
	public void setR226RWA_UNSECURED_CEA(BigDecimal r226rwa_UNSECURED_CEA) {
		R226RWA_UNSECURED_CEA = r226rwa_UNSECURED_CEA;
	}
	public void setR227NOMINAL_PRINCIPAL_AMT(BigDecimal r227nominal_PRINCIPAL_AMT) {
		R227NOMINAL_PRINCIPAL_AMT = r227nominal_PRINCIPAL_AMT;
	}
	public void setR227CCF_PCT(BigDecimal r227ccf_PCT) {
		R227CCF_PCT = r227ccf_PCT;
	}
	public void setR227CREDIT_EQUIVALENT_AMT(BigDecimal r227credit_EQUIVALENT_AMT) {
		R227CREDIT_EQUIVALENT_AMT = r227credit_EQUIVALENT_AMT;
	}
	public void setR227CEA_ELIGIBLE_NETTING_CP(BigDecimal r227cea_ELIGIBLE_NETTING_CP) {
		R227CEA_ELIGIBLE_NETTING_CP = r227cea_ELIGIBLE_NETTING_CP;
	}
	public void setR227CEA_AFTER_NETTING(BigDecimal r227cea_AFTER_NETTING) {
		R227CEA_AFTER_NETTING = r227cea_AFTER_NETTING;
	}
	public void setR227CEA_ELIGIBLE_CRM_SUB(BigDecimal r227cea_ELIGIBLE_CRM_SUB) {
		R227CEA_ELIGIBLE_CRM_SUB = r227cea_ELIGIBLE_CRM_SUB;
	}
	public void setR227GUARANTEE_ELIGIBLE(BigDecimal r227guarantee_ELIGIBLE) {
		R227GUARANTEE_ELIGIBLE = r227guarantee_ELIGIBLE;
	}
	public void setR227CREDIT_DERIVATIVES(BigDecimal r227credit_DERIVATIVES) {
		R227CREDIT_DERIVATIVES = r227credit_DERIVATIVES;
	}
	public void setR227EXPOSURE_COVERED_CRM(BigDecimal r227exposure_COVERED_CRM) {
		R227EXPOSURE_COVERED_CRM = r227exposure_COVERED_CRM;
	}
	public void setR227EXPOSURE_NOT_COVERED_CRM(BigDecimal r227exposure_NOT_COVERED_CRM) {
		R227EXPOSURE_NOT_COVERED_CRM = r227exposure_NOT_COVERED_CRM;
	}
	public void setR227RWA_TOTAL(BigDecimal r227rwa_TOTAL) {
		R227RWA_TOTAL = r227rwa_TOTAL;
	}
	public void setR227CRM_RISK_WEIGHT(BigDecimal r227crm_RISK_WEIGHT) {
		R227CRM_RISK_WEIGHT = r227crm_RISK_WEIGHT;
	}
	public void setR227RWA_CRM_COVERED(BigDecimal r227rwa_CRM_COVERED) {
		R227RWA_CRM_COVERED = r227rwa_CRM_COVERED;
	}
	public void setR227ORIGINAL_CP_RISK_WEIGHT(BigDecimal r227original_CP_RISK_WEIGHT) {
		R227ORIGINAL_CP_RISK_WEIGHT = r227original_CP_RISK_WEIGHT;
	}
	public void setR227RWA_NOT_COVERED(BigDecimal r227rwa_NOT_COVERED) {
		R227RWA_NOT_COVERED = r227rwa_NOT_COVERED;
	}
	public void setR227COLLATERAL_CEA_ELIGIBLE(BigDecimal r227collateral_CEA_ELIGIBLE) {
		R227COLLATERAL_CEA_ELIGIBLE = r227collateral_CEA_ELIGIBLE;
	}
	public void setR227CEA_AFTER_VOL_ADJ(BigDecimal r227cea_AFTER_VOL_ADJ) {
		R227CEA_AFTER_VOL_ADJ = r227cea_AFTER_VOL_ADJ;
	}
	public void setR227COLL_CASH(BigDecimal r227coll_CASH) {
		R227COLL_CASH = r227coll_CASH;
	}
	public void setR227COLL_TBILLS(BigDecimal r227coll_TBILLS) {
		R227COLL_TBILLS = r227coll_TBILLS;
	}
	public void setR227COLL_DEBT_SEC(BigDecimal r227coll_DEBT_SEC) {
		R227COLL_DEBT_SEC = r227coll_DEBT_SEC;
	}
	public void setR227COLL_EQUITIES(BigDecimal r227coll_EQUITIES) {
		R227COLL_EQUITIES = r227coll_EQUITIES;
	}
	public void setR227COLL_MUTUAL_FUNDS(BigDecimal r227coll_MUTUAL_FUNDS) {
		R227COLL_MUTUAL_FUNDS = r227coll_MUTUAL_FUNDS;
	}
	public void setR227COLL_TOTAL(BigDecimal r227coll_TOTAL) {
		R227COLL_TOTAL = r227coll_TOTAL;
	}
	public void setR227CEA_AFTER_CRM(BigDecimal r227cea_AFTER_CRM) {
		R227CEA_AFTER_CRM = r227cea_AFTER_CRM;
	}
	public void setR227RWA_CEA_NOT_COVERED(BigDecimal r227rwa_CEA_NOT_COVERED) {
		R227RWA_CEA_NOT_COVERED = r227rwa_CEA_NOT_COVERED;
	}
	public void setR227UNSECURED_CEA(BigDecimal r227unsecured_CEA) {
		R227UNSECURED_CEA = r227unsecured_CEA;
	}
	public void setR227RWA_UNSECURED_CEA(BigDecimal r227rwa_UNSECURED_CEA) {
		R227RWA_UNSECURED_CEA = r227rwa_UNSECURED_CEA;
	}
	public void setR228NOMINAL_PRINCIPAL_AMT(BigDecimal r228nominal_PRINCIPAL_AMT) {
		R228NOMINAL_PRINCIPAL_AMT = r228nominal_PRINCIPAL_AMT;
	}
	public void setR228CCF_PCT(BigDecimal r228ccf_PCT) {
		R228CCF_PCT = r228ccf_PCT;
	}
	public void setR228CREDIT_EQUIVALENT_AMT(BigDecimal r228credit_EQUIVALENT_AMT) {
		R228CREDIT_EQUIVALENT_AMT = r228credit_EQUIVALENT_AMT;
	}
	public void setR228CEA_ELIGIBLE_NETTING_CP(BigDecimal r228cea_ELIGIBLE_NETTING_CP) {
		R228CEA_ELIGIBLE_NETTING_CP = r228cea_ELIGIBLE_NETTING_CP;
	}
	public void setR228CEA_AFTER_NETTING(BigDecimal r228cea_AFTER_NETTING) {
		R228CEA_AFTER_NETTING = r228cea_AFTER_NETTING;
	}
	public void setR228CEA_ELIGIBLE_CRM_SUB(BigDecimal r228cea_ELIGIBLE_CRM_SUB) {
		R228CEA_ELIGIBLE_CRM_SUB = r228cea_ELIGIBLE_CRM_SUB;
	}
	public void setR228GUARANTEE_ELIGIBLE(BigDecimal r228guarantee_ELIGIBLE) {
		R228GUARANTEE_ELIGIBLE = r228guarantee_ELIGIBLE;
	}
	public void setR228CREDIT_DERIVATIVES(BigDecimal r228credit_DERIVATIVES) {
		R228CREDIT_DERIVATIVES = r228credit_DERIVATIVES;
	}
	public void setR228EXPOSURE_COVERED_CRM(BigDecimal r228exposure_COVERED_CRM) {
		R228EXPOSURE_COVERED_CRM = r228exposure_COVERED_CRM;
	}
	public void setR228EXPOSURE_NOT_COVERED_CRM(BigDecimal r228exposure_NOT_COVERED_CRM) {
		R228EXPOSURE_NOT_COVERED_CRM = r228exposure_NOT_COVERED_CRM;
	}
	public void setR228RWA_TOTAL(BigDecimal r228rwa_TOTAL) {
		R228RWA_TOTAL = r228rwa_TOTAL;
	}
	public void setR228CRM_RISK_WEIGHT(BigDecimal r228crm_RISK_WEIGHT) {
		R228CRM_RISK_WEIGHT = r228crm_RISK_WEIGHT;
	}
	public void setR228RWA_CRM_COVERED(BigDecimal r228rwa_CRM_COVERED) {
		R228RWA_CRM_COVERED = r228rwa_CRM_COVERED;
	}
	public void setR228ORIGINAL_CP_RISK_WEIGHT(BigDecimal r228original_CP_RISK_WEIGHT) {
		R228ORIGINAL_CP_RISK_WEIGHT = r228original_CP_RISK_WEIGHT;
	}
	public void setR228RWA_NOT_COVERED(BigDecimal r228rwa_NOT_COVERED) {
		R228RWA_NOT_COVERED = r228rwa_NOT_COVERED;
	}
	public void setR228COLLATERAL_CEA_ELIGIBLE(BigDecimal r228collateral_CEA_ELIGIBLE) {
		R228COLLATERAL_CEA_ELIGIBLE = r228collateral_CEA_ELIGIBLE;
	}
	public void setR228CEA_AFTER_VOL_ADJ(BigDecimal r228cea_AFTER_VOL_ADJ) {
		R228CEA_AFTER_VOL_ADJ = r228cea_AFTER_VOL_ADJ;
	}
	public void setR228COLL_CASH(BigDecimal r228coll_CASH) {
		R228COLL_CASH = r228coll_CASH;
	}
	public void setR228COLL_TBILLS(BigDecimal r228coll_TBILLS) {
		R228COLL_TBILLS = r228coll_TBILLS;
	}
	public void setR228COLL_DEBT_SEC(BigDecimal r228coll_DEBT_SEC) {
		R228COLL_DEBT_SEC = r228coll_DEBT_SEC;
	}
	public void setR228COLL_EQUITIES(BigDecimal r228coll_EQUITIES) {
		R228COLL_EQUITIES = r228coll_EQUITIES;
	}
	public void setR228COLL_MUTUAL_FUNDS(BigDecimal r228coll_MUTUAL_FUNDS) {
		R228COLL_MUTUAL_FUNDS = r228coll_MUTUAL_FUNDS;
	}
	public void setR228COLL_TOTAL(BigDecimal r228coll_TOTAL) {
		R228COLL_TOTAL = r228coll_TOTAL;
	}
	public void setR228CEA_AFTER_CRM(BigDecimal r228cea_AFTER_CRM) {
		R228CEA_AFTER_CRM = r228cea_AFTER_CRM;
	}
	public void setR228RWA_CEA_NOT_COVERED(BigDecimal r228rwa_CEA_NOT_COVERED) {
		R228RWA_CEA_NOT_COVERED = r228rwa_CEA_NOT_COVERED;
	}
	public void setR228UNSECURED_CEA(BigDecimal r228unsecured_CEA) {
		R228UNSECURED_CEA = r228unsecured_CEA;
	}
	public void setR228RWA_UNSECURED_CEA(BigDecimal r228rwa_UNSECURED_CEA) {
		R228RWA_UNSECURED_CEA = r228rwa_UNSECURED_CEA;
	}
	public void setR229NOMINAL_PRINCIPAL_AMT(BigDecimal r229nominal_PRINCIPAL_AMT) {
		R229NOMINAL_PRINCIPAL_AMT = r229nominal_PRINCIPAL_AMT;
	}
	public void setR229CCF_PCT(BigDecimal r229ccf_PCT) {
		R229CCF_PCT = r229ccf_PCT;
	}
	public void setR229CREDIT_EQUIVALENT_AMT(BigDecimal r229credit_EQUIVALENT_AMT) {
		R229CREDIT_EQUIVALENT_AMT = r229credit_EQUIVALENT_AMT;
	}
	public void setR229CEA_ELIGIBLE_NETTING_CP(BigDecimal r229cea_ELIGIBLE_NETTING_CP) {
		R229CEA_ELIGIBLE_NETTING_CP = r229cea_ELIGIBLE_NETTING_CP;
	}
	public void setR229CEA_AFTER_NETTING(BigDecimal r229cea_AFTER_NETTING) {
		R229CEA_AFTER_NETTING = r229cea_AFTER_NETTING;
	}
	public void setR229CEA_ELIGIBLE_CRM_SUB(BigDecimal r229cea_ELIGIBLE_CRM_SUB) {
		R229CEA_ELIGIBLE_CRM_SUB = r229cea_ELIGIBLE_CRM_SUB;
	}
	public void setR229GUARANTEE_ELIGIBLE(BigDecimal r229guarantee_ELIGIBLE) {
		R229GUARANTEE_ELIGIBLE = r229guarantee_ELIGIBLE;
	}
	public void setR229CREDIT_DERIVATIVES(BigDecimal r229credit_DERIVATIVES) {
		R229CREDIT_DERIVATIVES = r229credit_DERIVATIVES;
	}
	public void setR229EXPOSURE_COVERED_CRM(BigDecimal r229exposure_COVERED_CRM) {
		R229EXPOSURE_COVERED_CRM = r229exposure_COVERED_CRM;
	}
	public void setR229EXPOSURE_NOT_COVERED_CRM(BigDecimal r229exposure_NOT_COVERED_CRM) {
		R229EXPOSURE_NOT_COVERED_CRM = r229exposure_NOT_COVERED_CRM;
	}
	public void setR229RWA_TOTAL(BigDecimal r229rwa_TOTAL) {
		R229RWA_TOTAL = r229rwa_TOTAL;
	}
	public void setR229CRM_RISK_WEIGHT(BigDecimal r229crm_RISK_WEIGHT) {
		R229CRM_RISK_WEIGHT = r229crm_RISK_WEIGHT;
	}
	public void setR229RWA_CRM_COVERED(BigDecimal r229rwa_CRM_COVERED) {
		R229RWA_CRM_COVERED = r229rwa_CRM_COVERED;
	}
	public void setR229ORIGINAL_CP_RISK_WEIGHT(BigDecimal r229original_CP_RISK_WEIGHT) {
		R229ORIGINAL_CP_RISK_WEIGHT = r229original_CP_RISK_WEIGHT;
	}
	public void setR229RWA_NOT_COVERED(BigDecimal r229rwa_NOT_COVERED) {
		R229RWA_NOT_COVERED = r229rwa_NOT_COVERED;
	}
	public void setR229COLLATERAL_CEA_ELIGIBLE(BigDecimal r229collateral_CEA_ELIGIBLE) {
		R229COLLATERAL_CEA_ELIGIBLE = r229collateral_CEA_ELIGIBLE;
	}
	public void setR229CEA_AFTER_VOL_ADJ(BigDecimal r229cea_AFTER_VOL_ADJ) {
		R229CEA_AFTER_VOL_ADJ = r229cea_AFTER_VOL_ADJ;
	}
	public void setR229COLL_CASH(BigDecimal r229coll_CASH) {
		R229COLL_CASH = r229coll_CASH;
	}
	public void setR229COLL_TBILLS(BigDecimal r229coll_TBILLS) {
		R229COLL_TBILLS = r229coll_TBILLS;
	}
	public void setR229COLL_DEBT_SEC(BigDecimal r229coll_DEBT_SEC) {
		R229COLL_DEBT_SEC = r229coll_DEBT_SEC;
	}
	public void setR229COLL_EQUITIES(BigDecimal r229coll_EQUITIES) {
		R229COLL_EQUITIES = r229coll_EQUITIES;
	}
	public void setR229COLL_MUTUAL_FUNDS(BigDecimal r229coll_MUTUAL_FUNDS) {
		R229COLL_MUTUAL_FUNDS = r229coll_MUTUAL_FUNDS;
	}
	public void setR229COLL_TOTAL(BigDecimal r229coll_TOTAL) {
		R229COLL_TOTAL = r229coll_TOTAL;
	}
	public void setR229CEA_AFTER_CRM(BigDecimal r229cea_AFTER_CRM) {
		R229CEA_AFTER_CRM = r229cea_AFTER_CRM;
	}
	public void setR229RWA_CEA_NOT_COVERED(BigDecimal r229rwa_CEA_NOT_COVERED) {
		R229RWA_CEA_NOT_COVERED = r229rwa_CEA_NOT_COVERED;
	}
	public void setR229UNSECURED_CEA(BigDecimal r229unsecured_CEA) {
		R229UNSECURED_CEA = r229unsecured_CEA;
	}
	public void setR229RWA_UNSECURED_CEA(BigDecimal r229rwa_UNSECURED_CEA) {
		R229RWA_UNSECURED_CEA = r229rwa_UNSECURED_CEA;
	}
	public void setR230NOMINAL_PRINCIPAL_AMT(BigDecimal r230nominal_PRINCIPAL_AMT) {
		R230NOMINAL_PRINCIPAL_AMT = r230nominal_PRINCIPAL_AMT;
	}
	public void setR230CCF_PCT(BigDecimal r230ccf_PCT) {
		R230CCF_PCT = r230ccf_PCT;
	}
	public void setR230CREDIT_EQUIVALENT_AMT(BigDecimal r230credit_EQUIVALENT_AMT) {
		R230CREDIT_EQUIVALENT_AMT = r230credit_EQUIVALENT_AMT;
	}
	public void setR230CEA_ELIGIBLE_NETTING_CP(BigDecimal r230cea_ELIGIBLE_NETTING_CP) {
		R230CEA_ELIGIBLE_NETTING_CP = r230cea_ELIGIBLE_NETTING_CP;
	}
	public void setR230CEA_AFTER_NETTING(BigDecimal r230cea_AFTER_NETTING) {
		R230CEA_AFTER_NETTING = r230cea_AFTER_NETTING;
	}
	public void setR230CEA_ELIGIBLE_CRM_SUB(BigDecimal r230cea_ELIGIBLE_CRM_SUB) {
		R230CEA_ELIGIBLE_CRM_SUB = r230cea_ELIGIBLE_CRM_SUB;
	}
	public void setR230GUARANTEE_ELIGIBLE(BigDecimal r230guarantee_ELIGIBLE) {
		R230GUARANTEE_ELIGIBLE = r230guarantee_ELIGIBLE;
	}
	public void setR230CREDIT_DERIVATIVES(BigDecimal r230credit_DERIVATIVES) {
		R230CREDIT_DERIVATIVES = r230credit_DERIVATIVES;
	}
	public void setR230EXPOSURE_COVERED_CRM(BigDecimal r230exposure_COVERED_CRM) {
		R230EXPOSURE_COVERED_CRM = r230exposure_COVERED_CRM;
	}
	public void setR230EXPOSURE_NOT_COVERED_CRM(BigDecimal r230exposure_NOT_COVERED_CRM) {
		R230EXPOSURE_NOT_COVERED_CRM = r230exposure_NOT_COVERED_CRM;
	}
	public void setR230RWA_TOTAL(BigDecimal r230rwa_TOTAL) {
		R230RWA_TOTAL = r230rwa_TOTAL;
	}
	public void setR230CRM_RISK_WEIGHT(BigDecimal r230crm_RISK_WEIGHT) {
		R230CRM_RISK_WEIGHT = r230crm_RISK_WEIGHT;
	}
	public void setR230RWA_CRM_COVERED(BigDecimal r230rwa_CRM_COVERED) {
		R230RWA_CRM_COVERED = r230rwa_CRM_COVERED;
	}
	public void setR230ORIGINAL_CP_RISK_WEIGHT(BigDecimal r230original_CP_RISK_WEIGHT) {
		R230ORIGINAL_CP_RISK_WEIGHT = r230original_CP_RISK_WEIGHT;
	}
	public void setR230RWA_NOT_COVERED(BigDecimal r230rwa_NOT_COVERED) {
		R230RWA_NOT_COVERED = r230rwa_NOT_COVERED;
	}
	public void setR230COLLATERAL_CEA_ELIGIBLE(BigDecimal r230collateral_CEA_ELIGIBLE) {
		R230COLLATERAL_CEA_ELIGIBLE = r230collateral_CEA_ELIGIBLE;
	}
	public void setR230CEA_AFTER_VOL_ADJ(BigDecimal r230cea_AFTER_VOL_ADJ) {
		R230CEA_AFTER_VOL_ADJ = r230cea_AFTER_VOL_ADJ;
	}
	public void setR230COLL_CASH(BigDecimal r230coll_CASH) {
		R230COLL_CASH = r230coll_CASH;
	}
	public void setR230COLL_TBILLS(BigDecimal r230coll_TBILLS) {
		R230COLL_TBILLS = r230coll_TBILLS;
	}
	public void setR230COLL_DEBT_SEC(BigDecimal r230coll_DEBT_SEC) {
		R230COLL_DEBT_SEC = r230coll_DEBT_SEC;
	}
	public void setR230COLL_EQUITIES(BigDecimal r230coll_EQUITIES) {
		R230COLL_EQUITIES = r230coll_EQUITIES;
	}
	public void setR230COLL_MUTUAL_FUNDS(BigDecimal r230coll_MUTUAL_FUNDS) {
		R230COLL_MUTUAL_FUNDS = r230coll_MUTUAL_FUNDS;
	}
	public void setR230COLL_TOTAL(BigDecimal r230coll_TOTAL) {
		R230COLL_TOTAL = r230coll_TOTAL;
	}
	public void setR230CEA_AFTER_CRM(BigDecimal r230cea_AFTER_CRM) {
		R230CEA_AFTER_CRM = r230cea_AFTER_CRM;
	}
	public void setR230RWA_CEA_NOT_COVERED(BigDecimal r230rwa_CEA_NOT_COVERED) {
		R230RWA_CEA_NOT_COVERED = r230rwa_CEA_NOT_COVERED;
	}
	public void setR230UNSECURED_CEA(BigDecimal r230unsecured_CEA) {
		R230UNSECURED_CEA = r230unsecured_CEA;
	}
	public void setR230RWA_UNSECURED_CEA(BigDecimal r230rwa_UNSECURED_CEA) {
		R230RWA_UNSECURED_CEA = r230rwa_UNSECURED_CEA;
	}
	public void setR231NOMINAL_PRINCIPAL_AMT(BigDecimal r231nominal_PRINCIPAL_AMT) {
		R231NOMINAL_PRINCIPAL_AMT = r231nominal_PRINCIPAL_AMT;
	}
	public void setR231CCF_PCT(BigDecimal r231ccf_PCT) {
		R231CCF_PCT = r231ccf_PCT;
	}
	public void setR231CREDIT_EQUIVALENT_AMT(BigDecimal r231credit_EQUIVALENT_AMT) {
		R231CREDIT_EQUIVALENT_AMT = r231credit_EQUIVALENT_AMT;
	}
	public void setR231CEA_ELIGIBLE_NETTING_CP(BigDecimal r231cea_ELIGIBLE_NETTING_CP) {
		R231CEA_ELIGIBLE_NETTING_CP = r231cea_ELIGIBLE_NETTING_CP;
	}
	public void setR231CEA_AFTER_NETTING(BigDecimal r231cea_AFTER_NETTING) {
		R231CEA_AFTER_NETTING = r231cea_AFTER_NETTING;
	}
	public void setR231CEA_ELIGIBLE_CRM_SUB(BigDecimal r231cea_ELIGIBLE_CRM_SUB) {
		R231CEA_ELIGIBLE_CRM_SUB = r231cea_ELIGIBLE_CRM_SUB;
	}
	public void setR231GUARANTEE_ELIGIBLE(BigDecimal r231guarantee_ELIGIBLE) {
		R231GUARANTEE_ELIGIBLE = r231guarantee_ELIGIBLE;
	}
	public void setR231CREDIT_DERIVATIVES(BigDecimal r231credit_DERIVATIVES) {
		R231CREDIT_DERIVATIVES = r231credit_DERIVATIVES;
	}
	public void setR231EXPOSURE_COVERED_CRM(BigDecimal r231exposure_COVERED_CRM) {
		R231EXPOSURE_COVERED_CRM = r231exposure_COVERED_CRM;
	}
	public void setR231EXPOSURE_NOT_COVERED_CRM(BigDecimal r231exposure_NOT_COVERED_CRM) {
		R231EXPOSURE_NOT_COVERED_CRM = r231exposure_NOT_COVERED_CRM;
	}
	public void setR231RWA_TOTAL(BigDecimal r231rwa_TOTAL) {
		R231RWA_TOTAL = r231rwa_TOTAL;
	}
	public void setR231CRM_RISK_WEIGHT(BigDecimal r231crm_RISK_WEIGHT) {
		R231CRM_RISK_WEIGHT = r231crm_RISK_WEIGHT;
	}
	public void setR231RWA_CRM_COVERED(BigDecimal r231rwa_CRM_COVERED) {
		R231RWA_CRM_COVERED = r231rwa_CRM_COVERED;
	}
	public void setR231ORIGINAL_CP_RISK_WEIGHT(BigDecimal r231original_CP_RISK_WEIGHT) {
		R231ORIGINAL_CP_RISK_WEIGHT = r231original_CP_RISK_WEIGHT;
	}
	public void setR231RWA_NOT_COVERED(BigDecimal r231rwa_NOT_COVERED) {
		R231RWA_NOT_COVERED = r231rwa_NOT_COVERED;
	}
	public void setR231COLLATERAL_CEA_ELIGIBLE(BigDecimal r231collateral_CEA_ELIGIBLE) {
		R231COLLATERAL_CEA_ELIGIBLE = r231collateral_CEA_ELIGIBLE;
	}
	public void setR231CEA_AFTER_VOL_ADJ(BigDecimal r231cea_AFTER_VOL_ADJ) {
		R231CEA_AFTER_VOL_ADJ = r231cea_AFTER_VOL_ADJ;
	}
	public void setR231COLL_CASH(BigDecimal r231coll_CASH) {
		R231COLL_CASH = r231coll_CASH;
	}
	public void setR231COLL_TBILLS(BigDecimal r231coll_TBILLS) {
		R231COLL_TBILLS = r231coll_TBILLS;
	}
	public void setR231COLL_DEBT_SEC(BigDecimal r231coll_DEBT_SEC) {
		R231COLL_DEBT_SEC = r231coll_DEBT_SEC;
	}
	public void setR231COLL_EQUITIES(BigDecimal r231coll_EQUITIES) {
		R231COLL_EQUITIES = r231coll_EQUITIES;
	}
	public void setR231COLL_MUTUAL_FUNDS(BigDecimal r231coll_MUTUAL_FUNDS) {
		R231COLL_MUTUAL_FUNDS = r231coll_MUTUAL_FUNDS;
	}
	public void setR231COLL_TOTAL(BigDecimal r231coll_TOTAL) {
		R231COLL_TOTAL = r231coll_TOTAL;
	}
	public void setR231CEA_AFTER_CRM(BigDecimal r231cea_AFTER_CRM) {
		R231CEA_AFTER_CRM = r231cea_AFTER_CRM;
	}
	public void setR231RWA_CEA_NOT_COVERED(BigDecimal r231rwa_CEA_NOT_COVERED) {
		R231RWA_CEA_NOT_COVERED = r231rwa_CEA_NOT_COVERED;
	}
	public void setR231UNSECURED_CEA(BigDecimal r231unsecured_CEA) {
		R231UNSECURED_CEA = r231unsecured_CEA;
	}
	public void setR231RWA_UNSECURED_CEA(BigDecimal r231rwa_UNSECURED_CEA) {
		R231RWA_UNSECURED_CEA = r231rwa_UNSECURED_CEA;
	}
	public void setR232NOMINAL_PRINCIPAL_AMT(BigDecimal r232nominal_PRINCIPAL_AMT) {
		R232NOMINAL_PRINCIPAL_AMT = r232nominal_PRINCIPAL_AMT;
	}
	public void setR232CCF_PCT(BigDecimal r232ccf_PCT) {
		R232CCF_PCT = r232ccf_PCT;
	}
	public void setR232CREDIT_EQUIVALENT_AMT(BigDecimal r232credit_EQUIVALENT_AMT) {
		R232CREDIT_EQUIVALENT_AMT = r232credit_EQUIVALENT_AMT;
	}
	public void setR232CEA_ELIGIBLE_NETTING_CP(BigDecimal r232cea_ELIGIBLE_NETTING_CP) {
		R232CEA_ELIGIBLE_NETTING_CP = r232cea_ELIGIBLE_NETTING_CP;
	}
	public void setR232CEA_AFTER_NETTING(BigDecimal r232cea_AFTER_NETTING) {
		R232CEA_AFTER_NETTING = r232cea_AFTER_NETTING;
	}
	public void setR232CEA_ELIGIBLE_CRM_SUB(BigDecimal r232cea_ELIGIBLE_CRM_SUB) {
		R232CEA_ELIGIBLE_CRM_SUB = r232cea_ELIGIBLE_CRM_SUB;
	}
	public void setR232GUARANTEE_ELIGIBLE(BigDecimal r232guarantee_ELIGIBLE) {
		R232GUARANTEE_ELIGIBLE = r232guarantee_ELIGIBLE;
	}
	public void setR232CREDIT_DERIVATIVES(BigDecimal r232credit_DERIVATIVES) {
		R232CREDIT_DERIVATIVES = r232credit_DERIVATIVES;
	}
	public void setR232EXPOSURE_COVERED_CRM(BigDecimal r232exposure_COVERED_CRM) {
		R232EXPOSURE_COVERED_CRM = r232exposure_COVERED_CRM;
	}
	public void setR232EXPOSURE_NOT_COVERED_CRM(BigDecimal r232exposure_NOT_COVERED_CRM) {
		R232EXPOSURE_NOT_COVERED_CRM = r232exposure_NOT_COVERED_CRM;
	}
	public void setR232RWA_TOTAL(BigDecimal r232rwa_TOTAL) {
		R232RWA_TOTAL = r232rwa_TOTAL;
	}
	public void setR232CRM_RISK_WEIGHT(BigDecimal r232crm_RISK_WEIGHT) {
		R232CRM_RISK_WEIGHT = r232crm_RISK_WEIGHT;
	}
	public void setR232RWA_CRM_COVERED(BigDecimal r232rwa_CRM_COVERED) {
		R232RWA_CRM_COVERED = r232rwa_CRM_COVERED;
	}
	public void setR232ORIGINAL_CP_RISK_WEIGHT(BigDecimal r232original_CP_RISK_WEIGHT) {
		R232ORIGINAL_CP_RISK_WEIGHT = r232original_CP_RISK_WEIGHT;
	}
	public void setR232RWA_NOT_COVERED(BigDecimal r232rwa_NOT_COVERED) {
		R232RWA_NOT_COVERED = r232rwa_NOT_COVERED;
	}
	public void setR232COLLATERAL_CEA_ELIGIBLE(BigDecimal r232collateral_CEA_ELIGIBLE) {
		R232COLLATERAL_CEA_ELIGIBLE = r232collateral_CEA_ELIGIBLE;
	}
	public void setR232CEA_AFTER_VOL_ADJ(BigDecimal r232cea_AFTER_VOL_ADJ) {
		R232CEA_AFTER_VOL_ADJ = r232cea_AFTER_VOL_ADJ;
	}
	public void setR232COLL_CASH(BigDecimal r232coll_CASH) {
		R232COLL_CASH = r232coll_CASH;
	}
	public void setR232COLL_TBILLS(BigDecimal r232coll_TBILLS) {
		R232COLL_TBILLS = r232coll_TBILLS;
	}
	public void setR232COLL_DEBT_SEC(BigDecimal r232coll_DEBT_SEC) {
		R232COLL_DEBT_SEC = r232coll_DEBT_SEC;
	}
	public void setR232COLL_EQUITIES(BigDecimal r232coll_EQUITIES) {
		R232COLL_EQUITIES = r232coll_EQUITIES;
	}
	public void setR232COLL_MUTUAL_FUNDS(BigDecimal r232coll_MUTUAL_FUNDS) {
		R232COLL_MUTUAL_FUNDS = r232coll_MUTUAL_FUNDS;
	}
	public void setR232COLL_TOTAL(BigDecimal r232coll_TOTAL) {
		R232COLL_TOTAL = r232coll_TOTAL;
	}
	public void setR232CEA_AFTER_CRM(BigDecimal r232cea_AFTER_CRM) {
		R232CEA_AFTER_CRM = r232cea_AFTER_CRM;
	}
	public void setR232RWA_CEA_NOT_COVERED(BigDecimal r232rwa_CEA_NOT_COVERED) {
		R232RWA_CEA_NOT_COVERED = r232rwa_CEA_NOT_COVERED;
	}
	public void setR232UNSECURED_CEA(BigDecimal r232unsecured_CEA) {
		R232UNSECURED_CEA = r232unsecured_CEA;
	}
	public void setR232RWA_UNSECURED_CEA(BigDecimal r232rwa_UNSECURED_CEA) {
		R232RWA_UNSECURED_CEA = r232rwa_UNSECURED_CEA;
	}
	public void setR233NOMINAL_PRINCIPAL_AMT(BigDecimal r233nominal_PRINCIPAL_AMT) {
		R233NOMINAL_PRINCIPAL_AMT = r233nominal_PRINCIPAL_AMT;
	}
	public void setR233CCF_PCT(BigDecimal r233ccf_PCT) {
		R233CCF_PCT = r233ccf_PCT;
	}
	public void setR233CREDIT_EQUIVALENT_AMT(BigDecimal r233credit_EQUIVALENT_AMT) {
		R233CREDIT_EQUIVALENT_AMT = r233credit_EQUIVALENT_AMT;
	}
	public void setR233CEA_ELIGIBLE_NETTING_CP(BigDecimal r233cea_ELIGIBLE_NETTING_CP) {
		R233CEA_ELIGIBLE_NETTING_CP = r233cea_ELIGIBLE_NETTING_CP;
	}
	public void setR233CEA_AFTER_NETTING(BigDecimal r233cea_AFTER_NETTING) {
		R233CEA_AFTER_NETTING = r233cea_AFTER_NETTING;
	}
	public void setR233CEA_ELIGIBLE_CRM_SUB(BigDecimal r233cea_ELIGIBLE_CRM_SUB) {
		R233CEA_ELIGIBLE_CRM_SUB = r233cea_ELIGIBLE_CRM_SUB;
	}
	public void setR233GUARANTEE_ELIGIBLE(BigDecimal r233guarantee_ELIGIBLE) {
		R233GUARANTEE_ELIGIBLE = r233guarantee_ELIGIBLE;
	}
	public void setR233CREDIT_DERIVATIVES(BigDecimal r233credit_DERIVATIVES) {
		R233CREDIT_DERIVATIVES = r233credit_DERIVATIVES;
	}
	public void setR233EXPOSURE_COVERED_CRM(BigDecimal r233exposure_COVERED_CRM) {
		R233EXPOSURE_COVERED_CRM = r233exposure_COVERED_CRM;
	}
	public void setR233EXPOSURE_NOT_COVERED_CRM(BigDecimal r233exposure_NOT_COVERED_CRM) {
		R233EXPOSURE_NOT_COVERED_CRM = r233exposure_NOT_COVERED_CRM;
	}
	public void setR233RWA_TOTAL(BigDecimal r233rwa_TOTAL) {
		R233RWA_TOTAL = r233rwa_TOTAL;
	}
	public void setR233CRM_RISK_WEIGHT(BigDecimal r233crm_RISK_WEIGHT) {
		R233CRM_RISK_WEIGHT = r233crm_RISK_WEIGHT;
	}
	public void setR233RWA_CRM_COVERED(BigDecimal r233rwa_CRM_COVERED) {
		R233RWA_CRM_COVERED = r233rwa_CRM_COVERED;
	}
	public void setR233ORIGINAL_CP_RISK_WEIGHT(BigDecimal r233original_CP_RISK_WEIGHT) {
		R233ORIGINAL_CP_RISK_WEIGHT = r233original_CP_RISK_WEIGHT;
	}
	public void setR233RWA_NOT_COVERED(BigDecimal r233rwa_NOT_COVERED) {
		R233RWA_NOT_COVERED = r233rwa_NOT_COVERED;
	}
	public void setR233COLLATERAL_CEA_ELIGIBLE(BigDecimal r233collateral_CEA_ELIGIBLE) {
		R233COLLATERAL_CEA_ELIGIBLE = r233collateral_CEA_ELIGIBLE;
	}
	public void setR233CEA_AFTER_VOL_ADJ(BigDecimal r233cea_AFTER_VOL_ADJ) {
		R233CEA_AFTER_VOL_ADJ = r233cea_AFTER_VOL_ADJ;
	}
	public void setR233COLL_CASH(BigDecimal r233coll_CASH) {
		R233COLL_CASH = r233coll_CASH;
	}
	public void setR233COLL_TBILLS(BigDecimal r233coll_TBILLS) {
		R233COLL_TBILLS = r233coll_TBILLS;
	}
	public void setR233COLL_DEBT_SEC(BigDecimal r233coll_DEBT_SEC) {
		R233COLL_DEBT_SEC = r233coll_DEBT_SEC;
	}
	public void setR233COLL_EQUITIES(BigDecimal r233coll_EQUITIES) {
		R233COLL_EQUITIES = r233coll_EQUITIES;
	}
	public void setR233COLL_MUTUAL_FUNDS(BigDecimal r233coll_MUTUAL_FUNDS) {
		R233COLL_MUTUAL_FUNDS = r233coll_MUTUAL_FUNDS;
	}
	public void setR233COLL_TOTAL(BigDecimal r233coll_TOTAL) {
		R233COLL_TOTAL = r233coll_TOTAL;
	}
	public void setR233CEA_AFTER_CRM(BigDecimal r233cea_AFTER_CRM) {
		R233CEA_AFTER_CRM = r233cea_AFTER_CRM;
	}
	public void setR233RWA_CEA_NOT_COVERED(BigDecimal r233rwa_CEA_NOT_COVERED) {
		R233RWA_CEA_NOT_COVERED = r233rwa_CEA_NOT_COVERED;
	}
	public void setR233UNSECURED_CEA(BigDecimal r233unsecured_CEA) {
		R233UNSECURED_CEA = r233unsecured_CEA;
	}
	public void setR233RWA_UNSECURED_CEA(BigDecimal r233rwa_UNSECURED_CEA) {
		R233RWA_UNSECURED_CEA = r233rwa_UNSECURED_CEA;
	}
	public void setR234NOMINAL_PRINCIPAL_AMT(BigDecimal r234nominal_PRINCIPAL_AMT) {
		R234NOMINAL_PRINCIPAL_AMT = r234nominal_PRINCIPAL_AMT;
	}
	public void setR234CCF_PCT(BigDecimal r234ccf_PCT) {
		R234CCF_PCT = r234ccf_PCT;
	}
	public void setR234CREDIT_EQUIVALENT_AMT(BigDecimal r234credit_EQUIVALENT_AMT) {
		R234CREDIT_EQUIVALENT_AMT = r234credit_EQUIVALENT_AMT;
	}
	public void setR234CEA_ELIGIBLE_NETTING_CP(BigDecimal r234cea_ELIGIBLE_NETTING_CP) {
		R234CEA_ELIGIBLE_NETTING_CP = r234cea_ELIGIBLE_NETTING_CP;
	}
	public void setR234CEA_AFTER_NETTING(BigDecimal r234cea_AFTER_NETTING) {
		R234CEA_AFTER_NETTING = r234cea_AFTER_NETTING;
	}
	public void setR234CEA_ELIGIBLE_CRM_SUB(BigDecimal r234cea_ELIGIBLE_CRM_SUB) {
		R234CEA_ELIGIBLE_CRM_SUB = r234cea_ELIGIBLE_CRM_SUB;
	}
	public void setR234GUARANTEE_ELIGIBLE(BigDecimal r234guarantee_ELIGIBLE) {
		R234GUARANTEE_ELIGIBLE = r234guarantee_ELIGIBLE;
	}
	public void setR234CREDIT_DERIVATIVES(BigDecimal r234credit_DERIVATIVES) {
		R234CREDIT_DERIVATIVES = r234credit_DERIVATIVES;
	}
	public void setR234EXPOSURE_COVERED_CRM(BigDecimal r234exposure_COVERED_CRM) {
		R234EXPOSURE_COVERED_CRM = r234exposure_COVERED_CRM;
	}
	public void setR234EXPOSURE_NOT_COVERED_CRM(BigDecimal r234exposure_NOT_COVERED_CRM) {
		R234EXPOSURE_NOT_COVERED_CRM = r234exposure_NOT_COVERED_CRM;
	}
	public void setR234RWA_TOTAL(BigDecimal r234rwa_TOTAL) {
		R234RWA_TOTAL = r234rwa_TOTAL;
	}
	public void setR234CRM_RISK_WEIGHT(BigDecimal r234crm_RISK_WEIGHT) {
		R234CRM_RISK_WEIGHT = r234crm_RISK_WEIGHT;
	}
	public void setR234RWA_CRM_COVERED(BigDecimal r234rwa_CRM_COVERED) {
		R234RWA_CRM_COVERED = r234rwa_CRM_COVERED;
	}
	public void setR234ORIGINAL_CP_RISK_WEIGHT(BigDecimal r234original_CP_RISK_WEIGHT) {
		R234ORIGINAL_CP_RISK_WEIGHT = r234original_CP_RISK_WEIGHT;
	}
	public void setR234RWA_NOT_COVERED(BigDecimal r234rwa_NOT_COVERED) {
		R234RWA_NOT_COVERED = r234rwa_NOT_COVERED;
	}
	public void setR234COLLATERAL_CEA_ELIGIBLE(BigDecimal r234collateral_CEA_ELIGIBLE) {
		R234COLLATERAL_CEA_ELIGIBLE = r234collateral_CEA_ELIGIBLE;
	}
	public void setR234CEA_AFTER_VOL_ADJ(BigDecimal r234cea_AFTER_VOL_ADJ) {
		R234CEA_AFTER_VOL_ADJ = r234cea_AFTER_VOL_ADJ;
	}
	public void setR234COLL_CASH(BigDecimal r234coll_CASH) {
		R234COLL_CASH = r234coll_CASH;
	}
	public void setR234COLL_TBILLS(BigDecimal r234coll_TBILLS) {
		R234COLL_TBILLS = r234coll_TBILLS;
	}
	public void setR234COLL_DEBT_SEC(BigDecimal r234coll_DEBT_SEC) {
		R234COLL_DEBT_SEC = r234coll_DEBT_SEC;
	}
	public void setR234COLL_EQUITIES(BigDecimal r234coll_EQUITIES) {
		R234COLL_EQUITIES = r234coll_EQUITIES;
	}
	public void setR234COLL_MUTUAL_FUNDS(BigDecimal r234coll_MUTUAL_FUNDS) {
		R234COLL_MUTUAL_FUNDS = r234coll_MUTUAL_FUNDS;
	}
	public void setR234COLL_TOTAL(BigDecimal r234coll_TOTAL) {
		R234COLL_TOTAL = r234coll_TOTAL;
	}
	public void setR234CEA_AFTER_CRM(BigDecimal r234cea_AFTER_CRM) {
		R234CEA_AFTER_CRM = r234cea_AFTER_CRM;
	}
	public void setR234RWA_CEA_NOT_COVERED(BigDecimal r234rwa_CEA_NOT_COVERED) {
		R234RWA_CEA_NOT_COVERED = r234rwa_CEA_NOT_COVERED;
	}
	public void setR234UNSECURED_CEA(BigDecimal r234unsecured_CEA) {
		R234UNSECURED_CEA = r234unsecured_CEA;
	}
	public void setR234RWA_UNSECURED_CEA(BigDecimal r234rwa_UNSECURED_CEA) {
		R234RWA_UNSECURED_CEA = r234rwa_UNSECURED_CEA;
	}
	public M_SRWA_12B_SUMMARY_8_NEW_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
