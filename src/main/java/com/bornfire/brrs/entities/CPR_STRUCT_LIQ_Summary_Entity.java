package com.bornfire.brrs.entities;
import java.math.BigDecimal;
import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_CPR_STRUCT_LIQ_SUMMARYTABLE")
public class CPR_STRUCT_LIQ_Summary_Entity {


@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id

private Date report_date;
private String report_version;
private String report_frequency;
private String report_code;
private String report_desc;
private String entity_flg;
private String modify_flg;
private String del_flg;


private BigDecimal R8_1_DAY;
private BigDecimal R8_2TO7_DAYS;
private BigDecimal R8_8TO14_DAYS;
private BigDecimal R8_15TO30_DAYS;
private BigDecimal R8_31DAYS_UPTO_2MONTHS;
private BigDecimal R8_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R8_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R8_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R8_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R8_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R8_OVER_5YEARS;
private BigDecimal R8_TOTAL;
private BigDecimal R9_1_DAY;
private BigDecimal R9_2TO7_DAYS;
private BigDecimal R9_8TO14_DAYS;
private BigDecimal R9_15TO30_DAYS;
private BigDecimal R9_31DAYS_UPTO_2MONTHS;
private BigDecimal R9_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R9_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R9_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R9_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R9_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R9_OVER_5YEARS;
private BigDecimal R9_TOTAL;
private BigDecimal R10_1_DAY;
private BigDecimal R10_2TO7_DAYS;
private BigDecimal R10_8TO14_DAYS;
private BigDecimal R10_15TO30_DAYS;
private BigDecimal R10_31DAYS_UPTO_2MONTHS;
private BigDecimal R10_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R10_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R10_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R10_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R10_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R10_OVER_5YEARS;
private BigDecimal R10_TOTAL;
private BigDecimal R11_1_DAY;
private BigDecimal R11_2TO7_DAYS;
private BigDecimal R11_8TO14_DAYS;
private BigDecimal R11_15TO30_DAYS;
private BigDecimal R11_31DAYS_UPTO_2MONTHS;
private BigDecimal R11_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R11_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R11_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R11_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R11_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R11_OVER_5YEARS;
private BigDecimal R11_TOTAL;
private BigDecimal R12_1_DAY;
private BigDecimal R12_2TO7_DAYS;
private BigDecimal R12_8TO14_DAYS;
private BigDecimal R12_15TO30_DAYS;
private BigDecimal R12_31DAYS_UPTO_2MONTHS;
private BigDecimal R12_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R12_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R12_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R12_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R12_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R12_OVER_5YEARS;
private BigDecimal R12_TOTAL;
private BigDecimal R13_1_DAY;
private BigDecimal R13_2TO7_DAYS;
private BigDecimal R13_8TO14_DAYS;
private BigDecimal R13_15TO30_DAYS;
private BigDecimal R13_31DAYS_UPTO_2MONTHS;
private BigDecimal R13_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R13_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R13_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R13_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R13_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R13_OVER_5YEARS;
private BigDecimal R13_TOTAL;
private BigDecimal R14_1_DAY;
private BigDecimal R14_2TO7_DAYS;
private BigDecimal R14_8TO14_DAYS;
private BigDecimal R14_15TO30_DAYS;
private BigDecimal R14_31DAYS_UPTO_2MONTHS;
private BigDecimal R14_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R14_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R14_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R14_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R14_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R14_OVER_5YEARS;
private BigDecimal R14_TOTAL;
private BigDecimal R15_1_DAY;
private BigDecimal R15_2TO7_DAYS;
private BigDecimal R15_8TO14_DAYS;
private BigDecimal R15_15TO30_DAYS;
private BigDecimal R15_31DAYS_UPTO_2MONTHS;
private BigDecimal R15_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R15_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R15_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R15_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R15_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R15_OVER_5YEARS;
private BigDecimal R15_TOTAL;
private BigDecimal R16_1_DAY;
private BigDecimal R16_2TO7_DAYS;
private BigDecimal R16_8TO14_DAYS;
private BigDecimal R16_15TO30_DAYS;
private BigDecimal R16_31DAYS_UPTO_2MONTHS;
private BigDecimal R16_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R16_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R16_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R16_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R16_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R16_OVER_5YEARS;
private BigDecimal R16_TOTAL;
private BigDecimal R17_1_DAY;
private BigDecimal R17_2TO7_DAYS;
private BigDecimal R17_8TO14_DAYS;
private BigDecimal R17_15TO30_DAYS;
private BigDecimal R17_31DAYS_UPTO_2MONTHS;
private BigDecimal R17_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R17_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R17_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R17_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R17_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R17_OVER_5YEARS;
private BigDecimal R17_TOTAL;
private BigDecimal R18_1_DAY;
private BigDecimal R18_2TO7_DAYS;
private BigDecimal R18_8TO14_DAYS;
private BigDecimal R18_15TO30_DAYS;
private BigDecimal R18_31DAYS_UPTO_2MONTHS;
private BigDecimal R18_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R18_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R18_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R18_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R18_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R18_OVER_5YEARS;
private BigDecimal R18_TOTAL;
private BigDecimal R19_1_DAY;
private BigDecimal R19_2TO7_DAYS;
private BigDecimal R19_8TO14_DAYS;
private BigDecimal R19_15TO30_DAYS;
private BigDecimal R19_31DAYS_UPTO_2MONTHS;
private BigDecimal R19_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R19_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R19_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R19_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R19_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R19_OVER_5YEARS;
private BigDecimal R19_TOTAL;
private BigDecimal R20_1_DAY;
private BigDecimal R20_2TO7_DAYS;
private BigDecimal R20_8TO14_DAYS;
private BigDecimal R20_15TO30_DAYS;
private BigDecimal R20_31DAYS_UPTO_2MONTHS;
private BigDecimal R20_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R20_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R20_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R20_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R20_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R20_OVER_5YEARS;
private BigDecimal R20_TOTAL;
private BigDecimal R21_1_DAY;
private BigDecimal R21_2TO7_DAYS;
private BigDecimal R21_8TO14_DAYS;
private BigDecimal R21_15TO30_DAYS;
private BigDecimal R21_31DAYS_UPTO_2MONTHS;
private BigDecimal R21_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R21_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R21_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R21_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R21_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R21_OVER_5YEARS;
private BigDecimal R21_TOTAL;
private BigDecimal R22_1_DAY;
private BigDecimal R22_2TO7_DAYS;
private BigDecimal R22_8TO14_DAYS;
private BigDecimal R22_15TO30_DAYS;
private BigDecimal R22_31DAYS_UPTO_2MONTHS;
private BigDecimal R22_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R22_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R22_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R22_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R22_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R22_OVER_5YEARS;
private BigDecimal R22_TOTAL;
private BigDecimal R23_1_DAY;
private BigDecimal R23_2TO7_DAYS;
private BigDecimal R23_8TO14_DAYS;
private BigDecimal R23_15TO30_DAYS;
private BigDecimal R23_31DAYS_UPTO_2MONTHS;
private BigDecimal R23_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R23_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R23_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R23_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R23_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R23_OVER_5YEARS;
private BigDecimal R23_TOTAL;
private BigDecimal R24_1_DAY;
private BigDecimal R24_2TO7_DAYS;
private BigDecimal R24_8TO14_DAYS;
private BigDecimal R24_15TO30_DAYS;
private BigDecimal R24_31DAYS_UPTO_2MONTHS;
private BigDecimal R24_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R24_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R24_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R24_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R24_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R24_OVER_5YEARS;
private BigDecimal R24_TOTAL;
private BigDecimal R25_1_DAY;
private BigDecimal R25_2TO7_DAYS;
private BigDecimal R25_8TO14_DAYS;
private BigDecimal R25_15TO30_DAYS;
private BigDecimal R25_31DAYS_UPTO_2MONTHS;
private BigDecimal R25_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R25_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R25_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R25_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R25_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R25_OVER_5YEARS;
private BigDecimal R25_TOTAL;
private BigDecimal R26_1_DAY;
private BigDecimal R26_2TO7_DAYS;
private BigDecimal R26_8TO14_DAYS;
private BigDecimal R26_15TO30_DAYS;
private BigDecimal R26_31DAYS_UPTO_2MONTHS;
private BigDecimal R26_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R26_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R26_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R26_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R26_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R26_OVER_5YEARS;
private BigDecimal R26_TOTAL;
private BigDecimal R27_1_DAY;
private BigDecimal R27_2TO7_DAYS;
private BigDecimal R27_8TO14_DAYS;
private BigDecimal R27_15TO30_DAYS;
private BigDecimal R27_31DAYS_UPTO_2MONTHS;
private BigDecimal R27_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R27_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R27_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R27_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R27_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R27_OVER_5YEARS;
private BigDecimal R27_TOTAL;
private BigDecimal R28_1_DAY;
private BigDecimal R28_2TO7_DAYS;
private BigDecimal R28_8TO14_DAYS;
private BigDecimal R28_15TO30_DAYS;
private BigDecimal R28_31DAYS_UPTO_2MONTHS;
private BigDecimal R28_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R28_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R28_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R28_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R28_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R28_OVER_5YEARS;
private BigDecimal R28_TOTAL;
private BigDecimal R29_1_DAY;
private BigDecimal R29_2TO7_DAYS;
private BigDecimal R29_8TO14_DAYS;
private BigDecimal R29_15TO30_DAYS;
private BigDecimal R29_31DAYS_UPTO_2MONTHS;
private BigDecimal R29_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R29_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R29_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R29_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R29_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R29_OVER_5YEARS;
private BigDecimal R29_TOTAL;
private BigDecimal R30_1_DAY;
private BigDecimal R30_2TO7_DAYS;
private BigDecimal R30_8TO14_DAYS;
private BigDecimal R30_15TO30_DAYS;
private BigDecimal R30_31DAYS_UPTO_2MONTHS;
private BigDecimal R30_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R30_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R30_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R30_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R30_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R30_OVER_5YEARS;
private BigDecimal R30_TOTAL;
private BigDecimal R31_1_DAY;
private BigDecimal R31_2TO7_DAYS;
private BigDecimal R31_8TO14_DAYS;
private BigDecimal R31_15TO30_DAYS;
private BigDecimal R31_31DAYS_UPTO_2MONTHS;
private BigDecimal R31_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R31_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R31_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R31_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R31_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R31_OVER_5YEARS;
private BigDecimal R31_TOTAL;
private BigDecimal R32_1_DAY;
private BigDecimal R32_2TO7_DAYS;
private BigDecimal R32_8TO14_DAYS;
private BigDecimal R32_15TO30_DAYS;
private BigDecimal R32_31DAYS_UPTO_2MONTHS;
private BigDecimal R32_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R32_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R32_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R32_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R32_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R32_OVER_5YEARS;
private BigDecimal R32_TOTAL;
private BigDecimal R33_1_DAY;
private BigDecimal R33_2TO7_DAYS;
private BigDecimal R33_8TO14_DAYS;
private BigDecimal R33_15TO30_DAYS;
private BigDecimal R33_31DAYS_UPTO_2MONTHS;
private BigDecimal R33_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R33_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R33_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R33_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R33_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R33_OVER_5YEARS;
private BigDecimal R33_TOTAL;
private BigDecimal R34_1_DAY;
private BigDecimal R34_2TO7_DAYS;
private BigDecimal R34_8TO14_DAYS;
private BigDecimal R34_15TO30_DAYS;
private BigDecimal R34_31DAYS_UPTO_2MONTHS;
private BigDecimal R34_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R34_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R34_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R34_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R34_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R34_OVER_5YEARS;
private BigDecimal R34_TOTAL;
private BigDecimal R35_1_DAY;
private BigDecimal R35_2TO7_DAYS;
private BigDecimal R35_8TO14_DAYS;
private BigDecimal R35_15TO30_DAYS;
private BigDecimal R35_31DAYS_UPTO_2MONTHS;
private BigDecimal R35_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R35_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R35_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R35_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R35_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R35_OVER_5YEARS;
private BigDecimal R35_TOTAL;
private BigDecimal R36_1_DAY;
private BigDecimal R36_2TO7_DAYS;
private BigDecimal R36_8TO14_DAYS;
private BigDecimal R36_15TO30_DAYS;
private BigDecimal R36_31DAYS_UPTO_2MONTHS;
private BigDecimal R36_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R36_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R36_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R36_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R36_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R36_OVER_5YEARS;
private BigDecimal R36_TOTAL;
private BigDecimal R37_1_DAY;
private BigDecimal R37_2TO7_DAYS;
private BigDecimal R37_8TO14_DAYS;
private BigDecimal R37_15TO30_DAYS;
private BigDecimal R37_31DAYS_UPTO_2MONTHS;
private BigDecimal R37_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R37_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R37_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R37_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R37_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R37_OVER_5YEARS;
private BigDecimal R37_TOTAL;
private BigDecimal R38_1_DAY;
private BigDecimal R38_2TO7_DAYS;
private BigDecimal R38_8TO14_DAYS;
private BigDecimal R38_15TO30_DAYS;
private BigDecimal R38_31DAYS_UPTO_2MONTHS;
private BigDecimal R38_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R38_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R38_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R38_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R38_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R38_OVER_5YEARS;
private BigDecimal R38_TOTAL;
private BigDecimal R39_1_DAY;
private BigDecimal R39_2TO7_DAYS;
private BigDecimal R39_8TO14_DAYS;
private BigDecimal R39_15TO30_DAYS;
private BigDecimal R39_31DAYS_UPTO_2MONTHS;
private BigDecimal R39_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R39_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R39_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R39_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R39_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R39_OVER_5YEARS;
private BigDecimal R39_TOTAL;
private BigDecimal R40_1_DAY;
private BigDecimal R40_2TO7_DAYS;
private BigDecimal R40_8TO14_DAYS;
private BigDecimal R40_15TO30_DAYS;
private BigDecimal R40_31DAYS_UPTO_2MONTHS;
private BigDecimal R40_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R40_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R40_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R40_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R40_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R40_OVER_5YEARS;
private BigDecimal R40_TOTAL;
private BigDecimal R41_1_DAY;
private BigDecimal R41_2TO7_DAYS;
private BigDecimal R41_8TO14_DAYS;
private BigDecimal R41_15TO30_DAYS;
private BigDecimal R41_31DAYS_UPTO_2MONTHS;
private BigDecimal R41_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R41_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R41_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R41_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R41_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R41_OVER_5YEARS;
private BigDecimal R41_TOTAL;
private BigDecimal R42_1_DAY;
private BigDecimal R42_2TO7_DAYS;
private BigDecimal R42_8TO14_DAYS;
private BigDecimal R42_15TO30_DAYS;
private BigDecimal R42_31DAYS_UPTO_2MONTHS;
private BigDecimal R42_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R42_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R42_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R42_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R42_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R42_OVER_5YEARS;
private BigDecimal R42_TOTAL;
private BigDecimal R43_1_DAY;
private BigDecimal R43_2TO7_DAYS;
private BigDecimal R43_8TO14_DAYS;
private BigDecimal R43_15TO30_DAYS;
private BigDecimal R43_31DAYS_UPTO_2MONTHS;
private BigDecimal R43_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R43_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R43_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R43_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R43_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R43_OVER_5YEARS;
private BigDecimal R43_TOTAL;
private BigDecimal R44_1_DAY;
private BigDecimal R44_2TO7_DAYS;
private BigDecimal R44_8TO14_DAYS;
private BigDecimal R44_15TO30_DAYS;
private BigDecimal R44_31DAYS_UPTO_2MONTHS;
private BigDecimal R44_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R44_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R44_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R44_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R44_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R44_OVER_5YEARS;
private BigDecimal R44_TOTAL;
private BigDecimal R45_1_DAY;
private BigDecimal R45_2TO7_DAYS;
private BigDecimal R45_8TO14_DAYS;
private BigDecimal R45_15TO30_DAYS;
private BigDecimal R45_31DAYS_UPTO_2MONTHS;
private BigDecimal R45_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R45_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R45_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R45_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R45_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R45_OVER_5YEARS;
private BigDecimal R45_TOTAL;
private BigDecimal R46_1_DAY;
private BigDecimal R46_2TO7_DAYS;
private BigDecimal R46_8TO14_DAYS;
private BigDecimal R46_15TO30_DAYS;
private BigDecimal R46_31DAYS_UPTO_2MONTHS;
private BigDecimal R46_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R46_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R46_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R46_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R46_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R46_OVER_5YEARS;
private BigDecimal R46_TOTAL;
private BigDecimal R47_1_DAY;
private BigDecimal R47_2TO7_DAYS;
private BigDecimal R47_8TO14_DAYS;
private BigDecimal R47_15TO30_DAYS;
private BigDecimal R47_31DAYS_UPTO_2MONTHS;
private BigDecimal R47_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R47_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R47_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R47_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R47_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R47_OVER_5YEARS;
private BigDecimal R47_TOTAL;
private BigDecimal R48_1_DAY;
private BigDecimal R48_2TO7_DAYS;
private BigDecimal R48_8TO14_DAYS;
private BigDecimal R48_15TO30_DAYS;
private BigDecimal R48_31DAYS_UPTO_2MONTHS;
private BigDecimal R48_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R48_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R48_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R48_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R48_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R48_OVER_5YEARS;
private BigDecimal R48_TOTAL;
private BigDecimal R49_1_DAY;
private BigDecimal R49_2TO7_DAYS;
private BigDecimal R49_8TO14_DAYS;
private BigDecimal R49_15TO30_DAYS;
private BigDecimal R49_31DAYS_UPTO_2MONTHS;
private BigDecimal R49_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R49_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R49_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R49_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R49_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R49_OVER_5YEARS;
private BigDecimal R49_TOTAL;
private BigDecimal R50_1_DAY;
private BigDecimal R50_2TO7_DAYS;
private BigDecimal R50_8TO14_DAYS;
private BigDecimal R50_15TO30_DAYS;
private BigDecimal R50_31DAYS_UPTO_2MONTHS;
private BigDecimal R50_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R50_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R50_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R50_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R50_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R50_OVER_5YEARS;
private BigDecimal R50_TOTAL;
private BigDecimal R51_1_DAY;
private BigDecimal R51_2TO7_DAYS;
private BigDecimal R51_8TO14_DAYS;
private BigDecimal R51_15TO30_DAYS;
private BigDecimal R51_31DAYS_UPTO_2MONTHS;
private BigDecimal R51_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R51_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R51_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R51_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R51_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R51_OVER_5YEARS;
private BigDecimal R51_TOTAL;
private BigDecimal R52_1_DAY;
private BigDecimal R52_2TO7_DAYS;
private BigDecimal R52_8TO14_DAYS;
private BigDecimal R52_15TO30_DAYS;
private BigDecimal R52_31DAYS_UPTO_2MONTHS;
private BigDecimal R52_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R52_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R52_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R52_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R52_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R52_OVER_5YEARS;
private BigDecimal R52_TOTAL;
private BigDecimal R53_1_DAY;
private BigDecimal R53_2TO7_DAYS;
private BigDecimal R53_8TO14_DAYS;
private BigDecimal R53_15TO30_DAYS;
private BigDecimal R53_31DAYS_UPTO_2MONTHS;
private BigDecimal R53_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R53_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R53_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R53_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R53_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R53_OVER_5YEARS;
private BigDecimal R53_TOTAL;
private BigDecimal R54_1_DAY;
private BigDecimal R54_2TO7_DAYS;
private BigDecimal R54_8TO14_DAYS;
private BigDecimal R54_15TO30_DAYS;
private BigDecimal R54_31DAYS_UPTO_2MONTHS;
private BigDecimal R54_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R54_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R54_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R54_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R54_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R54_OVER_5YEARS;
private BigDecimal R54_TOTAL;
private BigDecimal R55_1_DAY;
private BigDecimal R55_2TO7_DAYS;
private BigDecimal R55_8TO14_DAYS;
private BigDecimal R55_15TO30_DAYS;
private BigDecimal R55_31DAYS_UPTO_2MONTHS;
private BigDecimal R55_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R55_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R55_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R55_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R55_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R55_OVER_5YEARS;
private BigDecimal R55_TOTAL;
private BigDecimal R56_1_DAY;
private BigDecimal R56_2TO7_DAYS;
private BigDecimal R56_8TO14_DAYS;
private BigDecimal R56_15TO30_DAYS;
private BigDecimal R56_31DAYS_UPTO_2MONTHS;
private BigDecimal R56_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R56_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R56_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R56_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R56_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R56_OVER_5YEARS;
private BigDecimal R56_TOTAL;
private BigDecimal R57_1_DAY;
private BigDecimal R57_2TO7_DAYS;
private BigDecimal R57_8TO14_DAYS;
private BigDecimal R57_15TO30_DAYS;
private BigDecimal R57_31DAYS_UPTO_2MONTHS;
private BigDecimal R57_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R57_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R57_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R57_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R57_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R57_OVER_5YEARS;
private BigDecimal R57_TOTAL;
private BigDecimal R58_1_DAY;
private BigDecimal R58_2TO7_DAYS;
private BigDecimal R58_8TO14_DAYS;
private BigDecimal R58_15TO30_DAYS;
private BigDecimal R58_31DAYS_UPTO_2MONTHS;
private BigDecimal R58_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R58_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R58_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R58_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R58_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R58_OVER_5YEARS;
private BigDecimal R58_TOTAL;
private BigDecimal R59_1_DAY;
private BigDecimal R59_2TO7_DAYS;
private BigDecimal R59_8TO14_DAYS;
private BigDecimal R59_15TO30_DAYS;
private BigDecimal R59_31DAYS_UPTO_2MONTHS;
private BigDecimal R59_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R59_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R59_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R59_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R59_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R59_OVER_5YEARS;
private BigDecimal R59_TOTAL;
private BigDecimal R60_1_DAY;
private BigDecimal R60_2TO7_DAYS;
private BigDecimal R60_8TO14_DAYS;
private BigDecimal R60_15TO30_DAYS;
private BigDecimal R60_31DAYS_UPTO_2MONTHS;
private BigDecimal R60_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R60_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R60_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R60_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R60_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R60_OVER_5YEARS;
private BigDecimal R60_TOTAL;
private BigDecimal R61_1_DAY;
private BigDecimal R61_2TO7_DAYS;
private BigDecimal R61_8TO14_DAYS;
private BigDecimal R61_15TO30_DAYS;
private BigDecimal R61_31DAYS_UPTO_2MONTHS;
private BigDecimal R61_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R61_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R61_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R61_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R61_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R61_OVER_5YEARS;
private BigDecimal R61_TOTAL;
private BigDecimal R62_1_DAY;
private BigDecimal R62_2TO7_DAYS;
private BigDecimal R62_8TO14_DAYS;
private BigDecimal R62_15TO30_DAYS;
private BigDecimal R62_31DAYS_UPTO_2MONTHS;
private BigDecimal R62_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R62_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R62_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R62_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R62_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R62_OVER_5YEARS;
private BigDecimal R62_TOTAL;
private BigDecimal R63_1_DAY;
private BigDecimal R63_2TO7_DAYS;
private BigDecimal R63_8TO14_DAYS;
private BigDecimal R63_15TO30_DAYS;
private BigDecimal R63_31DAYS_UPTO_2MONTHS;
private BigDecimal R63_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R63_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R63_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R63_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R63_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R63_OVER_5YEARS;
private BigDecimal R63_TOTAL;
private BigDecimal R64_1_DAY;
private BigDecimal R64_2TO7_DAYS;
private BigDecimal R64_8TO14_DAYS;
private BigDecimal R64_15TO30_DAYS;
private BigDecimal R64_31DAYS_UPTO_2MONTHS;
private BigDecimal R64_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R64_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R64_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R64_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R64_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R64_OVER_5YEARS;
private BigDecimal R64_TOTAL;
private BigDecimal R65_1_DAY;
private BigDecimal R65_2TO7_DAYS;
private BigDecimal R65_8TO14_DAYS;
private BigDecimal R65_15TO30_DAYS;
private BigDecimal R65_31DAYS_UPTO_2MONTHS;
private BigDecimal R65_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R65_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R65_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R65_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R65_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R65_OVER_5YEARS;
private BigDecimal R65_TOTAL;
private BigDecimal R66_1_DAY;
private BigDecimal R66_2TO7_DAYS;
private BigDecimal R66_8TO14_DAYS;
private BigDecimal R66_15TO30_DAYS;
private BigDecimal R66_31DAYS_UPTO_2MONTHS;
private BigDecimal R66_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R66_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R66_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R66_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R66_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R66_OVER_5YEARS;
private BigDecimal R66_TOTAL;
private BigDecimal R67_1_DAY;
private BigDecimal R67_2TO7_DAYS;
private BigDecimal R67_8TO14_DAYS;
private BigDecimal R67_15TO30_DAYS;
private BigDecimal R67_31DAYS_UPTO_2MONTHS;
private BigDecimal R67_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R67_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R67_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R67_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R67_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R67_OVER_5YEARS;
private BigDecimal R67_TOTAL;
private BigDecimal R68_1_DAY;
private BigDecimal R68_2TO7_DAYS;
private BigDecimal R68_8TO14_DAYS;
private BigDecimal R68_15TO30_DAYS;
private BigDecimal R68_31DAYS_UPTO_2MONTHS;
private BigDecimal R68_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R68_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R68_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R68_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R68_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R68_OVER_5YEARS;
private BigDecimal R68_TOTAL;
private BigDecimal R69_1_DAY;
private BigDecimal R69_2TO7_DAYS;
private BigDecimal R69_8TO14_DAYS;
private BigDecimal R69_15TO30_DAYS;
private BigDecimal R69_31DAYS_UPTO_2MONTHS;
private BigDecimal R69_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R69_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R69_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R69_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R69_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R69_OVER_5YEARS;
private BigDecimal R69_TOTAL;
private BigDecimal R70_1_DAY;
private BigDecimal R70_2TO7_DAYS;
private BigDecimal R70_8TO14_DAYS;
private BigDecimal R70_15TO30_DAYS;
private BigDecimal R70_31DAYS_UPTO_2MONTHS;
private BigDecimal R70_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R70_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R70_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R70_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R70_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R70_OVER_5YEARS;
private BigDecimal R70_TOTAL;
private BigDecimal R71_1_DAY;
private BigDecimal R71_2TO7_DAYS;
private BigDecimal R71_8TO14_DAYS;
private BigDecimal R71_15TO30_DAYS;
private BigDecimal R71_31DAYS_UPTO_2MONTHS;
private BigDecimal R71_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R71_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R71_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R71_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R71_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R71_OVER_5YEARS;
private BigDecimal R71_TOTAL;
private BigDecimal R72_1_DAY;
private BigDecimal R72_2TO7_DAYS;
private BigDecimal R72_8TO14_DAYS;
private BigDecimal R72_15TO30_DAYS;
private BigDecimal R72_31DAYS_UPTO_2MONTHS;
private BigDecimal R72_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R72_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R72_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R72_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R72_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R72_OVER_5YEARS;
private BigDecimal R72_TOTAL;
private BigDecimal R73_1_DAY;
private BigDecimal R73_2TO7_DAYS;
private BigDecimal R73_8TO14_DAYS;
private BigDecimal R73_15TO30_DAYS;
private BigDecimal R73_31DAYS_UPTO_2MONTHS;
private BigDecimal R73_MORETHAN_2MONTHS_UPTO_3MONHTS;
private BigDecimal R73_OVER_3MONTHS_UPTO_6MONTHS;
private BigDecimal R73_OVER_6MONTHS_UPTO_1YEAR;
private BigDecimal R73_OVER_1YEAR_UPTO_3YEARS;
private BigDecimal R73_OVER_3YEARS_UPTO_5YEARS;
private BigDecimal R73_OVER_5YEARS;
private BigDecimal R73_TOTAL;








public Date getReportDate() {
	return report_date;
}








public void setReportDate(Date report_date) {
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








public BigDecimal getR8_1_DAY() {
	return R8_1_DAY;
}








public void setR8_1_DAY(BigDecimal r8_1_DAY) {
	R8_1_DAY = r8_1_DAY;
}








public BigDecimal getR8_2TO7_DAYS() {
	return R8_2TO7_DAYS;
}








public void setR8_2TO7_DAYS(BigDecimal r8_2to7_DAYS) {
	R8_2TO7_DAYS = r8_2to7_DAYS;
}








public BigDecimal getR8_8TO14_DAYS() {
	return R8_8TO14_DAYS;
}








public void setR8_8TO14_DAYS(BigDecimal r8_8to14_DAYS) {
	R8_8TO14_DAYS = r8_8to14_DAYS;
}








public BigDecimal getR8_15TO30_DAYS() {
	return R8_15TO30_DAYS;
}








public void setR8_15TO30_DAYS(BigDecimal r8_15to30_DAYS) {
	R8_15TO30_DAYS = r8_15to30_DAYS;
}








public BigDecimal getR8_31DAYS_UPTO_2MONTHS() {
	return R8_31DAYS_UPTO_2MONTHS;
}








public void setR8_31DAYS_UPTO_2MONTHS(BigDecimal r8_31days_UPTO_2MONTHS) {
	R8_31DAYS_UPTO_2MONTHS = r8_31days_UPTO_2MONTHS;
}








public BigDecimal getR8_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R8_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR8_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r8_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R8_MORETHAN_2MONTHS_UPTO_3MONHTS = r8_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR8_OVER_3MONTHS_UPTO_6MONTHS() {
	return R8_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR8_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r8_OVER_3MONTHS_UPTO_6MONTHS) {
	R8_OVER_3MONTHS_UPTO_6MONTHS = r8_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR8_OVER_6MONTHS_UPTO_1YEAR() {
	return R8_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR8_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r8_OVER_6MONTHS_UPTO_1YEAR) {
	R8_OVER_6MONTHS_UPTO_1YEAR = r8_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR8_OVER_1YEAR_UPTO_3YEARS() {
	return R8_OVER_1YEAR_UPTO_3YEARS;
}








public void setR8_OVER_1YEAR_UPTO_3YEARS(BigDecimal r8_OVER_1YEAR_UPTO_3YEARS) {
	R8_OVER_1YEAR_UPTO_3YEARS = r8_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR8_OVER_3YEARS_UPTO_5YEARS() {
	return R8_OVER_3YEARS_UPTO_5YEARS;
}








public void setR8_OVER_3YEARS_UPTO_5YEARS(BigDecimal r8_OVER_3YEARS_UPTO_5YEARS) {
	R8_OVER_3YEARS_UPTO_5YEARS = r8_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR8_OVER_5YEARS() {
	return R8_OVER_5YEARS;
}








public void setR8_OVER_5YEARS(BigDecimal r8_OVER_5YEARS) {
	R8_OVER_5YEARS = r8_OVER_5YEARS;
}








public BigDecimal getR8_TOTAL() {
	return R8_TOTAL;
}








public void setR8_TOTAL(BigDecimal r8_TOTAL) {
	R8_TOTAL = r8_TOTAL;
}








public BigDecimal getR9_1_DAY() {
	return R9_1_DAY;
}








public void setR9_1_DAY(BigDecimal r9_1_DAY) {
	R9_1_DAY = r9_1_DAY;
}








public BigDecimal getR9_2TO7_DAYS() {
	return R9_2TO7_DAYS;
}








public void setR9_2TO7_DAYS(BigDecimal r9_2to7_DAYS) {
	R9_2TO7_DAYS = r9_2to7_DAYS;
}








public BigDecimal getR9_8TO14_DAYS() {
	return R9_8TO14_DAYS;
}








public void setR9_8TO14_DAYS(BigDecimal r9_8to14_DAYS) {
	R9_8TO14_DAYS = r9_8to14_DAYS;
}








public BigDecimal getR9_15TO30_DAYS() {
	return R9_15TO30_DAYS;
}








public void setR9_15TO30_DAYS(BigDecimal r9_15to30_DAYS) {
	R9_15TO30_DAYS = r9_15to30_DAYS;
}








public BigDecimal getR9_31DAYS_UPTO_2MONTHS() {
	return R9_31DAYS_UPTO_2MONTHS;
}








public void setR9_31DAYS_UPTO_2MONTHS(BigDecimal r9_31days_UPTO_2MONTHS) {
	R9_31DAYS_UPTO_2MONTHS = r9_31days_UPTO_2MONTHS;
}








public BigDecimal getR9_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R9_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR9_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r9_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R9_MORETHAN_2MONTHS_UPTO_3MONHTS = r9_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR9_OVER_3MONTHS_UPTO_6MONTHS() {
	return R9_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR9_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r9_OVER_3MONTHS_UPTO_6MONTHS) {
	R9_OVER_3MONTHS_UPTO_6MONTHS = r9_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR9_OVER_6MONTHS_UPTO_1YEAR() {
	return R9_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR9_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r9_OVER_6MONTHS_UPTO_1YEAR) {
	R9_OVER_6MONTHS_UPTO_1YEAR = r9_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR9_OVER_1YEAR_UPTO_3YEARS() {
	return R9_OVER_1YEAR_UPTO_3YEARS;
}








public void setR9_OVER_1YEAR_UPTO_3YEARS(BigDecimal r9_OVER_1YEAR_UPTO_3YEARS) {
	R9_OVER_1YEAR_UPTO_3YEARS = r9_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR9_OVER_3YEARS_UPTO_5YEARS() {
	return R9_OVER_3YEARS_UPTO_5YEARS;
}








public void setR9_OVER_3YEARS_UPTO_5YEARS(BigDecimal r9_OVER_3YEARS_UPTO_5YEARS) {
	R9_OVER_3YEARS_UPTO_5YEARS = r9_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR9_OVER_5YEARS() {
	return R9_OVER_5YEARS;
}








public void setR9_OVER_5YEARS(BigDecimal r9_OVER_5YEARS) {
	R9_OVER_5YEARS = r9_OVER_5YEARS;
}








public BigDecimal getR9_TOTAL() {
	return R9_TOTAL;
}








public void setR9_TOTAL(BigDecimal r9_TOTAL) {
	R9_TOTAL = r9_TOTAL;
}








public BigDecimal getR10_1_DAY() {
	return R10_1_DAY;
}








public void setR10_1_DAY(BigDecimal r10_1_DAY) {
	R10_1_DAY = r10_1_DAY;
}








public BigDecimal getR10_2TO7_DAYS() {
	return R10_2TO7_DAYS;
}








public void setR10_2TO7_DAYS(BigDecimal r10_2to7_DAYS) {
	R10_2TO7_DAYS = r10_2to7_DAYS;
}








public BigDecimal getR10_8TO14_DAYS() {
	return R10_8TO14_DAYS;
}








public void setR10_8TO14_DAYS(BigDecimal r10_8to14_DAYS) {
	R10_8TO14_DAYS = r10_8to14_DAYS;
}








public BigDecimal getR10_15TO30_DAYS() {
	return R10_15TO30_DAYS;
}








public void setR10_15TO30_DAYS(BigDecimal r10_15to30_DAYS) {
	R10_15TO30_DAYS = r10_15to30_DAYS;
}








public BigDecimal getR10_31DAYS_UPTO_2MONTHS() {
	return R10_31DAYS_UPTO_2MONTHS;
}








public void setR10_31DAYS_UPTO_2MONTHS(BigDecimal r10_31days_UPTO_2MONTHS) {
	R10_31DAYS_UPTO_2MONTHS = r10_31days_UPTO_2MONTHS;
}








public BigDecimal getR10_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R10_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR10_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r10_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R10_MORETHAN_2MONTHS_UPTO_3MONHTS = r10_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR10_OVER_3MONTHS_UPTO_6MONTHS() {
	return R10_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR10_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r10_OVER_3MONTHS_UPTO_6MONTHS) {
	R10_OVER_3MONTHS_UPTO_6MONTHS = r10_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR10_OVER_6MONTHS_UPTO_1YEAR() {
	return R10_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR10_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r10_OVER_6MONTHS_UPTO_1YEAR) {
	R10_OVER_6MONTHS_UPTO_1YEAR = r10_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR10_OVER_1YEAR_UPTO_3YEARS() {
	return R10_OVER_1YEAR_UPTO_3YEARS;
}








public void setR10_OVER_1YEAR_UPTO_3YEARS(BigDecimal r10_OVER_1YEAR_UPTO_3YEARS) {
	R10_OVER_1YEAR_UPTO_3YEARS = r10_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR10_OVER_3YEARS_UPTO_5YEARS() {
	return R10_OVER_3YEARS_UPTO_5YEARS;
}








public void setR10_OVER_3YEARS_UPTO_5YEARS(BigDecimal r10_OVER_3YEARS_UPTO_5YEARS) {
	R10_OVER_3YEARS_UPTO_5YEARS = r10_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR10_OVER_5YEARS() {
	return R10_OVER_5YEARS;
}








public void setR10_OVER_5YEARS(BigDecimal r10_OVER_5YEARS) {
	R10_OVER_5YEARS = r10_OVER_5YEARS;
}








public BigDecimal getR10_TOTAL() {
	return R10_TOTAL;
}








public void setR10_TOTAL(BigDecimal r10_TOTAL) {
	R10_TOTAL = r10_TOTAL;
}








public BigDecimal getR11_1_DAY() {
	return R11_1_DAY;
}








public void setR11_1_DAY(BigDecimal r11_1_DAY) {
	R11_1_DAY = r11_1_DAY;
}








public BigDecimal getR11_2TO7_DAYS() {
	return R11_2TO7_DAYS;
}








public void setR11_2TO7_DAYS(BigDecimal r11_2to7_DAYS) {
	R11_2TO7_DAYS = r11_2to7_DAYS;
}








public BigDecimal getR11_8TO14_DAYS() {
	return R11_8TO14_DAYS;
}








public void setR11_8TO14_DAYS(BigDecimal r11_8to14_DAYS) {
	R11_8TO14_DAYS = r11_8to14_DAYS;
}








public BigDecimal getR11_15TO30_DAYS() {
	return R11_15TO30_DAYS;
}








public void setR11_15TO30_DAYS(BigDecimal r11_15to30_DAYS) {
	R11_15TO30_DAYS = r11_15to30_DAYS;
}








public BigDecimal getR11_31DAYS_UPTO_2MONTHS() {
	return R11_31DAYS_UPTO_2MONTHS;
}








public void setR11_31DAYS_UPTO_2MONTHS(BigDecimal r11_31days_UPTO_2MONTHS) {
	R11_31DAYS_UPTO_2MONTHS = r11_31days_UPTO_2MONTHS;
}








public BigDecimal getR11_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R11_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR11_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r11_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R11_MORETHAN_2MONTHS_UPTO_3MONHTS = r11_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR11_OVER_3MONTHS_UPTO_6MONTHS() {
	return R11_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR11_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r11_OVER_3MONTHS_UPTO_6MONTHS) {
	R11_OVER_3MONTHS_UPTO_6MONTHS = r11_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR11_OVER_6MONTHS_UPTO_1YEAR() {
	return R11_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR11_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r11_OVER_6MONTHS_UPTO_1YEAR) {
	R11_OVER_6MONTHS_UPTO_1YEAR = r11_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR11_OVER_1YEAR_UPTO_3YEARS() {
	return R11_OVER_1YEAR_UPTO_3YEARS;
}








public void setR11_OVER_1YEAR_UPTO_3YEARS(BigDecimal r11_OVER_1YEAR_UPTO_3YEARS) {
	R11_OVER_1YEAR_UPTO_3YEARS = r11_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR11_OVER_3YEARS_UPTO_5YEARS() {
	return R11_OVER_3YEARS_UPTO_5YEARS;
}








public void setR11_OVER_3YEARS_UPTO_5YEARS(BigDecimal r11_OVER_3YEARS_UPTO_5YEARS) {
	R11_OVER_3YEARS_UPTO_5YEARS = r11_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR11_OVER_5YEARS() {
	return R11_OVER_5YEARS;
}








public void setR11_OVER_5YEARS(BigDecimal r11_OVER_5YEARS) {
	R11_OVER_5YEARS = r11_OVER_5YEARS;
}








public BigDecimal getR11_TOTAL() {
	return R11_TOTAL;
}








public void setR11_TOTAL(BigDecimal r11_TOTAL) {
	R11_TOTAL = r11_TOTAL;
}








public BigDecimal getR12_1_DAY() {
	return R12_1_DAY;
}








public void setR12_1_DAY(BigDecimal r12_1_DAY) {
	R12_1_DAY = r12_1_DAY;
}








public BigDecimal getR12_2TO7_DAYS() {
	return R12_2TO7_DAYS;
}








public void setR12_2TO7_DAYS(BigDecimal r12_2to7_DAYS) {
	R12_2TO7_DAYS = r12_2to7_DAYS;
}








public BigDecimal getR12_8TO14_DAYS() {
	return R12_8TO14_DAYS;
}








public void setR12_8TO14_DAYS(BigDecimal r12_8to14_DAYS) {
	R12_8TO14_DAYS = r12_8to14_DAYS;
}








public BigDecimal getR12_15TO30_DAYS() {
	return R12_15TO30_DAYS;
}








public void setR12_15TO30_DAYS(BigDecimal r12_15to30_DAYS) {
	R12_15TO30_DAYS = r12_15to30_DAYS;
}








public BigDecimal getR12_31DAYS_UPTO_2MONTHS() {
	return R12_31DAYS_UPTO_2MONTHS;
}








public void setR12_31DAYS_UPTO_2MONTHS(BigDecimal r12_31days_UPTO_2MONTHS) {
	R12_31DAYS_UPTO_2MONTHS = r12_31days_UPTO_2MONTHS;
}








public BigDecimal getR12_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R12_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR12_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r12_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R12_MORETHAN_2MONTHS_UPTO_3MONHTS = r12_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR12_OVER_3MONTHS_UPTO_6MONTHS() {
	return R12_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR12_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r12_OVER_3MONTHS_UPTO_6MONTHS) {
	R12_OVER_3MONTHS_UPTO_6MONTHS = r12_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR12_OVER_6MONTHS_UPTO_1YEAR() {
	return R12_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR12_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r12_OVER_6MONTHS_UPTO_1YEAR) {
	R12_OVER_6MONTHS_UPTO_1YEAR = r12_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR12_OVER_1YEAR_UPTO_3YEARS() {
	return R12_OVER_1YEAR_UPTO_3YEARS;
}








public void setR12_OVER_1YEAR_UPTO_3YEARS(BigDecimal r12_OVER_1YEAR_UPTO_3YEARS) {
	R12_OVER_1YEAR_UPTO_3YEARS = r12_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR12_OVER_3YEARS_UPTO_5YEARS() {
	return R12_OVER_3YEARS_UPTO_5YEARS;
}








public void setR12_OVER_3YEARS_UPTO_5YEARS(BigDecimal r12_OVER_3YEARS_UPTO_5YEARS) {
	R12_OVER_3YEARS_UPTO_5YEARS = r12_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR12_OVER_5YEARS() {
	return R12_OVER_5YEARS;
}








public void setR12_OVER_5YEARS(BigDecimal r12_OVER_5YEARS) {
	R12_OVER_5YEARS = r12_OVER_5YEARS;
}








public BigDecimal getR12_TOTAL() {
	return R12_TOTAL;
}








public void setR12_TOTAL(BigDecimal r12_TOTAL) {
	R12_TOTAL = r12_TOTAL;
}








public BigDecimal getR13_1_DAY() {
	return R13_1_DAY;
}








public void setR13_1_DAY(BigDecimal r13_1_DAY) {
	R13_1_DAY = r13_1_DAY;
}








public BigDecimal getR13_2TO7_DAYS() {
	return R13_2TO7_DAYS;
}








public void setR13_2TO7_DAYS(BigDecimal r13_2to7_DAYS) {
	R13_2TO7_DAYS = r13_2to7_DAYS;
}








public BigDecimal getR13_8TO14_DAYS() {
	return R13_8TO14_DAYS;
}








public void setR13_8TO14_DAYS(BigDecimal r13_8to14_DAYS) {
	R13_8TO14_DAYS = r13_8to14_DAYS;
}








public BigDecimal getR13_15TO30_DAYS() {
	return R13_15TO30_DAYS;
}








public void setR13_15TO30_DAYS(BigDecimal r13_15to30_DAYS) {
	R13_15TO30_DAYS = r13_15to30_DAYS;
}








public BigDecimal getR13_31DAYS_UPTO_2MONTHS() {
	return R13_31DAYS_UPTO_2MONTHS;
}








public void setR13_31DAYS_UPTO_2MONTHS(BigDecimal r13_31days_UPTO_2MONTHS) {
	R13_31DAYS_UPTO_2MONTHS = r13_31days_UPTO_2MONTHS;
}








public BigDecimal getR13_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R13_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR13_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r13_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R13_MORETHAN_2MONTHS_UPTO_3MONHTS = r13_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR13_OVER_3MONTHS_UPTO_6MONTHS() {
	return R13_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR13_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r13_OVER_3MONTHS_UPTO_6MONTHS) {
	R13_OVER_3MONTHS_UPTO_6MONTHS = r13_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR13_OVER_6MONTHS_UPTO_1YEAR() {
	return R13_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR13_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r13_OVER_6MONTHS_UPTO_1YEAR) {
	R13_OVER_6MONTHS_UPTO_1YEAR = r13_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR13_OVER_1YEAR_UPTO_3YEARS() {
	return R13_OVER_1YEAR_UPTO_3YEARS;
}








public void setR13_OVER_1YEAR_UPTO_3YEARS(BigDecimal r13_OVER_1YEAR_UPTO_3YEARS) {
	R13_OVER_1YEAR_UPTO_3YEARS = r13_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR13_OVER_3YEARS_UPTO_5YEARS() {
	return R13_OVER_3YEARS_UPTO_5YEARS;
}








public void setR13_OVER_3YEARS_UPTO_5YEARS(BigDecimal r13_OVER_3YEARS_UPTO_5YEARS) {
	R13_OVER_3YEARS_UPTO_5YEARS = r13_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR13_OVER_5YEARS() {
	return R13_OVER_5YEARS;
}








public void setR13_OVER_5YEARS(BigDecimal r13_OVER_5YEARS) {
	R13_OVER_5YEARS = r13_OVER_5YEARS;
}








public BigDecimal getR13_TOTAL() {
	return R13_TOTAL;
}








public void setR13_TOTAL(BigDecimal r13_TOTAL) {
	R13_TOTAL = r13_TOTAL;
}








public BigDecimal getR14_1_DAY() {
	return R14_1_DAY;
}








public void setR14_1_DAY(BigDecimal r14_1_DAY) {
	R14_1_DAY = r14_1_DAY;
}








public BigDecimal getR14_2TO7_DAYS() {
	return R14_2TO7_DAYS;
}








public void setR14_2TO7_DAYS(BigDecimal r14_2to7_DAYS) {
	R14_2TO7_DAYS = r14_2to7_DAYS;
}








public BigDecimal getR14_8TO14_DAYS() {
	return R14_8TO14_DAYS;
}








public void setR14_8TO14_DAYS(BigDecimal r14_8to14_DAYS) {
	R14_8TO14_DAYS = r14_8to14_DAYS;
}








public BigDecimal getR14_15TO30_DAYS() {
	return R14_15TO30_DAYS;
}








public void setR14_15TO30_DAYS(BigDecimal r14_15to30_DAYS) {
	R14_15TO30_DAYS = r14_15to30_DAYS;
}








public BigDecimal getR14_31DAYS_UPTO_2MONTHS() {
	return R14_31DAYS_UPTO_2MONTHS;
}








public void setR14_31DAYS_UPTO_2MONTHS(BigDecimal r14_31days_UPTO_2MONTHS) {
	R14_31DAYS_UPTO_2MONTHS = r14_31days_UPTO_2MONTHS;
}








public BigDecimal getR14_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R14_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR14_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r14_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R14_MORETHAN_2MONTHS_UPTO_3MONHTS = r14_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR14_OVER_3MONTHS_UPTO_6MONTHS() {
	return R14_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR14_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r14_OVER_3MONTHS_UPTO_6MONTHS) {
	R14_OVER_3MONTHS_UPTO_6MONTHS = r14_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR14_OVER_6MONTHS_UPTO_1YEAR() {
	return R14_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR14_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r14_OVER_6MONTHS_UPTO_1YEAR) {
	R14_OVER_6MONTHS_UPTO_1YEAR = r14_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR14_OVER_1YEAR_UPTO_3YEARS() {
	return R14_OVER_1YEAR_UPTO_3YEARS;
}








public void setR14_OVER_1YEAR_UPTO_3YEARS(BigDecimal r14_OVER_1YEAR_UPTO_3YEARS) {
	R14_OVER_1YEAR_UPTO_3YEARS = r14_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR14_OVER_3YEARS_UPTO_5YEARS() {
	return R14_OVER_3YEARS_UPTO_5YEARS;
}








public void setR14_OVER_3YEARS_UPTO_5YEARS(BigDecimal r14_OVER_3YEARS_UPTO_5YEARS) {
	R14_OVER_3YEARS_UPTO_5YEARS = r14_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR14_OVER_5YEARS() {
	return R14_OVER_5YEARS;
}








public void setR14_OVER_5YEARS(BigDecimal r14_OVER_5YEARS) {
	R14_OVER_5YEARS = r14_OVER_5YEARS;
}








public BigDecimal getR14_TOTAL() {
	return R14_TOTAL;
}








public void setR14_TOTAL(BigDecimal r14_TOTAL) {
	R14_TOTAL = r14_TOTAL;
}








public BigDecimal getR15_1_DAY() {
	return R15_1_DAY;
}








public void setR15_1_DAY(BigDecimal r15_1_DAY) {
	R15_1_DAY = r15_1_DAY;
}








public BigDecimal getR15_2TO7_DAYS() {
	return R15_2TO7_DAYS;
}








public void setR15_2TO7_DAYS(BigDecimal r15_2to7_DAYS) {
	R15_2TO7_DAYS = r15_2to7_DAYS;
}








public BigDecimal getR15_8TO14_DAYS() {
	return R15_8TO14_DAYS;
}








public void setR15_8TO14_DAYS(BigDecimal r15_8to14_DAYS) {
	R15_8TO14_DAYS = r15_8to14_DAYS;
}








public BigDecimal getR15_15TO30_DAYS() {
	return R15_15TO30_DAYS;
}








public void setR15_15TO30_DAYS(BigDecimal r15_15to30_DAYS) {
	R15_15TO30_DAYS = r15_15to30_DAYS;
}








public BigDecimal getR15_31DAYS_UPTO_2MONTHS() {
	return R15_31DAYS_UPTO_2MONTHS;
}








public void setR15_31DAYS_UPTO_2MONTHS(BigDecimal r15_31days_UPTO_2MONTHS) {
	R15_31DAYS_UPTO_2MONTHS = r15_31days_UPTO_2MONTHS;
}








public BigDecimal getR15_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R15_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR15_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r15_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R15_MORETHAN_2MONTHS_UPTO_3MONHTS = r15_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR15_OVER_3MONTHS_UPTO_6MONTHS() {
	return R15_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR15_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r15_OVER_3MONTHS_UPTO_6MONTHS) {
	R15_OVER_3MONTHS_UPTO_6MONTHS = r15_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR15_OVER_6MONTHS_UPTO_1YEAR() {
	return R15_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR15_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r15_OVER_6MONTHS_UPTO_1YEAR) {
	R15_OVER_6MONTHS_UPTO_1YEAR = r15_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR15_OVER_1YEAR_UPTO_3YEARS() {
	return R15_OVER_1YEAR_UPTO_3YEARS;
}








public void setR15_OVER_1YEAR_UPTO_3YEARS(BigDecimal r15_OVER_1YEAR_UPTO_3YEARS) {
	R15_OVER_1YEAR_UPTO_3YEARS = r15_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR15_OVER_3YEARS_UPTO_5YEARS() {
	return R15_OVER_3YEARS_UPTO_5YEARS;
}








public void setR15_OVER_3YEARS_UPTO_5YEARS(BigDecimal r15_OVER_3YEARS_UPTO_5YEARS) {
	R15_OVER_3YEARS_UPTO_5YEARS = r15_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR15_OVER_5YEARS() {
	return R15_OVER_5YEARS;
}








public void setR15_OVER_5YEARS(BigDecimal r15_OVER_5YEARS) {
	R15_OVER_5YEARS = r15_OVER_5YEARS;
}








public BigDecimal getR15_TOTAL() {
	return R15_TOTAL;
}








public void setR15_TOTAL(BigDecimal r15_TOTAL) {
	R15_TOTAL = r15_TOTAL;
}








public BigDecimal getR16_1_DAY() {
	return R16_1_DAY;
}








public void setR16_1_DAY(BigDecimal r16_1_DAY) {
	R16_1_DAY = r16_1_DAY;
}








public BigDecimal getR16_2TO7_DAYS() {
	return R16_2TO7_DAYS;
}








public void setR16_2TO7_DAYS(BigDecimal r16_2to7_DAYS) {
	R16_2TO7_DAYS = r16_2to7_DAYS;
}








public BigDecimal getR16_8TO14_DAYS() {
	return R16_8TO14_DAYS;
}








public void setR16_8TO14_DAYS(BigDecimal r16_8to14_DAYS) {
	R16_8TO14_DAYS = r16_8to14_DAYS;
}








public BigDecimal getR16_15TO30_DAYS() {
	return R16_15TO30_DAYS;
}








public void setR16_15TO30_DAYS(BigDecimal r16_15to30_DAYS) {
	R16_15TO30_DAYS = r16_15to30_DAYS;
}








public BigDecimal getR16_31DAYS_UPTO_2MONTHS() {
	return R16_31DAYS_UPTO_2MONTHS;
}








public void setR16_31DAYS_UPTO_2MONTHS(BigDecimal r16_31days_UPTO_2MONTHS) {
	R16_31DAYS_UPTO_2MONTHS = r16_31days_UPTO_2MONTHS;
}








public BigDecimal getR16_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R16_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR16_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r16_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R16_MORETHAN_2MONTHS_UPTO_3MONHTS = r16_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR16_OVER_3MONTHS_UPTO_6MONTHS() {
	return R16_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR16_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r16_OVER_3MONTHS_UPTO_6MONTHS) {
	R16_OVER_3MONTHS_UPTO_6MONTHS = r16_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR16_OVER_6MONTHS_UPTO_1YEAR() {
	return R16_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR16_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r16_OVER_6MONTHS_UPTO_1YEAR) {
	R16_OVER_6MONTHS_UPTO_1YEAR = r16_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR16_OVER_1YEAR_UPTO_3YEARS() {
	return R16_OVER_1YEAR_UPTO_3YEARS;
}








public void setR16_OVER_1YEAR_UPTO_3YEARS(BigDecimal r16_OVER_1YEAR_UPTO_3YEARS) {
	R16_OVER_1YEAR_UPTO_3YEARS = r16_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR16_OVER_3YEARS_UPTO_5YEARS() {
	return R16_OVER_3YEARS_UPTO_5YEARS;
}








public void setR16_OVER_3YEARS_UPTO_5YEARS(BigDecimal r16_OVER_3YEARS_UPTO_5YEARS) {
	R16_OVER_3YEARS_UPTO_5YEARS = r16_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR16_OVER_5YEARS() {
	return R16_OVER_5YEARS;
}








public void setR16_OVER_5YEARS(BigDecimal r16_OVER_5YEARS) {
	R16_OVER_5YEARS = r16_OVER_5YEARS;
}








public BigDecimal getR16_TOTAL() {
	return R16_TOTAL;
}








public void setR16_TOTAL(BigDecimal r16_TOTAL) {
	R16_TOTAL = r16_TOTAL;
}








public BigDecimal getR17_1_DAY() {
	return R17_1_DAY;
}








public void setR17_1_DAY(BigDecimal r17_1_DAY) {
	R17_1_DAY = r17_1_DAY;
}








public BigDecimal getR17_2TO7_DAYS() {
	return R17_2TO7_DAYS;
}








public void setR17_2TO7_DAYS(BigDecimal r17_2to7_DAYS) {
	R17_2TO7_DAYS = r17_2to7_DAYS;
}








public BigDecimal getR17_8TO14_DAYS() {
	return R17_8TO14_DAYS;
}








public void setR17_8TO14_DAYS(BigDecimal r17_8to14_DAYS) {
	R17_8TO14_DAYS = r17_8to14_DAYS;
}








public BigDecimal getR17_15TO30_DAYS() {
	return R17_15TO30_DAYS;
}








public void setR17_15TO30_DAYS(BigDecimal r17_15to30_DAYS) {
	R17_15TO30_DAYS = r17_15to30_DAYS;
}








public BigDecimal getR17_31DAYS_UPTO_2MONTHS() {
	return R17_31DAYS_UPTO_2MONTHS;
}








public void setR17_31DAYS_UPTO_2MONTHS(BigDecimal r17_31days_UPTO_2MONTHS) {
	R17_31DAYS_UPTO_2MONTHS = r17_31days_UPTO_2MONTHS;
}








public BigDecimal getR17_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R17_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR17_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r17_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R17_MORETHAN_2MONTHS_UPTO_3MONHTS = r17_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR17_OVER_3MONTHS_UPTO_6MONTHS() {
	return R17_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR17_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r17_OVER_3MONTHS_UPTO_6MONTHS) {
	R17_OVER_3MONTHS_UPTO_6MONTHS = r17_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR17_OVER_6MONTHS_UPTO_1YEAR() {
	return R17_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR17_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r17_OVER_6MONTHS_UPTO_1YEAR) {
	R17_OVER_6MONTHS_UPTO_1YEAR = r17_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR17_OVER_1YEAR_UPTO_3YEARS() {
	return R17_OVER_1YEAR_UPTO_3YEARS;
}








public void setR17_OVER_1YEAR_UPTO_3YEARS(BigDecimal r17_OVER_1YEAR_UPTO_3YEARS) {
	R17_OVER_1YEAR_UPTO_3YEARS = r17_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR17_OVER_3YEARS_UPTO_5YEARS() {
	return R17_OVER_3YEARS_UPTO_5YEARS;
}








public void setR17_OVER_3YEARS_UPTO_5YEARS(BigDecimal r17_OVER_3YEARS_UPTO_5YEARS) {
	R17_OVER_3YEARS_UPTO_5YEARS = r17_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR17_OVER_5YEARS() {
	return R17_OVER_5YEARS;
}








public void setR17_OVER_5YEARS(BigDecimal r17_OVER_5YEARS) {
	R17_OVER_5YEARS = r17_OVER_5YEARS;
}








public BigDecimal getR17_TOTAL() {
	return R17_TOTAL;
}








public void setR17_TOTAL(BigDecimal r17_TOTAL) {
	R17_TOTAL = r17_TOTAL;
}








public BigDecimal getR18_1_DAY() {
	return R18_1_DAY;
}








public void setR18_1_DAY(BigDecimal r18_1_DAY) {
	R18_1_DAY = r18_1_DAY;
}








public BigDecimal getR18_2TO7_DAYS() {
	return R18_2TO7_DAYS;
}








public void setR18_2TO7_DAYS(BigDecimal r18_2to7_DAYS) {
	R18_2TO7_DAYS = r18_2to7_DAYS;
}








public BigDecimal getR18_8TO14_DAYS() {
	return R18_8TO14_DAYS;
}








public void setR18_8TO14_DAYS(BigDecimal r18_8to14_DAYS) {
	R18_8TO14_DAYS = r18_8to14_DAYS;
}








public BigDecimal getR18_15TO30_DAYS() {
	return R18_15TO30_DAYS;
}








public void setR18_15TO30_DAYS(BigDecimal r18_15to30_DAYS) {
	R18_15TO30_DAYS = r18_15to30_DAYS;
}








public BigDecimal getR18_31DAYS_UPTO_2MONTHS() {
	return R18_31DAYS_UPTO_2MONTHS;
}








public void setR18_31DAYS_UPTO_2MONTHS(BigDecimal r18_31days_UPTO_2MONTHS) {
	R18_31DAYS_UPTO_2MONTHS = r18_31days_UPTO_2MONTHS;
}








public BigDecimal getR18_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R18_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR18_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r18_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R18_MORETHAN_2MONTHS_UPTO_3MONHTS = r18_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR18_OVER_3MONTHS_UPTO_6MONTHS() {
	return R18_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR18_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r18_OVER_3MONTHS_UPTO_6MONTHS) {
	R18_OVER_3MONTHS_UPTO_6MONTHS = r18_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR18_OVER_6MONTHS_UPTO_1YEAR() {
	return R18_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR18_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r18_OVER_6MONTHS_UPTO_1YEAR) {
	R18_OVER_6MONTHS_UPTO_1YEAR = r18_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR18_OVER_1YEAR_UPTO_3YEARS() {
	return R18_OVER_1YEAR_UPTO_3YEARS;
}








public void setR18_OVER_1YEAR_UPTO_3YEARS(BigDecimal r18_OVER_1YEAR_UPTO_3YEARS) {
	R18_OVER_1YEAR_UPTO_3YEARS = r18_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR18_OVER_3YEARS_UPTO_5YEARS() {
	return R18_OVER_3YEARS_UPTO_5YEARS;
}








public void setR18_OVER_3YEARS_UPTO_5YEARS(BigDecimal r18_OVER_3YEARS_UPTO_5YEARS) {
	R18_OVER_3YEARS_UPTO_5YEARS = r18_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR18_OVER_5YEARS() {
	return R18_OVER_5YEARS;
}








public void setR18_OVER_5YEARS(BigDecimal r18_OVER_5YEARS) {
	R18_OVER_5YEARS = r18_OVER_5YEARS;
}








public BigDecimal getR18_TOTAL() {
	return R18_TOTAL;
}








public void setR18_TOTAL(BigDecimal r18_TOTAL) {
	R18_TOTAL = r18_TOTAL;
}








public BigDecimal getR19_1_DAY() {
	return R19_1_DAY;
}








public void setR19_1_DAY(BigDecimal r19_1_DAY) {
	R19_1_DAY = r19_1_DAY;
}








public BigDecimal getR19_2TO7_DAYS() {
	return R19_2TO7_DAYS;
}








public void setR19_2TO7_DAYS(BigDecimal r19_2to7_DAYS) {
	R19_2TO7_DAYS = r19_2to7_DAYS;
}








public BigDecimal getR19_8TO14_DAYS() {
	return R19_8TO14_DAYS;
}








public void setR19_8TO14_DAYS(BigDecimal r19_8to14_DAYS) {
	R19_8TO14_DAYS = r19_8to14_DAYS;
}








public BigDecimal getR19_15TO30_DAYS() {
	return R19_15TO30_DAYS;
}








public void setR19_15TO30_DAYS(BigDecimal r19_15to30_DAYS) {
	R19_15TO30_DAYS = r19_15to30_DAYS;
}








public BigDecimal getR19_31DAYS_UPTO_2MONTHS() {
	return R19_31DAYS_UPTO_2MONTHS;
}








public void setR19_31DAYS_UPTO_2MONTHS(BigDecimal r19_31days_UPTO_2MONTHS) {
	R19_31DAYS_UPTO_2MONTHS = r19_31days_UPTO_2MONTHS;
}








public BigDecimal getR19_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R19_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR19_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r19_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R19_MORETHAN_2MONTHS_UPTO_3MONHTS = r19_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR19_OVER_3MONTHS_UPTO_6MONTHS() {
	return R19_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR19_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r19_OVER_3MONTHS_UPTO_6MONTHS) {
	R19_OVER_3MONTHS_UPTO_6MONTHS = r19_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR19_OVER_6MONTHS_UPTO_1YEAR() {
	return R19_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR19_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r19_OVER_6MONTHS_UPTO_1YEAR) {
	R19_OVER_6MONTHS_UPTO_1YEAR = r19_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR19_OVER_1YEAR_UPTO_3YEARS() {
	return R19_OVER_1YEAR_UPTO_3YEARS;
}








public void setR19_OVER_1YEAR_UPTO_3YEARS(BigDecimal r19_OVER_1YEAR_UPTO_3YEARS) {
	R19_OVER_1YEAR_UPTO_3YEARS = r19_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR19_OVER_3YEARS_UPTO_5YEARS() {
	return R19_OVER_3YEARS_UPTO_5YEARS;
}








public void setR19_OVER_3YEARS_UPTO_5YEARS(BigDecimal r19_OVER_3YEARS_UPTO_5YEARS) {
	R19_OVER_3YEARS_UPTO_5YEARS = r19_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR19_OVER_5YEARS() {
	return R19_OVER_5YEARS;
}








public void setR19_OVER_5YEARS(BigDecimal r19_OVER_5YEARS) {
	R19_OVER_5YEARS = r19_OVER_5YEARS;
}








public BigDecimal getR19_TOTAL() {
	return R19_TOTAL;
}








public void setR19_TOTAL(BigDecimal r19_TOTAL) {
	R19_TOTAL = r19_TOTAL;
}








public BigDecimal getR20_1_DAY() {
	return R20_1_DAY;
}








public void setR20_1_DAY(BigDecimal r20_1_DAY) {
	R20_1_DAY = r20_1_DAY;
}








public BigDecimal getR20_2TO7_DAYS() {
	return R20_2TO7_DAYS;
}








public void setR20_2TO7_DAYS(BigDecimal r20_2to7_DAYS) {
	R20_2TO7_DAYS = r20_2to7_DAYS;
}








public BigDecimal getR20_8TO14_DAYS() {
	return R20_8TO14_DAYS;
}








public void setR20_8TO14_DAYS(BigDecimal r20_8to14_DAYS) {
	R20_8TO14_DAYS = r20_8to14_DAYS;
}








public BigDecimal getR20_15TO30_DAYS() {
	return R20_15TO30_DAYS;
}








public void setR20_15TO30_DAYS(BigDecimal r20_15to30_DAYS) {
	R20_15TO30_DAYS = r20_15to30_DAYS;
}








public BigDecimal getR20_31DAYS_UPTO_2MONTHS() {
	return R20_31DAYS_UPTO_2MONTHS;
}








public void setR20_31DAYS_UPTO_2MONTHS(BigDecimal r20_31days_UPTO_2MONTHS) {
	R20_31DAYS_UPTO_2MONTHS = r20_31days_UPTO_2MONTHS;
}








public BigDecimal getR20_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R20_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR20_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r20_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R20_MORETHAN_2MONTHS_UPTO_3MONHTS = r20_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR20_OVER_3MONTHS_UPTO_6MONTHS() {
	return R20_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR20_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r20_OVER_3MONTHS_UPTO_6MONTHS) {
	R20_OVER_3MONTHS_UPTO_6MONTHS = r20_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR20_OVER_6MONTHS_UPTO_1YEAR() {
	return R20_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR20_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r20_OVER_6MONTHS_UPTO_1YEAR) {
	R20_OVER_6MONTHS_UPTO_1YEAR = r20_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR20_OVER_1YEAR_UPTO_3YEARS() {
	return R20_OVER_1YEAR_UPTO_3YEARS;
}








public void setR20_OVER_1YEAR_UPTO_3YEARS(BigDecimal r20_OVER_1YEAR_UPTO_3YEARS) {
	R20_OVER_1YEAR_UPTO_3YEARS = r20_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR20_OVER_3YEARS_UPTO_5YEARS() {
	return R20_OVER_3YEARS_UPTO_5YEARS;
}








public void setR20_OVER_3YEARS_UPTO_5YEARS(BigDecimal r20_OVER_3YEARS_UPTO_5YEARS) {
	R20_OVER_3YEARS_UPTO_5YEARS = r20_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR20_OVER_5YEARS() {
	return R20_OVER_5YEARS;
}








public void setR20_OVER_5YEARS(BigDecimal r20_OVER_5YEARS) {
	R20_OVER_5YEARS = r20_OVER_5YEARS;
}








public BigDecimal getR20_TOTAL() {
	return R20_TOTAL;
}








public void setR20_TOTAL(BigDecimal r20_TOTAL) {
	R20_TOTAL = r20_TOTAL;
}








public BigDecimal getR21_1_DAY() {
	return R21_1_DAY;
}








public void setR21_1_DAY(BigDecimal r21_1_DAY) {
	R21_1_DAY = r21_1_DAY;
}








public BigDecimal getR21_2TO7_DAYS() {
	return R21_2TO7_DAYS;
}








public void setR21_2TO7_DAYS(BigDecimal r21_2to7_DAYS) {
	R21_2TO7_DAYS = r21_2to7_DAYS;
}








public BigDecimal getR21_8TO14_DAYS() {
	return R21_8TO14_DAYS;
}








public void setR21_8TO14_DAYS(BigDecimal r21_8to14_DAYS) {
	R21_8TO14_DAYS = r21_8to14_DAYS;
}








public BigDecimal getR21_15TO30_DAYS() {
	return R21_15TO30_DAYS;
}








public void setR21_15TO30_DAYS(BigDecimal r21_15to30_DAYS) {
	R21_15TO30_DAYS = r21_15to30_DAYS;
}








public BigDecimal getR21_31DAYS_UPTO_2MONTHS() {
	return R21_31DAYS_UPTO_2MONTHS;
}








public void setR21_31DAYS_UPTO_2MONTHS(BigDecimal r21_31days_UPTO_2MONTHS) {
	R21_31DAYS_UPTO_2MONTHS = r21_31days_UPTO_2MONTHS;
}








public BigDecimal getR21_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R21_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR21_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r21_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R21_MORETHAN_2MONTHS_UPTO_3MONHTS = r21_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR21_OVER_3MONTHS_UPTO_6MONTHS() {
	return R21_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR21_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r21_OVER_3MONTHS_UPTO_6MONTHS) {
	R21_OVER_3MONTHS_UPTO_6MONTHS = r21_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR21_OVER_6MONTHS_UPTO_1YEAR() {
	return R21_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR21_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r21_OVER_6MONTHS_UPTO_1YEAR) {
	R21_OVER_6MONTHS_UPTO_1YEAR = r21_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR21_OVER_1YEAR_UPTO_3YEARS() {
	return R21_OVER_1YEAR_UPTO_3YEARS;
}








public void setR21_OVER_1YEAR_UPTO_3YEARS(BigDecimal r21_OVER_1YEAR_UPTO_3YEARS) {
	R21_OVER_1YEAR_UPTO_3YEARS = r21_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR21_OVER_3YEARS_UPTO_5YEARS() {
	return R21_OVER_3YEARS_UPTO_5YEARS;
}








public void setR21_OVER_3YEARS_UPTO_5YEARS(BigDecimal r21_OVER_3YEARS_UPTO_5YEARS) {
	R21_OVER_3YEARS_UPTO_5YEARS = r21_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR21_OVER_5YEARS() {
	return R21_OVER_5YEARS;
}








public void setR21_OVER_5YEARS(BigDecimal r21_OVER_5YEARS) {
	R21_OVER_5YEARS = r21_OVER_5YEARS;
}








public BigDecimal getR21_TOTAL() {
	return R21_TOTAL;
}








public void setR21_TOTAL(BigDecimal r21_TOTAL) {
	R21_TOTAL = r21_TOTAL;
}








public BigDecimal getR22_1_DAY() {
	return R22_1_DAY;
}








public void setR22_1_DAY(BigDecimal r22_1_DAY) {
	R22_1_DAY = r22_1_DAY;
}








public BigDecimal getR22_2TO7_DAYS() {
	return R22_2TO7_DAYS;
}








public void setR22_2TO7_DAYS(BigDecimal r22_2to7_DAYS) {
	R22_2TO7_DAYS = r22_2to7_DAYS;
}








public BigDecimal getR22_8TO14_DAYS() {
	return R22_8TO14_DAYS;
}








public void setR22_8TO14_DAYS(BigDecimal r22_8to14_DAYS) {
	R22_8TO14_DAYS = r22_8to14_DAYS;
}








public BigDecimal getR22_15TO30_DAYS() {
	return R22_15TO30_DAYS;
}








public void setR22_15TO30_DAYS(BigDecimal r22_15to30_DAYS) {
	R22_15TO30_DAYS = r22_15to30_DAYS;
}








public BigDecimal getR22_31DAYS_UPTO_2MONTHS() {
	return R22_31DAYS_UPTO_2MONTHS;
}








public void setR22_31DAYS_UPTO_2MONTHS(BigDecimal r22_31days_UPTO_2MONTHS) {
	R22_31DAYS_UPTO_2MONTHS = r22_31days_UPTO_2MONTHS;
}








public BigDecimal getR22_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R22_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR22_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r22_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R22_MORETHAN_2MONTHS_UPTO_3MONHTS = r22_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR22_OVER_3MONTHS_UPTO_6MONTHS() {
	return R22_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR22_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r22_OVER_3MONTHS_UPTO_6MONTHS) {
	R22_OVER_3MONTHS_UPTO_6MONTHS = r22_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR22_OVER_6MONTHS_UPTO_1YEAR() {
	return R22_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR22_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r22_OVER_6MONTHS_UPTO_1YEAR) {
	R22_OVER_6MONTHS_UPTO_1YEAR = r22_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR22_OVER_1YEAR_UPTO_3YEARS() {
	return R22_OVER_1YEAR_UPTO_3YEARS;
}








public void setR22_OVER_1YEAR_UPTO_3YEARS(BigDecimal r22_OVER_1YEAR_UPTO_3YEARS) {
	R22_OVER_1YEAR_UPTO_3YEARS = r22_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR22_OVER_3YEARS_UPTO_5YEARS() {
	return R22_OVER_3YEARS_UPTO_5YEARS;
}








public void setR22_OVER_3YEARS_UPTO_5YEARS(BigDecimal r22_OVER_3YEARS_UPTO_5YEARS) {
	R22_OVER_3YEARS_UPTO_5YEARS = r22_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR22_OVER_5YEARS() {
	return R22_OVER_5YEARS;
}








public void setR22_OVER_5YEARS(BigDecimal r22_OVER_5YEARS) {
	R22_OVER_5YEARS = r22_OVER_5YEARS;
}








public BigDecimal getR22_TOTAL() {
	return R22_TOTAL;
}








public void setR22_TOTAL(BigDecimal r22_TOTAL) {
	R22_TOTAL = r22_TOTAL;
}








public BigDecimal getR23_1_DAY() {
	return R23_1_DAY;
}








public void setR23_1_DAY(BigDecimal r23_1_DAY) {
	R23_1_DAY = r23_1_DAY;
}








public BigDecimal getR23_2TO7_DAYS() {
	return R23_2TO7_DAYS;
}








public void setR23_2TO7_DAYS(BigDecimal r23_2to7_DAYS) {
	R23_2TO7_DAYS = r23_2to7_DAYS;
}








public BigDecimal getR23_8TO14_DAYS() {
	return R23_8TO14_DAYS;
}








public void setR23_8TO14_DAYS(BigDecimal r23_8to14_DAYS) {
	R23_8TO14_DAYS = r23_8to14_DAYS;
}








public BigDecimal getR23_15TO30_DAYS() {
	return R23_15TO30_DAYS;
}








public void setR23_15TO30_DAYS(BigDecimal r23_15to30_DAYS) {
	R23_15TO30_DAYS = r23_15to30_DAYS;
}








public BigDecimal getR23_31DAYS_UPTO_2MONTHS() {
	return R23_31DAYS_UPTO_2MONTHS;
}








public void setR23_31DAYS_UPTO_2MONTHS(BigDecimal r23_31days_UPTO_2MONTHS) {
	R23_31DAYS_UPTO_2MONTHS = r23_31days_UPTO_2MONTHS;
}








public BigDecimal getR23_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R23_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR23_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r23_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R23_MORETHAN_2MONTHS_UPTO_3MONHTS = r23_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR23_OVER_3MONTHS_UPTO_6MONTHS() {
	return R23_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR23_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r23_OVER_3MONTHS_UPTO_6MONTHS) {
	R23_OVER_3MONTHS_UPTO_6MONTHS = r23_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR23_OVER_6MONTHS_UPTO_1YEAR() {
	return R23_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR23_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r23_OVER_6MONTHS_UPTO_1YEAR) {
	R23_OVER_6MONTHS_UPTO_1YEAR = r23_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR23_OVER_1YEAR_UPTO_3YEARS() {
	return R23_OVER_1YEAR_UPTO_3YEARS;
}








public void setR23_OVER_1YEAR_UPTO_3YEARS(BigDecimal r23_OVER_1YEAR_UPTO_3YEARS) {
	R23_OVER_1YEAR_UPTO_3YEARS = r23_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR23_OVER_3YEARS_UPTO_5YEARS() {
	return R23_OVER_3YEARS_UPTO_5YEARS;
}








public void setR23_OVER_3YEARS_UPTO_5YEARS(BigDecimal r23_OVER_3YEARS_UPTO_5YEARS) {
	R23_OVER_3YEARS_UPTO_5YEARS = r23_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR23_OVER_5YEARS() {
	return R23_OVER_5YEARS;
}








public void setR23_OVER_5YEARS(BigDecimal r23_OVER_5YEARS) {
	R23_OVER_5YEARS = r23_OVER_5YEARS;
}








public BigDecimal getR23_TOTAL() {
	return R23_TOTAL;
}








public void setR23_TOTAL(BigDecimal r23_TOTAL) {
	R23_TOTAL = r23_TOTAL;
}








public BigDecimal getR24_1_DAY() {
	return R24_1_DAY;
}








public void setR24_1_DAY(BigDecimal r24_1_DAY) {
	R24_1_DAY = r24_1_DAY;
}








public BigDecimal getR24_2TO7_DAYS() {
	return R24_2TO7_DAYS;
}








public void setR24_2TO7_DAYS(BigDecimal r24_2to7_DAYS) {
	R24_2TO7_DAYS = r24_2to7_DAYS;
}








public BigDecimal getR24_8TO14_DAYS() {
	return R24_8TO14_DAYS;
}








public void setR24_8TO14_DAYS(BigDecimal r24_8to14_DAYS) {
	R24_8TO14_DAYS = r24_8to14_DAYS;
}








public BigDecimal getR24_15TO30_DAYS() {
	return R24_15TO30_DAYS;
}








public void setR24_15TO30_DAYS(BigDecimal r24_15to30_DAYS) {
	R24_15TO30_DAYS = r24_15to30_DAYS;
}








public BigDecimal getR24_31DAYS_UPTO_2MONTHS() {
	return R24_31DAYS_UPTO_2MONTHS;
}








public void setR24_31DAYS_UPTO_2MONTHS(BigDecimal r24_31days_UPTO_2MONTHS) {
	R24_31DAYS_UPTO_2MONTHS = r24_31days_UPTO_2MONTHS;
}








public BigDecimal getR24_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R24_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR24_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r24_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R24_MORETHAN_2MONTHS_UPTO_3MONHTS = r24_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR24_OVER_3MONTHS_UPTO_6MONTHS() {
	return R24_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR24_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r24_OVER_3MONTHS_UPTO_6MONTHS) {
	R24_OVER_3MONTHS_UPTO_6MONTHS = r24_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR24_OVER_6MONTHS_UPTO_1YEAR() {
	return R24_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR24_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r24_OVER_6MONTHS_UPTO_1YEAR) {
	R24_OVER_6MONTHS_UPTO_1YEAR = r24_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR24_OVER_1YEAR_UPTO_3YEARS() {
	return R24_OVER_1YEAR_UPTO_3YEARS;
}








public void setR24_OVER_1YEAR_UPTO_3YEARS(BigDecimal r24_OVER_1YEAR_UPTO_3YEARS) {
	R24_OVER_1YEAR_UPTO_3YEARS = r24_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR24_OVER_3YEARS_UPTO_5YEARS() {
	return R24_OVER_3YEARS_UPTO_5YEARS;
}








public void setR24_OVER_3YEARS_UPTO_5YEARS(BigDecimal r24_OVER_3YEARS_UPTO_5YEARS) {
	R24_OVER_3YEARS_UPTO_5YEARS = r24_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR24_OVER_5YEARS() {
	return R24_OVER_5YEARS;
}








public void setR24_OVER_5YEARS(BigDecimal r24_OVER_5YEARS) {
	R24_OVER_5YEARS = r24_OVER_5YEARS;
}








public BigDecimal getR24_TOTAL() {
	return R24_TOTAL;
}








public void setR24_TOTAL(BigDecimal r24_TOTAL) {
	R24_TOTAL = r24_TOTAL;
}








public BigDecimal getR25_1_DAY() {
	return R25_1_DAY;
}








public void setR25_1_DAY(BigDecimal r25_1_DAY) {
	R25_1_DAY = r25_1_DAY;
}








public BigDecimal getR25_2TO7_DAYS() {
	return R25_2TO7_DAYS;
}








public void setR25_2TO7_DAYS(BigDecimal r25_2to7_DAYS) {
	R25_2TO7_DAYS = r25_2to7_DAYS;
}








public BigDecimal getR25_8TO14_DAYS() {
	return R25_8TO14_DAYS;
}








public void setR25_8TO14_DAYS(BigDecimal r25_8to14_DAYS) {
	R25_8TO14_DAYS = r25_8to14_DAYS;
}








public BigDecimal getR25_15TO30_DAYS() {
	return R25_15TO30_DAYS;
}








public void setR25_15TO30_DAYS(BigDecimal r25_15to30_DAYS) {
	R25_15TO30_DAYS = r25_15to30_DAYS;
}








public BigDecimal getR25_31DAYS_UPTO_2MONTHS() {
	return R25_31DAYS_UPTO_2MONTHS;
}








public void setR25_31DAYS_UPTO_2MONTHS(BigDecimal r25_31days_UPTO_2MONTHS) {
	R25_31DAYS_UPTO_2MONTHS = r25_31days_UPTO_2MONTHS;
}








public BigDecimal getR25_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R25_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR25_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r25_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R25_MORETHAN_2MONTHS_UPTO_3MONHTS = r25_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR25_OVER_3MONTHS_UPTO_6MONTHS() {
	return R25_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR25_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r25_OVER_3MONTHS_UPTO_6MONTHS) {
	R25_OVER_3MONTHS_UPTO_6MONTHS = r25_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR25_OVER_6MONTHS_UPTO_1YEAR() {
	return R25_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR25_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r25_OVER_6MONTHS_UPTO_1YEAR) {
	R25_OVER_6MONTHS_UPTO_1YEAR = r25_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR25_OVER_1YEAR_UPTO_3YEARS() {
	return R25_OVER_1YEAR_UPTO_3YEARS;
}








public void setR25_OVER_1YEAR_UPTO_3YEARS(BigDecimal r25_OVER_1YEAR_UPTO_3YEARS) {
	R25_OVER_1YEAR_UPTO_3YEARS = r25_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR25_OVER_3YEARS_UPTO_5YEARS() {
	return R25_OVER_3YEARS_UPTO_5YEARS;
}








public void setR25_OVER_3YEARS_UPTO_5YEARS(BigDecimal r25_OVER_3YEARS_UPTO_5YEARS) {
	R25_OVER_3YEARS_UPTO_5YEARS = r25_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR25_OVER_5YEARS() {
	return R25_OVER_5YEARS;
}








public void setR25_OVER_5YEARS(BigDecimal r25_OVER_5YEARS) {
	R25_OVER_5YEARS = r25_OVER_5YEARS;
}








public BigDecimal getR25_TOTAL() {
	return R25_TOTAL;
}








public void setR25_TOTAL(BigDecimal r25_TOTAL) {
	R25_TOTAL = r25_TOTAL;
}








public BigDecimal getR26_1_DAY() {
	return R26_1_DAY;
}








public void setR26_1_DAY(BigDecimal r26_1_DAY) {
	R26_1_DAY = r26_1_DAY;
}








public BigDecimal getR26_2TO7_DAYS() {
	return R26_2TO7_DAYS;
}








public void setR26_2TO7_DAYS(BigDecimal r26_2to7_DAYS) {
	R26_2TO7_DAYS = r26_2to7_DAYS;
}








public BigDecimal getR26_8TO14_DAYS() {
	return R26_8TO14_DAYS;
}








public void setR26_8TO14_DAYS(BigDecimal r26_8to14_DAYS) {
	R26_8TO14_DAYS = r26_8to14_DAYS;
}








public BigDecimal getR26_15TO30_DAYS() {
	return R26_15TO30_DAYS;
}








public void setR26_15TO30_DAYS(BigDecimal r26_15to30_DAYS) {
	R26_15TO30_DAYS = r26_15to30_DAYS;
}








public BigDecimal getR26_31DAYS_UPTO_2MONTHS() {
	return R26_31DAYS_UPTO_2MONTHS;
}








public void setR26_31DAYS_UPTO_2MONTHS(BigDecimal r26_31days_UPTO_2MONTHS) {
	R26_31DAYS_UPTO_2MONTHS = r26_31days_UPTO_2MONTHS;
}








public BigDecimal getR26_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R26_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR26_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r26_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R26_MORETHAN_2MONTHS_UPTO_3MONHTS = r26_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR26_OVER_3MONTHS_UPTO_6MONTHS() {
	return R26_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR26_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r26_OVER_3MONTHS_UPTO_6MONTHS) {
	R26_OVER_3MONTHS_UPTO_6MONTHS = r26_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR26_OVER_6MONTHS_UPTO_1YEAR() {
	return R26_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR26_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r26_OVER_6MONTHS_UPTO_1YEAR) {
	R26_OVER_6MONTHS_UPTO_1YEAR = r26_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR26_OVER_1YEAR_UPTO_3YEARS() {
	return R26_OVER_1YEAR_UPTO_3YEARS;
}








public void setR26_OVER_1YEAR_UPTO_3YEARS(BigDecimal r26_OVER_1YEAR_UPTO_3YEARS) {
	R26_OVER_1YEAR_UPTO_3YEARS = r26_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR26_OVER_3YEARS_UPTO_5YEARS() {
	return R26_OVER_3YEARS_UPTO_5YEARS;
}








public void setR26_OVER_3YEARS_UPTO_5YEARS(BigDecimal r26_OVER_3YEARS_UPTO_5YEARS) {
	R26_OVER_3YEARS_UPTO_5YEARS = r26_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR26_OVER_5YEARS() {
	return R26_OVER_5YEARS;
}








public void setR26_OVER_5YEARS(BigDecimal r26_OVER_5YEARS) {
	R26_OVER_5YEARS = r26_OVER_5YEARS;
}








public BigDecimal getR26_TOTAL() {
	return R26_TOTAL;
}








public void setR26_TOTAL(BigDecimal r26_TOTAL) {
	R26_TOTAL = r26_TOTAL;
}








public BigDecimal getR27_1_DAY() {
	return R27_1_DAY;
}








public void setR27_1_DAY(BigDecimal r27_1_DAY) {
	R27_1_DAY = r27_1_DAY;
}








public BigDecimal getR27_2TO7_DAYS() {
	return R27_2TO7_DAYS;
}








public void setR27_2TO7_DAYS(BigDecimal r27_2to7_DAYS) {
	R27_2TO7_DAYS = r27_2to7_DAYS;
}








public BigDecimal getR27_8TO14_DAYS() {
	return R27_8TO14_DAYS;
}








public void setR27_8TO14_DAYS(BigDecimal r27_8to14_DAYS) {
	R27_8TO14_DAYS = r27_8to14_DAYS;
}








public BigDecimal getR27_15TO30_DAYS() {
	return R27_15TO30_DAYS;
}








public void setR27_15TO30_DAYS(BigDecimal r27_15to30_DAYS) {
	R27_15TO30_DAYS = r27_15to30_DAYS;
}








public BigDecimal getR27_31DAYS_UPTO_2MONTHS() {
	return R27_31DAYS_UPTO_2MONTHS;
}








public void setR27_31DAYS_UPTO_2MONTHS(BigDecimal r27_31days_UPTO_2MONTHS) {
	R27_31DAYS_UPTO_2MONTHS = r27_31days_UPTO_2MONTHS;
}








public BigDecimal getR27_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R27_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR27_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r27_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R27_MORETHAN_2MONTHS_UPTO_3MONHTS = r27_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR27_OVER_3MONTHS_UPTO_6MONTHS() {
	return R27_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR27_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r27_OVER_3MONTHS_UPTO_6MONTHS) {
	R27_OVER_3MONTHS_UPTO_6MONTHS = r27_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR27_OVER_6MONTHS_UPTO_1YEAR() {
	return R27_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR27_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r27_OVER_6MONTHS_UPTO_1YEAR) {
	R27_OVER_6MONTHS_UPTO_1YEAR = r27_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR27_OVER_1YEAR_UPTO_3YEARS() {
	return R27_OVER_1YEAR_UPTO_3YEARS;
}








public void setR27_OVER_1YEAR_UPTO_3YEARS(BigDecimal r27_OVER_1YEAR_UPTO_3YEARS) {
	R27_OVER_1YEAR_UPTO_3YEARS = r27_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR27_OVER_3YEARS_UPTO_5YEARS() {
	return R27_OVER_3YEARS_UPTO_5YEARS;
}








public void setR27_OVER_3YEARS_UPTO_5YEARS(BigDecimal r27_OVER_3YEARS_UPTO_5YEARS) {
	R27_OVER_3YEARS_UPTO_5YEARS = r27_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR27_OVER_5YEARS() {
	return R27_OVER_5YEARS;
}








public void setR27_OVER_5YEARS(BigDecimal r27_OVER_5YEARS) {
	R27_OVER_5YEARS = r27_OVER_5YEARS;
}








public BigDecimal getR27_TOTAL() {
	return R27_TOTAL;
}








public void setR27_TOTAL(BigDecimal r27_TOTAL) {
	R27_TOTAL = r27_TOTAL;
}








public BigDecimal getR28_1_DAY() {
	return R28_1_DAY;
}








public void setR28_1_DAY(BigDecimal r28_1_DAY) {
	R28_1_DAY = r28_1_DAY;
}








public BigDecimal getR28_2TO7_DAYS() {
	return R28_2TO7_DAYS;
}








public void setR28_2TO7_DAYS(BigDecimal r28_2to7_DAYS) {
	R28_2TO7_DAYS = r28_2to7_DAYS;
}








public BigDecimal getR28_8TO14_DAYS() {
	return R28_8TO14_DAYS;
}








public void setR28_8TO14_DAYS(BigDecimal r28_8to14_DAYS) {
	R28_8TO14_DAYS = r28_8to14_DAYS;
}








public BigDecimal getR28_15TO30_DAYS() {
	return R28_15TO30_DAYS;
}








public void setR28_15TO30_DAYS(BigDecimal r28_15to30_DAYS) {
	R28_15TO30_DAYS = r28_15to30_DAYS;
}








public BigDecimal getR28_31DAYS_UPTO_2MONTHS() {
	return R28_31DAYS_UPTO_2MONTHS;
}








public void setR28_31DAYS_UPTO_2MONTHS(BigDecimal r28_31days_UPTO_2MONTHS) {
	R28_31DAYS_UPTO_2MONTHS = r28_31days_UPTO_2MONTHS;
}








public BigDecimal getR28_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R28_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR28_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r28_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R28_MORETHAN_2MONTHS_UPTO_3MONHTS = r28_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR28_OVER_3MONTHS_UPTO_6MONTHS() {
	return R28_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR28_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r28_OVER_3MONTHS_UPTO_6MONTHS) {
	R28_OVER_3MONTHS_UPTO_6MONTHS = r28_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR28_OVER_6MONTHS_UPTO_1YEAR() {
	return R28_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR28_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r28_OVER_6MONTHS_UPTO_1YEAR) {
	R28_OVER_6MONTHS_UPTO_1YEAR = r28_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR28_OVER_1YEAR_UPTO_3YEARS() {
	return R28_OVER_1YEAR_UPTO_3YEARS;
}








public void setR28_OVER_1YEAR_UPTO_3YEARS(BigDecimal r28_OVER_1YEAR_UPTO_3YEARS) {
	R28_OVER_1YEAR_UPTO_3YEARS = r28_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR28_OVER_3YEARS_UPTO_5YEARS() {
	return R28_OVER_3YEARS_UPTO_5YEARS;
}








public void setR28_OVER_3YEARS_UPTO_5YEARS(BigDecimal r28_OVER_3YEARS_UPTO_5YEARS) {
	R28_OVER_3YEARS_UPTO_5YEARS = r28_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR28_OVER_5YEARS() {
	return R28_OVER_5YEARS;
}








public void setR28_OVER_5YEARS(BigDecimal r28_OVER_5YEARS) {
	R28_OVER_5YEARS = r28_OVER_5YEARS;
}








public BigDecimal getR28_TOTAL() {
	return R28_TOTAL;
}








public void setR28_TOTAL(BigDecimal r28_TOTAL) {
	R28_TOTAL = r28_TOTAL;
}








public BigDecimal getR29_1_DAY() {
	return R29_1_DAY;
}








public void setR29_1_DAY(BigDecimal r29_1_DAY) {
	R29_1_DAY = r29_1_DAY;
}








public BigDecimal getR29_2TO7_DAYS() {
	return R29_2TO7_DAYS;
}








public void setR29_2TO7_DAYS(BigDecimal r29_2to7_DAYS) {
	R29_2TO7_DAYS = r29_2to7_DAYS;
}








public BigDecimal getR29_8TO14_DAYS() {
	return R29_8TO14_DAYS;
}








public void setR29_8TO14_DAYS(BigDecimal r29_8to14_DAYS) {
	R29_8TO14_DAYS = r29_8to14_DAYS;
}








public BigDecimal getR29_15TO30_DAYS() {
	return R29_15TO30_DAYS;
}








public void setR29_15TO30_DAYS(BigDecimal r29_15to30_DAYS) {
	R29_15TO30_DAYS = r29_15to30_DAYS;
}








public BigDecimal getR29_31DAYS_UPTO_2MONTHS() {
	return R29_31DAYS_UPTO_2MONTHS;
}








public void setR29_31DAYS_UPTO_2MONTHS(BigDecimal r29_31days_UPTO_2MONTHS) {
	R29_31DAYS_UPTO_2MONTHS = r29_31days_UPTO_2MONTHS;
}








public BigDecimal getR29_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R29_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR29_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r29_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R29_MORETHAN_2MONTHS_UPTO_3MONHTS = r29_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR29_OVER_3MONTHS_UPTO_6MONTHS() {
	return R29_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR29_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r29_OVER_3MONTHS_UPTO_6MONTHS) {
	R29_OVER_3MONTHS_UPTO_6MONTHS = r29_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR29_OVER_6MONTHS_UPTO_1YEAR() {
	return R29_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR29_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r29_OVER_6MONTHS_UPTO_1YEAR) {
	R29_OVER_6MONTHS_UPTO_1YEAR = r29_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR29_OVER_1YEAR_UPTO_3YEARS() {
	return R29_OVER_1YEAR_UPTO_3YEARS;
}








public void setR29_OVER_1YEAR_UPTO_3YEARS(BigDecimal r29_OVER_1YEAR_UPTO_3YEARS) {
	R29_OVER_1YEAR_UPTO_3YEARS = r29_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR29_OVER_3YEARS_UPTO_5YEARS() {
	return R29_OVER_3YEARS_UPTO_5YEARS;
}








public void setR29_OVER_3YEARS_UPTO_5YEARS(BigDecimal r29_OVER_3YEARS_UPTO_5YEARS) {
	R29_OVER_3YEARS_UPTO_5YEARS = r29_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR29_OVER_5YEARS() {
	return R29_OVER_5YEARS;
}








public void setR29_OVER_5YEARS(BigDecimal r29_OVER_5YEARS) {
	R29_OVER_5YEARS = r29_OVER_5YEARS;
}








public BigDecimal getR29_TOTAL() {
	return R29_TOTAL;
}








public void setR29_TOTAL(BigDecimal r29_TOTAL) {
	R29_TOTAL = r29_TOTAL;
}








public BigDecimal getR30_1_DAY() {
	return R30_1_DAY;
}








public void setR30_1_DAY(BigDecimal r30_1_DAY) {
	R30_1_DAY = r30_1_DAY;
}








public BigDecimal getR30_2TO7_DAYS() {
	return R30_2TO7_DAYS;
}








public void setR30_2TO7_DAYS(BigDecimal r30_2to7_DAYS) {
	R30_2TO7_DAYS = r30_2to7_DAYS;
}








public BigDecimal getR30_8TO14_DAYS() {
	return R30_8TO14_DAYS;
}








public void setR30_8TO14_DAYS(BigDecimal r30_8to14_DAYS) {
	R30_8TO14_DAYS = r30_8to14_DAYS;
}








public BigDecimal getR30_15TO30_DAYS() {
	return R30_15TO30_DAYS;
}








public void setR30_15TO30_DAYS(BigDecimal r30_15to30_DAYS) {
	R30_15TO30_DAYS = r30_15to30_DAYS;
}








public BigDecimal getR30_31DAYS_UPTO_2MONTHS() {
	return R30_31DAYS_UPTO_2MONTHS;
}








public void setR30_31DAYS_UPTO_2MONTHS(BigDecimal r30_31days_UPTO_2MONTHS) {
	R30_31DAYS_UPTO_2MONTHS = r30_31days_UPTO_2MONTHS;
}








public BigDecimal getR30_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R30_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR30_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r30_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R30_MORETHAN_2MONTHS_UPTO_3MONHTS = r30_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR30_OVER_3MONTHS_UPTO_6MONTHS() {
	return R30_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR30_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r30_OVER_3MONTHS_UPTO_6MONTHS) {
	R30_OVER_3MONTHS_UPTO_6MONTHS = r30_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR30_OVER_6MONTHS_UPTO_1YEAR() {
	return R30_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR30_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r30_OVER_6MONTHS_UPTO_1YEAR) {
	R30_OVER_6MONTHS_UPTO_1YEAR = r30_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR30_OVER_1YEAR_UPTO_3YEARS() {
	return R30_OVER_1YEAR_UPTO_3YEARS;
}








public void setR30_OVER_1YEAR_UPTO_3YEARS(BigDecimal r30_OVER_1YEAR_UPTO_3YEARS) {
	R30_OVER_1YEAR_UPTO_3YEARS = r30_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR30_OVER_3YEARS_UPTO_5YEARS() {
	return R30_OVER_3YEARS_UPTO_5YEARS;
}








public void setR30_OVER_3YEARS_UPTO_5YEARS(BigDecimal r30_OVER_3YEARS_UPTO_5YEARS) {
	R30_OVER_3YEARS_UPTO_5YEARS = r30_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR30_OVER_5YEARS() {
	return R30_OVER_5YEARS;
}








public void setR30_OVER_5YEARS(BigDecimal r30_OVER_5YEARS) {
	R30_OVER_5YEARS = r30_OVER_5YEARS;
}








public BigDecimal getR30_TOTAL() {
	return R30_TOTAL;
}








public void setR30_TOTAL(BigDecimal r30_TOTAL) {
	R30_TOTAL = r30_TOTAL;
}








public BigDecimal getR31_1_DAY() {
	return R31_1_DAY;
}








public void setR31_1_DAY(BigDecimal r31_1_DAY) {
	R31_1_DAY = r31_1_DAY;
}








public BigDecimal getR31_2TO7_DAYS() {
	return R31_2TO7_DAYS;
}








public void setR31_2TO7_DAYS(BigDecimal r31_2to7_DAYS) {
	R31_2TO7_DAYS = r31_2to7_DAYS;
}








public BigDecimal getR31_8TO14_DAYS() {
	return R31_8TO14_DAYS;
}








public void setR31_8TO14_DAYS(BigDecimal r31_8to14_DAYS) {
	R31_8TO14_DAYS = r31_8to14_DAYS;
}








public BigDecimal getR31_15TO30_DAYS() {
	return R31_15TO30_DAYS;
}








public void setR31_15TO30_DAYS(BigDecimal r31_15to30_DAYS) {
	R31_15TO30_DAYS = r31_15to30_DAYS;
}








public BigDecimal getR31_31DAYS_UPTO_2MONTHS() {
	return R31_31DAYS_UPTO_2MONTHS;
}








public void setR31_31DAYS_UPTO_2MONTHS(BigDecimal r31_31days_UPTO_2MONTHS) {
	R31_31DAYS_UPTO_2MONTHS = r31_31days_UPTO_2MONTHS;
}








public BigDecimal getR31_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R31_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR31_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r31_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R31_MORETHAN_2MONTHS_UPTO_3MONHTS = r31_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR31_OVER_3MONTHS_UPTO_6MONTHS() {
	return R31_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR31_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r31_OVER_3MONTHS_UPTO_6MONTHS) {
	R31_OVER_3MONTHS_UPTO_6MONTHS = r31_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR31_OVER_6MONTHS_UPTO_1YEAR() {
	return R31_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR31_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r31_OVER_6MONTHS_UPTO_1YEAR) {
	R31_OVER_6MONTHS_UPTO_1YEAR = r31_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR31_OVER_1YEAR_UPTO_3YEARS() {
	return R31_OVER_1YEAR_UPTO_3YEARS;
}








public void setR31_OVER_1YEAR_UPTO_3YEARS(BigDecimal r31_OVER_1YEAR_UPTO_3YEARS) {
	R31_OVER_1YEAR_UPTO_3YEARS = r31_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR31_OVER_3YEARS_UPTO_5YEARS() {
	return R31_OVER_3YEARS_UPTO_5YEARS;
}








public void setR31_OVER_3YEARS_UPTO_5YEARS(BigDecimal r31_OVER_3YEARS_UPTO_5YEARS) {
	R31_OVER_3YEARS_UPTO_5YEARS = r31_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR31_OVER_5YEARS() {
	return R31_OVER_5YEARS;
}








public void setR31_OVER_5YEARS(BigDecimal r31_OVER_5YEARS) {
	R31_OVER_5YEARS = r31_OVER_5YEARS;
}








public BigDecimal getR31_TOTAL() {
	return R31_TOTAL;
}








public void setR31_TOTAL(BigDecimal r31_TOTAL) {
	R31_TOTAL = r31_TOTAL;
}








public BigDecimal getR32_1_DAY() {
	return R32_1_DAY;
}








public void setR32_1_DAY(BigDecimal r32_1_DAY) {
	R32_1_DAY = r32_1_DAY;
}








public BigDecimal getR32_2TO7_DAYS() {
	return R32_2TO7_DAYS;
}








public void setR32_2TO7_DAYS(BigDecimal r32_2to7_DAYS) {
	R32_2TO7_DAYS = r32_2to7_DAYS;
}








public BigDecimal getR32_8TO14_DAYS() {
	return R32_8TO14_DAYS;
}








public void setR32_8TO14_DAYS(BigDecimal r32_8to14_DAYS) {
	R32_8TO14_DAYS = r32_8to14_DAYS;
}








public BigDecimal getR32_15TO30_DAYS() {
	return R32_15TO30_DAYS;
}








public void setR32_15TO30_DAYS(BigDecimal r32_15to30_DAYS) {
	R32_15TO30_DAYS = r32_15to30_DAYS;
}








public BigDecimal getR32_31DAYS_UPTO_2MONTHS() {
	return R32_31DAYS_UPTO_2MONTHS;
}








public void setR32_31DAYS_UPTO_2MONTHS(BigDecimal r32_31days_UPTO_2MONTHS) {
	R32_31DAYS_UPTO_2MONTHS = r32_31days_UPTO_2MONTHS;
}








public BigDecimal getR32_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R32_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR32_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r32_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R32_MORETHAN_2MONTHS_UPTO_3MONHTS = r32_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR32_OVER_3MONTHS_UPTO_6MONTHS() {
	return R32_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR32_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r32_OVER_3MONTHS_UPTO_6MONTHS) {
	R32_OVER_3MONTHS_UPTO_6MONTHS = r32_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR32_OVER_6MONTHS_UPTO_1YEAR() {
	return R32_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR32_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r32_OVER_6MONTHS_UPTO_1YEAR) {
	R32_OVER_6MONTHS_UPTO_1YEAR = r32_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR32_OVER_1YEAR_UPTO_3YEARS() {
	return R32_OVER_1YEAR_UPTO_3YEARS;
}








public void setR32_OVER_1YEAR_UPTO_3YEARS(BigDecimal r32_OVER_1YEAR_UPTO_3YEARS) {
	R32_OVER_1YEAR_UPTO_3YEARS = r32_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR32_OVER_3YEARS_UPTO_5YEARS() {
	return R32_OVER_3YEARS_UPTO_5YEARS;
}








public void setR32_OVER_3YEARS_UPTO_5YEARS(BigDecimal r32_OVER_3YEARS_UPTO_5YEARS) {
	R32_OVER_3YEARS_UPTO_5YEARS = r32_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR32_OVER_5YEARS() {
	return R32_OVER_5YEARS;
}








public void setR32_OVER_5YEARS(BigDecimal r32_OVER_5YEARS) {
	R32_OVER_5YEARS = r32_OVER_5YEARS;
}








public BigDecimal getR32_TOTAL() {
	return R32_TOTAL;
}








public void setR32_TOTAL(BigDecimal r32_TOTAL) {
	R32_TOTAL = r32_TOTAL;
}








public BigDecimal getR33_1_DAY() {
	return R33_1_DAY;
}








public void setR33_1_DAY(BigDecimal r33_1_DAY) {
	R33_1_DAY = r33_1_DAY;
}








public BigDecimal getR33_2TO7_DAYS() {
	return R33_2TO7_DAYS;
}








public void setR33_2TO7_DAYS(BigDecimal r33_2to7_DAYS) {
	R33_2TO7_DAYS = r33_2to7_DAYS;
}








public BigDecimal getR33_8TO14_DAYS() {
	return R33_8TO14_DAYS;
}








public void setR33_8TO14_DAYS(BigDecimal r33_8to14_DAYS) {
	R33_8TO14_DAYS = r33_8to14_DAYS;
}








public BigDecimal getR33_15TO30_DAYS() {
	return R33_15TO30_DAYS;
}








public void setR33_15TO30_DAYS(BigDecimal r33_15to30_DAYS) {
	R33_15TO30_DAYS = r33_15to30_DAYS;
}








public BigDecimal getR33_31DAYS_UPTO_2MONTHS() {
	return R33_31DAYS_UPTO_2MONTHS;
}








public void setR33_31DAYS_UPTO_2MONTHS(BigDecimal r33_31days_UPTO_2MONTHS) {
	R33_31DAYS_UPTO_2MONTHS = r33_31days_UPTO_2MONTHS;
}








public BigDecimal getR33_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R33_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR33_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r33_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R33_MORETHAN_2MONTHS_UPTO_3MONHTS = r33_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR33_OVER_3MONTHS_UPTO_6MONTHS() {
	return R33_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR33_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r33_OVER_3MONTHS_UPTO_6MONTHS) {
	R33_OVER_3MONTHS_UPTO_6MONTHS = r33_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR33_OVER_6MONTHS_UPTO_1YEAR() {
	return R33_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR33_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r33_OVER_6MONTHS_UPTO_1YEAR) {
	R33_OVER_6MONTHS_UPTO_1YEAR = r33_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR33_OVER_1YEAR_UPTO_3YEARS() {
	return R33_OVER_1YEAR_UPTO_3YEARS;
}








public void setR33_OVER_1YEAR_UPTO_3YEARS(BigDecimal r33_OVER_1YEAR_UPTO_3YEARS) {
	R33_OVER_1YEAR_UPTO_3YEARS = r33_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR33_OVER_3YEARS_UPTO_5YEARS() {
	return R33_OVER_3YEARS_UPTO_5YEARS;
}








public void setR33_OVER_3YEARS_UPTO_5YEARS(BigDecimal r33_OVER_3YEARS_UPTO_5YEARS) {
	R33_OVER_3YEARS_UPTO_5YEARS = r33_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR33_OVER_5YEARS() {
	return R33_OVER_5YEARS;
}








public void setR33_OVER_5YEARS(BigDecimal r33_OVER_5YEARS) {
	R33_OVER_5YEARS = r33_OVER_5YEARS;
}








public BigDecimal getR33_TOTAL() {
	return R33_TOTAL;
}








public void setR33_TOTAL(BigDecimal r33_TOTAL) {
	R33_TOTAL = r33_TOTAL;
}








public BigDecimal getR34_1_DAY() {
	return R34_1_DAY;
}








public void setR34_1_DAY(BigDecimal r34_1_DAY) {
	R34_1_DAY = r34_1_DAY;
}








public BigDecimal getR34_2TO7_DAYS() {
	return R34_2TO7_DAYS;
}








public void setR34_2TO7_DAYS(BigDecimal r34_2to7_DAYS) {
	R34_2TO7_DAYS = r34_2to7_DAYS;
}








public BigDecimal getR34_8TO14_DAYS() {
	return R34_8TO14_DAYS;
}








public void setR34_8TO14_DAYS(BigDecimal r34_8to14_DAYS) {
	R34_8TO14_DAYS = r34_8to14_DAYS;
}








public BigDecimal getR34_15TO30_DAYS() {
	return R34_15TO30_DAYS;
}








public void setR34_15TO30_DAYS(BigDecimal r34_15to30_DAYS) {
	R34_15TO30_DAYS = r34_15to30_DAYS;
}








public BigDecimal getR34_31DAYS_UPTO_2MONTHS() {
	return R34_31DAYS_UPTO_2MONTHS;
}








public void setR34_31DAYS_UPTO_2MONTHS(BigDecimal r34_31days_UPTO_2MONTHS) {
	R34_31DAYS_UPTO_2MONTHS = r34_31days_UPTO_2MONTHS;
}








public BigDecimal getR34_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R34_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR34_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r34_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R34_MORETHAN_2MONTHS_UPTO_3MONHTS = r34_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR34_OVER_3MONTHS_UPTO_6MONTHS() {
	return R34_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR34_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r34_OVER_3MONTHS_UPTO_6MONTHS) {
	R34_OVER_3MONTHS_UPTO_6MONTHS = r34_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR34_OVER_6MONTHS_UPTO_1YEAR() {
	return R34_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR34_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r34_OVER_6MONTHS_UPTO_1YEAR) {
	R34_OVER_6MONTHS_UPTO_1YEAR = r34_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR34_OVER_1YEAR_UPTO_3YEARS() {
	return R34_OVER_1YEAR_UPTO_3YEARS;
}








public void setR34_OVER_1YEAR_UPTO_3YEARS(BigDecimal r34_OVER_1YEAR_UPTO_3YEARS) {
	R34_OVER_1YEAR_UPTO_3YEARS = r34_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR34_OVER_3YEARS_UPTO_5YEARS() {
	return R34_OVER_3YEARS_UPTO_5YEARS;
}








public void setR34_OVER_3YEARS_UPTO_5YEARS(BigDecimal r34_OVER_3YEARS_UPTO_5YEARS) {
	R34_OVER_3YEARS_UPTO_5YEARS = r34_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR34_OVER_5YEARS() {
	return R34_OVER_5YEARS;
}








public void setR34_OVER_5YEARS(BigDecimal r34_OVER_5YEARS) {
	R34_OVER_5YEARS = r34_OVER_5YEARS;
}








public BigDecimal getR34_TOTAL() {
	return R34_TOTAL;
}








public void setR34_TOTAL(BigDecimal r34_TOTAL) {
	R34_TOTAL = r34_TOTAL;
}








public BigDecimal getR35_1_DAY() {
	return R35_1_DAY;
}








public void setR35_1_DAY(BigDecimal r35_1_DAY) {
	R35_1_DAY = r35_1_DAY;
}








public BigDecimal getR35_2TO7_DAYS() {
	return R35_2TO7_DAYS;
}








public void setR35_2TO7_DAYS(BigDecimal r35_2to7_DAYS) {
	R35_2TO7_DAYS = r35_2to7_DAYS;
}








public BigDecimal getR35_8TO14_DAYS() {
	return R35_8TO14_DAYS;
}








public void setR35_8TO14_DAYS(BigDecimal r35_8to14_DAYS) {
	R35_8TO14_DAYS = r35_8to14_DAYS;
}








public BigDecimal getR35_15TO30_DAYS() {
	return R35_15TO30_DAYS;
}








public void setR35_15TO30_DAYS(BigDecimal r35_15to30_DAYS) {
	R35_15TO30_DAYS = r35_15to30_DAYS;
}








public BigDecimal getR35_31DAYS_UPTO_2MONTHS() {
	return R35_31DAYS_UPTO_2MONTHS;
}








public void setR35_31DAYS_UPTO_2MONTHS(BigDecimal r35_31days_UPTO_2MONTHS) {
	R35_31DAYS_UPTO_2MONTHS = r35_31days_UPTO_2MONTHS;
}








public BigDecimal getR35_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R35_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR35_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r35_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R35_MORETHAN_2MONTHS_UPTO_3MONHTS = r35_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR35_OVER_3MONTHS_UPTO_6MONTHS() {
	return R35_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR35_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r35_OVER_3MONTHS_UPTO_6MONTHS) {
	R35_OVER_3MONTHS_UPTO_6MONTHS = r35_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR35_OVER_6MONTHS_UPTO_1YEAR() {
	return R35_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR35_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r35_OVER_6MONTHS_UPTO_1YEAR) {
	R35_OVER_6MONTHS_UPTO_1YEAR = r35_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR35_OVER_1YEAR_UPTO_3YEARS() {
	return R35_OVER_1YEAR_UPTO_3YEARS;
}








public void setR35_OVER_1YEAR_UPTO_3YEARS(BigDecimal r35_OVER_1YEAR_UPTO_3YEARS) {
	R35_OVER_1YEAR_UPTO_3YEARS = r35_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR35_OVER_3YEARS_UPTO_5YEARS() {
	return R35_OVER_3YEARS_UPTO_5YEARS;
}








public void setR35_OVER_3YEARS_UPTO_5YEARS(BigDecimal r35_OVER_3YEARS_UPTO_5YEARS) {
	R35_OVER_3YEARS_UPTO_5YEARS = r35_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR35_OVER_5YEARS() {
	return R35_OVER_5YEARS;
}








public void setR35_OVER_5YEARS(BigDecimal r35_OVER_5YEARS) {
	R35_OVER_5YEARS = r35_OVER_5YEARS;
}








public BigDecimal getR35_TOTAL() {
	return R35_TOTAL;
}








public void setR35_TOTAL(BigDecimal r35_TOTAL) {
	R35_TOTAL = r35_TOTAL;
}








public BigDecimal getR36_1_DAY() {
	return R36_1_DAY;
}








public void setR36_1_DAY(BigDecimal r36_1_DAY) {
	R36_1_DAY = r36_1_DAY;
}








public BigDecimal getR36_2TO7_DAYS() {
	return R36_2TO7_DAYS;
}








public void setR36_2TO7_DAYS(BigDecimal r36_2to7_DAYS) {
	R36_2TO7_DAYS = r36_2to7_DAYS;
}








public BigDecimal getR36_8TO14_DAYS() {
	return R36_8TO14_DAYS;
}








public void setR36_8TO14_DAYS(BigDecimal r36_8to14_DAYS) {
	R36_8TO14_DAYS = r36_8to14_DAYS;
}








public BigDecimal getR36_15TO30_DAYS() {
	return R36_15TO30_DAYS;
}








public void setR36_15TO30_DAYS(BigDecimal r36_15to30_DAYS) {
	R36_15TO30_DAYS = r36_15to30_DAYS;
}








public BigDecimal getR36_31DAYS_UPTO_2MONTHS() {
	return R36_31DAYS_UPTO_2MONTHS;
}








public void setR36_31DAYS_UPTO_2MONTHS(BigDecimal r36_31days_UPTO_2MONTHS) {
	R36_31DAYS_UPTO_2MONTHS = r36_31days_UPTO_2MONTHS;
}








public BigDecimal getR36_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R36_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR36_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r36_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R36_MORETHAN_2MONTHS_UPTO_3MONHTS = r36_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR36_OVER_3MONTHS_UPTO_6MONTHS() {
	return R36_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR36_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r36_OVER_3MONTHS_UPTO_6MONTHS) {
	R36_OVER_3MONTHS_UPTO_6MONTHS = r36_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR36_OVER_6MONTHS_UPTO_1YEAR() {
	return R36_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR36_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r36_OVER_6MONTHS_UPTO_1YEAR) {
	R36_OVER_6MONTHS_UPTO_1YEAR = r36_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR36_OVER_1YEAR_UPTO_3YEARS() {
	return R36_OVER_1YEAR_UPTO_3YEARS;
}








public void setR36_OVER_1YEAR_UPTO_3YEARS(BigDecimal r36_OVER_1YEAR_UPTO_3YEARS) {
	R36_OVER_1YEAR_UPTO_3YEARS = r36_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR36_OVER_3YEARS_UPTO_5YEARS() {
	return R36_OVER_3YEARS_UPTO_5YEARS;
}








public void setR36_OVER_3YEARS_UPTO_5YEARS(BigDecimal r36_OVER_3YEARS_UPTO_5YEARS) {
	R36_OVER_3YEARS_UPTO_5YEARS = r36_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR36_OVER_5YEARS() {
	return R36_OVER_5YEARS;
}








public void setR36_OVER_5YEARS(BigDecimal r36_OVER_5YEARS) {
	R36_OVER_5YEARS = r36_OVER_5YEARS;
}








public BigDecimal getR36_TOTAL() {
	return R36_TOTAL;
}








public void setR36_TOTAL(BigDecimal r36_TOTAL) {
	R36_TOTAL = r36_TOTAL;
}








public BigDecimal getR37_1_DAY() {
	return R37_1_DAY;
}








public void setR37_1_DAY(BigDecimal r37_1_DAY) {
	R37_1_DAY = r37_1_DAY;
}








public BigDecimal getR37_2TO7_DAYS() {
	return R37_2TO7_DAYS;
}








public void setR37_2TO7_DAYS(BigDecimal r37_2to7_DAYS) {
	R37_2TO7_DAYS = r37_2to7_DAYS;
}








public BigDecimal getR37_8TO14_DAYS() {
	return R37_8TO14_DAYS;
}








public void setR37_8TO14_DAYS(BigDecimal r37_8to14_DAYS) {
	R37_8TO14_DAYS = r37_8to14_DAYS;
}








public BigDecimal getR37_15TO30_DAYS() {
	return R37_15TO30_DAYS;
}








public void setR37_15TO30_DAYS(BigDecimal r37_15to30_DAYS) {
	R37_15TO30_DAYS = r37_15to30_DAYS;
}








public BigDecimal getR37_31DAYS_UPTO_2MONTHS() {
	return R37_31DAYS_UPTO_2MONTHS;
}








public void setR37_31DAYS_UPTO_2MONTHS(BigDecimal r37_31days_UPTO_2MONTHS) {
	R37_31DAYS_UPTO_2MONTHS = r37_31days_UPTO_2MONTHS;
}








public BigDecimal getR37_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R37_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR37_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r37_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R37_MORETHAN_2MONTHS_UPTO_3MONHTS = r37_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR37_OVER_3MONTHS_UPTO_6MONTHS() {
	return R37_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR37_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r37_OVER_3MONTHS_UPTO_6MONTHS) {
	R37_OVER_3MONTHS_UPTO_6MONTHS = r37_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR37_OVER_6MONTHS_UPTO_1YEAR() {
	return R37_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR37_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r37_OVER_6MONTHS_UPTO_1YEAR) {
	R37_OVER_6MONTHS_UPTO_1YEAR = r37_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR37_OVER_1YEAR_UPTO_3YEARS() {
	return R37_OVER_1YEAR_UPTO_3YEARS;
}








public void setR37_OVER_1YEAR_UPTO_3YEARS(BigDecimal r37_OVER_1YEAR_UPTO_3YEARS) {
	R37_OVER_1YEAR_UPTO_3YEARS = r37_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR37_OVER_3YEARS_UPTO_5YEARS() {
	return R37_OVER_3YEARS_UPTO_5YEARS;
}








public void setR37_OVER_3YEARS_UPTO_5YEARS(BigDecimal r37_OVER_3YEARS_UPTO_5YEARS) {
	R37_OVER_3YEARS_UPTO_5YEARS = r37_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR37_OVER_5YEARS() {
	return R37_OVER_5YEARS;
}








public void setR37_OVER_5YEARS(BigDecimal r37_OVER_5YEARS) {
	R37_OVER_5YEARS = r37_OVER_5YEARS;
}








public BigDecimal getR37_TOTAL() {
	return R37_TOTAL;
}








public void setR37_TOTAL(BigDecimal r37_TOTAL) {
	R37_TOTAL = r37_TOTAL;
}








public BigDecimal getR38_1_DAY() {
	return R38_1_DAY;
}








public void setR38_1_DAY(BigDecimal r38_1_DAY) {
	R38_1_DAY = r38_1_DAY;
}








public BigDecimal getR38_2TO7_DAYS() {
	return R38_2TO7_DAYS;
}








public void setR38_2TO7_DAYS(BigDecimal r38_2to7_DAYS) {
	R38_2TO7_DAYS = r38_2to7_DAYS;
}








public BigDecimal getR38_8TO14_DAYS() {
	return R38_8TO14_DAYS;
}








public void setR38_8TO14_DAYS(BigDecimal r38_8to14_DAYS) {
	R38_8TO14_DAYS = r38_8to14_DAYS;
}








public BigDecimal getR38_15TO30_DAYS() {
	return R38_15TO30_DAYS;
}








public void setR38_15TO30_DAYS(BigDecimal r38_15to30_DAYS) {
	R38_15TO30_DAYS = r38_15to30_DAYS;
}








public BigDecimal getR38_31DAYS_UPTO_2MONTHS() {
	return R38_31DAYS_UPTO_2MONTHS;
}








public void setR38_31DAYS_UPTO_2MONTHS(BigDecimal r38_31days_UPTO_2MONTHS) {
	R38_31DAYS_UPTO_2MONTHS = r38_31days_UPTO_2MONTHS;
}








public BigDecimal getR38_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R38_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR38_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r38_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R38_MORETHAN_2MONTHS_UPTO_3MONHTS = r38_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR38_OVER_3MONTHS_UPTO_6MONTHS() {
	return R38_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR38_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r38_OVER_3MONTHS_UPTO_6MONTHS) {
	R38_OVER_3MONTHS_UPTO_6MONTHS = r38_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR38_OVER_6MONTHS_UPTO_1YEAR() {
	return R38_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR38_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r38_OVER_6MONTHS_UPTO_1YEAR) {
	R38_OVER_6MONTHS_UPTO_1YEAR = r38_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR38_OVER_1YEAR_UPTO_3YEARS() {
	return R38_OVER_1YEAR_UPTO_3YEARS;
}








public void setR38_OVER_1YEAR_UPTO_3YEARS(BigDecimal r38_OVER_1YEAR_UPTO_3YEARS) {
	R38_OVER_1YEAR_UPTO_3YEARS = r38_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR38_OVER_3YEARS_UPTO_5YEARS() {
	return R38_OVER_3YEARS_UPTO_5YEARS;
}








public void setR38_OVER_3YEARS_UPTO_5YEARS(BigDecimal r38_OVER_3YEARS_UPTO_5YEARS) {
	R38_OVER_3YEARS_UPTO_5YEARS = r38_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR38_OVER_5YEARS() {
	return R38_OVER_5YEARS;
}








public void setR38_OVER_5YEARS(BigDecimal r38_OVER_5YEARS) {
	R38_OVER_5YEARS = r38_OVER_5YEARS;
}








public BigDecimal getR38_TOTAL() {
	return R38_TOTAL;
}








public void setR38_TOTAL(BigDecimal r38_TOTAL) {
	R38_TOTAL = r38_TOTAL;
}








public BigDecimal getR39_1_DAY() {
	return R39_1_DAY;
}








public void setR39_1_DAY(BigDecimal r39_1_DAY) {
	R39_1_DAY = r39_1_DAY;
}








public BigDecimal getR39_2TO7_DAYS() {
	return R39_2TO7_DAYS;
}








public void setR39_2TO7_DAYS(BigDecimal r39_2to7_DAYS) {
	R39_2TO7_DAYS = r39_2to7_DAYS;
}








public BigDecimal getR39_8TO14_DAYS() {
	return R39_8TO14_DAYS;
}








public void setR39_8TO14_DAYS(BigDecimal r39_8to14_DAYS) {
	R39_8TO14_DAYS = r39_8to14_DAYS;
}








public BigDecimal getR39_15TO30_DAYS() {
	return R39_15TO30_DAYS;
}








public void setR39_15TO30_DAYS(BigDecimal r39_15to30_DAYS) {
	R39_15TO30_DAYS = r39_15to30_DAYS;
}








public BigDecimal getR39_31DAYS_UPTO_2MONTHS() {
	return R39_31DAYS_UPTO_2MONTHS;
}








public void setR39_31DAYS_UPTO_2MONTHS(BigDecimal r39_31days_UPTO_2MONTHS) {
	R39_31DAYS_UPTO_2MONTHS = r39_31days_UPTO_2MONTHS;
}








public BigDecimal getR39_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R39_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR39_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r39_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R39_MORETHAN_2MONTHS_UPTO_3MONHTS = r39_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR39_OVER_3MONTHS_UPTO_6MONTHS() {
	return R39_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR39_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r39_OVER_3MONTHS_UPTO_6MONTHS) {
	R39_OVER_3MONTHS_UPTO_6MONTHS = r39_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR39_OVER_6MONTHS_UPTO_1YEAR() {
	return R39_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR39_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r39_OVER_6MONTHS_UPTO_1YEAR) {
	R39_OVER_6MONTHS_UPTO_1YEAR = r39_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR39_OVER_1YEAR_UPTO_3YEARS() {
	return R39_OVER_1YEAR_UPTO_3YEARS;
}








public void setR39_OVER_1YEAR_UPTO_3YEARS(BigDecimal r39_OVER_1YEAR_UPTO_3YEARS) {
	R39_OVER_1YEAR_UPTO_3YEARS = r39_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR39_OVER_3YEARS_UPTO_5YEARS() {
	return R39_OVER_3YEARS_UPTO_5YEARS;
}








public void setR39_OVER_3YEARS_UPTO_5YEARS(BigDecimal r39_OVER_3YEARS_UPTO_5YEARS) {
	R39_OVER_3YEARS_UPTO_5YEARS = r39_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR39_OVER_5YEARS() {
	return R39_OVER_5YEARS;
}








public void setR39_OVER_5YEARS(BigDecimal r39_OVER_5YEARS) {
	R39_OVER_5YEARS = r39_OVER_5YEARS;
}








public BigDecimal getR39_TOTAL() {
	return R39_TOTAL;
}








public void setR39_TOTAL(BigDecimal r39_TOTAL) {
	R39_TOTAL = r39_TOTAL;
}








public BigDecimal getR40_1_DAY() {
	return R40_1_DAY;
}








public void setR40_1_DAY(BigDecimal r40_1_DAY) {
	R40_1_DAY = r40_1_DAY;
}








public BigDecimal getR40_2TO7_DAYS() {
	return R40_2TO7_DAYS;
}








public void setR40_2TO7_DAYS(BigDecimal r40_2to7_DAYS) {
	R40_2TO7_DAYS = r40_2to7_DAYS;
}








public BigDecimal getR40_8TO14_DAYS() {
	return R40_8TO14_DAYS;
}








public void setR40_8TO14_DAYS(BigDecimal r40_8to14_DAYS) {
	R40_8TO14_DAYS = r40_8to14_DAYS;
}








public BigDecimal getR40_15TO30_DAYS() {
	return R40_15TO30_DAYS;
}








public void setR40_15TO30_DAYS(BigDecimal r40_15to30_DAYS) {
	R40_15TO30_DAYS = r40_15to30_DAYS;
}








public BigDecimal getR40_31DAYS_UPTO_2MONTHS() {
	return R40_31DAYS_UPTO_2MONTHS;
}








public void setR40_31DAYS_UPTO_2MONTHS(BigDecimal r40_31days_UPTO_2MONTHS) {
	R40_31DAYS_UPTO_2MONTHS = r40_31days_UPTO_2MONTHS;
}








public BigDecimal getR40_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R40_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR40_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r40_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R40_MORETHAN_2MONTHS_UPTO_3MONHTS = r40_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR40_OVER_3MONTHS_UPTO_6MONTHS() {
	return R40_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR40_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r40_OVER_3MONTHS_UPTO_6MONTHS) {
	R40_OVER_3MONTHS_UPTO_6MONTHS = r40_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR40_OVER_6MONTHS_UPTO_1YEAR() {
	return R40_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR40_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r40_OVER_6MONTHS_UPTO_1YEAR) {
	R40_OVER_6MONTHS_UPTO_1YEAR = r40_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR40_OVER_1YEAR_UPTO_3YEARS() {
	return R40_OVER_1YEAR_UPTO_3YEARS;
}








public void setR40_OVER_1YEAR_UPTO_3YEARS(BigDecimal r40_OVER_1YEAR_UPTO_3YEARS) {
	R40_OVER_1YEAR_UPTO_3YEARS = r40_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR40_OVER_3YEARS_UPTO_5YEARS() {
	return R40_OVER_3YEARS_UPTO_5YEARS;
}








public void setR40_OVER_3YEARS_UPTO_5YEARS(BigDecimal r40_OVER_3YEARS_UPTO_5YEARS) {
	R40_OVER_3YEARS_UPTO_5YEARS = r40_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR40_OVER_5YEARS() {
	return R40_OVER_5YEARS;
}








public void setR40_OVER_5YEARS(BigDecimal r40_OVER_5YEARS) {
	R40_OVER_5YEARS = r40_OVER_5YEARS;
}








public BigDecimal getR40_TOTAL() {
	return R40_TOTAL;
}








public void setR40_TOTAL(BigDecimal r40_TOTAL) {
	R40_TOTAL = r40_TOTAL;
}








public BigDecimal getR41_1_DAY() {
	return R41_1_DAY;
}








public void setR41_1_DAY(BigDecimal r41_1_DAY) {
	R41_1_DAY = r41_1_DAY;
}








public BigDecimal getR41_2TO7_DAYS() {
	return R41_2TO7_DAYS;
}








public void setR41_2TO7_DAYS(BigDecimal r41_2to7_DAYS) {
	R41_2TO7_DAYS = r41_2to7_DAYS;
}








public BigDecimal getR41_8TO14_DAYS() {
	return R41_8TO14_DAYS;
}








public void setR41_8TO14_DAYS(BigDecimal r41_8to14_DAYS) {
	R41_8TO14_DAYS = r41_8to14_DAYS;
}








public BigDecimal getR41_15TO30_DAYS() {
	return R41_15TO30_DAYS;
}








public void setR41_15TO30_DAYS(BigDecimal r41_15to30_DAYS) {
	R41_15TO30_DAYS = r41_15to30_DAYS;
}








public BigDecimal getR41_31DAYS_UPTO_2MONTHS() {
	return R41_31DAYS_UPTO_2MONTHS;
}








public void setR41_31DAYS_UPTO_2MONTHS(BigDecimal r41_31days_UPTO_2MONTHS) {
	R41_31DAYS_UPTO_2MONTHS = r41_31days_UPTO_2MONTHS;
}








public BigDecimal getR41_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R41_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR41_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r41_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R41_MORETHAN_2MONTHS_UPTO_3MONHTS = r41_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR41_OVER_3MONTHS_UPTO_6MONTHS() {
	return R41_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR41_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r41_OVER_3MONTHS_UPTO_6MONTHS) {
	R41_OVER_3MONTHS_UPTO_6MONTHS = r41_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR41_OVER_6MONTHS_UPTO_1YEAR() {
	return R41_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR41_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r41_OVER_6MONTHS_UPTO_1YEAR) {
	R41_OVER_6MONTHS_UPTO_1YEAR = r41_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR41_OVER_1YEAR_UPTO_3YEARS() {
	return R41_OVER_1YEAR_UPTO_3YEARS;
}








public void setR41_OVER_1YEAR_UPTO_3YEARS(BigDecimal r41_OVER_1YEAR_UPTO_3YEARS) {
	R41_OVER_1YEAR_UPTO_3YEARS = r41_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR41_OVER_3YEARS_UPTO_5YEARS() {
	return R41_OVER_3YEARS_UPTO_5YEARS;
}








public void setR41_OVER_3YEARS_UPTO_5YEARS(BigDecimal r41_OVER_3YEARS_UPTO_5YEARS) {
	R41_OVER_3YEARS_UPTO_5YEARS = r41_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR41_OVER_5YEARS() {
	return R41_OVER_5YEARS;
}








public void setR41_OVER_5YEARS(BigDecimal r41_OVER_5YEARS) {
	R41_OVER_5YEARS = r41_OVER_5YEARS;
}








public BigDecimal getR41_TOTAL() {
	return R41_TOTAL;
}








public void setR41_TOTAL(BigDecimal r41_TOTAL) {
	R41_TOTAL = r41_TOTAL;
}








public BigDecimal getR42_1_DAY() {
	return R42_1_DAY;
}








public void setR42_1_DAY(BigDecimal r42_1_DAY) {
	R42_1_DAY = r42_1_DAY;
}








public BigDecimal getR42_2TO7_DAYS() {
	return R42_2TO7_DAYS;
}








public void setR42_2TO7_DAYS(BigDecimal r42_2to7_DAYS) {
	R42_2TO7_DAYS = r42_2to7_DAYS;
}








public BigDecimal getR42_8TO14_DAYS() {
	return R42_8TO14_DAYS;
}








public void setR42_8TO14_DAYS(BigDecimal r42_8to14_DAYS) {
	R42_8TO14_DAYS = r42_8to14_DAYS;
}








public BigDecimal getR42_15TO30_DAYS() {
	return R42_15TO30_DAYS;
}








public void setR42_15TO30_DAYS(BigDecimal r42_15to30_DAYS) {
	R42_15TO30_DAYS = r42_15to30_DAYS;
}








public BigDecimal getR42_31DAYS_UPTO_2MONTHS() {
	return R42_31DAYS_UPTO_2MONTHS;
}








public void setR42_31DAYS_UPTO_2MONTHS(BigDecimal r42_31days_UPTO_2MONTHS) {
	R42_31DAYS_UPTO_2MONTHS = r42_31days_UPTO_2MONTHS;
}








public BigDecimal getR42_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R42_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR42_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r42_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R42_MORETHAN_2MONTHS_UPTO_3MONHTS = r42_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR42_OVER_3MONTHS_UPTO_6MONTHS() {
	return R42_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR42_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r42_OVER_3MONTHS_UPTO_6MONTHS) {
	R42_OVER_3MONTHS_UPTO_6MONTHS = r42_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR42_OVER_6MONTHS_UPTO_1YEAR() {
	return R42_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR42_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r42_OVER_6MONTHS_UPTO_1YEAR) {
	R42_OVER_6MONTHS_UPTO_1YEAR = r42_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR42_OVER_1YEAR_UPTO_3YEARS() {
	return R42_OVER_1YEAR_UPTO_3YEARS;
}








public void setR42_OVER_1YEAR_UPTO_3YEARS(BigDecimal r42_OVER_1YEAR_UPTO_3YEARS) {
	R42_OVER_1YEAR_UPTO_3YEARS = r42_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR42_OVER_3YEARS_UPTO_5YEARS() {
	return R42_OVER_3YEARS_UPTO_5YEARS;
}








public void setR42_OVER_3YEARS_UPTO_5YEARS(BigDecimal r42_OVER_3YEARS_UPTO_5YEARS) {
	R42_OVER_3YEARS_UPTO_5YEARS = r42_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR42_OVER_5YEARS() {
	return R42_OVER_5YEARS;
}








public void setR42_OVER_5YEARS(BigDecimal r42_OVER_5YEARS) {
	R42_OVER_5YEARS = r42_OVER_5YEARS;
}








public BigDecimal getR42_TOTAL() {
	return R42_TOTAL;
}








public void setR42_TOTAL(BigDecimal r42_TOTAL) {
	R42_TOTAL = r42_TOTAL;
}








public BigDecimal getR43_1_DAY() {
	return R43_1_DAY;
}








public void setR43_1_DAY(BigDecimal r43_1_DAY) {
	R43_1_DAY = r43_1_DAY;
}








public BigDecimal getR43_2TO7_DAYS() {
	return R43_2TO7_DAYS;
}








public void setR43_2TO7_DAYS(BigDecimal r43_2to7_DAYS) {
	R43_2TO7_DAYS = r43_2to7_DAYS;
}








public BigDecimal getR43_8TO14_DAYS() {
	return R43_8TO14_DAYS;
}








public void setR43_8TO14_DAYS(BigDecimal r43_8to14_DAYS) {
	R43_8TO14_DAYS = r43_8to14_DAYS;
}








public BigDecimal getR43_15TO30_DAYS() {
	return R43_15TO30_DAYS;
}








public void setR43_15TO30_DAYS(BigDecimal r43_15to30_DAYS) {
	R43_15TO30_DAYS = r43_15to30_DAYS;
}








public BigDecimal getR43_31DAYS_UPTO_2MONTHS() {
	return R43_31DAYS_UPTO_2MONTHS;
}








public void setR43_31DAYS_UPTO_2MONTHS(BigDecimal r43_31days_UPTO_2MONTHS) {
	R43_31DAYS_UPTO_2MONTHS = r43_31days_UPTO_2MONTHS;
}








public BigDecimal getR43_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R43_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR43_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r43_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R43_MORETHAN_2MONTHS_UPTO_3MONHTS = r43_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR43_OVER_3MONTHS_UPTO_6MONTHS() {
	return R43_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR43_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r43_OVER_3MONTHS_UPTO_6MONTHS) {
	R43_OVER_3MONTHS_UPTO_6MONTHS = r43_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR43_OVER_6MONTHS_UPTO_1YEAR() {
	return R43_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR43_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r43_OVER_6MONTHS_UPTO_1YEAR) {
	R43_OVER_6MONTHS_UPTO_1YEAR = r43_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR43_OVER_1YEAR_UPTO_3YEARS() {
	return R43_OVER_1YEAR_UPTO_3YEARS;
}








public void setR43_OVER_1YEAR_UPTO_3YEARS(BigDecimal r43_OVER_1YEAR_UPTO_3YEARS) {
	R43_OVER_1YEAR_UPTO_3YEARS = r43_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR43_OVER_3YEARS_UPTO_5YEARS() {
	return R43_OVER_3YEARS_UPTO_5YEARS;
}








public void setR43_OVER_3YEARS_UPTO_5YEARS(BigDecimal r43_OVER_3YEARS_UPTO_5YEARS) {
	R43_OVER_3YEARS_UPTO_5YEARS = r43_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR43_OVER_5YEARS() {
	return R43_OVER_5YEARS;
}








public void setR43_OVER_5YEARS(BigDecimal r43_OVER_5YEARS) {
	R43_OVER_5YEARS = r43_OVER_5YEARS;
}








public BigDecimal getR43_TOTAL() {
	return R43_TOTAL;
}








public void setR43_TOTAL(BigDecimal r43_TOTAL) {
	R43_TOTAL = r43_TOTAL;
}








public BigDecimal getR44_1_DAY() {
	return R44_1_DAY;
}








public void setR44_1_DAY(BigDecimal r44_1_DAY) {
	R44_1_DAY = r44_1_DAY;
}








public BigDecimal getR44_2TO7_DAYS() {
	return R44_2TO7_DAYS;
}








public void setR44_2TO7_DAYS(BigDecimal r44_2to7_DAYS) {
	R44_2TO7_DAYS = r44_2to7_DAYS;
}








public BigDecimal getR44_8TO14_DAYS() {
	return R44_8TO14_DAYS;
}








public void setR44_8TO14_DAYS(BigDecimal r44_8to14_DAYS) {
	R44_8TO14_DAYS = r44_8to14_DAYS;
}








public BigDecimal getR44_15TO30_DAYS() {
	return R44_15TO30_DAYS;
}








public void setR44_15TO30_DAYS(BigDecimal r44_15to30_DAYS) {
	R44_15TO30_DAYS = r44_15to30_DAYS;
}








public BigDecimal getR44_31DAYS_UPTO_2MONTHS() {
	return R44_31DAYS_UPTO_2MONTHS;
}








public void setR44_31DAYS_UPTO_2MONTHS(BigDecimal r44_31days_UPTO_2MONTHS) {
	R44_31DAYS_UPTO_2MONTHS = r44_31days_UPTO_2MONTHS;
}








public BigDecimal getR44_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R44_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR44_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r44_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R44_MORETHAN_2MONTHS_UPTO_3MONHTS = r44_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR44_OVER_3MONTHS_UPTO_6MONTHS() {
	return R44_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR44_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r44_OVER_3MONTHS_UPTO_6MONTHS) {
	R44_OVER_3MONTHS_UPTO_6MONTHS = r44_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR44_OVER_6MONTHS_UPTO_1YEAR() {
	return R44_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR44_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r44_OVER_6MONTHS_UPTO_1YEAR) {
	R44_OVER_6MONTHS_UPTO_1YEAR = r44_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR44_OVER_1YEAR_UPTO_3YEARS() {
	return R44_OVER_1YEAR_UPTO_3YEARS;
}








public void setR44_OVER_1YEAR_UPTO_3YEARS(BigDecimal r44_OVER_1YEAR_UPTO_3YEARS) {
	R44_OVER_1YEAR_UPTO_3YEARS = r44_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR44_OVER_3YEARS_UPTO_5YEARS() {
	return R44_OVER_3YEARS_UPTO_5YEARS;
}








public void setR44_OVER_3YEARS_UPTO_5YEARS(BigDecimal r44_OVER_3YEARS_UPTO_5YEARS) {
	R44_OVER_3YEARS_UPTO_5YEARS = r44_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR44_OVER_5YEARS() {
	return R44_OVER_5YEARS;
}








public void setR44_OVER_5YEARS(BigDecimal r44_OVER_5YEARS) {
	R44_OVER_5YEARS = r44_OVER_5YEARS;
}








public BigDecimal getR44_TOTAL() {
	return R44_TOTAL;
}








public void setR44_TOTAL(BigDecimal r44_TOTAL) {
	R44_TOTAL = r44_TOTAL;
}








public BigDecimal getR45_1_DAY() {
	return R45_1_DAY;
}








public void setR45_1_DAY(BigDecimal r45_1_DAY) {
	R45_1_DAY = r45_1_DAY;
}








public BigDecimal getR45_2TO7_DAYS() {
	return R45_2TO7_DAYS;
}








public void setR45_2TO7_DAYS(BigDecimal r45_2to7_DAYS) {
	R45_2TO7_DAYS = r45_2to7_DAYS;
}








public BigDecimal getR45_8TO14_DAYS() {
	return R45_8TO14_DAYS;
}








public void setR45_8TO14_DAYS(BigDecimal r45_8to14_DAYS) {
	R45_8TO14_DAYS = r45_8to14_DAYS;
}








public BigDecimal getR45_15TO30_DAYS() {
	return R45_15TO30_DAYS;
}








public void setR45_15TO30_DAYS(BigDecimal r45_15to30_DAYS) {
	R45_15TO30_DAYS = r45_15to30_DAYS;
}








public BigDecimal getR45_31DAYS_UPTO_2MONTHS() {
	return R45_31DAYS_UPTO_2MONTHS;
}








public void setR45_31DAYS_UPTO_2MONTHS(BigDecimal r45_31days_UPTO_2MONTHS) {
	R45_31DAYS_UPTO_2MONTHS = r45_31days_UPTO_2MONTHS;
}








public BigDecimal getR45_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R45_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR45_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r45_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R45_MORETHAN_2MONTHS_UPTO_3MONHTS = r45_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR45_OVER_3MONTHS_UPTO_6MONTHS() {
	return R45_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR45_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r45_OVER_3MONTHS_UPTO_6MONTHS) {
	R45_OVER_3MONTHS_UPTO_6MONTHS = r45_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR45_OVER_6MONTHS_UPTO_1YEAR() {
	return R45_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR45_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r45_OVER_6MONTHS_UPTO_1YEAR) {
	R45_OVER_6MONTHS_UPTO_1YEAR = r45_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR45_OVER_1YEAR_UPTO_3YEARS() {
	return R45_OVER_1YEAR_UPTO_3YEARS;
}








public void setR45_OVER_1YEAR_UPTO_3YEARS(BigDecimal r45_OVER_1YEAR_UPTO_3YEARS) {
	R45_OVER_1YEAR_UPTO_3YEARS = r45_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR45_OVER_3YEARS_UPTO_5YEARS() {
	return R45_OVER_3YEARS_UPTO_5YEARS;
}








public void setR45_OVER_3YEARS_UPTO_5YEARS(BigDecimal r45_OVER_3YEARS_UPTO_5YEARS) {
	R45_OVER_3YEARS_UPTO_5YEARS = r45_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR45_OVER_5YEARS() {
	return R45_OVER_5YEARS;
}








public void setR45_OVER_5YEARS(BigDecimal r45_OVER_5YEARS) {
	R45_OVER_5YEARS = r45_OVER_5YEARS;
}








public BigDecimal getR45_TOTAL() {
	return R45_TOTAL;
}








public void setR45_TOTAL(BigDecimal r45_TOTAL) {
	R45_TOTAL = r45_TOTAL;
}








public BigDecimal getR46_1_DAY() {
	return R46_1_DAY;
}








public void setR46_1_DAY(BigDecimal r46_1_DAY) {
	R46_1_DAY = r46_1_DAY;
}








public BigDecimal getR46_2TO7_DAYS() {
	return R46_2TO7_DAYS;
}








public void setR46_2TO7_DAYS(BigDecimal r46_2to7_DAYS) {
	R46_2TO7_DAYS = r46_2to7_DAYS;
}








public BigDecimal getR46_8TO14_DAYS() {
	return R46_8TO14_DAYS;
}








public void setR46_8TO14_DAYS(BigDecimal r46_8to14_DAYS) {
	R46_8TO14_DAYS = r46_8to14_DAYS;
}








public BigDecimal getR46_15TO30_DAYS() {
	return R46_15TO30_DAYS;
}








public void setR46_15TO30_DAYS(BigDecimal r46_15to30_DAYS) {
	R46_15TO30_DAYS = r46_15to30_DAYS;
}








public BigDecimal getR46_31DAYS_UPTO_2MONTHS() {
	return R46_31DAYS_UPTO_2MONTHS;
}








public void setR46_31DAYS_UPTO_2MONTHS(BigDecimal r46_31days_UPTO_2MONTHS) {
	R46_31DAYS_UPTO_2MONTHS = r46_31days_UPTO_2MONTHS;
}








public BigDecimal getR46_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R46_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR46_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r46_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R46_MORETHAN_2MONTHS_UPTO_3MONHTS = r46_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR46_OVER_3MONTHS_UPTO_6MONTHS() {
	return R46_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR46_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r46_OVER_3MONTHS_UPTO_6MONTHS) {
	R46_OVER_3MONTHS_UPTO_6MONTHS = r46_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR46_OVER_6MONTHS_UPTO_1YEAR() {
	return R46_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR46_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r46_OVER_6MONTHS_UPTO_1YEAR) {
	R46_OVER_6MONTHS_UPTO_1YEAR = r46_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR46_OVER_1YEAR_UPTO_3YEARS() {
	return R46_OVER_1YEAR_UPTO_3YEARS;
}








public void setR46_OVER_1YEAR_UPTO_3YEARS(BigDecimal r46_OVER_1YEAR_UPTO_3YEARS) {
	R46_OVER_1YEAR_UPTO_3YEARS = r46_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR46_OVER_3YEARS_UPTO_5YEARS() {
	return R46_OVER_3YEARS_UPTO_5YEARS;
}








public void setR46_OVER_3YEARS_UPTO_5YEARS(BigDecimal r46_OVER_3YEARS_UPTO_5YEARS) {
	R46_OVER_3YEARS_UPTO_5YEARS = r46_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR46_OVER_5YEARS() {
	return R46_OVER_5YEARS;
}








public void setR46_OVER_5YEARS(BigDecimal r46_OVER_5YEARS) {
	R46_OVER_5YEARS = r46_OVER_5YEARS;
}








public BigDecimal getR46_TOTAL() {
	return R46_TOTAL;
}








public void setR46_TOTAL(BigDecimal r46_TOTAL) {
	R46_TOTAL = r46_TOTAL;
}








public BigDecimal getR47_1_DAY() {
	return R47_1_DAY;
}








public void setR47_1_DAY(BigDecimal r47_1_DAY) {
	R47_1_DAY = r47_1_DAY;
}








public BigDecimal getR47_2TO7_DAYS() {
	return R47_2TO7_DAYS;
}








public void setR47_2TO7_DAYS(BigDecimal r47_2to7_DAYS) {
	R47_2TO7_DAYS = r47_2to7_DAYS;
}








public BigDecimal getR47_8TO14_DAYS() {
	return R47_8TO14_DAYS;
}








public void setR47_8TO14_DAYS(BigDecimal r47_8to14_DAYS) {
	R47_8TO14_DAYS = r47_8to14_DAYS;
}








public BigDecimal getR47_15TO30_DAYS() {
	return R47_15TO30_DAYS;
}








public void setR47_15TO30_DAYS(BigDecimal r47_15to30_DAYS) {
	R47_15TO30_DAYS = r47_15to30_DAYS;
}








public BigDecimal getR47_31DAYS_UPTO_2MONTHS() {
	return R47_31DAYS_UPTO_2MONTHS;
}








public void setR47_31DAYS_UPTO_2MONTHS(BigDecimal r47_31days_UPTO_2MONTHS) {
	R47_31DAYS_UPTO_2MONTHS = r47_31days_UPTO_2MONTHS;
}








public BigDecimal getR47_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R47_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR47_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r47_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R47_MORETHAN_2MONTHS_UPTO_3MONHTS = r47_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR47_OVER_3MONTHS_UPTO_6MONTHS() {
	return R47_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR47_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r47_OVER_3MONTHS_UPTO_6MONTHS) {
	R47_OVER_3MONTHS_UPTO_6MONTHS = r47_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR47_OVER_6MONTHS_UPTO_1YEAR() {
	return R47_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR47_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r47_OVER_6MONTHS_UPTO_1YEAR) {
	R47_OVER_6MONTHS_UPTO_1YEAR = r47_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR47_OVER_1YEAR_UPTO_3YEARS() {
	return R47_OVER_1YEAR_UPTO_3YEARS;
}








public void setR47_OVER_1YEAR_UPTO_3YEARS(BigDecimal r47_OVER_1YEAR_UPTO_3YEARS) {
	R47_OVER_1YEAR_UPTO_3YEARS = r47_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR47_OVER_3YEARS_UPTO_5YEARS() {
	return R47_OVER_3YEARS_UPTO_5YEARS;
}








public void setR47_OVER_3YEARS_UPTO_5YEARS(BigDecimal r47_OVER_3YEARS_UPTO_5YEARS) {
	R47_OVER_3YEARS_UPTO_5YEARS = r47_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR47_OVER_5YEARS() {
	return R47_OVER_5YEARS;
}








public void setR47_OVER_5YEARS(BigDecimal r47_OVER_5YEARS) {
	R47_OVER_5YEARS = r47_OVER_5YEARS;
}








public BigDecimal getR47_TOTAL() {
	return R47_TOTAL;
}








public void setR47_TOTAL(BigDecimal r47_TOTAL) {
	R47_TOTAL = r47_TOTAL;
}








public BigDecimal getR48_1_DAY() {
	return R48_1_DAY;
}








public void setR48_1_DAY(BigDecimal r48_1_DAY) {
	R48_1_DAY = r48_1_DAY;
}








public BigDecimal getR48_2TO7_DAYS() {
	return R48_2TO7_DAYS;
}








public void setR48_2TO7_DAYS(BigDecimal r48_2to7_DAYS) {
	R48_2TO7_DAYS = r48_2to7_DAYS;
}








public BigDecimal getR48_8TO14_DAYS() {
	return R48_8TO14_DAYS;
}








public void setR48_8TO14_DAYS(BigDecimal r48_8to14_DAYS) {
	R48_8TO14_DAYS = r48_8to14_DAYS;
}








public BigDecimal getR48_15TO30_DAYS() {
	return R48_15TO30_DAYS;
}








public void setR48_15TO30_DAYS(BigDecimal r48_15to30_DAYS) {
	R48_15TO30_DAYS = r48_15to30_DAYS;
}








public BigDecimal getR48_31DAYS_UPTO_2MONTHS() {
	return R48_31DAYS_UPTO_2MONTHS;
}








public void setR48_31DAYS_UPTO_2MONTHS(BigDecimal r48_31days_UPTO_2MONTHS) {
	R48_31DAYS_UPTO_2MONTHS = r48_31days_UPTO_2MONTHS;
}








public BigDecimal getR48_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R48_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR48_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r48_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R48_MORETHAN_2MONTHS_UPTO_3MONHTS = r48_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR48_OVER_3MONTHS_UPTO_6MONTHS() {
	return R48_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR48_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r48_OVER_3MONTHS_UPTO_6MONTHS) {
	R48_OVER_3MONTHS_UPTO_6MONTHS = r48_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR48_OVER_6MONTHS_UPTO_1YEAR() {
	return R48_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR48_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r48_OVER_6MONTHS_UPTO_1YEAR) {
	R48_OVER_6MONTHS_UPTO_1YEAR = r48_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR48_OVER_1YEAR_UPTO_3YEARS() {
	return R48_OVER_1YEAR_UPTO_3YEARS;
}








public void setR48_OVER_1YEAR_UPTO_3YEARS(BigDecimal r48_OVER_1YEAR_UPTO_3YEARS) {
	R48_OVER_1YEAR_UPTO_3YEARS = r48_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR48_OVER_3YEARS_UPTO_5YEARS() {
	return R48_OVER_3YEARS_UPTO_5YEARS;
}








public void setR48_OVER_3YEARS_UPTO_5YEARS(BigDecimal r48_OVER_3YEARS_UPTO_5YEARS) {
	R48_OVER_3YEARS_UPTO_5YEARS = r48_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR48_OVER_5YEARS() {
	return R48_OVER_5YEARS;
}








public void setR48_OVER_5YEARS(BigDecimal r48_OVER_5YEARS) {
	R48_OVER_5YEARS = r48_OVER_5YEARS;
}








public BigDecimal getR48_TOTAL() {
	return R48_TOTAL;
}








public void setR48_TOTAL(BigDecimal r48_TOTAL) {
	R48_TOTAL = r48_TOTAL;
}








public BigDecimal getR49_1_DAY() {
	return R49_1_DAY;
}








public void setR49_1_DAY(BigDecimal r49_1_DAY) {
	R49_1_DAY = r49_1_DAY;
}








public BigDecimal getR49_2TO7_DAYS() {
	return R49_2TO7_DAYS;
}








public void setR49_2TO7_DAYS(BigDecimal r49_2to7_DAYS) {
	R49_2TO7_DAYS = r49_2to7_DAYS;
}








public BigDecimal getR49_8TO14_DAYS() {
	return R49_8TO14_DAYS;
}








public void setR49_8TO14_DAYS(BigDecimal r49_8to14_DAYS) {
	R49_8TO14_DAYS = r49_8to14_DAYS;
}








public BigDecimal getR49_15TO30_DAYS() {
	return R49_15TO30_DAYS;
}








public void setR49_15TO30_DAYS(BigDecimal r49_15to30_DAYS) {
	R49_15TO30_DAYS = r49_15to30_DAYS;
}








public BigDecimal getR49_31DAYS_UPTO_2MONTHS() {
	return R49_31DAYS_UPTO_2MONTHS;
}








public void setR49_31DAYS_UPTO_2MONTHS(BigDecimal r49_31days_UPTO_2MONTHS) {
	R49_31DAYS_UPTO_2MONTHS = r49_31days_UPTO_2MONTHS;
}








public BigDecimal getR49_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R49_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR49_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r49_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R49_MORETHAN_2MONTHS_UPTO_3MONHTS = r49_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR49_OVER_3MONTHS_UPTO_6MONTHS() {
	return R49_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR49_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r49_OVER_3MONTHS_UPTO_6MONTHS) {
	R49_OVER_3MONTHS_UPTO_6MONTHS = r49_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR49_OVER_6MONTHS_UPTO_1YEAR() {
	return R49_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR49_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r49_OVER_6MONTHS_UPTO_1YEAR) {
	R49_OVER_6MONTHS_UPTO_1YEAR = r49_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR49_OVER_1YEAR_UPTO_3YEARS() {
	return R49_OVER_1YEAR_UPTO_3YEARS;
}








public void setR49_OVER_1YEAR_UPTO_3YEARS(BigDecimal r49_OVER_1YEAR_UPTO_3YEARS) {
	R49_OVER_1YEAR_UPTO_3YEARS = r49_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR49_OVER_3YEARS_UPTO_5YEARS() {
	return R49_OVER_3YEARS_UPTO_5YEARS;
}








public void setR49_OVER_3YEARS_UPTO_5YEARS(BigDecimal r49_OVER_3YEARS_UPTO_5YEARS) {
	R49_OVER_3YEARS_UPTO_5YEARS = r49_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR49_OVER_5YEARS() {
	return R49_OVER_5YEARS;
}








public void setR49_OVER_5YEARS(BigDecimal r49_OVER_5YEARS) {
	R49_OVER_5YEARS = r49_OVER_5YEARS;
}








public BigDecimal getR49_TOTAL() {
	return R49_TOTAL;
}








public void setR49_TOTAL(BigDecimal r49_TOTAL) {
	R49_TOTAL = r49_TOTAL;
}








public BigDecimal getR50_1_DAY() {
	return R50_1_DAY;
}








public void setR50_1_DAY(BigDecimal r50_1_DAY) {
	R50_1_DAY = r50_1_DAY;
}








public BigDecimal getR50_2TO7_DAYS() {
	return R50_2TO7_DAYS;
}








public void setR50_2TO7_DAYS(BigDecimal r50_2to7_DAYS) {
	R50_2TO7_DAYS = r50_2to7_DAYS;
}








public BigDecimal getR50_8TO14_DAYS() {
	return R50_8TO14_DAYS;
}








public void setR50_8TO14_DAYS(BigDecimal r50_8to14_DAYS) {
	R50_8TO14_DAYS = r50_8to14_DAYS;
}








public BigDecimal getR50_15TO30_DAYS() {
	return R50_15TO30_DAYS;
}








public void setR50_15TO30_DAYS(BigDecimal r50_15to30_DAYS) {
	R50_15TO30_DAYS = r50_15to30_DAYS;
}








public BigDecimal getR50_31DAYS_UPTO_2MONTHS() {
	return R50_31DAYS_UPTO_2MONTHS;
}








public void setR50_31DAYS_UPTO_2MONTHS(BigDecimal r50_31days_UPTO_2MONTHS) {
	R50_31DAYS_UPTO_2MONTHS = r50_31days_UPTO_2MONTHS;
}








public BigDecimal getR50_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R50_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR50_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r50_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R50_MORETHAN_2MONTHS_UPTO_3MONHTS = r50_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR50_OVER_3MONTHS_UPTO_6MONTHS() {
	return R50_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR50_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r50_OVER_3MONTHS_UPTO_6MONTHS) {
	R50_OVER_3MONTHS_UPTO_6MONTHS = r50_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR50_OVER_6MONTHS_UPTO_1YEAR() {
	return R50_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR50_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r50_OVER_6MONTHS_UPTO_1YEAR) {
	R50_OVER_6MONTHS_UPTO_1YEAR = r50_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR50_OVER_1YEAR_UPTO_3YEARS() {
	return R50_OVER_1YEAR_UPTO_3YEARS;
}








public void setR50_OVER_1YEAR_UPTO_3YEARS(BigDecimal r50_OVER_1YEAR_UPTO_3YEARS) {
	R50_OVER_1YEAR_UPTO_3YEARS = r50_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR50_OVER_3YEARS_UPTO_5YEARS() {
	return R50_OVER_3YEARS_UPTO_5YEARS;
}








public void setR50_OVER_3YEARS_UPTO_5YEARS(BigDecimal r50_OVER_3YEARS_UPTO_5YEARS) {
	R50_OVER_3YEARS_UPTO_5YEARS = r50_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR50_OVER_5YEARS() {
	return R50_OVER_5YEARS;
}








public void setR50_OVER_5YEARS(BigDecimal r50_OVER_5YEARS) {
	R50_OVER_5YEARS = r50_OVER_5YEARS;
}








public BigDecimal getR50_TOTAL() {
	return R50_TOTAL;
}








public void setR50_TOTAL(BigDecimal r50_TOTAL) {
	R50_TOTAL = r50_TOTAL;
}








public BigDecimal getR51_1_DAY() {
	return R51_1_DAY;
}








public void setR51_1_DAY(BigDecimal r51_1_DAY) {
	R51_1_DAY = r51_1_DAY;
}








public BigDecimal getR51_2TO7_DAYS() {
	return R51_2TO7_DAYS;
}








public void setR51_2TO7_DAYS(BigDecimal r51_2to7_DAYS) {
	R51_2TO7_DAYS = r51_2to7_DAYS;
}








public BigDecimal getR51_8TO14_DAYS() {
	return R51_8TO14_DAYS;
}








public void setR51_8TO14_DAYS(BigDecimal r51_8to14_DAYS) {
	R51_8TO14_DAYS = r51_8to14_DAYS;
}








public BigDecimal getR51_15TO30_DAYS() {
	return R51_15TO30_DAYS;
}








public void setR51_15TO30_DAYS(BigDecimal r51_15to30_DAYS) {
	R51_15TO30_DAYS = r51_15to30_DAYS;
}








public BigDecimal getR51_31DAYS_UPTO_2MONTHS() {
	return R51_31DAYS_UPTO_2MONTHS;
}








public void setR51_31DAYS_UPTO_2MONTHS(BigDecimal r51_31days_UPTO_2MONTHS) {
	R51_31DAYS_UPTO_2MONTHS = r51_31days_UPTO_2MONTHS;
}








public BigDecimal getR51_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R51_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR51_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r51_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R51_MORETHAN_2MONTHS_UPTO_3MONHTS = r51_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR51_OVER_3MONTHS_UPTO_6MONTHS() {
	return R51_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR51_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r51_OVER_3MONTHS_UPTO_6MONTHS) {
	R51_OVER_3MONTHS_UPTO_6MONTHS = r51_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR51_OVER_6MONTHS_UPTO_1YEAR() {
	return R51_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR51_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r51_OVER_6MONTHS_UPTO_1YEAR) {
	R51_OVER_6MONTHS_UPTO_1YEAR = r51_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR51_OVER_1YEAR_UPTO_3YEARS() {
	return R51_OVER_1YEAR_UPTO_3YEARS;
}








public void setR51_OVER_1YEAR_UPTO_3YEARS(BigDecimal r51_OVER_1YEAR_UPTO_3YEARS) {
	R51_OVER_1YEAR_UPTO_3YEARS = r51_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR51_OVER_3YEARS_UPTO_5YEARS() {
	return R51_OVER_3YEARS_UPTO_5YEARS;
}








public void setR51_OVER_3YEARS_UPTO_5YEARS(BigDecimal r51_OVER_3YEARS_UPTO_5YEARS) {
	R51_OVER_3YEARS_UPTO_5YEARS = r51_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR51_OVER_5YEARS() {
	return R51_OVER_5YEARS;
}








public void setR51_OVER_5YEARS(BigDecimal r51_OVER_5YEARS) {
	R51_OVER_5YEARS = r51_OVER_5YEARS;
}








public BigDecimal getR51_TOTAL() {
	return R51_TOTAL;
}








public void setR51_TOTAL(BigDecimal r51_TOTAL) {
	R51_TOTAL = r51_TOTAL;
}








public BigDecimal getR52_1_DAY() {
	return R52_1_DAY;
}








public void setR52_1_DAY(BigDecimal r52_1_DAY) {
	R52_1_DAY = r52_1_DAY;
}








public BigDecimal getR52_2TO7_DAYS() {
	return R52_2TO7_DAYS;
}








public void setR52_2TO7_DAYS(BigDecimal r52_2to7_DAYS) {
	R52_2TO7_DAYS = r52_2to7_DAYS;
}








public BigDecimal getR52_8TO14_DAYS() {
	return R52_8TO14_DAYS;
}








public void setR52_8TO14_DAYS(BigDecimal r52_8to14_DAYS) {
	R52_8TO14_DAYS = r52_8to14_DAYS;
}








public BigDecimal getR52_15TO30_DAYS() {
	return R52_15TO30_DAYS;
}








public void setR52_15TO30_DAYS(BigDecimal r52_15to30_DAYS) {
	R52_15TO30_DAYS = r52_15to30_DAYS;
}








public BigDecimal getR52_31DAYS_UPTO_2MONTHS() {
	return R52_31DAYS_UPTO_2MONTHS;
}








public void setR52_31DAYS_UPTO_2MONTHS(BigDecimal r52_31days_UPTO_2MONTHS) {
	R52_31DAYS_UPTO_2MONTHS = r52_31days_UPTO_2MONTHS;
}








public BigDecimal getR52_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R52_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR52_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r52_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R52_MORETHAN_2MONTHS_UPTO_3MONHTS = r52_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR52_OVER_3MONTHS_UPTO_6MONTHS() {
	return R52_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR52_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r52_OVER_3MONTHS_UPTO_6MONTHS) {
	R52_OVER_3MONTHS_UPTO_6MONTHS = r52_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR52_OVER_6MONTHS_UPTO_1YEAR() {
	return R52_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR52_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r52_OVER_6MONTHS_UPTO_1YEAR) {
	R52_OVER_6MONTHS_UPTO_1YEAR = r52_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR52_OVER_1YEAR_UPTO_3YEARS() {
	return R52_OVER_1YEAR_UPTO_3YEARS;
}








public void setR52_OVER_1YEAR_UPTO_3YEARS(BigDecimal r52_OVER_1YEAR_UPTO_3YEARS) {
	R52_OVER_1YEAR_UPTO_3YEARS = r52_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR52_OVER_3YEARS_UPTO_5YEARS() {
	return R52_OVER_3YEARS_UPTO_5YEARS;
}








public void setR52_OVER_3YEARS_UPTO_5YEARS(BigDecimal r52_OVER_3YEARS_UPTO_5YEARS) {
	R52_OVER_3YEARS_UPTO_5YEARS = r52_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR52_OVER_5YEARS() {
	return R52_OVER_5YEARS;
}








public void setR52_OVER_5YEARS(BigDecimal r52_OVER_5YEARS) {
	R52_OVER_5YEARS = r52_OVER_5YEARS;
}








public BigDecimal getR52_TOTAL() {
	return R52_TOTAL;
}








public void setR52_TOTAL(BigDecimal r52_TOTAL) {
	R52_TOTAL = r52_TOTAL;
}








public BigDecimal getR53_1_DAY() {
	return R53_1_DAY;
}








public void setR53_1_DAY(BigDecimal r53_1_DAY) {
	R53_1_DAY = r53_1_DAY;
}








public BigDecimal getR53_2TO7_DAYS() {
	return R53_2TO7_DAYS;
}








public void setR53_2TO7_DAYS(BigDecimal r53_2to7_DAYS) {
	R53_2TO7_DAYS = r53_2to7_DAYS;
}








public BigDecimal getR53_8TO14_DAYS() {
	return R53_8TO14_DAYS;
}








public void setR53_8TO14_DAYS(BigDecimal r53_8to14_DAYS) {
	R53_8TO14_DAYS = r53_8to14_DAYS;
}








public BigDecimal getR53_15TO30_DAYS() {
	return R53_15TO30_DAYS;
}








public void setR53_15TO30_DAYS(BigDecimal r53_15to30_DAYS) {
	R53_15TO30_DAYS = r53_15to30_DAYS;
}








public BigDecimal getR53_31DAYS_UPTO_2MONTHS() {
	return R53_31DAYS_UPTO_2MONTHS;
}








public void setR53_31DAYS_UPTO_2MONTHS(BigDecimal r53_31days_UPTO_2MONTHS) {
	R53_31DAYS_UPTO_2MONTHS = r53_31days_UPTO_2MONTHS;
}








public BigDecimal getR53_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R53_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR53_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r53_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R53_MORETHAN_2MONTHS_UPTO_3MONHTS = r53_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR53_OVER_3MONTHS_UPTO_6MONTHS() {
	return R53_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR53_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r53_OVER_3MONTHS_UPTO_6MONTHS) {
	R53_OVER_3MONTHS_UPTO_6MONTHS = r53_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR53_OVER_6MONTHS_UPTO_1YEAR() {
	return R53_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR53_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r53_OVER_6MONTHS_UPTO_1YEAR) {
	R53_OVER_6MONTHS_UPTO_1YEAR = r53_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR53_OVER_1YEAR_UPTO_3YEARS() {
	return R53_OVER_1YEAR_UPTO_3YEARS;
}








public void setR53_OVER_1YEAR_UPTO_3YEARS(BigDecimal r53_OVER_1YEAR_UPTO_3YEARS) {
	R53_OVER_1YEAR_UPTO_3YEARS = r53_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR53_OVER_3YEARS_UPTO_5YEARS() {
	return R53_OVER_3YEARS_UPTO_5YEARS;
}








public void setR53_OVER_3YEARS_UPTO_5YEARS(BigDecimal r53_OVER_3YEARS_UPTO_5YEARS) {
	R53_OVER_3YEARS_UPTO_5YEARS = r53_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR53_OVER_5YEARS() {
	return R53_OVER_5YEARS;
}








public void setR53_OVER_5YEARS(BigDecimal r53_OVER_5YEARS) {
	R53_OVER_5YEARS = r53_OVER_5YEARS;
}








public BigDecimal getR53_TOTAL() {
	return R53_TOTAL;
}








public void setR53_TOTAL(BigDecimal r53_TOTAL) {
	R53_TOTAL = r53_TOTAL;
}








public BigDecimal getR54_1_DAY() {
	return R54_1_DAY;
}








public void setR54_1_DAY(BigDecimal r54_1_DAY) {
	R54_1_DAY = r54_1_DAY;
}








public BigDecimal getR54_2TO7_DAYS() {
	return R54_2TO7_DAYS;
}








public void setR54_2TO7_DAYS(BigDecimal r54_2to7_DAYS) {
	R54_2TO7_DAYS = r54_2to7_DAYS;
}








public BigDecimal getR54_8TO14_DAYS() {
	return R54_8TO14_DAYS;
}








public void setR54_8TO14_DAYS(BigDecimal r54_8to14_DAYS) {
	R54_8TO14_DAYS = r54_8to14_DAYS;
}








public BigDecimal getR54_15TO30_DAYS() {
	return R54_15TO30_DAYS;
}








public void setR54_15TO30_DAYS(BigDecimal r54_15to30_DAYS) {
	R54_15TO30_DAYS = r54_15to30_DAYS;
}








public BigDecimal getR54_31DAYS_UPTO_2MONTHS() {
	return R54_31DAYS_UPTO_2MONTHS;
}








public void setR54_31DAYS_UPTO_2MONTHS(BigDecimal r54_31days_UPTO_2MONTHS) {
	R54_31DAYS_UPTO_2MONTHS = r54_31days_UPTO_2MONTHS;
}








public BigDecimal getR54_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R54_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR54_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r54_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R54_MORETHAN_2MONTHS_UPTO_3MONHTS = r54_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR54_OVER_3MONTHS_UPTO_6MONTHS() {
	return R54_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR54_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r54_OVER_3MONTHS_UPTO_6MONTHS) {
	R54_OVER_3MONTHS_UPTO_6MONTHS = r54_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR54_OVER_6MONTHS_UPTO_1YEAR() {
	return R54_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR54_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r54_OVER_6MONTHS_UPTO_1YEAR) {
	R54_OVER_6MONTHS_UPTO_1YEAR = r54_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR54_OVER_1YEAR_UPTO_3YEARS() {
	return R54_OVER_1YEAR_UPTO_3YEARS;
}








public void setR54_OVER_1YEAR_UPTO_3YEARS(BigDecimal r54_OVER_1YEAR_UPTO_3YEARS) {
	R54_OVER_1YEAR_UPTO_3YEARS = r54_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR54_OVER_3YEARS_UPTO_5YEARS() {
	return R54_OVER_3YEARS_UPTO_5YEARS;
}








public void setR54_OVER_3YEARS_UPTO_5YEARS(BigDecimal r54_OVER_3YEARS_UPTO_5YEARS) {
	R54_OVER_3YEARS_UPTO_5YEARS = r54_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR54_OVER_5YEARS() {
	return R54_OVER_5YEARS;
}








public void setR54_OVER_5YEARS(BigDecimal r54_OVER_5YEARS) {
	R54_OVER_5YEARS = r54_OVER_5YEARS;
}








public BigDecimal getR54_TOTAL() {
	return R54_TOTAL;
}








public void setR54_TOTAL(BigDecimal r54_TOTAL) {
	R54_TOTAL = r54_TOTAL;
}








public BigDecimal getR55_1_DAY() {
	return R55_1_DAY;
}








public void setR55_1_DAY(BigDecimal r55_1_DAY) {
	R55_1_DAY = r55_1_DAY;
}








public BigDecimal getR55_2TO7_DAYS() {
	return R55_2TO7_DAYS;
}








public void setR55_2TO7_DAYS(BigDecimal r55_2to7_DAYS) {
	R55_2TO7_DAYS = r55_2to7_DAYS;
}








public BigDecimal getR55_8TO14_DAYS() {
	return R55_8TO14_DAYS;
}








public void setR55_8TO14_DAYS(BigDecimal r55_8to14_DAYS) {
	R55_8TO14_DAYS = r55_8to14_DAYS;
}








public BigDecimal getR55_15TO30_DAYS() {
	return R55_15TO30_DAYS;
}








public void setR55_15TO30_DAYS(BigDecimal r55_15to30_DAYS) {
	R55_15TO30_DAYS = r55_15to30_DAYS;
}








public BigDecimal getR55_31DAYS_UPTO_2MONTHS() {
	return R55_31DAYS_UPTO_2MONTHS;
}








public void setR55_31DAYS_UPTO_2MONTHS(BigDecimal r55_31days_UPTO_2MONTHS) {
	R55_31DAYS_UPTO_2MONTHS = r55_31days_UPTO_2MONTHS;
}








public BigDecimal getR55_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R55_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR55_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r55_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R55_MORETHAN_2MONTHS_UPTO_3MONHTS = r55_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR55_OVER_3MONTHS_UPTO_6MONTHS() {
	return R55_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR55_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r55_OVER_3MONTHS_UPTO_6MONTHS) {
	R55_OVER_3MONTHS_UPTO_6MONTHS = r55_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR55_OVER_6MONTHS_UPTO_1YEAR() {
	return R55_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR55_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r55_OVER_6MONTHS_UPTO_1YEAR) {
	R55_OVER_6MONTHS_UPTO_1YEAR = r55_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR55_OVER_1YEAR_UPTO_3YEARS() {
	return R55_OVER_1YEAR_UPTO_3YEARS;
}








public void setR55_OVER_1YEAR_UPTO_3YEARS(BigDecimal r55_OVER_1YEAR_UPTO_3YEARS) {
	R55_OVER_1YEAR_UPTO_3YEARS = r55_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR55_OVER_3YEARS_UPTO_5YEARS() {
	return R55_OVER_3YEARS_UPTO_5YEARS;
}








public void setR55_OVER_3YEARS_UPTO_5YEARS(BigDecimal r55_OVER_3YEARS_UPTO_5YEARS) {
	R55_OVER_3YEARS_UPTO_5YEARS = r55_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR55_OVER_5YEARS() {
	return R55_OVER_5YEARS;
}








public void setR55_OVER_5YEARS(BigDecimal r55_OVER_5YEARS) {
	R55_OVER_5YEARS = r55_OVER_5YEARS;
}








public BigDecimal getR55_TOTAL() {
	return R55_TOTAL;
}








public void setR55_TOTAL(BigDecimal r55_TOTAL) {
	R55_TOTAL = r55_TOTAL;
}








public BigDecimal getR56_1_DAY() {
	return R56_1_DAY;
}








public void setR56_1_DAY(BigDecimal r56_1_DAY) {
	R56_1_DAY = r56_1_DAY;
}








public BigDecimal getR56_2TO7_DAYS() {
	return R56_2TO7_DAYS;
}








public void setR56_2TO7_DAYS(BigDecimal r56_2to7_DAYS) {
	R56_2TO7_DAYS = r56_2to7_DAYS;
}








public BigDecimal getR56_8TO14_DAYS() {
	return R56_8TO14_DAYS;
}








public void setR56_8TO14_DAYS(BigDecimal r56_8to14_DAYS) {
	R56_8TO14_DAYS = r56_8to14_DAYS;
}








public BigDecimal getR56_15TO30_DAYS() {
	return R56_15TO30_DAYS;
}








public void setR56_15TO30_DAYS(BigDecimal r56_15to30_DAYS) {
	R56_15TO30_DAYS = r56_15to30_DAYS;
}








public BigDecimal getR56_31DAYS_UPTO_2MONTHS() {
	return R56_31DAYS_UPTO_2MONTHS;
}








public void setR56_31DAYS_UPTO_2MONTHS(BigDecimal r56_31days_UPTO_2MONTHS) {
	R56_31DAYS_UPTO_2MONTHS = r56_31days_UPTO_2MONTHS;
}








public BigDecimal getR56_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R56_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR56_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r56_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R56_MORETHAN_2MONTHS_UPTO_3MONHTS = r56_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR56_OVER_3MONTHS_UPTO_6MONTHS() {
	return R56_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR56_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r56_OVER_3MONTHS_UPTO_6MONTHS) {
	R56_OVER_3MONTHS_UPTO_6MONTHS = r56_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR56_OVER_6MONTHS_UPTO_1YEAR() {
	return R56_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR56_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r56_OVER_6MONTHS_UPTO_1YEAR) {
	R56_OVER_6MONTHS_UPTO_1YEAR = r56_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR56_OVER_1YEAR_UPTO_3YEARS() {
	return R56_OVER_1YEAR_UPTO_3YEARS;
}








public void setR56_OVER_1YEAR_UPTO_3YEARS(BigDecimal r56_OVER_1YEAR_UPTO_3YEARS) {
	R56_OVER_1YEAR_UPTO_3YEARS = r56_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR56_OVER_3YEARS_UPTO_5YEARS() {
	return R56_OVER_3YEARS_UPTO_5YEARS;
}








public void setR56_OVER_3YEARS_UPTO_5YEARS(BigDecimal r56_OVER_3YEARS_UPTO_5YEARS) {
	R56_OVER_3YEARS_UPTO_5YEARS = r56_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR56_OVER_5YEARS() {
	return R56_OVER_5YEARS;
}








public void setR56_OVER_5YEARS(BigDecimal r56_OVER_5YEARS) {
	R56_OVER_5YEARS = r56_OVER_5YEARS;
}








public BigDecimal getR56_TOTAL() {
	return R56_TOTAL;
}








public void setR56_TOTAL(BigDecimal r56_TOTAL) {
	R56_TOTAL = r56_TOTAL;
}








public BigDecimal getR57_1_DAY() {
	return R57_1_DAY;
}








public void setR57_1_DAY(BigDecimal r57_1_DAY) {
	R57_1_DAY = r57_1_DAY;
}








public BigDecimal getR57_2TO7_DAYS() {
	return R57_2TO7_DAYS;
}








public void setR57_2TO7_DAYS(BigDecimal r57_2to7_DAYS) {
	R57_2TO7_DAYS = r57_2to7_DAYS;
}








public BigDecimal getR57_8TO14_DAYS() {
	return R57_8TO14_DAYS;
}








public void setR57_8TO14_DAYS(BigDecimal r57_8to14_DAYS) {
	R57_8TO14_DAYS = r57_8to14_DAYS;
}








public BigDecimal getR57_15TO30_DAYS() {
	return R57_15TO30_DAYS;
}








public void setR57_15TO30_DAYS(BigDecimal r57_15to30_DAYS) {
	R57_15TO30_DAYS = r57_15to30_DAYS;
}








public BigDecimal getR57_31DAYS_UPTO_2MONTHS() {
	return R57_31DAYS_UPTO_2MONTHS;
}








public void setR57_31DAYS_UPTO_2MONTHS(BigDecimal r57_31days_UPTO_2MONTHS) {
	R57_31DAYS_UPTO_2MONTHS = r57_31days_UPTO_2MONTHS;
}








public BigDecimal getR57_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R57_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR57_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r57_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R57_MORETHAN_2MONTHS_UPTO_3MONHTS = r57_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR57_OVER_3MONTHS_UPTO_6MONTHS() {
	return R57_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR57_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r57_OVER_3MONTHS_UPTO_6MONTHS) {
	R57_OVER_3MONTHS_UPTO_6MONTHS = r57_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR57_OVER_6MONTHS_UPTO_1YEAR() {
	return R57_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR57_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r57_OVER_6MONTHS_UPTO_1YEAR) {
	R57_OVER_6MONTHS_UPTO_1YEAR = r57_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR57_OVER_1YEAR_UPTO_3YEARS() {
	return R57_OVER_1YEAR_UPTO_3YEARS;
}








public void setR57_OVER_1YEAR_UPTO_3YEARS(BigDecimal r57_OVER_1YEAR_UPTO_3YEARS) {
	R57_OVER_1YEAR_UPTO_3YEARS = r57_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR57_OVER_3YEARS_UPTO_5YEARS() {
	return R57_OVER_3YEARS_UPTO_5YEARS;
}








public void setR57_OVER_3YEARS_UPTO_5YEARS(BigDecimal r57_OVER_3YEARS_UPTO_5YEARS) {
	R57_OVER_3YEARS_UPTO_5YEARS = r57_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR57_OVER_5YEARS() {
	return R57_OVER_5YEARS;
}








public void setR57_OVER_5YEARS(BigDecimal r57_OVER_5YEARS) {
	R57_OVER_5YEARS = r57_OVER_5YEARS;
}








public BigDecimal getR57_TOTAL() {
	return R57_TOTAL;
}








public void setR57_TOTAL(BigDecimal r57_TOTAL) {
	R57_TOTAL = r57_TOTAL;
}








public BigDecimal getR58_1_DAY() {
	return R58_1_DAY;
}








public void setR58_1_DAY(BigDecimal r58_1_DAY) {
	R58_1_DAY = r58_1_DAY;
}








public BigDecimal getR58_2TO7_DAYS() {
	return R58_2TO7_DAYS;
}








public void setR58_2TO7_DAYS(BigDecimal r58_2to7_DAYS) {
	R58_2TO7_DAYS = r58_2to7_DAYS;
}








public BigDecimal getR58_8TO14_DAYS() {
	return R58_8TO14_DAYS;
}








public void setR58_8TO14_DAYS(BigDecimal r58_8to14_DAYS) {
	R58_8TO14_DAYS = r58_8to14_DAYS;
}








public BigDecimal getR58_15TO30_DAYS() {
	return R58_15TO30_DAYS;
}








public void setR58_15TO30_DAYS(BigDecimal r58_15to30_DAYS) {
	R58_15TO30_DAYS = r58_15to30_DAYS;
}








public BigDecimal getR58_31DAYS_UPTO_2MONTHS() {
	return R58_31DAYS_UPTO_2MONTHS;
}








public void setR58_31DAYS_UPTO_2MONTHS(BigDecimal r58_31days_UPTO_2MONTHS) {
	R58_31DAYS_UPTO_2MONTHS = r58_31days_UPTO_2MONTHS;
}








public BigDecimal getR58_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R58_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR58_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r58_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R58_MORETHAN_2MONTHS_UPTO_3MONHTS = r58_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR58_OVER_3MONTHS_UPTO_6MONTHS() {
	return R58_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR58_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r58_OVER_3MONTHS_UPTO_6MONTHS) {
	R58_OVER_3MONTHS_UPTO_6MONTHS = r58_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR58_OVER_6MONTHS_UPTO_1YEAR() {
	return R58_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR58_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r58_OVER_6MONTHS_UPTO_1YEAR) {
	R58_OVER_6MONTHS_UPTO_1YEAR = r58_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR58_OVER_1YEAR_UPTO_3YEARS() {
	return R58_OVER_1YEAR_UPTO_3YEARS;
}








public void setR58_OVER_1YEAR_UPTO_3YEARS(BigDecimal r58_OVER_1YEAR_UPTO_3YEARS) {
	R58_OVER_1YEAR_UPTO_3YEARS = r58_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR58_OVER_3YEARS_UPTO_5YEARS() {
	return R58_OVER_3YEARS_UPTO_5YEARS;
}








public void setR58_OVER_3YEARS_UPTO_5YEARS(BigDecimal r58_OVER_3YEARS_UPTO_5YEARS) {
	R58_OVER_3YEARS_UPTO_5YEARS = r58_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR58_OVER_5YEARS() {
	return R58_OVER_5YEARS;
}








public void setR58_OVER_5YEARS(BigDecimal r58_OVER_5YEARS) {
	R58_OVER_5YEARS = r58_OVER_5YEARS;
}








public BigDecimal getR58_TOTAL() {
	return R58_TOTAL;
}








public void setR58_TOTAL(BigDecimal r58_TOTAL) {
	R58_TOTAL = r58_TOTAL;
}








public BigDecimal getR59_1_DAY() {
	return R59_1_DAY;
}








public void setR59_1_DAY(BigDecimal r59_1_DAY) {
	R59_1_DAY = r59_1_DAY;
}








public BigDecimal getR59_2TO7_DAYS() {
	return R59_2TO7_DAYS;
}








public void setR59_2TO7_DAYS(BigDecimal r59_2to7_DAYS) {
	R59_2TO7_DAYS = r59_2to7_DAYS;
}








public BigDecimal getR59_8TO14_DAYS() {
	return R59_8TO14_DAYS;
}








public void setR59_8TO14_DAYS(BigDecimal r59_8to14_DAYS) {
	R59_8TO14_DAYS = r59_8to14_DAYS;
}








public BigDecimal getR59_15TO30_DAYS() {
	return R59_15TO30_DAYS;
}








public void setR59_15TO30_DAYS(BigDecimal r59_15to30_DAYS) {
	R59_15TO30_DAYS = r59_15to30_DAYS;
}








public BigDecimal getR59_31DAYS_UPTO_2MONTHS() {
	return R59_31DAYS_UPTO_2MONTHS;
}








public void setR59_31DAYS_UPTO_2MONTHS(BigDecimal r59_31days_UPTO_2MONTHS) {
	R59_31DAYS_UPTO_2MONTHS = r59_31days_UPTO_2MONTHS;
}








public BigDecimal getR59_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R59_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR59_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r59_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R59_MORETHAN_2MONTHS_UPTO_3MONHTS = r59_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR59_OVER_3MONTHS_UPTO_6MONTHS() {
	return R59_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR59_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r59_OVER_3MONTHS_UPTO_6MONTHS) {
	R59_OVER_3MONTHS_UPTO_6MONTHS = r59_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR59_OVER_6MONTHS_UPTO_1YEAR() {
	return R59_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR59_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r59_OVER_6MONTHS_UPTO_1YEAR) {
	R59_OVER_6MONTHS_UPTO_1YEAR = r59_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR59_OVER_1YEAR_UPTO_3YEARS() {
	return R59_OVER_1YEAR_UPTO_3YEARS;
}








public void setR59_OVER_1YEAR_UPTO_3YEARS(BigDecimal r59_OVER_1YEAR_UPTO_3YEARS) {
	R59_OVER_1YEAR_UPTO_3YEARS = r59_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR59_OVER_3YEARS_UPTO_5YEARS() {
	return R59_OVER_3YEARS_UPTO_5YEARS;
}








public void setR59_OVER_3YEARS_UPTO_5YEARS(BigDecimal r59_OVER_3YEARS_UPTO_5YEARS) {
	R59_OVER_3YEARS_UPTO_5YEARS = r59_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR59_OVER_5YEARS() {
	return R59_OVER_5YEARS;
}








public void setR59_OVER_5YEARS(BigDecimal r59_OVER_5YEARS) {
	R59_OVER_5YEARS = r59_OVER_5YEARS;
}








public BigDecimal getR59_TOTAL() {
	return R59_TOTAL;
}








public void setR59_TOTAL(BigDecimal r59_TOTAL) {
	R59_TOTAL = r59_TOTAL;
}








public BigDecimal getR60_1_DAY() {
	return R60_1_DAY;
}








public void setR60_1_DAY(BigDecimal r60_1_DAY) {
	R60_1_DAY = r60_1_DAY;
}








public BigDecimal getR60_2TO7_DAYS() {
	return R60_2TO7_DAYS;
}








public void setR60_2TO7_DAYS(BigDecimal r60_2to7_DAYS) {
	R60_2TO7_DAYS = r60_2to7_DAYS;
}








public BigDecimal getR60_8TO14_DAYS() {
	return R60_8TO14_DAYS;
}








public void setR60_8TO14_DAYS(BigDecimal r60_8to14_DAYS) {
	R60_8TO14_DAYS = r60_8to14_DAYS;
}








public BigDecimal getR60_15TO30_DAYS() {
	return R60_15TO30_DAYS;
}








public void setR60_15TO30_DAYS(BigDecimal r60_15to30_DAYS) {
	R60_15TO30_DAYS = r60_15to30_DAYS;
}








public BigDecimal getR60_31DAYS_UPTO_2MONTHS() {
	return R60_31DAYS_UPTO_2MONTHS;
}








public void setR60_31DAYS_UPTO_2MONTHS(BigDecimal r60_31days_UPTO_2MONTHS) {
	R60_31DAYS_UPTO_2MONTHS = r60_31days_UPTO_2MONTHS;
}








public BigDecimal getR60_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R60_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR60_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r60_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R60_MORETHAN_2MONTHS_UPTO_3MONHTS = r60_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR60_OVER_3MONTHS_UPTO_6MONTHS() {
	return R60_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR60_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r60_OVER_3MONTHS_UPTO_6MONTHS) {
	R60_OVER_3MONTHS_UPTO_6MONTHS = r60_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR60_OVER_6MONTHS_UPTO_1YEAR() {
	return R60_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR60_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r60_OVER_6MONTHS_UPTO_1YEAR) {
	R60_OVER_6MONTHS_UPTO_1YEAR = r60_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR60_OVER_1YEAR_UPTO_3YEARS() {
	return R60_OVER_1YEAR_UPTO_3YEARS;
}








public void setR60_OVER_1YEAR_UPTO_3YEARS(BigDecimal r60_OVER_1YEAR_UPTO_3YEARS) {
	R60_OVER_1YEAR_UPTO_3YEARS = r60_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR60_OVER_3YEARS_UPTO_5YEARS() {
	return R60_OVER_3YEARS_UPTO_5YEARS;
}








public void setR60_OVER_3YEARS_UPTO_5YEARS(BigDecimal r60_OVER_3YEARS_UPTO_5YEARS) {
	R60_OVER_3YEARS_UPTO_5YEARS = r60_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR60_OVER_5YEARS() {
	return R60_OVER_5YEARS;
}








public void setR60_OVER_5YEARS(BigDecimal r60_OVER_5YEARS) {
	R60_OVER_5YEARS = r60_OVER_5YEARS;
}








public BigDecimal getR60_TOTAL() {
	return R60_TOTAL;
}








public void setR60_TOTAL(BigDecimal r60_TOTAL) {
	R60_TOTAL = r60_TOTAL;
}








public BigDecimal getR61_1_DAY() {
	return R61_1_DAY;
}








public void setR61_1_DAY(BigDecimal r61_1_DAY) {
	R61_1_DAY = r61_1_DAY;
}








public BigDecimal getR61_2TO7_DAYS() {
	return R61_2TO7_DAYS;
}








public void setR61_2TO7_DAYS(BigDecimal r61_2to7_DAYS) {
	R61_2TO7_DAYS = r61_2to7_DAYS;
}








public BigDecimal getR61_8TO14_DAYS() {
	return R61_8TO14_DAYS;
}








public void setR61_8TO14_DAYS(BigDecimal r61_8to14_DAYS) {
	R61_8TO14_DAYS = r61_8to14_DAYS;
}








public BigDecimal getR61_15TO30_DAYS() {
	return R61_15TO30_DAYS;
}








public void setR61_15TO30_DAYS(BigDecimal r61_15to30_DAYS) {
	R61_15TO30_DAYS = r61_15to30_DAYS;
}








public BigDecimal getR61_31DAYS_UPTO_2MONTHS() {
	return R61_31DAYS_UPTO_2MONTHS;
}








public void setR61_31DAYS_UPTO_2MONTHS(BigDecimal r61_31days_UPTO_2MONTHS) {
	R61_31DAYS_UPTO_2MONTHS = r61_31days_UPTO_2MONTHS;
}








public BigDecimal getR61_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R61_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR61_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r61_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R61_MORETHAN_2MONTHS_UPTO_3MONHTS = r61_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR61_OVER_3MONTHS_UPTO_6MONTHS() {
	return R61_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR61_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r61_OVER_3MONTHS_UPTO_6MONTHS) {
	R61_OVER_3MONTHS_UPTO_6MONTHS = r61_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR61_OVER_6MONTHS_UPTO_1YEAR() {
	return R61_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR61_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r61_OVER_6MONTHS_UPTO_1YEAR) {
	R61_OVER_6MONTHS_UPTO_1YEAR = r61_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR61_OVER_1YEAR_UPTO_3YEARS() {
	return R61_OVER_1YEAR_UPTO_3YEARS;
}








public void setR61_OVER_1YEAR_UPTO_3YEARS(BigDecimal r61_OVER_1YEAR_UPTO_3YEARS) {
	R61_OVER_1YEAR_UPTO_3YEARS = r61_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR61_OVER_3YEARS_UPTO_5YEARS() {
	return R61_OVER_3YEARS_UPTO_5YEARS;
}








public void setR61_OVER_3YEARS_UPTO_5YEARS(BigDecimal r61_OVER_3YEARS_UPTO_5YEARS) {
	R61_OVER_3YEARS_UPTO_5YEARS = r61_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR61_OVER_5YEARS() {
	return R61_OVER_5YEARS;
}








public void setR61_OVER_5YEARS(BigDecimal r61_OVER_5YEARS) {
	R61_OVER_5YEARS = r61_OVER_5YEARS;
}








public BigDecimal getR61_TOTAL() {
	return R61_TOTAL;
}








public void setR61_TOTAL(BigDecimal r61_TOTAL) {
	R61_TOTAL = r61_TOTAL;
}








public BigDecimal getR62_1_DAY() {
	return R62_1_DAY;
}








public void setR62_1_DAY(BigDecimal r62_1_DAY) {
	R62_1_DAY = r62_1_DAY;
}








public BigDecimal getR62_2TO7_DAYS() {
	return R62_2TO7_DAYS;
}








public void setR62_2TO7_DAYS(BigDecimal r62_2to7_DAYS) {
	R62_2TO7_DAYS = r62_2to7_DAYS;
}








public BigDecimal getR62_8TO14_DAYS() {
	return R62_8TO14_DAYS;
}








public void setR62_8TO14_DAYS(BigDecimal r62_8to14_DAYS) {
	R62_8TO14_DAYS = r62_8to14_DAYS;
}








public BigDecimal getR62_15TO30_DAYS() {
	return R62_15TO30_DAYS;
}








public void setR62_15TO30_DAYS(BigDecimal r62_15to30_DAYS) {
	R62_15TO30_DAYS = r62_15to30_DAYS;
}








public BigDecimal getR62_31DAYS_UPTO_2MONTHS() {
	return R62_31DAYS_UPTO_2MONTHS;
}








public void setR62_31DAYS_UPTO_2MONTHS(BigDecimal r62_31days_UPTO_2MONTHS) {
	R62_31DAYS_UPTO_2MONTHS = r62_31days_UPTO_2MONTHS;
}








public BigDecimal getR62_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R62_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR62_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r62_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R62_MORETHAN_2MONTHS_UPTO_3MONHTS = r62_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR62_OVER_3MONTHS_UPTO_6MONTHS() {
	return R62_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR62_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r62_OVER_3MONTHS_UPTO_6MONTHS) {
	R62_OVER_3MONTHS_UPTO_6MONTHS = r62_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR62_OVER_6MONTHS_UPTO_1YEAR() {
	return R62_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR62_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r62_OVER_6MONTHS_UPTO_1YEAR) {
	R62_OVER_6MONTHS_UPTO_1YEAR = r62_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR62_OVER_1YEAR_UPTO_3YEARS() {
	return R62_OVER_1YEAR_UPTO_3YEARS;
}








public void setR62_OVER_1YEAR_UPTO_3YEARS(BigDecimal r62_OVER_1YEAR_UPTO_3YEARS) {
	R62_OVER_1YEAR_UPTO_3YEARS = r62_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR62_OVER_3YEARS_UPTO_5YEARS() {
	return R62_OVER_3YEARS_UPTO_5YEARS;
}








public void setR62_OVER_3YEARS_UPTO_5YEARS(BigDecimal r62_OVER_3YEARS_UPTO_5YEARS) {
	R62_OVER_3YEARS_UPTO_5YEARS = r62_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR62_OVER_5YEARS() {
	return R62_OVER_5YEARS;
}








public void setR62_OVER_5YEARS(BigDecimal r62_OVER_5YEARS) {
	R62_OVER_5YEARS = r62_OVER_5YEARS;
}








public BigDecimal getR62_TOTAL() {
	return R62_TOTAL;
}








public void setR62_TOTAL(BigDecimal r62_TOTAL) {
	R62_TOTAL = r62_TOTAL;
}








public BigDecimal getR63_1_DAY() {
	return R63_1_DAY;
}








public void setR63_1_DAY(BigDecimal r63_1_DAY) {
	R63_1_DAY = r63_1_DAY;
}








public BigDecimal getR63_2TO7_DAYS() {
	return R63_2TO7_DAYS;
}








public void setR63_2TO7_DAYS(BigDecimal r63_2to7_DAYS) {
	R63_2TO7_DAYS = r63_2to7_DAYS;
}








public BigDecimal getR63_8TO14_DAYS() {
	return R63_8TO14_DAYS;
}








public void setR63_8TO14_DAYS(BigDecimal r63_8to14_DAYS) {
	R63_8TO14_DAYS = r63_8to14_DAYS;
}








public BigDecimal getR63_15TO30_DAYS() {
	return R63_15TO30_DAYS;
}








public void setR63_15TO30_DAYS(BigDecimal r63_15to30_DAYS) {
	R63_15TO30_DAYS = r63_15to30_DAYS;
}








public BigDecimal getR63_31DAYS_UPTO_2MONTHS() {
	return R63_31DAYS_UPTO_2MONTHS;
}








public void setR63_31DAYS_UPTO_2MONTHS(BigDecimal r63_31days_UPTO_2MONTHS) {
	R63_31DAYS_UPTO_2MONTHS = r63_31days_UPTO_2MONTHS;
}








public BigDecimal getR63_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R63_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR63_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r63_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R63_MORETHAN_2MONTHS_UPTO_3MONHTS = r63_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR63_OVER_3MONTHS_UPTO_6MONTHS() {
	return R63_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR63_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r63_OVER_3MONTHS_UPTO_6MONTHS) {
	R63_OVER_3MONTHS_UPTO_6MONTHS = r63_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR63_OVER_6MONTHS_UPTO_1YEAR() {
	return R63_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR63_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r63_OVER_6MONTHS_UPTO_1YEAR) {
	R63_OVER_6MONTHS_UPTO_1YEAR = r63_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR63_OVER_1YEAR_UPTO_3YEARS() {
	return R63_OVER_1YEAR_UPTO_3YEARS;
}








public void setR63_OVER_1YEAR_UPTO_3YEARS(BigDecimal r63_OVER_1YEAR_UPTO_3YEARS) {
	R63_OVER_1YEAR_UPTO_3YEARS = r63_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR63_OVER_3YEARS_UPTO_5YEARS() {
	return R63_OVER_3YEARS_UPTO_5YEARS;
}








public void setR63_OVER_3YEARS_UPTO_5YEARS(BigDecimal r63_OVER_3YEARS_UPTO_5YEARS) {
	R63_OVER_3YEARS_UPTO_5YEARS = r63_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR63_OVER_5YEARS() {
	return R63_OVER_5YEARS;
}








public void setR63_OVER_5YEARS(BigDecimal r63_OVER_5YEARS) {
	R63_OVER_5YEARS = r63_OVER_5YEARS;
}








public BigDecimal getR63_TOTAL() {
	return R63_TOTAL;
}








public void setR63_TOTAL(BigDecimal r63_TOTAL) {
	R63_TOTAL = r63_TOTAL;
}








public BigDecimal getR64_1_DAY() {
	return R64_1_DAY;
}








public void setR64_1_DAY(BigDecimal r64_1_DAY) {
	R64_1_DAY = r64_1_DAY;
}








public BigDecimal getR64_2TO7_DAYS() {
	return R64_2TO7_DAYS;
}








public void setR64_2TO7_DAYS(BigDecimal r64_2to7_DAYS) {
	R64_2TO7_DAYS = r64_2to7_DAYS;
}








public BigDecimal getR64_8TO14_DAYS() {
	return R64_8TO14_DAYS;
}








public void setR64_8TO14_DAYS(BigDecimal r64_8to14_DAYS) {
	R64_8TO14_DAYS = r64_8to14_DAYS;
}








public BigDecimal getR64_15TO30_DAYS() {
	return R64_15TO30_DAYS;
}








public void setR64_15TO30_DAYS(BigDecimal r64_15to30_DAYS) {
	R64_15TO30_DAYS = r64_15to30_DAYS;
}








public BigDecimal getR64_31DAYS_UPTO_2MONTHS() {
	return R64_31DAYS_UPTO_2MONTHS;
}








public void setR64_31DAYS_UPTO_2MONTHS(BigDecimal r64_31days_UPTO_2MONTHS) {
	R64_31DAYS_UPTO_2MONTHS = r64_31days_UPTO_2MONTHS;
}








public BigDecimal getR64_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R64_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR64_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r64_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R64_MORETHAN_2MONTHS_UPTO_3MONHTS = r64_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR64_OVER_3MONTHS_UPTO_6MONTHS() {
	return R64_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR64_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r64_OVER_3MONTHS_UPTO_6MONTHS) {
	R64_OVER_3MONTHS_UPTO_6MONTHS = r64_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR64_OVER_6MONTHS_UPTO_1YEAR() {
	return R64_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR64_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r64_OVER_6MONTHS_UPTO_1YEAR) {
	R64_OVER_6MONTHS_UPTO_1YEAR = r64_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR64_OVER_1YEAR_UPTO_3YEARS() {
	return R64_OVER_1YEAR_UPTO_3YEARS;
}








public void setR64_OVER_1YEAR_UPTO_3YEARS(BigDecimal r64_OVER_1YEAR_UPTO_3YEARS) {
	R64_OVER_1YEAR_UPTO_3YEARS = r64_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR64_OVER_3YEARS_UPTO_5YEARS() {
	return R64_OVER_3YEARS_UPTO_5YEARS;
}








public void setR64_OVER_3YEARS_UPTO_5YEARS(BigDecimal r64_OVER_3YEARS_UPTO_5YEARS) {
	R64_OVER_3YEARS_UPTO_5YEARS = r64_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR64_OVER_5YEARS() {
	return R64_OVER_5YEARS;
}








public void setR64_OVER_5YEARS(BigDecimal r64_OVER_5YEARS) {
	R64_OVER_5YEARS = r64_OVER_5YEARS;
}








public BigDecimal getR64_TOTAL() {
	return R64_TOTAL;
}








public void setR64_TOTAL(BigDecimal r64_TOTAL) {
	R64_TOTAL = r64_TOTAL;
}








public BigDecimal getR65_1_DAY() {
	return R65_1_DAY;
}








public void setR65_1_DAY(BigDecimal r65_1_DAY) {
	R65_1_DAY = r65_1_DAY;
}








public BigDecimal getR65_2TO7_DAYS() {
	return R65_2TO7_DAYS;
}








public void setR65_2TO7_DAYS(BigDecimal r65_2to7_DAYS) {
	R65_2TO7_DAYS = r65_2to7_DAYS;
}








public BigDecimal getR65_8TO14_DAYS() {
	return R65_8TO14_DAYS;
}








public void setR65_8TO14_DAYS(BigDecimal r65_8to14_DAYS) {
	R65_8TO14_DAYS = r65_8to14_DAYS;
}








public BigDecimal getR65_15TO30_DAYS() {
	return R65_15TO30_DAYS;
}








public void setR65_15TO30_DAYS(BigDecimal r65_15to30_DAYS) {
	R65_15TO30_DAYS = r65_15to30_DAYS;
}








public BigDecimal getR65_31DAYS_UPTO_2MONTHS() {
	return R65_31DAYS_UPTO_2MONTHS;
}








public void setR65_31DAYS_UPTO_2MONTHS(BigDecimal r65_31days_UPTO_2MONTHS) {
	R65_31DAYS_UPTO_2MONTHS = r65_31days_UPTO_2MONTHS;
}








public BigDecimal getR65_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R65_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR65_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r65_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R65_MORETHAN_2MONTHS_UPTO_3MONHTS = r65_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR65_OVER_3MONTHS_UPTO_6MONTHS() {
	return R65_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR65_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r65_OVER_3MONTHS_UPTO_6MONTHS) {
	R65_OVER_3MONTHS_UPTO_6MONTHS = r65_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR65_OVER_6MONTHS_UPTO_1YEAR() {
	return R65_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR65_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r65_OVER_6MONTHS_UPTO_1YEAR) {
	R65_OVER_6MONTHS_UPTO_1YEAR = r65_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR65_OVER_1YEAR_UPTO_3YEARS() {
	return R65_OVER_1YEAR_UPTO_3YEARS;
}








public void setR65_OVER_1YEAR_UPTO_3YEARS(BigDecimal r65_OVER_1YEAR_UPTO_3YEARS) {
	R65_OVER_1YEAR_UPTO_3YEARS = r65_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR65_OVER_3YEARS_UPTO_5YEARS() {
	return R65_OVER_3YEARS_UPTO_5YEARS;
}








public void setR65_OVER_3YEARS_UPTO_5YEARS(BigDecimal r65_OVER_3YEARS_UPTO_5YEARS) {
	R65_OVER_3YEARS_UPTO_5YEARS = r65_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR65_OVER_5YEARS() {
	return R65_OVER_5YEARS;
}








public void setR65_OVER_5YEARS(BigDecimal r65_OVER_5YEARS) {
	R65_OVER_5YEARS = r65_OVER_5YEARS;
}








public BigDecimal getR65_TOTAL() {
	return R65_TOTAL;
}








public void setR65_TOTAL(BigDecimal r65_TOTAL) {
	R65_TOTAL = r65_TOTAL;
}








public BigDecimal getR66_1_DAY() {
	return R66_1_DAY;
}








public void setR66_1_DAY(BigDecimal r66_1_DAY) {
	R66_1_DAY = r66_1_DAY;
}








public BigDecimal getR66_2TO7_DAYS() {
	return R66_2TO7_DAYS;
}








public void setR66_2TO7_DAYS(BigDecimal r66_2to7_DAYS) {
	R66_2TO7_DAYS = r66_2to7_DAYS;
}








public BigDecimal getR66_8TO14_DAYS() {
	return R66_8TO14_DAYS;
}








public void setR66_8TO14_DAYS(BigDecimal r66_8to14_DAYS) {
	R66_8TO14_DAYS = r66_8to14_DAYS;
}








public BigDecimal getR66_15TO30_DAYS() {
	return R66_15TO30_DAYS;
}








public void setR66_15TO30_DAYS(BigDecimal r66_15to30_DAYS) {
	R66_15TO30_DAYS = r66_15to30_DAYS;
}








public BigDecimal getR66_31DAYS_UPTO_2MONTHS() {
	return R66_31DAYS_UPTO_2MONTHS;
}








public void setR66_31DAYS_UPTO_2MONTHS(BigDecimal r66_31days_UPTO_2MONTHS) {
	R66_31DAYS_UPTO_2MONTHS = r66_31days_UPTO_2MONTHS;
}








public BigDecimal getR66_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R66_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR66_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r66_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R66_MORETHAN_2MONTHS_UPTO_3MONHTS = r66_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR66_OVER_3MONTHS_UPTO_6MONTHS() {
	return R66_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR66_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r66_OVER_3MONTHS_UPTO_6MONTHS) {
	R66_OVER_3MONTHS_UPTO_6MONTHS = r66_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR66_OVER_6MONTHS_UPTO_1YEAR() {
	return R66_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR66_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r66_OVER_6MONTHS_UPTO_1YEAR) {
	R66_OVER_6MONTHS_UPTO_1YEAR = r66_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR66_OVER_1YEAR_UPTO_3YEARS() {
	return R66_OVER_1YEAR_UPTO_3YEARS;
}








public void setR66_OVER_1YEAR_UPTO_3YEARS(BigDecimal r66_OVER_1YEAR_UPTO_3YEARS) {
	R66_OVER_1YEAR_UPTO_3YEARS = r66_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR66_OVER_3YEARS_UPTO_5YEARS() {
	return R66_OVER_3YEARS_UPTO_5YEARS;
}








public void setR66_OVER_3YEARS_UPTO_5YEARS(BigDecimal r66_OVER_3YEARS_UPTO_5YEARS) {
	R66_OVER_3YEARS_UPTO_5YEARS = r66_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR66_OVER_5YEARS() {
	return R66_OVER_5YEARS;
}








public void setR66_OVER_5YEARS(BigDecimal r66_OVER_5YEARS) {
	R66_OVER_5YEARS = r66_OVER_5YEARS;
}








public BigDecimal getR66_TOTAL() {
	return R66_TOTAL;
}








public void setR66_TOTAL(BigDecimal r66_TOTAL) {
	R66_TOTAL = r66_TOTAL;
}








public BigDecimal getR67_1_DAY() {
	return R67_1_DAY;
}








public void setR67_1_DAY(BigDecimal r67_1_DAY) {
	R67_1_DAY = r67_1_DAY;
}








public BigDecimal getR67_2TO7_DAYS() {
	return R67_2TO7_DAYS;
}








public void setR67_2TO7_DAYS(BigDecimal r67_2to7_DAYS) {
	R67_2TO7_DAYS = r67_2to7_DAYS;
}








public BigDecimal getR67_8TO14_DAYS() {
	return R67_8TO14_DAYS;
}








public void setR67_8TO14_DAYS(BigDecimal r67_8to14_DAYS) {
	R67_8TO14_DAYS = r67_8to14_DAYS;
}








public BigDecimal getR67_15TO30_DAYS() {
	return R67_15TO30_DAYS;
}








public void setR67_15TO30_DAYS(BigDecimal r67_15to30_DAYS) {
	R67_15TO30_DAYS = r67_15to30_DAYS;
}








public BigDecimal getR67_31DAYS_UPTO_2MONTHS() {
	return R67_31DAYS_UPTO_2MONTHS;
}








public void setR67_31DAYS_UPTO_2MONTHS(BigDecimal r67_31days_UPTO_2MONTHS) {
	R67_31DAYS_UPTO_2MONTHS = r67_31days_UPTO_2MONTHS;
}








public BigDecimal getR67_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R67_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR67_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r67_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R67_MORETHAN_2MONTHS_UPTO_3MONHTS = r67_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR67_OVER_3MONTHS_UPTO_6MONTHS() {
	return R67_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR67_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r67_OVER_3MONTHS_UPTO_6MONTHS) {
	R67_OVER_3MONTHS_UPTO_6MONTHS = r67_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR67_OVER_6MONTHS_UPTO_1YEAR() {
	return R67_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR67_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r67_OVER_6MONTHS_UPTO_1YEAR) {
	R67_OVER_6MONTHS_UPTO_1YEAR = r67_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR67_OVER_1YEAR_UPTO_3YEARS() {
	return R67_OVER_1YEAR_UPTO_3YEARS;
}








public void setR67_OVER_1YEAR_UPTO_3YEARS(BigDecimal r67_OVER_1YEAR_UPTO_3YEARS) {
	R67_OVER_1YEAR_UPTO_3YEARS = r67_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR67_OVER_3YEARS_UPTO_5YEARS() {
	return R67_OVER_3YEARS_UPTO_5YEARS;
}








public void setR67_OVER_3YEARS_UPTO_5YEARS(BigDecimal r67_OVER_3YEARS_UPTO_5YEARS) {
	R67_OVER_3YEARS_UPTO_5YEARS = r67_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR67_OVER_5YEARS() {
	return R67_OVER_5YEARS;
}








public void setR67_OVER_5YEARS(BigDecimal r67_OVER_5YEARS) {
	R67_OVER_5YEARS = r67_OVER_5YEARS;
}








public BigDecimal getR67_TOTAL() {
	return R67_TOTAL;
}








public void setR67_TOTAL(BigDecimal r67_TOTAL) {
	R67_TOTAL = r67_TOTAL;
}








public BigDecimal getR68_1_DAY() {
	return R68_1_DAY;
}








public void setR68_1_DAY(BigDecimal r68_1_DAY) {
	R68_1_DAY = r68_1_DAY;
}








public BigDecimal getR68_2TO7_DAYS() {
	return R68_2TO7_DAYS;
}








public void setR68_2TO7_DAYS(BigDecimal r68_2to7_DAYS) {
	R68_2TO7_DAYS = r68_2to7_DAYS;
}








public BigDecimal getR68_8TO14_DAYS() {
	return R68_8TO14_DAYS;
}








public void setR68_8TO14_DAYS(BigDecimal r68_8to14_DAYS) {
	R68_8TO14_DAYS = r68_8to14_DAYS;
}








public BigDecimal getR68_15TO30_DAYS() {
	return R68_15TO30_DAYS;
}








public void setR68_15TO30_DAYS(BigDecimal r68_15to30_DAYS) {
	R68_15TO30_DAYS = r68_15to30_DAYS;
}








public BigDecimal getR68_31DAYS_UPTO_2MONTHS() {
	return R68_31DAYS_UPTO_2MONTHS;
}








public void setR68_31DAYS_UPTO_2MONTHS(BigDecimal r68_31days_UPTO_2MONTHS) {
	R68_31DAYS_UPTO_2MONTHS = r68_31days_UPTO_2MONTHS;
}








public BigDecimal getR68_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R68_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR68_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r68_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R68_MORETHAN_2MONTHS_UPTO_3MONHTS = r68_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR68_OVER_3MONTHS_UPTO_6MONTHS() {
	return R68_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR68_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r68_OVER_3MONTHS_UPTO_6MONTHS) {
	R68_OVER_3MONTHS_UPTO_6MONTHS = r68_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR68_OVER_6MONTHS_UPTO_1YEAR() {
	return R68_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR68_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r68_OVER_6MONTHS_UPTO_1YEAR) {
	R68_OVER_6MONTHS_UPTO_1YEAR = r68_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR68_OVER_1YEAR_UPTO_3YEARS() {
	return R68_OVER_1YEAR_UPTO_3YEARS;
}








public void setR68_OVER_1YEAR_UPTO_3YEARS(BigDecimal r68_OVER_1YEAR_UPTO_3YEARS) {
	R68_OVER_1YEAR_UPTO_3YEARS = r68_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR68_OVER_3YEARS_UPTO_5YEARS() {
	return R68_OVER_3YEARS_UPTO_5YEARS;
}








public void setR68_OVER_3YEARS_UPTO_5YEARS(BigDecimal r68_OVER_3YEARS_UPTO_5YEARS) {
	R68_OVER_3YEARS_UPTO_5YEARS = r68_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR68_OVER_5YEARS() {
	return R68_OVER_5YEARS;
}








public void setR68_OVER_5YEARS(BigDecimal r68_OVER_5YEARS) {
	R68_OVER_5YEARS = r68_OVER_5YEARS;
}








public BigDecimal getR68_TOTAL() {
	return R68_TOTAL;
}








public void setR68_TOTAL(BigDecimal r68_TOTAL) {
	R68_TOTAL = r68_TOTAL;
}








public BigDecimal getR69_1_DAY() {
	return R69_1_DAY;
}








public void setR69_1_DAY(BigDecimal r69_1_DAY) {
	R69_1_DAY = r69_1_DAY;
}








public BigDecimal getR69_2TO7_DAYS() {
	return R69_2TO7_DAYS;
}








public void setR69_2TO7_DAYS(BigDecimal r69_2to7_DAYS) {
	R69_2TO7_DAYS = r69_2to7_DAYS;
}








public BigDecimal getR69_8TO14_DAYS() {
	return R69_8TO14_DAYS;
}








public void setR69_8TO14_DAYS(BigDecimal r69_8to14_DAYS) {
	R69_8TO14_DAYS = r69_8to14_DAYS;
}








public BigDecimal getR69_15TO30_DAYS() {
	return R69_15TO30_DAYS;
}








public void setR69_15TO30_DAYS(BigDecimal r69_15to30_DAYS) {
	R69_15TO30_DAYS = r69_15to30_DAYS;
}








public BigDecimal getR69_31DAYS_UPTO_2MONTHS() {
	return R69_31DAYS_UPTO_2MONTHS;
}








public void setR69_31DAYS_UPTO_2MONTHS(BigDecimal r69_31days_UPTO_2MONTHS) {
	R69_31DAYS_UPTO_2MONTHS = r69_31days_UPTO_2MONTHS;
}








public BigDecimal getR69_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R69_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR69_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r69_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R69_MORETHAN_2MONTHS_UPTO_3MONHTS = r69_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR69_OVER_3MONTHS_UPTO_6MONTHS() {
	return R69_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR69_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r69_OVER_3MONTHS_UPTO_6MONTHS) {
	R69_OVER_3MONTHS_UPTO_6MONTHS = r69_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR69_OVER_6MONTHS_UPTO_1YEAR() {
	return R69_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR69_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r69_OVER_6MONTHS_UPTO_1YEAR) {
	R69_OVER_6MONTHS_UPTO_1YEAR = r69_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR69_OVER_1YEAR_UPTO_3YEARS() {
	return R69_OVER_1YEAR_UPTO_3YEARS;
}








public void setR69_OVER_1YEAR_UPTO_3YEARS(BigDecimal r69_OVER_1YEAR_UPTO_3YEARS) {
	R69_OVER_1YEAR_UPTO_3YEARS = r69_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR69_OVER_3YEARS_UPTO_5YEARS() {
	return R69_OVER_3YEARS_UPTO_5YEARS;
}








public void setR69_OVER_3YEARS_UPTO_5YEARS(BigDecimal r69_OVER_3YEARS_UPTO_5YEARS) {
	R69_OVER_3YEARS_UPTO_5YEARS = r69_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR69_OVER_5YEARS() {
	return R69_OVER_5YEARS;
}








public void setR69_OVER_5YEARS(BigDecimal r69_OVER_5YEARS) {
	R69_OVER_5YEARS = r69_OVER_5YEARS;
}








public BigDecimal getR69_TOTAL() {
	return R69_TOTAL;
}








public void setR69_TOTAL(BigDecimal r69_TOTAL) {
	R69_TOTAL = r69_TOTAL;
}








public BigDecimal getR70_1_DAY() {
	return R70_1_DAY;
}








public void setR70_1_DAY(BigDecimal r70_1_DAY) {
	R70_1_DAY = r70_1_DAY;
}








public BigDecimal getR70_2TO7_DAYS() {
	return R70_2TO7_DAYS;
}








public void setR70_2TO7_DAYS(BigDecimal r70_2to7_DAYS) {
	R70_2TO7_DAYS = r70_2to7_DAYS;
}








public BigDecimal getR70_8TO14_DAYS() {
	return R70_8TO14_DAYS;
}








public void setR70_8TO14_DAYS(BigDecimal r70_8to14_DAYS) {
	R70_8TO14_DAYS = r70_8to14_DAYS;
}








public BigDecimal getR70_15TO30_DAYS() {
	return R70_15TO30_DAYS;
}








public void setR70_15TO30_DAYS(BigDecimal r70_15to30_DAYS) {
	R70_15TO30_DAYS = r70_15to30_DAYS;
}








public BigDecimal getR70_31DAYS_UPTO_2MONTHS() {
	return R70_31DAYS_UPTO_2MONTHS;
}








public void setR70_31DAYS_UPTO_2MONTHS(BigDecimal r70_31days_UPTO_2MONTHS) {
	R70_31DAYS_UPTO_2MONTHS = r70_31days_UPTO_2MONTHS;
}








public BigDecimal getR70_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R70_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR70_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r70_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R70_MORETHAN_2MONTHS_UPTO_3MONHTS = r70_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR70_OVER_3MONTHS_UPTO_6MONTHS() {
	return R70_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR70_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r70_OVER_3MONTHS_UPTO_6MONTHS) {
	R70_OVER_3MONTHS_UPTO_6MONTHS = r70_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR70_OVER_6MONTHS_UPTO_1YEAR() {
	return R70_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR70_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r70_OVER_6MONTHS_UPTO_1YEAR) {
	R70_OVER_6MONTHS_UPTO_1YEAR = r70_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR70_OVER_1YEAR_UPTO_3YEARS() {
	return R70_OVER_1YEAR_UPTO_3YEARS;
}








public void setR70_OVER_1YEAR_UPTO_3YEARS(BigDecimal r70_OVER_1YEAR_UPTO_3YEARS) {
	R70_OVER_1YEAR_UPTO_3YEARS = r70_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR70_OVER_3YEARS_UPTO_5YEARS() {
	return R70_OVER_3YEARS_UPTO_5YEARS;
}








public void setR70_OVER_3YEARS_UPTO_5YEARS(BigDecimal r70_OVER_3YEARS_UPTO_5YEARS) {
	R70_OVER_3YEARS_UPTO_5YEARS = r70_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR70_OVER_5YEARS() {
	return R70_OVER_5YEARS;
}








public void setR70_OVER_5YEARS(BigDecimal r70_OVER_5YEARS) {
	R70_OVER_5YEARS = r70_OVER_5YEARS;
}








public BigDecimal getR70_TOTAL() {
	return R70_TOTAL;
}








public void setR70_TOTAL(BigDecimal r70_TOTAL) {
	R70_TOTAL = r70_TOTAL;
}








public BigDecimal getR71_1_DAY() {
	return R71_1_DAY;
}








public void setR71_1_DAY(BigDecimal r71_1_DAY) {
	R71_1_DAY = r71_1_DAY;
}








public BigDecimal getR71_2TO7_DAYS() {
	return R71_2TO7_DAYS;
}








public void setR71_2TO7_DAYS(BigDecimal r71_2to7_DAYS) {
	R71_2TO7_DAYS = r71_2to7_DAYS;
}








public BigDecimal getR71_8TO14_DAYS() {
	return R71_8TO14_DAYS;
}








public void setR71_8TO14_DAYS(BigDecimal r71_8to14_DAYS) {
	R71_8TO14_DAYS = r71_8to14_DAYS;
}








public BigDecimal getR71_15TO30_DAYS() {
	return R71_15TO30_DAYS;
}








public void setR71_15TO30_DAYS(BigDecimal r71_15to30_DAYS) {
	R71_15TO30_DAYS = r71_15to30_DAYS;
}








public BigDecimal getR71_31DAYS_UPTO_2MONTHS() {
	return R71_31DAYS_UPTO_2MONTHS;
}








public void setR71_31DAYS_UPTO_2MONTHS(BigDecimal r71_31days_UPTO_2MONTHS) {
	R71_31DAYS_UPTO_2MONTHS = r71_31days_UPTO_2MONTHS;
}








public BigDecimal getR71_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R71_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR71_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r71_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R71_MORETHAN_2MONTHS_UPTO_3MONHTS = r71_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR71_OVER_3MONTHS_UPTO_6MONTHS() {
	return R71_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR71_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r71_OVER_3MONTHS_UPTO_6MONTHS) {
	R71_OVER_3MONTHS_UPTO_6MONTHS = r71_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR71_OVER_6MONTHS_UPTO_1YEAR() {
	return R71_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR71_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r71_OVER_6MONTHS_UPTO_1YEAR) {
	R71_OVER_6MONTHS_UPTO_1YEAR = r71_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR71_OVER_1YEAR_UPTO_3YEARS() {
	return R71_OVER_1YEAR_UPTO_3YEARS;
}








public void setR71_OVER_1YEAR_UPTO_3YEARS(BigDecimal r71_OVER_1YEAR_UPTO_3YEARS) {
	R71_OVER_1YEAR_UPTO_3YEARS = r71_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR71_OVER_3YEARS_UPTO_5YEARS() {
	return R71_OVER_3YEARS_UPTO_5YEARS;
}








public void setR71_OVER_3YEARS_UPTO_5YEARS(BigDecimal r71_OVER_3YEARS_UPTO_5YEARS) {
	R71_OVER_3YEARS_UPTO_5YEARS = r71_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR71_OVER_5YEARS() {
	return R71_OVER_5YEARS;
}








public void setR71_OVER_5YEARS(BigDecimal r71_OVER_5YEARS) {
	R71_OVER_5YEARS = r71_OVER_5YEARS;
}








public BigDecimal getR71_TOTAL() {
	return R71_TOTAL;
}








public void setR71_TOTAL(BigDecimal r71_TOTAL) {
	R71_TOTAL = r71_TOTAL;
}








public BigDecimal getR72_1_DAY() {
	return R72_1_DAY;
}








public void setR72_1_DAY(BigDecimal r72_1_DAY) {
	R72_1_DAY = r72_1_DAY;
}








public BigDecimal getR72_2TO7_DAYS() {
	return R72_2TO7_DAYS;
}








public void setR72_2TO7_DAYS(BigDecimal r72_2to7_DAYS) {
	R72_2TO7_DAYS = r72_2to7_DAYS;
}








public BigDecimal getR72_8TO14_DAYS() {
	return R72_8TO14_DAYS;
}








public void setR72_8TO14_DAYS(BigDecimal r72_8to14_DAYS) {
	R72_8TO14_DAYS = r72_8to14_DAYS;
}








public BigDecimal getR72_15TO30_DAYS() {
	return R72_15TO30_DAYS;
}








public void setR72_15TO30_DAYS(BigDecimal r72_15to30_DAYS) {
	R72_15TO30_DAYS = r72_15to30_DAYS;
}








public BigDecimal getR72_31DAYS_UPTO_2MONTHS() {
	return R72_31DAYS_UPTO_2MONTHS;
}








public void setR72_31DAYS_UPTO_2MONTHS(BigDecimal r72_31days_UPTO_2MONTHS) {
	R72_31DAYS_UPTO_2MONTHS = r72_31days_UPTO_2MONTHS;
}








public BigDecimal getR72_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R72_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR72_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r72_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R72_MORETHAN_2MONTHS_UPTO_3MONHTS = r72_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR72_OVER_3MONTHS_UPTO_6MONTHS() {
	return R72_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR72_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r72_OVER_3MONTHS_UPTO_6MONTHS) {
	R72_OVER_3MONTHS_UPTO_6MONTHS = r72_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR72_OVER_6MONTHS_UPTO_1YEAR() {
	return R72_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR72_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r72_OVER_6MONTHS_UPTO_1YEAR) {
	R72_OVER_6MONTHS_UPTO_1YEAR = r72_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR72_OVER_1YEAR_UPTO_3YEARS() {
	return R72_OVER_1YEAR_UPTO_3YEARS;
}








public void setR72_OVER_1YEAR_UPTO_3YEARS(BigDecimal r72_OVER_1YEAR_UPTO_3YEARS) {
	R72_OVER_1YEAR_UPTO_3YEARS = r72_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR72_OVER_3YEARS_UPTO_5YEARS() {
	return R72_OVER_3YEARS_UPTO_5YEARS;
}








public void setR72_OVER_3YEARS_UPTO_5YEARS(BigDecimal r72_OVER_3YEARS_UPTO_5YEARS) {
	R72_OVER_3YEARS_UPTO_5YEARS = r72_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR72_OVER_5YEARS() {
	return R72_OVER_5YEARS;
}








public void setR72_OVER_5YEARS(BigDecimal r72_OVER_5YEARS) {
	R72_OVER_5YEARS = r72_OVER_5YEARS;
}








public BigDecimal getR72_TOTAL() {
	return R72_TOTAL;
}








public void setR72_TOTAL(BigDecimal r72_TOTAL) {
	R72_TOTAL = r72_TOTAL;
}








public BigDecimal getR73_1_DAY() {
	return R73_1_DAY;
}








public void setR73_1_DAY(BigDecimal r73_1_DAY) {
	R73_1_DAY = r73_1_DAY;
}








public BigDecimal getR73_2TO7_DAYS() {
	return R73_2TO7_DAYS;
}








public void setR73_2TO7_DAYS(BigDecimal r73_2to7_DAYS) {
	R73_2TO7_DAYS = r73_2to7_DAYS;
}








public BigDecimal getR73_8TO14_DAYS() {
	return R73_8TO14_DAYS;
}








public void setR73_8TO14_DAYS(BigDecimal r73_8to14_DAYS) {
	R73_8TO14_DAYS = r73_8to14_DAYS;
}








public BigDecimal getR73_15TO30_DAYS() {
	return R73_15TO30_DAYS;
}








public void setR73_15TO30_DAYS(BigDecimal r73_15to30_DAYS) {
	R73_15TO30_DAYS = r73_15to30_DAYS;
}








public BigDecimal getR73_31DAYS_UPTO_2MONTHS() {
	return R73_31DAYS_UPTO_2MONTHS;
}








public void setR73_31DAYS_UPTO_2MONTHS(BigDecimal r73_31days_UPTO_2MONTHS) {
	R73_31DAYS_UPTO_2MONTHS = r73_31days_UPTO_2MONTHS;
}








public BigDecimal getR73_MORETHAN_2MONTHS_UPTO_3MONHTS() {
	return R73_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public void setR73_MORETHAN_2MONTHS_UPTO_3MONHTS(BigDecimal r73_MORETHAN_2MONTHS_UPTO_3MONHTS) {
	R73_MORETHAN_2MONTHS_UPTO_3MONHTS = r73_MORETHAN_2MONTHS_UPTO_3MONHTS;
}








public BigDecimal getR73_OVER_3MONTHS_UPTO_6MONTHS() {
	return R73_OVER_3MONTHS_UPTO_6MONTHS;
}








public void setR73_OVER_3MONTHS_UPTO_6MONTHS(BigDecimal r73_OVER_3MONTHS_UPTO_6MONTHS) {
	R73_OVER_3MONTHS_UPTO_6MONTHS = r73_OVER_3MONTHS_UPTO_6MONTHS;
}








public BigDecimal getR73_OVER_6MONTHS_UPTO_1YEAR() {
	return R73_OVER_6MONTHS_UPTO_1YEAR;
}








public void setR73_OVER_6MONTHS_UPTO_1YEAR(BigDecimal r73_OVER_6MONTHS_UPTO_1YEAR) {
	R73_OVER_6MONTHS_UPTO_1YEAR = r73_OVER_6MONTHS_UPTO_1YEAR;
}








public BigDecimal getR73_OVER_1YEAR_UPTO_3YEARS() {
	return R73_OVER_1YEAR_UPTO_3YEARS;
}








public void setR73_OVER_1YEAR_UPTO_3YEARS(BigDecimal r73_OVER_1YEAR_UPTO_3YEARS) {
	R73_OVER_1YEAR_UPTO_3YEARS = r73_OVER_1YEAR_UPTO_3YEARS;
}








public BigDecimal getR73_OVER_3YEARS_UPTO_5YEARS() {
	return R73_OVER_3YEARS_UPTO_5YEARS;
}








public void setR73_OVER_3YEARS_UPTO_5YEARS(BigDecimal r73_OVER_3YEARS_UPTO_5YEARS) {
	R73_OVER_3YEARS_UPTO_5YEARS = r73_OVER_3YEARS_UPTO_5YEARS;
}








public BigDecimal getR73_OVER_5YEARS() {
	return R73_OVER_5YEARS;
}








public void setR73_OVER_5YEARS(BigDecimal r73_OVER_5YEARS) {
	R73_OVER_5YEARS = r73_OVER_5YEARS;
}








public BigDecimal getR73_TOTAL() {
	return R73_TOTAL;
}








public void setR73_TOTAL(BigDecimal r73_TOTAL) {
	R73_TOTAL = r73_TOTAL;
}








public CPR_STRUCT_LIQ_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}








}
