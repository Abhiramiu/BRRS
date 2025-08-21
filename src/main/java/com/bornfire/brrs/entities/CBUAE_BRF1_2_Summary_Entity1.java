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
@Table(name = "CBUAE_BRF1_2_SUMMARYTABLE1")

public class CBUAE_BRF1_2_Summary_Entity1 {

	private String	r0010_product;
	private BigDecimal	r0010_no_acct_aed_resident;
	private BigDecimal	r0010_amount_aed_resident;
	private BigDecimal	r0010_no_acct_fcy_resident;
	private BigDecimal	r0010_amount_fcy_resident;
	private BigDecimal	r0010_no_acct_aed_non_resident;
	private BigDecimal	r0010_amount_aed_non_resident;
	private BigDecimal	r0010_no_acct_fcy_non_resident;
	private BigDecimal	r0010_amount_fcy_non_resident;
	private BigDecimal	r0010_total_amount;
	private String	r0020_product;
	private BigDecimal	r0020_no_acct_aed_resident;
	private BigDecimal	r0020_amount_aed_resident;
	private BigDecimal	r0020_no_acct_fcy_resident;
	private BigDecimal	r0020_amount_fcy_resident;
	private BigDecimal	r0020_no_acct_aed_non_resident;
	private BigDecimal	r0020_amount_aed_non_resident;
	private BigDecimal	r0020_no_acct_fcy_non_resident;
	private BigDecimal	r0020_amount_fcy_non_resident;
	private BigDecimal	r0020_total_amount;
	private String	r0030_product;
	private BigDecimal	r0030_no_acct_aed_resident;
	private BigDecimal	r0030_amount_aed_resident;
	private BigDecimal	r0030_no_acct_fcy_resident;
	private BigDecimal	r0030_amount_fcy_resident;
	private BigDecimal	r0030_no_acct_aed_non_resident;
	private BigDecimal	r0030_amount_aed_non_resident;
	private BigDecimal	r0030_no_acct_fcy_non_resident;
	private BigDecimal	r0030_amount_fcy_non_resident;
	private BigDecimal	r0030_total_amount;
	private String	r0040_product;
	private BigDecimal	r0040_no_acct_aed_resident;
	private BigDecimal	r0040_amount_aed_resident;
	private BigDecimal	r0040_no_acct_fcy_resident;
	private BigDecimal	r0040_amount_fcy_resident;
	private BigDecimal	r0040_no_acct_aed_non_resident;
	private BigDecimal	r0040_amount_aed_non_resident;
	private BigDecimal	r0040_no_acct_fcy_non_resident;
	private BigDecimal	r0040_amount_fcy_non_resident;
	private BigDecimal	r0040_total_amount;
	private String	r0050_product;
	private BigDecimal	r0050_no_acct_aed_resident;
	private BigDecimal	r0050_amount_aed_resident;
	private BigDecimal	r0050_no_acct_fcy_resident;
	private BigDecimal	r0050_amount_fcy_resident;
	private BigDecimal	r0050_no_acct_aed_non_resident;
	private BigDecimal	r0050_amount_aed_non_resident;
	private BigDecimal	r0050_no_acct_fcy_non_resident;
	private BigDecimal	r0050_amount_fcy_non_resident;
	private BigDecimal	r0050_total_amount;
	private String	r0060_product;
	private BigDecimal	r0060_no_acct_aed_resident;
	private BigDecimal	r0060_amount_aed_resident;
	private BigDecimal	r0060_no_acct_fcy_resident;
	private BigDecimal	r0060_amount_fcy_resident;
	private BigDecimal	r0060_no_acct_aed_non_resident;
	private BigDecimal	r0060_amount_aed_non_resident;
	private BigDecimal	r0060_no_acct_fcy_non_resident;
	private BigDecimal	r0060_amount_fcy_non_resident;
	private BigDecimal	r0060_total_amount;
	private String	r0070_product;
	private BigDecimal	r0070_no_acct_aed_resident;
	private BigDecimal	r0070_amount_aed_resident;
	private BigDecimal	r0070_no_acct_fcy_resident;
	private BigDecimal	r0070_amount_fcy_resident;
	private BigDecimal	r0070_no_acct_aed_non_resident;
	private BigDecimal	r0070_amount_aed_non_resident;
	private BigDecimal	r0070_no_acct_fcy_non_resident;
	private BigDecimal	r0070_amount_fcy_non_resident;
	private BigDecimal	r0070_total_amount;
	private String	r0080_product;
	private BigDecimal	r0080_no_acct_aed_resident;
	private BigDecimal	r0080_amount_aed_resident;
	private BigDecimal	r0080_no_acct_fcy_resident;
	private BigDecimal	r0080_amount_fcy_resident;
	private BigDecimal	r0080_no_acct_aed_non_resident;
	private BigDecimal	r0080_amount_aed_non_resident;
	private BigDecimal	r0080_no_acct_fcy_non_resident;
	private BigDecimal	r0080_amount_fcy_non_resident;
	private BigDecimal	r0080_total_amount;
	private String	r0090_product;
	private BigDecimal	r0090_no_acct_aed_resident;
	private BigDecimal	r0090_amount_aed_resident;
	private BigDecimal	r0090_no_acct_fcy_resident;
	private BigDecimal	r0090_amount_fcy_resident;
	private BigDecimal	r0090_no_acct_aed_non_resident;
	private BigDecimal	r0090_amount_aed_non_resident;
	private BigDecimal	r0090_no_acct_fcy_non_resident;
	private BigDecimal	r0090_amount_fcy_non_resident;
	private BigDecimal	r0090_total_amount;
	private String	r0100_product;
	private BigDecimal	r0100_no_acct_aed_resident;
	private BigDecimal	r0100_amount_aed_resident;
	private BigDecimal	r0100_no_acct_fcy_resident;
	private BigDecimal	r0100_amount_fcy_resident;
	private BigDecimal	r0100_no_acct_aed_non_resident;
	private BigDecimal	r0100_amount_aed_non_resident;
	private BigDecimal	r0100_no_acct_fcy_non_resident;
	private BigDecimal	r0100_amount_fcy_non_resident;
	private BigDecimal	r0100_total_amount;
	private String	r0110_product;
	private BigDecimal	r0110_no_acct_aed_resident;
	private BigDecimal	r0110_amount_aed_resident;
	private BigDecimal	r0110_no_acct_fcy_resident;
	private BigDecimal	r0110_amount_fcy_resident;
	private BigDecimal	r0110_no_acct_aed_non_resident;
	private BigDecimal	r0110_amount_aed_non_resident;
	private BigDecimal	r0110_no_acct_fcy_non_resident;
	private BigDecimal	r0110_amount_fcy_non_resident;
	private BigDecimal	r0110_total_amount;
	private String	r0120_product;
	private BigDecimal	r0120_no_acct_aed_resident;
	private BigDecimal	r0120_amount_aed_resident;
	private BigDecimal	r0120_no_acct_fcy_resident;
	private BigDecimal	r0120_amount_fcy_resident;
	private BigDecimal	r0120_no_acct_aed_non_resident;
	private BigDecimal	r0120_amount_aed_non_resident;
	private BigDecimal	r0120_no_acct_fcy_non_resident;
	private BigDecimal	r0120_amount_fcy_non_resident;
	private BigDecimal	r0120_total_amount;
	private String	r0130_product;
	private BigDecimal	r0130_no_acct_aed_resident;
	private BigDecimal	r0130_amount_aed_resident;
	private BigDecimal	r0130_no_acct_fcy_resident;
	private BigDecimal	r0130_amount_fcy_resident;
	private BigDecimal	r0130_no_acct_aed_non_resident;
	private BigDecimal	r0130_amount_aed_non_resident;
	private BigDecimal	r0130_no_acct_fcy_non_resident;
	private BigDecimal	r0130_amount_fcy_non_resident;
	private BigDecimal	r0130_total_amount;
	private String	r0140_product;
	private BigDecimal	r0140_no_acct_aed_resident;
	private BigDecimal	r0140_amount_aed_resident;
	private BigDecimal	r0140_no_acct_fcy_resident;
	private BigDecimal	r0140_amount_fcy_resident;
	private BigDecimal	r0140_no_acct_aed_non_resident;
	private BigDecimal	r0140_amount_aed_non_resident;
	private BigDecimal	r0140_no_acct_fcy_non_resident;
	private BigDecimal	r0140_amount_fcy_non_resident;
	private BigDecimal	r0140_total_amount;
	private String	r0150_product;
	private BigDecimal	r0150_no_acct_aed_resident;
	private BigDecimal	r0150_amount_aed_resident;
	private BigDecimal	r0150_no_acct_fcy_resident;
	private BigDecimal	r0150_amount_fcy_resident;
	private BigDecimal	r0150_no_acct_aed_non_resident;
	private BigDecimal	r0150_amount_aed_non_resident;
	private BigDecimal	r0150_no_acct_fcy_non_resident;
	private BigDecimal	r0150_amount_fcy_non_resident;
	private BigDecimal	r0150_total_amount;
	private String	r0160_product;
	private BigDecimal	r0160_no_acct_aed_resident;
	private BigDecimal	r0160_amount_aed_resident;
	private BigDecimal	r0160_no_acct_fcy_resident;
	private BigDecimal	r0160_amount_fcy_resident;
	private BigDecimal	r0160_no_acct_aed_non_resident;
	private BigDecimal	r0160_amount_aed_non_resident;
	private BigDecimal	r0160_no_acct_fcy_non_resident;
	private BigDecimal	r0160_amount_fcy_non_resident;
	private BigDecimal	r0160_total_amount;
	private String	r0170_product;
	private BigDecimal	r0170_no_acct_aed_resident;
	private BigDecimal	r0170_amount_aed_resident;
	private BigDecimal	r0170_no_acct_fcy_resident;
	private BigDecimal	r0170_amount_fcy_resident;
	private BigDecimal	r0170_no_acct_aed_non_resident;
	private BigDecimal	r0170_amount_aed_non_resident;
	private BigDecimal	r0170_no_acct_fcy_non_resident;
	private BigDecimal	r0170_amount_fcy_non_resident;
	private BigDecimal	r0170_total_amount;
	private String	r0180_product;
	private BigDecimal	r0180_no_acct_aed_resident;
	private BigDecimal	r0180_amount_aed_resident;
	private BigDecimal	r0180_no_acct_fcy_resident;
	private BigDecimal	r0180_amount_fcy_resident;
	private BigDecimal	r0180_no_acct_aed_non_resident;
	private BigDecimal	r0180_amount_aed_non_resident;
	private BigDecimal	r0180_no_acct_fcy_non_resident;
	private BigDecimal	r0180_amount_fcy_non_resident;
	private BigDecimal	r0180_total_amount;
	private String	r0190_product;
	private BigDecimal	r0190_no_acct_aed_resident;
	private BigDecimal	r0190_amount_aed_resident;
	private BigDecimal	r0190_no_acct_fcy_resident;
	private BigDecimal	r0190_amount_fcy_resident;
	private BigDecimal	r0190_no_acct_aed_non_resident;
	private BigDecimal	r0190_amount_aed_non_resident;
	private BigDecimal	r0190_no_acct_fcy_non_resident;
	private BigDecimal	r0190_amount_fcy_non_resident;
	private BigDecimal	r0190_total_amount;
	private String	r0200_product;
	private BigDecimal	r0200_no_acct_aed_resident;
	private BigDecimal	r0200_amount_aed_resident;
	private BigDecimal	r0200_no_acct_fcy_resident;
	private BigDecimal	r0200_amount_fcy_resident;
	private BigDecimal	r0200_no_acct_aed_non_resident;
	private BigDecimal	r0200_amount_aed_non_resident;
	private BigDecimal	r0200_no_acct_fcy_non_resident;
	private BigDecimal	r0200_amount_fcy_non_resident;
	private BigDecimal	r0200_total_amount;
	private String	r0210_product;
	private BigDecimal	r0210_no_acct_aed_resident;
	private BigDecimal	r0210_amount_aed_resident;
	private BigDecimal	r0210_no_acct_fcy_resident;
	private BigDecimal	r0210_amount_fcy_resident;
	private BigDecimal	r0210_no_acct_aed_non_resident;
	private BigDecimal	r0210_amount_aed_non_resident;
	private BigDecimal	r0210_no_acct_fcy_non_resident;
	private BigDecimal	r0210_amount_fcy_non_resident;
	private BigDecimal	r0210_total_amount;
	private String	r0220_product;
	private BigDecimal	r0220_no_acct_aed_resident;
	private BigDecimal	r0220_amount_aed_resident;
	private BigDecimal	r0220_no_acct_fcy_resident;
	private BigDecimal	r0220_amount_fcy_resident;
	private BigDecimal	r0220_no_acct_aed_non_resident;
	private BigDecimal	r0220_amount_aed_non_resident;
	private BigDecimal	r0220_no_acct_fcy_non_resident;
	private BigDecimal	r0220_amount_fcy_non_resident;
	private BigDecimal	r0220_total_amount;
	private String	r0230_product;
	private BigDecimal	r0230_no_acct_aed_resident;
	private BigDecimal	r0230_amount_aed_resident;
	private BigDecimal	r0230_no_acct_fcy_resident;
	private BigDecimal	r0230_amount_fcy_resident;
	private BigDecimal	r0230_no_acct_aed_non_resident;
	private BigDecimal	r0230_amount_aed_non_resident;
	private BigDecimal	r0230_no_acct_fcy_non_resident;
	private BigDecimal	r0230_amount_fcy_non_resident;
	private BigDecimal	r0230_total_amount;
	private String	r0240_product;
	private BigDecimal	r0240_no_acct_aed_resident;
	private BigDecimal	r0240_amount_aed_resident;
	private BigDecimal	r0240_no_acct_fcy_resident;
	private BigDecimal	r0240_amount_fcy_resident;
	private BigDecimal	r0240_no_acct_aed_non_resident;
	private BigDecimal	r0240_amount_aed_non_resident;
	private BigDecimal	r0240_no_acct_fcy_non_resident;
	private BigDecimal	r0240_amount_fcy_non_resident;
	private BigDecimal	r0240_total_amount;
	private String	r0250_product;
	private BigDecimal	r0250_no_acct_aed_resident;
	private BigDecimal	r0250_amount_aed_resident;
	private BigDecimal	r0250_no_acct_fcy_resident;
	private BigDecimal	r0250_amount_fcy_resident;
	private BigDecimal	r0250_no_acct_aed_non_resident;
	private BigDecimal	r0250_amount_aed_non_resident;
	private BigDecimal	r0250_no_acct_fcy_non_resident;
	private BigDecimal	r0250_amount_fcy_non_resident;
	private BigDecimal	r0250_total_amount;
	private String	r0260_product;
	private BigDecimal	r0260_no_acct_aed_resident;
	private BigDecimal	r0260_amount_aed_resident;
	private BigDecimal	r0260_no_acct_fcy_resident;
	private BigDecimal	r0260_amount_fcy_resident;
	private BigDecimal	r0260_no_acct_aed_non_resident;
	private BigDecimal	r0260_amount_aed_non_resident;
	private BigDecimal	r0260_no_acct_fcy_non_resident;
	private BigDecimal	r0260_amount_fcy_non_resident;
	private BigDecimal	r0260_total_amount;
	private String	r0270_product;
	private BigDecimal	r0270_no_acct_aed_resident;
	private BigDecimal	r0270_amount_aed_resident;
	private BigDecimal	r0270_no_acct_fcy_resident;
	private BigDecimal	r0270_amount_fcy_resident;
	private BigDecimal	r0270_no_acct_aed_non_resident;
	private BigDecimal	r0270_amount_aed_non_resident;
	private BigDecimal	r0270_no_acct_fcy_non_resident;
	private BigDecimal	r0270_amount_fcy_non_resident;
	private BigDecimal	r0270_total_amount;
	private String	r0280_product;
	private BigDecimal	r0280_no_acct_aed_resident;
	private BigDecimal	r0280_amount_aed_resident;
	private BigDecimal	r0280_no_acct_fcy_resident;
	private BigDecimal	r0280_amount_fcy_resident;
	private BigDecimal	r0280_no_acct_aed_non_resident;
	private BigDecimal	r0280_amount_aed_non_resident;
	private BigDecimal	r0280_no_acct_fcy_non_resident;
	private BigDecimal	r0280_amount_fcy_non_resident;
	private BigDecimal	r0280_total_amount;
	private String	r0290_product;
	private BigDecimal	r0290_no_acct_aed_resident;
	private BigDecimal	r0290_amount_aed_resident;
	private BigDecimal	r0290_no_acct_fcy_resident;
	private BigDecimal	r0290_amount_fcy_resident;
	private BigDecimal	r0290_no_acct_aed_non_resident;
	private BigDecimal	r0290_amount_aed_non_resident;
	private BigDecimal	r0290_no_acct_fcy_non_resident;
	private BigDecimal	r0290_amount_fcy_non_resident;
	private BigDecimal	r0290_total_amount;
	private String	r0300_product;
	private BigDecimal	r0300_no_acct_aed_resident;
	private BigDecimal	r0300_amount_aed_resident;
	private BigDecimal	r0300_no_acct_fcy_resident;
	private BigDecimal	r0300_amount_fcy_resident;
	private BigDecimal	r0300_no_acct_aed_non_resident;
	private BigDecimal	r0300_amount_aed_non_resident;
	private BigDecimal	r0300_no_acct_fcy_non_resident;
	private BigDecimal	r0300_amount_fcy_non_resident;
	private BigDecimal	r0300_total_amount;
	private String	r0310_product;
	private BigDecimal	r0310_no_acct_aed_resident;
	private BigDecimal	r0310_amount_aed_resident;
	private BigDecimal	r0310_no_acct_fcy_resident;
	private BigDecimal	r0310_amount_fcy_resident;
	private BigDecimal	r0310_no_acct_aed_non_resident;
	private BigDecimal	r0310_amount_aed_non_resident;
	private BigDecimal	r0310_no_acct_fcy_non_resident;
	private BigDecimal	r0310_amount_fcy_non_resident;
	private BigDecimal	r0310_total_amount;
	private String	r0320_product;
	private BigDecimal	r0320_no_acct_aed_resident;
	private BigDecimal	r0320_amount_aed_resident;
	private BigDecimal	r0320_no_acct_fcy_resident;
	private BigDecimal	r0320_amount_fcy_resident;
	private BigDecimal	r0320_no_acct_aed_non_resident;
	private BigDecimal	r0320_amount_aed_non_resident;
	private BigDecimal	r0320_no_acct_fcy_non_resident;
	private BigDecimal	r0320_amount_fcy_non_resident;
	private BigDecimal	r0320_total_amount;
	private String	r0330_product;
	private BigDecimal	r0330_no_acct_aed_resident;
	private BigDecimal	r0330_amount_aed_resident;
	private BigDecimal	r0330_no_acct_fcy_resident;
	private BigDecimal	r0330_amount_fcy_resident;
	private BigDecimal	r0330_no_acct_aed_non_resident;
	private BigDecimal	r0330_amount_aed_non_resident;
	private BigDecimal	r0330_no_acct_fcy_non_resident;
	private BigDecimal	r0330_amount_fcy_non_resident;
	private BigDecimal	r0330_total_amount;
	private String	r0340_product;
	private BigDecimal	r0340_no_acct_aed_resident;
	private BigDecimal	r0340_amount_aed_resident;
	private BigDecimal	r0340_no_acct_fcy_resident;
	private BigDecimal	r0340_amount_fcy_resident;
	private BigDecimal	r0340_no_acct_aed_non_resident;
	private BigDecimal	r0340_amount_aed_non_resident;
	private BigDecimal	r0340_no_acct_fcy_non_resident;
	private BigDecimal	r0340_amount_fcy_non_resident;
	private BigDecimal	r0340_total_amount;
	private String	r0350_product;
	private BigDecimal	r0350_no_acct_aed_resident;
	private BigDecimal	r0350_amount_aed_resident;
	private BigDecimal	r0350_no_acct_fcy_resident;
	private BigDecimal	r0350_amount_fcy_resident;
	private BigDecimal	r0350_no_acct_aed_non_resident;
	private BigDecimal	r0350_amount_aed_non_resident;
	private BigDecimal	r0350_no_acct_fcy_non_resident;
	private BigDecimal	r0350_amount_fcy_non_resident;
	private BigDecimal	r0350_total_amount;
	private String	r0360_product;
	private BigDecimal	r0360_no_acct_aed_resident;
	private BigDecimal	r0360_amount_aed_resident;
	private BigDecimal	r0360_no_acct_fcy_resident;
	private BigDecimal	r0360_amount_fcy_resident;
	private BigDecimal	r0360_no_acct_aed_non_resident;
	private BigDecimal	r0360_amount_aed_non_resident;
	private BigDecimal	r0360_no_acct_fcy_non_resident;
	private BigDecimal	r0360_amount_fcy_non_resident;
	private BigDecimal	r0360_total_amount;
	private String	r0370_product;
	private BigDecimal	r0370_no_acct_aed_resident;
	private BigDecimal	r0370_amount_aed_resident;
	private BigDecimal	r0370_no_acct_fcy_resident;
	private BigDecimal	r0370_amount_fcy_resident;
	private BigDecimal	r0370_no_acct_aed_non_resident;
	private BigDecimal	r0370_amount_aed_non_resident;
	private BigDecimal	r0370_no_acct_fcy_non_resident;
	private BigDecimal	r0370_amount_fcy_non_resident;
	private BigDecimal	r0370_total_amount;
	private String	r0380_product;
	private BigDecimal	r0380_no_acct_aed_resident;
	private BigDecimal	r0380_amount_aed_resident;
	private BigDecimal	r0380_no_acct_fcy_resident;
	private BigDecimal	r0380_amount_fcy_resident;
	private BigDecimal	r0380_no_acct_aed_non_resident;
	private BigDecimal	r0380_amount_aed_non_resident;
	private BigDecimal	r0380_no_acct_fcy_non_resident;
	private BigDecimal	r0380_amount_fcy_non_resident;
	private BigDecimal	r0380_total_amount;
	private String	r0390_product;
	private BigDecimal	r0390_no_acct_aed_resident;
	private BigDecimal	r0390_amount_aed_resident;
	private BigDecimal	r0390_no_acct_fcy_resident;
	private BigDecimal	r0390_amount_fcy_resident;
	private BigDecimal	r0390_no_acct_aed_non_resident;
	private BigDecimal	r0390_amount_aed_non_resident;
	private BigDecimal	r0390_no_acct_fcy_non_resident;
	private BigDecimal	r0390_amount_fcy_non_resident;
	private BigDecimal	r0390_total_amount;
	private String	r0400_product;
	private BigDecimal	r0400_no_acct_aed_resident;
	private BigDecimal	r0400_amount_aed_resident;
	private BigDecimal	r0400_no_acct_fcy_resident;
	private BigDecimal	r0400_amount_fcy_resident;
	private BigDecimal	r0400_no_acct_aed_non_resident;
	private BigDecimal	r0400_amount_aed_non_resident;
	private BigDecimal	r0400_no_acct_fcy_non_resident;
	private BigDecimal	r0400_amount_fcy_non_resident;
	private BigDecimal	r0400_total_amount;
	private String	r0410_product;
	private BigDecimal	r0410_no_acct_aed_resident;
	private BigDecimal	r0410_amount_aed_resident;
	private BigDecimal	r0410_no_acct_fcy_resident;
	private BigDecimal	r0410_amount_fcy_resident;
	private BigDecimal	r0410_no_acct_aed_non_resident;
	private BigDecimal	r0410_amount_aed_non_resident;
	private BigDecimal	r0410_no_acct_fcy_non_resident;
	private BigDecimal	r0410_amount_fcy_non_resident;
	private BigDecimal	r0410_total_amount;
	private String	r0420_product;
	private BigDecimal	r0420_no_acct_aed_resident;
	private BigDecimal	r0420_amount_aed_resident;
	private BigDecimal	r0420_no_acct_fcy_resident;
	private BigDecimal	r0420_amount_fcy_resident;
	private BigDecimal	r0420_no_acct_aed_non_resident;
	private BigDecimal	r0420_amount_aed_non_resident;
	private BigDecimal	r0420_no_acct_fcy_non_resident;
	private BigDecimal	r0420_amount_fcy_non_resident;
	private BigDecimal	r0420_total_amount;
	private String	r0430_product;
	private BigDecimal	r0430_no_acct_aed_resident;
	private BigDecimal	r0430_amount_aed_resident;
	private BigDecimal	r0430_no_acct_fcy_resident;
	private BigDecimal	r0430_amount_fcy_resident;
	private BigDecimal	r0430_no_acct_aed_non_resident;
	private BigDecimal	r0430_amount_aed_non_resident;
	private BigDecimal	r0430_no_acct_fcy_non_resident;
	private BigDecimal	r0430_amount_fcy_non_resident;
	private BigDecimal	r0430_total_amount;
	private String	r0440_product;
	private BigDecimal	r0440_no_acct_aed_resident;
	private BigDecimal	r0440_amount_aed_resident;
	private BigDecimal	r0440_no_acct_fcy_resident;
	private BigDecimal	r0440_amount_fcy_resident;
	private BigDecimal	r0440_no_acct_aed_non_resident;
	private BigDecimal	r0440_amount_aed_non_resident;
	private BigDecimal	r0440_no_acct_fcy_non_resident;
	private BigDecimal	r0440_amount_fcy_non_resident;
	private BigDecimal	r0440_total_amount;
	private String	r0450_product;
	private BigDecimal	r0450_no_acct_aed_resident;
	private BigDecimal	r0450_amount_aed_resident;
	private BigDecimal	r0450_no_acct_fcy_resident;
	private BigDecimal	r0450_amount_fcy_resident;
	private BigDecimal	r0450_no_acct_aed_non_resident;
	private BigDecimal	r0450_amount_aed_non_resident;
	private BigDecimal	r0450_no_acct_fcy_non_resident;
	private BigDecimal	r0450_amount_fcy_non_resident;
	private BigDecimal	r0450_total_amount;
	private String	r0460_product;
	private BigDecimal	r0460_no_acct_aed_resident;
	private BigDecimal	r0460_amount_aed_resident;
	private BigDecimal	r0460_no_acct_fcy_resident;
	private BigDecimal	r0460_amount_fcy_resident;
	private BigDecimal	r0460_no_acct_aed_non_resident;
	private BigDecimal	r0460_amount_aed_non_resident;
	private BigDecimal	r0460_no_acct_fcy_non_resident;
	private BigDecimal	r0460_amount_fcy_non_resident;
	private BigDecimal	r0460_total_amount;
	private String	r0470_product;
	private BigDecimal	r0470_no_acct_aed_resident;
	private BigDecimal	r0470_amount_aed_resident;
	private BigDecimal	r0470_no_acct_fcy_resident;
	private BigDecimal	r0470_amount_fcy_resident;
	private BigDecimal	r0470_no_acct_aed_non_resident;
	private BigDecimal	r0470_amount_aed_non_resident;
	private BigDecimal	r0470_no_acct_fcy_non_resident;
	private BigDecimal	r0470_amount_fcy_non_resident;
	private BigDecimal	r0470_total_amount;
	private String	r0480_product;
	private BigDecimal	r0480_no_acct_aed_resident;
	private BigDecimal	r0480_amount_aed_resident;
	private BigDecimal	r0480_no_acct_fcy_resident;
	private BigDecimal	r0480_amount_fcy_resident;
	private BigDecimal	r0480_no_acct_aed_non_resident;
	private BigDecimal	r0480_amount_aed_non_resident;
	private BigDecimal	r0480_no_acct_fcy_non_resident;
	private BigDecimal	r0480_amount_fcy_non_resident;
	private BigDecimal	r0480_total_amount;
	private String	r0490_product;
	private BigDecimal	r0490_no_acct_aed_resident;
	private BigDecimal	r0490_amount_aed_resident;
	private BigDecimal	r0490_no_acct_fcy_resident;
	private BigDecimal	r0490_amount_fcy_resident;
	private BigDecimal	r0490_no_acct_aed_non_resident;
	private BigDecimal	r0490_amount_aed_non_resident;
	private BigDecimal	r0490_no_acct_fcy_non_resident;
	private BigDecimal	r0490_amount_fcy_non_resident;
	private BigDecimal	r0490_total_amount;
	private String	r0500_product;
	private BigDecimal	r0500_no_acct_aed_resident;
	private BigDecimal	r0500_amount_aed_resident;
	private BigDecimal	r0500_no_acct_fcy_resident;
	private BigDecimal	r0500_amount_fcy_resident;
	private BigDecimal	r0500_no_acct_aed_non_resident;
	private BigDecimal	r0500_amount_aed_non_resident;
	private BigDecimal	r0500_no_acct_fcy_non_resident;
	private BigDecimal	r0500_amount_fcy_non_resident;
	private BigDecimal	r0500_total_amount;
	private String	r0510_product;
	private BigDecimal	r0510_no_acct_aed_resident;
	private BigDecimal	r0510_amount_aed_resident;
	private BigDecimal	r0510_no_acct_fcy_resident;
	private BigDecimal	r0510_amount_fcy_resident;
	private BigDecimal	r0510_no_acct_aed_non_resident;
	private BigDecimal	r0510_amount_aed_non_resident;
	private BigDecimal	r0510_no_acct_fcy_non_resident;
	private BigDecimal	r0510_amount_fcy_non_resident;
	private BigDecimal	r0510_total_amount;
	private String	r0520_product;
	private BigDecimal	r0520_no_acct_aed_resident;
	private BigDecimal	r0520_amount_aed_resident;
	private BigDecimal	r0520_no_acct_fcy_resident;
	private BigDecimal	r0520_amount_fcy_resident;
	private BigDecimal	r0520_no_acct_aed_non_resident;
	private BigDecimal	r0520_amount_aed_non_resident;
	private BigDecimal	r0520_no_acct_fcy_non_resident;
	private BigDecimal	r0520_amount_fcy_non_resident;
	private BigDecimal	r0520_total_amount;
	private String	r0530_product;
	private BigDecimal	r0530_no_acct_aed_resident;
	private BigDecimal	r0530_amount_aed_resident;
	private BigDecimal	r0530_no_acct_fcy_resident;
	private BigDecimal	r0530_amount_fcy_resident;
	private BigDecimal	r0530_no_acct_aed_non_resident;
	private BigDecimal	r0530_amount_aed_non_resident;
	private BigDecimal	r0530_no_acct_fcy_non_resident;
	private BigDecimal	r0530_amount_fcy_non_resident;
	private BigDecimal	r0530_total_amount;
	private String	r0540_product;
	private BigDecimal	r0540_no_acct_aed_resident;
	private BigDecimal	r0540_amount_aed_resident;
	private BigDecimal	r0540_no_acct_fcy_resident;
	private BigDecimal	r0540_amount_fcy_resident;
	private BigDecimal	r0540_no_acct_aed_non_resident;
	private BigDecimal	r0540_amount_aed_non_resident;
	private BigDecimal	r0540_no_acct_fcy_non_resident;
	private BigDecimal	r0540_amount_fcy_non_resident;
	private BigDecimal	r0540_total_amount;
	private String	r0550_product;
	private BigDecimal	r0550_no_acct_aed_resident;
	private BigDecimal	r0550_amount_aed_resident;
	private BigDecimal	r0550_no_acct_fcy_resident;
	private BigDecimal	r0550_amount_fcy_resident;
	private BigDecimal	r0550_no_acct_aed_non_resident;
	private BigDecimal	r0550_amount_aed_non_resident;
	private BigDecimal	r0550_no_acct_fcy_non_resident;
	private BigDecimal	r0550_amount_fcy_non_resident;
	private BigDecimal	r0550_total_amount;
	private String	r0560_product;
	private BigDecimal	r0560_no_acct_aed_resident;
	private BigDecimal	r0560_amount_aed_resident;
	private BigDecimal	r0560_no_acct_fcy_resident;
	private BigDecimal	r0560_amount_fcy_resident;
	private BigDecimal	r0560_no_acct_aed_non_resident;
	private BigDecimal	r0560_amount_aed_non_resident;
	private BigDecimal	r0560_no_acct_fcy_non_resident;
	private BigDecimal	r0560_amount_fcy_non_resident;
	private BigDecimal	r0560_total_amount;
	private String	r0570_product;
	private BigDecimal	r0570_no_acct_aed_resident;
	private BigDecimal	r0570_amount_aed_resident;
	private BigDecimal	r0570_no_acct_fcy_resident;
	private BigDecimal	r0570_amount_fcy_resident;
	private BigDecimal	r0570_no_acct_aed_non_resident;
	private BigDecimal	r0570_amount_aed_non_resident;
	private BigDecimal	r0570_no_acct_fcy_non_resident;
	private BigDecimal	r0570_amount_fcy_non_resident;
	private BigDecimal	r0570_total_amount;
	private String	r0580_product;
	private BigDecimal	r0580_no_acct_aed_resident;
	private BigDecimal	r0580_amount_aed_resident;
	private BigDecimal	r0580_no_acct_fcy_resident;
	private BigDecimal	r0580_amount_fcy_resident;
	private BigDecimal	r0580_no_acct_aed_non_resident;
	private BigDecimal	r0580_amount_aed_non_resident;
	private BigDecimal	r0580_no_acct_fcy_non_resident;
	private BigDecimal	r0580_amount_fcy_non_resident;
	private BigDecimal	r0580_total_amount;
	private String	r0590_product;
	private BigDecimal	r0590_no_acct_aed_resident;
	private BigDecimal	r0590_amount_aed_resident;
	private BigDecimal	r0590_no_acct_fcy_resident;
	private BigDecimal	r0590_amount_fcy_resident;
	private BigDecimal	r0590_no_acct_aed_non_resident;
	private BigDecimal	r0590_amount_aed_non_resident;
	private BigDecimal	r0590_no_acct_fcy_non_resident;
	private BigDecimal	r0590_amount_fcy_non_resident;
	private BigDecimal	r0590_total_amount;
	private String	r0600_product;
	private BigDecimal	r0600_no_acct_aed_resident;
	private BigDecimal	r0600_amount_aed_resident;
	private BigDecimal	r0600_no_acct_fcy_resident;
	private BigDecimal	r0600_amount_fcy_resident;
	private BigDecimal	r0600_no_acct_aed_non_resident;
	private BigDecimal	r0600_amount_aed_non_resident;
	private BigDecimal	r0600_no_acct_fcy_non_resident;
	private BigDecimal	r0600_amount_fcy_non_resident;
	private BigDecimal	r0600_total_amount;
	private String	r0610_product;
	private BigDecimal	r0610_no_acct_aed_resident;
	private BigDecimal	r0610_amount_aed_resident;
	private BigDecimal	r0610_no_acct_fcy_resident;
	private BigDecimal	r0610_amount_fcy_resident;
	private BigDecimal	r0610_no_acct_aed_non_resident;
	private BigDecimal	r0610_amount_aed_non_resident;
	private BigDecimal	r0610_no_acct_fcy_non_resident;
	private BigDecimal	r0610_amount_fcy_non_resident;
	private BigDecimal	r0610_total_amount;
	private String	r0620_product;
	private BigDecimal	r0620_no_acct_aed_resident;
	private BigDecimal	r0620_amount_aed_resident;
	private BigDecimal	r0620_no_acct_fcy_resident;
	private BigDecimal	r0620_amount_fcy_resident;
	private BigDecimal	r0620_no_acct_aed_non_resident;
	private BigDecimal	r0620_amount_aed_non_resident;
	private BigDecimal	r0620_no_acct_fcy_non_resident;
	private BigDecimal	r0620_amount_fcy_non_resident;
	private BigDecimal	r0620_total_amount;
	private String	r0630_product;
	private BigDecimal	r0630_no_acct_aed_resident;
	private BigDecimal	r0630_amount_aed_resident;
	private BigDecimal	r0630_no_acct_fcy_resident;
	private BigDecimal	r0630_amount_fcy_resident;
	private BigDecimal	r0630_no_acct_aed_non_resident;
	private BigDecimal	r0630_amount_aed_non_resident;
	private BigDecimal	r0630_no_acct_fcy_non_resident;
	private BigDecimal	r0630_amount_fcy_non_resident;
	private BigDecimal	r0630_total_amount;
	private String	r0640_product;
	private BigDecimal	r0640_no_acct_aed_resident;
	private BigDecimal	r0640_amount_aed_resident;
	private BigDecimal	r0640_no_acct_fcy_resident;
	private BigDecimal	r0640_amount_fcy_resident;
	private BigDecimal	r0640_no_acct_aed_non_resident;
	private BigDecimal	r0640_amount_aed_non_resident;
	private BigDecimal	r0640_no_acct_fcy_non_resident;
	private BigDecimal	r0640_amount_fcy_non_resident;
	private BigDecimal	r0640_total_amount;
	private String	r0650_product;
	private BigDecimal	r0650_no_acct_aed_resident;
	private BigDecimal	r0650_amount_aed_resident;
	private BigDecimal	r0650_no_acct_fcy_resident;
	private BigDecimal	r0650_amount_fcy_resident;
	private BigDecimal	r0650_no_acct_aed_non_resident;
	private BigDecimal	r0650_amount_aed_non_resident;
	private BigDecimal	r0650_no_acct_fcy_non_resident;
	private BigDecimal	r0650_amount_fcy_non_resident;
	private BigDecimal	r0650_total_amount;
	private String	r0660_product;
	private BigDecimal	r0660_no_acct_aed_resident;
	private BigDecimal	r0660_amount_aed_resident;
	private BigDecimal	r0660_no_acct_fcy_resident;
	private BigDecimal	r0660_amount_fcy_resident;
	private BigDecimal	r0660_no_acct_aed_non_resident;
	private BigDecimal	r0660_amount_aed_non_resident;
	private BigDecimal	r0660_no_acct_fcy_non_resident;
	private BigDecimal	r0660_amount_fcy_non_resident;
	private BigDecimal	r0660_total_amount;
	private String	r0670_product;
	private BigDecimal	r0670_no_acct_aed_resident;
	private BigDecimal	r0670_amount_aed_resident;
	private BigDecimal	r0670_no_acct_fcy_resident;
	private BigDecimal	r0670_amount_fcy_resident;
	private BigDecimal	r0670_no_acct_aed_non_resident;
	private BigDecimal	r0670_amount_aed_non_resident;
	private BigDecimal	r0670_no_acct_fcy_non_resident;
	private BigDecimal	r0670_amount_fcy_non_resident;
	private BigDecimal	r0670_total_amount;
	private String	r0680_product;
	private BigDecimal	r0680_no_acct_aed_resident;
	private BigDecimal	r0680_amount_aed_resident;
	private BigDecimal	r0680_no_acct_fcy_resident;
	private BigDecimal	r0680_amount_fcy_resident;
	private BigDecimal	r0680_no_acct_aed_non_resident;
	private BigDecimal	r0680_amount_aed_non_resident;
	private BigDecimal	r0680_no_acct_fcy_non_resident;
	private BigDecimal	r0680_amount_fcy_non_resident;
	private BigDecimal	r0680_total_amount;
	private String	r0690_product;
	private BigDecimal	r0690_no_acct_aed_resident;
	private BigDecimal	r0690_amount_aed_resident;
	private BigDecimal	r0690_no_acct_fcy_resident;
	private BigDecimal	r0690_amount_fcy_resident;
	private BigDecimal	r0690_no_acct_aed_non_resident;
	private BigDecimal	r0690_amount_aed_non_resident;
	private BigDecimal	r0690_no_acct_fcy_non_resident;
	private BigDecimal	r0690_amount_fcy_non_resident;
	private BigDecimal	r0690_total_amount;
	private String	r0700_product;
	private BigDecimal	r0700_no_acct_aed_resident;
	private BigDecimal	r0700_amount_aed_resident;
	private BigDecimal	r0700_no_acct_fcy_resident;
	private BigDecimal	r0700_amount_fcy_resident;
	private BigDecimal	r0700_no_acct_aed_non_resident;
	private BigDecimal	r0700_amount_aed_non_resident;
	private BigDecimal	r0700_no_acct_fcy_non_resident;
	private BigDecimal	r0700_amount_fcy_non_resident;
	private BigDecimal	r0700_total_amount;
	private String	r0710_product;
	private BigDecimal	r0710_no_acct_aed_resident;
	private BigDecimal	r0710_amount_aed_resident;
	private BigDecimal	r0710_no_acct_fcy_resident;
	private BigDecimal	r0710_amount_fcy_resident;
	private BigDecimal	r0710_no_acct_aed_non_resident;
	private BigDecimal	r0710_amount_aed_non_resident;
	private BigDecimal	r0710_no_acct_fcy_non_resident;
	private BigDecimal	r0710_amount_fcy_non_resident;
	private BigDecimal	r0710_total_amount;
	private String	r0720_product;
	private BigDecimal	r0720_no_acct_aed_resident;
	private BigDecimal	r0720_amount_aed_resident;
	private BigDecimal	r0720_no_acct_fcy_resident;
	private BigDecimal	r0720_amount_fcy_resident;
	private BigDecimal	r0720_no_acct_aed_non_resident;
	private BigDecimal	r0720_amount_aed_non_resident;
	private BigDecimal	r0720_no_acct_fcy_non_resident;
	private BigDecimal	r0720_amount_fcy_non_resident;
	private BigDecimal	r0720_total_amount;
	private String	r0730_product;
	private BigDecimal	r0730_no_acct_aed_resident;
	private BigDecimal	r0730_amount_aed_resident;
	private BigDecimal	r0730_no_acct_fcy_resident;
	private BigDecimal	r0730_amount_fcy_resident;
	private BigDecimal	r0730_no_acct_aed_non_resident;
	private BigDecimal	r0730_amount_aed_non_resident;
	private BigDecimal	r0730_no_acct_fcy_non_resident;
	private BigDecimal	r0730_amount_fcy_non_resident;
	private BigDecimal	r0730_total_amount;
	private String	r0740_product;
	private BigDecimal	r0740_no_acct_aed_resident;
	private BigDecimal	r0740_amount_aed_resident;
	private BigDecimal	r0740_no_acct_fcy_resident;
	private BigDecimal	r0740_amount_fcy_resident;
	private BigDecimal	r0740_no_acct_aed_non_resident;
	private BigDecimal	r0740_amount_aed_non_resident;
	private BigDecimal	r0740_no_acct_fcy_non_resident;
	private BigDecimal	r0740_amount_fcy_non_resident;
	private BigDecimal	r0740_total_amount;
	private String	r0750_product;
	private BigDecimal	r0750_no_acct_aed_resident;
	private BigDecimal	r0750_amount_aed_resident;
	private BigDecimal	r0750_no_acct_fcy_resident;
	private BigDecimal	r0750_amount_fcy_resident;
	private BigDecimal	r0750_no_acct_aed_non_resident;
	private BigDecimal	r0750_amount_aed_non_resident;
	private BigDecimal	r0750_no_acct_fcy_non_resident;
	private BigDecimal	r0750_amount_fcy_non_resident;
	private BigDecimal	r0750_total_amount;
	private String	r0760_product;
	private BigDecimal	r0760_no_acct_aed_resident;
	private BigDecimal	r0760_amount_aed_resident;
	private BigDecimal	r0760_no_acct_fcy_resident;
	private BigDecimal	r0760_amount_fcy_resident;
	private BigDecimal	r0760_no_acct_aed_non_resident;
	private BigDecimal	r0760_amount_aed_non_resident;
	private BigDecimal	r0760_no_acct_fcy_non_resident;
	private BigDecimal	r0760_amount_fcy_non_resident;
	private BigDecimal	r0760_total_amount;
	private String	r0770_product;
	private BigDecimal	r0770_no_acct_aed_resident;
	private BigDecimal	r0770_amount_aed_resident;
	private BigDecimal	r0770_no_acct_fcy_resident;
	private BigDecimal	r0770_amount_fcy_resident;
	private BigDecimal	r0770_no_acct_aed_non_resident;
	private BigDecimal	r0770_amount_aed_non_resident;
	private BigDecimal	r0770_no_acct_fcy_non_resident;
	private BigDecimal	r0770_amount_fcy_non_resident;
	private BigDecimal	r0770_total_amount;
	private String	r0780_product;
	private BigDecimal	r0780_no_acct_aed_resident;
	private BigDecimal	r0780_amount_aed_resident;
	private BigDecimal	r0780_no_acct_fcy_resident;
	private BigDecimal	r0780_amount_fcy_resident;
	private BigDecimal	r0780_no_acct_aed_non_resident;
	private BigDecimal	r0780_amount_aed_non_resident;
	private BigDecimal	r0780_no_acct_fcy_non_resident;
	private BigDecimal	r0780_amount_fcy_non_resident;
	private BigDecimal	r0780_total_amount;
	private String	r0790_product;
	private BigDecimal	r0790_no_acct_aed_resident;
	private BigDecimal	r0790_amount_aed_resident;
	private BigDecimal	r0790_no_acct_fcy_resident;
	private BigDecimal	r0790_amount_fcy_resident;
	private BigDecimal	r0790_no_acct_aed_non_resident;
	private BigDecimal	r0790_amount_aed_non_resident;
	private BigDecimal	r0790_no_acct_fcy_non_resident;
	private BigDecimal	r0790_amount_fcy_non_resident;
	private BigDecimal	r0790_total_amount;
	private String	r0800_product;
	private BigDecimal	r0800_no_acct_aed_resident;
	private BigDecimal	r0800_amount_aed_resident;
	private BigDecimal	r0800_no_acct_fcy_resident;
	private BigDecimal	r0800_amount_fcy_resident;
	private BigDecimal	r0800_no_acct_aed_non_resident;
	private BigDecimal	r0800_amount_aed_non_resident;
	private BigDecimal	r0800_no_acct_fcy_non_resident;
	private BigDecimal	r0800_amount_fcy_non_resident;
	private BigDecimal	r0800_total_amount;
	private String	r0810_product;
	private BigDecimal	r0810_no_acct_aed_resident;
	private BigDecimal	r0810_amount_aed_resident;
	private BigDecimal	r0810_no_acct_fcy_resident;
	private BigDecimal	r0810_amount_fcy_resident;
	private BigDecimal	r0810_no_acct_aed_non_resident;
	private BigDecimal	r0810_amount_aed_non_resident;
	private BigDecimal	r0810_no_acct_fcy_non_resident;
	private BigDecimal	r0810_amount_fcy_non_resident;
	private BigDecimal	r0810_total_amount;
	private String	r0820_product;
	private BigDecimal	r0820_no_acct_aed_resident;
	private BigDecimal	r0820_amount_aed_resident;
	private BigDecimal	r0820_no_acct_fcy_resident;
	private BigDecimal	r0820_amount_fcy_resident;
	private BigDecimal	r0820_no_acct_aed_non_resident;
	private BigDecimal	r0820_amount_aed_non_resident;
	private BigDecimal	r0820_no_acct_fcy_non_resident;
	private BigDecimal	r0820_amount_fcy_non_resident;
	private BigDecimal	r0820_total_amount;
	private String	r0830_product;
	private BigDecimal	r0830_no_acct_aed_resident;
	private BigDecimal	r0830_amount_aed_resident;
	private BigDecimal	r0830_no_acct_fcy_resident;
	private BigDecimal	r0830_amount_fcy_resident;
	private BigDecimal	r0830_no_acct_aed_non_resident;
	private BigDecimal	r0830_amount_aed_non_resident;
	private BigDecimal	r0830_no_acct_fcy_non_resident;
	private BigDecimal	r0830_amount_fcy_non_resident;
	private BigDecimal	r0830_total_amount;
	private String	r0840_product;
	private BigDecimal	r0840_no_acct_aed_resident;
	private BigDecimal	r0840_amount_aed_resident;
	private BigDecimal	r0840_no_acct_fcy_resident;
	private BigDecimal	r0840_amount_fcy_resident;
	private BigDecimal	r0840_no_acct_aed_non_resident;
	private BigDecimal	r0840_amount_aed_non_resident;
	private BigDecimal	r0840_no_acct_fcy_non_resident;
	private BigDecimal	r0840_amount_fcy_non_resident;
	private BigDecimal	r0840_total_amount;
	private String	r0850_product;
	private BigDecimal	r0850_no_acct_aed_resident;
	private BigDecimal	r0850_amount_aed_resident;
	private BigDecimal	r0850_no_acct_fcy_resident;
	private BigDecimal	r0850_amount_fcy_resident;
	private BigDecimal	r0850_no_acct_aed_non_resident;
	private BigDecimal	r0850_amount_aed_non_resident;
	private BigDecimal	r0850_no_acct_fcy_non_resident;
	private BigDecimal	r0850_amount_fcy_non_resident;
	private BigDecimal	r0850_total_amount;
	private String	r0860_product;
	private BigDecimal	r0860_no_acct_aed_resident;
	private BigDecimal	r0860_amount_aed_resident;
	private BigDecimal	r0860_no_acct_fcy_resident;
	private BigDecimal	r0860_amount_fcy_resident;
	private BigDecimal	r0860_no_acct_aed_non_resident;
	private BigDecimal	r0860_amount_aed_non_resident;
	private BigDecimal	r0860_no_acct_fcy_non_resident;
	private BigDecimal	r0860_amount_fcy_non_resident;
	private BigDecimal	r0860_total_amount;
	private String	r0870_product;
	private BigDecimal	r0870_no_acct_aed_resident;
	private BigDecimal	r0870_amount_aed_resident;
	private BigDecimal	r0870_no_acct_fcy_resident;
	private BigDecimal	r0870_amount_fcy_resident;
	private BigDecimal	r0870_no_acct_aed_non_resident;
	private BigDecimal	r0870_amount_aed_non_resident;
	private BigDecimal	r0870_no_acct_fcy_non_resident;
	private BigDecimal	r0870_amount_fcy_non_resident;
	private BigDecimal	r0870_total_amount;
	private String	r0880_product;
	private BigDecimal	r0880_no_acct_aed_resident;
	private BigDecimal	r0880_amount_aed_resident;
	private BigDecimal	r0880_no_acct_fcy_resident;
	private BigDecimal	r0880_amount_fcy_resident;
	private BigDecimal	r0880_no_acct_aed_non_resident;
	private BigDecimal	r0880_amount_aed_non_resident;
	private BigDecimal	r0880_no_acct_fcy_non_resident;
	private BigDecimal	r0880_amount_fcy_non_resident;
	private BigDecimal	r0880_total_amount;
	private String	r0890_product;
	private BigDecimal	r0890_no_acct_aed_resident;
	private BigDecimal	r0890_amount_aed_resident;
	private BigDecimal	r0890_no_acct_fcy_resident;
	private BigDecimal	r0890_amount_fcy_resident;
	private BigDecimal	r0890_no_acct_aed_non_resident;
	private BigDecimal	r0890_amount_aed_non_resident;
	private BigDecimal	r0890_no_acct_fcy_non_resident;
	private BigDecimal	r0890_amount_fcy_non_resident;
	private BigDecimal	r0890_total_amount;
	private String	r0900_product;
	private BigDecimal	r0900_no_acct_aed_resident;
	private BigDecimal	r0900_amount_aed_resident;
	private BigDecimal	r0900_no_acct_fcy_resident;
	private BigDecimal	r0900_amount_fcy_resident;
	private BigDecimal	r0900_no_acct_aed_non_resident;
	private BigDecimal	r0900_amount_aed_non_resident;
	private BigDecimal	r0900_no_acct_fcy_non_resident;
	private BigDecimal	r0900_amount_fcy_non_resident;
	private BigDecimal	r0900_total_amount;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	private Date	report_date;
	private String	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	public String getR0010_product() {
		return r0010_product;
	}
	public void setR0010_product(String r0010_product) {
		this.r0010_product = r0010_product;
	}
	public BigDecimal getR0010_no_acct_aed_resident() {
		return r0010_no_acct_aed_resident;
	}
	public void setR0010_no_acct_aed_resident(BigDecimal r0010_no_acct_aed_resident) {
		this.r0010_no_acct_aed_resident = r0010_no_acct_aed_resident;
	}
	public BigDecimal getR0010_amount_aed_resident() {
		return r0010_amount_aed_resident;
	}
	public void setR0010_amount_aed_resident(BigDecimal r0010_amount_aed_resident) {
		this.r0010_amount_aed_resident = r0010_amount_aed_resident;
	}
	public BigDecimal getR0010_no_acct_fcy_resident() {
		return r0010_no_acct_fcy_resident;
	}
	public void setR0010_no_acct_fcy_resident(BigDecimal r0010_no_acct_fcy_resident) {
		this.r0010_no_acct_fcy_resident = r0010_no_acct_fcy_resident;
	}
	public BigDecimal getR0010_amount_fcy_resident() {
		return r0010_amount_fcy_resident;
	}
	public void setR0010_amount_fcy_resident(BigDecimal r0010_amount_fcy_resident) {
		this.r0010_amount_fcy_resident = r0010_amount_fcy_resident;
	}
	public BigDecimal getR0010_no_acct_aed_non_resident() {
		return r0010_no_acct_aed_non_resident;
	}
	public void setR0010_no_acct_aed_non_resident(BigDecimal r0010_no_acct_aed_non_resident) {
		this.r0010_no_acct_aed_non_resident = r0010_no_acct_aed_non_resident;
	}
	public BigDecimal getR0010_amount_aed_non_resident() {
		return r0010_amount_aed_non_resident;
	}
	public void setR0010_amount_aed_non_resident(BigDecimal r0010_amount_aed_non_resident) {
		this.r0010_amount_aed_non_resident = r0010_amount_aed_non_resident;
	}
	public BigDecimal getR0010_no_acct_fcy_non_resident() {
		return r0010_no_acct_fcy_non_resident;
	}
	public void setR0010_no_acct_fcy_non_resident(BigDecimal r0010_no_acct_fcy_non_resident) {
		this.r0010_no_acct_fcy_non_resident = r0010_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0010_amount_fcy_non_resident() {
		return r0010_amount_fcy_non_resident;
	}
	public void setR0010_amount_fcy_non_resident(BigDecimal r0010_amount_fcy_non_resident) {
		this.r0010_amount_fcy_non_resident = r0010_amount_fcy_non_resident;
	}
	public BigDecimal getR0010_total_amount() {
		return r0010_total_amount;
	}
	public void setR0010_total_amount(BigDecimal r0010_total_amount) {
		this.r0010_total_amount = r0010_total_amount;
	}
	public String getR0020_product() {
		return r0020_product;
	}
	public void setR0020_product(String r0020_product) {
		this.r0020_product = r0020_product;
	}
	public BigDecimal getR0020_no_acct_aed_resident() {
		return r0020_no_acct_aed_resident;
	}
	public void setR0020_no_acct_aed_resident(BigDecimal r0020_no_acct_aed_resident) {
		this.r0020_no_acct_aed_resident = r0020_no_acct_aed_resident;
	}
	public BigDecimal getR0020_amount_aed_resident() {
		return r0020_amount_aed_resident;
	}
	public void setR0020_amount_aed_resident(BigDecimal r0020_amount_aed_resident) {
		this.r0020_amount_aed_resident = r0020_amount_aed_resident;
	}
	public BigDecimal getR0020_no_acct_fcy_resident() {
		return r0020_no_acct_fcy_resident;
	}
	public void setR0020_no_acct_fcy_resident(BigDecimal r0020_no_acct_fcy_resident) {
		this.r0020_no_acct_fcy_resident = r0020_no_acct_fcy_resident;
	}
	public BigDecimal getR0020_amount_fcy_resident() {
		return r0020_amount_fcy_resident;
	}
	public void setR0020_amount_fcy_resident(BigDecimal r0020_amount_fcy_resident) {
		this.r0020_amount_fcy_resident = r0020_amount_fcy_resident;
	}
	public BigDecimal getR0020_no_acct_aed_non_resident() {
		return r0020_no_acct_aed_non_resident;
	}
	public void setR0020_no_acct_aed_non_resident(BigDecimal r0020_no_acct_aed_non_resident) {
		this.r0020_no_acct_aed_non_resident = r0020_no_acct_aed_non_resident;
	}
	public BigDecimal getR0020_amount_aed_non_resident() {
		return r0020_amount_aed_non_resident;
	}
	public void setR0020_amount_aed_non_resident(BigDecimal r0020_amount_aed_non_resident) {
		this.r0020_amount_aed_non_resident = r0020_amount_aed_non_resident;
	}
	public BigDecimal getR0020_no_acct_fcy_non_resident() {
		return r0020_no_acct_fcy_non_resident;
	}
	public void setR0020_no_acct_fcy_non_resident(BigDecimal r0020_no_acct_fcy_non_resident) {
		this.r0020_no_acct_fcy_non_resident = r0020_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0020_amount_fcy_non_resident() {
		return r0020_amount_fcy_non_resident;
	}
	public void setR0020_amount_fcy_non_resident(BigDecimal r0020_amount_fcy_non_resident) {
		this.r0020_amount_fcy_non_resident = r0020_amount_fcy_non_resident;
	}
	public BigDecimal getR0020_total_amount() {
		return r0020_total_amount;
	}
	public void setR0020_total_amount(BigDecimal r0020_total_amount) {
		this.r0020_total_amount = r0020_total_amount;
	}
	public String getR0030_product() {
		return r0030_product;
	}
	public void setR0030_product(String r0030_product) {
		this.r0030_product = r0030_product;
	}
	public BigDecimal getR0030_no_acct_aed_resident() {
		return r0030_no_acct_aed_resident;
	}
	public void setR0030_no_acct_aed_resident(BigDecimal r0030_no_acct_aed_resident) {
		this.r0030_no_acct_aed_resident = r0030_no_acct_aed_resident;
	}
	public BigDecimal getR0030_amount_aed_resident() {
		return r0030_amount_aed_resident;
	}
	public void setR0030_amount_aed_resident(BigDecimal r0030_amount_aed_resident) {
		this.r0030_amount_aed_resident = r0030_amount_aed_resident;
	}
	public BigDecimal getR0030_no_acct_fcy_resident() {
		return r0030_no_acct_fcy_resident;
	}
	public void setR0030_no_acct_fcy_resident(BigDecimal r0030_no_acct_fcy_resident) {
		this.r0030_no_acct_fcy_resident = r0030_no_acct_fcy_resident;
	}
	public BigDecimal getR0030_amount_fcy_resident() {
		return r0030_amount_fcy_resident;
	}
	public void setR0030_amount_fcy_resident(BigDecimal r0030_amount_fcy_resident) {
		this.r0030_amount_fcy_resident = r0030_amount_fcy_resident;
	}
	public BigDecimal getR0030_no_acct_aed_non_resident() {
		return r0030_no_acct_aed_non_resident;
	}
	public void setR0030_no_acct_aed_non_resident(BigDecimal r0030_no_acct_aed_non_resident) {
		this.r0030_no_acct_aed_non_resident = r0030_no_acct_aed_non_resident;
	}
	public BigDecimal getR0030_amount_aed_non_resident() {
		return r0030_amount_aed_non_resident;
	}
	public void setR0030_amount_aed_non_resident(BigDecimal r0030_amount_aed_non_resident) {
		this.r0030_amount_aed_non_resident = r0030_amount_aed_non_resident;
	}
	public BigDecimal getR0030_no_acct_fcy_non_resident() {
		return r0030_no_acct_fcy_non_resident;
	}
	public void setR0030_no_acct_fcy_non_resident(BigDecimal r0030_no_acct_fcy_non_resident) {
		this.r0030_no_acct_fcy_non_resident = r0030_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0030_amount_fcy_non_resident() {
		return r0030_amount_fcy_non_resident;
	}
	public void setR0030_amount_fcy_non_resident(BigDecimal r0030_amount_fcy_non_resident) {
		this.r0030_amount_fcy_non_resident = r0030_amount_fcy_non_resident;
	}
	public BigDecimal getR0030_total_amount() {
		return r0030_total_amount;
	}
	public void setR0030_total_amount(BigDecimal r0030_total_amount) {
		this.r0030_total_amount = r0030_total_amount;
	}
	public String getR0040_product() {
		return r0040_product;
	}
	public void setR0040_product(String r0040_product) {
		this.r0040_product = r0040_product;
	}
	public BigDecimal getR0040_no_acct_aed_resident() {
		return r0040_no_acct_aed_resident;
	}
	public void setR0040_no_acct_aed_resident(BigDecimal r0040_no_acct_aed_resident) {
		this.r0040_no_acct_aed_resident = r0040_no_acct_aed_resident;
	}
	public BigDecimal getR0040_amount_aed_resident() {
		return r0040_amount_aed_resident;
	}
	public void setR0040_amount_aed_resident(BigDecimal r0040_amount_aed_resident) {
		this.r0040_amount_aed_resident = r0040_amount_aed_resident;
	}
	public BigDecimal getR0040_no_acct_fcy_resident() {
		return r0040_no_acct_fcy_resident;
	}
	public void setR0040_no_acct_fcy_resident(BigDecimal r0040_no_acct_fcy_resident) {
		this.r0040_no_acct_fcy_resident = r0040_no_acct_fcy_resident;
	}
	public BigDecimal getR0040_amount_fcy_resident() {
		return r0040_amount_fcy_resident;
	}
	public void setR0040_amount_fcy_resident(BigDecimal r0040_amount_fcy_resident) {
		this.r0040_amount_fcy_resident = r0040_amount_fcy_resident;
	}
	public BigDecimal getR0040_no_acct_aed_non_resident() {
		return r0040_no_acct_aed_non_resident;
	}
	public void setR0040_no_acct_aed_non_resident(BigDecimal r0040_no_acct_aed_non_resident) {
		this.r0040_no_acct_aed_non_resident = r0040_no_acct_aed_non_resident;
	}
	public BigDecimal getR0040_amount_aed_non_resident() {
		return r0040_amount_aed_non_resident;
	}
	public void setR0040_amount_aed_non_resident(BigDecimal r0040_amount_aed_non_resident) {
		this.r0040_amount_aed_non_resident = r0040_amount_aed_non_resident;
	}
	public BigDecimal getR0040_no_acct_fcy_non_resident() {
		return r0040_no_acct_fcy_non_resident;
	}
	public void setR0040_no_acct_fcy_non_resident(BigDecimal r0040_no_acct_fcy_non_resident) {
		this.r0040_no_acct_fcy_non_resident = r0040_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0040_amount_fcy_non_resident() {
		return r0040_amount_fcy_non_resident;
	}
	public void setR0040_amount_fcy_non_resident(BigDecimal r0040_amount_fcy_non_resident) {
		this.r0040_amount_fcy_non_resident = r0040_amount_fcy_non_resident;
	}
	public BigDecimal getR0040_total_amount() {
		return r0040_total_amount;
	}
	public void setR0040_total_amount(BigDecimal r0040_total_amount) {
		this.r0040_total_amount = r0040_total_amount;
	}
	public String getR0050_product() {
		return r0050_product;
	}
	public void setR0050_product(String r0050_product) {
		this.r0050_product = r0050_product;
	}
	public BigDecimal getR0050_no_acct_aed_resident() {
		return r0050_no_acct_aed_resident;
	}
	public void setR0050_no_acct_aed_resident(BigDecimal r0050_no_acct_aed_resident) {
		this.r0050_no_acct_aed_resident = r0050_no_acct_aed_resident;
	}
	public BigDecimal getR0050_amount_aed_resident() {
		return r0050_amount_aed_resident;
	}
	public void setR0050_amount_aed_resident(BigDecimal r0050_amount_aed_resident) {
		this.r0050_amount_aed_resident = r0050_amount_aed_resident;
	}
	public BigDecimal getR0050_no_acct_fcy_resident() {
		return r0050_no_acct_fcy_resident;
	}
	public void setR0050_no_acct_fcy_resident(BigDecimal r0050_no_acct_fcy_resident) {
		this.r0050_no_acct_fcy_resident = r0050_no_acct_fcy_resident;
	}
	public BigDecimal getR0050_amount_fcy_resident() {
		return r0050_amount_fcy_resident;
	}
	public void setR0050_amount_fcy_resident(BigDecimal r0050_amount_fcy_resident) {
		this.r0050_amount_fcy_resident = r0050_amount_fcy_resident;
	}
	public BigDecimal getR0050_no_acct_aed_non_resident() {
		return r0050_no_acct_aed_non_resident;
	}
	public void setR0050_no_acct_aed_non_resident(BigDecimal r0050_no_acct_aed_non_resident) {
		this.r0050_no_acct_aed_non_resident = r0050_no_acct_aed_non_resident;
	}
	public BigDecimal getR0050_amount_aed_non_resident() {
		return r0050_amount_aed_non_resident;
	}
	public void setR0050_amount_aed_non_resident(BigDecimal r0050_amount_aed_non_resident) {
		this.r0050_amount_aed_non_resident = r0050_amount_aed_non_resident;
	}
	public BigDecimal getR0050_no_acct_fcy_non_resident() {
		return r0050_no_acct_fcy_non_resident;
	}
	public void setR0050_no_acct_fcy_non_resident(BigDecimal r0050_no_acct_fcy_non_resident) {
		this.r0050_no_acct_fcy_non_resident = r0050_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0050_amount_fcy_non_resident() {
		return r0050_amount_fcy_non_resident;
	}
	public void setR0050_amount_fcy_non_resident(BigDecimal r0050_amount_fcy_non_resident) {
		this.r0050_amount_fcy_non_resident = r0050_amount_fcy_non_resident;
	}
	public BigDecimal getR0050_total_amount() {
		return r0050_total_amount;
	}
	public void setR0050_total_amount(BigDecimal r0050_total_amount) {
		this.r0050_total_amount = r0050_total_amount;
	}
	public String getR0060_product() {
		return r0060_product;
	}
	public void setR0060_product(String r0060_product) {
		this.r0060_product = r0060_product;
	}
	public BigDecimal getR0060_no_acct_aed_resident() {
		return r0060_no_acct_aed_resident;
	}
	public void setR0060_no_acct_aed_resident(BigDecimal r0060_no_acct_aed_resident) {
		this.r0060_no_acct_aed_resident = r0060_no_acct_aed_resident;
	}
	public BigDecimal getR0060_amount_aed_resident() {
		return r0060_amount_aed_resident;
	}
	public void setR0060_amount_aed_resident(BigDecimal r0060_amount_aed_resident) {
		this.r0060_amount_aed_resident = r0060_amount_aed_resident;
	}
	public BigDecimal getR0060_no_acct_fcy_resident() {
		return r0060_no_acct_fcy_resident;
	}
	public void setR0060_no_acct_fcy_resident(BigDecimal r0060_no_acct_fcy_resident) {
		this.r0060_no_acct_fcy_resident = r0060_no_acct_fcy_resident;
	}
	public BigDecimal getR0060_amount_fcy_resident() {
		return r0060_amount_fcy_resident;
	}
	public void setR0060_amount_fcy_resident(BigDecimal r0060_amount_fcy_resident) {
		this.r0060_amount_fcy_resident = r0060_amount_fcy_resident;
	}
	public BigDecimal getR0060_no_acct_aed_non_resident() {
		return r0060_no_acct_aed_non_resident;
	}
	public void setR0060_no_acct_aed_non_resident(BigDecimal r0060_no_acct_aed_non_resident) {
		this.r0060_no_acct_aed_non_resident = r0060_no_acct_aed_non_resident;
	}
	public BigDecimal getR0060_amount_aed_non_resident() {
		return r0060_amount_aed_non_resident;
	}
	public void setR0060_amount_aed_non_resident(BigDecimal r0060_amount_aed_non_resident) {
		this.r0060_amount_aed_non_resident = r0060_amount_aed_non_resident;
	}
	public BigDecimal getR0060_no_acct_fcy_non_resident() {
		return r0060_no_acct_fcy_non_resident;
	}
	public void setR0060_no_acct_fcy_non_resident(BigDecimal r0060_no_acct_fcy_non_resident) {
		this.r0060_no_acct_fcy_non_resident = r0060_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0060_amount_fcy_non_resident() {
		return r0060_amount_fcy_non_resident;
	}
	public void setR0060_amount_fcy_non_resident(BigDecimal r0060_amount_fcy_non_resident) {
		this.r0060_amount_fcy_non_resident = r0060_amount_fcy_non_resident;
	}
	public BigDecimal getR0060_total_amount() {
		return r0060_total_amount;
	}
	public void setR0060_total_amount(BigDecimal r0060_total_amount) {
		this.r0060_total_amount = r0060_total_amount;
	}
	public String getR0070_product() {
		return r0070_product;
	}
	public void setR0070_product(String r0070_product) {
		this.r0070_product = r0070_product;
	}
	public BigDecimal getR0070_no_acct_aed_resident() {
		return r0070_no_acct_aed_resident;
	}
	public void setR0070_no_acct_aed_resident(BigDecimal r0070_no_acct_aed_resident) {
		this.r0070_no_acct_aed_resident = r0070_no_acct_aed_resident;
	}
	public BigDecimal getR0070_amount_aed_resident() {
		return r0070_amount_aed_resident;
	}
	public void setR0070_amount_aed_resident(BigDecimal r0070_amount_aed_resident) {
		this.r0070_amount_aed_resident = r0070_amount_aed_resident;
	}
	public BigDecimal getR0070_no_acct_fcy_resident() {
		return r0070_no_acct_fcy_resident;
	}
	public void setR0070_no_acct_fcy_resident(BigDecimal r0070_no_acct_fcy_resident) {
		this.r0070_no_acct_fcy_resident = r0070_no_acct_fcy_resident;
	}
	public BigDecimal getR0070_amount_fcy_resident() {
		return r0070_amount_fcy_resident;
	}
	public void setR0070_amount_fcy_resident(BigDecimal r0070_amount_fcy_resident) {
		this.r0070_amount_fcy_resident = r0070_amount_fcy_resident;
	}
	public BigDecimal getR0070_no_acct_aed_non_resident() {
		return r0070_no_acct_aed_non_resident;
	}
	public void setR0070_no_acct_aed_non_resident(BigDecimal r0070_no_acct_aed_non_resident) {
		this.r0070_no_acct_aed_non_resident = r0070_no_acct_aed_non_resident;
	}
	public BigDecimal getR0070_amount_aed_non_resident() {
		return r0070_amount_aed_non_resident;
	}
	public void setR0070_amount_aed_non_resident(BigDecimal r0070_amount_aed_non_resident) {
		this.r0070_amount_aed_non_resident = r0070_amount_aed_non_resident;
	}
	public BigDecimal getR0070_no_acct_fcy_non_resident() {
		return r0070_no_acct_fcy_non_resident;
	}
	public void setR0070_no_acct_fcy_non_resident(BigDecimal r0070_no_acct_fcy_non_resident) {
		this.r0070_no_acct_fcy_non_resident = r0070_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0070_amount_fcy_non_resident() {
		return r0070_amount_fcy_non_resident;
	}
	public void setR0070_amount_fcy_non_resident(BigDecimal r0070_amount_fcy_non_resident) {
		this.r0070_amount_fcy_non_resident = r0070_amount_fcy_non_resident;
	}
	public BigDecimal getR0070_total_amount() {
		return r0070_total_amount;
	}
	public void setR0070_total_amount(BigDecimal r0070_total_amount) {
		this.r0070_total_amount = r0070_total_amount;
	}
	public String getR0080_product() {
		return r0080_product;
	}
	public void setR0080_product(String r0080_product) {
		this.r0080_product = r0080_product;
	}
	public BigDecimal getR0080_no_acct_aed_resident() {
		return r0080_no_acct_aed_resident;
	}
	public void setR0080_no_acct_aed_resident(BigDecimal r0080_no_acct_aed_resident) {
		this.r0080_no_acct_aed_resident = r0080_no_acct_aed_resident;
	}
	public BigDecimal getR0080_amount_aed_resident() {
		return r0080_amount_aed_resident;
	}
	public void setR0080_amount_aed_resident(BigDecimal r0080_amount_aed_resident) {
		this.r0080_amount_aed_resident = r0080_amount_aed_resident;
	}
	public BigDecimal getR0080_no_acct_fcy_resident() {
		return r0080_no_acct_fcy_resident;
	}
	public void setR0080_no_acct_fcy_resident(BigDecimal r0080_no_acct_fcy_resident) {
		this.r0080_no_acct_fcy_resident = r0080_no_acct_fcy_resident;
	}
	public BigDecimal getR0080_amount_fcy_resident() {
		return r0080_amount_fcy_resident;
	}
	public void setR0080_amount_fcy_resident(BigDecimal r0080_amount_fcy_resident) {
		this.r0080_amount_fcy_resident = r0080_amount_fcy_resident;
	}
	public BigDecimal getR0080_no_acct_aed_non_resident() {
		return r0080_no_acct_aed_non_resident;
	}
	public void setR0080_no_acct_aed_non_resident(BigDecimal r0080_no_acct_aed_non_resident) {
		this.r0080_no_acct_aed_non_resident = r0080_no_acct_aed_non_resident;
	}
	public BigDecimal getR0080_amount_aed_non_resident() {
		return r0080_amount_aed_non_resident;
	}
	public void setR0080_amount_aed_non_resident(BigDecimal r0080_amount_aed_non_resident) {
		this.r0080_amount_aed_non_resident = r0080_amount_aed_non_resident;
	}
	public BigDecimal getR0080_no_acct_fcy_non_resident() {
		return r0080_no_acct_fcy_non_resident;
	}
	public void setR0080_no_acct_fcy_non_resident(BigDecimal r0080_no_acct_fcy_non_resident) {
		this.r0080_no_acct_fcy_non_resident = r0080_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0080_amount_fcy_non_resident() {
		return r0080_amount_fcy_non_resident;
	}
	public void setR0080_amount_fcy_non_resident(BigDecimal r0080_amount_fcy_non_resident) {
		this.r0080_amount_fcy_non_resident = r0080_amount_fcy_non_resident;
	}
	public BigDecimal getR0080_total_amount() {
		return r0080_total_amount;
	}
	public void setR0080_total_amount(BigDecimal r0080_total_amount) {
		this.r0080_total_amount = r0080_total_amount;
	}
	public String getR0090_product() {
		return r0090_product;
	}
	public void setR0090_product(String r0090_product) {
		this.r0090_product = r0090_product;
	}
	public BigDecimal getR0090_no_acct_aed_resident() {
		return r0090_no_acct_aed_resident;
	}
	public void setR0090_no_acct_aed_resident(BigDecimal r0090_no_acct_aed_resident) {
		this.r0090_no_acct_aed_resident = r0090_no_acct_aed_resident;
	}
	public BigDecimal getR0090_amount_aed_resident() {
		return r0090_amount_aed_resident;
	}
	public void setR0090_amount_aed_resident(BigDecimal r0090_amount_aed_resident) {
		this.r0090_amount_aed_resident = r0090_amount_aed_resident;
	}
	public BigDecimal getR0090_no_acct_fcy_resident() {
		return r0090_no_acct_fcy_resident;
	}
	public void setR0090_no_acct_fcy_resident(BigDecimal r0090_no_acct_fcy_resident) {
		this.r0090_no_acct_fcy_resident = r0090_no_acct_fcy_resident;
	}
	public BigDecimal getR0090_amount_fcy_resident() {
		return r0090_amount_fcy_resident;
	}
	public void setR0090_amount_fcy_resident(BigDecimal r0090_amount_fcy_resident) {
		this.r0090_amount_fcy_resident = r0090_amount_fcy_resident;
	}
	public BigDecimal getR0090_no_acct_aed_non_resident() {
		return r0090_no_acct_aed_non_resident;
	}
	public void setR0090_no_acct_aed_non_resident(BigDecimal r0090_no_acct_aed_non_resident) {
		this.r0090_no_acct_aed_non_resident = r0090_no_acct_aed_non_resident;
	}
	public BigDecimal getR0090_amount_aed_non_resident() {
		return r0090_amount_aed_non_resident;
	}
	public void setR0090_amount_aed_non_resident(BigDecimal r0090_amount_aed_non_resident) {
		this.r0090_amount_aed_non_resident = r0090_amount_aed_non_resident;
	}
	public BigDecimal getR0090_no_acct_fcy_non_resident() {
		return r0090_no_acct_fcy_non_resident;
	}
	public void setR0090_no_acct_fcy_non_resident(BigDecimal r0090_no_acct_fcy_non_resident) {
		this.r0090_no_acct_fcy_non_resident = r0090_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0090_amount_fcy_non_resident() {
		return r0090_amount_fcy_non_resident;
	}
	public void setR0090_amount_fcy_non_resident(BigDecimal r0090_amount_fcy_non_resident) {
		this.r0090_amount_fcy_non_resident = r0090_amount_fcy_non_resident;
	}
	public BigDecimal getR0090_total_amount() {
		return r0090_total_amount;
	}
	public void setR0090_total_amount(BigDecimal r0090_total_amount) {
		this.r0090_total_amount = r0090_total_amount;
	}
	public String getR0100_product() {
		return r0100_product;
	}
	public void setR0100_product(String r0100_product) {
		this.r0100_product = r0100_product;
	}
	public BigDecimal getR0100_no_acct_aed_resident() {
		return r0100_no_acct_aed_resident;
	}
	public void setR0100_no_acct_aed_resident(BigDecimal r0100_no_acct_aed_resident) {
		this.r0100_no_acct_aed_resident = r0100_no_acct_aed_resident;
	}
	public BigDecimal getR0100_amount_aed_resident() {
		return r0100_amount_aed_resident;
	}
	public void setR0100_amount_aed_resident(BigDecimal r0100_amount_aed_resident) {
		this.r0100_amount_aed_resident = r0100_amount_aed_resident;
	}
	public BigDecimal getR0100_no_acct_fcy_resident() {
		return r0100_no_acct_fcy_resident;
	}
	public void setR0100_no_acct_fcy_resident(BigDecimal r0100_no_acct_fcy_resident) {
		this.r0100_no_acct_fcy_resident = r0100_no_acct_fcy_resident;
	}
	public BigDecimal getR0100_amount_fcy_resident() {
		return r0100_amount_fcy_resident;
	}
	public void setR0100_amount_fcy_resident(BigDecimal r0100_amount_fcy_resident) {
		this.r0100_amount_fcy_resident = r0100_amount_fcy_resident;
	}
	public BigDecimal getR0100_no_acct_aed_non_resident() {
		return r0100_no_acct_aed_non_resident;
	}
	public void setR0100_no_acct_aed_non_resident(BigDecimal r0100_no_acct_aed_non_resident) {
		this.r0100_no_acct_aed_non_resident = r0100_no_acct_aed_non_resident;
	}
	public BigDecimal getR0100_amount_aed_non_resident() {
		return r0100_amount_aed_non_resident;
	}
	public void setR0100_amount_aed_non_resident(BigDecimal r0100_amount_aed_non_resident) {
		this.r0100_amount_aed_non_resident = r0100_amount_aed_non_resident;
	}
	public BigDecimal getR0100_no_acct_fcy_non_resident() {
		return r0100_no_acct_fcy_non_resident;
	}
	public void setR0100_no_acct_fcy_non_resident(BigDecimal r0100_no_acct_fcy_non_resident) {
		this.r0100_no_acct_fcy_non_resident = r0100_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0100_amount_fcy_non_resident() {
		return r0100_amount_fcy_non_resident;
	}
	public void setR0100_amount_fcy_non_resident(BigDecimal r0100_amount_fcy_non_resident) {
		this.r0100_amount_fcy_non_resident = r0100_amount_fcy_non_resident;
	}
	public BigDecimal getR0100_total_amount() {
		return r0100_total_amount;
	}
	public void setR0100_total_amount(BigDecimal r0100_total_amount) {
		this.r0100_total_amount = r0100_total_amount;
	}
	public String getR0110_product() {
		return r0110_product;
	}
	public void setR0110_product(String r0110_product) {
		this.r0110_product = r0110_product;
	}
	public BigDecimal getR0110_no_acct_aed_resident() {
		return r0110_no_acct_aed_resident;
	}
	public void setR0110_no_acct_aed_resident(BigDecimal r0110_no_acct_aed_resident) {
		this.r0110_no_acct_aed_resident = r0110_no_acct_aed_resident;
	}
	public BigDecimal getR0110_amount_aed_resident() {
		return r0110_amount_aed_resident;
	}
	public void setR0110_amount_aed_resident(BigDecimal r0110_amount_aed_resident) {
		this.r0110_amount_aed_resident = r0110_amount_aed_resident;
	}
	public BigDecimal getR0110_no_acct_fcy_resident() {
		return r0110_no_acct_fcy_resident;
	}
	public void setR0110_no_acct_fcy_resident(BigDecimal r0110_no_acct_fcy_resident) {
		this.r0110_no_acct_fcy_resident = r0110_no_acct_fcy_resident;
	}
	public BigDecimal getR0110_amount_fcy_resident() {
		return r0110_amount_fcy_resident;
	}
	public void setR0110_amount_fcy_resident(BigDecimal r0110_amount_fcy_resident) {
		this.r0110_amount_fcy_resident = r0110_amount_fcy_resident;
	}
	public BigDecimal getR0110_no_acct_aed_non_resident() {
		return r0110_no_acct_aed_non_resident;
	}
	public void setR0110_no_acct_aed_non_resident(BigDecimal r0110_no_acct_aed_non_resident) {
		this.r0110_no_acct_aed_non_resident = r0110_no_acct_aed_non_resident;
	}
	public BigDecimal getR0110_amount_aed_non_resident() {
		return r0110_amount_aed_non_resident;
	}
	public void setR0110_amount_aed_non_resident(BigDecimal r0110_amount_aed_non_resident) {
		this.r0110_amount_aed_non_resident = r0110_amount_aed_non_resident;
	}
	public BigDecimal getR0110_no_acct_fcy_non_resident() {
		return r0110_no_acct_fcy_non_resident;
	}
	public void setR0110_no_acct_fcy_non_resident(BigDecimal r0110_no_acct_fcy_non_resident) {
		this.r0110_no_acct_fcy_non_resident = r0110_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0110_amount_fcy_non_resident() {
		return r0110_amount_fcy_non_resident;
	}
	public void setR0110_amount_fcy_non_resident(BigDecimal r0110_amount_fcy_non_resident) {
		this.r0110_amount_fcy_non_resident = r0110_amount_fcy_non_resident;
	}
	public BigDecimal getR0110_total_amount() {
		return r0110_total_amount;
	}
	public void setR0110_total_amount(BigDecimal r0110_total_amount) {
		this.r0110_total_amount = r0110_total_amount;
	}
	public String getR0120_product() {
		return r0120_product;
	}
	public void setR0120_product(String r0120_product) {
		this.r0120_product = r0120_product;
	}
	public BigDecimal getR0120_no_acct_aed_resident() {
		return r0120_no_acct_aed_resident;
	}
	public void setR0120_no_acct_aed_resident(BigDecimal r0120_no_acct_aed_resident) {
		this.r0120_no_acct_aed_resident = r0120_no_acct_aed_resident;
	}
	public BigDecimal getR0120_amount_aed_resident() {
		return r0120_amount_aed_resident;
	}
	public void setR0120_amount_aed_resident(BigDecimal r0120_amount_aed_resident) {
		this.r0120_amount_aed_resident = r0120_amount_aed_resident;
	}
	public BigDecimal getR0120_no_acct_fcy_resident() {
		return r0120_no_acct_fcy_resident;
	}
	public void setR0120_no_acct_fcy_resident(BigDecimal r0120_no_acct_fcy_resident) {
		this.r0120_no_acct_fcy_resident = r0120_no_acct_fcy_resident;
	}
	public BigDecimal getR0120_amount_fcy_resident() {
		return r0120_amount_fcy_resident;
	}
	public void setR0120_amount_fcy_resident(BigDecimal r0120_amount_fcy_resident) {
		this.r0120_amount_fcy_resident = r0120_amount_fcy_resident;
	}
	public BigDecimal getR0120_no_acct_aed_non_resident() {
		return r0120_no_acct_aed_non_resident;
	}
	public void setR0120_no_acct_aed_non_resident(BigDecimal r0120_no_acct_aed_non_resident) {
		this.r0120_no_acct_aed_non_resident = r0120_no_acct_aed_non_resident;
	}
	public BigDecimal getR0120_amount_aed_non_resident() {
		return r0120_amount_aed_non_resident;
	}
	public void setR0120_amount_aed_non_resident(BigDecimal r0120_amount_aed_non_resident) {
		this.r0120_amount_aed_non_resident = r0120_amount_aed_non_resident;
	}
	public BigDecimal getR0120_no_acct_fcy_non_resident() {
		return r0120_no_acct_fcy_non_resident;
	}
	public void setR0120_no_acct_fcy_non_resident(BigDecimal r0120_no_acct_fcy_non_resident) {
		this.r0120_no_acct_fcy_non_resident = r0120_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0120_amount_fcy_non_resident() {
		return r0120_amount_fcy_non_resident;
	}
	public void setR0120_amount_fcy_non_resident(BigDecimal r0120_amount_fcy_non_resident) {
		this.r0120_amount_fcy_non_resident = r0120_amount_fcy_non_resident;
	}
	public BigDecimal getR0120_total_amount() {
		return r0120_total_amount;
	}
	public void setR0120_total_amount(BigDecimal r0120_total_amount) {
		this.r0120_total_amount = r0120_total_amount;
	}
	public String getR0130_product() {
		return r0130_product;
	}
	public void setR0130_product(String r0130_product) {
		this.r0130_product = r0130_product;
	}
	public BigDecimal getR0130_no_acct_aed_resident() {
		return r0130_no_acct_aed_resident;
	}
	public void setR0130_no_acct_aed_resident(BigDecimal r0130_no_acct_aed_resident) {
		this.r0130_no_acct_aed_resident = r0130_no_acct_aed_resident;
	}
	public BigDecimal getR0130_amount_aed_resident() {
		return r0130_amount_aed_resident;
	}
	public void setR0130_amount_aed_resident(BigDecimal r0130_amount_aed_resident) {
		this.r0130_amount_aed_resident = r0130_amount_aed_resident;
	}
	public BigDecimal getR0130_no_acct_fcy_resident() {
		return r0130_no_acct_fcy_resident;
	}
	public void setR0130_no_acct_fcy_resident(BigDecimal r0130_no_acct_fcy_resident) {
		this.r0130_no_acct_fcy_resident = r0130_no_acct_fcy_resident;
	}
	public BigDecimal getR0130_amount_fcy_resident() {
		return r0130_amount_fcy_resident;
	}
	public void setR0130_amount_fcy_resident(BigDecimal r0130_amount_fcy_resident) {
		this.r0130_amount_fcy_resident = r0130_amount_fcy_resident;
	}
	public BigDecimal getR0130_no_acct_aed_non_resident() {
		return r0130_no_acct_aed_non_resident;
	}
	public void setR0130_no_acct_aed_non_resident(BigDecimal r0130_no_acct_aed_non_resident) {
		this.r0130_no_acct_aed_non_resident = r0130_no_acct_aed_non_resident;
	}
	public BigDecimal getR0130_amount_aed_non_resident() {
		return r0130_amount_aed_non_resident;
	}
	public void setR0130_amount_aed_non_resident(BigDecimal r0130_amount_aed_non_resident) {
		this.r0130_amount_aed_non_resident = r0130_amount_aed_non_resident;
	}
	public BigDecimal getR0130_no_acct_fcy_non_resident() {
		return r0130_no_acct_fcy_non_resident;
	}
	public void setR0130_no_acct_fcy_non_resident(BigDecimal r0130_no_acct_fcy_non_resident) {
		this.r0130_no_acct_fcy_non_resident = r0130_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0130_amount_fcy_non_resident() {
		return r0130_amount_fcy_non_resident;
	}
	public void setR0130_amount_fcy_non_resident(BigDecimal r0130_amount_fcy_non_resident) {
		this.r0130_amount_fcy_non_resident = r0130_amount_fcy_non_resident;
	}
	public BigDecimal getR0130_total_amount() {
		return r0130_total_amount;
	}
	public void setR0130_total_amount(BigDecimal r0130_total_amount) {
		this.r0130_total_amount = r0130_total_amount;
	}
	public String getR0140_product() {
		return r0140_product;
	}
	public void setR0140_product(String r0140_product) {
		this.r0140_product = r0140_product;
	}
	public BigDecimal getR0140_no_acct_aed_resident() {
		return r0140_no_acct_aed_resident;
	}
	public void setR0140_no_acct_aed_resident(BigDecimal r0140_no_acct_aed_resident) {
		this.r0140_no_acct_aed_resident = r0140_no_acct_aed_resident;
	}
	public BigDecimal getR0140_amount_aed_resident() {
		return r0140_amount_aed_resident;
	}
	public void setR0140_amount_aed_resident(BigDecimal r0140_amount_aed_resident) {
		this.r0140_amount_aed_resident = r0140_amount_aed_resident;
	}
	public BigDecimal getR0140_no_acct_fcy_resident() {
		return r0140_no_acct_fcy_resident;
	}
	public void setR0140_no_acct_fcy_resident(BigDecimal r0140_no_acct_fcy_resident) {
		this.r0140_no_acct_fcy_resident = r0140_no_acct_fcy_resident;
	}
	public BigDecimal getR0140_amount_fcy_resident() {
		return r0140_amount_fcy_resident;
	}
	public void setR0140_amount_fcy_resident(BigDecimal r0140_amount_fcy_resident) {
		this.r0140_amount_fcy_resident = r0140_amount_fcy_resident;
	}
	public BigDecimal getR0140_no_acct_aed_non_resident() {
		return r0140_no_acct_aed_non_resident;
	}
	public void setR0140_no_acct_aed_non_resident(BigDecimal r0140_no_acct_aed_non_resident) {
		this.r0140_no_acct_aed_non_resident = r0140_no_acct_aed_non_resident;
	}
	public BigDecimal getR0140_amount_aed_non_resident() {
		return r0140_amount_aed_non_resident;
	}
	public void setR0140_amount_aed_non_resident(BigDecimal r0140_amount_aed_non_resident) {
		this.r0140_amount_aed_non_resident = r0140_amount_aed_non_resident;
	}
	public BigDecimal getR0140_no_acct_fcy_non_resident() {
		return r0140_no_acct_fcy_non_resident;
	}
	public void setR0140_no_acct_fcy_non_resident(BigDecimal r0140_no_acct_fcy_non_resident) {
		this.r0140_no_acct_fcy_non_resident = r0140_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0140_amount_fcy_non_resident() {
		return r0140_amount_fcy_non_resident;
	}
	public void setR0140_amount_fcy_non_resident(BigDecimal r0140_amount_fcy_non_resident) {
		this.r0140_amount_fcy_non_resident = r0140_amount_fcy_non_resident;
	}
	public BigDecimal getR0140_total_amount() {
		return r0140_total_amount;
	}
	public void setR0140_total_amount(BigDecimal r0140_total_amount) {
		this.r0140_total_amount = r0140_total_amount;
	}
	public String getR0150_product() {
		return r0150_product;
	}
	public void setR0150_product(String r0150_product) {
		this.r0150_product = r0150_product;
	}
	public BigDecimal getR0150_no_acct_aed_resident() {
		return r0150_no_acct_aed_resident;
	}
	public void setR0150_no_acct_aed_resident(BigDecimal r0150_no_acct_aed_resident) {
		this.r0150_no_acct_aed_resident = r0150_no_acct_aed_resident;
	}
	public BigDecimal getR0150_amount_aed_resident() {
		return r0150_amount_aed_resident;
	}
	public void setR0150_amount_aed_resident(BigDecimal r0150_amount_aed_resident) {
		this.r0150_amount_aed_resident = r0150_amount_aed_resident;
	}
	public BigDecimal getR0150_no_acct_fcy_resident() {
		return r0150_no_acct_fcy_resident;
	}
	public void setR0150_no_acct_fcy_resident(BigDecimal r0150_no_acct_fcy_resident) {
		this.r0150_no_acct_fcy_resident = r0150_no_acct_fcy_resident;
	}
	public BigDecimal getR0150_amount_fcy_resident() {
		return r0150_amount_fcy_resident;
	}
	public void setR0150_amount_fcy_resident(BigDecimal r0150_amount_fcy_resident) {
		this.r0150_amount_fcy_resident = r0150_amount_fcy_resident;
	}
	public BigDecimal getR0150_no_acct_aed_non_resident() {
		return r0150_no_acct_aed_non_resident;
	}
	public void setR0150_no_acct_aed_non_resident(BigDecimal r0150_no_acct_aed_non_resident) {
		this.r0150_no_acct_aed_non_resident = r0150_no_acct_aed_non_resident;
	}
	public BigDecimal getR0150_amount_aed_non_resident() {
		return r0150_amount_aed_non_resident;
	}
	public void setR0150_amount_aed_non_resident(BigDecimal r0150_amount_aed_non_resident) {
		this.r0150_amount_aed_non_resident = r0150_amount_aed_non_resident;
	}
	public BigDecimal getR0150_no_acct_fcy_non_resident() {
		return r0150_no_acct_fcy_non_resident;
	}
	public void setR0150_no_acct_fcy_non_resident(BigDecimal r0150_no_acct_fcy_non_resident) {
		this.r0150_no_acct_fcy_non_resident = r0150_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0150_amount_fcy_non_resident() {
		return r0150_amount_fcy_non_resident;
	}
	public void setR0150_amount_fcy_non_resident(BigDecimal r0150_amount_fcy_non_resident) {
		this.r0150_amount_fcy_non_resident = r0150_amount_fcy_non_resident;
	}
	public BigDecimal getR0150_total_amount() {
		return r0150_total_amount;
	}
	public void setR0150_total_amount(BigDecimal r0150_total_amount) {
		this.r0150_total_amount = r0150_total_amount;
	}
	public String getR0160_product() {
		return r0160_product;
	}
	public void setR0160_product(String r0160_product) {
		this.r0160_product = r0160_product;
	}
	public BigDecimal getR0160_no_acct_aed_resident() {
		return r0160_no_acct_aed_resident;
	}
	public void setR0160_no_acct_aed_resident(BigDecimal r0160_no_acct_aed_resident) {
		this.r0160_no_acct_aed_resident = r0160_no_acct_aed_resident;
	}
	public BigDecimal getR0160_amount_aed_resident() {
		return r0160_amount_aed_resident;
	}
	public void setR0160_amount_aed_resident(BigDecimal r0160_amount_aed_resident) {
		this.r0160_amount_aed_resident = r0160_amount_aed_resident;
	}
	public BigDecimal getR0160_no_acct_fcy_resident() {
		return r0160_no_acct_fcy_resident;
	}
	public void setR0160_no_acct_fcy_resident(BigDecimal r0160_no_acct_fcy_resident) {
		this.r0160_no_acct_fcy_resident = r0160_no_acct_fcy_resident;
	}
	public BigDecimal getR0160_amount_fcy_resident() {
		return r0160_amount_fcy_resident;
	}
	public void setR0160_amount_fcy_resident(BigDecimal r0160_amount_fcy_resident) {
		this.r0160_amount_fcy_resident = r0160_amount_fcy_resident;
	}
	public BigDecimal getR0160_no_acct_aed_non_resident() {
		return r0160_no_acct_aed_non_resident;
	}
	public void setR0160_no_acct_aed_non_resident(BigDecimal r0160_no_acct_aed_non_resident) {
		this.r0160_no_acct_aed_non_resident = r0160_no_acct_aed_non_resident;
	}
	public BigDecimal getR0160_amount_aed_non_resident() {
		return r0160_amount_aed_non_resident;
	}
	public void setR0160_amount_aed_non_resident(BigDecimal r0160_amount_aed_non_resident) {
		this.r0160_amount_aed_non_resident = r0160_amount_aed_non_resident;
	}
	public BigDecimal getR0160_no_acct_fcy_non_resident() {
		return r0160_no_acct_fcy_non_resident;
	}
	public void setR0160_no_acct_fcy_non_resident(BigDecimal r0160_no_acct_fcy_non_resident) {
		this.r0160_no_acct_fcy_non_resident = r0160_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0160_amount_fcy_non_resident() {
		return r0160_amount_fcy_non_resident;
	}
	public void setR0160_amount_fcy_non_resident(BigDecimal r0160_amount_fcy_non_resident) {
		this.r0160_amount_fcy_non_resident = r0160_amount_fcy_non_resident;
	}
	public BigDecimal getR0160_total_amount() {
		return r0160_total_amount;
	}
	public void setR0160_total_amount(BigDecimal r0160_total_amount) {
		this.r0160_total_amount = r0160_total_amount;
	}
	public String getR0170_product() {
		return r0170_product;
	}
	public void setR0170_product(String r0170_product) {
		this.r0170_product = r0170_product;
	}
	public BigDecimal getR0170_no_acct_aed_resident() {
		return r0170_no_acct_aed_resident;
	}
	public void setR0170_no_acct_aed_resident(BigDecimal r0170_no_acct_aed_resident) {
		this.r0170_no_acct_aed_resident = r0170_no_acct_aed_resident;
	}
	public BigDecimal getR0170_amount_aed_resident() {
		return r0170_amount_aed_resident;
	}
	public void setR0170_amount_aed_resident(BigDecimal r0170_amount_aed_resident) {
		this.r0170_amount_aed_resident = r0170_amount_aed_resident;
	}
	public BigDecimal getR0170_no_acct_fcy_resident() {
		return r0170_no_acct_fcy_resident;
	}
	public void setR0170_no_acct_fcy_resident(BigDecimal r0170_no_acct_fcy_resident) {
		this.r0170_no_acct_fcy_resident = r0170_no_acct_fcy_resident;
	}
	public BigDecimal getR0170_amount_fcy_resident() {
		return r0170_amount_fcy_resident;
	}
	public void setR0170_amount_fcy_resident(BigDecimal r0170_amount_fcy_resident) {
		this.r0170_amount_fcy_resident = r0170_amount_fcy_resident;
	}
	public BigDecimal getR0170_no_acct_aed_non_resident() {
		return r0170_no_acct_aed_non_resident;
	}
	public void setR0170_no_acct_aed_non_resident(BigDecimal r0170_no_acct_aed_non_resident) {
		this.r0170_no_acct_aed_non_resident = r0170_no_acct_aed_non_resident;
	}
	public BigDecimal getR0170_amount_aed_non_resident() {
		return r0170_amount_aed_non_resident;
	}
	public void setR0170_amount_aed_non_resident(BigDecimal r0170_amount_aed_non_resident) {
		this.r0170_amount_aed_non_resident = r0170_amount_aed_non_resident;
	}
	public BigDecimal getR0170_no_acct_fcy_non_resident() {
		return r0170_no_acct_fcy_non_resident;
	}
	public void setR0170_no_acct_fcy_non_resident(BigDecimal r0170_no_acct_fcy_non_resident) {
		this.r0170_no_acct_fcy_non_resident = r0170_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0170_amount_fcy_non_resident() {
		return r0170_amount_fcy_non_resident;
	}
	public void setR0170_amount_fcy_non_resident(BigDecimal r0170_amount_fcy_non_resident) {
		this.r0170_amount_fcy_non_resident = r0170_amount_fcy_non_resident;
	}
	public BigDecimal getR0170_total_amount() {
		return r0170_total_amount;
	}
	public void setR0170_total_amount(BigDecimal r0170_total_amount) {
		this.r0170_total_amount = r0170_total_amount;
	}
	public String getR0180_product() {
		return r0180_product;
	}
	public void setR0180_product(String r0180_product) {
		this.r0180_product = r0180_product;
	}
	public BigDecimal getR0180_no_acct_aed_resident() {
		return r0180_no_acct_aed_resident;
	}
	public void setR0180_no_acct_aed_resident(BigDecimal r0180_no_acct_aed_resident) {
		this.r0180_no_acct_aed_resident = r0180_no_acct_aed_resident;
	}
	public BigDecimal getR0180_amount_aed_resident() {
		return r0180_amount_aed_resident;
	}
	public void setR0180_amount_aed_resident(BigDecimal r0180_amount_aed_resident) {
		this.r0180_amount_aed_resident = r0180_amount_aed_resident;
	}
	public BigDecimal getR0180_no_acct_fcy_resident() {
		return r0180_no_acct_fcy_resident;
	}
	public void setR0180_no_acct_fcy_resident(BigDecimal r0180_no_acct_fcy_resident) {
		this.r0180_no_acct_fcy_resident = r0180_no_acct_fcy_resident;
	}
	public BigDecimal getR0180_amount_fcy_resident() {
		return r0180_amount_fcy_resident;
	}
	public void setR0180_amount_fcy_resident(BigDecimal r0180_amount_fcy_resident) {
		this.r0180_amount_fcy_resident = r0180_amount_fcy_resident;
	}
	public BigDecimal getR0180_no_acct_aed_non_resident() {
		return r0180_no_acct_aed_non_resident;
	}
	public void setR0180_no_acct_aed_non_resident(BigDecimal r0180_no_acct_aed_non_resident) {
		this.r0180_no_acct_aed_non_resident = r0180_no_acct_aed_non_resident;
	}
	public BigDecimal getR0180_amount_aed_non_resident() {
		return r0180_amount_aed_non_resident;
	}
	public void setR0180_amount_aed_non_resident(BigDecimal r0180_amount_aed_non_resident) {
		this.r0180_amount_aed_non_resident = r0180_amount_aed_non_resident;
	}
	public BigDecimal getR0180_no_acct_fcy_non_resident() {
		return r0180_no_acct_fcy_non_resident;
	}
	public void setR0180_no_acct_fcy_non_resident(BigDecimal r0180_no_acct_fcy_non_resident) {
		this.r0180_no_acct_fcy_non_resident = r0180_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0180_amount_fcy_non_resident() {
		return r0180_amount_fcy_non_resident;
	}
	public void setR0180_amount_fcy_non_resident(BigDecimal r0180_amount_fcy_non_resident) {
		this.r0180_amount_fcy_non_resident = r0180_amount_fcy_non_resident;
	}
	public BigDecimal getR0180_total_amount() {
		return r0180_total_amount;
	}
	public void setR0180_total_amount(BigDecimal r0180_total_amount) {
		this.r0180_total_amount = r0180_total_amount;
	}
	public String getR0190_product() {
		return r0190_product;
	}
	public void setR0190_product(String r0190_product) {
		this.r0190_product = r0190_product;
	}
	public BigDecimal getR0190_no_acct_aed_resident() {
		return r0190_no_acct_aed_resident;
	}
	public void setR0190_no_acct_aed_resident(BigDecimal r0190_no_acct_aed_resident) {
		this.r0190_no_acct_aed_resident = r0190_no_acct_aed_resident;
	}
	public BigDecimal getR0190_amount_aed_resident() {
		return r0190_amount_aed_resident;
	}
	public void setR0190_amount_aed_resident(BigDecimal r0190_amount_aed_resident) {
		this.r0190_amount_aed_resident = r0190_amount_aed_resident;
	}
	public BigDecimal getR0190_no_acct_fcy_resident() {
		return r0190_no_acct_fcy_resident;
	}
	public void setR0190_no_acct_fcy_resident(BigDecimal r0190_no_acct_fcy_resident) {
		this.r0190_no_acct_fcy_resident = r0190_no_acct_fcy_resident;
	}
	public BigDecimal getR0190_amount_fcy_resident() {
		return r0190_amount_fcy_resident;
	}
	public void setR0190_amount_fcy_resident(BigDecimal r0190_amount_fcy_resident) {
		this.r0190_amount_fcy_resident = r0190_amount_fcy_resident;
	}
	public BigDecimal getR0190_no_acct_aed_non_resident() {
		return r0190_no_acct_aed_non_resident;
	}
	public void setR0190_no_acct_aed_non_resident(BigDecimal r0190_no_acct_aed_non_resident) {
		this.r0190_no_acct_aed_non_resident = r0190_no_acct_aed_non_resident;
	}
	public BigDecimal getR0190_amount_aed_non_resident() {
		return r0190_amount_aed_non_resident;
	}
	public void setR0190_amount_aed_non_resident(BigDecimal r0190_amount_aed_non_resident) {
		this.r0190_amount_aed_non_resident = r0190_amount_aed_non_resident;
	}
	public BigDecimal getR0190_no_acct_fcy_non_resident() {
		return r0190_no_acct_fcy_non_resident;
	}
	public void setR0190_no_acct_fcy_non_resident(BigDecimal r0190_no_acct_fcy_non_resident) {
		this.r0190_no_acct_fcy_non_resident = r0190_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0190_amount_fcy_non_resident() {
		return r0190_amount_fcy_non_resident;
	}
	public void setR0190_amount_fcy_non_resident(BigDecimal r0190_amount_fcy_non_resident) {
		this.r0190_amount_fcy_non_resident = r0190_amount_fcy_non_resident;
	}
	public BigDecimal getR0190_total_amount() {
		return r0190_total_amount;
	}
	public void setR0190_total_amount(BigDecimal r0190_total_amount) {
		this.r0190_total_amount = r0190_total_amount;
	}
	public String getR0200_product() {
		return r0200_product;
	}
	public void setR0200_product(String r0200_product) {
		this.r0200_product = r0200_product;
	}
	public BigDecimal getR0200_no_acct_aed_resident() {
		return r0200_no_acct_aed_resident;
	}
	public void setR0200_no_acct_aed_resident(BigDecimal r0200_no_acct_aed_resident) {
		this.r0200_no_acct_aed_resident = r0200_no_acct_aed_resident;
	}
	public BigDecimal getR0200_amount_aed_resident() {
		return r0200_amount_aed_resident;
	}
	public void setR0200_amount_aed_resident(BigDecimal r0200_amount_aed_resident) {
		this.r0200_amount_aed_resident = r0200_amount_aed_resident;
	}
	public BigDecimal getR0200_no_acct_fcy_resident() {
		return r0200_no_acct_fcy_resident;
	}
	public void setR0200_no_acct_fcy_resident(BigDecimal r0200_no_acct_fcy_resident) {
		this.r0200_no_acct_fcy_resident = r0200_no_acct_fcy_resident;
	}
	public BigDecimal getR0200_amount_fcy_resident() {
		return r0200_amount_fcy_resident;
	}
	public void setR0200_amount_fcy_resident(BigDecimal r0200_amount_fcy_resident) {
		this.r0200_amount_fcy_resident = r0200_amount_fcy_resident;
	}
	public BigDecimal getR0200_no_acct_aed_non_resident() {
		return r0200_no_acct_aed_non_resident;
	}
	public void setR0200_no_acct_aed_non_resident(BigDecimal r0200_no_acct_aed_non_resident) {
		this.r0200_no_acct_aed_non_resident = r0200_no_acct_aed_non_resident;
	}
	public BigDecimal getR0200_amount_aed_non_resident() {
		return r0200_amount_aed_non_resident;
	}
	public void setR0200_amount_aed_non_resident(BigDecimal r0200_amount_aed_non_resident) {
		this.r0200_amount_aed_non_resident = r0200_amount_aed_non_resident;
	}
	public BigDecimal getR0200_no_acct_fcy_non_resident() {
		return r0200_no_acct_fcy_non_resident;
	}
	public void setR0200_no_acct_fcy_non_resident(BigDecimal r0200_no_acct_fcy_non_resident) {
		this.r0200_no_acct_fcy_non_resident = r0200_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0200_amount_fcy_non_resident() {
		return r0200_amount_fcy_non_resident;
	}
	public void setR0200_amount_fcy_non_resident(BigDecimal r0200_amount_fcy_non_resident) {
		this.r0200_amount_fcy_non_resident = r0200_amount_fcy_non_resident;
	}
	public BigDecimal getR0200_total_amount() {
		return r0200_total_amount;
	}
	public void setR0200_total_amount(BigDecimal r0200_total_amount) {
		this.r0200_total_amount = r0200_total_amount;
	}
	public String getR0210_product() {
		return r0210_product;
	}
	public void setR0210_product(String r0210_product) {
		this.r0210_product = r0210_product;
	}
	public BigDecimal getR0210_no_acct_aed_resident() {
		return r0210_no_acct_aed_resident;
	}
	public void setR0210_no_acct_aed_resident(BigDecimal r0210_no_acct_aed_resident) {
		this.r0210_no_acct_aed_resident = r0210_no_acct_aed_resident;
	}
	public BigDecimal getR0210_amount_aed_resident() {
		return r0210_amount_aed_resident;
	}
	public void setR0210_amount_aed_resident(BigDecimal r0210_amount_aed_resident) {
		this.r0210_amount_aed_resident = r0210_amount_aed_resident;
	}
	public BigDecimal getR0210_no_acct_fcy_resident() {
		return r0210_no_acct_fcy_resident;
	}
	public void setR0210_no_acct_fcy_resident(BigDecimal r0210_no_acct_fcy_resident) {
		this.r0210_no_acct_fcy_resident = r0210_no_acct_fcy_resident;
	}
	public BigDecimal getR0210_amount_fcy_resident() {
		return r0210_amount_fcy_resident;
	}
	public void setR0210_amount_fcy_resident(BigDecimal r0210_amount_fcy_resident) {
		this.r0210_amount_fcy_resident = r0210_amount_fcy_resident;
	}
	public BigDecimal getR0210_no_acct_aed_non_resident() {
		return r0210_no_acct_aed_non_resident;
	}
	public void setR0210_no_acct_aed_non_resident(BigDecimal r0210_no_acct_aed_non_resident) {
		this.r0210_no_acct_aed_non_resident = r0210_no_acct_aed_non_resident;
	}
	public BigDecimal getR0210_amount_aed_non_resident() {
		return r0210_amount_aed_non_resident;
	}
	public void setR0210_amount_aed_non_resident(BigDecimal r0210_amount_aed_non_resident) {
		this.r0210_amount_aed_non_resident = r0210_amount_aed_non_resident;
	}
	public BigDecimal getR0210_no_acct_fcy_non_resident() {
		return r0210_no_acct_fcy_non_resident;
	}
	public void setR0210_no_acct_fcy_non_resident(BigDecimal r0210_no_acct_fcy_non_resident) {
		this.r0210_no_acct_fcy_non_resident = r0210_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0210_amount_fcy_non_resident() {
		return r0210_amount_fcy_non_resident;
	}
	public void setR0210_amount_fcy_non_resident(BigDecimal r0210_amount_fcy_non_resident) {
		this.r0210_amount_fcy_non_resident = r0210_amount_fcy_non_resident;
	}
	public BigDecimal getR0210_total_amount() {
		return r0210_total_amount;
	}
	public void setR0210_total_amount(BigDecimal r0210_total_amount) {
		this.r0210_total_amount = r0210_total_amount;
	}
	public String getR0220_product() {
		return r0220_product;
	}
	public void setR0220_product(String r0220_product) {
		this.r0220_product = r0220_product;
	}
	public BigDecimal getR0220_no_acct_aed_resident() {
		return r0220_no_acct_aed_resident;
	}
	public void setR0220_no_acct_aed_resident(BigDecimal r0220_no_acct_aed_resident) {
		this.r0220_no_acct_aed_resident = r0220_no_acct_aed_resident;
	}
	public BigDecimal getR0220_amount_aed_resident() {
		return r0220_amount_aed_resident;
	}
	public void setR0220_amount_aed_resident(BigDecimal r0220_amount_aed_resident) {
		this.r0220_amount_aed_resident = r0220_amount_aed_resident;
	}
	public BigDecimal getR0220_no_acct_fcy_resident() {
		return r0220_no_acct_fcy_resident;
	}
	public void setR0220_no_acct_fcy_resident(BigDecimal r0220_no_acct_fcy_resident) {
		this.r0220_no_acct_fcy_resident = r0220_no_acct_fcy_resident;
	}
	public BigDecimal getR0220_amount_fcy_resident() {
		return r0220_amount_fcy_resident;
	}
	public void setR0220_amount_fcy_resident(BigDecimal r0220_amount_fcy_resident) {
		this.r0220_amount_fcy_resident = r0220_amount_fcy_resident;
	}
	public BigDecimal getR0220_no_acct_aed_non_resident() {
		return r0220_no_acct_aed_non_resident;
	}
	public void setR0220_no_acct_aed_non_resident(BigDecimal r0220_no_acct_aed_non_resident) {
		this.r0220_no_acct_aed_non_resident = r0220_no_acct_aed_non_resident;
	}
	public BigDecimal getR0220_amount_aed_non_resident() {
		return r0220_amount_aed_non_resident;
	}
	public void setR0220_amount_aed_non_resident(BigDecimal r0220_amount_aed_non_resident) {
		this.r0220_amount_aed_non_resident = r0220_amount_aed_non_resident;
	}
	public BigDecimal getR0220_no_acct_fcy_non_resident() {
		return r0220_no_acct_fcy_non_resident;
	}
	public void setR0220_no_acct_fcy_non_resident(BigDecimal r0220_no_acct_fcy_non_resident) {
		this.r0220_no_acct_fcy_non_resident = r0220_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0220_amount_fcy_non_resident() {
		return r0220_amount_fcy_non_resident;
	}
	public void setR0220_amount_fcy_non_resident(BigDecimal r0220_amount_fcy_non_resident) {
		this.r0220_amount_fcy_non_resident = r0220_amount_fcy_non_resident;
	}
	public BigDecimal getR0220_total_amount() {
		return r0220_total_amount;
	}
	public void setR0220_total_amount(BigDecimal r0220_total_amount) {
		this.r0220_total_amount = r0220_total_amount;
	}
	public String getR0230_product() {
		return r0230_product;
	}
	public void setR0230_product(String r0230_product) {
		this.r0230_product = r0230_product;
	}
	public BigDecimal getR0230_no_acct_aed_resident() {
		return r0230_no_acct_aed_resident;
	}
	public void setR0230_no_acct_aed_resident(BigDecimal r0230_no_acct_aed_resident) {
		this.r0230_no_acct_aed_resident = r0230_no_acct_aed_resident;
	}
	public BigDecimal getR0230_amount_aed_resident() {
		return r0230_amount_aed_resident;
	}
	public void setR0230_amount_aed_resident(BigDecimal r0230_amount_aed_resident) {
		this.r0230_amount_aed_resident = r0230_amount_aed_resident;
	}
	public BigDecimal getR0230_no_acct_fcy_resident() {
		return r0230_no_acct_fcy_resident;
	}
	public void setR0230_no_acct_fcy_resident(BigDecimal r0230_no_acct_fcy_resident) {
		this.r0230_no_acct_fcy_resident = r0230_no_acct_fcy_resident;
	}
	public BigDecimal getR0230_amount_fcy_resident() {
		return r0230_amount_fcy_resident;
	}
	public void setR0230_amount_fcy_resident(BigDecimal r0230_amount_fcy_resident) {
		this.r0230_amount_fcy_resident = r0230_amount_fcy_resident;
	}
	public BigDecimal getR0230_no_acct_aed_non_resident() {
		return r0230_no_acct_aed_non_resident;
	}
	public void setR0230_no_acct_aed_non_resident(BigDecimal r0230_no_acct_aed_non_resident) {
		this.r0230_no_acct_aed_non_resident = r0230_no_acct_aed_non_resident;
	}
	public BigDecimal getR0230_amount_aed_non_resident() {
		return r0230_amount_aed_non_resident;
	}
	public void setR0230_amount_aed_non_resident(BigDecimal r0230_amount_aed_non_resident) {
		this.r0230_amount_aed_non_resident = r0230_amount_aed_non_resident;
	}
	public BigDecimal getR0230_no_acct_fcy_non_resident() {
		return r0230_no_acct_fcy_non_resident;
	}
	public void setR0230_no_acct_fcy_non_resident(BigDecimal r0230_no_acct_fcy_non_resident) {
		this.r0230_no_acct_fcy_non_resident = r0230_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0230_amount_fcy_non_resident() {
		return r0230_amount_fcy_non_resident;
	}
	public void setR0230_amount_fcy_non_resident(BigDecimal r0230_amount_fcy_non_resident) {
		this.r0230_amount_fcy_non_resident = r0230_amount_fcy_non_resident;
	}
	public BigDecimal getR0230_total_amount() {
		return r0230_total_amount;
	}
	public void setR0230_total_amount(BigDecimal r0230_total_amount) {
		this.r0230_total_amount = r0230_total_amount;
	}
	public String getR0240_product() {
		return r0240_product;
	}
	public void setR0240_product(String r0240_product) {
		this.r0240_product = r0240_product;
	}
	public BigDecimal getR0240_no_acct_aed_resident() {
		return r0240_no_acct_aed_resident;
	}
	public void setR0240_no_acct_aed_resident(BigDecimal r0240_no_acct_aed_resident) {
		this.r0240_no_acct_aed_resident = r0240_no_acct_aed_resident;
	}
	public BigDecimal getR0240_amount_aed_resident() {
		return r0240_amount_aed_resident;
	}
	public void setR0240_amount_aed_resident(BigDecimal r0240_amount_aed_resident) {
		this.r0240_amount_aed_resident = r0240_amount_aed_resident;
	}
	public BigDecimal getR0240_no_acct_fcy_resident() {
		return r0240_no_acct_fcy_resident;
	}
	public void setR0240_no_acct_fcy_resident(BigDecimal r0240_no_acct_fcy_resident) {
		this.r0240_no_acct_fcy_resident = r0240_no_acct_fcy_resident;
	}
	public BigDecimal getR0240_amount_fcy_resident() {
		return r0240_amount_fcy_resident;
	}
	public void setR0240_amount_fcy_resident(BigDecimal r0240_amount_fcy_resident) {
		this.r0240_amount_fcy_resident = r0240_amount_fcy_resident;
	}
	public BigDecimal getR0240_no_acct_aed_non_resident() {
		return r0240_no_acct_aed_non_resident;
	}
	public void setR0240_no_acct_aed_non_resident(BigDecimal r0240_no_acct_aed_non_resident) {
		this.r0240_no_acct_aed_non_resident = r0240_no_acct_aed_non_resident;
	}
	public BigDecimal getR0240_amount_aed_non_resident() {
		return r0240_amount_aed_non_resident;
	}
	public void setR0240_amount_aed_non_resident(BigDecimal r0240_amount_aed_non_resident) {
		this.r0240_amount_aed_non_resident = r0240_amount_aed_non_resident;
	}
	public BigDecimal getR0240_no_acct_fcy_non_resident() {
		return r0240_no_acct_fcy_non_resident;
	}
	public void setR0240_no_acct_fcy_non_resident(BigDecimal r0240_no_acct_fcy_non_resident) {
		this.r0240_no_acct_fcy_non_resident = r0240_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0240_amount_fcy_non_resident() {
		return r0240_amount_fcy_non_resident;
	}
	public void setR0240_amount_fcy_non_resident(BigDecimal r0240_amount_fcy_non_resident) {
		this.r0240_amount_fcy_non_resident = r0240_amount_fcy_non_resident;
	}
	public BigDecimal getR0240_total_amount() {
		return r0240_total_amount;
	}
	public void setR0240_total_amount(BigDecimal r0240_total_amount) {
		this.r0240_total_amount = r0240_total_amount;
	}
	public String getR0250_product() {
		return r0250_product;
	}
	public void setR0250_product(String r0250_product) {
		this.r0250_product = r0250_product;
	}
	public BigDecimal getR0250_no_acct_aed_resident() {
		return r0250_no_acct_aed_resident;
	}
	public void setR0250_no_acct_aed_resident(BigDecimal r0250_no_acct_aed_resident) {
		this.r0250_no_acct_aed_resident = r0250_no_acct_aed_resident;
	}
	public BigDecimal getR0250_amount_aed_resident() {
		return r0250_amount_aed_resident;
	}
	public void setR0250_amount_aed_resident(BigDecimal r0250_amount_aed_resident) {
		this.r0250_amount_aed_resident = r0250_amount_aed_resident;
	}
	public BigDecimal getR0250_no_acct_fcy_resident() {
		return r0250_no_acct_fcy_resident;
	}
	public void setR0250_no_acct_fcy_resident(BigDecimal r0250_no_acct_fcy_resident) {
		this.r0250_no_acct_fcy_resident = r0250_no_acct_fcy_resident;
	}
	public BigDecimal getR0250_amount_fcy_resident() {
		return r0250_amount_fcy_resident;
	}
	public void setR0250_amount_fcy_resident(BigDecimal r0250_amount_fcy_resident) {
		this.r0250_amount_fcy_resident = r0250_amount_fcy_resident;
	}
	public BigDecimal getR0250_no_acct_aed_non_resident() {
		return r0250_no_acct_aed_non_resident;
	}
	public void setR0250_no_acct_aed_non_resident(BigDecimal r0250_no_acct_aed_non_resident) {
		this.r0250_no_acct_aed_non_resident = r0250_no_acct_aed_non_resident;
	}
	public BigDecimal getR0250_amount_aed_non_resident() {
		return r0250_amount_aed_non_resident;
	}
	public void setR0250_amount_aed_non_resident(BigDecimal r0250_amount_aed_non_resident) {
		this.r0250_amount_aed_non_resident = r0250_amount_aed_non_resident;
	}
	public BigDecimal getR0250_no_acct_fcy_non_resident() {
		return r0250_no_acct_fcy_non_resident;
	}
	public void setR0250_no_acct_fcy_non_resident(BigDecimal r0250_no_acct_fcy_non_resident) {
		this.r0250_no_acct_fcy_non_resident = r0250_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0250_amount_fcy_non_resident() {
		return r0250_amount_fcy_non_resident;
	}
	public void setR0250_amount_fcy_non_resident(BigDecimal r0250_amount_fcy_non_resident) {
		this.r0250_amount_fcy_non_resident = r0250_amount_fcy_non_resident;
	}
	public BigDecimal getR0250_total_amount() {
		return r0250_total_amount;
	}
	public void setR0250_total_amount(BigDecimal r0250_total_amount) {
		this.r0250_total_amount = r0250_total_amount;
	}
	public String getR0260_product() {
		return r0260_product;
	}
	public void setR0260_product(String r0260_product) {
		this.r0260_product = r0260_product;
	}
	public BigDecimal getR0260_no_acct_aed_resident() {
		return r0260_no_acct_aed_resident;
	}
	public void setR0260_no_acct_aed_resident(BigDecimal r0260_no_acct_aed_resident) {
		this.r0260_no_acct_aed_resident = r0260_no_acct_aed_resident;
	}
	public BigDecimal getR0260_amount_aed_resident() {
		return r0260_amount_aed_resident;
	}
	public void setR0260_amount_aed_resident(BigDecimal r0260_amount_aed_resident) {
		this.r0260_amount_aed_resident = r0260_amount_aed_resident;
	}
	public BigDecimal getR0260_no_acct_fcy_resident() {
		return r0260_no_acct_fcy_resident;
	}
	public void setR0260_no_acct_fcy_resident(BigDecimal r0260_no_acct_fcy_resident) {
		this.r0260_no_acct_fcy_resident = r0260_no_acct_fcy_resident;
	}
	public BigDecimal getR0260_amount_fcy_resident() {
		return r0260_amount_fcy_resident;
	}
	public void setR0260_amount_fcy_resident(BigDecimal r0260_amount_fcy_resident) {
		this.r0260_amount_fcy_resident = r0260_amount_fcy_resident;
	}
	public BigDecimal getR0260_no_acct_aed_non_resident() {
		return r0260_no_acct_aed_non_resident;
	}
	public void setR0260_no_acct_aed_non_resident(BigDecimal r0260_no_acct_aed_non_resident) {
		this.r0260_no_acct_aed_non_resident = r0260_no_acct_aed_non_resident;
	}
	public BigDecimal getR0260_amount_aed_non_resident() {
		return r0260_amount_aed_non_resident;
	}
	public void setR0260_amount_aed_non_resident(BigDecimal r0260_amount_aed_non_resident) {
		this.r0260_amount_aed_non_resident = r0260_amount_aed_non_resident;
	}
	public BigDecimal getR0260_no_acct_fcy_non_resident() {
		return r0260_no_acct_fcy_non_resident;
	}
	public void setR0260_no_acct_fcy_non_resident(BigDecimal r0260_no_acct_fcy_non_resident) {
		this.r0260_no_acct_fcy_non_resident = r0260_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0260_amount_fcy_non_resident() {
		return r0260_amount_fcy_non_resident;
	}
	public void setR0260_amount_fcy_non_resident(BigDecimal r0260_amount_fcy_non_resident) {
		this.r0260_amount_fcy_non_resident = r0260_amount_fcy_non_resident;
	}
	public BigDecimal getR0260_total_amount() {
		return r0260_total_amount;
	}
	public void setR0260_total_amount(BigDecimal r0260_total_amount) {
		this.r0260_total_amount = r0260_total_amount;
	}
	public String getR0270_product() {
		return r0270_product;
	}
	public void setR0270_product(String r0270_product) {
		this.r0270_product = r0270_product;
	}
	public BigDecimal getR0270_no_acct_aed_resident() {
		return r0270_no_acct_aed_resident;
	}
	public void setR0270_no_acct_aed_resident(BigDecimal r0270_no_acct_aed_resident) {
		this.r0270_no_acct_aed_resident = r0270_no_acct_aed_resident;
	}
	public BigDecimal getR0270_amount_aed_resident() {
		return r0270_amount_aed_resident;
	}
	public void setR0270_amount_aed_resident(BigDecimal r0270_amount_aed_resident) {
		this.r0270_amount_aed_resident = r0270_amount_aed_resident;
	}
	public BigDecimal getR0270_no_acct_fcy_resident() {
		return r0270_no_acct_fcy_resident;
	}
	public void setR0270_no_acct_fcy_resident(BigDecimal r0270_no_acct_fcy_resident) {
		this.r0270_no_acct_fcy_resident = r0270_no_acct_fcy_resident;
	}
	public BigDecimal getR0270_amount_fcy_resident() {
		return r0270_amount_fcy_resident;
	}
	public void setR0270_amount_fcy_resident(BigDecimal r0270_amount_fcy_resident) {
		this.r0270_amount_fcy_resident = r0270_amount_fcy_resident;
	}
	public BigDecimal getR0270_no_acct_aed_non_resident() {
		return r0270_no_acct_aed_non_resident;
	}
	public void setR0270_no_acct_aed_non_resident(BigDecimal r0270_no_acct_aed_non_resident) {
		this.r0270_no_acct_aed_non_resident = r0270_no_acct_aed_non_resident;
	}
	public BigDecimal getR0270_amount_aed_non_resident() {
		return r0270_amount_aed_non_resident;
	}
	public void setR0270_amount_aed_non_resident(BigDecimal r0270_amount_aed_non_resident) {
		this.r0270_amount_aed_non_resident = r0270_amount_aed_non_resident;
	}
	public BigDecimal getR0270_no_acct_fcy_non_resident() {
		return r0270_no_acct_fcy_non_resident;
	}
	public void setR0270_no_acct_fcy_non_resident(BigDecimal r0270_no_acct_fcy_non_resident) {
		this.r0270_no_acct_fcy_non_resident = r0270_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0270_amount_fcy_non_resident() {
		return r0270_amount_fcy_non_resident;
	}
	public void setR0270_amount_fcy_non_resident(BigDecimal r0270_amount_fcy_non_resident) {
		this.r0270_amount_fcy_non_resident = r0270_amount_fcy_non_resident;
	}
	public BigDecimal getR0270_total_amount() {
		return r0270_total_amount;
	}
	public void setR0270_total_amount(BigDecimal r0270_total_amount) {
		this.r0270_total_amount = r0270_total_amount;
	}
	public String getR0280_product() {
		return r0280_product;
	}
	public void setR0280_product(String r0280_product) {
		this.r0280_product = r0280_product;
	}
	public BigDecimal getR0280_no_acct_aed_resident() {
		return r0280_no_acct_aed_resident;
	}
	public void setR0280_no_acct_aed_resident(BigDecimal r0280_no_acct_aed_resident) {
		this.r0280_no_acct_aed_resident = r0280_no_acct_aed_resident;
	}
	public BigDecimal getR0280_amount_aed_resident() {
		return r0280_amount_aed_resident;
	}
	public void setR0280_amount_aed_resident(BigDecimal r0280_amount_aed_resident) {
		this.r0280_amount_aed_resident = r0280_amount_aed_resident;
	}
	public BigDecimal getR0280_no_acct_fcy_resident() {
		return r0280_no_acct_fcy_resident;
	}
	public void setR0280_no_acct_fcy_resident(BigDecimal r0280_no_acct_fcy_resident) {
		this.r0280_no_acct_fcy_resident = r0280_no_acct_fcy_resident;
	}
	public BigDecimal getR0280_amount_fcy_resident() {
		return r0280_amount_fcy_resident;
	}
	public void setR0280_amount_fcy_resident(BigDecimal r0280_amount_fcy_resident) {
		this.r0280_amount_fcy_resident = r0280_amount_fcy_resident;
	}
	public BigDecimal getR0280_no_acct_aed_non_resident() {
		return r0280_no_acct_aed_non_resident;
	}
	public void setR0280_no_acct_aed_non_resident(BigDecimal r0280_no_acct_aed_non_resident) {
		this.r0280_no_acct_aed_non_resident = r0280_no_acct_aed_non_resident;
	}
	public BigDecimal getR0280_amount_aed_non_resident() {
		return r0280_amount_aed_non_resident;
	}
	public void setR0280_amount_aed_non_resident(BigDecimal r0280_amount_aed_non_resident) {
		this.r0280_amount_aed_non_resident = r0280_amount_aed_non_resident;
	}
	public BigDecimal getR0280_no_acct_fcy_non_resident() {
		return r0280_no_acct_fcy_non_resident;
	}
	public void setR0280_no_acct_fcy_non_resident(BigDecimal r0280_no_acct_fcy_non_resident) {
		this.r0280_no_acct_fcy_non_resident = r0280_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0280_amount_fcy_non_resident() {
		return r0280_amount_fcy_non_resident;
	}
	public void setR0280_amount_fcy_non_resident(BigDecimal r0280_amount_fcy_non_resident) {
		this.r0280_amount_fcy_non_resident = r0280_amount_fcy_non_resident;
	}
	public BigDecimal getR0280_total_amount() {
		return r0280_total_amount;
	}
	public void setR0280_total_amount(BigDecimal r0280_total_amount) {
		this.r0280_total_amount = r0280_total_amount;
	}
	public String getR0290_product() {
		return r0290_product;
	}
	public void setR0290_product(String r0290_product) {
		this.r0290_product = r0290_product;
	}
	public BigDecimal getR0290_no_acct_aed_resident() {
		return r0290_no_acct_aed_resident;
	}
	public void setR0290_no_acct_aed_resident(BigDecimal r0290_no_acct_aed_resident) {
		this.r0290_no_acct_aed_resident = r0290_no_acct_aed_resident;
	}
	public BigDecimal getR0290_amount_aed_resident() {
		return r0290_amount_aed_resident;
	}
	public void setR0290_amount_aed_resident(BigDecimal r0290_amount_aed_resident) {
		this.r0290_amount_aed_resident = r0290_amount_aed_resident;
	}
	public BigDecimal getR0290_no_acct_fcy_resident() {
		return r0290_no_acct_fcy_resident;
	}
	public void setR0290_no_acct_fcy_resident(BigDecimal r0290_no_acct_fcy_resident) {
		this.r0290_no_acct_fcy_resident = r0290_no_acct_fcy_resident;
	}
	public BigDecimal getR0290_amount_fcy_resident() {
		return r0290_amount_fcy_resident;
	}
	public void setR0290_amount_fcy_resident(BigDecimal r0290_amount_fcy_resident) {
		this.r0290_amount_fcy_resident = r0290_amount_fcy_resident;
	}
	public BigDecimal getR0290_no_acct_aed_non_resident() {
		return r0290_no_acct_aed_non_resident;
	}
	public void setR0290_no_acct_aed_non_resident(BigDecimal r0290_no_acct_aed_non_resident) {
		this.r0290_no_acct_aed_non_resident = r0290_no_acct_aed_non_resident;
	}
	public BigDecimal getR0290_amount_aed_non_resident() {
		return r0290_amount_aed_non_resident;
	}
	public void setR0290_amount_aed_non_resident(BigDecimal r0290_amount_aed_non_resident) {
		this.r0290_amount_aed_non_resident = r0290_amount_aed_non_resident;
	}
	public BigDecimal getR0290_no_acct_fcy_non_resident() {
		return r0290_no_acct_fcy_non_resident;
	}
	public void setR0290_no_acct_fcy_non_resident(BigDecimal r0290_no_acct_fcy_non_resident) {
		this.r0290_no_acct_fcy_non_resident = r0290_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0290_amount_fcy_non_resident() {
		return r0290_amount_fcy_non_resident;
	}
	public void setR0290_amount_fcy_non_resident(BigDecimal r0290_amount_fcy_non_resident) {
		this.r0290_amount_fcy_non_resident = r0290_amount_fcy_non_resident;
	}
	public BigDecimal getR0290_total_amount() {
		return r0290_total_amount;
	}
	public void setR0290_total_amount(BigDecimal r0290_total_amount) {
		this.r0290_total_amount = r0290_total_amount;
	}
	public String getR0300_product() {
		return r0300_product;
	}
	public void setR0300_product(String r0300_product) {
		this.r0300_product = r0300_product;
	}
	public BigDecimal getR0300_no_acct_aed_resident() {
		return r0300_no_acct_aed_resident;
	}
	public void setR0300_no_acct_aed_resident(BigDecimal r0300_no_acct_aed_resident) {
		this.r0300_no_acct_aed_resident = r0300_no_acct_aed_resident;
	}
	public BigDecimal getR0300_amount_aed_resident() {
		return r0300_amount_aed_resident;
	}
	public void setR0300_amount_aed_resident(BigDecimal r0300_amount_aed_resident) {
		this.r0300_amount_aed_resident = r0300_amount_aed_resident;
	}
	public BigDecimal getR0300_no_acct_fcy_resident() {
		return r0300_no_acct_fcy_resident;
	}
	public void setR0300_no_acct_fcy_resident(BigDecimal r0300_no_acct_fcy_resident) {
		this.r0300_no_acct_fcy_resident = r0300_no_acct_fcy_resident;
	}
	public BigDecimal getR0300_amount_fcy_resident() {
		return r0300_amount_fcy_resident;
	}
	public void setR0300_amount_fcy_resident(BigDecimal r0300_amount_fcy_resident) {
		this.r0300_amount_fcy_resident = r0300_amount_fcy_resident;
	}
	public BigDecimal getR0300_no_acct_aed_non_resident() {
		return r0300_no_acct_aed_non_resident;
	}
	public void setR0300_no_acct_aed_non_resident(BigDecimal r0300_no_acct_aed_non_resident) {
		this.r0300_no_acct_aed_non_resident = r0300_no_acct_aed_non_resident;
	}
	public BigDecimal getR0300_amount_aed_non_resident() {
		return r0300_amount_aed_non_resident;
	}
	public void setR0300_amount_aed_non_resident(BigDecimal r0300_amount_aed_non_resident) {
		this.r0300_amount_aed_non_resident = r0300_amount_aed_non_resident;
	}
	public BigDecimal getR0300_no_acct_fcy_non_resident() {
		return r0300_no_acct_fcy_non_resident;
	}
	public void setR0300_no_acct_fcy_non_resident(BigDecimal r0300_no_acct_fcy_non_resident) {
		this.r0300_no_acct_fcy_non_resident = r0300_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0300_amount_fcy_non_resident() {
		return r0300_amount_fcy_non_resident;
	}
	public void setR0300_amount_fcy_non_resident(BigDecimal r0300_amount_fcy_non_resident) {
		this.r0300_amount_fcy_non_resident = r0300_amount_fcy_non_resident;
	}
	public BigDecimal getR0300_total_amount() {
		return r0300_total_amount;
	}
	public void setR0300_total_amount(BigDecimal r0300_total_amount) {
		this.r0300_total_amount = r0300_total_amount;
	}
	public String getR0310_product() {
		return r0310_product;
	}
	public void setR0310_product(String r0310_product) {
		this.r0310_product = r0310_product;
	}
	public BigDecimal getR0310_no_acct_aed_resident() {
		return r0310_no_acct_aed_resident;
	}
	public void setR0310_no_acct_aed_resident(BigDecimal r0310_no_acct_aed_resident) {
		this.r0310_no_acct_aed_resident = r0310_no_acct_aed_resident;
	}
	public BigDecimal getR0310_amount_aed_resident() {
		return r0310_amount_aed_resident;
	}
	public void setR0310_amount_aed_resident(BigDecimal r0310_amount_aed_resident) {
		this.r0310_amount_aed_resident = r0310_amount_aed_resident;
	}
	public BigDecimal getR0310_no_acct_fcy_resident() {
		return r0310_no_acct_fcy_resident;
	}
	public void setR0310_no_acct_fcy_resident(BigDecimal r0310_no_acct_fcy_resident) {
		this.r0310_no_acct_fcy_resident = r0310_no_acct_fcy_resident;
	}
	public BigDecimal getR0310_amount_fcy_resident() {
		return r0310_amount_fcy_resident;
	}
	public void setR0310_amount_fcy_resident(BigDecimal r0310_amount_fcy_resident) {
		this.r0310_amount_fcy_resident = r0310_amount_fcy_resident;
	}
	public BigDecimal getR0310_no_acct_aed_non_resident() {
		return r0310_no_acct_aed_non_resident;
	}
	public void setR0310_no_acct_aed_non_resident(BigDecimal r0310_no_acct_aed_non_resident) {
		this.r0310_no_acct_aed_non_resident = r0310_no_acct_aed_non_resident;
	}
	public BigDecimal getR0310_amount_aed_non_resident() {
		return r0310_amount_aed_non_resident;
	}
	public void setR0310_amount_aed_non_resident(BigDecimal r0310_amount_aed_non_resident) {
		this.r0310_amount_aed_non_resident = r0310_amount_aed_non_resident;
	}
	public BigDecimal getR0310_no_acct_fcy_non_resident() {
		return r0310_no_acct_fcy_non_resident;
	}
	public void setR0310_no_acct_fcy_non_resident(BigDecimal r0310_no_acct_fcy_non_resident) {
		this.r0310_no_acct_fcy_non_resident = r0310_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0310_amount_fcy_non_resident() {
		return r0310_amount_fcy_non_resident;
	}
	public void setR0310_amount_fcy_non_resident(BigDecimal r0310_amount_fcy_non_resident) {
		this.r0310_amount_fcy_non_resident = r0310_amount_fcy_non_resident;
	}
	public BigDecimal getR0310_total_amount() {
		return r0310_total_amount;
	}
	public void setR0310_total_amount(BigDecimal r0310_total_amount) {
		this.r0310_total_amount = r0310_total_amount;
	}
	public String getR0320_product() {
		return r0320_product;
	}
	public void setR0320_product(String r0320_product) {
		this.r0320_product = r0320_product;
	}
	public BigDecimal getR0320_no_acct_aed_resident() {
		return r0320_no_acct_aed_resident;
	}
	public void setR0320_no_acct_aed_resident(BigDecimal r0320_no_acct_aed_resident) {
		this.r0320_no_acct_aed_resident = r0320_no_acct_aed_resident;
	}
	public BigDecimal getR0320_amount_aed_resident() {
		return r0320_amount_aed_resident;
	}
	public void setR0320_amount_aed_resident(BigDecimal r0320_amount_aed_resident) {
		this.r0320_amount_aed_resident = r0320_amount_aed_resident;
	}
	public BigDecimal getR0320_no_acct_fcy_resident() {
		return r0320_no_acct_fcy_resident;
	}
	public void setR0320_no_acct_fcy_resident(BigDecimal r0320_no_acct_fcy_resident) {
		this.r0320_no_acct_fcy_resident = r0320_no_acct_fcy_resident;
	}
	public BigDecimal getR0320_amount_fcy_resident() {
		return r0320_amount_fcy_resident;
	}
	public void setR0320_amount_fcy_resident(BigDecimal r0320_amount_fcy_resident) {
		this.r0320_amount_fcy_resident = r0320_amount_fcy_resident;
	}
	public BigDecimal getR0320_no_acct_aed_non_resident() {
		return r0320_no_acct_aed_non_resident;
	}
	public void setR0320_no_acct_aed_non_resident(BigDecimal r0320_no_acct_aed_non_resident) {
		this.r0320_no_acct_aed_non_resident = r0320_no_acct_aed_non_resident;
	}
	public BigDecimal getR0320_amount_aed_non_resident() {
		return r0320_amount_aed_non_resident;
	}
	public void setR0320_amount_aed_non_resident(BigDecimal r0320_amount_aed_non_resident) {
		this.r0320_amount_aed_non_resident = r0320_amount_aed_non_resident;
	}
	public BigDecimal getR0320_no_acct_fcy_non_resident() {
		return r0320_no_acct_fcy_non_resident;
	}
	public void setR0320_no_acct_fcy_non_resident(BigDecimal r0320_no_acct_fcy_non_resident) {
		this.r0320_no_acct_fcy_non_resident = r0320_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0320_amount_fcy_non_resident() {
		return r0320_amount_fcy_non_resident;
	}
	public void setR0320_amount_fcy_non_resident(BigDecimal r0320_amount_fcy_non_resident) {
		this.r0320_amount_fcy_non_resident = r0320_amount_fcy_non_resident;
	}
	public BigDecimal getR0320_total_amount() {
		return r0320_total_amount;
	}
	public void setR0320_total_amount(BigDecimal r0320_total_amount) {
		this.r0320_total_amount = r0320_total_amount;
	}
	public String getR0330_product() {
		return r0330_product;
	}
	public void setR0330_product(String r0330_product) {
		this.r0330_product = r0330_product;
	}
	public BigDecimal getR0330_no_acct_aed_resident() {
		return r0330_no_acct_aed_resident;
	}
	public void setR0330_no_acct_aed_resident(BigDecimal r0330_no_acct_aed_resident) {
		this.r0330_no_acct_aed_resident = r0330_no_acct_aed_resident;
	}
	public BigDecimal getR0330_amount_aed_resident() {
		return r0330_amount_aed_resident;
	}
	public void setR0330_amount_aed_resident(BigDecimal r0330_amount_aed_resident) {
		this.r0330_amount_aed_resident = r0330_amount_aed_resident;
	}
	public BigDecimal getR0330_no_acct_fcy_resident() {
		return r0330_no_acct_fcy_resident;
	}
	public void setR0330_no_acct_fcy_resident(BigDecimal r0330_no_acct_fcy_resident) {
		this.r0330_no_acct_fcy_resident = r0330_no_acct_fcy_resident;
	}
	public BigDecimal getR0330_amount_fcy_resident() {
		return r0330_amount_fcy_resident;
	}
	public void setR0330_amount_fcy_resident(BigDecimal r0330_amount_fcy_resident) {
		this.r0330_amount_fcy_resident = r0330_amount_fcy_resident;
	}
	public BigDecimal getR0330_no_acct_aed_non_resident() {
		return r0330_no_acct_aed_non_resident;
	}
	public void setR0330_no_acct_aed_non_resident(BigDecimal r0330_no_acct_aed_non_resident) {
		this.r0330_no_acct_aed_non_resident = r0330_no_acct_aed_non_resident;
	}
	public BigDecimal getR0330_amount_aed_non_resident() {
		return r0330_amount_aed_non_resident;
	}
	public void setR0330_amount_aed_non_resident(BigDecimal r0330_amount_aed_non_resident) {
		this.r0330_amount_aed_non_resident = r0330_amount_aed_non_resident;
	}
	public BigDecimal getR0330_no_acct_fcy_non_resident() {
		return r0330_no_acct_fcy_non_resident;
	}
	public void setR0330_no_acct_fcy_non_resident(BigDecimal r0330_no_acct_fcy_non_resident) {
		this.r0330_no_acct_fcy_non_resident = r0330_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0330_amount_fcy_non_resident() {
		return r0330_amount_fcy_non_resident;
	}
	public void setR0330_amount_fcy_non_resident(BigDecimal r0330_amount_fcy_non_resident) {
		this.r0330_amount_fcy_non_resident = r0330_amount_fcy_non_resident;
	}
	public BigDecimal getR0330_total_amount() {
		return r0330_total_amount;
	}
	public void setR0330_total_amount(BigDecimal r0330_total_amount) {
		this.r0330_total_amount = r0330_total_amount;
	}
	public String getR0340_product() {
		return r0340_product;
	}
	public void setR0340_product(String r0340_product) {
		this.r0340_product = r0340_product;
	}
	public BigDecimal getR0340_no_acct_aed_resident() {
		return r0340_no_acct_aed_resident;
	}
	public void setR0340_no_acct_aed_resident(BigDecimal r0340_no_acct_aed_resident) {
		this.r0340_no_acct_aed_resident = r0340_no_acct_aed_resident;
	}
	public BigDecimal getR0340_amount_aed_resident() {
		return r0340_amount_aed_resident;
	}
	public void setR0340_amount_aed_resident(BigDecimal r0340_amount_aed_resident) {
		this.r0340_amount_aed_resident = r0340_amount_aed_resident;
	}
	public BigDecimal getR0340_no_acct_fcy_resident() {
		return r0340_no_acct_fcy_resident;
	}
	public void setR0340_no_acct_fcy_resident(BigDecimal r0340_no_acct_fcy_resident) {
		this.r0340_no_acct_fcy_resident = r0340_no_acct_fcy_resident;
	}
	public BigDecimal getR0340_amount_fcy_resident() {
		return r0340_amount_fcy_resident;
	}
	public void setR0340_amount_fcy_resident(BigDecimal r0340_amount_fcy_resident) {
		this.r0340_amount_fcy_resident = r0340_amount_fcy_resident;
	}
	public BigDecimal getR0340_no_acct_aed_non_resident() {
		return r0340_no_acct_aed_non_resident;
	}
	public void setR0340_no_acct_aed_non_resident(BigDecimal r0340_no_acct_aed_non_resident) {
		this.r0340_no_acct_aed_non_resident = r0340_no_acct_aed_non_resident;
	}
	public BigDecimal getR0340_amount_aed_non_resident() {
		return r0340_amount_aed_non_resident;
	}
	public void setR0340_amount_aed_non_resident(BigDecimal r0340_amount_aed_non_resident) {
		this.r0340_amount_aed_non_resident = r0340_amount_aed_non_resident;
	}
	public BigDecimal getR0340_no_acct_fcy_non_resident() {
		return r0340_no_acct_fcy_non_resident;
	}
	public void setR0340_no_acct_fcy_non_resident(BigDecimal r0340_no_acct_fcy_non_resident) {
		this.r0340_no_acct_fcy_non_resident = r0340_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0340_amount_fcy_non_resident() {
		return r0340_amount_fcy_non_resident;
	}
	public void setR0340_amount_fcy_non_resident(BigDecimal r0340_amount_fcy_non_resident) {
		this.r0340_amount_fcy_non_resident = r0340_amount_fcy_non_resident;
	}
	public BigDecimal getR0340_total_amount() {
		return r0340_total_amount;
	}
	public void setR0340_total_amount(BigDecimal r0340_total_amount) {
		this.r0340_total_amount = r0340_total_amount;
	}
	public String getR0350_product() {
		return r0350_product;
	}
	public void setR0350_product(String r0350_product) {
		this.r0350_product = r0350_product;
	}
	public BigDecimal getR0350_no_acct_aed_resident() {
		return r0350_no_acct_aed_resident;
	}
	public void setR0350_no_acct_aed_resident(BigDecimal r0350_no_acct_aed_resident) {
		this.r0350_no_acct_aed_resident = r0350_no_acct_aed_resident;
	}
	public BigDecimal getR0350_amount_aed_resident() {
		return r0350_amount_aed_resident;
	}
	public void setR0350_amount_aed_resident(BigDecimal r0350_amount_aed_resident) {
		this.r0350_amount_aed_resident = r0350_amount_aed_resident;
	}
	public BigDecimal getR0350_no_acct_fcy_resident() {
		return r0350_no_acct_fcy_resident;
	}
	public void setR0350_no_acct_fcy_resident(BigDecimal r0350_no_acct_fcy_resident) {
		this.r0350_no_acct_fcy_resident = r0350_no_acct_fcy_resident;
	}
	public BigDecimal getR0350_amount_fcy_resident() {
		return r0350_amount_fcy_resident;
	}
	public void setR0350_amount_fcy_resident(BigDecimal r0350_amount_fcy_resident) {
		this.r0350_amount_fcy_resident = r0350_amount_fcy_resident;
	}
	public BigDecimal getR0350_no_acct_aed_non_resident() {
		return r0350_no_acct_aed_non_resident;
	}
	public void setR0350_no_acct_aed_non_resident(BigDecimal r0350_no_acct_aed_non_resident) {
		this.r0350_no_acct_aed_non_resident = r0350_no_acct_aed_non_resident;
	}
	public BigDecimal getR0350_amount_aed_non_resident() {
		return r0350_amount_aed_non_resident;
	}
	public void setR0350_amount_aed_non_resident(BigDecimal r0350_amount_aed_non_resident) {
		this.r0350_amount_aed_non_resident = r0350_amount_aed_non_resident;
	}
	public BigDecimal getR0350_no_acct_fcy_non_resident() {
		return r0350_no_acct_fcy_non_resident;
	}
	public void setR0350_no_acct_fcy_non_resident(BigDecimal r0350_no_acct_fcy_non_resident) {
		this.r0350_no_acct_fcy_non_resident = r0350_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0350_amount_fcy_non_resident() {
		return r0350_amount_fcy_non_resident;
	}
	public void setR0350_amount_fcy_non_resident(BigDecimal r0350_amount_fcy_non_resident) {
		this.r0350_amount_fcy_non_resident = r0350_amount_fcy_non_resident;
	}
	public BigDecimal getR0350_total_amount() {
		return r0350_total_amount;
	}
	public void setR0350_total_amount(BigDecimal r0350_total_amount) {
		this.r0350_total_amount = r0350_total_amount;
	}
	public String getR0360_product() {
		return r0360_product;
	}
	public void setR0360_product(String r0360_product) {
		this.r0360_product = r0360_product;
	}
	public BigDecimal getR0360_no_acct_aed_resident() {
		return r0360_no_acct_aed_resident;
	}
	public void setR0360_no_acct_aed_resident(BigDecimal r0360_no_acct_aed_resident) {
		this.r0360_no_acct_aed_resident = r0360_no_acct_aed_resident;
	}
	public BigDecimal getR0360_amount_aed_resident() {
		return r0360_amount_aed_resident;
	}
	public void setR0360_amount_aed_resident(BigDecimal r0360_amount_aed_resident) {
		this.r0360_amount_aed_resident = r0360_amount_aed_resident;
	}
	public BigDecimal getR0360_no_acct_fcy_resident() {
		return r0360_no_acct_fcy_resident;
	}
	public void setR0360_no_acct_fcy_resident(BigDecimal r0360_no_acct_fcy_resident) {
		this.r0360_no_acct_fcy_resident = r0360_no_acct_fcy_resident;
	}
	public BigDecimal getR0360_amount_fcy_resident() {
		return r0360_amount_fcy_resident;
	}
	public void setR0360_amount_fcy_resident(BigDecimal r0360_amount_fcy_resident) {
		this.r0360_amount_fcy_resident = r0360_amount_fcy_resident;
	}
	public BigDecimal getR0360_no_acct_aed_non_resident() {
		return r0360_no_acct_aed_non_resident;
	}
	public void setR0360_no_acct_aed_non_resident(BigDecimal r0360_no_acct_aed_non_resident) {
		this.r0360_no_acct_aed_non_resident = r0360_no_acct_aed_non_resident;
	}
	public BigDecimal getR0360_amount_aed_non_resident() {
		return r0360_amount_aed_non_resident;
	}
	public void setR0360_amount_aed_non_resident(BigDecimal r0360_amount_aed_non_resident) {
		this.r0360_amount_aed_non_resident = r0360_amount_aed_non_resident;
	}
	public BigDecimal getR0360_no_acct_fcy_non_resident() {
		return r0360_no_acct_fcy_non_resident;
	}
	public void setR0360_no_acct_fcy_non_resident(BigDecimal r0360_no_acct_fcy_non_resident) {
		this.r0360_no_acct_fcy_non_resident = r0360_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0360_amount_fcy_non_resident() {
		return r0360_amount_fcy_non_resident;
	}
	public void setR0360_amount_fcy_non_resident(BigDecimal r0360_amount_fcy_non_resident) {
		this.r0360_amount_fcy_non_resident = r0360_amount_fcy_non_resident;
	}
	public BigDecimal getR0360_total_amount() {
		return r0360_total_amount;
	}
	public void setR0360_total_amount(BigDecimal r0360_total_amount) {
		this.r0360_total_amount = r0360_total_amount;
	}
	public String getR0370_product() {
		return r0370_product;
	}
	public void setR0370_product(String r0370_product) {
		this.r0370_product = r0370_product;
	}
	public BigDecimal getR0370_no_acct_aed_resident() {
		return r0370_no_acct_aed_resident;
	}
	public void setR0370_no_acct_aed_resident(BigDecimal r0370_no_acct_aed_resident) {
		this.r0370_no_acct_aed_resident = r0370_no_acct_aed_resident;
	}
	public BigDecimal getR0370_amount_aed_resident() {
		return r0370_amount_aed_resident;
	}
	public void setR0370_amount_aed_resident(BigDecimal r0370_amount_aed_resident) {
		this.r0370_amount_aed_resident = r0370_amount_aed_resident;
	}
	public BigDecimal getR0370_no_acct_fcy_resident() {
		return r0370_no_acct_fcy_resident;
	}
	public void setR0370_no_acct_fcy_resident(BigDecimal r0370_no_acct_fcy_resident) {
		this.r0370_no_acct_fcy_resident = r0370_no_acct_fcy_resident;
	}
	public BigDecimal getR0370_amount_fcy_resident() {
		return r0370_amount_fcy_resident;
	}
	public void setR0370_amount_fcy_resident(BigDecimal r0370_amount_fcy_resident) {
		this.r0370_amount_fcy_resident = r0370_amount_fcy_resident;
	}
	public BigDecimal getR0370_no_acct_aed_non_resident() {
		return r0370_no_acct_aed_non_resident;
	}
	public void setR0370_no_acct_aed_non_resident(BigDecimal r0370_no_acct_aed_non_resident) {
		this.r0370_no_acct_aed_non_resident = r0370_no_acct_aed_non_resident;
	}
	public BigDecimal getR0370_amount_aed_non_resident() {
		return r0370_amount_aed_non_resident;
	}
	public void setR0370_amount_aed_non_resident(BigDecimal r0370_amount_aed_non_resident) {
		this.r0370_amount_aed_non_resident = r0370_amount_aed_non_resident;
	}
	public BigDecimal getR0370_no_acct_fcy_non_resident() {
		return r0370_no_acct_fcy_non_resident;
	}
	public void setR0370_no_acct_fcy_non_resident(BigDecimal r0370_no_acct_fcy_non_resident) {
		this.r0370_no_acct_fcy_non_resident = r0370_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0370_amount_fcy_non_resident() {
		return r0370_amount_fcy_non_resident;
	}
	public void setR0370_amount_fcy_non_resident(BigDecimal r0370_amount_fcy_non_resident) {
		this.r0370_amount_fcy_non_resident = r0370_amount_fcy_non_resident;
	}
	public BigDecimal getR0370_total_amount() {
		return r0370_total_amount;
	}
	public void setR0370_total_amount(BigDecimal r0370_total_amount) {
		this.r0370_total_amount = r0370_total_amount;
	}
	public String getR0380_product() {
		return r0380_product;
	}
	public void setR0380_product(String r0380_product) {
		this.r0380_product = r0380_product;
	}
	public BigDecimal getR0380_no_acct_aed_resident() {
		return r0380_no_acct_aed_resident;
	}
	public void setR0380_no_acct_aed_resident(BigDecimal r0380_no_acct_aed_resident) {
		this.r0380_no_acct_aed_resident = r0380_no_acct_aed_resident;
	}
	public BigDecimal getR0380_amount_aed_resident() {
		return r0380_amount_aed_resident;
	}
	public void setR0380_amount_aed_resident(BigDecimal r0380_amount_aed_resident) {
		this.r0380_amount_aed_resident = r0380_amount_aed_resident;
	}
	public BigDecimal getR0380_no_acct_fcy_resident() {
		return r0380_no_acct_fcy_resident;
	}
	public void setR0380_no_acct_fcy_resident(BigDecimal r0380_no_acct_fcy_resident) {
		this.r0380_no_acct_fcy_resident = r0380_no_acct_fcy_resident;
	}
	public BigDecimal getR0380_amount_fcy_resident() {
		return r0380_amount_fcy_resident;
	}
	public void setR0380_amount_fcy_resident(BigDecimal r0380_amount_fcy_resident) {
		this.r0380_amount_fcy_resident = r0380_amount_fcy_resident;
	}
	public BigDecimal getR0380_no_acct_aed_non_resident() {
		return r0380_no_acct_aed_non_resident;
	}
	public void setR0380_no_acct_aed_non_resident(BigDecimal r0380_no_acct_aed_non_resident) {
		this.r0380_no_acct_aed_non_resident = r0380_no_acct_aed_non_resident;
	}
	public BigDecimal getR0380_amount_aed_non_resident() {
		return r0380_amount_aed_non_resident;
	}
	public void setR0380_amount_aed_non_resident(BigDecimal r0380_amount_aed_non_resident) {
		this.r0380_amount_aed_non_resident = r0380_amount_aed_non_resident;
	}
	public BigDecimal getR0380_no_acct_fcy_non_resident() {
		return r0380_no_acct_fcy_non_resident;
	}
	public void setR0380_no_acct_fcy_non_resident(BigDecimal r0380_no_acct_fcy_non_resident) {
		this.r0380_no_acct_fcy_non_resident = r0380_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0380_amount_fcy_non_resident() {
		return r0380_amount_fcy_non_resident;
	}
	public void setR0380_amount_fcy_non_resident(BigDecimal r0380_amount_fcy_non_resident) {
		this.r0380_amount_fcy_non_resident = r0380_amount_fcy_non_resident;
	}
	public BigDecimal getR0380_total_amount() {
		return r0380_total_amount;
	}
	public void setR0380_total_amount(BigDecimal r0380_total_amount) {
		this.r0380_total_amount = r0380_total_amount;
	}
	public String getR0390_product() {
		return r0390_product;
	}
	public void setR0390_product(String r0390_product) {
		this.r0390_product = r0390_product;
	}
	public BigDecimal getR0390_no_acct_aed_resident() {
		return r0390_no_acct_aed_resident;
	}
	public void setR0390_no_acct_aed_resident(BigDecimal r0390_no_acct_aed_resident) {
		this.r0390_no_acct_aed_resident = r0390_no_acct_aed_resident;
	}
	public BigDecimal getR0390_amount_aed_resident() {
		return r0390_amount_aed_resident;
	}
	public void setR0390_amount_aed_resident(BigDecimal r0390_amount_aed_resident) {
		this.r0390_amount_aed_resident = r0390_amount_aed_resident;
	}
	public BigDecimal getR0390_no_acct_fcy_resident() {
		return r0390_no_acct_fcy_resident;
	}
	public void setR0390_no_acct_fcy_resident(BigDecimal r0390_no_acct_fcy_resident) {
		this.r0390_no_acct_fcy_resident = r0390_no_acct_fcy_resident;
	}
	public BigDecimal getR0390_amount_fcy_resident() {
		return r0390_amount_fcy_resident;
	}
	public void setR0390_amount_fcy_resident(BigDecimal r0390_amount_fcy_resident) {
		this.r0390_amount_fcy_resident = r0390_amount_fcy_resident;
	}
	public BigDecimal getR0390_no_acct_aed_non_resident() {
		return r0390_no_acct_aed_non_resident;
	}
	public void setR0390_no_acct_aed_non_resident(BigDecimal r0390_no_acct_aed_non_resident) {
		this.r0390_no_acct_aed_non_resident = r0390_no_acct_aed_non_resident;
	}
	public BigDecimal getR0390_amount_aed_non_resident() {
		return r0390_amount_aed_non_resident;
	}
	public void setR0390_amount_aed_non_resident(BigDecimal r0390_amount_aed_non_resident) {
		this.r0390_amount_aed_non_resident = r0390_amount_aed_non_resident;
	}
	public BigDecimal getR0390_no_acct_fcy_non_resident() {
		return r0390_no_acct_fcy_non_resident;
	}
	public void setR0390_no_acct_fcy_non_resident(BigDecimal r0390_no_acct_fcy_non_resident) {
		this.r0390_no_acct_fcy_non_resident = r0390_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0390_amount_fcy_non_resident() {
		return r0390_amount_fcy_non_resident;
	}
	public void setR0390_amount_fcy_non_resident(BigDecimal r0390_amount_fcy_non_resident) {
		this.r0390_amount_fcy_non_resident = r0390_amount_fcy_non_resident;
	}
	public BigDecimal getR0390_total_amount() {
		return r0390_total_amount;
	}
	public void setR0390_total_amount(BigDecimal r0390_total_amount) {
		this.r0390_total_amount = r0390_total_amount;
	}
	public String getR0400_product() {
		return r0400_product;
	}
	public void setR0400_product(String r0400_product) {
		this.r0400_product = r0400_product;
	}
	public BigDecimal getR0400_no_acct_aed_resident() {
		return r0400_no_acct_aed_resident;
	}
	public void setR0400_no_acct_aed_resident(BigDecimal r0400_no_acct_aed_resident) {
		this.r0400_no_acct_aed_resident = r0400_no_acct_aed_resident;
	}
	public BigDecimal getR0400_amount_aed_resident() {
		return r0400_amount_aed_resident;
	}
	public void setR0400_amount_aed_resident(BigDecimal r0400_amount_aed_resident) {
		this.r0400_amount_aed_resident = r0400_amount_aed_resident;
	}
	public BigDecimal getR0400_no_acct_fcy_resident() {
		return r0400_no_acct_fcy_resident;
	}
	public void setR0400_no_acct_fcy_resident(BigDecimal r0400_no_acct_fcy_resident) {
		this.r0400_no_acct_fcy_resident = r0400_no_acct_fcy_resident;
	}
	public BigDecimal getR0400_amount_fcy_resident() {
		return r0400_amount_fcy_resident;
	}
	public void setR0400_amount_fcy_resident(BigDecimal r0400_amount_fcy_resident) {
		this.r0400_amount_fcy_resident = r0400_amount_fcy_resident;
	}
	public BigDecimal getR0400_no_acct_aed_non_resident() {
		return r0400_no_acct_aed_non_resident;
	}
	public void setR0400_no_acct_aed_non_resident(BigDecimal r0400_no_acct_aed_non_resident) {
		this.r0400_no_acct_aed_non_resident = r0400_no_acct_aed_non_resident;
	}
	public BigDecimal getR0400_amount_aed_non_resident() {
		return r0400_amount_aed_non_resident;
	}
	public void setR0400_amount_aed_non_resident(BigDecimal r0400_amount_aed_non_resident) {
		this.r0400_amount_aed_non_resident = r0400_amount_aed_non_resident;
	}
	public BigDecimal getR0400_no_acct_fcy_non_resident() {
		return r0400_no_acct_fcy_non_resident;
	}
	public void setR0400_no_acct_fcy_non_resident(BigDecimal r0400_no_acct_fcy_non_resident) {
		this.r0400_no_acct_fcy_non_resident = r0400_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0400_amount_fcy_non_resident() {
		return r0400_amount_fcy_non_resident;
	}
	public void setR0400_amount_fcy_non_resident(BigDecimal r0400_amount_fcy_non_resident) {
		this.r0400_amount_fcy_non_resident = r0400_amount_fcy_non_resident;
	}
	public BigDecimal getR0400_total_amount() {
		return r0400_total_amount;
	}
	public void setR0400_total_amount(BigDecimal r0400_total_amount) {
		this.r0400_total_amount = r0400_total_amount;
	}
	public String getR0410_product() {
		return r0410_product;
	}
	public void setR0410_product(String r0410_product) {
		this.r0410_product = r0410_product;
	}
	public BigDecimal getR0410_no_acct_aed_resident() {
		return r0410_no_acct_aed_resident;
	}
	public void setR0410_no_acct_aed_resident(BigDecimal r0410_no_acct_aed_resident) {
		this.r0410_no_acct_aed_resident = r0410_no_acct_aed_resident;
	}
	public BigDecimal getR0410_amount_aed_resident() {
		return r0410_amount_aed_resident;
	}
	public void setR0410_amount_aed_resident(BigDecimal r0410_amount_aed_resident) {
		this.r0410_amount_aed_resident = r0410_amount_aed_resident;
	}
	public BigDecimal getR0410_no_acct_fcy_resident() {
		return r0410_no_acct_fcy_resident;
	}
	public void setR0410_no_acct_fcy_resident(BigDecimal r0410_no_acct_fcy_resident) {
		this.r0410_no_acct_fcy_resident = r0410_no_acct_fcy_resident;
	}
	public BigDecimal getR0410_amount_fcy_resident() {
		return r0410_amount_fcy_resident;
	}
	public void setR0410_amount_fcy_resident(BigDecimal r0410_amount_fcy_resident) {
		this.r0410_amount_fcy_resident = r0410_amount_fcy_resident;
	}
	public BigDecimal getR0410_no_acct_aed_non_resident() {
		return r0410_no_acct_aed_non_resident;
	}
	public void setR0410_no_acct_aed_non_resident(BigDecimal r0410_no_acct_aed_non_resident) {
		this.r0410_no_acct_aed_non_resident = r0410_no_acct_aed_non_resident;
	}
	public BigDecimal getR0410_amount_aed_non_resident() {
		return r0410_amount_aed_non_resident;
	}
	public void setR0410_amount_aed_non_resident(BigDecimal r0410_amount_aed_non_resident) {
		this.r0410_amount_aed_non_resident = r0410_amount_aed_non_resident;
	}
	public BigDecimal getR0410_no_acct_fcy_non_resident() {
		return r0410_no_acct_fcy_non_resident;
	}
	public void setR0410_no_acct_fcy_non_resident(BigDecimal r0410_no_acct_fcy_non_resident) {
		this.r0410_no_acct_fcy_non_resident = r0410_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0410_amount_fcy_non_resident() {
		return r0410_amount_fcy_non_resident;
	}
	public void setR0410_amount_fcy_non_resident(BigDecimal r0410_amount_fcy_non_resident) {
		this.r0410_amount_fcy_non_resident = r0410_amount_fcy_non_resident;
	}
	public BigDecimal getR0410_total_amount() {
		return r0410_total_amount;
	}
	public void setR0410_total_amount(BigDecimal r0410_total_amount) {
		this.r0410_total_amount = r0410_total_amount;
	}
	public String getR0420_product() {
		return r0420_product;
	}
	public void setR0420_product(String r0420_product) {
		this.r0420_product = r0420_product;
	}
	public BigDecimal getR0420_no_acct_aed_resident() {
		return r0420_no_acct_aed_resident;
	}
	public void setR0420_no_acct_aed_resident(BigDecimal r0420_no_acct_aed_resident) {
		this.r0420_no_acct_aed_resident = r0420_no_acct_aed_resident;
	}
	public BigDecimal getR0420_amount_aed_resident() {
		return r0420_amount_aed_resident;
	}
	public void setR0420_amount_aed_resident(BigDecimal r0420_amount_aed_resident) {
		this.r0420_amount_aed_resident = r0420_amount_aed_resident;
	}
	public BigDecimal getR0420_no_acct_fcy_resident() {
		return r0420_no_acct_fcy_resident;
	}
	public void setR0420_no_acct_fcy_resident(BigDecimal r0420_no_acct_fcy_resident) {
		this.r0420_no_acct_fcy_resident = r0420_no_acct_fcy_resident;
	}
	public BigDecimal getR0420_amount_fcy_resident() {
		return r0420_amount_fcy_resident;
	}
	public void setR0420_amount_fcy_resident(BigDecimal r0420_amount_fcy_resident) {
		this.r0420_amount_fcy_resident = r0420_amount_fcy_resident;
	}
	public BigDecimal getR0420_no_acct_aed_non_resident() {
		return r0420_no_acct_aed_non_resident;
	}
	public void setR0420_no_acct_aed_non_resident(BigDecimal r0420_no_acct_aed_non_resident) {
		this.r0420_no_acct_aed_non_resident = r0420_no_acct_aed_non_resident;
	}
	public BigDecimal getR0420_amount_aed_non_resident() {
		return r0420_amount_aed_non_resident;
	}
	public void setR0420_amount_aed_non_resident(BigDecimal r0420_amount_aed_non_resident) {
		this.r0420_amount_aed_non_resident = r0420_amount_aed_non_resident;
	}
	public BigDecimal getR0420_no_acct_fcy_non_resident() {
		return r0420_no_acct_fcy_non_resident;
	}
	public void setR0420_no_acct_fcy_non_resident(BigDecimal r0420_no_acct_fcy_non_resident) {
		this.r0420_no_acct_fcy_non_resident = r0420_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0420_amount_fcy_non_resident() {
		return r0420_amount_fcy_non_resident;
	}
	public void setR0420_amount_fcy_non_resident(BigDecimal r0420_amount_fcy_non_resident) {
		this.r0420_amount_fcy_non_resident = r0420_amount_fcy_non_resident;
	}
	public BigDecimal getR0420_total_amount() {
		return r0420_total_amount;
	}
	public void setR0420_total_amount(BigDecimal r0420_total_amount) {
		this.r0420_total_amount = r0420_total_amount;
	}
	public String getR0430_product() {
		return r0430_product;
	}
	public void setR0430_product(String r0430_product) {
		this.r0430_product = r0430_product;
	}
	public BigDecimal getR0430_no_acct_aed_resident() {
		return r0430_no_acct_aed_resident;
	}
	public void setR0430_no_acct_aed_resident(BigDecimal r0430_no_acct_aed_resident) {
		this.r0430_no_acct_aed_resident = r0430_no_acct_aed_resident;
	}
	public BigDecimal getR0430_amount_aed_resident() {
		return r0430_amount_aed_resident;
	}
	public void setR0430_amount_aed_resident(BigDecimal r0430_amount_aed_resident) {
		this.r0430_amount_aed_resident = r0430_amount_aed_resident;
	}
	public BigDecimal getR0430_no_acct_fcy_resident() {
		return r0430_no_acct_fcy_resident;
	}
	public void setR0430_no_acct_fcy_resident(BigDecimal r0430_no_acct_fcy_resident) {
		this.r0430_no_acct_fcy_resident = r0430_no_acct_fcy_resident;
	}
	public BigDecimal getR0430_amount_fcy_resident() {
		return r0430_amount_fcy_resident;
	}
	public void setR0430_amount_fcy_resident(BigDecimal r0430_amount_fcy_resident) {
		this.r0430_amount_fcy_resident = r0430_amount_fcy_resident;
	}
	public BigDecimal getR0430_no_acct_aed_non_resident() {
		return r0430_no_acct_aed_non_resident;
	}
	public void setR0430_no_acct_aed_non_resident(BigDecimal r0430_no_acct_aed_non_resident) {
		this.r0430_no_acct_aed_non_resident = r0430_no_acct_aed_non_resident;
	}
	public BigDecimal getR0430_amount_aed_non_resident() {
		return r0430_amount_aed_non_resident;
	}
	public void setR0430_amount_aed_non_resident(BigDecimal r0430_amount_aed_non_resident) {
		this.r0430_amount_aed_non_resident = r0430_amount_aed_non_resident;
	}
	public BigDecimal getR0430_no_acct_fcy_non_resident() {
		return r0430_no_acct_fcy_non_resident;
	}
	public void setR0430_no_acct_fcy_non_resident(BigDecimal r0430_no_acct_fcy_non_resident) {
		this.r0430_no_acct_fcy_non_resident = r0430_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0430_amount_fcy_non_resident() {
		return r0430_amount_fcy_non_resident;
	}
	public void setR0430_amount_fcy_non_resident(BigDecimal r0430_amount_fcy_non_resident) {
		this.r0430_amount_fcy_non_resident = r0430_amount_fcy_non_resident;
	}
	public BigDecimal getR0430_total_amount() {
		return r0430_total_amount;
	}
	public void setR0430_total_amount(BigDecimal r0430_total_amount) {
		this.r0430_total_amount = r0430_total_amount;
	}
	public String getR0440_product() {
		return r0440_product;
	}
	public void setR0440_product(String r0440_product) {
		this.r0440_product = r0440_product;
	}
	public BigDecimal getR0440_no_acct_aed_resident() {
		return r0440_no_acct_aed_resident;
	}
	public void setR0440_no_acct_aed_resident(BigDecimal r0440_no_acct_aed_resident) {
		this.r0440_no_acct_aed_resident = r0440_no_acct_aed_resident;
	}
	public BigDecimal getR0440_amount_aed_resident() {
		return r0440_amount_aed_resident;
	}
	public void setR0440_amount_aed_resident(BigDecimal r0440_amount_aed_resident) {
		this.r0440_amount_aed_resident = r0440_amount_aed_resident;
	}
	public BigDecimal getR0440_no_acct_fcy_resident() {
		return r0440_no_acct_fcy_resident;
	}
	public void setR0440_no_acct_fcy_resident(BigDecimal r0440_no_acct_fcy_resident) {
		this.r0440_no_acct_fcy_resident = r0440_no_acct_fcy_resident;
	}
	public BigDecimal getR0440_amount_fcy_resident() {
		return r0440_amount_fcy_resident;
	}
	public void setR0440_amount_fcy_resident(BigDecimal r0440_amount_fcy_resident) {
		this.r0440_amount_fcy_resident = r0440_amount_fcy_resident;
	}
	public BigDecimal getR0440_no_acct_aed_non_resident() {
		return r0440_no_acct_aed_non_resident;
	}
	public void setR0440_no_acct_aed_non_resident(BigDecimal r0440_no_acct_aed_non_resident) {
		this.r0440_no_acct_aed_non_resident = r0440_no_acct_aed_non_resident;
	}
	public BigDecimal getR0440_amount_aed_non_resident() {
		return r0440_amount_aed_non_resident;
	}
	public void setR0440_amount_aed_non_resident(BigDecimal r0440_amount_aed_non_resident) {
		this.r0440_amount_aed_non_resident = r0440_amount_aed_non_resident;
	}
	public BigDecimal getR0440_no_acct_fcy_non_resident() {
		return r0440_no_acct_fcy_non_resident;
	}
	public void setR0440_no_acct_fcy_non_resident(BigDecimal r0440_no_acct_fcy_non_resident) {
		this.r0440_no_acct_fcy_non_resident = r0440_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0440_amount_fcy_non_resident() {
		return r0440_amount_fcy_non_resident;
	}
	public void setR0440_amount_fcy_non_resident(BigDecimal r0440_amount_fcy_non_resident) {
		this.r0440_amount_fcy_non_resident = r0440_amount_fcy_non_resident;
	}
	public BigDecimal getR0440_total_amount() {
		return r0440_total_amount;
	}
	public void setR0440_total_amount(BigDecimal r0440_total_amount) {
		this.r0440_total_amount = r0440_total_amount;
	}
	public String getR0450_product() {
		return r0450_product;
	}
	public void setR0450_product(String r0450_product) {
		this.r0450_product = r0450_product;
	}
	public BigDecimal getR0450_no_acct_aed_resident() {
		return r0450_no_acct_aed_resident;
	}
	public void setR0450_no_acct_aed_resident(BigDecimal r0450_no_acct_aed_resident) {
		this.r0450_no_acct_aed_resident = r0450_no_acct_aed_resident;
	}
	public BigDecimal getR0450_amount_aed_resident() {
		return r0450_amount_aed_resident;
	}
	public void setR0450_amount_aed_resident(BigDecimal r0450_amount_aed_resident) {
		this.r0450_amount_aed_resident = r0450_amount_aed_resident;
	}
	public BigDecimal getR0450_no_acct_fcy_resident() {
		return r0450_no_acct_fcy_resident;
	}
	public void setR0450_no_acct_fcy_resident(BigDecimal r0450_no_acct_fcy_resident) {
		this.r0450_no_acct_fcy_resident = r0450_no_acct_fcy_resident;
	}
	public BigDecimal getR0450_amount_fcy_resident() {
		return r0450_amount_fcy_resident;
	}
	public void setR0450_amount_fcy_resident(BigDecimal r0450_amount_fcy_resident) {
		this.r0450_amount_fcy_resident = r0450_amount_fcy_resident;
	}
	public BigDecimal getR0450_no_acct_aed_non_resident() {
		return r0450_no_acct_aed_non_resident;
	}
	public void setR0450_no_acct_aed_non_resident(BigDecimal r0450_no_acct_aed_non_resident) {
		this.r0450_no_acct_aed_non_resident = r0450_no_acct_aed_non_resident;
	}
	public BigDecimal getR0450_amount_aed_non_resident() {
		return r0450_amount_aed_non_resident;
	}
	public void setR0450_amount_aed_non_resident(BigDecimal r0450_amount_aed_non_resident) {
		this.r0450_amount_aed_non_resident = r0450_amount_aed_non_resident;
	}
	public BigDecimal getR0450_no_acct_fcy_non_resident() {
		return r0450_no_acct_fcy_non_resident;
	}
	public void setR0450_no_acct_fcy_non_resident(BigDecimal r0450_no_acct_fcy_non_resident) {
		this.r0450_no_acct_fcy_non_resident = r0450_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0450_amount_fcy_non_resident() {
		return r0450_amount_fcy_non_resident;
	}
	public void setR0450_amount_fcy_non_resident(BigDecimal r0450_amount_fcy_non_resident) {
		this.r0450_amount_fcy_non_resident = r0450_amount_fcy_non_resident;
	}
	public BigDecimal getR0450_total_amount() {
		return r0450_total_amount;
	}
	public void setR0450_total_amount(BigDecimal r0450_total_amount) {
		this.r0450_total_amount = r0450_total_amount;
	}
	public String getR0460_product() {
		return r0460_product;
	}
	public void setR0460_product(String r0460_product) {
		this.r0460_product = r0460_product;
	}
	public BigDecimal getR0460_no_acct_aed_resident() {
		return r0460_no_acct_aed_resident;
	}
	public void setR0460_no_acct_aed_resident(BigDecimal r0460_no_acct_aed_resident) {
		this.r0460_no_acct_aed_resident = r0460_no_acct_aed_resident;
	}
	public BigDecimal getR0460_amount_aed_resident() {
		return r0460_amount_aed_resident;
	}
	public void setR0460_amount_aed_resident(BigDecimal r0460_amount_aed_resident) {
		this.r0460_amount_aed_resident = r0460_amount_aed_resident;
	}
	public BigDecimal getR0460_no_acct_fcy_resident() {
		return r0460_no_acct_fcy_resident;
	}
	public void setR0460_no_acct_fcy_resident(BigDecimal r0460_no_acct_fcy_resident) {
		this.r0460_no_acct_fcy_resident = r0460_no_acct_fcy_resident;
	}
	public BigDecimal getR0460_amount_fcy_resident() {
		return r0460_amount_fcy_resident;
	}
	public void setR0460_amount_fcy_resident(BigDecimal r0460_amount_fcy_resident) {
		this.r0460_amount_fcy_resident = r0460_amount_fcy_resident;
	}
	public BigDecimal getR0460_no_acct_aed_non_resident() {
		return r0460_no_acct_aed_non_resident;
	}
	public void setR0460_no_acct_aed_non_resident(BigDecimal r0460_no_acct_aed_non_resident) {
		this.r0460_no_acct_aed_non_resident = r0460_no_acct_aed_non_resident;
	}
	public BigDecimal getR0460_amount_aed_non_resident() {
		return r0460_amount_aed_non_resident;
	}
	public void setR0460_amount_aed_non_resident(BigDecimal r0460_amount_aed_non_resident) {
		this.r0460_amount_aed_non_resident = r0460_amount_aed_non_resident;
	}
	public BigDecimal getR0460_no_acct_fcy_non_resident() {
		return r0460_no_acct_fcy_non_resident;
	}
	public void setR0460_no_acct_fcy_non_resident(BigDecimal r0460_no_acct_fcy_non_resident) {
		this.r0460_no_acct_fcy_non_resident = r0460_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0460_amount_fcy_non_resident() {
		return r0460_amount_fcy_non_resident;
	}
	public void setR0460_amount_fcy_non_resident(BigDecimal r0460_amount_fcy_non_resident) {
		this.r0460_amount_fcy_non_resident = r0460_amount_fcy_non_resident;
	}
	public BigDecimal getR0460_total_amount() {
		return r0460_total_amount;
	}
	public void setR0460_total_amount(BigDecimal r0460_total_amount) {
		this.r0460_total_amount = r0460_total_amount;
	}
	public String getR0470_product() {
		return r0470_product;
	}
	public void setR0470_product(String r0470_product) {
		this.r0470_product = r0470_product;
	}
	public BigDecimal getR0470_no_acct_aed_resident() {
		return r0470_no_acct_aed_resident;
	}
	public void setR0470_no_acct_aed_resident(BigDecimal r0470_no_acct_aed_resident) {
		this.r0470_no_acct_aed_resident = r0470_no_acct_aed_resident;
	}
	public BigDecimal getR0470_amount_aed_resident() {
		return r0470_amount_aed_resident;
	}
	public void setR0470_amount_aed_resident(BigDecimal r0470_amount_aed_resident) {
		this.r0470_amount_aed_resident = r0470_amount_aed_resident;
	}
	public BigDecimal getR0470_no_acct_fcy_resident() {
		return r0470_no_acct_fcy_resident;
	}
	public void setR0470_no_acct_fcy_resident(BigDecimal r0470_no_acct_fcy_resident) {
		this.r0470_no_acct_fcy_resident = r0470_no_acct_fcy_resident;
	}
	public BigDecimal getR0470_amount_fcy_resident() {
		return r0470_amount_fcy_resident;
	}
	public void setR0470_amount_fcy_resident(BigDecimal r0470_amount_fcy_resident) {
		this.r0470_amount_fcy_resident = r0470_amount_fcy_resident;
	}
	public BigDecimal getR0470_no_acct_aed_non_resident() {
		return r0470_no_acct_aed_non_resident;
	}
	public void setR0470_no_acct_aed_non_resident(BigDecimal r0470_no_acct_aed_non_resident) {
		this.r0470_no_acct_aed_non_resident = r0470_no_acct_aed_non_resident;
	}
	public BigDecimal getR0470_amount_aed_non_resident() {
		return r0470_amount_aed_non_resident;
	}
	public void setR0470_amount_aed_non_resident(BigDecimal r0470_amount_aed_non_resident) {
		this.r0470_amount_aed_non_resident = r0470_amount_aed_non_resident;
	}
	public BigDecimal getR0470_no_acct_fcy_non_resident() {
		return r0470_no_acct_fcy_non_resident;
	}
	public void setR0470_no_acct_fcy_non_resident(BigDecimal r0470_no_acct_fcy_non_resident) {
		this.r0470_no_acct_fcy_non_resident = r0470_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0470_amount_fcy_non_resident() {
		return r0470_amount_fcy_non_resident;
	}
	public void setR0470_amount_fcy_non_resident(BigDecimal r0470_amount_fcy_non_resident) {
		this.r0470_amount_fcy_non_resident = r0470_amount_fcy_non_resident;
	}
	public BigDecimal getR0470_total_amount() {
		return r0470_total_amount;
	}
	public void setR0470_total_amount(BigDecimal r0470_total_amount) {
		this.r0470_total_amount = r0470_total_amount;
	}
	public String getR0480_product() {
		return r0480_product;
	}
	public void setR0480_product(String r0480_product) {
		this.r0480_product = r0480_product;
	}
	public BigDecimal getR0480_no_acct_aed_resident() {
		return r0480_no_acct_aed_resident;
	}
	public void setR0480_no_acct_aed_resident(BigDecimal r0480_no_acct_aed_resident) {
		this.r0480_no_acct_aed_resident = r0480_no_acct_aed_resident;
	}
	public BigDecimal getR0480_amount_aed_resident() {
		return r0480_amount_aed_resident;
	}
	public void setR0480_amount_aed_resident(BigDecimal r0480_amount_aed_resident) {
		this.r0480_amount_aed_resident = r0480_amount_aed_resident;
	}
	public BigDecimal getR0480_no_acct_fcy_resident() {
		return r0480_no_acct_fcy_resident;
	}
	public void setR0480_no_acct_fcy_resident(BigDecimal r0480_no_acct_fcy_resident) {
		this.r0480_no_acct_fcy_resident = r0480_no_acct_fcy_resident;
	}
	public BigDecimal getR0480_amount_fcy_resident() {
		return r0480_amount_fcy_resident;
	}
	public void setR0480_amount_fcy_resident(BigDecimal r0480_amount_fcy_resident) {
		this.r0480_amount_fcy_resident = r0480_amount_fcy_resident;
	}
	public BigDecimal getR0480_no_acct_aed_non_resident() {
		return r0480_no_acct_aed_non_resident;
	}
	public void setR0480_no_acct_aed_non_resident(BigDecimal r0480_no_acct_aed_non_resident) {
		this.r0480_no_acct_aed_non_resident = r0480_no_acct_aed_non_resident;
	}
	public BigDecimal getR0480_amount_aed_non_resident() {
		return r0480_amount_aed_non_resident;
	}
	public void setR0480_amount_aed_non_resident(BigDecimal r0480_amount_aed_non_resident) {
		this.r0480_amount_aed_non_resident = r0480_amount_aed_non_resident;
	}
	public BigDecimal getR0480_no_acct_fcy_non_resident() {
		return r0480_no_acct_fcy_non_resident;
	}
	public void setR0480_no_acct_fcy_non_resident(BigDecimal r0480_no_acct_fcy_non_resident) {
		this.r0480_no_acct_fcy_non_resident = r0480_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0480_amount_fcy_non_resident() {
		return r0480_amount_fcy_non_resident;
	}
	public void setR0480_amount_fcy_non_resident(BigDecimal r0480_amount_fcy_non_resident) {
		this.r0480_amount_fcy_non_resident = r0480_amount_fcy_non_resident;
	}
	public BigDecimal getR0480_total_amount() {
		return r0480_total_amount;
	}
	public void setR0480_total_amount(BigDecimal r0480_total_amount) {
		this.r0480_total_amount = r0480_total_amount;
	}
	public String getR0490_product() {
		return r0490_product;
	}
	public void setR0490_product(String r0490_product) {
		this.r0490_product = r0490_product;
	}
	public BigDecimal getR0490_no_acct_aed_resident() {
		return r0490_no_acct_aed_resident;
	}
	public void setR0490_no_acct_aed_resident(BigDecimal r0490_no_acct_aed_resident) {
		this.r0490_no_acct_aed_resident = r0490_no_acct_aed_resident;
	}
	public BigDecimal getR0490_amount_aed_resident() {
		return r0490_amount_aed_resident;
	}
	public void setR0490_amount_aed_resident(BigDecimal r0490_amount_aed_resident) {
		this.r0490_amount_aed_resident = r0490_amount_aed_resident;
	}
	public BigDecimal getR0490_no_acct_fcy_resident() {
		return r0490_no_acct_fcy_resident;
	}
	public void setR0490_no_acct_fcy_resident(BigDecimal r0490_no_acct_fcy_resident) {
		this.r0490_no_acct_fcy_resident = r0490_no_acct_fcy_resident;
	}
	public BigDecimal getR0490_amount_fcy_resident() {
		return r0490_amount_fcy_resident;
	}
	public void setR0490_amount_fcy_resident(BigDecimal r0490_amount_fcy_resident) {
		this.r0490_amount_fcy_resident = r0490_amount_fcy_resident;
	}
	public BigDecimal getR0490_no_acct_aed_non_resident() {
		return r0490_no_acct_aed_non_resident;
	}
	public void setR0490_no_acct_aed_non_resident(BigDecimal r0490_no_acct_aed_non_resident) {
		this.r0490_no_acct_aed_non_resident = r0490_no_acct_aed_non_resident;
	}
	public BigDecimal getR0490_amount_aed_non_resident() {
		return r0490_amount_aed_non_resident;
	}
	public void setR0490_amount_aed_non_resident(BigDecimal r0490_amount_aed_non_resident) {
		this.r0490_amount_aed_non_resident = r0490_amount_aed_non_resident;
	}
	public BigDecimal getR0490_no_acct_fcy_non_resident() {
		return r0490_no_acct_fcy_non_resident;
	}
	public void setR0490_no_acct_fcy_non_resident(BigDecimal r0490_no_acct_fcy_non_resident) {
		this.r0490_no_acct_fcy_non_resident = r0490_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0490_amount_fcy_non_resident() {
		return r0490_amount_fcy_non_resident;
	}
	public void setR0490_amount_fcy_non_resident(BigDecimal r0490_amount_fcy_non_resident) {
		this.r0490_amount_fcy_non_resident = r0490_amount_fcy_non_resident;
	}
	public BigDecimal getR0490_total_amount() {
		return r0490_total_amount;
	}
	public void setR0490_total_amount(BigDecimal r0490_total_amount) {
		this.r0490_total_amount = r0490_total_amount;
	}
	public String getR0500_product() {
		return r0500_product;
	}
	public void setR0500_product(String r0500_product) {
		this.r0500_product = r0500_product;
	}
	public BigDecimal getR0500_no_acct_aed_resident() {
		return r0500_no_acct_aed_resident;
	}
	public void setR0500_no_acct_aed_resident(BigDecimal r0500_no_acct_aed_resident) {
		this.r0500_no_acct_aed_resident = r0500_no_acct_aed_resident;
	}
	public BigDecimal getR0500_amount_aed_resident() {
		return r0500_amount_aed_resident;
	}
	public void setR0500_amount_aed_resident(BigDecimal r0500_amount_aed_resident) {
		this.r0500_amount_aed_resident = r0500_amount_aed_resident;
	}
	public BigDecimal getR0500_no_acct_fcy_resident() {
		return r0500_no_acct_fcy_resident;
	}
	public void setR0500_no_acct_fcy_resident(BigDecimal r0500_no_acct_fcy_resident) {
		this.r0500_no_acct_fcy_resident = r0500_no_acct_fcy_resident;
	}
	public BigDecimal getR0500_amount_fcy_resident() {
		return r0500_amount_fcy_resident;
	}
	public void setR0500_amount_fcy_resident(BigDecimal r0500_amount_fcy_resident) {
		this.r0500_amount_fcy_resident = r0500_amount_fcy_resident;
	}
	public BigDecimal getR0500_no_acct_aed_non_resident() {
		return r0500_no_acct_aed_non_resident;
	}
	public void setR0500_no_acct_aed_non_resident(BigDecimal r0500_no_acct_aed_non_resident) {
		this.r0500_no_acct_aed_non_resident = r0500_no_acct_aed_non_resident;
	}
	public BigDecimal getR0500_amount_aed_non_resident() {
		return r0500_amount_aed_non_resident;
	}
	public void setR0500_amount_aed_non_resident(BigDecimal r0500_amount_aed_non_resident) {
		this.r0500_amount_aed_non_resident = r0500_amount_aed_non_resident;
	}
	public BigDecimal getR0500_no_acct_fcy_non_resident() {
		return r0500_no_acct_fcy_non_resident;
	}
	public void setR0500_no_acct_fcy_non_resident(BigDecimal r0500_no_acct_fcy_non_resident) {
		this.r0500_no_acct_fcy_non_resident = r0500_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0500_amount_fcy_non_resident() {
		return r0500_amount_fcy_non_resident;
	}
	public void setR0500_amount_fcy_non_resident(BigDecimal r0500_amount_fcy_non_resident) {
		this.r0500_amount_fcy_non_resident = r0500_amount_fcy_non_resident;
	}
	public BigDecimal getR0500_total_amount() {
		return r0500_total_amount;
	}
	public void setR0500_total_amount(BigDecimal r0500_total_amount) {
		this.r0500_total_amount = r0500_total_amount;
	}
	public String getR0510_product() {
		return r0510_product;
	}
	public void setR0510_product(String r0510_product) {
		this.r0510_product = r0510_product;
	}
	public BigDecimal getR0510_no_acct_aed_resident() {
		return r0510_no_acct_aed_resident;
	}
	public void setR0510_no_acct_aed_resident(BigDecimal r0510_no_acct_aed_resident) {
		this.r0510_no_acct_aed_resident = r0510_no_acct_aed_resident;
	}
	public BigDecimal getR0510_amount_aed_resident() {
		return r0510_amount_aed_resident;
	}
	public void setR0510_amount_aed_resident(BigDecimal r0510_amount_aed_resident) {
		this.r0510_amount_aed_resident = r0510_amount_aed_resident;
	}
	public BigDecimal getR0510_no_acct_fcy_resident() {
		return r0510_no_acct_fcy_resident;
	}
	public void setR0510_no_acct_fcy_resident(BigDecimal r0510_no_acct_fcy_resident) {
		this.r0510_no_acct_fcy_resident = r0510_no_acct_fcy_resident;
	}
	public BigDecimal getR0510_amount_fcy_resident() {
		return r0510_amount_fcy_resident;
	}
	public void setR0510_amount_fcy_resident(BigDecimal r0510_amount_fcy_resident) {
		this.r0510_amount_fcy_resident = r0510_amount_fcy_resident;
	}
	public BigDecimal getR0510_no_acct_aed_non_resident() {
		return r0510_no_acct_aed_non_resident;
	}
	public void setR0510_no_acct_aed_non_resident(BigDecimal r0510_no_acct_aed_non_resident) {
		this.r0510_no_acct_aed_non_resident = r0510_no_acct_aed_non_resident;
	}
	public BigDecimal getR0510_amount_aed_non_resident() {
		return r0510_amount_aed_non_resident;
	}
	public void setR0510_amount_aed_non_resident(BigDecimal r0510_amount_aed_non_resident) {
		this.r0510_amount_aed_non_resident = r0510_amount_aed_non_resident;
	}
	public BigDecimal getR0510_no_acct_fcy_non_resident() {
		return r0510_no_acct_fcy_non_resident;
	}
	public void setR0510_no_acct_fcy_non_resident(BigDecimal r0510_no_acct_fcy_non_resident) {
		this.r0510_no_acct_fcy_non_resident = r0510_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0510_amount_fcy_non_resident() {
		return r0510_amount_fcy_non_resident;
	}
	public void setR0510_amount_fcy_non_resident(BigDecimal r0510_amount_fcy_non_resident) {
		this.r0510_amount_fcy_non_resident = r0510_amount_fcy_non_resident;
	}
	public BigDecimal getR0510_total_amount() {
		return r0510_total_amount;
	}
	public void setR0510_total_amount(BigDecimal r0510_total_amount) {
		this.r0510_total_amount = r0510_total_amount;
	}
	public String getR0520_product() {
		return r0520_product;
	}
	public void setR0520_product(String r0520_product) {
		this.r0520_product = r0520_product;
	}
	public BigDecimal getR0520_no_acct_aed_resident() {
		return r0520_no_acct_aed_resident;
	}
	public void setR0520_no_acct_aed_resident(BigDecimal r0520_no_acct_aed_resident) {
		this.r0520_no_acct_aed_resident = r0520_no_acct_aed_resident;
	}
	public BigDecimal getR0520_amount_aed_resident() {
		return r0520_amount_aed_resident;
	}
	public void setR0520_amount_aed_resident(BigDecimal r0520_amount_aed_resident) {
		this.r0520_amount_aed_resident = r0520_amount_aed_resident;
	}
	public BigDecimal getR0520_no_acct_fcy_resident() {
		return r0520_no_acct_fcy_resident;
	}
	public void setR0520_no_acct_fcy_resident(BigDecimal r0520_no_acct_fcy_resident) {
		this.r0520_no_acct_fcy_resident = r0520_no_acct_fcy_resident;
	}
	public BigDecimal getR0520_amount_fcy_resident() {
		return r0520_amount_fcy_resident;
	}
	public void setR0520_amount_fcy_resident(BigDecimal r0520_amount_fcy_resident) {
		this.r0520_amount_fcy_resident = r0520_amount_fcy_resident;
	}
	public BigDecimal getR0520_no_acct_aed_non_resident() {
		return r0520_no_acct_aed_non_resident;
	}
	public void setR0520_no_acct_aed_non_resident(BigDecimal r0520_no_acct_aed_non_resident) {
		this.r0520_no_acct_aed_non_resident = r0520_no_acct_aed_non_resident;
	}
	public BigDecimal getR0520_amount_aed_non_resident() {
		return r0520_amount_aed_non_resident;
	}
	public void setR0520_amount_aed_non_resident(BigDecimal r0520_amount_aed_non_resident) {
		this.r0520_amount_aed_non_resident = r0520_amount_aed_non_resident;
	}
	public BigDecimal getR0520_no_acct_fcy_non_resident() {
		return r0520_no_acct_fcy_non_resident;
	}
	public void setR0520_no_acct_fcy_non_resident(BigDecimal r0520_no_acct_fcy_non_resident) {
		this.r0520_no_acct_fcy_non_resident = r0520_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0520_amount_fcy_non_resident() {
		return r0520_amount_fcy_non_resident;
	}
	public void setR0520_amount_fcy_non_resident(BigDecimal r0520_amount_fcy_non_resident) {
		this.r0520_amount_fcy_non_resident = r0520_amount_fcy_non_resident;
	}
	public BigDecimal getR0520_total_amount() {
		return r0520_total_amount;
	}
	public void setR0520_total_amount(BigDecimal r0520_total_amount) {
		this.r0520_total_amount = r0520_total_amount;
	}
	public String getR0530_product() {
		return r0530_product;
	}
	public void setR0530_product(String r0530_product) {
		this.r0530_product = r0530_product;
	}
	public BigDecimal getR0530_no_acct_aed_resident() {
		return r0530_no_acct_aed_resident;
	}
	public void setR0530_no_acct_aed_resident(BigDecimal r0530_no_acct_aed_resident) {
		this.r0530_no_acct_aed_resident = r0530_no_acct_aed_resident;
	}
	public BigDecimal getR0530_amount_aed_resident() {
		return r0530_amount_aed_resident;
	}
	public void setR0530_amount_aed_resident(BigDecimal r0530_amount_aed_resident) {
		this.r0530_amount_aed_resident = r0530_amount_aed_resident;
	}
	public BigDecimal getR0530_no_acct_fcy_resident() {
		return r0530_no_acct_fcy_resident;
	}
	public void setR0530_no_acct_fcy_resident(BigDecimal r0530_no_acct_fcy_resident) {
		this.r0530_no_acct_fcy_resident = r0530_no_acct_fcy_resident;
	}
	public BigDecimal getR0530_amount_fcy_resident() {
		return r0530_amount_fcy_resident;
	}
	public void setR0530_amount_fcy_resident(BigDecimal r0530_amount_fcy_resident) {
		this.r0530_amount_fcy_resident = r0530_amount_fcy_resident;
	}
	public BigDecimal getR0530_no_acct_aed_non_resident() {
		return r0530_no_acct_aed_non_resident;
	}
	public void setR0530_no_acct_aed_non_resident(BigDecimal r0530_no_acct_aed_non_resident) {
		this.r0530_no_acct_aed_non_resident = r0530_no_acct_aed_non_resident;
	}
	public BigDecimal getR0530_amount_aed_non_resident() {
		return r0530_amount_aed_non_resident;
	}
	public void setR0530_amount_aed_non_resident(BigDecimal r0530_amount_aed_non_resident) {
		this.r0530_amount_aed_non_resident = r0530_amount_aed_non_resident;
	}
	public BigDecimal getR0530_no_acct_fcy_non_resident() {
		return r0530_no_acct_fcy_non_resident;
	}
	public void setR0530_no_acct_fcy_non_resident(BigDecimal r0530_no_acct_fcy_non_resident) {
		this.r0530_no_acct_fcy_non_resident = r0530_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0530_amount_fcy_non_resident() {
		return r0530_amount_fcy_non_resident;
	}
	public void setR0530_amount_fcy_non_resident(BigDecimal r0530_amount_fcy_non_resident) {
		this.r0530_amount_fcy_non_resident = r0530_amount_fcy_non_resident;
	}
	public BigDecimal getR0530_total_amount() {
		return r0530_total_amount;
	}
	public void setR0530_total_amount(BigDecimal r0530_total_amount) {
		this.r0530_total_amount = r0530_total_amount;
	}
	public String getR0540_product() {
		return r0540_product;
	}
	public void setR0540_product(String r0540_product) {
		this.r0540_product = r0540_product;
	}
	public BigDecimal getR0540_no_acct_aed_resident() {
		return r0540_no_acct_aed_resident;
	}
	public void setR0540_no_acct_aed_resident(BigDecimal r0540_no_acct_aed_resident) {
		this.r0540_no_acct_aed_resident = r0540_no_acct_aed_resident;
	}
	public BigDecimal getR0540_amount_aed_resident() {
		return r0540_amount_aed_resident;
	}
	public void setR0540_amount_aed_resident(BigDecimal r0540_amount_aed_resident) {
		this.r0540_amount_aed_resident = r0540_amount_aed_resident;
	}
	public BigDecimal getR0540_no_acct_fcy_resident() {
		return r0540_no_acct_fcy_resident;
	}
	public void setR0540_no_acct_fcy_resident(BigDecimal r0540_no_acct_fcy_resident) {
		this.r0540_no_acct_fcy_resident = r0540_no_acct_fcy_resident;
	}
	public BigDecimal getR0540_amount_fcy_resident() {
		return r0540_amount_fcy_resident;
	}
	public void setR0540_amount_fcy_resident(BigDecimal r0540_amount_fcy_resident) {
		this.r0540_amount_fcy_resident = r0540_amount_fcy_resident;
	}
	public BigDecimal getR0540_no_acct_aed_non_resident() {
		return r0540_no_acct_aed_non_resident;
	}
	public void setR0540_no_acct_aed_non_resident(BigDecimal r0540_no_acct_aed_non_resident) {
		this.r0540_no_acct_aed_non_resident = r0540_no_acct_aed_non_resident;
	}
	public BigDecimal getR0540_amount_aed_non_resident() {
		return r0540_amount_aed_non_resident;
	}
	public void setR0540_amount_aed_non_resident(BigDecimal r0540_amount_aed_non_resident) {
		this.r0540_amount_aed_non_resident = r0540_amount_aed_non_resident;
	}
	public BigDecimal getR0540_no_acct_fcy_non_resident() {
		return r0540_no_acct_fcy_non_resident;
	}
	public void setR0540_no_acct_fcy_non_resident(BigDecimal r0540_no_acct_fcy_non_resident) {
		this.r0540_no_acct_fcy_non_resident = r0540_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0540_amount_fcy_non_resident() {
		return r0540_amount_fcy_non_resident;
	}
	public void setR0540_amount_fcy_non_resident(BigDecimal r0540_amount_fcy_non_resident) {
		this.r0540_amount_fcy_non_resident = r0540_amount_fcy_non_resident;
	}
	public BigDecimal getR0540_total_amount() {
		return r0540_total_amount;
	}
	public void setR0540_total_amount(BigDecimal r0540_total_amount) {
		this.r0540_total_amount = r0540_total_amount;
	}
	public String getR0550_product() {
		return r0550_product;
	}
	public void setR0550_product(String r0550_product) {
		this.r0550_product = r0550_product;
	}
	public BigDecimal getR0550_no_acct_aed_resident() {
		return r0550_no_acct_aed_resident;
	}
	public void setR0550_no_acct_aed_resident(BigDecimal r0550_no_acct_aed_resident) {
		this.r0550_no_acct_aed_resident = r0550_no_acct_aed_resident;
	}
	public BigDecimal getR0550_amount_aed_resident() {
		return r0550_amount_aed_resident;
	}
	public void setR0550_amount_aed_resident(BigDecimal r0550_amount_aed_resident) {
		this.r0550_amount_aed_resident = r0550_amount_aed_resident;
	}
	public BigDecimal getR0550_no_acct_fcy_resident() {
		return r0550_no_acct_fcy_resident;
	}
	public void setR0550_no_acct_fcy_resident(BigDecimal r0550_no_acct_fcy_resident) {
		this.r0550_no_acct_fcy_resident = r0550_no_acct_fcy_resident;
	}
	public BigDecimal getR0550_amount_fcy_resident() {
		return r0550_amount_fcy_resident;
	}
	public void setR0550_amount_fcy_resident(BigDecimal r0550_amount_fcy_resident) {
		this.r0550_amount_fcy_resident = r0550_amount_fcy_resident;
	}
	public BigDecimal getR0550_no_acct_aed_non_resident() {
		return r0550_no_acct_aed_non_resident;
	}
	public void setR0550_no_acct_aed_non_resident(BigDecimal r0550_no_acct_aed_non_resident) {
		this.r0550_no_acct_aed_non_resident = r0550_no_acct_aed_non_resident;
	}
	public BigDecimal getR0550_amount_aed_non_resident() {
		return r0550_amount_aed_non_resident;
	}
	public void setR0550_amount_aed_non_resident(BigDecimal r0550_amount_aed_non_resident) {
		this.r0550_amount_aed_non_resident = r0550_amount_aed_non_resident;
	}
	public BigDecimal getR0550_no_acct_fcy_non_resident() {
		return r0550_no_acct_fcy_non_resident;
	}
	public void setR0550_no_acct_fcy_non_resident(BigDecimal r0550_no_acct_fcy_non_resident) {
		this.r0550_no_acct_fcy_non_resident = r0550_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0550_amount_fcy_non_resident() {
		return r0550_amount_fcy_non_resident;
	}
	public void setR0550_amount_fcy_non_resident(BigDecimal r0550_amount_fcy_non_resident) {
		this.r0550_amount_fcy_non_resident = r0550_amount_fcy_non_resident;
	}
	public BigDecimal getR0550_total_amount() {
		return r0550_total_amount;
	}
	public void setR0550_total_amount(BigDecimal r0550_total_amount) {
		this.r0550_total_amount = r0550_total_amount;
	}
	public String getR0560_product() {
		return r0560_product;
	}
	public void setR0560_product(String r0560_product) {
		this.r0560_product = r0560_product;
	}
	public BigDecimal getR0560_no_acct_aed_resident() {
		return r0560_no_acct_aed_resident;
	}
	public void setR0560_no_acct_aed_resident(BigDecimal r0560_no_acct_aed_resident) {
		this.r0560_no_acct_aed_resident = r0560_no_acct_aed_resident;
	}
	public BigDecimal getR0560_amount_aed_resident() {
		return r0560_amount_aed_resident;
	}
	public void setR0560_amount_aed_resident(BigDecimal r0560_amount_aed_resident) {
		this.r0560_amount_aed_resident = r0560_amount_aed_resident;
	}
	public BigDecimal getR0560_no_acct_fcy_resident() {
		return r0560_no_acct_fcy_resident;
	}
	public void setR0560_no_acct_fcy_resident(BigDecimal r0560_no_acct_fcy_resident) {
		this.r0560_no_acct_fcy_resident = r0560_no_acct_fcy_resident;
	}
	public BigDecimal getR0560_amount_fcy_resident() {
		return r0560_amount_fcy_resident;
	}
	public void setR0560_amount_fcy_resident(BigDecimal r0560_amount_fcy_resident) {
		this.r0560_amount_fcy_resident = r0560_amount_fcy_resident;
	}
	public BigDecimal getR0560_no_acct_aed_non_resident() {
		return r0560_no_acct_aed_non_resident;
	}
	public void setR0560_no_acct_aed_non_resident(BigDecimal r0560_no_acct_aed_non_resident) {
		this.r0560_no_acct_aed_non_resident = r0560_no_acct_aed_non_resident;
	}
	public BigDecimal getR0560_amount_aed_non_resident() {
		return r0560_amount_aed_non_resident;
	}
	public void setR0560_amount_aed_non_resident(BigDecimal r0560_amount_aed_non_resident) {
		this.r0560_amount_aed_non_resident = r0560_amount_aed_non_resident;
	}
	public BigDecimal getR0560_no_acct_fcy_non_resident() {
		return r0560_no_acct_fcy_non_resident;
	}
	public void setR0560_no_acct_fcy_non_resident(BigDecimal r0560_no_acct_fcy_non_resident) {
		this.r0560_no_acct_fcy_non_resident = r0560_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0560_amount_fcy_non_resident() {
		return r0560_amount_fcy_non_resident;
	}
	public void setR0560_amount_fcy_non_resident(BigDecimal r0560_amount_fcy_non_resident) {
		this.r0560_amount_fcy_non_resident = r0560_amount_fcy_non_resident;
	}
	public BigDecimal getR0560_total_amount() {
		return r0560_total_amount;
	}
	public void setR0560_total_amount(BigDecimal r0560_total_amount) {
		this.r0560_total_amount = r0560_total_amount;
	}
	public String getR0570_product() {
		return r0570_product;
	}
	public void setR0570_product(String r0570_product) {
		this.r0570_product = r0570_product;
	}
	public BigDecimal getR0570_no_acct_aed_resident() {
		return r0570_no_acct_aed_resident;
	}
	public void setR0570_no_acct_aed_resident(BigDecimal r0570_no_acct_aed_resident) {
		this.r0570_no_acct_aed_resident = r0570_no_acct_aed_resident;
	}
	public BigDecimal getR0570_amount_aed_resident() {
		return r0570_amount_aed_resident;
	}
	public void setR0570_amount_aed_resident(BigDecimal r0570_amount_aed_resident) {
		this.r0570_amount_aed_resident = r0570_amount_aed_resident;
	}
	public BigDecimal getR0570_no_acct_fcy_resident() {
		return r0570_no_acct_fcy_resident;
	}
	public void setR0570_no_acct_fcy_resident(BigDecimal r0570_no_acct_fcy_resident) {
		this.r0570_no_acct_fcy_resident = r0570_no_acct_fcy_resident;
	}
	public BigDecimal getR0570_amount_fcy_resident() {
		return r0570_amount_fcy_resident;
	}
	public void setR0570_amount_fcy_resident(BigDecimal r0570_amount_fcy_resident) {
		this.r0570_amount_fcy_resident = r0570_amount_fcy_resident;
	}
	public BigDecimal getR0570_no_acct_aed_non_resident() {
		return r0570_no_acct_aed_non_resident;
	}
	public void setR0570_no_acct_aed_non_resident(BigDecimal r0570_no_acct_aed_non_resident) {
		this.r0570_no_acct_aed_non_resident = r0570_no_acct_aed_non_resident;
	}
	public BigDecimal getR0570_amount_aed_non_resident() {
		return r0570_amount_aed_non_resident;
	}
	public void setR0570_amount_aed_non_resident(BigDecimal r0570_amount_aed_non_resident) {
		this.r0570_amount_aed_non_resident = r0570_amount_aed_non_resident;
	}
	public BigDecimal getR0570_no_acct_fcy_non_resident() {
		return r0570_no_acct_fcy_non_resident;
	}
	public void setR0570_no_acct_fcy_non_resident(BigDecimal r0570_no_acct_fcy_non_resident) {
		this.r0570_no_acct_fcy_non_resident = r0570_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0570_amount_fcy_non_resident() {
		return r0570_amount_fcy_non_resident;
	}
	public void setR0570_amount_fcy_non_resident(BigDecimal r0570_amount_fcy_non_resident) {
		this.r0570_amount_fcy_non_resident = r0570_amount_fcy_non_resident;
	}
	public BigDecimal getR0570_total_amount() {
		return r0570_total_amount;
	}
	public void setR0570_total_amount(BigDecimal r0570_total_amount) {
		this.r0570_total_amount = r0570_total_amount;
	}
	public String getR0580_product() {
		return r0580_product;
	}
	public void setR0580_product(String r0580_product) {
		this.r0580_product = r0580_product;
	}
	public BigDecimal getR0580_no_acct_aed_resident() {
		return r0580_no_acct_aed_resident;
	}
	public void setR0580_no_acct_aed_resident(BigDecimal r0580_no_acct_aed_resident) {
		this.r0580_no_acct_aed_resident = r0580_no_acct_aed_resident;
	}
	public BigDecimal getR0580_amount_aed_resident() {
		return r0580_amount_aed_resident;
	}
	public void setR0580_amount_aed_resident(BigDecimal r0580_amount_aed_resident) {
		this.r0580_amount_aed_resident = r0580_amount_aed_resident;
	}
	public BigDecimal getR0580_no_acct_fcy_resident() {
		return r0580_no_acct_fcy_resident;
	}
	public void setR0580_no_acct_fcy_resident(BigDecimal r0580_no_acct_fcy_resident) {
		this.r0580_no_acct_fcy_resident = r0580_no_acct_fcy_resident;
	}
	public BigDecimal getR0580_amount_fcy_resident() {
		return r0580_amount_fcy_resident;
	}
	public void setR0580_amount_fcy_resident(BigDecimal r0580_amount_fcy_resident) {
		this.r0580_amount_fcy_resident = r0580_amount_fcy_resident;
	}
	public BigDecimal getR0580_no_acct_aed_non_resident() {
		return r0580_no_acct_aed_non_resident;
	}
	public void setR0580_no_acct_aed_non_resident(BigDecimal r0580_no_acct_aed_non_resident) {
		this.r0580_no_acct_aed_non_resident = r0580_no_acct_aed_non_resident;
	}
	public BigDecimal getR0580_amount_aed_non_resident() {
		return r0580_amount_aed_non_resident;
	}
	public void setR0580_amount_aed_non_resident(BigDecimal r0580_amount_aed_non_resident) {
		this.r0580_amount_aed_non_resident = r0580_amount_aed_non_resident;
	}
	public BigDecimal getR0580_no_acct_fcy_non_resident() {
		return r0580_no_acct_fcy_non_resident;
	}
	public void setR0580_no_acct_fcy_non_resident(BigDecimal r0580_no_acct_fcy_non_resident) {
		this.r0580_no_acct_fcy_non_resident = r0580_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0580_amount_fcy_non_resident() {
		return r0580_amount_fcy_non_resident;
	}
	public void setR0580_amount_fcy_non_resident(BigDecimal r0580_amount_fcy_non_resident) {
		this.r0580_amount_fcy_non_resident = r0580_amount_fcy_non_resident;
	}
	public BigDecimal getR0580_total_amount() {
		return r0580_total_amount;
	}
	public void setR0580_total_amount(BigDecimal r0580_total_amount) {
		this.r0580_total_amount = r0580_total_amount;
	}
	public String getR0590_product() {
		return r0590_product;
	}
	public void setR0590_product(String r0590_product) {
		this.r0590_product = r0590_product;
	}
	public BigDecimal getR0590_no_acct_aed_resident() {
		return r0590_no_acct_aed_resident;
	}
	public void setR0590_no_acct_aed_resident(BigDecimal r0590_no_acct_aed_resident) {
		this.r0590_no_acct_aed_resident = r0590_no_acct_aed_resident;
	}
	public BigDecimal getR0590_amount_aed_resident() {
		return r0590_amount_aed_resident;
	}
	public void setR0590_amount_aed_resident(BigDecimal r0590_amount_aed_resident) {
		this.r0590_amount_aed_resident = r0590_amount_aed_resident;
	}
	public BigDecimal getR0590_no_acct_fcy_resident() {
		return r0590_no_acct_fcy_resident;
	}
	public void setR0590_no_acct_fcy_resident(BigDecimal r0590_no_acct_fcy_resident) {
		this.r0590_no_acct_fcy_resident = r0590_no_acct_fcy_resident;
	}
	public BigDecimal getR0590_amount_fcy_resident() {
		return r0590_amount_fcy_resident;
	}
	public void setR0590_amount_fcy_resident(BigDecimal r0590_amount_fcy_resident) {
		this.r0590_amount_fcy_resident = r0590_amount_fcy_resident;
	}
	public BigDecimal getR0590_no_acct_aed_non_resident() {
		return r0590_no_acct_aed_non_resident;
	}
	public void setR0590_no_acct_aed_non_resident(BigDecimal r0590_no_acct_aed_non_resident) {
		this.r0590_no_acct_aed_non_resident = r0590_no_acct_aed_non_resident;
	}
	public BigDecimal getR0590_amount_aed_non_resident() {
		return r0590_amount_aed_non_resident;
	}
	public void setR0590_amount_aed_non_resident(BigDecimal r0590_amount_aed_non_resident) {
		this.r0590_amount_aed_non_resident = r0590_amount_aed_non_resident;
	}
	public BigDecimal getR0590_no_acct_fcy_non_resident() {
		return r0590_no_acct_fcy_non_resident;
	}
	public void setR0590_no_acct_fcy_non_resident(BigDecimal r0590_no_acct_fcy_non_resident) {
		this.r0590_no_acct_fcy_non_resident = r0590_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0590_amount_fcy_non_resident() {
		return r0590_amount_fcy_non_resident;
	}
	public void setR0590_amount_fcy_non_resident(BigDecimal r0590_amount_fcy_non_resident) {
		this.r0590_amount_fcy_non_resident = r0590_amount_fcy_non_resident;
	}
	public BigDecimal getR0590_total_amount() {
		return r0590_total_amount;
	}
	public void setR0590_total_amount(BigDecimal r0590_total_amount) {
		this.r0590_total_amount = r0590_total_amount;
	}
	public String getR0600_product() {
		return r0600_product;
	}
	public void setR0600_product(String r0600_product) {
		this.r0600_product = r0600_product;
	}
	public BigDecimal getR0600_no_acct_aed_resident() {
		return r0600_no_acct_aed_resident;
	}
	public void setR0600_no_acct_aed_resident(BigDecimal r0600_no_acct_aed_resident) {
		this.r0600_no_acct_aed_resident = r0600_no_acct_aed_resident;
	}
	public BigDecimal getR0600_amount_aed_resident() {
		return r0600_amount_aed_resident;
	}
	public void setR0600_amount_aed_resident(BigDecimal r0600_amount_aed_resident) {
		this.r0600_amount_aed_resident = r0600_amount_aed_resident;
	}
	public BigDecimal getR0600_no_acct_fcy_resident() {
		return r0600_no_acct_fcy_resident;
	}
	public void setR0600_no_acct_fcy_resident(BigDecimal r0600_no_acct_fcy_resident) {
		this.r0600_no_acct_fcy_resident = r0600_no_acct_fcy_resident;
	}
	public BigDecimal getR0600_amount_fcy_resident() {
		return r0600_amount_fcy_resident;
	}
	public void setR0600_amount_fcy_resident(BigDecimal r0600_amount_fcy_resident) {
		this.r0600_amount_fcy_resident = r0600_amount_fcy_resident;
	}
	public BigDecimal getR0600_no_acct_aed_non_resident() {
		return r0600_no_acct_aed_non_resident;
	}
	public void setR0600_no_acct_aed_non_resident(BigDecimal r0600_no_acct_aed_non_resident) {
		this.r0600_no_acct_aed_non_resident = r0600_no_acct_aed_non_resident;
	}
	public BigDecimal getR0600_amount_aed_non_resident() {
		return r0600_amount_aed_non_resident;
	}
	public void setR0600_amount_aed_non_resident(BigDecimal r0600_amount_aed_non_resident) {
		this.r0600_amount_aed_non_resident = r0600_amount_aed_non_resident;
	}
	public BigDecimal getR0600_no_acct_fcy_non_resident() {
		return r0600_no_acct_fcy_non_resident;
	}
	public void setR0600_no_acct_fcy_non_resident(BigDecimal r0600_no_acct_fcy_non_resident) {
		this.r0600_no_acct_fcy_non_resident = r0600_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0600_amount_fcy_non_resident() {
		return r0600_amount_fcy_non_resident;
	}
	public void setR0600_amount_fcy_non_resident(BigDecimal r0600_amount_fcy_non_resident) {
		this.r0600_amount_fcy_non_resident = r0600_amount_fcy_non_resident;
	}
	public BigDecimal getR0600_total_amount() {
		return r0600_total_amount;
	}
	public void setR0600_total_amount(BigDecimal r0600_total_amount) {
		this.r0600_total_amount = r0600_total_amount;
	}
	public String getR0610_product() {
		return r0610_product;
	}
	public void setR0610_product(String r0610_product) {
		this.r0610_product = r0610_product;
	}
	public BigDecimal getR0610_no_acct_aed_resident() {
		return r0610_no_acct_aed_resident;
	}
	public void setR0610_no_acct_aed_resident(BigDecimal r0610_no_acct_aed_resident) {
		this.r0610_no_acct_aed_resident = r0610_no_acct_aed_resident;
	}
	public BigDecimal getR0610_amount_aed_resident() {
		return r0610_amount_aed_resident;
	}
	public void setR0610_amount_aed_resident(BigDecimal r0610_amount_aed_resident) {
		this.r0610_amount_aed_resident = r0610_amount_aed_resident;
	}
	public BigDecimal getR0610_no_acct_fcy_resident() {
		return r0610_no_acct_fcy_resident;
	}
	public void setR0610_no_acct_fcy_resident(BigDecimal r0610_no_acct_fcy_resident) {
		this.r0610_no_acct_fcy_resident = r0610_no_acct_fcy_resident;
	}
	public BigDecimal getR0610_amount_fcy_resident() {
		return r0610_amount_fcy_resident;
	}
	public void setR0610_amount_fcy_resident(BigDecimal r0610_amount_fcy_resident) {
		this.r0610_amount_fcy_resident = r0610_amount_fcy_resident;
	}
	public BigDecimal getR0610_no_acct_aed_non_resident() {
		return r0610_no_acct_aed_non_resident;
	}
	public void setR0610_no_acct_aed_non_resident(BigDecimal r0610_no_acct_aed_non_resident) {
		this.r0610_no_acct_aed_non_resident = r0610_no_acct_aed_non_resident;
	}
	public BigDecimal getR0610_amount_aed_non_resident() {
		return r0610_amount_aed_non_resident;
	}
	public void setR0610_amount_aed_non_resident(BigDecimal r0610_amount_aed_non_resident) {
		this.r0610_amount_aed_non_resident = r0610_amount_aed_non_resident;
	}
	public BigDecimal getR0610_no_acct_fcy_non_resident() {
		return r0610_no_acct_fcy_non_resident;
	}
	public void setR0610_no_acct_fcy_non_resident(BigDecimal r0610_no_acct_fcy_non_resident) {
		this.r0610_no_acct_fcy_non_resident = r0610_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0610_amount_fcy_non_resident() {
		return r0610_amount_fcy_non_resident;
	}
	public void setR0610_amount_fcy_non_resident(BigDecimal r0610_amount_fcy_non_resident) {
		this.r0610_amount_fcy_non_resident = r0610_amount_fcy_non_resident;
	}
	public BigDecimal getR0610_total_amount() {
		return r0610_total_amount;
	}
	public void setR0610_total_amount(BigDecimal r0610_total_amount) {
		this.r0610_total_amount = r0610_total_amount;
	}
	public String getR0620_product() {
		return r0620_product;
	}
	public void setR0620_product(String r0620_product) {
		this.r0620_product = r0620_product;
	}
	public BigDecimal getR0620_no_acct_aed_resident() {
		return r0620_no_acct_aed_resident;
	}
	public void setR0620_no_acct_aed_resident(BigDecimal r0620_no_acct_aed_resident) {
		this.r0620_no_acct_aed_resident = r0620_no_acct_aed_resident;
	}
	public BigDecimal getR0620_amount_aed_resident() {
		return r0620_amount_aed_resident;
	}
	public void setR0620_amount_aed_resident(BigDecimal r0620_amount_aed_resident) {
		this.r0620_amount_aed_resident = r0620_amount_aed_resident;
	}
	public BigDecimal getR0620_no_acct_fcy_resident() {
		return r0620_no_acct_fcy_resident;
	}
	public void setR0620_no_acct_fcy_resident(BigDecimal r0620_no_acct_fcy_resident) {
		this.r0620_no_acct_fcy_resident = r0620_no_acct_fcy_resident;
	}
	public BigDecimal getR0620_amount_fcy_resident() {
		return r0620_amount_fcy_resident;
	}
	public void setR0620_amount_fcy_resident(BigDecimal r0620_amount_fcy_resident) {
		this.r0620_amount_fcy_resident = r0620_amount_fcy_resident;
	}
	public BigDecimal getR0620_no_acct_aed_non_resident() {
		return r0620_no_acct_aed_non_resident;
	}
	public void setR0620_no_acct_aed_non_resident(BigDecimal r0620_no_acct_aed_non_resident) {
		this.r0620_no_acct_aed_non_resident = r0620_no_acct_aed_non_resident;
	}
	public BigDecimal getR0620_amount_aed_non_resident() {
		return r0620_amount_aed_non_resident;
	}
	public void setR0620_amount_aed_non_resident(BigDecimal r0620_amount_aed_non_resident) {
		this.r0620_amount_aed_non_resident = r0620_amount_aed_non_resident;
	}
	public BigDecimal getR0620_no_acct_fcy_non_resident() {
		return r0620_no_acct_fcy_non_resident;
	}
	public void setR0620_no_acct_fcy_non_resident(BigDecimal r0620_no_acct_fcy_non_resident) {
		this.r0620_no_acct_fcy_non_resident = r0620_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0620_amount_fcy_non_resident() {
		return r0620_amount_fcy_non_resident;
	}
	public void setR0620_amount_fcy_non_resident(BigDecimal r0620_amount_fcy_non_resident) {
		this.r0620_amount_fcy_non_resident = r0620_amount_fcy_non_resident;
	}
	public BigDecimal getR0620_total_amount() {
		return r0620_total_amount;
	}
	public void setR0620_total_amount(BigDecimal r0620_total_amount) {
		this.r0620_total_amount = r0620_total_amount;
	}
	public String getR0630_product() {
		return r0630_product;
	}
	public void setR0630_product(String r0630_product) {
		this.r0630_product = r0630_product;
	}
	public BigDecimal getR0630_no_acct_aed_resident() {
		return r0630_no_acct_aed_resident;
	}
	public void setR0630_no_acct_aed_resident(BigDecimal r0630_no_acct_aed_resident) {
		this.r0630_no_acct_aed_resident = r0630_no_acct_aed_resident;
	}
	public BigDecimal getR0630_amount_aed_resident() {
		return r0630_amount_aed_resident;
	}
	public void setR0630_amount_aed_resident(BigDecimal r0630_amount_aed_resident) {
		this.r0630_amount_aed_resident = r0630_amount_aed_resident;
	}
	public BigDecimal getR0630_no_acct_fcy_resident() {
		return r0630_no_acct_fcy_resident;
	}
	public void setR0630_no_acct_fcy_resident(BigDecimal r0630_no_acct_fcy_resident) {
		this.r0630_no_acct_fcy_resident = r0630_no_acct_fcy_resident;
	}
	public BigDecimal getR0630_amount_fcy_resident() {
		return r0630_amount_fcy_resident;
	}
	public void setR0630_amount_fcy_resident(BigDecimal r0630_amount_fcy_resident) {
		this.r0630_amount_fcy_resident = r0630_amount_fcy_resident;
	}
	public BigDecimal getR0630_no_acct_aed_non_resident() {
		return r0630_no_acct_aed_non_resident;
	}
	public void setR0630_no_acct_aed_non_resident(BigDecimal r0630_no_acct_aed_non_resident) {
		this.r0630_no_acct_aed_non_resident = r0630_no_acct_aed_non_resident;
	}
	public BigDecimal getR0630_amount_aed_non_resident() {
		return r0630_amount_aed_non_resident;
	}
	public void setR0630_amount_aed_non_resident(BigDecimal r0630_amount_aed_non_resident) {
		this.r0630_amount_aed_non_resident = r0630_amount_aed_non_resident;
	}
	public BigDecimal getR0630_no_acct_fcy_non_resident() {
		return r0630_no_acct_fcy_non_resident;
	}
	public void setR0630_no_acct_fcy_non_resident(BigDecimal r0630_no_acct_fcy_non_resident) {
		this.r0630_no_acct_fcy_non_resident = r0630_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0630_amount_fcy_non_resident() {
		return r0630_amount_fcy_non_resident;
	}
	public void setR0630_amount_fcy_non_resident(BigDecimal r0630_amount_fcy_non_resident) {
		this.r0630_amount_fcy_non_resident = r0630_amount_fcy_non_resident;
	}
	public BigDecimal getR0630_total_amount() {
		return r0630_total_amount;
	}
	public void setR0630_total_amount(BigDecimal r0630_total_amount) {
		this.r0630_total_amount = r0630_total_amount;
	}
	public String getR0640_product() {
		return r0640_product;
	}
	public void setR0640_product(String r0640_product) {
		this.r0640_product = r0640_product;
	}
	public BigDecimal getR0640_no_acct_aed_resident() {
		return r0640_no_acct_aed_resident;
	}
	public void setR0640_no_acct_aed_resident(BigDecimal r0640_no_acct_aed_resident) {
		this.r0640_no_acct_aed_resident = r0640_no_acct_aed_resident;
	}
	public BigDecimal getR0640_amount_aed_resident() {
		return r0640_amount_aed_resident;
	}
	public void setR0640_amount_aed_resident(BigDecimal r0640_amount_aed_resident) {
		this.r0640_amount_aed_resident = r0640_amount_aed_resident;
	}
	public BigDecimal getR0640_no_acct_fcy_resident() {
		return r0640_no_acct_fcy_resident;
	}
	public void setR0640_no_acct_fcy_resident(BigDecimal r0640_no_acct_fcy_resident) {
		this.r0640_no_acct_fcy_resident = r0640_no_acct_fcy_resident;
	}
	public BigDecimal getR0640_amount_fcy_resident() {
		return r0640_amount_fcy_resident;
	}
	public void setR0640_amount_fcy_resident(BigDecimal r0640_amount_fcy_resident) {
		this.r0640_amount_fcy_resident = r0640_amount_fcy_resident;
	}
	public BigDecimal getR0640_no_acct_aed_non_resident() {
		return r0640_no_acct_aed_non_resident;
	}
	public void setR0640_no_acct_aed_non_resident(BigDecimal r0640_no_acct_aed_non_resident) {
		this.r0640_no_acct_aed_non_resident = r0640_no_acct_aed_non_resident;
	}
	public BigDecimal getR0640_amount_aed_non_resident() {
		return r0640_amount_aed_non_resident;
	}
	public void setR0640_amount_aed_non_resident(BigDecimal r0640_amount_aed_non_resident) {
		this.r0640_amount_aed_non_resident = r0640_amount_aed_non_resident;
	}
	public BigDecimal getR0640_no_acct_fcy_non_resident() {
		return r0640_no_acct_fcy_non_resident;
	}
	public void setR0640_no_acct_fcy_non_resident(BigDecimal r0640_no_acct_fcy_non_resident) {
		this.r0640_no_acct_fcy_non_resident = r0640_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0640_amount_fcy_non_resident() {
		return r0640_amount_fcy_non_resident;
	}
	public void setR0640_amount_fcy_non_resident(BigDecimal r0640_amount_fcy_non_resident) {
		this.r0640_amount_fcy_non_resident = r0640_amount_fcy_non_resident;
	}
	public BigDecimal getR0640_total_amount() {
		return r0640_total_amount;
	}
	public void setR0640_total_amount(BigDecimal r0640_total_amount) {
		this.r0640_total_amount = r0640_total_amount;
	}
	public String getR0650_product() {
		return r0650_product;
	}
	public void setR0650_product(String r0650_product) {
		this.r0650_product = r0650_product;
	}
	public BigDecimal getR0650_no_acct_aed_resident() {
		return r0650_no_acct_aed_resident;
	}
	public void setR0650_no_acct_aed_resident(BigDecimal r0650_no_acct_aed_resident) {
		this.r0650_no_acct_aed_resident = r0650_no_acct_aed_resident;
	}
	public BigDecimal getR0650_amount_aed_resident() {
		return r0650_amount_aed_resident;
	}
	public void setR0650_amount_aed_resident(BigDecimal r0650_amount_aed_resident) {
		this.r0650_amount_aed_resident = r0650_amount_aed_resident;
	}
	public BigDecimal getR0650_no_acct_fcy_resident() {
		return r0650_no_acct_fcy_resident;
	}
	public void setR0650_no_acct_fcy_resident(BigDecimal r0650_no_acct_fcy_resident) {
		this.r0650_no_acct_fcy_resident = r0650_no_acct_fcy_resident;
	}
	public BigDecimal getR0650_amount_fcy_resident() {
		return r0650_amount_fcy_resident;
	}
	public void setR0650_amount_fcy_resident(BigDecimal r0650_amount_fcy_resident) {
		this.r0650_amount_fcy_resident = r0650_amount_fcy_resident;
	}
	public BigDecimal getR0650_no_acct_aed_non_resident() {
		return r0650_no_acct_aed_non_resident;
	}
	public void setR0650_no_acct_aed_non_resident(BigDecimal r0650_no_acct_aed_non_resident) {
		this.r0650_no_acct_aed_non_resident = r0650_no_acct_aed_non_resident;
	}
	public BigDecimal getR0650_amount_aed_non_resident() {
		return r0650_amount_aed_non_resident;
	}
	public void setR0650_amount_aed_non_resident(BigDecimal r0650_amount_aed_non_resident) {
		this.r0650_amount_aed_non_resident = r0650_amount_aed_non_resident;
	}
	public BigDecimal getR0650_no_acct_fcy_non_resident() {
		return r0650_no_acct_fcy_non_resident;
	}
	public void setR0650_no_acct_fcy_non_resident(BigDecimal r0650_no_acct_fcy_non_resident) {
		this.r0650_no_acct_fcy_non_resident = r0650_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0650_amount_fcy_non_resident() {
		return r0650_amount_fcy_non_resident;
	}
	public void setR0650_amount_fcy_non_resident(BigDecimal r0650_amount_fcy_non_resident) {
		this.r0650_amount_fcy_non_resident = r0650_amount_fcy_non_resident;
	}
	public BigDecimal getR0650_total_amount() {
		return r0650_total_amount;
	}
	public void setR0650_total_amount(BigDecimal r0650_total_amount) {
		this.r0650_total_amount = r0650_total_amount;
	}
	public String getR0660_product() {
		return r0660_product;
	}
	public void setR0660_product(String r0660_product) {
		this.r0660_product = r0660_product;
	}
	public BigDecimal getR0660_no_acct_aed_resident() {
		return r0660_no_acct_aed_resident;
	}
	public void setR0660_no_acct_aed_resident(BigDecimal r0660_no_acct_aed_resident) {
		this.r0660_no_acct_aed_resident = r0660_no_acct_aed_resident;
	}
	public BigDecimal getR0660_amount_aed_resident() {
		return r0660_amount_aed_resident;
	}
	public void setR0660_amount_aed_resident(BigDecimal r0660_amount_aed_resident) {
		this.r0660_amount_aed_resident = r0660_amount_aed_resident;
	}
	public BigDecimal getR0660_no_acct_fcy_resident() {
		return r0660_no_acct_fcy_resident;
	}
	public void setR0660_no_acct_fcy_resident(BigDecimal r0660_no_acct_fcy_resident) {
		this.r0660_no_acct_fcy_resident = r0660_no_acct_fcy_resident;
	}
	public BigDecimal getR0660_amount_fcy_resident() {
		return r0660_amount_fcy_resident;
	}
	public void setR0660_amount_fcy_resident(BigDecimal r0660_amount_fcy_resident) {
		this.r0660_amount_fcy_resident = r0660_amount_fcy_resident;
	}
	public BigDecimal getR0660_no_acct_aed_non_resident() {
		return r0660_no_acct_aed_non_resident;
	}
	public void setR0660_no_acct_aed_non_resident(BigDecimal r0660_no_acct_aed_non_resident) {
		this.r0660_no_acct_aed_non_resident = r0660_no_acct_aed_non_resident;
	}
	public BigDecimal getR0660_amount_aed_non_resident() {
		return r0660_amount_aed_non_resident;
	}
	public void setR0660_amount_aed_non_resident(BigDecimal r0660_amount_aed_non_resident) {
		this.r0660_amount_aed_non_resident = r0660_amount_aed_non_resident;
	}
	public BigDecimal getR0660_no_acct_fcy_non_resident() {
		return r0660_no_acct_fcy_non_resident;
	}
	public void setR0660_no_acct_fcy_non_resident(BigDecimal r0660_no_acct_fcy_non_resident) {
		this.r0660_no_acct_fcy_non_resident = r0660_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0660_amount_fcy_non_resident() {
		return r0660_amount_fcy_non_resident;
	}
	public void setR0660_amount_fcy_non_resident(BigDecimal r0660_amount_fcy_non_resident) {
		this.r0660_amount_fcy_non_resident = r0660_amount_fcy_non_resident;
	}
	public BigDecimal getR0660_total_amount() {
		return r0660_total_amount;
	}
	public void setR0660_total_amount(BigDecimal r0660_total_amount) {
		this.r0660_total_amount = r0660_total_amount;
	}
	public String getR0670_product() {
		return r0670_product;
	}
	public void setR0670_product(String r0670_product) {
		this.r0670_product = r0670_product;
	}
	public BigDecimal getR0670_no_acct_aed_resident() {
		return r0670_no_acct_aed_resident;
	}
	public void setR0670_no_acct_aed_resident(BigDecimal r0670_no_acct_aed_resident) {
		this.r0670_no_acct_aed_resident = r0670_no_acct_aed_resident;
	}
	public BigDecimal getR0670_amount_aed_resident() {
		return r0670_amount_aed_resident;
	}
	public void setR0670_amount_aed_resident(BigDecimal r0670_amount_aed_resident) {
		this.r0670_amount_aed_resident = r0670_amount_aed_resident;
	}
	public BigDecimal getR0670_no_acct_fcy_resident() {
		return r0670_no_acct_fcy_resident;
	}
	public void setR0670_no_acct_fcy_resident(BigDecimal r0670_no_acct_fcy_resident) {
		this.r0670_no_acct_fcy_resident = r0670_no_acct_fcy_resident;
	}
	public BigDecimal getR0670_amount_fcy_resident() {
		return r0670_amount_fcy_resident;
	}
	public void setR0670_amount_fcy_resident(BigDecimal r0670_amount_fcy_resident) {
		this.r0670_amount_fcy_resident = r0670_amount_fcy_resident;
	}
	public BigDecimal getR0670_no_acct_aed_non_resident() {
		return r0670_no_acct_aed_non_resident;
	}
	public void setR0670_no_acct_aed_non_resident(BigDecimal r0670_no_acct_aed_non_resident) {
		this.r0670_no_acct_aed_non_resident = r0670_no_acct_aed_non_resident;
	}
	public BigDecimal getR0670_amount_aed_non_resident() {
		return r0670_amount_aed_non_resident;
	}
	public void setR0670_amount_aed_non_resident(BigDecimal r0670_amount_aed_non_resident) {
		this.r0670_amount_aed_non_resident = r0670_amount_aed_non_resident;
	}
	public BigDecimal getR0670_no_acct_fcy_non_resident() {
		return r0670_no_acct_fcy_non_resident;
	}
	public void setR0670_no_acct_fcy_non_resident(BigDecimal r0670_no_acct_fcy_non_resident) {
		this.r0670_no_acct_fcy_non_resident = r0670_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0670_amount_fcy_non_resident() {
		return r0670_amount_fcy_non_resident;
	}
	public void setR0670_amount_fcy_non_resident(BigDecimal r0670_amount_fcy_non_resident) {
		this.r0670_amount_fcy_non_resident = r0670_amount_fcy_non_resident;
	}
	public BigDecimal getR0670_total_amount() {
		return r0670_total_amount;
	}
	public void setR0670_total_amount(BigDecimal r0670_total_amount) {
		this.r0670_total_amount = r0670_total_amount;
	}
	public String getR0680_product() {
		return r0680_product;
	}
	public void setR0680_product(String r0680_product) {
		this.r0680_product = r0680_product;
	}
	public BigDecimal getR0680_no_acct_aed_resident() {
		return r0680_no_acct_aed_resident;
	}
	public void setR0680_no_acct_aed_resident(BigDecimal r0680_no_acct_aed_resident) {
		this.r0680_no_acct_aed_resident = r0680_no_acct_aed_resident;
	}
	public BigDecimal getR0680_amount_aed_resident() {
		return r0680_amount_aed_resident;
	}
	public void setR0680_amount_aed_resident(BigDecimal r0680_amount_aed_resident) {
		this.r0680_amount_aed_resident = r0680_amount_aed_resident;
	}
	public BigDecimal getR0680_no_acct_fcy_resident() {
		return r0680_no_acct_fcy_resident;
	}
	public void setR0680_no_acct_fcy_resident(BigDecimal r0680_no_acct_fcy_resident) {
		this.r0680_no_acct_fcy_resident = r0680_no_acct_fcy_resident;
	}
	public BigDecimal getR0680_amount_fcy_resident() {
		return r0680_amount_fcy_resident;
	}
	public void setR0680_amount_fcy_resident(BigDecimal r0680_amount_fcy_resident) {
		this.r0680_amount_fcy_resident = r0680_amount_fcy_resident;
	}
	public BigDecimal getR0680_no_acct_aed_non_resident() {
		return r0680_no_acct_aed_non_resident;
	}
	public void setR0680_no_acct_aed_non_resident(BigDecimal r0680_no_acct_aed_non_resident) {
		this.r0680_no_acct_aed_non_resident = r0680_no_acct_aed_non_resident;
	}
	public BigDecimal getR0680_amount_aed_non_resident() {
		return r0680_amount_aed_non_resident;
	}
	public void setR0680_amount_aed_non_resident(BigDecimal r0680_amount_aed_non_resident) {
		this.r0680_amount_aed_non_resident = r0680_amount_aed_non_resident;
	}
	public BigDecimal getR0680_no_acct_fcy_non_resident() {
		return r0680_no_acct_fcy_non_resident;
	}
	public void setR0680_no_acct_fcy_non_resident(BigDecimal r0680_no_acct_fcy_non_resident) {
		this.r0680_no_acct_fcy_non_resident = r0680_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0680_amount_fcy_non_resident() {
		return r0680_amount_fcy_non_resident;
	}
	public void setR0680_amount_fcy_non_resident(BigDecimal r0680_amount_fcy_non_resident) {
		this.r0680_amount_fcy_non_resident = r0680_amount_fcy_non_resident;
	}
	public BigDecimal getR0680_total_amount() {
		return r0680_total_amount;
	}
	public void setR0680_total_amount(BigDecimal r0680_total_amount) {
		this.r0680_total_amount = r0680_total_amount;
	}
	public String getR0690_product() {
		return r0690_product;
	}
	public void setR0690_product(String r0690_product) {
		this.r0690_product = r0690_product;
	}
	public BigDecimal getR0690_no_acct_aed_resident() {
		return r0690_no_acct_aed_resident;
	}
	public void setR0690_no_acct_aed_resident(BigDecimal r0690_no_acct_aed_resident) {
		this.r0690_no_acct_aed_resident = r0690_no_acct_aed_resident;
	}
	public BigDecimal getR0690_amount_aed_resident() {
		return r0690_amount_aed_resident;
	}
	public void setR0690_amount_aed_resident(BigDecimal r0690_amount_aed_resident) {
		this.r0690_amount_aed_resident = r0690_amount_aed_resident;
	}
	public BigDecimal getR0690_no_acct_fcy_resident() {
		return r0690_no_acct_fcy_resident;
	}
	public void setR0690_no_acct_fcy_resident(BigDecimal r0690_no_acct_fcy_resident) {
		this.r0690_no_acct_fcy_resident = r0690_no_acct_fcy_resident;
	}
	public BigDecimal getR0690_amount_fcy_resident() {
		return r0690_amount_fcy_resident;
	}
	public void setR0690_amount_fcy_resident(BigDecimal r0690_amount_fcy_resident) {
		this.r0690_amount_fcy_resident = r0690_amount_fcy_resident;
	}
	public BigDecimal getR0690_no_acct_aed_non_resident() {
		return r0690_no_acct_aed_non_resident;
	}
	public void setR0690_no_acct_aed_non_resident(BigDecimal r0690_no_acct_aed_non_resident) {
		this.r0690_no_acct_aed_non_resident = r0690_no_acct_aed_non_resident;
	}
	public BigDecimal getR0690_amount_aed_non_resident() {
		return r0690_amount_aed_non_resident;
	}
	public void setR0690_amount_aed_non_resident(BigDecimal r0690_amount_aed_non_resident) {
		this.r0690_amount_aed_non_resident = r0690_amount_aed_non_resident;
	}
	public BigDecimal getR0690_no_acct_fcy_non_resident() {
		return r0690_no_acct_fcy_non_resident;
	}
	public void setR0690_no_acct_fcy_non_resident(BigDecimal r0690_no_acct_fcy_non_resident) {
		this.r0690_no_acct_fcy_non_resident = r0690_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0690_amount_fcy_non_resident() {
		return r0690_amount_fcy_non_resident;
	}
	public void setR0690_amount_fcy_non_resident(BigDecimal r0690_amount_fcy_non_resident) {
		this.r0690_amount_fcy_non_resident = r0690_amount_fcy_non_resident;
	}
	public BigDecimal getR0690_total_amount() {
		return r0690_total_amount;
	}
	public void setR0690_total_amount(BigDecimal r0690_total_amount) {
		this.r0690_total_amount = r0690_total_amount;
	}
	public String getR0700_product() {
		return r0700_product;
	}
	public void setR0700_product(String r0700_product) {
		this.r0700_product = r0700_product;
	}
	public BigDecimal getR0700_no_acct_aed_resident() {
		return r0700_no_acct_aed_resident;
	}
	public void setR0700_no_acct_aed_resident(BigDecimal r0700_no_acct_aed_resident) {
		this.r0700_no_acct_aed_resident = r0700_no_acct_aed_resident;
	}
	public BigDecimal getR0700_amount_aed_resident() {
		return r0700_amount_aed_resident;
	}
	public void setR0700_amount_aed_resident(BigDecimal r0700_amount_aed_resident) {
		this.r0700_amount_aed_resident = r0700_amount_aed_resident;
	}
	public BigDecimal getR0700_no_acct_fcy_resident() {
		return r0700_no_acct_fcy_resident;
	}
	public void setR0700_no_acct_fcy_resident(BigDecimal r0700_no_acct_fcy_resident) {
		this.r0700_no_acct_fcy_resident = r0700_no_acct_fcy_resident;
	}
	public BigDecimal getR0700_amount_fcy_resident() {
		return r0700_amount_fcy_resident;
	}
	public void setR0700_amount_fcy_resident(BigDecimal r0700_amount_fcy_resident) {
		this.r0700_amount_fcy_resident = r0700_amount_fcy_resident;
	}
	public BigDecimal getR0700_no_acct_aed_non_resident() {
		return r0700_no_acct_aed_non_resident;
	}
	public void setR0700_no_acct_aed_non_resident(BigDecimal r0700_no_acct_aed_non_resident) {
		this.r0700_no_acct_aed_non_resident = r0700_no_acct_aed_non_resident;
	}
	public BigDecimal getR0700_amount_aed_non_resident() {
		return r0700_amount_aed_non_resident;
	}
	public void setR0700_amount_aed_non_resident(BigDecimal r0700_amount_aed_non_resident) {
		this.r0700_amount_aed_non_resident = r0700_amount_aed_non_resident;
	}
	public BigDecimal getR0700_no_acct_fcy_non_resident() {
		return r0700_no_acct_fcy_non_resident;
	}
	public void setR0700_no_acct_fcy_non_resident(BigDecimal r0700_no_acct_fcy_non_resident) {
		this.r0700_no_acct_fcy_non_resident = r0700_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0700_amount_fcy_non_resident() {
		return r0700_amount_fcy_non_resident;
	}
	public void setR0700_amount_fcy_non_resident(BigDecimal r0700_amount_fcy_non_resident) {
		this.r0700_amount_fcy_non_resident = r0700_amount_fcy_non_resident;
	}
	public BigDecimal getR0700_total_amount() {
		return r0700_total_amount;
	}
	public void setR0700_total_amount(BigDecimal r0700_total_amount) {
		this.r0700_total_amount = r0700_total_amount;
	}
	public String getR0710_product() {
		return r0710_product;
	}
	public void setR0710_product(String r0710_product) {
		this.r0710_product = r0710_product;
	}
	public BigDecimal getR0710_no_acct_aed_resident() {
		return r0710_no_acct_aed_resident;
	}
	public void setR0710_no_acct_aed_resident(BigDecimal r0710_no_acct_aed_resident) {
		this.r0710_no_acct_aed_resident = r0710_no_acct_aed_resident;
	}
	public BigDecimal getR0710_amount_aed_resident() {
		return r0710_amount_aed_resident;
	}
	public void setR0710_amount_aed_resident(BigDecimal r0710_amount_aed_resident) {
		this.r0710_amount_aed_resident = r0710_amount_aed_resident;
	}
	public BigDecimal getR0710_no_acct_fcy_resident() {
		return r0710_no_acct_fcy_resident;
	}
	public void setR0710_no_acct_fcy_resident(BigDecimal r0710_no_acct_fcy_resident) {
		this.r0710_no_acct_fcy_resident = r0710_no_acct_fcy_resident;
	}
	public BigDecimal getR0710_amount_fcy_resident() {
		return r0710_amount_fcy_resident;
	}
	public void setR0710_amount_fcy_resident(BigDecimal r0710_amount_fcy_resident) {
		this.r0710_amount_fcy_resident = r0710_amount_fcy_resident;
	}
	public BigDecimal getR0710_no_acct_aed_non_resident() {
		return r0710_no_acct_aed_non_resident;
	}
	public void setR0710_no_acct_aed_non_resident(BigDecimal r0710_no_acct_aed_non_resident) {
		this.r0710_no_acct_aed_non_resident = r0710_no_acct_aed_non_resident;
	}
	public BigDecimal getR0710_amount_aed_non_resident() {
		return r0710_amount_aed_non_resident;
	}
	public void setR0710_amount_aed_non_resident(BigDecimal r0710_amount_aed_non_resident) {
		this.r0710_amount_aed_non_resident = r0710_amount_aed_non_resident;
	}
	public BigDecimal getR0710_no_acct_fcy_non_resident() {
		return r0710_no_acct_fcy_non_resident;
	}
	public void setR0710_no_acct_fcy_non_resident(BigDecimal r0710_no_acct_fcy_non_resident) {
		this.r0710_no_acct_fcy_non_resident = r0710_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0710_amount_fcy_non_resident() {
		return r0710_amount_fcy_non_resident;
	}
	public void setR0710_amount_fcy_non_resident(BigDecimal r0710_amount_fcy_non_resident) {
		this.r0710_amount_fcy_non_resident = r0710_amount_fcy_non_resident;
	}
	public BigDecimal getR0710_total_amount() {
		return r0710_total_amount;
	}
	public void setR0710_total_amount(BigDecimal r0710_total_amount) {
		this.r0710_total_amount = r0710_total_amount;
	}
	public String getR0720_product() {
		return r0720_product;
	}
	public void setR0720_product(String r0720_product) {
		this.r0720_product = r0720_product;
	}
	public BigDecimal getR0720_no_acct_aed_resident() {
		return r0720_no_acct_aed_resident;
	}
	public void setR0720_no_acct_aed_resident(BigDecimal r0720_no_acct_aed_resident) {
		this.r0720_no_acct_aed_resident = r0720_no_acct_aed_resident;
	}
	public BigDecimal getR0720_amount_aed_resident() {
		return r0720_amount_aed_resident;
	}
	public void setR0720_amount_aed_resident(BigDecimal r0720_amount_aed_resident) {
		this.r0720_amount_aed_resident = r0720_amount_aed_resident;
	}
	public BigDecimal getR0720_no_acct_fcy_resident() {
		return r0720_no_acct_fcy_resident;
	}
	public void setR0720_no_acct_fcy_resident(BigDecimal r0720_no_acct_fcy_resident) {
		this.r0720_no_acct_fcy_resident = r0720_no_acct_fcy_resident;
	}
	public BigDecimal getR0720_amount_fcy_resident() {
		return r0720_amount_fcy_resident;
	}
	public void setR0720_amount_fcy_resident(BigDecimal r0720_amount_fcy_resident) {
		this.r0720_amount_fcy_resident = r0720_amount_fcy_resident;
	}
	public BigDecimal getR0720_no_acct_aed_non_resident() {
		return r0720_no_acct_aed_non_resident;
	}
	public void setR0720_no_acct_aed_non_resident(BigDecimal r0720_no_acct_aed_non_resident) {
		this.r0720_no_acct_aed_non_resident = r0720_no_acct_aed_non_resident;
	}
	public BigDecimal getR0720_amount_aed_non_resident() {
		return r0720_amount_aed_non_resident;
	}
	public void setR0720_amount_aed_non_resident(BigDecimal r0720_amount_aed_non_resident) {
		this.r0720_amount_aed_non_resident = r0720_amount_aed_non_resident;
	}
	public BigDecimal getR0720_no_acct_fcy_non_resident() {
		return r0720_no_acct_fcy_non_resident;
	}
	public void setR0720_no_acct_fcy_non_resident(BigDecimal r0720_no_acct_fcy_non_resident) {
		this.r0720_no_acct_fcy_non_resident = r0720_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0720_amount_fcy_non_resident() {
		return r0720_amount_fcy_non_resident;
	}
	public void setR0720_amount_fcy_non_resident(BigDecimal r0720_amount_fcy_non_resident) {
		this.r0720_amount_fcy_non_resident = r0720_amount_fcy_non_resident;
	}
	public BigDecimal getR0720_total_amount() {
		return r0720_total_amount;
	}
	public void setR0720_total_amount(BigDecimal r0720_total_amount) {
		this.r0720_total_amount = r0720_total_amount;
	}
	public String getR0730_product() {
		return r0730_product;
	}
	public void setR0730_product(String r0730_product) {
		this.r0730_product = r0730_product;
	}
	public BigDecimal getR0730_no_acct_aed_resident() {
		return r0730_no_acct_aed_resident;
	}
	public void setR0730_no_acct_aed_resident(BigDecimal r0730_no_acct_aed_resident) {
		this.r0730_no_acct_aed_resident = r0730_no_acct_aed_resident;
	}
	public BigDecimal getR0730_amount_aed_resident() {
		return r0730_amount_aed_resident;
	}
	public void setR0730_amount_aed_resident(BigDecimal r0730_amount_aed_resident) {
		this.r0730_amount_aed_resident = r0730_amount_aed_resident;
	}
	public BigDecimal getR0730_no_acct_fcy_resident() {
		return r0730_no_acct_fcy_resident;
	}
	public void setR0730_no_acct_fcy_resident(BigDecimal r0730_no_acct_fcy_resident) {
		this.r0730_no_acct_fcy_resident = r0730_no_acct_fcy_resident;
	}
	public BigDecimal getR0730_amount_fcy_resident() {
		return r0730_amount_fcy_resident;
	}
	public void setR0730_amount_fcy_resident(BigDecimal r0730_amount_fcy_resident) {
		this.r0730_amount_fcy_resident = r0730_amount_fcy_resident;
	}
	public BigDecimal getR0730_no_acct_aed_non_resident() {
		return r0730_no_acct_aed_non_resident;
	}
	public void setR0730_no_acct_aed_non_resident(BigDecimal r0730_no_acct_aed_non_resident) {
		this.r0730_no_acct_aed_non_resident = r0730_no_acct_aed_non_resident;
	}
	public BigDecimal getR0730_amount_aed_non_resident() {
		return r0730_amount_aed_non_resident;
	}
	public void setR0730_amount_aed_non_resident(BigDecimal r0730_amount_aed_non_resident) {
		this.r0730_amount_aed_non_resident = r0730_amount_aed_non_resident;
	}
	public BigDecimal getR0730_no_acct_fcy_non_resident() {
		return r0730_no_acct_fcy_non_resident;
	}
	public void setR0730_no_acct_fcy_non_resident(BigDecimal r0730_no_acct_fcy_non_resident) {
		this.r0730_no_acct_fcy_non_resident = r0730_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0730_amount_fcy_non_resident() {
		return r0730_amount_fcy_non_resident;
	}
	public void setR0730_amount_fcy_non_resident(BigDecimal r0730_amount_fcy_non_resident) {
		this.r0730_amount_fcy_non_resident = r0730_amount_fcy_non_resident;
	}
	public BigDecimal getR0730_total_amount() {
		return r0730_total_amount;
	}
	public void setR0730_total_amount(BigDecimal r0730_total_amount) {
		this.r0730_total_amount = r0730_total_amount;
	}
	public String getR0740_product() {
		return r0740_product;
	}
	public void setR0740_product(String r0740_product) {
		this.r0740_product = r0740_product;
	}
	public BigDecimal getR0740_no_acct_aed_resident() {
		return r0740_no_acct_aed_resident;
	}
	public void setR0740_no_acct_aed_resident(BigDecimal r0740_no_acct_aed_resident) {
		this.r0740_no_acct_aed_resident = r0740_no_acct_aed_resident;
	}
	public BigDecimal getR0740_amount_aed_resident() {
		return r0740_amount_aed_resident;
	}
	public void setR0740_amount_aed_resident(BigDecimal r0740_amount_aed_resident) {
		this.r0740_amount_aed_resident = r0740_amount_aed_resident;
	}
	public BigDecimal getR0740_no_acct_fcy_resident() {
		return r0740_no_acct_fcy_resident;
	}
	public void setR0740_no_acct_fcy_resident(BigDecimal r0740_no_acct_fcy_resident) {
		this.r0740_no_acct_fcy_resident = r0740_no_acct_fcy_resident;
	}
	public BigDecimal getR0740_amount_fcy_resident() {
		return r0740_amount_fcy_resident;
	}
	public void setR0740_amount_fcy_resident(BigDecimal r0740_amount_fcy_resident) {
		this.r0740_amount_fcy_resident = r0740_amount_fcy_resident;
	}
	public BigDecimal getR0740_no_acct_aed_non_resident() {
		return r0740_no_acct_aed_non_resident;
	}
	public void setR0740_no_acct_aed_non_resident(BigDecimal r0740_no_acct_aed_non_resident) {
		this.r0740_no_acct_aed_non_resident = r0740_no_acct_aed_non_resident;
	}
	public BigDecimal getR0740_amount_aed_non_resident() {
		return r0740_amount_aed_non_resident;
	}
	public void setR0740_amount_aed_non_resident(BigDecimal r0740_amount_aed_non_resident) {
		this.r0740_amount_aed_non_resident = r0740_amount_aed_non_resident;
	}
	public BigDecimal getR0740_no_acct_fcy_non_resident() {
		return r0740_no_acct_fcy_non_resident;
	}
	public void setR0740_no_acct_fcy_non_resident(BigDecimal r0740_no_acct_fcy_non_resident) {
		this.r0740_no_acct_fcy_non_resident = r0740_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0740_amount_fcy_non_resident() {
		return r0740_amount_fcy_non_resident;
	}
	public void setR0740_amount_fcy_non_resident(BigDecimal r0740_amount_fcy_non_resident) {
		this.r0740_amount_fcy_non_resident = r0740_amount_fcy_non_resident;
	}
	public BigDecimal getR0740_total_amount() {
		return r0740_total_amount;
	}
	public void setR0740_total_amount(BigDecimal r0740_total_amount) {
		this.r0740_total_amount = r0740_total_amount;
	}
	public String getR0750_product() {
		return r0750_product;
	}
	public void setR0750_product(String r0750_product) {
		this.r0750_product = r0750_product;
	}
	public BigDecimal getR0750_no_acct_aed_resident() {
		return r0750_no_acct_aed_resident;
	}
	public void setR0750_no_acct_aed_resident(BigDecimal r0750_no_acct_aed_resident) {
		this.r0750_no_acct_aed_resident = r0750_no_acct_aed_resident;
	}
	public BigDecimal getR0750_amount_aed_resident() {
		return r0750_amount_aed_resident;
	}
	public void setR0750_amount_aed_resident(BigDecimal r0750_amount_aed_resident) {
		this.r0750_amount_aed_resident = r0750_amount_aed_resident;
	}
	public BigDecimal getR0750_no_acct_fcy_resident() {
		return r0750_no_acct_fcy_resident;
	}
	public void setR0750_no_acct_fcy_resident(BigDecimal r0750_no_acct_fcy_resident) {
		this.r0750_no_acct_fcy_resident = r0750_no_acct_fcy_resident;
	}
	public BigDecimal getR0750_amount_fcy_resident() {
		return r0750_amount_fcy_resident;
	}
	public void setR0750_amount_fcy_resident(BigDecimal r0750_amount_fcy_resident) {
		this.r0750_amount_fcy_resident = r0750_amount_fcy_resident;
	}
	public BigDecimal getR0750_no_acct_aed_non_resident() {
		return r0750_no_acct_aed_non_resident;
	}
	public void setR0750_no_acct_aed_non_resident(BigDecimal r0750_no_acct_aed_non_resident) {
		this.r0750_no_acct_aed_non_resident = r0750_no_acct_aed_non_resident;
	}
	public BigDecimal getR0750_amount_aed_non_resident() {
		return r0750_amount_aed_non_resident;
	}
	public void setR0750_amount_aed_non_resident(BigDecimal r0750_amount_aed_non_resident) {
		this.r0750_amount_aed_non_resident = r0750_amount_aed_non_resident;
	}
	public BigDecimal getR0750_no_acct_fcy_non_resident() {
		return r0750_no_acct_fcy_non_resident;
	}
	public void setR0750_no_acct_fcy_non_resident(BigDecimal r0750_no_acct_fcy_non_resident) {
		this.r0750_no_acct_fcy_non_resident = r0750_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0750_amount_fcy_non_resident() {
		return r0750_amount_fcy_non_resident;
	}
	public void setR0750_amount_fcy_non_resident(BigDecimal r0750_amount_fcy_non_resident) {
		this.r0750_amount_fcy_non_resident = r0750_amount_fcy_non_resident;
	}
	public BigDecimal getR0750_total_amount() {
		return r0750_total_amount;
	}
	public void setR0750_total_amount(BigDecimal r0750_total_amount) {
		this.r0750_total_amount = r0750_total_amount;
	}
	public String getR0760_product() {
		return r0760_product;
	}
	public void setR0760_product(String r0760_product) {
		this.r0760_product = r0760_product;
	}
	public BigDecimal getR0760_no_acct_aed_resident() {
		return r0760_no_acct_aed_resident;
	}
	public void setR0760_no_acct_aed_resident(BigDecimal r0760_no_acct_aed_resident) {
		this.r0760_no_acct_aed_resident = r0760_no_acct_aed_resident;
	}
	public BigDecimal getR0760_amount_aed_resident() {
		return r0760_amount_aed_resident;
	}
	public void setR0760_amount_aed_resident(BigDecimal r0760_amount_aed_resident) {
		this.r0760_amount_aed_resident = r0760_amount_aed_resident;
	}
	public BigDecimal getR0760_no_acct_fcy_resident() {
		return r0760_no_acct_fcy_resident;
	}
	public void setR0760_no_acct_fcy_resident(BigDecimal r0760_no_acct_fcy_resident) {
		this.r0760_no_acct_fcy_resident = r0760_no_acct_fcy_resident;
	}
	public BigDecimal getR0760_amount_fcy_resident() {
		return r0760_amount_fcy_resident;
	}
	public void setR0760_amount_fcy_resident(BigDecimal r0760_amount_fcy_resident) {
		this.r0760_amount_fcy_resident = r0760_amount_fcy_resident;
	}
	public BigDecimal getR0760_no_acct_aed_non_resident() {
		return r0760_no_acct_aed_non_resident;
	}
	public void setR0760_no_acct_aed_non_resident(BigDecimal r0760_no_acct_aed_non_resident) {
		this.r0760_no_acct_aed_non_resident = r0760_no_acct_aed_non_resident;
	}
	public BigDecimal getR0760_amount_aed_non_resident() {
		return r0760_amount_aed_non_resident;
	}
	public void setR0760_amount_aed_non_resident(BigDecimal r0760_amount_aed_non_resident) {
		this.r0760_amount_aed_non_resident = r0760_amount_aed_non_resident;
	}
	public BigDecimal getR0760_no_acct_fcy_non_resident() {
		return r0760_no_acct_fcy_non_resident;
	}
	public void setR0760_no_acct_fcy_non_resident(BigDecimal r0760_no_acct_fcy_non_resident) {
		this.r0760_no_acct_fcy_non_resident = r0760_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0760_amount_fcy_non_resident() {
		return r0760_amount_fcy_non_resident;
	}
	public void setR0760_amount_fcy_non_resident(BigDecimal r0760_amount_fcy_non_resident) {
		this.r0760_amount_fcy_non_resident = r0760_amount_fcy_non_resident;
	}
	public BigDecimal getR0760_total_amount() {
		return r0760_total_amount;
	}
	public void setR0760_total_amount(BigDecimal r0760_total_amount) {
		this.r0760_total_amount = r0760_total_amount;
	}
	public String getR0770_product() {
		return r0770_product;
	}
	public void setR0770_product(String r0770_product) {
		this.r0770_product = r0770_product;
	}
	public BigDecimal getR0770_no_acct_aed_resident() {
		return r0770_no_acct_aed_resident;
	}
	public void setR0770_no_acct_aed_resident(BigDecimal r0770_no_acct_aed_resident) {
		this.r0770_no_acct_aed_resident = r0770_no_acct_aed_resident;
	}
	public BigDecimal getR0770_amount_aed_resident() {
		return r0770_amount_aed_resident;
	}
	public void setR0770_amount_aed_resident(BigDecimal r0770_amount_aed_resident) {
		this.r0770_amount_aed_resident = r0770_amount_aed_resident;
	}
	public BigDecimal getR0770_no_acct_fcy_resident() {
		return r0770_no_acct_fcy_resident;
	}
	public void setR0770_no_acct_fcy_resident(BigDecimal r0770_no_acct_fcy_resident) {
		this.r0770_no_acct_fcy_resident = r0770_no_acct_fcy_resident;
	}
	public BigDecimal getR0770_amount_fcy_resident() {
		return r0770_amount_fcy_resident;
	}
	public void setR0770_amount_fcy_resident(BigDecimal r0770_amount_fcy_resident) {
		this.r0770_amount_fcy_resident = r0770_amount_fcy_resident;
	}
	public BigDecimal getR0770_no_acct_aed_non_resident() {
		return r0770_no_acct_aed_non_resident;
	}
	public void setR0770_no_acct_aed_non_resident(BigDecimal r0770_no_acct_aed_non_resident) {
		this.r0770_no_acct_aed_non_resident = r0770_no_acct_aed_non_resident;
	}
	public BigDecimal getR0770_amount_aed_non_resident() {
		return r0770_amount_aed_non_resident;
	}
	public void setR0770_amount_aed_non_resident(BigDecimal r0770_amount_aed_non_resident) {
		this.r0770_amount_aed_non_resident = r0770_amount_aed_non_resident;
	}
	public BigDecimal getR0770_no_acct_fcy_non_resident() {
		return r0770_no_acct_fcy_non_resident;
	}
	public void setR0770_no_acct_fcy_non_resident(BigDecimal r0770_no_acct_fcy_non_resident) {
		this.r0770_no_acct_fcy_non_resident = r0770_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0770_amount_fcy_non_resident() {
		return r0770_amount_fcy_non_resident;
	}
	public void setR0770_amount_fcy_non_resident(BigDecimal r0770_amount_fcy_non_resident) {
		this.r0770_amount_fcy_non_resident = r0770_amount_fcy_non_resident;
	}
	public BigDecimal getR0770_total_amount() {
		return r0770_total_amount;
	}
	public void setR0770_total_amount(BigDecimal r0770_total_amount) {
		this.r0770_total_amount = r0770_total_amount;
	}
	public String getR0780_product() {
		return r0780_product;
	}
	public void setR0780_product(String r0780_product) {
		this.r0780_product = r0780_product;
	}
	public BigDecimal getR0780_no_acct_aed_resident() {
		return r0780_no_acct_aed_resident;
	}
	public void setR0780_no_acct_aed_resident(BigDecimal r0780_no_acct_aed_resident) {
		this.r0780_no_acct_aed_resident = r0780_no_acct_aed_resident;
	}
	public BigDecimal getR0780_amount_aed_resident() {
		return r0780_amount_aed_resident;
	}
	public void setR0780_amount_aed_resident(BigDecimal r0780_amount_aed_resident) {
		this.r0780_amount_aed_resident = r0780_amount_aed_resident;
	}
	public BigDecimal getR0780_no_acct_fcy_resident() {
		return r0780_no_acct_fcy_resident;
	}
	public void setR0780_no_acct_fcy_resident(BigDecimal r0780_no_acct_fcy_resident) {
		this.r0780_no_acct_fcy_resident = r0780_no_acct_fcy_resident;
	}
	public BigDecimal getR0780_amount_fcy_resident() {
		return r0780_amount_fcy_resident;
	}
	public void setR0780_amount_fcy_resident(BigDecimal r0780_amount_fcy_resident) {
		this.r0780_amount_fcy_resident = r0780_amount_fcy_resident;
	}
	public BigDecimal getR0780_no_acct_aed_non_resident() {
		return r0780_no_acct_aed_non_resident;
	}
	public void setR0780_no_acct_aed_non_resident(BigDecimal r0780_no_acct_aed_non_resident) {
		this.r0780_no_acct_aed_non_resident = r0780_no_acct_aed_non_resident;
	}
	public BigDecimal getR0780_amount_aed_non_resident() {
		return r0780_amount_aed_non_resident;
	}
	public void setR0780_amount_aed_non_resident(BigDecimal r0780_amount_aed_non_resident) {
		this.r0780_amount_aed_non_resident = r0780_amount_aed_non_resident;
	}
	public BigDecimal getR0780_no_acct_fcy_non_resident() {
		return r0780_no_acct_fcy_non_resident;
	}
	public void setR0780_no_acct_fcy_non_resident(BigDecimal r0780_no_acct_fcy_non_resident) {
		this.r0780_no_acct_fcy_non_resident = r0780_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0780_amount_fcy_non_resident() {
		return r0780_amount_fcy_non_resident;
	}
	public void setR0780_amount_fcy_non_resident(BigDecimal r0780_amount_fcy_non_resident) {
		this.r0780_amount_fcy_non_resident = r0780_amount_fcy_non_resident;
	}
	public BigDecimal getR0780_total_amount() {
		return r0780_total_amount;
	}
	public void setR0780_total_amount(BigDecimal r0780_total_amount) {
		this.r0780_total_amount = r0780_total_amount;
	}
	public String getR0790_product() {
		return r0790_product;
	}
	public void setR0790_product(String r0790_product) {
		this.r0790_product = r0790_product;
	}
	public BigDecimal getR0790_no_acct_aed_resident() {
		return r0790_no_acct_aed_resident;
	}
	public void setR0790_no_acct_aed_resident(BigDecimal r0790_no_acct_aed_resident) {
		this.r0790_no_acct_aed_resident = r0790_no_acct_aed_resident;
	}
	public BigDecimal getR0790_amount_aed_resident() {
		return r0790_amount_aed_resident;
	}
	public void setR0790_amount_aed_resident(BigDecimal r0790_amount_aed_resident) {
		this.r0790_amount_aed_resident = r0790_amount_aed_resident;
	}
	public BigDecimal getR0790_no_acct_fcy_resident() {
		return r0790_no_acct_fcy_resident;
	}
	public void setR0790_no_acct_fcy_resident(BigDecimal r0790_no_acct_fcy_resident) {
		this.r0790_no_acct_fcy_resident = r0790_no_acct_fcy_resident;
	}
	public BigDecimal getR0790_amount_fcy_resident() {
		return r0790_amount_fcy_resident;
	}
	public void setR0790_amount_fcy_resident(BigDecimal r0790_amount_fcy_resident) {
		this.r0790_amount_fcy_resident = r0790_amount_fcy_resident;
	}
	public BigDecimal getR0790_no_acct_aed_non_resident() {
		return r0790_no_acct_aed_non_resident;
	}
	public void setR0790_no_acct_aed_non_resident(BigDecimal r0790_no_acct_aed_non_resident) {
		this.r0790_no_acct_aed_non_resident = r0790_no_acct_aed_non_resident;
	}
	public BigDecimal getR0790_amount_aed_non_resident() {
		return r0790_amount_aed_non_resident;
	}
	public void setR0790_amount_aed_non_resident(BigDecimal r0790_amount_aed_non_resident) {
		this.r0790_amount_aed_non_resident = r0790_amount_aed_non_resident;
	}
	public BigDecimal getR0790_no_acct_fcy_non_resident() {
		return r0790_no_acct_fcy_non_resident;
	}
	public void setR0790_no_acct_fcy_non_resident(BigDecimal r0790_no_acct_fcy_non_resident) {
		this.r0790_no_acct_fcy_non_resident = r0790_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0790_amount_fcy_non_resident() {
		return r0790_amount_fcy_non_resident;
	}
	public void setR0790_amount_fcy_non_resident(BigDecimal r0790_amount_fcy_non_resident) {
		this.r0790_amount_fcy_non_resident = r0790_amount_fcy_non_resident;
	}
	public BigDecimal getR0790_total_amount() {
		return r0790_total_amount;
	}
	public void setR0790_total_amount(BigDecimal r0790_total_amount) {
		this.r0790_total_amount = r0790_total_amount;
	}
	public String getR0800_product() {
		return r0800_product;
	}
	public void setR0800_product(String r0800_product) {
		this.r0800_product = r0800_product;
	}
	public BigDecimal getR0800_no_acct_aed_resident() {
		return r0800_no_acct_aed_resident;
	}
	public void setR0800_no_acct_aed_resident(BigDecimal r0800_no_acct_aed_resident) {
		this.r0800_no_acct_aed_resident = r0800_no_acct_aed_resident;
	}
	public BigDecimal getR0800_amount_aed_resident() {
		return r0800_amount_aed_resident;
	}
	public void setR0800_amount_aed_resident(BigDecimal r0800_amount_aed_resident) {
		this.r0800_amount_aed_resident = r0800_amount_aed_resident;
	}
	public BigDecimal getR0800_no_acct_fcy_resident() {
		return r0800_no_acct_fcy_resident;
	}
	public void setR0800_no_acct_fcy_resident(BigDecimal r0800_no_acct_fcy_resident) {
		this.r0800_no_acct_fcy_resident = r0800_no_acct_fcy_resident;
	}
	public BigDecimal getR0800_amount_fcy_resident() {
		return r0800_amount_fcy_resident;
	}
	public void setR0800_amount_fcy_resident(BigDecimal r0800_amount_fcy_resident) {
		this.r0800_amount_fcy_resident = r0800_amount_fcy_resident;
	}
	public BigDecimal getR0800_no_acct_aed_non_resident() {
		return r0800_no_acct_aed_non_resident;
	}
	public void setR0800_no_acct_aed_non_resident(BigDecimal r0800_no_acct_aed_non_resident) {
		this.r0800_no_acct_aed_non_resident = r0800_no_acct_aed_non_resident;
	}
	public BigDecimal getR0800_amount_aed_non_resident() {
		return r0800_amount_aed_non_resident;
	}
	public void setR0800_amount_aed_non_resident(BigDecimal r0800_amount_aed_non_resident) {
		this.r0800_amount_aed_non_resident = r0800_amount_aed_non_resident;
	}
	public BigDecimal getR0800_no_acct_fcy_non_resident() {
		return r0800_no_acct_fcy_non_resident;
	}
	public void setR0800_no_acct_fcy_non_resident(BigDecimal r0800_no_acct_fcy_non_resident) {
		this.r0800_no_acct_fcy_non_resident = r0800_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0800_amount_fcy_non_resident() {
		return r0800_amount_fcy_non_resident;
	}
	public void setR0800_amount_fcy_non_resident(BigDecimal r0800_amount_fcy_non_resident) {
		this.r0800_amount_fcy_non_resident = r0800_amount_fcy_non_resident;
	}
	public BigDecimal getR0800_total_amount() {
		return r0800_total_amount;
	}
	public void setR0800_total_amount(BigDecimal r0800_total_amount) {
		this.r0800_total_amount = r0800_total_amount;
	}
	public String getR0810_product() {
		return r0810_product;
	}
	public void setR0810_product(String r0810_product) {
		this.r0810_product = r0810_product;
	}
	public BigDecimal getR0810_no_acct_aed_resident() {
		return r0810_no_acct_aed_resident;
	}
	public void setR0810_no_acct_aed_resident(BigDecimal r0810_no_acct_aed_resident) {
		this.r0810_no_acct_aed_resident = r0810_no_acct_aed_resident;
	}
	public BigDecimal getR0810_amount_aed_resident() {
		return r0810_amount_aed_resident;
	}
	public void setR0810_amount_aed_resident(BigDecimal r0810_amount_aed_resident) {
		this.r0810_amount_aed_resident = r0810_amount_aed_resident;
	}
	public BigDecimal getR0810_no_acct_fcy_resident() {
		return r0810_no_acct_fcy_resident;
	}
	public void setR0810_no_acct_fcy_resident(BigDecimal r0810_no_acct_fcy_resident) {
		this.r0810_no_acct_fcy_resident = r0810_no_acct_fcy_resident;
	}
	public BigDecimal getR0810_amount_fcy_resident() {
		return r0810_amount_fcy_resident;
	}
	public void setR0810_amount_fcy_resident(BigDecimal r0810_amount_fcy_resident) {
		this.r0810_amount_fcy_resident = r0810_amount_fcy_resident;
	}
	public BigDecimal getR0810_no_acct_aed_non_resident() {
		return r0810_no_acct_aed_non_resident;
	}
	public void setR0810_no_acct_aed_non_resident(BigDecimal r0810_no_acct_aed_non_resident) {
		this.r0810_no_acct_aed_non_resident = r0810_no_acct_aed_non_resident;
	}
	public BigDecimal getR0810_amount_aed_non_resident() {
		return r0810_amount_aed_non_resident;
	}
	public void setR0810_amount_aed_non_resident(BigDecimal r0810_amount_aed_non_resident) {
		this.r0810_amount_aed_non_resident = r0810_amount_aed_non_resident;
	}
	public BigDecimal getR0810_no_acct_fcy_non_resident() {
		return r0810_no_acct_fcy_non_resident;
	}
	public void setR0810_no_acct_fcy_non_resident(BigDecimal r0810_no_acct_fcy_non_resident) {
		this.r0810_no_acct_fcy_non_resident = r0810_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0810_amount_fcy_non_resident() {
		return r0810_amount_fcy_non_resident;
	}
	public void setR0810_amount_fcy_non_resident(BigDecimal r0810_amount_fcy_non_resident) {
		this.r0810_amount_fcy_non_resident = r0810_amount_fcy_non_resident;
	}
	public BigDecimal getR0810_total_amount() {
		return r0810_total_amount;
	}
	public void setR0810_total_amount(BigDecimal r0810_total_amount) {
		this.r0810_total_amount = r0810_total_amount;
	}
	public String getR0820_product() {
		return r0820_product;
	}
	public void setR0820_product(String r0820_product) {
		this.r0820_product = r0820_product;
	}
	public BigDecimal getR0820_no_acct_aed_resident() {
		return r0820_no_acct_aed_resident;
	}
	public void setR0820_no_acct_aed_resident(BigDecimal r0820_no_acct_aed_resident) {
		this.r0820_no_acct_aed_resident = r0820_no_acct_aed_resident;
	}
	public BigDecimal getR0820_amount_aed_resident() {
		return r0820_amount_aed_resident;
	}
	public void setR0820_amount_aed_resident(BigDecimal r0820_amount_aed_resident) {
		this.r0820_amount_aed_resident = r0820_amount_aed_resident;
	}
	public BigDecimal getR0820_no_acct_fcy_resident() {
		return r0820_no_acct_fcy_resident;
	}
	public void setR0820_no_acct_fcy_resident(BigDecimal r0820_no_acct_fcy_resident) {
		this.r0820_no_acct_fcy_resident = r0820_no_acct_fcy_resident;
	}
	public BigDecimal getR0820_amount_fcy_resident() {
		return r0820_amount_fcy_resident;
	}
	public void setR0820_amount_fcy_resident(BigDecimal r0820_amount_fcy_resident) {
		this.r0820_amount_fcy_resident = r0820_amount_fcy_resident;
	}
	public BigDecimal getR0820_no_acct_aed_non_resident() {
		return r0820_no_acct_aed_non_resident;
	}
	public void setR0820_no_acct_aed_non_resident(BigDecimal r0820_no_acct_aed_non_resident) {
		this.r0820_no_acct_aed_non_resident = r0820_no_acct_aed_non_resident;
	}
	public BigDecimal getR0820_amount_aed_non_resident() {
		return r0820_amount_aed_non_resident;
	}
	public void setR0820_amount_aed_non_resident(BigDecimal r0820_amount_aed_non_resident) {
		this.r0820_amount_aed_non_resident = r0820_amount_aed_non_resident;
	}
	public BigDecimal getR0820_no_acct_fcy_non_resident() {
		return r0820_no_acct_fcy_non_resident;
	}
	public void setR0820_no_acct_fcy_non_resident(BigDecimal r0820_no_acct_fcy_non_resident) {
		this.r0820_no_acct_fcy_non_resident = r0820_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0820_amount_fcy_non_resident() {
		return r0820_amount_fcy_non_resident;
	}
	public void setR0820_amount_fcy_non_resident(BigDecimal r0820_amount_fcy_non_resident) {
		this.r0820_amount_fcy_non_resident = r0820_amount_fcy_non_resident;
	}
	public BigDecimal getR0820_total_amount() {
		return r0820_total_amount;
	}
	public void setR0820_total_amount(BigDecimal r0820_total_amount) {
		this.r0820_total_amount = r0820_total_amount;
	}
	public String getR0830_product() {
		return r0830_product;
	}
	public void setR0830_product(String r0830_product) {
		this.r0830_product = r0830_product;
	}
	public BigDecimal getR0830_no_acct_aed_resident() {
		return r0830_no_acct_aed_resident;
	}
	public void setR0830_no_acct_aed_resident(BigDecimal r0830_no_acct_aed_resident) {
		this.r0830_no_acct_aed_resident = r0830_no_acct_aed_resident;
	}
	public BigDecimal getR0830_amount_aed_resident() {
		return r0830_amount_aed_resident;
	}
	public void setR0830_amount_aed_resident(BigDecimal r0830_amount_aed_resident) {
		this.r0830_amount_aed_resident = r0830_amount_aed_resident;
	}
	public BigDecimal getR0830_no_acct_fcy_resident() {
		return r0830_no_acct_fcy_resident;
	}
	public void setR0830_no_acct_fcy_resident(BigDecimal r0830_no_acct_fcy_resident) {
		this.r0830_no_acct_fcy_resident = r0830_no_acct_fcy_resident;
	}
	public BigDecimal getR0830_amount_fcy_resident() {
		return r0830_amount_fcy_resident;
	}
	public void setR0830_amount_fcy_resident(BigDecimal r0830_amount_fcy_resident) {
		this.r0830_amount_fcy_resident = r0830_amount_fcy_resident;
	}
	public BigDecimal getR0830_no_acct_aed_non_resident() {
		return r0830_no_acct_aed_non_resident;
	}
	public void setR0830_no_acct_aed_non_resident(BigDecimal r0830_no_acct_aed_non_resident) {
		this.r0830_no_acct_aed_non_resident = r0830_no_acct_aed_non_resident;
	}
	public BigDecimal getR0830_amount_aed_non_resident() {
		return r0830_amount_aed_non_resident;
	}
	public void setR0830_amount_aed_non_resident(BigDecimal r0830_amount_aed_non_resident) {
		this.r0830_amount_aed_non_resident = r0830_amount_aed_non_resident;
	}
	public BigDecimal getR0830_no_acct_fcy_non_resident() {
		return r0830_no_acct_fcy_non_resident;
	}
	public void setR0830_no_acct_fcy_non_resident(BigDecimal r0830_no_acct_fcy_non_resident) {
		this.r0830_no_acct_fcy_non_resident = r0830_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0830_amount_fcy_non_resident() {
		return r0830_amount_fcy_non_resident;
	}
	public void setR0830_amount_fcy_non_resident(BigDecimal r0830_amount_fcy_non_resident) {
		this.r0830_amount_fcy_non_resident = r0830_amount_fcy_non_resident;
	}
	public BigDecimal getR0830_total_amount() {
		return r0830_total_amount;
	}
	public void setR0830_total_amount(BigDecimal r0830_total_amount) {
		this.r0830_total_amount = r0830_total_amount;
	}
	public String getR0840_product() {
		return r0840_product;
	}
	public void setR0840_product(String r0840_product) {
		this.r0840_product = r0840_product;
	}
	public BigDecimal getR0840_no_acct_aed_resident() {
		return r0840_no_acct_aed_resident;
	}
	public void setR0840_no_acct_aed_resident(BigDecimal r0840_no_acct_aed_resident) {
		this.r0840_no_acct_aed_resident = r0840_no_acct_aed_resident;
	}
	public BigDecimal getR0840_amount_aed_resident() {
		return r0840_amount_aed_resident;
	}
	public void setR0840_amount_aed_resident(BigDecimal r0840_amount_aed_resident) {
		this.r0840_amount_aed_resident = r0840_amount_aed_resident;
	}
	public BigDecimal getR0840_no_acct_fcy_resident() {
		return r0840_no_acct_fcy_resident;
	}
	public void setR0840_no_acct_fcy_resident(BigDecimal r0840_no_acct_fcy_resident) {
		this.r0840_no_acct_fcy_resident = r0840_no_acct_fcy_resident;
	}
	public BigDecimal getR0840_amount_fcy_resident() {
		return r0840_amount_fcy_resident;
	}
	public void setR0840_amount_fcy_resident(BigDecimal r0840_amount_fcy_resident) {
		this.r0840_amount_fcy_resident = r0840_amount_fcy_resident;
	}
	public BigDecimal getR0840_no_acct_aed_non_resident() {
		return r0840_no_acct_aed_non_resident;
	}
	public void setR0840_no_acct_aed_non_resident(BigDecimal r0840_no_acct_aed_non_resident) {
		this.r0840_no_acct_aed_non_resident = r0840_no_acct_aed_non_resident;
	}
	public BigDecimal getR0840_amount_aed_non_resident() {
		return r0840_amount_aed_non_resident;
	}
	public void setR0840_amount_aed_non_resident(BigDecimal r0840_amount_aed_non_resident) {
		this.r0840_amount_aed_non_resident = r0840_amount_aed_non_resident;
	}
	public BigDecimal getR0840_no_acct_fcy_non_resident() {
		return r0840_no_acct_fcy_non_resident;
	}
	public void setR0840_no_acct_fcy_non_resident(BigDecimal r0840_no_acct_fcy_non_resident) {
		this.r0840_no_acct_fcy_non_resident = r0840_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0840_amount_fcy_non_resident() {
		return r0840_amount_fcy_non_resident;
	}
	public void setR0840_amount_fcy_non_resident(BigDecimal r0840_amount_fcy_non_resident) {
		this.r0840_amount_fcy_non_resident = r0840_amount_fcy_non_resident;
	}
	public BigDecimal getR0840_total_amount() {
		return r0840_total_amount;
	}
	public void setR0840_total_amount(BigDecimal r0840_total_amount) {
		this.r0840_total_amount = r0840_total_amount;
	}
	public String getR0850_product() {
		return r0850_product;
	}
	public void setR0850_product(String r0850_product) {
		this.r0850_product = r0850_product;
	}
	public BigDecimal getR0850_no_acct_aed_resident() {
		return r0850_no_acct_aed_resident;
	}
	public void setR0850_no_acct_aed_resident(BigDecimal r0850_no_acct_aed_resident) {
		this.r0850_no_acct_aed_resident = r0850_no_acct_aed_resident;
	}
	public BigDecimal getR0850_amount_aed_resident() {
		return r0850_amount_aed_resident;
	}
	public void setR0850_amount_aed_resident(BigDecimal r0850_amount_aed_resident) {
		this.r0850_amount_aed_resident = r0850_amount_aed_resident;
	}
	public BigDecimal getR0850_no_acct_fcy_resident() {
		return r0850_no_acct_fcy_resident;
	}
	public void setR0850_no_acct_fcy_resident(BigDecimal r0850_no_acct_fcy_resident) {
		this.r0850_no_acct_fcy_resident = r0850_no_acct_fcy_resident;
	}
	public BigDecimal getR0850_amount_fcy_resident() {
		return r0850_amount_fcy_resident;
	}
	public void setR0850_amount_fcy_resident(BigDecimal r0850_amount_fcy_resident) {
		this.r0850_amount_fcy_resident = r0850_amount_fcy_resident;
	}
	public BigDecimal getR0850_no_acct_aed_non_resident() {
		return r0850_no_acct_aed_non_resident;
	}
	public void setR0850_no_acct_aed_non_resident(BigDecimal r0850_no_acct_aed_non_resident) {
		this.r0850_no_acct_aed_non_resident = r0850_no_acct_aed_non_resident;
	}
	public BigDecimal getR0850_amount_aed_non_resident() {
		return r0850_amount_aed_non_resident;
	}
	public void setR0850_amount_aed_non_resident(BigDecimal r0850_amount_aed_non_resident) {
		this.r0850_amount_aed_non_resident = r0850_amount_aed_non_resident;
	}
	public BigDecimal getR0850_no_acct_fcy_non_resident() {
		return r0850_no_acct_fcy_non_resident;
	}
	public void setR0850_no_acct_fcy_non_resident(BigDecimal r0850_no_acct_fcy_non_resident) {
		this.r0850_no_acct_fcy_non_resident = r0850_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0850_amount_fcy_non_resident() {
		return r0850_amount_fcy_non_resident;
	}
	public void setR0850_amount_fcy_non_resident(BigDecimal r0850_amount_fcy_non_resident) {
		this.r0850_amount_fcy_non_resident = r0850_amount_fcy_non_resident;
	}
	public BigDecimal getR0850_total_amount() {
		return r0850_total_amount;
	}
	public void setR0850_total_amount(BigDecimal r0850_total_amount) {
		this.r0850_total_amount = r0850_total_amount;
	}
	public String getR0860_product() {
		return r0860_product;
	}
	public void setR0860_product(String r0860_product) {
		this.r0860_product = r0860_product;
	}
	public BigDecimal getR0860_no_acct_aed_resident() {
		return r0860_no_acct_aed_resident;
	}
	public void setR0860_no_acct_aed_resident(BigDecimal r0860_no_acct_aed_resident) {
		this.r0860_no_acct_aed_resident = r0860_no_acct_aed_resident;
	}
	public BigDecimal getR0860_amount_aed_resident() {
		return r0860_amount_aed_resident;
	}
	public void setR0860_amount_aed_resident(BigDecimal r0860_amount_aed_resident) {
		this.r0860_amount_aed_resident = r0860_amount_aed_resident;
	}
	public BigDecimal getR0860_no_acct_fcy_resident() {
		return r0860_no_acct_fcy_resident;
	}
	public void setR0860_no_acct_fcy_resident(BigDecimal r0860_no_acct_fcy_resident) {
		this.r0860_no_acct_fcy_resident = r0860_no_acct_fcy_resident;
	}
	public BigDecimal getR0860_amount_fcy_resident() {
		return r0860_amount_fcy_resident;
	}
	public void setR0860_amount_fcy_resident(BigDecimal r0860_amount_fcy_resident) {
		this.r0860_amount_fcy_resident = r0860_amount_fcy_resident;
	}
	public BigDecimal getR0860_no_acct_aed_non_resident() {
		return r0860_no_acct_aed_non_resident;
	}
	public void setR0860_no_acct_aed_non_resident(BigDecimal r0860_no_acct_aed_non_resident) {
		this.r0860_no_acct_aed_non_resident = r0860_no_acct_aed_non_resident;
	}
	public BigDecimal getR0860_amount_aed_non_resident() {
		return r0860_amount_aed_non_resident;
	}
	public void setR0860_amount_aed_non_resident(BigDecimal r0860_amount_aed_non_resident) {
		this.r0860_amount_aed_non_resident = r0860_amount_aed_non_resident;
	}
	public BigDecimal getR0860_no_acct_fcy_non_resident() {
		return r0860_no_acct_fcy_non_resident;
	}
	public void setR0860_no_acct_fcy_non_resident(BigDecimal r0860_no_acct_fcy_non_resident) {
		this.r0860_no_acct_fcy_non_resident = r0860_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0860_amount_fcy_non_resident() {
		return r0860_amount_fcy_non_resident;
	}
	public void setR0860_amount_fcy_non_resident(BigDecimal r0860_amount_fcy_non_resident) {
		this.r0860_amount_fcy_non_resident = r0860_amount_fcy_non_resident;
	}
	public BigDecimal getR0860_total_amount() {
		return r0860_total_amount;
	}
	public void setR0860_total_amount(BigDecimal r0860_total_amount) {
		this.r0860_total_amount = r0860_total_amount;
	}
	public String getR0870_product() {
		return r0870_product;
	}
	public void setR0870_product(String r0870_product) {
		this.r0870_product = r0870_product;
	}
	public BigDecimal getR0870_no_acct_aed_resident() {
		return r0870_no_acct_aed_resident;
	}
	public void setR0870_no_acct_aed_resident(BigDecimal r0870_no_acct_aed_resident) {
		this.r0870_no_acct_aed_resident = r0870_no_acct_aed_resident;
	}
	public BigDecimal getR0870_amount_aed_resident() {
		return r0870_amount_aed_resident;
	}
	public void setR0870_amount_aed_resident(BigDecimal r0870_amount_aed_resident) {
		this.r0870_amount_aed_resident = r0870_amount_aed_resident;
	}
	public BigDecimal getR0870_no_acct_fcy_resident() {
		return r0870_no_acct_fcy_resident;
	}
	public void setR0870_no_acct_fcy_resident(BigDecimal r0870_no_acct_fcy_resident) {
		this.r0870_no_acct_fcy_resident = r0870_no_acct_fcy_resident;
	}
	public BigDecimal getR0870_amount_fcy_resident() {
		return r0870_amount_fcy_resident;
	}
	public void setR0870_amount_fcy_resident(BigDecimal r0870_amount_fcy_resident) {
		this.r0870_amount_fcy_resident = r0870_amount_fcy_resident;
	}
	public BigDecimal getR0870_no_acct_aed_non_resident() {
		return r0870_no_acct_aed_non_resident;
	}
	public void setR0870_no_acct_aed_non_resident(BigDecimal r0870_no_acct_aed_non_resident) {
		this.r0870_no_acct_aed_non_resident = r0870_no_acct_aed_non_resident;
	}
	public BigDecimal getR0870_amount_aed_non_resident() {
		return r0870_amount_aed_non_resident;
	}
	public void setR0870_amount_aed_non_resident(BigDecimal r0870_amount_aed_non_resident) {
		this.r0870_amount_aed_non_resident = r0870_amount_aed_non_resident;
	}
	public BigDecimal getR0870_no_acct_fcy_non_resident() {
		return r0870_no_acct_fcy_non_resident;
	}
	public void setR0870_no_acct_fcy_non_resident(BigDecimal r0870_no_acct_fcy_non_resident) {
		this.r0870_no_acct_fcy_non_resident = r0870_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0870_amount_fcy_non_resident() {
		return r0870_amount_fcy_non_resident;
	}
	public void setR0870_amount_fcy_non_resident(BigDecimal r0870_amount_fcy_non_resident) {
		this.r0870_amount_fcy_non_resident = r0870_amount_fcy_non_resident;
	}
	public BigDecimal getR0870_total_amount() {
		return r0870_total_amount;
	}
	public void setR0870_total_amount(BigDecimal r0870_total_amount) {
		this.r0870_total_amount = r0870_total_amount;
	}
	public String getR0880_product() {
		return r0880_product;
	}
	public void setR0880_product(String r0880_product) {
		this.r0880_product = r0880_product;
	}
	public BigDecimal getR0880_no_acct_aed_resident() {
		return r0880_no_acct_aed_resident;
	}
	public void setR0880_no_acct_aed_resident(BigDecimal r0880_no_acct_aed_resident) {
		this.r0880_no_acct_aed_resident = r0880_no_acct_aed_resident;
	}
	public BigDecimal getR0880_amount_aed_resident() {
		return r0880_amount_aed_resident;
	}
	public void setR0880_amount_aed_resident(BigDecimal r0880_amount_aed_resident) {
		this.r0880_amount_aed_resident = r0880_amount_aed_resident;
	}
	public BigDecimal getR0880_no_acct_fcy_resident() {
		return r0880_no_acct_fcy_resident;
	}
	public void setR0880_no_acct_fcy_resident(BigDecimal r0880_no_acct_fcy_resident) {
		this.r0880_no_acct_fcy_resident = r0880_no_acct_fcy_resident;
	}
	public BigDecimal getR0880_amount_fcy_resident() {
		return r0880_amount_fcy_resident;
	}
	public void setR0880_amount_fcy_resident(BigDecimal r0880_amount_fcy_resident) {
		this.r0880_amount_fcy_resident = r0880_amount_fcy_resident;
	}
	public BigDecimal getR0880_no_acct_aed_non_resident() {
		return r0880_no_acct_aed_non_resident;
	}
	public void setR0880_no_acct_aed_non_resident(BigDecimal r0880_no_acct_aed_non_resident) {
		this.r0880_no_acct_aed_non_resident = r0880_no_acct_aed_non_resident;
	}
	public BigDecimal getR0880_amount_aed_non_resident() {
		return r0880_amount_aed_non_resident;
	}
	public void setR0880_amount_aed_non_resident(BigDecimal r0880_amount_aed_non_resident) {
		this.r0880_amount_aed_non_resident = r0880_amount_aed_non_resident;
	}
	public BigDecimal getR0880_no_acct_fcy_non_resident() {
		return r0880_no_acct_fcy_non_resident;
	}
	public void setR0880_no_acct_fcy_non_resident(BigDecimal r0880_no_acct_fcy_non_resident) {
		this.r0880_no_acct_fcy_non_resident = r0880_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0880_amount_fcy_non_resident() {
		return r0880_amount_fcy_non_resident;
	}
	public void setR0880_amount_fcy_non_resident(BigDecimal r0880_amount_fcy_non_resident) {
		this.r0880_amount_fcy_non_resident = r0880_amount_fcy_non_resident;
	}
	public BigDecimal getR0880_total_amount() {
		return r0880_total_amount;
	}
	public void setR0880_total_amount(BigDecimal r0880_total_amount) {
		this.r0880_total_amount = r0880_total_amount;
	}
	public String getR0890_product() {
		return r0890_product;
	}
	public void setR0890_product(String r0890_product) {
		this.r0890_product = r0890_product;
	}
	public BigDecimal getR0890_no_acct_aed_resident() {
		return r0890_no_acct_aed_resident;
	}
	public void setR0890_no_acct_aed_resident(BigDecimal r0890_no_acct_aed_resident) {
		this.r0890_no_acct_aed_resident = r0890_no_acct_aed_resident;
	}
	public BigDecimal getR0890_amount_aed_resident() {
		return r0890_amount_aed_resident;
	}
	public void setR0890_amount_aed_resident(BigDecimal r0890_amount_aed_resident) {
		this.r0890_amount_aed_resident = r0890_amount_aed_resident;
	}
	public BigDecimal getR0890_no_acct_fcy_resident() {
		return r0890_no_acct_fcy_resident;
	}
	public void setR0890_no_acct_fcy_resident(BigDecimal r0890_no_acct_fcy_resident) {
		this.r0890_no_acct_fcy_resident = r0890_no_acct_fcy_resident;
	}
	public BigDecimal getR0890_amount_fcy_resident() {
		return r0890_amount_fcy_resident;
	}
	public void setR0890_amount_fcy_resident(BigDecimal r0890_amount_fcy_resident) {
		this.r0890_amount_fcy_resident = r0890_amount_fcy_resident;
	}
	public BigDecimal getR0890_no_acct_aed_non_resident() {
		return r0890_no_acct_aed_non_resident;
	}
	public void setR0890_no_acct_aed_non_resident(BigDecimal r0890_no_acct_aed_non_resident) {
		this.r0890_no_acct_aed_non_resident = r0890_no_acct_aed_non_resident;
	}
	public BigDecimal getR0890_amount_aed_non_resident() {
		return r0890_amount_aed_non_resident;
	}
	public void setR0890_amount_aed_non_resident(BigDecimal r0890_amount_aed_non_resident) {
		this.r0890_amount_aed_non_resident = r0890_amount_aed_non_resident;
	}
	public BigDecimal getR0890_no_acct_fcy_non_resident() {
		return r0890_no_acct_fcy_non_resident;
	}
	public void setR0890_no_acct_fcy_non_resident(BigDecimal r0890_no_acct_fcy_non_resident) {
		this.r0890_no_acct_fcy_non_resident = r0890_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0890_amount_fcy_non_resident() {
		return r0890_amount_fcy_non_resident;
	}
	public void setR0890_amount_fcy_non_resident(BigDecimal r0890_amount_fcy_non_resident) {
		this.r0890_amount_fcy_non_resident = r0890_amount_fcy_non_resident;
	}
	public BigDecimal getR0890_total_amount() {
		return r0890_total_amount;
	}
	public void setR0890_total_amount(BigDecimal r0890_total_amount) {
		this.r0890_total_amount = r0890_total_amount;
	}
	public String getR0900_product() {
		return r0900_product;
	}
	public void setR0900_product(String r0900_product) {
		this.r0900_product = r0900_product;
	}
	public BigDecimal getR0900_no_acct_aed_resident() {
		return r0900_no_acct_aed_resident;
	}
	public void setR0900_no_acct_aed_resident(BigDecimal r0900_no_acct_aed_resident) {
		this.r0900_no_acct_aed_resident = r0900_no_acct_aed_resident;
	}
	public BigDecimal getR0900_amount_aed_resident() {
		return r0900_amount_aed_resident;
	}
	public void setR0900_amount_aed_resident(BigDecimal r0900_amount_aed_resident) {
		this.r0900_amount_aed_resident = r0900_amount_aed_resident;
	}
	public BigDecimal getR0900_no_acct_fcy_resident() {
		return r0900_no_acct_fcy_resident;
	}
	public void setR0900_no_acct_fcy_resident(BigDecimal r0900_no_acct_fcy_resident) {
		this.r0900_no_acct_fcy_resident = r0900_no_acct_fcy_resident;
	}
	public BigDecimal getR0900_amount_fcy_resident() {
		return r0900_amount_fcy_resident;
	}
	public void setR0900_amount_fcy_resident(BigDecimal r0900_amount_fcy_resident) {
		this.r0900_amount_fcy_resident = r0900_amount_fcy_resident;
	}
	public BigDecimal getR0900_no_acct_aed_non_resident() {
		return r0900_no_acct_aed_non_resident;
	}
	public void setR0900_no_acct_aed_non_resident(BigDecimal r0900_no_acct_aed_non_resident) {
		this.r0900_no_acct_aed_non_resident = r0900_no_acct_aed_non_resident;
	}
	public BigDecimal getR0900_amount_aed_non_resident() {
		return r0900_amount_aed_non_resident;
	}
	public void setR0900_amount_aed_non_resident(BigDecimal r0900_amount_aed_non_resident) {
		this.r0900_amount_aed_non_resident = r0900_amount_aed_non_resident;
	}
	public BigDecimal getR0900_no_acct_fcy_non_resident() {
		return r0900_no_acct_fcy_non_resident;
	}
	public void setR0900_no_acct_fcy_non_resident(BigDecimal r0900_no_acct_fcy_non_resident) {
		this.r0900_no_acct_fcy_non_resident = r0900_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0900_amount_fcy_non_resident() {
		return r0900_amount_fcy_non_resident;
	}
	public void setR0900_amount_fcy_non_resident(BigDecimal r0900_amount_fcy_non_resident) {
		this.r0900_amount_fcy_non_resident = r0900_amount_fcy_non_resident;
	}
	public BigDecimal getR0900_total_amount() {
		return r0900_total_amount;
	}
	public void setR0900_total_amount(BigDecimal r0900_total_amount) {
		this.r0900_total_amount = r0900_total_amount;
	}
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
	public CBUAE_BRF1_2_Summary_Entity1() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
