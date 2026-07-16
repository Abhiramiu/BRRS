package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="BRRS_M_SRWA_12B_SUMMARY_TABLE_3_NEW")
public class M_SRWA_12B_SUMMARY_3_NEW_ENTITY {
	
	@Id
	private Date REPORT_DATE;
	private String REPORT_VERSION;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	private BigDecimal R70_EXPOSURE_BEFORE_CRM;
	private BigDecimal R70_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R70_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R70_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R70_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R70_ELIG_GUARANTEES;
	private BigDecimal R70_CREDIT_DERIVATIVES;
	private BigDecimal R70_CRM_COVERED_EXPOSURE;
	private BigDecimal R70_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R70_CRM_RISK_WEIGHT;
	private BigDecimal R70_RWA_CRM_COVERED;
	private BigDecimal R70_ORIG_COUNTERPARTY_RW;
	private BigDecimal R70_RWA_CRM_NOT_COVERED;
	private BigDecimal R70_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R70_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R70_COLLATERAL_CASH;
	private BigDecimal R70_COLLATERAL_TBILLS;
	private BigDecimal R70_COLLATERAL_DEBT_SEC;
	private BigDecimal R70_COLLATERAL_EQUITIES;
	private BigDecimal R70_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R70_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R70_EXPOSURE_AFTER_CRM;
	private BigDecimal R70_RWA_NOT_COVERED_CRM;
	private BigDecimal R70_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R70_RWA_UNSECURED;
	private BigDecimal R70_TOTAL_RWA;
	private BigDecimal R71_EXPOSURE_BEFORE_CRM;
	private BigDecimal R71_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R71_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R71_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R71_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R71_ELIG_GUARANTEES;
	private BigDecimal R71_CREDIT_DERIVATIVES;
	private BigDecimal R71_CRM_COVERED_EXPOSURE;
	private BigDecimal R71_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R71_CRM_RISK_WEIGHT;
	private BigDecimal R71_RWA_CRM_COVERED;
	private BigDecimal R71_ORIG_COUNTERPARTY_RW;
	private BigDecimal R71_RWA_CRM_NOT_COVERED;
	private BigDecimal R71_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R71_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R71_COLLATERAL_CASH;
	private BigDecimal R71_COLLATERAL_TBILLS;
	private BigDecimal R71_COLLATERAL_DEBT_SEC;
	private BigDecimal R71_COLLATERAL_EQUITIES;
	private BigDecimal R71_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R71_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R71_EXPOSURE_AFTER_CRM;
	private BigDecimal R71_RWA_NOT_COVERED_CRM;
	private BigDecimal R71_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R71_RWA_UNSECURED;
	private BigDecimal R71_TOTAL_RWA;
	private BigDecimal R72_EXPOSURE_BEFORE_CRM;
	private BigDecimal R72_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R72_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R72_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R72_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R72_ELIG_GUARANTEES;
	private BigDecimal R72_CREDIT_DERIVATIVES;
	private BigDecimal R72_CRM_COVERED_EXPOSURE;
	private BigDecimal R72_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R72_CRM_RISK_WEIGHT;
	private BigDecimal R72_RWA_CRM_COVERED;
	private BigDecimal R72_ORIG_COUNTERPARTY_RW;
	private BigDecimal R72_RWA_CRM_NOT_COVERED;
	private BigDecimal R72_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R72_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R72_COLLATERAL_CASH;
	private BigDecimal R72_COLLATERAL_TBILLS;
	private BigDecimal R72_COLLATERAL_DEBT_SEC;
	private BigDecimal R72_COLLATERAL_EQUITIES;
	private BigDecimal R72_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R72_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R72_EXPOSURE_AFTER_CRM;
	private BigDecimal R72_RWA_NOT_COVERED_CRM;
	private BigDecimal R72_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R72_RWA_UNSECURED;
	private BigDecimal R72_TOTAL_RWA;
	private BigDecimal R73_EXPOSURE_BEFORE_CRM;
	private BigDecimal R73_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R73_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R73_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R73_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R73_ELIG_GUARANTEES;
	private BigDecimal R73_CREDIT_DERIVATIVES;
	private BigDecimal R73_CRM_COVERED_EXPOSURE;
	private BigDecimal R73_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R73_CRM_RISK_WEIGHT;
	private BigDecimal R73_RWA_CRM_COVERED;
	private BigDecimal R73_ORIG_COUNTERPARTY_RW;
	private BigDecimal R73_RWA_CRM_NOT_COVERED;
	private BigDecimal R73_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R73_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R73_COLLATERAL_CASH;
	private BigDecimal R73_COLLATERAL_TBILLS;
	private BigDecimal R73_COLLATERAL_DEBT_SEC;
	private BigDecimal R73_COLLATERAL_EQUITIES;
	private BigDecimal R73_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R73_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R73_EXPOSURE_AFTER_CRM;
	private BigDecimal R73_RWA_NOT_COVERED_CRM;
	private BigDecimal R73_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R73_RWA_UNSECURED;
	private BigDecimal R73_TOTAL_RWA;
	private BigDecimal R74_EXPOSURE_BEFORE_CRM;
	private BigDecimal R74_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R74_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R74_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R74_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R74_ELIG_GUARANTEES;
	private BigDecimal R74_CREDIT_DERIVATIVES;
	private BigDecimal R74_CRM_COVERED_EXPOSURE;
	private BigDecimal R74_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R74_CRM_RISK_WEIGHT;
	private BigDecimal R74_RWA_CRM_COVERED;
	private BigDecimal R74_ORIG_COUNTERPARTY_RW;
	private BigDecimal R74_RWA_CRM_NOT_COVERED;
	private BigDecimal R74_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R74_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R74_COLLATERAL_CASH;
	private BigDecimal R74_COLLATERAL_TBILLS;
	private BigDecimal R74_COLLATERAL_DEBT_SEC;
	private BigDecimal R74_COLLATERAL_EQUITIES;
	private BigDecimal R74_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R74_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R74_EXPOSURE_AFTER_CRM;
	private BigDecimal R74_RWA_NOT_COVERED_CRM;
	private BigDecimal R74_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R74_RWA_UNSECURED;
	private BigDecimal R74_TOTAL_RWA;
	private BigDecimal R75_EXPOSURE_BEFORE_CRM;
	private BigDecimal R75_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R75_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R75_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R75_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R75_ELIG_GUARANTEES;
	private BigDecimal R75_CREDIT_DERIVATIVES;
	private BigDecimal R75_CRM_COVERED_EXPOSURE;
	private BigDecimal R75_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R75_CRM_RISK_WEIGHT;
	private BigDecimal R75_RWA_CRM_COVERED;
	private BigDecimal R75_ORIG_COUNTERPARTY_RW;
	private BigDecimal R75_RWA_CRM_NOT_COVERED;
	private BigDecimal R75_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R75_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R75_COLLATERAL_CASH;
	private BigDecimal R75_COLLATERAL_TBILLS;
	private BigDecimal R75_COLLATERAL_DEBT_SEC;
	private BigDecimal R75_COLLATERAL_EQUITIES;
	private BigDecimal R75_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R75_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R75_EXPOSURE_AFTER_CRM;
	private BigDecimal R75_RWA_NOT_COVERED_CRM;
	private BigDecimal R75_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R75_RWA_UNSECURED;
	private BigDecimal R75_TOTAL_RWA;
	private BigDecimal R76_EXPOSURE_BEFORE_CRM;
	private BigDecimal R76_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R76_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R76_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R76_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R76_ELIG_GUARANTEES;
	private BigDecimal R76_CREDIT_DERIVATIVES;
	private BigDecimal R76_CRM_COVERED_EXPOSURE;
	private BigDecimal R76_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R76_CRM_RISK_WEIGHT;
	private BigDecimal R76_RWA_CRM_COVERED;
	private BigDecimal R76_ORIG_COUNTERPARTY_RW;
	private BigDecimal R76_RWA_CRM_NOT_COVERED;
	private BigDecimal R76_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R76_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R76_COLLATERAL_CASH;
	private BigDecimal R76_COLLATERAL_TBILLS;
	private BigDecimal R76_COLLATERAL_DEBT_SEC;
	private BigDecimal R76_COLLATERAL_EQUITIES;
	private BigDecimal R76_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R76_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R76_EXPOSURE_AFTER_CRM;
	private BigDecimal R76_RWA_NOT_COVERED_CRM;
	private BigDecimal R76_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R76_RWA_UNSECURED;
	private BigDecimal R76_TOTAL_RWA;
	private BigDecimal R77_EXPOSURE_BEFORE_CRM;
	private BigDecimal R77_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R77_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R77_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R77_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R77_ELIG_GUARANTEES;
	private BigDecimal R77_CREDIT_DERIVATIVES;
	private BigDecimal R77_CRM_COVERED_EXPOSURE;
	private BigDecimal R77_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R77_CRM_RISK_WEIGHT;
	private BigDecimal R77_RWA_CRM_COVERED;
	private BigDecimal R77_ORIG_COUNTERPARTY_RW;
	private BigDecimal R77_RWA_CRM_NOT_COVERED;
	private BigDecimal R77_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R77_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R77_COLLATERAL_CASH;
	private BigDecimal R77_COLLATERAL_TBILLS;
	private BigDecimal R77_COLLATERAL_DEBT_SEC;
	private BigDecimal R77_COLLATERAL_EQUITIES;
	private BigDecimal R77_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R77_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R77_EXPOSURE_AFTER_CRM;
	private BigDecimal R77_RWA_NOT_COVERED_CRM;
	private BigDecimal R77_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R77_RWA_UNSECURED;
	private BigDecimal R77_TOTAL_RWA;
	private BigDecimal R78_EXPOSURE_BEFORE_CRM;
	private BigDecimal R78_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R78_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R78_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R78_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R78_ELIG_GUARANTEES;
	private BigDecimal R78_CREDIT_DERIVATIVES;
	private BigDecimal R78_CRM_COVERED_EXPOSURE;
	private BigDecimal R78_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R78_CRM_RISK_WEIGHT;
	private BigDecimal R78_RWA_CRM_COVERED;
	private BigDecimal R78_ORIG_COUNTERPARTY_RW;
	private BigDecimal R78_RWA_CRM_NOT_COVERED;
	private BigDecimal R78_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R78_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R78_COLLATERAL_CASH;
	private BigDecimal R78_COLLATERAL_TBILLS;
	private BigDecimal R78_COLLATERAL_DEBT_SEC;
	private BigDecimal R78_COLLATERAL_EQUITIES;
	private BigDecimal R78_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R78_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R78_EXPOSURE_AFTER_CRM;
	private BigDecimal R78_RWA_NOT_COVERED_CRM;
	private BigDecimal R78_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R78_RWA_UNSECURED;
	private BigDecimal R78_TOTAL_RWA;
	private BigDecimal R79_EXPOSURE_BEFORE_CRM;
	private BigDecimal R79_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R79_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R79_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R79_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R79_ELIG_GUARANTEES;
	private BigDecimal R79_CREDIT_DERIVATIVES;
	private BigDecimal R79_CRM_COVERED_EXPOSURE;
	private BigDecimal R79_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R79_CRM_RISK_WEIGHT;
	private BigDecimal R79_RWA_CRM_COVERED;
	private BigDecimal R79_ORIG_COUNTERPARTY_RW;
	private BigDecimal R79_RWA_CRM_NOT_COVERED;
	private BigDecimal R79_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R79_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R79_COLLATERAL_CASH;
	private BigDecimal R79_COLLATERAL_TBILLS;
	private BigDecimal R79_COLLATERAL_DEBT_SEC;
	private BigDecimal R79_COLLATERAL_EQUITIES;
	private BigDecimal R79_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R79_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R79_EXPOSURE_AFTER_CRM;
	private BigDecimal R79_RWA_NOT_COVERED_CRM;
	private BigDecimal R79_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R79_RWA_UNSECURED;
	private BigDecimal R79_TOTAL_RWA;
	private BigDecimal R80_EXPOSURE_BEFORE_CRM;
	private BigDecimal R80_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R80_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R80_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R80_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R80_ELIG_GUARANTEES;
	private BigDecimal R80_CREDIT_DERIVATIVES;
	private BigDecimal R80_CRM_COVERED_EXPOSURE;
	private BigDecimal R80_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R80_CRM_RISK_WEIGHT;
	private BigDecimal R80_RWA_CRM_COVERED;
	private BigDecimal R80_ORIG_COUNTERPARTY_RW;
	private BigDecimal R80_RWA_CRM_NOT_COVERED;
	private BigDecimal R80_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R80_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R80_COLLATERAL_CASH;
	private BigDecimal R80_COLLATERAL_TBILLS;
	private BigDecimal R80_COLLATERAL_DEBT_SEC;
	private BigDecimal R80_COLLATERAL_EQUITIES;
	private BigDecimal R80_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R80_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R80_EXPOSURE_AFTER_CRM;
	private BigDecimal R80_RWA_NOT_COVERED_CRM;
	private BigDecimal R80_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R80_RWA_UNSECURED;
	private BigDecimal R80_TOTAL_RWA;
	private BigDecimal R81_EXPOSURE_BEFORE_CRM;
	private BigDecimal R81_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R81_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R81_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R81_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R81_ELIG_GUARANTEES;
	private BigDecimal R81_CREDIT_DERIVATIVES;
	private BigDecimal R81_CRM_COVERED_EXPOSURE;
	private BigDecimal R81_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R81_CRM_RISK_WEIGHT;
	private BigDecimal R81_RWA_CRM_COVERED;
	private BigDecimal R81_ORIG_COUNTERPARTY_RW;
	private BigDecimal R81_RWA_CRM_NOT_COVERED;
	private BigDecimal R81_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R81_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R81_COLLATERAL_CASH;
	private BigDecimal R81_COLLATERAL_TBILLS;
	private BigDecimal R81_COLLATERAL_DEBT_SEC;
	private BigDecimal R81_COLLATERAL_EQUITIES;
	private BigDecimal R81_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R81_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R81_EXPOSURE_AFTER_CRM;
	private BigDecimal R81_RWA_NOT_COVERED_CRM;
	private BigDecimal R81_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R81_RWA_UNSECURED;
	private BigDecimal R81_TOTAL_RWA;
	private BigDecimal R82_EXPOSURE_BEFORE_CRM;
	private BigDecimal R82_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R82_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R82_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R82_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R82_ELIG_GUARANTEES;
	private BigDecimal R82_CREDIT_DERIVATIVES;
	private BigDecimal R82_CRM_COVERED_EXPOSURE;
	private BigDecimal R82_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R82_CRM_RISK_WEIGHT;
	private BigDecimal R82_RWA_CRM_COVERED;
	private BigDecimal R82_ORIG_COUNTERPARTY_RW;
	private BigDecimal R82_RWA_CRM_NOT_COVERED;
	private BigDecimal R82_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R82_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R82_COLLATERAL_CASH;
	private BigDecimal R82_COLLATERAL_TBILLS;
	private BigDecimal R82_COLLATERAL_DEBT_SEC;
	private BigDecimal R82_COLLATERAL_EQUITIES;
	private BigDecimal R82_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R82_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R82_EXPOSURE_AFTER_CRM;
	private BigDecimal R82_RWA_NOT_COVERED_CRM;
	private BigDecimal R82_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R82_RWA_UNSECURED;
	private BigDecimal R82_TOTAL_RWA;
	private BigDecimal R83_EXPOSURE_BEFORE_CRM;
	private BigDecimal R83_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R83_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R83_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R83_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R83_ELIG_GUARANTEES;
	private BigDecimal R83_CREDIT_DERIVATIVES;
	private BigDecimal R83_CRM_COVERED_EXPOSURE;
	private BigDecimal R83_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R83_CRM_RISK_WEIGHT;
	private BigDecimal R83_RWA_CRM_COVERED;
	private BigDecimal R83_ORIG_COUNTERPARTY_RW;
	private BigDecimal R83_RWA_CRM_NOT_COVERED;
	private BigDecimal R83_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R83_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R83_COLLATERAL_CASH;
	private BigDecimal R83_COLLATERAL_TBILLS;
	private BigDecimal R83_COLLATERAL_DEBT_SEC;
	private BigDecimal R83_COLLATERAL_EQUITIES;
	private BigDecimal R83_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R83_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R83_EXPOSURE_AFTER_CRM;
	private BigDecimal R83_RWA_NOT_COVERED_CRM;
	private BigDecimal R83_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R83_RWA_UNSECURED;
	private BigDecimal R83_TOTAL_RWA;
	private BigDecimal R84_EXPOSURE_BEFORE_CRM;
	private BigDecimal R84_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R84_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R84_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R84_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R84_ELIG_GUARANTEES;
	private BigDecimal R84_CREDIT_DERIVATIVES;
	private BigDecimal R84_CRM_COVERED_EXPOSURE;
	private BigDecimal R84_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R84_CRM_RISK_WEIGHT;
	private BigDecimal R84_RWA_CRM_COVERED;
	private BigDecimal R84_ORIG_COUNTERPARTY_RW;
	private BigDecimal R84_RWA_CRM_NOT_COVERED;
	private BigDecimal R84_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R84_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R84_COLLATERAL_CASH;
	private BigDecimal R84_COLLATERAL_TBILLS;
	private BigDecimal R84_COLLATERAL_DEBT_SEC;
	private BigDecimal R84_COLLATERAL_EQUITIES;
	private BigDecimal R84_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R84_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R84_EXPOSURE_AFTER_CRM;
	private BigDecimal R84_RWA_NOT_COVERED_CRM;
	private BigDecimal R84_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R84_RWA_UNSECURED;
	private BigDecimal R84_TOTAL_RWA;
	private BigDecimal R85_EXPOSURE_BEFORE_CRM;
	private BigDecimal R85_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R85_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R85_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R85_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R85_ELIG_GUARANTEES;
	private BigDecimal R85_CREDIT_DERIVATIVES;
	private BigDecimal R85_CRM_COVERED_EXPOSURE;
	private BigDecimal R85_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R85_CRM_RISK_WEIGHT;
	private BigDecimal R85_RWA_CRM_COVERED;
	private BigDecimal R85_ORIG_COUNTERPARTY_RW;
	private BigDecimal R85_RWA_CRM_NOT_COVERED;
	private BigDecimal R85_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R85_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R85_COLLATERAL_CASH;
	private BigDecimal R85_COLLATERAL_TBILLS;
	private BigDecimal R85_COLLATERAL_DEBT_SEC;
	private BigDecimal R85_COLLATERAL_EQUITIES;
	private BigDecimal R85_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R85_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R85_EXPOSURE_AFTER_CRM;
	private BigDecimal R85_RWA_NOT_COVERED_CRM;
	private BigDecimal R85_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R85_RWA_UNSECURED;
	private BigDecimal R85_TOTAL_RWA;
	private BigDecimal R86_EXPOSURE_BEFORE_CRM;
	private BigDecimal R86_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R86_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R86_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R86_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R86_ELIG_GUARANTEES;
	private BigDecimal R86_CREDIT_DERIVATIVES;
	private BigDecimal R86_CRM_COVERED_EXPOSURE;
	private BigDecimal R86_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R86_CRM_RISK_WEIGHT;
	private BigDecimal R86_RWA_CRM_COVERED;
	private BigDecimal R86_ORIG_COUNTERPARTY_RW;
	private BigDecimal R86_RWA_CRM_NOT_COVERED;
	private BigDecimal R86_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R86_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R86_COLLATERAL_CASH;
	private BigDecimal R86_COLLATERAL_TBILLS;
	private BigDecimal R86_COLLATERAL_DEBT_SEC;
	private BigDecimal R86_COLLATERAL_EQUITIES;
	private BigDecimal R86_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R86_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R86_EXPOSURE_AFTER_CRM;
	private BigDecimal R86_RWA_NOT_COVERED_CRM;
	private BigDecimal R86_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R86_RWA_UNSECURED;
	private BigDecimal R86_TOTAL_RWA;
	private BigDecimal R87_EXPOSURE_BEFORE_CRM;
	private BigDecimal R87_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R87_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R87_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R87_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R87_ELIG_GUARANTEES;
	private BigDecimal R87_CREDIT_DERIVATIVES;
	private BigDecimal R87_CRM_COVERED_EXPOSURE;
	private BigDecimal R87_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R87_CRM_RISK_WEIGHT;
	private BigDecimal R87_RWA_CRM_COVERED;
	private BigDecimal R87_ORIG_COUNTERPARTY_RW;
	private BigDecimal R87_RWA_CRM_NOT_COVERED;
	private BigDecimal R87_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R87_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R87_COLLATERAL_CASH;
	private BigDecimal R87_COLLATERAL_TBILLS;
	private BigDecimal R87_COLLATERAL_DEBT_SEC;
	private BigDecimal R87_COLLATERAL_EQUITIES;
	private BigDecimal R87_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R87_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R87_EXPOSURE_AFTER_CRM;
	private BigDecimal R87_RWA_NOT_COVERED_CRM;
	private BigDecimal R87_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R87_RWA_UNSECURED;
	private BigDecimal R87_TOTAL_RWA;
	private BigDecimal R88_EXPOSURE_BEFORE_CRM;
	private BigDecimal R88_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R88_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R88_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R88_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R88_ELIG_GUARANTEES;
	private BigDecimal R88_CREDIT_DERIVATIVES;
	private BigDecimal R88_CRM_COVERED_EXPOSURE;
	private BigDecimal R88_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R88_CRM_RISK_WEIGHT;
	private BigDecimal R88_RWA_CRM_COVERED;
	private BigDecimal R88_ORIG_COUNTERPARTY_RW;
	private BigDecimal R88_RWA_CRM_NOT_COVERED;
	private BigDecimal R88_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R88_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R88_COLLATERAL_CASH;
	private BigDecimal R88_COLLATERAL_TBILLS;
	private BigDecimal R88_COLLATERAL_DEBT_SEC;
	private BigDecimal R88_COLLATERAL_EQUITIES;
	private BigDecimal R88_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R88_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R88_EXPOSURE_AFTER_CRM;
	private BigDecimal R88_RWA_NOT_COVERED_CRM;
	private BigDecimal R88_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R88_RWA_UNSECURED;
	private BigDecimal R88_TOTAL_RWA;
	private BigDecimal R89_EXPOSURE_BEFORE_CRM;
	private BigDecimal R89_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R89_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R89_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R89_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R89_ELIG_GUARANTEES;
	private BigDecimal R89_CREDIT_DERIVATIVES;
	private BigDecimal R89_CRM_COVERED_EXPOSURE;
	private BigDecimal R89_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R89_CRM_RISK_WEIGHT;
	private BigDecimal R89_RWA_CRM_COVERED;
	private BigDecimal R89_ORIG_COUNTERPARTY_RW;
	private BigDecimal R89_RWA_CRM_NOT_COVERED;
	private BigDecimal R89_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R89_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R89_COLLATERAL_CASH;
	private BigDecimal R89_COLLATERAL_TBILLS;
	private BigDecimal R89_COLLATERAL_DEBT_SEC;
	private BigDecimal R89_COLLATERAL_EQUITIES;
	private BigDecimal R89_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R89_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R89_EXPOSURE_AFTER_CRM;
	private BigDecimal R89_RWA_NOT_COVERED_CRM;
	private BigDecimal R89_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R89_RWA_UNSECURED;
	private BigDecimal R89_TOTAL_RWA;
	private BigDecimal R90_EXPOSURE_BEFORE_CRM;
	private BigDecimal R90_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R90_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R90_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R90_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R90_ELIG_GUARANTEES;
	private BigDecimal R90_CREDIT_DERIVATIVES;
	private BigDecimal R90_CRM_COVERED_EXPOSURE;
	private BigDecimal R90_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R90_CRM_RISK_WEIGHT;
	private BigDecimal R90_RWA_CRM_COVERED;
	private BigDecimal R90_ORIG_COUNTERPARTY_RW;
	private BigDecimal R90_RWA_CRM_NOT_COVERED;
	private BigDecimal R90_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R90_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R90_COLLATERAL_CASH;
	private BigDecimal R90_COLLATERAL_TBILLS;
	private BigDecimal R90_COLLATERAL_DEBT_SEC;
	private BigDecimal R90_COLLATERAL_EQUITIES;
	private BigDecimal R90_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R90_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R90_EXPOSURE_AFTER_CRM;
	private BigDecimal R90_RWA_NOT_COVERED_CRM;
	private BigDecimal R90_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R90_RWA_UNSECURED;
	private BigDecimal R90_TOTAL_RWA;
	private BigDecimal R91_EXPOSURE_BEFORE_CRM;
	private BigDecimal R91_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R91_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R91_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R91_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R91_ELIG_GUARANTEES;
	private BigDecimal R91_CREDIT_DERIVATIVES;
	private BigDecimal R91_CRM_COVERED_EXPOSURE;
	private BigDecimal R91_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R91_CRM_RISK_WEIGHT;
	private BigDecimal R91_RWA_CRM_COVERED;
	private BigDecimal R91_ORIG_COUNTERPARTY_RW;
	private BigDecimal R91_RWA_CRM_NOT_COVERED;
	private BigDecimal R91_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R91_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R91_COLLATERAL_CASH;
	private BigDecimal R91_COLLATERAL_TBILLS;
	private BigDecimal R91_COLLATERAL_DEBT_SEC;
	private BigDecimal R91_COLLATERAL_EQUITIES;
	private BigDecimal R91_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R91_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R91_EXPOSURE_AFTER_CRM;
	private BigDecimal R91_RWA_NOT_COVERED_CRM;
	private BigDecimal R91_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R91_RWA_UNSECURED;
	private BigDecimal R91_TOTAL_RWA;
	private BigDecimal R92_EXPOSURE_BEFORE_CRM;
	private BigDecimal R92_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R92_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R92_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R92_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R92_ELIG_GUARANTEES;
	private BigDecimal R92_CREDIT_DERIVATIVES;
	private BigDecimal R92_CRM_COVERED_EXPOSURE;
	private BigDecimal R92_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R92_CRM_RISK_WEIGHT;
	private BigDecimal R92_RWA_CRM_COVERED;
	private BigDecimal R92_ORIG_COUNTERPARTY_RW;
	private BigDecimal R92_RWA_CRM_NOT_COVERED;
	private BigDecimal R92_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R92_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R92_COLLATERAL_CASH;
	private BigDecimal R92_COLLATERAL_TBILLS;
	private BigDecimal R92_COLLATERAL_DEBT_SEC;
	private BigDecimal R92_COLLATERAL_EQUITIES;
	private BigDecimal R92_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R92_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R92_EXPOSURE_AFTER_CRM;
	private BigDecimal R92_RWA_NOT_COVERED_CRM;
	private BigDecimal R92_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R92_RWA_UNSECURED;
	private BigDecimal R92_TOTAL_RWA;
	private BigDecimal R93_EXPOSURE_BEFORE_CRM;
	private BigDecimal R93_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R93_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R93_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R93_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R93_ELIG_GUARANTEES;
	private BigDecimal R93_CREDIT_DERIVATIVES;
	private BigDecimal R93_CRM_COVERED_EXPOSURE;
	private BigDecimal R93_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R93_CRM_RISK_WEIGHT;
	private BigDecimal R93_RWA_CRM_COVERED;
	private BigDecimal R93_ORIG_COUNTERPARTY_RW;
	private BigDecimal R93_RWA_CRM_NOT_COVERED;
	private BigDecimal R93_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R93_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R93_COLLATERAL_CASH;
	private BigDecimal R93_COLLATERAL_TBILLS;
	private BigDecimal R93_COLLATERAL_DEBT_SEC;
	private BigDecimal R93_COLLATERAL_EQUITIES;
	private BigDecimal R93_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R93_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R93_EXPOSURE_AFTER_CRM;
	private BigDecimal R93_RWA_NOT_COVERED_CRM;
	private BigDecimal R93_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R93_RWA_UNSECURED;
	private BigDecimal R93_TOTAL_RWA;
	private BigDecimal R94_EXPOSURE_BEFORE_CRM;
	private BigDecimal R94_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R94_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R94_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R94_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R94_ELIG_GUARANTEES;
	private BigDecimal R94_CREDIT_DERIVATIVES;
	private BigDecimal R94_CRM_COVERED_EXPOSURE;
	private BigDecimal R94_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R94_CRM_RISK_WEIGHT;
	private BigDecimal R94_RWA_CRM_COVERED;
	private BigDecimal R94_ORIG_COUNTERPARTY_RW;
	private BigDecimal R94_RWA_CRM_NOT_COVERED;
	private BigDecimal R94_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R94_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R94_COLLATERAL_CASH;
	private BigDecimal R94_COLLATERAL_TBILLS;
	private BigDecimal R94_COLLATERAL_DEBT_SEC;
	private BigDecimal R94_COLLATERAL_EQUITIES;
	private BigDecimal R94_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R94_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R94_EXPOSURE_AFTER_CRM;
	private BigDecimal R94_RWA_NOT_COVERED_CRM;
	private BigDecimal R94_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R94_RWA_UNSECURED;
	private BigDecimal R94_TOTAL_RWA;
	private BigDecimal R95_EXPOSURE_BEFORE_CRM;
	private BigDecimal R95_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R95_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R95_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R95_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R95_ELIG_GUARANTEES;
	private BigDecimal R95_CREDIT_DERIVATIVES;
	private BigDecimal R95_CRM_COVERED_EXPOSURE;
	private BigDecimal R95_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R95_CRM_RISK_WEIGHT;
	private BigDecimal R95_RWA_CRM_COVERED;
	private BigDecimal R95_ORIG_COUNTERPARTY_RW;
	private BigDecimal R95_RWA_CRM_NOT_COVERED;
	private BigDecimal R95_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R95_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R95_COLLATERAL_CASH;
	private BigDecimal R95_COLLATERAL_TBILLS;
	private BigDecimal R95_COLLATERAL_DEBT_SEC;
	private BigDecimal R95_COLLATERAL_EQUITIES;
	private BigDecimal R95_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R95_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R95_EXPOSURE_AFTER_CRM;
	private BigDecimal R95_RWA_NOT_COVERED_CRM;
	private BigDecimal R95_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R95_RWA_UNSECURED;
	private BigDecimal R95_TOTAL_RWA;
	private BigDecimal R96_EXPOSURE_BEFORE_CRM;
	private BigDecimal R96_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R96_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R96_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R96_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R96_ELIG_GUARANTEES;
	private BigDecimal R96_CREDIT_DERIVATIVES;
	private BigDecimal R96_CRM_COVERED_EXPOSURE;
	private BigDecimal R96_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R96_CRM_RISK_WEIGHT;
	private BigDecimal R96_RWA_CRM_COVERED;
	private BigDecimal R96_ORIG_COUNTERPARTY_RW;
	private BigDecimal R96_RWA_CRM_NOT_COVERED;
	private BigDecimal R96_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R96_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R96_COLLATERAL_CASH;
	private BigDecimal R96_COLLATERAL_TBILLS;
	private BigDecimal R96_COLLATERAL_DEBT_SEC;
	private BigDecimal R96_COLLATERAL_EQUITIES;
	private BigDecimal R96_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R96_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R96_EXPOSURE_AFTER_CRM;
	private BigDecimal R96_RWA_NOT_COVERED_CRM;
	private BigDecimal R96_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R96_RWA_UNSECURED;
	private BigDecimal R96_TOTAL_RWA;
	private BigDecimal R97_EXPOSURE_BEFORE_CRM;
	private BigDecimal R97_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R97_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R97_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R97_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R97_ELIG_GUARANTEES;
	private BigDecimal R97_CREDIT_DERIVATIVES;
	private BigDecimal R97_CRM_COVERED_EXPOSURE;
	private BigDecimal R97_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R97_CRM_RISK_WEIGHT;
	private BigDecimal R97_RWA_CRM_COVERED;
	private BigDecimal R97_ORIG_COUNTERPARTY_RW;
	private BigDecimal R97_RWA_CRM_NOT_COVERED;
	private BigDecimal R97_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R97_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R97_COLLATERAL_CASH;
	private BigDecimal R97_COLLATERAL_TBILLS;
	private BigDecimal R97_COLLATERAL_DEBT_SEC;
	private BigDecimal R97_COLLATERAL_EQUITIES;
	private BigDecimal R97_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R97_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R97_EXPOSURE_AFTER_CRM;
	private BigDecimal R97_RWA_NOT_COVERED_CRM;
	private BigDecimal R97_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R97_RWA_UNSECURED;
	private BigDecimal R97_TOTAL_RWA;
	private BigDecimal R98_EXPOSURE_BEFORE_CRM;
	private BigDecimal R98_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R98_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R98_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R98_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R98_ELIG_GUARANTEES;
	private BigDecimal R98_CREDIT_DERIVATIVES;
	private BigDecimal R98_CRM_COVERED_EXPOSURE;
	private BigDecimal R98_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R98_CRM_RISK_WEIGHT;
	private BigDecimal R98_RWA_CRM_COVERED;
	private BigDecimal R98_ORIG_COUNTERPARTY_RW;
	private BigDecimal R98_RWA_CRM_NOT_COVERED;
	private BigDecimal R98_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R98_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R98_COLLATERAL_CASH;
	private BigDecimal R98_COLLATERAL_TBILLS;
	private BigDecimal R98_COLLATERAL_DEBT_SEC;
	private BigDecimal R98_COLLATERAL_EQUITIES;
	private BigDecimal R98_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R98_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R98_EXPOSURE_AFTER_CRM;
	private BigDecimal R98_RWA_NOT_COVERED_CRM;
	private BigDecimal R98_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R98_RWA_UNSECURED;
	private BigDecimal R98_TOTAL_RWA;
	private BigDecimal R99_EXPOSURE_BEFORE_CRM;
	private BigDecimal R99_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R99_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R99_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R99_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R99_ELIG_GUARANTEES;
	private BigDecimal R99_CREDIT_DERIVATIVES;
	private BigDecimal R99_CRM_COVERED_EXPOSURE;
	private BigDecimal R99_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R99_CRM_RISK_WEIGHT;
	private BigDecimal R99_RWA_CRM_COVERED;
	private BigDecimal R99_ORIG_COUNTERPARTY_RW;
	private BigDecimal R99_RWA_CRM_NOT_COVERED;
	private BigDecimal R99_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R99_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R99_COLLATERAL_CASH;
	private BigDecimal R99_COLLATERAL_TBILLS;
	private BigDecimal R99_COLLATERAL_DEBT_SEC;
	private BigDecimal R99_COLLATERAL_EQUITIES;
	private BigDecimal R99_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R99_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R99_EXPOSURE_AFTER_CRM;
	private BigDecimal R99_RWA_NOT_COVERED_CRM;
	private BigDecimal R99_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R99_RWA_UNSECURED;
	private BigDecimal R99_TOTAL_RWA;
	private BigDecimal R100_EXPOSURE_BEFORE_CRM;
	private BigDecimal R100_SPEC_PROVISION_PAST_DUE;
	private BigDecimal R100_ON_BAL_SHEET_NETTING_ELIG;
	private BigDecimal R100_TOTAL_EXPOSURE_AFTER_NET;
	private BigDecimal R100_CRM_ELIG_EXPOSURE_SUBS;
	private BigDecimal R100_ELIG_GUARANTEES;
	private BigDecimal R100_CREDIT_DERIVATIVES;
	private BigDecimal R100_CRM_COVERED_EXPOSURE;
	private BigDecimal R100_CRM_NOT_COVERED_EXPOSURE;
	private BigDecimal R100_CRM_RISK_WEIGHT;
	private BigDecimal R100_RWA_CRM_COVERED;
	private BigDecimal R100_ORIG_COUNTERPARTY_RW;
	private BigDecimal R100_RWA_CRM_NOT_COVERED;
	private BigDecimal R100_CRM_ELIG_EXPOSURE_COMP;
	private BigDecimal R100_EXPOSURE_AFTER_VOL_ADJ;
	private BigDecimal R100_COLLATERAL_CASH;
	private BigDecimal R100_COLLATERAL_TBILLS;
	private BigDecimal R100_COLLATERAL_DEBT_SEC;
	private BigDecimal R100_COLLATERAL_EQUITIES;
	private BigDecimal R100_COLLATERAL_MUTUAL_FUNDS;
	private BigDecimal R100_TOTAL_COLLATERAL_HAIRCUT;
	private BigDecimal R100_EXPOSURE_AFTER_CRM;
	private BigDecimal R100_RWA_NOT_COVERED_CRM;
	private BigDecimal R100_RWA_UNSECURED_EXPOSURE;
	private BigDecimal R100_RWA_UNSECURED;
	private BigDecimal R100_TOTAL_RWA;
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
	public BigDecimal getR70_EXPOSURE_BEFORE_CRM() {
		return R70_EXPOSURE_BEFORE_CRM;
	}
	public void setR70_EXPOSURE_BEFORE_CRM(BigDecimal r70_EXPOSURE_BEFORE_CRM) {
		R70_EXPOSURE_BEFORE_CRM = r70_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR70_SPEC_PROVISION_PAST_DUE() {
		return R70_SPEC_PROVISION_PAST_DUE;
	}
	public void setR70_SPEC_PROVISION_PAST_DUE(BigDecimal r70_SPEC_PROVISION_PAST_DUE) {
		R70_SPEC_PROVISION_PAST_DUE = r70_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR70_ON_BAL_SHEET_NETTING_ELIG() {
		return R70_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR70_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r70_ON_BAL_SHEET_NETTING_ELIG) {
		R70_ON_BAL_SHEET_NETTING_ELIG = r70_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR70_TOTAL_EXPOSURE_AFTER_NET() {
		return R70_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR70_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r70_TOTAL_EXPOSURE_AFTER_NET) {
		R70_TOTAL_EXPOSURE_AFTER_NET = r70_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR70_CRM_ELIG_EXPOSURE_SUBS() {
		return R70_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR70_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r70_CRM_ELIG_EXPOSURE_SUBS) {
		R70_CRM_ELIG_EXPOSURE_SUBS = r70_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR70_ELIG_GUARANTEES() {
		return R70_ELIG_GUARANTEES;
	}
	public void setR70_ELIG_GUARANTEES(BigDecimal r70_ELIG_GUARANTEES) {
		R70_ELIG_GUARANTEES = r70_ELIG_GUARANTEES;
	}
	public BigDecimal getR70_CREDIT_DERIVATIVES() {
		return R70_CREDIT_DERIVATIVES;
	}
	public void setR70_CREDIT_DERIVATIVES(BigDecimal r70_CREDIT_DERIVATIVES) {
		R70_CREDIT_DERIVATIVES = r70_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR70_CRM_COVERED_EXPOSURE() {
		return R70_CRM_COVERED_EXPOSURE;
	}
	public void setR70_CRM_COVERED_EXPOSURE(BigDecimal r70_CRM_COVERED_EXPOSURE) {
		R70_CRM_COVERED_EXPOSURE = r70_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR70_CRM_NOT_COVERED_EXPOSURE() {
		return R70_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR70_CRM_NOT_COVERED_EXPOSURE(BigDecimal r70_CRM_NOT_COVERED_EXPOSURE) {
		R70_CRM_NOT_COVERED_EXPOSURE = r70_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR70_CRM_RISK_WEIGHT() {
		return R70_CRM_RISK_WEIGHT;
	}
	public void setR70_CRM_RISK_WEIGHT(BigDecimal r70_CRM_RISK_WEIGHT) {
		R70_CRM_RISK_WEIGHT = r70_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR70_RWA_CRM_COVERED() {
		return R70_RWA_CRM_COVERED;
	}
	public void setR70_RWA_CRM_COVERED(BigDecimal r70_RWA_CRM_COVERED) {
		R70_RWA_CRM_COVERED = r70_RWA_CRM_COVERED;
	}
	public BigDecimal getR70_ORIG_COUNTERPARTY_RW() {
		return R70_ORIG_COUNTERPARTY_RW;
	}
	public void setR70_ORIG_COUNTERPARTY_RW(BigDecimal r70_ORIG_COUNTERPARTY_RW) {
		R70_ORIG_COUNTERPARTY_RW = r70_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR70_RWA_CRM_NOT_COVERED() {
		return R70_RWA_CRM_NOT_COVERED;
	}
	public void setR70_RWA_CRM_NOT_COVERED(BigDecimal r70_RWA_CRM_NOT_COVERED) {
		R70_RWA_CRM_NOT_COVERED = r70_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR70_CRM_ELIG_EXPOSURE_COMP() {
		return R70_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR70_CRM_ELIG_EXPOSURE_COMP(BigDecimal r70_CRM_ELIG_EXPOSURE_COMP) {
		R70_CRM_ELIG_EXPOSURE_COMP = r70_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR70_EXPOSURE_AFTER_VOL_ADJ() {
		return R70_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR70_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r70_EXPOSURE_AFTER_VOL_ADJ) {
		R70_EXPOSURE_AFTER_VOL_ADJ = r70_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR70_COLLATERAL_CASH() {
		return R70_COLLATERAL_CASH;
	}
	public void setR70_COLLATERAL_CASH(BigDecimal r70_COLLATERAL_CASH) {
		R70_COLLATERAL_CASH = r70_COLLATERAL_CASH;
	}
	public BigDecimal getR70_COLLATERAL_TBILLS() {
		return R70_COLLATERAL_TBILLS;
	}
	public void setR70_COLLATERAL_TBILLS(BigDecimal r70_COLLATERAL_TBILLS) {
		R70_COLLATERAL_TBILLS = r70_COLLATERAL_TBILLS;
	}
	public BigDecimal getR70_COLLATERAL_DEBT_SEC() {
		return R70_COLLATERAL_DEBT_SEC;
	}
	public void setR70_COLLATERAL_DEBT_SEC(BigDecimal r70_COLLATERAL_DEBT_SEC) {
		R70_COLLATERAL_DEBT_SEC = r70_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR70_COLLATERAL_EQUITIES() {
		return R70_COLLATERAL_EQUITIES;
	}
	public void setR70_COLLATERAL_EQUITIES(BigDecimal r70_COLLATERAL_EQUITIES) {
		R70_COLLATERAL_EQUITIES = r70_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR70_COLLATERAL_MUTUAL_FUNDS() {
		return R70_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR70_COLLATERAL_MUTUAL_FUNDS(BigDecimal r70_COLLATERAL_MUTUAL_FUNDS) {
		R70_COLLATERAL_MUTUAL_FUNDS = r70_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR70_TOTAL_COLLATERAL_HAIRCUT() {
		return R70_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR70_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r70_TOTAL_COLLATERAL_HAIRCUT) {
		R70_TOTAL_COLLATERAL_HAIRCUT = r70_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR70_EXPOSURE_AFTER_CRM() {
		return R70_EXPOSURE_AFTER_CRM;
	}
	public void setR70_EXPOSURE_AFTER_CRM(BigDecimal r70_EXPOSURE_AFTER_CRM) {
		R70_EXPOSURE_AFTER_CRM = r70_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR70_RWA_NOT_COVERED_CRM() {
		return R70_RWA_NOT_COVERED_CRM;
	}
	public void setR70_RWA_NOT_COVERED_CRM(BigDecimal r70_RWA_NOT_COVERED_CRM) {
		R70_RWA_NOT_COVERED_CRM = r70_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR70_RWA_UNSECURED_EXPOSURE() {
		return R70_RWA_UNSECURED_EXPOSURE;
	}
	public void setR70_RWA_UNSECURED_EXPOSURE(BigDecimal r70_RWA_UNSECURED_EXPOSURE) {
		R70_RWA_UNSECURED_EXPOSURE = r70_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR70_RWA_UNSECURED() {
		return R70_RWA_UNSECURED;
	}
	public void setR70_RWA_UNSECURED(BigDecimal r70_RWA_UNSECURED) {
		R70_RWA_UNSECURED = r70_RWA_UNSECURED;
	}
	public BigDecimal getR70_TOTAL_RWA() {
		return R70_TOTAL_RWA;
	}
	public void setR70_TOTAL_RWA(BigDecimal r70_TOTAL_RWA) {
		R70_TOTAL_RWA = r70_TOTAL_RWA;
	}
	public BigDecimal getR71_EXPOSURE_BEFORE_CRM() {
		return R71_EXPOSURE_BEFORE_CRM;
	}
	public void setR71_EXPOSURE_BEFORE_CRM(BigDecimal r71_EXPOSURE_BEFORE_CRM) {
		R71_EXPOSURE_BEFORE_CRM = r71_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR71_SPEC_PROVISION_PAST_DUE() {
		return R71_SPEC_PROVISION_PAST_DUE;
	}
	public void setR71_SPEC_PROVISION_PAST_DUE(BigDecimal r71_SPEC_PROVISION_PAST_DUE) {
		R71_SPEC_PROVISION_PAST_DUE = r71_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR71_ON_BAL_SHEET_NETTING_ELIG() {
		return R71_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR71_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r71_ON_BAL_SHEET_NETTING_ELIG) {
		R71_ON_BAL_SHEET_NETTING_ELIG = r71_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR71_TOTAL_EXPOSURE_AFTER_NET() {
		return R71_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR71_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r71_TOTAL_EXPOSURE_AFTER_NET) {
		R71_TOTAL_EXPOSURE_AFTER_NET = r71_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR71_CRM_ELIG_EXPOSURE_SUBS() {
		return R71_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR71_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r71_CRM_ELIG_EXPOSURE_SUBS) {
		R71_CRM_ELIG_EXPOSURE_SUBS = r71_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR71_ELIG_GUARANTEES() {
		return R71_ELIG_GUARANTEES;
	}
	public void setR71_ELIG_GUARANTEES(BigDecimal r71_ELIG_GUARANTEES) {
		R71_ELIG_GUARANTEES = r71_ELIG_GUARANTEES;
	}
	public BigDecimal getR71_CREDIT_DERIVATIVES() {
		return R71_CREDIT_DERIVATIVES;
	}
	public void setR71_CREDIT_DERIVATIVES(BigDecimal r71_CREDIT_DERIVATIVES) {
		R71_CREDIT_DERIVATIVES = r71_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR71_CRM_COVERED_EXPOSURE() {
		return R71_CRM_COVERED_EXPOSURE;
	}
	public void setR71_CRM_COVERED_EXPOSURE(BigDecimal r71_CRM_COVERED_EXPOSURE) {
		R71_CRM_COVERED_EXPOSURE = r71_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR71_CRM_NOT_COVERED_EXPOSURE() {
		return R71_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR71_CRM_NOT_COVERED_EXPOSURE(BigDecimal r71_CRM_NOT_COVERED_EXPOSURE) {
		R71_CRM_NOT_COVERED_EXPOSURE = r71_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR71_CRM_RISK_WEIGHT() {
		return R71_CRM_RISK_WEIGHT;
	}
	public void setR71_CRM_RISK_WEIGHT(BigDecimal r71_CRM_RISK_WEIGHT) {
		R71_CRM_RISK_WEIGHT = r71_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR71_RWA_CRM_COVERED() {
		return R71_RWA_CRM_COVERED;
	}
	public void setR71_RWA_CRM_COVERED(BigDecimal r71_RWA_CRM_COVERED) {
		R71_RWA_CRM_COVERED = r71_RWA_CRM_COVERED;
	}
	public BigDecimal getR71_ORIG_COUNTERPARTY_RW() {
		return R71_ORIG_COUNTERPARTY_RW;
	}
	public void setR71_ORIG_COUNTERPARTY_RW(BigDecimal r71_ORIG_COUNTERPARTY_RW) {
		R71_ORIG_COUNTERPARTY_RW = r71_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR71_RWA_CRM_NOT_COVERED() {
		return R71_RWA_CRM_NOT_COVERED;
	}
	public void setR71_RWA_CRM_NOT_COVERED(BigDecimal r71_RWA_CRM_NOT_COVERED) {
		R71_RWA_CRM_NOT_COVERED = r71_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR71_CRM_ELIG_EXPOSURE_COMP() {
		return R71_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR71_CRM_ELIG_EXPOSURE_COMP(BigDecimal r71_CRM_ELIG_EXPOSURE_COMP) {
		R71_CRM_ELIG_EXPOSURE_COMP = r71_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR71_EXPOSURE_AFTER_VOL_ADJ() {
		return R71_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR71_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r71_EXPOSURE_AFTER_VOL_ADJ) {
		R71_EXPOSURE_AFTER_VOL_ADJ = r71_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR71_COLLATERAL_CASH() {
		return R71_COLLATERAL_CASH;
	}
	public void setR71_COLLATERAL_CASH(BigDecimal r71_COLLATERAL_CASH) {
		R71_COLLATERAL_CASH = r71_COLLATERAL_CASH;
	}
	public BigDecimal getR71_COLLATERAL_TBILLS() {
		return R71_COLLATERAL_TBILLS;
	}
	public void setR71_COLLATERAL_TBILLS(BigDecimal r71_COLLATERAL_TBILLS) {
		R71_COLLATERAL_TBILLS = r71_COLLATERAL_TBILLS;
	}
	public BigDecimal getR71_COLLATERAL_DEBT_SEC() {
		return R71_COLLATERAL_DEBT_SEC;
	}
	public void setR71_COLLATERAL_DEBT_SEC(BigDecimal r71_COLLATERAL_DEBT_SEC) {
		R71_COLLATERAL_DEBT_SEC = r71_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR71_COLLATERAL_EQUITIES() {
		return R71_COLLATERAL_EQUITIES;
	}
	public void setR71_COLLATERAL_EQUITIES(BigDecimal r71_COLLATERAL_EQUITIES) {
		R71_COLLATERAL_EQUITIES = r71_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR71_COLLATERAL_MUTUAL_FUNDS() {
		return R71_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR71_COLLATERAL_MUTUAL_FUNDS(BigDecimal r71_COLLATERAL_MUTUAL_FUNDS) {
		R71_COLLATERAL_MUTUAL_FUNDS = r71_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR71_TOTAL_COLLATERAL_HAIRCUT() {
		return R71_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR71_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r71_TOTAL_COLLATERAL_HAIRCUT) {
		R71_TOTAL_COLLATERAL_HAIRCUT = r71_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR71_EXPOSURE_AFTER_CRM() {
		return R71_EXPOSURE_AFTER_CRM;
	}
	public void setR71_EXPOSURE_AFTER_CRM(BigDecimal r71_EXPOSURE_AFTER_CRM) {
		R71_EXPOSURE_AFTER_CRM = r71_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR71_RWA_NOT_COVERED_CRM() {
		return R71_RWA_NOT_COVERED_CRM;
	}
	public void setR71_RWA_NOT_COVERED_CRM(BigDecimal r71_RWA_NOT_COVERED_CRM) {
		R71_RWA_NOT_COVERED_CRM = r71_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR71_RWA_UNSECURED_EXPOSURE() {
		return R71_RWA_UNSECURED_EXPOSURE;
	}
	public void setR71_RWA_UNSECURED_EXPOSURE(BigDecimal r71_RWA_UNSECURED_EXPOSURE) {
		R71_RWA_UNSECURED_EXPOSURE = r71_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR71_RWA_UNSECURED() {
		return R71_RWA_UNSECURED;
	}
	public void setR71_RWA_UNSECURED(BigDecimal r71_RWA_UNSECURED) {
		R71_RWA_UNSECURED = r71_RWA_UNSECURED;
	}
	public BigDecimal getR71_TOTAL_RWA() {
		return R71_TOTAL_RWA;
	}
	public void setR71_TOTAL_RWA(BigDecimal r71_TOTAL_RWA) {
		R71_TOTAL_RWA = r71_TOTAL_RWA;
	}
	public BigDecimal getR72_EXPOSURE_BEFORE_CRM() {
		return R72_EXPOSURE_BEFORE_CRM;
	}
	public void setR72_EXPOSURE_BEFORE_CRM(BigDecimal r72_EXPOSURE_BEFORE_CRM) {
		R72_EXPOSURE_BEFORE_CRM = r72_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR72_SPEC_PROVISION_PAST_DUE() {
		return R72_SPEC_PROVISION_PAST_DUE;
	}
	public void setR72_SPEC_PROVISION_PAST_DUE(BigDecimal r72_SPEC_PROVISION_PAST_DUE) {
		R72_SPEC_PROVISION_PAST_DUE = r72_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR72_ON_BAL_SHEET_NETTING_ELIG() {
		return R72_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR72_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r72_ON_BAL_SHEET_NETTING_ELIG) {
		R72_ON_BAL_SHEET_NETTING_ELIG = r72_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR72_TOTAL_EXPOSURE_AFTER_NET() {
		return R72_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR72_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r72_TOTAL_EXPOSURE_AFTER_NET) {
		R72_TOTAL_EXPOSURE_AFTER_NET = r72_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR72_CRM_ELIG_EXPOSURE_SUBS() {
		return R72_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR72_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r72_CRM_ELIG_EXPOSURE_SUBS) {
		R72_CRM_ELIG_EXPOSURE_SUBS = r72_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR72_ELIG_GUARANTEES() {
		return R72_ELIG_GUARANTEES;
	}
	public void setR72_ELIG_GUARANTEES(BigDecimal r72_ELIG_GUARANTEES) {
		R72_ELIG_GUARANTEES = r72_ELIG_GUARANTEES;
	}
	public BigDecimal getR72_CREDIT_DERIVATIVES() {
		return R72_CREDIT_DERIVATIVES;
	}
	public void setR72_CREDIT_DERIVATIVES(BigDecimal r72_CREDIT_DERIVATIVES) {
		R72_CREDIT_DERIVATIVES = r72_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR72_CRM_COVERED_EXPOSURE() {
		return R72_CRM_COVERED_EXPOSURE;
	}
	public void setR72_CRM_COVERED_EXPOSURE(BigDecimal r72_CRM_COVERED_EXPOSURE) {
		R72_CRM_COVERED_EXPOSURE = r72_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR72_CRM_NOT_COVERED_EXPOSURE() {
		return R72_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR72_CRM_NOT_COVERED_EXPOSURE(BigDecimal r72_CRM_NOT_COVERED_EXPOSURE) {
		R72_CRM_NOT_COVERED_EXPOSURE = r72_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR72_CRM_RISK_WEIGHT() {
		return R72_CRM_RISK_WEIGHT;
	}
	public void setR72_CRM_RISK_WEIGHT(BigDecimal r72_CRM_RISK_WEIGHT) {
		R72_CRM_RISK_WEIGHT = r72_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR72_RWA_CRM_COVERED() {
		return R72_RWA_CRM_COVERED;
	}
	public void setR72_RWA_CRM_COVERED(BigDecimal r72_RWA_CRM_COVERED) {
		R72_RWA_CRM_COVERED = r72_RWA_CRM_COVERED;
	}
	public BigDecimal getR72_ORIG_COUNTERPARTY_RW() {
		return R72_ORIG_COUNTERPARTY_RW;
	}
	public void setR72_ORIG_COUNTERPARTY_RW(BigDecimal r72_ORIG_COUNTERPARTY_RW) {
		R72_ORIG_COUNTERPARTY_RW = r72_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR72_RWA_CRM_NOT_COVERED() {
		return R72_RWA_CRM_NOT_COVERED;
	}
	public void setR72_RWA_CRM_NOT_COVERED(BigDecimal r72_RWA_CRM_NOT_COVERED) {
		R72_RWA_CRM_NOT_COVERED = r72_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR72_CRM_ELIG_EXPOSURE_COMP() {
		return R72_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR72_CRM_ELIG_EXPOSURE_COMP(BigDecimal r72_CRM_ELIG_EXPOSURE_COMP) {
		R72_CRM_ELIG_EXPOSURE_COMP = r72_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR72_EXPOSURE_AFTER_VOL_ADJ() {
		return R72_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR72_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r72_EXPOSURE_AFTER_VOL_ADJ) {
		R72_EXPOSURE_AFTER_VOL_ADJ = r72_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR72_COLLATERAL_CASH() {
		return R72_COLLATERAL_CASH;
	}
	public void setR72_COLLATERAL_CASH(BigDecimal r72_COLLATERAL_CASH) {
		R72_COLLATERAL_CASH = r72_COLLATERAL_CASH;
	}
	public BigDecimal getR72_COLLATERAL_TBILLS() {
		return R72_COLLATERAL_TBILLS;
	}
	public void setR72_COLLATERAL_TBILLS(BigDecimal r72_COLLATERAL_TBILLS) {
		R72_COLLATERAL_TBILLS = r72_COLLATERAL_TBILLS;
	}
	public BigDecimal getR72_COLLATERAL_DEBT_SEC() {
		return R72_COLLATERAL_DEBT_SEC;
	}
	public void setR72_COLLATERAL_DEBT_SEC(BigDecimal r72_COLLATERAL_DEBT_SEC) {
		R72_COLLATERAL_DEBT_SEC = r72_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR72_COLLATERAL_EQUITIES() {
		return R72_COLLATERAL_EQUITIES;
	}
	public void setR72_COLLATERAL_EQUITIES(BigDecimal r72_COLLATERAL_EQUITIES) {
		R72_COLLATERAL_EQUITIES = r72_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR72_COLLATERAL_MUTUAL_FUNDS() {
		return R72_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR72_COLLATERAL_MUTUAL_FUNDS(BigDecimal r72_COLLATERAL_MUTUAL_FUNDS) {
		R72_COLLATERAL_MUTUAL_FUNDS = r72_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR72_TOTAL_COLLATERAL_HAIRCUT() {
		return R72_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR72_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r72_TOTAL_COLLATERAL_HAIRCUT) {
		R72_TOTAL_COLLATERAL_HAIRCUT = r72_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR72_EXPOSURE_AFTER_CRM() {
		return R72_EXPOSURE_AFTER_CRM;
	}
	public void setR72_EXPOSURE_AFTER_CRM(BigDecimal r72_EXPOSURE_AFTER_CRM) {
		R72_EXPOSURE_AFTER_CRM = r72_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR72_RWA_NOT_COVERED_CRM() {
		return R72_RWA_NOT_COVERED_CRM;
	}
	public void setR72_RWA_NOT_COVERED_CRM(BigDecimal r72_RWA_NOT_COVERED_CRM) {
		R72_RWA_NOT_COVERED_CRM = r72_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR72_RWA_UNSECURED_EXPOSURE() {
		return R72_RWA_UNSECURED_EXPOSURE;
	}
	public void setR72_RWA_UNSECURED_EXPOSURE(BigDecimal r72_RWA_UNSECURED_EXPOSURE) {
		R72_RWA_UNSECURED_EXPOSURE = r72_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR72_RWA_UNSECURED() {
		return R72_RWA_UNSECURED;
	}
	public void setR72_RWA_UNSECURED(BigDecimal r72_RWA_UNSECURED) {
		R72_RWA_UNSECURED = r72_RWA_UNSECURED;
	}
	public BigDecimal getR72_TOTAL_RWA() {
		return R72_TOTAL_RWA;
	}
	public void setR72_TOTAL_RWA(BigDecimal r72_TOTAL_RWA) {
		R72_TOTAL_RWA = r72_TOTAL_RWA;
	}
	public BigDecimal getR73_EXPOSURE_BEFORE_CRM() {
		return R73_EXPOSURE_BEFORE_CRM;
	}
	public void setR73_EXPOSURE_BEFORE_CRM(BigDecimal r73_EXPOSURE_BEFORE_CRM) {
		R73_EXPOSURE_BEFORE_CRM = r73_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR73_SPEC_PROVISION_PAST_DUE() {
		return R73_SPEC_PROVISION_PAST_DUE;
	}
	public void setR73_SPEC_PROVISION_PAST_DUE(BigDecimal r73_SPEC_PROVISION_PAST_DUE) {
		R73_SPEC_PROVISION_PAST_DUE = r73_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR73_ON_BAL_SHEET_NETTING_ELIG() {
		return R73_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR73_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r73_ON_BAL_SHEET_NETTING_ELIG) {
		R73_ON_BAL_SHEET_NETTING_ELIG = r73_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR73_TOTAL_EXPOSURE_AFTER_NET() {
		return R73_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR73_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r73_TOTAL_EXPOSURE_AFTER_NET) {
		R73_TOTAL_EXPOSURE_AFTER_NET = r73_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR73_CRM_ELIG_EXPOSURE_SUBS() {
		return R73_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR73_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r73_CRM_ELIG_EXPOSURE_SUBS) {
		R73_CRM_ELIG_EXPOSURE_SUBS = r73_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR73_ELIG_GUARANTEES() {
		return R73_ELIG_GUARANTEES;
	}
	public void setR73_ELIG_GUARANTEES(BigDecimal r73_ELIG_GUARANTEES) {
		R73_ELIG_GUARANTEES = r73_ELIG_GUARANTEES;
	}
	public BigDecimal getR73_CREDIT_DERIVATIVES() {
		return R73_CREDIT_DERIVATIVES;
	}
	public void setR73_CREDIT_DERIVATIVES(BigDecimal r73_CREDIT_DERIVATIVES) {
		R73_CREDIT_DERIVATIVES = r73_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR73_CRM_COVERED_EXPOSURE() {
		return R73_CRM_COVERED_EXPOSURE;
	}
	public void setR73_CRM_COVERED_EXPOSURE(BigDecimal r73_CRM_COVERED_EXPOSURE) {
		R73_CRM_COVERED_EXPOSURE = r73_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR73_CRM_NOT_COVERED_EXPOSURE() {
		return R73_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR73_CRM_NOT_COVERED_EXPOSURE(BigDecimal r73_CRM_NOT_COVERED_EXPOSURE) {
		R73_CRM_NOT_COVERED_EXPOSURE = r73_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR73_CRM_RISK_WEIGHT() {
		return R73_CRM_RISK_WEIGHT;
	}
	public void setR73_CRM_RISK_WEIGHT(BigDecimal r73_CRM_RISK_WEIGHT) {
		R73_CRM_RISK_WEIGHT = r73_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR73_RWA_CRM_COVERED() {
		return R73_RWA_CRM_COVERED;
	}
	public void setR73_RWA_CRM_COVERED(BigDecimal r73_RWA_CRM_COVERED) {
		R73_RWA_CRM_COVERED = r73_RWA_CRM_COVERED;
	}
	public BigDecimal getR73_ORIG_COUNTERPARTY_RW() {
		return R73_ORIG_COUNTERPARTY_RW;
	}
	public void setR73_ORIG_COUNTERPARTY_RW(BigDecimal r73_ORIG_COUNTERPARTY_RW) {
		R73_ORIG_COUNTERPARTY_RW = r73_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR73_RWA_CRM_NOT_COVERED() {
		return R73_RWA_CRM_NOT_COVERED;
	}
	public void setR73_RWA_CRM_NOT_COVERED(BigDecimal r73_RWA_CRM_NOT_COVERED) {
		R73_RWA_CRM_NOT_COVERED = r73_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR73_CRM_ELIG_EXPOSURE_COMP() {
		return R73_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR73_CRM_ELIG_EXPOSURE_COMP(BigDecimal r73_CRM_ELIG_EXPOSURE_COMP) {
		R73_CRM_ELIG_EXPOSURE_COMP = r73_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR73_EXPOSURE_AFTER_VOL_ADJ() {
		return R73_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR73_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r73_EXPOSURE_AFTER_VOL_ADJ) {
		R73_EXPOSURE_AFTER_VOL_ADJ = r73_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR73_COLLATERAL_CASH() {
		return R73_COLLATERAL_CASH;
	}
	public void setR73_COLLATERAL_CASH(BigDecimal r73_COLLATERAL_CASH) {
		R73_COLLATERAL_CASH = r73_COLLATERAL_CASH;
	}
	public BigDecimal getR73_COLLATERAL_TBILLS() {
		return R73_COLLATERAL_TBILLS;
	}
	public void setR73_COLLATERAL_TBILLS(BigDecimal r73_COLLATERAL_TBILLS) {
		R73_COLLATERAL_TBILLS = r73_COLLATERAL_TBILLS;
	}
	public BigDecimal getR73_COLLATERAL_DEBT_SEC() {
		return R73_COLLATERAL_DEBT_SEC;
	}
	public void setR73_COLLATERAL_DEBT_SEC(BigDecimal r73_COLLATERAL_DEBT_SEC) {
		R73_COLLATERAL_DEBT_SEC = r73_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR73_COLLATERAL_EQUITIES() {
		return R73_COLLATERAL_EQUITIES;
	}
	public void setR73_COLLATERAL_EQUITIES(BigDecimal r73_COLLATERAL_EQUITIES) {
		R73_COLLATERAL_EQUITIES = r73_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR73_COLLATERAL_MUTUAL_FUNDS() {
		return R73_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR73_COLLATERAL_MUTUAL_FUNDS(BigDecimal r73_COLLATERAL_MUTUAL_FUNDS) {
		R73_COLLATERAL_MUTUAL_FUNDS = r73_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR73_TOTAL_COLLATERAL_HAIRCUT() {
		return R73_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR73_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r73_TOTAL_COLLATERAL_HAIRCUT) {
		R73_TOTAL_COLLATERAL_HAIRCUT = r73_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR73_EXPOSURE_AFTER_CRM() {
		return R73_EXPOSURE_AFTER_CRM;
	}
	public void setR73_EXPOSURE_AFTER_CRM(BigDecimal r73_EXPOSURE_AFTER_CRM) {
		R73_EXPOSURE_AFTER_CRM = r73_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR73_RWA_NOT_COVERED_CRM() {
		return R73_RWA_NOT_COVERED_CRM;
	}
	public void setR73_RWA_NOT_COVERED_CRM(BigDecimal r73_RWA_NOT_COVERED_CRM) {
		R73_RWA_NOT_COVERED_CRM = r73_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR73_RWA_UNSECURED_EXPOSURE() {
		return R73_RWA_UNSECURED_EXPOSURE;
	}
	public void setR73_RWA_UNSECURED_EXPOSURE(BigDecimal r73_RWA_UNSECURED_EXPOSURE) {
		R73_RWA_UNSECURED_EXPOSURE = r73_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR73_RWA_UNSECURED() {
		return R73_RWA_UNSECURED;
	}
	public void setR73_RWA_UNSECURED(BigDecimal r73_RWA_UNSECURED) {
		R73_RWA_UNSECURED = r73_RWA_UNSECURED;
	}
	public BigDecimal getR73_TOTAL_RWA() {
		return R73_TOTAL_RWA;
	}
	public void setR73_TOTAL_RWA(BigDecimal r73_TOTAL_RWA) {
		R73_TOTAL_RWA = r73_TOTAL_RWA;
	}
	public BigDecimal getR74_EXPOSURE_BEFORE_CRM() {
		return R74_EXPOSURE_BEFORE_CRM;
	}
	public void setR74_EXPOSURE_BEFORE_CRM(BigDecimal r74_EXPOSURE_BEFORE_CRM) {
		R74_EXPOSURE_BEFORE_CRM = r74_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR74_SPEC_PROVISION_PAST_DUE() {
		return R74_SPEC_PROVISION_PAST_DUE;
	}
	public void setR74_SPEC_PROVISION_PAST_DUE(BigDecimal r74_SPEC_PROVISION_PAST_DUE) {
		R74_SPEC_PROVISION_PAST_DUE = r74_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR74_ON_BAL_SHEET_NETTING_ELIG() {
		return R74_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR74_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r74_ON_BAL_SHEET_NETTING_ELIG) {
		R74_ON_BAL_SHEET_NETTING_ELIG = r74_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR74_TOTAL_EXPOSURE_AFTER_NET() {
		return R74_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR74_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r74_TOTAL_EXPOSURE_AFTER_NET) {
		R74_TOTAL_EXPOSURE_AFTER_NET = r74_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR74_CRM_ELIG_EXPOSURE_SUBS() {
		return R74_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR74_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r74_CRM_ELIG_EXPOSURE_SUBS) {
		R74_CRM_ELIG_EXPOSURE_SUBS = r74_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR74_ELIG_GUARANTEES() {
		return R74_ELIG_GUARANTEES;
	}
	public void setR74_ELIG_GUARANTEES(BigDecimal r74_ELIG_GUARANTEES) {
		R74_ELIG_GUARANTEES = r74_ELIG_GUARANTEES;
	}
	public BigDecimal getR74_CREDIT_DERIVATIVES() {
		return R74_CREDIT_DERIVATIVES;
	}
	public void setR74_CREDIT_DERIVATIVES(BigDecimal r74_CREDIT_DERIVATIVES) {
		R74_CREDIT_DERIVATIVES = r74_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR74_CRM_COVERED_EXPOSURE() {
		return R74_CRM_COVERED_EXPOSURE;
	}
	public void setR74_CRM_COVERED_EXPOSURE(BigDecimal r74_CRM_COVERED_EXPOSURE) {
		R74_CRM_COVERED_EXPOSURE = r74_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR74_CRM_NOT_COVERED_EXPOSURE() {
		return R74_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR74_CRM_NOT_COVERED_EXPOSURE(BigDecimal r74_CRM_NOT_COVERED_EXPOSURE) {
		R74_CRM_NOT_COVERED_EXPOSURE = r74_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR74_CRM_RISK_WEIGHT() {
		return R74_CRM_RISK_WEIGHT;
	}
	public void setR74_CRM_RISK_WEIGHT(BigDecimal r74_CRM_RISK_WEIGHT) {
		R74_CRM_RISK_WEIGHT = r74_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR74_RWA_CRM_COVERED() {
		return R74_RWA_CRM_COVERED;
	}
	public void setR74_RWA_CRM_COVERED(BigDecimal r74_RWA_CRM_COVERED) {
		R74_RWA_CRM_COVERED = r74_RWA_CRM_COVERED;
	}
	public BigDecimal getR74_ORIG_COUNTERPARTY_RW() {
		return R74_ORIG_COUNTERPARTY_RW;
	}
	public void setR74_ORIG_COUNTERPARTY_RW(BigDecimal r74_ORIG_COUNTERPARTY_RW) {
		R74_ORIG_COUNTERPARTY_RW = r74_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR74_RWA_CRM_NOT_COVERED() {
		return R74_RWA_CRM_NOT_COVERED;
	}
	public void setR74_RWA_CRM_NOT_COVERED(BigDecimal r74_RWA_CRM_NOT_COVERED) {
		R74_RWA_CRM_NOT_COVERED = r74_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR74_CRM_ELIG_EXPOSURE_COMP() {
		return R74_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR74_CRM_ELIG_EXPOSURE_COMP(BigDecimal r74_CRM_ELIG_EXPOSURE_COMP) {
		R74_CRM_ELIG_EXPOSURE_COMP = r74_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR74_EXPOSURE_AFTER_VOL_ADJ() {
		return R74_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR74_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r74_EXPOSURE_AFTER_VOL_ADJ) {
		R74_EXPOSURE_AFTER_VOL_ADJ = r74_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR74_COLLATERAL_CASH() {
		return R74_COLLATERAL_CASH;
	}
	public void setR74_COLLATERAL_CASH(BigDecimal r74_COLLATERAL_CASH) {
		R74_COLLATERAL_CASH = r74_COLLATERAL_CASH;
	}
	public BigDecimal getR74_COLLATERAL_TBILLS() {
		return R74_COLLATERAL_TBILLS;
	}
	public void setR74_COLLATERAL_TBILLS(BigDecimal r74_COLLATERAL_TBILLS) {
		R74_COLLATERAL_TBILLS = r74_COLLATERAL_TBILLS;
	}
	public BigDecimal getR74_COLLATERAL_DEBT_SEC() {
		return R74_COLLATERAL_DEBT_SEC;
	}
	public void setR74_COLLATERAL_DEBT_SEC(BigDecimal r74_COLLATERAL_DEBT_SEC) {
		R74_COLLATERAL_DEBT_SEC = r74_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR74_COLLATERAL_EQUITIES() {
		return R74_COLLATERAL_EQUITIES;
	}
	public void setR74_COLLATERAL_EQUITIES(BigDecimal r74_COLLATERAL_EQUITIES) {
		R74_COLLATERAL_EQUITIES = r74_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR74_COLLATERAL_MUTUAL_FUNDS() {
		return R74_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR74_COLLATERAL_MUTUAL_FUNDS(BigDecimal r74_COLLATERAL_MUTUAL_FUNDS) {
		R74_COLLATERAL_MUTUAL_FUNDS = r74_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR74_TOTAL_COLLATERAL_HAIRCUT() {
		return R74_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR74_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r74_TOTAL_COLLATERAL_HAIRCUT) {
		R74_TOTAL_COLLATERAL_HAIRCUT = r74_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR74_EXPOSURE_AFTER_CRM() {
		return R74_EXPOSURE_AFTER_CRM;
	}
	public void setR74_EXPOSURE_AFTER_CRM(BigDecimal r74_EXPOSURE_AFTER_CRM) {
		R74_EXPOSURE_AFTER_CRM = r74_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR74_RWA_NOT_COVERED_CRM() {
		return R74_RWA_NOT_COVERED_CRM;
	}
	public void setR74_RWA_NOT_COVERED_CRM(BigDecimal r74_RWA_NOT_COVERED_CRM) {
		R74_RWA_NOT_COVERED_CRM = r74_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR74_RWA_UNSECURED_EXPOSURE() {
		return R74_RWA_UNSECURED_EXPOSURE;
	}
	public void setR74_RWA_UNSECURED_EXPOSURE(BigDecimal r74_RWA_UNSECURED_EXPOSURE) {
		R74_RWA_UNSECURED_EXPOSURE = r74_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR74_RWA_UNSECURED() {
		return R74_RWA_UNSECURED;
	}
	public void setR74_RWA_UNSECURED(BigDecimal r74_RWA_UNSECURED) {
		R74_RWA_UNSECURED = r74_RWA_UNSECURED;
	}
	public BigDecimal getR74_TOTAL_RWA() {
		return R74_TOTAL_RWA;
	}
	public void setR74_TOTAL_RWA(BigDecimal r74_TOTAL_RWA) {
		R74_TOTAL_RWA = r74_TOTAL_RWA;
	}
	public BigDecimal getR75_EXPOSURE_BEFORE_CRM() {
		return R75_EXPOSURE_BEFORE_CRM;
	}
	public void setR75_EXPOSURE_BEFORE_CRM(BigDecimal r75_EXPOSURE_BEFORE_CRM) {
		R75_EXPOSURE_BEFORE_CRM = r75_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR75_SPEC_PROVISION_PAST_DUE() {
		return R75_SPEC_PROVISION_PAST_DUE;
	}
	public void setR75_SPEC_PROVISION_PAST_DUE(BigDecimal r75_SPEC_PROVISION_PAST_DUE) {
		R75_SPEC_PROVISION_PAST_DUE = r75_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR75_ON_BAL_SHEET_NETTING_ELIG() {
		return R75_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR75_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r75_ON_BAL_SHEET_NETTING_ELIG) {
		R75_ON_BAL_SHEET_NETTING_ELIG = r75_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR75_TOTAL_EXPOSURE_AFTER_NET() {
		return R75_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR75_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r75_TOTAL_EXPOSURE_AFTER_NET) {
		R75_TOTAL_EXPOSURE_AFTER_NET = r75_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR75_CRM_ELIG_EXPOSURE_SUBS() {
		return R75_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR75_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r75_CRM_ELIG_EXPOSURE_SUBS) {
		R75_CRM_ELIG_EXPOSURE_SUBS = r75_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR75_ELIG_GUARANTEES() {
		return R75_ELIG_GUARANTEES;
	}
	public void setR75_ELIG_GUARANTEES(BigDecimal r75_ELIG_GUARANTEES) {
		R75_ELIG_GUARANTEES = r75_ELIG_GUARANTEES;
	}
	public BigDecimal getR75_CREDIT_DERIVATIVES() {
		return R75_CREDIT_DERIVATIVES;
	}
	public void setR75_CREDIT_DERIVATIVES(BigDecimal r75_CREDIT_DERIVATIVES) {
		R75_CREDIT_DERIVATIVES = r75_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR75_CRM_COVERED_EXPOSURE() {
		return R75_CRM_COVERED_EXPOSURE;
	}
	public void setR75_CRM_COVERED_EXPOSURE(BigDecimal r75_CRM_COVERED_EXPOSURE) {
		R75_CRM_COVERED_EXPOSURE = r75_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR75_CRM_NOT_COVERED_EXPOSURE() {
		return R75_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR75_CRM_NOT_COVERED_EXPOSURE(BigDecimal r75_CRM_NOT_COVERED_EXPOSURE) {
		R75_CRM_NOT_COVERED_EXPOSURE = r75_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR75_CRM_RISK_WEIGHT() {
		return R75_CRM_RISK_WEIGHT;
	}
	public void setR75_CRM_RISK_WEIGHT(BigDecimal r75_CRM_RISK_WEIGHT) {
		R75_CRM_RISK_WEIGHT = r75_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR75_RWA_CRM_COVERED() {
		return R75_RWA_CRM_COVERED;
	}
	public void setR75_RWA_CRM_COVERED(BigDecimal r75_RWA_CRM_COVERED) {
		R75_RWA_CRM_COVERED = r75_RWA_CRM_COVERED;
	}
	public BigDecimal getR75_ORIG_COUNTERPARTY_RW() {
		return R75_ORIG_COUNTERPARTY_RW;
	}
	public void setR75_ORIG_COUNTERPARTY_RW(BigDecimal r75_ORIG_COUNTERPARTY_RW) {
		R75_ORIG_COUNTERPARTY_RW = r75_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR75_RWA_CRM_NOT_COVERED() {
		return R75_RWA_CRM_NOT_COVERED;
	}
	public void setR75_RWA_CRM_NOT_COVERED(BigDecimal r75_RWA_CRM_NOT_COVERED) {
		R75_RWA_CRM_NOT_COVERED = r75_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR75_CRM_ELIG_EXPOSURE_COMP() {
		return R75_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR75_CRM_ELIG_EXPOSURE_COMP(BigDecimal r75_CRM_ELIG_EXPOSURE_COMP) {
		R75_CRM_ELIG_EXPOSURE_COMP = r75_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR75_EXPOSURE_AFTER_VOL_ADJ() {
		return R75_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR75_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r75_EXPOSURE_AFTER_VOL_ADJ) {
		R75_EXPOSURE_AFTER_VOL_ADJ = r75_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR75_COLLATERAL_CASH() {
		return R75_COLLATERAL_CASH;
	}
	public void setR75_COLLATERAL_CASH(BigDecimal r75_COLLATERAL_CASH) {
		R75_COLLATERAL_CASH = r75_COLLATERAL_CASH;
	}
	public BigDecimal getR75_COLLATERAL_TBILLS() {
		return R75_COLLATERAL_TBILLS;
	}
	public void setR75_COLLATERAL_TBILLS(BigDecimal r75_COLLATERAL_TBILLS) {
		R75_COLLATERAL_TBILLS = r75_COLLATERAL_TBILLS;
	}
	public BigDecimal getR75_COLLATERAL_DEBT_SEC() {
		return R75_COLLATERAL_DEBT_SEC;
	}
	public void setR75_COLLATERAL_DEBT_SEC(BigDecimal r75_COLLATERAL_DEBT_SEC) {
		R75_COLLATERAL_DEBT_SEC = r75_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR75_COLLATERAL_EQUITIES() {
		return R75_COLLATERAL_EQUITIES;
	}
	public void setR75_COLLATERAL_EQUITIES(BigDecimal r75_COLLATERAL_EQUITIES) {
		R75_COLLATERAL_EQUITIES = r75_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR75_COLLATERAL_MUTUAL_FUNDS() {
		return R75_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR75_COLLATERAL_MUTUAL_FUNDS(BigDecimal r75_COLLATERAL_MUTUAL_FUNDS) {
		R75_COLLATERAL_MUTUAL_FUNDS = r75_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR75_TOTAL_COLLATERAL_HAIRCUT() {
		return R75_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR75_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r75_TOTAL_COLLATERAL_HAIRCUT) {
		R75_TOTAL_COLLATERAL_HAIRCUT = r75_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR75_EXPOSURE_AFTER_CRM() {
		return R75_EXPOSURE_AFTER_CRM;
	}
	public void setR75_EXPOSURE_AFTER_CRM(BigDecimal r75_EXPOSURE_AFTER_CRM) {
		R75_EXPOSURE_AFTER_CRM = r75_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR75_RWA_NOT_COVERED_CRM() {
		return R75_RWA_NOT_COVERED_CRM;
	}
	public void setR75_RWA_NOT_COVERED_CRM(BigDecimal r75_RWA_NOT_COVERED_CRM) {
		R75_RWA_NOT_COVERED_CRM = r75_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR75_RWA_UNSECURED_EXPOSURE() {
		return R75_RWA_UNSECURED_EXPOSURE;
	}
	public void setR75_RWA_UNSECURED_EXPOSURE(BigDecimal r75_RWA_UNSECURED_EXPOSURE) {
		R75_RWA_UNSECURED_EXPOSURE = r75_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR75_RWA_UNSECURED() {
		return R75_RWA_UNSECURED;
	}
	public void setR75_RWA_UNSECURED(BigDecimal r75_RWA_UNSECURED) {
		R75_RWA_UNSECURED = r75_RWA_UNSECURED;
	}
	public BigDecimal getR75_TOTAL_RWA() {
		return R75_TOTAL_RWA;
	}
	public void setR75_TOTAL_RWA(BigDecimal r75_TOTAL_RWA) {
		R75_TOTAL_RWA = r75_TOTAL_RWA;
	}
	public BigDecimal getR76_EXPOSURE_BEFORE_CRM() {
		return R76_EXPOSURE_BEFORE_CRM;
	}
	public void setR76_EXPOSURE_BEFORE_CRM(BigDecimal r76_EXPOSURE_BEFORE_CRM) {
		R76_EXPOSURE_BEFORE_CRM = r76_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR76_SPEC_PROVISION_PAST_DUE() {
		return R76_SPEC_PROVISION_PAST_DUE;
	}
	public void setR76_SPEC_PROVISION_PAST_DUE(BigDecimal r76_SPEC_PROVISION_PAST_DUE) {
		R76_SPEC_PROVISION_PAST_DUE = r76_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR76_ON_BAL_SHEET_NETTING_ELIG() {
		return R76_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR76_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r76_ON_BAL_SHEET_NETTING_ELIG) {
		R76_ON_BAL_SHEET_NETTING_ELIG = r76_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR76_TOTAL_EXPOSURE_AFTER_NET() {
		return R76_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR76_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r76_TOTAL_EXPOSURE_AFTER_NET) {
		R76_TOTAL_EXPOSURE_AFTER_NET = r76_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR76_CRM_ELIG_EXPOSURE_SUBS() {
		return R76_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR76_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r76_CRM_ELIG_EXPOSURE_SUBS) {
		R76_CRM_ELIG_EXPOSURE_SUBS = r76_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR76_ELIG_GUARANTEES() {
		return R76_ELIG_GUARANTEES;
	}
	public void setR76_ELIG_GUARANTEES(BigDecimal r76_ELIG_GUARANTEES) {
		R76_ELIG_GUARANTEES = r76_ELIG_GUARANTEES;
	}
	public BigDecimal getR76_CREDIT_DERIVATIVES() {
		return R76_CREDIT_DERIVATIVES;
	}
	public void setR76_CREDIT_DERIVATIVES(BigDecimal r76_CREDIT_DERIVATIVES) {
		R76_CREDIT_DERIVATIVES = r76_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR76_CRM_COVERED_EXPOSURE() {
		return R76_CRM_COVERED_EXPOSURE;
	}
	public void setR76_CRM_COVERED_EXPOSURE(BigDecimal r76_CRM_COVERED_EXPOSURE) {
		R76_CRM_COVERED_EXPOSURE = r76_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR76_CRM_NOT_COVERED_EXPOSURE() {
		return R76_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR76_CRM_NOT_COVERED_EXPOSURE(BigDecimal r76_CRM_NOT_COVERED_EXPOSURE) {
		R76_CRM_NOT_COVERED_EXPOSURE = r76_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR76_CRM_RISK_WEIGHT() {
		return R76_CRM_RISK_WEIGHT;
	}
	public void setR76_CRM_RISK_WEIGHT(BigDecimal r76_CRM_RISK_WEIGHT) {
		R76_CRM_RISK_WEIGHT = r76_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR76_RWA_CRM_COVERED() {
		return R76_RWA_CRM_COVERED;
	}
	public void setR76_RWA_CRM_COVERED(BigDecimal r76_RWA_CRM_COVERED) {
		R76_RWA_CRM_COVERED = r76_RWA_CRM_COVERED;
	}
	public BigDecimal getR76_ORIG_COUNTERPARTY_RW() {
		return R76_ORIG_COUNTERPARTY_RW;
	}
	public void setR76_ORIG_COUNTERPARTY_RW(BigDecimal r76_ORIG_COUNTERPARTY_RW) {
		R76_ORIG_COUNTERPARTY_RW = r76_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR76_RWA_CRM_NOT_COVERED() {
		return R76_RWA_CRM_NOT_COVERED;
	}
	public void setR76_RWA_CRM_NOT_COVERED(BigDecimal r76_RWA_CRM_NOT_COVERED) {
		R76_RWA_CRM_NOT_COVERED = r76_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR76_CRM_ELIG_EXPOSURE_COMP() {
		return R76_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR76_CRM_ELIG_EXPOSURE_COMP(BigDecimal r76_CRM_ELIG_EXPOSURE_COMP) {
		R76_CRM_ELIG_EXPOSURE_COMP = r76_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR76_EXPOSURE_AFTER_VOL_ADJ() {
		return R76_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR76_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r76_EXPOSURE_AFTER_VOL_ADJ) {
		R76_EXPOSURE_AFTER_VOL_ADJ = r76_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR76_COLLATERAL_CASH() {
		return R76_COLLATERAL_CASH;
	}
	public void setR76_COLLATERAL_CASH(BigDecimal r76_COLLATERAL_CASH) {
		R76_COLLATERAL_CASH = r76_COLLATERAL_CASH;
	}
	public BigDecimal getR76_COLLATERAL_TBILLS() {
		return R76_COLLATERAL_TBILLS;
	}
	public void setR76_COLLATERAL_TBILLS(BigDecimal r76_COLLATERAL_TBILLS) {
		R76_COLLATERAL_TBILLS = r76_COLLATERAL_TBILLS;
	}
	public BigDecimal getR76_COLLATERAL_DEBT_SEC() {
		return R76_COLLATERAL_DEBT_SEC;
	}
	public void setR76_COLLATERAL_DEBT_SEC(BigDecimal r76_COLLATERAL_DEBT_SEC) {
		R76_COLLATERAL_DEBT_SEC = r76_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR76_COLLATERAL_EQUITIES() {
		return R76_COLLATERAL_EQUITIES;
	}
	public void setR76_COLLATERAL_EQUITIES(BigDecimal r76_COLLATERAL_EQUITIES) {
		R76_COLLATERAL_EQUITIES = r76_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR76_COLLATERAL_MUTUAL_FUNDS() {
		return R76_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR76_COLLATERAL_MUTUAL_FUNDS(BigDecimal r76_COLLATERAL_MUTUAL_FUNDS) {
		R76_COLLATERAL_MUTUAL_FUNDS = r76_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR76_TOTAL_COLLATERAL_HAIRCUT() {
		return R76_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR76_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r76_TOTAL_COLLATERAL_HAIRCUT) {
		R76_TOTAL_COLLATERAL_HAIRCUT = r76_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR76_EXPOSURE_AFTER_CRM() {
		return R76_EXPOSURE_AFTER_CRM;
	}
	public void setR76_EXPOSURE_AFTER_CRM(BigDecimal r76_EXPOSURE_AFTER_CRM) {
		R76_EXPOSURE_AFTER_CRM = r76_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR76_RWA_NOT_COVERED_CRM() {
		return R76_RWA_NOT_COVERED_CRM;
	}
	public void setR76_RWA_NOT_COVERED_CRM(BigDecimal r76_RWA_NOT_COVERED_CRM) {
		R76_RWA_NOT_COVERED_CRM = r76_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR76_RWA_UNSECURED_EXPOSURE() {
		return R76_RWA_UNSECURED_EXPOSURE;
	}
	public void setR76_RWA_UNSECURED_EXPOSURE(BigDecimal r76_RWA_UNSECURED_EXPOSURE) {
		R76_RWA_UNSECURED_EXPOSURE = r76_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR76_RWA_UNSECURED() {
		return R76_RWA_UNSECURED;
	}
	public void setR76_RWA_UNSECURED(BigDecimal r76_RWA_UNSECURED) {
		R76_RWA_UNSECURED = r76_RWA_UNSECURED;
	}
	public BigDecimal getR76_TOTAL_RWA() {
		return R76_TOTAL_RWA;
	}
	public void setR76_TOTAL_RWA(BigDecimal r76_TOTAL_RWA) {
		R76_TOTAL_RWA = r76_TOTAL_RWA;
	}
	public BigDecimal getR77_EXPOSURE_BEFORE_CRM() {
		return R77_EXPOSURE_BEFORE_CRM;
	}
	public void setR77_EXPOSURE_BEFORE_CRM(BigDecimal r77_EXPOSURE_BEFORE_CRM) {
		R77_EXPOSURE_BEFORE_CRM = r77_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR77_SPEC_PROVISION_PAST_DUE() {
		return R77_SPEC_PROVISION_PAST_DUE;
	}
	public void setR77_SPEC_PROVISION_PAST_DUE(BigDecimal r77_SPEC_PROVISION_PAST_DUE) {
		R77_SPEC_PROVISION_PAST_DUE = r77_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR77_ON_BAL_SHEET_NETTING_ELIG() {
		return R77_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR77_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r77_ON_BAL_SHEET_NETTING_ELIG) {
		R77_ON_BAL_SHEET_NETTING_ELIG = r77_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR77_TOTAL_EXPOSURE_AFTER_NET() {
		return R77_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR77_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r77_TOTAL_EXPOSURE_AFTER_NET) {
		R77_TOTAL_EXPOSURE_AFTER_NET = r77_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR77_CRM_ELIG_EXPOSURE_SUBS() {
		return R77_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR77_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r77_CRM_ELIG_EXPOSURE_SUBS) {
		R77_CRM_ELIG_EXPOSURE_SUBS = r77_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR77_ELIG_GUARANTEES() {
		return R77_ELIG_GUARANTEES;
	}
	public void setR77_ELIG_GUARANTEES(BigDecimal r77_ELIG_GUARANTEES) {
		R77_ELIG_GUARANTEES = r77_ELIG_GUARANTEES;
	}
	public BigDecimal getR77_CREDIT_DERIVATIVES() {
		return R77_CREDIT_DERIVATIVES;
	}
	public void setR77_CREDIT_DERIVATIVES(BigDecimal r77_CREDIT_DERIVATIVES) {
		R77_CREDIT_DERIVATIVES = r77_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR77_CRM_COVERED_EXPOSURE() {
		return R77_CRM_COVERED_EXPOSURE;
	}
	public void setR77_CRM_COVERED_EXPOSURE(BigDecimal r77_CRM_COVERED_EXPOSURE) {
		R77_CRM_COVERED_EXPOSURE = r77_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR77_CRM_NOT_COVERED_EXPOSURE() {
		return R77_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR77_CRM_NOT_COVERED_EXPOSURE(BigDecimal r77_CRM_NOT_COVERED_EXPOSURE) {
		R77_CRM_NOT_COVERED_EXPOSURE = r77_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR77_CRM_RISK_WEIGHT() {
		return R77_CRM_RISK_WEIGHT;
	}
	public void setR77_CRM_RISK_WEIGHT(BigDecimal r77_CRM_RISK_WEIGHT) {
		R77_CRM_RISK_WEIGHT = r77_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR77_RWA_CRM_COVERED() {
		return R77_RWA_CRM_COVERED;
	}
	public void setR77_RWA_CRM_COVERED(BigDecimal r77_RWA_CRM_COVERED) {
		R77_RWA_CRM_COVERED = r77_RWA_CRM_COVERED;
	}
	public BigDecimal getR77_ORIG_COUNTERPARTY_RW() {
		return R77_ORIG_COUNTERPARTY_RW;
	}
	public void setR77_ORIG_COUNTERPARTY_RW(BigDecimal r77_ORIG_COUNTERPARTY_RW) {
		R77_ORIG_COUNTERPARTY_RW = r77_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR77_RWA_CRM_NOT_COVERED() {
		return R77_RWA_CRM_NOT_COVERED;
	}
	public void setR77_RWA_CRM_NOT_COVERED(BigDecimal r77_RWA_CRM_NOT_COVERED) {
		R77_RWA_CRM_NOT_COVERED = r77_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR77_CRM_ELIG_EXPOSURE_COMP() {
		return R77_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR77_CRM_ELIG_EXPOSURE_COMP(BigDecimal r77_CRM_ELIG_EXPOSURE_COMP) {
		R77_CRM_ELIG_EXPOSURE_COMP = r77_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR77_EXPOSURE_AFTER_VOL_ADJ() {
		return R77_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR77_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r77_EXPOSURE_AFTER_VOL_ADJ) {
		R77_EXPOSURE_AFTER_VOL_ADJ = r77_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR77_COLLATERAL_CASH() {
		return R77_COLLATERAL_CASH;
	}
	public void setR77_COLLATERAL_CASH(BigDecimal r77_COLLATERAL_CASH) {
		R77_COLLATERAL_CASH = r77_COLLATERAL_CASH;
	}
	public BigDecimal getR77_COLLATERAL_TBILLS() {
		return R77_COLLATERAL_TBILLS;
	}
	public void setR77_COLLATERAL_TBILLS(BigDecimal r77_COLLATERAL_TBILLS) {
		R77_COLLATERAL_TBILLS = r77_COLLATERAL_TBILLS;
	}
	public BigDecimal getR77_COLLATERAL_DEBT_SEC() {
		return R77_COLLATERAL_DEBT_SEC;
	}
	public void setR77_COLLATERAL_DEBT_SEC(BigDecimal r77_COLLATERAL_DEBT_SEC) {
		R77_COLLATERAL_DEBT_SEC = r77_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR77_COLLATERAL_EQUITIES() {
		return R77_COLLATERAL_EQUITIES;
	}
	public void setR77_COLLATERAL_EQUITIES(BigDecimal r77_COLLATERAL_EQUITIES) {
		R77_COLLATERAL_EQUITIES = r77_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR77_COLLATERAL_MUTUAL_FUNDS() {
		return R77_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR77_COLLATERAL_MUTUAL_FUNDS(BigDecimal r77_COLLATERAL_MUTUAL_FUNDS) {
		R77_COLLATERAL_MUTUAL_FUNDS = r77_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR77_TOTAL_COLLATERAL_HAIRCUT() {
		return R77_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR77_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r77_TOTAL_COLLATERAL_HAIRCUT) {
		R77_TOTAL_COLLATERAL_HAIRCUT = r77_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR77_EXPOSURE_AFTER_CRM() {
		return R77_EXPOSURE_AFTER_CRM;
	}
	public void setR77_EXPOSURE_AFTER_CRM(BigDecimal r77_EXPOSURE_AFTER_CRM) {
		R77_EXPOSURE_AFTER_CRM = r77_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR77_RWA_NOT_COVERED_CRM() {
		return R77_RWA_NOT_COVERED_CRM;
	}
	public void setR77_RWA_NOT_COVERED_CRM(BigDecimal r77_RWA_NOT_COVERED_CRM) {
		R77_RWA_NOT_COVERED_CRM = r77_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR77_RWA_UNSECURED_EXPOSURE() {
		return R77_RWA_UNSECURED_EXPOSURE;
	}
	public void setR77_RWA_UNSECURED_EXPOSURE(BigDecimal r77_RWA_UNSECURED_EXPOSURE) {
		R77_RWA_UNSECURED_EXPOSURE = r77_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR77_RWA_UNSECURED() {
		return R77_RWA_UNSECURED;
	}
	public void setR77_RWA_UNSECURED(BigDecimal r77_RWA_UNSECURED) {
		R77_RWA_UNSECURED = r77_RWA_UNSECURED;
	}
	public BigDecimal getR77_TOTAL_RWA() {
		return R77_TOTAL_RWA;
	}
	public void setR77_TOTAL_RWA(BigDecimal r77_TOTAL_RWA) {
		R77_TOTAL_RWA = r77_TOTAL_RWA;
	}
	public BigDecimal getR78_EXPOSURE_BEFORE_CRM() {
		return R78_EXPOSURE_BEFORE_CRM;
	}
	public void setR78_EXPOSURE_BEFORE_CRM(BigDecimal r78_EXPOSURE_BEFORE_CRM) {
		R78_EXPOSURE_BEFORE_CRM = r78_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR78_SPEC_PROVISION_PAST_DUE() {
		return R78_SPEC_PROVISION_PAST_DUE;
	}
	public void setR78_SPEC_PROVISION_PAST_DUE(BigDecimal r78_SPEC_PROVISION_PAST_DUE) {
		R78_SPEC_PROVISION_PAST_DUE = r78_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR78_ON_BAL_SHEET_NETTING_ELIG() {
		return R78_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR78_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r78_ON_BAL_SHEET_NETTING_ELIG) {
		R78_ON_BAL_SHEET_NETTING_ELIG = r78_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR78_TOTAL_EXPOSURE_AFTER_NET() {
		return R78_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR78_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r78_TOTAL_EXPOSURE_AFTER_NET) {
		R78_TOTAL_EXPOSURE_AFTER_NET = r78_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR78_CRM_ELIG_EXPOSURE_SUBS() {
		return R78_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR78_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r78_CRM_ELIG_EXPOSURE_SUBS) {
		R78_CRM_ELIG_EXPOSURE_SUBS = r78_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR78_ELIG_GUARANTEES() {
		return R78_ELIG_GUARANTEES;
	}
	public void setR78_ELIG_GUARANTEES(BigDecimal r78_ELIG_GUARANTEES) {
		R78_ELIG_GUARANTEES = r78_ELIG_GUARANTEES;
	}
	public BigDecimal getR78_CREDIT_DERIVATIVES() {
		return R78_CREDIT_DERIVATIVES;
	}
	public void setR78_CREDIT_DERIVATIVES(BigDecimal r78_CREDIT_DERIVATIVES) {
		R78_CREDIT_DERIVATIVES = r78_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR78_CRM_COVERED_EXPOSURE() {
		return R78_CRM_COVERED_EXPOSURE;
	}
	public void setR78_CRM_COVERED_EXPOSURE(BigDecimal r78_CRM_COVERED_EXPOSURE) {
		R78_CRM_COVERED_EXPOSURE = r78_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR78_CRM_NOT_COVERED_EXPOSURE() {
		return R78_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR78_CRM_NOT_COVERED_EXPOSURE(BigDecimal r78_CRM_NOT_COVERED_EXPOSURE) {
		R78_CRM_NOT_COVERED_EXPOSURE = r78_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR78_CRM_RISK_WEIGHT() {
		return R78_CRM_RISK_WEIGHT;
	}
	public void setR78_CRM_RISK_WEIGHT(BigDecimal r78_CRM_RISK_WEIGHT) {
		R78_CRM_RISK_WEIGHT = r78_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR78_RWA_CRM_COVERED() {
		return R78_RWA_CRM_COVERED;
	}
	public void setR78_RWA_CRM_COVERED(BigDecimal r78_RWA_CRM_COVERED) {
		R78_RWA_CRM_COVERED = r78_RWA_CRM_COVERED;
	}
	public BigDecimal getR78_ORIG_COUNTERPARTY_RW() {
		return R78_ORIG_COUNTERPARTY_RW;
	}
	public void setR78_ORIG_COUNTERPARTY_RW(BigDecimal r78_ORIG_COUNTERPARTY_RW) {
		R78_ORIG_COUNTERPARTY_RW = r78_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR78_RWA_CRM_NOT_COVERED() {
		return R78_RWA_CRM_NOT_COVERED;
	}
	public void setR78_RWA_CRM_NOT_COVERED(BigDecimal r78_RWA_CRM_NOT_COVERED) {
		R78_RWA_CRM_NOT_COVERED = r78_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR78_CRM_ELIG_EXPOSURE_COMP() {
		return R78_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR78_CRM_ELIG_EXPOSURE_COMP(BigDecimal r78_CRM_ELIG_EXPOSURE_COMP) {
		R78_CRM_ELIG_EXPOSURE_COMP = r78_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR78_EXPOSURE_AFTER_VOL_ADJ() {
		return R78_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR78_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r78_EXPOSURE_AFTER_VOL_ADJ) {
		R78_EXPOSURE_AFTER_VOL_ADJ = r78_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR78_COLLATERAL_CASH() {
		return R78_COLLATERAL_CASH;
	}
	public void setR78_COLLATERAL_CASH(BigDecimal r78_COLLATERAL_CASH) {
		R78_COLLATERAL_CASH = r78_COLLATERAL_CASH;
	}
	public BigDecimal getR78_COLLATERAL_TBILLS() {
		return R78_COLLATERAL_TBILLS;
	}
	public void setR78_COLLATERAL_TBILLS(BigDecimal r78_COLLATERAL_TBILLS) {
		R78_COLLATERAL_TBILLS = r78_COLLATERAL_TBILLS;
	}
	public BigDecimal getR78_COLLATERAL_DEBT_SEC() {
		return R78_COLLATERAL_DEBT_SEC;
	}
	public void setR78_COLLATERAL_DEBT_SEC(BigDecimal r78_COLLATERAL_DEBT_SEC) {
		R78_COLLATERAL_DEBT_SEC = r78_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR78_COLLATERAL_EQUITIES() {
		return R78_COLLATERAL_EQUITIES;
	}
	public void setR78_COLLATERAL_EQUITIES(BigDecimal r78_COLLATERAL_EQUITIES) {
		R78_COLLATERAL_EQUITIES = r78_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR78_COLLATERAL_MUTUAL_FUNDS() {
		return R78_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR78_COLLATERAL_MUTUAL_FUNDS(BigDecimal r78_COLLATERAL_MUTUAL_FUNDS) {
		R78_COLLATERAL_MUTUAL_FUNDS = r78_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR78_TOTAL_COLLATERAL_HAIRCUT() {
		return R78_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR78_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r78_TOTAL_COLLATERAL_HAIRCUT) {
		R78_TOTAL_COLLATERAL_HAIRCUT = r78_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR78_EXPOSURE_AFTER_CRM() {
		return R78_EXPOSURE_AFTER_CRM;
	}
	public void setR78_EXPOSURE_AFTER_CRM(BigDecimal r78_EXPOSURE_AFTER_CRM) {
		R78_EXPOSURE_AFTER_CRM = r78_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR78_RWA_NOT_COVERED_CRM() {
		return R78_RWA_NOT_COVERED_CRM;
	}
	public void setR78_RWA_NOT_COVERED_CRM(BigDecimal r78_RWA_NOT_COVERED_CRM) {
		R78_RWA_NOT_COVERED_CRM = r78_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR78_RWA_UNSECURED_EXPOSURE() {
		return R78_RWA_UNSECURED_EXPOSURE;
	}
	public void setR78_RWA_UNSECURED_EXPOSURE(BigDecimal r78_RWA_UNSECURED_EXPOSURE) {
		R78_RWA_UNSECURED_EXPOSURE = r78_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR78_RWA_UNSECURED() {
		return R78_RWA_UNSECURED;
	}
	public void setR78_RWA_UNSECURED(BigDecimal r78_RWA_UNSECURED) {
		R78_RWA_UNSECURED = r78_RWA_UNSECURED;
	}
	public BigDecimal getR78_TOTAL_RWA() {
		return R78_TOTAL_RWA;
	}
	public void setR78_TOTAL_RWA(BigDecimal r78_TOTAL_RWA) {
		R78_TOTAL_RWA = r78_TOTAL_RWA;
	}
	public BigDecimal getR79_EXPOSURE_BEFORE_CRM() {
		return R79_EXPOSURE_BEFORE_CRM;
	}
	public void setR79_EXPOSURE_BEFORE_CRM(BigDecimal r79_EXPOSURE_BEFORE_CRM) {
		R79_EXPOSURE_BEFORE_CRM = r79_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR79_SPEC_PROVISION_PAST_DUE() {
		return R79_SPEC_PROVISION_PAST_DUE;
	}
	public void setR79_SPEC_PROVISION_PAST_DUE(BigDecimal r79_SPEC_PROVISION_PAST_DUE) {
		R79_SPEC_PROVISION_PAST_DUE = r79_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR79_ON_BAL_SHEET_NETTING_ELIG() {
		return R79_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR79_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r79_ON_BAL_SHEET_NETTING_ELIG) {
		R79_ON_BAL_SHEET_NETTING_ELIG = r79_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR79_TOTAL_EXPOSURE_AFTER_NET() {
		return R79_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR79_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r79_TOTAL_EXPOSURE_AFTER_NET) {
		R79_TOTAL_EXPOSURE_AFTER_NET = r79_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR79_CRM_ELIG_EXPOSURE_SUBS() {
		return R79_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR79_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r79_CRM_ELIG_EXPOSURE_SUBS) {
		R79_CRM_ELIG_EXPOSURE_SUBS = r79_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR79_ELIG_GUARANTEES() {
		return R79_ELIG_GUARANTEES;
	}
	public void setR79_ELIG_GUARANTEES(BigDecimal r79_ELIG_GUARANTEES) {
		R79_ELIG_GUARANTEES = r79_ELIG_GUARANTEES;
	}
	public BigDecimal getR79_CREDIT_DERIVATIVES() {
		return R79_CREDIT_DERIVATIVES;
	}
	public void setR79_CREDIT_DERIVATIVES(BigDecimal r79_CREDIT_DERIVATIVES) {
		R79_CREDIT_DERIVATIVES = r79_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR79_CRM_COVERED_EXPOSURE() {
		return R79_CRM_COVERED_EXPOSURE;
	}
	public void setR79_CRM_COVERED_EXPOSURE(BigDecimal r79_CRM_COVERED_EXPOSURE) {
		R79_CRM_COVERED_EXPOSURE = r79_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR79_CRM_NOT_COVERED_EXPOSURE() {
		return R79_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR79_CRM_NOT_COVERED_EXPOSURE(BigDecimal r79_CRM_NOT_COVERED_EXPOSURE) {
		R79_CRM_NOT_COVERED_EXPOSURE = r79_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR79_CRM_RISK_WEIGHT() {
		return R79_CRM_RISK_WEIGHT;
	}
	public void setR79_CRM_RISK_WEIGHT(BigDecimal r79_CRM_RISK_WEIGHT) {
		R79_CRM_RISK_WEIGHT = r79_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR79_RWA_CRM_COVERED() {
		return R79_RWA_CRM_COVERED;
	}
	public void setR79_RWA_CRM_COVERED(BigDecimal r79_RWA_CRM_COVERED) {
		R79_RWA_CRM_COVERED = r79_RWA_CRM_COVERED;
	}
	public BigDecimal getR79_ORIG_COUNTERPARTY_RW() {
		return R79_ORIG_COUNTERPARTY_RW;
	}
	public void setR79_ORIG_COUNTERPARTY_RW(BigDecimal r79_ORIG_COUNTERPARTY_RW) {
		R79_ORIG_COUNTERPARTY_RW = r79_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR79_RWA_CRM_NOT_COVERED() {
		return R79_RWA_CRM_NOT_COVERED;
	}
	public void setR79_RWA_CRM_NOT_COVERED(BigDecimal r79_RWA_CRM_NOT_COVERED) {
		R79_RWA_CRM_NOT_COVERED = r79_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR79_CRM_ELIG_EXPOSURE_COMP() {
		return R79_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR79_CRM_ELIG_EXPOSURE_COMP(BigDecimal r79_CRM_ELIG_EXPOSURE_COMP) {
		R79_CRM_ELIG_EXPOSURE_COMP = r79_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR79_EXPOSURE_AFTER_VOL_ADJ() {
		return R79_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR79_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r79_EXPOSURE_AFTER_VOL_ADJ) {
		R79_EXPOSURE_AFTER_VOL_ADJ = r79_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR79_COLLATERAL_CASH() {
		return R79_COLLATERAL_CASH;
	}
	public void setR79_COLLATERAL_CASH(BigDecimal r79_COLLATERAL_CASH) {
		R79_COLLATERAL_CASH = r79_COLLATERAL_CASH;
	}
	public BigDecimal getR79_COLLATERAL_TBILLS() {
		return R79_COLLATERAL_TBILLS;
	}
	public void setR79_COLLATERAL_TBILLS(BigDecimal r79_COLLATERAL_TBILLS) {
		R79_COLLATERAL_TBILLS = r79_COLLATERAL_TBILLS;
	}
	public BigDecimal getR79_COLLATERAL_DEBT_SEC() {
		return R79_COLLATERAL_DEBT_SEC;
	}
	public void setR79_COLLATERAL_DEBT_SEC(BigDecimal r79_COLLATERAL_DEBT_SEC) {
		R79_COLLATERAL_DEBT_SEC = r79_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR79_COLLATERAL_EQUITIES() {
		return R79_COLLATERAL_EQUITIES;
	}
	public void setR79_COLLATERAL_EQUITIES(BigDecimal r79_COLLATERAL_EQUITIES) {
		R79_COLLATERAL_EQUITIES = r79_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR79_COLLATERAL_MUTUAL_FUNDS() {
		return R79_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR79_COLLATERAL_MUTUAL_FUNDS(BigDecimal r79_COLLATERAL_MUTUAL_FUNDS) {
		R79_COLLATERAL_MUTUAL_FUNDS = r79_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR79_TOTAL_COLLATERAL_HAIRCUT() {
		return R79_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR79_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r79_TOTAL_COLLATERAL_HAIRCUT) {
		R79_TOTAL_COLLATERAL_HAIRCUT = r79_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR79_EXPOSURE_AFTER_CRM() {
		return R79_EXPOSURE_AFTER_CRM;
	}
	public void setR79_EXPOSURE_AFTER_CRM(BigDecimal r79_EXPOSURE_AFTER_CRM) {
		R79_EXPOSURE_AFTER_CRM = r79_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR79_RWA_NOT_COVERED_CRM() {
		return R79_RWA_NOT_COVERED_CRM;
	}
	public void setR79_RWA_NOT_COVERED_CRM(BigDecimal r79_RWA_NOT_COVERED_CRM) {
		R79_RWA_NOT_COVERED_CRM = r79_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR79_RWA_UNSECURED_EXPOSURE() {
		return R79_RWA_UNSECURED_EXPOSURE;
	}
	public void setR79_RWA_UNSECURED_EXPOSURE(BigDecimal r79_RWA_UNSECURED_EXPOSURE) {
		R79_RWA_UNSECURED_EXPOSURE = r79_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR79_RWA_UNSECURED() {
		return R79_RWA_UNSECURED;
	}
	public void setR79_RWA_UNSECURED(BigDecimal r79_RWA_UNSECURED) {
		R79_RWA_UNSECURED = r79_RWA_UNSECURED;
	}
	public BigDecimal getR79_TOTAL_RWA() {
		return R79_TOTAL_RWA;
	}
	public void setR79_TOTAL_RWA(BigDecimal r79_TOTAL_RWA) {
		R79_TOTAL_RWA = r79_TOTAL_RWA;
	}
	public BigDecimal getR80_EXPOSURE_BEFORE_CRM() {
		return R80_EXPOSURE_BEFORE_CRM;
	}
	public void setR80_EXPOSURE_BEFORE_CRM(BigDecimal r80_EXPOSURE_BEFORE_CRM) {
		R80_EXPOSURE_BEFORE_CRM = r80_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR80_SPEC_PROVISION_PAST_DUE() {
		return R80_SPEC_PROVISION_PAST_DUE;
	}
	public void setR80_SPEC_PROVISION_PAST_DUE(BigDecimal r80_SPEC_PROVISION_PAST_DUE) {
		R80_SPEC_PROVISION_PAST_DUE = r80_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR80_ON_BAL_SHEET_NETTING_ELIG() {
		return R80_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR80_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r80_ON_BAL_SHEET_NETTING_ELIG) {
		R80_ON_BAL_SHEET_NETTING_ELIG = r80_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR80_TOTAL_EXPOSURE_AFTER_NET() {
		return R80_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR80_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r80_TOTAL_EXPOSURE_AFTER_NET) {
		R80_TOTAL_EXPOSURE_AFTER_NET = r80_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR80_CRM_ELIG_EXPOSURE_SUBS() {
		return R80_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR80_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r80_CRM_ELIG_EXPOSURE_SUBS) {
		R80_CRM_ELIG_EXPOSURE_SUBS = r80_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR80_ELIG_GUARANTEES() {
		return R80_ELIG_GUARANTEES;
	}
	public void setR80_ELIG_GUARANTEES(BigDecimal r80_ELIG_GUARANTEES) {
		R80_ELIG_GUARANTEES = r80_ELIG_GUARANTEES;
	}
	public BigDecimal getR80_CREDIT_DERIVATIVES() {
		return R80_CREDIT_DERIVATIVES;
	}
	public void setR80_CREDIT_DERIVATIVES(BigDecimal r80_CREDIT_DERIVATIVES) {
		R80_CREDIT_DERIVATIVES = r80_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR80_CRM_COVERED_EXPOSURE() {
		return R80_CRM_COVERED_EXPOSURE;
	}
	public void setR80_CRM_COVERED_EXPOSURE(BigDecimal r80_CRM_COVERED_EXPOSURE) {
		R80_CRM_COVERED_EXPOSURE = r80_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR80_CRM_NOT_COVERED_EXPOSURE() {
		return R80_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR80_CRM_NOT_COVERED_EXPOSURE(BigDecimal r80_CRM_NOT_COVERED_EXPOSURE) {
		R80_CRM_NOT_COVERED_EXPOSURE = r80_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR80_CRM_RISK_WEIGHT() {
		return R80_CRM_RISK_WEIGHT;
	}
	public void setR80_CRM_RISK_WEIGHT(BigDecimal r80_CRM_RISK_WEIGHT) {
		R80_CRM_RISK_WEIGHT = r80_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR80_RWA_CRM_COVERED() {
		return R80_RWA_CRM_COVERED;
	}
	public void setR80_RWA_CRM_COVERED(BigDecimal r80_RWA_CRM_COVERED) {
		R80_RWA_CRM_COVERED = r80_RWA_CRM_COVERED;
	}
	public BigDecimal getR80_ORIG_COUNTERPARTY_RW() {
		return R80_ORIG_COUNTERPARTY_RW;
	}
	public void setR80_ORIG_COUNTERPARTY_RW(BigDecimal r80_ORIG_COUNTERPARTY_RW) {
		R80_ORIG_COUNTERPARTY_RW = r80_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR80_RWA_CRM_NOT_COVERED() {
		return R80_RWA_CRM_NOT_COVERED;
	}
	public void setR80_RWA_CRM_NOT_COVERED(BigDecimal r80_RWA_CRM_NOT_COVERED) {
		R80_RWA_CRM_NOT_COVERED = r80_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR80_CRM_ELIG_EXPOSURE_COMP() {
		return R80_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR80_CRM_ELIG_EXPOSURE_COMP(BigDecimal r80_CRM_ELIG_EXPOSURE_COMP) {
		R80_CRM_ELIG_EXPOSURE_COMP = r80_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR80_EXPOSURE_AFTER_VOL_ADJ() {
		return R80_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR80_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r80_EXPOSURE_AFTER_VOL_ADJ) {
		R80_EXPOSURE_AFTER_VOL_ADJ = r80_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR80_COLLATERAL_CASH() {
		return R80_COLLATERAL_CASH;
	}
	public void setR80_COLLATERAL_CASH(BigDecimal r80_COLLATERAL_CASH) {
		R80_COLLATERAL_CASH = r80_COLLATERAL_CASH;
	}
	public BigDecimal getR80_COLLATERAL_TBILLS() {
		return R80_COLLATERAL_TBILLS;
	}
	public void setR80_COLLATERAL_TBILLS(BigDecimal r80_COLLATERAL_TBILLS) {
		R80_COLLATERAL_TBILLS = r80_COLLATERAL_TBILLS;
	}
	public BigDecimal getR80_COLLATERAL_DEBT_SEC() {
		return R80_COLLATERAL_DEBT_SEC;
	}
	public void setR80_COLLATERAL_DEBT_SEC(BigDecimal r80_COLLATERAL_DEBT_SEC) {
		R80_COLLATERAL_DEBT_SEC = r80_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR80_COLLATERAL_EQUITIES() {
		return R80_COLLATERAL_EQUITIES;
	}
	public void setR80_COLLATERAL_EQUITIES(BigDecimal r80_COLLATERAL_EQUITIES) {
		R80_COLLATERAL_EQUITIES = r80_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR80_COLLATERAL_MUTUAL_FUNDS() {
		return R80_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR80_COLLATERAL_MUTUAL_FUNDS(BigDecimal r80_COLLATERAL_MUTUAL_FUNDS) {
		R80_COLLATERAL_MUTUAL_FUNDS = r80_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR80_TOTAL_COLLATERAL_HAIRCUT() {
		return R80_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR80_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r80_TOTAL_COLLATERAL_HAIRCUT) {
		R80_TOTAL_COLLATERAL_HAIRCUT = r80_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR80_EXPOSURE_AFTER_CRM() {
		return R80_EXPOSURE_AFTER_CRM;
	}
	public void setR80_EXPOSURE_AFTER_CRM(BigDecimal r80_EXPOSURE_AFTER_CRM) {
		R80_EXPOSURE_AFTER_CRM = r80_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR80_RWA_NOT_COVERED_CRM() {
		return R80_RWA_NOT_COVERED_CRM;
	}
	public void setR80_RWA_NOT_COVERED_CRM(BigDecimal r80_RWA_NOT_COVERED_CRM) {
		R80_RWA_NOT_COVERED_CRM = r80_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR80_RWA_UNSECURED_EXPOSURE() {
		return R80_RWA_UNSECURED_EXPOSURE;
	}
	public void setR80_RWA_UNSECURED_EXPOSURE(BigDecimal r80_RWA_UNSECURED_EXPOSURE) {
		R80_RWA_UNSECURED_EXPOSURE = r80_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR80_RWA_UNSECURED() {
		return R80_RWA_UNSECURED;
	}
	public void setR80_RWA_UNSECURED(BigDecimal r80_RWA_UNSECURED) {
		R80_RWA_UNSECURED = r80_RWA_UNSECURED;
	}
	public BigDecimal getR80_TOTAL_RWA() {
		return R80_TOTAL_RWA;
	}
	public void setR80_TOTAL_RWA(BigDecimal r80_TOTAL_RWA) {
		R80_TOTAL_RWA = r80_TOTAL_RWA;
	}
	public BigDecimal getR81_EXPOSURE_BEFORE_CRM() {
		return R81_EXPOSURE_BEFORE_CRM;
	}
	public void setR81_EXPOSURE_BEFORE_CRM(BigDecimal r81_EXPOSURE_BEFORE_CRM) {
		R81_EXPOSURE_BEFORE_CRM = r81_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR81_SPEC_PROVISION_PAST_DUE() {
		return R81_SPEC_PROVISION_PAST_DUE;
	}
	public void setR81_SPEC_PROVISION_PAST_DUE(BigDecimal r81_SPEC_PROVISION_PAST_DUE) {
		R81_SPEC_PROVISION_PAST_DUE = r81_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR81_ON_BAL_SHEET_NETTING_ELIG() {
		return R81_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR81_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r81_ON_BAL_SHEET_NETTING_ELIG) {
		R81_ON_BAL_SHEET_NETTING_ELIG = r81_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR81_TOTAL_EXPOSURE_AFTER_NET() {
		return R81_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR81_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r81_TOTAL_EXPOSURE_AFTER_NET) {
		R81_TOTAL_EXPOSURE_AFTER_NET = r81_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR81_CRM_ELIG_EXPOSURE_SUBS() {
		return R81_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR81_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r81_CRM_ELIG_EXPOSURE_SUBS) {
		R81_CRM_ELIG_EXPOSURE_SUBS = r81_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR81_ELIG_GUARANTEES() {
		return R81_ELIG_GUARANTEES;
	}
	public void setR81_ELIG_GUARANTEES(BigDecimal r81_ELIG_GUARANTEES) {
		R81_ELIG_GUARANTEES = r81_ELIG_GUARANTEES;
	}
	public BigDecimal getR81_CREDIT_DERIVATIVES() {
		return R81_CREDIT_DERIVATIVES;
	}
	public void setR81_CREDIT_DERIVATIVES(BigDecimal r81_CREDIT_DERIVATIVES) {
		R81_CREDIT_DERIVATIVES = r81_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR81_CRM_COVERED_EXPOSURE() {
		return R81_CRM_COVERED_EXPOSURE;
	}
	public void setR81_CRM_COVERED_EXPOSURE(BigDecimal r81_CRM_COVERED_EXPOSURE) {
		R81_CRM_COVERED_EXPOSURE = r81_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR81_CRM_NOT_COVERED_EXPOSURE() {
		return R81_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR81_CRM_NOT_COVERED_EXPOSURE(BigDecimal r81_CRM_NOT_COVERED_EXPOSURE) {
		R81_CRM_NOT_COVERED_EXPOSURE = r81_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR81_CRM_RISK_WEIGHT() {
		return R81_CRM_RISK_WEIGHT;
	}
	public void setR81_CRM_RISK_WEIGHT(BigDecimal r81_CRM_RISK_WEIGHT) {
		R81_CRM_RISK_WEIGHT = r81_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR81_RWA_CRM_COVERED() {
		return R81_RWA_CRM_COVERED;
	}
	public void setR81_RWA_CRM_COVERED(BigDecimal r81_RWA_CRM_COVERED) {
		R81_RWA_CRM_COVERED = r81_RWA_CRM_COVERED;
	}
	public BigDecimal getR81_ORIG_COUNTERPARTY_RW() {
		return R81_ORIG_COUNTERPARTY_RW;
	}
	public void setR81_ORIG_COUNTERPARTY_RW(BigDecimal r81_ORIG_COUNTERPARTY_RW) {
		R81_ORIG_COUNTERPARTY_RW = r81_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR81_RWA_CRM_NOT_COVERED() {
		return R81_RWA_CRM_NOT_COVERED;
	}
	public void setR81_RWA_CRM_NOT_COVERED(BigDecimal r81_RWA_CRM_NOT_COVERED) {
		R81_RWA_CRM_NOT_COVERED = r81_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR81_CRM_ELIG_EXPOSURE_COMP() {
		return R81_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR81_CRM_ELIG_EXPOSURE_COMP(BigDecimal r81_CRM_ELIG_EXPOSURE_COMP) {
		R81_CRM_ELIG_EXPOSURE_COMP = r81_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR81_EXPOSURE_AFTER_VOL_ADJ() {
		return R81_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR81_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r81_EXPOSURE_AFTER_VOL_ADJ) {
		R81_EXPOSURE_AFTER_VOL_ADJ = r81_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR81_COLLATERAL_CASH() {
		return R81_COLLATERAL_CASH;
	}
	public void setR81_COLLATERAL_CASH(BigDecimal r81_COLLATERAL_CASH) {
		R81_COLLATERAL_CASH = r81_COLLATERAL_CASH;
	}
	public BigDecimal getR81_COLLATERAL_TBILLS() {
		return R81_COLLATERAL_TBILLS;
	}
	public void setR81_COLLATERAL_TBILLS(BigDecimal r81_COLLATERAL_TBILLS) {
		R81_COLLATERAL_TBILLS = r81_COLLATERAL_TBILLS;
	}
	public BigDecimal getR81_COLLATERAL_DEBT_SEC() {
		return R81_COLLATERAL_DEBT_SEC;
	}
	public void setR81_COLLATERAL_DEBT_SEC(BigDecimal r81_COLLATERAL_DEBT_SEC) {
		R81_COLLATERAL_DEBT_SEC = r81_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR81_COLLATERAL_EQUITIES() {
		return R81_COLLATERAL_EQUITIES;
	}
	public void setR81_COLLATERAL_EQUITIES(BigDecimal r81_COLLATERAL_EQUITIES) {
		R81_COLLATERAL_EQUITIES = r81_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR81_COLLATERAL_MUTUAL_FUNDS() {
		return R81_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR81_COLLATERAL_MUTUAL_FUNDS(BigDecimal r81_COLLATERAL_MUTUAL_FUNDS) {
		R81_COLLATERAL_MUTUAL_FUNDS = r81_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR81_TOTAL_COLLATERAL_HAIRCUT() {
		return R81_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR81_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r81_TOTAL_COLLATERAL_HAIRCUT) {
		R81_TOTAL_COLLATERAL_HAIRCUT = r81_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR81_EXPOSURE_AFTER_CRM() {
		return R81_EXPOSURE_AFTER_CRM;
	}
	public void setR81_EXPOSURE_AFTER_CRM(BigDecimal r81_EXPOSURE_AFTER_CRM) {
		R81_EXPOSURE_AFTER_CRM = r81_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR81_RWA_NOT_COVERED_CRM() {
		return R81_RWA_NOT_COVERED_CRM;
	}
	public void setR81_RWA_NOT_COVERED_CRM(BigDecimal r81_RWA_NOT_COVERED_CRM) {
		R81_RWA_NOT_COVERED_CRM = r81_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR81_RWA_UNSECURED_EXPOSURE() {
		return R81_RWA_UNSECURED_EXPOSURE;
	}
	public void setR81_RWA_UNSECURED_EXPOSURE(BigDecimal r81_RWA_UNSECURED_EXPOSURE) {
		R81_RWA_UNSECURED_EXPOSURE = r81_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR81_RWA_UNSECURED() {
		return R81_RWA_UNSECURED;
	}
	public void setR81_RWA_UNSECURED(BigDecimal r81_RWA_UNSECURED) {
		R81_RWA_UNSECURED = r81_RWA_UNSECURED;
	}
	public BigDecimal getR81_TOTAL_RWA() {
		return R81_TOTAL_RWA;
	}
	public void setR81_TOTAL_RWA(BigDecimal r81_TOTAL_RWA) {
		R81_TOTAL_RWA = r81_TOTAL_RWA;
	}
	public BigDecimal getR82_EXPOSURE_BEFORE_CRM() {
		return R82_EXPOSURE_BEFORE_CRM;
	}
	public void setR82_EXPOSURE_BEFORE_CRM(BigDecimal r82_EXPOSURE_BEFORE_CRM) {
		R82_EXPOSURE_BEFORE_CRM = r82_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR82_SPEC_PROVISION_PAST_DUE() {
		return R82_SPEC_PROVISION_PAST_DUE;
	}
	public void setR82_SPEC_PROVISION_PAST_DUE(BigDecimal r82_SPEC_PROVISION_PAST_DUE) {
		R82_SPEC_PROVISION_PAST_DUE = r82_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR82_ON_BAL_SHEET_NETTING_ELIG() {
		return R82_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR82_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r82_ON_BAL_SHEET_NETTING_ELIG) {
		R82_ON_BAL_SHEET_NETTING_ELIG = r82_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR82_TOTAL_EXPOSURE_AFTER_NET() {
		return R82_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR82_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r82_TOTAL_EXPOSURE_AFTER_NET) {
		R82_TOTAL_EXPOSURE_AFTER_NET = r82_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR82_CRM_ELIG_EXPOSURE_SUBS() {
		return R82_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR82_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r82_CRM_ELIG_EXPOSURE_SUBS) {
		R82_CRM_ELIG_EXPOSURE_SUBS = r82_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR82_ELIG_GUARANTEES() {
		return R82_ELIG_GUARANTEES;
	}
	public void setR82_ELIG_GUARANTEES(BigDecimal r82_ELIG_GUARANTEES) {
		R82_ELIG_GUARANTEES = r82_ELIG_GUARANTEES;
	}
	public BigDecimal getR82_CREDIT_DERIVATIVES() {
		return R82_CREDIT_DERIVATIVES;
	}
	public void setR82_CREDIT_DERIVATIVES(BigDecimal r82_CREDIT_DERIVATIVES) {
		R82_CREDIT_DERIVATIVES = r82_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR82_CRM_COVERED_EXPOSURE() {
		return R82_CRM_COVERED_EXPOSURE;
	}
	public void setR82_CRM_COVERED_EXPOSURE(BigDecimal r82_CRM_COVERED_EXPOSURE) {
		R82_CRM_COVERED_EXPOSURE = r82_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR82_CRM_NOT_COVERED_EXPOSURE() {
		return R82_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR82_CRM_NOT_COVERED_EXPOSURE(BigDecimal r82_CRM_NOT_COVERED_EXPOSURE) {
		R82_CRM_NOT_COVERED_EXPOSURE = r82_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR82_CRM_RISK_WEIGHT() {
		return R82_CRM_RISK_WEIGHT;
	}
	public void setR82_CRM_RISK_WEIGHT(BigDecimal r82_CRM_RISK_WEIGHT) {
		R82_CRM_RISK_WEIGHT = r82_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR82_RWA_CRM_COVERED() {
		return R82_RWA_CRM_COVERED;
	}
	public void setR82_RWA_CRM_COVERED(BigDecimal r82_RWA_CRM_COVERED) {
		R82_RWA_CRM_COVERED = r82_RWA_CRM_COVERED;
	}
	public BigDecimal getR82_ORIG_COUNTERPARTY_RW() {
		return R82_ORIG_COUNTERPARTY_RW;
	}
	public void setR82_ORIG_COUNTERPARTY_RW(BigDecimal r82_ORIG_COUNTERPARTY_RW) {
		R82_ORIG_COUNTERPARTY_RW = r82_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR82_RWA_CRM_NOT_COVERED() {
		return R82_RWA_CRM_NOT_COVERED;
	}
	public void setR82_RWA_CRM_NOT_COVERED(BigDecimal r82_RWA_CRM_NOT_COVERED) {
		R82_RWA_CRM_NOT_COVERED = r82_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR82_CRM_ELIG_EXPOSURE_COMP() {
		return R82_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR82_CRM_ELIG_EXPOSURE_COMP(BigDecimal r82_CRM_ELIG_EXPOSURE_COMP) {
		R82_CRM_ELIG_EXPOSURE_COMP = r82_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR82_EXPOSURE_AFTER_VOL_ADJ() {
		return R82_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR82_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r82_EXPOSURE_AFTER_VOL_ADJ) {
		R82_EXPOSURE_AFTER_VOL_ADJ = r82_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR82_COLLATERAL_CASH() {
		return R82_COLLATERAL_CASH;
	}
	public void setR82_COLLATERAL_CASH(BigDecimal r82_COLLATERAL_CASH) {
		R82_COLLATERAL_CASH = r82_COLLATERAL_CASH;
	}
	public BigDecimal getR82_COLLATERAL_TBILLS() {
		return R82_COLLATERAL_TBILLS;
	}
	public void setR82_COLLATERAL_TBILLS(BigDecimal r82_COLLATERAL_TBILLS) {
		R82_COLLATERAL_TBILLS = r82_COLLATERAL_TBILLS;
	}
	public BigDecimal getR82_COLLATERAL_DEBT_SEC() {
		return R82_COLLATERAL_DEBT_SEC;
	}
	public void setR82_COLLATERAL_DEBT_SEC(BigDecimal r82_COLLATERAL_DEBT_SEC) {
		R82_COLLATERAL_DEBT_SEC = r82_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR82_COLLATERAL_EQUITIES() {
		return R82_COLLATERAL_EQUITIES;
	}
	public void setR82_COLLATERAL_EQUITIES(BigDecimal r82_COLLATERAL_EQUITIES) {
		R82_COLLATERAL_EQUITIES = r82_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR82_COLLATERAL_MUTUAL_FUNDS() {
		return R82_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR82_COLLATERAL_MUTUAL_FUNDS(BigDecimal r82_COLLATERAL_MUTUAL_FUNDS) {
		R82_COLLATERAL_MUTUAL_FUNDS = r82_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR82_TOTAL_COLLATERAL_HAIRCUT() {
		return R82_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR82_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r82_TOTAL_COLLATERAL_HAIRCUT) {
		R82_TOTAL_COLLATERAL_HAIRCUT = r82_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR82_EXPOSURE_AFTER_CRM() {
		return R82_EXPOSURE_AFTER_CRM;
	}
	public void setR82_EXPOSURE_AFTER_CRM(BigDecimal r82_EXPOSURE_AFTER_CRM) {
		R82_EXPOSURE_AFTER_CRM = r82_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR82_RWA_NOT_COVERED_CRM() {
		return R82_RWA_NOT_COVERED_CRM;
	}
	public void setR82_RWA_NOT_COVERED_CRM(BigDecimal r82_RWA_NOT_COVERED_CRM) {
		R82_RWA_NOT_COVERED_CRM = r82_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR82_RWA_UNSECURED_EXPOSURE() {
		return R82_RWA_UNSECURED_EXPOSURE;
	}
	public void setR82_RWA_UNSECURED_EXPOSURE(BigDecimal r82_RWA_UNSECURED_EXPOSURE) {
		R82_RWA_UNSECURED_EXPOSURE = r82_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR82_RWA_UNSECURED() {
		return R82_RWA_UNSECURED;
	}
	public void setR82_RWA_UNSECURED(BigDecimal r82_RWA_UNSECURED) {
		R82_RWA_UNSECURED = r82_RWA_UNSECURED;
	}
	public BigDecimal getR82_TOTAL_RWA() {
		return R82_TOTAL_RWA;
	}
	public void setR82_TOTAL_RWA(BigDecimal r82_TOTAL_RWA) {
		R82_TOTAL_RWA = r82_TOTAL_RWA;
	}
	public BigDecimal getR83_EXPOSURE_BEFORE_CRM() {
		return R83_EXPOSURE_BEFORE_CRM;
	}
	public void setR83_EXPOSURE_BEFORE_CRM(BigDecimal r83_EXPOSURE_BEFORE_CRM) {
		R83_EXPOSURE_BEFORE_CRM = r83_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR83_SPEC_PROVISION_PAST_DUE() {
		return R83_SPEC_PROVISION_PAST_DUE;
	}
	public void setR83_SPEC_PROVISION_PAST_DUE(BigDecimal r83_SPEC_PROVISION_PAST_DUE) {
		R83_SPEC_PROVISION_PAST_DUE = r83_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR83_ON_BAL_SHEET_NETTING_ELIG() {
		return R83_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR83_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r83_ON_BAL_SHEET_NETTING_ELIG) {
		R83_ON_BAL_SHEET_NETTING_ELIG = r83_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR83_TOTAL_EXPOSURE_AFTER_NET() {
		return R83_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR83_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r83_TOTAL_EXPOSURE_AFTER_NET) {
		R83_TOTAL_EXPOSURE_AFTER_NET = r83_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR83_CRM_ELIG_EXPOSURE_SUBS() {
		return R83_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR83_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r83_CRM_ELIG_EXPOSURE_SUBS) {
		R83_CRM_ELIG_EXPOSURE_SUBS = r83_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR83_ELIG_GUARANTEES() {
		return R83_ELIG_GUARANTEES;
	}
	public void setR83_ELIG_GUARANTEES(BigDecimal r83_ELIG_GUARANTEES) {
		R83_ELIG_GUARANTEES = r83_ELIG_GUARANTEES;
	}
	public BigDecimal getR83_CREDIT_DERIVATIVES() {
		return R83_CREDIT_DERIVATIVES;
	}
	public void setR83_CREDIT_DERIVATIVES(BigDecimal r83_CREDIT_DERIVATIVES) {
		R83_CREDIT_DERIVATIVES = r83_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR83_CRM_COVERED_EXPOSURE() {
		return R83_CRM_COVERED_EXPOSURE;
	}
	public void setR83_CRM_COVERED_EXPOSURE(BigDecimal r83_CRM_COVERED_EXPOSURE) {
		R83_CRM_COVERED_EXPOSURE = r83_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR83_CRM_NOT_COVERED_EXPOSURE() {
		return R83_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR83_CRM_NOT_COVERED_EXPOSURE(BigDecimal r83_CRM_NOT_COVERED_EXPOSURE) {
		R83_CRM_NOT_COVERED_EXPOSURE = r83_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR83_CRM_RISK_WEIGHT() {
		return R83_CRM_RISK_WEIGHT;
	}
	public void setR83_CRM_RISK_WEIGHT(BigDecimal r83_CRM_RISK_WEIGHT) {
		R83_CRM_RISK_WEIGHT = r83_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR83_RWA_CRM_COVERED() {
		return R83_RWA_CRM_COVERED;
	}
	public void setR83_RWA_CRM_COVERED(BigDecimal r83_RWA_CRM_COVERED) {
		R83_RWA_CRM_COVERED = r83_RWA_CRM_COVERED;
	}
	public BigDecimal getR83_ORIG_COUNTERPARTY_RW() {
		return R83_ORIG_COUNTERPARTY_RW;
	}
	public void setR83_ORIG_COUNTERPARTY_RW(BigDecimal r83_ORIG_COUNTERPARTY_RW) {
		R83_ORIG_COUNTERPARTY_RW = r83_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR83_RWA_CRM_NOT_COVERED() {
		return R83_RWA_CRM_NOT_COVERED;
	}
	public void setR83_RWA_CRM_NOT_COVERED(BigDecimal r83_RWA_CRM_NOT_COVERED) {
		R83_RWA_CRM_NOT_COVERED = r83_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR83_CRM_ELIG_EXPOSURE_COMP() {
		return R83_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR83_CRM_ELIG_EXPOSURE_COMP(BigDecimal r83_CRM_ELIG_EXPOSURE_COMP) {
		R83_CRM_ELIG_EXPOSURE_COMP = r83_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR83_EXPOSURE_AFTER_VOL_ADJ() {
		return R83_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR83_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r83_EXPOSURE_AFTER_VOL_ADJ) {
		R83_EXPOSURE_AFTER_VOL_ADJ = r83_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR83_COLLATERAL_CASH() {
		return R83_COLLATERAL_CASH;
	}
	public void setR83_COLLATERAL_CASH(BigDecimal r83_COLLATERAL_CASH) {
		R83_COLLATERAL_CASH = r83_COLLATERAL_CASH;
	}
	public BigDecimal getR83_COLLATERAL_TBILLS() {
		return R83_COLLATERAL_TBILLS;
	}
	public void setR83_COLLATERAL_TBILLS(BigDecimal r83_COLLATERAL_TBILLS) {
		R83_COLLATERAL_TBILLS = r83_COLLATERAL_TBILLS;
	}
	public BigDecimal getR83_COLLATERAL_DEBT_SEC() {
		return R83_COLLATERAL_DEBT_SEC;
	}
	public void setR83_COLLATERAL_DEBT_SEC(BigDecimal r83_COLLATERAL_DEBT_SEC) {
		R83_COLLATERAL_DEBT_SEC = r83_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR83_COLLATERAL_EQUITIES() {
		return R83_COLLATERAL_EQUITIES;
	}
	public void setR83_COLLATERAL_EQUITIES(BigDecimal r83_COLLATERAL_EQUITIES) {
		R83_COLLATERAL_EQUITIES = r83_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR83_COLLATERAL_MUTUAL_FUNDS() {
		return R83_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR83_COLLATERAL_MUTUAL_FUNDS(BigDecimal r83_COLLATERAL_MUTUAL_FUNDS) {
		R83_COLLATERAL_MUTUAL_FUNDS = r83_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR83_TOTAL_COLLATERAL_HAIRCUT() {
		return R83_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR83_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r83_TOTAL_COLLATERAL_HAIRCUT) {
		R83_TOTAL_COLLATERAL_HAIRCUT = r83_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR83_EXPOSURE_AFTER_CRM() {
		return R83_EXPOSURE_AFTER_CRM;
	}
	public void setR83_EXPOSURE_AFTER_CRM(BigDecimal r83_EXPOSURE_AFTER_CRM) {
		R83_EXPOSURE_AFTER_CRM = r83_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR83_RWA_NOT_COVERED_CRM() {
		return R83_RWA_NOT_COVERED_CRM;
	}
	public void setR83_RWA_NOT_COVERED_CRM(BigDecimal r83_RWA_NOT_COVERED_CRM) {
		R83_RWA_NOT_COVERED_CRM = r83_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR83_RWA_UNSECURED_EXPOSURE() {
		return R83_RWA_UNSECURED_EXPOSURE;
	}
	public void setR83_RWA_UNSECURED_EXPOSURE(BigDecimal r83_RWA_UNSECURED_EXPOSURE) {
		R83_RWA_UNSECURED_EXPOSURE = r83_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR83_RWA_UNSECURED() {
		return R83_RWA_UNSECURED;
	}
	public void setR83_RWA_UNSECURED(BigDecimal r83_RWA_UNSECURED) {
		R83_RWA_UNSECURED = r83_RWA_UNSECURED;
	}
	public BigDecimal getR83_TOTAL_RWA() {
		return R83_TOTAL_RWA;
	}
	public void setR83_TOTAL_RWA(BigDecimal r83_TOTAL_RWA) {
		R83_TOTAL_RWA = r83_TOTAL_RWA;
	}
	public BigDecimal getR84_EXPOSURE_BEFORE_CRM() {
		return R84_EXPOSURE_BEFORE_CRM;
	}
	public void setR84_EXPOSURE_BEFORE_CRM(BigDecimal r84_EXPOSURE_BEFORE_CRM) {
		R84_EXPOSURE_BEFORE_CRM = r84_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR84_SPEC_PROVISION_PAST_DUE() {
		return R84_SPEC_PROVISION_PAST_DUE;
	}
	public void setR84_SPEC_PROVISION_PAST_DUE(BigDecimal r84_SPEC_PROVISION_PAST_DUE) {
		R84_SPEC_PROVISION_PAST_DUE = r84_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR84_ON_BAL_SHEET_NETTING_ELIG() {
		return R84_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR84_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r84_ON_BAL_SHEET_NETTING_ELIG) {
		R84_ON_BAL_SHEET_NETTING_ELIG = r84_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR84_TOTAL_EXPOSURE_AFTER_NET() {
		return R84_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR84_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r84_TOTAL_EXPOSURE_AFTER_NET) {
		R84_TOTAL_EXPOSURE_AFTER_NET = r84_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR84_CRM_ELIG_EXPOSURE_SUBS() {
		return R84_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR84_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r84_CRM_ELIG_EXPOSURE_SUBS) {
		R84_CRM_ELIG_EXPOSURE_SUBS = r84_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR84_ELIG_GUARANTEES() {
		return R84_ELIG_GUARANTEES;
	}
	public void setR84_ELIG_GUARANTEES(BigDecimal r84_ELIG_GUARANTEES) {
		R84_ELIG_GUARANTEES = r84_ELIG_GUARANTEES;
	}
	public BigDecimal getR84_CREDIT_DERIVATIVES() {
		return R84_CREDIT_DERIVATIVES;
	}
	public void setR84_CREDIT_DERIVATIVES(BigDecimal r84_CREDIT_DERIVATIVES) {
		R84_CREDIT_DERIVATIVES = r84_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR84_CRM_COVERED_EXPOSURE() {
		return R84_CRM_COVERED_EXPOSURE;
	}
	public void setR84_CRM_COVERED_EXPOSURE(BigDecimal r84_CRM_COVERED_EXPOSURE) {
		R84_CRM_COVERED_EXPOSURE = r84_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR84_CRM_NOT_COVERED_EXPOSURE() {
		return R84_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR84_CRM_NOT_COVERED_EXPOSURE(BigDecimal r84_CRM_NOT_COVERED_EXPOSURE) {
		R84_CRM_NOT_COVERED_EXPOSURE = r84_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR84_CRM_RISK_WEIGHT() {
		return R84_CRM_RISK_WEIGHT;
	}
	public void setR84_CRM_RISK_WEIGHT(BigDecimal r84_CRM_RISK_WEIGHT) {
		R84_CRM_RISK_WEIGHT = r84_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR84_RWA_CRM_COVERED() {
		return R84_RWA_CRM_COVERED;
	}
	public void setR84_RWA_CRM_COVERED(BigDecimal r84_RWA_CRM_COVERED) {
		R84_RWA_CRM_COVERED = r84_RWA_CRM_COVERED;
	}
	public BigDecimal getR84_ORIG_COUNTERPARTY_RW() {
		return R84_ORIG_COUNTERPARTY_RW;
	}
	public void setR84_ORIG_COUNTERPARTY_RW(BigDecimal r84_ORIG_COUNTERPARTY_RW) {
		R84_ORIG_COUNTERPARTY_RW = r84_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR84_RWA_CRM_NOT_COVERED() {
		return R84_RWA_CRM_NOT_COVERED;
	}
	public void setR84_RWA_CRM_NOT_COVERED(BigDecimal r84_RWA_CRM_NOT_COVERED) {
		R84_RWA_CRM_NOT_COVERED = r84_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR84_CRM_ELIG_EXPOSURE_COMP() {
		return R84_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR84_CRM_ELIG_EXPOSURE_COMP(BigDecimal r84_CRM_ELIG_EXPOSURE_COMP) {
		R84_CRM_ELIG_EXPOSURE_COMP = r84_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR84_EXPOSURE_AFTER_VOL_ADJ() {
		return R84_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR84_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r84_EXPOSURE_AFTER_VOL_ADJ) {
		R84_EXPOSURE_AFTER_VOL_ADJ = r84_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR84_COLLATERAL_CASH() {
		return R84_COLLATERAL_CASH;
	}
	public void setR84_COLLATERAL_CASH(BigDecimal r84_COLLATERAL_CASH) {
		R84_COLLATERAL_CASH = r84_COLLATERAL_CASH;
	}
	public BigDecimal getR84_COLLATERAL_TBILLS() {
		return R84_COLLATERAL_TBILLS;
	}
	public void setR84_COLLATERAL_TBILLS(BigDecimal r84_COLLATERAL_TBILLS) {
		R84_COLLATERAL_TBILLS = r84_COLLATERAL_TBILLS;
	}
	public BigDecimal getR84_COLLATERAL_DEBT_SEC() {
		return R84_COLLATERAL_DEBT_SEC;
	}
	public void setR84_COLLATERAL_DEBT_SEC(BigDecimal r84_COLLATERAL_DEBT_SEC) {
		R84_COLLATERAL_DEBT_SEC = r84_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR84_COLLATERAL_EQUITIES() {
		return R84_COLLATERAL_EQUITIES;
	}
	public void setR84_COLLATERAL_EQUITIES(BigDecimal r84_COLLATERAL_EQUITIES) {
		R84_COLLATERAL_EQUITIES = r84_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR84_COLLATERAL_MUTUAL_FUNDS() {
		return R84_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR84_COLLATERAL_MUTUAL_FUNDS(BigDecimal r84_COLLATERAL_MUTUAL_FUNDS) {
		R84_COLLATERAL_MUTUAL_FUNDS = r84_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR84_TOTAL_COLLATERAL_HAIRCUT() {
		return R84_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR84_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r84_TOTAL_COLLATERAL_HAIRCUT) {
		R84_TOTAL_COLLATERAL_HAIRCUT = r84_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR84_EXPOSURE_AFTER_CRM() {
		return R84_EXPOSURE_AFTER_CRM;
	}
	public void setR84_EXPOSURE_AFTER_CRM(BigDecimal r84_EXPOSURE_AFTER_CRM) {
		R84_EXPOSURE_AFTER_CRM = r84_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR84_RWA_NOT_COVERED_CRM() {
		return R84_RWA_NOT_COVERED_CRM;
	}
	public void setR84_RWA_NOT_COVERED_CRM(BigDecimal r84_RWA_NOT_COVERED_CRM) {
		R84_RWA_NOT_COVERED_CRM = r84_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR84_RWA_UNSECURED_EXPOSURE() {
		return R84_RWA_UNSECURED_EXPOSURE;
	}
	public void setR84_RWA_UNSECURED_EXPOSURE(BigDecimal r84_RWA_UNSECURED_EXPOSURE) {
		R84_RWA_UNSECURED_EXPOSURE = r84_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR84_RWA_UNSECURED() {
		return R84_RWA_UNSECURED;
	}
	public void setR84_RWA_UNSECURED(BigDecimal r84_RWA_UNSECURED) {
		R84_RWA_UNSECURED = r84_RWA_UNSECURED;
	}
	public BigDecimal getR84_TOTAL_RWA() {
		return R84_TOTAL_RWA;
	}
	public void setR84_TOTAL_RWA(BigDecimal r84_TOTAL_RWA) {
		R84_TOTAL_RWA = r84_TOTAL_RWA;
	}
	public BigDecimal getR85_EXPOSURE_BEFORE_CRM() {
		return R85_EXPOSURE_BEFORE_CRM;
	}
	public void setR85_EXPOSURE_BEFORE_CRM(BigDecimal r85_EXPOSURE_BEFORE_CRM) {
		R85_EXPOSURE_BEFORE_CRM = r85_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR85_SPEC_PROVISION_PAST_DUE() {
		return R85_SPEC_PROVISION_PAST_DUE;
	}
	public void setR85_SPEC_PROVISION_PAST_DUE(BigDecimal r85_SPEC_PROVISION_PAST_DUE) {
		R85_SPEC_PROVISION_PAST_DUE = r85_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR85_ON_BAL_SHEET_NETTING_ELIG() {
		return R85_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR85_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r85_ON_BAL_SHEET_NETTING_ELIG) {
		R85_ON_BAL_SHEET_NETTING_ELIG = r85_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR85_TOTAL_EXPOSURE_AFTER_NET() {
		return R85_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR85_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r85_TOTAL_EXPOSURE_AFTER_NET) {
		R85_TOTAL_EXPOSURE_AFTER_NET = r85_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR85_CRM_ELIG_EXPOSURE_SUBS() {
		return R85_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR85_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r85_CRM_ELIG_EXPOSURE_SUBS) {
		R85_CRM_ELIG_EXPOSURE_SUBS = r85_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR85_ELIG_GUARANTEES() {
		return R85_ELIG_GUARANTEES;
	}
	public void setR85_ELIG_GUARANTEES(BigDecimal r85_ELIG_GUARANTEES) {
		R85_ELIG_GUARANTEES = r85_ELIG_GUARANTEES;
	}
	public BigDecimal getR85_CREDIT_DERIVATIVES() {
		return R85_CREDIT_DERIVATIVES;
	}
	public void setR85_CREDIT_DERIVATIVES(BigDecimal r85_CREDIT_DERIVATIVES) {
		R85_CREDIT_DERIVATIVES = r85_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR85_CRM_COVERED_EXPOSURE() {
		return R85_CRM_COVERED_EXPOSURE;
	}
	public void setR85_CRM_COVERED_EXPOSURE(BigDecimal r85_CRM_COVERED_EXPOSURE) {
		R85_CRM_COVERED_EXPOSURE = r85_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR85_CRM_NOT_COVERED_EXPOSURE() {
		return R85_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR85_CRM_NOT_COVERED_EXPOSURE(BigDecimal r85_CRM_NOT_COVERED_EXPOSURE) {
		R85_CRM_NOT_COVERED_EXPOSURE = r85_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR85_CRM_RISK_WEIGHT() {
		return R85_CRM_RISK_WEIGHT;
	}
	public void setR85_CRM_RISK_WEIGHT(BigDecimal r85_CRM_RISK_WEIGHT) {
		R85_CRM_RISK_WEIGHT = r85_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR85_RWA_CRM_COVERED() {
		return R85_RWA_CRM_COVERED;
	}
	public void setR85_RWA_CRM_COVERED(BigDecimal r85_RWA_CRM_COVERED) {
		R85_RWA_CRM_COVERED = r85_RWA_CRM_COVERED;
	}
	public BigDecimal getR85_ORIG_COUNTERPARTY_RW() {
		return R85_ORIG_COUNTERPARTY_RW;
	}
	public void setR85_ORIG_COUNTERPARTY_RW(BigDecimal r85_ORIG_COUNTERPARTY_RW) {
		R85_ORIG_COUNTERPARTY_RW = r85_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR85_RWA_CRM_NOT_COVERED() {
		return R85_RWA_CRM_NOT_COVERED;
	}
	public void setR85_RWA_CRM_NOT_COVERED(BigDecimal r85_RWA_CRM_NOT_COVERED) {
		R85_RWA_CRM_NOT_COVERED = r85_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR85_CRM_ELIG_EXPOSURE_COMP() {
		return R85_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR85_CRM_ELIG_EXPOSURE_COMP(BigDecimal r85_CRM_ELIG_EXPOSURE_COMP) {
		R85_CRM_ELIG_EXPOSURE_COMP = r85_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR85_EXPOSURE_AFTER_VOL_ADJ() {
		return R85_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR85_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r85_EXPOSURE_AFTER_VOL_ADJ) {
		R85_EXPOSURE_AFTER_VOL_ADJ = r85_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR85_COLLATERAL_CASH() {
		return R85_COLLATERAL_CASH;
	}
	public void setR85_COLLATERAL_CASH(BigDecimal r85_COLLATERAL_CASH) {
		R85_COLLATERAL_CASH = r85_COLLATERAL_CASH;
	}
	public BigDecimal getR85_COLLATERAL_TBILLS() {
		return R85_COLLATERAL_TBILLS;
	}
	public void setR85_COLLATERAL_TBILLS(BigDecimal r85_COLLATERAL_TBILLS) {
		R85_COLLATERAL_TBILLS = r85_COLLATERAL_TBILLS;
	}
	public BigDecimal getR85_COLLATERAL_DEBT_SEC() {
		return R85_COLLATERAL_DEBT_SEC;
	}
	public void setR85_COLLATERAL_DEBT_SEC(BigDecimal r85_COLLATERAL_DEBT_SEC) {
		R85_COLLATERAL_DEBT_SEC = r85_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR85_COLLATERAL_EQUITIES() {
		return R85_COLLATERAL_EQUITIES;
	}
	public void setR85_COLLATERAL_EQUITIES(BigDecimal r85_COLLATERAL_EQUITIES) {
		R85_COLLATERAL_EQUITIES = r85_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR85_COLLATERAL_MUTUAL_FUNDS() {
		return R85_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR85_COLLATERAL_MUTUAL_FUNDS(BigDecimal r85_COLLATERAL_MUTUAL_FUNDS) {
		R85_COLLATERAL_MUTUAL_FUNDS = r85_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR85_TOTAL_COLLATERAL_HAIRCUT() {
		return R85_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR85_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r85_TOTAL_COLLATERAL_HAIRCUT) {
		R85_TOTAL_COLLATERAL_HAIRCUT = r85_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR85_EXPOSURE_AFTER_CRM() {
		return R85_EXPOSURE_AFTER_CRM;
	}
	public void setR85_EXPOSURE_AFTER_CRM(BigDecimal r85_EXPOSURE_AFTER_CRM) {
		R85_EXPOSURE_AFTER_CRM = r85_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR85_RWA_NOT_COVERED_CRM() {
		return R85_RWA_NOT_COVERED_CRM;
	}
	public void setR85_RWA_NOT_COVERED_CRM(BigDecimal r85_RWA_NOT_COVERED_CRM) {
		R85_RWA_NOT_COVERED_CRM = r85_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR85_RWA_UNSECURED_EXPOSURE() {
		return R85_RWA_UNSECURED_EXPOSURE;
	}
	public void setR85_RWA_UNSECURED_EXPOSURE(BigDecimal r85_RWA_UNSECURED_EXPOSURE) {
		R85_RWA_UNSECURED_EXPOSURE = r85_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR85_RWA_UNSECURED() {
		return R85_RWA_UNSECURED;
	}
	public void setR85_RWA_UNSECURED(BigDecimal r85_RWA_UNSECURED) {
		R85_RWA_UNSECURED = r85_RWA_UNSECURED;
	}
	public BigDecimal getR85_TOTAL_RWA() {
		return R85_TOTAL_RWA;
	}
	public void setR85_TOTAL_RWA(BigDecimal r85_TOTAL_RWA) {
		R85_TOTAL_RWA = r85_TOTAL_RWA;
	}
	public BigDecimal getR86_EXPOSURE_BEFORE_CRM() {
		return R86_EXPOSURE_BEFORE_CRM;
	}
	public void setR86_EXPOSURE_BEFORE_CRM(BigDecimal r86_EXPOSURE_BEFORE_CRM) {
		R86_EXPOSURE_BEFORE_CRM = r86_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR86_SPEC_PROVISION_PAST_DUE() {
		return R86_SPEC_PROVISION_PAST_DUE;
	}
	public void setR86_SPEC_PROVISION_PAST_DUE(BigDecimal r86_SPEC_PROVISION_PAST_DUE) {
		R86_SPEC_PROVISION_PAST_DUE = r86_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR86_ON_BAL_SHEET_NETTING_ELIG() {
		return R86_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR86_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r86_ON_BAL_SHEET_NETTING_ELIG) {
		R86_ON_BAL_SHEET_NETTING_ELIG = r86_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR86_TOTAL_EXPOSURE_AFTER_NET() {
		return R86_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR86_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r86_TOTAL_EXPOSURE_AFTER_NET) {
		R86_TOTAL_EXPOSURE_AFTER_NET = r86_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR86_CRM_ELIG_EXPOSURE_SUBS() {
		return R86_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR86_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r86_CRM_ELIG_EXPOSURE_SUBS) {
		R86_CRM_ELIG_EXPOSURE_SUBS = r86_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR86_ELIG_GUARANTEES() {
		return R86_ELIG_GUARANTEES;
	}
	public void setR86_ELIG_GUARANTEES(BigDecimal r86_ELIG_GUARANTEES) {
		R86_ELIG_GUARANTEES = r86_ELIG_GUARANTEES;
	}
	public BigDecimal getR86_CREDIT_DERIVATIVES() {
		return R86_CREDIT_DERIVATIVES;
	}
	public void setR86_CREDIT_DERIVATIVES(BigDecimal r86_CREDIT_DERIVATIVES) {
		R86_CREDIT_DERIVATIVES = r86_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR86_CRM_COVERED_EXPOSURE() {
		return R86_CRM_COVERED_EXPOSURE;
	}
	public void setR86_CRM_COVERED_EXPOSURE(BigDecimal r86_CRM_COVERED_EXPOSURE) {
		R86_CRM_COVERED_EXPOSURE = r86_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR86_CRM_NOT_COVERED_EXPOSURE() {
		return R86_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR86_CRM_NOT_COVERED_EXPOSURE(BigDecimal r86_CRM_NOT_COVERED_EXPOSURE) {
		R86_CRM_NOT_COVERED_EXPOSURE = r86_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR86_CRM_RISK_WEIGHT() {
		return R86_CRM_RISK_WEIGHT;
	}
	public void setR86_CRM_RISK_WEIGHT(BigDecimal r86_CRM_RISK_WEIGHT) {
		R86_CRM_RISK_WEIGHT = r86_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR86_RWA_CRM_COVERED() {
		return R86_RWA_CRM_COVERED;
	}
	public void setR86_RWA_CRM_COVERED(BigDecimal r86_RWA_CRM_COVERED) {
		R86_RWA_CRM_COVERED = r86_RWA_CRM_COVERED;
	}
	public BigDecimal getR86_ORIG_COUNTERPARTY_RW() {
		return R86_ORIG_COUNTERPARTY_RW;
	}
	public void setR86_ORIG_COUNTERPARTY_RW(BigDecimal r86_ORIG_COUNTERPARTY_RW) {
		R86_ORIG_COUNTERPARTY_RW = r86_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR86_RWA_CRM_NOT_COVERED() {
		return R86_RWA_CRM_NOT_COVERED;
	}
	public void setR86_RWA_CRM_NOT_COVERED(BigDecimal r86_RWA_CRM_NOT_COVERED) {
		R86_RWA_CRM_NOT_COVERED = r86_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR86_CRM_ELIG_EXPOSURE_COMP() {
		return R86_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR86_CRM_ELIG_EXPOSURE_COMP(BigDecimal r86_CRM_ELIG_EXPOSURE_COMP) {
		R86_CRM_ELIG_EXPOSURE_COMP = r86_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR86_EXPOSURE_AFTER_VOL_ADJ() {
		return R86_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR86_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r86_EXPOSURE_AFTER_VOL_ADJ) {
		R86_EXPOSURE_AFTER_VOL_ADJ = r86_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR86_COLLATERAL_CASH() {
		return R86_COLLATERAL_CASH;
	}
	public void setR86_COLLATERAL_CASH(BigDecimal r86_COLLATERAL_CASH) {
		R86_COLLATERAL_CASH = r86_COLLATERAL_CASH;
	}
	public BigDecimal getR86_COLLATERAL_TBILLS() {
		return R86_COLLATERAL_TBILLS;
	}
	public void setR86_COLLATERAL_TBILLS(BigDecimal r86_COLLATERAL_TBILLS) {
		R86_COLLATERAL_TBILLS = r86_COLLATERAL_TBILLS;
	}
	public BigDecimal getR86_COLLATERAL_DEBT_SEC() {
		return R86_COLLATERAL_DEBT_SEC;
	}
	public void setR86_COLLATERAL_DEBT_SEC(BigDecimal r86_COLLATERAL_DEBT_SEC) {
		R86_COLLATERAL_DEBT_SEC = r86_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR86_COLLATERAL_EQUITIES() {
		return R86_COLLATERAL_EQUITIES;
	}
	public void setR86_COLLATERAL_EQUITIES(BigDecimal r86_COLLATERAL_EQUITIES) {
		R86_COLLATERAL_EQUITIES = r86_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR86_COLLATERAL_MUTUAL_FUNDS() {
		return R86_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR86_COLLATERAL_MUTUAL_FUNDS(BigDecimal r86_COLLATERAL_MUTUAL_FUNDS) {
		R86_COLLATERAL_MUTUAL_FUNDS = r86_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR86_TOTAL_COLLATERAL_HAIRCUT() {
		return R86_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR86_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r86_TOTAL_COLLATERAL_HAIRCUT) {
		R86_TOTAL_COLLATERAL_HAIRCUT = r86_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR86_EXPOSURE_AFTER_CRM() {
		return R86_EXPOSURE_AFTER_CRM;
	}
	public void setR86_EXPOSURE_AFTER_CRM(BigDecimal r86_EXPOSURE_AFTER_CRM) {
		R86_EXPOSURE_AFTER_CRM = r86_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR86_RWA_NOT_COVERED_CRM() {
		return R86_RWA_NOT_COVERED_CRM;
	}
	public void setR86_RWA_NOT_COVERED_CRM(BigDecimal r86_RWA_NOT_COVERED_CRM) {
		R86_RWA_NOT_COVERED_CRM = r86_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR86_RWA_UNSECURED_EXPOSURE() {
		return R86_RWA_UNSECURED_EXPOSURE;
	}
	public void setR86_RWA_UNSECURED_EXPOSURE(BigDecimal r86_RWA_UNSECURED_EXPOSURE) {
		R86_RWA_UNSECURED_EXPOSURE = r86_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR86_RWA_UNSECURED() {
		return R86_RWA_UNSECURED;
	}
	public void setR86_RWA_UNSECURED(BigDecimal r86_RWA_UNSECURED) {
		R86_RWA_UNSECURED = r86_RWA_UNSECURED;
	}
	public BigDecimal getR86_TOTAL_RWA() {
		return R86_TOTAL_RWA;
	}
	public void setR86_TOTAL_RWA(BigDecimal r86_TOTAL_RWA) {
		R86_TOTAL_RWA = r86_TOTAL_RWA;
	}
	public BigDecimal getR87_EXPOSURE_BEFORE_CRM() {
		return R87_EXPOSURE_BEFORE_CRM;
	}
	public void setR87_EXPOSURE_BEFORE_CRM(BigDecimal r87_EXPOSURE_BEFORE_CRM) {
		R87_EXPOSURE_BEFORE_CRM = r87_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR87_SPEC_PROVISION_PAST_DUE() {
		return R87_SPEC_PROVISION_PAST_DUE;
	}
	public void setR87_SPEC_PROVISION_PAST_DUE(BigDecimal r87_SPEC_PROVISION_PAST_DUE) {
		R87_SPEC_PROVISION_PAST_DUE = r87_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR87_ON_BAL_SHEET_NETTING_ELIG() {
		return R87_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR87_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r87_ON_BAL_SHEET_NETTING_ELIG) {
		R87_ON_BAL_SHEET_NETTING_ELIG = r87_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR87_TOTAL_EXPOSURE_AFTER_NET() {
		return R87_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR87_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r87_TOTAL_EXPOSURE_AFTER_NET) {
		R87_TOTAL_EXPOSURE_AFTER_NET = r87_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR87_CRM_ELIG_EXPOSURE_SUBS() {
		return R87_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR87_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r87_CRM_ELIG_EXPOSURE_SUBS) {
		R87_CRM_ELIG_EXPOSURE_SUBS = r87_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR87_ELIG_GUARANTEES() {
		return R87_ELIG_GUARANTEES;
	}
	public void setR87_ELIG_GUARANTEES(BigDecimal r87_ELIG_GUARANTEES) {
		R87_ELIG_GUARANTEES = r87_ELIG_GUARANTEES;
	}
	public BigDecimal getR87_CREDIT_DERIVATIVES() {
		return R87_CREDIT_DERIVATIVES;
	}
	public void setR87_CREDIT_DERIVATIVES(BigDecimal r87_CREDIT_DERIVATIVES) {
		R87_CREDIT_DERIVATIVES = r87_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR87_CRM_COVERED_EXPOSURE() {
		return R87_CRM_COVERED_EXPOSURE;
	}
	public void setR87_CRM_COVERED_EXPOSURE(BigDecimal r87_CRM_COVERED_EXPOSURE) {
		R87_CRM_COVERED_EXPOSURE = r87_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR87_CRM_NOT_COVERED_EXPOSURE() {
		return R87_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR87_CRM_NOT_COVERED_EXPOSURE(BigDecimal r87_CRM_NOT_COVERED_EXPOSURE) {
		R87_CRM_NOT_COVERED_EXPOSURE = r87_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR87_CRM_RISK_WEIGHT() {
		return R87_CRM_RISK_WEIGHT;
	}
	public void setR87_CRM_RISK_WEIGHT(BigDecimal r87_CRM_RISK_WEIGHT) {
		R87_CRM_RISK_WEIGHT = r87_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR87_RWA_CRM_COVERED() {
		return R87_RWA_CRM_COVERED;
	}
	public void setR87_RWA_CRM_COVERED(BigDecimal r87_RWA_CRM_COVERED) {
		R87_RWA_CRM_COVERED = r87_RWA_CRM_COVERED;
	}
	public BigDecimal getR87_ORIG_COUNTERPARTY_RW() {
		return R87_ORIG_COUNTERPARTY_RW;
	}
	public void setR87_ORIG_COUNTERPARTY_RW(BigDecimal r87_ORIG_COUNTERPARTY_RW) {
		R87_ORIG_COUNTERPARTY_RW = r87_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR87_RWA_CRM_NOT_COVERED() {
		return R87_RWA_CRM_NOT_COVERED;
	}
	public void setR87_RWA_CRM_NOT_COVERED(BigDecimal r87_RWA_CRM_NOT_COVERED) {
		R87_RWA_CRM_NOT_COVERED = r87_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR87_CRM_ELIG_EXPOSURE_COMP() {
		return R87_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR87_CRM_ELIG_EXPOSURE_COMP(BigDecimal r87_CRM_ELIG_EXPOSURE_COMP) {
		R87_CRM_ELIG_EXPOSURE_COMP = r87_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR87_EXPOSURE_AFTER_VOL_ADJ() {
		return R87_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR87_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r87_EXPOSURE_AFTER_VOL_ADJ) {
		R87_EXPOSURE_AFTER_VOL_ADJ = r87_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR87_COLLATERAL_CASH() {
		return R87_COLLATERAL_CASH;
	}
	public void setR87_COLLATERAL_CASH(BigDecimal r87_COLLATERAL_CASH) {
		R87_COLLATERAL_CASH = r87_COLLATERAL_CASH;
	}
	public BigDecimal getR87_COLLATERAL_TBILLS() {
		return R87_COLLATERAL_TBILLS;
	}
	public void setR87_COLLATERAL_TBILLS(BigDecimal r87_COLLATERAL_TBILLS) {
		R87_COLLATERAL_TBILLS = r87_COLLATERAL_TBILLS;
	}
	public BigDecimal getR87_COLLATERAL_DEBT_SEC() {
		return R87_COLLATERAL_DEBT_SEC;
	}
	public void setR87_COLLATERAL_DEBT_SEC(BigDecimal r87_COLLATERAL_DEBT_SEC) {
		R87_COLLATERAL_DEBT_SEC = r87_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR87_COLLATERAL_EQUITIES() {
		return R87_COLLATERAL_EQUITIES;
	}
	public void setR87_COLLATERAL_EQUITIES(BigDecimal r87_COLLATERAL_EQUITIES) {
		R87_COLLATERAL_EQUITIES = r87_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR87_COLLATERAL_MUTUAL_FUNDS() {
		return R87_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR87_COLLATERAL_MUTUAL_FUNDS(BigDecimal r87_COLLATERAL_MUTUAL_FUNDS) {
		R87_COLLATERAL_MUTUAL_FUNDS = r87_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR87_TOTAL_COLLATERAL_HAIRCUT() {
		return R87_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR87_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r87_TOTAL_COLLATERAL_HAIRCUT) {
		R87_TOTAL_COLLATERAL_HAIRCUT = r87_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR87_EXPOSURE_AFTER_CRM() {
		return R87_EXPOSURE_AFTER_CRM;
	}
	public void setR87_EXPOSURE_AFTER_CRM(BigDecimal r87_EXPOSURE_AFTER_CRM) {
		R87_EXPOSURE_AFTER_CRM = r87_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR87_RWA_NOT_COVERED_CRM() {
		return R87_RWA_NOT_COVERED_CRM;
	}
	public void setR87_RWA_NOT_COVERED_CRM(BigDecimal r87_RWA_NOT_COVERED_CRM) {
		R87_RWA_NOT_COVERED_CRM = r87_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR87_RWA_UNSECURED_EXPOSURE() {
		return R87_RWA_UNSECURED_EXPOSURE;
	}
	public void setR87_RWA_UNSECURED_EXPOSURE(BigDecimal r87_RWA_UNSECURED_EXPOSURE) {
		R87_RWA_UNSECURED_EXPOSURE = r87_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR87_RWA_UNSECURED() {
		return R87_RWA_UNSECURED;
	}
	public void setR87_RWA_UNSECURED(BigDecimal r87_RWA_UNSECURED) {
		R87_RWA_UNSECURED = r87_RWA_UNSECURED;
	}
	public BigDecimal getR87_TOTAL_RWA() {
		return R87_TOTAL_RWA;
	}
	public void setR87_TOTAL_RWA(BigDecimal r87_TOTAL_RWA) {
		R87_TOTAL_RWA = r87_TOTAL_RWA;
	}
	public BigDecimal getR88_EXPOSURE_BEFORE_CRM() {
		return R88_EXPOSURE_BEFORE_CRM;
	}
	public void setR88_EXPOSURE_BEFORE_CRM(BigDecimal r88_EXPOSURE_BEFORE_CRM) {
		R88_EXPOSURE_BEFORE_CRM = r88_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR88_SPEC_PROVISION_PAST_DUE() {
		return R88_SPEC_PROVISION_PAST_DUE;
	}
	public void setR88_SPEC_PROVISION_PAST_DUE(BigDecimal r88_SPEC_PROVISION_PAST_DUE) {
		R88_SPEC_PROVISION_PAST_DUE = r88_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR88_ON_BAL_SHEET_NETTING_ELIG() {
		return R88_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR88_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r88_ON_BAL_SHEET_NETTING_ELIG) {
		R88_ON_BAL_SHEET_NETTING_ELIG = r88_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR88_TOTAL_EXPOSURE_AFTER_NET() {
		return R88_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR88_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r88_TOTAL_EXPOSURE_AFTER_NET) {
		R88_TOTAL_EXPOSURE_AFTER_NET = r88_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR88_CRM_ELIG_EXPOSURE_SUBS() {
		return R88_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR88_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r88_CRM_ELIG_EXPOSURE_SUBS) {
		R88_CRM_ELIG_EXPOSURE_SUBS = r88_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR88_ELIG_GUARANTEES() {
		return R88_ELIG_GUARANTEES;
	}
	public void setR88_ELIG_GUARANTEES(BigDecimal r88_ELIG_GUARANTEES) {
		R88_ELIG_GUARANTEES = r88_ELIG_GUARANTEES;
	}
	public BigDecimal getR88_CREDIT_DERIVATIVES() {
		return R88_CREDIT_DERIVATIVES;
	}
	public void setR88_CREDIT_DERIVATIVES(BigDecimal r88_CREDIT_DERIVATIVES) {
		R88_CREDIT_DERIVATIVES = r88_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR88_CRM_COVERED_EXPOSURE() {
		return R88_CRM_COVERED_EXPOSURE;
	}
	public void setR88_CRM_COVERED_EXPOSURE(BigDecimal r88_CRM_COVERED_EXPOSURE) {
		R88_CRM_COVERED_EXPOSURE = r88_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR88_CRM_NOT_COVERED_EXPOSURE() {
		return R88_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR88_CRM_NOT_COVERED_EXPOSURE(BigDecimal r88_CRM_NOT_COVERED_EXPOSURE) {
		R88_CRM_NOT_COVERED_EXPOSURE = r88_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR88_CRM_RISK_WEIGHT() {
		return R88_CRM_RISK_WEIGHT;
	}
	public void setR88_CRM_RISK_WEIGHT(BigDecimal r88_CRM_RISK_WEIGHT) {
		R88_CRM_RISK_WEIGHT = r88_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR88_RWA_CRM_COVERED() {
		return R88_RWA_CRM_COVERED;
	}
	public void setR88_RWA_CRM_COVERED(BigDecimal r88_RWA_CRM_COVERED) {
		R88_RWA_CRM_COVERED = r88_RWA_CRM_COVERED;
	}
	public BigDecimal getR88_ORIG_COUNTERPARTY_RW() {
		return R88_ORIG_COUNTERPARTY_RW;
	}
	public void setR88_ORIG_COUNTERPARTY_RW(BigDecimal r88_ORIG_COUNTERPARTY_RW) {
		R88_ORIG_COUNTERPARTY_RW = r88_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR88_RWA_CRM_NOT_COVERED() {
		return R88_RWA_CRM_NOT_COVERED;
	}
	public void setR88_RWA_CRM_NOT_COVERED(BigDecimal r88_RWA_CRM_NOT_COVERED) {
		R88_RWA_CRM_NOT_COVERED = r88_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR88_CRM_ELIG_EXPOSURE_COMP() {
		return R88_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR88_CRM_ELIG_EXPOSURE_COMP(BigDecimal r88_CRM_ELIG_EXPOSURE_COMP) {
		R88_CRM_ELIG_EXPOSURE_COMP = r88_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR88_EXPOSURE_AFTER_VOL_ADJ() {
		return R88_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR88_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r88_EXPOSURE_AFTER_VOL_ADJ) {
		R88_EXPOSURE_AFTER_VOL_ADJ = r88_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR88_COLLATERAL_CASH() {
		return R88_COLLATERAL_CASH;
	}
	public void setR88_COLLATERAL_CASH(BigDecimal r88_COLLATERAL_CASH) {
		R88_COLLATERAL_CASH = r88_COLLATERAL_CASH;
	}
	public BigDecimal getR88_COLLATERAL_TBILLS() {
		return R88_COLLATERAL_TBILLS;
	}
	public void setR88_COLLATERAL_TBILLS(BigDecimal r88_COLLATERAL_TBILLS) {
		R88_COLLATERAL_TBILLS = r88_COLLATERAL_TBILLS;
	}
	public BigDecimal getR88_COLLATERAL_DEBT_SEC() {
		return R88_COLLATERAL_DEBT_SEC;
	}
	public void setR88_COLLATERAL_DEBT_SEC(BigDecimal r88_COLLATERAL_DEBT_SEC) {
		R88_COLLATERAL_DEBT_SEC = r88_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR88_COLLATERAL_EQUITIES() {
		return R88_COLLATERAL_EQUITIES;
	}
	public void setR88_COLLATERAL_EQUITIES(BigDecimal r88_COLLATERAL_EQUITIES) {
		R88_COLLATERAL_EQUITIES = r88_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR88_COLLATERAL_MUTUAL_FUNDS() {
		return R88_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR88_COLLATERAL_MUTUAL_FUNDS(BigDecimal r88_COLLATERAL_MUTUAL_FUNDS) {
		R88_COLLATERAL_MUTUAL_FUNDS = r88_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR88_TOTAL_COLLATERAL_HAIRCUT() {
		return R88_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR88_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r88_TOTAL_COLLATERAL_HAIRCUT) {
		R88_TOTAL_COLLATERAL_HAIRCUT = r88_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR88_EXPOSURE_AFTER_CRM() {
		return R88_EXPOSURE_AFTER_CRM;
	}
	public void setR88_EXPOSURE_AFTER_CRM(BigDecimal r88_EXPOSURE_AFTER_CRM) {
		R88_EXPOSURE_AFTER_CRM = r88_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR88_RWA_NOT_COVERED_CRM() {
		return R88_RWA_NOT_COVERED_CRM;
	}
	public void setR88_RWA_NOT_COVERED_CRM(BigDecimal r88_RWA_NOT_COVERED_CRM) {
		R88_RWA_NOT_COVERED_CRM = r88_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR88_RWA_UNSECURED_EXPOSURE() {
		return R88_RWA_UNSECURED_EXPOSURE;
	}
	public void setR88_RWA_UNSECURED_EXPOSURE(BigDecimal r88_RWA_UNSECURED_EXPOSURE) {
		R88_RWA_UNSECURED_EXPOSURE = r88_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR88_RWA_UNSECURED() {
		return R88_RWA_UNSECURED;
	}
	public void setR88_RWA_UNSECURED(BigDecimal r88_RWA_UNSECURED) {
		R88_RWA_UNSECURED = r88_RWA_UNSECURED;
	}
	public BigDecimal getR88_TOTAL_RWA() {
		return R88_TOTAL_RWA;
	}
	public void setR88_TOTAL_RWA(BigDecimal r88_TOTAL_RWA) {
		R88_TOTAL_RWA = r88_TOTAL_RWA;
	}
	public BigDecimal getR89_EXPOSURE_BEFORE_CRM() {
		return R89_EXPOSURE_BEFORE_CRM;
	}
	public void setR89_EXPOSURE_BEFORE_CRM(BigDecimal r89_EXPOSURE_BEFORE_CRM) {
		R89_EXPOSURE_BEFORE_CRM = r89_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR89_SPEC_PROVISION_PAST_DUE() {
		return R89_SPEC_PROVISION_PAST_DUE;
	}
	public void setR89_SPEC_PROVISION_PAST_DUE(BigDecimal r89_SPEC_PROVISION_PAST_DUE) {
		R89_SPEC_PROVISION_PAST_DUE = r89_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR89_ON_BAL_SHEET_NETTING_ELIG() {
		return R89_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR89_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r89_ON_BAL_SHEET_NETTING_ELIG) {
		R89_ON_BAL_SHEET_NETTING_ELIG = r89_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR89_TOTAL_EXPOSURE_AFTER_NET() {
		return R89_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR89_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r89_TOTAL_EXPOSURE_AFTER_NET) {
		R89_TOTAL_EXPOSURE_AFTER_NET = r89_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR89_CRM_ELIG_EXPOSURE_SUBS() {
		return R89_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR89_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r89_CRM_ELIG_EXPOSURE_SUBS) {
		R89_CRM_ELIG_EXPOSURE_SUBS = r89_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR89_ELIG_GUARANTEES() {
		return R89_ELIG_GUARANTEES;
	}
	public void setR89_ELIG_GUARANTEES(BigDecimal r89_ELIG_GUARANTEES) {
		R89_ELIG_GUARANTEES = r89_ELIG_GUARANTEES;
	}
	public BigDecimal getR89_CREDIT_DERIVATIVES() {
		return R89_CREDIT_DERIVATIVES;
	}
	public void setR89_CREDIT_DERIVATIVES(BigDecimal r89_CREDIT_DERIVATIVES) {
		R89_CREDIT_DERIVATIVES = r89_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR89_CRM_COVERED_EXPOSURE() {
		return R89_CRM_COVERED_EXPOSURE;
	}
	public void setR89_CRM_COVERED_EXPOSURE(BigDecimal r89_CRM_COVERED_EXPOSURE) {
		R89_CRM_COVERED_EXPOSURE = r89_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR89_CRM_NOT_COVERED_EXPOSURE() {
		return R89_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR89_CRM_NOT_COVERED_EXPOSURE(BigDecimal r89_CRM_NOT_COVERED_EXPOSURE) {
		R89_CRM_NOT_COVERED_EXPOSURE = r89_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR89_CRM_RISK_WEIGHT() {
		return R89_CRM_RISK_WEIGHT;
	}
	public void setR89_CRM_RISK_WEIGHT(BigDecimal r89_CRM_RISK_WEIGHT) {
		R89_CRM_RISK_WEIGHT = r89_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR89_RWA_CRM_COVERED() {
		return R89_RWA_CRM_COVERED;
	}
	public void setR89_RWA_CRM_COVERED(BigDecimal r89_RWA_CRM_COVERED) {
		R89_RWA_CRM_COVERED = r89_RWA_CRM_COVERED;
	}
	public BigDecimal getR89_ORIG_COUNTERPARTY_RW() {
		return R89_ORIG_COUNTERPARTY_RW;
	}
	public void setR89_ORIG_COUNTERPARTY_RW(BigDecimal r89_ORIG_COUNTERPARTY_RW) {
		R89_ORIG_COUNTERPARTY_RW = r89_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR89_RWA_CRM_NOT_COVERED() {
		return R89_RWA_CRM_NOT_COVERED;
	}
	public void setR89_RWA_CRM_NOT_COVERED(BigDecimal r89_RWA_CRM_NOT_COVERED) {
		R89_RWA_CRM_NOT_COVERED = r89_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR89_CRM_ELIG_EXPOSURE_COMP() {
		return R89_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR89_CRM_ELIG_EXPOSURE_COMP(BigDecimal r89_CRM_ELIG_EXPOSURE_COMP) {
		R89_CRM_ELIG_EXPOSURE_COMP = r89_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR89_EXPOSURE_AFTER_VOL_ADJ() {
		return R89_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR89_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r89_EXPOSURE_AFTER_VOL_ADJ) {
		R89_EXPOSURE_AFTER_VOL_ADJ = r89_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR89_COLLATERAL_CASH() {
		return R89_COLLATERAL_CASH;
	}
	public void setR89_COLLATERAL_CASH(BigDecimal r89_COLLATERAL_CASH) {
		R89_COLLATERAL_CASH = r89_COLLATERAL_CASH;
	}
	public BigDecimal getR89_COLLATERAL_TBILLS() {
		return R89_COLLATERAL_TBILLS;
	}
	public void setR89_COLLATERAL_TBILLS(BigDecimal r89_COLLATERAL_TBILLS) {
		R89_COLLATERAL_TBILLS = r89_COLLATERAL_TBILLS;
	}
	public BigDecimal getR89_COLLATERAL_DEBT_SEC() {
		return R89_COLLATERAL_DEBT_SEC;
	}
	public void setR89_COLLATERAL_DEBT_SEC(BigDecimal r89_COLLATERAL_DEBT_SEC) {
		R89_COLLATERAL_DEBT_SEC = r89_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR89_COLLATERAL_EQUITIES() {
		return R89_COLLATERAL_EQUITIES;
	}
	public void setR89_COLLATERAL_EQUITIES(BigDecimal r89_COLLATERAL_EQUITIES) {
		R89_COLLATERAL_EQUITIES = r89_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR89_COLLATERAL_MUTUAL_FUNDS() {
		return R89_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR89_COLLATERAL_MUTUAL_FUNDS(BigDecimal r89_COLLATERAL_MUTUAL_FUNDS) {
		R89_COLLATERAL_MUTUAL_FUNDS = r89_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR89_TOTAL_COLLATERAL_HAIRCUT() {
		return R89_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR89_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r89_TOTAL_COLLATERAL_HAIRCUT) {
		R89_TOTAL_COLLATERAL_HAIRCUT = r89_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR89_EXPOSURE_AFTER_CRM() {
		return R89_EXPOSURE_AFTER_CRM;
	}
	public void setR89_EXPOSURE_AFTER_CRM(BigDecimal r89_EXPOSURE_AFTER_CRM) {
		R89_EXPOSURE_AFTER_CRM = r89_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR89_RWA_NOT_COVERED_CRM() {
		return R89_RWA_NOT_COVERED_CRM;
	}
	public void setR89_RWA_NOT_COVERED_CRM(BigDecimal r89_RWA_NOT_COVERED_CRM) {
		R89_RWA_NOT_COVERED_CRM = r89_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR89_RWA_UNSECURED_EXPOSURE() {
		return R89_RWA_UNSECURED_EXPOSURE;
	}
	public void setR89_RWA_UNSECURED_EXPOSURE(BigDecimal r89_RWA_UNSECURED_EXPOSURE) {
		R89_RWA_UNSECURED_EXPOSURE = r89_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR89_RWA_UNSECURED() {
		return R89_RWA_UNSECURED;
	}
	public void setR89_RWA_UNSECURED(BigDecimal r89_RWA_UNSECURED) {
		R89_RWA_UNSECURED = r89_RWA_UNSECURED;
	}
	public BigDecimal getR89_TOTAL_RWA() {
		return R89_TOTAL_RWA;
	}
	public void setR89_TOTAL_RWA(BigDecimal r89_TOTAL_RWA) {
		R89_TOTAL_RWA = r89_TOTAL_RWA;
	}
	public BigDecimal getR90_EXPOSURE_BEFORE_CRM() {
		return R90_EXPOSURE_BEFORE_CRM;
	}
	public void setR90_EXPOSURE_BEFORE_CRM(BigDecimal r90_EXPOSURE_BEFORE_CRM) {
		R90_EXPOSURE_BEFORE_CRM = r90_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR90_SPEC_PROVISION_PAST_DUE() {
		return R90_SPEC_PROVISION_PAST_DUE;
	}
	public void setR90_SPEC_PROVISION_PAST_DUE(BigDecimal r90_SPEC_PROVISION_PAST_DUE) {
		R90_SPEC_PROVISION_PAST_DUE = r90_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR90_ON_BAL_SHEET_NETTING_ELIG() {
		return R90_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR90_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r90_ON_BAL_SHEET_NETTING_ELIG) {
		R90_ON_BAL_SHEET_NETTING_ELIG = r90_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR90_TOTAL_EXPOSURE_AFTER_NET() {
		return R90_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR90_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r90_TOTAL_EXPOSURE_AFTER_NET) {
		R90_TOTAL_EXPOSURE_AFTER_NET = r90_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR90_CRM_ELIG_EXPOSURE_SUBS() {
		return R90_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR90_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r90_CRM_ELIG_EXPOSURE_SUBS) {
		R90_CRM_ELIG_EXPOSURE_SUBS = r90_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR90_ELIG_GUARANTEES() {
		return R90_ELIG_GUARANTEES;
	}
	public void setR90_ELIG_GUARANTEES(BigDecimal r90_ELIG_GUARANTEES) {
		R90_ELIG_GUARANTEES = r90_ELIG_GUARANTEES;
	}
	public BigDecimal getR90_CREDIT_DERIVATIVES() {
		return R90_CREDIT_DERIVATIVES;
	}
	public void setR90_CREDIT_DERIVATIVES(BigDecimal r90_CREDIT_DERIVATIVES) {
		R90_CREDIT_DERIVATIVES = r90_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR90_CRM_COVERED_EXPOSURE() {
		return R90_CRM_COVERED_EXPOSURE;
	}
	public void setR90_CRM_COVERED_EXPOSURE(BigDecimal r90_CRM_COVERED_EXPOSURE) {
		R90_CRM_COVERED_EXPOSURE = r90_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR90_CRM_NOT_COVERED_EXPOSURE() {
		return R90_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR90_CRM_NOT_COVERED_EXPOSURE(BigDecimal r90_CRM_NOT_COVERED_EXPOSURE) {
		R90_CRM_NOT_COVERED_EXPOSURE = r90_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR90_CRM_RISK_WEIGHT() {
		return R90_CRM_RISK_WEIGHT;
	}
	public void setR90_CRM_RISK_WEIGHT(BigDecimal r90_CRM_RISK_WEIGHT) {
		R90_CRM_RISK_WEIGHT = r90_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR90_RWA_CRM_COVERED() {
		return R90_RWA_CRM_COVERED;
	}
	public void setR90_RWA_CRM_COVERED(BigDecimal r90_RWA_CRM_COVERED) {
		R90_RWA_CRM_COVERED = r90_RWA_CRM_COVERED;
	}
	public BigDecimal getR90_ORIG_COUNTERPARTY_RW() {
		return R90_ORIG_COUNTERPARTY_RW;
	}
	public void setR90_ORIG_COUNTERPARTY_RW(BigDecimal r90_ORIG_COUNTERPARTY_RW) {
		R90_ORIG_COUNTERPARTY_RW = r90_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR90_RWA_CRM_NOT_COVERED() {
		return R90_RWA_CRM_NOT_COVERED;
	}
	public void setR90_RWA_CRM_NOT_COVERED(BigDecimal r90_RWA_CRM_NOT_COVERED) {
		R90_RWA_CRM_NOT_COVERED = r90_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR90_CRM_ELIG_EXPOSURE_COMP() {
		return R90_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR90_CRM_ELIG_EXPOSURE_COMP(BigDecimal r90_CRM_ELIG_EXPOSURE_COMP) {
		R90_CRM_ELIG_EXPOSURE_COMP = r90_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR90_EXPOSURE_AFTER_VOL_ADJ() {
		return R90_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR90_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r90_EXPOSURE_AFTER_VOL_ADJ) {
		R90_EXPOSURE_AFTER_VOL_ADJ = r90_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR90_COLLATERAL_CASH() {
		return R90_COLLATERAL_CASH;
	}
	public void setR90_COLLATERAL_CASH(BigDecimal r90_COLLATERAL_CASH) {
		R90_COLLATERAL_CASH = r90_COLLATERAL_CASH;
	}
	public BigDecimal getR90_COLLATERAL_TBILLS() {
		return R90_COLLATERAL_TBILLS;
	}
	public void setR90_COLLATERAL_TBILLS(BigDecimal r90_COLLATERAL_TBILLS) {
		R90_COLLATERAL_TBILLS = r90_COLLATERAL_TBILLS;
	}
	public BigDecimal getR90_COLLATERAL_DEBT_SEC() {
		return R90_COLLATERAL_DEBT_SEC;
	}
	public void setR90_COLLATERAL_DEBT_SEC(BigDecimal r90_COLLATERAL_DEBT_SEC) {
		R90_COLLATERAL_DEBT_SEC = r90_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR90_COLLATERAL_EQUITIES() {
		return R90_COLLATERAL_EQUITIES;
	}
	public void setR90_COLLATERAL_EQUITIES(BigDecimal r90_COLLATERAL_EQUITIES) {
		R90_COLLATERAL_EQUITIES = r90_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR90_COLLATERAL_MUTUAL_FUNDS() {
		return R90_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR90_COLLATERAL_MUTUAL_FUNDS(BigDecimal r90_COLLATERAL_MUTUAL_FUNDS) {
		R90_COLLATERAL_MUTUAL_FUNDS = r90_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR90_TOTAL_COLLATERAL_HAIRCUT() {
		return R90_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR90_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r90_TOTAL_COLLATERAL_HAIRCUT) {
		R90_TOTAL_COLLATERAL_HAIRCUT = r90_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR90_EXPOSURE_AFTER_CRM() {
		return R90_EXPOSURE_AFTER_CRM;
	}
	public void setR90_EXPOSURE_AFTER_CRM(BigDecimal r90_EXPOSURE_AFTER_CRM) {
		R90_EXPOSURE_AFTER_CRM = r90_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR90_RWA_NOT_COVERED_CRM() {
		return R90_RWA_NOT_COVERED_CRM;
	}
	public void setR90_RWA_NOT_COVERED_CRM(BigDecimal r90_RWA_NOT_COVERED_CRM) {
		R90_RWA_NOT_COVERED_CRM = r90_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR90_RWA_UNSECURED_EXPOSURE() {
		return R90_RWA_UNSECURED_EXPOSURE;
	}
	public void setR90_RWA_UNSECURED_EXPOSURE(BigDecimal r90_RWA_UNSECURED_EXPOSURE) {
		R90_RWA_UNSECURED_EXPOSURE = r90_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR90_RWA_UNSECURED() {
		return R90_RWA_UNSECURED;
	}
	public void setR90_RWA_UNSECURED(BigDecimal r90_RWA_UNSECURED) {
		R90_RWA_UNSECURED = r90_RWA_UNSECURED;
	}
	public BigDecimal getR90_TOTAL_RWA() {
		return R90_TOTAL_RWA;
	}
	public void setR90_TOTAL_RWA(BigDecimal r90_TOTAL_RWA) {
		R90_TOTAL_RWA = r90_TOTAL_RWA;
	}
	public BigDecimal getR91_EXPOSURE_BEFORE_CRM() {
		return R91_EXPOSURE_BEFORE_CRM;
	}
	public void setR91_EXPOSURE_BEFORE_CRM(BigDecimal r91_EXPOSURE_BEFORE_CRM) {
		R91_EXPOSURE_BEFORE_CRM = r91_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR91_SPEC_PROVISION_PAST_DUE() {
		return R91_SPEC_PROVISION_PAST_DUE;
	}
	public void setR91_SPEC_PROVISION_PAST_DUE(BigDecimal r91_SPEC_PROVISION_PAST_DUE) {
		R91_SPEC_PROVISION_PAST_DUE = r91_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR91_ON_BAL_SHEET_NETTING_ELIG() {
		return R91_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR91_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r91_ON_BAL_SHEET_NETTING_ELIG) {
		R91_ON_BAL_SHEET_NETTING_ELIG = r91_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR91_TOTAL_EXPOSURE_AFTER_NET() {
		return R91_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR91_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r91_TOTAL_EXPOSURE_AFTER_NET) {
		R91_TOTAL_EXPOSURE_AFTER_NET = r91_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR91_CRM_ELIG_EXPOSURE_SUBS() {
		return R91_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR91_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r91_CRM_ELIG_EXPOSURE_SUBS) {
		R91_CRM_ELIG_EXPOSURE_SUBS = r91_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR91_ELIG_GUARANTEES() {
		return R91_ELIG_GUARANTEES;
	}
	public void setR91_ELIG_GUARANTEES(BigDecimal r91_ELIG_GUARANTEES) {
		R91_ELIG_GUARANTEES = r91_ELIG_GUARANTEES;
	}
	public BigDecimal getR91_CREDIT_DERIVATIVES() {
		return R91_CREDIT_DERIVATIVES;
	}
	public void setR91_CREDIT_DERIVATIVES(BigDecimal r91_CREDIT_DERIVATIVES) {
		R91_CREDIT_DERIVATIVES = r91_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR91_CRM_COVERED_EXPOSURE() {
		return R91_CRM_COVERED_EXPOSURE;
	}
	public void setR91_CRM_COVERED_EXPOSURE(BigDecimal r91_CRM_COVERED_EXPOSURE) {
		R91_CRM_COVERED_EXPOSURE = r91_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR91_CRM_NOT_COVERED_EXPOSURE() {
		return R91_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR91_CRM_NOT_COVERED_EXPOSURE(BigDecimal r91_CRM_NOT_COVERED_EXPOSURE) {
		R91_CRM_NOT_COVERED_EXPOSURE = r91_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR91_CRM_RISK_WEIGHT() {
		return R91_CRM_RISK_WEIGHT;
	}
	public void setR91_CRM_RISK_WEIGHT(BigDecimal r91_CRM_RISK_WEIGHT) {
		R91_CRM_RISK_WEIGHT = r91_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR91_RWA_CRM_COVERED() {
		return R91_RWA_CRM_COVERED;
	}
	public void setR91_RWA_CRM_COVERED(BigDecimal r91_RWA_CRM_COVERED) {
		R91_RWA_CRM_COVERED = r91_RWA_CRM_COVERED;
	}
	public BigDecimal getR91_ORIG_COUNTERPARTY_RW() {
		return R91_ORIG_COUNTERPARTY_RW;
	}
	public void setR91_ORIG_COUNTERPARTY_RW(BigDecimal r91_ORIG_COUNTERPARTY_RW) {
		R91_ORIG_COUNTERPARTY_RW = r91_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR91_RWA_CRM_NOT_COVERED() {
		return R91_RWA_CRM_NOT_COVERED;
	}
	public void setR91_RWA_CRM_NOT_COVERED(BigDecimal r91_RWA_CRM_NOT_COVERED) {
		R91_RWA_CRM_NOT_COVERED = r91_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR91_CRM_ELIG_EXPOSURE_COMP() {
		return R91_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR91_CRM_ELIG_EXPOSURE_COMP(BigDecimal r91_CRM_ELIG_EXPOSURE_COMP) {
		R91_CRM_ELIG_EXPOSURE_COMP = r91_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR91_EXPOSURE_AFTER_VOL_ADJ() {
		return R91_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR91_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r91_EXPOSURE_AFTER_VOL_ADJ) {
		R91_EXPOSURE_AFTER_VOL_ADJ = r91_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR91_COLLATERAL_CASH() {
		return R91_COLLATERAL_CASH;
	}
	public void setR91_COLLATERAL_CASH(BigDecimal r91_COLLATERAL_CASH) {
		R91_COLLATERAL_CASH = r91_COLLATERAL_CASH;
	}
	public BigDecimal getR91_COLLATERAL_TBILLS() {
		return R91_COLLATERAL_TBILLS;
	}
	public void setR91_COLLATERAL_TBILLS(BigDecimal r91_COLLATERAL_TBILLS) {
		R91_COLLATERAL_TBILLS = r91_COLLATERAL_TBILLS;
	}
	public BigDecimal getR91_COLLATERAL_DEBT_SEC() {
		return R91_COLLATERAL_DEBT_SEC;
	}
	public void setR91_COLLATERAL_DEBT_SEC(BigDecimal r91_COLLATERAL_DEBT_SEC) {
		R91_COLLATERAL_DEBT_SEC = r91_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR91_COLLATERAL_EQUITIES() {
		return R91_COLLATERAL_EQUITIES;
	}
	public void setR91_COLLATERAL_EQUITIES(BigDecimal r91_COLLATERAL_EQUITIES) {
		R91_COLLATERAL_EQUITIES = r91_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR91_COLLATERAL_MUTUAL_FUNDS() {
		return R91_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR91_COLLATERAL_MUTUAL_FUNDS(BigDecimal r91_COLLATERAL_MUTUAL_FUNDS) {
		R91_COLLATERAL_MUTUAL_FUNDS = r91_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR91_TOTAL_COLLATERAL_HAIRCUT() {
		return R91_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR91_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r91_TOTAL_COLLATERAL_HAIRCUT) {
		R91_TOTAL_COLLATERAL_HAIRCUT = r91_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR91_EXPOSURE_AFTER_CRM() {
		return R91_EXPOSURE_AFTER_CRM;
	}
	public void setR91_EXPOSURE_AFTER_CRM(BigDecimal r91_EXPOSURE_AFTER_CRM) {
		R91_EXPOSURE_AFTER_CRM = r91_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR91_RWA_NOT_COVERED_CRM() {
		return R91_RWA_NOT_COVERED_CRM;
	}
	public void setR91_RWA_NOT_COVERED_CRM(BigDecimal r91_RWA_NOT_COVERED_CRM) {
		R91_RWA_NOT_COVERED_CRM = r91_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR91_RWA_UNSECURED_EXPOSURE() {
		return R91_RWA_UNSECURED_EXPOSURE;
	}
	public void setR91_RWA_UNSECURED_EXPOSURE(BigDecimal r91_RWA_UNSECURED_EXPOSURE) {
		R91_RWA_UNSECURED_EXPOSURE = r91_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR91_RWA_UNSECURED() {
		return R91_RWA_UNSECURED;
	}
	public void setR91_RWA_UNSECURED(BigDecimal r91_RWA_UNSECURED) {
		R91_RWA_UNSECURED = r91_RWA_UNSECURED;
	}
	public BigDecimal getR91_TOTAL_RWA() {
		return R91_TOTAL_RWA;
	}
	public void setR91_TOTAL_RWA(BigDecimal r91_TOTAL_RWA) {
		R91_TOTAL_RWA = r91_TOTAL_RWA;
	}
	public BigDecimal getR92_EXPOSURE_BEFORE_CRM() {
		return R92_EXPOSURE_BEFORE_CRM;
	}
	public void setR92_EXPOSURE_BEFORE_CRM(BigDecimal r92_EXPOSURE_BEFORE_CRM) {
		R92_EXPOSURE_BEFORE_CRM = r92_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR92_SPEC_PROVISION_PAST_DUE() {
		return R92_SPEC_PROVISION_PAST_DUE;
	}
	public void setR92_SPEC_PROVISION_PAST_DUE(BigDecimal r92_SPEC_PROVISION_PAST_DUE) {
		R92_SPEC_PROVISION_PAST_DUE = r92_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR92_ON_BAL_SHEET_NETTING_ELIG() {
		return R92_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR92_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r92_ON_BAL_SHEET_NETTING_ELIG) {
		R92_ON_BAL_SHEET_NETTING_ELIG = r92_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR92_TOTAL_EXPOSURE_AFTER_NET() {
		return R92_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR92_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r92_TOTAL_EXPOSURE_AFTER_NET) {
		R92_TOTAL_EXPOSURE_AFTER_NET = r92_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR92_CRM_ELIG_EXPOSURE_SUBS() {
		return R92_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR92_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r92_CRM_ELIG_EXPOSURE_SUBS) {
		R92_CRM_ELIG_EXPOSURE_SUBS = r92_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR92_ELIG_GUARANTEES() {
		return R92_ELIG_GUARANTEES;
	}
	public void setR92_ELIG_GUARANTEES(BigDecimal r92_ELIG_GUARANTEES) {
		R92_ELIG_GUARANTEES = r92_ELIG_GUARANTEES;
	}
	public BigDecimal getR92_CREDIT_DERIVATIVES() {
		return R92_CREDIT_DERIVATIVES;
	}
	public void setR92_CREDIT_DERIVATIVES(BigDecimal r92_CREDIT_DERIVATIVES) {
		R92_CREDIT_DERIVATIVES = r92_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR92_CRM_COVERED_EXPOSURE() {
		return R92_CRM_COVERED_EXPOSURE;
	}
	public void setR92_CRM_COVERED_EXPOSURE(BigDecimal r92_CRM_COVERED_EXPOSURE) {
		R92_CRM_COVERED_EXPOSURE = r92_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR92_CRM_NOT_COVERED_EXPOSURE() {
		return R92_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR92_CRM_NOT_COVERED_EXPOSURE(BigDecimal r92_CRM_NOT_COVERED_EXPOSURE) {
		R92_CRM_NOT_COVERED_EXPOSURE = r92_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR92_CRM_RISK_WEIGHT() {
		return R92_CRM_RISK_WEIGHT;
	}
	public void setR92_CRM_RISK_WEIGHT(BigDecimal r92_CRM_RISK_WEIGHT) {
		R92_CRM_RISK_WEIGHT = r92_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR92_RWA_CRM_COVERED() {
		return R92_RWA_CRM_COVERED;
	}
	public void setR92_RWA_CRM_COVERED(BigDecimal r92_RWA_CRM_COVERED) {
		R92_RWA_CRM_COVERED = r92_RWA_CRM_COVERED;
	}
	public BigDecimal getR92_ORIG_COUNTERPARTY_RW() {
		return R92_ORIG_COUNTERPARTY_RW;
	}
	public void setR92_ORIG_COUNTERPARTY_RW(BigDecimal r92_ORIG_COUNTERPARTY_RW) {
		R92_ORIG_COUNTERPARTY_RW = r92_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR92_RWA_CRM_NOT_COVERED() {
		return R92_RWA_CRM_NOT_COVERED;
	}
	public void setR92_RWA_CRM_NOT_COVERED(BigDecimal r92_RWA_CRM_NOT_COVERED) {
		R92_RWA_CRM_NOT_COVERED = r92_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR92_CRM_ELIG_EXPOSURE_COMP() {
		return R92_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR92_CRM_ELIG_EXPOSURE_COMP(BigDecimal r92_CRM_ELIG_EXPOSURE_COMP) {
		R92_CRM_ELIG_EXPOSURE_COMP = r92_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR92_EXPOSURE_AFTER_VOL_ADJ() {
		return R92_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR92_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r92_EXPOSURE_AFTER_VOL_ADJ) {
		R92_EXPOSURE_AFTER_VOL_ADJ = r92_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR92_COLLATERAL_CASH() {
		return R92_COLLATERAL_CASH;
	}
	public void setR92_COLLATERAL_CASH(BigDecimal r92_COLLATERAL_CASH) {
		R92_COLLATERAL_CASH = r92_COLLATERAL_CASH;
	}
	public BigDecimal getR92_COLLATERAL_TBILLS() {
		return R92_COLLATERAL_TBILLS;
	}
	public void setR92_COLLATERAL_TBILLS(BigDecimal r92_COLLATERAL_TBILLS) {
		R92_COLLATERAL_TBILLS = r92_COLLATERAL_TBILLS;
	}
	public BigDecimal getR92_COLLATERAL_DEBT_SEC() {
		return R92_COLLATERAL_DEBT_SEC;
	}
	public void setR92_COLLATERAL_DEBT_SEC(BigDecimal r92_COLLATERAL_DEBT_SEC) {
		R92_COLLATERAL_DEBT_SEC = r92_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR92_COLLATERAL_EQUITIES() {
		return R92_COLLATERAL_EQUITIES;
	}
	public void setR92_COLLATERAL_EQUITIES(BigDecimal r92_COLLATERAL_EQUITIES) {
		R92_COLLATERAL_EQUITIES = r92_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR92_COLLATERAL_MUTUAL_FUNDS() {
		return R92_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR92_COLLATERAL_MUTUAL_FUNDS(BigDecimal r92_COLLATERAL_MUTUAL_FUNDS) {
		R92_COLLATERAL_MUTUAL_FUNDS = r92_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR92_TOTAL_COLLATERAL_HAIRCUT() {
		return R92_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR92_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r92_TOTAL_COLLATERAL_HAIRCUT) {
		R92_TOTAL_COLLATERAL_HAIRCUT = r92_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR92_EXPOSURE_AFTER_CRM() {
		return R92_EXPOSURE_AFTER_CRM;
	}
	public void setR92_EXPOSURE_AFTER_CRM(BigDecimal r92_EXPOSURE_AFTER_CRM) {
		R92_EXPOSURE_AFTER_CRM = r92_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR92_RWA_NOT_COVERED_CRM() {
		return R92_RWA_NOT_COVERED_CRM;
	}
	public void setR92_RWA_NOT_COVERED_CRM(BigDecimal r92_RWA_NOT_COVERED_CRM) {
		R92_RWA_NOT_COVERED_CRM = r92_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR92_RWA_UNSECURED_EXPOSURE() {
		return R92_RWA_UNSECURED_EXPOSURE;
	}
	public void setR92_RWA_UNSECURED_EXPOSURE(BigDecimal r92_RWA_UNSECURED_EXPOSURE) {
		R92_RWA_UNSECURED_EXPOSURE = r92_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR92_RWA_UNSECURED() {
		return R92_RWA_UNSECURED;
	}
	public void setR92_RWA_UNSECURED(BigDecimal r92_RWA_UNSECURED) {
		R92_RWA_UNSECURED = r92_RWA_UNSECURED;
	}
	public BigDecimal getR92_TOTAL_RWA() {
		return R92_TOTAL_RWA;
	}
	public void setR92_TOTAL_RWA(BigDecimal r92_TOTAL_RWA) {
		R92_TOTAL_RWA = r92_TOTAL_RWA;
	}
	public BigDecimal getR93_EXPOSURE_BEFORE_CRM() {
		return R93_EXPOSURE_BEFORE_CRM;
	}
	public void setR93_EXPOSURE_BEFORE_CRM(BigDecimal r93_EXPOSURE_BEFORE_CRM) {
		R93_EXPOSURE_BEFORE_CRM = r93_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR93_SPEC_PROVISION_PAST_DUE() {
		return R93_SPEC_PROVISION_PAST_DUE;
	}
	public void setR93_SPEC_PROVISION_PAST_DUE(BigDecimal r93_SPEC_PROVISION_PAST_DUE) {
		R93_SPEC_PROVISION_PAST_DUE = r93_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR93_ON_BAL_SHEET_NETTING_ELIG() {
		return R93_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR93_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r93_ON_BAL_SHEET_NETTING_ELIG) {
		R93_ON_BAL_SHEET_NETTING_ELIG = r93_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR93_TOTAL_EXPOSURE_AFTER_NET() {
		return R93_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR93_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r93_TOTAL_EXPOSURE_AFTER_NET) {
		R93_TOTAL_EXPOSURE_AFTER_NET = r93_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR93_CRM_ELIG_EXPOSURE_SUBS() {
		return R93_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR93_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r93_CRM_ELIG_EXPOSURE_SUBS) {
		R93_CRM_ELIG_EXPOSURE_SUBS = r93_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR93_ELIG_GUARANTEES() {
		return R93_ELIG_GUARANTEES;
	}
	public void setR93_ELIG_GUARANTEES(BigDecimal r93_ELIG_GUARANTEES) {
		R93_ELIG_GUARANTEES = r93_ELIG_GUARANTEES;
	}
	public BigDecimal getR93_CREDIT_DERIVATIVES() {
		return R93_CREDIT_DERIVATIVES;
	}
	public void setR93_CREDIT_DERIVATIVES(BigDecimal r93_CREDIT_DERIVATIVES) {
		R93_CREDIT_DERIVATIVES = r93_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR93_CRM_COVERED_EXPOSURE() {
		return R93_CRM_COVERED_EXPOSURE;
	}
	public void setR93_CRM_COVERED_EXPOSURE(BigDecimal r93_CRM_COVERED_EXPOSURE) {
		R93_CRM_COVERED_EXPOSURE = r93_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR93_CRM_NOT_COVERED_EXPOSURE() {
		return R93_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR93_CRM_NOT_COVERED_EXPOSURE(BigDecimal r93_CRM_NOT_COVERED_EXPOSURE) {
		R93_CRM_NOT_COVERED_EXPOSURE = r93_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR93_CRM_RISK_WEIGHT() {
		return R93_CRM_RISK_WEIGHT;
	}
	public void setR93_CRM_RISK_WEIGHT(BigDecimal r93_CRM_RISK_WEIGHT) {
		R93_CRM_RISK_WEIGHT = r93_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR93_RWA_CRM_COVERED() {
		return R93_RWA_CRM_COVERED;
	}
	public void setR93_RWA_CRM_COVERED(BigDecimal r93_RWA_CRM_COVERED) {
		R93_RWA_CRM_COVERED = r93_RWA_CRM_COVERED;
	}
	public BigDecimal getR93_ORIG_COUNTERPARTY_RW() {
		return R93_ORIG_COUNTERPARTY_RW;
	}
	public void setR93_ORIG_COUNTERPARTY_RW(BigDecimal r93_ORIG_COUNTERPARTY_RW) {
		R93_ORIG_COUNTERPARTY_RW = r93_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR93_RWA_CRM_NOT_COVERED() {
		return R93_RWA_CRM_NOT_COVERED;
	}
	public void setR93_RWA_CRM_NOT_COVERED(BigDecimal r93_RWA_CRM_NOT_COVERED) {
		R93_RWA_CRM_NOT_COVERED = r93_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR93_CRM_ELIG_EXPOSURE_COMP() {
		return R93_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR93_CRM_ELIG_EXPOSURE_COMP(BigDecimal r93_CRM_ELIG_EXPOSURE_COMP) {
		R93_CRM_ELIG_EXPOSURE_COMP = r93_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR93_EXPOSURE_AFTER_VOL_ADJ() {
		return R93_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR93_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r93_EXPOSURE_AFTER_VOL_ADJ) {
		R93_EXPOSURE_AFTER_VOL_ADJ = r93_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR93_COLLATERAL_CASH() {
		return R93_COLLATERAL_CASH;
	}
	public void setR93_COLLATERAL_CASH(BigDecimal r93_COLLATERAL_CASH) {
		R93_COLLATERAL_CASH = r93_COLLATERAL_CASH;
	}
	public BigDecimal getR93_COLLATERAL_TBILLS() {
		return R93_COLLATERAL_TBILLS;
	}
	public void setR93_COLLATERAL_TBILLS(BigDecimal r93_COLLATERAL_TBILLS) {
		R93_COLLATERAL_TBILLS = r93_COLLATERAL_TBILLS;
	}
	public BigDecimal getR93_COLLATERAL_DEBT_SEC() {
		return R93_COLLATERAL_DEBT_SEC;
	}
	public void setR93_COLLATERAL_DEBT_SEC(BigDecimal r93_COLLATERAL_DEBT_SEC) {
		R93_COLLATERAL_DEBT_SEC = r93_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR93_COLLATERAL_EQUITIES() {
		return R93_COLLATERAL_EQUITIES;
	}
	public void setR93_COLLATERAL_EQUITIES(BigDecimal r93_COLLATERAL_EQUITIES) {
		R93_COLLATERAL_EQUITIES = r93_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR93_COLLATERAL_MUTUAL_FUNDS() {
		return R93_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR93_COLLATERAL_MUTUAL_FUNDS(BigDecimal r93_COLLATERAL_MUTUAL_FUNDS) {
		R93_COLLATERAL_MUTUAL_FUNDS = r93_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR93_TOTAL_COLLATERAL_HAIRCUT() {
		return R93_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR93_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r93_TOTAL_COLLATERAL_HAIRCUT) {
		R93_TOTAL_COLLATERAL_HAIRCUT = r93_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR93_EXPOSURE_AFTER_CRM() {
		return R93_EXPOSURE_AFTER_CRM;
	}
	public void setR93_EXPOSURE_AFTER_CRM(BigDecimal r93_EXPOSURE_AFTER_CRM) {
		R93_EXPOSURE_AFTER_CRM = r93_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR93_RWA_NOT_COVERED_CRM() {
		return R93_RWA_NOT_COVERED_CRM;
	}
	public void setR93_RWA_NOT_COVERED_CRM(BigDecimal r93_RWA_NOT_COVERED_CRM) {
		R93_RWA_NOT_COVERED_CRM = r93_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR93_RWA_UNSECURED_EXPOSURE() {
		return R93_RWA_UNSECURED_EXPOSURE;
	}
	public void setR93_RWA_UNSECURED_EXPOSURE(BigDecimal r93_RWA_UNSECURED_EXPOSURE) {
		R93_RWA_UNSECURED_EXPOSURE = r93_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR93_RWA_UNSECURED() {
		return R93_RWA_UNSECURED;
	}
	public void setR93_RWA_UNSECURED(BigDecimal r93_RWA_UNSECURED) {
		R93_RWA_UNSECURED = r93_RWA_UNSECURED;
	}
	public BigDecimal getR93_TOTAL_RWA() {
		return R93_TOTAL_RWA;
	}
	public void setR93_TOTAL_RWA(BigDecimal r93_TOTAL_RWA) {
		R93_TOTAL_RWA = r93_TOTAL_RWA;
	}
	public BigDecimal getR94_EXPOSURE_BEFORE_CRM() {
		return R94_EXPOSURE_BEFORE_CRM;
	}
	public void setR94_EXPOSURE_BEFORE_CRM(BigDecimal r94_EXPOSURE_BEFORE_CRM) {
		R94_EXPOSURE_BEFORE_CRM = r94_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR94_SPEC_PROVISION_PAST_DUE() {
		return R94_SPEC_PROVISION_PAST_DUE;
	}
	public void setR94_SPEC_PROVISION_PAST_DUE(BigDecimal r94_SPEC_PROVISION_PAST_DUE) {
		R94_SPEC_PROVISION_PAST_DUE = r94_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR94_ON_BAL_SHEET_NETTING_ELIG() {
		return R94_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR94_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r94_ON_BAL_SHEET_NETTING_ELIG) {
		R94_ON_BAL_SHEET_NETTING_ELIG = r94_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR94_TOTAL_EXPOSURE_AFTER_NET() {
		return R94_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR94_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r94_TOTAL_EXPOSURE_AFTER_NET) {
		R94_TOTAL_EXPOSURE_AFTER_NET = r94_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR94_CRM_ELIG_EXPOSURE_SUBS() {
		return R94_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR94_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r94_CRM_ELIG_EXPOSURE_SUBS) {
		R94_CRM_ELIG_EXPOSURE_SUBS = r94_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR94_ELIG_GUARANTEES() {
		return R94_ELIG_GUARANTEES;
	}
	public void setR94_ELIG_GUARANTEES(BigDecimal r94_ELIG_GUARANTEES) {
		R94_ELIG_GUARANTEES = r94_ELIG_GUARANTEES;
	}
	public BigDecimal getR94_CREDIT_DERIVATIVES() {
		return R94_CREDIT_DERIVATIVES;
	}
	public void setR94_CREDIT_DERIVATIVES(BigDecimal r94_CREDIT_DERIVATIVES) {
		R94_CREDIT_DERIVATIVES = r94_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR94_CRM_COVERED_EXPOSURE() {
		return R94_CRM_COVERED_EXPOSURE;
	}
	public void setR94_CRM_COVERED_EXPOSURE(BigDecimal r94_CRM_COVERED_EXPOSURE) {
		R94_CRM_COVERED_EXPOSURE = r94_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR94_CRM_NOT_COVERED_EXPOSURE() {
		return R94_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR94_CRM_NOT_COVERED_EXPOSURE(BigDecimal r94_CRM_NOT_COVERED_EXPOSURE) {
		R94_CRM_NOT_COVERED_EXPOSURE = r94_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR94_CRM_RISK_WEIGHT() {
		return R94_CRM_RISK_WEIGHT;
	}
	public void setR94_CRM_RISK_WEIGHT(BigDecimal r94_CRM_RISK_WEIGHT) {
		R94_CRM_RISK_WEIGHT = r94_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR94_RWA_CRM_COVERED() {
		return R94_RWA_CRM_COVERED;
	}
	public void setR94_RWA_CRM_COVERED(BigDecimal r94_RWA_CRM_COVERED) {
		R94_RWA_CRM_COVERED = r94_RWA_CRM_COVERED;
	}
	public BigDecimal getR94_ORIG_COUNTERPARTY_RW() {
		return R94_ORIG_COUNTERPARTY_RW;
	}
	public void setR94_ORIG_COUNTERPARTY_RW(BigDecimal r94_ORIG_COUNTERPARTY_RW) {
		R94_ORIG_COUNTERPARTY_RW = r94_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR94_RWA_CRM_NOT_COVERED() {
		return R94_RWA_CRM_NOT_COVERED;
	}
	public void setR94_RWA_CRM_NOT_COVERED(BigDecimal r94_RWA_CRM_NOT_COVERED) {
		R94_RWA_CRM_NOT_COVERED = r94_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR94_CRM_ELIG_EXPOSURE_COMP() {
		return R94_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR94_CRM_ELIG_EXPOSURE_COMP(BigDecimal r94_CRM_ELIG_EXPOSURE_COMP) {
		R94_CRM_ELIG_EXPOSURE_COMP = r94_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR94_EXPOSURE_AFTER_VOL_ADJ() {
		return R94_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR94_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r94_EXPOSURE_AFTER_VOL_ADJ) {
		R94_EXPOSURE_AFTER_VOL_ADJ = r94_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR94_COLLATERAL_CASH() {
		return R94_COLLATERAL_CASH;
	}
	public void setR94_COLLATERAL_CASH(BigDecimal r94_COLLATERAL_CASH) {
		R94_COLLATERAL_CASH = r94_COLLATERAL_CASH;
	}
	public BigDecimal getR94_COLLATERAL_TBILLS() {
		return R94_COLLATERAL_TBILLS;
	}
	public void setR94_COLLATERAL_TBILLS(BigDecimal r94_COLLATERAL_TBILLS) {
		R94_COLLATERAL_TBILLS = r94_COLLATERAL_TBILLS;
	}
	public BigDecimal getR94_COLLATERAL_DEBT_SEC() {
		return R94_COLLATERAL_DEBT_SEC;
	}
	public void setR94_COLLATERAL_DEBT_SEC(BigDecimal r94_COLLATERAL_DEBT_SEC) {
		R94_COLLATERAL_DEBT_SEC = r94_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR94_COLLATERAL_EQUITIES() {
		return R94_COLLATERAL_EQUITIES;
	}
	public void setR94_COLLATERAL_EQUITIES(BigDecimal r94_COLLATERAL_EQUITIES) {
		R94_COLLATERAL_EQUITIES = r94_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR94_COLLATERAL_MUTUAL_FUNDS() {
		return R94_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR94_COLLATERAL_MUTUAL_FUNDS(BigDecimal r94_COLLATERAL_MUTUAL_FUNDS) {
		R94_COLLATERAL_MUTUAL_FUNDS = r94_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR94_TOTAL_COLLATERAL_HAIRCUT() {
		return R94_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR94_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r94_TOTAL_COLLATERAL_HAIRCUT) {
		R94_TOTAL_COLLATERAL_HAIRCUT = r94_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR94_EXPOSURE_AFTER_CRM() {
		return R94_EXPOSURE_AFTER_CRM;
	}
	public void setR94_EXPOSURE_AFTER_CRM(BigDecimal r94_EXPOSURE_AFTER_CRM) {
		R94_EXPOSURE_AFTER_CRM = r94_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR94_RWA_NOT_COVERED_CRM() {
		return R94_RWA_NOT_COVERED_CRM;
	}
	public void setR94_RWA_NOT_COVERED_CRM(BigDecimal r94_RWA_NOT_COVERED_CRM) {
		R94_RWA_NOT_COVERED_CRM = r94_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR94_RWA_UNSECURED_EXPOSURE() {
		return R94_RWA_UNSECURED_EXPOSURE;
	}
	public void setR94_RWA_UNSECURED_EXPOSURE(BigDecimal r94_RWA_UNSECURED_EXPOSURE) {
		R94_RWA_UNSECURED_EXPOSURE = r94_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR94_RWA_UNSECURED() {
		return R94_RWA_UNSECURED;
	}
	public void setR94_RWA_UNSECURED(BigDecimal r94_RWA_UNSECURED) {
		R94_RWA_UNSECURED = r94_RWA_UNSECURED;
	}
	public BigDecimal getR94_TOTAL_RWA() {
		return R94_TOTAL_RWA;
	}
	public void setR94_TOTAL_RWA(BigDecimal r94_TOTAL_RWA) {
		R94_TOTAL_RWA = r94_TOTAL_RWA;
	}
	public BigDecimal getR95_EXPOSURE_BEFORE_CRM() {
		return R95_EXPOSURE_BEFORE_CRM;
	}
	public void setR95_EXPOSURE_BEFORE_CRM(BigDecimal r95_EXPOSURE_BEFORE_CRM) {
		R95_EXPOSURE_BEFORE_CRM = r95_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR95_SPEC_PROVISION_PAST_DUE() {
		return R95_SPEC_PROVISION_PAST_DUE;
	}
	public void setR95_SPEC_PROVISION_PAST_DUE(BigDecimal r95_SPEC_PROVISION_PAST_DUE) {
		R95_SPEC_PROVISION_PAST_DUE = r95_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR95_ON_BAL_SHEET_NETTING_ELIG() {
		return R95_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR95_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r95_ON_BAL_SHEET_NETTING_ELIG) {
		R95_ON_BAL_SHEET_NETTING_ELIG = r95_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR95_TOTAL_EXPOSURE_AFTER_NET() {
		return R95_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR95_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r95_TOTAL_EXPOSURE_AFTER_NET) {
		R95_TOTAL_EXPOSURE_AFTER_NET = r95_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR95_CRM_ELIG_EXPOSURE_SUBS() {
		return R95_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR95_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r95_CRM_ELIG_EXPOSURE_SUBS) {
		R95_CRM_ELIG_EXPOSURE_SUBS = r95_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR95_ELIG_GUARANTEES() {
		return R95_ELIG_GUARANTEES;
	}
	public void setR95_ELIG_GUARANTEES(BigDecimal r95_ELIG_GUARANTEES) {
		R95_ELIG_GUARANTEES = r95_ELIG_GUARANTEES;
	}
	public BigDecimal getR95_CREDIT_DERIVATIVES() {
		return R95_CREDIT_DERIVATIVES;
	}
	public void setR95_CREDIT_DERIVATIVES(BigDecimal r95_CREDIT_DERIVATIVES) {
		R95_CREDIT_DERIVATIVES = r95_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR95_CRM_COVERED_EXPOSURE() {
		return R95_CRM_COVERED_EXPOSURE;
	}
	public void setR95_CRM_COVERED_EXPOSURE(BigDecimal r95_CRM_COVERED_EXPOSURE) {
		R95_CRM_COVERED_EXPOSURE = r95_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR95_CRM_NOT_COVERED_EXPOSURE() {
		return R95_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR95_CRM_NOT_COVERED_EXPOSURE(BigDecimal r95_CRM_NOT_COVERED_EXPOSURE) {
		R95_CRM_NOT_COVERED_EXPOSURE = r95_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR95_CRM_RISK_WEIGHT() {
		return R95_CRM_RISK_WEIGHT;
	}
	public void setR95_CRM_RISK_WEIGHT(BigDecimal r95_CRM_RISK_WEIGHT) {
		R95_CRM_RISK_WEIGHT = r95_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR95_RWA_CRM_COVERED() {
		return R95_RWA_CRM_COVERED;
	}
	public void setR95_RWA_CRM_COVERED(BigDecimal r95_RWA_CRM_COVERED) {
		R95_RWA_CRM_COVERED = r95_RWA_CRM_COVERED;
	}
	public BigDecimal getR95_ORIG_COUNTERPARTY_RW() {
		return R95_ORIG_COUNTERPARTY_RW;
	}
	public void setR95_ORIG_COUNTERPARTY_RW(BigDecimal r95_ORIG_COUNTERPARTY_RW) {
		R95_ORIG_COUNTERPARTY_RW = r95_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR95_RWA_CRM_NOT_COVERED() {
		return R95_RWA_CRM_NOT_COVERED;
	}
	public void setR95_RWA_CRM_NOT_COVERED(BigDecimal r95_RWA_CRM_NOT_COVERED) {
		R95_RWA_CRM_NOT_COVERED = r95_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR95_CRM_ELIG_EXPOSURE_COMP() {
		return R95_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR95_CRM_ELIG_EXPOSURE_COMP(BigDecimal r95_CRM_ELIG_EXPOSURE_COMP) {
		R95_CRM_ELIG_EXPOSURE_COMP = r95_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR95_EXPOSURE_AFTER_VOL_ADJ() {
		return R95_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR95_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r95_EXPOSURE_AFTER_VOL_ADJ) {
		R95_EXPOSURE_AFTER_VOL_ADJ = r95_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR95_COLLATERAL_CASH() {
		return R95_COLLATERAL_CASH;
	}
	public void setR95_COLLATERAL_CASH(BigDecimal r95_COLLATERAL_CASH) {
		R95_COLLATERAL_CASH = r95_COLLATERAL_CASH;
	}
	public BigDecimal getR95_COLLATERAL_TBILLS() {
		return R95_COLLATERAL_TBILLS;
	}
	public void setR95_COLLATERAL_TBILLS(BigDecimal r95_COLLATERAL_TBILLS) {
		R95_COLLATERAL_TBILLS = r95_COLLATERAL_TBILLS;
	}
	public BigDecimal getR95_COLLATERAL_DEBT_SEC() {
		return R95_COLLATERAL_DEBT_SEC;
	}
	public void setR95_COLLATERAL_DEBT_SEC(BigDecimal r95_COLLATERAL_DEBT_SEC) {
		R95_COLLATERAL_DEBT_SEC = r95_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR95_COLLATERAL_EQUITIES() {
		return R95_COLLATERAL_EQUITIES;
	}
	public void setR95_COLLATERAL_EQUITIES(BigDecimal r95_COLLATERAL_EQUITIES) {
		R95_COLLATERAL_EQUITIES = r95_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR95_COLLATERAL_MUTUAL_FUNDS() {
		return R95_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR95_COLLATERAL_MUTUAL_FUNDS(BigDecimal r95_COLLATERAL_MUTUAL_FUNDS) {
		R95_COLLATERAL_MUTUAL_FUNDS = r95_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR95_TOTAL_COLLATERAL_HAIRCUT() {
		return R95_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR95_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r95_TOTAL_COLLATERAL_HAIRCUT) {
		R95_TOTAL_COLLATERAL_HAIRCUT = r95_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR95_EXPOSURE_AFTER_CRM() {
		return R95_EXPOSURE_AFTER_CRM;
	}
	public void setR95_EXPOSURE_AFTER_CRM(BigDecimal r95_EXPOSURE_AFTER_CRM) {
		R95_EXPOSURE_AFTER_CRM = r95_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR95_RWA_NOT_COVERED_CRM() {
		return R95_RWA_NOT_COVERED_CRM;
	}
	public void setR95_RWA_NOT_COVERED_CRM(BigDecimal r95_RWA_NOT_COVERED_CRM) {
		R95_RWA_NOT_COVERED_CRM = r95_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR95_RWA_UNSECURED_EXPOSURE() {
		return R95_RWA_UNSECURED_EXPOSURE;
	}
	public void setR95_RWA_UNSECURED_EXPOSURE(BigDecimal r95_RWA_UNSECURED_EXPOSURE) {
		R95_RWA_UNSECURED_EXPOSURE = r95_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR95_RWA_UNSECURED() {
		return R95_RWA_UNSECURED;
	}
	public void setR95_RWA_UNSECURED(BigDecimal r95_RWA_UNSECURED) {
		R95_RWA_UNSECURED = r95_RWA_UNSECURED;
	}
	public BigDecimal getR95_TOTAL_RWA() {
		return R95_TOTAL_RWA;
	}
	public void setR95_TOTAL_RWA(BigDecimal r95_TOTAL_RWA) {
		R95_TOTAL_RWA = r95_TOTAL_RWA;
	}
	public BigDecimal getR96_EXPOSURE_BEFORE_CRM() {
		return R96_EXPOSURE_BEFORE_CRM;
	}
	public void setR96_EXPOSURE_BEFORE_CRM(BigDecimal r96_EXPOSURE_BEFORE_CRM) {
		R96_EXPOSURE_BEFORE_CRM = r96_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR96_SPEC_PROVISION_PAST_DUE() {
		return R96_SPEC_PROVISION_PAST_DUE;
	}
	public void setR96_SPEC_PROVISION_PAST_DUE(BigDecimal r96_SPEC_PROVISION_PAST_DUE) {
		R96_SPEC_PROVISION_PAST_DUE = r96_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR96_ON_BAL_SHEET_NETTING_ELIG() {
		return R96_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR96_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r96_ON_BAL_SHEET_NETTING_ELIG) {
		R96_ON_BAL_SHEET_NETTING_ELIG = r96_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR96_TOTAL_EXPOSURE_AFTER_NET() {
		return R96_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR96_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r96_TOTAL_EXPOSURE_AFTER_NET) {
		R96_TOTAL_EXPOSURE_AFTER_NET = r96_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR96_CRM_ELIG_EXPOSURE_SUBS() {
		return R96_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR96_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r96_CRM_ELIG_EXPOSURE_SUBS) {
		R96_CRM_ELIG_EXPOSURE_SUBS = r96_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR96_ELIG_GUARANTEES() {
		return R96_ELIG_GUARANTEES;
	}
	public void setR96_ELIG_GUARANTEES(BigDecimal r96_ELIG_GUARANTEES) {
		R96_ELIG_GUARANTEES = r96_ELIG_GUARANTEES;
	}
	public BigDecimal getR96_CREDIT_DERIVATIVES() {
		return R96_CREDIT_DERIVATIVES;
	}
	public void setR96_CREDIT_DERIVATIVES(BigDecimal r96_CREDIT_DERIVATIVES) {
		R96_CREDIT_DERIVATIVES = r96_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR96_CRM_COVERED_EXPOSURE() {
		return R96_CRM_COVERED_EXPOSURE;
	}
	public void setR96_CRM_COVERED_EXPOSURE(BigDecimal r96_CRM_COVERED_EXPOSURE) {
		R96_CRM_COVERED_EXPOSURE = r96_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR96_CRM_NOT_COVERED_EXPOSURE() {
		return R96_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR96_CRM_NOT_COVERED_EXPOSURE(BigDecimal r96_CRM_NOT_COVERED_EXPOSURE) {
		R96_CRM_NOT_COVERED_EXPOSURE = r96_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR96_CRM_RISK_WEIGHT() {
		return R96_CRM_RISK_WEIGHT;
	}
	public void setR96_CRM_RISK_WEIGHT(BigDecimal r96_CRM_RISK_WEIGHT) {
		R96_CRM_RISK_WEIGHT = r96_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR96_RWA_CRM_COVERED() {
		return R96_RWA_CRM_COVERED;
	}
	public void setR96_RWA_CRM_COVERED(BigDecimal r96_RWA_CRM_COVERED) {
		R96_RWA_CRM_COVERED = r96_RWA_CRM_COVERED;
	}
	public BigDecimal getR96_ORIG_COUNTERPARTY_RW() {
		return R96_ORIG_COUNTERPARTY_RW;
	}
	public void setR96_ORIG_COUNTERPARTY_RW(BigDecimal r96_ORIG_COUNTERPARTY_RW) {
		R96_ORIG_COUNTERPARTY_RW = r96_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR96_RWA_CRM_NOT_COVERED() {
		return R96_RWA_CRM_NOT_COVERED;
	}
	public void setR96_RWA_CRM_NOT_COVERED(BigDecimal r96_RWA_CRM_NOT_COVERED) {
		R96_RWA_CRM_NOT_COVERED = r96_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR96_CRM_ELIG_EXPOSURE_COMP() {
		return R96_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR96_CRM_ELIG_EXPOSURE_COMP(BigDecimal r96_CRM_ELIG_EXPOSURE_COMP) {
		R96_CRM_ELIG_EXPOSURE_COMP = r96_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR96_EXPOSURE_AFTER_VOL_ADJ() {
		return R96_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR96_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r96_EXPOSURE_AFTER_VOL_ADJ) {
		R96_EXPOSURE_AFTER_VOL_ADJ = r96_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR96_COLLATERAL_CASH() {
		return R96_COLLATERAL_CASH;
	}
	public void setR96_COLLATERAL_CASH(BigDecimal r96_COLLATERAL_CASH) {
		R96_COLLATERAL_CASH = r96_COLLATERAL_CASH;
	}
	public BigDecimal getR96_COLLATERAL_TBILLS() {
		return R96_COLLATERAL_TBILLS;
	}
	public void setR96_COLLATERAL_TBILLS(BigDecimal r96_COLLATERAL_TBILLS) {
		R96_COLLATERAL_TBILLS = r96_COLLATERAL_TBILLS;
	}
	public BigDecimal getR96_COLLATERAL_DEBT_SEC() {
		return R96_COLLATERAL_DEBT_SEC;
	}
	public void setR96_COLLATERAL_DEBT_SEC(BigDecimal r96_COLLATERAL_DEBT_SEC) {
		R96_COLLATERAL_DEBT_SEC = r96_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR96_COLLATERAL_EQUITIES() {
		return R96_COLLATERAL_EQUITIES;
	}
	public void setR96_COLLATERAL_EQUITIES(BigDecimal r96_COLLATERAL_EQUITIES) {
		R96_COLLATERAL_EQUITIES = r96_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR96_COLLATERAL_MUTUAL_FUNDS() {
		return R96_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR96_COLLATERAL_MUTUAL_FUNDS(BigDecimal r96_COLLATERAL_MUTUAL_FUNDS) {
		R96_COLLATERAL_MUTUAL_FUNDS = r96_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR96_TOTAL_COLLATERAL_HAIRCUT() {
		return R96_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR96_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r96_TOTAL_COLLATERAL_HAIRCUT) {
		R96_TOTAL_COLLATERAL_HAIRCUT = r96_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR96_EXPOSURE_AFTER_CRM() {
		return R96_EXPOSURE_AFTER_CRM;
	}
	public void setR96_EXPOSURE_AFTER_CRM(BigDecimal r96_EXPOSURE_AFTER_CRM) {
		R96_EXPOSURE_AFTER_CRM = r96_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR96_RWA_NOT_COVERED_CRM() {
		return R96_RWA_NOT_COVERED_CRM;
	}
	public void setR96_RWA_NOT_COVERED_CRM(BigDecimal r96_RWA_NOT_COVERED_CRM) {
		R96_RWA_NOT_COVERED_CRM = r96_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR96_RWA_UNSECURED_EXPOSURE() {
		return R96_RWA_UNSECURED_EXPOSURE;
	}
	public void setR96_RWA_UNSECURED_EXPOSURE(BigDecimal r96_RWA_UNSECURED_EXPOSURE) {
		R96_RWA_UNSECURED_EXPOSURE = r96_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR96_RWA_UNSECURED() {
		return R96_RWA_UNSECURED;
	}
	public void setR96_RWA_UNSECURED(BigDecimal r96_RWA_UNSECURED) {
		R96_RWA_UNSECURED = r96_RWA_UNSECURED;
	}
	public BigDecimal getR96_TOTAL_RWA() {
		return R96_TOTAL_RWA;
	}
	public void setR96_TOTAL_RWA(BigDecimal r96_TOTAL_RWA) {
		R96_TOTAL_RWA = r96_TOTAL_RWA;
	}
	public BigDecimal getR97_EXPOSURE_BEFORE_CRM() {
		return R97_EXPOSURE_BEFORE_CRM;
	}
	public void setR97_EXPOSURE_BEFORE_CRM(BigDecimal r97_EXPOSURE_BEFORE_CRM) {
		R97_EXPOSURE_BEFORE_CRM = r97_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR97_SPEC_PROVISION_PAST_DUE() {
		return R97_SPEC_PROVISION_PAST_DUE;
	}
	public void setR97_SPEC_PROVISION_PAST_DUE(BigDecimal r97_SPEC_PROVISION_PAST_DUE) {
		R97_SPEC_PROVISION_PAST_DUE = r97_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR97_ON_BAL_SHEET_NETTING_ELIG() {
		return R97_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR97_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r97_ON_BAL_SHEET_NETTING_ELIG) {
		R97_ON_BAL_SHEET_NETTING_ELIG = r97_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR97_TOTAL_EXPOSURE_AFTER_NET() {
		return R97_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR97_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r97_TOTAL_EXPOSURE_AFTER_NET) {
		R97_TOTAL_EXPOSURE_AFTER_NET = r97_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR97_CRM_ELIG_EXPOSURE_SUBS() {
		return R97_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR97_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r97_CRM_ELIG_EXPOSURE_SUBS) {
		R97_CRM_ELIG_EXPOSURE_SUBS = r97_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR97_ELIG_GUARANTEES() {
		return R97_ELIG_GUARANTEES;
	}
	public void setR97_ELIG_GUARANTEES(BigDecimal r97_ELIG_GUARANTEES) {
		R97_ELIG_GUARANTEES = r97_ELIG_GUARANTEES;
	}
	public BigDecimal getR97_CREDIT_DERIVATIVES() {
		return R97_CREDIT_DERIVATIVES;
	}
	public void setR97_CREDIT_DERIVATIVES(BigDecimal r97_CREDIT_DERIVATIVES) {
		R97_CREDIT_DERIVATIVES = r97_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR97_CRM_COVERED_EXPOSURE() {
		return R97_CRM_COVERED_EXPOSURE;
	}
	public void setR97_CRM_COVERED_EXPOSURE(BigDecimal r97_CRM_COVERED_EXPOSURE) {
		R97_CRM_COVERED_EXPOSURE = r97_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR97_CRM_NOT_COVERED_EXPOSURE() {
		return R97_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR97_CRM_NOT_COVERED_EXPOSURE(BigDecimal r97_CRM_NOT_COVERED_EXPOSURE) {
		R97_CRM_NOT_COVERED_EXPOSURE = r97_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR97_CRM_RISK_WEIGHT() {
		return R97_CRM_RISK_WEIGHT;
	}
	public void setR97_CRM_RISK_WEIGHT(BigDecimal r97_CRM_RISK_WEIGHT) {
		R97_CRM_RISK_WEIGHT = r97_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR97_RWA_CRM_COVERED() {
		return R97_RWA_CRM_COVERED;
	}
	public void setR97_RWA_CRM_COVERED(BigDecimal r97_RWA_CRM_COVERED) {
		R97_RWA_CRM_COVERED = r97_RWA_CRM_COVERED;
	}
	public BigDecimal getR97_ORIG_COUNTERPARTY_RW() {
		return R97_ORIG_COUNTERPARTY_RW;
	}
	public void setR97_ORIG_COUNTERPARTY_RW(BigDecimal r97_ORIG_COUNTERPARTY_RW) {
		R97_ORIG_COUNTERPARTY_RW = r97_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR97_RWA_CRM_NOT_COVERED() {
		return R97_RWA_CRM_NOT_COVERED;
	}
	public void setR97_RWA_CRM_NOT_COVERED(BigDecimal r97_RWA_CRM_NOT_COVERED) {
		R97_RWA_CRM_NOT_COVERED = r97_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR97_CRM_ELIG_EXPOSURE_COMP() {
		return R97_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR97_CRM_ELIG_EXPOSURE_COMP(BigDecimal r97_CRM_ELIG_EXPOSURE_COMP) {
		R97_CRM_ELIG_EXPOSURE_COMP = r97_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR97_EXPOSURE_AFTER_VOL_ADJ() {
		return R97_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR97_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r97_EXPOSURE_AFTER_VOL_ADJ) {
		R97_EXPOSURE_AFTER_VOL_ADJ = r97_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR97_COLLATERAL_CASH() {
		return R97_COLLATERAL_CASH;
	}
	public void setR97_COLLATERAL_CASH(BigDecimal r97_COLLATERAL_CASH) {
		R97_COLLATERAL_CASH = r97_COLLATERAL_CASH;
	}
	public BigDecimal getR97_COLLATERAL_TBILLS() {
		return R97_COLLATERAL_TBILLS;
	}
	public void setR97_COLLATERAL_TBILLS(BigDecimal r97_COLLATERAL_TBILLS) {
		R97_COLLATERAL_TBILLS = r97_COLLATERAL_TBILLS;
	}
	public BigDecimal getR97_COLLATERAL_DEBT_SEC() {
		return R97_COLLATERAL_DEBT_SEC;
	}
	public void setR97_COLLATERAL_DEBT_SEC(BigDecimal r97_COLLATERAL_DEBT_SEC) {
		R97_COLLATERAL_DEBT_SEC = r97_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR97_COLLATERAL_EQUITIES() {
		return R97_COLLATERAL_EQUITIES;
	}
	public void setR97_COLLATERAL_EQUITIES(BigDecimal r97_COLLATERAL_EQUITIES) {
		R97_COLLATERAL_EQUITIES = r97_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR97_COLLATERAL_MUTUAL_FUNDS() {
		return R97_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR97_COLLATERAL_MUTUAL_FUNDS(BigDecimal r97_COLLATERAL_MUTUAL_FUNDS) {
		R97_COLLATERAL_MUTUAL_FUNDS = r97_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR97_TOTAL_COLLATERAL_HAIRCUT() {
		return R97_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR97_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r97_TOTAL_COLLATERAL_HAIRCUT) {
		R97_TOTAL_COLLATERAL_HAIRCUT = r97_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR97_EXPOSURE_AFTER_CRM() {
		return R97_EXPOSURE_AFTER_CRM;
	}
	public void setR97_EXPOSURE_AFTER_CRM(BigDecimal r97_EXPOSURE_AFTER_CRM) {
		R97_EXPOSURE_AFTER_CRM = r97_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR97_RWA_NOT_COVERED_CRM() {
		return R97_RWA_NOT_COVERED_CRM;
	}
	public void setR97_RWA_NOT_COVERED_CRM(BigDecimal r97_RWA_NOT_COVERED_CRM) {
		R97_RWA_NOT_COVERED_CRM = r97_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR97_RWA_UNSECURED_EXPOSURE() {
		return R97_RWA_UNSECURED_EXPOSURE;
	}
	public void setR97_RWA_UNSECURED_EXPOSURE(BigDecimal r97_RWA_UNSECURED_EXPOSURE) {
		R97_RWA_UNSECURED_EXPOSURE = r97_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR97_RWA_UNSECURED() {
		return R97_RWA_UNSECURED;
	}
	public void setR97_RWA_UNSECURED(BigDecimal r97_RWA_UNSECURED) {
		R97_RWA_UNSECURED = r97_RWA_UNSECURED;
	}
	public BigDecimal getR97_TOTAL_RWA() {
		return R97_TOTAL_RWA;
	}
	public void setR97_TOTAL_RWA(BigDecimal r97_TOTAL_RWA) {
		R97_TOTAL_RWA = r97_TOTAL_RWA;
	}
	public BigDecimal getR98_EXPOSURE_BEFORE_CRM() {
		return R98_EXPOSURE_BEFORE_CRM;
	}
	public void setR98_EXPOSURE_BEFORE_CRM(BigDecimal r98_EXPOSURE_BEFORE_CRM) {
		R98_EXPOSURE_BEFORE_CRM = r98_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR98_SPEC_PROVISION_PAST_DUE() {
		return R98_SPEC_PROVISION_PAST_DUE;
	}
	public void setR98_SPEC_PROVISION_PAST_DUE(BigDecimal r98_SPEC_PROVISION_PAST_DUE) {
		R98_SPEC_PROVISION_PAST_DUE = r98_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR98_ON_BAL_SHEET_NETTING_ELIG() {
		return R98_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR98_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r98_ON_BAL_SHEET_NETTING_ELIG) {
		R98_ON_BAL_SHEET_NETTING_ELIG = r98_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR98_TOTAL_EXPOSURE_AFTER_NET() {
		return R98_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR98_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r98_TOTAL_EXPOSURE_AFTER_NET) {
		R98_TOTAL_EXPOSURE_AFTER_NET = r98_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR98_CRM_ELIG_EXPOSURE_SUBS() {
		return R98_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR98_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r98_CRM_ELIG_EXPOSURE_SUBS) {
		R98_CRM_ELIG_EXPOSURE_SUBS = r98_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR98_ELIG_GUARANTEES() {
		return R98_ELIG_GUARANTEES;
	}
	public void setR98_ELIG_GUARANTEES(BigDecimal r98_ELIG_GUARANTEES) {
		R98_ELIG_GUARANTEES = r98_ELIG_GUARANTEES;
	}
	public BigDecimal getR98_CREDIT_DERIVATIVES() {
		return R98_CREDIT_DERIVATIVES;
	}
	public void setR98_CREDIT_DERIVATIVES(BigDecimal r98_CREDIT_DERIVATIVES) {
		R98_CREDIT_DERIVATIVES = r98_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR98_CRM_COVERED_EXPOSURE() {
		return R98_CRM_COVERED_EXPOSURE;
	}
	public void setR98_CRM_COVERED_EXPOSURE(BigDecimal r98_CRM_COVERED_EXPOSURE) {
		R98_CRM_COVERED_EXPOSURE = r98_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR98_CRM_NOT_COVERED_EXPOSURE() {
		return R98_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR98_CRM_NOT_COVERED_EXPOSURE(BigDecimal r98_CRM_NOT_COVERED_EXPOSURE) {
		R98_CRM_NOT_COVERED_EXPOSURE = r98_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR98_CRM_RISK_WEIGHT() {
		return R98_CRM_RISK_WEIGHT;
	}
	public void setR98_CRM_RISK_WEIGHT(BigDecimal r98_CRM_RISK_WEIGHT) {
		R98_CRM_RISK_WEIGHT = r98_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR98_RWA_CRM_COVERED() {
		return R98_RWA_CRM_COVERED;
	}
	public void setR98_RWA_CRM_COVERED(BigDecimal r98_RWA_CRM_COVERED) {
		R98_RWA_CRM_COVERED = r98_RWA_CRM_COVERED;
	}
	public BigDecimal getR98_ORIG_COUNTERPARTY_RW() {
		return R98_ORIG_COUNTERPARTY_RW;
	}
	public void setR98_ORIG_COUNTERPARTY_RW(BigDecimal r98_ORIG_COUNTERPARTY_RW) {
		R98_ORIG_COUNTERPARTY_RW = r98_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR98_RWA_CRM_NOT_COVERED() {
		return R98_RWA_CRM_NOT_COVERED;
	}
	public void setR98_RWA_CRM_NOT_COVERED(BigDecimal r98_RWA_CRM_NOT_COVERED) {
		R98_RWA_CRM_NOT_COVERED = r98_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR98_CRM_ELIG_EXPOSURE_COMP() {
		return R98_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR98_CRM_ELIG_EXPOSURE_COMP(BigDecimal r98_CRM_ELIG_EXPOSURE_COMP) {
		R98_CRM_ELIG_EXPOSURE_COMP = r98_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR98_EXPOSURE_AFTER_VOL_ADJ() {
		return R98_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR98_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r98_EXPOSURE_AFTER_VOL_ADJ) {
		R98_EXPOSURE_AFTER_VOL_ADJ = r98_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR98_COLLATERAL_CASH() {
		return R98_COLLATERAL_CASH;
	}
	public void setR98_COLLATERAL_CASH(BigDecimal r98_COLLATERAL_CASH) {
		R98_COLLATERAL_CASH = r98_COLLATERAL_CASH;
	}
	public BigDecimal getR98_COLLATERAL_TBILLS() {
		return R98_COLLATERAL_TBILLS;
	}
	public void setR98_COLLATERAL_TBILLS(BigDecimal r98_COLLATERAL_TBILLS) {
		R98_COLLATERAL_TBILLS = r98_COLLATERAL_TBILLS;
	}
	public BigDecimal getR98_COLLATERAL_DEBT_SEC() {
		return R98_COLLATERAL_DEBT_SEC;
	}
	public void setR98_COLLATERAL_DEBT_SEC(BigDecimal r98_COLLATERAL_DEBT_SEC) {
		R98_COLLATERAL_DEBT_SEC = r98_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR98_COLLATERAL_EQUITIES() {
		return R98_COLLATERAL_EQUITIES;
	}
	public void setR98_COLLATERAL_EQUITIES(BigDecimal r98_COLLATERAL_EQUITIES) {
		R98_COLLATERAL_EQUITIES = r98_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR98_COLLATERAL_MUTUAL_FUNDS() {
		return R98_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR98_COLLATERAL_MUTUAL_FUNDS(BigDecimal r98_COLLATERAL_MUTUAL_FUNDS) {
		R98_COLLATERAL_MUTUAL_FUNDS = r98_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR98_TOTAL_COLLATERAL_HAIRCUT() {
		return R98_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR98_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r98_TOTAL_COLLATERAL_HAIRCUT) {
		R98_TOTAL_COLLATERAL_HAIRCUT = r98_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR98_EXPOSURE_AFTER_CRM() {
		return R98_EXPOSURE_AFTER_CRM;
	}
	public void setR98_EXPOSURE_AFTER_CRM(BigDecimal r98_EXPOSURE_AFTER_CRM) {
		R98_EXPOSURE_AFTER_CRM = r98_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR98_RWA_NOT_COVERED_CRM() {
		return R98_RWA_NOT_COVERED_CRM;
	}
	public void setR98_RWA_NOT_COVERED_CRM(BigDecimal r98_RWA_NOT_COVERED_CRM) {
		R98_RWA_NOT_COVERED_CRM = r98_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR98_RWA_UNSECURED_EXPOSURE() {
		return R98_RWA_UNSECURED_EXPOSURE;
	}
	public void setR98_RWA_UNSECURED_EXPOSURE(BigDecimal r98_RWA_UNSECURED_EXPOSURE) {
		R98_RWA_UNSECURED_EXPOSURE = r98_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR98_RWA_UNSECURED() {
		return R98_RWA_UNSECURED;
	}
	public void setR98_RWA_UNSECURED(BigDecimal r98_RWA_UNSECURED) {
		R98_RWA_UNSECURED = r98_RWA_UNSECURED;
	}
	public BigDecimal getR98_TOTAL_RWA() {
		return R98_TOTAL_RWA;
	}
	public void setR98_TOTAL_RWA(BigDecimal r98_TOTAL_RWA) {
		R98_TOTAL_RWA = r98_TOTAL_RWA;
	}
	public BigDecimal getR99_EXPOSURE_BEFORE_CRM() {
		return R99_EXPOSURE_BEFORE_CRM;
	}
	public void setR99_EXPOSURE_BEFORE_CRM(BigDecimal r99_EXPOSURE_BEFORE_CRM) {
		R99_EXPOSURE_BEFORE_CRM = r99_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR99_SPEC_PROVISION_PAST_DUE() {
		return R99_SPEC_PROVISION_PAST_DUE;
	}
	public void setR99_SPEC_PROVISION_PAST_DUE(BigDecimal r99_SPEC_PROVISION_PAST_DUE) {
		R99_SPEC_PROVISION_PAST_DUE = r99_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR99_ON_BAL_SHEET_NETTING_ELIG() {
		return R99_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR99_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r99_ON_BAL_SHEET_NETTING_ELIG) {
		R99_ON_BAL_SHEET_NETTING_ELIG = r99_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR99_TOTAL_EXPOSURE_AFTER_NET() {
		return R99_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR99_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r99_TOTAL_EXPOSURE_AFTER_NET) {
		R99_TOTAL_EXPOSURE_AFTER_NET = r99_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR99_CRM_ELIG_EXPOSURE_SUBS() {
		return R99_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR99_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r99_CRM_ELIG_EXPOSURE_SUBS) {
		R99_CRM_ELIG_EXPOSURE_SUBS = r99_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR99_ELIG_GUARANTEES() {
		return R99_ELIG_GUARANTEES;
	}
	public void setR99_ELIG_GUARANTEES(BigDecimal r99_ELIG_GUARANTEES) {
		R99_ELIG_GUARANTEES = r99_ELIG_GUARANTEES;
	}
	public BigDecimal getR99_CREDIT_DERIVATIVES() {
		return R99_CREDIT_DERIVATIVES;
	}
	public void setR99_CREDIT_DERIVATIVES(BigDecimal r99_CREDIT_DERIVATIVES) {
		R99_CREDIT_DERIVATIVES = r99_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR99_CRM_COVERED_EXPOSURE() {
		return R99_CRM_COVERED_EXPOSURE;
	}
	public void setR99_CRM_COVERED_EXPOSURE(BigDecimal r99_CRM_COVERED_EXPOSURE) {
		R99_CRM_COVERED_EXPOSURE = r99_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR99_CRM_NOT_COVERED_EXPOSURE() {
		return R99_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR99_CRM_NOT_COVERED_EXPOSURE(BigDecimal r99_CRM_NOT_COVERED_EXPOSURE) {
		R99_CRM_NOT_COVERED_EXPOSURE = r99_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR99_CRM_RISK_WEIGHT() {
		return R99_CRM_RISK_WEIGHT;
	}
	public void setR99_CRM_RISK_WEIGHT(BigDecimal r99_CRM_RISK_WEIGHT) {
		R99_CRM_RISK_WEIGHT = r99_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR99_RWA_CRM_COVERED() {
		return R99_RWA_CRM_COVERED;
	}
	public void setR99_RWA_CRM_COVERED(BigDecimal r99_RWA_CRM_COVERED) {
		R99_RWA_CRM_COVERED = r99_RWA_CRM_COVERED;
	}
	public BigDecimal getR99_ORIG_COUNTERPARTY_RW() {
		return R99_ORIG_COUNTERPARTY_RW;
	}
	public void setR99_ORIG_COUNTERPARTY_RW(BigDecimal r99_ORIG_COUNTERPARTY_RW) {
		R99_ORIG_COUNTERPARTY_RW = r99_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR99_RWA_CRM_NOT_COVERED() {
		return R99_RWA_CRM_NOT_COVERED;
	}
	public void setR99_RWA_CRM_NOT_COVERED(BigDecimal r99_RWA_CRM_NOT_COVERED) {
		R99_RWA_CRM_NOT_COVERED = r99_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR99_CRM_ELIG_EXPOSURE_COMP() {
		return R99_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR99_CRM_ELIG_EXPOSURE_COMP(BigDecimal r99_CRM_ELIG_EXPOSURE_COMP) {
		R99_CRM_ELIG_EXPOSURE_COMP = r99_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR99_EXPOSURE_AFTER_VOL_ADJ() {
		return R99_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR99_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r99_EXPOSURE_AFTER_VOL_ADJ) {
		R99_EXPOSURE_AFTER_VOL_ADJ = r99_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR99_COLLATERAL_CASH() {
		return R99_COLLATERAL_CASH;
	}
	public void setR99_COLLATERAL_CASH(BigDecimal r99_COLLATERAL_CASH) {
		R99_COLLATERAL_CASH = r99_COLLATERAL_CASH;
	}
	public BigDecimal getR99_COLLATERAL_TBILLS() {
		return R99_COLLATERAL_TBILLS;
	}
	public void setR99_COLLATERAL_TBILLS(BigDecimal r99_COLLATERAL_TBILLS) {
		R99_COLLATERAL_TBILLS = r99_COLLATERAL_TBILLS;
	}
	public BigDecimal getR99_COLLATERAL_DEBT_SEC() {
		return R99_COLLATERAL_DEBT_SEC;
	}
	public void setR99_COLLATERAL_DEBT_SEC(BigDecimal r99_COLLATERAL_DEBT_SEC) {
		R99_COLLATERAL_DEBT_SEC = r99_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR99_COLLATERAL_EQUITIES() {
		return R99_COLLATERAL_EQUITIES;
	}
	public void setR99_COLLATERAL_EQUITIES(BigDecimal r99_COLLATERAL_EQUITIES) {
		R99_COLLATERAL_EQUITIES = r99_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR99_COLLATERAL_MUTUAL_FUNDS() {
		return R99_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR99_COLLATERAL_MUTUAL_FUNDS(BigDecimal r99_COLLATERAL_MUTUAL_FUNDS) {
		R99_COLLATERAL_MUTUAL_FUNDS = r99_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR99_TOTAL_COLLATERAL_HAIRCUT() {
		return R99_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR99_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r99_TOTAL_COLLATERAL_HAIRCUT) {
		R99_TOTAL_COLLATERAL_HAIRCUT = r99_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR99_EXPOSURE_AFTER_CRM() {
		return R99_EXPOSURE_AFTER_CRM;
	}
	public void setR99_EXPOSURE_AFTER_CRM(BigDecimal r99_EXPOSURE_AFTER_CRM) {
		R99_EXPOSURE_AFTER_CRM = r99_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR99_RWA_NOT_COVERED_CRM() {
		return R99_RWA_NOT_COVERED_CRM;
	}
	public void setR99_RWA_NOT_COVERED_CRM(BigDecimal r99_RWA_NOT_COVERED_CRM) {
		R99_RWA_NOT_COVERED_CRM = r99_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR99_RWA_UNSECURED_EXPOSURE() {
		return R99_RWA_UNSECURED_EXPOSURE;
	}
	public void setR99_RWA_UNSECURED_EXPOSURE(BigDecimal r99_RWA_UNSECURED_EXPOSURE) {
		R99_RWA_UNSECURED_EXPOSURE = r99_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR99_RWA_UNSECURED() {
		return R99_RWA_UNSECURED;
	}
	public void setR99_RWA_UNSECURED(BigDecimal r99_RWA_UNSECURED) {
		R99_RWA_UNSECURED = r99_RWA_UNSECURED;
	}
	public BigDecimal getR99_TOTAL_RWA() {
		return R99_TOTAL_RWA;
	}
	public void setR99_TOTAL_RWA(BigDecimal r99_TOTAL_RWA) {
		R99_TOTAL_RWA = r99_TOTAL_RWA;
	}
	public BigDecimal getR100_EXPOSURE_BEFORE_CRM() {
		return R100_EXPOSURE_BEFORE_CRM;
	}
	public void setR100_EXPOSURE_BEFORE_CRM(BigDecimal r100_EXPOSURE_BEFORE_CRM) {
		R100_EXPOSURE_BEFORE_CRM = r100_EXPOSURE_BEFORE_CRM;
	}
	public BigDecimal getR100_SPEC_PROVISION_PAST_DUE() {
		return R100_SPEC_PROVISION_PAST_DUE;
	}
	public void setR100_SPEC_PROVISION_PAST_DUE(BigDecimal r100_SPEC_PROVISION_PAST_DUE) {
		R100_SPEC_PROVISION_PAST_DUE = r100_SPEC_PROVISION_PAST_DUE;
	}
	public BigDecimal getR100_ON_BAL_SHEET_NETTING_ELIG() {
		return R100_ON_BAL_SHEET_NETTING_ELIG;
	}
	public void setR100_ON_BAL_SHEET_NETTING_ELIG(BigDecimal r100_ON_BAL_SHEET_NETTING_ELIG) {
		R100_ON_BAL_SHEET_NETTING_ELIG = r100_ON_BAL_SHEET_NETTING_ELIG;
	}
	public BigDecimal getR100_TOTAL_EXPOSURE_AFTER_NET() {
		return R100_TOTAL_EXPOSURE_AFTER_NET;
	}
	public void setR100_TOTAL_EXPOSURE_AFTER_NET(BigDecimal r100_TOTAL_EXPOSURE_AFTER_NET) {
		R100_TOTAL_EXPOSURE_AFTER_NET = r100_TOTAL_EXPOSURE_AFTER_NET;
	}
	public BigDecimal getR100_CRM_ELIG_EXPOSURE_SUBS() {
		return R100_CRM_ELIG_EXPOSURE_SUBS;
	}
	public void setR100_CRM_ELIG_EXPOSURE_SUBS(BigDecimal r100_CRM_ELIG_EXPOSURE_SUBS) {
		R100_CRM_ELIG_EXPOSURE_SUBS = r100_CRM_ELIG_EXPOSURE_SUBS;
	}
	public BigDecimal getR100_ELIG_GUARANTEES() {
		return R100_ELIG_GUARANTEES;
	}
	public void setR100_ELIG_GUARANTEES(BigDecimal r100_ELIG_GUARANTEES) {
		R100_ELIG_GUARANTEES = r100_ELIG_GUARANTEES;
	}
	public BigDecimal getR100_CREDIT_DERIVATIVES() {
		return R100_CREDIT_DERIVATIVES;
	}
	public void setR100_CREDIT_DERIVATIVES(BigDecimal r100_CREDIT_DERIVATIVES) {
		R100_CREDIT_DERIVATIVES = r100_CREDIT_DERIVATIVES;
	}
	public BigDecimal getR100_CRM_COVERED_EXPOSURE() {
		return R100_CRM_COVERED_EXPOSURE;
	}
	public void setR100_CRM_COVERED_EXPOSURE(BigDecimal r100_CRM_COVERED_EXPOSURE) {
		R100_CRM_COVERED_EXPOSURE = r100_CRM_COVERED_EXPOSURE;
	}
	public BigDecimal getR100_CRM_NOT_COVERED_EXPOSURE() {
		return R100_CRM_NOT_COVERED_EXPOSURE;
	}
	public void setR100_CRM_NOT_COVERED_EXPOSURE(BigDecimal r100_CRM_NOT_COVERED_EXPOSURE) {
		R100_CRM_NOT_COVERED_EXPOSURE = r100_CRM_NOT_COVERED_EXPOSURE;
	}
	public BigDecimal getR100_CRM_RISK_WEIGHT() {
		return R100_CRM_RISK_WEIGHT;
	}
	public void setR100_CRM_RISK_WEIGHT(BigDecimal r100_CRM_RISK_WEIGHT) {
		R100_CRM_RISK_WEIGHT = r100_CRM_RISK_WEIGHT;
	}
	public BigDecimal getR100_RWA_CRM_COVERED() {
		return R100_RWA_CRM_COVERED;
	}
	public void setR100_RWA_CRM_COVERED(BigDecimal r100_RWA_CRM_COVERED) {
		R100_RWA_CRM_COVERED = r100_RWA_CRM_COVERED;
	}
	public BigDecimal getR100_ORIG_COUNTERPARTY_RW() {
		return R100_ORIG_COUNTERPARTY_RW;
	}
	public void setR100_ORIG_COUNTERPARTY_RW(BigDecimal r100_ORIG_COUNTERPARTY_RW) {
		R100_ORIG_COUNTERPARTY_RW = r100_ORIG_COUNTERPARTY_RW;
	}
	public BigDecimal getR100_RWA_CRM_NOT_COVERED() {
		return R100_RWA_CRM_NOT_COVERED;
	}
	public void setR100_RWA_CRM_NOT_COVERED(BigDecimal r100_RWA_CRM_NOT_COVERED) {
		R100_RWA_CRM_NOT_COVERED = r100_RWA_CRM_NOT_COVERED;
	}
	public BigDecimal getR100_CRM_ELIG_EXPOSURE_COMP() {
		return R100_CRM_ELIG_EXPOSURE_COMP;
	}
	public void setR100_CRM_ELIG_EXPOSURE_COMP(BigDecimal r100_CRM_ELIG_EXPOSURE_COMP) {
		R100_CRM_ELIG_EXPOSURE_COMP = r100_CRM_ELIG_EXPOSURE_COMP;
	}
	public BigDecimal getR100_EXPOSURE_AFTER_VOL_ADJ() {
		return R100_EXPOSURE_AFTER_VOL_ADJ;
	}
	public void setR100_EXPOSURE_AFTER_VOL_ADJ(BigDecimal r100_EXPOSURE_AFTER_VOL_ADJ) {
		R100_EXPOSURE_AFTER_VOL_ADJ = r100_EXPOSURE_AFTER_VOL_ADJ;
	}
	public BigDecimal getR100_COLLATERAL_CASH() {
		return R100_COLLATERAL_CASH;
	}
	public void setR100_COLLATERAL_CASH(BigDecimal r100_COLLATERAL_CASH) {
		R100_COLLATERAL_CASH = r100_COLLATERAL_CASH;
	}
	public BigDecimal getR100_COLLATERAL_TBILLS() {
		return R100_COLLATERAL_TBILLS;
	}
	public void setR100_COLLATERAL_TBILLS(BigDecimal r100_COLLATERAL_TBILLS) {
		R100_COLLATERAL_TBILLS = r100_COLLATERAL_TBILLS;
	}
	public BigDecimal getR100_COLLATERAL_DEBT_SEC() {
		return R100_COLLATERAL_DEBT_SEC;
	}
	public void setR100_COLLATERAL_DEBT_SEC(BigDecimal r100_COLLATERAL_DEBT_SEC) {
		R100_COLLATERAL_DEBT_SEC = r100_COLLATERAL_DEBT_SEC;
	}
	public BigDecimal getR100_COLLATERAL_EQUITIES() {
		return R100_COLLATERAL_EQUITIES;
	}
	public void setR100_COLLATERAL_EQUITIES(BigDecimal r100_COLLATERAL_EQUITIES) {
		R100_COLLATERAL_EQUITIES = r100_COLLATERAL_EQUITIES;
	}
	public BigDecimal getR100_COLLATERAL_MUTUAL_FUNDS() {
		return R100_COLLATERAL_MUTUAL_FUNDS;
	}
	public void setR100_COLLATERAL_MUTUAL_FUNDS(BigDecimal r100_COLLATERAL_MUTUAL_FUNDS) {
		R100_COLLATERAL_MUTUAL_FUNDS = r100_COLLATERAL_MUTUAL_FUNDS;
	}
	public BigDecimal getR100_TOTAL_COLLATERAL_HAIRCUT() {
		return R100_TOTAL_COLLATERAL_HAIRCUT;
	}
	public void setR100_TOTAL_COLLATERAL_HAIRCUT(BigDecimal r100_TOTAL_COLLATERAL_HAIRCUT) {
		R100_TOTAL_COLLATERAL_HAIRCUT = r100_TOTAL_COLLATERAL_HAIRCUT;
	}
	public BigDecimal getR100_EXPOSURE_AFTER_CRM() {
		return R100_EXPOSURE_AFTER_CRM;
	}
	public void setR100_EXPOSURE_AFTER_CRM(BigDecimal r100_EXPOSURE_AFTER_CRM) {
		R100_EXPOSURE_AFTER_CRM = r100_EXPOSURE_AFTER_CRM;
	}
	public BigDecimal getR100_RWA_NOT_COVERED_CRM() {
		return R100_RWA_NOT_COVERED_CRM;
	}
	public void setR100_RWA_NOT_COVERED_CRM(BigDecimal r100_RWA_NOT_COVERED_CRM) {
		R100_RWA_NOT_COVERED_CRM = r100_RWA_NOT_COVERED_CRM;
	}
	public BigDecimal getR100_RWA_UNSECURED_EXPOSURE() {
		return R100_RWA_UNSECURED_EXPOSURE;
	}
	public void setR100_RWA_UNSECURED_EXPOSURE(BigDecimal r100_RWA_UNSECURED_EXPOSURE) {
		R100_RWA_UNSECURED_EXPOSURE = r100_RWA_UNSECURED_EXPOSURE;
	}
	public BigDecimal getR100_RWA_UNSECURED() {
		return R100_RWA_UNSECURED;
	}
	public void setR100_RWA_UNSECURED(BigDecimal r100_RWA_UNSECURED) {
		R100_RWA_UNSECURED = r100_RWA_UNSECURED;
	}
	public BigDecimal getR100_TOTAL_RWA() {
		return R100_TOTAL_RWA;
	}
	public void setR100_TOTAL_RWA(BigDecimal r100_TOTAL_RWA) {
		R100_TOTAL_RWA = r100_TOTAL_RWA;
	}
	public M_SRWA_12B_SUMMARY_3_NEW_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
