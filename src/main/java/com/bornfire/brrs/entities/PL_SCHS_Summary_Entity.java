package com.bornfire.brrs.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

@Entity
@Table(name = "BRRS_PL_SCHS_SUMMARYTABLE")
public class PL_SCHS_Summary_Entity {

    // ================= R9 to R13 =================
    private String r9IntrestDiv;
    private BigDecimal r9FigBalSheet;
    private BigDecimal r9FigBalSheetBwp;
    private BigDecimal r9AmtStatementAdj;
    private BigDecimal r9AmtStatementAdjBwp;
    private BigDecimal r9NetAmt;
    private BigDecimal r9NetAmtBwp;
    private BigDecimal r9BalSub;
    private BigDecimal r9BalSubBwp;
    private BigDecimal r9BalSubDiaries;
    private BigDecimal r9BalSubDiariesBwp;

    private String r10IntrestDiv;
    private BigDecimal r10FigBalSheet;
    private BigDecimal r10FigBalSheetBwp;
    private BigDecimal r10AmtStatementAdj;
    private BigDecimal r10AmtStatementAdjBwp;
    private BigDecimal r10NetAmt;
    private BigDecimal r10NetAmtBwp;
    private BigDecimal r10BalSub;
    private BigDecimal r10BalSubBwp;
    private BigDecimal r10BalSubDiaries;
    private BigDecimal r10BalSubDiariesBwp;

    private String r11IntrestDiv;
    private BigDecimal r11FigBalSheet;
    private BigDecimal r11FigBalSheetBwp;
    private BigDecimal r11AmtStatementAdj;
    private BigDecimal r11AmtStatementAdjBwp;
    private BigDecimal r11NetAmt;
    private BigDecimal r11NetAmtBwp;
    private BigDecimal r11BalSub;
    private BigDecimal r11BalSubBwp;
    private BigDecimal r11BalSubDiaries;
    private BigDecimal r11BalSubDiariesBwp;

    private String r12IntrestDiv;
    private BigDecimal r12FigBalSheet;
    private BigDecimal r12FigBalSheetBwp;
    private BigDecimal r12AmtStatementAdj;
    private BigDecimal r12AmtStatementAdjBwp;
    private BigDecimal r12NetAmt;
    private BigDecimal r12NetAmtBwp;
    private BigDecimal r12BalSub;
    private BigDecimal r12BalSubBwp;
    private BigDecimal r12BalSubDiaries;
    private BigDecimal r12BalSubDiariesBwp;

    private String r13IntrestDiv;
    private BigDecimal r13FigBalSheet;
    private BigDecimal r13FigBalSheetBwp;
    private BigDecimal r13AmtStatementAdj;
    private BigDecimal r13AmtStatementAdjBwp;
    private BigDecimal r13NetAmt;
    private BigDecimal r13NetAmtBwp;
    private BigDecimal r13BalSub;
    private BigDecimal r13BalSubBwp;
    private BigDecimal r13BalSubDiaries;
    private BigDecimal r13BalSubDiariesBwp;

    // ================= R17 to R31 =================
    private String r17OtherIncome;
    private BigDecimal r17FigBalSheet;
    private BigDecimal r17FigBalSheetBwp;
    private BigDecimal r17AmtStatementAdj;
    private BigDecimal r17AmtStatementAdjBwp;
    private BigDecimal r17NetAmt;
    private BigDecimal r17NetAmtBwp;
    private BigDecimal r17BalSub;
    private BigDecimal r17BalSubBwp;
    private BigDecimal r17BalSubDiariesBwp;

    private String r18OtherIncome;
    private BigDecimal r18FigBalSheet;
    private BigDecimal r18FigBalSheetBwp;
    private BigDecimal r18AmtStatementAdj;
    private BigDecimal r18AmtStatementAdjBwp;
    private BigDecimal r18NetAmt;
    private BigDecimal r18NetAmtBwp;
    private BigDecimal r18BalSub;
    private BigDecimal r18BalSubBwp;
    private BigDecimal r18BalSubDiariesBwp;

    private String r19OtherIncome;
    private BigDecimal r19FigBalSheet;
    private BigDecimal r19FigBalSheetBwp;
    private BigDecimal r19AmtStatementAdj;
    private BigDecimal r19AmtStatementAdjBwp;
    private BigDecimal r19NetAmt;
    private BigDecimal r19NetAmtBwp;
    private BigDecimal r19BalSub;
    private BigDecimal r19BalSubBwp;
    private BigDecimal r19BalSubDiariesBwp;

    private String r20OtherIncome;
    private BigDecimal r20FigBalSheet;
    private BigDecimal r20FigBalSheetBwp;
    private BigDecimal r20AmtStatementAdj;
    private BigDecimal r20AmtStatementAdjBwp;
    private BigDecimal r20NetAmt;
    private BigDecimal r20NetAmtBwp;
    private BigDecimal r20BalSub;
    private BigDecimal r20BalSubBwp;
    private BigDecimal r20BalSubDiariesBwp;

    private String r21OtherIncome;
    private BigDecimal r21FigBalSheet;
    private BigDecimal r21FigBalSheetBwp;
    private BigDecimal r21AmtStatementAdj;
    private BigDecimal r21AmtStatementAdjBwp;
    private BigDecimal r21NetAmt;
    private BigDecimal r21NetAmtBwp;
    private BigDecimal r21BalSub;
    private BigDecimal r21BalSubBwp;
    private BigDecimal r21BalSubDiariesBwp;

    private String r22OtherIncome;
    private BigDecimal r22FigBalSheet;
    private BigDecimal r22FigBalSheetBwp;
    private BigDecimal r22AmtStatementAdj;
    private BigDecimal r22AmtStatementAdjBwp;
    private BigDecimal r22NetAmt;
    private BigDecimal r22NetAmtBwp;
    private BigDecimal r22BalSub;
    private BigDecimal r22BalSubBwp;
    private BigDecimal r22BalSubDiariesBwp;

    private String r23OtherIncome;
    private BigDecimal r23FigBalSheet;
    private BigDecimal r23FigBalSheetBwp;
    private BigDecimal r23AmtStatementAdj;
    private BigDecimal r23AmtStatementAdjBwp;
    private BigDecimal r23NetAmt;
    private BigDecimal r23NetAmtBwp;
    private BigDecimal r23BalSub;
    private BigDecimal r23BalSubBwp;
    private BigDecimal r23BalSubDiariesBwp;

    private String r24OtherIncome;
    private BigDecimal r24FigBalSheet;
    private BigDecimal r24FigBalSheetBwp;
    private BigDecimal r24AmtStatementAdj;
    private BigDecimal r24AmtStatementAdjBwp;
    private BigDecimal r24NetAmt;
    private BigDecimal r24NetAmtBwp;
    private BigDecimal r24BalSub;
    private BigDecimal r24BalSubBwp;
    private BigDecimal r24BalSubDiariesBwp;

    private String r25OtherIncome;
    private BigDecimal r25FigBalSheet;
    private BigDecimal r25FigBalSheetBwp;
    private BigDecimal r25AmtStatementAdj;
    private BigDecimal r25AmtStatementAdjBwp;
    private BigDecimal r25NetAmt;
    private BigDecimal r25NetAmtBwp;
    private BigDecimal r25BalSub;
    private BigDecimal r25BalSubBwp;
    private BigDecimal r25BalSubDiariesBwp;

    private String r26OtherIncome;
    private BigDecimal r26FigBalSheet;
    private BigDecimal r26FigBalSheetBwp;
    private BigDecimal r26AmtStatementAdj;
    private BigDecimal r26AmtStatementAdjBwp;
    private BigDecimal r26NetAmt;
    private BigDecimal r26NetAmtBwp;
    private BigDecimal r26BalSub;
    private BigDecimal r26BalSubBwp;
    private BigDecimal r26BalSubDiariesBwp;

    private String r27OtherIncome;
    private BigDecimal r27FigBalSheet;
    private BigDecimal r27FigBalSheetBwp;
    private BigDecimal r27AmtStatementAdj;
    private BigDecimal r27AmtStatementAdjBwp;
    private BigDecimal r27NetAmt;
    private BigDecimal r27NetAmtBwp;
    private BigDecimal r27BalSub;
    private BigDecimal r27BalSubBwp;
    private BigDecimal r27BalSubDiariesBwp;

    private String r28OtherIncome;
    private BigDecimal r28FigBalSheet;
    private BigDecimal r28FigBalSheetBwp;
    private BigDecimal r28AmtStatementAdj;
    private BigDecimal r28AmtStatementAdjBwp;
    private BigDecimal r28NetAmt;
    private BigDecimal r28NetAmtBwp;
    private BigDecimal r28BalSub;
    private BigDecimal r28BalSubBwp;
    private BigDecimal r28BalSubDiariesBwp;

    private String r29OtherIncome;
    private BigDecimal r29FigBalSheet;
    private BigDecimal r29FigBalSheetBwp;
    private BigDecimal r29AmtStatementAdj;
    private BigDecimal r29AmtStatementAdjBwp;
    private BigDecimal r29NetAmt;
    private BigDecimal r29NetAmtBwp;
    private BigDecimal r29BalSub;
    private BigDecimal r29BalSubBwp;
    private BigDecimal r29BalSubDiariesBwp;

    private String r30OtherIncome;
    private BigDecimal r30FigBalSheet;
    private BigDecimal r30FigBalSheetBwp;
    private BigDecimal r30AmtStatementAdj;
    private BigDecimal r30AmtStatementAdjBwp;
    private BigDecimal r30NetAmt;
    private BigDecimal r30NetAmtBwp;
    private BigDecimal r30BalSub;
    private BigDecimal r30BalSubBwp;
    private BigDecimal r30BalSubDiariesBwp;

    private String r31OtherIncome;
    private BigDecimal r31FigBalSheet;
    private BigDecimal r31FigBalSheetBwp;
    private BigDecimal r31AmtStatementAdj;
    private BigDecimal r31AmtStatementAdjBwp;
    private BigDecimal r31NetAmt;
    private BigDecimal r31NetAmtBwp;
    private BigDecimal r31BalSub;
    private BigDecimal r31BalSubBwp;
    private BigDecimal r31BalSubDiariesBwp;

    // ================= R40 to R43 =================
    private String r40IntrestExpended;
    private BigDecimal r40FigBalSheet;
    private BigDecimal r40FigBalSheetBwp;
    private BigDecimal r40AmtStatementAdj;
    private BigDecimal r40AmtStatementAdjBwp;
    private BigDecimal r40NetAmt;
    private BigDecimal r40NetAmtBwp;
    private BigDecimal r40BalSub;
    private BigDecimal r40BalSubBwp;
    private BigDecimal r40BalSubDiariesBwp;

    private String r41IntrestExpended;
    private BigDecimal r41FigBalSheet;
    private BigDecimal r41FigBalSheetBwp;
    private BigDecimal r41AmtStatementAdj;
    private BigDecimal r41AmtStatementAdjBwp;
    private BigDecimal r41NetAmt;
    private BigDecimal r41NetAmtBwp;
    private BigDecimal r41BalSub;
    private BigDecimal r41BalSubBwp;
    private BigDecimal r41BalSubDiariesBwp;

    private String r42IntrestExpended;
    private BigDecimal r42FigBalSheet;
    private BigDecimal r42FigBalSheetBwp;
    private BigDecimal r42AmtStatementAdj;
    private BigDecimal r42AmtStatementAdjBwp;
    private BigDecimal r42NetAmt;
    private BigDecimal r42NetAmtBwp;
    private BigDecimal r42BalSub;
    private BigDecimal r42BalSubBwp;
    private BigDecimal r42BalSubDiariesBwp;

    private String r43IntrestExpended;
    private BigDecimal r43FigBalSheet;
    private BigDecimal r43FigBalSheetBwp;
    private BigDecimal r43AmtStatementAdj;
    private BigDecimal r43AmtStatementAdjBwp;
    private BigDecimal r43NetAmt;
    private BigDecimal r43NetAmtBwp;
    private BigDecimal r43BalSub;
    private BigDecimal r43BalSubBwp;
    private BigDecimal r43BalSubDiariesBwp;

    // ================= R48 to R63 =================
    private String r48OperatingExpenses;
    private BigDecimal r48FigBalSheet;
    private BigDecimal r48FigBalSheetBwp;
    private BigDecimal r48AmtStatementAdj;
    private BigDecimal r48AmtStatementAdjBwp;
    private BigDecimal r48NetAmt;
    private BigDecimal r48NetAmtBwp;
    private BigDecimal r48BalSub;
    private BigDecimal r48BalSubBwp;
    private BigDecimal r48BalSubDiariesBwp;

    private String r49OperatingExpenses;
    private BigDecimal r49FigBalSheet;
    private BigDecimal r49FigBalSheetBwp;
    private BigDecimal r49AmtStatementAdj;
    private BigDecimal r49AmtStatementAdjBwp;
    private BigDecimal r49NetAmt;
    private BigDecimal r49NetAmtBwp;
    private BigDecimal r49BalSub;
    private BigDecimal r49BalSubBwp;
    private BigDecimal r49BalSubDiariesBwp;

    private String r50OperatingExpenses;
    private BigDecimal r50FigBalSheet;
    private BigDecimal r50FigBalSheetBwp;
    private BigDecimal r50AmtStatementAdj;
    private BigDecimal r50AmtStatementAdjBwp;
    private BigDecimal r50NetAmt;
    private BigDecimal r50NetAmtBwp;
    private BigDecimal r50BalSub;
    private BigDecimal r50BalSubBwp;
    private BigDecimal r50BalSubDiariesBwp;

    private String r51OperatingExpenses;
    private BigDecimal r51FigBalSheet;
    private BigDecimal r51FigBalSheetBwp;
    private BigDecimal r51AmtStatementAdj;
    private BigDecimal r51AmtStatementAdjBwp;
    private BigDecimal r51NetAmt;
    private BigDecimal r51NetAmtBwp;
    private BigDecimal r51BalSub;
    private BigDecimal r51BalSubBwp;
    private BigDecimal r51BalSubDiariesBwp;

    private String r52OperatingExpenses;
    private BigDecimal r52FigBalSheet;
    private BigDecimal r52FigBalSheetBwp;
    private BigDecimal r52AmtStatementAdj;
    private BigDecimal r52AmtStatementAdjBwp;
    private BigDecimal r52NetAmt;
    private BigDecimal r52NetAmtBwp;
    private BigDecimal r52BalSub;
    private BigDecimal r52BalSubBwp;
    private BigDecimal r52BalSubDiariesBwp;

    private String r53OperatingExpenses;
    private BigDecimal r53FigBalSheet;
    private BigDecimal r53FigBalSheetBwp;
    private BigDecimal r53AmtStatementAdj;
    private BigDecimal r53AmtStatementAdjBwp;
    private BigDecimal r53NetAmt;
    private BigDecimal r53NetAmtBwp;
    private BigDecimal r53BalSub;
    private BigDecimal r53BalSubBwp;
    private BigDecimal r53BalSubDiariesBwp;

