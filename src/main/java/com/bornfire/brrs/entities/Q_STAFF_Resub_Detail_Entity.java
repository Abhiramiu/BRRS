package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BRRS_Q_STAFF_RESUB_DETAILTABLE")
@IdClass(Q_STAFF_PK.class)
public class Q_STAFF_Resub_Detail_Entity {

    private String R9_STAFF_COMPLEMENT;
    private BigDecimal R9_LOCAL;
    private BigDecimal R9_EXPARIATES;
    private BigDecimal R9_TOTAL;
    private String R10_STAFF_COMPLEMENT;
    private BigDecimal R10_LOCAL;
    private BigDecimal R10_EXPARIATES;
    private BigDecimal R10_TOTAL;
    private String R11_STAFF_COMPLEMENT;
    private BigDecimal R11_LOCAL;
    private BigDecimal R11_EXPARIATES;
    private BigDecimal R11_TOTAL;
    private String R12_STAFF_COMPLEMENT;
    private BigDecimal R12_LOCAL;
    private BigDecimal R12_EXPARIATES;
    private BigDecimal R12_TOTAL;
    private String R13_STAFF_COMPLEMENT;
    private BigDecimal R13_LOCAL;
    private BigDecimal R13_EXPARIATES;
    private BigDecimal R13_TOTAL;
    private String R14_STAFF_COMPLEMENT;
    private BigDecimal R14_LOCAL;
    private BigDecimal R14_EXPARIATES;
    private BigDecimal R14_TOTAL;
    private String R15_STAFF_COMPLEMENT;
    private BigDecimal R15_LOCAL;
    private BigDecimal R15_EXPARIATES;
    private BigDecimal R15_TOTAL;
    private String R21_SENIOR_MANAGEMENT_COMPENSATION;
    private BigDecimal R21_LOCAL;
    private BigDecimal R21_EXPARIATES;
    private BigDecimal R21_TOTAL;
    private String R22_SENIOR_MANAGEMENT_COMPENSATION;
    private BigDecimal R22_LOCAL;
    private BigDecimal R22_EXPARIATES;
    private BigDecimal R22_TOTAL;
    private String R23_SENIOR_MANAGEMENT_COMPENSATION;
    private BigDecimal R23_LOCAL;
    private BigDecimal R23_EXPARIATES;
    private BigDecimal R23_TOTAL;
    private String R24_SENIOR_MANAGEMENT_COMPENSATION;
    private BigDecimal R24_LOCAL;
    private BigDecimal R24_EXPARIATES;
    private BigDecimal R24_TOTAL;
    private String R25_SENIOR_MANAGEMENT_COMPENSATION;
    private BigDecimal R25_LOCAL;
    private BigDecimal R25_EXPARIATES;
    private BigDecimal R25_TOTAL;
    private String R26_SENIOR_MANAGEMENT_COMPENSATION;
    private BigDecimal R26_LOCAL;
    private BigDecimal R26_EXPARIATES;
    private BigDecimal R26_TOTAL;
    private String R27_SENIOR_MANAGEMENT_COMPENSATION;
    private BigDecimal R27_LOCAL;
    private BigDecimal R27_EXPARIATES;
    private BigDecimal R27_TOTAL;
    private String R28_SENIOR_MANAGEMENT_COMPENSATION;
    private BigDecimal R28_LOCAL;
    private BigDecimal R28_EXPARIATES;
    private BigDecimal R28_TOTAL;
    private String R33_STAFF_LOANS;
    private BigDecimal R33_ORIGINAL_AMT;
    private BigDecimal R33_BALANCE_OUTSTANDING;
    private BigDecimal R33_NO_OF_ACS;
    private BigDecimal R33_INTEREST_RATE;
    private String R34_STAFF_LOANS;
    private BigDecimal R34_ORIGINAL_AMT;
    private BigDecimal R34_BALANCE_OUTSTANDING;
    private BigDecimal R34_NO_OF_ACS;
    private BigDecimal R34_INTEREST_RATE;
    private String R35_STAFF_LOANS;
    private BigDecimal R35_ORIGINAL_AMT;
    private BigDecimal R35_BALANCE_OUTSTANDING;
    private BigDecimal R35_NO_OF_ACS;
    private BigDecimal R35_INTEREST_RATE;
    private String R36_STAFF_LOANS;
    private BigDecimal R36_ORIGINAL_AMT;
    private BigDecimal R36_BALANCE_OUTSTANDING;
    private BigDecimal R36_NO_OF_ACS;
    private BigDecimal R36_INTEREST_RATE;
    private String R37_STAFF_LOANS;
    private BigDecimal R37_ORIGINAL_AMT;
    private BigDecimal R37_BALANCE_OUTSTANDING;
    private BigDecimal R37_NO_OF_ACS;
    private BigDecimal R37_INTEREST_RATE;
    private String R38_STAFF_LOANS;
    private BigDecimal R38_ORIGINAL_AMT;
    private BigDecimal R38_BALANCE_OUTSTANDING;
    private BigDecimal R38_NO_OF_ACS;
    private BigDecimal R38_INTEREST_RATE;
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reportResubDate;
    private String REPORT_FREQUENCY;
    private String REPORT_CODE;
    private String REPORT_DESC;
    private String ENTITY_FLG;
    private String MODIFY_FLG;
    private String DEL_FLG;

    
    public Date getReportResubDate() {
		return reportResubDate;
	}

	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
	}

	public Q_STAFF_Resub_Detail_Entity() {
        super();
    }

    public String getR9_STAFF_COMPLEMENT() {
        return R9_STAFF_COMPLEMENT;
    }

    public void setR9_STAFF_COMPLEMENT(String r9_STAFF_COMPLEMENT) {
        R9_STAFF_COMPLEMENT = r9_STAFF_COMPLEMENT;
    }

    public BigDecimal getR9_LOCAL() {
        return R9_LOCAL;
    }

    public void setR9_LOCAL(BigDecimal r9_LOCAL) {
        R9_LOCAL = r9_LOCAL;
    }

    public BigDecimal getR9_EXPARIATES() {
        return R9_EXPARIATES;
    }

    public void setR9_EXPARIATES(BigDecimal r9_EXPARIATES) {
        R9_EXPARIATES = r9_EXPARIATES;
    }

    public BigDecimal getR9_TOTAL() {
        return R9_TOTAL;
    }

    public void setR9_TOTAL(BigDecimal r9_TOTAL) {
        R9_TOTAL = r9_TOTAL;
    }

    public String getR10_STAFF_COMPLEMENT() {
        return R10_STAFF_COMPLEMENT;
    }

    public void setR10_STAFF_COMPLEMENT(String r10_STAFF_COMPLEMENT) {
        R10_STAFF_COMPLEMENT = r10_STAFF_COMPLEMENT;
    }

    public BigDecimal getR10_LOCAL() {
        return R10_LOCAL;
    }

    public void setR10_LOCAL(BigDecimal r10_LOCAL) {
        R10_LOCAL = r10_LOCAL;
    }

    public BigDecimal getR10_EXPARIATES() {
        return R10_EXPARIATES;
    }

    public void setR10_EXPARIATES(BigDecimal r10_EXPARIATES) {
        R10_EXPARIATES = r10_EXPARIATES;
    }

    public BigDecimal getR10_TOTAL() {
        return R10_TOTAL;
    }

    public void setR10_TOTAL(BigDecimal r10_TOTAL) {
        R10_TOTAL = r10_TOTAL;
    }

    public String getR11_STAFF_COMPLEMENT() {
        return R11_STAFF_COMPLEMENT;
    }

    public void setR11_STAFF_COMPLEMENT(String r11_STAFF_COMPLEMENT) {
        R11_STAFF_COMPLEMENT = r11_STAFF_COMPLEMENT;
    }

    public BigDecimal getR11_LOCAL() {
        return R11_LOCAL;
    }

    public void setR11_LOCAL(BigDecimal r11_LOCAL) {
        R11_LOCAL = r11_LOCAL;
    }

    public BigDecimal getR11_EXPARIATES() {
        return R11_EXPARIATES;
    }

    public void setR11_EXPARIATES(BigDecimal r11_EXPARIATES) {
        R11_EXPARIATES = r11_EXPARIATES;
    }

    public BigDecimal getR11_TOTAL() {
        return R11_TOTAL;
    }

    public void setR11_TOTAL(BigDecimal r11_TOTAL) {
        R11_TOTAL = r11_TOTAL;
    }

    public String getR12_STAFF_COMPLEMENT() {
        return R12_STAFF_COMPLEMENT;
    }

    public void setR12_STAFF_COMPLEMENT(String r12_STAFF_COMPLEMENT) {
        R12_STAFF_COMPLEMENT = r12_STAFF_COMPLEMENT;
    }

    public BigDecimal getR12_LOCAL() {
        return R12_LOCAL;
    }

    public void setR12_LOCAL(BigDecimal r12_LOCAL) {
        R12_LOCAL = r12_LOCAL;
    }

    public BigDecimal getR12_EXPARIATES() {
        return R12_EXPARIATES;
    }

    public void setR12_EXPARIATES(BigDecimal r12_EXPARIATES) {
        R12_EXPARIATES = r12_EXPARIATES;
    }

    public BigDecimal getR12_TOTAL() {
        return R12_TOTAL;
    }

    public void setR12_TOTAL(BigDecimal r12_TOTAL) {
        R12_TOTAL = r12_TOTAL;
    }

    public String getR13_STAFF_COMPLEMENT() {
        return R13_STAFF_COMPLEMENT;
    }

    public void setR13_STAFF_COMPLEMENT(String r13_STAFF_COMPLEMENT) {
        R13_STAFF_COMPLEMENT = r13_STAFF_COMPLEMENT;
    }

    public BigDecimal getR13_LOCAL() {
        return R13_LOCAL;
    }

    public void setR13_LOCAL(BigDecimal r13_LOCAL) {
        R13_LOCAL = r13_LOCAL;
    }

    public BigDecimal getR13_EXPARIATES() {
        return R13_EXPARIATES;
    }

    public void setR13_EXPARIATES(BigDecimal r13_EXPARIATES) {
        R13_EXPARIATES = r13_EXPARIATES;
    }

    public BigDecimal getR13_TOTAL() {
        return R13_TOTAL;
    }

    public void setR13_TOTAL(BigDecimal r13_TOTAL) {
        R13_TOTAL = r13_TOTAL;
    }

    public String getR14_STAFF_COMPLEMENT() {
        return R14_STAFF_COMPLEMENT;
    }

    public void setR14_STAFF_COMPLEMENT(String r14_STAFF_COMPLEMENT) {
        R14_STAFF_COMPLEMENT = r14_STAFF_COMPLEMENT;
    }

    public BigDecimal getR14_LOCAL() {
        return R14_LOCAL;
    }

    public void setR14_LOCAL(BigDecimal r14_LOCAL) {
        R14_LOCAL = r14_LOCAL;
    }

    public BigDecimal getR14_EXPARIATES() {
        return R14_EXPARIATES;
    }

    public void setR14_EXPARIATES(BigDecimal r14_EXPARIATES) {
        R14_EXPARIATES = r14_EXPARIATES;
    }

    public BigDecimal getR14_TOTAL() {
        return R14_TOTAL;
    }

    public void setR14_TOTAL(BigDecimal r14_TOTAL) {
        R14_TOTAL = r14_TOTAL;
    }

    public String getR15_STAFF_COMPLEMENT() {
        return R15_STAFF_COMPLEMENT;
    }

    public void setR15_STAFF_COMPLEMENT(String r15_STAFF_COMPLEMENT) {
        R15_STAFF_COMPLEMENT = r15_STAFF_COMPLEMENT;
    }

    public BigDecimal getR15_LOCAL() {
        return R15_LOCAL;
    }

    public void setR15_LOCAL(BigDecimal r15_LOCAL) {
        R15_LOCAL = r15_LOCAL;
    }

    public BigDecimal getR15_EXPARIATES() {
        return R15_EXPARIATES;
    }

    public void setR15_EXPARIATES(BigDecimal r15_EXPARIATES) {
        R15_EXPARIATES = r15_EXPARIATES;
    }

    public BigDecimal getR15_TOTAL() {
        return R15_TOTAL;
    }

    public void setR15_TOTAL(BigDecimal r15_TOTAL) {
        R15_TOTAL = r15_TOTAL;
    }

    public String getR21_SENIOR_MANAGEMENT_COMPENSATION() {
        return R21_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public void setR21_SENIOR_MANAGEMENT_COMPENSATION(String r21_SENIOR_MANAGEMENT_COMPENSATION) {
        R21_SENIOR_MANAGEMENT_COMPENSATION = r21_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public BigDecimal getR21_LOCAL() {
        return R21_LOCAL;
    }

    public void setR21_LOCAL(BigDecimal r21_LOCAL) {
        R21_LOCAL = r21_LOCAL;
    }

    public BigDecimal getR21_EXPARIATES() {
        return R21_EXPARIATES;
    }

    public void setR21_EXPARIATES(BigDecimal r21_EXPARIATES) {
        R21_EXPARIATES = r21_EXPARIATES;
    }

    public BigDecimal getR21_TOTAL() {
        return R21_TOTAL;
    }

    public void setR21_TOTAL(BigDecimal r21_TOTAL) {
        R21_TOTAL = r21_TOTAL;
    }

    public String getR22_SENIOR_MANAGEMENT_COMPENSATION() {
        return R22_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public void setR22_SENIOR_MANAGEMENT_COMPENSATION(String r22_SENIOR_MANAGEMENT_COMPENSATION) {
        R22_SENIOR_MANAGEMENT_COMPENSATION = r22_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public BigDecimal getR22_LOCAL() {
        return R22_LOCAL;
    }

    public void setR22_LOCAL(BigDecimal r22_LOCAL) {
        R22_LOCAL = r22_LOCAL;
    }

    public BigDecimal getR22_EXPARIATES() {
        return R22_EXPARIATES;
    }

    public void setR22_EXPARIATES(BigDecimal r22_EXPARIATES) {
        R22_EXPARIATES = r22_EXPARIATES;
    }

    public BigDecimal getR22_TOTAL() {
        return R22_TOTAL;
    }

    public void setR22_TOTAL(BigDecimal r22_TOTAL) {
        R22_TOTAL = r22_TOTAL;
    }

    public String getR23_SENIOR_MANAGEMENT_COMPENSATION() {
        return R23_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public void setR23_SENIOR_MANAGEMENT_COMPENSATION(String r23_SENIOR_MANAGEMENT_COMPENSATION) {
        R23_SENIOR_MANAGEMENT_COMPENSATION = r23_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public BigDecimal getR23_LOCAL() {
        return R23_LOCAL;
    }

    public void setR23_LOCAL(BigDecimal r23_LOCAL) {
        R23_LOCAL = r23_LOCAL;
    }

    public BigDecimal getR23_EXPARIATES() {
        return R23_EXPARIATES;
    }

    public void setR23_EXPARIATES(BigDecimal r23_EXPARIATES) {
        R23_EXPARIATES = r23_EXPARIATES;
    }

    public BigDecimal getR23_TOTAL() {
        return R23_TOTAL;
    }

    public void setR23_TOTAL(BigDecimal r23_TOTAL) {
        R23_TOTAL = r23_TOTAL;
    }

    public String getR24_SENIOR_MANAGEMENT_COMPENSATION() {
        return R24_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public void setR24_SENIOR_MANAGEMENT_COMPENSATION(String r24_SENIOR_MANAGEMENT_COMPENSATION) {
        R24_SENIOR_MANAGEMENT_COMPENSATION = r24_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public BigDecimal getR24_LOCAL() {
        return R24_LOCAL;
    }

    public void setR24_LOCAL(BigDecimal r24_LOCAL) {
        R24_LOCAL = r24_LOCAL;
    }

    public BigDecimal getR24_EXPARIATES() {
        return R24_EXPARIATES;
    }

    public void setR24_EXPARIATES(BigDecimal r24_EXPARIATES) {
        R24_EXPARIATES = r24_EXPARIATES;
    }

    public BigDecimal getR24_TOTAL() {
        return R24_TOTAL;
    }

    public void setR24_TOTAL(BigDecimal r24_TOTAL) {
        R24_TOTAL = r24_TOTAL;
    }

    public String getR25_SENIOR_MANAGEMENT_COMPENSATION() {
        return R25_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public void setR25_SENIOR_MANAGEMENT_COMPENSATION(String r25_SENIOR_MANAGEMENT_COMPENSATION) {
        R25_SENIOR_MANAGEMENT_COMPENSATION = r25_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public BigDecimal getR25_LOCAL() {
        return R25_LOCAL;
    }

    public void setR25_LOCAL(BigDecimal r25_LOCAL) {
        R25_LOCAL = r25_LOCAL;
    }

    public BigDecimal getR25_EXPARIATES() {
        return R25_EXPARIATES;
    }

    public void setR25_EXPARIATES(BigDecimal r25_EXPARIATES) {
        R25_EXPARIATES = r25_EXPARIATES;
    }

    public BigDecimal getR25_TOTAL() {
        return R25_TOTAL;
    }

    public void setR25_TOTAL(BigDecimal r25_TOTAL) {
        R25_TOTAL = r25_TOTAL;
    }

    public String getR26_SENIOR_MANAGEMENT_COMPENSATION() {
        return R26_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public void setR26_SENIOR_MANAGEMENT_COMPENSATION(String r26_SENIOR_MANAGEMENT_COMPENSATION) {
        R26_SENIOR_MANAGEMENT_COMPENSATION = r26_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public BigDecimal getR26_LOCAL() {
        return R26_LOCAL;
    }

    public void setR26_LOCAL(BigDecimal r26_LOCAL) {
        R26_LOCAL = r26_LOCAL;
    }

    public BigDecimal getR26_EXPARIATES() {
        return R26_EXPARIATES;
    }

    public void setR26_EXPARIATES(BigDecimal r26_EXPARIATES) {
        R26_EXPARIATES = r26_EXPARIATES;
    }

    public BigDecimal getR26_TOTAL() {
        return R26_TOTAL;
    }

    public void setR26_TOTAL(BigDecimal r26_TOTAL) {
        R26_TOTAL = r26_TOTAL;
    }

    public String getR27_SENIOR_MANAGEMENT_COMPENSATION() {
        return R27_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public void setR27_SENIOR_MANAGEMENT_COMPENSATION(String r27_SENIOR_MANAGEMENT_COMPENSATION) {
        R27_SENIOR_MANAGEMENT_COMPENSATION = r27_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public BigDecimal getR27_LOCAL() {
        return R27_LOCAL;
    }

    public void setR27_LOCAL(BigDecimal r27_LOCAL) {
        R27_LOCAL = r27_LOCAL;
    }

    public BigDecimal getR27_EXPARIATES() {
        return R27_EXPARIATES;
    }

    public void setR27_EXPARIATES(BigDecimal r27_EXPARIATES) {
        R27_EXPARIATES = r27_EXPARIATES;
    }

    public BigDecimal getR27_TOTAL() {
        return R27_TOTAL;
    }

    public void setR27_TOTAL(BigDecimal r27_TOTAL) {
        R27_TOTAL = r27_TOTAL;
    }

    public String getR28_SENIOR_MANAGEMENT_COMPENSATION() {
        return R28_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public void setR28_SENIOR_MANAGEMENT_COMPENSATION(String r28_SENIOR_MANAGEMENT_COMPENSATION) {
        R28_SENIOR_MANAGEMENT_COMPENSATION = r28_SENIOR_MANAGEMENT_COMPENSATION;
    }

    public BigDecimal getR28_LOCAL() {
        return R28_LOCAL;
    }

    public void setR28_LOCAL(BigDecimal r28_LOCAL) {
        R28_LOCAL = r28_LOCAL;
    }

    public BigDecimal getR28_EXPARIATES() {
        return R28_EXPARIATES;
    }

    public void setR28_EXPARIATES(BigDecimal r28_EXPARIATES) {
        R28_EXPARIATES = r28_EXPARIATES;
    }

    public BigDecimal getR28_TOTAL() {
        return R28_TOTAL;
    }

    public void setR28_TOTAL(BigDecimal r28_TOTAL) {
        R28_TOTAL = r28_TOTAL;
    }

    public String getR33_STAFF_LOANS() {
        return R33_STAFF_LOANS;
    }

    public void setR33_STAFF_LOANS(String r33_STAFF_LOANS) {
        R33_STAFF_LOANS = r33_STAFF_LOANS;
    }

    public BigDecimal getR33_ORIGINAL_AMT() {
        return R33_ORIGINAL_AMT;
    }

    public void setR33_ORIGINAL_AMT(BigDecimal r33_ORIGINAL_AMT) {
        R33_ORIGINAL_AMT = r33_ORIGINAL_AMT;
    }

    public BigDecimal getR33_BALANCE_OUTSTANDING() {
        return R33_BALANCE_OUTSTANDING;
    }

    public void setR33_BALANCE_OUTSTANDING(BigDecimal r33_BALANCE_OUTSTANDING) {
        R33_BALANCE_OUTSTANDING = r33_BALANCE_OUTSTANDING;
    }

    public BigDecimal getR33_NO_OF_ACS() {
        return R33_NO_OF_ACS;
    }

    public void setR33_NO_OF_ACS(BigDecimal r33_NO_OF_ACS) {
        R33_NO_OF_ACS = r33_NO_OF_ACS;
    }

    public BigDecimal getR33_INTEREST_RATE() {
        return R33_INTEREST_RATE;
    }

    public void setR33_INTEREST_RATE(BigDecimal r33_INTEREST_RATE) {
        R33_INTEREST_RATE = r33_INTEREST_RATE;
    }

    public String getR34_STAFF_LOANS() {
        return R34_STAFF_LOANS;
    }

    public void setR34_STAFF_LOANS(String r34_STAFF_LOANS) {
        R34_STAFF_LOANS = r34_STAFF_LOANS;
    }

    public BigDecimal getR34_ORIGINAL_AMT() {
        return R34_ORIGINAL_AMT;
    }

    public void setR34_ORIGINAL_AMT(BigDecimal r34_ORIGINAL_AMT) {
        R34_ORIGINAL_AMT = r34_ORIGINAL_AMT;
    }

    public BigDecimal getR34_BALANCE_OUTSTANDING() {
        return R34_BALANCE_OUTSTANDING;
    }

    public void setR34_BALANCE_OUTSTANDING(BigDecimal r34_BALANCE_OUTSTANDING) {
        R34_BALANCE_OUTSTANDING = r34_BALANCE_OUTSTANDING;
    }

    public BigDecimal getR34_NO_OF_ACS() {
        return R34_NO_OF_ACS;
    }

    public void setR34_NO_OF_ACS(BigDecimal r34_NO_OF_ACS) {
        R34_NO_OF_ACS = r34_NO_OF_ACS;
    }

    public BigDecimal getR34_INTEREST_RATE() {
        return R34_INTEREST_RATE;
    }

    public void setR34_INTEREST_RATE(BigDecimal r34_INTEREST_RATE) {
        R34_INTEREST_RATE = r34_INTEREST_RATE;
    }

    public String getR35_STAFF_LOANS() {
        return R35_STAFF_LOANS;
    }

    public void setR35_STAFF_LOANS(String r35_STAFF_LOANS) {
        R35_STAFF_LOANS = r35_STAFF_LOANS;
    }

    public BigDecimal getR35_ORIGINAL_AMT() {
        return R35_ORIGINAL_AMT;
    }

    public void setR35_ORIGINAL_AMT(BigDecimal r35_ORIGINAL_AMT) {
        R35_ORIGINAL_AMT = r35_ORIGINAL_AMT;
    }

    public BigDecimal getR35_BALANCE_OUTSTANDING() {
        return R35_BALANCE_OUTSTANDING;
    }

    public void setR35_BALANCE_OUTSTANDING(BigDecimal r35_BALANCE_OUTSTANDING) {
        R35_BALANCE_OUTSTANDING = r35_BALANCE_OUTSTANDING;
    }

    public BigDecimal getR35_NO_OF_ACS() {
        return R35_NO_OF_ACS;
    }

    public void setR35_NO_OF_ACS(BigDecimal r35_NO_OF_ACS) {
        R35_NO_OF_ACS = r35_NO_OF_ACS;
    }

    public BigDecimal getR35_INTEREST_RATE() {
        return R35_INTEREST_RATE;
    }

    public void setR35_INTEREST_RATE(BigDecimal r35_INTEREST_RATE) {
        R35_INTEREST_RATE = r35_INTEREST_RATE;
    }

    public String getR36_STAFF_LOANS() {
        return R36_STAFF_LOANS;
    }

    public void setR36_STAFF_LOANS(String r36_STAFF_LOANS) {
        R36_STAFF_LOANS = r36_STAFF_LOANS;
    }

    public BigDecimal getR36_ORIGINAL_AMT() {
        return R36_ORIGINAL_AMT;
    }

    public void setR36_ORIGINAL_AMT(BigDecimal r36_ORIGINAL_AMT) {
        R36_ORIGINAL_AMT = r36_ORIGINAL_AMT;
    }

    public BigDecimal getR36_BALANCE_OUTSTANDING() {
        return R36_BALANCE_OUTSTANDING;
    }

    public void setR36_BALANCE_OUTSTANDING(BigDecimal r36_BALANCE_OUTSTANDING) {
        R36_BALANCE_OUTSTANDING = r36_BALANCE_OUTSTANDING;
    }

    public BigDecimal getR36_NO_OF_ACS() {
        return R36_NO_OF_ACS;
    }

    public void setR36_NO_OF_ACS(BigDecimal r36_NO_OF_ACS) {
        R36_NO_OF_ACS = r36_NO_OF_ACS;
    }

    public BigDecimal getR36_INTEREST_RATE() {
        return R36_INTEREST_RATE;
    }

    public void setR36_INTEREST_RATE(BigDecimal r36_INTEREST_RATE) {
        R36_INTEREST_RATE = r36_INTEREST_RATE;
    }

    public String getR37_STAFF_LOANS() {
        return R37_STAFF_LOANS;
    }

    public void setR37_STAFF_LOANS(String r37_STAFF_LOANS) {
        R37_STAFF_LOANS = r37_STAFF_LOANS;
    }

    public BigDecimal getR37_ORIGINAL_AMT() {
        return R37_ORIGINAL_AMT;
    }

    public void setR37_ORIGINAL_AMT(BigDecimal r37_ORIGINAL_AMT) {
        R37_ORIGINAL_AMT = r37_ORIGINAL_AMT;
    }

    public BigDecimal getR37_BALANCE_OUTSTANDING() {
        return R37_BALANCE_OUTSTANDING;
    }

    public void setR37_BALANCE_OUTSTANDING(BigDecimal r37_BALANCE_OUTSTANDING) {
        R37_BALANCE_OUTSTANDING = r37_BALANCE_OUTSTANDING;
    }

    public BigDecimal getR37_NO_OF_ACS() {
        return R37_NO_OF_ACS;
    }

    public void setR37_NO_OF_ACS(BigDecimal r37_NO_OF_ACS) {
        R37_NO_OF_ACS = r37_NO_OF_ACS;
    }

    public BigDecimal getR37_INTEREST_RATE() {
        return R37_INTEREST_RATE;
    }

    public void setR37_INTEREST_RATE(BigDecimal r37_INTEREST_RATE) {
        R37_INTEREST_RATE = r37_INTEREST_RATE;
    }

    public String getR38_STAFF_LOANS() {
        return R38_STAFF_LOANS;
    }

    public void setR38_STAFF_LOANS(String r38_STAFF_LOANS) {
        R38_STAFF_LOANS = r38_STAFF_LOANS;
    }

    public BigDecimal getR38_ORIGINAL_AMT() {
        return R38_ORIGINAL_AMT;
    }

    public void setR38_ORIGINAL_AMT(BigDecimal r38_ORIGINAL_AMT) {
        R38_ORIGINAL_AMT = r38_ORIGINAL_AMT;
    }

    public BigDecimal getR38_BALANCE_OUTSTANDING() {
        return R38_BALANCE_OUTSTANDING;
    }

    public void setR38_BALANCE_OUTSTANDING(BigDecimal r38_BALANCE_OUTSTANDING) {
        R38_BALANCE_OUTSTANDING = r38_BALANCE_OUTSTANDING;
    }

    public BigDecimal getR38_NO_OF_ACS() {
        return R38_NO_OF_ACS;
    }

    public void setR38_NO_OF_ACS(BigDecimal r38_NO_OF_ACS) {
        R38_NO_OF_ACS = r38_NO_OF_ACS;
    }

    public BigDecimal getR38_INTEREST_RATE() {
        return R38_INTEREST_RATE;
    }

    public void setR38_INTEREST_RATE(BigDecimal r38_INTEREST_RATE) {
        R38_INTEREST_RATE = r38_INTEREST_RATE;
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

    
}
