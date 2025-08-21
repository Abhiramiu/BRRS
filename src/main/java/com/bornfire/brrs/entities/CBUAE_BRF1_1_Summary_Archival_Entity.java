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
@Table(name = "CBUAE_BRF1_1_ARCHIVALTABLE_SUMMARY")
public class CBUAE_BRF1_1_Summary_Archival_Entity {
	private String R0010_PRODUCT;
	private BigDecimal R0010_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0010_AMOUNT_AED_RESIDENT;
	private BigDecimal R0010_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0010_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0010_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0010_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0010_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0010_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0010_TOTAL_AMOUNT;
	private String R0020_PRODUCT;
	private BigDecimal R0020_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0020_AMOUNT_AED_RESIDENT;
	private BigDecimal R0020_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0020_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0020_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0020_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0020_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0020_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0020_TOTAL_AMOUNT;
	private String R0030_PRODUCT;
	private BigDecimal R0030_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0030_AMOUNT_AED_RESIDENT;
	private BigDecimal R0030_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0030_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0030_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0030_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0030_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0030_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0030_TOTAL_AMOUNT;
	private String R0040_PRODUCT;
	private BigDecimal R0040_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0040_AMOUNT_AED_RESIDENT;
	private BigDecimal R0040_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0040_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0040_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0040_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0040_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0040_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0040_TOTAL_AMOUNT;
	private String R0050_PRODUCT;
	private BigDecimal R0050_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0050_AMOUNT_AED_RESIDENT;
	private BigDecimal R0050_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0050_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0050_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0050_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0050_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0050_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0050_TOTAL_AMOUNT;
	private String R0060_PRODUCT;
	private BigDecimal R0060_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0060_AMOUNT_AED_RESIDENT;
	private BigDecimal R0060_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0060_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0060_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0060_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0060_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0060_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0060_TOTAL_AMOUNT;
	private String R0070_PRODUCT;
	private BigDecimal R0070_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0070_AMOUNT_AED_RESIDENT;
	private BigDecimal R0070_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0070_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0070_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0070_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0070_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0070_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0070_TOTAL_AMOUNT;
	private String R0080_PRODUCT;
	private BigDecimal R0080_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0080_AMOUNT_AED_RESIDENT;
	private BigDecimal R0080_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0080_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0080_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0080_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0080_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0080_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0080_TOTAL_AMOUNT;
	private String R0090_PRODUCT;
	private BigDecimal R0090_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0090_AMOUNT_AED_RESIDENT;
	private BigDecimal R0090_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0090_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0090_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0090_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0090_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0090_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0090_TOTAL_AMOUNT;
	private String R0100_PRODUCT;
	private BigDecimal R0100_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0100_AMOUNT_AED_RESIDENT;
	private BigDecimal R0100_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0100_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0100_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0100_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0100_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0100_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0100_TOTAL_AMOUNT;
	private String R0110_PRODUCT;
	private BigDecimal R0110_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0110_AMOUNT_AED_RESIDENT;
	private BigDecimal R0110_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0110_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0110_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0110_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0110_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0110_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0110_TOTAL_AMOUNT;
	private String R0120_PRODUCT;
	private BigDecimal R0120_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0120_AMOUNT_AED_RESIDENT;
	private BigDecimal R0120_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0120_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0120_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0120_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0120_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0120_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0120_TOTAL_AMOUNT;
	private String R0130_PRODUCT;
	private BigDecimal R0130_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0130_AMOUNT_AED_RESIDENT;
	private BigDecimal R0130_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0130_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0130_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0130_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0130_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0130_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0130_TOTAL_AMOUNT;
	private String R0140_PRODUCT;
	private BigDecimal R0140_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0140_AMOUNT_AED_RESIDENT;
	private BigDecimal R0140_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0140_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0140_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0140_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0140_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0140_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0140_TOTAL_AMOUNT;
	private String R0150_PRODUCT;
	private BigDecimal R0150_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0150_AMOUNT_AED_RESIDENT;
	private BigDecimal R0150_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0150_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0150_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0150_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0150_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0150_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0150_TOTAL_AMOUNT;
	private String R0160_PRODUCT;
	private BigDecimal R0160_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0160_AMOUNT_AED_RESIDENT;
	private BigDecimal R0160_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0160_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0160_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0160_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0160_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0160_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0160_TOTAL_AMOUNT;
	private String R0170_PRODUCT;
	private BigDecimal R0170_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0170_AMOUNT_AED_RESIDENT;
	private BigDecimal R0170_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0170_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0170_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0170_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0170_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0170_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0170_TOTAL_AMOUNT;
	private String R0180_PRODUCT;
	private BigDecimal R0180_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0180_AMOUNT_AED_RESIDENT;
	private BigDecimal R0180_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0180_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0180_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0180_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0180_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0180_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0180_TOTAL_AMOUNT;
	private String R0190_PRODUCT;
	private BigDecimal R0190_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0190_AMOUNT_AED_RESIDENT;
	private BigDecimal R0190_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0190_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0190_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0190_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0190_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0190_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0190_TOTAL_AMOUNT;
	private String R0200_PRODUCT;
	private BigDecimal R0200_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0200_AMOUNT_AED_RESIDENT;
	private BigDecimal R0200_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0200_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0200_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0200_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0200_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0200_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0200_TOTAL_AMOUNT;
	private String R0210_PRODUCT;
	private BigDecimal R0210_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0210_AMOUNT_AED_RESIDENT;
	private BigDecimal R0210_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0210_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0210_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0210_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0210_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0210_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0210_TOTAL_AMOUNT;
	private String R0220_PRODUCT;
	private BigDecimal R0220_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0220_AMOUNT_AED_RESIDENT;
	private BigDecimal R0220_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0220_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0220_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0220_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0220_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0220_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0220_TOTAL_AMOUNT;
	private String R0230_PRODUCT;
	private BigDecimal R0230_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0230_AMOUNT_AED_RESIDENT;
	private BigDecimal R0230_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0230_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0230_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0230_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0230_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0230_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0230_TOTAL_AMOUNT;
	private String R0240_PRODUCT;
	private BigDecimal R0240_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0240_AMOUNT_AED_RESIDENT;
	private BigDecimal R0240_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0240_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0240_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0240_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0240_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0240_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0240_TOTAL_AMOUNT;
	private String R0250_PRODUCT;
	private BigDecimal R0250_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0250_AMOUNT_AED_RESIDENT;
	private BigDecimal R0250_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0250_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0250_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0250_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0250_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0250_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0250_TOTAL_AMOUNT;
	private String R0260_PRODUCT;
	private BigDecimal R0260_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0260_AMOUNT_AED_RESIDENT;
	private BigDecimal R0260_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0260_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0260_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0260_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0260_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0260_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0260_TOTAL_AMOUNT;
	private String R0270_PRODUCT;
	private BigDecimal R0270_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0270_AMOUNT_AED_RESIDENT;
	private BigDecimal R0270_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0270_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0270_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0270_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0270_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0270_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0270_TOTAL_AMOUNT;
	private String R0280_PRODUCT;
	private BigDecimal R0280_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0280_AMOUNT_AED_RESIDENT;
	private BigDecimal R0280_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0280_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0280_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0280_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0280_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0280_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0280_TOTAL_AMOUNT;
	private String R0290_PRODUCT;
	private BigDecimal R0290_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0290_AMOUNT_AED_RESIDENT;
	private BigDecimal R0290_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0290_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0290_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0290_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0290_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0290_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0290_TOTAL_AMOUNT;
	private String R0300_PRODUCT;
	private BigDecimal R0300_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0300_AMOUNT_AED_RESIDENT;
	private BigDecimal R0300_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0300_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0300_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0300_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0300_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0300_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0300_TOTAL_AMOUNT;
	private String R0310_PRODUCT;
	private BigDecimal R0310_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0310_AMOUNT_AED_RESIDENT;
	private BigDecimal R0310_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0310_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0310_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0310_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0310_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0310_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0310_TOTAL_AMOUNT;
	private String R0320_PRODUCT;
	private BigDecimal R0320_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0320_AMOUNT_AED_RESIDENT;
	private BigDecimal R0320_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0320_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0320_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0320_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0320_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0320_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0320_TOTAL_AMOUNT;
	private String R0330_PRODUCT;
	private BigDecimal R0330_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0330_AMOUNT_AED_RESIDENT;
	private BigDecimal R0330_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0330_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0330_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0330_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0330_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0330_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0330_TOTAL_AMOUNT;
	private String R0340_PRODUCT;
	private BigDecimal R0340_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0340_AMOUNT_AED_RESIDENT;
	private BigDecimal R0340_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0340_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0340_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0340_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0340_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0340_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0340_TOTAL_AMOUNT;
	private String R0350_PRODUCT;
	private BigDecimal R0350_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0350_AMOUNT_AED_RESIDENT;
	private BigDecimal R0350_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0350_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0350_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0350_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0350_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0350_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0350_TOTAL_AMOUNT;
	private String R0360_PRODUCT;
	private BigDecimal R0360_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0360_AMOUNT_AED_RESIDENT;
	private BigDecimal R0360_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0360_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0360_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0360_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0360_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0360_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0360_TOTAL_AMOUNT;
	private String R0370_PRODUCT;
	private BigDecimal R0370_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0370_AMOUNT_AED_RESIDENT;
	private BigDecimal R0370_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0370_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0370_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0370_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0370_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0370_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0370_TOTAL_AMOUNT;
	private String R0380_PRODUCT;
	private BigDecimal R0380_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0380_AMOUNT_AED_RESIDENT;
	private BigDecimal R0380_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0380_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0380_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0380_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0380_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0380_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0380_TOTAL_AMOUNT;
	private String R0390_PRODUCT;
	private BigDecimal R0390_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0390_AMOUNT_AED_RESIDENT;
	private BigDecimal R0390_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0390_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0390_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0390_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0390_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0390_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0390_TOTAL_AMOUNT;
	private String R0400_PRODUCT;
	private BigDecimal R0400_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0400_AMOUNT_AED_RESIDENT;
	private BigDecimal R0400_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0400_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0400_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0400_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0400_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0400_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0400_TOTAL_AMOUNT;
	private String R0410_PRODUCT;
	private BigDecimal R0410_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0410_AMOUNT_AED_RESIDENT;
	private BigDecimal R0410_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0410_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0410_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0410_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0410_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0410_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0410_TOTAL_AMOUNT;
	private String R0420_PRODUCT;
	private BigDecimal R0420_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0420_AMOUNT_AED_RESIDENT;
	private BigDecimal R0420_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0420_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0420_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0420_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0420_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0420_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0420_TOTAL_AMOUNT;
	private String R0430_PRODUCT;
	private BigDecimal R0430_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0430_AMOUNT_AED_RESIDENT;
	private BigDecimal R0430_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0430_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0430_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0430_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0430_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0430_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0430_TOTAL_AMOUNT;
	private String R0440_PRODUCT;
	private BigDecimal R0440_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0440_AMOUNT_AED_RESIDENT;
	private BigDecimal R0440_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0440_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0440_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0440_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0440_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0440_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0440_TOTAL_AMOUNT;
	private String R0450_PRODUCT;
	private BigDecimal R0450_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0450_AMOUNT_AED_RESIDENT;
	private BigDecimal R0450_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0450_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0450_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0450_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0450_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0450_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0450_TOTAL_AMOUNT;
	private String R0460_PRODUCT;
	private BigDecimal R0460_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0460_AMOUNT_AED_RESIDENT;
	private BigDecimal R0460_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0460_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0460_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0460_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0460_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0460_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0460_TOTAL_AMOUNT;
	private String R0470_PRODUCT;
	private BigDecimal R0470_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0470_AMOUNT_AED_RESIDENT;
	private BigDecimal R0470_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0470_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0470_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0470_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0470_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0470_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0470_TOTAL_AMOUNT;
	private String R0480_PRODUCT;
	private BigDecimal R0480_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0480_AMOUNT_AED_RESIDENT;
	private BigDecimal R0480_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0480_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0480_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0480_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0480_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0480_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0480_TOTAL_AMOUNT;
	private String R0490_PRODUCT;
	private BigDecimal R0490_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0490_AMOUNT_AED_RESIDENT;
	private BigDecimal R0490_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0490_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0490_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0490_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0490_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0490_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0490_TOTAL_AMOUNT;
	private String R0500_PRODUCT;
	private BigDecimal R0500_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0500_AMOUNT_AED_RESIDENT;
	private BigDecimal R0500_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0500_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0500_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0500_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0500_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0500_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0500_TOTAL_AMOUNT;
	private String R0510_PRODUCT;
	private BigDecimal R0510_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0510_AMOUNT_AED_RESIDENT;
	private BigDecimal R0510_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0510_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0510_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0510_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0510_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0510_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0510_TOTAL_AMOUNT;
	private String R0520_PRODUCT;
	private BigDecimal R0520_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0520_AMOUNT_AED_RESIDENT;
	private BigDecimal R0520_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0520_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0520_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0520_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0520_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0520_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0520_TOTAL_AMOUNT;
	private String R0530_PRODUCT;
	private BigDecimal R0530_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0530_AMOUNT_AED_RESIDENT;
	private BigDecimal R0530_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0530_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0530_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0530_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0530_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0530_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0530_TOTAL_AMOUNT;
	private String R0540_PRODUCT;
	private BigDecimal R0540_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0540_AMOUNT_AED_RESIDENT;
	private BigDecimal R0540_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0540_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0540_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0540_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0540_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0540_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0540_TOTAL_AMOUNT;
	private String R0550_PRODUCT;
	private BigDecimal R0550_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0550_AMOUNT_AED_RESIDENT;
	private BigDecimal R0550_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0550_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0550_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0550_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0550_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0550_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0550_TOTAL_AMOUNT;
	private String R0560_PRODUCT;
	private BigDecimal R0560_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0560_AMOUNT_AED_RESIDENT;
	private BigDecimal R0560_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0560_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0560_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0560_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0560_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0560_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0560_TOTAL_AMOUNT;
	private String R0570_PRODUCT;
	private BigDecimal R0570_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0570_AMOUNT_AED_RESIDENT;
	private BigDecimal R0570_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0570_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0570_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0570_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0570_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0570_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0570_TOTAL_AMOUNT;
	private String R0580_PRODUCT;
	private BigDecimal R0580_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0580_AMOUNT_AED_RESIDENT;
	private BigDecimal R0580_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0580_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0580_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0580_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0580_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0580_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0580_TOTAL_AMOUNT;
	private String R0590_PRODUCT;
	private BigDecimal R0590_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0590_AMOUNT_AED_RESIDENT;
	private BigDecimal R0590_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0590_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0590_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0590_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0590_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0590_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0590_TOTAL_AMOUNT;
	private String R0600_PRODUCT;
	private BigDecimal R0600_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0600_AMOUNT_AED_RESIDENT;
	private BigDecimal R0600_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0600_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0600_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0600_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0600_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0600_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0600_TOTAL_AMOUNT;
	private String R0610_PRODUCT;
	private BigDecimal R0610_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0610_AMOUNT_AED_RESIDENT;
	private BigDecimal R0610_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0610_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0610_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0610_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0610_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0610_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0610_TOTAL_AMOUNT;
	private String R0620_PRODUCT;
	private BigDecimal R0620_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0620_AMOUNT_AED_RESIDENT;
	private BigDecimal R0620_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0620_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0620_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0620_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0620_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0620_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0620_TOTAL_AMOUNT;
	private String R0630_PRODUCT;
	private BigDecimal R0630_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0630_AMOUNT_AED_RESIDENT;
	private BigDecimal R0630_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0630_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0630_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0630_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0630_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0630_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0630_TOTAL_AMOUNT;
	private String R0640_PRODUCT;
	private BigDecimal R0640_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0640_AMOUNT_AED_RESIDENT;
	private BigDecimal R0640_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0640_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0640_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0640_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0640_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0640_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0640_TOTAL_AMOUNT;
	private String R0650_PRODUCT;
	private BigDecimal R0650_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0650_AMOUNT_AED_RESIDENT;
	private BigDecimal R0650_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0650_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0650_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0650_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0650_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0650_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0650_TOTAL_AMOUNT;
	private String R0660_PRODUCT;
	private BigDecimal R0660_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0660_AMOUNT_AED_RESIDENT;
	private BigDecimal R0660_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0660_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0660_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0660_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0660_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0660_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0660_TOTAL_AMOUNT;
	private String R0670_PRODUCT;
	private BigDecimal R0670_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0670_AMOUNT_AED_RESIDENT;
	private BigDecimal R0670_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0670_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0670_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0670_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0670_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0670_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0670_TOTAL_AMOUNT;
	private String R0680_PRODUCT;
	private BigDecimal R0680_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0680_AMOUNT_AED_RESIDENT;
	private BigDecimal R0680_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0680_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0680_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0680_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0680_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0680_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0680_TOTAL_AMOUNT;
	private String R0690_PRODUCT;
	private BigDecimal R0690_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0690_AMOUNT_AED_RESIDENT;
	private BigDecimal R0690_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0690_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0690_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0690_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0690_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0690_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0690_TOTAL_AMOUNT;
	private String R0700_PRODUCT;
	private BigDecimal R0700_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0700_AMOUNT_AED_RESIDENT;
	private BigDecimal R0700_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0700_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0700_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0700_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0700_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0700_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0700_TOTAL_AMOUNT;
	private String R0710_PRODUCT;
	private BigDecimal R0710_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0710_AMOUNT_AED_RESIDENT;
	private BigDecimal R0710_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0710_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0710_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0710_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0710_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0710_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0710_TOTAL_AMOUNT;
	private String R0720_PRODUCT;
	private BigDecimal R0720_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0720_AMOUNT_AED_RESIDENT;
	private BigDecimal R0720_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0720_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0720_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0720_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0720_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0720_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0720_TOTAL_AMOUNT;
	private String R0730_PRODUCT;
	private BigDecimal R0730_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0730_AMOUNT_AED_RESIDENT;
	private BigDecimal R0730_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0730_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0730_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0730_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0730_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0730_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0730_TOTAL_AMOUNT;
	private String R0740_PRODUCT;
	private BigDecimal R0740_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0740_AMOUNT_AED_RESIDENT;
	private BigDecimal R0740_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0740_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0740_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0740_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0740_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0740_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0740_TOTAL_AMOUNT;
	private String R0750_PRODUCT;
	private BigDecimal R0750_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0750_AMOUNT_AED_RESIDENT;
	private BigDecimal R0750_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0750_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0750_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0750_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0750_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0750_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0750_TOTAL_AMOUNT;
	private String R0760_PRODUCT;
	private BigDecimal R0760_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0760_AMOUNT_AED_RESIDENT;
	private BigDecimal R0760_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0760_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0760_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0760_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0760_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0760_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0760_TOTAL_AMOUNT;
	private String R0770_PRODUCT;
	private BigDecimal R0770_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0770_AMOUNT_AED_RESIDENT;
	private BigDecimal R0770_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0770_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0770_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0770_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0770_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0770_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0770_TOTAL_AMOUNT;
	private String R0780_PRODUCT;
	private BigDecimal R0780_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0780_AMOUNT_AED_RESIDENT;
	private BigDecimal R0780_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0780_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0780_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0780_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0780_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0780_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0780_TOTAL_AMOUNT;
	private String R0790_PRODUCT;
	private BigDecimal R0790_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0790_AMOUNT_AED_RESIDENT;
	private BigDecimal R0790_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0790_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0790_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0790_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0790_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0790_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0790_TOTAL_AMOUNT;
	private String R0800_PRODUCT;
	private BigDecimal R0800_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0800_AMOUNT_AED_RESIDENT;
	private BigDecimal R0800_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0800_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0800_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0800_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0800_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0800_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0800_TOTAL_AMOUNT;
	private String R0810_PRODUCT;
	private BigDecimal R0810_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0810_AMOUNT_AED_RESIDENT;
	private BigDecimal R0810_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0810_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0810_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0810_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0810_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0810_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0810_TOTAL_AMOUNT;
	private String R0820_PRODUCT;
	private BigDecimal R0820_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0820_AMOUNT_AED_RESIDENT;
	private BigDecimal R0820_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0820_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0820_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0820_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0820_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0820_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0820_TOTAL_AMOUNT;
	private String R0830_PRODUCT;
	private BigDecimal R0830_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0830_AMOUNT_AED_RESIDENT;
	private BigDecimal R0830_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0830_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0830_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0830_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0830_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0830_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0830_TOTAL_AMOUNT;
	private String R0840_PRODUCT;
	private BigDecimal R0840_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0840_AMOUNT_AED_RESIDENT;
	private BigDecimal R0840_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0840_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0840_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0840_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0840_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0840_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0840_TOTAL_AMOUNT;
	private String R0850_PRODUCT;
	private BigDecimal R0850_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0850_AMOUNT_AED_RESIDENT;
	private BigDecimal R0850_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0850_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0850_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0850_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0850_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0850_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0850_TOTAL_AMOUNT;
	private String R0860_PRODUCT;
	private BigDecimal R0860_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0860_AMOUNT_AED_RESIDENT;
	private BigDecimal R0860_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0860_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0860_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0860_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0860_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0860_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0860_TOTAL_AMOUNT;
	private String R0870_PRODUCT;
	private BigDecimal R0870_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0870_AMOUNT_AED_RESIDENT;
	private BigDecimal R0870_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0870_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0870_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0870_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0870_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0870_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0870_TOTAL_AMOUNT;
	private String R0880_PRODUCT;
	private BigDecimal R0880_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0880_AMOUNT_AED_RESIDENT;
	private BigDecimal R0880_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0880_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0880_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0880_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0880_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0880_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0880_TOTAL_AMOUNT;
	private String R0890_PRODUCT;
	private BigDecimal R0890_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0890_AMOUNT_AED_RESIDENT;
	private BigDecimal R0890_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0890_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0890_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0890_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0890_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0890_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0890_TOTAL_AMOUNT;
	private String R0900_PRODUCT;
	private BigDecimal R0900_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0900_AMOUNT_AED_RESIDENT;
	private BigDecimal R0900_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0900_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0900_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0900_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0900_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0900_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0900_TOTAL_AMOUNT;
	private String R0910_PRODUCT;
	private BigDecimal R0910_NO_ACCT_AED_RESIDENT;
	private BigDecimal R0910_AMOUNT_AED_RESIDENT;
	private BigDecimal R0910_NO_ACCT_FCY_RESIDENT;
	private BigDecimal R0910_AMOUNT_FCY_RESIDENT;
	private BigDecimal R0910_NO_ACCT_AED_NON_RESIDENT;
	private BigDecimal R0910_AMOUNT_AED_NON_RESIDENT;
	private BigDecimal R0910_NO_ACCT_FCY_NON_RESIDENT;
	private BigDecimal R0910_AMOUNT_FCY_NON_RESIDENT;
	private BigDecimal R0910_TOTAL_AMOUNT;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	private Date report_date;
	private String REPORT_VERSION;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;
	public String getR0010_PRODUCT() {
		return R0010_PRODUCT;
	}
	public void setR0010_PRODUCT(String r0010_PRODUCT) {
		R0010_PRODUCT = r0010_PRODUCT;
	}
	public BigDecimal getR0010_NO_ACCT_AED_RESIDENT() {
		return R0010_NO_ACCT_AED_RESIDENT;
	}
	public void setR0010_NO_ACCT_AED_RESIDENT(BigDecimal r0010_NO_ACCT_AED_RESIDENT) {
		R0010_NO_ACCT_AED_RESIDENT = r0010_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0010_AMOUNT_AED_RESIDENT() {
		return R0010_AMOUNT_AED_RESIDENT;
	}
	public void setR0010_AMOUNT_AED_RESIDENT(BigDecimal r0010_AMOUNT_AED_RESIDENT) {
		R0010_AMOUNT_AED_RESIDENT = r0010_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0010_NO_ACCT_FCY_RESIDENT() {
		return R0010_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0010_NO_ACCT_FCY_RESIDENT(BigDecimal r0010_NO_ACCT_FCY_RESIDENT) {
		R0010_NO_ACCT_FCY_RESIDENT = r0010_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0010_AMOUNT_FCY_RESIDENT() {
		return R0010_AMOUNT_FCY_RESIDENT;
	}
	public void setR0010_AMOUNT_FCY_RESIDENT(BigDecimal r0010_AMOUNT_FCY_RESIDENT) {
		R0010_AMOUNT_FCY_RESIDENT = r0010_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0010_NO_ACCT_AED_NON_RESIDENT() {
		return R0010_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0010_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0010_NO_ACCT_AED_NON_RESIDENT) {
		R0010_NO_ACCT_AED_NON_RESIDENT = r0010_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0010_AMOUNT_AED_NON_RESIDENT() {
		return R0010_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0010_AMOUNT_AED_NON_RESIDENT(BigDecimal r0010_AMOUNT_AED_NON_RESIDENT) {
		R0010_AMOUNT_AED_NON_RESIDENT = r0010_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0010_NO_ACCT_FCY_NON_RESIDENT() {
		return R0010_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0010_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0010_NO_ACCT_FCY_NON_RESIDENT) {
		R0010_NO_ACCT_FCY_NON_RESIDENT = r0010_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0010_AMOUNT_FCY_NON_RESIDENT() {
		return R0010_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0010_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0010_AMOUNT_FCY_NON_RESIDENT) {
		R0010_AMOUNT_FCY_NON_RESIDENT = r0010_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0010_TOTAL_AMOUNT() {
		return R0010_TOTAL_AMOUNT;
	}
	public void setR0010_TOTAL_AMOUNT(BigDecimal r0010_TOTAL_AMOUNT) {
		R0010_TOTAL_AMOUNT = r0010_TOTAL_AMOUNT;
	}
	public String getR0020_PRODUCT() {
		return R0020_PRODUCT;
	}
	public void setR0020_PRODUCT(String r0020_PRODUCT) {
		R0020_PRODUCT = r0020_PRODUCT;
	}
	public BigDecimal getR0020_NO_ACCT_AED_RESIDENT() {
		return R0020_NO_ACCT_AED_RESIDENT;
	}
	public void setR0020_NO_ACCT_AED_RESIDENT(BigDecimal r0020_NO_ACCT_AED_RESIDENT) {
		R0020_NO_ACCT_AED_RESIDENT = r0020_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0020_AMOUNT_AED_RESIDENT() {
		return R0020_AMOUNT_AED_RESIDENT;
	}
	public void setR0020_AMOUNT_AED_RESIDENT(BigDecimal r0020_AMOUNT_AED_RESIDENT) {
		R0020_AMOUNT_AED_RESIDENT = r0020_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0020_NO_ACCT_FCY_RESIDENT() {
		return R0020_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0020_NO_ACCT_FCY_RESIDENT(BigDecimal r0020_NO_ACCT_FCY_RESIDENT) {
		R0020_NO_ACCT_FCY_RESIDENT = r0020_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0020_AMOUNT_FCY_RESIDENT() {
		return R0020_AMOUNT_FCY_RESIDENT;
	}
	public void setR0020_AMOUNT_FCY_RESIDENT(BigDecimal r0020_AMOUNT_FCY_RESIDENT) {
		R0020_AMOUNT_FCY_RESIDENT = r0020_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0020_NO_ACCT_AED_NON_RESIDENT() {
		return R0020_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0020_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0020_NO_ACCT_AED_NON_RESIDENT) {
		R0020_NO_ACCT_AED_NON_RESIDENT = r0020_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0020_AMOUNT_AED_NON_RESIDENT() {
		return R0020_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0020_AMOUNT_AED_NON_RESIDENT(BigDecimal r0020_AMOUNT_AED_NON_RESIDENT) {
		R0020_AMOUNT_AED_NON_RESIDENT = r0020_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0020_NO_ACCT_FCY_NON_RESIDENT() {
		return R0020_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0020_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0020_NO_ACCT_FCY_NON_RESIDENT) {
		R0020_NO_ACCT_FCY_NON_RESIDENT = r0020_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0020_AMOUNT_FCY_NON_RESIDENT() {
		return R0020_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0020_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0020_AMOUNT_FCY_NON_RESIDENT) {
		R0020_AMOUNT_FCY_NON_RESIDENT = r0020_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0020_TOTAL_AMOUNT() {
		return R0020_TOTAL_AMOUNT;
	}
	public void setR0020_TOTAL_AMOUNT(BigDecimal r0020_TOTAL_AMOUNT) {
		R0020_TOTAL_AMOUNT = r0020_TOTAL_AMOUNT;
	}
	public String getR0030_PRODUCT() {
		return R0030_PRODUCT;
	}
	public void setR0030_PRODUCT(String r0030_PRODUCT) {
		R0030_PRODUCT = r0030_PRODUCT;
	}
	public BigDecimal getR0030_NO_ACCT_AED_RESIDENT() {
		return R0030_NO_ACCT_AED_RESIDENT;
	}
	public void setR0030_NO_ACCT_AED_RESIDENT(BigDecimal r0030_NO_ACCT_AED_RESIDENT) {
		R0030_NO_ACCT_AED_RESIDENT = r0030_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0030_AMOUNT_AED_RESIDENT() {
		return R0030_AMOUNT_AED_RESIDENT;
	}
	public void setR0030_AMOUNT_AED_RESIDENT(BigDecimal r0030_AMOUNT_AED_RESIDENT) {
		R0030_AMOUNT_AED_RESIDENT = r0030_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0030_NO_ACCT_FCY_RESIDENT() {
		return R0030_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0030_NO_ACCT_FCY_RESIDENT(BigDecimal r0030_NO_ACCT_FCY_RESIDENT) {
		R0030_NO_ACCT_FCY_RESIDENT = r0030_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0030_AMOUNT_FCY_RESIDENT() {
		return R0030_AMOUNT_FCY_RESIDENT;
	}
	public void setR0030_AMOUNT_FCY_RESIDENT(BigDecimal r0030_AMOUNT_FCY_RESIDENT) {
		R0030_AMOUNT_FCY_RESIDENT = r0030_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0030_NO_ACCT_AED_NON_RESIDENT() {
		return R0030_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0030_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0030_NO_ACCT_AED_NON_RESIDENT) {
		R0030_NO_ACCT_AED_NON_RESIDENT = r0030_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0030_AMOUNT_AED_NON_RESIDENT() {
		return R0030_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0030_AMOUNT_AED_NON_RESIDENT(BigDecimal r0030_AMOUNT_AED_NON_RESIDENT) {
		R0030_AMOUNT_AED_NON_RESIDENT = r0030_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0030_NO_ACCT_FCY_NON_RESIDENT() {
		return R0030_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0030_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0030_NO_ACCT_FCY_NON_RESIDENT) {
		R0030_NO_ACCT_FCY_NON_RESIDENT = r0030_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0030_AMOUNT_FCY_NON_RESIDENT() {
		return R0030_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0030_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0030_AMOUNT_FCY_NON_RESIDENT) {
		R0030_AMOUNT_FCY_NON_RESIDENT = r0030_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0030_TOTAL_AMOUNT() {
		return R0030_TOTAL_AMOUNT;
	}
	public void setR0030_TOTAL_AMOUNT(BigDecimal r0030_TOTAL_AMOUNT) {
		R0030_TOTAL_AMOUNT = r0030_TOTAL_AMOUNT;
	}
	public String getR0040_PRODUCT() {
		return R0040_PRODUCT;
	}
	public void setR0040_PRODUCT(String r0040_PRODUCT) {
		R0040_PRODUCT = r0040_PRODUCT;
	}
	public BigDecimal getR0040_NO_ACCT_AED_RESIDENT() {
		return R0040_NO_ACCT_AED_RESIDENT;
	}
	public void setR0040_NO_ACCT_AED_RESIDENT(BigDecimal r0040_NO_ACCT_AED_RESIDENT) {
		R0040_NO_ACCT_AED_RESIDENT = r0040_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0040_AMOUNT_AED_RESIDENT() {
		return R0040_AMOUNT_AED_RESIDENT;
	}
	public void setR0040_AMOUNT_AED_RESIDENT(BigDecimal r0040_AMOUNT_AED_RESIDENT) {
		R0040_AMOUNT_AED_RESIDENT = r0040_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0040_NO_ACCT_FCY_RESIDENT() {
		return R0040_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0040_NO_ACCT_FCY_RESIDENT(BigDecimal r0040_NO_ACCT_FCY_RESIDENT) {
		R0040_NO_ACCT_FCY_RESIDENT = r0040_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0040_AMOUNT_FCY_RESIDENT() {
		return R0040_AMOUNT_FCY_RESIDENT;
	}
	public void setR0040_AMOUNT_FCY_RESIDENT(BigDecimal r0040_AMOUNT_FCY_RESIDENT) {
		R0040_AMOUNT_FCY_RESIDENT = r0040_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0040_NO_ACCT_AED_NON_RESIDENT() {
		return R0040_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0040_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0040_NO_ACCT_AED_NON_RESIDENT) {
		R0040_NO_ACCT_AED_NON_RESIDENT = r0040_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0040_AMOUNT_AED_NON_RESIDENT() {
		return R0040_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0040_AMOUNT_AED_NON_RESIDENT(BigDecimal r0040_AMOUNT_AED_NON_RESIDENT) {
		R0040_AMOUNT_AED_NON_RESIDENT = r0040_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0040_NO_ACCT_FCY_NON_RESIDENT() {
		return R0040_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0040_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0040_NO_ACCT_FCY_NON_RESIDENT) {
		R0040_NO_ACCT_FCY_NON_RESIDENT = r0040_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0040_AMOUNT_FCY_NON_RESIDENT() {
		return R0040_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0040_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0040_AMOUNT_FCY_NON_RESIDENT) {
		R0040_AMOUNT_FCY_NON_RESIDENT = r0040_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0040_TOTAL_AMOUNT() {
		return R0040_TOTAL_AMOUNT;
	}
	public void setR0040_TOTAL_AMOUNT(BigDecimal r0040_TOTAL_AMOUNT) {
		R0040_TOTAL_AMOUNT = r0040_TOTAL_AMOUNT;
	}
	public String getR0050_PRODUCT() {
		return R0050_PRODUCT;
	}
	public void setR0050_PRODUCT(String r0050_PRODUCT) {
		R0050_PRODUCT = r0050_PRODUCT;
	}
	public BigDecimal getR0050_NO_ACCT_AED_RESIDENT() {
		return R0050_NO_ACCT_AED_RESIDENT;
	}
	public void setR0050_NO_ACCT_AED_RESIDENT(BigDecimal r0050_NO_ACCT_AED_RESIDENT) {
		R0050_NO_ACCT_AED_RESIDENT = r0050_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0050_AMOUNT_AED_RESIDENT() {
		return R0050_AMOUNT_AED_RESIDENT;
	}
	public void setR0050_AMOUNT_AED_RESIDENT(BigDecimal r0050_AMOUNT_AED_RESIDENT) {
		R0050_AMOUNT_AED_RESIDENT = r0050_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0050_NO_ACCT_FCY_RESIDENT() {
		return R0050_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0050_NO_ACCT_FCY_RESIDENT(BigDecimal r0050_NO_ACCT_FCY_RESIDENT) {
		R0050_NO_ACCT_FCY_RESIDENT = r0050_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0050_AMOUNT_FCY_RESIDENT() {
		return R0050_AMOUNT_FCY_RESIDENT;
	}
	public void setR0050_AMOUNT_FCY_RESIDENT(BigDecimal r0050_AMOUNT_FCY_RESIDENT) {
		R0050_AMOUNT_FCY_RESIDENT = r0050_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0050_NO_ACCT_AED_NON_RESIDENT() {
		return R0050_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0050_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0050_NO_ACCT_AED_NON_RESIDENT) {
		R0050_NO_ACCT_AED_NON_RESIDENT = r0050_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0050_AMOUNT_AED_NON_RESIDENT() {
		return R0050_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0050_AMOUNT_AED_NON_RESIDENT(BigDecimal r0050_AMOUNT_AED_NON_RESIDENT) {
		R0050_AMOUNT_AED_NON_RESIDENT = r0050_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0050_NO_ACCT_FCY_NON_RESIDENT() {
		return R0050_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0050_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0050_NO_ACCT_FCY_NON_RESIDENT) {
		R0050_NO_ACCT_FCY_NON_RESIDENT = r0050_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0050_AMOUNT_FCY_NON_RESIDENT() {
		return R0050_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0050_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0050_AMOUNT_FCY_NON_RESIDENT) {
		R0050_AMOUNT_FCY_NON_RESIDENT = r0050_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0050_TOTAL_AMOUNT() {
		return R0050_TOTAL_AMOUNT;
	}
	public void setR0050_TOTAL_AMOUNT(BigDecimal r0050_TOTAL_AMOUNT) {
		R0050_TOTAL_AMOUNT = r0050_TOTAL_AMOUNT;
	}
	public String getR0060_PRODUCT() {
		return R0060_PRODUCT;
	}
	public void setR0060_PRODUCT(String r0060_PRODUCT) {
		R0060_PRODUCT = r0060_PRODUCT;
	}
	public BigDecimal getR0060_NO_ACCT_AED_RESIDENT() {
		return R0060_NO_ACCT_AED_RESIDENT;
	}
	public void setR0060_NO_ACCT_AED_RESIDENT(BigDecimal r0060_NO_ACCT_AED_RESIDENT) {
		R0060_NO_ACCT_AED_RESIDENT = r0060_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0060_AMOUNT_AED_RESIDENT() {
		return R0060_AMOUNT_AED_RESIDENT;
	}
	public void setR0060_AMOUNT_AED_RESIDENT(BigDecimal r0060_AMOUNT_AED_RESIDENT) {
		R0060_AMOUNT_AED_RESIDENT = r0060_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0060_NO_ACCT_FCY_RESIDENT() {
		return R0060_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0060_NO_ACCT_FCY_RESIDENT(BigDecimal r0060_NO_ACCT_FCY_RESIDENT) {
		R0060_NO_ACCT_FCY_RESIDENT = r0060_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0060_AMOUNT_FCY_RESIDENT() {
		return R0060_AMOUNT_FCY_RESIDENT;
	}
	public void setR0060_AMOUNT_FCY_RESIDENT(BigDecimal r0060_AMOUNT_FCY_RESIDENT) {
		R0060_AMOUNT_FCY_RESIDENT = r0060_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0060_NO_ACCT_AED_NON_RESIDENT() {
		return R0060_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0060_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0060_NO_ACCT_AED_NON_RESIDENT) {
		R0060_NO_ACCT_AED_NON_RESIDENT = r0060_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0060_AMOUNT_AED_NON_RESIDENT() {
		return R0060_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0060_AMOUNT_AED_NON_RESIDENT(BigDecimal r0060_AMOUNT_AED_NON_RESIDENT) {
		R0060_AMOUNT_AED_NON_RESIDENT = r0060_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0060_NO_ACCT_FCY_NON_RESIDENT() {
		return R0060_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0060_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0060_NO_ACCT_FCY_NON_RESIDENT) {
		R0060_NO_ACCT_FCY_NON_RESIDENT = r0060_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0060_AMOUNT_FCY_NON_RESIDENT() {
		return R0060_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0060_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0060_AMOUNT_FCY_NON_RESIDENT) {
		R0060_AMOUNT_FCY_NON_RESIDENT = r0060_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0060_TOTAL_AMOUNT() {
		return R0060_TOTAL_AMOUNT;
	}
	public void setR0060_TOTAL_AMOUNT(BigDecimal r0060_TOTAL_AMOUNT) {
		R0060_TOTAL_AMOUNT = r0060_TOTAL_AMOUNT;
	}
	public String getR0070_PRODUCT() {
		return R0070_PRODUCT;
	}
	public void setR0070_PRODUCT(String r0070_PRODUCT) {
		R0070_PRODUCT = r0070_PRODUCT;
	}
	public BigDecimal getR0070_NO_ACCT_AED_RESIDENT() {
		return R0070_NO_ACCT_AED_RESIDENT;
	}
	public void setR0070_NO_ACCT_AED_RESIDENT(BigDecimal r0070_NO_ACCT_AED_RESIDENT) {
		R0070_NO_ACCT_AED_RESIDENT = r0070_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0070_AMOUNT_AED_RESIDENT() {
		return R0070_AMOUNT_AED_RESIDENT;
	}
	public void setR0070_AMOUNT_AED_RESIDENT(BigDecimal r0070_AMOUNT_AED_RESIDENT) {
		R0070_AMOUNT_AED_RESIDENT = r0070_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0070_NO_ACCT_FCY_RESIDENT() {
		return R0070_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0070_NO_ACCT_FCY_RESIDENT(BigDecimal r0070_NO_ACCT_FCY_RESIDENT) {
		R0070_NO_ACCT_FCY_RESIDENT = r0070_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0070_AMOUNT_FCY_RESIDENT() {
		return R0070_AMOUNT_FCY_RESIDENT;
	}
	public void setR0070_AMOUNT_FCY_RESIDENT(BigDecimal r0070_AMOUNT_FCY_RESIDENT) {
		R0070_AMOUNT_FCY_RESIDENT = r0070_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0070_NO_ACCT_AED_NON_RESIDENT() {
		return R0070_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0070_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0070_NO_ACCT_AED_NON_RESIDENT) {
		R0070_NO_ACCT_AED_NON_RESIDENT = r0070_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0070_AMOUNT_AED_NON_RESIDENT() {
		return R0070_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0070_AMOUNT_AED_NON_RESIDENT(BigDecimal r0070_AMOUNT_AED_NON_RESIDENT) {
		R0070_AMOUNT_AED_NON_RESIDENT = r0070_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0070_NO_ACCT_FCY_NON_RESIDENT() {
		return R0070_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0070_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0070_NO_ACCT_FCY_NON_RESIDENT) {
		R0070_NO_ACCT_FCY_NON_RESIDENT = r0070_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0070_AMOUNT_FCY_NON_RESIDENT() {
		return R0070_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0070_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0070_AMOUNT_FCY_NON_RESIDENT) {
		R0070_AMOUNT_FCY_NON_RESIDENT = r0070_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0070_TOTAL_AMOUNT() {
		return R0070_TOTAL_AMOUNT;
	}
	public void setR0070_TOTAL_AMOUNT(BigDecimal r0070_TOTAL_AMOUNT) {
		R0070_TOTAL_AMOUNT = r0070_TOTAL_AMOUNT;
	}
	public String getR0080_PRODUCT() {
		return R0080_PRODUCT;
	}
	public void setR0080_PRODUCT(String r0080_PRODUCT) {
		R0080_PRODUCT = r0080_PRODUCT;
	}
	public BigDecimal getR0080_NO_ACCT_AED_RESIDENT() {
		return R0080_NO_ACCT_AED_RESIDENT;
	}
	public void setR0080_NO_ACCT_AED_RESIDENT(BigDecimal r0080_NO_ACCT_AED_RESIDENT) {
		R0080_NO_ACCT_AED_RESIDENT = r0080_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0080_AMOUNT_AED_RESIDENT() {
		return R0080_AMOUNT_AED_RESIDENT;
	}
	public void setR0080_AMOUNT_AED_RESIDENT(BigDecimal r0080_AMOUNT_AED_RESIDENT) {
		R0080_AMOUNT_AED_RESIDENT = r0080_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0080_NO_ACCT_FCY_RESIDENT() {
		return R0080_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0080_NO_ACCT_FCY_RESIDENT(BigDecimal r0080_NO_ACCT_FCY_RESIDENT) {
		R0080_NO_ACCT_FCY_RESIDENT = r0080_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0080_AMOUNT_FCY_RESIDENT() {
		return R0080_AMOUNT_FCY_RESIDENT;
	}
	public void setR0080_AMOUNT_FCY_RESIDENT(BigDecimal r0080_AMOUNT_FCY_RESIDENT) {
		R0080_AMOUNT_FCY_RESIDENT = r0080_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0080_NO_ACCT_AED_NON_RESIDENT() {
		return R0080_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0080_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0080_NO_ACCT_AED_NON_RESIDENT) {
		R0080_NO_ACCT_AED_NON_RESIDENT = r0080_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0080_AMOUNT_AED_NON_RESIDENT() {
		return R0080_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0080_AMOUNT_AED_NON_RESIDENT(BigDecimal r0080_AMOUNT_AED_NON_RESIDENT) {
		R0080_AMOUNT_AED_NON_RESIDENT = r0080_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0080_NO_ACCT_FCY_NON_RESIDENT() {
		return R0080_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0080_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0080_NO_ACCT_FCY_NON_RESIDENT) {
		R0080_NO_ACCT_FCY_NON_RESIDENT = r0080_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0080_AMOUNT_FCY_NON_RESIDENT() {
		return R0080_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0080_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0080_AMOUNT_FCY_NON_RESIDENT) {
		R0080_AMOUNT_FCY_NON_RESIDENT = r0080_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0080_TOTAL_AMOUNT() {
		return R0080_TOTAL_AMOUNT;
	}
	public void setR0080_TOTAL_AMOUNT(BigDecimal r0080_TOTAL_AMOUNT) {
		R0080_TOTAL_AMOUNT = r0080_TOTAL_AMOUNT;
	}
	public String getR0090_PRODUCT() {
		return R0090_PRODUCT;
	}
	public void setR0090_PRODUCT(String r0090_PRODUCT) {
		R0090_PRODUCT = r0090_PRODUCT;
	}
	public BigDecimal getR0090_NO_ACCT_AED_RESIDENT() {
		return R0090_NO_ACCT_AED_RESIDENT;
	}
	public void setR0090_NO_ACCT_AED_RESIDENT(BigDecimal r0090_NO_ACCT_AED_RESIDENT) {
		R0090_NO_ACCT_AED_RESIDENT = r0090_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0090_AMOUNT_AED_RESIDENT() {
		return R0090_AMOUNT_AED_RESIDENT;
	}
	public void setR0090_AMOUNT_AED_RESIDENT(BigDecimal r0090_AMOUNT_AED_RESIDENT) {
		R0090_AMOUNT_AED_RESIDENT = r0090_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0090_NO_ACCT_FCY_RESIDENT() {
		return R0090_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0090_NO_ACCT_FCY_RESIDENT(BigDecimal r0090_NO_ACCT_FCY_RESIDENT) {
		R0090_NO_ACCT_FCY_RESIDENT = r0090_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0090_AMOUNT_FCY_RESIDENT() {
		return R0090_AMOUNT_FCY_RESIDENT;
	}
	public void setR0090_AMOUNT_FCY_RESIDENT(BigDecimal r0090_AMOUNT_FCY_RESIDENT) {
		R0090_AMOUNT_FCY_RESIDENT = r0090_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0090_NO_ACCT_AED_NON_RESIDENT() {
		return R0090_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0090_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0090_NO_ACCT_AED_NON_RESIDENT) {
		R0090_NO_ACCT_AED_NON_RESIDENT = r0090_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0090_AMOUNT_AED_NON_RESIDENT() {
		return R0090_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0090_AMOUNT_AED_NON_RESIDENT(BigDecimal r0090_AMOUNT_AED_NON_RESIDENT) {
		R0090_AMOUNT_AED_NON_RESIDENT = r0090_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0090_NO_ACCT_FCY_NON_RESIDENT() {
		return R0090_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0090_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0090_NO_ACCT_FCY_NON_RESIDENT) {
		R0090_NO_ACCT_FCY_NON_RESIDENT = r0090_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0090_AMOUNT_FCY_NON_RESIDENT() {
		return R0090_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0090_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0090_AMOUNT_FCY_NON_RESIDENT) {
		R0090_AMOUNT_FCY_NON_RESIDENT = r0090_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0090_TOTAL_AMOUNT() {
		return R0090_TOTAL_AMOUNT;
	}
	public void setR0090_TOTAL_AMOUNT(BigDecimal r0090_TOTAL_AMOUNT) {
		R0090_TOTAL_AMOUNT = r0090_TOTAL_AMOUNT;
	}
	public String getR0100_PRODUCT() {
		return R0100_PRODUCT;
	}
	public void setR0100_PRODUCT(String r0100_PRODUCT) {
		R0100_PRODUCT = r0100_PRODUCT;
	}
	public BigDecimal getR0100_NO_ACCT_AED_RESIDENT() {
		return R0100_NO_ACCT_AED_RESIDENT;
	}
	public void setR0100_NO_ACCT_AED_RESIDENT(BigDecimal r0100_NO_ACCT_AED_RESIDENT) {
		R0100_NO_ACCT_AED_RESIDENT = r0100_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0100_AMOUNT_AED_RESIDENT() {
		return R0100_AMOUNT_AED_RESIDENT;
	}
	public void setR0100_AMOUNT_AED_RESIDENT(BigDecimal r0100_AMOUNT_AED_RESIDENT) {
		R0100_AMOUNT_AED_RESIDENT = r0100_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0100_NO_ACCT_FCY_RESIDENT() {
		return R0100_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0100_NO_ACCT_FCY_RESIDENT(BigDecimal r0100_NO_ACCT_FCY_RESIDENT) {
		R0100_NO_ACCT_FCY_RESIDENT = r0100_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0100_AMOUNT_FCY_RESIDENT() {
		return R0100_AMOUNT_FCY_RESIDENT;
	}
	public void setR0100_AMOUNT_FCY_RESIDENT(BigDecimal r0100_AMOUNT_FCY_RESIDENT) {
		R0100_AMOUNT_FCY_RESIDENT = r0100_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0100_NO_ACCT_AED_NON_RESIDENT() {
		return R0100_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0100_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0100_NO_ACCT_AED_NON_RESIDENT) {
		R0100_NO_ACCT_AED_NON_RESIDENT = r0100_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0100_AMOUNT_AED_NON_RESIDENT() {
		return R0100_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0100_AMOUNT_AED_NON_RESIDENT(BigDecimal r0100_AMOUNT_AED_NON_RESIDENT) {
		R0100_AMOUNT_AED_NON_RESIDENT = r0100_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0100_NO_ACCT_FCY_NON_RESIDENT() {
		return R0100_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0100_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0100_NO_ACCT_FCY_NON_RESIDENT) {
		R0100_NO_ACCT_FCY_NON_RESIDENT = r0100_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0100_AMOUNT_FCY_NON_RESIDENT() {
		return R0100_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0100_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0100_AMOUNT_FCY_NON_RESIDENT) {
		R0100_AMOUNT_FCY_NON_RESIDENT = r0100_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0100_TOTAL_AMOUNT() {
		return R0100_TOTAL_AMOUNT;
	}
	public void setR0100_TOTAL_AMOUNT(BigDecimal r0100_TOTAL_AMOUNT) {
		R0100_TOTAL_AMOUNT = r0100_TOTAL_AMOUNT;
	}
	public String getR0110_PRODUCT() {
		return R0110_PRODUCT;
	}
	public void setR0110_PRODUCT(String r0110_PRODUCT) {
		R0110_PRODUCT = r0110_PRODUCT;
	}
	public BigDecimal getR0110_NO_ACCT_AED_RESIDENT() {
		return R0110_NO_ACCT_AED_RESIDENT;
	}
	public void setR0110_NO_ACCT_AED_RESIDENT(BigDecimal r0110_NO_ACCT_AED_RESIDENT) {
		R0110_NO_ACCT_AED_RESIDENT = r0110_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0110_AMOUNT_AED_RESIDENT() {
		return R0110_AMOUNT_AED_RESIDENT;
	}
	public void setR0110_AMOUNT_AED_RESIDENT(BigDecimal r0110_AMOUNT_AED_RESIDENT) {
		R0110_AMOUNT_AED_RESIDENT = r0110_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0110_NO_ACCT_FCY_RESIDENT() {
		return R0110_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0110_NO_ACCT_FCY_RESIDENT(BigDecimal r0110_NO_ACCT_FCY_RESIDENT) {
		R0110_NO_ACCT_FCY_RESIDENT = r0110_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0110_AMOUNT_FCY_RESIDENT() {
		return R0110_AMOUNT_FCY_RESIDENT;
	}
	public void setR0110_AMOUNT_FCY_RESIDENT(BigDecimal r0110_AMOUNT_FCY_RESIDENT) {
		R0110_AMOUNT_FCY_RESIDENT = r0110_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0110_NO_ACCT_AED_NON_RESIDENT() {
		return R0110_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0110_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0110_NO_ACCT_AED_NON_RESIDENT) {
		R0110_NO_ACCT_AED_NON_RESIDENT = r0110_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0110_AMOUNT_AED_NON_RESIDENT() {
		return R0110_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0110_AMOUNT_AED_NON_RESIDENT(BigDecimal r0110_AMOUNT_AED_NON_RESIDENT) {
		R0110_AMOUNT_AED_NON_RESIDENT = r0110_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0110_NO_ACCT_FCY_NON_RESIDENT() {
		return R0110_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0110_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0110_NO_ACCT_FCY_NON_RESIDENT) {
		R0110_NO_ACCT_FCY_NON_RESIDENT = r0110_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0110_AMOUNT_FCY_NON_RESIDENT() {
		return R0110_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0110_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0110_AMOUNT_FCY_NON_RESIDENT) {
		R0110_AMOUNT_FCY_NON_RESIDENT = r0110_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0110_TOTAL_AMOUNT() {
		return R0110_TOTAL_AMOUNT;
	}
	public void setR0110_TOTAL_AMOUNT(BigDecimal r0110_TOTAL_AMOUNT) {
		R0110_TOTAL_AMOUNT = r0110_TOTAL_AMOUNT;
	}
	public String getR0120_PRODUCT() {
		return R0120_PRODUCT;
	}
	public void setR0120_PRODUCT(String r0120_PRODUCT) {
		R0120_PRODUCT = r0120_PRODUCT;
	}
	public BigDecimal getR0120_NO_ACCT_AED_RESIDENT() {
		return R0120_NO_ACCT_AED_RESIDENT;
	}
	public void setR0120_NO_ACCT_AED_RESIDENT(BigDecimal r0120_NO_ACCT_AED_RESIDENT) {
		R0120_NO_ACCT_AED_RESIDENT = r0120_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0120_AMOUNT_AED_RESIDENT() {
		return R0120_AMOUNT_AED_RESIDENT;
	}
	public void setR0120_AMOUNT_AED_RESIDENT(BigDecimal r0120_AMOUNT_AED_RESIDENT) {
		R0120_AMOUNT_AED_RESIDENT = r0120_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0120_NO_ACCT_FCY_RESIDENT() {
		return R0120_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0120_NO_ACCT_FCY_RESIDENT(BigDecimal r0120_NO_ACCT_FCY_RESIDENT) {
		R0120_NO_ACCT_FCY_RESIDENT = r0120_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0120_AMOUNT_FCY_RESIDENT() {
		return R0120_AMOUNT_FCY_RESIDENT;
	}
	public void setR0120_AMOUNT_FCY_RESIDENT(BigDecimal r0120_AMOUNT_FCY_RESIDENT) {
		R0120_AMOUNT_FCY_RESIDENT = r0120_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0120_NO_ACCT_AED_NON_RESIDENT() {
		return R0120_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0120_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0120_NO_ACCT_AED_NON_RESIDENT) {
		R0120_NO_ACCT_AED_NON_RESIDENT = r0120_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0120_AMOUNT_AED_NON_RESIDENT() {
		return R0120_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0120_AMOUNT_AED_NON_RESIDENT(BigDecimal r0120_AMOUNT_AED_NON_RESIDENT) {
		R0120_AMOUNT_AED_NON_RESIDENT = r0120_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0120_NO_ACCT_FCY_NON_RESIDENT() {
		return R0120_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0120_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0120_NO_ACCT_FCY_NON_RESIDENT) {
		R0120_NO_ACCT_FCY_NON_RESIDENT = r0120_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0120_AMOUNT_FCY_NON_RESIDENT() {
		return R0120_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0120_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0120_AMOUNT_FCY_NON_RESIDENT) {
		R0120_AMOUNT_FCY_NON_RESIDENT = r0120_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0120_TOTAL_AMOUNT() {
		return R0120_TOTAL_AMOUNT;
	}
	public void setR0120_TOTAL_AMOUNT(BigDecimal r0120_TOTAL_AMOUNT) {
		R0120_TOTAL_AMOUNT = r0120_TOTAL_AMOUNT;
	}
	public String getR0130_PRODUCT() {
		return R0130_PRODUCT;
	}
	public void setR0130_PRODUCT(String r0130_PRODUCT) {
		R0130_PRODUCT = r0130_PRODUCT;
	}
	public BigDecimal getR0130_NO_ACCT_AED_RESIDENT() {
		return R0130_NO_ACCT_AED_RESIDENT;
	}
	public void setR0130_NO_ACCT_AED_RESIDENT(BigDecimal r0130_NO_ACCT_AED_RESIDENT) {
		R0130_NO_ACCT_AED_RESIDENT = r0130_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0130_AMOUNT_AED_RESIDENT() {
		return R0130_AMOUNT_AED_RESIDENT;
	}
	public void setR0130_AMOUNT_AED_RESIDENT(BigDecimal r0130_AMOUNT_AED_RESIDENT) {
		R0130_AMOUNT_AED_RESIDENT = r0130_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0130_NO_ACCT_FCY_RESIDENT() {
		return R0130_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0130_NO_ACCT_FCY_RESIDENT(BigDecimal r0130_NO_ACCT_FCY_RESIDENT) {
		R0130_NO_ACCT_FCY_RESIDENT = r0130_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0130_AMOUNT_FCY_RESIDENT() {
		return R0130_AMOUNT_FCY_RESIDENT;
	}
	public void setR0130_AMOUNT_FCY_RESIDENT(BigDecimal r0130_AMOUNT_FCY_RESIDENT) {
		R0130_AMOUNT_FCY_RESIDENT = r0130_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0130_NO_ACCT_AED_NON_RESIDENT() {
		return R0130_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0130_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0130_NO_ACCT_AED_NON_RESIDENT) {
		R0130_NO_ACCT_AED_NON_RESIDENT = r0130_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0130_AMOUNT_AED_NON_RESIDENT() {
		return R0130_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0130_AMOUNT_AED_NON_RESIDENT(BigDecimal r0130_AMOUNT_AED_NON_RESIDENT) {
		R0130_AMOUNT_AED_NON_RESIDENT = r0130_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0130_NO_ACCT_FCY_NON_RESIDENT() {
		return R0130_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0130_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0130_NO_ACCT_FCY_NON_RESIDENT) {
		R0130_NO_ACCT_FCY_NON_RESIDENT = r0130_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0130_AMOUNT_FCY_NON_RESIDENT() {
		return R0130_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0130_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0130_AMOUNT_FCY_NON_RESIDENT) {
		R0130_AMOUNT_FCY_NON_RESIDENT = r0130_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0130_TOTAL_AMOUNT() {
		return R0130_TOTAL_AMOUNT;
	}
	public void setR0130_TOTAL_AMOUNT(BigDecimal r0130_TOTAL_AMOUNT) {
		R0130_TOTAL_AMOUNT = r0130_TOTAL_AMOUNT;
	}
	public String getR0140_PRODUCT() {
		return R0140_PRODUCT;
	}
	public void setR0140_PRODUCT(String r0140_PRODUCT) {
		R0140_PRODUCT = r0140_PRODUCT;
	}
	public BigDecimal getR0140_NO_ACCT_AED_RESIDENT() {
		return R0140_NO_ACCT_AED_RESIDENT;
	}
	public void setR0140_NO_ACCT_AED_RESIDENT(BigDecimal r0140_NO_ACCT_AED_RESIDENT) {
		R0140_NO_ACCT_AED_RESIDENT = r0140_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0140_AMOUNT_AED_RESIDENT() {
		return R0140_AMOUNT_AED_RESIDENT;
	}
	public void setR0140_AMOUNT_AED_RESIDENT(BigDecimal r0140_AMOUNT_AED_RESIDENT) {
		R0140_AMOUNT_AED_RESIDENT = r0140_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0140_NO_ACCT_FCY_RESIDENT() {
		return R0140_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0140_NO_ACCT_FCY_RESIDENT(BigDecimal r0140_NO_ACCT_FCY_RESIDENT) {
		R0140_NO_ACCT_FCY_RESIDENT = r0140_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0140_AMOUNT_FCY_RESIDENT() {
		return R0140_AMOUNT_FCY_RESIDENT;
	}
	public void setR0140_AMOUNT_FCY_RESIDENT(BigDecimal r0140_AMOUNT_FCY_RESIDENT) {
		R0140_AMOUNT_FCY_RESIDENT = r0140_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0140_NO_ACCT_AED_NON_RESIDENT() {
		return R0140_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0140_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0140_NO_ACCT_AED_NON_RESIDENT) {
		R0140_NO_ACCT_AED_NON_RESIDENT = r0140_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0140_AMOUNT_AED_NON_RESIDENT() {
		return R0140_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0140_AMOUNT_AED_NON_RESIDENT(BigDecimal r0140_AMOUNT_AED_NON_RESIDENT) {
		R0140_AMOUNT_AED_NON_RESIDENT = r0140_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0140_NO_ACCT_FCY_NON_RESIDENT() {
		return R0140_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0140_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0140_NO_ACCT_FCY_NON_RESIDENT) {
		R0140_NO_ACCT_FCY_NON_RESIDENT = r0140_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0140_AMOUNT_FCY_NON_RESIDENT() {
		return R0140_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0140_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0140_AMOUNT_FCY_NON_RESIDENT) {
		R0140_AMOUNT_FCY_NON_RESIDENT = r0140_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0140_TOTAL_AMOUNT() {
		return R0140_TOTAL_AMOUNT;
	}
	public void setR0140_TOTAL_AMOUNT(BigDecimal r0140_TOTAL_AMOUNT) {
		R0140_TOTAL_AMOUNT = r0140_TOTAL_AMOUNT;
	}
	public String getR0150_PRODUCT() {
		return R0150_PRODUCT;
	}
	public void setR0150_PRODUCT(String r0150_PRODUCT) {
		R0150_PRODUCT = r0150_PRODUCT;
	}
	public BigDecimal getR0150_NO_ACCT_AED_RESIDENT() {
		return R0150_NO_ACCT_AED_RESIDENT;
	}
	public void setR0150_NO_ACCT_AED_RESIDENT(BigDecimal r0150_NO_ACCT_AED_RESIDENT) {
		R0150_NO_ACCT_AED_RESIDENT = r0150_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0150_AMOUNT_AED_RESIDENT() {
		return R0150_AMOUNT_AED_RESIDENT;
	}
	public void setR0150_AMOUNT_AED_RESIDENT(BigDecimal r0150_AMOUNT_AED_RESIDENT) {
		R0150_AMOUNT_AED_RESIDENT = r0150_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0150_NO_ACCT_FCY_RESIDENT() {
		return R0150_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0150_NO_ACCT_FCY_RESIDENT(BigDecimal r0150_NO_ACCT_FCY_RESIDENT) {
		R0150_NO_ACCT_FCY_RESIDENT = r0150_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0150_AMOUNT_FCY_RESIDENT() {
		return R0150_AMOUNT_FCY_RESIDENT;
	}
	public void setR0150_AMOUNT_FCY_RESIDENT(BigDecimal r0150_AMOUNT_FCY_RESIDENT) {
		R0150_AMOUNT_FCY_RESIDENT = r0150_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0150_NO_ACCT_AED_NON_RESIDENT() {
		return R0150_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0150_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0150_NO_ACCT_AED_NON_RESIDENT) {
		R0150_NO_ACCT_AED_NON_RESIDENT = r0150_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0150_AMOUNT_AED_NON_RESIDENT() {
		return R0150_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0150_AMOUNT_AED_NON_RESIDENT(BigDecimal r0150_AMOUNT_AED_NON_RESIDENT) {
		R0150_AMOUNT_AED_NON_RESIDENT = r0150_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0150_NO_ACCT_FCY_NON_RESIDENT() {
		return R0150_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0150_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0150_NO_ACCT_FCY_NON_RESIDENT) {
		R0150_NO_ACCT_FCY_NON_RESIDENT = r0150_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0150_AMOUNT_FCY_NON_RESIDENT() {
		return R0150_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0150_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0150_AMOUNT_FCY_NON_RESIDENT) {
		R0150_AMOUNT_FCY_NON_RESIDENT = r0150_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0150_TOTAL_AMOUNT() {
		return R0150_TOTAL_AMOUNT;
	}
	public void setR0150_TOTAL_AMOUNT(BigDecimal r0150_TOTAL_AMOUNT) {
		R0150_TOTAL_AMOUNT = r0150_TOTAL_AMOUNT;
	}
	public String getR0160_PRODUCT() {
		return R0160_PRODUCT;
	}
	public void setR0160_PRODUCT(String r0160_PRODUCT) {
		R0160_PRODUCT = r0160_PRODUCT;
	}
	public BigDecimal getR0160_NO_ACCT_AED_RESIDENT() {
		return R0160_NO_ACCT_AED_RESIDENT;
	}
	public void setR0160_NO_ACCT_AED_RESIDENT(BigDecimal r0160_NO_ACCT_AED_RESIDENT) {
		R0160_NO_ACCT_AED_RESIDENT = r0160_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0160_AMOUNT_AED_RESIDENT() {
		return R0160_AMOUNT_AED_RESIDENT;
	}
	public void setR0160_AMOUNT_AED_RESIDENT(BigDecimal r0160_AMOUNT_AED_RESIDENT) {
		R0160_AMOUNT_AED_RESIDENT = r0160_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0160_NO_ACCT_FCY_RESIDENT() {
		return R0160_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0160_NO_ACCT_FCY_RESIDENT(BigDecimal r0160_NO_ACCT_FCY_RESIDENT) {
		R0160_NO_ACCT_FCY_RESIDENT = r0160_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0160_AMOUNT_FCY_RESIDENT() {
		return R0160_AMOUNT_FCY_RESIDENT;
	}
	public void setR0160_AMOUNT_FCY_RESIDENT(BigDecimal r0160_AMOUNT_FCY_RESIDENT) {
		R0160_AMOUNT_FCY_RESIDENT = r0160_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0160_NO_ACCT_AED_NON_RESIDENT() {
		return R0160_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0160_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0160_NO_ACCT_AED_NON_RESIDENT) {
		R0160_NO_ACCT_AED_NON_RESIDENT = r0160_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0160_AMOUNT_AED_NON_RESIDENT() {
		return R0160_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0160_AMOUNT_AED_NON_RESIDENT(BigDecimal r0160_AMOUNT_AED_NON_RESIDENT) {
		R0160_AMOUNT_AED_NON_RESIDENT = r0160_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0160_NO_ACCT_FCY_NON_RESIDENT() {
		return R0160_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0160_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0160_NO_ACCT_FCY_NON_RESIDENT) {
		R0160_NO_ACCT_FCY_NON_RESIDENT = r0160_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0160_AMOUNT_FCY_NON_RESIDENT() {
		return R0160_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0160_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0160_AMOUNT_FCY_NON_RESIDENT) {
		R0160_AMOUNT_FCY_NON_RESIDENT = r0160_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0160_TOTAL_AMOUNT() {
		return R0160_TOTAL_AMOUNT;
	}
	public void setR0160_TOTAL_AMOUNT(BigDecimal r0160_TOTAL_AMOUNT) {
		R0160_TOTAL_AMOUNT = r0160_TOTAL_AMOUNT;
	}
	public String getR0170_PRODUCT() {
		return R0170_PRODUCT;
	}
	public void setR0170_PRODUCT(String r0170_PRODUCT) {
		R0170_PRODUCT = r0170_PRODUCT;
	}
	public BigDecimal getR0170_NO_ACCT_AED_RESIDENT() {
		return R0170_NO_ACCT_AED_RESIDENT;
	}
	public void setR0170_NO_ACCT_AED_RESIDENT(BigDecimal r0170_NO_ACCT_AED_RESIDENT) {
		R0170_NO_ACCT_AED_RESIDENT = r0170_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0170_AMOUNT_AED_RESIDENT() {
		return R0170_AMOUNT_AED_RESIDENT;
	}
	public void setR0170_AMOUNT_AED_RESIDENT(BigDecimal r0170_AMOUNT_AED_RESIDENT) {
		R0170_AMOUNT_AED_RESIDENT = r0170_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0170_NO_ACCT_FCY_RESIDENT() {
		return R0170_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0170_NO_ACCT_FCY_RESIDENT(BigDecimal r0170_NO_ACCT_FCY_RESIDENT) {
		R0170_NO_ACCT_FCY_RESIDENT = r0170_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0170_AMOUNT_FCY_RESIDENT() {
		return R0170_AMOUNT_FCY_RESIDENT;
	}
	public void setR0170_AMOUNT_FCY_RESIDENT(BigDecimal r0170_AMOUNT_FCY_RESIDENT) {
		R0170_AMOUNT_FCY_RESIDENT = r0170_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0170_NO_ACCT_AED_NON_RESIDENT() {
		return R0170_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0170_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0170_NO_ACCT_AED_NON_RESIDENT) {
		R0170_NO_ACCT_AED_NON_RESIDENT = r0170_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0170_AMOUNT_AED_NON_RESIDENT() {
		return R0170_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0170_AMOUNT_AED_NON_RESIDENT(BigDecimal r0170_AMOUNT_AED_NON_RESIDENT) {
		R0170_AMOUNT_AED_NON_RESIDENT = r0170_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0170_NO_ACCT_FCY_NON_RESIDENT() {
		return R0170_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0170_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0170_NO_ACCT_FCY_NON_RESIDENT) {
		R0170_NO_ACCT_FCY_NON_RESIDENT = r0170_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0170_AMOUNT_FCY_NON_RESIDENT() {
		return R0170_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0170_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0170_AMOUNT_FCY_NON_RESIDENT) {
		R0170_AMOUNT_FCY_NON_RESIDENT = r0170_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0170_TOTAL_AMOUNT() {
		return R0170_TOTAL_AMOUNT;
	}
	public void setR0170_TOTAL_AMOUNT(BigDecimal r0170_TOTAL_AMOUNT) {
		R0170_TOTAL_AMOUNT = r0170_TOTAL_AMOUNT;
	}
	public String getR0180_PRODUCT() {
		return R0180_PRODUCT;
	}
	public void setR0180_PRODUCT(String r0180_PRODUCT) {
		R0180_PRODUCT = r0180_PRODUCT;
	}
	public BigDecimal getR0180_NO_ACCT_AED_RESIDENT() {
		return R0180_NO_ACCT_AED_RESIDENT;
	}
	public void setR0180_NO_ACCT_AED_RESIDENT(BigDecimal r0180_NO_ACCT_AED_RESIDENT) {
		R0180_NO_ACCT_AED_RESIDENT = r0180_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0180_AMOUNT_AED_RESIDENT() {
		return R0180_AMOUNT_AED_RESIDENT;
	}
	public void setR0180_AMOUNT_AED_RESIDENT(BigDecimal r0180_AMOUNT_AED_RESIDENT) {
		R0180_AMOUNT_AED_RESIDENT = r0180_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0180_NO_ACCT_FCY_RESIDENT() {
		return R0180_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0180_NO_ACCT_FCY_RESIDENT(BigDecimal r0180_NO_ACCT_FCY_RESIDENT) {
		R0180_NO_ACCT_FCY_RESIDENT = r0180_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0180_AMOUNT_FCY_RESIDENT() {
		return R0180_AMOUNT_FCY_RESIDENT;
	}
	public void setR0180_AMOUNT_FCY_RESIDENT(BigDecimal r0180_AMOUNT_FCY_RESIDENT) {
		R0180_AMOUNT_FCY_RESIDENT = r0180_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0180_NO_ACCT_AED_NON_RESIDENT() {
		return R0180_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0180_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0180_NO_ACCT_AED_NON_RESIDENT) {
		R0180_NO_ACCT_AED_NON_RESIDENT = r0180_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0180_AMOUNT_AED_NON_RESIDENT() {
		return R0180_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0180_AMOUNT_AED_NON_RESIDENT(BigDecimal r0180_AMOUNT_AED_NON_RESIDENT) {
		R0180_AMOUNT_AED_NON_RESIDENT = r0180_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0180_NO_ACCT_FCY_NON_RESIDENT() {
		return R0180_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0180_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0180_NO_ACCT_FCY_NON_RESIDENT) {
		R0180_NO_ACCT_FCY_NON_RESIDENT = r0180_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0180_AMOUNT_FCY_NON_RESIDENT() {
		return R0180_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0180_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0180_AMOUNT_FCY_NON_RESIDENT) {
		R0180_AMOUNT_FCY_NON_RESIDENT = r0180_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0180_TOTAL_AMOUNT() {
		return R0180_TOTAL_AMOUNT;
	}
	public void setR0180_TOTAL_AMOUNT(BigDecimal r0180_TOTAL_AMOUNT) {
		R0180_TOTAL_AMOUNT = r0180_TOTAL_AMOUNT;
	}
	public String getR0190_PRODUCT() {
		return R0190_PRODUCT;
	}
	public void setR0190_PRODUCT(String r0190_PRODUCT) {
		R0190_PRODUCT = r0190_PRODUCT;
	}
	public BigDecimal getR0190_NO_ACCT_AED_RESIDENT() {
		return R0190_NO_ACCT_AED_RESIDENT;
	}
	public void setR0190_NO_ACCT_AED_RESIDENT(BigDecimal r0190_NO_ACCT_AED_RESIDENT) {
		R0190_NO_ACCT_AED_RESIDENT = r0190_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0190_AMOUNT_AED_RESIDENT() {
		return R0190_AMOUNT_AED_RESIDENT;
	}
	public void setR0190_AMOUNT_AED_RESIDENT(BigDecimal r0190_AMOUNT_AED_RESIDENT) {
		R0190_AMOUNT_AED_RESIDENT = r0190_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0190_NO_ACCT_FCY_RESIDENT() {
		return R0190_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0190_NO_ACCT_FCY_RESIDENT(BigDecimal r0190_NO_ACCT_FCY_RESIDENT) {
		R0190_NO_ACCT_FCY_RESIDENT = r0190_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0190_AMOUNT_FCY_RESIDENT() {
		return R0190_AMOUNT_FCY_RESIDENT;
	}
	public void setR0190_AMOUNT_FCY_RESIDENT(BigDecimal r0190_AMOUNT_FCY_RESIDENT) {
		R0190_AMOUNT_FCY_RESIDENT = r0190_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0190_NO_ACCT_AED_NON_RESIDENT() {
		return R0190_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0190_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0190_NO_ACCT_AED_NON_RESIDENT) {
		R0190_NO_ACCT_AED_NON_RESIDENT = r0190_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0190_AMOUNT_AED_NON_RESIDENT() {
		return R0190_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0190_AMOUNT_AED_NON_RESIDENT(BigDecimal r0190_AMOUNT_AED_NON_RESIDENT) {
		R0190_AMOUNT_AED_NON_RESIDENT = r0190_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0190_NO_ACCT_FCY_NON_RESIDENT() {
		return R0190_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0190_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0190_NO_ACCT_FCY_NON_RESIDENT) {
		R0190_NO_ACCT_FCY_NON_RESIDENT = r0190_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0190_AMOUNT_FCY_NON_RESIDENT() {
		return R0190_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0190_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0190_AMOUNT_FCY_NON_RESIDENT) {
		R0190_AMOUNT_FCY_NON_RESIDENT = r0190_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0190_TOTAL_AMOUNT() {
		return R0190_TOTAL_AMOUNT;
	}
	public void setR0190_TOTAL_AMOUNT(BigDecimal r0190_TOTAL_AMOUNT) {
		R0190_TOTAL_AMOUNT = r0190_TOTAL_AMOUNT;
	}
	public String getR0200_PRODUCT() {
		return R0200_PRODUCT;
	}
	public void setR0200_PRODUCT(String r0200_PRODUCT) {
		R0200_PRODUCT = r0200_PRODUCT;
	}
	public BigDecimal getR0200_NO_ACCT_AED_RESIDENT() {
		return R0200_NO_ACCT_AED_RESIDENT;
	}
	public void setR0200_NO_ACCT_AED_RESIDENT(BigDecimal r0200_NO_ACCT_AED_RESIDENT) {
		R0200_NO_ACCT_AED_RESIDENT = r0200_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0200_AMOUNT_AED_RESIDENT() {
		return R0200_AMOUNT_AED_RESIDENT;
	}
	public void setR0200_AMOUNT_AED_RESIDENT(BigDecimal r0200_AMOUNT_AED_RESIDENT) {
		R0200_AMOUNT_AED_RESIDENT = r0200_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0200_NO_ACCT_FCY_RESIDENT() {
		return R0200_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0200_NO_ACCT_FCY_RESIDENT(BigDecimal r0200_NO_ACCT_FCY_RESIDENT) {
		R0200_NO_ACCT_FCY_RESIDENT = r0200_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0200_AMOUNT_FCY_RESIDENT() {
		return R0200_AMOUNT_FCY_RESIDENT;
	}
	public void setR0200_AMOUNT_FCY_RESIDENT(BigDecimal r0200_AMOUNT_FCY_RESIDENT) {
		R0200_AMOUNT_FCY_RESIDENT = r0200_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0200_NO_ACCT_AED_NON_RESIDENT() {
		return R0200_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0200_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0200_NO_ACCT_AED_NON_RESIDENT) {
		R0200_NO_ACCT_AED_NON_RESIDENT = r0200_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0200_AMOUNT_AED_NON_RESIDENT() {
		return R0200_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0200_AMOUNT_AED_NON_RESIDENT(BigDecimal r0200_AMOUNT_AED_NON_RESIDENT) {
		R0200_AMOUNT_AED_NON_RESIDENT = r0200_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0200_NO_ACCT_FCY_NON_RESIDENT() {
		return R0200_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0200_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0200_NO_ACCT_FCY_NON_RESIDENT) {
		R0200_NO_ACCT_FCY_NON_RESIDENT = r0200_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0200_AMOUNT_FCY_NON_RESIDENT() {
		return R0200_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0200_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0200_AMOUNT_FCY_NON_RESIDENT) {
		R0200_AMOUNT_FCY_NON_RESIDENT = r0200_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0200_TOTAL_AMOUNT() {
		return R0200_TOTAL_AMOUNT;
	}
	public void setR0200_TOTAL_AMOUNT(BigDecimal r0200_TOTAL_AMOUNT) {
		R0200_TOTAL_AMOUNT = r0200_TOTAL_AMOUNT;
	}
	public String getR0210_PRODUCT() {
		return R0210_PRODUCT;
	}
	public void setR0210_PRODUCT(String r0210_PRODUCT) {
		R0210_PRODUCT = r0210_PRODUCT;
	}
	public BigDecimal getR0210_NO_ACCT_AED_RESIDENT() {
		return R0210_NO_ACCT_AED_RESIDENT;
	}
	public void setR0210_NO_ACCT_AED_RESIDENT(BigDecimal r0210_NO_ACCT_AED_RESIDENT) {
		R0210_NO_ACCT_AED_RESIDENT = r0210_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0210_AMOUNT_AED_RESIDENT() {
		return R0210_AMOUNT_AED_RESIDENT;
	}
	public void setR0210_AMOUNT_AED_RESIDENT(BigDecimal r0210_AMOUNT_AED_RESIDENT) {
		R0210_AMOUNT_AED_RESIDENT = r0210_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0210_NO_ACCT_FCY_RESIDENT() {
		return R0210_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0210_NO_ACCT_FCY_RESIDENT(BigDecimal r0210_NO_ACCT_FCY_RESIDENT) {
		R0210_NO_ACCT_FCY_RESIDENT = r0210_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0210_AMOUNT_FCY_RESIDENT() {
		return R0210_AMOUNT_FCY_RESIDENT;
	}
	public void setR0210_AMOUNT_FCY_RESIDENT(BigDecimal r0210_AMOUNT_FCY_RESIDENT) {
		R0210_AMOUNT_FCY_RESIDENT = r0210_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0210_NO_ACCT_AED_NON_RESIDENT() {
		return R0210_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0210_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0210_NO_ACCT_AED_NON_RESIDENT) {
		R0210_NO_ACCT_AED_NON_RESIDENT = r0210_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0210_AMOUNT_AED_NON_RESIDENT() {
		return R0210_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0210_AMOUNT_AED_NON_RESIDENT(BigDecimal r0210_AMOUNT_AED_NON_RESIDENT) {
		R0210_AMOUNT_AED_NON_RESIDENT = r0210_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0210_NO_ACCT_FCY_NON_RESIDENT() {
		return R0210_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0210_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0210_NO_ACCT_FCY_NON_RESIDENT) {
		R0210_NO_ACCT_FCY_NON_RESIDENT = r0210_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0210_AMOUNT_FCY_NON_RESIDENT() {
		return R0210_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0210_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0210_AMOUNT_FCY_NON_RESIDENT) {
		R0210_AMOUNT_FCY_NON_RESIDENT = r0210_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0210_TOTAL_AMOUNT() {
		return R0210_TOTAL_AMOUNT;
	}
	public void setR0210_TOTAL_AMOUNT(BigDecimal r0210_TOTAL_AMOUNT) {
		R0210_TOTAL_AMOUNT = r0210_TOTAL_AMOUNT;
	}
	public String getR0220_PRODUCT() {
		return R0220_PRODUCT;
	}
	public void setR0220_PRODUCT(String r0220_PRODUCT) {
		R0220_PRODUCT = r0220_PRODUCT;
	}
	public BigDecimal getR0220_NO_ACCT_AED_RESIDENT() {
		return R0220_NO_ACCT_AED_RESIDENT;
	}
	public void setR0220_NO_ACCT_AED_RESIDENT(BigDecimal r0220_NO_ACCT_AED_RESIDENT) {
		R0220_NO_ACCT_AED_RESIDENT = r0220_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0220_AMOUNT_AED_RESIDENT() {
		return R0220_AMOUNT_AED_RESIDENT;
	}
	public void setR0220_AMOUNT_AED_RESIDENT(BigDecimal r0220_AMOUNT_AED_RESIDENT) {
		R0220_AMOUNT_AED_RESIDENT = r0220_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0220_NO_ACCT_FCY_RESIDENT() {
		return R0220_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0220_NO_ACCT_FCY_RESIDENT(BigDecimal r0220_NO_ACCT_FCY_RESIDENT) {
		R0220_NO_ACCT_FCY_RESIDENT = r0220_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0220_AMOUNT_FCY_RESIDENT() {
		return R0220_AMOUNT_FCY_RESIDENT;
	}
	public void setR0220_AMOUNT_FCY_RESIDENT(BigDecimal r0220_AMOUNT_FCY_RESIDENT) {
		R0220_AMOUNT_FCY_RESIDENT = r0220_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0220_NO_ACCT_AED_NON_RESIDENT() {
		return R0220_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0220_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0220_NO_ACCT_AED_NON_RESIDENT) {
		R0220_NO_ACCT_AED_NON_RESIDENT = r0220_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0220_AMOUNT_AED_NON_RESIDENT() {
		return R0220_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0220_AMOUNT_AED_NON_RESIDENT(BigDecimal r0220_AMOUNT_AED_NON_RESIDENT) {
		R0220_AMOUNT_AED_NON_RESIDENT = r0220_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0220_NO_ACCT_FCY_NON_RESIDENT() {
		return R0220_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0220_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0220_NO_ACCT_FCY_NON_RESIDENT) {
		R0220_NO_ACCT_FCY_NON_RESIDENT = r0220_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0220_AMOUNT_FCY_NON_RESIDENT() {
		return R0220_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0220_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0220_AMOUNT_FCY_NON_RESIDENT) {
		R0220_AMOUNT_FCY_NON_RESIDENT = r0220_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0220_TOTAL_AMOUNT() {
		return R0220_TOTAL_AMOUNT;
	}
	public void setR0220_TOTAL_AMOUNT(BigDecimal r0220_TOTAL_AMOUNT) {
		R0220_TOTAL_AMOUNT = r0220_TOTAL_AMOUNT;
	}
	public String getR0230_PRODUCT() {
		return R0230_PRODUCT;
	}
	public void setR0230_PRODUCT(String r0230_PRODUCT) {
		R0230_PRODUCT = r0230_PRODUCT;
	}
	public BigDecimal getR0230_NO_ACCT_AED_RESIDENT() {
		return R0230_NO_ACCT_AED_RESIDENT;
	}
	public void setR0230_NO_ACCT_AED_RESIDENT(BigDecimal r0230_NO_ACCT_AED_RESIDENT) {
		R0230_NO_ACCT_AED_RESIDENT = r0230_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0230_AMOUNT_AED_RESIDENT() {
		return R0230_AMOUNT_AED_RESIDENT;
	}
	public void setR0230_AMOUNT_AED_RESIDENT(BigDecimal r0230_AMOUNT_AED_RESIDENT) {
		R0230_AMOUNT_AED_RESIDENT = r0230_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0230_NO_ACCT_FCY_RESIDENT() {
		return R0230_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0230_NO_ACCT_FCY_RESIDENT(BigDecimal r0230_NO_ACCT_FCY_RESIDENT) {
		R0230_NO_ACCT_FCY_RESIDENT = r0230_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0230_AMOUNT_FCY_RESIDENT() {
		return R0230_AMOUNT_FCY_RESIDENT;
	}
	public void setR0230_AMOUNT_FCY_RESIDENT(BigDecimal r0230_AMOUNT_FCY_RESIDENT) {
		R0230_AMOUNT_FCY_RESIDENT = r0230_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0230_NO_ACCT_AED_NON_RESIDENT() {
		return R0230_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0230_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0230_NO_ACCT_AED_NON_RESIDENT) {
		R0230_NO_ACCT_AED_NON_RESIDENT = r0230_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0230_AMOUNT_AED_NON_RESIDENT() {
		return R0230_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0230_AMOUNT_AED_NON_RESIDENT(BigDecimal r0230_AMOUNT_AED_NON_RESIDENT) {
		R0230_AMOUNT_AED_NON_RESIDENT = r0230_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0230_NO_ACCT_FCY_NON_RESIDENT() {
		return R0230_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0230_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0230_NO_ACCT_FCY_NON_RESIDENT) {
		R0230_NO_ACCT_FCY_NON_RESIDENT = r0230_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0230_AMOUNT_FCY_NON_RESIDENT() {
		return R0230_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0230_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0230_AMOUNT_FCY_NON_RESIDENT) {
		R0230_AMOUNT_FCY_NON_RESIDENT = r0230_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0230_TOTAL_AMOUNT() {
		return R0230_TOTAL_AMOUNT;
	}
	public void setR0230_TOTAL_AMOUNT(BigDecimal r0230_TOTAL_AMOUNT) {
		R0230_TOTAL_AMOUNT = r0230_TOTAL_AMOUNT;
	}
	public String getR0240_PRODUCT() {
		return R0240_PRODUCT;
	}
	public void setR0240_PRODUCT(String r0240_PRODUCT) {
		R0240_PRODUCT = r0240_PRODUCT;
	}
	public BigDecimal getR0240_NO_ACCT_AED_RESIDENT() {
		return R0240_NO_ACCT_AED_RESIDENT;
	}
	public void setR0240_NO_ACCT_AED_RESIDENT(BigDecimal r0240_NO_ACCT_AED_RESIDENT) {
		R0240_NO_ACCT_AED_RESIDENT = r0240_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0240_AMOUNT_AED_RESIDENT() {
		return R0240_AMOUNT_AED_RESIDENT;
	}
	public void setR0240_AMOUNT_AED_RESIDENT(BigDecimal r0240_AMOUNT_AED_RESIDENT) {
		R0240_AMOUNT_AED_RESIDENT = r0240_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0240_NO_ACCT_FCY_RESIDENT() {
		return R0240_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0240_NO_ACCT_FCY_RESIDENT(BigDecimal r0240_NO_ACCT_FCY_RESIDENT) {
		R0240_NO_ACCT_FCY_RESIDENT = r0240_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0240_AMOUNT_FCY_RESIDENT() {
		return R0240_AMOUNT_FCY_RESIDENT;
	}
	public void setR0240_AMOUNT_FCY_RESIDENT(BigDecimal r0240_AMOUNT_FCY_RESIDENT) {
		R0240_AMOUNT_FCY_RESIDENT = r0240_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0240_NO_ACCT_AED_NON_RESIDENT() {
		return R0240_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0240_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0240_NO_ACCT_AED_NON_RESIDENT) {
		R0240_NO_ACCT_AED_NON_RESIDENT = r0240_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0240_AMOUNT_AED_NON_RESIDENT() {
		return R0240_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0240_AMOUNT_AED_NON_RESIDENT(BigDecimal r0240_AMOUNT_AED_NON_RESIDENT) {
		R0240_AMOUNT_AED_NON_RESIDENT = r0240_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0240_NO_ACCT_FCY_NON_RESIDENT() {
		return R0240_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0240_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0240_NO_ACCT_FCY_NON_RESIDENT) {
		R0240_NO_ACCT_FCY_NON_RESIDENT = r0240_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0240_AMOUNT_FCY_NON_RESIDENT() {
		return R0240_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0240_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0240_AMOUNT_FCY_NON_RESIDENT) {
		R0240_AMOUNT_FCY_NON_RESIDENT = r0240_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0240_TOTAL_AMOUNT() {
		return R0240_TOTAL_AMOUNT;
	}
	public void setR0240_TOTAL_AMOUNT(BigDecimal r0240_TOTAL_AMOUNT) {
		R0240_TOTAL_AMOUNT = r0240_TOTAL_AMOUNT;
	}
	public String getR0250_PRODUCT() {
		return R0250_PRODUCT;
	}
	public void setR0250_PRODUCT(String r0250_PRODUCT) {
		R0250_PRODUCT = r0250_PRODUCT;
	}
	public BigDecimal getR0250_NO_ACCT_AED_RESIDENT() {
		return R0250_NO_ACCT_AED_RESIDENT;
	}
	public void setR0250_NO_ACCT_AED_RESIDENT(BigDecimal r0250_NO_ACCT_AED_RESIDENT) {
		R0250_NO_ACCT_AED_RESIDENT = r0250_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0250_AMOUNT_AED_RESIDENT() {
		return R0250_AMOUNT_AED_RESIDENT;
	}
	public void setR0250_AMOUNT_AED_RESIDENT(BigDecimal r0250_AMOUNT_AED_RESIDENT) {
		R0250_AMOUNT_AED_RESIDENT = r0250_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0250_NO_ACCT_FCY_RESIDENT() {
		return R0250_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0250_NO_ACCT_FCY_RESIDENT(BigDecimal r0250_NO_ACCT_FCY_RESIDENT) {
		R0250_NO_ACCT_FCY_RESIDENT = r0250_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0250_AMOUNT_FCY_RESIDENT() {
		return R0250_AMOUNT_FCY_RESIDENT;
	}
	public void setR0250_AMOUNT_FCY_RESIDENT(BigDecimal r0250_AMOUNT_FCY_RESIDENT) {
		R0250_AMOUNT_FCY_RESIDENT = r0250_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0250_NO_ACCT_AED_NON_RESIDENT() {
		return R0250_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0250_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0250_NO_ACCT_AED_NON_RESIDENT) {
		R0250_NO_ACCT_AED_NON_RESIDENT = r0250_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0250_AMOUNT_AED_NON_RESIDENT() {
		return R0250_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0250_AMOUNT_AED_NON_RESIDENT(BigDecimal r0250_AMOUNT_AED_NON_RESIDENT) {
		R0250_AMOUNT_AED_NON_RESIDENT = r0250_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0250_NO_ACCT_FCY_NON_RESIDENT() {
		return R0250_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0250_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0250_NO_ACCT_FCY_NON_RESIDENT) {
		R0250_NO_ACCT_FCY_NON_RESIDENT = r0250_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0250_AMOUNT_FCY_NON_RESIDENT() {
		return R0250_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0250_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0250_AMOUNT_FCY_NON_RESIDENT) {
		R0250_AMOUNT_FCY_NON_RESIDENT = r0250_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0250_TOTAL_AMOUNT() {
		return R0250_TOTAL_AMOUNT;
	}
	public void setR0250_TOTAL_AMOUNT(BigDecimal r0250_TOTAL_AMOUNT) {
		R0250_TOTAL_AMOUNT = r0250_TOTAL_AMOUNT;
	}
	public String getR0260_PRODUCT() {
		return R0260_PRODUCT;
	}
	public void setR0260_PRODUCT(String r0260_PRODUCT) {
		R0260_PRODUCT = r0260_PRODUCT;
	}
	public BigDecimal getR0260_NO_ACCT_AED_RESIDENT() {
		return R0260_NO_ACCT_AED_RESIDENT;
	}
	public void setR0260_NO_ACCT_AED_RESIDENT(BigDecimal r0260_NO_ACCT_AED_RESIDENT) {
		R0260_NO_ACCT_AED_RESIDENT = r0260_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0260_AMOUNT_AED_RESIDENT() {
		return R0260_AMOUNT_AED_RESIDENT;
	}
	public void setR0260_AMOUNT_AED_RESIDENT(BigDecimal r0260_AMOUNT_AED_RESIDENT) {
		R0260_AMOUNT_AED_RESIDENT = r0260_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0260_NO_ACCT_FCY_RESIDENT() {
		return R0260_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0260_NO_ACCT_FCY_RESIDENT(BigDecimal r0260_NO_ACCT_FCY_RESIDENT) {
		R0260_NO_ACCT_FCY_RESIDENT = r0260_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0260_AMOUNT_FCY_RESIDENT() {
		return R0260_AMOUNT_FCY_RESIDENT;
	}
	public void setR0260_AMOUNT_FCY_RESIDENT(BigDecimal r0260_AMOUNT_FCY_RESIDENT) {
		R0260_AMOUNT_FCY_RESIDENT = r0260_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0260_NO_ACCT_AED_NON_RESIDENT() {
		return R0260_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0260_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0260_NO_ACCT_AED_NON_RESIDENT) {
		R0260_NO_ACCT_AED_NON_RESIDENT = r0260_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0260_AMOUNT_AED_NON_RESIDENT() {
		return R0260_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0260_AMOUNT_AED_NON_RESIDENT(BigDecimal r0260_AMOUNT_AED_NON_RESIDENT) {
		R0260_AMOUNT_AED_NON_RESIDENT = r0260_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0260_NO_ACCT_FCY_NON_RESIDENT() {
		return R0260_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0260_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0260_NO_ACCT_FCY_NON_RESIDENT) {
		R0260_NO_ACCT_FCY_NON_RESIDENT = r0260_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0260_AMOUNT_FCY_NON_RESIDENT() {
		return R0260_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0260_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0260_AMOUNT_FCY_NON_RESIDENT) {
		R0260_AMOUNT_FCY_NON_RESIDENT = r0260_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0260_TOTAL_AMOUNT() {
		return R0260_TOTAL_AMOUNT;
	}
	public void setR0260_TOTAL_AMOUNT(BigDecimal r0260_TOTAL_AMOUNT) {
		R0260_TOTAL_AMOUNT = r0260_TOTAL_AMOUNT;
	}
	public String getR0270_PRODUCT() {
		return R0270_PRODUCT;
	}
	public void setR0270_PRODUCT(String r0270_PRODUCT) {
		R0270_PRODUCT = r0270_PRODUCT;
	}
	public BigDecimal getR0270_NO_ACCT_AED_RESIDENT() {
		return R0270_NO_ACCT_AED_RESIDENT;
	}
	public void setR0270_NO_ACCT_AED_RESIDENT(BigDecimal r0270_NO_ACCT_AED_RESIDENT) {
		R0270_NO_ACCT_AED_RESIDENT = r0270_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0270_AMOUNT_AED_RESIDENT() {
		return R0270_AMOUNT_AED_RESIDENT;
	}
	public void setR0270_AMOUNT_AED_RESIDENT(BigDecimal r0270_AMOUNT_AED_RESIDENT) {
		R0270_AMOUNT_AED_RESIDENT = r0270_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0270_NO_ACCT_FCY_RESIDENT() {
		return R0270_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0270_NO_ACCT_FCY_RESIDENT(BigDecimal r0270_NO_ACCT_FCY_RESIDENT) {
		R0270_NO_ACCT_FCY_RESIDENT = r0270_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0270_AMOUNT_FCY_RESIDENT() {
		return R0270_AMOUNT_FCY_RESIDENT;
	}
	public void setR0270_AMOUNT_FCY_RESIDENT(BigDecimal r0270_AMOUNT_FCY_RESIDENT) {
		R0270_AMOUNT_FCY_RESIDENT = r0270_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0270_NO_ACCT_AED_NON_RESIDENT() {
		return R0270_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0270_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0270_NO_ACCT_AED_NON_RESIDENT) {
		R0270_NO_ACCT_AED_NON_RESIDENT = r0270_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0270_AMOUNT_AED_NON_RESIDENT() {
		return R0270_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0270_AMOUNT_AED_NON_RESIDENT(BigDecimal r0270_AMOUNT_AED_NON_RESIDENT) {
		R0270_AMOUNT_AED_NON_RESIDENT = r0270_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0270_NO_ACCT_FCY_NON_RESIDENT() {
		return R0270_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0270_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0270_NO_ACCT_FCY_NON_RESIDENT) {
		R0270_NO_ACCT_FCY_NON_RESIDENT = r0270_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0270_AMOUNT_FCY_NON_RESIDENT() {
		return R0270_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0270_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0270_AMOUNT_FCY_NON_RESIDENT) {
		R0270_AMOUNT_FCY_NON_RESIDENT = r0270_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0270_TOTAL_AMOUNT() {
		return R0270_TOTAL_AMOUNT;
	}
	public void setR0270_TOTAL_AMOUNT(BigDecimal r0270_TOTAL_AMOUNT) {
		R0270_TOTAL_AMOUNT = r0270_TOTAL_AMOUNT;
	}
	public String getR0280_PRODUCT() {
		return R0280_PRODUCT;
	}
	public void setR0280_PRODUCT(String r0280_PRODUCT) {
		R0280_PRODUCT = r0280_PRODUCT;
	}
	public BigDecimal getR0280_NO_ACCT_AED_RESIDENT() {
		return R0280_NO_ACCT_AED_RESIDENT;
	}
	public void setR0280_NO_ACCT_AED_RESIDENT(BigDecimal r0280_NO_ACCT_AED_RESIDENT) {
		R0280_NO_ACCT_AED_RESIDENT = r0280_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0280_AMOUNT_AED_RESIDENT() {
		return R0280_AMOUNT_AED_RESIDENT;
	}
	public void setR0280_AMOUNT_AED_RESIDENT(BigDecimal r0280_AMOUNT_AED_RESIDENT) {
		R0280_AMOUNT_AED_RESIDENT = r0280_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0280_NO_ACCT_FCY_RESIDENT() {
		return R0280_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0280_NO_ACCT_FCY_RESIDENT(BigDecimal r0280_NO_ACCT_FCY_RESIDENT) {
		R0280_NO_ACCT_FCY_RESIDENT = r0280_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0280_AMOUNT_FCY_RESIDENT() {
		return R0280_AMOUNT_FCY_RESIDENT;
	}
	public void setR0280_AMOUNT_FCY_RESIDENT(BigDecimal r0280_AMOUNT_FCY_RESIDENT) {
		R0280_AMOUNT_FCY_RESIDENT = r0280_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0280_NO_ACCT_AED_NON_RESIDENT() {
		return R0280_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0280_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0280_NO_ACCT_AED_NON_RESIDENT) {
		R0280_NO_ACCT_AED_NON_RESIDENT = r0280_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0280_AMOUNT_AED_NON_RESIDENT() {
		return R0280_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0280_AMOUNT_AED_NON_RESIDENT(BigDecimal r0280_AMOUNT_AED_NON_RESIDENT) {
		R0280_AMOUNT_AED_NON_RESIDENT = r0280_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0280_NO_ACCT_FCY_NON_RESIDENT() {
		return R0280_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0280_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0280_NO_ACCT_FCY_NON_RESIDENT) {
		R0280_NO_ACCT_FCY_NON_RESIDENT = r0280_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0280_AMOUNT_FCY_NON_RESIDENT() {
		return R0280_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0280_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0280_AMOUNT_FCY_NON_RESIDENT) {
		R0280_AMOUNT_FCY_NON_RESIDENT = r0280_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0280_TOTAL_AMOUNT() {
		return R0280_TOTAL_AMOUNT;
	}
	public void setR0280_TOTAL_AMOUNT(BigDecimal r0280_TOTAL_AMOUNT) {
		R0280_TOTAL_AMOUNT = r0280_TOTAL_AMOUNT;
	}
	public String getR0290_PRODUCT() {
		return R0290_PRODUCT;
	}
	public void setR0290_PRODUCT(String r0290_PRODUCT) {
		R0290_PRODUCT = r0290_PRODUCT;
	}
	public BigDecimal getR0290_NO_ACCT_AED_RESIDENT() {
		return R0290_NO_ACCT_AED_RESIDENT;
	}
	public void setR0290_NO_ACCT_AED_RESIDENT(BigDecimal r0290_NO_ACCT_AED_RESIDENT) {
		R0290_NO_ACCT_AED_RESIDENT = r0290_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0290_AMOUNT_AED_RESIDENT() {
		return R0290_AMOUNT_AED_RESIDENT;
	}
	public void setR0290_AMOUNT_AED_RESIDENT(BigDecimal r0290_AMOUNT_AED_RESIDENT) {
		R0290_AMOUNT_AED_RESIDENT = r0290_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0290_NO_ACCT_FCY_RESIDENT() {
		return R0290_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0290_NO_ACCT_FCY_RESIDENT(BigDecimal r0290_NO_ACCT_FCY_RESIDENT) {
		R0290_NO_ACCT_FCY_RESIDENT = r0290_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0290_AMOUNT_FCY_RESIDENT() {
		return R0290_AMOUNT_FCY_RESIDENT;
	}
	public void setR0290_AMOUNT_FCY_RESIDENT(BigDecimal r0290_AMOUNT_FCY_RESIDENT) {
		R0290_AMOUNT_FCY_RESIDENT = r0290_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0290_NO_ACCT_AED_NON_RESIDENT() {
		return R0290_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0290_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0290_NO_ACCT_AED_NON_RESIDENT) {
		R0290_NO_ACCT_AED_NON_RESIDENT = r0290_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0290_AMOUNT_AED_NON_RESIDENT() {
		return R0290_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0290_AMOUNT_AED_NON_RESIDENT(BigDecimal r0290_AMOUNT_AED_NON_RESIDENT) {
		R0290_AMOUNT_AED_NON_RESIDENT = r0290_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0290_NO_ACCT_FCY_NON_RESIDENT() {
		return R0290_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0290_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0290_NO_ACCT_FCY_NON_RESIDENT) {
		R0290_NO_ACCT_FCY_NON_RESIDENT = r0290_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0290_AMOUNT_FCY_NON_RESIDENT() {
		return R0290_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0290_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0290_AMOUNT_FCY_NON_RESIDENT) {
		R0290_AMOUNT_FCY_NON_RESIDENT = r0290_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0290_TOTAL_AMOUNT() {
		return R0290_TOTAL_AMOUNT;
	}
	public void setR0290_TOTAL_AMOUNT(BigDecimal r0290_TOTAL_AMOUNT) {
		R0290_TOTAL_AMOUNT = r0290_TOTAL_AMOUNT;
	}
	public String getR0300_PRODUCT() {
		return R0300_PRODUCT;
	}
	public void setR0300_PRODUCT(String r0300_PRODUCT) {
		R0300_PRODUCT = r0300_PRODUCT;
	}
	public BigDecimal getR0300_NO_ACCT_AED_RESIDENT() {
		return R0300_NO_ACCT_AED_RESIDENT;
	}
	public void setR0300_NO_ACCT_AED_RESIDENT(BigDecimal r0300_NO_ACCT_AED_RESIDENT) {
		R0300_NO_ACCT_AED_RESIDENT = r0300_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0300_AMOUNT_AED_RESIDENT() {
		return R0300_AMOUNT_AED_RESIDENT;
	}
	public void setR0300_AMOUNT_AED_RESIDENT(BigDecimal r0300_AMOUNT_AED_RESIDENT) {
		R0300_AMOUNT_AED_RESIDENT = r0300_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0300_NO_ACCT_FCY_RESIDENT() {
		return R0300_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0300_NO_ACCT_FCY_RESIDENT(BigDecimal r0300_NO_ACCT_FCY_RESIDENT) {
		R0300_NO_ACCT_FCY_RESIDENT = r0300_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0300_AMOUNT_FCY_RESIDENT() {
		return R0300_AMOUNT_FCY_RESIDENT;
	}
	public void setR0300_AMOUNT_FCY_RESIDENT(BigDecimal r0300_AMOUNT_FCY_RESIDENT) {
		R0300_AMOUNT_FCY_RESIDENT = r0300_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0300_NO_ACCT_AED_NON_RESIDENT() {
		return R0300_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0300_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0300_NO_ACCT_AED_NON_RESIDENT) {
		R0300_NO_ACCT_AED_NON_RESIDENT = r0300_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0300_AMOUNT_AED_NON_RESIDENT() {
		return R0300_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0300_AMOUNT_AED_NON_RESIDENT(BigDecimal r0300_AMOUNT_AED_NON_RESIDENT) {
		R0300_AMOUNT_AED_NON_RESIDENT = r0300_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0300_NO_ACCT_FCY_NON_RESIDENT() {
		return R0300_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0300_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0300_NO_ACCT_FCY_NON_RESIDENT) {
		R0300_NO_ACCT_FCY_NON_RESIDENT = r0300_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0300_AMOUNT_FCY_NON_RESIDENT() {
		return R0300_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0300_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0300_AMOUNT_FCY_NON_RESIDENT) {
		R0300_AMOUNT_FCY_NON_RESIDENT = r0300_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0300_TOTAL_AMOUNT() {
		return R0300_TOTAL_AMOUNT;
	}
	public void setR0300_TOTAL_AMOUNT(BigDecimal r0300_TOTAL_AMOUNT) {
		R0300_TOTAL_AMOUNT = r0300_TOTAL_AMOUNT;
	}
	public String getR0310_PRODUCT() {
		return R0310_PRODUCT;
	}
	public void setR0310_PRODUCT(String r0310_PRODUCT) {
		R0310_PRODUCT = r0310_PRODUCT;
	}
	public BigDecimal getR0310_NO_ACCT_AED_RESIDENT() {
		return R0310_NO_ACCT_AED_RESIDENT;
	}
	public void setR0310_NO_ACCT_AED_RESIDENT(BigDecimal r0310_NO_ACCT_AED_RESIDENT) {
		R0310_NO_ACCT_AED_RESIDENT = r0310_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0310_AMOUNT_AED_RESIDENT() {
		return R0310_AMOUNT_AED_RESIDENT;
	}
	public void setR0310_AMOUNT_AED_RESIDENT(BigDecimal r0310_AMOUNT_AED_RESIDENT) {
		R0310_AMOUNT_AED_RESIDENT = r0310_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0310_NO_ACCT_FCY_RESIDENT() {
		return R0310_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0310_NO_ACCT_FCY_RESIDENT(BigDecimal r0310_NO_ACCT_FCY_RESIDENT) {
		R0310_NO_ACCT_FCY_RESIDENT = r0310_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0310_AMOUNT_FCY_RESIDENT() {
		return R0310_AMOUNT_FCY_RESIDENT;
	}
	public void setR0310_AMOUNT_FCY_RESIDENT(BigDecimal r0310_AMOUNT_FCY_RESIDENT) {
		R0310_AMOUNT_FCY_RESIDENT = r0310_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0310_NO_ACCT_AED_NON_RESIDENT() {
		return R0310_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0310_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0310_NO_ACCT_AED_NON_RESIDENT) {
		R0310_NO_ACCT_AED_NON_RESIDENT = r0310_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0310_AMOUNT_AED_NON_RESIDENT() {
		return R0310_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0310_AMOUNT_AED_NON_RESIDENT(BigDecimal r0310_AMOUNT_AED_NON_RESIDENT) {
		R0310_AMOUNT_AED_NON_RESIDENT = r0310_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0310_NO_ACCT_FCY_NON_RESIDENT() {
		return R0310_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0310_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0310_NO_ACCT_FCY_NON_RESIDENT) {
		R0310_NO_ACCT_FCY_NON_RESIDENT = r0310_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0310_AMOUNT_FCY_NON_RESIDENT() {
		return R0310_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0310_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0310_AMOUNT_FCY_NON_RESIDENT) {
		R0310_AMOUNT_FCY_NON_RESIDENT = r0310_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0310_TOTAL_AMOUNT() {
		return R0310_TOTAL_AMOUNT;
	}
	public void setR0310_TOTAL_AMOUNT(BigDecimal r0310_TOTAL_AMOUNT) {
		R0310_TOTAL_AMOUNT = r0310_TOTAL_AMOUNT;
	}
	public String getR0320_PRODUCT() {
		return R0320_PRODUCT;
	}
	public void setR0320_PRODUCT(String r0320_PRODUCT) {
		R0320_PRODUCT = r0320_PRODUCT;
	}
	public BigDecimal getR0320_NO_ACCT_AED_RESIDENT() {
		return R0320_NO_ACCT_AED_RESIDENT;
	}
	public void setR0320_NO_ACCT_AED_RESIDENT(BigDecimal r0320_NO_ACCT_AED_RESIDENT) {
		R0320_NO_ACCT_AED_RESIDENT = r0320_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0320_AMOUNT_AED_RESIDENT() {
		return R0320_AMOUNT_AED_RESIDENT;
	}
	public void setR0320_AMOUNT_AED_RESIDENT(BigDecimal r0320_AMOUNT_AED_RESIDENT) {
		R0320_AMOUNT_AED_RESIDENT = r0320_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0320_NO_ACCT_FCY_RESIDENT() {
		return R0320_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0320_NO_ACCT_FCY_RESIDENT(BigDecimal r0320_NO_ACCT_FCY_RESIDENT) {
		R0320_NO_ACCT_FCY_RESIDENT = r0320_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0320_AMOUNT_FCY_RESIDENT() {
		return R0320_AMOUNT_FCY_RESIDENT;
	}
	public void setR0320_AMOUNT_FCY_RESIDENT(BigDecimal r0320_AMOUNT_FCY_RESIDENT) {
		R0320_AMOUNT_FCY_RESIDENT = r0320_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0320_NO_ACCT_AED_NON_RESIDENT() {
		return R0320_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0320_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0320_NO_ACCT_AED_NON_RESIDENT) {
		R0320_NO_ACCT_AED_NON_RESIDENT = r0320_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0320_AMOUNT_AED_NON_RESIDENT() {
		return R0320_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0320_AMOUNT_AED_NON_RESIDENT(BigDecimal r0320_AMOUNT_AED_NON_RESIDENT) {
		R0320_AMOUNT_AED_NON_RESIDENT = r0320_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0320_NO_ACCT_FCY_NON_RESIDENT() {
		return R0320_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0320_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0320_NO_ACCT_FCY_NON_RESIDENT) {
		R0320_NO_ACCT_FCY_NON_RESIDENT = r0320_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0320_AMOUNT_FCY_NON_RESIDENT() {
		return R0320_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0320_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0320_AMOUNT_FCY_NON_RESIDENT) {
		R0320_AMOUNT_FCY_NON_RESIDENT = r0320_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0320_TOTAL_AMOUNT() {
		return R0320_TOTAL_AMOUNT;
	}
	public void setR0320_TOTAL_AMOUNT(BigDecimal r0320_TOTAL_AMOUNT) {
		R0320_TOTAL_AMOUNT = r0320_TOTAL_AMOUNT;
	}
	public String getR0330_PRODUCT() {
		return R0330_PRODUCT;
	}
	public void setR0330_PRODUCT(String r0330_PRODUCT) {
		R0330_PRODUCT = r0330_PRODUCT;
	}
	public BigDecimal getR0330_NO_ACCT_AED_RESIDENT() {
		return R0330_NO_ACCT_AED_RESIDENT;
	}
	public void setR0330_NO_ACCT_AED_RESIDENT(BigDecimal r0330_NO_ACCT_AED_RESIDENT) {
		R0330_NO_ACCT_AED_RESIDENT = r0330_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0330_AMOUNT_AED_RESIDENT() {
		return R0330_AMOUNT_AED_RESIDENT;
	}
	public void setR0330_AMOUNT_AED_RESIDENT(BigDecimal r0330_AMOUNT_AED_RESIDENT) {
		R0330_AMOUNT_AED_RESIDENT = r0330_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0330_NO_ACCT_FCY_RESIDENT() {
		return R0330_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0330_NO_ACCT_FCY_RESIDENT(BigDecimal r0330_NO_ACCT_FCY_RESIDENT) {
		R0330_NO_ACCT_FCY_RESIDENT = r0330_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0330_AMOUNT_FCY_RESIDENT() {
		return R0330_AMOUNT_FCY_RESIDENT;
	}
	public void setR0330_AMOUNT_FCY_RESIDENT(BigDecimal r0330_AMOUNT_FCY_RESIDENT) {
		R0330_AMOUNT_FCY_RESIDENT = r0330_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0330_NO_ACCT_AED_NON_RESIDENT() {
		return R0330_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0330_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0330_NO_ACCT_AED_NON_RESIDENT) {
		R0330_NO_ACCT_AED_NON_RESIDENT = r0330_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0330_AMOUNT_AED_NON_RESIDENT() {
		return R0330_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0330_AMOUNT_AED_NON_RESIDENT(BigDecimal r0330_AMOUNT_AED_NON_RESIDENT) {
		R0330_AMOUNT_AED_NON_RESIDENT = r0330_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0330_NO_ACCT_FCY_NON_RESIDENT() {
		return R0330_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0330_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0330_NO_ACCT_FCY_NON_RESIDENT) {
		R0330_NO_ACCT_FCY_NON_RESIDENT = r0330_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0330_AMOUNT_FCY_NON_RESIDENT() {
		return R0330_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0330_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0330_AMOUNT_FCY_NON_RESIDENT) {
		R0330_AMOUNT_FCY_NON_RESIDENT = r0330_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0330_TOTAL_AMOUNT() {
		return R0330_TOTAL_AMOUNT;
	}
	public void setR0330_TOTAL_AMOUNT(BigDecimal r0330_TOTAL_AMOUNT) {
		R0330_TOTAL_AMOUNT = r0330_TOTAL_AMOUNT;
	}
	public String getR0340_PRODUCT() {
		return R0340_PRODUCT;
	}
	public void setR0340_PRODUCT(String r0340_PRODUCT) {
		R0340_PRODUCT = r0340_PRODUCT;
	}
	public BigDecimal getR0340_NO_ACCT_AED_RESIDENT() {
		return R0340_NO_ACCT_AED_RESIDENT;
	}
	public void setR0340_NO_ACCT_AED_RESIDENT(BigDecimal r0340_NO_ACCT_AED_RESIDENT) {
		R0340_NO_ACCT_AED_RESIDENT = r0340_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0340_AMOUNT_AED_RESIDENT() {
		return R0340_AMOUNT_AED_RESIDENT;
	}
	public void setR0340_AMOUNT_AED_RESIDENT(BigDecimal r0340_AMOUNT_AED_RESIDENT) {
		R0340_AMOUNT_AED_RESIDENT = r0340_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0340_NO_ACCT_FCY_RESIDENT() {
		return R0340_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0340_NO_ACCT_FCY_RESIDENT(BigDecimal r0340_NO_ACCT_FCY_RESIDENT) {
		R0340_NO_ACCT_FCY_RESIDENT = r0340_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0340_AMOUNT_FCY_RESIDENT() {
		return R0340_AMOUNT_FCY_RESIDENT;
	}
	public void setR0340_AMOUNT_FCY_RESIDENT(BigDecimal r0340_AMOUNT_FCY_RESIDENT) {
		R0340_AMOUNT_FCY_RESIDENT = r0340_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0340_NO_ACCT_AED_NON_RESIDENT() {
		return R0340_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0340_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0340_NO_ACCT_AED_NON_RESIDENT) {
		R0340_NO_ACCT_AED_NON_RESIDENT = r0340_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0340_AMOUNT_AED_NON_RESIDENT() {
		return R0340_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0340_AMOUNT_AED_NON_RESIDENT(BigDecimal r0340_AMOUNT_AED_NON_RESIDENT) {
		R0340_AMOUNT_AED_NON_RESIDENT = r0340_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0340_NO_ACCT_FCY_NON_RESIDENT() {
		return R0340_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0340_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0340_NO_ACCT_FCY_NON_RESIDENT) {
		R0340_NO_ACCT_FCY_NON_RESIDENT = r0340_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0340_AMOUNT_FCY_NON_RESIDENT() {
		return R0340_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0340_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0340_AMOUNT_FCY_NON_RESIDENT) {
		R0340_AMOUNT_FCY_NON_RESIDENT = r0340_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0340_TOTAL_AMOUNT() {
		return R0340_TOTAL_AMOUNT;
	}
	public void setR0340_TOTAL_AMOUNT(BigDecimal r0340_TOTAL_AMOUNT) {
		R0340_TOTAL_AMOUNT = r0340_TOTAL_AMOUNT;
	}
	public String getR0350_PRODUCT() {
		return R0350_PRODUCT;
	}
	public void setR0350_PRODUCT(String r0350_PRODUCT) {
		R0350_PRODUCT = r0350_PRODUCT;
	}
	public BigDecimal getR0350_NO_ACCT_AED_RESIDENT() {
		return R0350_NO_ACCT_AED_RESIDENT;
	}
	public void setR0350_NO_ACCT_AED_RESIDENT(BigDecimal r0350_NO_ACCT_AED_RESIDENT) {
		R0350_NO_ACCT_AED_RESIDENT = r0350_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0350_AMOUNT_AED_RESIDENT() {
		return R0350_AMOUNT_AED_RESIDENT;
	}
	public void setR0350_AMOUNT_AED_RESIDENT(BigDecimal r0350_AMOUNT_AED_RESIDENT) {
		R0350_AMOUNT_AED_RESIDENT = r0350_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0350_NO_ACCT_FCY_RESIDENT() {
		return R0350_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0350_NO_ACCT_FCY_RESIDENT(BigDecimal r0350_NO_ACCT_FCY_RESIDENT) {
		R0350_NO_ACCT_FCY_RESIDENT = r0350_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0350_AMOUNT_FCY_RESIDENT() {
		return R0350_AMOUNT_FCY_RESIDENT;
	}
	public void setR0350_AMOUNT_FCY_RESIDENT(BigDecimal r0350_AMOUNT_FCY_RESIDENT) {
		R0350_AMOUNT_FCY_RESIDENT = r0350_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0350_NO_ACCT_AED_NON_RESIDENT() {
		return R0350_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0350_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0350_NO_ACCT_AED_NON_RESIDENT) {
		R0350_NO_ACCT_AED_NON_RESIDENT = r0350_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0350_AMOUNT_AED_NON_RESIDENT() {
		return R0350_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0350_AMOUNT_AED_NON_RESIDENT(BigDecimal r0350_AMOUNT_AED_NON_RESIDENT) {
		R0350_AMOUNT_AED_NON_RESIDENT = r0350_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0350_NO_ACCT_FCY_NON_RESIDENT() {
		return R0350_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0350_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0350_NO_ACCT_FCY_NON_RESIDENT) {
		R0350_NO_ACCT_FCY_NON_RESIDENT = r0350_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0350_AMOUNT_FCY_NON_RESIDENT() {
		return R0350_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0350_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0350_AMOUNT_FCY_NON_RESIDENT) {
		R0350_AMOUNT_FCY_NON_RESIDENT = r0350_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0350_TOTAL_AMOUNT() {
		return R0350_TOTAL_AMOUNT;
	}
	public void setR0350_TOTAL_AMOUNT(BigDecimal r0350_TOTAL_AMOUNT) {
		R0350_TOTAL_AMOUNT = r0350_TOTAL_AMOUNT;
	}
	public String getR0360_PRODUCT() {
		return R0360_PRODUCT;
	}
	public void setR0360_PRODUCT(String r0360_PRODUCT) {
		R0360_PRODUCT = r0360_PRODUCT;
	}
	public BigDecimal getR0360_NO_ACCT_AED_RESIDENT() {
		return R0360_NO_ACCT_AED_RESIDENT;
	}
	public void setR0360_NO_ACCT_AED_RESIDENT(BigDecimal r0360_NO_ACCT_AED_RESIDENT) {
		R0360_NO_ACCT_AED_RESIDENT = r0360_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0360_AMOUNT_AED_RESIDENT() {
		return R0360_AMOUNT_AED_RESIDENT;
	}
	public void setR0360_AMOUNT_AED_RESIDENT(BigDecimal r0360_AMOUNT_AED_RESIDENT) {
		R0360_AMOUNT_AED_RESIDENT = r0360_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0360_NO_ACCT_FCY_RESIDENT() {
		return R0360_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0360_NO_ACCT_FCY_RESIDENT(BigDecimal r0360_NO_ACCT_FCY_RESIDENT) {
		R0360_NO_ACCT_FCY_RESIDENT = r0360_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0360_AMOUNT_FCY_RESIDENT() {
		return R0360_AMOUNT_FCY_RESIDENT;
	}
	public void setR0360_AMOUNT_FCY_RESIDENT(BigDecimal r0360_AMOUNT_FCY_RESIDENT) {
		R0360_AMOUNT_FCY_RESIDENT = r0360_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0360_NO_ACCT_AED_NON_RESIDENT() {
		return R0360_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0360_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0360_NO_ACCT_AED_NON_RESIDENT) {
		R0360_NO_ACCT_AED_NON_RESIDENT = r0360_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0360_AMOUNT_AED_NON_RESIDENT() {
		return R0360_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0360_AMOUNT_AED_NON_RESIDENT(BigDecimal r0360_AMOUNT_AED_NON_RESIDENT) {
		R0360_AMOUNT_AED_NON_RESIDENT = r0360_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0360_NO_ACCT_FCY_NON_RESIDENT() {
		return R0360_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0360_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0360_NO_ACCT_FCY_NON_RESIDENT) {
		R0360_NO_ACCT_FCY_NON_RESIDENT = r0360_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0360_AMOUNT_FCY_NON_RESIDENT() {
		return R0360_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0360_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0360_AMOUNT_FCY_NON_RESIDENT) {
		R0360_AMOUNT_FCY_NON_RESIDENT = r0360_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0360_TOTAL_AMOUNT() {
		return R0360_TOTAL_AMOUNT;
	}
	public void setR0360_TOTAL_AMOUNT(BigDecimal r0360_TOTAL_AMOUNT) {
		R0360_TOTAL_AMOUNT = r0360_TOTAL_AMOUNT;
	}
	public String getR0370_PRODUCT() {
		return R0370_PRODUCT;
	}
	public void setR0370_PRODUCT(String r0370_PRODUCT) {
		R0370_PRODUCT = r0370_PRODUCT;
	}
	public BigDecimal getR0370_NO_ACCT_AED_RESIDENT() {
		return R0370_NO_ACCT_AED_RESIDENT;
	}
	public void setR0370_NO_ACCT_AED_RESIDENT(BigDecimal r0370_NO_ACCT_AED_RESIDENT) {
		R0370_NO_ACCT_AED_RESIDENT = r0370_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0370_AMOUNT_AED_RESIDENT() {
		return R0370_AMOUNT_AED_RESIDENT;
	}
	public void setR0370_AMOUNT_AED_RESIDENT(BigDecimal r0370_AMOUNT_AED_RESIDENT) {
		R0370_AMOUNT_AED_RESIDENT = r0370_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0370_NO_ACCT_FCY_RESIDENT() {
		return R0370_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0370_NO_ACCT_FCY_RESIDENT(BigDecimal r0370_NO_ACCT_FCY_RESIDENT) {
		R0370_NO_ACCT_FCY_RESIDENT = r0370_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0370_AMOUNT_FCY_RESIDENT() {
		return R0370_AMOUNT_FCY_RESIDENT;
	}
	public void setR0370_AMOUNT_FCY_RESIDENT(BigDecimal r0370_AMOUNT_FCY_RESIDENT) {
		R0370_AMOUNT_FCY_RESIDENT = r0370_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0370_NO_ACCT_AED_NON_RESIDENT() {
		return R0370_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0370_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0370_NO_ACCT_AED_NON_RESIDENT) {
		R0370_NO_ACCT_AED_NON_RESIDENT = r0370_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0370_AMOUNT_AED_NON_RESIDENT() {
		return R0370_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0370_AMOUNT_AED_NON_RESIDENT(BigDecimal r0370_AMOUNT_AED_NON_RESIDENT) {
		R0370_AMOUNT_AED_NON_RESIDENT = r0370_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0370_NO_ACCT_FCY_NON_RESIDENT() {
		return R0370_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0370_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0370_NO_ACCT_FCY_NON_RESIDENT) {
		R0370_NO_ACCT_FCY_NON_RESIDENT = r0370_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0370_AMOUNT_FCY_NON_RESIDENT() {
		return R0370_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0370_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0370_AMOUNT_FCY_NON_RESIDENT) {
		R0370_AMOUNT_FCY_NON_RESIDENT = r0370_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0370_TOTAL_AMOUNT() {
		return R0370_TOTAL_AMOUNT;
	}
	public void setR0370_TOTAL_AMOUNT(BigDecimal r0370_TOTAL_AMOUNT) {
		R0370_TOTAL_AMOUNT = r0370_TOTAL_AMOUNT;
	}
	public String getR0380_PRODUCT() {
		return R0380_PRODUCT;
	}
	public void setR0380_PRODUCT(String r0380_PRODUCT) {
		R0380_PRODUCT = r0380_PRODUCT;
	}
	public BigDecimal getR0380_NO_ACCT_AED_RESIDENT() {
		return R0380_NO_ACCT_AED_RESIDENT;
	}
	public void setR0380_NO_ACCT_AED_RESIDENT(BigDecimal r0380_NO_ACCT_AED_RESIDENT) {
		R0380_NO_ACCT_AED_RESIDENT = r0380_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0380_AMOUNT_AED_RESIDENT() {
		return R0380_AMOUNT_AED_RESIDENT;
	}
	public void setR0380_AMOUNT_AED_RESIDENT(BigDecimal r0380_AMOUNT_AED_RESIDENT) {
		R0380_AMOUNT_AED_RESIDENT = r0380_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0380_NO_ACCT_FCY_RESIDENT() {
		return R0380_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0380_NO_ACCT_FCY_RESIDENT(BigDecimal r0380_NO_ACCT_FCY_RESIDENT) {
		R0380_NO_ACCT_FCY_RESIDENT = r0380_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0380_AMOUNT_FCY_RESIDENT() {
		return R0380_AMOUNT_FCY_RESIDENT;
	}
	public void setR0380_AMOUNT_FCY_RESIDENT(BigDecimal r0380_AMOUNT_FCY_RESIDENT) {
		R0380_AMOUNT_FCY_RESIDENT = r0380_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0380_NO_ACCT_AED_NON_RESIDENT() {
		return R0380_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0380_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0380_NO_ACCT_AED_NON_RESIDENT) {
		R0380_NO_ACCT_AED_NON_RESIDENT = r0380_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0380_AMOUNT_AED_NON_RESIDENT() {
		return R0380_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0380_AMOUNT_AED_NON_RESIDENT(BigDecimal r0380_AMOUNT_AED_NON_RESIDENT) {
		R0380_AMOUNT_AED_NON_RESIDENT = r0380_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0380_NO_ACCT_FCY_NON_RESIDENT() {
		return R0380_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0380_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0380_NO_ACCT_FCY_NON_RESIDENT) {
		R0380_NO_ACCT_FCY_NON_RESIDENT = r0380_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0380_AMOUNT_FCY_NON_RESIDENT() {
		return R0380_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0380_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0380_AMOUNT_FCY_NON_RESIDENT) {
		R0380_AMOUNT_FCY_NON_RESIDENT = r0380_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0380_TOTAL_AMOUNT() {
		return R0380_TOTAL_AMOUNT;
	}
	public void setR0380_TOTAL_AMOUNT(BigDecimal r0380_TOTAL_AMOUNT) {
		R0380_TOTAL_AMOUNT = r0380_TOTAL_AMOUNT;
	}
	public String getR0390_PRODUCT() {
		return R0390_PRODUCT;
	}
	public void setR0390_PRODUCT(String r0390_PRODUCT) {
		R0390_PRODUCT = r0390_PRODUCT;
	}
	public BigDecimal getR0390_NO_ACCT_AED_RESIDENT() {
		return R0390_NO_ACCT_AED_RESIDENT;
	}
	public void setR0390_NO_ACCT_AED_RESIDENT(BigDecimal r0390_NO_ACCT_AED_RESIDENT) {
		R0390_NO_ACCT_AED_RESIDENT = r0390_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0390_AMOUNT_AED_RESIDENT() {
		return R0390_AMOUNT_AED_RESIDENT;
	}
	public void setR0390_AMOUNT_AED_RESIDENT(BigDecimal r0390_AMOUNT_AED_RESIDENT) {
		R0390_AMOUNT_AED_RESIDENT = r0390_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0390_NO_ACCT_FCY_RESIDENT() {
		return R0390_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0390_NO_ACCT_FCY_RESIDENT(BigDecimal r0390_NO_ACCT_FCY_RESIDENT) {
		R0390_NO_ACCT_FCY_RESIDENT = r0390_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0390_AMOUNT_FCY_RESIDENT() {
		return R0390_AMOUNT_FCY_RESIDENT;
	}
	public void setR0390_AMOUNT_FCY_RESIDENT(BigDecimal r0390_AMOUNT_FCY_RESIDENT) {
		R0390_AMOUNT_FCY_RESIDENT = r0390_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0390_NO_ACCT_AED_NON_RESIDENT() {
		return R0390_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0390_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0390_NO_ACCT_AED_NON_RESIDENT) {
		R0390_NO_ACCT_AED_NON_RESIDENT = r0390_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0390_AMOUNT_AED_NON_RESIDENT() {
		return R0390_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0390_AMOUNT_AED_NON_RESIDENT(BigDecimal r0390_AMOUNT_AED_NON_RESIDENT) {
		R0390_AMOUNT_AED_NON_RESIDENT = r0390_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0390_NO_ACCT_FCY_NON_RESIDENT() {
		return R0390_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0390_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0390_NO_ACCT_FCY_NON_RESIDENT) {
		R0390_NO_ACCT_FCY_NON_RESIDENT = r0390_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0390_AMOUNT_FCY_NON_RESIDENT() {
		return R0390_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0390_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0390_AMOUNT_FCY_NON_RESIDENT) {
		R0390_AMOUNT_FCY_NON_RESIDENT = r0390_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0390_TOTAL_AMOUNT() {
		return R0390_TOTAL_AMOUNT;
	}
	public void setR0390_TOTAL_AMOUNT(BigDecimal r0390_TOTAL_AMOUNT) {
		R0390_TOTAL_AMOUNT = r0390_TOTAL_AMOUNT;
	}
	public String getR0400_PRODUCT() {
		return R0400_PRODUCT;
	}
	public void setR0400_PRODUCT(String r0400_PRODUCT) {
		R0400_PRODUCT = r0400_PRODUCT;
	}
	public BigDecimal getR0400_NO_ACCT_AED_RESIDENT() {
		return R0400_NO_ACCT_AED_RESIDENT;
	}
	public void setR0400_NO_ACCT_AED_RESIDENT(BigDecimal r0400_NO_ACCT_AED_RESIDENT) {
		R0400_NO_ACCT_AED_RESIDENT = r0400_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0400_AMOUNT_AED_RESIDENT() {
		return R0400_AMOUNT_AED_RESIDENT;
	}
	public void setR0400_AMOUNT_AED_RESIDENT(BigDecimal r0400_AMOUNT_AED_RESIDENT) {
		R0400_AMOUNT_AED_RESIDENT = r0400_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0400_NO_ACCT_FCY_RESIDENT() {
		return R0400_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0400_NO_ACCT_FCY_RESIDENT(BigDecimal r0400_NO_ACCT_FCY_RESIDENT) {
		R0400_NO_ACCT_FCY_RESIDENT = r0400_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0400_AMOUNT_FCY_RESIDENT() {
		return R0400_AMOUNT_FCY_RESIDENT;
	}
	public void setR0400_AMOUNT_FCY_RESIDENT(BigDecimal r0400_AMOUNT_FCY_RESIDENT) {
		R0400_AMOUNT_FCY_RESIDENT = r0400_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0400_NO_ACCT_AED_NON_RESIDENT() {
		return R0400_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0400_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0400_NO_ACCT_AED_NON_RESIDENT) {
		R0400_NO_ACCT_AED_NON_RESIDENT = r0400_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0400_AMOUNT_AED_NON_RESIDENT() {
		return R0400_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0400_AMOUNT_AED_NON_RESIDENT(BigDecimal r0400_AMOUNT_AED_NON_RESIDENT) {
		R0400_AMOUNT_AED_NON_RESIDENT = r0400_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0400_NO_ACCT_FCY_NON_RESIDENT() {
		return R0400_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0400_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0400_NO_ACCT_FCY_NON_RESIDENT) {
		R0400_NO_ACCT_FCY_NON_RESIDENT = r0400_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0400_AMOUNT_FCY_NON_RESIDENT() {
		return R0400_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0400_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0400_AMOUNT_FCY_NON_RESIDENT) {
		R0400_AMOUNT_FCY_NON_RESIDENT = r0400_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0400_TOTAL_AMOUNT() {
		return R0400_TOTAL_AMOUNT;
	}
	public void setR0400_TOTAL_AMOUNT(BigDecimal r0400_TOTAL_AMOUNT) {
		R0400_TOTAL_AMOUNT = r0400_TOTAL_AMOUNT;
	}
	public String getR0410_PRODUCT() {
		return R0410_PRODUCT;
	}
	public void setR0410_PRODUCT(String r0410_PRODUCT) {
		R0410_PRODUCT = r0410_PRODUCT;
	}
	public BigDecimal getR0410_NO_ACCT_AED_RESIDENT() {
		return R0410_NO_ACCT_AED_RESIDENT;
	}
	public void setR0410_NO_ACCT_AED_RESIDENT(BigDecimal r0410_NO_ACCT_AED_RESIDENT) {
		R0410_NO_ACCT_AED_RESIDENT = r0410_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0410_AMOUNT_AED_RESIDENT() {
		return R0410_AMOUNT_AED_RESIDENT;
	}
	public void setR0410_AMOUNT_AED_RESIDENT(BigDecimal r0410_AMOUNT_AED_RESIDENT) {
		R0410_AMOUNT_AED_RESIDENT = r0410_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0410_NO_ACCT_FCY_RESIDENT() {
		return R0410_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0410_NO_ACCT_FCY_RESIDENT(BigDecimal r0410_NO_ACCT_FCY_RESIDENT) {
		R0410_NO_ACCT_FCY_RESIDENT = r0410_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0410_AMOUNT_FCY_RESIDENT() {
		return R0410_AMOUNT_FCY_RESIDENT;
	}
	public void setR0410_AMOUNT_FCY_RESIDENT(BigDecimal r0410_AMOUNT_FCY_RESIDENT) {
		R0410_AMOUNT_FCY_RESIDENT = r0410_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0410_NO_ACCT_AED_NON_RESIDENT() {
		return R0410_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0410_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0410_NO_ACCT_AED_NON_RESIDENT) {
		R0410_NO_ACCT_AED_NON_RESIDENT = r0410_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0410_AMOUNT_AED_NON_RESIDENT() {
		return R0410_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0410_AMOUNT_AED_NON_RESIDENT(BigDecimal r0410_AMOUNT_AED_NON_RESIDENT) {
		R0410_AMOUNT_AED_NON_RESIDENT = r0410_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0410_NO_ACCT_FCY_NON_RESIDENT() {
		return R0410_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0410_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0410_NO_ACCT_FCY_NON_RESIDENT) {
		R0410_NO_ACCT_FCY_NON_RESIDENT = r0410_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0410_AMOUNT_FCY_NON_RESIDENT() {
		return R0410_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0410_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0410_AMOUNT_FCY_NON_RESIDENT) {
		R0410_AMOUNT_FCY_NON_RESIDENT = r0410_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0410_TOTAL_AMOUNT() {
		return R0410_TOTAL_AMOUNT;
	}
	public void setR0410_TOTAL_AMOUNT(BigDecimal r0410_TOTAL_AMOUNT) {
		R0410_TOTAL_AMOUNT = r0410_TOTAL_AMOUNT;
	}
	public String getR0420_PRODUCT() {
		return R0420_PRODUCT;
	}
	public void setR0420_PRODUCT(String r0420_PRODUCT) {
		R0420_PRODUCT = r0420_PRODUCT;
	}
	public BigDecimal getR0420_NO_ACCT_AED_RESIDENT() {
		return R0420_NO_ACCT_AED_RESIDENT;
	}
	public void setR0420_NO_ACCT_AED_RESIDENT(BigDecimal r0420_NO_ACCT_AED_RESIDENT) {
		R0420_NO_ACCT_AED_RESIDENT = r0420_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0420_AMOUNT_AED_RESIDENT() {
		return R0420_AMOUNT_AED_RESIDENT;
	}
	public void setR0420_AMOUNT_AED_RESIDENT(BigDecimal r0420_AMOUNT_AED_RESIDENT) {
		R0420_AMOUNT_AED_RESIDENT = r0420_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0420_NO_ACCT_FCY_RESIDENT() {
		return R0420_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0420_NO_ACCT_FCY_RESIDENT(BigDecimal r0420_NO_ACCT_FCY_RESIDENT) {
		R0420_NO_ACCT_FCY_RESIDENT = r0420_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0420_AMOUNT_FCY_RESIDENT() {
		return R0420_AMOUNT_FCY_RESIDENT;
	}
	public void setR0420_AMOUNT_FCY_RESIDENT(BigDecimal r0420_AMOUNT_FCY_RESIDENT) {
		R0420_AMOUNT_FCY_RESIDENT = r0420_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0420_NO_ACCT_AED_NON_RESIDENT() {
		return R0420_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0420_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0420_NO_ACCT_AED_NON_RESIDENT) {
		R0420_NO_ACCT_AED_NON_RESIDENT = r0420_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0420_AMOUNT_AED_NON_RESIDENT() {
		return R0420_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0420_AMOUNT_AED_NON_RESIDENT(BigDecimal r0420_AMOUNT_AED_NON_RESIDENT) {
		R0420_AMOUNT_AED_NON_RESIDENT = r0420_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0420_NO_ACCT_FCY_NON_RESIDENT() {
		return R0420_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0420_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0420_NO_ACCT_FCY_NON_RESIDENT) {
		R0420_NO_ACCT_FCY_NON_RESIDENT = r0420_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0420_AMOUNT_FCY_NON_RESIDENT() {
		return R0420_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0420_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0420_AMOUNT_FCY_NON_RESIDENT) {
		R0420_AMOUNT_FCY_NON_RESIDENT = r0420_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0420_TOTAL_AMOUNT() {
		return R0420_TOTAL_AMOUNT;
	}
	public void setR0420_TOTAL_AMOUNT(BigDecimal r0420_TOTAL_AMOUNT) {
		R0420_TOTAL_AMOUNT = r0420_TOTAL_AMOUNT;
	}
	public String getR0430_PRODUCT() {
		return R0430_PRODUCT;
	}
	public void setR0430_PRODUCT(String r0430_PRODUCT) {
		R0430_PRODUCT = r0430_PRODUCT;
	}
	public BigDecimal getR0430_NO_ACCT_AED_RESIDENT() {
		return R0430_NO_ACCT_AED_RESIDENT;
	}
	public void setR0430_NO_ACCT_AED_RESIDENT(BigDecimal r0430_NO_ACCT_AED_RESIDENT) {
		R0430_NO_ACCT_AED_RESIDENT = r0430_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0430_AMOUNT_AED_RESIDENT() {
		return R0430_AMOUNT_AED_RESIDENT;
	}
	public void setR0430_AMOUNT_AED_RESIDENT(BigDecimal r0430_AMOUNT_AED_RESIDENT) {
		R0430_AMOUNT_AED_RESIDENT = r0430_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0430_NO_ACCT_FCY_RESIDENT() {
		return R0430_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0430_NO_ACCT_FCY_RESIDENT(BigDecimal r0430_NO_ACCT_FCY_RESIDENT) {
		R0430_NO_ACCT_FCY_RESIDENT = r0430_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0430_AMOUNT_FCY_RESIDENT() {
		return R0430_AMOUNT_FCY_RESIDENT;
	}
	public void setR0430_AMOUNT_FCY_RESIDENT(BigDecimal r0430_AMOUNT_FCY_RESIDENT) {
		R0430_AMOUNT_FCY_RESIDENT = r0430_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0430_NO_ACCT_AED_NON_RESIDENT() {
		return R0430_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0430_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0430_NO_ACCT_AED_NON_RESIDENT) {
		R0430_NO_ACCT_AED_NON_RESIDENT = r0430_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0430_AMOUNT_AED_NON_RESIDENT() {
		return R0430_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0430_AMOUNT_AED_NON_RESIDENT(BigDecimal r0430_AMOUNT_AED_NON_RESIDENT) {
		R0430_AMOUNT_AED_NON_RESIDENT = r0430_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0430_NO_ACCT_FCY_NON_RESIDENT() {
		return R0430_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0430_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0430_NO_ACCT_FCY_NON_RESIDENT) {
		R0430_NO_ACCT_FCY_NON_RESIDENT = r0430_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0430_AMOUNT_FCY_NON_RESIDENT() {
		return R0430_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0430_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0430_AMOUNT_FCY_NON_RESIDENT) {
		R0430_AMOUNT_FCY_NON_RESIDENT = r0430_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0430_TOTAL_AMOUNT() {
		return R0430_TOTAL_AMOUNT;
	}
	public void setR0430_TOTAL_AMOUNT(BigDecimal r0430_TOTAL_AMOUNT) {
		R0430_TOTAL_AMOUNT = r0430_TOTAL_AMOUNT;
	}
	public String getR0440_PRODUCT() {
		return R0440_PRODUCT;
	}
	public void setR0440_PRODUCT(String r0440_PRODUCT) {
		R0440_PRODUCT = r0440_PRODUCT;
	}
	public BigDecimal getR0440_NO_ACCT_AED_RESIDENT() {
		return R0440_NO_ACCT_AED_RESIDENT;
	}
	public void setR0440_NO_ACCT_AED_RESIDENT(BigDecimal r0440_NO_ACCT_AED_RESIDENT) {
		R0440_NO_ACCT_AED_RESIDENT = r0440_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0440_AMOUNT_AED_RESIDENT() {
		return R0440_AMOUNT_AED_RESIDENT;
	}
	public void setR0440_AMOUNT_AED_RESIDENT(BigDecimal r0440_AMOUNT_AED_RESIDENT) {
		R0440_AMOUNT_AED_RESIDENT = r0440_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0440_NO_ACCT_FCY_RESIDENT() {
		return R0440_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0440_NO_ACCT_FCY_RESIDENT(BigDecimal r0440_NO_ACCT_FCY_RESIDENT) {
		R0440_NO_ACCT_FCY_RESIDENT = r0440_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0440_AMOUNT_FCY_RESIDENT() {
		return R0440_AMOUNT_FCY_RESIDENT;
	}
	public void setR0440_AMOUNT_FCY_RESIDENT(BigDecimal r0440_AMOUNT_FCY_RESIDENT) {
		R0440_AMOUNT_FCY_RESIDENT = r0440_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0440_NO_ACCT_AED_NON_RESIDENT() {
		return R0440_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0440_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0440_NO_ACCT_AED_NON_RESIDENT) {
		R0440_NO_ACCT_AED_NON_RESIDENT = r0440_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0440_AMOUNT_AED_NON_RESIDENT() {
		return R0440_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0440_AMOUNT_AED_NON_RESIDENT(BigDecimal r0440_AMOUNT_AED_NON_RESIDENT) {
		R0440_AMOUNT_AED_NON_RESIDENT = r0440_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0440_NO_ACCT_FCY_NON_RESIDENT() {
		return R0440_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0440_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0440_NO_ACCT_FCY_NON_RESIDENT) {
		R0440_NO_ACCT_FCY_NON_RESIDENT = r0440_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0440_AMOUNT_FCY_NON_RESIDENT() {
		return R0440_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0440_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0440_AMOUNT_FCY_NON_RESIDENT) {
		R0440_AMOUNT_FCY_NON_RESIDENT = r0440_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0440_TOTAL_AMOUNT() {
		return R0440_TOTAL_AMOUNT;
	}
	public void setR0440_TOTAL_AMOUNT(BigDecimal r0440_TOTAL_AMOUNT) {
		R0440_TOTAL_AMOUNT = r0440_TOTAL_AMOUNT;
	}
	public String getR0450_PRODUCT() {
		return R0450_PRODUCT;
	}
	public void setR0450_PRODUCT(String r0450_PRODUCT) {
		R0450_PRODUCT = r0450_PRODUCT;
	}
	public BigDecimal getR0450_NO_ACCT_AED_RESIDENT() {
		return R0450_NO_ACCT_AED_RESIDENT;
	}
	public void setR0450_NO_ACCT_AED_RESIDENT(BigDecimal r0450_NO_ACCT_AED_RESIDENT) {
		R0450_NO_ACCT_AED_RESIDENT = r0450_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0450_AMOUNT_AED_RESIDENT() {
		return R0450_AMOUNT_AED_RESIDENT;
	}
	public void setR0450_AMOUNT_AED_RESIDENT(BigDecimal r0450_AMOUNT_AED_RESIDENT) {
		R0450_AMOUNT_AED_RESIDENT = r0450_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0450_NO_ACCT_FCY_RESIDENT() {
		return R0450_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0450_NO_ACCT_FCY_RESIDENT(BigDecimal r0450_NO_ACCT_FCY_RESIDENT) {
		R0450_NO_ACCT_FCY_RESIDENT = r0450_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0450_AMOUNT_FCY_RESIDENT() {
		return R0450_AMOUNT_FCY_RESIDENT;
	}
	public void setR0450_AMOUNT_FCY_RESIDENT(BigDecimal r0450_AMOUNT_FCY_RESIDENT) {
		R0450_AMOUNT_FCY_RESIDENT = r0450_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0450_NO_ACCT_AED_NON_RESIDENT() {
		return R0450_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0450_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0450_NO_ACCT_AED_NON_RESIDENT) {
		R0450_NO_ACCT_AED_NON_RESIDENT = r0450_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0450_AMOUNT_AED_NON_RESIDENT() {
		return R0450_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0450_AMOUNT_AED_NON_RESIDENT(BigDecimal r0450_AMOUNT_AED_NON_RESIDENT) {
		R0450_AMOUNT_AED_NON_RESIDENT = r0450_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0450_NO_ACCT_FCY_NON_RESIDENT() {
		return R0450_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0450_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0450_NO_ACCT_FCY_NON_RESIDENT) {
		R0450_NO_ACCT_FCY_NON_RESIDENT = r0450_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0450_AMOUNT_FCY_NON_RESIDENT() {
		return R0450_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0450_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0450_AMOUNT_FCY_NON_RESIDENT) {
		R0450_AMOUNT_FCY_NON_RESIDENT = r0450_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0450_TOTAL_AMOUNT() {
		return R0450_TOTAL_AMOUNT;
	}
	public void setR0450_TOTAL_AMOUNT(BigDecimal r0450_TOTAL_AMOUNT) {
		R0450_TOTAL_AMOUNT = r0450_TOTAL_AMOUNT;
	}
	public String getR0460_PRODUCT() {
		return R0460_PRODUCT;
	}
	public void setR0460_PRODUCT(String r0460_PRODUCT) {
		R0460_PRODUCT = r0460_PRODUCT;
	}
	public BigDecimal getR0460_NO_ACCT_AED_RESIDENT() {
		return R0460_NO_ACCT_AED_RESIDENT;
	}
	public void setR0460_NO_ACCT_AED_RESIDENT(BigDecimal r0460_NO_ACCT_AED_RESIDENT) {
		R0460_NO_ACCT_AED_RESIDENT = r0460_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0460_AMOUNT_AED_RESIDENT() {
		return R0460_AMOUNT_AED_RESIDENT;
	}
	public void setR0460_AMOUNT_AED_RESIDENT(BigDecimal r0460_AMOUNT_AED_RESIDENT) {
		R0460_AMOUNT_AED_RESIDENT = r0460_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0460_NO_ACCT_FCY_RESIDENT() {
		return R0460_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0460_NO_ACCT_FCY_RESIDENT(BigDecimal r0460_NO_ACCT_FCY_RESIDENT) {
		R0460_NO_ACCT_FCY_RESIDENT = r0460_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0460_AMOUNT_FCY_RESIDENT() {
		return R0460_AMOUNT_FCY_RESIDENT;
	}
	public void setR0460_AMOUNT_FCY_RESIDENT(BigDecimal r0460_AMOUNT_FCY_RESIDENT) {
		R0460_AMOUNT_FCY_RESIDENT = r0460_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0460_NO_ACCT_AED_NON_RESIDENT() {
		return R0460_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0460_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0460_NO_ACCT_AED_NON_RESIDENT) {
		R0460_NO_ACCT_AED_NON_RESIDENT = r0460_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0460_AMOUNT_AED_NON_RESIDENT() {
		return R0460_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0460_AMOUNT_AED_NON_RESIDENT(BigDecimal r0460_AMOUNT_AED_NON_RESIDENT) {
		R0460_AMOUNT_AED_NON_RESIDENT = r0460_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0460_NO_ACCT_FCY_NON_RESIDENT() {
		return R0460_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0460_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0460_NO_ACCT_FCY_NON_RESIDENT) {
		R0460_NO_ACCT_FCY_NON_RESIDENT = r0460_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0460_AMOUNT_FCY_NON_RESIDENT() {
		return R0460_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0460_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0460_AMOUNT_FCY_NON_RESIDENT) {
		R0460_AMOUNT_FCY_NON_RESIDENT = r0460_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0460_TOTAL_AMOUNT() {
		return R0460_TOTAL_AMOUNT;
	}
	public void setR0460_TOTAL_AMOUNT(BigDecimal r0460_TOTAL_AMOUNT) {
		R0460_TOTAL_AMOUNT = r0460_TOTAL_AMOUNT;
	}
	public String getR0470_PRODUCT() {
		return R0470_PRODUCT;
	}
	public void setR0470_PRODUCT(String r0470_PRODUCT) {
		R0470_PRODUCT = r0470_PRODUCT;
	}
	public BigDecimal getR0470_NO_ACCT_AED_RESIDENT() {
		return R0470_NO_ACCT_AED_RESIDENT;
	}
	public void setR0470_NO_ACCT_AED_RESIDENT(BigDecimal r0470_NO_ACCT_AED_RESIDENT) {
		R0470_NO_ACCT_AED_RESIDENT = r0470_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0470_AMOUNT_AED_RESIDENT() {
		return R0470_AMOUNT_AED_RESIDENT;
	}
	public void setR0470_AMOUNT_AED_RESIDENT(BigDecimal r0470_AMOUNT_AED_RESIDENT) {
		R0470_AMOUNT_AED_RESIDENT = r0470_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0470_NO_ACCT_FCY_RESIDENT() {
		return R0470_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0470_NO_ACCT_FCY_RESIDENT(BigDecimal r0470_NO_ACCT_FCY_RESIDENT) {
		R0470_NO_ACCT_FCY_RESIDENT = r0470_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0470_AMOUNT_FCY_RESIDENT() {
		return R0470_AMOUNT_FCY_RESIDENT;
	}
	public void setR0470_AMOUNT_FCY_RESIDENT(BigDecimal r0470_AMOUNT_FCY_RESIDENT) {
		R0470_AMOUNT_FCY_RESIDENT = r0470_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0470_NO_ACCT_AED_NON_RESIDENT() {
		return R0470_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0470_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0470_NO_ACCT_AED_NON_RESIDENT) {
		R0470_NO_ACCT_AED_NON_RESIDENT = r0470_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0470_AMOUNT_AED_NON_RESIDENT() {
		return R0470_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0470_AMOUNT_AED_NON_RESIDENT(BigDecimal r0470_AMOUNT_AED_NON_RESIDENT) {
		R0470_AMOUNT_AED_NON_RESIDENT = r0470_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0470_NO_ACCT_FCY_NON_RESIDENT() {
		return R0470_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0470_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0470_NO_ACCT_FCY_NON_RESIDENT) {
		R0470_NO_ACCT_FCY_NON_RESIDENT = r0470_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0470_AMOUNT_FCY_NON_RESIDENT() {
		return R0470_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0470_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0470_AMOUNT_FCY_NON_RESIDENT) {
		R0470_AMOUNT_FCY_NON_RESIDENT = r0470_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0470_TOTAL_AMOUNT() {
		return R0470_TOTAL_AMOUNT;
	}
	public void setR0470_TOTAL_AMOUNT(BigDecimal r0470_TOTAL_AMOUNT) {
		R0470_TOTAL_AMOUNT = r0470_TOTAL_AMOUNT;
	}
	public String getR0480_PRODUCT() {
		return R0480_PRODUCT;
	}
	public void setR0480_PRODUCT(String r0480_PRODUCT) {
		R0480_PRODUCT = r0480_PRODUCT;
	}
	public BigDecimal getR0480_NO_ACCT_AED_RESIDENT() {
		return R0480_NO_ACCT_AED_RESIDENT;
	}
	public void setR0480_NO_ACCT_AED_RESIDENT(BigDecimal r0480_NO_ACCT_AED_RESIDENT) {
		R0480_NO_ACCT_AED_RESIDENT = r0480_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0480_AMOUNT_AED_RESIDENT() {
		return R0480_AMOUNT_AED_RESIDENT;
	}
	public void setR0480_AMOUNT_AED_RESIDENT(BigDecimal r0480_AMOUNT_AED_RESIDENT) {
		R0480_AMOUNT_AED_RESIDENT = r0480_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0480_NO_ACCT_FCY_RESIDENT() {
		return R0480_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0480_NO_ACCT_FCY_RESIDENT(BigDecimal r0480_NO_ACCT_FCY_RESIDENT) {
		R0480_NO_ACCT_FCY_RESIDENT = r0480_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0480_AMOUNT_FCY_RESIDENT() {
		return R0480_AMOUNT_FCY_RESIDENT;
	}
	public void setR0480_AMOUNT_FCY_RESIDENT(BigDecimal r0480_AMOUNT_FCY_RESIDENT) {
		R0480_AMOUNT_FCY_RESIDENT = r0480_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0480_NO_ACCT_AED_NON_RESIDENT() {
		return R0480_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0480_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0480_NO_ACCT_AED_NON_RESIDENT) {
		R0480_NO_ACCT_AED_NON_RESIDENT = r0480_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0480_AMOUNT_AED_NON_RESIDENT() {
		return R0480_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0480_AMOUNT_AED_NON_RESIDENT(BigDecimal r0480_AMOUNT_AED_NON_RESIDENT) {
		R0480_AMOUNT_AED_NON_RESIDENT = r0480_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0480_NO_ACCT_FCY_NON_RESIDENT() {
		return R0480_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0480_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0480_NO_ACCT_FCY_NON_RESIDENT) {
		R0480_NO_ACCT_FCY_NON_RESIDENT = r0480_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0480_AMOUNT_FCY_NON_RESIDENT() {
		return R0480_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0480_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0480_AMOUNT_FCY_NON_RESIDENT) {
		R0480_AMOUNT_FCY_NON_RESIDENT = r0480_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0480_TOTAL_AMOUNT() {
		return R0480_TOTAL_AMOUNT;
	}
	public void setR0480_TOTAL_AMOUNT(BigDecimal r0480_TOTAL_AMOUNT) {
		R0480_TOTAL_AMOUNT = r0480_TOTAL_AMOUNT;
	}
	public String getR0490_PRODUCT() {
		return R0490_PRODUCT;
	}
	public void setR0490_PRODUCT(String r0490_PRODUCT) {
		R0490_PRODUCT = r0490_PRODUCT;
	}
	public BigDecimal getR0490_NO_ACCT_AED_RESIDENT() {
		return R0490_NO_ACCT_AED_RESIDENT;
	}
	public void setR0490_NO_ACCT_AED_RESIDENT(BigDecimal r0490_NO_ACCT_AED_RESIDENT) {
		R0490_NO_ACCT_AED_RESIDENT = r0490_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0490_AMOUNT_AED_RESIDENT() {
		return R0490_AMOUNT_AED_RESIDENT;
	}
	public void setR0490_AMOUNT_AED_RESIDENT(BigDecimal r0490_AMOUNT_AED_RESIDENT) {
		R0490_AMOUNT_AED_RESIDENT = r0490_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0490_NO_ACCT_FCY_RESIDENT() {
		return R0490_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0490_NO_ACCT_FCY_RESIDENT(BigDecimal r0490_NO_ACCT_FCY_RESIDENT) {
		R0490_NO_ACCT_FCY_RESIDENT = r0490_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0490_AMOUNT_FCY_RESIDENT() {
		return R0490_AMOUNT_FCY_RESIDENT;
	}
	public void setR0490_AMOUNT_FCY_RESIDENT(BigDecimal r0490_AMOUNT_FCY_RESIDENT) {
		R0490_AMOUNT_FCY_RESIDENT = r0490_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0490_NO_ACCT_AED_NON_RESIDENT() {
		return R0490_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0490_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0490_NO_ACCT_AED_NON_RESIDENT) {
		R0490_NO_ACCT_AED_NON_RESIDENT = r0490_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0490_AMOUNT_AED_NON_RESIDENT() {
		return R0490_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0490_AMOUNT_AED_NON_RESIDENT(BigDecimal r0490_AMOUNT_AED_NON_RESIDENT) {
		R0490_AMOUNT_AED_NON_RESIDENT = r0490_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0490_NO_ACCT_FCY_NON_RESIDENT() {
		return R0490_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0490_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0490_NO_ACCT_FCY_NON_RESIDENT) {
		R0490_NO_ACCT_FCY_NON_RESIDENT = r0490_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0490_AMOUNT_FCY_NON_RESIDENT() {
		return R0490_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0490_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0490_AMOUNT_FCY_NON_RESIDENT) {
		R0490_AMOUNT_FCY_NON_RESIDENT = r0490_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0490_TOTAL_AMOUNT() {
		return R0490_TOTAL_AMOUNT;
	}
	public void setR0490_TOTAL_AMOUNT(BigDecimal r0490_TOTAL_AMOUNT) {
		R0490_TOTAL_AMOUNT = r0490_TOTAL_AMOUNT;
	}
	public String getR0500_PRODUCT() {
		return R0500_PRODUCT;
	}
	public void setR0500_PRODUCT(String r0500_PRODUCT) {
		R0500_PRODUCT = r0500_PRODUCT;
	}
	public BigDecimal getR0500_NO_ACCT_AED_RESIDENT() {
		return R0500_NO_ACCT_AED_RESIDENT;
	}
	public void setR0500_NO_ACCT_AED_RESIDENT(BigDecimal r0500_NO_ACCT_AED_RESIDENT) {
		R0500_NO_ACCT_AED_RESIDENT = r0500_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0500_AMOUNT_AED_RESIDENT() {
		return R0500_AMOUNT_AED_RESIDENT;
	}
	public void setR0500_AMOUNT_AED_RESIDENT(BigDecimal r0500_AMOUNT_AED_RESIDENT) {
		R0500_AMOUNT_AED_RESIDENT = r0500_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0500_NO_ACCT_FCY_RESIDENT() {
		return R0500_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0500_NO_ACCT_FCY_RESIDENT(BigDecimal r0500_NO_ACCT_FCY_RESIDENT) {
		R0500_NO_ACCT_FCY_RESIDENT = r0500_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0500_AMOUNT_FCY_RESIDENT() {
		return R0500_AMOUNT_FCY_RESIDENT;
	}
	public void setR0500_AMOUNT_FCY_RESIDENT(BigDecimal r0500_AMOUNT_FCY_RESIDENT) {
		R0500_AMOUNT_FCY_RESIDENT = r0500_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0500_NO_ACCT_AED_NON_RESIDENT() {
		return R0500_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0500_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0500_NO_ACCT_AED_NON_RESIDENT) {
		R0500_NO_ACCT_AED_NON_RESIDENT = r0500_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0500_AMOUNT_AED_NON_RESIDENT() {
		return R0500_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0500_AMOUNT_AED_NON_RESIDENT(BigDecimal r0500_AMOUNT_AED_NON_RESIDENT) {
		R0500_AMOUNT_AED_NON_RESIDENT = r0500_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0500_NO_ACCT_FCY_NON_RESIDENT() {
		return R0500_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0500_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0500_NO_ACCT_FCY_NON_RESIDENT) {
		R0500_NO_ACCT_FCY_NON_RESIDENT = r0500_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0500_AMOUNT_FCY_NON_RESIDENT() {
		return R0500_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0500_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0500_AMOUNT_FCY_NON_RESIDENT) {
		R0500_AMOUNT_FCY_NON_RESIDENT = r0500_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0500_TOTAL_AMOUNT() {
		return R0500_TOTAL_AMOUNT;
	}
	public void setR0500_TOTAL_AMOUNT(BigDecimal r0500_TOTAL_AMOUNT) {
		R0500_TOTAL_AMOUNT = r0500_TOTAL_AMOUNT;
	}
	public String getR0510_PRODUCT() {
		return R0510_PRODUCT;
	}
	public void setR0510_PRODUCT(String r0510_PRODUCT) {
		R0510_PRODUCT = r0510_PRODUCT;
	}
	public BigDecimal getR0510_NO_ACCT_AED_RESIDENT() {
		return R0510_NO_ACCT_AED_RESIDENT;
	}
	public void setR0510_NO_ACCT_AED_RESIDENT(BigDecimal r0510_NO_ACCT_AED_RESIDENT) {
		R0510_NO_ACCT_AED_RESIDENT = r0510_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0510_AMOUNT_AED_RESIDENT() {
		return R0510_AMOUNT_AED_RESIDENT;
	}
	public void setR0510_AMOUNT_AED_RESIDENT(BigDecimal r0510_AMOUNT_AED_RESIDENT) {
		R0510_AMOUNT_AED_RESIDENT = r0510_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0510_NO_ACCT_FCY_RESIDENT() {
		return R0510_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0510_NO_ACCT_FCY_RESIDENT(BigDecimal r0510_NO_ACCT_FCY_RESIDENT) {
		R0510_NO_ACCT_FCY_RESIDENT = r0510_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0510_AMOUNT_FCY_RESIDENT() {
		return R0510_AMOUNT_FCY_RESIDENT;
	}
	public void setR0510_AMOUNT_FCY_RESIDENT(BigDecimal r0510_AMOUNT_FCY_RESIDENT) {
		R0510_AMOUNT_FCY_RESIDENT = r0510_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0510_NO_ACCT_AED_NON_RESIDENT() {
		return R0510_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0510_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0510_NO_ACCT_AED_NON_RESIDENT) {
		R0510_NO_ACCT_AED_NON_RESIDENT = r0510_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0510_AMOUNT_AED_NON_RESIDENT() {
		return R0510_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0510_AMOUNT_AED_NON_RESIDENT(BigDecimal r0510_AMOUNT_AED_NON_RESIDENT) {
		R0510_AMOUNT_AED_NON_RESIDENT = r0510_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0510_NO_ACCT_FCY_NON_RESIDENT() {
		return R0510_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0510_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0510_NO_ACCT_FCY_NON_RESIDENT) {
		R0510_NO_ACCT_FCY_NON_RESIDENT = r0510_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0510_AMOUNT_FCY_NON_RESIDENT() {
		return R0510_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0510_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0510_AMOUNT_FCY_NON_RESIDENT) {
		R0510_AMOUNT_FCY_NON_RESIDENT = r0510_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0510_TOTAL_AMOUNT() {
		return R0510_TOTAL_AMOUNT;
	}
	public void setR0510_TOTAL_AMOUNT(BigDecimal r0510_TOTAL_AMOUNT) {
		R0510_TOTAL_AMOUNT = r0510_TOTAL_AMOUNT;
	}
	public String getR0520_PRODUCT() {
		return R0520_PRODUCT;
	}
	public void setR0520_PRODUCT(String r0520_PRODUCT) {
		R0520_PRODUCT = r0520_PRODUCT;
	}
	public BigDecimal getR0520_NO_ACCT_AED_RESIDENT() {
		return R0520_NO_ACCT_AED_RESIDENT;
	}
	public void setR0520_NO_ACCT_AED_RESIDENT(BigDecimal r0520_NO_ACCT_AED_RESIDENT) {
		R0520_NO_ACCT_AED_RESIDENT = r0520_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0520_AMOUNT_AED_RESIDENT() {
		return R0520_AMOUNT_AED_RESIDENT;
	}
	public void setR0520_AMOUNT_AED_RESIDENT(BigDecimal r0520_AMOUNT_AED_RESIDENT) {
		R0520_AMOUNT_AED_RESIDENT = r0520_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0520_NO_ACCT_FCY_RESIDENT() {
		return R0520_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0520_NO_ACCT_FCY_RESIDENT(BigDecimal r0520_NO_ACCT_FCY_RESIDENT) {
		R0520_NO_ACCT_FCY_RESIDENT = r0520_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0520_AMOUNT_FCY_RESIDENT() {
		return R0520_AMOUNT_FCY_RESIDENT;
	}
	public void setR0520_AMOUNT_FCY_RESIDENT(BigDecimal r0520_AMOUNT_FCY_RESIDENT) {
		R0520_AMOUNT_FCY_RESIDENT = r0520_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0520_NO_ACCT_AED_NON_RESIDENT() {
		return R0520_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0520_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0520_NO_ACCT_AED_NON_RESIDENT) {
		R0520_NO_ACCT_AED_NON_RESIDENT = r0520_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0520_AMOUNT_AED_NON_RESIDENT() {
		return R0520_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0520_AMOUNT_AED_NON_RESIDENT(BigDecimal r0520_AMOUNT_AED_NON_RESIDENT) {
		R0520_AMOUNT_AED_NON_RESIDENT = r0520_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0520_NO_ACCT_FCY_NON_RESIDENT() {
		return R0520_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0520_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0520_NO_ACCT_FCY_NON_RESIDENT) {
		R0520_NO_ACCT_FCY_NON_RESIDENT = r0520_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0520_AMOUNT_FCY_NON_RESIDENT() {
		return R0520_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0520_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0520_AMOUNT_FCY_NON_RESIDENT) {
		R0520_AMOUNT_FCY_NON_RESIDENT = r0520_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0520_TOTAL_AMOUNT() {
		return R0520_TOTAL_AMOUNT;
	}
	public void setR0520_TOTAL_AMOUNT(BigDecimal r0520_TOTAL_AMOUNT) {
		R0520_TOTAL_AMOUNT = r0520_TOTAL_AMOUNT;
	}
	public String getR0530_PRODUCT() {
		return R0530_PRODUCT;
	}
	public void setR0530_PRODUCT(String r0530_PRODUCT) {
		R0530_PRODUCT = r0530_PRODUCT;
	}
	public BigDecimal getR0530_NO_ACCT_AED_RESIDENT() {
		return R0530_NO_ACCT_AED_RESIDENT;
	}
	public void setR0530_NO_ACCT_AED_RESIDENT(BigDecimal r0530_NO_ACCT_AED_RESIDENT) {
		R0530_NO_ACCT_AED_RESIDENT = r0530_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0530_AMOUNT_AED_RESIDENT() {
		return R0530_AMOUNT_AED_RESIDENT;
	}
	public void setR0530_AMOUNT_AED_RESIDENT(BigDecimal r0530_AMOUNT_AED_RESIDENT) {
		R0530_AMOUNT_AED_RESIDENT = r0530_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0530_NO_ACCT_FCY_RESIDENT() {
		return R0530_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0530_NO_ACCT_FCY_RESIDENT(BigDecimal r0530_NO_ACCT_FCY_RESIDENT) {
		R0530_NO_ACCT_FCY_RESIDENT = r0530_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0530_AMOUNT_FCY_RESIDENT() {
		return R0530_AMOUNT_FCY_RESIDENT;
	}
	public void setR0530_AMOUNT_FCY_RESIDENT(BigDecimal r0530_AMOUNT_FCY_RESIDENT) {
		R0530_AMOUNT_FCY_RESIDENT = r0530_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0530_NO_ACCT_AED_NON_RESIDENT() {
		return R0530_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0530_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0530_NO_ACCT_AED_NON_RESIDENT) {
		R0530_NO_ACCT_AED_NON_RESIDENT = r0530_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0530_AMOUNT_AED_NON_RESIDENT() {
		return R0530_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0530_AMOUNT_AED_NON_RESIDENT(BigDecimal r0530_AMOUNT_AED_NON_RESIDENT) {
		R0530_AMOUNT_AED_NON_RESIDENT = r0530_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0530_NO_ACCT_FCY_NON_RESIDENT() {
		return R0530_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0530_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0530_NO_ACCT_FCY_NON_RESIDENT) {
		R0530_NO_ACCT_FCY_NON_RESIDENT = r0530_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0530_AMOUNT_FCY_NON_RESIDENT() {
		return R0530_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0530_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0530_AMOUNT_FCY_NON_RESIDENT) {
		R0530_AMOUNT_FCY_NON_RESIDENT = r0530_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0530_TOTAL_AMOUNT() {
		return R0530_TOTAL_AMOUNT;
	}
	public void setR0530_TOTAL_AMOUNT(BigDecimal r0530_TOTAL_AMOUNT) {
		R0530_TOTAL_AMOUNT = r0530_TOTAL_AMOUNT;
	}
	public String getR0540_PRODUCT() {
		return R0540_PRODUCT;
	}
	public void setR0540_PRODUCT(String r0540_PRODUCT) {
		R0540_PRODUCT = r0540_PRODUCT;
	}
	public BigDecimal getR0540_NO_ACCT_AED_RESIDENT() {
		return R0540_NO_ACCT_AED_RESIDENT;
	}
	public void setR0540_NO_ACCT_AED_RESIDENT(BigDecimal r0540_NO_ACCT_AED_RESIDENT) {
		R0540_NO_ACCT_AED_RESIDENT = r0540_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0540_AMOUNT_AED_RESIDENT() {
		return R0540_AMOUNT_AED_RESIDENT;
	}
	public void setR0540_AMOUNT_AED_RESIDENT(BigDecimal r0540_AMOUNT_AED_RESIDENT) {
		R0540_AMOUNT_AED_RESIDENT = r0540_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0540_NO_ACCT_FCY_RESIDENT() {
		return R0540_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0540_NO_ACCT_FCY_RESIDENT(BigDecimal r0540_NO_ACCT_FCY_RESIDENT) {
		R0540_NO_ACCT_FCY_RESIDENT = r0540_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0540_AMOUNT_FCY_RESIDENT() {
		return R0540_AMOUNT_FCY_RESIDENT;
	}
	public void setR0540_AMOUNT_FCY_RESIDENT(BigDecimal r0540_AMOUNT_FCY_RESIDENT) {
		R0540_AMOUNT_FCY_RESIDENT = r0540_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0540_NO_ACCT_AED_NON_RESIDENT() {
		return R0540_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0540_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0540_NO_ACCT_AED_NON_RESIDENT) {
		R0540_NO_ACCT_AED_NON_RESIDENT = r0540_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0540_AMOUNT_AED_NON_RESIDENT() {
		return R0540_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0540_AMOUNT_AED_NON_RESIDENT(BigDecimal r0540_AMOUNT_AED_NON_RESIDENT) {
		R0540_AMOUNT_AED_NON_RESIDENT = r0540_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0540_NO_ACCT_FCY_NON_RESIDENT() {
		return R0540_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0540_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0540_NO_ACCT_FCY_NON_RESIDENT) {
		R0540_NO_ACCT_FCY_NON_RESIDENT = r0540_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0540_AMOUNT_FCY_NON_RESIDENT() {
		return R0540_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0540_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0540_AMOUNT_FCY_NON_RESIDENT) {
		R0540_AMOUNT_FCY_NON_RESIDENT = r0540_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0540_TOTAL_AMOUNT() {
		return R0540_TOTAL_AMOUNT;
	}
	public void setR0540_TOTAL_AMOUNT(BigDecimal r0540_TOTAL_AMOUNT) {
		R0540_TOTAL_AMOUNT = r0540_TOTAL_AMOUNT;
	}
	public String getR0550_PRODUCT() {
		return R0550_PRODUCT;
	}
	public void setR0550_PRODUCT(String r0550_PRODUCT) {
		R0550_PRODUCT = r0550_PRODUCT;
	}
	public BigDecimal getR0550_NO_ACCT_AED_RESIDENT() {
		return R0550_NO_ACCT_AED_RESIDENT;
	}
	public void setR0550_NO_ACCT_AED_RESIDENT(BigDecimal r0550_NO_ACCT_AED_RESIDENT) {
		R0550_NO_ACCT_AED_RESIDENT = r0550_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0550_AMOUNT_AED_RESIDENT() {
		return R0550_AMOUNT_AED_RESIDENT;
	}
	public void setR0550_AMOUNT_AED_RESIDENT(BigDecimal r0550_AMOUNT_AED_RESIDENT) {
		R0550_AMOUNT_AED_RESIDENT = r0550_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0550_NO_ACCT_FCY_RESIDENT() {
		return R0550_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0550_NO_ACCT_FCY_RESIDENT(BigDecimal r0550_NO_ACCT_FCY_RESIDENT) {
		R0550_NO_ACCT_FCY_RESIDENT = r0550_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0550_AMOUNT_FCY_RESIDENT() {
		return R0550_AMOUNT_FCY_RESIDENT;
	}
	public void setR0550_AMOUNT_FCY_RESIDENT(BigDecimal r0550_AMOUNT_FCY_RESIDENT) {
		R0550_AMOUNT_FCY_RESIDENT = r0550_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0550_NO_ACCT_AED_NON_RESIDENT() {
		return R0550_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0550_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0550_NO_ACCT_AED_NON_RESIDENT) {
		R0550_NO_ACCT_AED_NON_RESIDENT = r0550_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0550_AMOUNT_AED_NON_RESIDENT() {
		return R0550_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0550_AMOUNT_AED_NON_RESIDENT(BigDecimal r0550_AMOUNT_AED_NON_RESIDENT) {
		R0550_AMOUNT_AED_NON_RESIDENT = r0550_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0550_NO_ACCT_FCY_NON_RESIDENT() {
		return R0550_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0550_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0550_NO_ACCT_FCY_NON_RESIDENT) {
		R0550_NO_ACCT_FCY_NON_RESIDENT = r0550_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0550_AMOUNT_FCY_NON_RESIDENT() {
		return R0550_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0550_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0550_AMOUNT_FCY_NON_RESIDENT) {
		R0550_AMOUNT_FCY_NON_RESIDENT = r0550_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0550_TOTAL_AMOUNT() {
		return R0550_TOTAL_AMOUNT;
	}
	public void setR0550_TOTAL_AMOUNT(BigDecimal r0550_TOTAL_AMOUNT) {
		R0550_TOTAL_AMOUNT = r0550_TOTAL_AMOUNT;
	}
	public String getR0560_PRODUCT() {
		return R0560_PRODUCT;
	}
	public void setR0560_PRODUCT(String r0560_PRODUCT) {
		R0560_PRODUCT = r0560_PRODUCT;
	}
	public BigDecimal getR0560_NO_ACCT_AED_RESIDENT() {
		return R0560_NO_ACCT_AED_RESIDENT;
	}
	public void setR0560_NO_ACCT_AED_RESIDENT(BigDecimal r0560_NO_ACCT_AED_RESIDENT) {
		R0560_NO_ACCT_AED_RESIDENT = r0560_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0560_AMOUNT_AED_RESIDENT() {
		return R0560_AMOUNT_AED_RESIDENT;
	}
	public void setR0560_AMOUNT_AED_RESIDENT(BigDecimal r0560_AMOUNT_AED_RESIDENT) {
		R0560_AMOUNT_AED_RESIDENT = r0560_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0560_NO_ACCT_FCY_RESIDENT() {
		return R0560_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0560_NO_ACCT_FCY_RESIDENT(BigDecimal r0560_NO_ACCT_FCY_RESIDENT) {
		R0560_NO_ACCT_FCY_RESIDENT = r0560_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0560_AMOUNT_FCY_RESIDENT() {
		return R0560_AMOUNT_FCY_RESIDENT;
	}
	public void setR0560_AMOUNT_FCY_RESIDENT(BigDecimal r0560_AMOUNT_FCY_RESIDENT) {
		R0560_AMOUNT_FCY_RESIDENT = r0560_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0560_NO_ACCT_AED_NON_RESIDENT() {
		return R0560_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0560_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0560_NO_ACCT_AED_NON_RESIDENT) {
		R0560_NO_ACCT_AED_NON_RESIDENT = r0560_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0560_AMOUNT_AED_NON_RESIDENT() {
		return R0560_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0560_AMOUNT_AED_NON_RESIDENT(BigDecimal r0560_AMOUNT_AED_NON_RESIDENT) {
		R0560_AMOUNT_AED_NON_RESIDENT = r0560_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0560_NO_ACCT_FCY_NON_RESIDENT() {
		return R0560_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0560_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0560_NO_ACCT_FCY_NON_RESIDENT) {
		R0560_NO_ACCT_FCY_NON_RESIDENT = r0560_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0560_AMOUNT_FCY_NON_RESIDENT() {
		return R0560_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0560_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0560_AMOUNT_FCY_NON_RESIDENT) {
		R0560_AMOUNT_FCY_NON_RESIDENT = r0560_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0560_TOTAL_AMOUNT() {
		return R0560_TOTAL_AMOUNT;
	}
	public void setR0560_TOTAL_AMOUNT(BigDecimal r0560_TOTAL_AMOUNT) {
		R0560_TOTAL_AMOUNT = r0560_TOTAL_AMOUNT;
	}
	public String getR0570_PRODUCT() {
		return R0570_PRODUCT;
	}
	public void setR0570_PRODUCT(String r0570_PRODUCT) {
		R0570_PRODUCT = r0570_PRODUCT;
	}
	public BigDecimal getR0570_NO_ACCT_AED_RESIDENT() {
		return R0570_NO_ACCT_AED_RESIDENT;
	}
	public void setR0570_NO_ACCT_AED_RESIDENT(BigDecimal r0570_NO_ACCT_AED_RESIDENT) {
		R0570_NO_ACCT_AED_RESIDENT = r0570_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0570_AMOUNT_AED_RESIDENT() {
		return R0570_AMOUNT_AED_RESIDENT;
	}
	public void setR0570_AMOUNT_AED_RESIDENT(BigDecimal r0570_AMOUNT_AED_RESIDENT) {
		R0570_AMOUNT_AED_RESIDENT = r0570_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0570_NO_ACCT_FCY_RESIDENT() {
		return R0570_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0570_NO_ACCT_FCY_RESIDENT(BigDecimal r0570_NO_ACCT_FCY_RESIDENT) {
		R0570_NO_ACCT_FCY_RESIDENT = r0570_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0570_AMOUNT_FCY_RESIDENT() {
		return R0570_AMOUNT_FCY_RESIDENT;
	}
	public void setR0570_AMOUNT_FCY_RESIDENT(BigDecimal r0570_AMOUNT_FCY_RESIDENT) {
		R0570_AMOUNT_FCY_RESIDENT = r0570_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0570_NO_ACCT_AED_NON_RESIDENT() {
		return R0570_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0570_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0570_NO_ACCT_AED_NON_RESIDENT) {
		R0570_NO_ACCT_AED_NON_RESIDENT = r0570_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0570_AMOUNT_AED_NON_RESIDENT() {
		return R0570_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0570_AMOUNT_AED_NON_RESIDENT(BigDecimal r0570_AMOUNT_AED_NON_RESIDENT) {
		R0570_AMOUNT_AED_NON_RESIDENT = r0570_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0570_NO_ACCT_FCY_NON_RESIDENT() {
		return R0570_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0570_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0570_NO_ACCT_FCY_NON_RESIDENT) {
		R0570_NO_ACCT_FCY_NON_RESIDENT = r0570_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0570_AMOUNT_FCY_NON_RESIDENT() {
		return R0570_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0570_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0570_AMOUNT_FCY_NON_RESIDENT) {
		R0570_AMOUNT_FCY_NON_RESIDENT = r0570_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0570_TOTAL_AMOUNT() {
		return R0570_TOTAL_AMOUNT;
	}
	public void setR0570_TOTAL_AMOUNT(BigDecimal r0570_TOTAL_AMOUNT) {
		R0570_TOTAL_AMOUNT = r0570_TOTAL_AMOUNT;
	}
	public String getR0580_PRODUCT() {
		return R0580_PRODUCT;
	}
	public void setR0580_PRODUCT(String r0580_PRODUCT) {
		R0580_PRODUCT = r0580_PRODUCT;
	}
	public BigDecimal getR0580_NO_ACCT_AED_RESIDENT() {
		return R0580_NO_ACCT_AED_RESIDENT;
	}
	public void setR0580_NO_ACCT_AED_RESIDENT(BigDecimal r0580_NO_ACCT_AED_RESIDENT) {
		R0580_NO_ACCT_AED_RESIDENT = r0580_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0580_AMOUNT_AED_RESIDENT() {
		return R0580_AMOUNT_AED_RESIDENT;
	}
	public void setR0580_AMOUNT_AED_RESIDENT(BigDecimal r0580_AMOUNT_AED_RESIDENT) {
		R0580_AMOUNT_AED_RESIDENT = r0580_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0580_NO_ACCT_FCY_RESIDENT() {
		return R0580_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0580_NO_ACCT_FCY_RESIDENT(BigDecimal r0580_NO_ACCT_FCY_RESIDENT) {
		R0580_NO_ACCT_FCY_RESIDENT = r0580_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0580_AMOUNT_FCY_RESIDENT() {
		return R0580_AMOUNT_FCY_RESIDENT;
	}
	public void setR0580_AMOUNT_FCY_RESIDENT(BigDecimal r0580_AMOUNT_FCY_RESIDENT) {
		R0580_AMOUNT_FCY_RESIDENT = r0580_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0580_NO_ACCT_AED_NON_RESIDENT() {
		return R0580_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0580_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0580_NO_ACCT_AED_NON_RESIDENT) {
		R0580_NO_ACCT_AED_NON_RESIDENT = r0580_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0580_AMOUNT_AED_NON_RESIDENT() {
		return R0580_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0580_AMOUNT_AED_NON_RESIDENT(BigDecimal r0580_AMOUNT_AED_NON_RESIDENT) {
		R0580_AMOUNT_AED_NON_RESIDENT = r0580_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0580_NO_ACCT_FCY_NON_RESIDENT() {
		return R0580_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0580_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0580_NO_ACCT_FCY_NON_RESIDENT) {
		R0580_NO_ACCT_FCY_NON_RESIDENT = r0580_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0580_AMOUNT_FCY_NON_RESIDENT() {
		return R0580_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0580_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0580_AMOUNT_FCY_NON_RESIDENT) {
		R0580_AMOUNT_FCY_NON_RESIDENT = r0580_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0580_TOTAL_AMOUNT() {
		return R0580_TOTAL_AMOUNT;
	}
	public void setR0580_TOTAL_AMOUNT(BigDecimal r0580_TOTAL_AMOUNT) {
		R0580_TOTAL_AMOUNT = r0580_TOTAL_AMOUNT;
	}
	public String getR0590_PRODUCT() {
		return R0590_PRODUCT;
	}
	public void setR0590_PRODUCT(String r0590_PRODUCT) {
		R0590_PRODUCT = r0590_PRODUCT;
	}
	public BigDecimal getR0590_NO_ACCT_AED_RESIDENT() {
		return R0590_NO_ACCT_AED_RESIDENT;
	}
	public void setR0590_NO_ACCT_AED_RESIDENT(BigDecimal r0590_NO_ACCT_AED_RESIDENT) {
		R0590_NO_ACCT_AED_RESIDENT = r0590_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0590_AMOUNT_AED_RESIDENT() {
		return R0590_AMOUNT_AED_RESIDENT;
	}
	public void setR0590_AMOUNT_AED_RESIDENT(BigDecimal r0590_AMOUNT_AED_RESIDENT) {
		R0590_AMOUNT_AED_RESIDENT = r0590_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0590_NO_ACCT_FCY_RESIDENT() {
		return R0590_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0590_NO_ACCT_FCY_RESIDENT(BigDecimal r0590_NO_ACCT_FCY_RESIDENT) {
		R0590_NO_ACCT_FCY_RESIDENT = r0590_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0590_AMOUNT_FCY_RESIDENT() {
		return R0590_AMOUNT_FCY_RESIDENT;
	}
	public void setR0590_AMOUNT_FCY_RESIDENT(BigDecimal r0590_AMOUNT_FCY_RESIDENT) {
		R0590_AMOUNT_FCY_RESIDENT = r0590_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0590_NO_ACCT_AED_NON_RESIDENT() {
		return R0590_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0590_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0590_NO_ACCT_AED_NON_RESIDENT) {
		R0590_NO_ACCT_AED_NON_RESIDENT = r0590_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0590_AMOUNT_AED_NON_RESIDENT() {
		return R0590_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0590_AMOUNT_AED_NON_RESIDENT(BigDecimal r0590_AMOUNT_AED_NON_RESIDENT) {
		R0590_AMOUNT_AED_NON_RESIDENT = r0590_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0590_NO_ACCT_FCY_NON_RESIDENT() {
		return R0590_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0590_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0590_NO_ACCT_FCY_NON_RESIDENT) {
		R0590_NO_ACCT_FCY_NON_RESIDENT = r0590_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0590_AMOUNT_FCY_NON_RESIDENT() {
		return R0590_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0590_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0590_AMOUNT_FCY_NON_RESIDENT) {
		R0590_AMOUNT_FCY_NON_RESIDENT = r0590_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0590_TOTAL_AMOUNT() {
		return R0590_TOTAL_AMOUNT;
	}
	public void setR0590_TOTAL_AMOUNT(BigDecimal r0590_TOTAL_AMOUNT) {
		R0590_TOTAL_AMOUNT = r0590_TOTAL_AMOUNT;
	}
	public String getR0600_PRODUCT() {
		return R0600_PRODUCT;
	}
	public void setR0600_PRODUCT(String r0600_PRODUCT) {
		R0600_PRODUCT = r0600_PRODUCT;
	}
	public BigDecimal getR0600_NO_ACCT_AED_RESIDENT() {
		return R0600_NO_ACCT_AED_RESIDENT;
	}
	public void setR0600_NO_ACCT_AED_RESIDENT(BigDecimal r0600_NO_ACCT_AED_RESIDENT) {
		R0600_NO_ACCT_AED_RESIDENT = r0600_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0600_AMOUNT_AED_RESIDENT() {
		return R0600_AMOUNT_AED_RESIDENT;
	}
	public void setR0600_AMOUNT_AED_RESIDENT(BigDecimal r0600_AMOUNT_AED_RESIDENT) {
		R0600_AMOUNT_AED_RESIDENT = r0600_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0600_NO_ACCT_FCY_RESIDENT() {
		return R0600_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0600_NO_ACCT_FCY_RESIDENT(BigDecimal r0600_NO_ACCT_FCY_RESIDENT) {
		R0600_NO_ACCT_FCY_RESIDENT = r0600_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0600_AMOUNT_FCY_RESIDENT() {
		return R0600_AMOUNT_FCY_RESIDENT;
	}
	public void setR0600_AMOUNT_FCY_RESIDENT(BigDecimal r0600_AMOUNT_FCY_RESIDENT) {
		R0600_AMOUNT_FCY_RESIDENT = r0600_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0600_NO_ACCT_AED_NON_RESIDENT() {
		return R0600_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0600_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0600_NO_ACCT_AED_NON_RESIDENT) {
		R0600_NO_ACCT_AED_NON_RESIDENT = r0600_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0600_AMOUNT_AED_NON_RESIDENT() {
		return R0600_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0600_AMOUNT_AED_NON_RESIDENT(BigDecimal r0600_AMOUNT_AED_NON_RESIDENT) {
		R0600_AMOUNT_AED_NON_RESIDENT = r0600_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0600_NO_ACCT_FCY_NON_RESIDENT() {
		return R0600_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0600_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0600_NO_ACCT_FCY_NON_RESIDENT) {
		R0600_NO_ACCT_FCY_NON_RESIDENT = r0600_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0600_AMOUNT_FCY_NON_RESIDENT() {
		return R0600_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0600_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0600_AMOUNT_FCY_NON_RESIDENT) {
		R0600_AMOUNT_FCY_NON_RESIDENT = r0600_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0600_TOTAL_AMOUNT() {
		return R0600_TOTAL_AMOUNT;
	}
	public void setR0600_TOTAL_AMOUNT(BigDecimal r0600_TOTAL_AMOUNT) {
		R0600_TOTAL_AMOUNT = r0600_TOTAL_AMOUNT;
	}
	public String getR0610_PRODUCT() {
		return R0610_PRODUCT;
	}
	public void setR0610_PRODUCT(String r0610_PRODUCT) {
		R0610_PRODUCT = r0610_PRODUCT;
	}
	public BigDecimal getR0610_NO_ACCT_AED_RESIDENT() {
		return R0610_NO_ACCT_AED_RESIDENT;
	}
	public void setR0610_NO_ACCT_AED_RESIDENT(BigDecimal r0610_NO_ACCT_AED_RESIDENT) {
		R0610_NO_ACCT_AED_RESIDENT = r0610_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0610_AMOUNT_AED_RESIDENT() {
		return R0610_AMOUNT_AED_RESIDENT;
	}
	public void setR0610_AMOUNT_AED_RESIDENT(BigDecimal r0610_AMOUNT_AED_RESIDENT) {
		R0610_AMOUNT_AED_RESIDENT = r0610_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0610_NO_ACCT_FCY_RESIDENT() {
		return R0610_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0610_NO_ACCT_FCY_RESIDENT(BigDecimal r0610_NO_ACCT_FCY_RESIDENT) {
		R0610_NO_ACCT_FCY_RESIDENT = r0610_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0610_AMOUNT_FCY_RESIDENT() {
		return R0610_AMOUNT_FCY_RESIDENT;
	}
	public void setR0610_AMOUNT_FCY_RESIDENT(BigDecimal r0610_AMOUNT_FCY_RESIDENT) {
		R0610_AMOUNT_FCY_RESIDENT = r0610_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0610_NO_ACCT_AED_NON_RESIDENT() {
		return R0610_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0610_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0610_NO_ACCT_AED_NON_RESIDENT) {
		R0610_NO_ACCT_AED_NON_RESIDENT = r0610_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0610_AMOUNT_AED_NON_RESIDENT() {
		return R0610_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0610_AMOUNT_AED_NON_RESIDENT(BigDecimal r0610_AMOUNT_AED_NON_RESIDENT) {
		R0610_AMOUNT_AED_NON_RESIDENT = r0610_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0610_NO_ACCT_FCY_NON_RESIDENT() {
		return R0610_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0610_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0610_NO_ACCT_FCY_NON_RESIDENT) {
		R0610_NO_ACCT_FCY_NON_RESIDENT = r0610_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0610_AMOUNT_FCY_NON_RESIDENT() {
		return R0610_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0610_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0610_AMOUNT_FCY_NON_RESIDENT) {
		R0610_AMOUNT_FCY_NON_RESIDENT = r0610_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0610_TOTAL_AMOUNT() {
		return R0610_TOTAL_AMOUNT;
	}
	public void setR0610_TOTAL_AMOUNT(BigDecimal r0610_TOTAL_AMOUNT) {
		R0610_TOTAL_AMOUNT = r0610_TOTAL_AMOUNT;
	}
	public String getR0620_PRODUCT() {
		return R0620_PRODUCT;
	}
	public void setR0620_PRODUCT(String r0620_PRODUCT) {
		R0620_PRODUCT = r0620_PRODUCT;
	}
	public BigDecimal getR0620_NO_ACCT_AED_RESIDENT() {
		return R0620_NO_ACCT_AED_RESIDENT;
	}
	public void setR0620_NO_ACCT_AED_RESIDENT(BigDecimal r0620_NO_ACCT_AED_RESIDENT) {
		R0620_NO_ACCT_AED_RESIDENT = r0620_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0620_AMOUNT_AED_RESIDENT() {
		return R0620_AMOUNT_AED_RESIDENT;
	}
	public void setR0620_AMOUNT_AED_RESIDENT(BigDecimal r0620_AMOUNT_AED_RESIDENT) {
		R0620_AMOUNT_AED_RESIDENT = r0620_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0620_NO_ACCT_FCY_RESIDENT() {
		return R0620_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0620_NO_ACCT_FCY_RESIDENT(BigDecimal r0620_NO_ACCT_FCY_RESIDENT) {
		R0620_NO_ACCT_FCY_RESIDENT = r0620_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0620_AMOUNT_FCY_RESIDENT() {
		return R0620_AMOUNT_FCY_RESIDENT;
	}
	public void setR0620_AMOUNT_FCY_RESIDENT(BigDecimal r0620_AMOUNT_FCY_RESIDENT) {
		R0620_AMOUNT_FCY_RESIDENT = r0620_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0620_NO_ACCT_AED_NON_RESIDENT() {
		return R0620_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0620_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0620_NO_ACCT_AED_NON_RESIDENT) {
		R0620_NO_ACCT_AED_NON_RESIDENT = r0620_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0620_AMOUNT_AED_NON_RESIDENT() {
		return R0620_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0620_AMOUNT_AED_NON_RESIDENT(BigDecimal r0620_AMOUNT_AED_NON_RESIDENT) {
		R0620_AMOUNT_AED_NON_RESIDENT = r0620_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0620_NO_ACCT_FCY_NON_RESIDENT() {
		return R0620_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0620_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0620_NO_ACCT_FCY_NON_RESIDENT) {
		R0620_NO_ACCT_FCY_NON_RESIDENT = r0620_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0620_AMOUNT_FCY_NON_RESIDENT() {
		return R0620_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0620_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0620_AMOUNT_FCY_NON_RESIDENT) {
		R0620_AMOUNT_FCY_NON_RESIDENT = r0620_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0620_TOTAL_AMOUNT() {
		return R0620_TOTAL_AMOUNT;
	}
	public void setR0620_TOTAL_AMOUNT(BigDecimal r0620_TOTAL_AMOUNT) {
		R0620_TOTAL_AMOUNT = r0620_TOTAL_AMOUNT;
	}
	public String getR0630_PRODUCT() {
		return R0630_PRODUCT;
	}
	public void setR0630_PRODUCT(String r0630_PRODUCT) {
		R0630_PRODUCT = r0630_PRODUCT;
	}
	public BigDecimal getR0630_NO_ACCT_AED_RESIDENT() {
		return R0630_NO_ACCT_AED_RESIDENT;
	}
	public void setR0630_NO_ACCT_AED_RESIDENT(BigDecimal r0630_NO_ACCT_AED_RESIDENT) {
		R0630_NO_ACCT_AED_RESIDENT = r0630_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0630_AMOUNT_AED_RESIDENT() {
		return R0630_AMOUNT_AED_RESIDENT;
	}
	public void setR0630_AMOUNT_AED_RESIDENT(BigDecimal r0630_AMOUNT_AED_RESIDENT) {
		R0630_AMOUNT_AED_RESIDENT = r0630_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0630_NO_ACCT_FCY_RESIDENT() {
		return R0630_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0630_NO_ACCT_FCY_RESIDENT(BigDecimal r0630_NO_ACCT_FCY_RESIDENT) {
		R0630_NO_ACCT_FCY_RESIDENT = r0630_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0630_AMOUNT_FCY_RESIDENT() {
		return R0630_AMOUNT_FCY_RESIDENT;
	}
	public void setR0630_AMOUNT_FCY_RESIDENT(BigDecimal r0630_AMOUNT_FCY_RESIDENT) {
		R0630_AMOUNT_FCY_RESIDENT = r0630_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0630_NO_ACCT_AED_NON_RESIDENT() {
		return R0630_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0630_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0630_NO_ACCT_AED_NON_RESIDENT) {
		R0630_NO_ACCT_AED_NON_RESIDENT = r0630_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0630_AMOUNT_AED_NON_RESIDENT() {
		return R0630_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0630_AMOUNT_AED_NON_RESIDENT(BigDecimal r0630_AMOUNT_AED_NON_RESIDENT) {
		R0630_AMOUNT_AED_NON_RESIDENT = r0630_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0630_NO_ACCT_FCY_NON_RESIDENT() {
		return R0630_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0630_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0630_NO_ACCT_FCY_NON_RESIDENT) {
		R0630_NO_ACCT_FCY_NON_RESIDENT = r0630_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0630_AMOUNT_FCY_NON_RESIDENT() {
		return R0630_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0630_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0630_AMOUNT_FCY_NON_RESIDENT) {
		R0630_AMOUNT_FCY_NON_RESIDENT = r0630_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0630_TOTAL_AMOUNT() {
		return R0630_TOTAL_AMOUNT;
	}
	public void setR0630_TOTAL_AMOUNT(BigDecimal r0630_TOTAL_AMOUNT) {
		R0630_TOTAL_AMOUNT = r0630_TOTAL_AMOUNT;
	}
	public String getR0640_PRODUCT() {
		return R0640_PRODUCT;
	}
	public void setR0640_PRODUCT(String r0640_PRODUCT) {
		R0640_PRODUCT = r0640_PRODUCT;
	}
	public BigDecimal getR0640_NO_ACCT_AED_RESIDENT() {
		return R0640_NO_ACCT_AED_RESIDENT;
	}
	public void setR0640_NO_ACCT_AED_RESIDENT(BigDecimal r0640_NO_ACCT_AED_RESIDENT) {
		R0640_NO_ACCT_AED_RESIDENT = r0640_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0640_AMOUNT_AED_RESIDENT() {
		return R0640_AMOUNT_AED_RESIDENT;
	}
	public void setR0640_AMOUNT_AED_RESIDENT(BigDecimal r0640_AMOUNT_AED_RESIDENT) {
		R0640_AMOUNT_AED_RESIDENT = r0640_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0640_NO_ACCT_FCY_RESIDENT() {
		return R0640_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0640_NO_ACCT_FCY_RESIDENT(BigDecimal r0640_NO_ACCT_FCY_RESIDENT) {
		R0640_NO_ACCT_FCY_RESIDENT = r0640_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0640_AMOUNT_FCY_RESIDENT() {
		return R0640_AMOUNT_FCY_RESIDENT;
	}
	public void setR0640_AMOUNT_FCY_RESIDENT(BigDecimal r0640_AMOUNT_FCY_RESIDENT) {
		R0640_AMOUNT_FCY_RESIDENT = r0640_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0640_NO_ACCT_AED_NON_RESIDENT() {
		return R0640_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0640_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0640_NO_ACCT_AED_NON_RESIDENT) {
		R0640_NO_ACCT_AED_NON_RESIDENT = r0640_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0640_AMOUNT_AED_NON_RESIDENT() {
		return R0640_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0640_AMOUNT_AED_NON_RESIDENT(BigDecimal r0640_AMOUNT_AED_NON_RESIDENT) {
		R0640_AMOUNT_AED_NON_RESIDENT = r0640_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0640_NO_ACCT_FCY_NON_RESIDENT() {
		return R0640_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0640_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0640_NO_ACCT_FCY_NON_RESIDENT) {
		R0640_NO_ACCT_FCY_NON_RESIDENT = r0640_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0640_AMOUNT_FCY_NON_RESIDENT() {
		return R0640_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0640_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0640_AMOUNT_FCY_NON_RESIDENT) {
		R0640_AMOUNT_FCY_NON_RESIDENT = r0640_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0640_TOTAL_AMOUNT() {
		return R0640_TOTAL_AMOUNT;
	}
	public void setR0640_TOTAL_AMOUNT(BigDecimal r0640_TOTAL_AMOUNT) {
		R0640_TOTAL_AMOUNT = r0640_TOTAL_AMOUNT;
	}
	public String getR0650_PRODUCT() {
		return R0650_PRODUCT;
	}
	public void setR0650_PRODUCT(String r0650_PRODUCT) {
		R0650_PRODUCT = r0650_PRODUCT;
	}
	public BigDecimal getR0650_NO_ACCT_AED_RESIDENT() {
		return R0650_NO_ACCT_AED_RESIDENT;
	}
	public void setR0650_NO_ACCT_AED_RESIDENT(BigDecimal r0650_NO_ACCT_AED_RESIDENT) {
		R0650_NO_ACCT_AED_RESIDENT = r0650_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0650_AMOUNT_AED_RESIDENT() {
		return R0650_AMOUNT_AED_RESIDENT;
	}
	public void setR0650_AMOUNT_AED_RESIDENT(BigDecimal r0650_AMOUNT_AED_RESIDENT) {
		R0650_AMOUNT_AED_RESIDENT = r0650_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0650_NO_ACCT_FCY_RESIDENT() {
		return R0650_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0650_NO_ACCT_FCY_RESIDENT(BigDecimal r0650_NO_ACCT_FCY_RESIDENT) {
		R0650_NO_ACCT_FCY_RESIDENT = r0650_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0650_AMOUNT_FCY_RESIDENT() {
		return R0650_AMOUNT_FCY_RESIDENT;
	}
	public void setR0650_AMOUNT_FCY_RESIDENT(BigDecimal r0650_AMOUNT_FCY_RESIDENT) {
		R0650_AMOUNT_FCY_RESIDENT = r0650_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0650_NO_ACCT_AED_NON_RESIDENT() {
		return R0650_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0650_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0650_NO_ACCT_AED_NON_RESIDENT) {
		R0650_NO_ACCT_AED_NON_RESIDENT = r0650_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0650_AMOUNT_AED_NON_RESIDENT() {
		return R0650_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0650_AMOUNT_AED_NON_RESIDENT(BigDecimal r0650_AMOUNT_AED_NON_RESIDENT) {
		R0650_AMOUNT_AED_NON_RESIDENT = r0650_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0650_NO_ACCT_FCY_NON_RESIDENT() {
		return R0650_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0650_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0650_NO_ACCT_FCY_NON_RESIDENT) {
		R0650_NO_ACCT_FCY_NON_RESIDENT = r0650_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0650_AMOUNT_FCY_NON_RESIDENT() {
		return R0650_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0650_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0650_AMOUNT_FCY_NON_RESIDENT) {
		R0650_AMOUNT_FCY_NON_RESIDENT = r0650_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0650_TOTAL_AMOUNT() {
		return R0650_TOTAL_AMOUNT;
	}
	public void setR0650_TOTAL_AMOUNT(BigDecimal r0650_TOTAL_AMOUNT) {
		R0650_TOTAL_AMOUNT = r0650_TOTAL_AMOUNT;
	}
	public String getR0660_PRODUCT() {
		return R0660_PRODUCT;
	}
	public void setR0660_PRODUCT(String r0660_PRODUCT) {
		R0660_PRODUCT = r0660_PRODUCT;
	}
	public BigDecimal getR0660_NO_ACCT_AED_RESIDENT() {
		return R0660_NO_ACCT_AED_RESIDENT;
	}
	public void setR0660_NO_ACCT_AED_RESIDENT(BigDecimal r0660_NO_ACCT_AED_RESIDENT) {
		R0660_NO_ACCT_AED_RESIDENT = r0660_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0660_AMOUNT_AED_RESIDENT() {
		return R0660_AMOUNT_AED_RESIDENT;
	}
	public void setR0660_AMOUNT_AED_RESIDENT(BigDecimal r0660_AMOUNT_AED_RESIDENT) {
		R0660_AMOUNT_AED_RESIDENT = r0660_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0660_NO_ACCT_FCY_RESIDENT() {
		return R0660_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0660_NO_ACCT_FCY_RESIDENT(BigDecimal r0660_NO_ACCT_FCY_RESIDENT) {
		R0660_NO_ACCT_FCY_RESIDENT = r0660_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0660_AMOUNT_FCY_RESIDENT() {
		return R0660_AMOUNT_FCY_RESIDENT;
	}
	public void setR0660_AMOUNT_FCY_RESIDENT(BigDecimal r0660_AMOUNT_FCY_RESIDENT) {
		R0660_AMOUNT_FCY_RESIDENT = r0660_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0660_NO_ACCT_AED_NON_RESIDENT() {
		return R0660_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0660_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0660_NO_ACCT_AED_NON_RESIDENT) {
		R0660_NO_ACCT_AED_NON_RESIDENT = r0660_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0660_AMOUNT_AED_NON_RESIDENT() {
		return R0660_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0660_AMOUNT_AED_NON_RESIDENT(BigDecimal r0660_AMOUNT_AED_NON_RESIDENT) {
		R0660_AMOUNT_AED_NON_RESIDENT = r0660_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0660_NO_ACCT_FCY_NON_RESIDENT() {
		return R0660_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0660_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0660_NO_ACCT_FCY_NON_RESIDENT) {
		R0660_NO_ACCT_FCY_NON_RESIDENT = r0660_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0660_AMOUNT_FCY_NON_RESIDENT() {
		return R0660_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0660_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0660_AMOUNT_FCY_NON_RESIDENT) {
		R0660_AMOUNT_FCY_NON_RESIDENT = r0660_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0660_TOTAL_AMOUNT() {
		return R0660_TOTAL_AMOUNT;
	}
	public void setR0660_TOTAL_AMOUNT(BigDecimal r0660_TOTAL_AMOUNT) {
		R0660_TOTAL_AMOUNT = r0660_TOTAL_AMOUNT;
	}
	public String getR0670_PRODUCT() {
		return R0670_PRODUCT;
	}
	public void setR0670_PRODUCT(String r0670_PRODUCT) {
		R0670_PRODUCT = r0670_PRODUCT;
	}
	public BigDecimal getR0670_NO_ACCT_AED_RESIDENT() {
		return R0670_NO_ACCT_AED_RESIDENT;
	}
	public void setR0670_NO_ACCT_AED_RESIDENT(BigDecimal r0670_NO_ACCT_AED_RESIDENT) {
		R0670_NO_ACCT_AED_RESIDENT = r0670_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0670_AMOUNT_AED_RESIDENT() {
		return R0670_AMOUNT_AED_RESIDENT;
	}
	public void setR0670_AMOUNT_AED_RESIDENT(BigDecimal r0670_AMOUNT_AED_RESIDENT) {
		R0670_AMOUNT_AED_RESIDENT = r0670_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0670_NO_ACCT_FCY_RESIDENT() {
		return R0670_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0670_NO_ACCT_FCY_RESIDENT(BigDecimal r0670_NO_ACCT_FCY_RESIDENT) {
		R0670_NO_ACCT_FCY_RESIDENT = r0670_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0670_AMOUNT_FCY_RESIDENT() {
		return R0670_AMOUNT_FCY_RESIDENT;
	}
	public void setR0670_AMOUNT_FCY_RESIDENT(BigDecimal r0670_AMOUNT_FCY_RESIDENT) {
		R0670_AMOUNT_FCY_RESIDENT = r0670_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0670_NO_ACCT_AED_NON_RESIDENT() {
		return R0670_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0670_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0670_NO_ACCT_AED_NON_RESIDENT) {
		R0670_NO_ACCT_AED_NON_RESIDENT = r0670_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0670_AMOUNT_AED_NON_RESIDENT() {
		return R0670_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0670_AMOUNT_AED_NON_RESIDENT(BigDecimal r0670_AMOUNT_AED_NON_RESIDENT) {
		R0670_AMOUNT_AED_NON_RESIDENT = r0670_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0670_NO_ACCT_FCY_NON_RESIDENT() {
		return R0670_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0670_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0670_NO_ACCT_FCY_NON_RESIDENT) {
		R0670_NO_ACCT_FCY_NON_RESIDENT = r0670_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0670_AMOUNT_FCY_NON_RESIDENT() {
		return R0670_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0670_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0670_AMOUNT_FCY_NON_RESIDENT) {
		R0670_AMOUNT_FCY_NON_RESIDENT = r0670_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0670_TOTAL_AMOUNT() {
		return R0670_TOTAL_AMOUNT;
	}
	public void setR0670_TOTAL_AMOUNT(BigDecimal r0670_TOTAL_AMOUNT) {
		R0670_TOTAL_AMOUNT = r0670_TOTAL_AMOUNT;
	}
	public String getR0680_PRODUCT() {
		return R0680_PRODUCT;
	}
	public void setR0680_PRODUCT(String r0680_PRODUCT) {
		R0680_PRODUCT = r0680_PRODUCT;
	}
	public BigDecimal getR0680_NO_ACCT_AED_RESIDENT() {
		return R0680_NO_ACCT_AED_RESIDENT;
	}
	public void setR0680_NO_ACCT_AED_RESIDENT(BigDecimal r0680_NO_ACCT_AED_RESIDENT) {
		R0680_NO_ACCT_AED_RESIDENT = r0680_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0680_AMOUNT_AED_RESIDENT() {
		return R0680_AMOUNT_AED_RESIDENT;
	}
	public void setR0680_AMOUNT_AED_RESIDENT(BigDecimal r0680_AMOUNT_AED_RESIDENT) {
		R0680_AMOUNT_AED_RESIDENT = r0680_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0680_NO_ACCT_FCY_RESIDENT() {
		return R0680_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0680_NO_ACCT_FCY_RESIDENT(BigDecimal r0680_NO_ACCT_FCY_RESIDENT) {
		R0680_NO_ACCT_FCY_RESIDENT = r0680_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0680_AMOUNT_FCY_RESIDENT() {
		return R0680_AMOUNT_FCY_RESIDENT;
	}
	public void setR0680_AMOUNT_FCY_RESIDENT(BigDecimal r0680_AMOUNT_FCY_RESIDENT) {
		R0680_AMOUNT_FCY_RESIDENT = r0680_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0680_NO_ACCT_AED_NON_RESIDENT() {
		return R0680_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0680_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0680_NO_ACCT_AED_NON_RESIDENT) {
		R0680_NO_ACCT_AED_NON_RESIDENT = r0680_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0680_AMOUNT_AED_NON_RESIDENT() {
		return R0680_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0680_AMOUNT_AED_NON_RESIDENT(BigDecimal r0680_AMOUNT_AED_NON_RESIDENT) {
		R0680_AMOUNT_AED_NON_RESIDENT = r0680_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0680_NO_ACCT_FCY_NON_RESIDENT() {
		return R0680_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0680_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0680_NO_ACCT_FCY_NON_RESIDENT) {
		R0680_NO_ACCT_FCY_NON_RESIDENT = r0680_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0680_AMOUNT_FCY_NON_RESIDENT() {
		return R0680_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0680_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0680_AMOUNT_FCY_NON_RESIDENT) {
		R0680_AMOUNT_FCY_NON_RESIDENT = r0680_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0680_TOTAL_AMOUNT() {
		return R0680_TOTAL_AMOUNT;
	}
	public void setR0680_TOTAL_AMOUNT(BigDecimal r0680_TOTAL_AMOUNT) {
		R0680_TOTAL_AMOUNT = r0680_TOTAL_AMOUNT;
	}
	public String getR0690_PRODUCT() {
		return R0690_PRODUCT;
	}
	public void setR0690_PRODUCT(String r0690_PRODUCT) {
		R0690_PRODUCT = r0690_PRODUCT;
	}
	public BigDecimal getR0690_NO_ACCT_AED_RESIDENT() {
		return R0690_NO_ACCT_AED_RESIDENT;
	}
	public void setR0690_NO_ACCT_AED_RESIDENT(BigDecimal r0690_NO_ACCT_AED_RESIDENT) {
		R0690_NO_ACCT_AED_RESIDENT = r0690_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0690_AMOUNT_AED_RESIDENT() {
		return R0690_AMOUNT_AED_RESIDENT;
	}
	public void setR0690_AMOUNT_AED_RESIDENT(BigDecimal r0690_AMOUNT_AED_RESIDENT) {
		R0690_AMOUNT_AED_RESIDENT = r0690_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0690_NO_ACCT_FCY_RESIDENT() {
		return R0690_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0690_NO_ACCT_FCY_RESIDENT(BigDecimal r0690_NO_ACCT_FCY_RESIDENT) {
		R0690_NO_ACCT_FCY_RESIDENT = r0690_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0690_AMOUNT_FCY_RESIDENT() {
		return R0690_AMOUNT_FCY_RESIDENT;
	}
	public void setR0690_AMOUNT_FCY_RESIDENT(BigDecimal r0690_AMOUNT_FCY_RESIDENT) {
		R0690_AMOUNT_FCY_RESIDENT = r0690_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0690_NO_ACCT_AED_NON_RESIDENT() {
		return R0690_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0690_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0690_NO_ACCT_AED_NON_RESIDENT) {
		R0690_NO_ACCT_AED_NON_RESIDENT = r0690_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0690_AMOUNT_AED_NON_RESIDENT() {
		return R0690_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0690_AMOUNT_AED_NON_RESIDENT(BigDecimal r0690_AMOUNT_AED_NON_RESIDENT) {
		R0690_AMOUNT_AED_NON_RESIDENT = r0690_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0690_NO_ACCT_FCY_NON_RESIDENT() {
		return R0690_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0690_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0690_NO_ACCT_FCY_NON_RESIDENT) {
		R0690_NO_ACCT_FCY_NON_RESIDENT = r0690_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0690_AMOUNT_FCY_NON_RESIDENT() {
		return R0690_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0690_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0690_AMOUNT_FCY_NON_RESIDENT) {
		R0690_AMOUNT_FCY_NON_RESIDENT = r0690_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0690_TOTAL_AMOUNT() {
		return R0690_TOTAL_AMOUNT;
	}
	public void setR0690_TOTAL_AMOUNT(BigDecimal r0690_TOTAL_AMOUNT) {
		R0690_TOTAL_AMOUNT = r0690_TOTAL_AMOUNT;
	}
	public String getR0700_PRODUCT() {
		return R0700_PRODUCT;
	}
	public void setR0700_PRODUCT(String r0700_PRODUCT) {
		R0700_PRODUCT = r0700_PRODUCT;
	}
	public BigDecimal getR0700_NO_ACCT_AED_RESIDENT() {
		return R0700_NO_ACCT_AED_RESIDENT;
	}
	public void setR0700_NO_ACCT_AED_RESIDENT(BigDecimal r0700_NO_ACCT_AED_RESIDENT) {
		R0700_NO_ACCT_AED_RESIDENT = r0700_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0700_AMOUNT_AED_RESIDENT() {
		return R0700_AMOUNT_AED_RESIDENT;
	}
	public void setR0700_AMOUNT_AED_RESIDENT(BigDecimal r0700_AMOUNT_AED_RESIDENT) {
		R0700_AMOUNT_AED_RESIDENT = r0700_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0700_NO_ACCT_FCY_RESIDENT() {
		return R0700_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0700_NO_ACCT_FCY_RESIDENT(BigDecimal r0700_NO_ACCT_FCY_RESIDENT) {
		R0700_NO_ACCT_FCY_RESIDENT = r0700_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0700_AMOUNT_FCY_RESIDENT() {
		return R0700_AMOUNT_FCY_RESIDENT;
	}
	public void setR0700_AMOUNT_FCY_RESIDENT(BigDecimal r0700_AMOUNT_FCY_RESIDENT) {
		R0700_AMOUNT_FCY_RESIDENT = r0700_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0700_NO_ACCT_AED_NON_RESIDENT() {
		return R0700_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0700_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0700_NO_ACCT_AED_NON_RESIDENT) {
		R0700_NO_ACCT_AED_NON_RESIDENT = r0700_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0700_AMOUNT_AED_NON_RESIDENT() {
		return R0700_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0700_AMOUNT_AED_NON_RESIDENT(BigDecimal r0700_AMOUNT_AED_NON_RESIDENT) {
		R0700_AMOUNT_AED_NON_RESIDENT = r0700_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0700_NO_ACCT_FCY_NON_RESIDENT() {
		return R0700_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0700_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0700_NO_ACCT_FCY_NON_RESIDENT) {
		R0700_NO_ACCT_FCY_NON_RESIDENT = r0700_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0700_AMOUNT_FCY_NON_RESIDENT() {
		return R0700_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0700_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0700_AMOUNT_FCY_NON_RESIDENT) {
		R0700_AMOUNT_FCY_NON_RESIDENT = r0700_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0700_TOTAL_AMOUNT() {
		return R0700_TOTAL_AMOUNT;
	}
	public void setR0700_TOTAL_AMOUNT(BigDecimal r0700_TOTAL_AMOUNT) {
		R0700_TOTAL_AMOUNT = r0700_TOTAL_AMOUNT;
	}
	public String getR0710_PRODUCT() {
		return R0710_PRODUCT;
	}
	public void setR0710_PRODUCT(String r0710_PRODUCT) {
		R0710_PRODUCT = r0710_PRODUCT;
	}
	public BigDecimal getR0710_NO_ACCT_AED_RESIDENT() {
		return R0710_NO_ACCT_AED_RESIDENT;
	}
	public void setR0710_NO_ACCT_AED_RESIDENT(BigDecimal r0710_NO_ACCT_AED_RESIDENT) {
		R0710_NO_ACCT_AED_RESIDENT = r0710_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0710_AMOUNT_AED_RESIDENT() {
		return R0710_AMOUNT_AED_RESIDENT;
	}
	public void setR0710_AMOUNT_AED_RESIDENT(BigDecimal r0710_AMOUNT_AED_RESIDENT) {
		R0710_AMOUNT_AED_RESIDENT = r0710_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0710_NO_ACCT_FCY_RESIDENT() {
		return R0710_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0710_NO_ACCT_FCY_RESIDENT(BigDecimal r0710_NO_ACCT_FCY_RESIDENT) {
		R0710_NO_ACCT_FCY_RESIDENT = r0710_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0710_AMOUNT_FCY_RESIDENT() {
		return R0710_AMOUNT_FCY_RESIDENT;
	}
	public void setR0710_AMOUNT_FCY_RESIDENT(BigDecimal r0710_AMOUNT_FCY_RESIDENT) {
		R0710_AMOUNT_FCY_RESIDENT = r0710_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0710_NO_ACCT_AED_NON_RESIDENT() {
		return R0710_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0710_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0710_NO_ACCT_AED_NON_RESIDENT) {
		R0710_NO_ACCT_AED_NON_RESIDENT = r0710_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0710_AMOUNT_AED_NON_RESIDENT() {
		return R0710_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0710_AMOUNT_AED_NON_RESIDENT(BigDecimal r0710_AMOUNT_AED_NON_RESIDENT) {
		R0710_AMOUNT_AED_NON_RESIDENT = r0710_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0710_NO_ACCT_FCY_NON_RESIDENT() {
		return R0710_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0710_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0710_NO_ACCT_FCY_NON_RESIDENT) {
		R0710_NO_ACCT_FCY_NON_RESIDENT = r0710_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0710_AMOUNT_FCY_NON_RESIDENT() {
		return R0710_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0710_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0710_AMOUNT_FCY_NON_RESIDENT) {
		R0710_AMOUNT_FCY_NON_RESIDENT = r0710_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0710_TOTAL_AMOUNT() {
		return R0710_TOTAL_AMOUNT;
	}
	public void setR0710_TOTAL_AMOUNT(BigDecimal r0710_TOTAL_AMOUNT) {
		R0710_TOTAL_AMOUNT = r0710_TOTAL_AMOUNT;
	}
	public String getR0720_PRODUCT() {
		return R0720_PRODUCT;
	}
	public void setR0720_PRODUCT(String r0720_PRODUCT) {
		R0720_PRODUCT = r0720_PRODUCT;
	}
	public BigDecimal getR0720_NO_ACCT_AED_RESIDENT() {
		return R0720_NO_ACCT_AED_RESIDENT;
	}
	public void setR0720_NO_ACCT_AED_RESIDENT(BigDecimal r0720_NO_ACCT_AED_RESIDENT) {
		R0720_NO_ACCT_AED_RESIDENT = r0720_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0720_AMOUNT_AED_RESIDENT() {
		return R0720_AMOUNT_AED_RESIDENT;
	}
	public void setR0720_AMOUNT_AED_RESIDENT(BigDecimal r0720_AMOUNT_AED_RESIDENT) {
		R0720_AMOUNT_AED_RESIDENT = r0720_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0720_NO_ACCT_FCY_RESIDENT() {
		return R0720_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0720_NO_ACCT_FCY_RESIDENT(BigDecimal r0720_NO_ACCT_FCY_RESIDENT) {
		R0720_NO_ACCT_FCY_RESIDENT = r0720_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0720_AMOUNT_FCY_RESIDENT() {
		return R0720_AMOUNT_FCY_RESIDENT;
	}
	public void setR0720_AMOUNT_FCY_RESIDENT(BigDecimal r0720_AMOUNT_FCY_RESIDENT) {
		R0720_AMOUNT_FCY_RESIDENT = r0720_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0720_NO_ACCT_AED_NON_RESIDENT() {
		return R0720_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0720_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0720_NO_ACCT_AED_NON_RESIDENT) {
		R0720_NO_ACCT_AED_NON_RESIDENT = r0720_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0720_AMOUNT_AED_NON_RESIDENT() {
		return R0720_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0720_AMOUNT_AED_NON_RESIDENT(BigDecimal r0720_AMOUNT_AED_NON_RESIDENT) {
		R0720_AMOUNT_AED_NON_RESIDENT = r0720_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0720_NO_ACCT_FCY_NON_RESIDENT() {
		return R0720_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0720_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0720_NO_ACCT_FCY_NON_RESIDENT) {
		R0720_NO_ACCT_FCY_NON_RESIDENT = r0720_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0720_AMOUNT_FCY_NON_RESIDENT() {
		return R0720_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0720_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0720_AMOUNT_FCY_NON_RESIDENT) {
		R0720_AMOUNT_FCY_NON_RESIDENT = r0720_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0720_TOTAL_AMOUNT() {
		return R0720_TOTAL_AMOUNT;
	}
	public void setR0720_TOTAL_AMOUNT(BigDecimal r0720_TOTAL_AMOUNT) {
		R0720_TOTAL_AMOUNT = r0720_TOTAL_AMOUNT;
	}
	public String getR0730_PRODUCT() {
		return R0730_PRODUCT;
	}
	public void setR0730_PRODUCT(String r0730_PRODUCT) {
		R0730_PRODUCT = r0730_PRODUCT;
	}
	public BigDecimal getR0730_NO_ACCT_AED_RESIDENT() {
		return R0730_NO_ACCT_AED_RESIDENT;
	}
	public void setR0730_NO_ACCT_AED_RESIDENT(BigDecimal r0730_NO_ACCT_AED_RESIDENT) {
		R0730_NO_ACCT_AED_RESIDENT = r0730_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0730_AMOUNT_AED_RESIDENT() {
		return R0730_AMOUNT_AED_RESIDENT;
	}
	public void setR0730_AMOUNT_AED_RESIDENT(BigDecimal r0730_AMOUNT_AED_RESIDENT) {
		R0730_AMOUNT_AED_RESIDENT = r0730_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0730_NO_ACCT_FCY_RESIDENT() {
		return R0730_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0730_NO_ACCT_FCY_RESIDENT(BigDecimal r0730_NO_ACCT_FCY_RESIDENT) {
		R0730_NO_ACCT_FCY_RESIDENT = r0730_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0730_AMOUNT_FCY_RESIDENT() {
		return R0730_AMOUNT_FCY_RESIDENT;
	}
	public void setR0730_AMOUNT_FCY_RESIDENT(BigDecimal r0730_AMOUNT_FCY_RESIDENT) {
		R0730_AMOUNT_FCY_RESIDENT = r0730_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0730_NO_ACCT_AED_NON_RESIDENT() {
		return R0730_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0730_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0730_NO_ACCT_AED_NON_RESIDENT) {
		R0730_NO_ACCT_AED_NON_RESIDENT = r0730_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0730_AMOUNT_AED_NON_RESIDENT() {
		return R0730_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0730_AMOUNT_AED_NON_RESIDENT(BigDecimal r0730_AMOUNT_AED_NON_RESIDENT) {
		R0730_AMOUNT_AED_NON_RESIDENT = r0730_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0730_NO_ACCT_FCY_NON_RESIDENT() {
		return R0730_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0730_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0730_NO_ACCT_FCY_NON_RESIDENT) {
		R0730_NO_ACCT_FCY_NON_RESIDENT = r0730_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0730_AMOUNT_FCY_NON_RESIDENT() {
		return R0730_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0730_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0730_AMOUNT_FCY_NON_RESIDENT) {
		R0730_AMOUNT_FCY_NON_RESIDENT = r0730_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0730_TOTAL_AMOUNT() {
		return R0730_TOTAL_AMOUNT;
	}
	public void setR0730_TOTAL_AMOUNT(BigDecimal r0730_TOTAL_AMOUNT) {
		R0730_TOTAL_AMOUNT = r0730_TOTAL_AMOUNT;
	}
	public String getR0740_PRODUCT() {
		return R0740_PRODUCT;
	}
	public void setR0740_PRODUCT(String r0740_PRODUCT) {
		R0740_PRODUCT = r0740_PRODUCT;
	}
	public BigDecimal getR0740_NO_ACCT_AED_RESIDENT() {
		return R0740_NO_ACCT_AED_RESIDENT;
	}
	public void setR0740_NO_ACCT_AED_RESIDENT(BigDecimal r0740_NO_ACCT_AED_RESIDENT) {
		R0740_NO_ACCT_AED_RESIDENT = r0740_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0740_AMOUNT_AED_RESIDENT() {
		return R0740_AMOUNT_AED_RESIDENT;
	}
	public void setR0740_AMOUNT_AED_RESIDENT(BigDecimal r0740_AMOUNT_AED_RESIDENT) {
		R0740_AMOUNT_AED_RESIDENT = r0740_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0740_NO_ACCT_FCY_RESIDENT() {
		return R0740_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0740_NO_ACCT_FCY_RESIDENT(BigDecimal r0740_NO_ACCT_FCY_RESIDENT) {
		R0740_NO_ACCT_FCY_RESIDENT = r0740_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0740_AMOUNT_FCY_RESIDENT() {
		return R0740_AMOUNT_FCY_RESIDENT;
	}
	public void setR0740_AMOUNT_FCY_RESIDENT(BigDecimal r0740_AMOUNT_FCY_RESIDENT) {
		R0740_AMOUNT_FCY_RESIDENT = r0740_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0740_NO_ACCT_AED_NON_RESIDENT() {
		return R0740_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0740_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0740_NO_ACCT_AED_NON_RESIDENT) {
		R0740_NO_ACCT_AED_NON_RESIDENT = r0740_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0740_AMOUNT_AED_NON_RESIDENT() {
		return R0740_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0740_AMOUNT_AED_NON_RESIDENT(BigDecimal r0740_AMOUNT_AED_NON_RESIDENT) {
		R0740_AMOUNT_AED_NON_RESIDENT = r0740_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0740_NO_ACCT_FCY_NON_RESIDENT() {
		return R0740_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0740_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0740_NO_ACCT_FCY_NON_RESIDENT) {
		R0740_NO_ACCT_FCY_NON_RESIDENT = r0740_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0740_AMOUNT_FCY_NON_RESIDENT() {
		return R0740_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0740_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0740_AMOUNT_FCY_NON_RESIDENT) {
		R0740_AMOUNT_FCY_NON_RESIDENT = r0740_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0740_TOTAL_AMOUNT() {
		return R0740_TOTAL_AMOUNT;
	}
	public void setR0740_TOTAL_AMOUNT(BigDecimal r0740_TOTAL_AMOUNT) {
		R0740_TOTAL_AMOUNT = r0740_TOTAL_AMOUNT;
	}
	public String getR0750_PRODUCT() {
		return R0750_PRODUCT;
	}
	public void setR0750_PRODUCT(String r0750_PRODUCT) {
		R0750_PRODUCT = r0750_PRODUCT;
	}
	public BigDecimal getR0750_NO_ACCT_AED_RESIDENT() {
		return R0750_NO_ACCT_AED_RESIDENT;
	}
	public void setR0750_NO_ACCT_AED_RESIDENT(BigDecimal r0750_NO_ACCT_AED_RESIDENT) {
		R0750_NO_ACCT_AED_RESIDENT = r0750_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0750_AMOUNT_AED_RESIDENT() {
		return R0750_AMOUNT_AED_RESIDENT;
	}
	public void setR0750_AMOUNT_AED_RESIDENT(BigDecimal r0750_AMOUNT_AED_RESIDENT) {
		R0750_AMOUNT_AED_RESIDENT = r0750_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0750_NO_ACCT_FCY_RESIDENT() {
		return R0750_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0750_NO_ACCT_FCY_RESIDENT(BigDecimal r0750_NO_ACCT_FCY_RESIDENT) {
		R0750_NO_ACCT_FCY_RESIDENT = r0750_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0750_AMOUNT_FCY_RESIDENT() {
		return R0750_AMOUNT_FCY_RESIDENT;
	}
	public void setR0750_AMOUNT_FCY_RESIDENT(BigDecimal r0750_AMOUNT_FCY_RESIDENT) {
		R0750_AMOUNT_FCY_RESIDENT = r0750_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0750_NO_ACCT_AED_NON_RESIDENT() {
		return R0750_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0750_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0750_NO_ACCT_AED_NON_RESIDENT) {
		R0750_NO_ACCT_AED_NON_RESIDENT = r0750_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0750_AMOUNT_AED_NON_RESIDENT() {
		return R0750_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0750_AMOUNT_AED_NON_RESIDENT(BigDecimal r0750_AMOUNT_AED_NON_RESIDENT) {
		R0750_AMOUNT_AED_NON_RESIDENT = r0750_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0750_NO_ACCT_FCY_NON_RESIDENT() {
		return R0750_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0750_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0750_NO_ACCT_FCY_NON_RESIDENT) {
		R0750_NO_ACCT_FCY_NON_RESIDENT = r0750_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0750_AMOUNT_FCY_NON_RESIDENT() {
		return R0750_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0750_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0750_AMOUNT_FCY_NON_RESIDENT) {
		R0750_AMOUNT_FCY_NON_RESIDENT = r0750_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0750_TOTAL_AMOUNT() {
		return R0750_TOTAL_AMOUNT;
	}
	public void setR0750_TOTAL_AMOUNT(BigDecimal r0750_TOTAL_AMOUNT) {
		R0750_TOTAL_AMOUNT = r0750_TOTAL_AMOUNT;
	}
	public String getR0760_PRODUCT() {
		return R0760_PRODUCT;
	}
	public void setR0760_PRODUCT(String r0760_PRODUCT) {
		R0760_PRODUCT = r0760_PRODUCT;
	}
	public BigDecimal getR0760_NO_ACCT_AED_RESIDENT() {
		return R0760_NO_ACCT_AED_RESIDENT;
	}
	public void setR0760_NO_ACCT_AED_RESIDENT(BigDecimal r0760_NO_ACCT_AED_RESIDENT) {
		R0760_NO_ACCT_AED_RESIDENT = r0760_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0760_AMOUNT_AED_RESIDENT() {
		return R0760_AMOUNT_AED_RESIDENT;
	}
	public void setR0760_AMOUNT_AED_RESIDENT(BigDecimal r0760_AMOUNT_AED_RESIDENT) {
		R0760_AMOUNT_AED_RESIDENT = r0760_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0760_NO_ACCT_FCY_RESIDENT() {
		return R0760_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0760_NO_ACCT_FCY_RESIDENT(BigDecimal r0760_NO_ACCT_FCY_RESIDENT) {
		R0760_NO_ACCT_FCY_RESIDENT = r0760_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0760_AMOUNT_FCY_RESIDENT() {
		return R0760_AMOUNT_FCY_RESIDENT;
	}
	public void setR0760_AMOUNT_FCY_RESIDENT(BigDecimal r0760_AMOUNT_FCY_RESIDENT) {
		R0760_AMOUNT_FCY_RESIDENT = r0760_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0760_NO_ACCT_AED_NON_RESIDENT() {
		return R0760_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0760_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0760_NO_ACCT_AED_NON_RESIDENT) {
		R0760_NO_ACCT_AED_NON_RESIDENT = r0760_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0760_AMOUNT_AED_NON_RESIDENT() {
		return R0760_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0760_AMOUNT_AED_NON_RESIDENT(BigDecimal r0760_AMOUNT_AED_NON_RESIDENT) {
		R0760_AMOUNT_AED_NON_RESIDENT = r0760_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0760_NO_ACCT_FCY_NON_RESIDENT() {
		return R0760_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0760_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0760_NO_ACCT_FCY_NON_RESIDENT) {
		R0760_NO_ACCT_FCY_NON_RESIDENT = r0760_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0760_AMOUNT_FCY_NON_RESIDENT() {
		return R0760_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0760_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0760_AMOUNT_FCY_NON_RESIDENT) {
		R0760_AMOUNT_FCY_NON_RESIDENT = r0760_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0760_TOTAL_AMOUNT() {
		return R0760_TOTAL_AMOUNT;
	}
	public void setR0760_TOTAL_AMOUNT(BigDecimal r0760_TOTAL_AMOUNT) {
		R0760_TOTAL_AMOUNT = r0760_TOTAL_AMOUNT;
	}
	public String getR0770_PRODUCT() {
		return R0770_PRODUCT;
	}
	public void setR0770_PRODUCT(String r0770_PRODUCT) {
		R0770_PRODUCT = r0770_PRODUCT;
	}
	public BigDecimal getR0770_NO_ACCT_AED_RESIDENT() {
		return R0770_NO_ACCT_AED_RESIDENT;
	}
	public void setR0770_NO_ACCT_AED_RESIDENT(BigDecimal r0770_NO_ACCT_AED_RESIDENT) {
		R0770_NO_ACCT_AED_RESIDENT = r0770_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0770_AMOUNT_AED_RESIDENT() {
		return R0770_AMOUNT_AED_RESIDENT;
	}
	public void setR0770_AMOUNT_AED_RESIDENT(BigDecimal r0770_AMOUNT_AED_RESIDENT) {
		R0770_AMOUNT_AED_RESIDENT = r0770_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0770_NO_ACCT_FCY_RESIDENT() {
		return R0770_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0770_NO_ACCT_FCY_RESIDENT(BigDecimal r0770_NO_ACCT_FCY_RESIDENT) {
		R0770_NO_ACCT_FCY_RESIDENT = r0770_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0770_AMOUNT_FCY_RESIDENT() {
		return R0770_AMOUNT_FCY_RESIDENT;
	}
	public void setR0770_AMOUNT_FCY_RESIDENT(BigDecimal r0770_AMOUNT_FCY_RESIDENT) {
		R0770_AMOUNT_FCY_RESIDENT = r0770_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0770_NO_ACCT_AED_NON_RESIDENT() {
		return R0770_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0770_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0770_NO_ACCT_AED_NON_RESIDENT) {
		R0770_NO_ACCT_AED_NON_RESIDENT = r0770_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0770_AMOUNT_AED_NON_RESIDENT() {
		return R0770_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0770_AMOUNT_AED_NON_RESIDENT(BigDecimal r0770_AMOUNT_AED_NON_RESIDENT) {
		R0770_AMOUNT_AED_NON_RESIDENT = r0770_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0770_NO_ACCT_FCY_NON_RESIDENT() {
		return R0770_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0770_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0770_NO_ACCT_FCY_NON_RESIDENT) {
		R0770_NO_ACCT_FCY_NON_RESIDENT = r0770_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0770_AMOUNT_FCY_NON_RESIDENT() {
		return R0770_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0770_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0770_AMOUNT_FCY_NON_RESIDENT) {
		R0770_AMOUNT_FCY_NON_RESIDENT = r0770_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0770_TOTAL_AMOUNT() {
		return R0770_TOTAL_AMOUNT;
	}
	public void setR0770_TOTAL_AMOUNT(BigDecimal r0770_TOTAL_AMOUNT) {
		R0770_TOTAL_AMOUNT = r0770_TOTAL_AMOUNT;
	}
	public String getR0780_PRODUCT() {
		return R0780_PRODUCT;
	}
	public void setR0780_PRODUCT(String r0780_PRODUCT) {
		R0780_PRODUCT = r0780_PRODUCT;
	}
	public BigDecimal getR0780_NO_ACCT_AED_RESIDENT() {
		return R0780_NO_ACCT_AED_RESIDENT;
	}
	public void setR0780_NO_ACCT_AED_RESIDENT(BigDecimal r0780_NO_ACCT_AED_RESIDENT) {
		R0780_NO_ACCT_AED_RESIDENT = r0780_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0780_AMOUNT_AED_RESIDENT() {
		return R0780_AMOUNT_AED_RESIDENT;
	}
	public void setR0780_AMOUNT_AED_RESIDENT(BigDecimal r0780_AMOUNT_AED_RESIDENT) {
		R0780_AMOUNT_AED_RESIDENT = r0780_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0780_NO_ACCT_FCY_RESIDENT() {
		return R0780_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0780_NO_ACCT_FCY_RESIDENT(BigDecimal r0780_NO_ACCT_FCY_RESIDENT) {
		R0780_NO_ACCT_FCY_RESIDENT = r0780_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0780_AMOUNT_FCY_RESIDENT() {
		return R0780_AMOUNT_FCY_RESIDENT;
	}
	public void setR0780_AMOUNT_FCY_RESIDENT(BigDecimal r0780_AMOUNT_FCY_RESIDENT) {
		R0780_AMOUNT_FCY_RESIDENT = r0780_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0780_NO_ACCT_AED_NON_RESIDENT() {
		return R0780_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0780_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0780_NO_ACCT_AED_NON_RESIDENT) {
		R0780_NO_ACCT_AED_NON_RESIDENT = r0780_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0780_AMOUNT_AED_NON_RESIDENT() {
		return R0780_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0780_AMOUNT_AED_NON_RESIDENT(BigDecimal r0780_AMOUNT_AED_NON_RESIDENT) {
		R0780_AMOUNT_AED_NON_RESIDENT = r0780_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0780_NO_ACCT_FCY_NON_RESIDENT() {
		return R0780_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0780_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0780_NO_ACCT_FCY_NON_RESIDENT) {
		R0780_NO_ACCT_FCY_NON_RESIDENT = r0780_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0780_AMOUNT_FCY_NON_RESIDENT() {
		return R0780_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0780_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0780_AMOUNT_FCY_NON_RESIDENT) {
		R0780_AMOUNT_FCY_NON_RESIDENT = r0780_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0780_TOTAL_AMOUNT() {
		return R0780_TOTAL_AMOUNT;
	}
	public void setR0780_TOTAL_AMOUNT(BigDecimal r0780_TOTAL_AMOUNT) {
		R0780_TOTAL_AMOUNT = r0780_TOTAL_AMOUNT;
	}
	public String getR0790_PRODUCT() {
		return R0790_PRODUCT;
	}
	public void setR0790_PRODUCT(String r0790_PRODUCT) {
		R0790_PRODUCT = r0790_PRODUCT;
	}
	public BigDecimal getR0790_NO_ACCT_AED_RESIDENT() {
		return R0790_NO_ACCT_AED_RESIDENT;
	}
	public void setR0790_NO_ACCT_AED_RESIDENT(BigDecimal r0790_NO_ACCT_AED_RESIDENT) {
		R0790_NO_ACCT_AED_RESIDENT = r0790_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0790_AMOUNT_AED_RESIDENT() {
		return R0790_AMOUNT_AED_RESIDENT;
	}
	public void setR0790_AMOUNT_AED_RESIDENT(BigDecimal r0790_AMOUNT_AED_RESIDENT) {
		R0790_AMOUNT_AED_RESIDENT = r0790_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0790_NO_ACCT_FCY_RESIDENT() {
		return R0790_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0790_NO_ACCT_FCY_RESIDENT(BigDecimal r0790_NO_ACCT_FCY_RESIDENT) {
		R0790_NO_ACCT_FCY_RESIDENT = r0790_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0790_AMOUNT_FCY_RESIDENT() {
		return R0790_AMOUNT_FCY_RESIDENT;
	}
	public void setR0790_AMOUNT_FCY_RESIDENT(BigDecimal r0790_AMOUNT_FCY_RESIDENT) {
		R0790_AMOUNT_FCY_RESIDENT = r0790_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0790_NO_ACCT_AED_NON_RESIDENT() {
		return R0790_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0790_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0790_NO_ACCT_AED_NON_RESIDENT) {
		R0790_NO_ACCT_AED_NON_RESIDENT = r0790_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0790_AMOUNT_AED_NON_RESIDENT() {
		return R0790_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0790_AMOUNT_AED_NON_RESIDENT(BigDecimal r0790_AMOUNT_AED_NON_RESIDENT) {
		R0790_AMOUNT_AED_NON_RESIDENT = r0790_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0790_NO_ACCT_FCY_NON_RESIDENT() {
		return R0790_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0790_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0790_NO_ACCT_FCY_NON_RESIDENT) {
		R0790_NO_ACCT_FCY_NON_RESIDENT = r0790_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0790_AMOUNT_FCY_NON_RESIDENT() {
		return R0790_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0790_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0790_AMOUNT_FCY_NON_RESIDENT) {
		R0790_AMOUNT_FCY_NON_RESIDENT = r0790_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0790_TOTAL_AMOUNT() {
		return R0790_TOTAL_AMOUNT;
	}
	public void setR0790_TOTAL_AMOUNT(BigDecimal r0790_TOTAL_AMOUNT) {
		R0790_TOTAL_AMOUNT = r0790_TOTAL_AMOUNT;
	}
	public String getR0800_PRODUCT() {
		return R0800_PRODUCT;
	}
	public void setR0800_PRODUCT(String r0800_PRODUCT) {
		R0800_PRODUCT = r0800_PRODUCT;
	}
	public BigDecimal getR0800_NO_ACCT_AED_RESIDENT() {
		return R0800_NO_ACCT_AED_RESIDENT;
	}
	public void setR0800_NO_ACCT_AED_RESIDENT(BigDecimal r0800_NO_ACCT_AED_RESIDENT) {
		R0800_NO_ACCT_AED_RESIDENT = r0800_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0800_AMOUNT_AED_RESIDENT() {
		return R0800_AMOUNT_AED_RESIDENT;
	}
	public void setR0800_AMOUNT_AED_RESIDENT(BigDecimal r0800_AMOUNT_AED_RESIDENT) {
		R0800_AMOUNT_AED_RESIDENT = r0800_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0800_NO_ACCT_FCY_RESIDENT() {
		return R0800_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0800_NO_ACCT_FCY_RESIDENT(BigDecimal r0800_NO_ACCT_FCY_RESIDENT) {
		R0800_NO_ACCT_FCY_RESIDENT = r0800_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0800_AMOUNT_FCY_RESIDENT() {
		return R0800_AMOUNT_FCY_RESIDENT;
	}
	public void setR0800_AMOUNT_FCY_RESIDENT(BigDecimal r0800_AMOUNT_FCY_RESIDENT) {
		R0800_AMOUNT_FCY_RESIDENT = r0800_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0800_NO_ACCT_AED_NON_RESIDENT() {
		return R0800_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0800_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0800_NO_ACCT_AED_NON_RESIDENT) {
		R0800_NO_ACCT_AED_NON_RESIDENT = r0800_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0800_AMOUNT_AED_NON_RESIDENT() {
		return R0800_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0800_AMOUNT_AED_NON_RESIDENT(BigDecimal r0800_AMOUNT_AED_NON_RESIDENT) {
		R0800_AMOUNT_AED_NON_RESIDENT = r0800_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0800_NO_ACCT_FCY_NON_RESIDENT() {
		return R0800_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0800_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0800_NO_ACCT_FCY_NON_RESIDENT) {
		R0800_NO_ACCT_FCY_NON_RESIDENT = r0800_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0800_AMOUNT_FCY_NON_RESIDENT() {
		return R0800_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0800_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0800_AMOUNT_FCY_NON_RESIDENT) {
		R0800_AMOUNT_FCY_NON_RESIDENT = r0800_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0800_TOTAL_AMOUNT() {
		return R0800_TOTAL_AMOUNT;
	}
	public void setR0800_TOTAL_AMOUNT(BigDecimal r0800_TOTAL_AMOUNT) {
		R0800_TOTAL_AMOUNT = r0800_TOTAL_AMOUNT;
	}
	public String getR0810_PRODUCT() {
		return R0810_PRODUCT;
	}
	public void setR0810_PRODUCT(String r0810_PRODUCT) {
		R0810_PRODUCT = r0810_PRODUCT;
	}
	public BigDecimal getR0810_NO_ACCT_AED_RESIDENT() {
		return R0810_NO_ACCT_AED_RESIDENT;
	}
	public void setR0810_NO_ACCT_AED_RESIDENT(BigDecimal r0810_NO_ACCT_AED_RESIDENT) {
		R0810_NO_ACCT_AED_RESIDENT = r0810_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0810_AMOUNT_AED_RESIDENT() {
		return R0810_AMOUNT_AED_RESIDENT;
	}
	public void setR0810_AMOUNT_AED_RESIDENT(BigDecimal r0810_AMOUNT_AED_RESIDENT) {
		R0810_AMOUNT_AED_RESIDENT = r0810_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0810_NO_ACCT_FCY_RESIDENT() {
		return R0810_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0810_NO_ACCT_FCY_RESIDENT(BigDecimal r0810_NO_ACCT_FCY_RESIDENT) {
		R0810_NO_ACCT_FCY_RESIDENT = r0810_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0810_AMOUNT_FCY_RESIDENT() {
		return R0810_AMOUNT_FCY_RESIDENT;
	}
	public void setR0810_AMOUNT_FCY_RESIDENT(BigDecimal r0810_AMOUNT_FCY_RESIDENT) {
		R0810_AMOUNT_FCY_RESIDENT = r0810_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0810_NO_ACCT_AED_NON_RESIDENT() {
		return R0810_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0810_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0810_NO_ACCT_AED_NON_RESIDENT) {
		R0810_NO_ACCT_AED_NON_RESIDENT = r0810_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0810_AMOUNT_AED_NON_RESIDENT() {
		return R0810_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0810_AMOUNT_AED_NON_RESIDENT(BigDecimal r0810_AMOUNT_AED_NON_RESIDENT) {
		R0810_AMOUNT_AED_NON_RESIDENT = r0810_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0810_NO_ACCT_FCY_NON_RESIDENT() {
		return R0810_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0810_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0810_NO_ACCT_FCY_NON_RESIDENT) {
		R0810_NO_ACCT_FCY_NON_RESIDENT = r0810_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0810_AMOUNT_FCY_NON_RESIDENT() {
		return R0810_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0810_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0810_AMOUNT_FCY_NON_RESIDENT) {
		R0810_AMOUNT_FCY_NON_RESIDENT = r0810_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0810_TOTAL_AMOUNT() {
		return R0810_TOTAL_AMOUNT;
	}
	public void setR0810_TOTAL_AMOUNT(BigDecimal r0810_TOTAL_AMOUNT) {
		R0810_TOTAL_AMOUNT = r0810_TOTAL_AMOUNT;
	}
	public String getR0820_PRODUCT() {
		return R0820_PRODUCT;
	}
	public void setR0820_PRODUCT(String r0820_PRODUCT) {
		R0820_PRODUCT = r0820_PRODUCT;
	}
	public BigDecimal getR0820_NO_ACCT_AED_RESIDENT() {
		return R0820_NO_ACCT_AED_RESIDENT;
	}
	public void setR0820_NO_ACCT_AED_RESIDENT(BigDecimal r0820_NO_ACCT_AED_RESIDENT) {
		R0820_NO_ACCT_AED_RESIDENT = r0820_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0820_AMOUNT_AED_RESIDENT() {
		return R0820_AMOUNT_AED_RESIDENT;
	}
	public void setR0820_AMOUNT_AED_RESIDENT(BigDecimal r0820_AMOUNT_AED_RESIDENT) {
		R0820_AMOUNT_AED_RESIDENT = r0820_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0820_NO_ACCT_FCY_RESIDENT() {
		return R0820_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0820_NO_ACCT_FCY_RESIDENT(BigDecimal r0820_NO_ACCT_FCY_RESIDENT) {
		R0820_NO_ACCT_FCY_RESIDENT = r0820_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0820_AMOUNT_FCY_RESIDENT() {
		return R0820_AMOUNT_FCY_RESIDENT;
	}
	public void setR0820_AMOUNT_FCY_RESIDENT(BigDecimal r0820_AMOUNT_FCY_RESIDENT) {
		R0820_AMOUNT_FCY_RESIDENT = r0820_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0820_NO_ACCT_AED_NON_RESIDENT() {
		return R0820_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0820_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0820_NO_ACCT_AED_NON_RESIDENT) {
		R0820_NO_ACCT_AED_NON_RESIDENT = r0820_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0820_AMOUNT_AED_NON_RESIDENT() {
		return R0820_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0820_AMOUNT_AED_NON_RESIDENT(BigDecimal r0820_AMOUNT_AED_NON_RESIDENT) {
		R0820_AMOUNT_AED_NON_RESIDENT = r0820_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0820_NO_ACCT_FCY_NON_RESIDENT() {
		return R0820_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0820_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0820_NO_ACCT_FCY_NON_RESIDENT) {
		R0820_NO_ACCT_FCY_NON_RESIDENT = r0820_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0820_AMOUNT_FCY_NON_RESIDENT() {
		return R0820_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0820_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0820_AMOUNT_FCY_NON_RESIDENT) {
		R0820_AMOUNT_FCY_NON_RESIDENT = r0820_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0820_TOTAL_AMOUNT() {
		return R0820_TOTAL_AMOUNT;
	}
	public void setR0820_TOTAL_AMOUNT(BigDecimal r0820_TOTAL_AMOUNT) {
		R0820_TOTAL_AMOUNT = r0820_TOTAL_AMOUNT;
	}
	public String getR0830_PRODUCT() {
		return R0830_PRODUCT;
	}
	public void setR0830_PRODUCT(String r0830_PRODUCT) {
		R0830_PRODUCT = r0830_PRODUCT;
	}
	public BigDecimal getR0830_NO_ACCT_AED_RESIDENT() {
		return R0830_NO_ACCT_AED_RESIDENT;
	}
	public void setR0830_NO_ACCT_AED_RESIDENT(BigDecimal r0830_NO_ACCT_AED_RESIDENT) {
		R0830_NO_ACCT_AED_RESIDENT = r0830_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0830_AMOUNT_AED_RESIDENT() {
		return R0830_AMOUNT_AED_RESIDENT;
	}
	public void setR0830_AMOUNT_AED_RESIDENT(BigDecimal r0830_AMOUNT_AED_RESIDENT) {
		R0830_AMOUNT_AED_RESIDENT = r0830_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0830_NO_ACCT_FCY_RESIDENT() {
		return R0830_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0830_NO_ACCT_FCY_RESIDENT(BigDecimal r0830_NO_ACCT_FCY_RESIDENT) {
		R0830_NO_ACCT_FCY_RESIDENT = r0830_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0830_AMOUNT_FCY_RESIDENT() {
		return R0830_AMOUNT_FCY_RESIDENT;
	}
	public void setR0830_AMOUNT_FCY_RESIDENT(BigDecimal r0830_AMOUNT_FCY_RESIDENT) {
		R0830_AMOUNT_FCY_RESIDENT = r0830_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0830_NO_ACCT_AED_NON_RESIDENT() {
		return R0830_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0830_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0830_NO_ACCT_AED_NON_RESIDENT) {
		R0830_NO_ACCT_AED_NON_RESIDENT = r0830_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0830_AMOUNT_AED_NON_RESIDENT() {
		return R0830_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0830_AMOUNT_AED_NON_RESIDENT(BigDecimal r0830_AMOUNT_AED_NON_RESIDENT) {
		R0830_AMOUNT_AED_NON_RESIDENT = r0830_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0830_NO_ACCT_FCY_NON_RESIDENT() {
		return R0830_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0830_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0830_NO_ACCT_FCY_NON_RESIDENT) {
		R0830_NO_ACCT_FCY_NON_RESIDENT = r0830_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0830_AMOUNT_FCY_NON_RESIDENT() {
		return R0830_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0830_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0830_AMOUNT_FCY_NON_RESIDENT) {
		R0830_AMOUNT_FCY_NON_RESIDENT = r0830_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0830_TOTAL_AMOUNT() {
		return R0830_TOTAL_AMOUNT;
	}
	public void setR0830_TOTAL_AMOUNT(BigDecimal r0830_TOTAL_AMOUNT) {
		R0830_TOTAL_AMOUNT = r0830_TOTAL_AMOUNT;
	}
	public String getR0840_PRODUCT() {
		return R0840_PRODUCT;
	}
	public void setR0840_PRODUCT(String r0840_PRODUCT) {
		R0840_PRODUCT = r0840_PRODUCT;
	}
	public BigDecimal getR0840_NO_ACCT_AED_RESIDENT() {
		return R0840_NO_ACCT_AED_RESIDENT;
	}
	public void setR0840_NO_ACCT_AED_RESIDENT(BigDecimal r0840_NO_ACCT_AED_RESIDENT) {
		R0840_NO_ACCT_AED_RESIDENT = r0840_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0840_AMOUNT_AED_RESIDENT() {
		return R0840_AMOUNT_AED_RESIDENT;
	}
	public void setR0840_AMOUNT_AED_RESIDENT(BigDecimal r0840_AMOUNT_AED_RESIDENT) {
		R0840_AMOUNT_AED_RESIDENT = r0840_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0840_NO_ACCT_FCY_RESIDENT() {
		return R0840_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0840_NO_ACCT_FCY_RESIDENT(BigDecimal r0840_NO_ACCT_FCY_RESIDENT) {
		R0840_NO_ACCT_FCY_RESIDENT = r0840_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0840_AMOUNT_FCY_RESIDENT() {
		return R0840_AMOUNT_FCY_RESIDENT;
	}
	public void setR0840_AMOUNT_FCY_RESIDENT(BigDecimal r0840_AMOUNT_FCY_RESIDENT) {
		R0840_AMOUNT_FCY_RESIDENT = r0840_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0840_NO_ACCT_AED_NON_RESIDENT() {
		return R0840_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0840_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0840_NO_ACCT_AED_NON_RESIDENT) {
		R0840_NO_ACCT_AED_NON_RESIDENT = r0840_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0840_AMOUNT_AED_NON_RESIDENT() {
		return R0840_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0840_AMOUNT_AED_NON_RESIDENT(BigDecimal r0840_AMOUNT_AED_NON_RESIDENT) {
		R0840_AMOUNT_AED_NON_RESIDENT = r0840_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0840_NO_ACCT_FCY_NON_RESIDENT() {
		return R0840_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0840_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0840_NO_ACCT_FCY_NON_RESIDENT) {
		R0840_NO_ACCT_FCY_NON_RESIDENT = r0840_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0840_AMOUNT_FCY_NON_RESIDENT() {
		return R0840_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0840_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0840_AMOUNT_FCY_NON_RESIDENT) {
		R0840_AMOUNT_FCY_NON_RESIDENT = r0840_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0840_TOTAL_AMOUNT() {
		return R0840_TOTAL_AMOUNT;
	}
	public void setR0840_TOTAL_AMOUNT(BigDecimal r0840_TOTAL_AMOUNT) {
		R0840_TOTAL_AMOUNT = r0840_TOTAL_AMOUNT;
	}
	public String getR0850_PRODUCT() {
		return R0850_PRODUCT;
	}
	public void setR0850_PRODUCT(String r0850_PRODUCT) {
		R0850_PRODUCT = r0850_PRODUCT;
	}
	public BigDecimal getR0850_NO_ACCT_AED_RESIDENT() {
		return R0850_NO_ACCT_AED_RESIDENT;
	}
	public void setR0850_NO_ACCT_AED_RESIDENT(BigDecimal r0850_NO_ACCT_AED_RESIDENT) {
		R0850_NO_ACCT_AED_RESIDENT = r0850_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0850_AMOUNT_AED_RESIDENT() {
		return R0850_AMOUNT_AED_RESIDENT;
	}
	public void setR0850_AMOUNT_AED_RESIDENT(BigDecimal r0850_AMOUNT_AED_RESIDENT) {
		R0850_AMOUNT_AED_RESIDENT = r0850_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0850_NO_ACCT_FCY_RESIDENT() {
		return R0850_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0850_NO_ACCT_FCY_RESIDENT(BigDecimal r0850_NO_ACCT_FCY_RESIDENT) {
		R0850_NO_ACCT_FCY_RESIDENT = r0850_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0850_AMOUNT_FCY_RESIDENT() {
		return R0850_AMOUNT_FCY_RESIDENT;
	}
	public void setR0850_AMOUNT_FCY_RESIDENT(BigDecimal r0850_AMOUNT_FCY_RESIDENT) {
		R0850_AMOUNT_FCY_RESIDENT = r0850_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0850_NO_ACCT_AED_NON_RESIDENT() {
		return R0850_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0850_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0850_NO_ACCT_AED_NON_RESIDENT) {
		R0850_NO_ACCT_AED_NON_RESIDENT = r0850_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0850_AMOUNT_AED_NON_RESIDENT() {
		return R0850_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0850_AMOUNT_AED_NON_RESIDENT(BigDecimal r0850_AMOUNT_AED_NON_RESIDENT) {
		R0850_AMOUNT_AED_NON_RESIDENT = r0850_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0850_NO_ACCT_FCY_NON_RESIDENT() {
		return R0850_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0850_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0850_NO_ACCT_FCY_NON_RESIDENT) {
		R0850_NO_ACCT_FCY_NON_RESIDENT = r0850_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0850_AMOUNT_FCY_NON_RESIDENT() {
		return R0850_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0850_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0850_AMOUNT_FCY_NON_RESIDENT) {
		R0850_AMOUNT_FCY_NON_RESIDENT = r0850_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0850_TOTAL_AMOUNT() {
		return R0850_TOTAL_AMOUNT;
	}
	public void setR0850_TOTAL_AMOUNT(BigDecimal r0850_TOTAL_AMOUNT) {
		R0850_TOTAL_AMOUNT = r0850_TOTAL_AMOUNT;
	}
	public String getR0860_PRODUCT() {
		return R0860_PRODUCT;
	}
	public void setR0860_PRODUCT(String r0860_PRODUCT) {
		R0860_PRODUCT = r0860_PRODUCT;
	}
	public BigDecimal getR0860_NO_ACCT_AED_RESIDENT() {
		return R0860_NO_ACCT_AED_RESIDENT;
	}
	public void setR0860_NO_ACCT_AED_RESIDENT(BigDecimal r0860_NO_ACCT_AED_RESIDENT) {
		R0860_NO_ACCT_AED_RESIDENT = r0860_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0860_AMOUNT_AED_RESIDENT() {
		return R0860_AMOUNT_AED_RESIDENT;
	}
	public void setR0860_AMOUNT_AED_RESIDENT(BigDecimal r0860_AMOUNT_AED_RESIDENT) {
		R0860_AMOUNT_AED_RESIDENT = r0860_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0860_NO_ACCT_FCY_RESIDENT() {
		return R0860_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0860_NO_ACCT_FCY_RESIDENT(BigDecimal r0860_NO_ACCT_FCY_RESIDENT) {
		R0860_NO_ACCT_FCY_RESIDENT = r0860_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0860_AMOUNT_FCY_RESIDENT() {
		return R0860_AMOUNT_FCY_RESIDENT;
	}
	public void setR0860_AMOUNT_FCY_RESIDENT(BigDecimal r0860_AMOUNT_FCY_RESIDENT) {
		R0860_AMOUNT_FCY_RESIDENT = r0860_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0860_NO_ACCT_AED_NON_RESIDENT() {
		return R0860_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0860_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0860_NO_ACCT_AED_NON_RESIDENT) {
		R0860_NO_ACCT_AED_NON_RESIDENT = r0860_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0860_AMOUNT_AED_NON_RESIDENT() {
		return R0860_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0860_AMOUNT_AED_NON_RESIDENT(BigDecimal r0860_AMOUNT_AED_NON_RESIDENT) {
		R0860_AMOUNT_AED_NON_RESIDENT = r0860_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0860_NO_ACCT_FCY_NON_RESIDENT() {
		return R0860_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0860_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0860_NO_ACCT_FCY_NON_RESIDENT) {
		R0860_NO_ACCT_FCY_NON_RESIDENT = r0860_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0860_AMOUNT_FCY_NON_RESIDENT() {
		return R0860_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0860_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0860_AMOUNT_FCY_NON_RESIDENT) {
		R0860_AMOUNT_FCY_NON_RESIDENT = r0860_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0860_TOTAL_AMOUNT() {
		return R0860_TOTAL_AMOUNT;
	}
	public void setR0860_TOTAL_AMOUNT(BigDecimal r0860_TOTAL_AMOUNT) {
		R0860_TOTAL_AMOUNT = r0860_TOTAL_AMOUNT;
	}
	public String getR0870_PRODUCT() {
		return R0870_PRODUCT;
	}
	public void setR0870_PRODUCT(String r0870_PRODUCT) {
		R0870_PRODUCT = r0870_PRODUCT;
	}
	public BigDecimal getR0870_NO_ACCT_AED_RESIDENT() {
		return R0870_NO_ACCT_AED_RESIDENT;
	}
	public void setR0870_NO_ACCT_AED_RESIDENT(BigDecimal r0870_NO_ACCT_AED_RESIDENT) {
		R0870_NO_ACCT_AED_RESIDENT = r0870_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0870_AMOUNT_AED_RESIDENT() {
		return R0870_AMOUNT_AED_RESIDENT;
	}
	public void setR0870_AMOUNT_AED_RESIDENT(BigDecimal r0870_AMOUNT_AED_RESIDENT) {
		R0870_AMOUNT_AED_RESIDENT = r0870_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0870_NO_ACCT_FCY_RESIDENT() {
		return R0870_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0870_NO_ACCT_FCY_RESIDENT(BigDecimal r0870_NO_ACCT_FCY_RESIDENT) {
		R0870_NO_ACCT_FCY_RESIDENT = r0870_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0870_AMOUNT_FCY_RESIDENT() {
		return R0870_AMOUNT_FCY_RESIDENT;
	}
	public void setR0870_AMOUNT_FCY_RESIDENT(BigDecimal r0870_AMOUNT_FCY_RESIDENT) {
		R0870_AMOUNT_FCY_RESIDENT = r0870_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0870_NO_ACCT_AED_NON_RESIDENT() {
		return R0870_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0870_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0870_NO_ACCT_AED_NON_RESIDENT) {
		R0870_NO_ACCT_AED_NON_RESIDENT = r0870_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0870_AMOUNT_AED_NON_RESIDENT() {
		return R0870_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0870_AMOUNT_AED_NON_RESIDENT(BigDecimal r0870_AMOUNT_AED_NON_RESIDENT) {
		R0870_AMOUNT_AED_NON_RESIDENT = r0870_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0870_NO_ACCT_FCY_NON_RESIDENT() {
		return R0870_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0870_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0870_NO_ACCT_FCY_NON_RESIDENT) {
		R0870_NO_ACCT_FCY_NON_RESIDENT = r0870_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0870_AMOUNT_FCY_NON_RESIDENT() {
		return R0870_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0870_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0870_AMOUNT_FCY_NON_RESIDENT) {
		R0870_AMOUNT_FCY_NON_RESIDENT = r0870_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0870_TOTAL_AMOUNT() {
		return R0870_TOTAL_AMOUNT;
	}
	public void setR0870_TOTAL_AMOUNT(BigDecimal r0870_TOTAL_AMOUNT) {
		R0870_TOTAL_AMOUNT = r0870_TOTAL_AMOUNT;
	}
	public String getR0880_PRODUCT() {
		return R0880_PRODUCT;
	}
	public void setR0880_PRODUCT(String r0880_PRODUCT) {
		R0880_PRODUCT = r0880_PRODUCT;
	}
	public BigDecimal getR0880_NO_ACCT_AED_RESIDENT() {
		return R0880_NO_ACCT_AED_RESIDENT;
	}
	public void setR0880_NO_ACCT_AED_RESIDENT(BigDecimal r0880_NO_ACCT_AED_RESIDENT) {
		R0880_NO_ACCT_AED_RESIDENT = r0880_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0880_AMOUNT_AED_RESIDENT() {
		return R0880_AMOUNT_AED_RESIDENT;
	}
	public void setR0880_AMOUNT_AED_RESIDENT(BigDecimal r0880_AMOUNT_AED_RESIDENT) {
		R0880_AMOUNT_AED_RESIDENT = r0880_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0880_NO_ACCT_FCY_RESIDENT() {
		return R0880_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0880_NO_ACCT_FCY_RESIDENT(BigDecimal r0880_NO_ACCT_FCY_RESIDENT) {
		R0880_NO_ACCT_FCY_RESIDENT = r0880_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0880_AMOUNT_FCY_RESIDENT() {
		return R0880_AMOUNT_FCY_RESIDENT;
	}
	public void setR0880_AMOUNT_FCY_RESIDENT(BigDecimal r0880_AMOUNT_FCY_RESIDENT) {
		R0880_AMOUNT_FCY_RESIDENT = r0880_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0880_NO_ACCT_AED_NON_RESIDENT() {
		return R0880_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0880_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0880_NO_ACCT_AED_NON_RESIDENT) {
		R0880_NO_ACCT_AED_NON_RESIDENT = r0880_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0880_AMOUNT_AED_NON_RESIDENT() {
		return R0880_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0880_AMOUNT_AED_NON_RESIDENT(BigDecimal r0880_AMOUNT_AED_NON_RESIDENT) {
		R0880_AMOUNT_AED_NON_RESIDENT = r0880_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0880_NO_ACCT_FCY_NON_RESIDENT() {
		return R0880_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0880_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0880_NO_ACCT_FCY_NON_RESIDENT) {
		R0880_NO_ACCT_FCY_NON_RESIDENT = r0880_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0880_AMOUNT_FCY_NON_RESIDENT() {
		return R0880_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0880_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0880_AMOUNT_FCY_NON_RESIDENT) {
		R0880_AMOUNT_FCY_NON_RESIDENT = r0880_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0880_TOTAL_AMOUNT() {
		return R0880_TOTAL_AMOUNT;
	}
	public void setR0880_TOTAL_AMOUNT(BigDecimal r0880_TOTAL_AMOUNT) {
		R0880_TOTAL_AMOUNT = r0880_TOTAL_AMOUNT;
	}
	public String getR0890_PRODUCT() {
		return R0890_PRODUCT;
	}
	public void setR0890_PRODUCT(String r0890_PRODUCT) {
		R0890_PRODUCT = r0890_PRODUCT;
	}
	public BigDecimal getR0890_NO_ACCT_AED_RESIDENT() {
		return R0890_NO_ACCT_AED_RESIDENT;
	}
	public void setR0890_NO_ACCT_AED_RESIDENT(BigDecimal r0890_NO_ACCT_AED_RESIDENT) {
		R0890_NO_ACCT_AED_RESIDENT = r0890_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0890_AMOUNT_AED_RESIDENT() {
		return R0890_AMOUNT_AED_RESIDENT;
	}
	public void setR0890_AMOUNT_AED_RESIDENT(BigDecimal r0890_AMOUNT_AED_RESIDENT) {
		R0890_AMOUNT_AED_RESIDENT = r0890_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0890_NO_ACCT_FCY_RESIDENT() {
		return R0890_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0890_NO_ACCT_FCY_RESIDENT(BigDecimal r0890_NO_ACCT_FCY_RESIDENT) {
		R0890_NO_ACCT_FCY_RESIDENT = r0890_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0890_AMOUNT_FCY_RESIDENT() {
		return R0890_AMOUNT_FCY_RESIDENT;
	}
	public void setR0890_AMOUNT_FCY_RESIDENT(BigDecimal r0890_AMOUNT_FCY_RESIDENT) {
		R0890_AMOUNT_FCY_RESIDENT = r0890_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0890_NO_ACCT_AED_NON_RESIDENT() {
		return R0890_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0890_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0890_NO_ACCT_AED_NON_RESIDENT) {
		R0890_NO_ACCT_AED_NON_RESIDENT = r0890_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0890_AMOUNT_AED_NON_RESIDENT() {
		return R0890_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0890_AMOUNT_AED_NON_RESIDENT(BigDecimal r0890_AMOUNT_AED_NON_RESIDENT) {
		R0890_AMOUNT_AED_NON_RESIDENT = r0890_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0890_NO_ACCT_FCY_NON_RESIDENT() {
		return R0890_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0890_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0890_NO_ACCT_FCY_NON_RESIDENT) {
		R0890_NO_ACCT_FCY_NON_RESIDENT = r0890_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0890_AMOUNT_FCY_NON_RESIDENT() {
		return R0890_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0890_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0890_AMOUNT_FCY_NON_RESIDENT) {
		R0890_AMOUNT_FCY_NON_RESIDENT = r0890_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0890_TOTAL_AMOUNT() {
		return R0890_TOTAL_AMOUNT;
	}
	public void setR0890_TOTAL_AMOUNT(BigDecimal r0890_TOTAL_AMOUNT) {
		R0890_TOTAL_AMOUNT = r0890_TOTAL_AMOUNT;
	}
	public String getR0900_PRODUCT() {
		return R0900_PRODUCT;
	}
	public void setR0900_PRODUCT(String r0900_PRODUCT) {
		R0900_PRODUCT = r0900_PRODUCT;
	}
	public BigDecimal getR0900_NO_ACCT_AED_RESIDENT() {
		return R0900_NO_ACCT_AED_RESIDENT;
	}
	public void setR0900_NO_ACCT_AED_RESIDENT(BigDecimal r0900_NO_ACCT_AED_RESIDENT) {
		R0900_NO_ACCT_AED_RESIDENT = r0900_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0900_AMOUNT_AED_RESIDENT() {
		return R0900_AMOUNT_AED_RESIDENT;
	}
	public void setR0900_AMOUNT_AED_RESIDENT(BigDecimal r0900_AMOUNT_AED_RESIDENT) {
		R0900_AMOUNT_AED_RESIDENT = r0900_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0900_NO_ACCT_FCY_RESIDENT() {
		return R0900_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0900_NO_ACCT_FCY_RESIDENT(BigDecimal r0900_NO_ACCT_FCY_RESIDENT) {
		R0900_NO_ACCT_FCY_RESIDENT = r0900_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0900_AMOUNT_FCY_RESIDENT() {
		return R0900_AMOUNT_FCY_RESIDENT;
	}
	public void setR0900_AMOUNT_FCY_RESIDENT(BigDecimal r0900_AMOUNT_FCY_RESIDENT) {
		R0900_AMOUNT_FCY_RESIDENT = r0900_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0900_NO_ACCT_AED_NON_RESIDENT() {
		return R0900_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0900_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0900_NO_ACCT_AED_NON_RESIDENT) {
		R0900_NO_ACCT_AED_NON_RESIDENT = r0900_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0900_AMOUNT_AED_NON_RESIDENT() {
		return R0900_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0900_AMOUNT_AED_NON_RESIDENT(BigDecimal r0900_AMOUNT_AED_NON_RESIDENT) {
		R0900_AMOUNT_AED_NON_RESIDENT = r0900_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0900_NO_ACCT_FCY_NON_RESIDENT() {
		return R0900_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0900_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0900_NO_ACCT_FCY_NON_RESIDENT) {
		R0900_NO_ACCT_FCY_NON_RESIDENT = r0900_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0900_AMOUNT_FCY_NON_RESIDENT() {
		return R0900_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0900_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0900_AMOUNT_FCY_NON_RESIDENT) {
		R0900_AMOUNT_FCY_NON_RESIDENT = r0900_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0900_TOTAL_AMOUNT() {
		return R0900_TOTAL_AMOUNT;
	}
	public void setR0900_TOTAL_AMOUNT(BigDecimal r0900_TOTAL_AMOUNT) {
		R0900_TOTAL_AMOUNT = r0900_TOTAL_AMOUNT;
	}
	public String getR0910_PRODUCT() {
		return R0910_PRODUCT;
	}
	public void setR0910_PRODUCT(String r0910_PRODUCT) {
		R0910_PRODUCT = r0910_PRODUCT;
	}
	public BigDecimal getR0910_NO_ACCT_AED_RESIDENT() {
		return R0910_NO_ACCT_AED_RESIDENT;
	}
	public void setR0910_NO_ACCT_AED_RESIDENT(BigDecimal r0910_NO_ACCT_AED_RESIDENT) {
		R0910_NO_ACCT_AED_RESIDENT = r0910_NO_ACCT_AED_RESIDENT;
	}
	public BigDecimal getR0910_AMOUNT_AED_RESIDENT() {
		return R0910_AMOUNT_AED_RESIDENT;
	}
	public void setR0910_AMOUNT_AED_RESIDENT(BigDecimal r0910_AMOUNT_AED_RESIDENT) {
		R0910_AMOUNT_AED_RESIDENT = r0910_AMOUNT_AED_RESIDENT;
	}
	public BigDecimal getR0910_NO_ACCT_FCY_RESIDENT() {
		return R0910_NO_ACCT_FCY_RESIDENT;
	}
	public void setR0910_NO_ACCT_FCY_RESIDENT(BigDecimal r0910_NO_ACCT_FCY_RESIDENT) {
		R0910_NO_ACCT_FCY_RESIDENT = r0910_NO_ACCT_FCY_RESIDENT;
	}
	public BigDecimal getR0910_AMOUNT_FCY_RESIDENT() {
		return R0910_AMOUNT_FCY_RESIDENT;
	}
	public void setR0910_AMOUNT_FCY_RESIDENT(BigDecimal r0910_AMOUNT_FCY_RESIDENT) {
		R0910_AMOUNT_FCY_RESIDENT = r0910_AMOUNT_FCY_RESIDENT;
	}
	public BigDecimal getR0910_NO_ACCT_AED_NON_RESIDENT() {
		return R0910_NO_ACCT_AED_NON_RESIDENT;
	}
	public void setR0910_NO_ACCT_AED_NON_RESIDENT(BigDecimal r0910_NO_ACCT_AED_NON_RESIDENT) {
		R0910_NO_ACCT_AED_NON_RESIDENT = r0910_NO_ACCT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0910_AMOUNT_AED_NON_RESIDENT() {
		return R0910_AMOUNT_AED_NON_RESIDENT;
	}
	public void setR0910_AMOUNT_AED_NON_RESIDENT(BigDecimal r0910_AMOUNT_AED_NON_RESIDENT) {
		R0910_AMOUNT_AED_NON_RESIDENT = r0910_AMOUNT_AED_NON_RESIDENT;
	}
	public BigDecimal getR0910_NO_ACCT_FCY_NON_RESIDENT() {
		return R0910_NO_ACCT_FCY_NON_RESIDENT;
	}
	public void setR0910_NO_ACCT_FCY_NON_RESIDENT(BigDecimal r0910_NO_ACCT_FCY_NON_RESIDENT) {
		R0910_NO_ACCT_FCY_NON_RESIDENT = r0910_NO_ACCT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0910_AMOUNT_FCY_NON_RESIDENT() {
		return R0910_AMOUNT_FCY_NON_RESIDENT;
	}
	public void setR0910_AMOUNT_FCY_NON_RESIDENT(BigDecimal r0910_AMOUNT_FCY_NON_RESIDENT) {
		R0910_AMOUNT_FCY_NON_RESIDENT = r0910_AMOUNT_FCY_NON_RESIDENT;
	}
	public BigDecimal getR0910_TOTAL_AMOUNT() {
		return R0910_TOTAL_AMOUNT;
	}
	public void setR0910_TOTAL_AMOUNT(BigDecimal r0910_TOTAL_AMOUNT) {
		R0910_TOTAL_AMOUNT = r0910_TOTAL_AMOUNT;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public String getREPORT_VERSION() {
		return REPORT_VERSION;
	}
	public void setREPORT_VERSION(String rEPORT_VERSION) {
		REPORT_VERSION = rEPORT_VERSION;
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
	public CBUAE_BRF1_1_Summary_Archival_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