    private String r54OperatingExpenses;
    private BigDecimal r54FigBalSheet;
    private BigDecimal r54FigBalSheetBwp;
    private BigDecimal r54AmtStatementAdj;
    private BigDecimal r54AmtStatementAdjBwp;
    private BigDecimal r54NetAmt;
    private BigDecimal r54NetAmtBwp;
    private BigDecimal r54BalSub;
    private BigDecimal r54BalSubBwp;
    private BigDecimal r54BalSubDiariesBwp;

    private String r55OperatingExpenses;
    private BigDecimal r55FigBalSheet;
    private BigDecimal r55FigBalSheetBwp;
    private BigDecimal r55AmtStatementAdj;
    private BigDecimal r55AmtStatementAdjBwp;
    private BigDecimal r55NetAmt;
    private BigDecimal r55NetAmtBwp;
    private BigDecimal r55BalSub;
    private BigDecimal r55BalSubBwp;
    private BigDecimal r55BalSubDiariesBwp;

    private String r56OperatingExpenses;
    private BigDecimal r56FigBalSheet;
    private BigDecimal r56FigBalSheetBwp;
    private BigDecimal r56AmtStatementAdj;
    private BigDecimal r56AmtStatementAdjBwp;
    private BigDecimal r56NetAmt;
    private BigDecimal r56NetAmtBwp;
    private BigDecimal r56BalSub;
    private BigDecimal r56BalSubBwp;
    private BigDecimal r56BalSubDiariesBwp;

    private String r57OperatingExpenses;
    private BigDecimal r57FigBalSheet;
    private BigDecimal r57FigBalSheetBwp;
    private BigDecimal r57AmtStatementAdj;
    private BigDecimal r57AmtStatementAdjBwp;
    private BigDecimal r57NetAmt;
    private BigDecimal r57NetAmtBwp;
    private BigDecimal r57BalSub;
    private BigDecimal r57BalSubBwp;
    private BigDecimal r57BalSubDiariesBwp;

    private String r58OperatingExpenses;
    private BigDecimal r58FigBalSheet;
    private BigDecimal r58FigBalSheetBwp;
    private BigDecimal r58AmtStatementAdj;
    private BigDecimal r58AmtStatementAdjBwp;
    private BigDecimal r58NetAmt;
    private BigDecimal r58NetAmtBwp;
    private BigDecimal r58BalSub;
    private BigDecimal r58BalSubBwp;
    private BigDecimal r58BalSubDiariesBwp;

    private String r59OperatingExpenses;
    private BigDecimal r59FigBalSheet;
    private BigDecimal r59FigBalSheetBwp;
    private BigDecimal r59AmtStatementAdj;
    private BigDecimal r59AmtStatementAdjBwp;
    private BigDecimal r59NetAmt;
    private BigDecimal r59NetAmtBwp;
    private BigDecimal r59BalSub;
    private BigDecimal r59BalSubBwp;
    private BigDecimal r59BalSubDiariesBwp;

    private String r60OperatingExpenses;
    private BigDecimal r60FigBalSheet;
    private BigDecimal r60FigBalSheetBwp;
    private BigDecimal r60AmtStatementAdj;
    private BigDecimal r60AmtStatementAdjBwp;
    private BigDecimal r60NetAmt;
    private BigDecimal r60NetAmtBwp;
    private BigDecimal r60BalSub;
    private BigDecimal r60BalSubBwp;
    private BigDecimal r60BalSubDiariesBwp;

    private String r61OperatingExpenses;
    private BigDecimal r61FigBalSheet;
    private BigDecimal r61FigBalSheetBwp;
    private BigDecimal r61AmtStatementAdj;
    private BigDecimal r61AmtStatementAdjBwp;
    private BigDecimal r61NetAmt;
    private BigDecimal r61NetAmtBwp;
    private BigDecimal r61BalSub;
    private BigDecimal r61BalSubBwp;
    private BigDecimal r61BalSubDiariesBwp;

    private String r62OperatingExpenses;
    private BigDecimal r62FigBalSheet;
    private BigDecimal r62FigBalSheetBwp;
    private BigDecimal r62AmtStatementAdj;
    private BigDecimal r62AmtStatementAdjBwp;
    private BigDecimal r62NetAmt;
    private BigDecimal r62NetAmtBwp;
    private BigDecimal r62BalSub;
    private BigDecimal r62BalSubBwp;
    private BigDecimal r62BalSubDiariesBwp;

    private String r63OperatingExpenses;
    private BigDecimal r63FigBalSheet;
    private BigDecimal r63FigBalSheetBwp;
    private BigDecimal r63AmtStatementAdj;
    private BigDecimal r63AmtStatementAdjBwp;
    private BigDecimal r63NetAmt;
    private BigDecimal r63NetAmtBwp;
    private BigDecimal r63BalSub;
    private BigDecimal r63BalSubBwp;
    private BigDecimal r63BalSubDiariesBwp;

    
    /* ================= COMMON ================= */

