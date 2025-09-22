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
@Table(name = "BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY2")

public class M_UNCONS_INVEST_Archival_Summary_Entity2 {

	private String R22_PRODUCT;
	private BigDecimal R22_ACCUULATED_EQUITY_INTEREST_5;
	private BigDecimal R22_ASSETS;
	private BigDecimal R22_LIABILITIES;
	private BigDecimal R22_REVENUE;
	private BigDecimal R22_PROFIT_OR_LOSS;
	private BigDecimal R22_UNREG_SHARE_OF_LOSS;
	private BigDecimal R22_CUMULATIVE_UNREG_SHARE_OF_LOSS;

	private String R23_PRODUCT;
	private BigDecimal R23_ACCUULATED_EQUITY_INTEREST_5;
	private BigDecimal R23_ASSETS;
	private BigDecimal R23_LIABILITIES;
	private BigDecimal R23_REVENUE;
	private BigDecimal R23_PROFIT_OR_LOSS;
	private BigDecimal R23_UNREG_SHARE_OF_LOSS;
	private BigDecimal R23_CUMULATIVE_UNREG_SHARE_OF_LOSS;

	private String R24_PRODUCT;
	private BigDecimal R24_ACCUULATED_EQUITY_INTEREST_5;
	private BigDecimal R24_ASSETS;
	private BigDecimal R24_LIABILITIES;
	private BigDecimal R24_REVENUE;
	private BigDecimal R24_PROFIT_OR_LOSS;
	private BigDecimal R24_UNREG_SHARE_OF_LOSS;
	private BigDecimal R24_CUMULATIVE_UNREG_SHARE_OF_LOSS;

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
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_ACCUULATED_EQUITY_INTEREST_5() {
		return R22_ACCUULATED_EQUITY_INTEREST_5;
	}
	public void setR22_ACCUULATED_EQUITY_INTEREST_5(BigDecimal r22_ACCUULATED_EQUITY_INTEREST_5) {
		R22_ACCUULATED_EQUITY_INTEREST_5 = r22_ACCUULATED_EQUITY_INTEREST_5;
	}
	public BigDecimal getR22_ASSETS() {
		return R22_ASSETS;
	}
	public void setR22_ASSETS(BigDecimal r22_ASSETS) {
		R22_ASSETS = r22_ASSETS;
	}
	public BigDecimal getR22_LIABILITIES() {
		return R22_LIABILITIES;
	}
	public void setR22_LIABILITIES(BigDecimal r22_LIABILITIES) {
		R22_LIABILITIES = r22_LIABILITIES;
	}
	public BigDecimal getR22_REVENUE() {
		return R22_REVENUE;
	}
	public void setR22_REVENUE(BigDecimal r22_REVENUE) {
		R22_REVENUE = r22_REVENUE;
	}
	public BigDecimal getR22_PROFIT_OR_LOSS() {
		return R22_PROFIT_OR_LOSS;
	}
	public void setR22_PROFIT_OR_LOSS(BigDecimal r22_PROFIT_OR_LOSS) {
		R22_PROFIT_OR_LOSS = r22_PROFIT_OR_LOSS;
	}
	public BigDecimal getR22_UNREG_SHARE_OF_LOSS() {
		return R22_UNREG_SHARE_OF_LOSS;
	}
	public void setR22_UNREG_SHARE_OF_LOSS(BigDecimal r22_UNREG_SHARE_OF_LOSS) {
		R22_UNREG_SHARE_OF_LOSS = r22_UNREG_SHARE_OF_LOSS;
	}
	public BigDecimal getR22_CUMULATIVE_UNREG_SHARE_OF_LOSS() {
		return R22_CUMULATIVE_UNREG_SHARE_OF_LOSS;
	}
	public void setR22_CUMULATIVE_UNREG_SHARE_OF_LOSS(BigDecimal r22_CUMULATIVE_UNREG_SHARE_OF_LOSS) {
		R22_CUMULATIVE_UNREG_SHARE_OF_LOSS = r22_CUMULATIVE_UNREG_SHARE_OF_LOSS;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_ACCUULATED_EQUITY_INTEREST_5() {
		return R23_ACCUULATED_EQUITY_INTEREST_5;
	}
	public void setR23_ACCUULATED_EQUITY_INTEREST_5(BigDecimal r23_ACCUULATED_EQUITY_INTEREST_5) {
		R23_ACCUULATED_EQUITY_INTEREST_5 = r23_ACCUULATED_EQUITY_INTEREST_5;
	}
	public BigDecimal getR23_ASSETS() {
		return R23_ASSETS;
	}
	public void setR23_ASSETS(BigDecimal r23_ASSETS) {
		R23_ASSETS = r23_ASSETS;
	}
	public BigDecimal getR23_LIABILITIES() {
		return R23_LIABILITIES;
	}
	public void setR23_LIABILITIES(BigDecimal r23_LIABILITIES) {
		R23_LIABILITIES = r23_LIABILITIES;
	}
	public BigDecimal getR23_REVENUE() {
		return R23_REVENUE;
	}
	public void setR23_REVENUE(BigDecimal r23_REVENUE) {
		R23_REVENUE = r23_REVENUE;
	}
	public BigDecimal getR23_PROFIT_OR_LOSS() {
		return R23_PROFIT_OR_LOSS;
	}
	public void setR23_PROFIT_OR_LOSS(BigDecimal r23_PROFIT_OR_LOSS) {
		R23_PROFIT_OR_LOSS = r23_PROFIT_OR_LOSS;
	}
	public BigDecimal getR23_UNREG_SHARE_OF_LOSS() {
		return R23_UNREG_SHARE_OF_LOSS;
	}
	public void setR23_UNREG_SHARE_OF_LOSS(BigDecimal r23_UNREG_SHARE_OF_LOSS) {
		R23_UNREG_SHARE_OF_LOSS = r23_UNREG_SHARE_OF_LOSS;
	}
	public BigDecimal getR23_CUMULATIVE_UNREG_SHARE_OF_LOSS() {
		return R23_CUMULATIVE_UNREG_SHARE_OF_LOSS;
	}
	public void setR23_CUMULATIVE_UNREG_SHARE_OF_LOSS(BigDecimal r23_CUMULATIVE_UNREG_SHARE_OF_LOSS) {
		R23_CUMULATIVE_UNREG_SHARE_OF_LOSS = r23_CUMULATIVE_UNREG_SHARE_OF_LOSS;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_ACCUULATED_EQUITY_INTEREST_5() {
		return R24_ACCUULATED_EQUITY_INTEREST_5;
	}
	public void setR24_ACCUULATED_EQUITY_INTEREST_5(BigDecimal r24_ACCUULATED_EQUITY_INTEREST_5) {
		R24_ACCUULATED_EQUITY_INTEREST_5 = r24_ACCUULATED_EQUITY_INTEREST_5;
	}
	public BigDecimal getR24_ASSETS() {
		return R24_ASSETS;
	}
	public void setR24_ASSETS(BigDecimal r24_ASSETS) {
		R24_ASSETS = r24_ASSETS;
	}
	public BigDecimal getR24_LIABILITIES() {
		return R24_LIABILITIES;
	}
	public void setR24_LIABILITIES(BigDecimal r24_LIABILITIES) {
		R24_LIABILITIES = r24_LIABILITIES;
	}
	public BigDecimal getR24_REVENUE() {
		return R24_REVENUE;
	}
	public void setR24_REVENUE(BigDecimal r24_REVENUE) {
		R24_REVENUE = r24_REVENUE;
	}
	public BigDecimal getR24_PROFIT_OR_LOSS() {
		return R24_PROFIT_OR_LOSS;
	}
	public void setR24_PROFIT_OR_LOSS(BigDecimal r24_PROFIT_OR_LOSS) {
		R24_PROFIT_OR_LOSS = r24_PROFIT_OR_LOSS;
	}
	public BigDecimal getR24_UNREG_SHARE_OF_LOSS() {
		return R24_UNREG_SHARE_OF_LOSS;
	}
	public void setR24_UNREG_SHARE_OF_LOSS(BigDecimal r24_UNREG_SHARE_OF_LOSS) {
		R24_UNREG_SHARE_OF_LOSS = r24_UNREG_SHARE_OF_LOSS;
	}
	public BigDecimal getR24_CUMULATIVE_UNREG_SHARE_OF_LOSS() {
		return R24_CUMULATIVE_UNREG_SHARE_OF_LOSS;
	}
	public void setR24_CUMULATIVE_UNREG_SHARE_OF_LOSS(BigDecimal r24_CUMULATIVE_UNREG_SHARE_OF_LOSS) {
		R24_CUMULATIVE_UNREG_SHARE_OF_LOSS = r24_CUMULATIVE_UNREG_SHARE_OF_LOSS;
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
	public M_UNCONS_INVEST_Archival_Summary_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
    
}