package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="BRRS_M_SRWA_12B_SUMMARY_TABLE_2_NEW")
public class M_SRWA_12B_SUMMARY_2_NEW_ENTITY {
	
	@Id
	private Date REPORT_DATE;
	private String REPORT_VERSION;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	private BigDecimal R40_EXPOSURE_BEFORE_CRM;
	private BigDecimal R40_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R40_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R40_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R40_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R40_ELIG_GUARANTEES;
	private BigDecimal R40_CREDIT_DERIVATIVES;
	private BigDecimal R40_CRM_COVERED_EXPOSURE;
	private BigDecimal R40_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R40_CRM_RISK_WEIGHT;
	private BigDecimal R40_RWA_CRM_COVERED;
	private BigDecimal R40_ORIG_COUNTERPARTY_RW;
	private BigDecimal R40_RWA_CRM_NOT_COVERED;
	private BigDecimal R40_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R40_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R40_COLLATERAL_CASH;
	private BigDecimal R40_COLLATERAL_TBILLS;
	private BigDecimal R40_COLLATERAL_DEBT_SEC;
	private BigDecimal R40_COLLATERAL_EQUITIES;
	private BigDecimal R40_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R40_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R40_EXPOSURE_AFTER_CRM;
	private BigDecimal R40_RWA_NOT_COVERED_CRM;
	private BigDecimal R40_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R40_RWA_UNSECURED;
	private BigDecimal R40_TOTAL_RWA;
	private BigDecimal R41_EXPOSURE_BEFORE_CRM;
	private BigDecimal R41_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R41_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R41_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R41_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R41_ELIG_GUARANTEES;
	private BigDecimal R41_CREDIT_DERIVATIVES;
	private BigDecimal R41_CRM_COVERED_EXPOSURE;
	private BigDecimal R41_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R41_CRM_RISK_WEIGHT;
	private BigDecimal R41_RWA_CRM_COVERED;
	private BigDecimal R41_ORIG_COUNTERPARTY_RW;
	private BigDecimal R41_RWA_CRM_NOT_COVERED;
	private BigDecimal R41_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R41_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R41_COLLATERAL_CASH;
	private BigDecimal R41_COLLATERAL_TBILLS;
	private BigDecimal R41_COLLATERAL_DEBT_SEC;
	private BigDecimal R41_COLLATERAL_EQUITIES;
	private BigDecimal R41_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R41_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R41_EXPOSURE_AFTER_CRM;
	private BigDecimal R41_RWA_NOT_COVERED_CRM;
	private BigDecimal R41_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R41_RWA_UNSECURED;
	private BigDecimal R41_TOTAL_RWA;
	private BigDecimal R42_EXPOSURE_BEFORE_CRM;
	private BigDecimal R42_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R42_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R42_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R42_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R42_ELIG_GUARANTEES;
	private BigDecimal R42_CREDIT_DERIVATIVES;
	private BigDecimal R42_CRM_COVERED_EXPOSURE;
	private BigDecimal R42_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R42_CRM_RISK_WEIGHT;
	private BigDecimal R42_RWA_CRM_COVERED;
	private BigDecimal R42_ORIG_COUNTERPARTY_RW;
	private BigDecimal R42_RWA_CRM_NOT_COVERED;
	private BigDecimal R42_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R42_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R42_COLLATERAL_CASH;
	private BigDecimal R42_COLLATERAL_TBILLS;
	private BigDecimal R42_COLLATERAL_DEBT_SEC;
	private BigDecimal R42_COLLATERAL_EQUITIES;
	private BigDecimal R42_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R42_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R42_EXPOSURE_AFTER_CRM;
	private BigDecimal R42_RWA_NOT_COVERED_CRM;
	private BigDecimal R42_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R42_RWA_UNSECURED;
	private BigDecimal R42_TOTAL_RWA;
	private BigDecimal R43_EXPOSURE_BEFORE_CRM;
	private BigDecimal R43_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R43_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R43_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R43_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R43_ELIG_GUARANTEES;
	private BigDecimal R43_CREDIT_DERIVATIVES;
	private BigDecimal R43_CRM_COVERED_EXPOSURE;
	private BigDecimal R43_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R43_CRM_RISK_WEIGHT;
	private BigDecimal R43_RWA_CRM_COVERED;
	private BigDecimal R43_ORIG_COUNTERPARTY_RW;
	private BigDecimal R43_RWA_CRM_NOT_COVERED;
	private BigDecimal R43_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R43_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R43_COLLATERAL_CASH;
	private BigDecimal R43_COLLATERAL_TBILLS;
	private BigDecimal R43_COLLATERAL_DEBT_SEC;
	private BigDecimal R43_COLLATERAL_EQUITIES;
	private BigDecimal R43_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R43_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R43_EXPOSURE_AFTER_CRM;
	private BigDecimal R43_RWA_NOT_COVERED_CRM;
	private BigDecimal R43_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R43_RWA_UNSECURED;
	private BigDecimal R43_TOTAL_RWA;
	private BigDecimal R44_EXPOSURE_BEFORE_CRM;
	private BigDecimal R44_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R44_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R44_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R44_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R44_ELIG_GUARANTEES;
	private BigDecimal R44_CREDIT_DERIVATIVES;
	private BigDecimal R44_CRM_COVERED_EXPOSURE;
	private BigDecimal R44_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R44_CRM_RISK_WEIGHT;
	private BigDecimal R44_RWA_CRM_COVERED;
	private BigDecimal R44_ORIG_COUNTERPARTY_RW;
	private BigDecimal R44_RWA_CRM_NOT_COVERED;
	private BigDecimal R44_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R44_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R44_COLLATERAL_CASH;
	private BigDecimal R44_COLLATERAL_TBILLS;
	private BigDecimal R44_COLLATERAL_DEBT_SEC;
	private BigDecimal R44_COLLATERAL_EQUITIES;
	private BigDecimal R44_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R44_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R44_EXPOSURE_AFTER_CRM;
	private BigDecimal R44_RWA_NOT_COVERED_CRM;
	private BigDecimal R44_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R44_RWA_UNSECURED;
	private BigDecimal R44_TOTAL_RWA;
	private BigDecimal R45_EXPOSURE_BEFORE_CRM;
	private BigDecimal R45_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R45_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R45_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R45_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R45_ELIG_GUARANTEES;
	private BigDecimal R45_CREDIT_DERIVATIVES;
	private BigDecimal R45_CRM_COVERED_EXPOSURE;
	private BigDecimal R45_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R45_CRM_RISK_WEIGHT;
	private BigDecimal R45_RWA_CRM_COVERED;
	private BigDecimal R45_ORIG_COUNTERPARTY_RW;
	private BigDecimal R45_RWA_CRM_NOT_COVERED;
	private BigDecimal R45_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R45_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R45_COLLATERAL_CASH;
	private BigDecimal R45_COLLATERAL_TBILLS;
	private BigDecimal R45_COLLATERAL_DEBT_SEC;
	private BigDecimal R45_COLLATERAL_EQUITIES;
	private BigDecimal R45_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R45_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R45_EXPOSURE_AFTER_CRM;
	private BigDecimal R45_RWA_NOT_COVERED_CRM;
	private BigDecimal R45_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R45_RWA_UNSECURED;
	private BigDecimal R45_TOTAL_RWA;
	private BigDecimal R46_EXPOSURE_BEFORE_CRM;
	private BigDecimal R46_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R46_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R46_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R46_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R46_ELIG_GUARANTEES;
	private BigDecimal R46_CREDIT_DERIVATIVES;
	private BigDecimal R46_CRM_COVERED_EXPOSURE;
	private BigDecimal R46_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R46_CRM_RISK_WEIGHT;
	private BigDecimal R46_RWA_CRM_COVERED;
	private BigDecimal R46_ORIG_COUNTERPARTY_RW;
	private BigDecimal R46_RWA_CRM_NOT_COVERED;
	private BigDecimal R46_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R46_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R46_COLLATERAL_CASH;
	private BigDecimal R46_COLLATERAL_TBILLS;
	private BigDecimal R46_COLLATERAL_DEBT_SEC;
	private BigDecimal R46_COLLATERAL_EQUITIES;
	private BigDecimal R46_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R46_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R46_EXPOSURE_AFTER_CRM;
	private BigDecimal R46_RWA_NOT_COVERED_CRM;
	private BigDecimal R46_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R46_RWA_UNSECURED;
	private BigDecimal R46_TOTAL_RWA;
	private BigDecimal R47_EXPOSURE_BEFORE_CRM;
	private BigDecimal R47_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R47_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R47_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R47_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R47_ELIG_GUARANTEES;
	private BigDecimal R47_CREDIT_DERIVATIVES;
	private BigDecimal R47_CRM_COVERED_EXPOSURE;
	private BigDecimal R47_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R47_CRM_RISK_WEIGHT;
	private BigDecimal R47_RWA_CRM_COVERED;
	private BigDecimal R47_ORIG_COUNTERPARTY_RW;
	private BigDecimal R47_RWA_CRM_NOT_COVERED;
	private BigDecimal R47_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R47_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R47_COLLATERAL_CASH;
	private BigDecimal R47_COLLATERAL_TBILLS;
	private BigDecimal R47_COLLATERAL_DEBT_SEC;
	private BigDecimal R47_COLLATERAL_EQUITIES;
	private BigDecimal R47_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R47_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R47_EXPOSURE_AFTER_CRM;
	private BigDecimal R47_RWA_NOT_COVERED_CRM;
	private BigDecimal R47_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R47_RWA_UNSECURED;
	private BigDecimal R47_TOTAL_RWA;
	private BigDecimal R48_EXPOSURE_BEFORE_CRM;
	private BigDecimal R48_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R48_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R48_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R48_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R48_ELIG_GUARANTEES;
	private BigDecimal R48_CREDIT_DERIVATIVES;
	private BigDecimal R48_CRM_COVERED_EXPOSURE;
	private BigDecimal R48_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R48_CRM_RISK_WEIGHT;
	private BigDecimal R48_RWA_CRM_COVERED;
	private BigDecimal R48_ORIG_COUNTERPARTY_RW;
	private BigDecimal R48_RWA_CRM_NOT_COVERED;
	private BigDecimal R48_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R48_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R48_COLLATERAL_CASH;
	private BigDecimal R48_COLLATERAL_TBILLS;
	private BigDecimal R48_COLLATERAL_DEBT_SEC;
	private BigDecimal R48_COLLATERAL_EQUITIES;
	private BigDecimal R48_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R48_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R48_EXPOSURE_AFTER_CRM;
	private BigDecimal R48_RWA_NOT_COVERED_CRM;
	private BigDecimal R48_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R48_RWA_UNSECURED;
	private BigDecimal R48_TOTAL_RWA;
	private BigDecimal R49_EXPOSURE_BEFORE_CRM;
	private BigDecimal R49_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R49_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R49_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R49_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R49_ELIG_GUARANTEES;
	private BigDecimal R49_CREDIT_DERIVATIVES;
	private BigDecimal R49_CRM_COVERED_EXPOSURE;
	private BigDecimal R49_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R49_CRM_RISK_WEIGHT;
	private BigDecimal R49_RWA_CRM_COVERED;
	private BigDecimal R49_ORIG_COUNTERPARTY_RW;
	private BigDecimal R49_RWA_CRM_NOT_COVERED;
	private BigDecimal R49_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R49_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R49_COLLATERAL_CASH;
	private BigDecimal R49_COLLATERAL_TBILLS;
	private BigDecimal R49_COLLATERAL_DEBT_SEC;
	private BigDecimal R49_COLLATERAL_EQUITIES;
	private BigDecimal R49_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R49_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R49_EXPOSURE_AFTER_CRM;
	private BigDecimal R49_RWA_NOT_COVERED_CRM;
	private BigDecimal R49_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R49_RWA_UNSECURED;
	private BigDecimal R49_TOTAL_RWA;
	private BigDecimal R50_EXPOSURE_BEFORE_CRM;
	private BigDecimal R50_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R50_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R50_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R50_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R50_ELIG_GUARANTEES;
	private BigDecimal R50_CREDIT_DERIVATIVES;
	private BigDecimal R50_CRM_COVERED_EXPOSURE;
	private BigDecimal R50_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R50_CRM_RISK_WEIGHT;
	private BigDecimal R50_RWA_CRM_COVERED;
	private BigDecimal R50_ORIG_COUNTERPARTY_RW;
	private BigDecimal R50_RWA_CRM_NOT_COVERED;
	private BigDecimal R50_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R50_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R50_COLLATERAL_CASH;
	private BigDecimal R50_COLLATERAL_TBILLS;
	private BigDecimal R50_COLLATERAL_DEBT_SEC;
	private BigDecimal R50_COLLATERAL_EQUITIES;
	private BigDecimal R50_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R50_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R50_EXPOSURE_AFTER_CRM;
	private BigDecimal R50_RWA_NOT_COVERED_CRM;
	private BigDecimal R50_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R50_RWA_UNSECURED;
	private BigDecimal R50_TOTAL_RWA;
	private BigDecimal R51_EXPOSURE_BEFORE_CRM;
	private BigDecimal R51_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R51_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R51_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R51_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R51_ELIG_GUARANTEES;
	private BigDecimal R51_CREDIT_DERIVATIVES;
	private BigDecimal R51_CRM_COVERED_EXPOSURE;
	private BigDecimal R51_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R51_CRM_RISK_WEIGHT;
	private BigDecimal R51_RWA_CRM_COVERED;
	private BigDecimal R51_ORIG_COUNTERPARTY_RW;
	private BigDecimal R51_RWA_CRM_NOT_COVERED;
	private BigDecimal R51_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R51_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R51_COLLATERAL_CASH;
	private BigDecimal R51_COLLATERAL_TBILLS;
	private BigDecimal R51_COLLATERAL_DEBT_SEC;
	private BigDecimal R51_COLLATERAL_EQUITIES;
	private BigDecimal R51_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R51_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R51_EXPOSURE_AFTER_CRM;
	private BigDecimal R51_RWA_NOT_COVERED_CRM;
	private BigDecimal R51_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R51_RWA_UNSECURED;
	private BigDecimal R51_TOTAL_RWA;
	private BigDecimal R52_EXPOSURE_BEFORE_CRM;
	private BigDecimal R52_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R52_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R52_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R52_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R52_ELIG_GUARANTEES;
	private BigDecimal R52_CREDIT_DERIVATIVES;
	private BigDecimal R52_CRM_COVERED_EXPOSURE;
	private BigDecimal R52_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R52_CRM_RISK_WEIGHT;
	private BigDecimal R52_RWA_CRM_COVERED;
	private BigDecimal R52_ORIG_COUNTERPARTY_RW;
	private BigDecimal R52_RWA_CRM_NOT_COVERED;
	private BigDecimal R52_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R52_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R52_COLLATERAL_CASH;
	private BigDecimal R52_COLLATERAL_TBILLS;
	private BigDecimal R52_COLLATERAL_DEBT_SEC;
	private BigDecimal R52_COLLATERAL_EQUITIES;
	private BigDecimal R52_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R52_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R52_EXPOSURE_AFTER_CRM;
	private BigDecimal R52_RWA_NOT_COVERED_CRM;
	private BigDecimal R52_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R52_RWA_UNSECURED;
	private BigDecimal R52_TOTAL_RWA;
	private BigDecimal R53_EXPOSURE_BEFORE_CRM;
	private BigDecimal R53_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R53_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R53_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R53_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R53_ELIG_GUARANTEES;
	private BigDecimal R53_CREDIT_DERIVATIVES;
	private BigDecimal R53_CRM_COVERED_EXPOSURE;
	private BigDecimal R53_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R53_CRM_RISK_WEIGHT;
	private BigDecimal R53_RWA_CRM_COVERED;
	private BigDecimal R53_ORIG_COUNTERPARTY_RW;
	private BigDecimal R53_RWA_CRM_NOT_COVERED;
	private BigDecimal R53_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R53_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R53_COLLATERAL_CASH;
	private BigDecimal R53_COLLATERAL_TBILLS;
	private BigDecimal R53_COLLATERAL_DEBT_SEC;
	private BigDecimal R53_COLLATERAL_EQUITIES;
	private BigDecimal R53_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R53_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R53_EXPOSURE_AFTER_CRM;
	private BigDecimal R53_RWA_NOT_COVERED_CRM;
	private BigDecimal R53_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R53_RWA_UNSECURED;
	private BigDecimal R53_TOTAL_RWA;
	private BigDecimal R54_EXPOSURE_BEFORE_CRM;
	private BigDecimal R54_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R54_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R54_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R54_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R54_ELIG_GUARANTEES;
	private BigDecimal R54_CREDIT_DERIVATIVES;
	private BigDecimal R54_CRM_COVERED_EXPOSURE;
	private BigDecimal R54_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R54_CRM_RISK_WEIGHT;
	private BigDecimal R54_RWA_CRM_COVERED;
	private BigDecimal R54_ORIG_COUNTERPARTY_RW;
	private BigDecimal R54_RWA_CRM_NOT_COVERED;
	private BigDecimal R54_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R54_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R54_COLLATERAL_CASH;
	private BigDecimal R54_COLLATERAL_TBILLS;
	private BigDecimal R54_COLLATERAL_DEBT_SEC;
	private BigDecimal R54_COLLATERAL_EQUITIES;
	private BigDecimal R54_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R54_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R54_EXPOSURE_AFTER_CRM;
	private BigDecimal R54_RWA_NOT_COVERED_CRM;
	private BigDecimal R54_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R54_RWA_UNSECURED;
	private BigDecimal R54_TOTAL_RWA;
	private BigDecimal R55_EXPOSURE_BEFORE_CRM;
	private BigDecimal R55_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R55_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R55_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R55_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R55_ELIG_GUARANTEES;
	private BigDecimal R55_CREDIT_DERIVATIVES;
	private BigDecimal R55_CRM_COVERED_EXPOSURE;
	private BigDecimal R55_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R55_CRM_RISK_WEIGHT;
	private BigDecimal R55_RWA_CRM_COVERED;
	private BigDecimal R55_ORIG_COUNTERPARTY_RW;
	private BigDecimal R55_RWA_CRM_NOT_COVERED;
	private BigDecimal R55_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R55_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R55_COLLATERAL_CASH;
	private BigDecimal R55_COLLATERAL_TBILLS;
	private BigDecimal R55_COLLATERAL_DEBT_SEC;
	private BigDecimal R55_COLLATERAL_EQUITIES;
	private BigDecimal R55_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R55_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R55_EXPOSURE_AFTER_CRM;
	private BigDecimal R55_RWA_NOT_COVERED_CRM;
	private BigDecimal R55_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R55_RWA_UNSECURED;
	private BigDecimal R55_TOTAL_RWA;
	private BigDecimal R56_EXPOSURE_BEFORE_CRM;
	private BigDecimal R56_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R56_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R56_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R56_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R56_ELIG_GUARANTEES;
	private BigDecimal R56_CREDIT_DERIVATIVES;
	private BigDecimal R56_CRM_COVERED_EXPOSURE;
	private BigDecimal R56_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R56_CRM_RISK_WEIGHT;
	private BigDecimal R56_RWA_CRM_COVERED;
	private BigDecimal R56_ORIG_COUNTERPARTY_RW;
	private BigDecimal R56_RWA_CRM_NOT_COVERED;
	private BigDecimal R56_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R56_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R56_COLLATERAL_CASH;
	private BigDecimal R56_COLLATERAL_TBILLS;
	private BigDecimal R56_COLLATERAL_DEBT_SEC;
	private BigDecimal R56_COLLATERAL_EQUITIES;
	private BigDecimal R56_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R56_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R56_EXPOSURE_AFTER_CRM;
	private BigDecimal R56_RWA_NOT_COVERED_CRM;
	private BigDecimal R56_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R56_RWA_UNSECURED;
	private BigDecimal R56_TOTAL_RWA;
	private BigDecimal R57_EXPOSURE_BEFORE_CRM;
	private BigDecimal R57_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R57_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R57_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R57_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R57_ELIG_GUARANTEES;
	private BigDecimal R57_CREDIT_DERIVATIVES;
	private BigDecimal R57_CRM_COVERED_EXPOSURE;
	private BigDecimal R57_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R57_CRM_RISK_WEIGHT;
	private BigDecimal R57_RWA_CRM_COVERED;
	private BigDecimal R57_ORIG_COUNTERPARTY_RW;
	private BigDecimal R57_RWA_CRM_NOT_COVERED;
	private BigDecimal R57_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R57_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R57_COLLATERAL_CASH;
	private BigDecimal R57_COLLATERAL_TBILLS;
	private BigDecimal R57_COLLATERAL_DEBT_SEC;
	private BigDecimal R57_COLLATERAL_EQUITIES;
	private BigDecimal R57_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R57_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R57_EXPOSURE_AFTER_CRM;
	private BigDecimal R57_RWA_NOT_COVERED_CRM;
	private BigDecimal R57_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R57_RWA_UNSECURED;
	private BigDecimal R57_TOTAL_RWA;
	private BigDecimal R58_EXPOSURE_BEFORE_CRM;
	private BigDecimal R58_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R58_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R58_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R58_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R58_ELIG_GUARANTEES;
	private BigDecimal R58_CREDIT_DERIVATIVES;
	private BigDecimal R58_CRM_COVERED_EXPOSURE;
	private BigDecimal R58_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R58_CRM_RISK_WEIGHT;
	private BigDecimal R58_RWA_CRM_COVERED;
	private BigDecimal R58_ORIG_COUNTERPARTY_RW;
	private BigDecimal R58_RWA_CRM_NOT_COVERED;
	private BigDecimal R58_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R58_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R58_COLLATERAL_CASH;
	private BigDecimal R58_COLLATERAL_TBILLS;
	private BigDecimal R58_COLLATERAL_DEBT_SEC;
	private BigDecimal R58_COLLATERAL_EQUITIES;
	private BigDecimal R58_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R58_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R58_EXPOSURE_AFTER_CRM;
	private BigDecimal R58_RWA_NOT_COVERED_CRM;
	private BigDecimal R58_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R58_RWA_UNSECURED;
	private BigDecimal R58_TOTAL_RWA;
	private BigDecimal R59_EXPOSURE_BEFORE_CRM;
	private BigDecimal R59_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R59_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R59_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R59_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R59_ELIG_GUARANTEES;
	private BigDecimal R59_CREDIT_DERIVATIVES;
	private BigDecimal R59_CRM_COVERED_EXPOSURE;
	private BigDecimal R59_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R59_CRM_RISK_WEIGHT;
	private BigDecimal R59_RWA_CRM_COVERED;
	private BigDecimal R59_ORIG_COUNTERPARTY_RW;
	private BigDecimal R59_RWA_CRM_NOT_COVERED;
	private BigDecimal R59_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R59_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R59_COLLATERAL_CASH;
	private BigDecimal R59_COLLATERAL_TBILLS;
	private BigDecimal R59_COLLATERAL_DEBT_SEC;
	private BigDecimal R59_COLLATERAL_EQUITIES;
	private BigDecimal R59_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R59_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R59_EXPOSURE_AFTER_CRM;
	private BigDecimal R59_RWA_NOT_COVERED_CRM;
	private BigDecimal R59_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R59_RWA_UNSECURED;
	private BigDecimal R59_TOTAL_RWA;
	private BigDecimal R60_EXPOSURE_BEFORE_CRM;
	private BigDecimal R60_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R60_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R60_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R60_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R60_ELIG_GUARANTEES;
	private BigDecimal R60_CREDIT_DERIVATIVES;
	private BigDecimal R60_CRM_COVERED_EXPOSURE;
	private BigDecimal R60_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R60_CRM_RISK_WEIGHT;
	private BigDecimal R60_RWA_CRM_COVERED;
	private BigDecimal R60_ORIG_COUNTERPARTY_RW;
	private BigDecimal R60_RWA_CRM_NOT_COVERED;
	private BigDecimal R60_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R60_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R60_COLLATERAL_CASH;
	private BigDecimal R60_COLLATERAL_TBILLS;
	private BigDecimal R60_COLLATERAL_DEBT_SEC;
	private BigDecimal R60_COLLATERAL_EQUITIES;
	private BigDecimal R60_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R60_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R60_EXPOSURE_AFTER_CRM;
	private BigDecimal R60_RWA_NOT_COVERED_CRM;
	private BigDecimal R60_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R60_RWA_UNSECURED;
	private BigDecimal R60_TOTAL_RWA;
	private BigDecimal R61_EXPOSURE_BEFORE_CRM;
	private BigDecimal R61_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R61_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R61_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R61_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R61_ELIG_GUARANTEES;
	private BigDecimal R61_CREDIT_DERIVATIVES;
	private BigDecimal R61_CRM_COVERED_EXPOSURE;
	private BigDecimal R61_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R61_CRM_RISK_WEIGHT;
	private BigDecimal R61_RWA_CRM_COVERED;
	private BigDecimal R61_ORIG_COUNTERPARTY_RW;
	private BigDecimal R61_RWA_CRM_NOT_COVERED;
	private BigDecimal R61_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R61_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R61_COLLATERAL_CASH;
	private BigDecimal R61_COLLATERAL_TBILLS;
	private BigDecimal R61_COLLATERAL_DEBT_SEC;
	private BigDecimal R61_COLLATERAL_EQUITIES;
	private BigDecimal R61_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R61_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R61_EXPOSURE_AFTER_CRM;
	private BigDecimal R61_RWA_NOT_COVERED_CRM;
	private BigDecimal R61_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R61_RWA_UNSECURED;
	private BigDecimal R61_TOTAL_RWA;
	private BigDecimal R62_EXPOSURE_BEFORE_CRM;
	private BigDecimal R62_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R62_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R62_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R62_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R62_ELIG_GUARANTEES;
	private BigDecimal R62_CREDIT_DERIVATIVES;
	private BigDecimal R62_CRM_COVERED_EXPOSURE;
	private BigDecimal R62_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R62_CRM_RISK_WEIGHT;
	private BigDecimal R62_RWA_CRM_COVERED;
	private BigDecimal R62_ORIG_COUNTERPARTY_RW;
	private BigDecimal R62_RWA_CRM_NOT_COVERED;
	private BigDecimal R62_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R62_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R62_COLLATERAL_CASH;
	private BigDecimal R62_COLLATERAL_TBILLS;
	private BigDecimal R62_COLLATERAL_DEBT_SEC;
	private BigDecimal R62_COLLATERAL_EQUITIES;
	private BigDecimal R62_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R62_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R62_EXPOSURE_AFTER_CRM;
	private BigDecimal R62_RWA_NOT_COVERED_CRM;
	private BigDecimal R62_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R62_RWA_UNSECURED;
	private BigDecimal R62_TOTAL_RWA;
	private BigDecimal R63_EXPOSURE_BEFORE_CRM;
	private BigDecimal R63_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R63_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R63_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R63_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R63_ELIG_GUARANTEES;
	private BigDecimal R63_CREDIT_DERIVATIVES;
	private BigDecimal R63_CRM_COVERED_EXPOSURE;
	private BigDecimal R63_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R63_CRM_RISK_WEIGHT;
	private BigDecimal R63_RWA_CRM_COVERED;
	private BigDecimal R63_ORIG_COUNTERPARTY_RW;
	private BigDecimal R63_RWA_CRM_NOT_COVERED;
	private BigDecimal R63_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R63_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R63_COLLATERAL_CASH;
	private BigDecimal R63_COLLATERAL_TBILLS;
	private BigDecimal R63_COLLATERAL_DEBT_SEC;
	private BigDecimal R63_COLLATERAL_EQUITIES;
	private BigDecimal R63_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R63_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R63_EXPOSURE_AFTER_CRM;
	private BigDecimal R63_RWA_NOT_COVERED_CRM;
	private BigDecimal R63_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R63_RWA_UNSECURED;
	private BigDecimal R63_TOTAL_RWA;
	private BigDecimal R64_EXPOSURE_BEFORE_CRM;
	private BigDecimal R64_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R64_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R64_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R64_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R64_ELIG_GUARANTEES;
	private BigDecimal R64_CREDIT_DERIVATIVES;
	private BigDecimal R64_CRM_COVERED_EXPOSURE;
	private BigDecimal R64_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R64_CRM_RISK_WEIGHT;
	private BigDecimal R64_RWA_CRM_COVERED;
	private BigDecimal R64_ORIG_COUNTERPARTY_RW;
	private BigDecimal R64_RWA_CRM_NOT_COVERED;
	private BigDecimal R64_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R64_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R64_COLLATERAL_CASH;
	private BigDecimal R64_COLLATERAL_TBILLS;
	private BigDecimal R64_COLLATERAL_DEBT_SEC;
	private BigDecimal R64_COLLATERAL_EQUITIES;
	private BigDecimal R64_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R64_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R64_EXPOSURE_AFTER_CRM;
	private BigDecimal R64_RWA_NOT_COVERED_CRM;
	private BigDecimal R64_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R64_RWA_UNSECURED;
	private BigDecimal R64_TOTAL_RWA;
	private BigDecimal R65_EXPOSURE_BEFORE_CRM;
	private BigDecimal R65_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R65_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R65_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R65_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R65_ELIG_GUARANTEES;
	private BigDecimal R65_CREDIT_DERIVATIVES;
	private BigDecimal R65_CRM_COVERED_EXPOSURE;
	private BigDecimal R65_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R65_CRM_RISK_WEIGHT;
	private BigDecimal R65_RWA_CRM_COVERED;
	private BigDecimal R65_ORIG_COUNTERPARTY_RW;
	private BigDecimal R65_RWA_CRM_NOT_COVERED;
	private BigDecimal R65_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R65_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R65_COLLATERAL_CASH;
	private BigDecimal R65_COLLATERAL_TBILLS;
	private BigDecimal R65_COLLATERAL_DEBT_SEC;
	private BigDecimal R65_COLLATERAL_EQUITIES;
	private BigDecimal R65_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R65_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R65_EXPOSURE_AFTER_CRM;
	private BigDecimal R65_RWA_NOT_COVERED_CRM;
	private BigDecimal R65_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R65_RWA_UNSECURED;
	private BigDecimal R65_TOTAL_RWA;
	private BigDecimal R66_EXPOSURE_BEFORE_CRM;
	private BigDecimal R66_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R66_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R66_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R66_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R66_ELIG_GUARANTEES;
	private BigDecimal R66_CREDIT_DERIVATIVES;
	private BigDecimal R66_CRM_COVERED_EXPOSURE;
	private BigDecimal R66_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R66_CRM_RISK_WEIGHT;
	private BigDecimal R66_RWA_CRM_COVERED;
	private BigDecimal R66_ORIG_COUNTERPARTY_RW;
	private BigDecimal R66_RWA_CRM_NOT_COVERED;
	private BigDecimal R66_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R66_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R66_COLLATERAL_CASH;
	private BigDecimal R66_COLLATERAL_TBILLS;
	private BigDecimal R66_COLLATERAL_DEBT_SEC;
	private BigDecimal R66_COLLATERAL_EQUITIES;
	private BigDecimal R66_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R66_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R66_EXPOSURE_AFTER_CRM;
	private BigDecimal R66_RWA_NOT_COVERED_CRM;
	private BigDecimal R66_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R66_RWA_UNSECURED;
	private BigDecimal R66_TOTAL_RWA;
	private BigDecimal R67_EXPOSURE_BEFORE_CRM;
	private BigDecimal R67_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R67_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R67_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R67_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R67_ELIG_GUARANTEES;
	private BigDecimal R67_CREDIT_DERIVATIVES;
	private BigDecimal R67_CRM_COVERED_EXPOSURE;
	private BigDecimal R67_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R67_CRM_RISK_WEIGHT;
	private BigDecimal R67_RWA_CRM_COVERED;
	private BigDecimal R67_ORIG_COUNTERPARTY_RW;
	private BigDecimal R67_RWA_CRM_NOT_COVERED;
	private BigDecimal R67_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R67_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R67_COLLATERAL_CASH;
	private BigDecimal R67_COLLATERAL_TBILLS;
	private BigDecimal R67_COLLATERAL_DEBT_SEC;
	private BigDecimal R67_COLLATERAL_EQUITIES;
	private BigDecimal R67_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R67_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R67_EXPOSURE_AFTER_CRM;
	private BigDecimal R67_RWA_NOT_COVERED_CRM;
	private BigDecimal R67_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R67_RWA_UNSECURED;
	private BigDecimal R67_TOTAL_RWA;
	private BigDecimal R68_EXPOSURE_BEFORE_CRM;
	private BigDecimal R68_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R68_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R68_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R68_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R68_ELIG_GUARANTEES;
	private BigDecimal R68_CREDIT_DERIVATIVES;
	private BigDecimal R68_CRM_COVERED_EXPOSURE;
	private BigDecimal R68_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R68_CRM_RISK_WEIGHT;
	private BigDecimal R68_RWA_CRM_COVERED;
	private BigDecimal R68_ORIG_COUNTERPARTY_RW;
	private BigDecimal R68_RWA_CRM_NOT_COVERED;
	private BigDecimal R68_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R68_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R68_COLLATERAL_CASH;
	private BigDecimal R68_COLLATERAL_TBILLS;
	private BigDecimal R68_COLLATERAL_DEBT_SEC;
	private BigDecimal R68_COLLATERAL_EQUITIES;
	private BigDecimal R68_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R68_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R68_EXPOSURE_AFTER_CRM;
	private BigDecimal R68_RWA_NOT_COVERED_CRM;
	private BigDecimal R68_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R68_RWA_UNSECURED;
	private BigDecimal R68_TOTAL_RWA;
	private BigDecimal R69_EXPOSURE_BEFORE_CRM;
	private BigDecimal R69_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R69_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R69_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R69_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R69_ELIG_GUARANTEES;
	private BigDecimal R69_CREDIT_DERIVATIVES;
	private BigDecimal R69_CRM_COVERED_EXPOSURE;
	private BigDecimal R69_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R69_CRM_RISK_WEIGHT;
	private BigDecimal R69_RWA_CRM_COVERED;
	private BigDecimal R69_ORIG_COUNTERPARTY_RW;
	private BigDecimal R69_RWA_CRM_NOT_COVERED;
	private BigDecimal R69_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R69_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R69_COLLATERAL_CASH;
	private BigDecimal R69_COLLATERAL_TBILLS;
	private BigDecimal R69_COLLATERAL_DEBT_SEC;
	private BigDecimal R69_COLLATERAL_EQUITIES;
	private BigDecimal R69_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R69_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R69_EXPOSURE_AFTER_CRM;
	private BigDecimal R69_RWA_NOT_COVERED_CRM;
	private BigDecimal R69_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R69_RWA_UNSECURED;
	private BigDecimal R69_TOTAL_RWA;
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
	public BigDecimal getR40_EXPOSURE_BEFORE_CRM() {
		return R40_EXPOSURE_BEFORE_CRM;
	}
	public void setR40_EXPOSURE_BEFORE_CRM(BigDecimal r40_EXPOSURE_BEFORE_CRM) {
		R40_EXPOSURE_BEFORE_CRM = r40_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR40_SPEC_PROVISION_PAST_DUE() {
		return R40_SPEC_PROVISION_PAST_DUE;
	}
	public void setR40_SPEC_PROVISION_PAST_DUE(BigDecimal r40_SPEC_PROVISION_PAST_DUE) {
		R40_SPEC_PROVISION_PAST_DUE = r40_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR40_ON_BAL_SHEET_NETTING_ELIG() {
		return R40_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR40_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r40_ON_BAL_SHEET_NETTING_ELIG) {
		R40_ON_BAL_SHEET_NETTING_ELIG = r40_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR40_TOTAL_EXPOSURE_AFTER_NET() {
		return R40_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR40_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r40_TOTAL_EXPOSURE_AFTER_NET) {
		R40_TOTAL_EXPOSURE_AFTER_NET = r40_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR40_CRM_ELIG_EXPOSURE_SUBS() {
		return R40_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR40_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r40_CRM_ELIG_EXPOSURE_SUBS) {
		R40_CRM_ELIG_EXPOSURE_SUBS = r40_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR40_ELIG_GUARANTEES() {
		return R40_ELIG_GUARANTEES;
	}
	public void setR40_ELIG_GUARANTEES(BigDecimal r40_ELIG_GUARANTEES) {
		R40_ELIG_GUARANTEES = r40_ELIG_GUARANTEES;
	}
	public BigDecimal getR40_CREDIT_DERIVATIVES() {
		return R40_CREDIT_DERIVATIVES;
	}
	public void setR40_CREDIT_DERIVATIVES(BigDecimal r40_CREDIT_DERIVATIVES) {
		R40_CREDIT_DERIVATIVES = r40_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR40_CRM_COVERED_EXPOSURE() {
		return R40_CRM_COVERED_EXPOSURE;
	}
	public void setR40_CRM_COVERED_EXPOSURE(BigDecimal r40_CRM_COVERED_EXPOSURE) {
		R40_CRM_COVERED_EXPOSURE = r40_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR40_CRM_NOT_COVERED_EXPOSURE() {
		return R40_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR40_CRM_NOT_COVERED_EXPOSURE(BigDecimal r40_CRM_NOT_COVERED_EXPOSURE) {
		R40_CRM_NOT_COVERED_EXPOSURE = r40_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR40_CRM_RISK_WEIGHT() {
		return R40_CRM_RISK_WEIGHT;
	}
	public void setR40_CRM_RISK_WEIGHT(BigDecimal r40_CRM_RISK_WEIGHT) {
		R40_CRM_RISK_WEIGHT = r40_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR40_RWA_CRM_COVERED() {
		return R40_RWA_CRM_COVERED;
	}
	public void setR40_RWA_CRM_COVERED(BigDecimal r40_RWA_CRM_COVERED) {
		R40_RWA_CRM_COVERED = r40_RWA_CRM_COVERED;
	}
	public BigDecimal getR40_ORIG_COUNTERPARTY_RW() {
		return R40_ORIG_COUNTERPARTY_RW;
	}
	public void setR40_ORIG_COUNTERPARTY_RW(BigDecimal r40_ORIG_COUNTERPARTY_RW) {
		R40_ORIG_COUNTERPARTY_RW = r40_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR40_RWA_CRM_NOT_COVERED() {
		return R40_RWA_CRM_NOT_COVERED;
	}
	public void setR40_RWA_CRM_NOT_COVERED(BigDecimal r40_RWA_CRM_NOT_COVERED) {
		R40_RWA_CRM_NOT_COVERED = r40_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR40_CRM_ELIG_EXPOSURE_COMP() {
		return R40_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR40_CRM_ELIG_EXPOSURE_COMP(BigDecimal r40_CRM_ELIG_EXPOSURE_COMP) {
		R40_CRM_ELIG_EXPOSURE_COMP = r40_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR40_EXPOSURE_AFTER_VOL_ADJ() {
		return R40_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR40_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r40_EXPOSURE_AFTER_VOL_ADJ) {
		R40_EXPOSURE_AFTER_VOL_ADJ = r40_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR40_COLLATERAL_CASH() {
		return R40_COLLATERAL_CASH;
	}
	public void setR40_COLLATERAL_CASH(BigDecimal r40_COLLATERAL_CASH) {
		R40_COLLATERAL_CASH = r40_COLLATERAL_CASH;
	}
	public BigDecimal getR40_COLLATERAL_TBILLS() {
		return R40_COLLATERAL_TBILLS;
	}
	public void setR40_COLLATERAL_TBILLS(BigDecimal r40_COLLATERAL_TBILLS) {
		R40_COLLATERAL_TBILLS = r40_COLLATERAL_TBILLS;
	}
	public BigDecimal getR40_COLLATERAL_DEBT_SEC() {
		return R40_COLLATERAL_DEBT_SEC;
	}
	public void setR40_COLLATERAL_DEBT_SEC(BigDecimal r40_COLLATERAL_DEBT_SEC) {
		R40_COLLATERAL_DEBT_SEC = r40_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR40_COLLATERAL_EQUITIES() {
		return R40_COLLATERAL_EQUITIES;
	}
	public void setR40_COLLATERAL_EQUITIES(BigDecimal r40_COLLATERAL_EQUITIES) {
		R40_COLLATERAL_EQUITIES = r40_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR40_COLLATERAL_MUTUAL_FUNDS() {
		return R40_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR40_COLLATERAL_MUTUAL_FUNDS(BigDecimal r40_COLLATERAL_MUTUAL_FUNDS) {
		R40_COLLATERAL_MUTUAL_FUNDS = r40_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR40_TOTAL_COLLATERAL_HAIRCUT() {
		return R40_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR40_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r40_TOTAL_COLLATERAL_HAIRCUT) {
		R40_TOTAL_COLLATERAL_HAIRCUT = r40_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR40_EXPOSURE_AFTER_CRM() {
		return R40_EXPOSURE_AFTER_CRM;
	}
	public void setR40_EXPOSURE_AFTER_CRM(BigDecimal r40_EXPOSURE_AFTER_CRM) {
		R40_EXPOSURE_AFTER_CRM = r40_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR40_RWA_NOT_COVERED_CRM() {
		return R40_RWA_NOT_COVERED_CRM;
	}
	public void setR40_RWA_NOT_COVERED_CRM(BigDecimal r40_RWA_NOT_COVERED_CRM) {
		R40_RWA_NOT_COVERED_CRM = r40_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR40_RWA_UNSECURED_EXPOSURE() {
		return R40_RWA_UNSECURED_EXPOSURE;
	}
	public void setR40_RWA_UNSECURED_EXPOSURE(BigDecimal r40_RWA_UNSECURED_EXPOSURE) {
		R40_RWA_UNSECURED_EXPOSURE = r40_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR40_RWA_UNSECURED() {
		return R40_RWA_UNSECURED;
	}
	public void setR40_RWA_UNSECURED(BigDecimal r40_RWA_UNSECURED) {
		R40_RWA_UNSECURED = r40_RWA_UNSECURED;
	}
	public BigDecimal getR40_TOTAL_RWA() {
		return R40_TOTAL_RWA;
	}
	public void setR40_TOTAL_RWA(BigDecimal r40_TOTAL_RWA) {
		R40_TOTAL_RWA = r40_TOTAL_RWA;
	}
	public BigDecimal getR41_EXPOSURE_BEFORE_CRM() {
		return R41_EXPOSURE_BEFORE_CRM;
	}
	public void setR41_EXPOSURE_BEFORE_CRM(BigDecimal r41_EXPOSURE_BEFORE_CRM) {
		R41_EXPOSURE_BEFORE_CRM = r41_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR41_SPEC_PROVISION_PAST_DUE() {
		return R41_SPEC_PROVISION_PAST_DUE;
	}
	public void setR41_SPEC_PROVISION_PAST_DUE(BigDecimal r41_SPEC_PROVISION_PAST_DUE) {
		R41_SPEC_PROVISION_PAST_DUE = r41_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR41_ON_BAL_SHEET_NETTING_ELIG() {
		return R41_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR41_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r41_ON_BAL_SHEET_NETTING_ELIG) {
		R41_ON_BAL_SHEET_NETTING_ELIG = r41_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR41_TOTAL_EXPOSURE_AFTER_NET() {
		return R41_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR41_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r41_TOTAL_EXPOSURE_AFTER_NET) {
		R41_TOTAL_EXPOSURE_AFTER_NET = r41_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR41_CRM_ELIG_EXPOSURE_SUBS() {
		return R41_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR41_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r41_CRM_ELIG_EXPOSURE_SUBS) {
		R41_CRM_ELIG_EXPOSURE_SUBS = r41_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR41_ELIG_GUARANTEES() {
		return R41_ELIG_GUARANTEES;
	}
	public void setR41_ELIG_GUARANTEES(BigDecimal r41_ELIG_GUARANTEES) {
		R41_ELIG_GUARANTEES = r41_ELIG_GUARANTEES;
	}
	public BigDecimal getR41_CREDIT_DERIVATIVES() {
		return R41_CREDIT_DERIVATIVES;
	}
	public void setR41_CREDIT_DERIVATIVES(BigDecimal r41_CREDIT_DERIVATIVES) {
		R41_CREDIT_DERIVATIVES = r41_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR41_CRM_COVERED_EXPOSURE() {
		return R41_CRM_COVERED_EXPOSURE;
	}
	public void setR41_CRM_COVERED_EXPOSURE(BigDecimal r41_CRM_COVERED_EXPOSURE) {
		R41_CRM_COVERED_EXPOSURE = r41_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR41_CRM_NOT_COVERED_EXPOSURE() {
		return R41_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR41_CRM_NOT_COVERED_EXPOSURE(BigDecimal r41_CRM_NOT_COVERED_EXPOSURE) {
		R41_CRM_NOT_COVERED_EXPOSURE = r41_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR41_CRM_RISK_WEIGHT() {
		return R41_CRM_RISK_WEIGHT;
	}
	public void setR41_CRM_RISK_WEIGHT(BigDecimal r41_CRM_RISK_WEIGHT) {
		R41_CRM_RISK_WEIGHT = r41_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR41_RWA_CRM_COVERED() {
		return R41_RWA_CRM_COVERED;
	}
	public void setR41_RWA_CRM_COVERED(BigDecimal r41_RWA_CRM_COVERED) {
		R41_RWA_CRM_COVERED = r41_RWA_CRM_COVERED;
	}
	public BigDecimal getR41_ORIG_COUNTERPARTY_RW() {
		return R41_ORIG_COUNTERPARTY_RW;
	}
	public void setR41_ORIG_COUNTERPARTY_RW(BigDecimal r41_ORIG_COUNTERPARTY_RW) {
		R41_ORIG_COUNTERPARTY_RW = r41_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR41_RWA_CRM_NOT_COVERED() {
		return R41_RWA_CRM_NOT_COVERED;
	}
	public void setR41_RWA_CRM_NOT_COVERED(BigDecimal r41_RWA_CRM_NOT_COVERED) {
		R41_RWA_CRM_NOT_COVERED = r41_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR41_CRM_ELIG_EXPOSURE_COMP() {
		return R41_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR41_CRM_ELIG_EXPOSURE_COMP(BigDecimal r41_CRM_ELIG_EXPOSURE_COMP) {
		R41_CRM_ELIG_EXPOSURE_COMP = r41_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR41_EXPOSURE_AFTER_VOL_ADJ() {
		return R41_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR41_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r41_EXPOSURE_AFTER_VOL_ADJ) {
		R41_EXPOSURE_AFTER_VOL_ADJ = r41_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR41_COLLATERAL_CASH() {
		return R41_COLLATERAL_CASH;
	}
	public void setR41_COLLATERAL_CASH(BigDecimal r41_COLLATERAL_CASH) {
		R41_COLLATERAL_CASH = r41_COLLATERAL_CASH;
	}
	public BigDecimal getR41_COLLATERAL_TBILLS() {
		return R41_COLLATERAL_TBILLS;
	}
	public void setR41_COLLATERAL_TBILLS(BigDecimal r41_COLLATERAL_TBILLS) {
		R41_COLLATERAL_TBILLS = r41_COLLATERAL_TBILLS;
	}
	public BigDecimal getR41_COLLATERAL_DEBT_SEC() {
		return R41_COLLATERAL_DEBT_SEC;
	}
	public void setR41_COLLATERAL_DEBT_SEC(BigDecimal r41_COLLATERAL_DEBT_SEC) {
		R41_COLLATERAL_DEBT_SEC = r41_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR41_COLLATERAL_EQUITIES() {
		return R41_COLLATERAL_EQUITIES;
	}
	public void setR41_COLLATERAL_EQUITIES(BigDecimal r41_COLLATERAL_EQUITIES) {
		R41_COLLATERAL_EQUITIES = r41_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR41_COLLATERAL_MUTUAL_FUNDS() {
		return R41_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR41_COLLATERAL_MUTUAL_FUNDS(BigDecimal r41_COLLATERAL_MUTUAL_FUNDS) {
		R41_COLLATERAL_MUTUAL_FUNDS = r41_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR41_TOTAL_COLLATERAL_HAIRCUT() {
		return R41_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR41_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r41_TOTAL_COLLATERAL_HAIRCUT) {
		R41_TOTAL_COLLATERAL_HAIRCUT = r41_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR41_EXPOSURE_AFTER_CRM() {
		return R41_EXPOSURE_AFTER_CRM;
	}
	public void setR41_EXPOSURE_AFTER_CRM(BigDecimal r41_EXPOSURE_AFTER_CRM) {
		R41_EXPOSURE_AFTER_CRM = r41_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR41_RWA_NOT_COVERED_CRM() {
		return R41_RWA_NOT_COVERED_CRM;
	}
	public void setR41_RWA_NOT_COVERED_CRM(BigDecimal r41_RWA_NOT_COVERED_CRM) {
		R41_RWA_NOT_COVERED_CRM = r41_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR41_RWA_UNSECURED_EXPOSURE() {
		return R41_RWA_UNSECURED_EXPOSURE;
	}
	public void setR41_RWA_UNSECURED_EXPOSURE(BigDecimal r41_RWA_UNSECURED_EXPOSURE) {
		R41_RWA_UNSECURED_EXPOSURE = r41_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR41_RWA_UNSECURED() {
		return R41_RWA_UNSECURED;
	}
	public void setR41_RWA_UNSECURED(BigDecimal r41_RWA_UNSECURED) {
		R41_RWA_UNSECURED = r41_RWA_UNSECURED;
	}
	public BigDecimal getR41_TOTAL_RWA() {
		return R41_TOTAL_RWA;
	}
	public void setR41_TOTAL_RWA(BigDecimal r41_TOTAL_RWA) {
		R41_TOTAL_RWA = r41_TOTAL_RWA;
	}
	public BigDecimal getR42_EXPOSURE_BEFORE_CRM() {
		return R42_EXPOSURE_BEFORE_CRM;
	}
	public void setR42_EXPOSURE_BEFORE_CRM(BigDecimal r42_EXPOSURE_BEFORE_CRM) {
		R42_EXPOSURE_BEFORE_CRM = r42_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR42_SPEC_PROVISION_PAST_DUE() {
		return R42_SPEC_PROVISION_PAST_DUE;
	}
	public void setR42_SPEC_PROVISION_PAST_DUE(BigDecimal r42_SPEC_PROVISION_PAST_DUE) {
		R42_SPEC_PROVISION_PAST_DUE = r42_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR42_ON_BAL_SHEET_NETTING_ELIG() {
		return R42_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR42_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r42_ON_BAL_SHEET_NETTING_ELIG) {
		R42_ON_BAL_SHEET_NETTING_ELIG = r42_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR42_TOTAL_EXPOSURE_AFTER_NET() {
		return R42_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR42_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r42_TOTAL_EXPOSURE_AFTER_NET) {
		R42_TOTAL_EXPOSURE_AFTER_NET = r42_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR42_CRM_ELIG_EXPOSURE_SUBS() {
		return R42_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR42_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r42_CRM_ELIG_EXPOSURE_SUBS) {
		R42_CRM_ELIG_EXPOSURE_SUBS = r42_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR42_ELIG_GUARANTEES() {
		return R42_ELIG_GUARANTEES;
	}
	public void setR42_ELIG_GUARANTEES(BigDecimal r42_ELIG_GUARANTEES) {
		R42_ELIG_GUARANTEES = r42_ELIG_GUARANTEES;
	}
	public BigDecimal getR42_CREDIT_DERIVATIVES() {
		return R42_CREDIT_DERIVATIVES;
	}
	public void setR42_CREDIT_DERIVATIVES(BigDecimal r42_CREDIT_DERIVATIVES) {
		R42_CREDIT_DERIVATIVES = r42_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR42_CRM_COVERED_EXPOSURE() {
		return R42_CRM_COVERED_EXPOSURE;
	}
	public void setR42_CRM_COVERED_EXPOSURE(BigDecimal r42_CRM_COVERED_EXPOSURE) {
		R42_CRM_COVERED_EXPOSURE = r42_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR42_CRM_NOT_COVERED_EXPOSURE() {
		return R42_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR42_CRM_NOT_COVERED_EXPOSURE(BigDecimal r42_CRM_NOT_COVERED_EXPOSURE) {
		R42_CRM_NOT_COVERED_EXPOSURE = r42_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR42_CRM_RISK_WEIGHT() {
		return R42_CRM_RISK_WEIGHT;
	}
	public void setR42_CRM_RISK_WEIGHT(BigDecimal r42_CRM_RISK_WEIGHT) {
		R42_CRM_RISK_WEIGHT = r42_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR42_RWA_CRM_COVERED() {
		return R42_RWA_CRM_COVERED;
	}
	public void setR42_RWA_CRM_COVERED(BigDecimal r42_RWA_CRM_COVERED) {
		R42_RWA_CRM_COVERED = r42_RWA_CRM_COVERED;
	}
	public BigDecimal getR42_ORIG_COUNTERPARTY_RW() {
		return R42_ORIG_COUNTERPARTY_RW;
	}
	public void setR42_ORIG_COUNTERPARTY_RW(BigDecimal r42_ORIG_COUNTERPARTY_RW) {
		R42_ORIG_COUNTERPARTY_RW = r42_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR42_RWA_CRM_NOT_COVERED() {
		return R42_RWA_CRM_NOT_COVERED;
	}
	public void setR42_RWA_CRM_NOT_COVERED(BigDecimal r42_RWA_CRM_NOT_COVERED) {
		R42_RWA_CRM_NOT_COVERED = r42_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR42_CRM_ELIG_EXPOSURE_COMP() {
		return R42_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR42_CRM_ELIG_EXPOSURE_COMP(BigDecimal r42_CRM_ELIG_EXPOSURE_COMP) {
		R42_CRM_ELIG_EXPOSURE_COMP = r42_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR42_EXPOSURE_AFTER_VOL_ADJ() {
		return R42_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR42_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r42_EXPOSURE_AFTER_VOL_ADJ) {
		R42_EXPOSURE_AFTER_VOL_ADJ = r42_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR42_COLLATERAL_CASH() {
		return R42_COLLATERAL_CASH;
	}
	public void setR42_COLLATERAL_CASH(BigDecimal r42_COLLATERAL_CASH) {
		R42_COLLATERAL_CASH = r42_COLLATERAL_CASH;
	}
	public BigDecimal getR42_COLLATERAL_TBILLS() {
		return R42_COLLATERAL_TBILLS;
	}
	public void setR42_COLLATERAL_TBILLS(BigDecimal r42_COLLATERAL_TBILLS) {
		R42_COLLATERAL_TBILLS = r42_COLLATERAL_TBILLS;
	}
	public BigDecimal getR42_COLLATERAL_DEBT_SEC() {
		return R42_COLLATERAL_DEBT_SEC;
	}
	public void setR42_COLLATERAL_DEBT_SEC(BigDecimal r42_COLLATERAL_DEBT_SEC) {
		R42_COLLATERAL_DEBT_SEC = r42_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR42_COLLATERAL_EQUITIES() {
		return R42_COLLATERAL_EQUITIES;
	}
	public void setR42_COLLATERAL_EQUITIES(BigDecimal r42_COLLATERAL_EQUITIES) {
		R42_COLLATERAL_EQUITIES = r42_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR42_COLLATERAL_MUTUAL_FUNDS() {
		return R42_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR42_COLLATERAL_MUTUAL_FUNDS(BigDecimal r42_COLLATERAL_MUTUAL_FUNDS) {
		R42_COLLATERAL_MUTUAL_FUNDS = r42_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR42_TOTAL_COLLATERAL_HAIRCUT() {
		return R42_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR42_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r42_TOTAL_COLLATERAL_HAIRCUT) {
		R42_TOTAL_COLLATERAL_HAIRCUT = r42_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR42_EXPOSURE_AFTER_CRM() {
		return R42_EXPOSURE_AFTER_CRM;
	}
	public void setR42_EXPOSURE_AFTER_CRM(BigDecimal r42_EXPOSURE_AFTER_CRM) {
		R42_EXPOSURE_AFTER_CRM = r42_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR42_RWA_NOT_COVERED_CRM() {
		return R42_RWA_NOT_COVERED_CRM;
	}
	public void setR42_RWA_NOT_COVERED_CRM(BigDecimal r42_RWA_NOT_COVERED_CRM) {
		R42_RWA_NOT_COVERED_CRM = r42_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR42_RWA_UNSECURED_EXPOSURE() {
		return R42_RWA_UNSECURED_EXPOSURE;
	}
	public void setR42_RWA_UNSECURED_EXPOSURE(BigDecimal r42_RWA_UNSECURED_EXPOSURE) {
		R42_RWA_UNSECURED_EXPOSURE = r42_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR42_RWA_UNSECURED() {
		return R42_RWA_UNSECURED;
	}
	public void setR42_RWA_UNSECURED(BigDecimal r42_RWA_UNSECURED) {
		R42_RWA_UNSECURED = r42_RWA_UNSECURED;
	}
	public BigDecimal getR42_TOTAL_RWA() {
		return R42_TOTAL_RWA;
	}
	public void setR42_TOTAL_RWA(BigDecimal r42_TOTAL_RWA) {
		R42_TOTAL_RWA = r42_TOTAL_RWA;
	}
	public BigDecimal getR43_EXPOSURE_BEFORE_CRM() {
		return R43_EXPOSURE_BEFORE_CRM;
	}
	public void setR43_EXPOSURE_BEFORE_CRM(BigDecimal r43_EXPOSURE_BEFORE_CRM) {
		R43_EXPOSURE_BEFORE_CRM = r43_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR43_SPEC_PROVISION_PAST_DUE() {
		return R43_SPEC_PROVISION_PAST_DUE;
	}
	public void setR43_SPEC_PROVISION_PAST_DUE(BigDecimal r43_SPEC_PROVISION_PAST_DUE) {
		R43_SPEC_PROVISION_PAST_DUE = r43_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR43_ON_BAL_SHEET_NETTING_ELIG() {
		return R43_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR43_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r43_ON_BAL_SHEET_NETTING_ELIG) {
		R43_ON_BAL_SHEET_NETTING_ELIG = r43_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR43_TOTAL_EXPOSURE_AFTER_NET() {
		return R43_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR43_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r43_TOTAL_EXPOSURE_AFTER_NET) {
		R43_TOTAL_EXPOSURE_AFTER_NET = r43_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR43_CRM_ELIG_EXPOSURE_SUBS() {
		return R43_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR43_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r43_CRM_ELIG_EXPOSURE_SUBS) {
		R43_CRM_ELIG_EXPOSURE_SUBS = r43_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR43_ELIG_GUARANTEES() {
		return R43_ELIG_GUARANTEES;
	}
	public void setR43_ELIG_GUARANTEES(BigDecimal r43_ELIG_GUARANTEES) {
		R43_ELIG_GUARANTEES = r43_ELIG_GUARANTEES;
	}
	public BigDecimal getR43_CREDIT_DERIVATIVES() {
		return R43_CREDIT_DERIVATIVES;
	}
	public void setR43_CREDIT_DERIVATIVES(BigDecimal r43_CREDIT_DERIVATIVES) {
		R43_CREDIT_DERIVATIVES = r43_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR43_CRM_COVERED_EXPOSURE() {
		return R43_CRM_COVERED_EXPOSURE;
	}
	public void setR43_CRM_COVERED_EXPOSURE(BigDecimal r43_CRM_COVERED_EXPOSURE) {
		R43_CRM_COVERED_EXPOSURE = r43_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR43_CRM_NOT_COVERED_EXPOSURE() {
		return R43_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR43_CRM_NOT_COVERED_EXPOSURE(BigDecimal r43_CRM_NOT_COVERED_EXPOSURE) {
		R43_CRM_NOT_COVERED_EXPOSURE = r43_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR43_CRM_RISK_WEIGHT() {
		return R43_CRM_RISK_WEIGHT;
	}
	public void setR43_CRM_RISK_WEIGHT(BigDecimal r43_CRM_RISK_WEIGHT) {
		R43_CRM_RISK_WEIGHT = r43_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR43_RWA_CRM_COVERED() {
		return R43_RWA_CRM_COVERED;
	}
	public void setR43_RWA_CRM_COVERED(BigDecimal r43_RWA_CRM_COVERED) {
		R43_RWA_CRM_COVERED = r43_RWA_CRM_COVERED;
	}
	public BigDecimal getR43_ORIG_COUNTERPARTY_RW() {
		return R43_ORIG_COUNTERPARTY_RW;
	}
	public void setR43_ORIG_COUNTERPARTY_RW(BigDecimal r43_ORIG_COUNTERPARTY_RW) {
		R43_ORIG_COUNTERPARTY_RW = r43_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR43_RWA_CRM_NOT_COVERED() {
		return R43_RWA_CRM_NOT_COVERED;
	}
	public void setR43_RWA_CRM_NOT_COVERED(BigDecimal r43_RWA_CRM_NOT_COVERED) {
		R43_RWA_CRM_NOT_COVERED = r43_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR43_CRM_ELIG_EXPOSURE_COMP() {
		return R43_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR43_CRM_ELIG_EXPOSURE_COMP(BigDecimal r43_CRM_ELIG_EXPOSURE_COMP) {
		R43_CRM_ELIG_EXPOSURE_COMP = r43_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR43_EXPOSURE_AFTER_VOL_ADJ() {
		return R43_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR43_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r43_EXPOSURE_AFTER_VOL_ADJ) {
		R43_EXPOSURE_AFTER_VOL_ADJ = r43_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR43_COLLATERAL_CASH() {
		return R43_COLLATERAL_CASH;
	}
	public void setR43_COLLATERAL_CASH(BigDecimal r43_COLLATERAL_CASH) {
		R43_COLLATERAL_CASH = r43_COLLATERAL_CASH;
	}
	public BigDecimal getR43_COLLATERAL_TBILLS() {
		return R43_COLLATERAL_TBILLS;
	}
	public void setR43_COLLATERAL_TBILLS(BigDecimal r43_COLLATERAL_TBILLS) {
		R43_COLLATERAL_TBILLS = r43_COLLATERAL_TBILLS;
	}
	public BigDecimal getR43_COLLATERAL_DEBT_SEC() {
		return R43_COLLATERAL_DEBT_SEC;
	}
	public void setR43_COLLATERAL_DEBT_SEC(BigDecimal r43_COLLATERAL_DEBT_SEC) {
		R43_COLLATERAL_DEBT_SEC = r43_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR43_COLLATERAL_EQUITIES() {
		return R43_COLLATERAL_EQUITIES;
	}
	public void setR43_COLLATERAL_EQUITIES(BigDecimal r43_COLLATERAL_EQUITIES) {
		R43_COLLATERAL_EQUITIES = r43_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR43_COLLATERAL_MUTUAL_FUNDS() {
		return R43_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR43_COLLATERAL_MUTUAL_FUNDS(BigDecimal r43_COLLATERAL_MUTUAL_FUNDS) {
		R43_COLLATERAL_MUTUAL_FUNDS = r43_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR43_TOTAL_COLLATERAL_HAIRCUT() {
		return R43_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR43_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r43_TOTAL_COLLATERAL_HAIRCUT) {
		R43_TOTAL_COLLATERAL_HAIRCUT = r43_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR43_EXPOSURE_AFTER_CRM() {
		return R43_EXPOSURE_AFTER_CRM;
	}
	public void setR43_EXPOSURE_AFTER_CRM(BigDecimal r43_EXPOSURE_AFTER_CRM) {
		R43_EXPOSURE_AFTER_CRM = r43_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR43_RWA_NOT_COVERED_CRM() {
		return R43_RWA_NOT_COVERED_CRM;
	}
	public void setR43_RWA_NOT_COVERED_CRM(BigDecimal r43_RWA_NOT_COVERED_CRM) {
		R43_RWA_NOT_COVERED_CRM = r43_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR43_RWA_UNSECURED_EXPOSURE() {
		return R43_RWA_UNSECURED_EXPOSURE;
	}
	public void setR43_RWA_UNSECURED_EXPOSURE(BigDecimal r43_RWA_UNSECURED_EXPOSURE) {
		R43_RWA_UNSECURED_EXPOSURE = r43_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR43_RWA_UNSECURED() {
		return R43_RWA_UNSECURED;
	}
	public void setR43_RWA_UNSECURED(BigDecimal r43_RWA_UNSECURED) {
		R43_RWA_UNSECURED = r43_RWA_UNSECURED;
	}
	public BigDecimal getR43_TOTAL_RWA() {
		return R43_TOTAL_RWA;
	}
	public void setR43_TOTAL_RWA(BigDecimal r43_TOTAL_RWA) {
		R43_TOTAL_RWA = r43_TOTAL_RWA;
	}
	public BigDecimal getR44_EXPOSURE_BEFORE_CRM() {
		return R44_EXPOSURE_BEFORE_CRM;
	}
	public void setR44_EXPOSURE_BEFORE_CRM(BigDecimal r44_EXPOSURE_BEFORE_CRM) {
		R44_EXPOSURE_BEFORE_CRM = r44_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR44_SPEC_PROVISION_PAST_DUE() {
		return R44_SPEC_PROVISION_PAST_DUE;
	}
	public void setR44_SPEC_PROVISION_PAST_DUE(BigDecimal r44_SPEC_PROVISION_PAST_DUE) {
		R44_SPEC_PROVISION_PAST_DUE = r44_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR44_ON_BAL_SHEET_NETTING_ELIG() {
		return R44_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR44_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r44_ON_BAL_SHEET_NETTING_ELIG) {
		R44_ON_BAL_SHEET_NETTING_ELIG = r44_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR44_TOTAL_EXPOSURE_AFTER_NET() {
		return R44_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR44_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r44_TOTAL_EXPOSURE_AFTER_NET) {
		R44_TOTAL_EXPOSURE_AFTER_NET = r44_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR44_CRM_ELIG_EXPOSURE_SUBS() {
		return R44_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR44_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r44_CRM_ELIG_EXPOSURE_SUBS) {
		R44_CRM_ELIG_EXPOSURE_SUBS = r44_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR44_ELIG_GUARANTEES() {
		return R44_ELIG_GUARANTEES;
	}
	public void setR44_ELIG_GUARANTEES(BigDecimal r44_ELIG_GUARANTEES) {
		R44_ELIG_GUARANTEES = r44_ELIG_GUARANTEES;
	}
	public BigDecimal getR44_CREDIT_DERIVATIVES() {
		return R44_CREDIT_DERIVATIVES;
	}
	public void setR44_CREDIT_DERIVATIVES(BigDecimal r44_CREDIT_DERIVATIVES) {
		R44_CREDIT_DERIVATIVES = r44_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR44_CRM_COVERED_EXPOSURE() {
		return R44_CRM_COVERED_EXPOSURE;
	}
	public void setR44_CRM_COVERED_EXPOSURE(BigDecimal r44_CRM_COVERED_EXPOSURE) {
		R44_CRM_COVERED_EXPOSURE = r44_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR44_CRM_NOT_COVERED_EXPOSURE() {
		return R44_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR44_CRM_NOT_COVERED_EXPOSURE(BigDecimal r44_CRM_NOT_COVERED_EXPOSURE) {
		R44_CRM_NOT_COVERED_EXPOSURE = r44_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR44_CRM_RISK_WEIGHT() {
		return R44_CRM_RISK_WEIGHT;
	}
	public void setR44_CRM_RISK_WEIGHT(BigDecimal r44_CRM_RISK_WEIGHT) {
		R44_CRM_RISK_WEIGHT = r44_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR44_RWA_CRM_COVERED() {
		return R44_RWA_CRM_COVERED;
	}
	public void setR44_RWA_CRM_COVERED(BigDecimal r44_RWA_CRM_COVERED) {
		R44_RWA_CRM_COVERED = r44_RWA_CRM_COVERED;
	}
	public BigDecimal getR44_ORIG_COUNTERPARTY_RW() {
		return R44_ORIG_COUNTERPARTY_RW;
	}
	public void setR44_ORIG_COUNTERPARTY_RW(BigDecimal r44_ORIG_COUNTERPARTY_RW) {
		R44_ORIG_COUNTERPARTY_RW = r44_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR44_RWA_CRM_NOT_COVERED() {
		return R44_RWA_CRM_NOT_COVERED;
	}
	public void setR44_RWA_CRM_NOT_COVERED(BigDecimal r44_RWA_CRM_NOT_COVERED) {
		R44_RWA_CRM_NOT_COVERED = r44_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR44_CRM_ELIG_EXPOSURE_COMP() {
		return R44_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR44_CRM_ELIG_EXPOSURE_COMP(BigDecimal r44_CRM_ELIG_EXPOSURE_COMP) {
		R44_CRM_ELIG_EXPOSURE_COMP = r44_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR44_EXPOSURE_AFTER_VOL_ADJ() {
		return R44_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR44_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r44_EXPOSURE_AFTER_VOL_ADJ) {
		R44_EXPOSURE_AFTER_VOL_ADJ = r44_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR44_COLLATERAL_CASH() {
		return R44_COLLATERAL_CASH;
	}
	public void setR44_COLLATERAL_CASH(BigDecimal r44_COLLATERAL_CASH) {
		R44_COLLATERAL_CASH = r44_COLLATERAL_CASH;
	}
	public BigDecimal getR44_COLLATERAL_TBILLS() {
		return R44_COLLATERAL_TBILLS;
	}
	public void setR44_COLLATERAL_TBILLS(BigDecimal r44_COLLATERAL_TBILLS) {
		R44_COLLATERAL_TBILLS = r44_COLLATERAL_TBILLS;
	}
	public BigDecimal getR44_COLLATERAL_DEBT_SEC() {
		return R44_COLLATERAL_DEBT_SEC;
	}
	public void setR44_COLLATERAL_DEBT_SEC(BigDecimal r44_COLLATERAL_DEBT_SEC) {
		R44_COLLATERAL_DEBT_SEC = r44_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR44_COLLATERAL_EQUITIES() {
		return R44_COLLATERAL_EQUITIES;
	}
	public void setR44_COLLATERAL_EQUITIES(BigDecimal r44_COLLATERAL_EQUITIES) {
		R44_COLLATERAL_EQUITIES = r44_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR44_COLLATERAL_MUTUAL_FUNDS() {
		return R44_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR44_COLLATERAL_MUTUAL_FUNDS(BigDecimal r44_COLLATERAL_MUTUAL_FUNDS) {
		R44_COLLATERAL_MUTUAL_FUNDS = r44_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR44_TOTAL_COLLATERAL_HAIRCUT() {
		return R44_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR44_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r44_TOTAL_COLLATERAL_HAIRCUT) {
		R44_TOTAL_COLLATERAL_HAIRCUT = r44_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR44_EXPOSURE_AFTER_CRM() {
		return R44_EXPOSURE_AFTER_CRM;
	}
	public void setR44_EXPOSURE_AFTER_CRM(BigDecimal r44_EXPOSURE_AFTER_CRM) {
		R44_EXPOSURE_AFTER_CRM = r44_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR44_RWA_NOT_COVERED_CRM() {
		return R44_RWA_NOT_COVERED_CRM;
	}
	public void setR44_RWA_NOT_COVERED_CRM(BigDecimal r44_RWA_NOT_COVERED_CRM) {
		R44_RWA_NOT_COVERED_CRM = r44_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR44_RWA_UNSECURED_EXPOSURE() {
		return R44_RWA_UNSECURED_EXPOSURE;
	}
	public void setR44_RWA_UNSECURED_EXPOSURE(BigDecimal r44_RWA_UNSECURED_EXPOSURE) {
		R44_RWA_UNSECURED_EXPOSURE = r44_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR44_RWA_UNSECURED() {
		return R44_RWA_UNSECURED;
	}
	public void setR44_RWA_UNSECURED(BigDecimal r44_RWA_UNSECURED) {
		R44_RWA_UNSECURED = r44_RWA_UNSECURED;
	}
	public BigDecimal getR44_TOTAL_RWA() {
		return R44_TOTAL_RWA;
	}
	public void setR44_TOTAL_RWA(BigDecimal r44_TOTAL_RWA) {
		R44_TOTAL_RWA = r44_TOTAL_RWA;
	}
	public BigDecimal getR45_EXPOSURE_BEFORE_CRM() {
		return R45_EXPOSURE_BEFORE_CRM;
	}
	public void setR45_EXPOSURE_BEFORE_CRM(BigDecimal r45_EXPOSURE_BEFORE_CRM) {
		R45_EXPOSURE_BEFORE_CRM = r45_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR45_SPEC_PROVISION_PAST_DUE() {
		return R45_SPEC_PROVISION_PAST_DUE;
	}
	public void setR45_SPEC_PROVISION_PAST_DUE(BigDecimal r45_SPEC_PROVISION_PAST_DUE) {
		R45_SPEC_PROVISION_PAST_DUE = r45_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR45_ON_BAL_SHEET_NETTING_ELIG() {
		return R45_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR45_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r45_ON_BAL_SHEET_NETTING_ELIG) {
		R45_ON_BAL_SHEET_NETTING_ELIG = r45_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR45_TOTAL_EXPOSURE_AFTER_NET() {
		return R45_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR45_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r45_TOTAL_EXPOSURE_AFTER_NET) {
		R45_TOTAL_EXPOSURE_AFTER_NET = r45_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR45_CRM_ELIG_EXPOSURE_SUBS() {
		return R45_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR45_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r45_CRM_ELIG_EXPOSURE_SUBS) {
		R45_CRM_ELIG_EXPOSURE_SUBS = r45_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR45_ELIG_GUARANTEES() {
		return R45_ELIG_GUARANTEES;
	}
	public void setR45_ELIG_GUARANTEES(BigDecimal r45_ELIG_GUARANTEES) {
		R45_ELIG_GUARANTEES = r45_ELIG_GUARANTEES;
	}
	public BigDecimal getR45_CREDIT_DERIVATIVES() {
		return R45_CREDIT_DERIVATIVES;
	}
	public void setR45_CREDIT_DERIVATIVES(BigDecimal r45_CREDIT_DERIVATIVES) {
		R45_CREDIT_DERIVATIVES = r45_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR45_CRM_COVERED_EXPOSURE() {
		return R45_CRM_COVERED_EXPOSURE;
	}
	public void setR45_CRM_COVERED_EXPOSURE(BigDecimal r45_CRM_COVERED_EXPOSURE) {
		R45_CRM_COVERED_EXPOSURE = r45_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR45_CRM_NOT_COVERED_EXPOSURE() {
		return R45_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR45_CRM_NOT_COVERED_EXPOSURE(BigDecimal r45_CRM_NOT_COVERED_EXPOSURE) {
		R45_CRM_NOT_COVERED_EXPOSURE = r45_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR45_CRM_RISK_WEIGHT() {
		return R45_CRM_RISK_WEIGHT;
	}
	public void setR45_CRM_RISK_WEIGHT(BigDecimal r45_CRM_RISK_WEIGHT) {
		R45_CRM_RISK_WEIGHT = r45_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR45_RWA_CRM_COVERED() {
		return R45_RWA_CRM_COVERED;
	}
	public void setR45_RWA_CRM_COVERED(BigDecimal r45_RWA_CRM_COVERED) {
		R45_RWA_CRM_COVERED = r45_RWA_CRM_COVERED;
	}
	public BigDecimal getR45_ORIG_COUNTERPARTY_RW() {
		return R45_ORIG_COUNTERPARTY_RW;
	}
	public void setR45_ORIG_COUNTERPARTY_RW(BigDecimal r45_ORIG_COUNTERPARTY_RW) {
		R45_ORIG_COUNTERPARTY_RW = r45_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR45_RWA_CRM_NOT_COVERED() {
		return R45_RWA_CRM_NOT_COVERED;
	}
	public void setR45_RWA_CRM_NOT_COVERED(BigDecimal r45_RWA_CRM_NOT_COVERED) {
		R45_RWA_CRM_NOT_COVERED = r45_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR45_CRM_ELIG_EXPOSURE_COMP() {
		return R45_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR45_CRM_ELIG_EXPOSURE_COMP(BigDecimal r45_CRM_ELIG_EXPOSURE_COMP) {
		R45_CRM_ELIG_EXPOSURE_COMP = r45_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR45_EXPOSURE_AFTER_VOL_ADJ() {
		return R45_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR45_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r45_EXPOSURE_AFTER_VOL_ADJ) {
		R45_EXPOSURE_AFTER_VOL_ADJ = r45_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR45_COLLATERAL_CASH() {
		return R45_COLLATERAL_CASH;
	}
	public void setR45_COLLATERAL_CASH(BigDecimal r45_COLLATERAL_CASH) {
		R45_COLLATERAL_CASH = r45_COLLATERAL_CASH;
	}
	public BigDecimal getR45_COLLATERAL_TBILLS() {
		return R45_COLLATERAL_TBILLS;
	}
	public void setR45_COLLATERAL_TBILLS(BigDecimal r45_COLLATERAL_TBILLS) {
		R45_COLLATERAL_TBILLS = r45_COLLATERAL_TBILLS;
	}
	public BigDecimal getR45_COLLATERAL_DEBT_SEC() {
		return R45_COLLATERAL_DEBT_SEC;
	}
	public void setR45_COLLATERAL_DEBT_SEC(BigDecimal r45_COLLATERAL_DEBT_SEC) {
		R45_COLLATERAL_DEBT_SEC = r45_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR45_COLLATERAL_EQUITIES() {
		return R45_COLLATERAL_EQUITIES;
	}
	public void setR45_COLLATERAL_EQUITIES(BigDecimal r45_COLLATERAL_EQUITIES) {
		R45_COLLATERAL_EQUITIES = r45_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR45_COLLATERAL_MUTUAL_FUNDS() {
		return R45_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR45_COLLATERAL_MUTUAL_FUNDS(BigDecimal r45_COLLATERAL_MUTUAL_FUNDS) {
		R45_COLLATERAL_MUTUAL_FUNDS = r45_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR45_TOTAL_COLLATERAL_HAIRCUT() {
		return R45_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR45_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r45_TOTAL_COLLATERAL_HAIRCUT) {
		R45_TOTAL_COLLATERAL_HAIRCUT = r45_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR45_EXPOSURE_AFTER_CRM() {
		return R45_EXPOSURE_AFTER_CRM;
	}
	public void setR45_EXPOSURE_AFTER_CRM(BigDecimal r45_EXPOSURE_AFTER_CRM) {
		R45_EXPOSURE_AFTER_CRM = r45_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR45_RWA_NOT_COVERED_CRM() {
		return R45_RWA_NOT_COVERED_CRM;
	}
	public void setR45_RWA_NOT_COVERED_CRM(BigDecimal r45_RWA_NOT_COVERED_CRM) {
		R45_RWA_NOT_COVERED_CRM = r45_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR45_RWA_UNSECURED_EXPOSURE() {
		return R45_RWA_UNSECURED_EXPOSURE;
	}
	public void setR45_RWA_UNSECURED_EXPOSURE(BigDecimal r45_RWA_UNSECURED_EXPOSURE) {
		R45_RWA_UNSECURED_EXPOSURE = r45_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR45_RWA_UNSECURED() {
		return R45_RWA_UNSECURED;
	}
	public void setR45_RWA_UNSECURED(BigDecimal r45_RWA_UNSECURED) {
		R45_RWA_UNSECURED = r45_RWA_UNSECURED;
	}
	public BigDecimal getR45_TOTAL_RWA() {
		return R45_TOTAL_RWA;
	}
	public void setR45_TOTAL_RWA(BigDecimal r45_TOTAL_RWA) {
		R45_TOTAL_RWA = r45_TOTAL_RWA;
	}
	public BigDecimal getR46_EXPOSURE_BEFORE_CRM() {
		return R46_EXPOSURE_BEFORE_CRM;
	}
	public void setR46_EXPOSURE_BEFORE_CRM(BigDecimal r46_EXPOSURE_BEFORE_CRM) {
		R46_EXPOSURE_BEFORE_CRM = r46_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR46_SPEC_PROVISION_PAST_DUE() {
		return R46_SPEC_PROVISION_PAST_DUE;
	}
	public void setR46_SPEC_PROVISION_PAST_DUE(BigDecimal r46_SPEC_PROVISION_PAST_DUE) {
		R46_SPEC_PROVISION_PAST_DUE = r46_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR46_ON_BAL_SHEET_NETTING_ELIG() {
		return R46_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR46_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r46_ON_BAL_SHEET_NETTING_ELIG) {
		R46_ON_BAL_SHEET_NETTING_ELIG = r46_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR46_TOTAL_EXPOSURE_AFTER_NET() {
		return R46_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR46_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r46_TOTAL_EXPOSURE_AFTER_NET) {
		R46_TOTAL_EXPOSURE_AFTER_NET = r46_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR46_CRM_ELIG_EXPOSURE_SUBS() {
		return R46_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR46_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r46_CRM_ELIG_EXPOSURE_SUBS) {
		R46_CRM_ELIG_EXPOSURE_SUBS = r46_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR46_ELIG_GUARANTEES() {
		return R46_ELIG_GUARANTEES;
	}
	public void setR46_ELIG_GUARANTEES(BigDecimal r46_ELIG_GUARANTEES) {
		R46_ELIG_GUARANTEES = r46_ELIG_GUARANTEES;
	}
	public BigDecimal getR46_CREDIT_DERIVATIVES() {
		return R46_CREDIT_DERIVATIVES;
	}
	public void setR46_CREDIT_DERIVATIVES(BigDecimal r46_CREDIT_DERIVATIVES) {
		R46_CREDIT_DERIVATIVES = r46_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR46_CRM_COVERED_EXPOSURE() {
		return R46_CRM_COVERED_EXPOSURE;
	}
	public void setR46_CRM_COVERED_EXPOSURE(BigDecimal r46_CRM_COVERED_EXPOSURE) {
		R46_CRM_COVERED_EXPOSURE = r46_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR46_CRM_NOT_COVERED_EXPOSURE() {
		return R46_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR46_CRM_NOT_COVERED_EXPOSURE(BigDecimal r46_CRM_NOT_COVERED_EXPOSURE) {
		R46_CRM_NOT_COVERED_EXPOSURE = r46_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR46_CRM_RISK_WEIGHT() {
		return R46_CRM_RISK_WEIGHT;
	}
	public void setR46_CRM_RISK_WEIGHT(BigDecimal r46_CRM_RISK_WEIGHT) {
		R46_CRM_RISK_WEIGHT = r46_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR46_RWA_CRM_COVERED() {
		return R46_RWA_CRM_COVERED;
	}
	public void setR46_RWA_CRM_COVERED(BigDecimal r46_RWA_CRM_COVERED) {
		R46_RWA_CRM_COVERED = r46_RWA_CRM_COVERED;
	}
	public BigDecimal getR46_ORIG_COUNTERPARTY_RW() {
		return R46_ORIG_COUNTERPARTY_RW;
	}
	public void setR46_ORIG_COUNTERPARTY_RW(BigDecimal r46_ORIG_COUNTERPARTY_RW) {
		R46_ORIG_COUNTERPARTY_RW = r46_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR46_RWA_CRM_NOT_COVERED() {
		return R46_RWA_CRM_NOT_COVERED;
	}
	public void setR46_RWA_CRM_NOT_COVERED(BigDecimal r46_RWA_CRM_NOT_COVERED) {
		R46_RWA_CRM_NOT_COVERED = r46_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR46_CRM_ELIG_EXPOSURE_COMP() {
		return R46_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR46_CRM_ELIG_EXPOSURE_COMP(BigDecimal r46_CRM_ELIG_EXPOSURE_COMP) {
		R46_CRM_ELIG_EXPOSURE_COMP = r46_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR46_EXPOSURE_AFTER_VOL_ADJ() {
		return R46_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR46_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r46_EXPOSURE_AFTER_VOL_ADJ) {
		R46_EXPOSURE_AFTER_VOL_ADJ = r46_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR46_COLLATERAL_CASH() {
		return R46_COLLATERAL_CASH;
	}
	public void setR46_COLLATERAL_CASH(BigDecimal r46_COLLATERAL_CASH) {
		R46_COLLATERAL_CASH = r46_COLLATERAL_CASH;
	}
	public BigDecimal getR46_COLLATERAL_TBILLS() {
		return R46_COLLATERAL_TBILLS;
	}
	public void setR46_COLLATERAL_TBILLS(BigDecimal r46_COLLATERAL_TBILLS) {
		R46_COLLATERAL_TBILLS = r46_COLLATERAL_TBILLS;
	}
	public BigDecimal getR46_COLLATERAL_DEBT_SEC() {
		return R46_COLLATERAL_DEBT_SEC;
	}
	public void setR46_COLLATERAL_DEBT_SEC(BigDecimal r46_COLLATERAL_DEBT_SEC) {
		R46_COLLATERAL_DEBT_SEC = r46_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR46_COLLATERAL_EQUITIES() {
		return R46_COLLATERAL_EQUITIES;
	}
	public void setR46_COLLATERAL_EQUITIES(BigDecimal r46_COLLATERAL_EQUITIES) {
		R46_COLLATERAL_EQUITIES = r46_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR46_COLLATERAL_MUTUAL_FUNDS() {
		return R46_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR46_COLLATERAL_MUTUAL_FUNDS(BigDecimal r46_COLLATERAL_MUTUAL_FUNDS) {
		R46_COLLATERAL_MUTUAL_FUNDS = r46_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR46_TOTAL_COLLATERAL_HAIRCUT() {
		return R46_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR46_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r46_TOTAL_COLLATERAL_HAIRCUT) {
		R46_TOTAL_COLLATERAL_HAIRCUT = r46_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR46_EXPOSURE_AFTER_CRM() {
		return R46_EXPOSURE_AFTER_CRM;
	}
	public void setR46_EXPOSURE_AFTER_CRM(BigDecimal r46_EXPOSURE_AFTER_CRM) {
		R46_EXPOSURE_AFTER_CRM = r46_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR46_RWA_NOT_COVERED_CRM() {
		return R46_RWA_NOT_COVERED_CRM;
	}
	public void setR46_RWA_NOT_COVERED_CRM(BigDecimal r46_RWA_NOT_COVERED_CRM) {
		R46_RWA_NOT_COVERED_CRM = r46_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR46_RWA_UNSECURED_EXPOSURE() {
		return R46_RWA_UNSECURED_EXPOSURE;
	}
	public void setR46_RWA_UNSECURED_EXPOSURE(BigDecimal r46_RWA_UNSECURED_EXPOSURE) {
		R46_RWA_UNSECURED_EXPOSURE = r46_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR46_RWA_UNSECURED() {
		return R46_RWA_UNSECURED;
	}
	public void setR46_RWA_UNSECURED(BigDecimal r46_RWA_UNSECURED) {
		R46_RWA_UNSECURED = r46_RWA_UNSECURED;
	}
	public BigDecimal getR46_TOTAL_RWA() {
		return R46_TOTAL_RWA;
	}
	public void setR46_TOTAL_RWA(BigDecimal r46_TOTAL_RWA) {
		R46_TOTAL_RWA = r46_TOTAL_RWA;
	}
	public BigDecimal getR47_EXPOSURE_BEFORE_CRM() {
		return R47_EXPOSURE_BEFORE_CRM;
	}
	public void setR47_EXPOSURE_BEFORE_CRM(BigDecimal r47_EXPOSURE_BEFORE_CRM) {
		R47_EXPOSURE_BEFORE_CRM = r47_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR47_SPEC_PROVISION_PAST_DUE() {
		return R47_SPEC_PROVISION_PAST_DUE;
	}
	public void setR47_SPEC_PROVISION_PAST_DUE(BigDecimal r47_SPEC_PROVISION_PAST_DUE) {
		R47_SPEC_PROVISION_PAST_DUE = r47_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR47_ON_BAL_SHEET_NETTING_ELIG() {
		return R47_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR47_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r47_ON_BAL_SHEET_NETTING_ELIG) {
		R47_ON_BAL_SHEET_NETTING_ELIG = r47_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR47_TOTAL_EXPOSURE_AFTER_NET() {
		return R47_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR47_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r47_TOTAL_EXPOSURE_AFTER_NET) {
		R47_TOTAL_EXPOSURE_AFTER_NET = r47_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR47_CRM_ELIG_EXPOSURE_SUBS() {
		return R47_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR47_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r47_CRM_ELIG_EXPOSURE_SUBS) {
		R47_CRM_ELIG_EXPOSURE_SUBS = r47_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR47_ELIG_GUARANTEES() {
		return R47_ELIG_GUARANTEES;
	}
	public void setR47_ELIG_GUARANTEES(BigDecimal r47_ELIG_GUARANTEES) {
		R47_ELIG_GUARANTEES = r47_ELIG_GUARANTEES;
	}
	public BigDecimal getR47_CREDIT_DERIVATIVES() {
		return R47_CREDIT_DERIVATIVES;
	}
	public void setR47_CREDIT_DERIVATIVES(BigDecimal r47_CREDIT_DERIVATIVES) {
		R47_CREDIT_DERIVATIVES = r47_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR47_CRM_COVERED_EXPOSURE() {
		return R47_CRM_COVERED_EXPOSURE;
	}
	public void setR47_CRM_COVERED_EXPOSURE(BigDecimal r47_CRM_COVERED_EXPOSURE) {
		R47_CRM_COVERED_EXPOSURE = r47_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR47_CRM_NOT_COVERED_EXPOSURE() {
		return R47_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR47_CRM_NOT_COVERED_EXPOSURE(BigDecimal r47_CRM_NOT_COVERED_EXPOSURE) {
		R47_CRM_NOT_COVERED_EXPOSURE = r47_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR47_CRM_RISK_WEIGHT() {
		return R47_CRM_RISK_WEIGHT;
	}
	public void setR47_CRM_RISK_WEIGHT(BigDecimal r47_CRM_RISK_WEIGHT) {
		R47_CRM_RISK_WEIGHT = r47_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR47_RWA_CRM_COVERED() {
		return R47_RWA_CRM_COVERED;
	}
	public void setR47_RWA_CRM_COVERED(BigDecimal r47_RWA_CRM_COVERED) {
		R47_RWA_CRM_COVERED = r47_RWA_CRM_COVERED;
	}
	public BigDecimal getR47_ORIG_COUNTERPARTY_RW() {
		return R47_ORIG_COUNTERPARTY_RW;
	}
	public void setR47_ORIG_COUNTERPARTY_RW(BigDecimal r47_ORIG_COUNTERPARTY_RW) {
		R47_ORIG_COUNTERPARTY_RW = r47_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR47_RWA_CRM_NOT_COVERED() {
		return R47_RWA_CRM_NOT_COVERED;
	}
	public void setR47_RWA_CRM_NOT_COVERED(BigDecimal r47_RWA_CRM_NOT_COVERED) {
		R47_RWA_CRM_NOT_COVERED = r47_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR47_CRM_ELIG_EXPOSURE_COMP() {
		return R47_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR47_CRM_ELIG_EXPOSURE_COMP(BigDecimal r47_CRM_ELIG_EXPOSURE_COMP) {
		R47_CRM_ELIG_EXPOSURE_COMP = r47_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR47_EXPOSURE_AFTER_VOL_ADJ() {
		return R47_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR47_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r47_EXPOSURE_AFTER_VOL_ADJ) {
		R47_EXPOSURE_AFTER_VOL_ADJ = r47_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR47_COLLATERAL_CASH() {
		return R47_COLLATERAL_CASH;
	}
	public void setR47_COLLATERAL_CASH(BigDecimal r47_COLLATERAL_CASH) {
		R47_COLLATERAL_CASH = r47_COLLATERAL_CASH;
	}
	public BigDecimal getR47_COLLATERAL_TBILLS() {
		return R47_COLLATERAL_TBILLS;
	}
	public void setR47_COLLATERAL_TBILLS(BigDecimal r47_COLLATERAL_TBILLS) {
		R47_COLLATERAL_TBILLS = r47_COLLATERAL_TBILLS;
	}
	public BigDecimal getR47_COLLATERAL_DEBT_SEC() {
		return R47_COLLATERAL_DEBT_SEC;
	}
	public void setR47_COLLATERAL_DEBT_SEC(BigDecimal r47_COLLATERAL_DEBT_SEC) {
		R47_COLLATERAL_DEBT_SEC = r47_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR47_COLLATERAL_EQUITIES() {
		return R47_COLLATERAL_EQUITIES;
	}
	public void setR47_COLLATERAL_EQUITIES(BigDecimal r47_COLLATERAL_EQUITIES) {
		R47_COLLATERAL_EQUITIES = r47_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR47_COLLATERAL_MUTUAL_FUNDS() {
		return R47_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR47_COLLATERAL_MUTUAL_FUNDS(BigDecimal r47_COLLATERAL_MUTUAL_FUNDS) {
		R47_COLLATERAL_MUTUAL_FUNDS = r47_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR47_TOTAL_COLLATERAL_HAIRCUT() {
		return R47_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR47_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r47_TOTAL_COLLATERAL_HAIRCUT) {
		R47_TOTAL_COLLATERAL_HAIRCUT = r47_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR47_EXPOSURE_AFTER_CRM() {
		return R47_EXPOSURE_AFTER_CRM;
	}
	public void setR47_EXPOSURE_AFTER_CRM(BigDecimal r47_EXPOSURE_AFTER_CRM) {
		R47_EXPOSURE_AFTER_CRM = r47_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR47_RWA_NOT_COVERED_CRM() {
		return R47_RWA_NOT_COVERED_CRM;
	}
	public void setR47_RWA_NOT_COVERED_CRM(BigDecimal r47_RWA_NOT_COVERED_CRM) {
		R47_RWA_NOT_COVERED_CRM = r47_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR47_RWA_UNSECURED_EXPOSURE() {
		return R47_RWA_UNSECURED_EXPOSURE;
	}
	public void setR47_RWA_UNSECURED_EXPOSURE(BigDecimal r47_RWA_UNSECURED_EXPOSURE) {
		R47_RWA_UNSECURED_EXPOSURE = r47_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR47_RWA_UNSECURED() {
		return R47_RWA_UNSECURED;
	}
	public void setR47_RWA_UNSECURED(BigDecimal r47_RWA_UNSECURED) {
		R47_RWA_UNSECURED = r47_RWA_UNSECURED;
	}
	public BigDecimal getR47_TOTAL_RWA() {
		return R47_TOTAL_RWA;
	}
	public void setR47_TOTAL_RWA(BigDecimal r47_TOTAL_RWA) {
		R47_TOTAL_RWA = r47_TOTAL_RWA;
	}
	public BigDecimal getR48_EXPOSURE_BEFORE_CRM() {
		return R48_EXPOSURE_BEFORE_CRM;
	}
	public void setR48_EXPOSURE_BEFORE_CRM(BigDecimal r48_EXPOSURE_BEFORE_CRM) {
		R48_EXPOSURE_BEFORE_CRM = r48_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR48_SPEC_PROVISION_PAST_DUE() {
		return R48_SPEC_PROVISION_PAST_DUE;
	}
	public void setR48_SPEC_PROVISION_PAST_DUE(BigDecimal r48_SPEC_PROVISION_PAST_DUE) {
		R48_SPEC_PROVISION_PAST_DUE = r48_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR48_ON_BAL_SHEET_NETTING_ELIG() {
		return R48_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR48_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r48_ON_BAL_SHEET_NETTING_ELIG) {
		R48_ON_BAL_SHEET_NETTING_ELIG = r48_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR48_TOTAL_EXPOSURE_AFTER_NET() {
		return R48_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR48_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r48_TOTAL_EXPOSURE_AFTER_NET) {
		R48_TOTAL_EXPOSURE_AFTER_NET = r48_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR48_CRM_ELIG_EXPOSURE_SUBS() {
		return R48_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR48_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r48_CRM_ELIG_EXPOSURE_SUBS) {
		R48_CRM_ELIG_EXPOSURE_SUBS = r48_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR48_ELIG_GUARANTEES() {
		return R48_ELIG_GUARANTEES;
	}
	public void setR48_ELIG_GUARANTEES(BigDecimal r48_ELIG_GUARANTEES) {
		R48_ELIG_GUARANTEES = r48_ELIG_GUARANTEES;
	}
	public BigDecimal getR48_CREDIT_DERIVATIVES() {
		return R48_CREDIT_DERIVATIVES;
	}
	public void setR48_CREDIT_DERIVATIVES(BigDecimal r48_CREDIT_DERIVATIVES) {
		R48_CREDIT_DERIVATIVES = r48_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR48_CRM_COVERED_EXPOSURE() {
		return R48_CRM_COVERED_EXPOSURE;
	}
	public void setR48_CRM_COVERED_EXPOSURE(BigDecimal r48_CRM_COVERED_EXPOSURE) {
		R48_CRM_COVERED_EXPOSURE = r48_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR48_CRM_NOT_COVERED_EXPOSURE() {
		return R48_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR48_CRM_NOT_COVERED_EXPOSURE(BigDecimal r48_CRM_NOT_COVERED_EXPOSURE) {
		R48_CRM_NOT_COVERED_EXPOSURE = r48_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR48_CRM_RISK_WEIGHT() {
		return R48_CRM_RISK_WEIGHT;
	}
	public void setR48_CRM_RISK_WEIGHT(BigDecimal r48_CRM_RISK_WEIGHT) {
		R48_CRM_RISK_WEIGHT = r48_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR48_RWA_CRM_COVERED() {
		return R48_RWA_CRM_COVERED;
	}
	public void setR48_RWA_CRM_COVERED(BigDecimal r48_RWA_CRM_COVERED) {
		R48_RWA_CRM_COVERED = r48_RWA_CRM_COVERED;
	}
	public BigDecimal getR48_ORIG_COUNTERPARTY_RW() {
		return R48_ORIG_COUNTERPARTY_RW;
	}
	public void setR48_ORIG_COUNTERPARTY_RW(BigDecimal r48_ORIG_COUNTERPARTY_RW) {
		R48_ORIG_COUNTERPARTY_RW = r48_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR48_RWA_CRM_NOT_COVERED() {
		return R48_RWA_CRM_NOT_COVERED;
	}
	public void setR48_RWA_CRM_NOT_COVERED(BigDecimal r48_RWA_CRM_NOT_COVERED) {
		R48_RWA_CRM_NOT_COVERED = r48_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR48_CRM_ELIG_EXPOSURE_COMP() {
		return R48_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR48_CRM_ELIG_EXPOSURE_COMP(BigDecimal r48_CRM_ELIG_EXPOSURE_COMP) {
		R48_CRM_ELIG_EXPOSURE_COMP = r48_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR48_EXPOSURE_AFTER_VOL_ADJ() {
		return R48_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR48_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r48_EXPOSURE_AFTER_VOL_ADJ) {
		R48_EXPOSURE_AFTER_VOL_ADJ = r48_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR48_COLLATERAL_CASH() {
		return R48_COLLATERAL_CASH;
	}
	public void setR48_COLLATERAL_CASH(BigDecimal r48_COLLATERAL_CASH) {
		R48_COLLATERAL_CASH = r48_COLLATERAL_CASH;
	}
	public BigDecimal getR48_COLLATERAL_TBILLS() {
		return R48_COLLATERAL_TBILLS;
	}
	public void setR48_COLLATERAL_TBILLS(BigDecimal r48_COLLATERAL_TBILLS) {
		R48_COLLATERAL_TBILLS = r48_COLLATERAL_TBILLS;
	}
	public BigDecimal getR48_COLLATERAL_DEBT_SEC() {
		return R48_COLLATERAL_DEBT_SEC;
	}
	public void setR48_COLLATERAL_DEBT_SEC(BigDecimal r48_COLLATERAL_DEBT_SEC) {
		R48_COLLATERAL_DEBT_SEC = r48_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR48_COLLATERAL_EQUITIES() {
		return R48_COLLATERAL_EQUITIES;
	}
	public void setR48_COLLATERAL_EQUITIES(BigDecimal r48_COLLATERAL_EQUITIES) {
		R48_COLLATERAL_EQUITIES = r48_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR48_COLLATERAL_MUTUAL_FUNDS() {
		return R48_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR48_COLLATERAL_MUTUAL_FUNDS(BigDecimal r48_COLLATERAL_MUTUAL_FUNDS) {
		R48_COLLATERAL_MUTUAL_FUNDS = r48_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR48_TOTAL_COLLATERAL_HAIRCUT() {
		return R48_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR48_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r48_TOTAL_COLLATERAL_HAIRCUT) {
		R48_TOTAL_COLLATERAL_HAIRCUT = r48_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR48_EXPOSURE_AFTER_CRM() {
		return R48_EXPOSURE_AFTER_CRM;
	}
	public void setR48_EXPOSURE_AFTER_CRM(BigDecimal r48_EXPOSURE_AFTER_CRM) {
		R48_EXPOSURE_AFTER_CRM = r48_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR48_RWA_NOT_COVERED_CRM() {
		return R48_RWA_NOT_COVERED_CRM;
	}
	public void setR48_RWA_NOT_COVERED_CRM(BigDecimal r48_RWA_NOT_COVERED_CRM) {
		R48_RWA_NOT_COVERED_CRM = r48_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR48_RWA_UNSECURED_EXPOSURE() {
		return R48_RWA_UNSECURED_EXPOSURE;
	}
	public void setR48_RWA_UNSECURED_EXPOSURE(BigDecimal r48_RWA_UNSECURED_EXPOSURE) {
		R48_RWA_UNSECURED_EXPOSURE = r48_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR48_RWA_UNSECURED() {
		return R48_RWA_UNSECURED;
	}
	public void setR48_RWA_UNSECURED(BigDecimal r48_RWA_UNSECURED) {
		R48_RWA_UNSECURED = r48_RWA_UNSECURED;
	}
	public BigDecimal getR48_TOTAL_RWA() {
		return R48_TOTAL_RWA;
	}
	public void setR48_TOTAL_RWA(BigDecimal r48_TOTAL_RWA) {
		R48_TOTAL_RWA = r48_TOTAL_RWA;
	}
	public BigDecimal getR49_EXPOSURE_BEFORE_CRM() {
		return R49_EXPOSURE_BEFORE_CRM;
	}
	public void setR49_EXPOSURE_BEFORE_CRM(BigDecimal r49_EXPOSURE_BEFORE_CRM) {
		R49_EXPOSURE_BEFORE_CRM = r49_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR49_SPEC_PROVISION_PAST_DUE() {
		return R49_SPEC_PROVISION_PAST_DUE;
	}
	public void setR49_SPEC_PROVISION_PAST_DUE(BigDecimal r49_SPEC_PROVISION_PAST_DUE) {
		R49_SPEC_PROVISION_PAST_DUE = r49_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR49_ON_BAL_SHEET_NETTING_ELIG() {
		return R49_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR49_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r49_ON_BAL_SHEET_NETTING_ELIG) {
		R49_ON_BAL_SHEET_NETTING_ELIG = r49_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR49_TOTAL_EXPOSURE_AFTER_NET() {
		return R49_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR49_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r49_TOTAL_EXPOSURE_AFTER_NET) {
		R49_TOTAL_EXPOSURE_AFTER_NET = r49_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR49_CRM_ELIG_EXPOSURE_SUBS() {
		return R49_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR49_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r49_CRM_ELIG_EXPOSURE_SUBS) {
		R49_CRM_ELIG_EXPOSURE_SUBS = r49_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR49_ELIG_GUARANTEES() {
		return R49_ELIG_GUARANTEES;
	}
	public void setR49_ELIG_GUARANTEES(BigDecimal r49_ELIG_GUARANTEES) {
		R49_ELIG_GUARANTEES = r49_ELIG_GUARANTEES;
	}
	public BigDecimal getR49_CREDIT_DERIVATIVES() {
		return R49_CREDIT_DERIVATIVES;
	}
	public void setR49_CREDIT_DERIVATIVES(BigDecimal r49_CREDIT_DERIVATIVES) {
		R49_CREDIT_DERIVATIVES = r49_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR49_CRM_COVERED_EXPOSURE() {
		return R49_CRM_COVERED_EXPOSURE;
	}
	public void setR49_CRM_COVERED_EXPOSURE(BigDecimal r49_CRM_COVERED_EXPOSURE) {
		R49_CRM_COVERED_EXPOSURE = r49_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR49_CRM_NOT_COVERED_EXPOSURE() {
		return R49_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR49_CRM_NOT_COVERED_EXPOSURE(BigDecimal r49_CRM_NOT_COVERED_EXPOSURE) {
		R49_CRM_NOT_COVERED_EXPOSURE = r49_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR49_CRM_RISK_WEIGHT() {
		return R49_CRM_RISK_WEIGHT;
	}
	public void setR49_CRM_RISK_WEIGHT(BigDecimal r49_CRM_RISK_WEIGHT) {
		R49_CRM_RISK_WEIGHT = r49_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR49_RWA_CRM_COVERED() {
		return R49_RWA_CRM_COVERED;
	}
	public void setR49_RWA_CRM_COVERED(BigDecimal r49_RWA_CRM_COVERED) {
		R49_RWA_CRM_COVERED = r49_RWA_CRM_COVERED;
	}
	public BigDecimal getR49_ORIG_COUNTERPARTY_RW() {
		return R49_ORIG_COUNTERPARTY_RW;
	}
	public void setR49_ORIG_COUNTERPARTY_RW(BigDecimal r49_ORIG_COUNTERPARTY_RW) {
		R49_ORIG_COUNTERPARTY_RW = r49_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR49_RWA_CRM_NOT_COVERED() {
		return R49_RWA_CRM_NOT_COVERED;
	}
	public void setR49_RWA_CRM_NOT_COVERED(BigDecimal r49_RWA_CRM_NOT_COVERED) {
		R49_RWA_CRM_NOT_COVERED = r49_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR49_CRM_ELIG_EXPOSURE_COMP() {
		return R49_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR49_CRM_ELIG_EXPOSURE_COMP(BigDecimal r49_CRM_ELIG_EXPOSURE_COMP) {
		R49_CRM_ELIG_EXPOSURE_COMP = r49_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR49_EXPOSURE_AFTER_VOL_ADJ() {
		return R49_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR49_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r49_EXPOSURE_AFTER_VOL_ADJ) {
		R49_EXPOSURE_AFTER_VOL_ADJ = r49_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR49_COLLATERAL_CASH() {
		return R49_COLLATERAL_CASH;
	}
	public void setR49_COLLATERAL_CASH(BigDecimal r49_COLLATERAL_CASH) {
		R49_COLLATERAL_CASH = r49_COLLATERAL_CASH;
	}
	public BigDecimal getR49_COLLATERAL_TBILLS() {
		return R49_COLLATERAL_TBILLS;
	}
	public void setR49_COLLATERAL_TBILLS(BigDecimal r49_COLLATERAL_TBILLS) {
		R49_COLLATERAL_TBILLS = r49_COLLATERAL_TBILLS;
	}
	public BigDecimal getR49_COLLATERAL_DEBT_SEC() {
		return R49_COLLATERAL_DEBT_SEC;
	}
	public void setR49_COLLATERAL_DEBT_SEC(BigDecimal r49_COLLATERAL_DEBT_SEC) {
		R49_COLLATERAL_DEBT_SEC = r49_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR49_COLLATERAL_EQUITIES() {
		return R49_COLLATERAL_EQUITIES;
	}
	public void setR49_COLLATERAL_EQUITIES(BigDecimal r49_COLLATERAL_EQUITIES) {
		R49_COLLATERAL_EQUITIES = r49_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR49_COLLATERAL_MUTUAL_FUNDS() {
		return R49_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR49_COLLATERAL_MUTUAL_FUNDS(BigDecimal r49_COLLATERAL_MUTUAL_FUNDS) {
		R49_COLLATERAL_MUTUAL_FUNDS = r49_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR49_TOTAL_COLLATERAL_HAIRCUT() {
		return R49_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR49_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r49_TOTAL_COLLATERAL_HAIRCUT) {
		R49_TOTAL_COLLATERAL_HAIRCUT = r49_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR49_EXPOSURE_AFTER_CRM() {
		return R49_EXPOSURE_AFTER_CRM;
	}
	public void setR49_EXPOSURE_AFTER_CRM(BigDecimal r49_EXPOSURE_AFTER_CRM) {
		R49_EXPOSURE_AFTER_CRM = r49_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR49_RWA_NOT_COVERED_CRM() {
		return R49_RWA_NOT_COVERED_CRM;
	}
	public void setR49_RWA_NOT_COVERED_CRM(BigDecimal r49_RWA_NOT_COVERED_CRM) {
		R49_RWA_NOT_COVERED_CRM = r49_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR49_RWA_UNSECURED_EXPOSURE() {
		return R49_RWA_UNSECURED_EXPOSURE;
	}
	public void setR49_RWA_UNSECURED_EXPOSURE(BigDecimal r49_RWA_UNSECURED_EXPOSURE) {
		R49_RWA_UNSECURED_EXPOSURE = r49_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR49_RWA_UNSECURED() {
		return R49_RWA_UNSECURED;
	}
	public void setR49_RWA_UNSECURED(BigDecimal r49_RWA_UNSECURED) {
		R49_RWA_UNSECURED = r49_RWA_UNSECURED;
	}
	public BigDecimal getR49_TOTAL_RWA() {
		return R49_TOTAL_RWA;
	}
	public void setR49_TOTAL_RWA(BigDecimal r49_TOTAL_RWA) {
		R49_TOTAL_RWA = r49_TOTAL_RWA;
	}
	public BigDecimal getR50_EXPOSURE_BEFORE_CRM() {
		return R50_EXPOSURE_BEFORE_CRM;
	}
	public void setR50_EXPOSURE_BEFORE_CRM(BigDecimal r50_EXPOSURE_BEFORE_CRM) {
		R50_EXPOSURE_BEFORE_CRM = r50_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR50_SPEC_PROVISION_PAST_DUE() {
		return R50_SPEC_PROVISION_PAST_DUE;
	}
	public void setR50_SPEC_PROVISION_PAST_DUE(BigDecimal r50_SPEC_PROVISION_PAST_DUE) {
		R50_SPEC_PROVISION_PAST_DUE = r50_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR50_ON_BAL_SHEET_NETTING_ELIG() {
		return R50_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR50_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r50_ON_BAL_SHEET_NETTING_ELIG) {
		R50_ON_BAL_SHEET_NETTING_ELIG = r50_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR50_TOTAL_EXPOSURE_AFTER_NET() {
		return R50_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR50_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r50_TOTAL_EXPOSURE_AFTER_NET) {
		R50_TOTAL_EXPOSURE_AFTER_NET = r50_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR50_CRM_ELIG_EXPOSURE_SUBS() {
		return R50_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR50_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r50_CRM_ELIG_EXPOSURE_SUBS) {
		R50_CRM_ELIG_EXPOSURE_SUBS = r50_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR50_ELIG_GUARANTEES() {
		return R50_ELIG_GUARANTEES;
	}
	public void setR50_ELIG_GUARANTEES(BigDecimal r50_ELIG_GUARANTEES) {
		R50_ELIG_GUARANTEES = r50_ELIG_GUARANTEES;
	}
	public BigDecimal getR50_CREDIT_DERIVATIVES() {
		return R50_CREDIT_DERIVATIVES;
	}
	public void setR50_CREDIT_DERIVATIVES(BigDecimal r50_CREDIT_DERIVATIVES) {
		R50_CREDIT_DERIVATIVES = r50_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR50_CRM_COVERED_EXPOSURE() {
		return R50_CRM_COVERED_EXPOSURE;
	}
	public void setR50_CRM_COVERED_EXPOSURE(BigDecimal r50_CRM_COVERED_EXPOSURE) {
		R50_CRM_COVERED_EXPOSURE = r50_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR50_CRM_NOT_COVERED_EXPOSURE() {
		return R50_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR50_CRM_NOT_COVERED_EXPOSURE(BigDecimal r50_CRM_NOT_COVERED_EXPOSURE) {
		R50_CRM_NOT_COVERED_EXPOSURE = r50_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR50_CRM_RISK_WEIGHT() {
		return R50_CRM_RISK_WEIGHT;
	}
	public void setR50_CRM_RISK_WEIGHT(BigDecimal r50_CRM_RISK_WEIGHT) {
		R50_CRM_RISK_WEIGHT = r50_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR50_RWA_CRM_COVERED() {
		return R50_RWA_CRM_COVERED;
	}
	public void setR50_RWA_CRM_COVERED(BigDecimal r50_RWA_CRM_COVERED) {
		R50_RWA_CRM_COVERED = r50_RWA_CRM_COVERED;
	}
	public BigDecimal getR50_ORIG_COUNTERPARTY_RW() {
		return R50_ORIG_COUNTERPARTY_RW;
	}
	public void setR50_ORIG_COUNTERPARTY_RW(BigDecimal r50_ORIG_COUNTERPARTY_RW) {
		R50_ORIG_COUNTERPARTY_RW = r50_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR50_RWA_CRM_NOT_COVERED() {
		return R50_RWA_CRM_NOT_COVERED;
	}
	public void setR50_RWA_CRM_NOT_COVERED(BigDecimal r50_RWA_CRM_NOT_COVERED) {
		R50_RWA_CRM_NOT_COVERED = r50_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR50_CRM_ELIG_EXPOSURE_COMP() {
		return R50_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR50_CRM_ELIG_EXPOSURE_COMP(BigDecimal r50_CRM_ELIG_EXPOSURE_COMP) {
		R50_CRM_ELIG_EXPOSURE_COMP = r50_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR50_EXPOSURE_AFTER_VOL_ADJ() {
		return R50_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR50_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r50_EXPOSURE_AFTER_VOL_ADJ) {
		R50_EXPOSURE_AFTER_VOL_ADJ = r50_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR50_COLLATERAL_CASH() {
		return R50_COLLATERAL_CASH;
	}
	public void setR50_COLLATERAL_CASH(BigDecimal r50_COLLATERAL_CASH) {
		R50_COLLATERAL_CASH = r50_COLLATERAL_CASH;
	}
	public BigDecimal getR50_COLLATERAL_TBILLS() {
		return R50_COLLATERAL_TBILLS;
	}
	public void setR50_COLLATERAL_TBILLS(BigDecimal r50_COLLATERAL_TBILLS) {
		R50_COLLATERAL_TBILLS = r50_COLLATERAL_TBILLS;
	}
	public BigDecimal getR50_COLLATERAL_DEBT_SEC() {
		return R50_COLLATERAL_DEBT_SEC;
	}
	public void setR50_COLLATERAL_DEBT_SEC(BigDecimal r50_COLLATERAL_DEBT_SEC) {
		R50_COLLATERAL_DEBT_SEC = r50_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR50_COLLATERAL_EQUITIES() {
		return R50_COLLATERAL_EQUITIES;
	}
	public void setR50_COLLATERAL_EQUITIES(BigDecimal r50_COLLATERAL_EQUITIES) {
		R50_COLLATERAL_EQUITIES = r50_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR50_COLLATERAL_MUTUAL_FUNDS() {
		return R50_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR50_COLLATERAL_MUTUAL_FUNDS(BigDecimal r50_COLLATERAL_MUTUAL_FUNDS) {
		R50_COLLATERAL_MUTUAL_FUNDS = r50_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR50_TOTAL_COLLATERAL_HAIRCUT() {
		return R50_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR50_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r50_TOTAL_COLLATERAL_HAIRCUT) {
		R50_TOTAL_COLLATERAL_HAIRCUT = r50_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR50_EXPOSURE_AFTER_CRM() {
		return R50_EXPOSURE_AFTER_CRM;
	}
	public void setR50_EXPOSURE_AFTER_CRM(BigDecimal r50_EXPOSURE_AFTER_CRM) {
		R50_EXPOSURE_AFTER_CRM = r50_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR50_RWA_NOT_COVERED_CRM() {
		return R50_RWA_NOT_COVERED_CRM;
	}
	public void setR50_RWA_NOT_COVERED_CRM(BigDecimal r50_RWA_NOT_COVERED_CRM) {
		R50_RWA_NOT_COVERED_CRM = r50_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR50_RWA_UNSECURED_EXPOSURE() {
		return R50_RWA_UNSECURED_EXPOSURE;
	}
	public void setR50_RWA_UNSECURED_EXPOSURE(BigDecimal r50_RWA_UNSECURED_EXPOSURE) {
		R50_RWA_UNSECURED_EXPOSURE = r50_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR50_RWA_UNSECURED() {
		return R50_RWA_UNSECURED;
	}
	public void setR50_RWA_UNSECURED(BigDecimal r50_RWA_UNSECURED) {
		R50_RWA_UNSECURED = r50_RWA_UNSECURED;
	}
	public BigDecimal getR50_TOTAL_RWA() {
		return R50_TOTAL_RWA;
	}
	public void setR50_TOTAL_RWA(BigDecimal r50_TOTAL_RWA) {
		R50_TOTAL_RWA = r50_TOTAL_RWA;
	}
	public BigDecimal getR51_EXPOSURE_BEFORE_CRM() {
		return R51_EXPOSURE_BEFORE_CRM;
	}
	public void setR51_EXPOSURE_BEFORE_CRM(BigDecimal r51_EXPOSURE_BEFORE_CRM) {
		R51_EXPOSURE_BEFORE_CRM = r51_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR51_SPEC_PROVISION_PAST_DUE() {
		return R51_SPEC_PROVISION_PAST_DUE;
	}
	public void setR51_SPEC_PROVISION_PAST_DUE(BigDecimal r51_SPEC_PROVISION_PAST_DUE) {
		R51_SPEC_PROVISION_PAST_DUE = r51_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR51_ON_BAL_SHEET_NETTING_ELIG() {
		return R51_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR51_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r51_ON_BAL_SHEET_NETTING_ELIG) {
		R51_ON_BAL_SHEET_NETTING_ELIG = r51_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR51_TOTAL_EXPOSURE_AFTER_NET() {
		return R51_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR51_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r51_TOTAL_EXPOSURE_AFTER_NET) {
		R51_TOTAL_EXPOSURE_AFTER_NET = r51_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR51_CRM_ELIG_EXPOSURE_SUBS() {
		return R51_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR51_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r51_CRM_ELIG_EXPOSURE_SUBS) {
		R51_CRM_ELIG_EXPOSURE_SUBS = r51_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR51_ELIG_GUARANTEES() {
		return R51_ELIG_GUARANTEES;
	}
	public void setR51_ELIG_GUARANTEES(BigDecimal r51_ELIG_GUARANTEES) {
		R51_ELIG_GUARANTEES = r51_ELIG_GUARANTEES;
	}
	public BigDecimal getR51_CREDIT_DERIVATIVES() {
		return R51_CREDIT_DERIVATIVES;
	}
	public void setR51_CREDIT_DERIVATIVES(BigDecimal r51_CREDIT_DERIVATIVES) {
		R51_CREDIT_DERIVATIVES = r51_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR51_CRM_COVERED_EXPOSURE() {
		return R51_CRM_COVERED_EXPOSURE;
	}
	public void setR51_CRM_COVERED_EXPOSURE(BigDecimal r51_CRM_COVERED_EXPOSURE) {
		R51_CRM_COVERED_EXPOSURE = r51_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR51_CRM_NOT_COVERED_EXPOSURE() {
		return R51_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR51_CRM_NOT_COVERED_EXPOSURE(BigDecimal r51_CRM_NOT_COVERED_EXPOSURE) {
		R51_CRM_NOT_COVERED_EXPOSURE = r51_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR51_CRM_RISK_WEIGHT() {
		return R51_CRM_RISK_WEIGHT;
	}
	public void setR51_CRM_RISK_WEIGHT(BigDecimal r51_CRM_RISK_WEIGHT) {
		R51_CRM_RISK_WEIGHT = r51_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR51_RWA_CRM_COVERED() {
		return R51_RWA_CRM_COVERED;
	}
	public void setR51_RWA_CRM_COVERED(BigDecimal r51_RWA_CRM_COVERED) {
		R51_RWA_CRM_COVERED = r51_RWA_CRM_COVERED;
	}
	public BigDecimal getR51_ORIG_COUNTERPARTY_RW() {
		return R51_ORIG_COUNTERPARTY_RW;
	}
	public void setR51_ORIG_COUNTERPARTY_RW(BigDecimal r51_ORIG_COUNTERPARTY_RW) {
		R51_ORIG_COUNTERPARTY_RW = r51_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR51_RWA_CRM_NOT_COVERED() {
		return R51_RWA_CRM_NOT_COVERED;
	}
	public void setR51_RWA_CRM_NOT_COVERED(BigDecimal r51_RWA_CRM_NOT_COVERED) {
		R51_RWA_CRM_NOT_COVERED = r51_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR51_CRM_ELIG_EXPOSURE_COMP() {
		return R51_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR51_CRM_ELIG_EXPOSURE_COMP(BigDecimal r51_CRM_ELIG_EXPOSURE_COMP) {
		R51_CRM_ELIG_EXPOSURE_COMP = r51_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR51_EXPOSURE_AFTER_VOL_ADJ() {
		return R51_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR51_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r51_EXPOSURE_AFTER_VOL_ADJ) {
		R51_EXPOSURE_AFTER_VOL_ADJ = r51_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR51_COLLATERAL_CASH() {
		return R51_COLLATERAL_CASH;
	}
	public void setR51_COLLATERAL_CASH(BigDecimal r51_COLLATERAL_CASH) {
		R51_COLLATERAL_CASH = r51_COLLATERAL_CASH;
	}
	public BigDecimal getR51_COLLATERAL_TBILLS() {
		return R51_COLLATERAL_TBILLS;
	}
	public void setR51_COLLATERAL_TBILLS(BigDecimal r51_COLLATERAL_TBILLS) {
		R51_COLLATERAL_TBILLS = r51_COLLATERAL_TBILLS;
	}
	public BigDecimal getR51_COLLATERAL_DEBT_SEC() {
		return R51_COLLATERAL_DEBT_SEC;
	}
	public void setR51_COLLATERAL_DEBT_SEC(BigDecimal r51_COLLATERAL_DEBT_SEC) {
		R51_COLLATERAL_DEBT_SEC = r51_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR51_COLLATERAL_EQUITIES() {
		return R51_COLLATERAL_EQUITIES;
	}
	public void setR51_COLLATERAL_EQUITIES(BigDecimal r51_COLLATERAL_EQUITIES) {
		R51_COLLATERAL_EQUITIES = r51_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR51_COLLATERAL_MUTUAL_FUNDS() {
		return R51_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR51_COLLATERAL_MUTUAL_FUNDS(BigDecimal r51_COLLATERAL_MUTUAL_FUNDS) {
		R51_COLLATERAL_MUTUAL_FUNDS = r51_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR51_TOTAL_COLLATERAL_HAIRCUT() {
		return R51_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR51_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r51_TOTAL_COLLATERAL_HAIRCUT) {
		R51_TOTAL_COLLATERAL_HAIRCUT = r51_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR51_EXPOSURE_AFTER_CRM() {
		return R51_EXPOSURE_AFTER_CRM;
	}
	public void setR51_EXPOSURE_AFTER_CRM(BigDecimal r51_EXPOSURE_AFTER_CRM) {
		R51_EXPOSURE_AFTER_CRM = r51_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR51_RWA_NOT_COVERED_CRM() {
		return R51_RWA_NOT_COVERED_CRM;
	}
	public void setR51_RWA_NOT_COVERED_CRM(BigDecimal r51_RWA_NOT_COVERED_CRM) {
		R51_RWA_NOT_COVERED_CRM = r51_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR51_RWA_UNSECURED_EXPOSURE() {
		return R51_RWA_UNSECURED_EXPOSURE;
	}
	public void setR51_RWA_UNSECURED_EXPOSURE(BigDecimal r51_RWA_UNSECURED_EXPOSURE) {
		R51_RWA_UNSECURED_EXPOSURE = r51_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR51_RWA_UNSECURED() {
		return R51_RWA_UNSECURED;
	}
	public void setR51_RWA_UNSECURED(BigDecimal r51_RWA_UNSECURED) {
		R51_RWA_UNSECURED = r51_RWA_UNSECURED;
	}
	public BigDecimal getR51_TOTAL_RWA() {
		return R51_TOTAL_RWA;
	}
	public void setR51_TOTAL_RWA(BigDecimal r51_TOTAL_RWA) {
		R51_TOTAL_RWA = r51_TOTAL_RWA;
	}
	public BigDecimal getR52_EXPOSURE_BEFORE_CRM() {
		return R52_EXPOSURE_BEFORE_CRM;
	}
	public void setR52_EXPOSURE_BEFORE_CRM(BigDecimal r52_EXPOSURE_BEFORE_CRM) {
		R52_EXPOSURE_BEFORE_CRM = r52_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR52_SPEC_PROVISION_PAST_DUE() {
		return R52_SPEC_PROVISION_PAST_DUE;
	}
	public void setR52_SPEC_PROVISION_PAST_DUE(BigDecimal r52_SPEC_PROVISION_PAST_DUE) {
		R52_SPEC_PROVISION_PAST_DUE = r52_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR52_ON_BAL_SHEET_NETTING_ELIG() {
		return R52_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR52_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r52_ON_BAL_SHEET_NETTING_ELIG) {
		R52_ON_BAL_SHEET_NETTING_ELIG = r52_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR52_TOTAL_EXPOSURE_AFTER_NET() {
		return R52_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR52_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r52_TOTAL_EXPOSURE_AFTER_NET) {
		R52_TOTAL_EXPOSURE_AFTER_NET = r52_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR52_CRM_ELIG_EXPOSURE_SUBS() {
		return R52_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR52_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r52_CRM_ELIG_EXPOSURE_SUBS) {
		R52_CRM_ELIG_EXPOSURE_SUBS = r52_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR52_ELIG_GUARANTEES() {
		return R52_ELIG_GUARANTEES;
	}
	public void setR52_ELIG_GUARANTEES(BigDecimal r52_ELIG_GUARANTEES) {
		R52_ELIG_GUARANTEES = r52_ELIG_GUARANTEES;
	}
	public BigDecimal getR52_CREDIT_DERIVATIVES() {
		return R52_CREDIT_DERIVATIVES;
	}
	public void setR52_CREDIT_DERIVATIVES(BigDecimal r52_CREDIT_DERIVATIVES) {
		R52_CREDIT_DERIVATIVES = r52_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR52_CRM_COVERED_EXPOSURE() {
		return R52_CRM_COVERED_EXPOSURE;
	}
	public void setR52_CRM_COVERED_EXPOSURE(BigDecimal r52_CRM_COVERED_EXPOSURE) {
		R52_CRM_COVERED_EXPOSURE = r52_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR52_CRM_NOT_COVERED_EXPOSURE() {
		return R52_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR52_CRM_NOT_COVERED_EXPOSURE(BigDecimal r52_CRM_NOT_COVERED_EXPOSURE) {
		R52_CRM_NOT_COVERED_EXPOSURE = r52_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR52_CRM_RISK_WEIGHT() {
		return R52_CRM_RISK_WEIGHT;
	}
	public void setR52_CRM_RISK_WEIGHT(BigDecimal r52_CRM_RISK_WEIGHT) {
		R52_CRM_RISK_WEIGHT = r52_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR52_RWA_CRM_COVERED() {
		return R52_RWA_CRM_COVERED;
	}
	public void setR52_RWA_CRM_COVERED(BigDecimal r52_RWA_CRM_COVERED) {
		R52_RWA_CRM_COVERED = r52_RWA_CRM_COVERED;
	}
	public BigDecimal getR52_ORIG_COUNTERPARTY_RW() {
		return R52_ORIG_COUNTERPARTY_RW;
	}
	public void setR52_ORIG_COUNTERPARTY_RW(BigDecimal r52_ORIG_COUNTERPARTY_RW) {
		R52_ORIG_COUNTERPARTY_RW = r52_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR52_RWA_CRM_NOT_COVERED() {
		return R52_RWA_CRM_NOT_COVERED;
	}
	public void setR52_RWA_CRM_NOT_COVERED(BigDecimal r52_RWA_CRM_NOT_COVERED) {
		R52_RWA_CRM_NOT_COVERED = r52_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR52_CRM_ELIG_EXPOSURE_COMP() {
		return R52_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR52_CRM_ELIG_EXPOSURE_COMP(BigDecimal r52_CRM_ELIG_EXPOSURE_COMP) {
		R52_CRM_ELIG_EXPOSURE_COMP = r52_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR52_EXPOSURE_AFTER_VOL_ADJ() {
		return R52_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR52_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r52_EXPOSURE_AFTER_VOL_ADJ) {
		R52_EXPOSURE_AFTER_VOL_ADJ = r52_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR52_COLLATERAL_CASH() {
		return R52_COLLATERAL_CASH;
	}
	public void setR52_COLLATERAL_CASH(BigDecimal r52_COLLATERAL_CASH) {
		R52_COLLATERAL_CASH = r52_COLLATERAL_CASH;
	}
	public BigDecimal getR52_COLLATERAL_TBILLS() {
		return R52_COLLATERAL_TBILLS;
	}
	public void setR52_COLLATERAL_TBILLS(BigDecimal r52_COLLATERAL_TBILLS) {
		R52_COLLATERAL_TBILLS = r52_COLLATERAL_TBILLS;
	}
	public BigDecimal getR52_COLLATERAL_DEBT_SEC() {
		return R52_COLLATERAL_DEBT_SEC;
	}
	public void setR52_COLLATERAL_DEBT_SEC(BigDecimal r52_COLLATERAL_DEBT_SEC) {
		R52_COLLATERAL_DEBT_SEC = r52_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR52_COLLATERAL_EQUITIES() {
		return R52_COLLATERAL_EQUITIES;
	}
	public void setR52_COLLATERAL_EQUITIES(BigDecimal r52_COLLATERAL_EQUITIES) {
		R52_COLLATERAL_EQUITIES = r52_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR52_COLLATERAL_MUTUAL_FUNDS() {
		return R52_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR52_COLLATERAL_MUTUAL_FUNDS(BigDecimal r52_COLLATERAL_MUTUAL_FUNDS) {
		R52_COLLATERAL_MUTUAL_FUNDS = r52_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR52_TOTAL_COLLATERAL_HAIRCUT() {
		return R52_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR52_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r52_TOTAL_COLLATERAL_HAIRCUT) {
		R52_TOTAL_COLLATERAL_HAIRCUT = r52_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR52_EXPOSURE_AFTER_CRM() {
		return R52_EXPOSURE_AFTER_CRM;
	}
	public void setR52_EXPOSURE_AFTER_CRM(BigDecimal r52_EXPOSURE_AFTER_CRM) {
		R52_EXPOSURE_AFTER_CRM = r52_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR52_RWA_NOT_COVERED_CRM() {
		return R52_RWA_NOT_COVERED_CRM;
	}
	public void setR52_RWA_NOT_COVERED_CRM(BigDecimal r52_RWA_NOT_COVERED_CRM) {
		R52_RWA_NOT_COVERED_CRM = r52_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR52_RWA_UNSECURED_EXPOSURE() {
		return R52_RWA_UNSECURED_EXPOSURE;
	}
	public void setR52_RWA_UNSECURED_EXPOSURE(BigDecimal r52_RWA_UNSECURED_EXPOSURE) {
		R52_RWA_UNSECURED_EXPOSURE = r52_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR52_RWA_UNSECURED() {
		return R52_RWA_UNSECURED;
	}
	public void setR52_RWA_UNSECURED(BigDecimal r52_RWA_UNSECURED) {
		R52_RWA_UNSECURED = r52_RWA_UNSECURED;
	}
	public BigDecimal getR52_TOTAL_RWA() {
		return R52_TOTAL_RWA;
	}
	public void setR52_TOTAL_RWA(BigDecimal r52_TOTAL_RWA) {
		R52_TOTAL_RWA = r52_TOTAL_RWA;
	}
	public BigDecimal getR53_EXPOSURE_BEFORE_CRM() {
		return R53_EXPOSURE_BEFORE_CRM;
	}
	public void setR53_EXPOSURE_BEFORE_CRM(BigDecimal r53_EXPOSURE_BEFORE_CRM) {
		R53_EXPOSURE_BEFORE_CRM = r53_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR53_SPEC_PROVISION_PAST_DUE() {
		return R53_SPEC_PROVISION_PAST_DUE;
	}
	public void setR53_SPEC_PROVISION_PAST_DUE(BigDecimal r53_SPEC_PROVISION_PAST_DUE) {
		R53_SPEC_PROVISION_PAST_DUE = r53_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR53_ON_BAL_SHEET_NETTING_ELIG() {
		return R53_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR53_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r53_ON_BAL_SHEET_NETTING_ELIG) {
		R53_ON_BAL_SHEET_NETTING_ELIG = r53_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR53_TOTAL_EXPOSURE_AFTER_NET() {
		return R53_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR53_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r53_TOTAL_EXPOSURE_AFTER_NET) {
		R53_TOTAL_EXPOSURE_AFTER_NET = r53_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR53_CRM_ELIG_EXPOSURE_SUBS() {
		return R53_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR53_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r53_CRM_ELIG_EXPOSURE_SUBS) {
		R53_CRM_ELIG_EXPOSURE_SUBS = r53_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR53_ELIG_GUARANTEES() {
		return R53_ELIG_GUARANTEES;
	}
	public void setR53_ELIG_GUARANTEES(BigDecimal r53_ELIG_GUARANTEES) {
		R53_ELIG_GUARANTEES = r53_ELIG_GUARANTEES;
	}
	public BigDecimal getR53_CREDIT_DERIVATIVES() {
		return R53_CREDIT_DERIVATIVES;
	}
	public void setR53_CREDIT_DERIVATIVES(BigDecimal r53_CREDIT_DERIVATIVES) {
		R53_CREDIT_DERIVATIVES = r53_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR53_CRM_COVERED_EXPOSURE() {
		return R53_CRM_COVERED_EXPOSURE;
	}
	public void setR53_CRM_COVERED_EXPOSURE(BigDecimal r53_CRM_COVERED_EXPOSURE) {
		R53_CRM_COVERED_EXPOSURE = r53_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR53_CRM_NOT_COVERED_EXPOSURE() {
		return R53_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR53_CRM_NOT_COVERED_EXPOSURE(BigDecimal r53_CRM_NOT_COVERED_EXPOSURE) {
		R53_CRM_NOT_COVERED_EXPOSURE = r53_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR53_CRM_RISK_WEIGHT() {
		return R53_CRM_RISK_WEIGHT;
	}
	public void setR53_CRM_RISK_WEIGHT(BigDecimal r53_CRM_RISK_WEIGHT) {
		R53_CRM_RISK_WEIGHT = r53_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR53_RWA_CRM_COVERED() {
		return R53_RWA_CRM_COVERED;
	}
	public void setR53_RWA_CRM_COVERED(BigDecimal r53_RWA_CRM_COVERED) {
		R53_RWA_CRM_COVERED = r53_RWA_CRM_COVERED;
	}
	public BigDecimal getR53_ORIG_COUNTERPARTY_RW() {
		return R53_ORIG_COUNTERPARTY_RW;
	}
	public void setR53_ORIG_COUNTERPARTY_RW(BigDecimal r53_ORIG_COUNTERPARTY_RW) {
		R53_ORIG_COUNTERPARTY_RW = r53_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR53_RWA_CRM_NOT_COVERED() {
		return R53_RWA_CRM_NOT_COVERED;
	}
	public void setR53_RWA_CRM_NOT_COVERED(BigDecimal r53_RWA_CRM_NOT_COVERED) {
		R53_RWA_CRM_NOT_COVERED = r53_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR53_CRM_ELIG_EXPOSURE_COMP() {
		return R53_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR53_CRM_ELIG_EXPOSURE_COMP(BigDecimal r53_CRM_ELIG_EXPOSURE_COMP) {
		R53_CRM_ELIG_EXPOSURE_COMP = r53_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR53_EXPOSURE_AFTER_VOL_ADJ() {
		return R53_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR53_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r53_EXPOSURE_AFTER_VOL_ADJ) {
		R53_EXPOSURE_AFTER_VOL_ADJ = r53_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR53_COLLATERAL_CASH() {
		return R53_COLLATERAL_CASH;
	}
	public void setR53_COLLATERAL_CASH(BigDecimal r53_COLLATERAL_CASH) {
		R53_COLLATERAL_CASH = r53_COLLATERAL_CASH;
	}
	public BigDecimal getR53_COLLATERAL_TBILLS() {
		return R53_COLLATERAL_TBILLS;
	}
	public void setR53_COLLATERAL_TBILLS(BigDecimal r53_COLLATERAL_TBILLS) {
		R53_COLLATERAL_TBILLS = r53_COLLATERAL_TBILLS;
	}
	public BigDecimal getR53_COLLATERAL_DEBT_SEC() {
		return R53_COLLATERAL_DEBT_SEC;
	}
	public void setR53_COLLATERAL_DEBT_SEC(BigDecimal r53_COLLATERAL_DEBT_SEC) {
		R53_COLLATERAL_DEBT_SEC = r53_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR53_COLLATERAL_EQUITIES() {
		return R53_COLLATERAL_EQUITIES;
	}
	public void setR53_COLLATERAL_EQUITIES(BigDecimal r53_COLLATERAL_EQUITIES) {
		R53_COLLATERAL_EQUITIES = r53_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR53_COLLATERAL_MUTUAL_FUNDS() {
		return R53_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR53_COLLATERAL_MUTUAL_FUNDS(BigDecimal r53_COLLATERAL_MUTUAL_FUNDS) {
		R53_COLLATERAL_MUTUAL_FUNDS = r53_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR53_TOTAL_COLLATERAL_HAIRCUT() {
		return R53_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR53_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r53_TOTAL_COLLATERAL_HAIRCUT) {
		R53_TOTAL_COLLATERAL_HAIRCUT = r53_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR53_EXPOSURE_AFTER_CRM() {
		return R53_EXPOSURE_AFTER_CRM;
	}
	public void setR53_EXPOSURE_AFTER_CRM(BigDecimal r53_EXPOSURE_AFTER_CRM) {
		R53_EXPOSURE_AFTER_CRM = r53_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR53_RWA_NOT_COVERED_CRM() {
		return R53_RWA_NOT_COVERED_CRM;
	}
	public void setR53_RWA_NOT_COVERED_CRM(BigDecimal r53_RWA_NOT_COVERED_CRM) {
		R53_RWA_NOT_COVERED_CRM = r53_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR53_RWA_UNSECURED_EXPOSURE() {
		return R53_RWA_UNSECURED_EXPOSURE;
	}
	public void setR53_RWA_UNSECURED_EXPOSURE(BigDecimal r53_RWA_UNSECURED_EXPOSURE) {
		R53_RWA_UNSECURED_EXPOSURE = r53_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR53_RWA_UNSECURED() {
		return R53_RWA_UNSECURED;
	}
	public void setR53_RWA_UNSECURED(BigDecimal r53_RWA_UNSECURED) {
		R53_RWA_UNSECURED = r53_RWA_UNSECURED;
	}
	public BigDecimal getR53_TOTAL_RWA() {
		return R53_TOTAL_RWA;
	}
	public void setR53_TOTAL_RWA(BigDecimal r53_TOTAL_RWA) {
		R53_TOTAL_RWA = r53_TOTAL_RWA;
	}
	public BigDecimal getR54_EXPOSURE_BEFORE_CRM() {
		return R54_EXPOSURE_BEFORE_CRM;
	}
	public void setR54_EXPOSURE_BEFORE_CRM(BigDecimal r54_EXPOSURE_BEFORE_CRM) {
		R54_EXPOSURE_BEFORE_CRM = r54_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR54_SPEC_PROVISION_PAST_DUE() {
		return R54_SPEC_PROVISION_PAST_DUE;
	}
	public void setR54_SPEC_PROVISION_PAST_DUE(BigDecimal r54_SPEC_PROVISION_PAST_DUE) {
		R54_SPEC_PROVISION_PAST_DUE = r54_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR54_ON_BAL_SHEET_NETTING_ELIG() {
		return R54_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR54_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r54_ON_BAL_SHEET_NETTING_ELIG) {
		R54_ON_BAL_SHEET_NETTING_ELIG = r54_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR54_TOTAL_EXPOSURE_AFTER_NET() {
		return R54_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR54_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r54_TOTAL_EXPOSURE_AFTER_NET) {
		R54_TOTAL_EXPOSURE_AFTER_NET = r54_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR54_CRM_ELIG_EXPOSURE_SUBS() {
		return R54_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR54_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r54_CRM_ELIG_EXPOSURE_SUBS) {
		R54_CRM_ELIG_EXPOSURE_SUBS = r54_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR54_ELIG_GUARANTEES() {
		return R54_ELIG_GUARANTEES;
	}
	public void setR54_ELIG_GUARANTEES(BigDecimal r54_ELIG_GUARANTEES) {
		R54_ELIG_GUARANTEES = r54_ELIG_GUARANTEES;
	}
	public BigDecimal getR54_CREDIT_DERIVATIVES() {
		return R54_CREDIT_DERIVATIVES;
	}
	public void setR54_CREDIT_DERIVATIVES(BigDecimal r54_CREDIT_DERIVATIVES) {
		R54_CREDIT_DERIVATIVES = r54_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR54_CRM_COVERED_EXPOSURE() {
		return R54_CRM_COVERED_EXPOSURE;
	}
	public void setR54_CRM_COVERED_EXPOSURE(BigDecimal r54_CRM_COVERED_EXPOSURE) {
		R54_CRM_COVERED_EXPOSURE = r54_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR54_CRM_NOT_COVERED_EXPOSURE() {
		return R54_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR54_CRM_NOT_COVERED_EXPOSURE(BigDecimal r54_CRM_NOT_COVERED_EXPOSURE) {
		R54_CRM_NOT_COVERED_EXPOSURE = r54_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR54_CRM_RISK_WEIGHT() {
		return R54_CRM_RISK_WEIGHT;
	}
	public void setR54_CRM_RISK_WEIGHT(BigDecimal r54_CRM_RISK_WEIGHT) {
		R54_CRM_RISK_WEIGHT = r54_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR54_RWA_CRM_COVERED() {
		return R54_RWA_CRM_COVERED;
	}
	public void setR54_RWA_CRM_COVERED(BigDecimal r54_RWA_CRM_COVERED) {
		R54_RWA_CRM_COVERED = r54_RWA_CRM_COVERED;
	}
	public BigDecimal getR54_ORIG_COUNTERPARTY_RW() {
		return R54_ORIG_COUNTERPARTY_RW;
	}
	public void setR54_ORIG_COUNTERPARTY_RW(BigDecimal r54_ORIG_COUNTERPARTY_RW) {
		R54_ORIG_COUNTERPARTY_RW = r54_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR54_RWA_CRM_NOT_COVERED() {
		return R54_RWA_CRM_NOT_COVERED;
	}
	public void setR54_RWA_CRM_NOT_COVERED(BigDecimal r54_RWA_CRM_NOT_COVERED) {
		R54_RWA_CRM_NOT_COVERED = r54_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR54_CRM_ELIG_EXPOSURE_COMP() {
		return R54_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR54_CRM_ELIG_EXPOSURE_COMP(BigDecimal r54_CRM_ELIG_EXPOSURE_COMP) {
		R54_CRM_ELIG_EXPOSURE_COMP = r54_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR54_EXPOSURE_AFTER_VOL_ADJ() {
		return R54_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR54_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r54_EXPOSURE_AFTER_VOL_ADJ) {
		R54_EXPOSURE_AFTER_VOL_ADJ = r54_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR54_COLLATERAL_CASH() {
		return R54_COLLATERAL_CASH;
	}
	public void setR54_COLLATERAL_CASH(BigDecimal r54_COLLATERAL_CASH) {
		R54_COLLATERAL_CASH = r54_COLLATERAL_CASH;
	}
	public BigDecimal getR54_COLLATERAL_TBILLS() {
		return R54_COLLATERAL_TBILLS;
	}
	public void setR54_COLLATERAL_TBILLS(BigDecimal r54_COLLATERAL_TBILLS) {
		R54_COLLATERAL_TBILLS = r54_COLLATERAL_TBILLS;
	}
	public BigDecimal getR54_COLLATERAL_DEBT_SEC() {
		return R54_COLLATERAL_DEBT_SEC;
	}
	public void setR54_COLLATERAL_DEBT_SEC(BigDecimal r54_COLLATERAL_DEBT_SEC) {
		R54_COLLATERAL_DEBT_SEC = r54_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR54_COLLATERAL_EQUITIES() {
		return R54_COLLATERAL_EQUITIES;
	}
	public void setR54_COLLATERAL_EQUITIES(BigDecimal r54_COLLATERAL_EQUITIES) {
		R54_COLLATERAL_EQUITIES = r54_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR54_COLLATERAL_MUTUAL_FUNDS() {
		return R54_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR54_COLLATERAL_MUTUAL_FUNDS(BigDecimal r54_COLLATERAL_MUTUAL_FUNDS) {
		R54_COLLATERAL_MUTUAL_FUNDS = r54_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR54_TOTAL_COLLATERAL_HAIRCUT() {
		return R54_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR54_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r54_TOTAL_COLLATERAL_HAIRCUT) {
		R54_TOTAL_COLLATERAL_HAIRCUT = r54_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR54_EXPOSURE_AFTER_CRM() {
		return R54_EXPOSURE_AFTER_CRM;
	}
	public void setR54_EXPOSURE_AFTER_CRM(BigDecimal r54_EXPOSURE_AFTER_CRM) {
		R54_EXPOSURE_AFTER_CRM = r54_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR54_RWA_NOT_COVERED_CRM() {
		return R54_RWA_NOT_COVERED_CRM;
	}
	public void setR54_RWA_NOT_COVERED_CRM(BigDecimal r54_RWA_NOT_COVERED_CRM) {
		R54_RWA_NOT_COVERED_CRM = r54_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR54_RWA_UNSECURED_EXPOSURE() {
		return R54_RWA_UNSECURED_EXPOSURE;
	}
	public void setR54_RWA_UNSECURED_EXPOSURE(BigDecimal r54_RWA_UNSECURED_EXPOSURE) {
		R54_RWA_UNSECURED_EXPOSURE = r54_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR54_RWA_UNSECURED() {
		return R54_RWA_UNSECURED;
	}
	public void setR54_RWA_UNSECURED(BigDecimal r54_RWA_UNSECURED) {
		R54_RWA_UNSECURED = r54_RWA_UNSECURED;
	}
	public BigDecimal getR54_TOTAL_RWA() {
		return R54_TOTAL_RWA;
	}
	public void setR54_TOTAL_RWA(BigDecimal r54_TOTAL_RWA) {
		R54_TOTAL_RWA = r54_TOTAL_RWA;
	}
	public BigDecimal getR55_EXPOSURE_BEFORE_CRM() {
		return R55_EXPOSURE_BEFORE_CRM;
	}
	public void setR55_EXPOSURE_BEFORE_CRM(BigDecimal r55_EXPOSURE_BEFORE_CRM) {
		R55_EXPOSURE_BEFORE_CRM = r55_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR55_SPEC_PROVISION_PAST_DUE() {
		return R55_SPEC_PROVISION_PAST_DUE;
	}
	public void setR55_SPEC_PROVISION_PAST_DUE(BigDecimal r55_SPEC_PROVISION_PAST_DUE) {
		R55_SPEC_PROVISION_PAST_DUE = r55_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR55_ON_BAL_SHEET_NETTING_ELIG() {
		return R55_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR55_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r55_ON_BAL_SHEET_NETTING_ELIG) {
		R55_ON_BAL_SHEET_NETTING_ELIG = r55_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR55_TOTAL_EXPOSURE_AFTER_NET() {
		return R55_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR55_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r55_TOTAL_EXPOSURE_AFTER_NET) {
		R55_TOTAL_EXPOSURE_AFTER_NET = r55_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR55_CRM_ELIG_EXPOSURE_SUBS() {
		return R55_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR55_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r55_CRM_ELIG_EXPOSURE_SUBS) {
		R55_CRM_ELIG_EXPOSURE_SUBS = r55_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR55_ELIG_GUARANTEES() {
		return R55_ELIG_GUARANTEES;
	}
	public void setR55_ELIG_GUARANTEES(BigDecimal r55_ELIG_GUARANTEES) {
		R55_ELIG_GUARANTEES = r55_ELIG_GUARANTEES;
	}
	public BigDecimal getR55_CREDIT_DERIVATIVES() {
		return R55_CREDIT_DERIVATIVES;
	}
	public void setR55_CREDIT_DERIVATIVES(BigDecimal r55_CREDIT_DERIVATIVES) {
		R55_CREDIT_DERIVATIVES = r55_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR55_CRM_COVERED_EXPOSURE() {
		return R55_CRM_COVERED_EXPOSURE;
	}
	public void setR55_CRM_COVERED_EXPOSURE(BigDecimal r55_CRM_COVERED_EXPOSURE) {
		R55_CRM_COVERED_EXPOSURE = r55_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR55_CRM_NOT_COVERED_EXPOSURE() {
		return R55_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR55_CRM_NOT_COVERED_EXPOSURE(BigDecimal r55_CRM_NOT_COVERED_EXPOSURE) {
		R55_CRM_NOT_COVERED_EXPOSURE = r55_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR55_CRM_RISK_WEIGHT() {
		return R55_CRM_RISK_WEIGHT;
	}
	public void setR55_CRM_RISK_WEIGHT(BigDecimal r55_CRM_RISK_WEIGHT) {
		R55_CRM_RISK_WEIGHT = r55_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR55_RWA_CRM_COVERED() {
		return R55_RWA_CRM_COVERED;
	}
	public void setR55_RWA_CRM_COVERED(BigDecimal r55_RWA_CRM_COVERED) {
		R55_RWA_CRM_COVERED = r55_RWA_CRM_COVERED;
	}
	public BigDecimal getR55_ORIG_COUNTERPARTY_RW() {
		return R55_ORIG_COUNTERPARTY_RW;
	}
	public void setR55_ORIG_COUNTERPARTY_RW(BigDecimal r55_ORIG_COUNTERPARTY_RW) {
		R55_ORIG_COUNTERPARTY_RW = r55_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR55_RWA_CRM_NOT_COVERED() {
		return R55_RWA_CRM_NOT_COVERED;
	}
	public void setR55_RWA_CRM_NOT_COVERED(BigDecimal r55_RWA_CRM_NOT_COVERED) {
		R55_RWA_CRM_NOT_COVERED = r55_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR55_CRM_ELIG_EXPOSURE_COMP() {
		return R55_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR55_CRM_ELIG_EXPOSURE_COMP(BigDecimal r55_CRM_ELIG_EXPOSURE_COMP) {
		R55_CRM_ELIG_EXPOSURE_COMP = r55_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR55_EXPOSURE_AFTER_VOL_ADJ() {
		return R55_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR55_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r55_EXPOSURE_AFTER_VOL_ADJ) {
		R55_EXPOSURE_AFTER_VOL_ADJ = r55_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR55_COLLATERAL_CASH() {
		return R55_COLLATERAL_CASH;
	}
	public void setR55_COLLATERAL_CASH(BigDecimal r55_COLLATERAL_CASH) {
		R55_COLLATERAL_CASH = r55_COLLATERAL_CASH;
	}
	public BigDecimal getR55_COLLATERAL_TBILLS() {
		return R55_COLLATERAL_TBILLS;
	}
	public void setR55_COLLATERAL_TBILLS(BigDecimal r55_COLLATERAL_TBILLS) {
		R55_COLLATERAL_TBILLS = r55_COLLATERAL_TBILLS;
	}
	public BigDecimal getR55_COLLATERAL_DEBT_SEC() {
		return R55_COLLATERAL_DEBT_SEC;
	}
	public void setR55_COLLATERAL_DEBT_SEC(BigDecimal r55_COLLATERAL_DEBT_SEC) {
		R55_COLLATERAL_DEBT_SEC = r55_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR55_COLLATERAL_EQUITIES() {
		return R55_COLLATERAL_EQUITIES;
	}
	public void setR55_COLLATERAL_EQUITIES(BigDecimal r55_COLLATERAL_EQUITIES) {
		R55_COLLATERAL_EQUITIES = r55_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR55_COLLATERAL_MUTUAL_FUNDS() {
		return R55_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR55_COLLATERAL_MUTUAL_FUNDS(BigDecimal r55_COLLATERAL_MUTUAL_FUNDS) {
		R55_COLLATERAL_MUTUAL_FUNDS = r55_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR55_TOTAL_COLLATERAL_HAIRCUT() {
		return R55_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR55_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r55_TOTAL_COLLATERAL_HAIRCUT) {
		R55_TOTAL_COLLATERAL_HAIRCUT = r55_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR55_EXPOSURE_AFTER_CRM() {
		return R55_EXPOSURE_AFTER_CRM;
	}
	public void setR55_EXPOSURE_AFTER_CRM(BigDecimal r55_EXPOSURE_AFTER_CRM) {
		R55_EXPOSURE_AFTER_CRM = r55_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR55_RWA_NOT_COVERED_CRM() {
		return R55_RWA_NOT_COVERED_CRM;
	}
	public void setR55_RWA_NOT_COVERED_CRM(BigDecimal r55_RWA_NOT_COVERED_CRM) {
		R55_RWA_NOT_COVERED_CRM = r55_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR55_RWA_UNSECURED_EXPOSURE() {
		return R55_RWA_UNSECURED_EXPOSURE;
	}
	public void setR55_RWA_UNSECURED_EXPOSURE(BigDecimal r55_RWA_UNSECURED_EXPOSURE) {
		R55_RWA_UNSECURED_EXPOSURE = r55_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR55_RWA_UNSECURED() {
		return R55_RWA_UNSECURED;
	}
	public void setR55_RWA_UNSECURED(BigDecimal r55_RWA_UNSECURED) {
		R55_RWA_UNSECURED = r55_RWA_UNSECURED;
	}
	public BigDecimal getR55_TOTAL_RWA() {
		return R55_TOTAL_RWA;
	}
	public void setR55_TOTAL_RWA(BigDecimal r55_TOTAL_RWA) {
		R55_TOTAL_RWA = r55_TOTAL_RWA;
	}
	public BigDecimal getR56_EXPOSURE_BEFORE_CRM() {
		return R56_EXPOSURE_BEFORE_CRM;
	}
	public void setR56_EXPOSURE_BEFORE_CRM(BigDecimal r56_EXPOSURE_BEFORE_CRM) {
		R56_EXPOSURE_BEFORE_CRM = r56_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR56_SPEC_PROVISION_PAST_DUE() {
		return R56_SPEC_PROVISION_PAST_DUE;
	}
	public void setR56_SPEC_PROVISION_PAST_DUE(BigDecimal r56_SPEC_PROVISION_PAST_DUE) {
		R56_SPEC_PROVISION_PAST_DUE = r56_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR56_ON_BAL_SHEET_NETTING_ELIG() {
		return R56_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR56_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r56_ON_BAL_SHEET_NETTING_ELIG) {
		R56_ON_BAL_SHEET_NETTING_ELIG = r56_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR56_TOTAL_EXPOSURE_AFTER_NET() {
		return R56_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR56_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r56_TOTAL_EXPOSURE_AFTER_NET) {
		R56_TOTAL_EXPOSURE_AFTER_NET = r56_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR56_CRM_ELIG_EXPOSURE_SUBS() {
		return R56_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR56_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r56_CRM_ELIG_EXPOSURE_SUBS) {
		R56_CRM_ELIG_EXPOSURE_SUBS = r56_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR56_ELIG_GUARANTEES() {
		return R56_ELIG_GUARANTEES;
	}
	public void setR56_ELIG_GUARANTEES(BigDecimal r56_ELIG_GUARANTEES) {
		R56_ELIG_GUARANTEES = r56_ELIG_GUARANTEES;
	}
	public BigDecimal getR56_CREDIT_DERIVATIVES() {
		return R56_CREDIT_DERIVATIVES;
	}
	public void setR56_CREDIT_DERIVATIVES(BigDecimal r56_CREDIT_DERIVATIVES) {
		R56_CREDIT_DERIVATIVES = r56_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR56_CRM_COVERED_EXPOSURE() {
		return R56_CRM_COVERED_EXPOSURE;
	}
	public void setR56_CRM_COVERED_EXPOSURE(BigDecimal r56_CRM_COVERED_EXPOSURE) {
		R56_CRM_COVERED_EXPOSURE = r56_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR56_CRM_NOT_COVERED_EXPOSURE() {
		return R56_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR56_CRM_NOT_COVERED_EXPOSURE(BigDecimal r56_CRM_NOT_COVERED_EXPOSURE) {
		R56_CRM_NOT_COVERED_EXPOSURE = r56_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR56_CRM_RISK_WEIGHT() {
		return R56_CRM_RISK_WEIGHT;
	}
	public void setR56_CRM_RISK_WEIGHT(BigDecimal r56_CRM_RISK_WEIGHT) {
		R56_CRM_RISK_WEIGHT = r56_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR56_RWA_CRM_COVERED() {
		return R56_RWA_CRM_COVERED;
	}
	public void setR56_RWA_CRM_COVERED(BigDecimal r56_RWA_CRM_COVERED) {
		R56_RWA_CRM_COVERED = r56_RWA_CRM_COVERED;
	}
	public BigDecimal getR56_ORIG_COUNTERPARTY_RW() {
		return R56_ORIG_COUNTERPARTY_RW;
	}
	public void setR56_ORIG_COUNTERPARTY_RW(BigDecimal r56_ORIG_COUNTERPARTY_RW) {
		R56_ORIG_COUNTERPARTY_RW = r56_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR56_RWA_CRM_NOT_COVERED() {
		return R56_RWA_CRM_NOT_COVERED;
	}
	public void setR56_RWA_CRM_NOT_COVERED(BigDecimal r56_RWA_CRM_NOT_COVERED) {
		R56_RWA_CRM_NOT_COVERED = r56_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR56_CRM_ELIG_EXPOSURE_COMP() {
		return R56_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR56_CRM_ELIG_EXPOSURE_COMP(BigDecimal r56_CRM_ELIG_EXPOSURE_COMP) {
		R56_CRM_ELIG_EXPOSURE_COMP = r56_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR56_EXPOSURE_AFTER_VOL_ADJ() {
		return R56_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR56_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r56_EXPOSURE_AFTER_VOL_ADJ) {
		R56_EXPOSURE_AFTER_VOL_ADJ = r56_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR56_COLLATERAL_CASH() {
		return R56_COLLATERAL_CASH;
	}
	public void setR56_COLLATERAL_CASH(BigDecimal r56_COLLATERAL_CASH) {
		R56_COLLATERAL_CASH = r56_COLLATERAL_CASH;
	}
	public BigDecimal getR56_COLLATERAL_TBILLS() {
		return R56_COLLATERAL_TBILLS;
	}
	public void setR56_COLLATERAL_TBILLS(BigDecimal r56_COLLATERAL_TBILLS) {
		R56_COLLATERAL_TBILLS = r56_COLLATERAL_TBILLS;
	}
	public BigDecimal getR56_COLLATERAL_DEBT_SEC() {
		return R56_COLLATERAL_DEBT_SEC;
	}
	public void setR56_COLLATERAL_DEBT_SEC(BigDecimal r56_COLLATERAL_DEBT_SEC) {
		R56_COLLATERAL_DEBT_SEC = r56_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR56_COLLATERAL_EQUITIES() {
		return R56_COLLATERAL_EQUITIES;
	}
	public void setR56_COLLATERAL_EQUITIES(BigDecimal r56_COLLATERAL_EQUITIES) {
		R56_COLLATERAL_EQUITIES = r56_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR56_COLLATERAL_MUTUAL_FUNDS() {
		return R56_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR56_COLLATERAL_MUTUAL_FUNDS(BigDecimal r56_COLLATERAL_MUTUAL_FUNDS) {
		R56_COLLATERAL_MUTUAL_FUNDS = r56_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR56_TOTAL_COLLATERAL_HAIRCUT() {
		return R56_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR56_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r56_TOTAL_COLLATERAL_HAIRCUT) {
		R56_TOTAL_COLLATERAL_HAIRCUT = r56_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR56_EXPOSURE_AFTER_CRM() {
		return R56_EXPOSURE_AFTER_CRM;
	}
	public void setR56_EXPOSURE_AFTER_CRM(BigDecimal r56_EXPOSURE_AFTER_CRM) {
		R56_EXPOSURE_AFTER_CRM = r56_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR56_RWA_NOT_COVERED_CRM() {
		return R56_RWA_NOT_COVERED_CRM;
	}
	public void setR56_RWA_NOT_COVERED_CRM(BigDecimal r56_RWA_NOT_COVERED_CRM) {
		R56_RWA_NOT_COVERED_CRM = r56_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR56_RWA_UNSECURED_EXPOSURE() {
		return R56_RWA_UNSECURED_EXPOSURE;
	}
	public void setR56_RWA_UNSECURED_EXPOSURE(BigDecimal r56_RWA_UNSECURED_EXPOSURE) {
		R56_RWA_UNSECURED_EXPOSURE = r56_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR56_RWA_UNSECURED() {
		return R56_RWA_UNSECURED;
	}
	public void setR56_RWA_UNSECURED(BigDecimal r56_RWA_UNSECURED) {
		R56_RWA_UNSECURED = r56_RWA_UNSECURED;
	}
	public BigDecimal getR56_TOTAL_RWA() {
		return R56_TOTAL_RWA;
	}
	public void setR56_TOTAL_RWA(BigDecimal r56_TOTAL_RWA) {
		R56_TOTAL_RWA = r56_TOTAL_RWA;
	}
	public BigDecimal getR57_EXPOSURE_BEFORE_CRM() {
		return R57_EXPOSURE_BEFORE_CRM;
	}
	public void setR57_EXPOSURE_BEFORE_CRM(BigDecimal r57_EXPOSURE_BEFORE_CRM) {
		R57_EXPOSURE_BEFORE_CRM = r57_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR57_SPEC_PROVISION_PAST_DUE() {
		return R57_SPEC_PROVISION_PAST_DUE;
	}
	public void setR57_SPEC_PROVISION_PAST_DUE(BigDecimal r57_SPEC_PROVISION_PAST_DUE) {
		R57_SPEC_PROVISION_PAST_DUE = r57_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR57_ON_BAL_SHEET_NETTING_ELIG() {
		return R57_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR57_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r57_ON_BAL_SHEET_NETTING_ELIG) {
		R57_ON_BAL_SHEET_NETTING_ELIG = r57_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR57_TOTAL_EXPOSURE_AFTER_NET() {
		return R57_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR57_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r57_TOTAL_EXPOSURE_AFTER_NET) {
		R57_TOTAL_EXPOSURE_AFTER_NET = r57_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR57_CRM_ELIG_EXPOSURE_SUBS() {
		return R57_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR57_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r57_CRM_ELIG_EXPOSURE_SUBS) {
		R57_CRM_ELIG_EXPOSURE_SUBS = r57_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR57_ELIG_GUARANTEES() {
		return R57_ELIG_GUARANTEES;
	}
	public void setR57_ELIG_GUARANTEES(BigDecimal r57_ELIG_GUARANTEES) {
		R57_ELIG_GUARANTEES = r57_ELIG_GUARANTEES;
	}
	public BigDecimal getR57_CREDIT_DERIVATIVES() {
		return R57_CREDIT_DERIVATIVES;
	}
	public void setR57_CREDIT_DERIVATIVES(BigDecimal r57_CREDIT_DERIVATIVES) {
		R57_CREDIT_DERIVATIVES = r57_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR57_CRM_COVERED_EXPOSURE() {
		return R57_CRM_COVERED_EXPOSURE;
	}
	public void setR57_CRM_COVERED_EXPOSURE(BigDecimal r57_CRM_COVERED_EXPOSURE) {
		R57_CRM_COVERED_EXPOSURE = r57_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR57_CRM_NOT_COVERED_EXPOSURE() {
		return R57_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR57_CRM_NOT_COVERED_EXPOSURE(BigDecimal r57_CRM_NOT_COVERED_EXPOSURE) {
		R57_CRM_NOT_COVERED_EXPOSURE = r57_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR57_CRM_RISK_WEIGHT() {
		return R57_CRM_RISK_WEIGHT;
	}
	public void setR57_CRM_RISK_WEIGHT(BigDecimal r57_CRM_RISK_WEIGHT) {
		R57_CRM_RISK_WEIGHT = r57_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR57_RWA_CRM_COVERED() {
		return R57_RWA_CRM_COVERED;
	}
	public void setR57_RWA_CRM_COVERED(BigDecimal r57_RWA_CRM_COVERED) {
		R57_RWA_CRM_COVERED = r57_RWA_CRM_COVERED;
	}
	public BigDecimal getR57_ORIG_COUNTERPARTY_RW() {
		return R57_ORIG_COUNTERPARTY_RW;
	}
	public void setR57_ORIG_COUNTERPARTY_RW(BigDecimal r57_ORIG_COUNTERPARTY_RW) {
		R57_ORIG_COUNTERPARTY_RW = r57_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR57_RWA_CRM_NOT_COVERED() {
		return R57_RWA_CRM_NOT_COVERED;
	}
	public void setR57_RWA_CRM_NOT_COVERED(BigDecimal r57_RWA_CRM_NOT_COVERED) {
		R57_RWA_CRM_NOT_COVERED = r57_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR57_CRM_ELIG_EXPOSURE_COMP() {
		return R57_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR57_CRM_ELIG_EXPOSURE_COMP(BigDecimal r57_CRM_ELIG_EXPOSURE_COMP) {
		R57_CRM_ELIG_EXPOSURE_COMP = r57_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR57_EXPOSURE_AFTER_VOL_ADJ() {
		return R57_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR57_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r57_EXPOSURE_AFTER_VOL_ADJ) {
		R57_EXPOSURE_AFTER_VOL_ADJ = r57_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR57_COLLATERAL_CASH() {
		return R57_COLLATERAL_CASH;
	}
	public void setR57_COLLATERAL_CASH(BigDecimal r57_COLLATERAL_CASH) {
		R57_COLLATERAL_CASH = r57_COLLATERAL_CASH;
	}
	public BigDecimal getR57_COLLATERAL_TBILLS() {
		return R57_COLLATERAL_TBILLS;
	}
	public void setR57_COLLATERAL_TBILLS(BigDecimal r57_COLLATERAL_TBILLS) {
		R57_COLLATERAL_TBILLS = r57_COLLATERAL_TBILLS;
	}
	public BigDecimal getR57_COLLATERAL_DEBT_SEC() {
		return R57_COLLATERAL_DEBT_SEC;
	}
	public void setR57_COLLATERAL_DEBT_SEC(BigDecimal r57_COLLATERAL_DEBT_SEC) {
		R57_COLLATERAL_DEBT_SEC = r57_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR57_COLLATERAL_EQUITIES() {
		return R57_COLLATERAL_EQUITIES;
	}
	public void setR57_COLLATERAL_EQUITIES(BigDecimal r57_COLLATERAL_EQUITIES) {
		R57_COLLATERAL_EQUITIES = r57_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR57_COLLATERAL_MUTUAL_FUNDS() {
		return R57_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR57_COLLATERAL_MUTUAL_FUNDS(BigDecimal r57_COLLATERAL_MUTUAL_FUNDS) {
		R57_COLLATERAL_MUTUAL_FUNDS = r57_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR57_TOTAL_COLLATERAL_HAIRCUT() {
		return R57_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR57_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r57_TOTAL_COLLATERAL_HAIRCUT) {
		R57_TOTAL_COLLATERAL_HAIRCUT = r57_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR57_EXPOSURE_AFTER_CRM() {
		return R57_EXPOSURE_AFTER_CRM;
	}
	public void setR57_EXPOSURE_AFTER_CRM(BigDecimal r57_EXPOSURE_AFTER_CRM) {
		R57_EXPOSURE_AFTER_CRM = r57_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR57_RWA_NOT_COVERED_CRM() {
		return R57_RWA_NOT_COVERED_CRM;
	}
	public void setR57_RWA_NOT_COVERED_CRM(BigDecimal r57_RWA_NOT_COVERED_CRM) {
		R57_RWA_NOT_COVERED_CRM = r57_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR57_RWA_UNSECURED_EXPOSURE() {
		return R57_RWA_UNSECURED_EXPOSURE;
	}
	public void setR57_RWA_UNSECURED_EXPOSURE(BigDecimal r57_RWA_UNSECURED_EXPOSURE) {
		R57_RWA_UNSECURED_EXPOSURE = r57_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR57_RWA_UNSECURED() {
		return R57_RWA_UNSECURED;
	}
	public void setR57_RWA_UNSECURED(BigDecimal r57_RWA_UNSECURED) {
		R57_RWA_UNSECURED = r57_RWA_UNSECURED;
	}
	public BigDecimal getR57_TOTAL_RWA() {
		return R57_TOTAL_RWA;
	}
	public void setR57_TOTAL_RWA(BigDecimal r57_TOTAL_RWA) {
		R57_TOTAL_RWA = r57_TOTAL_RWA;
	}
	public BigDecimal getR58_EXPOSURE_BEFORE_CRM() {
		return R58_EXPOSURE_BEFORE_CRM;
	}
	public void setR58_EXPOSURE_BEFORE_CRM(BigDecimal r58_EXPOSURE_BEFORE_CRM) {
		R58_EXPOSURE_BEFORE_CRM = r58_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR58_SPEC_PROVISION_PAST_DUE() {
		return R58_SPEC_PROVISION_PAST_DUE;
	}
	public void setR58_SPEC_PROVISION_PAST_DUE(BigDecimal r58_SPEC_PROVISION_PAST_DUE) {
		R58_SPEC_PROVISION_PAST_DUE = r58_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR58_ON_BAL_SHEET_NETTING_ELIG() {
		return R58_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR58_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r58_ON_BAL_SHEET_NETTING_ELIG) {
		R58_ON_BAL_SHEET_NETTING_ELIG = r58_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR58_TOTAL_EXPOSURE_AFTER_NET() {
		return R58_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR58_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r58_TOTAL_EXPOSURE_AFTER_NET) {
		R58_TOTAL_EXPOSURE_AFTER_NET = r58_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR58_CRM_ELIG_EXPOSURE_SUBS() {
		return R58_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR58_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r58_CRM_ELIG_EXPOSURE_SUBS) {
		R58_CRM_ELIG_EXPOSURE_SUBS = r58_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR58_ELIG_GUARANTEES() {
		return R58_ELIG_GUARANTEES;
	}
	public void setR58_ELIG_GUARANTEES(BigDecimal r58_ELIG_GUARANTEES) {
		R58_ELIG_GUARANTEES = r58_ELIG_GUARANTEES;
	}
	public BigDecimal getR58_CREDIT_DERIVATIVES() {
		return R58_CREDIT_DERIVATIVES;
	}
	public void setR58_CREDIT_DERIVATIVES(BigDecimal r58_CREDIT_DERIVATIVES) {
		R58_CREDIT_DERIVATIVES = r58_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR58_CRM_COVERED_EXPOSURE() {
		return R58_CRM_COVERED_EXPOSURE;
	}
	public void setR58_CRM_COVERED_EXPOSURE(BigDecimal r58_CRM_COVERED_EXPOSURE) {
		R58_CRM_COVERED_EXPOSURE = r58_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR58_CRM_NOT_COVERED_EXPOSURE() {
		return R58_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR58_CRM_NOT_COVERED_EXPOSURE(BigDecimal r58_CRM_NOT_COVERED_EXPOSURE) {
		R58_CRM_NOT_COVERED_EXPOSURE = r58_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR58_CRM_RISK_WEIGHT() {
		return R58_CRM_RISK_WEIGHT;
	}
	public void setR58_CRM_RISK_WEIGHT(BigDecimal r58_CRM_RISK_WEIGHT) {
		R58_CRM_RISK_WEIGHT = r58_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR58_RWA_CRM_COVERED() {
		return R58_RWA_CRM_COVERED;
	}
	public void setR58_RWA_CRM_COVERED(BigDecimal r58_RWA_CRM_COVERED) {
		R58_RWA_CRM_COVERED = r58_RWA_CRM_COVERED;
	}
	public BigDecimal getR58_ORIG_COUNTERPARTY_RW() {
		return R58_ORIG_COUNTERPARTY_RW;
	}
	public void setR58_ORIG_COUNTERPARTY_RW(BigDecimal r58_ORIG_COUNTERPARTY_RW) {
		R58_ORIG_COUNTERPARTY_RW = r58_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR58_RWA_CRM_NOT_COVERED() {
		return R58_RWA_CRM_NOT_COVERED;
	}
	public void setR58_RWA_CRM_NOT_COVERED(BigDecimal r58_RWA_CRM_NOT_COVERED) {
		R58_RWA_CRM_NOT_COVERED = r58_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR58_CRM_ELIG_EXPOSURE_COMP() {
		return R58_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR58_CRM_ELIG_EXPOSURE_COMP(BigDecimal r58_CRM_ELIG_EXPOSURE_COMP) {
		R58_CRM_ELIG_EXPOSURE_COMP = r58_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR58_EXPOSURE_AFTER_VOL_ADJ() {
		return R58_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR58_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r58_EXPOSURE_AFTER_VOL_ADJ) {
		R58_EXPOSURE_AFTER_VOL_ADJ = r58_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR58_COLLATERAL_CASH() {
		return R58_COLLATERAL_CASH;
	}
	public void setR58_COLLATERAL_CASH(BigDecimal r58_COLLATERAL_CASH) {
		R58_COLLATERAL_CASH = r58_COLLATERAL_CASH;
	}
	public BigDecimal getR58_COLLATERAL_TBILLS() {
		return R58_COLLATERAL_TBILLS;
	}
	public void setR58_COLLATERAL_TBILLS(BigDecimal r58_COLLATERAL_TBILLS) {
		R58_COLLATERAL_TBILLS = r58_COLLATERAL_TBILLS;
	}
	public BigDecimal getR58_COLLATERAL_DEBT_SEC() {
		return R58_COLLATERAL_DEBT_SEC;
	}
	public void setR58_COLLATERAL_DEBT_SEC(BigDecimal r58_COLLATERAL_DEBT_SEC) {
		R58_COLLATERAL_DEBT_SEC = r58_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR58_COLLATERAL_EQUITIES() {
		return R58_COLLATERAL_EQUITIES;
	}
	public void setR58_COLLATERAL_EQUITIES(BigDecimal r58_COLLATERAL_EQUITIES) {
		R58_COLLATERAL_EQUITIES = r58_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR58_COLLATERAL_MUTUAL_FUNDS() {
		return R58_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR58_COLLATERAL_MUTUAL_FUNDS(BigDecimal r58_COLLATERAL_MUTUAL_FUNDS) {
		R58_COLLATERAL_MUTUAL_FUNDS = r58_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR58_TOTAL_COLLATERAL_HAIRCUT() {
		return R58_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR58_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r58_TOTAL_COLLATERAL_HAIRCUT) {
		R58_TOTAL_COLLATERAL_HAIRCUT = r58_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR58_EXPOSURE_AFTER_CRM() {
		return R58_EXPOSURE_AFTER_CRM;
	}
	public void setR58_EXPOSURE_AFTER_CRM(BigDecimal r58_EXPOSURE_AFTER_CRM) {
		R58_EXPOSURE_AFTER_CRM = r58_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR58_RWA_NOT_COVERED_CRM() {
		return R58_RWA_NOT_COVERED_CRM;
	}
	public void setR58_RWA_NOT_COVERED_CRM(BigDecimal r58_RWA_NOT_COVERED_CRM) {
		R58_RWA_NOT_COVERED_CRM = r58_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR58_RWA_UNSECURED_EXPOSURE() {
		return R58_RWA_UNSECURED_EXPOSURE;
	}
	public void setR58_RWA_UNSECURED_EXPOSURE(BigDecimal r58_RWA_UNSECURED_EXPOSURE) {
		R58_RWA_UNSECURED_EXPOSURE = r58_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR58_RWA_UNSECURED() {
		return R58_RWA_UNSECURED;
	}
	public void setR58_RWA_UNSECURED(BigDecimal r58_RWA_UNSECURED) {
		R58_RWA_UNSECURED = r58_RWA_UNSECURED;
	}
	public BigDecimal getR58_TOTAL_RWA() {
		return R58_TOTAL_RWA;
	}
	public void setR58_TOTAL_RWA(BigDecimal r58_TOTAL_RWA) {
		R58_TOTAL_RWA = r58_TOTAL_RWA;
	}
	public BigDecimal getR59_EXPOSURE_BEFORE_CRM() {
		return R59_EXPOSURE_BEFORE_CRM;
	}
	public void setR59_EXPOSURE_BEFORE_CRM(BigDecimal r59_EXPOSURE_BEFORE_CRM) {
		R59_EXPOSURE_BEFORE_CRM = r59_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR59_SPEC_PROVISION_PAST_DUE() {
		return R59_SPEC_PROVISION_PAST_DUE;
	}
	public void setR59_SPEC_PROVISION_PAST_DUE(BigDecimal r59_SPEC_PROVISION_PAST_DUE) {
		R59_SPEC_PROVISION_PAST_DUE = r59_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR59_ON_BAL_SHEET_NETTING_ELIG() {
		return R59_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR59_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r59_ON_BAL_SHEET_NETTING_ELIG) {
		R59_ON_BAL_SHEET_NETTING_ELIG = r59_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR59_TOTAL_EXPOSURE_AFTER_NET() {
		return R59_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR59_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r59_TOTAL_EXPOSURE_AFTER_NET) {
		R59_TOTAL_EXPOSURE_AFTER_NET = r59_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR59_CRM_ELIG_EXPOSURE_SUBS() {
		return R59_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR59_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r59_CRM_ELIG_EXPOSURE_SUBS) {
		R59_CRM_ELIG_EXPOSURE_SUBS = r59_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR59_ELIG_GUARANTEES() {
		return R59_ELIG_GUARANTEES;
	}
	public void setR59_ELIG_GUARANTEES(BigDecimal r59_ELIG_GUARANTEES) {
		R59_ELIG_GUARANTEES = r59_ELIG_GUARANTEES;
	}
	public BigDecimal getR59_CREDIT_DERIVATIVES() {
		return R59_CREDIT_DERIVATIVES;
	}
	public void setR59_CREDIT_DERIVATIVES(BigDecimal r59_CREDIT_DERIVATIVES) {
		R59_CREDIT_DERIVATIVES = r59_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR59_CRM_COVERED_EXPOSURE() {
		return R59_CRM_COVERED_EXPOSURE;
	}
	public void setR59_CRM_COVERED_EXPOSURE(BigDecimal r59_CRM_COVERED_EXPOSURE) {
		R59_CRM_COVERED_EXPOSURE = r59_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR59_CRM_NOT_COVERED_EXPOSURE() {
		return R59_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR59_CRM_NOT_COVERED_EXPOSURE(BigDecimal r59_CRM_NOT_COVERED_EXPOSURE) {
		R59_CRM_NOT_COVERED_EXPOSURE = r59_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR59_CRM_RISK_WEIGHT() {
		return R59_CRM_RISK_WEIGHT;
	}
	public void setR59_CRM_RISK_WEIGHT(BigDecimal r59_CRM_RISK_WEIGHT) {
		R59_CRM_RISK_WEIGHT = r59_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR59_RWA_CRM_COVERED() {
		return R59_RWA_CRM_COVERED;
	}
	public void setR59_RWA_CRM_COVERED(BigDecimal r59_RWA_CRM_COVERED) {
		R59_RWA_CRM_COVERED = r59_RWA_CRM_COVERED;
	}
	public BigDecimal getR59_ORIG_COUNTERPARTY_RW() {
		return R59_ORIG_COUNTERPARTY_RW;
	}
	public void setR59_ORIG_COUNTERPARTY_RW(BigDecimal r59_ORIG_COUNTERPARTY_RW) {
		R59_ORIG_COUNTERPARTY_RW = r59_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR59_RWA_CRM_NOT_COVERED() {
		return R59_RWA_CRM_NOT_COVERED;
	}
	public void setR59_RWA_CRM_NOT_COVERED(BigDecimal r59_RWA_CRM_NOT_COVERED) {
		R59_RWA_CRM_NOT_COVERED = r59_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR59_CRM_ELIG_EXPOSURE_COMP() {
		return R59_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR59_CRM_ELIG_EXPOSURE_COMP(BigDecimal r59_CRM_ELIG_EXPOSURE_COMP) {
		R59_CRM_ELIG_EXPOSURE_COMP = r59_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR59_EXPOSURE_AFTER_VOL_ADJ() {
		return R59_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR59_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r59_EXPOSURE_AFTER_VOL_ADJ) {
		R59_EXPOSURE_AFTER_VOL_ADJ = r59_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR59_COLLATERAL_CASH() {
		return R59_COLLATERAL_CASH;
	}
	public void setR59_COLLATERAL_CASH(BigDecimal r59_COLLATERAL_CASH) {
		R59_COLLATERAL_CASH = r59_COLLATERAL_CASH;
	}
	public BigDecimal getR59_COLLATERAL_TBILLS() {
		return R59_COLLATERAL_TBILLS;
	}
	public void setR59_COLLATERAL_TBILLS(BigDecimal r59_COLLATERAL_TBILLS) {
		R59_COLLATERAL_TBILLS = r59_COLLATERAL_TBILLS;
	}
	public BigDecimal getR59_COLLATERAL_DEBT_SEC() {
		return R59_COLLATERAL_DEBT_SEC;
	}
	public void setR59_COLLATERAL_DEBT_SEC(BigDecimal r59_COLLATERAL_DEBT_SEC) {
		R59_COLLATERAL_DEBT_SEC = r59_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR59_COLLATERAL_EQUITIES() {
		return R59_COLLATERAL_EQUITIES;
	}
	public void setR59_COLLATERAL_EQUITIES(BigDecimal r59_COLLATERAL_EQUITIES) {
		R59_COLLATERAL_EQUITIES = r59_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR59_COLLATERAL_MUTUAL_FUNDS() {
		return R59_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR59_COLLATERAL_MUTUAL_FUNDS(BigDecimal r59_COLLATERAL_MUTUAL_FUNDS) {
		R59_COLLATERAL_MUTUAL_FUNDS = r59_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR59_TOTAL_COLLATERAL_HAIRCUT() {
		return R59_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR59_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r59_TOTAL_COLLATERAL_HAIRCUT) {
		R59_TOTAL_COLLATERAL_HAIRCUT = r59_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR59_EXPOSURE_AFTER_CRM() {
		return R59_EXPOSURE_AFTER_CRM;
	}
	public void setR59_EXPOSURE_AFTER_CRM(BigDecimal r59_EXPOSURE_AFTER_CRM) {
		R59_EXPOSURE_AFTER_CRM = r59_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR59_RWA_NOT_COVERED_CRM() {
		return R59_RWA_NOT_COVERED_CRM;
	}
	public void setR59_RWA_NOT_COVERED_CRM(BigDecimal r59_RWA_NOT_COVERED_CRM) {
		R59_RWA_NOT_COVERED_CRM = r59_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR59_RWA_UNSECURED_EXPOSURE() {
		return R59_RWA_UNSECURED_EXPOSURE;
	}
	public void setR59_RWA_UNSECURED_EXPOSURE(BigDecimal r59_RWA_UNSECURED_EXPOSURE) {
		R59_RWA_UNSECURED_EXPOSURE = r59_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR59_RWA_UNSECURED() {
		return R59_RWA_UNSECURED;
	}
	public void setR59_RWA_UNSECURED(BigDecimal r59_RWA_UNSECURED) {
		R59_RWA_UNSECURED = r59_RWA_UNSECURED;
	}
	public BigDecimal getR59_TOTAL_RWA() {
		return R59_TOTAL_RWA;
	}
	public void setR59_TOTAL_RWA(BigDecimal r59_TOTAL_RWA) {
		R59_TOTAL_RWA = r59_TOTAL_RWA;
	}
	public BigDecimal getR60_EXPOSURE_BEFORE_CRM() {
		return R60_EXPOSURE_BEFORE_CRM;
	}
	public void setR60_EXPOSURE_BEFORE_CRM(BigDecimal r60_EXPOSURE_BEFORE_CRM) {
		R60_EXPOSURE_BEFORE_CRM = r60_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR60_SPEC_PROVISION_PAST_DUE() {
		return R60_SPEC_PROVISION_PAST_DUE;
	}
	public void setR60_SPEC_PROVISION_PAST_DUE(BigDecimal r60_SPEC_PROVISION_PAST_DUE) {
		R60_SPEC_PROVISION_PAST_DUE = r60_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR60_ON_BAL_SHEET_NETTING_ELIG() {
		return R60_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR60_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r60_ON_BAL_SHEET_NETTING_ELIG) {
		R60_ON_BAL_SHEET_NETTING_ELIG = r60_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR60_TOTAL_EXPOSURE_AFTER_NET() {
		return R60_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR60_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r60_TOTAL_EXPOSURE_AFTER_NET) {
		R60_TOTAL_EXPOSURE_AFTER_NET = r60_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR60_CRM_ELIG_EXPOSURE_SUBS() {
		return R60_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR60_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r60_CRM_ELIG_EXPOSURE_SUBS) {
		R60_CRM_ELIG_EXPOSURE_SUBS = r60_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR60_ELIG_GUARANTEES() {
		return R60_ELIG_GUARANTEES;
	}
	public void setR60_ELIG_GUARANTEES(BigDecimal r60_ELIG_GUARANTEES) {
		R60_ELIG_GUARANTEES = r60_ELIG_GUARANTEES;
	}
	public BigDecimal getR60_CREDIT_DERIVATIVES() {
		return R60_CREDIT_DERIVATIVES;
	}
	public void setR60_CREDIT_DERIVATIVES(BigDecimal r60_CREDIT_DERIVATIVES) {
		R60_CREDIT_DERIVATIVES = r60_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR60_CRM_COVERED_EXPOSURE() {
		return R60_CRM_COVERED_EXPOSURE;
	}
	public void setR60_CRM_COVERED_EXPOSURE(BigDecimal r60_CRM_COVERED_EXPOSURE) {
		R60_CRM_COVERED_EXPOSURE = r60_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR60_CRM_NOT_COVERED_EXPOSURE() {
		return R60_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR60_CRM_NOT_COVERED_EXPOSURE(BigDecimal r60_CRM_NOT_COVERED_EXPOSURE) {
		R60_CRM_NOT_COVERED_EXPOSURE = r60_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR60_CRM_RISK_WEIGHT() {
		return R60_CRM_RISK_WEIGHT;
	}
	public void setR60_CRM_RISK_WEIGHT(BigDecimal r60_CRM_RISK_WEIGHT) {
		R60_CRM_RISK_WEIGHT = r60_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR60_RWA_CRM_COVERED() {
		return R60_RWA_CRM_COVERED;
	}
	public void setR60_RWA_CRM_COVERED(BigDecimal r60_RWA_CRM_COVERED) {
		R60_RWA_CRM_COVERED = r60_RWA_CRM_COVERED;
	}
	public BigDecimal getR60_ORIG_COUNTERPARTY_RW() {
		return R60_ORIG_COUNTERPARTY_RW;
	}
	public void setR60_ORIG_COUNTERPARTY_RW(BigDecimal r60_ORIG_COUNTERPARTY_RW) {
		R60_ORIG_COUNTERPARTY_RW = r60_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR60_RWA_CRM_NOT_COVERED() {
		return R60_RWA_CRM_NOT_COVERED;
	}
	public void setR60_RWA_CRM_NOT_COVERED(BigDecimal r60_RWA_CRM_NOT_COVERED) {
		R60_RWA_CRM_NOT_COVERED = r60_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR60_CRM_ELIG_EXPOSURE_COMP() {
		return R60_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR60_CRM_ELIG_EXPOSURE_COMP(BigDecimal r60_CRM_ELIG_EXPOSURE_COMP) {
		R60_CRM_ELIG_EXPOSURE_COMP = r60_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR60_EXPOSURE_AFTER_VOL_ADJ() {
		return R60_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR60_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r60_EXPOSURE_AFTER_VOL_ADJ) {
		R60_EXPOSURE_AFTER_VOL_ADJ = r60_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR60_COLLATERAL_CASH() {
		return R60_COLLATERAL_CASH;
	}
	public void setR60_COLLATERAL_CASH(BigDecimal r60_COLLATERAL_CASH) {
		R60_COLLATERAL_CASH = r60_COLLATERAL_CASH;
	}
	public BigDecimal getR60_COLLATERAL_TBILLS() {
		return R60_COLLATERAL_TBILLS;
	}
	public void setR60_COLLATERAL_TBILLS(BigDecimal r60_COLLATERAL_TBILLS) {
		R60_COLLATERAL_TBILLS = r60_COLLATERAL_TBILLS;
	}
	public BigDecimal getR60_COLLATERAL_DEBT_SEC() {
		return R60_COLLATERAL_DEBT_SEC;
	}
	public void setR60_COLLATERAL_DEBT_SEC(BigDecimal r60_COLLATERAL_DEBT_SEC) {
		R60_COLLATERAL_DEBT_SEC = r60_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR60_COLLATERAL_EQUITIES() {
		return R60_COLLATERAL_EQUITIES;
	}
	public void setR60_COLLATERAL_EQUITIES(BigDecimal r60_COLLATERAL_EQUITIES) {
		R60_COLLATERAL_EQUITIES = r60_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR60_COLLATERAL_MUTUAL_FUNDS() {
		return R60_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR60_COLLATERAL_MUTUAL_FUNDS(BigDecimal r60_COLLATERAL_MUTUAL_FUNDS) {
		R60_COLLATERAL_MUTUAL_FUNDS = r60_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR60_TOTAL_COLLATERAL_HAIRCUT() {
		return R60_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR60_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r60_TOTAL_COLLATERAL_HAIRCUT) {
		R60_TOTAL_COLLATERAL_HAIRCUT = r60_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR60_EXPOSURE_AFTER_CRM() {
		return R60_EXPOSURE_AFTER_CRM;
	}
	public void setR60_EXPOSURE_AFTER_CRM(BigDecimal r60_EXPOSURE_AFTER_CRM) {
		R60_EXPOSURE_AFTER_CRM = r60_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR60_RWA_NOT_COVERED_CRM() {
		return R60_RWA_NOT_COVERED_CRM;
	}
	public void setR60_RWA_NOT_COVERED_CRM(BigDecimal r60_RWA_NOT_COVERED_CRM) {
		R60_RWA_NOT_COVERED_CRM = r60_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR60_RWA_UNSECURED_EXPOSURE() {
		return R60_RWA_UNSECURED_EXPOSURE;
	}
	public void setR60_RWA_UNSECURED_EXPOSURE(BigDecimal r60_RWA_UNSECURED_EXPOSURE) {
		R60_RWA_UNSECURED_EXPOSURE = r60_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR60_RWA_UNSECURED() {
		return R60_RWA_UNSECURED;
	}
	public void setR60_RWA_UNSECURED(BigDecimal r60_RWA_UNSECURED) {
		R60_RWA_UNSECURED = r60_RWA_UNSECURED;
	}
	public BigDecimal getR60_TOTAL_RWA() {
		return R60_TOTAL_RWA;
	}
	public void setR60_TOTAL_RWA(BigDecimal r60_TOTAL_RWA) {
		R60_TOTAL_RWA = r60_TOTAL_RWA;
	}
	public BigDecimal getR61_EXPOSURE_BEFORE_CRM() {
		return R61_EXPOSURE_BEFORE_CRM;
	}
	public void setR61_EXPOSURE_BEFORE_CRM(BigDecimal r61_EXPOSURE_BEFORE_CRM) {
		R61_EXPOSURE_BEFORE_CRM = r61_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR61_SPEC_PROVISION_PAST_DUE() {
		return R61_SPEC_PROVISION_PAST_DUE;
	}
	public void setR61_SPEC_PROVISION_PAST_DUE(BigDecimal r61_SPEC_PROVISION_PAST_DUE) {
		R61_SPEC_PROVISION_PAST_DUE = r61_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR61_ON_BAL_SHEET_NETTING_ELIG() {
		return R61_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR61_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r61_ON_BAL_SHEET_NETTING_ELIG) {
		R61_ON_BAL_SHEET_NETTING_ELIG = r61_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR61_TOTAL_EXPOSURE_AFTER_NET() {
		return R61_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR61_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r61_TOTAL_EXPOSURE_AFTER_NET) {
		R61_TOTAL_EXPOSURE_AFTER_NET = r61_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR61_CRM_ELIG_EXPOSURE_SUBS() {
		return R61_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR61_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r61_CRM_ELIG_EXPOSURE_SUBS) {
		R61_CRM_ELIG_EXPOSURE_SUBS = r61_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR61_ELIG_GUARANTEES() {
		return R61_ELIG_GUARANTEES;
	}
	public void setR61_ELIG_GUARANTEES(BigDecimal r61_ELIG_GUARANTEES) {
		R61_ELIG_GUARANTEES = r61_ELIG_GUARANTEES;
	}
	public BigDecimal getR61_CREDIT_DERIVATIVES() {
		return R61_CREDIT_DERIVATIVES;
	}
	public void setR61_CREDIT_DERIVATIVES(BigDecimal r61_CREDIT_DERIVATIVES) {
		R61_CREDIT_DERIVATIVES = r61_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR61_CRM_COVERED_EXPOSURE() {
		return R61_CRM_COVERED_EXPOSURE;
	}
	public void setR61_CRM_COVERED_EXPOSURE(BigDecimal r61_CRM_COVERED_EXPOSURE) {
		R61_CRM_COVERED_EXPOSURE = r61_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR61_CRM_NOT_COVERED_EXPOSURE() {
		return R61_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR61_CRM_NOT_COVERED_EXPOSURE(BigDecimal r61_CRM_NOT_COVERED_EXPOSURE) {
		R61_CRM_NOT_COVERED_EXPOSURE = r61_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR61_CRM_RISK_WEIGHT() {
		return R61_CRM_RISK_WEIGHT;
	}
	public void setR61_CRM_RISK_WEIGHT(BigDecimal r61_CRM_RISK_WEIGHT) {
		R61_CRM_RISK_WEIGHT = r61_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR61_RWA_CRM_COVERED() {
		return R61_RWA_CRM_COVERED;
	}
	public void setR61_RWA_CRM_COVERED(BigDecimal r61_RWA_CRM_COVERED) {
		R61_RWA_CRM_COVERED = r61_RWA_CRM_COVERED;
	}
	public BigDecimal getR61_ORIG_COUNTERPARTY_RW() {
		return R61_ORIG_COUNTERPARTY_RW;
	}
	public void setR61_ORIG_COUNTERPARTY_RW(BigDecimal r61_ORIG_COUNTERPARTY_RW) {
		R61_ORIG_COUNTERPARTY_RW = r61_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR61_RWA_CRM_NOT_COVERED() {
		return R61_RWA_CRM_NOT_COVERED;
	}
	public void setR61_RWA_CRM_NOT_COVERED(BigDecimal r61_RWA_CRM_NOT_COVERED) {
		R61_RWA_CRM_NOT_COVERED = r61_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR61_CRM_ELIG_EXPOSURE_COMP() {
		return R61_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR61_CRM_ELIG_EXPOSURE_COMP(BigDecimal r61_CRM_ELIG_EXPOSURE_COMP) {
		R61_CRM_ELIG_EXPOSURE_COMP = r61_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR61_EXPOSURE_AFTER_VOL_ADJ() {
		return R61_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR61_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r61_EXPOSURE_AFTER_VOL_ADJ) {
		R61_EXPOSURE_AFTER_VOL_ADJ = r61_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR61_COLLATERAL_CASH() {
		return R61_COLLATERAL_CASH;
	}
	public void setR61_COLLATERAL_CASH(BigDecimal r61_COLLATERAL_CASH) {
		R61_COLLATERAL_CASH = r61_COLLATERAL_CASH;
	}
	public BigDecimal getR61_COLLATERAL_TBILLS() {
		return R61_COLLATERAL_TBILLS;
	}
	public void setR61_COLLATERAL_TBILLS(BigDecimal r61_COLLATERAL_TBILLS) {
		R61_COLLATERAL_TBILLS = r61_COLLATERAL_TBILLS;
	}
	public BigDecimal getR61_COLLATERAL_DEBT_SEC() {
		return R61_COLLATERAL_DEBT_SEC;
	}
	public void setR61_COLLATERAL_DEBT_SEC(BigDecimal r61_COLLATERAL_DEBT_SEC) {
		R61_COLLATERAL_DEBT_SEC = r61_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR61_COLLATERAL_EQUITIES() {
		return R61_COLLATERAL_EQUITIES;
	}
	public void setR61_COLLATERAL_EQUITIES(BigDecimal r61_COLLATERAL_EQUITIES) {
		R61_COLLATERAL_EQUITIES = r61_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR61_COLLATERAL_MUTUAL_FUNDS() {
		return R61_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR61_COLLATERAL_MUTUAL_FUNDS(BigDecimal r61_COLLATERAL_MUTUAL_FUNDS) {
		R61_COLLATERAL_MUTUAL_FUNDS = r61_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR61_TOTAL_COLLATERAL_HAIRCUT() {
		return R61_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR61_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r61_TOTAL_COLLATERAL_HAIRCUT) {
		R61_TOTAL_COLLATERAL_HAIRCUT = r61_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR61_EXPOSURE_AFTER_CRM() {
		return R61_EXPOSURE_AFTER_CRM;
	}
	public void setR61_EXPOSURE_AFTER_CRM(BigDecimal r61_EXPOSURE_AFTER_CRM) {
		R61_EXPOSURE_AFTER_CRM = r61_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR61_RWA_NOT_COVERED_CRM() {
		return R61_RWA_NOT_COVERED_CRM;
	}
	public void setR61_RWA_NOT_COVERED_CRM(BigDecimal r61_RWA_NOT_COVERED_CRM) {
		R61_RWA_NOT_COVERED_CRM = r61_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR61_RWA_UNSECURED_EXPOSURE() {
		return R61_RWA_UNSECURED_EXPOSURE;
	}
	public void setR61_RWA_UNSECURED_EXPOSURE(BigDecimal r61_RWA_UNSECURED_EXPOSURE) {
		R61_RWA_UNSECURED_EXPOSURE = r61_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR61_RWA_UNSECURED() {
		return R61_RWA_UNSECURED;
	}
	public void setR61_RWA_UNSECURED(BigDecimal r61_RWA_UNSECURED) {
		R61_RWA_UNSECURED = r61_RWA_UNSECURED;
	}
	public BigDecimal getR61_TOTAL_RWA() {
		return R61_TOTAL_RWA;
	}
	public void setR61_TOTAL_RWA(BigDecimal r61_TOTAL_RWA) {
		R61_TOTAL_RWA = r61_TOTAL_RWA;
	}
	public BigDecimal getR62_EXPOSURE_BEFORE_CRM() {
		return R62_EXPOSURE_BEFORE_CRM;
	}
	public void setR62_EXPOSURE_BEFORE_CRM(BigDecimal r62_EXPOSURE_BEFORE_CRM) {
		R62_EXPOSURE_BEFORE_CRM = r62_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR62_SPEC_PROVISION_PAST_DUE() {
		return R62_SPEC_PROVISION_PAST_DUE;
	}
	public void setR62_SPEC_PROVISION_PAST_DUE(BigDecimal r62_SPEC_PROVISION_PAST_DUE) {
		R62_SPEC_PROVISION_PAST_DUE = r62_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR62_ON_BAL_SHEET_NETTING_ELIG() {
		return R62_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR62_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r62_ON_BAL_SHEET_NETTING_ELIG) {
		R62_ON_BAL_SHEET_NETTING_ELIG = r62_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR62_TOTAL_EXPOSURE_AFTER_NET() {
		return R62_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR62_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r62_TOTAL_EXPOSURE_AFTER_NET) {
		R62_TOTAL_EXPOSURE_AFTER_NET = r62_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR62_CRM_ELIG_EXPOSURE_SUBS() {
		return R62_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR62_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r62_CRM_ELIG_EXPOSURE_SUBS) {
		R62_CRM_ELIG_EXPOSURE_SUBS = r62_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR62_ELIG_GUARANTEES() {
		return R62_ELIG_GUARANTEES;
	}
	public void setR62_ELIG_GUARANTEES(BigDecimal r62_ELIG_GUARANTEES) {
		R62_ELIG_GUARANTEES = r62_ELIG_GUARANTEES;
	}
	public BigDecimal getR62_CREDIT_DERIVATIVES() {
		return R62_CREDIT_DERIVATIVES;
	}
	public void setR62_CREDIT_DERIVATIVES(BigDecimal r62_CREDIT_DERIVATIVES) {
		R62_CREDIT_DERIVATIVES = r62_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR62_CRM_COVERED_EXPOSURE() {
		return R62_CRM_COVERED_EXPOSURE;
	}
	public void setR62_CRM_COVERED_EXPOSURE(BigDecimal r62_CRM_COVERED_EXPOSURE) {
		R62_CRM_COVERED_EXPOSURE = r62_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR62_CRM_NOT_COVERED_EXPOSURE() {
		return R62_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR62_CRM_NOT_COVERED_EXPOSURE(BigDecimal r62_CRM_NOT_COVERED_EXPOSURE) {
		R62_CRM_NOT_COVERED_EXPOSURE = r62_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR62_CRM_RISK_WEIGHT() {
		return R62_CRM_RISK_WEIGHT;
	}
	public void setR62_CRM_RISK_WEIGHT(BigDecimal r62_CRM_RISK_WEIGHT) {
		R62_CRM_RISK_WEIGHT = r62_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR62_RWA_CRM_COVERED() {
		return R62_RWA_CRM_COVERED;
	}
	public void setR62_RWA_CRM_COVERED(BigDecimal r62_RWA_CRM_COVERED) {
		R62_RWA_CRM_COVERED = r62_RWA_CRM_COVERED;
	}
	public BigDecimal getR62_ORIG_COUNTERPARTY_RW() {
		return R62_ORIG_COUNTERPARTY_RW;
	}
	public void setR62_ORIG_COUNTERPARTY_RW(BigDecimal r62_ORIG_COUNTERPARTY_RW) {
		R62_ORIG_COUNTERPARTY_RW = r62_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR62_RWA_CRM_NOT_COVERED() {
		return R62_RWA_CRM_NOT_COVERED;
	}
	public void setR62_RWA_CRM_NOT_COVERED(BigDecimal r62_RWA_CRM_NOT_COVERED) {
		R62_RWA_CRM_NOT_COVERED = r62_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR62_CRM_ELIG_EXPOSURE_COMP() {
		return R62_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR62_CRM_ELIG_EXPOSURE_COMP(BigDecimal r62_CRM_ELIG_EXPOSURE_COMP) {
		R62_CRM_ELIG_EXPOSURE_COMP = r62_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR62_EXPOSURE_AFTER_VOL_ADJ() {
		return R62_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR62_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r62_EXPOSURE_AFTER_VOL_ADJ) {
		R62_EXPOSURE_AFTER_VOL_ADJ = r62_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR62_COLLATERAL_CASH() {
		return R62_COLLATERAL_CASH;
	}
	public void setR62_COLLATERAL_CASH(BigDecimal r62_COLLATERAL_CASH) {
		R62_COLLATERAL_CASH = r62_COLLATERAL_CASH;
	}
	public BigDecimal getR62_COLLATERAL_TBILLS() {
		return R62_COLLATERAL_TBILLS;
	}
	public void setR62_COLLATERAL_TBILLS(BigDecimal r62_COLLATERAL_TBILLS) {
		R62_COLLATERAL_TBILLS = r62_COLLATERAL_TBILLS;
	}
	public BigDecimal getR62_COLLATERAL_DEBT_SEC() {
		return R62_COLLATERAL_DEBT_SEC;
	}
	public void setR62_COLLATERAL_DEBT_SEC(BigDecimal r62_COLLATERAL_DEBT_SEC) {
		R62_COLLATERAL_DEBT_SEC = r62_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR62_COLLATERAL_EQUITIES() {
		return R62_COLLATERAL_EQUITIES;
	}
	public void setR62_COLLATERAL_EQUITIES(BigDecimal r62_COLLATERAL_EQUITIES) {
		R62_COLLATERAL_EQUITIES = r62_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR62_COLLATERAL_MUTUAL_FUNDS() {
		return R62_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR62_COLLATERAL_MUTUAL_FUNDS(BigDecimal r62_COLLATERAL_MUTUAL_FUNDS) {
		R62_COLLATERAL_MUTUAL_FUNDS = r62_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR62_TOTAL_COLLATERAL_HAIRCUT() {
		return R62_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR62_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r62_TOTAL_COLLATERAL_HAIRCUT) {
		R62_TOTAL_COLLATERAL_HAIRCUT = r62_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR62_EXPOSURE_AFTER_CRM() {
		return R62_EXPOSURE_AFTER_CRM;
	}
	public void setR62_EXPOSURE_AFTER_CRM(BigDecimal r62_EXPOSURE_AFTER_CRM) {
		R62_EXPOSURE_AFTER_CRM = r62_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR62_RWA_NOT_COVERED_CRM() {
		return R62_RWA_NOT_COVERED_CRM;
	}
	public void setR62_RWA_NOT_COVERED_CRM(BigDecimal r62_RWA_NOT_COVERED_CRM) {
		R62_RWA_NOT_COVERED_CRM = r62_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR62_RWA_UNSECURED_EXPOSURE() {
		return R62_RWA_UNSECURED_EXPOSURE;
	}
	public void setR62_RWA_UNSECURED_EXPOSURE(BigDecimal r62_RWA_UNSECURED_EXPOSURE) {
		R62_RWA_UNSECURED_EXPOSURE = r62_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR62_RWA_UNSECURED() {
		return R62_RWA_UNSECURED;
	}
	public void setR62_RWA_UNSECURED(BigDecimal r62_RWA_UNSECURED) {
		R62_RWA_UNSECURED = r62_RWA_UNSECURED;
	}
	public BigDecimal getR62_TOTAL_RWA() {
		return R62_TOTAL_RWA;
	}
	public void setR62_TOTAL_RWA(BigDecimal r62_TOTAL_RWA) {
		R62_TOTAL_RWA = r62_TOTAL_RWA;
	}
	public BigDecimal getR63_EXPOSURE_BEFORE_CRM() {
		return R63_EXPOSURE_BEFORE_CRM;
	}
	public void setR63_EXPOSURE_BEFORE_CRM(BigDecimal r63_EXPOSURE_BEFORE_CRM) {
		R63_EXPOSURE_BEFORE_CRM = r63_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR63_SPEC_PROVISION_PAST_DUE() {
		return R63_SPEC_PROVISION_PAST_DUE;
	}
	public void setR63_SPEC_PROVISION_PAST_DUE(BigDecimal r63_SPEC_PROVISION_PAST_DUE) {
		R63_SPEC_PROVISION_PAST_DUE = r63_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR63_ON_BAL_SHEET_NETTING_ELIG() {
		return R63_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR63_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r63_ON_BAL_SHEET_NETTING_ELIG) {
		R63_ON_BAL_SHEET_NETTING_ELIG = r63_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR63_TOTAL_EXPOSURE_AFTER_NET() {
		return R63_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR63_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r63_TOTAL_EXPOSURE_AFTER_NET) {
		R63_TOTAL_EXPOSURE_AFTER_NET = r63_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR63_CRM_ELIG_EXPOSURE_SUBS() {
		return R63_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR63_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r63_CRM_ELIG_EXPOSURE_SUBS) {
		R63_CRM_ELIG_EXPOSURE_SUBS = r63_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR63_ELIG_GUARANTEES() {
		return R63_ELIG_GUARANTEES;
	}
	public void setR63_ELIG_GUARANTEES(BigDecimal r63_ELIG_GUARANTEES) {
		R63_ELIG_GUARANTEES = r63_ELIG_GUARANTEES;
	}
	public BigDecimal getR63_CREDIT_DERIVATIVES() {
		return R63_CREDIT_DERIVATIVES;
	}
	public void setR63_CREDIT_DERIVATIVES(BigDecimal r63_CREDIT_DERIVATIVES) {
		R63_CREDIT_DERIVATIVES = r63_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR63_CRM_COVERED_EXPOSURE() {
		return R63_CRM_COVERED_EXPOSURE;
	}
	public void setR63_CRM_COVERED_EXPOSURE(BigDecimal r63_CRM_COVERED_EXPOSURE) {
		R63_CRM_COVERED_EXPOSURE = r63_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR63_CRM_NOT_COVERED_EXPOSURE() {
		return R63_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR63_CRM_NOT_COVERED_EXPOSURE(BigDecimal r63_CRM_NOT_COVERED_EXPOSURE) {
		R63_CRM_NOT_COVERED_EXPOSURE = r63_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR63_CRM_RISK_WEIGHT() {
		return R63_CRM_RISK_WEIGHT;
	}
	public void setR63_CRM_RISK_WEIGHT(BigDecimal r63_CRM_RISK_WEIGHT) {
		R63_CRM_RISK_WEIGHT = r63_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR63_RWA_CRM_COVERED() {
		return R63_RWA_CRM_COVERED;
	}
	public void setR63_RWA_CRM_COVERED(BigDecimal r63_RWA_CRM_COVERED) {
		R63_RWA_CRM_COVERED = r63_RWA_CRM_COVERED;
	}
	public BigDecimal getR63_ORIG_COUNTERPARTY_RW() {
		return R63_ORIG_COUNTERPARTY_RW;
	}
	public void setR63_ORIG_COUNTERPARTY_RW(BigDecimal r63_ORIG_COUNTERPARTY_RW) {
		R63_ORIG_COUNTERPARTY_RW = r63_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR63_RWA_CRM_NOT_COVERED() {
		return R63_RWA_CRM_NOT_COVERED;
	}
	public void setR63_RWA_CRM_NOT_COVERED(BigDecimal r63_RWA_CRM_NOT_COVERED) {
		R63_RWA_CRM_NOT_COVERED = r63_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR63_CRM_ELIG_EXPOSURE_COMP() {
		return R63_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR63_CRM_ELIG_EXPOSURE_COMP(BigDecimal r63_CRM_ELIG_EXPOSURE_COMP) {
		R63_CRM_ELIG_EXPOSURE_COMP = r63_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR63_EXPOSURE_AFTER_VOL_ADJ() {
		return R63_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR63_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r63_EXPOSURE_AFTER_VOL_ADJ) {
		R63_EXPOSURE_AFTER_VOL_ADJ = r63_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR63_COLLATERAL_CASH() {
		return R63_COLLATERAL_CASH;
	}
	public void setR63_COLLATERAL_CASH(BigDecimal r63_COLLATERAL_CASH) {
		R63_COLLATERAL_CASH = r63_COLLATERAL_CASH;
	}
	public BigDecimal getR63_COLLATERAL_TBILLS() {
		return R63_COLLATERAL_TBILLS;
	}
	public void setR63_COLLATERAL_TBILLS(BigDecimal r63_COLLATERAL_TBILLS) {
		R63_COLLATERAL_TBILLS = r63_COLLATERAL_TBILLS;
	}
	public BigDecimal getR63_COLLATERAL_DEBT_SEC() {
		return R63_COLLATERAL_DEBT_SEC;
	}
	public void setR63_COLLATERAL_DEBT_SEC(BigDecimal r63_COLLATERAL_DEBT_SEC) {
		R63_COLLATERAL_DEBT_SEC = r63_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR63_COLLATERAL_EQUITIES() {
		return R63_COLLATERAL_EQUITIES;
	}
	public void setR63_COLLATERAL_EQUITIES(BigDecimal r63_COLLATERAL_EQUITIES) {
		R63_COLLATERAL_EQUITIES = r63_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR63_COLLATERAL_MUTUAL_FUNDS() {
		return R63_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR63_COLLATERAL_MUTUAL_FUNDS(BigDecimal r63_COLLATERAL_MUTUAL_FUNDS) {
		R63_COLLATERAL_MUTUAL_FUNDS = r63_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR63_TOTAL_COLLATERAL_HAIRCUT() {
		return R63_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR63_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r63_TOTAL_COLLATERAL_HAIRCUT) {
		R63_TOTAL_COLLATERAL_HAIRCUT = r63_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR63_EXPOSURE_AFTER_CRM() {
		return R63_EXPOSURE_AFTER_CRM;
	}
	public void setR63_EXPOSURE_AFTER_CRM(BigDecimal r63_EXPOSURE_AFTER_CRM) {
		R63_EXPOSURE_AFTER_CRM = r63_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR63_RWA_NOT_COVERED_CRM() {
		return R63_RWA_NOT_COVERED_CRM;
	}
	public void setR63_RWA_NOT_COVERED_CRM(BigDecimal r63_RWA_NOT_COVERED_CRM) {
		R63_RWA_NOT_COVERED_CRM = r63_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR63_RWA_UNSECURED_EXPOSURE() {
		return R63_RWA_UNSECURED_EXPOSURE;
	}
	public void setR63_RWA_UNSECURED_EXPOSURE(BigDecimal r63_RWA_UNSECURED_EXPOSURE) {
		R63_RWA_UNSECURED_EXPOSURE = r63_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR63_RWA_UNSECURED() {
		return R63_RWA_UNSECURED;
	}
	public void setR63_RWA_UNSECURED(BigDecimal r63_RWA_UNSECURED) {
		R63_RWA_UNSECURED = r63_RWA_UNSECURED;
	}
	public BigDecimal getR63_TOTAL_RWA() {
		return R63_TOTAL_RWA;
	}
	public void setR63_TOTAL_RWA(BigDecimal r63_TOTAL_RWA) {
		R63_TOTAL_RWA = r63_TOTAL_RWA;
	}
	public BigDecimal getR64_EXPOSURE_BEFORE_CRM() {
		return R64_EXPOSURE_BEFORE_CRM;
	}
	public void setR64_EXPOSURE_BEFORE_CRM(BigDecimal r64_EXPOSURE_BEFORE_CRM) {
		R64_EXPOSURE_BEFORE_CRM = r64_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR64_SPEC_PROVISION_PAST_DUE() {
		return R64_SPEC_PROVISION_PAST_DUE;
	}
	public void setR64_SPEC_PROVISION_PAST_DUE(BigDecimal r64_SPEC_PROVISION_PAST_DUE) {
		R64_SPEC_PROVISION_PAST_DUE = r64_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR64_ON_BAL_SHEET_NETTING_ELIG() {
		return R64_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR64_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r64_ON_BAL_SHEET_NETTING_ELIG) {
		R64_ON_BAL_SHEET_NETTING_ELIG = r64_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR64_TOTAL_EXPOSURE_AFTER_NET() {
		return R64_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR64_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r64_TOTAL_EXPOSURE_AFTER_NET) {
		R64_TOTAL_EXPOSURE_AFTER_NET = r64_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR64_CRM_ELIG_EXPOSURE_SUBS() {
		return R64_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR64_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r64_CRM_ELIG_EXPOSURE_SUBS) {
		R64_CRM_ELIG_EXPOSURE_SUBS = r64_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR64_ELIG_GUARANTEES() {
		return R64_ELIG_GUARANTEES;
	}
	public void setR64_ELIG_GUARANTEES(BigDecimal r64_ELIG_GUARANTEES) {
		R64_ELIG_GUARANTEES = r64_ELIG_GUARANTEES;
	}
	public BigDecimal getR64_CREDIT_DERIVATIVES() {
		return R64_CREDIT_DERIVATIVES;
	}
	public void setR64_CREDIT_DERIVATIVES(BigDecimal r64_CREDIT_DERIVATIVES) {
		R64_CREDIT_DERIVATIVES = r64_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR64_CRM_COVERED_EXPOSURE() {
		return R64_CRM_COVERED_EXPOSURE;
	}
	public void setR64_CRM_COVERED_EXPOSURE(BigDecimal r64_CRM_COVERED_EXPOSURE) {
		R64_CRM_COVERED_EXPOSURE = r64_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR64_CRM_NOT_COVERED_EXPOSURE() {
		return R64_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR64_CRM_NOT_COVERED_EXPOSURE(BigDecimal r64_CRM_NOT_COVERED_EXPOSURE) {
		R64_CRM_NOT_COVERED_EXPOSURE = r64_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR64_CRM_RISK_WEIGHT() {
		return R64_CRM_RISK_WEIGHT;
	}
	public void setR64_CRM_RISK_WEIGHT(BigDecimal r64_CRM_RISK_WEIGHT) {
		R64_CRM_RISK_WEIGHT = r64_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR64_RWA_CRM_COVERED() {
		return R64_RWA_CRM_COVERED;
	}
	public void setR64_RWA_CRM_COVERED(BigDecimal r64_RWA_CRM_COVERED) {
		R64_RWA_CRM_COVERED = r64_RWA_CRM_COVERED;
	}
	public BigDecimal getR64_ORIG_COUNTERPARTY_RW() {
		return R64_ORIG_COUNTERPARTY_RW;
	}
	public void setR64_ORIG_COUNTERPARTY_RW(BigDecimal r64_ORIG_COUNTERPARTY_RW) {
		R64_ORIG_COUNTERPARTY_RW = r64_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR64_RWA_CRM_NOT_COVERED() {
		return R64_RWA_CRM_NOT_COVERED;
	}
	public void setR64_RWA_CRM_NOT_COVERED(BigDecimal r64_RWA_CRM_NOT_COVERED) {
		R64_RWA_CRM_NOT_COVERED = r64_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR64_CRM_ELIG_EXPOSURE_COMP() {
		return R64_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR64_CRM_ELIG_EXPOSURE_COMP(BigDecimal r64_CRM_ELIG_EXPOSURE_COMP) {
		R64_CRM_ELIG_EXPOSURE_COMP = r64_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR64_EXPOSURE_AFTER_VOL_ADJ() {
		return R64_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR64_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r64_EXPOSURE_AFTER_VOL_ADJ) {
		R64_EXPOSURE_AFTER_VOL_ADJ = r64_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR64_COLLATERAL_CASH() {
		return R64_COLLATERAL_CASH;
	}
	public void setR64_COLLATERAL_CASH(BigDecimal r64_COLLATERAL_CASH) {
		R64_COLLATERAL_CASH = r64_COLLATERAL_CASH;
	}
	public BigDecimal getR64_COLLATERAL_TBILLS() {
		return R64_COLLATERAL_TBILLS;
	}
	public void setR64_COLLATERAL_TBILLS(BigDecimal r64_COLLATERAL_TBILLS) {
		R64_COLLATERAL_TBILLS = r64_COLLATERAL_TBILLS;
	}
	public BigDecimal getR64_COLLATERAL_DEBT_SEC() {
		return R64_COLLATERAL_DEBT_SEC;
	}
	public void setR64_COLLATERAL_DEBT_SEC(BigDecimal r64_COLLATERAL_DEBT_SEC) {
		R64_COLLATERAL_DEBT_SEC = r64_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR64_COLLATERAL_EQUITIES() {
		return R64_COLLATERAL_EQUITIES;
	}
	public void setR64_COLLATERAL_EQUITIES(BigDecimal r64_COLLATERAL_EQUITIES) {
		R64_COLLATERAL_EQUITIES = r64_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR64_COLLATERAL_MUTUAL_FUNDS() {
		return R64_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR64_COLLATERAL_MUTUAL_FUNDS(BigDecimal r64_COLLATERAL_MUTUAL_FUNDS) {
		R64_COLLATERAL_MUTUAL_FUNDS = r64_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR64_TOTAL_COLLATERAL_HAIRCUT() {
		return R64_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR64_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r64_TOTAL_COLLATERAL_HAIRCUT) {
		R64_TOTAL_COLLATERAL_HAIRCUT = r64_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR64_EXPOSURE_AFTER_CRM() {
		return R64_EXPOSURE_AFTER_CRM;
	}
	public void setR64_EXPOSURE_AFTER_CRM(BigDecimal r64_EXPOSURE_AFTER_CRM) {
		R64_EXPOSURE_AFTER_CRM = r64_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR64_RWA_NOT_COVERED_CRM() {
		return R64_RWA_NOT_COVERED_CRM;
	}
	public void setR64_RWA_NOT_COVERED_CRM(BigDecimal r64_RWA_NOT_COVERED_CRM) {
		R64_RWA_NOT_COVERED_CRM = r64_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR64_RWA_UNSECURED_EXPOSURE() {
		return R64_RWA_UNSECURED_EXPOSURE;
	}
	public void setR64_RWA_UNSECURED_EXPOSURE(BigDecimal r64_RWA_UNSECURED_EXPOSURE) {
		R64_RWA_UNSECURED_EXPOSURE = r64_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR64_RWA_UNSECURED() {
		return R64_RWA_UNSECURED;
	}
	public void setR64_RWA_UNSECURED(BigDecimal r64_RWA_UNSECURED) {
		R64_RWA_UNSECURED = r64_RWA_UNSECURED;
	}
	public BigDecimal getR64_TOTAL_RWA() {
		return R64_TOTAL_RWA;
	}
	public void setR64_TOTAL_RWA(BigDecimal r64_TOTAL_RWA) {
		R64_TOTAL_RWA = r64_TOTAL_RWA;
	}
	public BigDecimal getR65_EXPOSURE_BEFORE_CRM() {
		return R65_EXPOSURE_BEFORE_CRM;
	}
	public void setR65_EXPOSURE_BEFORE_CRM(BigDecimal r65_EXPOSURE_BEFORE_CRM) {
		R65_EXPOSURE_BEFORE_CRM = r65_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR65_SPEC_PROVISION_PAST_DUE() {
		return R65_SPEC_PROVISION_PAST_DUE;
	}
	public void setR65_SPEC_PROVISION_PAST_DUE(BigDecimal r65_SPEC_PROVISION_PAST_DUE) {
		R65_SPEC_PROVISION_PAST_DUE = r65_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR65_ON_BAL_SHEET_NETTING_ELIG() {
		return R65_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR65_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r65_ON_BAL_SHEET_NETTING_ELIG) {
		R65_ON_BAL_SHEET_NETTING_ELIG = r65_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR65_TOTAL_EXPOSURE_AFTER_NET() {
		return R65_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR65_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r65_TOTAL_EXPOSURE_AFTER_NET) {
		R65_TOTAL_EXPOSURE_AFTER_NET = r65_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR65_CRM_ELIG_EXPOSURE_SUBS() {
		return R65_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR65_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r65_CRM_ELIG_EXPOSURE_SUBS) {
		R65_CRM_ELIG_EXPOSURE_SUBS = r65_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR65_ELIG_GUARANTEES() {
		return R65_ELIG_GUARANTEES;
	}
	public void setR65_ELIG_GUARANTEES(BigDecimal r65_ELIG_GUARANTEES) {
		R65_ELIG_GUARANTEES = r65_ELIG_GUARANTEES;
	}
	public BigDecimal getR65_CREDIT_DERIVATIVES() {
		return R65_CREDIT_DERIVATIVES;
	}
	public void setR65_CREDIT_DERIVATIVES(BigDecimal r65_CREDIT_DERIVATIVES) {
		R65_CREDIT_DERIVATIVES = r65_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR65_CRM_COVERED_EXPOSURE() {
		return R65_CRM_COVERED_EXPOSURE;
	}
	public void setR65_CRM_COVERED_EXPOSURE(BigDecimal r65_CRM_COVERED_EXPOSURE) {
		R65_CRM_COVERED_EXPOSURE = r65_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR65_CRM_NOT_COVERED_EXPOSURE() {
		return R65_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR65_CRM_NOT_COVERED_EXPOSURE(BigDecimal r65_CRM_NOT_COVERED_EXPOSURE) {
		R65_CRM_NOT_COVERED_EXPOSURE = r65_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR65_CRM_RISK_WEIGHT() {
		return R65_CRM_RISK_WEIGHT;
	}
	public void setR65_CRM_RISK_WEIGHT(BigDecimal r65_CRM_RISK_WEIGHT) {
		R65_CRM_RISK_WEIGHT = r65_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR65_RWA_CRM_COVERED() {
		return R65_RWA_CRM_COVERED;
	}
	public void setR65_RWA_CRM_COVERED(BigDecimal r65_RWA_CRM_COVERED) {
		R65_RWA_CRM_COVERED = r65_RWA_CRM_COVERED;
	}
	public BigDecimal getR65_ORIG_COUNTERPARTY_RW() {
		return R65_ORIG_COUNTERPARTY_RW;
	}
	public void setR65_ORIG_COUNTERPARTY_RW(BigDecimal r65_ORIG_COUNTERPARTY_RW) {
		R65_ORIG_COUNTERPARTY_RW = r65_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR65_RWA_CRM_NOT_COVERED() {
		return R65_RWA_CRM_NOT_COVERED;
	}
	public void setR65_RWA_CRM_NOT_COVERED(BigDecimal r65_RWA_CRM_NOT_COVERED) {
		R65_RWA_CRM_NOT_COVERED = r65_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR65_CRM_ELIG_EXPOSURE_COMP() {
		return R65_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR65_CRM_ELIG_EXPOSURE_COMP(BigDecimal r65_CRM_ELIG_EXPOSURE_COMP) {
		R65_CRM_ELIG_EXPOSURE_COMP = r65_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR65_EXPOSURE_AFTER_VOL_ADJ() {
		return R65_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR65_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r65_EXPOSURE_AFTER_VOL_ADJ) {
		R65_EXPOSURE_AFTER_VOL_ADJ = r65_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR65_COLLATERAL_CASH() {
		return R65_COLLATERAL_CASH;
	}
	public void setR65_COLLATERAL_CASH(BigDecimal r65_COLLATERAL_CASH) {
		R65_COLLATERAL_CASH = r65_COLLATERAL_CASH;
	}
	public BigDecimal getR65_COLLATERAL_TBILLS() {
		return R65_COLLATERAL_TBILLS;
	}
	public void setR65_COLLATERAL_TBILLS(BigDecimal r65_COLLATERAL_TBILLS) {
		R65_COLLATERAL_TBILLS = r65_COLLATERAL_TBILLS;
	}
	public BigDecimal getR65_COLLATERAL_DEBT_SEC() {
		return R65_COLLATERAL_DEBT_SEC;
	}
	public void setR65_COLLATERAL_DEBT_SEC(BigDecimal r65_COLLATERAL_DEBT_SEC) {
		R65_COLLATERAL_DEBT_SEC = r65_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR65_COLLATERAL_EQUITIES() {
		return R65_COLLATERAL_EQUITIES;
	}
	public void setR65_COLLATERAL_EQUITIES(BigDecimal r65_COLLATERAL_EQUITIES) {
		R65_COLLATERAL_EQUITIES = r65_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR65_COLLATERAL_MUTUAL_FUNDS() {
		return R65_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR65_COLLATERAL_MUTUAL_FUNDS(BigDecimal r65_COLLATERAL_MUTUAL_FUNDS) {
		R65_COLLATERAL_MUTUAL_FUNDS = r65_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR65_TOTAL_COLLATERAL_HAIRCUT() {
		return R65_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR65_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r65_TOTAL_COLLATERAL_HAIRCUT) {
		R65_TOTAL_COLLATERAL_HAIRCUT = r65_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR65_EXPOSURE_AFTER_CRM() {
		return R65_EXPOSURE_AFTER_CRM;
	}
	public void setR65_EXPOSURE_AFTER_CRM(BigDecimal r65_EXPOSURE_AFTER_CRM) {
		R65_EXPOSURE_AFTER_CRM = r65_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR65_RWA_NOT_COVERED_CRM() {
		return R65_RWA_NOT_COVERED_CRM;
	}
	public void setR65_RWA_NOT_COVERED_CRM(BigDecimal r65_RWA_NOT_COVERED_CRM) {
		R65_RWA_NOT_COVERED_CRM = r65_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR65_RWA_UNSECURED_EXPOSURE() {
		return R65_RWA_UNSECURED_EXPOSURE;
	}
	public void setR65_RWA_UNSECURED_EXPOSURE(BigDecimal r65_RWA_UNSECURED_EXPOSURE) {
		R65_RWA_UNSECURED_EXPOSURE = r65_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR65_RWA_UNSECURED() {
		return R65_RWA_UNSECURED;
	}
	public void setR65_RWA_UNSECURED(BigDecimal r65_RWA_UNSECURED) {
		R65_RWA_UNSECURED = r65_RWA_UNSECURED;
	}
	public BigDecimal getR65_TOTAL_RWA() {
		return R65_TOTAL_RWA;
	}
	public void setR65_TOTAL_RWA(BigDecimal r65_TOTAL_RWA) {
		R65_TOTAL_RWA = r65_TOTAL_RWA;
	}
	public BigDecimal getR66_EXPOSURE_BEFORE_CRM() {
		return R66_EXPOSURE_BEFORE_CRM;
	}
	public void setR66_EXPOSURE_BEFORE_CRM(BigDecimal r66_EXPOSURE_BEFORE_CRM) {
		R66_EXPOSURE_BEFORE_CRM = r66_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR66_SPEC_PROVISION_PAST_DUE() {
		return R66_SPEC_PROVISION_PAST_DUE;
	}
	public void setR66_SPEC_PROVISION_PAST_DUE(BigDecimal r66_SPEC_PROVISION_PAST_DUE) {
		R66_SPEC_PROVISION_PAST_DUE = r66_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR66_ON_BAL_SHEET_NETTING_ELIG() {
		return R66_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR66_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r66_ON_BAL_SHEET_NETTING_ELIG) {
		R66_ON_BAL_SHEET_NETTING_ELIG = r66_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR66_TOTAL_EXPOSURE_AFTER_NET() {
		return R66_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR66_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r66_TOTAL_EXPOSURE_AFTER_NET) {
		R66_TOTAL_EXPOSURE_AFTER_NET = r66_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR66_CRM_ELIG_EXPOSURE_SUBS() {
		return R66_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR66_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r66_CRM_ELIG_EXPOSURE_SUBS) {
		R66_CRM_ELIG_EXPOSURE_SUBS = r66_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR66_ELIG_GUARANTEES() {
		return R66_ELIG_GUARANTEES;
	}
	public void setR66_ELIG_GUARANTEES(BigDecimal r66_ELIG_GUARANTEES) {
		R66_ELIG_GUARANTEES = r66_ELIG_GUARANTEES;
	}
	public BigDecimal getR66_CREDIT_DERIVATIVES() {
		return R66_CREDIT_DERIVATIVES;
	}
	public void setR66_CREDIT_DERIVATIVES(BigDecimal r66_CREDIT_DERIVATIVES) {
		R66_CREDIT_DERIVATIVES = r66_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR66_CRM_COVERED_EXPOSURE() {
		return R66_CRM_COVERED_EXPOSURE;
	}
	public void setR66_CRM_COVERED_EXPOSURE(BigDecimal r66_CRM_COVERED_EXPOSURE) {
		R66_CRM_COVERED_EXPOSURE = r66_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR66_CRM_NOT_COVERED_EXPOSURE() {
		return R66_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR66_CRM_NOT_COVERED_EXPOSURE(BigDecimal r66_CRM_NOT_COVERED_EXPOSURE) {
		R66_CRM_NOT_COVERED_EXPOSURE = r66_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR66_CRM_RISK_WEIGHT() {
		return R66_CRM_RISK_WEIGHT;
	}
	public void setR66_CRM_RISK_WEIGHT(BigDecimal r66_CRM_RISK_WEIGHT) {
		R66_CRM_RISK_WEIGHT = r66_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR66_RWA_CRM_COVERED() {
		return R66_RWA_CRM_COVERED;
	}
	public void setR66_RWA_CRM_COVERED(BigDecimal r66_RWA_CRM_COVERED) {
		R66_RWA_CRM_COVERED = r66_RWA_CRM_COVERED;
	}
	public BigDecimal getR66_ORIG_COUNTERPARTY_RW() {
		return R66_ORIG_COUNTERPARTY_RW;
	}
	public void setR66_ORIG_COUNTERPARTY_RW(BigDecimal r66_ORIG_COUNTERPARTY_RW) {
		R66_ORIG_COUNTERPARTY_RW = r66_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR66_RWA_CRM_NOT_COVERED() {
		return R66_RWA_CRM_NOT_COVERED;
	}
	public void setR66_RWA_CRM_NOT_COVERED(BigDecimal r66_RWA_CRM_NOT_COVERED) {
		R66_RWA_CRM_NOT_COVERED = r66_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR66_CRM_ELIG_EXPOSURE_COMP() {
		return R66_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR66_CRM_ELIG_EXPOSURE_COMP(BigDecimal r66_CRM_ELIG_EXPOSURE_COMP) {
		R66_CRM_ELIG_EXPOSURE_COMP = r66_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR66_EXPOSURE_AFTER_VOL_ADJ() {
		return R66_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR66_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r66_EXPOSURE_AFTER_VOL_ADJ) {
		R66_EXPOSURE_AFTER_VOL_ADJ = r66_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR66_COLLATERAL_CASH() {
		return R66_COLLATERAL_CASH;
	}
	public void setR66_COLLATERAL_CASH(BigDecimal r66_COLLATERAL_CASH) {
		R66_COLLATERAL_CASH = r66_COLLATERAL_CASH;
	}
	public BigDecimal getR66_COLLATERAL_TBILLS() {
		return R66_COLLATERAL_TBILLS;
	}
	public void setR66_COLLATERAL_TBILLS(BigDecimal r66_COLLATERAL_TBILLS) {
		R66_COLLATERAL_TBILLS = r66_COLLATERAL_TBILLS;
	}
	public BigDecimal getR66_COLLATERAL_DEBT_SEC() {
		return R66_COLLATERAL_DEBT_SEC;
	}
	public void setR66_COLLATERAL_DEBT_SEC(BigDecimal r66_COLLATERAL_DEBT_SEC) {
		R66_COLLATERAL_DEBT_SEC = r66_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR66_COLLATERAL_EQUITIES() {
		return R66_COLLATERAL_EQUITIES;
	}
	public void setR66_COLLATERAL_EQUITIES(BigDecimal r66_COLLATERAL_EQUITIES) {
		R66_COLLATERAL_EQUITIES = r66_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR66_COLLATERAL_MUTUAL_FUNDS() {
		return R66_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR66_COLLATERAL_MUTUAL_FUNDS(BigDecimal r66_COLLATERAL_MUTUAL_FUNDS) {
		R66_COLLATERAL_MUTUAL_FUNDS = r66_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR66_TOTAL_COLLATERAL_HAIRCUT() {
		return R66_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR66_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r66_TOTAL_COLLATERAL_HAIRCUT) {
		R66_TOTAL_COLLATERAL_HAIRCUT = r66_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR66_EXPOSURE_AFTER_CRM() {
		return R66_EXPOSURE_AFTER_CRM;
	}
	public void setR66_EXPOSURE_AFTER_CRM(BigDecimal r66_EXPOSURE_AFTER_CRM) {
		R66_EXPOSURE_AFTER_CRM = r66_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR66_RWA_NOT_COVERED_CRM() {
		return R66_RWA_NOT_COVERED_CRM;
	}
	public void setR66_RWA_NOT_COVERED_CRM(BigDecimal r66_RWA_NOT_COVERED_CRM) {
		R66_RWA_NOT_COVERED_CRM = r66_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR66_RWA_UNSECURED_EXPOSURE() {
		return R66_RWA_UNSECURED_EXPOSURE;
	}
	public void setR66_RWA_UNSECURED_EXPOSURE(BigDecimal r66_RWA_UNSECURED_EXPOSURE) {
		R66_RWA_UNSECURED_EXPOSURE = r66_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR66_RWA_UNSECURED() {
		return R66_RWA_UNSECURED;
	}
	public void setR66_RWA_UNSECURED(BigDecimal r66_RWA_UNSECURED) {
		R66_RWA_UNSECURED = r66_RWA_UNSECURED;
	}
	public BigDecimal getR66_TOTAL_RWA() {
		return R66_TOTAL_RWA;
	}
	public void setR66_TOTAL_RWA(BigDecimal r66_TOTAL_RWA) {
		R66_TOTAL_RWA = r66_TOTAL_RWA;
	}
	public BigDecimal getR67_EXPOSURE_BEFORE_CRM() {
		return R67_EXPOSURE_BEFORE_CRM;
	}
	public void setR67_EXPOSURE_BEFORE_CRM(BigDecimal r67_EXPOSURE_BEFORE_CRM) {
		R67_EXPOSURE_BEFORE_CRM = r67_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR67_SPEC_PROVISION_PAST_DUE() {
		return R67_SPEC_PROVISION_PAST_DUE;
	}
	public void setR67_SPEC_PROVISION_PAST_DUE(BigDecimal r67_SPEC_PROVISION_PAST_DUE) {
		R67_SPEC_PROVISION_PAST_DUE = r67_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR67_ON_BAL_SHEET_NETTING_ELIG() {
		return R67_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR67_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r67_ON_BAL_SHEET_NETTING_ELIG) {
		R67_ON_BAL_SHEET_NETTING_ELIG = r67_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR67_TOTAL_EXPOSURE_AFTER_NET() {
		return R67_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR67_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r67_TOTAL_EXPOSURE_AFTER_NET) {
		R67_TOTAL_EXPOSURE_AFTER_NET = r67_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR67_CRM_ELIG_EXPOSURE_SUBS() {
		return R67_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR67_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r67_CRM_ELIG_EXPOSURE_SUBS) {
		R67_CRM_ELIG_EXPOSURE_SUBS = r67_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR67_ELIG_GUARANTEES() {
		return R67_ELIG_GUARANTEES;
	}
	public void setR67_ELIG_GUARANTEES(BigDecimal r67_ELIG_GUARANTEES) {
		R67_ELIG_GUARANTEES = r67_ELIG_GUARANTEES;
	}
	public BigDecimal getR67_CREDIT_DERIVATIVES() {
		return R67_CREDIT_DERIVATIVES;
	}
	public void setR67_CREDIT_DERIVATIVES(BigDecimal r67_CREDIT_DERIVATIVES) {
		R67_CREDIT_DERIVATIVES = r67_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR67_CRM_COVERED_EXPOSURE() {
		return R67_CRM_COVERED_EXPOSURE;
	}
	public void setR67_CRM_COVERED_EXPOSURE(BigDecimal r67_CRM_COVERED_EXPOSURE) {
		R67_CRM_COVERED_EXPOSURE = r67_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR67_CRM_NOT_COVERED_EXPOSURE() {
		return R67_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR67_CRM_NOT_COVERED_EXPOSURE(BigDecimal r67_CRM_NOT_COVERED_EXPOSURE) {
		R67_CRM_NOT_COVERED_EXPOSURE = r67_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR67_CRM_RISK_WEIGHT() {
		return R67_CRM_RISK_WEIGHT;
	}
	public void setR67_CRM_RISK_WEIGHT(BigDecimal r67_CRM_RISK_WEIGHT) {
		R67_CRM_RISK_WEIGHT = r67_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR67_RWA_CRM_COVERED() {
		return R67_RWA_CRM_COVERED;
	}
	public void setR67_RWA_CRM_COVERED(BigDecimal r67_RWA_CRM_COVERED) {
		R67_RWA_CRM_COVERED = r67_RWA_CRM_COVERED;
	}
	public BigDecimal getR67_ORIG_COUNTERPARTY_RW() {
		return R67_ORIG_COUNTERPARTY_RW;
	}
	public void setR67_ORIG_COUNTERPARTY_RW(BigDecimal r67_ORIG_COUNTERPARTY_RW) {
		R67_ORIG_COUNTERPARTY_RW = r67_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR67_RWA_CRM_NOT_COVERED() {
		return R67_RWA_CRM_NOT_COVERED;
	}
	public void setR67_RWA_CRM_NOT_COVERED(BigDecimal r67_RWA_CRM_NOT_COVERED) {
		R67_RWA_CRM_NOT_COVERED = r67_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR67_CRM_ELIG_EXPOSURE_COMP() {
		return R67_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR67_CRM_ELIG_EXPOSURE_COMP(BigDecimal r67_CRM_ELIG_EXPOSURE_COMP) {
		R67_CRM_ELIG_EXPOSURE_COMP = r67_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR67_EXPOSURE_AFTER_VOL_ADJ() {
		return R67_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR67_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r67_EXPOSURE_AFTER_VOL_ADJ) {
		R67_EXPOSURE_AFTER_VOL_ADJ = r67_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR67_COLLATERAL_CASH() {
		return R67_COLLATERAL_CASH;
	}
	public void setR67_COLLATERAL_CASH(BigDecimal r67_COLLATERAL_CASH) {
		R67_COLLATERAL_CASH = r67_COLLATERAL_CASH;
	}
	public BigDecimal getR67_COLLATERAL_TBILLS() {
		return R67_COLLATERAL_TBILLS;
	}
	public void setR67_COLLATERAL_TBILLS(BigDecimal r67_COLLATERAL_TBILLS) {
		R67_COLLATERAL_TBILLS = r67_COLLATERAL_TBILLS;
	}
	public BigDecimal getR67_COLLATERAL_DEBT_SEC() {
		return R67_COLLATERAL_DEBT_SEC;
	}
	public void setR67_COLLATERAL_DEBT_SEC(BigDecimal r67_COLLATERAL_DEBT_SEC) {
		R67_COLLATERAL_DEBT_SEC = r67_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR67_COLLATERAL_EQUITIES() {
		return R67_COLLATERAL_EQUITIES;
	}
	public void setR67_COLLATERAL_EQUITIES(BigDecimal r67_COLLATERAL_EQUITIES) {
		R67_COLLATERAL_EQUITIES = r67_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR67_COLLATERAL_MUTUAL_FUNDS() {
		return R67_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR67_COLLATERAL_MUTUAL_FUNDS(BigDecimal r67_COLLATERAL_MUTUAL_FUNDS) {
		R67_COLLATERAL_MUTUAL_FUNDS = r67_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR67_TOTAL_COLLATERAL_HAIRCUT() {
		return R67_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR67_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r67_TOTAL_COLLATERAL_HAIRCUT) {
		R67_TOTAL_COLLATERAL_HAIRCUT = r67_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR67_EXPOSURE_AFTER_CRM() {
		return R67_EXPOSURE_AFTER_CRM;
	}
	public void setR67_EXPOSURE_AFTER_CRM(BigDecimal r67_EXPOSURE_AFTER_CRM) {
		R67_EXPOSURE_AFTER_CRM = r67_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR67_RWA_NOT_COVERED_CRM() {
		return R67_RWA_NOT_COVERED_CRM;
	}
	public void setR67_RWA_NOT_COVERED_CRM(BigDecimal r67_RWA_NOT_COVERED_CRM) {
		R67_RWA_NOT_COVERED_CRM = r67_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR67_RWA_UNSECURED_EXPOSURE() {
		return R67_RWA_UNSECURED_EXPOSURE;
	}
	public void setR67_RWA_UNSECURED_EXPOSURE(BigDecimal r67_RWA_UNSECURED_EXPOSURE) {
		R67_RWA_UNSECURED_EXPOSURE = r67_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR67_RWA_UNSECURED() {
		return R67_RWA_UNSECURED;
	}
	public void setR67_RWA_UNSECURED(BigDecimal r67_RWA_UNSECURED) {
		R67_RWA_UNSECURED = r67_RWA_UNSECURED;
	}
	public BigDecimal getR67_TOTAL_RWA() {
		return R67_TOTAL_RWA;
	}
	public void setR67_TOTAL_RWA(BigDecimal r67_TOTAL_RWA) {
		R67_TOTAL_RWA = r67_TOTAL_RWA;
	}
	public BigDecimal getR68_EXPOSURE_BEFORE_CRM() {
		return R68_EXPOSURE_BEFORE_CRM;
	}
	public void setR68_EXPOSURE_BEFORE_CRM(BigDecimal r68_EXPOSURE_BEFORE_CRM) {
		R68_EXPOSURE_BEFORE_CRM = r68_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR68_SPEC_PROVISION_PAST_DUE() {
		return R68_SPEC_PROVISION_PAST_DUE;
	}
	public void setR68_SPEC_PROVISION_PAST_DUE(BigDecimal r68_SPEC_PROVISION_PAST_DUE) {
		R68_SPEC_PROVISION_PAST_DUE = r68_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR68_ON_BAL_SHEET_NETTING_ELIG() {
		return R68_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR68_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r68_ON_BAL_SHEET_NETTING_ELIG) {
		R68_ON_BAL_SHEET_NETTING_ELIG = r68_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR68_TOTAL_EXPOSURE_AFTER_NET() {
		return R68_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR68_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r68_TOTAL_EXPOSURE_AFTER_NET) {
		R68_TOTAL_EXPOSURE_AFTER_NET = r68_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR68_CRM_ELIG_EXPOSURE_SUBS() {
		return R68_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR68_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r68_CRM_ELIG_EXPOSURE_SUBS) {
		R68_CRM_ELIG_EXPOSURE_SUBS = r68_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR68_ELIG_GUARANTEES() {
		return R68_ELIG_GUARANTEES;
	}
	public void setR68_ELIG_GUARANTEES(BigDecimal r68_ELIG_GUARANTEES) {
		R68_ELIG_GUARANTEES = r68_ELIG_GUARANTEES;
	}
	public BigDecimal getR68_CREDIT_DERIVATIVES() {
		return R68_CREDIT_DERIVATIVES;
	}
	public void setR68_CREDIT_DERIVATIVES(BigDecimal r68_CREDIT_DERIVATIVES) {
		R68_CREDIT_DERIVATIVES = r68_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR68_CRM_COVERED_EXPOSURE() {
		return R68_CRM_COVERED_EXPOSURE;
	}
	public void setR68_CRM_COVERED_EXPOSURE(BigDecimal r68_CRM_COVERED_EXPOSURE) {
		R68_CRM_COVERED_EXPOSURE = r68_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR68_CRM_NOT_COVERED_EXPOSURE() {
		return R68_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR68_CRM_NOT_COVERED_EXPOSURE(BigDecimal r68_CRM_NOT_COVERED_EXPOSURE) {
		R68_CRM_NOT_COVERED_EXPOSURE = r68_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR68_CRM_RISK_WEIGHT() {
		return R68_CRM_RISK_WEIGHT;
	}
	public void setR68_CRM_RISK_WEIGHT(BigDecimal r68_CRM_RISK_WEIGHT) {
		R68_CRM_RISK_WEIGHT = r68_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR68_RWA_CRM_COVERED() {
		return R68_RWA_CRM_COVERED;
	}
	public void setR68_RWA_CRM_COVERED(BigDecimal r68_RWA_CRM_COVERED) {
		R68_RWA_CRM_COVERED = r68_RWA_CRM_COVERED;
	}
	public BigDecimal getR68_ORIG_COUNTERPARTY_RW() {
		return R68_ORIG_COUNTERPARTY_RW;
	}
	public void setR68_ORIG_COUNTERPARTY_RW(BigDecimal r68_ORIG_COUNTERPARTY_RW) {
		R68_ORIG_COUNTERPARTY_RW = r68_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR68_RWA_CRM_NOT_COVERED() {
		return R68_RWA_CRM_NOT_COVERED;
	}
	public void setR68_RWA_CRM_NOT_COVERED(BigDecimal r68_RWA_CRM_NOT_COVERED) {
		R68_RWA_CRM_NOT_COVERED = r68_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR68_CRM_ELIG_EXPOSURE_COMP() {
		return R68_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR68_CRM_ELIG_EXPOSURE_COMP(BigDecimal r68_CRM_ELIG_EXPOSURE_COMP) {
		R68_CRM_ELIG_EXPOSURE_COMP = r68_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR68_EXPOSURE_AFTER_VOL_ADJ() {
		return R68_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR68_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r68_EXPOSURE_AFTER_VOL_ADJ) {
		R68_EXPOSURE_AFTER_VOL_ADJ = r68_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR68_COLLATERAL_CASH() {
		return R68_COLLATERAL_CASH;
	}
	public void setR68_COLLATERAL_CASH(BigDecimal r68_COLLATERAL_CASH) {
		R68_COLLATERAL_CASH = r68_COLLATERAL_CASH;
	}
	public BigDecimal getR68_COLLATERAL_TBILLS() {
		return R68_COLLATERAL_TBILLS;
	}
	public void setR68_COLLATERAL_TBILLS(BigDecimal r68_COLLATERAL_TBILLS) {
		R68_COLLATERAL_TBILLS = r68_COLLATERAL_TBILLS;
	}
	public BigDecimal getR68_COLLATERAL_DEBT_SEC() {
		return R68_COLLATERAL_DEBT_SEC;
	}
	public void setR68_COLLATERAL_DEBT_SEC(BigDecimal r68_COLLATERAL_DEBT_SEC) {
		R68_COLLATERAL_DEBT_SEC = r68_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR68_COLLATERAL_EQUITIES() {
		return R68_COLLATERAL_EQUITIES;
	}
	public void setR68_COLLATERAL_EQUITIES(BigDecimal r68_COLLATERAL_EQUITIES) {
		R68_COLLATERAL_EQUITIES = r68_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR68_COLLATERAL_MUTUAL_FUNDS() {
		return R68_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR68_COLLATERAL_MUTUAL_FUNDS(BigDecimal r68_COLLATERAL_MUTUAL_FUNDS) {
		R68_COLLATERAL_MUTUAL_FUNDS = r68_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR68_TOTAL_COLLATERAL_HAIRCUT() {
		return R68_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR68_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r68_TOTAL_COLLATERAL_HAIRCUT) {
		R68_TOTAL_COLLATERAL_HAIRCUT = r68_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR68_EXPOSURE_AFTER_CRM() {
		return R68_EXPOSURE_AFTER_CRM;
	}
	public void setR68_EXPOSURE_AFTER_CRM(BigDecimal r68_EXPOSURE_AFTER_CRM) {
		R68_EXPOSURE_AFTER_CRM = r68_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR68_RWA_NOT_COVERED_CRM() {
		return R68_RWA_NOT_COVERED_CRM;
	}
	public void setR68_RWA_NOT_COVERED_CRM(BigDecimal r68_RWA_NOT_COVERED_CRM) {
		R68_RWA_NOT_COVERED_CRM = r68_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR68_RWA_UNSECURED_EXPOSURE() {
		return R68_RWA_UNSECURED_EXPOSURE;
	}
	public void setR68_RWA_UNSECURED_EXPOSURE(BigDecimal r68_RWA_UNSECURED_EXPOSURE) {
		R68_RWA_UNSECURED_EXPOSURE = r68_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR68_RWA_UNSECURED() {
		return R68_RWA_UNSECURED;
	}
	public void setR68_RWA_UNSECURED(BigDecimal r68_RWA_UNSECURED) {
		R68_RWA_UNSECURED = r68_RWA_UNSECURED;
	}
	public BigDecimal getR68_TOTAL_RWA() {
		return R68_TOTAL_RWA;
	}
	public void setR68_TOTAL_RWA(BigDecimal r68_TOTAL_RWA) {
		R68_TOTAL_RWA = r68_TOTAL_RWA;
	}
	public BigDecimal getR69_EXPOSURE_BEFORE_CRM() {
		return R69_EXPOSURE_BEFORE_CRM;
	}
	public void setR69_EXPOSURE_BEFORE_CRM(BigDecimal r69_EXPOSURE_BEFORE_CRM) {
		R69_EXPOSURE_BEFORE_CRM = r69_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR69_SPEC_PROVISION_PAST_DUE() {
		return R69_SPEC_PROVISION_PAST_DUE;
	}
	public void setR69_SPEC_PROVISION_PAST_DUE(BigDecimal r69_SPEC_PROVISION_PAST_DUE) {
		R69_SPEC_PROVISION_PAST_DUE = r69_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR69_ON_BAL_SHEET_NETTING_ELIG() {
		return R69_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR69_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r69_ON_BAL_SHEET_NETTING_ELIG) {
		R69_ON_BAL_SHEET_NETTING_ELIG = r69_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR69_TOTAL_EXPOSURE_AFTER_NET() {
		return R69_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR69_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r69_TOTAL_EXPOSURE_AFTER_NET) {
		R69_TOTAL_EXPOSURE_AFTER_NET = r69_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR69_CRM_ELIG_EXPOSURE_SUBS() {
		return R69_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR69_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r69_CRM_ELIG_EXPOSURE_SUBS) {
		R69_CRM_ELIG_EXPOSURE_SUBS = r69_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR69_ELIG_GUARANTEES() {
		return R69_ELIG_GUARANTEES;
	}
	public void setR69_ELIG_GUARANTEES(BigDecimal r69_ELIG_GUARANTEES) {
		R69_ELIG_GUARANTEES = r69_ELIG_GUARANTEES;
	}
	public BigDecimal getR69_CREDIT_DERIVATIVES() {
		return R69_CREDIT_DERIVATIVES;
	}
	public void setR69_CREDIT_DERIVATIVES(BigDecimal r69_CREDIT_DERIVATIVES) {
		R69_CREDIT_DERIVATIVES = r69_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR69_CRM_COVERED_EXPOSURE() {
		return R69_CRM_COVERED_EXPOSURE;
	}
	public void setR69_CRM_COVERED_EXPOSURE(BigDecimal r69_CRM_COVERED_EXPOSURE) {
		R69_CRM_COVERED_EXPOSURE = r69_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR69_CRM_NOT_COVERED_EXPOSURE() {
		return R69_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR69_CRM_NOT_COVERED_EXPOSURE(BigDecimal r69_CRM_NOT_COVERED_EXPOSURE) {
		R69_CRM_NOT_COVERED_EXPOSURE = r69_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR69_CRM_RISK_WEIGHT() {
		return R69_CRM_RISK_WEIGHT;
	}
	public void setR69_CRM_RISK_WEIGHT(BigDecimal r69_CRM_RISK_WEIGHT) {
		R69_CRM_RISK_WEIGHT = r69_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR69_RWA_CRM_COVERED() {
		return R69_RWA_CRM_COVERED;
	}
	public void setR69_RWA_CRM_COVERED(BigDecimal r69_RWA_CRM_COVERED) {
		R69_RWA_CRM_COVERED = r69_RWA_CRM_COVERED;
	}
	public BigDecimal getR69_ORIG_COUNTERPARTY_RW() {
		return R69_ORIG_COUNTERPARTY_RW;
	}
	public void setR69_ORIG_COUNTERPARTY_RW(BigDecimal r69_ORIG_COUNTERPARTY_RW) {
		R69_ORIG_COUNTERPARTY_RW = r69_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR69_RWA_CRM_NOT_COVERED() {
		return R69_RWA_CRM_NOT_COVERED;
	}
	public void setR69_RWA_CRM_NOT_COVERED(BigDecimal r69_RWA_CRM_NOT_COVERED) {
		R69_RWA_CRM_NOT_COVERED = r69_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR69_CRM_ELIG_EXPOSURE_COMP() {
		return R69_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR69_CRM_ELIG_EXPOSURE_COMP(BigDecimal r69_CRM_ELIG_EXPOSURE_COMP) {
		R69_CRM_ELIG_EXPOSURE_COMP = r69_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR69_EXPOSURE_AFTER_VOL_ADJ() {
		return R69_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR69_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r69_EXPOSURE_AFTER_VOL_ADJ) {
		R69_EXPOSURE_AFTER_VOL_ADJ = r69_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR69_COLLATERAL_CASH() {
		return R69_COLLATERAL_CASH;
	}
	public void setR69_COLLATERAL_CASH(BigDecimal r69_COLLATERAL_CASH) {
		R69_COLLATERAL_CASH = r69_COLLATERAL_CASH;
	}
	public BigDecimal getR69_COLLATERAL_TBILLS() {
		return R69_COLLATERAL_TBILLS;
	}
	public void setR69_COLLATERAL_TBILLS(BigDecimal r69_COLLATERAL_TBILLS) {
		R69_COLLATERAL_TBILLS = r69_COLLATERAL_TBILLS;
	}
	public BigDecimal getR69_COLLATERAL_DEBT_SEC() {
		return R69_COLLATERAL_DEBT_SEC;
	}
	public void setR69_COLLATERAL_DEBT_SEC(BigDecimal r69_COLLATERAL_DEBT_SEC) {
		R69_COLLATERAL_DEBT_SEC = r69_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR69_COLLATERAL_EQUITIES() {
		return R69_COLLATERAL_EQUITIES;
	}
	public void setR69_COLLATERAL_EQUITIES(BigDecimal r69_COLLATERAL_EQUITIES) {
		R69_COLLATERAL_EQUITIES = r69_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR69_COLLATERAL_MUTUAL_FUNDS() {
		return R69_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR69_COLLATERAL_MUTUAL_FUNDS(BigDecimal r69_COLLATERAL_MUTUAL_FUNDS) {
		R69_COLLATERAL_MUTUAL_FUNDS = r69_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR69_TOTAL_COLLATERAL_HAIRCUT() {
		return R69_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR69_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r69_TOTAL_COLLATERAL_HAIRCUT) {
		R69_TOTAL_COLLATERAL_HAIRCUT = r69_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR69_EXPOSURE_AFTER_CRM() {
		return R69_EXPOSURE_AFTER_CRM;
	}
	public void setR69_EXPOSURE_AFTER_CRM(BigDecimal r69_EXPOSURE_AFTER_CRM) {
		R69_EXPOSURE_AFTER_CRM = r69_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR69_RWA_NOT_COVERED_CRM() {
		return R69_RWA_NOT_COVERED_CRM;
	}
	public void setR69_RWA_NOT_COVERED_CRM(BigDecimal r69_RWA_NOT_COVERED_CRM) {
		R69_RWA_NOT_COVERED_CRM = r69_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR69_RWA_UNSECURED_EXPOSURE() {
		return R69_RWA_UNSECURED_EXPOSURE;
	}
	public void setR69_RWA_UNSECURED_EXPOSURE(BigDecimal r69_RWA_UNSECURED_EXPOSURE) {
		R69_RWA_UNSECURED_EXPOSURE = r69_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR69_RWA_UNSECURED() {
		return R69_RWA_UNSECURED;
	}
	public void setR69_RWA_UNSECURED(BigDecimal r69_RWA_UNSECURED) {
		R69_RWA_UNSECURED = r69_RWA_UNSECURED;
	}
	public BigDecimal getR69_TOTAL_RWA() {
		return R69_TOTAL_RWA;
	}
	public void setR69_TOTAL_RWA(BigDecimal r69_TOTAL_RWA) {
		R69_TOTAL_RWA = r69_TOTAL_RWA;
	}
	public M_SRWA_12B_SUMMARY_2_NEW_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
