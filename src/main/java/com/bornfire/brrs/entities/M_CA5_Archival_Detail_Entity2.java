package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_CA5_ARCHIVALTABLE_DETAIL2")
@IdClass(M_CA5_Archival_Detail2_PK.class)
public class M_CA5_Archival_Detail_Entity2 {
	
	private String r101_line_no;
    private String r101_note_holders;
    private String r101_name_of_instrument_programe;
    private String r101_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r101_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r101_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r101_effective_maturity_date_if_date;
    private BigDecimal r101_amount;
    private BigDecimal r101_amount_derecognised_p000;
    private BigDecimal r101_total_p000;

    private String r102_line_no;
    private String r102_note_holders;
    private String r102_name_of_instrument_programe;
    private String r102_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r102_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r102_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r102_effective_maturity_date_if_date;
    private BigDecimal r102_amount;
    private BigDecimal r102_amount_derecognised_p000;
    private BigDecimal r102_total_p000;

    private String r103_line_no;
    private String r103_note_holders;
    private String r103_name_of_instrument_programe;
    private String r103_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r103_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r103_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r103_effective_maturity_date_if_date;
    private BigDecimal r103_amount;
    private BigDecimal r103_amount_derecognised_p000;
    private BigDecimal r103_total_p000;

    private String r104_line_no;
    private String r104_note_holders;
    private String r104_name_of_instrument_programe;
    private String r104_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r104_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r104_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r104_effective_maturity_date_if_date;
    private BigDecimal r104_amount;
    private BigDecimal r104_amount_derecognised_p000;
    private BigDecimal r104_total_p000;

    private String r105_line_no;
    private String r105_note_holders;
    private String r105_name_of_instrument_programe;
    private String r105_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r105_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r105_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r105_effective_maturity_date_if_date;
    private BigDecimal r105_amount;
    private BigDecimal r105_amount_derecognised_p000;
    private BigDecimal r105_total_p000;

    private String r106_line_no;
    private String r106_note_holders;
    private String r106_name_of_instrument_programe;
    private String r106_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r106_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r106_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r106_effective_maturity_date_if_date;
    private BigDecimal r106_amount;
    private BigDecimal r106_amount_derecognised_p000;
    private BigDecimal r106_total_p000;

    private String r107_line_no;
    private String r107_note_holders;
    private String r107_name_of_instrument_programe;
    private String r107_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r107_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r107_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r107_effective_maturity_date_if_date;
    private BigDecimal r107_amount;
    private BigDecimal r107_amount_derecognised_p000;
    private BigDecimal r107_total_p000;

    private String r108_line_no;
    private String r108_note_holders;
    private String r108_name_of_instrument_programe;
    private String r108_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r108_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r108_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r108_effective_maturity_date_if_date;
    private BigDecimal r108_amount;
    private BigDecimal r108_amount_derecognised_p000;
    private BigDecimal r108_total_p000;

    private String r109_line_no;
    private String r109_note_holders;
    private String r109_name_of_instrument_programe;
    private String r109_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r109_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r109_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r109_effective_maturity_date_if_date;
    private BigDecimal r109_amount;
    private BigDecimal r109_amount_derecognised_p000;
    private BigDecimal r109_total_p000;

    private String r110_line_no;
    private String r110_note_holders;
    private String r110_name_of_instrument_programe;
    private String r110_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r110_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r110_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r110_effective_maturity_date_if_date;
    private BigDecimal r110_amount;
    private BigDecimal r110_amount_derecognised_p000;
    private BigDecimal r110_total_p000;

    private String r111_line_no;
    private String r111_note_holders;
    private String r111_name_of_instrument_programe;
    private String r111_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r111_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r111_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r111_effective_maturity_date_if_date;
    private BigDecimal r111_amount;
    private BigDecimal r111_amount_derecognised_p000;
    private BigDecimal r111_total_p000;

    private String r112_line_no;
    private String r112_note_holders;
    private String r112_name_of_instrument_programe;
    private String r112_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r112_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r112_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r112_effective_maturity_date_if_date;
    private BigDecimal r112_amount;
    private BigDecimal r112_amount_derecognised_p000;
    private BigDecimal r112_total_p000;

    private String r113_line_no;
    private String r113_note_holders;
    private String r113_name_of_instrument_programe;
    private String r113_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r113_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r113_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r113_effective_maturity_date_if_date;
    private BigDecimal r113_amount;
    private BigDecimal r113_amount_derecognised_p000;
    private BigDecimal r113_total_p000;

    private String r114_line_no;
    private String r114_note_holders;
    private String r114_name_of_instrument_programe;
    private String r114_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r114_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r114_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r114_effective_maturity_date_if_date;
    private BigDecimal r114_amount;
    private BigDecimal r114_amount_derecognised_p000;
    private BigDecimal r114_total_p000;

    private String r115_line_no;
    private String r115_note_holders;
    private String r115_name_of_instrument_programe;
    private String r115_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r115_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r115_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r115_effective_maturity_date_if_date;
    private BigDecimal r115_amount;
    private BigDecimal r115_amount_derecognised_p000;
    private BigDecimal r115_total_p000;

    private String r116_line_no;
    private String r116_note_holders;
    private String r116_name_of_instrument_programe;
    private String r116_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r116_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r116_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r116_effective_maturity_date_if_date;
    private BigDecimal r116_amount;
    private BigDecimal r116_amount_derecognised_p000;
    private BigDecimal r116_total_p000;

    private String r117_line_no;
    private String r117_note_holders;
    private String r117_name_of_instrument_programe;
    private String r117_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r117_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r117_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r117_effective_maturity_date_if_date;
    private BigDecimal r117_amount;
    private BigDecimal r117_amount_derecognised_p000;
    private BigDecimal r117_total_p000;

    private String r118_line_no;
    private String r118_note_holders;
    private String r118_name_of_instrument_programe;
    private String r118_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r118_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r118_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r118_effective_maturity_date_if_date;
    private BigDecimal r118_amount;
    private BigDecimal r118_amount_derecognised_p000;
    private BigDecimal r118_total_p000;

    private String r119_line_no;
    private String r119_note_holders;
    private String r119_name_of_instrument_programe;
    private String r119_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r119_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r119_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r119_effective_maturity_date_if_date;
    private BigDecimal r119_amount;
    private BigDecimal r119_amount_derecognised_p000;
    private BigDecimal r119_total_p000;

    private String r120_line_no;
    private String r120_note_holders;
    private String r120_name_of_instrument_programe;
    private String r120_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r120_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r120_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r120_effective_maturity_date_if_date;
    private BigDecimal r120_amount;
    private BigDecimal r120_amount_derecognised_p000;
    private BigDecimal r120_total_p000;

    private String r121_line_no;
    private String r121_note_holders;
    private String r121_name_of_instrument_programe;
    private String r121_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r121_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r121_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r121_effective_maturity_date_if_date;
    private BigDecimal r121_amount;
    private BigDecimal r121_amount_derecognised_p000;
    private BigDecimal r121_total_p000;

    private String r122_line_no;
    private String r122_note_holders;
    private String r122_name_of_instrument_programe;
    private String r122_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r122_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r122_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r122_effective_maturity_date_if_date;
    private BigDecimal r122_amount;
    private BigDecimal r122_amount_derecognised_p000;
    private BigDecimal r122_total_p000;

    private String r123_line_no;
    private String r123_note_holders;
    private String r123_name_of_instrument_programe;
    private String r123_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r123_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r123_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r123_effective_maturity_date_if_date;
    private BigDecimal r123_amount;
    private BigDecimal r123_amount_derecognised_p000;
    private BigDecimal r123_total_p000;

    private String r124_line_no;
    private String r124_note_holders;
    private String r124_name_of_instrument_programe;
    private String r124_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r124_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r124_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r124_effective_maturity_date_if_date;
    private BigDecimal r124_amount;
    private BigDecimal r124_amount_derecognised_p000;
    private BigDecimal r124_total_p000;

    private String r125_line_no;
    private String r125_note_holders;
    private String r125_name_of_instrument_programe;
    private String r125_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r125_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r125_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r125_effective_maturity_date_if_date;
    private BigDecimal r125_amount;
    private BigDecimal r125_amount_derecognised_p000;
    private BigDecimal r125_total_p000;

    private String r126_line_no;
    private String r126_note_holders;
    private String r126_name_of_instrument_programe;
    private String r126_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r126_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r126_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r126_effective_maturity_date_if_date;
    private BigDecimal r126_amount;
    private BigDecimal r126_amount_derecognised_p000;
    private BigDecimal r126_total_p000;

    private String r127_line_no;
    private String r127_note_holders;
    private String r127_name_of_instrument_programe;
    private String r127_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r127_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r127_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r127_effective_maturity_date_if_date;
    private BigDecimal r127_amount;
    private BigDecimal r127_amount_derecognised_p000;
    private BigDecimal r127_total_p000;

    private String r128_line_no;
    private String r128_note_holders;
    private String r128_name_of_instrument_programe;
    private String r128_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r128_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r128_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r128_effective_maturity_date_if_date;
    private BigDecimal r128_amount;
    private BigDecimal r128_amount_derecognised_p000;
    private BigDecimal r128_total_p000;

    private String r129_line_no;
    private String r129_note_holders;
    private String r129_name_of_instrument_programe;
    private String r129_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r129_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r129_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r129_effective_maturity_date_if_date;
    private BigDecimal r129_amount;
    private BigDecimal r129_amount_derecognised_p000;
    private BigDecimal r129_total_p000;

    private String r130_line_no;
    private String r130_note_holders;
    private String r130_name_of_instrument_programe;
    private String r130_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r130_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r130_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r130_effective_maturity_date_if_date;
    private BigDecimal r130_amount;
    private BigDecimal r130_amount_derecognised_p000;
    private BigDecimal r130_total_p000;

    private String r131_line_no;
    private String r131_note_holders;
    private String r131_name_of_instrument_programe;
    private String r131_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r131_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r131_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r131_effective_maturity_date_if_date;
    private BigDecimal r131_amount;
    private BigDecimal r131_amount_derecognised_p000;
    private BigDecimal r131_total_p000;

    private String r132_line_no;
    private String r132_note_holders;
    private String r132_name_of_instrument_programe;
    private String r132_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r132_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r132_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r132_effective_maturity_date_if_date;
    private BigDecimal r132_amount;
    private BigDecimal r132_amount_derecognised_p000;
    private BigDecimal r132_total_p000;

    private String r133_line_no;
    private String r133_note_holders;
    private String r133_name_of_instrument_programe;
    private String r133_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r133_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r133_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r133_effective_maturity_date_if_date;
    private BigDecimal r133_amount;
    private BigDecimal r133_amount_derecognised_p000;
    private BigDecimal r133_total_p000;

    private String r134_line_no;
    private String r134_note_holders;
    private String r134_name_of_instrument_programe;
    private String r134_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r134_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r134_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r134_effective_maturity_date_if_date;
    private BigDecimal r134_amount;
    private BigDecimal r134_amount_derecognised_p000;
    private BigDecimal r134_total_p000;

    private String r135_line_no;
    private String r135_note_holders;
    private String r135_name_of_instrument_programe;
    private String r135_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r135_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r135_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r135_effective_maturity_date_if_date;
    private BigDecimal r135_amount;
    private BigDecimal r135_amount_derecognised_p000;
    private BigDecimal r135_total_p000;

    private String r136_line_no;
    private String r136_note_holders;
    private String r136_name_of_instrument_programe;
    private String r136_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r136_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r136_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r136_effective_maturity_date_if_date;
    private BigDecimal r136_amount;
    private BigDecimal r136_amount_derecognised_p000;
    private BigDecimal r136_total_p000;

    private String r137_line_no;
    private String r137_note_holders;
    private String r137_name_of_instrument_programe;
    private String r137_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r137_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r137_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r137_effective_maturity_date_if_date;
    private BigDecimal r137_amount;
    private BigDecimal r137_amount_derecognised_p000;
    private BigDecimal r137_total_p000;

    private String r138_line_no;
    private String r138_note_holders;
    private String r138_name_of_instrument_programe;
    private String r138_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r138_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r138_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r138_effective_maturity_date_if_date;
    private BigDecimal r138_amount;
    private BigDecimal r138_amount_derecognised_p000;
    private BigDecimal r138_total_p000;

    private String r139_line_no;
    private String r139_note_holders;
    private String r139_name_of_instrument_programe;
    private String r139_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r139_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r139_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r139_effective_maturity_date_if_date;
    private BigDecimal r139_amount;
    private BigDecimal r139_amount_derecognised_p000;
    private BigDecimal r139_total_p000;

    private String r140_line_no;
    private String r140_note_holders;
    private String r140_name_of_instrument_programe;
    private String r140_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r140_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r140_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r140_effective_maturity_date_if_date;
    private BigDecimal r140_amount;
    private BigDecimal r140_amount_derecognised_p000;
    private BigDecimal r140_total_p000;

    private String r141_line_no;
    private String r141_note_holders;
    private String r141_name_of_instrument_programe;
    private String r141_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r141_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r141_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r141_effective_maturity_date_if_date;
    private BigDecimal r141_amount;
    private BigDecimal r141_amount_derecognised_p000;
    private BigDecimal r141_total_p000;

    private String r142_line_no;
    private String r142_note_holders;
    private String r142_name_of_instrument_programe;
    private String r142_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r142_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r142_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r142_effective_maturity_date_if_date;
    private BigDecimal r142_amount;
    private BigDecimal r142_amount_derecognised_p000;
    private BigDecimal r142_total_p000;

    private String r143_line_no;
    private String r143_note_holders;
    private String r143_name_of_instrument_programe;
    private String r143_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r143_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r143_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r143_effective_maturity_date_if_date;
    private BigDecimal r143_amount;
    private BigDecimal r143_amount_derecognised_p000;
    private BigDecimal r143_total_p000;

    private String r144_line_no;
    private String r144_note_holders;
    private String r144_name_of_instrument_programe;
    private String r144_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r144_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r144_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r144_effective_maturity_date_if_date;
    private BigDecimal r144_amount;
    private BigDecimal r144_amount_derecognised_p000;
    private BigDecimal r144_total_p000;

    private String r145_line_no;
    private String r145_note_holders;
    private String r145_name_of_instrument_programe;
    private String r145_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r145_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r145_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r145_effective_maturity_date_if_date;
    private BigDecimal r145_amount;
    private BigDecimal r145_amount_derecognised_p000;
    private BigDecimal r145_total_p000;

    private String r146_line_no;
    private String r146_note_holders;
    private String r146_name_of_instrument_programe;
    private String r146_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r146_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r146_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r146_effective_maturity_date_if_date;
    private BigDecimal r146_amount;
    private BigDecimal r146_amount_derecognised_p000;
    private BigDecimal r146_total_p000;

    private String r147_line_no;
    private String r147_note_holders;
    private String r147_name_of_instrument_programe;
    private String r147_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r147_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r147_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r147_effective_maturity_date_if_date;
    private BigDecimal r147_amount;
    private BigDecimal r147_amount_derecognised_p000;
    private BigDecimal r147_total_p000;

    private String r148_line_no;
    private String r148_note_holders;
    private String r148_name_of_instrument_programe;
    private String r148_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r148_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r148_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r148_effective_maturity_date_if_date;
    private BigDecimal r148_amount;
    private BigDecimal r148_amount_derecognised_p000;
    private BigDecimal r148_total_p000;

    private String r149_line_no;
    private String r149_note_holders;
    private String r149_name_of_instrument_programe;
    private String r149_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r149_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r149_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r149_effective_maturity_date_if_date;
    private BigDecimal r149_amount;
    private BigDecimal r149_amount_derecognised_p000;
    private BigDecimal r149_total_p000;

