package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="BRRS_M_SRWA_12B_SUMMARY_TABLE_1_NEW")
public class M_SRWA_12B_SUMMARY_1_NEW_ENTITY {

	@Id
	private Date REPORT_DATE;
	private String REPORT_VERSION;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;
	
	private BigDecimal R13_EXPOSURE_BEFORE_CRM;
	private BigDecimal R13_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R13_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R13_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R13_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R13_ELIG_GUARANTEES;
	private BigDecimal R13_CREDIT_DERIVATIVES;
	private BigDecimal R13_CRM_COVERED_EXPOSURE;
	private BigDecimal R13_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R13_CRM_RISK_WEIGHT;
	private BigDecimal R13_RWA_CRM_COVERED;
	private BigDecimal R13_ORIG_COUNTERPARTY_RW;
	private BigDecimal R13_RWA_CRM_NOT_COVERED;
	private BigDecimal R13_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R13_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R13_COLLATERAL_CASH;
	private BigDecimal R13_COLLATERAL_TBILLS;
	private BigDecimal R13_COLLATERAL_DEBT_SEC;
	private BigDecimal R13_COLLATERAL_EQUITIES;
	private BigDecimal R13_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R13_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R13_EXPOSURE_AFTER_CRM;
	private BigDecimal R13_RWA_NOT_COVERED_CRM;
	private BigDecimal R13_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R13_RWA_UNSECURED;
	private BigDecimal R13_TOTAL_RWA;
	private BigDecimal R14_EXPOSURE_BEFORE_CRM;
	private BigDecimal R14_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R14_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R14_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R14_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R14_ELIG_GUARANTEES;
	private BigDecimal R14_CREDIT_DERIVATIVES;
	private BigDecimal R14_CRM_COVERED_EXPOSURE;
	private BigDecimal R14_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R14_CRM_RISK_WEIGHT;
	private BigDecimal R14_RWA_CRM_COVERED;
	private BigDecimal R14_ORIG_COUNTERPARTY_RW;
	private BigDecimal R14_RWA_CRM_NOT_COVERED;
	private BigDecimal R14_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R14_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R14_COLLATERAL_CASH;
	private BigDecimal R14_COLLATERAL_TBILLS;
	private BigDecimal R14_COLLATERAL_DEBT_SEC;
	private BigDecimal R14_COLLATERAL_EQUITIES;
	private BigDecimal R14_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R14_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R14_EXPOSURE_AFTER_CRM;
	private BigDecimal R14_RWA_NOT_COVERED_CRM;
	private BigDecimal R14_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R14_RWA_UNSECURED;
	private BigDecimal R14_TOTAL_RWA;
	private BigDecimal R15_EXPOSURE_BEFORE_CRM;
	private BigDecimal R15_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R15_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R15_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R15_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R15_ELIG_GUARANTEES;
	private BigDecimal R15_CREDIT_DERIVATIVES;
	private BigDecimal R15_CRM_COVERED_EXPOSURE;
	private BigDecimal R15_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R15_CRM_RISK_WEIGHT;
	private BigDecimal R15_RWA_CRM_COVERED;
	private BigDecimal R15_ORIG_COUNTERPARTY_RW;
	private BigDecimal R15_RWA_CRM_NOT_COVERED;
	private BigDecimal R15_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R15_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R15_COLLATERAL_CASH;
	private BigDecimal R15_COLLATERAL_TBILLS;
	private BigDecimal R15_COLLATERAL_DEBT_SEC;
	private BigDecimal R15_COLLATERAL_EQUITIES;
	private BigDecimal R15_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R15_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R15_EXPOSURE_AFTER_CRM;
	private BigDecimal R15_RWA_NOT_COVERED_CRM;
	private BigDecimal R15_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R15_RWA_UNSECURED;
	private BigDecimal R15_TOTAL_RWA;
	private BigDecimal R16_EXPOSURE_BEFORE_CRM;
	private BigDecimal R16_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R16_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R16_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R16_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R16_ELIG_GUARANTEES;
	private BigDecimal R16_CREDIT_DERIVATIVES;
	private BigDecimal R16_CRM_COVERED_EXPOSURE;
	private BigDecimal R16_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R16_CRM_RISK_WEIGHT;
	private BigDecimal R16_RWA_CRM_COVERED;
	private BigDecimal R16_ORIG_COUNTERPARTY_RW;
	private BigDecimal R16_RWA_CRM_NOT_COVERED;
	private BigDecimal R16_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R16_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R16_COLLATERAL_CASH;
	private BigDecimal R16_COLLATERAL_TBILLS;
	private BigDecimal R16_COLLATERAL_DEBT_SEC;
	private BigDecimal R16_COLLATERAL_EQUITIES;
	private BigDecimal R16_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R16_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R16_EXPOSURE_AFTER_CRM;
	private BigDecimal R16_RWA_NOT_COVERED_CRM;
	private BigDecimal R16_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R16_RWA_UNSECURED;
	private BigDecimal R16_TOTAL_RWA;
	private BigDecimal R17_EXPOSURE_BEFORE_CRM;
	private BigDecimal R17_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R17_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R17_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R17_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R17_ELIG_GUARANTEES;
	private BigDecimal R17_CREDIT_DERIVATIVES;
	private BigDecimal R17_CRM_COVERED_EXPOSURE;
	private BigDecimal R17_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R17_CRM_RISK_WEIGHT;
	private BigDecimal R17_RWA_CRM_COVERED;
	private BigDecimal R17_ORIG_COUNTERPARTY_RW;
	private BigDecimal R17_RWA_CRM_NOT_COVERED;
	private BigDecimal R17_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R17_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R17_COLLATERAL_CASH;
	private BigDecimal R17_COLLATERAL_TBILLS;
	private BigDecimal R17_COLLATERAL_DEBT_SEC;
	private BigDecimal R17_COLLATERAL_EQUITIES;
	private BigDecimal R17_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R17_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R17_EXPOSURE_AFTER_CRM;
	private BigDecimal R17_RWA_NOT_COVERED_CRM;
	private BigDecimal R17_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R17_RWA_UNSECURED;
	private BigDecimal R17_TOTAL_RWA;
	private BigDecimal R18_EXPOSURE_BEFORE_CRM;
	private BigDecimal R18_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R18_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R18_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R18_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R18_ELIG_GUARANTEES;
	private BigDecimal R18_CREDIT_DERIVATIVES;
	private BigDecimal R18_CRM_COVERED_EXPOSURE;
	private BigDecimal R18_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R18_CRM_RISK_WEIGHT;
	private BigDecimal R18_RWA_CRM_COVERED;
	private BigDecimal R18_ORIG_COUNTERPARTY_RW;
	private BigDecimal R18_RWA_CRM_NOT_COVERED;
	private BigDecimal R18_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R18_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R18_COLLATERAL_CASH;
	private BigDecimal R18_COLLATERAL_TBILLS;
	private BigDecimal R18_COLLATERAL_DEBT_SEC;
	private BigDecimal R18_COLLATERAL_EQUITIES;
	private BigDecimal R18_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R18_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R18_EXPOSURE_AFTER_CRM;
	private BigDecimal R18_RWA_NOT_COVERED_CRM;
	private BigDecimal R18_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R18_RWA_UNSECURED;
	private BigDecimal R18_TOTAL_RWA;
	private BigDecimal R19_EXPOSURE_BEFORE_CRM;
	private BigDecimal R19_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R19_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R19_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R19_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R19_ELIG_GUARANTEES;
	private BigDecimal R19_CREDIT_DERIVATIVES;
	private BigDecimal R19_CRM_COVERED_EXPOSURE;
	private BigDecimal R19_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R19_CRM_RISK_WEIGHT;
	private BigDecimal R19_RWA_CRM_COVERED;
	private BigDecimal R19_ORIG_COUNTERPARTY_RW;
	private BigDecimal R19_RWA_CRM_NOT_COVERED;
	private BigDecimal R19_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R19_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R19_COLLATERAL_CASH;
	private BigDecimal R19_COLLATERAL_TBILLS;
	private BigDecimal R19_COLLATERAL_DEBT_SEC;
	private BigDecimal R19_COLLATERAL_EQUITIES;
	private BigDecimal R19_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R19_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R19_EXPOSURE_AFTER_CRM;
	private BigDecimal R19_RWA_NOT_COVERED_CRM;
	private BigDecimal R19_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R19_RWA_UNSECURED;
	private BigDecimal R19_TOTAL_RWA;
	private BigDecimal R20_EXPOSURE_BEFORE_CRM;
	private BigDecimal R20_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R20_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R20_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R20_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R20_ELIG_GUARANTEES;
	private BigDecimal R20_CREDIT_DERIVATIVES;
	private BigDecimal R20_CRM_COVERED_EXPOSURE;
	private BigDecimal R20_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R20_CRM_RISK_WEIGHT;
	private BigDecimal R20_RWA_CRM_COVERED;
	private BigDecimal R20_ORIG_COUNTERPARTY_RW;
	private BigDecimal R20_RWA_CRM_NOT_COVERED;
	private BigDecimal R20_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R20_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R20_COLLATERAL_CASH;
	private BigDecimal R20_COLLATERAL_TBILLS;
	private BigDecimal R20_COLLATERAL_DEBT_SEC;
	private BigDecimal R20_COLLATERAL_EQUITIES;
	private BigDecimal R20_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R20_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R20_EXPOSURE_AFTER_CRM;
	private BigDecimal R20_RWA_NOT_COVERED_CRM;
	private BigDecimal R20_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R20_RWA_UNSECURED;
	private BigDecimal R20_TOTAL_RWA;
	private BigDecimal R21_EXPOSURE_BEFORE_CRM;
	private BigDecimal R21_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R21_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R21_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R21_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R21_ELIG_GUARANTEES;
	private BigDecimal R21_CREDIT_DERIVATIVES;
	private BigDecimal R21_CRM_COVERED_EXPOSURE;
	private BigDecimal R21_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R21_CRM_RISK_WEIGHT;
	private BigDecimal R21_RWA_CRM_COVERED;
	private BigDecimal R21_ORIG_COUNTERPARTY_RW;
	private BigDecimal R21_RWA_CRM_NOT_COVERED;
	private BigDecimal R21_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R21_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R21_COLLATERAL_CASH;
	private BigDecimal R21_COLLATERAL_TBILLS;
	private BigDecimal R21_COLLATERAL_DEBT_SEC;
	private BigDecimal R21_COLLATERAL_EQUITIES;
	private BigDecimal R21_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R21_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R21_EXPOSURE_AFTER_CRM;
	private BigDecimal R21_RWA_NOT_COVERED_CRM;
	private BigDecimal R21_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R21_RWA_UNSECURED;
	private BigDecimal R21_TOTAL_RWA;
	private BigDecimal R22_EXPOSURE_BEFORE_CRM;
	private BigDecimal R22_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R22_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R22_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R22_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R22_ELIG_GUARANTEES;
	private BigDecimal R22_CREDIT_DERIVATIVES;
	private BigDecimal R22_CRM_COVERED_EXPOSURE;
	private BigDecimal R22_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R22_CRM_RISK_WEIGHT;
	private BigDecimal R22_RWA_CRM_COVERED;
	private BigDecimal R22_ORIG_COUNTERPARTY_RW;
	private BigDecimal R22_RWA_CRM_NOT_COVERED;
	private BigDecimal R22_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R22_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R22_COLLATERAL_CASH;
	private BigDecimal R22_COLLATERAL_TBILLS;
	private BigDecimal R22_COLLATERAL_DEBT_SEC;
	private BigDecimal R22_COLLATERAL_EQUITIES;
	private BigDecimal R22_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R22_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R22_EXPOSURE_AFTER_CRM;
	private BigDecimal R22_RWA_NOT_COVERED_CRM;
	private BigDecimal R22_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R22_RWA_UNSECURED;
	private BigDecimal R22_TOTAL_RWA;
	private BigDecimal R23_EXPOSURE_BEFORE_CRM;
	private BigDecimal R23_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R23_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R23_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R23_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R23_ELIG_GUARANTEES;
	private BigDecimal R23_CREDIT_DERIVATIVES;
	private BigDecimal R23_CRM_COVERED_EXPOSURE;
	private BigDecimal R23_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R23_CRM_RISK_WEIGHT;
	private BigDecimal R23_RWA_CRM_COVERED;
	private BigDecimal R23_ORIG_COUNTERPARTY_RW;
	private BigDecimal R23_RWA_CRM_NOT_COVERED;
	private BigDecimal R23_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R23_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R23_COLLATERAL_CASH;
	private BigDecimal R23_COLLATERAL_TBILLS;
	private BigDecimal R23_COLLATERAL_DEBT_SEC;
	private BigDecimal R23_COLLATERAL_EQUITIES;
	private BigDecimal R23_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R23_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R23_EXPOSURE_AFTER_CRM;
	private BigDecimal R23_RWA_NOT_COVERED_CRM;
	private BigDecimal R23_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R23_RWA_UNSECURED;
	private BigDecimal R23_TOTAL_RWA;
	private BigDecimal R24_EXPOSURE_BEFORE_CRM;
	private BigDecimal R24_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R24_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R24_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R24_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R24_ELIG_GUARANTEES;
	private BigDecimal R24_CREDIT_DERIVATIVES;
	private BigDecimal R24_CRM_COVERED_EXPOSURE;
	private BigDecimal R24_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R24_CRM_RISK_WEIGHT;
	private BigDecimal R24_RWA_CRM_COVERED;
	private BigDecimal R24_ORIG_COUNTERPARTY_RW;
	private BigDecimal R24_RWA_CRM_NOT_COVERED;
	private BigDecimal R24_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R24_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R24_COLLATERAL_CASH;
	private BigDecimal R24_COLLATERAL_TBILLS;
	private BigDecimal R24_COLLATERAL_DEBT_SEC;
	private BigDecimal R24_COLLATERAL_EQUITIES;
	private BigDecimal R24_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R24_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R24_EXPOSURE_AFTER_CRM;
	private BigDecimal R24_RWA_NOT_COVERED_CRM;
	private BigDecimal R24_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R24_RWA_UNSECURED;
	private BigDecimal R24_TOTAL_RWA;
	private BigDecimal R25_EXPOSURE_BEFORE_CRM;
	private BigDecimal R25_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R25_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R25_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R25_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R25_ELIG_GUARANTEES;
	private BigDecimal R25_CREDIT_DERIVATIVES;
	private BigDecimal R25_CRM_COVERED_EXPOSURE;
	private BigDecimal R25_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R25_CRM_RISK_WEIGHT;
	private BigDecimal R25_RWA_CRM_COVERED;
	private BigDecimal R25_ORIG_COUNTERPARTY_RW;
	private BigDecimal R25_RWA_CRM_NOT_COVERED;
	private BigDecimal R25_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R25_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R25_COLLATERAL_CASH;
	private BigDecimal R25_COLLATERAL_TBILLS;
	private BigDecimal R25_COLLATERAL_DEBT_SEC;
	private BigDecimal R25_COLLATERAL_EQUITIES;
	private BigDecimal R25_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R25_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R25_EXPOSURE_AFTER_CRM;
	private BigDecimal R25_RWA_NOT_COVERED_CRM;
	private BigDecimal R25_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R25_RWA_UNSECURED;
	private BigDecimal R25_TOTAL_RWA;
	private BigDecimal R26_EXPOSURE_BEFORE_CRM;
	private BigDecimal R26_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R26_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R26_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R26_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R26_ELIG_GUARANTEES;
	private BigDecimal R26_CREDIT_DERIVATIVES;
	private BigDecimal R26_CRM_COVERED_EXPOSURE;
	private BigDecimal R26_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R26_CRM_RISK_WEIGHT;
	private BigDecimal R26_RWA_CRM_COVERED;
	private BigDecimal R26_ORIG_COUNTERPARTY_RW;
	private BigDecimal R26_RWA_CRM_NOT_COVERED;
	private BigDecimal R26_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R26_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R26_COLLATERAL_CASH;
	private BigDecimal R26_COLLATERAL_TBILLS;
	private BigDecimal R26_COLLATERAL_DEBT_SEC;
	private BigDecimal R26_COLLATERAL_EQUITIES;
	private BigDecimal R26_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R26_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R26_EXPOSURE_AFTER_CRM;
	private BigDecimal R26_RWA_NOT_COVERED_CRM;
	private BigDecimal R26_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R26_RWA_UNSECURED;
	private BigDecimal R26_TOTAL_RWA;
	private BigDecimal R27_EXPOSURE_BEFORE_CRM;
	private BigDecimal R27_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R27_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R27_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R27_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R27_ELIG_GUARANTEES;
	private BigDecimal R27_CREDIT_DERIVATIVES;
	private BigDecimal R27_CRM_COVERED_EXPOSURE;
	private BigDecimal R27_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R27_CRM_RISK_WEIGHT;
	private BigDecimal R27_RWA_CRM_COVERED;
	private BigDecimal R27_ORIG_COUNTERPARTY_RW;
	private BigDecimal R27_RWA_CRM_NOT_COVERED;
	private BigDecimal R27_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R27_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R27_COLLATERAL_CASH;
	private BigDecimal R27_COLLATERAL_TBILLS;
	private BigDecimal R27_COLLATERAL_DEBT_SEC;
	private BigDecimal R27_COLLATERAL_EQUITIES;
	private BigDecimal R27_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R27_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R27_EXPOSURE_AFTER_CRM;
	private BigDecimal R27_RWA_NOT_COVERED_CRM;
	private BigDecimal R27_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R27_RWA_UNSECURED;
	private BigDecimal R27_TOTAL_RWA;
	private BigDecimal R28_EXPOSURE_BEFORE_CRM;
	private BigDecimal R28_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R28_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R28_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R28_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R28_ELIG_GUARANTEES;
	private BigDecimal R28_CREDIT_DERIVATIVES;
	private BigDecimal R28_CRM_COVERED_EXPOSURE;
	private BigDecimal R28_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R28_CRM_RISK_WEIGHT;
	private BigDecimal R28_RWA_CRM_COVERED;
	private BigDecimal R28_ORIG_COUNTERPARTY_RW;
	private BigDecimal R28_RWA_CRM_NOT_COVERED;
	private BigDecimal R28_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R28_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R28_COLLATERAL_CASH;
	private BigDecimal R28_COLLATERAL_TBILLS;
	private BigDecimal R28_COLLATERAL_DEBT_SEC;
	private BigDecimal R28_COLLATERAL_EQUITIES;
	private BigDecimal R28_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R28_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R28_EXPOSURE_AFTER_CRM;
	private BigDecimal R28_RWA_NOT_COVERED_CRM;
	private BigDecimal R28_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R28_RWA_UNSECURED;
	private BigDecimal R28_TOTAL_RWA;
	private BigDecimal R29_EXPOSURE_BEFORE_CRM;
	private BigDecimal R29_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R29_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R29_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R29_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R29_ELIG_GUARANTEES;
	private BigDecimal R29_CREDIT_DERIVATIVES;
	private BigDecimal R29_CRM_COVERED_EXPOSURE;
	private BigDecimal R29_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R29_CRM_RISK_WEIGHT;
	private BigDecimal R29_RWA_CRM_COVERED;
	private BigDecimal R29_ORIG_COUNTERPARTY_RW;
	private BigDecimal R29_RWA_CRM_NOT_COVERED;
	private BigDecimal R29_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R29_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R29_COLLATERAL_CASH;
	private BigDecimal R29_COLLATERAL_TBILLS;
	private BigDecimal R29_COLLATERAL_DEBT_SEC;
	private BigDecimal R29_COLLATERAL_EQUITIES;
	private BigDecimal R29_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R29_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R29_EXPOSURE_AFTER_CRM;
	private BigDecimal R29_RWA_NOT_COVERED_CRM;
	private BigDecimal R29_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R29_RWA_UNSECURED;
	private BigDecimal R29_TOTAL_RWA;
	private BigDecimal R30_EXPOSURE_BEFORE_CRM;
	private BigDecimal R30_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R30_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R30_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R30_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R30_ELIG_GUARANTEES;
	private BigDecimal R30_CREDIT_DERIVATIVES;
	private BigDecimal R30_CRM_COVERED_EXPOSURE;
	private BigDecimal R30_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R30_CRM_RISK_WEIGHT;
	private BigDecimal R30_RWA_CRM_COVERED;
	private BigDecimal R30_ORIG_COUNTERPARTY_RW;
	private BigDecimal R30_RWA_CRM_NOT_COVERED;
	private BigDecimal R30_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R30_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R30_COLLATERAL_CASH;
	private BigDecimal R30_COLLATERAL_TBILLS;
	private BigDecimal R30_COLLATERAL_DEBT_SEC;
	private BigDecimal R30_COLLATERAL_EQUITIES;
	private BigDecimal R30_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R30_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R30_EXPOSURE_AFTER_CRM;
	private BigDecimal R30_RWA_NOT_COVERED_CRM;
	private BigDecimal R30_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R30_RWA_UNSECURED;
	private BigDecimal R30_TOTAL_RWA;
	private BigDecimal R31_EXPOSURE_BEFORE_CRM;
	private BigDecimal R31_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R31_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R31_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R31_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R31_ELIG_GUARANTEES;
	private BigDecimal R31_CREDIT_DERIVATIVES;
	private BigDecimal R31_CRM_COVERED_EXPOSURE;
	private BigDecimal R31_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R31_CRM_RISK_WEIGHT;
	private BigDecimal R31_RWA_CRM_COVERED;
	private BigDecimal R31_ORIG_COUNTERPARTY_RW;
	private BigDecimal R31_RWA_CRM_NOT_COVERED;
	private BigDecimal R31_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R31_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R31_COLLATERAL_CASH;
	private BigDecimal R31_COLLATERAL_TBILLS;
	private BigDecimal R31_COLLATERAL_DEBT_SEC;
	private BigDecimal R31_COLLATERAL_EQUITIES;
	private BigDecimal R31_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R31_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R31_EXPOSURE_AFTER_CRM;
	private BigDecimal R31_RWA_NOT_COVERED_CRM;
	private BigDecimal R31_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R31_RWA_UNSECURED;
	private BigDecimal R31_TOTAL_RWA;
	private BigDecimal R32_EXPOSURE_BEFORE_CRM;
	private BigDecimal R32_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R32_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R32_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R32_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R32_ELIG_GUARANTEES;
	private BigDecimal R32_CREDIT_DERIVATIVES;
	private BigDecimal R32_CRM_COVERED_EXPOSURE;
	private BigDecimal R32_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R32_CRM_RISK_WEIGHT;
	private BigDecimal R32_RWA_CRM_COVERED;
	private BigDecimal R32_ORIG_COUNTERPARTY_RW;
	private BigDecimal R32_RWA_CRM_NOT_COVERED;
	private BigDecimal R32_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R32_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R32_COLLATERAL_CASH;
	private BigDecimal R32_COLLATERAL_TBILLS;
	private BigDecimal R32_COLLATERAL_DEBT_SEC;
	private BigDecimal R32_COLLATERAL_EQUITIES;
	private BigDecimal R32_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R32_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R32_EXPOSURE_AFTER_CRM;
	private BigDecimal R32_RWA_NOT_COVERED_CRM;
	private BigDecimal R32_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R32_RWA_UNSECURED;
	private BigDecimal R32_TOTAL_RWA;
	private BigDecimal R33_EXPOSURE_BEFORE_CRM;
	private BigDecimal R33_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R33_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R33_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R33_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R33_ELIG_GUARANTEES;
	private BigDecimal R33_CREDIT_DERIVATIVES;
	private BigDecimal R33_CRM_COVERED_EXPOSURE;
	private BigDecimal R33_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R33_CRM_RISK_WEIGHT;
	private BigDecimal R33_RWA_CRM_COVERED;
	private BigDecimal R33_ORIG_COUNTERPARTY_RW;
	private BigDecimal R33_RWA_CRM_NOT_COVERED;
	private BigDecimal R33_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R33_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R33_COLLATERAL_CASH;
	private BigDecimal R33_COLLATERAL_TBILLS;
	private BigDecimal R33_COLLATERAL_DEBT_SEC;
	private BigDecimal R33_COLLATERAL_EQUITIES;
	private BigDecimal R33_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R33_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R33_EXPOSURE_AFTER_CRM;
	private BigDecimal R33_RWA_NOT_COVERED_CRM;
	private BigDecimal R33_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R33_RWA_UNSECURED;
	private BigDecimal R33_TOTAL_RWA;
	private BigDecimal R34_EXPOSURE_BEFORE_CRM;
	private BigDecimal R34_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R34_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R34_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R34_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R34_ELIG_GUARANTEES;
	private BigDecimal R34_CREDIT_DERIVATIVES;
	private BigDecimal R34_CRM_COVERED_EXPOSURE;
	private BigDecimal R34_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R34_CRM_RISK_WEIGHT;
	private BigDecimal R34_RWA_CRM_COVERED;
	private BigDecimal R34_ORIG_COUNTERPARTY_RW;
	private BigDecimal R34_RWA_CRM_NOT_COVERED;
	private BigDecimal R34_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R34_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R34_COLLATERAL_CASH;
	private BigDecimal R34_COLLATERAL_TBILLS;
	private BigDecimal R34_COLLATERAL_DEBT_SEC;
	private BigDecimal R34_COLLATERAL_EQUITIES;
	private BigDecimal R34_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R34_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R34_EXPOSURE_AFTER_CRM;
	private BigDecimal R34_RWA_NOT_COVERED_CRM;
	private BigDecimal R34_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R34_RWA_UNSECURED;
	private BigDecimal R34_TOTAL_RWA;
	private BigDecimal R35_EXPOSURE_BEFORE_CRM;
	private BigDecimal R35_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R35_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R35_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R35_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R35_ELIG_GUARANTEES;
	private BigDecimal R35_CREDIT_DERIVATIVES;
	private BigDecimal R35_CRM_COVERED_EXPOSURE;
	private BigDecimal R35_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R35_CRM_RISK_WEIGHT;
	private BigDecimal R35_RWA_CRM_COVERED;
	private BigDecimal R35_ORIG_COUNTERPARTY_RW;
	private BigDecimal R35_RWA_CRM_NOT_COVERED;
	private BigDecimal R35_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R35_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R35_COLLATERAL_CASH;
	private BigDecimal R35_COLLATERAL_TBILLS;
	private BigDecimal R35_COLLATERAL_DEBT_SEC;
	private BigDecimal R35_COLLATERAL_EQUITIES;
	private BigDecimal R35_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R35_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R35_EXPOSURE_AFTER_CRM;
	private BigDecimal R35_RWA_NOT_COVERED_CRM;
	private BigDecimal R35_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R35_RWA_UNSECURED;
	private BigDecimal R35_TOTAL_RWA;
	private BigDecimal R36_EXPOSURE_BEFORE_CRM;
	private BigDecimal R36_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R36_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R36_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R36_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R36_ELIG_GUARANTEES;
	private BigDecimal R36_CREDIT_DERIVATIVES;
	private BigDecimal R36_CRM_COVERED_EXPOSURE;
	private BigDecimal R36_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R36_CRM_RISK_WEIGHT;
	private BigDecimal R36_RWA_CRM_COVERED;
	private BigDecimal R36_ORIG_COUNTERPARTY_RW;
	private BigDecimal R36_RWA_CRM_NOT_COVERED;
	private BigDecimal R36_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R36_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R36_COLLATERAL_CASH;
	private BigDecimal R36_COLLATERAL_TBILLS;
	private BigDecimal R36_COLLATERAL_DEBT_SEC;
	private BigDecimal R36_COLLATERAL_EQUITIES;
	private BigDecimal R36_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R36_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R36_EXPOSURE_AFTER_CRM;
	private BigDecimal R36_RWA_NOT_COVERED_CRM;
	private BigDecimal R36_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R36_RWA_UNSECURED;
	private BigDecimal R36_TOTAL_RWA;
	private BigDecimal R37_EXPOSURE_BEFORE_CRM;
	private BigDecimal R37_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R37_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R37_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R37_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R37_ELIG_GUARANTEES;
	private BigDecimal R37_CREDIT_DERIVATIVES;
	private BigDecimal R37_CRM_COVERED_EXPOSURE;
	private BigDecimal R37_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R37_CRM_RISK_WEIGHT;
	private BigDecimal R37_RWA_CRM_COVERED;
	private BigDecimal R37_ORIG_COUNTERPARTY_RW;
	private BigDecimal R37_RWA_CRM_NOT_COVERED;
	private BigDecimal R37_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R37_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R37_COLLATERAL_CASH;
	private BigDecimal R37_COLLATERAL_TBILLS;
	private BigDecimal R37_COLLATERAL_DEBT_SEC;
	private BigDecimal R37_COLLATERAL_EQUITIES;
	private BigDecimal R37_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R37_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R37_EXPOSURE_AFTER_CRM;
	private BigDecimal R37_RWA_NOT_COVERED_CRM;
	private BigDecimal R37_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R37_RWA_UNSECURED;
	private BigDecimal R37_TOTAL_RWA;
	private BigDecimal R38_EXPOSURE_BEFORE_CRM;
	private BigDecimal R38_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R38_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R38_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R38_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R38_ELIG_GUARANTEES;
	private BigDecimal R38_CREDIT_DERIVATIVES;
	private BigDecimal R38_CRM_COVERED_EXPOSURE;
	private BigDecimal R38_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R38_CRM_RISK_WEIGHT;
	private BigDecimal R38_RWA_CRM_COVERED;
	private BigDecimal R38_ORIG_COUNTERPARTY_RW;
	private BigDecimal R38_RWA_CRM_NOT_COVERED;
	private BigDecimal R38_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R38_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R38_COLLATERAL_CASH;
	private BigDecimal R38_COLLATERAL_TBILLS;
	private BigDecimal R38_COLLATERAL_DEBT_SEC;
	private BigDecimal R38_COLLATERAL_EQUITIES;
	private BigDecimal R38_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R38_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R38_EXPOSURE_AFTER_CRM;
	private BigDecimal R38_RWA_NOT_COVERED_CRM;
	private BigDecimal R38_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R38_RWA_UNSECURED;
	private BigDecimal R38_TOTAL_RWA;
	private BigDecimal R39_EXPOSURE_BEFORE_CRM;
	private BigDecimal R39_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R39_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R39_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R39_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R39_ELIG_GUARANTEES;
	private BigDecimal R39_CREDIT_DERIVATIVES;
	private BigDecimal R39_CRM_COVERED_EXPOSURE;
	private BigDecimal R39_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R39_CRM_RISK_WEIGHT;
	private BigDecimal R39_RWA_CRM_COVERED;
	private BigDecimal R39_ORIG_COUNTERPARTY_RW;
	private BigDecimal R39_RWA_CRM_NOT_COVERED;
	private BigDecimal R39_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R39_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R39_COLLATERAL_CASH;
	private BigDecimal R39_COLLATERAL_TBILLS;
	private BigDecimal R39_COLLATERAL_DEBT_SEC;
	private BigDecimal R39_COLLATERAL_EQUITIES;
	private BigDecimal R39_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R39_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R39_EXPOSURE_AFTER_CRM;
	private BigDecimal R39_RWA_NOT_COVERED_CRM;
	private BigDecimal R39_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R39_RWA_UNSECURED;
	private BigDecimal R39_TOTAL_RWA;
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
	public BigDecimal getR13_EXPOSURE_BEFORE_CRM() {
		return R13_EXPOSURE_BEFORE_CRM;
	}
	public void setR13_EXPOSURE_BEFORE_CRM(BigDecimal r13_EXPOSURE_BEFORE_CRM) {
		R13_EXPOSURE_BEFORE_CRM = r13_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR13_SPEC_PROVISION_PAST_DUE() {
		return R13_SPEC_PROVISION_PAST_DUE;
	}
	public void setR13_SPEC_PROVISION_PAST_DUE(BigDecimal r13_SPEC_PROVISION_PAST_DUE) {
		R13_SPEC_PROVISION_PAST_DUE = r13_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR13_ON_BAL_SHEET_NETTING_ELIG() {
		return R13_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR13_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r13_ON_BAL_SHEET_NETTING_ELIG) {
		R13_ON_BAL_SHEET_NETTING_ELIG = r13_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR13_TOTAL_EXPOSURE_AFTER_NET() {
		return R13_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR13_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r13_TOTAL_EXPOSURE_AFTER_NET) {
		R13_TOTAL_EXPOSURE_AFTER_NET = r13_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR13_CRM_ELIG_EXPOSURE_SUBS() {
		return R13_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR13_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r13_CRM_ELIG_EXPOSURE_SUBS) {
		R13_CRM_ELIG_EXPOSURE_SUBS = r13_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR13_ELIG_GUARANTEES() {
		return R13_ELIG_GUARANTEES;
	}
	public void setR13_ELIG_GUARANTEES(BigDecimal r13_ELIG_GUARANTEES) {
		R13_ELIG_GUARANTEES = r13_ELIG_GUARANTEES;
	}
	public BigDecimal getR13_CREDIT_DERIVATIVES() {
		return R13_CREDIT_DERIVATIVES;
	}
	public void setR13_CREDIT_DERIVATIVES(BigDecimal r13_CREDIT_DERIVATIVES) {
		R13_CREDIT_DERIVATIVES = r13_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR13_CRM_COVERED_EXPOSURE() {
		return R13_CRM_COVERED_EXPOSURE;
	}
	public void setR13_CRM_COVERED_EXPOSURE(BigDecimal r13_CRM_COVERED_EXPOSURE) {
		R13_CRM_COVERED_EXPOSURE = r13_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR13_CRM_NOT_COVERED_EXPOSURE() {
		return R13_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR13_CRM_NOT_COVERED_EXPOSURE(BigDecimal r13_CRM_NOT_COVERED_EXPOSURE) {
		R13_CRM_NOT_COVERED_EXPOSURE = r13_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR13_CRM_RISK_WEIGHT() {
		return R13_CRM_RISK_WEIGHT;
	}
	public void setR13_CRM_RISK_WEIGHT(BigDecimal r13_CRM_RISK_WEIGHT) {
		R13_CRM_RISK_WEIGHT = r13_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR13_RWA_CRM_COVERED() {
		return R13_RWA_CRM_COVERED;
	}
	public void setR13_RWA_CRM_COVERED(BigDecimal r13_RWA_CRM_COVERED) {
		R13_RWA_CRM_COVERED = r13_RWA_CRM_COVERED;
	}
	public BigDecimal getR13_ORIG_COUNTERPARTY_RW() {
		return R13_ORIG_COUNTERPARTY_RW;
	}
	public void setR13_ORIG_COUNTERPARTY_RW(BigDecimal r13_ORIG_COUNTERPARTY_RW) {
		R13_ORIG_COUNTERPARTY_RW = r13_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR13_RWA_CRM_NOT_COVERED() {
		return R13_RWA_CRM_NOT_COVERED;
	}
	public void setR13_RWA_CRM_NOT_COVERED(BigDecimal r13_RWA_CRM_NOT_COVERED) {
		R13_RWA_CRM_NOT_COVERED = r13_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR13_CRM_ELIG_EXPOSURE_COMP() {
		return R13_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR13_CRM_ELIG_EXPOSURE_COMP(BigDecimal r13_CRM_ELIG_EXPOSURE_COMP) {
		R13_CRM_ELIG_EXPOSURE_COMP = r13_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR13_EXPOSURE_AFTER_VOL_ADJ() {
		return R13_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR13_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r13_EXPOSURE_AFTER_VOL_ADJ) {
		R13_EXPOSURE_AFTER_VOL_ADJ = r13_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR13_COLLATERAL_CASH() {
		return R13_COLLATERAL_CASH;
	}
	public void setR13_COLLATERAL_CASH(BigDecimal r13_COLLATERAL_CASH) {
		R13_COLLATERAL_CASH = r13_COLLATERAL_CASH;
	}
	public BigDecimal getR13_COLLATERAL_TBILLS() {
		return R13_COLLATERAL_TBILLS;
	}
	public void setR13_COLLATERAL_TBILLS(BigDecimal r13_COLLATERAL_TBILLS) {
		R13_COLLATERAL_TBILLS = r13_COLLATERAL_TBILLS;
	}
	public BigDecimal getR13_COLLATERAL_DEBT_SEC() {
		return R13_COLLATERAL_DEBT_SEC;
	}
	public void setR13_COLLATERAL_DEBT_SEC(BigDecimal r13_COLLATERAL_DEBT_SEC) {
		R13_COLLATERAL_DEBT_SEC = r13_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR13_COLLATERAL_EQUITIES() {
		return R13_COLLATERAL_EQUITIES;
	}
	public void setR13_COLLATERAL_EQUITIES(BigDecimal r13_COLLATERAL_EQUITIES) {
		R13_COLLATERAL_EQUITIES = r13_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR13_COLLATERAL_MUTUAL_FUNDS() {
		return R13_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR13_COLLATERAL_MUTUAL_FUNDS(BigDecimal r13_COLLATERAL_MUTUAL_FUNDS) {
		R13_COLLATERAL_MUTUAL_FUNDS = r13_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR13_TOTAL_COLLATERAL_HAIRCUT() {
		return R13_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR13_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r13_TOTAL_COLLATERAL_HAIRCUT) {
		R13_TOTAL_COLLATERAL_HAIRCUT = r13_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR13_EXPOSURE_AFTER_CRM() {
		return R13_EXPOSURE_AFTER_CRM;
	}
	public void setR13_EXPOSURE_AFTER_CRM(BigDecimal r13_EXPOSURE_AFTER_CRM) {
		R13_EXPOSURE_AFTER_CRM = r13_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR13_RWA_NOT_COVERED_CRM() {
		return R13_RWA_NOT_COVERED_CRM;
	}
	public void setR13_RWA_NOT_COVERED_CRM(BigDecimal r13_RWA_NOT_COVERED_CRM) {
		R13_RWA_NOT_COVERED_CRM = r13_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR13_RWA_UNSECURED_EXPOSURE() {
		return R13_RWA_UNSECURED_EXPOSURE;
	}
	public void setR13_RWA_UNSECURED_EXPOSURE(BigDecimal r13_RWA_UNSECURED_EXPOSURE) {
		R13_RWA_UNSECURED_EXPOSURE = r13_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR13_RWA_UNSECURED() {
		return R13_RWA_UNSECURED;
	}
	public void setR13_RWA_UNSECURED(BigDecimal r13_RWA_UNSECURED) {
		R13_RWA_UNSECURED = r13_RWA_UNSECURED;
	}
	public BigDecimal getR13_TOTAL_RWA() {
		return R13_TOTAL_RWA;
	}
	public void setR13_TOTAL_RWA(BigDecimal r13_TOTAL_RWA) {
		R13_TOTAL_RWA = r13_TOTAL_RWA;
	}
	public BigDecimal getR14_EXPOSURE_BEFORE_CRM() {
		return R14_EXPOSURE_BEFORE_CRM;
	}
	public void setR14_EXPOSURE_BEFORE_CRM(BigDecimal r14_EXPOSURE_BEFORE_CRM) {
		R14_EXPOSURE_BEFORE_CRM = r14_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR14_SPEC_PROVISION_PAST_DUE() {
		return R14_SPEC_PROVISION_PAST_DUE;
	}
	public void setR14_SPEC_PROVISION_PAST_DUE(BigDecimal r14_SPEC_PROVISION_PAST_DUE) {
		R14_SPEC_PROVISION_PAST_DUE = r14_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR14_ON_BAL_SHEET_NETTING_ELIG() {
		return R14_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR14_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r14_ON_BAL_SHEET_NETTING_ELIG) {
		R14_ON_BAL_SHEET_NETTING_ELIG = r14_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR14_TOTAL_EXPOSURE_AFTER_NET() {
		return R14_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR14_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r14_TOTAL_EXPOSURE_AFTER_NET) {
		R14_TOTAL_EXPOSURE_AFTER_NET = r14_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR14_CRM_ELIG_EXPOSURE_SUBS() {
		return R14_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR14_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r14_CRM_ELIG_EXPOSURE_SUBS) {
		R14_CRM_ELIG_EXPOSURE_SUBS = r14_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR14_ELIG_GUARANTEES() {
		return R14_ELIG_GUARANTEES;
	}
	public void setR14_ELIG_GUARANTEES(BigDecimal r14_ELIG_GUARANTEES) {
		R14_ELIG_GUARANTEES = r14_ELIG_GUARANTEES;
	}
	public BigDecimal getR14_CREDIT_DERIVATIVES() {
		return R14_CREDIT_DERIVATIVES;
	}
	public void setR14_CREDIT_DERIVATIVES(BigDecimal r14_CREDIT_DERIVATIVES) {
		R14_CREDIT_DERIVATIVES = r14_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR14_CRM_COVERED_EXPOSURE() {
		return R14_CRM_COVERED_EXPOSURE;
	}
	public void setR14_CRM_COVERED_EXPOSURE(BigDecimal r14_CRM_COVERED_EXPOSURE) {
		R14_CRM_COVERED_EXPOSURE = r14_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR14_CRM_NOT_COVERED_EXPOSURE() {
		return R14_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR14_CRM_NOT_COVERED_EXPOSURE(BigDecimal r14_CRM_NOT_COVERED_EXPOSURE) {
		R14_CRM_NOT_COVERED_EXPOSURE = r14_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR14_CRM_RISK_WEIGHT() {
		return R14_CRM_RISK_WEIGHT;
	}
	public void setR14_CRM_RISK_WEIGHT(BigDecimal r14_CRM_RISK_WEIGHT) {
		R14_CRM_RISK_WEIGHT = r14_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR14_RWA_CRM_COVERED() {
		return R14_RWA_CRM_COVERED;
	}
	public void setR14_RWA_CRM_COVERED(BigDecimal r14_RWA_CRM_COVERED) {
		R14_RWA_CRM_COVERED = r14_RWA_CRM_COVERED;
	}
	public BigDecimal getR14_ORIG_COUNTERPARTY_RW() {
		return R14_ORIG_COUNTERPARTY_RW;
	}
	public void setR14_ORIG_COUNTERPARTY_RW(BigDecimal r14_ORIG_COUNTERPARTY_RW) {
		R14_ORIG_COUNTERPARTY_RW = r14_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR14_RWA_CRM_NOT_COVERED() {
		return R14_RWA_CRM_NOT_COVERED;
	}
	public void setR14_RWA_CRM_NOT_COVERED(BigDecimal r14_RWA_CRM_NOT_COVERED) {
		R14_RWA_CRM_NOT_COVERED = r14_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR14_CRM_ELIG_EXPOSURE_COMP() {
		return R14_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR14_CRM_ELIG_EXPOSURE_COMP(BigDecimal r14_CRM_ELIG_EXPOSURE_COMP) {
		R14_CRM_ELIG_EXPOSURE_COMP = r14_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR14_EXPOSURE_AFTER_VOL_ADJ() {
		return R14_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR14_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r14_EXPOSURE_AFTER_VOL_ADJ) {
		R14_EXPOSURE_AFTER_VOL_ADJ = r14_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR14_COLLATERAL_CASH() {
		return R14_COLLATERAL_CASH;
	}
	public void setR14_COLLATERAL_CASH(BigDecimal r14_COLLATERAL_CASH) {
		R14_COLLATERAL_CASH = r14_COLLATERAL_CASH;
	}
	public BigDecimal getR14_COLLATERAL_TBILLS() {
		return R14_COLLATERAL_TBILLS;
	}
	public void setR14_COLLATERAL_TBILLS(BigDecimal r14_COLLATERAL_TBILLS) {
		R14_COLLATERAL_TBILLS = r14_COLLATERAL_TBILLS;
	}
	public BigDecimal getR14_COLLATERAL_DEBT_SEC() {
		return R14_COLLATERAL_DEBT_SEC;
	}
	public void setR14_COLLATERAL_DEBT_SEC(BigDecimal r14_COLLATERAL_DEBT_SEC) {
		R14_COLLATERAL_DEBT_SEC = r14_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR14_COLLATERAL_EQUITIES() {
		return R14_COLLATERAL_EQUITIES;
	}
	public void setR14_COLLATERAL_EQUITIES(BigDecimal r14_COLLATERAL_EQUITIES) {
		R14_COLLATERAL_EQUITIES = r14_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR14_COLLATERAL_MUTUAL_FUNDS() {
		return R14_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR14_COLLATERAL_MUTUAL_FUNDS(BigDecimal r14_COLLATERAL_MUTUAL_FUNDS) {
		R14_COLLATERAL_MUTUAL_FUNDS = r14_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR14_TOTAL_COLLATERAL_HAIRCUT() {
		return R14_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR14_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r14_TOTAL_COLLATERAL_HAIRCUT) {
		R14_TOTAL_COLLATERAL_HAIRCUT = r14_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR14_EXPOSURE_AFTER_CRM() {
		return R14_EXPOSURE_AFTER_CRM;
	}
	public void setR14_EXPOSURE_AFTER_CRM(BigDecimal r14_EXPOSURE_AFTER_CRM) {
		R14_EXPOSURE_AFTER_CRM = r14_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR14_RWA_NOT_COVERED_CRM() {
		return R14_RWA_NOT_COVERED_CRM;
	}
	public void setR14_RWA_NOT_COVERED_CRM(BigDecimal r14_RWA_NOT_COVERED_CRM) {
		R14_RWA_NOT_COVERED_CRM = r14_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR14_RWA_UNSECURED_EXPOSURE() {
		return R14_RWA_UNSECURED_EXPOSURE;
	}
	public void setR14_RWA_UNSECURED_EXPOSURE(BigDecimal r14_RWA_UNSECURED_EXPOSURE) {
		R14_RWA_UNSECURED_EXPOSURE = r14_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR14_RWA_UNSECURED() {
		return R14_RWA_UNSECURED;
	}
	public void setR14_RWA_UNSECURED(BigDecimal r14_RWA_UNSECURED) {
		R14_RWA_UNSECURED = r14_RWA_UNSECURED;
	}
	public BigDecimal getR14_TOTAL_RWA() {
		return R14_TOTAL_RWA;
	}
	public void setR14_TOTAL_RWA(BigDecimal r14_TOTAL_RWA) {
		R14_TOTAL_RWA = r14_TOTAL_RWA;
	}
	public BigDecimal getR15_EXPOSURE_BEFORE_CRM() {
		return R15_EXPOSURE_BEFORE_CRM;
	}
	public void setR15_EXPOSURE_BEFORE_CRM(BigDecimal r15_EXPOSURE_BEFORE_CRM) {
		R15_EXPOSURE_BEFORE_CRM = r15_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR15_SPEC_PROVISION_PAST_DUE() {
		return R15_SPEC_PROVISION_PAST_DUE;
	}
	public void setR15_SPEC_PROVISION_PAST_DUE(BigDecimal r15_SPEC_PROVISION_PAST_DUE) {
		R15_SPEC_PROVISION_PAST_DUE = r15_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR15_ON_BAL_SHEET_NETTING_ELIG() {
		return R15_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR15_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r15_ON_BAL_SHEET_NETTING_ELIG) {
		R15_ON_BAL_SHEET_NETTING_ELIG = r15_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR15_TOTAL_EXPOSURE_AFTER_NET() {
		return R15_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR15_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r15_TOTAL_EXPOSURE_AFTER_NET) {
		R15_TOTAL_EXPOSURE_AFTER_NET = r15_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR15_CRM_ELIG_EXPOSURE_SUBS() {
		return R15_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR15_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r15_CRM_ELIG_EXPOSURE_SUBS) {
		R15_CRM_ELIG_EXPOSURE_SUBS = r15_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR15_ELIG_GUARANTEES() {
		return R15_ELIG_GUARANTEES;
	}
	public void setR15_ELIG_GUARANTEES(BigDecimal r15_ELIG_GUARANTEES) {
		R15_ELIG_GUARANTEES = r15_ELIG_GUARANTEES;
	}
	public BigDecimal getR15_CREDIT_DERIVATIVES() {
		return R15_CREDIT_DERIVATIVES;
	}
	public void setR15_CREDIT_DERIVATIVES(BigDecimal r15_CREDIT_DERIVATIVES) {
		R15_CREDIT_DERIVATIVES = r15_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR15_CRM_COVERED_EXPOSURE() {
		return R15_CRM_COVERED_EXPOSURE;
	}
	public void setR15_CRM_COVERED_EXPOSURE(BigDecimal r15_CRM_COVERED_EXPOSURE) {
		R15_CRM_COVERED_EXPOSURE = r15_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR15_CRM_NOT_COVERED_EXPOSURE() {
		return R15_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR15_CRM_NOT_COVERED_EXPOSURE(BigDecimal r15_CRM_NOT_COVERED_EXPOSURE) {
		R15_CRM_NOT_COVERED_EXPOSURE = r15_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR15_CRM_RISK_WEIGHT() {
		return R15_CRM_RISK_WEIGHT;
	}
	public void setR15_CRM_RISK_WEIGHT(BigDecimal r15_CRM_RISK_WEIGHT) {
		R15_CRM_RISK_WEIGHT = r15_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR15_RWA_CRM_COVERED() {
		return R15_RWA_CRM_COVERED;
	}
	public void setR15_RWA_CRM_COVERED(BigDecimal r15_RWA_CRM_COVERED) {
		R15_RWA_CRM_COVERED = r15_RWA_CRM_COVERED;
	}
	public BigDecimal getR15_ORIG_COUNTERPARTY_RW() {
		return R15_ORIG_COUNTERPARTY_RW;
	}
	public void setR15_ORIG_COUNTERPARTY_RW(BigDecimal r15_ORIG_COUNTERPARTY_RW) {
		R15_ORIG_COUNTERPARTY_RW = r15_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR15_RWA_CRM_NOT_COVERED() {
		return R15_RWA_CRM_NOT_COVERED;
	}
	public void setR15_RWA_CRM_NOT_COVERED(BigDecimal r15_RWA_CRM_NOT_COVERED) {
		R15_RWA_CRM_NOT_COVERED = r15_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR15_CRM_ELIG_EXPOSURE_COMP() {
		return R15_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR15_CRM_ELIG_EXPOSURE_COMP(BigDecimal r15_CRM_ELIG_EXPOSURE_COMP) {
		R15_CRM_ELIG_EXPOSURE_COMP = r15_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR15_EXPOSURE_AFTER_VOL_ADJ() {
		return R15_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR15_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r15_EXPOSURE_AFTER_VOL_ADJ) {
		R15_EXPOSURE_AFTER_VOL_ADJ = r15_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR15_COLLATERAL_CASH() {
		return R15_COLLATERAL_CASH;
	}
	public void setR15_COLLATERAL_CASH(BigDecimal r15_COLLATERAL_CASH) {
		R15_COLLATERAL_CASH = r15_COLLATERAL_CASH;
	}
	public BigDecimal getR15_COLLATERAL_TBILLS() {
		return R15_COLLATERAL_TBILLS;
	}
	public void setR15_COLLATERAL_TBILLS(BigDecimal r15_COLLATERAL_TBILLS) {
		R15_COLLATERAL_TBILLS = r15_COLLATERAL_TBILLS;
	}
	public BigDecimal getR15_COLLATERAL_DEBT_SEC() {
		return R15_COLLATERAL_DEBT_SEC;
	}
	public void setR15_COLLATERAL_DEBT_SEC(BigDecimal r15_COLLATERAL_DEBT_SEC) {
		R15_COLLATERAL_DEBT_SEC = r15_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR15_COLLATERAL_EQUITIES() {
		return R15_COLLATERAL_EQUITIES;
	}
	public void setR15_COLLATERAL_EQUITIES(BigDecimal r15_COLLATERAL_EQUITIES) {
		R15_COLLATERAL_EQUITIES = r15_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR15_COLLATERAL_MUTUAL_FUNDS() {
		return R15_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR15_COLLATERAL_MUTUAL_FUNDS(BigDecimal r15_COLLATERAL_MUTUAL_FUNDS) {
		R15_COLLATERAL_MUTUAL_FUNDS = r15_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR15_TOTAL_COLLATERAL_HAIRCUT() {
		return R15_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR15_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r15_TOTAL_COLLATERAL_HAIRCUT) {
		R15_TOTAL_COLLATERAL_HAIRCUT = r15_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR15_EXPOSURE_AFTER_CRM() {
		return R15_EXPOSURE_AFTER_CRM;
	}
	public void setR15_EXPOSURE_AFTER_CRM(BigDecimal r15_EXPOSURE_AFTER_CRM) {
		R15_EXPOSURE_AFTER_CRM = r15_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR15_RWA_NOT_COVERED_CRM() {
		return R15_RWA_NOT_COVERED_CRM;
	}
	public void setR15_RWA_NOT_COVERED_CRM(BigDecimal r15_RWA_NOT_COVERED_CRM) {
		R15_RWA_NOT_COVERED_CRM = r15_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR15_RWA_UNSECURED_EXPOSURE() {
		return R15_RWA_UNSECURED_EXPOSURE;
	}
	public void setR15_RWA_UNSECURED_EXPOSURE(BigDecimal r15_RWA_UNSECURED_EXPOSURE) {
		R15_RWA_UNSECURED_EXPOSURE = r15_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR15_RWA_UNSECURED() {
		return R15_RWA_UNSECURED;
	}
	public void setR15_RWA_UNSECURED(BigDecimal r15_RWA_UNSECURED) {
		R15_RWA_UNSECURED = r15_RWA_UNSECURED;
	}
	public BigDecimal getR15_TOTAL_RWA() {
		return R15_TOTAL_RWA;
	}
	public void setR15_TOTAL_RWA(BigDecimal r15_TOTAL_RWA) {
		R15_TOTAL_RWA = r15_TOTAL_RWA;
	}
	public BigDecimal getR16_EXPOSURE_BEFORE_CRM() {
		return R16_EXPOSURE_BEFORE_CRM;
	}
	public void setR16_EXPOSURE_BEFORE_CRM(BigDecimal r16_EXPOSURE_BEFORE_CRM) {
		R16_EXPOSURE_BEFORE_CRM = r16_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR16_SPEC_PROVISION_PAST_DUE() {
		return R16_SPEC_PROVISION_PAST_DUE;
	}
	public void setR16_SPEC_PROVISION_PAST_DUE(BigDecimal r16_SPEC_PROVISION_PAST_DUE) {
		R16_SPEC_PROVISION_PAST_DUE = r16_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR16_ON_BAL_SHEET_NETTING_ELIG() {
		return R16_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR16_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r16_ON_BAL_SHEET_NETTING_ELIG) {
		R16_ON_BAL_SHEET_NETTING_ELIG = r16_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR16_TOTAL_EXPOSURE_AFTER_NET() {
		return R16_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR16_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r16_TOTAL_EXPOSURE_AFTER_NET) {
		R16_TOTAL_EXPOSURE_AFTER_NET = r16_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR16_CRM_ELIG_EXPOSURE_SUBS() {
		return R16_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR16_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r16_CRM_ELIG_EXPOSURE_SUBS) {
		R16_CRM_ELIG_EXPOSURE_SUBS = r16_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR16_ELIG_GUARANTEES() {
		return R16_ELIG_GUARANTEES;
	}
	public void setR16_ELIG_GUARANTEES(BigDecimal r16_ELIG_GUARANTEES) {
		R16_ELIG_GUARANTEES = r16_ELIG_GUARANTEES;
	}
	public BigDecimal getR16_CREDIT_DERIVATIVES() {
		return R16_CREDIT_DERIVATIVES;
	}
	public void setR16_CREDIT_DERIVATIVES(BigDecimal r16_CREDIT_DERIVATIVES) {
		R16_CREDIT_DERIVATIVES = r16_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR16_CRM_COVERED_EXPOSURE() {
		return R16_CRM_COVERED_EXPOSURE;
	}
	public void setR16_CRM_COVERED_EXPOSURE(BigDecimal r16_CRM_COVERED_EXPOSURE) {
		R16_CRM_COVERED_EXPOSURE = r16_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR16_CRM_NOT_COVERED_EXPOSURE() {
		return R16_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR16_CRM_NOT_COVERED_EXPOSURE(BigDecimal r16_CRM_NOT_COVERED_EXPOSURE) {
		R16_CRM_NOT_COVERED_EXPOSURE = r16_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR16_CRM_RISK_WEIGHT() {
		return R16_CRM_RISK_WEIGHT;
	}
	public void setR16_CRM_RISK_WEIGHT(BigDecimal r16_CRM_RISK_WEIGHT) {
		R16_CRM_RISK_WEIGHT = r16_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR16_RWA_CRM_COVERED() {
		return R16_RWA_CRM_COVERED;
	}
	public void setR16_RWA_CRM_COVERED(BigDecimal r16_RWA_CRM_COVERED) {
		R16_RWA_CRM_COVERED = r16_RWA_CRM_COVERED;
	}
	public BigDecimal getR16_ORIG_COUNTERPARTY_RW() {
		return R16_ORIG_COUNTERPARTY_RW;
	}
	public void setR16_ORIG_COUNTERPARTY_RW(BigDecimal r16_ORIG_COUNTERPARTY_RW) {
		R16_ORIG_COUNTERPARTY_RW = r16_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR16_RWA_CRM_NOT_COVERED() {
		return R16_RWA_CRM_NOT_COVERED;
	}
	public void setR16_RWA_CRM_NOT_COVERED(BigDecimal r16_RWA_CRM_NOT_COVERED) {
		R16_RWA_CRM_NOT_COVERED = r16_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR16_CRM_ELIG_EXPOSURE_COMP() {
		return R16_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR16_CRM_ELIG_EXPOSURE_COMP(BigDecimal r16_CRM_ELIG_EXPOSURE_COMP) {
		R16_CRM_ELIG_EXPOSURE_COMP = r16_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR16_EXPOSURE_AFTER_VOL_ADJ() {
		return R16_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR16_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r16_EXPOSURE_AFTER_VOL_ADJ) {
		R16_EXPOSURE_AFTER_VOL_ADJ = r16_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR16_COLLATERAL_CASH() {
		return R16_COLLATERAL_CASH;
	}
	public void setR16_COLLATERAL_CASH(BigDecimal r16_COLLATERAL_CASH) {
		R16_COLLATERAL_CASH = r16_COLLATERAL_CASH;
	}
	public BigDecimal getR16_COLLATERAL_TBILLS() {
		return R16_COLLATERAL_TBILLS;
	}
	public void setR16_COLLATERAL_TBILLS(BigDecimal r16_COLLATERAL_TBILLS) {
		R16_COLLATERAL_TBILLS = r16_COLLATERAL_TBILLS;
	}
	public BigDecimal getR16_COLLATERAL_DEBT_SEC() {
		return R16_COLLATERAL_DEBT_SEC;
	}
	public void setR16_COLLATERAL_DEBT_SEC(BigDecimal r16_COLLATERAL_DEBT_SEC) {
		R16_COLLATERAL_DEBT_SEC = r16_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR16_COLLATERAL_EQUITIES() {
		return R16_COLLATERAL_EQUITIES;
	}
	public void setR16_COLLATERAL_EQUITIES(BigDecimal r16_COLLATERAL_EQUITIES) {
		R16_COLLATERAL_EQUITIES = r16_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR16_COLLATERAL_MUTUAL_FUNDS() {
		return R16_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR16_COLLATERAL_MUTUAL_FUNDS(BigDecimal r16_COLLATERAL_MUTUAL_FUNDS) {
		R16_COLLATERAL_MUTUAL_FUNDS = r16_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR16_TOTAL_COLLATERAL_HAIRCUT() {
		return R16_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR16_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r16_TOTAL_COLLATERAL_HAIRCUT) {
		R16_TOTAL_COLLATERAL_HAIRCUT = r16_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR16_EXPOSURE_AFTER_CRM() {
		return R16_EXPOSURE_AFTER_CRM;
	}
	public void setR16_EXPOSURE_AFTER_CRM(BigDecimal r16_EXPOSURE_AFTER_CRM) {
		R16_EXPOSURE_AFTER_CRM = r16_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR16_RWA_NOT_COVERED_CRM() {
		return R16_RWA_NOT_COVERED_CRM;
	}
	public void setR16_RWA_NOT_COVERED_CRM(BigDecimal r16_RWA_NOT_COVERED_CRM) {
		R16_RWA_NOT_COVERED_CRM = r16_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR16_RWA_UNSECURED_EXPOSURE() {
		return R16_RWA_UNSECURED_EXPOSURE;
	}
	public void setR16_RWA_UNSECURED_EXPOSURE(BigDecimal r16_RWA_UNSECURED_EXPOSURE) {
		R16_RWA_UNSECURED_EXPOSURE = r16_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR16_RWA_UNSECURED() {
		return R16_RWA_UNSECURED;
	}
	public void setR16_RWA_UNSECURED(BigDecimal r16_RWA_UNSECURED) {
		R16_RWA_UNSECURED = r16_RWA_UNSECURED;
	}
	public BigDecimal getR16_TOTAL_RWA() {
		return R16_TOTAL_RWA;
	}
	public void setR16_TOTAL_RWA(BigDecimal r16_TOTAL_RWA) {
		R16_TOTAL_RWA = r16_TOTAL_RWA;
	}
	public BigDecimal getR17_EXPOSURE_BEFORE_CRM() {
		return R17_EXPOSURE_BEFORE_CRM;
	}
	public void setR17_EXPOSURE_BEFORE_CRM(BigDecimal r17_EXPOSURE_BEFORE_CRM) {
		R17_EXPOSURE_BEFORE_CRM = r17_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR17_SPEC_PROVISION_PAST_DUE() {
		return R17_SPEC_PROVISION_PAST_DUE;
	}
	public void setR17_SPEC_PROVISION_PAST_DUE(BigDecimal r17_SPEC_PROVISION_PAST_DUE) {
		R17_SPEC_PROVISION_PAST_DUE = r17_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR17_ON_BAL_SHEET_NETTING_ELIG() {
		return R17_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR17_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r17_ON_BAL_SHEET_NETTING_ELIG) {
		R17_ON_BAL_SHEET_NETTING_ELIG = r17_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR17_TOTAL_EXPOSURE_AFTER_NET() {
		return R17_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR17_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r17_TOTAL_EXPOSURE_AFTER_NET) {
		R17_TOTAL_EXPOSURE_AFTER_NET = r17_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR17_CRM_ELIG_EXPOSURE_SUBS() {
		return R17_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR17_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r17_CRM_ELIG_EXPOSURE_SUBS) {
		R17_CRM_ELIG_EXPOSURE_SUBS = r17_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR17_ELIG_GUARANTEES() {
		return R17_ELIG_GUARANTEES;
	}
	public void setR17_ELIG_GUARANTEES(BigDecimal r17_ELIG_GUARANTEES) {
		R17_ELIG_GUARANTEES = r17_ELIG_GUARANTEES;
	}
	public BigDecimal getR17_CREDIT_DERIVATIVES() {
		return R17_CREDIT_DERIVATIVES;
	}
	public void setR17_CREDIT_DERIVATIVES(BigDecimal r17_CREDIT_DERIVATIVES) {
		R17_CREDIT_DERIVATIVES = r17_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR17_CRM_COVERED_EXPOSURE() {
		return R17_CRM_COVERED_EXPOSURE;
	}
	public void setR17_CRM_COVERED_EXPOSURE(BigDecimal r17_CRM_COVERED_EXPOSURE) {
		R17_CRM_COVERED_EXPOSURE = r17_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR17_CRM_NOT_COVERED_EXPOSURE() {
		return R17_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR17_CRM_NOT_COVERED_EXPOSURE(BigDecimal r17_CRM_NOT_COVERED_EXPOSURE) {
		R17_CRM_NOT_COVERED_EXPOSURE = r17_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR17_CRM_RISK_WEIGHT() {
		return R17_CRM_RISK_WEIGHT;
	}
	public void setR17_CRM_RISK_WEIGHT(BigDecimal r17_CRM_RISK_WEIGHT) {
		R17_CRM_RISK_WEIGHT = r17_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR17_RWA_CRM_COVERED() {
		return R17_RWA_CRM_COVERED;
	}
	public void setR17_RWA_CRM_COVERED(BigDecimal r17_RWA_CRM_COVERED) {
		R17_RWA_CRM_COVERED = r17_RWA_CRM_COVERED;
	}
	public BigDecimal getR17_ORIG_COUNTERPARTY_RW() {
		return R17_ORIG_COUNTERPARTY_RW;
	}
	public void setR17_ORIG_COUNTERPARTY_RW(BigDecimal r17_ORIG_COUNTERPARTY_RW) {
		R17_ORIG_COUNTERPARTY_RW = r17_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR17_RWA_CRM_NOT_COVERED() {
		return R17_RWA_CRM_NOT_COVERED;
	}
	public void setR17_RWA_CRM_NOT_COVERED(BigDecimal r17_RWA_CRM_NOT_COVERED) {
		R17_RWA_CRM_NOT_COVERED = r17_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR17_CRM_ELIG_EXPOSURE_COMP() {
		return R17_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR17_CRM_ELIG_EXPOSURE_COMP(BigDecimal r17_CRM_ELIG_EXPOSURE_COMP) {
		R17_CRM_ELIG_EXPOSURE_COMP = r17_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR17_EXPOSURE_AFTER_VOL_ADJ() {
		return R17_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR17_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r17_EXPOSURE_AFTER_VOL_ADJ) {
		R17_EXPOSURE_AFTER_VOL_ADJ = r17_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR17_COLLATERAL_CASH() {
		return R17_COLLATERAL_CASH;
	}
	public void setR17_COLLATERAL_CASH(BigDecimal r17_COLLATERAL_CASH) {
		R17_COLLATERAL_CASH = r17_COLLATERAL_CASH;
	}
	public BigDecimal getR17_COLLATERAL_TBILLS() {
		return R17_COLLATERAL_TBILLS;
	}
	public void setR17_COLLATERAL_TBILLS(BigDecimal r17_COLLATERAL_TBILLS) {
		R17_COLLATERAL_TBILLS = r17_COLLATERAL_TBILLS;
	}
	public BigDecimal getR17_COLLATERAL_DEBT_SEC() {
		return R17_COLLATERAL_DEBT_SEC;
	}
	public void setR17_COLLATERAL_DEBT_SEC(BigDecimal r17_COLLATERAL_DEBT_SEC) {
		R17_COLLATERAL_DEBT_SEC = r17_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR17_COLLATERAL_EQUITIES() {
		return R17_COLLATERAL_EQUITIES;
	}
	public void setR17_COLLATERAL_EQUITIES(BigDecimal r17_COLLATERAL_EQUITIES) {
		R17_COLLATERAL_EQUITIES = r17_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR17_COLLATERAL_MUTUAL_FUNDS() {
		return R17_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR17_COLLATERAL_MUTUAL_FUNDS(BigDecimal r17_COLLATERAL_MUTUAL_FUNDS) {
		R17_COLLATERAL_MUTUAL_FUNDS = r17_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR17_TOTAL_COLLATERAL_HAIRCUT() {
		return R17_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR17_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r17_TOTAL_COLLATERAL_HAIRCUT) {
		R17_TOTAL_COLLATERAL_HAIRCUT = r17_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR17_EXPOSURE_AFTER_CRM() {
		return R17_EXPOSURE_AFTER_CRM;
	}
	public void setR17_EXPOSURE_AFTER_CRM(BigDecimal r17_EXPOSURE_AFTER_CRM) {
		R17_EXPOSURE_AFTER_CRM = r17_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR17_RWA_NOT_COVERED_CRM() {
		return R17_RWA_NOT_COVERED_CRM;
	}
	public void setR17_RWA_NOT_COVERED_CRM(BigDecimal r17_RWA_NOT_COVERED_CRM) {
		R17_RWA_NOT_COVERED_CRM = r17_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR17_RWA_UNSECURED_EXPOSURE() {
		return R17_RWA_UNSECURED_EXPOSURE;
	}
	public void setR17_RWA_UNSECURED_EXPOSURE(BigDecimal r17_RWA_UNSECURED_EXPOSURE) {
		R17_RWA_UNSECURED_EXPOSURE = r17_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR17_RWA_UNSECURED() {
		return R17_RWA_UNSECURED;
	}
	public void setR17_RWA_UNSECURED(BigDecimal r17_RWA_UNSECURED) {
		R17_RWA_UNSECURED = r17_RWA_UNSECURED;
	}
	public BigDecimal getR17_TOTAL_RWA() {
		return R17_TOTAL_RWA;
	}
	public void setR17_TOTAL_RWA(BigDecimal r17_TOTAL_RWA) {
		R17_TOTAL_RWA = r17_TOTAL_RWA;
	}
	public BigDecimal getR18_EXPOSURE_BEFORE_CRM() {
		return R18_EXPOSURE_BEFORE_CRM;
	}
	public void setR18_EXPOSURE_BEFORE_CRM(BigDecimal r18_EXPOSURE_BEFORE_CRM) {
		R18_EXPOSURE_BEFORE_CRM = r18_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR18_SPEC_PROVISION_PAST_DUE() {
		return R18_SPEC_PROVISION_PAST_DUE;
	}
	public void setR18_SPEC_PROVISION_PAST_DUE(BigDecimal r18_SPEC_PROVISION_PAST_DUE) {
		R18_SPEC_PROVISION_PAST_DUE = r18_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR18_ON_BAL_SHEET_NETTING_ELIG() {
		return R18_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR18_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r18_ON_BAL_SHEET_NETTING_ELIG) {
		R18_ON_BAL_SHEET_NETTING_ELIG = r18_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR18_TOTAL_EXPOSURE_AFTER_NET() {
		return R18_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR18_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r18_TOTAL_EXPOSURE_AFTER_NET) {
		R18_TOTAL_EXPOSURE_AFTER_NET = r18_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR18_CRM_ELIG_EXPOSURE_SUBS() {
		return R18_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR18_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r18_CRM_ELIG_EXPOSURE_SUBS) {
		R18_CRM_ELIG_EXPOSURE_SUBS = r18_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR18_ELIG_GUARANTEES() {
		return R18_ELIG_GUARANTEES;
	}
	public void setR18_ELIG_GUARANTEES(BigDecimal r18_ELIG_GUARANTEES) {
		R18_ELIG_GUARANTEES = r18_ELIG_GUARANTEES;
	}
	public BigDecimal getR18_CREDIT_DERIVATIVES() {
		return R18_CREDIT_DERIVATIVES;
	}
	public void setR18_CREDIT_DERIVATIVES(BigDecimal r18_CREDIT_DERIVATIVES) {
		R18_CREDIT_DERIVATIVES = r18_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR18_CRM_COVERED_EXPOSURE() {
		return R18_CRM_COVERED_EXPOSURE;
	}
	public void setR18_CRM_COVERED_EXPOSURE(BigDecimal r18_CRM_COVERED_EXPOSURE) {
		R18_CRM_COVERED_EXPOSURE = r18_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR18_CRM_NOT_COVERED_EXPOSURE() {
		return R18_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR18_CRM_NOT_COVERED_EXPOSURE(BigDecimal r18_CRM_NOT_COVERED_EXPOSURE) {
		R18_CRM_NOT_COVERED_EXPOSURE = r18_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR18_CRM_RISK_WEIGHT() {
		return R18_CRM_RISK_WEIGHT;
	}
	public void setR18_CRM_RISK_WEIGHT(BigDecimal r18_CRM_RISK_WEIGHT) {
		R18_CRM_RISK_WEIGHT = r18_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR18_RWA_CRM_COVERED() {
		return R18_RWA_CRM_COVERED;
	}
	public void setR18_RWA_CRM_COVERED(BigDecimal r18_RWA_CRM_COVERED) {
		R18_RWA_CRM_COVERED = r18_RWA_CRM_COVERED;
	}
	public BigDecimal getR18_ORIG_COUNTERPARTY_RW() {
		return R18_ORIG_COUNTERPARTY_RW;
	}
	public void setR18_ORIG_COUNTERPARTY_RW(BigDecimal r18_ORIG_COUNTERPARTY_RW) {
		R18_ORIG_COUNTERPARTY_RW = r18_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR18_RWA_CRM_NOT_COVERED() {
		return R18_RWA_CRM_NOT_COVERED;
	}
	public void setR18_RWA_CRM_NOT_COVERED(BigDecimal r18_RWA_CRM_NOT_COVERED) {
		R18_RWA_CRM_NOT_COVERED = r18_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR18_CRM_ELIG_EXPOSURE_COMP() {
		return R18_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR18_CRM_ELIG_EXPOSURE_COMP(BigDecimal r18_CRM_ELIG_EXPOSURE_COMP) {
		R18_CRM_ELIG_EXPOSURE_COMP = r18_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR18_EXPOSURE_AFTER_VOL_ADJ() {
		return R18_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR18_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r18_EXPOSURE_AFTER_VOL_ADJ) {
		R18_EXPOSURE_AFTER_VOL_ADJ = r18_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR18_COLLATERAL_CASH() {
		return R18_COLLATERAL_CASH;
	}
	public void setR18_COLLATERAL_CASH(BigDecimal r18_COLLATERAL_CASH) {
		R18_COLLATERAL_CASH = r18_COLLATERAL_CASH;
	}
	public BigDecimal getR18_COLLATERAL_TBILLS() {
		return R18_COLLATERAL_TBILLS;
	}
	public void setR18_COLLATERAL_TBILLS(BigDecimal r18_COLLATERAL_TBILLS) {
		R18_COLLATERAL_TBILLS = r18_COLLATERAL_TBILLS;
	}
	public BigDecimal getR18_COLLATERAL_DEBT_SEC() {
		return R18_COLLATERAL_DEBT_SEC;
	}
	public void setR18_COLLATERAL_DEBT_SEC(BigDecimal r18_COLLATERAL_DEBT_SEC) {
		R18_COLLATERAL_DEBT_SEC = r18_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR18_COLLATERAL_EQUITIES() {
		return R18_COLLATERAL_EQUITIES;
	}
	public void setR18_COLLATERAL_EQUITIES(BigDecimal r18_COLLATERAL_EQUITIES) {
		R18_COLLATERAL_EQUITIES = r18_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR18_COLLATERAL_MUTUAL_FUNDS() {
		return R18_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR18_COLLATERAL_MUTUAL_FUNDS(BigDecimal r18_COLLATERAL_MUTUAL_FUNDS) {
		R18_COLLATERAL_MUTUAL_FUNDS = r18_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR18_TOTAL_COLLATERAL_HAIRCUT() {
		return R18_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR18_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r18_TOTAL_COLLATERAL_HAIRCUT) {
		R18_TOTAL_COLLATERAL_HAIRCUT = r18_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR18_EXPOSURE_AFTER_CRM() {
		return R18_EXPOSURE_AFTER_CRM;
	}
	public void setR18_EXPOSURE_AFTER_CRM(BigDecimal r18_EXPOSURE_AFTER_CRM) {
		R18_EXPOSURE_AFTER_CRM = r18_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR18_RWA_NOT_COVERED_CRM() {
		return R18_RWA_NOT_COVERED_CRM;
	}
	public void setR18_RWA_NOT_COVERED_CRM(BigDecimal r18_RWA_NOT_COVERED_CRM) {
		R18_RWA_NOT_COVERED_CRM = r18_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR18_RWA_UNSECURED_EXPOSURE() {
		return R18_RWA_UNSECURED_EXPOSURE;
	}
	public void setR18_RWA_UNSECURED_EXPOSURE(BigDecimal r18_RWA_UNSECURED_EXPOSURE) {
		R18_RWA_UNSECURED_EXPOSURE = r18_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR18_RWA_UNSECURED() {
		return R18_RWA_UNSECURED;
	}
	public void setR18_RWA_UNSECURED(BigDecimal r18_RWA_UNSECURED) {
		R18_RWA_UNSECURED = r18_RWA_UNSECURED;
	}
	public BigDecimal getR18_TOTAL_RWA() {
		return R18_TOTAL_RWA;
	}
	public void setR18_TOTAL_RWA(BigDecimal r18_TOTAL_RWA) {
		R18_TOTAL_RWA = r18_TOTAL_RWA;
	}
	public BigDecimal getR19_EXPOSURE_BEFORE_CRM() {
		return R19_EXPOSURE_BEFORE_CRM;
	}
	public void setR19_EXPOSURE_BEFORE_CRM(BigDecimal r19_EXPOSURE_BEFORE_CRM) {
		R19_EXPOSURE_BEFORE_CRM = r19_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR19_SPEC_PROVISION_PAST_DUE() {
		return R19_SPEC_PROVISION_PAST_DUE;
	}
	public void setR19_SPEC_PROVISION_PAST_DUE(BigDecimal r19_SPEC_PROVISION_PAST_DUE) {
		R19_SPEC_PROVISION_PAST_DUE = r19_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR19_ON_BAL_SHEET_NETTING_ELIG() {
		return R19_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR19_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r19_ON_BAL_SHEET_NETTING_ELIG) {
		R19_ON_BAL_SHEET_NETTING_ELIG = r19_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR19_TOTAL_EXPOSURE_AFTER_NET() {
		return R19_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR19_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r19_TOTAL_EXPOSURE_AFTER_NET) {
		R19_TOTAL_EXPOSURE_AFTER_NET = r19_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR19_CRM_ELIG_EXPOSURE_SUBS() {
		return R19_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR19_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r19_CRM_ELIG_EXPOSURE_SUBS) {
		R19_CRM_ELIG_EXPOSURE_SUBS = r19_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR19_ELIG_GUARANTEES() {
		return R19_ELIG_GUARANTEES;
	}
	public void setR19_ELIG_GUARANTEES(BigDecimal r19_ELIG_GUARANTEES) {
		R19_ELIG_GUARANTEES = r19_ELIG_GUARANTEES;
	}
	public BigDecimal getR19_CREDIT_DERIVATIVES() {
		return R19_CREDIT_DERIVATIVES;
	}
	public void setR19_CREDIT_DERIVATIVES(BigDecimal r19_CREDIT_DERIVATIVES) {
		R19_CREDIT_DERIVATIVES = r19_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR19_CRM_COVERED_EXPOSURE() {
		return R19_CRM_COVERED_EXPOSURE;
	}
	public void setR19_CRM_COVERED_EXPOSURE(BigDecimal r19_CRM_COVERED_EXPOSURE) {
		R19_CRM_COVERED_EXPOSURE = r19_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR19_CRM_NOT_COVERED_EXPOSURE() {
		return R19_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR19_CRM_NOT_COVERED_EXPOSURE(BigDecimal r19_CRM_NOT_COVERED_EXPOSURE) {
		R19_CRM_NOT_COVERED_EXPOSURE = r19_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR19_CRM_RISK_WEIGHT() {
		return R19_CRM_RISK_WEIGHT;
	}
	public void setR19_CRM_RISK_WEIGHT(BigDecimal r19_CRM_RISK_WEIGHT) {
		R19_CRM_RISK_WEIGHT = r19_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR19_RWA_CRM_COVERED() {
		return R19_RWA_CRM_COVERED;
	}
	public void setR19_RWA_CRM_COVERED(BigDecimal r19_RWA_CRM_COVERED) {
		R19_RWA_CRM_COVERED = r19_RWA_CRM_COVERED;
	}
	public BigDecimal getR19_ORIG_COUNTERPARTY_RW() {
		return R19_ORIG_COUNTERPARTY_RW;
	}
	public void setR19_ORIG_COUNTERPARTY_RW(BigDecimal r19_ORIG_COUNTERPARTY_RW) {
		R19_ORIG_COUNTERPARTY_RW = r19_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR19_RWA_CRM_NOT_COVERED() {
		return R19_RWA_CRM_NOT_COVERED;
	}
	public void setR19_RWA_CRM_NOT_COVERED(BigDecimal r19_RWA_CRM_NOT_COVERED) {
		R19_RWA_CRM_NOT_COVERED = r19_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR19_CRM_ELIG_EXPOSURE_COMP() {
		return R19_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR19_CRM_ELIG_EXPOSURE_COMP(BigDecimal r19_CRM_ELIG_EXPOSURE_COMP) {
		R19_CRM_ELIG_EXPOSURE_COMP = r19_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR19_EXPOSURE_AFTER_VOL_ADJ() {
		return R19_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR19_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r19_EXPOSURE_AFTER_VOL_ADJ) {
		R19_EXPOSURE_AFTER_VOL_ADJ = r19_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR19_COLLATERAL_CASH() {
		return R19_COLLATERAL_CASH;
	}
	public void setR19_COLLATERAL_CASH(BigDecimal r19_COLLATERAL_CASH) {
		R19_COLLATERAL_CASH = r19_COLLATERAL_CASH;
	}
	public BigDecimal getR19_COLLATERAL_TBILLS() {
		return R19_COLLATERAL_TBILLS;
	}
	public void setR19_COLLATERAL_TBILLS(BigDecimal r19_COLLATERAL_TBILLS) {
		R19_COLLATERAL_TBILLS = r19_COLLATERAL_TBILLS;
	}
	public BigDecimal getR19_COLLATERAL_DEBT_SEC() {
		return R19_COLLATERAL_DEBT_SEC;
	}
	public void setR19_COLLATERAL_DEBT_SEC(BigDecimal r19_COLLATERAL_DEBT_SEC) {
		R19_COLLATERAL_DEBT_SEC = r19_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR19_COLLATERAL_EQUITIES() {
		return R19_COLLATERAL_EQUITIES;
	}
	public void setR19_COLLATERAL_EQUITIES(BigDecimal r19_COLLATERAL_EQUITIES) {
		R19_COLLATERAL_EQUITIES = r19_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR19_COLLATERAL_MUTUAL_FUNDS() {
		return R19_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR19_COLLATERAL_MUTUAL_FUNDS(BigDecimal r19_COLLATERAL_MUTUAL_FUNDS) {
		R19_COLLATERAL_MUTUAL_FUNDS = r19_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR19_TOTAL_COLLATERAL_HAIRCUT() {
		return R19_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR19_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r19_TOTAL_COLLATERAL_HAIRCUT) {
		R19_TOTAL_COLLATERAL_HAIRCUT = r19_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR19_EXPOSURE_AFTER_CRM() {
		return R19_EXPOSURE_AFTER_CRM;
	}
	public void setR19_EXPOSURE_AFTER_CRM(BigDecimal r19_EXPOSURE_AFTER_CRM) {
		R19_EXPOSURE_AFTER_CRM = r19_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR19_RWA_NOT_COVERED_CRM() {
		return R19_RWA_NOT_COVERED_CRM;
	}
	public void setR19_RWA_NOT_COVERED_CRM(BigDecimal r19_RWA_NOT_COVERED_CRM) {
		R19_RWA_NOT_COVERED_CRM = r19_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR19_RWA_UNSECURED_EXPOSURE() {
		return R19_RWA_UNSECURED_EXPOSURE;
	}
	public void setR19_RWA_UNSECURED_EXPOSURE(BigDecimal r19_RWA_UNSECURED_EXPOSURE) {
		R19_RWA_UNSECURED_EXPOSURE = r19_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR19_RWA_UNSECURED() {
		return R19_RWA_UNSECURED;
	}
	public void setR19_RWA_UNSECURED(BigDecimal r19_RWA_UNSECURED) {
		R19_RWA_UNSECURED = r19_RWA_UNSECURED;
	}
	public BigDecimal getR19_TOTAL_RWA() {
		return R19_TOTAL_RWA;
	}
	public void setR19_TOTAL_RWA(BigDecimal r19_TOTAL_RWA) {
		R19_TOTAL_RWA = r19_TOTAL_RWA;
	}
	public BigDecimal getR20_EXPOSURE_BEFORE_CRM() {
		return R20_EXPOSURE_BEFORE_CRM;
	}
	public void setR20_EXPOSURE_BEFORE_CRM(BigDecimal r20_EXPOSURE_BEFORE_CRM) {
		R20_EXPOSURE_BEFORE_CRM = r20_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR20_SPEC_PROVISION_PAST_DUE() {
		return R20_SPEC_PROVISION_PAST_DUE;
	}
	public void setR20_SPEC_PROVISION_PAST_DUE(BigDecimal r20_SPEC_PROVISION_PAST_DUE) {
		R20_SPEC_PROVISION_PAST_DUE = r20_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR20_ON_BAL_SHEET_NETTING_ELIG() {
		return R20_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR20_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r20_ON_BAL_SHEET_NETTING_ELIG) {
		R20_ON_BAL_SHEET_NETTING_ELIG = r20_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR20_TOTAL_EXPOSURE_AFTER_NET() {
		return R20_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR20_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r20_TOTAL_EXPOSURE_AFTER_NET) {
		R20_TOTAL_EXPOSURE_AFTER_NET = r20_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR20_CRM_ELIG_EXPOSURE_SUBS() {
		return R20_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR20_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r20_CRM_ELIG_EXPOSURE_SUBS) {
		R20_CRM_ELIG_EXPOSURE_SUBS = r20_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR20_ELIG_GUARANTEES() {
		return R20_ELIG_GUARANTEES;
	}
	public void setR20_ELIG_GUARANTEES(BigDecimal r20_ELIG_GUARANTEES) {
		R20_ELIG_GUARANTEES = r20_ELIG_GUARANTEES;
	}
	public BigDecimal getR20_CREDIT_DERIVATIVES() {
		return R20_CREDIT_DERIVATIVES;
	}
	public void setR20_CREDIT_DERIVATIVES(BigDecimal r20_CREDIT_DERIVATIVES) {
		R20_CREDIT_DERIVATIVES = r20_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR20_CRM_COVERED_EXPOSURE() {
		return R20_CRM_COVERED_EXPOSURE;
	}
	public void setR20_CRM_COVERED_EXPOSURE(BigDecimal r20_CRM_COVERED_EXPOSURE) {
		R20_CRM_COVERED_EXPOSURE = r20_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR20_CRM_NOT_COVERED_EXPOSURE() {
		return R20_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR20_CRM_NOT_COVERED_EXPOSURE(BigDecimal r20_CRM_NOT_COVERED_EXPOSURE) {
		R20_CRM_NOT_COVERED_EXPOSURE = r20_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR20_CRM_RISK_WEIGHT() {
		return R20_CRM_RISK_WEIGHT;
	}
	public void setR20_CRM_RISK_WEIGHT(BigDecimal r20_CRM_RISK_WEIGHT) {
		R20_CRM_RISK_WEIGHT = r20_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR20_RWA_CRM_COVERED() {
		return R20_RWA_CRM_COVERED;
	}
	public void setR20_RWA_CRM_COVERED(BigDecimal r20_RWA_CRM_COVERED) {
		R20_RWA_CRM_COVERED = r20_RWA_CRM_COVERED;
	}
	public BigDecimal getR20_ORIG_COUNTERPARTY_RW() {
		return R20_ORIG_COUNTERPARTY_RW;
	}
	public void setR20_ORIG_COUNTERPARTY_RW(BigDecimal r20_ORIG_COUNTERPARTY_RW) {
		R20_ORIG_COUNTERPARTY_RW = r20_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR20_RWA_CRM_NOT_COVERED() {
		return R20_RWA_CRM_NOT_COVERED;
	}
	public void setR20_RWA_CRM_NOT_COVERED(BigDecimal r20_RWA_CRM_NOT_COVERED) {
		R20_RWA_CRM_NOT_COVERED = r20_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR20_CRM_ELIG_EXPOSURE_COMP() {
		return R20_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR20_CRM_ELIG_EXPOSURE_COMP(BigDecimal r20_CRM_ELIG_EXPOSURE_COMP) {
		R20_CRM_ELIG_EXPOSURE_COMP = r20_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR20_EXPOSURE_AFTER_VOL_ADJ() {
		return R20_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR20_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r20_EXPOSURE_AFTER_VOL_ADJ) {
		R20_EXPOSURE_AFTER_VOL_ADJ = r20_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR20_COLLATERAL_CASH() {
		return R20_COLLATERAL_CASH;
	}
	public void setR20_COLLATERAL_CASH(BigDecimal r20_COLLATERAL_CASH) {
		R20_COLLATERAL_CASH = r20_COLLATERAL_CASH;
	}
	public BigDecimal getR20_COLLATERAL_TBILLS() {
		return R20_COLLATERAL_TBILLS;
	}
	public void setR20_COLLATERAL_TBILLS(BigDecimal r20_COLLATERAL_TBILLS) {
		R20_COLLATERAL_TBILLS = r20_COLLATERAL_TBILLS;
	}
	public BigDecimal getR20_COLLATERAL_DEBT_SEC() {
		return R20_COLLATERAL_DEBT_SEC;
	}
	public void setR20_COLLATERAL_DEBT_SEC(BigDecimal r20_COLLATERAL_DEBT_SEC) {
		R20_COLLATERAL_DEBT_SEC = r20_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR20_COLLATERAL_EQUITIES() {
		return R20_COLLATERAL_EQUITIES;
	}
	public void setR20_COLLATERAL_EQUITIES(BigDecimal r20_COLLATERAL_EQUITIES) {
		R20_COLLATERAL_EQUITIES = r20_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR20_COLLATERAL_MUTUAL_FUNDS() {
		return R20_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR20_COLLATERAL_MUTUAL_FUNDS(BigDecimal r20_COLLATERAL_MUTUAL_FUNDS) {
		R20_COLLATERAL_MUTUAL_FUNDS = r20_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR20_TOTAL_COLLATERAL_HAIRCUT() {
		return R20_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR20_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r20_TOTAL_COLLATERAL_HAIRCUT) {
		R20_TOTAL_COLLATERAL_HAIRCUT = r20_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR20_EXPOSURE_AFTER_CRM() {
		return R20_EXPOSURE_AFTER_CRM;
	}
	public void setR20_EXPOSURE_AFTER_CRM(BigDecimal r20_EXPOSURE_AFTER_CRM) {
		R20_EXPOSURE_AFTER_CRM = r20_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR20_RWA_NOT_COVERED_CRM() {
		return R20_RWA_NOT_COVERED_CRM;
	}
	public void setR20_RWA_NOT_COVERED_CRM(BigDecimal r20_RWA_NOT_COVERED_CRM) {
		R20_RWA_NOT_COVERED_CRM = r20_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR20_RWA_UNSECURED_EXPOSURE() {
		return R20_RWA_UNSECURED_EXPOSURE;
	}
	public void setR20_RWA_UNSECURED_EXPOSURE(BigDecimal r20_RWA_UNSECURED_EXPOSURE) {
		R20_RWA_UNSECURED_EXPOSURE = r20_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR20_RWA_UNSECURED() {
		return R20_RWA_UNSECURED;
	}
	public void setR20_RWA_UNSECURED(BigDecimal r20_RWA_UNSECURED) {
		R20_RWA_UNSECURED = r20_RWA_UNSECURED;
	}
	public BigDecimal getR20_TOTAL_RWA() {
		return R20_TOTAL_RWA;
	}
	public void setR20_TOTAL_RWA(BigDecimal r20_TOTAL_RWA) {
		R20_TOTAL_RWA = r20_TOTAL_RWA;
	}
	public BigDecimal getR21_EXPOSURE_BEFORE_CRM() {
		return R21_EXPOSURE_BEFORE_CRM;
	}
	public void setR21_EXPOSURE_BEFORE_CRM(BigDecimal r21_EXPOSURE_BEFORE_CRM) {
		R21_EXPOSURE_BEFORE_CRM = r21_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR21_SPEC_PROVISION_PAST_DUE() {
		return R21_SPEC_PROVISION_PAST_DUE;
	}
	public void setR21_SPEC_PROVISION_PAST_DUE(BigDecimal r21_SPEC_PROVISION_PAST_DUE) {
		R21_SPEC_PROVISION_PAST_DUE = r21_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR21_ON_BAL_SHEET_NETTING_ELIG() {
		return R21_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR21_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r21_ON_BAL_SHEET_NETTING_ELIG) {
		R21_ON_BAL_SHEET_NETTING_ELIG = r21_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR21_TOTAL_EXPOSURE_AFTER_NET() {
		return R21_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR21_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r21_TOTAL_EXPOSURE_AFTER_NET) {
		R21_TOTAL_EXPOSURE_AFTER_NET = r21_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR21_CRM_ELIG_EXPOSURE_SUBS() {
		return R21_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR21_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r21_CRM_ELIG_EXPOSURE_SUBS) {
		R21_CRM_ELIG_EXPOSURE_SUBS = r21_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR21_ELIG_GUARANTEES() {
		return R21_ELIG_GUARANTEES;
	}
	public void setR21_ELIG_GUARANTEES(BigDecimal r21_ELIG_GUARANTEES) {
		R21_ELIG_GUARANTEES = r21_ELIG_GUARANTEES;
	}
	public BigDecimal getR21_CREDIT_DERIVATIVES() {
		return R21_CREDIT_DERIVATIVES;
	}
	public void setR21_CREDIT_DERIVATIVES(BigDecimal r21_CREDIT_DERIVATIVES) {
		R21_CREDIT_DERIVATIVES = r21_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR21_CRM_COVERED_EXPOSURE() {
		return R21_CRM_COVERED_EXPOSURE;
	}
	public void setR21_CRM_COVERED_EXPOSURE(BigDecimal r21_CRM_COVERED_EXPOSURE) {
		R21_CRM_COVERED_EXPOSURE = r21_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR21_CRM_NOT_COVERED_EXPOSURE() {
		return R21_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR21_CRM_NOT_COVERED_EXPOSURE(BigDecimal r21_CRM_NOT_COVERED_EXPOSURE) {
		R21_CRM_NOT_COVERED_EXPOSURE = r21_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR21_CRM_RISK_WEIGHT() {
		return R21_CRM_RISK_WEIGHT;
	}
	public void setR21_CRM_RISK_WEIGHT(BigDecimal r21_CRM_RISK_WEIGHT) {
		R21_CRM_RISK_WEIGHT = r21_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR21_RWA_CRM_COVERED() {
		return R21_RWA_CRM_COVERED;
	}
	public void setR21_RWA_CRM_COVERED(BigDecimal r21_RWA_CRM_COVERED) {
		R21_RWA_CRM_COVERED = r21_RWA_CRM_COVERED;
	}
	public BigDecimal getR21_ORIG_COUNTERPARTY_RW() {
		return R21_ORIG_COUNTERPARTY_RW;
	}
	public void setR21_ORIG_COUNTERPARTY_RW(BigDecimal r21_ORIG_COUNTERPARTY_RW) {
		R21_ORIG_COUNTERPARTY_RW = r21_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR21_RWA_CRM_NOT_COVERED() {
		return R21_RWA_CRM_NOT_COVERED;
	}
	public void setR21_RWA_CRM_NOT_COVERED(BigDecimal r21_RWA_CRM_NOT_COVERED) {
		R21_RWA_CRM_NOT_COVERED = r21_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR21_CRM_ELIG_EXPOSURE_COMP() {
		return R21_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR21_CRM_ELIG_EXPOSURE_COMP(BigDecimal r21_CRM_ELIG_EXPOSURE_COMP) {
		R21_CRM_ELIG_EXPOSURE_COMP = r21_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR21_EXPOSURE_AFTER_VOL_ADJ() {
		return R21_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR21_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r21_EXPOSURE_AFTER_VOL_ADJ) {
		R21_EXPOSURE_AFTER_VOL_ADJ = r21_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR21_COLLATERAL_CASH() {
		return R21_COLLATERAL_CASH;
	}
	public void setR21_COLLATERAL_CASH(BigDecimal r21_COLLATERAL_CASH) {
		R21_COLLATERAL_CASH = r21_COLLATERAL_CASH;
	}
	public BigDecimal getR21_COLLATERAL_TBILLS() {
		return R21_COLLATERAL_TBILLS;
	}
	public void setR21_COLLATERAL_TBILLS(BigDecimal r21_COLLATERAL_TBILLS) {
		R21_COLLATERAL_TBILLS = r21_COLLATERAL_TBILLS;
	}
	public BigDecimal getR21_COLLATERAL_DEBT_SEC() {
		return R21_COLLATERAL_DEBT_SEC;
	}
	public void setR21_COLLATERAL_DEBT_SEC(BigDecimal r21_COLLATERAL_DEBT_SEC) {
		R21_COLLATERAL_DEBT_SEC = r21_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR21_COLLATERAL_EQUITIES() {
		return R21_COLLATERAL_EQUITIES;
	}
	public void setR21_COLLATERAL_EQUITIES(BigDecimal r21_COLLATERAL_EQUITIES) {
		R21_COLLATERAL_EQUITIES = r21_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR21_COLLATERAL_MUTUAL_FUNDS() {
		return R21_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR21_COLLATERAL_MUTUAL_FUNDS(BigDecimal r21_COLLATERAL_MUTUAL_FUNDS) {
		R21_COLLATERAL_MUTUAL_FUNDS = r21_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR21_TOTAL_COLLATERAL_HAIRCUT() {
		return R21_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR21_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r21_TOTAL_COLLATERAL_HAIRCUT) {
		R21_TOTAL_COLLATERAL_HAIRCUT = r21_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR21_EXPOSURE_AFTER_CRM() {
		return R21_EXPOSURE_AFTER_CRM;
	}
	public void setR21_EXPOSURE_AFTER_CRM(BigDecimal r21_EXPOSURE_AFTER_CRM) {
		R21_EXPOSURE_AFTER_CRM = r21_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR21_RWA_NOT_COVERED_CRM() {
		return R21_RWA_NOT_COVERED_CRM;
	}
	public void setR21_RWA_NOT_COVERED_CRM(BigDecimal r21_RWA_NOT_COVERED_CRM) {
		R21_RWA_NOT_COVERED_CRM = r21_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR21_RWA_UNSECURED_EXPOSURE() {
		return R21_RWA_UNSECURED_EXPOSURE;
	}
	public void setR21_RWA_UNSECURED_EXPOSURE(BigDecimal r21_RWA_UNSECURED_EXPOSURE) {
		R21_RWA_UNSECURED_EXPOSURE = r21_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR21_RWA_UNSECURED() {
		return R21_RWA_UNSECURED;
	}
	public void setR21_RWA_UNSECURED(BigDecimal r21_RWA_UNSECURED) {
		R21_RWA_UNSECURED = r21_RWA_UNSECURED;
	}
	public BigDecimal getR21_TOTAL_RWA() {
		return R21_TOTAL_RWA;
	}
	public void setR21_TOTAL_RWA(BigDecimal r21_TOTAL_RWA) {
		R21_TOTAL_RWA = r21_TOTAL_RWA;
	}
	public BigDecimal getR22_EXPOSURE_BEFORE_CRM() {
		return R22_EXPOSURE_BEFORE_CRM;
	}
	public void setR22_EXPOSURE_BEFORE_CRM(BigDecimal r22_EXPOSURE_BEFORE_CRM) {
		R22_EXPOSURE_BEFORE_CRM = r22_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR22_SPEC_PROVISION_PAST_DUE() {
		return R22_SPEC_PROVISION_PAST_DUE;
	}
	public void setR22_SPEC_PROVISION_PAST_DUE(BigDecimal r22_SPEC_PROVISION_PAST_DUE) {
		R22_SPEC_PROVISION_PAST_DUE = r22_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR22_ON_BAL_SHEET_NETTING_ELIG() {
		return R22_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR22_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r22_ON_BAL_SHEET_NETTING_ELIG) {
		R22_ON_BAL_SHEET_NETTING_ELIG = r22_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR22_TOTAL_EXPOSURE_AFTER_NET() {
		return R22_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR22_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r22_TOTAL_EXPOSURE_AFTER_NET) {
		R22_TOTAL_EXPOSURE_AFTER_NET = r22_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR22_CRM_ELIG_EXPOSURE_SUBS() {
		return R22_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR22_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r22_CRM_ELIG_EXPOSURE_SUBS) {
		R22_CRM_ELIG_EXPOSURE_SUBS = r22_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR22_ELIG_GUARANTEES() {
		return R22_ELIG_GUARANTEES;
	}
	public void setR22_ELIG_GUARANTEES(BigDecimal r22_ELIG_GUARANTEES) {
		R22_ELIG_GUARANTEES = r22_ELIG_GUARANTEES;
	}
	public BigDecimal getR22_CREDIT_DERIVATIVES() {
		return R22_CREDIT_DERIVATIVES;
	}
	public void setR22_CREDIT_DERIVATIVES(BigDecimal r22_CREDIT_DERIVATIVES) {
		R22_CREDIT_DERIVATIVES = r22_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR22_CRM_COVERED_EXPOSURE() {
		return R22_CRM_COVERED_EXPOSURE;
	}
	public void setR22_CRM_COVERED_EXPOSURE(BigDecimal r22_CRM_COVERED_EXPOSURE) {
		R22_CRM_COVERED_EXPOSURE = r22_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR22_CRM_NOT_COVERED_EXPOSURE() {
		return R22_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR22_CRM_NOT_COVERED_EXPOSURE(BigDecimal r22_CRM_NOT_COVERED_EXPOSURE) {
		R22_CRM_NOT_COVERED_EXPOSURE = r22_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR22_CRM_RISK_WEIGHT() {
		return R22_CRM_RISK_WEIGHT;
	}
	public void setR22_CRM_RISK_WEIGHT(BigDecimal r22_CRM_RISK_WEIGHT) {
		R22_CRM_RISK_WEIGHT = r22_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR22_RWA_CRM_COVERED() {
		return R22_RWA_CRM_COVERED;
	}
	public void setR22_RWA_CRM_COVERED(BigDecimal r22_RWA_CRM_COVERED) {
		R22_RWA_CRM_COVERED = r22_RWA_CRM_COVERED;
	}
	public BigDecimal getR22_ORIG_COUNTERPARTY_RW() {
		return R22_ORIG_COUNTERPARTY_RW;
	}
	public void setR22_ORIG_COUNTERPARTY_RW(BigDecimal r22_ORIG_COUNTERPARTY_RW) {
		R22_ORIG_COUNTERPARTY_RW = r22_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR22_RWA_CRM_NOT_COVERED() {
		return R22_RWA_CRM_NOT_COVERED;
	}
	public void setR22_RWA_CRM_NOT_COVERED(BigDecimal r22_RWA_CRM_NOT_COVERED) {
		R22_RWA_CRM_NOT_COVERED = r22_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR22_CRM_ELIG_EXPOSURE_COMP() {
		return R22_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR22_CRM_ELIG_EXPOSURE_COMP(BigDecimal r22_CRM_ELIG_EXPOSURE_COMP) {
		R22_CRM_ELIG_EXPOSURE_COMP = r22_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR22_EXPOSURE_AFTER_VOL_ADJ() {
		return R22_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR22_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r22_EXPOSURE_AFTER_VOL_ADJ) {
		R22_EXPOSURE_AFTER_VOL_ADJ = r22_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR22_COLLATERAL_CASH() {
		return R22_COLLATERAL_CASH;
	}
	public void setR22_COLLATERAL_CASH(BigDecimal r22_COLLATERAL_CASH) {
		R22_COLLATERAL_CASH = r22_COLLATERAL_CASH;
	}
	public BigDecimal getR22_COLLATERAL_TBILLS() {
		return R22_COLLATERAL_TBILLS;
	}
	public void setR22_COLLATERAL_TBILLS(BigDecimal r22_COLLATERAL_TBILLS) {
		R22_COLLATERAL_TBILLS = r22_COLLATERAL_TBILLS;
	}
	public BigDecimal getR22_COLLATERAL_DEBT_SEC() {
		return R22_COLLATERAL_DEBT_SEC;
	}
	public void setR22_COLLATERAL_DEBT_SEC(BigDecimal r22_COLLATERAL_DEBT_SEC) {
		R22_COLLATERAL_DEBT_SEC = r22_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR22_COLLATERAL_EQUITIES() {
		return R22_COLLATERAL_EQUITIES;
	}
	public void setR22_COLLATERAL_EQUITIES(BigDecimal r22_COLLATERAL_EQUITIES) {
		R22_COLLATERAL_EQUITIES = r22_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR22_COLLATERAL_MUTUAL_FUNDS() {
		return R22_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR22_COLLATERAL_MUTUAL_FUNDS(BigDecimal r22_COLLATERAL_MUTUAL_FUNDS) {
		R22_COLLATERAL_MUTUAL_FUNDS = r22_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR22_TOTAL_COLLATERAL_HAIRCUT() {
		return R22_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR22_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r22_TOTAL_COLLATERAL_HAIRCUT) {
		R22_TOTAL_COLLATERAL_HAIRCUT = r22_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR22_EXPOSURE_AFTER_CRM() {
		return R22_EXPOSURE_AFTER_CRM;
	}
	public void setR22_EXPOSURE_AFTER_CRM(BigDecimal r22_EXPOSURE_AFTER_CRM) {
		R22_EXPOSURE_AFTER_CRM = r22_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR22_RWA_NOT_COVERED_CRM() {
		return R22_RWA_NOT_COVERED_CRM;
	}
	public void setR22_RWA_NOT_COVERED_CRM(BigDecimal r22_RWA_NOT_COVERED_CRM) {
		R22_RWA_NOT_COVERED_CRM = r22_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR22_RWA_UNSECURED_EXPOSURE() {
		return R22_RWA_UNSECURED_EXPOSURE;
	}
	public void setR22_RWA_UNSECURED_EXPOSURE(BigDecimal r22_RWA_UNSECURED_EXPOSURE) {
		R22_RWA_UNSECURED_EXPOSURE = r22_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR22_RWA_UNSECURED() {
		return R22_RWA_UNSECURED;
	}
	public void setR22_RWA_UNSECURED(BigDecimal r22_RWA_UNSECURED) {
		R22_RWA_UNSECURED = r22_RWA_UNSECURED;
	}
	public BigDecimal getR22_TOTAL_RWA() {
		return R22_TOTAL_RWA;
	}
	public void setR22_TOTAL_RWA(BigDecimal r22_TOTAL_RWA) {
		R22_TOTAL_RWA = r22_TOTAL_RWA;
	}
	public BigDecimal getR23_EXPOSURE_BEFORE_CRM() {
		return R23_EXPOSURE_BEFORE_CRM;
	}
	public void setR23_EXPOSURE_BEFORE_CRM(BigDecimal r23_EXPOSURE_BEFORE_CRM) {
		R23_EXPOSURE_BEFORE_CRM = r23_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR23_SPEC_PROVISION_PAST_DUE() {
		return R23_SPEC_PROVISION_PAST_DUE;
	}
	public void setR23_SPEC_PROVISION_PAST_DUE(BigDecimal r23_SPEC_PROVISION_PAST_DUE) {
		R23_SPEC_PROVISION_PAST_DUE = r23_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR23_ON_BAL_SHEET_NETTING_ELIG() {
		return R23_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR23_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r23_ON_BAL_SHEET_NETTING_ELIG) {
		R23_ON_BAL_SHEET_NETTING_ELIG = r23_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR23_TOTAL_EXPOSURE_AFTER_NET() {
		return R23_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR23_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r23_TOTAL_EXPOSURE_AFTER_NET) {
		R23_TOTAL_EXPOSURE_AFTER_NET = r23_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR23_CRM_ELIG_EXPOSURE_SUBS() {
		return R23_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR23_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r23_CRM_ELIG_EXPOSURE_SUBS) {
		R23_CRM_ELIG_EXPOSURE_SUBS = r23_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR23_ELIG_GUARANTEES() {
		return R23_ELIG_GUARANTEES;
	}
	public void setR23_ELIG_GUARANTEES(BigDecimal r23_ELIG_GUARANTEES) {
		R23_ELIG_GUARANTEES = r23_ELIG_GUARANTEES;
	}
	public BigDecimal getR23_CREDIT_DERIVATIVES() {
		return R23_CREDIT_DERIVATIVES;
	}
	public void setR23_CREDIT_DERIVATIVES(BigDecimal r23_CREDIT_DERIVATIVES) {
		R23_CREDIT_DERIVATIVES = r23_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR23_CRM_COVERED_EXPOSURE() {
		return R23_CRM_COVERED_EXPOSURE;
	}
	public void setR23_CRM_COVERED_EXPOSURE(BigDecimal r23_CRM_COVERED_EXPOSURE) {
		R23_CRM_COVERED_EXPOSURE = r23_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR23_CRM_NOT_COVERED_EXPOSURE() {
		return R23_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR23_CRM_NOT_COVERED_EXPOSURE(BigDecimal r23_CRM_NOT_COVERED_EXPOSURE) {
		R23_CRM_NOT_COVERED_EXPOSURE = r23_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR23_CRM_RISK_WEIGHT() {
		return R23_CRM_RISK_WEIGHT;
	}
	public void setR23_CRM_RISK_WEIGHT(BigDecimal r23_CRM_RISK_WEIGHT) {
		R23_CRM_RISK_WEIGHT = r23_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR23_RWA_CRM_COVERED() {
		return R23_RWA_CRM_COVERED;
	}
	public void setR23_RWA_CRM_COVERED(BigDecimal r23_RWA_CRM_COVERED) {
		R23_RWA_CRM_COVERED = r23_RWA_CRM_COVERED;
	}
	public BigDecimal getR23_ORIG_COUNTERPARTY_RW() {
		return R23_ORIG_COUNTERPARTY_RW;
	}
	public void setR23_ORIG_COUNTERPARTY_RW(BigDecimal r23_ORIG_COUNTERPARTY_RW) {
		R23_ORIG_COUNTERPARTY_RW = r23_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR23_RWA_CRM_NOT_COVERED() {
		return R23_RWA_CRM_NOT_COVERED;
	}
	public void setR23_RWA_CRM_NOT_COVERED(BigDecimal r23_RWA_CRM_NOT_COVERED) {
		R23_RWA_CRM_NOT_COVERED = r23_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR23_CRM_ELIG_EXPOSURE_COMP() {
		return R23_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR23_CRM_ELIG_EXPOSURE_COMP(BigDecimal r23_CRM_ELIG_EXPOSURE_COMP) {
		R23_CRM_ELIG_EXPOSURE_COMP = r23_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR23_EXPOSURE_AFTER_VOL_ADJ() {
		return R23_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR23_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r23_EXPOSURE_AFTER_VOL_ADJ) {
		R23_EXPOSURE_AFTER_VOL_ADJ = r23_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR23_COLLATERAL_CASH() {
		return R23_COLLATERAL_CASH;
	}
	public void setR23_COLLATERAL_CASH(BigDecimal r23_COLLATERAL_CASH) {
		R23_COLLATERAL_CASH = r23_COLLATERAL_CASH;
	}
	public BigDecimal getR23_COLLATERAL_TBILLS() {
		return R23_COLLATERAL_TBILLS;
	}
	public void setR23_COLLATERAL_TBILLS(BigDecimal r23_COLLATERAL_TBILLS) {
		R23_COLLATERAL_TBILLS = r23_COLLATERAL_TBILLS;
	}
	public BigDecimal getR23_COLLATERAL_DEBT_SEC() {
		return R23_COLLATERAL_DEBT_SEC;
	}
	public void setR23_COLLATERAL_DEBT_SEC(BigDecimal r23_COLLATERAL_DEBT_SEC) {
		R23_COLLATERAL_DEBT_SEC = r23_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR23_COLLATERAL_EQUITIES() {
		return R23_COLLATERAL_EQUITIES;
	}
	public void setR23_COLLATERAL_EQUITIES(BigDecimal r23_COLLATERAL_EQUITIES) {
		R23_COLLATERAL_EQUITIES = r23_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR23_COLLATERAL_MUTUAL_FUNDS() {
		return R23_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR23_COLLATERAL_MUTUAL_FUNDS(BigDecimal r23_COLLATERAL_MUTUAL_FUNDS) {
		R23_COLLATERAL_MUTUAL_FUNDS = r23_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR23_TOTAL_COLLATERAL_HAIRCUT() {
		return R23_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR23_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r23_TOTAL_COLLATERAL_HAIRCUT) {
		R23_TOTAL_COLLATERAL_HAIRCUT = r23_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR23_EXPOSURE_AFTER_CRM() {
		return R23_EXPOSURE_AFTER_CRM;
	}
	public void setR23_EXPOSURE_AFTER_CRM(BigDecimal r23_EXPOSURE_AFTER_CRM) {
		R23_EXPOSURE_AFTER_CRM = r23_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR23_RWA_NOT_COVERED_CRM() {
		return R23_RWA_NOT_COVERED_CRM;
	}
	public void setR23_RWA_NOT_COVERED_CRM(BigDecimal r23_RWA_NOT_COVERED_CRM) {
		R23_RWA_NOT_COVERED_CRM = r23_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR23_RWA_UNSECURED_EXPOSURE() {
		return R23_RWA_UNSECURED_EXPOSURE;
	}
	public void setR23_RWA_UNSECURED_EXPOSURE(BigDecimal r23_RWA_UNSECURED_EXPOSURE) {
		R23_RWA_UNSECURED_EXPOSURE = r23_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR23_RWA_UNSECURED() {
		return R23_RWA_UNSECURED;
	}
	public void setR23_RWA_UNSECURED(BigDecimal r23_RWA_UNSECURED) {
		R23_RWA_UNSECURED = r23_RWA_UNSECURED;
	}
	public BigDecimal getR23_TOTAL_RWA() {
		return R23_TOTAL_RWA;
	}
	public void setR23_TOTAL_RWA(BigDecimal r23_TOTAL_RWA) {
		R23_TOTAL_RWA = r23_TOTAL_RWA;
	}
	public BigDecimal getR24_EXPOSURE_BEFORE_CRM() {
		return R24_EXPOSURE_BEFORE_CRM;
	}
	public void setR24_EXPOSURE_BEFORE_CRM(BigDecimal r24_EXPOSURE_BEFORE_CRM) {
		R24_EXPOSURE_BEFORE_CRM = r24_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR24_SPEC_PROVISION_PAST_DUE() {
		return R24_SPEC_PROVISION_PAST_DUE;
	}
	public void setR24_SPEC_PROVISION_PAST_DUE(BigDecimal r24_SPEC_PROVISION_PAST_DUE) {
		R24_SPEC_PROVISION_PAST_DUE = r24_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR24_ON_BAL_SHEET_NETTING_ELIG() {
		return R24_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR24_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r24_ON_BAL_SHEET_NETTING_ELIG) {
		R24_ON_BAL_SHEET_NETTING_ELIG = r24_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR24_TOTAL_EXPOSURE_AFTER_NET() {
		return R24_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR24_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r24_TOTAL_EXPOSURE_AFTER_NET) {
		R24_TOTAL_EXPOSURE_AFTER_NET = r24_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR24_CRM_ELIG_EXPOSURE_SUBS() {
		return R24_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR24_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r24_CRM_ELIG_EXPOSURE_SUBS) {
		R24_CRM_ELIG_EXPOSURE_SUBS = r24_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR24_ELIG_GUARANTEES() {
		return R24_ELIG_GUARANTEES;
	}
	public void setR24_ELIG_GUARANTEES(BigDecimal r24_ELIG_GUARANTEES) {
		R24_ELIG_GUARANTEES = r24_ELIG_GUARANTEES;
	}
	public BigDecimal getR24_CREDIT_DERIVATIVES() {
		return R24_CREDIT_DERIVATIVES;
	}
	public void setR24_CREDIT_DERIVATIVES(BigDecimal r24_CREDIT_DERIVATIVES) {
		R24_CREDIT_DERIVATIVES = r24_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR24_CRM_COVERED_EXPOSURE() {
		return R24_CRM_COVERED_EXPOSURE;
	}
	public void setR24_CRM_COVERED_EXPOSURE(BigDecimal r24_CRM_COVERED_EXPOSURE) {
		R24_CRM_COVERED_EXPOSURE = r24_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR24_CRM_NOT_COVERED_EXPOSURE() {
		return R24_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR24_CRM_NOT_COVERED_EXPOSURE(BigDecimal r24_CRM_NOT_COVERED_EXPOSURE) {
		R24_CRM_NOT_COVERED_EXPOSURE = r24_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR24_CRM_RISK_WEIGHT() {
		return R24_CRM_RISK_WEIGHT;
	}
	public void setR24_CRM_RISK_WEIGHT(BigDecimal r24_CRM_RISK_WEIGHT) {
		R24_CRM_RISK_WEIGHT = r24_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR24_RWA_CRM_COVERED() {
		return R24_RWA_CRM_COVERED;
	}
	public void setR24_RWA_CRM_COVERED(BigDecimal r24_RWA_CRM_COVERED) {
		R24_RWA_CRM_COVERED = r24_RWA_CRM_COVERED;
	}
	public BigDecimal getR24_ORIG_COUNTERPARTY_RW() {
		return R24_ORIG_COUNTERPARTY_RW;
	}
	public void setR24_ORIG_COUNTERPARTY_RW(BigDecimal r24_ORIG_COUNTERPARTY_RW) {
		R24_ORIG_COUNTERPARTY_RW = r24_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR24_RWA_CRM_NOT_COVERED() {
		return R24_RWA_CRM_NOT_COVERED;
	}
	public void setR24_RWA_CRM_NOT_COVERED(BigDecimal r24_RWA_CRM_NOT_COVERED) {
		R24_RWA_CRM_NOT_COVERED = r24_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR24_CRM_ELIG_EXPOSURE_COMP() {
		return R24_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR24_CRM_ELIG_EXPOSURE_COMP(BigDecimal r24_CRM_ELIG_EXPOSURE_COMP) {
		R24_CRM_ELIG_EXPOSURE_COMP = r24_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR24_EXPOSURE_AFTER_VOL_ADJ() {
		return R24_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR24_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r24_EXPOSURE_AFTER_VOL_ADJ) {
		R24_EXPOSURE_AFTER_VOL_ADJ = r24_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR24_COLLATERAL_CASH() {
		return R24_COLLATERAL_CASH;
	}
	public void setR24_COLLATERAL_CASH(BigDecimal r24_COLLATERAL_CASH) {
		R24_COLLATERAL_CASH = r24_COLLATERAL_CASH;
	}
	public BigDecimal getR24_COLLATERAL_TBILLS() {
		return R24_COLLATERAL_TBILLS;
	}
	public void setR24_COLLATERAL_TBILLS(BigDecimal r24_COLLATERAL_TBILLS) {
		R24_COLLATERAL_TBILLS = r24_COLLATERAL_TBILLS;
	}
	public BigDecimal getR24_COLLATERAL_DEBT_SEC() {
		return R24_COLLATERAL_DEBT_SEC;
	}
	public void setR24_COLLATERAL_DEBT_SEC(BigDecimal r24_COLLATERAL_DEBT_SEC) {
		R24_COLLATERAL_DEBT_SEC = r24_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR24_COLLATERAL_EQUITIES() {
		return R24_COLLATERAL_EQUITIES;
	}
	public void setR24_COLLATERAL_EQUITIES(BigDecimal r24_COLLATERAL_EQUITIES) {
		R24_COLLATERAL_EQUITIES = r24_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR24_COLLATERAL_MUTUAL_FUNDS() {
		return R24_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR24_COLLATERAL_MUTUAL_FUNDS(BigDecimal r24_COLLATERAL_MUTUAL_FUNDS) {
		R24_COLLATERAL_MUTUAL_FUNDS = r24_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR24_TOTAL_COLLATERAL_HAIRCUT() {
		return R24_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR24_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r24_TOTAL_COLLATERAL_HAIRCUT) {
		R24_TOTAL_COLLATERAL_HAIRCUT = r24_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR24_EXPOSURE_AFTER_CRM() {
		return R24_EXPOSURE_AFTER_CRM;
	}
	public void setR24_EXPOSURE_AFTER_CRM(BigDecimal r24_EXPOSURE_AFTER_CRM) {
		R24_EXPOSURE_AFTER_CRM = r24_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR24_RWA_NOT_COVERED_CRM() {
		return R24_RWA_NOT_COVERED_CRM;
	}
	public void setR24_RWA_NOT_COVERED_CRM(BigDecimal r24_RWA_NOT_COVERED_CRM) {
		R24_RWA_NOT_COVERED_CRM = r24_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR24_RWA_UNSECURED_EXPOSURE() {
		return R24_RWA_UNSECURED_EXPOSURE;
	}
	public void setR24_RWA_UNSECURED_EXPOSURE(BigDecimal r24_RWA_UNSECURED_EXPOSURE) {
		R24_RWA_UNSECURED_EXPOSURE = r24_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR24_RWA_UNSECURED() {
		return R24_RWA_UNSECURED;
	}
	public void setR24_RWA_UNSECURED(BigDecimal r24_RWA_UNSECURED) {
		R24_RWA_UNSECURED = r24_RWA_UNSECURED;
	}
	public BigDecimal getR24_TOTAL_RWA() {
		return R24_TOTAL_RWA;
	}
	public void setR24_TOTAL_RWA(BigDecimal r24_TOTAL_RWA) {
		R24_TOTAL_RWA = r24_TOTAL_RWA;
	}
	public BigDecimal getR25_EXPOSURE_BEFORE_CRM() {
		return R25_EXPOSURE_BEFORE_CRM;
	}
	public void setR25_EXPOSURE_BEFORE_CRM(BigDecimal r25_EXPOSURE_BEFORE_CRM) {
		R25_EXPOSURE_BEFORE_CRM = r25_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR25_SPEC_PROVISION_PAST_DUE() {
		return R25_SPEC_PROVISION_PAST_DUE;
	}
	public void setR25_SPEC_PROVISION_PAST_DUE(BigDecimal r25_SPEC_PROVISION_PAST_DUE) {
		R25_SPEC_PROVISION_PAST_DUE = r25_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR25_ON_BAL_SHEET_NETTING_ELIG() {
		return R25_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR25_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r25_ON_BAL_SHEET_NETTING_ELIG) {
		R25_ON_BAL_SHEET_NETTING_ELIG = r25_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR25_TOTAL_EXPOSURE_AFTER_NET() {
		return R25_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR25_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r25_TOTAL_EXPOSURE_AFTER_NET) {
		R25_TOTAL_EXPOSURE_AFTER_NET = r25_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR25_CRM_ELIG_EXPOSURE_SUBS() {
		return R25_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR25_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r25_CRM_ELIG_EXPOSURE_SUBS) {
		R25_CRM_ELIG_EXPOSURE_SUBS = r25_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR25_ELIG_GUARANTEES() {
		return R25_ELIG_GUARANTEES;
	}
	public void setR25_ELIG_GUARANTEES(BigDecimal r25_ELIG_GUARANTEES) {
		R25_ELIG_GUARANTEES = r25_ELIG_GUARANTEES;
	}
	public BigDecimal getR25_CREDIT_DERIVATIVES() {
		return R25_CREDIT_DERIVATIVES;
	}
	public void setR25_CREDIT_DERIVATIVES(BigDecimal r25_CREDIT_DERIVATIVES) {
		R25_CREDIT_DERIVATIVES = r25_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR25_CRM_COVERED_EXPOSURE() {
		return R25_CRM_COVERED_EXPOSURE;
	}
	public void setR25_CRM_COVERED_EXPOSURE(BigDecimal r25_CRM_COVERED_EXPOSURE) {
		R25_CRM_COVERED_EXPOSURE = r25_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR25_CRM_NOT_COVERED_EXPOSURE() {
		return R25_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR25_CRM_NOT_COVERED_EXPOSURE(BigDecimal r25_CRM_NOT_COVERED_EXPOSURE) {
		R25_CRM_NOT_COVERED_EXPOSURE = r25_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR25_CRM_RISK_WEIGHT() {
		return R25_CRM_RISK_WEIGHT;
	}
	public void setR25_CRM_RISK_WEIGHT(BigDecimal r25_CRM_RISK_WEIGHT) {
		R25_CRM_RISK_WEIGHT = r25_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR25_RWA_CRM_COVERED() {
		return R25_RWA_CRM_COVERED;
	}
	public void setR25_RWA_CRM_COVERED(BigDecimal r25_RWA_CRM_COVERED) {
		R25_RWA_CRM_COVERED = r25_RWA_CRM_COVERED;
	}
	public BigDecimal getR25_ORIG_COUNTERPARTY_RW() {
		return R25_ORIG_COUNTERPARTY_RW;
	}
	public void setR25_ORIG_COUNTERPARTY_RW(BigDecimal r25_ORIG_COUNTERPARTY_RW) {
		R25_ORIG_COUNTERPARTY_RW = r25_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR25_RWA_CRM_NOT_COVERED() {
		return R25_RWA_CRM_NOT_COVERED;
	}
	public void setR25_RWA_CRM_NOT_COVERED(BigDecimal r25_RWA_CRM_NOT_COVERED) {
		R25_RWA_CRM_NOT_COVERED = r25_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR25_CRM_ELIG_EXPOSURE_COMP() {
		return R25_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR25_CRM_ELIG_EXPOSURE_COMP(BigDecimal r25_CRM_ELIG_EXPOSURE_COMP) {
		R25_CRM_ELIG_EXPOSURE_COMP = r25_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR25_EXPOSURE_AFTER_VOL_ADJ() {
		return R25_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR25_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r25_EXPOSURE_AFTER_VOL_ADJ) {
		R25_EXPOSURE_AFTER_VOL_ADJ = r25_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR25_COLLATERAL_CASH() {
		return R25_COLLATERAL_CASH;
	}
	public void setR25_COLLATERAL_CASH(BigDecimal r25_COLLATERAL_CASH) {
		R25_COLLATERAL_CASH = r25_COLLATERAL_CASH;
	}
	public BigDecimal getR25_COLLATERAL_TBILLS() {
		return R25_COLLATERAL_TBILLS;
	}
	public void setR25_COLLATERAL_TBILLS(BigDecimal r25_COLLATERAL_TBILLS) {
		R25_COLLATERAL_TBILLS = r25_COLLATERAL_TBILLS;
	}
	public BigDecimal getR25_COLLATERAL_DEBT_SEC() {
		return R25_COLLATERAL_DEBT_SEC;
	}
	public void setR25_COLLATERAL_DEBT_SEC(BigDecimal r25_COLLATERAL_DEBT_SEC) {
		R25_COLLATERAL_DEBT_SEC = r25_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR25_COLLATERAL_EQUITIES() {
		return R25_COLLATERAL_EQUITIES;
	}
	public void setR25_COLLATERAL_EQUITIES(BigDecimal r25_COLLATERAL_EQUITIES) {
		R25_COLLATERAL_EQUITIES = r25_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR25_COLLATERAL_MUTUAL_FUNDS() {
		return R25_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR25_COLLATERAL_MUTUAL_FUNDS(BigDecimal r25_COLLATERAL_MUTUAL_FUNDS) {
		R25_COLLATERAL_MUTUAL_FUNDS = r25_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR25_TOTAL_COLLATERAL_HAIRCUT() {
		return R25_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR25_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r25_TOTAL_COLLATERAL_HAIRCUT) {
		R25_TOTAL_COLLATERAL_HAIRCUT = r25_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR25_EXPOSURE_AFTER_CRM() {
		return R25_EXPOSURE_AFTER_CRM;
	}
	public void setR25_EXPOSURE_AFTER_CRM(BigDecimal r25_EXPOSURE_AFTER_CRM) {
		R25_EXPOSURE_AFTER_CRM = r25_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR25_RWA_NOT_COVERED_CRM() {
		return R25_RWA_NOT_COVERED_CRM;
	}
	public void setR25_RWA_NOT_COVERED_CRM(BigDecimal r25_RWA_NOT_COVERED_CRM) {
		R25_RWA_NOT_COVERED_CRM = r25_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR25_RWA_UNSECURED_EXPOSURE() {
		return R25_RWA_UNSECURED_EXPOSURE;
	}
	public void setR25_RWA_UNSECURED_EXPOSURE(BigDecimal r25_RWA_UNSECURED_EXPOSURE) {
		R25_RWA_UNSECURED_EXPOSURE = r25_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR25_RWA_UNSECURED() {
		return R25_RWA_UNSECURED;
	}
	public void setR25_RWA_UNSECURED(BigDecimal r25_RWA_UNSECURED) {
		R25_RWA_UNSECURED = r25_RWA_UNSECURED;
	}
	public BigDecimal getR25_TOTAL_RWA() {
		return R25_TOTAL_RWA;
	}
	public void setR25_TOTAL_RWA(BigDecimal r25_TOTAL_RWA) {
		R25_TOTAL_RWA = r25_TOTAL_RWA;
	}
	public BigDecimal getR26_EXPOSURE_BEFORE_CRM() {
		return R26_EXPOSURE_BEFORE_CRM;
	}
	public void setR26_EXPOSURE_BEFORE_CRM(BigDecimal r26_EXPOSURE_BEFORE_CRM) {
		R26_EXPOSURE_BEFORE_CRM = r26_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR26_SPEC_PROVISION_PAST_DUE() {
		return R26_SPEC_PROVISION_PAST_DUE;
	}
	public void setR26_SPEC_PROVISION_PAST_DUE(BigDecimal r26_SPEC_PROVISION_PAST_DUE) {
		R26_SPEC_PROVISION_PAST_DUE = r26_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR26_ON_BAL_SHEET_NETTING_ELIG() {
		return R26_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR26_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r26_ON_BAL_SHEET_NETTING_ELIG) {
		R26_ON_BAL_SHEET_NETTING_ELIG = r26_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR26_TOTAL_EXPOSURE_AFTER_NET() {
		return R26_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR26_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r26_TOTAL_EXPOSURE_AFTER_NET) {
		R26_TOTAL_EXPOSURE_AFTER_NET = r26_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR26_CRM_ELIG_EXPOSURE_SUBS() {
		return R26_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR26_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r26_CRM_ELIG_EXPOSURE_SUBS) {
		R26_CRM_ELIG_EXPOSURE_SUBS = r26_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR26_ELIG_GUARANTEES() {
		return R26_ELIG_GUARANTEES;
	}
	public void setR26_ELIG_GUARANTEES(BigDecimal r26_ELIG_GUARANTEES) {
		R26_ELIG_GUARANTEES = r26_ELIG_GUARANTEES;
	}
	public BigDecimal getR26_CREDIT_DERIVATIVES() {
		return R26_CREDIT_DERIVATIVES;
	}
	public void setR26_CREDIT_DERIVATIVES(BigDecimal r26_CREDIT_DERIVATIVES) {
		R26_CREDIT_DERIVATIVES = r26_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR26_CRM_COVERED_EXPOSURE() {
		return R26_CRM_COVERED_EXPOSURE;
	}
	public void setR26_CRM_COVERED_EXPOSURE(BigDecimal r26_CRM_COVERED_EXPOSURE) {
		R26_CRM_COVERED_EXPOSURE = r26_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR26_CRM_NOT_COVERED_EXPOSURE() {
		return R26_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR26_CRM_NOT_COVERED_EXPOSURE(BigDecimal r26_CRM_NOT_COVERED_EXPOSURE) {
		R26_CRM_NOT_COVERED_EXPOSURE = r26_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR26_CRM_RISK_WEIGHT() {
		return R26_CRM_RISK_WEIGHT;
	}
	public void setR26_CRM_RISK_WEIGHT(BigDecimal r26_CRM_RISK_WEIGHT) {
		R26_CRM_RISK_WEIGHT = r26_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR26_RWA_CRM_COVERED() {
		return R26_RWA_CRM_COVERED;
	}
	public void setR26_RWA_CRM_COVERED(BigDecimal r26_RWA_CRM_COVERED) {
		R26_RWA_CRM_COVERED = r26_RWA_CRM_COVERED;
	}
	public BigDecimal getR26_ORIG_COUNTERPARTY_RW() {
		return R26_ORIG_COUNTERPARTY_RW;
	}
	public void setR26_ORIG_COUNTERPARTY_RW(BigDecimal r26_ORIG_COUNTERPARTY_RW) {
		R26_ORIG_COUNTERPARTY_RW = r26_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR26_RWA_CRM_NOT_COVERED() {
		return R26_RWA_CRM_NOT_COVERED;
	}
	public void setR26_RWA_CRM_NOT_COVERED(BigDecimal r26_RWA_CRM_NOT_COVERED) {
		R26_RWA_CRM_NOT_COVERED = r26_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR26_CRM_ELIG_EXPOSURE_COMP() {
		return R26_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR26_CRM_ELIG_EXPOSURE_COMP(BigDecimal r26_CRM_ELIG_EXPOSURE_COMP) {
		R26_CRM_ELIG_EXPOSURE_COMP = r26_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR26_EXPOSURE_AFTER_VOL_ADJ() {
		return R26_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR26_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r26_EXPOSURE_AFTER_VOL_ADJ) {
		R26_EXPOSURE_AFTER_VOL_ADJ = r26_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR26_COLLATERAL_CASH() {
		return R26_COLLATERAL_CASH;
	}
	public void setR26_COLLATERAL_CASH(BigDecimal r26_COLLATERAL_CASH) {
		R26_COLLATERAL_CASH = r26_COLLATERAL_CASH;
	}
	public BigDecimal getR26_COLLATERAL_TBILLS() {
		return R26_COLLATERAL_TBILLS;
	}
	public void setR26_COLLATERAL_TBILLS(BigDecimal r26_COLLATERAL_TBILLS) {
		R26_COLLATERAL_TBILLS = r26_COLLATERAL_TBILLS;
	}
	public BigDecimal getR26_COLLATERAL_DEBT_SEC() {
		return R26_COLLATERAL_DEBT_SEC;
	}
	public void setR26_COLLATERAL_DEBT_SEC(BigDecimal r26_COLLATERAL_DEBT_SEC) {
		R26_COLLATERAL_DEBT_SEC = r26_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR26_COLLATERAL_EQUITIES() {
		return R26_COLLATERAL_EQUITIES;
	}
	public void setR26_COLLATERAL_EQUITIES(BigDecimal r26_COLLATERAL_EQUITIES) {
		R26_COLLATERAL_EQUITIES = r26_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR26_COLLATERAL_MUTUAL_FUNDS() {
		return R26_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR26_COLLATERAL_MUTUAL_FUNDS(BigDecimal r26_COLLATERAL_MUTUAL_FUNDS) {
		R26_COLLATERAL_MUTUAL_FUNDS = r26_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR26_TOTAL_COLLATERAL_HAIRCUT() {
		return R26_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR26_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r26_TOTAL_COLLATERAL_HAIRCUT) {
		R26_TOTAL_COLLATERAL_HAIRCUT = r26_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR26_EXPOSURE_AFTER_CRM() {
		return R26_EXPOSURE_AFTER_CRM;
	}
	public void setR26_EXPOSURE_AFTER_CRM(BigDecimal r26_EXPOSURE_AFTER_CRM) {
		R26_EXPOSURE_AFTER_CRM = r26_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR26_RWA_NOT_COVERED_CRM() {
		return R26_RWA_NOT_COVERED_CRM;
	}
	public void setR26_RWA_NOT_COVERED_CRM(BigDecimal r26_RWA_NOT_COVERED_CRM) {
		R26_RWA_NOT_COVERED_CRM = r26_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR26_RWA_UNSECURED_EXPOSURE() {
		return R26_RWA_UNSECURED_EXPOSURE;
	}
	public void setR26_RWA_UNSECURED_EXPOSURE(BigDecimal r26_RWA_UNSECURED_EXPOSURE) {
		R26_RWA_UNSECURED_EXPOSURE = r26_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR26_RWA_UNSECURED() {
		return R26_RWA_UNSECURED;
	}
	public void setR26_RWA_UNSECURED(BigDecimal r26_RWA_UNSECURED) {
		R26_RWA_UNSECURED = r26_RWA_UNSECURED;
	}
	public BigDecimal getR26_TOTAL_RWA() {
		return R26_TOTAL_RWA;
	}
	public void setR26_TOTAL_RWA(BigDecimal r26_TOTAL_RWA) {
		R26_TOTAL_RWA = r26_TOTAL_RWA;
	}
	public BigDecimal getR27_EXPOSURE_BEFORE_CRM() {
		return R27_EXPOSURE_BEFORE_CRM;
	}
	public void setR27_EXPOSURE_BEFORE_CRM(BigDecimal r27_EXPOSURE_BEFORE_CRM) {
		R27_EXPOSURE_BEFORE_CRM = r27_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR27_SPEC_PROVISION_PAST_DUE() {
		return R27_SPEC_PROVISION_PAST_DUE;
	}
	public void setR27_SPEC_PROVISION_PAST_DUE(BigDecimal r27_SPEC_PROVISION_PAST_DUE) {
		R27_SPEC_PROVISION_PAST_DUE = r27_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR27_ON_BAL_SHEET_NETTING_ELIG() {
		return R27_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR27_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r27_ON_BAL_SHEET_NETTING_ELIG) {
		R27_ON_BAL_SHEET_NETTING_ELIG = r27_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR27_TOTAL_EXPOSURE_AFTER_NET() {
		return R27_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR27_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r27_TOTAL_EXPOSURE_AFTER_NET) {
		R27_TOTAL_EXPOSURE_AFTER_NET = r27_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR27_CRM_ELIG_EXPOSURE_SUBS() {
		return R27_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR27_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r27_CRM_ELIG_EXPOSURE_SUBS) {
		R27_CRM_ELIG_EXPOSURE_SUBS = r27_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR27_ELIG_GUARANTEES() {
		return R27_ELIG_GUARANTEES;
	}
	public void setR27_ELIG_GUARANTEES(BigDecimal r27_ELIG_GUARANTEES) {
		R27_ELIG_GUARANTEES = r27_ELIG_GUARANTEES;
	}
	public BigDecimal getR27_CREDIT_DERIVATIVES() {
		return R27_CREDIT_DERIVATIVES;
	}
	public void setR27_CREDIT_DERIVATIVES(BigDecimal r27_CREDIT_DERIVATIVES) {
		R27_CREDIT_DERIVATIVES = r27_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR27_CRM_COVERED_EXPOSURE() {
		return R27_CRM_COVERED_EXPOSURE;
	}
	public void setR27_CRM_COVERED_EXPOSURE(BigDecimal r27_CRM_COVERED_EXPOSURE) {
		R27_CRM_COVERED_EXPOSURE = r27_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR27_CRM_NOT_COVERED_EXPOSURE() {
		return R27_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR27_CRM_NOT_COVERED_EXPOSURE(BigDecimal r27_CRM_NOT_COVERED_EXPOSURE) {
		R27_CRM_NOT_COVERED_EXPOSURE = r27_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR27_CRM_RISK_WEIGHT() {
		return R27_CRM_RISK_WEIGHT;
	}
	public void setR27_CRM_RISK_WEIGHT(BigDecimal r27_CRM_RISK_WEIGHT) {
		R27_CRM_RISK_WEIGHT = r27_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR27_RWA_CRM_COVERED() {
		return R27_RWA_CRM_COVERED;
	}
	public void setR27_RWA_CRM_COVERED(BigDecimal r27_RWA_CRM_COVERED) {
		R27_RWA_CRM_COVERED = r27_RWA_CRM_COVERED;
	}
	public BigDecimal getR27_ORIG_COUNTERPARTY_RW() {
		return R27_ORIG_COUNTERPARTY_RW;
	}
	public void setR27_ORIG_COUNTERPARTY_RW(BigDecimal r27_ORIG_COUNTERPARTY_RW) {
		R27_ORIG_COUNTERPARTY_RW = r27_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR27_RWA_CRM_NOT_COVERED() {
		return R27_RWA_CRM_NOT_COVERED;
	}
	public void setR27_RWA_CRM_NOT_COVERED(BigDecimal r27_RWA_CRM_NOT_COVERED) {
		R27_RWA_CRM_NOT_COVERED = r27_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR27_CRM_ELIG_EXPOSURE_COMP() {
		return R27_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR27_CRM_ELIG_EXPOSURE_COMP(BigDecimal r27_CRM_ELIG_EXPOSURE_COMP) {
		R27_CRM_ELIG_EXPOSURE_COMP = r27_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR27_EXPOSURE_AFTER_VOL_ADJ() {
		return R27_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR27_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r27_EXPOSURE_AFTER_VOL_ADJ) {
		R27_EXPOSURE_AFTER_VOL_ADJ = r27_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR27_COLLATERAL_CASH() {
		return R27_COLLATERAL_CASH;
	}
	public void setR27_COLLATERAL_CASH(BigDecimal r27_COLLATERAL_CASH) {
		R27_COLLATERAL_CASH = r27_COLLATERAL_CASH;
	}
	public BigDecimal getR27_COLLATERAL_TBILLS() {
		return R27_COLLATERAL_TBILLS;
	}
	public void setR27_COLLATERAL_TBILLS(BigDecimal r27_COLLATERAL_TBILLS) {
		R27_COLLATERAL_TBILLS = r27_COLLATERAL_TBILLS;
	}
	public BigDecimal getR27_COLLATERAL_DEBT_SEC() {
		return R27_COLLATERAL_DEBT_SEC;
	}
	public void setR27_COLLATERAL_DEBT_SEC(BigDecimal r27_COLLATERAL_DEBT_SEC) {
		R27_COLLATERAL_DEBT_SEC = r27_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR27_COLLATERAL_EQUITIES() {
		return R27_COLLATERAL_EQUITIES;
	}
	public void setR27_COLLATERAL_EQUITIES(BigDecimal r27_COLLATERAL_EQUITIES) {
		R27_COLLATERAL_EQUITIES = r27_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR27_COLLATERAL_MUTUAL_FUNDS() {
		return R27_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR27_COLLATERAL_MUTUAL_FUNDS(BigDecimal r27_COLLATERAL_MUTUAL_FUNDS) {
		R27_COLLATERAL_MUTUAL_FUNDS = r27_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR27_TOTAL_COLLATERAL_HAIRCUT() {
		return R27_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR27_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r27_TOTAL_COLLATERAL_HAIRCUT) {
		R27_TOTAL_COLLATERAL_HAIRCUT = r27_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR27_EXPOSURE_AFTER_CRM() {
		return R27_EXPOSURE_AFTER_CRM;
	}
	public void setR27_EXPOSURE_AFTER_CRM(BigDecimal r27_EXPOSURE_AFTER_CRM) {
		R27_EXPOSURE_AFTER_CRM = r27_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR27_RWA_NOT_COVERED_CRM() {
		return R27_RWA_NOT_COVERED_CRM;
	}
	public void setR27_RWA_NOT_COVERED_CRM(BigDecimal r27_RWA_NOT_COVERED_CRM) {
		R27_RWA_NOT_COVERED_CRM = r27_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR27_RWA_UNSECURED_EXPOSURE() {
		return R27_RWA_UNSECURED_EXPOSURE;
	}
	public void setR27_RWA_UNSECURED_EXPOSURE(BigDecimal r27_RWA_UNSECURED_EXPOSURE) {
		R27_RWA_UNSECURED_EXPOSURE = r27_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR27_RWA_UNSECURED() {
		return R27_RWA_UNSECURED;
	}
	public void setR27_RWA_UNSECURED(BigDecimal r27_RWA_UNSECURED) {
		R27_RWA_UNSECURED = r27_RWA_UNSECURED;
	}
	public BigDecimal getR27_TOTAL_RWA() {
		return R27_TOTAL_RWA;
	}
	public void setR27_TOTAL_RWA(BigDecimal r27_TOTAL_RWA) {
		R27_TOTAL_RWA = r27_TOTAL_RWA;
	}
	public BigDecimal getR28_EXPOSURE_BEFORE_CRM() {
		return R28_EXPOSURE_BEFORE_CRM;
	}
	public void setR28_EXPOSURE_BEFORE_CRM(BigDecimal r28_EXPOSURE_BEFORE_CRM) {
		R28_EXPOSURE_BEFORE_CRM = r28_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR28_SPEC_PROVISION_PAST_DUE() {
		return R28_SPEC_PROVISION_PAST_DUE;
	}
	public void setR28_SPEC_PROVISION_PAST_DUE(BigDecimal r28_SPEC_PROVISION_PAST_DUE) {
		R28_SPEC_PROVISION_PAST_DUE = r28_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR28_ON_BAL_SHEET_NETTING_ELIG() {
		return R28_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR28_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r28_ON_BAL_SHEET_NETTING_ELIG) {
		R28_ON_BAL_SHEET_NETTING_ELIG = r28_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR28_TOTAL_EXPOSURE_AFTER_NET() {
		return R28_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR28_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r28_TOTAL_EXPOSURE_AFTER_NET) {
		R28_TOTAL_EXPOSURE_AFTER_NET = r28_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR28_CRM_ELIG_EXPOSURE_SUBS() {
		return R28_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR28_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r28_CRM_ELIG_EXPOSURE_SUBS) {
		R28_CRM_ELIG_EXPOSURE_SUBS = r28_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR28_ELIG_GUARANTEES() {
		return R28_ELIG_GUARANTEES;
	}
	public void setR28_ELIG_GUARANTEES(BigDecimal r28_ELIG_GUARANTEES) {
		R28_ELIG_GUARANTEES = r28_ELIG_GUARANTEES;
	}
	public BigDecimal getR28_CREDIT_DERIVATIVES() {
		return R28_CREDIT_DERIVATIVES;
	}
	public void setR28_CREDIT_DERIVATIVES(BigDecimal r28_CREDIT_DERIVATIVES) {
		R28_CREDIT_DERIVATIVES = r28_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR28_CRM_COVERED_EXPOSURE() {
		return R28_CRM_COVERED_EXPOSURE;
	}
	public void setR28_CRM_COVERED_EXPOSURE(BigDecimal r28_CRM_COVERED_EXPOSURE) {
		R28_CRM_COVERED_EXPOSURE = r28_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR28_CRM_NOT_COVERED_EXPOSURE() {
		return R28_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR28_CRM_NOT_COVERED_EXPOSURE(BigDecimal r28_CRM_NOT_COVERED_EXPOSURE) {
		R28_CRM_NOT_COVERED_EXPOSURE = r28_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR28_CRM_RISK_WEIGHT() {
		return R28_CRM_RISK_WEIGHT;
	}
	public void setR28_CRM_RISK_WEIGHT(BigDecimal r28_CRM_RISK_WEIGHT) {
		R28_CRM_RISK_WEIGHT = r28_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR28_RWA_CRM_COVERED() {
		return R28_RWA_CRM_COVERED;
	}
	public void setR28_RWA_CRM_COVERED(BigDecimal r28_RWA_CRM_COVERED) {
		R28_RWA_CRM_COVERED = r28_RWA_CRM_COVERED;
	}
	public BigDecimal getR28_ORIG_COUNTERPARTY_RW() {
		return R28_ORIG_COUNTERPARTY_RW;
	}
	public void setR28_ORIG_COUNTERPARTY_RW(BigDecimal r28_ORIG_COUNTERPARTY_RW) {
		R28_ORIG_COUNTERPARTY_RW = r28_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR28_RWA_CRM_NOT_COVERED() {
		return R28_RWA_CRM_NOT_COVERED;
	}
	public void setR28_RWA_CRM_NOT_COVERED(BigDecimal r28_RWA_CRM_NOT_COVERED) {
		R28_RWA_CRM_NOT_COVERED = r28_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR28_CRM_ELIG_EXPOSURE_COMP() {
		return R28_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR28_CRM_ELIG_EXPOSURE_COMP(BigDecimal r28_CRM_ELIG_EXPOSURE_COMP) {
		R28_CRM_ELIG_EXPOSURE_COMP = r28_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR28_EXPOSURE_AFTER_VOL_ADJ() {
		return R28_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR28_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r28_EXPOSURE_AFTER_VOL_ADJ) {
		R28_EXPOSURE_AFTER_VOL_ADJ = r28_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR28_COLLATERAL_CASH() {
		return R28_COLLATERAL_CASH;
	}
	public void setR28_COLLATERAL_CASH(BigDecimal r28_COLLATERAL_CASH) {
		R28_COLLATERAL_CASH = r28_COLLATERAL_CASH;
	}
	public BigDecimal getR28_COLLATERAL_TBILLS() {
		return R28_COLLATERAL_TBILLS;
	}
	public void setR28_COLLATERAL_TBILLS(BigDecimal r28_COLLATERAL_TBILLS) {
		R28_COLLATERAL_TBILLS = r28_COLLATERAL_TBILLS;
	}
	public BigDecimal getR28_COLLATERAL_DEBT_SEC() {
		return R28_COLLATERAL_DEBT_SEC;
	}
	public void setR28_COLLATERAL_DEBT_SEC(BigDecimal r28_COLLATERAL_DEBT_SEC) {
		R28_COLLATERAL_DEBT_SEC = r28_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR28_COLLATERAL_EQUITIES() {
		return R28_COLLATERAL_EQUITIES;
	}
	public void setR28_COLLATERAL_EQUITIES(BigDecimal r28_COLLATERAL_EQUITIES) {
		R28_COLLATERAL_EQUITIES = r28_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR28_COLLATERAL_MUTUAL_FUNDS() {
		return R28_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR28_COLLATERAL_MUTUAL_FUNDS(BigDecimal r28_COLLATERAL_MUTUAL_FUNDS) {
		R28_COLLATERAL_MUTUAL_FUNDS = r28_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR28_TOTAL_COLLATERAL_HAIRCUT() {
		return R28_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR28_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r28_TOTAL_COLLATERAL_HAIRCUT) {
		R28_TOTAL_COLLATERAL_HAIRCUT = r28_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR28_EXPOSURE_AFTER_CRM() {
		return R28_EXPOSURE_AFTER_CRM;
	}
	public void setR28_EXPOSURE_AFTER_CRM(BigDecimal r28_EXPOSURE_AFTER_CRM) {
		R28_EXPOSURE_AFTER_CRM = r28_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR28_RWA_NOT_COVERED_CRM() {
		return R28_RWA_NOT_COVERED_CRM;
	}
	public void setR28_RWA_NOT_COVERED_CRM(BigDecimal r28_RWA_NOT_COVERED_CRM) {
		R28_RWA_NOT_COVERED_CRM = r28_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR28_RWA_UNSECURED_EXPOSURE() {
		return R28_RWA_UNSECURED_EXPOSURE;
	}
	public void setR28_RWA_UNSECURED_EXPOSURE(BigDecimal r28_RWA_UNSECURED_EXPOSURE) {
		R28_RWA_UNSECURED_EXPOSURE = r28_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR28_RWA_UNSECURED() {
		return R28_RWA_UNSECURED;
	}
	public void setR28_RWA_UNSECURED(BigDecimal r28_RWA_UNSECURED) {
		R28_RWA_UNSECURED = r28_RWA_UNSECURED;
	}
	public BigDecimal getR28_TOTAL_RWA() {
		return R28_TOTAL_RWA;
	}
	public void setR28_TOTAL_RWA(BigDecimal r28_TOTAL_RWA) {
		R28_TOTAL_RWA = r28_TOTAL_RWA;
	}
	public BigDecimal getR29_EXPOSURE_BEFORE_CRM() {
		return R29_EXPOSURE_BEFORE_CRM;
	}
	public void setR29_EXPOSURE_BEFORE_CRM(BigDecimal r29_EXPOSURE_BEFORE_CRM) {
		R29_EXPOSURE_BEFORE_CRM = r29_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR29_SPEC_PROVISION_PAST_DUE() {
		return R29_SPEC_PROVISION_PAST_DUE;
	}
	public void setR29_SPEC_PROVISION_PAST_DUE(BigDecimal r29_SPEC_PROVISION_PAST_DUE) {
		R29_SPEC_PROVISION_PAST_DUE = r29_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR29_ON_BAL_SHEET_NETTING_ELIG() {
		return R29_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR29_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r29_ON_BAL_SHEET_NETTING_ELIG) {
		R29_ON_BAL_SHEET_NETTING_ELIG = r29_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR29_TOTAL_EXPOSURE_AFTER_NET() {
		return R29_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR29_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r29_TOTAL_EXPOSURE_AFTER_NET) {
		R29_TOTAL_EXPOSURE_AFTER_NET = r29_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR29_CRM_ELIG_EXPOSURE_SUBS() {
		return R29_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR29_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r29_CRM_ELIG_EXPOSURE_SUBS) {
		R29_CRM_ELIG_EXPOSURE_SUBS = r29_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR29_ELIG_GUARANTEES() {
		return R29_ELIG_GUARANTEES;
	}
	public void setR29_ELIG_GUARANTEES(BigDecimal r29_ELIG_GUARANTEES) {
		R29_ELIG_GUARANTEES = r29_ELIG_GUARANTEES;
	}
	public BigDecimal getR29_CREDIT_DERIVATIVES() {
		return R29_CREDIT_DERIVATIVES;
	}
	public void setR29_CREDIT_DERIVATIVES(BigDecimal r29_CREDIT_DERIVATIVES) {
		R29_CREDIT_DERIVATIVES = r29_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR29_CRM_COVERED_EXPOSURE() {
		return R29_CRM_COVERED_EXPOSURE;
	}
	public void setR29_CRM_COVERED_EXPOSURE(BigDecimal r29_CRM_COVERED_EXPOSURE) {
		R29_CRM_COVERED_EXPOSURE = r29_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR29_CRM_NOT_COVERED_EXPOSURE() {
		return R29_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR29_CRM_NOT_COVERED_EXPOSURE(BigDecimal r29_CRM_NOT_COVERED_EXPOSURE) {
		R29_CRM_NOT_COVERED_EXPOSURE = r29_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR29_CRM_RISK_WEIGHT() {
		return R29_CRM_RISK_WEIGHT;
	}
	public void setR29_CRM_RISK_WEIGHT(BigDecimal r29_CRM_RISK_WEIGHT) {
		R29_CRM_RISK_WEIGHT = r29_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR29_RWA_CRM_COVERED() {
		return R29_RWA_CRM_COVERED;
	}
	public void setR29_RWA_CRM_COVERED(BigDecimal r29_RWA_CRM_COVERED) {
		R29_RWA_CRM_COVERED = r29_RWA_CRM_COVERED;
	}
	public BigDecimal getR29_ORIG_COUNTERPARTY_RW() {
		return R29_ORIG_COUNTERPARTY_RW;
	}
	public void setR29_ORIG_COUNTERPARTY_RW(BigDecimal r29_ORIG_COUNTERPARTY_RW) {
		R29_ORIG_COUNTERPARTY_RW = r29_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR29_RWA_CRM_NOT_COVERED() {
		return R29_RWA_CRM_NOT_COVERED;
	}
	public void setR29_RWA_CRM_NOT_COVERED(BigDecimal r29_RWA_CRM_NOT_COVERED) {
		R29_RWA_CRM_NOT_COVERED = r29_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR29_CRM_ELIG_EXPOSURE_COMP() {
		return R29_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR29_CRM_ELIG_EXPOSURE_COMP(BigDecimal r29_CRM_ELIG_EXPOSURE_COMP) {
		R29_CRM_ELIG_EXPOSURE_COMP = r29_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR29_EXPOSURE_AFTER_VOL_ADJ() {
		return R29_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR29_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r29_EXPOSURE_AFTER_VOL_ADJ) {
		R29_EXPOSURE_AFTER_VOL_ADJ = r29_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR29_COLLATERAL_CASH() {
		return R29_COLLATERAL_CASH;
	}
	public void setR29_COLLATERAL_CASH(BigDecimal r29_COLLATERAL_CASH) {
		R29_COLLATERAL_CASH = r29_COLLATERAL_CASH;
	}
	public BigDecimal getR29_COLLATERAL_TBILLS() {
		return R29_COLLATERAL_TBILLS;
	}
	public void setR29_COLLATERAL_TBILLS(BigDecimal r29_COLLATERAL_TBILLS) {
		R29_COLLATERAL_TBILLS = r29_COLLATERAL_TBILLS;
	}
	public BigDecimal getR29_COLLATERAL_DEBT_SEC() {
		return R29_COLLATERAL_DEBT_SEC;
	}
	public void setR29_COLLATERAL_DEBT_SEC(BigDecimal r29_COLLATERAL_DEBT_SEC) {
		R29_COLLATERAL_DEBT_SEC = r29_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR29_COLLATERAL_EQUITIES() {
		return R29_COLLATERAL_EQUITIES;
	}
	public void setR29_COLLATERAL_EQUITIES(BigDecimal r29_COLLATERAL_EQUITIES) {
		R29_COLLATERAL_EQUITIES = r29_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR29_COLLATERAL_MUTUAL_FUNDS() {
		return R29_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR29_COLLATERAL_MUTUAL_FUNDS(BigDecimal r29_COLLATERAL_MUTUAL_FUNDS) {
		R29_COLLATERAL_MUTUAL_FUNDS = r29_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR29_TOTAL_COLLATERAL_HAIRCUT() {
		return R29_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR29_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r29_TOTAL_COLLATERAL_HAIRCUT) {
		R29_TOTAL_COLLATERAL_HAIRCUT = r29_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR29_EXPOSURE_AFTER_CRM() {
		return R29_EXPOSURE_AFTER_CRM;
	}
	public void setR29_EXPOSURE_AFTER_CRM(BigDecimal r29_EXPOSURE_AFTER_CRM) {
		R29_EXPOSURE_AFTER_CRM = r29_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR29_RWA_NOT_COVERED_CRM() {
		return R29_RWA_NOT_COVERED_CRM;
	}
	public void setR29_RWA_NOT_COVERED_CRM(BigDecimal r29_RWA_NOT_COVERED_CRM) {
		R29_RWA_NOT_COVERED_CRM = r29_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR29_RWA_UNSECURED_EXPOSURE() {
		return R29_RWA_UNSECURED_EXPOSURE;
	}
	public void setR29_RWA_UNSECURED_EXPOSURE(BigDecimal r29_RWA_UNSECURED_EXPOSURE) {
		R29_RWA_UNSECURED_EXPOSURE = r29_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR29_RWA_UNSECURED() {
		return R29_RWA_UNSECURED;
	}
	public void setR29_RWA_UNSECURED(BigDecimal r29_RWA_UNSECURED) {
		R29_RWA_UNSECURED = r29_RWA_UNSECURED;
	}
	public BigDecimal getR29_TOTAL_RWA() {
		return R29_TOTAL_RWA;
	}
	public void setR29_TOTAL_RWA(BigDecimal r29_TOTAL_RWA) {
		R29_TOTAL_RWA = r29_TOTAL_RWA;
	}
	public BigDecimal getR30_EXPOSURE_BEFORE_CRM() {
		return R30_EXPOSURE_BEFORE_CRM;
	}
	public void setR30_EXPOSURE_BEFORE_CRM(BigDecimal r30_EXPOSURE_BEFORE_CRM) {
		R30_EXPOSURE_BEFORE_CRM = r30_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR30_SPEC_PROVISION_PAST_DUE() {
		return R30_SPEC_PROVISION_PAST_DUE;
	}
	public void setR30_SPEC_PROVISION_PAST_DUE(BigDecimal r30_SPEC_PROVISION_PAST_DUE) {
		R30_SPEC_PROVISION_PAST_DUE = r30_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR30_ON_BAL_SHEET_NETTING_ELIG() {
		return R30_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR30_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r30_ON_BAL_SHEET_NETTING_ELIG) {
		R30_ON_BAL_SHEET_NETTING_ELIG = r30_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR30_TOTAL_EXPOSURE_AFTER_NET() {
		return R30_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR30_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r30_TOTAL_EXPOSURE_AFTER_NET) {
		R30_TOTAL_EXPOSURE_AFTER_NET = r30_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR30_CRM_ELIG_EXPOSURE_SUBS() {
		return R30_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR30_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r30_CRM_ELIG_EXPOSURE_SUBS) {
		R30_CRM_ELIG_EXPOSURE_SUBS = r30_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR30_ELIG_GUARANTEES() {
		return R30_ELIG_GUARANTEES;
	}
	public void setR30_ELIG_GUARANTEES(BigDecimal r30_ELIG_GUARANTEES) {
		R30_ELIG_GUARANTEES = r30_ELIG_GUARANTEES;
	}
	public BigDecimal getR30_CREDIT_DERIVATIVES() {
		return R30_CREDIT_DERIVATIVES;
	}
	public void setR30_CREDIT_DERIVATIVES(BigDecimal r30_CREDIT_DERIVATIVES) {
		R30_CREDIT_DERIVATIVES = r30_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR30_CRM_COVERED_EXPOSURE() {
		return R30_CRM_COVERED_EXPOSURE;
	}
	public void setR30_CRM_COVERED_EXPOSURE(BigDecimal r30_CRM_COVERED_EXPOSURE) {
		R30_CRM_COVERED_EXPOSURE = r30_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR30_CRM_NOT_COVERED_EXPOSURE() {
		return R30_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR30_CRM_NOT_COVERED_EXPOSURE(BigDecimal r30_CRM_NOT_COVERED_EXPOSURE) {
		R30_CRM_NOT_COVERED_EXPOSURE = r30_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR30_CRM_RISK_WEIGHT() {
		return R30_CRM_RISK_WEIGHT;
	}
	public void setR30_CRM_RISK_WEIGHT(BigDecimal r30_CRM_RISK_WEIGHT) {
		R30_CRM_RISK_WEIGHT = r30_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR30_RWA_CRM_COVERED() {
		return R30_RWA_CRM_COVERED;
	}
	public void setR30_RWA_CRM_COVERED(BigDecimal r30_RWA_CRM_COVERED) {
		R30_RWA_CRM_COVERED = r30_RWA_CRM_COVERED;
	}
	public BigDecimal getR30_ORIG_COUNTERPARTY_RW() {
		return R30_ORIG_COUNTERPARTY_RW;
	}
	public void setR30_ORIG_COUNTERPARTY_RW(BigDecimal r30_ORIG_COUNTERPARTY_RW) {
		R30_ORIG_COUNTERPARTY_RW = r30_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR30_RWA_CRM_NOT_COVERED() {
		return R30_RWA_CRM_NOT_COVERED;
	}
	public void setR30_RWA_CRM_NOT_COVERED(BigDecimal r30_RWA_CRM_NOT_COVERED) {
		R30_RWA_CRM_NOT_COVERED = r30_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR30_CRM_ELIG_EXPOSURE_COMP() {
		return R30_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR30_CRM_ELIG_EXPOSURE_COMP(BigDecimal r30_CRM_ELIG_EXPOSURE_COMP) {
		R30_CRM_ELIG_EXPOSURE_COMP = r30_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR30_EXPOSURE_AFTER_VOL_ADJ() {
		return R30_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR30_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r30_EXPOSURE_AFTER_VOL_ADJ) {
		R30_EXPOSURE_AFTER_VOL_ADJ = r30_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR30_COLLATERAL_CASH() {
		return R30_COLLATERAL_CASH;
	}
	public void setR30_COLLATERAL_CASH(BigDecimal r30_COLLATERAL_CASH) {
		R30_COLLATERAL_CASH = r30_COLLATERAL_CASH;
	}
	public BigDecimal getR30_COLLATERAL_TBILLS() {
		return R30_COLLATERAL_TBILLS;
	}
	public void setR30_COLLATERAL_TBILLS(BigDecimal r30_COLLATERAL_TBILLS) {
		R30_COLLATERAL_TBILLS = r30_COLLATERAL_TBILLS;
	}
	public BigDecimal getR30_COLLATERAL_DEBT_SEC() {
		return R30_COLLATERAL_DEBT_SEC;
	}
	public void setR30_COLLATERAL_DEBT_SEC(BigDecimal r30_COLLATERAL_DEBT_SEC) {
		R30_COLLATERAL_DEBT_SEC = r30_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR30_COLLATERAL_EQUITIES() {
		return R30_COLLATERAL_EQUITIES;
	}
	public void setR30_COLLATERAL_EQUITIES(BigDecimal r30_COLLATERAL_EQUITIES) {
		R30_COLLATERAL_EQUITIES = r30_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR30_COLLATERAL_MUTUAL_FUNDS() {
		return R30_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR30_COLLATERAL_MUTUAL_FUNDS(BigDecimal r30_COLLATERAL_MUTUAL_FUNDS) {
		R30_COLLATERAL_MUTUAL_FUNDS = r30_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR30_TOTAL_COLLATERAL_HAIRCUT() {
		return R30_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR30_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r30_TOTAL_COLLATERAL_HAIRCUT) {
		R30_TOTAL_COLLATERAL_HAIRCUT = r30_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR30_EXPOSURE_AFTER_CRM() {
		return R30_EXPOSURE_AFTER_CRM;
	}
	public void setR30_EXPOSURE_AFTER_CRM(BigDecimal r30_EXPOSURE_AFTER_CRM) {
		R30_EXPOSURE_AFTER_CRM = r30_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR30_RWA_NOT_COVERED_CRM() {
		return R30_RWA_NOT_COVERED_CRM;
	}
	public void setR30_RWA_NOT_COVERED_CRM(BigDecimal r30_RWA_NOT_COVERED_CRM) {
		R30_RWA_NOT_COVERED_CRM = r30_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR30_RWA_UNSECURED_EXPOSURE() {
		return R30_RWA_UNSECURED_EXPOSURE;
	}
	public void setR30_RWA_UNSECURED_EXPOSURE(BigDecimal r30_RWA_UNSECURED_EXPOSURE) {
		R30_RWA_UNSECURED_EXPOSURE = r30_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR30_RWA_UNSECURED() {
		return R30_RWA_UNSECURED;
	}
	public void setR30_RWA_UNSECURED(BigDecimal r30_RWA_UNSECURED) {
		R30_RWA_UNSECURED = r30_RWA_UNSECURED;
	}
	public BigDecimal getR30_TOTAL_RWA() {
		return R30_TOTAL_RWA;
	}
	public void setR30_TOTAL_RWA(BigDecimal r30_TOTAL_RWA) {
		R30_TOTAL_RWA = r30_TOTAL_RWA;
	}
	public BigDecimal getR31_EXPOSURE_BEFORE_CRM() {
		return R31_EXPOSURE_BEFORE_CRM;
	}
	public void setR31_EXPOSURE_BEFORE_CRM(BigDecimal r31_EXPOSURE_BEFORE_CRM) {
		R31_EXPOSURE_BEFORE_CRM = r31_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR31_SPEC_PROVISION_PAST_DUE() {
		return R31_SPEC_PROVISION_PAST_DUE;
	}
	public void setR31_SPEC_PROVISION_PAST_DUE(BigDecimal r31_SPEC_PROVISION_PAST_DUE) {
		R31_SPEC_PROVISION_PAST_DUE = r31_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR31_ON_BAL_SHEET_NETTING_ELIG() {
		return R31_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR31_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r31_ON_BAL_SHEET_NETTING_ELIG) {
		R31_ON_BAL_SHEET_NETTING_ELIG = r31_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR31_TOTAL_EXPOSURE_AFTER_NET() {
		return R31_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR31_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r31_TOTAL_EXPOSURE_AFTER_NET) {
		R31_TOTAL_EXPOSURE_AFTER_NET = r31_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR31_CRM_ELIG_EXPOSURE_SUBS() {
		return R31_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR31_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r31_CRM_ELIG_EXPOSURE_SUBS) {
		R31_CRM_ELIG_EXPOSURE_SUBS = r31_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR31_ELIG_GUARANTEES() {
		return R31_ELIG_GUARANTEES;
	}
	public void setR31_ELIG_GUARANTEES(BigDecimal r31_ELIG_GUARANTEES) {
		R31_ELIG_GUARANTEES = r31_ELIG_GUARANTEES;
	}
	public BigDecimal getR31_CREDIT_DERIVATIVES() {
		return R31_CREDIT_DERIVATIVES;
	}
	public void setR31_CREDIT_DERIVATIVES(BigDecimal r31_CREDIT_DERIVATIVES) {
		R31_CREDIT_DERIVATIVES = r31_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR31_CRM_COVERED_EXPOSURE() {
		return R31_CRM_COVERED_EXPOSURE;
	}
	public void setR31_CRM_COVERED_EXPOSURE(BigDecimal r31_CRM_COVERED_EXPOSURE) {
		R31_CRM_COVERED_EXPOSURE = r31_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR31_CRM_NOT_COVERED_EXPOSURE() {
		return R31_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR31_CRM_NOT_COVERED_EXPOSURE(BigDecimal r31_CRM_NOT_COVERED_EXPOSURE) {
		R31_CRM_NOT_COVERED_EXPOSURE = r31_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR31_CRM_RISK_WEIGHT() {
		return R31_CRM_RISK_WEIGHT;
	}
	public void setR31_CRM_RISK_WEIGHT(BigDecimal r31_CRM_RISK_WEIGHT) {
		R31_CRM_RISK_WEIGHT = r31_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR31_RWA_CRM_COVERED() {
		return R31_RWA_CRM_COVERED;
	}
	public void setR31_RWA_CRM_COVERED(BigDecimal r31_RWA_CRM_COVERED) {
		R31_RWA_CRM_COVERED = r31_RWA_CRM_COVERED;
	}
	public BigDecimal getR31_ORIG_COUNTERPARTY_RW() {
		return R31_ORIG_COUNTERPARTY_RW;
	}
	public void setR31_ORIG_COUNTERPARTY_RW(BigDecimal r31_ORIG_COUNTERPARTY_RW) {
		R31_ORIG_COUNTERPARTY_RW = r31_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR31_RWA_CRM_NOT_COVERED() {
		return R31_RWA_CRM_NOT_COVERED;
	}
	public void setR31_RWA_CRM_NOT_COVERED(BigDecimal r31_RWA_CRM_NOT_COVERED) {
		R31_RWA_CRM_NOT_COVERED = r31_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR31_CRM_ELIG_EXPOSURE_COMP() {
		return R31_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR31_CRM_ELIG_EXPOSURE_COMP(BigDecimal r31_CRM_ELIG_EXPOSURE_COMP) {
		R31_CRM_ELIG_EXPOSURE_COMP = r31_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR31_EXPOSURE_AFTER_VOL_ADJ() {
		return R31_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR31_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r31_EXPOSURE_AFTER_VOL_ADJ) {
		R31_EXPOSURE_AFTER_VOL_ADJ = r31_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR31_COLLATERAL_CASH() {
		return R31_COLLATERAL_CASH;
	}
	public void setR31_COLLATERAL_CASH(BigDecimal r31_COLLATERAL_CASH) {
		R31_COLLATERAL_CASH = r31_COLLATERAL_CASH;
	}
	public BigDecimal getR31_COLLATERAL_TBILLS() {
		return R31_COLLATERAL_TBILLS;
	}
	public void setR31_COLLATERAL_TBILLS(BigDecimal r31_COLLATERAL_TBILLS) {
		R31_COLLATERAL_TBILLS = r31_COLLATERAL_TBILLS;
	}
	public BigDecimal getR31_COLLATERAL_DEBT_SEC() {
		return R31_COLLATERAL_DEBT_SEC;
	}
	public void setR31_COLLATERAL_DEBT_SEC(BigDecimal r31_COLLATERAL_DEBT_SEC) {
		R31_COLLATERAL_DEBT_SEC = r31_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR31_COLLATERAL_EQUITIES() {
		return R31_COLLATERAL_EQUITIES;
	}
	public void setR31_COLLATERAL_EQUITIES(BigDecimal r31_COLLATERAL_EQUITIES) {
		R31_COLLATERAL_EQUITIES = r31_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR31_COLLATERAL_MUTUAL_FUNDS() {
		return R31_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR31_COLLATERAL_MUTUAL_FUNDS(BigDecimal r31_COLLATERAL_MUTUAL_FUNDS) {
		R31_COLLATERAL_MUTUAL_FUNDS = r31_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR31_TOTAL_COLLATERAL_HAIRCUT() {
		return R31_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR31_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r31_TOTAL_COLLATERAL_HAIRCUT) {
		R31_TOTAL_COLLATERAL_HAIRCUT = r31_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR31_EXPOSURE_AFTER_CRM() {
		return R31_EXPOSURE_AFTER_CRM;
	}
	public void setR31_EXPOSURE_AFTER_CRM(BigDecimal r31_EXPOSURE_AFTER_CRM) {
		R31_EXPOSURE_AFTER_CRM = r31_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR31_RWA_NOT_COVERED_CRM() {
		return R31_RWA_NOT_COVERED_CRM;
	}
	public void setR31_RWA_NOT_COVERED_CRM(BigDecimal r31_RWA_NOT_COVERED_CRM) {
		R31_RWA_NOT_COVERED_CRM = r31_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR31_RWA_UNSECURED_EXPOSURE() {
		return R31_RWA_UNSECURED_EXPOSURE;
	}
	public void setR31_RWA_UNSECURED_EXPOSURE(BigDecimal r31_RWA_UNSECURED_EXPOSURE) {
		R31_RWA_UNSECURED_EXPOSURE = r31_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR31_RWA_UNSECURED() {
		return R31_RWA_UNSECURED;
	}
	public void setR31_RWA_UNSECURED(BigDecimal r31_RWA_UNSECURED) {
		R31_RWA_UNSECURED = r31_RWA_UNSECURED;
	}
	public BigDecimal getR31_TOTAL_RWA() {
		return R31_TOTAL_RWA;
	}
	public void setR31_TOTAL_RWA(BigDecimal r31_TOTAL_RWA) {
		R31_TOTAL_RWA = r31_TOTAL_RWA;
	}
	public BigDecimal getR32_EXPOSURE_BEFORE_CRM() {
		return R32_EXPOSURE_BEFORE_CRM;
	}
	public void setR32_EXPOSURE_BEFORE_CRM(BigDecimal r32_EXPOSURE_BEFORE_CRM) {
		R32_EXPOSURE_BEFORE_CRM = r32_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR32_SPEC_PROVISION_PAST_DUE() {
		return R32_SPEC_PROVISION_PAST_DUE;
	}
	public void setR32_SPEC_PROVISION_PAST_DUE(BigDecimal r32_SPEC_PROVISION_PAST_DUE) {
		R32_SPEC_PROVISION_PAST_DUE = r32_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR32_ON_BAL_SHEET_NETTING_ELIG() {
		return R32_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR32_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r32_ON_BAL_SHEET_NETTING_ELIG) {
		R32_ON_BAL_SHEET_NETTING_ELIG = r32_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR32_TOTAL_EXPOSURE_AFTER_NET() {
		return R32_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR32_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r32_TOTAL_EXPOSURE_AFTER_NET) {
		R32_TOTAL_EXPOSURE_AFTER_NET = r32_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR32_CRM_ELIG_EXPOSURE_SUBS() {
		return R32_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR32_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r32_CRM_ELIG_EXPOSURE_SUBS) {
		R32_CRM_ELIG_EXPOSURE_SUBS = r32_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR32_ELIG_GUARANTEES() {
		return R32_ELIG_GUARANTEES;
	}
	public void setR32_ELIG_GUARANTEES(BigDecimal r32_ELIG_GUARANTEES) {
		R32_ELIG_GUARANTEES = r32_ELIG_GUARANTEES;
	}
	public BigDecimal getR32_CREDIT_DERIVATIVES() {
		return R32_CREDIT_DERIVATIVES;
	}
	public void setR32_CREDIT_DERIVATIVES(BigDecimal r32_CREDIT_DERIVATIVES) {
		R32_CREDIT_DERIVATIVES = r32_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR32_CRM_COVERED_EXPOSURE() {
		return R32_CRM_COVERED_EXPOSURE;
	}
	public void setR32_CRM_COVERED_EXPOSURE(BigDecimal r32_CRM_COVERED_EXPOSURE) {
		R32_CRM_COVERED_EXPOSURE = r32_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR32_CRM_NOT_COVERED_EXPOSURE() {
		return R32_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR32_CRM_NOT_COVERED_EXPOSURE(BigDecimal r32_CRM_NOT_COVERED_EXPOSURE) {
		R32_CRM_NOT_COVERED_EXPOSURE = r32_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR32_CRM_RISK_WEIGHT() {
		return R32_CRM_RISK_WEIGHT;
	}
	public void setR32_CRM_RISK_WEIGHT(BigDecimal r32_CRM_RISK_WEIGHT) {
		R32_CRM_RISK_WEIGHT = r32_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR32_RWA_CRM_COVERED() {
		return R32_RWA_CRM_COVERED;
	}
	public void setR32_RWA_CRM_COVERED(BigDecimal r32_RWA_CRM_COVERED) {
		R32_RWA_CRM_COVERED = r32_RWA_CRM_COVERED;
	}
	public BigDecimal getR32_ORIG_COUNTERPARTY_RW() {
		return R32_ORIG_COUNTERPARTY_RW;
	}
	public void setR32_ORIG_COUNTERPARTY_RW(BigDecimal r32_ORIG_COUNTERPARTY_RW) {
		R32_ORIG_COUNTERPARTY_RW = r32_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR32_RWA_CRM_NOT_COVERED() {
		return R32_RWA_CRM_NOT_COVERED;
	}
	public void setR32_RWA_CRM_NOT_COVERED(BigDecimal r32_RWA_CRM_NOT_COVERED) {
		R32_RWA_CRM_NOT_COVERED = r32_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR32_CRM_ELIG_EXPOSURE_COMP() {
		return R32_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR32_CRM_ELIG_EXPOSURE_COMP(BigDecimal r32_CRM_ELIG_EXPOSURE_COMP) {
		R32_CRM_ELIG_EXPOSURE_COMP = r32_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR32_EXPOSURE_AFTER_VOL_ADJ() {
		return R32_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR32_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r32_EXPOSURE_AFTER_VOL_ADJ) {
		R32_EXPOSURE_AFTER_VOL_ADJ = r32_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR32_COLLATERAL_CASH() {
		return R32_COLLATERAL_CASH;
	}
	public void setR32_COLLATERAL_CASH(BigDecimal r32_COLLATERAL_CASH) {
		R32_COLLATERAL_CASH = r32_COLLATERAL_CASH;
	}
	public BigDecimal getR32_COLLATERAL_TBILLS() {
		return R32_COLLATERAL_TBILLS;
	}
	public void setR32_COLLATERAL_TBILLS(BigDecimal r32_COLLATERAL_TBILLS) {
		R32_COLLATERAL_TBILLS = r32_COLLATERAL_TBILLS;
	}
	public BigDecimal getR32_COLLATERAL_DEBT_SEC() {
		return R32_COLLATERAL_DEBT_SEC;
	}
	public void setR32_COLLATERAL_DEBT_SEC(BigDecimal r32_COLLATERAL_DEBT_SEC) {
		R32_COLLATERAL_DEBT_SEC = r32_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR32_COLLATERAL_EQUITIES() {
		return R32_COLLATERAL_EQUITIES;
	}
	public void setR32_COLLATERAL_EQUITIES(BigDecimal r32_COLLATERAL_EQUITIES) {
		R32_COLLATERAL_EQUITIES = r32_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR32_COLLATERAL_MUTUAL_FUNDS() {
		return R32_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR32_COLLATERAL_MUTUAL_FUNDS(BigDecimal r32_COLLATERAL_MUTUAL_FUNDS) {
		R32_COLLATERAL_MUTUAL_FUNDS = r32_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR32_TOTAL_COLLATERAL_HAIRCUT() {
		return R32_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR32_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r32_TOTAL_COLLATERAL_HAIRCUT) {
		R32_TOTAL_COLLATERAL_HAIRCUT = r32_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR32_EXPOSURE_AFTER_CRM() {
		return R32_EXPOSURE_AFTER_CRM;
	}
	public void setR32_EXPOSURE_AFTER_CRM(BigDecimal r32_EXPOSURE_AFTER_CRM) {
		R32_EXPOSURE_AFTER_CRM = r32_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR32_RWA_NOT_COVERED_CRM() {
		return R32_RWA_NOT_COVERED_CRM;
	}
	public void setR32_RWA_NOT_COVERED_CRM(BigDecimal r32_RWA_NOT_COVERED_CRM) {
		R32_RWA_NOT_COVERED_CRM = r32_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR32_RWA_UNSECURED_EXPOSURE() {
		return R32_RWA_UNSECURED_EXPOSURE;
	}
	public void setR32_RWA_UNSECURED_EXPOSURE(BigDecimal r32_RWA_UNSECURED_EXPOSURE) {
		R32_RWA_UNSECURED_EXPOSURE = r32_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR32_RWA_UNSECURED() {
		return R32_RWA_UNSECURED;
	}
	public void setR32_RWA_UNSECURED(BigDecimal r32_RWA_UNSECURED) {
		R32_RWA_UNSECURED = r32_RWA_UNSECURED;
	}
	public BigDecimal getR32_TOTAL_RWA() {
		return R32_TOTAL_RWA;
	}
	public void setR32_TOTAL_RWA(BigDecimal r32_TOTAL_RWA) {
		R32_TOTAL_RWA = r32_TOTAL_RWA;
	}
	public BigDecimal getR33_EXPOSURE_BEFORE_CRM() {
		return R33_EXPOSURE_BEFORE_CRM;
	}
	public void setR33_EXPOSURE_BEFORE_CRM(BigDecimal r33_EXPOSURE_BEFORE_CRM) {
		R33_EXPOSURE_BEFORE_CRM = r33_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR33_SPEC_PROVISION_PAST_DUE() {
		return R33_SPEC_PROVISION_PAST_DUE;
	}
	public void setR33_SPEC_PROVISION_PAST_DUE(BigDecimal r33_SPEC_PROVISION_PAST_DUE) {
		R33_SPEC_PROVISION_PAST_DUE = r33_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR33_ON_BAL_SHEET_NETTING_ELIG() {
		return R33_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR33_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r33_ON_BAL_SHEET_NETTING_ELIG) {
		R33_ON_BAL_SHEET_NETTING_ELIG = r33_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR33_TOTAL_EXPOSURE_AFTER_NET() {
		return R33_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR33_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r33_TOTAL_EXPOSURE_AFTER_NET) {
		R33_TOTAL_EXPOSURE_AFTER_NET = r33_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR33_CRM_ELIG_EXPOSURE_SUBS() {
		return R33_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR33_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r33_CRM_ELIG_EXPOSURE_SUBS) {
		R33_CRM_ELIG_EXPOSURE_SUBS = r33_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR33_ELIG_GUARANTEES() {
		return R33_ELIG_GUARANTEES;
	}
	public void setR33_ELIG_GUARANTEES(BigDecimal r33_ELIG_GUARANTEES) {
		R33_ELIG_GUARANTEES = r33_ELIG_GUARANTEES;
	}
	public BigDecimal getR33_CREDIT_DERIVATIVES() {
		return R33_CREDIT_DERIVATIVES;
	}
	public void setR33_CREDIT_DERIVATIVES(BigDecimal r33_CREDIT_DERIVATIVES) {
		R33_CREDIT_DERIVATIVES = r33_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR33_CRM_COVERED_EXPOSURE() {
		return R33_CRM_COVERED_EXPOSURE;
	}
	public void setR33_CRM_COVERED_EXPOSURE(BigDecimal r33_CRM_COVERED_EXPOSURE) {
		R33_CRM_COVERED_EXPOSURE = r33_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR33_CRM_NOT_COVERED_EXPOSURE() {
		return R33_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR33_CRM_NOT_COVERED_EXPOSURE(BigDecimal r33_CRM_NOT_COVERED_EXPOSURE) {
		R33_CRM_NOT_COVERED_EXPOSURE = r33_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR33_CRM_RISK_WEIGHT() {
		return R33_CRM_RISK_WEIGHT;
	}
	public void setR33_CRM_RISK_WEIGHT(BigDecimal r33_CRM_RISK_WEIGHT) {
		R33_CRM_RISK_WEIGHT = r33_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR33_RWA_CRM_COVERED() {
		return R33_RWA_CRM_COVERED;
	}
	public void setR33_RWA_CRM_COVERED(BigDecimal r33_RWA_CRM_COVERED) {
		R33_RWA_CRM_COVERED = r33_RWA_CRM_COVERED;
	}
	public BigDecimal getR33_ORIG_COUNTERPARTY_RW() {
		return R33_ORIG_COUNTERPARTY_RW;
	}
	public void setR33_ORIG_COUNTERPARTY_RW(BigDecimal r33_ORIG_COUNTERPARTY_RW) {
		R33_ORIG_COUNTERPARTY_RW = r33_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR33_RWA_CRM_NOT_COVERED() {
		return R33_RWA_CRM_NOT_COVERED;
	}
	public void setR33_RWA_CRM_NOT_COVERED(BigDecimal r33_RWA_CRM_NOT_COVERED) {
		R33_RWA_CRM_NOT_COVERED = r33_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR33_CRM_ELIG_EXPOSURE_COMP() {
		return R33_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR33_CRM_ELIG_EXPOSURE_COMP(BigDecimal r33_CRM_ELIG_EXPOSURE_COMP) {
		R33_CRM_ELIG_EXPOSURE_COMP = r33_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR33_EXPOSURE_AFTER_VOL_ADJ() {
		return R33_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR33_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r33_EXPOSURE_AFTER_VOL_ADJ) {
		R33_EXPOSURE_AFTER_VOL_ADJ = r33_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR33_COLLATERAL_CASH() {
		return R33_COLLATERAL_CASH;
	}
	public void setR33_COLLATERAL_CASH(BigDecimal r33_COLLATERAL_CASH) {
		R33_COLLATERAL_CASH = r33_COLLATERAL_CASH;
	}
	public BigDecimal getR33_COLLATERAL_TBILLS() {
		return R33_COLLATERAL_TBILLS;
	}
	public void setR33_COLLATERAL_TBILLS(BigDecimal r33_COLLATERAL_TBILLS) {
		R33_COLLATERAL_TBILLS = r33_COLLATERAL_TBILLS;
	}
	public BigDecimal getR33_COLLATERAL_DEBT_SEC() {
		return R33_COLLATERAL_DEBT_SEC;
	}
	public void setR33_COLLATERAL_DEBT_SEC(BigDecimal r33_COLLATERAL_DEBT_SEC) {
		R33_COLLATERAL_DEBT_SEC = r33_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR33_COLLATERAL_EQUITIES() {
		return R33_COLLATERAL_EQUITIES;
	}
	public void setR33_COLLATERAL_EQUITIES(BigDecimal r33_COLLATERAL_EQUITIES) {
		R33_COLLATERAL_EQUITIES = r33_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR33_COLLATERAL_MUTUAL_FUNDS() {
		return R33_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR33_COLLATERAL_MUTUAL_FUNDS(BigDecimal r33_COLLATERAL_MUTUAL_FUNDS) {
		R33_COLLATERAL_MUTUAL_FUNDS = r33_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR33_TOTAL_COLLATERAL_HAIRCUT() {
		return R33_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR33_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r33_TOTAL_COLLATERAL_HAIRCUT) {
		R33_TOTAL_COLLATERAL_HAIRCUT = r33_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR33_EXPOSURE_AFTER_CRM() {
		return R33_EXPOSURE_AFTER_CRM;
	}
	public void setR33_EXPOSURE_AFTER_CRM(BigDecimal r33_EXPOSURE_AFTER_CRM) {
		R33_EXPOSURE_AFTER_CRM = r33_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR33_RWA_NOT_COVERED_CRM() {
		return R33_RWA_NOT_COVERED_CRM;
	}
	public void setR33_RWA_NOT_COVERED_CRM(BigDecimal r33_RWA_NOT_COVERED_CRM) {
		R33_RWA_NOT_COVERED_CRM = r33_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR33_RWA_UNSECURED_EXPOSURE() {
		return R33_RWA_UNSECURED_EXPOSURE;
	}
	public void setR33_RWA_UNSECURED_EXPOSURE(BigDecimal r33_RWA_UNSECURED_EXPOSURE) {
		R33_RWA_UNSECURED_EXPOSURE = r33_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR33_RWA_UNSECURED() {
		return R33_RWA_UNSECURED;
	}
	public void setR33_RWA_UNSECURED(BigDecimal r33_RWA_UNSECURED) {
		R33_RWA_UNSECURED = r33_RWA_UNSECURED;
	}
	public BigDecimal getR33_TOTAL_RWA() {
		return R33_TOTAL_RWA;
	}
	public void setR33_TOTAL_RWA(BigDecimal r33_TOTAL_RWA) {
		R33_TOTAL_RWA = r33_TOTAL_RWA;
	}
	public BigDecimal getR34_EXPOSURE_BEFORE_CRM() {
		return R34_EXPOSURE_BEFORE_CRM;
	}
	public void setR34_EXPOSURE_BEFORE_CRM(BigDecimal r34_EXPOSURE_BEFORE_CRM) {
		R34_EXPOSURE_BEFORE_CRM = r34_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR34_SPEC_PROVISION_PAST_DUE() {
		return R34_SPEC_PROVISION_PAST_DUE;
	}
	public void setR34_SPEC_PROVISION_PAST_DUE(BigDecimal r34_SPEC_PROVISION_PAST_DUE) {
		R34_SPEC_PROVISION_PAST_DUE = r34_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR34_ON_BAL_SHEET_NETTING_ELIG() {
		return R34_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR34_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r34_ON_BAL_SHEET_NETTING_ELIG) {
		R34_ON_BAL_SHEET_NETTING_ELIG = r34_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR34_TOTAL_EXPOSURE_AFTER_NET() {
		return R34_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR34_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r34_TOTAL_EXPOSURE_AFTER_NET) {
		R34_TOTAL_EXPOSURE_AFTER_NET = r34_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR34_CRM_ELIG_EXPOSURE_SUBS() {
		return R34_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR34_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r34_CRM_ELIG_EXPOSURE_SUBS) {
		R34_CRM_ELIG_EXPOSURE_SUBS = r34_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR34_ELIG_GUARANTEES() {
		return R34_ELIG_GUARANTEES;
	}
	public void setR34_ELIG_GUARANTEES(BigDecimal r34_ELIG_GUARANTEES) {
		R34_ELIG_GUARANTEES = r34_ELIG_GUARANTEES;
	}
	public BigDecimal getR34_CREDIT_DERIVATIVES() {
		return R34_CREDIT_DERIVATIVES;
	}
	public void setR34_CREDIT_DERIVATIVES(BigDecimal r34_CREDIT_DERIVATIVES) {
		R34_CREDIT_DERIVATIVES = r34_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR34_CRM_COVERED_EXPOSURE() {
		return R34_CRM_COVERED_EXPOSURE;
	}
	public void setR34_CRM_COVERED_EXPOSURE(BigDecimal r34_CRM_COVERED_EXPOSURE) {
		R34_CRM_COVERED_EXPOSURE = r34_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR34_CRM_NOT_COVERED_EXPOSURE() {
		return R34_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR34_CRM_NOT_COVERED_EXPOSURE(BigDecimal r34_CRM_NOT_COVERED_EXPOSURE) {
		R34_CRM_NOT_COVERED_EXPOSURE = r34_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR34_CRM_RISK_WEIGHT() {
		return R34_CRM_RISK_WEIGHT;
	}
	public void setR34_CRM_RISK_WEIGHT(BigDecimal r34_CRM_RISK_WEIGHT) {
		R34_CRM_RISK_WEIGHT = r34_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR34_RWA_CRM_COVERED() {
		return R34_RWA_CRM_COVERED;
	}
	public void setR34_RWA_CRM_COVERED(BigDecimal r34_RWA_CRM_COVERED) {
		R34_RWA_CRM_COVERED = r34_RWA_CRM_COVERED;
	}
	public BigDecimal getR34_ORIG_COUNTERPARTY_RW() {
		return R34_ORIG_COUNTERPARTY_RW;
	}
	public void setR34_ORIG_COUNTERPARTY_RW(BigDecimal r34_ORIG_COUNTERPARTY_RW) {
		R34_ORIG_COUNTERPARTY_RW = r34_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR34_RWA_CRM_NOT_COVERED() {
		return R34_RWA_CRM_NOT_COVERED;
	}
	public void setR34_RWA_CRM_NOT_COVERED(BigDecimal r34_RWA_CRM_NOT_COVERED) {
		R34_RWA_CRM_NOT_COVERED = r34_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR34_CRM_ELIG_EXPOSURE_COMP() {
		return R34_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR34_CRM_ELIG_EXPOSURE_COMP(BigDecimal r34_CRM_ELIG_EXPOSURE_COMP) {
		R34_CRM_ELIG_EXPOSURE_COMP = r34_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR34_EXPOSURE_AFTER_VOL_ADJ() {
		return R34_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR34_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r34_EXPOSURE_AFTER_VOL_ADJ) {
		R34_EXPOSURE_AFTER_VOL_ADJ = r34_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR34_COLLATERAL_CASH() {
		return R34_COLLATERAL_CASH;
	}
	public void setR34_COLLATERAL_CASH(BigDecimal r34_COLLATERAL_CASH) {
		R34_COLLATERAL_CASH = r34_COLLATERAL_CASH;
	}
	public BigDecimal getR34_COLLATERAL_TBILLS() {
		return R34_COLLATERAL_TBILLS;
	}
	public void setR34_COLLATERAL_TBILLS(BigDecimal r34_COLLATERAL_TBILLS) {
		R34_COLLATERAL_TBILLS = r34_COLLATERAL_TBILLS;
	}
	public BigDecimal getR34_COLLATERAL_DEBT_SEC() {
		return R34_COLLATERAL_DEBT_SEC;
	}
	public void setR34_COLLATERAL_DEBT_SEC(BigDecimal r34_COLLATERAL_DEBT_SEC) {
		R34_COLLATERAL_DEBT_SEC = r34_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR34_COLLATERAL_EQUITIES() {
		return R34_COLLATERAL_EQUITIES;
	}
	public void setR34_COLLATERAL_EQUITIES(BigDecimal r34_COLLATERAL_EQUITIES) {
		R34_COLLATERAL_EQUITIES = r34_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR34_COLLATERAL_MUTUAL_FUNDS() {
		return R34_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR34_COLLATERAL_MUTUAL_FUNDS(BigDecimal r34_COLLATERAL_MUTUAL_FUNDS) {
		R34_COLLATERAL_MUTUAL_FUNDS = r34_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR34_TOTAL_COLLATERAL_HAIRCUT() {
		return R34_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR34_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r34_TOTAL_COLLATERAL_HAIRCUT) {
		R34_TOTAL_COLLATERAL_HAIRCUT = r34_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR34_EXPOSURE_AFTER_CRM() {
		return R34_EXPOSURE_AFTER_CRM;
	}
	public void setR34_EXPOSURE_AFTER_CRM(BigDecimal r34_EXPOSURE_AFTER_CRM) {
		R34_EXPOSURE_AFTER_CRM = r34_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR34_RWA_NOT_COVERED_CRM() {
		return R34_RWA_NOT_COVERED_CRM;
	}
	public void setR34_RWA_NOT_COVERED_CRM(BigDecimal r34_RWA_NOT_COVERED_CRM) {
		R34_RWA_NOT_COVERED_CRM = r34_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR34_RWA_UNSECURED_EXPOSURE() {
		return R34_RWA_UNSECURED_EXPOSURE;
	}
	public void setR34_RWA_UNSECURED_EXPOSURE(BigDecimal r34_RWA_UNSECURED_EXPOSURE) {
		R34_RWA_UNSECURED_EXPOSURE = r34_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR34_RWA_UNSECURED() {
		return R34_RWA_UNSECURED;
	}
	public void setR34_RWA_UNSECURED(BigDecimal r34_RWA_UNSECURED) {
		R34_RWA_UNSECURED = r34_RWA_UNSECURED;
	}
	public BigDecimal getR34_TOTAL_RWA() {
		return R34_TOTAL_RWA;
	}
	public void setR34_TOTAL_RWA(BigDecimal r34_TOTAL_RWA) {
		R34_TOTAL_RWA = r34_TOTAL_RWA;
	}
	public BigDecimal getR35_EXPOSURE_BEFORE_CRM() {
		return R35_EXPOSURE_BEFORE_CRM;
	}
	public void setR35_EXPOSURE_BEFORE_CRM(BigDecimal r35_EXPOSURE_BEFORE_CRM) {
		R35_EXPOSURE_BEFORE_CRM = r35_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR35_SPEC_PROVISION_PAST_DUE() {
		return R35_SPEC_PROVISION_PAST_DUE;
	}
	public void setR35_SPEC_PROVISION_PAST_DUE(BigDecimal r35_SPEC_PROVISION_PAST_DUE) {
		R35_SPEC_PROVISION_PAST_DUE = r35_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR35_ON_BAL_SHEET_NETTING_ELIG() {
		return R35_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR35_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r35_ON_BAL_SHEET_NETTING_ELIG) {
		R35_ON_BAL_SHEET_NETTING_ELIG = r35_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR35_TOTAL_EXPOSURE_AFTER_NET() {
		return R35_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR35_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r35_TOTAL_EXPOSURE_AFTER_NET) {
		R35_TOTAL_EXPOSURE_AFTER_NET = r35_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR35_CRM_ELIG_EXPOSURE_SUBS() {
		return R35_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR35_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r35_CRM_ELIG_EXPOSURE_SUBS) {
		R35_CRM_ELIG_EXPOSURE_SUBS = r35_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR35_ELIG_GUARANTEES() {
		return R35_ELIG_GUARANTEES;
	}
	public void setR35_ELIG_GUARANTEES(BigDecimal r35_ELIG_GUARANTEES) {
		R35_ELIG_GUARANTEES = r35_ELIG_GUARANTEES;
	}
	public BigDecimal getR35_CREDIT_DERIVATIVES() {
		return R35_CREDIT_DERIVATIVES;
	}
	public void setR35_CREDIT_DERIVATIVES(BigDecimal r35_CREDIT_DERIVATIVES) {
		R35_CREDIT_DERIVATIVES = r35_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR35_CRM_COVERED_EXPOSURE() {
		return R35_CRM_COVERED_EXPOSURE;
	}
	public void setR35_CRM_COVERED_EXPOSURE(BigDecimal r35_CRM_COVERED_EXPOSURE) {
		R35_CRM_COVERED_EXPOSURE = r35_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR35_CRM_NOT_COVERED_EXPOSURE() {
		return R35_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR35_CRM_NOT_COVERED_EXPOSURE(BigDecimal r35_CRM_NOT_COVERED_EXPOSURE) {
		R35_CRM_NOT_COVERED_EXPOSURE = r35_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR35_CRM_RISK_WEIGHT() {
		return R35_CRM_RISK_WEIGHT;
	}
	public void setR35_CRM_RISK_WEIGHT(BigDecimal r35_CRM_RISK_WEIGHT) {
		R35_CRM_RISK_WEIGHT = r35_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR35_RWA_CRM_COVERED() {
		return R35_RWA_CRM_COVERED;
	}
	public void setR35_RWA_CRM_COVERED(BigDecimal r35_RWA_CRM_COVERED) {
		R35_RWA_CRM_COVERED = r35_RWA_CRM_COVERED;
	}
	public BigDecimal getR35_ORIG_COUNTERPARTY_RW() {
		return R35_ORIG_COUNTERPARTY_RW;
	}
	public void setR35_ORIG_COUNTERPARTY_RW(BigDecimal r35_ORIG_COUNTERPARTY_RW) {
		R35_ORIG_COUNTERPARTY_RW = r35_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR35_RWA_CRM_NOT_COVERED() {
		return R35_RWA_CRM_NOT_COVERED;
	}
	public void setR35_RWA_CRM_NOT_COVERED(BigDecimal r35_RWA_CRM_NOT_COVERED) {
		R35_RWA_CRM_NOT_COVERED = r35_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR35_CRM_ELIG_EXPOSURE_COMP() {
		return R35_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR35_CRM_ELIG_EXPOSURE_COMP(BigDecimal r35_CRM_ELIG_EXPOSURE_COMP) {
		R35_CRM_ELIG_EXPOSURE_COMP = r35_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR35_EXPOSURE_AFTER_VOL_ADJ() {
		return R35_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR35_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r35_EXPOSURE_AFTER_VOL_ADJ) {
		R35_EXPOSURE_AFTER_VOL_ADJ = r35_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR35_COLLATERAL_CASH() {
		return R35_COLLATERAL_CASH;
	}
	public void setR35_COLLATERAL_CASH(BigDecimal r35_COLLATERAL_CASH) {
		R35_COLLATERAL_CASH = r35_COLLATERAL_CASH;
	}
	public BigDecimal getR35_COLLATERAL_TBILLS() {
		return R35_COLLATERAL_TBILLS;
	}
	public void setR35_COLLATERAL_TBILLS(BigDecimal r35_COLLATERAL_TBILLS) {
		R35_COLLATERAL_TBILLS = r35_COLLATERAL_TBILLS;
	}
	public BigDecimal getR35_COLLATERAL_DEBT_SEC() {
		return R35_COLLATERAL_DEBT_SEC;
	}
	public void setR35_COLLATERAL_DEBT_SEC(BigDecimal r35_COLLATERAL_DEBT_SEC) {
		R35_COLLATERAL_DEBT_SEC = r35_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR35_COLLATERAL_EQUITIES() {
		return R35_COLLATERAL_EQUITIES;
	}
	public void setR35_COLLATERAL_EQUITIES(BigDecimal r35_COLLATERAL_EQUITIES) {
		R35_COLLATERAL_EQUITIES = r35_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR35_COLLATERAL_MUTUAL_FUNDS() {
		return R35_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR35_COLLATERAL_MUTUAL_FUNDS(BigDecimal r35_COLLATERAL_MUTUAL_FUNDS) {
		R35_COLLATERAL_MUTUAL_FUNDS = r35_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR35_TOTAL_COLLATERAL_HAIRCUT() {
		return R35_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR35_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r35_TOTAL_COLLATERAL_HAIRCUT) {
		R35_TOTAL_COLLATERAL_HAIRCUT = r35_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR35_EXPOSURE_AFTER_CRM() {
		return R35_EXPOSURE_AFTER_CRM;
	}
	public void setR35_EXPOSURE_AFTER_CRM(BigDecimal r35_EXPOSURE_AFTER_CRM) {
		R35_EXPOSURE_AFTER_CRM = r35_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR35_RWA_NOT_COVERED_CRM() {
		return R35_RWA_NOT_COVERED_CRM;
	}
	public void setR35_RWA_NOT_COVERED_CRM(BigDecimal r35_RWA_NOT_COVERED_CRM) {
		R35_RWA_NOT_COVERED_CRM = r35_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR35_RWA_UNSECURED_EXPOSURE() {
		return R35_RWA_UNSECURED_EXPOSURE;
	}
	public void setR35_RWA_UNSECURED_EXPOSURE(BigDecimal r35_RWA_UNSECURED_EXPOSURE) {
		R35_RWA_UNSECURED_EXPOSURE = r35_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR35_RWA_UNSECURED() {
		return R35_RWA_UNSECURED;
	}
	public void setR35_RWA_UNSECURED(BigDecimal r35_RWA_UNSECURED) {
		R35_RWA_UNSECURED = r35_RWA_UNSECURED;
	}
	public BigDecimal getR35_TOTAL_RWA() {
		return R35_TOTAL_RWA;
	}
	public void setR35_TOTAL_RWA(BigDecimal r35_TOTAL_RWA) {
		R35_TOTAL_RWA = r35_TOTAL_RWA;
	}
	public BigDecimal getR36_EXPOSURE_BEFORE_CRM() {
		return R36_EXPOSURE_BEFORE_CRM;
	}
	public void setR36_EXPOSURE_BEFORE_CRM(BigDecimal r36_EXPOSURE_BEFORE_CRM) {
		R36_EXPOSURE_BEFORE_CRM = r36_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR36_SPEC_PROVISION_PAST_DUE() {
		return R36_SPEC_PROVISION_PAST_DUE;
	}
	public void setR36_SPEC_PROVISION_PAST_DUE(BigDecimal r36_SPEC_PROVISION_PAST_DUE) {
		R36_SPEC_PROVISION_PAST_DUE = r36_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR36_ON_BAL_SHEET_NETTING_ELIG() {
		return R36_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR36_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r36_ON_BAL_SHEET_NETTING_ELIG) {
		R36_ON_BAL_SHEET_NETTING_ELIG = r36_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR36_TOTAL_EXPOSURE_AFTER_NET() {
		return R36_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR36_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r36_TOTAL_EXPOSURE_AFTER_NET) {
		R36_TOTAL_EXPOSURE_AFTER_NET = r36_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR36_CRM_ELIG_EXPOSURE_SUBS() {
		return R36_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR36_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r36_CRM_ELIG_EXPOSURE_SUBS) {
		R36_CRM_ELIG_EXPOSURE_SUBS = r36_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR36_ELIG_GUARANTEES() {
		return R36_ELIG_GUARANTEES;
	}
	public void setR36_ELIG_GUARANTEES(BigDecimal r36_ELIG_GUARANTEES) {
		R36_ELIG_GUARANTEES = r36_ELIG_GUARANTEES;
	}
	public BigDecimal getR36_CREDIT_DERIVATIVES() {
		return R36_CREDIT_DERIVATIVES;
	}
	public void setR36_CREDIT_DERIVATIVES(BigDecimal r36_CREDIT_DERIVATIVES) {
		R36_CREDIT_DERIVATIVES = r36_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR36_CRM_COVERED_EXPOSURE() {
		return R36_CRM_COVERED_EXPOSURE;
	}
	public void setR36_CRM_COVERED_EXPOSURE(BigDecimal r36_CRM_COVERED_EXPOSURE) {
		R36_CRM_COVERED_EXPOSURE = r36_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR36_CRM_NOT_COVERED_EXPOSURE() {
		return R36_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR36_CRM_NOT_COVERED_EXPOSURE(BigDecimal r36_CRM_NOT_COVERED_EXPOSURE) {
		R36_CRM_NOT_COVERED_EXPOSURE = r36_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR36_CRM_RISK_WEIGHT() {
		return R36_CRM_RISK_WEIGHT;
	}
	public void setR36_CRM_RISK_WEIGHT(BigDecimal r36_CRM_RISK_WEIGHT) {
		R36_CRM_RISK_WEIGHT = r36_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR36_RWA_CRM_COVERED() {
		return R36_RWA_CRM_COVERED;
	}
	public void setR36_RWA_CRM_COVERED(BigDecimal r36_RWA_CRM_COVERED) {
		R36_RWA_CRM_COVERED = r36_RWA_CRM_COVERED;
	}
	public BigDecimal getR36_ORIG_COUNTERPARTY_RW() {
		return R36_ORIG_COUNTERPARTY_RW;
	}
	public void setR36_ORIG_COUNTERPARTY_RW(BigDecimal r36_ORIG_COUNTERPARTY_RW) {
		R36_ORIG_COUNTERPARTY_RW = r36_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR36_RWA_CRM_NOT_COVERED() {
		return R36_RWA_CRM_NOT_COVERED;
	}
	public void setR36_RWA_CRM_NOT_COVERED(BigDecimal r36_RWA_CRM_NOT_COVERED) {
		R36_RWA_CRM_NOT_COVERED = r36_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR36_CRM_ELIG_EXPOSURE_COMP() {
		return R36_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR36_CRM_ELIG_EXPOSURE_COMP(BigDecimal r36_CRM_ELIG_EXPOSURE_COMP) {
		R36_CRM_ELIG_EXPOSURE_COMP = r36_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR36_EXPOSURE_AFTER_VOL_ADJ() {
		return R36_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR36_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r36_EXPOSURE_AFTER_VOL_ADJ) {
		R36_EXPOSURE_AFTER_VOL_ADJ = r36_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR36_COLLATERAL_CASH() {
		return R36_COLLATERAL_CASH;
	}
	public void setR36_COLLATERAL_CASH(BigDecimal r36_COLLATERAL_CASH) {
		R36_COLLATERAL_CASH = r36_COLLATERAL_CASH;
	}
	public BigDecimal getR36_COLLATERAL_TBILLS() {
		return R36_COLLATERAL_TBILLS;
	}
	public void setR36_COLLATERAL_TBILLS(BigDecimal r36_COLLATERAL_TBILLS) {
		R36_COLLATERAL_TBILLS = r36_COLLATERAL_TBILLS;
	}
	public BigDecimal getR36_COLLATERAL_DEBT_SEC() {
		return R36_COLLATERAL_DEBT_SEC;
	}
	public void setR36_COLLATERAL_DEBT_SEC(BigDecimal r36_COLLATERAL_DEBT_SEC) {
		R36_COLLATERAL_DEBT_SEC = r36_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR36_COLLATERAL_EQUITIES() {
		return R36_COLLATERAL_EQUITIES;
	}
	public void setR36_COLLATERAL_EQUITIES(BigDecimal r36_COLLATERAL_EQUITIES) {
		R36_COLLATERAL_EQUITIES = r36_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR36_COLLATERAL_MUTUAL_FUNDS() {
		return R36_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR36_COLLATERAL_MUTUAL_FUNDS(BigDecimal r36_COLLATERAL_MUTUAL_FUNDS) {
		R36_COLLATERAL_MUTUAL_FUNDS = r36_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR36_TOTAL_COLLATERAL_HAIRCUT() {
		return R36_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR36_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r36_TOTAL_COLLATERAL_HAIRCUT) {
		R36_TOTAL_COLLATERAL_HAIRCUT = r36_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR36_EXPOSURE_AFTER_CRM() {
		return R36_EXPOSURE_AFTER_CRM;
	}
	public void setR36_EXPOSURE_AFTER_CRM(BigDecimal r36_EXPOSURE_AFTER_CRM) {
		R36_EXPOSURE_AFTER_CRM = r36_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR36_RWA_NOT_COVERED_CRM() {
		return R36_RWA_NOT_COVERED_CRM;
	}
	public void setR36_RWA_NOT_COVERED_CRM(BigDecimal r36_RWA_NOT_COVERED_CRM) {
		R36_RWA_NOT_COVERED_CRM = r36_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR36_RWA_UNSECURED_EXPOSURE() {
		return R36_RWA_UNSECURED_EXPOSURE;
	}
	public void setR36_RWA_UNSECURED_EXPOSURE(BigDecimal r36_RWA_UNSECURED_EXPOSURE) {
		R36_RWA_UNSECURED_EXPOSURE = r36_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR36_RWA_UNSECURED() {
		return R36_RWA_UNSECURED;
	}
	public void setR36_RWA_UNSECURED(BigDecimal r36_RWA_UNSECURED) {
		R36_RWA_UNSECURED = r36_RWA_UNSECURED;
	}
	public BigDecimal getR36_TOTAL_RWA() {
		return R36_TOTAL_RWA;
	}
	public void setR36_TOTAL_RWA(BigDecimal r36_TOTAL_RWA) {
		R36_TOTAL_RWA = r36_TOTAL_RWA;
	}
	public BigDecimal getR37_EXPOSURE_BEFORE_CRM() {
		return R37_EXPOSURE_BEFORE_CRM;
	}
	public void setR37_EXPOSURE_BEFORE_CRM(BigDecimal r37_EXPOSURE_BEFORE_CRM) {
		R37_EXPOSURE_BEFORE_CRM = r37_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR37_SPEC_PROVISION_PAST_DUE() {
		return R37_SPEC_PROVISION_PAST_DUE;
	}
	public void setR37_SPEC_PROVISION_PAST_DUE(BigDecimal r37_SPEC_PROVISION_PAST_DUE) {
		R37_SPEC_PROVISION_PAST_DUE = r37_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR37_ON_BAL_SHEET_NETTING_ELIG() {
		return R37_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR37_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r37_ON_BAL_SHEET_NETTING_ELIG) {
		R37_ON_BAL_SHEET_NETTING_ELIG = r37_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR37_TOTAL_EXPOSURE_AFTER_NET() {
		return R37_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR37_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r37_TOTAL_EXPOSURE_AFTER_NET) {
		R37_TOTAL_EXPOSURE_AFTER_NET = r37_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR37_CRM_ELIG_EXPOSURE_SUBS() {
		return R37_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR37_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r37_CRM_ELIG_EXPOSURE_SUBS) {
		R37_CRM_ELIG_EXPOSURE_SUBS = r37_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR37_ELIG_GUARANTEES() {
		return R37_ELIG_GUARANTEES;
	}
	public void setR37_ELIG_GUARANTEES(BigDecimal r37_ELIG_GUARANTEES) {
		R37_ELIG_GUARANTEES = r37_ELIG_GUARANTEES;
	}
	public BigDecimal getR37_CREDIT_DERIVATIVES() {
		return R37_CREDIT_DERIVATIVES;
	}
	public void setR37_CREDIT_DERIVATIVES(BigDecimal r37_CREDIT_DERIVATIVES) {
		R37_CREDIT_DERIVATIVES = r37_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR37_CRM_COVERED_EXPOSURE() {
		return R37_CRM_COVERED_EXPOSURE;
	}
	public void setR37_CRM_COVERED_EXPOSURE(BigDecimal r37_CRM_COVERED_EXPOSURE) {
		R37_CRM_COVERED_EXPOSURE = r37_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR37_CRM_NOT_COVERED_EXPOSURE() {
		return R37_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR37_CRM_NOT_COVERED_EXPOSURE(BigDecimal r37_CRM_NOT_COVERED_EXPOSURE) {
		R37_CRM_NOT_COVERED_EXPOSURE = r37_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR37_CRM_RISK_WEIGHT() {
		return R37_CRM_RISK_WEIGHT;
	}
	public void setR37_CRM_RISK_WEIGHT(BigDecimal r37_CRM_RISK_WEIGHT) {
		R37_CRM_RISK_WEIGHT = r37_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR37_RWA_CRM_COVERED() {
		return R37_RWA_CRM_COVERED;
	}
	public void setR37_RWA_CRM_COVERED(BigDecimal r37_RWA_CRM_COVERED) {
		R37_RWA_CRM_COVERED = r37_RWA_CRM_COVERED;
	}
	public BigDecimal getR37_ORIG_COUNTERPARTY_RW() {
		return R37_ORIG_COUNTERPARTY_RW;
	}
	public void setR37_ORIG_COUNTERPARTY_RW(BigDecimal r37_ORIG_COUNTERPARTY_RW) {
		R37_ORIG_COUNTERPARTY_RW = r37_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR37_RWA_CRM_NOT_COVERED() {
		return R37_RWA_CRM_NOT_COVERED;
	}
	public void setR37_RWA_CRM_NOT_COVERED(BigDecimal r37_RWA_CRM_NOT_COVERED) {
		R37_RWA_CRM_NOT_COVERED = r37_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR37_CRM_ELIG_EXPOSURE_COMP() {
		return R37_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR37_CRM_ELIG_EXPOSURE_COMP(BigDecimal r37_CRM_ELIG_EXPOSURE_COMP) {
		R37_CRM_ELIG_EXPOSURE_COMP = r37_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR37_EXPOSURE_AFTER_VOL_ADJ() {
		return R37_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR37_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r37_EXPOSURE_AFTER_VOL_ADJ) {
		R37_EXPOSURE_AFTER_VOL_ADJ = r37_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR37_COLLATERAL_CASH() {
		return R37_COLLATERAL_CASH;
	}
	public void setR37_COLLATERAL_CASH(BigDecimal r37_COLLATERAL_CASH) {
		R37_COLLATERAL_CASH = r37_COLLATERAL_CASH;
	}
	public BigDecimal getR37_COLLATERAL_TBILLS() {
		return R37_COLLATERAL_TBILLS;
	}
	public void setR37_COLLATERAL_TBILLS(BigDecimal r37_COLLATERAL_TBILLS) {
		R37_COLLATERAL_TBILLS = r37_COLLATERAL_TBILLS;
	}
	public BigDecimal getR37_COLLATERAL_DEBT_SEC() {
		return R37_COLLATERAL_DEBT_SEC;
	}
	public void setR37_COLLATERAL_DEBT_SEC(BigDecimal r37_COLLATERAL_DEBT_SEC) {
		R37_COLLATERAL_DEBT_SEC = r37_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR37_COLLATERAL_EQUITIES() {
		return R37_COLLATERAL_EQUITIES;
	}
	public void setR37_COLLATERAL_EQUITIES(BigDecimal r37_COLLATERAL_EQUITIES) {
		R37_COLLATERAL_EQUITIES = r37_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR37_COLLATERAL_MUTUAL_FUNDS() {
		return R37_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR37_COLLATERAL_MUTUAL_FUNDS(BigDecimal r37_COLLATERAL_MUTUAL_FUNDS) {
		R37_COLLATERAL_MUTUAL_FUNDS = r37_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR37_TOTAL_COLLATERAL_HAIRCUT() {
		return R37_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR37_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r37_TOTAL_COLLATERAL_HAIRCUT) {
		R37_TOTAL_COLLATERAL_HAIRCUT = r37_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR37_EXPOSURE_AFTER_CRM() {
		return R37_EXPOSURE_AFTER_CRM;
	}
	public void setR37_EXPOSURE_AFTER_CRM(BigDecimal r37_EXPOSURE_AFTER_CRM) {
		R37_EXPOSURE_AFTER_CRM = r37_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR37_RWA_NOT_COVERED_CRM() {
		return R37_RWA_NOT_COVERED_CRM;
	}
	public void setR37_RWA_NOT_COVERED_CRM(BigDecimal r37_RWA_NOT_COVERED_CRM) {
		R37_RWA_NOT_COVERED_CRM = r37_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR37_RWA_UNSECURED_EXPOSURE() {
		return R37_RWA_UNSECURED_EXPOSURE;
	}
	public void setR37_RWA_UNSECURED_EXPOSURE(BigDecimal r37_RWA_UNSECURED_EXPOSURE) {
		R37_RWA_UNSECURED_EXPOSURE = r37_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR37_RWA_UNSECURED() {
		return R37_RWA_UNSECURED;
	}
	public void setR37_RWA_UNSECURED(BigDecimal r37_RWA_UNSECURED) {
		R37_RWA_UNSECURED = r37_RWA_UNSECURED;
	}
	public BigDecimal getR37_TOTAL_RWA() {
		return R37_TOTAL_RWA;
	}
	public void setR37_TOTAL_RWA(BigDecimal r37_TOTAL_RWA) {
		R37_TOTAL_RWA = r37_TOTAL_RWA;
	}
	public BigDecimal getR38_EXPOSURE_BEFORE_CRM() {
		return R38_EXPOSURE_BEFORE_CRM;
	}
	public void setR38_EXPOSURE_BEFORE_CRM(BigDecimal r38_EXPOSURE_BEFORE_CRM) {
		R38_EXPOSURE_BEFORE_CRM = r38_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR38_SPEC_PROVISION_PAST_DUE() {
		return R38_SPEC_PROVISION_PAST_DUE;
	}
	public void setR38_SPEC_PROVISION_PAST_DUE(BigDecimal r38_SPEC_PROVISION_PAST_DUE) {
		R38_SPEC_PROVISION_PAST_DUE = r38_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR38_ON_BAL_SHEET_NETTING_ELIG() {
		return R38_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR38_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r38_ON_BAL_SHEET_NETTING_ELIG) {
		R38_ON_BAL_SHEET_NETTING_ELIG = r38_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR38_TOTAL_EXPOSURE_AFTER_NET() {
		return R38_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR38_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r38_TOTAL_EXPOSURE_AFTER_NET) {
		R38_TOTAL_EXPOSURE_AFTER_NET = r38_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR38_CRM_ELIG_EXPOSURE_SUBS() {
		return R38_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR38_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r38_CRM_ELIG_EXPOSURE_SUBS) {
		R38_CRM_ELIG_EXPOSURE_SUBS = r38_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR38_ELIG_GUARANTEES() {
		return R38_ELIG_GUARANTEES;
	}
	public void setR38_ELIG_GUARANTEES(BigDecimal r38_ELIG_GUARANTEES) {
		R38_ELIG_GUARANTEES = r38_ELIG_GUARANTEES;
	}
	public BigDecimal getR38_CREDIT_DERIVATIVES() {
		return R38_CREDIT_DERIVATIVES;
	}
	public void setR38_CREDIT_DERIVATIVES(BigDecimal r38_CREDIT_DERIVATIVES) {
		R38_CREDIT_DERIVATIVES = r38_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR38_CRM_COVERED_EXPOSURE() {
		return R38_CRM_COVERED_EXPOSURE;
	}
	public void setR38_CRM_COVERED_EXPOSURE(BigDecimal r38_CRM_COVERED_EXPOSURE) {
		R38_CRM_COVERED_EXPOSURE = r38_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR38_CRM_NOT_COVERED_EXPOSURE() {
		return R38_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR38_CRM_NOT_COVERED_EXPOSURE(BigDecimal r38_CRM_NOT_COVERED_EXPOSURE) {
		R38_CRM_NOT_COVERED_EXPOSURE = r38_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR38_CRM_RISK_WEIGHT() {
		return R38_CRM_RISK_WEIGHT;
	}
	public void setR38_CRM_RISK_WEIGHT(BigDecimal r38_CRM_RISK_WEIGHT) {
		R38_CRM_RISK_WEIGHT = r38_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR38_RWA_CRM_COVERED() {
		return R38_RWA_CRM_COVERED;
	}
	public void setR38_RWA_CRM_COVERED(BigDecimal r38_RWA_CRM_COVERED) {
		R38_RWA_CRM_COVERED = r38_RWA_CRM_COVERED;
	}
	public BigDecimal getR38_ORIG_COUNTERPARTY_RW() {
		return R38_ORIG_COUNTERPARTY_RW;
	}
	public void setR38_ORIG_COUNTERPARTY_RW(BigDecimal r38_ORIG_COUNTERPARTY_RW) {
		R38_ORIG_COUNTERPARTY_RW = r38_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR38_RWA_CRM_NOT_COVERED() {
		return R38_RWA_CRM_NOT_COVERED;
	}
	public void setR38_RWA_CRM_NOT_COVERED(BigDecimal r38_RWA_CRM_NOT_COVERED) {
		R38_RWA_CRM_NOT_COVERED = r38_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR38_CRM_ELIG_EXPOSURE_COMP() {
		return R38_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR38_CRM_ELIG_EXPOSURE_COMP(BigDecimal r38_CRM_ELIG_EXPOSURE_COMP) {
		R38_CRM_ELIG_EXPOSURE_COMP = r38_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR38_EXPOSURE_AFTER_VOL_ADJ() {
		return R38_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR38_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r38_EXPOSURE_AFTER_VOL_ADJ) {
		R38_EXPOSURE_AFTER_VOL_ADJ = r38_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR38_COLLATERAL_CASH() {
		return R38_COLLATERAL_CASH;
	}
	public void setR38_COLLATERAL_CASH(BigDecimal r38_COLLATERAL_CASH) {
		R38_COLLATERAL_CASH = r38_COLLATERAL_CASH;
	}
	public BigDecimal getR38_COLLATERAL_TBILLS() {
		return R38_COLLATERAL_TBILLS;
	}
	public void setR38_COLLATERAL_TBILLS(BigDecimal r38_COLLATERAL_TBILLS) {
		R38_COLLATERAL_TBILLS = r38_COLLATERAL_TBILLS;
	}
	public BigDecimal getR38_COLLATERAL_DEBT_SEC() {
		return R38_COLLATERAL_DEBT_SEC;
	}
	public void setR38_COLLATERAL_DEBT_SEC(BigDecimal r38_COLLATERAL_DEBT_SEC) {
		R38_COLLATERAL_DEBT_SEC = r38_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR38_COLLATERAL_EQUITIES() {
		return R38_COLLATERAL_EQUITIES;
	}
	public void setR38_COLLATERAL_EQUITIES(BigDecimal r38_COLLATERAL_EQUITIES) {
		R38_COLLATERAL_EQUITIES = r38_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR38_COLLATERAL_MUTUAL_FUNDS() {
		return R38_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR38_COLLATERAL_MUTUAL_FUNDS(BigDecimal r38_COLLATERAL_MUTUAL_FUNDS) {
		R38_COLLATERAL_MUTUAL_FUNDS = r38_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR38_TOTAL_COLLATERAL_HAIRCUT() {
		return R38_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR38_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r38_TOTAL_COLLATERAL_HAIRCUT) {
		R38_TOTAL_COLLATERAL_HAIRCUT = r38_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR38_EXPOSURE_AFTER_CRM() {
		return R38_EXPOSURE_AFTER_CRM;
	}
	public void setR38_EXPOSURE_AFTER_CRM(BigDecimal r38_EXPOSURE_AFTER_CRM) {
		R38_EXPOSURE_AFTER_CRM = r38_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR38_RWA_NOT_COVERED_CRM() {
		return R38_RWA_NOT_COVERED_CRM;
	}
	public void setR38_RWA_NOT_COVERED_CRM(BigDecimal r38_RWA_NOT_COVERED_CRM) {
		R38_RWA_NOT_COVERED_CRM = r38_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR38_RWA_UNSECURED_EXPOSURE() {
		return R38_RWA_UNSECURED_EXPOSURE;
	}
	public void setR38_RWA_UNSECURED_EXPOSURE(BigDecimal r38_RWA_UNSECURED_EXPOSURE) {
		R38_RWA_UNSECURED_EXPOSURE = r38_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR38_RWA_UNSECURED() {
		return R38_RWA_UNSECURED;
	}
	public void setR38_RWA_UNSECURED(BigDecimal r38_RWA_UNSECURED) {
		R38_RWA_UNSECURED = r38_RWA_UNSECURED;
	}
	public BigDecimal getR38_TOTAL_RWA() {
		return R38_TOTAL_RWA;
	}
	public void setR38_TOTAL_RWA(BigDecimal r38_TOTAL_RWA) {
		R38_TOTAL_RWA = r38_TOTAL_RWA;
	}
	public BigDecimal getR39_EXPOSURE_BEFORE_CRM() {
		return R39_EXPOSURE_BEFORE_CRM;
	}
	public void setR39_EXPOSURE_BEFORE_CRM(BigDecimal r39_EXPOSURE_BEFORE_CRM) {
		R39_EXPOSURE_BEFORE_CRM = r39_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR39_SPEC_PROVISION_PAST_DUE() {
		return R39_SPEC_PROVISION_PAST_DUE;
	}
	public void setR39_SPEC_PROVISION_PAST_DUE(BigDecimal r39_SPEC_PROVISION_PAST_DUE) {
		R39_SPEC_PROVISION_PAST_DUE = r39_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR39_ON_BAL_SHEET_NETTING_ELIG() {
		return R39_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR39_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r39_ON_BAL_SHEET_NETTING_ELIG) {
		R39_ON_BAL_SHEET_NETTING_ELIG = r39_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR39_TOTAL_EXPOSURE_AFTER_NET() {
		return R39_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR39_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r39_TOTAL_EXPOSURE_AFTER_NET) {
		R39_TOTAL_EXPOSURE_AFTER_NET = r39_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR39_CRM_ELIG_EXPOSURE_SUBS() {
		return R39_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR39_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r39_CRM_ELIG_EXPOSURE_SUBS) {
		R39_CRM_ELIG_EXPOSURE_SUBS = r39_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR39_ELIG_GUARANTEES() {
		return R39_ELIG_GUARANTEES;
	}
	public void setR39_ELIG_GUARANTEES(BigDecimal r39_ELIG_GUARANTEES) {
		R39_ELIG_GUARANTEES = r39_ELIG_GUARANTEES;
	}
	public BigDecimal getR39_CREDIT_DERIVATIVES() {
		return R39_CREDIT_DERIVATIVES;
	}
	public void setR39_CREDIT_DERIVATIVES(BigDecimal r39_CREDIT_DERIVATIVES) {
		R39_CREDIT_DERIVATIVES = r39_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR39_CRM_COVERED_EXPOSURE() {
		return R39_CRM_COVERED_EXPOSURE;
	}
	public void setR39_CRM_COVERED_EXPOSURE(BigDecimal r39_CRM_COVERED_EXPOSURE) {
		R39_CRM_COVERED_EXPOSURE = r39_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR39_CRM_NOT_COVERED_EXPOSURE() {
		return R39_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR39_CRM_NOT_COVERED_EXPOSURE(BigDecimal r39_CRM_NOT_COVERED_EXPOSURE) {
		R39_CRM_NOT_COVERED_EXPOSURE = r39_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR39_CRM_RISK_WEIGHT() {
		return R39_CRM_RISK_WEIGHT;
	}
	public void setR39_CRM_RISK_WEIGHT(BigDecimal r39_CRM_RISK_WEIGHT) {
		R39_CRM_RISK_WEIGHT = r39_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR39_RWA_CRM_COVERED() {
		return R39_RWA_CRM_COVERED;
	}
	public void setR39_RWA_CRM_COVERED(BigDecimal r39_RWA_CRM_COVERED) {
		R39_RWA_CRM_COVERED = r39_RWA_CRM_COVERED;
	}
	public BigDecimal getR39_ORIG_COUNTERPARTY_RW() {
		return R39_ORIG_COUNTERPARTY_RW;
	}
	public void setR39_ORIG_COUNTERPARTY_RW(BigDecimal r39_ORIG_COUNTERPARTY_RW) {
		R39_ORIG_COUNTERPARTY_RW = r39_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR39_RWA_CRM_NOT_COVERED() {
		return R39_RWA_CRM_NOT_COVERED;
	}
	public void setR39_RWA_CRM_NOT_COVERED(BigDecimal r39_RWA_CRM_NOT_COVERED) {
		R39_RWA_CRM_NOT_COVERED = r39_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR39_CRM_ELIG_EXPOSURE_COMP() {
		return R39_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR39_CRM_ELIG_EXPOSURE_COMP(BigDecimal r39_CRM_ELIG_EXPOSURE_COMP) {
		R39_CRM_ELIG_EXPOSURE_COMP = r39_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR39_EXPOSURE_AFTER_VOL_ADJ() {
		return R39_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR39_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r39_EXPOSURE_AFTER_VOL_ADJ) {
		R39_EXPOSURE_AFTER_VOL_ADJ = r39_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR39_COLLATERAL_CASH() {
		return R39_COLLATERAL_CASH;
	}
	public void setR39_COLLATERAL_CASH(BigDecimal r39_COLLATERAL_CASH) {
		R39_COLLATERAL_CASH = r39_COLLATERAL_CASH;
	}
	public BigDecimal getR39_COLLATERAL_TBILLS() {
		return R39_COLLATERAL_TBILLS;
	}
	public void setR39_COLLATERAL_TBILLS(BigDecimal r39_COLLATERAL_TBILLS) {
		R39_COLLATERAL_TBILLS = r39_COLLATERAL_TBILLS;
	}
	public BigDecimal getR39_COLLATERAL_DEBT_SEC() {
		return R39_COLLATERAL_DEBT_SEC;
	}
	public void setR39_COLLATERAL_DEBT_SEC(BigDecimal r39_COLLATERAL_DEBT_SEC) {
		R39_COLLATERAL_DEBT_SEC = r39_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR39_COLLATERAL_EQUITIES() {
		return R39_COLLATERAL_EQUITIES;
	}
	public void setR39_COLLATERAL_EQUITIES(BigDecimal r39_COLLATERAL_EQUITIES) {
		R39_COLLATERAL_EQUITIES = r39_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR39_COLLATERAL_MUTUAL_FUNDS() {
		return R39_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR39_COLLATERAL_MUTUAL_FUNDS(BigDecimal r39_COLLATERAL_MUTUAL_FUNDS) {
		R39_COLLATERAL_MUTUAL_FUNDS = r39_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR39_TOTAL_COLLATERAL_HAIRCUT() {
		return R39_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR39_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r39_TOTAL_COLLATERAL_HAIRCUT) {
		R39_TOTAL_COLLATERAL_HAIRCUT = r39_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR39_EXPOSURE_AFTER_CRM() {
		return R39_EXPOSURE_AFTER_CRM;
	}
	public void setR39_EXPOSURE_AFTER_CRM(BigDecimal r39_EXPOSURE_AFTER_CRM) {
		R39_EXPOSURE_AFTER_CRM = r39_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR39_RWA_NOT_COVERED_CRM() {
		return R39_RWA_NOT_COVERED_CRM;
	}
	public void setR39_RWA_NOT_COVERED_CRM(BigDecimal r39_RWA_NOT_COVERED_CRM) {
		R39_RWA_NOT_COVERED_CRM = r39_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR39_RWA_UNSECURED_EXPOSURE() {
		return R39_RWA_UNSECURED_EXPOSURE;
	}
	public void setR39_RWA_UNSECURED_EXPOSURE(BigDecimal r39_RWA_UNSECURED_EXPOSURE) {
		R39_RWA_UNSECURED_EXPOSURE = r39_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR39_RWA_UNSECURED() {
		return R39_RWA_UNSECURED;
	}
	public void setR39_RWA_UNSECURED(BigDecimal r39_RWA_UNSECURED) {
		R39_RWA_UNSECURED = r39_RWA_UNSECURED;
	}
	public BigDecimal getR39_TOTAL_RWA() {
		return R39_TOTAL_RWA;
	}
	public void setR39_TOTAL_RWA(BigDecimal r39_TOTAL_RWA) {
		R39_TOTAL_RWA = r39_TOTAL_RWA;
	}
	public M_SRWA_12B_SUMMARY_1_NEW_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
