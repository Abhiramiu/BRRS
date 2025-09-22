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
@Table(name = "BRRS_M_UNCONS_INVEST_SUMMARYTABLE4")

public class M_UNCONS_INVEST_Summary_Entity4 {
	
	private String R35_PRODUCT;
	private BigDecimal R35_COMPANY;
	private BigDecimal R35_JURISDICTION_OF_INCORP_1;
	private BigDecimal R35_JURISDICTION_OF_INCORP_2;
	private BigDecimal R35_LINE_OF_BUSINESS;
	private BigDecimal R35_CURRENCY;
	private BigDecimal R35_SHARE_CAPITAL;
	private BigDecimal R35_ACCUMULATED_EQUITY_INTEREST;

	private String R36_PRODUCT;
	private BigDecimal R36_COMPANY;
	private BigDecimal R36_JURISDICTION_OF_INCORP_1;
	private BigDecimal R36_JURISDICTION_OF_INCORP_2;
	private BigDecimal R36_LINE_OF_BUSINESS;
	private BigDecimal R36_CURRENCY;
	private BigDecimal R36_SHARE_CAPITAL;
	private BigDecimal R36_ACCUMULATED_EQUITY_INTEREST;

	private String R37_PRODUCT;
	private BigDecimal R37_COMPANY;
	private BigDecimal R37_JURISDICTION_OF_INCORP_1;
	private BigDecimal R37_JURISDICTION_OF_INCORP_2;
	private BigDecimal R37_LINE_OF_BUSINESS;
	private BigDecimal R37_CURRENCY;
	private BigDecimal R37_SHARE_CAPITAL;
	private BigDecimal R37_ACCUMULATED_EQUITY_INTEREST;