    private String r150_line_no;
    private String r150_note_holders;
    private String r150_name_of_instrument_programe;
    private String r150_issuing_entity_if_issued_throughan_spv;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r150_issuance_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r150_contractual_maturity_date;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date r150_effective_maturity_date_if_date;
    private BigDecimal r150_amount;
    private BigDecimal r150_amount_derecognised_p000;
    private BigDecimal r150_total_p000;


	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;
	public String getR101_line_no() {
		return r101_line_no;
	}
	public void setR101_line_no(String r101_line_no) {
		this.r101_line_no = r101_line_no;
	}
	public String getR101_note_holders() {
		return r101_note_holders;
	}
	public void setR101_note_holders(String r101_note_holders) {
		this.r101_note_holders = r101_note_holders;
	}
	public String getR101_name_of_instrument_programe() {
		return r101_name_of_instrument_programe;
	}
	public void setR101_name_of_instrument_programe(String r101_name_of_instrument_programe) {
		this.r101_name_of_instrument_programe = r101_name_of_instrument_programe;
	}
	public String getR101_issuing_entity_if_issued_throughan_spv() {
		return r101_issuing_entity_if_issued_throughan_spv;
	}
	public void setR101_issuing_entity_if_issued_throughan_spv(String r101_issuing_entity_if_issued_throughan_spv) {
		this.r101_issuing_entity_if_issued_throughan_spv = r101_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR101_issuance_date() {
		return r101_issuance_date;
	}
	public void setR101_issuance_date(Date r101_issuance_date) {
		this.r101_issuance_date = r101_issuance_date;
	}
	public Date getR101_contractual_maturity_date() {
		return r101_contractual_maturity_date;
	}
	public void setR101_contractual_maturity_date(Date r101_contractual_maturity_date) {
		this.r101_contractual_maturity_date = r101_contractual_maturity_date;
	}
	public Date getR101_effective_maturity_date_if_date() {
		return r101_effective_maturity_date_if_date;
	}
	public void setR101_effective_maturity_date_if_date(Date r101_effective_maturity_date_if_date) {
		this.r101_effective_maturity_date_if_date = r101_effective_maturity_date_if_date;
	}
	public BigDecimal getR101_amount() {
		return r101_amount;
	}
	public void setR101_amount(BigDecimal r101_amount) {
		this.r101_amount = r101_amount;
	}
	public BigDecimal getR101_amount_derecognised_p000() {
		return r101_amount_derecognised_p000;
	}
	public void setR101_amount_derecognised_p000(BigDecimal r101_amount_derecognised_p000) {
		this.r101_amount_derecognised_p000 = r101_amount_derecognised_p000;
	}
	public BigDecimal getR101_total_p000() {
		return r101_total_p000;
	}
	public void setR101_total_p000(BigDecimal r101_total_p000) {
		this.r101_total_p000 = r101_total_p000;
	}
	public String getR102_line_no() {
		return r102_line_no;
	}
	public void setR102_line_no(String r102_line_no) {
		this.r102_line_no = r102_line_no;
	}
	public String getR102_note_holders() {
		return r102_note_holders;
	}
	public void setR102_note_holders(String r102_note_holders) {
		this.r102_note_holders = r102_note_holders;
	}
	public String getR102_name_of_instrument_programe() {
		return r102_name_of_instrument_programe;
	}
	public void setR102_name_of_instrument_programe(String r102_name_of_instrument_programe) {
		this.r102_name_of_instrument_programe = r102_name_of_instrument_programe;
	}
	public String getR102_issuing_entity_if_issued_throughan_spv() {
		return r102_issuing_entity_if_issued_throughan_spv;
	}
	public void setR102_issuing_entity_if_issued_throughan_spv(String r102_issuing_entity_if_issued_throughan_spv) {
		this.r102_issuing_entity_if_issued_throughan_spv = r102_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR102_issuance_date() {
		return r102_issuance_date;
	}
	public void setR102_issuance_date(Date r102_issuance_date) {
		this.r102_issuance_date = r102_issuance_date;
	}
	public Date getR102_contractual_maturity_date() {
		return r102_contractual_maturity_date;
	}
	public void setR102_contractual_maturity_date(Date r102_contractual_maturity_date) {
		this.r102_contractual_maturity_date = r102_contractual_maturity_date;
	}
	public Date getR102_effective_maturity_date_if_date() {
		return r102_effective_maturity_date_if_date;
	}
	public void setR102_effective_maturity_date_if_date(Date r102_effective_maturity_date_if_date) {
		this.r102_effective_maturity_date_if_date = r102_effective_maturity_date_if_date;
	}
	public BigDecimal getR102_amount() {
		return r102_amount;
	}
	public void setR102_amount(BigDecimal r102_amount) {
		this.r102_amount = r102_amount;
	}
	public BigDecimal getR102_amount_derecognised_p000() {
		return r102_amount_derecognised_p000;
	}
	public void setR102_amount_derecognised_p000(BigDecimal r102_amount_derecognised_p000) {
		this.r102_amount_derecognised_p000 = r102_amount_derecognised_p000;
	}
	public BigDecimal getR102_total_p000() {
		return r102_total_p000;
	}
	public void setR102_total_p000(BigDecimal r102_total_p000) {
		this.r102_total_p000 = r102_total_p000;
	}
	public String getR103_line_no() {
		return r103_line_no;
	}
	public void setR103_line_no(String r103_line_no) {
		this.r103_line_no = r103_line_no;
	}
	public String getR103_note_holders() {
		return r103_note_holders;
	}
	public void setR103_note_holders(String r103_note_holders) {
		this.r103_note_holders = r103_note_holders;
	}
	public String getR103_name_of_instrument_programe() {
		return r103_name_of_instrument_programe;
	}
	public void setR103_name_of_instrument_programe(String r103_name_of_instrument_programe) {
		this.r103_name_of_instrument_programe = r103_name_of_instrument_programe;
	}
	public String getR103_issuing_entity_if_issued_throughan_spv() {
		return r103_issuing_entity_if_issued_throughan_spv;
	}
	public void setR103_issuing_entity_if_issued_throughan_spv(String r103_issuing_entity_if_issued_throughan_spv) {
		this.r103_issuing_entity_if_issued_throughan_spv = r103_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR103_issuance_date() {
		return r103_issuance_date;
	}
	public void setR103_issuance_date(Date r103_issuance_date) {
		this.r103_issuance_date = r103_issuance_date;
	}
	public Date getR103_contractual_maturity_date() {
		return r103_contractual_maturity_date;
	}
	public void setR103_contractual_maturity_date(Date r103_contractual_maturity_date) {
		this.r103_contractual_maturity_date = r103_contractual_maturity_date;
	}
	public Date getR103_effective_maturity_date_if_date() {
		return r103_effective_maturity_date_if_date;
	}
	public void setR103_effective_maturity_date_if_date(Date r103_effective_maturity_date_if_date) {
		this.r103_effective_maturity_date_if_date = r103_effective_maturity_date_if_date;
	}
	public BigDecimal getR103_amount() {
		return r103_amount;
	}
	public void setR103_amount(BigDecimal r103_amount) {
		this.r103_amount = r103_amount;
	}
	public BigDecimal getR103_amount_derecognised_p000() {
		return r103_amount_derecognised_p000;
	}
	public void setR103_amount_derecognised_p000(BigDecimal r103_amount_derecognised_p000) {
		this.r103_amount_derecognised_p000 = r103_amount_derecognised_p000;
	}
	public BigDecimal getR103_total_p000() {
		return r103_total_p000;
	}
	public void setR103_total_p000(BigDecimal r103_total_p000) {
		this.r103_total_p000 = r103_total_p000;
	}
	public String getR104_line_no() {
		return r104_line_no;
	}
	public void setR104_line_no(String r104_line_no) {
		this.r104_line_no = r104_line_no;
	}
	public String getR104_note_holders() {
		return r104_note_holders;
	}
	public void setR104_note_holders(String r104_note_holders) {
		this.r104_note_holders = r104_note_holders;
	}
	public String getR104_name_of_instrument_programe() {
		return r104_name_of_instrument_programe;
	}
	public void setR104_name_of_instrument_programe(String r104_name_of_instrument_programe) {
		this.r104_name_of_instrument_programe = r104_name_of_instrument_programe;
	}
	public String getR104_issuing_entity_if_issued_throughan_spv() {
		return r104_issuing_entity_if_issued_throughan_spv;
	}
	public void setR104_issuing_entity_if_issued_throughan_spv(String r104_issuing_entity_if_issued_throughan_spv) {
		this.r104_issuing_entity_if_issued_throughan_spv = r104_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR104_issuance_date() {
		return r104_issuance_date;
	}
	public void setR104_issuance_date(Date r104_issuance_date) {
		this.r104_issuance_date = r104_issuance_date;
	}
	public Date getR104_contractual_maturity_date() {
		return r104_contractual_maturity_date;
	}
	public void setR104_contractual_maturity_date(Date r104_contractual_maturity_date) {
		this.r104_contractual_maturity_date = r104_contractual_maturity_date;
	}
	public Date getR104_effective_maturity_date_if_date() {
		return r104_effective_maturity_date_if_date;
	}
	public void setR104_effective_maturity_date_if_date(Date r104_effective_maturity_date_if_date) {
		this.r104_effective_maturity_date_if_date = r104_effective_maturity_date_if_date;
	}
	public BigDecimal getR104_amount() {
		return r104_amount;
	}
	public void setR104_amount(BigDecimal r104_amount) {
		this.r104_amount = r104_amount;
	}
	public BigDecimal getR104_amount_derecognised_p000() {
		return r104_amount_derecognised_p000;
	}
	public void setR104_amount_derecognised_p000(BigDecimal r104_amount_derecognised_p000) {
		this.r104_amount_derecognised_p000 = r104_amount_derecognised_p000;
	}
	public BigDecimal getR104_total_p000() {
		return r104_total_p000;
	}
	public void setR104_total_p000(BigDecimal r104_total_p000) {
		this.r104_total_p000 = r104_total_p000;
	}
	public String getR105_line_no() {
		return r105_line_no;
	}
	public void setR105_line_no(String r105_line_no) {
		this.r105_line_no = r105_line_no;
	}
	public String getR105_note_holders() {
		return r105_note_holders;
	}
	public void setR105_note_holders(String r105_note_holders) {
		this.r105_note_holders = r105_note_holders;
	}
	public String getR105_name_of_instrument_programe() {
		return r105_name_of_instrument_programe;
	}
	public void setR105_name_of_instrument_programe(String r105_name_of_instrument_programe) {
		this.r105_name_of_instrument_programe = r105_name_of_instrument_programe;
	}
	public String getR105_issuing_entity_if_issued_throughan_spv() {
		return r105_issuing_entity_if_issued_throughan_spv;
	}
	public void setR105_issuing_entity_if_issued_throughan_spv(String r105_issuing_entity_if_issued_throughan_spv) {
		this.r105_issuing_entity_if_issued_throughan_spv = r105_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR105_issuance_date() {
		return r105_issuance_date;
	}
	public void setR105_issuance_date(Date r105_issuance_date) {
		this.r105_issuance_date = r105_issuance_date;
	}
	public Date getR105_contractual_maturity_date() {
		return r105_contractual_maturity_date;
	}
	public void setR105_contractual_maturity_date(Date r105_contractual_maturity_date) {
		this.r105_contractual_maturity_date = r105_contractual_maturity_date;
	}
	public Date getR105_effective_maturity_date_if_date() {
		return r105_effective_maturity_date_if_date;
	}
	public void setR105_effective_maturity_date_if_date(Date r105_effective_maturity_date_if_date) {
		this.r105_effective_maturity_date_if_date = r105_effective_maturity_date_if_date;
	}
	public BigDecimal getR105_amount() {
		return r105_amount;
	}
	public void setR105_amount(BigDecimal r105_amount) {
		this.r105_amount = r105_amount;
	}
	public BigDecimal getR105_amount_derecognised_p000() {
		return r105_amount_derecognised_p000;
	}
	public void setR105_amount_derecognised_p000(BigDecimal r105_amount_derecognised_p000) {
		this.r105_amount_derecognised_p000 = r105_amount_derecognised_p000;
	}
	public BigDecimal getR105_total_p000() {
		return r105_total_p000;
	}
	public void setR105_total_p000(BigDecimal r105_total_p000) {
		this.r105_total_p000 = r105_total_p000;
	}
	public String getR106_line_no() {
		return r106_line_no;
	}
	public void setR106_line_no(String r106_line_no) {
		this.r106_line_no = r106_line_no;
	}
	public String getR106_note_holders() {
		return r106_note_holders;
	}
	public void setR106_note_holders(String r106_note_holders) {
		this.r106_note_holders = r106_note_holders;
	}
	public String getR106_name_of_instrument_programe() {
		return r106_name_of_instrument_programe;
	}
	public void setR106_name_of_instrument_programe(String r106_name_of_instrument_programe) {
		this.r106_name_of_instrument_programe = r106_name_of_instrument_programe;
	}
	public String getR106_issuing_entity_if_issued_throughan_spv() {
		return r106_issuing_entity_if_issued_throughan_spv;
	}
	public void setR106_issuing_entity_if_issued_throughan_spv(String r106_issuing_entity_if_issued_throughan_spv) {
		this.r106_issuing_entity_if_issued_throughan_spv = r106_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR106_issuance_date() {
		return r106_issuance_date;
	}
	public void setR106_issuance_date(Date r106_issuance_date) {
		this.r106_issuance_date = r106_issuance_date;
	}
	public Date getR106_contractual_maturity_date() {
		return r106_contractual_maturity_date;
	}
	public void setR106_contractual_maturity_date(Date r106_contractual_maturity_date) {
		this.r106_contractual_maturity_date = r106_contractual_maturity_date;
	}
	public Date getR106_effective_maturity_date_if_date() {
		return r106_effective_maturity_date_if_date;
	}
	public void setR106_effective_maturity_date_if_date(Date r106_effective_maturity_date_if_date) {
		this.r106_effective_maturity_date_if_date = r106_effective_maturity_date_if_date;
	}
	public BigDecimal getR106_amount() {
		return r106_amount;
	}
	public void setR106_amount(BigDecimal r106_amount) {
		this.r106_amount = r106_amount;
	}
	public BigDecimal getR106_amount_derecognised_p000() {
		return r106_amount_derecognised_p000;
	}
	public void setR106_amount_derecognised_p000(BigDecimal r106_amount_derecognised_p000) {
		this.r106_amount_derecognised_p000 = r106_amount_derecognised_p000;
	}
	public BigDecimal getR106_total_p000() {
		return r106_total_p000;
	}
	public void setR106_total_p000(BigDecimal r106_total_p000) {
		this.r106_total_p000 = r106_total_p000;
	}
	public String getR107_line_no() {
		return r107_line_no;
	}
	public void setR107_line_no(String r107_line_no) {
		this.r107_line_no = r107_line_no;
	}
	public String getR107_note_holders() {
		return r107_note_holders;
	}
	public void setR107_note_holders(String r107_note_holders) {
		this.r107_note_holders = r107_note_holders;
	}
	public String getR107_name_of_instrument_programe() {
		return r107_name_of_instrument_programe;
	}
	public void setR107_name_of_instrument_programe(String r107_name_of_instrument_programe) {
		this.r107_name_of_instrument_programe = r107_name_of_instrument_programe;
	}
	public String getR107_issuing_entity_if_issued_throughan_spv() {
		return r107_issuing_entity_if_issued_throughan_spv;
	}
	public void setR107_issuing_entity_if_issued_throughan_spv(String r107_issuing_entity_if_issued_throughan_spv) {
		this.r107_issuing_entity_if_issued_throughan_spv = r107_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR107_issuance_date() {
		return r107_issuance_date;
	}
	public void setR107_issuance_date(Date r107_issuance_date) {
		this.r107_issuance_date = r107_issuance_date;
	}
	public Date getR107_contractual_maturity_date() {
		return r107_contractual_maturity_date;
	}
	public void setR107_contractual_maturity_date(Date r107_contractual_maturity_date) {
		this.r107_contractual_maturity_date = r107_contractual_maturity_date;
	}
	public Date getR107_effective_maturity_date_if_date() {
		return r107_effective_maturity_date_if_date;
	}
	public void setR107_effective_maturity_date_if_date(Date r107_effective_maturity_date_if_date) {
		this.r107_effective_maturity_date_if_date = r107_effective_maturity_date_if_date;
	}
	public BigDecimal getR107_amount() {
		return r107_amount;
	}
	public void setR107_amount(BigDecimal r107_amount) {
		this.r107_amount = r107_amount;
	}
	public BigDecimal getR107_amount_derecognised_p000() {
		return r107_amount_derecognised_p000;
	}
	public void setR107_amount_derecognised_p000(BigDecimal r107_amount_derecognised_p000) {
		this.r107_amount_derecognised_p000 = r107_amount_derecognised_p000;
	}
	public BigDecimal getR107_total_p000() {
		return r107_total_p000;
	}
	public void setR107_total_p000(BigDecimal r107_total_p000) {
		this.r107_total_p000 = r107_total_p000;
	}
	public String getR108_line_no() {
		return r108_line_no;
	}
	public void setR108_line_no(String r108_line_no) {
		this.r108_line_no = r108_line_no;
	}
	public String getR108_note_holders() {
		return r108_note_holders;
	}
	public void setR108_note_holders(String r108_note_holders) {
		this.r108_note_holders = r108_note_holders;
	}
	public String getR108_name_of_instrument_programe() {
		return r108_name_of_instrument_programe;
	}
	public void setR108_name_of_instrument_programe(String r108_name_of_instrument_programe) {
		this.r108_name_of_instrument_programe = r108_name_of_instrument_programe;
	}
	public String getR108_issuing_entity_if_issued_throughan_spv() {
		return r108_issuing_entity_if_issued_throughan_spv;
	}
	public void setR108_issuing_entity_if_issued_throughan_spv(String r108_issuing_entity_if_issued_throughan_spv) {
		this.r108_issuing_entity_if_issued_throughan_spv = r108_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR108_issuance_date() {
		return r108_issuance_date;
	}
	public void setR108_issuance_date(Date r108_issuance_date) {
		this.r108_issuance_date = r108_issuance_date;
	}
	public Date getR108_contractual_maturity_date() {
		return r108_contractual_maturity_date;
	}
	public void setR108_contractual_maturity_date(Date r108_contractual_maturity_date) {
		this.r108_contractual_maturity_date = r108_contractual_maturity_date;
	}
	public Date getR108_effective_maturity_date_if_date() {
		return r108_effective_maturity_date_if_date;
	}
	public void setR108_effective_maturity_date_if_date(Date r108_effective_maturity_date_if_date) {
		this.r108_effective_maturity_date_if_date = r108_effective_maturity_date_if_date;
	}
	public BigDecimal getR108_amount() {
		return r108_amount;
	}
	public void setR108_amount(BigDecimal r108_amount) {
		this.r108_amount = r108_amount;
	}
	public BigDecimal getR108_amount_derecognised_p000() {
		return r108_amount_derecognised_p000;
	}
	public void setR108_amount_derecognised_p000(BigDecimal r108_amount_derecognised_p000) {
		this.r108_amount_derecognised_p000 = r108_amount_derecognised_p000;
	}
	public BigDecimal getR108_total_p000() {
		return r108_total_p000;
	}
	public void setR108_total_p000(BigDecimal r108_total_p000) {
		this.r108_total_p000 = r108_total_p000;
	}
	public String getR109_line_no() {
		return r109_line_no;
	}
	public void setR109_line_no(String r109_line_no) {
		this.r109_line_no = r109_line_no;
	}
	public String getR109_note_holders() {
		return r109_note_holders;
	}
	public void setR109_note_holders(String r109_note_holders) {
		this.r109_note_holders = r109_note_holders;
	}
	public String getR109_name_of_instrument_programe() {
		return r109_name_of_instrument_programe;
	}
	public void setR109_name_of_instrument_programe(String r109_name_of_instrument_programe) {
		this.r109_name_of_instrument_programe = r109_name_of_instrument_programe;
	}
	public String getR109_issuing_entity_if_issued_throughan_spv() {
		return r109_issuing_entity_if_issued_throughan_spv;
	}
	public void setR109_issuing_entity_if_issued_throughan_spv(String r109_issuing_entity_if_issued_throughan_spv) {
		this.r109_issuing_entity_if_issued_throughan_spv = r109_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR109_issuance_date() {
		return r109_issuance_date;
	}
	public void setR109_issuance_date(Date r109_issuance_date) {
		this.r109_issuance_date = r109_issuance_date;
	}
	public Date getR109_contractual_maturity_date() {
		return r109_contractual_maturity_date;
	}
	public void setR109_contractual_maturity_date(Date r109_contractual_maturity_date) {
		this.r109_contractual_maturity_date = r109_contractual_maturity_date;
	}
	public Date getR109_effective_maturity_date_if_date() {
		return r109_effective_maturity_date_if_date;
	}
	public void setR109_effective_maturity_date_if_date(Date r109_effective_maturity_date_if_date) {
		this.r109_effective_maturity_date_if_date = r109_effective_maturity_date_if_date;
	}
	public BigDecimal getR109_amount() {
		return r109_amount;
	}
	public void setR109_amount(BigDecimal r109_amount) {
		this.r109_amount = r109_amount;
	}
	public BigDecimal getR109_amount_derecognised_p000() {
		return r109_amount_derecognised_p000;
	}
	public void setR109_amount_derecognised_p000(BigDecimal r109_amount_derecognised_p000) {
		this.r109_amount_derecognised_p000 = r109_amount_derecognised_p000;
	}
	public BigDecimal getR109_total_p000() {
		return r109_total_p000;
	}
	public void setR109_total_p000(BigDecimal r109_total_p000) {
		this.r109_total_p000 = r109_total_p000;
	}
	public String getR110_line_no() {
		return r110_line_no;
	}
	public void setR110_line_no(String r110_line_no) {
		this.r110_line_no = r110_line_no;
	}
	public String getR110_note_holders() {
		return r110_note_holders;
	}
	public void setR110_note_holders(String r110_note_holders) {
		this.r110_note_holders = r110_note_holders;
	}
	public String getR110_name_of_instrument_programe() {
		return r110_name_of_instrument_programe;
	}
	public void setR110_name_of_instrument_programe(String r110_name_of_instrument_programe) {
		this.r110_name_of_instrument_programe = r110_name_of_instrument_programe;
	}
	public String getR110_issuing_entity_if_issued_throughan_spv() {
		return r110_issuing_entity_if_issued_throughan_spv;
	}
	public void setR110_issuing_entity_if_issued_throughan_spv(String r110_issuing_entity_if_issued_throughan_spv) {
		this.r110_issuing_entity_if_issued_throughan_spv = r110_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR110_issuance_date() {
		return r110_issuance_date;
	}
	public void setR110_issuance_date(Date r110_issuance_date) {
		this.r110_issuance_date = r110_issuance_date;
	}
	public Date getR110_contractual_maturity_date() {
		return r110_contractual_maturity_date;
	}
	public void setR110_contractual_maturity_date(Date r110_contractual_maturity_date) {
		this.r110_contractual_maturity_date = r110_contractual_maturity_date;
	}
	public Date getR110_effective_maturity_date_if_date() {
		return r110_effective_maturity_date_if_date;
	}
	public void setR110_effective_maturity_date_if_date(Date r110_effective_maturity_date_if_date) {
		this.r110_effective_maturity_date_if_date = r110_effective_maturity_date_if_date;
	}
	public BigDecimal getR110_amount() {
		return r110_amount;
	}
	public void setR110_amount(BigDecimal r110_amount) {
		this.r110_amount = r110_amount;
	}
	public BigDecimal getR110_amount_derecognised_p000() {
		return r110_amount_derecognised_p000;
	}
	public void setR110_amount_derecognised_p000(BigDecimal r110_amount_derecognised_p000) {
		this.r110_amount_derecognised_p000 = r110_amount_derecognised_p000;
	}
	public BigDecimal getR110_total_p000() {
		return r110_total_p000;
	}
	public void setR110_total_p000(BigDecimal r110_total_p000) {
		this.r110_total_p000 = r110_total_p000;
	}
	public String getR111_line_no() {
		return r111_line_no;
	}
	public void setR111_line_no(String r111_line_no) {
		this.r111_line_no = r111_line_no;
	}
	public String getR111_note_holders() {
		return r111_note_holders;
	}
	public void setR111_note_holders(String r111_note_holders) {
		this.r111_note_holders = r111_note_holders;
	}
	public String getR111_name_of_instrument_programe() {
		return r111_name_of_instrument_programe;
	}
	public void setR111_name_of_instrument_programe(String r111_name_of_instrument_programe) {
		this.r111_name_of_instrument_programe = r111_name_of_instrument_programe;
	}
	public String getR111_issuing_entity_if_issued_throughan_spv() {
		return r111_issuing_entity_if_issued_throughan_spv;
	}
	public void setR111_issuing_entity_if_issued_throughan_spv(String r111_issuing_entity_if_issued_throughan_spv) {
		this.r111_issuing_entity_if_issued_throughan_spv = r111_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR111_issuance_date() {
		return r111_issuance_date;
	}
	public void setR111_issuance_date(Date r111_issuance_date) {
		this.r111_issuance_date = r111_issuance_date;
	}
	public Date getR111_contractual_maturity_date() {
		return r111_contractual_maturity_date;
	}
	public void setR111_contractual_maturity_date(Date r111_contractual_maturity_date) {
		this.r111_contractual_maturity_date = r111_contractual_maturity_date;
	}
	public Date getR111_effective_maturity_date_if_date() {
		return r111_effective_maturity_date_if_date;
	}
	public void setR111_effective_maturity_date_if_date(Date r111_effective_maturity_date_if_date) {
		this.r111_effective_maturity_date_if_date = r111_effective_maturity_date_if_date;
	}
	public BigDecimal getR111_amount() {
		return r111_amount;
	}
	public void setR111_amount(BigDecimal r111_amount) {
		this.r111_amount = r111_amount;
	}
	public BigDecimal getR111_amount_derecognised_p000() {
		return r111_amount_derecognised_p000;
	}
	public void setR111_amount_derecognised_p000(BigDecimal r111_amount_derecognised_p000) {
		this.r111_amount_derecognised_p000 = r111_amount_derecognised_p000;
	}
	public BigDecimal getR111_total_p000() {
		return r111_total_p000;
	}
	public void setR111_total_p000(BigDecimal r111_total_p000) {
		this.r111_total_p000 = r111_total_p000;
	}
	public String getR112_line_no() {
		return r112_line_no;
	}
	public void setR112_line_no(String r112_line_no) {
		this.r112_line_no = r112_line_no;
	}
	public String getR112_note_holders() {
		return r112_note_holders;
	}
	public void setR112_note_holders(String r112_note_holders) {
		this.r112_note_holders = r112_note_holders;
	}
	public String getR112_name_of_instrument_programe() {
		return r112_name_of_instrument_programe;
	}
	public void setR112_name_of_instrument_programe(String r112_name_of_instrument_programe) {
		this.r112_name_of_instrument_programe = r112_name_of_instrument_programe;
	}
	public String getR112_issuing_entity_if_issued_throughan_spv() {
		return r112_issuing_entity_if_issued_throughan_spv;
	}
	public void setR112_issuing_entity_if_issued_throughan_spv(String r112_issuing_entity_if_issued_throughan_spv) {
		this.r112_issuing_entity_if_issued_throughan_spv = r112_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR112_issuance_date() {
		return r112_issuance_date;
	}
	public void setR112_issuance_date(Date r112_issuance_date) {
		this.r112_issuance_date = r112_issuance_date;
	}
	public Date getR112_contractual_maturity_date() {
		return r112_contractual_maturity_date;
	}
	public void setR112_contractual_maturity_date(Date r112_contractual_maturity_date) {
		this.r112_contractual_maturity_date = r112_contractual_maturity_date;
	}
	public Date getR112_effective_maturity_date_if_date() {
		return r112_effective_maturity_date_if_date;
	}
	public void setR112_effective_maturity_date_if_date(Date r112_effective_maturity_date_if_date) {
		this.r112_effective_maturity_date_if_date = r112_effective_maturity_date_if_date;
	}
	public BigDecimal getR112_amount() {
		return r112_amount;
	}
	public void setR112_amount(BigDecimal r112_amount) {
		this.r112_amount = r112_amount;
	}
	public BigDecimal getR112_amount_derecognised_p000() {
		return r112_amount_derecognised_p000;
	}
	public void setR112_amount_derecognised_p000(BigDecimal r112_amount_derecognised_p000) {
		this.r112_amount_derecognised_p000 = r112_amount_derecognised_p000;
	}
	public BigDecimal getR112_total_p000() {
		return r112_total_p000;
	}
	public void setR112_total_p000(BigDecimal r112_total_p000) {
		this.r112_total_p000 = r112_total_p000;
	}
	public String getR113_line_no() {
		return r113_line_no;
	}
	public void setR113_line_no(String r113_line_no) {
		this.r113_line_no = r113_line_no;
	}
	public String getR113_note_holders() {
		return r113_note_holders;
	}
	public void setR113_note_holders(String r113_note_holders) {
		this.r113_note_holders = r113_note_holders;
	}
	public String getR113_name_of_instrument_programe() {
		return r113_name_of_instrument_programe;
	}
	public void setR113_name_of_instrument_programe(String r113_name_of_instrument_programe) {
		this.r113_name_of_instrument_programe = r113_name_of_instrument_programe;
	}
	public String getR113_issuing_entity_if_issued_throughan_spv() {
		return r113_issuing_entity_if_issued_throughan_spv;
	}
	public void setR113_issuing_entity_if_issued_throughan_spv(String r113_issuing_entity_if_issued_throughan_spv) {
		this.r113_issuing_entity_if_issued_throughan_spv = r113_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR113_issuance_date() {
		return r113_issuance_date;
	}
	public void setR113_issuance_date(Date r113_issuance_date) {
		this.r113_issuance_date = r113_issuance_date;
	}
	public Date getR113_contractual_maturity_date() {
		return r113_contractual_maturity_date;
	}
	public void setR113_contractual_maturity_date(Date r113_contractual_maturity_date) {
		this.r113_contractual_maturity_date = r113_contractual_maturity_date;
	}
	public Date getR113_effective_maturity_date_if_date() {
		return r113_effective_maturity_date_if_date;
	}
	public void setR113_effective_maturity_date_if_date(Date r113_effective_maturity_date_if_date) {
		this.r113_effective_maturity_date_if_date = r113_effective_maturity_date_if_date;
	}
	public BigDecimal getR113_amount() {
		return r113_amount;
	}
	public void setR113_amount(BigDecimal r113_amount) {
		this.r113_amount = r113_amount;
	}
	public BigDecimal getR113_amount_derecognised_p000() {
		return r113_amount_derecognised_p000;
	}
	public void setR113_amount_derecognised_p000(BigDecimal r113_amount_derecognised_p000) {
		this.r113_amount_derecognised_p000 = r113_amount_derecognised_p000;
	}
	public BigDecimal getR113_total_p000() {
		return r113_total_p000;
	}
	public void setR113_total_p000(BigDecimal r113_total_p000) {
		this.r113_total_p000 = r113_total_p000;
	}
	public String getR114_line_no() {
		return r114_line_no;
	}
	public void setR114_line_no(String r114_line_no) {
		this.r114_line_no = r114_line_no;
	}
	public String getR114_note_holders() {
		return r114_note_holders;
	}
	public void setR114_note_holders(String r114_note_holders) {
		this.r114_note_holders = r114_note_holders;
	}
	public String getR114_name_of_instrument_programe() {
		return r114_name_of_instrument_programe;
	}
	public void setR114_name_of_instrument_programe(String r114_name_of_instrument_programe) {
		this.r114_name_of_instrument_programe = r114_name_of_instrument_programe;
	}
	public String getR114_issuing_entity_if_issued_throughan_spv() {
		return r114_issuing_entity_if_issued_throughan_spv;
	}
	public void setR114_issuing_entity_if_issued_throughan_spv(String r114_issuing_entity_if_issued_throughan_spv) {
		this.r114_issuing_entity_if_issued_throughan_spv = r114_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR114_issuance_date() {
		return r114_issuance_date;
	}
	public void setR114_issuance_date(Date r114_issuance_date) {
		this.r114_issuance_date = r114_issuance_date;
	}
	public Date getR114_contractual_maturity_date() {
		return r114_contractual_maturity_date;
	}
	public void setR114_contractual_maturity_date(Date r114_contractual_maturity_date) {
		this.r114_contractual_maturity_date = r114_contractual_maturity_date;
	}
	public Date getR114_effective_maturity_date_if_date() {
		return r114_effective_maturity_date_if_date;
	}
	public void setR114_effective_maturity_date_if_date(Date r114_effective_maturity_date_if_date) {
		this.r114_effective_maturity_date_if_date = r114_effective_maturity_date_if_date;
	}
	public BigDecimal getR114_amount() {
		return r114_amount;
	}
	public void setR114_amount(BigDecimal r114_amount) {
		this.r114_amount = r114_amount;
	}
	public BigDecimal getR114_amount_derecognised_p000() {
		return r114_amount_derecognised_p000;
	}
	public void setR114_amount_derecognised_p000(BigDecimal r114_amount_derecognised_p000) {
		this.r114_amount_derecognised_p000 = r114_amount_derecognised_p000;
	}
	public BigDecimal getR114_total_p000() {
		return r114_total_p000;
	}
	public void setR114_total_p000(BigDecimal r114_total_p000) {
		this.r114_total_p000 = r114_total_p000;
	}
	public String getR115_line_no() {
		return r115_line_no;
	}
	public void setR115_line_no(String r115_line_no) {
		this.r115_line_no = r115_line_no;
	}
	public String getR115_note_holders() {
		return r115_note_holders;
	}
	public void setR115_note_holders(String r115_note_holders) {
		this.r115_note_holders = r115_note_holders;
	}
	public String getR115_name_of_instrument_programe() {
		return r115_name_of_instrument_programe;
	}
	public void setR115_name_of_instrument_programe(String r115_name_of_instrument_programe) {
		this.r115_name_of_instrument_programe = r115_name_of_instrument_programe;
	}
	public String getR115_issuing_entity_if_issued_throughan_spv() {
		return r115_issuing_entity_if_issued_throughan_spv;
	}
	public void setR115_issuing_entity_if_issued_throughan_spv(String r115_issuing_entity_if_issued_throughan_spv) {
		this.r115_issuing_entity_if_issued_throughan_spv = r115_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR115_issuance_date() {
		return r115_issuance_date;
	}
	public void setR115_issuance_date(Date r115_issuance_date) {
		this.r115_issuance_date = r115_issuance_date;
	}
	public Date getR115_contractual_maturity_date() {
		return r115_contractual_maturity_date;
	}
	public void setR115_contractual_maturity_date(Date r115_contractual_maturity_date) {
		this.r115_contractual_maturity_date = r115_contractual_maturity_date;
	}
	public Date getR115_effective_maturity_date_if_date() {
		return r115_effective_maturity_date_if_date;
	}
	public void setR115_effective_maturity_date_if_date(Date r115_effective_maturity_date_if_date) {
		this.r115_effective_maturity_date_if_date = r115_effective_maturity_date_if_date;
	}
	public BigDecimal getR115_amount() {
		return r115_amount;
	}
	public void setR115_amount(BigDecimal r115_amount) {
		this.r115_amount = r115_amount;
	}
	public BigDecimal getR115_amount_derecognised_p000() {
		return r115_amount_derecognised_p000;
	}
	public void setR115_amount_derecognised_p000(BigDecimal r115_amount_derecognised_p000) {
		this.r115_amount_derecognised_p000 = r115_amount_derecognised_p000;
	}
	public BigDecimal getR115_total_p000() {
		return r115_total_p000;
	}
	public void setR115_total_p000(BigDecimal r115_total_p000) {
		this.r115_total_p000 = r115_total_p000;
	}
	public String getR116_line_no() {
		return r116_line_no;
	}
	public void setR116_line_no(String r116_line_no) {
		this.r116_line_no = r116_line_no;
	}
	public String getR116_note_holders() {
		return r116_note_holders;
	}
	public void setR116_note_holders(String r116_note_holders) {
		this.r116_note_holders = r116_note_holders;
	}
	public String getR116_name_of_instrument_programe() {
		return r116_name_of_instrument_programe;
	}
	public void setR116_name_of_instrument_programe(String r116_name_of_instrument_programe) {
		this.r116_name_of_instrument_programe = r116_name_of_instrument_programe;
	}
	public String getR116_issuing_entity_if_issued_throughan_spv() {
		return r116_issuing_entity_if_issued_throughan_spv;
	}
	public void setR116_issuing_entity_if_issued_throughan_spv(String r116_issuing_entity_if_issued_throughan_spv) {
		this.r116_issuing_entity_if_issued_throughan_spv = r116_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR116_issuance_date() {
		return r116_issuance_date;
	}
	public void setR116_issuance_date(Date r116_issuance_date) {
		this.r116_issuance_date = r116_issuance_date;
	}
	public Date getR116_contractual_maturity_date() {
		return r116_contractual_maturity_date;
	}
	public void setR116_contractual_maturity_date(Date r116_contractual_maturity_date) {
		this.r116_contractual_maturity_date = r116_contractual_maturity_date;
	}
	public Date getR116_effective_maturity_date_if_date() {
		return r116_effective_maturity_date_if_date;
	}
	public void setR116_effective_maturity_date_if_date(Date r116_effective_maturity_date_if_date) {
		this.r116_effective_maturity_date_if_date = r116_effective_maturity_date_if_date;
	}
	public BigDecimal getR116_amount() {
		return r116_amount;
	}
	public void setR116_amount(BigDecimal r116_amount) {
		this.r116_amount = r116_amount;
	}
	public BigDecimal getR116_amount_derecognised_p000() {
		return r116_amount_derecognised_p000;
	}
	public void setR116_amount_derecognised_p000(BigDecimal r116_amount_derecognised_p000) {
		this.r116_amount_derecognised_p000 = r116_amount_derecognised_p000;
	}
	public BigDecimal getR116_total_p000() {
		return r116_total_p000;
	}
	public void setR116_total_p000(BigDecimal r116_total_p000) {
		this.r116_total_p000 = r116_total_p000;
	}
	public String getR117_line_no() {
		return r117_line_no;
	}
	public void setR117_line_no(String r117_line_no) {
		this.r117_line_no = r117_line_no;
	}
	public String getR117_note_holders() {
		return r117_note_holders;
	}
	public void setR117_note_holders(String r117_note_holders) {
		this.r117_note_holders = r117_note_holders;
	}
	public String getR117_name_of_instrument_programe() {
		return r117_name_of_instrument_programe;
	}
	public void setR117_name_of_instrument_programe(String r117_name_of_instrument_programe) {
		this.r117_name_of_instrument_programe = r117_name_of_instrument_programe;
	}
	public String getR117_issuing_entity_if_issued_throughan_spv() {
		return r117_issuing_entity_if_issued_throughan_spv;
	}
	public void setR117_issuing_entity_if_issued_throughan_spv(String r117_issuing_entity_if_issued_throughan_spv) {
		this.r117_issuing_entity_if_issued_throughan_spv = r117_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR117_issuance_date() {
		return r117_issuance_date;
	}
	public void setR117_issuance_date(Date r117_issuance_date) {
		this.r117_issuance_date = r117_issuance_date;
	}
	public Date getR117_contractual_maturity_date() {
		return r117_contractual_maturity_date;
	}
	public void setR117_contractual_maturity_date(Date r117_contractual_maturity_date) {
		this.r117_contractual_maturity_date = r117_contractual_maturity_date;
	}
	public Date getR117_effective_maturity_date_if_date() {
		return r117_effective_maturity_date_if_date;
	}
	public void setR117_effective_maturity_date_if_date(Date r117_effective_maturity_date_if_date) {
		this.r117_effective_maturity_date_if_date = r117_effective_maturity_date_if_date;
	}
	public BigDecimal getR117_amount() {
		return r117_amount;
	}
	public void setR117_amount(BigDecimal r117_amount) {
		this.r117_amount = r117_amount;
	}
	public BigDecimal getR117_amount_derecognised_p000() {
		return r117_amount_derecognised_p000;
	}
	public void setR117_amount_derecognised_p000(BigDecimal r117_amount_derecognised_p000) {
		this.r117_amount_derecognised_p000 = r117_amount_derecognised_p000;
	}
	public BigDecimal getR117_total_p000() {
		return r117_total_p000;
	}
	public void setR117_total_p000(BigDecimal r117_total_p000) {
		this.r117_total_p000 = r117_total_p000;
	}
	public String getR118_line_no() {
		return r118_line_no;
	}
	public void setR118_line_no(String r118_line_no) {
		this.r118_line_no = r118_line_no;
	}
	public String getR118_note_holders() {
		return r118_note_holders;
	}
	public void setR118_note_holders(String r118_note_holders) {
		this.r118_note_holders = r118_note_holders;
	}
	public String getR118_name_of_instrument_programe() {
		return r118_name_of_instrument_programe;
	}
	public void setR118_name_of_instrument_programe(String r118_name_of_instrument_programe) {
		this.r118_name_of_instrument_programe = r118_name_of_instrument_programe;
	}
	public String getR118_issuing_entity_if_issued_throughan_spv() {
		return r118_issuing_entity_if_issued_throughan_spv;
	}
	public void setR118_issuing_entity_if_issued_throughan_spv(String r118_issuing_entity_if_issued_throughan_spv) {
		this.r118_issuing_entity_if_issued_throughan_spv = r118_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR118_issuance_date() {
		return r118_issuance_date;
	}
	public void setR118_issuance_date(Date r118_issuance_date) {
		this.r118_issuance_date = r118_issuance_date;
	}
	public Date getR118_contractual_maturity_date() {
		return r118_contractual_maturity_date;
	}
	public void setR118_contractual_maturity_date(Date r118_contractual_maturity_date) {
		this.r118_contractual_maturity_date = r118_contractual_maturity_date;
	}
	public Date getR118_effective_maturity_date_if_date() {
		return r118_effective_maturity_date_if_date;
	}
	public void setR118_effective_maturity_date_if_date(Date r118_effective_maturity_date_if_date) {
		this.r118_effective_maturity_date_if_date = r118_effective_maturity_date_if_date;
	}
	public BigDecimal getR118_amount() {
		return r118_amount;
	}
	public void setR118_amount(BigDecimal r118_amount) {
		this.r118_amount = r118_amount;
	}
	public BigDecimal getR118_amount_derecognised_p000() {
		return r118_amount_derecognised_p000;
	}
	public void setR118_amount_derecognised_p000(BigDecimal r118_amount_derecognised_p000) {
		this.r118_amount_derecognised_p000 = r118_amount_derecognised_p000;
	}
	public BigDecimal getR118_total_p000() {
		return r118_total_p000;
	}
	public void setR118_total_p000(BigDecimal r118_total_p000) {
		this.r118_total_p000 = r118_total_p000;
	}
	public String getR119_line_no() {
		return r119_line_no;
	}
	public void setR119_line_no(String r119_line_no) {
		this.r119_line_no = r119_line_no;
	}
	public String getR119_note_holders() {
		return r119_note_holders;
	}
	public void setR119_note_holders(String r119_note_holders) {
		this.r119_note_holders = r119_note_holders;
	}
	public String getR119_name_of_instrument_programe() {
		return r119_name_of_instrument_programe;
	}
	public void setR119_name_of_instrument_programe(String r119_name_of_instrument_programe) {
		this.r119_name_of_instrument_programe = r119_name_of_instrument_programe;
	}
	public String getR119_issuing_entity_if_issued_throughan_spv() {
		return r119_issuing_entity_if_issued_throughan_spv;
	}
	public void setR119_issuing_entity_if_issued_throughan_spv(String r119_issuing_entity_if_issued_throughan_spv) {
		this.r119_issuing_entity_if_issued_throughan_spv = r119_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR119_issuance_date() {
		return r119_issuance_date;
	}
	public void setR119_issuance_date(Date r119_issuance_date) {
		this.r119_issuance_date = r119_issuance_date;
	}
	public Date getR119_contractual_maturity_date() {
		return r119_contractual_maturity_date;
	}
	public void setR119_contractual_maturity_date(Date r119_contractual_maturity_date) {
		this.r119_contractual_maturity_date = r119_contractual_maturity_date;
	}
	public Date getR119_effective_maturity_date_if_date() {
		return r119_effective_maturity_date_if_date;
	}
	public void setR119_effective_maturity_date_if_date(Date r119_effective_maturity_date_if_date) {
		this.r119_effective_maturity_date_if_date = r119_effective_maturity_date_if_date;
	}
	public BigDecimal getR119_amount() {
		return r119_amount;
	}
	public void setR119_amount(BigDecimal r119_amount) {
		this.r119_amount = r119_amount;
	}
	public BigDecimal getR119_amount_derecognised_p000() {
		return r119_amount_derecognised_p000;
	}
	public void setR119_amount_derecognised_p000(BigDecimal r119_amount_derecognised_p000) {
		this.r119_amount_derecognised_p000 = r119_amount_derecognised_p000;
	}
	public BigDecimal getR119_total_p000() {
		return r119_total_p000;
	}
	public void setR119_total_p000(BigDecimal r119_total_p000) {
		this.r119_total_p000 = r119_total_p000;
	}
	public String getR120_line_no() {
		return r120_line_no;
	}
	public void setR120_line_no(String r120_line_no) {
		this.r120_line_no = r120_line_no;
	}
	public String getR120_note_holders() {
		return r120_note_holders;
	}
	public void setR120_note_holders(String r120_note_holders) {
		this.r120_note_holders = r120_note_holders;
	}
	public String getR120_name_of_instrument_programe() {
		return r120_name_of_instrument_programe;
	}
	public void setR120_name_of_instrument_programe(String r120_name_of_instrument_programe) {
		this.r120_name_of_instrument_programe = r120_name_of_instrument_programe;
	}
	public String getR120_issuing_entity_if_issued_throughan_spv() {
		return r120_issuing_entity_if_issued_throughan_spv;
	}
	public void setR120_issuing_entity_if_issued_throughan_spv(String r120_issuing_entity_if_issued_throughan_spv) {
		this.r120_issuing_entity_if_issued_throughan_spv = r120_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR120_issuance_date() {
		return r120_issuance_date;
	}
	public void setR120_issuance_date(Date r120_issuance_date) {
		this.r120_issuance_date = r120_issuance_date;
	}
	public Date getR120_contractual_maturity_date() {
		return r120_contractual_maturity_date;
	}
	public void setR120_contractual_maturity_date(Date r120_contractual_maturity_date) {
		this.r120_contractual_maturity_date = r120_contractual_maturity_date;
	}
	public Date getR120_effective_maturity_date_if_date() {
		return r120_effective_maturity_date_if_date;
	}
	public void setR120_effective_maturity_date_if_date(Date r120_effective_maturity_date_if_date) {
		this.r120_effective_maturity_date_if_date = r120_effective_maturity_date_if_date;
	}
	public BigDecimal getR120_amount() {
		return r120_amount;
	}
	public void setR120_amount(BigDecimal r120_amount) {
		this.r120_amount = r120_amount;
	}
	public BigDecimal getR120_amount_derecognised_p000() {
		return r120_amount_derecognised_p000;
	}
	public void setR120_amount_derecognised_p000(BigDecimal r120_amount_derecognised_p000) {
		this.r120_amount_derecognised_p000 = r120_amount_derecognised_p000;
	}
	public BigDecimal getR120_total_p000() {
		return r120_total_p000;
	}
	public void setR120_total_p000(BigDecimal r120_total_p000) {
		this.r120_total_p000 = r120_total_p000;
	}
	public String getR121_line_no() {
		return r121_line_no;
	}
	public void setR121_line_no(String r121_line_no) {
		this.r121_line_no = r121_line_no;
	}
	public String getR121_note_holders() {
		return r121_note_holders;
	}
	public void setR121_note_holders(String r121_note_holders) {
		this.r121_note_holders = r121_note_holders;
	}
	public String getR121_name_of_instrument_programe() {
		return r121_name_of_instrument_programe;
	}
	public void setR121_name_of_instrument_programe(String r121_name_of_instrument_programe) {
		this.r121_name_of_instrument_programe = r121_name_of_instrument_programe;
	}
	public String getR121_issuing_entity_if_issued_throughan_spv() {
		return r121_issuing_entity_if_issued_throughan_spv;
	}
	public void setR121_issuing_entity_if_issued_throughan_spv(String r121_issuing_entity_if_issued_throughan_spv) {
		this.r121_issuing_entity_if_issued_throughan_spv = r121_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR121_issuance_date() {
		return r121_issuance_date;
	}
	public void setR121_issuance_date(Date r121_issuance_date) {
		this.r121_issuance_date = r121_issuance_date;
	}
	public Date getR121_contractual_maturity_date() {
		return r121_contractual_maturity_date;
	}
	public void setR121_contractual_maturity_date(Date r121_contractual_maturity_date) {
		this.r121_contractual_maturity_date = r121_contractual_maturity_date;
	}
	public Date getR121_effective_maturity_date_if_date() {
		return r121_effective_maturity_date_if_date;
	}
	public void setR121_effective_maturity_date_if_date(Date r121_effective_maturity_date_if_date) {
		this.r121_effective_maturity_date_if_date = r121_effective_maturity_date_if_date;
	}
	public BigDecimal getR121_amount() {
		return r121_amount;
	}
	public void setR121_amount(BigDecimal r121_amount) {
		this.r121_amount = r121_amount;
	}
	public BigDecimal getR121_amount_derecognised_p000() {
		return r121_amount_derecognised_p000;
	}
	public void setR121_amount_derecognised_p000(BigDecimal r121_amount_derecognised_p000) {
		this.r121_amount_derecognised_p000 = r121_amount_derecognised_p000;
	}
	public BigDecimal getR121_total_p000() {
		return r121_total_p000;
	}
	public void setR121_total_p000(BigDecimal r121_total_p000) {
		this.r121_total_p000 = r121_total_p000;
	}
	public String getR122_line_no() {
		return r122_line_no;
	}
	public void setR122_line_no(String r122_line_no) {
		this.r122_line_no = r122_line_no;
	}
	public String getR122_note_holders() {
		return r122_note_holders;
	}
	public void setR122_note_holders(String r122_note_holders) {
		this.r122_note_holders = r122_note_holders;
	}
	public String getR122_name_of_instrument_programe() {
		return r122_name_of_instrument_programe;
	}
	public void setR122_name_of_instrument_programe(String r122_name_of_instrument_programe) {
		this.r122_name_of_instrument_programe = r122_name_of_instrument_programe;
	}
	public String getR122_issuing_entity_if_issued_throughan_spv() {
		return r122_issuing_entity_if_issued_throughan_spv;
	}
	public void setR122_issuing_entity_if_issued_throughan_spv(String r122_issuing_entity_if_issued_throughan_spv) {
		this.r122_issuing_entity_if_issued_throughan_spv = r122_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR122_issuance_date() {
		return r122_issuance_date;
	}
	public void setR122_issuance_date(Date r122_issuance_date) {
		this.r122_issuance_date = r122_issuance_date;
	}
	public Date getR122_contractual_maturity_date() {
		return r122_contractual_maturity_date;
	}
	public void setR122_contractual_maturity_date(Date r122_contractual_maturity_date) {
		this.r122_contractual_maturity_date = r122_contractual_maturity_date;
	}
	public Date getR122_effective_maturity_date_if_date() {
		return r122_effective_maturity_date_if_date;
	}
	public void setR122_effective_maturity_date_if_date(Date r122_effective_maturity_date_if_date) {
		this.r122_effective_maturity_date_if_date = r122_effective_maturity_date_if_date;
	}
	public BigDecimal getR122_amount() {
		return r122_amount;
	}
	public void setR122_amount(BigDecimal r122_amount) {
		this.r122_amount = r122_amount;
	}
	public BigDecimal getR122_amount_derecognised_p000() {
		return r122_amount_derecognised_p000;
	}
	public void setR122_amount_derecognised_p000(BigDecimal r122_amount_derecognised_p000) {
		this.r122_amount_derecognised_p000 = r122_amount_derecognised_p000;
	}
	public BigDecimal getR122_total_p000() {
		return r122_total_p000;
	}
	public void setR122_total_p000(BigDecimal r122_total_p000) {
		this.r122_total_p000 = r122_total_p000;
	}
	public String getR123_line_no() {
		return r123_line_no;
	}
	public void setR123_line_no(String r123_line_no) {
		this.r123_line_no = r123_line_no;
	}
	public String getR123_note_holders() {
		return r123_note_holders;
	}
	public void setR123_note_holders(String r123_note_holders) {
		this.r123_note_holders = r123_note_holders;
	}
	public String getR123_name_of_instrument_programe() {
		return r123_name_of_instrument_programe;
	}
	public void setR123_name_of_instrument_programe(String r123_name_of_instrument_programe) {
		this.r123_name_of_instrument_programe = r123_name_of_instrument_programe;
	}
	public String getR123_issuing_entity_if_issued_throughan_spv() {
		return r123_issuing_entity_if_issued_throughan_spv;
	}
	public void setR123_issuing_entity_if_issued_throughan_spv(String r123_issuing_entity_if_issued_throughan_spv) {
		this.r123_issuing_entity_if_issued_throughan_spv = r123_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR123_issuance_date() {
		return r123_issuance_date;
	}
	public void setR123_issuance_date(Date r123_issuance_date) {
		this.r123_issuance_date = r123_issuance_date;
	}
	public Date getR123_contractual_maturity_date() {
		return r123_contractual_maturity_date;
	}
	public void setR123_contractual_maturity_date(Date r123_contractual_maturity_date) {
		this.r123_contractual_maturity_date = r123_contractual_maturity_date;
	}
	public Date getR123_effective_maturity_date_if_date() {
		return r123_effective_maturity_date_if_date;
	}
	public void setR123_effective_maturity_date_if_date(Date r123_effective_maturity_date_if_date) {
		this.r123_effective_maturity_date_if_date = r123_effective_maturity_date_if_date;
	}
	public BigDecimal getR123_amount() {
		return r123_amount;
	}
	public void setR123_amount(BigDecimal r123_amount) {
		this.r123_amount = r123_amount;
	}
	public BigDecimal getR123_amount_derecognised_p000() {
		return r123_amount_derecognised_p000;
	}
	public void setR123_amount_derecognised_p000(BigDecimal r123_amount_derecognised_p000) {
		this.r123_amount_derecognised_p000 = r123_amount_derecognised_p000;
	}
	public BigDecimal getR123_total_p000() {
		return r123_total_p000;
	}
	public void setR123_total_p000(BigDecimal r123_total_p000) {
		this.r123_total_p000 = r123_total_p000;
	}
	public String getR124_line_no() {
		return r124_line_no;
	}
	public void setR124_line_no(String r124_line_no) {
		this.r124_line_no = r124_line_no;
	}
	public String getR124_note_holders() {
		return r124_note_holders;
	}
	public void setR124_note_holders(String r124_note_holders) {
		this.r124_note_holders = r124_note_holders;
	}
	public String getR124_name_of_instrument_programe() {
		return r124_name_of_instrument_programe;
	}
	public void setR124_name_of_instrument_programe(String r124_name_of_instrument_programe) {
		this.r124_name_of_instrument_programe = r124_name_of_instrument_programe;
	}
	public String getR124_issuing_entity_if_issued_throughan_spv() {
		return r124_issuing_entity_if_issued_throughan_spv;
	}
	public void setR124_issuing_entity_if_issued_throughan_spv(String r124_issuing_entity_if_issued_throughan_spv) {
		this.r124_issuing_entity_if_issued_throughan_spv = r124_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR124_issuance_date() {
		return r124_issuance_date;
	}
	public void setR124_issuance_date(Date r124_issuance_date) {
		this.r124_issuance_date = r124_issuance_date;
	}
	public Date getR124_contractual_maturity_date() {
		return r124_contractual_maturity_date;
	}
	public void setR124_contractual_maturity_date(Date r124_contractual_maturity_date) {
		this.r124_contractual_maturity_date = r124_contractual_maturity_date;
	}
	public Date getR124_effective_maturity_date_if_date() {
		return r124_effective_maturity_date_if_date;
	}
	public void setR124_effective_maturity_date_if_date(Date r124_effective_maturity_date_if_date) {
		this.r124_effective_maturity_date_if_date = r124_effective_maturity_date_if_date;
	}
	public BigDecimal getR124_amount() {
		return r124_amount;
	}
	public void setR124_amount(BigDecimal r124_amount) {
		this.r124_amount = r124_amount;
	}
	public BigDecimal getR124_amount_derecognised_p000() {
		return r124_amount_derecognised_p000;
	}
	public void setR124_amount_derecognised_p000(BigDecimal r124_amount_derecognised_p000) {
		this.r124_amount_derecognised_p000 = r124_amount_derecognised_p000;
	}
	public BigDecimal getR124_total_p000() {
		return r124_total_p000;
	}
	public void setR124_total_p000(BigDecimal r124_total_p000) {
		this.r124_total_p000 = r124_total_p000;
	}
	public String getR125_line_no() {
		return r125_line_no;
	}
	public void setR125_line_no(String r125_line_no) {
		this.r125_line_no = r125_line_no;
	}
	public String getR125_note_holders() {
		return r125_note_holders;
	}
	public void setR125_note_holders(String r125_note_holders) {
		this.r125_note_holders = r125_note_holders;
	}
	public String getR125_name_of_instrument_programe() {
		return r125_name_of_instrument_programe;
	}
	public void setR125_name_of_instrument_programe(String r125_name_of_instrument_programe) {
		this.r125_name_of_instrument_programe = r125_name_of_instrument_programe;
	}
	public String getR125_issuing_entity_if_issued_throughan_spv() {
		return r125_issuing_entity_if_issued_throughan_spv;
	}
	public void setR125_issuing_entity_if_issued_throughan_spv(String r125_issuing_entity_if_issued_throughan_spv) {
		this.r125_issuing_entity_if_issued_throughan_spv = r125_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR125_issuance_date() {
		return r125_issuance_date;
	}
	public void setR125_issuance_date(Date r125_issuance_date) {
		this.r125_issuance_date = r125_issuance_date;
	}
	public Date getR125_contractual_maturity_date() {
		return r125_contractual_maturity_date;
	}
	public void setR125_contractual_maturity_date(Date r125_contractual_maturity_date) {
		this.r125_contractual_maturity_date = r125_contractual_maturity_date;
	}
	public Date getR125_effective_maturity_date_if_date() {
		return r125_effective_maturity_date_if_date;
	}
	public void setR125_effective_maturity_date_if_date(Date r125_effective_maturity_date_if_date) {
		this.r125_effective_maturity_date_if_date = r125_effective_maturity_date_if_date;
	}
	public BigDecimal getR125_amount() {
		return r125_amount;
	}
	public void setR125_amount(BigDecimal r125_amount) {
		this.r125_amount = r125_amount;
	}
	public BigDecimal getR125_amount_derecognised_p000() {
		return r125_amount_derecognised_p000;
	}
	public void setR125_amount_derecognised_p000(BigDecimal r125_amount_derecognised_p000) {
		this.r125_amount_derecognised_p000 = r125_amount_derecognised_p000;
	}
	public BigDecimal getR125_total_p000() {
		return r125_total_p000;
	}
	public void setR125_total_p000(BigDecimal r125_total_p000) {
		this.r125_total_p000 = r125_total_p000;
	}
	public String getR126_line_no() {
		return r126_line_no;
	}
	public void setR126_line_no(String r126_line_no) {
		this.r126_line_no = r126_line_no;
	}
	public String getR126_note_holders() {
		return r126_note_holders;
	}
	public void setR126_note_holders(String r126_note_holders) {
		this.r126_note_holders = r126_note_holders;
	}
	public String getR126_name_of_instrument_programe() {
		return r126_name_of_instrument_programe;
	}
	public void setR126_name_of_instrument_programe(String r126_name_of_instrument_programe) {
		this.r126_name_of_instrument_programe = r126_name_of_instrument_programe;
	}
	public String getR126_issuing_entity_if_issued_throughan_spv() {
		return r126_issuing_entity_if_issued_throughan_spv;
	}
	public void setR126_issuing_entity_if_issued_throughan_spv(String r126_issuing_entity_if_issued_throughan_spv) {
		this.r126_issuing_entity_if_issued_throughan_spv = r126_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR126_issuance_date() {
		return r126_issuance_date;
	}
	public void setR126_issuance_date(Date r126_issuance_date) {
		this.r126_issuance_date = r126_issuance_date;
	}
	public Date getR126_contractual_maturity_date() {
		return r126_contractual_maturity_date;
	}
	public void setR126_contractual_maturity_date(Date r126_contractual_maturity_date) {
		this.r126_contractual_maturity_date = r126_contractual_maturity_date;
	}
	public Date getR126_effective_maturity_date_if_date() {
		return r126_effective_maturity_date_if_date;
	}
	public void setR126_effective_maturity_date_if_date(Date r126_effective_maturity_date_if_date) {
		this.r126_effective_maturity_date_if_date = r126_effective_maturity_date_if_date;
	}
	public BigDecimal getR126_amount() {
		return r126_amount;
	}
	public void setR126_amount(BigDecimal r126_amount) {
		this.r126_amount = r126_amount;
	}
	public BigDecimal getR126_amount_derecognised_p000() {
		return r126_amount_derecognised_p000;
	}
	public void setR126_amount_derecognised_p000(BigDecimal r126_amount_derecognised_p000) {
		this.r126_amount_derecognised_p000 = r126_amount_derecognised_p000;
	}
	public BigDecimal getR126_total_p000() {
		return r126_total_p000;
	}
	public void setR126_total_p000(BigDecimal r126_total_p000) {
		this.r126_total_p000 = r126_total_p000;
	}
	public String getR127_line_no() {
		return r127_line_no;
	}
	public void setR127_line_no(String r127_line_no) {
		this.r127_line_no = r127_line_no;
	}
	public String getR127_note_holders() {
		return r127_note_holders;
	}
	public void setR127_note_holders(String r127_note_holders) {
		this.r127_note_holders = r127_note_holders;
	}
	public String getR127_name_of_instrument_programe() {
		return r127_name_of_instrument_programe;
	}
	public void setR127_name_of_instrument_programe(String r127_name_of_instrument_programe) {
		this.r127_name_of_instrument_programe = r127_name_of_instrument_programe;
	}
	public String getR127_issuing_entity_if_issued_throughan_spv() {
		return r127_issuing_entity_if_issued_throughan_spv;
	}
	public void setR127_issuing_entity_if_issued_throughan_spv(String r127_issuing_entity_if_issued_throughan_spv) {
		this.r127_issuing_entity_if_issued_throughan_spv = r127_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR127_issuance_date() {
		return r127_issuance_date;
	}
	public void setR127_issuance_date(Date r127_issuance_date) {
		this.r127_issuance_date = r127_issuance_date;
	}
	public Date getR127_contractual_maturity_date() {
		return r127_contractual_maturity_date;
	}
	public void setR127_contractual_maturity_date(Date r127_contractual_maturity_date) {
		this.r127_contractual_maturity_date = r127_contractual_maturity_date;
	}
	public Date getR127_effective_maturity_date_if_date() {
		return r127_effective_maturity_date_if_date;
	}
	public void setR127_effective_maturity_date_if_date(Date r127_effective_maturity_date_if_date) {
		this.r127_effective_maturity_date_if_date = r127_effective_maturity_date_if_date;
	}
	public BigDecimal getR127_amount() {
		return r127_amount;
	}
	public void setR127_amount(BigDecimal r127_amount) {
		this.r127_amount = r127_amount;
	}
	public BigDecimal getR127_amount_derecognised_p000() {
		return r127_amount_derecognised_p000;
	}
	public void setR127_amount_derecognised_p000(BigDecimal r127_amount_derecognised_p000) {
		this.r127_amount_derecognised_p000 = r127_amount_derecognised_p000;
	}
	public BigDecimal getR127_total_p000() {
		return r127_total_p000;
	}
	public void setR127_total_p000(BigDecimal r127_total_p000) {
		this.r127_total_p000 = r127_total_p000;
	}
	public String getR128_line_no() {
		return r128_line_no;
	}
	public void setR128_line_no(String r128_line_no) {
		this.r128_line_no = r128_line_no;
	}
	public String getR128_note_holders() {
		return r128_note_holders;
	}
	public void setR128_note_holders(String r128_note_holders) {
		this.r128_note_holders = r128_note_holders;
	}
	public String getR128_name_of_instrument_programe() {
		return r128_name_of_instrument_programe;
	}
	public void setR128_name_of_instrument_programe(String r128_name_of_instrument_programe) {
		this.r128_name_of_instrument_programe = r128_name_of_instrument_programe;
	}
	public String getR128_issuing_entity_if_issued_throughan_spv() {
		return r128_issuing_entity_if_issued_throughan_spv;
	}
	public void setR128_issuing_entity_if_issued_throughan_spv(String r128_issuing_entity_if_issued_throughan_spv) {
		this.r128_issuing_entity_if_issued_throughan_spv = r128_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR128_issuance_date() {
		return r128_issuance_date;
	}
	public void setR128_issuance_date(Date r128_issuance_date) {
		this.r128_issuance_date = r128_issuance_date;
	}
	public Date getR128_contractual_maturity_date() {
		return r128_contractual_maturity_date;
	}
	public void setR128_contractual_maturity_date(Date r128_contractual_maturity_date) {
		this.r128_contractual_maturity_date = r128_contractual_maturity_date;
	}
	public Date getR128_effective_maturity_date_if_date() {
		return r128_effective_maturity_date_if_date;
	}
	public void setR128_effective_maturity_date_if_date(Date r128_effective_maturity_date_if_date) {
		this.r128_effective_maturity_date_if_date = r128_effective_maturity_date_if_date;
	}
	public BigDecimal getR128_amount() {
		return r128_amount;
	}
	public void setR128_amount(BigDecimal r128_amount) {
		this.r128_amount = r128_amount;
	}
	public BigDecimal getR128_amount_derecognised_p000() {
		return r128_amount_derecognised_p000;
	}
	public void setR128_amount_derecognised_p000(BigDecimal r128_amount_derecognised_p000) {
		this.r128_amount_derecognised_p000 = r128_amount_derecognised_p000;
	}
	public BigDecimal getR128_total_p000() {
		return r128_total_p000;
	}
	public void setR128_total_p000(BigDecimal r128_total_p000) {
		this.r128_total_p000 = r128_total_p000;
	}
	public String getR129_line_no() {
		return r129_line_no;
	}
	public void setR129_line_no(String r129_line_no) {
		this.r129_line_no = r129_line_no;
	}
	public String getR129_note_holders() {
		return r129_note_holders;
	}
	public void setR129_note_holders(String r129_note_holders) {
		this.r129_note_holders = r129_note_holders;
	}
	public String getR129_name_of_instrument_programe() {
		return r129_name_of_instrument_programe;
	}
	public void setR129_name_of_instrument_programe(String r129_name_of_instrument_programe) {
		this.r129_name_of_instrument_programe = r129_name_of_instrument_programe;
	}
	public String getR129_issuing_entity_if_issued_throughan_spv() {
		return r129_issuing_entity_if_issued_throughan_spv;
	}
	public void setR129_issuing_entity_if_issued_throughan_spv(String r129_issuing_entity_if_issued_throughan_spv) {
		this.r129_issuing_entity_if_issued_throughan_spv = r129_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR129_issuance_date() {
		return r129_issuance_date;
	}
	public void setR129_issuance_date(Date r129_issuance_date) {
		this.r129_issuance_date = r129_issuance_date;
	}
	public Date getR129_contractual_maturity_date() {
		return r129_contractual_maturity_date;
	}
	public void setR129_contractual_maturity_date(Date r129_contractual_maturity_date) {
		this.r129_contractual_maturity_date = r129_contractual_maturity_date;
	}
	public Date getR129_effective_maturity_date_if_date() {
		return r129_effective_maturity_date_if_date;
	}
	public void setR129_effective_maturity_date_if_date(Date r129_effective_maturity_date_if_date) {
		this.r129_effective_maturity_date_if_date = r129_effective_maturity_date_if_date;
	}
	public BigDecimal getR129_amount() {
		return r129_amount;
	}
	public void setR129_amount(BigDecimal r129_amount) {
		this.r129_amount = r129_amount;
	}
	public BigDecimal getR129_amount_derecognised_p000() {
		return r129_amount_derecognised_p000;
	}
	public void setR129_amount_derecognised_p000(BigDecimal r129_amount_derecognised_p000) {
		this.r129_amount_derecognised_p000 = r129_amount_derecognised_p000;
	}
	public BigDecimal getR129_total_p000() {
		return r129_total_p000;
	}
	public void setR129_total_p000(BigDecimal r129_total_p000) {
		this.r129_total_p000 = r129_total_p000;
	}
	public String getR130_line_no() {
		return r130_line_no;
	}
	public void setR130_line_no(String r130_line_no) {
		this.r130_line_no = r130_line_no;
	}
	public String getR130_note_holders() {
		return r130_note_holders;
	}
	public void setR130_note_holders(String r130_note_holders) {
		this.r130_note_holders = r130_note_holders;
	}
	public String getR130_name_of_instrument_programe() {
		return r130_name_of_instrument_programe;
	}
	public void setR130_name_of_instrument_programe(String r130_name_of_instrument_programe) {
		this.r130_name_of_instrument_programe = r130_name_of_instrument_programe;
	}
	public String getR130_issuing_entity_if_issued_throughan_spv() {
		return r130_issuing_entity_if_issued_throughan_spv;
	}
	public void setR130_issuing_entity_if_issued_throughan_spv(String r130_issuing_entity_if_issued_throughan_spv) {
		this.r130_issuing_entity_if_issued_throughan_spv = r130_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR130_issuance_date() {
		return r130_issuance_date;
	}
	public void setR130_issuance_date(Date r130_issuance_date) {
		this.r130_issuance_date = r130_issuance_date;
	}
	public Date getR130_contractual_maturity_date() {
		return r130_contractual_maturity_date;
	}
	public void setR130_contractual_maturity_date(Date r130_contractual_maturity_date) {
		this.r130_contractual_maturity_date = r130_contractual_maturity_date;
	}
	public Date getR130_effective_maturity_date_if_date() {
		return r130_effective_maturity_date_if_date;
	}
	public void setR130_effective_maturity_date_if_date(Date r130_effective_maturity_date_if_date) {
		this.r130_effective_maturity_date_if_date = r130_effective_maturity_date_if_date;
	}
	public BigDecimal getR130_amount() {
		return r130_amount;
	}
	public void setR130_amount(BigDecimal r130_amount) {
		this.r130_amount = r130_amount;
	}
	public BigDecimal getR130_amount_derecognised_p000() {
		return r130_amount_derecognised_p000;
	}
	public void setR130_amount_derecognised_p000(BigDecimal r130_amount_derecognised_p000) {
		this.r130_amount_derecognised_p000 = r130_amount_derecognised_p000;
	}
	public BigDecimal getR130_total_p000() {
		return r130_total_p000;
	}
	public void setR130_total_p000(BigDecimal r130_total_p000) {
		this.r130_total_p000 = r130_total_p000;
	}
	public String getR131_line_no() {
		return r131_line_no;
	}
	public void setR131_line_no(String r131_line_no) {
		this.r131_line_no = r131_line_no;
	}
	public String getR131_note_holders() {
		return r131_note_holders;
	}
	public void setR131_note_holders(String r131_note_holders) {
		this.r131_note_holders = r131_note_holders;
	}
	public String getR131_name_of_instrument_programe() {
		return r131_name_of_instrument_programe;
	}
	public void setR131_name_of_instrument_programe(String r131_name_of_instrument_programe) {
		this.r131_name_of_instrument_programe = r131_name_of_instrument_programe;
	}
	public String getR131_issuing_entity_if_issued_throughan_spv() {
		return r131_issuing_entity_if_issued_throughan_spv;
	}
	public void setR131_issuing_entity_if_issued_throughan_spv(String r131_issuing_entity_if_issued_throughan_spv) {
		this.r131_issuing_entity_if_issued_throughan_spv = r131_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR131_issuance_date() {
		return r131_issuance_date;
	}
	public void setR131_issuance_date(Date r131_issuance_date) {
		this.r131_issuance_date = r131_issuance_date;
	}
	public Date getR131_contractual_maturity_date() {
		return r131_contractual_maturity_date;
	}
	public void setR131_contractual_maturity_date(Date r131_contractual_maturity_date) {
		this.r131_contractual_maturity_date = r131_contractual_maturity_date;
	}
	public Date getR131_effective_maturity_date_if_date() {
		return r131_effective_maturity_date_if_date;
	}
	public void setR131_effective_maturity_date_if_date(Date r131_effective_maturity_date_if_date) {
		this.r131_effective_maturity_date_if_date = r131_effective_maturity_date_if_date;
	}
	public BigDecimal getR131_amount() {
		return r131_amount;
	}
	public void setR131_amount(BigDecimal r131_amount) {
		this.r131_amount = r131_amount;
	}
	public BigDecimal getR131_amount_derecognised_p000() {
		return r131_amount_derecognised_p000;
	}
	public void setR131_amount_derecognised_p000(BigDecimal r131_amount_derecognised_p000) {
		this.r131_amount_derecognised_p000 = r131_amount_derecognised_p000;
	}
	public BigDecimal getR131_total_p000() {
		return r131_total_p000;
	}
	public void setR131_total_p000(BigDecimal r131_total_p000) {
		this.r131_total_p000 = r131_total_p000;
	}
	public String getR132_line_no() {
		return r132_line_no;
	}
	public void setR132_line_no(String r132_line_no) {
		this.r132_line_no = r132_line_no;
	}
	public String getR132_note_holders() {
		return r132_note_holders;
	}
	public void setR132_note_holders(String r132_note_holders) {
		this.r132_note_holders = r132_note_holders;
	}
	public String getR132_name_of_instrument_programe() {
		return r132_name_of_instrument_programe;
	}
	public void setR132_name_of_instrument_programe(String r132_name_of_instrument_programe) {
		this.r132_name_of_instrument_programe = r132_name_of_instrument_programe;
	}
	public String getR132_issuing_entity_if_issued_throughan_spv() {
		return r132_issuing_entity_if_issued_throughan_spv;
	}
	public void setR132_issuing_entity_if_issued_throughan_spv(String r132_issuing_entity_if_issued_throughan_spv) {
		this.r132_issuing_entity_if_issued_throughan_spv = r132_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR132_issuance_date() {
		return r132_issuance_date;
	}
	public void setR132_issuance_date(Date r132_issuance_date) {
		this.r132_issuance_date = r132_issuance_date;
	}
	public Date getR132_contractual_maturity_date() {
		return r132_contractual_maturity_date;
	}
	public void setR132_contractual_maturity_date(Date r132_contractual_maturity_date) {
		this.r132_contractual_maturity_date = r132_contractual_maturity_date;
	}
	public Date getR132_effective_maturity_date_if_date() {
		return r132_effective_maturity_date_if_date;
	}
	public void setR132_effective_maturity_date_if_date(Date r132_effective_maturity_date_if_date) {
		this.r132_effective_maturity_date_if_date = r132_effective_maturity_date_if_date;
	}
	public BigDecimal getR132_amount() {
		return r132_amount;
	}
	public void setR132_amount(BigDecimal r132_amount) {
		this.r132_amount = r132_amount;
	}
	public BigDecimal getR132_amount_derecognised_p000() {
		return r132_amount_derecognised_p000;
	}
	public void setR132_amount_derecognised_p000(BigDecimal r132_amount_derecognised_p000) {
		this.r132_amount_derecognised_p000 = r132_amount_derecognised_p000;
	}
	public BigDecimal getR132_total_p000() {
		return r132_total_p000;
	}
	public void setR132_total_p000(BigDecimal r132_total_p000) {
		this.r132_total_p000 = r132_total_p000;
	}
	public String getR133_line_no() {
		return r133_line_no;
	}
	public void setR133_line_no(String r133_line_no) {
		this.r133_line_no = r133_line_no;
	}
	public String getR133_note_holders() {
		return r133_note_holders;
	}
	public void setR133_note_holders(String r133_note_holders) {
		this.r133_note_holders = r133_note_holders;
	}
	public String getR133_name_of_instrument_programe() {
		return r133_name_of_instrument_programe;
	}
	public void setR133_name_of_instrument_programe(String r133_name_of_instrument_programe) {
		this.r133_name_of_instrument_programe = r133_name_of_instrument_programe;
	}
	public String getR133_issuing_entity_if_issued_throughan_spv() {
		return r133_issuing_entity_if_issued_throughan_spv;
	}
	public void setR133_issuing_entity_if_issued_throughan_spv(String r133_issuing_entity_if_issued_throughan_spv) {
		this.r133_issuing_entity_if_issued_throughan_spv = r133_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR133_issuance_date() {
		return r133_issuance_date;
	}
	public void setR133_issuance_date(Date r133_issuance_date) {
		this.r133_issuance_date = r133_issuance_date;
	}
	public Date getR133_contractual_maturity_date() {
		return r133_contractual_maturity_date;
	}
	public void setR133_contractual_maturity_date(Date r133_contractual_maturity_date) {
		this.r133_contractual_maturity_date = r133_contractual_maturity_date;
	}
	public Date getR133_effective_maturity_date_if_date() {
		return r133_effective_maturity_date_if_date;
	}
	public void setR133_effective_maturity_date_if_date(Date r133_effective_maturity_date_if_date) {
		this.r133_effective_maturity_date_if_date = r133_effective_maturity_date_if_date;
	}
	public BigDecimal getR133_amount() {
		return r133_amount;
	}
	public void setR133_amount(BigDecimal r133_amount) {
		this.r133_amount = r133_amount;
	}
	public BigDecimal getR133_amount_derecognised_p000() {
		return r133_amount_derecognised_p000;
	}
	public void setR133_amount_derecognised_p000(BigDecimal r133_amount_derecognised_p000) {
		this.r133_amount_derecognised_p000 = r133_amount_derecognised_p000;
	}
	public BigDecimal getR133_total_p000() {
		return r133_total_p000;
	}
	public void setR133_total_p000(BigDecimal r133_total_p000) {
		this.r133_total_p000 = r133_total_p000;
	}
	public String getR134_line_no() {
		return r134_line_no;
	}
	public void setR134_line_no(String r134_line_no) {
		this.r134_line_no = r134_line_no;
	}
	public String getR134_note_holders() {
		return r134_note_holders;
	}
	public void setR134_note_holders(String r134_note_holders) {
		this.r134_note_holders = r134_note_holders;
	}
	public String getR134_name_of_instrument_programe() {
		return r134_name_of_instrument_programe;
	}
	public void setR134_name_of_instrument_programe(String r134_name_of_instrument_programe) {
		this.r134_name_of_instrument_programe = r134_name_of_instrument_programe;
	}
	public String getR134_issuing_entity_if_issued_throughan_spv() {
		return r134_issuing_entity_if_issued_throughan_spv;
	}
	public void setR134_issuing_entity_if_issued_throughan_spv(String r134_issuing_entity_if_issued_throughan_spv) {
		this.r134_issuing_entity_if_issued_throughan_spv = r134_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR134_issuance_date() {
		return r134_issuance_date;
	}
	public void setR134_issuance_date(Date r134_issuance_date) {
		this.r134_issuance_date = r134_issuance_date;
	}
	public Date getR134_contractual_maturity_date() {
		return r134_contractual_maturity_date;
	}
	public void setR134_contractual_maturity_date(Date r134_contractual_maturity_date) {
		this.r134_contractual_maturity_date = r134_contractual_maturity_date;
	}
	public Date getR134_effective_maturity_date_if_date() {
		return r134_effective_maturity_date_if_date;
	}
	public void setR134_effective_maturity_date_if_date(Date r134_effective_maturity_date_if_date) {
		this.r134_effective_maturity_date_if_date = r134_effective_maturity_date_if_date;
	}
	public BigDecimal getR134_amount() {
		return r134_amount;
	}
	public void setR134_amount(BigDecimal r134_amount) {
		this.r134_amount = r134_amount;
	}
	public BigDecimal getR134_amount_derecognised_p000() {
		return r134_amount_derecognised_p000;
	}
	public void setR134_amount_derecognised_p000(BigDecimal r134_amount_derecognised_p000) {
		this.r134_amount_derecognised_p000 = r134_amount_derecognised_p000;
	}
	public BigDecimal getR134_total_p000() {
		return r134_total_p000;
	}
	public void setR134_total_p000(BigDecimal r134_total_p000) {
		this.r134_total_p000 = r134_total_p000;
	}
	public String getR135_line_no() {
		return r135_line_no;
	}
	public void setR135_line_no(String r135_line_no) {
		this.r135_line_no = r135_line_no;
	}
	public String getR135_note_holders() {
		return r135_note_holders;
	}
	public void setR135_note_holders(String r135_note_holders) {
		this.r135_note_holders = r135_note_holders;
	}
	public String getR135_name_of_instrument_programe() {
		return r135_name_of_instrument_programe;
	}
	public void setR135_name_of_instrument_programe(String r135_name_of_instrument_programe) {
		this.r135_name_of_instrument_programe = r135_name_of_instrument_programe;
	}
	public String getR135_issuing_entity_if_issued_throughan_spv() {
		return r135_issuing_entity_if_issued_throughan_spv;
	}
	public void setR135_issuing_entity_if_issued_throughan_spv(String r135_issuing_entity_if_issued_throughan_spv) {
		this.r135_issuing_entity_if_issued_throughan_spv = r135_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR135_issuance_date() {
		return r135_issuance_date;
	}
	public void setR135_issuance_date(Date r135_issuance_date) {
		this.r135_issuance_date = r135_issuance_date;
	}
	public Date getR135_contractual_maturity_date() {
		return r135_contractual_maturity_date;
	}
	public void setR135_contractual_maturity_date(Date r135_contractual_maturity_date) {
		this.r135_contractual_maturity_date = r135_contractual_maturity_date;
	}
	public Date getR135_effective_maturity_date_if_date() {
		return r135_effective_maturity_date_if_date;
	}
	public void setR135_effective_maturity_date_if_date(Date r135_effective_maturity_date_if_date) {
		this.r135_effective_maturity_date_if_date = r135_effective_maturity_date_if_date;
	}
	public BigDecimal getR135_amount() {
		return r135_amount;
	}
	public void setR135_amount(BigDecimal r135_amount) {
		this.r135_amount = r135_amount;
	}
	public BigDecimal getR135_amount_derecognised_p000() {
		return r135_amount_derecognised_p000;
	}
	public void setR135_amount_derecognised_p000(BigDecimal r135_amount_derecognised_p000) {
		this.r135_amount_derecognised_p000 = r135_amount_derecognised_p000;
	}
	public BigDecimal getR135_total_p000() {
		return r135_total_p000;
	}
	public void setR135_total_p000(BigDecimal r135_total_p000) {
		this.r135_total_p000 = r135_total_p000;
	}
	public String getR136_line_no() {
		return r136_line_no;
	}
	public void setR136_line_no(String r136_line_no) {
		this.r136_line_no = r136_line_no;
	}
	public String getR136_note_holders() {
		return r136_note_holders;
	}
	public void setR136_note_holders(String r136_note_holders) {
		this.r136_note_holders = r136_note_holders;
	}
	public String getR136_name_of_instrument_programe() {
		return r136_name_of_instrument_programe;
	}
	public void setR136_name_of_instrument_programe(String r136_name_of_instrument_programe) {
		this.r136_name_of_instrument_programe = r136_name_of_instrument_programe;
	}
	public String getR136_issuing_entity_if_issued_throughan_spv() {
		return r136_issuing_entity_if_issued_throughan_spv;
	}
	public void setR136_issuing_entity_if_issued_throughan_spv(String r136_issuing_entity_if_issued_throughan_spv) {
		this.r136_issuing_entity_if_issued_throughan_spv = r136_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR136_issuance_date() {
		return r136_issuance_date;
	}
	public void setR136_issuance_date(Date r136_issuance_date) {
		this.r136_issuance_date = r136_issuance_date;
	}
	public Date getR136_contractual_maturity_date() {
		return r136_contractual_maturity_date;
	}
	public void setR136_contractual_maturity_date(Date r136_contractual_maturity_date) {
		this.r136_contractual_maturity_date = r136_contractual_maturity_date;
	}
	public Date getR136_effective_maturity_date_if_date() {
		return r136_effective_maturity_date_if_date;
	}
	public void setR136_effective_maturity_date_if_date(Date r136_effective_maturity_date_if_date) {
		this.r136_effective_maturity_date_if_date = r136_effective_maturity_date_if_date;
	}
	public BigDecimal getR136_amount() {
		return r136_amount;
	}
	public void setR136_amount(BigDecimal r136_amount) {
		this.r136_amount = r136_amount;
	}
	public BigDecimal getR136_amount_derecognised_p000() {
		return r136_amount_derecognised_p000;
	}
	public void setR136_amount_derecognised_p000(BigDecimal r136_amount_derecognised_p000) {
		this.r136_amount_derecognised_p000 = r136_amount_derecognised_p000;
	}
	public BigDecimal getR136_total_p000() {
		return r136_total_p000;
	}
	public void setR136_total_p000(BigDecimal r136_total_p000) {
		this.r136_total_p000 = r136_total_p000;
	}
	public String getR137_line_no() {
		return r137_line_no;
	}
	public void setR137_line_no(String r137_line_no) {
		this.r137_line_no = r137_line_no;
	}
	public String getR137_note_holders() {
		return r137_note_holders;
	}
	public void setR137_note_holders(String r137_note_holders) {
		this.r137_note_holders = r137_note_holders;
	}
	public String getR137_name_of_instrument_programe() {
		return r137_name_of_instrument_programe;
	}
	public void setR137_name_of_instrument_programe(String r137_name_of_instrument_programe) {
		this.r137_name_of_instrument_programe = r137_name_of_instrument_programe;
	}
	public String getR137_issuing_entity_if_issued_throughan_spv() {
		return r137_issuing_entity_if_issued_throughan_spv;
	}
	public void setR137_issuing_entity_if_issued_throughan_spv(String r137_issuing_entity_if_issued_throughan_spv) {
		this.r137_issuing_entity_if_issued_throughan_spv = r137_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR137_issuance_date() {
		return r137_issuance_date;
	}
	public void setR137_issuance_date(Date r137_issuance_date) {
		this.r137_issuance_date = r137_issuance_date;
	}
	public Date getR137_contractual_maturity_date() {
		return r137_contractual_maturity_date;
	}
	public void setR137_contractual_maturity_date(Date r137_contractual_maturity_date) {
		this.r137_contractual_maturity_date = r137_contractual_maturity_date;
	}
	public Date getR137_effective_maturity_date_if_date() {
		return r137_effective_maturity_date_if_date;
	}
	public void setR137_effective_maturity_date_if_date(Date r137_effective_maturity_date_if_date) {
		this.r137_effective_maturity_date_if_date = r137_effective_maturity_date_if_date;
	}
	public BigDecimal getR137_amount() {
		return r137_amount;
	}
	public void setR137_amount(BigDecimal r137_amount) {
		this.r137_amount = r137_amount;
	}
	public BigDecimal getR137_amount_derecognised_p000() {
		return r137_amount_derecognised_p000;
	}
	public void setR137_amount_derecognised_p000(BigDecimal r137_amount_derecognised_p000) {
		this.r137_amount_derecognised_p000 = r137_amount_derecognised_p000;
	}
	public BigDecimal getR137_total_p000() {
		return r137_total_p000;
	}
	public void setR137_total_p000(BigDecimal r137_total_p000) {
		this.r137_total_p000 = r137_total_p000;
	}
	public String getR138_line_no() {
		return r138_line_no;
	}
	public void setR138_line_no(String r138_line_no) {
		this.r138_line_no = r138_line_no;
	}
	public String getR138_note_holders() {
		return r138_note_holders;
	}
	public void setR138_note_holders(String r138_note_holders) {
		this.r138_note_holders = r138_note_holders;
	}
	public String getR138_name_of_instrument_programe() {
		return r138_name_of_instrument_programe;
	}
	public void setR138_name_of_instrument_programe(String r138_name_of_instrument_programe) {
		this.r138_name_of_instrument_programe = r138_name_of_instrument_programe;
	}
	public String getR138_issuing_entity_if_issued_throughan_spv() {
		return r138_issuing_entity_if_issued_throughan_spv;
	}
	public void setR138_issuing_entity_if_issued_throughan_spv(String r138_issuing_entity_if_issued_throughan_spv) {
		this.r138_issuing_entity_if_issued_throughan_spv = r138_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR138_issuance_date() {
		return r138_issuance_date;
	}
	public void setR138_issuance_date(Date r138_issuance_date) {
		this.r138_issuance_date = r138_issuance_date;
	}
	public Date getR138_contractual_maturity_date() {
		return r138_contractual_maturity_date;
	}
	public void setR138_contractual_maturity_date(Date r138_contractual_maturity_date) {
		this.r138_contractual_maturity_date = r138_contractual_maturity_date;
	}
	public Date getR138_effective_maturity_date_if_date() {
		return r138_effective_maturity_date_if_date;
	}
	public void setR138_effective_maturity_date_if_date(Date r138_effective_maturity_date_if_date) {
		this.r138_effective_maturity_date_if_date = r138_effective_maturity_date_if_date;
	}
	public BigDecimal getR138_amount() {
		return r138_amount;
	}
	public void setR138_amount(BigDecimal r138_amount) {
		this.r138_amount = r138_amount;
	}
	public BigDecimal getR138_amount_derecognised_p000() {
		return r138_amount_derecognised_p000;
	}
	public void setR138_amount_derecognised_p000(BigDecimal r138_amount_derecognised_p000) {
		this.r138_amount_derecognised_p000 = r138_amount_derecognised_p000;
	}
	public BigDecimal getR138_total_p000() {
		return r138_total_p000;
	}
	public void setR138_total_p000(BigDecimal r138_total_p000) {
		this.r138_total_p000 = r138_total_p000;
	}
	public String getR139_line_no() {
		return r139_line_no;
	}
	public void setR139_line_no(String r139_line_no) {
		this.r139_line_no = r139_line_no;
	}
	public String getR139_note_holders() {
		return r139_note_holders;
	}
	public void setR139_note_holders(String r139_note_holders) {
		this.r139_note_holders = r139_note_holders;
	}
	public String getR139_name_of_instrument_programe() {
		return r139_name_of_instrument_programe;
	}
	public void setR139_name_of_instrument_programe(String r139_name_of_instrument_programe) {
		this.r139_name_of_instrument_programe = r139_name_of_instrument_programe;
	}
	public String getR139_issuing_entity_if_issued_throughan_spv() {
		return r139_issuing_entity_if_issued_throughan_spv;
	}
	public void setR139_issuing_entity_if_issued_throughan_spv(String r139_issuing_entity_if_issued_throughan_spv) {
		this.r139_issuing_entity_if_issued_throughan_spv = r139_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR139_issuance_date() {
		return r139_issuance_date;
	}
	public void setR139_issuance_date(Date r139_issuance_date) {
		this.r139_issuance_date = r139_issuance_date;
	}
	public Date getR139_contractual_maturity_date() {
		return r139_contractual_maturity_date;
	}
	public void setR139_contractual_maturity_date(Date r139_contractual_maturity_date) {
		this.r139_contractual_maturity_date = r139_contractual_maturity_date;
	}
	public Date getR139_effective_maturity_date_if_date() {
		return r139_effective_maturity_date_if_date;
	}
	public void setR139_effective_maturity_date_if_date(Date r139_effective_maturity_date_if_date) {
		this.r139_effective_maturity_date_if_date = r139_effective_maturity_date_if_date;
	}
	public BigDecimal getR139_amount() {
		return r139_amount;
	}
	public void setR139_amount(BigDecimal r139_amount) {
		this.r139_amount = r139_amount;
	}
	public BigDecimal getR139_amount_derecognised_p000() {
		return r139_amount_derecognised_p000;
	}
	public void setR139_amount_derecognised_p000(BigDecimal r139_amount_derecognised_p000) {
		this.r139_amount_derecognised_p000 = r139_amount_derecognised_p000;
	}
	public BigDecimal getR139_total_p000() {
		return r139_total_p000;
	}
	public void setR139_total_p000(BigDecimal r139_total_p000) {
		this.r139_total_p000 = r139_total_p000;
	}
	public String getR140_line_no() {
		return r140_line_no;
	}
	public void setR140_line_no(String r140_line_no) {
		this.r140_line_no = r140_line_no;
	}
	public String getR140_note_holders() {
		return r140_note_holders;
	}
	public void setR140_note_holders(String r140_note_holders) {
		this.r140_note_holders = r140_note_holders;
	}
	public String getR140_name_of_instrument_programe() {
		return r140_name_of_instrument_programe;
	}
	public void setR140_name_of_instrument_programe(String r140_name_of_instrument_programe) {
		this.r140_name_of_instrument_programe = r140_name_of_instrument_programe;
	}
	public String getR140_issuing_entity_if_issued_throughan_spv() {
		return r140_issuing_entity_if_issued_throughan_spv;
	}
	public void setR140_issuing_entity_if_issued_throughan_spv(String r140_issuing_entity_if_issued_throughan_spv) {
		this.r140_issuing_entity_if_issued_throughan_spv = r140_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR140_issuance_date() {
		return r140_issuance_date;
	}
	public void setR140_issuance_date(Date r140_issuance_date) {
		this.r140_issuance_date = r140_issuance_date;
	}
	public Date getR140_contractual_maturity_date() {
		return r140_contractual_maturity_date;
	}
	public void setR140_contractual_maturity_date(Date r140_contractual_maturity_date) {
		this.r140_contractual_maturity_date = r140_contractual_maturity_date;
	}
	public Date getR140_effective_maturity_date_if_date() {
		return r140_effective_maturity_date_if_date;
	}
	public void setR140_effective_maturity_date_if_date(Date r140_effective_maturity_date_if_date) {
		this.r140_effective_maturity_date_if_date = r140_effective_maturity_date_if_date;
	}
	public BigDecimal getR140_amount() {
		return r140_amount;
	}
	public void setR140_amount(BigDecimal r140_amount) {
		this.r140_amount = r140_amount;
	}
	public BigDecimal getR140_amount_derecognised_p000() {
		return r140_amount_derecognised_p000;
	}
	public void setR140_amount_derecognised_p000(BigDecimal r140_amount_derecognised_p000) {
		this.r140_amount_derecognised_p000 = r140_amount_derecognised_p000;
	}
	public BigDecimal getR140_total_p000() {
		return r140_total_p000;
	}
	public void setR140_total_p000(BigDecimal r140_total_p000) {
		this.r140_total_p000 = r140_total_p000;
	}
	public String getR141_line_no() {
		return r141_line_no;
	}
	public void setR141_line_no(String r141_line_no) {
		this.r141_line_no = r141_line_no;
	}
	public String getR141_note_holders() {
		return r141_note_holders;
	}
	public void setR141_note_holders(String r141_note_holders) {
		this.r141_note_holders = r141_note_holders;
	}
	public String getR141_name_of_instrument_programe() {
		return r141_name_of_instrument_programe;
	}
	public void setR141_name_of_instrument_programe(String r141_name_of_instrument_programe) {
		this.r141_name_of_instrument_programe = r141_name_of_instrument_programe;
	}
	public String getR141_issuing_entity_if_issued_throughan_spv() {
		return r141_issuing_entity_if_issued_throughan_spv;
	}
	public void setR141_issuing_entity_if_issued_throughan_spv(String r141_issuing_entity_if_issued_throughan_spv) {
		this.r141_issuing_entity_if_issued_throughan_spv = r141_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR141_issuance_date() {
		return r141_issuance_date;
	}
	public void setR141_issuance_date(Date r141_issuance_date) {
		this.r141_issuance_date = r141_issuance_date;
	}
	public Date getR141_contractual_maturity_date() {
		return r141_contractual_maturity_date;
	}
	public void setR141_contractual_maturity_date(Date r141_contractual_maturity_date) {
		this.r141_contractual_maturity_date = r141_contractual_maturity_date;
	}
	public Date getR141_effective_maturity_date_if_date() {
		return r141_effective_maturity_date_if_date;
	}
	public void setR141_effective_maturity_date_if_date(Date r141_effective_maturity_date_if_date) {
		this.r141_effective_maturity_date_if_date = r141_effective_maturity_date_if_date;
	}
	public BigDecimal getR141_amount() {
		return r141_amount;
	}
	public void setR141_amount(BigDecimal r141_amount) {
		this.r141_amount = r141_amount;
	}
	public BigDecimal getR141_amount_derecognised_p000() {
		return r141_amount_derecognised_p000;
	}
	public void setR141_amount_derecognised_p000(BigDecimal r141_amount_derecognised_p000) {
		this.r141_amount_derecognised_p000 = r141_amount_derecognised_p000;
	}
	public BigDecimal getR141_total_p000() {
		return r141_total_p000;
	}
	public void setR141_total_p000(BigDecimal r141_total_p000) {
		this.r141_total_p000 = r141_total_p000;
	}
	public String getR142_line_no() {
		return r142_line_no;
	}
	public void setR142_line_no(String r142_line_no) {
		this.r142_line_no = r142_line_no;
	}
	public String getR142_note_holders() {
		return r142_note_holders;
	}
	public void setR142_note_holders(String r142_note_holders) {
		this.r142_note_holders = r142_note_holders;
	}
	public String getR142_name_of_instrument_programe() {
		return r142_name_of_instrument_programe;
	}
	public void setR142_name_of_instrument_programe(String r142_name_of_instrument_programe) {
		this.r142_name_of_instrument_programe = r142_name_of_instrument_programe;
	}
	public String getR142_issuing_entity_if_issued_throughan_spv() {
		return r142_issuing_entity_if_issued_throughan_spv;
	}
	public void setR142_issuing_entity_if_issued_throughan_spv(String r142_issuing_entity_if_issued_throughan_spv) {
		this.r142_issuing_entity_if_issued_throughan_spv = r142_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR142_issuance_date() {
		return r142_issuance_date;
	}
	public void setR142_issuance_date(Date r142_issuance_date) {
		this.r142_issuance_date = r142_issuance_date;
	}
	public Date getR142_contractual_maturity_date() {
		return r142_contractual_maturity_date;
	}
	public void setR142_contractual_maturity_date(Date r142_contractual_maturity_date) {
		this.r142_contractual_maturity_date = r142_contractual_maturity_date;
	}
	public Date getR142_effective_maturity_date_if_date() {
		return r142_effective_maturity_date_if_date;
	}
	public void setR142_effective_maturity_date_if_date(Date r142_effective_maturity_date_if_date) {
		this.r142_effective_maturity_date_if_date = r142_effective_maturity_date_if_date;
	}
	public BigDecimal getR142_amount() {
		return r142_amount;
	}
	public void setR142_amount(BigDecimal r142_amount) {
		this.r142_amount = r142_amount;
	}
	public BigDecimal getR142_amount_derecognised_p000() {
		return r142_amount_derecognised_p000;
	}
	public void setR142_amount_derecognised_p000(BigDecimal r142_amount_derecognised_p000) {
		this.r142_amount_derecognised_p000 = r142_amount_derecognised_p000;
	}
	public BigDecimal getR142_total_p000() {
		return r142_total_p000;
	}
	public void setR142_total_p000(BigDecimal r142_total_p000) {
		this.r142_total_p000 = r142_total_p000;
	}
	public String getR143_line_no() {
		return r143_line_no;
	}
	public void setR143_line_no(String r143_line_no) {
		this.r143_line_no = r143_line_no;
	}
	public String getR143_note_holders() {
		return r143_note_holders;
	}
	public void setR143_note_holders(String r143_note_holders) {
		this.r143_note_holders = r143_note_holders;
	}
	public String getR143_name_of_instrument_programe() {
		return r143_name_of_instrument_programe;
	}
	public void setR143_name_of_instrument_programe(String r143_name_of_instrument_programe) {
		this.r143_name_of_instrument_programe = r143_name_of_instrument_programe;
	}
	public String getR143_issuing_entity_if_issued_throughan_spv() {
		return r143_issuing_entity_if_issued_throughan_spv;
	}
	public void setR143_issuing_entity_if_issued_throughan_spv(String r143_issuing_entity_if_issued_throughan_spv) {
		this.r143_issuing_entity_if_issued_throughan_spv = r143_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR143_issuance_date() {
		return r143_issuance_date;
	}
	public void setR143_issuance_date(Date r143_issuance_date) {
		this.r143_issuance_date = r143_issuance_date;
	}
	public Date getR143_contractual_maturity_date() {
		return r143_contractual_maturity_date;
	}
	public void setR143_contractual_maturity_date(Date r143_contractual_maturity_date) {
		this.r143_contractual_maturity_date = r143_contractual_maturity_date;
	}
	public Date getR143_effective_maturity_date_if_date() {
		return r143_effective_maturity_date_if_date;
	}
	public void setR143_effective_maturity_date_if_date(Date r143_effective_maturity_date_if_date) {
		this.r143_effective_maturity_date_if_date = r143_effective_maturity_date_if_date;
	}
	public BigDecimal getR143_amount() {
		return r143_amount;
	}
	public void setR143_amount(BigDecimal r143_amount) {
		this.r143_amount = r143_amount;
	}
	public BigDecimal getR143_amount_derecognised_p000() {
		return r143_amount_derecognised_p000;
	}
	public void setR143_amount_derecognised_p000(BigDecimal r143_amount_derecognised_p000) {
		this.r143_amount_derecognised_p000 = r143_amount_derecognised_p000;
	}
	public BigDecimal getR143_total_p000() {
		return r143_total_p000;
	}
	public void setR143_total_p000(BigDecimal r143_total_p000) {
		this.r143_total_p000 = r143_total_p000;
	}
	public String getR144_line_no() {
		return r144_line_no;
	}
	public void setR144_line_no(String r144_line_no) {
		this.r144_line_no = r144_line_no;
	}
	public String getR144_note_holders() {
		return r144_note_holders;
	}
	public void setR144_note_holders(String r144_note_holders) {
		this.r144_note_holders = r144_note_holders;
	}
	public String getR144_name_of_instrument_programe() {
		return r144_name_of_instrument_programe;
	}
	public void setR144_name_of_instrument_programe(String r144_name_of_instrument_programe) {
		this.r144_name_of_instrument_programe = r144_name_of_instrument_programe;
	}
	public String getR144_issuing_entity_if_issued_throughan_spv() {
		return r144_issuing_entity_if_issued_throughan_spv;
	}
	public void setR144_issuing_entity_if_issued_throughan_spv(String r144_issuing_entity_if_issued_throughan_spv) {
		this.r144_issuing_entity_if_issued_throughan_spv = r144_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR144_issuance_date() {
		return r144_issuance_date;
	}
	public void setR144_issuance_date(Date r144_issuance_date) {
		this.r144_issuance_date = r144_issuance_date;
	}
	public Date getR144_contractual_maturity_date() {
		return r144_contractual_maturity_date;
	}
	public void setR144_contractual_maturity_date(Date r144_contractual_maturity_date) {
		this.r144_contractual_maturity_date = r144_contractual_maturity_date;
	}
	public Date getR144_effective_maturity_date_if_date() {
		return r144_effective_maturity_date_if_date;
	}
	public void setR144_effective_maturity_date_if_date(Date r144_effective_maturity_date_if_date) {
		this.r144_effective_maturity_date_if_date = r144_effective_maturity_date_if_date;
	}
	public BigDecimal getR144_amount() {
		return r144_amount;
	}
	public void setR144_amount(BigDecimal r144_amount) {
		this.r144_amount = r144_amount;
	}
	public BigDecimal getR144_amount_derecognised_p000() {
		return r144_amount_derecognised_p000;
	}
	public void setR144_amount_derecognised_p000(BigDecimal r144_amount_derecognised_p000) {
		this.r144_amount_derecognised_p000 = r144_amount_derecognised_p000;
	}
	public BigDecimal getR144_total_p000() {
		return r144_total_p000;
	}
	public void setR144_total_p000(BigDecimal r144_total_p000) {
		this.r144_total_p000 = r144_total_p000;
	}
	public String getR145_line_no() {
		return r145_line_no;
	}
	public void setR145_line_no(String r145_line_no) {
		this.r145_line_no = r145_line_no;
	}
	public String getR145_note_holders() {
		return r145_note_holders;
	}
	public void setR145_note_holders(String r145_note_holders) {
		this.r145_note_holders = r145_note_holders;
	}
	public String getR145_name_of_instrument_programe() {
		return r145_name_of_instrument_programe;
	}
	public void setR145_name_of_instrument_programe(String r145_name_of_instrument_programe) {
		this.r145_name_of_instrument_programe = r145_name_of_instrument_programe;
	}
	public String getR145_issuing_entity_if_issued_throughan_spv() {
		return r145_issuing_entity_if_issued_throughan_spv;
	}
	public void setR145_issuing_entity_if_issued_throughan_spv(String r145_issuing_entity_if_issued_throughan_spv) {
		this.r145_issuing_entity_if_issued_throughan_spv = r145_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR145_issuance_date() {
		return r145_issuance_date;
	}
	public void setR145_issuance_date(Date r145_issuance_date) {
		this.r145_issuance_date = r145_issuance_date;
	}
	public Date getR145_contractual_maturity_date() {
		return r145_contractual_maturity_date;
	}
	public void setR145_contractual_maturity_date(Date r145_contractual_maturity_date) {
		this.r145_contractual_maturity_date = r145_contractual_maturity_date;
	}
	public Date getR145_effective_maturity_date_if_date() {
		return r145_effective_maturity_date_if_date;
	}
	public void setR145_effective_maturity_date_if_date(Date r145_effective_maturity_date_if_date) {
		this.r145_effective_maturity_date_if_date = r145_effective_maturity_date_if_date;
	}
	public BigDecimal getR145_amount() {
		return r145_amount;
	}
	public void setR145_amount(BigDecimal r145_amount) {
		this.r145_amount = r145_amount;
	}
	public BigDecimal getR145_amount_derecognised_p000() {
		return r145_amount_derecognised_p000;
	}
	public void setR145_amount_derecognised_p000(BigDecimal r145_amount_derecognised_p000) {
		this.r145_amount_derecognised_p000 = r145_amount_derecognised_p000;
	}
	public BigDecimal getR145_total_p000() {
		return r145_total_p000;
	}
	public void setR145_total_p000(BigDecimal r145_total_p000) {
		this.r145_total_p000 = r145_total_p000;
	}
	public String getR146_line_no() {
		return r146_line_no;
	}
	public void setR146_line_no(String r146_line_no) {
		this.r146_line_no = r146_line_no;
	}
	public String getR146_note_holders() {
		return r146_note_holders;
	}
	public void setR146_note_holders(String r146_note_holders) {
		this.r146_note_holders = r146_note_holders;
	}
	public String getR146_name_of_instrument_programe() {
		return r146_name_of_instrument_programe;
	}
	public void setR146_name_of_instrument_programe(String r146_name_of_instrument_programe) {
		this.r146_name_of_instrument_programe = r146_name_of_instrument_programe;
	}
	public String getR146_issuing_entity_if_issued_throughan_spv() {
		return r146_issuing_entity_if_issued_throughan_spv;
	}
	public void setR146_issuing_entity_if_issued_throughan_spv(String r146_issuing_entity_if_issued_throughan_spv) {
		this.r146_issuing_entity_if_issued_throughan_spv = r146_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR146_issuance_date() {
		return r146_issuance_date;
	}
	public void setR146_issuance_date(Date r146_issuance_date) {
		this.r146_issuance_date = r146_issuance_date;
	}
	public Date getR146_contractual_maturity_date() {
		return r146_contractual_maturity_date;
	}
	public void setR146_contractual_maturity_date(Date r146_contractual_maturity_date) {
		this.r146_contractual_maturity_date = r146_contractual_maturity_date;
	}
	public Date getR146_effective_maturity_date_if_date() {
		return r146_effective_maturity_date_if_date;
	}
	public void setR146_effective_maturity_date_if_date(Date r146_effective_maturity_date_if_date) {
		this.r146_effective_maturity_date_if_date = r146_effective_maturity_date_if_date;
	}
	public BigDecimal getR146_amount() {
		return r146_amount;
	}
	public void setR146_amount(BigDecimal r146_amount) {
		this.r146_amount = r146_amount;
	}
	public BigDecimal getR146_amount_derecognised_p000() {
		return r146_amount_derecognised_p000;
	}
	public void setR146_amount_derecognised_p000(BigDecimal r146_amount_derecognised_p000) {
		this.r146_amount_derecognised_p000 = r146_amount_derecognised_p000;
	}
	public BigDecimal getR146_total_p000() {
		return r146_total_p000;
	}
	public void setR146_total_p000(BigDecimal r146_total_p000) {
		this.r146_total_p000 = r146_total_p000;
	}
	public String getR147_line_no() {
		return r147_line_no;
	}
	public void setR147_line_no(String r147_line_no) {
		this.r147_line_no = r147_line_no;
	}
	public String getR147_note_holders() {
		return r147_note_holders;
	}
	public void setR147_note_holders(String r147_note_holders) {
		this.r147_note_holders = r147_note_holders;
	}
	public String getR147_name_of_instrument_programe() {
		return r147_name_of_instrument_programe;
	}
	public void setR147_name_of_instrument_programe(String r147_name_of_instrument_programe) {
		this.r147_name_of_instrument_programe = r147_name_of_instrument_programe;
	}
	public String getR147_issuing_entity_if_issued_throughan_spv() {
		return r147_issuing_entity_if_issued_throughan_spv;
	}
	public void setR147_issuing_entity_if_issued_throughan_spv(String r147_issuing_entity_if_issued_throughan_spv) {
		this.r147_issuing_entity_if_issued_throughan_spv = r147_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR147_issuance_date() {
		return r147_issuance_date;
	}
	public void setR147_issuance_date(Date r147_issuance_date) {
		this.r147_issuance_date = r147_issuance_date;
	}
	public Date getR147_contractual_maturity_date() {
		return r147_contractual_maturity_date;
	}
	public void setR147_contractual_maturity_date(Date r147_contractual_maturity_date) {
		this.r147_contractual_maturity_date = r147_contractual_maturity_date;
	}
	public Date getR147_effective_maturity_date_if_date() {
		return r147_effective_maturity_date_if_date;
	}
	public void setR147_effective_maturity_date_if_date(Date r147_effective_maturity_date_if_date) {
		this.r147_effective_maturity_date_if_date = r147_effective_maturity_date_if_date;
	}
	public BigDecimal getR147_amount() {
		return r147_amount;
	}
	public void setR147_amount(BigDecimal r147_amount) {
		this.r147_amount = r147_amount;
	}
	public BigDecimal getR147_amount_derecognised_p000() {
		return r147_amount_derecognised_p000;
	}
	public void setR147_amount_derecognised_p000(BigDecimal r147_amount_derecognised_p000) {
		this.r147_amount_derecognised_p000 = r147_amount_derecognised_p000;
	}
	public BigDecimal getR147_total_p000() {
		return r147_total_p000;
	}
	public void setR147_total_p000(BigDecimal r147_total_p000) {
		this.r147_total_p000 = r147_total_p000;
	}
	public String getR148_line_no() {
		return r148_line_no;
	}
	public void setR148_line_no(String r148_line_no) {
		this.r148_line_no = r148_line_no;
	}
	public String getR148_note_holders() {
		return r148_note_holders;
	}
	public void setR148_note_holders(String r148_note_holders) {
		this.r148_note_holders = r148_note_holders;
	}
	public String getR148_name_of_instrument_programe() {
		return r148_name_of_instrument_programe;
	}
	public void setR148_name_of_instrument_programe(String r148_name_of_instrument_programe) {
		this.r148_name_of_instrument_programe = r148_name_of_instrument_programe;
	}
	public String getR148_issuing_entity_if_issued_throughan_spv() {
		return r148_issuing_entity_if_issued_throughan_spv;
	}
	public void setR148_issuing_entity_if_issued_throughan_spv(String r148_issuing_entity_if_issued_throughan_spv) {
		this.r148_issuing_entity_if_issued_throughan_spv = r148_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR148_issuance_date() {
		return r148_issuance_date;
	}
	public void setR148_issuance_date(Date r148_issuance_date) {
		this.r148_issuance_date = r148_issuance_date;
	}
	public Date getR148_contractual_maturity_date() {
		return r148_contractual_maturity_date;
	}
	public void setR148_contractual_maturity_date(Date r148_contractual_maturity_date) {
		this.r148_contractual_maturity_date = r148_contractual_maturity_date;
	}
	public Date getR148_effective_maturity_date_if_date() {
		return r148_effective_maturity_date_if_date;
	}
	public void setR148_effective_maturity_date_if_date(Date r148_effective_maturity_date_if_date) {
		this.r148_effective_maturity_date_if_date = r148_effective_maturity_date_if_date;
	}
	public BigDecimal getR148_amount() {
		return r148_amount;
	}
	public void setR148_amount(BigDecimal r148_amount) {
		this.r148_amount = r148_amount;
	}
	public BigDecimal getR148_amount_derecognised_p000() {
		return r148_amount_derecognised_p000;
	}
	public void setR148_amount_derecognised_p000(BigDecimal r148_amount_derecognised_p000) {
		this.r148_amount_derecognised_p000 = r148_amount_derecognised_p000;
	}
	public BigDecimal getR148_total_p000() {
		return r148_total_p000;
	}
	public void setR148_total_p000(BigDecimal r148_total_p000) {
		this.r148_total_p000 = r148_total_p000;
	}
	public String getR149_line_no() {
		return r149_line_no;
	}
	public void setR149_line_no(String r149_line_no) {
		this.r149_line_no = r149_line_no;
	}
	public String getR149_note_holders() {
		return r149_note_holders;
	}
	public void setR149_note_holders(String r149_note_holders) {
		this.r149_note_holders = r149_note_holders;
	}
	public String getR149_name_of_instrument_programe() {
		return r149_name_of_instrument_programe;
	}
	public void setR149_name_of_instrument_programe(String r149_name_of_instrument_programe) {
		this.r149_name_of_instrument_programe = r149_name_of_instrument_programe;
	}
	public String getR149_issuing_entity_if_issued_throughan_spv() {
		return r149_issuing_entity_if_issued_throughan_spv;
	}
	public void setR149_issuing_entity_if_issued_throughan_spv(String r149_issuing_entity_if_issued_throughan_spv) {
		this.r149_issuing_entity_if_issued_throughan_spv = r149_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR149_issuance_date() {
		return r149_issuance_date;
	}
	public void setR149_issuance_date(Date r149_issuance_date) {
		this.r149_issuance_date = r149_issuance_date;
	}
	public Date getR149_contractual_maturity_date() {
		return r149_contractual_maturity_date;
	}
	public void setR149_contractual_maturity_date(Date r149_contractual_maturity_date) {
		this.r149_contractual_maturity_date = r149_contractual_maturity_date;
	}
	public Date getR149_effective_maturity_date_if_date() {
		return r149_effective_maturity_date_if_date;
	}
	public void setR149_effective_maturity_date_if_date(Date r149_effective_maturity_date_if_date) {
		this.r149_effective_maturity_date_if_date = r149_effective_maturity_date_if_date;
	}
	public BigDecimal getR149_amount() {
		return r149_amount;
	}
	public void setR149_amount(BigDecimal r149_amount) {
		this.r149_amount = r149_amount;
	}
	public BigDecimal getR149_amount_derecognised_p000() {
		return r149_amount_derecognised_p000;
	}
	public void setR149_amount_derecognised_p000(BigDecimal r149_amount_derecognised_p000) {
		this.r149_amount_derecognised_p000 = r149_amount_derecognised_p000;
	}
	public BigDecimal getR149_total_p000() {
		return r149_total_p000;
	}
	public void setR149_total_p000(BigDecimal r149_total_p000) {
		this.r149_total_p000 = r149_total_p000;
	}
	public String getR150_line_no() {
		return r150_line_no;
	}
	public void setR150_line_no(String r150_line_no) {
		this.r150_line_no = r150_line_no;
	}
	public String getR150_note_holders() {
		return r150_note_holders;
	}
	public void setR150_note_holders(String r150_note_holders) {
		this.r150_note_holders = r150_note_holders;
	}
	public String getR150_name_of_instrument_programe() {
		return r150_name_of_instrument_programe;
	}
	public void setR150_name_of_instrument_programe(String r150_name_of_instrument_programe) {
		this.r150_name_of_instrument_programe = r150_name_of_instrument_programe;
	}
	public String getR150_issuing_entity_if_issued_throughan_spv() {
		return r150_issuing_entity_if_issued_throughan_spv;
	}
	public void setR150_issuing_entity_if_issued_throughan_spv(String r150_issuing_entity_if_issued_throughan_spv) {
		this.r150_issuing_entity_if_issued_throughan_spv = r150_issuing_entity_if_issued_throughan_spv;
	}
	public Date getR150_issuance_date() {
		return r150_issuance_date;
	}
	public void setR150_issuance_date(Date r150_issuance_date) {
		this.r150_issuance_date = r150_issuance_date;
	}
	public Date getR150_contractual_maturity_date() {
		return r150_contractual_maturity_date;
	}
	public void setR150_contractual_maturity_date(Date r150_contractual_maturity_date) {
		this.r150_contractual_maturity_date = r150_contractual_maturity_date;
	}
	public Date getR150_effective_maturity_date_if_date() {
		return r150_effective_maturity_date_if_date;
	}
	public void setR150_effective_maturity_date_if_date(Date r150_effective_maturity_date_if_date) {
		this.r150_effective_maturity_date_if_date = r150_effective_maturity_date_if_date;
	}
	public BigDecimal getR150_amount() {
		return r150_amount;
	}
	public void setR150_amount(BigDecimal r150_amount) {
		this.r150_amount = r150_amount;
	}
	public BigDecimal getR150_amount_derecognised_p000() {
		return r150_amount_derecognised_p000;
	}
	public void setR150_amount_derecognised_p000(BigDecimal r150_amount_derecognised_p000) {
		this.r150_amount_derecognised_p000 = r150_amount_derecognised_p000;
	}
	public BigDecimal getR150_total_p000() {
		return r150_total_p000;
	}
	public void setR150_total_p000(BigDecimal r150_total_p000) {
		this.r150_total_p000 = r150_total_p000;
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
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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
	public M_CA5_Archival_Detail_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}
	


}
