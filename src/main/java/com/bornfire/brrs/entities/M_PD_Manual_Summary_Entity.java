
package com.bornfire.brrs.entities;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_PD_MANUAL_SUMMARYTABLE")
public class M_PD_Manual_Summary_Entity {


@Id
@Temporal(TemporalType.DATE)
@DateTimeFormat(pattern = "dd/MM/yyyy")
private Date report_date;
private String report_version;
private String report_frequency;
private String report_code;
private String report_desc;
private String entity_flg;
private String modify_flg;
private String del_flg;

/* R8 */
private BigDecimal R8_30D_90D_PASTDUE;
private BigDecimal R8_NON_PERFORM_LOANS;
private BigDecimal R8_NON_ACCRUALS1;
private BigDecimal R8_SPECIFIC_PROV1;
private BigDecimal R8_NO_OF_ACC1;
private BigDecimal R8_90D_180D_PASTDUE;
private BigDecimal R8_NON_ACCRUALS2;
private BigDecimal R8_SPECIFIC_PROV2;
private BigDecimal R8_NO_OF_ACC2;
private BigDecimal R8_180D_ABOVE_PASTDUE;
private BigDecimal R8_NON_ACCRUALS3;
private BigDecimal R8_SPECIFIC_PROV3;
private BigDecimal R8_NO_OF_ACC3;
private BigDecimal R8_TOTAL_NON_ACCRUAL;
private BigDecimal R8_TOTAL_DUE_LOANS;
private BigDecimal R8_TOTAL_PERFORMING_LOAN;
private BigDecimal R8_VALUE_OF_COLLATERAL;
private BigDecimal R8_TOTAL_VALUE_NPL;
private BigDecimal R8_TOTAL_SPECIFIC_PROV;
private BigDecimal R8_SPECIFIC_PROV_NPL;

/* R9 */
private BigDecimal R9_30D_90D_PASTDUE;
private BigDecimal R9_NON_PERFORM_LOANS;
private BigDecimal R9_NON_ACCRUALS1;
private BigDecimal R9_SPECIFIC_PROV1;
private BigDecimal R9_NO_OF_ACC1;
private BigDecimal R9_90D_180D_PASTDUE;
private BigDecimal R9_NON_ACCRUALS2;
private BigDecimal R9_SPECIFIC_PROV2;
private BigDecimal R9_NO_OF_ACC2;
private BigDecimal R9_TOTAL_NON_ACCRUAL;
private BigDecimal R9_TOTAL_DUE_LOANS;
private BigDecimal R9_TOTAL_PERFORMING_LOAN;
private BigDecimal R9_VALUE_OF_COLLATERAL;
private BigDecimal R9_TOTAL_VALUE_NPL;
private BigDecimal R9_TOTAL_SPECIFIC_PROV;
private BigDecimal R9_SPECIFIC_PROV_NPL;

/* R10 */
private BigDecimal R10_30D_90D_PASTDUE;
private BigDecimal R10_NON_PERFORM_LOANS;
private BigDecimal R10_NON_ACCRUALS1;
private BigDecimal R10_SPECIFIC_PROV1;
private BigDecimal R10_NO_OF_ACC1;
private BigDecimal R10_90D_180D_PASTDUE;
private BigDecimal R10_NON_ACCRUALS2;
private BigDecimal R10_SPECIFIC_PROV2;
private BigDecimal R10_NO_OF_ACC2;
private BigDecimal R10_TOTAL_NON_ACCRUAL;
private BigDecimal R10_TOTAL_DUE_LOANS;
private BigDecimal R10_TOTAL_PERFORMING_LOAN;
private BigDecimal R10_VALUE_OF_COLLATERAL;
private BigDecimal R10_TOTAL_VALUE_NPL;
private BigDecimal R10_TOTAL_SPECIFIC_PROV;
private BigDecimal R10_SPECIFIC_PROV_NPL;

/* R11 */
private BigDecimal R11_30D_90D_PASTDUE;
private BigDecimal R11_NON_PERFORM_LOANS;
private BigDecimal R11_NON_ACCRUALS1;
private BigDecimal R11_SPECIFIC_PROV1;
private BigDecimal R11_NO_OF_ACC1;
private BigDecimal R11_90D_180D_PASTDUE;
private BigDecimal R11_NON_ACCRUALS2;
private BigDecimal R11_SPECIFIC_PROV2;
private BigDecimal R11_NO_OF_ACC2;
private BigDecimal R11_TOTAL_NON_ACCRUAL;
private BigDecimal R11_TOTAL_DUE_LOANS;
private BigDecimal R11_TOTAL_PERFORMING_LOAN;
private BigDecimal R11_VALUE_OF_COLLATERAL;
private BigDecimal R11_TOTAL_VALUE_NPL;
private BigDecimal R11_TOTAL_SPECIFIC_PROV;
private BigDecimal R11_SPECIFIC_PROV_NPL;

/* R12 */
private BigDecimal R12_30D_90D_PASTDUE;
private BigDecimal R12_NON_PERFORM_LOANS;
private BigDecimal R12_NON_ACCRUALS1;
private BigDecimal R12_SPECIFIC_PROV1;
private BigDecimal R12_NO_OF_ACC1;
private BigDecimal R12_90D_180D_PASTDUE;
private BigDecimal R12_NON_ACCRUALS2;
private BigDecimal R12_SPECIFIC_PROV2;
private BigDecimal R12_NO_OF_ACC2;
private BigDecimal R12_180D_ABOVE_PASTDUE;
private BigDecimal R12_NON_ACCRUALS3;
private BigDecimal R12_SPECIFIC_PROV3;
private BigDecimal R12_NO_OF_ACC3;
private BigDecimal R12_TOTAL_NON_ACCRUAL;
private BigDecimal R12_TOTAL_DUE_LOANS;
private BigDecimal R12_TOTAL_PERFORMING_LOAN;
private BigDecimal R12_VALUE_OF_COLLATERAL;
private BigDecimal R12_TOTAL_VALUE_NPL;
private BigDecimal R12_TOTAL_SPECIFIC_PROV;
private BigDecimal R12_SPECIFIC_PROV_NPL;

/* R13 */
private BigDecimal R13_30D_90D_PASTDUE;
private BigDecimal R13_NON_PERFORM_LOANS;
private BigDecimal R13_NON_ACCRUALS1;
private BigDecimal R13_SPECIFIC_PROV1;
private BigDecimal R13_NO_OF_ACC1;
private BigDecimal R13_90D_180D_PASTDUE;
private BigDecimal R13_NON_ACCRUALS2;
private BigDecimal R13_SPECIFIC_PROV2;
private BigDecimal R13_NO_OF_ACC2;
private BigDecimal R13_TOTAL_NON_ACCRUAL;
private BigDecimal R13_TOTAL_DUE_LOANS;
private BigDecimal R13_TOTAL_PERFORMING_LOAN;
private BigDecimal R13_VALUE_OF_COLLATERAL;
private BigDecimal R13_TOTAL_VALUE_NPL;
private BigDecimal R13_TOTAL_SPECIFIC_PROV;
private BigDecimal R13_SPECIFIC_PROV_NPL;

/* R14 */
private BigDecimal R14_30D_90D_PASTDUE;
private BigDecimal R14_NON_PERFORM_LOANS;
private BigDecimal R14_NON_ACCRUALS1;
private BigDecimal R14_SPECIFIC_PROV1;
private BigDecimal R14_NO_OF_ACC1;
private BigDecimal R14_90D_180D_PASTDUE;
private BigDecimal R14_NON_ACCRUALS2;
private BigDecimal R14_SPECIFIC_PROV2;
private BigDecimal R14_NO_OF_ACC2;
private BigDecimal R14_TOTAL_NON_ACCRUAL;
private BigDecimal R14_TOTAL_DUE_LOANS;
private BigDecimal R14_TOTAL_PERFORMING_LOAN;
private BigDecimal R14_VALUE_OF_COLLATERAL;
private BigDecimal R14_TOTAL_VALUE_NPL;
private BigDecimal R14_TOTAL_SPECIFIC_PROV;
private BigDecimal R14_SPECIFIC_PROV_NPL;

/* R15 */
private BigDecimal R15_30D_90D_PASTDUE;
private BigDecimal R15_NON_PERFORM_LOANS;
private BigDecimal R15_NON_ACCRUALS1;
private BigDecimal R15_SPECIFIC_PROV1;
private BigDecimal R15_NO_OF_ACC1;
private BigDecimal R15_90D_180D_PASTDUE;
private BigDecimal R15_NON_ACCRUALS2;
private BigDecimal R15_SPECIFIC_PROV2;
private BigDecimal R15_NO_OF_ACC2;
private BigDecimal R15_TOTAL_NON_ACCRUAL;
private BigDecimal R15_TOTAL_DUE_LOANS;
private BigDecimal R15_TOTAL_PERFORMING_LOAN;
private BigDecimal R15_VALUE_OF_COLLATERAL;
private BigDecimal R15_TOTAL_VALUE_NPL;
private BigDecimal R15_TOTAL_SPECIFIC_PROV;
private BigDecimal R15_SPECIFIC_PROV_NPL;

/* R16 */
private BigDecimal R16_30D_90D_PASTDUE;
private BigDecimal R16_NON_PERFORM_LOANS;
private BigDecimal R16_NON_ACCRUALS1;
private BigDecimal R16_SPECIFIC_PROV1;
private BigDecimal R16_NO_OF_ACC1;
private BigDecimal R16_90D_180D_PASTDUE;
private BigDecimal R16_NON_ACCRUALS2;
private BigDecimal R16_SPECIFIC_PROV2;
private BigDecimal R16_NO_OF_ACC2;
private BigDecimal R16_TOTAL_NON_ACCRUAL;
private BigDecimal R16_TOTAL_DUE_LOANS;
private BigDecimal R16_TOTAL_PERFORMING_LOAN;
private BigDecimal R16_VALUE_OF_COLLATERAL;
private BigDecimal R16_TOTAL_VALUE_NPL;
private BigDecimal R16_TOTAL_SPECIFIC_PROV;
private BigDecimal R16_SPECIFIC_PROV_NPL;

/* R17 */
private BigDecimal R17_30D_90D_PASTDUE;
private BigDecimal R17_NON_PERFORM_LOANS;
private BigDecimal R17_NON_ACCRUALS1;
private BigDecimal R17_SPECIFIC_PROV1;
private BigDecimal R17_NO_OF_ACC1;
private BigDecimal R17_90D_180D_PASTDUE;
private BigDecimal R17_NON_ACCRUALS2;
private BigDecimal R17_SPECIFIC_PROV2;
private BigDecimal R17_NO_OF_ACC2;
private BigDecimal R17_TOTAL_NON_ACCRUAL;
private BigDecimal R17_TOTAL_DUE_LOANS;
private BigDecimal R17_TOTAL_PERFORMING_LOAN;
private BigDecimal R17_VALUE_OF_COLLATERAL;
private BigDecimal R17_TOTAL_VALUE_NPL;
private BigDecimal R17_TOTAL_SPECIFIC_PROV;
private BigDecimal R17_SPECIFIC_PROV_NPL;

/* R18 */
private BigDecimal R18_30D_90D_PASTDUE;
private BigDecimal R18_NON_PERFORM_LOANS;
private BigDecimal R18_NON_ACCRUALS1;
private BigDecimal R18_SPECIFIC_PROV1;
private BigDecimal R18_NO_OF_ACC1;
private BigDecimal R18_90D_180D_PASTDUE;
private BigDecimal R18_NON_ACCRUALS2;
private BigDecimal R18_SPECIFIC_PROV2;
private BigDecimal R18_NO_OF_ACC2;
private BigDecimal R18_TOTAL_NON_ACCRUAL;
private BigDecimal R18_TOTAL_DUE_LOANS;
private BigDecimal R18_TOTAL_PERFORMING_LOAN;
private BigDecimal R18_VALUE_OF_COLLATERAL;
private BigDecimal R18_TOTAL_VALUE_NPL;
private BigDecimal R18_TOTAL_SPECIFIC_PROV;
private BigDecimal R18_SPECIFIC_PROV_NPL;

/* R19 */
private BigDecimal R19_30D_90D_PASTDUE;
private BigDecimal R19_NON_PERFORM_LOANS;
private BigDecimal R19_NON_ACCRUALS1;
private BigDecimal R19_SPECIFIC_PROV1;
private BigDecimal R19_NO_OF_ACC1;
private BigDecimal R19_90D_180D_PASTDUE;
private BigDecimal R19_NON_ACCRUALS2;
private BigDecimal R19_SPECIFIC_PROV2;
private BigDecimal R19_NO_OF_ACC2;
private BigDecimal R19_TOTAL_NON_ACCRUAL;
private BigDecimal R19_TOTAL_DUE_LOANS;
private BigDecimal R19_TOTAL_PERFORMING_LOAN;
private BigDecimal R19_VALUE_OF_COLLATERAL;
private BigDecimal R19_TOTAL_VALUE_NPL;
private BigDecimal R19_TOTAL_SPECIFIC_PROV;
private BigDecimal R19_SPECIFIC_PROV_NPL;

/* R20 */
private BigDecimal R20_30D_90D_PASTDUE;
private BigDecimal R20_NON_PERFORM_LOANS;
private BigDecimal R20_NON_ACCRUALS1;
private BigDecimal R20_SPECIFIC_PROV1;
private BigDecimal R20_NO_OF_ACC1;
private BigDecimal R20_90D_180D_PASTDUE;
private BigDecimal R20_NON_ACCRUALS2;
private BigDecimal R20_SPECIFIC_PROV2;
private BigDecimal R20_NO_OF_ACC2;
private BigDecimal R20_TOTAL_NON_ACCRUAL;
private BigDecimal R20_TOTAL_DUE_LOANS;
private BigDecimal R20_TOTAL_PERFORMING_LOAN;
private BigDecimal R20_VALUE_OF_COLLATERAL;
private BigDecimal R20_TOTAL_VALUE_NPL;
private BigDecimal R20_TOTAL_SPECIFIC_PROV;
private BigDecimal R20_SPECIFIC_PROV_NPL;

/* R21 */
private BigDecimal R21_30D_90D_PASTDUE;
private BigDecimal R21_NON_PERFORM_LOANS;
private BigDecimal R21_NON_ACCRUALS1;
private BigDecimal R21_SPECIFIC_PROV1;
private BigDecimal R21_NO_OF_ACC1;
private BigDecimal R21_90D_180D_PASTDUE;
private BigDecimal R21_NON_ACCRUALS2;
private BigDecimal R21_SPECIFIC_PROV2;
private BigDecimal R21_NO_OF_ACC2;
private BigDecimal R21_TOTAL_NON_ACCRUAL;
private BigDecimal R21_TOTAL_DUE_LOANS;
private BigDecimal R21_TOTAL_PERFORMING_LOAN;
private BigDecimal R21_VALUE_OF_COLLATERAL;
private BigDecimal R21_TOTAL_VALUE_NPL;
private BigDecimal R21_TOTAL_SPECIFIC_PROV;
private BigDecimal R21_SPECIFIC_PROV_NPL;

/* R22 */
private BigDecimal R22_30D_90D_PASTDUE;
private BigDecimal R22_NON_PERFORM_LOANS;
private BigDecimal R22_NON_ACCRUALS1;
private BigDecimal R22_SPECIFIC_PROV1;
private BigDecimal R22_NO_OF_ACC1;
private BigDecimal R22_90D_180D_PASTDUE;
private BigDecimal R22_NON_ACCRUALS2;
private BigDecimal R22_SPECIFIC_PROV2;
private BigDecimal R22_NO_OF_ACC2;
private BigDecimal R22_TOTAL_NON_ACCRUAL;
private BigDecimal R22_TOTAL_DUE_LOANS;
private BigDecimal R22_TOTAL_PERFORMING_LOAN;
private BigDecimal R22_VALUE_OF_COLLATERAL;
private BigDecimal R22_TOTAL_VALUE_NPL;
private BigDecimal R22_TOTAL_SPECIFIC_PROV;
private BigDecimal R22_SPECIFIC_PROV_NPL;

/* R23 */
private BigDecimal R23_30D_90D_PASTDUE;
private BigDecimal R23_NON_PERFORM_LOANS;
private BigDecimal R23_NON_ACCRUALS1;
private BigDecimal R23_SPECIFIC_PROV1;
private BigDecimal R23_NO_OF_ACC1;
private BigDecimal R23_90D_180D_PASTDUE;
private BigDecimal R23_NON_ACCRUALS2;
private BigDecimal R23_SPECIFIC_PROV2;
private BigDecimal R23_NO_OF_ACC2;
private BigDecimal R23_TOTAL_NON_ACCRUAL;
private BigDecimal R23_TOTAL_DUE_LOANS;
private BigDecimal R23_TOTAL_PERFORMING_LOAN;
private BigDecimal R23_VALUE_OF_COLLATERAL;
private BigDecimal R23_TOTAL_VALUE_NPL;
private BigDecimal R23_TOTAL_SPECIFIC_PROV;
private BigDecimal R23_SPECIFIC_PROV_NPL;

/* R24 */
private BigDecimal R24_30D_90D_PASTDUE;
private BigDecimal R24_NON_PERFORM_LOANS;
private BigDecimal R24_NON_ACCRUALS1;
private BigDecimal R24_SPECIFIC_PROV1;
private BigDecimal R24_NO_OF_ACC1;
private BigDecimal R24_90D_180D_PASTDUE;
private BigDecimal R24_NON_ACCRUALS2;
private BigDecimal R24_SPECIFIC_PROV2;
private BigDecimal R24_NO_OF_ACC2;
private BigDecimal R24_TOTAL_NON_ACCRUAL;
private BigDecimal R24_TOTAL_DUE_LOANS;
private BigDecimal R24_TOTAL_PERFORMING_LOAN;
private BigDecimal R24_VALUE_OF_COLLATERAL;
private BigDecimal R24_TOTAL_VALUE_NPL;
private BigDecimal R24_TOTAL_SPECIFIC_PROV;
private BigDecimal R24_SPECIFIC_PROV_NPL;

/* R25 */
private BigDecimal R25_30D_90D_PASTDUE;
private BigDecimal R25_NON_PERFORM_LOANS;
private BigDecimal R25_NON_ACCRUALS1;
private BigDecimal R25_SPECIFIC_PROV1;
private BigDecimal R25_NO_OF_ACC1;
private BigDecimal R25_90D_180D_PASTDUE;
private BigDecimal R25_NON_ACCRUALS2;
private BigDecimal R25_SPECIFIC_PROV2;
private BigDecimal R25_NO_OF_ACC2;
private BigDecimal R25_TOTAL_NON_ACCRUAL;
private BigDecimal R25_TOTAL_DUE_LOANS;
private BigDecimal R25_TOTAL_PERFORMING_LOAN;
private BigDecimal R25_VALUE_OF_COLLATERAL;
private BigDecimal R25_TOTAL_VALUE_NPL;
private BigDecimal R25_TOTAL_SPECIFIC_PROV;
private BigDecimal R25_SPECIFIC_PROV_NPL;

/* R26 */
private BigDecimal R26_30D_90D_PASTDUE;
private BigDecimal R26_NON_PERFORM_LOANS;
private BigDecimal R26_NON_ACCRUALS1;
private BigDecimal R26_SPECIFIC_PROV1;
private BigDecimal R26_NO_OF_ACC1;
private BigDecimal R26_90D_180D_PASTDUE;
private BigDecimal R26_NON_ACCRUALS2;
private BigDecimal R26_SPECIFIC_PROV2;
private BigDecimal R26_NO_OF_ACC2;
private BigDecimal R26_180D_ABOVE_PASTDUE;
private BigDecimal R26_NON_ACCRUALS3;
private BigDecimal R26_SPECIFIC_PROV3;
private BigDecimal R26_NO_OF_ACC3;
private BigDecimal R26_TOTAL_NON_ACCRUAL;
private BigDecimal R26_TOTAL_DUE_LOANS;
private BigDecimal R26_TOTAL_PERFORMING_LOAN;
private BigDecimal R26_VALUE_OF_COLLATERAL;
private BigDecimal R26_TOTAL_VALUE_NPL;
private BigDecimal R26_TOTAL_SPECIFIC_PROV;
private BigDecimal R26_SPECIFIC_PROV_NPL;

/* R27 */
private BigDecimal R27_30D_90D_PASTDUE;
private BigDecimal R27_NON_PERFORM_LOANS;
private BigDecimal R27_NON_ACCRUALS1;
private BigDecimal R27_SPECIFIC_PROV1;
private BigDecimal R27_NO_OF_ACC1;
private BigDecimal R27_90D_180D_PASTDUE;
private BigDecimal R27_NON_ACCRUALS2;
private BigDecimal R27_SPECIFIC_PROV2;
private BigDecimal R27_NO_OF_ACC2;
private BigDecimal R27_TOTAL_NON_ACCRUAL;
private BigDecimal R27_TOTAL_DUE_LOANS;
private BigDecimal R27_TOTAL_PERFORMING_LOAN;
private BigDecimal R27_VALUE_OF_COLLATERAL;
private BigDecimal R27_TOTAL_VALUE_NPL;
private BigDecimal R27_TOTAL_SPECIFIC_PROV;
private BigDecimal R27_SPECIFIC_PROV_NPL;

/* R28 */
private BigDecimal R28_30D_90D_PASTDUE;
private BigDecimal R28_NON_PERFORM_LOANS;
private BigDecimal R28_NON_ACCRUALS1;
private BigDecimal R28_SPECIFIC_PROV1;
private BigDecimal R28_NO_OF_ACC1;
private BigDecimal R28_90D_180D_PASTDUE;
private BigDecimal R28_NON_ACCRUALS2;
private BigDecimal R28_SPECIFIC_PROV2;
private BigDecimal R28_NO_OF_ACC2;
private BigDecimal R28_TOTAL_NON_ACCRUAL;
private BigDecimal R28_TOTAL_DUE_LOANS;
private BigDecimal R28_TOTAL_PERFORMING_LOAN;
private BigDecimal R28_VALUE_OF_COLLATERAL;
private BigDecimal R28_TOTAL_VALUE_NPL;
private BigDecimal R28_TOTAL_SPECIFIC_PROV;
private BigDecimal R28_SPECIFIC_PROV_NPL;

/* R29 */
private BigDecimal R29_30D_90D_PASTDUE;
private BigDecimal R29_NON_PERFORM_LOANS;
private BigDecimal R29_NON_ACCRUALS1;
private BigDecimal R29_SPECIFIC_PROV1;
private BigDecimal R29_NO_OF_ACC1;
private BigDecimal R29_90D_180D_PASTDUE;
private BigDecimal R29_NON_ACCRUALS2;
private BigDecimal R29_SPECIFIC_PROV2;
private BigDecimal R29_NO_OF_ACC2;
private BigDecimal R29_TOTAL_NON_ACCRUAL;
private BigDecimal R29_TOTAL_DUE_LOANS;
private BigDecimal R29_TOTAL_PERFORMING_LOAN;
private BigDecimal R29_VALUE_OF_COLLATERAL;
private BigDecimal R29_TOTAL_VALUE_NPL;
private BigDecimal R29_TOTAL_SPECIFIC_PROV;
private BigDecimal R29_SPECIFIC_PROV_NPL;

/* R30 */
private BigDecimal R30_30D_90D_PASTDUE;
private BigDecimal R30_NON_PERFORM_LOANS;
private BigDecimal R30_NON_ACCRUALS1;
private BigDecimal R30_SPECIFIC_PROV1;
private BigDecimal R30_NO_OF_ACC1;
private BigDecimal R30_90D_180D_PASTDUE;
private BigDecimal R30_NON_ACCRUALS2;
private BigDecimal R30_SPECIFIC_PROV2;
private BigDecimal R30_NO_OF_ACC2;
private BigDecimal R30_TOTAL_NON_ACCRUAL;
private BigDecimal R30_TOTAL_DUE_LOANS;
private BigDecimal R30_TOTAL_PERFORMING_LOAN;
private BigDecimal R30_VALUE_OF_COLLATERAL;
private BigDecimal R30_TOTAL_VALUE_NPL;
private BigDecimal R30_TOTAL_SPECIFIC_PROV;
private BigDecimal R30_SPECIFIC_PROV_NPL;

/* R31 */
private BigDecimal R31_30D_90D_PASTDUE;
private BigDecimal R31_NON_PERFORM_LOANS;
private BigDecimal R31_NON_ACCRUALS1;
private BigDecimal R31_SPECIFIC_PROV1;
private BigDecimal R31_NO_OF_ACC1;
private BigDecimal R31_90D_180D_PASTDUE;
private BigDecimal R31_NON_ACCRUALS2;
private BigDecimal R31_SPECIFIC_PROV2;
private BigDecimal R31_NO_OF_ACC2;
private BigDecimal R31_TOTAL_NON_ACCRUAL;
private BigDecimal R31_TOTAL_DUE_LOANS;
private BigDecimal R31_TOTAL_PERFORMING_LOAN;
private BigDecimal R31_VALUE_OF_COLLATERAL;
private BigDecimal R31_TOTAL_VALUE_NPL;
private BigDecimal R31_TOTAL_SPECIFIC_PROV;
private BigDecimal R31_SPECIFIC_PROV_NPL;

/* R32 */
private BigDecimal R32_30D_90D_PASTDUE;
private BigDecimal R32_NON_PERFORM_LOANS;
private BigDecimal R32_NON_ACCRUALS1;
private BigDecimal R32_SPECIFIC_PROV1;
private BigDecimal R32_NO_OF_ACC1;
private BigDecimal R32_90D_180D_PASTDUE;
private BigDecimal R32_NON_ACCRUALS2;
private BigDecimal R32_SPECIFIC_PROV2;
private BigDecimal R32_NO_OF_ACC2;
private BigDecimal R32_TOTAL_NON_ACCRUAL;
private BigDecimal R32_TOTAL_DUE_LOANS;
private BigDecimal R32_TOTAL_PERFORMING_LOAN;
private BigDecimal R32_VALUE_OF_COLLATERAL;
private BigDecimal R32_TOTAL_VALUE_NPL;
private BigDecimal R32_TOTAL_SPECIFIC_PROV;
private BigDecimal R32_SPECIFIC_PROV_NPL;

/* R33 */
private BigDecimal R33_30D_90D_PASTDUE;
private BigDecimal R33_NON_PERFORM_LOANS;
private BigDecimal R33_NON_ACCRUALS1;
private BigDecimal R33_SPECIFIC_PROV1;
private BigDecimal R33_NO_OF_ACC1;
private BigDecimal R33_90D_180D_PASTDUE;
private BigDecimal R33_NON_ACCRUALS2;
private BigDecimal R33_SPECIFIC_PROV2;
private BigDecimal R33_NO_OF_ACC2;
private BigDecimal R33_TOTAL_NON_ACCRUAL;
private BigDecimal R33_TOTAL_DUE_LOANS;
private BigDecimal R33_TOTAL_PERFORMING_LOAN;
private BigDecimal R33_VALUE_OF_COLLATERAL;
private BigDecimal R33_TOTAL_VALUE_NPL;
private BigDecimal R33_TOTAL_SPECIFIC_PROV;
private BigDecimal R33_SPECIFIC_PROV_NPL;

/* R34 */
private BigDecimal R34_30D_90D_PASTDUE;
private BigDecimal R34_NON_PERFORM_LOANS;
private BigDecimal R34_NON_ACCRUALS1;
private BigDecimal R34_SPECIFIC_PROV1;
private BigDecimal R34_NO_OF_ACC1;
private BigDecimal R34_90D_180D_PASTDUE;
private BigDecimal R34_NON_ACCRUALS2;
private BigDecimal R34_SPECIFIC_PROV2;
private BigDecimal R34_NO_OF_ACC2;
private BigDecimal R34_TOTAL_NON_ACCRUAL;
private BigDecimal R34_TOTAL_DUE_LOANS;
private BigDecimal R34_TOTAL_PERFORMING_LOAN;
private BigDecimal R34_VALUE_OF_COLLATERAL;
private BigDecimal R34_TOTAL_VALUE_NPL;
private BigDecimal R34_TOTAL_SPECIFIC_PROV;
private BigDecimal R34_SPECIFIC_PROV_NPL;

/* R35 */
private BigDecimal R35_30D_90D_PASTDUE;
private BigDecimal R35_NON_PERFORM_LOANS;
private BigDecimal R35_NON_ACCRUALS1;
private BigDecimal R35_SPECIFIC_PROV1;
private BigDecimal R35_NO_OF_ACC1;
private BigDecimal R35_90D_180D_PASTDUE;
private BigDecimal R35_NON_ACCRUALS2;
private BigDecimal R35_SPECIFIC_PROV2;
private BigDecimal R35_NO_OF_ACC2;
private BigDecimal R35_180D_ABOVE_PASTDUE;
private BigDecimal R35_NON_ACCRUALS3;
private BigDecimal R35_SPECIFIC_PROV3;
private BigDecimal R35_NO_OF_ACC3;
private BigDecimal R35_TOTAL_NON_ACCRUAL;
private BigDecimal R35_TOTAL_DUE_LOANS;
private BigDecimal R35_TOTAL_PERFORMING_LOAN;
private BigDecimal R35_VALUE_OF_COLLATERAL;
private BigDecimal R35_TOTAL_VALUE_NPL;
private BigDecimal R35_TOTAL_SPECIFIC_PROV;
private BigDecimal R35_SPECIFIC_PROV_NPL;

/* R36 */
private BigDecimal R36_30D_90D_PASTDUE;
private BigDecimal R36_NON_PERFORM_LOANS;
private BigDecimal R36_NON_ACCRUALS1;
private BigDecimal R36_SPECIFIC_PROV1;
private BigDecimal R36_NO_OF_ACC1;
private BigDecimal R36_90D_180D_PASTDUE;
private BigDecimal R36_NON_ACCRUALS2;
private BigDecimal R36_SPECIFIC_PROV2;
private BigDecimal R36_NO_OF_ACC2;
private BigDecimal R36_TOTAL_NON_ACCRUAL;
private BigDecimal R36_TOTAL_DUE_LOANS;
private BigDecimal R36_TOTAL_PERFORMING_LOAN;
private BigDecimal R36_VALUE_OF_COLLATERAL;
private BigDecimal R36_TOTAL_VALUE_NPL;
private BigDecimal R36_TOTAL_SPECIFIC_PROV;
private BigDecimal R36_SPECIFIC_PROV_NPL;

/* R37 */
private BigDecimal R37_30D_90D_PASTDUE;
private BigDecimal R37_NON_PERFORM_LOANS;
private BigDecimal R37_NON_ACCRUALS1;
private BigDecimal R37_SPECIFIC_PROV1;
private BigDecimal R37_NO_OF_ACC1;
private BigDecimal R37_90D_180D_PASTDUE;
private BigDecimal R37_NON_ACCRUALS2;
private BigDecimal R37_SPECIFIC_PROV2;
private BigDecimal R37_NO_OF_ACC2;
private BigDecimal R37_TOTAL_NON_ACCRUAL;
private BigDecimal R37_TOTAL_DUE_LOANS;
private BigDecimal R37_TOTAL_PERFORMING_LOAN;
private BigDecimal R37_VALUE_OF_COLLATERAL;
private BigDecimal R37_TOTAL_VALUE_NPL;
private BigDecimal R37_TOTAL_SPECIFIC_PROV;
private BigDecimal R37_SPECIFIC_PROV_NPL;

/* R38 */
private BigDecimal R38_30D_90D_PASTDUE;
private BigDecimal R38_NON_PERFORM_LOANS;
private BigDecimal R38_NON_ACCRUALS1;
private BigDecimal R38_SPECIFIC_PROV1;
private BigDecimal R38_NO_OF_ACC1;
private BigDecimal R38_90D_180D_PASTDUE;
private BigDecimal R38_NON_ACCRUALS2;
private BigDecimal R38_SPECIFIC_PROV2;
private BigDecimal R38_NO_OF_ACC2;
private BigDecimal R38_180D_ABOVE_PASTDUE;
private BigDecimal R38_NON_ACCRUALS3;
private BigDecimal R38_SPECIFIC_PROV3;
private BigDecimal R38_NO_OF_ACC3;
private BigDecimal R38_TOTAL_NON_ACCRUAL;
private BigDecimal R38_TOTAL_DUE_LOANS;
private BigDecimal R38_TOTAL_PERFORMING_LOAN;
private BigDecimal R38_VALUE_OF_COLLATERAL;
private BigDecimal R38_TOTAL_VALUE_NPL;
private BigDecimal R38_TOTAL_SPECIFIC_PROV;
private BigDecimal R38_SPECIFIC_PROV_NPL;

/* R39 */
private BigDecimal R39_30D_90D_PASTDUE;
private BigDecimal R39_NON_PERFORM_LOANS;
private BigDecimal R39_NON_ACCRUALS1;
private BigDecimal R39_SPECIFIC_PROV1;
private BigDecimal R39_NO_OF_ACC1;
private BigDecimal R39_90D_180D_PASTDUE;
private BigDecimal R39_NON_ACCRUALS2;
private BigDecimal R39_SPECIFIC_PROV2;
private BigDecimal R39_NO_OF_ACC2;
private BigDecimal R39_TOTAL_NON_ACCRUAL;
private BigDecimal R39_TOTAL_DUE_LOANS;
private BigDecimal R39_TOTAL_PERFORMING_LOAN;
private BigDecimal R39_VALUE_OF_COLLATERAL;
private BigDecimal R39_TOTAL_VALUE_NPL;
private BigDecimal R39_TOTAL_SPECIFIC_PROV;
private BigDecimal R39_SPECIFIC_PROV_NPL;

/* R40 */
private BigDecimal R40_30D_90D_PASTDUE;
private BigDecimal R40_NON_PERFORM_LOANS;
private BigDecimal R40_NON_ACCRUALS1;
private BigDecimal R40_SPECIFIC_PROV1;
private BigDecimal R40_NO_OF_ACC1;
private BigDecimal R40_90D_180D_PASTDUE;
private BigDecimal R40_NON_ACCRUALS2;
private BigDecimal R40_SPECIFIC_PROV2;
private BigDecimal R40_NO_OF_ACC2;
private BigDecimal R40_TOTAL_NON_ACCRUAL;
private BigDecimal R40_TOTAL_DUE_LOANS;
private BigDecimal R40_TOTAL_PERFORMING_LOAN;
private BigDecimal R40_VALUE_OF_COLLATERAL;
private BigDecimal R40_TOTAL_VALUE_NPL;
private BigDecimal R40_TOTAL_SPECIFIC_PROV;
private BigDecimal R40_SPECIFIC_PROV_NPL;

/* R41 */
private BigDecimal R41_30D_90D_PASTDUE;
private BigDecimal R41_NON_PERFORM_LOANS;
private BigDecimal R41_NON_ACCRUALS1;
private BigDecimal R41_SPECIFIC_PROV1;
private BigDecimal R41_NO_OF_ACC1;
private BigDecimal R41_90D_180D_PASTDUE;
private BigDecimal R41_NON_ACCRUALS2;
private BigDecimal R41_SPECIFIC_PROV2;
private BigDecimal R41_NO_OF_ACC2;
private BigDecimal R41_180D_ABOVE_PASTDUE;
private BigDecimal R41_NON_ACCRUALS3;
private BigDecimal R41_SPECIFIC_PROV3;
private BigDecimal R41_NO_OF_ACC3;
private BigDecimal R41_TOTAL_NON_ACCRUAL;
private BigDecimal R41_TOTAL_DUE_LOANS;
private BigDecimal R41_TOTAL_PERFORMING_LOAN;
private BigDecimal R41_VALUE_OF_COLLATERAL;
private BigDecimal R41_TOTAL_VALUE_NPL;
private BigDecimal R41_TOTAL_SPECIFIC_PROV;
private BigDecimal R41_SPECIFIC_PROV_NPL;

/* R42 */
private BigDecimal R42_30D_90D_PASTDUE;
private BigDecimal R42_NON_PERFORM_LOANS;
private BigDecimal R42_NON_ACCRUALS1;
private BigDecimal R42_SPECIFIC_PROV1;
private BigDecimal R42_NO_OF_ACC1;
private BigDecimal R42_90D_180D_PASTDUE;
private BigDecimal R42_NON_ACCRUALS2;
private BigDecimal R42_SPECIFIC_PROV2;
private BigDecimal R42_NO_OF_ACC2;
private BigDecimal R42_TOTAL_NON_ACCRUAL;
private BigDecimal R42_TOTAL_DUE_LOANS;
private BigDecimal R42_TOTAL_PERFORMING_LOAN;
private BigDecimal R42_VALUE_OF_COLLATERAL;
private BigDecimal R42_TOTAL_VALUE_NPL;
private BigDecimal R42_TOTAL_SPECIFIC_PROV;
private BigDecimal R42_SPECIFIC_PROV_NPL;

/* R43 */
private BigDecimal R43_30D_90D_PASTDUE;
private BigDecimal R43_NON_PERFORM_LOANS;
private BigDecimal R43_NON_ACCRUALS1;
private BigDecimal R43_SPECIFIC_PROV1;
private BigDecimal R43_NO_OF_ACC1;
private BigDecimal R43_90D_180D_PASTDUE;
private BigDecimal R43_NON_ACCRUALS2;
private BigDecimal R43_SPECIFIC_PROV2;
private BigDecimal R43_NO_OF_ACC2;
private BigDecimal R43_TOTAL_NON_ACCRUAL;
private BigDecimal R43_TOTAL_DUE_LOANS;
private BigDecimal R43_TOTAL_PERFORMING_LOAN;
private BigDecimal R43_VALUE_OF_COLLATERAL;
private BigDecimal R43_TOTAL_VALUE_NPL;
private BigDecimal R43_TOTAL_SPECIFIC_PROV;
private BigDecimal R43_SPECIFIC_PROV_NPL;

/* R44 */
private BigDecimal R44_30D_90D_PASTDUE;
private BigDecimal R44_NON_PERFORM_LOANS;
private BigDecimal R44_NON_ACCRUALS1;
private BigDecimal R44_SPECIFIC_PROV1;
private BigDecimal R44_NO_OF_ACC1;
private BigDecimal R44_90D_180D_PASTDUE;
private BigDecimal R44_NON_ACCRUALS2;
private BigDecimal R44_SPECIFIC_PROV2;
private BigDecimal R44_NO_OF_ACC2;
private BigDecimal R44_TOTAL_NON_ACCRUAL;
private BigDecimal R44_TOTAL_DUE_LOANS;
private BigDecimal R44_TOTAL_PERFORMING_LOAN;
private BigDecimal R44_VALUE_OF_COLLATERAL;
private BigDecimal R44_TOTAL_VALUE_NPL;
private BigDecimal R44_TOTAL_SPECIFIC_PROV;
private BigDecimal R44_SPECIFIC_PROV_NPL;

/* R45 */
private BigDecimal R45_30D_90D_PASTDUE;
private BigDecimal R45_NON_PERFORM_LOANS;
private BigDecimal R45_NON_ACCRUALS1;
private BigDecimal R45_SPECIFIC_PROV1;
private BigDecimal R45_NO_OF_ACC1;
private BigDecimal R45_90D_180D_PASTDUE;
private BigDecimal R45_NON_ACCRUALS2;
private BigDecimal R45_SPECIFIC_PROV2;
private BigDecimal R45_NO_OF_ACC2;
private BigDecimal R45_TOTAL_NON_ACCRUAL;
private BigDecimal R45_TOTAL_DUE_LOANS;
private BigDecimal R45_TOTAL_PERFORMING_LOAN;
private BigDecimal R45_VALUE_OF_COLLATERAL;
private BigDecimal R45_TOTAL_VALUE_NPL;
private BigDecimal R45_TOTAL_SPECIFIC_PROV;
private BigDecimal R45_SPECIFIC_PROV_NPL;

/* R46 */
private BigDecimal R46_30D_90D_PASTDUE;
private BigDecimal R46_NON_PERFORM_LOANS;
private BigDecimal R46_NON_ACCRUALS1;
private BigDecimal R46_SPECIFIC_PROV1;
private BigDecimal R46_NO_OF_ACC1;
private BigDecimal R46_90D_180D_PASTDUE;
private BigDecimal R46_NON_ACCRUALS2;
private BigDecimal R46_SPECIFIC_PROV2;
private BigDecimal R46_NO_OF_ACC2;
private BigDecimal R46_180D_ABOVE_PASTDUE;
private BigDecimal R46_NON_ACCRUALS3;
private BigDecimal R46_SPECIFIC_PROV3;
private BigDecimal R46_NO_OF_ACC3;
private BigDecimal R46_TOTAL_NON_ACCRUAL;
private BigDecimal R46_TOTAL_DUE_LOANS;
private BigDecimal R46_TOTAL_PERFORMING_LOAN;
private BigDecimal R46_VALUE_OF_COLLATERAL;
private BigDecimal R46_TOTAL_VALUE_NPL;
private BigDecimal R46_TOTAL_SPECIFIC_PROV;
private BigDecimal R46_SPECIFIC_PROV_NPL;

/* R47 */
private BigDecimal R47_30D_90D_PASTDUE;
private BigDecimal R47_NON_PERFORM_LOANS;
private BigDecimal R47_NON_ACCRUALS1;
private BigDecimal R47_SPECIFIC_PROV1;
private BigDecimal R47_NO_OF_ACC1;
private BigDecimal R47_90D_180D_PASTDUE;
private BigDecimal R47_NON_ACCRUALS2;
private BigDecimal R47_SPECIFIC_PROV2;
private BigDecimal R47_NO_OF_ACC2;
private BigDecimal R47_TOTAL_NON_ACCRUAL;
private BigDecimal R47_TOTAL_DUE_LOANS;
private BigDecimal R47_TOTAL_PERFORMING_LOAN;
private BigDecimal R47_VALUE_OF_COLLATERAL;
private BigDecimal R47_TOTAL_VALUE_NPL;
private BigDecimal R47_TOTAL_SPECIFIC_PROV;
private BigDecimal R47_SPECIFIC_PROV_NPL;

/* R48 */
private BigDecimal R48_30D_90D_PASTDUE;
private BigDecimal R48_NON_PERFORM_LOANS;
private BigDecimal R48_NON_ACCRUALS1;
private BigDecimal R48_SPECIFIC_PROV1;
private BigDecimal R48_NO_OF_ACC1;
private BigDecimal R48_90D_180D_PASTDUE;
private BigDecimal R48_NON_ACCRUALS2;
private BigDecimal R48_SPECIFIC_PROV2;
private BigDecimal R48_NO_OF_ACC2;
private BigDecimal R48_TOTAL_NON_ACCRUAL;
private BigDecimal R48_TOTAL_DUE_LOANS;
private BigDecimal R48_TOTAL_PERFORMING_LOAN;
private BigDecimal R48_VALUE_OF_COLLATERAL;
private BigDecimal R48_TOTAL_VALUE_NPL;
private BigDecimal R48_TOTAL_SPECIFIC_PROV;
private BigDecimal R48_SPECIFIC_PROV_NPL;

/* R49 */
private BigDecimal R49_30D_90D_PASTDUE;
private BigDecimal R49_NON_PERFORM_LOANS;
private BigDecimal R49_NON_ACCRUALS1;
private BigDecimal R49_SPECIFIC_PROV1;
private BigDecimal R49_NO_OF_ACC1;
private BigDecimal R49_90D_180D_PASTDUE;
private BigDecimal R49_NON_ACCRUALS2;
private BigDecimal R49_SPECIFIC_PROV2;
private BigDecimal R49_NO_OF_ACC2;
private BigDecimal R49_TOTAL_NON_ACCRUAL;
private BigDecimal R49_TOTAL_DUE_LOANS;
private BigDecimal R49_TOTAL_PERFORMING_LOAN;
private BigDecimal R49_VALUE_OF_COLLATERAL;
private BigDecimal R49_TOTAL_VALUE_NPL;
private BigDecimal R49_TOTAL_SPECIFIC_PROV;
private BigDecimal R49_SPECIFIC_PROV_NPL;

/* R50 */
private BigDecimal R50_30D_90D_PASTDUE;
private BigDecimal R50_NON_PERFORM_LOANS;
private BigDecimal R50_NON_ACCRUALS1;
private BigDecimal R50_SPECIFIC_PROV1;
private BigDecimal R50_NO_OF_ACC1;
private BigDecimal R50_90D_180D_PASTDUE;
private BigDecimal R50_NON_ACCRUALS2;
private BigDecimal R50_SPECIFIC_PROV2;
private BigDecimal R50_NO_OF_ACC2;
private BigDecimal R50_180D_ABOVE_PASTDUE;
private BigDecimal R50_NON_ACCRUALS3;
private BigDecimal R50_SPECIFIC_PROV3;
private BigDecimal R50_NO_OF_ACC3;
private BigDecimal R50_TOTAL_NON_ACCRUAL;
private BigDecimal R50_TOTAL_DUE_LOANS;
private BigDecimal R50_TOTAL_PERFORMING_LOAN;
private BigDecimal R50_VALUE_OF_COLLATERAL;
private BigDecimal R50_TOTAL_VALUE_NPL;
private BigDecimal R50_TOTAL_SPECIFIC_PROV;
private BigDecimal R50_SPECIFIC_PROV_NPL;

/* R51 */
private BigDecimal R51_30D_90D_PASTDUE;
private BigDecimal R51_NON_PERFORM_LOANS;
private BigDecimal R51_NON_ACCRUALS1;
private BigDecimal R51_SPECIFIC_PROV1;
private BigDecimal R51_NO_OF_ACC1;
private BigDecimal R51_90D_180D_PASTDUE;
private BigDecimal R51_NON_ACCRUALS2;
private BigDecimal R51_SPECIFIC_PROV2;
private BigDecimal R51_NO_OF_ACC2;
private BigDecimal R51_TOTAL_NON_ACCRUAL;
private BigDecimal R51_TOTAL_DUE_LOANS;
private BigDecimal R51_TOTAL_PERFORMING_LOAN;
private BigDecimal R51_VALUE_OF_COLLATERAL;
private BigDecimal R51_TOTAL_VALUE_NPL;
private BigDecimal R51_TOTAL_SPECIFIC_PROV;
private BigDecimal R51_SPECIFIC_PROV_NPL;

/* R52 */
private BigDecimal R52_30D_90D_PASTDUE;
private BigDecimal R52_NON_PERFORM_LOANS;
private BigDecimal R52_NON_ACCRUALS1;
private BigDecimal R52_SPECIFIC_PROV1;
private BigDecimal R52_NO_OF_ACC1;
private BigDecimal R52_90D_180D_PASTDUE;
private BigDecimal R52_NON_ACCRUALS2;
private BigDecimal R52_SPECIFIC_PROV2;
private BigDecimal R52_NO_OF_ACC2;
private BigDecimal R52_TOTAL_NON_ACCRUAL;
private BigDecimal R52_TOTAL_DUE_LOANS;
private BigDecimal R52_TOTAL_PERFORMING_LOAN;
private BigDecimal R52_VALUE_OF_COLLATERAL;
private BigDecimal R52_TOTAL_VALUE_NPL;
private BigDecimal R52_TOTAL_SPECIFIC_PROV;
private BigDecimal R52_SPECIFIC_PROV_NPL;

/* R53 */
private BigDecimal R53_30D_90D_PASTDUE;
private BigDecimal R53_NON_PERFORM_LOANS;
private BigDecimal R53_NON_ACCRUALS1;
private BigDecimal R53_SPECIFIC_PROV1;
private BigDecimal R53_NO_OF_ACC1;
private BigDecimal R53_90D_180D_PASTDUE;
private BigDecimal R53_NON_ACCRUALS2;
private BigDecimal R53_SPECIFIC_PROV2;
private BigDecimal R53_NO_OF_ACC2;
private BigDecimal R53_TOTAL_NON_ACCRUAL;
private BigDecimal R53_TOTAL_DUE_LOANS;
private BigDecimal R53_TOTAL_PERFORMING_LOAN;
private BigDecimal R53_VALUE_OF_COLLATERAL;
private BigDecimal R53_TOTAL_VALUE_NPL;
private BigDecimal R53_TOTAL_SPECIFIC_PROV;
private BigDecimal R53_SPECIFIC_PROV_NPL;

/* R54 */
private BigDecimal R54_30D_90D_PASTDUE;
private BigDecimal R54_NON_PERFORM_LOANS;
private BigDecimal R54_NON_ACCRUALS1;
private BigDecimal R54_SPECIFIC_PROV1;
private BigDecimal R54_NO_OF_ACC1;
private BigDecimal R54_90D_180D_PASTDUE;
private BigDecimal R54_NON_ACCRUALS2;
private BigDecimal R54_SPECIFIC_PROV2;
private BigDecimal R54_NO_OF_ACC2;
private BigDecimal R54_180D_ABOVE_PASTDUE;
private BigDecimal R54_NON_ACCRUALS3;
private BigDecimal R54_SPECIFIC_PROV3;
private BigDecimal R54_NO_OF_ACC3;
private BigDecimal R54_TOTAL_NON_ACCRUAL;
private BigDecimal R54_TOTAL_DUE_LOANS;
private BigDecimal R54_TOTAL_PERFORMING_LOAN;
private BigDecimal R54_VALUE_OF_COLLATERAL;
private BigDecimal R54_TOTAL_VALUE_NPL;
private BigDecimal R54_TOTAL_SPECIFIC_PROV;
private BigDecimal R54_SPECIFIC_PROV_NPL;

/* R55 */
private BigDecimal R55_30D_90D_PASTDUE;
private BigDecimal R55_NON_PERFORM_LOANS;
private BigDecimal R55_NON_ACCRUALS1;
private BigDecimal R55_SPECIFIC_PROV1;
private BigDecimal R55_NO_OF_ACC1;
private BigDecimal R55_90D_180D_PASTDUE;
private BigDecimal R55_NON_ACCRUALS2;
private BigDecimal R55_SPECIFIC_PROV2;
private BigDecimal R55_NO_OF_ACC2;
private BigDecimal R55_TOTAL_NON_ACCRUAL;
private BigDecimal R55_TOTAL_DUE_LOANS;
private BigDecimal R55_TOTAL_PERFORMING_LOAN;
private BigDecimal R55_VALUE_OF_COLLATERAL;
private BigDecimal R55_TOTAL_VALUE_NPL;
private BigDecimal R55_TOTAL_SPECIFIC_PROV;
private BigDecimal R55_SPECIFIC_PROV_NPL;

/* R56 */
private BigDecimal R56_30D_90D_PASTDUE;
private BigDecimal R56_NON_PERFORM_LOANS;
private BigDecimal R56_NON_ACCRUALS1;
private BigDecimal R56_SPECIFIC_PROV1;
private BigDecimal R56_NO_OF_ACC1;
private BigDecimal R56_90D_180D_PASTDUE;
private BigDecimal R56_NON_ACCRUALS2;
private BigDecimal R56_SPECIFIC_PROV2;
private BigDecimal R56_NO_OF_ACC2;
private BigDecimal R56_TOTAL_NON_ACCRUAL;
private BigDecimal R56_TOTAL_DUE_LOANS;
private BigDecimal R56_TOTAL_PERFORMING_LOAN;
private BigDecimal R56_VALUE_OF_COLLATERAL;
private BigDecimal R56_TOTAL_VALUE_NPL;
private BigDecimal R56_TOTAL_SPECIFIC_PROV;
private BigDecimal R56_SPECIFIC_PROV_NPL;

/* R57 */
private BigDecimal R57_30D_90D_PASTDUE;
private BigDecimal R57_NON_PERFORM_LOANS;
private BigDecimal R57_NON_ACCRUALS1;
private BigDecimal R57_SPECIFIC_PROV1;
private BigDecimal R57_NO_OF_ACC1;
private BigDecimal R57_90D_180D_PASTDUE;
private BigDecimal R57_NON_ACCRUALS2;
private BigDecimal R57_SPECIFIC_PROV2;
private BigDecimal R57_NO_OF_ACC2;
private BigDecimal R57_TOTAL_NON_ACCRUAL;
private BigDecimal R57_TOTAL_DUE_LOANS;
private BigDecimal R57_TOTAL_PERFORMING_LOAN;
private BigDecimal R57_VALUE_OF_COLLATERAL;
private BigDecimal R57_TOTAL_VALUE_NPL;
private BigDecimal R57_TOTAL_SPECIFIC_PROV;
private BigDecimal R57_SPECIFIC_PROV_NPL;

/* R58 */
private BigDecimal R58_30D_90D_PASTDUE;
private BigDecimal R58_NON_PERFORM_LOANS;
private BigDecimal R58_NON_ACCRUALS1;
private BigDecimal R58_SPECIFIC_PROV1;
private BigDecimal R58_NO_OF_ACC1;
private BigDecimal R58_90D_180D_PASTDUE;
private BigDecimal R58_NON_ACCRUALS2;
private BigDecimal R58_SPECIFIC_PROV2;
private BigDecimal R58_NO_OF_ACC2;
private BigDecimal R58_TOTAL_NON_ACCRUAL;
private BigDecimal R58_TOTAL_DUE_LOANS;
private BigDecimal R58_TOTAL_PERFORMING_LOAN;
private BigDecimal R58_VALUE_OF_COLLATERAL;
private BigDecimal R58_TOTAL_VALUE_NPL;
private BigDecimal R58_TOTAL_SPECIFIC_PROV;
private BigDecimal R58_SPECIFIC_PROV_NPL;

/* R59 */
private BigDecimal R59_30D_90D_PASTDUE;
private BigDecimal R59_NON_PERFORM_LOANS;
private BigDecimal R59_NON_ACCRUALS1;
private BigDecimal R59_SPECIFIC_PROV1;
private BigDecimal R59_NO_OF_ACC1;
private BigDecimal R59_90D_180D_PASTDUE;
private BigDecimal R59_NON_ACCRUALS2;
private BigDecimal R59_SPECIFIC_PROV2;
private BigDecimal R59_NO_OF_ACC2;
private BigDecimal R59_TOTAL_NON_ACCRUAL;
private BigDecimal R59_TOTAL_DUE_LOANS;
private BigDecimal R59_TOTAL_PERFORMING_LOAN;
private BigDecimal R59_VALUE_OF_COLLATERAL;
private BigDecimal R59_TOTAL_VALUE_NPL;
private BigDecimal R59_TOTAL_SPECIFIC_PROV;
private BigDecimal R59_SPECIFIC_PROV_NPL;

/* R60 */
private BigDecimal R60_30D_90D_PASTDUE;
private BigDecimal R60_NON_PERFORM_LOANS;
private BigDecimal R60_NON_ACCRUALS1;
private BigDecimal R60_SPECIFIC_PROV1;
private BigDecimal R60_NO_OF_ACC1;
private BigDecimal R60_90D_180D_PASTDUE;
private BigDecimal R60_NON_ACCRUALS2;
private BigDecimal R60_SPECIFIC_PROV2;
private BigDecimal R60_NO_OF_ACC2;
private BigDecimal R60_TOTAL_NON_ACCRUAL;
private BigDecimal R60_TOTAL_DUE_LOANS;
private BigDecimal R60_TOTAL_PERFORMING_LOAN;
private BigDecimal R60_VALUE_OF_COLLATERAL;
private BigDecimal R60_TOTAL_VALUE_NPL;
private BigDecimal R60_TOTAL_SPECIFIC_PROV;
private BigDecimal R60_SPECIFIC_PROV_NPL;

/* R61 */
private BigDecimal R61_30D_90D_PASTDUE;
private BigDecimal R61_NON_PERFORM_LOANS;
private BigDecimal R61_NON_ACCRUALS1;
private BigDecimal R61_SPECIFIC_PROV1;
private BigDecimal R61_NO_OF_ACC1;
private BigDecimal R61_90D_180D_PASTDUE;
private BigDecimal R61_NON_ACCRUALS2;
private BigDecimal R61_SPECIFIC_PROV2;
private BigDecimal R61_NO_OF_ACC2;
private BigDecimal R61_180D_ABOVE_PASTDUE;
private BigDecimal R61_NON_ACCRUALS3;
private BigDecimal R61_SPECIFIC_PROV3;
private BigDecimal R61_NO_OF_ACC3;
private BigDecimal R61_TOTAL_NON_ACCRUAL;
private BigDecimal R61_TOTAL_DUE_LOANS;
private BigDecimal R61_TOTAL_PERFORMING_LOAN;
private BigDecimal R61_VALUE_OF_COLLATERAL;
private BigDecimal R61_TOTAL_VALUE_NPL;
private BigDecimal R61_TOTAL_SPECIFIC_PROV;
private BigDecimal R61_SPECIFIC_PROV_NPL;



public Date getReport_date() {
	return report_date;
}



public void setReport_date(Date report_date) {
	this.report_date = report_date;
}



public String getReport_version() {
	return report_version;
}



public void setReport_version(String report_version) {
	this.report_version = report_version;
}



public String getReport_frequency() {
	return report_frequency;
}



public void setReport_frequency(String report_frequency) {
	this.report_frequency = report_frequency;
}



public String getReport_code() {
	return report_code;
}



public void setReport_code(String report_code) {
	this.report_code = report_code;
}



public String getReport_desc() {
	return report_desc;
}



public void setReport_desc(String report_desc) {
	this.report_desc = report_desc;
}



public String getEntity_flg() {
	return entity_flg;
}



public void setEntity_flg(String entity_flg) {
	this.entity_flg = entity_flg;
}



public String getModify_flg() {
	return modify_flg;
}



public void setModify_flg(String modify_flg) {
	this.modify_flg = modify_flg;
}



public String getDel_flg() {
	return del_flg;
}



public void setDel_flg(String del_flg) {
	this.del_flg = del_flg;
}



public BigDecimal getR8_30D_90D_PASTDUE() {
	return R8_30D_90D_PASTDUE;
}



public void setR8_30D_90D_PASTDUE(BigDecimal r8_30d_90d_PASTDUE) {
	R8_30D_90D_PASTDUE = r8_30d_90d_PASTDUE;
}



public BigDecimal getR8_NON_PERFORM_LOANS() {
	return R8_NON_PERFORM_LOANS;
}



public void setR8_NON_PERFORM_LOANS(BigDecimal r8_NON_PERFORM_LOANS) {
	R8_NON_PERFORM_LOANS = r8_NON_PERFORM_LOANS;
}



public BigDecimal getR8_NON_ACCRUALS1() {
	return R8_NON_ACCRUALS1;
}



public void setR8_NON_ACCRUALS1(BigDecimal r8_NON_ACCRUALS1) {
	R8_NON_ACCRUALS1 = r8_NON_ACCRUALS1;
}



public BigDecimal getR8_SPECIFIC_PROV1() {
	return R8_SPECIFIC_PROV1;
}



public void setR8_SPECIFIC_PROV1(BigDecimal r8_SPECIFIC_PROV1) {
	R8_SPECIFIC_PROV1 = r8_SPECIFIC_PROV1;
}



public BigDecimal getR8_NO_OF_ACC1() {
	return R8_NO_OF_ACC1;
}



public void setR8_NO_OF_ACC1(BigDecimal r8_NO_OF_ACC1) {
	R8_NO_OF_ACC1 = r8_NO_OF_ACC1;
}



public BigDecimal getR8_90D_180D_PASTDUE() {
	return R8_90D_180D_PASTDUE;
}



public void setR8_90D_180D_PASTDUE(BigDecimal r8_90d_180d_PASTDUE) {
	R8_90D_180D_PASTDUE = r8_90d_180d_PASTDUE;
}



public BigDecimal getR8_NON_ACCRUALS2() {
	return R8_NON_ACCRUALS2;
}



public void setR8_NON_ACCRUALS2(BigDecimal r8_NON_ACCRUALS2) {
	R8_NON_ACCRUALS2 = r8_NON_ACCRUALS2;
}



public BigDecimal getR8_SPECIFIC_PROV2() {
	return R8_SPECIFIC_PROV2;
}



public void setR8_SPECIFIC_PROV2(BigDecimal r8_SPECIFIC_PROV2) {
	R8_SPECIFIC_PROV2 = r8_SPECIFIC_PROV2;
}



public BigDecimal getR8_NO_OF_ACC2() {
	return R8_NO_OF_ACC2;
}



public void setR8_NO_OF_ACC2(BigDecimal r8_NO_OF_ACC2) {
	R8_NO_OF_ACC2 = r8_NO_OF_ACC2;
}



public BigDecimal getR8_180D_ABOVE_PASTDUE() {
	return R8_180D_ABOVE_PASTDUE;
}



public void setR8_180D_ABOVE_PASTDUE(BigDecimal r8_180d_ABOVE_PASTDUE) {
	R8_180D_ABOVE_PASTDUE = r8_180d_ABOVE_PASTDUE;
}



public BigDecimal getR8_NON_ACCRUALS3() {
	return R8_NON_ACCRUALS3;
}



public void setR8_NON_ACCRUALS3(BigDecimal r8_NON_ACCRUALS3) {
	R8_NON_ACCRUALS3 = r8_NON_ACCRUALS3;
}



public BigDecimal getR8_SPECIFIC_PROV3() {
	return R8_SPECIFIC_PROV3;
}



public void setR8_SPECIFIC_PROV3(BigDecimal r8_SPECIFIC_PROV3) {
	R8_SPECIFIC_PROV3 = r8_SPECIFIC_PROV3;
}



public BigDecimal getR8_NO_OF_ACC3() {
	return R8_NO_OF_ACC3;
}



public void setR8_NO_OF_ACC3(BigDecimal r8_NO_OF_ACC3) {
	R8_NO_OF_ACC3 = r8_NO_OF_ACC3;
}



public BigDecimal getR8_TOTAL_NON_ACCRUAL() {
	return R8_TOTAL_NON_ACCRUAL;
}



public void setR8_TOTAL_NON_ACCRUAL(BigDecimal r8_TOTAL_NON_ACCRUAL) {
	R8_TOTAL_NON_ACCRUAL = r8_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR8_TOTAL_DUE_LOANS() {
	return R8_TOTAL_DUE_LOANS;
}



public void setR8_TOTAL_DUE_LOANS(BigDecimal r8_TOTAL_DUE_LOANS) {
	R8_TOTAL_DUE_LOANS = r8_TOTAL_DUE_LOANS;
}



public BigDecimal getR8_TOTAL_PERFORMING_LOAN() {
	return R8_TOTAL_PERFORMING_LOAN;
}



public void setR8_TOTAL_PERFORMING_LOAN(BigDecimal r8_TOTAL_PERFORMING_LOAN) {
	R8_TOTAL_PERFORMING_LOAN = r8_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR8_VALUE_OF_COLLATERAL() {
	return R8_VALUE_OF_COLLATERAL;
}



public void setR8_VALUE_OF_COLLATERAL(BigDecimal r8_VALUE_OF_COLLATERAL) {
	R8_VALUE_OF_COLLATERAL = r8_VALUE_OF_COLLATERAL;
}



public BigDecimal getR8_TOTAL_VALUE_NPL() {
	return R8_TOTAL_VALUE_NPL;
}



public void setR8_TOTAL_VALUE_NPL(BigDecimal r8_TOTAL_VALUE_NPL) {
	R8_TOTAL_VALUE_NPL = r8_TOTAL_VALUE_NPL;
}



public BigDecimal getR8_TOTAL_SPECIFIC_PROV() {
	return R8_TOTAL_SPECIFIC_PROV;
}



public void setR8_TOTAL_SPECIFIC_PROV(BigDecimal r8_TOTAL_SPECIFIC_PROV) {
	R8_TOTAL_SPECIFIC_PROV = r8_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR8_SPECIFIC_PROV_NPL() {
	return R8_SPECIFIC_PROV_NPL;
}



public void setR8_SPECIFIC_PROV_NPL(BigDecimal r8_SPECIFIC_PROV_NPL) {
	R8_SPECIFIC_PROV_NPL = r8_SPECIFIC_PROV_NPL;
}



public BigDecimal getR9_30D_90D_PASTDUE() {
	return R9_30D_90D_PASTDUE;
}



public void setR9_30D_90D_PASTDUE(BigDecimal r9_30d_90d_PASTDUE) {
	R9_30D_90D_PASTDUE = r9_30d_90d_PASTDUE;
}



public BigDecimal getR9_NON_PERFORM_LOANS() {
	return R9_NON_PERFORM_LOANS;
}



public void setR9_NON_PERFORM_LOANS(BigDecimal r9_NON_PERFORM_LOANS) {
	R9_NON_PERFORM_LOANS = r9_NON_PERFORM_LOANS;
}



public BigDecimal getR9_NON_ACCRUALS1() {
	return R9_NON_ACCRUALS1;
}



public void setR9_NON_ACCRUALS1(BigDecimal r9_NON_ACCRUALS1) {
	R9_NON_ACCRUALS1 = r9_NON_ACCRUALS1;
}



public BigDecimal getR9_SPECIFIC_PROV1() {
	return R9_SPECIFIC_PROV1;
}



public void setR9_SPECIFIC_PROV1(BigDecimal r9_SPECIFIC_PROV1) {
	R9_SPECIFIC_PROV1 = r9_SPECIFIC_PROV1;
}



public BigDecimal getR9_NO_OF_ACC1() {
	return R9_NO_OF_ACC1;
}



public void setR9_NO_OF_ACC1(BigDecimal r9_NO_OF_ACC1) {
	R9_NO_OF_ACC1 = r9_NO_OF_ACC1;
}



public BigDecimal getR9_90D_180D_PASTDUE() {
	return R9_90D_180D_PASTDUE;
}



public void setR9_90D_180D_PASTDUE(BigDecimal r9_90d_180d_PASTDUE) {
	R9_90D_180D_PASTDUE = r9_90d_180d_PASTDUE;
}



public BigDecimal getR9_NON_ACCRUALS2() {
	return R9_NON_ACCRUALS2;
}



public void setR9_NON_ACCRUALS2(BigDecimal r9_NON_ACCRUALS2) {
	R9_NON_ACCRUALS2 = r9_NON_ACCRUALS2;
}



public BigDecimal getR9_SPECIFIC_PROV2() {
	return R9_SPECIFIC_PROV2;
}



public void setR9_SPECIFIC_PROV2(BigDecimal r9_SPECIFIC_PROV2) {
	R9_SPECIFIC_PROV2 = r9_SPECIFIC_PROV2;
}



public BigDecimal getR9_NO_OF_ACC2() {
	return R9_NO_OF_ACC2;
}



public void setR9_NO_OF_ACC2(BigDecimal r9_NO_OF_ACC2) {
	R9_NO_OF_ACC2 = r9_NO_OF_ACC2;
}



public BigDecimal getR9_TOTAL_NON_ACCRUAL() {
	return R9_TOTAL_NON_ACCRUAL;
}



public void setR9_TOTAL_NON_ACCRUAL(BigDecimal r9_TOTAL_NON_ACCRUAL) {
	R9_TOTAL_NON_ACCRUAL = r9_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR9_TOTAL_DUE_LOANS() {
	return R9_TOTAL_DUE_LOANS;
}



public void setR9_TOTAL_DUE_LOANS(BigDecimal r9_TOTAL_DUE_LOANS) {
	R9_TOTAL_DUE_LOANS = r9_TOTAL_DUE_LOANS;
}



public BigDecimal getR9_TOTAL_PERFORMING_LOAN() {
	return R9_TOTAL_PERFORMING_LOAN;
}



public void setR9_TOTAL_PERFORMING_LOAN(BigDecimal r9_TOTAL_PERFORMING_LOAN) {
	R9_TOTAL_PERFORMING_LOAN = r9_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR9_VALUE_OF_COLLATERAL() {
	return R9_VALUE_OF_COLLATERAL;
}



public void setR9_VALUE_OF_COLLATERAL(BigDecimal r9_VALUE_OF_COLLATERAL) {
	R9_VALUE_OF_COLLATERAL = r9_VALUE_OF_COLLATERAL;
}



public BigDecimal getR9_TOTAL_VALUE_NPL() {
	return R9_TOTAL_VALUE_NPL;
}



public void setR9_TOTAL_VALUE_NPL(BigDecimal r9_TOTAL_VALUE_NPL) {
	R9_TOTAL_VALUE_NPL = r9_TOTAL_VALUE_NPL;
}



public BigDecimal getR9_TOTAL_SPECIFIC_PROV() {
	return R9_TOTAL_SPECIFIC_PROV;
}



public void setR9_TOTAL_SPECIFIC_PROV(BigDecimal r9_TOTAL_SPECIFIC_PROV) {
	R9_TOTAL_SPECIFIC_PROV = r9_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR9_SPECIFIC_PROV_NPL() {
	return R9_SPECIFIC_PROV_NPL;
}



public void setR9_SPECIFIC_PROV_NPL(BigDecimal r9_SPECIFIC_PROV_NPL) {
	R9_SPECIFIC_PROV_NPL = r9_SPECIFIC_PROV_NPL;
}



public BigDecimal getR10_30D_90D_PASTDUE() {
	return R10_30D_90D_PASTDUE;
}



public void setR10_30D_90D_PASTDUE(BigDecimal r10_30d_90d_PASTDUE) {
	R10_30D_90D_PASTDUE = r10_30d_90d_PASTDUE;
}



public BigDecimal getR10_NON_PERFORM_LOANS() {
	return R10_NON_PERFORM_LOANS;
}



public void setR10_NON_PERFORM_LOANS(BigDecimal r10_NON_PERFORM_LOANS) {
	R10_NON_PERFORM_LOANS = r10_NON_PERFORM_LOANS;
}



public BigDecimal getR10_NON_ACCRUALS1() {
	return R10_NON_ACCRUALS1;
}



public void setR10_NON_ACCRUALS1(BigDecimal r10_NON_ACCRUALS1) {
	R10_NON_ACCRUALS1 = r10_NON_ACCRUALS1;
}



public BigDecimal getR10_SPECIFIC_PROV1() {
	return R10_SPECIFIC_PROV1;
}



public void setR10_SPECIFIC_PROV1(BigDecimal r10_SPECIFIC_PROV1) {
	R10_SPECIFIC_PROV1 = r10_SPECIFIC_PROV1;
}



public BigDecimal getR10_NO_OF_ACC1() {
	return R10_NO_OF_ACC1;
}



public void setR10_NO_OF_ACC1(BigDecimal r10_NO_OF_ACC1) {
	R10_NO_OF_ACC1 = r10_NO_OF_ACC1;
}



public BigDecimal getR10_90D_180D_PASTDUE() {
	return R10_90D_180D_PASTDUE;
}



public void setR10_90D_180D_PASTDUE(BigDecimal r10_90d_180d_PASTDUE) {
	R10_90D_180D_PASTDUE = r10_90d_180d_PASTDUE;
}



public BigDecimal getR10_NON_ACCRUALS2() {
	return R10_NON_ACCRUALS2;
}



public void setR10_NON_ACCRUALS2(BigDecimal r10_NON_ACCRUALS2) {
	R10_NON_ACCRUALS2 = r10_NON_ACCRUALS2;
}



public BigDecimal getR10_SPECIFIC_PROV2() {
	return R10_SPECIFIC_PROV2;
}



public void setR10_SPECIFIC_PROV2(BigDecimal r10_SPECIFIC_PROV2) {
	R10_SPECIFIC_PROV2 = r10_SPECIFIC_PROV2;
}



public BigDecimal getR10_NO_OF_ACC2() {
	return R10_NO_OF_ACC2;
}



public void setR10_NO_OF_ACC2(BigDecimal r10_NO_OF_ACC2) {
	R10_NO_OF_ACC2 = r10_NO_OF_ACC2;
}



public BigDecimal getR10_TOTAL_NON_ACCRUAL() {
	return R10_TOTAL_NON_ACCRUAL;
}



public void setR10_TOTAL_NON_ACCRUAL(BigDecimal r10_TOTAL_NON_ACCRUAL) {
	R10_TOTAL_NON_ACCRUAL = r10_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR10_TOTAL_DUE_LOANS() {
	return R10_TOTAL_DUE_LOANS;
}



public void setR10_TOTAL_DUE_LOANS(BigDecimal r10_TOTAL_DUE_LOANS) {
	R10_TOTAL_DUE_LOANS = r10_TOTAL_DUE_LOANS;
}



public BigDecimal getR10_TOTAL_PERFORMING_LOAN() {
	return R10_TOTAL_PERFORMING_LOAN;
}



public void setR10_TOTAL_PERFORMING_LOAN(BigDecimal r10_TOTAL_PERFORMING_LOAN) {
	R10_TOTAL_PERFORMING_LOAN = r10_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR10_VALUE_OF_COLLATERAL() {
	return R10_VALUE_OF_COLLATERAL;
}



public void setR10_VALUE_OF_COLLATERAL(BigDecimal r10_VALUE_OF_COLLATERAL) {
	R10_VALUE_OF_COLLATERAL = r10_VALUE_OF_COLLATERAL;
}



public BigDecimal getR10_TOTAL_VALUE_NPL() {
	return R10_TOTAL_VALUE_NPL;
}



public void setR10_TOTAL_VALUE_NPL(BigDecimal r10_TOTAL_VALUE_NPL) {
	R10_TOTAL_VALUE_NPL = r10_TOTAL_VALUE_NPL;
}



public BigDecimal getR10_TOTAL_SPECIFIC_PROV() {
	return R10_TOTAL_SPECIFIC_PROV;
}



public void setR10_TOTAL_SPECIFIC_PROV(BigDecimal r10_TOTAL_SPECIFIC_PROV) {
	R10_TOTAL_SPECIFIC_PROV = r10_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR10_SPECIFIC_PROV_NPL() {
	return R10_SPECIFIC_PROV_NPL;
}



public void setR10_SPECIFIC_PROV_NPL(BigDecimal r10_SPECIFIC_PROV_NPL) {
	R10_SPECIFIC_PROV_NPL = r10_SPECIFIC_PROV_NPL;
}



public BigDecimal getR11_30D_90D_PASTDUE() {
	return R11_30D_90D_PASTDUE;
}



public void setR11_30D_90D_PASTDUE(BigDecimal r11_30d_90d_PASTDUE) {
	R11_30D_90D_PASTDUE = r11_30d_90d_PASTDUE;
}



public BigDecimal getR11_NON_PERFORM_LOANS() {
	return R11_NON_PERFORM_LOANS;
}



public void setR11_NON_PERFORM_LOANS(BigDecimal r11_NON_PERFORM_LOANS) {
	R11_NON_PERFORM_LOANS = r11_NON_PERFORM_LOANS;
}



public BigDecimal getR11_NON_ACCRUALS1() {
	return R11_NON_ACCRUALS1;
}



public void setR11_NON_ACCRUALS1(BigDecimal r11_NON_ACCRUALS1) {
	R11_NON_ACCRUALS1 = r11_NON_ACCRUALS1;
}



public BigDecimal getR11_SPECIFIC_PROV1() {
	return R11_SPECIFIC_PROV1;
}



public void setR11_SPECIFIC_PROV1(BigDecimal r11_SPECIFIC_PROV1) {
	R11_SPECIFIC_PROV1 = r11_SPECIFIC_PROV1;
}



public BigDecimal getR11_NO_OF_ACC1() {
	return R11_NO_OF_ACC1;
}



public void setR11_NO_OF_ACC1(BigDecimal r11_NO_OF_ACC1) {
	R11_NO_OF_ACC1 = r11_NO_OF_ACC1;
}



public BigDecimal getR11_90D_180D_PASTDUE() {
	return R11_90D_180D_PASTDUE;
}



public void setR11_90D_180D_PASTDUE(BigDecimal r11_90d_180d_PASTDUE) {
	R11_90D_180D_PASTDUE = r11_90d_180d_PASTDUE;
}



public BigDecimal getR11_NON_ACCRUALS2() {
	return R11_NON_ACCRUALS2;
}



public void setR11_NON_ACCRUALS2(BigDecimal r11_NON_ACCRUALS2) {
	R11_NON_ACCRUALS2 = r11_NON_ACCRUALS2;
}



public BigDecimal getR11_SPECIFIC_PROV2() {
	return R11_SPECIFIC_PROV2;
}



public void setR11_SPECIFIC_PROV2(BigDecimal r11_SPECIFIC_PROV2) {
	R11_SPECIFIC_PROV2 = r11_SPECIFIC_PROV2;
}



public BigDecimal getR11_NO_OF_ACC2() {
	return R11_NO_OF_ACC2;
}



public void setR11_NO_OF_ACC2(BigDecimal r11_NO_OF_ACC2) {
	R11_NO_OF_ACC2 = r11_NO_OF_ACC2;
}



public BigDecimal getR11_TOTAL_NON_ACCRUAL() {
	return R11_TOTAL_NON_ACCRUAL;
}



public void setR11_TOTAL_NON_ACCRUAL(BigDecimal r11_TOTAL_NON_ACCRUAL) {
	R11_TOTAL_NON_ACCRUAL = r11_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR11_TOTAL_DUE_LOANS() {
	return R11_TOTAL_DUE_LOANS;
}



public void setR11_TOTAL_DUE_LOANS(BigDecimal r11_TOTAL_DUE_LOANS) {
	R11_TOTAL_DUE_LOANS = r11_TOTAL_DUE_LOANS;
}



public BigDecimal getR11_TOTAL_PERFORMING_LOAN() {
	return R11_TOTAL_PERFORMING_LOAN;
}



public void setR11_TOTAL_PERFORMING_LOAN(BigDecimal r11_TOTAL_PERFORMING_LOAN) {
	R11_TOTAL_PERFORMING_LOAN = r11_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR11_VALUE_OF_COLLATERAL() {
	return R11_VALUE_OF_COLLATERAL;
}



public void setR11_VALUE_OF_COLLATERAL(BigDecimal r11_VALUE_OF_COLLATERAL) {
	R11_VALUE_OF_COLLATERAL = r11_VALUE_OF_COLLATERAL;
}



public BigDecimal getR11_TOTAL_VALUE_NPL() {
	return R11_TOTAL_VALUE_NPL;
}



public void setR11_TOTAL_VALUE_NPL(BigDecimal r11_TOTAL_VALUE_NPL) {
	R11_TOTAL_VALUE_NPL = r11_TOTAL_VALUE_NPL;
}



public BigDecimal getR11_TOTAL_SPECIFIC_PROV() {
	return R11_TOTAL_SPECIFIC_PROV;
}



public void setR11_TOTAL_SPECIFIC_PROV(BigDecimal r11_TOTAL_SPECIFIC_PROV) {
	R11_TOTAL_SPECIFIC_PROV = r11_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR11_SPECIFIC_PROV_NPL() {
	return R11_SPECIFIC_PROV_NPL;
}



public void setR11_SPECIFIC_PROV_NPL(BigDecimal r11_SPECIFIC_PROV_NPL) {
	R11_SPECIFIC_PROV_NPL = r11_SPECIFIC_PROV_NPL;
}



public BigDecimal getR12_30D_90D_PASTDUE() {
	return R12_30D_90D_PASTDUE;
}



public void setR12_30D_90D_PASTDUE(BigDecimal r12_30d_90d_PASTDUE) {
	R12_30D_90D_PASTDUE = r12_30d_90d_PASTDUE;
}



public BigDecimal getR12_NON_PERFORM_LOANS() {
	return R12_NON_PERFORM_LOANS;
}



public void setR12_NON_PERFORM_LOANS(BigDecimal r12_NON_PERFORM_LOANS) {
	R12_NON_PERFORM_LOANS = r12_NON_PERFORM_LOANS;
}



public BigDecimal getR12_NON_ACCRUALS1() {
	return R12_NON_ACCRUALS1;
}



public void setR12_NON_ACCRUALS1(BigDecimal r12_NON_ACCRUALS1) {
	R12_NON_ACCRUALS1 = r12_NON_ACCRUALS1;
}



public BigDecimal getR12_SPECIFIC_PROV1() {
	return R12_SPECIFIC_PROV1;
}



public void setR12_SPECIFIC_PROV1(BigDecimal r12_SPECIFIC_PROV1) {
	R12_SPECIFIC_PROV1 = r12_SPECIFIC_PROV1;
}



public BigDecimal getR12_NO_OF_ACC1() {
	return R12_NO_OF_ACC1;
}



public void setR12_NO_OF_ACC1(BigDecimal r12_NO_OF_ACC1) {
	R12_NO_OF_ACC1 = r12_NO_OF_ACC1;
}



public BigDecimal getR12_90D_180D_PASTDUE() {
	return R12_90D_180D_PASTDUE;
}



public void setR12_90D_180D_PASTDUE(BigDecimal r12_90d_180d_PASTDUE) {
	R12_90D_180D_PASTDUE = r12_90d_180d_PASTDUE;
}



public BigDecimal getR12_NON_ACCRUALS2() {
	return R12_NON_ACCRUALS2;
}



public void setR12_NON_ACCRUALS2(BigDecimal r12_NON_ACCRUALS2) {
	R12_NON_ACCRUALS2 = r12_NON_ACCRUALS2;
}



public BigDecimal getR12_SPECIFIC_PROV2() {
	return R12_SPECIFIC_PROV2;
}



public void setR12_SPECIFIC_PROV2(BigDecimal r12_SPECIFIC_PROV2) {
	R12_SPECIFIC_PROV2 = r12_SPECIFIC_PROV2;
}



public BigDecimal getR12_NO_OF_ACC2() {
	return R12_NO_OF_ACC2;
}



public void setR12_NO_OF_ACC2(BigDecimal r12_NO_OF_ACC2) {
	R12_NO_OF_ACC2 = r12_NO_OF_ACC2;
}



public BigDecimal getR12_180D_ABOVE_PASTDUE() {
	return R12_180D_ABOVE_PASTDUE;
}



public void setR12_180D_ABOVE_PASTDUE(BigDecimal r12_180d_ABOVE_PASTDUE) {
	R12_180D_ABOVE_PASTDUE = r12_180d_ABOVE_PASTDUE;
}



public BigDecimal getR12_NON_ACCRUALS3() {
	return R12_NON_ACCRUALS3;
}



public void setR12_NON_ACCRUALS3(BigDecimal r12_NON_ACCRUALS3) {
	R12_NON_ACCRUALS3 = r12_NON_ACCRUALS3;
}



public BigDecimal getR12_SPECIFIC_PROV3() {
	return R12_SPECIFIC_PROV3;
}



public void setR12_SPECIFIC_PROV3(BigDecimal r12_SPECIFIC_PROV3) {
	R12_SPECIFIC_PROV3 = r12_SPECIFIC_PROV3;
}



public BigDecimal getR12_NO_OF_ACC3() {
	return R12_NO_OF_ACC3;
}



public void setR12_NO_OF_ACC3(BigDecimal r12_NO_OF_ACC3) {
	R12_NO_OF_ACC3 = r12_NO_OF_ACC3;
}



public BigDecimal getR12_TOTAL_NON_ACCRUAL() {
	return R12_TOTAL_NON_ACCRUAL;
}



public void setR12_TOTAL_NON_ACCRUAL(BigDecimal r12_TOTAL_NON_ACCRUAL) {
	R12_TOTAL_NON_ACCRUAL = r12_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR12_TOTAL_DUE_LOANS() {
	return R12_TOTAL_DUE_LOANS;
}



public void setR12_TOTAL_DUE_LOANS(BigDecimal r12_TOTAL_DUE_LOANS) {
	R12_TOTAL_DUE_LOANS = r12_TOTAL_DUE_LOANS;
}



public BigDecimal getR12_TOTAL_PERFORMING_LOAN() {
	return R12_TOTAL_PERFORMING_LOAN;
}



public void setR12_TOTAL_PERFORMING_LOAN(BigDecimal r12_TOTAL_PERFORMING_LOAN) {
	R12_TOTAL_PERFORMING_LOAN = r12_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR12_VALUE_OF_COLLATERAL() {
	return R12_VALUE_OF_COLLATERAL;
}



public void setR12_VALUE_OF_COLLATERAL(BigDecimal r12_VALUE_OF_COLLATERAL) {
	R12_VALUE_OF_COLLATERAL = r12_VALUE_OF_COLLATERAL;
}



public BigDecimal getR12_TOTAL_VALUE_NPL() {
	return R12_TOTAL_VALUE_NPL;
}



public void setR12_TOTAL_VALUE_NPL(BigDecimal r12_TOTAL_VALUE_NPL) {
	R12_TOTAL_VALUE_NPL = r12_TOTAL_VALUE_NPL;
}



public BigDecimal getR12_TOTAL_SPECIFIC_PROV() {
	return R12_TOTAL_SPECIFIC_PROV;
}



public void setR12_TOTAL_SPECIFIC_PROV(BigDecimal r12_TOTAL_SPECIFIC_PROV) {
	R12_TOTAL_SPECIFIC_PROV = r12_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR12_SPECIFIC_PROV_NPL() {
	return R12_SPECIFIC_PROV_NPL;
}



public void setR12_SPECIFIC_PROV_NPL(BigDecimal r12_SPECIFIC_PROV_NPL) {
	R12_SPECIFIC_PROV_NPL = r12_SPECIFIC_PROV_NPL;
}



public BigDecimal getR13_30D_90D_PASTDUE() {
	return R13_30D_90D_PASTDUE;
}



public void setR13_30D_90D_PASTDUE(BigDecimal r13_30d_90d_PASTDUE) {
	R13_30D_90D_PASTDUE = r13_30d_90d_PASTDUE;
}



public BigDecimal getR13_NON_PERFORM_LOANS() {
	return R13_NON_PERFORM_LOANS;
}



public void setR13_NON_PERFORM_LOANS(BigDecimal r13_NON_PERFORM_LOANS) {
	R13_NON_PERFORM_LOANS = r13_NON_PERFORM_LOANS;
}



public BigDecimal getR13_NON_ACCRUALS1() {
	return R13_NON_ACCRUALS1;
}



public void setR13_NON_ACCRUALS1(BigDecimal r13_NON_ACCRUALS1) {
	R13_NON_ACCRUALS1 = r13_NON_ACCRUALS1;
}



public BigDecimal getR13_SPECIFIC_PROV1() {
	return R13_SPECIFIC_PROV1;
}



public void setR13_SPECIFIC_PROV1(BigDecimal r13_SPECIFIC_PROV1) {
	R13_SPECIFIC_PROV1 = r13_SPECIFIC_PROV1;
}



public BigDecimal getR13_NO_OF_ACC1() {
	return R13_NO_OF_ACC1;
}



public void setR13_NO_OF_ACC1(BigDecimal r13_NO_OF_ACC1) {
	R13_NO_OF_ACC1 = r13_NO_OF_ACC1;
}



public BigDecimal getR13_90D_180D_PASTDUE() {
	return R13_90D_180D_PASTDUE;
}



public void setR13_90D_180D_PASTDUE(BigDecimal r13_90d_180d_PASTDUE) {
	R13_90D_180D_PASTDUE = r13_90d_180d_PASTDUE;
}



public BigDecimal getR13_NON_ACCRUALS2() {
	return R13_NON_ACCRUALS2;
}



public void setR13_NON_ACCRUALS2(BigDecimal r13_NON_ACCRUALS2) {
	R13_NON_ACCRUALS2 = r13_NON_ACCRUALS2;
}



public BigDecimal getR13_SPECIFIC_PROV2() {
	return R13_SPECIFIC_PROV2;
}



public void setR13_SPECIFIC_PROV2(BigDecimal r13_SPECIFIC_PROV2) {
	R13_SPECIFIC_PROV2 = r13_SPECIFIC_PROV2;
}



public BigDecimal getR13_NO_OF_ACC2() {
	return R13_NO_OF_ACC2;
}



public void setR13_NO_OF_ACC2(BigDecimal r13_NO_OF_ACC2) {
	R13_NO_OF_ACC2 = r13_NO_OF_ACC2;
}



public BigDecimal getR13_TOTAL_NON_ACCRUAL() {
	return R13_TOTAL_NON_ACCRUAL;
}



public void setR13_TOTAL_NON_ACCRUAL(BigDecimal r13_TOTAL_NON_ACCRUAL) {
	R13_TOTAL_NON_ACCRUAL = r13_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR13_TOTAL_DUE_LOANS() {
	return R13_TOTAL_DUE_LOANS;
}



public void setR13_TOTAL_DUE_LOANS(BigDecimal r13_TOTAL_DUE_LOANS) {
	R13_TOTAL_DUE_LOANS = r13_TOTAL_DUE_LOANS;
}



public BigDecimal getR13_TOTAL_PERFORMING_LOAN() {
	return R13_TOTAL_PERFORMING_LOAN;
}



public void setR13_TOTAL_PERFORMING_LOAN(BigDecimal r13_TOTAL_PERFORMING_LOAN) {
	R13_TOTAL_PERFORMING_LOAN = r13_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR13_VALUE_OF_COLLATERAL() {
	return R13_VALUE_OF_COLLATERAL;
}



public void setR13_VALUE_OF_COLLATERAL(BigDecimal r13_VALUE_OF_COLLATERAL) {
	R13_VALUE_OF_COLLATERAL = r13_VALUE_OF_COLLATERAL;
}



public BigDecimal getR13_TOTAL_VALUE_NPL() {
	return R13_TOTAL_VALUE_NPL;
}



public void setR13_TOTAL_VALUE_NPL(BigDecimal r13_TOTAL_VALUE_NPL) {
	R13_TOTAL_VALUE_NPL = r13_TOTAL_VALUE_NPL;
}



public BigDecimal getR13_TOTAL_SPECIFIC_PROV() {
	return R13_TOTAL_SPECIFIC_PROV;
}



public void setR13_TOTAL_SPECIFIC_PROV(BigDecimal r13_TOTAL_SPECIFIC_PROV) {
	R13_TOTAL_SPECIFIC_PROV = r13_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR13_SPECIFIC_PROV_NPL() {
	return R13_SPECIFIC_PROV_NPL;
}



public void setR13_SPECIFIC_PROV_NPL(BigDecimal r13_SPECIFIC_PROV_NPL) {
	R13_SPECIFIC_PROV_NPL = r13_SPECIFIC_PROV_NPL;
}



public BigDecimal getR14_30D_90D_PASTDUE() {
	return R14_30D_90D_PASTDUE;
}



public void setR14_30D_90D_PASTDUE(BigDecimal r14_30d_90d_PASTDUE) {
	R14_30D_90D_PASTDUE = r14_30d_90d_PASTDUE;
}



public BigDecimal getR14_NON_PERFORM_LOANS() {
	return R14_NON_PERFORM_LOANS;
}



public void setR14_NON_PERFORM_LOANS(BigDecimal r14_NON_PERFORM_LOANS) {
	R14_NON_PERFORM_LOANS = r14_NON_PERFORM_LOANS;
}



public BigDecimal getR14_NON_ACCRUALS1() {
	return R14_NON_ACCRUALS1;
}



public void setR14_NON_ACCRUALS1(BigDecimal r14_NON_ACCRUALS1) {
	R14_NON_ACCRUALS1 = r14_NON_ACCRUALS1;
}



public BigDecimal getR14_SPECIFIC_PROV1() {
	return R14_SPECIFIC_PROV1;
}



public void setR14_SPECIFIC_PROV1(BigDecimal r14_SPECIFIC_PROV1) {
	R14_SPECIFIC_PROV1 = r14_SPECIFIC_PROV1;
}



public BigDecimal getR14_NO_OF_ACC1() {
	return R14_NO_OF_ACC1;
}



public void setR14_NO_OF_ACC1(BigDecimal r14_NO_OF_ACC1) {
	R14_NO_OF_ACC1 = r14_NO_OF_ACC1;
}



public BigDecimal getR14_90D_180D_PASTDUE() {
	return R14_90D_180D_PASTDUE;
}



public void setR14_90D_180D_PASTDUE(BigDecimal r14_90d_180d_PASTDUE) {
	R14_90D_180D_PASTDUE = r14_90d_180d_PASTDUE;
}



public BigDecimal getR14_NON_ACCRUALS2() {
	return R14_NON_ACCRUALS2;
}



public void setR14_NON_ACCRUALS2(BigDecimal r14_NON_ACCRUALS2) {
	R14_NON_ACCRUALS2 = r14_NON_ACCRUALS2;
}



public BigDecimal getR14_SPECIFIC_PROV2() {
	return R14_SPECIFIC_PROV2;
}



public void setR14_SPECIFIC_PROV2(BigDecimal r14_SPECIFIC_PROV2) {
	R14_SPECIFIC_PROV2 = r14_SPECIFIC_PROV2;
}



public BigDecimal getR14_NO_OF_ACC2() {
	return R14_NO_OF_ACC2;
}



public void setR14_NO_OF_ACC2(BigDecimal r14_NO_OF_ACC2) {
	R14_NO_OF_ACC2 = r14_NO_OF_ACC2;
}



public BigDecimal getR14_TOTAL_NON_ACCRUAL() {
	return R14_TOTAL_NON_ACCRUAL;
}



public void setR14_TOTAL_NON_ACCRUAL(BigDecimal r14_TOTAL_NON_ACCRUAL) {
	R14_TOTAL_NON_ACCRUAL = r14_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR14_TOTAL_DUE_LOANS() {
	return R14_TOTAL_DUE_LOANS;
}



public void setR14_TOTAL_DUE_LOANS(BigDecimal r14_TOTAL_DUE_LOANS) {
	R14_TOTAL_DUE_LOANS = r14_TOTAL_DUE_LOANS;
}



public BigDecimal getR14_TOTAL_PERFORMING_LOAN() {
	return R14_TOTAL_PERFORMING_LOAN;
}



public void setR14_TOTAL_PERFORMING_LOAN(BigDecimal r14_TOTAL_PERFORMING_LOAN) {
	R14_TOTAL_PERFORMING_LOAN = r14_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR14_VALUE_OF_COLLATERAL() {
	return R14_VALUE_OF_COLLATERAL;
}



public void setR14_VALUE_OF_COLLATERAL(BigDecimal r14_VALUE_OF_COLLATERAL) {
	R14_VALUE_OF_COLLATERAL = r14_VALUE_OF_COLLATERAL;
}



public BigDecimal getR14_TOTAL_VALUE_NPL() {
	return R14_TOTAL_VALUE_NPL;
}



public void setR14_TOTAL_VALUE_NPL(BigDecimal r14_TOTAL_VALUE_NPL) {
	R14_TOTAL_VALUE_NPL = r14_TOTAL_VALUE_NPL;
}



public BigDecimal getR14_TOTAL_SPECIFIC_PROV() {
	return R14_TOTAL_SPECIFIC_PROV;
}



public void setR14_TOTAL_SPECIFIC_PROV(BigDecimal r14_TOTAL_SPECIFIC_PROV) {
	R14_TOTAL_SPECIFIC_PROV = r14_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR14_SPECIFIC_PROV_NPL() {
	return R14_SPECIFIC_PROV_NPL;
}



public void setR14_SPECIFIC_PROV_NPL(BigDecimal r14_SPECIFIC_PROV_NPL) {
	R14_SPECIFIC_PROV_NPL = r14_SPECIFIC_PROV_NPL;
}



public BigDecimal getR15_30D_90D_PASTDUE() {
	return R15_30D_90D_PASTDUE;
}



public void setR15_30D_90D_PASTDUE(BigDecimal r15_30d_90d_PASTDUE) {
	R15_30D_90D_PASTDUE = r15_30d_90d_PASTDUE;
}



public BigDecimal getR15_NON_PERFORM_LOANS() {
	return R15_NON_PERFORM_LOANS;
}



public void setR15_NON_PERFORM_LOANS(BigDecimal r15_NON_PERFORM_LOANS) {
	R15_NON_PERFORM_LOANS = r15_NON_PERFORM_LOANS;
}



public BigDecimal getR15_NON_ACCRUALS1() {
	return R15_NON_ACCRUALS1;
}



public void setR15_NON_ACCRUALS1(BigDecimal r15_NON_ACCRUALS1) {
	R15_NON_ACCRUALS1 = r15_NON_ACCRUALS1;
}



public BigDecimal getR15_SPECIFIC_PROV1() {
	return R15_SPECIFIC_PROV1;
}



public void setR15_SPECIFIC_PROV1(BigDecimal r15_SPECIFIC_PROV1) {
	R15_SPECIFIC_PROV1 = r15_SPECIFIC_PROV1;
}



public BigDecimal getR15_NO_OF_ACC1() {
	return R15_NO_OF_ACC1;
}



public void setR15_NO_OF_ACC1(BigDecimal r15_NO_OF_ACC1) {
	R15_NO_OF_ACC1 = r15_NO_OF_ACC1;
}



public BigDecimal getR15_90D_180D_PASTDUE() {
	return R15_90D_180D_PASTDUE;
}



public void setR15_90D_180D_PASTDUE(BigDecimal r15_90d_180d_PASTDUE) {
	R15_90D_180D_PASTDUE = r15_90d_180d_PASTDUE;
}



public BigDecimal getR15_NON_ACCRUALS2() {
	return R15_NON_ACCRUALS2;
}



public void setR15_NON_ACCRUALS2(BigDecimal r15_NON_ACCRUALS2) {
	R15_NON_ACCRUALS2 = r15_NON_ACCRUALS2;
}



public BigDecimal getR15_SPECIFIC_PROV2() {
	return R15_SPECIFIC_PROV2;
}



public void setR15_SPECIFIC_PROV2(BigDecimal r15_SPECIFIC_PROV2) {
	R15_SPECIFIC_PROV2 = r15_SPECIFIC_PROV2;
}



public BigDecimal getR15_NO_OF_ACC2() {
	return R15_NO_OF_ACC2;
}



public void setR15_NO_OF_ACC2(BigDecimal r15_NO_OF_ACC2) {
	R15_NO_OF_ACC2 = r15_NO_OF_ACC2;
}



public BigDecimal getR15_TOTAL_NON_ACCRUAL() {
	return R15_TOTAL_NON_ACCRUAL;
}



public void setR15_TOTAL_NON_ACCRUAL(BigDecimal r15_TOTAL_NON_ACCRUAL) {
	R15_TOTAL_NON_ACCRUAL = r15_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR15_TOTAL_DUE_LOANS() {
	return R15_TOTAL_DUE_LOANS;
}



public void setR15_TOTAL_DUE_LOANS(BigDecimal r15_TOTAL_DUE_LOANS) {
	R15_TOTAL_DUE_LOANS = r15_TOTAL_DUE_LOANS;
}



public BigDecimal getR15_TOTAL_PERFORMING_LOAN() {
	return R15_TOTAL_PERFORMING_LOAN;
}



public void setR15_TOTAL_PERFORMING_LOAN(BigDecimal r15_TOTAL_PERFORMING_LOAN) {
	R15_TOTAL_PERFORMING_LOAN = r15_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR15_VALUE_OF_COLLATERAL() {
	return R15_VALUE_OF_COLLATERAL;
}



public void setR15_VALUE_OF_COLLATERAL(BigDecimal r15_VALUE_OF_COLLATERAL) {
	R15_VALUE_OF_COLLATERAL = r15_VALUE_OF_COLLATERAL;
}



public BigDecimal getR15_TOTAL_VALUE_NPL() {
	return R15_TOTAL_VALUE_NPL;
}



public void setR15_TOTAL_VALUE_NPL(BigDecimal r15_TOTAL_VALUE_NPL) {
	R15_TOTAL_VALUE_NPL = r15_TOTAL_VALUE_NPL;
}



public BigDecimal getR15_TOTAL_SPECIFIC_PROV() {
	return R15_TOTAL_SPECIFIC_PROV;
}



public void setR15_TOTAL_SPECIFIC_PROV(BigDecimal r15_TOTAL_SPECIFIC_PROV) {
	R15_TOTAL_SPECIFIC_PROV = r15_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR15_SPECIFIC_PROV_NPL() {
	return R15_SPECIFIC_PROV_NPL;
}



public void setR15_SPECIFIC_PROV_NPL(BigDecimal r15_SPECIFIC_PROV_NPL) {
	R15_SPECIFIC_PROV_NPL = r15_SPECIFIC_PROV_NPL;
}



public BigDecimal getR16_30D_90D_PASTDUE() {
	return R16_30D_90D_PASTDUE;
}



public void setR16_30D_90D_PASTDUE(BigDecimal r16_30d_90d_PASTDUE) {
	R16_30D_90D_PASTDUE = r16_30d_90d_PASTDUE;
}



public BigDecimal getR16_NON_PERFORM_LOANS() {
	return R16_NON_PERFORM_LOANS;
}



public void setR16_NON_PERFORM_LOANS(BigDecimal r16_NON_PERFORM_LOANS) {
	R16_NON_PERFORM_LOANS = r16_NON_PERFORM_LOANS;
}



public BigDecimal getR16_NON_ACCRUALS1() {
	return R16_NON_ACCRUALS1;
}



public void setR16_NON_ACCRUALS1(BigDecimal r16_NON_ACCRUALS1) {
	R16_NON_ACCRUALS1 = r16_NON_ACCRUALS1;
}



public BigDecimal getR16_SPECIFIC_PROV1() {
	return R16_SPECIFIC_PROV1;
}



public void setR16_SPECIFIC_PROV1(BigDecimal r16_SPECIFIC_PROV1) {
	R16_SPECIFIC_PROV1 = r16_SPECIFIC_PROV1;
}



public BigDecimal getR16_NO_OF_ACC1() {
	return R16_NO_OF_ACC1;
}



public void setR16_NO_OF_ACC1(BigDecimal r16_NO_OF_ACC1) {
	R16_NO_OF_ACC1 = r16_NO_OF_ACC1;
}



public BigDecimal getR16_90D_180D_PASTDUE() {
	return R16_90D_180D_PASTDUE;
}



public void setR16_90D_180D_PASTDUE(BigDecimal r16_90d_180d_PASTDUE) {
	R16_90D_180D_PASTDUE = r16_90d_180d_PASTDUE;
}



public BigDecimal getR16_NON_ACCRUALS2() {
	return R16_NON_ACCRUALS2;
}



public void setR16_NON_ACCRUALS2(BigDecimal r16_NON_ACCRUALS2) {
	R16_NON_ACCRUALS2 = r16_NON_ACCRUALS2;
}



public BigDecimal getR16_SPECIFIC_PROV2() {
	return R16_SPECIFIC_PROV2;
}



public void setR16_SPECIFIC_PROV2(BigDecimal r16_SPECIFIC_PROV2) {
	R16_SPECIFIC_PROV2 = r16_SPECIFIC_PROV2;
}



public BigDecimal getR16_NO_OF_ACC2() {
	return R16_NO_OF_ACC2;
}



public void setR16_NO_OF_ACC2(BigDecimal r16_NO_OF_ACC2) {
	R16_NO_OF_ACC2 = r16_NO_OF_ACC2;
}



public BigDecimal getR16_TOTAL_NON_ACCRUAL() {
	return R16_TOTAL_NON_ACCRUAL;
}



public void setR16_TOTAL_NON_ACCRUAL(BigDecimal r16_TOTAL_NON_ACCRUAL) {
	R16_TOTAL_NON_ACCRUAL = r16_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR16_TOTAL_DUE_LOANS() {
	return R16_TOTAL_DUE_LOANS;
}



public void setR16_TOTAL_DUE_LOANS(BigDecimal r16_TOTAL_DUE_LOANS) {
	R16_TOTAL_DUE_LOANS = r16_TOTAL_DUE_LOANS;
}



public BigDecimal getR16_TOTAL_PERFORMING_LOAN() {
	return R16_TOTAL_PERFORMING_LOAN;
}



public void setR16_TOTAL_PERFORMING_LOAN(BigDecimal r16_TOTAL_PERFORMING_LOAN) {
	R16_TOTAL_PERFORMING_LOAN = r16_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR16_VALUE_OF_COLLATERAL() {
	return R16_VALUE_OF_COLLATERAL;
}



public void setR16_VALUE_OF_COLLATERAL(BigDecimal r16_VALUE_OF_COLLATERAL) {
	R16_VALUE_OF_COLLATERAL = r16_VALUE_OF_COLLATERAL;
}



public BigDecimal getR16_TOTAL_VALUE_NPL() {
	return R16_TOTAL_VALUE_NPL;
}



public void setR16_TOTAL_VALUE_NPL(BigDecimal r16_TOTAL_VALUE_NPL) {
	R16_TOTAL_VALUE_NPL = r16_TOTAL_VALUE_NPL;
}



public BigDecimal getR16_TOTAL_SPECIFIC_PROV() {
	return R16_TOTAL_SPECIFIC_PROV;
}



public void setR16_TOTAL_SPECIFIC_PROV(BigDecimal r16_TOTAL_SPECIFIC_PROV) {
	R16_TOTAL_SPECIFIC_PROV = r16_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR16_SPECIFIC_PROV_NPL() {
	return R16_SPECIFIC_PROV_NPL;
}



public void setR16_SPECIFIC_PROV_NPL(BigDecimal r16_SPECIFIC_PROV_NPL) {
	R16_SPECIFIC_PROV_NPL = r16_SPECIFIC_PROV_NPL;
}



public BigDecimal getR17_30D_90D_PASTDUE() {
	return R17_30D_90D_PASTDUE;
}



public void setR17_30D_90D_PASTDUE(BigDecimal r17_30d_90d_PASTDUE) {
	R17_30D_90D_PASTDUE = r17_30d_90d_PASTDUE;
}



public BigDecimal getR17_NON_PERFORM_LOANS() {
	return R17_NON_PERFORM_LOANS;
}



public void setR17_NON_PERFORM_LOANS(BigDecimal r17_NON_PERFORM_LOANS) {
	R17_NON_PERFORM_LOANS = r17_NON_PERFORM_LOANS;
}



public BigDecimal getR17_NON_ACCRUALS1() {
	return R17_NON_ACCRUALS1;
}



public void setR17_NON_ACCRUALS1(BigDecimal r17_NON_ACCRUALS1) {
	R17_NON_ACCRUALS1 = r17_NON_ACCRUALS1;
}



public BigDecimal getR17_SPECIFIC_PROV1() {
	return R17_SPECIFIC_PROV1;
}



public void setR17_SPECIFIC_PROV1(BigDecimal r17_SPECIFIC_PROV1) {
	R17_SPECIFIC_PROV1 = r17_SPECIFIC_PROV1;
}



public BigDecimal getR17_NO_OF_ACC1() {
	return R17_NO_OF_ACC1;
}



public void setR17_NO_OF_ACC1(BigDecimal r17_NO_OF_ACC1) {
	R17_NO_OF_ACC1 = r17_NO_OF_ACC1;
}



public BigDecimal getR17_90D_180D_PASTDUE() {
	return R17_90D_180D_PASTDUE;
}



public void setR17_90D_180D_PASTDUE(BigDecimal r17_90d_180d_PASTDUE) {
	R17_90D_180D_PASTDUE = r17_90d_180d_PASTDUE;
}



public BigDecimal getR17_NON_ACCRUALS2() {
	return R17_NON_ACCRUALS2;
}



public void setR17_NON_ACCRUALS2(BigDecimal r17_NON_ACCRUALS2) {
	R17_NON_ACCRUALS2 = r17_NON_ACCRUALS2;
}



public BigDecimal getR17_SPECIFIC_PROV2() {
	return R17_SPECIFIC_PROV2;
}



public void setR17_SPECIFIC_PROV2(BigDecimal r17_SPECIFIC_PROV2) {
	R17_SPECIFIC_PROV2 = r17_SPECIFIC_PROV2;
}



public BigDecimal getR17_NO_OF_ACC2() {
	return R17_NO_OF_ACC2;
}



public void setR17_NO_OF_ACC2(BigDecimal r17_NO_OF_ACC2) {
	R17_NO_OF_ACC2 = r17_NO_OF_ACC2;
}



public BigDecimal getR17_TOTAL_NON_ACCRUAL() {
	return R17_TOTAL_NON_ACCRUAL;
}



public void setR17_TOTAL_NON_ACCRUAL(BigDecimal r17_TOTAL_NON_ACCRUAL) {
	R17_TOTAL_NON_ACCRUAL = r17_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR17_TOTAL_DUE_LOANS() {
	return R17_TOTAL_DUE_LOANS;
}



public void setR17_TOTAL_DUE_LOANS(BigDecimal r17_TOTAL_DUE_LOANS) {
	R17_TOTAL_DUE_LOANS = r17_TOTAL_DUE_LOANS;
}



public BigDecimal getR17_TOTAL_PERFORMING_LOAN() {
	return R17_TOTAL_PERFORMING_LOAN;
}



public void setR17_TOTAL_PERFORMING_LOAN(BigDecimal r17_TOTAL_PERFORMING_LOAN) {
	R17_TOTAL_PERFORMING_LOAN = r17_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR17_VALUE_OF_COLLATERAL() {
	return R17_VALUE_OF_COLLATERAL;
}



public void setR17_VALUE_OF_COLLATERAL(BigDecimal r17_VALUE_OF_COLLATERAL) {
	R17_VALUE_OF_COLLATERAL = r17_VALUE_OF_COLLATERAL;
}



public BigDecimal getR17_TOTAL_VALUE_NPL() {
	return R17_TOTAL_VALUE_NPL;
}



public void setR17_TOTAL_VALUE_NPL(BigDecimal r17_TOTAL_VALUE_NPL) {
	R17_TOTAL_VALUE_NPL = r17_TOTAL_VALUE_NPL;
}



public BigDecimal getR17_TOTAL_SPECIFIC_PROV() {
	return R17_TOTAL_SPECIFIC_PROV;
}



public void setR17_TOTAL_SPECIFIC_PROV(BigDecimal r17_TOTAL_SPECIFIC_PROV) {
	R17_TOTAL_SPECIFIC_PROV = r17_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR17_SPECIFIC_PROV_NPL() {
	return R17_SPECIFIC_PROV_NPL;
}



public void setR17_SPECIFIC_PROV_NPL(BigDecimal r17_SPECIFIC_PROV_NPL) {
	R17_SPECIFIC_PROV_NPL = r17_SPECIFIC_PROV_NPL;
}



public BigDecimal getR18_30D_90D_PASTDUE() {
	return R18_30D_90D_PASTDUE;
}



public void setR18_30D_90D_PASTDUE(BigDecimal r18_30d_90d_PASTDUE) {
	R18_30D_90D_PASTDUE = r18_30d_90d_PASTDUE;
}



public BigDecimal getR18_NON_PERFORM_LOANS() {
	return R18_NON_PERFORM_LOANS;
}



public void setR18_NON_PERFORM_LOANS(BigDecimal r18_NON_PERFORM_LOANS) {
	R18_NON_PERFORM_LOANS = r18_NON_PERFORM_LOANS;
}



public BigDecimal getR18_NON_ACCRUALS1() {
	return R18_NON_ACCRUALS1;
}



public void setR18_NON_ACCRUALS1(BigDecimal r18_NON_ACCRUALS1) {
	R18_NON_ACCRUALS1 = r18_NON_ACCRUALS1;
}



public BigDecimal getR18_SPECIFIC_PROV1() {
	return R18_SPECIFIC_PROV1;
}



public void setR18_SPECIFIC_PROV1(BigDecimal r18_SPECIFIC_PROV1) {
	R18_SPECIFIC_PROV1 = r18_SPECIFIC_PROV1;
}



public BigDecimal getR18_NO_OF_ACC1() {
	return R18_NO_OF_ACC1;
}



public void setR18_NO_OF_ACC1(BigDecimal r18_NO_OF_ACC1) {
	R18_NO_OF_ACC1 = r18_NO_OF_ACC1;
}



public BigDecimal getR18_90D_180D_PASTDUE() {
	return R18_90D_180D_PASTDUE;
}



public void setR18_90D_180D_PASTDUE(BigDecimal r18_90d_180d_PASTDUE) {
	R18_90D_180D_PASTDUE = r18_90d_180d_PASTDUE;
}



public BigDecimal getR18_NON_ACCRUALS2() {
	return R18_NON_ACCRUALS2;
}



public void setR18_NON_ACCRUALS2(BigDecimal r18_NON_ACCRUALS2) {
	R18_NON_ACCRUALS2 = r18_NON_ACCRUALS2;
}



public BigDecimal getR18_SPECIFIC_PROV2() {
	return R18_SPECIFIC_PROV2;
}



public void setR18_SPECIFIC_PROV2(BigDecimal r18_SPECIFIC_PROV2) {
	R18_SPECIFIC_PROV2 = r18_SPECIFIC_PROV2;
}



public BigDecimal getR18_NO_OF_ACC2() {
	return R18_NO_OF_ACC2;
}



public void setR18_NO_OF_ACC2(BigDecimal r18_NO_OF_ACC2) {
	R18_NO_OF_ACC2 = r18_NO_OF_ACC2;
}



public BigDecimal getR18_TOTAL_NON_ACCRUAL() {
	return R18_TOTAL_NON_ACCRUAL;
}



public void setR18_TOTAL_NON_ACCRUAL(BigDecimal r18_TOTAL_NON_ACCRUAL) {
	R18_TOTAL_NON_ACCRUAL = r18_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR18_TOTAL_DUE_LOANS() {
	return R18_TOTAL_DUE_LOANS;
}



public void setR18_TOTAL_DUE_LOANS(BigDecimal r18_TOTAL_DUE_LOANS) {
	R18_TOTAL_DUE_LOANS = r18_TOTAL_DUE_LOANS;
}



public BigDecimal getR18_TOTAL_PERFORMING_LOAN() {
	return R18_TOTAL_PERFORMING_LOAN;
}



public void setR18_TOTAL_PERFORMING_LOAN(BigDecimal r18_TOTAL_PERFORMING_LOAN) {
	R18_TOTAL_PERFORMING_LOAN = r18_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR18_VALUE_OF_COLLATERAL() {
	return R18_VALUE_OF_COLLATERAL;
}



public void setR18_VALUE_OF_COLLATERAL(BigDecimal r18_VALUE_OF_COLLATERAL) {
	R18_VALUE_OF_COLLATERAL = r18_VALUE_OF_COLLATERAL;
}



public BigDecimal getR18_TOTAL_VALUE_NPL() {
	return R18_TOTAL_VALUE_NPL;
}



public void setR18_TOTAL_VALUE_NPL(BigDecimal r18_TOTAL_VALUE_NPL) {
	R18_TOTAL_VALUE_NPL = r18_TOTAL_VALUE_NPL;
}



public BigDecimal getR18_TOTAL_SPECIFIC_PROV() {
	return R18_TOTAL_SPECIFIC_PROV;
}



public void setR18_TOTAL_SPECIFIC_PROV(BigDecimal r18_TOTAL_SPECIFIC_PROV) {
	R18_TOTAL_SPECIFIC_PROV = r18_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR18_SPECIFIC_PROV_NPL() {
	return R18_SPECIFIC_PROV_NPL;
}



public void setR18_SPECIFIC_PROV_NPL(BigDecimal r18_SPECIFIC_PROV_NPL) {
	R18_SPECIFIC_PROV_NPL = r18_SPECIFIC_PROV_NPL;
}



public BigDecimal getR19_30D_90D_PASTDUE() {
	return R19_30D_90D_PASTDUE;
}



public void setR19_30D_90D_PASTDUE(BigDecimal r19_30d_90d_PASTDUE) {
	R19_30D_90D_PASTDUE = r19_30d_90d_PASTDUE;
}



public BigDecimal getR19_NON_PERFORM_LOANS() {
	return R19_NON_PERFORM_LOANS;
}



public void setR19_NON_PERFORM_LOANS(BigDecimal r19_NON_PERFORM_LOANS) {
	R19_NON_PERFORM_LOANS = r19_NON_PERFORM_LOANS;
}



public BigDecimal getR19_NON_ACCRUALS1() {
	return R19_NON_ACCRUALS1;
}



public void setR19_NON_ACCRUALS1(BigDecimal r19_NON_ACCRUALS1) {
	R19_NON_ACCRUALS1 = r19_NON_ACCRUALS1;
}



public BigDecimal getR19_SPECIFIC_PROV1() {
	return R19_SPECIFIC_PROV1;
}



public void setR19_SPECIFIC_PROV1(BigDecimal r19_SPECIFIC_PROV1) {
	R19_SPECIFIC_PROV1 = r19_SPECIFIC_PROV1;
}



public BigDecimal getR19_NO_OF_ACC1() {
	return R19_NO_OF_ACC1;
}



public void setR19_NO_OF_ACC1(BigDecimal r19_NO_OF_ACC1) {
	R19_NO_OF_ACC1 = r19_NO_OF_ACC1;
}



public BigDecimal getR19_90D_180D_PASTDUE() {
	return R19_90D_180D_PASTDUE;
}



public void setR19_90D_180D_PASTDUE(BigDecimal r19_90d_180d_PASTDUE) {
	R19_90D_180D_PASTDUE = r19_90d_180d_PASTDUE;
}



public BigDecimal getR19_NON_ACCRUALS2() {
	return R19_NON_ACCRUALS2;
}



public void setR19_NON_ACCRUALS2(BigDecimal r19_NON_ACCRUALS2) {
	R19_NON_ACCRUALS2 = r19_NON_ACCRUALS2;
}



public BigDecimal getR19_SPECIFIC_PROV2() {
	return R19_SPECIFIC_PROV2;
}



public void setR19_SPECIFIC_PROV2(BigDecimal r19_SPECIFIC_PROV2) {
	R19_SPECIFIC_PROV2 = r19_SPECIFIC_PROV2;
}



public BigDecimal getR19_NO_OF_ACC2() {
	return R19_NO_OF_ACC2;
}



public void setR19_NO_OF_ACC2(BigDecimal r19_NO_OF_ACC2) {
	R19_NO_OF_ACC2 = r19_NO_OF_ACC2;
}



public BigDecimal getR19_TOTAL_NON_ACCRUAL() {
	return R19_TOTAL_NON_ACCRUAL;
}



public void setR19_TOTAL_NON_ACCRUAL(BigDecimal r19_TOTAL_NON_ACCRUAL) {
	R19_TOTAL_NON_ACCRUAL = r19_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR19_TOTAL_DUE_LOANS() {
	return R19_TOTAL_DUE_LOANS;
}



public void setR19_TOTAL_DUE_LOANS(BigDecimal r19_TOTAL_DUE_LOANS) {
	R19_TOTAL_DUE_LOANS = r19_TOTAL_DUE_LOANS;
}



public BigDecimal getR19_TOTAL_PERFORMING_LOAN() {
	return R19_TOTAL_PERFORMING_LOAN;
}



public void setR19_TOTAL_PERFORMING_LOAN(BigDecimal r19_TOTAL_PERFORMING_LOAN) {
	R19_TOTAL_PERFORMING_LOAN = r19_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR19_VALUE_OF_COLLATERAL() {
	return R19_VALUE_OF_COLLATERAL;
}



public void setR19_VALUE_OF_COLLATERAL(BigDecimal r19_VALUE_OF_COLLATERAL) {
	R19_VALUE_OF_COLLATERAL = r19_VALUE_OF_COLLATERAL;
}



public BigDecimal getR19_TOTAL_VALUE_NPL() {
	return R19_TOTAL_VALUE_NPL;
}



public void setR19_TOTAL_VALUE_NPL(BigDecimal r19_TOTAL_VALUE_NPL) {
	R19_TOTAL_VALUE_NPL = r19_TOTAL_VALUE_NPL;
}



public BigDecimal getR19_TOTAL_SPECIFIC_PROV() {
	return R19_TOTAL_SPECIFIC_PROV;
}



public void setR19_TOTAL_SPECIFIC_PROV(BigDecimal r19_TOTAL_SPECIFIC_PROV) {
	R19_TOTAL_SPECIFIC_PROV = r19_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR19_SPECIFIC_PROV_NPL() {
	return R19_SPECIFIC_PROV_NPL;
}



public void setR19_SPECIFIC_PROV_NPL(BigDecimal r19_SPECIFIC_PROV_NPL) {
	R19_SPECIFIC_PROV_NPL = r19_SPECIFIC_PROV_NPL;
}



public BigDecimal getR20_30D_90D_PASTDUE() {
	return R20_30D_90D_PASTDUE;
}



public void setR20_30D_90D_PASTDUE(BigDecimal r20_30d_90d_PASTDUE) {
	R20_30D_90D_PASTDUE = r20_30d_90d_PASTDUE;
}



public BigDecimal getR20_NON_PERFORM_LOANS() {
	return R20_NON_PERFORM_LOANS;
}



public void setR20_NON_PERFORM_LOANS(BigDecimal r20_NON_PERFORM_LOANS) {
	R20_NON_PERFORM_LOANS = r20_NON_PERFORM_LOANS;
}



public BigDecimal getR20_NON_ACCRUALS1() {
	return R20_NON_ACCRUALS1;
}



public void setR20_NON_ACCRUALS1(BigDecimal r20_NON_ACCRUALS1) {
	R20_NON_ACCRUALS1 = r20_NON_ACCRUALS1;
}



public BigDecimal getR20_SPECIFIC_PROV1() {
	return R20_SPECIFIC_PROV1;
}



public void setR20_SPECIFIC_PROV1(BigDecimal r20_SPECIFIC_PROV1) {
	R20_SPECIFIC_PROV1 = r20_SPECIFIC_PROV1;
}



public BigDecimal getR20_NO_OF_ACC1() {
	return R20_NO_OF_ACC1;
}



public void setR20_NO_OF_ACC1(BigDecimal r20_NO_OF_ACC1) {
	R20_NO_OF_ACC1 = r20_NO_OF_ACC1;
}



public BigDecimal getR20_90D_180D_PASTDUE() {
	return R20_90D_180D_PASTDUE;
}



public void setR20_90D_180D_PASTDUE(BigDecimal r20_90d_180d_PASTDUE) {
	R20_90D_180D_PASTDUE = r20_90d_180d_PASTDUE;
}



public BigDecimal getR20_NON_ACCRUALS2() {
	return R20_NON_ACCRUALS2;
}



public void setR20_NON_ACCRUALS2(BigDecimal r20_NON_ACCRUALS2) {
	R20_NON_ACCRUALS2 = r20_NON_ACCRUALS2;
}



public BigDecimal getR20_SPECIFIC_PROV2() {
	return R20_SPECIFIC_PROV2;
}



public void setR20_SPECIFIC_PROV2(BigDecimal r20_SPECIFIC_PROV2) {
	R20_SPECIFIC_PROV2 = r20_SPECIFIC_PROV2;
}



public BigDecimal getR20_NO_OF_ACC2() {
	return R20_NO_OF_ACC2;
}



public void setR20_NO_OF_ACC2(BigDecimal r20_NO_OF_ACC2) {
	R20_NO_OF_ACC2 = r20_NO_OF_ACC2;
}



public BigDecimal getR20_TOTAL_NON_ACCRUAL() {
	return R20_TOTAL_NON_ACCRUAL;
}



public void setR20_TOTAL_NON_ACCRUAL(BigDecimal r20_TOTAL_NON_ACCRUAL) {
	R20_TOTAL_NON_ACCRUAL = r20_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR20_TOTAL_DUE_LOANS() {
	return R20_TOTAL_DUE_LOANS;
}



public void setR20_TOTAL_DUE_LOANS(BigDecimal r20_TOTAL_DUE_LOANS) {
	R20_TOTAL_DUE_LOANS = r20_TOTAL_DUE_LOANS;
}



public BigDecimal getR20_TOTAL_PERFORMING_LOAN() {
	return R20_TOTAL_PERFORMING_LOAN;
}



public void setR20_TOTAL_PERFORMING_LOAN(BigDecimal r20_TOTAL_PERFORMING_LOAN) {
	R20_TOTAL_PERFORMING_LOAN = r20_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR20_VALUE_OF_COLLATERAL() {
	return R20_VALUE_OF_COLLATERAL;
}



public void setR20_VALUE_OF_COLLATERAL(BigDecimal r20_VALUE_OF_COLLATERAL) {
	R20_VALUE_OF_COLLATERAL = r20_VALUE_OF_COLLATERAL;
}



public BigDecimal getR20_TOTAL_VALUE_NPL() {
	return R20_TOTAL_VALUE_NPL;
}



public void setR20_TOTAL_VALUE_NPL(BigDecimal r20_TOTAL_VALUE_NPL) {
	R20_TOTAL_VALUE_NPL = r20_TOTAL_VALUE_NPL;
}



public BigDecimal getR20_TOTAL_SPECIFIC_PROV() {
	return R20_TOTAL_SPECIFIC_PROV;
}



public void setR20_TOTAL_SPECIFIC_PROV(BigDecimal r20_TOTAL_SPECIFIC_PROV) {
	R20_TOTAL_SPECIFIC_PROV = r20_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR20_SPECIFIC_PROV_NPL() {
	return R20_SPECIFIC_PROV_NPL;
}



public void setR20_SPECIFIC_PROV_NPL(BigDecimal r20_SPECIFIC_PROV_NPL) {
	R20_SPECIFIC_PROV_NPL = r20_SPECIFIC_PROV_NPL;
}



public BigDecimal getR21_30D_90D_PASTDUE() {
	return R21_30D_90D_PASTDUE;
}



public void setR21_30D_90D_PASTDUE(BigDecimal r21_30d_90d_PASTDUE) {
	R21_30D_90D_PASTDUE = r21_30d_90d_PASTDUE;
}



public BigDecimal getR21_NON_PERFORM_LOANS() {
	return R21_NON_PERFORM_LOANS;
}



public void setR21_NON_PERFORM_LOANS(BigDecimal r21_NON_PERFORM_LOANS) {
	R21_NON_PERFORM_LOANS = r21_NON_PERFORM_LOANS;
}



public BigDecimal getR21_NON_ACCRUALS1() {
	return R21_NON_ACCRUALS1;
}



public void setR21_NON_ACCRUALS1(BigDecimal r21_NON_ACCRUALS1) {
	R21_NON_ACCRUALS1 = r21_NON_ACCRUALS1;
}



public BigDecimal getR21_SPECIFIC_PROV1() {
	return R21_SPECIFIC_PROV1;
}



public void setR21_SPECIFIC_PROV1(BigDecimal r21_SPECIFIC_PROV1) {
	R21_SPECIFIC_PROV1 = r21_SPECIFIC_PROV1;
}



public BigDecimal getR21_NO_OF_ACC1() {
	return R21_NO_OF_ACC1;
}



public void setR21_NO_OF_ACC1(BigDecimal r21_NO_OF_ACC1) {
	R21_NO_OF_ACC1 = r21_NO_OF_ACC1;
}



public BigDecimal getR21_90D_180D_PASTDUE() {
	return R21_90D_180D_PASTDUE;
}



public void setR21_90D_180D_PASTDUE(BigDecimal r21_90d_180d_PASTDUE) {
	R21_90D_180D_PASTDUE = r21_90d_180d_PASTDUE;
}



public BigDecimal getR21_NON_ACCRUALS2() {
	return R21_NON_ACCRUALS2;
}



public void setR21_NON_ACCRUALS2(BigDecimal r21_NON_ACCRUALS2) {
	R21_NON_ACCRUALS2 = r21_NON_ACCRUALS2;
}



public BigDecimal getR21_SPECIFIC_PROV2() {
	return R21_SPECIFIC_PROV2;
}



public void setR21_SPECIFIC_PROV2(BigDecimal r21_SPECIFIC_PROV2) {
	R21_SPECIFIC_PROV2 = r21_SPECIFIC_PROV2;
}



public BigDecimal getR21_NO_OF_ACC2() {
	return R21_NO_OF_ACC2;
}



public void setR21_NO_OF_ACC2(BigDecimal r21_NO_OF_ACC2) {
	R21_NO_OF_ACC2 = r21_NO_OF_ACC2;
}



public BigDecimal getR21_TOTAL_NON_ACCRUAL() {
	return R21_TOTAL_NON_ACCRUAL;
}



public void setR21_TOTAL_NON_ACCRUAL(BigDecimal r21_TOTAL_NON_ACCRUAL) {
	R21_TOTAL_NON_ACCRUAL = r21_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR21_TOTAL_DUE_LOANS() {
	return R21_TOTAL_DUE_LOANS;
}



public void setR21_TOTAL_DUE_LOANS(BigDecimal r21_TOTAL_DUE_LOANS) {
	R21_TOTAL_DUE_LOANS = r21_TOTAL_DUE_LOANS;
}



public BigDecimal getR21_TOTAL_PERFORMING_LOAN() {
	return R21_TOTAL_PERFORMING_LOAN;
}



public void setR21_TOTAL_PERFORMING_LOAN(BigDecimal r21_TOTAL_PERFORMING_LOAN) {
	R21_TOTAL_PERFORMING_LOAN = r21_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR21_VALUE_OF_COLLATERAL() {
	return R21_VALUE_OF_COLLATERAL;
}



public void setR21_VALUE_OF_COLLATERAL(BigDecimal r21_VALUE_OF_COLLATERAL) {
	R21_VALUE_OF_COLLATERAL = r21_VALUE_OF_COLLATERAL;
}



public BigDecimal getR21_TOTAL_VALUE_NPL() {
	return R21_TOTAL_VALUE_NPL;
}



public void setR21_TOTAL_VALUE_NPL(BigDecimal r21_TOTAL_VALUE_NPL) {
	R21_TOTAL_VALUE_NPL = r21_TOTAL_VALUE_NPL;
}



public BigDecimal getR21_TOTAL_SPECIFIC_PROV() {
	return R21_TOTAL_SPECIFIC_PROV;
}



public void setR21_TOTAL_SPECIFIC_PROV(BigDecimal r21_TOTAL_SPECIFIC_PROV) {
	R21_TOTAL_SPECIFIC_PROV = r21_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR21_SPECIFIC_PROV_NPL() {
	return R21_SPECIFIC_PROV_NPL;
}



public void setR21_SPECIFIC_PROV_NPL(BigDecimal r21_SPECIFIC_PROV_NPL) {
	R21_SPECIFIC_PROV_NPL = r21_SPECIFIC_PROV_NPL;
}



public BigDecimal getR22_30D_90D_PASTDUE() {
	return R22_30D_90D_PASTDUE;
}



public void setR22_30D_90D_PASTDUE(BigDecimal r22_30d_90d_PASTDUE) {
	R22_30D_90D_PASTDUE = r22_30d_90d_PASTDUE;
}



public BigDecimal getR22_NON_PERFORM_LOANS() {
	return R22_NON_PERFORM_LOANS;
}



public void setR22_NON_PERFORM_LOANS(BigDecimal r22_NON_PERFORM_LOANS) {
	R22_NON_PERFORM_LOANS = r22_NON_PERFORM_LOANS;
}



public BigDecimal getR22_NON_ACCRUALS1() {
	return R22_NON_ACCRUALS1;
}



public void setR22_NON_ACCRUALS1(BigDecimal r22_NON_ACCRUALS1) {
	R22_NON_ACCRUALS1 = r22_NON_ACCRUALS1;
}



public BigDecimal getR22_SPECIFIC_PROV1() {
	return R22_SPECIFIC_PROV1;
}



public void setR22_SPECIFIC_PROV1(BigDecimal r22_SPECIFIC_PROV1) {
	R22_SPECIFIC_PROV1 = r22_SPECIFIC_PROV1;
}



public BigDecimal getR22_NO_OF_ACC1() {
	return R22_NO_OF_ACC1;
}



public void setR22_NO_OF_ACC1(BigDecimal r22_NO_OF_ACC1) {
	R22_NO_OF_ACC1 = r22_NO_OF_ACC1;
}



public BigDecimal getR22_90D_180D_PASTDUE() {
	return R22_90D_180D_PASTDUE;
}



public void setR22_90D_180D_PASTDUE(BigDecimal r22_90d_180d_PASTDUE) {
	R22_90D_180D_PASTDUE = r22_90d_180d_PASTDUE;
}



public BigDecimal getR22_NON_ACCRUALS2() {
	return R22_NON_ACCRUALS2;
}



public void setR22_NON_ACCRUALS2(BigDecimal r22_NON_ACCRUALS2) {
	R22_NON_ACCRUALS2 = r22_NON_ACCRUALS2;
}



public BigDecimal getR22_SPECIFIC_PROV2() {
	return R22_SPECIFIC_PROV2;
}



public void setR22_SPECIFIC_PROV2(BigDecimal r22_SPECIFIC_PROV2) {
	R22_SPECIFIC_PROV2 = r22_SPECIFIC_PROV2;
}



public BigDecimal getR22_NO_OF_ACC2() {
	return R22_NO_OF_ACC2;
}



public void setR22_NO_OF_ACC2(BigDecimal r22_NO_OF_ACC2) {
	R22_NO_OF_ACC2 = r22_NO_OF_ACC2;
}



public BigDecimal getR22_TOTAL_NON_ACCRUAL() {
	return R22_TOTAL_NON_ACCRUAL;
}



public void setR22_TOTAL_NON_ACCRUAL(BigDecimal r22_TOTAL_NON_ACCRUAL) {
	R22_TOTAL_NON_ACCRUAL = r22_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR22_TOTAL_DUE_LOANS() {
	return R22_TOTAL_DUE_LOANS;
}



public void setR22_TOTAL_DUE_LOANS(BigDecimal r22_TOTAL_DUE_LOANS) {
	R22_TOTAL_DUE_LOANS = r22_TOTAL_DUE_LOANS;
}



public BigDecimal getR22_TOTAL_PERFORMING_LOAN() {
	return R22_TOTAL_PERFORMING_LOAN;
}



public void setR22_TOTAL_PERFORMING_LOAN(BigDecimal r22_TOTAL_PERFORMING_LOAN) {
	R22_TOTAL_PERFORMING_LOAN = r22_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR22_VALUE_OF_COLLATERAL() {
	return R22_VALUE_OF_COLLATERAL;
}



public void setR22_VALUE_OF_COLLATERAL(BigDecimal r22_VALUE_OF_COLLATERAL) {
	R22_VALUE_OF_COLLATERAL = r22_VALUE_OF_COLLATERAL;
}



public BigDecimal getR22_TOTAL_VALUE_NPL() {
	return R22_TOTAL_VALUE_NPL;
}



public void setR22_TOTAL_VALUE_NPL(BigDecimal r22_TOTAL_VALUE_NPL) {
	R22_TOTAL_VALUE_NPL = r22_TOTAL_VALUE_NPL;
}



public BigDecimal getR22_TOTAL_SPECIFIC_PROV() {
	return R22_TOTAL_SPECIFIC_PROV;
}



public void setR22_TOTAL_SPECIFIC_PROV(BigDecimal r22_TOTAL_SPECIFIC_PROV) {
	R22_TOTAL_SPECIFIC_PROV = r22_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR22_SPECIFIC_PROV_NPL() {
	return R22_SPECIFIC_PROV_NPL;
}



public void setR22_SPECIFIC_PROV_NPL(BigDecimal r22_SPECIFIC_PROV_NPL) {
	R22_SPECIFIC_PROV_NPL = r22_SPECIFIC_PROV_NPL;
}



public BigDecimal getR23_30D_90D_PASTDUE() {
	return R23_30D_90D_PASTDUE;
}



public void setR23_30D_90D_PASTDUE(BigDecimal r23_30d_90d_PASTDUE) {
	R23_30D_90D_PASTDUE = r23_30d_90d_PASTDUE;
}



public BigDecimal getR23_NON_PERFORM_LOANS() {
	return R23_NON_PERFORM_LOANS;
}



public void setR23_NON_PERFORM_LOANS(BigDecimal r23_NON_PERFORM_LOANS) {
	R23_NON_PERFORM_LOANS = r23_NON_PERFORM_LOANS;
}



public BigDecimal getR23_NON_ACCRUALS1() {
	return R23_NON_ACCRUALS1;
}



public void setR23_NON_ACCRUALS1(BigDecimal r23_NON_ACCRUALS1) {
	R23_NON_ACCRUALS1 = r23_NON_ACCRUALS1;
}



public BigDecimal getR23_SPECIFIC_PROV1() {
	return R23_SPECIFIC_PROV1;
}



public void setR23_SPECIFIC_PROV1(BigDecimal r23_SPECIFIC_PROV1) {
	R23_SPECIFIC_PROV1 = r23_SPECIFIC_PROV1;
}



public BigDecimal getR23_NO_OF_ACC1() {
	return R23_NO_OF_ACC1;
}



public void setR23_NO_OF_ACC1(BigDecimal r23_NO_OF_ACC1) {
	R23_NO_OF_ACC1 = r23_NO_OF_ACC1;
}



public BigDecimal getR23_90D_180D_PASTDUE() {
	return R23_90D_180D_PASTDUE;
}



public void setR23_90D_180D_PASTDUE(BigDecimal r23_90d_180d_PASTDUE) {
	R23_90D_180D_PASTDUE = r23_90d_180d_PASTDUE;
}



public BigDecimal getR23_NON_ACCRUALS2() {
	return R23_NON_ACCRUALS2;
}



public void setR23_NON_ACCRUALS2(BigDecimal r23_NON_ACCRUALS2) {
	R23_NON_ACCRUALS2 = r23_NON_ACCRUALS2;
}



public BigDecimal getR23_SPECIFIC_PROV2() {
	return R23_SPECIFIC_PROV2;
}



public void setR23_SPECIFIC_PROV2(BigDecimal r23_SPECIFIC_PROV2) {
	R23_SPECIFIC_PROV2 = r23_SPECIFIC_PROV2;
}



public BigDecimal getR23_NO_OF_ACC2() {
	return R23_NO_OF_ACC2;
}



public void setR23_NO_OF_ACC2(BigDecimal r23_NO_OF_ACC2) {
	R23_NO_OF_ACC2 = r23_NO_OF_ACC2;
}



public BigDecimal getR23_TOTAL_NON_ACCRUAL() {
	return R23_TOTAL_NON_ACCRUAL;
}



public void setR23_TOTAL_NON_ACCRUAL(BigDecimal r23_TOTAL_NON_ACCRUAL) {
	R23_TOTAL_NON_ACCRUAL = r23_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR23_TOTAL_DUE_LOANS() {
	return R23_TOTAL_DUE_LOANS;
}



public void setR23_TOTAL_DUE_LOANS(BigDecimal r23_TOTAL_DUE_LOANS) {
	R23_TOTAL_DUE_LOANS = r23_TOTAL_DUE_LOANS;
}



public BigDecimal getR23_TOTAL_PERFORMING_LOAN() {
	return R23_TOTAL_PERFORMING_LOAN;
}



public void setR23_TOTAL_PERFORMING_LOAN(BigDecimal r23_TOTAL_PERFORMING_LOAN) {
	R23_TOTAL_PERFORMING_LOAN = r23_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR23_VALUE_OF_COLLATERAL() {
	return R23_VALUE_OF_COLLATERAL;
}



public void setR23_VALUE_OF_COLLATERAL(BigDecimal r23_VALUE_OF_COLLATERAL) {
	R23_VALUE_OF_COLLATERAL = r23_VALUE_OF_COLLATERAL;
}



public BigDecimal getR23_TOTAL_VALUE_NPL() {
	return R23_TOTAL_VALUE_NPL;
}



public void setR23_TOTAL_VALUE_NPL(BigDecimal r23_TOTAL_VALUE_NPL) {
	R23_TOTAL_VALUE_NPL = r23_TOTAL_VALUE_NPL;
}



public BigDecimal getR23_TOTAL_SPECIFIC_PROV() {
	return R23_TOTAL_SPECIFIC_PROV;
}



public void setR23_TOTAL_SPECIFIC_PROV(BigDecimal r23_TOTAL_SPECIFIC_PROV) {
	R23_TOTAL_SPECIFIC_PROV = r23_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR23_SPECIFIC_PROV_NPL() {
	return R23_SPECIFIC_PROV_NPL;
}



public void setR23_SPECIFIC_PROV_NPL(BigDecimal r23_SPECIFIC_PROV_NPL) {
	R23_SPECIFIC_PROV_NPL = r23_SPECIFIC_PROV_NPL;
}



public BigDecimal getR24_30D_90D_PASTDUE() {
	return R24_30D_90D_PASTDUE;
}



public void setR24_30D_90D_PASTDUE(BigDecimal r24_30d_90d_PASTDUE) {
	R24_30D_90D_PASTDUE = r24_30d_90d_PASTDUE;
}



public BigDecimal getR24_NON_PERFORM_LOANS() {
	return R24_NON_PERFORM_LOANS;
}



public void setR24_NON_PERFORM_LOANS(BigDecimal r24_NON_PERFORM_LOANS) {
	R24_NON_PERFORM_LOANS = r24_NON_PERFORM_LOANS;
}



public BigDecimal getR24_NON_ACCRUALS1() {
	return R24_NON_ACCRUALS1;
}



public void setR24_NON_ACCRUALS1(BigDecimal r24_NON_ACCRUALS1) {
	R24_NON_ACCRUALS1 = r24_NON_ACCRUALS1;
}



public BigDecimal getR24_SPECIFIC_PROV1() {
	return R24_SPECIFIC_PROV1;
}



public void setR24_SPECIFIC_PROV1(BigDecimal r24_SPECIFIC_PROV1) {
	R24_SPECIFIC_PROV1 = r24_SPECIFIC_PROV1;
}



public BigDecimal getR24_NO_OF_ACC1() {
	return R24_NO_OF_ACC1;
}



public void setR24_NO_OF_ACC1(BigDecimal r24_NO_OF_ACC1) {
	R24_NO_OF_ACC1 = r24_NO_OF_ACC1;
}



public BigDecimal getR24_90D_180D_PASTDUE() {
	return R24_90D_180D_PASTDUE;
}



public void setR24_90D_180D_PASTDUE(BigDecimal r24_90d_180d_PASTDUE) {
	R24_90D_180D_PASTDUE = r24_90d_180d_PASTDUE;
}



public BigDecimal getR24_NON_ACCRUALS2() {
	return R24_NON_ACCRUALS2;
}



public void setR24_NON_ACCRUALS2(BigDecimal r24_NON_ACCRUALS2) {
	R24_NON_ACCRUALS2 = r24_NON_ACCRUALS2;
}



public BigDecimal getR24_SPECIFIC_PROV2() {
	return R24_SPECIFIC_PROV2;
}



public void setR24_SPECIFIC_PROV2(BigDecimal r24_SPECIFIC_PROV2) {
	R24_SPECIFIC_PROV2 = r24_SPECIFIC_PROV2;
}



public BigDecimal getR24_NO_OF_ACC2() {
	return R24_NO_OF_ACC2;
}



public void setR24_NO_OF_ACC2(BigDecimal r24_NO_OF_ACC2) {
	R24_NO_OF_ACC2 = r24_NO_OF_ACC2;
}



public BigDecimal getR24_TOTAL_NON_ACCRUAL() {
	return R24_TOTAL_NON_ACCRUAL;
}



public void setR24_TOTAL_NON_ACCRUAL(BigDecimal r24_TOTAL_NON_ACCRUAL) {
	R24_TOTAL_NON_ACCRUAL = r24_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR24_TOTAL_DUE_LOANS() {
	return R24_TOTAL_DUE_LOANS;
}



public void setR24_TOTAL_DUE_LOANS(BigDecimal r24_TOTAL_DUE_LOANS) {
	R24_TOTAL_DUE_LOANS = r24_TOTAL_DUE_LOANS;
}



public BigDecimal getR24_TOTAL_PERFORMING_LOAN() {
	return R24_TOTAL_PERFORMING_LOAN;
}



public void setR24_TOTAL_PERFORMING_LOAN(BigDecimal r24_TOTAL_PERFORMING_LOAN) {
	R24_TOTAL_PERFORMING_LOAN = r24_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR24_VALUE_OF_COLLATERAL() {
	return R24_VALUE_OF_COLLATERAL;
}



public void setR24_VALUE_OF_COLLATERAL(BigDecimal r24_VALUE_OF_COLLATERAL) {
	R24_VALUE_OF_COLLATERAL = r24_VALUE_OF_COLLATERAL;
}



public BigDecimal getR24_TOTAL_VALUE_NPL() {
	return R24_TOTAL_VALUE_NPL;
}



public void setR24_TOTAL_VALUE_NPL(BigDecimal r24_TOTAL_VALUE_NPL) {
	R24_TOTAL_VALUE_NPL = r24_TOTAL_VALUE_NPL;
}



public BigDecimal getR24_TOTAL_SPECIFIC_PROV() {
	return R24_TOTAL_SPECIFIC_PROV;
}



public void setR24_TOTAL_SPECIFIC_PROV(BigDecimal r24_TOTAL_SPECIFIC_PROV) {
	R24_TOTAL_SPECIFIC_PROV = r24_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR24_SPECIFIC_PROV_NPL() {
	return R24_SPECIFIC_PROV_NPL;
}



public void setR24_SPECIFIC_PROV_NPL(BigDecimal r24_SPECIFIC_PROV_NPL) {
	R24_SPECIFIC_PROV_NPL = r24_SPECIFIC_PROV_NPL;
}



public BigDecimal getR25_30D_90D_PASTDUE() {
	return R25_30D_90D_PASTDUE;
}



public void setR25_30D_90D_PASTDUE(BigDecimal r25_30d_90d_PASTDUE) {
	R25_30D_90D_PASTDUE = r25_30d_90d_PASTDUE;
}



public BigDecimal getR25_NON_PERFORM_LOANS() {
	return R25_NON_PERFORM_LOANS;
}



public void setR25_NON_PERFORM_LOANS(BigDecimal r25_NON_PERFORM_LOANS) {
	R25_NON_PERFORM_LOANS = r25_NON_PERFORM_LOANS;
}



public BigDecimal getR25_NON_ACCRUALS1() {
	return R25_NON_ACCRUALS1;
}



public void setR25_NON_ACCRUALS1(BigDecimal r25_NON_ACCRUALS1) {
	R25_NON_ACCRUALS1 = r25_NON_ACCRUALS1;
}



public BigDecimal getR25_SPECIFIC_PROV1() {
	return R25_SPECIFIC_PROV1;
}



public void setR25_SPECIFIC_PROV1(BigDecimal r25_SPECIFIC_PROV1) {
	R25_SPECIFIC_PROV1 = r25_SPECIFIC_PROV1;
}



public BigDecimal getR25_NO_OF_ACC1() {
	return R25_NO_OF_ACC1;
}



public void setR25_NO_OF_ACC1(BigDecimal r25_NO_OF_ACC1) {
	R25_NO_OF_ACC1 = r25_NO_OF_ACC1;
}



public BigDecimal getR25_90D_180D_PASTDUE() {
	return R25_90D_180D_PASTDUE;
}



public void setR25_90D_180D_PASTDUE(BigDecimal r25_90d_180d_PASTDUE) {
	R25_90D_180D_PASTDUE = r25_90d_180d_PASTDUE;
}



public BigDecimal getR25_NON_ACCRUALS2() {
	return R25_NON_ACCRUALS2;
}



public void setR25_NON_ACCRUALS2(BigDecimal r25_NON_ACCRUALS2) {
	R25_NON_ACCRUALS2 = r25_NON_ACCRUALS2;
}



public BigDecimal getR25_SPECIFIC_PROV2() {
	return R25_SPECIFIC_PROV2;
}



public void setR25_SPECIFIC_PROV2(BigDecimal r25_SPECIFIC_PROV2) {
	R25_SPECIFIC_PROV2 = r25_SPECIFIC_PROV2;
}



public BigDecimal getR25_NO_OF_ACC2() {
	return R25_NO_OF_ACC2;
}



public void setR25_NO_OF_ACC2(BigDecimal r25_NO_OF_ACC2) {
	R25_NO_OF_ACC2 = r25_NO_OF_ACC2;
}



public BigDecimal getR25_TOTAL_NON_ACCRUAL() {
	return R25_TOTAL_NON_ACCRUAL;
}



public void setR25_TOTAL_NON_ACCRUAL(BigDecimal r25_TOTAL_NON_ACCRUAL) {
	R25_TOTAL_NON_ACCRUAL = r25_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR25_TOTAL_DUE_LOANS() {
	return R25_TOTAL_DUE_LOANS;
}



public void setR25_TOTAL_DUE_LOANS(BigDecimal r25_TOTAL_DUE_LOANS) {
	R25_TOTAL_DUE_LOANS = r25_TOTAL_DUE_LOANS;
}



public BigDecimal getR25_TOTAL_PERFORMING_LOAN() {
	return R25_TOTAL_PERFORMING_LOAN;
}



public void setR25_TOTAL_PERFORMING_LOAN(BigDecimal r25_TOTAL_PERFORMING_LOAN) {
	R25_TOTAL_PERFORMING_LOAN = r25_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR25_VALUE_OF_COLLATERAL() {
	return R25_VALUE_OF_COLLATERAL;
}



public void setR25_VALUE_OF_COLLATERAL(BigDecimal r25_VALUE_OF_COLLATERAL) {
	R25_VALUE_OF_COLLATERAL = r25_VALUE_OF_COLLATERAL;
}



public BigDecimal getR25_TOTAL_VALUE_NPL() {
	return R25_TOTAL_VALUE_NPL;
}



public void setR25_TOTAL_VALUE_NPL(BigDecimal r25_TOTAL_VALUE_NPL) {
	R25_TOTAL_VALUE_NPL = r25_TOTAL_VALUE_NPL;
}



public BigDecimal getR25_TOTAL_SPECIFIC_PROV() {
	return R25_TOTAL_SPECIFIC_PROV;
}



public void setR25_TOTAL_SPECIFIC_PROV(BigDecimal r25_TOTAL_SPECIFIC_PROV) {
	R25_TOTAL_SPECIFIC_PROV = r25_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR25_SPECIFIC_PROV_NPL() {
	return R25_SPECIFIC_PROV_NPL;
}



public void setR25_SPECIFIC_PROV_NPL(BigDecimal r25_SPECIFIC_PROV_NPL) {
	R25_SPECIFIC_PROV_NPL = r25_SPECIFIC_PROV_NPL;
}



public BigDecimal getR26_30D_90D_PASTDUE() {
	return R26_30D_90D_PASTDUE;
}



public void setR26_30D_90D_PASTDUE(BigDecimal r26_30d_90d_PASTDUE) {
	R26_30D_90D_PASTDUE = r26_30d_90d_PASTDUE;
}



public BigDecimal getR26_NON_PERFORM_LOANS() {
	return R26_NON_PERFORM_LOANS;
}



public void setR26_NON_PERFORM_LOANS(BigDecimal r26_NON_PERFORM_LOANS) {
	R26_NON_PERFORM_LOANS = r26_NON_PERFORM_LOANS;
}



public BigDecimal getR26_NON_ACCRUALS1() {
	return R26_NON_ACCRUALS1;
}



public void setR26_NON_ACCRUALS1(BigDecimal r26_NON_ACCRUALS1) {
	R26_NON_ACCRUALS1 = r26_NON_ACCRUALS1;
}



public BigDecimal getR26_SPECIFIC_PROV1() {
	return R26_SPECIFIC_PROV1;
}



public void setR26_SPECIFIC_PROV1(BigDecimal r26_SPECIFIC_PROV1) {
	R26_SPECIFIC_PROV1 = r26_SPECIFIC_PROV1;
}



public BigDecimal getR26_NO_OF_ACC1() {
	return R26_NO_OF_ACC1;
}



public void setR26_NO_OF_ACC1(BigDecimal r26_NO_OF_ACC1) {
	R26_NO_OF_ACC1 = r26_NO_OF_ACC1;
}



public BigDecimal getR26_90D_180D_PASTDUE() {
	return R26_90D_180D_PASTDUE;
}



public void setR26_90D_180D_PASTDUE(BigDecimal r26_90d_180d_PASTDUE) {
	R26_90D_180D_PASTDUE = r26_90d_180d_PASTDUE;
}



public BigDecimal getR26_NON_ACCRUALS2() {
	return R26_NON_ACCRUALS2;
}



public void setR26_NON_ACCRUALS2(BigDecimal r26_NON_ACCRUALS2) {
	R26_NON_ACCRUALS2 = r26_NON_ACCRUALS2;
}



public BigDecimal getR26_SPECIFIC_PROV2() {
	return R26_SPECIFIC_PROV2;
}



public void setR26_SPECIFIC_PROV2(BigDecimal r26_SPECIFIC_PROV2) {
	R26_SPECIFIC_PROV2 = r26_SPECIFIC_PROV2;
}



public BigDecimal getR26_NO_OF_ACC2() {
	return R26_NO_OF_ACC2;
}



public void setR26_NO_OF_ACC2(BigDecimal r26_NO_OF_ACC2) {
	R26_NO_OF_ACC2 = r26_NO_OF_ACC2;
}



public BigDecimal getR26_180D_ABOVE_PASTDUE() {
	return R26_180D_ABOVE_PASTDUE;
}



public void setR26_180D_ABOVE_PASTDUE(BigDecimal r26_180d_ABOVE_PASTDUE) {
	R26_180D_ABOVE_PASTDUE = r26_180d_ABOVE_PASTDUE;
}



public BigDecimal getR26_NON_ACCRUALS3() {
	return R26_NON_ACCRUALS3;
}



public void setR26_NON_ACCRUALS3(BigDecimal r26_NON_ACCRUALS3) {
	R26_NON_ACCRUALS3 = r26_NON_ACCRUALS3;
}



public BigDecimal getR26_SPECIFIC_PROV3() {
	return R26_SPECIFIC_PROV3;
}



public void setR26_SPECIFIC_PROV3(BigDecimal r26_SPECIFIC_PROV3) {
	R26_SPECIFIC_PROV3 = r26_SPECIFIC_PROV3;
}



public BigDecimal getR26_NO_OF_ACC3() {
	return R26_NO_OF_ACC3;
}



public void setR26_NO_OF_ACC3(BigDecimal r26_NO_OF_ACC3) {
	R26_NO_OF_ACC3 = r26_NO_OF_ACC3;
}



public BigDecimal getR26_TOTAL_NON_ACCRUAL() {
	return R26_TOTAL_NON_ACCRUAL;
}



public void setR26_TOTAL_NON_ACCRUAL(BigDecimal r26_TOTAL_NON_ACCRUAL) {
	R26_TOTAL_NON_ACCRUAL = r26_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR26_TOTAL_DUE_LOANS() {
	return R26_TOTAL_DUE_LOANS;
}



public void setR26_TOTAL_DUE_LOANS(BigDecimal r26_TOTAL_DUE_LOANS) {
	R26_TOTAL_DUE_LOANS = r26_TOTAL_DUE_LOANS;
}



public BigDecimal getR26_TOTAL_PERFORMING_LOAN() {
	return R26_TOTAL_PERFORMING_LOAN;
}



public void setR26_TOTAL_PERFORMING_LOAN(BigDecimal r26_TOTAL_PERFORMING_LOAN) {
	R26_TOTAL_PERFORMING_LOAN = r26_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR26_VALUE_OF_COLLATERAL() {
	return R26_VALUE_OF_COLLATERAL;
}



public void setR26_VALUE_OF_COLLATERAL(BigDecimal r26_VALUE_OF_COLLATERAL) {
	R26_VALUE_OF_COLLATERAL = r26_VALUE_OF_COLLATERAL;
}



public BigDecimal getR26_TOTAL_VALUE_NPL() {
	return R26_TOTAL_VALUE_NPL;
}



public void setR26_TOTAL_VALUE_NPL(BigDecimal r26_TOTAL_VALUE_NPL) {
	R26_TOTAL_VALUE_NPL = r26_TOTAL_VALUE_NPL;
}



public BigDecimal getR26_TOTAL_SPECIFIC_PROV() {
	return R26_TOTAL_SPECIFIC_PROV;
}



public void setR26_TOTAL_SPECIFIC_PROV(BigDecimal r26_TOTAL_SPECIFIC_PROV) {
	R26_TOTAL_SPECIFIC_PROV = r26_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR26_SPECIFIC_PROV_NPL() {
	return R26_SPECIFIC_PROV_NPL;
}



public void setR26_SPECIFIC_PROV_NPL(BigDecimal r26_SPECIFIC_PROV_NPL) {
	R26_SPECIFIC_PROV_NPL = r26_SPECIFIC_PROV_NPL;
}



public BigDecimal getR27_30D_90D_PASTDUE() {
	return R27_30D_90D_PASTDUE;
}



public void setR27_30D_90D_PASTDUE(BigDecimal r27_30d_90d_PASTDUE) {
	R27_30D_90D_PASTDUE = r27_30d_90d_PASTDUE;
}



public BigDecimal getR27_NON_PERFORM_LOANS() {
	return R27_NON_PERFORM_LOANS;
}



public void setR27_NON_PERFORM_LOANS(BigDecimal r27_NON_PERFORM_LOANS) {
	R27_NON_PERFORM_LOANS = r27_NON_PERFORM_LOANS;
}



public BigDecimal getR27_NON_ACCRUALS1() {
	return R27_NON_ACCRUALS1;
}



public void setR27_NON_ACCRUALS1(BigDecimal r27_NON_ACCRUALS1) {
	R27_NON_ACCRUALS1 = r27_NON_ACCRUALS1;
}



public BigDecimal getR27_SPECIFIC_PROV1() {
	return R27_SPECIFIC_PROV1;
}



public void setR27_SPECIFIC_PROV1(BigDecimal r27_SPECIFIC_PROV1) {
	R27_SPECIFIC_PROV1 = r27_SPECIFIC_PROV1;
}



public BigDecimal getR27_NO_OF_ACC1() {
	return R27_NO_OF_ACC1;
}



public void setR27_NO_OF_ACC1(BigDecimal r27_NO_OF_ACC1) {
	R27_NO_OF_ACC1 = r27_NO_OF_ACC1;
}



public BigDecimal getR27_90D_180D_PASTDUE() {
	return R27_90D_180D_PASTDUE;
}



public void setR27_90D_180D_PASTDUE(BigDecimal r27_90d_180d_PASTDUE) {
	R27_90D_180D_PASTDUE = r27_90d_180d_PASTDUE;
}



public BigDecimal getR27_NON_ACCRUALS2() {
	return R27_NON_ACCRUALS2;
}



public void setR27_NON_ACCRUALS2(BigDecimal r27_NON_ACCRUALS2) {
	R27_NON_ACCRUALS2 = r27_NON_ACCRUALS2;
}



public BigDecimal getR27_SPECIFIC_PROV2() {
	return R27_SPECIFIC_PROV2;
}



public void setR27_SPECIFIC_PROV2(BigDecimal r27_SPECIFIC_PROV2) {
	R27_SPECIFIC_PROV2 = r27_SPECIFIC_PROV2;
}



public BigDecimal getR27_NO_OF_ACC2() {
	return R27_NO_OF_ACC2;
}



public void setR27_NO_OF_ACC2(BigDecimal r27_NO_OF_ACC2) {
	R27_NO_OF_ACC2 = r27_NO_OF_ACC2;
}



public BigDecimal getR27_TOTAL_NON_ACCRUAL() {
	return R27_TOTAL_NON_ACCRUAL;
}



public void setR27_TOTAL_NON_ACCRUAL(BigDecimal r27_TOTAL_NON_ACCRUAL) {
	R27_TOTAL_NON_ACCRUAL = r27_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR27_TOTAL_DUE_LOANS() {
	return R27_TOTAL_DUE_LOANS;
}



public void setR27_TOTAL_DUE_LOANS(BigDecimal r27_TOTAL_DUE_LOANS) {
	R27_TOTAL_DUE_LOANS = r27_TOTAL_DUE_LOANS;
}



public BigDecimal getR27_TOTAL_PERFORMING_LOAN() {
	return R27_TOTAL_PERFORMING_LOAN;
}



public void setR27_TOTAL_PERFORMING_LOAN(BigDecimal r27_TOTAL_PERFORMING_LOAN) {
	R27_TOTAL_PERFORMING_LOAN = r27_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR27_VALUE_OF_COLLATERAL() {
	return R27_VALUE_OF_COLLATERAL;
}



public void setR27_VALUE_OF_COLLATERAL(BigDecimal r27_VALUE_OF_COLLATERAL) {
	R27_VALUE_OF_COLLATERAL = r27_VALUE_OF_COLLATERAL;
}



public BigDecimal getR27_TOTAL_VALUE_NPL() {
	return R27_TOTAL_VALUE_NPL;
}



public void setR27_TOTAL_VALUE_NPL(BigDecimal r27_TOTAL_VALUE_NPL) {
	R27_TOTAL_VALUE_NPL = r27_TOTAL_VALUE_NPL;
}



public BigDecimal getR27_TOTAL_SPECIFIC_PROV() {
	return R27_TOTAL_SPECIFIC_PROV;
}



public void setR27_TOTAL_SPECIFIC_PROV(BigDecimal r27_TOTAL_SPECIFIC_PROV) {
	R27_TOTAL_SPECIFIC_PROV = r27_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR27_SPECIFIC_PROV_NPL() {
	return R27_SPECIFIC_PROV_NPL;
}



public void setR27_SPECIFIC_PROV_NPL(BigDecimal r27_SPECIFIC_PROV_NPL) {
	R27_SPECIFIC_PROV_NPL = r27_SPECIFIC_PROV_NPL;
}



public BigDecimal getR28_30D_90D_PASTDUE() {
	return R28_30D_90D_PASTDUE;
}



public void setR28_30D_90D_PASTDUE(BigDecimal r28_30d_90d_PASTDUE) {
	R28_30D_90D_PASTDUE = r28_30d_90d_PASTDUE;
}



public BigDecimal getR28_NON_PERFORM_LOANS() {
	return R28_NON_PERFORM_LOANS;
}



public void setR28_NON_PERFORM_LOANS(BigDecimal r28_NON_PERFORM_LOANS) {
	R28_NON_PERFORM_LOANS = r28_NON_PERFORM_LOANS;
}



public BigDecimal getR28_NON_ACCRUALS1() {
	return R28_NON_ACCRUALS1;
}



public void setR28_NON_ACCRUALS1(BigDecimal r28_NON_ACCRUALS1) {
	R28_NON_ACCRUALS1 = r28_NON_ACCRUALS1;
}



public BigDecimal getR28_SPECIFIC_PROV1() {
	return R28_SPECIFIC_PROV1;
}



public void setR28_SPECIFIC_PROV1(BigDecimal r28_SPECIFIC_PROV1) {
	R28_SPECIFIC_PROV1 = r28_SPECIFIC_PROV1;
}



public BigDecimal getR28_NO_OF_ACC1() {
	return R28_NO_OF_ACC1;
}



public void setR28_NO_OF_ACC1(BigDecimal r28_NO_OF_ACC1) {
	R28_NO_OF_ACC1 = r28_NO_OF_ACC1;
}



public BigDecimal getR28_90D_180D_PASTDUE() {
	return R28_90D_180D_PASTDUE;
}



public void setR28_90D_180D_PASTDUE(BigDecimal r28_90d_180d_PASTDUE) {
	R28_90D_180D_PASTDUE = r28_90d_180d_PASTDUE;
}



public BigDecimal getR28_NON_ACCRUALS2() {
	return R28_NON_ACCRUALS2;
}



public void setR28_NON_ACCRUALS2(BigDecimal r28_NON_ACCRUALS2) {
	R28_NON_ACCRUALS2 = r28_NON_ACCRUALS2;
}



public BigDecimal getR28_SPECIFIC_PROV2() {
	return R28_SPECIFIC_PROV2;
}



public void setR28_SPECIFIC_PROV2(BigDecimal r28_SPECIFIC_PROV2) {
	R28_SPECIFIC_PROV2 = r28_SPECIFIC_PROV2;
}



public BigDecimal getR28_NO_OF_ACC2() {
	return R28_NO_OF_ACC2;
}



public void setR28_NO_OF_ACC2(BigDecimal r28_NO_OF_ACC2) {
	R28_NO_OF_ACC2 = r28_NO_OF_ACC2;
}



public BigDecimal getR28_TOTAL_NON_ACCRUAL() {
	return R28_TOTAL_NON_ACCRUAL;
}



public void setR28_TOTAL_NON_ACCRUAL(BigDecimal r28_TOTAL_NON_ACCRUAL) {
	R28_TOTAL_NON_ACCRUAL = r28_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR28_TOTAL_DUE_LOANS() {
	return R28_TOTAL_DUE_LOANS;
}



public void setR28_TOTAL_DUE_LOANS(BigDecimal r28_TOTAL_DUE_LOANS) {
	R28_TOTAL_DUE_LOANS = r28_TOTAL_DUE_LOANS;
}



public BigDecimal getR28_TOTAL_PERFORMING_LOAN() {
	return R28_TOTAL_PERFORMING_LOAN;
}



public void setR28_TOTAL_PERFORMING_LOAN(BigDecimal r28_TOTAL_PERFORMING_LOAN) {
	R28_TOTAL_PERFORMING_LOAN = r28_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR28_VALUE_OF_COLLATERAL() {
	return R28_VALUE_OF_COLLATERAL;
}



public void setR28_VALUE_OF_COLLATERAL(BigDecimal r28_VALUE_OF_COLLATERAL) {
	R28_VALUE_OF_COLLATERAL = r28_VALUE_OF_COLLATERAL;
}



public BigDecimal getR28_TOTAL_VALUE_NPL() {
	return R28_TOTAL_VALUE_NPL;
}



public void setR28_TOTAL_VALUE_NPL(BigDecimal r28_TOTAL_VALUE_NPL) {
	R28_TOTAL_VALUE_NPL = r28_TOTAL_VALUE_NPL;
}



public BigDecimal getR28_TOTAL_SPECIFIC_PROV() {
	return R28_TOTAL_SPECIFIC_PROV;
}



public void setR28_TOTAL_SPECIFIC_PROV(BigDecimal r28_TOTAL_SPECIFIC_PROV) {
	R28_TOTAL_SPECIFIC_PROV = r28_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR28_SPECIFIC_PROV_NPL() {
	return R28_SPECIFIC_PROV_NPL;
}



public void setR28_SPECIFIC_PROV_NPL(BigDecimal r28_SPECIFIC_PROV_NPL) {
	R28_SPECIFIC_PROV_NPL = r28_SPECIFIC_PROV_NPL;
}



public BigDecimal getR29_30D_90D_PASTDUE() {
	return R29_30D_90D_PASTDUE;
}



public void setR29_30D_90D_PASTDUE(BigDecimal r29_30d_90d_PASTDUE) {
	R29_30D_90D_PASTDUE = r29_30d_90d_PASTDUE;
}



public BigDecimal getR29_NON_PERFORM_LOANS() {
	return R29_NON_PERFORM_LOANS;
}



public void setR29_NON_PERFORM_LOANS(BigDecimal r29_NON_PERFORM_LOANS) {
	R29_NON_PERFORM_LOANS = r29_NON_PERFORM_LOANS;
}



public BigDecimal getR29_NON_ACCRUALS1() {
	return R29_NON_ACCRUALS1;
}



public void setR29_NON_ACCRUALS1(BigDecimal r29_NON_ACCRUALS1) {
	R29_NON_ACCRUALS1 = r29_NON_ACCRUALS1;
}



public BigDecimal getR29_SPECIFIC_PROV1() {
	return R29_SPECIFIC_PROV1;
}



public void setR29_SPECIFIC_PROV1(BigDecimal r29_SPECIFIC_PROV1) {
	R29_SPECIFIC_PROV1 = r29_SPECIFIC_PROV1;
}



public BigDecimal getR29_NO_OF_ACC1() {
	return R29_NO_OF_ACC1;
}



public void setR29_NO_OF_ACC1(BigDecimal r29_NO_OF_ACC1) {
	R29_NO_OF_ACC1 = r29_NO_OF_ACC1;
}



public BigDecimal getR29_90D_180D_PASTDUE() {
	return R29_90D_180D_PASTDUE;
}



public void setR29_90D_180D_PASTDUE(BigDecimal r29_90d_180d_PASTDUE) {
	R29_90D_180D_PASTDUE = r29_90d_180d_PASTDUE;
}



public BigDecimal getR29_NON_ACCRUALS2() {
	return R29_NON_ACCRUALS2;
}



public void setR29_NON_ACCRUALS2(BigDecimal r29_NON_ACCRUALS2) {
	R29_NON_ACCRUALS2 = r29_NON_ACCRUALS2;
}



public BigDecimal getR29_SPECIFIC_PROV2() {
	return R29_SPECIFIC_PROV2;
}



public void setR29_SPECIFIC_PROV2(BigDecimal r29_SPECIFIC_PROV2) {
	R29_SPECIFIC_PROV2 = r29_SPECIFIC_PROV2;
}



public BigDecimal getR29_NO_OF_ACC2() {
	return R29_NO_OF_ACC2;
}



public void setR29_NO_OF_ACC2(BigDecimal r29_NO_OF_ACC2) {
	R29_NO_OF_ACC2 = r29_NO_OF_ACC2;
}



public BigDecimal getR29_TOTAL_NON_ACCRUAL() {
	return R29_TOTAL_NON_ACCRUAL;
}



public void setR29_TOTAL_NON_ACCRUAL(BigDecimal r29_TOTAL_NON_ACCRUAL) {
	R29_TOTAL_NON_ACCRUAL = r29_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR29_TOTAL_DUE_LOANS() {
	return R29_TOTAL_DUE_LOANS;
}



public void setR29_TOTAL_DUE_LOANS(BigDecimal r29_TOTAL_DUE_LOANS) {
	R29_TOTAL_DUE_LOANS = r29_TOTAL_DUE_LOANS;
}



public BigDecimal getR29_TOTAL_PERFORMING_LOAN() {
	return R29_TOTAL_PERFORMING_LOAN;
}



public void setR29_TOTAL_PERFORMING_LOAN(BigDecimal r29_TOTAL_PERFORMING_LOAN) {
	R29_TOTAL_PERFORMING_LOAN = r29_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR29_VALUE_OF_COLLATERAL() {
	return R29_VALUE_OF_COLLATERAL;
}



public void setR29_VALUE_OF_COLLATERAL(BigDecimal r29_VALUE_OF_COLLATERAL) {
	R29_VALUE_OF_COLLATERAL = r29_VALUE_OF_COLLATERAL;
}



public BigDecimal getR29_TOTAL_VALUE_NPL() {
	return R29_TOTAL_VALUE_NPL;
}



public void setR29_TOTAL_VALUE_NPL(BigDecimal r29_TOTAL_VALUE_NPL) {
	R29_TOTAL_VALUE_NPL = r29_TOTAL_VALUE_NPL;
}



public BigDecimal getR29_TOTAL_SPECIFIC_PROV() {
	return R29_TOTAL_SPECIFIC_PROV;
}



public void setR29_TOTAL_SPECIFIC_PROV(BigDecimal r29_TOTAL_SPECIFIC_PROV) {
	R29_TOTAL_SPECIFIC_PROV = r29_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR29_SPECIFIC_PROV_NPL() {
	return R29_SPECIFIC_PROV_NPL;
}



public void setR29_SPECIFIC_PROV_NPL(BigDecimal r29_SPECIFIC_PROV_NPL) {
	R29_SPECIFIC_PROV_NPL = r29_SPECIFIC_PROV_NPL;
}



public BigDecimal getR30_30D_90D_PASTDUE() {
	return R30_30D_90D_PASTDUE;
}



public void setR30_30D_90D_PASTDUE(BigDecimal r30_30d_90d_PASTDUE) {
	R30_30D_90D_PASTDUE = r30_30d_90d_PASTDUE;
}



public BigDecimal getR30_NON_PERFORM_LOANS() {
	return R30_NON_PERFORM_LOANS;
}



public void setR30_NON_PERFORM_LOANS(BigDecimal r30_NON_PERFORM_LOANS) {
	R30_NON_PERFORM_LOANS = r30_NON_PERFORM_LOANS;
}



public BigDecimal getR30_NON_ACCRUALS1() {
	return R30_NON_ACCRUALS1;
}



public void setR30_NON_ACCRUALS1(BigDecimal r30_NON_ACCRUALS1) {
	R30_NON_ACCRUALS1 = r30_NON_ACCRUALS1;
}



public BigDecimal getR30_SPECIFIC_PROV1() {
	return R30_SPECIFIC_PROV1;
}



public void setR30_SPECIFIC_PROV1(BigDecimal r30_SPECIFIC_PROV1) {
	R30_SPECIFIC_PROV1 = r30_SPECIFIC_PROV1;
}



public BigDecimal getR30_NO_OF_ACC1() {
	return R30_NO_OF_ACC1;
}



public void setR30_NO_OF_ACC1(BigDecimal r30_NO_OF_ACC1) {
	R30_NO_OF_ACC1 = r30_NO_OF_ACC1;
}



public BigDecimal getR30_90D_180D_PASTDUE() {
	return R30_90D_180D_PASTDUE;
}



public void setR30_90D_180D_PASTDUE(BigDecimal r30_90d_180d_PASTDUE) {
	R30_90D_180D_PASTDUE = r30_90d_180d_PASTDUE;
}



public BigDecimal getR30_NON_ACCRUALS2() {
	return R30_NON_ACCRUALS2;
}



public void setR30_NON_ACCRUALS2(BigDecimal r30_NON_ACCRUALS2) {
	R30_NON_ACCRUALS2 = r30_NON_ACCRUALS2;
}



public BigDecimal getR30_SPECIFIC_PROV2() {
	return R30_SPECIFIC_PROV2;
}



public void setR30_SPECIFIC_PROV2(BigDecimal r30_SPECIFIC_PROV2) {
	R30_SPECIFIC_PROV2 = r30_SPECIFIC_PROV2;
}



public BigDecimal getR30_NO_OF_ACC2() {
	return R30_NO_OF_ACC2;
}



public void setR30_NO_OF_ACC2(BigDecimal r30_NO_OF_ACC2) {
	R30_NO_OF_ACC2 = r30_NO_OF_ACC2;
}



public BigDecimal getR30_TOTAL_NON_ACCRUAL() {
	return R30_TOTAL_NON_ACCRUAL;
}



public void setR30_TOTAL_NON_ACCRUAL(BigDecimal r30_TOTAL_NON_ACCRUAL) {
	R30_TOTAL_NON_ACCRUAL = r30_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR30_TOTAL_DUE_LOANS() {
	return R30_TOTAL_DUE_LOANS;
}



public void setR30_TOTAL_DUE_LOANS(BigDecimal r30_TOTAL_DUE_LOANS) {
	R30_TOTAL_DUE_LOANS = r30_TOTAL_DUE_LOANS;
}



public BigDecimal getR30_TOTAL_PERFORMING_LOAN() {
	return R30_TOTAL_PERFORMING_LOAN;
}



public void setR30_TOTAL_PERFORMING_LOAN(BigDecimal r30_TOTAL_PERFORMING_LOAN) {
	R30_TOTAL_PERFORMING_LOAN = r30_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR30_VALUE_OF_COLLATERAL() {
	return R30_VALUE_OF_COLLATERAL;
}



public void setR30_VALUE_OF_COLLATERAL(BigDecimal r30_VALUE_OF_COLLATERAL) {
	R30_VALUE_OF_COLLATERAL = r30_VALUE_OF_COLLATERAL;
}



public BigDecimal getR30_TOTAL_VALUE_NPL() {
	return R30_TOTAL_VALUE_NPL;
}



public void setR30_TOTAL_VALUE_NPL(BigDecimal r30_TOTAL_VALUE_NPL) {
	R30_TOTAL_VALUE_NPL = r30_TOTAL_VALUE_NPL;
}



public BigDecimal getR30_TOTAL_SPECIFIC_PROV() {
	return R30_TOTAL_SPECIFIC_PROV;
}



public void setR30_TOTAL_SPECIFIC_PROV(BigDecimal r30_TOTAL_SPECIFIC_PROV) {
	R30_TOTAL_SPECIFIC_PROV = r30_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR30_SPECIFIC_PROV_NPL() {
	return R30_SPECIFIC_PROV_NPL;
}



public void setR30_SPECIFIC_PROV_NPL(BigDecimal r30_SPECIFIC_PROV_NPL) {
	R30_SPECIFIC_PROV_NPL = r30_SPECIFIC_PROV_NPL;
}



public BigDecimal getR31_30D_90D_PASTDUE() {
	return R31_30D_90D_PASTDUE;
}



public void setR31_30D_90D_PASTDUE(BigDecimal r31_30d_90d_PASTDUE) {
	R31_30D_90D_PASTDUE = r31_30d_90d_PASTDUE;
}



public BigDecimal getR31_NON_PERFORM_LOANS() {
	return R31_NON_PERFORM_LOANS;
}



public void setR31_NON_PERFORM_LOANS(BigDecimal r31_NON_PERFORM_LOANS) {
	R31_NON_PERFORM_LOANS = r31_NON_PERFORM_LOANS;
}



public BigDecimal getR31_NON_ACCRUALS1() {
	return R31_NON_ACCRUALS1;
}



public void setR31_NON_ACCRUALS1(BigDecimal r31_NON_ACCRUALS1) {
	R31_NON_ACCRUALS1 = r31_NON_ACCRUALS1;
}



public BigDecimal getR31_SPECIFIC_PROV1() {
	return R31_SPECIFIC_PROV1;
}



public void setR31_SPECIFIC_PROV1(BigDecimal r31_SPECIFIC_PROV1) {
	R31_SPECIFIC_PROV1 = r31_SPECIFIC_PROV1;
}



public BigDecimal getR31_NO_OF_ACC1() {
	return R31_NO_OF_ACC1;
}



public void setR31_NO_OF_ACC1(BigDecimal r31_NO_OF_ACC1) {
	R31_NO_OF_ACC1 = r31_NO_OF_ACC1;
}



public BigDecimal getR31_90D_180D_PASTDUE() {
	return R31_90D_180D_PASTDUE;
}



public void setR31_90D_180D_PASTDUE(BigDecimal r31_90d_180d_PASTDUE) {
	R31_90D_180D_PASTDUE = r31_90d_180d_PASTDUE;
}



public BigDecimal getR31_NON_ACCRUALS2() {
	return R31_NON_ACCRUALS2;
}



public void setR31_NON_ACCRUALS2(BigDecimal r31_NON_ACCRUALS2) {
	R31_NON_ACCRUALS2 = r31_NON_ACCRUALS2;
}



public BigDecimal getR31_SPECIFIC_PROV2() {
	return R31_SPECIFIC_PROV2;
}



public void setR31_SPECIFIC_PROV2(BigDecimal r31_SPECIFIC_PROV2) {
	R31_SPECIFIC_PROV2 = r31_SPECIFIC_PROV2;
}



public BigDecimal getR31_NO_OF_ACC2() {
	return R31_NO_OF_ACC2;
}



public void setR31_NO_OF_ACC2(BigDecimal r31_NO_OF_ACC2) {
	R31_NO_OF_ACC2 = r31_NO_OF_ACC2;
}



public BigDecimal getR31_TOTAL_NON_ACCRUAL() {
	return R31_TOTAL_NON_ACCRUAL;
}



public void setR31_TOTAL_NON_ACCRUAL(BigDecimal r31_TOTAL_NON_ACCRUAL) {
	R31_TOTAL_NON_ACCRUAL = r31_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR31_TOTAL_DUE_LOANS() {
	return R31_TOTAL_DUE_LOANS;
}



public void setR31_TOTAL_DUE_LOANS(BigDecimal r31_TOTAL_DUE_LOANS) {
	R31_TOTAL_DUE_LOANS = r31_TOTAL_DUE_LOANS;
}



public BigDecimal getR31_TOTAL_PERFORMING_LOAN() {
	return R31_TOTAL_PERFORMING_LOAN;
}



public void setR31_TOTAL_PERFORMING_LOAN(BigDecimal r31_TOTAL_PERFORMING_LOAN) {
	R31_TOTAL_PERFORMING_LOAN = r31_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR31_VALUE_OF_COLLATERAL() {
	return R31_VALUE_OF_COLLATERAL;
}



public void setR31_VALUE_OF_COLLATERAL(BigDecimal r31_VALUE_OF_COLLATERAL) {
	R31_VALUE_OF_COLLATERAL = r31_VALUE_OF_COLLATERAL;
}



public BigDecimal getR31_TOTAL_VALUE_NPL() {
	return R31_TOTAL_VALUE_NPL;
}



public void setR31_TOTAL_VALUE_NPL(BigDecimal r31_TOTAL_VALUE_NPL) {
	R31_TOTAL_VALUE_NPL = r31_TOTAL_VALUE_NPL;
}



public BigDecimal getR31_TOTAL_SPECIFIC_PROV() {
	return R31_TOTAL_SPECIFIC_PROV;
}



public void setR31_TOTAL_SPECIFIC_PROV(BigDecimal r31_TOTAL_SPECIFIC_PROV) {
	R31_TOTAL_SPECIFIC_PROV = r31_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR31_SPECIFIC_PROV_NPL() {
	return R31_SPECIFIC_PROV_NPL;
}



public void setR31_SPECIFIC_PROV_NPL(BigDecimal r31_SPECIFIC_PROV_NPL) {
	R31_SPECIFIC_PROV_NPL = r31_SPECIFIC_PROV_NPL;
}



public BigDecimal getR32_30D_90D_PASTDUE() {
	return R32_30D_90D_PASTDUE;
}



public void setR32_30D_90D_PASTDUE(BigDecimal r32_30d_90d_PASTDUE) {
	R32_30D_90D_PASTDUE = r32_30d_90d_PASTDUE;
}



public BigDecimal getR32_NON_PERFORM_LOANS() {
	return R32_NON_PERFORM_LOANS;
}



public void setR32_NON_PERFORM_LOANS(BigDecimal r32_NON_PERFORM_LOANS) {
	R32_NON_PERFORM_LOANS = r32_NON_PERFORM_LOANS;
}



public BigDecimal getR32_NON_ACCRUALS1() {
	return R32_NON_ACCRUALS1;
}



public void setR32_NON_ACCRUALS1(BigDecimal r32_NON_ACCRUALS1) {
	R32_NON_ACCRUALS1 = r32_NON_ACCRUALS1;
}



public BigDecimal getR32_SPECIFIC_PROV1() {
	return R32_SPECIFIC_PROV1;
}



public void setR32_SPECIFIC_PROV1(BigDecimal r32_SPECIFIC_PROV1) {
	R32_SPECIFIC_PROV1 = r32_SPECIFIC_PROV1;
}



public BigDecimal getR32_NO_OF_ACC1() {
	return R32_NO_OF_ACC1;
}



public void setR32_NO_OF_ACC1(BigDecimal r32_NO_OF_ACC1) {
	R32_NO_OF_ACC1 = r32_NO_OF_ACC1;
}



public BigDecimal getR32_90D_180D_PASTDUE() {
	return R32_90D_180D_PASTDUE;
}



public void setR32_90D_180D_PASTDUE(BigDecimal r32_90d_180d_PASTDUE) {
	R32_90D_180D_PASTDUE = r32_90d_180d_PASTDUE;
}



public BigDecimal getR32_NON_ACCRUALS2() {
	return R32_NON_ACCRUALS2;
}



public void setR32_NON_ACCRUALS2(BigDecimal r32_NON_ACCRUALS2) {
	R32_NON_ACCRUALS2 = r32_NON_ACCRUALS2;
}



public BigDecimal getR32_SPECIFIC_PROV2() {
	return R32_SPECIFIC_PROV2;
}



public void setR32_SPECIFIC_PROV2(BigDecimal r32_SPECIFIC_PROV2) {
	R32_SPECIFIC_PROV2 = r32_SPECIFIC_PROV2;
}



public BigDecimal getR32_NO_OF_ACC2() {
	return R32_NO_OF_ACC2;
}



public void setR32_NO_OF_ACC2(BigDecimal r32_NO_OF_ACC2) {
	R32_NO_OF_ACC2 = r32_NO_OF_ACC2;
}



public BigDecimal getR32_TOTAL_NON_ACCRUAL() {
	return R32_TOTAL_NON_ACCRUAL;
}



public void setR32_TOTAL_NON_ACCRUAL(BigDecimal r32_TOTAL_NON_ACCRUAL) {
	R32_TOTAL_NON_ACCRUAL = r32_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR32_TOTAL_DUE_LOANS() {
	return R32_TOTAL_DUE_LOANS;
}



public void setR32_TOTAL_DUE_LOANS(BigDecimal r32_TOTAL_DUE_LOANS) {
	R32_TOTAL_DUE_LOANS = r32_TOTAL_DUE_LOANS;
}



public BigDecimal getR32_TOTAL_PERFORMING_LOAN() {
	return R32_TOTAL_PERFORMING_LOAN;
}



public void setR32_TOTAL_PERFORMING_LOAN(BigDecimal r32_TOTAL_PERFORMING_LOAN) {
	R32_TOTAL_PERFORMING_LOAN = r32_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR32_VALUE_OF_COLLATERAL() {
	return R32_VALUE_OF_COLLATERAL;
}



public void setR32_VALUE_OF_COLLATERAL(BigDecimal r32_VALUE_OF_COLLATERAL) {
	R32_VALUE_OF_COLLATERAL = r32_VALUE_OF_COLLATERAL;
}



public BigDecimal getR32_TOTAL_VALUE_NPL() {
	return R32_TOTAL_VALUE_NPL;
}



public void setR32_TOTAL_VALUE_NPL(BigDecimal r32_TOTAL_VALUE_NPL) {
	R32_TOTAL_VALUE_NPL = r32_TOTAL_VALUE_NPL;
}



public BigDecimal getR32_TOTAL_SPECIFIC_PROV() {
	return R32_TOTAL_SPECIFIC_PROV;
}



public void setR32_TOTAL_SPECIFIC_PROV(BigDecimal r32_TOTAL_SPECIFIC_PROV) {
	R32_TOTAL_SPECIFIC_PROV = r32_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR32_SPECIFIC_PROV_NPL() {
	return R32_SPECIFIC_PROV_NPL;
}



public void setR32_SPECIFIC_PROV_NPL(BigDecimal r32_SPECIFIC_PROV_NPL) {
	R32_SPECIFIC_PROV_NPL = r32_SPECIFIC_PROV_NPL;
}



public BigDecimal getR33_30D_90D_PASTDUE() {
	return R33_30D_90D_PASTDUE;
}



public void setR33_30D_90D_PASTDUE(BigDecimal r33_30d_90d_PASTDUE) {
	R33_30D_90D_PASTDUE = r33_30d_90d_PASTDUE;
}



public BigDecimal getR33_NON_PERFORM_LOANS() {
	return R33_NON_PERFORM_LOANS;
}



public void setR33_NON_PERFORM_LOANS(BigDecimal r33_NON_PERFORM_LOANS) {
	R33_NON_PERFORM_LOANS = r33_NON_PERFORM_LOANS;
}



public BigDecimal getR33_NON_ACCRUALS1() {
	return R33_NON_ACCRUALS1;
}



public void setR33_NON_ACCRUALS1(BigDecimal r33_NON_ACCRUALS1) {
	R33_NON_ACCRUALS1 = r33_NON_ACCRUALS1;
}



public BigDecimal getR33_SPECIFIC_PROV1() {
	return R33_SPECIFIC_PROV1;
}



public void setR33_SPECIFIC_PROV1(BigDecimal r33_SPECIFIC_PROV1) {
	R33_SPECIFIC_PROV1 = r33_SPECIFIC_PROV1;
}



public BigDecimal getR33_NO_OF_ACC1() {
	return R33_NO_OF_ACC1;
}



public void setR33_NO_OF_ACC1(BigDecimal r33_NO_OF_ACC1) {
	R33_NO_OF_ACC1 = r33_NO_OF_ACC1;
}



public BigDecimal getR33_90D_180D_PASTDUE() {
	return R33_90D_180D_PASTDUE;
}



public void setR33_90D_180D_PASTDUE(BigDecimal r33_90d_180d_PASTDUE) {
	R33_90D_180D_PASTDUE = r33_90d_180d_PASTDUE;
}



public BigDecimal getR33_NON_ACCRUALS2() {
	return R33_NON_ACCRUALS2;
}



public void setR33_NON_ACCRUALS2(BigDecimal r33_NON_ACCRUALS2) {
	R33_NON_ACCRUALS2 = r33_NON_ACCRUALS2;
}



public BigDecimal getR33_SPECIFIC_PROV2() {
	return R33_SPECIFIC_PROV2;
}



public void setR33_SPECIFIC_PROV2(BigDecimal r33_SPECIFIC_PROV2) {
	R33_SPECIFIC_PROV2 = r33_SPECIFIC_PROV2;
}



public BigDecimal getR33_NO_OF_ACC2() {
	return R33_NO_OF_ACC2;
}



public void setR33_NO_OF_ACC2(BigDecimal r33_NO_OF_ACC2) {
	R33_NO_OF_ACC2 = r33_NO_OF_ACC2;
}



public BigDecimal getR33_TOTAL_NON_ACCRUAL() {
	return R33_TOTAL_NON_ACCRUAL;
}



public void setR33_TOTAL_NON_ACCRUAL(BigDecimal r33_TOTAL_NON_ACCRUAL) {
	R33_TOTAL_NON_ACCRUAL = r33_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR33_TOTAL_DUE_LOANS() {
	return R33_TOTAL_DUE_LOANS;
}



public void setR33_TOTAL_DUE_LOANS(BigDecimal r33_TOTAL_DUE_LOANS) {
	R33_TOTAL_DUE_LOANS = r33_TOTAL_DUE_LOANS;
}



public BigDecimal getR33_TOTAL_PERFORMING_LOAN() {
	return R33_TOTAL_PERFORMING_LOAN;
}



public void setR33_TOTAL_PERFORMING_LOAN(BigDecimal r33_TOTAL_PERFORMING_LOAN) {
	R33_TOTAL_PERFORMING_LOAN = r33_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR33_VALUE_OF_COLLATERAL() {
	return R33_VALUE_OF_COLLATERAL;
}



public void setR33_VALUE_OF_COLLATERAL(BigDecimal r33_VALUE_OF_COLLATERAL) {
	R33_VALUE_OF_COLLATERAL = r33_VALUE_OF_COLLATERAL;
}



public BigDecimal getR33_TOTAL_VALUE_NPL() {
	return R33_TOTAL_VALUE_NPL;
}



public void setR33_TOTAL_VALUE_NPL(BigDecimal r33_TOTAL_VALUE_NPL) {
	R33_TOTAL_VALUE_NPL = r33_TOTAL_VALUE_NPL;
}



public BigDecimal getR33_TOTAL_SPECIFIC_PROV() {
	return R33_TOTAL_SPECIFIC_PROV;
}



public void setR33_TOTAL_SPECIFIC_PROV(BigDecimal r33_TOTAL_SPECIFIC_PROV) {
	R33_TOTAL_SPECIFIC_PROV = r33_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR33_SPECIFIC_PROV_NPL() {
	return R33_SPECIFIC_PROV_NPL;
}



public void setR33_SPECIFIC_PROV_NPL(BigDecimal r33_SPECIFIC_PROV_NPL) {
	R33_SPECIFIC_PROV_NPL = r33_SPECIFIC_PROV_NPL;
}



public BigDecimal getR34_30D_90D_PASTDUE() {
	return R34_30D_90D_PASTDUE;
}



public void setR34_30D_90D_PASTDUE(BigDecimal r34_30d_90d_PASTDUE) {
	R34_30D_90D_PASTDUE = r34_30d_90d_PASTDUE;
}



public BigDecimal getR34_NON_PERFORM_LOANS() {
	return R34_NON_PERFORM_LOANS;
}



public void setR34_NON_PERFORM_LOANS(BigDecimal r34_NON_PERFORM_LOANS) {
	R34_NON_PERFORM_LOANS = r34_NON_PERFORM_LOANS;
}



public BigDecimal getR34_NON_ACCRUALS1() {
	return R34_NON_ACCRUALS1;
}



public void setR34_NON_ACCRUALS1(BigDecimal r34_NON_ACCRUALS1) {
	R34_NON_ACCRUALS1 = r34_NON_ACCRUALS1;
}



public BigDecimal getR34_SPECIFIC_PROV1() {
	return R34_SPECIFIC_PROV1;
}



public void setR34_SPECIFIC_PROV1(BigDecimal r34_SPECIFIC_PROV1) {
	R34_SPECIFIC_PROV1 = r34_SPECIFIC_PROV1;
}



public BigDecimal getR34_NO_OF_ACC1() {
	return R34_NO_OF_ACC1;
}



public void setR34_NO_OF_ACC1(BigDecimal r34_NO_OF_ACC1) {
	R34_NO_OF_ACC1 = r34_NO_OF_ACC1;
}



public BigDecimal getR34_90D_180D_PASTDUE() {
	return R34_90D_180D_PASTDUE;
}



public void setR34_90D_180D_PASTDUE(BigDecimal r34_90d_180d_PASTDUE) {
	R34_90D_180D_PASTDUE = r34_90d_180d_PASTDUE;
}



public BigDecimal getR34_NON_ACCRUALS2() {
	return R34_NON_ACCRUALS2;
}



public void setR34_NON_ACCRUALS2(BigDecimal r34_NON_ACCRUALS2) {
	R34_NON_ACCRUALS2 = r34_NON_ACCRUALS2;
}



public BigDecimal getR34_SPECIFIC_PROV2() {
	return R34_SPECIFIC_PROV2;
}



public void setR34_SPECIFIC_PROV2(BigDecimal r34_SPECIFIC_PROV2) {
	R34_SPECIFIC_PROV2 = r34_SPECIFIC_PROV2;
}



public BigDecimal getR34_NO_OF_ACC2() {
	return R34_NO_OF_ACC2;
}



public void setR34_NO_OF_ACC2(BigDecimal r34_NO_OF_ACC2) {
	R34_NO_OF_ACC2 = r34_NO_OF_ACC2;
}



public BigDecimal getR34_TOTAL_NON_ACCRUAL() {
	return R34_TOTAL_NON_ACCRUAL;
}



public void setR34_TOTAL_NON_ACCRUAL(BigDecimal r34_TOTAL_NON_ACCRUAL) {
	R34_TOTAL_NON_ACCRUAL = r34_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR34_TOTAL_DUE_LOANS() {
	return R34_TOTAL_DUE_LOANS;
}



public void setR34_TOTAL_DUE_LOANS(BigDecimal r34_TOTAL_DUE_LOANS) {
	R34_TOTAL_DUE_LOANS = r34_TOTAL_DUE_LOANS;
}



public BigDecimal getR34_TOTAL_PERFORMING_LOAN() {
	return R34_TOTAL_PERFORMING_LOAN;
}



public void setR34_TOTAL_PERFORMING_LOAN(BigDecimal r34_TOTAL_PERFORMING_LOAN) {
	R34_TOTAL_PERFORMING_LOAN = r34_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR34_VALUE_OF_COLLATERAL() {
	return R34_VALUE_OF_COLLATERAL;
}



public void setR34_VALUE_OF_COLLATERAL(BigDecimal r34_VALUE_OF_COLLATERAL) {
	R34_VALUE_OF_COLLATERAL = r34_VALUE_OF_COLLATERAL;
}



public BigDecimal getR34_TOTAL_VALUE_NPL() {
	return R34_TOTAL_VALUE_NPL;
}



public void setR34_TOTAL_VALUE_NPL(BigDecimal r34_TOTAL_VALUE_NPL) {
	R34_TOTAL_VALUE_NPL = r34_TOTAL_VALUE_NPL;
}



public BigDecimal getR34_TOTAL_SPECIFIC_PROV() {
	return R34_TOTAL_SPECIFIC_PROV;
}



public void setR34_TOTAL_SPECIFIC_PROV(BigDecimal r34_TOTAL_SPECIFIC_PROV) {
	R34_TOTAL_SPECIFIC_PROV = r34_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR34_SPECIFIC_PROV_NPL() {
	return R34_SPECIFIC_PROV_NPL;
}



public void setR34_SPECIFIC_PROV_NPL(BigDecimal r34_SPECIFIC_PROV_NPL) {
	R34_SPECIFIC_PROV_NPL = r34_SPECIFIC_PROV_NPL;
}



public BigDecimal getR35_30D_90D_PASTDUE() {
	return R35_30D_90D_PASTDUE;
}



public void setR35_30D_90D_PASTDUE(BigDecimal r35_30d_90d_PASTDUE) {
	R35_30D_90D_PASTDUE = r35_30d_90d_PASTDUE;
}



public BigDecimal getR35_NON_PERFORM_LOANS() {
	return R35_NON_PERFORM_LOANS;
}



public void setR35_NON_PERFORM_LOANS(BigDecimal r35_NON_PERFORM_LOANS) {
	R35_NON_PERFORM_LOANS = r35_NON_PERFORM_LOANS;
}



public BigDecimal getR35_NON_ACCRUALS1() {
	return R35_NON_ACCRUALS1;
}



public void setR35_NON_ACCRUALS1(BigDecimal r35_NON_ACCRUALS1) {
	R35_NON_ACCRUALS1 = r35_NON_ACCRUALS1;
}



public BigDecimal getR35_SPECIFIC_PROV1() {
	return R35_SPECIFIC_PROV1;
}



public void setR35_SPECIFIC_PROV1(BigDecimal r35_SPECIFIC_PROV1) {
	R35_SPECIFIC_PROV1 = r35_SPECIFIC_PROV1;
}



public BigDecimal getR35_NO_OF_ACC1() {
	return R35_NO_OF_ACC1;
}



public void setR35_NO_OF_ACC1(BigDecimal r35_NO_OF_ACC1) {
	R35_NO_OF_ACC1 = r35_NO_OF_ACC1;
}



public BigDecimal getR35_90D_180D_PASTDUE() {
	return R35_90D_180D_PASTDUE;
}



public void setR35_90D_180D_PASTDUE(BigDecimal r35_90d_180d_PASTDUE) {
	R35_90D_180D_PASTDUE = r35_90d_180d_PASTDUE;
}



public BigDecimal getR35_NON_ACCRUALS2() {
	return R35_NON_ACCRUALS2;
}



public void setR35_NON_ACCRUALS2(BigDecimal r35_NON_ACCRUALS2) {
	R35_NON_ACCRUALS2 = r35_NON_ACCRUALS2;
}



public BigDecimal getR35_SPECIFIC_PROV2() {
	return R35_SPECIFIC_PROV2;
}



public void setR35_SPECIFIC_PROV2(BigDecimal r35_SPECIFIC_PROV2) {
	R35_SPECIFIC_PROV2 = r35_SPECIFIC_PROV2;
}



public BigDecimal getR35_NO_OF_ACC2() {
	return R35_NO_OF_ACC2;
}



public void setR35_NO_OF_ACC2(BigDecimal r35_NO_OF_ACC2) {
	R35_NO_OF_ACC2 = r35_NO_OF_ACC2;
}



public BigDecimal getR35_180D_ABOVE_PASTDUE() {
	return R35_180D_ABOVE_PASTDUE;
}



public void setR35_180D_ABOVE_PASTDUE(BigDecimal r35_180d_ABOVE_PASTDUE) {
	R35_180D_ABOVE_PASTDUE = r35_180d_ABOVE_PASTDUE;
}



public BigDecimal getR35_NON_ACCRUALS3() {
	return R35_NON_ACCRUALS3;
}



public void setR35_NON_ACCRUALS3(BigDecimal r35_NON_ACCRUALS3) {
	R35_NON_ACCRUALS3 = r35_NON_ACCRUALS3;
}



public BigDecimal getR35_SPECIFIC_PROV3() {
	return R35_SPECIFIC_PROV3;
}



public void setR35_SPECIFIC_PROV3(BigDecimal r35_SPECIFIC_PROV3) {
	R35_SPECIFIC_PROV3 = r35_SPECIFIC_PROV3;
}



public BigDecimal getR35_NO_OF_ACC3() {
	return R35_NO_OF_ACC3;
}



public void setR35_NO_OF_ACC3(BigDecimal r35_NO_OF_ACC3) {
	R35_NO_OF_ACC3 = r35_NO_OF_ACC3;
}



public BigDecimal getR35_TOTAL_NON_ACCRUAL() {
	return R35_TOTAL_NON_ACCRUAL;
}



public void setR35_TOTAL_NON_ACCRUAL(BigDecimal r35_TOTAL_NON_ACCRUAL) {
	R35_TOTAL_NON_ACCRUAL = r35_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR35_TOTAL_DUE_LOANS() {
	return R35_TOTAL_DUE_LOANS;
}



public void setR35_TOTAL_DUE_LOANS(BigDecimal r35_TOTAL_DUE_LOANS) {
	R35_TOTAL_DUE_LOANS = r35_TOTAL_DUE_LOANS;
}



public BigDecimal getR35_TOTAL_PERFORMING_LOAN() {
	return R35_TOTAL_PERFORMING_LOAN;
}



public void setR35_TOTAL_PERFORMING_LOAN(BigDecimal r35_TOTAL_PERFORMING_LOAN) {
	R35_TOTAL_PERFORMING_LOAN = r35_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR35_VALUE_OF_COLLATERAL() {
	return R35_VALUE_OF_COLLATERAL;
}



public void setR35_VALUE_OF_COLLATERAL(BigDecimal r35_VALUE_OF_COLLATERAL) {
	R35_VALUE_OF_COLLATERAL = r35_VALUE_OF_COLLATERAL;
}



public BigDecimal getR35_TOTAL_VALUE_NPL() {
	return R35_TOTAL_VALUE_NPL;
}



public void setR35_TOTAL_VALUE_NPL(BigDecimal r35_TOTAL_VALUE_NPL) {
	R35_TOTAL_VALUE_NPL = r35_TOTAL_VALUE_NPL;
}



public BigDecimal getR35_TOTAL_SPECIFIC_PROV() {
	return R35_TOTAL_SPECIFIC_PROV;
}



public void setR35_TOTAL_SPECIFIC_PROV(BigDecimal r35_TOTAL_SPECIFIC_PROV) {
	R35_TOTAL_SPECIFIC_PROV = r35_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR35_SPECIFIC_PROV_NPL() {
	return R35_SPECIFIC_PROV_NPL;
}



public void setR35_SPECIFIC_PROV_NPL(BigDecimal r35_SPECIFIC_PROV_NPL) {
	R35_SPECIFIC_PROV_NPL = r35_SPECIFIC_PROV_NPL;
}



public BigDecimal getR36_30D_90D_PASTDUE() {
	return R36_30D_90D_PASTDUE;
}



public void setR36_30D_90D_PASTDUE(BigDecimal r36_30d_90d_PASTDUE) {
	R36_30D_90D_PASTDUE = r36_30d_90d_PASTDUE;
}



public BigDecimal getR36_NON_PERFORM_LOANS() {
	return R36_NON_PERFORM_LOANS;
}



public void setR36_NON_PERFORM_LOANS(BigDecimal r36_NON_PERFORM_LOANS) {
	R36_NON_PERFORM_LOANS = r36_NON_PERFORM_LOANS;
}



public BigDecimal getR36_NON_ACCRUALS1() {
	return R36_NON_ACCRUALS1;
}



public void setR36_NON_ACCRUALS1(BigDecimal r36_NON_ACCRUALS1) {
	R36_NON_ACCRUALS1 = r36_NON_ACCRUALS1;
}



public BigDecimal getR36_SPECIFIC_PROV1() {
	return R36_SPECIFIC_PROV1;
}



public void setR36_SPECIFIC_PROV1(BigDecimal r36_SPECIFIC_PROV1) {
	R36_SPECIFIC_PROV1 = r36_SPECIFIC_PROV1;
}



public BigDecimal getR36_NO_OF_ACC1() {
	return R36_NO_OF_ACC1;
}



public void setR36_NO_OF_ACC1(BigDecimal r36_NO_OF_ACC1) {
	R36_NO_OF_ACC1 = r36_NO_OF_ACC1;
}



public BigDecimal getR36_90D_180D_PASTDUE() {
	return R36_90D_180D_PASTDUE;
}



public void setR36_90D_180D_PASTDUE(BigDecimal r36_90d_180d_PASTDUE) {
	R36_90D_180D_PASTDUE = r36_90d_180d_PASTDUE;
}



public BigDecimal getR36_NON_ACCRUALS2() {
	return R36_NON_ACCRUALS2;
}



public void setR36_NON_ACCRUALS2(BigDecimal r36_NON_ACCRUALS2) {
	R36_NON_ACCRUALS2 = r36_NON_ACCRUALS2;
}



public BigDecimal getR36_SPECIFIC_PROV2() {
	return R36_SPECIFIC_PROV2;
}



public void setR36_SPECIFIC_PROV2(BigDecimal r36_SPECIFIC_PROV2) {
	R36_SPECIFIC_PROV2 = r36_SPECIFIC_PROV2;
}



public BigDecimal getR36_NO_OF_ACC2() {
	return R36_NO_OF_ACC2;
}



public void setR36_NO_OF_ACC2(BigDecimal r36_NO_OF_ACC2) {
	R36_NO_OF_ACC2 = r36_NO_OF_ACC2;
}



public BigDecimal getR36_TOTAL_NON_ACCRUAL() {
	return R36_TOTAL_NON_ACCRUAL;
}



public void setR36_TOTAL_NON_ACCRUAL(BigDecimal r36_TOTAL_NON_ACCRUAL) {
	R36_TOTAL_NON_ACCRUAL = r36_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR36_TOTAL_DUE_LOANS() {
	return R36_TOTAL_DUE_LOANS;
}



public void setR36_TOTAL_DUE_LOANS(BigDecimal r36_TOTAL_DUE_LOANS) {
	R36_TOTAL_DUE_LOANS = r36_TOTAL_DUE_LOANS;
}



public BigDecimal getR36_TOTAL_PERFORMING_LOAN() {
	return R36_TOTAL_PERFORMING_LOAN;
}



public void setR36_TOTAL_PERFORMING_LOAN(BigDecimal r36_TOTAL_PERFORMING_LOAN) {
	R36_TOTAL_PERFORMING_LOAN = r36_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR36_VALUE_OF_COLLATERAL() {
	return R36_VALUE_OF_COLLATERAL;
}



public void setR36_VALUE_OF_COLLATERAL(BigDecimal r36_VALUE_OF_COLLATERAL) {
	R36_VALUE_OF_COLLATERAL = r36_VALUE_OF_COLLATERAL;
}



public BigDecimal getR36_TOTAL_VALUE_NPL() {
	return R36_TOTAL_VALUE_NPL;
}



public void setR36_TOTAL_VALUE_NPL(BigDecimal r36_TOTAL_VALUE_NPL) {
	R36_TOTAL_VALUE_NPL = r36_TOTAL_VALUE_NPL;
}



public BigDecimal getR36_TOTAL_SPECIFIC_PROV() {
	return R36_TOTAL_SPECIFIC_PROV;
}



public void setR36_TOTAL_SPECIFIC_PROV(BigDecimal r36_TOTAL_SPECIFIC_PROV) {
	R36_TOTAL_SPECIFIC_PROV = r36_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR36_SPECIFIC_PROV_NPL() {
	return R36_SPECIFIC_PROV_NPL;
}



public void setR36_SPECIFIC_PROV_NPL(BigDecimal r36_SPECIFIC_PROV_NPL) {
	R36_SPECIFIC_PROV_NPL = r36_SPECIFIC_PROV_NPL;
}



public BigDecimal getR37_30D_90D_PASTDUE() {
	return R37_30D_90D_PASTDUE;
}



public void setR37_30D_90D_PASTDUE(BigDecimal r37_30d_90d_PASTDUE) {
	R37_30D_90D_PASTDUE = r37_30d_90d_PASTDUE;
}



public BigDecimal getR37_NON_PERFORM_LOANS() {
	return R37_NON_PERFORM_LOANS;
}



public void setR37_NON_PERFORM_LOANS(BigDecimal r37_NON_PERFORM_LOANS) {
	R37_NON_PERFORM_LOANS = r37_NON_PERFORM_LOANS;
}



public BigDecimal getR37_NON_ACCRUALS1() {
	return R37_NON_ACCRUALS1;
}



public void setR37_NON_ACCRUALS1(BigDecimal r37_NON_ACCRUALS1) {
	R37_NON_ACCRUALS1 = r37_NON_ACCRUALS1;
}



public BigDecimal getR37_SPECIFIC_PROV1() {
	return R37_SPECIFIC_PROV1;
}



public void setR37_SPECIFIC_PROV1(BigDecimal r37_SPECIFIC_PROV1) {
	R37_SPECIFIC_PROV1 = r37_SPECIFIC_PROV1;
}



public BigDecimal getR37_NO_OF_ACC1() {
	return R37_NO_OF_ACC1;
}



public void setR37_NO_OF_ACC1(BigDecimal r37_NO_OF_ACC1) {
	R37_NO_OF_ACC1 = r37_NO_OF_ACC1;
}



public BigDecimal getR37_90D_180D_PASTDUE() {
	return R37_90D_180D_PASTDUE;
}



public void setR37_90D_180D_PASTDUE(BigDecimal r37_90d_180d_PASTDUE) {
	R37_90D_180D_PASTDUE = r37_90d_180d_PASTDUE;
}



public BigDecimal getR37_NON_ACCRUALS2() {
	return R37_NON_ACCRUALS2;
}



public void setR37_NON_ACCRUALS2(BigDecimal r37_NON_ACCRUALS2) {
	R37_NON_ACCRUALS2 = r37_NON_ACCRUALS2;
}



public BigDecimal getR37_SPECIFIC_PROV2() {
	return R37_SPECIFIC_PROV2;
}



public void setR37_SPECIFIC_PROV2(BigDecimal r37_SPECIFIC_PROV2) {
	R37_SPECIFIC_PROV2 = r37_SPECIFIC_PROV2;
}



public BigDecimal getR37_NO_OF_ACC2() {
	return R37_NO_OF_ACC2;
}



public void setR37_NO_OF_ACC2(BigDecimal r37_NO_OF_ACC2) {
	R37_NO_OF_ACC2 = r37_NO_OF_ACC2;
}



public BigDecimal getR37_TOTAL_NON_ACCRUAL() {
	return R37_TOTAL_NON_ACCRUAL;
}



public void setR37_TOTAL_NON_ACCRUAL(BigDecimal r37_TOTAL_NON_ACCRUAL) {
	R37_TOTAL_NON_ACCRUAL = r37_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR37_TOTAL_DUE_LOANS() {
	return R37_TOTAL_DUE_LOANS;
}



public void setR37_TOTAL_DUE_LOANS(BigDecimal r37_TOTAL_DUE_LOANS) {
	R37_TOTAL_DUE_LOANS = r37_TOTAL_DUE_LOANS;
}



public BigDecimal getR37_TOTAL_PERFORMING_LOAN() {
	return R37_TOTAL_PERFORMING_LOAN;
}



public void setR37_TOTAL_PERFORMING_LOAN(BigDecimal r37_TOTAL_PERFORMING_LOAN) {
	R37_TOTAL_PERFORMING_LOAN = r37_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR37_VALUE_OF_COLLATERAL() {
	return R37_VALUE_OF_COLLATERAL;
}



public void setR37_VALUE_OF_COLLATERAL(BigDecimal r37_VALUE_OF_COLLATERAL) {
	R37_VALUE_OF_COLLATERAL = r37_VALUE_OF_COLLATERAL;
}



public BigDecimal getR37_TOTAL_VALUE_NPL() {
	return R37_TOTAL_VALUE_NPL;
}



public void setR37_TOTAL_VALUE_NPL(BigDecimal r37_TOTAL_VALUE_NPL) {
	R37_TOTAL_VALUE_NPL = r37_TOTAL_VALUE_NPL;
}



public BigDecimal getR37_TOTAL_SPECIFIC_PROV() {
	return R37_TOTAL_SPECIFIC_PROV;
}



public void setR37_TOTAL_SPECIFIC_PROV(BigDecimal r37_TOTAL_SPECIFIC_PROV) {
	R37_TOTAL_SPECIFIC_PROV = r37_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR37_SPECIFIC_PROV_NPL() {
	return R37_SPECIFIC_PROV_NPL;
}



public void setR37_SPECIFIC_PROV_NPL(BigDecimal r37_SPECIFIC_PROV_NPL) {
	R37_SPECIFIC_PROV_NPL = r37_SPECIFIC_PROV_NPL;
}



public BigDecimal getR38_30D_90D_PASTDUE() {
	return R38_30D_90D_PASTDUE;
}



public void setR38_30D_90D_PASTDUE(BigDecimal r38_30d_90d_PASTDUE) {
	R38_30D_90D_PASTDUE = r38_30d_90d_PASTDUE;
}



public BigDecimal getR38_NON_PERFORM_LOANS() {
	return R38_NON_PERFORM_LOANS;
}



public void setR38_NON_PERFORM_LOANS(BigDecimal r38_NON_PERFORM_LOANS) {
	R38_NON_PERFORM_LOANS = r38_NON_PERFORM_LOANS;
}



public BigDecimal getR38_NON_ACCRUALS1() {
	return R38_NON_ACCRUALS1;
}



public void setR38_NON_ACCRUALS1(BigDecimal r38_NON_ACCRUALS1) {
	R38_NON_ACCRUALS1 = r38_NON_ACCRUALS1;
}



public BigDecimal getR38_SPECIFIC_PROV1() {
	return R38_SPECIFIC_PROV1;
}



public void setR38_SPECIFIC_PROV1(BigDecimal r38_SPECIFIC_PROV1) {
	R38_SPECIFIC_PROV1 = r38_SPECIFIC_PROV1;
}



public BigDecimal getR38_NO_OF_ACC1() {
	return R38_NO_OF_ACC1;
}



public void setR38_NO_OF_ACC1(BigDecimal r38_NO_OF_ACC1) {
	R38_NO_OF_ACC1 = r38_NO_OF_ACC1;
}



public BigDecimal getR38_90D_180D_PASTDUE() {
	return R38_90D_180D_PASTDUE;
}



public void setR38_90D_180D_PASTDUE(BigDecimal r38_90d_180d_PASTDUE) {
	R38_90D_180D_PASTDUE = r38_90d_180d_PASTDUE;
}



public BigDecimal getR38_NON_ACCRUALS2() {
	return R38_NON_ACCRUALS2;
}



public void setR38_NON_ACCRUALS2(BigDecimal r38_NON_ACCRUALS2) {
	R38_NON_ACCRUALS2 = r38_NON_ACCRUALS2;
}



public BigDecimal getR38_SPECIFIC_PROV2() {
	return R38_SPECIFIC_PROV2;
}



public void setR38_SPECIFIC_PROV2(BigDecimal r38_SPECIFIC_PROV2) {
	R38_SPECIFIC_PROV2 = r38_SPECIFIC_PROV2;
}



public BigDecimal getR38_NO_OF_ACC2() {
	return R38_NO_OF_ACC2;
}



public void setR38_NO_OF_ACC2(BigDecimal r38_NO_OF_ACC2) {
	R38_NO_OF_ACC2 = r38_NO_OF_ACC2;
}



public BigDecimal getR38_180D_ABOVE_PASTDUE() {
	return R38_180D_ABOVE_PASTDUE;
}



public void setR38_180D_ABOVE_PASTDUE(BigDecimal r38_180d_ABOVE_PASTDUE) {
	R38_180D_ABOVE_PASTDUE = r38_180d_ABOVE_PASTDUE;
}



public BigDecimal getR38_NON_ACCRUALS3() {
	return R38_NON_ACCRUALS3;
}



public void setR38_NON_ACCRUALS3(BigDecimal r38_NON_ACCRUALS3) {
	R38_NON_ACCRUALS3 = r38_NON_ACCRUALS3;
}



public BigDecimal getR38_SPECIFIC_PROV3() {
	return R38_SPECIFIC_PROV3;
}



public void setR38_SPECIFIC_PROV3(BigDecimal r38_SPECIFIC_PROV3) {
	R38_SPECIFIC_PROV3 = r38_SPECIFIC_PROV3;
}



public BigDecimal getR38_NO_OF_ACC3() {
	return R38_NO_OF_ACC3;
}



public void setR38_NO_OF_ACC3(BigDecimal r38_NO_OF_ACC3) {
	R38_NO_OF_ACC3 = r38_NO_OF_ACC3;
}



public BigDecimal getR38_TOTAL_NON_ACCRUAL() {
	return R38_TOTAL_NON_ACCRUAL;
}



public void setR38_TOTAL_NON_ACCRUAL(BigDecimal r38_TOTAL_NON_ACCRUAL) {
	R38_TOTAL_NON_ACCRUAL = r38_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR38_TOTAL_DUE_LOANS() {
	return R38_TOTAL_DUE_LOANS;
}



public void setR38_TOTAL_DUE_LOANS(BigDecimal r38_TOTAL_DUE_LOANS) {
	R38_TOTAL_DUE_LOANS = r38_TOTAL_DUE_LOANS;
}



public BigDecimal getR38_TOTAL_PERFORMING_LOAN() {
	return R38_TOTAL_PERFORMING_LOAN;
}



public void setR38_TOTAL_PERFORMING_LOAN(BigDecimal r38_TOTAL_PERFORMING_LOAN) {
	R38_TOTAL_PERFORMING_LOAN = r38_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR38_VALUE_OF_COLLATERAL() {
	return R38_VALUE_OF_COLLATERAL;
}



public void setR38_VALUE_OF_COLLATERAL(BigDecimal r38_VALUE_OF_COLLATERAL) {
	R38_VALUE_OF_COLLATERAL = r38_VALUE_OF_COLLATERAL;
}



public BigDecimal getR38_TOTAL_VALUE_NPL() {
	return R38_TOTAL_VALUE_NPL;
}



public void setR38_TOTAL_VALUE_NPL(BigDecimal r38_TOTAL_VALUE_NPL) {
	R38_TOTAL_VALUE_NPL = r38_TOTAL_VALUE_NPL;
}



public BigDecimal getR38_TOTAL_SPECIFIC_PROV() {
	return R38_TOTAL_SPECIFIC_PROV;
}



public void setR38_TOTAL_SPECIFIC_PROV(BigDecimal r38_TOTAL_SPECIFIC_PROV) {
	R38_TOTAL_SPECIFIC_PROV = r38_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR38_SPECIFIC_PROV_NPL() {
	return R38_SPECIFIC_PROV_NPL;
}



public void setR38_SPECIFIC_PROV_NPL(BigDecimal r38_SPECIFIC_PROV_NPL) {
	R38_SPECIFIC_PROV_NPL = r38_SPECIFIC_PROV_NPL;
}



public BigDecimal getR39_30D_90D_PASTDUE() {
	return R39_30D_90D_PASTDUE;
}



public void setR39_30D_90D_PASTDUE(BigDecimal r39_30d_90d_PASTDUE) {
	R39_30D_90D_PASTDUE = r39_30d_90d_PASTDUE;
}



public BigDecimal getR39_NON_PERFORM_LOANS() {
	return R39_NON_PERFORM_LOANS;
}



public void setR39_NON_PERFORM_LOANS(BigDecimal r39_NON_PERFORM_LOANS) {
	R39_NON_PERFORM_LOANS = r39_NON_PERFORM_LOANS;
}



public BigDecimal getR39_NON_ACCRUALS1() {
	return R39_NON_ACCRUALS1;
}



public void setR39_NON_ACCRUALS1(BigDecimal r39_NON_ACCRUALS1) {
	R39_NON_ACCRUALS1 = r39_NON_ACCRUALS1;
}



public BigDecimal getR39_SPECIFIC_PROV1() {
	return R39_SPECIFIC_PROV1;
}



public void setR39_SPECIFIC_PROV1(BigDecimal r39_SPECIFIC_PROV1) {
	R39_SPECIFIC_PROV1 = r39_SPECIFIC_PROV1;
}



public BigDecimal getR39_NO_OF_ACC1() {
	return R39_NO_OF_ACC1;
}



public void setR39_NO_OF_ACC1(BigDecimal r39_NO_OF_ACC1) {
	R39_NO_OF_ACC1 = r39_NO_OF_ACC1;
}



public BigDecimal getR39_90D_180D_PASTDUE() {
	return R39_90D_180D_PASTDUE;
}



public void setR39_90D_180D_PASTDUE(BigDecimal r39_90d_180d_PASTDUE) {
	R39_90D_180D_PASTDUE = r39_90d_180d_PASTDUE;
}



public BigDecimal getR39_NON_ACCRUALS2() {
	return R39_NON_ACCRUALS2;
}



public void setR39_NON_ACCRUALS2(BigDecimal r39_NON_ACCRUALS2) {
	R39_NON_ACCRUALS2 = r39_NON_ACCRUALS2;
}



public BigDecimal getR39_SPECIFIC_PROV2() {
	return R39_SPECIFIC_PROV2;
}



public void setR39_SPECIFIC_PROV2(BigDecimal r39_SPECIFIC_PROV2) {
	R39_SPECIFIC_PROV2 = r39_SPECIFIC_PROV2;
}



public BigDecimal getR39_NO_OF_ACC2() {
	return R39_NO_OF_ACC2;
}



public void setR39_NO_OF_ACC2(BigDecimal r39_NO_OF_ACC2) {
	R39_NO_OF_ACC2 = r39_NO_OF_ACC2;
}



public BigDecimal getR39_TOTAL_NON_ACCRUAL() {
	return R39_TOTAL_NON_ACCRUAL;
}



public void setR39_TOTAL_NON_ACCRUAL(BigDecimal r39_TOTAL_NON_ACCRUAL) {
	R39_TOTAL_NON_ACCRUAL = r39_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR39_TOTAL_DUE_LOANS() {
	return R39_TOTAL_DUE_LOANS;
}



public void setR39_TOTAL_DUE_LOANS(BigDecimal r39_TOTAL_DUE_LOANS) {
	R39_TOTAL_DUE_LOANS = r39_TOTAL_DUE_LOANS;
}



public BigDecimal getR39_TOTAL_PERFORMING_LOAN() {
	return R39_TOTAL_PERFORMING_LOAN;
}



public void setR39_TOTAL_PERFORMING_LOAN(BigDecimal r39_TOTAL_PERFORMING_LOAN) {
	R39_TOTAL_PERFORMING_LOAN = r39_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR39_VALUE_OF_COLLATERAL() {
	return R39_VALUE_OF_COLLATERAL;
}



public void setR39_VALUE_OF_COLLATERAL(BigDecimal r39_VALUE_OF_COLLATERAL) {
	R39_VALUE_OF_COLLATERAL = r39_VALUE_OF_COLLATERAL;
}



public BigDecimal getR39_TOTAL_VALUE_NPL() {
	return R39_TOTAL_VALUE_NPL;
}



public void setR39_TOTAL_VALUE_NPL(BigDecimal r39_TOTAL_VALUE_NPL) {
	R39_TOTAL_VALUE_NPL = r39_TOTAL_VALUE_NPL;
}



public BigDecimal getR39_TOTAL_SPECIFIC_PROV() {
	return R39_TOTAL_SPECIFIC_PROV;
}



public void setR39_TOTAL_SPECIFIC_PROV(BigDecimal r39_TOTAL_SPECIFIC_PROV) {
	R39_TOTAL_SPECIFIC_PROV = r39_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR39_SPECIFIC_PROV_NPL() {
	return R39_SPECIFIC_PROV_NPL;
}



public void setR39_SPECIFIC_PROV_NPL(BigDecimal r39_SPECIFIC_PROV_NPL) {
	R39_SPECIFIC_PROV_NPL = r39_SPECIFIC_PROV_NPL;
}



public BigDecimal getR40_30D_90D_PASTDUE() {
	return R40_30D_90D_PASTDUE;
}



public void setR40_30D_90D_PASTDUE(BigDecimal r40_30d_90d_PASTDUE) {
	R40_30D_90D_PASTDUE = r40_30d_90d_PASTDUE;
}



public BigDecimal getR40_NON_PERFORM_LOANS() {
	return R40_NON_PERFORM_LOANS;
}



public void setR40_NON_PERFORM_LOANS(BigDecimal r40_NON_PERFORM_LOANS) {
	R40_NON_PERFORM_LOANS = r40_NON_PERFORM_LOANS;
}



public BigDecimal getR40_NON_ACCRUALS1() {
	return R40_NON_ACCRUALS1;
}



public void setR40_NON_ACCRUALS1(BigDecimal r40_NON_ACCRUALS1) {
	R40_NON_ACCRUALS1 = r40_NON_ACCRUALS1;
}



public BigDecimal getR40_SPECIFIC_PROV1() {
	return R40_SPECIFIC_PROV1;
}



public void setR40_SPECIFIC_PROV1(BigDecimal r40_SPECIFIC_PROV1) {
	R40_SPECIFIC_PROV1 = r40_SPECIFIC_PROV1;
}



public BigDecimal getR40_NO_OF_ACC1() {
	return R40_NO_OF_ACC1;
}



public void setR40_NO_OF_ACC1(BigDecimal r40_NO_OF_ACC1) {
	R40_NO_OF_ACC1 = r40_NO_OF_ACC1;
}



public BigDecimal getR40_90D_180D_PASTDUE() {
	return R40_90D_180D_PASTDUE;
}



public void setR40_90D_180D_PASTDUE(BigDecimal r40_90d_180d_PASTDUE) {
	R40_90D_180D_PASTDUE = r40_90d_180d_PASTDUE;
}



public BigDecimal getR40_NON_ACCRUALS2() {
	return R40_NON_ACCRUALS2;
}



public void setR40_NON_ACCRUALS2(BigDecimal r40_NON_ACCRUALS2) {
	R40_NON_ACCRUALS2 = r40_NON_ACCRUALS2;
}



public BigDecimal getR40_SPECIFIC_PROV2() {
	return R40_SPECIFIC_PROV2;
}



public void setR40_SPECIFIC_PROV2(BigDecimal r40_SPECIFIC_PROV2) {
	R40_SPECIFIC_PROV2 = r40_SPECIFIC_PROV2;
}



public BigDecimal getR40_NO_OF_ACC2() {
	return R40_NO_OF_ACC2;
}



public void setR40_NO_OF_ACC2(BigDecimal r40_NO_OF_ACC2) {
	R40_NO_OF_ACC2 = r40_NO_OF_ACC2;
}



public BigDecimal getR40_TOTAL_NON_ACCRUAL() {
	return R40_TOTAL_NON_ACCRUAL;
}



public void setR40_TOTAL_NON_ACCRUAL(BigDecimal r40_TOTAL_NON_ACCRUAL) {
	R40_TOTAL_NON_ACCRUAL = r40_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR40_TOTAL_DUE_LOANS() {
	return R40_TOTAL_DUE_LOANS;
}



public void setR40_TOTAL_DUE_LOANS(BigDecimal r40_TOTAL_DUE_LOANS) {
	R40_TOTAL_DUE_LOANS = r40_TOTAL_DUE_LOANS;
}



public BigDecimal getR40_TOTAL_PERFORMING_LOAN() {
	return R40_TOTAL_PERFORMING_LOAN;
}



public void setR40_TOTAL_PERFORMING_LOAN(BigDecimal r40_TOTAL_PERFORMING_LOAN) {
	R40_TOTAL_PERFORMING_LOAN = r40_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR40_VALUE_OF_COLLATERAL() {
	return R40_VALUE_OF_COLLATERAL;
}



public void setR40_VALUE_OF_COLLATERAL(BigDecimal r40_VALUE_OF_COLLATERAL) {
	R40_VALUE_OF_COLLATERAL = r40_VALUE_OF_COLLATERAL;
}



public BigDecimal getR40_TOTAL_VALUE_NPL() {
	return R40_TOTAL_VALUE_NPL;
}



public void setR40_TOTAL_VALUE_NPL(BigDecimal r40_TOTAL_VALUE_NPL) {
	R40_TOTAL_VALUE_NPL = r40_TOTAL_VALUE_NPL;
}



public BigDecimal getR40_TOTAL_SPECIFIC_PROV() {
	return R40_TOTAL_SPECIFIC_PROV;
}



public void setR40_TOTAL_SPECIFIC_PROV(BigDecimal r40_TOTAL_SPECIFIC_PROV) {
	R40_TOTAL_SPECIFIC_PROV = r40_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR40_SPECIFIC_PROV_NPL() {
	return R40_SPECIFIC_PROV_NPL;
}



public void setR40_SPECIFIC_PROV_NPL(BigDecimal r40_SPECIFIC_PROV_NPL) {
	R40_SPECIFIC_PROV_NPL = r40_SPECIFIC_PROV_NPL;
}



public BigDecimal getR41_30D_90D_PASTDUE() {
	return R41_30D_90D_PASTDUE;
}



public void setR41_30D_90D_PASTDUE(BigDecimal r41_30d_90d_PASTDUE) {
	R41_30D_90D_PASTDUE = r41_30d_90d_PASTDUE;
}



public BigDecimal getR41_NON_PERFORM_LOANS() {
	return R41_NON_PERFORM_LOANS;
}



public void setR41_NON_PERFORM_LOANS(BigDecimal r41_NON_PERFORM_LOANS) {
	R41_NON_PERFORM_LOANS = r41_NON_PERFORM_LOANS;
}



public BigDecimal getR41_NON_ACCRUALS1() {
	return R41_NON_ACCRUALS1;
}



public void setR41_NON_ACCRUALS1(BigDecimal r41_NON_ACCRUALS1) {
	R41_NON_ACCRUALS1 = r41_NON_ACCRUALS1;
}



public BigDecimal getR41_SPECIFIC_PROV1() {
	return R41_SPECIFIC_PROV1;
}



public void setR41_SPECIFIC_PROV1(BigDecimal r41_SPECIFIC_PROV1) {
	R41_SPECIFIC_PROV1 = r41_SPECIFIC_PROV1;
}



public BigDecimal getR41_NO_OF_ACC1() {
	return R41_NO_OF_ACC1;
}



public void setR41_NO_OF_ACC1(BigDecimal r41_NO_OF_ACC1) {
	R41_NO_OF_ACC1 = r41_NO_OF_ACC1;
}



public BigDecimal getR41_90D_180D_PASTDUE() {
	return R41_90D_180D_PASTDUE;
}



public void setR41_90D_180D_PASTDUE(BigDecimal r41_90d_180d_PASTDUE) {
	R41_90D_180D_PASTDUE = r41_90d_180d_PASTDUE;
}



public BigDecimal getR41_NON_ACCRUALS2() {
	return R41_NON_ACCRUALS2;
}



public void setR41_NON_ACCRUALS2(BigDecimal r41_NON_ACCRUALS2) {
	R41_NON_ACCRUALS2 = r41_NON_ACCRUALS2;
}



public BigDecimal getR41_SPECIFIC_PROV2() {
	return R41_SPECIFIC_PROV2;
}



public void setR41_SPECIFIC_PROV2(BigDecimal r41_SPECIFIC_PROV2) {
	R41_SPECIFIC_PROV2 = r41_SPECIFIC_PROV2;
}



public BigDecimal getR41_NO_OF_ACC2() {
	return R41_NO_OF_ACC2;
}



public void setR41_NO_OF_ACC2(BigDecimal r41_NO_OF_ACC2) {
	R41_NO_OF_ACC2 = r41_NO_OF_ACC2;
}



public BigDecimal getR41_180D_ABOVE_PASTDUE() {
	return R41_180D_ABOVE_PASTDUE;
}



public void setR41_180D_ABOVE_PASTDUE(BigDecimal r41_180d_ABOVE_PASTDUE) {
	R41_180D_ABOVE_PASTDUE = r41_180d_ABOVE_PASTDUE;
}



public BigDecimal getR41_NON_ACCRUALS3() {
	return R41_NON_ACCRUALS3;
}



public void setR41_NON_ACCRUALS3(BigDecimal r41_NON_ACCRUALS3) {
	R41_NON_ACCRUALS3 = r41_NON_ACCRUALS3;
}



public BigDecimal getR41_SPECIFIC_PROV3() {
	return R41_SPECIFIC_PROV3;
}



public void setR41_SPECIFIC_PROV3(BigDecimal r41_SPECIFIC_PROV3) {
	R41_SPECIFIC_PROV3 = r41_SPECIFIC_PROV3;
}



public BigDecimal getR41_NO_OF_ACC3() {
	return R41_NO_OF_ACC3;
}



public void setR41_NO_OF_ACC3(BigDecimal r41_NO_OF_ACC3) {
	R41_NO_OF_ACC3 = r41_NO_OF_ACC3;
}



public BigDecimal getR41_TOTAL_NON_ACCRUAL() {
	return R41_TOTAL_NON_ACCRUAL;
}



public void setR41_TOTAL_NON_ACCRUAL(BigDecimal r41_TOTAL_NON_ACCRUAL) {
	R41_TOTAL_NON_ACCRUAL = r41_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR41_TOTAL_DUE_LOANS() {
	return R41_TOTAL_DUE_LOANS;
}



public void setR41_TOTAL_DUE_LOANS(BigDecimal r41_TOTAL_DUE_LOANS) {
	R41_TOTAL_DUE_LOANS = r41_TOTAL_DUE_LOANS;
}



public BigDecimal getR41_TOTAL_PERFORMING_LOAN() {
	return R41_TOTAL_PERFORMING_LOAN;
}



public void setR41_TOTAL_PERFORMING_LOAN(BigDecimal r41_TOTAL_PERFORMING_LOAN) {
	R41_TOTAL_PERFORMING_LOAN = r41_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR41_VALUE_OF_COLLATERAL() {
	return R41_VALUE_OF_COLLATERAL;
}



public void setR41_VALUE_OF_COLLATERAL(BigDecimal r41_VALUE_OF_COLLATERAL) {
	R41_VALUE_OF_COLLATERAL = r41_VALUE_OF_COLLATERAL;
}



public BigDecimal getR41_TOTAL_VALUE_NPL() {
	return R41_TOTAL_VALUE_NPL;
}



public void setR41_TOTAL_VALUE_NPL(BigDecimal r41_TOTAL_VALUE_NPL) {
	R41_TOTAL_VALUE_NPL = r41_TOTAL_VALUE_NPL;
}



public BigDecimal getR41_TOTAL_SPECIFIC_PROV() {
	return R41_TOTAL_SPECIFIC_PROV;
}



public void setR41_TOTAL_SPECIFIC_PROV(BigDecimal r41_TOTAL_SPECIFIC_PROV) {
	R41_TOTAL_SPECIFIC_PROV = r41_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR41_SPECIFIC_PROV_NPL() {
	return R41_SPECIFIC_PROV_NPL;
}



public void setR41_SPECIFIC_PROV_NPL(BigDecimal r41_SPECIFIC_PROV_NPL) {
	R41_SPECIFIC_PROV_NPL = r41_SPECIFIC_PROV_NPL;
}



public BigDecimal getR42_30D_90D_PASTDUE() {
	return R42_30D_90D_PASTDUE;
}



public void setR42_30D_90D_PASTDUE(BigDecimal r42_30d_90d_PASTDUE) {
	R42_30D_90D_PASTDUE = r42_30d_90d_PASTDUE;
}



public BigDecimal getR42_NON_PERFORM_LOANS() {
	return R42_NON_PERFORM_LOANS;
}



public void setR42_NON_PERFORM_LOANS(BigDecimal r42_NON_PERFORM_LOANS) {
	R42_NON_PERFORM_LOANS = r42_NON_PERFORM_LOANS;
}



public BigDecimal getR42_NON_ACCRUALS1() {
	return R42_NON_ACCRUALS1;
}



public void setR42_NON_ACCRUALS1(BigDecimal r42_NON_ACCRUALS1) {
	R42_NON_ACCRUALS1 = r42_NON_ACCRUALS1;
}



public BigDecimal getR42_SPECIFIC_PROV1() {
	return R42_SPECIFIC_PROV1;
}



public void setR42_SPECIFIC_PROV1(BigDecimal r42_SPECIFIC_PROV1) {
	R42_SPECIFIC_PROV1 = r42_SPECIFIC_PROV1;
}



public BigDecimal getR42_NO_OF_ACC1() {
	return R42_NO_OF_ACC1;
}



public void setR42_NO_OF_ACC1(BigDecimal r42_NO_OF_ACC1) {
	R42_NO_OF_ACC1 = r42_NO_OF_ACC1;
}



public BigDecimal getR42_90D_180D_PASTDUE() {
	return R42_90D_180D_PASTDUE;
}



public void setR42_90D_180D_PASTDUE(BigDecimal r42_90d_180d_PASTDUE) {
	R42_90D_180D_PASTDUE = r42_90d_180d_PASTDUE;
}



public BigDecimal getR42_NON_ACCRUALS2() {
	return R42_NON_ACCRUALS2;
}



public void setR42_NON_ACCRUALS2(BigDecimal r42_NON_ACCRUALS2) {
	R42_NON_ACCRUALS2 = r42_NON_ACCRUALS2;
}



public BigDecimal getR42_SPECIFIC_PROV2() {
	return R42_SPECIFIC_PROV2;
}



public void setR42_SPECIFIC_PROV2(BigDecimal r42_SPECIFIC_PROV2) {
	R42_SPECIFIC_PROV2 = r42_SPECIFIC_PROV2;
}



public BigDecimal getR42_NO_OF_ACC2() {
	return R42_NO_OF_ACC2;
}



public void setR42_NO_OF_ACC2(BigDecimal r42_NO_OF_ACC2) {
	R42_NO_OF_ACC2 = r42_NO_OF_ACC2;
}



public BigDecimal getR42_TOTAL_NON_ACCRUAL() {
	return R42_TOTAL_NON_ACCRUAL;
}



public void setR42_TOTAL_NON_ACCRUAL(BigDecimal r42_TOTAL_NON_ACCRUAL) {
	R42_TOTAL_NON_ACCRUAL = r42_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR42_TOTAL_DUE_LOANS() {
	return R42_TOTAL_DUE_LOANS;
}



public void setR42_TOTAL_DUE_LOANS(BigDecimal r42_TOTAL_DUE_LOANS) {
	R42_TOTAL_DUE_LOANS = r42_TOTAL_DUE_LOANS;
}



public BigDecimal getR42_TOTAL_PERFORMING_LOAN() {
	return R42_TOTAL_PERFORMING_LOAN;
}



public void setR42_TOTAL_PERFORMING_LOAN(BigDecimal r42_TOTAL_PERFORMING_LOAN) {
	R42_TOTAL_PERFORMING_LOAN = r42_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR42_VALUE_OF_COLLATERAL() {
	return R42_VALUE_OF_COLLATERAL;
}



public void setR42_VALUE_OF_COLLATERAL(BigDecimal r42_VALUE_OF_COLLATERAL) {
	R42_VALUE_OF_COLLATERAL = r42_VALUE_OF_COLLATERAL;
}



public BigDecimal getR42_TOTAL_VALUE_NPL() {
	return R42_TOTAL_VALUE_NPL;
}



public void setR42_TOTAL_VALUE_NPL(BigDecimal r42_TOTAL_VALUE_NPL) {
	R42_TOTAL_VALUE_NPL = r42_TOTAL_VALUE_NPL;
}



public BigDecimal getR42_TOTAL_SPECIFIC_PROV() {
	return R42_TOTAL_SPECIFIC_PROV;
}



public void setR42_TOTAL_SPECIFIC_PROV(BigDecimal r42_TOTAL_SPECIFIC_PROV) {
	R42_TOTAL_SPECIFIC_PROV = r42_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR42_SPECIFIC_PROV_NPL() {
	return R42_SPECIFIC_PROV_NPL;
}



public void setR42_SPECIFIC_PROV_NPL(BigDecimal r42_SPECIFIC_PROV_NPL) {
	R42_SPECIFIC_PROV_NPL = r42_SPECIFIC_PROV_NPL;
}



public BigDecimal getR43_30D_90D_PASTDUE() {
	return R43_30D_90D_PASTDUE;
}



public void setR43_30D_90D_PASTDUE(BigDecimal r43_30d_90d_PASTDUE) {
	R43_30D_90D_PASTDUE = r43_30d_90d_PASTDUE;
}



public BigDecimal getR43_NON_PERFORM_LOANS() {
	return R43_NON_PERFORM_LOANS;
}



public void setR43_NON_PERFORM_LOANS(BigDecimal r43_NON_PERFORM_LOANS) {
	R43_NON_PERFORM_LOANS = r43_NON_PERFORM_LOANS;
}



public BigDecimal getR43_NON_ACCRUALS1() {
	return R43_NON_ACCRUALS1;
}



public void setR43_NON_ACCRUALS1(BigDecimal r43_NON_ACCRUALS1) {
	R43_NON_ACCRUALS1 = r43_NON_ACCRUALS1;
}



public BigDecimal getR43_SPECIFIC_PROV1() {
	return R43_SPECIFIC_PROV1;
}



public void setR43_SPECIFIC_PROV1(BigDecimal r43_SPECIFIC_PROV1) {
	R43_SPECIFIC_PROV1 = r43_SPECIFIC_PROV1;
}



public BigDecimal getR43_NO_OF_ACC1() {
	return R43_NO_OF_ACC1;
}



public void setR43_NO_OF_ACC1(BigDecimal r43_NO_OF_ACC1) {
	R43_NO_OF_ACC1 = r43_NO_OF_ACC1;
}



public BigDecimal getR43_90D_180D_PASTDUE() {
	return R43_90D_180D_PASTDUE;
}



public void setR43_90D_180D_PASTDUE(BigDecimal r43_90d_180d_PASTDUE) {
	R43_90D_180D_PASTDUE = r43_90d_180d_PASTDUE;
}



public BigDecimal getR43_NON_ACCRUALS2() {
	return R43_NON_ACCRUALS2;
}



public void setR43_NON_ACCRUALS2(BigDecimal r43_NON_ACCRUALS2) {
	R43_NON_ACCRUALS2 = r43_NON_ACCRUALS2;
}



public BigDecimal getR43_SPECIFIC_PROV2() {
	return R43_SPECIFIC_PROV2;
}



public void setR43_SPECIFIC_PROV2(BigDecimal r43_SPECIFIC_PROV2) {
	R43_SPECIFIC_PROV2 = r43_SPECIFIC_PROV2;
}



public BigDecimal getR43_NO_OF_ACC2() {
	return R43_NO_OF_ACC2;
}



public void setR43_NO_OF_ACC2(BigDecimal r43_NO_OF_ACC2) {
	R43_NO_OF_ACC2 = r43_NO_OF_ACC2;
}



public BigDecimal getR43_TOTAL_NON_ACCRUAL() {
	return R43_TOTAL_NON_ACCRUAL;
}



public void setR43_TOTAL_NON_ACCRUAL(BigDecimal r43_TOTAL_NON_ACCRUAL) {
	R43_TOTAL_NON_ACCRUAL = r43_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR43_TOTAL_DUE_LOANS() {
	return R43_TOTAL_DUE_LOANS;
}



public void setR43_TOTAL_DUE_LOANS(BigDecimal r43_TOTAL_DUE_LOANS) {
	R43_TOTAL_DUE_LOANS = r43_TOTAL_DUE_LOANS;
}



public BigDecimal getR43_TOTAL_PERFORMING_LOAN() {
	return R43_TOTAL_PERFORMING_LOAN;
}



public void setR43_TOTAL_PERFORMING_LOAN(BigDecimal r43_TOTAL_PERFORMING_LOAN) {
	R43_TOTAL_PERFORMING_LOAN = r43_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR43_VALUE_OF_COLLATERAL() {
	return R43_VALUE_OF_COLLATERAL;
}



public void setR43_VALUE_OF_COLLATERAL(BigDecimal r43_VALUE_OF_COLLATERAL) {
	R43_VALUE_OF_COLLATERAL = r43_VALUE_OF_COLLATERAL;
}



public BigDecimal getR43_TOTAL_VALUE_NPL() {
	return R43_TOTAL_VALUE_NPL;
}



public void setR43_TOTAL_VALUE_NPL(BigDecimal r43_TOTAL_VALUE_NPL) {
	R43_TOTAL_VALUE_NPL = r43_TOTAL_VALUE_NPL;
}



public BigDecimal getR43_TOTAL_SPECIFIC_PROV() {
	return R43_TOTAL_SPECIFIC_PROV;
}



public void setR43_TOTAL_SPECIFIC_PROV(BigDecimal r43_TOTAL_SPECIFIC_PROV) {
	R43_TOTAL_SPECIFIC_PROV = r43_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR43_SPECIFIC_PROV_NPL() {
	return R43_SPECIFIC_PROV_NPL;
}



public void setR43_SPECIFIC_PROV_NPL(BigDecimal r43_SPECIFIC_PROV_NPL) {
	R43_SPECIFIC_PROV_NPL = r43_SPECIFIC_PROV_NPL;
}



public BigDecimal getR44_30D_90D_PASTDUE() {
	return R44_30D_90D_PASTDUE;
}



public void setR44_30D_90D_PASTDUE(BigDecimal r44_30d_90d_PASTDUE) {
	R44_30D_90D_PASTDUE = r44_30d_90d_PASTDUE;
}



public BigDecimal getR44_NON_PERFORM_LOANS() {
	return R44_NON_PERFORM_LOANS;
}



public void setR44_NON_PERFORM_LOANS(BigDecimal r44_NON_PERFORM_LOANS) {
	R44_NON_PERFORM_LOANS = r44_NON_PERFORM_LOANS;
}



public BigDecimal getR44_NON_ACCRUALS1() {
	return R44_NON_ACCRUALS1;
}



public void setR44_NON_ACCRUALS1(BigDecimal r44_NON_ACCRUALS1) {
	R44_NON_ACCRUALS1 = r44_NON_ACCRUALS1;
}



public BigDecimal getR44_SPECIFIC_PROV1() {
	return R44_SPECIFIC_PROV1;
}



public void setR44_SPECIFIC_PROV1(BigDecimal r44_SPECIFIC_PROV1) {
	R44_SPECIFIC_PROV1 = r44_SPECIFIC_PROV1;
}



public BigDecimal getR44_NO_OF_ACC1() {
	return R44_NO_OF_ACC1;
}



public void setR44_NO_OF_ACC1(BigDecimal r44_NO_OF_ACC1) {
	R44_NO_OF_ACC1 = r44_NO_OF_ACC1;
}



public BigDecimal getR44_90D_180D_PASTDUE() {
	return R44_90D_180D_PASTDUE;
}



public void setR44_90D_180D_PASTDUE(BigDecimal r44_90d_180d_PASTDUE) {
	R44_90D_180D_PASTDUE = r44_90d_180d_PASTDUE;
}



public BigDecimal getR44_NON_ACCRUALS2() {
	return R44_NON_ACCRUALS2;
}



public void setR44_NON_ACCRUALS2(BigDecimal r44_NON_ACCRUALS2) {
	R44_NON_ACCRUALS2 = r44_NON_ACCRUALS2;
}



public BigDecimal getR44_SPECIFIC_PROV2() {
	return R44_SPECIFIC_PROV2;
}



public void setR44_SPECIFIC_PROV2(BigDecimal r44_SPECIFIC_PROV2) {
	R44_SPECIFIC_PROV2 = r44_SPECIFIC_PROV2;
}



public BigDecimal getR44_NO_OF_ACC2() {
	return R44_NO_OF_ACC2;
}



public void setR44_NO_OF_ACC2(BigDecimal r44_NO_OF_ACC2) {
	R44_NO_OF_ACC2 = r44_NO_OF_ACC2;
}



public BigDecimal getR44_TOTAL_NON_ACCRUAL() {
	return R44_TOTAL_NON_ACCRUAL;
}



public void setR44_TOTAL_NON_ACCRUAL(BigDecimal r44_TOTAL_NON_ACCRUAL) {
	R44_TOTAL_NON_ACCRUAL = r44_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR44_TOTAL_DUE_LOANS() {
	return R44_TOTAL_DUE_LOANS;
}



public void setR44_TOTAL_DUE_LOANS(BigDecimal r44_TOTAL_DUE_LOANS) {
	R44_TOTAL_DUE_LOANS = r44_TOTAL_DUE_LOANS;
}



public BigDecimal getR44_TOTAL_PERFORMING_LOAN() {
	return R44_TOTAL_PERFORMING_LOAN;
}



public void setR44_TOTAL_PERFORMING_LOAN(BigDecimal r44_TOTAL_PERFORMING_LOAN) {
	R44_TOTAL_PERFORMING_LOAN = r44_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR44_VALUE_OF_COLLATERAL() {
	return R44_VALUE_OF_COLLATERAL;
}



public void setR44_VALUE_OF_COLLATERAL(BigDecimal r44_VALUE_OF_COLLATERAL) {
	R44_VALUE_OF_COLLATERAL = r44_VALUE_OF_COLLATERAL;
}



public BigDecimal getR44_TOTAL_VALUE_NPL() {
	return R44_TOTAL_VALUE_NPL;
}



public void setR44_TOTAL_VALUE_NPL(BigDecimal r44_TOTAL_VALUE_NPL) {
	R44_TOTAL_VALUE_NPL = r44_TOTAL_VALUE_NPL;
}



public BigDecimal getR44_TOTAL_SPECIFIC_PROV() {
	return R44_TOTAL_SPECIFIC_PROV;
}



public void setR44_TOTAL_SPECIFIC_PROV(BigDecimal r44_TOTAL_SPECIFIC_PROV) {
	R44_TOTAL_SPECIFIC_PROV = r44_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR44_SPECIFIC_PROV_NPL() {
	return R44_SPECIFIC_PROV_NPL;
}



public void setR44_SPECIFIC_PROV_NPL(BigDecimal r44_SPECIFIC_PROV_NPL) {
	R44_SPECIFIC_PROV_NPL = r44_SPECIFIC_PROV_NPL;
}



public BigDecimal getR45_30D_90D_PASTDUE() {
	return R45_30D_90D_PASTDUE;
}



public void setR45_30D_90D_PASTDUE(BigDecimal r45_30d_90d_PASTDUE) {
	R45_30D_90D_PASTDUE = r45_30d_90d_PASTDUE;
}



public BigDecimal getR45_NON_PERFORM_LOANS() {
	return R45_NON_PERFORM_LOANS;
}



public void setR45_NON_PERFORM_LOANS(BigDecimal r45_NON_PERFORM_LOANS) {
	R45_NON_PERFORM_LOANS = r45_NON_PERFORM_LOANS;
}



public BigDecimal getR45_NON_ACCRUALS1() {
	return R45_NON_ACCRUALS1;
}



public void setR45_NON_ACCRUALS1(BigDecimal r45_NON_ACCRUALS1) {
	R45_NON_ACCRUALS1 = r45_NON_ACCRUALS1;
}



public BigDecimal getR45_SPECIFIC_PROV1() {
	return R45_SPECIFIC_PROV1;
}



public void setR45_SPECIFIC_PROV1(BigDecimal r45_SPECIFIC_PROV1) {
	R45_SPECIFIC_PROV1 = r45_SPECIFIC_PROV1;
}



public BigDecimal getR45_NO_OF_ACC1() {
	return R45_NO_OF_ACC1;
}



public void setR45_NO_OF_ACC1(BigDecimal r45_NO_OF_ACC1) {
	R45_NO_OF_ACC1 = r45_NO_OF_ACC1;
}



public BigDecimal getR45_90D_180D_PASTDUE() {
	return R45_90D_180D_PASTDUE;
}



public void setR45_90D_180D_PASTDUE(BigDecimal r45_90d_180d_PASTDUE) {
	R45_90D_180D_PASTDUE = r45_90d_180d_PASTDUE;
}



public BigDecimal getR45_NON_ACCRUALS2() {
	return R45_NON_ACCRUALS2;
}



public void setR45_NON_ACCRUALS2(BigDecimal r45_NON_ACCRUALS2) {
	R45_NON_ACCRUALS2 = r45_NON_ACCRUALS2;
}



public BigDecimal getR45_SPECIFIC_PROV2() {
	return R45_SPECIFIC_PROV2;
}



public void setR45_SPECIFIC_PROV2(BigDecimal r45_SPECIFIC_PROV2) {
	R45_SPECIFIC_PROV2 = r45_SPECIFIC_PROV2;
}



public BigDecimal getR45_NO_OF_ACC2() {
	return R45_NO_OF_ACC2;
}



public void setR45_NO_OF_ACC2(BigDecimal r45_NO_OF_ACC2) {
	R45_NO_OF_ACC2 = r45_NO_OF_ACC2;
}



public BigDecimal getR45_TOTAL_NON_ACCRUAL() {
	return R45_TOTAL_NON_ACCRUAL;
}



public void setR45_TOTAL_NON_ACCRUAL(BigDecimal r45_TOTAL_NON_ACCRUAL) {
	R45_TOTAL_NON_ACCRUAL = r45_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR45_TOTAL_DUE_LOANS() {
	return R45_TOTAL_DUE_LOANS;
}



public void setR45_TOTAL_DUE_LOANS(BigDecimal r45_TOTAL_DUE_LOANS) {
	R45_TOTAL_DUE_LOANS = r45_TOTAL_DUE_LOANS;
}



public BigDecimal getR45_TOTAL_PERFORMING_LOAN() {
	return R45_TOTAL_PERFORMING_LOAN;
}



public void setR45_TOTAL_PERFORMING_LOAN(BigDecimal r45_TOTAL_PERFORMING_LOAN) {
	R45_TOTAL_PERFORMING_LOAN = r45_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR45_VALUE_OF_COLLATERAL() {
	return R45_VALUE_OF_COLLATERAL;
}



public void setR45_VALUE_OF_COLLATERAL(BigDecimal r45_VALUE_OF_COLLATERAL) {
	R45_VALUE_OF_COLLATERAL = r45_VALUE_OF_COLLATERAL;
}



public BigDecimal getR45_TOTAL_VALUE_NPL() {
	return R45_TOTAL_VALUE_NPL;
}



public void setR45_TOTAL_VALUE_NPL(BigDecimal r45_TOTAL_VALUE_NPL) {
	R45_TOTAL_VALUE_NPL = r45_TOTAL_VALUE_NPL;
}



public BigDecimal getR45_TOTAL_SPECIFIC_PROV() {
	return R45_TOTAL_SPECIFIC_PROV;
}



public void setR45_TOTAL_SPECIFIC_PROV(BigDecimal r45_TOTAL_SPECIFIC_PROV) {
	R45_TOTAL_SPECIFIC_PROV = r45_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR45_SPECIFIC_PROV_NPL() {
	return R45_SPECIFIC_PROV_NPL;
}



public void setR45_SPECIFIC_PROV_NPL(BigDecimal r45_SPECIFIC_PROV_NPL) {
	R45_SPECIFIC_PROV_NPL = r45_SPECIFIC_PROV_NPL;
}



public BigDecimal getR46_30D_90D_PASTDUE() {
	return R46_30D_90D_PASTDUE;
}



public void setR46_30D_90D_PASTDUE(BigDecimal r46_30d_90d_PASTDUE) {
	R46_30D_90D_PASTDUE = r46_30d_90d_PASTDUE;
}



public BigDecimal getR46_NON_PERFORM_LOANS() {
	return R46_NON_PERFORM_LOANS;
}



public void setR46_NON_PERFORM_LOANS(BigDecimal r46_NON_PERFORM_LOANS) {
	R46_NON_PERFORM_LOANS = r46_NON_PERFORM_LOANS;
}



public BigDecimal getR46_NON_ACCRUALS1() {
	return R46_NON_ACCRUALS1;
}



public void setR46_NON_ACCRUALS1(BigDecimal r46_NON_ACCRUALS1) {
	R46_NON_ACCRUALS1 = r46_NON_ACCRUALS1;
}



public BigDecimal getR46_SPECIFIC_PROV1() {
	return R46_SPECIFIC_PROV1;
}



public void setR46_SPECIFIC_PROV1(BigDecimal r46_SPECIFIC_PROV1) {
	R46_SPECIFIC_PROV1 = r46_SPECIFIC_PROV1;
}



public BigDecimal getR46_NO_OF_ACC1() {
	return R46_NO_OF_ACC1;
}



public void setR46_NO_OF_ACC1(BigDecimal r46_NO_OF_ACC1) {
	R46_NO_OF_ACC1 = r46_NO_OF_ACC1;
}



public BigDecimal getR46_90D_180D_PASTDUE() {
	return R46_90D_180D_PASTDUE;
}



public void setR46_90D_180D_PASTDUE(BigDecimal r46_90d_180d_PASTDUE) {
	R46_90D_180D_PASTDUE = r46_90d_180d_PASTDUE;
}



public BigDecimal getR46_NON_ACCRUALS2() {
	return R46_NON_ACCRUALS2;
}



public void setR46_NON_ACCRUALS2(BigDecimal r46_NON_ACCRUALS2) {
	R46_NON_ACCRUALS2 = r46_NON_ACCRUALS2;
}



public BigDecimal getR46_SPECIFIC_PROV2() {
	return R46_SPECIFIC_PROV2;
}



public void setR46_SPECIFIC_PROV2(BigDecimal r46_SPECIFIC_PROV2) {
	R46_SPECIFIC_PROV2 = r46_SPECIFIC_PROV2;
}



public BigDecimal getR46_NO_OF_ACC2() {
	return R46_NO_OF_ACC2;
}



public void setR46_NO_OF_ACC2(BigDecimal r46_NO_OF_ACC2) {
	R46_NO_OF_ACC2 = r46_NO_OF_ACC2;
}



public BigDecimal getR46_180D_ABOVE_PASTDUE() {
	return R46_180D_ABOVE_PASTDUE;
}



public void setR46_180D_ABOVE_PASTDUE(BigDecimal r46_180d_ABOVE_PASTDUE) {
	R46_180D_ABOVE_PASTDUE = r46_180d_ABOVE_PASTDUE;
}



public BigDecimal getR46_NON_ACCRUALS3() {
	return R46_NON_ACCRUALS3;
}



public void setR46_NON_ACCRUALS3(BigDecimal r46_NON_ACCRUALS3) {
	R46_NON_ACCRUALS3 = r46_NON_ACCRUALS3;
}



public BigDecimal getR46_SPECIFIC_PROV3() {
	return R46_SPECIFIC_PROV3;
}



public void setR46_SPECIFIC_PROV3(BigDecimal r46_SPECIFIC_PROV3) {
	R46_SPECIFIC_PROV3 = r46_SPECIFIC_PROV3;
}



public BigDecimal getR46_NO_OF_ACC3() {
	return R46_NO_OF_ACC3;
}



public void setR46_NO_OF_ACC3(BigDecimal r46_NO_OF_ACC3) {
	R46_NO_OF_ACC3 = r46_NO_OF_ACC3;
}



public BigDecimal getR46_TOTAL_NON_ACCRUAL() {
	return R46_TOTAL_NON_ACCRUAL;
}



public void setR46_TOTAL_NON_ACCRUAL(BigDecimal r46_TOTAL_NON_ACCRUAL) {
	R46_TOTAL_NON_ACCRUAL = r46_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR46_TOTAL_DUE_LOANS() {
	return R46_TOTAL_DUE_LOANS;
}



public void setR46_TOTAL_DUE_LOANS(BigDecimal r46_TOTAL_DUE_LOANS) {
	R46_TOTAL_DUE_LOANS = r46_TOTAL_DUE_LOANS;
}



public BigDecimal getR46_TOTAL_PERFORMING_LOAN() {
	return R46_TOTAL_PERFORMING_LOAN;
}



public void setR46_TOTAL_PERFORMING_LOAN(BigDecimal r46_TOTAL_PERFORMING_LOAN) {
	R46_TOTAL_PERFORMING_LOAN = r46_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR46_VALUE_OF_COLLATERAL() {
	return R46_VALUE_OF_COLLATERAL;
}



public void setR46_VALUE_OF_COLLATERAL(BigDecimal r46_VALUE_OF_COLLATERAL) {
	R46_VALUE_OF_COLLATERAL = r46_VALUE_OF_COLLATERAL;
}



public BigDecimal getR46_TOTAL_VALUE_NPL() {
	return R46_TOTAL_VALUE_NPL;
}



public void setR46_TOTAL_VALUE_NPL(BigDecimal r46_TOTAL_VALUE_NPL) {
	R46_TOTAL_VALUE_NPL = r46_TOTAL_VALUE_NPL;
}



public BigDecimal getR46_TOTAL_SPECIFIC_PROV() {
	return R46_TOTAL_SPECIFIC_PROV;
}



public void setR46_TOTAL_SPECIFIC_PROV(BigDecimal r46_TOTAL_SPECIFIC_PROV) {
	R46_TOTAL_SPECIFIC_PROV = r46_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR46_SPECIFIC_PROV_NPL() {
	return R46_SPECIFIC_PROV_NPL;
}



public void setR46_SPECIFIC_PROV_NPL(BigDecimal r46_SPECIFIC_PROV_NPL) {
	R46_SPECIFIC_PROV_NPL = r46_SPECIFIC_PROV_NPL;
}



public BigDecimal getR47_30D_90D_PASTDUE() {
	return R47_30D_90D_PASTDUE;
}



public void setR47_30D_90D_PASTDUE(BigDecimal r47_30d_90d_PASTDUE) {
	R47_30D_90D_PASTDUE = r47_30d_90d_PASTDUE;
}



public BigDecimal getR47_NON_PERFORM_LOANS() {
	return R47_NON_PERFORM_LOANS;
}



public void setR47_NON_PERFORM_LOANS(BigDecimal r47_NON_PERFORM_LOANS) {
	R47_NON_PERFORM_LOANS = r47_NON_PERFORM_LOANS;
}



public BigDecimal getR47_NON_ACCRUALS1() {
	return R47_NON_ACCRUALS1;
}



public void setR47_NON_ACCRUALS1(BigDecimal r47_NON_ACCRUALS1) {
	R47_NON_ACCRUALS1 = r47_NON_ACCRUALS1;
}



public BigDecimal getR47_SPECIFIC_PROV1() {
	return R47_SPECIFIC_PROV1;
}



public void setR47_SPECIFIC_PROV1(BigDecimal r47_SPECIFIC_PROV1) {
	R47_SPECIFIC_PROV1 = r47_SPECIFIC_PROV1;
}



public BigDecimal getR47_NO_OF_ACC1() {
	return R47_NO_OF_ACC1;
}



public void setR47_NO_OF_ACC1(BigDecimal r47_NO_OF_ACC1) {
	R47_NO_OF_ACC1 = r47_NO_OF_ACC1;
}



public BigDecimal getR47_90D_180D_PASTDUE() {
	return R47_90D_180D_PASTDUE;
}



public void setR47_90D_180D_PASTDUE(BigDecimal r47_90d_180d_PASTDUE) {
	R47_90D_180D_PASTDUE = r47_90d_180d_PASTDUE;
}



public BigDecimal getR47_NON_ACCRUALS2() {
	return R47_NON_ACCRUALS2;
}



public void setR47_NON_ACCRUALS2(BigDecimal r47_NON_ACCRUALS2) {
	R47_NON_ACCRUALS2 = r47_NON_ACCRUALS2;
}



public BigDecimal getR47_SPECIFIC_PROV2() {
	return R47_SPECIFIC_PROV2;
}



public void setR47_SPECIFIC_PROV2(BigDecimal r47_SPECIFIC_PROV2) {
	R47_SPECIFIC_PROV2 = r47_SPECIFIC_PROV2;
}



public BigDecimal getR47_NO_OF_ACC2() {
	return R47_NO_OF_ACC2;
}



public void setR47_NO_OF_ACC2(BigDecimal r47_NO_OF_ACC2) {
	R47_NO_OF_ACC2 = r47_NO_OF_ACC2;
}



public BigDecimal getR47_TOTAL_NON_ACCRUAL() {
	return R47_TOTAL_NON_ACCRUAL;
}



public void setR47_TOTAL_NON_ACCRUAL(BigDecimal r47_TOTAL_NON_ACCRUAL) {
	R47_TOTAL_NON_ACCRUAL = r47_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR47_TOTAL_DUE_LOANS() {
	return R47_TOTAL_DUE_LOANS;
}



public void setR47_TOTAL_DUE_LOANS(BigDecimal r47_TOTAL_DUE_LOANS) {
	R47_TOTAL_DUE_LOANS = r47_TOTAL_DUE_LOANS;
}



public BigDecimal getR47_TOTAL_PERFORMING_LOAN() {
	return R47_TOTAL_PERFORMING_LOAN;
}



public void setR47_TOTAL_PERFORMING_LOAN(BigDecimal r47_TOTAL_PERFORMING_LOAN) {
	R47_TOTAL_PERFORMING_LOAN = r47_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR47_VALUE_OF_COLLATERAL() {
	return R47_VALUE_OF_COLLATERAL;
}



public void setR47_VALUE_OF_COLLATERAL(BigDecimal r47_VALUE_OF_COLLATERAL) {
	R47_VALUE_OF_COLLATERAL = r47_VALUE_OF_COLLATERAL;
}



public BigDecimal getR47_TOTAL_VALUE_NPL() {
	return R47_TOTAL_VALUE_NPL;
}



public void setR47_TOTAL_VALUE_NPL(BigDecimal r47_TOTAL_VALUE_NPL) {
	R47_TOTAL_VALUE_NPL = r47_TOTAL_VALUE_NPL;
}



public BigDecimal getR47_TOTAL_SPECIFIC_PROV() {
	return R47_TOTAL_SPECIFIC_PROV;
}



public void setR47_TOTAL_SPECIFIC_PROV(BigDecimal r47_TOTAL_SPECIFIC_PROV) {
	R47_TOTAL_SPECIFIC_PROV = r47_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR47_SPECIFIC_PROV_NPL() {
	return R47_SPECIFIC_PROV_NPL;
}



public void setR47_SPECIFIC_PROV_NPL(BigDecimal r47_SPECIFIC_PROV_NPL) {
	R47_SPECIFIC_PROV_NPL = r47_SPECIFIC_PROV_NPL;
}



public BigDecimal getR48_30D_90D_PASTDUE() {
	return R48_30D_90D_PASTDUE;
}



public void setR48_30D_90D_PASTDUE(BigDecimal r48_30d_90d_PASTDUE) {
	R48_30D_90D_PASTDUE = r48_30d_90d_PASTDUE;
}



public BigDecimal getR48_NON_PERFORM_LOANS() {
	return R48_NON_PERFORM_LOANS;
}



public void setR48_NON_PERFORM_LOANS(BigDecimal r48_NON_PERFORM_LOANS) {
	R48_NON_PERFORM_LOANS = r48_NON_PERFORM_LOANS;
}



public BigDecimal getR48_NON_ACCRUALS1() {
	return R48_NON_ACCRUALS1;
}



public void setR48_NON_ACCRUALS1(BigDecimal r48_NON_ACCRUALS1) {
	R48_NON_ACCRUALS1 = r48_NON_ACCRUALS1;
}



public BigDecimal getR48_SPECIFIC_PROV1() {
	return R48_SPECIFIC_PROV1;
}



public void setR48_SPECIFIC_PROV1(BigDecimal r48_SPECIFIC_PROV1) {
	R48_SPECIFIC_PROV1 = r48_SPECIFIC_PROV1;
}



public BigDecimal getR48_NO_OF_ACC1() {
	return R48_NO_OF_ACC1;
}



public void setR48_NO_OF_ACC1(BigDecimal r48_NO_OF_ACC1) {
	R48_NO_OF_ACC1 = r48_NO_OF_ACC1;
}



public BigDecimal getR48_90D_180D_PASTDUE() {
	return R48_90D_180D_PASTDUE;
}



public void setR48_90D_180D_PASTDUE(BigDecimal r48_90d_180d_PASTDUE) {
	R48_90D_180D_PASTDUE = r48_90d_180d_PASTDUE;
}



public BigDecimal getR48_NON_ACCRUALS2() {
	return R48_NON_ACCRUALS2;
}



public void setR48_NON_ACCRUALS2(BigDecimal r48_NON_ACCRUALS2) {
	R48_NON_ACCRUALS2 = r48_NON_ACCRUALS2;
}



public BigDecimal getR48_SPECIFIC_PROV2() {
	return R48_SPECIFIC_PROV2;
}



public void setR48_SPECIFIC_PROV2(BigDecimal r48_SPECIFIC_PROV2) {
	R48_SPECIFIC_PROV2 = r48_SPECIFIC_PROV2;
}



public BigDecimal getR48_NO_OF_ACC2() {
	return R48_NO_OF_ACC2;
}



public void setR48_NO_OF_ACC2(BigDecimal r48_NO_OF_ACC2) {
	R48_NO_OF_ACC2 = r48_NO_OF_ACC2;
}



public BigDecimal getR48_TOTAL_NON_ACCRUAL() {
	return R48_TOTAL_NON_ACCRUAL;
}



public void setR48_TOTAL_NON_ACCRUAL(BigDecimal r48_TOTAL_NON_ACCRUAL) {
	R48_TOTAL_NON_ACCRUAL = r48_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR48_TOTAL_DUE_LOANS() {
	return R48_TOTAL_DUE_LOANS;
}



public void setR48_TOTAL_DUE_LOANS(BigDecimal r48_TOTAL_DUE_LOANS) {
	R48_TOTAL_DUE_LOANS = r48_TOTAL_DUE_LOANS;
}



public BigDecimal getR48_TOTAL_PERFORMING_LOAN() {
	return R48_TOTAL_PERFORMING_LOAN;
}



public void setR48_TOTAL_PERFORMING_LOAN(BigDecimal r48_TOTAL_PERFORMING_LOAN) {
	R48_TOTAL_PERFORMING_LOAN = r48_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR48_VALUE_OF_COLLATERAL() {
	return R48_VALUE_OF_COLLATERAL;
}



public void setR48_VALUE_OF_COLLATERAL(BigDecimal r48_VALUE_OF_COLLATERAL) {
	R48_VALUE_OF_COLLATERAL = r48_VALUE_OF_COLLATERAL;
}



public BigDecimal getR48_TOTAL_VALUE_NPL() {
	return R48_TOTAL_VALUE_NPL;
}



public void setR48_TOTAL_VALUE_NPL(BigDecimal r48_TOTAL_VALUE_NPL) {
	R48_TOTAL_VALUE_NPL = r48_TOTAL_VALUE_NPL;
}



public BigDecimal getR48_TOTAL_SPECIFIC_PROV() {
	return R48_TOTAL_SPECIFIC_PROV;
}



public void setR48_TOTAL_SPECIFIC_PROV(BigDecimal r48_TOTAL_SPECIFIC_PROV) {
	R48_TOTAL_SPECIFIC_PROV = r48_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR48_SPECIFIC_PROV_NPL() {
	return R48_SPECIFIC_PROV_NPL;
}



public void setR48_SPECIFIC_PROV_NPL(BigDecimal r48_SPECIFIC_PROV_NPL) {
	R48_SPECIFIC_PROV_NPL = r48_SPECIFIC_PROV_NPL;
}



public BigDecimal getR49_30D_90D_PASTDUE() {
	return R49_30D_90D_PASTDUE;
}



public void setR49_30D_90D_PASTDUE(BigDecimal r49_30d_90d_PASTDUE) {
	R49_30D_90D_PASTDUE = r49_30d_90d_PASTDUE;
}



public BigDecimal getR49_NON_PERFORM_LOANS() {
	return R49_NON_PERFORM_LOANS;
}



public void setR49_NON_PERFORM_LOANS(BigDecimal r49_NON_PERFORM_LOANS) {
	R49_NON_PERFORM_LOANS = r49_NON_PERFORM_LOANS;
}



public BigDecimal getR49_NON_ACCRUALS1() {
	return R49_NON_ACCRUALS1;
}



public void setR49_NON_ACCRUALS1(BigDecimal r49_NON_ACCRUALS1) {
	R49_NON_ACCRUALS1 = r49_NON_ACCRUALS1;
}



public BigDecimal getR49_SPECIFIC_PROV1() {
	return R49_SPECIFIC_PROV1;
}



public void setR49_SPECIFIC_PROV1(BigDecimal r49_SPECIFIC_PROV1) {
	R49_SPECIFIC_PROV1 = r49_SPECIFIC_PROV1;
}



public BigDecimal getR49_NO_OF_ACC1() {
	return R49_NO_OF_ACC1;
}



public void setR49_NO_OF_ACC1(BigDecimal r49_NO_OF_ACC1) {
	R49_NO_OF_ACC1 = r49_NO_OF_ACC1;
}



public BigDecimal getR49_90D_180D_PASTDUE() {
	return R49_90D_180D_PASTDUE;
}



public void setR49_90D_180D_PASTDUE(BigDecimal r49_90d_180d_PASTDUE) {
	R49_90D_180D_PASTDUE = r49_90d_180d_PASTDUE;
}



public BigDecimal getR49_NON_ACCRUALS2() {
	return R49_NON_ACCRUALS2;
}



public void setR49_NON_ACCRUALS2(BigDecimal r49_NON_ACCRUALS2) {
	R49_NON_ACCRUALS2 = r49_NON_ACCRUALS2;
}



public BigDecimal getR49_SPECIFIC_PROV2() {
	return R49_SPECIFIC_PROV2;
}



public void setR49_SPECIFIC_PROV2(BigDecimal r49_SPECIFIC_PROV2) {
	R49_SPECIFIC_PROV2 = r49_SPECIFIC_PROV2;
}



public BigDecimal getR49_NO_OF_ACC2() {
	return R49_NO_OF_ACC2;
}



public void setR49_NO_OF_ACC2(BigDecimal r49_NO_OF_ACC2) {
	R49_NO_OF_ACC2 = r49_NO_OF_ACC2;
}



public BigDecimal getR49_TOTAL_NON_ACCRUAL() {
	return R49_TOTAL_NON_ACCRUAL;
}



public void setR49_TOTAL_NON_ACCRUAL(BigDecimal r49_TOTAL_NON_ACCRUAL) {
	R49_TOTAL_NON_ACCRUAL = r49_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR49_TOTAL_DUE_LOANS() {
	return R49_TOTAL_DUE_LOANS;
}



public void setR49_TOTAL_DUE_LOANS(BigDecimal r49_TOTAL_DUE_LOANS) {
	R49_TOTAL_DUE_LOANS = r49_TOTAL_DUE_LOANS;
}



public BigDecimal getR49_TOTAL_PERFORMING_LOAN() {
	return R49_TOTAL_PERFORMING_LOAN;
}



public void setR49_TOTAL_PERFORMING_LOAN(BigDecimal r49_TOTAL_PERFORMING_LOAN) {
	R49_TOTAL_PERFORMING_LOAN = r49_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR49_VALUE_OF_COLLATERAL() {
	return R49_VALUE_OF_COLLATERAL;
}



public void setR49_VALUE_OF_COLLATERAL(BigDecimal r49_VALUE_OF_COLLATERAL) {
	R49_VALUE_OF_COLLATERAL = r49_VALUE_OF_COLLATERAL;
}



public BigDecimal getR49_TOTAL_VALUE_NPL() {
	return R49_TOTAL_VALUE_NPL;
}



public void setR49_TOTAL_VALUE_NPL(BigDecimal r49_TOTAL_VALUE_NPL) {
	R49_TOTAL_VALUE_NPL = r49_TOTAL_VALUE_NPL;
}



public BigDecimal getR49_TOTAL_SPECIFIC_PROV() {
	return R49_TOTAL_SPECIFIC_PROV;
}



public void setR49_TOTAL_SPECIFIC_PROV(BigDecimal r49_TOTAL_SPECIFIC_PROV) {
	R49_TOTAL_SPECIFIC_PROV = r49_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR49_SPECIFIC_PROV_NPL() {
	return R49_SPECIFIC_PROV_NPL;
}



public void setR49_SPECIFIC_PROV_NPL(BigDecimal r49_SPECIFIC_PROV_NPL) {
	R49_SPECIFIC_PROV_NPL = r49_SPECIFIC_PROV_NPL;
}



public BigDecimal getR50_30D_90D_PASTDUE() {
	return R50_30D_90D_PASTDUE;
}



public void setR50_30D_90D_PASTDUE(BigDecimal r50_30d_90d_PASTDUE) {
	R50_30D_90D_PASTDUE = r50_30d_90d_PASTDUE;
}



public BigDecimal getR50_NON_PERFORM_LOANS() {
	return R50_NON_PERFORM_LOANS;
}



public void setR50_NON_PERFORM_LOANS(BigDecimal r50_NON_PERFORM_LOANS) {
	R50_NON_PERFORM_LOANS = r50_NON_PERFORM_LOANS;
}



public BigDecimal getR50_NON_ACCRUALS1() {
	return R50_NON_ACCRUALS1;
}



public void setR50_NON_ACCRUALS1(BigDecimal r50_NON_ACCRUALS1) {
	R50_NON_ACCRUALS1 = r50_NON_ACCRUALS1;
}



public BigDecimal getR50_SPECIFIC_PROV1() {
	return R50_SPECIFIC_PROV1;
}



public void setR50_SPECIFIC_PROV1(BigDecimal r50_SPECIFIC_PROV1) {
	R50_SPECIFIC_PROV1 = r50_SPECIFIC_PROV1;
}



public BigDecimal getR50_NO_OF_ACC1() {
	return R50_NO_OF_ACC1;
}



public void setR50_NO_OF_ACC1(BigDecimal r50_NO_OF_ACC1) {
	R50_NO_OF_ACC1 = r50_NO_OF_ACC1;
}



public BigDecimal getR50_90D_180D_PASTDUE() {
	return R50_90D_180D_PASTDUE;
}



public void setR50_90D_180D_PASTDUE(BigDecimal r50_90d_180d_PASTDUE) {
	R50_90D_180D_PASTDUE = r50_90d_180d_PASTDUE;
}



public BigDecimal getR50_NON_ACCRUALS2() {
	return R50_NON_ACCRUALS2;
}



public void setR50_NON_ACCRUALS2(BigDecimal r50_NON_ACCRUALS2) {
	R50_NON_ACCRUALS2 = r50_NON_ACCRUALS2;
}



public BigDecimal getR50_SPECIFIC_PROV2() {
	return R50_SPECIFIC_PROV2;
}



public void setR50_SPECIFIC_PROV2(BigDecimal r50_SPECIFIC_PROV2) {
	R50_SPECIFIC_PROV2 = r50_SPECIFIC_PROV2;
}



public BigDecimal getR50_NO_OF_ACC2() {
	return R50_NO_OF_ACC2;
}



public void setR50_NO_OF_ACC2(BigDecimal r50_NO_OF_ACC2) {
	R50_NO_OF_ACC2 = r50_NO_OF_ACC2;
}



public BigDecimal getR50_180D_ABOVE_PASTDUE() {
	return R50_180D_ABOVE_PASTDUE;
}



public void setR50_180D_ABOVE_PASTDUE(BigDecimal r50_180d_ABOVE_PASTDUE) {
	R50_180D_ABOVE_PASTDUE = r50_180d_ABOVE_PASTDUE;
}



public BigDecimal getR50_NON_ACCRUALS3() {
	return R50_NON_ACCRUALS3;
}



public void setR50_NON_ACCRUALS3(BigDecimal r50_NON_ACCRUALS3) {
	R50_NON_ACCRUALS3 = r50_NON_ACCRUALS3;
}



public BigDecimal getR50_SPECIFIC_PROV3() {
	return R50_SPECIFIC_PROV3;
}



public void setR50_SPECIFIC_PROV3(BigDecimal r50_SPECIFIC_PROV3) {
	R50_SPECIFIC_PROV3 = r50_SPECIFIC_PROV3;
}



public BigDecimal getR50_NO_OF_ACC3() {
	return R50_NO_OF_ACC3;
}



public void setR50_NO_OF_ACC3(BigDecimal r50_NO_OF_ACC3) {
	R50_NO_OF_ACC3 = r50_NO_OF_ACC3;
}



public BigDecimal getR50_TOTAL_NON_ACCRUAL() {
	return R50_TOTAL_NON_ACCRUAL;
}



public void setR50_TOTAL_NON_ACCRUAL(BigDecimal r50_TOTAL_NON_ACCRUAL) {
	R50_TOTAL_NON_ACCRUAL = r50_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR50_TOTAL_DUE_LOANS() {
	return R50_TOTAL_DUE_LOANS;
}



public void setR50_TOTAL_DUE_LOANS(BigDecimal r50_TOTAL_DUE_LOANS) {
	R50_TOTAL_DUE_LOANS = r50_TOTAL_DUE_LOANS;
}



public BigDecimal getR50_TOTAL_PERFORMING_LOAN() {
	return R50_TOTAL_PERFORMING_LOAN;
}



public void setR50_TOTAL_PERFORMING_LOAN(BigDecimal r50_TOTAL_PERFORMING_LOAN) {
	R50_TOTAL_PERFORMING_LOAN = r50_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR50_VALUE_OF_COLLATERAL() {
	return R50_VALUE_OF_COLLATERAL;
}



public void setR50_VALUE_OF_COLLATERAL(BigDecimal r50_VALUE_OF_COLLATERAL) {
	R50_VALUE_OF_COLLATERAL = r50_VALUE_OF_COLLATERAL;
}



public BigDecimal getR50_TOTAL_VALUE_NPL() {
	return R50_TOTAL_VALUE_NPL;
}



public void setR50_TOTAL_VALUE_NPL(BigDecimal r50_TOTAL_VALUE_NPL) {
	R50_TOTAL_VALUE_NPL = r50_TOTAL_VALUE_NPL;
}



public BigDecimal getR50_TOTAL_SPECIFIC_PROV() {
	return R50_TOTAL_SPECIFIC_PROV;
}



public void setR50_TOTAL_SPECIFIC_PROV(BigDecimal r50_TOTAL_SPECIFIC_PROV) {
	R50_TOTAL_SPECIFIC_PROV = r50_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR50_SPECIFIC_PROV_NPL() {
	return R50_SPECIFIC_PROV_NPL;
}



public void setR50_SPECIFIC_PROV_NPL(BigDecimal r50_SPECIFIC_PROV_NPL) {
	R50_SPECIFIC_PROV_NPL = r50_SPECIFIC_PROV_NPL;
}



public BigDecimal getR51_30D_90D_PASTDUE() {
	return R51_30D_90D_PASTDUE;
}



public void setR51_30D_90D_PASTDUE(BigDecimal r51_30d_90d_PASTDUE) {
	R51_30D_90D_PASTDUE = r51_30d_90d_PASTDUE;
}



public BigDecimal getR51_NON_PERFORM_LOANS() {
	return R51_NON_PERFORM_LOANS;
}



public void setR51_NON_PERFORM_LOANS(BigDecimal r51_NON_PERFORM_LOANS) {
	R51_NON_PERFORM_LOANS = r51_NON_PERFORM_LOANS;
}



public BigDecimal getR51_NON_ACCRUALS1() {
	return R51_NON_ACCRUALS1;
}



public void setR51_NON_ACCRUALS1(BigDecimal r51_NON_ACCRUALS1) {
	R51_NON_ACCRUALS1 = r51_NON_ACCRUALS1;
}



public BigDecimal getR51_SPECIFIC_PROV1() {
	return R51_SPECIFIC_PROV1;
}



public void setR51_SPECIFIC_PROV1(BigDecimal r51_SPECIFIC_PROV1) {
	R51_SPECIFIC_PROV1 = r51_SPECIFIC_PROV1;
}



public BigDecimal getR51_NO_OF_ACC1() {
	return R51_NO_OF_ACC1;
}



public void setR51_NO_OF_ACC1(BigDecimal r51_NO_OF_ACC1) {
	R51_NO_OF_ACC1 = r51_NO_OF_ACC1;
}



public BigDecimal getR51_90D_180D_PASTDUE() {
	return R51_90D_180D_PASTDUE;
}



public void setR51_90D_180D_PASTDUE(BigDecimal r51_90d_180d_PASTDUE) {
	R51_90D_180D_PASTDUE = r51_90d_180d_PASTDUE;
}



public BigDecimal getR51_NON_ACCRUALS2() {
	return R51_NON_ACCRUALS2;
}



public void setR51_NON_ACCRUALS2(BigDecimal r51_NON_ACCRUALS2) {
	R51_NON_ACCRUALS2 = r51_NON_ACCRUALS2;
}



public BigDecimal getR51_SPECIFIC_PROV2() {
	return R51_SPECIFIC_PROV2;
}



public void setR51_SPECIFIC_PROV2(BigDecimal r51_SPECIFIC_PROV2) {
	R51_SPECIFIC_PROV2 = r51_SPECIFIC_PROV2;
}



public BigDecimal getR51_NO_OF_ACC2() {
	return R51_NO_OF_ACC2;
}



public void setR51_NO_OF_ACC2(BigDecimal r51_NO_OF_ACC2) {
	R51_NO_OF_ACC2 = r51_NO_OF_ACC2;
}



public BigDecimal getR51_TOTAL_NON_ACCRUAL() {
	return R51_TOTAL_NON_ACCRUAL;
}



public void setR51_TOTAL_NON_ACCRUAL(BigDecimal r51_TOTAL_NON_ACCRUAL) {
	R51_TOTAL_NON_ACCRUAL = r51_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR51_TOTAL_DUE_LOANS() {
	return R51_TOTAL_DUE_LOANS;
}



public void setR51_TOTAL_DUE_LOANS(BigDecimal r51_TOTAL_DUE_LOANS) {
	R51_TOTAL_DUE_LOANS = r51_TOTAL_DUE_LOANS;
}



public BigDecimal getR51_TOTAL_PERFORMING_LOAN() {
	return R51_TOTAL_PERFORMING_LOAN;
}



public void setR51_TOTAL_PERFORMING_LOAN(BigDecimal r51_TOTAL_PERFORMING_LOAN) {
	R51_TOTAL_PERFORMING_LOAN = r51_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR51_VALUE_OF_COLLATERAL() {
	return R51_VALUE_OF_COLLATERAL;
}



public void setR51_VALUE_OF_COLLATERAL(BigDecimal r51_VALUE_OF_COLLATERAL) {
	R51_VALUE_OF_COLLATERAL = r51_VALUE_OF_COLLATERAL;
}



public BigDecimal getR51_TOTAL_VALUE_NPL() {
	return R51_TOTAL_VALUE_NPL;
}



public void setR51_TOTAL_VALUE_NPL(BigDecimal r51_TOTAL_VALUE_NPL) {
	R51_TOTAL_VALUE_NPL = r51_TOTAL_VALUE_NPL;
}



public BigDecimal getR51_TOTAL_SPECIFIC_PROV() {
	return R51_TOTAL_SPECIFIC_PROV;
}



public void setR51_TOTAL_SPECIFIC_PROV(BigDecimal r51_TOTAL_SPECIFIC_PROV) {
	R51_TOTAL_SPECIFIC_PROV = r51_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR51_SPECIFIC_PROV_NPL() {
	return R51_SPECIFIC_PROV_NPL;
}



public void setR51_SPECIFIC_PROV_NPL(BigDecimal r51_SPECIFIC_PROV_NPL) {
	R51_SPECIFIC_PROV_NPL = r51_SPECIFIC_PROV_NPL;
}



public BigDecimal getR52_30D_90D_PASTDUE() {
	return R52_30D_90D_PASTDUE;
}



public void setR52_30D_90D_PASTDUE(BigDecimal r52_30d_90d_PASTDUE) {
	R52_30D_90D_PASTDUE = r52_30d_90d_PASTDUE;
}



public BigDecimal getR52_NON_PERFORM_LOANS() {
	return R52_NON_PERFORM_LOANS;
}



public void setR52_NON_PERFORM_LOANS(BigDecimal r52_NON_PERFORM_LOANS) {
	R52_NON_PERFORM_LOANS = r52_NON_PERFORM_LOANS;
}



public BigDecimal getR52_NON_ACCRUALS1() {
	return R52_NON_ACCRUALS1;
}



public void setR52_NON_ACCRUALS1(BigDecimal r52_NON_ACCRUALS1) {
	R52_NON_ACCRUALS1 = r52_NON_ACCRUALS1;
}



public BigDecimal getR52_SPECIFIC_PROV1() {
	return R52_SPECIFIC_PROV1;
}



public void setR52_SPECIFIC_PROV1(BigDecimal r52_SPECIFIC_PROV1) {
	R52_SPECIFIC_PROV1 = r52_SPECIFIC_PROV1;
}



public BigDecimal getR52_NO_OF_ACC1() {
	return R52_NO_OF_ACC1;
}



public void setR52_NO_OF_ACC1(BigDecimal r52_NO_OF_ACC1) {
	R52_NO_OF_ACC1 = r52_NO_OF_ACC1;
}



public BigDecimal getR52_90D_180D_PASTDUE() {
	return R52_90D_180D_PASTDUE;
}



public void setR52_90D_180D_PASTDUE(BigDecimal r52_90d_180d_PASTDUE) {
	R52_90D_180D_PASTDUE = r52_90d_180d_PASTDUE;
}



public BigDecimal getR52_NON_ACCRUALS2() {
	return R52_NON_ACCRUALS2;
}



public void setR52_NON_ACCRUALS2(BigDecimal r52_NON_ACCRUALS2) {
	R52_NON_ACCRUALS2 = r52_NON_ACCRUALS2;
}



public BigDecimal getR52_SPECIFIC_PROV2() {
	return R52_SPECIFIC_PROV2;
}



public void setR52_SPECIFIC_PROV2(BigDecimal r52_SPECIFIC_PROV2) {
	R52_SPECIFIC_PROV2 = r52_SPECIFIC_PROV2;
}



public BigDecimal getR52_NO_OF_ACC2() {
	return R52_NO_OF_ACC2;
}



public void setR52_NO_OF_ACC2(BigDecimal r52_NO_OF_ACC2) {
	R52_NO_OF_ACC2 = r52_NO_OF_ACC2;
}



public BigDecimal getR52_TOTAL_NON_ACCRUAL() {
	return R52_TOTAL_NON_ACCRUAL;
}



public void setR52_TOTAL_NON_ACCRUAL(BigDecimal r52_TOTAL_NON_ACCRUAL) {
	R52_TOTAL_NON_ACCRUAL = r52_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR52_TOTAL_DUE_LOANS() {
	return R52_TOTAL_DUE_LOANS;
}



public void setR52_TOTAL_DUE_LOANS(BigDecimal r52_TOTAL_DUE_LOANS) {
	R52_TOTAL_DUE_LOANS = r52_TOTAL_DUE_LOANS;
}



public BigDecimal getR52_TOTAL_PERFORMING_LOAN() {
	return R52_TOTAL_PERFORMING_LOAN;
}



public void setR52_TOTAL_PERFORMING_LOAN(BigDecimal r52_TOTAL_PERFORMING_LOAN) {
	R52_TOTAL_PERFORMING_LOAN = r52_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR52_VALUE_OF_COLLATERAL() {
	return R52_VALUE_OF_COLLATERAL;
}



public void setR52_VALUE_OF_COLLATERAL(BigDecimal r52_VALUE_OF_COLLATERAL) {
	R52_VALUE_OF_COLLATERAL = r52_VALUE_OF_COLLATERAL;
}



public BigDecimal getR52_TOTAL_VALUE_NPL() {
	return R52_TOTAL_VALUE_NPL;
}



public void setR52_TOTAL_VALUE_NPL(BigDecimal r52_TOTAL_VALUE_NPL) {
	R52_TOTAL_VALUE_NPL = r52_TOTAL_VALUE_NPL;
}



public BigDecimal getR52_TOTAL_SPECIFIC_PROV() {
	return R52_TOTAL_SPECIFIC_PROV;
}



public void setR52_TOTAL_SPECIFIC_PROV(BigDecimal r52_TOTAL_SPECIFIC_PROV) {
	R52_TOTAL_SPECIFIC_PROV = r52_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR52_SPECIFIC_PROV_NPL() {
	return R52_SPECIFIC_PROV_NPL;
}



public void setR52_SPECIFIC_PROV_NPL(BigDecimal r52_SPECIFIC_PROV_NPL) {
	R52_SPECIFIC_PROV_NPL = r52_SPECIFIC_PROV_NPL;
}



public BigDecimal getR53_30D_90D_PASTDUE() {
	return R53_30D_90D_PASTDUE;
}



public void setR53_30D_90D_PASTDUE(BigDecimal r53_30d_90d_PASTDUE) {
	R53_30D_90D_PASTDUE = r53_30d_90d_PASTDUE;
}



public BigDecimal getR53_NON_PERFORM_LOANS() {
	return R53_NON_PERFORM_LOANS;
}



public void setR53_NON_PERFORM_LOANS(BigDecimal r53_NON_PERFORM_LOANS) {
	R53_NON_PERFORM_LOANS = r53_NON_PERFORM_LOANS;
}



public BigDecimal getR53_NON_ACCRUALS1() {
	return R53_NON_ACCRUALS1;
}



public void setR53_NON_ACCRUALS1(BigDecimal r53_NON_ACCRUALS1) {
	R53_NON_ACCRUALS1 = r53_NON_ACCRUALS1;
}



public BigDecimal getR53_SPECIFIC_PROV1() {
	return R53_SPECIFIC_PROV1;
}



public void setR53_SPECIFIC_PROV1(BigDecimal r53_SPECIFIC_PROV1) {
	R53_SPECIFIC_PROV1 = r53_SPECIFIC_PROV1;
}



public BigDecimal getR53_NO_OF_ACC1() {
	return R53_NO_OF_ACC1;
}



public void setR53_NO_OF_ACC1(BigDecimal r53_NO_OF_ACC1) {
	R53_NO_OF_ACC1 = r53_NO_OF_ACC1;
}



public BigDecimal getR53_90D_180D_PASTDUE() {
	return R53_90D_180D_PASTDUE;
}



public void setR53_90D_180D_PASTDUE(BigDecimal r53_90d_180d_PASTDUE) {
	R53_90D_180D_PASTDUE = r53_90d_180d_PASTDUE;
}



public BigDecimal getR53_NON_ACCRUALS2() {
	return R53_NON_ACCRUALS2;
}



public void setR53_NON_ACCRUALS2(BigDecimal r53_NON_ACCRUALS2) {
	R53_NON_ACCRUALS2 = r53_NON_ACCRUALS2;
}



public BigDecimal getR53_SPECIFIC_PROV2() {
	return R53_SPECIFIC_PROV2;
}



public void setR53_SPECIFIC_PROV2(BigDecimal r53_SPECIFIC_PROV2) {
	R53_SPECIFIC_PROV2 = r53_SPECIFIC_PROV2;
}



public BigDecimal getR53_NO_OF_ACC2() {
	return R53_NO_OF_ACC2;
}



public void setR53_NO_OF_ACC2(BigDecimal r53_NO_OF_ACC2) {
	R53_NO_OF_ACC2 = r53_NO_OF_ACC2;
}



public BigDecimal getR53_TOTAL_NON_ACCRUAL() {
	return R53_TOTAL_NON_ACCRUAL;
}



public void setR53_TOTAL_NON_ACCRUAL(BigDecimal r53_TOTAL_NON_ACCRUAL) {
	R53_TOTAL_NON_ACCRUAL = r53_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR53_TOTAL_DUE_LOANS() {
	return R53_TOTAL_DUE_LOANS;
}



public void setR53_TOTAL_DUE_LOANS(BigDecimal r53_TOTAL_DUE_LOANS) {
	R53_TOTAL_DUE_LOANS = r53_TOTAL_DUE_LOANS;
}



public BigDecimal getR53_TOTAL_PERFORMING_LOAN() {
	return R53_TOTAL_PERFORMING_LOAN;
}



public void setR53_TOTAL_PERFORMING_LOAN(BigDecimal r53_TOTAL_PERFORMING_LOAN) {
	R53_TOTAL_PERFORMING_LOAN = r53_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR53_VALUE_OF_COLLATERAL() {
	return R53_VALUE_OF_COLLATERAL;
}



public void setR53_VALUE_OF_COLLATERAL(BigDecimal r53_VALUE_OF_COLLATERAL) {
	R53_VALUE_OF_COLLATERAL = r53_VALUE_OF_COLLATERAL;
}



public BigDecimal getR53_TOTAL_VALUE_NPL() {
	return R53_TOTAL_VALUE_NPL;
}



public void setR53_TOTAL_VALUE_NPL(BigDecimal r53_TOTAL_VALUE_NPL) {
	R53_TOTAL_VALUE_NPL = r53_TOTAL_VALUE_NPL;
}



public BigDecimal getR53_TOTAL_SPECIFIC_PROV() {
	return R53_TOTAL_SPECIFIC_PROV;
}



public void setR53_TOTAL_SPECIFIC_PROV(BigDecimal r53_TOTAL_SPECIFIC_PROV) {
	R53_TOTAL_SPECIFIC_PROV = r53_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR53_SPECIFIC_PROV_NPL() {
	return R53_SPECIFIC_PROV_NPL;
}



public void setR53_SPECIFIC_PROV_NPL(BigDecimal r53_SPECIFIC_PROV_NPL) {
	R53_SPECIFIC_PROV_NPL = r53_SPECIFIC_PROV_NPL;
}



public BigDecimal getR54_30D_90D_PASTDUE() {
	return R54_30D_90D_PASTDUE;
}



public void setR54_30D_90D_PASTDUE(BigDecimal r54_30d_90d_PASTDUE) {
	R54_30D_90D_PASTDUE = r54_30d_90d_PASTDUE;
}



public BigDecimal getR54_NON_PERFORM_LOANS() {
	return R54_NON_PERFORM_LOANS;
}



public void setR54_NON_PERFORM_LOANS(BigDecimal r54_NON_PERFORM_LOANS) {
	R54_NON_PERFORM_LOANS = r54_NON_PERFORM_LOANS;
}



public BigDecimal getR54_NON_ACCRUALS1() {
	return R54_NON_ACCRUALS1;
}



public void setR54_NON_ACCRUALS1(BigDecimal r54_NON_ACCRUALS1) {
	R54_NON_ACCRUALS1 = r54_NON_ACCRUALS1;
}



public BigDecimal getR54_SPECIFIC_PROV1() {
	return R54_SPECIFIC_PROV1;
}



public void setR54_SPECIFIC_PROV1(BigDecimal r54_SPECIFIC_PROV1) {
	R54_SPECIFIC_PROV1 = r54_SPECIFIC_PROV1;
}



public BigDecimal getR54_NO_OF_ACC1() {
	return R54_NO_OF_ACC1;
}



public void setR54_NO_OF_ACC1(BigDecimal r54_NO_OF_ACC1) {
	R54_NO_OF_ACC1 = r54_NO_OF_ACC1;
}



public BigDecimal getR54_90D_180D_PASTDUE() {
	return R54_90D_180D_PASTDUE;
}



public void setR54_90D_180D_PASTDUE(BigDecimal r54_90d_180d_PASTDUE) {
	R54_90D_180D_PASTDUE = r54_90d_180d_PASTDUE;
}



public BigDecimal getR54_NON_ACCRUALS2() {
	return R54_NON_ACCRUALS2;
}



public void setR54_NON_ACCRUALS2(BigDecimal r54_NON_ACCRUALS2) {
	R54_NON_ACCRUALS2 = r54_NON_ACCRUALS2;
}



public BigDecimal getR54_SPECIFIC_PROV2() {
	return R54_SPECIFIC_PROV2;
}



public void setR54_SPECIFIC_PROV2(BigDecimal r54_SPECIFIC_PROV2) {
	R54_SPECIFIC_PROV2 = r54_SPECIFIC_PROV2;
}



public BigDecimal getR54_NO_OF_ACC2() {
	return R54_NO_OF_ACC2;
}



public void setR54_NO_OF_ACC2(BigDecimal r54_NO_OF_ACC2) {
	R54_NO_OF_ACC2 = r54_NO_OF_ACC2;
}



public BigDecimal getR54_180D_ABOVE_PASTDUE() {
	return R54_180D_ABOVE_PASTDUE;
}



public void setR54_180D_ABOVE_PASTDUE(BigDecimal r54_180d_ABOVE_PASTDUE) {
	R54_180D_ABOVE_PASTDUE = r54_180d_ABOVE_PASTDUE;
}



public BigDecimal getR54_NON_ACCRUALS3() {
	return R54_NON_ACCRUALS3;
}



public void setR54_NON_ACCRUALS3(BigDecimal r54_NON_ACCRUALS3) {
	R54_NON_ACCRUALS3 = r54_NON_ACCRUALS3;
}



public BigDecimal getR54_SPECIFIC_PROV3() {
	return R54_SPECIFIC_PROV3;
}



public void setR54_SPECIFIC_PROV3(BigDecimal r54_SPECIFIC_PROV3) {
	R54_SPECIFIC_PROV3 = r54_SPECIFIC_PROV3;
}



public BigDecimal getR54_NO_OF_ACC3() {
	return R54_NO_OF_ACC3;
}



public void setR54_NO_OF_ACC3(BigDecimal r54_NO_OF_ACC3) {
	R54_NO_OF_ACC3 = r54_NO_OF_ACC3;
}



public BigDecimal getR54_TOTAL_NON_ACCRUAL() {
	return R54_TOTAL_NON_ACCRUAL;
}



public void setR54_TOTAL_NON_ACCRUAL(BigDecimal r54_TOTAL_NON_ACCRUAL) {
	R54_TOTAL_NON_ACCRUAL = r54_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR54_TOTAL_DUE_LOANS() {
	return R54_TOTAL_DUE_LOANS;
}



public void setR54_TOTAL_DUE_LOANS(BigDecimal r54_TOTAL_DUE_LOANS) {
	R54_TOTAL_DUE_LOANS = r54_TOTAL_DUE_LOANS;
}



public BigDecimal getR54_TOTAL_PERFORMING_LOAN() {
	return R54_TOTAL_PERFORMING_LOAN;
}



public void setR54_TOTAL_PERFORMING_LOAN(BigDecimal r54_TOTAL_PERFORMING_LOAN) {
	R54_TOTAL_PERFORMING_LOAN = r54_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR54_VALUE_OF_COLLATERAL() {
	return R54_VALUE_OF_COLLATERAL;
}



public void setR54_VALUE_OF_COLLATERAL(BigDecimal r54_VALUE_OF_COLLATERAL) {
	R54_VALUE_OF_COLLATERAL = r54_VALUE_OF_COLLATERAL;
}



public BigDecimal getR54_TOTAL_VALUE_NPL() {
	return R54_TOTAL_VALUE_NPL;
}



public void setR54_TOTAL_VALUE_NPL(BigDecimal r54_TOTAL_VALUE_NPL) {
	R54_TOTAL_VALUE_NPL = r54_TOTAL_VALUE_NPL;
}



public BigDecimal getR54_TOTAL_SPECIFIC_PROV() {
	return R54_TOTAL_SPECIFIC_PROV;
}



public void setR54_TOTAL_SPECIFIC_PROV(BigDecimal r54_TOTAL_SPECIFIC_PROV) {
	R54_TOTAL_SPECIFIC_PROV = r54_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR54_SPECIFIC_PROV_NPL() {
	return R54_SPECIFIC_PROV_NPL;
}



public void setR54_SPECIFIC_PROV_NPL(BigDecimal r54_SPECIFIC_PROV_NPL) {
	R54_SPECIFIC_PROV_NPL = r54_SPECIFIC_PROV_NPL;
}



public BigDecimal getR55_30D_90D_PASTDUE() {
	return R55_30D_90D_PASTDUE;
}



public void setR55_30D_90D_PASTDUE(BigDecimal r55_30d_90d_PASTDUE) {
	R55_30D_90D_PASTDUE = r55_30d_90d_PASTDUE;
}



public BigDecimal getR55_NON_PERFORM_LOANS() {
	return R55_NON_PERFORM_LOANS;
}



public void setR55_NON_PERFORM_LOANS(BigDecimal r55_NON_PERFORM_LOANS) {
	R55_NON_PERFORM_LOANS = r55_NON_PERFORM_LOANS;
}



public BigDecimal getR55_NON_ACCRUALS1() {
	return R55_NON_ACCRUALS1;
}



public void setR55_NON_ACCRUALS1(BigDecimal r55_NON_ACCRUALS1) {
	R55_NON_ACCRUALS1 = r55_NON_ACCRUALS1;
}



public BigDecimal getR55_SPECIFIC_PROV1() {
	return R55_SPECIFIC_PROV1;
}



public void setR55_SPECIFIC_PROV1(BigDecimal r55_SPECIFIC_PROV1) {
	R55_SPECIFIC_PROV1 = r55_SPECIFIC_PROV1;
}



public BigDecimal getR55_NO_OF_ACC1() {
	return R55_NO_OF_ACC1;
}



public void setR55_NO_OF_ACC1(BigDecimal r55_NO_OF_ACC1) {
	R55_NO_OF_ACC1 = r55_NO_OF_ACC1;
}



public BigDecimal getR55_90D_180D_PASTDUE() {
	return R55_90D_180D_PASTDUE;
}



public void setR55_90D_180D_PASTDUE(BigDecimal r55_90d_180d_PASTDUE) {
	R55_90D_180D_PASTDUE = r55_90d_180d_PASTDUE;
}



public BigDecimal getR55_NON_ACCRUALS2() {
	return R55_NON_ACCRUALS2;
}



public void setR55_NON_ACCRUALS2(BigDecimal r55_NON_ACCRUALS2) {
	R55_NON_ACCRUALS2 = r55_NON_ACCRUALS2;
}



public BigDecimal getR55_SPECIFIC_PROV2() {
	return R55_SPECIFIC_PROV2;
}



public void setR55_SPECIFIC_PROV2(BigDecimal r55_SPECIFIC_PROV2) {
	R55_SPECIFIC_PROV2 = r55_SPECIFIC_PROV2;
}



public BigDecimal getR55_NO_OF_ACC2() {
	return R55_NO_OF_ACC2;
}



public void setR55_NO_OF_ACC2(BigDecimal r55_NO_OF_ACC2) {
	R55_NO_OF_ACC2 = r55_NO_OF_ACC2;
}



public BigDecimal getR55_TOTAL_NON_ACCRUAL() {
	return R55_TOTAL_NON_ACCRUAL;
}



public void setR55_TOTAL_NON_ACCRUAL(BigDecimal r55_TOTAL_NON_ACCRUAL) {
	R55_TOTAL_NON_ACCRUAL = r55_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR55_TOTAL_DUE_LOANS() {
	return R55_TOTAL_DUE_LOANS;
}



public void setR55_TOTAL_DUE_LOANS(BigDecimal r55_TOTAL_DUE_LOANS) {
	R55_TOTAL_DUE_LOANS = r55_TOTAL_DUE_LOANS;
}



public BigDecimal getR55_TOTAL_PERFORMING_LOAN() {
	return R55_TOTAL_PERFORMING_LOAN;
}



public void setR55_TOTAL_PERFORMING_LOAN(BigDecimal r55_TOTAL_PERFORMING_LOAN) {
	R55_TOTAL_PERFORMING_LOAN = r55_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR55_VALUE_OF_COLLATERAL() {
	return R55_VALUE_OF_COLLATERAL;
}



public void setR55_VALUE_OF_COLLATERAL(BigDecimal r55_VALUE_OF_COLLATERAL) {
	R55_VALUE_OF_COLLATERAL = r55_VALUE_OF_COLLATERAL;
}



public BigDecimal getR55_TOTAL_VALUE_NPL() {
	return R55_TOTAL_VALUE_NPL;
}



public void setR55_TOTAL_VALUE_NPL(BigDecimal r55_TOTAL_VALUE_NPL) {
	R55_TOTAL_VALUE_NPL = r55_TOTAL_VALUE_NPL;
}



public BigDecimal getR55_TOTAL_SPECIFIC_PROV() {
	return R55_TOTAL_SPECIFIC_PROV;
}



public void setR55_TOTAL_SPECIFIC_PROV(BigDecimal r55_TOTAL_SPECIFIC_PROV) {
	R55_TOTAL_SPECIFIC_PROV = r55_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR55_SPECIFIC_PROV_NPL() {
	return R55_SPECIFIC_PROV_NPL;
}



public void setR55_SPECIFIC_PROV_NPL(BigDecimal r55_SPECIFIC_PROV_NPL) {
	R55_SPECIFIC_PROV_NPL = r55_SPECIFIC_PROV_NPL;
}



public BigDecimal getR56_30D_90D_PASTDUE() {
	return R56_30D_90D_PASTDUE;
}



public void setR56_30D_90D_PASTDUE(BigDecimal r56_30d_90d_PASTDUE) {
	R56_30D_90D_PASTDUE = r56_30d_90d_PASTDUE;
}



public BigDecimal getR56_NON_PERFORM_LOANS() {
	return R56_NON_PERFORM_LOANS;
}



public void setR56_NON_PERFORM_LOANS(BigDecimal r56_NON_PERFORM_LOANS) {
	R56_NON_PERFORM_LOANS = r56_NON_PERFORM_LOANS;
}



public BigDecimal getR56_NON_ACCRUALS1() {
	return R56_NON_ACCRUALS1;
}



public void setR56_NON_ACCRUALS1(BigDecimal r56_NON_ACCRUALS1) {
	R56_NON_ACCRUALS1 = r56_NON_ACCRUALS1;
}



public BigDecimal getR56_SPECIFIC_PROV1() {
	return R56_SPECIFIC_PROV1;
}



public void setR56_SPECIFIC_PROV1(BigDecimal r56_SPECIFIC_PROV1) {
	R56_SPECIFIC_PROV1 = r56_SPECIFIC_PROV1;
}



public BigDecimal getR56_NO_OF_ACC1() {
	return R56_NO_OF_ACC1;
}



public void setR56_NO_OF_ACC1(BigDecimal r56_NO_OF_ACC1) {
	R56_NO_OF_ACC1 = r56_NO_OF_ACC1;
}



public BigDecimal getR56_90D_180D_PASTDUE() {
	return R56_90D_180D_PASTDUE;
}



public void setR56_90D_180D_PASTDUE(BigDecimal r56_90d_180d_PASTDUE) {
	R56_90D_180D_PASTDUE = r56_90d_180d_PASTDUE;
}



public BigDecimal getR56_NON_ACCRUALS2() {
	return R56_NON_ACCRUALS2;
}



public void setR56_NON_ACCRUALS2(BigDecimal r56_NON_ACCRUALS2) {
	R56_NON_ACCRUALS2 = r56_NON_ACCRUALS2;
}



public BigDecimal getR56_SPECIFIC_PROV2() {
	return R56_SPECIFIC_PROV2;
}



public void setR56_SPECIFIC_PROV2(BigDecimal r56_SPECIFIC_PROV2) {
	R56_SPECIFIC_PROV2 = r56_SPECIFIC_PROV2;
}



public BigDecimal getR56_NO_OF_ACC2() {
	return R56_NO_OF_ACC2;
}



public void setR56_NO_OF_ACC2(BigDecimal r56_NO_OF_ACC2) {
	R56_NO_OF_ACC2 = r56_NO_OF_ACC2;
}



public BigDecimal getR56_TOTAL_NON_ACCRUAL() {
	return R56_TOTAL_NON_ACCRUAL;
}



public void setR56_TOTAL_NON_ACCRUAL(BigDecimal r56_TOTAL_NON_ACCRUAL) {
	R56_TOTAL_NON_ACCRUAL = r56_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR56_TOTAL_DUE_LOANS() {
	return R56_TOTAL_DUE_LOANS;
}



public void setR56_TOTAL_DUE_LOANS(BigDecimal r56_TOTAL_DUE_LOANS) {
	R56_TOTAL_DUE_LOANS = r56_TOTAL_DUE_LOANS;
}



public BigDecimal getR56_TOTAL_PERFORMING_LOAN() {
	return R56_TOTAL_PERFORMING_LOAN;
}



public void setR56_TOTAL_PERFORMING_LOAN(BigDecimal r56_TOTAL_PERFORMING_LOAN) {
	R56_TOTAL_PERFORMING_LOAN = r56_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR56_VALUE_OF_COLLATERAL() {
	return R56_VALUE_OF_COLLATERAL;
}



public void setR56_VALUE_OF_COLLATERAL(BigDecimal r56_VALUE_OF_COLLATERAL) {
	R56_VALUE_OF_COLLATERAL = r56_VALUE_OF_COLLATERAL;
}



public BigDecimal getR56_TOTAL_VALUE_NPL() {
	return R56_TOTAL_VALUE_NPL;
}



public void setR56_TOTAL_VALUE_NPL(BigDecimal r56_TOTAL_VALUE_NPL) {
	R56_TOTAL_VALUE_NPL = r56_TOTAL_VALUE_NPL;
}



public BigDecimal getR56_TOTAL_SPECIFIC_PROV() {
	return R56_TOTAL_SPECIFIC_PROV;
}



public void setR56_TOTAL_SPECIFIC_PROV(BigDecimal r56_TOTAL_SPECIFIC_PROV) {
	R56_TOTAL_SPECIFIC_PROV = r56_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR56_SPECIFIC_PROV_NPL() {
	return R56_SPECIFIC_PROV_NPL;
}



public void setR56_SPECIFIC_PROV_NPL(BigDecimal r56_SPECIFIC_PROV_NPL) {
	R56_SPECIFIC_PROV_NPL = r56_SPECIFIC_PROV_NPL;
}



public BigDecimal getR57_30D_90D_PASTDUE() {
	return R57_30D_90D_PASTDUE;
}



public void setR57_30D_90D_PASTDUE(BigDecimal r57_30d_90d_PASTDUE) {
	R57_30D_90D_PASTDUE = r57_30d_90d_PASTDUE;
}



public BigDecimal getR57_NON_PERFORM_LOANS() {
	return R57_NON_PERFORM_LOANS;
}



public void setR57_NON_PERFORM_LOANS(BigDecimal r57_NON_PERFORM_LOANS) {
	R57_NON_PERFORM_LOANS = r57_NON_PERFORM_LOANS;
}



public BigDecimal getR57_NON_ACCRUALS1() {
	return R57_NON_ACCRUALS1;
}



public void setR57_NON_ACCRUALS1(BigDecimal r57_NON_ACCRUALS1) {
	R57_NON_ACCRUALS1 = r57_NON_ACCRUALS1;
}



public BigDecimal getR57_SPECIFIC_PROV1() {
	return R57_SPECIFIC_PROV1;
}



public void setR57_SPECIFIC_PROV1(BigDecimal r57_SPECIFIC_PROV1) {
	R57_SPECIFIC_PROV1 = r57_SPECIFIC_PROV1;
}



public BigDecimal getR57_NO_OF_ACC1() {
	return R57_NO_OF_ACC1;
}



public void setR57_NO_OF_ACC1(BigDecimal r57_NO_OF_ACC1) {
	R57_NO_OF_ACC1 = r57_NO_OF_ACC1;
}



public BigDecimal getR57_90D_180D_PASTDUE() {
	return R57_90D_180D_PASTDUE;
}



public void setR57_90D_180D_PASTDUE(BigDecimal r57_90d_180d_PASTDUE) {
	R57_90D_180D_PASTDUE = r57_90d_180d_PASTDUE;
}



public BigDecimal getR57_NON_ACCRUALS2() {
	return R57_NON_ACCRUALS2;
}



public void setR57_NON_ACCRUALS2(BigDecimal r57_NON_ACCRUALS2) {
	R57_NON_ACCRUALS2 = r57_NON_ACCRUALS2;
}



public BigDecimal getR57_SPECIFIC_PROV2() {
	return R57_SPECIFIC_PROV2;
}



public void setR57_SPECIFIC_PROV2(BigDecimal r57_SPECIFIC_PROV2) {
	R57_SPECIFIC_PROV2 = r57_SPECIFIC_PROV2;
}



public BigDecimal getR57_NO_OF_ACC2() {
	return R57_NO_OF_ACC2;
}



public void setR57_NO_OF_ACC2(BigDecimal r57_NO_OF_ACC2) {
	R57_NO_OF_ACC2 = r57_NO_OF_ACC2;
}



public BigDecimal getR57_TOTAL_NON_ACCRUAL() {
	return R57_TOTAL_NON_ACCRUAL;
}



public void setR57_TOTAL_NON_ACCRUAL(BigDecimal r57_TOTAL_NON_ACCRUAL) {
	R57_TOTAL_NON_ACCRUAL = r57_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR57_TOTAL_DUE_LOANS() {
	return R57_TOTAL_DUE_LOANS;
}



public void setR57_TOTAL_DUE_LOANS(BigDecimal r57_TOTAL_DUE_LOANS) {
	R57_TOTAL_DUE_LOANS = r57_TOTAL_DUE_LOANS;
}



public BigDecimal getR57_TOTAL_PERFORMING_LOAN() {
	return R57_TOTAL_PERFORMING_LOAN;
}



public void setR57_TOTAL_PERFORMING_LOAN(BigDecimal r57_TOTAL_PERFORMING_LOAN) {
	R57_TOTAL_PERFORMING_LOAN = r57_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR57_VALUE_OF_COLLATERAL() {
	return R57_VALUE_OF_COLLATERAL;
}



public void setR57_VALUE_OF_COLLATERAL(BigDecimal r57_VALUE_OF_COLLATERAL) {
	R57_VALUE_OF_COLLATERAL = r57_VALUE_OF_COLLATERAL;
}



public BigDecimal getR57_TOTAL_VALUE_NPL() {
	return R57_TOTAL_VALUE_NPL;
}



public void setR57_TOTAL_VALUE_NPL(BigDecimal r57_TOTAL_VALUE_NPL) {
	R57_TOTAL_VALUE_NPL = r57_TOTAL_VALUE_NPL;
}



public BigDecimal getR57_TOTAL_SPECIFIC_PROV() {
	return R57_TOTAL_SPECIFIC_PROV;
}



public void setR57_TOTAL_SPECIFIC_PROV(BigDecimal r57_TOTAL_SPECIFIC_PROV) {
	R57_TOTAL_SPECIFIC_PROV = r57_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR57_SPECIFIC_PROV_NPL() {
	return R57_SPECIFIC_PROV_NPL;
}



public void setR57_SPECIFIC_PROV_NPL(BigDecimal r57_SPECIFIC_PROV_NPL) {
	R57_SPECIFIC_PROV_NPL = r57_SPECIFIC_PROV_NPL;
}



public BigDecimal getR58_30D_90D_PASTDUE() {
	return R58_30D_90D_PASTDUE;
}



public void setR58_30D_90D_PASTDUE(BigDecimal r58_30d_90d_PASTDUE) {
	R58_30D_90D_PASTDUE = r58_30d_90d_PASTDUE;
}



public BigDecimal getR58_NON_PERFORM_LOANS() {
	return R58_NON_PERFORM_LOANS;
}



public void setR58_NON_PERFORM_LOANS(BigDecimal r58_NON_PERFORM_LOANS) {
	R58_NON_PERFORM_LOANS = r58_NON_PERFORM_LOANS;
}



public BigDecimal getR58_NON_ACCRUALS1() {
	return R58_NON_ACCRUALS1;
}



public void setR58_NON_ACCRUALS1(BigDecimal r58_NON_ACCRUALS1) {
	R58_NON_ACCRUALS1 = r58_NON_ACCRUALS1;
}



public BigDecimal getR58_SPECIFIC_PROV1() {
	return R58_SPECIFIC_PROV1;
}



public void setR58_SPECIFIC_PROV1(BigDecimal r58_SPECIFIC_PROV1) {
	R58_SPECIFIC_PROV1 = r58_SPECIFIC_PROV1;
}



public BigDecimal getR58_NO_OF_ACC1() {
	return R58_NO_OF_ACC1;
}



public void setR58_NO_OF_ACC1(BigDecimal r58_NO_OF_ACC1) {
	R58_NO_OF_ACC1 = r58_NO_OF_ACC1;
}



public BigDecimal getR58_90D_180D_PASTDUE() {
	return R58_90D_180D_PASTDUE;
}



public void setR58_90D_180D_PASTDUE(BigDecimal r58_90d_180d_PASTDUE) {
	R58_90D_180D_PASTDUE = r58_90d_180d_PASTDUE;
}



public BigDecimal getR58_NON_ACCRUALS2() {
	return R58_NON_ACCRUALS2;
}



public void setR58_NON_ACCRUALS2(BigDecimal r58_NON_ACCRUALS2) {
	R58_NON_ACCRUALS2 = r58_NON_ACCRUALS2;
}



public BigDecimal getR58_SPECIFIC_PROV2() {
	return R58_SPECIFIC_PROV2;
}



public void setR58_SPECIFIC_PROV2(BigDecimal r58_SPECIFIC_PROV2) {
	R58_SPECIFIC_PROV2 = r58_SPECIFIC_PROV2;
}



public BigDecimal getR58_NO_OF_ACC2() {
	return R58_NO_OF_ACC2;
}



public void setR58_NO_OF_ACC2(BigDecimal r58_NO_OF_ACC2) {
	R58_NO_OF_ACC2 = r58_NO_OF_ACC2;
}



public BigDecimal getR58_TOTAL_NON_ACCRUAL() {
	return R58_TOTAL_NON_ACCRUAL;
}



public void setR58_TOTAL_NON_ACCRUAL(BigDecimal r58_TOTAL_NON_ACCRUAL) {
	R58_TOTAL_NON_ACCRUAL = r58_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR58_TOTAL_DUE_LOANS() {
	return R58_TOTAL_DUE_LOANS;
}



public void setR58_TOTAL_DUE_LOANS(BigDecimal r58_TOTAL_DUE_LOANS) {
	R58_TOTAL_DUE_LOANS = r58_TOTAL_DUE_LOANS;
}



public BigDecimal getR58_TOTAL_PERFORMING_LOAN() {
	return R58_TOTAL_PERFORMING_LOAN;
}



public void setR58_TOTAL_PERFORMING_LOAN(BigDecimal r58_TOTAL_PERFORMING_LOAN) {
	R58_TOTAL_PERFORMING_LOAN = r58_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR58_VALUE_OF_COLLATERAL() {
	return R58_VALUE_OF_COLLATERAL;
}



public void setR58_VALUE_OF_COLLATERAL(BigDecimal r58_VALUE_OF_COLLATERAL) {
	R58_VALUE_OF_COLLATERAL = r58_VALUE_OF_COLLATERAL;
}



public BigDecimal getR58_TOTAL_VALUE_NPL() {
	return R58_TOTAL_VALUE_NPL;
}



public void setR58_TOTAL_VALUE_NPL(BigDecimal r58_TOTAL_VALUE_NPL) {
	R58_TOTAL_VALUE_NPL = r58_TOTAL_VALUE_NPL;
}



public BigDecimal getR58_TOTAL_SPECIFIC_PROV() {
	return R58_TOTAL_SPECIFIC_PROV;
}



public void setR58_TOTAL_SPECIFIC_PROV(BigDecimal r58_TOTAL_SPECIFIC_PROV) {
	R58_TOTAL_SPECIFIC_PROV = r58_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR58_SPECIFIC_PROV_NPL() {
	return R58_SPECIFIC_PROV_NPL;
}



public void setR58_SPECIFIC_PROV_NPL(BigDecimal r58_SPECIFIC_PROV_NPL) {
	R58_SPECIFIC_PROV_NPL = r58_SPECIFIC_PROV_NPL;
}



public BigDecimal getR59_30D_90D_PASTDUE() {
	return R59_30D_90D_PASTDUE;
}



public void setR59_30D_90D_PASTDUE(BigDecimal r59_30d_90d_PASTDUE) {
	R59_30D_90D_PASTDUE = r59_30d_90d_PASTDUE;
}



public BigDecimal getR59_NON_PERFORM_LOANS() {
	return R59_NON_PERFORM_LOANS;
}



public void setR59_NON_PERFORM_LOANS(BigDecimal r59_NON_PERFORM_LOANS) {
	R59_NON_PERFORM_LOANS = r59_NON_PERFORM_LOANS;
}



public BigDecimal getR59_NON_ACCRUALS1() {
	return R59_NON_ACCRUALS1;
}



public void setR59_NON_ACCRUALS1(BigDecimal r59_NON_ACCRUALS1) {
	R59_NON_ACCRUALS1 = r59_NON_ACCRUALS1;
}



public BigDecimal getR59_SPECIFIC_PROV1() {
	return R59_SPECIFIC_PROV1;
}



public void setR59_SPECIFIC_PROV1(BigDecimal r59_SPECIFIC_PROV1) {
	R59_SPECIFIC_PROV1 = r59_SPECIFIC_PROV1;
}



public BigDecimal getR59_NO_OF_ACC1() {
	return R59_NO_OF_ACC1;
}



public void setR59_NO_OF_ACC1(BigDecimal r59_NO_OF_ACC1) {
	R59_NO_OF_ACC1 = r59_NO_OF_ACC1;
}



public BigDecimal getR59_90D_180D_PASTDUE() {
	return R59_90D_180D_PASTDUE;
}



public void setR59_90D_180D_PASTDUE(BigDecimal r59_90d_180d_PASTDUE) {
	R59_90D_180D_PASTDUE = r59_90d_180d_PASTDUE;
}



public BigDecimal getR59_NON_ACCRUALS2() {
	return R59_NON_ACCRUALS2;
}



public void setR59_NON_ACCRUALS2(BigDecimal r59_NON_ACCRUALS2) {
	R59_NON_ACCRUALS2 = r59_NON_ACCRUALS2;
}



public BigDecimal getR59_SPECIFIC_PROV2() {
	return R59_SPECIFIC_PROV2;
}



public void setR59_SPECIFIC_PROV2(BigDecimal r59_SPECIFIC_PROV2) {
	R59_SPECIFIC_PROV2 = r59_SPECIFIC_PROV2;
}



public BigDecimal getR59_NO_OF_ACC2() {
	return R59_NO_OF_ACC2;
}



public void setR59_NO_OF_ACC2(BigDecimal r59_NO_OF_ACC2) {
	R59_NO_OF_ACC2 = r59_NO_OF_ACC2;
}



public BigDecimal getR59_TOTAL_NON_ACCRUAL() {
	return R59_TOTAL_NON_ACCRUAL;
}



public void setR59_TOTAL_NON_ACCRUAL(BigDecimal r59_TOTAL_NON_ACCRUAL) {
	R59_TOTAL_NON_ACCRUAL = r59_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR59_TOTAL_DUE_LOANS() {
	return R59_TOTAL_DUE_LOANS;
}



public void setR59_TOTAL_DUE_LOANS(BigDecimal r59_TOTAL_DUE_LOANS) {
	R59_TOTAL_DUE_LOANS = r59_TOTAL_DUE_LOANS;
}



public BigDecimal getR59_TOTAL_PERFORMING_LOAN() {
	return R59_TOTAL_PERFORMING_LOAN;
}



public void setR59_TOTAL_PERFORMING_LOAN(BigDecimal r59_TOTAL_PERFORMING_LOAN) {
	R59_TOTAL_PERFORMING_LOAN = r59_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR59_VALUE_OF_COLLATERAL() {
	return R59_VALUE_OF_COLLATERAL;
}



public void setR59_VALUE_OF_COLLATERAL(BigDecimal r59_VALUE_OF_COLLATERAL) {
	R59_VALUE_OF_COLLATERAL = r59_VALUE_OF_COLLATERAL;
}



public BigDecimal getR59_TOTAL_VALUE_NPL() {
	return R59_TOTAL_VALUE_NPL;
}



public void setR59_TOTAL_VALUE_NPL(BigDecimal r59_TOTAL_VALUE_NPL) {
	R59_TOTAL_VALUE_NPL = r59_TOTAL_VALUE_NPL;
}



public BigDecimal getR59_TOTAL_SPECIFIC_PROV() {
	return R59_TOTAL_SPECIFIC_PROV;
}



public void setR59_TOTAL_SPECIFIC_PROV(BigDecimal r59_TOTAL_SPECIFIC_PROV) {
	R59_TOTAL_SPECIFIC_PROV = r59_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR59_SPECIFIC_PROV_NPL() {
	return R59_SPECIFIC_PROV_NPL;
}



public void setR59_SPECIFIC_PROV_NPL(BigDecimal r59_SPECIFIC_PROV_NPL) {
	R59_SPECIFIC_PROV_NPL = r59_SPECIFIC_PROV_NPL;
}



public BigDecimal getR60_30D_90D_PASTDUE() {
	return R60_30D_90D_PASTDUE;
}



public void setR60_30D_90D_PASTDUE(BigDecimal r60_30d_90d_PASTDUE) {
	R60_30D_90D_PASTDUE = r60_30d_90d_PASTDUE;
}



public BigDecimal getR60_NON_PERFORM_LOANS() {
	return R60_NON_PERFORM_LOANS;
}



public void setR60_NON_PERFORM_LOANS(BigDecimal r60_NON_PERFORM_LOANS) {
	R60_NON_PERFORM_LOANS = r60_NON_PERFORM_LOANS;
}



public BigDecimal getR60_NON_ACCRUALS1() {
	return R60_NON_ACCRUALS1;
}



public void setR60_NON_ACCRUALS1(BigDecimal r60_NON_ACCRUALS1) {
	R60_NON_ACCRUALS1 = r60_NON_ACCRUALS1;
}



public BigDecimal getR60_SPECIFIC_PROV1() {
	return R60_SPECIFIC_PROV1;
}



public void setR60_SPECIFIC_PROV1(BigDecimal r60_SPECIFIC_PROV1) {
	R60_SPECIFIC_PROV1 = r60_SPECIFIC_PROV1;
}



public BigDecimal getR60_NO_OF_ACC1() {
	return R60_NO_OF_ACC1;
}



public void setR60_NO_OF_ACC1(BigDecimal r60_NO_OF_ACC1) {
	R60_NO_OF_ACC1 = r60_NO_OF_ACC1;
}



public BigDecimal getR60_90D_180D_PASTDUE() {
	return R60_90D_180D_PASTDUE;
}



public void setR60_90D_180D_PASTDUE(BigDecimal r60_90d_180d_PASTDUE) {
	R60_90D_180D_PASTDUE = r60_90d_180d_PASTDUE;
}



public BigDecimal getR60_NON_ACCRUALS2() {
	return R60_NON_ACCRUALS2;
}



public void setR60_NON_ACCRUALS2(BigDecimal r60_NON_ACCRUALS2) {
	R60_NON_ACCRUALS2 = r60_NON_ACCRUALS2;
}



public BigDecimal getR60_SPECIFIC_PROV2() {
	return R60_SPECIFIC_PROV2;
}



public void setR60_SPECIFIC_PROV2(BigDecimal r60_SPECIFIC_PROV2) {
	R60_SPECIFIC_PROV2 = r60_SPECIFIC_PROV2;
}



public BigDecimal getR60_NO_OF_ACC2() {
	return R60_NO_OF_ACC2;
}



public void setR60_NO_OF_ACC2(BigDecimal r60_NO_OF_ACC2) {
	R60_NO_OF_ACC2 = r60_NO_OF_ACC2;
}



public BigDecimal getR60_TOTAL_NON_ACCRUAL() {
	return R60_TOTAL_NON_ACCRUAL;
}



public void setR60_TOTAL_NON_ACCRUAL(BigDecimal r60_TOTAL_NON_ACCRUAL) {
	R60_TOTAL_NON_ACCRUAL = r60_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR60_TOTAL_DUE_LOANS() {
	return R60_TOTAL_DUE_LOANS;
}



public void setR60_TOTAL_DUE_LOANS(BigDecimal r60_TOTAL_DUE_LOANS) {
	R60_TOTAL_DUE_LOANS = r60_TOTAL_DUE_LOANS;
}



public BigDecimal getR60_TOTAL_PERFORMING_LOAN() {
	return R60_TOTAL_PERFORMING_LOAN;
}



public void setR60_TOTAL_PERFORMING_LOAN(BigDecimal r60_TOTAL_PERFORMING_LOAN) {
	R60_TOTAL_PERFORMING_LOAN = r60_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR60_VALUE_OF_COLLATERAL() {
	return R60_VALUE_OF_COLLATERAL;
}



public void setR60_VALUE_OF_COLLATERAL(BigDecimal r60_VALUE_OF_COLLATERAL) {
	R60_VALUE_OF_COLLATERAL = r60_VALUE_OF_COLLATERAL;
}



public BigDecimal getR60_TOTAL_VALUE_NPL() {
	return R60_TOTAL_VALUE_NPL;
}



public void setR60_TOTAL_VALUE_NPL(BigDecimal r60_TOTAL_VALUE_NPL) {
	R60_TOTAL_VALUE_NPL = r60_TOTAL_VALUE_NPL;
}



public BigDecimal getR60_TOTAL_SPECIFIC_PROV() {
	return R60_TOTAL_SPECIFIC_PROV;
}



public void setR60_TOTAL_SPECIFIC_PROV(BigDecimal r60_TOTAL_SPECIFIC_PROV) {
	R60_TOTAL_SPECIFIC_PROV = r60_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR60_SPECIFIC_PROV_NPL() {
	return R60_SPECIFIC_PROV_NPL;
}



public void setR60_SPECIFIC_PROV_NPL(BigDecimal r60_SPECIFIC_PROV_NPL) {
	R60_SPECIFIC_PROV_NPL = r60_SPECIFIC_PROV_NPL;
}



public BigDecimal getR61_30D_90D_PASTDUE() {
	return R61_30D_90D_PASTDUE;
}



public void setR61_30D_90D_PASTDUE(BigDecimal r61_30d_90d_PASTDUE) {
	R61_30D_90D_PASTDUE = r61_30d_90d_PASTDUE;
}



public BigDecimal getR61_NON_PERFORM_LOANS() {
	return R61_NON_PERFORM_LOANS;
}



public void setR61_NON_PERFORM_LOANS(BigDecimal r61_NON_PERFORM_LOANS) {
	R61_NON_PERFORM_LOANS = r61_NON_PERFORM_LOANS;
}



public BigDecimal getR61_NON_ACCRUALS1() {
	return R61_NON_ACCRUALS1;
}



public void setR61_NON_ACCRUALS1(BigDecimal r61_NON_ACCRUALS1) {
	R61_NON_ACCRUALS1 = r61_NON_ACCRUALS1;
}



public BigDecimal getR61_SPECIFIC_PROV1() {
	return R61_SPECIFIC_PROV1;
}



public void setR61_SPECIFIC_PROV1(BigDecimal r61_SPECIFIC_PROV1) {
	R61_SPECIFIC_PROV1 = r61_SPECIFIC_PROV1;
}



public BigDecimal getR61_NO_OF_ACC1() {
	return R61_NO_OF_ACC1;
}



public void setR61_NO_OF_ACC1(BigDecimal r61_NO_OF_ACC1) {
	R61_NO_OF_ACC1 = r61_NO_OF_ACC1;
}



public BigDecimal getR61_90D_180D_PASTDUE() {
	return R61_90D_180D_PASTDUE;
}



public void setR61_90D_180D_PASTDUE(BigDecimal r61_90d_180d_PASTDUE) {
	R61_90D_180D_PASTDUE = r61_90d_180d_PASTDUE;
}



public BigDecimal getR61_NON_ACCRUALS2() {
	return R61_NON_ACCRUALS2;
}



public void setR61_NON_ACCRUALS2(BigDecimal r61_NON_ACCRUALS2) {
	R61_NON_ACCRUALS2 = r61_NON_ACCRUALS2;
}



public BigDecimal getR61_SPECIFIC_PROV2() {
	return R61_SPECIFIC_PROV2;
}



public void setR61_SPECIFIC_PROV2(BigDecimal r61_SPECIFIC_PROV2) {
	R61_SPECIFIC_PROV2 = r61_SPECIFIC_PROV2;
}



public BigDecimal getR61_NO_OF_ACC2() {
	return R61_NO_OF_ACC2;
}



public void setR61_NO_OF_ACC2(BigDecimal r61_NO_OF_ACC2) {
	R61_NO_OF_ACC2 = r61_NO_OF_ACC2;
}



public BigDecimal getR61_180D_ABOVE_PASTDUE() {
	return R61_180D_ABOVE_PASTDUE;
}



public void setR61_180D_ABOVE_PASTDUE(BigDecimal r61_180d_ABOVE_PASTDUE) {
	R61_180D_ABOVE_PASTDUE = r61_180d_ABOVE_PASTDUE;
}



public BigDecimal getR61_NON_ACCRUALS3() {
	return R61_NON_ACCRUALS3;
}



public void setR61_NON_ACCRUALS3(BigDecimal r61_NON_ACCRUALS3) {
	R61_NON_ACCRUALS3 = r61_NON_ACCRUALS3;
}



public BigDecimal getR61_SPECIFIC_PROV3() {
	return R61_SPECIFIC_PROV3;
}



public void setR61_SPECIFIC_PROV3(BigDecimal r61_SPECIFIC_PROV3) {
	R61_SPECIFIC_PROV3 = r61_SPECIFIC_PROV3;
}



public BigDecimal getR61_NO_OF_ACC3() {
	return R61_NO_OF_ACC3;
}



public void setR61_NO_OF_ACC3(BigDecimal r61_NO_OF_ACC3) {
	R61_NO_OF_ACC3 = r61_NO_OF_ACC3;
}



public BigDecimal getR61_TOTAL_NON_ACCRUAL() {
	return R61_TOTAL_NON_ACCRUAL;
}



public void setR61_TOTAL_NON_ACCRUAL(BigDecimal r61_TOTAL_NON_ACCRUAL) {
	R61_TOTAL_NON_ACCRUAL = r61_TOTAL_NON_ACCRUAL;
}



public BigDecimal getR61_TOTAL_DUE_LOANS() {
	return R61_TOTAL_DUE_LOANS;
}



public void setR61_TOTAL_DUE_LOANS(BigDecimal r61_TOTAL_DUE_LOANS) {
	R61_TOTAL_DUE_LOANS = r61_TOTAL_DUE_LOANS;
}



public BigDecimal getR61_TOTAL_PERFORMING_LOAN() {
	return R61_TOTAL_PERFORMING_LOAN;
}



public void setR61_TOTAL_PERFORMING_LOAN(BigDecimal r61_TOTAL_PERFORMING_LOAN) {
	R61_TOTAL_PERFORMING_LOAN = r61_TOTAL_PERFORMING_LOAN;
}



public BigDecimal getR61_VALUE_OF_COLLATERAL() {
	return R61_VALUE_OF_COLLATERAL;
}



public void setR61_VALUE_OF_COLLATERAL(BigDecimal r61_VALUE_OF_COLLATERAL) {
	R61_VALUE_OF_COLLATERAL = r61_VALUE_OF_COLLATERAL;
}



public BigDecimal getR61_TOTAL_VALUE_NPL() {
	return R61_TOTAL_VALUE_NPL;
}



public void setR61_TOTAL_VALUE_NPL(BigDecimal r61_TOTAL_VALUE_NPL) {
	R61_TOTAL_VALUE_NPL = r61_TOTAL_VALUE_NPL;
}



public BigDecimal getR61_TOTAL_SPECIFIC_PROV() {
	return R61_TOTAL_SPECIFIC_PROV;
}



public void setR61_TOTAL_SPECIFIC_PROV(BigDecimal r61_TOTAL_SPECIFIC_PROV) {
	R61_TOTAL_SPECIFIC_PROV = r61_TOTAL_SPECIFIC_PROV;
}



public BigDecimal getR61_SPECIFIC_PROV_NPL() {
	return R61_SPECIFIC_PROV_NPL;
}



public void setR61_SPECIFIC_PROV_NPL(BigDecimal r61_SPECIFIC_PROV_NPL) {
	R61_SPECIFIC_PROV_NPL = r61_SPECIFIC_PROV_NPL;
}



public M_PD_Manual_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}




}