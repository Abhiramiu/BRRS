package com.bornfire.brrs.entities;

import java.io.Serializable;
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
@Table(name = "BRRS_M_LARADV_ARCHIVALTABLE_SUMMARY3")
@IdClass(M_LARADV_PK.class)
public class M_LARADV_Archival_Summary_Entity3 implements Serializable{
	private static final long serialVersionUID = 1L;
	// Fields for R141
    private String R141_NO_OF_GROUP;
    private String R141_NO_OF_CUSTOMER;
    private String R141_SECTOR_TYPE;
    private String R141_FACILITY_TYPE;
    private BigDecimal R141_ORIGINAL_AMOUNT;
    private BigDecimal R141_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R141_EFFECTIVE_DATE;
    private String R141_REPAYMENT_PERIOD;
    private String R141_PERFORMANCE_STATUS;
    private String R141_SECURITY;
    private String R141_BOARD_APPROVAL;
    private BigDecimal R141_INTEREST_RATE;
    private BigDecimal R141_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R141_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R142
    private String R142_NO_OF_GROUP;
    private String R142_NO_OF_CUSTOMER;
    private String R142_SECTOR_TYPE;
    private String R142_FACILITY_TYPE;
    private BigDecimal R142_ORIGINAL_AMOUNT;
    private BigDecimal R142_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R142_EFFECTIVE_DATE;
    private String R142_REPAYMENT_PERIOD;
    private String R142_PERFORMANCE_STATUS;
    private String R142_SECURITY;
    private String R142_BOARD_APPROVAL;
    private BigDecimal R142_INTEREST_RATE;
    private BigDecimal R142_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R142_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R143
    private String R143_NO_OF_GROUP;
    private String R143_NO_OF_CUSTOMER;
    private String R143_SECTOR_TYPE;
    private String R143_FACILITY_TYPE;
    private BigDecimal R143_ORIGINAL_AMOUNT;
    private BigDecimal R143_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R143_EFFECTIVE_DATE;
    private String R143_REPAYMENT_PERIOD;
    private String R143_PERFORMANCE_STATUS;
    private String R143_SECURITY;
    private String R143_BOARD_APPROVAL;
    private BigDecimal R143_INTEREST_RATE;
    private BigDecimal R143_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R143_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R144
    private String R144_NO_OF_GROUP;
    private String R144_NO_OF_CUSTOMER;
    private String R144_SECTOR_TYPE;
    private String R144_FACILITY_TYPE;
    private BigDecimal R144_ORIGINAL_AMOUNT;
    private BigDecimal R144_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R144_EFFECTIVE_DATE;
    private String R144_REPAYMENT_PERIOD;
    private String R144_PERFORMANCE_STATUS;
    private String R144_SECURITY;
    private String R144_BOARD_APPROVAL;
    private BigDecimal R144_INTEREST_RATE;
    private BigDecimal R144_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R144_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R145
    private String R145_NO_OF_GROUP;
    private String R145_NO_OF_CUSTOMER;
    private String R145_SECTOR_TYPE;
    private String R145_FACILITY_TYPE;
    private BigDecimal R145_ORIGINAL_AMOUNT;
    private BigDecimal R145_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R145_EFFECTIVE_DATE;
    private String R145_REPAYMENT_PERIOD;
    private String R145_PERFORMANCE_STATUS;
    private String R145_SECURITY;
    private String R145_BOARD_APPROVAL;
    private BigDecimal R145_INTEREST_RATE;
    private BigDecimal R145_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R145_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R146
    private String R146_NO_OF_GROUP;
    private String R146_NO_OF_CUSTOMER;
    private String R146_SECTOR_TYPE;
    private String R146_FACILITY_TYPE;
    private BigDecimal R146_ORIGINAL_AMOUNT;
    private BigDecimal R146_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R146_EFFECTIVE_DATE;
    private String R146_REPAYMENT_PERIOD;
    private String R146_PERFORMANCE_STATUS;
    private String R146_SECURITY;
    private String R146_BOARD_APPROVAL;
    private BigDecimal R146_INTEREST_RATE;
    private BigDecimal R146_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R146_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R147
    private String R147_NO_OF_GROUP;
    private String R147_NO_OF_CUSTOMER;
    private String R147_SECTOR_TYPE;
    private String R147_FACILITY_TYPE;
    private BigDecimal R147_ORIGINAL_AMOUNT;
    private BigDecimal R147_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R147_EFFECTIVE_DATE;
    private String R147_REPAYMENT_PERIOD;
    private String R147_PERFORMANCE_STATUS;
    private String R147_SECURITY;
    private String R147_BOARD_APPROVAL;
    private BigDecimal R147_INTEREST_RATE;
    private BigDecimal R147_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R147_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R148
    private String R148_NO_OF_GROUP;
    private String R148_NO_OF_CUSTOMER;
    private String R148_SECTOR_TYPE;
    private String R148_FACILITY_TYPE;
    private BigDecimal R148_ORIGINAL_AMOUNT;
    private BigDecimal R148_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R148_EFFECTIVE_DATE;
    private String R148_REPAYMENT_PERIOD;
    private String R148_PERFORMANCE_STATUS;
    private String R148_SECURITY;
    private String R148_BOARD_APPROVAL;
    private BigDecimal R148_INTEREST_RATE;
    private BigDecimal R148_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R148_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R149
    private String R149_NO_OF_GROUP;
    private String R149_NO_OF_CUSTOMER;
    private String R149_SECTOR_TYPE;
    private String R149_FACILITY_TYPE;
    private BigDecimal R149_ORIGINAL_AMOUNT;
    private BigDecimal R149_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R149_EFFECTIVE_DATE;
    private String R149_REPAYMENT_PERIOD;
    private String R149_PERFORMANCE_STATUS;
    private String R149_SECURITY;
    private String R149_BOARD_APPROVAL;
    private BigDecimal R149_INTEREST_RATE;
    private BigDecimal R149_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R149_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R150
    private String R150_NO_OF_GROUP;
    private String R150_NO_OF_CUSTOMER;
    private String R150_SECTOR_TYPE;
    private String R150_FACILITY_TYPE;
    private BigDecimal R150_ORIGINAL_AMOUNT;
    private BigDecimal R150_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R150_EFFECTIVE_DATE;
    private String R150_REPAYMENT_PERIOD;
    private String R150_PERFORMANCE_STATUS;
    private String R150_SECURITY;
    private String R150_BOARD_APPROVAL;
    private BigDecimal R150_INTEREST_RATE;
    private BigDecimal R150_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R150_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R151
    private String R151_NO_OF_GROUP;
    private String R151_NO_OF_CUSTOMER;
    private String R151_SECTOR_TYPE;
    private String R151_FACILITY_TYPE;
    private BigDecimal R151_ORIGINAL_AMOUNT;
    private BigDecimal R151_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R151_EFFECTIVE_DATE;
    private String R151_REPAYMENT_PERIOD;
    private String R151_PERFORMANCE_STATUS;
    private String R151_SECURITY;
    private String R151_BOARD_APPROVAL;
    private BigDecimal R151_INTEREST_RATE;
    private BigDecimal R151_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R151_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R152
    private String R152_NO_OF_GROUP;
    private String R152_NO_OF_CUSTOMER;
    private String R152_SECTOR_TYPE;
    private String R152_FACILITY_TYPE;
    private BigDecimal R152_ORIGINAL_AMOUNT;
    private BigDecimal R152_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R152_EFFECTIVE_DATE;
    private String R152_REPAYMENT_PERIOD;
    private String R152_PERFORMANCE_STATUS;
    private String R152_SECURITY;
    private String R152_BOARD_APPROVAL;
    private BigDecimal R152_INTEREST_RATE;
    private BigDecimal R152_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R152_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R153
    private String R153_NO_OF_GROUP;
    private String R153_NO_OF_CUSTOMER;
    private String R153_SECTOR_TYPE;
    private String R153_FACILITY_TYPE;
    private BigDecimal R153_ORIGINAL_AMOUNT;
    private BigDecimal R153_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R153_EFFECTIVE_DATE;
    private String R153_REPAYMENT_PERIOD;
    private String R153_PERFORMANCE_STATUS;
    private String R153_SECURITY;
    private String R153_BOARD_APPROVAL;
    private BigDecimal R153_INTEREST_RATE;
    private BigDecimal R153_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R153_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R154
    private String R154_NO_OF_GROUP;
    private String R154_NO_OF_CUSTOMER;
    private String R154_SECTOR_TYPE;
    private String R154_FACILITY_TYPE;
    private BigDecimal R154_ORIGINAL_AMOUNT;
    private BigDecimal R154_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R154_EFFECTIVE_DATE;
    private String R154_REPAYMENT_PERIOD;
    private String R154_PERFORMANCE_STATUS;
    private String R154_SECURITY;
    private String R154_BOARD_APPROVAL;
    private BigDecimal R154_INTEREST_RATE;
    private BigDecimal R154_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R154_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R155
    private String R155_NO_OF_GROUP;
    private String R155_NO_OF_CUSTOMER;
    private String R155_SECTOR_TYPE;
    private String R155_FACILITY_TYPE;
    private BigDecimal R155_ORIGINAL_AMOUNT;
    private BigDecimal R155_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R155_EFFECTIVE_DATE;
    private String R155_REPAYMENT_PERIOD;
    private String R155_PERFORMANCE_STATUS;
    private String R155_SECURITY;
    private String R155_BOARD_APPROVAL;
    private BigDecimal R155_INTEREST_RATE;
    private BigDecimal R155_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R155_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R156
    private String R156_NO_OF_GROUP;
    private String R156_NO_OF_CUSTOMER;
    private String R156_SECTOR_TYPE;
    private String R156_FACILITY_TYPE;
    private BigDecimal R156_ORIGINAL_AMOUNT;
    private BigDecimal R156_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R156_EFFECTIVE_DATE;
    private String R156_REPAYMENT_PERIOD;
    private String R156_PERFORMANCE_STATUS;
    private String R156_SECURITY;
    private String R156_BOARD_APPROVAL;
    private BigDecimal R156_INTEREST_RATE;
    private BigDecimal R156_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R156_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R157
    private String R157_NO_OF_GROUP;
    private String R157_NO_OF_CUSTOMER;
    private String R157_SECTOR_TYPE;
    private String R157_FACILITY_TYPE;
    private BigDecimal R157_ORIGINAL_AMOUNT;
    private BigDecimal R157_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R157_EFFECTIVE_DATE;
    private String R157_REPAYMENT_PERIOD;
    private String R157_PERFORMANCE_STATUS;
    private String R157_SECURITY;
    private String R157_BOARD_APPROVAL;
    private BigDecimal R157_INTEREST_RATE;
    private BigDecimal R157_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R157_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R158
    private String R158_NO_OF_GROUP;
    private String R158_NO_OF_CUSTOMER;
    private String R158_SECTOR_TYPE;
    private String R158_FACILITY_TYPE;
    private BigDecimal R158_ORIGINAL_AMOUNT;
    private BigDecimal R158_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R158_EFFECTIVE_DATE;
    private String R158_REPAYMENT_PERIOD;
    private String R158_PERFORMANCE_STATUS;
    private String R158_SECURITY;
    private String R158_BOARD_APPROVAL;
    private BigDecimal R158_INTEREST_RATE;
    private BigDecimal R158_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R158_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R159
    private String R159_NO_OF_GROUP;
    private String R159_NO_OF_CUSTOMER;
    private String R159_SECTOR_TYPE;
    private String R159_FACILITY_TYPE;
    private BigDecimal R159_ORIGINAL_AMOUNT;
    private BigDecimal R159_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R159_EFFECTIVE_DATE;
    private String R159_REPAYMENT_PERIOD;
    private String R159_PERFORMANCE_STATUS;
    private String R159_SECURITY;
    private String R159_BOARD_APPROVAL;
    private BigDecimal R159_INTEREST_RATE;
    private BigDecimal R159_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R159_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R160
    private String R160_NO_OF_GROUP;
    private String R160_NO_OF_CUSTOMER;
    private String R160_SECTOR_TYPE;
    private String R160_FACILITY_TYPE;
    private BigDecimal R160_ORIGINAL_AMOUNT;
    private BigDecimal R160_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R160_EFFECTIVE_DATE;
    private String R160_REPAYMENT_PERIOD;
    private String R160_PERFORMANCE_STATUS;
    private String R160_SECURITY;
    private String R160_BOARD_APPROVAL;
    private BigDecimal R160_INTEREST_RATE;
    private BigDecimal R160_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R160_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R161
    private String R161_NO_OF_GROUP;
    private String R161_NO_OF_CUSTOMER;
    private String R161_SECTOR_TYPE;
    private String R161_FACILITY_TYPE;
    private BigDecimal R161_ORIGINAL_AMOUNT;
    private BigDecimal R161_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R161_EFFECTIVE_DATE;
    private String R161_REPAYMENT_PERIOD;
    private String R161_PERFORMANCE_STATUS;
    private String R161_SECURITY;
    private String R161_BOARD_APPROVAL;
    private BigDecimal R161_INTEREST_RATE;
    private BigDecimal R161_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R161_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R162
    private String R162_NO_OF_GROUP;
    private String R162_NO_OF_CUSTOMER;
    private String R162_SECTOR_TYPE;
    private String R162_FACILITY_TYPE;
    private BigDecimal R162_ORIGINAL_AMOUNT;
    private BigDecimal R162_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R162_EFFECTIVE_DATE;
    private String R162_REPAYMENT_PERIOD;
    private String R162_PERFORMANCE_STATUS;
    private String R162_SECURITY;
    private String R162_BOARD_APPROVAL;
    private BigDecimal R162_INTEREST_RATE;
    private BigDecimal R162_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R162_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R163
    private String R163_NO_OF_GROUP;
    private String R163_NO_OF_CUSTOMER;
    private String R163_SECTOR_TYPE;
    private String R163_FACILITY_TYPE;
    private BigDecimal R163_ORIGINAL_AMOUNT;
    private BigDecimal R163_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R163_EFFECTIVE_DATE;
    private String R163_REPAYMENT_PERIOD;
    private String R163_PERFORMANCE_STATUS;
    private String R163_SECURITY;
    private String R163_BOARD_APPROVAL;
    private BigDecimal R163_INTEREST_RATE;
    private BigDecimal R163_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R163_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R164
    private String R164_NO_OF_GROUP;
    private String R164_NO_OF_CUSTOMER;
    private String R164_SECTOR_TYPE;
    private String R164_FACILITY_TYPE;
    private BigDecimal R164_ORIGINAL_AMOUNT;
    private BigDecimal R164_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R164_EFFECTIVE_DATE;
    private String R164_REPAYMENT_PERIOD;
    private String R164_PERFORMANCE_STATUS;
    private String R164_SECURITY;
    private String R164_BOARD_APPROVAL;
    private BigDecimal R164_INTEREST_RATE;
    private BigDecimal R164_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R164_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R165
    private String R165_NO_OF_GROUP;
    private String R165_NO_OF_CUSTOMER;
    private String R165_SECTOR_TYPE;
    private String R165_FACILITY_TYPE;
    private BigDecimal R165_ORIGINAL_AMOUNT;
    private BigDecimal R165_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R165_EFFECTIVE_DATE;
    private String R165_REPAYMENT_PERIOD;
    private String R165_PERFORMANCE_STATUS;
    private String R165_SECURITY;
    private String R165_BOARD_APPROVAL;
    private BigDecimal R165_INTEREST_RATE;
    private BigDecimal R165_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R165_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R166
    private String R166_NO_OF_GROUP;
    private String R166_NO_OF_CUSTOMER;
    private String R166_SECTOR_TYPE;
    private String R166_FACILITY_TYPE;
    private BigDecimal R166_ORIGINAL_AMOUNT;
    private BigDecimal R166_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R166_EFFECTIVE_DATE;
    private String R166_REPAYMENT_PERIOD;
    private String R166_PERFORMANCE_STATUS;
    private String R166_SECURITY;
    private String R166_BOARD_APPROVAL;
    private BigDecimal R166_INTEREST_RATE;
    private BigDecimal R166_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R166_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R167
    private String R167_NO_OF_GROUP;
    private String R167_NO_OF_CUSTOMER;
    private String R167_SECTOR_TYPE;
    private String R167_FACILITY_TYPE;
    private BigDecimal R167_ORIGINAL_AMOUNT;
    private BigDecimal R167_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R167_EFFECTIVE_DATE;
    private String R167_REPAYMENT_PERIOD;
    private String R167_PERFORMANCE_STATUS;
    private String R167_SECURITY;
    private String R167_BOARD_APPROVAL;
    private BigDecimal R167_INTEREST_RATE;
    private BigDecimal R167_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R167_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R168
    private String R168_NO_OF_GROUP;
    private String R168_NO_OF_CUSTOMER;
    private String R168_SECTOR_TYPE;
    private String R168_FACILITY_TYPE;
    private BigDecimal R168_ORIGINAL_AMOUNT;
    private BigDecimal R168_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R168_EFFECTIVE_DATE;
    private String R168_REPAYMENT_PERIOD;
    private String R168_PERFORMANCE_STATUS;
    private String R168_SECURITY;
    private String R168_BOARD_APPROVAL;
    private BigDecimal R168_INTEREST_RATE;
    private BigDecimal R168_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R168_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R169
    private String R169_NO_OF_GROUP;
    private String R169_NO_OF_CUSTOMER;
    private String R169_SECTOR_TYPE;
    private String R169_FACILITY_TYPE;
    private BigDecimal R169_ORIGINAL_AMOUNT;
    private BigDecimal R169_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R169_EFFECTIVE_DATE;
    private String R169_REPAYMENT_PERIOD;
    private String R169_PERFORMANCE_STATUS;
    private String R169_SECURITY;
    private String R169_BOARD_APPROVAL;
    private BigDecimal R169_INTEREST_RATE;
    private BigDecimal R169_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R169_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R170
    private String R170_NO_OF_GROUP;
    private String R170_NO_OF_CUSTOMER;
    private String R170_SECTOR_TYPE;
    private String R170_FACILITY_TYPE;
    private BigDecimal R170_ORIGINAL_AMOUNT;
    private BigDecimal R170_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R170_EFFECTIVE_DATE;
    private String R170_REPAYMENT_PERIOD;
    private String R170_PERFORMANCE_STATUS;
    private String R170_SECURITY;
    private String R170_BOARD_APPROVAL;
    private BigDecimal R170_INTEREST_RATE;
    private BigDecimal R170_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R170_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R171
    private String R171_NO_OF_GROUP;
    private String R171_NO_OF_CUSTOMER;
    private String R171_SECTOR_TYPE;
    private String R171_FACILITY_TYPE;
    private BigDecimal R171_ORIGINAL_AMOUNT;
    private BigDecimal R171_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R171_EFFECTIVE_DATE;
    private String R171_REPAYMENT_PERIOD;
    private String R171_PERFORMANCE_STATUS;
    private String R171_SECURITY;
    private String R171_BOARD_APPROVAL;
    private BigDecimal R171_INTEREST_RATE;
    private BigDecimal R171_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R171_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R172
    private String R172_NO_OF_GROUP;
    private String R172_NO_OF_CUSTOMER;
    private String R172_SECTOR_TYPE;
    private String R172_FACILITY_TYPE;
    private BigDecimal R172_ORIGINAL_AMOUNT;
    private BigDecimal R172_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R172_EFFECTIVE_DATE;
    private String R172_REPAYMENT_PERIOD;
    private String R172_PERFORMANCE_STATUS;
    private String R172_SECURITY;
    private String R172_BOARD_APPROVAL;
    private BigDecimal R172_INTEREST_RATE;
    private BigDecimal R172_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R172_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R173
    private String R173_NO_OF_GROUP;
    private String R173_NO_OF_CUSTOMER;
    private String R173_SECTOR_TYPE;
    private String R173_FACILITY_TYPE;
    private BigDecimal R173_ORIGINAL_AMOUNT;
    private BigDecimal R173_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R173_EFFECTIVE_DATE;
    private String R173_REPAYMENT_PERIOD;
    private String R173_PERFORMANCE_STATUS;
    private String R173_SECURITY;
    private String R173_BOARD_APPROVAL;
    private BigDecimal R173_INTEREST_RATE;
    private BigDecimal R173_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R173_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R174
    private String R174_NO_OF_GROUP;
    private String R174_NO_OF_CUSTOMER;
    private String R174_SECTOR_TYPE;
    private String R174_FACILITY_TYPE;
    private BigDecimal R174_ORIGINAL_AMOUNT;
    private BigDecimal R174_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R174_EFFECTIVE_DATE;
    private String R174_REPAYMENT_PERIOD;
    private String R174_PERFORMANCE_STATUS;
    private String R174_SECURITY;
    private String R174_BOARD_APPROVAL;
    private BigDecimal R174_INTEREST_RATE;
    private BigDecimal R174_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R174_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R175
    private String R175_NO_OF_GROUP;
    private String R175_NO_OF_CUSTOMER;
    private String R175_SECTOR_TYPE;
    private String R175_FACILITY_TYPE;
    private BigDecimal R175_ORIGINAL_AMOUNT;
    private BigDecimal R175_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R175_EFFECTIVE_DATE;
    private String R175_REPAYMENT_PERIOD;
    private String R175_PERFORMANCE_STATUS;
    private String R175_SECURITY;
    private String R175_BOARD_APPROVAL;
    private BigDecimal R175_INTEREST_RATE;
    private BigDecimal R175_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R175_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R176
    private String R176_NO_OF_GROUP;
    private String R176_NO_OF_CUSTOMER;
    private String R176_SECTOR_TYPE;
    private String R176_FACILITY_TYPE;
    private BigDecimal R176_ORIGINAL_AMOUNT;
    private BigDecimal R176_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R176_EFFECTIVE_DATE;
    private String R176_REPAYMENT_PERIOD;
    private String R176_PERFORMANCE_STATUS;
    private String R176_SECURITY;
    private String R176_BOARD_APPROVAL;
    private BigDecimal R176_INTEREST_RATE;
    private BigDecimal R176_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R176_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R177
    private String R177_NO_OF_GROUP;
    private String R177_NO_OF_CUSTOMER;
    private String R177_SECTOR_TYPE;
    private String R177_FACILITY_TYPE;
    private BigDecimal R177_ORIGINAL_AMOUNT;
    private BigDecimal R177_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R177_EFFECTIVE_DATE;
    private String R177_REPAYMENT_PERIOD;
    private String R177_PERFORMANCE_STATUS;
    private String R177_SECURITY;
    private String R177_BOARD_APPROVAL;
    private BigDecimal R177_INTEREST_RATE;
    private BigDecimal R177_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R177_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R178
    private String R178_NO_OF_GROUP;
    private String R178_NO_OF_CUSTOMER;
    private String R178_SECTOR_TYPE;
    private String R178_FACILITY_TYPE;
    private BigDecimal R178_ORIGINAL_AMOUNT;
    private BigDecimal R178_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R178_EFFECTIVE_DATE;
    private String R178_REPAYMENT_PERIOD;
    private String R178_PERFORMANCE_STATUS;
    private String R178_SECURITY;
    private String R178_BOARD_APPROVAL;
    private BigDecimal R178_INTEREST_RATE;
    private BigDecimal R178_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R178_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R179
    private String R179_NO_OF_GROUP;
    private String R179_NO_OF_CUSTOMER;
    private String R179_SECTOR_TYPE;
    private String R179_FACILITY_TYPE;
    private BigDecimal R179_ORIGINAL_AMOUNT;
    private BigDecimal R179_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R179_EFFECTIVE_DATE;
    private String R179_REPAYMENT_PERIOD;
    private String R179_PERFORMANCE_STATUS;
    private String R179_SECURITY;
    private String R179_BOARD_APPROVAL;
    private BigDecimal R179_INTEREST_RATE;
    private BigDecimal R179_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R179_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R180
    private String R180_NO_OF_GROUP;
    private String R180_NO_OF_CUSTOMER;
    private String R180_SECTOR_TYPE;
    private String R180_FACILITY_TYPE;
    private BigDecimal R180_ORIGINAL_AMOUNT;
    private BigDecimal R180_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R180_EFFECTIVE_DATE;
    private String R180_REPAYMENT_PERIOD;
    private String R180_PERFORMANCE_STATUS;
    private String R180_SECURITY;
    private String R180_BOARD_APPROVAL;
    private BigDecimal R180_INTEREST_RATE;
    private BigDecimal R180_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R180_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R181
    private String R181_NO_OF_GROUP;
    private String R181_NO_OF_CUSTOMER;
    private String R181_SECTOR_TYPE;
    private String R181_FACILITY_TYPE;
    private BigDecimal R181_ORIGINAL_AMOUNT;
    private BigDecimal R181_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R181_EFFECTIVE_DATE;
    private String R181_REPAYMENT_PERIOD;
    private String R181_PERFORMANCE_STATUS;
    private String R181_SECURITY;
    private String R181_BOARD_APPROVAL;
    private BigDecimal R181_INTEREST_RATE;
    private BigDecimal R181_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R181_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R182
    private String R182_NO_OF_GROUP;
    private String R182_NO_OF_CUSTOMER;
    private String R182_SECTOR_TYPE;
    private String R182_FACILITY_TYPE;
    private BigDecimal R182_ORIGINAL_AMOUNT;
    private BigDecimal R182_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R182_EFFECTIVE_DATE;
    private String R182_REPAYMENT_PERIOD;
    private String R182_PERFORMANCE_STATUS;
    private String R182_SECURITY;
    private String R182_BOARD_APPROVAL;
    private BigDecimal R182_INTEREST_RATE;
    private BigDecimal R182_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R182_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R183
    private String R183_NO_OF_GROUP;
    private String R183_NO_OF_CUSTOMER;
    private String R183_SECTOR_TYPE;
    private String R183_FACILITY_TYPE;
    private BigDecimal R183_ORIGINAL_AMOUNT;
    private BigDecimal R183_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R183_EFFECTIVE_DATE;
    private String R183_REPAYMENT_PERIOD;
    private String R183_PERFORMANCE_STATUS;
    private String R183_SECURITY;
    private String R183_BOARD_APPROVAL;
    private BigDecimal R183_INTEREST_RATE;
    private BigDecimal R183_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R183_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R184
    private String R184_NO_OF_GROUP;
    private String R184_NO_OF_CUSTOMER;
    private String R184_SECTOR_TYPE;
    private String R184_FACILITY_TYPE;
    private BigDecimal R184_ORIGINAL_AMOUNT;
    private BigDecimal R184_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R184_EFFECTIVE_DATE;
    private String R184_REPAYMENT_PERIOD;
    private String R184_PERFORMANCE_STATUS;
    private String R184_SECURITY;
    private String R184_BOARD_APPROVAL;
    private BigDecimal R184_INTEREST_RATE;
    private BigDecimal R184_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R184_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R185
    private String R185_NO_OF_GROUP;
    private String R185_NO_OF_CUSTOMER;
    private String R185_SECTOR_TYPE;
    private String R185_FACILITY_TYPE;
    private BigDecimal R185_ORIGINAL_AMOUNT;
    private BigDecimal R185_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R185_EFFECTIVE_DATE;
    private String R185_REPAYMENT_PERIOD;
    private String R185_PERFORMANCE_STATUS;
    private String R185_SECURITY;
    private String R185_BOARD_APPROVAL;
    private BigDecimal R185_INTEREST_RATE;
    private BigDecimal R185_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R185_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R186
    private String R186_NO_OF_GROUP;
    private String R186_NO_OF_CUSTOMER;
    private String R186_SECTOR_TYPE;
    private String R186_FACILITY_TYPE;
    private BigDecimal R186_ORIGINAL_AMOUNT;
    private BigDecimal R186_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R186_EFFECTIVE_DATE;
    private String R186_REPAYMENT_PERIOD;
    private String R186_PERFORMANCE_STATUS;
    private String R186_SECURITY;
    private String R186_BOARD_APPROVAL;
    private BigDecimal R186_INTEREST_RATE;
    private BigDecimal R186_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R186_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R187
    private String R187_NO_OF_GROUP;
    private String R187_NO_OF_CUSTOMER;
    private String R187_SECTOR_TYPE;
    private String R187_FACILITY_TYPE;
    private BigDecimal R187_ORIGINAL_AMOUNT;
    private BigDecimal R187_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R187_EFFECTIVE_DATE;
    private String R187_REPAYMENT_PERIOD;
    private String R187_PERFORMANCE_STATUS;
    private String R187_SECURITY;
    private String R187_BOARD_APPROVAL;
    private BigDecimal R187_INTEREST_RATE;
    private BigDecimal R187_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R187_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R188
    private String R188_NO_OF_GROUP;
    private String R188_NO_OF_CUSTOMER;
    private String R188_SECTOR_TYPE;
    private String R188_FACILITY_TYPE;
    private BigDecimal R188_ORIGINAL_AMOUNT;
    private BigDecimal R188_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R188_EFFECTIVE_DATE;
    private String R188_REPAYMENT_PERIOD;
    private String R188_PERFORMANCE_STATUS;
    private String R188_SECURITY;
    private String R188_BOARD_APPROVAL;
    private BigDecimal R188_INTEREST_RATE;
    private BigDecimal R188_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R188_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R189
    private String R189_NO_OF_GROUP;
    private String R189_NO_OF_CUSTOMER;
    private String R189_SECTOR_TYPE;
    private String R189_FACILITY_TYPE;
    private BigDecimal R189_ORIGINAL_AMOUNT;
    private BigDecimal R189_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R189_EFFECTIVE_DATE;
    private String R189_REPAYMENT_PERIOD;
    private String R189_PERFORMANCE_STATUS;
    private String R189_SECURITY;
    private String R189_BOARD_APPROVAL;
    private BigDecimal R189_INTEREST_RATE;
    private BigDecimal R189_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R189_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R190
    private String R190_NO_OF_GROUP;
    private String R190_NO_OF_CUSTOMER;
    private String R190_SECTOR_TYPE;
    private String R190_FACILITY_TYPE;
    private BigDecimal R190_ORIGINAL_AMOUNT;
    private BigDecimal R190_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R190_EFFECTIVE_DATE;
    private String R190_REPAYMENT_PERIOD;
    private String R190_PERFORMANCE_STATUS;
    private String R190_SECURITY;
    private String R190_BOARD_APPROVAL;
    private BigDecimal R190_INTEREST_RATE;
    private BigDecimal R190_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R190_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R191
    private String R191_NO_OF_GROUP;
    private String R191_NO_OF_CUSTOMER;
    private String R191_SECTOR_TYPE;
    private String R191_FACILITY_TYPE;
    private BigDecimal R191_ORIGINAL_AMOUNT;
    private BigDecimal R191_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R191_EFFECTIVE_DATE;
    private String R191_REPAYMENT_PERIOD;
    private String R191_PERFORMANCE_STATUS;
    private String R191_SECURITY;
    private String R191_BOARD_APPROVAL;
    private BigDecimal R191_INTEREST_RATE;
    private BigDecimal R191_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R191_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R192
    private String R192_NO_OF_GROUP;
    private String R192_NO_OF_CUSTOMER;
    private String R192_SECTOR_TYPE;
    private String R192_FACILITY_TYPE;
    private BigDecimal R192_ORIGINAL_AMOUNT;
    private BigDecimal R192_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R192_EFFECTIVE_DATE;
    private String R192_REPAYMENT_PERIOD;
    private String R192_PERFORMANCE_STATUS;
    private String R192_SECURITY;
    private String R192_BOARD_APPROVAL;
    private BigDecimal R192_INTEREST_RATE;
    private BigDecimal R192_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R192_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R193
    private String R193_NO_OF_GROUP;
    private String R193_NO_OF_CUSTOMER;
    private String R193_SECTOR_TYPE;
    private String R193_FACILITY_TYPE;
    private BigDecimal R193_ORIGINAL_AMOUNT;
    private BigDecimal R193_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R193_EFFECTIVE_DATE;
    private String R193_REPAYMENT_PERIOD;
    private String R193_PERFORMANCE_STATUS;
    private String R193_SECURITY;
    private String R193_BOARD_APPROVAL;
    private BigDecimal R193_INTEREST_RATE;
    private BigDecimal R193_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R193_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R194
    private String R194_NO_OF_GROUP;
    private String R194_NO_OF_CUSTOMER;
    private String R194_SECTOR_TYPE;
    private String R194_FACILITY_TYPE;
    private BigDecimal R194_ORIGINAL_AMOUNT;
    private BigDecimal R194_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R194_EFFECTIVE_DATE;
    private String R194_REPAYMENT_PERIOD;
    private String R194_PERFORMANCE_STATUS;
    private String R194_SECURITY;
    private String R194_BOARD_APPROVAL;
    private BigDecimal R194_INTEREST_RATE;
    private BigDecimal R194_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R194_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R195
    private String R195_NO_OF_GROUP;
    private String R195_NO_OF_CUSTOMER;
    private String R195_SECTOR_TYPE;
    private String R195_FACILITY_TYPE;
    private BigDecimal R195_ORIGINAL_AMOUNT;
    private BigDecimal R195_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R195_EFFECTIVE_DATE;
    private String R195_REPAYMENT_PERIOD;
    private String R195_PERFORMANCE_STATUS;
    private String R195_SECURITY;
    private String R195_BOARD_APPROVAL;
    private BigDecimal R195_INTEREST_RATE;
    private BigDecimal R195_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R195_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R196
    private String R196_NO_OF_GROUP;
    private String R196_NO_OF_CUSTOMER;
    private String R196_SECTOR_TYPE;
    private String R196_FACILITY_TYPE;
    private BigDecimal R196_ORIGINAL_AMOUNT;
    private BigDecimal R196_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R196_EFFECTIVE_DATE;
    private String R196_REPAYMENT_PERIOD;
    private String R196_PERFORMANCE_STATUS;
    private String R196_SECURITY;
    private String R196_BOARD_APPROVAL;
    private BigDecimal R196_INTEREST_RATE;
    private BigDecimal R196_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R196_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R197
    private String R197_NO_OF_GROUP;
    private String R197_NO_OF_CUSTOMER;
    private String R197_SECTOR_TYPE;
    private String R197_FACILITY_TYPE;
    private BigDecimal R197_ORIGINAL_AMOUNT;
    private BigDecimal R197_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R197_EFFECTIVE_DATE;
    private String R197_REPAYMENT_PERIOD;
    private String R197_PERFORMANCE_STATUS;
    private String R197_SECURITY;
    private String R197_BOARD_APPROVAL;
    private BigDecimal R197_INTEREST_RATE;
    private BigDecimal R197_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R197_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R198
    private String R198_NO_OF_GROUP;
    private String R198_NO_OF_CUSTOMER;
    private String R198_SECTOR_TYPE;
    private String R198_FACILITY_TYPE;
    private BigDecimal R198_ORIGINAL_AMOUNT;
    private BigDecimal R198_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R198_EFFECTIVE_DATE;
    private String R198_REPAYMENT_PERIOD;
    private String R198_PERFORMANCE_STATUS;
    private String R198_SECURITY;
    private String R198_BOARD_APPROVAL;
    private BigDecimal R198_INTEREST_RATE;
    private BigDecimal R198_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R198_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R199
    private String R199_NO_OF_GROUP;
    private String R199_NO_OF_CUSTOMER;
    private String R199_SECTOR_TYPE;
    private String R199_FACILITY_TYPE;
    private BigDecimal R199_ORIGINAL_AMOUNT;
    private BigDecimal R199_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R199_EFFECTIVE_DATE;
    private String R199_REPAYMENT_PERIOD;
    private String R199_PERFORMANCE_STATUS;
    private String R199_SECURITY;
    private String R199_BOARD_APPROVAL;
    private BigDecimal R199_INTEREST_RATE;
    private BigDecimal R199_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R199_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R200
    private String R200_NO_OF_GROUP;
    private String R200_NO_OF_CUSTOMER;
    private String R200_SECTOR_TYPE;
    private String R200_FACILITY_TYPE;
    private BigDecimal R200_ORIGINAL_AMOUNT;
    private BigDecimal R200_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R200_EFFECTIVE_DATE;
    private String R200_REPAYMENT_PERIOD;
    private String R200_PERFORMANCE_STATUS;
    private String R200_SECURITY;
    private String R200_BOARD_APPROVAL;
    private BigDecimal R200_INTEREST_RATE;
    private BigDecimal R200_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R200_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R201
    private String R201_NO_OF_GROUP;
    private String R201_NO_OF_CUSTOMER;
    private String R201_SECTOR_TYPE;
    private String R201_FACILITY_TYPE;
    private BigDecimal R201_ORIGINAL_AMOUNT;
    private BigDecimal R201_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R201_EFFECTIVE_DATE;
    private String R201_REPAYMENT_PERIOD;
    private String R201_PERFORMANCE_STATUS;
    private String R201_SECURITY;
    private String R201_BOARD_APPROVAL;
    private BigDecimal R201_INTEREST_RATE;
    private BigDecimal R201_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R201_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R202
    private String R202_NO_OF_GROUP;
    private String R202_NO_OF_CUSTOMER;
    private String R202_SECTOR_TYPE;
    private String R202_FACILITY_TYPE;
    private BigDecimal R202_ORIGINAL_AMOUNT;
    private BigDecimal R202_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R202_EFFECTIVE_DATE;
    private String R202_REPAYMENT_PERIOD;
    private String R202_PERFORMANCE_STATUS;
    private String R202_SECURITY;
    private String R202_BOARD_APPROVAL;
    private BigDecimal R202_INTEREST_RATE;
    private BigDecimal R202_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R202_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R203
    private String R203_NO_OF_GROUP;
    private String R203_NO_OF_CUSTOMER;
    private String R203_SECTOR_TYPE;
    private String R203_FACILITY_TYPE;
    private BigDecimal R203_ORIGINAL_AMOUNT;
    private BigDecimal R203_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R203_EFFECTIVE_DATE;
    private String R203_REPAYMENT_PERIOD;
    private String R203_PERFORMANCE_STATUS;
    private String R203_SECURITY;
    private String R203_BOARD_APPROVAL;
    private BigDecimal R203_INTEREST_RATE;
    private BigDecimal R203_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R203_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R204
    private String R204_NO_OF_GROUP;
    private String R204_NO_OF_CUSTOMER;
    private String R204_SECTOR_TYPE;
    private String R204_FACILITY_TYPE;
    private BigDecimal R204_ORIGINAL_AMOUNT;
    private BigDecimal R204_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R204_EFFECTIVE_DATE;
    private String R204_REPAYMENT_PERIOD;
    private String R204_PERFORMANCE_STATUS;
    private String R204_SECURITY;
    private String R204_BOARD_APPROVAL;
    private BigDecimal R204_INTEREST_RATE;
    private BigDecimal R204_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R204_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R205
    private String R205_NO_OF_GROUP;
    private String R205_NO_OF_CUSTOMER;
    private String R205_SECTOR_TYPE;
    private String R205_FACILITY_TYPE;
    private BigDecimal R205_ORIGINAL_AMOUNT;
    private BigDecimal R205_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R205_EFFECTIVE_DATE;
    private String R205_REPAYMENT_PERIOD;
    private String R205_PERFORMANCE_STATUS;
    private String R205_SECURITY;
    private String R205_BOARD_APPROVAL;
    private BigDecimal R205_INTEREST_RATE;
    private BigDecimal R205_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R205_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R206
    private String R206_NO_OF_GROUP;
    private String R206_NO_OF_CUSTOMER;
    private String R206_SECTOR_TYPE;
    private String R206_FACILITY_TYPE;
    private BigDecimal R206_ORIGINAL_AMOUNT;
    private BigDecimal R206_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R206_EFFECTIVE_DATE;
    private String R206_REPAYMENT_PERIOD;
    private String R206_PERFORMANCE_STATUS;
    private String R206_SECURITY;
    private String R206_BOARD_APPROVAL;
    private BigDecimal R206_INTEREST_RATE;
    private BigDecimal R206_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R206_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R207
    private String R207_NO_OF_GROUP;
    private String R207_NO_OF_CUSTOMER;
    private String R207_SECTOR_TYPE;
    private String R207_FACILITY_TYPE;
    private BigDecimal R207_ORIGINAL_AMOUNT;
    private BigDecimal R207_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R207_EFFECTIVE_DATE;
    private String R207_REPAYMENT_PERIOD;
    private String R207_PERFORMANCE_STATUS;
    private String R207_SECURITY;
    private String R207_BOARD_APPROVAL;
    private BigDecimal R207_INTEREST_RATE;
    private BigDecimal R207_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R207_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R208
    private String R208_NO_OF_GROUP;
    private String R208_NO_OF_CUSTOMER;
    private String R208_SECTOR_TYPE;
    private String R208_FACILITY_TYPE;
    private BigDecimal R208_ORIGINAL_AMOUNT;
    private BigDecimal R208_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R208_EFFECTIVE_DATE;
    private String R208_REPAYMENT_PERIOD;
    private String R208_PERFORMANCE_STATUS;
    private String R208_SECURITY;
    private String R208_BOARD_APPROVAL;
    private BigDecimal R208_INTEREST_RATE;
    private BigDecimal R208_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R208_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R209
    private String R209_NO_OF_GROUP;
    private String R209_NO_OF_CUSTOMER;
    private String R209_SECTOR_TYPE;
    private String R209_FACILITY_TYPE;
    private BigDecimal R209_ORIGINAL_AMOUNT;
    private BigDecimal R209_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R209_EFFECTIVE_DATE;
    private String R209_REPAYMENT_PERIOD;
    private String R209_PERFORMANCE_STATUS;
    private String R209_SECURITY;
    private String R209_BOARD_APPROVAL;
    private BigDecimal R209_INTEREST_RATE;
    private BigDecimal R209_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R209_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R210
    private String R210_NO_OF_GROUP;
    private String R210_NO_OF_CUSTOMER;
    private String R210_SECTOR_TYPE;
    private String R210_FACILITY_TYPE;
    private BigDecimal R210_ORIGINAL_AMOUNT;
    private BigDecimal R210_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R210_EFFECTIVE_DATE;
    private String R210_REPAYMENT_PERIOD;
    private String R210_PERFORMANCE_STATUS;
    private String R210_SECURITY;
    private String R210_BOARD_APPROVAL;
    private BigDecimal R210_INTEREST_RATE;
    private BigDecimal R210_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R210_LIMIT_PCT_UNIMPAIRED_CAP;
	
	