	private String R38_PRODUCT;
	private BigDecimal R38_COMPANY;
	private BigDecimal R38_JURISDICTION_OF_INCORP_1;
	private BigDecimal R38_JURISDICTION_OF_INCORP_2;
	private BigDecimal R38_LINE_OF_BUSINESS;
	private BigDecimal R38_CURRENCY;
	private BigDecimal R38_SHARE_CAPITAL;
	private BigDecimal R38_ACCUMULATED_EQUITY_INTEREST;

    
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
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_COMPANY() {
		return R35_COMPANY;
	}
	public void setR35_COMPANY(BigDecimal r35_COMPANY) {
		R35_COMPANY = r35_COMPANY;
	}
	public BigDecimal getR35_JURISDICTION_OF_INCORP_1() {
		return R35_JURISDICTION_OF_INCORP_1;
	}
	public void setR35_JURISDICTION_OF_INCORP_1(BigDecimal r35_JURISDICTION_OF_INCORP_1) {
		R35_JURISDICTION_OF_INCORP_1 = r35_JURISDICTION_OF_INCORP_1;
	}
	public BigDecimal getR35_JURISDICTION_OF_INCORP_2() {
		return R35_JURISDICTION_OF_INCORP_2;
	}
	public void setR35_JURISDICTION_OF_INCORP_2(BigDecimal r35_JURISDICTION_OF_INCORP_2) {
		R35_JURISDICTION_OF_INCORP_2 = r35_JURISDICTION_OF_INCORP_2;
	}
	public BigDecimal getR35_LINE_OF_BUSINESS() {
		return R35_LINE_OF_BUSINESS;
	}
	public void setR35_LINE_OF_BUSINESS(BigDecimal r35_LINE_OF_BUSINESS) {
		R35_LINE_OF_BUSINESS = r35_LINE_OF_BUSINESS;
	}
	public BigDecimal getR35_CURRENCY() {
		return R35_CURRENCY;
	}
	public void setR35_CURRENCY(BigDecimal r35_CURRENCY) {
		R35_CURRENCY = r35_CURRENCY;
	}
	public BigDecimal getR35_SHARE_CAPITAL() {
		return R35_SHARE_CAPITAL;
	}
	public void setR35_SHARE_CAPITAL(BigDecimal r35_SHARE_CAPITAL) {
		R35_SHARE_CAPITAL = r35_SHARE_CAPITAL;
	}
	public BigDecimal getR35_ACCUMULATED_EQUITY_INTEREST() {
		return R35_ACCUMULATED_EQUITY_INTEREST;
	}
	public void setR35_ACCUMULATED_EQUITY_INTEREST(BigDecimal r35_ACCUMULATED_EQUITY_INTEREST) {
		R35_ACCUMULATED_EQUITY_INTEREST = r35_ACCUMULATED_EQUITY_INTEREST;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_COMPANY() {
		return R36_COMPANY;
	}
	public void setR36_COMPANY(BigDecimal r36_COMPANY) {
		R36_COMPANY = r36_COMPANY;
	}
	public BigDecimal getR36_JURISDICTION_OF_INCORP_1() {
		return R36_JURISDICTION_OF_INCORP_1;
	}
	public void setR36_JURISDICTION_OF_INCORP_1(BigDecimal r36_JURISDICTION_OF_INCORP_1) {
		R36_JURISDICTION_OF_INCORP_1 = r36_JURISDICTION_OF_INCORP_1;
	}
	public BigDecimal getR36_JURISDICTION_OF_INCORP_2() {
		return R36_JURISDICTION_OF_INCORP_2;
	}
	public void setR36_JURISDICTION_OF_INCORP_2(BigDecimal r36_JURISDICTION_OF_INCORP_2) {
		R36_JURISDICTION_OF_INCORP_2 = r36_JURISDICTION_OF_INCORP_2;
	}
	public BigDecimal getR36_LINE_OF_BUSINESS() {
		return R36_LINE_OF_BUSINESS;
	}
	public void setR36_LINE_OF_BUSINESS(BigDecimal r36_LINE_OF_BUSINESS) {
		R36_LINE_OF_BUSINESS = r36_LINE_OF_BUSINESS;
	}
	public BigDecimal getR36_CURRENCY() {
		return R36_CURRENCY;
	}
	public void setR36_CURRENCY(BigDecimal r36_CURRENCY) {
		R36_CURRENCY = r36_CURRENCY;
	}
	public BigDecimal getR36_SHARE_CAPITAL() {
		return R36_SHARE_CAPITAL;
	}
	public void setR36_SHARE_CAPITAL(BigDecimal r36_SHARE_CAPITAL) {
		R36_SHARE_CAPITAL = r36_SHARE_CAPITAL;
	}
	public BigDecimal getR36_ACCUMULATED_EQUITY_INTEREST() {
		return R36_ACCUMULATED_EQUITY_INTEREST;
	}
	public void setR36_ACCUMULATED_EQUITY_INTEREST(BigDecimal r36_ACCUMULATED_EQUITY_INTEREST) {
		R36_ACCUMULATED_EQUITY_INTEREST = r36_ACCUMULATED_EQUITY_INTEREST;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_COMPANY() {
		return R37_COMPANY;
	}
	public void setR37_COMPANY(BigDecimal r37_COMPANY) {
		R37_COMPANY = r37_COMPANY;
	}
	public BigDecimal getR37_JURISDICTION_OF_INCORP_1() {
		return R37_JURISDICTION_OF_INCORP_1;
	}
	public void setR37_JURISDICTION_OF_INCORP_1(BigDecimal r37_JURISDICTION_OF_INCORP_1) {
		R37_JURISDICTION_OF_INCORP_1 = r37_JURISDICTION_OF_INCORP_1;
	}
	public BigDecimal getR37_JURISDICTION_OF_INCORP_2() {
		return R37_JURISDICTION_OF_INCORP_2;
	}
	public void setR37_JURISDICTION_OF_INCORP_2(BigDecimal r37_JURISDICTION_OF_INCORP_2) {
		R37_JURISDICTION_OF_INCORP_2 = r37_JURISDICTION_OF_INCORP_2;
	}
	public BigDecimal getR37_LINE_OF_BUSINESS() {
		return R37_LINE_OF_BUSINESS;
	}
	public void setR37_LINE_OF_BUSINESS(BigDecimal r37_LINE_OF_BUSINESS) {
		R37_LINE_OF_BUSINESS = r37_LINE_OF_BUSINESS;
	}
	public BigDecimal getR37_CURRENCY() {
		return R37_CURRENCY;
	}
	public void setR37_CURRENCY(BigDecimal r37_CURRENCY) {
		R37_CURRENCY = r37_CURRENCY;
	}
	public BigDecimal getR37_SHARE_CAPITAL() {
		return R37_SHARE_CAPITAL;
	}
	public void setR37_SHARE_CAPITAL(BigDecimal r37_SHARE_CAPITAL) {
		R37_SHARE_CAPITAL = r37_SHARE_CAPITAL;
	}
	public BigDecimal getR37_ACCUMULATED_EQUITY_INTEREST() {
		return R37_ACCUMULATED_EQUITY_INTEREST;
	}
	public void setR37_ACCUMULATED_EQUITY_INTEREST(BigDecimal r37_ACCUMULATED_EQUITY_INTEREST) {
		R37_ACCUMULATED_EQUITY_INTEREST = r37_ACCUMULATED_EQUITY_INTEREST;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_COMPANY() {
		return R38_COMPANY;
	}
	public void setR38_COMPANY(BigDecimal r38_COMPANY) {
		R38_COMPANY = r38_COMPANY;
	}
	public BigDecimal getR38_JURISDICTION_OF_INCORP_1() {
		return R38_JURISDICTION_OF_INCORP_1;
	}
	public void setR38_JURISDICTION_OF_INCORP_1(BigDecimal r38_JURISDICTION_OF_INCORP_1) {
		R38_JURISDICTION_OF_INCORP_1 = r38_JURISDICTION_OF_INCORP_1;
	}
	public BigDecimal getR38_JURISDICTION_OF_INCORP_2() {
		return R38_JURISDICTION_OF_INCORP_2;
	}
	public void setR38_JURISDICTION_OF_INCORP_2(BigDecimal r38_JURISDICTION_OF_INCORP_2) {
		R38_JURISDICTION_OF_INCORP_2 = r38_JURISDICTION_OF_INCORP_2;
	}
	public BigDecimal getR38_LINE_OF_BUSINESS() {
		return R38_LINE_OF_BUSINESS;
	}
	public void setR38_LINE_OF_BUSINESS(BigDecimal r38_LINE_OF_BUSINESS) {
		R38_LINE_OF_BUSINESS = r38_LINE_OF_BUSINESS;
	}
	public BigDecimal getR38_CURRENCY() {
		return R38_CURRENCY;
	}
	public void setR38_CURRENCY(BigDecimal r38_CURRENCY) {
		R38_CURRENCY = r38_CURRENCY;
	}
	public BigDecimal getR38_SHARE_CAPITAL() {
		return R38_SHARE_CAPITAL;
	}
	public void setR38_SHARE_CAPITAL(BigDecimal r38_SHARE_CAPITAL) {
		R38_SHARE_CAPITAL = r38_SHARE_CAPITAL;
	}
	public BigDecimal getR38_ACCUMULATED_EQUITY_INTEREST() {
		return R38_ACCUMULATED_EQUITY_INTEREST;
	}
	public void setR38_ACCUMULATED_EQUITY_INTEREST(BigDecimal r38_ACCUMULATED_EQUITY_INTEREST) {
		R38_ACCUMULATED_EQUITY_INTEREST = r38_ACCUMULATED_EQUITY_INTEREST;
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
	public M_UNCONS_INVEST_Summary_Entity4() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	
	
	

}