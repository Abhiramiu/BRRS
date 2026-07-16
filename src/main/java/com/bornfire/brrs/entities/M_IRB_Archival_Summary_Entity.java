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
@Table(name = "M_IRB_Archival_Summary_Entity")

public class M_IRB_Archival_Summary_Entity {
	
	// --- Group R10 ---
		@Column(name = "R10_PRODUCT")
		private String r10product;

		@Column(name = "R10_UP_TO_1_MONTH")
		private BigDecimal r10_upTo1Month;

		@Column(name = "R10_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r10_moreThan1MonthTo3Months;

		@Column(name = "R10_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r10_moreThan3MonthTo6Months;

		@Column(name = "R10_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r10_moreThan6MonthTo12Months;

		@Column(name = "R10_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r10_moreThan12MonthTo3Years;

		@Column(name = "R10_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r10_moreThan3YearsTo5Years;
		
		@Column(name = "R10_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r10_moreThan5YearsTo10Years;

		@Column(name = "R10_MORE_THAN_10_YEARS")
		private BigDecimal r10_moreThan10Years;

		@Column(name = "R10_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r10_nonRatioSensativeItems;

		@Column(name = "R10_total")
		private BigDecimal r10_total;

		// --- Group R11 ---
		@Column(name = "R11_PRODUCT")
		private String r11product;

		@Column(name = "R11_UP_TO_1_MONTH")
		private BigDecimal r11_upTo1Month;

		@Column(name = "R11_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r11_moreThan1MonthTo3Months;

		@Column(name = "R11_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r11_moreThan3MonthTo6Months;

		@Column(name = "R11_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r11_moreThan6MonthTo12Months;

		@Column(name = "R11_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r11_moreThan12MonthTo3Years;

		@Column(name = "R11_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r11_moreThan3YearsTo5Years;

		@Column(name = "R11_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r11_moreThan5YearsTo10Years;

		@Column(name = "R11_MORE_THAN_10_YEARS")
		private BigDecimal r11_moreThan10Years;

		@Column(name = "R11_total")
		private BigDecimal r11_total;

		// --- Group R12 ---
		@Column(name = "R12_PRODUCT")
		private String r12product;

		@Column(name = "R12_UP_TO_1_MONTH")
		private BigDecimal r12_upTo1Month;

		@Column(name = "R12_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r12_moreThan1MonthTo3Months;

		@Column(name = "R12_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r12_moreThan3MonthTo6Months;

		@Column(name = "R12_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r12_moreThan6MonthTo12Months;

		@Column(name = "R12_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r12_moreThan12MonthTo3Years;

		@Column(name = "R12_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r12_moreThan3YearsTo5Years;

		@Column(name = "R12_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r12_moreThan5YearsTo10Years;

		@Column(name = "R12_MORE_THAN_10_YEARS")
		private BigDecimal r12_moreThan10Years;

		@Column(name = "R12_total")
		private BigDecimal r12_total;

		// --- Group R13 ---
		@Column(name = "R13_PRODUCT")
		private String r13product;

		@Column(name = "R13_UP_TO_1_MONTH")
		private BigDecimal r13_upTo1Month;

		@Column(name = "R13_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r13_moreThan1MonthTo3Months;

		@Column(name = "R13_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r13_moreThan3MonthTo6Months;

		@Column(name = "R13_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r13_moreThan6MonthTo12Months;

		@Column(name = "R13_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r13_moreThan12MonthTo3Years;

		@Column(name = "R13_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r13_moreThan3YearsTo5Years;

		@Column(name = "R13_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r13_moreThan5YearsTo10Years;

		@Column(name = "R13_MORE_THAN_10_YEARS")
		private BigDecimal r13_moreThan10Years;

		@Column(name = "R13_total")
		private BigDecimal r13_total;

		// --- Group R14 ---
		@Column(name = "R14_PRODUCT")
		private String r14product;

		@Column(name = "R14_UP_TO_1_MONTH")
		private BigDecimal r14_upTo1Month;

		@Column(name = "R14_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r14_moreThan1MonthTo3Months;

		@Column(name = "R14_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r14_moreThan3MonthTo6Months;

		@Column(name = "R14_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r14_moreThan6MonthTo12Months;

		@Column(name = "R14_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r14_moreThan12MonthTo3Years;

		@Column(name = "R14_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r14_moreThan3YearsTo5Years;

		@Column(name = "R14_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r14_moreThan5YearsTo10Years;

		@Column(name = "R14_MORE_THAN_10_YEARS")
		private BigDecimal r14_moreThan10Years;

		@Column(name = "R14_total")
		private BigDecimal r14_total;

		// --- Group R15 ---
		@Column(name = "R15_PRODUCT")
		private String r15product;

		@Column(name = "R15_UP_TO_1_MONTH")
		private BigDecimal r15_upTo1Month;

		@Column(name = "R15_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r15_moreThan1MonthTo3Months;

		@Column(name = "R15_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r15_moreThan3MonthTo6Months;

		@Column(name = "R15_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r15_moreThan6MonthTo12Months;

		@Column(name = "R15_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r15_moreThan12MonthTo3Years;

		@Column(name = "R15_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r15_moreThan3YearsTo5Years;

		@Column(name = "R15_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r15_moreThan5YearsTo10Years;

		@Column(name = "R15_MORE_THAN_10_YEARS")
		private BigDecimal r15_moreThan10Years;

		@Column(name = "R15_total")
		private BigDecimal r15_total;

		// --- Group R16 ---
		@Column(name = "R16_PRODUCT")
		private String r16product;

		@Column(name = "R16_UP_TO_1_MONTH")
		private BigDecimal r16_upTo1Month;

		@Column(name = "R16_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r16_moreThan1MonthTo3Months;

		@Column(name = "R16_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r16_moreThan3MonthTo6Months;

		@Column(name = "R16_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r16_moreThan6MonthTo12Months;

		@Column(name = "R16_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r16_moreThan12MonthTo3Years;

		@Column(name = "R16_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r16_moreThan3YearsTo5Years;

		@Column(name = "R16_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r16_moreThan5YearsTo10Years;

		@Column(name = "R16_MORE_THAN_10_YEARS")
		private BigDecimal r16_moreThan10Years;

		@Column(name = "R16_total")
		private BigDecimal r16_total;

		// --- Group R17 ---
		@Column(name = "R17_PRODUCT")
		private String r17product;

		@Column(name = "R17_UP_TO_1_MONTH")
		private BigDecimal r17_upTo1Month;

		@Column(name = "R17_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r17_moreThan1MonthTo3Months;

		@Column(name = "R17_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r17_moreThan3MonthTo6Months;

		@Column(name = "R17_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r17_moreThan6MonthTo12Months;

		@Column(name = "R17_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r17_moreThan12MonthTo3Years;

		@Column(name = "R17_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r17_moreThan3YearsTo5Years;

		@Column(name = "R17_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r17_moreThan5YearsTo10Years;

		@Column(name = "R17_MORE_THAN_10_YEARS")
		private BigDecimal r17_moreThan10Years;

		@Column(name = "R17_total")
		private BigDecimal r17_total;

		// --- Group R18 ---
		@Column(name = "R18_PRODUCT")
		private String r18product;

		@Column(name = "R18_UP_TO_1_MONTH")
		private BigDecimal r18_upTo1Month;

		@Column(name = "R18_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r18_moreThan1MonthTo3Months;

		@Column(name = "R18_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r18_moreThan3MonthTo6Months;

		@Column(name = "R18_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r18_moreThan6MonthTo12Months;

		@Column(name = "R18_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r18_moreThan12MonthTo3Years;

		@Column(name = "R18_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r18_moreThan3YearsTo5Years;

		@Column(name = "R18_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r18_moreThan5YearsTo10Years;

		@Column(name = "R18_MORE_THAN_10_YEARS")
		private BigDecimal r18_moreThan10Years;

		@Column(name = "R18_total")
		private BigDecimal r18_total;

		// --- Group R19 ---
		@Column(name = "R19_PRODUCT")
		private String r19product;

		@Column(name = "R19_UP_TO_1_MONTH")
		private BigDecimal r19_upTo1Month;

		@Column(name = "R19_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r19_moreThan1MonthTo3Months;

		@Column(name = "R19_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r19_moreThan3MonthTo6Months;

		@Column(name = "R19_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r19_moreThan6MonthTo12Months;

		@Column(name = "R19_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r19_moreThan12MonthTo3Years;

		@Column(name = "R19_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r19_moreThan3YearsTo5Years;

		@Column(name = "R19_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r19_moreThan5YearsTo10Years;

		@Column(name = "R19_MORE_THAN_10_YEARS")
		private BigDecimal r19_moreThan10Years;

		@Column(name = "R19_total")
		private BigDecimal r19_total;

		// --- Group R20 ---
		@Column(name = "R20_PRODUCT")
		private String r20product;

		@Column(name = "R20_UP_TO_1_MONTH")
		private BigDecimal r20_upTo1Month;

		@Column(name = "R20_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r20_moreThan1MonthTo3Months;

		@Column(name = "R20_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r20_moreThan3MonthTo6Months;

		@Column(name = "R20_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r20_moreThan6MonthTo12Months;

		@Column(name = "R20_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r20_moreThan12MonthTo3Years;

		@Column(name = "R20_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r20_moreThan3YearsTo5Years;

		@Column(name = "R20_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r20_moreThan5YearsTo10Years;

		@Column(name = "R20_MORE_THAN_10_YEARS")
		private BigDecimal r20_moreThan10Years;

		@Column(name = "R20_total")
		private BigDecimal r20_total;

		// --- Group R21 ---
		@Column(name = "R21_PRODUCT")
		private String r21product;

		@Column(name = "R21_UP_TO_1_MONTH")
		private BigDecimal r21_upTo1Month;

		@Column(name = "R21_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r21_moreThan1MonthTo3Months;

		@Column(name = "R21_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r21_moreThan3MonthTo6Months;

		@Column(name = "R21_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r21_moreThan6MonthTo12Months;

		@Column(name = "R21_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r21_moreThan12MonthTo3Years;

		@Column(name = "R21_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r21_moreThan3YearsTo5Years;

		@Column(name = "R21_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r21_moreThan5YearsTo10Years;

		@Column(name = "R21_MORE_THAN_10_YEARS")
		private BigDecimal r21_moreThan10Years;

		@Column(name = "R21_total")
		private BigDecimal r21_total;

		// --- Group R22 ---
		@Column(name = "R22_PRODUCT")
		private String r22product;

		@Column(name = "R22_UP_TO_1_MONTH")
		private BigDecimal r22_upTo1Month;

		@Column(name = "R22_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r22_moreThan1MonthTo3Months;

		@Column(name = "R22_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r22_moreThan3MonthTo6Months;

		@Column(name = "R22_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r22_moreThan6MonthTo12Months;

		@Column(name = "R22_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r22_moreThan12MonthTo3Years;

		@Column(name = "R22_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r22_moreThan3YearsTo5Years;

		@Column(name = "R22_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r22_moreThan5YearsTo10Years;

		@Column(name = "R22_MORE_THAN_10_YEARS")
		private BigDecimal r22_moreThan10Years;

		@Column(name = "R22_total")
		private BigDecimal r22_total;

		// --- Group R23 ---
		@Column(name = "R23_PRODUCT")
		private String r23product;

		@Column(name = "R23_UP_TO_1_MONTH")
		private BigDecimal r23_upTo1Month;

		@Column(name = "R23_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r23_moreThan1MonthTo3Months;

		@Column(name = "R23_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r23_moreThan3MonthTo6Months;

		@Column(name = "R23_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r23_moreThan6MonthTo12Months;

		@Column(name = "R23_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r23_moreThan12MonthTo3Years;

		@Column(name = "R23_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r23_moreThan3YearsTo5Years;

		@Column(name = "R23_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r23_moreThan5YearsTo10Years;

		@Column(name = "R23_MORE_THAN_10_YEARS")
		private BigDecimal r23_moreThan10Years;

		@Column(name = "R23_total")
		private BigDecimal r23_total;

		// --- Group R24 ---
		@Column(name = "R24_PRODUCT")
		private String r24product;

		@Column(name = "R24_UP_TO_1_MONTH")
		private BigDecimal r24_upTo1Month;

		@Column(name = "R24_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r24_moreThan1MonthTo3Months;

		@Column(name = "R24_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r24_moreThan3MonthTo6Months;

		@Column(name = "R24_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r24_moreThan6MonthTo12Months;

		@Column(name = "R24_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r24_moreThan12MonthTo3Years;

		@Column(name = "R24_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r24_moreThan3YearsTo5Years;

		@Column(name = "R24_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r24_moreThan5YearsTo10Years;

		@Column(name = "R24_MORE_THAN_10_YEARS")
		private BigDecimal r24_moreThan10Years;

		@Column(name = "R24_total")
		private BigDecimal r24_total;

		// --- Group R25 ---
		@Column(name = "R25_PRODUCT")
		private String r25product;

		@Column(name = "R25_UP_TO_1_MONTH")
		private BigDecimal r25_upTo1Month;

		@Column(name = "R25_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r25_moreThan1MonthTo3Months;

		@Column(name = "R25_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r25_moreThan3MonthTo6Months;

		@Column(name = "R25_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r25_moreThan6MonthTo12Months;

		@Column(name = "R25_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r25_moreThan12MonthTo3Years;

		@Column(name = "R25_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r25_moreThan3YearsTo5Years;

		@Column(name = "R25_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r25_moreThan5YearsTo10Years;

		@Column(name = "R25_MORE_THAN_10_YEARS")
		private BigDecimal r25_moreThan10Years;

		@Column(name = "R25_total")
		private BigDecimal r25_total;

		// --- Group R26 ---
		@Column(name = "R26_PRODUCT")
		private String r26product;

		@Column(name = "R26_UP_TO_1_MONTH")
		private BigDecimal r26_upTo1Month;

		@Column(name = "R26_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r26_moreThan1MonthTo3Months;

		@Column(name = "R26_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r26_moreThan3MonthTo6Months;

		@Column(name = "R26_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r26_moreThan6MonthTo12Months;

		@Column(name = "R26_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r26_moreThan12MonthTo3Years;

		@Column(name = "R26_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r26_moreThan3YearsTo5Years;

		@Column(name = "R26_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r26_moreThan5YearsTo10Years;

		@Column(name = "R26_MORE_THAN_10_YEARS")
		private BigDecimal r26_moreThan10Years;

		@Column(name = "R26_total")
		private BigDecimal r26_total;

		// --- Group R27 ---
		@Column(name = "R27_PRODUCT")
		private String r27product;

		@Column(name = "R27_UP_TO_1_MONTH")
		private BigDecimal r27_upTo1Month;

		@Column(name = "R27_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r27_moreThan1MonthTo3Months;

		@Column(name = "R27_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r27_moreThan3MonthTo6Months;

		@Column(name = "R27_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r27_moreThan6MonthTo12Months;

		@Column(name = "R27_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r27_moreThan12MonthTo3Years;

		@Column(name = "R27_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r27_moreThan3YearsTo5Years;

		@Column(name = "R27_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r27_moreThan5YearsTo10Years;

		@Column(name = "R27_MORE_THAN_10_YEARS")
		private BigDecimal r27_moreThan10Years;

		@Column(name = "R27_total")
		private BigDecimal r27_total;

		// --- Group R28 ---
		@Column(name = "R28_PRODUCT")
		private String r28product;

		@Column(name = "R28_UP_TO_1_MONTH")
		private BigDecimal r28_upTo1Month;

		@Column(name = "R28_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r28_moreThan1MonthTo3Months;

		@Column(name = "R28_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r28_moreThan3MonthTo6Months;

		@Column(name = "R28_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r28_moreThan6MonthTo12Months;

		@Column(name = "R28_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r28_moreThan12MonthTo3Years;

		@Column(name = "R28_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r28_moreThan3YearsTo5Years;

		@Column(name = "R28_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r28_moreThan5YearsTo10Years;

		@Column(name = "R28_MORE_THAN_10_YEARS")
		private BigDecimal r28_moreThan10Years;

		@Column(name = "R28_total")
		private BigDecimal r28_total;

		// --- Group R29 ---
		@Column(name = "R29_PRODUCT")
		private String r29product;

		@Column(name = "R29_UP_TO_1_MONTH")
		private BigDecimal r29_upTo1Month;

		@Column(name = "R29_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r29_moreThan1MonthTo3Months;

		@Column(name = "R29_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r29_moreThan3MonthTo6Months;

		@Column(name = "R29_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r29_moreThan6MonthTo12Months;

		@Column(name = "R29_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r29_moreThan12MonthTo3Years;

		@Column(name = "R29_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r29_moreThan3YearsTo5Years;

		@Column(name = "R29_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r29_moreThan5YearsTo10Years;

		@Column(name = "R29_MORE_THAN_10_YEARS")
		private BigDecimal r29_moreThan10Years;

		@Column(name = "R29_total")
		private BigDecimal r29_total;

		// --- Group R30 ---
		@Column(name = "R30_PRODUCT")
		private String r30product;

		@Column(name = "R30_UP_TO_1_MONTH")
		private BigDecimal r30_upTo1Month;

		@Column(name = "R30_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r30_moreThan1MonthTo3Months;

		@Column(name = "R30_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r30_moreThan3MonthTo6Months;

		@Column(name = "R30_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r30_moreThan6MonthTo12Months;

		@Column(name = "R30_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r30_moreThan12MonthTo3Years;

		@Column(name = "R30_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r30_moreThan3YearsTo5Years;

		@Column(name = "R30_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r30_moreThan5YearsTo10Years;

		@Column(name = "R30_MORE_THAN_10_YEARS")
		private BigDecimal r30_moreThan10Years;

		@Column(name = "R30_total")
		private BigDecimal r30_total;

		// --- Group R31 ---
		@Column(name = "R31_PRODUCT")
		private String r31product;

		@Column(name = "R31_UP_TO_1_MONTH")
		private BigDecimal r31_upTo1Month;

		@Column(name = "R31_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r31_moreThan1MonthTo3Months;

		@Column(name = "R31_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r31_moreThan3MonthTo6Months;

		@Column(name = "R31_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r31_moreThan6MonthTo12Months;

		@Column(name = "R31_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r31_moreThan12MonthTo3Years;

		@Column(name = "R31_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r31_moreThan3YearsTo5Years;

		@Column(name = "R31_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r31_moreThan5YearsTo10Years;

		@Column(name = "R31_MORE_THAN_10_YEARS")
		private BigDecimal r31_moreThan10Years;

		@Column(name = "R31_total")
		private BigDecimal r31_total;

		// --- Group R32 ---
		@Column(name = "R32_PRODUCT")
		private String r32product;

		@Column(name = "R32_UP_TO_1_MONTH")
		private BigDecimal r32_upTo1Month;

		@Column(name = "R32_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r32_moreThan1MonthTo3Months;

		@Column(name = "R32_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r32_moreThan3MonthTo6Months;

		@Column(name = "R32_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r32_moreThan6MonthTo12Months;

		@Column(name = "R32_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r32_moreThan12MonthTo3Years;

		@Column(name = "R32_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r32_moreThan3YearsTo5Years;

		@Column(name = "R32_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r32_moreThan5YearsTo10Years;

		@Column(name = "R32_MORE_THAN_10_YEARS")
		private BigDecimal r32_moreThan10Years;

		@Column(name = "R32_total")
		private BigDecimal r32_total;

		// --- Group R33 ---
		@Column(name = "R33_PRODUCT")
		private String r33product;

		@Column(name = "R33_UP_TO_1_MONTH")
		private BigDecimal r33_upTo1Month;

		@Column(name = "R33_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r33_moreThan1MonthTo3Months;

		@Column(name = "R33_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r33_moreThan3MonthTo6Months;

		@Column(name = "R33_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r33_moreThan6MonthTo12Months;

		@Column(name = "R33_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r33_moreThan12MonthTo3Years;

		@Column(name = "R33_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r33_moreThan3YearsTo5Years;

		@Column(name = "R33_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r33_moreThan5YearsTo10Years;

		@Column(name = "R33_MORE_THAN_10_YEARS")
		private BigDecimal r33_moreThan10Years;

		@Column(name = "R33_total")
		private BigDecimal r33_total;

		// --- Group R34 ---
		@Column(name = "R34_PRODUCT")
		private String r34product;

		@Column(name = "R34_UP_TO_1_MONTH")
		private BigDecimal r34_upTo1Month;

		@Column(name = "R34_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r34_moreThan1MonthTo3Months;

		@Column(name = "R34_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r34_moreThan3MonthTo6Months;

		@Column(name = "R34_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r34_moreThan6MonthTo12Months;

		@Column(name = "R34_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r34_moreThan12MonthTo3Years;

		@Column(name = "R34_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r34_moreThan3YearsTo5Years;

		@Column(name = "R34_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r34_moreThan5YearsTo10Years;

		@Column(name = "R34_MORE_THAN_10_YEARS")
		private BigDecimal r34_moreThan10Years;

		@Column(name = "R34_total")
		private BigDecimal r34_total;

		// --- Group R35 ---
		@Column(name = "R35_PRODUCT")
		private String r35product;

		@Column(name = "R35_UP_TO_1_MONTH")
		private BigDecimal r35_upTo1Month;

		@Column(name = "R35_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r35_moreThan1MonthTo3Months;

		@Column(name = "R35_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r35_moreThan3MonthTo6Months;

		@Column(name = "R35_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r35_moreThan6MonthTo12Months;

		@Column(name = "R35_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r35_moreThan12MonthTo3Years;

		@Column(name = "R35_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r35_moreThan3YearsTo5Years;

		@Column(name = "R35_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r35_moreThan5YearsTo10Years;

		@Column(name = "R35_MORE_THAN_10_YEARS")
		private BigDecimal r35_moreThan10Years;

		@Column(name = "R35_total")
		private BigDecimal r35_total;

		// --- Group R36 ---
		@Column(name = "R36_PRODUCT")
		private String r36product;

		@Column(name = "R36_UP_TO_1_MONTH")
		private BigDecimal r36_upTo1Month;

		@Column(name = "R36_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r36_moreThan1MonthTo3Months;

		@Column(name = "R36_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r36_moreThan3MonthTo6Months;

		@Column(name = "R36_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r36_moreThan6MonthTo12Months;

		@Column(name = "R36_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r36_moreThan12MonthTo3Years;

		@Column(name = "R36_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r36_moreThan3YearsTo5Years;

		@Column(name = "R36_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r36_moreThan5YearsTo10Years;

		@Column(name = "R36_MORE_THAN_10_YEARS")
		private BigDecimal r36_moreThan10Years;

		@Column(name = "R36_total")
		private BigDecimal r36_total;

		// --- Group R37 ---
		@Column(name = "R37_PRODUCT")
		private String r37product;

		@Column(name = "R37_UP_TO_1_MONTH")
		private BigDecimal r37_upTo1Month;

		@Column(name = "R37_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r37_moreThan1MonthTo3Months;

		@Column(name = "R37_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r37_moreThan3MonthTo6Months;

		@Column(name = "R37_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r37_moreThan6MonthTo12Months;

		@Column(name = "R37_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r37_moreThan12MonthTo3Years;

		@Column(name = "R37_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r37_moreThan3YearsTo5Years;

		@Column(name = "R37_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r37_moreThan5YearsTo10Years;

		@Column(name = "R37_MORE_THAN_10_YEARS")
		private BigDecimal r37_moreThan10Years;

		@Column(name = "R37_total")
		private BigDecimal r37_total;

		// --- Group R38 ---
		@Column(name = "R38_PRODUCT")
		private String r38product;

		@Column(name = "R38_UP_TO_1_MONTH")
		private BigDecimal r38_upTo1Month;

		@Column(name = "R38_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r38_moreThan1MonthTo3Months;

		@Column(name = "R38_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r38_moreThan3MonthTo6Months;

		@Column(name = "R38_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r38_moreThan6MonthTo12Months;

		@Column(name = "R38_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r38_moreThan12MonthTo3Years;

		@Column(name = "R38_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r38_moreThan3YearsTo5Years;

		@Column(name = "R38_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r38_moreThan5YearsTo10Years;

		@Column(name = "R38_MORE_THAN_10_YEARS")
		private BigDecimal r38_moreThan10Years;

		@Column(name = "R38_total")
		private BigDecimal r38_total;

		// --- Group R39 ---
		@Column(name = "R39_PRODUCT")
		private String r39product;

		@Column(name = "R39_UP_TO_1_MONTH")
		private BigDecimal r39_upTo1Month;

		@Column(name = "R39_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r39_moreThan1MonthTo3Months;

		@Column(name = "R39_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r39_moreThan3MonthTo6Months;

		@Column(name = "R39_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r39_moreThan6MonthTo12Months;

		@Column(name = "R39_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r39_moreThan12MonthTo3Years;

		@Column(name = "R39_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r39_moreThan3YearsTo5Years;

		@Column(name = "R39_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r39_moreThan5YearsTo10Years;

		@Column(name = "R39_MORE_THAN_10_YEARS")
		private BigDecimal r39_moreThan10Years;

		@Column(name = "R39_total")
		private BigDecimal r39_total;

		// --- Group R40 ---
		@Column(name = "R40_PRODUCT")
		private String r40product;

		@Column(name = "R40_UP_TO_1_MONTH")
		private BigDecimal r40_upTo1Month;

		@Column(name = "R40_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r40_moreThan1MonthTo3Months;

		@Column(name = "R40_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r40_moreThan3MonthTo6Months;

		@Column(name = "R40_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r40_moreThan6MonthTo12Months;

		@Column(name = "R40_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r40_moreThan12MonthTo3Years;

		@Column(name = "R40_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r40_moreThan3YearsTo5Years;

		@Column(name = "R40_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r40_moreThan5YearsTo10Years;

		@Column(name = "R40_MORE_THAN_10_YEARS")
		private BigDecimal r40_moreThan10Years;

		@Column(name = "R40_total")
		private BigDecimal r40_total;

		// --- Group R41 ---
		@Column(name = "R41_PRODUCT")
		private String r41product;

		@Column(name = "R41_UP_TO_1_MONTH")
		private BigDecimal r41_upTo1Month;

		@Column(name = "R41_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r41_moreThan1MonthTo3Months;

		@Column(name = "R41_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r41_moreThan3MonthTo6Months;

		@Column(name = "R41_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r41_moreThan6MonthTo12Months;

		@Column(name = "R41_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r41_moreThan12MonthTo3Years;

		@Column(name = "R41_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r41_moreThan3YearsTo5Years;

		@Column(name = "R41_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r41_moreThan5YearsTo10Years;

		@Column(name = "R41_MORE_THAN_10_YEARS")
		private BigDecimal r41_moreThan10Years;

		@Column(name = "R41_total")
		private BigDecimal r41_total;

		// --- Group R42 ---
		@Column(name = "R42_PRODUCT")
		private String r42product;

		@Column(name = "R42_UP_TO_1_MONTH")
		private BigDecimal r42_upTo1Month;

		@Column(name = "R42_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r42_moreThan1MonthTo3Months;

		@Column(name = "R42_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r42_moreThan3MonthTo6Months;

		@Column(name = "R42_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r42_moreThan6MonthTo12Months;

		@Column(name = "R42_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r42_moreThan12MonthTo3Years;

		@Column(name = "R42_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r42_moreThan3YearsTo5Years;

		@Column(name = "R42_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r42_moreThan5YearsTo10Years;

		@Column(name = "R42_MORE_THAN_10_YEARS")
		private BigDecimal r42_moreThan10Years;

		@Column(name = "R42_total")
		private BigDecimal r42_total;

		// --- Group R43 ---
		@Column(name = "R43_PRODUCT")
		private String r43product;

		@Column(name = "R43_UP_TO_1_MONTH")
		private BigDecimal r43_upTo1Month;

		@Column(name = "R43_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r43_moreThan1MonthTo3Months;

		@Column(name = "R43_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r43_moreThan3MonthTo6Months;

		@Column(name = "R43_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r43_moreThan6MonthTo12Months;

		@Column(name = "R43_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r43_moreThan12MonthTo3Years;

		@Column(name = "R43_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r43_moreThan3YearsTo5Years;

		@Column(name = "R43_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r43_moreThan5YearsTo10Years;

		@Column(name = "R43_MORE_THAN_10_YEARS")
		private BigDecimal r43_moreThan10Years;

		@Column(name = "R43_total")
		private BigDecimal r43_total;

		// --- Group R44 ---
		@Column(name = "R44_PRODUCT")
		private String r44product;

		@Column(name = "R44_UP_TO_1_MONTH")
		private BigDecimal r44_upTo1Month;

		@Column(name = "R44_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r44_moreThan1MonthTo3Months;

		@Column(name = "R44_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r44_moreThan3MonthTo6Months;

		@Column(name = "R44_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r44_moreThan6MonthTo12Months;

		@Column(name = "R44_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r44_moreThan12MonthTo3Years;

		@Column(name = "R44_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r44_moreThan3YearsTo5Years;

		@Column(name = "R44_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r44_moreThan5YearsTo10Years;

		@Column(name = "R44_MORE_THAN_10_YEARS")
		private BigDecimal r44_moreThan10Years;

		@Column(name = "R44_total")
		private BigDecimal r44_total;

		// --- Group R45 ---
		@Column(name = "R45_PRODUCT")
		private String r45product;

		@Column(name = "R45_UP_TO_1_MONTH")
		private BigDecimal r45_upTo1Month;

		@Column(name = "R45_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r45_moreThan1MonthTo3Months;

		@Column(name = "R45_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r45_moreThan3MonthTo6Months;

		@Column(name = "R45_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r45_moreThan6MonthTo12Months;

		@Column(name = "R45_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r45_moreThan12MonthTo3Years;

		@Column(name = "R45_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r45_moreThan3YearsTo5Years;

		@Column(name = "R45_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r45_moreThan5YearsTo10Years;

		@Column(name = "R45_MORE_THAN_10_YEARS")
		private BigDecimal r45_moreThan10Years;

		@Column(name = "R45_total")
		private BigDecimal r45_total;

		// --- Group R46 ---
		@Column(name = "R46_PRODUCT")
		private String r46product;

		@Column(name = "R46_UP_TO_1_MONTH")
		private BigDecimal r46_upTo1Month;

		@Column(name = "R46_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r46_moreThan1MonthTo3Months;

		@Column(name = "R46_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r46_moreThan3MonthTo6Months;

		@Column(name = "R46_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r46_moreThan6MonthTo12Months;

		@Column(name = "R46_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r46_moreThan12MonthTo3Years;

		@Column(name = "R46_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r46_moreThan3YearsTo5Years;

		@Column(name = "R46_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r46_moreThan5YearsTo10Years;

		@Column(name = "R46_MORE_THAN_10_YEARS")
		private BigDecimal r46_moreThan10Years;

		@Column(name = "R46_total")
		private BigDecimal r46_total;

		// --- Group R47 ---
		@Column(name = "R47_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r47_nonRatioSensativeItems;

		@Column(name = "R47_total")
		private BigDecimal r47_total;

		// --- Group R48 ---
		@Column(name = "R48_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r48_nonRatioSensativeItems;

		@Column(name = "R48_total")
		private BigDecimal r48_total;

		// --- Group R49 ---
		@Column(name = "R49_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r49_nonRatioSensativeItems;

		@Column(name = "R49_total")
		private BigDecimal r49_total;

		// --- Group R50 ---
		@Column(name = "R50_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r50_nonRatioSensativeItems;

		@Column(name = "R50_total")
		private BigDecimal r50_total;

		// --- Group R51 ---
		@Column(name = "R51_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r51_nonRatioSensativeItems;

		@Column(name = "R51_total")
		private BigDecimal r51_total;

		// --- Group R52 ---
		@Column(name = "R52_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r52_nonRatioSensativeItems;

		@Column(name = "R52_total")
		private BigDecimal r52_total;

		// --- Group R53 ---
		@Column(name = "R53_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r53_nonRatioSensativeItems;

		@Column(name = "R53_total")
		private BigDecimal r53_total;

		// --- Group R54 ---
		@Column(name = "R54_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r54_nonRatioSensativeItems;

		@Column(name = "R54_total")
		private BigDecimal r54_total;

		// --- Group R55 ---
		@Column(name = "R55_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r55_nonRatioSensativeItems;

		@Column(name = "R55_total")
		private BigDecimal r55_total;

		// --- Group R56 ---
		@Column(name = "R56_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r56_nonRatioSensativeItems;

		@Column(name = "R56_total")
		private BigDecimal r56_total;

		// --- Group R57 ---
		@Column(name = "R57_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r57_nonRatioSensativeItems;

		@Column(name = "R57_total")
		private BigDecimal r57_total;

		// --- Group R58 ---
		@Column(name = "R58_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r58_nonRatioSensativeItems;

		@Column(name = "R58_total")
		private BigDecimal r58_total;

		// --- Group R59 ---
		@Column(name = "R59_PRODUCT")
		private String r59product;

		@Column(name = "R59_UP_TO_1_MONTH")
		private BigDecimal r59_upTo1Month;

		@Column(name = "R59_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r59_moreThan1MonthTo3Months;

		@Column(name = "R59_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r59_moreThan3MonthTo6Months;

		@Column(name = "R59_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r59_moreThan6MonthTo12Months;

		@Column(name = "R59_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r59_moreThan12MonthTo3Years;

		@Column(name = "R59_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r59_moreThan3YearsTo5Years;

		@Column(name = "R59_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r59_moreThan5YearsTo10Years;

		@Column(name = "R59_MORE_THAN_10_YEARS")
		private BigDecimal r59_moreThan10Years;

		@Column(name = "R59_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r59_nonRatioSensativeItems;

		@Column(name = "R59_total")
		private BigDecimal r59_total;

		// --- Group R60 ---
		@Column(name = "R60_PRODUCT")
		private String r60product;

		@Column(name = "R60_UP_TO_1_MONTH")
		private BigDecimal r60_upTo1Month;

		@Column(name = "R60_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r60_moreThan1MonthTo3Months;

		@Column(name = "R60_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r60_moreThan3MonthTo6Months;

		@Column(name = "R60_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r60_moreThan6MonthTo12Months;

		@Column(name = "R60_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r60_moreThan12MonthTo3Years;

		@Column(name = "R60_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r60_moreThan3YearsTo5Years;

		@Column(name = "R60_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r60_moreThan5YearsTo10Years;

		@Column(name = "R60_MORE_THAN_10_YEARS")
		private BigDecimal r60_moreThan10Years;

		@Column(name = "R60_total")
		private BigDecimal r60_total;

		// --- Group R61 ---
		@Column(name = "R61_PRODUCT")
		private String r61product;

		@Column(name = "R61_UP_TO_1_MONTH")
		private BigDecimal r61_upTo1Month;

		@Column(name = "R61_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r61_moreThan1MonthTo3Months;

		@Column(name = "R61_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r61_moreThan3MonthTo6Months;

		@Column(name = "R61_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r61_moreThan6MonthTo12Months;

		@Column(name = "R61_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r61_moreThan12MonthTo3Years;

		@Column(name = "R61_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r61_moreThan3YearsTo5Years;

		@Column(name = "R61_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r61_moreThan5YearsTo10Years;

		@Column(name = "R61_MORE_THAN_10_YEARS")
		private BigDecimal r61_moreThan10Years;

		@Column(name = "R61_total")
		private BigDecimal r61_total;

		// --- Group R62 ---
		@Column(name = "R62_PRODUCT")
		private String r62product;

		@Column(name = "R62_UP_TO_1_MONTH")
		private BigDecimal r62_upTo1Month;

		@Column(name = "R62_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r62_moreThan1MonthTo3Months;

		@Column(name = "R62_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r62_moreThan3MonthTo6Months;

		@Column(name = "R62_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r62_moreThan6MonthTo12Months;

		@Column(name = "R62_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r62_moreThan12MonthTo3Years;

		@Column(name = "R62_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r62_moreThan3YearsTo5Years;

		@Column(name = "R62_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r62_moreThan5YearsTo10Years;

		@Column(name = "R62_MORE_THAN_10_YEARS")
		private BigDecimal r62_moreThan10Years;

		@Column(name = "R62_total")
		private BigDecimal r62_total;

		// --- Group R63 ---
		@Column(name = "R63_PRODUCT")
		private String r63product;

		@Column(name = "R63_UP_TO_1_MONTH")
		private BigDecimal r63_upTo1Month;

		@Column(name = "R63_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r63_moreThan1MonthTo3Months;

		@Column(name = "R63_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r63_moreThan3MonthTo6Months;

		@Column(name = "R63_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r63_moreThan6MonthTo12Months;

		@Column(name = "R63_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r63_moreThan12MonthTo3Years;

		@Column(name = "R63_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r63_moreThan3YearsTo5Years;

		@Column(name = "R63_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r63_moreThan5YearsTo10Years;

		@Column(name = "R63_MORE_THAN_10_YEARS")
		private BigDecimal r63_moreThan10Years;

		@Column(name = "R63_total")
		private BigDecimal r63_total;

		// --- Group R64 ---
		@Column(name = "R64_PRODUCT")
		private String r64product;

		@Column(name = "R64_UP_TO_1_MONTH")
		private BigDecimal r64_upTo1Month;

		@Column(name = "R64_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r64_moreThan1MonthTo3Months;

		@Column(name = "R64_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r64_moreThan3MonthTo6Months;

		@Column(name = "R64_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r64_moreThan6MonthTo12Months;

		@Column(name = "R64_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r64_moreThan12MonthTo3Years;

		@Column(name = "R64_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r64_moreThan3YearsTo5Years;

		@Column(name = "R64_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r64_moreThan5YearsTo10Years;

		@Column(name = "R64_MORE_THAN_10_YEARS")
		private BigDecimal r64_moreThan10Years;

		@Column(name = "R64_total")
		private BigDecimal r64_total;

		// --- Group R65 ---
		@Column(name = "R65_PRODUCT")
		private String r65product;

		@Column(name = "R65_UP_TO_1_MONTH")
		private BigDecimal r65_upTo1Month;

		@Column(name = "R65_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r65_moreThan1MonthTo3Months;

		@Column(name = "R65_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r65_moreThan3MonthTo6Months;

		@Column(name = "R65_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r65_moreThan6MonthTo12Months;

		@Column(name = "R65_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r65_moreThan12MonthTo3Years;

		@Column(name = "R65_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r65_moreThan3YearsTo5Years;

		@Column(name = "R65_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r65_moreThan5YearsTo10Years;

		@Column(name = "R65_MORE_THAN_10_YEARS")
		private BigDecimal r65_moreThan10Years;

		@Column(name = "R65_total")
		private BigDecimal r65_total;

		// --- Group R66 ---
		@Column(name = "R66_PRODUCT")
		private String r66product;

		@Column(name = "R66_UP_TO_1_MONTH")
		private BigDecimal r66_upTo1Month;

		@Column(name = "R66_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r66_moreThan1MonthTo3Months;

		@Column(name = "R66_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r66_moreThan3MonthTo6Months;

		@Column(name = "R66_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r66_moreThan6MonthTo12Months;

		@Column(name = "R66_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r66_moreThan12MonthTo3Years;

		@Column(name = "R66_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r66_moreThan3YearsTo5Years;

		@Column(name = "R66_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r66_moreThan5YearsTo10Years;

		@Column(name = "R66_MORE_THAN_10_YEARS")
		private BigDecimal r66_moreThan10Years;

		@Column(name = "R66_total")
		private BigDecimal r66_total;

		// --- Group R67 ---
		@Column(name = "R67_PRODUCT")
		private String r67product;

		@Column(name = "R67_UP_TO_1_MONTH")
		private BigDecimal r67_upTo1Month;

		@Column(name = "R67_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r67_moreThan1MonthTo3Months;

		@Column(name = "R67_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r67_moreThan3MonthTo6Months;

		@Column(name = "R67_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r67_moreThan6MonthTo12Months;

		@Column(name = "R67_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r67_moreThan12MonthTo3Years;

		@Column(name = "R67_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r67_moreThan3YearsTo5Years;

		@Column(name = "R67_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r67_moreThan5YearsTo10Years;

		@Column(name = "R67_MORE_THAN_10_YEARS")
		private BigDecimal r67_moreThan10Years;

		@Column(name = "R67_total")
		private BigDecimal r67_total;

		// --- Group R68 ---
		@Column(name = "R68_PRODUCT")
		private String r68product;

		@Column(name = "R68_UP_TO_1_MONTH")
		private BigDecimal r68_upTo1Month;

		@Column(name = "R68_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r68_moreThan1MonthTo3Months;

		@Column(name = "R68_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r68_moreThan3MonthTo6Months;

		@Column(name = "R68_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r68_moreThan6MonthTo12Months;

		@Column(name = "R68_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r68_moreThan12MonthTo3Years;

		@Column(name = "R68_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r68_moreThan3YearsTo5Years;

		@Column(name = "R68_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r68_moreThan5YearsTo10Years;

		@Column(name = "R68_MORE_THAN_10_YEARS")
		private BigDecimal r68_moreThan10Years;

		@Column(name = "R68_total")
		private BigDecimal r68_total;

		// --- Group R69 ---
		@Column(name = "R69_PRODUCT")
		private String r69product;

		@Column(name = "R69_UP_TO_1_MONTH")
		private BigDecimal r69_upTo1Month;

		@Column(name = "R69_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r69_moreThan1MonthTo3Months;

		@Column(name = "R69_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r69_moreThan3MonthTo6Months;

		@Column(name = "R69_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r69_moreThan6MonthTo12Months;

		@Column(name = "R69_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r69_moreThan12MonthTo3Years;

		@Column(name = "R69_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r69_moreThan3YearsTo5Years;

		@Column(name = "R69_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r69_moreThan5YearsTo10Years;

		@Column(name = "R69_MORE_THAN_10_YEARS")
		private BigDecimal r69_moreThan10Years;

		@Column(name = "R69_total")
		private BigDecimal r69_total;

		// --- Group R70 ---
		@Column(name = "R70_PRODUCT")
		private String r70product;

		@Column(name = "R70_UP_TO_1_MONTH")
		private BigDecimal r70_upTo1Month;

		@Column(name = "R70_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r70_moreThan1MonthTo3Months;

		@Column(name = "R70_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r70_moreThan3MonthTo6Months;

		@Column(name = "R70_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r70_moreThan6MonthTo12Months;

		@Column(name = "R70_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r70_moreThan12MonthTo3Years;

		@Column(name = "R70_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r70_moreThan3YearsTo5Years;

		@Column(name = "R70_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r70_moreThan5YearsTo10Years;

		@Column(name = "R70_MORE_THAN_10_YEARS")
		private BigDecimal r70_moreThan10Years;

		@Column(name = "R70_total")
		private BigDecimal r70_total;

		// --- Group R71 ---
		@Column(name = "R71_PRODUCT")
		private String r71product;

		@Column(name = "R71_UP_TO_1_MONTH")
		private BigDecimal r71_upTo1Month;

		@Column(name = "R71_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r71_moreThan1MonthTo3Months;

		@Column(name = "R71_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r71_moreThan3MonthTo6Months;

		@Column(name = "R71_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r71_moreThan6MonthTo12Months;

		@Column(name = "R71_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r71_moreThan12MonthTo3Years;

		@Column(name = "R71_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r71_moreThan3YearsTo5Years;

		@Column(name = "R71_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r71_moreThan5YearsTo10Years;

		@Column(name = "R71_MORE_THAN_10_YEARS")
		private BigDecimal r71_moreThan10Years;

		@Column(name = "R71_total")
		private BigDecimal r71_total;

		// --- Group R72 ---
		@Column(name = "R72_PRODUCT")
		private String r72product;

		@Column(name = "R72_UP_TO_1_MONTH")
		private BigDecimal r72_upTo1Month;

		@Column(name = "R72_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r72_moreThan1MonthTo3Months;

		@Column(name = "R72_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r72_moreThan3MonthTo6Months;

		@Column(name = "R72_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r72_moreThan6MonthTo12Months;

		@Column(name = "R72_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r72_moreThan12MonthTo3Years;

		@Column(name = "R72_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r72_moreThan3YearsTo5Years;

		@Column(name = "R72_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r72_moreThan5YearsTo10Years;

		@Column(name = "R72_MORE_THAN_10_YEARS")
		private BigDecimal r72_moreThan10Years;

		@Column(name = "R72_total")
		private BigDecimal r72_total;

		// --- Group R73 ---
		@Column(name = "R73_PRODUCT")
		private String r73product;

		@Column(name = "R73_UP_TO_1_MONTH")
		private BigDecimal r73_upTo1Month;

		@Column(name = "R73_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r73_moreThan1MonthTo3Months;

		@Column(name = "R73_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r73_moreThan3MonthTo6Months;

		@Column(name = "R73_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r73_moreThan6MonthTo12Months;

		@Column(name = "R73_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r73_moreThan12MonthTo3Years;

		@Column(name = "R73_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r73_moreThan3YearsTo5Years;

		@Column(name = "R73_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r73_moreThan5YearsTo10Years;

		@Column(name = "R73_MORE_THAN_10_YEARS")
		private BigDecimal r73_moreThan10Years;

		@Column(name = "R73_total")
		private BigDecimal r73_total;

		// --- Group R74 ---
		@Column(name = "R74_PRODUCT")
		private String r74product;

		@Column(name = "R74_UP_TO_1_MONTH")
		private BigDecimal r74_upTo1Month;

		@Column(name = "R74_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r74_moreThan1MonthTo3Months;

		@Column(name = "R74_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r74_moreThan3MonthTo6Months;

		@Column(name = "R74_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r74_moreThan6MonthTo12Months;

		@Column(name = "R74_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r74_moreThan12MonthTo3Years;

		@Column(name = "R74_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r74_moreThan3YearsTo5Years;

		@Column(name = "R74_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r74_moreThan5YearsTo10Years;

		@Column(name = "R74_MORE_THAN_10_YEARS")
		private BigDecimal r74_moreThan10Years;

		@Column(name = "R74_total")
		private BigDecimal r74_total;

		// --- Group R75 ---
		@Column(name = "R75_PRODUCT")
		private String r75product;

		@Column(name = "R75_UP_TO_1_MONTH")
		private BigDecimal r75_upTo1Month;

		@Column(name = "R75_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r75_moreThan1MonthTo3Months;

		@Column(name = "R75_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r75_moreThan3MonthTo6Months;

		@Column(name = "R75_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r75_moreThan6MonthTo12Months;

		@Column(name = "R75_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r75_moreThan12MonthTo3Years;

		@Column(name = "R75_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r75_moreThan3YearsTo5Years;

		@Column(name = "R75_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r75_moreThan5YearsTo10Years;

		@Column(name = "R75_MORE_THAN_10_YEARS")
		private BigDecimal r75_moreThan10Years;

		@Column(name = "R75_total")
		private BigDecimal r75_total;

		// --- Group R76 ---
		@Column(name = "R76_PRODUCT")
		private String r76product;

		@Column(name = "R76_UP_TO_1_MONTH")
		private BigDecimal r76_upTo1Month;

		@Column(name = "R76_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r76_moreThan1MonthTo3Months;

		@Column(name = "R76_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r76_moreThan3MonthTo6Months;

		@Column(name = "R76_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r76_moreThan6MonthTo12Months;

		@Column(name = "R76_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r76_moreThan12MonthTo3Years;

		@Column(name = "R76_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r76_moreThan3YearsTo5Years;

		@Column(name = "R76_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r76_moreThan5YearsTo10Years;

		@Column(name = "R76_MORE_THAN_10_YEARS")
		private BigDecimal r76_moreThan10Years;

		@Column(name = "R76_total")
		private BigDecimal r76_total;

		// --- Group R77 ---
		@Column(name = "R77_PRODUCT")
		private String r77product;

		@Column(name = "R77_UP_TO_1_MONTH")
		private BigDecimal r77_upTo1Month;

		@Column(name = "R77_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r77_moreThan1MonthTo3Months;

		@Column(name = "R77_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r77_moreThan3MonthTo6Months;

		@Column(name = "R77_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r77_moreThan6MonthTo12Months;

		@Column(name = "R77_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r77_moreThan12MonthTo3Years;

		@Column(name = "R77_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r77_moreThan3YearsTo5Years;

		@Column(name = "R77_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r77_moreThan5YearsTo10Years;

		@Column(name = "R77_MORE_THAN_10_YEARS")
		private BigDecimal r77_moreThan10Years;

		@Column(name = "R77_total")
		private BigDecimal r77_total;

		// --- Group R78 ---
		@Column(name = "R78_PRODUCT")
		private String r78product;

		@Column(name = "R78_UP_TO_1_MONTH")
		private BigDecimal r78_upTo1Month;

		@Column(name = "R78_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r78_moreThan1MonthTo3Months;

		@Column(name = "R78_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r78_moreThan3MonthTo6Months;

		@Column(name = "R78_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r78_moreThan6MonthTo12Months;

		@Column(name = "R78_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r78_moreThan12MonthTo3Years;

		@Column(name = "R78_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r78_moreThan3YearsTo5Years;

		@Column(name = "R78_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r78_moreThan5YearsTo10Years;

		@Column(name = "R78_MORE_THAN_10_YEARS")
		private BigDecimal r78_moreThan10Years;

		@Column(name = "R78_total")
		private BigDecimal r78_total;

		// --- Group R79 ---
		@Column(name = "R79_PRODUCT")
		private String r79product;

		@Column(name = "R79_UP_TO_1_MONTH")
		private BigDecimal r79_upTo1Month;

		@Column(name = "R79_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r79_moreThan1MonthTo3Months;

		@Column(name = "R79_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r79_moreThan3MonthTo6Months;

		@Column(name = "R79_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r79_moreThan6MonthTo12Months;

		@Column(name = "R79_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r79_moreThan12MonthTo3Years;

		@Column(name = "R79_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r79_moreThan3YearsTo5Years;

		@Column(name = "R79_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r79_moreThan5YearsTo10Years;

		@Column(name = "R79_MORE_THAN_10_YEARS")
		private BigDecimal r79_moreThan10Years;

		@Column(name = "R79_total")
		private BigDecimal r79_total;

		// --- Group R80 ---
		@Column(name = "R80_PRODUCT")
		private String r80product;

		@Column(name = "R80_UP_TO_1_MONTH")
		private BigDecimal r80_upTo1Month;

		@Column(name = "R80_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r80_moreThan1MonthTo3Months;

		@Column(name = "R80_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r80_moreThan3MonthTo6Months;

		@Column(name = "R80_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r80_moreThan6MonthTo12Months;

		@Column(name = "R80_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r80_moreThan12MonthTo3Years;

		@Column(name = "R80_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r80_moreThan3YearsTo5Years;

		@Column(name = "R80_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r80_moreThan5YearsTo10Years;

		@Column(name = "R80_MORE_THAN_10_YEARS")
		private BigDecimal r80_moreThan10Years;

		@Column(name = "R80_total")
		private BigDecimal r80_total;

		// --- Group R81 ---
		@Column(name = "R81_PRODUCT")
		private String r81product;

		@Column(name = "R81_UP_TO_1_MONTH")
		private BigDecimal r81_upTo1Month;

		@Column(name = "R81_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r81_moreThan1MonthTo3Months;

		@Column(name = "R81_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r81_moreThan3MonthTo6Months;

		@Column(name = "R81_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r81_moreThan6MonthTo12Months;

		@Column(name = "R81_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r81_moreThan12MonthTo3Years;

		@Column(name = "R81_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r81_moreThan3YearsTo5Years;

		@Column(name = "R81_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r81_moreThan5YearsTo10Years;

		@Column(name = "R81_MORE_THAN_10_YEARS")
		private BigDecimal r81_moreThan10Years;

		@Column(name = "R81_total")
		private BigDecimal r81_total;

		// --- Group R82 ---
		@Column(name = "R82_PRODUCT")
		private String r82product;

		@Column(name = "R82_UP_TO_1_MONTH")
		private BigDecimal r82_upTo1Month;

		@Column(name = "R82_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r82_moreThan1MonthTo3Months;

		@Column(name = "R82_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r82_moreThan3MonthTo6Months;

		@Column(name = "R82_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r82_moreThan6MonthTo12Months;

		@Column(name = "R82_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r82_moreThan12MonthTo3Years;

		@Column(name = "R82_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r82_moreThan3YearsTo5Years;

		@Column(name = "R82_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r82_moreThan5YearsTo10Years;

		@Column(name = "R82_MORE_THAN_10_YEARS")
		private BigDecimal r82_moreThan10Years;

		@Column(name = "R82_total")
		private BigDecimal r82_total;

		// --- Group R83 ---
		@Column(name = "R83_PRODUCT")
		private String r83product;

		@Column(name = "R83_UP_TO_1_MONTH")
		private BigDecimal r83_upTo1Month;

		@Column(name = "R83_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r83_moreThan1MonthTo3Months;

		@Column(name = "R83_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r83_moreThan3MonthTo6Months;

		@Column(name = "R83_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r83_moreThan6MonthTo12Months;

		@Column(name = "R83_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r83_moreThan12MonthTo3Years;

		@Column(name = "R83_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r83_moreThan3YearsTo5Years;

		@Column(name = "R83_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r83_moreThan5YearsTo10Years;

		@Column(name = "R83_MORE_THAN_10_YEARS")
		private BigDecimal r83_moreThan10Years;

		@Column(name = "R83_total")
		private BigDecimal r83_total;

		// --- Group R84 ---
		@Column(name = "R84_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r84_nonRatioSensativeItems;

		@Column(name = "R84_total")
		private BigDecimal r84_total;

		// --- Group R85 ---
		@Column(name = "R85_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r85_nonRatioSensativeItems;

		@Column(name = "R85_total")
		private BigDecimal r85_total;

		// --- Group R86 ---
		@Column(name = "R86_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r86_nonRatioSensativeItems;

		@Column(name = "R86_total")
		private BigDecimal r86_total;

		// --- Group R87 ---
		@Column(name = "R87_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r87_nonRatioSensativeItems;

		@Column(name = "R87_total")
		private BigDecimal r87_total;

		// --- Group R88 ---
		@Column(name = "R88_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r88_nonRatioSensativeItems;

		@Column(name = "R88_total")
		private BigDecimal r88_total;

		// --- Group R89 ---
		@Column(name = "R89_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r89_nonRatioSensativeItems;

		@Column(name = "R89_total")
		private BigDecimal r89_total;

		// --- Group R90 ---
		@Column(name = "R90_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r90_nonRatioSensativeItems;

		@Column(name = "R90_total")
		private BigDecimal r90_total;

		// --- Group R91 ---
		@Column(name = "R91_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r91_nonRatioSensativeItems;

		@Column(name = "R91_total")
		private BigDecimal r91_total;

		// --- Group R92 ---
		@Column(name = "R92_PRODUCT")
		private String r92product;

		@Column(name = "R92_UP_TO_1_MONTH")
		private BigDecimal r92_upTo1Month;

		@Column(name = "R92_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r92_moreThan1MonthTo3Months;

		@Column(name = "R92_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r92_moreThan3MonthTo6Months;

		@Column(name = "R92_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r92_moreThan6MonthTo12Months;

		@Column(name = "R92_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r92_moreThan12MonthTo3Years;

		@Column(name = "R92_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r92_moreThan3YearsTo5Years;

		@Column(name = "R92_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r92_moreThan5YearsTo10Years;

		@Column(name = "R92_MORE_THAN_10_YEARS")
		private BigDecimal r92_moreThan10Years;

		@Column(name = "R92_total")
		private BigDecimal r92_total;

		// --- Group R93 ---
		@Column(name = "R93_PRODUCT")
		private String r93product;

		@Column(name = "R93_UP_TO_1_MONTH")
		private BigDecimal r93_upTo1Month;

		@Column(name = "R93_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r93_moreThan1MonthTo3Months;

		@Column(name = "R93_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r93_moreThan3MonthTo6Months;

		@Column(name = "R93_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r93_moreThan6MonthTo12Months;

		@Column(name = "R93_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r93_moreThan12MonthTo3Years;

		@Column(name = "R93_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r93_moreThan3YearsTo5Years;

		@Column(name = "R93_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r93_moreThan5YearsTo10Years;

		@Column(name = "R93_MORE_THAN_10_YEARS")
		private BigDecimal r93_moreThan10Years;

		@Column(name = "R93_total")
		private BigDecimal r93_total;

		// --- Group R94 ---
		@Column(name = "R94_PRODUCT")
		private String r94product;

		@Column(name = "R94_UP_TO_1_MONTH")
		private BigDecimal r94_upTo1Month;

		@Column(name = "R94_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r94_moreThan1MonthTo3Months;

		@Column(name = "R94_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r94_moreThan3MonthTo6Months;

		@Column(name = "R94_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r94_moreThan6MonthTo12Months;

		@Column(name = "R94_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r94_moreThan12MonthTo3Years;

		@Column(name = "R94_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r94_moreThan3YearsTo5Years;

		@Column(name = "R94_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r94_moreThan5YearsTo10Years;

		@Column(name = "R94_MORE_THAN_10_YEARS")
		private BigDecimal r94_moreThan10Years;

		@Column(name = "R94_total")
		private BigDecimal r94_total;

		// --- Group R95 ---
		@Column(name = "R95_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r95_nonRatioSensativeItems;

		@Column(name = "R95_total")
		private BigDecimal r95_total;

		// --- Group R96 ---
		@Column(name = "R96_PRODUCT")
		private String r96product;

		@Column(name = "R96_UP_TO_1_MONTH")
		private BigDecimal r96_upTo1Month;

		@Column(name = "R96_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r96_moreThan1MonthTo3Months;

		@Column(name = "R96_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r96_moreThan3MonthTo6Months;

		@Column(name = "R96_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r96_moreThan6MonthTo12Months;

		@Column(name = "R96_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r96_moreThan12MonthTo3Years;

		@Column(name = "R96_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r96_moreThan3YearsTo5Years;

		@Column(name = "R96_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r96_moreThan5YearsTo10Years;

		@Column(name = "R96_MORE_THAN_10_YEARS")
		private BigDecimal r96_moreThan10Years;

		@Column(name = "R96_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r96_nonRatioSensativeItems;

		@Column(name = "R96_total")
		private BigDecimal r96_total;

		// --- Group R97 ---
		@Column(name = "R97_PRODUCT")
		private String r97product;

		@Column(name = "R97_UP_TO_1_MONTH")
		private BigDecimal r97_upTo1Month;

		@Column(name = "R97_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r97_moreThan1MonthTo3Months;

		@Column(name = "R97_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r97_moreThan3MonthTo6Months;

		@Column(name = "R97_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r97_moreThan6MonthTo12Months;

		@Column(name = "R97_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r97_moreThan12MonthTo3Years;

		@Column(name = "R97_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r97_moreThan3YearsTo5Years;

		@Column(name = "R97_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r97_moreThan5YearsTo10Years;

		@Column(name = "R97_MORE_THAN_10_YEARS")
		private BigDecimal r97_moreThan10Years;

		@Column(name = "R97_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r97_nonRatioSensativeItems;

		@Column(name = "R97_total")
		private BigDecimal r97_total;

		// --- Group R98 ---
		@Column(name = "R98_PRODUCT")
		private String r98product;

		@Column(name = "R98_UP_TO_1_MONTH")
		private BigDecimal r98_upTo1Month;

		@Column(name = "R98_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r98_moreThan1MonthTo3Months;

		@Column(name = "R98_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r98_moreThan3MonthTo6Months;

		@Column(name = "R98_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r98_moreThan6MonthTo12Months;

		@Column(name = "R98_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r98_moreThan12MonthTo3Years;

		@Column(name = "R98_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r98_moreThan3YearsTo5Years;

		@Column(name = "R98_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r98_moreThan5YearsTo10Years;

		@Column(name = "R98_MORE_THAN_10_YEARS")
		private BigDecimal r98_moreThan10Years;

		@Column(name = "R98_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r98_nonRatioSensativeItems;

		@Column(name = "R98_total")
		private BigDecimal r98_total;

		// --- Group R99 ---
		@Column(name = "R99_PRODUCT")
		private String r99product;

		@Column(name = "R99_UP_TO_1_MONTH")
		private BigDecimal r99_upTo1Month;

		@Column(name = "R99_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r99_moreThan1MonthTo3Months;

		@Column(name = "R99_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r99_moreThan3MonthTo6Months;

		@Column(name = "R99_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r99_moreThan6MonthTo12Months;

		@Column(name = "R99_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r99_moreThan12MonthTo3Years;

		@Column(name = "R99_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r99_moreThan3YearsTo5Years;

		@Column(name = "R99_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r99_moreThan5YearsTo10Years;

		@Column(name = "R99_MORE_THAN_10_YEARS")
		private BigDecimal r99_moreThan10Years;

		@Column(name = "R99_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r99_nonRatioSensativeItems;

		@Column(name = "R99_total")
		private BigDecimal r99_total;

		// --- Group R100 ---
		@Column(name = "R100_PRODUCT")
		private String r100product;

		@Column(name = "R100_UP_TO_1_MONTH")
		private BigDecimal r100_upTo1Month;

		@Column(name = "R100_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r100_moreThan1MonthTo3Months;

		@Column(name = "R100_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r100_moreThan3MonthTo6Months;

		@Column(name = "R100_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r100_moreThan6MonthTo12Months;

		@Column(name = "R100_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r100_moreThan12MonthTo3Years;

		@Column(name = "R100_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r100_moreThan3YearsTo5Years;

		@Column(name = "R100_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r100_moreThan5YearsTo10Years;

		@Column(name = "R100_MORE_THAN_10_YEARS")
		private BigDecimal r100_moreThan10Years;

		@Column(name = "R100_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r100_nonRatioSensativeItems;

		@Column(name = "R100_total")
		private BigDecimal r100_total;

		// --- Group R101 ---
		@Column(name = "R101_PRODUCT")
		private String r101product;

		@Column(name = "R101_UP_TO_1_MONTH")
		private BigDecimal r101_upTo1Month;

		@Column(name = "R101_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r101_moreThan1MonthTo3Months;

		@Column(name = "R101_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r101_moreThan3MonthTo6Months;

		@Column(name = "R101_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r101_moreThan6MonthTo12Months;

		@Column(name = "R101_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r101_moreThan12MonthTo3Years;

		@Column(name = "R101_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r101_moreThan3YearsTo5Years;

		@Column(name = "R101_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r101_moreThan5YearsTo10Years;

		@Column(name = "R101_MORE_THAN_10_YEARS")
		private BigDecimal r101_moreThan10Years;

		@Column(name = "R101_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r101_nonRatioSensativeItems;

		@Column(name = "R101_total")
		private BigDecimal r101_total;

		// --- Group R102 ---
		@Column(name = "R102_PRODUCT")
		private String r102product;

		@Column(name = "R102_UP_TO_1_MONTH")
		private BigDecimal r102_upTo1Month;

		@Column(name = "R102_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r102_moreThan1MonthTo3Months;

		@Column(name = "R102_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r102_moreThan3MonthTo6Months;

		@Column(name = "R102_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r102_moreThan6MonthTo12Months;

		@Column(name = "R102_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r102_moreThan12MonthTo3Years;

		@Column(name = "R102_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r102_moreThan3YearsTo5Years;

		@Column(name = "R102_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r102_moreThan5YearsTo10Years;

		@Column(name = "R102_MORE_THAN_10_YEARS")
		private BigDecimal r102_moreThan10Years;

		@Column(name = "R102_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r102_nonRatioSensativeItems;

		@Column(name = "R102_total")
		private BigDecimal r102_total;

		// --- Group R103 ---
		@Column(name = "R103_PRODUCT")
		private String r103product;

		@Column(name = "R103_UP_TO_1_MONTH")
		private BigDecimal r103_upTo1Month;

		@Column(name = "R103_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r103_moreThan1MonthTo3Months;

		@Column(name = "R103_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r103_moreThan3MonthTo6Months;

		@Column(name = "R103_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r103_moreThan6MonthTo12Months;

		@Column(name = "R103_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r103_moreThan12MonthTo3Years;

		@Column(name = "R103_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r103_moreThan3YearsTo5Years;

		@Column(name = "R103_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r103_moreThan5YearsTo10Years;

		@Column(name = "R103_MORE_THAN_10_YEARS")
		private BigDecimal r103_moreThan10Years;

		@Column(name = "R103_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r103_nonRatioSensativeItems;

		@Column(name = "R103_total")
		private BigDecimal r103_total;

		// --- Group R104 ---
		@Column(name = "R104_PRODUCT")
		private String r104product;

		@Column(name = "R104_UP_TO_1_MONTH")
		private BigDecimal r104_upTo1Month;

		@Column(name = "R104_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r104_moreThan1MonthTo3Months;

		@Column(name = "R104_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r104_moreThan3MonthTo6Months;

		@Column(name = "R104_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r104_moreThan6MonthTo12Months;

		@Column(name = "R104_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r104_moreThan12MonthTo3Years;

		@Column(name = "R104_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r104_moreThan3YearsTo5Years;

		@Column(name = "R104_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r104_moreThan5YearsTo10Years;

		@Column(name = "R104_MORE_THAN_10_YEARS")
		private BigDecimal r104_moreThan10Years;

		@Column(name = "R104_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r104_nonRatioSensativeItems;

		@Column(name = "R104_total")
		private BigDecimal r104_total;

		// --- Group R105 ---
		@Column(name = "R105_PRODUCT")
		private String r105product;

		@Column(name = "R105_UP_TO_1_MONTH")
		private BigDecimal r105_upTo1Month;

		@Column(name = "R105_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r105_moreThan1MonthTo3Months;

		@Column(name = "R105_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r105_moreThan3MonthTo6Months;

		@Column(name = "R105_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r105_moreThan6MonthTo12Months;

		@Column(name = "R105_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r105_moreThan12MonthTo3Years;

		@Column(name = "R105_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r105_moreThan3YearsTo5Years;

		@Column(name = "R105_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r105_moreThan5YearsTo10Years;

		@Column(name = "R105_MORE_THAN_10_YEARS")
		private BigDecimal r105_moreThan10Years;

		@Column(name = "R105_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r105_nonRatioSensativeItems;

		@Column(name = "R105_total")
		private BigDecimal r105_total;

		// --- Group R106 ---
		@Column(name = "R106_PRODUCT")
		private String r106product;

		@Column(name = "R106_UP_TO_1_MONTH")
		private BigDecimal r106_upTo1Month;

		@Column(name = "R106_MORE_THAN_1_MONTH_TO_3_MONTHS")
		private BigDecimal r106_moreThan1MonthTo3Months;

		@Column(name = "R106_MORE_THAN_3_MONTH_TO_6_MONTHS")
		private BigDecimal r106_moreThan3MonthTo6Months;

		@Column(name = "R106_MORE_THAN_6_MONTH_TO_12_MONTHS")
		private BigDecimal r106_moreThan6MonthTo12Months;

		@Column(name = "R106_MORE_THAN_12_MONTH_TO_3_YEARS")
		private BigDecimal r106_moreThan12MonthTo3Years;

		@Column(name = "R106_MORE_THAN_3_YEARS_TO_5_YEARS")
		private BigDecimal r106_moreThan3YearsTo5Years;

		@Column(name = "R106_MORE_THAN_5_YEARS_TO_10_YEARS")
		private BigDecimal r106_moreThan5YearsTo10Years;

		@Column(name = "R106_MORE_THAN_10_YEARS")
		private BigDecimal r106_moreThan10Years;

		@Column(name = "R106_NON_RATIO_SENSATIVE_ITEMS")
		private BigDecimal r106_nonRatioSensativeItems;

		@Column(name = "R106_total")
		private BigDecimal r106_total;

		// --- Metadata Fields ---
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		
		@Column(name = "REPORT_DATE")
		private Date reportDate;

		@Column(name = "REPORT_VERSION")
		private BigDecimal reportVersion;

		@Column(name = "REPORT_FREQUENCY")
		private String reportFrequency;

		@Column(name = "REPORT_CODE")
		private String reportCode;

		@Column(name = "REPORT_DESC")
		private String reportDesc;

		@Column(name = "ENTITY_FLG")
		private String entityFlg;

		@Column(name = "MODIFY_FLG")
		private String modifyFlg;

		@Column(name = "DEL_FLG")
		private String delFlg;
		
		@Column(name = "REPORT_RESUBDATE")
		    @Temporal(TemporalType.TIMESTAMP)
		    private Date reportResubDate;
		 
		
		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

		public String getR10product() {
			return r10product;
		}

		public void setR10product(String r10product) {
			this.r10product = r10product;
		}

		public BigDecimal getR10_upTo1Month() {
			return r10_upTo1Month;
		}

		public void setR10_upTo1Month(BigDecimal r10_upTo1Month) {
			this.r10_upTo1Month = r10_upTo1Month;
		}

		public BigDecimal getR10_moreThan1MonthTo3Months() {
			return r10_moreThan1MonthTo3Months;
		}

		public void setR10_moreThan1MonthTo3Months(BigDecimal r10_moreThan1MonthTo3Months) {
			this.r10_moreThan1MonthTo3Months = r10_moreThan1MonthTo3Months;
		}

		public BigDecimal getR10_moreThan3MonthTo6Months() {
			return r10_moreThan3MonthTo6Months;
		}

		public void setR10_moreThan3MonthTo6Months(BigDecimal r10_moreThan3MonthTo6Months) {
			this.r10_moreThan3MonthTo6Months = r10_moreThan3MonthTo6Months;
		}

		public BigDecimal getR10_moreThan6MonthTo12Months() {
			return r10_moreThan6MonthTo12Months;
		}

		public void setR10_moreThan6MonthTo12Months(BigDecimal r10_moreThan6MonthTo12Months) {
			this.r10_moreThan6MonthTo12Months = r10_moreThan6MonthTo12Months;
		}

		public BigDecimal getR10_moreThan12MonthTo3Years() {
			return r10_moreThan12MonthTo3Years;
		}

		public void setR10_moreThan12MonthTo3Years(BigDecimal r10_moreThan12MonthTo3Years) {
			this.r10_moreThan12MonthTo3Years = r10_moreThan12MonthTo3Years;
		}

		public BigDecimal getR10_moreThan3YearsTo5Years() {
			return r10_moreThan3YearsTo5Years;
		}

		public void setR10_moreThan3YearsTo5Years(BigDecimal r10_moreThan3YearsTo5Years) {
			this.r10_moreThan3YearsTo5Years = r10_moreThan3YearsTo5Years;
		}

		public BigDecimal getR10_moreThan5YearsTo10Years() {
			return r10_moreThan5YearsTo10Years;
		}

		public void setR10_moreThan5YearsTo10Years(BigDecimal r10_moreThan5YearsTo10Years) {
			this.r10_moreThan5YearsTo10Years = r10_moreThan5YearsTo10Years;
		}

		public BigDecimal getR10_moreThan10Years() {
			return r10_moreThan10Years;
		}

		public void setR10_moreThan10Years(BigDecimal r10_moreThan10Years) {
			this.r10_moreThan10Years = r10_moreThan10Years;
		}

		public BigDecimal getR10_nonRatioSensativeItems() {
			return r10_nonRatioSensativeItems;
		}

		public void setR10_nonRatioSensativeItems(BigDecimal r10_nonRatioSensativeItems) {
			this.r10_nonRatioSensativeItems = r10_nonRatioSensativeItems;
		}

		public BigDecimal getR10_total() {
			return r10_total;
		}

		public void setR10_total(BigDecimal r10_total) {
			this.r10_total = r10_total;
		}

		public String getR11product() {
			return r11product;
		}

		public void setR11product(String r11product) {
			this.r11product = r11product;
		}

		public BigDecimal getR11_upTo1Month() {
			return r11_upTo1Month;
		}

		public void setR11_upTo1Month(BigDecimal r11_upTo1Month) {
			this.r11_upTo1Month = r11_upTo1Month;
		}

		public BigDecimal getR11_moreThan1MonthTo3Months() {
			return r11_moreThan1MonthTo3Months;
		}

		public void setR11_moreThan1MonthTo3Months(BigDecimal r11_moreThan1MonthTo3Months) {
			this.r11_moreThan1MonthTo3Months = r11_moreThan1MonthTo3Months;
		}

		public BigDecimal getR11_moreThan3MonthTo6Months() {
			return r11_moreThan3MonthTo6Months;
		}

		public void setR11_moreThan3MonthTo6Months(BigDecimal r11_moreThan3MonthTo6Months) {
			this.r11_moreThan3MonthTo6Months = r11_moreThan3MonthTo6Months;
		}

		public BigDecimal getR11_moreThan6MonthTo12Months() {
			return r11_moreThan6MonthTo12Months;
		}

		public void setR11_moreThan6MonthTo12Months(BigDecimal r11_moreThan6MonthTo12Months) {
			this.r11_moreThan6MonthTo12Months = r11_moreThan6MonthTo12Months;
		}

		public BigDecimal getR11_moreThan12MonthTo3Years() {
			return r11_moreThan12MonthTo3Years;
		}

		public void setR11_moreThan12MonthTo3Years(BigDecimal r11_moreThan12MonthTo3Years) {
			this.r11_moreThan12MonthTo3Years = r11_moreThan12MonthTo3Years;
		}

		public BigDecimal getR11_moreThan3YearsTo5Years() {
			return r11_moreThan3YearsTo5Years;
		}

		public void setR11_moreThan3YearsTo5Years(BigDecimal r11_moreThan3YearsTo5Years) {
			this.r11_moreThan3YearsTo5Years = r11_moreThan3YearsTo5Years;
		}

		public BigDecimal getR11_moreThan5YearsTo10Years() {
			return r11_moreThan5YearsTo10Years;
		}

		public void setR11_moreThan5YearsTo10Years(BigDecimal r11_moreThan5YearsTo10Years) {
			this.r11_moreThan5YearsTo10Years = r11_moreThan5YearsTo10Years;
		}

		public BigDecimal getR11_moreThan10Years() {
			return r11_moreThan10Years;
		}

		public void setR11_moreThan10Years(BigDecimal r11_moreThan10Years) {
			this.r11_moreThan10Years = r11_moreThan10Years;
		}

		public BigDecimal getR11_total() {
			return r11_total;
		}

		public void setR11_total(BigDecimal r11_total) {
			this.r11_total = r11_total;
		}

		public String getR12product() {
			return r12product;
		}

		public void setR12product(String r12product) {
			this.r12product = r12product;
		}

		public BigDecimal getR12_upTo1Month() {
			return r12_upTo1Month;
		}

		public void setR12_upTo1Month(BigDecimal r12_upTo1Month) {
			this.r12_upTo1Month = r12_upTo1Month;
		}

		public BigDecimal getR12_moreThan1MonthTo3Months() {
			return r12_moreThan1MonthTo3Months;
		}

		public void setR12_moreThan1MonthTo3Months(BigDecimal r12_moreThan1MonthTo3Months) {
			this.r12_moreThan1MonthTo3Months = r12_moreThan1MonthTo3Months;
		}

		public BigDecimal getR12_moreThan3MonthTo6Months() {
			return r12_moreThan3MonthTo6Months;
		}

		public void setR12_moreThan3MonthTo6Months(BigDecimal r12_moreThan3MonthTo6Months) {
			this.r12_moreThan3MonthTo6Months = r12_moreThan3MonthTo6Months;
		}

		public BigDecimal getR12_moreThan6MonthTo12Months() {
			return r12_moreThan6MonthTo12Months;
		}

		public void setR12_moreThan6MonthTo12Months(BigDecimal r12_moreThan6MonthTo12Months) {
			this.r12_moreThan6MonthTo12Months = r12_moreThan6MonthTo12Months;
		}

		public BigDecimal getR12_moreThan12MonthTo3Years() {
			return r12_moreThan12MonthTo3Years;
		}

		public void setR12_moreThan12MonthTo3Years(BigDecimal r12_moreThan12MonthTo3Years) {
			this.r12_moreThan12MonthTo3Years = r12_moreThan12MonthTo3Years;
		}

		public BigDecimal getR12_moreThan3YearsTo5Years() {
			return r12_moreThan3YearsTo5Years;
		}

		public void setR12_moreThan3YearsTo5Years(BigDecimal r12_moreThan3YearsTo5Years) {
			this.r12_moreThan3YearsTo5Years = r12_moreThan3YearsTo5Years;
		}

		public BigDecimal getR12_moreThan5YearsTo10Years() {
			return r12_moreThan5YearsTo10Years;
		}

		public void setR12_moreThan5YearsTo10Years(BigDecimal r12_moreThan5YearsTo10Years) {
			this.r12_moreThan5YearsTo10Years = r12_moreThan5YearsTo10Years;
		}

		public BigDecimal getR12_moreThan10Years() {
			return r12_moreThan10Years;
		}

		public void setR12_moreThan10Years(BigDecimal r12_moreThan10Years) {
			this.r12_moreThan10Years = r12_moreThan10Years;
		}

		public BigDecimal getR12_total() {
			return r12_total;
		}

		public void setR12_total(BigDecimal r12_total) {
			this.r12_total = r12_total;
		}

		

		public String getR13product() {
			return r13product;
		}

		public void setR13product(String r13product) {
			this.r13product = r13product;
		}

		public BigDecimal getR13_upTo1Month() {
			return r13_upTo1Month;
		}

		public void setR13_upTo1Month(BigDecimal r13_upTo1Month) {
			this.r13_upTo1Month = r13_upTo1Month;
		}

		public BigDecimal getR13_moreThan1MonthTo3Months() {
			return r13_moreThan1MonthTo3Months;
		}

		public void setR13_moreThan1MonthTo3Months(BigDecimal r13_moreThan1MonthTo3Months) {
			this.r13_moreThan1MonthTo3Months = r13_moreThan1MonthTo3Months;
		}

		public BigDecimal getR13_moreThan3MonthTo6Months() {
			return r13_moreThan3MonthTo6Months;
		}

		public void setR13_moreThan3MonthTo6Months(BigDecimal r13_moreThan3MonthTo6Months) {
			this.r13_moreThan3MonthTo6Months = r13_moreThan3MonthTo6Months;
		}

		public BigDecimal getR13_moreThan6MonthTo12Months() {
			return r13_moreThan6MonthTo12Months;
		}

		public void setR13_moreThan6MonthTo12Months(BigDecimal r13_moreThan6MonthTo12Months) {
			this.r13_moreThan6MonthTo12Months = r13_moreThan6MonthTo12Months;
		}

		public BigDecimal getR13_moreThan12MonthTo3Years() {
			return r13_moreThan12MonthTo3Years;
		}

		public void setR13_moreThan12MonthTo3Years(BigDecimal r13_moreThan12MonthTo3Years) {
			this.r13_moreThan12MonthTo3Years = r13_moreThan12MonthTo3Years;
		}

		public BigDecimal getR13_moreThan3YearsTo5Years() {
			return r13_moreThan3YearsTo5Years;
		}

		public void setR13_moreThan3YearsTo5Years(BigDecimal r13_moreThan3YearsTo5Years) {
			this.r13_moreThan3YearsTo5Years = r13_moreThan3YearsTo5Years;
		}

		public BigDecimal getR13_moreThan5YearsTo10Years() {
			return r13_moreThan5YearsTo10Years;
		}

		public void setR13_moreThan5YearsTo10Years(BigDecimal r13_moreThan5YearsTo10Years) {
			this.r13_moreThan5YearsTo10Years = r13_moreThan5YearsTo10Years;
		}

		public BigDecimal getR13_moreThan10Years() {
			return r13_moreThan10Years;
		}

		public void setR13_moreThan10Years(BigDecimal r13_moreThan10Years) {
			this.r13_moreThan10Years = r13_moreThan10Years;
		}

		public BigDecimal getR13_total() {
			return r13_total;
		}

		public void setR13_total(BigDecimal r13_total) {
			this.r13_total = r13_total;
		}

		public String getR14product() {
			return r14product;
		}

		public void setR14product(String r14product) {
			this.r14product = r14product;
		}

		public BigDecimal getR14_upTo1Month() {
			return r14_upTo1Month;
		}

		public void setR14_upTo1Month(BigDecimal r14_upTo1Month) {
			this.r14_upTo1Month = r14_upTo1Month;
		}

		public BigDecimal getR14_moreThan1MonthTo3Months() {
			return r14_moreThan1MonthTo3Months;
		}

		public void setR14_moreThan1MonthTo3Months(BigDecimal r14_moreThan1MonthTo3Months) {
			this.r14_moreThan1MonthTo3Months = r14_moreThan1MonthTo3Months;
		}

		public BigDecimal getR14_moreThan3MonthTo6Months() {
			return r14_moreThan3MonthTo6Months;
		}

		public void setR14_moreThan3MonthTo6Months(BigDecimal r14_moreThan3MonthTo6Months) {
			this.r14_moreThan3MonthTo6Months = r14_moreThan3MonthTo6Months;
		}

		public BigDecimal getR14_moreThan6MonthTo12Months() {
			return r14_moreThan6MonthTo12Months;
		}

		public void setR14_moreThan6MonthTo12Months(BigDecimal r14_moreThan6MonthTo12Months) {
			this.r14_moreThan6MonthTo12Months = r14_moreThan6MonthTo12Months;
		}

		public BigDecimal getR14_moreThan12MonthTo3Years() {
			return r14_moreThan12MonthTo3Years;
		}

		public void setR14_moreThan12MonthTo3Years(BigDecimal r14_moreThan12MonthTo3Years) {
			this.r14_moreThan12MonthTo3Years = r14_moreThan12MonthTo3Years;
		}

		public BigDecimal getR14_moreThan3YearsTo5Years() {
			return r14_moreThan3YearsTo5Years;
		}

		public void setR14_moreThan3YearsTo5Years(BigDecimal r14_moreThan3YearsTo5Years) {
			this.r14_moreThan3YearsTo5Years = r14_moreThan3YearsTo5Years;
		}

		public BigDecimal getR14_moreThan5YearsTo10Years() {
			return r14_moreThan5YearsTo10Years;
		}

		public void setR14_moreThan5YearsTo10Years(BigDecimal r14_moreThan5YearsTo10Years) {
			this.r14_moreThan5YearsTo10Years = r14_moreThan5YearsTo10Years;
		}

		public BigDecimal getR14_moreThan10Years() {
			return r14_moreThan10Years;
		}

		public void setR14_moreThan10Years(BigDecimal r14_moreThan10Years) {
			this.r14_moreThan10Years = r14_moreThan10Years;
		}

		public BigDecimal getR14_total() {
			return r14_total;
		}

		public void setR14_total(BigDecimal r14_total) {
			this.r14_total = r14_total;
		}

		public String getR15product() {
			return r15product;
		}

		public void setR15product(String r15product) {
			this.r15product = r15product;
		}

		public BigDecimal getR15_upTo1Month() {
			return r15_upTo1Month;
		}

		public void setR15_upTo1Month(BigDecimal r15_upTo1Month) {
			this.r15_upTo1Month = r15_upTo1Month;
		}

		public BigDecimal getR15_moreThan1MonthTo3Months() {
			return r15_moreThan1MonthTo3Months;
		}

		public void setR15_moreThan1MonthTo3Months(BigDecimal r15_moreThan1MonthTo3Months) {
			this.r15_moreThan1MonthTo3Months = r15_moreThan1MonthTo3Months;
		}

		public BigDecimal getR15_moreThan3MonthTo6Months() {
			return r15_moreThan3MonthTo6Months;
		}

		public void setR15_moreThan3MonthTo6Months(BigDecimal r15_moreThan3MonthTo6Months) {
			this.r15_moreThan3MonthTo6Months = r15_moreThan3MonthTo6Months;
		}

		public BigDecimal getR15_moreThan6MonthTo12Months() {
			return r15_moreThan6MonthTo12Months;
		}

		public void setR15_moreThan6MonthTo12Months(BigDecimal r15_moreThan6MonthTo12Months) {
			this.r15_moreThan6MonthTo12Months = r15_moreThan6MonthTo12Months;
		}

		public BigDecimal getR15_moreThan12MonthTo3Years() {
			return r15_moreThan12MonthTo3Years;
		}

		public void setR15_moreThan12MonthTo3Years(BigDecimal r15_moreThan12MonthTo3Years) {
			this.r15_moreThan12MonthTo3Years = r15_moreThan12MonthTo3Years;
		}

		public BigDecimal getR15_moreThan3YearsTo5Years() {
			return r15_moreThan3YearsTo5Years;
		}

		public void setR15_moreThan3YearsTo5Years(BigDecimal r15_moreThan3YearsTo5Years) {
			this.r15_moreThan3YearsTo5Years = r15_moreThan3YearsTo5Years;
		}

		public BigDecimal getR15_moreThan5YearsTo10Years() {
			return r15_moreThan5YearsTo10Years;
		}

		public void setR15_moreThan5YearsTo10Years(BigDecimal r15_moreThan5YearsTo10Years) {
			this.r15_moreThan5YearsTo10Years = r15_moreThan5YearsTo10Years;
		}

		public BigDecimal getR15_moreThan10Years() {
			return r15_moreThan10Years;
		}

		public void setR15_moreThan10Years(BigDecimal r15_moreThan10Years) {
			this.r15_moreThan10Years = r15_moreThan10Years;
		}

		public BigDecimal getR15_total() {
			return r15_total;
		}

		public void setR15_total(BigDecimal r15_total) {
			this.r15_total = r15_total;
		}

		public String getR16product() {
			return r16product;
		}

		public void setR16product(String r16product) {
			this.r16product = r16product;
		}

		public BigDecimal getR16_upTo1Month() {
			return r16_upTo1Month;
		}

		public void setR16_upTo1Month(BigDecimal r16_upTo1Month) {
			this.r16_upTo1Month = r16_upTo1Month;
		}

		public BigDecimal getR16_moreThan1MonthTo3Months() {
			return r16_moreThan1MonthTo3Months;
		}

		public void setR16_moreThan1MonthTo3Months(BigDecimal r16_moreThan1MonthTo3Months) {
			this.r16_moreThan1MonthTo3Months = r16_moreThan1MonthTo3Months;
		}

		public BigDecimal getR16_moreThan3MonthTo6Months() {
			return r16_moreThan3MonthTo6Months;
		}

		public void setR16_moreThan3MonthTo6Months(BigDecimal r16_moreThan3MonthTo6Months) {
			this.r16_moreThan3MonthTo6Months = r16_moreThan3MonthTo6Months;
		}

		public BigDecimal getR16_moreThan6MonthTo12Months() {
			return r16_moreThan6MonthTo12Months;
		}

		public void setR16_moreThan6MonthTo12Months(BigDecimal r16_moreThan6MonthTo12Months) {
			this.r16_moreThan6MonthTo12Months = r16_moreThan6MonthTo12Months;
		}

		public BigDecimal getR16_moreThan12MonthTo3Years() {
			return r16_moreThan12MonthTo3Years;
		}

		public void setR16_moreThan12MonthTo3Years(BigDecimal r16_moreThan12MonthTo3Years) {
			this.r16_moreThan12MonthTo3Years = r16_moreThan12MonthTo3Years;
		}

		public BigDecimal getR16_moreThan3YearsTo5Years() {
			return r16_moreThan3YearsTo5Years;
		}

		public void setR16_moreThan3YearsTo5Years(BigDecimal r16_moreThan3YearsTo5Years) {
			this.r16_moreThan3YearsTo5Years = r16_moreThan3YearsTo5Years;
		}

		public BigDecimal getR16_moreThan5YearsTo10Years() {
			return r16_moreThan5YearsTo10Years;
		}

		public void setR16_moreThan5YearsTo10Years(BigDecimal r16_moreThan5YearsTo10Years) {
			this.r16_moreThan5YearsTo10Years = r16_moreThan5YearsTo10Years;
		}

		public BigDecimal getR16_moreThan10Years() {
			return r16_moreThan10Years;
		}

		public void setR16_moreThan10Years(BigDecimal r16_moreThan10Years) {
			this.r16_moreThan10Years = r16_moreThan10Years;
		}

		public BigDecimal getR16_total() {
			return r16_total;
		}

		public void setR16_total(BigDecimal r16_total) {
			this.r16_total = r16_total;
		}

		public String getR17product() {
			return r17product;
		}

		public void setR17product(String r17product) {
			this.r17product = r17product;
		}

		public BigDecimal getR17_upTo1Month() {
			return r17_upTo1Month;
		}

		public void setR17_upTo1Month(BigDecimal r17_upTo1Month) {
			this.r17_upTo1Month = r17_upTo1Month;
		}

		public BigDecimal getR17_moreThan1MonthTo3Months() {
			return r17_moreThan1MonthTo3Months;
		}

		public void setR17_moreThan1MonthTo3Months(BigDecimal r17_moreThan1MonthTo3Months) {
			this.r17_moreThan1MonthTo3Months = r17_moreThan1MonthTo3Months;
		}

		public BigDecimal getR17_moreThan3MonthTo6Months() {
			return r17_moreThan3MonthTo6Months;
		}

		public void setR17_moreThan3MonthTo6Months(BigDecimal r17_moreThan3MonthTo6Months) {
			this.r17_moreThan3MonthTo6Months = r17_moreThan3MonthTo6Months;
		}

		public BigDecimal getR17_moreThan6MonthTo12Months() {
			return r17_moreThan6MonthTo12Months;
		}

		public void setR17_moreThan6MonthTo12Months(BigDecimal r17_moreThan6MonthTo12Months) {
			this.r17_moreThan6MonthTo12Months = r17_moreThan6MonthTo12Months;
		}

		public BigDecimal getR17_moreThan12MonthTo3Years() {
			return r17_moreThan12MonthTo3Years;
		}

		public void setR17_moreThan12MonthTo3Years(BigDecimal r17_moreThan12MonthTo3Years) {
			this.r17_moreThan12MonthTo3Years = r17_moreThan12MonthTo3Years;
		}

		public BigDecimal getR17_moreThan3YearsTo5Years() {
			return r17_moreThan3YearsTo5Years;
		}

		public void setR17_moreThan3YearsTo5Years(BigDecimal r17_moreThan3YearsTo5Years) {
			this.r17_moreThan3YearsTo5Years = r17_moreThan3YearsTo5Years;
		}

		public BigDecimal getR17_moreThan5YearsTo10Years() {
			return r17_moreThan5YearsTo10Years;
		}

		public void setR17_moreThan5YearsTo10Years(BigDecimal r17_moreThan5YearsTo10Years) {
			this.r17_moreThan5YearsTo10Years = r17_moreThan5YearsTo10Years;
		}

		public BigDecimal getR17_moreThan10Years() {
			return r17_moreThan10Years;
		}

		public void setR17_moreThan10Years(BigDecimal r17_moreThan10Years) {
			this.r17_moreThan10Years = r17_moreThan10Years;
		}

		public BigDecimal getR17_total() {
			return r17_total;
		}

		public void setR17_total(BigDecimal r17_total) {
			this.r17_total = r17_total;
		}

		public String getR18product() {
			return r18product;
		}

		public void setR18product(String r18product) {
			this.r18product = r18product;
		}

		public BigDecimal getR18_upTo1Month() {
			return r18_upTo1Month;
		}

		public void setR18_upTo1Month(BigDecimal r18_upTo1Month) {
			this.r18_upTo1Month = r18_upTo1Month;
		}

		public BigDecimal getR18_moreThan1MonthTo3Months() {
			return r18_moreThan1MonthTo3Months;
		}

		public void setR18_moreThan1MonthTo3Months(BigDecimal r18_moreThan1MonthTo3Months) {
			this.r18_moreThan1MonthTo3Months = r18_moreThan1MonthTo3Months;
		}

		public BigDecimal getR18_moreThan3MonthTo6Months() {
			return r18_moreThan3MonthTo6Months;
		}

		public void setR18_moreThan3MonthTo6Months(BigDecimal r18_moreThan3MonthTo6Months) {
			this.r18_moreThan3MonthTo6Months = r18_moreThan3MonthTo6Months;
		}

		public BigDecimal getR18_moreThan6MonthTo12Months() {
			return r18_moreThan6MonthTo12Months;
		}

		public void setR18_moreThan6MonthTo12Months(BigDecimal r18_moreThan6MonthTo12Months) {
			this.r18_moreThan6MonthTo12Months = r18_moreThan6MonthTo12Months;
		}

		public BigDecimal getR18_moreThan12MonthTo3Years() {
			return r18_moreThan12MonthTo3Years;
		}

		public void setR18_moreThan12MonthTo3Years(BigDecimal r18_moreThan12MonthTo3Years) {
			this.r18_moreThan12MonthTo3Years = r18_moreThan12MonthTo3Years;
		}

		public BigDecimal getR18_moreThan3YearsTo5Years() {
			return r18_moreThan3YearsTo5Years;
		}

		public void setR18_moreThan3YearsTo5Years(BigDecimal r18_moreThan3YearsTo5Years) {
			this.r18_moreThan3YearsTo5Years = r18_moreThan3YearsTo5Years;
		}

		public BigDecimal getR18_moreThan5YearsTo10Years() {
			return r18_moreThan5YearsTo10Years;
		}

		public void setR18_moreThan5YearsTo10Years(BigDecimal r18_moreThan5YearsTo10Years) {
			this.r18_moreThan5YearsTo10Years = r18_moreThan5YearsTo10Years;
		}

		public BigDecimal getR18_moreThan10Years() {
			return r18_moreThan10Years;
		}

		public void setR18_moreThan10Years(BigDecimal r18_moreThan10Years) {
			this.r18_moreThan10Years = r18_moreThan10Years;
		}

		public BigDecimal getR18_total() {
			return r18_total;
		}

		public void setR18_total(BigDecimal r18_total) {
			this.r18_total = r18_total;
		}

		public String getR19product() {
			return r19product;
		}

		public void setR19product(String r19product) {
			this.r19product = r19product;
		}

		public BigDecimal getR19_upTo1Month() {
			return r19_upTo1Month;
		}

		public void setR19_upTo1Month(BigDecimal r19_upTo1Month) {
			this.r19_upTo1Month = r19_upTo1Month;
		}

		public BigDecimal getR19_moreThan1MonthTo3Months() {
			return r19_moreThan1MonthTo3Months;
		}

		public void setR19_moreThan1MonthTo3Months(BigDecimal r19_moreThan1MonthTo3Months) {
			this.r19_moreThan1MonthTo3Months = r19_moreThan1MonthTo3Months;
		}

		public BigDecimal getR19_moreThan3MonthTo6Months() {
			return r19_moreThan3MonthTo6Months;
		}

		public void setR19_moreThan3MonthTo6Months(BigDecimal r19_moreThan3MonthTo6Months) {
			this.r19_moreThan3MonthTo6Months = r19_moreThan3MonthTo6Months;
		}

		public BigDecimal getR19_moreThan6MonthTo12Months() {
			return r19_moreThan6MonthTo12Months;
		}

		public void setR19_moreThan6MonthTo12Months(BigDecimal r19_moreThan6MonthTo12Months) {
			this.r19_moreThan6MonthTo12Months = r19_moreThan6MonthTo12Months;
		}

		public BigDecimal getR19_moreThan12MonthTo3Years() {
			return r19_moreThan12MonthTo3Years;
		}

		public void setR19_moreThan12MonthTo3Years(BigDecimal r19_moreThan12MonthTo3Years) {
			this.r19_moreThan12MonthTo3Years = r19_moreThan12MonthTo3Years;
		}

		public BigDecimal getR19_moreThan3YearsTo5Years() {
			return r19_moreThan3YearsTo5Years;
		}

		public void setR19_moreThan3YearsTo5Years(BigDecimal r19_moreThan3YearsTo5Years) {
			this.r19_moreThan3YearsTo5Years = r19_moreThan3YearsTo5Years;
		}

		public BigDecimal getR19_moreThan5YearsTo10Years() {
			return r19_moreThan5YearsTo10Years;
		}

		public void setR19_moreThan5YearsTo10Years(BigDecimal r19_moreThan5YearsTo10Years) {
			this.r19_moreThan5YearsTo10Years = r19_moreThan5YearsTo10Years;
		}

		public BigDecimal getR19_moreThan10Years() {
			return r19_moreThan10Years;
		}

		public void setR19_moreThan10Years(BigDecimal r19_moreThan10Years) {
			this.r19_moreThan10Years = r19_moreThan10Years;
		}

		public BigDecimal getR19_total() {
			return r19_total;
		}

		public void setR19_total(BigDecimal r19_total) {
			this.r19_total = r19_total;
		}

		public String getR20product() {
			return r20product;
		}

		public void setR20product(String r20product) {
			this.r20product = r20product;
		}

		public BigDecimal getR20_upTo1Month() {
			return r20_upTo1Month;
		}

		public void setR20_upTo1Month(BigDecimal r20_upTo1Month) {
			this.r20_upTo1Month = r20_upTo1Month;
		}

		public BigDecimal getR20_moreThan1MonthTo3Months() {
			return r20_moreThan1MonthTo3Months;
		}

		public void setR20_moreThan1MonthTo3Months(BigDecimal r20_moreThan1MonthTo3Months) {
			this.r20_moreThan1MonthTo3Months = r20_moreThan1MonthTo3Months;
		}

		public BigDecimal getR20_moreThan3MonthTo6Months() {
			return r20_moreThan3MonthTo6Months;
		}

		public void setR20_moreThan3MonthTo6Months(BigDecimal r20_moreThan3MonthTo6Months) {
			this.r20_moreThan3MonthTo6Months = r20_moreThan3MonthTo6Months;
		}

		public BigDecimal getR20_moreThan6MonthTo12Months() {
			return r20_moreThan6MonthTo12Months;
		}

		public void setR20_moreThan6MonthTo12Months(BigDecimal r20_moreThan6MonthTo12Months) {
			this.r20_moreThan6MonthTo12Months = r20_moreThan6MonthTo12Months;
		}

		public BigDecimal getR20_moreThan12MonthTo3Years() {
			return r20_moreThan12MonthTo3Years;
		}

		public void setR20_moreThan12MonthTo3Years(BigDecimal r20_moreThan12MonthTo3Years) {
			this.r20_moreThan12MonthTo3Years = r20_moreThan12MonthTo3Years;
		}

		public BigDecimal getR20_moreThan3YearsTo5Years() {
			return r20_moreThan3YearsTo5Years;
		}

		public void setR20_moreThan3YearsTo5Years(BigDecimal r20_moreThan3YearsTo5Years) {
			this.r20_moreThan3YearsTo5Years = r20_moreThan3YearsTo5Years;
		}

		public BigDecimal getR20_moreThan5YearsTo10Years() {
			return r20_moreThan5YearsTo10Years;
		}

		public void setR20_moreThan5YearsTo10Years(BigDecimal r20_moreThan5YearsTo10Years) {
			this.r20_moreThan5YearsTo10Years = r20_moreThan5YearsTo10Years;
		}

		public BigDecimal getR20_moreThan10Years() {
			return r20_moreThan10Years;
		}

		public void setR20_moreThan10Years(BigDecimal r20_moreThan10Years) {
			this.r20_moreThan10Years = r20_moreThan10Years;
		}

		public BigDecimal getR20_total() {
			return r20_total;
		}

		public void setR20_total(BigDecimal r20_total) {
			this.r20_total = r20_total;
		}

		public String getR21product() {
			return r21product;
		}

		public void setR21product(String r21product) {
			this.r21product = r21product;
		}

		public BigDecimal getR21_upTo1Month() {
			return r21_upTo1Month;
		}

		public void setR21_upTo1Month(BigDecimal r21_upTo1Month) {
			this.r21_upTo1Month = r21_upTo1Month;
		}

		public BigDecimal getR21_moreThan1MonthTo3Months() {
			return r21_moreThan1MonthTo3Months;
		}

		public void setR21_moreThan1MonthTo3Months(BigDecimal r21_moreThan1MonthTo3Months) {
			this.r21_moreThan1MonthTo3Months = r21_moreThan1MonthTo3Months;
		}

		public BigDecimal getR21_moreThan3MonthTo6Months() {
			return r21_moreThan3MonthTo6Months;
		}

		public void setR21_moreThan3MonthTo6Months(BigDecimal r21_moreThan3MonthTo6Months) {
			this.r21_moreThan3MonthTo6Months = r21_moreThan3MonthTo6Months;
		}

		public BigDecimal getR21_moreThan6MonthTo12Months() {
			return r21_moreThan6MonthTo12Months;
		}

		public void setR21_moreThan6MonthTo12Months(BigDecimal r21_moreThan6MonthTo12Months) {
			this.r21_moreThan6MonthTo12Months = r21_moreThan6MonthTo12Months;
		}

		public BigDecimal getR21_moreThan12MonthTo3Years() {
			return r21_moreThan12MonthTo3Years;
		}

		public void setR21_moreThan12MonthTo3Years(BigDecimal r21_moreThan12MonthTo3Years) {
			this.r21_moreThan12MonthTo3Years = r21_moreThan12MonthTo3Years;
		}

		public BigDecimal getR21_moreThan3YearsTo5Years() {
			return r21_moreThan3YearsTo5Years;
		}

		public void setR21_moreThan3YearsTo5Years(BigDecimal r21_moreThan3YearsTo5Years) {
			this.r21_moreThan3YearsTo5Years = r21_moreThan3YearsTo5Years;
		}

		public BigDecimal getR21_moreThan5YearsTo10Years() {
			return r21_moreThan5YearsTo10Years;
		}

		public void setR21_moreThan5YearsTo10Years(BigDecimal r21_moreThan5YearsTo10Years) {
			this.r21_moreThan5YearsTo10Years = r21_moreThan5YearsTo10Years;
		}

		public BigDecimal getR21_moreThan10Years() {
			return r21_moreThan10Years;
		}

		public void setR21_moreThan10Years(BigDecimal r21_moreThan10Years) {
			this.r21_moreThan10Years = r21_moreThan10Years;
		}

		public BigDecimal getR21_total() {
			return r21_total;
		}

		public void setR21_total(BigDecimal r21_total) {
			this.r21_total = r21_total;
		}

		public String getR22product() {
			return r22product;
		}

		public void setR22product(String r22product) {
			this.r22product = r22product;
		}

		public BigDecimal getR22_upTo1Month() {
			return r22_upTo1Month;
		}

		public void setR22_upTo1Month(BigDecimal r22_upTo1Month) {
			this.r22_upTo1Month = r22_upTo1Month;
		}

		public BigDecimal getR22_moreThan1MonthTo3Months() {
			return r22_moreThan1MonthTo3Months;
		}

		public void setR22_moreThan1MonthTo3Months(BigDecimal r22_moreThan1MonthTo3Months) {
			this.r22_moreThan1MonthTo3Months = r22_moreThan1MonthTo3Months;
		}

		public BigDecimal getR22_moreThan3MonthTo6Months() {
			return r22_moreThan3MonthTo6Months;
		}

		public void setR22_moreThan3MonthTo6Months(BigDecimal r22_moreThan3MonthTo6Months) {
			this.r22_moreThan3MonthTo6Months = r22_moreThan3MonthTo6Months;
		}

		public BigDecimal getR22_moreThan6MonthTo12Months() {
			return r22_moreThan6MonthTo12Months;
		}

		public void setR22_moreThan6MonthTo12Months(BigDecimal r22_moreThan6MonthTo12Months) {
			this.r22_moreThan6MonthTo12Months = r22_moreThan6MonthTo12Months;
		}

		public BigDecimal getR22_moreThan12MonthTo3Years() {
			return r22_moreThan12MonthTo3Years;
		}

		public void setR22_moreThan12MonthTo3Years(BigDecimal r22_moreThan12MonthTo3Years) {
			this.r22_moreThan12MonthTo3Years = r22_moreThan12MonthTo3Years;
		}

		public BigDecimal getR22_moreThan3YearsTo5Years() {
			return r22_moreThan3YearsTo5Years;
		}

		public void setR22_moreThan3YearsTo5Years(BigDecimal r22_moreThan3YearsTo5Years) {
			this.r22_moreThan3YearsTo5Years = r22_moreThan3YearsTo5Years;
		}

		public BigDecimal getR22_moreThan5YearsTo10Years() {
			return r22_moreThan5YearsTo10Years;
		}

		public void setR22_moreThan5YearsTo10Years(BigDecimal r22_moreThan5YearsTo10Years) {
			this.r22_moreThan5YearsTo10Years = r22_moreThan5YearsTo10Years;
		}

		public BigDecimal getR22_moreThan10Years() {
			return r22_moreThan10Years;
		}

		public void setR22_moreThan10Years(BigDecimal r22_moreThan10Years) {
			this.r22_moreThan10Years = r22_moreThan10Years;
		}

		public BigDecimal getR22_total() {
			return r22_total;
		}

		public void setR22_total(BigDecimal r22_total) {
			this.r22_total = r22_total;
		}

		public String getR23product() {
			return r23product;
		}

		public void setR23product(String r23product) {
			this.r23product = r23product;
		}

		public BigDecimal getR23_upTo1Month() {
			return r23_upTo1Month;
		}

		public void setR23_upTo1Month(BigDecimal r23_upTo1Month) {
			this.r23_upTo1Month = r23_upTo1Month;
		}

		public BigDecimal getR23_moreThan1MonthTo3Months() {
			return r23_moreThan1MonthTo3Months;
		}

		public void setR23_moreThan1MonthTo3Months(BigDecimal r23_moreThan1MonthTo3Months) {
			this.r23_moreThan1MonthTo3Months = r23_moreThan1MonthTo3Months;
		}

		public BigDecimal getR23_moreThan3MonthTo6Months() {
			return r23_moreThan3MonthTo6Months;
		}

		public void setR23_moreThan3MonthTo6Months(BigDecimal r23_moreThan3MonthTo6Months) {
			this.r23_moreThan3MonthTo6Months = r23_moreThan3MonthTo6Months;
		}

		public BigDecimal getR23_moreThan6MonthTo12Months() {
			return r23_moreThan6MonthTo12Months;
		}

		public void setR23_moreThan6MonthTo12Months(BigDecimal r23_moreThan6MonthTo12Months) {
			this.r23_moreThan6MonthTo12Months = r23_moreThan6MonthTo12Months;
		}

		public BigDecimal getR23_moreThan12MonthTo3Years() {
			return r23_moreThan12MonthTo3Years;
		}

		public void setR23_moreThan12MonthTo3Years(BigDecimal r23_moreThan12MonthTo3Years) {
			this.r23_moreThan12MonthTo3Years = r23_moreThan12MonthTo3Years;
		}

		public BigDecimal getR23_moreThan3YearsTo5Years() {
			return r23_moreThan3YearsTo5Years;
		}

		public void setR23_moreThan3YearsTo5Years(BigDecimal r23_moreThan3YearsTo5Years) {
			this.r23_moreThan3YearsTo5Years = r23_moreThan3YearsTo5Years;
		}

		public BigDecimal getR23_moreThan5YearsTo10Years() {
			return r23_moreThan5YearsTo10Years;
		}

		public void setR23_moreThan5YearsTo10Years(BigDecimal r23_moreThan5YearsTo10Years) {
			this.r23_moreThan5YearsTo10Years = r23_moreThan5YearsTo10Years;
		}

		public BigDecimal getR23_moreThan10Years() {
			return r23_moreThan10Years;
		}

		public void setR23_moreThan10Years(BigDecimal r23_moreThan10Years) {
			this.r23_moreThan10Years = r23_moreThan10Years;
		}

		public BigDecimal getR23_total() {
			return r23_total;
		}

		public void setR23_total(BigDecimal r23_total) {
			this.r23_total = r23_total;
		}

		public String getR24product() {
			return r24product;
		}

		public void setR24product(String r24product) {
			this.r24product = r24product;
		}

		public BigDecimal getR24_upTo1Month() {
			return r24_upTo1Month;
		}

		public void setR24_upTo1Month(BigDecimal r24_upTo1Month) {
			this.r24_upTo1Month = r24_upTo1Month;
		}

		public BigDecimal getR24_moreThan1MonthTo3Months() {
			return r24_moreThan1MonthTo3Months;
		}

		public void setR24_moreThan1MonthTo3Months(BigDecimal r24_moreThan1MonthTo3Months) {
			this.r24_moreThan1MonthTo3Months = r24_moreThan1MonthTo3Months;
		}

		public BigDecimal getR24_moreThan3MonthTo6Months() {
			return r24_moreThan3MonthTo6Months;
		}

		public void setR24_moreThan3MonthTo6Months(BigDecimal r24_moreThan3MonthTo6Months) {
			this.r24_moreThan3MonthTo6Months = r24_moreThan3MonthTo6Months;
		}

		public BigDecimal getR24_moreThan6MonthTo12Months() {
			return r24_moreThan6MonthTo12Months;
		}

		public void setR24_moreThan6MonthTo12Months(BigDecimal r24_moreThan6MonthTo12Months) {
			this.r24_moreThan6MonthTo12Months = r24_moreThan6MonthTo12Months;
		}

		public BigDecimal getR24_moreThan12MonthTo3Years() {
			return r24_moreThan12MonthTo3Years;
		}

		public void setR24_moreThan12MonthTo3Years(BigDecimal r24_moreThan12MonthTo3Years) {
			this.r24_moreThan12MonthTo3Years = r24_moreThan12MonthTo3Years;
		}

		public BigDecimal getR24_moreThan3YearsTo5Years() {
			return r24_moreThan3YearsTo5Years;
		}

		public void setR24_moreThan3YearsTo5Years(BigDecimal r24_moreThan3YearsTo5Years) {
			this.r24_moreThan3YearsTo5Years = r24_moreThan3YearsTo5Years;
		}

		public BigDecimal getR24_moreThan5YearsTo10Years() {
			return r24_moreThan5YearsTo10Years;
		}

		public void setR24_moreThan5YearsTo10Years(BigDecimal r24_moreThan5YearsTo10Years) {
			this.r24_moreThan5YearsTo10Years = r24_moreThan5YearsTo10Years;
		}

		public BigDecimal getR24_moreThan10Years() {
			return r24_moreThan10Years;
		}

		public void setR24_moreThan10Years(BigDecimal r24_moreThan10Years) {
			this.r24_moreThan10Years = r24_moreThan10Years;
		}

		public BigDecimal getR24_total() {
			return r24_total;
		}

		public void setR24_total(BigDecimal r24_total) {
			this.r24_total = r24_total;
		}

		public String getR25product() {
			return r25product;
		}

		public void setR25product(String r25product) {
			this.r25product = r25product;
		}

		public BigDecimal getR25_upTo1Month() {
			return r25_upTo1Month;
		}

		public void setR25_upTo1Month(BigDecimal r25_upTo1Month) {
			this.r25_upTo1Month = r25_upTo1Month;
		}

		public BigDecimal getR25_moreThan1MonthTo3Months() {
			return r25_moreThan1MonthTo3Months;
		}

		public void setR25_moreThan1MonthTo3Months(BigDecimal r25_moreThan1MonthTo3Months) {
			this.r25_moreThan1MonthTo3Months = r25_moreThan1MonthTo3Months;
		}

		public BigDecimal getR25_moreThan3MonthTo6Months() {
			return r25_moreThan3MonthTo6Months;
		}

		public void setR25_moreThan3MonthTo6Months(BigDecimal r25_moreThan3MonthTo6Months) {
			this.r25_moreThan3MonthTo6Months = r25_moreThan3MonthTo6Months;
		}

		public BigDecimal getR25_moreThan6MonthTo12Months() {
			return r25_moreThan6MonthTo12Months;
		}

		public void setR25_moreThan6MonthTo12Months(BigDecimal r25_moreThan6MonthTo12Months) {
			this.r25_moreThan6MonthTo12Months = r25_moreThan6MonthTo12Months;
		}

		public BigDecimal getR25_moreThan12MonthTo3Years() {
			return r25_moreThan12MonthTo3Years;
		}

		public void setR25_moreThan12MonthTo3Years(BigDecimal r25_moreThan12MonthTo3Years) {
			this.r25_moreThan12MonthTo3Years = r25_moreThan12MonthTo3Years;
		}

		public BigDecimal getR25_moreThan3YearsTo5Years() {
			return r25_moreThan3YearsTo5Years;
		}

		public void setR25_moreThan3YearsTo5Years(BigDecimal r25_moreThan3YearsTo5Years) {
			this.r25_moreThan3YearsTo5Years = r25_moreThan3YearsTo5Years;
		}

		public BigDecimal getR25_moreThan5YearsTo10Years() {
			return r25_moreThan5YearsTo10Years;
		}

		public void setR25_moreThan5YearsTo10Years(BigDecimal r25_moreThan5YearsTo10Years) {
			this.r25_moreThan5YearsTo10Years = r25_moreThan5YearsTo10Years;
		}

		public BigDecimal getR25_moreThan10Years() {
			return r25_moreThan10Years;
		}

		public void setR25_moreThan10Years(BigDecimal r25_moreThan10Years) {
			this.r25_moreThan10Years = r25_moreThan10Years;
		}

		public BigDecimal getR25_total() {
			return r25_total;
		}

		public void setR25_total(BigDecimal r25_total) {
			this.r25_total = r25_total;
		}

		public String getR26product() {
			return r26product;
		}

		public void setR26product(String r26product) {
			this.r26product = r26product;
		}

		public BigDecimal getR26_upTo1Month() {
			return r26_upTo1Month;
		}

		public void setR26_upTo1Month(BigDecimal r26_upTo1Month) {
			this.r26_upTo1Month = r26_upTo1Month;
		}

		public BigDecimal getR26_moreThan1MonthTo3Months() {
			return r26_moreThan1MonthTo3Months;
		}

		public void setR26_moreThan1MonthTo3Months(BigDecimal r26_moreThan1MonthTo3Months) {
			this.r26_moreThan1MonthTo3Months = r26_moreThan1MonthTo3Months;
		}

		public BigDecimal getR26_moreThan3MonthTo6Months() {
			return r26_moreThan3MonthTo6Months;
		}

		public void setR26_moreThan3MonthTo6Months(BigDecimal r26_moreThan3MonthTo6Months) {
			this.r26_moreThan3MonthTo6Months = r26_moreThan3MonthTo6Months;
		}

		public BigDecimal getR26_moreThan6MonthTo12Months() {
			return r26_moreThan6MonthTo12Months;
		}

		public void setR26_moreThan6MonthTo12Months(BigDecimal r26_moreThan6MonthTo12Months) {
			this.r26_moreThan6MonthTo12Months = r26_moreThan6MonthTo12Months;
		}

		public BigDecimal getR26_moreThan12MonthTo3Years() {
			return r26_moreThan12MonthTo3Years;
		}

		public void setR26_moreThan12MonthTo3Years(BigDecimal r26_moreThan12MonthTo3Years) {
			this.r26_moreThan12MonthTo3Years = r26_moreThan12MonthTo3Years;
		}

		public BigDecimal getR26_moreThan3YearsTo5Years() {
			return r26_moreThan3YearsTo5Years;
		}

		public void setR26_moreThan3YearsTo5Years(BigDecimal r26_moreThan3YearsTo5Years) {
			this.r26_moreThan3YearsTo5Years = r26_moreThan3YearsTo5Years;
		}

		public BigDecimal getR26_moreThan5YearsTo10Years() {
			return r26_moreThan5YearsTo10Years;
		}

		public void setR26_moreThan5YearsTo10Years(BigDecimal r26_moreThan5YearsTo10Years) {
			this.r26_moreThan5YearsTo10Years = r26_moreThan5YearsTo10Years;
		}

		public BigDecimal getR26_moreThan10Years() {
			return r26_moreThan10Years;
		}

		public void setR26_moreThan10Years(BigDecimal r26_moreThan10Years) {
			this.r26_moreThan10Years = r26_moreThan10Years;
		}

		public BigDecimal getR26_total() {
			return r26_total;
		}

		public void setR26_total(BigDecimal r26_total) {
			this.r26_total = r26_total;
		}

		public String getR27product() {
			return r27product;
		}

		public void setR27product(String r27product) {
			this.r27product = r27product;
		}

		public BigDecimal getR27_upTo1Month() {
			return r27_upTo1Month;
		}

		public void setR27_upTo1Month(BigDecimal r27_upTo1Month) {
			this.r27_upTo1Month = r27_upTo1Month;
		}

		public BigDecimal getR27_moreThan1MonthTo3Months() {
			return r27_moreThan1MonthTo3Months;
		}

		public void setR27_moreThan1MonthTo3Months(BigDecimal r27_moreThan1MonthTo3Months) {
			this.r27_moreThan1MonthTo3Months = r27_moreThan1MonthTo3Months;
		}

		public BigDecimal getR27_moreThan3MonthTo6Months() {
			return r27_moreThan3MonthTo6Months;
		}

		public void setR27_moreThan3MonthTo6Months(BigDecimal r27_moreThan3MonthTo6Months) {
			this.r27_moreThan3MonthTo6Months = r27_moreThan3MonthTo6Months;
		}

		public BigDecimal getR27_moreThan6MonthTo12Months() {
			return r27_moreThan6MonthTo12Months;
		}

		public void setR27_moreThan6MonthTo12Months(BigDecimal r27_moreThan6MonthTo12Months) {
			this.r27_moreThan6MonthTo12Months = r27_moreThan6MonthTo12Months;
		}

		public BigDecimal getR27_moreThan12MonthTo3Years() {
			return r27_moreThan12MonthTo3Years;
		}

		public void setR27_moreThan12MonthTo3Years(BigDecimal r27_moreThan12MonthTo3Years) {
			this.r27_moreThan12MonthTo3Years = r27_moreThan12MonthTo3Years;
		}

		public BigDecimal getR27_moreThan3YearsTo5Years() {
			return r27_moreThan3YearsTo5Years;
		}

		public void setR27_moreThan3YearsTo5Years(BigDecimal r27_moreThan3YearsTo5Years) {
			this.r27_moreThan3YearsTo5Years = r27_moreThan3YearsTo5Years;
		}

		public BigDecimal getR27_moreThan5YearsTo10Years() {
			return r27_moreThan5YearsTo10Years;
		}

		public void setR27_moreThan5YearsTo10Years(BigDecimal r27_moreThan5YearsTo10Years) {
			this.r27_moreThan5YearsTo10Years = r27_moreThan5YearsTo10Years;
		}

		public BigDecimal getR27_moreThan10Years() {
			return r27_moreThan10Years;
		}

		public void setR27_moreThan10Years(BigDecimal r27_moreThan10Years) {
			this.r27_moreThan10Years = r27_moreThan10Years;
		}

		public BigDecimal getR27_total() {
			return r27_total;
		}

		public void setR27_total(BigDecimal r27_total) {
			this.r27_total = r27_total;
		}

		public String getR28product() {
			return r28product;
		}

		public void setR28product(String r28product) {
			this.r28product = r28product;
		}

		public BigDecimal getR28_upTo1Month() {
			return r28_upTo1Month;
		}

		public void setR28_upTo1Month(BigDecimal r28_upTo1Month) {
			this.r28_upTo1Month = r28_upTo1Month;
		}

		public BigDecimal getR28_moreThan1MonthTo3Months() {
			return r28_moreThan1MonthTo3Months;
		}

		public void setR28_moreThan1MonthTo3Months(BigDecimal r28_moreThan1MonthTo3Months) {
			this.r28_moreThan1MonthTo3Months = r28_moreThan1MonthTo3Months;
		}

		public BigDecimal getR28_moreThan3MonthTo6Months() {
			return r28_moreThan3MonthTo6Months;
		}

		public void setR28_moreThan3MonthTo6Months(BigDecimal r28_moreThan3MonthTo6Months) {
			this.r28_moreThan3MonthTo6Months = r28_moreThan3MonthTo6Months;
		}

		public BigDecimal getR28_moreThan6MonthTo12Months() {
			return r28_moreThan6MonthTo12Months;
		}

		public void setR28_moreThan6MonthTo12Months(BigDecimal r28_moreThan6MonthTo12Months) {
			this.r28_moreThan6MonthTo12Months = r28_moreThan6MonthTo12Months;
		}

		public BigDecimal getR28_moreThan12MonthTo3Years() {
			return r28_moreThan12MonthTo3Years;
		}

		public void setR28_moreThan12MonthTo3Years(BigDecimal r28_moreThan12MonthTo3Years) {
			this.r28_moreThan12MonthTo3Years = r28_moreThan12MonthTo3Years;
		}

		public BigDecimal getR28_moreThan3YearsTo5Years() {
			return r28_moreThan3YearsTo5Years;
		}

		public void setR28_moreThan3YearsTo5Years(BigDecimal r28_moreThan3YearsTo5Years) {
			this.r28_moreThan3YearsTo5Years = r28_moreThan3YearsTo5Years;
		}

		public BigDecimal getR28_moreThan5YearsTo10Years() {
			return r28_moreThan5YearsTo10Years;
		}

		public void setR28_moreThan5YearsTo10Years(BigDecimal r28_moreThan5YearsTo10Years) {
			this.r28_moreThan5YearsTo10Years = r28_moreThan5YearsTo10Years;
		}

		public BigDecimal getR28_moreThan10Years() {
			return r28_moreThan10Years;
		}

		public void setR28_moreThan10Years(BigDecimal r28_moreThan10Years) {
			this.r28_moreThan10Years = r28_moreThan10Years;
		}

		public BigDecimal getR28_total() {
			return r28_total;
		}

		public void setR28_total(BigDecimal r28_total) {
			this.r28_total = r28_total;
		}

		public String getR29product() {
			return r29product;
		}

		public void setR29product(String r29product) {
			this.r29product = r29product;
		}

		public BigDecimal getR29_upTo1Month() {
			return r29_upTo1Month;
		}

		public void setR29_upTo1Month(BigDecimal r29_upTo1Month) {
			this.r29_upTo1Month = r29_upTo1Month;
		}

		public BigDecimal getR29_moreThan1MonthTo3Months() {
			return r29_moreThan1MonthTo3Months;
		}

		public void setR29_moreThan1MonthTo3Months(BigDecimal r29_moreThan1MonthTo3Months) {
			this.r29_moreThan1MonthTo3Months = r29_moreThan1MonthTo3Months;
		}

		public BigDecimal getR29_moreThan3MonthTo6Months() {
			return r29_moreThan3MonthTo6Months;
		}

		public void setR29_moreThan3MonthTo6Months(BigDecimal r29_moreThan3MonthTo6Months) {
			this.r29_moreThan3MonthTo6Months = r29_moreThan3MonthTo6Months;
		}

		public BigDecimal getR29_moreThan6MonthTo12Months() {
			return r29_moreThan6MonthTo12Months;
		}

		public void setR29_moreThan6MonthTo12Months(BigDecimal r29_moreThan6MonthTo12Months) {
			this.r29_moreThan6MonthTo12Months = r29_moreThan6MonthTo12Months;
		}

		public BigDecimal getR29_moreThan12MonthTo3Years() {
			return r29_moreThan12MonthTo3Years;
		}

		public void setR29_moreThan12MonthTo3Years(BigDecimal r29_moreThan12MonthTo3Years) {
			this.r29_moreThan12MonthTo3Years = r29_moreThan12MonthTo3Years;
		}

		public BigDecimal getR29_moreThan3YearsTo5Years() {
			return r29_moreThan3YearsTo5Years;
		}

		public void setR29_moreThan3YearsTo5Years(BigDecimal r29_moreThan3YearsTo5Years) {
			this.r29_moreThan3YearsTo5Years = r29_moreThan3YearsTo5Years;
		}

		public BigDecimal getR29_moreThan5YearsTo10Years() {
			return r29_moreThan5YearsTo10Years;
		}

		public void setR29_moreThan5YearsTo10Years(BigDecimal r29_moreThan5YearsTo10Years) {
			this.r29_moreThan5YearsTo10Years = r29_moreThan5YearsTo10Years;
		}

		public BigDecimal getR29_moreThan10Years() {
			return r29_moreThan10Years;
		}

		public void setR29_moreThan10Years(BigDecimal r29_moreThan10Years) {
			this.r29_moreThan10Years = r29_moreThan10Years;
		}

		public BigDecimal getR29_total() {
			return r29_total;
		}

		public void setR29_total(BigDecimal r29_total) {
			this.r29_total = r29_total;
		}

		public String getR30product() {
			return r30product;
		}

		public void setR30product(String r30product) {
			this.r30product = r30product;
		}

		public BigDecimal getR30_upTo1Month() {
			return r30_upTo1Month;
		}

		public void setR30_upTo1Month(BigDecimal r30_upTo1Month) {
			this.r30_upTo1Month = r30_upTo1Month;
		}

		public BigDecimal getR30_moreThan1MonthTo3Months() {
			return r30_moreThan1MonthTo3Months;
		}

		public void setR30_moreThan1MonthTo3Months(BigDecimal r30_moreThan1MonthTo3Months) {
			this.r30_moreThan1MonthTo3Months = r30_moreThan1MonthTo3Months;
		}

		public BigDecimal getR30_moreThan3MonthTo6Months() {
			return r30_moreThan3MonthTo6Months;
		}

		public void setR30_moreThan3MonthTo6Months(BigDecimal r30_moreThan3MonthTo6Months) {
			this.r30_moreThan3MonthTo6Months = r30_moreThan3MonthTo6Months;
		}

		public BigDecimal getR30_moreThan6MonthTo12Months() {
			return r30_moreThan6MonthTo12Months;
		}

		public void setR30_moreThan6MonthTo12Months(BigDecimal r30_moreThan6MonthTo12Months) {
			this.r30_moreThan6MonthTo12Months = r30_moreThan6MonthTo12Months;
		}

		public BigDecimal getR30_moreThan12MonthTo3Years() {
			return r30_moreThan12MonthTo3Years;
		}

		public void setR30_moreThan12MonthTo3Years(BigDecimal r30_moreThan12MonthTo3Years) {
			this.r30_moreThan12MonthTo3Years = r30_moreThan12MonthTo3Years;
		}

		public BigDecimal getR30_moreThan3YearsTo5Years() {
			return r30_moreThan3YearsTo5Years;
		}

		public void setR30_moreThan3YearsTo5Years(BigDecimal r30_moreThan3YearsTo5Years) {
			this.r30_moreThan3YearsTo5Years = r30_moreThan3YearsTo5Years;
		}

		public BigDecimal getR30_moreThan5YearsTo10Years() {
			return r30_moreThan5YearsTo10Years;
		}

		public void setR30_moreThan5YearsTo10Years(BigDecimal r30_moreThan5YearsTo10Years) {
			this.r30_moreThan5YearsTo10Years = r30_moreThan5YearsTo10Years;
		}

		public BigDecimal getR30_moreThan10Years() {
			return r30_moreThan10Years;
		}

		public void setR30_moreThan10Years(BigDecimal r30_moreThan10Years) {
			this.r30_moreThan10Years = r30_moreThan10Years;
		}

		public BigDecimal getR30_total() {
			return r30_total;
		}

		public void setR30_total(BigDecimal r30_total) {
			this.r30_total = r30_total;
		}

		public String getR31product() {
			return r31product;
		}

		public void setR31product(String r31product) {
			this.r31product = r31product;
		}

		public BigDecimal getR31_upTo1Month() {
			return r31_upTo1Month;
		}

		public void setR31_upTo1Month(BigDecimal r31_upTo1Month) {
			this.r31_upTo1Month = r31_upTo1Month;
		}

		public BigDecimal getR31_moreThan1MonthTo3Months() {
			return r31_moreThan1MonthTo3Months;
		}

		public void setR31_moreThan1MonthTo3Months(BigDecimal r31_moreThan1MonthTo3Months) {
			this.r31_moreThan1MonthTo3Months = r31_moreThan1MonthTo3Months;
		}

		public BigDecimal getR31_moreThan3MonthTo6Months() {
			return r31_moreThan3MonthTo6Months;
		}

		public void setR31_moreThan3MonthTo6Months(BigDecimal r31_moreThan3MonthTo6Months) {
			this.r31_moreThan3MonthTo6Months = r31_moreThan3MonthTo6Months;
		}

		public BigDecimal getR31_moreThan6MonthTo12Months() {
			return r31_moreThan6MonthTo12Months;
		}

		public void setR31_moreThan6MonthTo12Months(BigDecimal r31_moreThan6MonthTo12Months) {
			this.r31_moreThan6MonthTo12Months = r31_moreThan6MonthTo12Months;
		}

		public BigDecimal getR31_moreThan12MonthTo3Years() {
			return r31_moreThan12MonthTo3Years;
		}

		public void setR31_moreThan12MonthTo3Years(BigDecimal r31_moreThan12MonthTo3Years) {
			this.r31_moreThan12MonthTo3Years = r31_moreThan12MonthTo3Years;
		}

		public BigDecimal getR31_moreThan3YearsTo5Years() {
			return r31_moreThan3YearsTo5Years;
		}

		public void setR31_moreThan3YearsTo5Years(BigDecimal r31_moreThan3YearsTo5Years) {
			this.r31_moreThan3YearsTo5Years = r31_moreThan3YearsTo5Years;
		}

		public BigDecimal getR31_moreThan5YearsTo10Years() {
			return r31_moreThan5YearsTo10Years;
		}

		public void setR31_moreThan5YearsTo10Years(BigDecimal r31_moreThan5YearsTo10Years) {
			this.r31_moreThan5YearsTo10Years = r31_moreThan5YearsTo10Years;
		}

		public BigDecimal getR31_moreThan10Years() {
			return r31_moreThan10Years;
		}

		public void setR31_moreThan10Years(BigDecimal r31_moreThan10Years) {
			this.r31_moreThan10Years = r31_moreThan10Years;
		}

		public BigDecimal getR31_total() {
			return r31_total;
		}

		public void setR31_total(BigDecimal r31_total) {
			this.r31_total = r31_total;
		}

		public String getR32product() {
			return r32product;
		}

		public void setR32product(String r32product) {
			this.r32product = r32product;
		}

		public BigDecimal getR32_upTo1Month() {
			return r32_upTo1Month;
		}

		public void setR32_upTo1Month(BigDecimal r32_upTo1Month) {
			this.r32_upTo1Month = r32_upTo1Month;
		}

		public BigDecimal getR32_moreThan1MonthTo3Months() {
			return r32_moreThan1MonthTo3Months;
		}

		public void setR32_moreThan1MonthTo3Months(BigDecimal r32_moreThan1MonthTo3Months) {
			this.r32_moreThan1MonthTo3Months = r32_moreThan1MonthTo3Months;
		}

		public BigDecimal getR32_moreThan3MonthTo6Months() {
			return r32_moreThan3MonthTo6Months;
		}

		public void setR32_moreThan3MonthTo6Months(BigDecimal r32_moreThan3MonthTo6Months) {
			this.r32_moreThan3MonthTo6Months = r32_moreThan3MonthTo6Months;
		}

		public BigDecimal getR32_moreThan6MonthTo12Months() {
			return r32_moreThan6MonthTo12Months;
		}

		public void setR32_moreThan6MonthTo12Months(BigDecimal r32_moreThan6MonthTo12Months) {
			this.r32_moreThan6MonthTo12Months = r32_moreThan6MonthTo12Months;
		}

		public BigDecimal getR32_moreThan12MonthTo3Years() {
			return r32_moreThan12MonthTo3Years;
		}

		public void setR32_moreThan12MonthTo3Years(BigDecimal r32_moreThan12MonthTo3Years) {
			this.r32_moreThan12MonthTo3Years = r32_moreThan12MonthTo3Years;
		}

		public BigDecimal getR32_moreThan3YearsTo5Years() {
			return r32_moreThan3YearsTo5Years;
		}

		public void setR32_moreThan3YearsTo5Years(BigDecimal r32_moreThan3YearsTo5Years) {
			this.r32_moreThan3YearsTo5Years = r32_moreThan3YearsTo5Years;
		}

		public BigDecimal getR32_moreThan5YearsTo10Years() {
			return r32_moreThan5YearsTo10Years;
		}

		public void setR32_moreThan5YearsTo10Years(BigDecimal r32_moreThan5YearsTo10Years) {
			this.r32_moreThan5YearsTo10Years = r32_moreThan5YearsTo10Years;
		}

		public BigDecimal getR32_moreThan10Years() {
			return r32_moreThan10Years;
		}

		public void setR32_moreThan10Years(BigDecimal r32_moreThan10Years) {
			this.r32_moreThan10Years = r32_moreThan10Years;
		}

		public BigDecimal getR32_total() {
			return r32_total;
		}

		public void setR32_total(BigDecimal r32_total) {
			this.r32_total = r32_total;
		}

		public String getR33product() {
			return r33product;
		}

		public void setR33product(String r33product) {
			this.r33product = r33product;
		}

		public BigDecimal getR33_upTo1Month() {
			return r33_upTo1Month;
		}

		public void setR33_upTo1Month(BigDecimal r33_upTo1Month) {
			this.r33_upTo1Month = r33_upTo1Month;
		}

		public BigDecimal getR33_moreThan1MonthTo3Months() {
			return r33_moreThan1MonthTo3Months;
		}

		public void setR33_moreThan1MonthTo3Months(BigDecimal r33_moreThan1MonthTo3Months) {
			this.r33_moreThan1MonthTo3Months = r33_moreThan1MonthTo3Months;
		}

		public BigDecimal getR33_moreThan3MonthTo6Months() {
			return r33_moreThan3MonthTo6Months;
		}

		public void setR33_moreThan3MonthTo6Months(BigDecimal r33_moreThan3MonthTo6Months) {
			this.r33_moreThan3MonthTo6Months = r33_moreThan3MonthTo6Months;
		}

		public BigDecimal getR33_moreThan6MonthTo12Months() {
			return r33_moreThan6MonthTo12Months;
		}

		public void setR33_moreThan6MonthTo12Months(BigDecimal r33_moreThan6MonthTo12Months) {
			this.r33_moreThan6MonthTo12Months = r33_moreThan6MonthTo12Months;
		}

		public BigDecimal getR33_moreThan12MonthTo3Years() {
			return r33_moreThan12MonthTo3Years;
		}

		public void setR33_moreThan12MonthTo3Years(BigDecimal r33_moreThan12MonthTo3Years) {
			this.r33_moreThan12MonthTo3Years = r33_moreThan12MonthTo3Years;
		}

		public BigDecimal getR33_moreThan3YearsTo5Years() {
			return r33_moreThan3YearsTo5Years;
		}

		public void setR33_moreThan3YearsTo5Years(BigDecimal r33_moreThan3YearsTo5Years) {
			this.r33_moreThan3YearsTo5Years = r33_moreThan3YearsTo5Years;
		}

		public BigDecimal getR33_moreThan5YearsTo10Years() {
			return r33_moreThan5YearsTo10Years;
		}

		public void setR33_moreThan5YearsTo10Years(BigDecimal r33_moreThan5YearsTo10Years) {
			this.r33_moreThan5YearsTo10Years = r33_moreThan5YearsTo10Years;
		}

		public BigDecimal getR33_moreThan10Years() {
			return r33_moreThan10Years;
		}

		public void setR33_moreThan10Years(BigDecimal r33_moreThan10Years) {
			this.r33_moreThan10Years = r33_moreThan10Years;
		}

		public BigDecimal getR33_total() {
			return r33_total;
		}

		public void setR33_total(BigDecimal r33_total) {
			this.r33_total = r33_total;
		}

		public String getR34product() {
			return r34product;
		}

		public void setR34product(String r34product) {
			this.r34product = r34product;
		}

		public BigDecimal getR34_upTo1Month() {
			return r34_upTo1Month;
		}

		public void setR34_upTo1Month(BigDecimal r34_upTo1Month) {
			this.r34_upTo1Month = r34_upTo1Month;
		}

		public BigDecimal getR34_moreThan1MonthTo3Months() {
			return r34_moreThan1MonthTo3Months;
		}

		public void setR34_moreThan1MonthTo3Months(BigDecimal r34_moreThan1MonthTo3Months) {
			this.r34_moreThan1MonthTo3Months = r34_moreThan1MonthTo3Months;
		}

		public BigDecimal getR34_moreThan3MonthTo6Months() {
			return r34_moreThan3MonthTo6Months;
		}

		public void setR34_moreThan3MonthTo6Months(BigDecimal r34_moreThan3MonthTo6Months) {
			this.r34_moreThan3MonthTo6Months = r34_moreThan3MonthTo6Months;
		}

		public BigDecimal getR34_moreThan6MonthTo12Months() {
			return r34_moreThan6MonthTo12Months;
		}

		public void setR34_moreThan6MonthTo12Months(BigDecimal r34_moreThan6MonthTo12Months) {
			this.r34_moreThan6MonthTo12Months = r34_moreThan6MonthTo12Months;
		}

		public BigDecimal getR34_moreThan12MonthTo3Years() {
			return r34_moreThan12MonthTo3Years;
		}

		public void setR34_moreThan12MonthTo3Years(BigDecimal r34_moreThan12MonthTo3Years) {
			this.r34_moreThan12MonthTo3Years = r34_moreThan12MonthTo3Years;
		}

		public BigDecimal getR34_moreThan3YearsTo5Years() {
			return r34_moreThan3YearsTo5Years;
		}

		public void setR34_moreThan3YearsTo5Years(BigDecimal r34_moreThan3YearsTo5Years) {
			this.r34_moreThan3YearsTo5Years = r34_moreThan3YearsTo5Years;
		}

		public BigDecimal getR34_moreThan5YearsTo10Years() {
			return r34_moreThan5YearsTo10Years;
		}

		public void setR34_moreThan5YearsTo10Years(BigDecimal r34_moreThan5YearsTo10Years) {
			this.r34_moreThan5YearsTo10Years = r34_moreThan5YearsTo10Years;
		}

		public BigDecimal getR34_moreThan10Years() {
			return r34_moreThan10Years;
		}

		public void setR34_moreThan10Years(BigDecimal r34_moreThan10Years) {
			this.r34_moreThan10Years = r34_moreThan10Years;
		}

		public BigDecimal getR34_total() {
			return r34_total;
		}

		public void setR34_total(BigDecimal r34_total) {
			this.r34_total = r34_total;
		}

		public String getR35product() {
			return r35product;
		}

		public void setR35product(String r35product) {
			this.r35product = r35product;
		}

		public BigDecimal getR35_upTo1Month() {
			return r35_upTo1Month;
		}

		public void setR35_upTo1Month(BigDecimal r35_upTo1Month) {
			this.r35_upTo1Month = r35_upTo1Month;
		}

		public BigDecimal getR35_moreThan1MonthTo3Months() {
			return r35_moreThan1MonthTo3Months;
		}

		public void setR35_moreThan1MonthTo3Months(BigDecimal r35_moreThan1MonthTo3Months) {
			this.r35_moreThan1MonthTo3Months = r35_moreThan1MonthTo3Months;
		}

		public BigDecimal getR35_moreThan3MonthTo6Months() {
			return r35_moreThan3MonthTo6Months;
		}

		public void setR35_moreThan3MonthTo6Months(BigDecimal r35_moreThan3MonthTo6Months) {
			this.r35_moreThan3MonthTo6Months = r35_moreThan3MonthTo6Months;
		}

		public BigDecimal getR35_moreThan6MonthTo12Months() {
			return r35_moreThan6MonthTo12Months;
		}

		public void setR35_moreThan6MonthTo12Months(BigDecimal r35_moreThan6MonthTo12Months) {
			this.r35_moreThan6MonthTo12Months = r35_moreThan6MonthTo12Months;
		}

		public BigDecimal getR35_moreThan12MonthTo3Years() {
			return r35_moreThan12MonthTo3Years;
		}

		public void setR35_moreThan12MonthTo3Years(BigDecimal r35_moreThan12MonthTo3Years) {
			this.r35_moreThan12MonthTo3Years = r35_moreThan12MonthTo3Years;
		}

		public BigDecimal getR35_moreThan3YearsTo5Years() {
			return r35_moreThan3YearsTo5Years;
		}

		public void setR35_moreThan3YearsTo5Years(BigDecimal r35_moreThan3YearsTo5Years) {
			this.r35_moreThan3YearsTo5Years = r35_moreThan3YearsTo5Years;
		}

		public BigDecimal getR35_moreThan5YearsTo10Years() {
			return r35_moreThan5YearsTo10Years;
		}

		public void setR35_moreThan5YearsTo10Years(BigDecimal r35_moreThan5YearsTo10Years) {
			this.r35_moreThan5YearsTo10Years = r35_moreThan5YearsTo10Years;
		}

		public BigDecimal getR35_moreThan10Years() {
			return r35_moreThan10Years;
		}

		public void setR35_moreThan10Years(BigDecimal r35_moreThan10Years) {
			this.r35_moreThan10Years = r35_moreThan10Years;
		}

		public BigDecimal getR35_total() {
			return r35_total;
		}

		public void setR35_total(BigDecimal r35_total) {
			this.r35_total = r35_total;
		}

		public String getR36product() {
			return r36product;
		}

		public void setR36product(String r36product) {
			this.r36product = r36product;
		}

		public BigDecimal getR36_upTo1Month() {
			return r36_upTo1Month;
		}

		public void setR36_upTo1Month(BigDecimal r36_upTo1Month) {
			this.r36_upTo1Month = r36_upTo1Month;
		}

		public BigDecimal getR36_moreThan1MonthTo3Months() {
			return r36_moreThan1MonthTo3Months;
		}

		public void setR36_moreThan1MonthTo3Months(BigDecimal r36_moreThan1MonthTo3Months) {
			this.r36_moreThan1MonthTo3Months = r36_moreThan1MonthTo3Months;
		}

		public BigDecimal getR36_moreThan3MonthTo6Months() {
			return r36_moreThan3MonthTo6Months;
		}

		public void setR36_moreThan3MonthTo6Months(BigDecimal r36_moreThan3MonthTo6Months) {
			this.r36_moreThan3MonthTo6Months = r36_moreThan3MonthTo6Months;
		}

		public BigDecimal getR36_moreThan6MonthTo12Months() {
			return r36_moreThan6MonthTo12Months;
		}

		public void setR36_moreThan6MonthTo12Months(BigDecimal r36_moreThan6MonthTo12Months) {
			this.r36_moreThan6MonthTo12Months = r36_moreThan6MonthTo12Months;
		}

		public BigDecimal getR36_moreThan12MonthTo3Years() {
			return r36_moreThan12MonthTo3Years;
		}

		public void setR36_moreThan12MonthTo3Years(BigDecimal r36_moreThan12MonthTo3Years) {
			this.r36_moreThan12MonthTo3Years = r36_moreThan12MonthTo3Years;
		}

		public BigDecimal getR36_moreThan3YearsTo5Years() {
			return r36_moreThan3YearsTo5Years;
		}

		public void setR36_moreThan3YearsTo5Years(BigDecimal r36_moreThan3YearsTo5Years) {
			this.r36_moreThan3YearsTo5Years = r36_moreThan3YearsTo5Years;
		}

		public BigDecimal getR36_moreThan5YearsTo10Years() {
			return r36_moreThan5YearsTo10Years;
		}

		public void setR36_moreThan5YearsTo10Years(BigDecimal r36_moreThan5YearsTo10Years) {
			this.r36_moreThan5YearsTo10Years = r36_moreThan5YearsTo10Years;
		}

		public BigDecimal getR36_moreThan10Years() {
			return r36_moreThan10Years;
		}

		public void setR36_moreThan10Years(BigDecimal r36_moreThan10Years) {
			this.r36_moreThan10Years = r36_moreThan10Years;
		}

		public BigDecimal getR36_total() {
			return r36_total;
		}

		public void setR36_total(BigDecimal r36_total) {
			this.r36_total = r36_total;
		}

		public String getR37product() {
			return r37product;
		}

		public void setR37product(String r37product) {
			this.r37product = r37product;
		}

		public BigDecimal getR37_upTo1Month() {
			return r37_upTo1Month;
		}

		public void setR37_upTo1Month(BigDecimal r37_upTo1Month) {
			this.r37_upTo1Month = r37_upTo1Month;
		}

		public BigDecimal getR37_moreThan1MonthTo3Months() {
			return r37_moreThan1MonthTo3Months;
		}

		public void setR37_moreThan1MonthTo3Months(BigDecimal r37_moreThan1MonthTo3Months) {
			this.r37_moreThan1MonthTo3Months = r37_moreThan1MonthTo3Months;
		}

		public BigDecimal getR37_moreThan3MonthTo6Months() {
			return r37_moreThan3MonthTo6Months;
		}

		public void setR37_moreThan3MonthTo6Months(BigDecimal r37_moreThan3MonthTo6Months) {
			this.r37_moreThan3MonthTo6Months = r37_moreThan3MonthTo6Months;
		}

		public BigDecimal getR37_moreThan6MonthTo12Months() {
			return r37_moreThan6MonthTo12Months;
		}

		public void setR37_moreThan6MonthTo12Months(BigDecimal r37_moreThan6MonthTo12Months) {
			this.r37_moreThan6MonthTo12Months = r37_moreThan6MonthTo12Months;
		}

		public BigDecimal getR37_moreThan12MonthTo3Years() {
			return r37_moreThan12MonthTo3Years;
		}

		public void setR37_moreThan12MonthTo3Years(BigDecimal r37_moreThan12MonthTo3Years) {
			this.r37_moreThan12MonthTo3Years = r37_moreThan12MonthTo3Years;
		}

		public BigDecimal getR37_moreThan3YearsTo5Years() {
			return r37_moreThan3YearsTo5Years;
		}

		public void setR37_moreThan3YearsTo5Years(BigDecimal r37_moreThan3YearsTo5Years) {
			this.r37_moreThan3YearsTo5Years = r37_moreThan3YearsTo5Years;
		}

		public BigDecimal getR37_moreThan5YearsTo10Years() {
			return r37_moreThan5YearsTo10Years;
		}

		public void setR37_moreThan5YearsTo10Years(BigDecimal r37_moreThan5YearsTo10Years) {
			this.r37_moreThan5YearsTo10Years = r37_moreThan5YearsTo10Years;
		}

		public BigDecimal getR37_moreThan10Years() {
			return r37_moreThan10Years;
		}

		public void setR37_moreThan10Years(BigDecimal r37_moreThan10Years) {
			this.r37_moreThan10Years = r37_moreThan10Years;
		}

		public BigDecimal getR37_total() {
			return r37_total;
		}

		public void setR37_total(BigDecimal r37_total) {
			this.r37_total = r37_total;
		}

		public String getR38product() {
			return r38product;
		}

		public void setR38product(String r38product) {
			this.r38product = r38product;
		}

		public BigDecimal getR38_upTo1Month() {
			return r38_upTo1Month;
		}

		public void setR38_upTo1Month(BigDecimal r38_upTo1Month) {
			this.r38_upTo1Month = r38_upTo1Month;
		}

		public BigDecimal getR38_moreThan1MonthTo3Months() {
			return r38_moreThan1MonthTo3Months;
		}

		public void setR38_moreThan1MonthTo3Months(BigDecimal r38_moreThan1MonthTo3Months) {
			this.r38_moreThan1MonthTo3Months = r38_moreThan1MonthTo3Months;
		}

		public BigDecimal getR38_moreThan3MonthTo6Months() {
			return r38_moreThan3MonthTo6Months;
		}

		public void setR38_moreThan3MonthTo6Months(BigDecimal r38_moreThan3MonthTo6Months) {
			this.r38_moreThan3MonthTo6Months = r38_moreThan3MonthTo6Months;
		}

		public BigDecimal getR38_moreThan6MonthTo12Months() {
			return r38_moreThan6MonthTo12Months;
		}

		public void setR38_moreThan6MonthTo12Months(BigDecimal r38_moreThan6MonthTo12Months) {
			this.r38_moreThan6MonthTo12Months = r38_moreThan6MonthTo12Months;
		}

		public BigDecimal getR38_moreThan12MonthTo3Years() {
			return r38_moreThan12MonthTo3Years;
		}

		public void setR38_moreThan12MonthTo3Years(BigDecimal r38_moreThan12MonthTo3Years) {
			this.r38_moreThan12MonthTo3Years = r38_moreThan12MonthTo3Years;
		}

		public BigDecimal getR38_moreThan3YearsTo5Years() {
			return r38_moreThan3YearsTo5Years;
		}

		public void setR38_moreThan3YearsTo5Years(BigDecimal r38_moreThan3YearsTo5Years) {
			this.r38_moreThan3YearsTo5Years = r38_moreThan3YearsTo5Years;
		}

		public BigDecimal getR38_moreThan5YearsTo10Years() {
			return r38_moreThan5YearsTo10Years;
		}

		public void setR38_moreThan5YearsTo10Years(BigDecimal r38_moreThan5YearsTo10Years) {
			this.r38_moreThan5YearsTo10Years = r38_moreThan5YearsTo10Years;
		}

		public BigDecimal getR38_moreThan10Years() {
			return r38_moreThan10Years;
		}

		public void setR38_moreThan10Years(BigDecimal r38_moreThan10Years) {
			this.r38_moreThan10Years = r38_moreThan10Years;
		}

		public BigDecimal getR38_total() {
			return r38_total;
		}

		public void setR38_total(BigDecimal r38_total) {
			this.r38_total = r38_total;
		}

		public String getR39product() {
			return r39product;
		}

		public void setR39product(String r39product) {
			this.r39product = r39product;
		}

		public BigDecimal getR39_upTo1Month() {
			return r39_upTo1Month;
		}

		public void setR39_upTo1Month(BigDecimal r39_upTo1Month) {
			this.r39_upTo1Month = r39_upTo1Month;
		}

		public BigDecimal getR39_moreThan1MonthTo3Months() {
			return r39_moreThan1MonthTo3Months;
		}

		public void setR39_moreThan1MonthTo3Months(BigDecimal r39_moreThan1MonthTo3Months) {
			this.r39_moreThan1MonthTo3Months = r39_moreThan1MonthTo3Months;
		}

		public BigDecimal getR39_moreThan3MonthTo6Months() {
			return r39_moreThan3MonthTo6Months;
		}

		public void setR39_moreThan3MonthTo6Months(BigDecimal r39_moreThan3MonthTo6Months) {
			this.r39_moreThan3MonthTo6Months = r39_moreThan3MonthTo6Months;
		}

		public BigDecimal getR39_moreThan6MonthTo12Months() {
			return r39_moreThan6MonthTo12Months;
		}

		public void setR39_moreThan6MonthTo12Months(BigDecimal r39_moreThan6MonthTo12Months) {
			this.r39_moreThan6MonthTo12Months = r39_moreThan6MonthTo12Months;
		}

		public BigDecimal getR39_moreThan12MonthTo3Years() {
			return r39_moreThan12MonthTo3Years;
		}

		public void setR39_moreThan12MonthTo3Years(BigDecimal r39_moreThan12MonthTo3Years) {
			this.r39_moreThan12MonthTo3Years = r39_moreThan12MonthTo3Years;
		}

		public BigDecimal getR39_moreThan3YearsTo5Years() {
			return r39_moreThan3YearsTo5Years;
		}

		public void setR39_moreThan3YearsTo5Years(BigDecimal r39_moreThan3YearsTo5Years) {
			this.r39_moreThan3YearsTo5Years = r39_moreThan3YearsTo5Years;
		}

		public BigDecimal getR39_moreThan5YearsTo10Years() {
			return r39_moreThan5YearsTo10Years;
		}

		public void setR39_moreThan5YearsTo10Years(BigDecimal r39_moreThan5YearsTo10Years) {
			this.r39_moreThan5YearsTo10Years = r39_moreThan5YearsTo10Years;
		}

		public BigDecimal getR39_moreThan10Years() {
			return r39_moreThan10Years;
		}

		public void setR39_moreThan10Years(BigDecimal r39_moreThan10Years) {
			this.r39_moreThan10Years = r39_moreThan10Years;
		}

		public BigDecimal getR39_total() {
			return r39_total;
		}

		public void setR39_total(BigDecimal r39_total) {
			this.r39_total = r39_total;
		}

		public String getR40product() {
			return r40product;
		}

		public void setR40product(String r40product) {
			this.r40product = r40product;
		}

		public BigDecimal getR40_upTo1Month() {
			return r40_upTo1Month;
		}

		public void setR40_upTo1Month(BigDecimal r40_upTo1Month) {
			this.r40_upTo1Month = r40_upTo1Month;
		}

		public BigDecimal getR40_moreThan1MonthTo3Months() {
			return r40_moreThan1MonthTo3Months;
		}

		public void setR40_moreThan1MonthTo3Months(BigDecimal r40_moreThan1MonthTo3Months) {
			this.r40_moreThan1MonthTo3Months = r40_moreThan1MonthTo3Months;
		}

		public BigDecimal getR40_moreThan3MonthTo6Months() {
			return r40_moreThan3MonthTo6Months;
		}

		public void setR40_moreThan3MonthTo6Months(BigDecimal r40_moreThan3MonthTo6Months) {
			this.r40_moreThan3MonthTo6Months = r40_moreThan3MonthTo6Months;
		}

		public BigDecimal getR40_moreThan6MonthTo12Months() {
			return r40_moreThan6MonthTo12Months;
		}

		public void setR40_moreThan6MonthTo12Months(BigDecimal r40_moreThan6MonthTo12Months) {
			this.r40_moreThan6MonthTo12Months = r40_moreThan6MonthTo12Months;
		}

		public BigDecimal getR40_moreThan12MonthTo3Years() {
			return r40_moreThan12MonthTo3Years;
		}

		public void setR40_moreThan12MonthTo3Years(BigDecimal r40_moreThan12MonthTo3Years) {
			this.r40_moreThan12MonthTo3Years = r40_moreThan12MonthTo3Years;
		}

		public BigDecimal getR40_moreThan3YearsTo5Years() {
			return r40_moreThan3YearsTo5Years;
		}

		public void setR40_moreThan3YearsTo5Years(BigDecimal r40_moreThan3YearsTo5Years) {
			this.r40_moreThan3YearsTo5Years = r40_moreThan3YearsTo5Years;
		}

		public BigDecimal getR40_moreThan5YearsTo10Years() {
			return r40_moreThan5YearsTo10Years;
		}

		public void setR40_moreThan5YearsTo10Years(BigDecimal r40_moreThan5YearsTo10Years) {
			this.r40_moreThan5YearsTo10Years = r40_moreThan5YearsTo10Years;
		}

		public BigDecimal getR40_moreThan10Years() {
			return r40_moreThan10Years;
		}

		public void setR40_moreThan10Years(BigDecimal r40_moreThan10Years) {
			this.r40_moreThan10Years = r40_moreThan10Years;
		}

		public BigDecimal getR40_total() {
			return r40_total;
		}

		public void setR40_total(BigDecimal r40_total) {
			this.r40_total = r40_total;
		}

		public String getR41product() {
			return r41product;
		}

		public void setR41product(String r41product) {
			this.r41product = r41product;
		}

		public BigDecimal getR41_upTo1Month() {
			return r41_upTo1Month;
		}

		public void setR41_upTo1Month(BigDecimal r41_upTo1Month) {
			this.r41_upTo1Month = r41_upTo1Month;
		}

		public BigDecimal getR41_moreThan1MonthTo3Months() {
			return r41_moreThan1MonthTo3Months;
		}

		public void setR41_moreThan1MonthTo3Months(BigDecimal r41_moreThan1MonthTo3Months) {
			this.r41_moreThan1MonthTo3Months = r41_moreThan1MonthTo3Months;
		}

		public BigDecimal getR41_moreThan3MonthTo6Months() {
			return r41_moreThan3MonthTo6Months;
		}

		public void setR41_moreThan3MonthTo6Months(BigDecimal r41_moreThan3MonthTo6Months) {
			this.r41_moreThan3MonthTo6Months = r41_moreThan3MonthTo6Months;
		}

		public BigDecimal getR41_moreThan6MonthTo12Months() {
			return r41_moreThan6MonthTo12Months;
		}

		public void setR41_moreThan6MonthTo12Months(BigDecimal r41_moreThan6MonthTo12Months) {
			this.r41_moreThan6MonthTo12Months = r41_moreThan6MonthTo12Months;
		}

		public BigDecimal getR41_moreThan12MonthTo3Years() {
			return r41_moreThan12MonthTo3Years;
		}

		public void setR41_moreThan12MonthTo3Years(BigDecimal r41_moreThan12MonthTo3Years) {
			this.r41_moreThan12MonthTo3Years = r41_moreThan12MonthTo3Years;
		}

		public BigDecimal getR41_moreThan3YearsTo5Years() {
			return r41_moreThan3YearsTo5Years;
		}

		public void setR41_moreThan3YearsTo5Years(BigDecimal r41_moreThan3YearsTo5Years) {
			this.r41_moreThan3YearsTo5Years = r41_moreThan3YearsTo5Years;
		}

		public BigDecimal getR41_moreThan5YearsTo10Years() {
			return r41_moreThan5YearsTo10Years;
		}

		public void setR41_moreThan5YearsTo10Years(BigDecimal r41_moreThan5YearsTo10Years) {
			this.r41_moreThan5YearsTo10Years = r41_moreThan5YearsTo10Years;
		}

		public BigDecimal getR41_moreThan10Years() {
			return r41_moreThan10Years;
		}

		public void setR41_moreThan10Years(BigDecimal r41_moreThan10Years) {
			this.r41_moreThan10Years = r41_moreThan10Years;
		}

		public BigDecimal getR41_total() {
			return r41_total;
		}

		public void setR41_total(BigDecimal r41_total) {
			this.r41_total = r41_total;
		}

		public String getR42product() {
			return r42product;
		}

		public void setR42product(String r42product) {
			this.r42product = r42product;
		}

		public BigDecimal getR42_upTo1Month() {
			return r42_upTo1Month;
		}

		public void setR42_upTo1Month(BigDecimal r42_upTo1Month) {
			this.r42_upTo1Month = r42_upTo1Month;
		}

		public BigDecimal getR42_moreThan1MonthTo3Months() {
			return r42_moreThan1MonthTo3Months;
		}

		public void setR42_moreThan1MonthTo3Months(BigDecimal r42_moreThan1MonthTo3Months) {
			this.r42_moreThan1MonthTo3Months = r42_moreThan1MonthTo3Months;
		}

		public BigDecimal getR42_moreThan3MonthTo6Months() {
			return r42_moreThan3MonthTo6Months;
		}

		public void setR42_moreThan3MonthTo6Months(BigDecimal r42_moreThan3MonthTo6Months) {
			this.r42_moreThan3MonthTo6Months = r42_moreThan3MonthTo6Months;
		}

		public BigDecimal getR42_moreThan6MonthTo12Months() {
			return r42_moreThan6MonthTo12Months;
		}

		public void setR42_moreThan6MonthTo12Months(BigDecimal r42_moreThan6MonthTo12Months) {
			this.r42_moreThan6MonthTo12Months = r42_moreThan6MonthTo12Months;
		}

		public BigDecimal getR42_moreThan12MonthTo3Years() {
			return r42_moreThan12MonthTo3Years;
		}

		public void setR42_moreThan12MonthTo3Years(BigDecimal r42_moreThan12MonthTo3Years) {
			this.r42_moreThan12MonthTo3Years = r42_moreThan12MonthTo3Years;
		}

		public BigDecimal getR42_moreThan3YearsTo5Years() {
			return r42_moreThan3YearsTo5Years;
		}

		public void setR42_moreThan3YearsTo5Years(BigDecimal r42_moreThan3YearsTo5Years) {
			this.r42_moreThan3YearsTo5Years = r42_moreThan3YearsTo5Years;
		}

		public BigDecimal getR42_moreThan5YearsTo10Years() {
			return r42_moreThan5YearsTo10Years;
		}

		public void setR42_moreThan5YearsTo10Years(BigDecimal r42_moreThan5YearsTo10Years) {
			this.r42_moreThan5YearsTo10Years = r42_moreThan5YearsTo10Years;
		}

		public BigDecimal getR42_moreThan10Years() {
			return r42_moreThan10Years;
		}

		public void setR42_moreThan10Years(BigDecimal r42_moreThan10Years) {
			this.r42_moreThan10Years = r42_moreThan10Years;
		}

		public BigDecimal getR42_total() {
			return r42_total;
		}

		public void setR42_total(BigDecimal r42_total) {
			this.r42_total = r42_total;
		}

		public String getR43product() {
			return r43product;
		}

		public void setR43product(String r43product) {
			this.r43product = r43product;
		}

		public BigDecimal getR43_upTo1Month() {
			return r43_upTo1Month;
		}

		public void setR43_upTo1Month(BigDecimal r43_upTo1Month) {
			this.r43_upTo1Month = r43_upTo1Month;
		}

		public BigDecimal getR43_moreThan1MonthTo3Months() {
			return r43_moreThan1MonthTo3Months;
		}

		public void setR43_moreThan1MonthTo3Months(BigDecimal r43_moreThan1MonthTo3Months) {
			this.r43_moreThan1MonthTo3Months = r43_moreThan1MonthTo3Months;
		}

		public BigDecimal getR43_moreThan3MonthTo6Months() {
			return r43_moreThan3MonthTo6Months;
		}

		public void setR43_moreThan3MonthTo6Months(BigDecimal r43_moreThan3MonthTo6Months) {
			this.r43_moreThan3MonthTo6Months = r43_moreThan3MonthTo6Months;
		}

		public BigDecimal getR43_moreThan6MonthTo12Months() {
			return r43_moreThan6MonthTo12Months;
		}

		public void setR43_moreThan6MonthTo12Months(BigDecimal r43_moreThan6MonthTo12Months) {
			this.r43_moreThan6MonthTo12Months = r43_moreThan6MonthTo12Months;
		}

		public BigDecimal getR43_moreThan12MonthTo3Years() {
			return r43_moreThan12MonthTo3Years;
		}

		public void setR43_moreThan12MonthTo3Years(BigDecimal r43_moreThan12MonthTo3Years) {
			this.r43_moreThan12MonthTo3Years = r43_moreThan12MonthTo3Years;
		}

		public BigDecimal getR43_moreThan3YearsTo5Years() {
			return r43_moreThan3YearsTo5Years;
		}

		public void setR43_moreThan3YearsTo5Years(BigDecimal r43_moreThan3YearsTo5Years) {
			this.r43_moreThan3YearsTo5Years = r43_moreThan3YearsTo5Years;
		}

		public BigDecimal getR43_moreThan5YearsTo10Years() {
			return r43_moreThan5YearsTo10Years;
		}

		public void setR43_moreThan5YearsTo10Years(BigDecimal r43_moreThan5YearsTo10Years) {
			this.r43_moreThan5YearsTo10Years = r43_moreThan5YearsTo10Years;
		}

		public BigDecimal getR43_moreThan10Years() {
			return r43_moreThan10Years;
		}

		public void setR43_moreThan10Years(BigDecimal r43_moreThan10Years) {
			this.r43_moreThan10Years = r43_moreThan10Years;
		}

		public BigDecimal getR43_total() {
			return r43_total;
		}

		public void setR43_total(BigDecimal r43_total) {
			this.r43_total = r43_total;
		}

		public String getR44product() {
			return r44product;
		}

		public void setR44product(String r44product) {
			this.r44product = r44product;
		}

		public BigDecimal getR44_upTo1Month() {
			return r44_upTo1Month;
		}

		public void setR44_upTo1Month(BigDecimal r44_upTo1Month) {
			this.r44_upTo1Month = r44_upTo1Month;
		}

		public BigDecimal getR44_moreThan1MonthTo3Months() {
			return r44_moreThan1MonthTo3Months;
		}

		public void setR44_moreThan1MonthTo3Months(BigDecimal r44_moreThan1MonthTo3Months) {
			this.r44_moreThan1MonthTo3Months = r44_moreThan1MonthTo3Months;
		}

		public BigDecimal getR44_moreThan3MonthTo6Months() {
			return r44_moreThan3MonthTo6Months;
		}

		public void setR44_moreThan3MonthTo6Months(BigDecimal r44_moreThan3MonthTo6Months) {
			this.r44_moreThan3MonthTo6Months = r44_moreThan3MonthTo6Months;
		}

		public BigDecimal getR44_moreThan6MonthTo12Months() {
			return r44_moreThan6MonthTo12Months;
		}

		public void setR44_moreThan6MonthTo12Months(BigDecimal r44_moreThan6MonthTo12Months) {
			this.r44_moreThan6MonthTo12Months = r44_moreThan6MonthTo12Months;
		}

		public BigDecimal getR44_moreThan12MonthTo3Years() {
			return r44_moreThan12MonthTo3Years;
		}

		public void setR44_moreThan12MonthTo3Years(BigDecimal r44_moreThan12MonthTo3Years) {
			this.r44_moreThan12MonthTo3Years = r44_moreThan12MonthTo3Years;
		}

		public BigDecimal getR44_moreThan3YearsTo5Years() {
			return r44_moreThan3YearsTo5Years;
		}

		public void setR44_moreThan3YearsTo5Years(BigDecimal r44_moreThan3YearsTo5Years) {
			this.r44_moreThan3YearsTo5Years = r44_moreThan3YearsTo5Years;
		}

		public BigDecimal getR44_moreThan5YearsTo10Years() {
			return r44_moreThan5YearsTo10Years;
		}

		public void setR44_moreThan5YearsTo10Years(BigDecimal r44_moreThan5YearsTo10Years) {
			this.r44_moreThan5YearsTo10Years = r44_moreThan5YearsTo10Years;
		}

		public BigDecimal getR44_moreThan10Years() {
			return r44_moreThan10Years;
		}

		public void setR44_moreThan10Years(BigDecimal r44_moreThan10Years) {
			this.r44_moreThan10Years = r44_moreThan10Years;
		}

		public BigDecimal getR44_total() {
			return r44_total;
		}

		public void setR44_total(BigDecimal r44_total) {
			this.r44_total = r44_total;
		}

		public String getR45product() {
			return r45product;
		}

		public void setR45product(String r45product) {
			this.r45product = r45product;
		}

		public BigDecimal getR45_upTo1Month() {
			return r45_upTo1Month;
		}

		public void setR45_upTo1Month(BigDecimal r45_upTo1Month) {
			this.r45_upTo1Month = r45_upTo1Month;
		}

		public BigDecimal getR45_moreThan1MonthTo3Months() {
			return r45_moreThan1MonthTo3Months;
		}

		public void setR45_moreThan1MonthTo3Months(BigDecimal r45_moreThan1MonthTo3Months) {
			this.r45_moreThan1MonthTo3Months = r45_moreThan1MonthTo3Months;
		}

		public BigDecimal getR45_moreThan3MonthTo6Months() {
			return r45_moreThan3MonthTo6Months;
		}

		public void setR45_moreThan3MonthTo6Months(BigDecimal r45_moreThan3MonthTo6Months) {
			this.r45_moreThan3MonthTo6Months = r45_moreThan3MonthTo6Months;
		}

		public BigDecimal getR45_moreThan6MonthTo12Months() {
			return r45_moreThan6MonthTo12Months;
		}

		public void setR45_moreThan6MonthTo12Months(BigDecimal r45_moreThan6MonthTo12Months) {
			this.r45_moreThan6MonthTo12Months = r45_moreThan6MonthTo12Months;
		}

		public BigDecimal getR45_moreThan12MonthTo3Years() {
			return r45_moreThan12MonthTo3Years;
		}

		public void setR45_moreThan12MonthTo3Years(BigDecimal r45_moreThan12MonthTo3Years) {
			this.r45_moreThan12MonthTo3Years = r45_moreThan12MonthTo3Years;
		}

		public BigDecimal getR45_moreThan3YearsTo5Years() {
			return r45_moreThan3YearsTo5Years;
		}

		public void setR45_moreThan3YearsTo5Years(BigDecimal r45_moreThan3YearsTo5Years) {
			this.r45_moreThan3YearsTo5Years = r45_moreThan3YearsTo5Years;
		}

		public BigDecimal getR45_moreThan5YearsTo10Years() {
			return r45_moreThan5YearsTo10Years;
		}

		public void setR45_moreThan5YearsTo10Years(BigDecimal r45_moreThan5YearsTo10Years) {
			this.r45_moreThan5YearsTo10Years = r45_moreThan5YearsTo10Years;
		}

		public BigDecimal getR45_moreThan10Years() {
			return r45_moreThan10Years;
		}

		public void setR45_moreThan10Years(BigDecimal r45_moreThan10Years) {
			this.r45_moreThan10Years = r45_moreThan10Years;
		}

		public BigDecimal getR45_total() {
			return r45_total;
		}

		public void setR45_total(BigDecimal r45_total) {
			this.r45_total = r45_total;
		}

		public String getR46product() {
			return r46product;
		}

		public void setR46product(String r46product) {
			this.r46product = r46product;
		}

		public BigDecimal getR46_upTo1Month() {
			return r46_upTo1Month;
		}

		public void setR46_upTo1Month(BigDecimal r46_upTo1Month) {
			this.r46_upTo1Month = r46_upTo1Month;
		}

		public BigDecimal getR46_moreThan1MonthTo3Months() {
			return r46_moreThan1MonthTo3Months;
		}

		public void setR46_moreThan1MonthTo3Months(BigDecimal r46_moreThan1MonthTo3Months) {
			this.r46_moreThan1MonthTo3Months = r46_moreThan1MonthTo3Months;
		}

		public BigDecimal getR46_moreThan3MonthTo6Months() {
			return r46_moreThan3MonthTo6Months;
		}

		public void setR46_moreThan3MonthTo6Months(BigDecimal r46_moreThan3MonthTo6Months) {
			this.r46_moreThan3MonthTo6Months = r46_moreThan3MonthTo6Months;
		}

		public BigDecimal getR46_moreThan6MonthTo12Months() {
			return r46_moreThan6MonthTo12Months;
		}

		public void setR46_moreThan6MonthTo12Months(BigDecimal r46_moreThan6MonthTo12Months) {
			this.r46_moreThan6MonthTo12Months = r46_moreThan6MonthTo12Months;
		}

		public BigDecimal getR46_moreThan12MonthTo3Years() {
			return r46_moreThan12MonthTo3Years;
		}

		public void setR46_moreThan12MonthTo3Years(BigDecimal r46_moreThan12MonthTo3Years) {
			this.r46_moreThan12MonthTo3Years = r46_moreThan12MonthTo3Years;
		}

		public BigDecimal getR46_moreThan3YearsTo5Years() {
			return r46_moreThan3YearsTo5Years;
		}

		public void setR46_moreThan3YearsTo5Years(BigDecimal r46_moreThan3YearsTo5Years) {
			this.r46_moreThan3YearsTo5Years = r46_moreThan3YearsTo5Years;
		}

		public BigDecimal getR46_moreThan5YearsTo10Years() {
			return r46_moreThan5YearsTo10Years;
		}

		public void setR46_moreThan5YearsTo10Years(BigDecimal r46_moreThan5YearsTo10Years) {
			this.r46_moreThan5YearsTo10Years = r46_moreThan5YearsTo10Years;
		}

		public BigDecimal getR46_moreThan10Years() {
			return r46_moreThan10Years;
		}

		public void setR46_moreThan10Years(BigDecimal r46_moreThan10Years) {
			this.r46_moreThan10Years = r46_moreThan10Years;
		}

		public BigDecimal getR46_total() {
			return r46_total;
		}

		public void setR46_total(BigDecimal r46_total) {
			this.r46_total = r46_total;
		}

		public BigDecimal getR47_nonRatioSensativeItems() {
			return r47_nonRatioSensativeItems;
		}

		public void setR47_nonRatioSensativeItems(BigDecimal r47_nonRatioSensativeItems) {
			this.r47_nonRatioSensativeItems = r47_nonRatioSensativeItems;
		}

		public BigDecimal getR47_total() {
			return r47_total;
		}

		public void setR47_total(BigDecimal r47_total) {
			this.r47_total = r47_total;
		}

		public BigDecimal getR48_nonRatioSensativeItems() {
			return r48_nonRatioSensativeItems;
		}

		public void setR48_nonRatioSensativeItems(BigDecimal r48_nonRatioSensativeItems) {
			this.r48_nonRatioSensativeItems = r48_nonRatioSensativeItems;
		}

		public BigDecimal getR48_total() {
			return r48_total;
		}

		public void setR48_total(BigDecimal r48_total) {
			this.r48_total = r48_total;
		}

		public BigDecimal getR49_nonRatioSensativeItems() {
			return r49_nonRatioSensativeItems;
		}

		public void setR49_nonRatioSensativeItems(BigDecimal r49_nonRatioSensativeItems) {
			this.r49_nonRatioSensativeItems = r49_nonRatioSensativeItems;
		}

		public BigDecimal getR49_total() {
			return r49_total;
		}

		public void setR49_total(BigDecimal r49_total) {
			this.r49_total = r49_total;
		}

		public BigDecimal getR50_nonRatioSensativeItems() {
			return r50_nonRatioSensativeItems;
		}

		public void setR50_nonRatioSensativeItems(BigDecimal r50_nonRatioSensativeItems) {
			this.r50_nonRatioSensativeItems = r50_nonRatioSensativeItems;
		}

		public BigDecimal getR50_total() {
			return r50_total;
		}

		public void setR50_total(BigDecimal r50_total) {
			this.r50_total = r50_total;
		}

		public BigDecimal getR51_nonRatioSensativeItems() {
			return r51_nonRatioSensativeItems;
		}

		public void setR51_nonRatioSensativeItems(BigDecimal r51_nonRatioSensativeItems) {
			this.r51_nonRatioSensativeItems = r51_nonRatioSensativeItems;
		}

		public BigDecimal getR51_total() {
			return r51_total;
		}

		public void setR51_total(BigDecimal r51_total) {
			this.r51_total = r51_total;
		}

		public BigDecimal getR52_nonRatioSensativeItems() {
			return r52_nonRatioSensativeItems;
		}

		public void setR52_nonRatioSensativeItems(BigDecimal r52_nonRatioSensativeItems) {
			this.r52_nonRatioSensativeItems = r52_nonRatioSensativeItems;
		}

		public BigDecimal getR52_total() {
			return r52_total;
		}

		public void setR52_total(BigDecimal r52_total) {
			this.r52_total = r52_total;
		}

		public BigDecimal getR53_nonRatioSensativeItems() {
			return r53_nonRatioSensativeItems;
		}

		public void setR53_nonRatioSensativeItems(BigDecimal r53_nonRatioSensativeItems) {
			this.r53_nonRatioSensativeItems = r53_nonRatioSensativeItems;
		}

		public BigDecimal getR53_total() {
			return r53_total;
		}

		public void setR53_total(BigDecimal r53_total) {
			this.r53_total = r53_total;
		}

		public BigDecimal getR54_nonRatioSensativeItems() {
			return r54_nonRatioSensativeItems;
		}

		public void setR54_nonRatioSensativeItems(BigDecimal r54_nonRatioSensativeItems) {
			this.r54_nonRatioSensativeItems = r54_nonRatioSensativeItems;
		}

		public BigDecimal getR54_total() {
			return r54_total;
		}

		public void setR54_total(BigDecimal r54_total) {
			this.r54_total = r54_total;
		}

		public BigDecimal getR55_nonRatioSensativeItems() {
			return r55_nonRatioSensativeItems;
		}

		public void setR55_nonRatioSensativeItems(BigDecimal r55_nonRatioSensativeItems) {
			this.r55_nonRatioSensativeItems = r55_nonRatioSensativeItems;
		}

		public BigDecimal getR55_total() {
			return r55_total;
		}

		public void setR55_total(BigDecimal r55_total) {
			this.r55_total = r55_total;
		}

		public BigDecimal getR56_nonRatioSensativeItems() {
			return r56_nonRatioSensativeItems;
		}

		public void setR56_nonRatioSensativeItems(BigDecimal r56_nonRatioSensativeItems) {
			this.r56_nonRatioSensativeItems = r56_nonRatioSensativeItems;
		}

		public BigDecimal getR56_total() {
			return r56_total;
		}

		public void setR56_total(BigDecimal r56_total) {
			this.r56_total = r56_total;
		}

		public BigDecimal getR57_nonRatioSensativeItems() {
			return r57_nonRatioSensativeItems;
		}

		public void setR57_nonRatioSensativeItems(BigDecimal r57_nonRatioSensativeItems) {
			this.r57_nonRatioSensativeItems = r57_nonRatioSensativeItems;
		}

		public BigDecimal getR57_total() {
			return r57_total;
		}

		public void setR57_total(BigDecimal r57_total) {
			this.r57_total = r57_total;
		}

		public BigDecimal getR58_nonRatioSensativeItems() {
			return r58_nonRatioSensativeItems;
		}

		public void setR58_nonRatioSensativeItems(BigDecimal r58_nonRatioSensativeItems) {
			this.r58_nonRatioSensativeItems = r58_nonRatioSensativeItems;
		}

		public BigDecimal getR58_total() {
			return r58_total;
		}

		public void setR58_total(BigDecimal r58_total) {
			this.r58_total = r58_total;
		}

		public String getR59product() {
			return r59product;
		}

		public void setR59product(String r59product) {
			this.r59product = r59product;
		}

		public BigDecimal getR59_upTo1Month() {
			return r59_upTo1Month;
		}

		public void setR59_upTo1Month(BigDecimal r59_upTo1Month) {
			this.r59_upTo1Month = r59_upTo1Month;
		}

		public BigDecimal getR59_moreThan1MonthTo3Months() {
			return r59_moreThan1MonthTo3Months;
		}

		public void setR59_moreThan1MonthTo3Months(BigDecimal r59_moreThan1MonthTo3Months) {
			this.r59_moreThan1MonthTo3Months = r59_moreThan1MonthTo3Months;
		}

		public BigDecimal getR59_moreThan3MonthTo6Months() {
			return r59_moreThan3MonthTo6Months;
		}

		public void setR59_moreThan3MonthTo6Months(BigDecimal r59_moreThan3MonthTo6Months) {
			this.r59_moreThan3MonthTo6Months = r59_moreThan3MonthTo6Months;
		}

		public BigDecimal getR59_moreThan6MonthTo12Months() {
			return r59_moreThan6MonthTo12Months;
		}

		public void setR59_moreThan6MonthTo12Months(BigDecimal r59_moreThan6MonthTo12Months) {
			this.r59_moreThan6MonthTo12Months = r59_moreThan6MonthTo12Months;
		}

		public BigDecimal getR59_moreThan12MonthTo3Years() {
			return r59_moreThan12MonthTo3Years;
		}

		public void setR59_moreThan12MonthTo3Years(BigDecimal r59_moreThan12MonthTo3Years) {
			this.r59_moreThan12MonthTo3Years = r59_moreThan12MonthTo3Years;
		}

		public BigDecimal getR59_moreThan3YearsTo5Years() {
			return r59_moreThan3YearsTo5Years;
		}

		public void setR59_moreThan3YearsTo5Years(BigDecimal r59_moreThan3YearsTo5Years) {
			this.r59_moreThan3YearsTo5Years = r59_moreThan3YearsTo5Years;
		}

		public BigDecimal getR59_moreThan5YearsTo10Years() {
			return r59_moreThan5YearsTo10Years;
		}

		public void setR59_moreThan5YearsTo10Years(BigDecimal r59_moreThan5YearsTo10Years) {
			this.r59_moreThan5YearsTo10Years = r59_moreThan5YearsTo10Years;
		}

		public BigDecimal getR59_moreThan10Years() {
			return r59_moreThan10Years;
		}

		public void setR59_moreThan10Years(BigDecimal r59_moreThan10Years) {
			this.r59_moreThan10Years = r59_moreThan10Years;
		}

		public BigDecimal getR59_nonRatioSensativeItems() {
			return r59_nonRatioSensativeItems;
		}

		public void setR59_nonRatioSensativeItems(BigDecimal r59_nonRatioSensativeItems) {
			this.r59_nonRatioSensativeItems = r59_nonRatioSensativeItems;
		}

		public BigDecimal getR59_total() {
			return r59_total;
		}

		public void setR59_total(BigDecimal r59_total) {
			this.r59_total = r59_total;
		}

		public String getR60product() {
			return r60product;
		}

		public void setR60product(String r60product) {
			this.r60product = r60product;
		}

		public BigDecimal getR60_upTo1Month() {
			return r60_upTo1Month;
		}

		public void setR60_upTo1Month(BigDecimal r60_upTo1Month) {
			this.r60_upTo1Month = r60_upTo1Month;
		}

		public BigDecimal getR60_moreThan1MonthTo3Months() {
			return r60_moreThan1MonthTo3Months;
		}

		public void setR60_moreThan1MonthTo3Months(BigDecimal r60_moreThan1MonthTo3Months) {
			this.r60_moreThan1MonthTo3Months = r60_moreThan1MonthTo3Months;
		}

		public BigDecimal getR60_moreThan3MonthTo6Months() {
			return r60_moreThan3MonthTo6Months;
		}

		public void setR60_moreThan3MonthTo6Months(BigDecimal r60_moreThan3MonthTo6Months) {
			this.r60_moreThan3MonthTo6Months = r60_moreThan3MonthTo6Months;
		}

		public BigDecimal getR60_moreThan6MonthTo12Months() {
			return r60_moreThan6MonthTo12Months;
		}

		public void setR60_moreThan6MonthTo12Months(BigDecimal r60_moreThan6MonthTo12Months) {
			this.r60_moreThan6MonthTo12Months = r60_moreThan6MonthTo12Months;
		}

		public BigDecimal getR60_moreThan12MonthTo3Years() {
			return r60_moreThan12MonthTo3Years;
		}

		public void setR60_moreThan12MonthTo3Years(BigDecimal r60_moreThan12MonthTo3Years) {
			this.r60_moreThan12MonthTo3Years = r60_moreThan12MonthTo3Years;
		}

		public BigDecimal getR60_moreThan3YearsTo5Years() {
			return r60_moreThan3YearsTo5Years;
		}

		public void setR60_moreThan3YearsTo5Years(BigDecimal r60_moreThan3YearsTo5Years) {
			this.r60_moreThan3YearsTo5Years = r60_moreThan3YearsTo5Years;
		}

		public BigDecimal getR60_moreThan5YearsTo10Years() {
			return r60_moreThan5YearsTo10Years;
		}

		public void setR60_moreThan5YearsTo10Years(BigDecimal r60_moreThan5YearsTo10Years) {
			this.r60_moreThan5YearsTo10Years = r60_moreThan5YearsTo10Years;
		}

		public BigDecimal getR60_moreThan10Years() {
			return r60_moreThan10Years;
		}

		public void setR60_moreThan10Years(BigDecimal r60_moreThan10Years) {
			this.r60_moreThan10Years = r60_moreThan10Years;
		}

		public BigDecimal getR60_total() {
			return r60_total;
		}

		public void setR60_total(BigDecimal r60_total) {
			this.r60_total = r60_total;
		}

		public String getR61product() {
			return r61product;
		}

		public void setR61product(String r61product) {
			this.r61product = r61product;
		}

		public BigDecimal getR61_upTo1Month() {
			return r61_upTo1Month;
		}

		public void setR61_upTo1Month(BigDecimal r61_upTo1Month) {
			this.r61_upTo1Month = r61_upTo1Month;
		}

		public BigDecimal getR61_moreThan1MonthTo3Months() {
			return r61_moreThan1MonthTo3Months;
		}

		public void setR61_moreThan1MonthTo3Months(BigDecimal r61_moreThan1MonthTo3Months) {
			this.r61_moreThan1MonthTo3Months = r61_moreThan1MonthTo3Months;
		}

		public BigDecimal getR61_moreThan3MonthTo6Months() {
			return r61_moreThan3MonthTo6Months;
		}

		public void setR61_moreThan3MonthTo6Months(BigDecimal r61_moreThan3MonthTo6Months) {
			this.r61_moreThan3MonthTo6Months = r61_moreThan3MonthTo6Months;
		}

		public BigDecimal getR61_moreThan6MonthTo12Months() {
			return r61_moreThan6MonthTo12Months;
		}

		public void setR61_moreThan6MonthTo12Months(BigDecimal r61_moreThan6MonthTo12Months) {
			this.r61_moreThan6MonthTo12Months = r61_moreThan6MonthTo12Months;
		}

		public BigDecimal getR61_moreThan12MonthTo3Years() {
			return r61_moreThan12MonthTo3Years;
		}

		public void setR61_moreThan12MonthTo3Years(BigDecimal r61_moreThan12MonthTo3Years) {
			this.r61_moreThan12MonthTo3Years = r61_moreThan12MonthTo3Years;
		}

		public BigDecimal getR61_moreThan3YearsTo5Years() {
			return r61_moreThan3YearsTo5Years;
		}

		public void setR61_moreThan3YearsTo5Years(BigDecimal r61_moreThan3YearsTo5Years) {
			this.r61_moreThan3YearsTo5Years = r61_moreThan3YearsTo5Years;
		}

		public BigDecimal getR61_moreThan5YearsTo10Years() {
			return r61_moreThan5YearsTo10Years;
		}

		public void setR61_moreThan5YearsTo10Years(BigDecimal r61_moreThan5YearsTo10Years) {
			this.r61_moreThan5YearsTo10Years = r61_moreThan5YearsTo10Years;
		}

		public BigDecimal getR61_moreThan10Years() {
			return r61_moreThan10Years;
		}

		public void setR61_moreThan10Years(BigDecimal r61_moreThan10Years) {
			this.r61_moreThan10Years = r61_moreThan10Years;
		}

		public BigDecimal getR61_total() {
			return r61_total;
		}

		public void setR61_total(BigDecimal r61_total) {
			this.r61_total = r61_total;
		}

		public String getR62product() {
			return r62product;
		}

		public void setR62product(String r62product) {
			this.r62product = r62product;
		}

		public BigDecimal getR62_upTo1Month() {
			return r62_upTo1Month;
		}

		public void setR62_upTo1Month(BigDecimal r62_upTo1Month) {
			this.r62_upTo1Month = r62_upTo1Month;
		}

		public BigDecimal getR62_moreThan1MonthTo3Months() {
			return r62_moreThan1MonthTo3Months;
		}

		public void setR62_moreThan1MonthTo3Months(BigDecimal r62_moreThan1MonthTo3Months) {
			this.r62_moreThan1MonthTo3Months = r62_moreThan1MonthTo3Months;
		}

		public BigDecimal getR62_moreThan3MonthTo6Months() {
			return r62_moreThan3MonthTo6Months;
		}

		public void setR62_moreThan3MonthTo6Months(BigDecimal r62_moreThan3MonthTo6Months) {
			this.r62_moreThan3MonthTo6Months = r62_moreThan3MonthTo6Months;
		}

		public BigDecimal getR62_moreThan6MonthTo12Months() {
			return r62_moreThan6MonthTo12Months;
		}

		public void setR62_moreThan6MonthTo12Months(BigDecimal r62_moreThan6MonthTo12Months) {
			this.r62_moreThan6MonthTo12Months = r62_moreThan6MonthTo12Months;
		}

		public BigDecimal getR62_moreThan12MonthTo3Years() {
			return r62_moreThan12MonthTo3Years;
		}

		public void setR62_moreThan12MonthTo3Years(BigDecimal r62_moreThan12MonthTo3Years) {
			this.r62_moreThan12MonthTo3Years = r62_moreThan12MonthTo3Years;
		}

		public BigDecimal getR62_moreThan3YearsTo5Years() {
			return r62_moreThan3YearsTo5Years;
		}

		public void setR62_moreThan3YearsTo5Years(BigDecimal r62_moreThan3YearsTo5Years) {
			this.r62_moreThan3YearsTo5Years = r62_moreThan3YearsTo5Years;
		}

		public BigDecimal getR62_moreThan5YearsTo10Years() {
			return r62_moreThan5YearsTo10Years;
		}

		public void setR62_moreThan5YearsTo10Years(BigDecimal r62_moreThan5YearsTo10Years) {
			this.r62_moreThan5YearsTo10Years = r62_moreThan5YearsTo10Years;
		}

		public BigDecimal getR62_moreThan10Years() {
			return r62_moreThan10Years;
		}

		public void setR62_moreThan10Years(BigDecimal r62_moreThan10Years) {
			this.r62_moreThan10Years = r62_moreThan10Years;
		}

		public BigDecimal getR62_total() {
			return r62_total;
		}

		public void setR62_total(BigDecimal r62_total) {
			this.r62_total = r62_total;
		}

		public String getR63product() {
			return r63product;
		}

		public void setR63product(String r63product) {
			this.r63product = r63product;
		}

		public BigDecimal getR63_upTo1Month() {
			return r63_upTo1Month;
		}

		public void setR63_upTo1Month(BigDecimal r63_upTo1Month) {
			this.r63_upTo1Month = r63_upTo1Month;
		}

		public BigDecimal getR63_moreThan1MonthTo3Months() {
			return r63_moreThan1MonthTo3Months;
		}

		public void setR63_moreThan1MonthTo3Months(BigDecimal r63_moreThan1MonthTo3Months) {
			this.r63_moreThan1MonthTo3Months = r63_moreThan1MonthTo3Months;
		}

		public BigDecimal getR63_moreThan3MonthTo6Months() {
			return r63_moreThan3MonthTo6Months;
		}

		public void setR63_moreThan3MonthTo6Months(BigDecimal r63_moreThan3MonthTo6Months) {
			this.r63_moreThan3MonthTo6Months = r63_moreThan3MonthTo6Months;
		}

		public BigDecimal getR63_moreThan6MonthTo12Months() {
			return r63_moreThan6MonthTo12Months;
		}

		public void setR63_moreThan6MonthTo12Months(BigDecimal r63_moreThan6MonthTo12Months) {
			this.r63_moreThan6MonthTo12Months = r63_moreThan6MonthTo12Months;
		}

		public BigDecimal getR63_moreThan12MonthTo3Years() {
			return r63_moreThan12MonthTo3Years;
		}

		public void setR63_moreThan12MonthTo3Years(BigDecimal r63_moreThan12MonthTo3Years) {
			this.r63_moreThan12MonthTo3Years = r63_moreThan12MonthTo3Years;
		}

		public BigDecimal getR63_moreThan3YearsTo5Years() {
			return r63_moreThan3YearsTo5Years;
		}

		public void setR63_moreThan3YearsTo5Years(BigDecimal r63_moreThan3YearsTo5Years) {
			this.r63_moreThan3YearsTo5Years = r63_moreThan3YearsTo5Years;
		}

		public BigDecimal getR63_moreThan5YearsTo10Years() {
			return r63_moreThan5YearsTo10Years;
		}

		public void setR63_moreThan5YearsTo10Years(BigDecimal r63_moreThan5YearsTo10Years) {
			this.r63_moreThan5YearsTo10Years = r63_moreThan5YearsTo10Years;
		}

		public BigDecimal getR63_moreThan10Years() {
			return r63_moreThan10Years;
		}

		public void setR63_moreThan10Years(BigDecimal r63_moreThan10Years) {
			this.r63_moreThan10Years = r63_moreThan10Years;
		}

		public BigDecimal getR63_total() {
			return r63_total;
		}

		public void setR63_total(BigDecimal r63_total) {
			this.r63_total = r63_total;
		}

		public String getR64product() {
			return r64product;
		}

		public void setR64product(String r64product) {
			this.r64product = r64product;
		}

		public BigDecimal getR64_upTo1Month() {
			return r64_upTo1Month;
		}

		public void setR64_upTo1Month(BigDecimal r64_upTo1Month) {
			this.r64_upTo1Month = r64_upTo1Month;
		}

		public BigDecimal getR64_moreThan1MonthTo3Months() {
			return r64_moreThan1MonthTo3Months;
		}

		public void setR64_moreThan1MonthTo3Months(BigDecimal r64_moreThan1MonthTo3Months) {
			this.r64_moreThan1MonthTo3Months = r64_moreThan1MonthTo3Months;
		}

		public BigDecimal getR64_moreThan3MonthTo6Months() {
			return r64_moreThan3MonthTo6Months;
		}

		public void setR64_moreThan3MonthTo6Months(BigDecimal r64_moreThan3MonthTo6Months) {
			this.r64_moreThan3MonthTo6Months = r64_moreThan3MonthTo6Months;
		}

		public BigDecimal getR64_moreThan6MonthTo12Months() {
			return r64_moreThan6MonthTo12Months;
		}

		public void setR64_moreThan6MonthTo12Months(BigDecimal r64_moreThan6MonthTo12Months) {
			this.r64_moreThan6MonthTo12Months = r64_moreThan6MonthTo12Months;
		}

		public BigDecimal getR64_moreThan12MonthTo3Years() {
			return r64_moreThan12MonthTo3Years;
		}

		public void setR64_moreThan12MonthTo3Years(BigDecimal r64_moreThan12MonthTo3Years) {
			this.r64_moreThan12MonthTo3Years = r64_moreThan12MonthTo3Years;
		}

		public BigDecimal getR64_moreThan3YearsTo5Years() {
			return r64_moreThan3YearsTo5Years;
		}

		public void setR64_moreThan3YearsTo5Years(BigDecimal r64_moreThan3YearsTo5Years) {
			this.r64_moreThan3YearsTo5Years = r64_moreThan3YearsTo5Years;
		}

		public BigDecimal getR64_moreThan5YearsTo10Years() {
			return r64_moreThan5YearsTo10Years;
		}

		public void setR64_moreThan5YearsTo10Years(BigDecimal r64_moreThan5YearsTo10Years) {
			this.r64_moreThan5YearsTo10Years = r64_moreThan5YearsTo10Years;
		}

		public BigDecimal getR64_moreThan10Years() {
			return r64_moreThan10Years;
		}

		public void setR64_moreThan10Years(BigDecimal r64_moreThan10Years) {
			this.r64_moreThan10Years = r64_moreThan10Years;
		}

		public BigDecimal getR64_total() {
			return r64_total;
		}

		public void setR64_total(BigDecimal r64_total) {
			this.r64_total = r64_total;
		}

		public String getR65product() {
			return r65product;
		}

		public void setR65product(String r65product) {
			this.r65product = r65product;
		}

		public BigDecimal getR65_upTo1Month() {
			return r65_upTo1Month;
		}

		public void setR65_upTo1Month(BigDecimal r65_upTo1Month) {
			this.r65_upTo1Month = r65_upTo1Month;
		}

		public BigDecimal getR65_moreThan1MonthTo3Months() {
			return r65_moreThan1MonthTo3Months;
		}

		public void setR65_moreThan1MonthTo3Months(BigDecimal r65_moreThan1MonthTo3Months) {
			this.r65_moreThan1MonthTo3Months = r65_moreThan1MonthTo3Months;
		}

		public BigDecimal getR65_moreThan3MonthTo6Months() {
			return r65_moreThan3MonthTo6Months;
		}

		public void setR65_moreThan3MonthTo6Months(BigDecimal r65_moreThan3MonthTo6Months) {
			this.r65_moreThan3MonthTo6Months = r65_moreThan3MonthTo6Months;
		}

		public BigDecimal getR65_moreThan6MonthTo12Months() {
			return r65_moreThan6MonthTo12Months;
		}

		public void setR65_moreThan6MonthTo12Months(BigDecimal r65_moreThan6MonthTo12Months) {
			this.r65_moreThan6MonthTo12Months = r65_moreThan6MonthTo12Months;
		}

		public BigDecimal getR65_moreThan12MonthTo3Years() {
			return r65_moreThan12MonthTo3Years;
		}

		public void setR65_moreThan12MonthTo3Years(BigDecimal r65_moreThan12MonthTo3Years) {
			this.r65_moreThan12MonthTo3Years = r65_moreThan12MonthTo3Years;
		}

		public BigDecimal getR65_moreThan3YearsTo5Years() {
			return r65_moreThan3YearsTo5Years;
		}

		public void setR65_moreThan3YearsTo5Years(BigDecimal r65_moreThan3YearsTo5Years) {
			this.r65_moreThan3YearsTo5Years = r65_moreThan3YearsTo5Years;
		}

		public BigDecimal getR65_moreThan5YearsTo10Years() {
			return r65_moreThan5YearsTo10Years;
		}

		public void setR65_moreThan5YearsTo10Years(BigDecimal r65_moreThan5YearsTo10Years) {
			this.r65_moreThan5YearsTo10Years = r65_moreThan5YearsTo10Years;
		}

		public BigDecimal getR65_moreThan10Years() {
			return r65_moreThan10Years;
		}

		public void setR65_moreThan10Years(BigDecimal r65_moreThan10Years) {
			this.r65_moreThan10Years = r65_moreThan10Years;
		}

		public BigDecimal getR65_total() {
			return r65_total;
		}

		public void setR65_total(BigDecimal r65_total) {
			this.r65_total = r65_total;
		}

		public String getR66product() {
			return r66product;
		}

		public void setR66product(String r66product) {
			this.r66product = r66product;
		}

		public BigDecimal getR66_upTo1Month() {
			return r66_upTo1Month;
		}

		public void setR66_upTo1Month(BigDecimal r66_upTo1Month) {
			this.r66_upTo1Month = r66_upTo1Month;
		}

		public BigDecimal getR66_moreThan1MonthTo3Months() {
			return r66_moreThan1MonthTo3Months;
		}

		public void setR66_moreThan1MonthTo3Months(BigDecimal r66_moreThan1MonthTo3Months) {
			this.r66_moreThan1MonthTo3Months = r66_moreThan1MonthTo3Months;
		}

		public BigDecimal getR66_moreThan3MonthTo6Months() {
			return r66_moreThan3MonthTo6Months;
		}

		public void setR66_moreThan3MonthTo6Months(BigDecimal r66_moreThan3MonthTo6Months) {
			this.r66_moreThan3MonthTo6Months = r66_moreThan3MonthTo6Months;
		}

		public BigDecimal getR66_moreThan6MonthTo12Months() {
			return r66_moreThan6MonthTo12Months;
		}

		public void setR66_moreThan6MonthTo12Months(BigDecimal r66_moreThan6MonthTo12Months) {
			this.r66_moreThan6MonthTo12Months = r66_moreThan6MonthTo12Months;
		}

		public BigDecimal getR66_moreThan12MonthTo3Years() {
			return r66_moreThan12MonthTo3Years;
		}

		public void setR66_moreThan12MonthTo3Years(BigDecimal r66_moreThan12MonthTo3Years) {
			this.r66_moreThan12MonthTo3Years = r66_moreThan12MonthTo3Years;
		}

		public BigDecimal getR66_moreThan3YearsTo5Years() {
			return r66_moreThan3YearsTo5Years;
		}

		public void setR66_moreThan3YearsTo5Years(BigDecimal r66_moreThan3YearsTo5Years) {
			this.r66_moreThan3YearsTo5Years = r66_moreThan3YearsTo5Years;
		}

		public BigDecimal getR66_moreThan5YearsTo10Years() {
			return r66_moreThan5YearsTo10Years;
		}

		public void setR66_moreThan5YearsTo10Years(BigDecimal r66_moreThan5YearsTo10Years) {
			this.r66_moreThan5YearsTo10Years = r66_moreThan5YearsTo10Years;
		}

		public BigDecimal getR66_moreThan10Years() {
			return r66_moreThan10Years;
		}

		public void setR66_moreThan10Years(BigDecimal r66_moreThan10Years) {
			this.r66_moreThan10Years = r66_moreThan10Years;
		}

		public BigDecimal getR66_total() {
			return r66_total;
		}

		public void setR66_total(BigDecimal r66_total) {
			this.r66_total = r66_total;
		}

		public String getR67product() {
			return r67product;
		}

		public void setR67product(String r67product) {
			this.r67product = r67product;
		}

		public BigDecimal getR67_upTo1Month() {
			return r67_upTo1Month;
		}

		public void setR67_upTo1Month(BigDecimal r67_upTo1Month) {
			this.r67_upTo1Month = r67_upTo1Month;
		}

		public BigDecimal getR67_moreThan1MonthTo3Months() {
			return r67_moreThan1MonthTo3Months;
		}

		public void setR67_moreThan1MonthTo3Months(BigDecimal r67_moreThan1MonthTo3Months) {
			this.r67_moreThan1MonthTo3Months = r67_moreThan1MonthTo3Months;
		}

		public BigDecimal getR67_moreThan3MonthTo6Months() {
			return r67_moreThan3MonthTo6Months;
		}

		public void setR67_moreThan3MonthTo6Months(BigDecimal r67_moreThan3MonthTo6Months) {
			this.r67_moreThan3MonthTo6Months = r67_moreThan3MonthTo6Months;
		}

		public BigDecimal getR67_moreThan6MonthTo12Months() {
			return r67_moreThan6MonthTo12Months;
		}

		public void setR67_moreThan6MonthTo12Months(BigDecimal r67_moreThan6MonthTo12Months) {
			this.r67_moreThan6MonthTo12Months = r67_moreThan6MonthTo12Months;
		}

		public BigDecimal getR67_moreThan12MonthTo3Years() {
			return r67_moreThan12MonthTo3Years;
		}

		public void setR67_moreThan12MonthTo3Years(BigDecimal r67_moreThan12MonthTo3Years) {
			this.r67_moreThan12MonthTo3Years = r67_moreThan12MonthTo3Years;
		}

		public BigDecimal getR67_moreThan3YearsTo5Years() {
			return r67_moreThan3YearsTo5Years;
		}

		public void setR67_moreThan3YearsTo5Years(BigDecimal r67_moreThan3YearsTo5Years) {
			this.r67_moreThan3YearsTo5Years = r67_moreThan3YearsTo5Years;
		}

		public BigDecimal getR67_moreThan5YearsTo10Years() {
			return r67_moreThan5YearsTo10Years;
		}

		public void setR67_moreThan5YearsTo10Years(BigDecimal r67_moreThan5YearsTo10Years) {
			this.r67_moreThan5YearsTo10Years = r67_moreThan5YearsTo10Years;
		}

		public BigDecimal getR67_moreThan10Years() {
			return r67_moreThan10Years;
		}

		public void setR67_moreThan10Years(BigDecimal r67_moreThan10Years) {
			this.r67_moreThan10Years = r67_moreThan10Years;
		}

		public BigDecimal getR67_total() {
			return r67_total;
		}

		public void setR67_total(BigDecimal r67_total) {
			this.r67_total = r67_total;
		}

		public String getR68product() {
			return r68product;
		}

		public void setR68product(String r68product) {
			this.r68product = r68product;
		}

		public BigDecimal getR68_upTo1Month() {
			return r68_upTo1Month;
		}

		public void setR68_upTo1Month(BigDecimal r68_upTo1Month) {
			this.r68_upTo1Month = r68_upTo1Month;
		}

		public BigDecimal getR68_moreThan1MonthTo3Months() {
			return r68_moreThan1MonthTo3Months;
		}

		public void setR68_moreThan1MonthTo3Months(BigDecimal r68_moreThan1MonthTo3Months) {
			this.r68_moreThan1MonthTo3Months = r68_moreThan1MonthTo3Months;
		}

		public BigDecimal getR68_moreThan3MonthTo6Months() {
			return r68_moreThan3MonthTo6Months;
		}

		public void setR68_moreThan3MonthTo6Months(BigDecimal r68_moreThan3MonthTo6Months) {
			this.r68_moreThan3MonthTo6Months = r68_moreThan3MonthTo6Months;
		}

		public BigDecimal getR68_moreThan6MonthTo12Months() {
			return r68_moreThan6MonthTo12Months;
		}

		public void setR68_moreThan6MonthTo12Months(BigDecimal r68_moreThan6MonthTo12Months) {
			this.r68_moreThan6MonthTo12Months = r68_moreThan6MonthTo12Months;
		}

		public BigDecimal getR68_moreThan12MonthTo3Years() {
			return r68_moreThan12MonthTo3Years;
		}

		public void setR68_moreThan12MonthTo3Years(BigDecimal r68_moreThan12MonthTo3Years) {
			this.r68_moreThan12MonthTo3Years = r68_moreThan12MonthTo3Years;
		}

		public BigDecimal getR68_moreThan3YearsTo5Years() {
			return r68_moreThan3YearsTo5Years;
		}

		public void setR68_moreThan3YearsTo5Years(BigDecimal r68_moreThan3YearsTo5Years) {
			this.r68_moreThan3YearsTo5Years = r68_moreThan3YearsTo5Years;
		}

		public BigDecimal getR68_moreThan5YearsTo10Years() {
			return r68_moreThan5YearsTo10Years;
		}

		public void setR68_moreThan5YearsTo10Years(BigDecimal r68_moreThan5YearsTo10Years) {
			this.r68_moreThan5YearsTo10Years = r68_moreThan5YearsTo10Years;
		}

		public BigDecimal getR68_moreThan10Years() {
			return r68_moreThan10Years;
		}

		public void setR68_moreThan10Years(BigDecimal r68_moreThan10Years) {
			this.r68_moreThan10Years = r68_moreThan10Years;
		}

		public BigDecimal getR68_total() {
			return r68_total;
		}

		public void setR68_total(BigDecimal r68_total) {
			this.r68_total = r68_total;
		}

		public String getR69product() {
			return r69product;
		}

		public void setR69product(String r69product) {
			this.r69product = r69product;
		}

		public BigDecimal getR69_upTo1Month() {
			return r69_upTo1Month;
		}

		public void setR69_upTo1Month(BigDecimal r69_upTo1Month) {
			this.r69_upTo1Month = r69_upTo1Month;
		}

		public BigDecimal getR69_moreThan1MonthTo3Months() {
			return r69_moreThan1MonthTo3Months;
		}

		public void setR69_moreThan1MonthTo3Months(BigDecimal r69_moreThan1MonthTo3Months) {
			this.r69_moreThan1MonthTo3Months = r69_moreThan1MonthTo3Months;
		}

		public BigDecimal getR69_moreThan3MonthTo6Months() {
			return r69_moreThan3MonthTo6Months;
		}

		public void setR69_moreThan3MonthTo6Months(BigDecimal r69_moreThan3MonthTo6Months) {
			this.r69_moreThan3MonthTo6Months = r69_moreThan3MonthTo6Months;
		}

		public BigDecimal getR69_moreThan6MonthTo12Months() {
			return r69_moreThan6MonthTo12Months;
		}

		public void setR69_moreThan6MonthTo12Months(BigDecimal r69_moreThan6MonthTo12Months) {
			this.r69_moreThan6MonthTo12Months = r69_moreThan6MonthTo12Months;
		}

		public BigDecimal getR69_moreThan12MonthTo3Years() {
			return r69_moreThan12MonthTo3Years;
		}

		public void setR69_moreThan12MonthTo3Years(BigDecimal r69_moreThan12MonthTo3Years) {
			this.r69_moreThan12MonthTo3Years = r69_moreThan12MonthTo3Years;
		}

		public BigDecimal getR69_moreThan3YearsTo5Years() {
			return r69_moreThan3YearsTo5Years;
		}

		public void setR69_moreThan3YearsTo5Years(BigDecimal r69_moreThan3YearsTo5Years) {
			this.r69_moreThan3YearsTo5Years = r69_moreThan3YearsTo5Years;
		}

		public BigDecimal getR69_moreThan5YearsTo10Years() {
			return r69_moreThan5YearsTo10Years;
		}

		public void setR69_moreThan5YearsTo10Years(BigDecimal r69_moreThan5YearsTo10Years) {
			this.r69_moreThan5YearsTo10Years = r69_moreThan5YearsTo10Years;
		}

		public BigDecimal getR69_moreThan10Years() {
			return r69_moreThan10Years;
		}

		public void setR69_moreThan10Years(BigDecimal r69_moreThan10Years) {
			this.r69_moreThan10Years = r69_moreThan10Years;
		}

		public BigDecimal getR69_total() {
			return r69_total;
		}

		public void setR69_total(BigDecimal r69_total) {
			this.r69_total = r69_total;
		}

		public String getR70product() {
			return r70product;
		}

		public void setR70product(String r70product) {
			this.r70product = r70product;
		}

		public BigDecimal getR70_upTo1Month() {
			return r70_upTo1Month;
		}

		public void setR70_upTo1Month(BigDecimal r70_upTo1Month) {
			this.r70_upTo1Month = r70_upTo1Month;
		}

		public BigDecimal getR70_moreThan1MonthTo3Months() {
			return r70_moreThan1MonthTo3Months;
		}

		public void setR70_moreThan1MonthTo3Months(BigDecimal r70_moreThan1MonthTo3Months) {
			this.r70_moreThan1MonthTo3Months = r70_moreThan1MonthTo3Months;
		}

		public BigDecimal getR70_moreThan3MonthTo6Months() {
			return r70_moreThan3MonthTo6Months;
		}

		public void setR70_moreThan3MonthTo6Months(BigDecimal r70_moreThan3MonthTo6Months) {
			this.r70_moreThan3MonthTo6Months = r70_moreThan3MonthTo6Months;
		}

		public BigDecimal getR70_moreThan6MonthTo12Months() {
			return r70_moreThan6MonthTo12Months;
		}

		public void setR70_moreThan6MonthTo12Months(BigDecimal r70_moreThan6MonthTo12Months) {
			this.r70_moreThan6MonthTo12Months = r70_moreThan6MonthTo12Months;
		}

		public BigDecimal getR70_moreThan12MonthTo3Years() {
			return r70_moreThan12MonthTo3Years;
		}

		public void setR70_moreThan12MonthTo3Years(BigDecimal r70_moreThan12MonthTo3Years) {
			this.r70_moreThan12MonthTo3Years = r70_moreThan12MonthTo3Years;
		}

		public BigDecimal getR70_moreThan3YearsTo5Years() {
			return r70_moreThan3YearsTo5Years;
		}

		public void setR70_moreThan3YearsTo5Years(BigDecimal r70_moreThan3YearsTo5Years) {
			this.r70_moreThan3YearsTo5Years = r70_moreThan3YearsTo5Years;
		}

		public BigDecimal getR70_moreThan5YearsTo10Years() {
			return r70_moreThan5YearsTo10Years;
		}

		public void setR70_moreThan5YearsTo10Years(BigDecimal r70_moreThan5YearsTo10Years) {
			this.r70_moreThan5YearsTo10Years = r70_moreThan5YearsTo10Years;
		}

		public BigDecimal getR70_moreThan10Years() {
			return r70_moreThan10Years;
		}

		public void setR70_moreThan10Years(BigDecimal r70_moreThan10Years) {
			this.r70_moreThan10Years = r70_moreThan10Years;
		}

		public BigDecimal getR70_total() {
			return r70_total;
		}

		public void setR70_total(BigDecimal r70_total) {
			this.r70_total = r70_total;
		}

		public String getR71product() {
			return r71product;
		}

		public void setR71product(String r71product) {
			this.r71product = r71product;
		}

		public BigDecimal getR71_upTo1Month() {
			return r71_upTo1Month;
		}

		public void setR71_upTo1Month(BigDecimal r71_upTo1Month) {
			this.r71_upTo1Month = r71_upTo1Month;
		}

		public BigDecimal getR71_moreThan1MonthTo3Months() {
			return r71_moreThan1MonthTo3Months;
		}

		public void setR71_moreThan1MonthTo3Months(BigDecimal r71_moreThan1MonthTo3Months) {
			this.r71_moreThan1MonthTo3Months = r71_moreThan1MonthTo3Months;
		}

		public BigDecimal getR71_moreThan3MonthTo6Months() {
			return r71_moreThan3MonthTo6Months;
		}

		public void setR71_moreThan3MonthTo6Months(BigDecimal r71_moreThan3MonthTo6Months) {
			this.r71_moreThan3MonthTo6Months = r71_moreThan3MonthTo6Months;
		}

		public BigDecimal getR71_moreThan6MonthTo12Months() {
			return r71_moreThan6MonthTo12Months;
		}

		public void setR71_moreThan6MonthTo12Months(BigDecimal r71_moreThan6MonthTo12Months) {
			this.r71_moreThan6MonthTo12Months = r71_moreThan6MonthTo12Months;
		}

		public BigDecimal getR71_moreThan12MonthTo3Years() {
			return r71_moreThan12MonthTo3Years;
		}

		public void setR71_moreThan12MonthTo3Years(BigDecimal r71_moreThan12MonthTo3Years) {
			this.r71_moreThan12MonthTo3Years = r71_moreThan12MonthTo3Years;
		}

		public BigDecimal getR71_moreThan3YearsTo5Years() {
			return r71_moreThan3YearsTo5Years;
		}

		public void setR71_moreThan3YearsTo5Years(BigDecimal r71_moreThan3YearsTo5Years) {
			this.r71_moreThan3YearsTo5Years = r71_moreThan3YearsTo5Years;
		}

		public BigDecimal getR71_moreThan5YearsTo10Years() {
			return r71_moreThan5YearsTo10Years;
		}

		public void setR71_moreThan5YearsTo10Years(BigDecimal r71_moreThan5YearsTo10Years) {
			this.r71_moreThan5YearsTo10Years = r71_moreThan5YearsTo10Years;
		}

		public BigDecimal getR71_moreThan10Years() {
			return r71_moreThan10Years;
		}

		public void setR71_moreThan10Years(BigDecimal r71_moreThan10Years) {
			this.r71_moreThan10Years = r71_moreThan10Years;
		}

		public BigDecimal getR71_total() {
			return r71_total;
		}

		public void setR71_total(BigDecimal r71_total) {
			this.r71_total = r71_total;
		}

		public String getR72product() {
			return r72product;
		}

		public void setR72product(String r72product) {
			this.r72product = r72product;
		}

		public BigDecimal getR72_upTo1Month() {
			return r72_upTo1Month;
		}

		public void setR72_upTo1Month(BigDecimal r72_upTo1Month) {
			this.r72_upTo1Month = r72_upTo1Month;
		}

		public BigDecimal getR72_moreThan1MonthTo3Months() {
			return r72_moreThan1MonthTo3Months;
		}

		public void setR72_moreThan1MonthTo3Months(BigDecimal r72_moreThan1MonthTo3Months) {
			this.r72_moreThan1MonthTo3Months = r72_moreThan1MonthTo3Months;
		}

		public BigDecimal getR72_moreThan3MonthTo6Months() {
			return r72_moreThan3MonthTo6Months;
		}

		public void setR72_moreThan3MonthTo6Months(BigDecimal r72_moreThan3MonthTo6Months) {
			this.r72_moreThan3MonthTo6Months = r72_moreThan3MonthTo6Months;
		}

		public BigDecimal getR72_moreThan6MonthTo12Months() {
			return r72_moreThan6MonthTo12Months;
		}

		public void setR72_moreThan6MonthTo12Months(BigDecimal r72_moreThan6MonthTo12Months) {
			this.r72_moreThan6MonthTo12Months = r72_moreThan6MonthTo12Months;
		}

		public BigDecimal getR72_moreThan12MonthTo3Years() {
			return r72_moreThan12MonthTo3Years;
		}

		public void setR72_moreThan12MonthTo3Years(BigDecimal r72_moreThan12MonthTo3Years) {
			this.r72_moreThan12MonthTo3Years = r72_moreThan12MonthTo3Years;
		}

		public BigDecimal getR72_moreThan3YearsTo5Years() {
			return r72_moreThan3YearsTo5Years;
		}

		public void setR72_moreThan3YearsTo5Years(BigDecimal r72_moreThan3YearsTo5Years) {
			this.r72_moreThan3YearsTo5Years = r72_moreThan3YearsTo5Years;
		}

		public BigDecimal getR72_moreThan5YearsTo10Years() {
			return r72_moreThan5YearsTo10Years;
		}

		public void setR72_moreThan5YearsTo10Years(BigDecimal r72_moreThan5YearsTo10Years) {
			this.r72_moreThan5YearsTo10Years = r72_moreThan5YearsTo10Years;
		}

		public BigDecimal getR72_moreThan10Years() {
			return r72_moreThan10Years;
		}

		public void setR72_moreThan10Years(BigDecimal r72_moreThan10Years) {
			this.r72_moreThan10Years = r72_moreThan10Years;
		}

		public BigDecimal getR72_total() {
			return r72_total;
		}

		public void setR72_total(BigDecimal r72_total) {
			this.r72_total = r72_total;
		}

		public String getR73product() {
			return r73product;
		}

		public void setR73product(String r73product) {
			this.r73product = r73product;
		}

		public BigDecimal getR73_upTo1Month() {
			return r73_upTo1Month;
		}

		public void setR73_upTo1Month(BigDecimal r73_upTo1Month) {
			this.r73_upTo1Month = r73_upTo1Month;
		}

		public BigDecimal getR73_moreThan1MonthTo3Months() {
			return r73_moreThan1MonthTo3Months;
		}

		public void setR73_moreThan1MonthTo3Months(BigDecimal r73_moreThan1MonthTo3Months) {
			this.r73_moreThan1MonthTo3Months = r73_moreThan1MonthTo3Months;
		}

		public BigDecimal getR73_moreThan3MonthTo6Months() {
			return r73_moreThan3MonthTo6Months;
		}

		public void setR73_moreThan3MonthTo6Months(BigDecimal r73_moreThan3MonthTo6Months) {
			this.r73_moreThan3MonthTo6Months = r73_moreThan3MonthTo6Months;
		}

		public BigDecimal getR73_moreThan6MonthTo12Months() {
			return r73_moreThan6MonthTo12Months;
		}

		public void setR73_moreThan6MonthTo12Months(BigDecimal r73_moreThan6MonthTo12Months) {
			this.r73_moreThan6MonthTo12Months = r73_moreThan6MonthTo12Months;
		}

		public BigDecimal getR73_moreThan12MonthTo3Years() {
			return r73_moreThan12MonthTo3Years;
		}

		public void setR73_moreThan12MonthTo3Years(BigDecimal r73_moreThan12MonthTo3Years) {
			this.r73_moreThan12MonthTo3Years = r73_moreThan12MonthTo3Years;
		}

		public BigDecimal getR73_moreThan3YearsTo5Years() {
			return r73_moreThan3YearsTo5Years;
		}

		public void setR73_moreThan3YearsTo5Years(BigDecimal r73_moreThan3YearsTo5Years) {
			this.r73_moreThan3YearsTo5Years = r73_moreThan3YearsTo5Years;
		}

		public BigDecimal getR73_moreThan5YearsTo10Years() {
			return r73_moreThan5YearsTo10Years;
		}

		public void setR73_moreThan5YearsTo10Years(BigDecimal r73_moreThan5YearsTo10Years) {
			this.r73_moreThan5YearsTo10Years = r73_moreThan5YearsTo10Years;
		}

		public BigDecimal getR73_moreThan10Years() {
			return r73_moreThan10Years;
		}

		public void setR73_moreThan10Years(BigDecimal r73_moreThan10Years) {
			this.r73_moreThan10Years = r73_moreThan10Years;
		}

		public BigDecimal getR73_total() {
			return r73_total;
		}

		public void setR73_total(BigDecimal r73_total) {
			this.r73_total = r73_total;
		}

		public String getR74product() {
			return r74product;
		}

		public void setR74product(String r74product) {
			this.r74product = r74product;
		}

		public BigDecimal getR74_upTo1Month() {
			return r74_upTo1Month;
		}

		public void setR74_upTo1Month(BigDecimal r74_upTo1Month) {
			this.r74_upTo1Month = r74_upTo1Month;
		}

		public BigDecimal getR74_moreThan1MonthTo3Months() {
			return r74_moreThan1MonthTo3Months;
		}

		public void setR74_moreThan1MonthTo3Months(BigDecimal r74_moreThan1MonthTo3Months) {
			this.r74_moreThan1MonthTo3Months = r74_moreThan1MonthTo3Months;
		}

		public BigDecimal getR74_moreThan3MonthTo6Months() {
			return r74_moreThan3MonthTo6Months;
		}

		public void setR74_moreThan3MonthTo6Months(BigDecimal r74_moreThan3MonthTo6Months) {
			this.r74_moreThan3MonthTo6Months = r74_moreThan3MonthTo6Months;
		}

		public BigDecimal getR74_moreThan6MonthTo12Months() {
			return r74_moreThan6MonthTo12Months;
		}

		public void setR74_moreThan6MonthTo12Months(BigDecimal r74_moreThan6MonthTo12Months) {
			this.r74_moreThan6MonthTo12Months = r74_moreThan6MonthTo12Months;
		}

		public BigDecimal getR74_moreThan12MonthTo3Years() {
			return r74_moreThan12MonthTo3Years;
		}

		public void setR74_moreThan12MonthTo3Years(BigDecimal r74_moreThan12MonthTo3Years) {
			this.r74_moreThan12MonthTo3Years = r74_moreThan12MonthTo3Years;
		}

		public BigDecimal getR74_moreThan3YearsTo5Years() {
			return r74_moreThan3YearsTo5Years;
		}

		public void setR74_moreThan3YearsTo5Years(BigDecimal r74_moreThan3YearsTo5Years) {
			this.r74_moreThan3YearsTo5Years = r74_moreThan3YearsTo5Years;
		}

		public BigDecimal getR74_moreThan5YearsTo10Years() {
			return r74_moreThan5YearsTo10Years;
		}

		public void setR74_moreThan5YearsTo10Years(BigDecimal r74_moreThan5YearsTo10Years) {
			this.r74_moreThan5YearsTo10Years = r74_moreThan5YearsTo10Years;
		}

		public BigDecimal getR74_moreThan10Years() {
			return r74_moreThan10Years;
		}

		public void setR74_moreThan10Years(BigDecimal r74_moreThan10Years) {
			this.r74_moreThan10Years = r74_moreThan10Years;
		}

		public BigDecimal getR74_total() {
			return r74_total;
		}

		public void setR74_total(BigDecimal r74_total) {
			this.r74_total = r74_total;
		}

		public String getR75product() {
			return r75product;
		}

		public void setR75product(String r75product) {
			this.r75product = r75product;
		}

		public BigDecimal getR75_upTo1Month() {
			return r75_upTo1Month;
		}

		public void setR75_upTo1Month(BigDecimal r75_upTo1Month) {
			this.r75_upTo1Month = r75_upTo1Month;
		}

		public BigDecimal getR75_moreThan1MonthTo3Months() {
			return r75_moreThan1MonthTo3Months;
		}

		public void setR75_moreThan1MonthTo3Months(BigDecimal r75_moreThan1MonthTo3Months) {
			this.r75_moreThan1MonthTo3Months = r75_moreThan1MonthTo3Months;
		}

		public BigDecimal getR75_moreThan3MonthTo6Months() {
			return r75_moreThan3MonthTo6Months;
		}

		public void setR75_moreThan3MonthTo6Months(BigDecimal r75_moreThan3MonthTo6Months) {
			this.r75_moreThan3MonthTo6Months = r75_moreThan3MonthTo6Months;
		}

		public BigDecimal getR75_moreThan6MonthTo12Months() {
			return r75_moreThan6MonthTo12Months;
		}

		public void setR75_moreThan6MonthTo12Months(BigDecimal r75_moreThan6MonthTo12Months) {
			this.r75_moreThan6MonthTo12Months = r75_moreThan6MonthTo12Months;
		}

		public BigDecimal getR75_moreThan12MonthTo3Years() {
			return r75_moreThan12MonthTo3Years;
		}

		public void setR75_moreThan12MonthTo3Years(BigDecimal r75_moreThan12MonthTo3Years) {
			this.r75_moreThan12MonthTo3Years = r75_moreThan12MonthTo3Years;
		}

		public BigDecimal getR75_moreThan3YearsTo5Years() {
			return r75_moreThan3YearsTo5Years;
		}

		public void setR75_moreThan3YearsTo5Years(BigDecimal r75_moreThan3YearsTo5Years) {
			this.r75_moreThan3YearsTo5Years = r75_moreThan3YearsTo5Years;
		}

		public BigDecimal getR75_moreThan5YearsTo10Years() {
			return r75_moreThan5YearsTo10Years;
		}

		public void setR75_moreThan5YearsTo10Years(BigDecimal r75_moreThan5YearsTo10Years) {
			this.r75_moreThan5YearsTo10Years = r75_moreThan5YearsTo10Years;
		}

		public BigDecimal getR75_moreThan10Years() {
			return r75_moreThan10Years;
		}

		public void setR75_moreThan10Years(BigDecimal r75_moreThan10Years) {
			this.r75_moreThan10Years = r75_moreThan10Years;
		}

		public BigDecimal getR75_total() {
			return r75_total;
		}

		public void setR75_total(BigDecimal r75_total) {
			this.r75_total = r75_total;
		}

		public String getR76product() {
			return r76product;
		}

		public void setR76product(String r76product) {
			this.r76product = r76product;
		}

		public BigDecimal getR76_upTo1Month() {
			return r76_upTo1Month;
		}

		public void setR76_upTo1Month(BigDecimal r76_upTo1Month) {
			this.r76_upTo1Month = r76_upTo1Month;
		}

		public BigDecimal getR76_moreThan1MonthTo3Months() {
			return r76_moreThan1MonthTo3Months;
		}

		public void setR76_moreThan1MonthTo3Months(BigDecimal r76_moreThan1MonthTo3Months) {
			this.r76_moreThan1MonthTo3Months = r76_moreThan1MonthTo3Months;
		}

		public BigDecimal getR76_moreThan3MonthTo6Months() {
			return r76_moreThan3MonthTo6Months;
		}

		public void setR76_moreThan3MonthTo6Months(BigDecimal r76_moreThan3MonthTo6Months) {
			this.r76_moreThan3MonthTo6Months = r76_moreThan3MonthTo6Months;
		}

		public BigDecimal getR76_moreThan6MonthTo12Months() {
			return r76_moreThan6MonthTo12Months;
		}

		public void setR76_moreThan6MonthTo12Months(BigDecimal r76_moreThan6MonthTo12Months) {
			this.r76_moreThan6MonthTo12Months = r76_moreThan6MonthTo12Months;
		}

		public BigDecimal getR76_moreThan12MonthTo3Years() {
			return r76_moreThan12MonthTo3Years;
		}

		public void setR76_moreThan12MonthTo3Years(BigDecimal r76_moreThan12MonthTo3Years) {
			this.r76_moreThan12MonthTo3Years = r76_moreThan12MonthTo3Years;
		}

		public BigDecimal getR76_moreThan3YearsTo5Years() {
			return r76_moreThan3YearsTo5Years;
		}

		public void setR76_moreThan3YearsTo5Years(BigDecimal r76_moreThan3YearsTo5Years) {
			this.r76_moreThan3YearsTo5Years = r76_moreThan3YearsTo5Years;
		}

		public BigDecimal getR76_moreThan5YearsTo10Years() {
			return r76_moreThan5YearsTo10Years;
		}

		public void setR76_moreThan5YearsTo10Years(BigDecimal r76_moreThan5YearsTo10Years) {
			this.r76_moreThan5YearsTo10Years = r76_moreThan5YearsTo10Years;
		}

		public BigDecimal getR76_moreThan10Years() {
			return r76_moreThan10Years;
		}

		public void setR76_moreThan10Years(BigDecimal r76_moreThan10Years) {
			this.r76_moreThan10Years = r76_moreThan10Years;
		}

		public BigDecimal getR76_total() {
			return r76_total;
		}

		public void setR76_total(BigDecimal r76_total) {
			this.r76_total = r76_total;
		}

		public String getR77product() {
			return r77product;
		}

		public void setR77product(String r77product) {
			this.r77product = r77product;
		}

		public BigDecimal getR77_upTo1Month() {
			return r77_upTo1Month;
		}

		public void setR77_upTo1Month(BigDecimal r77_upTo1Month) {
			this.r77_upTo1Month = r77_upTo1Month;
		}

		public BigDecimal getR77_moreThan1MonthTo3Months() {
			return r77_moreThan1MonthTo3Months;
		}

		public void setR77_moreThan1MonthTo3Months(BigDecimal r77_moreThan1MonthTo3Months) {
			this.r77_moreThan1MonthTo3Months = r77_moreThan1MonthTo3Months;
		}

		public BigDecimal getR77_moreThan3MonthTo6Months() {
			return r77_moreThan3MonthTo6Months;
		}

		public void setR77_moreThan3MonthTo6Months(BigDecimal r77_moreThan3MonthTo6Months) {
			this.r77_moreThan3MonthTo6Months = r77_moreThan3MonthTo6Months;
		}

		public BigDecimal getR77_moreThan6MonthTo12Months() {
			return r77_moreThan6MonthTo12Months;
		}

		public void setR77_moreThan6MonthTo12Months(BigDecimal r77_moreThan6MonthTo12Months) {
			this.r77_moreThan6MonthTo12Months = r77_moreThan6MonthTo12Months;
		}

		public BigDecimal getR77_moreThan12MonthTo3Years() {
			return r77_moreThan12MonthTo3Years;
		}

		public void setR77_moreThan12MonthTo3Years(BigDecimal r77_moreThan12MonthTo3Years) {
			this.r77_moreThan12MonthTo3Years = r77_moreThan12MonthTo3Years;
		}

		public BigDecimal getR77_moreThan3YearsTo5Years() {
			return r77_moreThan3YearsTo5Years;
		}

		public void setR77_moreThan3YearsTo5Years(BigDecimal r77_moreThan3YearsTo5Years) {
			this.r77_moreThan3YearsTo5Years = r77_moreThan3YearsTo5Years;
		}

		public BigDecimal getR77_moreThan5YearsTo10Years() {
			return r77_moreThan5YearsTo10Years;
		}

		public void setR77_moreThan5YearsTo10Years(BigDecimal r77_moreThan5YearsTo10Years) {
			this.r77_moreThan5YearsTo10Years = r77_moreThan5YearsTo10Years;
		}

		public BigDecimal getR77_moreThan10Years() {
			return r77_moreThan10Years;
		}

		public void setR77_moreThan10Years(BigDecimal r77_moreThan10Years) {
			this.r77_moreThan10Years = r77_moreThan10Years;
		}

		public BigDecimal getR77_total() {
			return r77_total;
		}

		public void setR77_total(BigDecimal r77_total) {
			this.r77_total = r77_total;
		}

		public String getR78product() {
			return r78product;
		}

		public void setR78product(String r78product) {
			this.r78product = r78product;
		}

		public BigDecimal getR78_upTo1Month() {
			return r78_upTo1Month;
		}

		public void setR78_upTo1Month(BigDecimal r78_upTo1Month) {
			this.r78_upTo1Month = r78_upTo1Month;
		}

		public BigDecimal getR78_moreThan1MonthTo3Months() {
			return r78_moreThan1MonthTo3Months;
		}

		public void setR78_moreThan1MonthTo3Months(BigDecimal r78_moreThan1MonthTo3Months) {
			this.r78_moreThan1MonthTo3Months = r78_moreThan1MonthTo3Months;
		}

		public BigDecimal getR78_moreThan3MonthTo6Months() {
			return r78_moreThan3MonthTo6Months;
		}

		public void setR78_moreThan3MonthTo6Months(BigDecimal r78_moreThan3MonthTo6Months) {
			this.r78_moreThan3MonthTo6Months = r78_moreThan3MonthTo6Months;
		}

		public BigDecimal getR78_moreThan6MonthTo12Months() {
			return r78_moreThan6MonthTo12Months;
		}

		public void setR78_moreThan6MonthTo12Months(BigDecimal r78_moreThan6MonthTo12Months) {
			this.r78_moreThan6MonthTo12Months = r78_moreThan6MonthTo12Months;
		}

		public BigDecimal getR78_moreThan12MonthTo3Years() {
			return r78_moreThan12MonthTo3Years;
		}

		public void setR78_moreThan12MonthTo3Years(BigDecimal r78_moreThan12MonthTo3Years) {
			this.r78_moreThan12MonthTo3Years = r78_moreThan12MonthTo3Years;
		}

		public BigDecimal getR78_moreThan3YearsTo5Years() {
			return r78_moreThan3YearsTo5Years;
		}

		public void setR78_moreThan3YearsTo5Years(BigDecimal r78_moreThan3YearsTo5Years) {
			this.r78_moreThan3YearsTo5Years = r78_moreThan3YearsTo5Years;
		}

		public BigDecimal getR78_moreThan5YearsTo10Years() {
			return r78_moreThan5YearsTo10Years;
		}

		public void setR78_moreThan5YearsTo10Years(BigDecimal r78_moreThan5YearsTo10Years) {
			this.r78_moreThan5YearsTo10Years = r78_moreThan5YearsTo10Years;
		}

		public BigDecimal getR78_moreThan10Years() {
			return r78_moreThan10Years;
		}

		public void setR78_moreThan10Years(BigDecimal r78_moreThan10Years) {
			this.r78_moreThan10Years = r78_moreThan10Years;
		}

		public BigDecimal getR78_total() {
			return r78_total;
		}

		public void setR78_total(BigDecimal r78_total) {
			this.r78_total = r78_total;
		}

		public String getR79product() {
			return r79product;
		}

		public void setR79product(String r79product) {
			this.r79product = r79product;
		}

		public BigDecimal getR79_upTo1Month() {
			return r79_upTo1Month;
		}

		public void setR79_upTo1Month(BigDecimal r79_upTo1Month) {
			this.r79_upTo1Month = r79_upTo1Month;
		}

		public BigDecimal getR79_moreThan1MonthTo3Months() {
			return r79_moreThan1MonthTo3Months;
		}

		public void setR79_moreThan1MonthTo3Months(BigDecimal r79_moreThan1MonthTo3Months) {
			this.r79_moreThan1MonthTo3Months = r79_moreThan1MonthTo3Months;
		}

		public BigDecimal getR79_moreThan3MonthTo6Months() {
			return r79_moreThan3MonthTo6Months;
		}

		public void setR79_moreThan3MonthTo6Months(BigDecimal r79_moreThan3MonthTo6Months) {
			this.r79_moreThan3MonthTo6Months = r79_moreThan3MonthTo6Months;
		}

		public BigDecimal getR79_moreThan6MonthTo12Months() {
			return r79_moreThan6MonthTo12Months;
		}

		public void setR79_moreThan6MonthTo12Months(BigDecimal r79_moreThan6MonthTo12Months) {
			this.r79_moreThan6MonthTo12Months = r79_moreThan6MonthTo12Months;
		}

		public BigDecimal getR79_moreThan12MonthTo3Years() {
			return r79_moreThan12MonthTo3Years;
		}

		public void setR79_moreThan12MonthTo3Years(BigDecimal r79_moreThan12MonthTo3Years) {
			this.r79_moreThan12MonthTo3Years = r79_moreThan12MonthTo3Years;
		}

		public BigDecimal getR79_moreThan3YearsTo5Years() {
			return r79_moreThan3YearsTo5Years;
		}

		public void setR79_moreThan3YearsTo5Years(BigDecimal r79_moreThan3YearsTo5Years) {
			this.r79_moreThan3YearsTo5Years = r79_moreThan3YearsTo5Years;
		}

		public BigDecimal getR79_moreThan5YearsTo10Years() {
			return r79_moreThan5YearsTo10Years;
		}

		public void setR79_moreThan5YearsTo10Years(BigDecimal r79_moreThan5YearsTo10Years) {
			this.r79_moreThan5YearsTo10Years = r79_moreThan5YearsTo10Years;
		}

		public BigDecimal getR79_moreThan10Years() {
			return r79_moreThan10Years;
		}

		public void setR79_moreThan10Years(BigDecimal r79_moreThan10Years) {
			this.r79_moreThan10Years = r79_moreThan10Years;
		}

		public BigDecimal getR79_total() {
			return r79_total;
		}

		public void setR79_total(BigDecimal r79_total) {
			this.r79_total = r79_total;
		}

		public String getR80product() {
			return r80product;
		}

		public void setR80product(String r80product) {
			this.r80product = r80product;
		}

		public BigDecimal getR80_upTo1Month() {
			return r80_upTo1Month;
		}

		public void setR80_upTo1Month(BigDecimal r80_upTo1Month) {
			this.r80_upTo1Month = r80_upTo1Month;
		}

		public BigDecimal getR80_moreThan1MonthTo3Months() {
			return r80_moreThan1MonthTo3Months;
		}

		public void setR80_moreThan1MonthTo3Months(BigDecimal r80_moreThan1MonthTo3Months) {
			this.r80_moreThan1MonthTo3Months = r80_moreThan1MonthTo3Months;
		}

		public BigDecimal getR80_moreThan3MonthTo6Months() {
			return r80_moreThan3MonthTo6Months;
		}

		public void setR80_moreThan3MonthTo6Months(BigDecimal r80_moreThan3MonthTo6Months) {
			this.r80_moreThan3MonthTo6Months = r80_moreThan3MonthTo6Months;
		}

		public BigDecimal getR80_moreThan6MonthTo12Months() {
			return r80_moreThan6MonthTo12Months;
		}

		public void setR80_moreThan6MonthTo12Months(BigDecimal r80_moreThan6MonthTo12Months) {
			this.r80_moreThan6MonthTo12Months = r80_moreThan6MonthTo12Months;
		}

		public BigDecimal getR80_moreThan12MonthTo3Years() {
			return r80_moreThan12MonthTo3Years;
		}

		public void setR80_moreThan12MonthTo3Years(BigDecimal r80_moreThan12MonthTo3Years) {
			this.r80_moreThan12MonthTo3Years = r80_moreThan12MonthTo3Years;
		}

		public BigDecimal getR80_moreThan3YearsTo5Years() {
			return r80_moreThan3YearsTo5Years;
		}

		public void setR80_moreThan3YearsTo5Years(BigDecimal r80_moreThan3YearsTo5Years) {
			this.r80_moreThan3YearsTo5Years = r80_moreThan3YearsTo5Years;
		}

		public BigDecimal getR80_moreThan5YearsTo10Years() {
			return r80_moreThan5YearsTo10Years;
		}

		public void setR80_moreThan5YearsTo10Years(BigDecimal r80_moreThan5YearsTo10Years) {
			this.r80_moreThan5YearsTo10Years = r80_moreThan5YearsTo10Years;
		}

		public BigDecimal getR80_moreThan10Years() {
			return r80_moreThan10Years;
		}

		public void setR80_moreThan10Years(BigDecimal r80_moreThan10Years) {
			this.r80_moreThan10Years = r80_moreThan10Years;
		}

		public BigDecimal getR80_total() {
			return r80_total;
		}

		public void setR80_total(BigDecimal r80_total) {
			this.r80_total = r80_total;
		}

		public String getR81product() {
			return r81product;
		}

		public void setR81product(String r81product) {
			this.r81product = r81product;
		}

		public BigDecimal getR81_upTo1Month() {
			return r81_upTo1Month;
		}

		public void setR81_upTo1Month(BigDecimal r81_upTo1Month) {
			this.r81_upTo1Month = r81_upTo1Month;
		}

		public BigDecimal getR81_moreThan1MonthTo3Months() {
			return r81_moreThan1MonthTo3Months;
		}

		public void setR81_moreThan1MonthTo3Months(BigDecimal r81_moreThan1MonthTo3Months) {
			this.r81_moreThan1MonthTo3Months = r81_moreThan1MonthTo3Months;
		}

		public BigDecimal getR81_moreThan3MonthTo6Months() {
			return r81_moreThan3MonthTo6Months;
		}

		public void setR81_moreThan3MonthTo6Months(BigDecimal r81_moreThan3MonthTo6Months) {
			this.r81_moreThan3MonthTo6Months = r81_moreThan3MonthTo6Months;
		}

		public BigDecimal getR81_moreThan6MonthTo12Months() {
			return r81_moreThan6MonthTo12Months;
		}

		public void setR81_moreThan6MonthTo12Months(BigDecimal r81_moreThan6MonthTo12Months) {
			this.r81_moreThan6MonthTo12Months = r81_moreThan6MonthTo12Months;
		}

		public BigDecimal getR81_moreThan12MonthTo3Years() {
			return r81_moreThan12MonthTo3Years;
		}

		public void setR81_moreThan12MonthTo3Years(BigDecimal r81_moreThan12MonthTo3Years) {
			this.r81_moreThan12MonthTo3Years = r81_moreThan12MonthTo3Years;
		}

		public BigDecimal getR81_moreThan3YearsTo5Years() {
			return r81_moreThan3YearsTo5Years;
		}

		public void setR81_moreThan3YearsTo5Years(BigDecimal r81_moreThan3YearsTo5Years) {
			this.r81_moreThan3YearsTo5Years = r81_moreThan3YearsTo5Years;
		}

		public BigDecimal getR81_moreThan5YearsTo10Years() {
			return r81_moreThan5YearsTo10Years;
		}

		public void setR81_moreThan5YearsTo10Years(BigDecimal r81_moreThan5YearsTo10Years) {
			this.r81_moreThan5YearsTo10Years = r81_moreThan5YearsTo10Years;
		}

		public BigDecimal getR81_moreThan10Years() {
			return r81_moreThan10Years;
		}

		public void setR81_moreThan10Years(BigDecimal r81_moreThan10Years) {
			this.r81_moreThan10Years = r81_moreThan10Years;
		}

		public BigDecimal getR81_total() {
			return r81_total;
		}

		public void setR81_total(BigDecimal r81_total) {
			this.r81_total = r81_total;
		}

		public String getR82product() {
			return r82product;
		}

		public void setR82product(String r82product) {
			this.r82product = r82product;
		}

		public BigDecimal getR82_upTo1Month() {
			return r82_upTo1Month;
		}

		public void setR82_upTo1Month(BigDecimal r82_upTo1Month) {
			this.r82_upTo1Month = r82_upTo1Month;
		}

		public BigDecimal getR82_moreThan1MonthTo3Months() {
			return r82_moreThan1MonthTo3Months;
		}

		public void setR82_moreThan1MonthTo3Months(BigDecimal r82_moreThan1MonthTo3Months) {
			this.r82_moreThan1MonthTo3Months = r82_moreThan1MonthTo3Months;
		}

		public BigDecimal getR82_moreThan3MonthTo6Months() {
			return r82_moreThan3MonthTo6Months;
		}

		public void setR82_moreThan3MonthTo6Months(BigDecimal r82_moreThan3MonthTo6Months) {
			this.r82_moreThan3MonthTo6Months = r82_moreThan3MonthTo6Months;
		}

		public BigDecimal getR82_moreThan6MonthTo12Months() {
			return r82_moreThan6MonthTo12Months;
		}

		public void setR82_moreThan6MonthTo12Months(BigDecimal r82_moreThan6MonthTo12Months) {
			this.r82_moreThan6MonthTo12Months = r82_moreThan6MonthTo12Months;
		}

		public BigDecimal getR82_moreThan12MonthTo3Years() {
			return r82_moreThan12MonthTo3Years;
		}

		public void setR82_moreThan12MonthTo3Years(BigDecimal r82_moreThan12MonthTo3Years) {
			this.r82_moreThan12MonthTo3Years = r82_moreThan12MonthTo3Years;
		}

		public BigDecimal getR82_moreThan3YearsTo5Years() {
			return r82_moreThan3YearsTo5Years;
		}

		public void setR82_moreThan3YearsTo5Years(BigDecimal r82_moreThan3YearsTo5Years) {
			this.r82_moreThan3YearsTo5Years = r82_moreThan3YearsTo5Years;
		}

		public BigDecimal getR82_moreThan5YearsTo10Years() {
			return r82_moreThan5YearsTo10Years;
		}

		public void setR82_moreThan5YearsTo10Years(BigDecimal r82_moreThan5YearsTo10Years) {
			this.r82_moreThan5YearsTo10Years = r82_moreThan5YearsTo10Years;
		}

		public BigDecimal getR82_moreThan10Years() {
			return r82_moreThan10Years;
		}

		public void setR82_moreThan10Years(BigDecimal r82_moreThan10Years) {
			this.r82_moreThan10Years = r82_moreThan10Years;
		}

		public BigDecimal getR82_total() {
			return r82_total;
		}

		public void setR82_total(BigDecimal r82_total) {
			this.r82_total = r82_total;
		}

		public String getR83product() {
			return r83product;
		}

		public void setR83product(String r83product) {
			this.r83product = r83product;
		}

		public BigDecimal getR83_upTo1Month() {
			return r83_upTo1Month;
		}

		public void setR83_upTo1Month(BigDecimal r83_upTo1Month) {
			this.r83_upTo1Month = r83_upTo1Month;
		}

		public BigDecimal getR83_moreThan1MonthTo3Months() {
			return r83_moreThan1MonthTo3Months;
		}

		public void setR83_moreThan1MonthTo3Months(BigDecimal r83_moreThan1MonthTo3Months) {
			this.r83_moreThan1MonthTo3Months = r83_moreThan1MonthTo3Months;
		}

		public BigDecimal getR83_moreThan3MonthTo6Months() {
			return r83_moreThan3MonthTo6Months;
		}

		public void setR83_moreThan3MonthTo6Months(BigDecimal r83_moreThan3MonthTo6Months) {
			this.r83_moreThan3MonthTo6Months = r83_moreThan3MonthTo6Months;
		}

		public BigDecimal getR83_moreThan6MonthTo12Months() {
			return r83_moreThan6MonthTo12Months;
		}

		public void setR83_moreThan6MonthTo12Months(BigDecimal r83_moreThan6MonthTo12Months) {
			this.r83_moreThan6MonthTo12Months = r83_moreThan6MonthTo12Months;
		}

		public BigDecimal getR83_moreThan12MonthTo3Years() {
			return r83_moreThan12MonthTo3Years;
		}

		public void setR83_moreThan12MonthTo3Years(BigDecimal r83_moreThan12MonthTo3Years) {
			this.r83_moreThan12MonthTo3Years = r83_moreThan12MonthTo3Years;
		}

		public BigDecimal getR83_moreThan3YearsTo5Years() {
			return r83_moreThan3YearsTo5Years;
		}

		public void setR83_moreThan3YearsTo5Years(BigDecimal r83_moreThan3YearsTo5Years) {
			this.r83_moreThan3YearsTo5Years = r83_moreThan3YearsTo5Years;
		}

		public BigDecimal getR83_moreThan5YearsTo10Years() {
			return r83_moreThan5YearsTo10Years;
		}

		public void setR83_moreThan5YearsTo10Years(BigDecimal r83_moreThan5YearsTo10Years) {
			this.r83_moreThan5YearsTo10Years = r83_moreThan5YearsTo10Years;
		}

		public BigDecimal getR83_moreThan10Years() {
			return r83_moreThan10Years;
		}

		public void setR83_moreThan10Years(BigDecimal r83_moreThan10Years) {
			this.r83_moreThan10Years = r83_moreThan10Years;
		}

		public BigDecimal getR83_total() {
			return r83_total;
		}

		public void setR83_total(BigDecimal r83_total) {
			this.r83_total = r83_total;
		}

		public BigDecimal getR84_nonRatioSensativeItems() {
			return r84_nonRatioSensativeItems;
		}

		public void setR84_nonRatioSensativeItems(BigDecimal r84_nonRatioSensativeItems) {
			this.r84_nonRatioSensativeItems = r84_nonRatioSensativeItems;
		}

		public BigDecimal getR84_total() {
			return r84_total;
		}

		public void setR84_total(BigDecimal r84_total) {
			this.r84_total = r84_total;
		}

		public BigDecimal getR85_nonRatioSensativeItems() {
			return r85_nonRatioSensativeItems;
		}

		public void setR85_nonRatioSensativeItems(BigDecimal r85_nonRatioSensativeItems) {
			this.r85_nonRatioSensativeItems = r85_nonRatioSensativeItems;
		}

		public BigDecimal getR85_total() {
			return r85_total;
		}

		public void setR85_total(BigDecimal r85_total) {
			this.r85_total = r85_total;
		}

		public BigDecimal getR86_nonRatioSensativeItems() {
			return r86_nonRatioSensativeItems;
		}

		public void setR86_nonRatioSensativeItems(BigDecimal r86_nonRatioSensativeItems) {
			this.r86_nonRatioSensativeItems = r86_nonRatioSensativeItems;
		}

		public BigDecimal getR86_total() {
			return r86_total;
		}

		public void setR86_total(BigDecimal r86_total) {
			this.r86_total = r86_total;
		}

		public BigDecimal getR87_nonRatioSensativeItems() {
			return r87_nonRatioSensativeItems;
		}

		public void setR87_nonRatioSensativeItems(BigDecimal r87_nonRatioSensativeItems) {
			this.r87_nonRatioSensativeItems = r87_nonRatioSensativeItems;
		}

		public BigDecimal getR87_total() {
			return r87_total;
		}

		public void setR87_total(BigDecimal r87_total) {
			this.r87_total = r87_total;
		}

		public BigDecimal getR88_nonRatioSensativeItems() {
			return r88_nonRatioSensativeItems;
		}

		public void setR88_nonRatioSensativeItems(BigDecimal r88_nonRatioSensativeItems) {
			this.r88_nonRatioSensativeItems = r88_nonRatioSensativeItems;
		}

		public BigDecimal getR88_total() {
			return r88_total;
		}

		public void setR88_total(BigDecimal r88_total) {
			this.r88_total = r88_total;
		}

		public BigDecimal getR89_nonRatioSensativeItems() {
			return r89_nonRatioSensativeItems;
		}

		public void setR89_nonRatioSensativeItems(BigDecimal r89_nonRatioSensativeItems) {
			this.r89_nonRatioSensativeItems = r89_nonRatioSensativeItems;
		}

		public BigDecimal getR89_total() {
			return r89_total;
		}

		public void setR89_total(BigDecimal r89_total) {
			this.r89_total = r89_total;
		}

		public BigDecimal getR90_nonRatioSensativeItems() {
			return r90_nonRatioSensativeItems;
		}

		public void setR90_nonRatioSensativeItems(BigDecimal r90_nonRatioSensativeItems) {
			this.r90_nonRatioSensativeItems = r90_nonRatioSensativeItems;
		}

		public BigDecimal getR90_total() {
			return r90_total;
		}

		public void setR90_total(BigDecimal r90_total) {
			this.r90_total = r90_total;
		}

		public BigDecimal getR91_nonRatioSensativeItems() {
			return r91_nonRatioSensativeItems;
		}

		public void setR91_nonRatioSensativeItems(BigDecimal r91_nonRatioSensativeItems) {
			this.r91_nonRatioSensativeItems = r91_nonRatioSensativeItems;
		}

		public BigDecimal getR91_total() {
			return r91_total;
		}

		public void setR91_total(BigDecimal r91_total) {
			this.r91_total = r91_total;
		}

		public String getR92product() {
			return r92product;
		}

		public void setR92product(String r92product) {
			this.r92product = r92product;
		}

		public BigDecimal getR92_upTo1Month() {
			return r92_upTo1Month;
		}

		public void setR92_upTo1Month(BigDecimal r92_upTo1Month) {
			this.r92_upTo1Month = r92_upTo1Month;
		}

		public BigDecimal getR92_moreThan1MonthTo3Months() {
			return r92_moreThan1MonthTo3Months;
		}

		public void setR92_moreThan1MonthTo3Months(BigDecimal r92_moreThan1MonthTo3Months) {
			this.r92_moreThan1MonthTo3Months = r92_moreThan1MonthTo3Months;
		}

		public BigDecimal getR92_moreThan3MonthTo6Months() {
			return r92_moreThan3MonthTo6Months;
		}

		public void setR92_moreThan3MonthTo6Months(BigDecimal r92_moreThan3MonthTo6Months) {
			this.r92_moreThan3MonthTo6Months = r92_moreThan3MonthTo6Months;
		}

		public BigDecimal getR92_moreThan6MonthTo12Months() {
			return r92_moreThan6MonthTo12Months;
		}

		public void setR92_moreThan6MonthTo12Months(BigDecimal r92_moreThan6MonthTo12Months) {
			this.r92_moreThan6MonthTo12Months = r92_moreThan6MonthTo12Months;
		}

		public BigDecimal getR92_moreThan12MonthTo3Years() {
			return r92_moreThan12MonthTo3Years;
		}

		public void setR92_moreThan12MonthTo3Years(BigDecimal r92_moreThan12MonthTo3Years) {
			this.r92_moreThan12MonthTo3Years = r92_moreThan12MonthTo3Years;
		}

		public BigDecimal getR92_moreThan3YearsTo5Years() {
			return r92_moreThan3YearsTo5Years;
		}

		public void setR92_moreThan3YearsTo5Years(BigDecimal r92_moreThan3YearsTo5Years) {
			this.r92_moreThan3YearsTo5Years = r92_moreThan3YearsTo5Years;
		}

		public BigDecimal getR92_moreThan5YearsTo10Years() {
			return r92_moreThan5YearsTo10Years;
		}

		public void setR92_moreThan5YearsTo10Years(BigDecimal r92_moreThan5YearsTo10Years) {
			this.r92_moreThan5YearsTo10Years = r92_moreThan5YearsTo10Years;
		}

		public BigDecimal getR92_moreThan10Years() {
			return r92_moreThan10Years;
		}

		public void setR92_moreThan10Years(BigDecimal r92_moreThan10Years) {
			this.r92_moreThan10Years = r92_moreThan10Years;
		}

		public BigDecimal getR92_total() {
			return r92_total;
		}

		public void setR92_total(BigDecimal r92_total) {
			this.r92_total = r92_total;
		}

		public String getR93product() {
			return r93product;
		}

		public void setR93product(String r93product) {
			this.r93product = r93product;
		}

		public BigDecimal getR93_upTo1Month() {
			return r93_upTo1Month;
		}

		public void setR93_upTo1Month(BigDecimal r93_upTo1Month) {
			this.r93_upTo1Month = r93_upTo1Month;
		}

		public BigDecimal getR93_moreThan1MonthTo3Months() {
			return r93_moreThan1MonthTo3Months;
		}

		public void setR93_moreThan1MonthTo3Months(BigDecimal r93_moreThan1MonthTo3Months) {
			this.r93_moreThan1MonthTo3Months = r93_moreThan1MonthTo3Months;
		}

		public BigDecimal getR93_moreThan3MonthTo6Months() {
			return r93_moreThan3MonthTo6Months;
		}

		public void setR93_moreThan3MonthTo6Months(BigDecimal r93_moreThan3MonthTo6Months) {
			this.r93_moreThan3MonthTo6Months = r93_moreThan3MonthTo6Months;
		}

		public BigDecimal getR93_moreThan6MonthTo12Months() {
			return r93_moreThan6MonthTo12Months;
		}

		public void setR93_moreThan6MonthTo12Months(BigDecimal r93_moreThan6MonthTo12Months) {
			this.r93_moreThan6MonthTo12Months = r93_moreThan6MonthTo12Months;
		}

		public BigDecimal getR93_moreThan12MonthTo3Years() {
			return r93_moreThan12MonthTo3Years;
		}

		public void setR93_moreThan12MonthTo3Years(BigDecimal r93_moreThan12MonthTo3Years) {
			this.r93_moreThan12MonthTo3Years = r93_moreThan12MonthTo3Years;
		}

		public BigDecimal getR93_moreThan3YearsTo5Years() {
			return r93_moreThan3YearsTo5Years;
		}

		public void setR93_moreThan3YearsTo5Years(BigDecimal r93_moreThan3YearsTo5Years) {
			this.r93_moreThan3YearsTo5Years = r93_moreThan3YearsTo5Years;
		}

		public BigDecimal getR93_moreThan5YearsTo10Years() {
			return r93_moreThan5YearsTo10Years;
		}

		public void setR93_moreThan5YearsTo10Years(BigDecimal r93_moreThan5YearsTo10Years) {
			this.r93_moreThan5YearsTo10Years = r93_moreThan5YearsTo10Years;
		}

		public BigDecimal getR93_moreThan10Years() {
			return r93_moreThan10Years;
		}

		public void setR93_moreThan10Years(BigDecimal r93_moreThan10Years) {
			this.r93_moreThan10Years = r93_moreThan10Years;
		}

		public BigDecimal getR93_total() {
			return r93_total;
		}

		public void setR93_total(BigDecimal r93_total) {
			this.r93_total = r93_total;
		}

		public String getR94product() {
			return r94product;
		}

		public void setR94product(String r94product) {
			this.r94product = r94product;
		}

		public BigDecimal getR94_upTo1Month() {
			return r94_upTo1Month;
		}

		public void setR94_upTo1Month(BigDecimal r94_upTo1Month) {
			this.r94_upTo1Month = r94_upTo1Month;
		}

		public BigDecimal getR94_moreThan1MonthTo3Months() {
			return r94_moreThan1MonthTo3Months;
		}

		public void setR94_moreThan1MonthTo3Months(BigDecimal r94_moreThan1MonthTo3Months) {
			this.r94_moreThan1MonthTo3Months = r94_moreThan1MonthTo3Months;
		}

		public BigDecimal getR94_moreThan3MonthTo6Months() {
			return r94_moreThan3MonthTo6Months;
		}

		public void setR94_moreThan3MonthTo6Months(BigDecimal r94_moreThan3MonthTo6Months) {
			this.r94_moreThan3MonthTo6Months = r94_moreThan3MonthTo6Months;
		}

		public BigDecimal getR94_moreThan6MonthTo12Months() {
			return r94_moreThan6MonthTo12Months;
		}

		public void setR94_moreThan6MonthTo12Months(BigDecimal r94_moreThan6MonthTo12Months) {
			this.r94_moreThan6MonthTo12Months = r94_moreThan6MonthTo12Months;
		}

		public BigDecimal getR94_moreThan12MonthTo3Years() {
			return r94_moreThan12MonthTo3Years;
		}

		public void setR94_moreThan12MonthTo3Years(BigDecimal r94_moreThan12MonthTo3Years) {
			this.r94_moreThan12MonthTo3Years = r94_moreThan12MonthTo3Years;
		}

		public BigDecimal getR94_moreThan3YearsTo5Years() {
			return r94_moreThan3YearsTo5Years;
		}

		public void setR94_moreThan3YearsTo5Years(BigDecimal r94_moreThan3YearsTo5Years) {
			this.r94_moreThan3YearsTo5Years = r94_moreThan3YearsTo5Years;
		}

		public BigDecimal getR94_moreThan5YearsTo10Years() {
			return r94_moreThan5YearsTo10Years;
		}

		public void setR94_moreThan5YearsTo10Years(BigDecimal r94_moreThan5YearsTo10Years) {
			this.r94_moreThan5YearsTo10Years = r94_moreThan5YearsTo10Years;
		}

		public BigDecimal getR94_moreThan10Years() {
			return r94_moreThan10Years;
		}

		public void setR94_moreThan10Years(BigDecimal r94_moreThan10Years) {
			this.r94_moreThan10Years = r94_moreThan10Years;
		}

		public BigDecimal getR94_total() {
			return r94_total;
		}

		public void setR94_total(BigDecimal r94_total) {
			this.r94_total = r94_total;
		}

		public BigDecimal getR95_nonRatioSensativeItems() {
			return r95_nonRatioSensativeItems;
		}

		public void setR95_nonRatioSensativeItems(BigDecimal r95_nonRatioSensativeItems) {
			this.r95_nonRatioSensativeItems = r95_nonRatioSensativeItems;
		}

		public BigDecimal getR95_total() {
			return r95_total;
		}

		public void setR95_total(BigDecimal r95_total) {
			this.r95_total = r95_total;
		}

		public String getR96product() {
			return r96product;
		}

		public void setR96product(String r96product) {
			this.r96product = r96product;
		}

		public BigDecimal getR96_upTo1Month() {
			return r96_upTo1Month;
		}

		public void setR96_upTo1Month(BigDecimal r96_upTo1Month) {
			this.r96_upTo1Month = r96_upTo1Month;
		}

		public BigDecimal getR96_moreThan1MonthTo3Months() {
			return r96_moreThan1MonthTo3Months;
		}

		public void setR96_moreThan1MonthTo3Months(BigDecimal r96_moreThan1MonthTo3Months) {
			this.r96_moreThan1MonthTo3Months = r96_moreThan1MonthTo3Months;
		}

		public BigDecimal getR96_moreThan3MonthTo6Months() {
			return r96_moreThan3MonthTo6Months;
		}

		public void setR96_moreThan3MonthTo6Months(BigDecimal r96_moreThan3MonthTo6Months) {
			this.r96_moreThan3MonthTo6Months = r96_moreThan3MonthTo6Months;
		}

		public BigDecimal getR96_moreThan6MonthTo12Months() {
			return r96_moreThan6MonthTo12Months;
		}

		public void setR96_moreThan6MonthTo12Months(BigDecimal r96_moreThan6MonthTo12Months) {
			this.r96_moreThan6MonthTo12Months = r96_moreThan6MonthTo12Months;
		}

		public BigDecimal getR96_moreThan12MonthTo3Years() {
			return r96_moreThan12MonthTo3Years;
		}

		public void setR96_moreThan12MonthTo3Years(BigDecimal r96_moreThan12MonthTo3Years) {
			this.r96_moreThan12MonthTo3Years = r96_moreThan12MonthTo3Years;
		}

		public BigDecimal getR96_moreThan3YearsTo5Years() {
			return r96_moreThan3YearsTo5Years;
		}

		public void setR96_moreThan3YearsTo5Years(BigDecimal r96_moreThan3YearsTo5Years) {
			this.r96_moreThan3YearsTo5Years = r96_moreThan3YearsTo5Years;
		}

		public BigDecimal getR96_moreThan5YearsTo10Years() {
			return r96_moreThan5YearsTo10Years;
		}

		public void setR96_moreThan5YearsTo10Years(BigDecimal r96_moreThan5YearsTo10Years) {
			this.r96_moreThan5YearsTo10Years = r96_moreThan5YearsTo10Years;
		}

		public BigDecimal getR96_moreThan10Years() {
			return r96_moreThan10Years;
		}

		public void setR96_moreThan10Years(BigDecimal r96_moreThan10Years) {
			this.r96_moreThan10Years = r96_moreThan10Years;
		}

		public BigDecimal getR96_nonRatioSensativeItems() {
			return r96_nonRatioSensativeItems;
		}

		public void setR96_nonRatioSensativeItems(BigDecimal r96_nonRatioSensativeItems) {
			this.r96_nonRatioSensativeItems = r96_nonRatioSensativeItems;
		}

		public BigDecimal getR96_total() {
			return r96_total;
		}

		public void setR96_total(BigDecimal r96_total) {
			this.r96_total = r96_total;
		}

		public String getR97product() {
			return r97product;
		}

		public void setR97product(String r97product) {
			this.r97product = r97product;
		}

		public BigDecimal getR97_upTo1Month() {
			return r97_upTo1Month;
		}

		public void setR97_upTo1Month(BigDecimal r97_upTo1Month) {
			this.r97_upTo1Month = r97_upTo1Month;
		}

		public BigDecimal getR97_moreThan1MonthTo3Months() {
			return r97_moreThan1MonthTo3Months;
		}

		public void setR97_moreThan1MonthTo3Months(BigDecimal r97_moreThan1MonthTo3Months) {
			this.r97_moreThan1MonthTo3Months = r97_moreThan1MonthTo3Months;
		}

		public BigDecimal getR97_moreThan3MonthTo6Months() {
			return r97_moreThan3MonthTo6Months;
		}

		public void setR97_moreThan3MonthTo6Months(BigDecimal r97_moreThan3MonthTo6Months) {
			this.r97_moreThan3MonthTo6Months = r97_moreThan3MonthTo6Months;
		}

		public BigDecimal getR97_moreThan6MonthTo12Months() {
			return r97_moreThan6MonthTo12Months;
		}

		public void setR97_moreThan6MonthTo12Months(BigDecimal r97_moreThan6MonthTo12Months) {
			this.r97_moreThan6MonthTo12Months = r97_moreThan6MonthTo12Months;
		}

		public BigDecimal getR97_moreThan12MonthTo3Years() {
			return r97_moreThan12MonthTo3Years;
		}

		public void setR97_moreThan12MonthTo3Years(BigDecimal r97_moreThan12MonthTo3Years) {
			this.r97_moreThan12MonthTo3Years = r97_moreThan12MonthTo3Years;
		}

		public BigDecimal getR97_moreThan3YearsTo5Years() {
			return r97_moreThan3YearsTo5Years;
		}

		public void setR97_moreThan3YearsTo5Years(BigDecimal r97_moreThan3YearsTo5Years) {
			this.r97_moreThan3YearsTo5Years = r97_moreThan3YearsTo5Years;
		}

		public BigDecimal getR97_moreThan5YearsTo10Years() {
			return r97_moreThan5YearsTo10Years;
		}

		public void setR97_moreThan5YearsTo10Years(BigDecimal r97_moreThan5YearsTo10Years) {
			this.r97_moreThan5YearsTo10Years = r97_moreThan5YearsTo10Years;
		}

		public BigDecimal getR97_moreThan10Years() {
			return r97_moreThan10Years;
		}

		public void setR97_moreThan10Years(BigDecimal r97_moreThan10Years) {
			this.r97_moreThan10Years = r97_moreThan10Years;
		}

		public BigDecimal getR97_nonRatioSensativeItems() {
			return r97_nonRatioSensativeItems;
		}

		public void setR97_nonRatioSensativeItems(BigDecimal r97_nonRatioSensativeItems) {
			this.r97_nonRatioSensativeItems = r97_nonRatioSensativeItems;
		}

		public BigDecimal getR97_total() {
			return r97_total;
		}

		public void setR97_total(BigDecimal r97_total) {
			this.r97_total = r97_total;
		}

		public String getR98product() {
			return r98product;
		}

		public void setR98product(String r98product) {
			this.r98product = r98product;
		}

		public BigDecimal getR98_upTo1Month() {
			return r98_upTo1Month;
		}

		public void setR98_upTo1Month(BigDecimal r98_upTo1Month) {
			this.r98_upTo1Month = r98_upTo1Month;
		}

		public BigDecimal getR98_moreThan1MonthTo3Months() {
			return r98_moreThan1MonthTo3Months;
		}

		public void setR98_moreThan1MonthTo3Months(BigDecimal r98_moreThan1MonthTo3Months) {
			this.r98_moreThan1MonthTo3Months = r98_moreThan1MonthTo3Months;
		}

		public BigDecimal getR98_moreThan3MonthTo6Months() {
			return r98_moreThan3MonthTo6Months;
		}

		public void setR98_moreThan3MonthTo6Months(BigDecimal r98_moreThan3MonthTo6Months) {
			this.r98_moreThan3MonthTo6Months = r98_moreThan3MonthTo6Months;
		}

		public BigDecimal getR98_moreThan6MonthTo12Months() {
			return r98_moreThan6MonthTo12Months;
		}

		public void setR98_moreThan6MonthTo12Months(BigDecimal r98_moreThan6MonthTo12Months) {
			this.r98_moreThan6MonthTo12Months = r98_moreThan6MonthTo12Months;
		}

		public BigDecimal getR98_moreThan12MonthTo3Years() {
			return r98_moreThan12MonthTo3Years;
		}

		public void setR98_moreThan12MonthTo3Years(BigDecimal r98_moreThan12MonthTo3Years) {
			this.r98_moreThan12MonthTo3Years = r98_moreThan12MonthTo3Years;
		}

		public BigDecimal getR98_moreThan3YearsTo5Years() {
			return r98_moreThan3YearsTo5Years;
		}

		public void setR98_moreThan3YearsTo5Years(BigDecimal r98_moreThan3YearsTo5Years) {
			this.r98_moreThan3YearsTo5Years = r98_moreThan3YearsTo5Years;
		}

		public BigDecimal getR98_moreThan5YearsTo10Years() {
			return r98_moreThan5YearsTo10Years;
		}

		public void setR98_moreThan5YearsTo10Years(BigDecimal r98_moreThan5YearsTo10Years) {
			this.r98_moreThan5YearsTo10Years = r98_moreThan5YearsTo10Years;
		}

		public BigDecimal getR98_moreThan10Years() {
			return r98_moreThan10Years;
		}

		public void setR98_moreThan10Years(BigDecimal r98_moreThan10Years) {
			this.r98_moreThan10Years = r98_moreThan10Years;
		}

		public BigDecimal getR98_nonRatioSensativeItems() {
			return r98_nonRatioSensativeItems;
		}

		public void setR98_nonRatioSensativeItems(BigDecimal r98_nonRatioSensativeItems) {
			this.r98_nonRatioSensativeItems = r98_nonRatioSensativeItems;
		}

		public BigDecimal getR98_total() {
			return r98_total;
		}

		public void setR98_total(BigDecimal r98_total) {
			this.r98_total = r98_total;
		}

		public String getR99product() {
			return r99product;
		}

		public void setR99product(String r99product) {
			this.r99product = r99product;
		}

		public BigDecimal getR99_upTo1Month() {
			return r99_upTo1Month;
		}

		public void setR99_upTo1Month(BigDecimal r99_upTo1Month) {
			this.r99_upTo1Month = r99_upTo1Month;
		}

		public BigDecimal getR99_moreThan1MonthTo3Months() {
			return r99_moreThan1MonthTo3Months;
		}

		public void setR99_moreThan1MonthTo3Months(BigDecimal r99_moreThan1MonthTo3Months) {
			this.r99_moreThan1MonthTo3Months = r99_moreThan1MonthTo3Months;
		}

		public BigDecimal getR99_moreThan3MonthTo6Months() {
			return r99_moreThan3MonthTo6Months;
		}

		public void setR99_moreThan3MonthTo6Months(BigDecimal r99_moreThan3MonthTo6Months) {
			this.r99_moreThan3MonthTo6Months = r99_moreThan3MonthTo6Months;
		}

		public BigDecimal getR99_moreThan6MonthTo12Months() {
			return r99_moreThan6MonthTo12Months;
		}

		public void setR99_moreThan6MonthTo12Months(BigDecimal r99_moreThan6MonthTo12Months) {
			this.r99_moreThan6MonthTo12Months = r99_moreThan6MonthTo12Months;
		}

		public BigDecimal getR99_moreThan12MonthTo3Years() {
			return r99_moreThan12MonthTo3Years;
		}

		public void setR99_moreThan12MonthTo3Years(BigDecimal r99_moreThan12MonthTo3Years) {
			this.r99_moreThan12MonthTo3Years = r99_moreThan12MonthTo3Years;
		}

		public BigDecimal getR99_moreThan3YearsTo5Years() {
			return r99_moreThan3YearsTo5Years;
		}

		public void setR99_moreThan3YearsTo5Years(BigDecimal r99_moreThan3YearsTo5Years) {
			this.r99_moreThan3YearsTo5Years = r99_moreThan3YearsTo5Years;
		}

		public BigDecimal getR99_moreThan5YearsTo10Years() {
			return r99_moreThan5YearsTo10Years;
		}

		public void setR99_moreThan5YearsTo10Years(BigDecimal r99_moreThan5YearsTo10Years) {
			this.r99_moreThan5YearsTo10Years = r99_moreThan5YearsTo10Years;
		}

		public BigDecimal getR99_moreThan10Years() {
			return r99_moreThan10Years;
		}

		public void setR99_moreThan10Years(BigDecimal r99_moreThan10Years) {
			this.r99_moreThan10Years = r99_moreThan10Years;
		}

		public BigDecimal getR99_nonRatioSensativeItems() {
			return r99_nonRatioSensativeItems;
		}

		public void setR99_nonRatioSensativeItems(BigDecimal r99_nonRatioSensativeItems) {
			this.r99_nonRatioSensativeItems = r99_nonRatioSensativeItems;
		}

		public BigDecimal getR99_total() {
			return r99_total;
		}

		public void setR99_total(BigDecimal r99_total) {
			this.r99_total = r99_total;
		}

		public String getR100product() {
			return r100product;
		}

		public void setR100product(String r100product) {
			this.r100product = r100product;
		}

		public BigDecimal getR100_upTo1Month() {
			return r100_upTo1Month;
		}

		public void setR100_upTo1Month(BigDecimal r100_upTo1Month) {
			this.r100_upTo1Month = r100_upTo1Month;
		}

		public BigDecimal getR100_moreThan1MonthTo3Months() {
			return r100_moreThan1MonthTo3Months;
		}

		public void setR100_moreThan1MonthTo3Months(BigDecimal r100_moreThan1MonthTo3Months) {
			this.r100_moreThan1MonthTo3Months = r100_moreThan1MonthTo3Months;
		}

		public BigDecimal getR100_moreThan3MonthTo6Months() {
			return r100_moreThan3MonthTo6Months;
		}

		public void setR100_moreThan3MonthTo6Months(BigDecimal r100_moreThan3MonthTo6Months) {
			this.r100_moreThan3MonthTo6Months = r100_moreThan3MonthTo6Months;
		}

		public BigDecimal getR100_moreThan6MonthTo12Months() {
			return r100_moreThan6MonthTo12Months;
		}

		public void setR100_moreThan6MonthTo12Months(BigDecimal r100_moreThan6MonthTo12Months) {
			this.r100_moreThan6MonthTo12Months = r100_moreThan6MonthTo12Months;
		}

		public BigDecimal getR100_moreThan12MonthTo3Years() {
			return r100_moreThan12MonthTo3Years;
		}

		public void setR100_moreThan12MonthTo3Years(BigDecimal r100_moreThan12MonthTo3Years) {
			this.r100_moreThan12MonthTo3Years = r100_moreThan12MonthTo3Years;
		}

		public BigDecimal getR100_moreThan3YearsTo5Years() {
			return r100_moreThan3YearsTo5Years;
		}

		public void setR100_moreThan3YearsTo5Years(BigDecimal r100_moreThan3YearsTo5Years) {
			this.r100_moreThan3YearsTo5Years = r100_moreThan3YearsTo5Years;
		}

		public BigDecimal getR100_moreThan5YearsTo10Years() {
			return r100_moreThan5YearsTo10Years;
		}

		public void setR100_moreThan5YearsTo10Years(BigDecimal r100_moreThan5YearsTo10Years) {
			this.r100_moreThan5YearsTo10Years = r100_moreThan5YearsTo10Years;
		}

		public BigDecimal getR100_moreThan10Years() {
			return r100_moreThan10Years;
		}

		public void setR100_moreThan10Years(BigDecimal r100_moreThan10Years) {
			this.r100_moreThan10Years = r100_moreThan10Years;
		}

		public BigDecimal getR100_nonRatioSensativeItems() {
			return r100_nonRatioSensativeItems;
		}

		public void setR100_nonRatioSensativeItems(BigDecimal r100_nonRatioSensativeItems) {
			this.r100_nonRatioSensativeItems = r100_nonRatioSensativeItems;
		}

		public BigDecimal getR100_total() {
			return r100_total;
		}

		public void setR100_total(BigDecimal r100_total) {
			this.r100_total = r100_total;
		}

		public String getR101product() {
			return r101product;
		}

		public void setR101product(String r101product) {
			this.r101product = r101product;
		}

		public BigDecimal getR101_upTo1Month() {
			return r101_upTo1Month;
		}

		public void setR101_upTo1Month(BigDecimal r101_upTo1Month) {
			this.r101_upTo1Month = r101_upTo1Month;
		}

		public BigDecimal getR101_moreThan1MonthTo3Months() {
			return r101_moreThan1MonthTo3Months;
		}

		public void setR101_moreThan1MonthTo3Months(BigDecimal r101_moreThan1MonthTo3Months) {
			this.r101_moreThan1MonthTo3Months = r101_moreThan1MonthTo3Months;
		}

		public BigDecimal getR101_moreThan3MonthTo6Months() {
			return r101_moreThan3MonthTo6Months;
		}

		public void setR101_moreThan3MonthTo6Months(BigDecimal r101_moreThan3MonthTo6Months) {
			this.r101_moreThan3MonthTo6Months = r101_moreThan3MonthTo6Months;
		}

		public BigDecimal getR101_moreThan6MonthTo12Months() {
			return r101_moreThan6MonthTo12Months;
		}

		public void setR101_moreThan6MonthTo12Months(BigDecimal r101_moreThan6MonthTo12Months) {
			this.r101_moreThan6MonthTo12Months = r101_moreThan6MonthTo12Months;
		}

		public BigDecimal getR101_moreThan12MonthTo3Years() {
			return r101_moreThan12MonthTo3Years;
		}

		public void setR101_moreThan12MonthTo3Years(BigDecimal r101_moreThan12MonthTo3Years) {
			this.r101_moreThan12MonthTo3Years = r101_moreThan12MonthTo3Years;
		}

		public BigDecimal getR101_moreThan3YearsTo5Years() {
			return r101_moreThan3YearsTo5Years;
		}

		public void setR101_moreThan3YearsTo5Years(BigDecimal r101_moreThan3YearsTo5Years) {
			this.r101_moreThan3YearsTo5Years = r101_moreThan3YearsTo5Years;
		}

		public BigDecimal getR101_moreThan5YearsTo10Years() {
			return r101_moreThan5YearsTo10Years;
		}

		public void setR101_moreThan5YearsTo10Years(BigDecimal r101_moreThan5YearsTo10Years) {
			this.r101_moreThan5YearsTo10Years = r101_moreThan5YearsTo10Years;
		}

		public BigDecimal getR101_moreThan10Years() {
			return r101_moreThan10Years;
		}

		public void setR101_moreThan10Years(BigDecimal r101_moreThan10Years) {
			this.r101_moreThan10Years = r101_moreThan10Years;
		}

		public BigDecimal getR101_nonRatioSensativeItems() {
			return r101_nonRatioSensativeItems;
		}

		public void setR101_nonRatioSensativeItems(BigDecimal r101_nonRatioSensativeItems) {
			this.r101_nonRatioSensativeItems = r101_nonRatioSensativeItems;
		}

		public BigDecimal getR101_total() {
			return r101_total;
		}

		public void setR101_total(BigDecimal r101_total) {
			this.r101_total = r101_total;
		}

		public String getR102product() {
			return r102product;
		}

		public void setR102product(String r102product) {
			this.r102product = r102product;
		}

		public BigDecimal getR102_upTo1Month() {
			return r102_upTo1Month;
		}

		public void setR102_upTo1Month(BigDecimal r102_upTo1Month) {
			this.r102_upTo1Month = r102_upTo1Month;
		}

		public BigDecimal getR102_moreThan1MonthTo3Months() {
			return r102_moreThan1MonthTo3Months;
		}

		public void setR102_moreThan1MonthTo3Months(BigDecimal r102_moreThan1MonthTo3Months) {
			this.r102_moreThan1MonthTo3Months = r102_moreThan1MonthTo3Months;
		}

		public BigDecimal getR102_moreThan3MonthTo6Months() {
			return r102_moreThan3MonthTo6Months;
		}

		public void setR102_moreThan3MonthTo6Months(BigDecimal r102_moreThan3MonthTo6Months) {
			this.r102_moreThan3MonthTo6Months = r102_moreThan3MonthTo6Months;
		}

		public BigDecimal getR102_moreThan6MonthTo12Months() {
			return r102_moreThan6MonthTo12Months;
		}

		public void setR102_moreThan6MonthTo12Months(BigDecimal r102_moreThan6MonthTo12Months) {
			this.r102_moreThan6MonthTo12Months = r102_moreThan6MonthTo12Months;
		}

		public BigDecimal getR102_moreThan12MonthTo3Years() {
			return r102_moreThan12MonthTo3Years;
		}

		public void setR102_moreThan12MonthTo3Years(BigDecimal r102_moreThan12MonthTo3Years) {
			this.r102_moreThan12MonthTo3Years = r102_moreThan12MonthTo3Years;
		}

		public BigDecimal getR102_moreThan3YearsTo5Years() {
			return r102_moreThan3YearsTo5Years;
		}

		public void setR102_moreThan3YearsTo5Years(BigDecimal r102_moreThan3YearsTo5Years) {
			this.r102_moreThan3YearsTo5Years = r102_moreThan3YearsTo5Years;
		}

		public BigDecimal getR102_moreThan5YearsTo10Years() {
			return r102_moreThan5YearsTo10Years;
		}

		public void setR102_moreThan5YearsTo10Years(BigDecimal r102_moreThan5YearsTo10Years) {
			this.r102_moreThan5YearsTo10Years = r102_moreThan5YearsTo10Years;
		}

		public BigDecimal getR102_moreThan10Years() {
			return r102_moreThan10Years;
		}

		public void setR102_moreThan10Years(BigDecimal r102_moreThan10Years) {
			this.r102_moreThan10Years = r102_moreThan10Years;
		}

		public BigDecimal getR102_nonRatioSensativeItems() {
			return r102_nonRatioSensativeItems;
		}

		public void setR102_nonRatioSensativeItems(BigDecimal r102_nonRatioSensativeItems) {
			this.r102_nonRatioSensativeItems = r102_nonRatioSensativeItems;
		}

		public BigDecimal getR102_total() {
			return r102_total;
		}

		public void setR102_total(BigDecimal r102_total) {
			this.r102_total = r102_total;
		}

		public String getR103product() {
			return r103product;
		}

		public void setR103product(String r103product) {
			this.r103product = r103product;
		}

		public BigDecimal getR103_upTo1Month() {
			return r103_upTo1Month;
		}

		public void setR103_upTo1Month(BigDecimal r103_upTo1Month) {
			this.r103_upTo1Month = r103_upTo1Month;
		}

		public BigDecimal getR103_moreThan1MonthTo3Months() {
			return r103_moreThan1MonthTo3Months;
		}

		public void setR103_moreThan1MonthTo3Months(BigDecimal r103_moreThan1MonthTo3Months) {
			this.r103_moreThan1MonthTo3Months = r103_moreThan1MonthTo3Months;
		}

		public BigDecimal getR103_moreThan3MonthTo6Months() {
			return r103_moreThan3MonthTo6Months;
		}

		public void setR103_moreThan3MonthTo6Months(BigDecimal r103_moreThan3MonthTo6Months) {
			this.r103_moreThan3MonthTo6Months = r103_moreThan3MonthTo6Months;
		}

		public BigDecimal getR103_moreThan6MonthTo12Months() {
			return r103_moreThan6MonthTo12Months;
		}

		public void setR103_moreThan6MonthTo12Months(BigDecimal r103_moreThan6MonthTo12Months) {
			this.r103_moreThan6MonthTo12Months = r103_moreThan6MonthTo12Months;
		}

		public BigDecimal getR103_moreThan12MonthTo3Years() {
			return r103_moreThan12MonthTo3Years;
		}

		public void setR103_moreThan12MonthTo3Years(BigDecimal r103_moreThan12MonthTo3Years) {
			this.r103_moreThan12MonthTo3Years = r103_moreThan12MonthTo3Years;
		}

		public BigDecimal getR103_moreThan3YearsTo5Years() {
			return r103_moreThan3YearsTo5Years;
		}

		public void setR103_moreThan3YearsTo5Years(BigDecimal r103_moreThan3YearsTo5Years) {
			this.r103_moreThan3YearsTo5Years = r103_moreThan3YearsTo5Years;
		}

		public BigDecimal getR103_moreThan5YearsTo10Years() {
			return r103_moreThan5YearsTo10Years;
		}

		public void setR103_moreThan5YearsTo10Years(BigDecimal r103_moreThan5YearsTo10Years) {
			this.r103_moreThan5YearsTo10Years = r103_moreThan5YearsTo10Years;
		}

		public BigDecimal getR103_moreThan10Years() {
			return r103_moreThan10Years;
		}

		public void setR103_moreThan10Years(BigDecimal r103_moreThan10Years) {
			this.r103_moreThan10Years = r103_moreThan10Years;
		}

		public BigDecimal getR103_nonRatioSensativeItems() {
			return r103_nonRatioSensativeItems;
		}

		public void setR103_nonRatioSensativeItems(BigDecimal r103_nonRatioSensativeItems) {
			this.r103_nonRatioSensativeItems = r103_nonRatioSensativeItems;
		}

		public BigDecimal getR103_total() {
			return r103_total;
		}

		public void setR103_total(BigDecimal r103_total) {
			this.r103_total = r103_total;
		}

		public String getR104product() {
			return r104product;
		}

		public void setR104product(String r104product) {
			this.r104product = r104product;
		}

		public BigDecimal getR104_upTo1Month() {
			return r104_upTo1Month;
		}

		public void setR104_upTo1Month(BigDecimal r104_upTo1Month) {
			this.r104_upTo1Month = r104_upTo1Month;
		}

		public BigDecimal getR104_moreThan1MonthTo3Months() {
			return r104_moreThan1MonthTo3Months;
		}

		public void setR104_moreThan1MonthTo3Months(BigDecimal r104_moreThan1MonthTo3Months) {
			this.r104_moreThan1MonthTo3Months = r104_moreThan1MonthTo3Months;
		}

		public BigDecimal getR104_moreThan3MonthTo6Months() {
			return r104_moreThan3MonthTo6Months;
		}

		public void setR104_moreThan3MonthTo6Months(BigDecimal r104_moreThan3MonthTo6Months) {
			this.r104_moreThan3MonthTo6Months = r104_moreThan3MonthTo6Months;
		}

		public BigDecimal getR104_moreThan6MonthTo12Months() {
			return r104_moreThan6MonthTo12Months;
		}

		public void setR104_moreThan6MonthTo12Months(BigDecimal r104_moreThan6MonthTo12Months) {
			this.r104_moreThan6MonthTo12Months = r104_moreThan6MonthTo12Months;
		}

		public BigDecimal getR104_moreThan12MonthTo3Years() {
			return r104_moreThan12MonthTo3Years;
		}

		public void setR104_moreThan12MonthTo3Years(BigDecimal r104_moreThan12MonthTo3Years) {
			this.r104_moreThan12MonthTo3Years = r104_moreThan12MonthTo3Years;
		}

		public BigDecimal getR104_moreThan3YearsTo5Years() {
			return r104_moreThan3YearsTo5Years;
		}

		public void setR104_moreThan3YearsTo5Years(BigDecimal r104_moreThan3YearsTo5Years) {
			this.r104_moreThan3YearsTo5Years = r104_moreThan3YearsTo5Years;
		}

		public BigDecimal getR104_moreThan5YearsTo10Years() {
			return r104_moreThan5YearsTo10Years;
		}

		public void setR104_moreThan5YearsTo10Years(BigDecimal r104_moreThan5YearsTo10Years) {
			this.r104_moreThan5YearsTo10Years = r104_moreThan5YearsTo10Years;
		}

		public BigDecimal getR104_moreThan10Years() {
			return r104_moreThan10Years;
		}

		public void setR104_moreThan10Years(BigDecimal r104_moreThan10Years) {
			this.r104_moreThan10Years = r104_moreThan10Years;
		}

		public BigDecimal getR104_nonRatioSensativeItems() {
			return r104_nonRatioSensativeItems;
		}

		public void setR104_nonRatioSensativeItems(BigDecimal r104_nonRatioSensativeItems) {
			this.r104_nonRatioSensativeItems = r104_nonRatioSensativeItems;
		}

		public BigDecimal getR104_total() {
			return r104_total;
		}

		public void setR104_total(BigDecimal r104_total) {
			this.r104_total = r104_total;
		}

		public String getR105product() {
			return r105product;
		}

		public void setR105product(String r105product) {
			this.r105product = r105product;
		}

		public BigDecimal getR105_upTo1Month() {
			return r105_upTo1Month;
		}

		public void setR105_upTo1Month(BigDecimal r105_upTo1Month) {
			this.r105_upTo1Month = r105_upTo1Month;
		}

		public BigDecimal getR105_moreThan1MonthTo3Months() {
			return r105_moreThan1MonthTo3Months;
		}

		public void setR105_moreThan1MonthTo3Months(BigDecimal r105_moreThan1MonthTo3Months) {
			this.r105_moreThan1MonthTo3Months = r105_moreThan1MonthTo3Months;
		}

		public BigDecimal getR105_moreThan3MonthTo6Months() {
			return r105_moreThan3MonthTo6Months;
		}

		public void setR105_moreThan3MonthTo6Months(BigDecimal r105_moreThan3MonthTo6Months) {
			this.r105_moreThan3MonthTo6Months = r105_moreThan3MonthTo6Months;
		}

		public BigDecimal getR105_moreThan6MonthTo12Months() {
			return r105_moreThan6MonthTo12Months;
		}

		public void setR105_moreThan6MonthTo12Months(BigDecimal r105_moreThan6MonthTo12Months) {
			this.r105_moreThan6MonthTo12Months = r105_moreThan6MonthTo12Months;
		}

		public BigDecimal getR105_moreThan12MonthTo3Years() {
			return r105_moreThan12MonthTo3Years;
		}

		public void setR105_moreThan12MonthTo3Years(BigDecimal r105_moreThan12MonthTo3Years) {
			this.r105_moreThan12MonthTo3Years = r105_moreThan12MonthTo3Years;
		}

		public BigDecimal getR105_moreThan3YearsTo5Years() {
			return r105_moreThan3YearsTo5Years;
		}

		public void setR105_moreThan3YearsTo5Years(BigDecimal r105_moreThan3YearsTo5Years) {
			this.r105_moreThan3YearsTo5Years = r105_moreThan3YearsTo5Years;
		}

		public BigDecimal getR105_moreThan5YearsTo10Years() {
			return r105_moreThan5YearsTo10Years;
		}

		public void setR105_moreThan5YearsTo10Years(BigDecimal r105_moreThan5YearsTo10Years) {
			this.r105_moreThan5YearsTo10Years = r105_moreThan5YearsTo10Years;
		}

		public BigDecimal getR105_moreThan10Years() {
			return r105_moreThan10Years;
		}

		public void setR105_moreThan10Years(BigDecimal r105_moreThan10Years) {
			this.r105_moreThan10Years = r105_moreThan10Years;
		}

		public BigDecimal getR105_nonRatioSensativeItems() {
			return r105_nonRatioSensativeItems;
		}

		public void setR105_nonRatioSensativeItems(BigDecimal r105_nonRatioSensativeItems) {
			this.r105_nonRatioSensativeItems = r105_nonRatioSensativeItems;
		}

		public BigDecimal getR105_total() {
			return r105_total;
		}

		public void setR105_total(BigDecimal r105_total) {
			this.r105_total = r105_total;
		}

		public String getR106product() {
			return r106product;
		}

		public void setR106product(String r106product) {
			this.r106product = r106product;
		}

		public BigDecimal getR106_upTo1Month() {
			return r106_upTo1Month;
		}

		public void setR106_upTo1Month(BigDecimal r106_upTo1Month) {
			this.r106_upTo1Month = r106_upTo1Month;
		}

		public BigDecimal getR106_moreThan1MonthTo3Months() {
			return r106_moreThan1MonthTo3Months;
		}

		public void setR106_moreThan1MonthTo3Months(BigDecimal r106_moreThan1MonthTo3Months) {
			this.r106_moreThan1MonthTo3Months = r106_moreThan1MonthTo3Months;
		}

		public BigDecimal getR106_moreThan3MonthTo6Months() {
			return r106_moreThan3MonthTo6Months;
		}

		public void setR106_moreThan3MonthTo6Months(BigDecimal r106_moreThan3MonthTo6Months) {
			this.r106_moreThan3MonthTo6Months = r106_moreThan3MonthTo6Months;
		}

		public BigDecimal getR106_moreThan6MonthTo12Months() {
			return r106_moreThan6MonthTo12Months;
		}

		public void setR106_moreThan6MonthTo12Months(BigDecimal r106_moreThan6MonthTo12Months) {
			this.r106_moreThan6MonthTo12Months = r106_moreThan6MonthTo12Months;
		}

		public BigDecimal getR106_moreThan12MonthTo3Years() {
			return r106_moreThan12MonthTo3Years;
		}

		public void setR106_moreThan12MonthTo3Years(BigDecimal r106_moreThan12MonthTo3Years) {
			this.r106_moreThan12MonthTo3Years = r106_moreThan12MonthTo3Years;
		}

		public BigDecimal getR106_moreThan3YearsTo5Years() {
			return r106_moreThan3YearsTo5Years;
		}

		public void setR106_moreThan3YearsTo5Years(BigDecimal r106_moreThan3YearsTo5Years) {
			this.r106_moreThan3YearsTo5Years = r106_moreThan3YearsTo5Years;
		}

		public BigDecimal getR106_moreThan5YearsTo10Years() {
			return r106_moreThan5YearsTo10Years;
		}

		public void setR106_moreThan5YearsTo10Years(BigDecimal r106_moreThan5YearsTo10Years) {
			this.r106_moreThan5YearsTo10Years = r106_moreThan5YearsTo10Years;
		}

		public BigDecimal getR106_moreThan10Years() {
			return r106_moreThan10Years;
		}

		public void setR106_moreThan10Years(BigDecimal r106_moreThan10Years) {
			this.r106_moreThan10Years = r106_moreThan10Years;
		}

		public BigDecimal getR106_nonRatioSensativeItems() {
			return r106_nonRatioSensativeItems;
		}

		public void setR106_nonRatioSensativeItems(BigDecimal r106_nonRatioSensativeItems) {
			this.r106_nonRatioSensativeItems = r106_nonRatioSensativeItems;
		}

		public BigDecimal getR106_total() {
			return r106_total;
		}

		public void setR106_total(BigDecimal r106_total) {
			this.r106_total = r106_total;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal reportVersion) {
			this.reportVersion = reportVersion;
		}

		public String getReportFrequency() {
			return reportFrequency;
		}

		public void setReportFrequency(String reportFrequency) {
			this.reportFrequency = reportFrequency;
		}

		public String getReportCode() {
			return reportCode;
		}

		public void setReportCode(String reportCode) {
			this.reportCode = reportCode;
		}

		public String getReportDesc() {
			return reportDesc;
		}

		public void setReportDesc(String reportDesc) {
			this.reportDesc = reportDesc;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}


	public M_IRB_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