    @Id
    private Date report_date;
    private BigDecimal report_version;

    @Column(name = "REPORT_RESUBDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportResubDate;
	private String report_frequency;
	private String report_code;
	private String report_desc;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;
	public String getR141_NO_OF_GROUP() {
		return R141_NO_OF_GROUP;
	}
	public void setR141_NO_OF_GROUP(String r141_NO_OF_GROUP) {
		R141_NO_OF_GROUP = r141_NO_OF_GROUP;
	}
	public String getR141_NO_OF_CUSTOMER() {
		return R141_NO_OF_CUSTOMER;
	}
	public void setR141_NO_OF_CUSTOMER(String r141_NO_OF_CUSTOMER) {
		R141_NO_OF_CUSTOMER = r141_NO_OF_CUSTOMER;
	}
	public String getR141_SECTOR_TYPE() {
		return R141_SECTOR_TYPE;
	}
	public void setR141_SECTOR_TYPE(String r141_SECTOR_TYPE) {
		R141_SECTOR_TYPE = r141_SECTOR_TYPE;
	}
	public String getR141_FACILITY_TYPE() {
		return R141_FACILITY_TYPE;
	}
	public void setR141_FACILITY_TYPE(String r141_FACILITY_TYPE) {
		R141_FACILITY_TYPE = r141_FACILITY_TYPE;
	}
	public BigDecimal getR141_ORIGINAL_AMOUNT() {
		return R141_ORIGINAL_AMOUNT;
	}
	public void setR141_ORIGINAL_AMOUNT(BigDecimal r141_ORIGINAL_AMOUNT) {
		R141_ORIGINAL_AMOUNT = r141_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR141_UTILISATION_OUTSTANDING_BAL() {
		return R141_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR141_UTILISATION_OUTSTANDING_BAL(BigDecimal r141_UTILISATION_OUTSTANDING_BAL) {
		R141_UTILISATION_OUTSTANDING_BAL = r141_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR141_EFFECTIVE_DATE() {
		return R141_EFFECTIVE_DATE;
	}
	public void setR141_EFFECTIVE_DATE(Date r141_EFFECTIVE_DATE) {
		R141_EFFECTIVE_DATE = r141_EFFECTIVE_DATE;
	}
	public String getR141_REPAYMENT_PERIOD() {
		return R141_REPAYMENT_PERIOD;
	}
	public void setR141_REPAYMENT_PERIOD(String r141_REPAYMENT_PERIOD) {
		R141_REPAYMENT_PERIOD = r141_REPAYMENT_PERIOD;
	}
	public String getR141_PERFORMANCE_STATUS() {
		return R141_PERFORMANCE_STATUS;
	}
	public void setR141_PERFORMANCE_STATUS(String r141_PERFORMANCE_STATUS) {
		R141_PERFORMANCE_STATUS = r141_PERFORMANCE_STATUS;
	}
	public String getR141_SECURITY() {
		return R141_SECURITY;
	}
	public void setR141_SECURITY(String r141_SECURITY) {
		R141_SECURITY = r141_SECURITY;
	}
	public String getR141_BOARD_APPROVAL() {
		return R141_BOARD_APPROVAL;
	}
	public void setR141_BOARD_APPROVAL(String r141_BOARD_APPROVAL) {
		R141_BOARD_APPROVAL = r141_BOARD_APPROVAL;
	}
	public BigDecimal getR141_INTEREST_RATE() {
		return R141_INTEREST_RATE;
	}
	public void setR141_INTEREST_RATE(BigDecimal r141_INTEREST_RATE) {
		R141_INTEREST_RATE = r141_INTEREST_RATE;
	}
	public BigDecimal getR141_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R141_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR141_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r141_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R141_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r141_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR141_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R141_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR141_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r141_LIMIT_PCT_UNIMPAIRED_CAP) {
		R141_LIMIT_PCT_UNIMPAIRED_CAP = r141_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR142_NO_OF_GROUP() {
		return R142_NO_OF_GROUP;
	}
	public void setR142_NO_OF_GROUP(String r142_NO_OF_GROUP) {
		R142_NO_OF_GROUP = r142_NO_OF_GROUP;
	}
	public String getR142_NO_OF_CUSTOMER() {
		return R142_NO_OF_CUSTOMER;
	}
	public void setR142_NO_OF_CUSTOMER(String r142_NO_OF_CUSTOMER) {
		R142_NO_OF_CUSTOMER = r142_NO_OF_CUSTOMER;
	}
	public String getR142_SECTOR_TYPE() {
		return R142_SECTOR_TYPE;
	}
	public void setR142_SECTOR_TYPE(String r142_SECTOR_TYPE) {
		R142_SECTOR_TYPE = r142_SECTOR_TYPE;
	}
	public String getR142_FACILITY_TYPE() {
		return R142_FACILITY_TYPE;
	}
	public void setR142_FACILITY_TYPE(String r142_FACILITY_TYPE) {
		R142_FACILITY_TYPE = r142_FACILITY_TYPE;
	}
	public BigDecimal getR142_ORIGINAL_AMOUNT() {
		return R142_ORIGINAL_AMOUNT;
	}
	public void setR142_ORIGINAL_AMOUNT(BigDecimal r142_ORIGINAL_AMOUNT) {
		R142_ORIGINAL_AMOUNT = r142_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR142_UTILISATION_OUTSTANDING_BAL() {
		return R142_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR142_UTILISATION_OUTSTANDING_BAL(BigDecimal r142_UTILISATION_OUTSTANDING_BAL) {
		R142_UTILISATION_OUTSTANDING_BAL = r142_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR142_EFFECTIVE_DATE() {
		return R142_EFFECTIVE_DATE;
	}
	public void setR142_EFFECTIVE_DATE(Date r142_EFFECTIVE_DATE) {
		R142_EFFECTIVE_DATE = r142_EFFECTIVE_DATE;
	}
	public String getR142_REPAYMENT_PERIOD() {
		return R142_REPAYMENT_PERIOD;
	}
	public void setR142_REPAYMENT_PERIOD(String r142_REPAYMENT_PERIOD) {
		R142_REPAYMENT_PERIOD = r142_REPAYMENT_PERIOD;
	}
	public String getR142_PERFORMANCE_STATUS() {
		return R142_PERFORMANCE_STATUS;
	}
	public void setR142_PERFORMANCE_STATUS(String r142_PERFORMANCE_STATUS) {
		R142_PERFORMANCE_STATUS = r142_PERFORMANCE_STATUS;
	}
	public String getR142_SECURITY() {
		return R142_SECURITY;
	}
	public void setR142_SECURITY(String r142_SECURITY) {
		R142_SECURITY = r142_SECURITY;
	}
	public String getR142_BOARD_APPROVAL() {
		return R142_BOARD_APPROVAL;
	}
	public void setR142_BOARD_APPROVAL(String r142_BOARD_APPROVAL) {
		R142_BOARD_APPROVAL = r142_BOARD_APPROVAL;
	}
	public BigDecimal getR142_INTEREST_RATE() {
		return R142_INTEREST_RATE;
	}
	public void setR142_INTEREST_RATE(BigDecimal r142_INTEREST_RATE) {
		R142_INTEREST_RATE = r142_INTEREST_RATE;
	}
	public BigDecimal getR142_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R142_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR142_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r142_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R142_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r142_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR142_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R142_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR142_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r142_LIMIT_PCT_UNIMPAIRED_CAP) {
		R142_LIMIT_PCT_UNIMPAIRED_CAP = r142_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR143_NO_OF_GROUP() {
		return R143_NO_OF_GROUP;
	}
	public void setR143_NO_OF_GROUP(String r143_NO_OF_GROUP) {
		R143_NO_OF_GROUP = r143_NO_OF_GROUP;
	}
	public String getR143_NO_OF_CUSTOMER() {
		return R143_NO_OF_CUSTOMER;
	}
	public void setR143_NO_OF_CUSTOMER(String r143_NO_OF_CUSTOMER) {
		R143_NO_OF_CUSTOMER = r143_NO_OF_CUSTOMER;
	}
	public String getR143_SECTOR_TYPE() {
		return R143_SECTOR_TYPE;
	}
	public void setR143_SECTOR_TYPE(String r143_SECTOR_TYPE) {
		R143_SECTOR_TYPE = r143_SECTOR_TYPE;
	}
	public String getR143_FACILITY_TYPE() {
		return R143_FACILITY_TYPE;
	}
	public void setR143_FACILITY_TYPE(String r143_FACILITY_TYPE) {
		R143_FACILITY_TYPE = r143_FACILITY_TYPE;
	}
	public BigDecimal getR143_ORIGINAL_AMOUNT() {
		return R143_ORIGINAL_AMOUNT;
	}
	public void setR143_ORIGINAL_AMOUNT(BigDecimal r143_ORIGINAL_AMOUNT) {
		R143_ORIGINAL_AMOUNT = r143_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR143_UTILISATION_OUTSTANDING_BAL() {
		return R143_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR143_UTILISATION_OUTSTANDING_BAL(BigDecimal r143_UTILISATION_OUTSTANDING_BAL) {
		R143_UTILISATION_OUTSTANDING_BAL = r143_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR143_EFFECTIVE_DATE() {
		return R143_EFFECTIVE_DATE;
	}
	public void setR143_EFFECTIVE_DATE(Date r143_EFFECTIVE_DATE) {
		R143_EFFECTIVE_DATE = r143_EFFECTIVE_DATE;
	}
	public String getR143_REPAYMENT_PERIOD() {
		return R143_REPAYMENT_PERIOD;
	}
	public void setR143_REPAYMENT_PERIOD(String r143_REPAYMENT_PERIOD) {
		R143_REPAYMENT_PERIOD = r143_REPAYMENT_PERIOD;
	}
	public String getR143_PERFORMANCE_STATUS() {
		return R143_PERFORMANCE_STATUS;
	}
	public void setR143_PERFORMANCE_STATUS(String r143_PERFORMANCE_STATUS) {
		R143_PERFORMANCE_STATUS = r143_PERFORMANCE_STATUS;
	}
	public String getR143_SECURITY() {
		return R143_SECURITY;
	}
	public void setR143_SECURITY(String r143_SECURITY) {
		R143_SECURITY = r143_SECURITY;
	}
	public String getR143_BOARD_APPROVAL() {
		return R143_BOARD_APPROVAL;
	}
	public void setR143_BOARD_APPROVAL(String r143_BOARD_APPROVAL) {
		R143_BOARD_APPROVAL = r143_BOARD_APPROVAL;
	}
	public BigDecimal getR143_INTEREST_RATE() {
		return R143_INTEREST_RATE;
	}
	public void setR143_INTEREST_RATE(BigDecimal r143_INTEREST_RATE) {
		R143_INTEREST_RATE = r143_INTEREST_RATE;
	}
	public BigDecimal getR143_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R143_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR143_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r143_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R143_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r143_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR143_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R143_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR143_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r143_LIMIT_PCT_UNIMPAIRED_CAP) {
		R143_LIMIT_PCT_UNIMPAIRED_CAP = r143_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR144_NO_OF_GROUP() {
		return R144_NO_OF_GROUP;
	}
	public void setR144_NO_OF_GROUP(String r144_NO_OF_GROUP) {
		R144_NO_OF_GROUP = r144_NO_OF_GROUP;
	}
	public String getR144_NO_OF_CUSTOMER() {
		return R144_NO_OF_CUSTOMER;
	}
	public void setR144_NO_OF_CUSTOMER(String r144_NO_OF_CUSTOMER) {
		R144_NO_OF_CUSTOMER = r144_NO_OF_CUSTOMER;
	}
	public String getR144_SECTOR_TYPE() {
		return R144_SECTOR_TYPE;
	}
	public void setR144_SECTOR_TYPE(String r144_SECTOR_TYPE) {
		R144_SECTOR_TYPE = r144_SECTOR_TYPE;
	}
	public String getR144_FACILITY_TYPE() {
		return R144_FACILITY_TYPE;
	}
	public void setR144_FACILITY_TYPE(String r144_FACILITY_TYPE) {
		R144_FACILITY_TYPE = r144_FACILITY_TYPE;
	}
	public BigDecimal getR144_ORIGINAL_AMOUNT() {
		return R144_ORIGINAL_AMOUNT;
	}
	public void setR144_ORIGINAL_AMOUNT(BigDecimal r144_ORIGINAL_AMOUNT) {
		R144_ORIGINAL_AMOUNT = r144_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR144_UTILISATION_OUTSTANDING_BAL() {
		return R144_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR144_UTILISATION_OUTSTANDING_BAL(BigDecimal r144_UTILISATION_OUTSTANDING_BAL) {
		R144_UTILISATION_OUTSTANDING_BAL = r144_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR144_EFFECTIVE_DATE() {
		return R144_EFFECTIVE_DATE;
	}
	public void setR144_EFFECTIVE_DATE(Date r144_EFFECTIVE_DATE) {
		R144_EFFECTIVE_DATE = r144_EFFECTIVE_DATE;
	}
	public String getR144_REPAYMENT_PERIOD() {
		return R144_REPAYMENT_PERIOD;
	}
	public void setR144_REPAYMENT_PERIOD(String r144_REPAYMENT_PERIOD) {
		R144_REPAYMENT_PERIOD = r144_REPAYMENT_PERIOD;
	}
	public String getR144_PERFORMANCE_STATUS() {
		return R144_PERFORMANCE_STATUS;
	}
	public void setR144_PERFORMANCE_STATUS(String r144_PERFORMANCE_STATUS) {
		R144_PERFORMANCE_STATUS = r144_PERFORMANCE_STATUS;
	}
	public String getR144_SECURITY() {
		return R144_SECURITY;
	}
	public void setR144_SECURITY(String r144_SECURITY) {
		R144_SECURITY = r144_SECURITY;
	}
	public String getR144_BOARD_APPROVAL() {
		return R144_BOARD_APPROVAL;
	}
	public void setR144_BOARD_APPROVAL(String r144_BOARD_APPROVAL) {
		R144_BOARD_APPROVAL = r144_BOARD_APPROVAL;
	}
	public BigDecimal getR144_INTEREST_RATE() {
		return R144_INTEREST_RATE;
	}
	public void setR144_INTEREST_RATE(BigDecimal r144_INTEREST_RATE) {
		R144_INTEREST_RATE = r144_INTEREST_RATE;
	}
	public BigDecimal getR144_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R144_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR144_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r144_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R144_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r144_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR144_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R144_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR144_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r144_LIMIT_PCT_UNIMPAIRED_CAP) {
		R144_LIMIT_PCT_UNIMPAIRED_CAP = r144_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR145_NO_OF_GROUP() {
		return R145_NO_OF_GROUP;
	}
	public void setR145_NO_OF_GROUP(String r145_NO_OF_GROUP) {
		R145_NO_OF_GROUP = r145_NO_OF_GROUP;
	}
	public String getR145_NO_OF_CUSTOMER() {
		return R145_NO_OF_CUSTOMER;
	}
	public void setR145_NO_OF_CUSTOMER(String r145_NO_OF_CUSTOMER) {
		R145_NO_OF_CUSTOMER = r145_NO_OF_CUSTOMER;
	}
	public String getR145_SECTOR_TYPE() {
		return R145_SECTOR_TYPE;
	}
	public void setR145_SECTOR_TYPE(String r145_SECTOR_TYPE) {
		R145_SECTOR_TYPE = r145_SECTOR_TYPE;
	}
	public String getR145_FACILITY_TYPE() {
		return R145_FACILITY_TYPE;
	}
	public void setR145_FACILITY_TYPE(String r145_FACILITY_TYPE) {
		R145_FACILITY_TYPE = r145_FACILITY_TYPE;
	}
	public BigDecimal getR145_ORIGINAL_AMOUNT() {
		return R145_ORIGINAL_AMOUNT;
	}
	public void setR145_ORIGINAL_AMOUNT(BigDecimal r145_ORIGINAL_AMOUNT) {
		R145_ORIGINAL_AMOUNT = r145_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR145_UTILISATION_OUTSTANDING_BAL() {
		return R145_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR145_UTILISATION_OUTSTANDING_BAL(BigDecimal r145_UTILISATION_OUTSTANDING_BAL) {
		R145_UTILISATION_OUTSTANDING_BAL = r145_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR145_EFFECTIVE_DATE() {
		return R145_EFFECTIVE_DATE;
	}
	public void setR145_EFFECTIVE_DATE(Date r145_EFFECTIVE_DATE) {
		R145_EFFECTIVE_DATE = r145_EFFECTIVE_DATE;
	}
	public String getR145_REPAYMENT_PERIOD() {
		return R145_REPAYMENT_PERIOD;
	}
	public void setR145_REPAYMENT_PERIOD(String r145_REPAYMENT_PERIOD) {
		R145_REPAYMENT_PERIOD = r145_REPAYMENT_PERIOD;
	}
	public String getR145_PERFORMANCE_STATUS() {
		return R145_PERFORMANCE_STATUS;
	}
	public void setR145_PERFORMANCE_STATUS(String r145_PERFORMANCE_STATUS) {
		R145_PERFORMANCE_STATUS = r145_PERFORMANCE_STATUS;
	}
	public String getR145_SECURITY() {
		return R145_SECURITY;
	}
	public void setR145_SECURITY(String r145_SECURITY) {
		R145_SECURITY = r145_SECURITY;
	}
	public String getR145_BOARD_APPROVAL() {
		return R145_BOARD_APPROVAL;
	}
	public void setR145_BOARD_APPROVAL(String r145_BOARD_APPROVAL) {
		R145_BOARD_APPROVAL = r145_BOARD_APPROVAL;
	}
	public BigDecimal getR145_INTEREST_RATE() {
		return R145_INTEREST_RATE;
	}
	public void setR145_INTEREST_RATE(BigDecimal r145_INTEREST_RATE) {
		R145_INTEREST_RATE = r145_INTEREST_RATE;
	}
	public BigDecimal getR145_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R145_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR145_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r145_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R145_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r145_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR145_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R145_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR145_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r145_LIMIT_PCT_UNIMPAIRED_CAP) {
		R145_LIMIT_PCT_UNIMPAIRED_CAP = r145_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR146_NO_OF_GROUP() {
		return R146_NO_OF_GROUP;
	}
	public void setR146_NO_OF_GROUP(String r146_NO_OF_GROUP) {
		R146_NO_OF_GROUP = r146_NO_OF_GROUP;
	}
	public String getR146_NO_OF_CUSTOMER() {
		return R146_NO_OF_CUSTOMER;
	}
	public void setR146_NO_OF_CUSTOMER(String r146_NO_OF_CUSTOMER) {
		R146_NO_OF_CUSTOMER = r146_NO_OF_CUSTOMER;
	}
	public String getR146_SECTOR_TYPE() {
		return R146_SECTOR_TYPE;
	}
	public void setR146_SECTOR_TYPE(String r146_SECTOR_TYPE) {
		R146_SECTOR_TYPE = r146_SECTOR_TYPE;
	}
	public String getR146_FACILITY_TYPE() {
		return R146_FACILITY_TYPE;
	}
	public void setR146_FACILITY_TYPE(String r146_FACILITY_TYPE) {
		R146_FACILITY_TYPE = r146_FACILITY_TYPE;
	}
	public BigDecimal getR146_ORIGINAL_AMOUNT() {
		return R146_ORIGINAL_AMOUNT;
	}
	public void setR146_ORIGINAL_AMOUNT(BigDecimal r146_ORIGINAL_AMOUNT) {
		R146_ORIGINAL_AMOUNT = r146_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR146_UTILISATION_OUTSTANDING_BAL() {
		return R146_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR146_UTILISATION_OUTSTANDING_BAL(BigDecimal r146_UTILISATION_OUTSTANDING_BAL) {
		R146_UTILISATION_OUTSTANDING_BAL = r146_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR146_EFFECTIVE_DATE() {
		return R146_EFFECTIVE_DATE;
	}
	public void setR146_EFFECTIVE_DATE(Date r146_EFFECTIVE_DATE) {
		R146_EFFECTIVE_DATE = r146_EFFECTIVE_DATE;
	}
	public String getR146_REPAYMENT_PERIOD() {
		return R146_REPAYMENT_PERIOD;
	}
	public void setR146_REPAYMENT_PERIOD(String r146_REPAYMENT_PERIOD) {
		R146_REPAYMENT_PERIOD = r146_REPAYMENT_PERIOD;
	}
	public String getR146_PERFORMANCE_STATUS() {
		return R146_PERFORMANCE_STATUS;
	}
	public void setR146_PERFORMANCE_STATUS(String r146_PERFORMANCE_STATUS) {
		R146_PERFORMANCE_STATUS = r146_PERFORMANCE_STATUS;
	}
	public String getR146_SECURITY() {
		return R146_SECURITY;
	}
	public void setR146_SECURITY(String r146_SECURITY) {
		R146_SECURITY = r146_SECURITY;
	}
	public String getR146_BOARD_APPROVAL() {
		return R146_BOARD_APPROVAL;
	}
	public void setR146_BOARD_APPROVAL(String r146_BOARD_APPROVAL) {
		R146_BOARD_APPROVAL = r146_BOARD_APPROVAL;
	}
	public BigDecimal getR146_INTEREST_RATE() {
		return R146_INTEREST_RATE;
	}
	public void setR146_INTEREST_RATE(BigDecimal r146_INTEREST_RATE) {
		R146_INTEREST_RATE = r146_INTEREST_RATE;
	}
	public BigDecimal getR146_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R146_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR146_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r146_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R146_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r146_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR146_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R146_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR146_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r146_LIMIT_PCT_UNIMPAIRED_CAP) {
		R146_LIMIT_PCT_UNIMPAIRED_CAP = r146_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR147_NO_OF_GROUP() {
		return R147_NO_OF_GROUP;
	}
	public void setR147_NO_OF_GROUP(String r147_NO_OF_GROUP) {
		R147_NO_OF_GROUP = r147_NO_OF_GROUP;
	}
	public String getR147_NO_OF_CUSTOMER() {
		return R147_NO_OF_CUSTOMER;
	}
	public void setR147_NO_OF_CUSTOMER(String r147_NO_OF_CUSTOMER) {
		R147_NO_OF_CUSTOMER = r147_NO_OF_CUSTOMER;
	}
	public String getR147_SECTOR_TYPE() {
		return R147_SECTOR_TYPE;
	}
	public void setR147_SECTOR_TYPE(String r147_SECTOR_TYPE) {
		R147_SECTOR_TYPE = r147_SECTOR_TYPE;
	}
	public String getR147_FACILITY_TYPE() {
		return R147_FACILITY_TYPE;
	}
	public void setR147_FACILITY_TYPE(String r147_FACILITY_TYPE) {
		R147_FACILITY_TYPE = r147_FACILITY_TYPE;
	}
	public BigDecimal getR147_ORIGINAL_AMOUNT() {
		return R147_ORIGINAL_AMOUNT;
	}
	public void setR147_ORIGINAL_AMOUNT(BigDecimal r147_ORIGINAL_AMOUNT) {
		R147_ORIGINAL_AMOUNT = r147_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR147_UTILISATION_OUTSTANDING_BAL() {
		return R147_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR147_UTILISATION_OUTSTANDING_BAL(BigDecimal r147_UTILISATION_OUTSTANDING_BAL) {
		R147_UTILISATION_OUTSTANDING_BAL = r147_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR147_EFFECTIVE_DATE() {
		return R147_EFFECTIVE_DATE;
	}
	public void setR147_EFFECTIVE_DATE(Date r147_EFFECTIVE_DATE) {
		R147_EFFECTIVE_DATE = r147_EFFECTIVE_DATE;
	}
	public String getR147_REPAYMENT_PERIOD() {
		return R147_REPAYMENT_PERIOD;
	}
	public void setR147_REPAYMENT_PERIOD(String r147_REPAYMENT_PERIOD) {
		R147_REPAYMENT_PERIOD = r147_REPAYMENT_PERIOD;
	}
	public String getR147_PERFORMANCE_STATUS() {
		return R147_PERFORMANCE_STATUS;
	}
	public void setR147_PERFORMANCE_STATUS(String r147_PERFORMANCE_STATUS) {
		R147_PERFORMANCE_STATUS = r147_PERFORMANCE_STATUS;
	}
	public String getR147_SECURITY() {
		return R147_SECURITY;
	}
	public void setR147_SECURITY(String r147_SECURITY) {
		R147_SECURITY = r147_SECURITY;
	}
	public String getR147_BOARD_APPROVAL() {
		return R147_BOARD_APPROVAL;
	}
	public void setR147_BOARD_APPROVAL(String r147_BOARD_APPROVAL) {
		R147_BOARD_APPROVAL = r147_BOARD_APPROVAL;
	}
	public BigDecimal getR147_INTEREST_RATE() {
		return R147_INTEREST_RATE;
	}
	public void setR147_INTEREST_RATE(BigDecimal r147_INTEREST_RATE) {
		R147_INTEREST_RATE = r147_INTEREST_RATE;
	}
	public BigDecimal getR147_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R147_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR147_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r147_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R147_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r147_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR147_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R147_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR147_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r147_LIMIT_PCT_UNIMPAIRED_CAP) {
		R147_LIMIT_PCT_UNIMPAIRED_CAP = r147_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR148_NO_OF_GROUP() {
		return R148_NO_OF_GROUP;
	}
	public void setR148_NO_OF_GROUP(String r148_NO_OF_GROUP) {
		R148_NO_OF_GROUP = r148_NO_OF_GROUP;
	}
	public String getR148_NO_OF_CUSTOMER() {
		return R148_NO_OF_CUSTOMER;
	}
	public void setR148_NO_OF_CUSTOMER(String r148_NO_OF_CUSTOMER) {
		R148_NO_OF_CUSTOMER = r148_NO_OF_CUSTOMER;
	}
	public String getR148_SECTOR_TYPE() {
		return R148_SECTOR_TYPE;
	}
	public void setR148_SECTOR_TYPE(String r148_SECTOR_TYPE) {
		R148_SECTOR_TYPE = r148_SECTOR_TYPE;
	}
	public String getR148_FACILITY_TYPE() {
		return R148_FACILITY_TYPE;
	}
	public void setR148_FACILITY_TYPE(String r148_FACILITY_TYPE) {
		R148_FACILITY_TYPE = r148_FACILITY_TYPE;
	}
	public BigDecimal getR148_ORIGINAL_AMOUNT() {
		return R148_ORIGINAL_AMOUNT;
	}
	public void setR148_ORIGINAL_AMOUNT(BigDecimal r148_ORIGINAL_AMOUNT) {
		R148_ORIGINAL_AMOUNT = r148_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR148_UTILISATION_OUTSTANDING_BAL() {
		return R148_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR148_UTILISATION_OUTSTANDING_BAL(BigDecimal r148_UTILISATION_OUTSTANDING_BAL) {
		R148_UTILISATION_OUTSTANDING_BAL = r148_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR148_EFFECTIVE_DATE() {
		return R148_EFFECTIVE_DATE;
	}
	public void setR148_EFFECTIVE_DATE(Date r148_EFFECTIVE_DATE) {
		R148_EFFECTIVE_DATE = r148_EFFECTIVE_DATE;
	}
	public String getR148_REPAYMENT_PERIOD() {
		return R148_REPAYMENT_PERIOD;
	}
	public void setR148_REPAYMENT_PERIOD(String r148_REPAYMENT_PERIOD) {
		R148_REPAYMENT_PERIOD = r148_REPAYMENT_PERIOD;
	}
	public String getR148_PERFORMANCE_STATUS() {
		return R148_PERFORMANCE_STATUS;
	}
	public void setR148_PERFORMANCE_STATUS(String r148_PERFORMANCE_STATUS) {
		R148_PERFORMANCE_STATUS = r148_PERFORMANCE_STATUS;
	}
	public String getR148_SECURITY() {
		return R148_SECURITY;
	}
	public void setR148_SECURITY(String r148_SECURITY) {
		R148_SECURITY = r148_SECURITY;
	}
	public String getR148_BOARD_APPROVAL() {
		return R148_BOARD_APPROVAL;
	}
	public void setR148_BOARD_APPROVAL(String r148_BOARD_APPROVAL) {
		R148_BOARD_APPROVAL = r148_BOARD_APPROVAL;
	}
	public BigDecimal getR148_INTEREST_RATE() {
		return R148_INTEREST_RATE;
	}
	public void setR148_INTEREST_RATE(BigDecimal r148_INTEREST_RATE) {
		R148_INTEREST_RATE = r148_INTEREST_RATE;
	}
	public BigDecimal getR148_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R148_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR148_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r148_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R148_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r148_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR148_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R148_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR148_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r148_LIMIT_PCT_UNIMPAIRED_CAP) {
		R148_LIMIT_PCT_UNIMPAIRED_CAP = r148_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR149_NO_OF_GROUP() {
		return R149_NO_OF_GROUP;
	}
	public void setR149_NO_OF_GROUP(String r149_NO_OF_GROUP) {
		R149_NO_OF_GROUP = r149_NO_OF_GROUP;
	}
	public String getR149_NO_OF_CUSTOMER() {
		return R149_NO_OF_CUSTOMER;
	}
	public void setR149_NO_OF_CUSTOMER(String r149_NO_OF_CUSTOMER) {
		R149_NO_OF_CUSTOMER = r149_NO_OF_CUSTOMER;
	}
	public String getR149_SECTOR_TYPE() {
		return R149_SECTOR_TYPE;
	}
	public void setR149_SECTOR_TYPE(String r149_SECTOR_TYPE) {
		R149_SECTOR_TYPE = r149_SECTOR_TYPE;
	}
	public String getR149_FACILITY_TYPE() {
		return R149_FACILITY_TYPE;
	}
	public void setR149_FACILITY_TYPE(String r149_FACILITY_TYPE) {
		R149_FACILITY_TYPE = r149_FACILITY_TYPE;
	}
	public BigDecimal getR149_ORIGINAL_AMOUNT() {
		return R149_ORIGINAL_AMOUNT;
	}
	public void setR149_ORIGINAL_AMOUNT(BigDecimal r149_ORIGINAL_AMOUNT) {
		R149_ORIGINAL_AMOUNT = r149_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR149_UTILISATION_OUTSTANDING_BAL() {
		return R149_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR149_UTILISATION_OUTSTANDING_BAL(BigDecimal r149_UTILISATION_OUTSTANDING_BAL) {
		R149_UTILISATION_OUTSTANDING_BAL = r149_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR149_EFFECTIVE_DATE() {
		return R149_EFFECTIVE_DATE;
	}
	public void setR149_EFFECTIVE_DATE(Date r149_EFFECTIVE_DATE) {
		R149_EFFECTIVE_DATE = r149_EFFECTIVE_DATE;
	}
	public String getR149_REPAYMENT_PERIOD() {
		return R149_REPAYMENT_PERIOD;
	}
	public void setR149_REPAYMENT_PERIOD(String r149_REPAYMENT_PERIOD) {
		R149_REPAYMENT_PERIOD = r149_REPAYMENT_PERIOD;
	}
	public String getR149_PERFORMANCE_STATUS() {
		return R149_PERFORMANCE_STATUS;
	}
	public void setR149_PERFORMANCE_STATUS(String r149_PERFORMANCE_STATUS) {
		R149_PERFORMANCE_STATUS = r149_PERFORMANCE_STATUS;
	}
	public String getR149_SECURITY() {
		return R149_SECURITY;
	}
	public void setR149_SECURITY(String r149_SECURITY) {
		R149_SECURITY = r149_SECURITY;
	}
	public String getR149_BOARD_APPROVAL() {
		return R149_BOARD_APPROVAL;
	}
	public void setR149_BOARD_APPROVAL(String r149_BOARD_APPROVAL) {
		R149_BOARD_APPROVAL = r149_BOARD_APPROVAL;
	}
	public BigDecimal getR149_INTEREST_RATE() {
		return R149_INTEREST_RATE;
	}
	public void setR149_INTEREST_RATE(BigDecimal r149_INTEREST_RATE) {
		R149_INTEREST_RATE = r149_INTEREST_RATE;
	}
	public BigDecimal getR149_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R149_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR149_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r149_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R149_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r149_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR149_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R149_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR149_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r149_LIMIT_PCT_UNIMPAIRED_CAP) {
		R149_LIMIT_PCT_UNIMPAIRED_CAP = r149_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR150_NO_OF_GROUP() {
		return R150_NO_OF_GROUP;
	}
	public void setR150_NO_OF_GROUP(String r150_NO_OF_GROUP) {
		R150_NO_OF_GROUP = r150_NO_OF_GROUP;
	}
	public String getR150_NO_OF_CUSTOMER() {
		return R150_NO_OF_CUSTOMER;
	}
	public void setR150_NO_OF_CUSTOMER(String r150_NO_OF_CUSTOMER) {
		R150_NO_OF_CUSTOMER = r150_NO_OF_CUSTOMER;
	}
	public String getR150_SECTOR_TYPE() {
		return R150_SECTOR_TYPE;
	}
	public void setR150_SECTOR_TYPE(String r150_SECTOR_TYPE) {
		R150_SECTOR_TYPE = r150_SECTOR_TYPE;
	}
	public String getR150_FACILITY_TYPE() {
		return R150_FACILITY_TYPE;
	}
	public void setR150_FACILITY_TYPE(String r150_FACILITY_TYPE) {
		R150_FACILITY_TYPE = r150_FACILITY_TYPE;
	}
	public BigDecimal getR150_ORIGINAL_AMOUNT() {
		return R150_ORIGINAL_AMOUNT;
	}
	public void setR150_ORIGINAL_AMOUNT(BigDecimal r150_ORIGINAL_AMOUNT) {
		R150_ORIGINAL_AMOUNT = r150_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR150_UTILISATION_OUTSTANDING_BAL() {
		return R150_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR150_UTILISATION_OUTSTANDING_BAL(BigDecimal r150_UTILISATION_OUTSTANDING_BAL) {
		R150_UTILISATION_OUTSTANDING_BAL = r150_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR150_EFFECTIVE_DATE() {
		return R150_EFFECTIVE_DATE;
	}
	public void setR150_EFFECTIVE_DATE(Date r150_EFFECTIVE_DATE) {
		R150_EFFECTIVE_DATE = r150_EFFECTIVE_DATE;
	}
	public String getR150_REPAYMENT_PERIOD() {
		return R150_REPAYMENT_PERIOD;
	}
	public void setR150_REPAYMENT_PERIOD(String r150_REPAYMENT_PERIOD) {
		R150_REPAYMENT_PERIOD = r150_REPAYMENT_PERIOD;
	}
	public String getR150_PERFORMANCE_STATUS() {
		return R150_PERFORMANCE_STATUS;
	}
	public void setR150_PERFORMANCE_STATUS(String r150_PERFORMANCE_STATUS) {
		R150_PERFORMANCE_STATUS = r150_PERFORMANCE_STATUS;
	}
	public String getR150_SECURITY() {
		return R150_SECURITY;
	}
	public void setR150_SECURITY(String r150_SECURITY) {
		R150_SECURITY = r150_SECURITY;
	}
	public String getR150_BOARD_APPROVAL() {
		return R150_BOARD_APPROVAL;
	}
	public void setR150_BOARD_APPROVAL(String r150_BOARD_APPROVAL) {
		R150_BOARD_APPROVAL = r150_BOARD_APPROVAL;
	}
	public BigDecimal getR150_INTEREST_RATE() {
		return R150_INTEREST_RATE;
	}
	public void setR150_INTEREST_RATE(BigDecimal r150_INTEREST_RATE) {
		R150_INTEREST_RATE = r150_INTEREST_RATE;
	}
	public BigDecimal getR150_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R150_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR150_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r150_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R150_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r150_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR150_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R150_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR150_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r150_LIMIT_PCT_UNIMPAIRED_CAP) {
		R150_LIMIT_PCT_UNIMPAIRED_CAP = r150_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR151_NO_OF_GROUP() {
		return R151_NO_OF_GROUP;
	}
	public void setR151_NO_OF_GROUP(String r151_NO_OF_GROUP) {
		R151_NO_OF_GROUP = r151_NO_OF_GROUP;
	}
	public String getR151_NO_OF_CUSTOMER() {
		return R151_NO_OF_CUSTOMER;
	}
	public void setR151_NO_OF_CUSTOMER(String r151_NO_OF_CUSTOMER) {
		R151_NO_OF_CUSTOMER = r151_NO_OF_CUSTOMER;
	}
	public String getR151_SECTOR_TYPE() {
		return R151_SECTOR_TYPE;
	}
	public void setR151_SECTOR_TYPE(String r151_SECTOR_TYPE) {
		R151_SECTOR_TYPE = r151_SECTOR_TYPE;
	}
	public String getR151_FACILITY_TYPE() {
		return R151_FACILITY_TYPE;
	}
	public void setR151_FACILITY_TYPE(String r151_FACILITY_TYPE) {
		R151_FACILITY_TYPE = r151_FACILITY_TYPE;
	}
	public BigDecimal getR151_ORIGINAL_AMOUNT() {
		return R151_ORIGINAL_AMOUNT;
	}
	public void setR151_ORIGINAL_AMOUNT(BigDecimal r151_ORIGINAL_AMOUNT) {
		R151_ORIGINAL_AMOUNT = r151_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR151_UTILISATION_OUTSTANDING_BAL() {
		return R151_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR151_UTILISATION_OUTSTANDING_BAL(BigDecimal r151_UTILISATION_OUTSTANDING_BAL) {
		R151_UTILISATION_OUTSTANDING_BAL = r151_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR151_EFFECTIVE_DATE() {
		return R151_EFFECTIVE_DATE;
	}
	public void setR151_EFFECTIVE_DATE(Date r151_EFFECTIVE_DATE) {
		R151_EFFECTIVE_DATE = r151_EFFECTIVE_DATE;
	}
	public String getR151_REPAYMENT_PERIOD() {
		return R151_REPAYMENT_PERIOD;
	}
	public void setR151_REPAYMENT_PERIOD(String r151_REPAYMENT_PERIOD) {
		R151_REPAYMENT_PERIOD = r151_REPAYMENT_PERIOD;
	}
	public String getR151_PERFORMANCE_STATUS() {
		return R151_PERFORMANCE_STATUS;
	}
	public void setR151_PERFORMANCE_STATUS(String r151_PERFORMANCE_STATUS) {
		R151_PERFORMANCE_STATUS = r151_PERFORMANCE_STATUS;
	}
	public String getR151_SECURITY() {
		return R151_SECURITY;
	}
	public void setR151_SECURITY(String r151_SECURITY) {
		R151_SECURITY = r151_SECURITY;
	}
	public String getR151_BOARD_APPROVAL() {
		return R151_BOARD_APPROVAL;
	}
	public void setR151_BOARD_APPROVAL(String r151_BOARD_APPROVAL) {
		R151_BOARD_APPROVAL = r151_BOARD_APPROVAL;
	}
	public BigDecimal getR151_INTEREST_RATE() {
		return R151_INTEREST_RATE;
	}
	public void setR151_INTEREST_RATE(BigDecimal r151_INTEREST_RATE) {
		R151_INTEREST_RATE = r151_INTEREST_RATE;
	}
	public BigDecimal getR151_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R151_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR151_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r151_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R151_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r151_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR151_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R151_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR151_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r151_LIMIT_PCT_UNIMPAIRED_CAP) {
		R151_LIMIT_PCT_UNIMPAIRED_CAP = r151_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR152_NO_OF_GROUP() {
		return R152_NO_OF_GROUP;
	}
	public void setR152_NO_OF_GROUP(String r152_NO_OF_GROUP) {
		R152_NO_OF_GROUP = r152_NO_OF_GROUP;
	}
	public String getR152_NO_OF_CUSTOMER() {
		return R152_NO_OF_CUSTOMER;
	}
	public void setR152_NO_OF_CUSTOMER(String r152_NO_OF_CUSTOMER) {
		R152_NO_OF_CUSTOMER = r152_NO_OF_CUSTOMER;
	}
	public String getR152_SECTOR_TYPE() {
		return R152_SECTOR_TYPE;
	}
	public void setR152_SECTOR_TYPE(String r152_SECTOR_TYPE) {
		R152_SECTOR_TYPE = r152_SECTOR_TYPE;
	}
	public String getR152_FACILITY_TYPE() {
		return R152_FACILITY_TYPE;
	}
	public void setR152_FACILITY_TYPE(String r152_FACILITY_TYPE) {
		R152_FACILITY_TYPE = r152_FACILITY_TYPE;
	}
	public BigDecimal getR152_ORIGINAL_AMOUNT() {
		return R152_ORIGINAL_AMOUNT;
	}
	public void setR152_ORIGINAL_AMOUNT(BigDecimal r152_ORIGINAL_AMOUNT) {
		R152_ORIGINAL_AMOUNT = r152_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR152_UTILISATION_OUTSTANDING_BAL() {
		return R152_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR152_UTILISATION_OUTSTANDING_BAL(BigDecimal r152_UTILISATION_OUTSTANDING_BAL) {
		R152_UTILISATION_OUTSTANDING_BAL = r152_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR152_EFFECTIVE_DATE() {
		return R152_EFFECTIVE_DATE;
	}
	public void setR152_EFFECTIVE_DATE(Date r152_EFFECTIVE_DATE) {
		R152_EFFECTIVE_DATE = r152_EFFECTIVE_DATE;
	}
	public String getR152_REPAYMENT_PERIOD() {
		return R152_REPAYMENT_PERIOD;
	}
	public void setR152_REPAYMENT_PERIOD(String r152_REPAYMENT_PERIOD) {
		R152_REPAYMENT_PERIOD = r152_REPAYMENT_PERIOD;
	}
	public String getR152_PERFORMANCE_STATUS() {
		return R152_PERFORMANCE_STATUS;
	}
	public void setR152_PERFORMANCE_STATUS(String r152_PERFORMANCE_STATUS) {
		R152_PERFORMANCE_STATUS = r152_PERFORMANCE_STATUS;
	}
	public String getR152_SECURITY() {
		return R152_SECURITY;
	}
	public void setR152_SECURITY(String r152_SECURITY) {
		R152_SECURITY = r152_SECURITY;
	}
	public String getR152_BOARD_APPROVAL() {
		return R152_BOARD_APPROVAL;
	}
	public void setR152_BOARD_APPROVAL(String r152_BOARD_APPROVAL) {
		R152_BOARD_APPROVAL = r152_BOARD_APPROVAL;
	}
	public BigDecimal getR152_INTEREST_RATE() {
		return R152_INTEREST_RATE;
	}
	public void setR152_INTEREST_RATE(BigDecimal r152_INTEREST_RATE) {
		R152_INTEREST_RATE = r152_INTEREST_RATE;
	}
	public BigDecimal getR152_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R152_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR152_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r152_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R152_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r152_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR152_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R152_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR152_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r152_LIMIT_PCT_UNIMPAIRED_CAP) {
		R152_LIMIT_PCT_UNIMPAIRED_CAP = r152_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR153_NO_OF_GROUP() {
		return R153_NO_OF_GROUP;
	}
	public void setR153_NO_OF_GROUP(String r153_NO_OF_GROUP) {
		R153_NO_OF_GROUP = r153_NO_OF_GROUP;
	}
	public String getR153_NO_OF_CUSTOMER() {
		return R153_NO_OF_CUSTOMER;
	}
	public void setR153_NO_OF_CUSTOMER(String r153_NO_OF_CUSTOMER) {
		R153_NO_OF_CUSTOMER = r153_NO_OF_CUSTOMER;
	}
	public String getR153_SECTOR_TYPE() {
		return R153_SECTOR_TYPE;
	}
	public void setR153_SECTOR_TYPE(String r153_SECTOR_TYPE) {
		R153_SECTOR_TYPE = r153_SECTOR_TYPE;
	}
	public String getR153_FACILITY_TYPE() {
		return R153_FACILITY_TYPE;
	}
	public void setR153_FACILITY_TYPE(String r153_FACILITY_TYPE) {
		R153_FACILITY_TYPE = r153_FACILITY_TYPE;
	}
	public BigDecimal getR153_ORIGINAL_AMOUNT() {
		return R153_ORIGINAL_AMOUNT;
	}
	public void setR153_ORIGINAL_AMOUNT(BigDecimal r153_ORIGINAL_AMOUNT) {
		R153_ORIGINAL_AMOUNT = r153_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR153_UTILISATION_OUTSTANDING_BAL() {
		return R153_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR153_UTILISATION_OUTSTANDING_BAL(BigDecimal r153_UTILISATION_OUTSTANDING_BAL) {
		R153_UTILISATION_OUTSTANDING_BAL = r153_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR153_EFFECTIVE_DATE() {
		return R153_EFFECTIVE_DATE;
	}
	public void setR153_EFFECTIVE_DATE(Date r153_EFFECTIVE_DATE) {
		R153_EFFECTIVE_DATE = r153_EFFECTIVE_DATE;
	}
	public String getR153_REPAYMENT_PERIOD() {
		return R153_REPAYMENT_PERIOD;
	}
	public void setR153_REPAYMENT_PERIOD(String r153_REPAYMENT_PERIOD) {
		R153_REPAYMENT_PERIOD = r153_REPAYMENT_PERIOD;
	}
	public String getR153_PERFORMANCE_STATUS() {
		return R153_PERFORMANCE_STATUS;
	}
	public void setR153_PERFORMANCE_STATUS(String r153_PERFORMANCE_STATUS) {
		R153_PERFORMANCE_STATUS = r153_PERFORMANCE_STATUS;
	}
	public String getR153_SECURITY() {
		return R153_SECURITY;
	}
	public void setR153_SECURITY(String r153_SECURITY) {
		R153_SECURITY = r153_SECURITY;
	}
	public String getR153_BOARD_APPROVAL() {
		return R153_BOARD_APPROVAL;
	}
	public void setR153_BOARD_APPROVAL(String r153_BOARD_APPROVAL) {
		R153_BOARD_APPROVAL = r153_BOARD_APPROVAL;
	}
	public BigDecimal getR153_INTEREST_RATE() {
		return R153_INTEREST_RATE;
	}
	public void setR153_INTEREST_RATE(BigDecimal r153_INTEREST_RATE) {
		R153_INTEREST_RATE = r153_INTEREST_RATE;
	}
	public BigDecimal getR153_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R153_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR153_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r153_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R153_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r153_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR153_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R153_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR153_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r153_LIMIT_PCT_UNIMPAIRED_CAP) {
		R153_LIMIT_PCT_UNIMPAIRED_CAP = r153_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR154_NO_OF_GROUP() {
		return R154_NO_OF_GROUP;
	}
	public void setR154_NO_OF_GROUP(String r154_NO_OF_GROUP) {
		R154_NO_OF_GROUP = r154_NO_OF_GROUP;
	}
	public String getR154_NO_OF_CUSTOMER() {
		return R154_NO_OF_CUSTOMER;
	}
	public void setR154_NO_OF_CUSTOMER(String r154_NO_OF_CUSTOMER) {
		R154_NO_OF_CUSTOMER = r154_NO_OF_CUSTOMER;
	}
	public String getR154_SECTOR_TYPE() {
		return R154_SECTOR_TYPE;
	}
	public void setR154_SECTOR_TYPE(String r154_SECTOR_TYPE) {
		R154_SECTOR_TYPE = r154_SECTOR_TYPE;
	}
	public String getR154_FACILITY_TYPE() {
		return R154_FACILITY_TYPE;
	}
	public void setR154_FACILITY_TYPE(String r154_FACILITY_TYPE) {
		R154_FACILITY_TYPE = r154_FACILITY_TYPE;
	}
	public BigDecimal getR154_ORIGINAL_AMOUNT() {
		return R154_ORIGINAL_AMOUNT;
	}
	public void setR154_ORIGINAL_AMOUNT(BigDecimal r154_ORIGINAL_AMOUNT) {
		R154_ORIGINAL_AMOUNT = r154_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR154_UTILISATION_OUTSTANDING_BAL() {
		return R154_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR154_UTILISATION_OUTSTANDING_BAL(BigDecimal r154_UTILISATION_OUTSTANDING_BAL) {
		R154_UTILISATION_OUTSTANDING_BAL = r154_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR154_EFFECTIVE_DATE() {
		return R154_EFFECTIVE_DATE;
	}
	public void setR154_EFFECTIVE_DATE(Date r154_EFFECTIVE_DATE) {
		R154_EFFECTIVE_DATE = r154_EFFECTIVE_DATE;
	}
	public String getR154_REPAYMENT_PERIOD() {
		return R154_REPAYMENT_PERIOD;
	}
	public void setR154_REPAYMENT_PERIOD(String r154_REPAYMENT_PERIOD) {
		R154_REPAYMENT_PERIOD = r154_REPAYMENT_PERIOD;
	}
	public String getR154_PERFORMANCE_STATUS() {
		return R154_PERFORMANCE_STATUS;
	}
	public void setR154_PERFORMANCE_STATUS(String r154_PERFORMANCE_STATUS) {
		R154_PERFORMANCE_STATUS = r154_PERFORMANCE_STATUS;
	}
	public String getR154_SECURITY() {
		return R154_SECURITY;
	}
	public void setR154_SECURITY(String r154_SECURITY) {
		R154_SECURITY = r154_SECURITY;
	}
	public String getR154_BOARD_APPROVAL() {
		return R154_BOARD_APPROVAL;
	}
	public void setR154_BOARD_APPROVAL(String r154_BOARD_APPROVAL) {
		R154_BOARD_APPROVAL = r154_BOARD_APPROVAL;
	}
	public BigDecimal getR154_INTEREST_RATE() {
		return R154_INTEREST_RATE;
	}
	public void setR154_INTEREST_RATE(BigDecimal r154_INTEREST_RATE) {
		R154_INTEREST_RATE = r154_INTEREST_RATE;
	}
	public BigDecimal getR154_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R154_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR154_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r154_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R154_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r154_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR154_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R154_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR154_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r154_LIMIT_PCT_UNIMPAIRED_CAP) {
		R154_LIMIT_PCT_UNIMPAIRED_CAP = r154_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR155_NO_OF_GROUP() {
		return R155_NO_OF_GROUP;
	}
	public void setR155_NO_OF_GROUP(String r155_NO_OF_GROUP) {
		R155_NO_OF_GROUP = r155_NO_OF_GROUP;
	}
	public String getR155_NO_OF_CUSTOMER() {
		return R155_NO_OF_CUSTOMER;
	}
	public void setR155_NO_OF_CUSTOMER(String r155_NO_OF_CUSTOMER) {
		R155_NO_OF_CUSTOMER = r155_NO_OF_CUSTOMER;
	}
	public String getR155_SECTOR_TYPE() {
		return R155_SECTOR_TYPE;
	}
	public void setR155_SECTOR_TYPE(String r155_SECTOR_TYPE) {
		R155_SECTOR_TYPE = r155_SECTOR_TYPE;
	}
	public String getR155_FACILITY_TYPE() {
		return R155_FACILITY_TYPE;
	}
	public void setR155_FACILITY_TYPE(String r155_FACILITY_TYPE) {
		R155_FACILITY_TYPE = r155_FACILITY_TYPE;
	}
	public BigDecimal getR155_ORIGINAL_AMOUNT() {
		return R155_ORIGINAL_AMOUNT;
	}
	public void setR155_ORIGINAL_AMOUNT(BigDecimal r155_ORIGINAL_AMOUNT) {
		R155_ORIGINAL_AMOUNT = r155_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR155_UTILISATION_OUTSTANDING_BAL() {
		return R155_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR155_UTILISATION_OUTSTANDING_BAL(BigDecimal r155_UTILISATION_OUTSTANDING_BAL) {
		R155_UTILISATION_OUTSTANDING_BAL = r155_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR155_EFFECTIVE_DATE() {
		return R155_EFFECTIVE_DATE;
	}
	public void setR155_EFFECTIVE_DATE(Date r155_EFFECTIVE_DATE) {
		R155_EFFECTIVE_DATE = r155_EFFECTIVE_DATE;
	}
	public String getR155_REPAYMENT_PERIOD() {
		return R155_REPAYMENT_PERIOD;
	}
	public void setR155_REPAYMENT_PERIOD(String r155_REPAYMENT_PERIOD) {
		R155_REPAYMENT_PERIOD = r155_REPAYMENT_PERIOD;
	}
	public String getR155_PERFORMANCE_STATUS() {
		return R155_PERFORMANCE_STATUS;
	}
	public void setR155_PERFORMANCE_STATUS(String r155_PERFORMANCE_STATUS) {
		R155_PERFORMANCE_STATUS = r155_PERFORMANCE_STATUS;
	}
	public String getR155_SECURITY() {
		return R155_SECURITY;
	}
	public void setR155_SECURITY(String r155_SECURITY) {
		R155_SECURITY = r155_SECURITY;
	}
	public String getR155_BOARD_APPROVAL() {
		return R155_BOARD_APPROVAL;
	}
	public void setR155_BOARD_APPROVAL(String r155_BOARD_APPROVAL) {
		R155_BOARD_APPROVAL = r155_BOARD_APPROVAL;
	}
	public BigDecimal getR155_INTEREST_RATE() {
		return R155_INTEREST_RATE;
	}
	public void setR155_INTEREST_RATE(BigDecimal r155_INTEREST_RATE) {
		R155_INTEREST_RATE = r155_INTEREST_RATE;
	}
	public BigDecimal getR155_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R155_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR155_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r155_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R155_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r155_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR155_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R155_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR155_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r155_LIMIT_PCT_UNIMPAIRED_CAP) {
		R155_LIMIT_PCT_UNIMPAIRED_CAP = r155_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR156_NO_OF_GROUP() {
		return R156_NO_OF_GROUP;
	}
	public void setR156_NO_OF_GROUP(String r156_NO_OF_GROUP) {
		R156_NO_OF_GROUP = r156_NO_OF_GROUP;
	}
	public String getR156_NO_OF_CUSTOMER() {
		return R156_NO_OF_CUSTOMER;
	}
	public void setR156_NO_OF_CUSTOMER(String r156_NO_OF_CUSTOMER) {
		R156_NO_OF_CUSTOMER = r156_NO_OF_CUSTOMER;
	}
	public String getR156_SECTOR_TYPE() {
		return R156_SECTOR_TYPE;
	}
	public void setR156_SECTOR_TYPE(String r156_SECTOR_TYPE) {
		R156_SECTOR_TYPE = r156_SECTOR_TYPE;
	}
	public String getR156_FACILITY_TYPE() {
		return R156_FACILITY_TYPE;
	}
	public void setR156_FACILITY_TYPE(String r156_FACILITY_TYPE) {
		R156_FACILITY_TYPE = r156_FACILITY_TYPE;
	}
	public BigDecimal getR156_ORIGINAL_AMOUNT() {
		return R156_ORIGINAL_AMOUNT;
	}
	public void setR156_ORIGINAL_AMOUNT(BigDecimal r156_ORIGINAL_AMOUNT) {
		R156_ORIGINAL_AMOUNT = r156_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR156_UTILISATION_OUTSTANDING_BAL() {
		return R156_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR156_UTILISATION_OUTSTANDING_BAL(BigDecimal r156_UTILISATION_OUTSTANDING_BAL) {
		R156_UTILISATION_OUTSTANDING_BAL = r156_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR156_EFFECTIVE_DATE() {
		return R156_EFFECTIVE_DATE;
	}
	public void setR156_EFFECTIVE_DATE(Date r156_EFFECTIVE_DATE) {
		R156_EFFECTIVE_DATE = r156_EFFECTIVE_DATE;
	}
	public String getR156_REPAYMENT_PERIOD() {
		return R156_REPAYMENT_PERIOD;
	}
	public void setR156_REPAYMENT_PERIOD(String r156_REPAYMENT_PERIOD) {
		R156_REPAYMENT_PERIOD = r156_REPAYMENT_PERIOD;
	}
	public String getR156_PERFORMANCE_STATUS() {
		return R156_PERFORMANCE_STATUS;
	}
	public void setR156_PERFORMANCE_STATUS(String r156_PERFORMANCE_STATUS) {
		R156_PERFORMANCE_STATUS = r156_PERFORMANCE_STATUS;
	}
	public String getR156_SECURITY() {
		return R156_SECURITY;
	}
	public void setR156_SECURITY(String r156_SECURITY) {
		R156_SECURITY = r156_SECURITY;
	}
	public String getR156_BOARD_APPROVAL() {
		return R156_BOARD_APPROVAL;
	}
	public void setR156_BOARD_APPROVAL(String r156_BOARD_APPROVAL) {
		R156_BOARD_APPROVAL = r156_BOARD_APPROVAL;
	}
	public BigDecimal getR156_INTEREST_RATE() {
		return R156_INTEREST_RATE;
	}
	public void setR156_INTEREST_RATE(BigDecimal r156_INTEREST_RATE) {
		R156_INTEREST_RATE = r156_INTEREST_RATE;
	}
	public BigDecimal getR156_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R156_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR156_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r156_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R156_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r156_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR156_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R156_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR156_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r156_LIMIT_PCT_UNIMPAIRED_CAP) {
		R156_LIMIT_PCT_UNIMPAIRED_CAP = r156_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR157_NO_OF_GROUP() {
		return R157_NO_OF_GROUP;
	}
	public void setR157_NO_OF_GROUP(String r157_NO_OF_GROUP) {
		R157_NO_OF_GROUP = r157_NO_OF_GROUP;
	}
	public String getR157_NO_OF_CUSTOMER() {
		return R157_NO_OF_CUSTOMER;
	}
	public void setR157_NO_OF_CUSTOMER(String r157_NO_OF_CUSTOMER) {
		R157_NO_OF_CUSTOMER = r157_NO_OF_CUSTOMER;
	}
	public String getR157_SECTOR_TYPE() {
		return R157_SECTOR_TYPE;
	}
	public void setR157_SECTOR_TYPE(String r157_SECTOR_TYPE) {
		R157_SECTOR_TYPE = r157_SECTOR_TYPE;
	}
	public String getR157_FACILITY_TYPE() {
		return R157_FACILITY_TYPE;
	}
	public void setR157_FACILITY_TYPE(String r157_FACILITY_TYPE) {
		R157_FACILITY_TYPE = r157_FACILITY_TYPE;
	}
	public BigDecimal getR157_ORIGINAL_AMOUNT() {
		return R157_ORIGINAL_AMOUNT;
	}
	public void setR157_ORIGINAL_AMOUNT(BigDecimal r157_ORIGINAL_AMOUNT) {
		R157_ORIGINAL_AMOUNT = r157_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR157_UTILISATION_OUTSTANDING_BAL() {
		return R157_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR157_UTILISATION_OUTSTANDING_BAL(BigDecimal r157_UTILISATION_OUTSTANDING_BAL) {
		R157_UTILISATION_OUTSTANDING_BAL = r157_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR157_EFFECTIVE_DATE() {
		return R157_EFFECTIVE_DATE;
	}
	public void setR157_EFFECTIVE_DATE(Date r157_EFFECTIVE_DATE) {
		R157_EFFECTIVE_DATE = r157_EFFECTIVE_DATE;
	}
	public String getR157_REPAYMENT_PERIOD() {
		return R157_REPAYMENT_PERIOD;
	}
	public void setR157_REPAYMENT_PERIOD(String r157_REPAYMENT_PERIOD) {
		R157_REPAYMENT_PERIOD = r157_REPAYMENT_PERIOD;
	}
	public String getR157_PERFORMANCE_STATUS() {
		return R157_PERFORMANCE_STATUS;
	}
	public void setR157_PERFORMANCE_STATUS(String r157_PERFORMANCE_STATUS) {
		R157_PERFORMANCE_STATUS = r157_PERFORMANCE_STATUS;
	}
	public String getR157_SECURITY() {
		return R157_SECURITY;
	}
	public void setR157_SECURITY(String r157_SECURITY) {
		R157_SECURITY = r157_SECURITY;
	}
	public String getR157_BOARD_APPROVAL() {
		return R157_BOARD_APPROVAL;
	}
	public void setR157_BOARD_APPROVAL(String r157_BOARD_APPROVAL) {
		R157_BOARD_APPROVAL = r157_BOARD_APPROVAL;
	}
	public BigDecimal getR157_INTEREST_RATE() {
		return R157_INTEREST_RATE;
	}
	public void setR157_INTEREST_RATE(BigDecimal r157_INTEREST_RATE) {
		R157_INTEREST_RATE = r157_INTEREST_RATE;
	}
	public BigDecimal getR157_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R157_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR157_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r157_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R157_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r157_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR157_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R157_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR157_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r157_LIMIT_PCT_UNIMPAIRED_CAP) {
		R157_LIMIT_PCT_UNIMPAIRED_CAP = r157_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR158_NO_OF_GROUP() {
		return R158_NO_OF_GROUP;
	}
	public void setR158_NO_OF_GROUP(String r158_NO_OF_GROUP) {
		R158_NO_OF_GROUP = r158_NO_OF_GROUP;
	}
	public String getR158_NO_OF_CUSTOMER() {
		return R158_NO_OF_CUSTOMER;
	}
	public void setR158_NO_OF_CUSTOMER(String r158_NO_OF_CUSTOMER) {
		R158_NO_OF_CUSTOMER = r158_NO_OF_CUSTOMER;
	}
	public String getR158_SECTOR_TYPE() {
		return R158_SECTOR_TYPE;
	}
	public void setR158_SECTOR_TYPE(String r158_SECTOR_TYPE) {
		R158_SECTOR_TYPE = r158_SECTOR_TYPE;
	}
	public String getR158_FACILITY_TYPE() {
		return R158_FACILITY_TYPE;
	}
	public void setR158_FACILITY_TYPE(String r158_FACILITY_TYPE) {
		R158_FACILITY_TYPE = r158_FACILITY_TYPE;
	}
	public BigDecimal getR158_ORIGINAL_AMOUNT() {
		return R158_ORIGINAL_AMOUNT;
	}
	public void setR158_ORIGINAL_AMOUNT(BigDecimal r158_ORIGINAL_AMOUNT) {
		R158_ORIGINAL_AMOUNT = r158_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR158_UTILISATION_OUTSTANDING_BAL() {
		return R158_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR158_UTILISATION_OUTSTANDING_BAL(BigDecimal r158_UTILISATION_OUTSTANDING_BAL) {
		R158_UTILISATION_OUTSTANDING_BAL = r158_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR158_EFFECTIVE_DATE() {
		return R158_EFFECTIVE_DATE;
	}
	public void setR158_EFFECTIVE_DATE(Date r158_EFFECTIVE_DATE) {
		R158_EFFECTIVE_DATE = r158_EFFECTIVE_DATE;
	}
	public String getR158_REPAYMENT_PERIOD() {
		return R158_REPAYMENT_PERIOD;
	}
	public void setR158_REPAYMENT_PERIOD(String r158_REPAYMENT_PERIOD) {
		R158_REPAYMENT_PERIOD = r158_REPAYMENT_PERIOD;
	}
	public String getR158_PERFORMANCE_STATUS() {
		return R158_PERFORMANCE_STATUS;
	}
	public void setR158_PERFORMANCE_STATUS(String r158_PERFORMANCE_STATUS) {
		R158_PERFORMANCE_STATUS = r158_PERFORMANCE_STATUS;
	}
	public String getR158_SECURITY() {
		return R158_SECURITY;
	}
	public void setR158_SECURITY(String r158_SECURITY) {
		R158_SECURITY = r158_SECURITY;
	}
	public String getR158_BOARD_APPROVAL() {
		return R158_BOARD_APPROVAL;
	}
	public void setR158_BOARD_APPROVAL(String r158_BOARD_APPROVAL) {
		R158_BOARD_APPROVAL = r158_BOARD_APPROVAL;
	}
	public BigDecimal getR158_INTEREST_RATE() {
		return R158_INTEREST_RATE;
	}
	public void setR158_INTEREST_RATE(BigDecimal r158_INTEREST_RATE) {
		R158_INTEREST_RATE = r158_INTEREST_RATE;
	}
	public BigDecimal getR158_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R158_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR158_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r158_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R158_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r158_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR158_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R158_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR158_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r158_LIMIT_PCT_UNIMPAIRED_CAP) {
		R158_LIMIT_PCT_UNIMPAIRED_CAP = r158_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR159_NO_OF_GROUP() {
		return R159_NO_OF_GROUP;
	}
	public void setR159_NO_OF_GROUP(String r159_NO_OF_GROUP) {
		R159_NO_OF_GROUP = r159_NO_OF_GROUP;
	}
	public String getR159_NO_OF_CUSTOMER() {
		return R159_NO_OF_CUSTOMER;
	}
	public void setR159_NO_OF_CUSTOMER(String r159_NO_OF_CUSTOMER) {
		R159_NO_OF_CUSTOMER = r159_NO_OF_CUSTOMER;
	}
	public String getR159_SECTOR_TYPE() {
		return R159_SECTOR_TYPE;
	}
	public void setR159_SECTOR_TYPE(String r159_SECTOR_TYPE) {
		R159_SECTOR_TYPE = r159_SECTOR_TYPE;
	}
	public String getR159_FACILITY_TYPE() {
		return R159_FACILITY_TYPE;
	}
	public void setR159_FACILITY_TYPE(String r159_FACILITY_TYPE) {
		R159_FACILITY_TYPE = r159_FACILITY_TYPE;
	}
	public BigDecimal getR159_ORIGINAL_AMOUNT() {
		return R159_ORIGINAL_AMOUNT;
	}
	public void setR159_ORIGINAL_AMOUNT(BigDecimal r159_ORIGINAL_AMOUNT) {
		R159_ORIGINAL_AMOUNT = r159_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR159_UTILISATION_OUTSTANDING_BAL() {
		return R159_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR159_UTILISATION_OUTSTANDING_BAL(BigDecimal r159_UTILISATION_OUTSTANDING_BAL) {
		R159_UTILISATION_OUTSTANDING_BAL = r159_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR159_EFFECTIVE_DATE() {
		return R159_EFFECTIVE_DATE;
	}
	public void setR159_EFFECTIVE_DATE(Date r159_EFFECTIVE_DATE) {
		R159_EFFECTIVE_DATE = r159_EFFECTIVE_DATE;
	}
	public String getR159_REPAYMENT_PERIOD() {
		return R159_REPAYMENT_PERIOD;
	}
	public void setR159_REPAYMENT_PERIOD(String r159_REPAYMENT_PERIOD) {
		R159_REPAYMENT_PERIOD = r159_REPAYMENT_PERIOD;
	}
	public String getR159_PERFORMANCE_STATUS() {
		return R159_PERFORMANCE_STATUS;
	}
	public void setR159_PERFORMANCE_STATUS(String r159_PERFORMANCE_STATUS) {
		R159_PERFORMANCE_STATUS = r159_PERFORMANCE_STATUS;
	}
	public String getR159_SECURITY() {
		return R159_SECURITY;
	}
	public void setR159_SECURITY(String r159_SECURITY) {
		R159_SECURITY = r159_SECURITY;
	}
	public String getR159_BOARD_APPROVAL() {
		return R159_BOARD_APPROVAL;
	}
	public void setR159_BOARD_APPROVAL(String r159_BOARD_APPROVAL) {
		R159_BOARD_APPROVAL = r159_BOARD_APPROVAL;
	}
	public BigDecimal getR159_INTEREST_RATE() {
		return R159_INTEREST_RATE;
	}
	public void setR159_INTEREST_RATE(BigDecimal r159_INTEREST_RATE) {
		R159_INTEREST_RATE = r159_INTEREST_RATE;
	}
	public BigDecimal getR159_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R159_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR159_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r159_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R159_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r159_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR159_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R159_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR159_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r159_LIMIT_PCT_UNIMPAIRED_CAP) {
		R159_LIMIT_PCT_UNIMPAIRED_CAP = r159_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR160_NO_OF_GROUP() {
		return R160_NO_OF_GROUP;
	}
	public void setR160_NO_OF_GROUP(String r160_NO_OF_GROUP) {
		R160_NO_OF_GROUP = r160_NO_OF_GROUP;
	}
	public String getR160_NO_OF_CUSTOMER() {
		return R160_NO_OF_CUSTOMER;
	}
	public void setR160_NO_OF_CUSTOMER(String r160_NO_OF_CUSTOMER) {
		R160_NO_OF_CUSTOMER = r160_NO_OF_CUSTOMER;
	}
	public String getR160_SECTOR_TYPE() {
		return R160_SECTOR_TYPE;
	}
	public void setR160_SECTOR_TYPE(String r160_SECTOR_TYPE) {
		R160_SECTOR_TYPE = r160_SECTOR_TYPE;
	}
	public String getR160_FACILITY_TYPE() {
		return R160_FACILITY_TYPE;
	}
	public void setR160_FACILITY_TYPE(String r160_FACILITY_TYPE) {
		R160_FACILITY_TYPE = r160_FACILITY_TYPE;
	}
	public BigDecimal getR160_ORIGINAL_AMOUNT() {
		return R160_ORIGINAL_AMOUNT;
	}
	public void setR160_ORIGINAL_AMOUNT(BigDecimal r160_ORIGINAL_AMOUNT) {
		R160_ORIGINAL_AMOUNT = r160_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR160_UTILISATION_OUTSTANDING_BAL() {
		return R160_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR160_UTILISATION_OUTSTANDING_BAL(BigDecimal r160_UTILISATION_OUTSTANDING_BAL) {
		R160_UTILISATION_OUTSTANDING_BAL = r160_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR160_EFFECTIVE_DATE() {
		return R160_EFFECTIVE_DATE;
	}
	public void setR160_EFFECTIVE_DATE(Date r160_EFFECTIVE_DATE) {
		R160_EFFECTIVE_DATE = r160_EFFECTIVE_DATE;
	}
	public String getR160_REPAYMENT_PERIOD() {
		return R160_REPAYMENT_PERIOD;
	}
	public void setR160_REPAYMENT_PERIOD(String r160_REPAYMENT_PERIOD) {
		R160_REPAYMENT_PERIOD = r160_REPAYMENT_PERIOD;
	}
	public String getR160_PERFORMANCE_STATUS() {
		return R160_PERFORMANCE_STATUS;
	}
	public void setR160_PERFORMANCE_STATUS(String r160_PERFORMANCE_STATUS) {
		R160_PERFORMANCE_STATUS = r160_PERFORMANCE_STATUS;
	}
	public String getR160_SECURITY() {
		return R160_SECURITY;
	}
	public void setR160_SECURITY(String r160_SECURITY) {
		R160_SECURITY = r160_SECURITY;
	}
	public String getR160_BOARD_APPROVAL() {
		return R160_BOARD_APPROVAL;
	}
	public void setR160_BOARD_APPROVAL(String r160_BOARD_APPROVAL) {
		R160_BOARD_APPROVAL = r160_BOARD_APPROVAL;
	}
	public BigDecimal getR160_INTEREST_RATE() {
		return R160_INTEREST_RATE;
	}
	public void setR160_INTEREST_RATE(BigDecimal r160_INTEREST_RATE) {
		R160_INTEREST_RATE = r160_INTEREST_RATE;
	}
	public BigDecimal getR160_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R160_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR160_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r160_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R160_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r160_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR160_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R160_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR160_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r160_LIMIT_PCT_UNIMPAIRED_CAP) {
		R160_LIMIT_PCT_UNIMPAIRED_CAP = r160_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR161_NO_OF_GROUP() {
		return R161_NO_OF_GROUP;
	}
	public void setR161_NO_OF_GROUP(String r161_NO_OF_GROUP) {
		R161_NO_OF_GROUP = r161_NO_OF_GROUP;
	}
	public String getR161_NO_OF_CUSTOMER() {
		return R161_NO_OF_CUSTOMER;
	}
	public void setR161_NO_OF_CUSTOMER(String r161_NO_OF_CUSTOMER) {
		R161_NO_OF_CUSTOMER = r161_NO_OF_CUSTOMER;
	}
	public String getR161_SECTOR_TYPE() {
		return R161_SECTOR_TYPE;
	}
	public void setR161_SECTOR_TYPE(String r161_SECTOR_TYPE) {
		R161_SECTOR_TYPE = r161_SECTOR_TYPE;
	}
	public String getR161_FACILITY_TYPE() {
		return R161_FACILITY_TYPE;
	}
	public void setR161_FACILITY_TYPE(String r161_FACILITY_TYPE) {
		R161_FACILITY_TYPE = r161_FACILITY_TYPE;
	}
	public BigDecimal getR161_ORIGINAL_AMOUNT() {
		return R161_ORIGINAL_AMOUNT;
	}
	public void setR161_ORIGINAL_AMOUNT(BigDecimal r161_ORIGINAL_AMOUNT) {
		R161_ORIGINAL_AMOUNT = r161_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR161_UTILISATION_OUTSTANDING_BAL() {
		return R161_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR161_UTILISATION_OUTSTANDING_BAL(BigDecimal r161_UTILISATION_OUTSTANDING_BAL) {
		R161_UTILISATION_OUTSTANDING_BAL = r161_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR161_EFFECTIVE_DATE() {
		return R161_EFFECTIVE_DATE;
	}
	public void setR161_EFFECTIVE_DATE(Date r161_EFFECTIVE_DATE) {
		R161_EFFECTIVE_DATE = r161_EFFECTIVE_DATE;
	}
	public String getR161_REPAYMENT_PERIOD() {
		return R161_REPAYMENT_PERIOD;
	}
	public void setR161_REPAYMENT_PERIOD(String r161_REPAYMENT_PERIOD) {
		R161_REPAYMENT_PERIOD = r161_REPAYMENT_PERIOD;
	}
	public String getR161_PERFORMANCE_STATUS() {
		return R161_PERFORMANCE_STATUS;
	}
	public void setR161_PERFORMANCE_STATUS(String r161_PERFORMANCE_STATUS) {
		R161_PERFORMANCE_STATUS = r161_PERFORMANCE_STATUS;
	}
	public String getR161_SECURITY() {
		return R161_SECURITY;
	}
	public void setR161_SECURITY(String r161_SECURITY) {
		R161_SECURITY = r161_SECURITY;
	}
	public String getR161_BOARD_APPROVAL() {
		return R161_BOARD_APPROVAL;
	}
	public void setR161_BOARD_APPROVAL(String r161_BOARD_APPROVAL) {
		R161_BOARD_APPROVAL = r161_BOARD_APPROVAL;
	}
	public BigDecimal getR161_INTEREST_RATE() {
		return R161_INTEREST_RATE;
	}
	public void setR161_INTEREST_RATE(BigDecimal r161_INTEREST_RATE) {
		R161_INTEREST_RATE = r161_INTEREST_RATE;
	}
	public BigDecimal getR161_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R161_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR161_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r161_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R161_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r161_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR161_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R161_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR161_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r161_LIMIT_PCT_UNIMPAIRED_CAP) {
		R161_LIMIT_PCT_UNIMPAIRED_CAP = r161_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR162_NO_OF_GROUP() {
		return R162_NO_OF_GROUP;
	}
	public void setR162_NO_OF_GROUP(String r162_NO_OF_GROUP) {
		R162_NO_OF_GROUP = r162_NO_OF_GROUP;
	}
	public String getR162_NO_OF_CUSTOMER() {
		return R162_NO_OF_CUSTOMER;
	}
	public void setR162_NO_OF_CUSTOMER(String r162_NO_OF_CUSTOMER) {
		R162_NO_OF_CUSTOMER = r162_NO_OF_CUSTOMER;
	}
	public String getR162_SECTOR_TYPE() {
		return R162_SECTOR_TYPE;
	}
	public void setR162_SECTOR_TYPE(String r162_SECTOR_TYPE) {
		R162_SECTOR_TYPE = r162_SECTOR_TYPE;
	}
	public String getR162_FACILITY_TYPE() {
		return R162_FACILITY_TYPE;
	}
	public void setR162_FACILITY_TYPE(String r162_FACILITY_TYPE) {
		R162_FACILITY_TYPE = r162_FACILITY_TYPE;
	}
	public BigDecimal getR162_ORIGINAL_AMOUNT() {
		return R162_ORIGINAL_AMOUNT;
	}
	public void setR162_ORIGINAL_AMOUNT(BigDecimal r162_ORIGINAL_AMOUNT) {
		R162_ORIGINAL_AMOUNT = r162_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR162_UTILISATION_OUTSTANDING_BAL() {
		return R162_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR162_UTILISATION_OUTSTANDING_BAL(BigDecimal r162_UTILISATION_OUTSTANDING_BAL) {
		R162_UTILISATION_OUTSTANDING_BAL = r162_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR162_EFFECTIVE_DATE() {
		return R162_EFFECTIVE_DATE;
	}
	public void setR162_EFFECTIVE_DATE(Date r162_EFFECTIVE_DATE) {
		R162_EFFECTIVE_DATE = r162_EFFECTIVE_DATE;
	}
	public String getR162_REPAYMENT_PERIOD() {
		return R162_REPAYMENT_PERIOD;
	}
	public void setR162_REPAYMENT_PERIOD(String r162_REPAYMENT_PERIOD) {
		R162_REPAYMENT_PERIOD = r162_REPAYMENT_PERIOD;
	}
	public String getR162_PERFORMANCE_STATUS() {
		return R162_PERFORMANCE_STATUS;
	}
	public void setR162_PERFORMANCE_STATUS(String r162_PERFORMANCE_STATUS) {
		R162_PERFORMANCE_STATUS = r162_PERFORMANCE_STATUS;
	}
	public String getR162_SECURITY() {
		return R162_SECURITY;
	}
	public void setR162_SECURITY(String r162_SECURITY) {
		R162_SECURITY = r162_SECURITY;
	}
	public String getR162_BOARD_APPROVAL() {
		return R162_BOARD_APPROVAL;
	}
	public void setR162_BOARD_APPROVAL(String r162_BOARD_APPROVAL) {
		R162_BOARD_APPROVAL = r162_BOARD_APPROVAL;
	}
	public BigDecimal getR162_INTEREST_RATE() {
		return R162_INTEREST_RATE;
	}
	public void setR162_INTEREST_RATE(BigDecimal r162_INTEREST_RATE) {
		R162_INTEREST_RATE = r162_INTEREST_RATE;
	}
	public BigDecimal getR162_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R162_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR162_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r162_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R162_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r162_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR162_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R162_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR162_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r162_LIMIT_PCT_UNIMPAIRED_CAP) {
		R162_LIMIT_PCT_UNIMPAIRED_CAP = r162_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR163_NO_OF_GROUP() {
		return R163_NO_OF_GROUP;
	}
	public void setR163_NO_OF_GROUP(String r163_NO_OF_GROUP) {
		R163_NO_OF_GROUP = r163_NO_OF_GROUP;
	}
	public String getR163_NO_OF_CUSTOMER() {
		return R163_NO_OF_CUSTOMER;
	}
	public void setR163_NO_OF_CUSTOMER(String r163_NO_OF_CUSTOMER) {
		R163_NO_OF_CUSTOMER = r163_NO_OF_CUSTOMER;
	}
	public String getR163_SECTOR_TYPE() {
		return R163_SECTOR_TYPE;
	}
	public void setR163_SECTOR_TYPE(String r163_SECTOR_TYPE) {
		R163_SECTOR_TYPE = r163_SECTOR_TYPE;
	}
	public String getR163_FACILITY_TYPE() {
		return R163_FACILITY_TYPE;
	}
	public void setR163_FACILITY_TYPE(String r163_FACILITY_TYPE) {
		R163_FACILITY_TYPE = r163_FACILITY_TYPE;
	}
	public BigDecimal getR163_ORIGINAL_AMOUNT() {
		return R163_ORIGINAL_AMOUNT;
	}
	public void setR163_ORIGINAL_AMOUNT(BigDecimal r163_ORIGINAL_AMOUNT) {
		R163_ORIGINAL_AMOUNT = r163_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR163_UTILISATION_OUTSTANDING_BAL() {
		return R163_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR163_UTILISATION_OUTSTANDING_BAL(BigDecimal r163_UTILISATION_OUTSTANDING_BAL) {
		R163_UTILISATION_OUTSTANDING_BAL = r163_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR163_EFFECTIVE_DATE() {
		return R163_EFFECTIVE_DATE;
	}
	public void setR163_EFFECTIVE_DATE(Date r163_EFFECTIVE_DATE) {
		R163_EFFECTIVE_DATE = r163_EFFECTIVE_DATE;
	}
	public String getR163_REPAYMENT_PERIOD() {
		return R163_REPAYMENT_PERIOD;
	}
	public void setR163_REPAYMENT_PERIOD(String r163_REPAYMENT_PERIOD) {
		R163_REPAYMENT_PERIOD = r163_REPAYMENT_PERIOD;
	}
	public String getR163_PERFORMANCE_STATUS() {
		return R163_PERFORMANCE_STATUS;
	}
	public void setR163_PERFORMANCE_STATUS(String r163_PERFORMANCE_STATUS) {
		R163_PERFORMANCE_STATUS = r163_PERFORMANCE_STATUS;
	}
	public String getR163_SECURITY() {
		return R163_SECURITY;
	}
	public void setR163_SECURITY(String r163_SECURITY) {
		R163_SECURITY = r163_SECURITY;
	}
	public String getR163_BOARD_APPROVAL() {
		return R163_BOARD_APPROVAL;
	}
	public void setR163_BOARD_APPROVAL(String r163_BOARD_APPROVAL) {
		R163_BOARD_APPROVAL = r163_BOARD_APPROVAL;
	}
	public BigDecimal getR163_INTEREST_RATE() {
		return R163_INTEREST_RATE;
	}
	public void setR163_INTEREST_RATE(BigDecimal r163_INTEREST_RATE) {
		R163_INTEREST_RATE = r163_INTEREST_RATE;
	}
	public BigDecimal getR163_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R163_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR163_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r163_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R163_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r163_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR163_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R163_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR163_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r163_LIMIT_PCT_UNIMPAIRED_CAP) {
		R163_LIMIT_PCT_UNIMPAIRED_CAP = r163_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR164_NO_OF_GROUP() {
		return R164_NO_OF_GROUP;
	}
	public void setR164_NO_OF_GROUP(String r164_NO_OF_GROUP) {
		R164_NO_OF_GROUP = r164_NO_OF_GROUP;
	}
	public String getR164_NO_OF_CUSTOMER() {
		return R164_NO_OF_CUSTOMER;
	}
	public void setR164_NO_OF_CUSTOMER(String r164_NO_OF_CUSTOMER) {
		R164_NO_OF_CUSTOMER = r164_NO_OF_CUSTOMER;
	}
	public String getR164_SECTOR_TYPE() {
		return R164_SECTOR_TYPE;
	}
	public void setR164_SECTOR_TYPE(String r164_SECTOR_TYPE) {
		R164_SECTOR_TYPE = r164_SECTOR_TYPE;
	}
	public String getR164_FACILITY_TYPE() {
		return R164_FACILITY_TYPE;
	}
	public void setR164_FACILITY_TYPE(String r164_FACILITY_TYPE) {
		R164_FACILITY_TYPE = r164_FACILITY_TYPE;
	}
	public BigDecimal getR164_ORIGINAL_AMOUNT() {
		return R164_ORIGINAL_AMOUNT;
	}
	public void setR164_ORIGINAL_AMOUNT(BigDecimal r164_ORIGINAL_AMOUNT) {
		R164_ORIGINAL_AMOUNT = r164_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR164_UTILISATION_OUTSTANDING_BAL() {
		return R164_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR164_UTILISATION_OUTSTANDING_BAL(BigDecimal r164_UTILISATION_OUTSTANDING_BAL) {
		R164_UTILISATION_OUTSTANDING_BAL = r164_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR164_EFFECTIVE_DATE() {
		return R164_EFFECTIVE_DATE;
	}
	public void setR164_EFFECTIVE_DATE(Date r164_EFFECTIVE_DATE) {
		R164_EFFECTIVE_DATE = r164_EFFECTIVE_DATE;
	}
	public String getR164_REPAYMENT_PERIOD() {
		return R164_REPAYMENT_PERIOD;
	}
	public void setR164_REPAYMENT_PERIOD(String r164_REPAYMENT_PERIOD) {
		R164_REPAYMENT_PERIOD = r164_REPAYMENT_PERIOD;
	}
	public String getR164_PERFORMANCE_STATUS() {
		return R164_PERFORMANCE_STATUS;
	}
	public void setR164_PERFORMANCE_STATUS(String r164_PERFORMANCE_STATUS) {
		R164_PERFORMANCE_STATUS = r164_PERFORMANCE_STATUS;
	}
	public String getR164_SECURITY() {
		return R164_SECURITY;
	}
	public void setR164_SECURITY(String r164_SECURITY) {
		R164_SECURITY = r164_SECURITY;
	}
	public String getR164_BOARD_APPROVAL() {
		return R164_BOARD_APPROVAL;
	}
	public void setR164_BOARD_APPROVAL(String r164_BOARD_APPROVAL) {
		R164_BOARD_APPROVAL = r164_BOARD_APPROVAL;
	}
	public BigDecimal getR164_INTEREST_RATE() {
		return R164_INTEREST_RATE;
	}
	public void setR164_INTEREST_RATE(BigDecimal r164_INTEREST_RATE) {
		R164_INTEREST_RATE = r164_INTEREST_RATE;
	}
	public BigDecimal getR164_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R164_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR164_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r164_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R164_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r164_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR164_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R164_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR164_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r164_LIMIT_PCT_UNIMPAIRED_CAP) {
		R164_LIMIT_PCT_UNIMPAIRED_CAP = r164_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR165_NO_OF_GROUP() {
		return R165_NO_OF_GROUP;
	}
	public void setR165_NO_OF_GROUP(String r165_NO_OF_GROUP) {
		R165_NO_OF_GROUP = r165_NO_OF_GROUP;
	}
	public String getR165_NO_OF_CUSTOMER() {
		return R165_NO_OF_CUSTOMER;
	}
	public void setR165_NO_OF_CUSTOMER(String r165_NO_OF_CUSTOMER) {
		R165_NO_OF_CUSTOMER = r165_NO_OF_CUSTOMER;
	}
	public String getR165_SECTOR_TYPE() {
		return R165_SECTOR_TYPE;
	}
	public void setR165_SECTOR_TYPE(String r165_SECTOR_TYPE) {
		R165_SECTOR_TYPE = r165_SECTOR_TYPE;
	}
	public String getR165_FACILITY_TYPE() {
		return R165_FACILITY_TYPE;
	}
	public void setR165_FACILITY_TYPE(String r165_FACILITY_TYPE) {
		R165_FACILITY_TYPE = r165_FACILITY_TYPE;
	}
	public BigDecimal getR165_ORIGINAL_AMOUNT() {
		return R165_ORIGINAL_AMOUNT;
	}
	public void setR165_ORIGINAL_AMOUNT(BigDecimal r165_ORIGINAL_AMOUNT) {
		R165_ORIGINAL_AMOUNT = r165_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR165_UTILISATION_OUTSTANDING_BAL() {
		return R165_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR165_UTILISATION_OUTSTANDING_BAL(BigDecimal r165_UTILISATION_OUTSTANDING_BAL) {
		R165_UTILISATION_OUTSTANDING_BAL = r165_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR165_EFFECTIVE_DATE() {
		return R165_EFFECTIVE_DATE;
	}
	public void setR165_EFFECTIVE_DATE(Date r165_EFFECTIVE_DATE) {
		R165_EFFECTIVE_DATE = r165_EFFECTIVE_DATE;
	}
	public String getR165_REPAYMENT_PERIOD() {
		return R165_REPAYMENT_PERIOD;
	}
	public void setR165_REPAYMENT_PERIOD(String r165_REPAYMENT_PERIOD) {
		R165_REPAYMENT_PERIOD = r165_REPAYMENT_PERIOD;
	}
	public String getR165_PERFORMANCE_STATUS() {
		return R165_PERFORMANCE_STATUS;
	}
	public void setR165_PERFORMANCE_STATUS(String r165_PERFORMANCE_STATUS) {
		R165_PERFORMANCE_STATUS = r165_PERFORMANCE_STATUS;
	}
	public String getR165_SECURITY() {
		return R165_SECURITY;
	}
	public void setR165_SECURITY(String r165_SECURITY) {
		R165_SECURITY = r165_SECURITY;
	}
	public String getR165_BOARD_APPROVAL() {
		return R165_BOARD_APPROVAL;
	}
	public void setR165_BOARD_APPROVAL(String r165_BOARD_APPROVAL) {
		R165_BOARD_APPROVAL = r165_BOARD_APPROVAL;
	}
	public BigDecimal getR165_INTEREST_RATE() {
		return R165_INTEREST_RATE;
	}
	public void setR165_INTEREST_RATE(BigDecimal r165_INTEREST_RATE) {
		R165_INTEREST_RATE = r165_INTEREST_RATE;
	}
	public BigDecimal getR165_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R165_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR165_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r165_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R165_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r165_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR165_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R165_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR165_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r165_LIMIT_PCT_UNIMPAIRED_CAP) {
		R165_LIMIT_PCT_UNIMPAIRED_CAP = r165_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR166_NO_OF_GROUP() {
		return R166_NO_OF_GROUP;
	}
	public void setR166_NO_OF_GROUP(String r166_NO_OF_GROUP) {
		R166_NO_OF_GROUP = r166_NO_OF_GROUP;
	}
	public String getR166_NO_OF_CUSTOMER() {
		return R166_NO_OF_CUSTOMER;
	}
	public void setR166_NO_OF_CUSTOMER(String r166_NO_OF_CUSTOMER) {
		R166_NO_OF_CUSTOMER = r166_NO_OF_CUSTOMER;
	}
	public String getR166_SECTOR_TYPE() {
		return R166_SECTOR_TYPE;
	}
	public void setR166_SECTOR_TYPE(String r166_SECTOR_TYPE) {
		R166_SECTOR_TYPE = r166_SECTOR_TYPE;
	}
	public String getR166_FACILITY_TYPE() {
		return R166_FACILITY_TYPE;
	}
	public void setR166_FACILITY_TYPE(String r166_FACILITY_TYPE) {
		R166_FACILITY_TYPE = r166_FACILITY_TYPE;
	}
	public BigDecimal getR166_ORIGINAL_AMOUNT() {
		return R166_ORIGINAL_AMOUNT;
	}
	public void setR166_ORIGINAL_AMOUNT(BigDecimal r166_ORIGINAL_AMOUNT) {
		R166_ORIGINAL_AMOUNT = r166_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR166_UTILISATION_OUTSTANDING_BAL() {
		return R166_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR166_UTILISATION_OUTSTANDING_BAL(BigDecimal r166_UTILISATION_OUTSTANDING_BAL) {
		R166_UTILISATION_OUTSTANDING_BAL = r166_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR166_EFFECTIVE_DATE() {
		return R166_EFFECTIVE_DATE;
	}
	public void setR166_EFFECTIVE_DATE(Date r166_EFFECTIVE_DATE) {
		R166_EFFECTIVE_DATE = r166_EFFECTIVE_DATE;
	}
	public String getR166_REPAYMENT_PERIOD() {
		return R166_REPAYMENT_PERIOD;
	}
	public void setR166_REPAYMENT_PERIOD(String r166_REPAYMENT_PERIOD) {
		R166_REPAYMENT_PERIOD = r166_REPAYMENT_PERIOD;
	}
	public String getR166_PERFORMANCE_STATUS() {
		return R166_PERFORMANCE_STATUS;
	}
	public void setR166_PERFORMANCE_STATUS(String r166_PERFORMANCE_STATUS) {
		R166_PERFORMANCE_STATUS = r166_PERFORMANCE_STATUS;
	}
	public String getR166_SECURITY() {
		return R166_SECURITY;
	}
	public void setR166_SECURITY(String r166_SECURITY) {
		R166_SECURITY = r166_SECURITY;
	}
	public String getR166_BOARD_APPROVAL() {
		return R166_BOARD_APPROVAL;
	}
	public void setR166_BOARD_APPROVAL(String r166_BOARD_APPROVAL) {
		R166_BOARD_APPROVAL = r166_BOARD_APPROVAL;
	}
	public BigDecimal getR166_INTEREST_RATE() {
		return R166_INTEREST_RATE;
	}
	public void setR166_INTEREST_RATE(BigDecimal r166_INTEREST_RATE) {
		R166_INTEREST_RATE = r166_INTEREST_RATE;
	}
	public BigDecimal getR166_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R166_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR166_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r166_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R166_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r166_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR166_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R166_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR166_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r166_LIMIT_PCT_UNIMPAIRED_CAP) {
		R166_LIMIT_PCT_UNIMPAIRED_CAP = r166_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR167_NO_OF_GROUP() {
		return R167_NO_OF_GROUP;
	}
	public void setR167_NO_OF_GROUP(String r167_NO_OF_GROUP) {
		R167_NO_OF_GROUP = r167_NO_OF_GROUP;
	}
	public String getR167_NO_OF_CUSTOMER() {
		return R167_NO_OF_CUSTOMER;
	}
	public void setR167_NO_OF_CUSTOMER(String r167_NO_OF_CUSTOMER) {
		R167_NO_OF_CUSTOMER = r167_NO_OF_CUSTOMER;
	}
	public String getR167_SECTOR_TYPE() {
		return R167_SECTOR_TYPE;
	}
	public void setR167_SECTOR_TYPE(String r167_SECTOR_TYPE) {
		R167_SECTOR_TYPE = r167_SECTOR_TYPE;
	}
	public String getR167_FACILITY_TYPE() {
		return R167_FACILITY_TYPE;
	}
	public void setR167_FACILITY_TYPE(String r167_FACILITY_TYPE) {
		R167_FACILITY_TYPE = r167_FACILITY_TYPE;
	}
	public BigDecimal getR167_ORIGINAL_AMOUNT() {
		return R167_ORIGINAL_AMOUNT;
	}
	public void setR167_ORIGINAL_AMOUNT(BigDecimal r167_ORIGINAL_AMOUNT) {
		R167_ORIGINAL_AMOUNT = r167_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR167_UTILISATION_OUTSTANDING_BAL() {
		return R167_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR167_UTILISATION_OUTSTANDING_BAL(BigDecimal r167_UTILISATION_OUTSTANDING_BAL) {
		R167_UTILISATION_OUTSTANDING_BAL = r167_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR167_EFFECTIVE_DATE() {
		return R167_EFFECTIVE_DATE;
	}
	public void setR167_EFFECTIVE_DATE(Date r167_EFFECTIVE_DATE) {
		R167_EFFECTIVE_DATE = r167_EFFECTIVE_DATE;
	}
	public String getR167_REPAYMENT_PERIOD() {
		return R167_REPAYMENT_PERIOD;
	}
	public void setR167_REPAYMENT_PERIOD(String r167_REPAYMENT_PERIOD) {
		R167_REPAYMENT_PERIOD = r167_REPAYMENT_PERIOD;
	}
	public String getR167_PERFORMANCE_STATUS() {
		return R167_PERFORMANCE_STATUS;
	}
	public void setR167_PERFORMANCE_STATUS(String r167_PERFORMANCE_STATUS) {
		R167_PERFORMANCE_STATUS = r167_PERFORMANCE_STATUS;
	}
	public String getR167_SECURITY() {
		return R167_SECURITY;
	}
	public void setR167_SECURITY(String r167_SECURITY) {
		R167_SECURITY = r167_SECURITY;
	}
	public String getR167_BOARD_APPROVAL() {
		return R167_BOARD_APPROVAL;
	}
	public void setR167_BOARD_APPROVAL(String r167_BOARD_APPROVAL) {
		R167_BOARD_APPROVAL = r167_BOARD_APPROVAL;
	}
	public BigDecimal getR167_INTEREST_RATE() {
		return R167_INTEREST_RATE;
	}
	public void setR167_INTEREST_RATE(BigDecimal r167_INTEREST_RATE) {
		R167_INTEREST_RATE = r167_INTEREST_RATE;
	}
	public BigDecimal getR167_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R167_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR167_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r167_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R167_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r167_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR167_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R167_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR167_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r167_LIMIT_PCT_UNIMPAIRED_CAP) {
		R167_LIMIT_PCT_UNIMPAIRED_CAP = r167_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR168_NO_OF_GROUP() {
		return R168_NO_OF_GROUP;
	}
	public void setR168_NO_OF_GROUP(String r168_NO_OF_GROUP) {
		R168_NO_OF_GROUP = r168_NO_OF_GROUP;
	}
	public String getR168_NO_OF_CUSTOMER() {
		return R168_NO_OF_CUSTOMER;
	}
	public void setR168_NO_OF_CUSTOMER(String r168_NO_OF_CUSTOMER) {
		R168_NO_OF_CUSTOMER = r168_NO_OF_CUSTOMER;
	}
	public String getR168_SECTOR_TYPE() {
		return R168_SECTOR_TYPE;
	}
	public void setR168_SECTOR_TYPE(String r168_SECTOR_TYPE) {
		R168_SECTOR_TYPE = r168_SECTOR_TYPE;
	}
	public String getR168_FACILITY_TYPE() {
		return R168_FACILITY_TYPE;
	}
	public void setR168_FACILITY_TYPE(String r168_FACILITY_TYPE) {
		R168_FACILITY_TYPE = r168_FACILITY_TYPE;
	}
	public BigDecimal getR168_ORIGINAL_AMOUNT() {
		return R168_ORIGINAL_AMOUNT;
	}
	public void setR168_ORIGINAL_AMOUNT(BigDecimal r168_ORIGINAL_AMOUNT) {
		R168_ORIGINAL_AMOUNT = r168_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR168_UTILISATION_OUTSTANDING_BAL() {
		return R168_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR168_UTILISATION_OUTSTANDING_BAL(BigDecimal r168_UTILISATION_OUTSTANDING_BAL) {
		R168_UTILISATION_OUTSTANDING_BAL = r168_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR168_EFFECTIVE_DATE() {
		return R168_EFFECTIVE_DATE;
	}
	public void setR168_EFFECTIVE_DATE(Date r168_EFFECTIVE_DATE) {
		R168_EFFECTIVE_DATE = r168_EFFECTIVE_DATE;
	}
	public String getR168_REPAYMENT_PERIOD() {
		return R168_REPAYMENT_PERIOD;
	}
	public void setR168_REPAYMENT_PERIOD(String r168_REPAYMENT_PERIOD) {
		R168_REPAYMENT_PERIOD = r168_REPAYMENT_PERIOD;
	}
	public String getR168_PERFORMANCE_STATUS() {
		return R168_PERFORMANCE_STATUS;
	}
	public void setR168_PERFORMANCE_STATUS(String r168_PERFORMANCE_STATUS) {
		R168_PERFORMANCE_STATUS = r168_PERFORMANCE_STATUS;
	}
	public String getR168_SECURITY() {
		return R168_SECURITY;
	}
	public void setR168_SECURITY(String r168_SECURITY) {
		R168_SECURITY = r168_SECURITY;
	}
	public String getR168_BOARD_APPROVAL() {
		return R168_BOARD_APPROVAL;
	}
	public void setR168_BOARD_APPROVAL(String r168_BOARD_APPROVAL) {
		R168_BOARD_APPROVAL = r168_BOARD_APPROVAL;
	}
	public BigDecimal getR168_INTEREST_RATE() {
		return R168_INTEREST_RATE;
	}
	public void setR168_INTEREST_RATE(BigDecimal r168_INTEREST_RATE) {
		R168_INTEREST_RATE = r168_INTEREST_RATE;
	}
	public BigDecimal getR168_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R168_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR168_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r168_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R168_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r168_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR168_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R168_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR168_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r168_LIMIT_PCT_UNIMPAIRED_CAP) {
		R168_LIMIT_PCT_UNIMPAIRED_CAP = r168_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR169_NO_OF_GROUP() {
		return R169_NO_OF_GROUP;
	}
	public void setR169_NO_OF_GROUP(String r169_NO_OF_GROUP) {
		R169_NO_OF_GROUP = r169_NO_OF_GROUP;
	}
	public String getR169_NO_OF_CUSTOMER() {
		return R169_NO_OF_CUSTOMER;
	}
	public void setR169_NO_OF_CUSTOMER(String r169_NO_OF_CUSTOMER) {
		R169_NO_OF_CUSTOMER = r169_NO_OF_CUSTOMER;
	}
	public String getR169_SECTOR_TYPE() {
		return R169_SECTOR_TYPE;
	}
	public void setR169_SECTOR_TYPE(String r169_SECTOR_TYPE) {
		R169_SECTOR_TYPE = r169_SECTOR_TYPE;
	}
	public String getR169_FACILITY_TYPE() {
		return R169_FACILITY_TYPE;
	}
	public void setR169_FACILITY_TYPE(String r169_FACILITY_TYPE) {
		R169_FACILITY_TYPE = r169_FACILITY_TYPE;
	}
	public BigDecimal getR169_ORIGINAL_AMOUNT() {
		return R169_ORIGINAL_AMOUNT;
	}
	public void setR169_ORIGINAL_AMOUNT(BigDecimal r169_ORIGINAL_AMOUNT) {
		R169_ORIGINAL_AMOUNT = r169_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR169_UTILISATION_OUTSTANDING_BAL() {
		return R169_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR169_UTILISATION_OUTSTANDING_BAL(BigDecimal r169_UTILISATION_OUTSTANDING_BAL) {
		R169_UTILISATION_OUTSTANDING_BAL = r169_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR169_EFFECTIVE_DATE() {
		return R169_EFFECTIVE_DATE;
	}
	public void setR169_EFFECTIVE_DATE(Date r169_EFFECTIVE_DATE) {
		R169_EFFECTIVE_DATE = r169_EFFECTIVE_DATE;
	}
	public String getR169_REPAYMENT_PERIOD() {
		return R169_REPAYMENT_PERIOD;
	}
	public void setR169_REPAYMENT_PERIOD(String r169_REPAYMENT_PERIOD) {
		R169_REPAYMENT_PERIOD = r169_REPAYMENT_PERIOD;
	}
	public String getR169_PERFORMANCE_STATUS() {
		return R169_PERFORMANCE_STATUS;
	}
	public void setR169_PERFORMANCE_STATUS(String r169_PERFORMANCE_STATUS) {
		R169_PERFORMANCE_STATUS = r169_PERFORMANCE_STATUS;
	}
	public String getR169_SECURITY() {
		return R169_SECURITY;
	}
	public void setR169_SECURITY(String r169_SECURITY) {
		R169_SECURITY = r169_SECURITY;
	}
	public String getR169_BOARD_APPROVAL() {
		return R169_BOARD_APPROVAL;
	}
	public void setR169_BOARD_APPROVAL(String r169_BOARD_APPROVAL) {
		R169_BOARD_APPROVAL = r169_BOARD_APPROVAL;
	}
	public BigDecimal getR169_INTEREST_RATE() {
		return R169_INTEREST_RATE;
	}
	public void setR169_INTEREST_RATE(BigDecimal r169_INTEREST_RATE) {
		R169_INTEREST_RATE = r169_INTEREST_RATE;
	}
	public BigDecimal getR169_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R169_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR169_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r169_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R169_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r169_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR169_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R169_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR169_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r169_LIMIT_PCT_UNIMPAIRED_CAP) {
		R169_LIMIT_PCT_UNIMPAIRED_CAP = r169_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR170_NO_OF_GROUP() {
		return R170_NO_OF_GROUP;
	}
	public void setR170_NO_OF_GROUP(String r170_NO_OF_GROUP) {
		R170_NO_OF_GROUP = r170_NO_OF_GROUP;
	}
	public String getR170_NO_OF_CUSTOMER() {
		return R170_NO_OF_CUSTOMER;
	}
	public void setR170_NO_OF_CUSTOMER(String r170_NO_OF_CUSTOMER) {
		R170_NO_OF_CUSTOMER = r170_NO_OF_CUSTOMER;
	}
	public String getR170_SECTOR_TYPE() {
		return R170_SECTOR_TYPE;
	}
	public void setR170_SECTOR_TYPE(String r170_SECTOR_TYPE) {
		R170_SECTOR_TYPE = r170_SECTOR_TYPE;
	}
	public String getR170_FACILITY_TYPE() {
		return R170_FACILITY_TYPE;
	}
	public void setR170_FACILITY_TYPE(String r170_FACILITY_TYPE) {
		R170_FACILITY_TYPE = r170_FACILITY_TYPE;
	}
	public BigDecimal getR170_ORIGINAL_AMOUNT() {
		return R170_ORIGINAL_AMOUNT;
	}
	public void setR170_ORIGINAL_AMOUNT(BigDecimal r170_ORIGINAL_AMOUNT) {
		R170_ORIGINAL_AMOUNT = r170_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR170_UTILISATION_OUTSTANDING_BAL() {
		return R170_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR170_UTILISATION_OUTSTANDING_BAL(BigDecimal r170_UTILISATION_OUTSTANDING_BAL) {
		R170_UTILISATION_OUTSTANDING_BAL = r170_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR170_EFFECTIVE_DATE() {
		return R170_EFFECTIVE_DATE;
	}
	public void setR170_EFFECTIVE_DATE(Date r170_EFFECTIVE_DATE) {
		R170_EFFECTIVE_DATE = r170_EFFECTIVE_DATE;
	}
	public String getR170_REPAYMENT_PERIOD() {
		return R170_REPAYMENT_PERIOD;
	}
	public void setR170_REPAYMENT_PERIOD(String r170_REPAYMENT_PERIOD) {
		R170_REPAYMENT_PERIOD = r170_REPAYMENT_PERIOD;
	}
	public String getR170_PERFORMANCE_STATUS() {
		return R170_PERFORMANCE_STATUS;
	}
	public void setR170_PERFORMANCE_STATUS(String r170_PERFORMANCE_STATUS) {
		R170_PERFORMANCE_STATUS = r170_PERFORMANCE_STATUS;
	}
	public String getR170_SECURITY() {
		return R170_SECURITY;
	}
	public void setR170_SECURITY(String r170_SECURITY) {
		R170_SECURITY = r170_SECURITY;
	}
	public String getR170_BOARD_APPROVAL() {
		return R170_BOARD_APPROVAL;
	}
	public void setR170_BOARD_APPROVAL(String r170_BOARD_APPROVAL) {
		R170_BOARD_APPROVAL = r170_BOARD_APPROVAL;
	}
	public BigDecimal getR170_INTEREST_RATE() {
		return R170_INTEREST_RATE;
	}
	public void setR170_INTEREST_RATE(BigDecimal r170_INTEREST_RATE) {
		R170_INTEREST_RATE = r170_INTEREST_RATE;
	}
	public BigDecimal getR170_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R170_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR170_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r170_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R170_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r170_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR170_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R170_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR170_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r170_LIMIT_PCT_UNIMPAIRED_CAP) {
		R170_LIMIT_PCT_UNIMPAIRED_CAP = r170_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR171_NO_OF_GROUP() {
		return R171_NO_OF_GROUP;
	}
	public void setR171_NO_OF_GROUP(String r171_NO_OF_GROUP) {
		R171_NO_OF_GROUP = r171_NO_OF_GROUP;
	}
	public String getR171_NO_OF_CUSTOMER() {
		return R171_NO_OF_CUSTOMER;
	}
	public void setR171_NO_OF_CUSTOMER(String r171_NO_OF_CUSTOMER) {
		R171_NO_OF_CUSTOMER = r171_NO_OF_CUSTOMER;
	}
	public String getR171_SECTOR_TYPE() {
		return R171_SECTOR_TYPE;
	}
	public void setR171_SECTOR_TYPE(String r171_SECTOR_TYPE) {
		R171_SECTOR_TYPE = r171_SECTOR_TYPE;
	}
	public String getR171_FACILITY_TYPE() {
		return R171_FACILITY_TYPE;
	}
	public void setR171_FACILITY_TYPE(String r171_FACILITY_TYPE) {
		R171_FACILITY_TYPE = r171_FACILITY_TYPE;
	}
	public BigDecimal getR171_ORIGINAL_AMOUNT() {
		return R171_ORIGINAL_AMOUNT;
	}
	public void setR171_ORIGINAL_AMOUNT(BigDecimal r171_ORIGINAL_AMOUNT) {
		R171_ORIGINAL_AMOUNT = r171_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR171_UTILISATION_OUTSTANDING_BAL() {
		return R171_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR171_UTILISATION_OUTSTANDING_BAL(BigDecimal r171_UTILISATION_OUTSTANDING_BAL) {
		R171_UTILISATION_OUTSTANDING_BAL = r171_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR171_EFFECTIVE_DATE() {
		return R171_EFFECTIVE_DATE;
	}
	public void setR171_EFFECTIVE_DATE(Date r171_EFFECTIVE_DATE) {
		R171_EFFECTIVE_DATE = r171_EFFECTIVE_DATE;
	}
	public String getR171_REPAYMENT_PERIOD() {
		return R171_REPAYMENT_PERIOD;
	}
	public void setR171_REPAYMENT_PERIOD(String r171_REPAYMENT_PERIOD) {
		R171_REPAYMENT_PERIOD = r171_REPAYMENT_PERIOD;
	}
	public String getR171_PERFORMANCE_STATUS() {
		return R171_PERFORMANCE_STATUS;
	}
	public void setR171_PERFORMANCE_STATUS(String r171_PERFORMANCE_STATUS) {
		R171_PERFORMANCE_STATUS = r171_PERFORMANCE_STATUS;
	}
	public String getR171_SECURITY() {
		return R171_SECURITY;
	}
	public void setR171_SECURITY(String r171_SECURITY) {
		R171_SECURITY = r171_SECURITY;
	}
	public String getR171_BOARD_APPROVAL() {
		return R171_BOARD_APPROVAL;
	}
	public void setR171_BOARD_APPROVAL(String r171_BOARD_APPROVAL) {
		R171_BOARD_APPROVAL = r171_BOARD_APPROVAL;
	}
	public BigDecimal getR171_INTEREST_RATE() {
		return R171_INTEREST_RATE;
	}
	public void setR171_INTEREST_RATE(BigDecimal r171_INTEREST_RATE) {
		R171_INTEREST_RATE = r171_INTEREST_RATE;
	}
	public BigDecimal getR171_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R171_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR171_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r171_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R171_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r171_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR171_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R171_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR171_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r171_LIMIT_PCT_UNIMPAIRED_CAP) {
		R171_LIMIT_PCT_UNIMPAIRED_CAP = r171_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR172_NO_OF_GROUP() {
		return R172_NO_OF_GROUP;
	}
	public void setR172_NO_OF_GROUP(String r172_NO_OF_GROUP) {
		R172_NO_OF_GROUP = r172_NO_OF_GROUP;
	}
	public String getR172_NO_OF_CUSTOMER() {
		return R172_NO_OF_CUSTOMER;
	}
	public void setR172_NO_OF_CUSTOMER(String r172_NO_OF_CUSTOMER) {
		R172_NO_OF_CUSTOMER = r172_NO_OF_CUSTOMER;
	}
	public String getR172_SECTOR_TYPE() {
		return R172_SECTOR_TYPE;
	}
	public void setR172_SECTOR_TYPE(String r172_SECTOR_TYPE) {
		R172_SECTOR_TYPE = r172_SECTOR_TYPE;
	}
	public String getR172_FACILITY_TYPE() {
		return R172_FACILITY_TYPE;
	}
	public void setR172_FACILITY_TYPE(String r172_FACILITY_TYPE) {
		R172_FACILITY_TYPE = r172_FACILITY_TYPE;
	}
	public BigDecimal getR172_ORIGINAL_AMOUNT() {
		return R172_ORIGINAL_AMOUNT;
	}
	public void setR172_ORIGINAL_AMOUNT(BigDecimal r172_ORIGINAL_AMOUNT) {
		R172_ORIGINAL_AMOUNT = r172_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR172_UTILISATION_OUTSTANDING_BAL() {
		return R172_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR172_UTILISATION_OUTSTANDING_BAL(BigDecimal r172_UTILISATION_OUTSTANDING_BAL) {
		R172_UTILISATION_OUTSTANDING_BAL = r172_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR172_EFFECTIVE_DATE() {
		return R172_EFFECTIVE_DATE;
	}
	public void setR172_EFFECTIVE_DATE(Date r172_EFFECTIVE_DATE) {
		R172_EFFECTIVE_DATE = r172_EFFECTIVE_DATE;
	}
	public String getR172_REPAYMENT_PERIOD() {
		return R172_REPAYMENT_PERIOD;
	}
	public void setR172_REPAYMENT_PERIOD(String r172_REPAYMENT_PERIOD) {
		R172_REPAYMENT_PERIOD = r172_REPAYMENT_PERIOD;
	}
	public String getR172_PERFORMANCE_STATUS() {
		return R172_PERFORMANCE_STATUS;
	}
	public void setR172_PERFORMANCE_STATUS(String r172_PERFORMANCE_STATUS) {
		R172_PERFORMANCE_STATUS = r172_PERFORMANCE_STATUS;
	}
	public String getR172_SECURITY() {
		return R172_SECURITY;
	}
	public void setR172_SECURITY(String r172_SECURITY) {
		R172_SECURITY = r172_SECURITY;
	}
	public String getR172_BOARD_APPROVAL() {
		return R172_BOARD_APPROVAL;
	}
	public void setR172_BOARD_APPROVAL(String r172_BOARD_APPROVAL) {
		R172_BOARD_APPROVAL = r172_BOARD_APPROVAL;
	}
	public BigDecimal getR172_INTEREST_RATE() {
		return R172_INTEREST_RATE;
	}
	public void setR172_INTEREST_RATE(BigDecimal r172_INTEREST_RATE) {
		R172_INTEREST_RATE = r172_INTEREST_RATE;
	}
	public BigDecimal getR172_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R172_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR172_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r172_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R172_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r172_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR172_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R172_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR172_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r172_LIMIT_PCT_UNIMPAIRED_CAP) {
		R172_LIMIT_PCT_UNIMPAIRED_CAP = r172_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR173_NO_OF_GROUP() {
		return R173_NO_OF_GROUP;
	}
	public void setR173_NO_OF_GROUP(String r173_NO_OF_GROUP) {
		R173_NO_OF_GROUP = r173_NO_OF_GROUP;
	}
	public String getR173_NO_OF_CUSTOMER() {
		return R173_NO_OF_CUSTOMER;
	}
	public void setR173_NO_OF_CUSTOMER(String r173_NO_OF_CUSTOMER) {
		R173_NO_OF_CUSTOMER = r173_NO_OF_CUSTOMER;
	}
	public String getR173_SECTOR_TYPE() {
		return R173_SECTOR_TYPE;
	}
	public void setR173_SECTOR_TYPE(String r173_SECTOR_TYPE) {
		R173_SECTOR_TYPE = r173_SECTOR_TYPE;
	}
	public String getR173_FACILITY_TYPE() {
		return R173_FACILITY_TYPE;
	}
	public void setR173_FACILITY_TYPE(String r173_FACILITY_TYPE) {
		R173_FACILITY_TYPE = r173_FACILITY_TYPE;
	}
	public BigDecimal getR173_ORIGINAL_AMOUNT() {
		return R173_ORIGINAL_AMOUNT;
	}
	public void setR173_ORIGINAL_AMOUNT(BigDecimal r173_ORIGINAL_AMOUNT) {
		R173_ORIGINAL_AMOUNT = r173_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR173_UTILISATION_OUTSTANDING_BAL() {
		return R173_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR173_UTILISATION_OUTSTANDING_BAL(BigDecimal r173_UTILISATION_OUTSTANDING_BAL) {
		R173_UTILISATION_OUTSTANDING_BAL = r173_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR173_EFFECTIVE_DATE() {
		return R173_EFFECTIVE_DATE;
	}
	public void setR173_EFFECTIVE_DATE(Date r173_EFFECTIVE_DATE) {
		R173_EFFECTIVE_DATE = r173_EFFECTIVE_DATE;
	}
	public String getR173_REPAYMENT_PERIOD() {
		return R173_REPAYMENT_PERIOD;
	}
	public void setR173_REPAYMENT_PERIOD(String r173_REPAYMENT_PERIOD) {
		R173_REPAYMENT_PERIOD = r173_REPAYMENT_PERIOD;
	}
	public String getR173_PERFORMANCE_STATUS() {
		return R173_PERFORMANCE_STATUS;
	}
	public void setR173_PERFORMANCE_STATUS(String r173_PERFORMANCE_STATUS) {
		R173_PERFORMANCE_STATUS = r173_PERFORMANCE_STATUS;
	}
	public String getR173_SECURITY() {
		return R173_SECURITY;
	}
	public void setR173_SECURITY(String r173_SECURITY) {
		R173_SECURITY = r173_SECURITY;
	}
	public String getR173_BOARD_APPROVAL() {
		return R173_BOARD_APPROVAL;
	}
	public void setR173_BOARD_APPROVAL(String r173_BOARD_APPROVAL) {
		R173_BOARD_APPROVAL = r173_BOARD_APPROVAL;
	}
	public BigDecimal getR173_INTEREST_RATE() {
		return R173_INTEREST_RATE;
	}
	public void setR173_INTEREST_RATE(BigDecimal r173_INTEREST_RATE) {
		R173_INTEREST_RATE = r173_INTEREST_RATE;
	}
	public BigDecimal getR173_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R173_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR173_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r173_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R173_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r173_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR173_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R173_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR173_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r173_LIMIT_PCT_UNIMPAIRED_CAP) {
		R173_LIMIT_PCT_UNIMPAIRED_CAP = r173_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR174_NO_OF_GROUP() {
		return R174_NO_OF_GROUP;
	}
	public void setR174_NO_OF_GROUP(String r174_NO_OF_GROUP) {
		R174_NO_OF_GROUP = r174_NO_OF_GROUP;
	}
	public String getR174_NO_OF_CUSTOMER() {
		return R174_NO_OF_CUSTOMER;
	}
	public void setR174_NO_OF_CUSTOMER(String r174_NO_OF_CUSTOMER) {
		R174_NO_OF_CUSTOMER = r174_NO_OF_CUSTOMER;
	}
	public String getR174_SECTOR_TYPE() {
		return R174_SECTOR_TYPE;
	}
	public void setR174_SECTOR_TYPE(String r174_SECTOR_TYPE) {
		R174_SECTOR_TYPE = r174_SECTOR_TYPE;
	}
	public String getR174_FACILITY_TYPE() {
		return R174_FACILITY_TYPE;
	}
	public void setR174_FACILITY_TYPE(String r174_FACILITY_TYPE) {
		R174_FACILITY_TYPE = r174_FACILITY_TYPE;
	}
	public BigDecimal getR174_ORIGINAL_AMOUNT() {
		return R174_ORIGINAL_AMOUNT;
	}
	public void setR174_ORIGINAL_AMOUNT(BigDecimal r174_ORIGINAL_AMOUNT) {
		R174_ORIGINAL_AMOUNT = r174_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR174_UTILISATION_OUTSTANDING_BAL() {
		return R174_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR174_UTILISATION_OUTSTANDING_BAL(BigDecimal r174_UTILISATION_OUTSTANDING_BAL) {
		R174_UTILISATION_OUTSTANDING_BAL = r174_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR174_EFFECTIVE_DATE() {
		return R174_EFFECTIVE_DATE;
	}
	public void setR174_EFFECTIVE_DATE(Date r174_EFFECTIVE_DATE) {
		R174_EFFECTIVE_DATE = r174_EFFECTIVE_DATE;
	}
	public String getR174_REPAYMENT_PERIOD() {
		return R174_REPAYMENT_PERIOD;
	}
	public void setR174_REPAYMENT_PERIOD(String r174_REPAYMENT_PERIOD) {
		R174_REPAYMENT_PERIOD = r174_REPAYMENT_PERIOD;
	}
	public String getR174_PERFORMANCE_STATUS() {
		return R174_PERFORMANCE_STATUS;
	}
	public void setR174_PERFORMANCE_STATUS(String r174_PERFORMANCE_STATUS) {
		R174_PERFORMANCE_STATUS = r174_PERFORMANCE_STATUS;
	}
	public String getR174_SECURITY() {
		return R174_SECURITY;
	}
	public void setR174_SECURITY(String r174_SECURITY) {
		R174_SECURITY = r174_SECURITY;
	}
	public String getR174_BOARD_APPROVAL() {
		return R174_BOARD_APPROVAL;
	}
	public void setR174_BOARD_APPROVAL(String r174_BOARD_APPROVAL) {
		R174_BOARD_APPROVAL = r174_BOARD_APPROVAL;
	}
	public BigDecimal getR174_INTEREST_RATE() {
		return R174_INTEREST_RATE;
	}
	public void setR174_INTEREST_RATE(BigDecimal r174_INTEREST_RATE) {
		R174_INTEREST_RATE = r174_INTEREST_RATE;
	}
	public BigDecimal getR174_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R174_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR174_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r174_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R174_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r174_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR174_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R174_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR174_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r174_LIMIT_PCT_UNIMPAIRED_CAP) {
		R174_LIMIT_PCT_UNIMPAIRED_CAP = r174_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR175_NO_OF_GROUP() {
		return R175_NO_OF_GROUP;
	}
	public void setR175_NO_OF_GROUP(String r175_NO_OF_GROUP) {
		R175_NO_OF_GROUP = r175_NO_OF_GROUP;
	}
	public String getR175_NO_OF_CUSTOMER() {
		return R175_NO_OF_CUSTOMER;
	}
	public void setR175_NO_OF_CUSTOMER(String r175_NO_OF_CUSTOMER) {
		R175_NO_OF_CUSTOMER = r175_NO_OF_CUSTOMER;
	}
	public String getR175_SECTOR_TYPE() {
		return R175_SECTOR_TYPE;
	}
	public void setR175_SECTOR_TYPE(String r175_SECTOR_TYPE) {
		R175_SECTOR_TYPE = r175_SECTOR_TYPE;
	}
	public String getR175_FACILITY_TYPE() {
		return R175_FACILITY_TYPE;
	}
	public void setR175_FACILITY_TYPE(String r175_FACILITY_TYPE) {
		R175_FACILITY_TYPE = r175_FACILITY_TYPE;
	}
	public BigDecimal getR175_ORIGINAL_AMOUNT() {
		return R175_ORIGINAL_AMOUNT;
	}
	public void setR175_ORIGINAL_AMOUNT(BigDecimal r175_ORIGINAL_AMOUNT) {
		R175_ORIGINAL_AMOUNT = r175_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR175_UTILISATION_OUTSTANDING_BAL() {
		return R175_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR175_UTILISATION_OUTSTANDING_BAL(BigDecimal r175_UTILISATION_OUTSTANDING_BAL) {
		R175_UTILISATION_OUTSTANDING_BAL = r175_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR175_EFFECTIVE_DATE() {
		return R175_EFFECTIVE_DATE;
	}
	public void setR175_EFFECTIVE_DATE(Date r175_EFFECTIVE_DATE) {
		R175_EFFECTIVE_DATE = r175_EFFECTIVE_DATE;
	}
	public String getR175_REPAYMENT_PERIOD() {
		return R175_REPAYMENT_PERIOD;
	}
	public void setR175_REPAYMENT_PERIOD(String r175_REPAYMENT_PERIOD) {
		R175_REPAYMENT_PERIOD = r175_REPAYMENT_PERIOD;
	}
	public String getR175_PERFORMANCE_STATUS() {
		return R175_PERFORMANCE_STATUS;
	}
	public void setR175_PERFORMANCE_STATUS(String r175_PERFORMANCE_STATUS) {
		R175_PERFORMANCE_STATUS = r175_PERFORMANCE_STATUS;
	}
	public String getR175_SECURITY() {
		return R175_SECURITY;
	}
	public void setR175_SECURITY(String r175_SECURITY) {
		R175_SECURITY = r175_SECURITY;
	}
	public String getR175_BOARD_APPROVAL() {
		return R175_BOARD_APPROVAL;
	}
	public void setR175_BOARD_APPROVAL(String r175_BOARD_APPROVAL) {
		R175_BOARD_APPROVAL = r175_BOARD_APPROVAL;
	}
	public BigDecimal getR175_INTEREST_RATE() {
		return R175_INTEREST_RATE;
	}
	public void setR175_INTEREST_RATE(BigDecimal r175_INTEREST_RATE) {
		R175_INTEREST_RATE = r175_INTEREST_RATE;
	}
	public BigDecimal getR175_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R175_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR175_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r175_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R175_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r175_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR175_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R175_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR175_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r175_LIMIT_PCT_UNIMPAIRED_CAP) {
		R175_LIMIT_PCT_UNIMPAIRED_CAP = r175_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR176_NO_OF_GROUP() {
		return R176_NO_OF_GROUP;
	}
	public void setR176_NO_OF_GROUP(String r176_NO_OF_GROUP) {
		R176_NO_OF_GROUP = r176_NO_OF_GROUP;
	}
	public String getR176_NO_OF_CUSTOMER() {
		return R176_NO_OF_CUSTOMER;
	}
	public void setR176_NO_OF_CUSTOMER(String r176_NO_OF_CUSTOMER) {
		R176_NO_OF_CUSTOMER = r176_NO_OF_CUSTOMER;
	}
	public String getR176_SECTOR_TYPE() {
		return R176_SECTOR_TYPE;
	}
	public void setR176_SECTOR_TYPE(String r176_SECTOR_TYPE) {
		R176_SECTOR_TYPE = r176_SECTOR_TYPE;
	}
	public String getR176_FACILITY_TYPE() {
		return R176_FACILITY_TYPE;
	}
	public void setR176_FACILITY_TYPE(String r176_FACILITY_TYPE) {
		R176_FACILITY_TYPE = r176_FACILITY_TYPE;
	}
	public BigDecimal getR176_ORIGINAL_AMOUNT() {
		return R176_ORIGINAL_AMOUNT;
	}
	public void setR176_ORIGINAL_AMOUNT(BigDecimal r176_ORIGINAL_AMOUNT) {
		R176_ORIGINAL_AMOUNT = r176_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR176_UTILISATION_OUTSTANDING_BAL() {
		return R176_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR176_UTILISATION_OUTSTANDING_BAL(BigDecimal r176_UTILISATION_OUTSTANDING_BAL) {
		R176_UTILISATION_OUTSTANDING_BAL = r176_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR176_EFFECTIVE_DATE() {
		return R176_EFFECTIVE_DATE;
	}
	public void setR176_EFFECTIVE_DATE(Date r176_EFFECTIVE_DATE) {
		R176_EFFECTIVE_DATE = r176_EFFECTIVE_DATE;
	}
	public String getR176_REPAYMENT_PERIOD() {
		return R176_REPAYMENT_PERIOD;
	}
	public void setR176_REPAYMENT_PERIOD(String r176_REPAYMENT_PERIOD) {
		R176_REPAYMENT_PERIOD = r176_REPAYMENT_PERIOD;
	}
	public String getR176_PERFORMANCE_STATUS() {
		return R176_PERFORMANCE_STATUS;
	}
	public void setR176_PERFORMANCE_STATUS(String r176_PERFORMANCE_STATUS) {
		R176_PERFORMANCE_STATUS = r176_PERFORMANCE_STATUS;
	}
	public String getR176_SECURITY() {
		return R176_SECURITY;
	}
	public void setR176_SECURITY(String r176_SECURITY) {
		R176_SECURITY = r176_SECURITY;
	}
	public String getR176_BOARD_APPROVAL() {
		return R176_BOARD_APPROVAL;
	}
	public void setR176_BOARD_APPROVAL(String r176_BOARD_APPROVAL) {
		R176_BOARD_APPROVAL = r176_BOARD_APPROVAL;
	}
	public BigDecimal getR176_INTEREST_RATE() {
		return R176_INTEREST_RATE;
	}
	public void setR176_INTEREST_RATE(BigDecimal r176_INTEREST_RATE) {
		R176_INTEREST_RATE = r176_INTEREST_RATE;
	}
	public BigDecimal getR176_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R176_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR176_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r176_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R176_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r176_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR176_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R176_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR176_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r176_LIMIT_PCT_UNIMPAIRED_CAP) {
		R176_LIMIT_PCT_UNIMPAIRED_CAP = r176_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR177_NO_OF_GROUP() {
		return R177_NO_OF_GROUP;
	}
	public void setR177_NO_OF_GROUP(String r177_NO_OF_GROUP) {
		R177_NO_OF_GROUP = r177_NO_OF_GROUP;
	}
	public String getR177_NO_OF_CUSTOMER() {
		return R177_NO_OF_CUSTOMER;
	}
	public void setR177_NO_OF_CUSTOMER(String r177_NO_OF_CUSTOMER) {
		R177_NO_OF_CUSTOMER = r177_NO_OF_CUSTOMER;
	}
	public String getR177_SECTOR_TYPE() {
		return R177_SECTOR_TYPE;
	}
	public void setR177_SECTOR_TYPE(String r177_SECTOR_TYPE) {
		R177_SECTOR_TYPE = r177_SECTOR_TYPE;
	}
	public String getR177_FACILITY_TYPE() {
		return R177_FACILITY_TYPE;
	}
	public void setR177_FACILITY_TYPE(String r177_FACILITY_TYPE) {
		R177_FACILITY_TYPE = r177_FACILITY_TYPE;
	}
	public BigDecimal getR177_ORIGINAL_AMOUNT() {
		return R177_ORIGINAL_AMOUNT;
	}
	public void setR177_ORIGINAL_AMOUNT(BigDecimal r177_ORIGINAL_AMOUNT) {
		R177_ORIGINAL_AMOUNT = r177_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR177_UTILISATION_OUTSTANDING_BAL() {
		return R177_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR177_UTILISATION_OUTSTANDING_BAL(BigDecimal r177_UTILISATION_OUTSTANDING_BAL) {
		R177_UTILISATION_OUTSTANDING_BAL = r177_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR177_EFFECTIVE_DATE() {
		return R177_EFFECTIVE_DATE;
	}
	public void setR177_EFFECTIVE_DATE(Date r177_EFFECTIVE_DATE) {
		R177_EFFECTIVE_DATE = r177_EFFECTIVE_DATE;
	}
	public String getR177_REPAYMENT_PERIOD() {
		return R177_REPAYMENT_PERIOD;
	}
	public void setR177_REPAYMENT_PERIOD(String r177_REPAYMENT_PERIOD) {
		R177_REPAYMENT_PERIOD = r177_REPAYMENT_PERIOD;
	}
	public String getR177_PERFORMANCE_STATUS() {
		return R177_PERFORMANCE_STATUS;
	}
	public void setR177_PERFORMANCE_STATUS(String r177_PERFORMANCE_STATUS) {
		R177_PERFORMANCE_STATUS = r177_PERFORMANCE_STATUS;
	}
	public String getR177_SECURITY() {
		return R177_SECURITY;
	}
	public void setR177_SECURITY(String r177_SECURITY) {
		R177_SECURITY = r177_SECURITY;
	}
	public String getR177_BOARD_APPROVAL() {
		return R177_BOARD_APPROVAL;
	}
	public void setR177_BOARD_APPROVAL(String r177_BOARD_APPROVAL) {
		R177_BOARD_APPROVAL = r177_BOARD_APPROVAL;
	}
	public BigDecimal getR177_INTEREST_RATE() {
		return R177_INTEREST_RATE;
	}
	public void setR177_INTEREST_RATE(BigDecimal r177_INTEREST_RATE) {
		R177_INTEREST_RATE = r177_INTEREST_RATE;
	}
	public BigDecimal getR177_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R177_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR177_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r177_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R177_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r177_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR177_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R177_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR177_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r177_LIMIT_PCT_UNIMPAIRED_CAP) {
		R177_LIMIT_PCT_UNIMPAIRED_CAP = r177_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR178_NO_OF_GROUP() {
		return R178_NO_OF_GROUP;
	}
	public void setR178_NO_OF_GROUP(String r178_NO_OF_GROUP) {
		R178_NO_OF_GROUP = r178_NO_OF_GROUP;
	}
	public String getR178_NO_OF_CUSTOMER() {
		return R178_NO_OF_CUSTOMER;
	}
	public void setR178_NO_OF_CUSTOMER(String r178_NO_OF_CUSTOMER) {
		R178_NO_OF_CUSTOMER = r178_NO_OF_CUSTOMER;
	}
	public String getR178_SECTOR_TYPE() {
		return R178_SECTOR_TYPE;
	}
	public void setR178_SECTOR_TYPE(String r178_SECTOR_TYPE) {
		R178_SECTOR_TYPE = r178_SECTOR_TYPE;
	}
	public String getR178_FACILITY_TYPE() {
		return R178_FACILITY_TYPE;
	}
	public void setR178_FACILITY_TYPE(String r178_FACILITY_TYPE) {
		R178_FACILITY_TYPE = r178_FACILITY_TYPE;
	}
	public BigDecimal getR178_ORIGINAL_AMOUNT() {
		return R178_ORIGINAL_AMOUNT;
	}
	public void setR178_ORIGINAL_AMOUNT(BigDecimal r178_ORIGINAL_AMOUNT) {
		R178_ORIGINAL_AMOUNT = r178_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR178_UTILISATION_OUTSTANDING_BAL() {
		return R178_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR178_UTILISATION_OUTSTANDING_BAL(BigDecimal r178_UTILISATION_OUTSTANDING_BAL) {
		R178_UTILISATION_OUTSTANDING_BAL = r178_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR178_EFFECTIVE_DATE() {
		return R178_EFFECTIVE_DATE;
	}
	public void setR178_EFFECTIVE_DATE(Date r178_EFFECTIVE_DATE) {
		R178_EFFECTIVE_DATE = r178_EFFECTIVE_DATE;
	}
	public String getR178_REPAYMENT_PERIOD() {
		return R178_REPAYMENT_PERIOD;
	}
	public void setR178_REPAYMENT_PERIOD(String r178_REPAYMENT_PERIOD) {
		R178_REPAYMENT_PERIOD = r178_REPAYMENT_PERIOD;
	}
	public String getR178_PERFORMANCE_STATUS() {
		return R178_PERFORMANCE_STATUS;
	}
	public void setR178_PERFORMANCE_STATUS(String r178_PERFORMANCE_STATUS) {
		R178_PERFORMANCE_STATUS = r178_PERFORMANCE_STATUS;
	}
	public String getR178_SECURITY() {
		return R178_SECURITY;
	}
	public void setR178_SECURITY(String r178_SECURITY) {
		R178_SECURITY = r178_SECURITY;
	}
	public String getR178_BOARD_APPROVAL() {
		return R178_BOARD_APPROVAL;
	}
	public void setR178_BOARD_APPROVAL(String r178_BOARD_APPROVAL) {
		R178_BOARD_APPROVAL = r178_BOARD_APPROVAL;
	}
	public BigDecimal getR178_INTEREST_RATE() {
		return R178_INTEREST_RATE;
	}
	public void setR178_INTEREST_RATE(BigDecimal r178_INTEREST_RATE) {
		R178_INTEREST_RATE = r178_INTEREST_RATE;
	}
	public BigDecimal getR178_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R178_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR178_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r178_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R178_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r178_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR178_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R178_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR178_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r178_LIMIT_PCT_UNIMPAIRED_CAP) {
		R178_LIMIT_PCT_UNIMPAIRED_CAP = r178_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR179_NO_OF_GROUP() {
		return R179_NO_OF_GROUP;
	}
	public void setR179_NO_OF_GROUP(String r179_NO_OF_GROUP) {
		R179_NO_OF_GROUP = r179_NO_OF_GROUP;
	}
	public String getR179_NO_OF_CUSTOMER() {
		return R179_NO_OF_CUSTOMER;
	}
	public void setR179_NO_OF_CUSTOMER(String r179_NO_OF_CUSTOMER) {
		R179_NO_OF_CUSTOMER = r179_NO_OF_CUSTOMER;
	}
	public String getR179_SECTOR_TYPE() {
		return R179_SECTOR_TYPE;
	}
	public void setR179_SECTOR_TYPE(String r179_SECTOR_TYPE) {
		R179_SECTOR_TYPE = r179_SECTOR_TYPE;
	}
	public String getR179_FACILITY_TYPE() {
		return R179_FACILITY_TYPE;
	}
	public void setR179_FACILITY_TYPE(String r179_FACILITY_TYPE) {
		R179_FACILITY_TYPE = r179_FACILITY_TYPE;
	}
	public BigDecimal getR179_ORIGINAL_AMOUNT() {
		return R179_ORIGINAL_AMOUNT;
	}
	public void setR179_ORIGINAL_AMOUNT(BigDecimal r179_ORIGINAL_AMOUNT) {
		R179_ORIGINAL_AMOUNT = r179_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR179_UTILISATION_OUTSTANDING_BAL() {
		return R179_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR179_UTILISATION_OUTSTANDING_BAL(BigDecimal r179_UTILISATION_OUTSTANDING_BAL) {
		R179_UTILISATION_OUTSTANDING_BAL = r179_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR179_EFFECTIVE_DATE() {
		return R179_EFFECTIVE_DATE;
	}
	public void setR179_EFFECTIVE_DATE(Date r179_EFFECTIVE_DATE) {
		R179_EFFECTIVE_DATE = r179_EFFECTIVE_DATE;
	}
	public String getR179_REPAYMENT_PERIOD() {
		return R179_REPAYMENT_PERIOD;
	}
	public void setR179_REPAYMENT_PERIOD(String r179_REPAYMENT_PERIOD) {
		R179_REPAYMENT_PERIOD = r179_REPAYMENT_PERIOD;
	}
	public String getR179_PERFORMANCE_STATUS() {
		return R179_PERFORMANCE_STATUS;
	}
	public void setR179_PERFORMANCE_STATUS(String r179_PERFORMANCE_STATUS) {
		R179_PERFORMANCE_STATUS = r179_PERFORMANCE_STATUS;
	}
	public String getR179_SECURITY() {
		return R179_SECURITY;
	}
	public void setR179_SECURITY(String r179_SECURITY) {
		R179_SECURITY = r179_SECURITY;
	}
	public String getR179_BOARD_APPROVAL() {
		return R179_BOARD_APPROVAL;
	}
	public void setR179_BOARD_APPROVAL(String r179_BOARD_APPROVAL) {
		R179_BOARD_APPROVAL = r179_BOARD_APPROVAL;
	}
	public BigDecimal getR179_INTEREST_RATE() {
		return R179_INTEREST_RATE;
	}
	public void setR179_INTEREST_RATE(BigDecimal r179_INTEREST_RATE) {
		R179_INTEREST_RATE = r179_INTEREST_RATE;
	}
	public BigDecimal getR179_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R179_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR179_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r179_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R179_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r179_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR179_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R179_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR179_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r179_LIMIT_PCT_UNIMPAIRED_CAP) {
		R179_LIMIT_PCT_UNIMPAIRED_CAP = r179_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR180_NO_OF_GROUP() {
		return R180_NO_OF_GROUP;
	}
	public void setR180_NO_OF_GROUP(String r180_NO_OF_GROUP) {
		R180_NO_OF_GROUP = r180_NO_OF_GROUP;
	}
	public String getR180_NO_OF_CUSTOMER() {
		return R180_NO_OF_CUSTOMER;
	}
	public void setR180_NO_OF_CUSTOMER(String r180_NO_OF_CUSTOMER) {
		R180_NO_OF_CUSTOMER = r180_NO_OF_CUSTOMER;
	}
	public String getR180_SECTOR_TYPE() {
		return R180_SECTOR_TYPE;
	}
	public void setR180_SECTOR_TYPE(String r180_SECTOR_TYPE) {
		R180_SECTOR_TYPE = r180_SECTOR_TYPE;
	}
	public String getR180_FACILITY_TYPE() {
		return R180_FACILITY_TYPE;
	}
	public void setR180_FACILITY_TYPE(String r180_FACILITY_TYPE) {
		R180_FACILITY_TYPE = r180_FACILITY_TYPE;
	}
	public BigDecimal getR180_ORIGINAL_AMOUNT() {
		return R180_ORIGINAL_AMOUNT;
	}
	public void setR180_ORIGINAL_AMOUNT(BigDecimal r180_ORIGINAL_AMOUNT) {
		R180_ORIGINAL_AMOUNT = r180_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR180_UTILISATION_OUTSTANDING_BAL() {
		return R180_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR180_UTILISATION_OUTSTANDING_BAL(BigDecimal r180_UTILISATION_OUTSTANDING_BAL) {
		R180_UTILISATION_OUTSTANDING_BAL = r180_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR180_EFFECTIVE_DATE() {
		return R180_EFFECTIVE_DATE;
	}
	public void setR180_EFFECTIVE_DATE(Date r180_EFFECTIVE_DATE) {
		R180_EFFECTIVE_DATE = r180_EFFECTIVE_DATE;
	}
	public String getR180_REPAYMENT_PERIOD() {
		return R180_REPAYMENT_PERIOD;
	}
	public void setR180_REPAYMENT_PERIOD(String r180_REPAYMENT_PERIOD) {
		R180_REPAYMENT_PERIOD = r180_REPAYMENT_PERIOD;
	}
	public String getR180_PERFORMANCE_STATUS() {
		return R180_PERFORMANCE_STATUS;
	}
	public void setR180_PERFORMANCE_STATUS(String r180_PERFORMANCE_STATUS) {
		R180_PERFORMANCE_STATUS = r180_PERFORMANCE_STATUS;
	}
	public String getR180_SECURITY() {
		return R180_SECURITY;
	}
	public void setR180_SECURITY(String r180_SECURITY) {
		R180_SECURITY = r180_SECURITY;
	}
	public String getR180_BOARD_APPROVAL() {
		return R180_BOARD_APPROVAL;
	}
	public void setR180_BOARD_APPROVAL(String r180_BOARD_APPROVAL) {
		R180_BOARD_APPROVAL = r180_BOARD_APPROVAL;
	}
	public BigDecimal getR180_INTEREST_RATE() {
		return R180_INTEREST_RATE;
	}
	public void setR180_INTEREST_RATE(BigDecimal r180_INTEREST_RATE) {
		R180_INTEREST_RATE = r180_INTEREST_RATE;
	}
	public BigDecimal getR180_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R180_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR180_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r180_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R180_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r180_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR180_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R180_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR180_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r180_LIMIT_PCT_UNIMPAIRED_CAP) {
		R180_LIMIT_PCT_UNIMPAIRED_CAP = r180_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR181_NO_OF_GROUP() {
		return R181_NO_OF_GROUP;
	}
	public void setR181_NO_OF_GROUP(String r181_NO_OF_GROUP) {
		R181_NO_OF_GROUP = r181_NO_OF_GROUP;
	}
	public String getR181_NO_OF_CUSTOMER() {
		return R181_NO_OF_CUSTOMER;
	}
	public void setR181_NO_OF_CUSTOMER(String r181_NO_OF_CUSTOMER) {
		R181_NO_OF_CUSTOMER = r181_NO_OF_CUSTOMER;
	}
	public String getR181_SECTOR_TYPE() {
		return R181_SECTOR_TYPE;
	}
	public void setR181_SECTOR_TYPE(String r181_SECTOR_TYPE) {
		R181_SECTOR_TYPE = r181_SECTOR_TYPE;
	}
	public String getR181_FACILITY_TYPE() {
		return R181_FACILITY_TYPE;
	}
	public void setR181_FACILITY_TYPE(String r181_FACILITY_TYPE) {
		R181_FACILITY_TYPE = r181_FACILITY_TYPE;
	}
	public BigDecimal getR181_ORIGINAL_AMOUNT() {
		return R181_ORIGINAL_AMOUNT;
	}
	public void setR181_ORIGINAL_AMOUNT(BigDecimal r181_ORIGINAL_AMOUNT) {
		R181_ORIGINAL_AMOUNT = r181_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR181_UTILISATION_OUTSTANDING_BAL() {
		return R181_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR181_UTILISATION_OUTSTANDING_BAL(BigDecimal r181_UTILISATION_OUTSTANDING_BAL) {
		R181_UTILISATION_OUTSTANDING_BAL = r181_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR181_EFFECTIVE_DATE() {
		return R181_EFFECTIVE_DATE;
	}
	public void setR181_EFFECTIVE_DATE(Date r181_EFFECTIVE_DATE) {
		R181_EFFECTIVE_DATE = r181_EFFECTIVE_DATE;
	}
	public String getR181_REPAYMENT_PERIOD() {
		return R181_REPAYMENT_PERIOD;
	}
	public void setR181_REPAYMENT_PERIOD(String r181_REPAYMENT_PERIOD) {
		R181_REPAYMENT_PERIOD = r181_REPAYMENT_PERIOD;
	}
	public String getR181_PERFORMANCE_STATUS() {
		return R181_PERFORMANCE_STATUS;
	}
	public void setR181_PERFORMANCE_STATUS(String r181_PERFORMANCE_STATUS) {
		R181_PERFORMANCE_STATUS = r181_PERFORMANCE_STATUS;
	}
	public String getR181_SECURITY() {
		return R181_SECURITY;
	}
	public void setR181_SECURITY(String r181_SECURITY) {
		R181_SECURITY = r181_SECURITY;
	}
	public String getR181_BOARD_APPROVAL() {
		return R181_BOARD_APPROVAL;
	}
	public void setR181_BOARD_APPROVAL(String r181_BOARD_APPROVAL) {
		R181_BOARD_APPROVAL = r181_BOARD_APPROVAL;
	}
	public BigDecimal getR181_INTEREST_RATE() {
		return R181_INTEREST_RATE;
	}
	public void setR181_INTEREST_RATE(BigDecimal r181_INTEREST_RATE) {
		R181_INTEREST_RATE = r181_INTEREST_RATE;
	}
	public BigDecimal getR181_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R181_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR181_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r181_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R181_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r181_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR181_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R181_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR181_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r181_LIMIT_PCT_UNIMPAIRED_CAP) {
		R181_LIMIT_PCT_UNIMPAIRED_CAP = r181_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR182_NO_OF_GROUP() {
		return R182_NO_OF_GROUP;
	}
	public void setR182_NO_OF_GROUP(String r182_NO_OF_GROUP) {
		R182_NO_OF_GROUP = r182_NO_OF_GROUP;
	}
	public String getR182_NO_OF_CUSTOMER() {
		return R182_NO_OF_CUSTOMER;
	}
	public void setR182_NO_OF_CUSTOMER(String r182_NO_OF_CUSTOMER) {
		R182_NO_OF_CUSTOMER = r182_NO_OF_CUSTOMER;
	}
	public String getR182_SECTOR_TYPE() {
		return R182_SECTOR_TYPE;
	}
	public void setR182_SECTOR_TYPE(String r182_SECTOR_TYPE) {
		R182_SECTOR_TYPE = r182_SECTOR_TYPE;
	}
	public String getR182_FACILITY_TYPE() {
		return R182_FACILITY_TYPE;
	}
	public void setR182_FACILITY_TYPE(String r182_FACILITY_TYPE) {
		R182_FACILITY_TYPE = r182_FACILITY_TYPE;
	}
	public BigDecimal getR182_ORIGINAL_AMOUNT() {
		return R182_ORIGINAL_AMOUNT;
	}
	public void setR182_ORIGINAL_AMOUNT(BigDecimal r182_ORIGINAL_AMOUNT) {
		R182_ORIGINAL_AMOUNT = r182_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR182_UTILISATION_OUTSTANDING_BAL() {
		return R182_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR182_UTILISATION_OUTSTANDING_BAL(BigDecimal r182_UTILISATION_OUTSTANDING_BAL) {
		R182_UTILISATION_OUTSTANDING_BAL = r182_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR182_EFFECTIVE_DATE() {
		return R182_EFFECTIVE_DATE;
	}
	public void setR182_EFFECTIVE_DATE(Date r182_EFFECTIVE_DATE) {
		R182_EFFECTIVE_DATE = r182_EFFECTIVE_DATE;
	}
	public String getR182_REPAYMENT_PERIOD() {
		return R182_REPAYMENT_PERIOD;
	}
	public void setR182_REPAYMENT_PERIOD(String r182_REPAYMENT_PERIOD) {
		R182_REPAYMENT_PERIOD = r182_REPAYMENT_PERIOD;
	}
	public String getR182_PERFORMANCE_STATUS() {
		return R182_PERFORMANCE_STATUS;
	}
	public void setR182_PERFORMANCE_STATUS(String r182_PERFORMANCE_STATUS) {
		R182_PERFORMANCE_STATUS = r182_PERFORMANCE_STATUS;
	}
	public String getR182_SECURITY() {
		return R182_SECURITY;
	}
	public void setR182_SECURITY(String r182_SECURITY) {
		R182_SECURITY = r182_SECURITY;
	}
	public String getR182_BOARD_APPROVAL() {
		return R182_BOARD_APPROVAL;
	}
	public void setR182_BOARD_APPROVAL(String r182_BOARD_APPROVAL) {
		R182_BOARD_APPROVAL = r182_BOARD_APPROVAL;
	}
	public BigDecimal getR182_INTEREST_RATE() {
		return R182_INTEREST_RATE;
	}
	public void setR182_INTEREST_RATE(BigDecimal r182_INTEREST_RATE) {
		R182_INTEREST_RATE = r182_INTEREST_RATE;
	}
	public BigDecimal getR182_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R182_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR182_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r182_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R182_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r182_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR182_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R182_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR182_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r182_LIMIT_PCT_UNIMPAIRED_CAP) {
		R182_LIMIT_PCT_UNIMPAIRED_CAP = r182_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR183_NO_OF_GROUP() {
		return R183_NO_OF_GROUP;
	}
	public void setR183_NO_OF_GROUP(String r183_NO_OF_GROUP) {
		R183_NO_OF_GROUP = r183_NO_OF_GROUP;
	}
	public String getR183_NO_OF_CUSTOMER() {
		return R183_NO_OF_CUSTOMER;
	}
	public void setR183_NO_OF_CUSTOMER(String r183_NO_OF_CUSTOMER) {
		R183_NO_OF_CUSTOMER = r183_NO_OF_CUSTOMER;
	}
	public String getR183_SECTOR_TYPE() {
		return R183_SECTOR_TYPE;
	}
	public void setR183_SECTOR_TYPE(String r183_SECTOR_TYPE) {
		R183_SECTOR_TYPE = r183_SECTOR_TYPE;
	}
	public String getR183_FACILITY_TYPE() {
		return R183_FACILITY_TYPE;
	}
	public void setR183_FACILITY_TYPE(String r183_FACILITY_TYPE) {
		R183_FACILITY_TYPE = r183_FACILITY_TYPE;
	}
	public BigDecimal getR183_ORIGINAL_AMOUNT() {
		return R183_ORIGINAL_AMOUNT;
	}
	public void setR183_ORIGINAL_AMOUNT(BigDecimal r183_ORIGINAL_AMOUNT) {
		R183_ORIGINAL_AMOUNT = r183_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR183_UTILISATION_OUTSTANDING_BAL() {
		return R183_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR183_UTILISATION_OUTSTANDING_BAL(BigDecimal r183_UTILISATION_OUTSTANDING_BAL) {
		R183_UTILISATION_OUTSTANDING_BAL = r183_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR183_EFFECTIVE_DATE() {
		return R183_EFFECTIVE_DATE;
	}
	public void setR183_EFFECTIVE_DATE(Date r183_EFFECTIVE_DATE) {
		R183_EFFECTIVE_DATE = r183_EFFECTIVE_DATE;
	}
	public String getR183_REPAYMENT_PERIOD() {
		return R183_REPAYMENT_PERIOD;
	}
	public void setR183_REPAYMENT_PERIOD(String r183_REPAYMENT_PERIOD) {
		R183_REPAYMENT_PERIOD = r183_REPAYMENT_PERIOD;
	}
	public String getR183_PERFORMANCE_STATUS() {
		return R183_PERFORMANCE_STATUS;
	}
	public void setR183_PERFORMANCE_STATUS(String r183_PERFORMANCE_STATUS) {
		R183_PERFORMANCE_STATUS = r183_PERFORMANCE_STATUS;
	}
	public String getR183_SECURITY() {
		return R183_SECURITY;
	}
	public void setR183_SECURITY(String r183_SECURITY) {
		R183_SECURITY = r183_SECURITY;
	}
	public String getR183_BOARD_APPROVAL() {
		return R183_BOARD_APPROVAL;
	}
	public void setR183_BOARD_APPROVAL(String r183_BOARD_APPROVAL) {
		R183_BOARD_APPROVAL = r183_BOARD_APPROVAL;
	}
	public BigDecimal getR183_INTEREST_RATE() {
		return R183_INTEREST_RATE;
	}
	public void setR183_INTEREST_RATE(BigDecimal r183_INTEREST_RATE) {
		R183_INTEREST_RATE = r183_INTEREST_RATE;
	}
	public BigDecimal getR183_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R183_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR183_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r183_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R183_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r183_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR183_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R183_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR183_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r183_LIMIT_PCT_UNIMPAIRED_CAP) {
		R183_LIMIT_PCT_UNIMPAIRED_CAP = r183_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR184_NO_OF_GROUP() {
		return R184_NO_OF_GROUP;
	}
	public void setR184_NO_OF_GROUP(String r184_NO_OF_GROUP) {
		R184_NO_OF_GROUP = r184_NO_OF_GROUP;
	}
	public String getR184_NO_OF_CUSTOMER() {
		return R184_NO_OF_CUSTOMER;
	}
	public void setR184_NO_OF_CUSTOMER(String r184_NO_OF_CUSTOMER) {
		R184_NO_OF_CUSTOMER = r184_NO_OF_CUSTOMER;
	}
	public String getR184_SECTOR_TYPE() {
		return R184_SECTOR_TYPE;
	}
	public void setR184_SECTOR_TYPE(String r184_SECTOR_TYPE) {
		R184_SECTOR_TYPE = r184_SECTOR_TYPE;
	}
	public String getR184_FACILITY_TYPE() {
		return R184_FACILITY_TYPE;
	}
	public void setR184_FACILITY_TYPE(String r184_FACILITY_TYPE) {
		R184_FACILITY_TYPE = r184_FACILITY_TYPE;
	}
	public BigDecimal getR184_ORIGINAL_AMOUNT() {
		return R184_ORIGINAL_AMOUNT;
	}
	public void setR184_ORIGINAL_AMOUNT(BigDecimal r184_ORIGINAL_AMOUNT) {
		R184_ORIGINAL_AMOUNT = r184_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR184_UTILISATION_OUTSTANDING_BAL() {
		return R184_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR184_UTILISATION_OUTSTANDING_BAL(BigDecimal r184_UTILISATION_OUTSTANDING_BAL) {
		R184_UTILISATION_OUTSTANDING_BAL = r184_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR184_EFFECTIVE_DATE() {
		return R184_EFFECTIVE_DATE;
	}
	public void setR184_EFFECTIVE_DATE(Date r184_EFFECTIVE_DATE) {
		R184_EFFECTIVE_DATE = r184_EFFECTIVE_DATE;
	}
	public String getR184_REPAYMENT_PERIOD() {
		return R184_REPAYMENT_PERIOD;
	}
	public void setR184_REPAYMENT_PERIOD(String r184_REPAYMENT_PERIOD) {
		R184_REPAYMENT_PERIOD = r184_REPAYMENT_PERIOD;
	}
	public String getR184_PERFORMANCE_STATUS() {
		return R184_PERFORMANCE_STATUS;
	}
	public void setR184_PERFORMANCE_STATUS(String r184_PERFORMANCE_STATUS) {
		R184_PERFORMANCE_STATUS = r184_PERFORMANCE_STATUS;
	}
	public String getR184_SECURITY() {
		return R184_SECURITY;
	}
	public void setR184_SECURITY(String r184_SECURITY) {
		R184_SECURITY = r184_SECURITY;
	}
	public String getR184_BOARD_APPROVAL() {
		return R184_BOARD_APPROVAL;
	}
	public void setR184_BOARD_APPROVAL(String r184_BOARD_APPROVAL) {
		R184_BOARD_APPROVAL = r184_BOARD_APPROVAL;
	}
	public BigDecimal getR184_INTEREST_RATE() {
		return R184_INTEREST_RATE;
	}
	public void setR184_INTEREST_RATE(BigDecimal r184_INTEREST_RATE) {
		R184_INTEREST_RATE = r184_INTEREST_RATE;
	}
	public BigDecimal getR184_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R184_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR184_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r184_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R184_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r184_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR184_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R184_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR184_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r184_LIMIT_PCT_UNIMPAIRED_CAP) {
		R184_LIMIT_PCT_UNIMPAIRED_CAP = r184_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR185_NO_OF_GROUP() {
		return R185_NO_OF_GROUP;
	}
	public void setR185_NO_OF_GROUP(String r185_NO_OF_GROUP) {
		R185_NO_OF_GROUP = r185_NO_OF_GROUP;
	}
	public String getR185_NO_OF_CUSTOMER() {
		return R185_NO_OF_CUSTOMER;
	}
	public void setR185_NO_OF_CUSTOMER(String r185_NO_OF_CUSTOMER) {
		R185_NO_OF_CUSTOMER = r185_NO_OF_CUSTOMER;
	}
	public String getR185_SECTOR_TYPE() {
		return R185_SECTOR_TYPE;
	}
	public void setR185_SECTOR_TYPE(String r185_SECTOR_TYPE) {
		R185_SECTOR_TYPE = r185_SECTOR_TYPE;
	}
	public String getR185_FACILITY_TYPE() {
		return R185_FACILITY_TYPE;
	}
	public void setR185_FACILITY_TYPE(String r185_FACILITY_TYPE) {
		R185_FACILITY_TYPE = r185_FACILITY_TYPE;
	}
	public BigDecimal getR185_ORIGINAL_AMOUNT() {
		return R185_ORIGINAL_AMOUNT;
	}
	public void setR185_ORIGINAL_AMOUNT(BigDecimal r185_ORIGINAL_AMOUNT) {
		R185_ORIGINAL_AMOUNT = r185_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR185_UTILISATION_OUTSTANDING_BAL() {
		return R185_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR185_UTILISATION_OUTSTANDING_BAL(BigDecimal r185_UTILISATION_OUTSTANDING_BAL) {
		R185_UTILISATION_OUTSTANDING_BAL = r185_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR185_EFFECTIVE_DATE() {
		return R185_EFFECTIVE_DATE;
	}
	public void setR185_EFFECTIVE_DATE(Date r185_EFFECTIVE_DATE) {
		R185_EFFECTIVE_DATE = r185_EFFECTIVE_DATE;
	}
	public String getR185_REPAYMENT_PERIOD() {
		return R185_REPAYMENT_PERIOD;
	}
	public void setR185_REPAYMENT_PERIOD(String r185_REPAYMENT_PERIOD) {
		R185_REPAYMENT_PERIOD = r185_REPAYMENT_PERIOD;
	}
	public String getR185_PERFORMANCE_STATUS() {
		return R185_PERFORMANCE_STATUS;
	}
	public void setR185_PERFORMANCE_STATUS(String r185_PERFORMANCE_STATUS) {
		R185_PERFORMANCE_STATUS = r185_PERFORMANCE_STATUS;
	}
	public String getR185_SECURITY() {
		return R185_SECURITY;
	}
	public void setR185_SECURITY(String r185_SECURITY) {
		R185_SECURITY = r185_SECURITY;
	}
	public String getR185_BOARD_APPROVAL() {
		return R185_BOARD_APPROVAL;
	}
	public void setR185_BOARD_APPROVAL(String r185_BOARD_APPROVAL) {
		R185_BOARD_APPROVAL = r185_BOARD_APPROVAL;
	}
	public BigDecimal getR185_INTEREST_RATE() {
		return R185_INTEREST_RATE;
	}
	public void setR185_INTEREST_RATE(BigDecimal r185_INTEREST_RATE) {
		R185_INTEREST_RATE = r185_INTEREST_RATE;
	}
	public BigDecimal getR185_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R185_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR185_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r185_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R185_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r185_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR185_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R185_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR185_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r185_LIMIT_PCT_UNIMPAIRED_CAP) {
		R185_LIMIT_PCT_UNIMPAIRED_CAP = r185_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR186_NO_OF_GROUP() {
		return R186_NO_OF_GROUP;
	}
	public void setR186_NO_OF_GROUP(String r186_NO_OF_GROUP) {
		R186_NO_OF_GROUP = r186_NO_OF_GROUP;
	}
	public String getR186_NO_OF_CUSTOMER() {
		return R186_NO_OF_CUSTOMER;
	}
	public void setR186_NO_OF_CUSTOMER(String r186_NO_OF_CUSTOMER) {
		R186_NO_OF_CUSTOMER = r186_NO_OF_CUSTOMER;
	}
	public String getR186_SECTOR_TYPE() {
		return R186_SECTOR_TYPE;
	}
	public void setR186_SECTOR_TYPE(String r186_SECTOR_TYPE) {
		R186_SECTOR_TYPE = r186_SECTOR_TYPE;
	}
	public String getR186_FACILITY_TYPE() {
		return R186_FACILITY_TYPE;
	}
	public void setR186_FACILITY_TYPE(String r186_FACILITY_TYPE) {
		R186_FACILITY_TYPE = r186_FACILITY_TYPE;
	}
	public BigDecimal getR186_ORIGINAL_AMOUNT() {
		return R186_ORIGINAL_AMOUNT;
	}
	public void setR186_ORIGINAL_AMOUNT(BigDecimal r186_ORIGINAL_AMOUNT) {
		R186_ORIGINAL_AMOUNT = r186_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR186_UTILISATION_OUTSTANDING_BAL() {
		return R186_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR186_UTILISATION_OUTSTANDING_BAL(BigDecimal r186_UTILISATION_OUTSTANDING_BAL) {
		R186_UTILISATION_OUTSTANDING_BAL = r186_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR186_EFFECTIVE_DATE() {
		return R186_EFFECTIVE_DATE;
	}
	public void setR186_EFFECTIVE_DATE(Date r186_EFFECTIVE_DATE) {
		R186_EFFECTIVE_DATE = r186_EFFECTIVE_DATE;
	}
	public String getR186_REPAYMENT_PERIOD() {
		return R186_REPAYMENT_PERIOD;
	}
	public void setR186_REPAYMENT_PERIOD(String r186_REPAYMENT_PERIOD) {
		R186_REPAYMENT_PERIOD = r186_REPAYMENT_PERIOD;
	}
	public String getR186_PERFORMANCE_STATUS() {
		return R186_PERFORMANCE_STATUS;
	}
	public void setR186_PERFORMANCE_STATUS(String r186_PERFORMANCE_STATUS) {
		R186_PERFORMANCE_STATUS = r186_PERFORMANCE_STATUS;
	}
	public String getR186_SECURITY() {
		return R186_SECURITY;
	}
	public void setR186_SECURITY(String r186_SECURITY) {
		R186_SECURITY = r186_SECURITY;
	}
	public String getR186_BOARD_APPROVAL() {
		return R186_BOARD_APPROVAL;
	}
	public void setR186_BOARD_APPROVAL(String r186_BOARD_APPROVAL) {
		R186_BOARD_APPROVAL = r186_BOARD_APPROVAL;
	}
	public BigDecimal getR186_INTEREST_RATE() {
		return R186_INTEREST_RATE;
	}
	public void setR186_INTEREST_RATE(BigDecimal r186_INTEREST_RATE) {
		R186_INTEREST_RATE = r186_INTEREST_RATE;
	}
	public BigDecimal getR186_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R186_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR186_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r186_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R186_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r186_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR186_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R186_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR186_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r186_LIMIT_PCT_UNIMPAIRED_CAP) {
		R186_LIMIT_PCT_UNIMPAIRED_CAP = r186_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR187_NO_OF_GROUP() {
		return R187_NO_OF_GROUP;
	}
	public void setR187_NO_OF_GROUP(String r187_NO_OF_GROUP) {
		R187_NO_OF_GROUP = r187_NO_OF_GROUP;
	}
	public String getR187_NO_OF_CUSTOMER() {
		return R187_NO_OF_CUSTOMER;
	}
	public void setR187_NO_OF_CUSTOMER(String r187_NO_OF_CUSTOMER) {
		R187_NO_OF_CUSTOMER = r187_NO_OF_CUSTOMER;
	}
	public String getR187_SECTOR_TYPE() {
		return R187_SECTOR_TYPE;
	}
	public void setR187_SECTOR_TYPE(String r187_SECTOR_TYPE) {
		R187_SECTOR_TYPE = r187_SECTOR_TYPE;
	}
	public String getR187_FACILITY_TYPE() {
		return R187_FACILITY_TYPE;
	}
	public void setR187_FACILITY_TYPE(String r187_FACILITY_TYPE) {
		R187_FACILITY_TYPE = r187_FACILITY_TYPE;
	}
	public BigDecimal getR187_ORIGINAL_AMOUNT() {
		return R187_ORIGINAL_AMOUNT;
	}
	public void setR187_ORIGINAL_AMOUNT(BigDecimal r187_ORIGINAL_AMOUNT) {
		R187_ORIGINAL_AMOUNT = r187_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR187_UTILISATION_OUTSTANDING_BAL() {
		return R187_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR187_UTILISATION_OUTSTANDING_BAL(BigDecimal r187_UTILISATION_OUTSTANDING_BAL) {
		R187_UTILISATION_OUTSTANDING_BAL = r187_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR187_EFFECTIVE_DATE() {
		return R187_EFFECTIVE_DATE;
	}
	public void setR187_EFFECTIVE_DATE(Date r187_EFFECTIVE_DATE) {
		R187_EFFECTIVE_DATE = r187_EFFECTIVE_DATE;
	}
	public String getR187_REPAYMENT_PERIOD() {
		return R187_REPAYMENT_PERIOD;
	}
	public void setR187_REPAYMENT_PERIOD(String r187_REPAYMENT_PERIOD) {
		R187_REPAYMENT_PERIOD = r187_REPAYMENT_PERIOD;
	}
	public String getR187_PERFORMANCE_STATUS() {
		return R187_PERFORMANCE_STATUS;
	}
	public void setR187_PERFORMANCE_STATUS(String r187_PERFORMANCE_STATUS) {
		R187_PERFORMANCE_STATUS = r187_PERFORMANCE_STATUS;
	}
	public String getR187_SECURITY() {
		return R187_SECURITY;
	}
	public void setR187_SECURITY(String r187_SECURITY) {
		R187_SECURITY = r187_SECURITY;
	}
	public String getR187_BOARD_APPROVAL() {
		return R187_BOARD_APPROVAL;
	}
	public void setR187_BOARD_APPROVAL(String r187_BOARD_APPROVAL) {
		R187_BOARD_APPROVAL = r187_BOARD_APPROVAL;
	}
	public BigDecimal getR187_INTEREST_RATE() {
		return R187_INTEREST_RATE;
	}
	public void setR187_INTEREST_RATE(BigDecimal r187_INTEREST_RATE) {
		R187_INTEREST_RATE = r187_INTEREST_RATE;
	}
	public BigDecimal getR187_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R187_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR187_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r187_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R187_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r187_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR187_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R187_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR187_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r187_LIMIT_PCT_UNIMPAIRED_CAP) {
		R187_LIMIT_PCT_UNIMPAIRED_CAP = r187_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR188_NO_OF_GROUP() {
		return R188_NO_OF_GROUP;
	}
	public void setR188_NO_OF_GROUP(String r188_NO_OF_GROUP) {
		R188_NO_OF_GROUP = r188_NO_OF_GROUP;
	}
	public String getR188_NO_OF_CUSTOMER() {
		return R188_NO_OF_CUSTOMER;
	}
	public void setR188_NO_OF_CUSTOMER(String r188_NO_OF_CUSTOMER) {
		R188_NO_OF_CUSTOMER = r188_NO_OF_CUSTOMER;
	}
	public String getR188_SECTOR_TYPE() {
		return R188_SECTOR_TYPE;
	}
	public void setR188_SECTOR_TYPE(String r188_SECTOR_TYPE) {
		R188_SECTOR_TYPE = r188_SECTOR_TYPE;
	}
	public String getR188_FACILITY_TYPE() {
		return R188_FACILITY_TYPE;
	}
	public void setR188_FACILITY_TYPE(String r188_FACILITY_TYPE) {
		R188_FACILITY_TYPE = r188_FACILITY_TYPE;
	}
	public BigDecimal getR188_ORIGINAL_AMOUNT() {
		return R188_ORIGINAL_AMOUNT;
	}
	public void setR188_ORIGINAL_AMOUNT(BigDecimal r188_ORIGINAL_AMOUNT) {
		R188_ORIGINAL_AMOUNT = r188_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR188_UTILISATION_OUTSTANDING_BAL() {
		return R188_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR188_UTILISATION_OUTSTANDING_BAL(BigDecimal r188_UTILISATION_OUTSTANDING_BAL) {
		R188_UTILISATION_OUTSTANDING_BAL = r188_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR188_EFFECTIVE_DATE() {
		return R188_EFFECTIVE_DATE;
	}
	public void setR188_EFFECTIVE_DATE(Date r188_EFFECTIVE_DATE) {
		R188_EFFECTIVE_DATE = r188_EFFECTIVE_DATE;
	}
	public String getR188_REPAYMENT_PERIOD() {
		return R188_REPAYMENT_PERIOD;
	}
	public void setR188_REPAYMENT_PERIOD(String r188_REPAYMENT_PERIOD) {
		R188_REPAYMENT_PERIOD = r188_REPAYMENT_PERIOD;
	}
	public String getR188_PERFORMANCE_STATUS() {
		return R188_PERFORMANCE_STATUS;
	}
	public void setR188_PERFORMANCE_STATUS(String r188_PERFORMANCE_STATUS) {
		R188_PERFORMANCE_STATUS = r188_PERFORMANCE_STATUS;
	}
	public String getR188_SECURITY() {
		return R188_SECURITY;
	}
	public void setR188_SECURITY(String r188_SECURITY) {
		R188_SECURITY = r188_SECURITY;
	}
	public String getR188_BOARD_APPROVAL() {
		return R188_BOARD_APPROVAL;
	}
	public void setR188_BOARD_APPROVAL(String r188_BOARD_APPROVAL) {
		R188_BOARD_APPROVAL = r188_BOARD_APPROVAL;
	}
	public BigDecimal getR188_INTEREST_RATE() {
		return R188_INTEREST_RATE;
	}
	public void setR188_INTEREST_RATE(BigDecimal r188_INTEREST_RATE) {
		R188_INTEREST_RATE = r188_INTEREST_RATE;
	}
	public BigDecimal getR188_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R188_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR188_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r188_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R188_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r188_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR188_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R188_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR188_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r188_LIMIT_PCT_UNIMPAIRED_CAP) {
		R188_LIMIT_PCT_UNIMPAIRED_CAP = r188_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR189_NO_OF_GROUP() {
		return R189_NO_OF_GROUP;
	}
	public void setR189_NO_OF_GROUP(String r189_NO_OF_GROUP) {
		R189_NO_OF_GROUP = r189_NO_OF_GROUP;
	}
	public String getR189_NO_OF_CUSTOMER() {
		return R189_NO_OF_CUSTOMER;
	}
	public void setR189_NO_OF_CUSTOMER(String r189_NO_OF_CUSTOMER) {
		R189_NO_OF_CUSTOMER = r189_NO_OF_CUSTOMER;
	}
	public String getR189_SECTOR_TYPE() {
		return R189_SECTOR_TYPE;
	}
	public void setR189_SECTOR_TYPE(String r189_SECTOR_TYPE) {
		R189_SECTOR_TYPE = r189_SECTOR_TYPE;
	}
	public String getR189_FACILITY_TYPE() {
		return R189_FACILITY_TYPE;
	}
	public void setR189_FACILITY_TYPE(String r189_FACILITY_TYPE) {
		R189_FACILITY_TYPE = r189_FACILITY_TYPE;
	}
	public BigDecimal getR189_ORIGINAL_AMOUNT() {
		return R189_ORIGINAL_AMOUNT;
	}
	public void setR189_ORIGINAL_AMOUNT(BigDecimal r189_ORIGINAL_AMOUNT) {
		R189_ORIGINAL_AMOUNT = r189_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR189_UTILISATION_OUTSTANDING_BAL() {
		return R189_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR189_UTILISATION_OUTSTANDING_BAL(BigDecimal r189_UTILISATION_OUTSTANDING_BAL) {
		R189_UTILISATION_OUTSTANDING_BAL = r189_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR189_EFFECTIVE_DATE() {
		return R189_EFFECTIVE_DATE;
	}
	public void setR189_EFFECTIVE_DATE(Date r189_EFFECTIVE_DATE) {
		R189_EFFECTIVE_DATE = r189_EFFECTIVE_DATE;
	}
	public String getR189_REPAYMENT_PERIOD() {
		return R189_REPAYMENT_PERIOD;
	}
	public void setR189_REPAYMENT_PERIOD(String r189_REPAYMENT_PERIOD) {
		R189_REPAYMENT_PERIOD = r189_REPAYMENT_PERIOD;
	}
	public String getR189_PERFORMANCE_STATUS() {
		return R189_PERFORMANCE_STATUS;
	}
	public void setR189_PERFORMANCE_STATUS(String r189_PERFORMANCE_STATUS) {
		R189_PERFORMANCE_STATUS = r189_PERFORMANCE_STATUS;
	}
	public String getR189_SECURITY() {
		return R189_SECURITY;
	}
	public void setR189_SECURITY(String r189_SECURITY) {
		R189_SECURITY = r189_SECURITY;
	}
	public String getR189_BOARD_APPROVAL() {
		return R189_BOARD_APPROVAL;
	}
	public void setR189_BOARD_APPROVAL(String r189_BOARD_APPROVAL) {
		R189_BOARD_APPROVAL = r189_BOARD_APPROVAL;
	}
	public BigDecimal getR189_INTEREST_RATE() {
		return R189_INTEREST_RATE;
	}
	public void setR189_INTEREST_RATE(BigDecimal r189_INTEREST_RATE) {
		R189_INTEREST_RATE = r189_INTEREST_RATE;
	}
	public BigDecimal getR189_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R189_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR189_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r189_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R189_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r189_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR189_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R189_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR189_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r189_LIMIT_PCT_UNIMPAIRED_CAP) {
		R189_LIMIT_PCT_UNIMPAIRED_CAP = r189_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR190_NO_OF_GROUP() {
		return R190_NO_OF_GROUP;
	}
	public void setR190_NO_OF_GROUP(String r190_NO_OF_GROUP) {
		R190_NO_OF_GROUP = r190_NO_OF_GROUP;
	}
	public String getR190_NO_OF_CUSTOMER() {
		return R190_NO_OF_CUSTOMER;
	}
	public void setR190_NO_OF_CUSTOMER(String r190_NO_OF_CUSTOMER) {
		R190_NO_OF_CUSTOMER = r190_NO_OF_CUSTOMER;
	}
	public String getR190_SECTOR_TYPE() {
		return R190_SECTOR_TYPE;
	}
	public void setR190_SECTOR_TYPE(String r190_SECTOR_TYPE) {
		R190_SECTOR_TYPE = r190_SECTOR_TYPE;
	}
	public String getR190_FACILITY_TYPE() {
		return R190_FACILITY_TYPE;
	}
	public void setR190_FACILITY_TYPE(String r190_FACILITY_TYPE) {
		R190_FACILITY_TYPE = r190_FACILITY_TYPE;
	}
	public BigDecimal getR190_ORIGINAL_AMOUNT() {
		return R190_ORIGINAL_AMOUNT;
	}
	public void setR190_ORIGINAL_AMOUNT(BigDecimal r190_ORIGINAL_AMOUNT) {
		R190_ORIGINAL_AMOUNT = r190_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR190_UTILISATION_OUTSTANDING_BAL() {
		return R190_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR190_UTILISATION_OUTSTANDING_BAL(BigDecimal r190_UTILISATION_OUTSTANDING_BAL) {
		R190_UTILISATION_OUTSTANDING_BAL = r190_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR190_EFFECTIVE_DATE() {
		return R190_EFFECTIVE_DATE;
	}
	public void setR190_EFFECTIVE_DATE(Date r190_EFFECTIVE_DATE) {
		R190_EFFECTIVE_DATE = r190_EFFECTIVE_DATE;
	}
	public String getR190_REPAYMENT_PERIOD() {
		return R190_REPAYMENT_PERIOD;
	}
	public void setR190_REPAYMENT_PERIOD(String r190_REPAYMENT_PERIOD) {
		R190_REPAYMENT_PERIOD = r190_REPAYMENT_PERIOD;
	}
	public String getR190_PERFORMANCE_STATUS() {
		return R190_PERFORMANCE_STATUS;
	}
	public void setR190_PERFORMANCE_STATUS(String r190_PERFORMANCE_STATUS) {
		R190_PERFORMANCE_STATUS = r190_PERFORMANCE_STATUS;
	}
	public String getR190_SECURITY() {
		return R190_SECURITY;
	}
	public void setR190_SECURITY(String r190_SECURITY) {
		R190_SECURITY = r190_SECURITY;
	}
	public String getR190_BOARD_APPROVAL() {
		return R190_BOARD_APPROVAL;
	}
	public void setR190_BOARD_APPROVAL(String r190_BOARD_APPROVAL) {
		R190_BOARD_APPROVAL = r190_BOARD_APPROVAL;
	}
	public BigDecimal getR190_INTEREST_RATE() {
		return R190_INTEREST_RATE;
	}
	public void setR190_INTEREST_RATE(BigDecimal r190_INTEREST_RATE) {
		R190_INTEREST_RATE = r190_INTEREST_RATE;
	}
	public BigDecimal getR190_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R190_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR190_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r190_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R190_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r190_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR190_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R190_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR190_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r190_LIMIT_PCT_UNIMPAIRED_CAP) {
		R190_LIMIT_PCT_UNIMPAIRED_CAP = r190_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR191_NO_OF_GROUP() {
		return R191_NO_OF_GROUP;
	}
	public void setR191_NO_OF_GROUP(String r191_NO_OF_GROUP) {
		R191_NO_OF_GROUP = r191_NO_OF_GROUP;
	}
	public String getR191_NO_OF_CUSTOMER() {
		return R191_NO_OF_CUSTOMER;
	}
	public void setR191_NO_OF_CUSTOMER(String r191_NO_OF_CUSTOMER) {
		R191_NO_OF_CUSTOMER = r191_NO_OF_CUSTOMER;
	}
	public String getR191_SECTOR_TYPE() {
		return R191_SECTOR_TYPE;
	}
	public void setR191_SECTOR_TYPE(String r191_SECTOR_TYPE) {
		R191_SECTOR_TYPE = r191_SECTOR_TYPE;
	}
	public String getR191_FACILITY_TYPE() {
		return R191_FACILITY_TYPE;
	}
	public void setR191_FACILITY_TYPE(String r191_FACILITY_TYPE) {
		R191_FACILITY_TYPE = r191_FACILITY_TYPE;
	}
	public BigDecimal getR191_ORIGINAL_AMOUNT() {
		return R191_ORIGINAL_AMOUNT;
	}
	public void setR191_ORIGINAL_AMOUNT(BigDecimal r191_ORIGINAL_AMOUNT) {
		R191_ORIGINAL_AMOUNT = r191_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR191_UTILISATION_OUTSTANDING_BAL() {
		return R191_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR191_UTILISATION_OUTSTANDING_BAL(BigDecimal r191_UTILISATION_OUTSTANDING_BAL) {
		R191_UTILISATION_OUTSTANDING_BAL = r191_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR191_EFFECTIVE_DATE() {
		return R191_EFFECTIVE_DATE;
	}
	public void setR191_EFFECTIVE_DATE(Date r191_EFFECTIVE_DATE) {
		R191_EFFECTIVE_DATE = r191_EFFECTIVE_DATE;
	}
	public String getR191_REPAYMENT_PERIOD() {
		return R191_REPAYMENT_PERIOD;
	}
	public void setR191_REPAYMENT_PERIOD(String r191_REPAYMENT_PERIOD) {
		R191_REPAYMENT_PERIOD = r191_REPAYMENT_PERIOD;
	}
	public String getR191_PERFORMANCE_STATUS() {
		return R191_PERFORMANCE_STATUS;
	}
	public void setR191_PERFORMANCE_STATUS(String r191_PERFORMANCE_STATUS) {
		R191_PERFORMANCE_STATUS = r191_PERFORMANCE_STATUS;
	}
	public String getR191_SECURITY() {
		return R191_SECURITY;
	}
	public void setR191_SECURITY(String r191_SECURITY) {
		R191_SECURITY = r191_SECURITY;
	}
	public String getR191_BOARD_APPROVAL() {
		return R191_BOARD_APPROVAL;
	}
	public void setR191_BOARD_APPROVAL(String r191_BOARD_APPROVAL) {
		R191_BOARD_APPROVAL = r191_BOARD_APPROVAL;
	}
	public BigDecimal getR191_INTEREST_RATE() {
		return R191_INTEREST_RATE;
	}
	public void setR191_INTEREST_RATE(BigDecimal r191_INTEREST_RATE) {
		R191_INTEREST_RATE = r191_INTEREST_RATE;
	}
	public BigDecimal getR191_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R191_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR191_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r191_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R191_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r191_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR191_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R191_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR191_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r191_LIMIT_PCT_UNIMPAIRED_CAP) {
		R191_LIMIT_PCT_UNIMPAIRED_CAP = r191_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR192_NO_OF_GROUP() {
		return R192_NO_OF_GROUP;
	}
	public void setR192_NO_OF_GROUP(String r192_NO_OF_GROUP) {
		R192_NO_OF_GROUP = r192_NO_OF_GROUP;
	}
	public String getR192_NO_OF_CUSTOMER() {
		return R192_NO_OF_CUSTOMER;
	}
	public void setR192_NO_OF_CUSTOMER(String r192_NO_OF_CUSTOMER) {
		R192_NO_OF_CUSTOMER = r192_NO_OF_CUSTOMER;
	}
	public String getR192_SECTOR_TYPE() {
		return R192_SECTOR_TYPE;
	}
	public void setR192_SECTOR_TYPE(String r192_SECTOR_TYPE) {
		R192_SECTOR_TYPE = r192_SECTOR_TYPE;
	}
	public String getR192_FACILITY_TYPE() {
		return R192_FACILITY_TYPE;
	}
	public void setR192_FACILITY_TYPE(String r192_FACILITY_TYPE) {
		R192_FACILITY_TYPE = r192_FACILITY_TYPE;
	}
	public BigDecimal getR192_ORIGINAL_AMOUNT() {
		return R192_ORIGINAL_AMOUNT;
	}
	public void setR192_ORIGINAL_AMOUNT(BigDecimal r192_ORIGINAL_AMOUNT) {
		R192_ORIGINAL_AMOUNT = r192_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR192_UTILISATION_OUTSTANDING_BAL() {
		return R192_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR192_UTILISATION_OUTSTANDING_BAL(BigDecimal r192_UTILISATION_OUTSTANDING_BAL) {
		R192_UTILISATION_OUTSTANDING_BAL = r192_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR192_EFFECTIVE_DATE() {
		return R192_EFFECTIVE_DATE;
	}
	public void setR192_EFFECTIVE_DATE(Date r192_EFFECTIVE_DATE) {
		R192_EFFECTIVE_DATE = r192_EFFECTIVE_DATE;
	}
	public String getR192_REPAYMENT_PERIOD() {
		return R192_REPAYMENT_PERIOD;
	}
	public void setR192_REPAYMENT_PERIOD(String r192_REPAYMENT_PERIOD) {
		R192_REPAYMENT_PERIOD = r192_REPAYMENT_PERIOD;
	}
	public String getR192_PERFORMANCE_STATUS() {
		return R192_PERFORMANCE_STATUS;
	}
	public void setR192_PERFORMANCE_STATUS(String r192_PERFORMANCE_STATUS) {
		R192_PERFORMANCE_STATUS = r192_PERFORMANCE_STATUS;
	}
	public String getR192_SECURITY() {
		return R192_SECURITY;
	}
	public void setR192_SECURITY(String r192_SECURITY) {
		R192_SECURITY = r192_SECURITY;
	}
	public String getR192_BOARD_APPROVAL() {
		return R192_BOARD_APPROVAL;
	}
	public void setR192_BOARD_APPROVAL(String r192_BOARD_APPROVAL) {
		R192_BOARD_APPROVAL = r192_BOARD_APPROVAL;
	}
	public BigDecimal getR192_INTEREST_RATE() {
		return R192_INTEREST_RATE;
	}
	public void setR192_INTEREST_RATE(BigDecimal r192_INTEREST_RATE) {
		R192_INTEREST_RATE = r192_INTEREST_RATE;
	}
	public BigDecimal getR192_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R192_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR192_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r192_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R192_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r192_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR192_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R192_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR192_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r192_LIMIT_PCT_UNIMPAIRED_CAP) {
		R192_LIMIT_PCT_UNIMPAIRED_CAP = r192_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR193_NO_OF_GROUP() {
		return R193_NO_OF_GROUP;
	}
	public void setR193_NO_OF_GROUP(String r193_NO_OF_GROUP) {
		R193_NO_OF_GROUP = r193_NO_OF_GROUP;
	}
	public String getR193_NO_OF_CUSTOMER() {
		return R193_NO_OF_CUSTOMER;
	}
	public void setR193_NO_OF_CUSTOMER(String r193_NO_OF_CUSTOMER) {
		R193_NO_OF_CUSTOMER = r193_NO_OF_CUSTOMER;
	}
	public String getR193_SECTOR_TYPE() {
		return R193_SECTOR_TYPE;
	}
	public void setR193_SECTOR_TYPE(String r193_SECTOR_TYPE) {
		R193_SECTOR_TYPE = r193_SECTOR_TYPE;
	}
	public String getR193_FACILITY_TYPE() {
		return R193_FACILITY_TYPE;
	}
	public void setR193_FACILITY_TYPE(String r193_FACILITY_TYPE) {
		R193_FACILITY_TYPE = r193_FACILITY_TYPE;
	}
	public BigDecimal getR193_ORIGINAL_AMOUNT() {
		return R193_ORIGINAL_AMOUNT;
	}
	public void setR193_ORIGINAL_AMOUNT(BigDecimal r193_ORIGINAL_AMOUNT) {
		R193_ORIGINAL_AMOUNT = r193_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR193_UTILISATION_OUTSTANDING_BAL() {
		return R193_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR193_UTILISATION_OUTSTANDING_BAL(BigDecimal r193_UTILISATION_OUTSTANDING_BAL) {
		R193_UTILISATION_OUTSTANDING_BAL = r193_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR193_EFFECTIVE_DATE() {
		return R193_EFFECTIVE_DATE;
	}
	public void setR193_EFFECTIVE_DATE(Date r193_EFFECTIVE_DATE) {
		R193_EFFECTIVE_DATE = r193_EFFECTIVE_DATE;
	}
	public String getR193_REPAYMENT_PERIOD() {
		return R193_REPAYMENT_PERIOD;
	}
	public void setR193_REPAYMENT_PERIOD(String r193_REPAYMENT_PERIOD) {
		R193_REPAYMENT_PERIOD = r193_REPAYMENT_PERIOD;
	}
	public String getR193_PERFORMANCE_STATUS() {
		return R193_PERFORMANCE_STATUS;
	}
	public void setR193_PERFORMANCE_STATUS(String r193_PERFORMANCE_STATUS) {
		R193_PERFORMANCE_STATUS = r193_PERFORMANCE_STATUS;
	}
	public String getR193_SECURITY() {
		return R193_SECURITY;
	}
	public void setR193_SECURITY(String r193_SECURITY) {
		R193_SECURITY = r193_SECURITY;
	}
	public String getR193_BOARD_APPROVAL() {
		return R193_BOARD_APPROVAL;
	}
	public void setR193_BOARD_APPROVAL(String r193_BOARD_APPROVAL) {
		R193_BOARD_APPROVAL = r193_BOARD_APPROVAL;
	}
	public BigDecimal getR193_INTEREST_RATE() {
		return R193_INTEREST_RATE;
	}
	public void setR193_INTEREST_RATE(BigDecimal r193_INTEREST_RATE) {
		R193_INTEREST_RATE = r193_INTEREST_RATE;
	}
	public BigDecimal getR193_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R193_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR193_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r193_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R193_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r193_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR193_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R193_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR193_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r193_LIMIT_PCT_UNIMPAIRED_CAP) {
		R193_LIMIT_PCT_UNIMPAIRED_CAP = r193_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR194_NO_OF_GROUP() {
		return R194_NO_OF_GROUP;
	}
	public void setR194_NO_OF_GROUP(String r194_NO_OF_GROUP) {
		R194_NO_OF_GROUP = r194_NO_OF_GROUP;
	}
	public String getR194_NO_OF_CUSTOMER() {
		return R194_NO_OF_CUSTOMER;
	}
	public void setR194_NO_OF_CUSTOMER(String r194_NO_OF_CUSTOMER) {
		R194_NO_OF_CUSTOMER = r194_NO_OF_CUSTOMER;
	}
	public String getR194_SECTOR_TYPE() {
		return R194_SECTOR_TYPE;
	}
	public void setR194_SECTOR_TYPE(String r194_SECTOR_TYPE) {
		R194_SECTOR_TYPE = r194_SECTOR_TYPE;
	}
	public String getR194_FACILITY_TYPE() {
		return R194_FACILITY_TYPE;
	}
	public void setR194_FACILITY_TYPE(String r194_FACILITY_TYPE) {
		R194_FACILITY_TYPE = r194_FACILITY_TYPE;
	}
	public BigDecimal getR194_ORIGINAL_AMOUNT() {
		return R194_ORIGINAL_AMOUNT;
	}
	public void setR194_ORIGINAL_AMOUNT(BigDecimal r194_ORIGINAL_AMOUNT) {
		R194_ORIGINAL_AMOUNT = r194_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR194_UTILISATION_OUTSTANDING_BAL() {
		return R194_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR194_UTILISATION_OUTSTANDING_BAL(BigDecimal r194_UTILISATION_OUTSTANDING_BAL) {
		R194_UTILISATION_OUTSTANDING_BAL = r194_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR194_EFFECTIVE_DATE() {
		return R194_EFFECTIVE_DATE;
	}
	public void setR194_EFFECTIVE_DATE(Date r194_EFFECTIVE_DATE) {
		R194_EFFECTIVE_DATE = r194_EFFECTIVE_DATE;
	}
	public String getR194_REPAYMENT_PERIOD() {
		return R194_REPAYMENT_PERIOD;
	}
	public void setR194_REPAYMENT_PERIOD(String r194_REPAYMENT_PERIOD) {
		R194_REPAYMENT_PERIOD = r194_REPAYMENT_PERIOD;
	}
	public String getR194_PERFORMANCE_STATUS() {
		return R194_PERFORMANCE_STATUS;
	}
	public void setR194_PERFORMANCE_STATUS(String r194_PERFORMANCE_STATUS) {
		R194_PERFORMANCE_STATUS = r194_PERFORMANCE_STATUS;
	}
	public String getR194_SECURITY() {
		return R194_SECURITY;
	}
	public void setR194_SECURITY(String r194_SECURITY) {
		R194_SECURITY = r194_SECURITY;
	}
	public String getR194_BOARD_APPROVAL() {
		return R194_BOARD_APPROVAL;
	}
	public void setR194_BOARD_APPROVAL(String r194_BOARD_APPROVAL) {
		R194_BOARD_APPROVAL = r194_BOARD_APPROVAL;
	}
	public BigDecimal getR194_INTEREST_RATE() {
		return R194_INTEREST_RATE;
	}
	public void setR194_INTEREST_RATE(BigDecimal r194_INTEREST_RATE) {
		R194_INTEREST_RATE = r194_INTEREST_RATE;
	}
	public BigDecimal getR194_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R194_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR194_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r194_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R194_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r194_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR194_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R194_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR194_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r194_LIMIT_PCT_UNIMPAIRED_CAP) {
		R194_LIMIT_PCT_UNIMPAIRED_CAP = r194_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR195_NO_OF_GROUP() {
		return R195_NO_OF_GROUP;
	}
	public void setR195_NO_OF_GROUP(String r195_NO_OF_GROUP) {
		R195_NO_OF_GROUP = r195_NO_OF_GROUP;
	}
	public String getR195_NO_OF_CUSTOMER() {
		return R195_NO_OF_CUSTOMER;
	}
	public void setR195_NO_OF_CUSTOMER(String r195_NO_OF_CUSTOMER) {
		R195_NO_OF_CUSTOMER = r195_NO_OF_CUSTOMER;
	}
	public String getR195_SECTOR_TYPE() {
		return R195_SECTOR_TYPE;
	}
	public void setR195_SECTOR_TYPE(String r195_SECTOR_TYPE) {
		R195_SECTOR_TYPE = r195_SECTOR_TYPE;
	}
	public String getR195_FACILITY_TYPE() {
		return R195_FACILITY_TYPE;
	}
	public void setR195_FACILITY_TYPE(String r195_FACILITY_TYPE) {
		R195_FACILITY_TYPE = r195_FACILITY_TYPE;
	}
	public BigDecimal getR195_ORIGINAL_AMOUNT() {
		return R195_ORIGINAL_AMOUNT;
	}
	public void setR195_ORIGINAL_AMOUNT(BigDecimal r195_ORIGINAL_AMOUNT) {
		R195_ORIGINAL_AMOUNT = r195_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR195_UTILISATION_OUTSTANDING_BAL() {
		return R195_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR195_UTILISATION_OUTSTANDING_BAL(BigDecimal r195_UTILISATION_OUTSTANDING_BAL) {
		R195_UTILISATION_OUTSTANDING_BAL = r195_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR195_EFFECTIVE_DATE() {
		return R195_EFFECTIVE_DATE;
	}
	public void setR195_EFFECTIVE_DATE(Date r195_EFFECTIVE_DATE) {
		R195_EFFECTIVE_DATE = r195_EFFECTIVE_DATE;
	}
	public String getR195_REPAYMENT_PERIOD() {
		return R195_REPAYMENT_PERIOD;
	}
	public void setR195_REPAYMENT_PERIOD(String r195_REPAYMENT_PERIOD) {
		R195_REPAYMENT_PERIOD = r195_REPAYMENT_PERIOD;
	}
	public String getR195_PERFORMANCE_STATUS() {
		return R195_PERFORMANCE_STATUS;
	}
	public void setR195_PERFORMANCE_STATUS(String r195_PERFORMANCE_STATUS) {
		R195_PERFORMANCE_STATUS = r195_PERFORMANCE_STATUS;
	}
	public String getR195_SECURITY() {
		return R195_SECURITY;
	}
	public void setR195_SECURITY(String r195_SECURITY) {
		R195_SECURITY = r195_SECURITY;
	}
	public String getR195_BOARD_APPROVAL() {
		return R195_BOARD_APPROVAL;
	}
	public void setR195_BOARD_APPROVAL(String r195_BOARD_APPROVAL) {
		R195_BOARD_APPROVAL = r195_BOARD_APPROVAL;
	}
	public BigDecimal getR195_INTEREST_RATE() {
		return R195_INTEREST_RATE;
	}
	public void setR195_INTEREST_RATE(BigDecimal r195_INTEREST_RATE) {
		R195_INTEREST_RATE = r195_INTEREST_RATE;
	}
	public BigDecimal getR195_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R195_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR195_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r195_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R195_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r195_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR195_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R195_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR195_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r195_LIMIT_PCT_UNIMPAIRED_CAP) {
		R195_LIMIT_PCT_UNIMPAIRED_CAP = r195_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR196_NO_OF_GROUP() {
		return R196_NO_OF_GROUP;
	}
	public void setR196_NO_OF_GROUP(String r196_NO_OF_GROUP) {
		R196_NO_OF_GROUP = r196_NO_OF_GROUP;
	}
	public String getR196_NO_OF_CUSTOMER() {
		return R196_NO_OF_CUSTOMER;
	}
	public void setR196_NO_OF_CUSTOMER(String r196_NO_OF_CUSTOMER) {
		R196_NO_OF_CUSTOMER = r196_NO_OF_CUSTOMER;
	}
	public String getR196_SECTOR_TYPE() {
		return R196_SECTOR_TYPE;
	}
	public void setR196_SECTOR_TYPE(String r196_SECTOR_TYPE) {
		R196_SECTOR_TYPE = r196_SECTOR_TYPE;
	}
	public String getR196_FACILITY_TYPE() {
		return R196_FACILITY_TYPE;
	}
	public void setR196_FACILITY_TYPE(String r196_FACILITY_TYPE) {
		R196_FACILITY_TYPE = r196_FACILITY_TYPE;
	}
	public BigDecimal getR196_ORIGINAL_AMOUNT() {
		return R196_ORIGINAL_AMOUNT;
	}
	public void setR196_ORIGINAL_AMOUNT(BigDecimal r196_ORIGINAL_AMOUNT) {
		R196_ORIGINAL_AMOUNT = r196_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR196_UTILISATION_OUTSTANDING_BAL() {
		return R196_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR196_UTILISATION_OUTSTANDING_BAL(BigDecimal r196_UTILISATION_OUTSTANDING_BAL) {
		R196_UTILISATION_OUTSTANDING_BAL = r196_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR196_EFFECTIVE_DATE() {
		return R196_EFFECTIVE_DATE;
	}
	public void setR196_EFFECTIVE_DATE(Date r196_EFFECTIVE_DATE) {
		R196_EFFECTIVE_DATE = r196_EFFECTIVE_DATE;
	}
	public String getR196_REPAYMENT_PERIOD() {
		return R196_REPAYMENT_PERIOD;
	}
	public void setR196_REPAYMENT_PERIOD(String r196_REPAYMENT_PERIOD) {
		R196_REPAYMENT_PERIOD = r196_REPAYMENT_PERIOD;
	}
	public String getR196_PERFORMANCE_STATUS() {
		return R196_PERFORMANCE_STATUS;
	}
	public void setR196_PERFORMANCE_STATUS(String r196_PERFORMANCE_STATUS) {
		R196_PERFORMANCE_STATUS = r196_PERFORMANCE_STATUS;
	}
	public String getR196_SECURITY() {
		return R196_SECURITY;
	}
	public void setR196_SECURITY(String r196_SECURITY) {
		R196_SECURITY = r196_SECURITY;
	}
	public String getR196_BOARD_APPROVAL() {
		return R196_BOARD_APPROVAL;
	}
	public void setR196_BOARD_APPROVAL(String r196_BOARD_APPROVAL) {
		R196_BOARD_APPROVAL = r196_BOARD_APPROVAL;
	}
	public BigDecimal getR196_INTEREST_RATE() {
		return R196_INTEREST_RATE;
	}
	public void setR196_INTEREST_RATE(BigDecimal r196_INTEREST_RATE) {
		R196_INTEREST_RATE = r196_INTEREST_RATE;
	}
	public BigDecimal getR196_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R196_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR196_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r196_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R196_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r196_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR196_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R196_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR196_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r196_LIMIT_PCT_UNIMPAIRED_CAP) {
		R196_LIMIT_PCT_UNIMPAIRED_CAP = r196_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR197_NO_OF_GROUP() {
		return R197_NO_OF_GROUP;
	}
	public void setR197_NO_OF_GROUP(String r197_NO_OF_GROUP) {
		R197_NO_OF_GROUP = r197_NO_OF_GROUP;
	}
	public String getR197_NO_OF_CUSTOMER() {
		return R197_NO_OF_CUSTOMER;
	}
	public void setR197_NO_OF_CUSTOMER(String r197_NO_OF_CUSTOMER) {
		R197_NO_OF_CUSTOMER = r197_NO_OF_CUSTOMER;
	}
	public String getR197_SECTOR_TYPE() {
		return R197_SECTOR_TYPE;
	}
	public void setR197_SECTOR_TYPE(String r197_SECTOR_TYPE) {
		R197_SECTOR_TYPE = r197_SECTOR_TYPE;
	}
	public String getR197_FACILITY_TYPE() {
		return R197_FACILITY_TYPE;
	}
	public void setR197_FACILITY_TYPE(String r197_FACILITY_TYPE) {
		R197_FACILITY_TYPE = r197_FACILITY_TYPE;
	}
	public BigDecimal getR197_ORIGINAL_AMOUNT() {
		return R197_ORIGINAL_AMOUNT;
	}
	public void setR197_ORIGINAL_AMOUNT(BigDecimal r197_ORIGINAL_AMOUNT) {
		R197_ORIGINAL_AMOUNT = r197_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR197_UTILISATION_OUTSTANDING_BAL() {
		return R197_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR197_UTILISATION_OUTSTANDING_BAL(BigDecimal r197_UTILISATION_OUTSTANDING_BAL) {
		R197_UTILISATION_OUTSTANDING_BAL = r197_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR197_EFFECTIVE_DATE() {
		return R197_EFFECTIVE_DATE;
	}
	public void setR197_EFFECTIVE_DATE(Date r197_EFFECTIVE_DATE) {
		R197_EFFECTIVE_DATE = r197_EFFECTIVE_DATE;
	}
	public String getR197_REPAYMENT_PERIOD() {
		return R197_REPAYMENT_PERIOD;
	}
	public void setR197_REPAYMENT_PERIOD(String r197_REPAYMENT_PERIOD) {
		R197_REPAYMENT_PERIOD = r197_REPAYMENT_PERIOD;
	}
	public String getR197_PERFORMANCE_STATUS() {
		return R197_PERFORMANCE_STATUS;
	}
	public void setR197_PERFORMANCE_STATUS(String r197_PERFORMANCE_STATUS) {
		R197_PERFORMANCE_STATUS = r197_PERFORMANCE_STATUS;
	}
	public String getR197_SECURITY() {
		return R197_SECURITY;
	}
	public void setR197_SECURITY(String r197_SECURITY) {
		R197_SECURITY = r197_SECURITY;
	}
	public String getR197_BOARD_APPROVAL() {
		return R197_BOARD_APPROVAL;
	}
	public void setR197_BOARD_APPROVAL(String r197_BOARD_APPROVAL) {
		R197_BOARD_APPROVAL = r197_BOARD_APPROVAL;
	}
	public BigDecimal getR197_INTEREST_RATE() {
		return R197_INTEREST_RATE;
	}
	public void setR197_INTEREST_RATE(BigDecimal r197_INTEREST_RATE) {
		R197_INTEREST_RATE = r197_INTEREST_RATE;
	}
	public BigDecimal getR197_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R197_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR197_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r197_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R197_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r197_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR197_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R197_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR197_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r197_LIMIT_PCT_UNIMPAIRED_CAP) {
		R197_LIMIT_PCT_UNIMPAIRED_CAP = r197_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR198_NO_OF_GROUP() {
		return R198_NO_OF_GROUP;
	}
	public void setR198_NO_OF_GROUP(String r198_NO_OF_GROUP) {
		R198_NO_OF_GROUP = r198_NO_OF_GROUP;
	}
	public String getR198_NO_OF_CUSTOMER() {
		return R198_NO_OF_CUSTOMER;
	}
	public void setR198_NO_OF_CUSTOMER(String r198_NO_OF_CUSTOMER) {
		R198_NO_OF_CUSTOMER = r198_NO_OF_CUSTOMER;
	}
	public String getR198_SECTOR_TYPE() {
		return R198_SECTOR_TYPE;
	}
	public void setR198_SECTOR_TYPE(String r198_SECTOR_TYPE) {
		R198_SECTOR_TYPE = r198_SECTOR_TYPE;
	}
	public String getR198_FACILITY_TYPE() {
		return R198_FACILITY_TYPE;
	}
	public void setR198_FACILITY_TYPE(String r198_FACILITY_TYPE) {
		R198_FACILITY_TYPE = r198_FACILITY_TYPE;
	}
	public BigDecimal getR198_ORIGINAL_AMOUNT() {
		return R198_ORIGINAL_AMOUNT;
	}
	public void setR198_ORIGINAL_AMOUNT(BigDecimal r198_ORIGINAL_AMOUNT) {
		R198_ORIGINAL_AMOUNT = r198_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR198_UTILISATION_OUTSTANDING_BAL() {
		return R198_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR198_UTILISATION_OUTSTANDING_BAL(BigDecimal r198_UTILISATION_OUTSTANDING_BAL) {
		R198_UTILISATION_OUTSTANDING_BAL = r198_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR198_EFFECTIVE_DATE() {
		return R198_EFFECTIVE_DATE;
	}
	public void setR198_EFFECTIVE_DATE(Date r198_EFFECTIVE_DATE) {
		R198_EFFECTIVE_DATE = r198_EFFECTIVE_DATE;
	}
	public String getR198_REPAYMENT_PERIOD() {
		return R198_REPAYMENT_PERIOD;
	}
	public void setR198_REPAYMENT_PERIOD(String r198_REPAYMENT_PERIOD) {
		R198_REPAYMENT_PERIOD = r198_REPAYMENT_PERIOD;
	}
	public String getR198_PERFORMANCE_STATUS() {
		return R198_PERFORMANCE_STATUS;
	}
	public void setR198_PERFORMANCE_STATUS(String r198_PERFORMANCE_STATUS) {
		R198_PERFORMANCE_STATUS = r198_PERFORMANCE_STATUS;
	}
	public String getR198_SECURITY() {
		return R198_SECURITY;
	}
	public void setR198_SECURITY(String r198_SECURITY) {
		R198_SECURITY = r198_SECURITY;
	}
	public String getR198_BOARD_APPROVAL() {
		return R198_BOARD_APPROVAL;
	}
	public void setR198_BOARD_APPROVAL(String r198_BOARD_APPROVAL) {
		R198_BOARD_APPROVAL = r198_BOARD_APPROVAL;
	}
	public BigDecimal getR198_INTEREST_RATE() {
		return R198_INTEREST_RATE;
	}
	public void setR198_INTEREST_RATE(BigDecimal r198_INTEREST_RATE) {
		R198_INTEREST_RATE = r198_INTEREST_RATE;
	}
	public BigDecimal getR198_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R198_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR198_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r198_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R198_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r198_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR198_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R198_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR198_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r198_LIMIT_PCT_UNIMPAIRED_CAP) {
		R198_LIMIT_PCT_UNIMPAIRED_CAP = r198_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR199_NO_OF_GROUP() {
		return R199_NO_OF_GROUP;
	}
	public void setR199_NO_OF_GROUP(String r199_NO_OF_GROUP) {
		R199_NO_OF_GROUP = r199_NO_OF_GROUP;
	}
	public String getR199_NO_OF_CUSTOMER() {
		return R199_NO_OF_CUSTOMER;
	}
	public void setR199_NO_OF_CUSTOMER(String r199_NO_OF_CUSTOMER) {
		R199_NO_OF_CUSTOMER = r199_NO_OF_CUSTOMER;
	}
	public String getR199_SECTOR_TYPE() {
		return R199_SECTOR_TYPE;
	}
	public void setR199_SECTOR_TYPE(String r199_SECTOR_TYPE) {
		R199_SECTOR_TYPE = r199_SECTOR_TYPE;
	}
	public String getR199_FACILITY_TYPE() {
		return R199_FACILITY_TYPE;
	}
	public void setR199_FACILITY_TYPE(String r199_FACILITY_TYPE) {
		R199_FACILITY_TYPE = r199_FACILITY_TYPE;
	}
	public BigDecimal getR199_ORIGINAL_AMOUNT() {
		return R199_ORIGINAL_AMOUNT;
	}
	public void setR199_ORIGINAL_AMOUNT(BigDecimal r199_ORIGINAL_AMOUNT) {
		R199_ORIGINAL_AMOUNT = r199_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR199_UTILISATION_OUTSTANDING_BAL() {
		return R199_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR199_UTILISATION_OUTSTANDING_BAL(BigDecimal r199_UTILISATION_OUTSTANDING_BAL) {
		R199_UTILISATION_OUTSTANDING_BAL = r199_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR199_EFFECTIVE_DATE() {
		return R199_EFFECTIVE_DATE;
	}
	public void setR199_EFFECTIVE_DATE(Date r199_EFFECTIVE_DATE) {
		R199_EFFECTIVE_DATE = r199_EFFECTIVE_DATE;
	}
	public String getR199_REPAYMENT_PERIOD() {
		return R199_REPAYMENT_PERIOD;
	}
	public void setR199_REPAYMENT_PERIOD(String r199_REPAYMENT_PERIOD) {
		R199_REPAYMENT_PERIOD = r199_REPAYMENT_PERIOD;
	}
	public String getR199_PERFORMANCE_STATUS() {
		return R199_PERFORMANCE_STATUS;
	}
	public void setR199_PERFORMANCE_STATUS(String r199_PERFORMANCE_STATUS) {
		R199_PERFORMANCE_STATUS = r199_PERFORMANCE_STATUS;
	}
	public String getR199_SECURITY() {
		return R199_SECURITY;
	}
	public void setR199_SECURITY(String r199_SECURITY) {
		R199_SECURITY = r199_SECURITY;
	}
	public String getR199_BOARD_APPROVAL() {
		return R199_BOARD_APPROVAL;
	}
	public void setR199_BOARD_APPROVAL(String r199_BOARD_APPROVAL) {
		R199_BOARD_APPROVAL = r199_BOARD_APPROVAL;
	}
	public BigDecimal getR199_INTEREST_RATE() {
		return R199_INTEREST_RATE;
	}
	public void setR199_INTEREST_RATE(BigDecimal r199_INTEREST_RATE) {
		R199_INTEREST_RATE = r199_INTEREST_RATE;
	}
	public BigDecimal getR199_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R199_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR199_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r199_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R199_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r199_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR199_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R199_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR199_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r199_LIMIT_PCT_UNIMPAIRED_CAP) {
		R199_LIMIT_PCT_UNIMPAIRED_CAP = r199_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR200_NO_OF_GROUP() {
		return R200_NO_OF_GROUP;
	}
	public void setR200_NO_OF_GROUP(String r200_NO_OF_GROUP) {
		R200_NO_OF_GROUP = r200_NO_OF_GROUP;
	}
	public String getR200_NO_OF_CUSTOMER() {
		return R200_NO_OF_CUSTOMER;
	}
	public void setR200_NO_OF_CUSTOMER(String r200_NO_OF_CUSTOMER) {
		R200_NO_OF_CUSTOMER = r200_NO_OF_CUSTOMER;
	}
	public String getR200_SECTOR_TYPE() {
		return R200_SECTOR_TYPE;
	}
	public void setR200_SECTOR_TYPE(String r200_SECTOR_TYPE) {
		R200_SECTOR_TYPE = r200_SECTOR_TYPE;
	}
	public String getR200_FACILITY_TYPE() {
		return R200_FACILITY_TYPE;
	}
	public void setR200_FACILITY_TYPE(String r200_FACILITY_TYPE) {
		R200_FACILITY_TYPE = r200_FACILITY_TYPE;
	}
	public BigDecimal getR200_ORIGINAL_AMOUNT() {
		return R200_ORIGINAL_AMOUNT;
	}
	public void setR200_ORIGINAL_AMOUNT(BigDecimal r200_ORIGINAL_AMOUNT) {
		R200_ORIGINAL_AMOUNT = r200_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR200_UTILISATION_OUTSTANDING_BAL() {
		return R200_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR200_UTILISATION_OUTSTANDING_BAL(BigDecimal r200_UTILISATION_OUTSTANDING_BAL) {
		R200_UTILISATION_OUTSTANDING_BAL = r200_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR200_EFFECTIVE_DATE() {
		return R200_EFFECTIVE_DATE;
	}
	public void setR200_EFFECTIVE_DATE(Date r200_EFFECTIVE_DATE) {
		R200_EFFECTIVE_DATE = r200_EFFECTIVE_DATE;
	}
	public String getR200_REPAYMENT_PERIOD() {
		return R200_REPAYMENT_PERIOD;
	}
	public void setR200_REPAYMENT_PERIOD(String r200_REPAYMENT_PERIOD) {
		R200_REPAYMENT_PERIOD = r200_REPAYMENT_PERIOD;
	}
	public String getR200_PERFORMANCE_STATUS() {
		return R200_PERFORMANCE_STATUS;
	}
	public void setR200_PERFORMANCE_STATUS(String r200_PERFORMANCE_STATUS) {
		R200_PERFORMANCE_STATUS = r200_PERFORMANCE_STATUS;
	}
	public String getR200_SECURITY() {
		return R200_SECURITY;
	}
	public void setR200_SECURITY(String r200_SECURITY) {
		R200_SECURITY = r200_SECURITY;
	}
	public String getR200_BOARD_APPROVAL() {
		return R200_BOARD_APPROVAL;
	}
	public void setR200_BOARD_APPROVAL(String r200_BOARD_APPROVAL) {
		R200_BOARD_APPROVAL = r200_BOARD_APPROVAL;
	}
	public BigDecimal getR200_INTEREST_RATE() {
		return R200_INTEREST_RATE;
	}
	public void setR200_INTEREST_RATE(BigDecimal r200_INTEREST_RATE) {
		R200_INTEREST_RATE = r200_INTEREST_RATE;
	}
	public BigDecimal getR200_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R200_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR200_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r200_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R200_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r200_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR200_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R200_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR200_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r200_LIMIT_PCT_UNIMPAIRED_CAP) {
		R200_LIMIT_PCT_UNIMPAIRED_CAP = r200_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR201_NO_OF_GROUP() {
		return R201_NO_OF_GROUP;
	}
	public void setR201_NO_OF_GROUP(String r201_NO_OF_GROUP) {
		R201_NO_OF_GROUP = r201_NO_OF_GROUP;
	}
	public String getR201_NO_OF_CUSTOMER() {
		return R201_NO_OF_CUSTOMER;
	}
	public void setR201_NO_OF_CUSTOMER(String r201_NO_OF_CUSTOMER) {
		R201_NO_OF_CUSTOMER = r201_NO_OF_CUSTOMER;
	}
	public String getR201_SECTOR_TYPE() {
		return R201_SECTOR_TYPE;
	}
	public void setR201_SECTOR_TYPE(String r201_SECTOR_TYPE) {
		R201_SECTOR_TYPE = r201_SECTOR_TYPE;
	}
	public String getR201_FACILITY_TYPE() {
		return R201_FACILITY_TYPE;
	}
	public void setR201_FACILITY_TYPE(String r201_FACILITY_TYPE) {
		R201_FACILITY_TYPE = r201_FACILITY_TYPE;
	}
	public BigDecimal getR201_ORIGINAL_AMOUNT() {
		return R201_ORIGINAL_AMOUNT;
	}
	public void setR201_ORIGINAL_AMOUNT(BigDecimal r201_ORIGINAL_AMOUNT) {
		R201_ORIGINAL_AMOUNT = r201_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR201_UTILISATION_OUTSTANDING_BAL() {
		return R201_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR201_UTILISATION_OUTSTANDING_BAL(BigDecimal r201_UTILISATION_OUTSTANDING_BAL) {
		R201_UTILISATION_OUTSTANDING_BAL = r201_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR201_EFFECTIVE_DATE() {
		return R201_EFFECTIVE_DATE;
	}
	public void setR201_EFFECTIVE_DATE(Date r201_EFFECTIVE_DATE) {
		R201_EFFECTIVE_DATE = r201_EFFECTIVE_DATE;
	}
	public String getR201_REPAYMENT_PERIOD() {
		return R201_REPAYMENT_PERIOD;
	}
	public void setR201_REPAYMENT_PERIOD(String r201_REPAYMENT_PERIOD) {
		R201_REPAYMENT_PERIOD = r201_REPAYMENT_PERIOD;
	}
	public String getR201_PERFORMANCE_STATUS() {
		return R201_PERFORMANCE_STATUS;
	}
	public void setR201_PERFORMANCE_STATUS(String r201_PERFORMANCE_STATUS) {
		R201_PERFORMANCE_STATUS = r201_PERFORMANCE_STATUS;
	}
	public String getR201_SECURITY() {
		return R201_SECURITY;
	}
	public void setR201_SECURITY(String r201_SECURITY) {
		R201_SECURITY = r201_SECURITY;
	}
	public String getR201_BOARD_APPROVAL() {
		return R201_BOARD_APPROVAL;
	}
	public void setR201_BOARD_APPROVAL(String r201_BOARD_APPROVAL) {
		R201_BOARD_APPROVAL = r201_BOARD_APPROVAL;
	}
	public BigDecimal getR201_INTEREST_RATE() {
		return R201_INTEREST_RATE;
	}
	public void setR201_INTEREST_RATE(BigDecimal r201_INTEREST_RATE) {
		R201_INTEREST_RATE = r201_INTEREST_RATE;
	}
	public BigDecimal getR201_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R201_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR201_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r201_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R201_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r201_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR201_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R201_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR201_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r201_LIMIT_PCT_UNIMPAIRED_CAP) {
		R201_LIMIT_PCT_UNIMPAIRED_CAP = r201_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR202_NO_OF_GROUP() {
		return R202_NO_OF_GROUP;
	}
	public void setR202_NO_OF_GROUP(String r202_NO_OF_GROUP) {
		R202_NO_OF_GROUP = r202_NO_OF_GROUP;
	}
	public String getR202_NO_OF_CUSTOMER() {
		return R202_NO_OF_CUSTOMER;
	}
	public void setR202_NO_OF_CUSTOMER(String r202_NO_OF_CUSTOMER) {
		R202_NO_OF_CUSTOMER = r202_NO_OF_CUSTOMER;
	}
	public String getR202_SECTOR_TYPE() {
		return R202_SECTOR_TYPE;
	}
	public void setR202_SECTOR_TYPE(String r202_SECTOR_TYPE) {
		R202_SECTOR_TYPE = r202_SECTOR_TYPE;
	}
	public String getR202_FACILITY_TYPE() {
		return R202_FACILITY_TYPE;
	}
	public void setR202_FACILITY_TYPE(String r202_FACILITY_TYPE) {
		R202_FACILITY_TYPE = r202_FACILITY_TYPE;
	}
	public BigDecimal getR202_ORIGINAL_AMOUNT() {
		return R202_ORIGINAL_AMOUNT;
	}
	public void setR202_ORIGINAL_AMOUNT(BigDecimal r202_ORIGINAL_AMOUNT) {
		R202_ORIGINAL_AMOUNT = r202_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR202_UTILISATION_OUTSTANDING_BAL() {
		return R202_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR202_UTILISATION_OUTSTANDING_BAL(BigDecimal r202_UTILISATION_OUTSTANDING_BAL) {
		R202_UTILISATION_OUTSTANDING_BAL = r202_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR202_EFFECTIVE_DATE() {
		return R202_EFFECTIVE_DATE;
	}
	public void setR202_EFFECTIVE_DATE(Date r202_EFFECTIVE_DATE) {
		R202_EFFECTIVE_DATE = r202_EFFECTIVE_DATE;
	}
	public String getR202_REPAYMENT_PERIOD() {
		return R202_REPAYMENT_PERIOD;
	}
	public void setR202_REPAYMENT_PERIOD(String r202_REPAYMENT_PERIOD) {
		R202_REPAYMENT_PERIOD = r202_REPAYMENT_PERIOD;
	}
	public String getR202_PERFORMANCE_STATUS() {
		return R202_PERFORMANCE_STATUS;
	}
	public void setR202_PERFORMANCE_STATUS(String r202_PERFORMANCE_STATUS) {
		R202_PERFORMANCE_STATUS = r202_PERFORMANCE_STATUS;
	}
	public String getR202_SECURITY() {
		return R202_SECURITY;
	}
	public void setR202_SECURITY(String r202_SECURITY) {
		R202_SECURITY = r202_SECURITY;
	}
	public String getR202_BOARD_APPROVAL() {
		return R202_BOARD_APPROVAL;
	}
	public void setR202_BOARD_APPROVAL(String r202_BOARD_APPROVAL) {
		R202_BOARD_APPROVAL = r202_BOARD_APPROVAL;
	}
	public BigDecimal getR202_INTEREST_RATE() {
		return R202_INTEREST_RATE;
	}
	public void setR202_INTEREST_RATE(BigDecimal r202_INTEREST_RATE) {
		R202_INTEREST_RATE = r202_INTEREST_RATE;
	}
	public BigDecimal getR202_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R202_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR202_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r202_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R202_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r202_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR202_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R202_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR202_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r202_LIMIT_PCT_UNIMPAIRED_CAP) {
		R202_LIMIT_PCT_UNIMPAIRED_CAP = r202_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR203_NO_OF_GROUP() {
		return R203_NO_OF_GROUP;
	}
	public void setR203_NO_OF_GROUP(String r203_NO_OF_GROUP) {
		R203_NO_OF_GROUP = r203_NO_OF_GROUP;
	}
	public String getR203_NO_OF_CUSTOMER() {
		return R203_NO_OF_CUSTOMER;
	}
	public void setR203_NO_OF_CUSTOMER(String r203_NO_OF_CUSTOMER) {
		R203_NO_OF_CUSTOMER = r203_NO_OF_CUSTOMER;
	}
	public String getR203_SECTOR_TYPE() {
		return R203_SECTOR_TYPE;
	}
	public void setR203_SECTOR_TYPE(String r203_SECTOR_TYPE) {
		R203_SECTOR_TYPE = r203_SECTOR_TYPE;
	}
	public String getR203_FACILITY_TYPE() {
		return R203_FACILITY_TYPE;
	}
	public void setR203_FACILITY_TYPE(String r203_FACILITY_TYPE) {
		R203_FACILITY_TYPE = r203_FACILITY_TYPE;
	}
	public BigDecimal getR203_ORIGINAL_AMOUNT() {
		return R203_ORIGINAL_AMOUNT;
	}
	public void setR203_ORIGINAL_AMOUNT(BigDecimal r203_ORIGINAL_AMOUNT) {
		R203_ORIGINAL_AMOUNT = r203_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR203_UTILISATION_OUTSTANDING_BAL() {
		return R203_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR203_UTILISATION_OUTSTANDING_BAL(BigDecimal r203_UTILISATION_OUTSTANDING_BAL) {
		R203_UTILISATION_OUTSTANDING_BAL = r203_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR203_EFFECTIVE_DATE() {
		return R203_EFFECTIVE_DATE;
	}
	public void setR203_EFFECTIVE_DATE(Date r203_EFFECTIVE_DATE) {
		R203_EFFECTIVE_DATE = r203_EFFECTIVE_DATE;
	}
	public String getR203_REPAYMENT_PERIOD() {
		return R203_REPAYMENT_PERIOD;
	}
	public void setR203_REPAYMENT_PERIOD(String r203_REPAYMENT_PERIOD) {
		R203_REPAYMENT_PERIOD = r203_REPAYMENT_PERIOD;
	}
	public String getR203_PERFORMANCE_STATUS() {
		return R203_PERFORMANCE_STATUS;
	}
	public void setR203_PERFORMANCE_STATUS(String r203_PERFORMANCE_STATUS) {
		R203_PERFORMANCE_STATUS = r203_PERFORMANCE_STATUS;
	}
	public String getR203_SECURITY() {
		return R203_SECURITY;
	}
	public void setR203_SECURITY(String r203_SECURITY) {
		R203_SECURITY = r203_SECURITY;
	}
	public String getR203_BOARD_APPROVAL() {
		return R203_BOARD_APPROVAL;
	}
	public void setR203_BOARD_APPROVAL(String r203_BOARD_APPROVAL) {
		R203_BOARD_APPROVAL = r203_BOARD_APPROVAL;
	}
	public BigDecimal getR203_INTEREST_RATE() {
		return R203_INTEREST_RATE;
	}
	public void setR203_INTEREST_RATE(BigDecimal r203_INTEREST_RATE) {
		R203_INTEREST_RATE = r203_INTEREST_RATE;
	}
	public BigDecimal getR203_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R203_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR203_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r203_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R203_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r203_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR203_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R203_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR203_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r203_LIMIT_PCT_UNIMPAIRED_CAP) {
		R203_LIMIT_PCT_UNIMPAIRED_CAP = r203_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR204_NO_OF_GROUP() {
		return R204_NO_OF_GROUP;
	}
	public void setR204_NO_OF_GROUP(String r204_NO_OF_GROUP) {
		R204_NO_OF_GROUP = r204_NO_OF_GROUP;
	}
	public String getR204_NO_OF_CUSTOMER() {
		return R204_NO_OF_CUSTOMER;
	}
	public void setR204_NO_OF_CUSTOMER(String r204_NO_OF_CUSTOMER) {
		R204_NO_OF_CUSTOMER = r204_NO_OF_CUSTOMER;
	}
	public String getR204_SECTOR_TYPE() {
		return R204_SECTOR_TYPE;
	}
	public void setR204_SECTOR_TYPE(String r204_SECTOR_TYPE) {
		R204_SECTOR_TYPE = r204_SECTOR_TYPE;
	}
	public String getR204_FACILITY_TYPE() {
		return R204_FACILITY_TYPE;
	}
	public void setR204_FACILITY_TYPE(String r204_FACILITY_TYPE) {
		R204_FACILITY_TYPE = r204_FACILITY_TYPE;
	}
	public BigDecimal getR204_ORIGINAL_AMOUNT() {
		return R204_ORIGINAL_AMOUNT;
	}
	public void setR204_ORIGINAL_AMOUNT(BigDecimal r204_ORIGINAL_AMOUNT) {
		R204_ORIGINAL_AMOUNT = r204_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR204_UTILISATION_OUTSTANDING_BAL() {
		return R204_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR204_UTILISATION_OUTSTANDING_BAL(BigDecimal r204_UTILISATION_OUTSTANDING_BAL) {
		R204_UTILISATION_OUTSTANDING_BAL = r204_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR204_EFFECTIVE_DATE() {
		return R204_EFFECTIVE_DATE;
	}
	public void setR204_EFFECTIVE_DATE(Date r204_EFFECTIVE_DATE) {
		R204_EFFECTIVE_DATE = r204_EFFECTIVE_DATE;
	}
	public String getR204_REPAYMENT_PERIOD() {
		return R204_REPAYMENT_PERIOD;
	}
	public void setR204_REPAYMENT_PERIOD(String r204_REPAYMENT_PERIOD) {
		R204_REPAYMENT_PERIOD = r204_REPAYMENT_PERIOD;
	}
	public String getR204_PERFORMANCE_STATUS() {
		return R204_PERFORMANCE_STATUS;
	}
	public void setR204_PERFORMANCE_STATUS(String r204_PERFORMANCE_STATUS) {
		R204_PERFORMANCE_STATUS = r204_PERFORMANCE_STATUS;
	}
	public String getR204_SECURITY() {
		return R204_SECURITY;
	}
	public void setR204_SECURITY(String r204_SECURITY) {
		R204_SECURITY = r204_SECURITY;
	}
	public String getR204_BOARD_APPROVAL() {
		return R204_BOARD_APPROVAL;
	}
	public void setR204_BOARD_APPROVAL(String r204_BOARD_APPROVAL) {
		R204_BOARD_APPROVAL = r204_BOARD_APPROVAL;
	}
	public BigDecimal getR204_INTEREST_RATE() {
		return R204_INTEREST_RATE;
	}
	public void setR204_INTEREST_RATE(BigDecimal r204_INTEREST_RATE) {
		R204_INTEREST_RATE = r204_INTEREST_RATE;
	}
	public BigDecimal getR204_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R204_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR204_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r204_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R204_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r204_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR204_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R204_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR204_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r204_LIMIT_PCT_UNIMPAIRED_CAP) {
		R204_LIMIT_PCT_UNIMPAIRED_CAP = r204_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR205_NO_OF_GROUP() {
		return R205_NO_OF_GROUP;
	}
	public void setR205_NO_OF_GROUP(String r205_NO_OF_GROUP) {
		R205_NO_OF_GROUP = r205_NO_OF_GROUP;
	}
	public String getR205_NO_OF_CUSTOMER() {
		return R205_NO_OF_CUSTOMER;
	}
	public void setR205_NO_OF_CUSTOMER(String r205_NO_OF_CUSTOMER) {
		R205_NO_OF_CUSTOMER = r205_NO_OF_CUSTOMER;
	}
	public String getR205_SECTOR_TYPE() {
		return R205_SECTOR_TYPE;
	}
	public void setR205_SECTOR_TYPE(String r205_SECTOR_TYPE) {
		R205_SECTOR_TYPE = r205_SECTOR_TYPE;
	}
	public String getR205_FACILITY_TYPE() {
		return R205_FACILITY_TYPE;
	}
	public void setR205_FACILITY_TYPE(String r205_FACILITY_TYPE) {
		R205_FACILITY_TYPE = r205_FACILITY_TYPE;
	}
	public BigDecimal getR205_ORIGINAL_AMOUNT() {
		return R205_ORIGINAL_AMOUNT;
	}
	public void setR205_ORIGINAL_AMOUNT(BigDecimal r205_ORIGINAL_AMOUNT) {
		R205_ORIGINAL_AMOUNT = r205_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR205_UTILISATION_OUTSTANDING_BAL() {
		return R205_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR205_UTILISATION_OUTSTANDING_BAL(BigDecimal r205_UTILISATION_OUTSTANDING_BAL) {
		R205_UTILISATION_OUTSTANDING_BAL = r205_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR205_EFFECTIVE_DATE() {
		return R205_EFFECTIVE_DATE;
	}
	public void setR205_EFFECTIVE_DATE(Date r205_EFFECTIVE_DATE) {
		R205_EFFECTIVE_DATE = r205_EFFECTIVE_DATE;
	}
	public String getR205_REPAYMENT_PERIOD() {
		return R205_REPAYMENT_PERIOD;
	}
	public void setR205_REPAYMENT_PERIOD(String r205_REPAYMENT_PERIOD) {
		R205_REPAYMENT_PERIOD = r205_REPAYMENT_PERIOD;
	}
	public String getR205_PERFORMANCE_STATUS() {
		return R205_PERFORMANCE_STATUS;
	}
	public void setR205_PERFORMANCE_STATUS(String r205_PERFORMANCE_STATUS) {
		R205_PERFORMANCE_STATUS = r205_PERFORMANCE_STATUS;
	}
	public String getR205_SECURITY() {
		return R205_SECURITY;
	}
	public void setR205_SECURITY(String r205_SECURITY) {
		R205_SECURITY = r205_SECURITY;
	}
	public String getR205_BOARD_APPROVAL() {
		return R205_BOARD_APPROVAL;
	}
	public void setR205_BOARD_APPROVAL(String r205_BOARD_APPROVAL) {
		R205_BOARD_APPROVAL = r205_BOARD_APPROVAL;
	}
	public BigDecimal getR205_INTEREST_RATE() {
		return R205_INTEREST_RATE;
	}
	public void setR205_INTEREST_RATE(BigDecimal r205_INTEREST_RATE) {
		R205_INTEREST_RATE = r205_INTEREST_RATE;
	}
	public BigDecimal getR205_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R205_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR205_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r205_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R205_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r205_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR205_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R205_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR205_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r205_LIMIT_PCT_UNIMPAIRED_CAP) {
		R205_LIMIT_PCT_UNIMPAIRED_CAP = r205_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR206_NO_OF_GROUP() {
		return R206_NO_OF_GROUP;
	}
	public void setR206_NO_OF_GROUP(String r206_NO_OF_GROUP) {
		R206_NO_OF_GROUP = r206_NO_OF_GROUP;
	}
	public String getR206_NO_OF_CUSTOMER() {
		return R206_NO_OF_CUSTOMER;
	}
	public void setR206_NO_OF_CUSTOMER(String r206_NO_OF_CUSTOMER) {
		R206_NO_OF_CUSTOMER = r206_NO_OF_CUSTOMER;
	}
	public String getR206_SECTOR_TYPE() {
		return R206_SECTOR_TYPE;
	}
	public void setR206_SECTOR_TYPE(String r206_SECTOR_TYPE) {
		R206_SECTOR_TYPE = r206_SECTOR_TYPE;
	}
	public String getR206_FACILITY_TYPE() {
		return R206_FACILITY_TYPE;
	}
	public void setR206_FACILITY_TYPE(String r206_FACILITY_TYPE) {
		R206_FACILITY_TYPE = r206_FACILITY_TYPE;
	}
	public BigDecimal getR206_ORIGINAL_AMOUNT() {
		return R206_ORIGINAL_AMOUNT;
	}
	public void setR206_ORIGINAL_AMOUNT(BigDecimal r206_ORIGINAL_AMOUNT) {
		R206_ORIGINAL_AMOUNT = r206_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR206_UTILISATION_OUTSTANDING_BAL() {
		return R206_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR206_UTILISATION_OUTSTANDING_BAL(BigDecimal r206_UTILISATION_OUTSTANDING_BAL) {
		R206_UTILISATION_OUTSTANDING_BAL = r206_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR206_EFFECTIVE_DATE() {
		return R206_EFFECTIVE_DATE;
	}
	public void setR206_EFFECTIVE_DATE(Date r206_EFFECTIVE_DATE) {
		R206_EFFECTIVE_DATE = r206_EFFECTIVE_DATE;
	}
	public String getR206_REPAYMENT_PERIOD() {
		return R206_REPAYMENT_PERIOD;
	}
	public void setR206_REPAYMENT_PERIOD(String r206_REPAYMENT_PERIOD) {
		R206_REPAYMENT_PERIOD = r206_REPAYMENT_PERIOD;
	}
	public String getR206_PERFORMANCE_STATUS() {
		return R206_PERFORMANCE_STATUS;
	}
	public void setR206_PERFORMANCE_STATUS(String r206_PERFORMANCE_STATUS) {
		R206_PERFORMANCE_STATUS = r206_PERFORMANCE_STATUS;
	}
	public String getR206_SECURITY() {
		return R206_SECURITY;
	}
	public void setR206_SECURITY(String r206_SECURITY) {
		R206_SECURITY = r206_SECURITY;
	}
	public String getR206_BOARD_APPROVAL() {
		return R206_BOARD_APPROVAL;
	}
	public void setR206_BOARD_APPROVAL(String r206_BOARD_APPROVAL) {
		R206_BOARD_APPROVAL = r206_BOARD_APPROVAL;
	}
	public BigDecimal getR206_INTEREST_RATE() {
		return R206_INTEREST_RATE;
	}
	public void setR206_INTEREST_RATE(BigDecimal r206_INTEREST_RATE) {
		R206_INTEREST_RATE = r206_INTEREST_RATE;
	}
	public BigDecimal getR206_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R206_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR206_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r206_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R206_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r206_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR206_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R206_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR206_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r206_LIMIT_PCT_UNIMPAIRED_CAP) {
		R206_LIMIT_PCT_UNIMPAIRED_CAP = r206_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR207_NO_OF_GROUP() {
		return R207_NO_OF_GROUP;
	}
	public void setR207_NO_OF_GROUP(String r207_NO_OF_GROUP) {
		R207_NO_OF_GROUP = r207_NO_OF_GROUP;
	}
	public String getR207_NO_OF_CUSTOMER() {
		return R207_NO_OF_CUSTOMER;
	}
	public void setR207_NO_OF_CUSTOMER(String r207_NO_OF_CUSTOMER) {
		R207_NO_OF_CUSTOMER = r207_NO_OF_CUSTOMER;
	}
	public String getR207_SECTOR_TYPE() {
		return R207_SECTOR_TYPE;
	}
	public void setR207_SECTOR_TYPE(String r207_SECTOR_TYPE) {
		R207_SECTOR_TYPE = r207_SECTOR_TYPE;
	}
	public String getR207_FACILITY_TYPE() {
		return R207_FACILITY_TYPE;
	}
	public void setR207_FACILITY_TYPE(String r207_FACILITY_TYPE) {
		R207_FACILITY_TYPE = r207_FACILITY_TYPE;
	}
	public BigDecimal getR207_ORIGINAL_AMOUNT() {
		return R207_ORIGINAL_AMOUNT;
	}
	public void setR207_ORIGINAL_AMOUNT(BigDecimal r207_ORIGINAL_AMOUNT) {
		R207_ORIGINAL_AMOUNT = r207_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR207_UTILISATION_OUTSTANDING_BAL() {
		return R207_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR207_UTILISATION_OUTSTANDING_BAL(BigDecimal r207_UTILISATION_OUTSTANDING_BAL) {
		R207_UTILISATION_OUTSTANDING_BAL = r207_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR207_EFFECTIVE_DATE() {
		return R207_EFFECTIVE_DATE;
	}
	public void setR207_EFFECTIVE_DATE(Date r207_EFFECTIVE_DATE) {
		R207_EFFECTIVE_DATE = r207_EFFECTIVE_DATE;
	}
	public String getR207_REPAYMENT_PERIOD() {
		return R207_REPAYMENT_PERIOD;
	}
	public void setR207_REPAYMENT_PERIOD(String r207_REPAYMENT_PERIOD) {
		R207_REPAYMENT_PERIOD = r207_REPAYMENT_PERIOD;
	}
	public String getR207_PERFORMANCE_STATUS() {
		return R207_PERFORMANCE_STATUS;
	}
	public void setR207_PERFORMANCE_STATUS(String r207_PERFORMANCE_STATUS) {
		R207_PERFORMANCE_STATUS = r207_PERFORMANCE_STATUS;
	}
	public String getR207_SECURITY() {
		return R207_SECURITY;
	}
	public void setR207_SECURITY(String r207_SECURITY) {
		R207_SECURITY = r207_SECURITY;
	}
	public String getR207_BOARD_APPROVAL() {
		return R207_BOARD_APPROVAL;
	}
	public void setR207_BOARD_APPROVAL(String r207_BOARD_APPROVAL) {
		R207_BOARD_APPROVAL = r207_BOARD_APPROVAL;
	}
	public BigDecimal getR207_INTEREST_RATE() {
		return R207_INTEREST_RATE;
	}
	public void setR207_INTEREST_RATE(BigDecimal r207_INTEREST_RATE) {
		R207_INTEREST_RATE = r207_INTEREST_RATE;
	}
	public BigDecimal getR207_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R207_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR207_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r207_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R207_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r207_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR207_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R207_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR207_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r207_LIMIT_PCT_UNIMPAIRED_CAP) {
		R207_LIMIT_PCT_UNIMPAIRED_CAP = r207_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR208_NO_OF_GROUP() {
		return R208_NO_OF_GROUP;
	}
	public void setR208_NO_OF_GROUP(String r208_NO_OF_GROUP) {
		R208_NO_OF_GROUP = r208_NO_OF_GROUP;
	}
	public String getR208_NO_OF_CUSTOMER() {
		return R208_NO_OF_CUSTOMER;
	}
	public void setR208_NO_OF_CUSTOMER(String r208_NO_OF_CUSTOMER) {
		R208_NO_OF_CUSTOMER = r208_NO_OF_CUSTOMER;
	}
	public String getR208_SECTOR_TYPE() {
		return R208_SECTOR_TYPE;
	}
	public void setR208_SECTOR_TYPE(String r208_SECTOR_TYPE) {
		R208_SECTOR_TYPE = r208_SECTOR_TYPE;
	}
	public String getR208_FACILITY_TYPE() {
		return R208_FACILITY_TYPE;
	}
	public void setR208_FACILITY_TYPE(String r208_FACILITY_TYPE) {
		R208_FACILITY_TYPE = r208_FACILITY_TYPE;
	}
	public BigDecimal getR208_ORIGINAL_AMOUNT() {
		return R208_ORIGINAL_AMOUNT;
	}
	public void setR208_ORIGINAL_AMOUNT(BigDecimal r208_ORIGINAL_AMOUNT) {
		R208_ORIGINAL_AMOUNT = r208_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR208_UTILISATION_OUTSTANDING_BAL() {
		return R208_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR208_UTILISATION_OUTSTANDING_BAL(BigDecimal r208_UTILISATION_OUTSTANDING_BAL) {
		R208_UTILISATION_OUTSTANDING_BAL = r208_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR208_EFFECTIVE_DATE() {
		return R208_EFFECTIVE_DATE;
	}
	public void setR208_EFFECTIVE_DATE(Date r208_EFFECTIVE_DATE) {
		R208_EFFECTIVE_DATE = r208_EFFECTIVE_DATE;
	}
	public String getR208_REPAYMENT_PERIOD() {
		return R208_REPAYMENT_PERIOD;
	}
	public void setR208_REPAYMENT_PERIOD(String r208_REPAYMENT_PERIOD) {
		R208_REPAYMENT_PERIOD = r208_REPAYMENT_PERIOD;
	}
	public String getR208_PERFORMANCE_STATUS() {
		return R208_PERFORMANCE_STATUS;
	}
	public void setR208_PERFORMANCE_STATUS(String r208_PERFORMANCE_STATUS) {
		R208_PERFORMANCE_STATUS = r208_PERFORMANCE_STATUS;
	}
	public String getR208_SECURITY() {
		return R208_SECURITY;
	}
	public void setR208_SECURITY(String r208_SECURITY) {
		R208_SECURITY = r208_SECURITY;
	}
	public String getR208_BOARD_APPROVAL() {
		return R208_BOARD_APPROVAL;
	}
	public void setR208_BOARD_APPROVAL(String r208_BOARD_APPROVAL) {
		R208_BOARD_APPROVAL = r208_BOARD_APPROVAL;
	}
	public BigDecimal getR208_INTEREST_RATE() {
		return R208_INTEREST_RATE;
	}
	public void setR208_INTEREST_RATE(BigDecimal r208_INTEREST_RATE) {
		R208_INTEREST_RATE = r208_INTEREST_RATE;
	}
	public BigDecimal getR208_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R208_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR208_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r208_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R208_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r208_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR208_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R208_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR208_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r208_LIMIT_PCT_UNIMPAIRED_CAP) {
		R208_LIMIT_PCT_UNIMPAIRED_CAP = r208_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR209_NO_OF_GROUP() {
		return R209_NO_OF_GROUP;
	}
	public void setR209_NO_OF_GROUP(String r209_NO_OF_GROUP) {
		R209_NO_OF_GROUP = r209_NO_OF_GROUP;
	}
	public String getR209_NO_OF_CUSTOMER() {
		return R209_NO_OF_CUSTOMER;
	}
	public void setR209_NO_OF_CUSTOMER(String r209_NO_OF_CUSTOMER) {
		R209_NO_OF_CUSTOMER = r209_NO_OF_CUSTOMER;
	}
	public String getR209_SECTOR_TYPE() {
		return R209_SECTOR_TYPE;
	}
	public void setR209_SECTOR_TYPE(String r209_SECTOR_TYPE) {
		R209_SECTOR_TYPE = r209_SECTOR_TYPE;
	}
	public String getR209_FACILITY_TYPE() {
		return R209_FACILITY_TYPE;
	}
	public void setR209_FACILITY_TYPE(String r209_FACILITY_TYPE) {
		R209_FACILITY_TYPE = r209_FACILITY_TYPE;
	}
	public BigDecimal getR209_ORIGINAL_AMOUNT() {
		return R209_ORIGINAL_AMOUNT;
	}
	public void setR209_ORIGINAL_AMOUNT(BigDecimal r209_ORIGINAL_AMOUNT) {
		R209_ORIGINAL_AMOUNT = r209_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR209_UTILISATION_OUTSTANDING_BAL() {
		return R209_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR209_UTILISATION_OUTSTANDING_BAL(BigDecimal r209_UTILISATION_OUTSTANDING_BAL) {
		R209_UTILISATION_OUTSTANDING_BAL = r209_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR209_EFFECTIVE_DATE() {
		return R209_EFFECTIVE_DATE;
	}
	public void setR209_EFFECTIVE_DATE(Date r209_EFFECTIVE_DATE) {
		R209_EFFECTIVE_DATE = r209_EFFECTIVE_DATE;
	}
	public String getR209_REPAYMENT_PERIOD() {
		return R209_REPAYMENT_PERIOD;
	}
	public void setR209_REPAYMENT_PERIOD(String r209_REPAYMENT_PERIOD) {
		R209_REPAYMENT_PERIOD = r209_REPAYMENT_PERIOD;
	}
	public String getR209_PERFORMANCE_STATUS() {
		return R209_PERFORMANCE_STATUS;
	}
	public void setR209_PERFORMANCE_STATUS(String r209_PERFORMANCE_STATUS) {
		R209_PERFORMANCE_STATUS = r209_PERFORMANCE_STATUS;
	}
	public String getR209_SECURITY() {
		return R209_SECURITY;
	}
	public void setR209_SECURITY(String r209_SECURITY) {
		R209_SECURITY = r209_SECURITY;
	}
	public String getR209_BOARD_APPROVAL() {
		return R209_BOARD_APPROVAL;
	}
	public void setR209_BOARD_APPROVAL(String r209_BOARD_APPROVAL) {
		R209_BOARD_APPROVAL = r209_BOARD_APPROVAL;
	}
	public BigDecimal getR209_INTEREST_RATE() {
		return R209_INTEREST_RATE;
	}
	public void setR209_INTEREST_RATE(BigDecimal r209_INTEREST_RATE) {
		R209_INTEREST_RATE = r209_INTEREST_RATE;
	}
	public BigDecimal getR209_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R209_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR209_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r209_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R209_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r209_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR209_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R209_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR209_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r209_LIMIT_PCT_UNIMPAIRED_CAP) {
		R209_LIMIT_PCT_UNIMPAIRED_CAP = r209_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR210_NO_OF_GROUP() {
		return R210_NO_OF_GROUP;
	}
	public void setR210_NO_OF_GROUP(String r210_NO_OF_GROUP) {
		R210_NO_OF_GROUP = r210_NO_OF_GROUP;
	}
	public String getR210_NO_OF_CUSTOMER() {
		return R210_NO_OF_CUSTOMER;
	}
	public void setR210_NO_OF_CUSTOMER(String r210_NO_OF_CUSTOMER) {
		R210_NO_OF_CUSTOMER = r210_NO_OF_CUSTOMER;
	}
	public String getR210_SECTOR_TYPE() {
		return R210_SECTOR_TYPE;
	}
	public void setR210_SECTOR_TYPE(String r210_SECTOR_TYPE) {
		R210_SECTOR_TYPE = r210_SECTOR_TYPE;
	}
	public String getR210_FACILITY_TYPE() {
		return R210_FACILITY_TYPE;
	}
	public void setR210_FACILITY_TYPE(String r210_FACILITY_TYPE) {
		R210_FACILITY_TYPE = r210_FACILITY_TYPE;
	}
	public BigDecimal getR210_ORIGINAL_AMOUNT() {
		return R210_ORIGINAL_AMOUNT;
	}
	public void setR210_ORIGINAL_AMOUNT(BigDecimal r210_ORIGINAL_AMOUNT) {
		R210_ORIGINAL_AMOUNT = r210_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR210_UTILISATION_OUTSTANDING_BAL() {
		return R210_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR210_UTILISATION_OUTSTANDING_BAL(BigDecimal r210_UTILISATION_OUTSTANDING_BAL) {
		R210_UTILISATION_OUTSTANDING_BAL = r210_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR210_EFFECTIVE_DATE() {
		return R210_EFFECTIVE_DATE;
	}
	public void setR210_EFFECTIVE_DATE(Date r210_EFFECTIVE_DATE) {
		R210_EFFECTIVE_DATE = r210_EFFECTIVE_DATE;
	}
	public String getR210_REPAYMENT_PERIOD() {
		return R210_REPAYMENT_PERIOD;
	}
	public void setR210_REPAYMENT_PERIOD(String r210_REPAYMENT_PERIOD) {
		R210_REPAYMENT_PERIOD = r210_REPAYMENT_PERIOD;
	}
	public String getR210_PERFORMANCE_STATUS() {
		return R210_PERFORMANCE_STATUS;
	}
	public void setR210_PERFORMANCE_STATUS(String r210_PERFORMANCE_STATUS) {
		R210_PERFORMANCE_STATUS = r210_PERFORMANCE_STATUS;
	}
	public String getR210_SECURITY() {
		return R210_SECURITY;
	}
	public void setR210_SECURITY(String r210_SECURITY) {
		R210_SECURITY = r210_SECURITY;
	}
	public String getR210_BOARD_APPROVAL() {
		return R210_BOARD_APPROVAL;
	}
	public void setR210_BOARD_APPROVAL(String r210_BOARD_APPROVAL) {
		R210_BOARD_APPROVAL = r210_BOARD_APPROVAL;
	}
	public BigDecimal getR210_INTEREST_RATE() {
		return R210_INTEREST_RATE;
	}
	public void setR210_INTEREST_RATE(BigDecimal r210_INTEREST_RATE) {
		R210_INTEREST_RATE = r210_INTEREST_RATE;
	}
	public BigDecimal getR210_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R210_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR210_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r210_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R210_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r210_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR210_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R210_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR210_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r210_LIMIT_PCT_UNIMPAIRED_CAP) {
		R210_LIMIT_PCT_UNIMPAIRED_CAP = r210_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public BigDecimal getReport_version() {
		return report_version;
	}
	public void setReport_version(BigDecimal report_version) {
		this.report_version = report_version;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public M_LARADV_Archival_Summary_Entity3() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
