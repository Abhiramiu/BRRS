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
@Table(name = "BRRS_Q_STAFF_SUMMARYTABLE3")
public class Q_STAFF_Summary_Entity3 {
 private String  R33_STAFF_LOANS;
private BigDecimal R33_ORIGINAL_AMT;
private BigDecimal R33_BALANCE_OUTSTANDING;
private BigDecimal R33_NO_OF_ACS;
private BigDecimal R33_INTEREST_RATE;
private String  R34_STAFF_LOANS;
private BigDecimal R34_ORIGINAL_AMT;
private BigDecimal R34_BALANCE_OUTSTANDING;
private BigDecimal R34_NO_OF_ACS;
private BigDecimal R34_INTEREST_RATE;
 private String  R35_STAFF_LOANS;
private BigDecimal R35_ORIGINAL_AMT;
private BigDecimal R35_BALANCE_OUTSTANDING;
private BigDecimal R35_NO_OF_ACS;
private BigDecimal R35_INTEREST_RATE;
 private String  R36_STAFF_LOANS;
private BigDecimal R36_ORIGINAL_AMT;
private BigDecimal R36_BALANCE_OUTSTANDING;
private BigDecimal R36_NO_OF_ACS;
private BigDecimal R36_INTEREST_RATE;
 private String  R37_STAFF_LOANS;
private BigDecimal R37_ORIGINAL_AMT;
private BigDecimal R37_BALANCE_OUTSTANDING;
private BigDecimal R37_NO_OF_ACS;
private BigDecimal R37_INTEREST_RATE;
 private String  R38_STAFF_LOANS;
private BigDecimal R38_ORIGINAL_AMT;
private BigDecimal R38_BALANCE_OUTSTANDING;
private BigDecimal R38_NO_OF_ACS;
private BigDecimal R38_INTEREST_RATE;
  @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
   
   @Id
   private Date REPORT_DATE;

   private String REPORT_VERSION;
   private String REPORT_FREQUENCY;
   private String REPORT_CODE;
   private String REPORT_DESC;
   private String ENTITY_FLG;
   private String MODIFY_FLG;
   private String DEL_FLG;

   
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
   public Q_STAFF_Summary_Entity3() {
    super();
   }

   
   
}
