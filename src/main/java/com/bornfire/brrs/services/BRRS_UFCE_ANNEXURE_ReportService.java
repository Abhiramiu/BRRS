package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class BRRS_UFCE_ANNEXURE_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_UFCE_ANNEXURE_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

	// ===========================================================
	// INNER ENTITY CLASSES
	// ===========================================================

	// ------------------------------------
	// UFCE_ANNEXURE SUMMARY ENTITY CLASS
	// ------------------------------------
	@Entity
	@Table(name = "BRRS_ANNEXURE_UFCE_SUMMARYTABLE")
	public static class UFCE_ANNEXURE_Summary_Entity {

		@Id
		private Long sno;
		private String nameOfBorrower;
		private String branch;
		private BigDecimal finacleAcId;
		private String finacleId;
		private BigDecimal totFrgnCcyExposure;
		private BigDecimal ufce;
		private BigDecimal likelyLoss;
		private BigDecimal ebid;
		private BigDecimal likelyLossEbid;
		private BigDecimal incrementalProvReq;
		private BigDecimal fundBasedOnDate;
		private BigDecimal provRequired;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private BigDecimal ufcePercent;

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getNameOfBorrower() {
			return nameOfBorrower;
		}

		public void setNameOfBorrower(String nameOfBorrower) {
			this.nameOfBorrower = nameOfBorrower;
		}

		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

		public BigDecimal getFinacleAcId() {
			return finacleAcId;
		}

		public void setFinacleAcId(BigDecimal finacleAcId) {
			this.finacleAcId = finacleAcId;
		}

		public String getFinacleId() {
			return finacleId;
		}

		public void setFinacleId(String finacleId) {
			this.finacleId = finacleId;
		}

		public BigDecimal getTotFrgnCcyExposure() {
			return totFrgnCcyExposure;
		}

		public void setTotFrgnCcyExposure(BigDecimal totFrgnCcyExposure) {
			this.totFrgnCcyExposure = totFrgnCcyExposure;
		}

		public BigDecimal getUfce() {
			return ufce;
		}

		public void setUfce(BigDecimal ufce) {
			this.ufce = ufce;
		}

		public BigDecimal getLikelyLoss() {
			return likelyLoss;
		}

		public void setLikelyLoss(BigDecimal likelyLoss) {
			this.likelyLoss = likelyLoss;
		}

		public BigDecimal getEbid() {
			return ebid;
		}

		public void setEbid(BigDecimal ebid) {
			this.ebid = ebid;
		}

		public BigDecimal getLikelyLossEbid() {
			return likelyLossEbid;
		}

		public void setLikelyLossEbid(BigDecimal likelyLossEbid) {
			this.likelyLossEbid = likelyLossEbid;
		}

		public BigDecimal getIncrementalProvReq() {
			return incrementalProvReq;
		}

		public void setIncrementalProvReq(BigDecimal incrementalProvReq) {
			this.incrementalProvReq = incrementalProvReq;
		}

		public BigDecimal getFundBasedOnDate() {
			return fundBasedOnDate;
		}

		public void setFundBasedOnDate(BigDecimal fundBasedOnDate) {
			this.fundBasedOnDate = fundBasedOnDate;
		}

		public BigDecimal getProvRequired() {
			return provRequired;
		}

		public void setProvRequired(BigDecimal provRequired) {
			this.provRequired = provRequired;
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

		public BigDecimal getUfcePercent() {
			return ufcePercent;
		}

		public void setUfcePercent(BigDecimal ufcePercent) {
			this.ufcePercent = ufcePercent;
		}

		public UFCE_ANNEXURE_Summary_Entity() {
			super();
		}
	}

	// ------------------------------------
	// UFCE_ANNEXURE DETAIL ENTITY CLASS
	// ------------------------------------
	@Entity
	@Table(name = "BRRS_ANNEXURE_UFCE_DETAILTABLE")
	public static class UFCE_ANNEXURE_Detail_Entity {

		@Id
		private Long sno;
		private String nameOfBorrower;
		private String branch;
		private BigDecimal finacleAcId;
		private String finacleId;
		private BigDecimal totFrgnCcyExposure;
		private BigDecimal ufce;
		private BigDecimal likelyLoss;
		private BigDecimal ebid;
		private BigDecimal likelyLossEbid;
		private BigDecimal incrementalProvReq;
		private BigDecimal fundBasedOnDate;
		private BigDecimal provRequired;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private BigDecimal ufcePercent;

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getNameOfBorrower() {
			return nameOfBorrower;
		}

		public void setNameOfBorrower(String nameOfBorrower) {
			this.nameOfBorrower = nameOfBorrower;
		}

		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

		public BigDecimal getFinacleAcId() {
			return finacleAcId;
		}

		public void setFinacleAcId(BigDecimal finacleAcId) {
			this.finacleAcId = finacleAcId;
		}

		public String getFinacleId() {
			return finacleId;
		}

		public void setFinacleId(String finacleId) {
			this.finacleId = finacleId;
		}

		public BigDecimal getTotFrgnCcyExposure() {
			return totFrgnCcyExposure;
		}

		public void setTotFrgnCcyExposure(BigDecimal totFrgnCcyExposure) {
			this.totFrgnCcyExposure = totFrgnCcyExposure;
		}

		public BigDecimal getUfce() {
			return ufce;
		}

		public void setUfce(BigDecimal ufce) {
			this.ufce = ufce;
		}

		public BigDecimal getLikelyLoss() {
			return likelyLoss;
		}

		public void setLikelyLoss(BigDecimal likelyLoss) {
			this.likelyLoss = likelyLoss;
		}

		public BigDecimal getEbid() {
			return ebid;
		}

		public void setEbid(BigDecimal ebid) {
			this.ebid = ebid;
		}

		public BigDecimal getLikelyLossEbid() {
			return likelyLossEbid;
		}

		public void setLikelyLossEbid(BigDecimal likelyLossEbid) {
			this.likelyLossEbid = likelyLossEbid;
		}

		public BigDecimal getIncrementalProvReq() {
			return incrementalProvReq;
		}

		public void setIncrementalProvReq(BigDecimal incrementalProvReq) {
			this.incrementalProvReq = incrementalProvReq;
		}

		public BigDecimal getFundBasedOnDate() {
			return fundBasedOnDate;
		}

		public void setFundBasedOnDate(BigDecimal fundBasedOnDate) {
			this.fundBasedOnDate = fundBasedOnDate;
		}

		public BigDecimal getProvRequired() {
			return provRequired;
		}

		public void setProvRequired(BigDecimal provRequired) {
			this.provRequired = provRequired;
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
		
		public BigDecimal getUfcePercent() {
			return ufcePercent;
		}

		public void setUfcePercent(BigDecimal ufcePercent) {
			this.ufcePercent = ufcePercent;
		}

		public UFCE_ANNEXURE_Detail_Entity() {
			super();
		}
	}

	// --------------------------------------------
	// UFCE_ANNEXURE ARCHIVAL SUMMARY ENTITY CLASS
	// --------------------------------------------
	@Entity
	@Table(name = "BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE")
	public static class UFCE_ANNEXURE_Archival_Summary_Entity {

		@Id
		private Long sno;
		private String nameOfBorrower;
		private String branch;
		private BigDecimal finacleAcId;
		private String finacleId;
		private BigDecimal totFrgnCcyExposure;
		private BigDecimal ufce;
		private BigDecimal likelyLoss;
		private BigDecimal ebid;
		private BigDecimal likelyLossEbid;
		private BigDecimal incrementalProvReq;
		private BigDecimal fundBasedOnDate;
		private BigDecimal provRequired;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportResubdate;

		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private BigDecimal ufcePercent;


		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getNameOfBorrower() {
			return nameOfBorrower;
		}

		public void setNameOfBorrower(String nameOfBorrower) {
			this.nameOfBorrower = nameOfBorrower;
		}

		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

		public BigDecimal getFinacleAcId() {
			return finacleAcId;
		}

		public void setFinacleAcId(BigDecimal finacleAcId) {
			this.finacleAcId = finacleAcId;
		}

		public String getFinacleId() {
			return finacleId;
		}

		public void setFinacleId(String finacleId) {
			this.finacleId = finacleId;
		}

		public BigDecimal getTotFrgnCcyExposure() {
			return totFrgnCcyExposure;
		}

		public void setTotFrgnCcyExposure(BigDecimal totFrgnCcyExposure) {
			this.totFrgnCcyExposure = totFrgnCcyExposure;
		}

		public BigDecimal getUfce() {
			return ufce;
		}

		public void setUfce(BigDecimal ufce) {
			this.ufce = ufce;
		}

		public BigDecimal getLikelyLoss() {
			return likelyLoss;
		}

		public void setLikelyLoss(BigDecimal likelyLoss) {
			this.likelyLoss = likelyLoss;
		}

		public BigDecimal getEbid() {
			return ebid;
		}

		public void setEbid(BigDecimal ebid) {
			this.ebid = ebid;
		}

		public BigDecimal getLikelyLossEbid() {
			return likelyLossEbid;
		}

		public void setLikelyLossEbid(BigDecimal likelyLossEbid) {
			this.likelyLossEbid = likelyLossEbid;
		}

		public BigDecimal getIncrementalProvReq() {
			return incrementalProvReq;
		}

		public void setIncrementalProvReq(BigDecimal incrementalProvReq) {
			this.incrementalProvReq = incrementalProvReq;
		}

		public BigDecimal getFundBasedOnDate() {
			return fundBasedOnDate;
		}

		public void setFundBasedOnDate(BigDecimal fundBasedOnDate) {
			this.fundBasedOnDate = fundBasedOnDate;
		}

		public BigDecimal getProvRequired() {
			return provRequired;
		}

		public void setProvRequired(BigDecimal provRequired) {
			this.provRequired = provRequired;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public Date getReportResubdate() {
			return reportResubdate;
		}

		public void setReportResubdate(Date reportResubdate) {
			this.reportResubdate = reportResubdate;
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
		
		public BigDecimal getUfcePercent() {
			return ufcePercent;
		}

		public void setUfcePercent(BigDecimal ufcePercent) {
			this.ufcePercent = ufcePercent;
		}

		public UFCE_ANNEXURE_Archival_Summary_Entity() {
			super();
		}
	}

	// --------------------------------------------
	// UFCE_ANNEXURE ARCHIVAL DETAIL ENTITY CLASS
	// --------------------------------------------
	@Entity
	@Table(name = "BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE")
	public static class UFCE_ANNEXURE_Archival_Detail_Entity {

		@Id
		private Long sno;
		private String nameOfBorrower;
		private String branch;
		private BigDecimal finacleAcId;
		private String finacleId;
		private BigDecimal totFrgnCcyExposure;
		private BigDecimal ufce;
		private BigDecimal likelyLoss;
		private BigDecimal ebid;
		private BigDecimal likelyLossEbid;
		private BigDecimal incrementalProvReq;
		private BigDecimal fundBasedOnDate;
		private BigDecimal provRequired;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportResubdate;

		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private BigDecimal ufcePercent;

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getNameOfBorrower() {
			return nameOfBorrower;
		}

		public void setNameOfBorrower(String nameOfBorrower) {
			this.nameOfBorrower = nameOfBorrower;
		}

		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

		public BigDecimal getFinacleAcId() {
			return finacleAcId;
		}

		public void setFinacleAcId(BigDecimal finacleAcId) {
			this.finacleAcId = finacleAcId;
		}

		public String getFinacleId() {
			return finacleId;
		}

		public void setFinacleId(String finacleId) {
			this.finacleId = finacleId;
		}

		public BigDecimal getTotFrgnCcyExposure() {
			return totFrgnCcyExposure;
		}

		public void setTotFrgnCcyExposure(BigDecimal totFrgnCcyExposure) {
			this.totFrgnCcyExposure = totFrgnCcyExposure;
		}

		public BigDecimal getUfce() {
			return ufce;
		}

		public void setUfce(BigDecimal ufce) {
			this.ufce = ufce;
		}

		public BigDecimal getLikelyLoss() {
			return likelyLoss;
		}

		public void setLikelyLoss(BigDecimal likelyLoss) {
			this.likelyLoss = likelyLoss;
		}

		public BigDecimal getEbid() {
			return ebid;
		}

		public void setEbid(BigDecimal ebid) {
			this.ebid = ebid;
		}

		public BigDecimal getLikelyLossEbid() {
			return likelyLossEbid;
		}

		public void setLikelyLossEbid(BigDecimal likelyLossEbid) {
			this.likelyLossEbid = likelyLossEbid;
		}

		public BigDecimal getIncrementalProvReq() {
			return incrementalProvReq;
		}

		public void setIncrementalProvReq(BigDecimal incrementalProvReq) {
			this.incrementalProvReq = incrementalProvReq;
		}

		public BigDecimal getFundBasedOnDate() {
			return fundBasedOnDate;
		}

		public void setFundBasedOnDate(BigDecimal fundBasedOnDate) {
			this.fundBasedOnDate = fundBasedOnDate;
		}

		public BigDecimal getProvRequired() {
			return provRequired;
		}

		public void setProvRequired(BigDecimal provRequired) {
			this.provRequired = provRequired;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public Date getReportResubdate() {
			return reportResubdate;
		}

		public void setReportResubdate(Date reportResubdate) {
			this.reportResubdate = reportResubdate;
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
		
		public BigDecimal getUfcePercent() {
			return ufcePercent;
		}

		public void setUfcePercent(BigDecimal ufcePercent) {
			this.ufcePercent = ufcePercent;
		}

		public UFCE_ANNEXURE_Archival_Detail_Entity() {
			super();
		}
	}

	// ===========================================================
	// ROW MAPPERS
	// ===========================================================

	private class SummaryRowMapper implements RowMapper<UFCE_ANNEXURE_Summary_Entity> {
		@Override
		public UFCE_ANNEXURE_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			UFCE_ANNEXURE_Summary_Entity entity = new UFCE_ANNEXURE_Summary_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setNameOfBorrower(rs.getString("NAME_OF_BORROWER"));
			entity.setBranch(rs.getString("BRANCH"));
			entity.setFinacleAcId(rs.getBigDecimal("FINACLE_AC_ID"));
			entity.setFinacleId(rs.getString("FINACLE_ID"));
			entity.setTotFrgnCcyExposure(rs.getBigDecimal("TOT_FRGN_CCY_EXPOSURE"));
			entity.setUfce(rs.getBigDecimal("UFCE"));
			entity.setUfcePercent(rs.getBigDecimal("UFCE_PERCENT"));
			entity.setLikelyLoss(rs.getBigDecimal("LIKELY_LOSS"));
			entity.setEbid(rs.getBigDecimal("EBID"));
			entity.setLikelyLossEbid(rs.getBigDecimal("LIKELY_LOSS_EBID"));
			entity.setIncrementalProvReq(rs.getBigDecimal("INCREMENTAL_PROV_REQ"));
			entity.setFundBasedOnDate(rs.getBigDecimal("FUND_BASED_ON_DATE"));
			entity.setProvRequired(rs.getBigDecimal("PROV_REQUIRED"));
			entity.setReportDate(rs.getDate("REPORT_DATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			entity.setReportCode(rs.getString("REPORT_CODE"));
			entity.setReportDesc(rs.getString("REPORT_DESC"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			return entity;
		}
	}

	private class DetailRowMapper implements RowMapper<UFCE_ANNEXURE_Detail_Entity> {
		@Override
		public UFCE_ANNEXURE_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			UFCE_ANNEXURE_Detail_Entity entity = new UFCE_ANNEXURE_Detail_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setNameOfBorrower(rs.getString("NAME_OF_BORROWER"));
			entity.setBranch(rs.getString("BRANCH"));
			entity.setFinacleAcId(rs.getBigDecimal("FINACLE_AC_ID"));
			entity.setFinacleId(rs.getString("FINACLE_ID"));
			entity.setTotFrgnCcyExposure(rs.getBigDecimal("TOT_FRGN_CCY_EXPOSURE"));
			entity.setUfce(rs.getBigDecimal("UFCE"));
			entity.setUfcePercent(rs.getBigDecimal("UFCE_PERCENT"));
			entity.setLikelyLoss(rs.getBigDecimal("LIKELY_LOSS"));
			entity.setEbid(rs.getBigDecimal("EBID"));
			entity.setLikelyLossEbid(rs.getBigDecimal("LIKELY_LOSS_EBID"));
			entity.setIncrementalProvReq(rs.getBigDecimal("INCREMENTAL_PROV_REQ"));
			entity.setFundBasedOnDate(rs.getBigDecimal("FUND_BASED_ON_DATE"));
			entity.setProvRequired(rs.getBigDecimal("PROV_REQUIRED"));
			entity.setReportDate(rs.getDate("REPORT_DATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			entity.setReportCode(rs.getString("REPORT_CODE"));
			entity.setReportDesc(rs.getString("REPORT_DESC"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			return entity;
		}
	}

	private class ArchivalSummaryRowMapper implements RowMapper<UFCE_ANNEXURE_Archival_Summary_Entity> {
		@Override
		public UFCE_ANNEXURE_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			UFCE_ANNEXURE_Archival_Summary_Entity entity = new UFCE_ANNEXURE_Archival_Summary_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setNameOfBorrower(rs.getString("NAME_OF_BORROWER"));
			entity.setBranch(rs.getString("BRANCH"));
			entity.setFinacleAcId(rs.getBigDecimal("FINACLE_AC_ID"));
			entity.setFinacleId(rs.getString("FINACLE_ID"));
			entity.setTotFrgnCcyExposure(rs.getBigDecimal("TOT_FRGN_CCY_EXPOSURE"));
			entity.setUfce(rs.getBigDecimal("UFCE"));
			entity.setUfcePercent(rs.getBigDecimal("UFCE_PERCENT"));
			entity.setLikelyLoss(rs.getBigDecimal("LIKELY_LOSS"));
			entity.setEbid(rs.getBigDecimal("EBID"));
			entity.setLikelyLossEbid(rs.getBigDecimal("LIKELY_LOSS_EBID"));
			entity.setIncrementalProvReq(rs.getBigDecimal("INCREMENTAL_PROV_REQ"));
			entity.setFundBasedOnDate(rs.getBigDecimal("FUND_BASED_ON_DATE"));
			entity.setProvRequired(rs.getBigDecimal("PROV_REQUIRED"));
			entity.setReportDate(rs.getDate("REPORT_DATE"));
			entity.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			entity.setReportCode(rs.getString("REPORT_CODE"));
			entity.setReportDesc(rs.getString("REPORT_DESC"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			return entity;
		}
	}

	private class ArchivalDetailRowMapper implements RowMapper<UFCE_ANNEXURE_Archival_Detail_Entity> {
		@Override
		public UFCE_ANNEXURE_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			UFCE_ANNEXURE_Archival_Detail_Entity entity = new UFCE_ANNEXURE_Archival_Detail_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setNameOfBorrower(rs.getString("NAME_OF_BORROWER"));
			entity.setBranch(rs.getString("BRANCH"));
			entity.setFinacleAcId(rs.getBigDecimal("FINACLE_AC_ID"));
			entity.setFinacleId(rs.getString("FINACLE_ID"));
			entity.setTotFrgnCcyExposure(rs.getBigDecimal("TOT_FRGN_CCY_EXPOSURE"));
			entity.setUfce(rs.getBigDecimal("UFCE"));
			entity.setUfcePercent(rs.getBigDecimal("UFCE_PERCENT"));
			entity.setLikelyLoss(rs.getBigDecimal("LIKELY_LOSS"));
			entity.setEbid(rs.getBigDecimal("EBID"));
			entity.setLikelyLossEbid(rs.getBigDecimal("LIKELY_LOSS_EBID"));
			entity.setIncrementalProvReq(rs.getBigDecimal("INCREMENTAL_PROV_REQ"));
			entity.setFundBasedOnDate(rs.getBigDecimal("FUND_BASED_ON_DATE"));
			entity.setProvRequired(rs.getBigDecimal("PROV_REQUIRED"));
			entity.setReportDate(rs.getDate("REPORT_DATE"));
			entity.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			entity.setReportCode(rs.getString("REPORT_CODE"));
			entity.setReportDesc(rs.getString("REPORT_DESC"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			return entity;
		}
	}

	// ===========================================================
	// INLINE UPDATE ROW DTO
	// ===========================================================

	public static class UFCE_ANNEXURE_Update_Row {
		private Long sno;
		private String branch;
		private BigDecimal totFrgnCcyExposure;
		private BigDecimal ufce;
		private BigDecimal ufcePercent;

		public BigDecimal getUfcePercent() {
			return ufcePercent;
		}

		public void setUfcePercent(BigDecimal ufcePercent) {
			this.ufcePercent = ufcePercent;
		}
		private BigDecimal ebid;
		private BigDecimal incrementalProvReq;
		private BigDecimal provRequired;

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

		public BigDecimal getTotFrgnCcyExposure() {
			return totFrgnCcyExposure;
		}

		public void setTotFrgnCcyExposure(BigDecimal totFrgnCcyExposure) {
			this.totFrgnCcyExposure = totFrgnCcyExposure;
		}

		public BigDecimal getUfce() {
			return ufce;
		}

		public void setUfce(BigDecimal ufce) {
			this.ufce = ufce;
		}

		public BigDecimal getEbid() {
			return ebid;
		}

		public void setEbid(BigDecimal ebid) {
			this.ebid = ebid;
		}

		public BigDecimal getIncrementalProvReq() {
			return incrementalProvReq;
		}

		public void setIncrementalProvReq(BigDecimal incrementalProvReq) {
			this.incrementalProvReq = incrementalProvReq;
		}

		public BigDecimal getProvRequired() {
			return provRequired;
		}

		public void setProvRequired(BigDecimal provRequired) {
			this.provRequired = provRequired;
		}
	}

	// ------------------------------
	// PARSE DATE UTILITY METHOD
	// ------------------------------
	private Date parseDate(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return null;
		}
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(dateStr.trim());
		} catch (Exception e) {
			try {
				return new SimpleDateFormat("dd-MMM-yyyy", java.util.Locale.ENGLISH).parse(dateStr.trim());
			} catch (Exception ex) {
				logger.error("Failed to parse date: {}", dateStr);
				return null;
			}
		}
	}

	private String formatDateForQuery(Date dt) {
		if (dt == null) {
			return "";
		}
		return new SimpleDateFormat("dd-MMM-yyyy", java.util.Locale.ENGLISH).format(dt);
	}

	// ===========================================================
	// SERVICE VIEW METHODS
	// ===========================================================

	public ModelAndView getBRRS_UFCE_ANNEXURE_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("UFCE_ANNEXURE View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// =====================================================
		// ARCHIVAL MODE
		// =====================================================
		if ("ARCHIVAL".equals(type) && version != null) {
			try {
				Date dt = parseDate(todate);

				List<UFCE_ANNEXURE_Archival_Summary_Entity> archivalSummary = getArchivalDataByDateAndVersion(dt,
						version);

				System.out.println("Archival Summary size = " + archivalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<UFCE_ANNEXURE_Archival_Detail_Entity> archivalDetail = getArchivalDetailDataByDateAndVersion(
							dt, version);
					mv.addObject("reportdetails", archivalDetail);
					mv.addObject("displaymode", "archivalDetail");
					System.out.println("Archival Detail size = " + archivalDetail.size());
				} else {
					mv.addObject("displaymode", "archivalSummary");
				}

				mv.addObject("reportsummary", archivalSummary);
				mv.addObject("report_date", dt != null ? dateformat.format(dt) : todate);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// =====================================================
		// RESUB MODE (Uses Archival Tables)
		// =====================================================
		else if ("RESUB".equals(type) && version != null) {
			try {
				Date dt = parseDate(todate);

				List<UFCE_ANNEXURE_Archival_Summary_Entity> resubSummary = getResubDataByDateAndVersion(dt, version);

				System.out.println("Resub Summary size = " + resubSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<UFCE_ANNEXURE_Archival_Detail_Entity> resubDetail = getResubDetailDataByDateAndVersion(dt,
							version);
					mv.addObject("reportdetails", resubDetail);
					mv.addObject("displaymode", "resubDetail");
					System.out.println("Resub Detail size = " + resubDetail.size());
				} else {
					mv.addObject("displaymode", "resubSummary");
				}

				mv.addObject("reportsummary", resubSummary);
				mv.addObject("report_date", dt != null ? dateformat.format(dt) : todate);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// =====================================================
		// NORMAL MODE
		// =====================================================
		else {
			try {
				Date dt = parseDate(todate);
				String formattedDate = formatDateForQuery(dt);

				List<UFCE_ANNEXURE_Summary_Entity> normalSummary = jdbcTemplate.query(
						"SELECT * FROM BRRS_ANNEXURE_UFCE_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
						new Object[] { formattedDate }, new SummaryRowMapper());

				System.out.println("Normal Summary size = " + normalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<UFCE_ANNEXURE_Detail_Entity> normalDetail = jdbcTemplate.query(
							"SELECT * FROM BRRS_ANNEXURE_UFCE_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
							new Object[] { formattedDate }, new DetailRowMapper());
					mv.addObject("reportdetails", normalDetail);
					mv.addObject("displaymode", "Details");
					System.out.println("Normal Detail size = " + normalDetail.size());
				} else {
					mv.addObject("displaymode", "summary");
				}

				mv.addObject("reportsummary", normalSummary);
				mv.addObject("report_date", dt != null ? dateformat.format(dt) : todate);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Date dtForMax = parseDate(todate);
		if (dtForMax != null) {
			try {
				BigDecimal maxVer = findMaxResubVersion(dtForMax);
				mv.addObject("maxVersion", maxVer);
			} catch (Exception ex) {
				logger.warn("Could not calculate maxVersion for date {}: {}", dtForMax, ex.getMessage());
			}
		}

		mv.setViewName("BRRS/UFCE_ANNEXURE");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);
		mv.addObject("version", version);
		mv.addObject("type", type);

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	public ModelAndView getBRRS_UFCE_ANNEXURE_DetailView(String reportId, String fromdate, String todate,
			String currency, String dtltype, Pageable pageable, String type, BigDecimal ver) {
		return getBRRS_UFCE_ANNEXURE_View(reportId, fromdate, todate, currency, "detail", pageable, type, ver);
	}

	public ModelAndView getBRRS_UFCE_ANNEXURE_DetailView(String reportId, String fromdate, String todate,
			String currency, String dtltype, Pageable pageable, String filter, String type, BigDecimal ver) {
		return getBRRS_UFCE_ANNEXURE_View(reportId, fromdate, todate, currency, "detail", pageable, type, ver);
	}

	public ModelAndView getBRRS_UFCE_ANNEXURE_DetailView(String reportId, String fromdate, String todate,
			String currency, String dtltype, Pageable pageable, String filter, String type, String version) {
		BigDecimal ver = null;
		if (version != null && !version.trim().isEmpty()) {
			try {
				ver = new BigDecimal(version);
			} catch (Exception ignored) {
			}
		}
		return getBRRS_UFCE_ANNEXURE_View(reportId, fromdate, todate, currency, "detail", pageable, type, ver);
	}

	public ModelAndView getBRRS_UFCE_ANNEXURE_DetailView(String reportId, String fromdate, String todate,
			String currency, String dtltype, Pageable pageable, String type, String version) {
		BigDecimal ver = null;
		if (version != null && !version.trim().isEmpty()) {
			try {
				ver = new BigDecimal(version);
			} catch (Exception ignored) {
			}
		}
		return getBRRS_UFCE_ANNEXURE_View(reportId, fromdate, todate, currency, "detail", pageable, type, ver);
	}

	// ===========================================================
	// ARCHIVAL & RESUB DATA ACCESS METHODS
	// ===========================================================

	public List<UFCE_ANNEXURE_Archival_Summary_Entity> getArchivalDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalSummaryRowMapper());
	}

	public List<UFCE_ANNEXURE_Archival_Detail_Entity> getArchivalDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalDetailRowMapper());
	}

	public List<UFCE_ANNEXURE_Archival_Summary_Entity> getResubDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalSummaryRowMapper());
	}

	public List<UFCE_ANNEXURE_Archival_Detail_Entity> getResubDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalDetailRowMapper());
	}

	public List<Object[]> getUFCE_ANNEXUREArchival() {
		String sql = "SELECT DISTINCT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
				+ "FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_DATE DESC, TO_NUMBER(REGEXP_REPLACE(TO_CHAR(REPORT_VERSION), '[^0-9.]', '')) DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<Object[]> getUFCE_ANNEXUREResubList() {
		String sql = "SELECT DISTINCT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
				+ "FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_DATE DESC, TO_NUMBER(REGEXP_REPLACE(TO_CHAR(REPORT_VERSION), '[^0-9.]', '')) DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<UFCE_ANNEXURE_Archival_Summary_Entity> getAllArchivalWithVersion() {
		String sql = "SELECT * FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, TO_NUMBER(REGEXP_REPLACE(TO_CHAR(REPORT_VERSION), '[^0-9.]', '')) DESC";
		return jdbcTemplate.query(sql, new ArchivalSummaryRowMapper());
	}

	public List<UFCE_ANNEXURE_Archival_Summary_Entity> getAllResubWithVersion() {
		String sql = "SELECT * FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, TO_NUMBER(REGEXP_REPLACE(TO_CHAR(REPORT_VERSION), '[^0-9.]', '')) DESC";
		return jdbcTemplate.query(sql, new ArchivalSummaryRowMapper());
	}

	public BigDecimal findMaxArchivalVersion(Date reportDate) {
		return getNextArchivalVersion(reportDate).subtract(BigDecimal.ONE);
	}

	public BigDecimal findMaxResubVersion(Date reportDate) {
		return getNextArchivalVersion(reportDate).subtract(BigDecimal.ONE);
	}

	private BigDecimal getNextArchivalVersion(Date reportDate) {
		try {
			BigDecimal maxVersion = null;
			if (reportDate != null) {
				String sql = "SELECT MAX(TO_NUMBER(REGEXP_REPLACE(TO_CHAR(REPORT_VERSION), '[^0-9.]', ''))) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				maxVersion = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			}
			if (maxVersion == null) {
				String fallbackSql = "SELECT MAX(TO_NUMBER(REGEXP_REPLACE(TO_CHAR(REPORT_VERSION), '[^0-9.]', ''))) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE";
				maxVersion = jdbcTemplate.queryForObject(fallbackSql, BigDecimal.class);
			}
			if (maxVersion == null) {
				return BigDecimal.ONE;
			}
			return maxVersion.add(BigDecimal.ONE);
		} catch (Exception e) {
			logger.error("Error querying next archival version: {}", e.getMessage(), e);
			try {
				String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				BigDecimal maxV = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
				return (maxV != null) ? maxV.add(BigDecimal.ONE) : BigDecimal.ONE;
			} catch (Exception ex) {
				return BigDecimal.ONE;
			}
		}
	}

	// ===========================================================
	// INLINE UPDATE & PROCEDURE EXECUTION FLOW
	// ===========================================================



	public void updateAllUfceAnnexureRows(List<UFCE_ANNEXURE_Update_Row> rows, String type) {
		if (rows == null || rows.isEmpty())
			return;

		// =====================================================
		// RESUBMISSION MODE: Update ONLY Archival Tables as a NEW version
		// =====================================================
		if ("RESUB".equalsIgnoreCase(type)) {
			Date reportDate = null;
			try {
				reportDate = jdbcTemplate.queryForObject("SELECT REPORT_DATE FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE SNO = ?",
						Date.class, rows.get(0).getSno());
			} catch (Exception e) {
				try {
					reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE", Date.class);
				} catch (Exception ex) {
					logger.warn("Could not query REPORT_DATE from archival table: {}", ex.getMessage());
				}
			}

			if (reportDate != null) {
				try {
					BigDecimal maxVer = findMaxResubVersion(reportDate);
					BigDecimal nextVersion = getNextArchivalVersion(reportDate);
					logger.info("Resubmission mode: Cloned max version {} into new archival version {}", maxVer, nextVersion);

					// Insert modified summary records directly into nextVersion in archival summary table
					String insertArchivalSummarySql = "INSERT INTO BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE "
							+ "(SNO, NAME_OF_BORROWER, BRANCH, FINACLE_AC_ID, FINACLE_ID, TOT_FRGN_CCY_EXPOSURE, "
							+ "UFCE, UFCE_PERCENT, LIKELY_LOSS, EBID, LIKELY_LOSS_EBID, INCREMENTAL_PROV_REQ, FUND_BASED_ON_DATE, "
							+ "PROV_REQUIRED, REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
							+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
							+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE) + 1, "
							+ "NAME_OF_BORROWER, ?, FINACLE_AC_ID, FINACLE_ID, ?, "
							+ "?, ?, LIKELY_LOSS, ?, LIKELY_LOSS_EBID, ?, FUND_BASED_ON_DATE, "
							+ "?, REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
							+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG "
							+ "FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE SNO = ?";

					for (UFCE_ANNEXURE_Update_Row row : rows) {
						if (row.getSno() == null) continue;
						jdbcTemplate.update(insertArchivalSummarySql,
								row.getBranch(),
								row.getTotFrgnCcyExposure(),
								row.getUfce(),
								row.getUfcePercent(),
								row.getEbid(),
								row.getIncrementalProvReq(),
								row.getProvRequired(),
								nextVersion,
								row.getSno());
					}

					// Clone maxVer detail records to nextVersion in archival detail table
					String cloneDetailSql = "INSERT INTO BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE "
							+ "(SNO, NAME_OF_BORROWER, BRANCH, FINACLE_AC_ID, FINACLE_ID, TOT_FRGN_CCY_EXPOSURE, "
							+ "UFCE, UFCE_PERCENT, LIKELY_LOSS, EBID, LIKELY_LOSS_EBID, INCREMENTAL_PROV_REQ, FUND_BASED_ON_DATE, "
							+ "PROV_REQUIRED, REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
							+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
							+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE) + ROWNUM, "
							+ "NAME_OF_BORROWER, BRANCH, FINACLE_AC_ID, FINACLE_ID, TOT_FRGN_CCY_EXPOSURE, "
							+ "UFCE, UFCE_PERCENT, LIKELY_LOSS, EBID, LIKELY_LOSS_EBID, INCREMENTAL_PROV_REQ, FUND_BASED_ON_DATE, "
							+ "PROV_REQUIRED, REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
							+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG "
							+ "FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

					jdbcTemplate.update(cloneDetailSql, nextVersion, reportDate, maxVer);

					String updateArchivalDetailSql = "UPDATE BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE SET "
							+ "BRANCH = ?, TOT_FRGN_CCY_EXPOSURE = ?, UFCE = ?, UFCE_PERCENT = ?, EBID = ?, INCREMENTAL_PROV_REQ = ?, PROV_REQUIRED = ? "
							+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ? "
							+ "AND ("
							+ "  (FINACLE_AC_ID IS NOT NULL AND FINACLE_AC_ID = (SELECT FINACLE_AC_ID FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE SNO = ?)) "
							+ "  OR (FINACLE_ID IS NOT NULL AND FINACLE_ID = (SELECT FINACLE_ID FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE SNO = ?)) "
							+ "  OR (NAME_OF_BORROWER IS NOT NULL AND NAME_OF_BORROWER = (SELECT NAME_OF_BORROWER FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE WHERE SNO = ?))"
							+ ")";

					for (UFCE_ANNEXURE_Update_Row row : rows) {
						if (row.getSno() == null) continue;
						try {
							jdbcTemplate.update(updateArchivalDetailSql, row.getBranch(), row.getTotFrgnCcyExposure(),
									row.getUfce(), row.getUfcePercent(), row.getEbid(), row.getIncrementalProvReq(), row.getProvRequired(),
									reportDate, nextVersion, row.getSno(), row.getSno(), row.getSno());
						} catch (Exception ex) {
							logger.warn("Detail archival update warning for SNO {}: {}", row.getSno(), ex.getMessage());
						}
					}

					if (rows.get(0).getUfcePercent() != null) {
						String bulkSummaryUfcePercentSql = "UPDATE BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE SET UFCE_PERCENT = ? WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
						jdbcTemplate.update(bulkSummaryUfcePercentSql, rows.get(0).getUfcePercent(), reportDate, nextVersion);

						String bulkDetailUfcePercentSql = "UPDATE BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE SET UFCE_PERCENT = ? WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
						jdbcTemplate.update(bulkDetailUfcePercentSql, rows.get(0).getUfcePercent(), reportDate, nextVersion);
					}

				} catch (Exception e) {
					logger.error("Error performing resubmission update: {}", e.getMessage(), e);
				}
			}
			return;
		}

		// =====================================================
		// NORMAL MODE: Archive current un-modified snapshot FIRST, then Update Live Tables
		// =====================================================
		String summaryTable = "BRRS_ANNEXURE_UFCE_SUMMARYTABLE";
		String detailTable = "BRRS_ANNEXURE_UFCE_DETAILTABLE";

		Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject("SELECT REPORT_DATE FROM " + summaryTable + " WHERE SNO = ?",
					Date.class, rows.get(0).getSno());
		} catch (Exception e) {
			try {
				reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + summaryTable, Date.class);
			} catch (Exception ex) {
				logger.warn("Could not query REPORT_DATE: {}", ex.getMessage());
			}
		}

		// Step 1: Copy CURRENT un-modified record from SUMMARY/DETAIL table into ARCHIVAL table as maxVersion + 1 FIRST
		if (reportDate != null) {
			try {
				BigDecimal nextVersion = getNextArchivalVersion(reportDate);
				logger.info("Archiving un-modified current snapshot for date {} as version {} before modifying summary",
						reportDate, nextVersion);

				String archiveSummarySql = "INSERT INTO BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE "
						+ "(SNO, NAME_OF_BORROWER, BRANCH, FINACLE_AC_ID, FINACLE_ID, TOT_FRGN_CCY_EXPOSURE, "
						+ "UFCE, UFCE_PERCENT, LIKELY_LOSS, EBID, LIKELY_LOSS_EBID, INCREMENTAL_PROV_REQ, FUND_BASED_ON_DATE, "
						+ "PROV_REQUIRED, REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
						+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
						+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE) + ROWNUM, "
						+ "NAME_OF_BORROWER, BRANCH, FINACLE_AC_ID, FINACLE_ID, TOT_FRGN_CCY_EXPOSURE, "
						+ "UFCE, UFCE_PERCENT, LIKELY_LOSS, EBID, LIKELY_LOSS_EBID, INCREMENTAL_PROV_REQ, FUND_BASED_ON_DATE, "
						+ "PROV_REQUIRED, REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
						+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG "
						+ "FROM BRRS_ANNEXURE_UFCE_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";

				int summaryCount = jdbcTemplate.update(archiveSummarySql, nextVersion, reportDate);
				logger.info("Archived {} summary records into BRRS_ANNEXURE_UFCE_ARCHIVAL_SUMMARYTABLE with version {}",
						summaryCount, nextVersion);

				String archiveDetailSql = "INSERT INTO BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE "
						+ "(SNO, NAME_OF_BORROWER, BRANCH, FINACLE_AC_ID, FINACLE_ID, TOT_FRGN_CCY_EXPOSURE, "
						+ "UFCE, UFCE_PERCENT, LIKELY_LOSS, EBID, LIKELY_LOSS_EBID, INCREMENTAL_PROV_REQ, FUND_BASED_ON_DATE, "
						+ "PROV_REQUIRED, REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
						+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
						+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE) + ROWNUM, "
						+ "NAME_OF_BORROWER, BRANCH, FINACLE_AC_ID, FINACLE_ID, TOT_FRGN_CCY_EXPOSURE, "
						+ "UFCE, UFCE_PERCENT, LIKELY_LOSS, EBID, LIKELY_LOSS_EBID, INCREMENTAL_PROV_REQ, FUND_BASED_ON_DATE, "
						+ "PROV_REQUIRED, REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
						+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG "
						+ "FROM BRRS_ANNEXURE_UFCE_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";

				int detailCount = jdbcTemplate.update(archiveDetailSql, nextVersion, reportDate);
				logger.info("Archived {} detail records into BRRS_ANNEXURE_UFCE_ARCHIVAL_DETAILTABLE with version {}",
						detailCount, nextVersion);
			} catch (Exception e) {
				logger.error("Error archiving current summary & detail snapshot before updates: {}", e.getMessage(), e);
			}
		}

		// Step 2: NOW apply user's inline updates to main summary and detail tables
		String summarySql = "UPDATE " + summaryTable + " SET " + "BRANCH = ?, " + "TOT_FRGN_CCY_EXPOSURE = ?, "
				+ "UFCE = ?, " + "UFCE_PERCENT = ?, " + "EBID = ?, " + "INCREMENTAL_PROV_REQ = ?, " + "PROV_REQUIRED = ? " + "WHERE SNO = ?";

		String detailSql = "UPDATE " + detailTable + " SET " + "BRANCH = ?, " + "TOT_FRGN_CCY_EXPOSURE = ?, "
				+ "UFCE = ?, " + "UFCE_PERCENT = ?, " + "EBID = ?, " + "INCREMENTAL_PROV_REQ = ?, " + "PROV_REQUIRED = ? " + "WHERE SNO = ?";

		for (UFCE_ANNEXURE_Update_Row row : rows) {
			if (row.getSno() == null)
				continue;

			jdbcTemplate.update(summarySql, row.getBranch(), row.getTotFrgnCcyExposure(), row.getUfce(),
					row.getUfcePercent(), row.getEbid(), row.getIncrementalProvReq(), row.getProvRequired(), row.getSno());

			int detailRowsUpdated = jdbcTemplate.update(detailSql, row.getBranch(), row.getTotFrgnCcyExposure(),
					row.getUfce(), row.getUfcePercent(), row.getEbid(), row.getIncrementalProvReq(), row.getProvRequired(), row.getSno());

			if (detailRowsUpdated == 0) {
				String detailSqlFallback = "UPDATE " + detailTable + " SET "
						+ "BRANCH = ?, TOT_FRGN_CCY_EXPOSURE = ?, UFCE = ?, UFCE_PERCENT = ?, EBID = ?, INCREMENTAL_PROV_REQ = ?, PROV_REQUIRED = ? "
						+ "WHERE TRUNC(REPORT_DATE) = TRUNC((SELECT REPORT_DATE FROM " + summaryTable
						+ " WHERE SNO = ?)) "
						+ "AND ("
						+ "  (FINACLE_AC_ID IS NOT NULL AND FINACLE_AC_ID = (SELECT FINACLE_AC_ID FROM " + summaryTable
						+ " WHERE SNO = ?)) OR " + "  (FINACLE_ID IS NOT NULL AND FINACLE_ID = (SELECT FINACLE_ID FROM "
						+ summaryTable + " WHERE SNO = ?)) OR "
						+ "  (NAME_OF_BORROWER IS NOT NULL AND NAME_OF_BORROWER = (SELECT NAME_OF_BORROWER FROM "
						+ summaryTable + " WHERE SNO = ?))" + ")";

				try {
					int count = jdbcTemplate.update(detailSqlFallback, row.getBranch(), row.getTotFrgnCcyExposure(),
							row.getUfce(), row.getUfcePercent(), row.getEbid(), row.getIncrementalProvReq(), row.getProvRequired(),
							row.getSno(), row.getSno(), row.getSno(), row.getSno());
					logger.info("Updated {} detail rows via fallback matching for SNO {}", count, row.getSno());
				} catch (Exception ex) {
					logger.warn("Detail table fallback update warning for SNO {}: {}", row.getSno(), ex.getMessage());
				}
			}
		}

		if (rows.get(0).getUfcePercent() != null && reportDate != null) {
			try {
				String bulkSummaryUfcePercentSql = "UPDATE " + summaryTable + " SET UFCE_PERCENT = ? WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				jdbcTemplate.update(bulkSummaryUfcePercentSql, rows.get(0).getUfcePercent(), reportDate);

				String bulkDetailUfcePercentSql = "UPDATE " + detailTable + " SET UFCE_PERCENT = ? WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				jdbcTemplate.update(bulkDetailUfcePercentSql, rows.get(0).getUfcePercent(), reportDate);
			} catch (Exception ex) {
				logger.warn("Bulk UFCE_PERCENT update warning: {}", ex.getMessage());
			}
		}
	}

	// ===========================================================
	// EXCEL GENERATION
	// ===========================================================

	public byte[] getBRRS_UFCE_ANNEXURE_Excel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process for UFCE_ANNEXURE.");

		Date reportDate = parseDate(todate);
		String formattedDate = formatDateForQuery(reportDate);

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			List<UFCE_ANNEXURE_Archival_Summary_Entity> dataList = getArchivalDataByDateAndVersion(reportDate, version);
			return generateExcelFromArchivalData(dataList, filename);
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			List<UFCE_ANNEXURE_Archival_Summary_Entity> dataList = getResubDataByDateAndVersion(reportDate, version);
			return generateExcelFromArchivalData(dataList, filename);
		} else {
			List<UFCE_ANNEXURE_Summary_Entity> dataList = jdbcTemplate.query(
					"SELECT * FROM BRRS_ANNEXURE_UFCE_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
					new Object[] { formattedDate }, new SummaryRowMapper());
			return generateExcelFromNormalData(dataList, filename);
		}
	}

	private byte[] generateExcelFromNormalData(List<UFCE_ANNEXURE_Summary_Entity> dataList, String filename)
			throws Exception {
		if (dataList == null || dataList.isEmpty()) {
			logger.warn("No data found for UFCE_ANNEXURE report.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found : " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			Font font = workbook.createFont();
			font.setFontName("Calibri");
			font.setFontHeightInPoints((short) 11);

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setFont(font);
			textStyle.setWrapText(true);
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			DataFormat dataFormat = workbook.createDataFormat();
			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setFont(font);
			numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.setFont(font);
			percentStyle.setDataFormat(dataFormat.getFormat("0.00%"));
			percentStyle.setBorderBottom(BorderStyle.THIN);
			percentStyle.setBorderTop(BorderStyle.THIN);
			percentStyle.setBorderLeft(BorderStyle.THIN);
			percentStyle.setBorderRight(BorderStyle.THIN);

			CellStyle totalHeaderStyle = createTotalHeaderStyle(workbook);
			CellStyle totalNumberStyle = createTotalNumberStyle(workbook);

			// Populate UFCE_PERCENT in 3rd row, 7th column (row index 2, col index 6)
			BigDecimal ufcePercentVal = (dataList != null && !dataList.isEmpty()) ? dataList.get(0).getUfcePercent() : null;
			if (ufcePercentVal != null) {
				Row row3 = sheet.getRow(2);
				if (row3 == null) {
					row3 = sheet.createRow(2);
				}
				Cell cell7 = row3.getCell(6);
				if (cell7 == null) {
					cell7 = row3.createCell(6);
					Cell sampleCell = row3.getCell(7) != null ? row3.getCell(7) : row3.getCell(5);
					if (sampleCell != null && sampleCell.getCellStyle() != null) {
						cell7.setCellStyle(sampleCell.getCellStyle());
					}
				}
				
				if (ufcePercentVal.scale() <= 0 || ufcePercentVal.stripTrailingZeros().scale() <= 0) {
					cell7.setCellValue(ufcePercentVal.longValue());
				} else {
					cell7.setCellValue(ufcePercentVal.doubleValue());
				}
			}

			// Populate asondate in 3rd row, 11th column (row index 2, col index 10)
			Date rDate = (dataList != null && !dataList.isEmpty()) ? dataList.get(0).getReportDate() : null;
			if (rDate != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String asondateStr = sdf.format(rDate);
				Row row3 = sheet.getRow(2);
				if (row3 == null) {
					row3 = sheet.createRow(2);
				}
				Cell cell11 = row3.getCell(10);
				if (cell11 == null) {
					cell11 = row3.createCell(10);
					Cell sampleCell = row3.getCell(11) != null ? row3.getCell(11) : (row3.getCell(7) != null ? row3.getCell(7) : row3.getCell(9));
					if (sampleCell != null && sampleCell.getCellStyle() != null) {
						cell11.setCellStyle(sampleCell.getCellStyle());
					}
				}
				
				cell11.setCellValue(asondateStr);
			}

			int rowIndex = 4;

			BigDecimal totalFundBasedOnDate = BigDecimal.ZERO;
			BigDecimal totalProvRequired = BigDecimal.ZERO;

			for (UFCE_ANNEXURE_Summary_Entity data : dataList) {
				Row row = sheet.createRow(rowIndex++);
				setCellValue(row, 0, data.getNameOfBorrower(), textStyle);
				setCellValue(row, 1, data.getBranch(), textStyle);
				setCellValue(row, 2, data.getFinacleAcId(), numberStyle);
				setCellValue(row, 3, data.getFinacleId(), textStyle);
				setCellValue(row, 4, data.getTotFrgnCcyExposure(), numberStyle);
				setCellValue(row, 5, data.getUfce(), numberStyle);
				setCellValue(row, 6, data.getLikelyLoss(), numberStyle);
				setCellValue(row, 7, data.getEbid(), numberStyle);
				setPercentCellValue(row, 8, data.getLikelyLossEbid(), percentStyle);
				setPercentCellValue(row, 9, data.getIncrementalProvReq(), percentStyle);
				setCellValue(row, 10, data.getFundBasedOnDate(), numberStyle);
				setCellValue(row, 11, data.getProvRequired(), numberStyle);

				if (data.getFundBasedOnDate() != null) {
					totalFundBasedOnDate = totalFundBasedOnDate.add(data.getFundBasedOnDate());
				}
				if (data.getProvRequired() != null) {
					totalProvRequired = totalProvRequired.add(data.getProvRequired());
				}
			}

			// Add Total Row
			Row totalRow = sheet.createRow(rowIndex++);
			for (int c = 0; c <= 9; c++) {
				Cell cCell = totalRow.createCell(c);
				cCell.setCellStyle(totalHeaderStyle);
				if (c == 0) {
					cCell.setCellValue("Total:");
				}
			}
			try {
				sheet.addMergedRegion(new CellRangeAddress(totalRow.getRowNum(), totalRow.getRowNum(), 0, 9));
			} catch (Exception e) {
				// Ignore if merge fails
			}

			setCellValue(totalRow, 10, totalFundBasedOnDate, totalNumberStyle);
			setCellValue(totalRow, 11, totalProvRequired, totalNumberStyle);

			workbook.write(out);
			return out.toByteArray();
		}
	}

	private byte[] generateExcelFromArchivalData(List<UFCE_ANNEXURE_Archival_Summary_Entity> dataList, String filename)
			throws Exception {
		if (dataList == null || dataList.isEmpty()) {
			logger.warn("No archival data found for UFCE_ANNEXURE report.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found : " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			Font font = workbook.createFont();
			font.setFontName("Calibri");
			font.setFontHeightInPoints((short) 11);

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setFont(font);
			textStyle.setWrapText(true);
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			DataFormat dataFormat = workbook.createDataFormat();
			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setFont(font);
			numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.setFont(font);
			percentStyle.setDataFormat(dataFormat.getFormat("0.00%"));
			percentStyle.setBorderBottom(BorderStyle.THIN);
			percentStyle.setBorderTop(BorderStyle.THIN);
			percentStyle.setBorderLeft(BorderStyle.THIN);
			percentStyle.setBorderRight(BorderStyle.THIN);

			CellStyle totalHeaderStyle = createTotalHeaderStyle(workbook);
			CellStyle totalNumberStyle = createTotalNumberStyle(workbook);

			// Populate UFCE_PERCENT in 3rd row, 7th column (row index 2, col index 6)
			BigDecimal ufcePercentVal = (dataList != null && !dataList.isEmpty()) ? dataList.get(0).getUfcePercent() : null;
			if (ufcePercentVal != null) {
				Row row3 = sheet.getRow(2);
				if (row3 == null) {
					row3 = sheet.createRow(2);
				}
				Cell cell7 = row3.getCell(6);
				if (cell7 == null) {
					cell7 = row3.createCell(6);
					Cell sampleCell = row3.getCell(7) != null ? row3.getCell(7) : row3.getCell(5);
					if (sampleCell != null && sampleCell.getCellStyle() != null) {
						cell7.setCellStyle(sampleCell.getCellStyle());
					}
				}
				
				if (ufcePercentVal.scale() <= 0 || ufcePercentVal.stripTrailingZeros().scale() <= 0) {
					cell7.setCellValue(ufcePercentVal.longValue());
				} else {
					cell7.setCellValue(ufcePercentVal.doubleValue());
				}
			}

			// Populate asondate in 3rd row, 11th column (row index 2, col index 10)
			Date rDate = (dataList != null && !dataList.isEmpty()) ? dataList.get(0).getReportDate() : null;
			if (rDate != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String asondateStr = sdf.format(rDate);
				Row row3 = sheet.getRow(2);
				if (row3 == null) {
					row3 = sheet.createRow(2);
				}
				Cell cell11 = row3.getCell(10);
				if (cell11 == null) {
					cell11 = row3.createCell(10);
					Cell sampleCell = row3.getCell(11) != null ? row3.getCell(11) : (row3.getCell(7) != null ? row3.getCell(7) : row3.getCell(9));
					if (sampleCell != null && sampleCell.getCellStyle() != null) {
						cell11.setCellStyle(sampleCell.getCellStyle());
					}
				}
				
				cell11.setCellValue(asondateStr);
			}

			int rowIndex = 4;

			BigDecimal totalFundBasedOnDate = BigDecimal.ZERO;
			BigDecimal totalProvRequired = BigDecimal.ZERO;

			for (UFCE_ANNEXURE_Archival_Summary_Entity data : dataList) {
				Row row = sheet.createRow(rowIndex++);
				setCellValue(row, 0, data.getNameOfBorrower(), textStyle);
				setCellValue(row, 1, data.getBranch(), textStyle);
				setCellValue(row, 2, data.getFinacleAcId(), numberStyle);
				setCellValue(row, 3, data.getFinacleId(), textStyle);
				setCellValue(row, 4, data.getTotFrgnCcyExposure(), numberStyle);
				setCellValue(row, 5, data.getUfce(), numberStyle);
				setCellValue(row, 6, data.getLikelyLoss(), numberStyle);
				setCellValue(row, 7, data.getEbid(), numberStyle);
				setPercentCellValue(row, 8, data.getLikelyLossEbid(), percentStyle);
				setPercentCellValue(row, 9, data.getIncrementalProvReq(), percentStyle);
				setCellValue(row, 10, data.getFundBasedOnDate(), numberStyle);
				setCellValue(row, 11, data.getProvRequired(), numberStyle);

				if (data.getFundBasedOnDate() != null) {
					totalFundBasedOnDate = totalFundBasedOnDate.add(data.getFundBasedOnDate());
				}
				if (data.getProvRequired() != null) {
					totalProvRequired = totalProvRequired.add(data.getProvRequired());
				}
			}

			// Add Total Row
			Row totalRow = sheet.createRow(rowIndex++);
			for (int c = 0; c <= 9; c++) {
				Cell cCell = totalRow.createCell(c);
				cCell.setCellStyle(totalHeaderStyle);
				if (c == 0) {
					cCell.setCellValue("Total:");
				}
			}
			try {
				sheet.addMergedRegion(new CellRangeAddress(totalRow.getRowNum(), totalRow.getRowNum(), 0, 9));
			} catch (Exception e) {
				// Ignore if merge fails
			}

			setCellValue(totalRow, 10, totalFundBasedOnDate, totalNumberStyle);
			setCellValue(totalRow, 11, totalProvRequired, totalNumberStyle);

			workbook.write(out);
			return out.toByteArray();
		}
	}

	private CellStyle createTotalHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	private CellStyle createTotalNumberStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);

		DataFormat dataFormat = workbook.createDataFormat();
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setDataFormat(dataFormat.getFormat("#,##0.00"));
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	private void setPercentCellValue(Row row, int column, BigDecimal value, CellStyle style) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(style);
		if (value == null) {
			cell.setCellValue("");
		} else {
			double val = value.doubleValue();
			if (Math.abs(val) > 1.0) {
				cell.setCellValue(val / 100.0);
			} else {
				cell.setCellValue(val);
			}
		}
	}

	private void setCellValue(Row row, int column, Object value, CellStyle style) {
		Cell cell = row.createCell(column);
		if (value == null) {
			cell.setCellValue("");
			cell.setCellStyle(style);
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
			cell.setCellStyle(style);
		} else if (value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;
			if (bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0) {
				cell.setCellValue(bd.longValue());
			} else {
				cell.setCellValue(bd.doubleValue());
			}
			cell.setCellStyle(style);
		} else if (value instanceof Date) {
			cell.setCellValue((Date) value);
			cell.setCellStyle(style);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
			cell.setCellStyle(style);
		} else {
			cell.setCellValue(value.toString());
			cell.setCellStyle(style);
		}
	}
}