    @Id
    @Column(name = "REPORT_DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date REPORT_DATE;

    @Column(name = "REPORT_VERSION")
    private String REPORT_VERSION;

    @Column(name = "REPORT_FREQUENCY")
    private String REPORT_FREQUENCY;

    @Column(name = "REPORT_CODE")
    private String REPORT_CODE;

    @Column(name = "REPORT_DESC")
    private String REPORT_DESC;

    @Column(name = "ENTITY_FLG")
    private String ENTITY_FLG;

    @Column(name = "MODIFY_FLG")
    private String MODIFY_FLG;

    @Column(name = "DEL_FLG")
    private String DEL_FLG;
    
    public String getR9IntrestDiv() {
        return r9IntrestDiv;
    }
    public void setR9IntrestDiv(String r9IntrestDiv) {
        this.r9IntrestDiv = r9IntrestDiv;
    }
    public BigDecimal getR9FigBalSheet() {
        return r9FigBalSheet;
    }
    public void setR9FigBalSheet(BigDecimal r9FigBalSheet) {
        this.r9FigBalSheet = r9FigBalSheet;
    }
    public BigDecimal getR9FigBalSheetBwp() {
        return r9FigBalSheetBwp;
    }
    public void setR9FigBalSheetBwp(BigDecimal r9FigBalSheetBwp) {
        this.r9FigBalSheetBwp = r9FigBalSheetBwp;
    }
    public BigDecimal getR9AmtStatementAdj() {
        return r9AmtStatementAdj;
    }
    public void setR9AmtStatementAdj(BigDecimal r9AmtStatementAdj) {
        this.r9AmtStatementAdj = r9AmtStatementAdj;
    }
    public BigDecimal getR9AmtStatementAdjBwp() {
        return r9AmtStatementAdjBwp;
    }
    public void setR9AmtStatementAdjBwp(BigDecimal r9AmtStatementAdjBwp) {
        this.r9AmtStatementAdjBwp = r9AmtStatementAdjBwp;
    }
    public BigDecimal getR9NetAmt() {
        return r9NetAmt;
    }
    public void setR9NetAmt(BigDecimal r9NetAmt) {
        this.r9NetAmt = r9NetAmt;
    }
    public BigDecimal getR9NetAmtBwp() {
        return r9NetAmtBwp;
    }
    public void setR9NetAmtBwp(BigDecimal r9NetAmtBwp) {
        this.r9NetAmtBwp = r9NetAmtBwp;
    }
    public BigDecimal getR9BalSub() {
        return r9BalSub;
    }
    public void setR9BalSub(BigDecimal r9BalSub) {
        this.r9BalSub = r9BalSub;
    }
    public BigDecimal getR9BalSubBwp() {
        return r9BalSubBwp;
    }
    public void setR9BalSubBwp(BigDecimal r9BalSubBwp) {
        this.r9BalSubBwp = r9BalSubBwp;
    }
    public BigDecimal getR9BalSubDiaries() {
        return r9BalSubDiaries;
    }
    public void setR9BalSubDiaries(BigDecimal r9BalSubDiaries) {
        this.r9BalSubDiaries = r9BalSubDiaries;
    }
    public BigDecimal getR9BalSubDiariesBwp() {
        return r9BalSubDiariesBwp;
    }
    public void setR9BalSubDiariesBwp(BigDecimal r9BalSubDiariesBwp) {
        this.r9BalSubDiariesBwp = r9BalSubDiariesBwp;
    }
    public String getR10IntrestDiv() {
        return r10IntrestDiv;
    }
    public void setR10IntrestDiv(String r10IntrestDiv) {
        this.r10IntrestDiv = r10IntrestDiv;
    }
    public BigDecimal getR10FigBalSheet() {
        return r10FigBalSheet;
    }
    public void setR10FigBalSheet(BigDecimal r10FigBalSheet) {
        this.r10FigBalSheet = r10FigBalSheet;
    }
    public BigDecimal getR10FigBalSheetBwp() {
        return r10FigBalSheetBwp;
    }
    public void setR10FigBalSheetBwp(BigDecimal r10FigBalSheetBwp) {
        this.r10FigBalSheetBwp = r10FigBalSheetBwp;
    }
    public BigDecimal getR10AmtStatementAdj() {
        return r10AmtStatementAdj;
    }
    public void setR10AmtStatementAdj(BigDecimal r10AmtStatementAdj) {
        this.r10AmtStatementAdj = r10AmtStatementAdj;
    }
    public BigDecimal getR10AmtStatementAdjBwp() {
        return r10AmtStatementAdjBwp;
    }
    public void setR10AmtStatementAdjBwp(BigDecimal r10AmtStatementAdjBwp) {
        this.r10AmtStatementAdjBwp = r10AmtStatementAdjBwp;
    }
    public BigDecimal getR10NetAmt() {
        return r10NetAmt;
    }
    public void setR10NetAmt(BigDecimal r10NetAmt) {
        this.r10NetAmt = r10NetAmt;
    }
    public BigDecimal getR10NetAmtBwp() {
        return r10NetAmtBwp;
    }
    public void setR10NetAmtBwp(BigDecimal r10NetAmtBwp) {
        this.r10NetAmtBwp = r10NetAmtBwp;
    }
    public BigDecimal getR10BalSub() {
        return r10BalSub;
    }
    public void setR10BalSub(BigDecimal r10BalSub) {
        this.r10BalSub = r10BalSub;
    }
    public BigDecimal getR10BalSubBwp() {
        return r10BalSubBwp;
    }
    public void setR10BalSubBwp(BigDecimal r10BalSubBwp) {
        this.r10BalSubBwp = r10BalSubBwp;
    }
    public BigDecimal getR10BalSubDiaries() {
        return r10BalSubDiaries;
    }
    public void setR10BalSubDiaries(BigDecimal r10BalSubDiaries) {
        this.r10BalSubDiaries = r10BalSubDiaries;
    }
    public BigDecimal getR10BalSubDiariesBwp() {
        return r10BalSubDiariesBwp;
    }
    public void setR10BalSubDiariesBwp(BigDecimal r10BalSubDiariesBwp) {
        this.r10BalSubDiariesBwp = r10BalSubDiariesBwp;
    }
    public String getR11IntrestDiv() {
        return r11IntrestDiv;
    }
    public void setR11IntrestDiv(String r11IntrestDiv) {
        this.r11IntrestDiv = r11IntrestDiv;
    }
    public BigDecimal getR11FigBalSheet() {
        return r11FigBalSheet;
    }
    public void setR11FigBalSheet(BigDecimal r11FigBalSheet) {
        this.r11FigBalSheet = r11FigBalSheet;
    }
    public BigDecimal getR11FigBalSheetBwp() {
        return r11FigBalSheetBwp;
    }
    public void setR11FigBalSheetBwp(BigDecimal r11FigBalSheetBwp) {
        this.r11FigBalSheetBwp = r11FigBalSheetBwp;
    }
    public BigDecimal getR11AmtStatementAdj() {
        return r11AmtStatementAdj;
    }
    public void setR11AmtStatementAdj(BigDecimal r11AmtStatementAdj) {
        this.r11AmtStatementAdj = r11AmtStatementAdj;
    }
    public BigDecimal getR11AmtStatementAdjBwp() {
        return r11AmtStatementAdjBwp;
    }
    public void setR11AmtStatementAdjBwp(BigDecimal r11AmtStatementAdjBwp) {
        this.r11AmtStatementAdjBwp = r11AmtStatementAdjBwp;
    }
    public BigDecimal getR11NetAmt() {
        return r11NetAmt;
    }
    public void setR11NetAmt(BigDecimal r11NetAmt) {
        this.r11NetAmt = r11NetAmt;
    }
    public BigDecimal getR11NetAmtBwp() {
        return r11NetAmtBwp;
    }
    public void setR11NetAmtBwp(BigDecimal r11NetAmtBwp) {
        this.r11NetAmtBwp = r11NetAmtBwp;
    }
    public BigDecimal getR11BalSub() {
        return r11BalSub;
    }
    public void setR11BalSub(BigDecimal r11BalSub) {
        this.r11BalSub = r11BalSub;
    }
    public BigDecimal getR11BalSubBwp() {
        return r11BalSubBwp;
    }
    public void setR11BalSubBwp(BigDecimal r11BalSubBwp) {
        this.r11BalSubBwp = r11BalSubBwp;
    }
    public BigDecimal getR11BalSubDiaries() {
        return r11BalSubDiaries;
    }
    public void setR11BalSubDiaries(BigDecimal r11BalSubDiaries) {
        this.r11BalSubDiaries = r11BalSubDiaries;
    }
    public BigDecimal getR11BalSubDiariesBwp() {
        return r11BalSubDiariesBwp;
    }
    public void setR11BalSubDiariesBwp(BigDecimal r11BalSubDiariesBwp) {
        this.r11BalSubDiariesBwp = r11BalSubDiariesBwp;
    }
    public String getR12IntrestDiv() {
        return r12IntrestDiv;
    }
    public void setR12IntrestDiv(String r12IntrestDiv) {
        this.r12IntrestDiv = r12IntrestDiv;
    }
    public BigDecimal getR12FigBalSheet() {
        return r12FigBalSheet;
    }
    public void setR12FigBalSheet(BigDecimal r12FigBalSheet) {
        this.r12FigBalSheet = r12FigBalSheet;
    }
    public BigDecimal getR12FigBalSheetBwp() {
        return r12FigBalSheetBwp;
    }
    public void setR12FigBalSheetBwp(BigDecimal r12FigBalSheetBwp) {
        this.r12FigBalSheetBwp = r12FigBalSheetBwp;
    }
    public BigDecimal getR12AmtStatementAdj() {
        return r12AmtStatementAdj;
    }
    public void setR12AmtStatementAdj(BigDecimal r12AmtStatementAdj) {
        this.r12AmtStatementAdj = r12AmtStatementAdj;
    }
    public BigDecimal getR12AmtStatementAdjBwp() {
        return r12AmtStatementAdjBwp;
    }
    public void setR12AmtStatementAdjBwp(BigDecimal r12AmtStatementAdjBwp) {
        this.r12AmtStatementAdjBwp = r12AmtStatementAdjBwp;
    }
    public BigDecimal getR12NetAmt() {
        return r12NetAmt;
    }
    public void setR12NetAmt(BigDecimal r12NetAmt) {
        this.r12NetAmt = r12NetAmt;
    }
    public BigDecimal getR12NetAmtBwp() {
        return r12NetAmtBwp;
    }
    public void setR12NetAmtBwp(BigDecimal r12NetAmtBwp) {
        this.r12NetAmtBwp = r12NetAmtBwp;
    }
    public BigDecimal getR12BalSub() {
        return r12BalSub;
    }
    public void setR12BalSub(BigDecimal r12BalSub) {
        this.r12BalSub = r12BalSub;
    }
    public BigDecimal getR12BalSubBwp() {
        return r12BalSubBwp;
    }
    public void setR12BalSubBwp(BigDecimal r12BalSubBwp) {
        this.r12BalSubBwp = r12BalSubBwp;
    }
    public BigDecimal getR12BalSubDiaries() {
        return r12BalSubDiaries;
    }
    public void setR12BalSubDiaries(BigDecimal r12BalSubDiaries) {
        this.r12BalSubDiaries = r12BalSubDiaries;
    }
    public BigDecimal getR12BalSubDiariesBwp() {
        return r12BalSubDiariesBwp;
    }
    public void setR12BalSubDiariesBwp(BigDecimal r12BalSubDiariesBwp) {
        this.r12BalSubDiariesBwp = r12BalSubDiariesBwp;
    }
    public String getR13IntrestDiv() {
        return r13IntrestDiv;
    }
    public void setR13IntrestDiv(String r13IntrestDiv) {
        this.r13IntrestDiv = r13IntrestDiv;
    }
    public BigDecimal getR13FigBalSheet() {
        return r13FigBalSheet;
    }
    public void setR13FigBalSheet(BigDecimal r13FigBalSheet) {
        this.r13FigBalSheet = r13FigBalSheet;
    }
    public BigDecimal getR13FigBalSheetBwp() {
        return r13FigBalSheetBwp;
    }
    public void setR13FigBalSheetBwp(BigDecimal r13FigBalSheetBwp) {
        this.r13FigBalSheetBwp = r13FigBalSheetBwp;
    }
    public BigDecimal getR13AmtStatementAdj() {
        return r13AmtStatementAdj;
    }
    public void setR13AmtStatementAdj(BigDecimal r13AmtStatementAdj) {
        this.r13AmtStatementAdj = r13AmtStatementAdj;
    }
    public BigDecimal getR13AmtStatementAdjBwp() {
        return r13AmtStatementAdjBwp;
    }
    public void setR13AmtStatementAdjBwp(BigDecimal r13AmtStatementAdjBwp) {
        this.r13AmtStatementAdjBwp = r13AmtStatementAdjBwp;
    }
    public BigDecimal getR13NetAmt() {
        return r13NetAmt;
    }
    public void setR13NetAmt(BigDecimal r13NetAmt) {
        this.r13NetAmt = r13NetAmt;
    }
    public BigDecimal getR13NetAmtBwp() {
        return r13NetAmtBwp;
    }
    public void setR13NetAmtBwp(BigDecimal r13NetAmtBwp) {
        this.r13NetAmtBwp = r13NetAmtBwp;
    }
    public BigDecimal getR13BalSub() {
        return r13BalSub;
    }
    public void setR13BalSub(BigDecimal r13BalSub) {
        this.r13BalSub = r13BalSub;
    }
    public BigDecimal getR13BalSubBwp() {
        return r13BalSubBwp;
    }
    public void setR13BalSubBwp(BigDecimal r13BalSubBwp) {
        this.r13BalSubBwp = r13BalSubBwp;
    }
    public BigDecimal getR13BalSubDiaries() {
        return r13BalSubDiaries;
    }
    public void setR13BalSubDiaries(BigDecimal r13BalSubDiaries) {
        this.r13BalSubDiaries = r13BalSubDiaries;
    }
    public BigDecimal getR13BalSubDiariesBwp() {
        return r13BalSubDiariesBwp;
    }
    public void setR13BalSubDiariesBwp(BigDecimal r13BalSubDiariesBwp) {
        this.r13BalSubDiariesBwp = r13BalSubDiariesBwp;
    }
    public String getR17OtherIncome() {
        return r17OtherIncome;
    }
    public void setR17OtherIncome(String r17OtherIncome) {
        this.r17OtherIncome = r17OtherIncome;
    }
    public BigDecimal getR17FigBalSheet() {
        return r17FigBalSheet;
    }
    public void setR17FigBalSheet(BigDecimal r17FigBalSheet) {
        this.r17FigBalSheet = r17FigBalSheet;
    }
    public BigDecimal getR17FigBalSheetBwp() {
        return r17FigBalSheetBwp;
    }
    public void setR17FigBalSheetBwp(BigDecimal r17FigBalSheetBwp) {
        this.r17FigBalSheetBwp = r17FigBalSheetBwp;
    }
    public BigDecimal getR17AmtStatementAdj() {
        return r17AmtStatementAdj;
    }
    public void setR17AmtStatementAdj(BigDecimal r17AmtStatementAdj) {
        this.r17AmtStatementAdj = r17AmtStatementAdj;
    }
    public BigDecimal getR17AmtStatementAdjBwp() {
        return r17AmtStatementAdjBwp;
    }
    public void setR17AmtStatementAdjBwp(BigDecimal r17AmtStatementAdjBwp) {
        this.r17AmtStatementAdjBwp = r17AmtStatementAdjBwp;
    }
    public BigDecimal getR17NetAmt() {
        return r17NetAmt;
    }
    public void setR17NetAmt(BigDecimal r17NetAmt) {
        this.r17NetAmt = r17NetAmt;
    }
    public BigDecimal getR17NetAmtBwp() {
        return r17NetAmtBwp;
    }
    public void setR17NetAmtBwp(BigDecimal r17NetAmtBwp) {
        this.r17NetAmtBwp = r17NetAmtBwp;
    }
    public BigDecimal getR17BalSub() {
        return r17BalSub;
    }
    public void setR17BalSub(BigDecimal r17BalSub) {
        this.r17BalSub = r17BalSub;
    }
    public BigDecimal getR17BalSubBwp() {
        return r17BalSubBwp;
    }
    public void setR17BalSubBwp(BigDecimal r17BalSubBwp) {
        this.r17BalSubBwp = r17BalSubBwp;
    }
    public BigDecimal getR17BalSubDiariesBwp() {
        return r17BalSubDiariesBwp;
    }
    public void setR17BalSubDiariesBwp(BigDecimal r17BalSubDiariesBwp) {
        this.r17BalSubDiariesBwp = r17BalSubDiariesBwp;
    }
    public String getR18OtherIncome() {
        return r18OtherIncome;
    }
    public void setR18OtherIncome(String r18OtherIncome) {
        this.r18OtherIncome = r18OtherIncome;
    }
    public BigDecimal getR18FigBalSheet() {
        return r18FigBalSheet;
    }
    public void setR18FigBalSheet(BigDecimal r18FigBalSheet) {
        this.r18FigBalSheet = r18FigBalSheet;
    }
    public BigDecimal getR18FigBalSheetBwp() {
        return r18FigBalSheetBwp;
    }
    public void setR18FigBalSheetBwp(BigDecimal r18FigBalSheetBwp) {
        this.r18FigBalSheetBwp = r18FigBalSheetBwp;
    }
    public BigDecimal getR18AmtStatementAdj() {
        return r18AmtStatementAdj;
    }
    public void setR18AmtStatementAdj(BigDecimal r18AmtStatementAdj) {
        this.r18AmtStatementAdj = r18AmtStatementAdj;
    }
    public BigDecimal getR18AmtStatementAdjBwp() {
        return r18AmtStatementAdjBwp;
    }
    public void setR18AmtStatementAdjBwp(BigDecimal r18AmtStatementAdjBwp) {
        this.r18AmtStatementAdjBwp = r18AmtStatementAdjBwp;
    }
    public BigDecimal getR18NetAmt() {
        return r18NetAmt;
    }
    public void setR18NetAmt(BigDecimal r18NetAmt) {
        this.r18NetAmt = r18NetAmt;
    }
    public BigDecimal getR18NetAmtBwp() {
        return r18NetAmtBwp;
    }
    public void setR18NetAmtBwp(BigDecimal r18NetAmtBwp) {
        this.r18NetAmtBwp = r18NetAmtBwp;
    }
    public BigDecimal getR18BalSub() {
        return r18BalSub;
    }
    public void setR18BalSub(BigDecimal r18BalSub) {
        this.r18BalSub = r18BalSub;
    }
    public BigDecimal getR18BalSubBwp() {
        return r18BalSubBwp;
    }
    public void setR18BalSubBwp(BigDecimal r18BalSubBwp) {
        this.r18BalSubBwp = r18BalSubBwp;
    }
    public BigDecimal getR18BalSubDiariesBwp() {
        return r18BalSubDiariesBwp;
    }
    public void setR18BalSubDiariesBwp(BigDecimal r18BalSubDiariesBwp) {
        this.r18BalSubDiariesBwp = r18BalSubDiariesBwp;
    }
    public String getR19OtherIncome() {
        return r19OtherIncome;
    }
    public void setR19OtherIncome(String r19OtherIncome) {
        this.r19OtherIncome = r19OtherIncome;
    }
    public BigDecimal getR19FigBalSheet() {
        return r19FigBalSheet;
    }
    public void setR19FigBalSheet(BigDecimal r19FigBalSheet) {
        this.r19FigBalSheet = r19FigBalSheet;
    }
    public BigDecimal getR19FigBalSheetBwp() {
        return r19FigBalSheetBwp;
    }
    public void setR19FigBalSheetBwp(BigDecimal r19FigBalSheetBwp) {
        this.r19FigBalSheetBwp = r19FigBalSheetBwp;
    }
    public BigDecimal getR19AmtStatementAdj() {
        return r19AmtStatementAdj;
    }
    public void setR19AmtStatementAdj(BigDecimal r19AmtStatementAdj) {
        this.r19AmtStatementAdj = r19AmtStatementAdj;
    }
    public BigDecimal getR19AmtStatementAdjBwp() {
        return r19AmtStatementAdjBwp;
    }
    public void setR19AmtStatementAdjBwp(BigDecimal r19AmtStatementAdjBwp) {
        this.r19AmtStatementAdjBwp = r19AmtStatementAdjBwp;
    }
    public BigDecimal getR19NetAmt() {
        return r19NetAmt;
    }
    public void setR19NetAmt(BigDecimal r19NetAmt) {
        this.r19NetAmt = r19NetAmt;
    }
    public BigDecimal getR19NetAmtBwp() {
        return r19NetAmtBwp;
    }
    public void setR19NetAmtBwp(BigDecimal r19NetAmtBwp) {
        this.r19NetAmtBwp = r19NetAmtBwp;
    }
    public BigDecimal getR19BalSub() {
        return r19BalSub;
    }
    public void setR19BalSub(BigDecimal r19BalSub) {
        this.r19BalSub = r19BalSub;
    }
    public BigDecimal getR19BalSubBwp() {
        return r19BalSubBwp;
    }
    public void setR19BalSubBwp(BigDecimal r19BalSubBwp) {
        this.r19BalSubBwp = r19BalSubBwp;
    }
    public BigDecimal getR19BalSubDiariesBwp() {
        return r19BalSubDiariesBwp;
    }
    public void setR19BalSubDiariesBwp(BigDecimal r19BalSubDiariesBwp) {
        this.r19BalSubDiariesBwp = r19BalSubDiariesBwp;
    }
    public String getR20OtherIncome() {
        return r20OtherIncome;
    }
    public void setR20OtherIncome(String r20OtherIncome) {
        this.r20OtherIncome = r20OtherIncome;
    }
    public BigDecimal getR20FigBalSheet() {
        return r20FigBalSheet;
    }
    public void setR20FigBalSheet(BigDecimal r20FigBalSheet) {
        this.r20FigBalSheet = r20FigBalSheet;
    }
    public BigDecimal getR20FigBalSheetBwp() {
        return r20FigBalSheetBwp;
    }
    public void setR20FigBalSheetBwp(BigDecimal r20FigBalSheetBwp) {
        this.r20FigBalSheetBwp = r20FigBalSheetBwp;
    }
    public BigDecimal getR20AmtStatementAdj() {
        return r20AmtStatementAdj;
    }
    public void setR20AmtStatementAdj(BigDecimal r20AmtStatementAdj) {
        this.r20AmtStatementAdj = r20AmtStatementAdj;
    }
    public BigDecimal getR20AmtStatementAdjBwp() {
        return r20AmtStatementAdjBwp;
    }
    public void setR20AmtStatementAdjBwp(BigDecimal r20AmtStatementAdjBwp) {
        this.r20AmtStatementAdjBwp = r20AmtStatementAdjBwp;
    }
    public BigDecimal getR20NetAmt() {
        return r20NetAmt;
    }
    public void setR20NetAmt(BigDecimal r20NetAmt) {
        this.r20NetAmt = r20NetAmt;
    }
    public BigDecimal getR20NetAmtBwp() {
        return r20NetAmtBwp;
    }
    public void setR20NetAmtBwp(BigDecimal r20NetAmtBwp) {
        this.r20NetAmtBwp = r20NetAmtBwp;
    }
    public BigDecimal getR20BalSub() {
        return r20BalSub;
    }
    public void setR20BalSub(BigDecimal r20BalSub) {
        this.r20BalSub = r20BalSub;
    }
    public BigDecimal getR20BalSubBwp() {
        return r20BalSubBwp;
    }
    public void setR20BalSubBwp(BigDecimal r20BalSubBwp) {
        this.r20BalSubBwp = r20BalSubBwp;
    }
    public BigDecimal getR20BalSubDiariesBwp() {
        return r20BalSubDiariesBwp;
    }
    public void setR20BalSubDiariesBwp(BigDecimal r20BalSubDiariesBwp) {
        this.r20BalSubDiariesBwp = r20BalSubDiariesBwp;
    }
    public String getR21OtherIncome() {
        return r21OtherIncome;
    }
    public void setR21OtherIncome(String r21OtherIncome) {
        this.r21OtherIncome = r21OtherIncome;
    }
    public BigDecimal getR21FigBalSheet() {
        return r21FigBalSheet;
    }
    public void setR21FigBalSheet(BigDecimal r21FigBalSheet) {
        this.r21FigBalSheet = r21FigBalSheet;
    }
    public BigDecimal getR21FigBalSheetBwp() {
        return r21FigBalSheetBwp;
    }
    public void setR21FigBalSheetBwp(BigDecimal r21FigBalSheetBwp) {
        this.r21FigBalSheetBwp = r21FigBalSheetBwp;
    }
    public BigDecimal getR21AmtStatementAdj() {
        return r21AmtStatementAdj;
    }
    public void setR21AmtStatementAdj(BigDecimal r21AmtStatementAdj) {
        this.r21AmtStatementAdj = r21AmtStatementAdj;
    }
    public BigDecimal getR21AmtStatementAdjBwp() {
        return r21AmtStatementAdjBwp;
    }
    public void setR21AmtStatementAdjBwp(BigDecimal r21AmtStatementAdjBwp) {
        this.r21AmtStatementAdjBwp = r21AmtStatementAdjBwp;
    }
    public BigDecimal getR21NetAmt() {
        return r21NetAmt;
    }
    public void setR21NetAmt(BigDecimal r21NetAmt) {
        this.r21NetAmt = r21NetAmt;
    }
    public BigDecimal getR21NetAmtBwp() {
        return r21NetAmtBwp;
    }
    public void setR21NetAmtBwp(BigDecimal r21NetAmtBwp) {
        this.r21NetAmtBwp = r21NetAmtBwp;
    }
    public BigDecimal getR21BalSub() {
        return r21BalSub;
    }
    public void setR21BalSub(BigDecimal r21BalSub) {
        this.r21BalSub = r21BalSub;
    }
    public BigDecimal getR21BalSubBwp() {
        return r21BalSubBwp;
    }
    public void setR21BalSubBwp(BigDecimal r21BalSubBwp) {
        this.r21BalSubBwp = r21BalSubBwp;
    }
    public BigDecimal getR21BalSubDiariesBwp() {
        return r21BalSubDiariesBwp;
    }
    public void setR21BalSubDiariesBwp(BigDecimal r21BalSubDiariesBwp) {
        this.r21BalSubDiariesBwp = r21BalSubDiariesBwp;
    }
    public String getR22OtherIncome() {
        return r22OtherIncome;
    }
    public void setR22OtherIncome(String r22OtherIncome) {
        this.r22OtherIncome = r22OtherIncome;
    }
    public BigDecimal getR22FigBalSheet() {
        return r22FigBalSheet;
    }
    public void setR22FigBalSheet(BigDecimal r22FigBalSheet) {
        this.r22FigBalSheet = r22FigBalSheet;
    }
    public BigDecimal getR22FigBalSheetBwp() {
        return r22FigBalSheetBwp;
    }
    public void setR22FigBalSheetBwp(BigDecimal r22FigBalSheetBwp) {
        this.r22FigBalSheetBwp = r22FigBalSheetBwp;
    }
    public BigDecimal getR22AmtStatementAdj() {
        return r22AmtStatementAdj;
    }
    public void setR22AmtStatementAdj(BigDecimal r22AmtStatementAdj) {
        this.r22AmtStatementAdj = r22AmtStatementAdj;
    }
    public BigDecimal getR22AmtStatementAdjBwp() {
        return r22AmtStatementAdjBwp;
    }
    public void setR22AmtStatementAdjBwp(BigDecimal r22AmtStatementAdjBwp) {
        this.r22AmtStatementAdjBwp = r22AmtStatementAdjBwp;
    }
    public BigDecimal getR22NetAmt() {
        return r22NetAmt;
    }
    public void setR22NetAmt(BigDecimal r22NetAmt) {
        this.r22NetAmt = r22NetAmt;
    }
    public BigDecimal getR22NetAmtBwp() {
        return r22NetAmtBwp;
    }
    public void setR22NetAmtBwp(BigDecimal r22NetAmtBwp) {
        this.r22NetAmtBwp = r22NetAmtBwp;
    }
    public BigDecimal getR22BalSub() {
        return r22BalSub;
    }
    public void setR22BalSub(BigDecimal r22BalSub) {
        this.r22BalSub = r22BalSub;
    }
    public BigDecimal getR22BalSubBwp() {
        return r22BalSubBwp;
    }
    public void setR22BalSubBwp(BigDecimal r22BalSubBwp) {
        this.r22BalSubBwp = r22BalSubBwp;
    }
    public BigDecimal getR22BalSubDiariesBwp() {
        return r22BalSubDiariesBwp;
    }
    public void setR22BalSubDiariesBwp(BigDecimal r22BalSubDiariesBwp) {
        this.r22BalSubDiariesBwp = r22BalSubDiariesBwp;
    }
    public String getR23OtherIncome() {
        return r23OtherIncome;
    }
    public void setR23OtherIncome(String r23OtherIncome) {
        this.r23OtherIncome = r23OtherIncome;
    }
    public BigDecimal getR23FigBalSheet() {
        return r23FigBalSheet;
    }
    public void setR23FigBalSheet(BigDecimal r23FigBalSheet) {
        this.r23FigBalSheet = r23FigBalSheet;
    }
    public BigDecimal getR23FigBalSheetBwp() {
        return r23FigBalSheetBwp;
    }
    public void setR23FigBalSheetBwp(BigDecimal r23FigBalSheetBwp) {
        this.r23FigBalSheetBwp = r23FigBalSheetBwp;
    }
    public BigDecimal getR23AmtStatementAdj() {
        return r23AmtStatementAdj;
    }
    public void setR23AmtStatementAdj(BigDecimal r23AmtStatementAdj) {
        this.r23AmtStatementAdj = r23AmtStatementAdj;
    }
    public BigDecimal getR23AmtStatementAdjBwp() {
        return r23AmtStatementAdjBwp;
    }
    public void setR23AmtStatementAdjBwp(BigDecimal r23AmtStatementAdjBwp) {
        this.r23AmtStatementAdjBwp = r23AmtStatementAdjBwp;
    }
    public BigDecimal getR23NetAmt() {
        return r23NetAmt;
    }
    public void setR23NetAmt(BigDecimal r23NetAmt) {
        this.r23NetAmt = r23NetAmt;
    }
    public BigDecimal getR23NetAmtBwp() {
        return r23NetAmtBwp;
    }
    public void setR23NetAmtBwp(BigDecimal r23NetAmtBwp) {
        this.r23NetAmtBwp = r23NetAmtBwp;
    }
    public BigDecimal getR23BalSub() {
        return r23BalSub;
    }
    public void setR23BalSub(BigDecimal r23BalSub) {
        this.r23BalSub = r23BalSub;
    }
    public BigDecimal getR23BalSubBwp() {
        return r23BalSubBwp;
    }
    public void setR23BalSubBwp(BigDecimal r23BalSubBwp) {
        this.r23BalSubBwp = r23BalSubBwp;
    }
    public BigDecimal getR23BalSubDiariesBwp() {
        return r23BalSubDiariesBwp;
    }
    public void setR23BalSubDiariesBwp(BigDecimal r23BalSubDiariesBwp) {
        this.r23BalSubDiariesBwp = r23BalSubDiariesBwp;
    }
    public String getR24OtherIncome() {
        return r24OtherIncome;
    }
    public void setR24OtherIncome(String r24OtherIncome) {
        this.r24OtherIncome = r24OtherIncome;
    }
    public BigDecimal getR24FigBalSheet() {
        return r24FigBalSheet;
    }
    public void setR24FigBalSheet(BigDecimal r24FigBalSheet) {
        this.r24FigBalSheet = r24FigBalSheet;
    }
    public BigDecimal getR24FigBalSheetBwp() {
        return r24FigBalSheetBwp;
    }
    public void setR24FigBalSheetBwp(BigDecimal r24FigBalSheetBwp) {
        this.r24FigBalSheetBwp = r24FigBalSheetBwp;
    }
    public BigDecimal getR24AmtStatementAdj() {
        return r24AmtStatementAdj;
    }
    public void setR24AmtStatementAdj(BigDecimal r24AmtStatementAdj) {
        this.r24AmtStatementAdj = r24AmtStatementAdj;
    }
    public BigDecimal getR24AmtStatementAdjBwp() {
        return r24AmtStatementAdjBwp;
    }
    public void setR24AmtStatementAdjBwp(BigDecimal r24AmtStatementAdjBwp) {
        this.r24AmtStatementAdjBwp = r24AmtStatementAdjBwp;
    }
    public BigDecimal getR24NetAmt() {
        return r24NetAmt;
    }
    public void setR24NetAmt(BigDecimal r24NetAmt) {
        this.r24NetAmt = r24NetAmt;
    }
    public BigDecimal getR24NetAmtBwp() {
        return r24NetAmtBwp;
    }
    public void setR24NetAmtBwp(BigDecimal r24NetAmtBwp) {
        this.r24NetAmtBwp = r24NetAmtBwp;
    }
    public BigDecimal getR24BalSub() {
        return r24BalSub;
    }
    public void setR24BalSub(BigDecimal r24BalSub) {
        this.r24BalSub = r24BalSub;
    }
    public BigDecimal getR24BalSubBwp() {
        return r24BalSubBwp;
    }
    public void setR24BalSubBwp(BigDecimal r24BalSubBwp) {
        this.r24BalSubBwp = r24BalSubBwp;
    }
    public BigDecimal getR24BalSubDiariesBwp() {
        return r24BalSubDiariesBwp;
    }
    public void setR24BalSubDiariesBwp(BigDecimal r24BalSubDiariesBwp) {
        this.r24BalSubDiariesBwp = r24BalSubDiariesBwp;
    }
    public String getR25OtherIncome() {
        return r25OtherIncome;
    }
    public void setR25OtherIncome(String r25OtherIncome) {
        this.r25OtherIncome = r25OtherIncome;
    }
    public BigDecimal getR25FigBalSheet() {
        return r25FigBalSheet;
    }
    public void setR25FigBalSheet(BigDecimal r25FigBalSheet) {
        this.r25FigBalSheet = r25FigBalSheet;
    }
    public BigDecimal getR25FigBalSheetBwp() {
        return r25FigBalSheetBwp;
    }
    public void setR25FigBalSheetBwp(BigDecimal r25FigBalSheetBwp) {
        this.r25FigBalSheetBwp = r25FigBalSheetBwp;
    }
    public BigDecimal getR25AmtStatementAdj() {
        return r25AmtStatementAdj;
    }
    public void setR25AmtStatementAdj(BigDecimal r25AmtStatementAdj) {
        this.r25AmtStatementAdj = r25AmtStatementAdj;
    }
    public BigDecimal getR25AmtStatementAdjBwp() {
        return r25AmtStatementAdjBwp;
    }
    public void setR25AmtStatementAdjBwp(BigDecimal r25AmtStatementAdjBwp) {
        this.r25AmtStatementAdjBwp = r25AmtStatementAdjBwp;
    }
    public BigDecimal getR25NetAmt() {
        return r25NetAmt;
    }
    public void setR25NetAmt(BigDecimal r25NetAmt) {
        this.r25NetAmt = r25NetAmt;
    }
    public BigDecimal getR25NetAmtBwp() {
        return r25NetAmtBwp;
    }
    public void setR25NetAmtBwp(BigDecimal r25NetAmtBwp) {
        this.r25NetAmtBwp = r25NetAmtBwp;
    }
    public BigDecimal getR25BalSub() {
        return r25BalSub;
    }
    public void setR25BalSub(BigDecimal r25BalSub) {
        this.r25BalSub = r25BalSub;
    }
    public BigDecimal getR25BalSubBwp() {
        return r25BalSubBwp;
    }
    public void setR25BalSubBwp(BigDecimal r25BalSubBwp) {
        this.r25BalSubBwp = r25BalSubBwp;
    }
    public BigDecimal getR25BalSubDiariesBwp() {
        return r25BalSubDiariesBwp;
    }
    public void setR25BalSubDiariesBwp(BigDecimal r25BalSubDiariesBwp) {
        this.r25BalSubDiariesBwp = r25BalSubDiariesBwp;
    }
    public String getR26OtherIncome() {
        return r26OtherIncome;
    }
    public void setR26OtherIncome(String r26OtherIncome) {
        this.r26OtherIncome = r26OtherIncome;
    }
    public BigDecimal getR26FigBalSheet() {
        return r26FigBalSheet;
    }
    public void setR26FigBalSheet(BigDecimal r26FigBalSheet) {
        this.r26FigBalSheet = r26FigBalSheet;
    }
    public BigDecimal getR26FigBalSheetBwp() {
        return r26FigBalSheetBwp;
    }
    public void setR26FigBalSheetBwp(BigDecimal r26FigBalSheetBwp) {
        this.r26FigBalSheetBwp = r26FigBalSheetBwp;
    }
    public BigDecimal getR26AmtStatementAdj() {
        return r26AmtStatementAdj;
    }
    public void setR26AmtStatementAdj(BigDecimal r26AmtStatementAdj) {
        this.r26AmtStatementAdj = r26AmtStatementAdj;
    }
    public BigDecimal getR26AmtStatementAdjBwp() {
        return r26AmtStatementAdjBwp;
    }
    public void setR26AmtStatementAdjBwp(BigDecimal r26AmtStatementAdjBwp) {
        this.r26AmtStatementAdjBwp = r26AmtStatementAdjBwp;
    }
    public BigDecimal getR26NetAmt() {
        return r26NetAmt;
    }
    public void setR26NetAmt(BigDecimal r26NetAmt) {
        this.r26NetAmt = r26NetAmt;
    }
    public BigDecimal getR26NetAmtBwp() {
        return r26NetAmtBwp;
    }
    public void setR26NetAmtBwp(BigDecimal r26NetAmtBwp) {
        this.r26NetAmtBwp = r26NetAmtBwp;
    }
    public BigDecimal getR26BalSub() {
        return r26BalSub;
    }
    public void setR26BalSub(BigDecimal r26BalSub) {
        this.r26BalSub = r26BalSub;
    }
    public BigDecimal getR26BalSubBwp() {
        return r26BalSubBwp;
    }
    public void setR26BalSubBwp(BigDecimal r26BalSubBwp) {
        this.r26BalSubBwp = r26BalSubBwp;
    }
    public BigDecimal getR26BalSubDiariesBwp() {
        return r26BalSubDiariesBwp;
    }
    public void setR26BalSubDiariesBwp(BigDecimal r26BalSubDiariesBwp) {
        this.r26BalSubDiariesBwp = r26BalSubDiariesBwp;
    }
    public String getR27OtherIncome() {
        return r27OtherIncome;
    }
    public void setR27OtherIncome(String r27OtherIncome) {
        this.r27OtherIncome = r27OtherIncome;
    }
    public BigDecimal getR27FigBalSheet() {
        return r27FigBalSheet;
    }
    public void setR27FigBalSheet(BigDecimal r27FigBalSheet) {
        this.r27FigBalSheet = r27FigBalSheet;
    }
    public BigDecimal getR27FigBalSheetBwp() {
        return r27FigBalSheetBwp;
    }
    public void setR27FigBalSheetBwp(BigDecimal r27FigBalSheetBwp) {
        this.r27FigBalSheetBwp = r27FigBalSheetBwp;
    }
    public BigDecimal getR27AmtStatementAdj() {
        return r27AmtStatementAdj;
    }
    public void setR27AmtStatementAdj(BigDecimal r27AmtStatementAdj) {
        this.r27AmtStatementAdj = r27AmtStatementAdj;
    }
    public BigDecimal getR27AmtStatementAdjBwp() {
        return r27AmtStatementAdjBwp;
    }
    public void setR27AmtStatementAdjBwp(BigDecimal r27AmtStatementAdjBwp) {
        this.r27AmtStatementAdjBwp = r27AmtStatementAdjBwp;
    }
    public BigDecimal getR27NetAmt() {
        return r27NetAmt;
    }
    public void setR27NetAmt(BigDecimal r27NetAmt) {
        this.r27NetAmt = r27NetAmt;
    }
    public BigDecimal getR27NetAmtBwp() {
        return r27NetAmtBwp;
    }
    public void setR27NetAmtBwp(BigDecimal r27NetAmtBwp) {
        this.r27NetAmtBwp = r27NetAmtBwp;
    }
    public BigDecimal getR27BalSub() {
        return r27BalSub;
    }
    public void setR27BalSub(BigDecimal r27BalSub) {
        this.r27BalSub = r27BalSub;
    }
    public BigDecimal getR27BalSubBwp() {
        return r27BalSubBwp;
    }
    public void setR27BalSubBwp(BigDecimal r27BalSubBwp) {
        this.r27BalSubBwp = r27BalSubBwp;
    }
    public BigDecimal getR27BalSubDiariesBwp() {
        return r27BalSubDiariesBwp;
    }
    public void setR27BalSubDiariesBwp(BigDecimal r27BalSubDiariesBwp) {
        this.r27BalSubDiariesBwp = r27BalSubDiariesBwp;
    }
    public String getR28OtherIncome() {
        return r28OtherIncome;
    }
    public void setR28OtherIncome(String r28OtherIncome) {
        this.r28OtherIncome = r28OtherIncome;
    }
    public BigDecimal getR28FigBalSheet() {
        return r28FigBalSheet;
    }
    public void setR28FigBalSheet(BigDecimal r28FigBalSheet) {
        this.r28FigBalSheet = r28FigBalSheet;
    }
    public BigDecimal getR28FigBalSheetBwp() {
        return r28FigBalSheetBwp;
    }
    public void setR28FigBalSheetBwp(BigDecimal r28FigBalSheetBwp) {
        this.r28FigBalSheetBwp = r28FigBalSheetBwp;
    }
    public BigDecimal getR28AmtStatementAdj() {
        return r28AmtStatementAdj;
    }
    public void setR28AmtStatementAdj(BigDecimal r28AmtStatementAdj) {
        this.r28AmtStatementAdj = r28AmtStatementAdj;
    }
    public BigDecimal getR28AmtStatementAdjBwp() {
        return r28AmtStatementAdjBwp;
    }
    public void setR28AmtStatementAdjBwp(BigDecimal r28AmtStatementAdjBwp) {
        this.r28AmtStatementAdjBwp = r28AmtStatementAdjBwp;
    }
    public BigDecimal getR28NetAmt() {
        return r28NetAmt;
    }
    public void setR28NetAmt(BigDecimal r28NetAmt) {
        this.r28NetAmt = r28NetAmt;
    }
    public BigDecimal getR28NetAmtBwp() {
        return r28NetAmtBwp;
    }
    public void setR28NetAmtBwp(BigDecimal r28NetAmtBwp) {
        this.r28NetAmtBwp = r28NetAmtBwp;
    }
    public BigDecimal getR28BalSub() {
        return r28BalSub;
    }
    public void setR28BalSub(BigDecimal r28BalSub) {
        this.r28BalSub = r28BalSub;
    }
    public BigDecimal getR28BalSubBwp() {
        return r28BalSubBwp;
    }
    public void setR28BalSubBwp(BigDecimal r28BalSubBwp) {
        this.r28BalSubBwp = r28BalSubBwp;
    }
    public BigDecimal getR28BalSubDiariesBwp() {
        return r28BalSubDiariesBwp;
    }
    public void setR28BalSubDiariesBwp(BigDecimal r28BalSubDiariesBwp) {
        this.r28BalSubDiariesBwp = r28BalSubDiariesBwp;
    }
    public String getR29OtherIncome() {
        return r29OtherIncome;
    }
    public void setR29OtherIncome(String r29OtherIncome) {
        this.r29OtherIncome = r29OtherIncome;
    }
    public BigDecimal getR29FigBalSheet() {
        return r29FigBalSheet;
    }
    public void setR29FigBalSheet(BigDecimal r29FigBalSheet) {
        this.r29FigBalSheet = r29FigBalSheet;
    }
    public BigDecimal getR29FigBalSheetBwp() {
        return r29FigBalSheetBwp;
    }
    public void setR29FigBalSheetBwp(BigDecimal r29FigBalSheetBwp) {
        this.r29FigBalSheetBwp = r29FigBalSheetBwp;
    }
    public BigDecimal getR29AmtStatementAdj() {
        return r29AmtStatementAdj;
    }
    public void setR29AmtStatementAdj(BigDecimal r29AmtStatementAdj) {
        this.r29AmtStatementAdj = r29AmtStatementAdj;
    }
    public BigDecimal getR29AmtStatementAdjBwp() {
        return r29AmtStatementAdjBwp;
    }
    public void setR29AmtStatementAdjBwp(BigDecimal r29AmtStatementAdjBwp) {
        this.r29AmtStatementAdjBwp = r29AmtStatementAdjBwp;
    }
    public BigDecimal getR29NetAmt() {
        return r29NetAmt;
    }
    public void setR29NetAmt(BigDecimal r29NetAmt) {
        this.r29NetAmt = r29NetAmt;
    }
    public BigDecimal getR29NetAmtBwp() {
        return r29NetAmtBwp;
    }
    public void setR29NetAmtBwp(BigDecimal r29NetAmtBwp) {
        this.r29NetAmtBwp = r29NetAmtBwp;
    }
    public BigDecimal getR29BalSub() {
        return r29BalSub;
    }
    public void setR29BalSub(BigDecimal r29BalSub) {
        this.r29BalSub = r29BalSub;
    }
    public BigDecimal getR29BalSubBwp() {
        return r29BalSubBwp;
    }
    public void setR29BalSubBwp(BigDecimal r29BalSubBwp) {
        this.r29BalSubBwp = r29BalSubBwp;
    }
    public BigDecimal getR29BalSubDiariesBwp() {
        return r29BalSubDiariesBwp;
    }
    public void setR29BalSubDiariesBwp(BigDecimal r29BalSubDiariesBwp) {
        this.r29BalSubDiariesBwp = r29BalSubDiariesBwp;
    }
    public String getR30OtherIncome() {
        return r30OtherIncome;
    }
    public void setR30OtherIncome(String r30OtherIncome) {
        this.r30OtherIncome = r30OtherIncome;
    }
    public BigDecimal getR30FigBalSheet() {
        return r30FigBalSheet;
    }
    public void setR30FigBalSheet(BigDecimal r30FigBalSheet) {
        this.r30FigBalSheet = r30FigBalSheet;
    }
    public BigDecimal getR30FigBalSheetBwp() {
        return r30FigBalSheetBwp;
    }
    public void setR30FigBalSheetBwp(BigDecimal r30FigBalSheetBwp) {
        this.r30FigBalSheetBwp = r30FigBalSheetBwp;
    }
    public BigDecimal getR30AmtStatementAdj() {
        return r30AmtStatementAdj;
    }
    public void setR30AmtStatementAdj(BigDecimal r30AmtStatementAdj) {
        this.r30AmtStatementAdj = r30AmtStatementAdj;
    }
    public BigDecimal getR30AmtStatementAdjBwp() {
        return r30AmtStatementAdjBwp;
    }
    public void setR30AmtStatementAdjBwp(BigDecimal r30AmtStatementAdjBwp) {
        this.r30AmtStatementAdjBwp = r30AmtStatementAdjBwp;
    }
    public BigDecimal getR30NetAmt() {
        return r30NetAmt;
    }
    public void setR30NetAmt(BigDecimal r30NetAmt) {
        this.r30NetAmt = r30NetAmt;
    }
    public BigDecimal getR30NetAmtBwp() {
        return r30NetAmtBwp;
    }
    public void setR30NetAmtBwp(BigDecimal r30NetAmtBwp) {
        this.r30NetAmtBwp = r30NetAmtBwp;
    }
    public BigDecimal getR30BalSub() {
        return r30BalSub;
    }
    public void setR30BalSub(BigDecimal r30BalSub) {
        this.r30BalSub = r30BalSub;
    }
    public BigDecimal getR30BalSubBwp() {
        return r30BalSubBwp;
    }
    public void setR30BalSubBwp(BigDecimal r30BalSubBwp) {
        this.r30BalSubBwp = r30BalSubBwp;
    }
    public BigDecimal getR30BalSubDiariesBwp() {
        return r30BalSubDiariesBwp;
    }
    public void setR30BalSubDiariesBwp(BigDecimal r30BalSubDiariesBwp) {
        this.r30BalSubDiariesBwp = r30BalSubDiariesBwp;
    }
    public String getR31OtherIncome() {
        return r31OtherIncome;
    }
    public void setR31OtherIncome(String r31OtherIncome) {
        this.r31OtherIncome = r31OtherIncome;
    }
    public BigDecimal getR31FigBalSheet() {
        return r31FigBalSheet;
    }
    public void setR31FigBalSheet(BigDecimal r31FigBalSheet) {
        this.r31FigBalSheet = r31FigBalSheet;
    }
    public BigDecimal getR31FigBalSheetBwp() {
        return r31FigBalSheetBwp;
    }
    public void setR31FigBalSheetBwp(BigDecimal r31FigBalSheetBwp) {
        this.r31FigBalSheetBwp = r31FigBalSheetBwp;
    }
    public BigDecimal getR31AmtStatementAdj() {
        return r31AmtStatementAdj;
    }
    public void setR31AmtStatementAdj(BigDecimal r31AmtStatementAdj) {
        this.r31AmtStatementAdj = r31AmtStatementAdj;
    }
    public BigDecimal getR31AmtStatementAdjBwp() {
        return r31AmtStatementAdjBwp;
    }
    public void setR31AmtStatementAdjBwp(BigDecimal r31AmtStatementAdjBwp) {
        this.r31AmtStatementAdjBwp = r31AmtStatementAdjBwp;
    }
    public BigDecimal getR31NetAmt() {
        return r31NetAmt;
    }
    public void setR31NetAmt(BigDecimal r31NetAmt) {
        this.r31NetAmt = r31NetAmt;
    }
    public BigDecimal getR31NetAmtBwp() {
        return r31NetAmtBwp;
    }
    public void setR31NetAmtBwp(BigDecimal r31NetAmtBwp) {
        this.r31NetAmtBwp = r31NetAmtBwp;
    }
    public BigDecimal getR31BalSub() {
        return r31BalSub;
    }
    public void setR31BalSub(BigDecimal r31BalSub) {
        this.r31BalSub = r31BalSub;
    }
    public BigDecimal getR31BalSubBwp() {
        return r31BalSubBwp;
    }
    public void setR31BalSubBwp(BigDecimal r31BalSubBwp) {
        this.r31BalSubBwp = r31BalSubBwp;
    }
    public BigDecimal getR31BalSubDiariesBwp() {
        return r31BalSubDiariesBwp;
    }
    public void setR31BalSubDiariesBwp(BigDecimal r31BalSubDiariesBwp) {
        this.r31BalSubDiariesBwp = r31BalSubDiariesBwp;
    }
    public String getR40IntrestExpended() {
        return r40IntrestExpended;
    }
    public void setR40IntrestExpended(String r40IntrestExpended) {
        this.r40IntrestExpended = r40IntrestExpended;
    }
    public BigDecimal getR40FigBalSheet() {
        return r40FigBalSheet;
    }
    public void setR40FigBalSheet(BigDecimal r40FigBalSheet) {
        this.r40FigBalSheet = r40FigBalSheet;
    }
    public BigDecimal getR40FigBalSheetBwp() {
        return r40FigBalSheetBwp;
    }
    public void setR40FigBalSheetBwp(BigDecimal r40FigBalSheetBwp) {
        this.r40FigBalSheetBwp = r40FigBalSheetBwp;
    }
    public BigDecimal getR40AmtStatementAdj() {
        return r40AmtStatementAdj;
    }
    public void setR40AmtStatementAdj(BigDecimal r40AmtStatementAdj) {
        this.r40AmtStatementAdj = r40AmtStatementAdj;
    }
    public BigDecimal getR40AmtStatementAdjBwp() {
        return r40AmtStatementAdjBwp;
    }
    public void setR40AmtStatementAdjBwp(BigDecimal r40AmtStatementAdjBwp) {
        this.r40AmtStatementAdjBwp = r40AmtStatementAdjBwp;
    }
    public BigDecimal getR40NetAmt() {
        return r40NetAmt;
    }
    public void setR40NetAmt(BigDecimal r40NetAmt) {
        this.r40NetAmt = r40NetAmt;
    }
    public BigDecimal getR40NetAmtBwp() {
        return r40NetAmtBwp;
    }
    public void setR40NetAmtBwp(BigDecimal r40NetAmtBwp) {
        this.r40NetAmtBwp = r40NetAmtBwp;
    }
    public BigDecimal getR40BalSub() {
        return r40BalSub;
    }
    public void setR40BalSub(BigDecimal r40BalSub) {
        this.r40BalSub = r40BalSub;
    }
    public BigDecimal getR40BalSubBwp() {
        return r40BalSubBwp;
    }
    public void setR40BalSubBwp(BigDecimal r40BalSubBwp) {
        this.r40BalSubBwp = r40BalSubBwp;
    }
    public BigDecimal getR40BalSubDiariesBwp() {
        return r40BalSubDiariesBwp;
    }
    public void setR40BalSubDiariesBwp(BigDecimal r40BalSubDiariesBwp) {
        this.r40BalSubDiariesBwp = r40BalSubDiariesBwp;
    }
    public String getR41IntrestExpended() {
        return r41IntrestExpended;
    }
    public void setR41IntrestExpended(String r41IntrestExpended) {
        this.r41IntrestExpended = r41IntrestExpended;
    }
    public BigDecimal getR41FigBalSheet() {
        return r41FigBalSheet;
    }
    public void setR41FigBalSheet(BigDecimal r41FigBalSheet) {
        this.r41FigBalSheet = r41FigBalSheet;
    }
    public BigDecimal getR41FigBalSheetBwp() {
        return r41FigBalSheetBwp;
    }
    public void setR41FigBalSheetBwp(BigDecimal r41FigBalSheetBwp) {
        this.r41FigBalSheetBwp = r41FigBalSheetBwp;
    }
    public BigDecimal getR41AmtStatementAdj() {
        return r41AmtStatementAdj;
    }
    public void setR41AmtStatementAdj(BigDecimal r41AmtStatementAdj) {
        this.r41AmtStatementAdj = r41AmtStatementAdj;
    }
    public BigDecimal getR41AmtStatementAdjBwp() {
        return r41AmtStatementAdjBwp;
    }
    public void setR41AmtStatementAdjBwp(BigDecimal r41AmtStatementAdjBwp) {
        this.r41AmtStatementAdjBwp = r41AmtStatementAdjBwp;
    }
    public BigDecimal getR41NetAmt() {
        return r41NetAmt;
    }
    public void setR41NetAmt(BigDecimal r41NetAmt) {
        this.r41NetAmt = r41NetAmt;
    }
    public BigDecimal getR41NetAmtBwp() {
        return r41NetAmtBwp;
    }
    public void setR41NetAmtBwp(BigDecimal r41NetAmtBwp) {
        this.r41NetAmtBwp = r41NetAmtBwp;
    }
    public BigDecimal getR41BalSub() {
        return r41BalSub;
    }
    public void setR41BalSub(BigDecimal r41BalSub) {
        this.r41BalSub = r41BalSub;
    }
    public BigDecimal getR41BalSubBwp() {
        return r41BalSubBwp;
    }
    public void setR41BalSubBwp(BigDecimal r41BalSubBwp) {
        this.r41BalSubBwp = r41BalSubBwp;
    }
    public BigDecimal getR41BalSubDiariesBwp() {
        return r41BalSubDiariesBwp;
    }
    public void setR41BalSubDiariesBwp(BigDecimal r41BalSubDiariesBwp) {
        this.r41BalSubDiariesBwp = r41BalSubDiariesBwp;
    }
    public String getR42IntrestExpended() {
        return r42IntrestExpended;
    }
    public void setR42IntrestExpended(String r42IntrestExpended) {
        this.r42IntrestExpended = r42IntrestExpended;
    }
    public BigDecimal getR42FigBalSheet() {
        return r42FigBalSheet;
    }
    public void setR42FigBalSheet(BigDecimal r42FigBalSheet) {
        this.r42FigBalSheet = r42FigBalSheet;
    }
    public BigDecimal getR42FigBalSheetBwp() {
        return r42FigBalSheetBwp;
    }
    public void setR42FigBalSheetBwp(BigDecimal r42FigBalSheetBwp) {
        this.r42FigBalSheetBwp = r42FigBalSheetBwp;
    }
    public BigDecimal getR42AmtStatementAdj() {
        return r42AmtStatementAdj;
    }
    public void setR42AmtStatementAdj(BigDecimal r42AmtStatementAdj) {
        this.r42AmtStatementAdj = r42AmtStatementAdj;
    }
    public BigDecimal getR42AmtStatementAdjBwp() {
        return r42AmtStatementAdjBwp;
    }
    public void setR42AmtStatementAdjBwp(BigDecimal r42AmtStatementAdjBwp) {
        this.r42AmtStatementAdjBwp = r42AmtStatementAdjBwp;
    }
    public BigDecimal getR42NetAmt() {
        return r42NetAmt;
    }
    public void setR42NetAmt(BigDecimal r42NetAmt) {
        this.r42NetAmt = r42NetAmt;
    }
    public BigDecimal getR42NetAmtBwp() {
        return r42NetAmtBwp;
    }
    public void setR42NetAmtBwp(BigDecimal r42NetAmtBwp) {
        this.r42NetAmtBwp = r42NetAmtBwp;
    }
    public BigDecimal getR42BalSub() {
        return r42BalSub;
    }
    public void setR42BalSub(BigDecimal r42BalSub) {
        this.r42BalSub = r42BalSub;
    }
    public BigDecimal getR42BalSubBwp() {
        return r42BalSubBwp;
    }
    public void setR42BalSubBwp(BigDecimal r42BalSubBwp) {
        this.r42BalSubBwp = r42BalSubBwp;
    }
    public BigDecimal getR42BalSubDiariesBwp() {
        return r42BalSubDiariesBwp;
    }
    public void setR42BalSubDiariesBwp(BigDecimal r42BalSubDiariesBwp) {
        this.r42BalSubDiariesBwp = r42BalSubDiariesBwp;
    }
    public String getR43IntrestExpended() {
        return r43IntrestExpended;
    }
    public void setR43IntrestExpended(String r43IntrestExpended) {
        this.r43IntrestExpended = r43IntrestExpended;
    }
    public BigDecimal getR43FigBalSheet() {
        return r43FigBalSheet;
    }
    public void setR43FigBalSheet(BigDecimal r43FigBalSheet) {
        this.r43FigBalSheet = r43FigBalSheet;
    }
    public BigDecimal getR43FigBalSheetBwp() {
        return r43FigBalSheetBwp;
    }
    public void setR43FigBalSheetBwp(BigDecimal r43FigBalSheetBwp) {
        this.r43FigBalSheetBwp = r43FigBalSheetBwp;
    }
    public BigDecimal getR43AmtStatementAdj() {
        return r43AmtStatementAdj;
    }
    public void setR43AmtStatementAdj(BigDecimal r43AmtStatementAdj) {
        this.r43AmtStatementAdj = r43AmtStatementAdj;
    }
    public BigDecimal getR43AmtStatementAdjBwp() {
        return r43AmtStatementAdjBwp;
    }
    public void setR43AmtStatementAdjBwp(BigDecimal r43AmtStatementAdjBwp) {
        this.r43AmtStatementAdjBwp = r43AmtStatementAdjBwp;
    }
    public BigDecimal getR43NetAmt() {
        return r43NetAmt;
    }
    public void setR43NetAmt(BigDecimal r43NetAmt) {
        this.r43NetAmt = r43NetAmt;
    }
    public BigDecimal getR43NetAmtBwp() {
        return r43NetAmtBwp;
    }
    public void setR43NetAmtBwp(BigDecimal r43NetAmtBwp) {
        this.r43NetAmtBwp = r43NetAmtBwp;
    }
    public BigDecimal getR43BalSub() {
        return r43BalSub;
    }
    public void setR43BalSub(BigDecimal r43BalSub) {
        this.r43BalSub = r43BalSub;
    }
    public BigDecimal getR43BalSubBwp() {
        return r43BalSubBwp;
    }
    public void setR43BalSubBwp(BigDecimal r43BalSubBwp) {
        this.r43BalSubBwp = r43BalSubBwp;
    }
    public BigDecimal getR43BalSubDiariesBwp() {
        return r43BalSubDiariesBwp;
    }
    public void setR43BalSubDiariesBwp(BigDecimal r43BalSubDiariesBwp) {
        this.r43BalSubDiariesBwp = r43BalSubDiariesBwp;
    }
    public String getR48OperatingExpenses() {
        return r48OperatingExpenses;
    }
    public void setR48OperatingExpenses(String r48OperatingExpenses) {
        this.r48OperatingExpenses = r48OperatingExpenses;
    }
    public BigDecimal getR48FigBalSheet() {
        return r48FigBalSheet;
    }
    public void setR48FigBalSheet(BigDecimal r48FigBalSheet) {
        this.r48FigBalSheet = r48FigBalSheet;
    }
    public BigDecimal getR48FigBalSheetBwp() {
        return r48FigBalSheetBwp;
    }
    public void setR48FigBalSheetBwp(BigDecimal r48FigBalSheetBwp) {
        this.r48FigBalSheetBwp = r48FigBalSheetBwp;
    }
    public BigDecimal getR48AmtStatementAdj() {
        return r48AmtStatementAdj;
    }
    public void setR48AmtStatementAdj(BigDecimal r48AmtStatementAdj) {
        this.r48AmtStatementAdj = r48AmtStatementAdj;
    }
    public BigDecimal getR48AmtStatementAdjBwp() {
        return r48AmtStatementAdjBwp;
    }
    public void setR48AmtStatementAdjBwp(BigDecimal r48AmtStatementAdjBwp) {
        this.r48AmtStatementAdjBwp = r48AmtStatementAdjBwp;
    }
    public BigDecimal getR48NetAmt() {
        return r48NetAmt;
    }
    public void setR48NetAmt(BigDecimal r48NetAmt) {
        this.r48NetAmt = r48NetAmt;
    }
    public BigDecimal getR48NetAmtBwp() {
        return r48NetAmtBwp;
    }
    public void setR48NetAmtBwp(BigDecimal r48NetAmtBwp) {
        this.r48NetAmtBwp = r48NetAmtBwp;
    }
    public BigDecimal getR48BalSub() {
        return r48BalSub;
    }
    public void setR48BalSub(BigDecimal r48BalSub) {
        this.r48BalSub = r48BalSub;
    }
    public BigDecimal getR48BalSubBwp() {
        return r48BalSubBwp;
    }
    public void setR48BalSubBwp(BigDecimal r48BalSubBwp) {
        this.r48BalSubBwp = r48BalSubBwp;
    }
    public BigDecimal getR48BalSubDiariesBwp() {
        return r48BalSubDiariesBwp;
    }
    public void setR48BalSubDiariesBwp(BigDecimal r48BalSubDiariesBwp) {
        this.r48BalSubDiariesBwp = r48BalSubDiariesBwp;
    }
    public String getR49OperatingExpenses() {
        return r49OperatingExpenses;
    }
    public void setR49OperatingExpenses(String r49OperatingExpenses) {
        this.r49OperatingExpenses = r49OperatingExpenses;
    }
    public BigDecimal getR49FigBalSheet() {
        return r49FigBalSheet;
    }
    public void setR49FigBalSheet(BigDecimal r49FigBalSheet) {
        this.r49FigBalSheet = r49FigBalSheet;
    }
    public BigDecimal getR49FigBalSheetBwp() {
        return r49FigBalSheetBwp;
    }
    public void setR49FigBalSheetBwp(BigDecimal r49FigBalSheetBwp) {
        this.r49FigBalSheetBwp = r49FigBalSheetBwp;
    }
    public BigDecimal getR49AmtStatementAdj() {
        return r49AmtStatementAdj;
    }
    public void setR49AmtStatementAdj(BigDecimal r49AmtStatementAdj) {
        this.r49AmtStatementAdj = r49AmtStatementAdj;
    }
    public BigDecimal getR49AmtStatementAdjBwp() {
        return r49AmtStatementAdjBwp;
    }
    public void setR49AmtStatementAdjBwp(BigDecimal r49AmtStatementAdjBwp) {
        this.r49AmtStatementAdjBwp = r49AmtStatementAdjBwp;
    }
    public BigDecimal getR49NetAmt() {
        return r49NetAmt;
    }
    public void setR49NetAmt(BigDecimal r49NetAmt) {
        this.r49NetAmt = r49NetAmt;
    }
    public BigDecimal getR49NetAmtBwp() {
        return r49NetAmtBwp;
    }
    public void setR49NetAmtBwp(BigDecimal r49NetAmtBwp) {
        this.r49NetAmtBwp = r49NetAmtBwp;
    }
    public BigDecimal getR49BalSub() {
        return r49BalSub;
    }
    public void setR49BalSub(BigDecimal r49BalSub) {
        this.r49BalSub = r49BalSub;
    }
    public BigDecimal getR49BalSubBwp() {
        return r49BalSubBwp;
    }
    public void setR49BalSubBwp(BigDecimal r49BalSubBwp) {
        this.r49BalSubBwp = r49BalSubBwp;
    }
    public BigDecimal getR49BalSubDiariesBwp() {
        return r49BalSubDiariesBwp;
    }
    public void setR49BalSubDiariesBwp(BigDecimal r49BalSubDiariesBwp) {
        this.r49BalSubDiariesBwp = r49BalSubDiariesBwp;
    }
    public String getR50OperatingExpenses() {
        return r50OperatingExpenses;
    }
    public void setR50OperatingExpenses(String r50OperatingExpenses) {
        this.r50OperatingExpenses = r50OperatingExpenses;
    }
    public BigDecimal getR50FigBalSheet() {
        return r50FigBalSheet;
    }
    public void setR50FigBalSheet(BigDecimal r50FigBalSheet) {
        this.r50FigBalSheet = r50FigBalSheet;
    }
    public BigDecimal getR50FigBalSheetBwp() {
        return r50FigBalSheetBwp;
    }
    public void setR50FigBalSheetBwp(BigDecimal r50FigBalSheetBwp) {
        this.r50FigBalSheetBwp = r50FigBalSheetBwp;
    }
    public BigDecimal getR50AmtStatementAdj() {
        return r50AmtStatementAdj;
    }
    public void setR50AmtStatementAdj(BigDecimal r50AmtStatementAdj) {
        this.r50AmtStatementAdj = r50AmtStatementAdj;
    }
    public BigDecimal getR50AmtStatementAdjBwp() {
        return r50AmtStatementAdjBwp;
    }
    public void setR50AmtStatementAdjBwp(BigDecimal r50AmtStatementAdjBwp) {
        this.r50AmtStatementAdjBwp = r50AmtStatementAdjBwp;
    }
    public BigDecimal getR50NetAmt() {
        return r50NetAmt;
    }
    public void setR50NetAmt(BigDecimal r50NetAmt) {
        this.r50NetAmt = r50NetAmt;
    }
    public BigDecimal getR50NetAmtBwp() {
        return r50NetAmtBwp;
    }
    public void setR50NetAmtBwp(BigDecimal r50NetAmtBwp) {
        this.r50NetAmtBwp = r50NetAmtBwp;
    }
    public BigDecimal getR50BalSub() {
        return r50BalSub;
    }
    public void setR50BalSub(BigDecimal r50BalSub) {
        this.r50BalSub = r50BalSub;
    }
    public BigDecimal getR50BalSubBwp() {
        return r50BalSubBwp;
    }
    public void setR50BalSubBwp(BigDecimal r50BalSubBwp) {
        this.r50BalSubBwp = r50BalSubBwp;
    }
    public BigDecimal getR50BalSubDiariesBwp() {
        return r50BalSubDiariesBwp;
    }
    public void setR50BalSubDiariesBwp(BigDecimal r50BalSubDiariesBwp) {
        this.r50BalSubDiariesBwp = r50BalSubDiariesBwp;
    }
    public String getR51OperatingExpenses() {
        return r51OperatingExpenses;
    }
    public void setR51OperatingExpenses(String r51OperatingExpenses) {
        this.r51OperatingExpenses = r51OperatingExpenses;
    }
    public BigDecimal getR51FigBalSheet() {
        return r51FigBalSheet;
    }
    public void setR51FigBalSheet(BigDecimal r51FigBalSheet) {
        this.r51FigBalSheet = r51FigBalSheet;
    }
    public BigDecimal getR51FigBalSheetBwp() {
        return r51FigBalSheetBwp;
    }
    public void setR51FigBalSheetBwp(BigDecimal r51FigBalSheetBwp) {
        this.r51FigBalSheetBwp = r51FigBalSheetBwp;
    }
    public BigDecimal getR51AmtStatementAdj() {
        return r51AmtStatementAdj;
    }
    public void setR51AmtStatementAdj(BigDecimal r51AmtStatementAdj) {
        this.r51AmtStatementAdj = r51AmtStatementAdj;
    }
    public BigDecimal getR51AmtStatementAdjBwp() {
        return r51AmtStatementAdjBwp;
    }
    public void setR51AmtStatementAdjBwp(BigDecimal r51AmtStatementAdjBwp) {
        this.r51AmtStatementAdjBwp = r51AmtStatementAdjBwp;
    }
    public BigDecimal getR51NetAmt() {
        return r51NetAmt;
    }
    public void setR51NetAmt(BigDecimal r51NetAmt) {
        this.r51NetAmt = r51NetAmt;
    }
    public BigDecimal getR51NetAmtBwp() {
        return r51NetAmtBwp;
    }
    public void setR51NetAmtBwp(BigDecimal r51NetAmtBwp) {
        this.r51NetAmtBwp = r51NetAmtBwp;
    }
    public BigDecimal getR51BalSub() {
        return r51BalSub;
    }
    public void setR51BalSub(BigDecimal r51BalSub) {
        this.r51BalSub = r51BalSub;
    }
    public BigDecimal getR51BalSubBwp() {
        return r51BalSubBwp;
    }
    public void setR51BalSubBwp(BigDecimal r51BalSubBwp) {
        this.r51BalSubBwp = r51BalSubBwp;
    }
    public BigDecimal getR51BalSubDiariesBwp() {
        return r51BalSubDiariesBwp;
    }
    public void setR51BalSubDiariesBwp(BigDecimal r51BalSubDiariesBwp) {
        this.r51BalSubDiariesBwp = r51BalSubDiariesBwp;
    }
    public String getR52OperatingExpenses() {
        return r52OperatingExpenses;
    }
    public void setR52OperatingExpenses(String r52OperatingExpenses) {
        this.r52OperatingExpenses = r52OperatingExpenses;
    }
    public BigDecimal getR52FigBalSheet() {
        return r52FigBalSheet;
    }
    public void setR52FigBalSheet(BigDecimal r52FigBalSheet) {
        this.r52FigBalSheet = r52FigBalSheet;
    }
    public BigDecimal getR52FigBalSheetBwp() {
        return r52FigBalSheetBwp;
    }
    public void setR52FigBalSheetBwp(BigDecimal r52FigBalSheetBwp) {
        this.r52FigBalSheetBwp = r52FigBalSheetBwp;
    }
    public BigDecimal getR52AmtStatementAdj() {
        return r52AmtStatementAdj;
    }
    public void setR52AmtStatementAdj(BigDecimal r52AmtStatementAdj) {
        this.r52AmtStatementAdj = r52AmtStatementAdj;
    }
    public BigDecimal getR52AmtStatementAdjBwp() {
        return r52AmtStatementAdjBwp;
    }
    public void setR52AmtStatementAdjBwp(BigDecimal r52AmtStatementAdjBwp) {
        this.r52AmtStatementAdjBwp = r52AmtStatementAdjBwp;
    }
    public BigDecimal getR52NetAmt() {
        return r52NetAmt;
    }
    public void setR52NetAmt(BigDecimal r52NetAmt) {
        this.r52NetAmt = r52NetAmt;
    }
    public BigDecimal getR52NetAmtBwp() {
        return r52NetAmtBwp;
    }
    public void setR52NetAmtBwp(BigDecimal r52NetAmtBwp) {
        this.r52NetAmtBwp = r52NetAmtBwp;
    }
    public BigDecimal getR52BalSub() {
        return r52BalSub;
    }
    public void setR52BalSub(BigDecimal r52BalSub) {
        this.r52BalSub = r52BalSub;
    }
    public BigDecimal getR52BalSubBwp() {
        return r52BalSubBwp;
    }
    public void setR52BalSubBwp(BigDecimal r52BalSubBwp) {
        this.r52BalSubBwp = r52BalSubBwp;
    }
    public BigDecimal getR52BalSubDiariesBwp() {
        return r52BalSubDiariesBwp;
    }
    public void setR52BalSubDiariesBwp(BigDecimal r52BalSubDiariesBwp) {
        this.r52BalSubDiariesBwp = r52BalSubDiariesBwp;
    }
    public String getR53OperatingExpenses() {
        return r53OperatingExpenses;
    }
    public void setR53OperatingExpenses(String r53OperatingExpenses) {
        this.r53OperatingExpenses = r53OperatingExpenses;
    }
    public BigDecimal getR53FigBalSheet() {
        return r53FigBalSheet;
    }
    public void setR53FigBalSheet(BigDecimal r53FigBalSheet) {
        this.r53FigBalSheet = r53FigBalSheet;
    }
    public BigDecimal getR53FigBalSheetBwp() {
        return r53FigBalSheetBwp;
    }
    public void setR53FigBalSheetBwp(BigDecimal r53FigBalSheetBwp) {
        this.r53FigBalSheetBwp = r53FigBalSheetBwp;
    }
    public BigDecimal getR53AmtStatementAdj() {
        return r53AmtStatementAdj;
    }
    public void setR53AmtStatementAdj(BigDecimal r53AmtStatementAdj) {
        this.r53AmtStatementAdj = r53AmtStatementAdj;
    }
    public BigDecimal getR53AmtStatementAdjBwp() {
        return r53AmtStatementAdjBwp;
    }
    public void setR53AmtStatementAdjBwp(BigDecimal r53AmtStatementAdjBwp) {
        this.r53AmtStatementAdjBwp = r53AmtStatementAdjBwp;
    }
    public BigDecimal getR53NetAmt() {
        return r53NetAmt;
    }
    public void setR53NetAmt(BigDecimal r53NetAmt) {
        this.r53NetAmt = r53NetAmt;
    }
    public BigDecimal getR53NetAmtBwp() {
        return r53NetAmtBwp;
    }
    public void setR53NetAmtBwp(BigDecimal r53NetAmtBwp) {
        this.r53NetAmtBwp = r53NetAmtBwp;
    }
    public BigDecimal getR53BalSub() {
        return r53BalSub;
    }
    public void setR53BalSub(BigDecimal r53BalSub) {
        this.r53BalSub = r53BalSub;
    }
    public BigDecimal getR53BalSubBwp() {
        return r53BalSubBwp;
    }
    public void setR53BalSubBwp(BigDecimal r53BalSubBwp) {
        this.r53BalSubBwp = r53BalSubBwp;
    }
    public BigDecimal getR53BalSubDiariesBwp() {
        return r53BalSubDiariesBwp;
    }
    public void setR53BalSubDiariesBwp(BigDecimal r53BalSubDiariesBwp) {
        this.r53BalSubDiariesBwp = r53BalSubDiariesBwp;
    }
    public String getR54OperatingExpenses() {
        return r54OperatingExpenses;
    }
    public void setR54OperatingExpenses(String r54OperatingExpenses) {
        this.r54OperatingExpenses = r54OperatingExpenses;
    }
    public BigDecimal getR54FigBalSheet() {
        return r54FigBalSheet;
    }
    public void setR54FigBalSheet(BigDecimal r54FigBalSheet) {
        this.r54FigBalSheet = r54FigBalSheet;
    }
    public BigDecimal getR54FigBalSheetBwp() {
        return r54FigBalSheetBwp;
    }
    public void setR54FigBalSheetBwp(BigDecimal r54FigBalSheetBwp) {
        this.r54FigBalSheetBwp = r54FigBalSheetBwp;
    }
    public BigDecimal getR54AmtStatementAdj() {
        return r54AmtStatementAdj;
    }
    public void setR54AmtStatementAdj(BigDecimal r54AmtStatementAdj) {
        this.r54AmtStatementAdj = r54AmtStatementAdj;
    }
    public BigDecimal getR54AmtStatementAdjBwp() {
        return r54AmtStatementAdjBwp;
    }
    public void setR54AmtStatementAdjBwp(BigDecimal r54AmtStatementAdjBwp) {
        this.r54AmtStatementAdjBwp = r54AmtStatementAdjBwp;
    }
    public BigDecimal getR54NetAmt() {
        return r54NetAmt;
    }
    public void setR54NetAmt(BigDecimal r54NetAmt) {
        this.r54NetAmt = r54NetAmt;
    }
    public BigDecimal getR54NetAmtBwp() {
        return r54NetAmtBwp;
    }
    public void setR54NetAmtBwp(BigDecimal r54NetAmtBwp) {
        this.r54NetAmtBwp = r54NetAmtBwp;
    }
    public BigDecimal getR54BalSub() {
        return r54BalSub;
    }
    public void setR54BalSub(BigDecimal r54BalSub) {
        this.r54BalSub = r54BalSub;
    }
    public BigDecimal getR54BalSubBwp() {
        return r54BalSubBwp;
    }
    public void setR54BalSubBwp(BigDecimal r54BalSubBwp) {
        this.r54BalSubBwp = r54BalSubBwp;
    }
    public BigDecimal getR54BalSubDiariesBwp() {
        return r54BalSubDiariesBwp;
    }
    public void setR54BalSubDiariesBwp(BigDecimal r54BalSubDiariesBwp) {
        this.r54BalSubDiariesBwp = r54BalSubDiariesBwp;
    }
    public String getR55OperatingExpenses() {
        return r55OperatingExpenses;
    }
    public void setR55OperatingExpenses(String r55OperatingExpenses) {
        this.r55OperatingExpenses = r55OperatingExpenses;
    }
    public BigDecimal getR55FigBalSheet() {
        return r55FigBalSheet;
    }
    public void setR55FigBalSheet(BigDecimal r55FigBalSheet) {
        this.r55FigBalSheet = r55FigBalSheet;
    }
    public BigDecimal getR55FigBalSheetBwp() {
        return r55FigBalSheetBwp;
    }
    public void setR55FigBalSheetBwp(BigDecimal r55FigBalSheetBwp) {
        this.r55FigBalSheetBwp = r55FigBalSheetBwp;
    }
    public BigDecimal getR55AmtStatementAdj() {
        return r55AmtStatementAdj;
    }
    public void setR55AmtStatementAdj(BigDecimal r55AmtStatementAdj) {
        this.r55AmtStatementAdj = r55AmtStatementAdj;
    }
    public BigDecimal getR55AmtStatementAdjBwp() {
        return r55AmtStatementAdjBwp;
    }
    public void setR55AmtStatementAdjBwp(BigDecimal r55AmtStatementAdjBwp) {
        this.r55AmtStatementAdjBwp = r55AmtStatementAdjBwp;
    }
    public BigDecimal getR55NetAmt() {
        return r55NetAmt;
    }
    public void setR55NetAmt(BigDecimal r55NetAmt) {
        this.r55NetAmt = r55NetAmt;
    }
    public BigDecimal getR55NetAmtBwp() {
        return r55NetAmtBwp;
    }
    public void setR55NetAmtBwp(BigDecimal r55NetAmtBwp) {
        this.r55NetAmtBwp = r55NetAmtBwp;
    }
    public BigDecimal getR55BalSub() {
        return r55BalSub;
    }
    public void setR55BalSub(BigDecimal r55BalSub) {
        this.r55BalSub = r55BalSub;
    }
    public BigDecimal getR55BalSubBwp() {
        return r55BalSubBwp;
    }
    public void setR55BalSubBwp(BigDecimal r55BalSubBwp) {
        this.r55BalSubBwp = r55BalSubBwp;
    }
    public BigDecimal getR55BalSubDiariesBwp() {
        return r55BalSubDiariesBwp;
    }
    public void setR55BalSubDiariesBwp(BigDecimal r55BalSubDiariesBwp) {
        this.r55BalSubDiariesBwp = r55BalSubDiariesBwp;
    }
    public String getR56OperatingExpenses() {
        return r56OperatingExpenses;
    }
    public void setR56OperatingExpenses(String r56OperatingExpenses) {
        this.r56OperatingExpenses = r56OperatingExpenses;
    }
    public BigDecimal getR56FigBalSheet() {
        return r56FigBalSheet;
    }
    public void setR56FigBalSheet(BigDecimal r56FigBalSheet) {
        this.r56FigBalSheet = r56FigBalSheet;
    }
    public BigDecimal getR56FigBalSheetBwp() {
        return r56FigBalSheetBwp;
    }
    public void setR56FigBalSheetBwp(BigDecimal r56FigBalSheetBwp) {
        this.r56FigBalSheetBwp = r56FigBalSheetBwp;
    }
    public BigDecimal getR56AmtStatementAdj() {
        return r56AmtStatementAdj;
    }
    public void setR56AmtStatementAdj(BigDecimal r56AmtStatementAdj) {
        this.r56AmtStatementAdj = r56AmtStatementAdj;
    }
    public BigDecimal getR56AmtStatementAdjBwp() {
        return r56AmtStatementAdjBwp;
    }
    public void setR56AmtStatementAdjBwp(BigDecimal r56AmtStatementAdjBwp) {
        this.r56AmtStatementAdjBwp = r56AmtStatementAdjBwp;
    }
    public BigDecimal getR56NetAmt() {
        return r56NetAmt;
    }
    public void setR56NetAmt(BigDecimal r56NetAmt) {
        this.r56NetAmt = r56NetAmt;
    }
    public BigDecimal getR56NetAmtBwp() {
        return r56NetAmtBwp;
    }
    public void setR56NetAmtBwp(BigDecimal r56NetAmtBwp) {
        this.r56NetAmtBwp = r56NetAmtBwp;
    }
    public BigDecimal getR56BalSub() {
        return r56BalSub;
    }
    public void setR56BalSub(BigDecimal r56BalSub) {
        this.r56BalSub = r56BalSub;
    }
    public BigDecimal getR56BalSubBwp() {
        return r56BalSubBwp;
    }
    public void setR56BalSubBwp(BigDecimal r56BalSubBwp) {
        this.r56BalSubBwp = r56BalSubBwp;
    }
    public BigDecimal getR56BalSubDiariesBwp() {
        return r56BalSubDiariesBwp;
    }
    public void setR56BalSubDiariesBwp(BigDecimal r56BalSubDiariesBwp) {
        this.r56BalSubDiariesBwp = r56BalSubDiariesBwp;
    }
    public String getR57OperatingExpenses() {
        return r57OperatingExpenses;
    }
    public void setR57OperatingExpenses(String r57OperatingExpenses) {
        this.r57OperatingExpenses = r57OperatingExpenses;
    }
    public BigDecimal getR57FigBalSheet() {
        return r57FigBalSheet;
    }
    public void setR57FigBalSheet(BigDecimal r57FigBalSheet) {
        this.r57FigBalSheet = r57FigBalSheet;
    }
    public BigDecimal getR57FigBalSheetBwp() {
        return r57FigBalSheetBwp;
    }
    public void setR57FigBalSheetBwp(BigDecimal r57FigBalSheetBwp) {
        this.r57FigBalSheetBwp = r57FigBalSheetBwp;
    }
    public BigDecimal getR57AmtStatementAdj() {
        return r57AmtStatementAdj;
    }
    public void setR57AmtStatementAdj(BigDecimal r57AmtStatementAdj) {
        this.r57AmtStatementAdj = r57AmtStatementAdj;
    }
    public BigDecimal getR57AmtStatementAdjBwp() {
        return r57AmtStatementAdjBwp;
    }
    public void setR57AmtStatementAdjBwp(BigDecimal r57AmtStatementAdjBwp) {
        this.r57AmtStatementAdjBwp = r57AmtStatementAdjBwp;
    }
    public BigDecimal getR57NetAmt() {
        return r57NetAmt;
    }
    public void setR57NetAmt(BigDecimal r57NetAmt) {
        this.r57NetAmt = r57NetAmt;
    }
    public BigDecimal getR57NetAmtBwp() {
        return r57NetAmtBwp;
    }
    public void setR57NetAmtBwp(BigDecimal r57NetAmtBwp) {
        this.r57NetAmtBwp = r57NetAmtBwp;
    }
    public BigDecimal getR57BalSub() {
        return r57BalSub;
    }
    public void setR57BalSub(BigDecimal r57BalSub) {
        this.r57BalSub = r57BalSub;
    }
    public BigDecimal getR57BalSubBwp() {
        return r57BalSubBwp;
    }
    public void setR57BalSubBwp(BigDecimal r57BalSubBwp) {
        this.r57BalSubBwp = r57BalSubBwp;
    }
    public BigDecimal getR57BalSubDiariesBwp() {
        return r57BalSubDiariesBwp;
    }
    public void setR57BalSubDiariesBwp(BigDecimal r57BalSubDiariesBwp) {
        this.r57BalSubDiariesBwp = r57BalSubDiariesBwp;
    }
    public String getR58OperatingExpenses() {
        return r58OperatingExpenses;
    }
    public void setR58OperatingExpenses(String r58OperatingExpenses) {
        this.r58OperatingExpenses = r58OperatingExpenses;
    }
    public BigDecimal getR58FigBalSheet() {
        return r58FigBalSheet;
    }
    public void setR58FigBalSheet(BigDecimal r58FigBalSheet) {
        this.r58FigBalSheet = r58FigBalSheet;
    }
    public BigDecimal getR58FigBalSheetBwp() {
        return r58FigBalSheetBwp;
    }
    public void setR58FigBalSheetBwp(BigDecimal r58FigBalSheetBwp) {
        this.r58FigBalSheetBwp = r58FigBalSheetBwp;
    }
    public BigDecimal getR58AmtStatementAdj() {
        return r58AmtStatementAdj;
    }
    public void setR58AmtStatementAdj(BigDecimal r58AmtStatementAdj) {
        this.r58AmtStatementAdj = r58AmtStatementAdj;
    }
    public BigDecimal getR58AmtStatementAdjBwp() {
        return r58AmtStatementAdjBwp;
    }
    public void setR58AmtStatementAdjBwp(BigDecimal r58AmtStatementAdjBwp) {
        this.r58AmtStatementAdjBwp = r58AmtStatementAdjBwp;
    }
    public BigDecimal getR58NetAmt() {
        return r58NetAmt;
    }
    public void setR58NetAmt(BigDecimal r58NetAmt) {
        this.r58NetAmt = r58NetAmt;
    }
    public BigDecimal getR58NetAmtBwp() {
        return r58NetAmtBwp;
    }
    public void setR58NetAmtBwp(BigDecimal r58NetAmtBwp) {
        this.r58NetAmtBwp = r58NetAmtBwp;
    }
    public BigDecimal getR58BalSub() {
        return r58BalSub;
    }
    public void setR58BalSub(BigDecimal r58BalSub) {
        this.r58BalSub = r58BalSub;
    }
    public BigDecimal getR58BalSubBwp() {
        return r58BalSubBwp;
    }
    public void setR58BalSubBwp(BigDecimal r58BalSubBwp) {
        this.r58BalSubBwp = r58BalSubBwp;
    }
    public BigDecimal getR58BalSubDiariesBwp() {
        return r58BalSubDiariesBwp;
    }
    public void setR58BalSubDiariesBwp(BigDecimal r58BalSubDiariesBwp) {
        this.r58BalSubDiariesBwp = r58BalSubDiariesBwp;
    }
    public String getR59OperatingExpenses() {
        return r59OperatingExpenses;
    }
    public void setR59OperatingExpenses(String r59OperatingExpenses) {
        this.r59OperatingExpenses = r59OperatingExpenses;
    }
    public BigDecimal getR59FigBalSheet() {
        return r59FigBalSheet;
    }
    public void setR59FigBalSheet(BigDecimal r59FigBalSheet) {
        this.r59FigBalSheet = r59FigBalSheet;
    }
    public BigDecimal getR59FigBalSheetBwp() {
        return r59FigBalSheetBwp;
    }
    public void setR59FigBalSheetBwp(BigDecimal r59FigBalSheetBwp) {
        this.r59FigBalSheetBwp = r59FigBalSheetBwp;
    }
    public BigDecimal getR59AmtStatementAdj() {
        return r59AmtStatementAdj;
    }
    public void setR59AmtStatementAdj(BigDecimal r59AmtStatementAdj) {
        this.r59AmtStatementAdj = r59AmtStatementAdj;
    }
    public BigDecimal getR59AmtStatementAdjBwp() {
        return r59AmtStatementAdjBwp;
    }
    public void setR59AmtStatementAdjBwp(BigDecimal r59AmtStatementAdjBwp) {
        this.r59AmtStatementAdjBwp = r59AmtStatementAdjBwp;
    }
    public BigDecimal getR59NetAmt() {
        return r59NetAmt;
    }
    public void setR59NetAmt(BigDecimal r59NetAmt) {
        this.r59NetAmt = r59NetAmt;
    }
    public BigDecimal getR59NetAmtBwp() {
        return r59NetAmtBwp;
    }
    public void setR59NetAmtBwp(BigDecimal r59NetAmtBwp) {
        this.r59NetAmtBwp = r59NetAmtBwp;
    }
    public BigDecimal getR59BalSub() {
        return r59BalSub;
    }
    public void setR59BalSub(BigDecimal r59BalSub) {
        this.r59BalSub = r59BalSub;
    }
    public BigDecimal getR59BalSubBwp() {
        return r59BalSubBwp;
    }
    public void setR59BalSubBwp(BigDecimal r59BalSubBwp) {
        this.r59BalSubBwp = r59BalSubBwp;
    }
    public BigDecimal getR59BalSubDiariesBwp() {
        return r59BalSubDiariesBwp;
    }
    public void setR59BalSubDiariesBwp(BigDecimal r59BalSubDiariesBwp) {
        this.r59BalSubDiariesBwp = r59BalSubDiariesBwp;
    }
    public String getR60OperatingExpenses() {
        return r60OperatingExpenses;
    }
    public void setR60OperatingExpenses(String r60OperatingExpenses) {
        this.r60OperatingExpenses = r60OperatingExpenses;
    }
    public BigDecimal getR60FigBalSheet() {
        return r60FigBalSheet;
    }
    public void setR60FigBalSheet(BigDecimal r60FigBalSheet) {
        this.r60FigBalSheet = r60FigBalSheet;
    }
    public BigDecimal getR60FigBalSheetBwp() {
        return r60FigBalSheetBwp;
    }
    public void setR60FigBalSheetBwp(BigDecimal r60FigBalSheetBwp) {
        this.r60FigBalSheetBwp = r60FigBalSheetBwp;
    }
    public BigDecimal getR60AmtStatementAdj() {
        return r60AmtStatementAdj;
    }
    public void setR60AmtStatementAdj(BigDecimal r60AmtStatementAdj) {
        this.r60AmtStatementAdj = r60AmtStatementAdj;
    }
    public BigDecimal getR60AmtStatementAdjBwp() {
        return r60AmtStatementAdjBwp;
    }
    public void setR60AmtStatementAdjBwp(BigDecimal r60AmtStatementAdjBwp) {
        this.r60AmtStatementAdjBwp = r60AmtStatementAdjBwp;
    }
    public BigDecimal getR60NetAmt() {
        return r60NetAmt;
    }
    public void setR60NetAmt(BigDecimal r60NetAmt) {
        this.r60NetAmt = r60NetAmt;
    }
    public BigDecimal getR60NetAmtBwp() {
        return r60NetAmtBwp;
    }
    public void setR60NetAmtBwp(BigDecimal r60NetAmtBwp) {
        this.r60NetAmtBwp = r60NetAmtBwp;
    }
    public BigDecimal getR60BalSub() {
        return r60BalSub;
    }
    public void setR60BalSub(BigDecimal r60BalSub) {
        this.r60BalSub = r60BalSub;
    }
    public BigDecimal getR60BalSubBwp() {
        return r60BalSubBwp;
    }
    public void setR60BalSubBwp(BigDecimal r60BalSubBwp) {
        this.r60BalSubBwp = r60BalSubBwp;
    }
    public BigDecimal getR60BalSubDiariesBwp() {
        return r60BalSubDiariesBwp;
    }
    public void setR60BalSubDiariesBwp(BigDecimal r60BalSubDiariesBwp) {
        this.r60BalSubDiariesBwp = r60BalSubDiariesBwp;
    }
    public String getR61OperatingExpenses() {
        return r61OperatingExpenses;
    }
    public void setR61OperatingExpenses(String r61OperatingExpenses) {
        this.r61OperatingExpenses = r61OperatingExpenses;
    }
    public BigDecimal getR61FigBalSheet() {
        return r61FigBalSheet;
    }
    public void setR61FigBalSheet(BigDecimal r61FigBalSheet) {
        this.r61FigBalSheet = r61FigBalSheet;
    }
    public BigDecimal getR61FigBalSheetBwp() {
        return r61FigBalSheetBwp;
    }
    public void setR61FigBalSheetBwp(BigDecimal r61FigBalSheetBwp) {
        this.r61FigBalSheetBwp = r61FigBalSheetBwp;
    }
    public BigDecimal getR61AmtStatementAdj() {
        return r61AmtStatementAdj;
    }
    public void setR61AmtStatementAdj(BigDecimal r61AmtStatementAdj) {
        this.r61AmtStatementAdj = r61AmtStatementAdj;
    }
    public BigDecimal getR61AmtStatementAdjBwp() {
        return r61AmtStatementAdjBwp;
    }
    public void setR61AmtStatementAdjBwp(BigDecimal r61AmtStatementAdjBwp) {
        this.r61AmtStatementAdjBwp = r61AmtStatementAdjBwp;
    }
    public BigDecimal getR61NetAmt() {
        return r61NetAmt;
    }
    public void setR61NetAmt(BigDecimal r61NetAmt) {
        this.r61NetAmt = r61NetAmt;
    }
    public BigDecimal getR61NetAmtBwp() {
        return r61NetAmtBwp;
    }
    public void setR61NetAmtBwp(BigDecimal r61NetAmtBwp) {
        this.r61NetAmtBwp = r61NetAmtBwp;
    }
    public BigDecimal getR61BalSub() {
        return r61BalSub;
    }
    public void setR61BalSub(BigDecimal r61BalSub) {
        this.r61BalSub = r61BalSub;
    }
    public BigDecimal getR61BalSubBwp() {
        return r61BalSubBwp;
    }
    public void setR61BalSubBwp(BigDecimal r61BalSubBwp) {
        this.r61BalSubBwp = r61BalSubBwp;
    }
    public BigDecimal getR61BalSubDiariesBwp() {
        return r61BalSubDiariesBwp;
    }
    public void setR61BalSubDiariesBwp(BigDecimal r61BalSubDiariesBwp) {
        this.r61BalSubDiariesBwp = r61BalSubDiariesBwp;
    }
    public String getR62OperatingExpenses() {
        return r62OperatingExpenses;
    }
    public void setR62OperatingExpenses(String r62OperatingExpenses) {
        this.r62OperatingExpenses = r62OperatingExpenses;
    }
    public BigDecimal getR62FigBalSheet() {
        return r62FigBalSheet;
    }
    public void setR62FigBalSheet(BigDecimal r62FigBalSheet) {
        this.r62FigBalSheet = r62FigBalSheet;
    }
    public BigDecimal getR62FigBalSheetBwp() {
        return r62FigBalSheetBwp;
    }
    public void setR62FigBalSheetBwp(BigDecimal r62FigBalSheetBwp) {
        this.r62FigBalSheetBwp = r62FigBalSheetBwp;
    }
    public BigDecimal getR62AmtStatementAdj() {
        return r62AmtStatementAdj;
    }
    public void setR62AmtStatementAdj(BigDecimal r62AmtStatementAdj) {
        this.r62AmtStatementAdj = r62AmtStatementAdj;
    }
    public BigDecimal getR62AmtStatementAdjBwp() {
        return r62AmtStatementAdjBwp;
    }
    public void setR62AmtStatementAdjBwp(BigDecimal r62AmtStatementAdjBwp) {
        this.r62AmtStatementAdjBwp = r62AmtStatementAdjBwp;
    }
    public BigDecimal getR62NetAmt() {
        return r62NetAmt;
    }
    public void setR62NetAmt(BigDecimal r62NetAmt) {
        this.r62NetAmt = r62NetAmt;
    }
    public BigDecimal getR62NetAmtBwp() {
        return r62NetAmtBwp;
    }
    public void setR62NetAmtBwp(BigDecimal r62NetAmtBwp) {
        this.r62NetAmtBwp = r62NetAmtBwp;
    }
    public BigDecimal getR62BalSub() {
        return r62BalSub;
    }
    public void setR62BalSub(BigDecimal r62BalSub) {
        this.r62BalSub = r62BalSub;
    }
    public BigDecimal getR62BalSubBwp() {
        return r62BalSubBwp;
    }
    public void setR62BalSubBwp(BigDecimal r62BalSubBwp) {
        this.r62BalSubBwp = r62BalSubBwp;
    }
    public BigDecimal getR62BalSubDiariesBwp() {
        return r62BalSubDiariesBwp;
    }
    public void setR62BalSubDiariesBwp(BigDecimal r62BalSubDiariesBwp) {
        this.r62BalSubDiariesBwp = r62BalSubDiariesBwp;
    }
    public String getR63OperatingExpenses() {
        return r63OperatingExpenses;
    }
    public void setR63OperatingExpenses(String r63OperatingExpenses) {
        this.r63OperatingExpenses = r63OperatingExpenses;
    }
    public BigDecimal getR63FigBalSheet() {
        return r63FigBalSheet;
    }
    public void setR63FigBalSheet(BigDecimal r63FigBalSheet) {
        this.r63FigBalSheet = r63FigBalSheet;
    }
    public BigDecimal getR63FigBalSheetBwp() {
        return r63FigBalSheetBwp;
    }
    public void setR63FigBalSheetBwp(BigDecimal r63FigBalSheetBwp) {
        this.r63FigBalSheetBwp = r63FigBalSheetBwp;
    }
    public BigDecimal getR63AmtStatementAdj() {
        return r63AmtStatementAdj;
    }
    public void setR63AmtStatementAdj(BigDecimal r63AmtStatementAdj) {
        this.r63AmtStatementAdj = r63AmtStatementAdj;
    }
    public BigDecimal getR63AmtStatementAdjBwp() {
        return r63AmtStatementAdjBwp;
    }
    public void setR63AmtStatementAdjBwp(BigDecimal r63AmtStatementAdjBwp) {
        this.r63AmtStatementAdjBwp = r63AmtStatementAdjBwp;
    }
    public BigDecimal getR63NetAmt() {
        return r63NetAmt;
    }
    public void setR63NetAmt(BigDecimal r63NetAmt) {
        this.r63NetAmt = r63NetAmt;
    }
    public BigDecimal getR63NetAmtBwp() {
        return r63NetAmtBwp;
    }
    public void setR63NetAmtBwp(BigDecimal r63NetAmtBwp) {
        this.r63NetAmtBwp = r63NetAmtBwp;
    }
    public BigDecimal getR63BalSub() {
        return r63BalSub;
    }
    public void setR63BalSub(BigDecimal r63BalSub) {
        this.r63BalSub = r63BalSub;
    }
    public BigDecimal getR63BalSubBwp() {
        return r63BalSubBwp;
    }
    public void setR63BalSubBwp(BigDecimal r63BalSubBwp) {
        this.r63BalSubBwp = r63BalSubBwp;
    }
    public BigDecimal getR63BalSubDiariesBwp() {
        return r63BalSubDiariesBwp;
    }
    public void setR63BalSubDiariesBwp(BigDecimal r63BalSubDiariesBwp) {
        this.r63BalSubDiariesBwp = r63BalSubDiariesBwp;
    }
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
    public PL_SCHS_Summary_Entity() {
        super();
    }

 

    
}
