package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service
public class BRRS_M_LCR_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LCR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ===========================================================
	// 1. COMPOSITE KEY CLASS (For Archival Entities)
	// ===========================================================

	public static class M_LCR_PK implements Serializable {
		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public M_LCR_PK() {
		}

		public M_LCR_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_LCR_PK))
				return false;
			M_LCR_PK that = (M_LCR_PK) o;
			return Objects.equals(REPORT_DATE, that.REPORT_DATE) && Objects.equals(REPORT_VERSION, that.REPORT_VERSION);
		}

		@Override
		public int hashCode() {
			return Objects.hash(REPORT_DATE, REPORT_VERSION);
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}
	}

	// ===========================================================
	// 2. ENTITY CLASSES (Static Inner Classes)
	// ===========================================================

	// 2.1 SUMMARY ENTITY
	public static class M_LCR_Summary_Entity {
		private String r10_product;
		private String r11_product;
		private BigDecimal r11_amount_factor;
		private BigDecimal r11_bob_total_amount;
		private BigDecimal r11_bob_with_factor_applied;
		private String r12_product;
		private BigDecimal r12_amount_factor;
		private BigDecimal r12_bob_total_amount;
		private BigDecimal r12_bob_with_factor_applied;
		private String r13_product;
		private BigDecimal r13_amount_factor;
		private BigDecimal r13_bob_total_amount;
		private BigDecimal r13_bob_with_factor_applied;
		private String r14_product;
		private BigDecimal r14_amount_factor;
		private BigDecimal r14_bob_total_amount;
		private BigDecimal r14_bob_with_factor_applied;
		private String r15_product;
		private BigDecimal r15_amount_factor;
		private BigDecimal r15_bob_total_amount;
		private BigDecimal r15_bob_with_factor_applied;
		private String r16_product;
		private BigDecimal r16_amount_factor;
		private BigDecimal r16_bob_total_amount;
		private BigDecimal r16_bob_with_factor_applied;
		private String r17_product;
		private BigDecimal r17_amount_factor;
		private BigDecimal r17_bob_total_amount;
		private BigDecimal r17_bob_with_factor_applied;
		private String r18_product;
		private BigDecimal r18_amount_factor;
		private BigDecimal r18_bob_total_amount;
		private BigDecimal r18_bob_with_factor_applied;
		private String r19_product;
		private BigDecimal r19_amount_factor;
		private BigDecimal r19_bob_total_amount;
		private BigDecimal r19_bob_with_factor_applied;
		private String r20_product;
		private BigDecimal r20_amount_factor;
		private BigDecimal r20_bob_total_amount;
		private BigDecimal r20_bob_with_factor_applied;
		private String r21_product;
		private BigDecimal r21_amount_factor;
		private BigDecimal r21_bob_total_amount;
		private BigDecimal r21_bob_with_factor_applied;
		private String r22_product;
		private BigDecimal r22_amount_factor;
		private BigDecimal r22_bob_total_amount;
		private BigDecimal r22_bob_with_factor_applied;
		private String r23_product;
		private BigDecimal r23_amount_factor;
		private BigDecimal r23_bob_total_amount;
		private BigDecimal r23_bob_with_factor_applied;
		private String r24_product;
		private BigDecimal r24_amount_factor;
		private BigDecimal r24_bob_total_amount;
		private BigDecimal r24_bob_with_factor_applied;
		private String r25_product;
		private BigDecimal r26_amount_factor;
		private BigDecimal r26_bob_total_amount;
		private BigDecimal r26_bob_with_factor_applied;
		private String r26_product;
		private String r27_product;
		private BigDecimal r27_amount_factor;
		private BigDecimal r27_bob_total_amount;
		private BigDecimal r27_bob_with_factor_applied;
		private String r28_product;
		private BigDecimal r28_amount_factor;
		private BigDecimal r28_bob_total_amount;
		private BigDecimal r28_bob_with_factor_applied;
		private String r29_product;
		private BigDecimal r29_amount_factor;
		private BigDecimal r29_bob_total_amount;
		private BigDecimal r29_bob_with_factor_applied;
		private String r30_product;
		private BigDecimal r30_amount_factor;
		private BigDecimal r30_bob_total_amount;
		private BigDecimal r30_bob_with_factor_applied;
		private String r31_product;
		private BigDecimal r31_amount_factor;
		private BigDecimal r31_bob_total_amount;
		private BigDecimal r31_bob_with_factor_applied;
		private String r32_product;
		private BigDecimal r32_amount_factor;
		private BigDecimal r32_bob_total_amount;
		private BigDecimal r32_bob_with_factor_applied;
		private String r33_product;
		private BigDecimal r33_amount_factor;
		private BigDecimal r33_bob_total_amount;
		private BigDecimal r33_bob_with_factor_applied;
		private String r34_product;
		private BigDecimal r34_amount_factor;
		private BigDecimal r34_bob_total_amount;
		private BigDecimal r34_bob_with_factor_applied;
		private String r35_product;
		private BigDecimal r35_amount_factor;
		private BigDecimal r35_bob_total_amount;
		private BigDecimal r35_bob_with_factor_applied;
		private String r36_product;
		private BigDecimal r36_amount_factor;
		private BigDecimal r36_bob_total_amount;
		private BigDecimal r36_bob_with_factor_applied;
		private String r37_product;
		private BigDecimal r37_amount_factor;
		private BigDecimal r37_bob_total_amount;
		private BigDecimal r37_bob_with_factor_applied;
		private String r38_product;
		private BigDecimal r38_amount_factor;
		private BigDecimal r38_bob_total_amount;
		private BigDecimal r38_bob_with_factor_applied;
		private String r39_product;
		private BigDecimal r39_amount_factor;
		private BigDecimal r39_bob_total_amount;
		private BigDecimal r39_bob_with_factor_applied;
		private String r40_product;
		private BigDecimal r40_amount_factor;
		private BigDecimal r40_bob_total_amount;
		private BigDecimal r40_bob_with_factor_applied;
		private String r41_product;
		private BigDecimal r41_amount_factor;
		private BigDecimal r41_bob_total_amount;
		private BigDecimal r41_bob_with_factor_applied;
		private String r42_product;
		private BigDecimal r42_amount_factor;
		private BigDecimal r42_bob_total_amount;
		private BigDecimal r42_bob_with_factor_applied;
		private String r43_product;
		private BigDecimal r43_amount_factor;
		private BigDecimal r43_bob_total_amount;
		private BigDecimal r43_bob_with_factor_applied;
		private String r44_product;
		private BigDecimal r44_amount_factor;
		private BigDecimal r44_bob_total_amount;
		private BigDecimal r44_bob_with_factor_applied;
		private String r45_product;
		private BigDecimal r45_amount_factor;
		private BigDecimal r45_bob_total_amount;
		private BigDecimal r45_bob_with_factor_applied;
		private String r46_product;
		private BigDecimal r46_amount_factor;
		private BigDecimal r46_bob_total_amount;
		private BigDecimal r46_bob_with_factor_applied;
		private String r47_product;
		private BigDecimal r47_amount_factor;
		private BigDecimal r47_bob_total_amount;
		private BigDecimal r47_bob_with_factor_applied;
		private String r48_product;
		private BigDecimal r48_amount_factor;
		private BigDecimal r48_bob_total_amount;
		private BigDecimal r48_bob_with_factor_applied;
		private String r49_product;
		private BigDecimal r49_amount_factor;
		private BigDecimal r49_bob_total_amount;
		private BigDecimal r49_bob_with_factor_applied;
		private String r50_product;
		private BigDecimal r50_amount_factor;
		private BigDecimal r50_bob_total_amount;
		private BigDecimal r50_bob_with_factor_applied;
		private String r51_product;
		private BigDecimal r51_amount_factor;
		private BigDecimal r51_bob_total_amount;
		private BigDecimal r51_bob_with_factor_applied;
		private String r52_product;
		private BigDecimal r52_amount_factor;
		private BigDecimal r52_bob_total_amount;
		private BigDecimal r52_bob_with_factor_applied;
		private String r53_product;
		private BigDecimal r53_amount_factor;
		private BigDecimal r53_bob_total_amount;
		private BigDecimal r53_bob_with_factor_applied;
		private String r54_product;
		private BigDecimal r54_amount_factor;
		private BigDecimal r54_bob_total_amount;
		private BigDecimal r54_bob_with_factor_applied;
		private String r55_product;
		private BigDecimal r55_amount_factor;
		private BigDecimal r55_bob_total_amount;
		private BigDecimal r55_bob_with_factor_applied;
		private String r56_product;
		private BigDecimal r56_amount_factor;
		private BigDecimal r56_bob_total_amount;
		private BigDecimal r56_bob_with_factor_applied;
		private String r57_product;
		private BigDecimal r57_amount_factor;
		private BigDecimal r57_bob_total_amount;
		private BigDecimal r57_bob_with_factor_applied;
		private String r58_product;
		private BigDecimal r58_amount_factor;
		private BigDecimal r58_bob_total_amount;
		private BigDecimal r58_bob_with_factor_applied;
		private String r59_product;
		private BigDecimal r59_amount_factor;
		private BigDecimal r59_bob_total_amount;
		private BigDecimal r59_bob_with_factor_applied;
		private String r60_product;
		private BigDecimal r60_amount_factor;
		private BigDecimal r60_bob_total_amount;
		private BigDecimal r60_bob_with_factor_applied;
		private String r61_product;
		private BigDecimal r61_amount_factor;
		private BigDecimal r61_bob_total_amount;
		private BigDecimal r61_bob_with_factor_applied;
		private String r62_product;
		private BigDecimal r62_amount_factor;
		private BigDecimal r62_bob_total_amount;
		private BigDecimal r62_bob_with_factor_applied;
		private String r63_product;
		private BigDecimal r63_amount_factor;
		private BigDecimal r63_bob_total_amount;
		private BigDecimal r63_bob_with_factor_applied;
		private String r64_product;
		private BigDecimal r64_amount_factor;
		private BigDecimal r64_bob_total_amount;
		private BigDecimal r64_bob_with_factor_applied;
		private String r65_product;
		private BigDecimal r65_amount_factor;
		private BigDecimal r65_bob_total_amount;
		private BigDecimal r65_bob_with_factor_applied;
		private String r66_product;
		private BigDecimal r66_amount_factor;
		private BigDecimal r66_bob_total_amount;
		private BigDecimal r66_bob_with_factor_applied;
		private String r67_product;
		private BigDecimal r67_amount_factor;
		private BigDecimal r67_bob_total_amount;
		private BigDecimal r67_bob_with_factor_applied;
		private String r68_product;
		private BigDecimal r68_amount_factor;
		private BigDecimal r68_bob_total_amount;
		private BigDecimal r68_bob_with_factor_applied;
		private String r69_product;
		private BigDecimal r69_amount_factor;
		private BigDecimal r69_bob_total_amount;
		private BigDecimal r69_bob_with_factor_applied;
		private String r70_product;
		private BigDecimal r70_amount_factor;
		private BigDecimal r70_bob_total_amount;
		private BigDecimal r70_bob_with_factor_applied;
		private String r72_product;
		private BigDecimal r72_amount_factor;
		private BigDecimal r72_bob_total_amount;
		private BigDecimal r72_bob_with_factor_applied;
		private String r73_product;
		private BigDecimal r73_amount_factor;
		private BigDecimal r73_bob_total_amount;
		private BigDecimal r73_bob_with_factor_applied;
		private String r74_product;
		private BigDecimal r74_amount_factor;
		private BigDecimal r74_bob_total_amount;
		private BigDecimal r74_bob_with_factor_applied;
		private String r75_product;
		private BigDecimal r75_amount_factor;
		private BigDecimal r75_bob_total_amount;
		private BigDecimal r75_bob_with_factor_applied;
		private String r76_product;
		private BigDecimal r76_amount_factor;
		private BigDecimal r76_bob_total_amount;
		private BigDecimal r76_bob_with_factor_applied;
		private String r77_product;
		private BigDecimal r77_amount_factor;
		private BigDecimal r77_bob_total_amount;
		private BigDecimal r77_bob_with_factor_applied;
		private String r78_product;
		private BigDecimal r78_amount_factor;
		private BigDecimal r78_bob_total_amount;
		private BigDecimal r78_bob_with_factor_applied;
		private String r79_product;
		private BigDecimal r79_amount_factor;
		private BigDecimal r79_bob_total_amount;
		private BigDecimal r79_bob_with_factor_applied;
		private String r80_product;
		private BigDecimal r80_amount_factor;
		private BigDecimal r80_bob_total_amount;
		private BigDecimal r80_bob_with_factor_applied;
		private String r81_product;
		private BigDecimal r81_amount_factor;
		private BigDecimal r81_bob_total_amount;
		private BigDecimal r81_bob_with_factor_applied;
		private String r82_product;
		private BigDecimal r82_amount_factor;
		private BigDecimal r82_bob_total_amount;
		private BigDecimal r82_bob_with_factor_applied;
		private String r83_product;
		private BigDecimal r83_amount_factor;
		private BigDecimal r83_bob_total_amount;
		private BigDecimal r83_bob_with_factor_applied;
		private String r84_product;
		private BigDecimal r84_amount_factor;
		private BigDecimal r84_bob_total_amount;
		private BigDecimal r84_bob_with_factor_applied;
		private String r85_product;
		private BigDecimal r85_amount_factor;
		private BigDecimal r85_bob_total_amount;
		private BigDecimal r85_bob_with_factor_applied;
		private String r86_product;
		private BigDecimal r86_amount_factor;
		private BigDecimal r86_bob_total_amount;
		private BigDecimal r86_bob_with_factor_applied;
		private String r87_product;
		private BigDecimal r87_amount_factor;
		private BigDecimal r87_bob_total_amount;
		private BigDecimal r87_bob_with_factor_applied;
		private String r88_product;
		private BigDecimal r88_amount_factor;
		private BigDecimal r88_bob_total_amount;
		private BigDecimal r88_bob_with_factor_applied;
		private String r89_product;
		private BigDecimal r89_amount_factor;
		private BigDecimal r89_bob_total_amount;
		private BigDecimal r89_bob_with_factor_applied;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		// Getters and Setters for all fields (I'll include the main ones, but you'll
		// need all)
		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_amount_factor() {
			return r11_amount_factor;
		}

		public void setR11_amount_factor(BigDecimal r11_amount_factor) {
			this.r11_amount_factor = r11_amount_factor;
		}

		public BigDecimal getR11_bob_total_amount() {
			return r11_bob_total_amount;
		}

		public void setR11_bob_total_amount(BigDecimal r11_bob_total_amount) {
			this.r11_bob_total_amount = r11_bob_total_amount;
		}

		public BigDecimal getR11_bob_with_factor_applied() {
			return r11_bob_with_factor_applied;
		}

		public void setR11_bob_with_factor_applied(BigDecimal r11_bob_with_factor_applied) {
			this.r11_bob_with_factor_applied = r11_bob_with_factor_applied;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_amount_factor() {
			return r12_amount_factor;
		}

		public void setR12_amount_factor(BigDecimal r12_amount_factor) {
			this.r12_amount_factor = r12_amount_factor;
		}

		public BigDecimal getR12_bob_total_amount() {
			return r12_bob_total_amount;
		}

		public void setR12_bob_total_amount(BigDecimal r12_bob_total_amount) {
			this.r12_bob_total_amount = r12_bob_total_amount;
		}

		public BigDecimal getR12_bob_with_factor_applied() {
			return r12_bob_with_factor_applied;
		}

		public void setR12_bob_with_factor_applied(BigDecimal r12_bob_with_factor_applied) {
			this.r12_bob_with_factor_applied = r12_bob_with_factor_applied;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_amount_factor() {
			return r13_amount_factor;
		}

		public void setR13_amount_factor(BigDecimal r13_amount_factor) {
			this.r13_amount_factor = r13_amount_factor;
		}

		public BigDecimal getR13_bob_total_amount() {
			return r13_bob_total_amount;
		}

		public void setR13_bob_total_amount(BigDecimal r13_bob_total_amount) {
			this.r13_bob_total_amount = r13_bob_total_amount;
		}

		public BigDecimal getR13_bob_with_factor_applied() {
			return r13_bob_with_factor_applied;
		}

		public void setR13_bob_with_factor_applied(BigDecimal r13_bob_with_factor_applied) {
			this.r13_bob_with_factor_applied = r13_bob_with_factor_applied;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_amount_factor() {
			return r14_amount_factor;
		}

		public void setR14_amount_factor(BigDecimal r14_amount_factor) {
			this.r14_amount_factor = r14_amount_factor;
		}

		public BigDecimal getR14_bob_total_amount() {
			return r14_bob_total_amount;
		}

		public void setR14_bob_total_amount(BigDecimal r14_bob_total_amount) {
			this.r14_bob_total_amount = r14_bob_total_amount;
		}

		public BigDecimal getR14_bob_with_factor_applied() {
			return r14_bob_with_factor_applied;
		}

		public void setR14_bob_with_factor_applied(BigDecimal r14_bob_with_factor_applied) {
			this.r14_bob_with_factor_applied = r14_bob_with_factor_applied;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_amount_factor() {
			return r15_amount_factor;
		}

		public void setR15_amount_factor(BigDecimal r15_amount_factor) {
			this.r15_amount_factor = r15_amount_factor;
		}

		public BigDecimal getR15_bob_total_amount() {
			return r15_bob_total_amount;
		}

		public void setR15_bob_total_amount(BigDecimal r15_bob_total_amount) {
			this.r15_bob_total_amount = r15_bob_total_amount;
		}

		public BigDecimal getR15_bob_with_factor_applied() {
			return r15_bob_with_factor_applied;
		}

		public void setR15_bob_with_factor_applied(BigDecimal r15_bob_with_factor_applied) {
			this.r15_bob_with_factor_applied = r15_bob_with_factor_applied;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_amount_factor() {
			return r16_amount_factor;
		}

		public void setR16_amount_factor(BigDecimal r16_amount_factor) {
			this.r16_amount_factor = r16_amount_factor;
		}

		public BigDecimal getR16_bob_total_amount() {
			return r16_bob_total_amount;
		}

		public void setR16_bob_total_amount(BigDecimal r16_bob_total_amount) {
			this.r16_bob_total_amount = r16_bob_total_amount;
		}

		public BigDecimal getR16_bob_with_factor_applied() {
			return r16_bob_with_factor_applied;
		}

		public void setR16_bob_with_factor_applied(BigDecimal r16_bob_with_factor_applied) {
			this.r16_bob_with_factor_applied = r16_bob_with_factor_applied;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_amount_factor() {
			return r17_amount_factor;
		}

		public void setR17_amount_factor(BigDecimal r17_amount_factor) {
			this.r17_amount_factor = r17_amount_factor;
		}

		public BigDecimal getR17_bob_total_amount() {
			return r17_bob_total_amount;
		}

		public void setR17_bob_total_amount(BigDecimal r17_bob_total_amount) {
			this.r17_bob_total_amount = r17_bob_total_amount;
		}

		public BigDecimal getR17_bob_with_factor_applied() {
			return r17_bob_with_factor_applied;
		}

		public void setR17_bob_with_factor_applied(BigDecimal r17_bob_with_factor_applied) {
			this.r17_bob_with_factor_applied = r17_bob_with_factor_applied;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_amount_factor() {
			return r18_amount_factor;
		}

		public void setR18_amount_factor(BigDecimal r18_amount_factor) {
			this.r18_amount_factor = r18_amount_factor;
		}

		public BigDecimal getR18_bob_total_amount() {
			return r18_bob_total_amount;
		}

		public void setR18_bob_total_amount(BigDecimal r18_bob_total_amount) {
			this.r18_bob_total_amount = r18_bob_total_amount;
		}

		public BigDecimal getR18_bob_with_factor_applied() {
			return r18_bob_with_factor_applied;
		}

		public void setR18_bob_with_factor_applied(BigDecimal r18_bob_with_factor_applied) {
			this.r18_bob_with_factor_applied = r18_bob_with_factor_applied;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_amount_factor() {
			return r19_amount_factor;
		}

		public void setR19_amount_factor(BigDecimal r19_amount_factor) {
			this.r19_amount_factor = r19_amount_factor;
		}

		public BigDecimal getR19_bob_total_amount() {
			return r19_bob_total_amount;
		}

		public void setR19_bob_total_amount(BigDecimal r19_bob_total_amount) {
			this.r19_bob_total_amount = r19_bob_total_amount;
		}

		public BigDecimal getR19_bob_with_factor_applied() {
			return r19_bob_with_factor_applied;
		}

		public void setR19_bob_with_factor_applied(BigDecimal r19_bob_with_factor_applied) {
			this.r19_bob_with_factor_applied = r19_bob_with_factor_applied;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_amount_factor() {
			return r20_amount_factor;
		}

		public void setR20_amount_factor(BigDecimal r20_amount_factor) {
			this.r20_amount_factor = r20_amount_factor;
		}

		public BigDecimal getR20_bob_total_amount() {
			return r20_bob_total_amount;
		}

		public void setR20_bob_total_amount(BigDecimal r20_bob_total_amount) {
			this.r20_bob_total_amount = r20_bob_total_amount;
		}

		public BigDecimal getR20_bob_with_factor_applied() {
			return r20_bob_with_factor_applied;
		}

		public void setR20_bob_with_factor_applied(BigDecimal r20_bob_with_factor_applied) {
			this.r20_bob_with_factor_applied = r20_bob_with_factor_applied;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_amount_factor() {
			return r21_amount_factor;
		}

		public void setR21_amount_factor(BigDecimal r21_amount_factor) {
			this.r21_amount_factor = r21_amount_factor;
		}

		public BigDecimal getR21_bob_total_amount() {
			return r21_bob_total_amount;
		}

		public void setR21_bob_total_amount(BigDecimal r21_bob_total_amount) {
			this.r21_bob_total_amount = r21_bob_total_amount;
		}

		public BigDecimal getR21_bob_with_factor_applied() {
			return r21_bob_with_factor_applied;
		}

		public void setR21_bob_with_factor_applied(BigDecimal r21_bob_with_factor_applied) {
			this.r21_bob_with_factor_applied = r21_bob_with_factor_applied;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_amount_factor() {
			return r22_amount_factor;
		}

		public void setR22_amount_factor(BigDecimal r22_amount_factor) {
			this.r22_amount_factor = r22_amount_factor;
		}

		public BigDecimal getR22_bob_total_amount() {
			return r22_bob_total_amount;
		}

		public void setR22_bob_total_amount(BigDecimal r22_bob_total_amount) {
			this.r22_bob_total_amount = r22_bob_total_amount;
		}

		public BigDecimal getR22_bob_with_factor_applied() {
			return r22_bob_with_factor_applied;
		}

		public void setR22_bob_with_factor_applied(BigDecimal r22_bob_with_factor_applied) {
			this.r22_bob_with_factor_applied = r22_bob_with_factor_applied;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_amount_factor() {
			return r23_amount_factor;
		}

		public void setR23_amount_factor(BigDecimal r23_amount_factor) {
			this.r23_amount_factor = r23_amount_factor;
		}

		public BigDecimal getR23_bob_total_amount() {
			return r23_bob_total_amount;
		}

		public void setR23_bob_total_amount(BigDecimal r23_bob_total_amount) {
			this.r23_bob_total_amount = r23_bob_total_amount;
		}

		public BigDecimal getR23_bob_with_factor_applied() {
			return r23_bob_with_factor_applied;
		}

		public void setR23_bob_with_factor_applied(BigDecimal r23_bob_with_factor_applied) {
			this.r23_bob_with_factor_applied = r23_bob_with_factor_applied;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_amount_factor() {
			return r24_amount_factor;
		}

		public void setR24_amount_factor(BigDecimal r24_amount_factor) {
			this.r24_amount_factor = r24_amount_factor;
		}

		public BigDecimal getR24_bob_total_amount() {
			return r24_bob_total_amount;
		}

		public void setR24_bob_total_amount(BigDecimal r24_bob_total_amount) {
			this.r24_bob_total_amount = r24_bob_total_amount;
		}

		public BigDecimal getR24_bob_with_factor_applied() {
			return r24_bob_with_factor_applied;
		}

		public void setR24_bob_with_factor_applied(BigDecimal r24_bob_with_factor_applied) {
			this.r24_bob_with_factor_applied = r24_bob_with_factor_applied;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR26_amount_factor() {
			return r26_amount_factor;
		}

		public void setR26_amount_factor(BigDecimal r26_amount_factor) {
			this.r26_amount_factor = r26_amount_factor;
		}

		public BigDecimal getR26_bob_total_amount() {
			return r26_bob_total_amount;
		}

		public void setR26_bob_total_amount(BigDecimal r26_bob_total_amount) {
			this.r26_bob_total_amount = r26_bob_total_amount;
		}

		public BigDecimal getR26_bob_with_factor_applied() {
			return r26_bob_with_factor_applied;
		}

		public void setR26_bob_with_factor_applied(BigDecimal r26_bob_with_factor_applied) {
			this.r26_bob_with_factor_applied = r26_bob_with_factor_applied;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_amount_factor() {
			return r27_amount_factor;
		}

		public void setR27_amount_factor(BigDecimal r27_amount_factor) {
			this.r27_amount_factor = r27_amount_factor;
		}

		public BigDecimal getR27_bob_total_amount() {
			return r27_bob_total_amount;
		}

		public void setR27_bob_total_amount(BigDecimal r27_bob_total_amount) {
			this.r27_bob_total_amount = r27_bob_total_amount;
		}

		public BigDecimal getR27_bob_with_factor_applied() {
			return r27_bob_with_factor_applied;
		}

		public void setR27_bob_with_factor_applied(BigDecimal r27_bob_with_factor_applied) {
			this.r27_bob_with_factor_applied = r27_bob_with_factor_applied;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_amount_factor() {
			return r28_amount_factor;
		}

		public void setR28_amount_factor(BigDecimal r28_amount_factor) {
			this.r28_amount_factor = r28_amount_factor;
		}

		public BigDecimal getR28_bob_total_amount() {
			return r28_bob_total_amount;
		}

		public void setR28_bob_total_amount(BigDecimal r28_bob_total_amount) {
			this.r28_bob_total_amount = r28_bob_total_amount;
		}

		public BigDecimal getR28_bob_with_factor_applied() {
			return r28_bob_with_factor_applied;
		}

		public void setR28_bob_with_factor_applied(BigDecimal r28_bob_with_factor_applied) {
			this.r28_bob_with_factor_applied = r28_bob_with_factor_applied;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_amount_factor() {
			return r29_amount_factor;
		}

		public void setR29_amount_factor(BigDecimal r29_amount_factor) {
			this.r29_amount_factor = r29_amount_factor;
		}

		public BigDecimal getR29_bob_total_amount() {
			return r29_bob_total_amount;
		}

		public void setR29_bob_total_amount(BigDecimal r29_bob_total_amount) {
			this.r29_bob_total_amount = r29_bob_total_amount;
		}

		public BigDecimal getR29_bob_with_factor_applied() {
			return r29_bob_with_factor_applied;
		}

		public void setR29_bob_with_factor_applied(BigDecimal r29_bob_with_factor_applied) {
			this.r29_bob_with_factor_applied = r29_bob_with_factor_applied;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_amount_factor() {
			return r30_amount_factor;
		}

		public void setR30_amount_factor(BigDecimal r30_amount_factor) {
			this.r30_amount_factor = r30_amount_factor;
		}

		public BigDecimal getR30_bob_total_amount() {
			return r30_bob_total_amount;
		}

		public void setR30_bob_total_amount(BigDecimal r30_bob_total_amount) {
			this.r30_bob_total_amount = r30_bob_total_amount;
		}

		public BigDecimal getR30_bob_with_factor_applied() {
			return r30_bob_with_factor_applied;
		}

		public void setR30_bob_with_factor_applied(BigDecimal r30_bob_with_factor_applied) {
			this.r30_bob_with_factor_applied = r30_bob_with_factor_applied;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_amount_factor() {
			return r31_amount_factor;
		}

		public void setR31_amount_factor(BigDecimal r31_amount_factor) {
			this.r31_amount_factor = r31_amount_factor;
		}

		public BigDecimal getR31_bob_total_amount() {
			return r31_bob_total_amount;
		}

		public void setR31_bob_total_amount(BigDecimal r31_bob_total_amount) {
			this.r31_bob_total_amount = r31_bob_total_amount;
		}

		public BigDecimal getR31_bob_with_factor_applied() {
			return r31_bob_with_factor_applied;
		}

		public void setR31_bob_with_factor_applied(BigDecimal r31_bob_with_factor_applied) {
			this.r31_bob_with_factor_applied = r31_bob_with_factor_applied;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_amount_factor() {
			return r32_amount_factor;
		}

		public void setR32_amount_factor(BigDecimal r32_amount_factor) {
			this.r32_amount_factor = r32_amount_factor;
		}

		public BigDecimal getR32_bob_total_amount() {
			return r32_bob_total_amount;
		}

		public void setR32_bob_total_amount(BigDecimal r32_bob_total_amount) {
			this.r32_bob_total_amount = r32_bob_total_amount;
		}

		public BigDecimal getR32_bob_with_factor_applied() {
			return r32_bob_with_factor_applied;
		}

		public void setR32_bob_with_factor_applied(BigDecimal r32_bob_with_factor_applied) {
			this.r32_bob_with_factor_applied = r32_bob_with_factor_applied;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_amount_factor() {
			return r33_amount_factor;
		}

		public void setR33_amount_factor(BigDecimal r33_amount_factor) {
			this.r33_amount_factor = r33_amount_factor;
		}

		public BigDecimal getR33_bob_total_amount() {
			return r33_bob_total_amount;
		}

		public void setR33_bob_total_amount(BigDecimal r33_bob_total_amount) {
			this.r33_bob_total_amount = r33_bob_total_amount;
		}

		public BigDecimal getR33_bob_with_factor_applied() {
			return r33_bob_with_factor_applied;
		}

		public void setR33_bob_with_factor_applied(BigDecimal r33_bob_with_factor_applied) {
			this.r33_bob_with_factor_applied = r33_bob_with_factor_applied;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_amount_factor() {
			return r34_amount_factor;
		}

		public void setR34_amount_factor(BigDecimal r34_amount_factor) {
			this.r34_amount_factor = r34_amount_factor;
		}

		public BigDecimal getR34_bob_total_amount() {
			return r34_bob_total_amount;
		}

		public void setR34_bob_total_amount(BigDecimal r34_bob_total_amount) {
			this.r34_bob_total_amount = r34_bob_total_amount;
		}

		public BigDecimal getR34_bob_with_factor_applied() {
			return r34_bob_with_factor_applied;
		}

		public void setR34_bob_with_factor_applied(BigDecimal r34_bob_with_factor_applied) {
			this.r34_bob_with_factor_applied = r34_bob_with_factor_applied;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_amount_factor() {
			return r35_amount_factor;
		}

		public void setR35_amount_factor(BigDecimal r35_amount_factor) {
			this.r35_amount_factor = r35_amount_factor;
		}

		public BigDecimal getR35_bob_total_amount() {
			return r35_bob_total_amount;
		}

		public void setR35_bob_total_amount(BigDecimal r35_bob_total_amount) {
			this.r35_bob_total_amount = r35_bob_total_amount;
		}

		public BigDecimal getR35_bob_with_factor_applied() {
			return r35_bob_with_factor_applied;
		}

		public void setR35_bob_with_factor_applied(BigDecimal r35_bob_with_factor_applied) {
			this.r35_bob_with_factor_applied = r35_bob_with_factor_applied;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_amount_factor() {
			return r36_amount_factor;
		}

		public void setR36_amount_factor(BigDecimal r36_amount_factor) {
			this.r36_amount_factor = r36_amount_factor;
		}

		public BigDecimal getR36_bob_total_amount() {
			return r36_bob_total_amount;
		}

		public void setR36_bob_total_amount(BigDecimal r36_bob_total_amount) {
			this.r36_bob_total_amount = r36_bob_total_amount;
		}

		public BigDecimal getR36_bob_with_factor_applied() {
			return r36_bob_with_factor_applied;
		}

		public void setR36_bob_with_factor_applied(BigDecimal r36_bob_with_factor_applied) {
			this.r36_bob_with_factor_applied = r36_bob_with_factor_applied;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_amount_factor() {
			return r37_amount_factor;
		}

		public void setR37_amount_factor(BigDecimal r37_amount_factor) {
			this.r37_amount_factor = r37_amount_factor;
		}

		public BigDecimal getR37_bob_total_amount() {
			return r37_bob_total_amount;
		}

		public void setR37_bob_total_amount(BigDecimal r37_bob_total_amount) {
			this.r37_bob_total_amount = r37_bob_total_amount;
		}

		public BigDecimal getR37_bob_with_factor_applied() {
			return r37_bob_with_factor_applied;
		}

		public void setR37_bob_with_factor_applied(BigDecimal r37_bob_with_factor_applied) {
			this.r37_bob_with_factor_applied = r37_bob_with_factor_applied;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_amount_factor() {
			return r38_amount_factor;
		}

		public void setR38_amount_factor(BigDecimal r38_amount_factor) {
			this.r38_amount_factor = r38_amount_factor;
		}

		public BigDecimal getR38_bob_total_amount() {
			return r38_bob_total_amount;
		}

		public void setR38_bob_total_amount(BigDecimal r38_bob_total_amount) {
			this.r38_bob_total_amount = r38_bob_total_amount;
		}

		public BigDecimal getR38_bob_with_factor_applied() {
			return r38_bob_with_factor_applied;
		}

		public void setR38_bob_with_factor_applied(BigDecimal r38_bob_with_factor_applied) {
			this.r38_bob_with_factor_applied = r38_bob_with_factor_applied;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_amount_factor() {
			return r39_amount_factor;
		}

		public void setR39_amount_factor(BigDecimal r39_amount_factor) {
			this.r39_amount_factor = r39_amount_factor;
		}

		public BigDecimal getR39_bob_total_amount() {
			return r39_bob_total_amount;
		}

		public void setR39_bob_total_amount(BigDecimal r39_bob_total_amount) {
			this.r39_bob_total_amount = r39_bob_total_amount;
		}

		public BigDecimal getR39_bob_with_factor_applied() {
			return r39_bob_with_factor_applied;
		}

		public void setR39_bob_with_factor_applied(BigDecimal r39_bob_with_factor_applied) {
			this.r39_bob_with_factor_applied = r39_bob_with_factor_applied;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_amount_factor() {
			return r40_amount_factor;
		}

		public void setR40_amount_factor(BigDecimal r40_amount_factor) {
			this.r40_amount_factor = r40_amount_factor;
		}

		public BigDecimal getR40_bob_total_amount() {
			return r40_bob_total_amount;
		}

		public void setR40_bob_total_amount(BigDecimal r40_bob_total_amount) {
			this.r40_bob_total_amount = r40_bob_total_amount;
		}

		public BigDecimal getR40_bob_with_factor_applied() {
			return r40_bob_with_factor_applied;
		}

		public void setR40_bob_with_factor_applied(BigDecimal r40_bob_with_factor_applied) {
			this.r40_bob_with_factor_applied = r40_bob_with_factor_applied;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_amount_factor() {
			return r41_amount_factor;
		}

		public void setR41_amount_factor(BigDecimal r41_amount_factor) {
			this.r41_amount_factor = r41_amount_factor;
		}

		public BigDecimal getR41_bob_total_amount() {
			return r41_bob_total_amount;
		}

		public void setR41_bob_total_amount(BigDecimal r41_bob_total_amount) {
			this.r41_bob_total_amount = r41_bob_total_amount;
		}

		public BigDecimal getR41_bob_with_factor_applied() {
			return r41_bob_with_factor_applied;
		}

		public void setR41_bob_with_factor_applied(BigDecimal r41_bob_with_factor_applied) {
			this.r41_bob_with_factor_applied = r41_bob_with_factor_applied;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_amount_factor() {
			return r42_amount_factor;
		}

		public void setR42_amount_factor(BigDecimal r42_amount_factor) {
			this.r42_amount_factor = r42_amount_factor;
		}

		public BigDecimal getR42_bob_total_amount() {
			return r42_bob_total_amount;
		}

		public void setR42_bob_total_amount(BigDecimal r42_bob_total_amount) {
			this.r42_bob_total_amount = r42_bob_total_amount;
		}

		public BigDecimal getR42_bob_with_factor_applied() {
			return r42_bob_with_factor_applied;
		}

		public void setR42_bob_with_factor_applied(BigDecimal r42_bob_with_factor_applied) {
			this.r42_bob_with_factor_applied = r42_bob_with_factor_applied;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_amount_factor() {
			return r43_amount_factor;
		}

		public void setR43_amount_factor(BigDecimal r43_amount_factor) {
			this.r43_amount_factor = r43_amount_factor;
		}

		public BigDecimal getR43_bob_total_amount() {
			return r43_bob_total_amount;
		}

		public void setR43_bob_total_amount(BigDecimal r43_bob_total_amount) {
			this.r43_bob_total_amount = r43_bob_total_amount;
		}

		public BigDecimal getR43_bob_with_factor_applied() {
			return r43_bob_with_factor_applied;
		}

		public void setR43_bob_with_factor_applied(BigDecimal r43_bob_with_factor_applied) {
			this.r43_bob_with_factor_applied = r43_bob_with_factor_applied;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_amount_factor() {
			return r44_amount_factor;
		}

		public void setR44_amount_factor(BigDecimal r44_amount_factor) {
			this.r44_amount_factor = r44_amount_factor;
		}

		public BigDecimal getR44_bob_total_amount() {
			return r44_bob_total_amount;
		}

		public void setR44_bob_total_amount(BigDecimal r44_bob_total_amount) {
			this.r44_bob_total_amount = r44_bob_total_amount;
		}

		public BigDecimal getR44_bob_with_factor_applied() {
			return r44_bob_with_factor_applied;
		}

		public void setR44_bob_with_factor_applied(BigDecimal r44_bob_with_factor_applied) {
			this.r44_bob_with_factor_applied = r44_bob_with_factor_applied;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_amount_factor() {
			return r45_amount_factor;
		}

		public void setR45_amount_factor(BigDecimal r45_amount_factor) {
			this.r45_amount_factor = r45_amount_factor;
		}

		public BigDecimal getR45_bob_total_amount() {
			return r45_bob_total_amount;
		}

		public void setR45_bob_total_amount(BigDecimal r45_bob_total_amount) {
			this.r45_bob_total_amount = r45_bob_total_amount;
		}

		public BigDecimal getR45_bob_with_factor_applied() {
			return r45_bob_with_factor_applied;
		}

		public void setR45_bob_with_factor_applied(BigDecimal r45_bob_with_factor_applied) {
			this.r45_bob_with_factor_applied = r45_bob_with_factor_applied;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_amount_factor() {
			return r46_amount_factor;
		}

		public void setR46_amount_factor(BigDecimal r46_amount_factor) {
			this.r46_amount_factor = r46_amount_factor;
		}

		public BigDecimal getR46_bob_total_amount() {
			return r46_bob_total_amount;
		}

		public void setR46_bob_total_amount(BigDecimal r46_bob_total_amount) {
			this.r46_bob_total_amount = r46_bob_total_amount;
		}

		public BigDecimal getR46_bob_with_factor_applied() {
			return r46_bob_with_factor_applied;
		}

		public void setR46_bob_with_factor_applied(BigDecimal r46_bob_with_factor_applied) {
			this.r46_bob_with_factor_applied = r46_bob_with_factor_applied;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_amount_factor() {
			return r47_amount_factor;
		}

		public void setR47_amount_factor(BigDecimal r47_amount_factor) {
			this.r47_amount_factor = r47_amount_factor;
		}

		public BigDecimal getR47_bob_total_amount() {
			return r47_bob_total_amount;
		}

		public void setR47_bob_total_amount(BigDecimal r47_bob_total_amount) {
			this.r47_bob_total_amount = r47_bob_total_amount;
		}

		public BigDecimal getR47_bob_with_factor_applied() {
			return r47_bob_with_factor_applied;
		}

		public void setR47_bob_with_factor_applied(BigDecimal r47_bob_with_factor_applied) {
			this.r47_bob_with_factor_applied = r47_bob_with_factor_applied;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_amount_factor() {
			return r48_amount_factor;
		}

		public void setR48_amount_factor(BigDecimal r48_amount_factor) {
			this.r48_amount_factor = r48_amount_factor;
		}

		public BigDecimal getR48_bob_total_amount() {
			return r48_bob_total_amount;
		}

		public void setR48_bob_total_amount(BigDecimal r48_bob_total_amount) {
			this.r48_bob_total_amount = r48_bob_total_amount;
		}

		public BigDecimal getR48_bob_with_factor_applied() {
			return r48_bob_with_factor_applied;
		}

		public void setR48_bob_with_factor_applied(BigDecimal r48_bob_with_factor_applied) {
			this.r48_bob_with_factor_applied = r48_bob_with_factor_applied;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_amount_factor() {
			return r49_amount_factor;
		}

		public void setR49_amount_factor(BigDecimal r49_amount_factor) {
			this.r49_amount_factor = r49_amount_factor;
		}

		public BigDecimal getR49_bob_total_amount() {
			return r49_bob_total_amount;
		}

		public void setR49_bob_total_amount(BigDecimal r49_bob_total_amount) {
			this.r49_bob_total_amount = r49_bob_total_amount;
		}

		public BigDecimal getR49_bob_with_factor_applied() {
			return r49_bob_with_factor_applied;
		}

		public void setR49_bob_with_factor_applied(BigDecimal r49_bob_with_factor_applied) {
			this.r49_bob_with_factor_applied = r49_bob_with_factor_applied;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_amount_factor() {
			return r50_amount_factor;
		}

		public void setR50_amount_factor(BigDecimal r50_amount_factor) {
			this.r50_amount_factor = r50_amount_factor;
		}

		public BigDecimal getR50_bob_total_amount() {
			return r50_bob_total_amount;
		}

		public void setR50_bob_total_amount(BigDecimal r50_bob_total_amount) {
			this.r50_bob_total_amount = r50_bob_total_amount;
		}

		public BigDecimal getR50_bob_with_factor_applied() {
			return r50_bob_with_factor_applied;
		}

		public void setR50_bob_with_factor_applied(BigDecimal r50_bob_with_factor_applied) {
			this.r50_bob_with_factor_applied = r50_bob_with_factor_applied;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_amount_factor() {
			return r51_amount_factor;
		}

		public void setR51_amount_factor(BigDecimal r51_amount_factor) {
			this.r51_amount_factor = r51_amount_factor;
		}

		public BigDecimal getR51_bob_total_amount() {
			return r51_bob_total_amount;
		}

		public void setR51_bob_total_amount(BigDecimal r51_bob_total_amount) {
			this.r51_bob_total_amount = r51_bob_total_amount;
		}

		public BigDecimal getR51_bob_with_factor_applied() {
			return r51_bob_with_factor_applied;
		}

		public void setR51_bob_with_factor_applied(BigDecimal r51_bob_with_factor_applied) {
			this.r51_bob_with_factor_applied = r51_bob_with_factor_applied;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_amount_factor() {
			return r52_amount_factor;
		}

		public void setR52_amount_factor(BigDecimal r52_amount_factor) {
			this.r52_amount_factor = r52_amount_factor;
		}

		public BigDecimal getR52_bob_total_amount() {
			return r52_bob_total_amount;
		}

		public void setR52_bob_total_amount(BigDecimal r52_bob_total_amount) {
			this.r52_bob_total_amount = r52_bob_total_amount;
		}

		public BigDecimal getR52_bob_with_factor_applied() {
			return r52_bob_with_factor_applied;
		}

		public void setR52_bob_with_factor_applied(BigDecimal r52_bob_with_factor_applied) {
			this.r52_bob_with_factor_applied = r52_bob_with_factor_applied;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_amount_factor() {
			return r53_amount_factor;
		}

		public void setR53_amount_factor(BigDecimal r53_amount_factor) {
			this.r53_amount_factor = r53_amount_factor;
		}

		public BigDecimal getR53_bob_total_amount() {
			return r53_bob_total_amount;
		}

		public void setR53_bob_total_amount(BigDecimal r53_bob_total_amount) {
			this.r53_bob_total_amount = r53_bob_total_amount;
		}

		public BigDecimal getR53_bob_with_factor_applied() {
			return r53_bob_with_factor_applied;
		}

		public void setR53_bob_with_factor_applied(BigDecimal r53_bob_with_factor_applied) {
			this.r53_bob_with_factor_applied = r53_bob_with_factor_applied;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_amount_factor() {
			return r54_amount_factor;
		}

		public void setR54_amount_factor(BigDecimal r54_amount_factor) {
			this.r54_amount_factor = r54_amount_factor;
		}

		public BigDecimal getR54_bob_total_amount() {
			return r54_bob_total_amount;
		}

		public void setR54_bob_total_amount(BigDecimal r54_bob_total_amount) {
			this.r54_bob_total_amount = r54_bob_total_amount;
		}

		public BigDecimal getR54_bob_with_factor_applied() {
			return r54_bob_with_factor_applied;
		}

		public void setR54_bob_with_factor_applied(BigDecimal r54_bob_with_factor_applied) {
			this.r54_bob_with_factor_applied = r54_bob_with_factor_applied;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_amount_factor() {
			return r55_amount_factor;
		}

		public void setR55_amount_factor(BigDecimal r55_amount_factor) {
			this.r55_amount_factor = r55_amount_factor;
		}

		public BigDecimal getR55_bob_total_amount() {
			return r55_bob_total_amount;
		}

		public void setR55_bob_total_amount(BigDecimal r55_bob_total_amount) {
			this.r55_bob_total_amount = r55_bob_total_amount;
		}

		public BigDecimal getR55_bob_with_factor_applied() {
			return r55_bob_with_factor_applied;
		}

		public void setR55_bob_with_factor_applied(BigDecimal r55_bob_with_factor_applied) {
			this.r55_bob_with_factor_applied = r55_bob_with_factor_applied;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_amount_factor() {
			return r56_amount_factor;
		}

		public void setR56_amount_factor(BigDecimal r56_amount_factor) {
			this.r56_amount_factor = r56_amount_factor;
		}

		public BigDecimal getR56_bob_total_amount() {
			return r56_bob_total_amount;
		}

		public void setR56_bob_total_amount(BigDecimal r56_bob_total_amount) {
			this.r56_bob_total_amount = r56_bob_total_amount;
		}

		public BigDecimal getR56_bob_with_factor_applied() {
			return r56_bob_with_factor_applied;
		}

		public void setR56_bob_with_factor_applied(BigDecimal r56_bob_with_factor_applied) {
			this.r56_bob_with_factor_applied = r56_bob_with_factor_applied;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_amount_factor() {
			return r57_amount_factor;
		}

		public void setR57_amount_factor(BigDecimal r57_amount_factor) {
			this.r57_amount_factor = r57_amount_factor;
		}

		public BigDecimal getR57_bob_total_amount() {
			return r57_bob_total_amount;
		}

		public void setR57_bob_total_amount(BigDecimal r57_bob_total_amount) {
			this.r57_bob_total_amount = r57_bob_total_amount;
		}

		public BigDecimal getR57_bob_with_factor_applied() {
			return r57_bob_with_factor_applied;
		}

		public void setR57_bob_with_factor_applied(BigDecimal r57_bob_with_factor_applied) {
			this.r57_bob_with_factor_applied = r57_bob_with_factor_applied;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public BigDecimal getR58_amount_factor() {
			return r58_amount_factor;
		}

		public void setR58_amount_factor(BigDecimal r58_amount_factor) {
			this.r58_amount_factor = r58_amount_factor;
		}

		public BigDecimal getR58_bob_total_amount() {
			return r58_bob_total_amount;
		}

		public void setR58_bob_total_amount(BigDecimal r58_bob_total_amount) {
			this.r58_bob_total_amount = r58_bob_total_amount;
		}

		public BigDecimal getR58_bob_with_factor_applied() {
			return r58_bob_with_factor_applied;
		}

		public void setR58_bob_with_factor_applied(BigDecimal r58_bob_with_factor_applied) {
			this.r58_bob_with_factor_applied = r58_bob_with_factor_applied;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public BigDecimal getR59_amount_factor() {
			return r59_amount_factor;
		}

		public void setR59_amount_factor(BigDecimal r59_amount_factor) {
			this.r59_amount_factor = r59_amount_factor;
		}

		public BigDecimal getR59_bob_total_amount() {
			return r59_bob_total_amount;
		}

		public void setR59_bob_total_amount(BigDecimal r59_bob_total_amount) {
			this.r59_bob_total_amount = r59_bob_total_amount;
		}

		public BigDecimal getR59_bob_with_factor_applied() {
			return r59_bob_with_factor_applied;
		}

		public void setR59_bob_with_factor_applied(BigDecimal r59_bob_with_factor_applied) {
			this.r59_bob_with_factor_applied = r59_bob_with_factor_applied;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public BigDecimal getR60_amount_factor() {
			return r60_amount_factor;
		}

		public void setR60_amount_factor(BigDecimal r60_amount_factor) {
			this.r60_amount_factor = r60_amount_factor;
		}

		public BigDecimal getR60_bob_total_amount() {
			return r60_bob_total_amount;
		}

		public void setR60_bob_total_amount(BigDecimal r60_bob_total_amount) {
			this.r60_bob_total_amount = r60_bob_total_amount;
		}

		public BigDecimal getR60_bob_with_factor_applied() {
			return r60_bob_with_factor_applied;
		}

		public void setR60_bob_with_factor_applied(BigDecimal r60_bob_with_factor_applied) {
			this.r60_bob_with_factor_applied = r60_bob_with_factor_applied;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public BigDecimal getR61_amount_factor() {
			return r61_amount_factor;
		}

		public void setR61_amount_factor(BigDecimal r61_amount_factor) {
			this.r61_amount_factor = r61_amount_factor;
		}

		public BigDecimal getR61_bob_total_amount() {
			return r61_bob_total_amount;
		}

		public void setR61_bob_total_amount(BigDecimal r61_bob_total_amount) {
			this.r61_bob_total_amount = r61_bob_total_amount;
		}

		public BigDecimal getR61_bob_with_factor_applied() {
			return r61_bob_with_factor_applied;
		}

		public void setR61_bob_with_factor_applied(BigDecimal r61_bob_with_factor_applied) {
			this.r61_bob_with_factor_applied = r61_bob_with_factor_applied;
		}

		public String getR62_product() {
			return r62_product;
		}

		public void setR62_product(String r62_product) {
			this.r62_product = r62_product;
		}

		public BigDecimal getR62_amount_factor() {
			return r62_amount_factor;
		}

		public void setR62_amount_factor(BigDecimal r62_amount_factor) {
			this.r62_amount_factor = r62_amount_factor;
		}

		public BigDecimal getR62_bob_total_amount() {
			return r62_bob_total_amount;
		}

		public void setR62_bob_total_amount(BigDecimal r62_bob_total_amount) {
			this.r62_bob_total_amount = r62_bob_total_amount;
		}

		public BigDecimal getR62_bob_with_factor_applied() {
			return r62_bob_with_factor_applied;
		}

		public void setR62_bob_with_factor_applied(BigDecimal r62_bob_with_factor_applied) {
			this.r62_bob_with_factor_applied = r62_bob_with_factor_applied;
		}

		public String getR63_product() {
			return r63_product;
		}

		public void setR63_product(String r63_product) {
			this.r63_product = r63_product;
		}

		public BigDecimal getR63_amount_factor() {
			return r63_amount_factor;
		}

		public void setR63_amount_factor(BigDecimal r63_amount_factor) {
			this.r63_amount_factor = r63_amount_factor;
		}

		public BigDecimal getR63_bob_total_amount() {
			return r63_bob_total_amount;
		}

		public void setR63_bob_total_amount(BigDecimal r63_bob_total_amount) {
			this.r63_bob_total_amount = r63_bob_total_amount;
		}

		public BigDecimal getR63_bob_with_factor_applied() {
			return r63_bob_with_factor_applied;
		}

		public void setR63_bob_with_factor_applied(BigDecimal r63_bob_with_factor_applied) {
			this.r63_bob_with_factor_applied = r63_bob_with_factor_applied;
		}

		public String getR64_product() {
			return r64_product;
		}

		public void setR64_product(String r64_product) {
			this.r64_product = r64_product;
		}

		public BigDecimal getR64_amount_factor() {
			return r64_amount_factor;
		}

		public void setR64_amount_factor(BigDecimal r64_amount_factor) {
			this.r64_amount_factor = r64_amount_factor;
		}

		public BigDecimal getR64_bob_total_amount() {
			return r64_bob_total_amount;
		}

		public void setR64_bob_total_amount(BigDecimal r64_bob_total_amount) {
			this.r64_bob_total_amount = r64_bob_total_amount;
		}

		public BigDecimal getR64_bob_with_factor_applied() {
			return r64_bob_with_factor_applied;
		}

		public void setR64_bob_with_factor_applied(BigDecimal r64_bob_with_factor_applied) {
			this.r64_bob_with_factor_applied = r64_bob_with_factor_applied;
		}

		public String getR65_product() {
			return r65_product;
		}

		public void setR65_product(String r65_product) {
			this.r65_product = r65_product;
		}

		public BigDecimal getR65_amount_factor() {
			return r65_amount_factor;
		}

		public void setR65_amount_factor(BigDecimal r65_amount_factor) {
			this.r65_amount_factor = r65_amount_factor;
		}

		public BigDecimal getR65_bob_total_amount() {
			return r65_bob_total_amount;
		}

		public void setR65_bob_total_amount(BigDecimal r65_bob_total_amount) {
			this.r65_bob_total_amount = r65_bob_total_amount;
		}

		public BigDecimal getR65_bob_with_factor_applied() {
			return r65_bob_with_factor_applied;
		}

		public void setR65_bob_with_factor_applied(BigDecimal r65_bob_with_factor_applied) {
			this.r65_bob_with_factor_applied = r65_bob_with_factor_applied;
		}

		public String getR66_product() {
			return r66_product;
		}

		public void setR66_product(String r66_product) {
			this.r66_product = r66_product;
		}

		public BigDecimal getR66_amount_factor() {
			return r66_amount_factor;
		}

		public void setR66_amount_factor(BigDecimal r66_amount_factor) {
			this.r66_amount_factor = r66_amount_factor;
		}

		public BigDecimal getR66_bob_total_amount() {
			return r66_bob_total_amount;
		}

		public void setR66_bob_total_amount(BigDecimal r66_bob_total_amount) {
			this.r66_bob_total_amount = r66_bob_total_amount;
		}

		public BigDecimal getR66_bob_with_factor_applied() {
			return r66_bob_with_factor_applied;
		}

		public void setR66_bob_with_factor_applied(BigDecimal r66_bob_with_factor_applied) {
			this.r66_bob_with_factor_applied = r66_bob_with_factor_applied;
		}

		public String getR67_product() {
			return r67_product;
		}

		public void setR67_product(String r67_product) {
			this.r67_product = r67_product;
		}

		public BigDecimal getR67_amount_factor() {
			return r67_amount_factor;
		}

		public void setR67_amount_factor(BigDecimal r67_amount_factor) {
			this.r67_amount_factor = r67_amount_factor;
		}

		public BigDecimal getR67_bob_total_amount() {
			return r67_bob_total_amount;
		}

		public void setR67_bob_total_amount(BigDecimal r67_bob_total_amount) {
			this.r67_bob_total_amount = r67_bob_total_amount;
		}

		public BigDecimal getR67_bob_with_factor_applied() {
			return r67_bob_with_factor_applied;
		}

		public void setR67_bob_with_factor_applied(BigDecimal r67_bob_with_factor_applied) {
			this.r67_bob_with_factor_applied = r67_bob_with_factor_applied;
		}

		public String getR68_product() {
			return r68_product;
		}

		public void setR68_product(String r68_product) {
			this.r68_product = r68_product;
		}

		public BigDecimal getR68_amount_factor() {
			return r68_amount_factor;
		}

		public void setR68_amount_factor(BigDecimal r68_amount_factor) {
			this.r68_amount_factor = r68_amount_factor;
		}

		public BigDecimal getR68_bob_total_amount() {
			return r68_bob_total_amount;
		}

		public void setR68_bob_total_amount(BigDecimal r68_bob_total_amount) {
			this.r68_bob_total_amount = r68_bob_total_amount;
		}

		public BigDecimal getR68_bob_with_factor_applied() {
			return r68_bob_with_factor_applied;
		}

		public void setR68_bob_with_factor_applied(BigDecimal r68_bob_with_factor_applied) {
			this.r68_bob_with_factor_applied = r68_bob_with_factor_applied;
		}

		public String getR69_product() {
			return r69_product;
		}

		public void setR69_product(String r69_product) {
			this.r69_product = r69_product;
		}

		public BigDecimal getR69_amount_factor() {
			return r69_amount_factor;
		}

		public void setR69_amount_factor(BigDecimal r69_amount_factor) {
			this.r69_amount_factor = r69_amount_factor;
		}

		public BigDecimal getR69_bob_total_amount() {
			return r69_bob_total_amount;
		}

		public void setR69_bob_total_amount(BigDecimal r69_bob_total_amount) {
			this.r69_bob_total_amount = r69_bob_total_amount;
		}

		public BigDecimal getR69_bob_with_factor_applied() {
			return r69_bob_with_factor_applied;
		}

		public void setR69_bob_with_factor_applied(BigDecimal r69_bob_with_factor_applied) {
			this.r69_bob_with_factor_applied = r69_bob_with_factor_applied;
		}

		public String getR70_product() {
			return r70_product;
		}

		public void setR70_product(String r70_product) {
			this.r70_product = r70_product;
		}

		public BigDecimal getR70_amount_factor() {
			return r70_amount_factor;
		}

		public void setR70_amount_factor(BigDecimal r70_amount_factor) {
			this.r70_amount_factor = r70_amount_factor;
		}

		public BigDecimal getR70_bob_total_amount() {
			return r70_bob_total_amount;
		}

		public void setR70_bob_total_amount(BigDecimal r70_bob_total_amount) {
			this.r70_bob_total_amount = r70_bob_total_amount;
		}

		public BigDecimal getR70_bob_with_factor_applied() {
			return r70_bob_with_factor_applied;
		}

		public void setR70_bob_with_factor_applied(BigDecimal r70_bob_with_factor_applied) {
			this.r70_bob_with_factor_applied = r70_bob_with_factor_applied;
		}

		public String getR72_product() {
			return r72_product;
		}

		public void setR72_product(String r72_product) {
			this.r72_product = r72_product;
		}

		public BigDecimal getR72_amount_factor() {
			return r72_amount_factor;
		}

		public void setR72_amount_factor(BigDecimal r72_amount_factor) {
			this.r72_amount_factor = r72_amount_factor;
		}

		public BigDecimal getR72_bob_total_amount() {
			return r72_bob_total_amount;
		}

		public void setR72_bob_total_amount(BigDecimal r72_bob_total_amount) {
			this.r72_bob_total_amount = r72_bob_total_amount;
		}

		public BigDecimal getR72_bob_with_factor_applied() {
			return r72_bob_with_factor_applied;
		}

		public void setR72_bob_with_factor_applied(BigDecimal r72_bob_with_factor_applied) {
			this.r72_bob_with_factor_applied = r72_bob_with_factor_applied;
		}

		public String getR73_product() {
			return r73_product;
		}

		public void setR73_product(String r73_product) {
			this.r73_product = r73_product;
		}

		public BigDecimal getR73_amount_factor() {
			return r73_amount_factor;
		}

		public void setR73_amount_factor(BigDecimal r73_amount_factor) {
			this.r73_amount_factor = r73_amount_factor;
		}

		public BigDecimal getR73_bob_total_amount() {
			return r73_bob_total_amount;
		}

		public void setR73_bob_total_amount(BigDecimal r73_bob_total_amount) {
			this.r73_bob_total_amount = r73_bob_total_amount;
		}

		public BigDecimal getR73_bob_with_factor_applied() {
			return r73_bob_with_factor_applied;
		}

		public void setR73_bob_with_factor_applied(BigDecimal r73_bob_with_factor_applied) {
			this.r73_bob_with_factor_applied = r73_bob_with_factor_applied;
		}

		public String getR74_product() {
			return r74_product;
		}

		public void setR74_product(String r74_product) {
			this.r74_product = r74_product;
		}

		public BigDecimal getR74_amount_factor() {
			return r74_amount_factor;
		}

		public void setR74_amount_factor(BigDecimal r74_amount_factor) {
			this.r74_amount_factor = r74_amount_factor;
		}

		public BigDecimal getR74_bob_total_amount() {
			return r74_bob_total_amount;
		}

		public void setR74_bob_total_amount(BigDecimal r74_bob_total_amount) {
			this.r74_bob_total_amount = r74_bob_total_amount;
		}

		public BigDecimal getR74_bob_with_factor_applied() {
			return r74_bob_with_factor_applied;
		}

		public void setR74_bob_with_factor_applied(BigDecimal r74_bob_with_factor_applied) {
			this.r74_bob_with_factor_applied = r74_bob_with_factor_applied;
		}

		public String getR75_product() {
			return r75_product;
		}

		public void setR75_product(String r75_product) {
			this.r75_product = r75_product;
		}

		public BigDecimal getR75_amount_factor() {
			return r75_amount_factor;
		}

		public void setR75_amount_factor(BigDecimal r75_amount_factor) {
			this.r75_amount_factor = r75_amount_factor;
		}

		public BigDecimal getR75_bob_total_amount() {
			return r75_bob_total_amount;
		}

		public void setR75_bob_total_amount(BigDecimal r75_bob_total_amount) {
			this.r75_bob_total_amount = r75_bob_total_amount;
		}

		public BigDecimal getR75_bob_with_factor_applied() {
			return r75_bob_with_factor_applied;
		}

		public void setR75_bob_with_factor_applied(BigDecimal r75_bob_with_factor_applied) {
			this.r75_bob_with_factor_applied = r75_bob_with_factor_applied;
		}

		public String getR76_product() {
			return r76_product;
		}

		public void setR76_product(String r76_product) {
			this.r76_product = r76_product;
		}

		public BigDecimal getR76_amount_factor() {
			return r76_amount_factor;
		}

		public void setR76_amount_factor(BigDecimal r76_amount_factor) {
			this.r76_amount_factor = r76_amount_factor;
		}

		public BigDecimal getR76_bob_total_amount() {
			return r76_bob_total_amount;
		}

		public void setR76_bob_total_amount(BigDecimal r76_bob_total_amount) {
			this.r76_bob_total_amount = r76_bob_total_amount;
		}

		public BigDecimal getR76_bob_with_factor_applied() {
			return r76_bob_with_factor_applied;
		}

		public void setR76_bob_with_factor_applied(BigDecimal r76_bob_with_factor_applied) {
			this.r76_bob_with_factor_applied = r76_bob_with_factor_applied;
		}

		public String getR77_product() {
			return r77_product;
		}

		public void setR77_product(String r77_product) {
			this.r77_product = r77_product;
		}

		public BigDecimal getR77_amount_factor() {
			return r77_amount_factor;
		}

		public void setR77_amount_factor(BigDecimal r77_amount_factor) {
			this.r77_amount_factor = r77_amount_factor;
		}

		public BigDecimal getR77_bob_total_amount() {
			return r77_bob_total_amount;
		}

		public void setR77_bob_total_amount(BigDecimal r77_bob_total_amount) {
			this.r77_bob_total_amount = r77_bob_total_amount;
		}

		public BigDecimal getR77_bob_with_factor_applied() {
			return r77_bob_with_factor_applied;
		}

		public void setR77_bob_with_factor_applied(BigDecimal r77_bob_with_factor_applied) {
			this.r77_bob_with_factor_applied = r77_bob_with_factor_applied;
		}

		public String getR78_product() {
			return r78_product;
		}

		public void setR78_product(String r78_product) {
			this.r78_product = r78_product;
		}

		public BigDecimal getR78_amount_factor() {
			return r78_amount_factor;
		}

		public void setR78_amount_factor(BigDecimal r78_amount_factor) {
			this.r78_amount_factor = r78_amount_factor;
		}

		public BigDecimal getR78_bob_total_amount() {
			return r78_bob_total_amount;
		}

		public void setR78_bob_total_amount(BigDecimal r78_bob_total_amount) {
			this.r78_bob_total_amount = r78_bob_total_amount;
		}

		public BigDecimal getR78_bob_with_factor_applied() {
			return r78_bob_with_factor_applied;
		}

		public void setR78_bob_with_factor_applied(BigDecimal r78_bob_with_factor_applied) {
			this.r78_bob_with_factor_applied = r78_bob_with_factor_applied;
		}

		public String getR79_product() {
			return r79_product;
		}

		public void setR79_product(String r79_product) {
			this.r79_product = r79_product;
		}

		public BigDecimal getR79_amount_factor() {
			return r79_amount_factor;
		}

		public void setR79_amount_factor(BigDecimal r79_amount_factor) {
			this.r79_amount_factor = r79_amount_factor;
		}

		public BigDecimal getR79_bob_total_amount() {
			return r79_bob_total_amount;
		}

		public void setR79_bob_total_amount(BigDecimal r79_bob_total_amount) {
			this.r79_bob_total_amount = r79_bob_total_amount;
		}

		public BigDecimal getR79_bob_with_factor_applied() {
			return r79_bob_with_factor_applied;
		}

		public void setR79_bob_with_factor_applied(BigDecimal r79_bob_with_factor_applied) {
			this.r79_bob_with_factor_applied = r79_bob_with_factor_applied;
		}

		public String getR80_product() {
			return r80_product;
		}

		public void setR80_product(String r80_product) {
			this.r80_product = r80_product;
		}

		public BigDecimal getR80_amount_factor() {
			return r80_amount_factor;
		}

		public void setR80_amount_factor(BigDecimal r80_amount_factor) {
			this.r80_amount_factor = r80_amount_factor;
		}

		public BigDecimal getR80_bob_total_amount() {
			return r80_bob_total_amount;
		}

		public void setR80_bob_total_amount(BigDecimal r80_bob_total_amount) {
			this.r80_bob_total_amount = r80_bob_total_amount;
		}

		public BigDecimal getR80_bob_with_factor_applied() {
			return r80_bob_with_factor_applied;
		}

		public void setR80_bob_with_factor_applied(BigDecimal r80_bob_with_factor_applied) {
			this.r80_bob_with_factor_applied = r80_bob_with_factor_applied;
		}

		public String getR81_product() {
			return r81_product;
		}

		public void setR81_product(String r81_product) {
			this.r81_product = r81_product;
		}

		public BigDecimal getR81_amount_factor() {
			return r81_amount_factor;
		}

		public void setR81_amount_factor(BigDecimal r81_amount_factor) {
			this.r81_amount_factor = r81_amount_factor;
		}

		public BigDecimal getR81_bob_total_amount() {
			return r81_bob_total_amount;
		}

		public void setR81_bob_total_amount(BigDecimal r81_bob_total_amount) {
			this.r81_bob_total_amount = r81_bob_total_amount;
		}

		public BigDecimal getR81_bob_with_factor_applied() {
			return r81_bob_with_factor_applied;
		}

		public void setR81_bob_with_factor_applied(BigDecimal r81_bob_with_factor_applied) {
			this.r81_bob_with_factor_applied = r81_bob_with_factor_applied;
		}

		public String getR82_product() {
			return r82_product;
		}

		public void setR82_product(String r82_product) {
			this.r82_product = r82_product;
		}

		public BigDecimal getR82_amount_factor() {
			return r82_amount_factor;
		}

		public void setR82_amount_factor(BigDecimal r82_amount_factor) {
			this.r82_amount_factor = r82_amount_factor;
		}

		public BigDecimal getR82_bob_total_amount() {
			return r82_bob_total_amount;
		}

		public void setR82_bob_total_amount(BigDecimal r82_bob_total_amount) {
			this.r82_bob_total_amount = r82_bob_total_amount;
		}

		public BigDecimal getR82_bob_with_factor_applied() {
			return r82_bob_with_factor_applied;
		}

		public void setR82_bob_with_factor_applied(BigDecimal r82_bob_with_factor_applied) {
			this.r82_bob_with_factor_applied = r82_bob_with_factor_applied;
		}

		public String getR83_product() {
			return r83_product;
		}

		public void setR83_product(String r83_product) {
			this.r83_product = r83_product;
		}

		public BigDecimal getR83_amount_factor() {
			return r83_amount_factor;
		}

		public void setR83_amount_factor(BigDecimal r83_amount_factor) {
			this.r83_amount_factor = r83_amount_factor;
		}

		public BigDecimal getR83_bob_total_amount() {
			return r83_bob_total_amount;
		}

		public void setR83_bob_total_amount(BigDecimal r83_bob_total_amount) {
			this.r83_bob_total_amount = r83_bob_total_amount;
		}

		public BigDecimal getR83_bob_with_factor_applied() {
			return r83_bob_with_factor_applied;
		}

		public void setR83_bob_with_factor_applied(BigDecimal r83_bob_with_factor_applied) {
			this.r83_bob_with_factor_applied = r83_bob_with_factor_applied;
		}

		public String getR84_product() {
			return r84_product;
		}

		public void setR84_product(String r84_product) {
			this.r84_product = r84_product;
		}

		public BigDecimal getR84_amount_factor() {
			return r84_amount_factor;
		}

		public void setR84_amount_factor(BigDecimal r84_amount_factor) {
			this.r84_amount_factor = r84_amount_factor;
		}

		public BigDecimal getR84_bob_total_amount() {
			return r84_bob_total_amount;
		}

		public void setR84_bob_total_amount(BigDecimal r84_bob_total_amount) {
			this.r84_bob_total_amount = r84_bob_total_amount;
		}

		public BigDecimal getR84_bob_with_factor_applied() {
			return r84_bob_with_factor_applied;
		}

		public void setR84_bob_with_factor_applied(BigDecimal r84_bob_with_factor_applied) {
			this.r84_bob_with_factor_applied = r84_bob_with_factor_applied;
		}

		public String getR85_product() {
			return r85_product;
		}

		public void setR85_product(String r85_product) {
			this.r85_product = r85_product;
		}

		public BigDecimal getR85_amount_factor() {
			return r85_amount_factor;
		}

		public void setR85_amount_factor(BigDecimal r85_amount_factor) {
			this.r85_amount_factor = r85_amount_factor;
		}

		public BigDecimal getR85_bob_total_amount() {
			return r85_bob_total_amount;
		}

		public void setR85_bob_total_amount(BigDecimal r85_bob_total_amount) {
			this.r85_bob_total_amount = r85_bob_total_amount;
		}

		public BigDecimal getR85_bob_with_factor_applied() {
			return r85_bob_with_factor_applied;
		}

		public void setR85_bob_with_factor_applied(BigDecimal r85_bob_with_factor_applied) {
			this.r85_bob_with_factor_applied = r85_bob_with_factor_applied;
		}

		public String getR86_product() {
			return r86_product;
		}

		public void setR86_product(String r86_product) {
			this.r86_product = r86_product;
		}

		public BigDecimal getR86_amount_factor() {
			return r86_amount_factor;
		}

		public void setR86_amount_factor(BigDecimal r86_amount_factor) {
			this.r86_amount_factor = r86_amount_factor;
		}

		public BigDecimal getR86_bob_total_amount() {
			return r86_bob_total_amount;
		}

		public void setR86_bob_total_amount(BigDecimal r86_bob_total_amount) {
			this.r86_bob_total_amount = r86_bob_total_amount;
		}

		public BigDecimal getR86_bob_with_factor_applied() {
			return r86_bob_with_factor_applied;
		}

		public void setR86_bob_with_factor_applied(BigDecimal r86_bob_with_factor_applied) {
			this.r86_bob_with_factor_applied = r86_bob_with_factor_applied;
		}

		public String getR87_product() {
			return r87_product;
		}

		public void setR87_product(String r87_product) {
			this.r87_product = r87_product;
		}

		public BigDecimal getR87_amount_factor() {
			return r87_amount_factor;
		}

		public void setR87_amount_factor(BigDecimal r87_amount_factor) {
			this.r87_amount_factor = r87_amount_factor;
		}

		public BigDecimal getR87_bob_total_amount() {
			return r87_bob_total_amount;
		}

		public void setR87_bob_total_amount(BigDecimal r87_bob_total_amount) {
			this.r87_bob_total_amount = r87_bob_total_amount;
		}

		public BigDecimal getR87_bob_with_factor_applied() {
			return r87_bob_with_factor_applied;
		}

		public void setR87_bob_with_factor_applied(BigDecimal r87_bob_with_factor_applied) {
			this.r87_bob_with_factor_applied = r87_bob_with_factor_applied;
		}

		public String getR88_product() {
			return r88_product;
		}

		public void setR88_product(String r88_product) {
			this.r88_product = r88_product;
		}

		public BigDecimal getR88_amount_factor() {
			return r88_amount_factor;
		}

		public void setR88_amount_factor(BigDecimal r88_amount_factor) {
			this.r88_amount_factor = r88_amount_factor;
		}

		public BigDecimal getR88_bob_total_amount() {
			return r88_bob_total_amount;
		}

		public void setR88_bob_total_amount(BigDecimal r88_bob_total_amount) {
			this.r88_bob_total_amount = r88_bob_total_amount;
		}

		public BigDecimal getR88_bob_with_factor_applied() {
			return r88_bob_with_factor_applied;
		}

		public void setR88_bob_with_factor_applied(BigDecimal r88_bob_with_factor_applied) {
			this.r88_bob_with_factor_applied = r88_bob_with_factor_applied;
		}

		public String getR89_product() {
			return r89_product;
		}

		public void setR89_product(String r89_product) {
			this.r89_product = r89_product;
		}

		public BigDecimal getR89_amount_factor() {
			return r89_amount_factor;
		}

		public void setR89_amount_factor(BigDecimal r89_amount_factor) {
			this.r89_amount_factor = r89_amount_factor;
		}

		public BigDecimal getR89_bob_total_amount() {
			return r89_bob_total_amount;
		}

		public void setR89_bob_total_amount(BigDecimal r89_bob_total_amount) {
			this.r89_bob_total_amount = r89_bob_total_amount;
		}

		public BigDecimal getR89_bob_with_factor_applied() {
			return r89_bob_with_factor_applied;
		}

		public void setR89_bob_with_factor_applied(BigDecimal r89_bob_with_factor_applied) {
			this.r89_bob_with_factor_applied = r89_bob_with_factor_applied;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String REPORT_FREQUENCY) {
			this.REPORT_FREQUENCY = REPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String REPORT_CODE) {
			this.REPORT_CODE = REPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String REPORT_DESC) {
			this.REPORT_DESC = REPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String ENTITY_FLG) {
			this.ENTITY_FLG = ENTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String MODIFY_FLG) {
			this.MODIFY_FLG = MODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String DEL_FLG) {
			this.DEL_FLG = DEL_FLG;
		}
	}

	// 2.2 DETAIL ENTITY
	public static class M_LCR_Detail_Entity {
		private String CUST_ID;
		private String ACCT_NUMBER;
		private String ACCT_NAME;
		private String DATA_TYPE;
		private String REPORT_LABEL;
		private String REPORT_LABEL_2;
		private String REPORT_LABEL_3;
		private String REPORT_ADDL_CRITERIA_1;
		private String REPORT_ADDL_CRITERIA_2;
		private String REPORT_ADDL_CRITERIA_3;
		private BigDecimal SANCTION_LIMIT;
		private String REPORT_REMARKS;
		private String MODIFICATION_REMARKS;
		private String DATA_ENTRY_VERSION;
		private BigDecimal ACCT_BALANCE_IN_PULA;
		private String REPORT_NAME;
		private String CREATE_USER;
		private Date CREATE_TIME;
		private String MODIFY_USER;
		private Date MODIFY_TIME;
		private String VERIFY_USER;
		private Date VERIFY_TIME;
		private BigDecimal DEBITEQUIVALENT;
		private BigDecimal EMI;
		private BigDecimal CREDITEQUIVALENT;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		// Getters and Setters
		public String getCUST_ID() {
			return CUST_ID;
		}

		public void setCUST_ID(String CUST_ID) {
			this.CUST_ID = CUST_ID;
		}

		public String getACCT_NUMBER() {
			return ACCT_NUMBER;
		}

		public void setACCT_NUMBER(String ACCT_NUMBER) {
			this.ACCT_NUMBER = ACCT_NUMBER;
		}

		public String getACCT_NAME() {
			return ACCT_NAME;
		}

		public void setACCT_NAME(String ACCT_NAME) {
			this.ACCT_NAME = ACCT_NAME;
		}

		public String getDATA_TYPE() {
			return DATA_TYPE;
		}

		public void setDATA_TYPE(String DATA_TYPE) {
			this.DATA_TYPE = DATA_TYPE;
		}

		public String getREPORT_LABEL() {
			return REPORT_LABEL;
		}

		public void setREPORT_LABEL(String REPORT_LABEL) {
			this.REPORT_LABEL = REPORT_LABEL;
		}

		public String getREPORT_LABEL_2() {
			return REPORT_LABEL_2;
		}

		public void setREPORT_LABEL_2(String REPORT_LABEL_2) {
			this.REPORT_LABEL_2 = REPORT_LABEL_2;
		}

		public String getREPORT_LABEL_3() {
			return REPORT_LABEL_3;
		}

		public void setREPORT_LABEL_3(String REPORT_LABEL_3) {
			this.REPORT_LABEL_3 = REPORT_LABEL_3;
		}

		public String getREPORT_ADDL_CRITERIA_1() {
			return REPORT_ADDL_CRITERIA_1;
		}

		public void setREPORT_ADDL_CRITERIA_1(String REPORT_ADDL_CRITERIA_1) {
			this.REPORT_ADDL_CRITERIA_1 = REPORT_ADDL_CRITERIA_1;
		}

		public String getREPORT_ADDL_CRITERIA_2() {
			return REPORT_ADDL_CRITERIA_2;
		}

		public void setREPORT_ADDL_CRITERIA_2(String REPORT_ADDL_CRITERIA_2) {
			this.REPORT_ADDL_CRITERIA_2 = REPORT_ADDL_CRITERIA_2;
		}

		public String getREPORT_ADDL_CRITERIA_3() {
			return REPORT_ADDL_CRITERIA_3;
		}

		public void setREPORT_ADDL_CRITERIA_3(String REPORT_ADDL_CRITERIA_3) {
			this.REPORT_ADDL_CRITERIA_3 = REPORT_ADDL_CRITERIA_3;
		}

		public BigDecimal getSANCTION_LIMIT() {
			return SANCTION_LIMIT;
		}

		public void setSANCTION_LIMIT(BigDecimal SANCTION_LIMIT) {
			this.SANCTION_LIMIT = SANCTION_LIMIT;
		}

		public String getREPORT_REMARKS() {
			return REPORT_REMARKS;
		}

		public void setREPORT_REMARKS(String REPORT_REMARKS) {
			this.REPORT_REMARKS = REPORT_REMARKS;
		}

		public String getMODIFICATION_REMARKS() {
			return MODIFICATION_REMARKS;
		}

		public void setMODIFICATION_REMARKS(String MODIFICATION_REMARKS) {
			this.MODIFICATION_REMARKS = MODIFICATION_REMARKS;
		}

		public String getDATA_ENTRY_VERSION() {
			return DATA_ENTRY_VERSION;
		}

		public void setDATA_ENTRY_VERSION(String DATA_ENTRY_VERSION) {
			this.DATA_ENTRY_VERSION = DATA_ENTRY_VERSION;
		}

		public BigDecimal getACCT_BALANCE_IN_PULA() {
			return ACCT_BALANCE_IN_PULA;
		}

		public void setACCT_BALANCE_IN_PULA(BigDecimal ACCT_BALANCE_IN_PULA) {
			this.ACCT_BALANCE_IN_PULA = ACCT_BALANCE_IN_PULA;
		}

		public String getREPORT_NAME() {
			return REPORT_NAME;
		}

		public void setREPORT_NAME(String REPORT_NAME) {
			this.REPORT_NAME = REPORT_NAME;
		}

		public String getCREATE_USER() {
			return CREATE_USER;
		}

		public void setCREATE_USER(String CREATE_USER) {
			this.CREATE_USER = CREATE_USER;
		}

		public Date getCREATE_TIME() {
			return CREATE_TIME;
		}

		public void setCREATE_TIME(Date CREATE_TIME) {
			this.CREATE_TIME = CREATE_TIME;
		}

		public String getMODIFY_USER() {
			return MODIFY_USER;
		}

		public void setMODIFY_USER(String MODIFY_USER) {
			this.MODIFY_USER = MODIFY_USER;
		}

		public Date getMODIFY_TIME() {
			return MODIFY_TIME;
		}

		public void setMODIFY_TIME(Date MODIFY_TIME) {
			this.MODIFY_TIME = MODIFY_TIME;
		}

		public String getVERIFY_USER() {
			return VERIFY_USER;
		}

		public void setVERIFY_USER(String VERIFY_USER) {
			this.VERIFY_USER = VERIFY_USER;
		}

		public Date getVERIFY_TIME() {
			return VERIFY_TIME;
		}

		public void setVERIFY_TIME(Date VERIFY_TIME) {
			this.VERIFY_TIME = VERIFY_TIME;
		}

		public BigDecimal getDEBITEQUIVALENT() {
			return DEBITEQUIVALENT;
		}

		public void setDEBITEQUIVALENT(BigDecimal DEBITEQUIVALENT) {
			this.DEBITEQUIVALENT = DEBITEQUIVALENT;
		}

		public BigDecimal getEMI() {
			return EMI;
		}

		public void setEMI(BigDecimal EMI) {
			this.EMI = EMI;
		}

		public BigDecimal getCREDITEQUIVALENT() {
			return CREDITEQUIVALENT;
		}

		public void setCREDITEQUIVALENT(BigDecimal CREDITEQUIVALENT) {
			this.CREDITEQUIVALENT = CREDITEQUIVALENT;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String REPORT_FREQUENCY) {
			this.REPORT_FREQUENCY = REPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String REPORT_CODE) {
			this.REPORT_CODE = REPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String REPORT_DESC) {
			this.REPORT_DESC = REPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String ENTITY_FLG) {
			this.ENTITY_FLG = ENTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String MODIFY_FLG) {
			this.MODIFY_FLG = MODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String DEL_FLG) {
			this.DEL_FLG = DEL_FLG;
		}
	}

	// 2.3 ARCHIVAL SUMMARY ENTITY
	@IdClass(M_LCR_PK.class)
	public static class M_LCR_Archival_Summary_Entity {
		private String r10_product;
		private String r11_product;
		private BigDecimal r11_amount_factor;
		private BigDecimal r11_bob_total_amount;
		private BigDecimal r11_bob_with_factor_applied;
		private String r12_product;
		private BigDecimal r12_amount_factor;
		private BigDecimal r12_bob_total_amount;
		private BigDecimal r12_bob_with_factor_applied;
		private String r13_product;
		private BigDecimal r13_amount_factor;
		private BigDecimal r13_bob_total_amount;
		private BigDecimal r13_bob_with_factor_applied;
		private String r14_product;
		private BigDecimal r14_amount_factor;
		private BigDecimal r14_bob_total_amount;
		private BigDecimal r14_bob_with_factor_applied;
		private String r15_product;
		private BigDecimal r15_amount_factor;
		private BigDecimal r15_bob_total_amount;
		private BigDecimal r15_bob_with_factor_applied;
		private String r16_product;
		private BigDecimal r16_amount_factor;
		private BigDecimal r16_bob_total_amount;
		private BigDecimal r16_bob_with_factor_applied;
		private String r17_product;
		private BigDecimal r17_amount_factor;
		private BigDecimal r17_bob_total_amount;
		private BigDecimal r17_bob_with_factor_applied;
		private String r18_product;
		private BigDecimal r18_amount_factor;
		private BigDecimal r18_bob_total_amount;
		private BigDecimal r18_bob_with_factor_applied;
		private String r19_product;
		private BigDecimal r19_amount_factor;
		private BigDecimal r19_bob_total_amount;
		private BigDecimal r19_bob_with_factor_applied;
		private String r20_product;
		private BigDecimal r20_amount_factor;
		private BigDecimal r20_bob_total_amount;
		private BigDecimal r20_bob_with_factor_applied;
		private String r21_product;
		private BigDecimal r21_amount_factor;
		private BigDecimal r21_bob_total_amount;
		private BigDecimal r21_bob_with_factor_applied;
		private String r22_product;
		private BigDecimal r22_amount_factor;
		private BigDecimal r22_bob_total_amount;
		private BigDecimal r22_bob_with_factor_applied;
		private String r23_product;
		private BigDecimal r23_amount_factor;
		private BigDecimal r23_bob_total_amount;
		private BigDecimal r23_bob_with_factor_applied;
		private String r24_product;
		private BigDecimal r24_amount_factor;
		private BigDecimal r24_bob_total_amount;
		private BigDecimal r24_bob_with_factor_applied;
		private String r25_product;
		private BigDecimal r26_amount_factor;
		private BigDecimal r26_bob_total_amount;
		private BigDecimal r26_bob_with_factor_applied;
		private String r26_product;
		private String r27_product;
		private BigDecimal r27_amount_factor;
		private BigDecimal r27_bob_total_amount;
		private BigDecimal r27_bob_with_factor_applied;
		private String r28_product;
		private BigDecimal r28_amount_factor;
		private BigDecimal r28_bob_total_amount;
		private BigDecimal r28_bob_with_factor_applied;
		private String r29_product;
		private BigDecimal r29_amount_factor;
		private BigDecimal r29_bob_total_amount;
		private BigDecimal r29_bob_with_factor_applied;
		private String r30_product;
		private BigDecimal r30_amount_factor;
		private BigDecimal r30_bob_total_amount;
		private BigDecimal r30_bob_with_factor_applied;
		private String r31_product;
		private BigDecimal r31_amount_factor;
		private BigDecimal r31_bob_total_amount;
		private BigDecimal r31_bob_with_factor_applied;
		private String r32_product;
		private BigDecimal r32_amount_factor;
		private BigDecimal r32_bob_total_amount;
		private BigDecimal r32_bob_with_factor_applied;
		private String r33_product;
		private BigDecimal r33_amount_factor;
		private BigDecimal r33_bob_total_amount;
		private BigDecimal r33_bob_with_factor_applied;
		private String r34_product;
		private BigDecimal r34_amount_factor;
		private BigDecimal r34_bob_total_amount;
		private BigDecimal r34_bob_with_factor_applied;
		private String r35_product;
		private BigDecimal r35_amount_factor;
		private BigDecimal r35_bob_total_amount;
		private BigDecimal r35_bob_with_factor_applied;
		private String r36_product;
		private BigDecimal r36_amount_factor;
		private BigDecimal r36_bob_total_amount;
		private BigDecimal r36_bob_with_factor_applied;
		private String r37_product;
		private BigDecimal r37_amount_factor;
		private BigDecimal r37_bob_total_amount;
		private BigDecimal r37_bob_with_factor_applied;
		private String r38_product;
		private BigDecimal r38_amount_factor;
		private BigDecimal r38_bob_total_amount;
		private BigDecimal r38_bob_with_factor_applied;
		private String r39_product;
		private BigDecimal r39_amount_factor;
		private BigDecimal r39_bob_total_amount;
		private BigDecimal r39_bob_with_factor_applied;
		private String r40_product;
		private BigDecimal r40_amount_factor;
		private BigDecimal r40_bob_total_amount;
		private BigDecimal r40_bob_with_factor_applied;
		private String r41_product;
		private BigDecimal r41_amount_factor;
		private BigDecimal r41_bob_total_amount;
		private BigDecimal r41_bob_with_factor_applied;
		private String r42_product;
		private BigDecimal r42_amount_factor;
		private BigDecimal r42_bob_total_amount;
		private BigDecimal r42_bob_with_factor_applied;
		private String r43_product;
		private BigDecimal r43_amount_factor;
		private BigDecimal r43_bob_total_amount;
		private BigDecimal r43_bob_with_factor_applied;
		private String r44_product;
		private BigDecimal r44_amount_factor;
		private BigDecimal r44_bob_total_amount;
		private BigDecimal r44_bob_with_factor_applied;
		private String r45_product;
		private BigDecimal r45_amount_factor;
		private BigDecimal r45_bob_total_amount;
		private BigDecimal r45_bob_with_factor_applied;
		private String r46_product;
		private BigDecimal r46_amount_factor;
		private BigDecimal r46_bob_total_amount;
		private BigDecimal r46_bob_with_factor_applied;
		private String r47_product;
		private BigDecimal r47_amount_factor;
		private BigDecimal r47_bob_total_amount;
		private BigDecimal r47_bob_with_factor_applied;
		private String r48_product;
		private BigDecimal r48_amount_factor;
		private BigDecimal r48_bob_total_amount;
		private BigDecimal r48_bob_with_factor_applied;
		private String r49_product;
		private BigDecimal r49_amount_factor;
		private BigDecimal r49_bob_total_amount;
		private BigDecimal r49_bob_with_factor_applied;
		private String r50_product;
		private BigDecimal r50_amount_factor;
		private BigDecimal r50_bob_total_amount;
		private BigDecimal r50_bob_with_factor_applied;
		private String r51_product;
		private BigDecimal r51_amount_factor;
		private BigDecimal r51_bob_total_amount;
		private BigDecimal r51_bob_with_factor_applied;
		private String r52_product;
		private BigDecimal r52_amount_factor;
		private BigDecimal r52_bob_total_amount;
		private BigDecimal r52_bob_with_factor_applied;
		private String r53_product;
		private BigDecimal r53_amount_factor;
		private BigDecimal r53_bob_total_amount;
		private BigDecimal r53_bob_with_factor_applied;
		private String r54_product;
		private BigDecimal r54_amount_factor;
		private BigDecimal r54_bob_total_amount;
		private BigDecimal r54_bob_with_factor_applied;
		private String r55_product;
		private BigDecimal r55_amount_factor;
		private BigDecimal r55_bob_total_amount;
		private BigDecimal r55_bob_with_factor_applied;
		private String r56_product;
		private BigDecimal r56_amount_factor;
		private BigDecimal r56_bob_total_amount;
		private BigDecimal r56_bob_with_factor_applied;
		private String r57_product;
		private BigDecimal r57_amount_factor;
		private BigDecimal r57_bob_total_amount;
		private BigDecimal r57_bob_with_factor_applied;
		private String r58_product;
		private BigDecimal r58_amount_factor;
		private BigDecimal r58_bob_total_amount;
		private BigDecimal r58_bob_with_factor_applied;
		private String r59_product;
		private BigDecimal r59_amount_factor;
		private BigDecimal r59_bob_total_amount;
		private BigDecimal r59_bob_with_factor_applied;
		private String r60_product;
		private BigDecimal r60_amount_factor;
		private BigDecimal r60_bob_total_amount;
		private BigDecimal r60_bob_with_factor_applied;
		private String r61_product;
		private BigDecimal r61_amount_factor;
		private BigDecimal r61_bob_total_amount;
		private BigDecimal r61_bob_with_factor_applied;
		private String r62_product;
		private BigDecimal r62_amount_factor;
		private BigDecimal r62_bob_total_amount;
		private BigDecimal r62_bob_with_factor_applied;
		private String r63_product;
		private BigDecimal r63_amount_factor;
		private BigDecimal r63_bob_total_amount;
		private BigDecimal r63_bob_with_factor_applied;
		private String r64_product;
		private BigDecimal r64_amount_factor;
		private BigDecimal r64_bob_total_amount;
		private BigDecimal r64_bob_with_factor_applied;
		private String r65_product;
		private BigDecimal r65_amount_factor;
		private BigDecimal r65_bob_total_amount;
		private BigDecimal r65_bob_with_factor_applied;
		private String r66_product;
		private BigDecimal r66_amount_factor;
		private BigDecimal r66_bob_total_amount;
		private BigDecimal r66_bob_with_factor_applied;
		private String r67_product;
		private BigDecimal r67_amount_factor;
		private BigDecimal r67_bob_total_amount;
		private BigDecimal r67_bob_with_factor_applied;
		private String r68_product;
		private BigDecimal r68_amount_factor;
		private BigDecimal r68_bob_total_amount;
		private BigDecimal r68_bob_with_factor_applied;
		private String r69_product;
		private BigDecimal r69_amount_factor;
		private BigDecimal r69_bob_total_amount;
		private BigDecimal r69_bob_with_factor_applied;
		private String r70_product;
		private BigDecimal r70_amount_factor;
		private BigDecimal r70_bob_total_amount;
		private BigDecimal r70_bob_with_factor_applied;
		private String r72_product;
		private BigDecimal r72_amount_factor;
		private BigDecimal r72_bob_total_amount;
		private BigDecimal r72_bob_with_factor_applied;
		private String r73_product;
		private BigDecimal r73_amount_factor;
		private BigDecimal r73_bob_total_amount;
		private BigDecimal r73_bob_with_factor_applied;
		private String r74_product;
		private BigDecimal r74_amount_factor;
		private BigDecimal r74_bob_total_amount;
		private BigDecimal r74_bob_with_factor_applied;
		private String r75_product;
		private BigDecimal r75_amount_factor;
		private BigDecimal r75_bob_total_amount;
		private BigDecimal r75_bob_with_factor_applied;
		private String r76_product;
		private BigDecimal r76_amount_factor;
		private BigDecimal r76_bob_total_amount;
		private BigDecimal r76_bob_with_factor_applied;
		private String r77_product;
		private BigDecimal r77_amount_factor;
		private BigDecimal r77_bob_total_amount;
		private BigDecimal r77_bob_with_factor_applied;
		private String r78_product;
		private BigDecimal r78_amount_factor;
		private BigDecimal r78_bob_total_amount;
		private BigDecimal r78_bob_with_factor_applied;
		private String r79_product;
		private BigDecimal r79_amount_factor;
		private BigDecimal r79_bob_total_amount;
		private BigDecimal r79_bob_with_factor_applied;
		private String r80_product;
		private BigDecimal r80_amount_factor;
		private BigDecimal r80_bob_total_amount;
		private BigDecimal r80_bob_with_factor_applied;
		private String r81_product;
		private BigDecimal r81_amount_factor;
		private BigDecimal r81_bob_total_amount;
		private BigDecimal r81_bob_with_factor_applied;
		private String r82_product;
		private BigDecimal r82_amount_factor;
		private BigDecimal r82_bob_total_amount;
		private BigDecimal r82_bob_with_factor_applied;
		private String r83_product;
		private BigDecimal r83_amount_factor;
		private BigDecimal r83_bob_total_amount;
		private BigDecimal r83_bob_with_factor_applied;
		private String r84_product;
		private BigDecimal r84_amount_factor;
		private BigDecimal r84_bob_total_amount;
		private BigDecimal r84_bob_with_factor_applied;
		private String r85_product;
		private BigDecimal r85_amount_factor;
		private BigDecimal r85_bob_total_amount;
		private BigDecimal r85_bob_with_factor_applied;
		private String r86_product;
		private BigDecimal r86_amount_factor;
		private BigDecimal r86_bob_total_amount;
		private BigDecimal r86_bob_with_factor_applied;
		private String r87_product;
		private BigDecimal r87_amount_factor;
		private BigDecimal r87_bob_total_amount;
		private BigDecimal r87_bob_with_factor_applied;
		private String r88_product;
		private BigDecimal r88_amount_factor;
		private BigDecimal r88_bob_total_amount;
		private BigDecimal r88_bob_with_factor_applied;
		private String r89_product;
		private BigDecimal r89_amount_factor;
		private BigDecimal r89_bob_total_amount;
		private BigDecimal r89_bob_with_factor_applied;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_RESUBDATE")
		private Date REPORT_RESUBDATE;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		// Getters and Setters for all fields
		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_amount_factor() {
			return r11_amount_factor;
		}

		public void setR11_amount_factor(BigDecimal r11_amount_factor) {
			this.r11_amount_factor = r11_amount_factor;
		}

		public BigDecimal getR11_bob_total_amount() {
			return r11_bob_total_amount;
		}

		public void setR11_bob_total_amount(BigDecimal r11_bob_total_amount) {
			this.r11_bob_total_amount = r11_bob_total_amount;
		}

		public BigDecimal getR11_bob_with_factor_applied() {
			return r11_bob_with_factor_applied;
		}

		public void setR11_bob_with_factor_applied(BigDecimal r11_bob_with_factor_applied) {
			this.r11_bob_with_factor_applied = r11_bob_with_factor_applied;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_amount_factor() {
			return r12_amount_factor;
		}

		public void setR12_amount_factor(BigDecimal r12_amount_factor) {
			this.r12_amount_factor = r12_amount_factor;
		}

		public BigDecimal getR12_bob_total_amount() {
			return r12_bob_total_amount;
		}

		public void setR12_bob_total_amount(BigDecimal r12_bob_total_amount) {
			this.r12_bob_total_amount = r12_bob_total_amount;
		}

		public BigDecimal getR12_bob_with_factor_applied() {
			return r12_bob_with_factor_applied;
		}

		public void setR12_bob_with_factor_applied(BigDecimal r12_bob_with_factor_applied) {
			this.r12_bob_with_factor_applied = r12_bob_with_factor_applied;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_amount_factor() {
			return r13_amount_factor;
		}

		public void setR13_amount_factor(BigDecimal r13_amount_factor) {
			this.r13_amount_factor = r13_amount_factor;
		}

		public BigDecimal getR13_bob_total_amount() {
			return r13_bob_total_amount;
		}

		public void setR13_bob_total_amount(BigDecimal r13_bob_total_amount) {
			this.r13_bob_total_amount = r13_bob_total_amount;
		}

		public BigDecimal getR13_bob_with_factor_applied() {
			return r13_bob_with_factor_applied;
		}

		public void setR13_bob_with_factor_applied(BigDecimal r13_bob_with_factor_applied) {
			this.r13_bob_with_factor_applied = r13_bob_with_factor_applied;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_amount_factor() {
			return r14_amount_factor;
		}

		public void setR14_amount_factor(BigDecimal r14_amount_factor) {
			this.r14_amount_factor = r14_amount_factor;
		}

		public BigDecimal getR14_bob_total_amount() {
			return r14_bob_total_amount;
		}

		public void setR14_bob_total_amount(BigDecimal r14_bob_total_amount) {
			this.r14_bob_total_amount = r14_bob_total_amount;
		}

		public BigDecimal getR14_bob_with_factor_applied() {
			return r14_bob_with_factor_applied;
		}

		public void setR14_bob_with_factor_applied(BigDecimal r14_bob_with_factor_applied) {
			this.r14_bob_with_factor_applied = r14_bob_with_factor_applied;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_amount_factor() {
			return r15_amount_factor;
		}

		public void setR15_amount_factor(BigDecimal r15_amount_factor) {
			this.r15_amount_factor = r15_amount_factor;
		}

		public BigDecimal getR15_bob_total_amount() {
			return r15_bob_total_amount;
		}

		public void setR15_bob_total_amount(BigDecimal r15_bob_total_amount) {
			this.r15_bob_total_amount = r15_bob_total_amount;
		}

		public BigDecimal getR15_bob_with_factor_applied() {
			return r15_bob_with_factor_applied;
		}

		public void setR15_bob_with_factor_applied(BigDecimal r15_bob_with_factor_applied) {
			this.r15_bob_with_factor_applied = r15_bob_with_factor_applied;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_amount_factor() {
			return r16_amount_factor;
		}

		public void setR16_amount_factor(BigDecimal r16_amount_factor) {
			this.r16_amount_factor = r16_amount_factor;
		}

		public BigDecimal getR16_bob_total_amount() {
			return r16_bob_total_amount;
		}

		public void setR16_bob_total_amount(BigDecimal r16_bob_total_amount) {
			this.r16_bob_total_amount = r16_bob_total_amount;
		}

		public BigDecimal getR16_bob_with_factor_applied() {
			return r16_bob_with_factor_applied;
		}

		public void setR16_bob_with_factor_applied(BigDecimal r16_bob_with_factor_applied) {
			this.r16_bob_with_factor_applied = r16_bob_with_factor_applied;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_amount_factor() {
			return r17_amount_factor;
		}

		public void setR17_amount_factor(BigDecimal r17_amount_factor) {
			this.r17_amount_factor = r17_amount_factor;
		}

		public BigDecimal getR17_bob_total_amount() {
			return r17_bob_total_amount;
		}

		public void setR17_bob_total_amount(BigDecimal r17_bob_total_amount) {
			this.r17_bob_total_amount = r17_bob_total_amount;
		}

		public BigDecimal getR17_bob_with_factor_applied() {
			return r17_bob_with_factor_applied;
		}

		public void setR17_bob_with_factor_applied(BigDecimal r17_bob_with_factor_applied) {
			this.r17_bob_with_factor_applied = r17_bob_with_factor_applied;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_amount_factor() {
			return r18_amount_factor;
		}

		public void setR18_amount_factor(BigDecimal r18_amount_factor) {
			this.r18_amount_factor = r18_amount_factor;
		}

		public BigDecimal getR18_bob_total_amount() {
			return r18_bob_total_amount;
		}

		public void setR18_bob_total_amount(BigDecimal r18_bob_total_amount) {
			this.r18_bob_total_amount = r18_bob_total_amount;
		}

		public BigDecimal getR18_bob_with_factor_applied() {
			return r18_bob_with_factor_applied;
		}

		public void setR18_bob_with_factor_applied(BigDecimal r18_bob_with_factor_applied) {
			this.r18_bob_with_factor_applied = r18_bob_with_factor_applied;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_amount_factor() {
			return r19_amount_factor;
		}

		public void setR19_amount_factor(BigDecimal r19_amount_factor) {
			this.r19_amount_factor = r19_amount_factor;
		}

		public BigDecimal getR19_bob_total_amount() {
			return r19_bob_total_amount;
		}

		public void setR19_bob_total_amount(BigDecimal r19_bob_total_amount) {
			this.r19_bob_total_amount = r19_bob_total_amount;
		}

		public BigDecimal getR19_bob_with_factor_applied() {
			return r19_bob_with_factor_applied;
		}

		public void setR19_bob_with_factor_applied(BigDecimal r19_bob_with_factor_applied) {
			this.r19_bob_with_factor_applied = r19_bob_with_factor_applied;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_amount_factor() {
			return r20_amount_factor;
		}

		public void setR20_amount_factor(BigDecimal r20_amount_factor) {
			this.r20_amount_factor = r20_amount_factor;
		}

		public BigDecimal getR20_bob_total_amount() {
			return r20_bob_total_amount;
		}

		public void setR20_bob_total_amount(BigDecimal r20_bob_total_amount) {
			this.r20_bob_total_amount = r20_bob_total_amount;
		}

		public BigDecimal getR20_bob_with_factor_applied() {
			return r20_bob_with_factor_applied;
		}

		public void setR20_bob_with_factor_applied(BigDecimal r20_bob_with_factor_applied) {
			this.r20_bob_with_factor_applied = r20_bob_with_factor_applied;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_amount_factor() {
			return r21_amount_factor;
		}

		public void setR21_amount_factor(BigDecimal r21_amount_factor) {
			this.r21_amount_factor = r21_amount_factor;
		}

		public BigDecimal getR21_bob_total_amount() {
			return r21_bob_total_amount;
		}

		public void setR21_bob_total_amount(BigDecimal r21_bob_total_amount) {
			this.r21_bob_total_amount = r21_bob_total_amount;
		}

		public BigDecimal getR21_bob_with_factor_applied() {
			return r21_bob_with_factor_applied;
		}

		public void setR21_bob_with_factor_applied(BigDecimal r21_bob_with_factor_applied) {
			this.r21_bob_with_factor_applied = r21_bob_with_factor_applied;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_amount_factor() {
			return r22_amount_factor;
		}

		public void setR22_amount_factor(BigDecimal r22_amount_factor) {
			this.r22_amount_factor = r22_amount_factor;
		}

		public BigDecimal getR22_bob_total_amount() {
			return r22_bob_total_amount;
		}

		public void setR22_bob_total_amount(BigDecimal r22_bob_total_amount) {
			this.r22_bob_total_amount = r22_bob_total_amount;
		}

		public BigDecimal getR22_bob_with_factor_applied() {
			return r22_bob_with_factor_applied;
		}

		public void setR22_bob_with_factor_applied(BigDecimal r22_bob_with_factor_applied) {
			this.r22_bob_with_factor_applied = r22_bob_with_factor_applied;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_amount_factor() {
			return r23_amount_factor;
		}

		public void setR23_amount_factor(BigDecimal r23_amount_factor) {
			this.r23_amount_factor = r23_amount_factor;
		}

		public BigDecimal getR23_bob_total_amount() {
			return r23_bob_total_amount;
		}

		public void setR23_bob_total_amount(BigDecimal r23_bob_total_amount) {
			this.r23_bob_total_amount = r23_bob_total_amount;
		}

		public BigDecimal getR23_bob_with_factor_applied() {
			return r23_bob_with_factor_applied;
		}

		public void setR23_bob_with_factor_applied(BigDecimal r23_bob_with_factor_applied) {
			this.r23_bob_with_factor_applied = r23_bob_with_factor_applied;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_amount_factor() {
			return r24_amount_factor;
		}

		public void setR24_amount_factor(BigDecimal r24_amount_factor) {
			this.r24_amount_factor = r24_amount_factor;
		}

		public BigDecimal getR24_bob_total_amount() {
			return r24_bob_total_amount;
		}

		public void setR24_bob_total_amount(BigDecimal r24_bob_total_amount) {
			this.r24_bob_total_amount = r24_bob_total_amount;
		}

		public BigDecimal getR24_bob_with_factor_applied() {
			return r24_bob_with_factor_applied;
		}

		public void setR24_bob_with_factor_applied(BigDecimal r24_bob_with_factor_applied) {
			this.r24_bob_with_factor_applied = r24_bob_with_factor_applied;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_amount_factor() {
			return r26_amount_factor;
		}

		public void setR26_amount_factor(BigDecimal r26_amount_factor) {
			this.r26_amount_factor = r26_amount_factor;
		}

		public BigDecimal getR26_bob_total_amount() {
			return r26_bob_total_amount;
		}

		public void setR26_bob_total_amount(BigDecimal r26_bob_total_amount) {
			this.r26_bob_total_amount = r26_bob_total_amount;
		}

		public BigDecimal getR26_bob_with_factor_applied() {
			return r26_bob_with_factor_applied;
		}

		public void setR26_bob_with_factor_applied(BigDecimal r26_bob_with_factor_applied) {
			this.r26_bob_with_factor_applied = r26_bob_with_factor_applied;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_amount_factor() {
			return r27_amount_factor;
		}

		public void setR27_amount_factor(BigDecimal r27_amount_factor) {
			this.r27_amount_factor = r27_amount_factor;
		}

		public BigDecimal getR27_bob_total_amount() {
			return r27_bob_total_amount;
		}

		public void setR27_bob_total_amount(BigDecimal r27_bob_total_amount) {
			this.r27_bob_total_amount = r27_bob_total_amount;
		}

		public BigDecimal getR27_bob_with_factor_applied() {
			return r27_bob_with_factor_applied;
		}

		public void setR27_bob_with_factor_applied(BigDecimal r27_bob_with_factor_applied) {
			this.r27_bob_with_factor_applied = r27_bob_with_factor_applied;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_amount_factor() {
			return r28_amount_factor;
		}

		public void setR28_amount_factor(BigDecimal r28_amount_factor) {
			this.r28_amount_factor = r28_amount_factor;
		}

		public BigDecimal getR28_bob_total_amount() {
			return r28_bob_total_amount;
		}

		public void setR28_bob_total_amount(BigDecimal r28_bob_total_amount) {
			this.r28_bob_total_amount = r28_bob_total_amount;
		}

		public BigDecimal getR28_bob_with_factor_applied() {
			return r28_bob_with_factor_applied;
		}

		public void setR28_bob_with_factor_applied(BigDecimal r28_bob_with_factor_applied) {
			this.r28_bob_with_factor_applied = r28_bob_with_factor_applied;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_amount_factor() {
			return r29_amount_factor;
		}

		public void setR29_amount_factor(BigDecimal r29_amount_factor) {
			this.r29_amount_factor = r29_amount_factor;
		}

		public BigDecimal getR29_bob_total_amount() {
			return r29_bob_total_amount;
		}

		public void setR29_bob_total_amount(BigDecimal r29_bob_total_amount) {
			this.r29_bob_total_amount = r29_bob_total_amount;
		}

		public BigDecimal getR29_bob_with_factor_applied() {
			return r29_bob_with_factor_applied;
		}

		public void setR29_bob_with_factor_applied(BigDecimal r29_bob_with_factor_applied) {
			this.r29_bob_with_factor_applied = r29_bob_with_factor_applied;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_amount_factor() {
			return r30_amount_factor;
		}

		public void setR30_amount_factor(BigDecimal r30_amount_factor) {
			this.r30_amount_factor = r30_amount_factor;
		}

		public BigDecimal getR30_bob_total_amount() {
			return r30_bob_total_amount;
		}

		public void setR30_bob_total_amount(BigDecimal r30_bob_total_amount) {
			this.r30_bob_total_amount = r30_bob_total_amount;
		}

		public BigDecimal getR30_bob_with_factor_applied() {
			return r30_bob_with_factor_applied;
		}

		public void setR30_bob_with_factor_applied(BigDecimal r30_bob_with_factor_applied) {
			this.r30_bob_with_factor_applied = r30_bob_with_factor_applied;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_amount_factor() {
			return r31_amount_factor;
		}

		public void setR31_amount_factor(BigDecimal r31_amount_factor) {
			this.r31_amount_factor = r31_amount_factor;
		}

		public BigDecimal getR31_bob_total_amount() {
			return r31_bob_total_amount;
		}

		public void setR31_bob_total_amount(BigDecimal r31_bob_total_amount) {
			this.r31_bob_total_amount = r31_bob_total_amount;
		}

		public BigDecimal getR31_bob_with_factor_applied() {
			return r31_bob_with_factor_applied;
		}

		public void setR31_bob_with_factor_applied(BigDecimal r31_bob_with_factor_applied) {
			this.r31_bob_with_factor_applied = r31_bob_with_factor_applied;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_amount_factor() {
			return r32_amount_factor;
		}

		public void setR32_amount_factor(BigDecimal r32_amount_factor) {
			this.r32_amount_factor = r32_amount_factor;
		}

		public BigDecimal getR32_bob_total_amount() {
			return r32_bob_total_amount;
		}

		public void setR32_bob_total_amount(BigDecimal r32_bob_total_amount) {
			this.r32_bob_total_amount = r32_bob_total_amount;
		}

		public BigDecimal getR32_bob_with_factor_applied() {
			return r32_bob_with_factor_applied;
		}

		public void setR32_bob_with_factor_applied(BigDecimal r32_bob_with_factor_applied) {
			this.r32_bob_with_factor_applied = r32_bob_with_factor_applied;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_amount_factor() {
			return r33_amount_factor;
		}

		public void setR33_amount_factor(BigDecimal r33_amount_factor) {
			this.r33_amount_factor = r33_amount_factor;
		}

		public BigDecimal getR33_bob_total_amount() {
			return r33_bob_total_amount;
		}

		public void setR33_bob_total_amount(BigDecimal r33_bob_total_amount) {
			this.r33_bob_total_amount = r33_bob_total_amount;
		}

		public BigDecimal getR33_bob_with_factor_applied() {
			return r33_bob_with_factor_applied;
		}

		public void setR33_bob_with_factor_applied(BigDecimal r33_bob_with_factor_applied) {
			this.r33_bob_with_factor_applied = r33_bob_with_factor_applied;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_amount_factor() {
			return r34_amount_factor;
		}

		public void setR34_amount_factor(BigDecimal r34_amount_factor) {
			this.r34_amount_factor = r34_amount_factor;
		}

		public BigDecimal getR34_bob_total_amount() {
			return r34_bob_total_amount;
		}

		public void setR34_bob_total_amount(BigDecimal r34_bob_total_amount) {
			this.r34_bob_total_amount = r34_bob_total_amount;
		}

		public BigDecimal getR34_bob_with_factor_applied() {
			return r34_bob_with_factor_applied;
		}

		public void setR34_bob_with_factor_applied(BigDecimal r34_bob_with_factor_applied) {
			this.r34_bob_with_factor_applied = r34_bob_with_factor_applied;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_amount_factor() {
			return r35_amount_factor;
		}

		public void setR35_amount_factor(BigDecimal r35_amount_factor) {
			this.r35_amount_factor = r35_amount_factor;
		}

		public BigDecimal getR35_bob_total_amount() {
			return r35_bob_total_amount;
		}

		public void setR35_bob_total_amount(BigDecimal r35_bob_total_amount) {
			this.r35_bob_total_amount = r35_bob_total_amount;
		}

		public BigDecimal getR35_bob_with_factor_applied() {
			return r35_bob_with_factor_applied;
		}

		public void setR35_bob_with_factor_applied(BigDecimal r35_bob_with_factor_applied) {
			this.r35_bob_with_factor_applied = r35_bob_with_factor_applied;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_amount_factor() {
			return r36_amount_factor;
		}

		public void setR36_amount_factor(BigDecimal r36_amount_factor) {
			this.r36_amount_factor = r36_amount_factor;
		}

		public BigDecimal getR36_bob_total_amount() {
			return r36_bob_total_amount;
		}

		public void setR36_bob_total_amount(BigDecimal r36_bob_total_amount) {
			this.r36_bob_total_amount = r36_bob_total_amount;
		}

		public BigDecimal getR36_bob_with_factor_applied() {
			return r36_bob_with_factor_applied;
		}

		public void setR36_bob_with_factor_applied(BigDecimal r36_bob_with_factor_applied) {
			this.r36_bob_with_factor_applied = r36_bob_with_factor_applied;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_amount_factor() {
			return r37_amount_factor;
		}

		public void setR37_amount_factor(BigDecimal r37_amount_factor) {
			this.r37_amount_factor = r37_amount_factor;
		}

		public BigDecimal getR37_bob_total_amount() {
			return r37_bob_total_amount;
		}

		public void setR37_bob_total_amount(BigDecimal r37_bob_total_amount) {
			this.r37_bob_total_amount = r37_bob_total_amount;
		}

		public BigDecimal getR37_bob_with_factor_applied() {
			return r37_bob_with_factor_applied;
		}

		public void setR37_bob_with_factor_applied(BigDecimal r37_bob_with_factor_applied) {
			this.r37_bob_with_factor_applied = r37_bob_with_factor_applied;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_amount_factor() {
			return r38_amount_factor;
		}

		public void setR38_amount_factor(BigDecimal r38_amount_factor) {
			this.r38_amount_factor = r38_amount_factor;
		}

		public BigDecimal getR38_bob_total_amount() {
			return r38_bob_total_amount;
		}

		public void setR38_bob_total_amount(BigDecimal r38_bob_total_amount) {
			this.r38_bob_total_amount = r38_bob_total_amount;
		}

		public BigDecimal getR38_bob_with_factor_applied() {
			return r38_bob_with_factor_applied;
		}

		public void setR38_bob_with_factor_applied(BigDecimal r38_bob_with_factor_applied) {
			this.r38_bob_with_factor_applied = r38_bob_with_factor_applied;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_amount_factor() {
			return r39_amount_factor;
		}

		public void setR39_amount_factor(BigDecimal r39_amount_factor) {
			this.r39_amount_factor = r39_amount_factor;
		}

		public BigDecimal getR39_bob_total_amount() {
			return r39_bob_total_amount;
		}

		public void setR39_bob_total_amount(BigDecimal r39_bob_total_amount) {
			this.r39_bob_total_amount = r39_bob_total_amount;
		}

		public BigDecimal getR39_bob_with_factor_applied() {
			return r39_bob_with_factor_applied;
		}

		public void setR39_bob_with_factor_applied(BigDecimal r39_bob_with_factor_applied) {
			this.r39_bob_with_factor_applied = r39_bob_with_factor_applied;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_amount_factor() {
			return r40_amount_factor;
		}

		public void setR40_amount_factor(BigDecimal r40_amount_factor) {
			this.r40_amount_factor = r40_amount_factor;
		}

		public BigDecimal getR40_bob_total_amount() {
			return r40_bob_total_amount;
		}

		public void setR40_bob_total_amount(BigDecimal r40_bob_total_amount) {
			this.r40_bob_total_amount = r40_bob_total_amount;
		}

		public BigDecimal getR40_bob_with_factor_applied() {
			return r40_bob_with_factor_applied;
		}

		public void setR40_bob_with_factor_applied(BigDecimal r40_bob_with_factor_applied) {
			this.r40_bob_with_factor_applied = r40_bob_with_factor_applied;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_amount_factor() {
			return r41_amount_factor;
		}

		public void setR41_amount_factor(BigDecimal r41_amount_factor) {
			this.r41_amount_factor = r41_amount_factor;
		}

		public BigDecimal getR41_bob_total_amount() {
			return r41_bob_total_amount;
		}

		public void setR41_bob_total_amount(BigDecimal r41_bob_total_amount) {
			this.r41_bob_total_amount = r41_bob_total_amount;
		}

		public BigDecimal getR41_bob_with_factor_applied() {
			return r41_bob_with_factor_applied;
		}

		public void setR41_bob_with_factor_applied(BigDecimal r41_bob_with_factor_applied) {
			this.r41_bob_with_factor_applied = r41_bob_with_factor_applied;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_amount_factor() {
			return r42_amount_factor;
		}

		public void setR42_amount_factor(BigDecimal r42_amount_factor) {
			this.r42_amount_factor = r42_amount_factor;
		}

		public BigDecimal getR42_bob_total_amount() {
			return r42_bob_total_amount;
		}

		public void setR42_bob_total_amount(BigDecimal r42_bob_total_amount) {
			this.r42_bob_total_amount = r42_bob_total_amount;
		}

		public BigDecimal getR42_bob_with_factor_applied() {
			return r42_bob_with_factor_applied;
		}

		public void setR42_bob_with_factor_applied(BigDecimal r42_bob_with_factor_applied) {
			this.r42_bob_with_factor_applied = r42_bob_with_factor_applied;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_amount_factor() {
			return r43_amount_factor;
		}

		public void setR43_amount_factor(BigDecimal r43_amount_factor) {
			this.r43_amount_factor = r43_amount_factor;
		}

		public BigDecimal getR43_bob_total_amount() {
			return r43_bob_total_amount;
		}

		public void setR43_bob_total_amount(BigDecimal r43_bob_total_amount) {
			this.r43_bob_total_amount = r43_bob_total_amount;
		}

		public BigDecimal getR43_bob_with_factor_applied() {
			return r43_bob_with_factor_applied;
		}

		public void setR43_bob_with_factor_applied(BigDecimal r43_bob_with_factor_applied) {
			this.r43_bob_with_factor_applied = r43_bob_with_factor_applied;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_amount_factor() {
			return r44_amount_factor;
		}

		public void setR44_amount_factor(BigDecimal r44_amount_factor) {
			this.r44_amount_factor = r44_amount_factor;
		}

		public BigDecimal getR44_bob_total_amount() {
			return r44_bob_total_amount;
		}

		public void setR44_bob_total_amount(BigDecimal r44_bob_total_amount) {
			this.r44_bob_total_amount = r44_bob_total_amount;
		}

		public BigDecimal getR44_bob_with_factor_applied() {
			return r44_bob_with_factor_applied;
		}

		public void setR44_bob_with_factor_applied(BigDecimal r44_bob_with_factor_applied) {
			this.r44_bob_with_factor_applied = r44_bob_with_factor_applied;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_amount_factor() {
			return r45_amount_factor;
		}

		public void setR45_amount_factor(BigDecimal r45_amount_factor) {
			this.r45_amount_factor = r45_amount_factor;
		}

		public BigDecimal getR45_bob_total_amount() {
			return r45_bob_total_amount;
		}

		public void setR45_bob_total_amount(BigDecimal r45_bob_total_amount) {
			this.r45_bob_total_amount = r45_bob_total_amount;
		}

		public BigDecimal getR45_bob_with_factor_applied() {
			return r45_bob_with_factor_applied;
		}

		public void setR45_bob_with_factor_applied(BigDecimal r45_bob_with_factor_applied) {
			this.r45_bob_with_factor_applied = r45_bob_with_factor_applied;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_amount_factor() {
			return r46_amount_factor;
		}

		public void setR46_amount_factor(BigDecimal r46_amount_factor) {
			this.r46_amount_factor = r46_amount_factor;
		}

		public BigDecimal getR46_bob_total_amount() {
			return r46_bob_total_amount;
		}

		public void setR46_bob_total_amount(BigDecimal r46_bob_total_amount) {
			this.r46_bob_total_amount = r46_bob_total_amount;
		}

		public BigDecimal getR46_bob_with_factor_applied() {
			return r46_bob_with_factor_applied;
		}

		public void setR46_bob_with_factor_applied(BigDecimal r46_bob_with_factor_applied) {
			this.r46_bob_with_factor_applied = r46_bob_with_factor_applied;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_amount_factor() {
			return r47_amount_factor;
		}

		public void setR47_amount_factor(BigDecimal r47_amount_factor) {
			this.r47_amount_factor = r47_amount_factor;
		}

		public BigDecimal getR47_bob_total_amount() {
			return r47_bob_total_amount;
		}

		public void setR47_bob_total_amount(BigDecimal r47_bob_total_amount) {
			this.r47_bob_total_amount = r47_bob_total_amount;
		}

		public BigDecimal getR47_bob_with_factor_applied() {
			return r47_bob_with_factor_applied;
		}

		public void setR47_bob_with_factor_applied(BigDecimal r47_bob_with_factor_applied) {
			this.r47_bob_with_factor_applied = r47_bob_with_factor_applied;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_amount_factor() {
			return r48_amount_factor;
		}

		public void setR48_amount_factor(BigDecimal r48_amount_factor) {
			this.r48_amount_factor = r48_amount_factor;
		}

		public BigDecimal getR48_bob_total_amount() {
			return r48_bob_total_amount;
		}

		public void setR48_bob_total_amount(BigDecimal r48_bob_total_amount) {
			this.r48_bob_total_amount = r48_bob_total_amount;
		}

		public BigDecimal getR48_bob_with_factor_applied() {
			return r48_bob_with_factor_applied;
		}

		public void setR48_bob_with_factor_applied(BigDecimal r48_bob_with_factor_applied) {
			this.r48_bob_with_factor_applied = r48_bob_with_factor_applied;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_amount_factor() {
			return r49_amount_factor;
		}

		public void setR49_amount_factor(BigDecimal r49_amount_factor) {
			this.r49_amount_factor = r49_amount_factor;
		}

		public BigDecimal getR49_bob_total_amount() {
			return r49_bob_total_amount;
		}

		public void setR49_bob_total_amount(BigDecimal r49_bob_total_amount) {
			this.r49_bob_total_amount = r49_bob_total_amount;
		}

		public BigDecimal getR49_bob_with_factor_applied() {
			return r49_bob_with_factor_applied;
		}

		public void setR49_bob_with_factor_applied(BigDecimal r49_bob_with_factor_applied) {
			this.r49_bob_with_factor_applied = r49_bob_with_factor_applied;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_amount_factor() {
			return r50_amount_factor;
		}

		public void setR50_amount_factor(BigDecimal r50_amount_factor) {
			this.r50_amount_factor = r50_amount_factor;
		}

		public BigDecimal getR50_bob_total_amount() {
			return r50_bob_total_amount;
		}

		public void setR50_bob_total_amount(BigDecimal r50_bob_total_amount) {
			this.r50_bob_total_amount = r50_bob_total_amount;
		}

		public BigDecimal getR50_bob_with_factor_applied() {
			return r50_bob_with_factor_applied;
		}

		public void setR50_bob_with_factor_applied(BigDecimal r50_bob_with_factor_applied) {
			this.r50_bob_with_factor_applied = r50_bob_with_factor_applied;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_amount_factor() {
			return r51_amount_factor;
		}

		public void setR51_amount_factor(BigDecimal r51_amount_factor) {
			this.r51_amount_factor = r51_amount_factor;
		}

		public BigDecimal getR51_bob_total_amount() {
			return r51_bob_total_amount;
		}

		public void setR51_bob_total_amount(BigDecimal r51_bob_total_amount) {
			this.r51_bob_total_amount = r51_bob_total_amount;
		}

		public BigDecimal getR51_bob_with_factor_applied() {
			return r51_bob_with_factor_applied;
		}

		public void setR51_bob_with_factor_applied(BigDecimal r51_bob_with_factor_applied) {
			this.r51_bob_with_factor_applied = r51_bob_with_factor_applied;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_amount_factor() {
			return r52_amount_factor;
		}

		public void setR52_amount_factor(BigDecimal r52_amount_factor) {
			this.r52_amount_factor = r52_amount_factor;
		}

		public BigDecimal getR52_bob_total_amount() {
			return r52_bob_total_amount;
		}

		public void setR52_bob_total_amount(BigDecimal r52_bob_total_amount) {
			this.r52_bob_total_amount = r52_bob_total_amount;
		}

		public BigDecimal getR52_bob_with_factor_applied() {
			return r52_bob_with_factor_applied;
		}

		public void setR52_bob_with_factor_applied(BigDecimal r52_bob_with_factor_applied) {
			this.r52_bob_with_factor_applied = r52_bob_with_factor_applied;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_amount_factor() {
			return r53_amount_factor;
		}

		public void setR53_amount_factor(BigDecimal r53_amount_factor) {
			this.r53_amount_factor = r53_amount_factor;
		}

		public BigDecimal getR53_bob_total_amount() {
			return r53_bob_total_amount;
		}

		public void setR53_bob_total_amount(BigDecimal r53_bob_total_amount) {
			this.r53_bob_total_amount = r53_bob_total_amount;
		}

		public BigDecimal getR53_bob_with_factor_applied() {
			return r53_bob_with_factor_applied;
		}

		public void setR53_bob_with_factor_applied(BigDecimal r53_bob_with_factor_applied) {
			this.r53_bob_with_factor_applied = r53_bob_with_factor_applied;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_amount_factor() {
			return r54_amount_factor;
		}

		public void setR54_amount_factor(BigDecimal r54_amount_factor) {
			this.r54_amount_factor = r54_amount_factor;
		}

		public BigDecimal getR54_bob_total_amount() {
			return r54_bob_total_amount;
		}

		public void setR54_bob_total_amount(BigDecimal r54_bob_total_amount) {
			this.r54_bob_total_amount = r54_bob_total_amount;
		}

		public BigDecimal getR54_bob_with_factor_applied() {
			return r54_bob_with_factor_applied;
		}

		public void setR54_bob_with_factor_applied(BigDecimal r54_bob_with_factor_applied) {
			this.r54_bob_with_factor_applied = r54_bob_with_factor_applied;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_amount_factor() {
			return r55_amount_factor;
		}

		public void setR55_amount_factor(BigDecimal r55_amount_factor) {
			this.r55_amount_factor = r55_amount_factor;
		}

		public BigDecimal getR55_bob_total_amount() {
			return r55_bob_total_amount;
		}

		public void setR55_bob_total_amount(BigDecimal r55_bob_total_amount) {
			this.r55_bob_total_amount = r55_bob_total_amount;
		}

		public BigDecimal getR55_bob_with_factor_applied() {
			return r55_bob_with_factor_applied;
		}

		public void setR55_bob_with_factor_applied(BigDecimal r55_bob_with_factor_applied) {
			this.r55_bob_with_factor_applied = r55_bob_with_factor_applied;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_amount_factor() {
			return r56_amount_factor;
		}

		public void setR56_amount_factor(BigDecimal r56_amount_factor) {
			this.r56_amount_factor = r56_amount_factor;
		}

		public BigDecimal getR56_bob_total_amount() {
			return r56_bob_total_amount;
		}

		public void setR56_bob_total_amount(BigDecimal r56_bob_total_amount) {
			this.r56_bob_total_amount = r56_bob_total_amount;
		}

		public BigDecimal getR56_bob_with_factor_applied() {
			return r56_bob_with_factor_applied;
		}

		public void setR56_bob_with_factor_applied(BigDecimal r56_bob_with_factor_applied) {
			this.r56_bob_with_factor_applied = r56_bob_with_factor_applied;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_amount_factor() {
			return r57_amount_factor;
		}

		public void setR57_amount_factor(BigDecimal r57_amount_factor) {
			this.r57_amount_factor = r57_amount_factor;
		}

		public BigDecimal getR57_bob_total_amount() {
			return r57_bob_total_amount;
		}

		public void setR57_bob_total_amount(BigDecimal r57_bob_total_amount) {
			this.r57_bob_total_amount = r57_bob_total_amount;
		}

		public BigDecimal getR57_bob_with_factor_applied() {
			return r57_bob_with_factor_applied;
		}

		public void setR57_bob_with_factor_applied(BigDecimal r57_bob_with_factor_applied) {
			this.r57_bob_with_factor_applied = r57_bob_with_factor_applied;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public BigDecimal getR58_amount_factor() {
			return r58_amount_factor;
		}

		public void setR58_amount_factor(BigDecimal r58_amount_factor) {
			this.r58_amount_factor = r58_amount_factor;
		}

		public BigDecimal getR58_bob_total_amount() {
			return r58_bob_total_amount;
		}

		public void setR58_bob_total_amount(BigDecimal r58_bob_total_amount) {
			this.r58_bob_total_amount = r58_bob_total_amount;
		}

		public BigDecimal getR58_bob_with_factor_applied() {
			return r58_bob_with_factor_applied;
		}

		public void setR58_bob_with_factor_applied(BigDecimal r58_bob_with_factor_applied) {
			this.r58_bob_with_factor_applied = r58_bob_with_factor_applied;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public BigDecimal getR59_amount_factor() {
			return r59_amount_factor;
		}

		public void setR59_amount_factor(BigDecimal r59_amount_factor) {
			this.r59_amount_factor = r59_amount_factor;
		}

		public BigDecimal getR59_bob_total_amount() {
			return r59_bob_total_amount;
		}

		public void setR59_bob_total_amount(BigDecimal r59_bob_total_amount) {
			this.r59_bob_total_amount = r59_bob_total_amount;
		}

		public BigDecimal getR59_bob_with_factor_applied() {
			return r59_bob_with_factor_applied;
		}

		public void setR59_bob_with_factor_applied(BigDecimal r59_bob_with_factor_applied) {
			this.r59_bob_with_factor_applied = r59_bob_with_factor_applied;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public BigDecimal getR60_amount_factor() {
			return r60_amount_factor;
		}

		public void setR60_amount_factor(BigDecimal r60_amount_factor) {
			this.r60_amount_factor = r60_amount_factor;
		}

		public BigDecimal getR60_bob_total_amount() {
			return r60_bob_total_amount;
		}

		public void setR60_bob_total_amount(BigDecimal r60_bob_total_amount) {
			this.r60_bob_total_amount = r60_bob_total_amount;
		}

		public BigDecimal getR60_bob_with_factor_applied() {
			return r60_bob_with_factor_applied;
		}

		public void setR60_bob_with_factor_applied(BigDecimal r60_bob_with_factor_applied) {
			this.r60_bob_with_factor_applied = r60_bob_with_factor_applied;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public BigDecimal getR61_amount_factor() {
			return r61_amount_factor;
		}

		public void setR61_amount_factor(BigDecimal r61_amount_factor) {
			this.r61_amount_factor = r61_amount_factor;
		}

		public BigDecimal getR61_bob_total_amount() {
			return r61_bob_total_amount;
		}

		public void setR61_bob_total_amount(BigDecimal r61_bob_total_amount) {
			this.r61_bob_total_amount = r61_bob_total_amount;
		}

		public BigDecimal getR61_bob_with_factor_applied() {
			return r61_bob_with_factor_applied;
		}

		public void setR61_bob_with_factor_applied(BigDecimal r61_bob_with_factor_applied) {
			this.r61_bob_with_factor_applied = r61_bob_with_factor_applied;
		}

		public String getR62_product() {
			return r62_product;
		}

		public void setR62_product(String r62_product) {
			this.r62_product = r62_product;
		}

		public BigDecimal getR62_amount_factor() {
			return r62_amount_factor;
		}

		public void setR62_amount_factor(BigDecimal r62_amount_factor) {
			this.r62_amount_factor = r62_amount_factor;
		}

		public BigDecimal getR62_bob_total_amount() {
			return r62_bob_total_amount;
		}

		public void setR62_bob_total_amount(BigDecimal r62_bob_total_amount) {
			this.r62_bob_total_amount = r62_bob_total_amount;
		}

		public BigDecimal getR62_bob_with_factor_applied() {
			return r62_bob_with_factor_applied;
		}

		public void setR62_bob_with_factor_applied(BigDecimal r62_bob_with_factor_applied) {
			this.r62_bob_with_factor_applied = r62_bob_with_factor_applied;
		}

		public String getR63_product() {
			return r63_product;
		}

		public void setR63_product(String r63_product) {
			this.r63_product = r63_product;
		}

		public BigDecimal getR63_amount_factor() {
			return r63_amount_factor;
		}

		public void setR63_amount_factor(BigDecimal r63_amount_factor) {
			this.r63_amount_factor = r63_amount_factor;
		}

		public BigDecimal getR63_bob_total_amount() {
			return r63_bob_total_amount;
		}

		public void setR63_bob_total_amount(BigDecimal r63_bob_total_amount) {
			this.r63_bob_total_amount = r63_bob_total_amount;
		}

		public BigDecimal getR63_bob_with_factor_applied() {
			return r63_bob_with_factor_applied;
		}

		public void setR63_bob_with_factor_applied(BigDecimal r63_bob_with_factor_applied) {
			this.r63_bob_with_factor_applied = r63_bob_with_factor_applied;
		}

		public String getR64_product() {
			return r64_product;
		}

		public void setR64_product(String r64_product) {
			this.r64_product = r64_product;
		}

		public BigDecimal getR64_amount_factor() {
			return r64_amount_factor;
		}

		public void setR64_amount_factor(BigDecimal r64_amount_factor) {
			this.r64_amount_factor = r64_amount_factor;
		}

		public BigDecimal getR64_bob_total_amount() {
			return r64_bob_total_amount;
		}

		public void setR64_bob_total_amount(BigDecimal r64_bob_total_amount) {
			this.r64_bob_total_amount = r64_bob_total_amount;
		}

		public BigDecimal getR64_bob_with_factor_applied() {
			return r64_bob_with_factor_applied;
		}

		public void setR64_bob_with_factor_applied(BigDecimal r64_bob_with_factor_applied) {
			this.r64_bob_with_factor_applied = r64_bob_with_factor_applied;
		}

		public String getR65_product() {
			return r65_product;
		}

		public void setR65_product(String r65_product) {
			this.r65_product = r65_product;
		}

		public BigDecimal getR65_amount_factor() {
			return r65_amount_factor;
		}

		public void setR65_amount_factor(BigDecimal r65_amount_factor) {
			this.r65_amount_factor = r65_amount_factor;
		}

		public BigDecimal getR65_bob_total_amount() {
			return r65_bob_total_amount;
		}

		public void setR65_bob_total_amount(BigDecimal r65_bob_total_amount) {
			this.r65_bob_total_amount = r65_bob_total_amount;
		}

		public BigDecimal getR65_bob_with_factor_applied() {
			return r65_bob_with_factor_applied;
		}

		public void setR65_bob_with_factor_applied(BigDecimal r65_bob_with_factor_applied) {
			this.r65_bob_with_factor_applied = r65_bob_with_factor_applied;
		}

		public String getR66_product() {
			return r66_product;
		}

		public void setR66_product(String r66_product) {
			this.r66_product = r66_product;
		}

		public BigDecimal getR66_amount_factor() {
			return r66_amount_factor;
		}

		public void setR66_amount_factor(BigDecimal r66_amount_factor) {
			this.r66_amount_factor = r66_amount_factor;
		}

		public BigDecimal getR66_bob_total_amount() {
			return r66_bob_total_amount;
		}

		public void setR66_bob_total_amount(BigDecimal r66_bob_total_amount) {
			this.r66_bob_total_amount = r66_bob_total_amount;
		}

		public BigDecimal getR66_bob_with_factor_applied() {
			return r66_bob_with_factor_applied;
		}

		public void setR66_bob_with_factor_applied(BigDecimal r66_bob_with_factor_applied) {
			this.r66_bob_with_factor_applied = r66_bob_with_factor_applied;
		}

		public String getR67_product() {
			return r67_product;
		}

		public void setR67_product(String r67_product) {
			this.r67_product = r67_product;
		}

		public BigDecimal getR67_amount_factor() {
			return r67_amount_factor;
		}

		public void setR67_amount_factor(BigDecimal r67_amount_factor) {
			this.r67_amount_factor = r67_amount_factor;
		}

		public BigDecimal getR67_bob_total_amount() {
			return r67_bob_total_amount;
		}

		public void setR67_bob_total_amount(BigDecimal r67_bob_total_amount) {
			this.r67_bob_total_amount = r67_bob_total_amount;
		}

		public BigDecimal getR67_bob_with_factor_applied() {
			return r67_bob_with_factor_applied;
		}

		public void setR67_bob_with_factor_applied(BigDecimal r67_bob_with_factor_applied) {
			this.r67_bob_with_factor_applied = r67_bob_with_factor_applied;
		}

		public String getR68_product() {
			return r68_product;
		}

		public void setR68_product(String r68_product) {
			this.r68_product = r68_product;
		}

		public BigDecimal getR68_amount_factor() {
			return r68_amount_factor;
		}

		public void setR68_amount_factor(BigDecimal r68_amount_factor) {
			this.r68_amount_factor = r68_amount_factor;
		}

		public BigDecimal getR68_bob_total_amount() {
			return r68_bob_total_amount;
		}

		public void setR68_bob_total_amount(BigDecimal r68_bob_total_amount) {
			this.r68_bob_total_amount = r68_bob_total_amount;
		}

		public BigDecimal getR68_bob_with_factor_applied() {
			return r68_bob_with_factor_applied;
		}

		public void setR68_bob_with_factor_applied(BigDecimal r68_bob_with_factor_applied) {
			this.r68_bob_with_factor_applied = r68_bob_with_factor_applied;
		}

		public String getR69_product() {
			return r69_product;
		}

		public void setR69_product(String r69_product) {
			this.r69_product = r69_product;
		}

		public BigDecimal getR69_amount_factor() {
			return r69_amount_factor;
		}

		public void setR69_amount_factor(BigDecimal r69_amount_factor) {
			this.r69_amount_factor = r69_amount_factor;
		}

		public BigDecimal getR69_bob_total_amount() {
			return r69_bob_total_amount;
		}

		public void setR69_bob_total_amount(BigDecimal r69_bob_total_amount) {
			this.r69_bob_total_amount = r69_bob_total_amount;
		}

		public BigDecimal getR69_bob_with_factor_applied() {
			return r69_bob_with_factor_applied;
		}

		public void setR69_bob_with_factor_applied(BigDecimal r69_bob_with_factor_applied) {
			this.r69_bob_with_factor_applied = r69_bob_with_factor_applied;
		}

		public String getR70_product() {
			return r70_product;
		}

		public void setR70_product(String r70_product) {
			this.r70_product = r70_product;
		}

		public BigDecimal getR70_amount_factor() {
			return r70_amount_factor;
		}

		public void setR70_amount_factor(BigDecimal r70_amount_factor) {
			this.r70_amount_factor = r70_amount_factor;
		}

		public BigDecimal getR70_bob_total_amount() {
			return r70_bob_total_amount;
		}

		public void setR70_bob_total_amount(BigDecimal r70_bob_total_amount) {
			this.r70_bob_total_amount = r70_bob_total_amount;
		}

		public BigDecimal getR70_bob_with_factor_applied() {
			return r70_bob_with_factor_applied;
		}

		public void setR70_bob_with_factor_applied(BigDecimal r70_bob_with_factor_applied) {
			this.r70_bob_with_factor_applied = r70_bob_with_factor_applied;
		}

		public String getR72_product() {
			return r72_product;
		}

		public void setR72_product(String r72_product) {
			this.r72_product = r72_product;
		}

		public BigDecimal getR72_amount_factor() {
			return r72_amount_factor;
		}

		public void setR72_amount_factor(BigDecimal r72_amount_factor) {
			this.r72_amount_factor = r72_amount_factor;
		}

		public BigDecimal getR72_bob_total_amount() {
			return r72_bob_total_amount;
		}

		public void setR72_bob_total_amount(BigDecimal r72_bob_total_amount) {
			this.r72_bob_total_amount = r72_bob_total_amount;
		}

		public BigDecimal getR72_bob_with_factor_applied() {
			return r72_bob_with_factor_applied;
		}

		public void setR72_bob_with_factor_applied(BigDecimal r72_bob_with_factor_applied) {
			this.r72_bob_with_factor_applied = r72_bob_with_factor_applied;
		}

		public String getR73_product() {
			return r73_product;
		}

		public void setR73_product(String r73_product) {
			this.r73_product = r73_product;
		}

		public BigDecimal getR73_amount_factor() {
			return r73_amount_factor;
		}

		public void setR73_amount_factor(BigDecimal r73_amount_factor) {
			this.r73_amount_factor = r73_amount_factor;
		}

		public BigDecimal getR73_bob_total_amount() {
			return r73_bob_total_amount;
		}

		public void setR73_bob_total_amount(BigDecimal r73_bob_total_amount) {
			this.r73_bob_total_amount = r73_bob_total_amount;
		}

		public BigDecimal getR73_bob_with_factor_applied() {
			return r73_bob_with_factor_applied;
		}

		public void setR73_bob_with_factor_applied(BigDecimal r73_bob_with_factor_applied) {
			this.r73_bob_with_factor_applied = r73_bob_with_factor_applied;
		}

		public String getR74_product() {
			return r74_product;
		}

		public void setR74_product(String r74_product) {
			this.r74_product = r74_product;
		}

		public BigDecimal getR74_amount_factor() {
			return r74_amount_factor;
		}

		public void setR74_amount_factor(BigDecimal r74_amount_factor) {
			this.r74_amount_factor = r74_amount_factor;
		}

		public BigDecimal getR74_bob_total_amount() {
			return r74_bob_total_amount;
		}

		public void setR74_bob_total_amount(BigDecimal r74_bob_total_amount) {
			this.r74_bob_total_amount = r74_bob_total_amount;
		}

		public BigDecimal getR74_bob_with_factor_applied() {
			return r74_bob_with_factor_applied;
		}

		public void setR74_bob_with_factor_applied(BigDecimal r74_bob_with_factor_applied) {
			this.r74_bob_with_factor_applied = r74_bob_with_factor_applied;
		}

		public String getR75_product() {
			return r75_product;
		}

		public void setR75_product(String r75_product) {
			this.r75_product = r75_product;
		}

		public BigDecimal getR75_amount_factor() {
			return r75_amount_factor;
		}

		public void setR75_amount_factor(BigDecimal r75_amount_factor) {
			this.r75_amount_factor = r75_amount_factor;
		}

		public BigDecimal getR75_bob_total_amount() {
			return r75_bob_total_amount;
		}

		public void setR75_bob_total_amount(BigDecimal r75_bob_total_amount) {
			this.r75_bob_total_amount = r75_bob_total_amount;
		}

		public BigDecimal getR75_bob_with_factor_applied() {
			return r75_bob_with_factor_applied;
		}

		public void setR75_bob_with_factor_applied(BigDecimal r75_bob_with_factor_applied) {
			this.r75_bob_with_factor_applied = r75_bob_with_factor_applied;
		}

		public String getR76_product() {
			return r76_product;
		}

		public void setR76_product(String r76_product) {
			this.r76_product = r76_product;
		}

		public BigDecimal getR76_amount_factor() {
			return r76_amount_factor;
		}

		public void setR76_amount_factor(BigDecimal r76_amount_factor) {
			this.r76_amount_factor = r76_amount_factor;
		}

		public BigDecimal getR76_bob_total_amount() {
			return r76_bob_total_amount;
		}

		public void setR76_bob_total_amount(BigDecimal r76_bob_total_amount) {
			this.r76_bob_total_amount = r76_bob_total_amount;
		}

		public BigDecimal getR76_bob_with_factor_applied() {
			return r76_bob_with_factor_applied;
		}

		public void setR76_bob_with_factor_applied(BigDecimal r76_bob_with_factor_applied) {
			this.r76_bob_with_factor_applied = r76_bob_with_factor_applied;
		}

		public String getR77_product() {
			return r77_product;
		}

		public void setR77_product(String r77_product) {
			this.r77_product = r77_product;
		}

		public BigDecimal getR77_amount_factor() {
			return r77_amount_factor;
		}

		public void setR77_amount_factor(BigDecimal r77_amount_factor) {
			this.r77_amount_factor = r77_amount_factor;
		}

		public BigDecimal getR77_bob_total_amount() {
			return r77_bob_total_amount;
		}

		public void setR77_bob_total_amount(BigDecimal r77_bob_total_amount) {
			this.r77_bob_total_amount = r77_bob_total_amount;
		}

		public BigDecimal getR77_bob_with_factor_applied() {
			return r77_bob_with_factor_applied;
		}

		public void setR77_bob_with_factor_applied(BigDecimal r77_bob_with_factor_applied) {
			this.r77_bob_with_factor_applied = r77_bob_with_factor_applied;
		}

		public String getR78_product() {
			return r78_product;
		}

		public void setR78_product(String r78_product) {
			this.r78_product = r78_product;
		}

		public BigDecimal getR78_amount_factor() {
			return r78_amount_factor;
		}

		public void setR78_amount_factor(BigDecimal r78_amount_factor) {
			this.r78_amount_factor = r78_amount_factor;
		}

		public BigDecimal getR78_bob_total_amount() {
			return r78_bob_total_amount;
		}

		public void setR78_bob_total_amount(BigDecimal r78_bob_total_amount) {
			this.r78_bob_total_amount = r78_bob_total_amount;
		}

		public BigDecimal getR78_bob_with_factor_applied() {
			return r78_bob_with_factor_applied;
		}

		public void setR78_bob_with_factor_applied(BigDecimal r78_bob_with_factor_applied) {
			this.r78_bob_with_factor_applied = r78_bob_with_factor_applied;
		}

		public String getR79_product() {
			return r79_product;
		}

		public void setR79_product(String r79_product) {
			this.r79_product = r79_product;
		}

		public BigDecimal getR79_amount_factor() {
			return r79_amount_factor;
		}

		public void setR79_amount_factor(BigDecimal r79_amount_factor) {
			this.r79_amount_factor = r79_amount_factor;
		}

		public BigDecimal getR79_bob_total_amount() {
			return r79_bob_total_amount;
		}

		public void setR79_bob_total_amount(BigDecimal r79_bob_total_amount) {
			this.r79_bob_total_amount = r79_bob_total_amount;
		}

		public BigDecimal getR79_bob_with_factor_applied() {
			return r79_bob_with_factor_applied;
		}

		public void setR79_bob_with_factor_applied(BigDecimal r79_bob_with_factor_applied) {
			this.r79_bob_with_factor_applied = r79_bob_with_factor_applied;
		}

		public String getR80_product() {
			return r80_product;
		}

		public void setR80_product(String r80_product) {
			this.r80_product = r80_product;
		}

		public BigDecimal getR80_amount_factor() {
			return r80_amount_factor;
		}

		public void setR80_amount_factor(BigDecimal r80_amount_factor) {
			this.r80_amount_factor = r80_amount_factor;
		}

		public BigDecimal getR80_bob_total_amount() {
			return r80_bob_total_amount;
		}

		public void setR80_bob_total_amount(BigDecimal r80_bob_total_amount) {
			this.r80_bob_total_amount = r80_bob_total_amount;
		}

		public BigDecimal getR80_bob_with_factor_applied() {
			return r80_bob_with_factor_applied;
		}

		public void setR80_bob_with_factor_applied(BigDecimal r80_bob_with_factor_applied) {
			this.r80_bob_with_factor_applied = r80_bob_with_factor_applied;
		}

		public String getR81_product() {
			return r81_product;
		}

		public void setR81_product(String r81_product) {
			this.r81_product = r81_product;
		}

		public BigDecimal getR81_amount_factor() {
			return r81_amount_factor;
		}

		public void setR81_amount_factor(BigDecimal r81_amount_factor) {
			this.r81_amount_factor = r81_amount_factor;
		}

		public BigDecimal getR81_bob_total_amount() {
			return r81_bob_total_amount;
		}

		public void setR81_bob_total_amount(BigDecimal r81_bob_total_amount) {
			this.r81_bob_total_amount = r81_bob_total_amount;
		}

		public BigDecimal getR81_bob_with_factor_applied() {
			return r81_bob_with_factor_applied;
		}

		public void setR81_bob_with_factor_applied(BigDecimal r81_bob_with_factor_applied) {
			this.r81_bob_with_factor_applied = r81_bob_with_factor_applied;
		}

		public String getR82_product() {
			return r82_product;
		}

		public void setR82_product(String r82_product) {
			this.r82_product = r82_product;
		}

		public BigDecimal getR82_amount_factor() {
			return r82_amount_factor;
		}

		public void setR82_amount_factor(BigDecimal r82_amount_factor) {
			this.r82_amount_factor = r82_amount_factor;
		}

		public BigDecimal getR82_bob_total_amount() {
			return r82_bob_total_amount;
		}

		public void setR82_bob_total_amount(BigDecimal r82_bob_total_amount) {
			this.r82_bob_total_amount = r82_bob_total_amount;
		}

		public BigDecimal getR82_bob_with_factor_applied() {
			return r82_bob_with_factor_applied;
		}

		public void setR82_bob_with_factor_applied(BigDecimal r82_bob_with_factor_applied) {
			this.r82_bob_with_factor_applied = r82_bob_with_factor_applied;
		}

		public String getR83_product() {
			return r83_product;
		}

		public void setR83_product(String r83_product) {
			this.r83_product = r83_product;
		}

		public BigDecimal getR83_amount_factor() {
			return r83_amount_factor;
		}

		public void setR83_amount_factor(BigDecimal r83_amount_factor) {
			this.r83_amount_factor = r83_amount_factor;
		}

		public BigDecimal getR83_bob_total_amount() {
			return r83_bob_total_amount;
		}

		public void setR83_bob_total_amount(BigDecimal r83_bob_total_amount) {
			this.r83_bob_total_amount = r83_bob_total_amount;
		}

		public BigDecimal getR83_bob_with_factor_applied() {
			return r83_bob_with_factor_applied;
		}

		public void setR83_bob_with_factor_applied(BigDecimal r83_bob_with_factor_applied) {
			this.r83_bob_with_factor_applied = r83_bob_with_factor_applied;
		}

		public String getR84_product() {
			return r84_product;
		}

		public void setR84_product(String r84_product) {
			this.r84_product = r84_product;
		}

		public BigDecimal getR84_amount_factor() {
			return r84_amount_factor;
		}

		public void setR84_amount_factor(BigDecimal r84_amount_factor) {
			this.r84_amount_factor = r84_amount_factor;
		}

		public BigDecimal getR84_bob_total_amount() {
			return r84_bob_total_amount;
		}

		public void setR84_bob_total_amount(BigDecimal r84_bob_total_amount) {
			this.r84_bob_total_amount = r84_bob_total_amount;
		}

		public BigDecimal getR84_bob_with_factor_applied() {
			return r84_bob_with_factor_applied;
		}

		public void setR84_bob_with_factor_applied(BigDecimal r84_bob_with_factor_applied) {
			this.r84_bob_with_factor_applied = r84_bob_with_factor_applied;
		}

		public String getR85_product() {
			return r85_product;
		}

		public void setR85_product(String r85_product) {
			this.r85_product = r85_product;
		}

		public BigDecimal getR85_amount_factor() {
			return r85_amount_factor;
		}

		public void setR85_amount_factor(BigDecimal r85_amount_factor) {
			this.r85_amount_factor = r85_amount_factor;
		}

		public BigDecimal getR85_bob_total_amount() {
			return r85_bob_total_amount;
		}

		public void setR85_bob_total_amount(BigDecimal r85_bob_total_amount) {
			this.r85_bob_total_amount = r85_bob_total_amount;
		}

		public BigDecimal getR85_bob_with_factor_applied() {
			return r85_bob_with_factor_applied;
		}

		public void setR85_bob_with_factor_applied(BigDecimal r85_bob_with_factor_applied) {
			this.r85_bob_with_factor_applied = r85_bob_with_factor_applied;
		}

		public String getR86_product() {
			return r86_product;
		}

		public void setR86_product(String r86_product) {
			this.r86_product = r86_product;
		}

		public BigDecimal getR86_amount_factor() {
			return r86_amount_factor;
		}

		public void setR86_amount_factor(BigDecimal r86_amount_factor) {
			this.r86_amount_factor = r86_amount_factor;
		}

		public BigDecimal getR86_bob_total_amount() {
			return r86_bob_total_amount;
		}

		public void setR86_bob_total_amount(BigDecimal r86_bob_total_amount) {
			this.r86_bob_total_amount = r86_bob_total_amount;
		}

		public BigDecimal getR86_bob_with_factor_applied() {
			return r86_bob_with_factor_applied;
		}

		public void setR86_bob_with_factor_applied(BigDecimal r86_bob_with_factor_applied) {
			this.r86_bob_with_factor_applied = r86_bob_with_factor_applied;
		}

		public String getR87_product() {
			return r87_product;
		}

		public void setR87_product(String r87_product) {
			this.r87_product = r87_product;
		}

		public BigDecimal getR87_amount_factor() {
			return r87_amount_factor;
		}

		public void setR87_amount_factor(BigDecimal r87_amount_factor) {
			this.r87_amount_factor = r87_amount_factor;
		}

		public BigDecimal getR87_bob_total_amount() {
			return r87_bob_total_amount;
		}

		public void setR87_bob_total_amount(BigDecimal r87_bob_total_amount) {
			this.r87_bob_total_amount = r87_bob_total_amount;
		}

		public BigDecimal getR87_bob_with_factor_applied() {
			return r87_bob_with_factor_applied;
		}

		public void setR87_bob_with_factor_applied(BigDecimal r87_bob_with_factor_applied) {
			this.r87_bob_with_factor_applied = r87_bob_with_factor_applied;
		}

		public String getR88_product() {
			return r88_product;
		}

		public void setR88_product(String r88_product) {
			this.r88_product = r88_product;
		}

		public BigDecimal getR88_amount_factor() {
			return r88_amount_factor;
		}

		public void setR88_amount_factor(BigDecimal r88_amount_factor) {
			this.r88_amount_factor = r88_amount_factor;
		}

		public BigDecimal getR88_bob_total_amount() {
			return r88_bob_total_amount;
		}

		public void setR88_bob_total_amount(BigDecimal r88_bob_total_amount) {
			this.r88_bob_total_amount = r88_bob_total_amount;
		}

		public BigDecimal getR88_bob_with_factor_applied() {
			return r88_bob_with_factor_applied;
		}

		public void setR88_bob_with_factor_applied(BigDecimal r88_bob_with_factor_applied) {
			this.r88_bob_with_factor_applied = r88_bob_with_factor_applied;
		}

		public String getR89_product() {
			return r89_product;
		}

		public void setR89_product(String r89_product) {
			this.r89_product = r89_product;
		}

		public BigDecimal getR89_amount_factor() {
			return r89_amount_factor;
		}

		public void setR89_amount_factor(BigDecimal r89_amount_factor) {
			this.r89_amount_factor = r89_amount_factor;
		}

		public BigDecimal getR89_bob_total_amount() {
			return r89_bob_total_amount;
		}

		public void setR89_bob_total_amount(BigDecimal r89_bob_total_amount) {
			this.r89_bob_total_amount = r89_bob_total_amount;
		}

		public BigDecimal getR89_bob_with_factor_applied() {
			return r89_bob_with_factor_applied;
		}

		public void setR89_bob_with_factor_applied(BigDecimal r89_bob_with_factor_applied) {
			this.r89_bob_with_factor_applied = r89_bob_with_factor_applied;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
			this.REPORT_RESUBDATE = REPORT_RESUBDATE;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String REPORT_FREQUENCY) {
			this.REPORT_FREQUENCY = REPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String REPORT_CODE) {
			this.REPORT_CODE = REPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String REPORT_DESC) {
			this.REPORT_DESC = REPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String ENTITY_FLG) {
			this.ENTITY_FLG = ENTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String MODIFY_FLG) {
			this.MODIFY_FLG = MODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String DEL_FLG) {
			this.DEL_FLG = DEL_FLG;
		}
	}

	// 2.4 ARCHIVAL DETAIL ENTITY
	public static class M_LCR_Archival_Detail_Entity {
		private String CUST_ID;
		private String ACCT_NUMBER;
		private String ACCT_NAME;
		private BigDecimal ACCT_BALANCE_IN_PULA;
		private String REPORT_LABEL;
		private String REPORT_ADDL_CRITERIA_1;
		private BigDecimal DEBITEQUIVALENT;
		private BigDecimal EMI;
		private BigDecimal CREDITEQUIVALENT;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_RESUBDATE")
		private Date REPORT_RESUBDATE;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		// Getters and Setters
		public String getCUST_ID() {
			return CUST_ID;
		}

		public void setCUST_ID(String CUST_ID) {
			this.CUST_ID = CUST_ID;
		}

		public String getACCT_NUMBER() {
			return ACCT_NUMBER;
		}

		public void setACCT_NUMBER(String ACCT_NUMBER) {
			this.ACCT_NUMBER = ACCT_NUMBER;
		}

		public String getACCT_NAME() {
			return ACCT_NAME;
		}

		public void setACCT_NAME(String ACCT_NAME) {
			this.ACCT_NAME = ACCT_NAME;
		}

		public BigDecimal getACCT_BALANCE_IN_PULA() {
			return ACCT_BALANCE_IN_PULA;
		}

		public void setACCT_BALANCE_IN_PULA(BigDecimal ACCT_BALANCE_IN_PULA) {
			this.ACCT_BALANCE_IN_PULA = ACCT_BALANCE_IN_PULA;
		}

		public String getREPORT_LABEL() {
			return REPORT_LABEL;
		}

		public void setREPORT_LABEL(String REPORT_LABEL) {
			this.REPORT_LABEL = REPORT_LABEL;
		}

		public String getREPORT_ADDL_CRITERIA_1() {
			return REPORT_ADDL_CRITERIA_1;
		}

		public void setREPORT_ADDL_CRITERIA_1(String REPORT_ADDL_CRITERIA_1) {
			this.REPORT_ADDL_CRITERIA_1 = REPORT_ADDL_CRITERIA_1;
		}

		public BigDecimal getDEBITEQUIVALENT() {
			return DEBITEQUIVALENT;
		}

		public void setDEBITEQUIVALENT(BigDecimal DEBITEQUIVALENT) {
			this.DEBITEQUIVALENT = DEBITEQUIVALENT;
		}

		public BigDecimal getEMI() {
			return EMI;
		}

		public void setEMI(BigDecimal EMI) {
			this.EMI = EMI;
		}

		public BigDecimal getCREDITEQUIVALENT() {
			return CREDITEQUIVALENT;
		}

		public void setCREDITEQUIVALENT(BigDecimal CREDITEQUIVALENT) {
			this.CREDITEQUIVALENT = CREDITEQUIVALENT;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
			this.REPORT_RESUBDATE = REPORT_RESUBDATE;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String REPORT_FREQUENCY) {
			this.REPORT_FREQUENCY = REPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String REPORT_CODE) {
			this.REPORT_CODE = REPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String REPORT_DESC) {
			this.REPORT_DESC = REPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String ENTITY_FLG) {
			this.ENTITY_FLG = ENTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String MODIFY_FLG) {
			this.MODIFY_FLG = MODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String DEL_FLG) {
			this.DEL_FLG = DEL_FLG;
		}
	}

	// ===========================================================
	// 3. ROW MAPPER CLASSES
	// ===========================================================

	// 3.1 SUMMARY ROW MAPPER
	class M_LCR_RowMapper_Summary implements RowMapper<M_LCR_Summary_Entity> {
		@Override
		public M_LCR_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_LCR_Summary_Entity obj = new M_LCR_Summary_Entity();

			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount_factor(rs.getBigDecimal("R11_AMOUNT_FACTOR"));
			obj.setR11_bob_total_amount(rs.getBigDecimal("R11_BOB_TOTAL_AMOUNT"));
			obj.setR11_bob_with_factor_applied(rs.getBigDecimal("R11_BOB_WITH_FACTOR_APPLIED"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount_factor(rs.getBigDecimal("R12_AMOUNT_FACTOR"));
			obj.setR12_bob_total_amount(rs.getBigDecimal("R12_BOB_TOTAL_AMOUNT"));
			obj.setR12_bob_with_factor_applied(rs.getBigDecimal("R12_BOB_WITH_FACTOR_APPLIED"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount_factor(rs.getBigDecimal("R13_AMOUNT_FACTOR"));
			obj.setR13_bob_total_amount(rs.getBigDecimal("R13_BOB_TOTAL_AMOUNT"));
			obj.setR13_bob_with_factor_applied(rs.getBigDecimal("R13_BOB_WITH_FACTOR_APPLIED"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount_factor(rs.getBigDecimal("R14_AMOUNT_FACTOR"));
			obj.setR14_bob_total_amount(rs.getBigDecimal("R14_BOB_TOTAL_AMOUNT"));
			obj.setR14_bob_with_factor_applied(rs.getBigDecimal("R14_BOB_WITH_FACTOR_APPLIED"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount_factor(rs.getBigDecimal("R15_AMOUNT_FACTOR"));
			obj.setR15_bob_total_amount(rs.getBigDecimal("R15_BOB_TOTAL_AMOUNT"));
			obj.setR15_bob_with_factor_applied(rs.getBigDecimal("R15_BOB_WITH_FACTOR_APPLIED"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_amount_factor(rs.getBigDecimal("R16_AMOUNT_FACTOR"));
			obj.setR16_bob_total_amount(rs.getBigDecimal("R16_BOB_TOTAL_AMOUNT"));
			obj.setR16_bob_with_factor_applied(rs.getBigDecimal("R16_BOB_WITH_FACTOR_APPLIED"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_amount_factor(rs.getBigDecimal("R17_AMOUNT_FACTOR"));
			obj.setR17_bob_total_amount(rs.getBigDecimal("R17_BOB_TOTAL_AMOUNT"));
			obj.setR17_bob_with_factor_applied(rs.getBigDecimal("R17_BOB_WITH_FACTOR_APPLIED"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_amount_factor(rs.getBigDecimal("R18_AMOUNT_FACTOR"));
			obj.setR18_bob_total_amount(rs.getBigDecimal("R18_BOB_TOTAL_AMOUNT"));
			obj.setR18_bob_with_factor_applied(rs.getBigDecimal("R18_BOB_WITH_FACTOR_APPLIED"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_amount_factor(rs.getBigDecimal("R19_AMOUNT_FACTOR"));
			obj.setR19_bob_total_amount(rs.getBigDecimal("R19_BOB_TOTAL_AMOUNT"));
			obj.setR19_bob_with_factor_applied(rs.getBigDecimal("R19_BOB_WITH_FACTOR_APPLIED"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_amount_factor(rs.getBigDecimal("R20_AMOUNT_FACTOR"));
			obj.setR20_bob_total_amount(rs.getBigDecimal("R20_BOB_TOTAL_AMOUNT"));
			obj.setR20_bob_with_factor_applied(rs.getBigDecimal("R20_BOB_WITH_FACTOR_APPLIED"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_amount_factor(rs.getBigDecimal("R21_AMOUNT_FACTOR"));
			obj.setR21_bob_total_amount(rs.getBigDecimal("R21_BOB_TOTAL_AMOUNT"));
			obj.setR21_bob_with_factor_applied(rs.getBigDecimal("R21_BOB_WITH_FACTOR_APPLIED"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_amount_factor(rs.getBigDecimal("R22_AMOUNT_FACTOR"));
			obj.setR22_bob_total_amount(rs.getBigDecimal("R22_BOB_TOTAL_AMOUNT"));
			obj.setR22_bob_with_factor_applied(rs.getBigDecimal("R22_BOB_WITH_FACTOR_APPLIED"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_amount_factor(rs.getBigDecimal("R23_AMOUNT_FACTOR"));
			obj.setR23_bob_total_amount(rs.getBigDecimal("R23_BOB_TOTAL_AMOUNT"));
			obj.setR23_bob_with_factor_applied(rs.getBigDecimal("R23_BOB_WITH_FACTOR_APPLIED"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_amount_factor(rs.getBigDecimal("R24_AMOUNT_FACTOR"));
			obj.setR24_bob_total_amount(rs.getBigDecimal("R24_BOB_TOTAL_AMOUNT"));
			obj.setR24_bob_with_factor_applied(rs.getBigDecimal("R24_BOB_WITH_FACTOR_APPLIED"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_amount_factor(rs.getBigDecimal("R26_AMOUNT_FACTOR"));
			obj.setR26_bob_total_amount(rs.getBigDecimal("R26_BOB_TOTAL_AMOUNT"));
			obj.setR26_bob_with_factor_applied(rs.getBigDecimal("R26_BOB_WITH_FACTOR_APPLIED"));
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_amount_factor(rs.getBigDecimal("R27_AMOUNT_FACTOR"));
			obj.setR27_bob_total_amount(rs.getBigDecimal("R27_BOB_TOTAL_AMOUNT"));
			obj.setR27_bob_with_factor_applied(rs.getBigDecimal("R27_BOB_WITH_FACTOR_APPLIED"));
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_amount_factor(rs.getBigDecimal("R28_AMOUNT_FACTOR"));
			obj.setR28_bob_total_amount(rs.getBigDecimal("R28_BOB_TOTAL_AMOUNT"));
			obj.setR28_bob_with_factor_applied(rs.getBigDecimal("R28_BOB_WITH_FACTOR_APPLIED"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_amount_factor(rs.getBigDecimal("R29_AMOUNT_FACTOR"));
			obj.setR29_bob_total_amount(rs.getBigDecimal("R29_BOB_TOTAL_AMOUNT"));
			obj.setR29_bob_with_factor_applied(rs.getBigDecimal("R29_BOB_WITH_FACTOR_APPLIED"));
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_amount_factor(rs.getBigDecimal("R30_AMOUNT_FACTOR"));
			obj.setR30_bob_total_amount(rs.getBigDecimal("R30_BOB_TOTAL_AMOUNT"));
			obj.setR30_bob_with_factor_applied(rs.getBigDecimal("R30_BOB_WITH_FACTOR_APPLIED"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_amount_factor(rs.getBigDecimal("R31_AMOUNT_FACTOR"));
			obj.setR31_bob_total_amount(rs.getBigDecimal("R31_BOB_TOTAL_AMOUNT"));
			obj.setR31_bob_with_factor_applied(rs.getBigDecimal("R31_BOB_WITH_FACTOR_APPLIED"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_amount_factor(rs.getBigDecimal("R32_AMOUNT_FACTOR"));
			obj.setR32_bob_total_amount(rs.getBigDecimal("R32_BOB_TOTAL_AMOUNT"));
			obj.setR32_bob_with_factor_applied(rs.getBigDecimal("R32_BOB_WITH_FACTOR_APPLIED"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_amount_factor(rs.getBigDecimal("R33_AMOUNT_FACTOR"));
			obj.setR33_bob_total_amount(rs.getBigDecimal("R33_BOB_TOTAL_AMOUNT"));
			obj.setR33_bob_with_factor_applied(rs.getBigDecimal("R33_BOB_WITH_FACTOR_APPLIED"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_amount_factor(rs.getBigDecimal("R34_AMOUNT_FACTOR"));
			obj.setR34_bob_total_amount(rs.getBigDecimal("R34_BOB_TOTAL_AMOUNT"));
			obj.setR34_bob_with_factor_applied(rs.getBigDecimal("R34_BOB_WITH_FACTOR_APPLIED"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_amount_factor(rs.getBigDecimal("R35_AMOUNT_FACTOR"));
			obj.setR35_bob_total_amount(rs.getBigDecimal("R35_BOB_TOTAL_AMOUNT"));
			obj.setR35_bob_with_factor_applied(rs.getBigDecimal("R35_BOB_WITH_FACTOR_APPLIED"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_amount_factor(rs.getBigDecimal("R36_AMOUNT_FACTOR"));
			obj.setR36_bob_total_amount(rs.getBigDecimal("R36_BOB_TOTAL_AMOUNT"));
			obj.setR36_bob_with_factor_applied(rs.getBigDecimal("R36_BOB_WITH_FACTOR_APPLIED"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_amount_factor(rs.getBigDecimal("R37_AMOUNT_FACTOR"));
			obj.setR37_bob_total_amount(rs.getBigDecimal("R37_BOB_TOTAL_AMOUNT"));
			obj.setR37_bob_with_factor_applied(rs.getBigDecimal("R37_BOB_WITH_FACTOR_APPLIED"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_amount_factor(rs.getBigDecimal("R38_AMOUNT_FACTOR"));
			obj.setR38_bob_total_amount(rs.getBigDecimal("R38_BOB_TOTAL_AMOUNT"));
			obj.setR38_bob_with_factor_applied(rs.getBigDecimal("R38_BOB_WITH_FACTOR_APPLIED"));
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_amount_factor(rs.getBigDecimal("R39_AMOUNT_FACTOR"));
			obj.setR39_bob_total_amount(rs.getBigDecimal("R39_BOB_TOTAL_AMOUNT"));
			obj.setR39_bob_with_factor_applied(rs.getBigDecimal("R39_BOB_WITH_FACTOR_APPLIED"));
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_amount_factor(rs.getBigDecimal("R40_AMOUNT_FACTOR"));
			obj.setR40_bob_total_amount(rs.getBigDecimal("R40_BOB_TOTAL_AMOUNT"));
			obj.setR40_bob_with_factor_applied(rs.getBigDecimal("R40_BOB_WITH_FACTOR_APPLIED"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_amount_factor(rs.getBigDecimal("R41_AMOUNT_FACTOR"));
			obj.setR41_bob_total_amount(rs.getBigDecimal("R41_BOB_TOTAL_AMOUNT"));
			obj.setR41_bob_with_factor_applied(rs.getBigDecimal("R41_BOB_WITH_FACTOR_APPLIED"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_amount_factor(rs.getBigDecimal("R42_AMOUNT_FACTOR"));
			obj.setR42_bob_total_amount(rs.getBigDecimal("R42_BOB_TOTAL_AMOUNT"));
			obj.setR42_bob_with_factor_applied(rs.getBigDecimal("R42_BOB_WITH_FACTOR_APPLIED"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_amount_factor(rs.getBigDecimal("R43_AMOUNT_FACTOR"));
			obj.setR43_bob_total_amount(rs.getBigDecimal("R43_BOB_TOTAL_AMOUNT"));
			obj.setR43_bob_with_factor_applied(rs.getBigDecimal("R43_BOB_WITH_FACTOR_APPLIED"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_amount_factor(rs.getBigDecimal("R44_AMOUNT_FACTOR"));
			obj.setR44_bob_total_amount(rs.getBigDecimal("R44_BOB_TOTAL_AMOUNT"));
			obj.setR44_bob_with_factor_applied(rs.getBigDecimal("R44_BOB_WITH_FACTOR_APPLIED"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_amount_factor(rs.getBigDecimal("R45_AMOUNT_FACTOR"));
			obj.setR45_bob_total_amount(rs.getBigDecimal("R45_BOB_TOTAL_AMOUNT"));
			obj.setR45_bob_with_factor_applied(rs.getBigDecimal("R45_BOB_WITH_FACTOR_APPLIED"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_amount_factor(rs.getBigDecimal("R46_AMOUNT_FACTOR"));
			obj.setR46_bob_total_amount(rs.getBigDecimal("R46_BOB_TOTAL_AMOUNT"));
			obj.setR46_bob_with_factor_applied(rs.getBigDecimal("R46_BOB_WITH_FACTOR_APPLIED"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_amount_factor(rs.getBigDecimal("R47_AMOUNT_FACTOR"));
			obj.setR47_bob_total_amount(rs.getBigDecimal("R47_BOB_TOTAL_AMOUNT"));
			obj.setR47_bob_with_factor_applied(rs.getBigDecimal("R47_BOB_WITH_FACTOR_APPLIED"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_amount_factor(rs.getBigDecimal("R48_AMOUNT_FACTOR"));
			obj.setR48_bob_total_amount(rs.getBigDecimal("R48_BOB_TOTAL_AMOUNT"));
			obj.setR48_bob_with_factor_applied(rs.getBigDecimal("R48_BOB_WITH_FACTOR_APPLIED"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_amount_factor(rs.getBigDecimal("R49_AMOUNT_FACTOR"));
			obj.setR49_bob_total_amount(rs.getBigDecimal("R49_BOB_TOTAL_AMOUNT"));
			obj.setR49_bob_with_factor_applied(rs.getBigDecimal("R49_BOB_WITH_FACTOR_APPLIED"));
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_amount_factor(rs.getBigDecimal("R50_AMOUNT_FACTOR"));
			obj.setR50_bob_total_amount(rs.getBigDecimal("R50_BOB_TOTAL_AMOUNT"));
			obj.setR50_bob_with_factor_applied(rs.getBigDecimal("R50_BOB_WITH_FACTOR_APPLIED"));
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_amount_factor(rs.getBigDecimal("R51_AMOUNT_FACTOR"));
			obj.setR51_bob_total_amount(rs.getBigDecimal("R51_BOB_TOTAL_AMOUNT"));
			obj.setR51_bob_with_factor_applied(rs.getBigDecimal("R51_BOB_WITH_FACTOR_APPLIED"));
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_amount_factor(rs.getBigDecimal("R52_AMOUNT_FACTOR"));
			obj.setR52_bob_total_amount(rs.getBigDecimal("R52_BOB_TOTAL_AMOUNT"));
			obj.setR52_bob_with_factor_applied(rs.getBigDecimal("R52_BOB_WITH_FACTOR_APPLIED"));
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_amount_factor(rs.getBigDecimal("R53_AMOUNT_FACTOR"));
			obj.setR53_bob_total_amount(rs.getBigDecimal("R53_BOB_TOTAL_AMOUNT"));
			obj.setR53_bob_with_factor_applied(rs.getBigDecimal("R53_BOB_WITH_FACTOR_APPLIED"));
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_amount_factor(rs.getBigDecimal("R54_AMOUNT_FACTOR"));
			obj.setR54_bob_total_amount(rs.getBigDecimal("R54_BOB_TOTAL_AMOUNT"));
			obj.setR54_bob_with_factor_applied(rs.getBigDecimal("R54_BOB_WITH_FACTOR_APPLIED"));
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_amount_factor(rs.getBigDecimal("R55_AMOUNT_FACTOR"));
			obj.setR55_bob_total_amount(rs.getBigDecimal("R55_BOB_TOTAL_AMOUNT"));
			obj.setR55_bob_with_factor_applied(rs.getBigDecimal("R55_BOB_WITH_FACTOR_APPLIED"));
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_amount_factor(rs.getBigDecimal("R56_AMOUNT_FACTOR"));
			obj.setR56_bob_total_amount(rs.getBigDecimal("R56_BOB_TOTAL_AMOUNT"));
			obj.setR56_bob_with_factor_applied(rs.getBigDecimal("R56_BOB_WITH_FACTOR_APPLIED"));
			obj.setR57_product(rs.getString("R57_PRODUCT"));
			obj.setR57_amount_factor(rs.getBigDecimal("R57_AMOUNT_FACTOR"));
			obj.setR57_bob_total_amount(rs.getBigDecimal("R57_BOB_TOTAL_AMOUNT"));
			obj.setR57_bob_with_factor_applied(rs.getBigDecimal("R57_BOB_WITH_FACTOR_APPLIED"));
			obj.setR58_product(rs.getString("R58_PRODUCT"));
			obj.setR58_amount_factor(rs.getBigDecimal("R58_AMOUNT_FACTOR"));
			obj.setR58_bob_total_amount(rs.getBigDecimal("R58_BOB_TOTAL_AMOUNT"));
			obj.setR58_bob_with_factor_applied(rs.getBigDecimal("R58_BOB_WITH_FACTOR_APPLIED"));
			obj.setR59_product(rs.getString("R59_PRODUCT"));
			obj.setR59_amount_factor(rs.getBigDecimal("R59_AMOUNT_FACTOR"));
			obj.setR59_bob_total_amount(rs.getBigDecimal("R59_BOB_TOTAL_AMOUNT"));
			obj.setR59_bob_with_factor_applied(rs.getBigDecimal("R59_BOB_WITH_FACTOR_APPLIED"));
			obj.setR60_product(rs.getString("R60_PRODUCT"));
			obj.setR60_amount_factor(rs.getBigDecimal("R60_AMOUNT_FACTOR"));
			obj.setR60_bob_total_amount(rs.getBigDecimal("R60_BOB_TOTAL_AMOUNT"));
			obj.setR60_bob_with_factor_applied(rs.getBigDecimal("R60_BOB_WITH_FACTOR_APPLIED"));
			obj.setR61_product(rs.getString("R61_PRODUCT"));
			obj.setR61_amount_factor(rs.getBigDecimal("R61_AMOUNT_FACTOR"));
			obj.setR61_bob_total_amount(rs.getBigDecimal("R61_BOB_TOTAL_AMOUNT"));
			obj.setR61_bob_with_factor_applied(rs.getBigDecimal("R61_BOB_WITH_FACTOR_APPLIED"));
			obj.setR62_product(rs.getString("R62_PRODUCT"));
			obj.setR62_amount_factor(rs.getBigDecimal("R62_AMOUNT_FACTOR"));
			obj.setR62_bob_total_amount(rs.getBigDecimal("R62_BOB_TOTAL_AMOUNT"));
			obj.setR62_bob_with_factor_applied(rs.getBigDecimal("R62_BOB_WITH_FACTOR_APPLIED"));
			obj.setR63_product(rs.getString("R63_PRODUCT"));
			obj.setR63_amount_factor(rs.getBigDecimal("R63_AMOUNT_FACTOR"));
			obj.setR63_bob_total_amount(rs.getBigDecimal("R63_BOB_TOTAL_AMOUNT"));
			obj.setR63_bob_with_factor_applied(rs.getBigDecimal("R63_BOB_WITH_FACTOR_APPLIED"));
			obj.setR64_product(rs.getString("R64_PRODUCT"));
			obj.setR64_amount_factor(rs.getBigDecimal("R64_AMOUNT_FACTOR"));
			obj.setR64_bob_total_amount(rs.getBigDecimal("R64_BOB_TOTAL_AMOUNT"));
			obj.setR64_bob_with_factor_applied(rs.getBigDecimal("R64_BOB_WITH_FACTOR_APPLIED"));
			obj.setR65_product(rs.getString("R65_PRODUCT"));
			obj.setR65_amount_factor(rs.getBigDecimal("R65_AMOUNT_FACTOR"));
			obj.setR65_bob_total_amount(rs.getBigDecimal("R65_BOB_TOTAL_AMOUNT"));
			obj.setR65_bob_with_factor_applied(rs.getBigDecimal("R65_BOB_WITH_FACTOR_APPLIED"));
			obj.setR66_product(rs.getString("R66_PRODUCT"));
			obj.setR66_amount_factor(rs.getBigDecimal("R66_AMOUNT_FACTOR"));
			obj.setR66_bob_total_amount(rs.getBigDecimal("R66_BOB_TOTAL_AMOUNT"));
			obj.setR66_bob_with_factor_applied(rs.getBigDecimal("R66_BOB_WITH_FACTOR_APPLIED"));
			obj.setR67_product(rs.getString("R67_PRODUCT"));
			obj.setR67_amount_factor(rs.getBigDecimal("R67_AMOUNT_FACTOR"));
			obj.setR67_bob_total_amount(rs.getBigDecimal("R67_BOB_TOTAL_AMOUNT"));
			obj.setR67_bob_with_factor_applied(rs.getBigDecimal("R67_BOB_WITH_FACTOR_APPLIED"));
			obj.setR68_product(rs.getString("R68_PRODUCT"));
			obj.setR68_amount_factor(rs.getBigDecimal("R68_AMOUNT_FACTOR"));
			obj.setR68_bob_total_amount(rs.getBigDecimal("R68_BOB_TOTAL_AMOUNT"));
			obj.setR68_bob_with_factor_applied(rs.getBigDecimal("R68_BOB_WITH_FACTOR_APPLIED"));
			obj.setR69_product(rs.getString("R69_PRODUCT"));
			obj.setR69_amount_factor(rs.getBigDecimal("R69_AMOUNT_FACTOR"));
			obj.setR69_bob_total_amount(rs.getBigDecimal("R69_BOB_TOTAL_AMOUNT"));
			obj.setR69_bob_with_factor_applied(rs.getBigDecimal("R69_BOB_WITH_FACTOR_APPLIED"));
			obj.setR70_product(rs.getString("R70_PRODUCT"));
			obj.setR70_amount_factor(rs.getBigDecimal("R70_AMOUNT_FACTOR"));
			obj.setR70_bob_total_amount(rs.getBigDecimal("R70_BOB_TOTAL_AMOUNT"));
			obj.setR70_bob_with_factor_applied(rs.getBigDecimal("R70_BOB_WITH_FACTOR_APPLIED"));
			obj.setR72_product(rs.getString("R72_PRODUCT"));
			obj.setR72_amount_factor(rs.getBigDecimal("R72_AMOUNT_FACTOR"));
			obj.setR72_bob_total_amount(rs.getBigDecimal("R72_BOB_TOTAL_AMOUNT"));
			obj.setR72_bob_with_factor_applied(rs.getBigDecimal("R72_BOB_WITH_FACTOR_APPLIED"));
			obj.setR73_product(rs.getString("R73_PRODUCT"));
			obj.setR73_amount_factor(rs.getBigDecimal("R73_AMOUNT_FACTOR"));
			obj.setR73_bob_total_amount(rs.getBigDecimal("R73_BOB_TOTAL_AMOUNT"));
			obj.setR73_bob_with_factor_applied(rs.getBigDecimal("R73_BOB_WITH_FACTOR_APPLIED"));
			obj.setR74_product(rs.getString("R74_PRODUCT"));
			obj.setR74_amount_factor(rs.getBigDecimal("R74_AMOUNT_FACTOR"));
			obj.setR74_bob_total_amount(rs.getBigDecimal("R74_BOB_TOTAL_AMOUNT"));
			obj.setR74_bob_with_factor_applied(rs.getBigDecimal("R74_BOB_WITH_FACTOR_APPLIED"));
			obj.setR75_product(rs.getString("R75_PRODUCT"));
			obj.setR75_amount_factor(rs.getBigDecimal("R75_AMOUNT_FACTOR"));
			obj.setR75_bob_total_amount(rs.getBigDecimal("R75_BOB_TOTAL_AMOUNT"));
			obj.setR75_bob_with_factor_applied(rs.getBigDecimal("R75_BOB_WITH_FACTOR_APPLIED"));
			obj.setR76_product(rs.getString("R76_PRODUCT"));
			obj.setR76_amount_factor(rs.getBigDecimal("R76_AMOUNT_FACTOR"));
			obj.setR76_bob_total_amount(rs.getBigDecimal("R76_BOB_TOTAL_AMOUNT"));
			obj.setR76_bob_with_factor_applied(rs.getBigDecimal("R76_BOB_WITH_FACTOR_APPLIED"));
			obj.setR77_product(rs.getString("R77_PRODUCT"));
			obj.setR77_amount_factor(rs.getBigDecimal("R77_AMOUNT_FACTOR"));
			obj.setR77_bob_total_amount(rs.getBigDecimal("R77_BOB_TOTAL_AMOUNT"));
			obj.setR77_bob_with_factor_applied(rs.getBigDecimal("R77_BOB_WITH_FACTOR_APPLIED"));
			obj.setR78_product(rs.getString("R78_PRODUCT"));
			obj.setR78_amount_factor(rs.getBigDecimal("R78_AMOUNT_FACTOR"));
			obj.setR78_bob_total_amount(rs.getBigDecimal("R78_BOB_TOTAL_AMOUNT"));
			obj.setR78_bob_with_factor_applied(rs.getBigDecimal("R78_BOB_WITH_FACTOR_APPLIED"));
			obj.setR79_product(rs.getString("R79_PRODUCT"));
			obj.setR79_amount_factor(rs.getBigDecimal("R79_AMOUNT_FACTOR"));
			obj.setR79_bob_total_amount(rs.getBigDecimal("R79_BOB_TOTAL_AMOUNT"));
			obj.setR79_bob_with_factor_applied(rs.getBigDecimal("R79_BOB_WITH_FACTOR_APPLIED"));
			obj.setR80_product(rs.getString("R80_PRODUCT"));
			obj.setR80_amount_factor(rs.getBigDecimal("R80_AMOUNT_FACTOR"));
			obj.setR80_bob_total_amount(rs.getBigDecimal("R80_BOB_TOTAL_AMOUNT"));
			obj.setR80_bob_with_factor_applied(rs.getBigDecimal("R80_BOB_WITH_FACTOR_APPLIED"));
			obj.setR81_product(rs.getString("R81_PRODUCT"));
			obj.setR81_amount_factor(rs.getBigDecimal("R81_AMOUNT_FACTOR"));
			obj.setR81_bob_total_amount(rs.getBigDecimal("R81_BOB_TOTAL_AMOUNT"));
			obj.setR81_bob_with_factor_applied(rs.getBigDecimal("R81_BOB_WITH_FACTOR_APPLIED"));
			obj.setR82_product(rs.getString("R82_PRODUCT"));
			obj.setR82_amount_factor(rs.getBigDecimal("R82_AMOUNT_FACTOR"));
			obj.setR82_bob_total_amount(rs.getBigDecimal("R82_BOB_TOTAL_AMOUNT"));
			obj.setR82_bob_with_factor_applied(rs.getBigDecimal("R82_BOB_WITH_FACTOR_APPLIED"));
			obj.setR83_product(rs.getString("R83_PRODUCT"));
			obj.setR83_amount_factor(rs.getBigDecimal("R83_AMOUNT_FACTOR"));
			obj.setR83_bob_total_amount(rs.getBigDecimal("R83_BOB_TOTAL_AMOUNT"));
			obj.setR83_bob_with_factor_applied(rs.getBigDecimal("R83_BOB_WITH_FACTOR_APPLIED"));
			obj.setR84_product(rs.getString("R84_PRODUCT"));
			obj.setR84_amount_factor(rs.getBigDecimal("R84_AMOUNT_FACTOR"));
			obj.setR84_bob_total_amount(rs.getBigDecimal("R84_BOB_TOTAL_AMOUNT"));
			obj.setR84_bob_with_factor_applied(rs.getBigDecimal("R84_BOB_WITH_FACTOR_APPLIED"));
			obj.setR85_product(rs.getString("R85_PRODUCT"));
			obj.setR85_amount_factor(rs.getBigDecimal("R85_AMOUNT_FACTOR"));
			obj.setR85_bob_total_amount(rs.getBigDecimal("R85_BOB_TOTAL_AMOUNT"));
			obj.setR85_bob_with_factor_applied(rs.getBigDecimal("R85_BOB_WITH_FACTOR_APPLIED"));
			obj.setR86_product(rs.getString("R86_PRODUCT"));
			obj.setR86_amount_factor(rs.getBigDecimal("R86_AMOUNT_FACTOR"));
			obj.setR86_bob_total_amount(rs.getBigDecimal("R86_BOB_TOTAL_AMOUNT"));
			obj.setR86_bob_with_factor_applied(rs.getBigDecimal("R86_BOB_WITH_FACTOR_APPLIED"));
			obj.setR87_product(rs.getString("R87_PRODUCT"));
			obj.setR87_amount_factor(rs.getBigDecimal("R87_AMOUNT_FACTOR"));
			obj.setR87_bob_total_amount(rs.getBigDecimal("R87_BOB_TOTAL_AMOUNT"));
			obj.setR87_bob_with_factor_applied(rs.getBigDecimal("R87_BOB_WITH_FACTOR_APPLIED"));
			obj.setR88_product(rs.getString("R88_PRODUCT"));
			obj.setR88_amount_factor(rs.getBigDecimal("R88_AMOUNT_FACTOR"));
			obj.setR88_bob_total_amount(rs.getBigDecimal("R88_BOB_TOTAL_AMOUNT"));
			obj.setR88_bob_with_factor_applied(rs.getBigDecimal("R88_BOB_WITH_FACTOR_APPLIED"));
			obj.setR89_product(rs.getString("R89_PRODUCT"));
			obj.setR89_amount_factor(rs.getBigDecimal("R89_AMOUNT_FACTOR"));
			obj.setR89_bob_total_amount(rs.getBigDecimal("R89_BOB_TOTAL_AMOUNT"));
			obj.setR89_bob_with_factor_applied(rs.getBigDecimal("R89_BOB_WITH_FACTOR_APPLIED"));

			// Common fields
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	// 3.2 DETAIL ROW MAPPER
	class M_LCRRowMapper_Detail implements RowMapper<M_LCR_Detail_Entity> {
		@Override
		public M_LCR_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_LCR_Detail_Entity obj = new M_LCR_Detail_Entity();

			obj.setCUST_ID(rs.getString("CUST_ID"));
			obj.setACCT_NUMBER(rs.getString("ACCT_NUMBER"));
			obj.setACCT_NAME(rs.getString("ACCT_NAME"));
			obj.setACCT_BALANCE_IN_PULA(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setREPORT_LABEL(rs.getString("REPORT_LABEL"));
			obj.setREPORT_ADDL_CRITERIA_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setDEBITEQUIVALENT(rs.getBigDecimal("DEBIT_EQUIVALENT"));
			obj.setEMI(rs.getBigDecimal("EMI"));
			obj.setCREDITEQUIVALENT(rs.getBigDecimal("CREDIT_EQUIVALENT"));

			// Common fields
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	// 3.3 ARCHIVAL SUMMARY ROW MAPPER
	class M_LCR_RowMapper_Archival implements RowMapper<M_LCR_Archival_Summary_Entity> {
		@Override
		public M_LCR_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_LCR_Archival_Summary_Entity obj = new M_LCR_Archival_Summary_Entity();

			// R10
			obj.setR10_product(rs.getString("R10_PRODUCT"));

			// R11
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount_factor(rs.getBigDecimal("R11_AMOUNT_FACTOR"));
			obj.setR11_bob_total_amount(rs.getBigDecimal("R11_BOB_TOTAL_AMOUNT"));
			obj.setR11_bob_with_factor_applied(rs.getBigDecimal("R11_BOB_WITH_FACTOR_APPLIED"));

			// R12
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount_factor(rs.getBigDecimal("R12_AMOUNT_FACTOR"));
			obj.setR12_bob_total_amount(rs.getBigDecimal("R12_BOB_TOTAL_AMOUNT"));
			obj.setR12_bob_with_factor_applied(rs.getBigDecimal("R12_BOB_WITH_FACTOR_APPLIED"));

			// R13
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount_factor(rs.getBigDecimal("R13_AMOUNT_FACTOR"));
			obj.setR13_bob_total_amount(rs.getBigDecimal("R13_BOB_TOTAL_AMOUNT"));
			obj.setR13_bob_with_factor_applied(rs.getBigDecimal("R13_BOB_WITH_FACTOR_APPLIED"));

			// R14
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount_factor(rs.getBigDecimal("R14_AMOUNT_FACTOR"));
			obj.setR14_bob_total_amount(rs.getBigDecimal("R14_BOB_TOTAL_AMOUNT"));
			obj.setR14_bob_with_factor_applied(rs.getBigDecimal("R14_BOB_WITH_FACTOR_APPLIED"));

			// R15
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount_factor(rs.getBigDecimal("R15_AMOUNT_FACTOR"));
			obj.setR15_bob_total_amount(rs.getBigDecimal("R15_BOB_TOTAL_AMOUNT"));
			obj.setR15_bob_with_factor_applied(rs.getBigDecimal("R15_BOB_WITH_FACTOR_APPLIED"));

			// R16
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_amount_factor(rs.getBigDecimal("R16_AMOUNT_FACTOR"));
			obj.setR16_bob_total_amount(rs.getBigDecimal("R16_BOB_TOTAL_AMOUNT"));
			obj.setR16_bob_with_factor_applied(rs.getBigDecimal("R16_BOB_WITH_FACTOR_APPLIED"));

			// R17
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_amount_factor(rs.getBigDecimal("R17_AMOUNT_FACTOR"));
			obj.setR17_bob_total_amount(rs.getBigDecimal("R17_BOB_TOTAL_AMOUNT"));
			obj.setR17_bob_with_factor_applied(rs.getBigDecimal("R17_BOB_WITH_FACTOR_APPLIED"));

			// R18
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_amount_factor(rs.getBigDecimal("R18_AMOUNT_FACTOR"));
			obj.setR18_bob_total_amount(rs.getBigDecimal("R18_BOB_TOTAL_AMOUNT"));
			obj.setR18_bob_with_factor_applied(rs.getBigDecimal("R18_BOB_WITH_FACTOR_APPLIED"));

			// R19
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_amount_factor(rs.getBigDecimal("R19_AMOUNT_FACTOR"));
			obj.setR19_bob_total_amount(rs.getBigDecimal("R19_BOB_TOTAL_AMOUNT"));
			obj.setR19_bob_with_factor_applied(rs.getBigDecimal("R19_BOB_WITH_FACTOR_APPLIED"));

			// R20
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_amount_factor(rs.getBigDecimal("R20_AMOUNT_FACTOR"));
			obj.setR20_bob_total_amount(rs.getBigDecimal("R20_BOB_TOTAL_AMOUNT"));
			obj.setR20_bob_with_factor_applied(rs.getBigDecimal("R20_BOB_WITH_FACTOR_APPLIED"));

			// R21
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_amount_factor(rs.getBigDecimal("R21_AMOUNT_FACTOR"));
			obj.setR21_bob_total_amount(rs.getBigDecimal("R21_BOB_TOTAL_AMOUNT"));
			obj.setR21_bob_with_factor_applied(rs.getBigDecimal("R21_BOB_WITH_FACTOR_APPLIED"));

			// R22
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_amount_factor(rs.getBigDecimal("R22_AMOUNT_FACTOR"));
			obj.setR22_bob_total_amount(rs.getBigDecimal("R22_BOB_TOTAL_AMOUNT"));
			obj.setR22_bob_with_factor_applied(rs.getBigDecimal("R22_BOB_WITH_FACTOR_APPLIED"));

			// R23
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_amount_factor(rs.getBigDecimal("R23_AMOUNT_FACTOR"));
			obj.setR23_bob_total_amount(rs.getBigDecimal("R23_BOB_TOTAL_AMOUNT"));
			obj.setR23_bob_with_factor_applied(rs.getBigDecimal("R23_BOB_WITH_FACTOR_APPLIED"));

			// R24
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_amount_factor(rs.getBigDecimal("R24_AMOUNT_FACTOR"));
			obj.setR24_bob_total_amount(rs.getBigDecimal("R24_BOB_TOTAL_AMOUNT"));
			obj.setR24_bob_with_factor_applied(rs.getBigDecimal("R24_BOB_WITH_FACTOR_APPLIED"));

			// R25
			obj.setR25_product(rs.getString("R25_PRODUCT"));

			// R26
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_amount_factor(rs.getBigDecimal("R26_AMOUNT_FACTOR"));
			obj.setR26_bob_total_amount(rs.getBigDecimal("R26_BOB_TOTAL_AMOUNT"));
			obj.setR26_bob_with_factor_applied(rs.getBigDecimal("R26_BOB_WITH_FACTOR_APPLIED"));

			// R27
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_amount_factor(rs.getBigDecimal("R27_AMOUNT_FACTOR"));
			obj.setR27_bob_total_amount(rs.getBigDecimal("R27_BOB_TOTAL_AMOUNT"));
			obj.setR27_bob_with_factor_applied(rs.getBigDecimal("R27_BOB_WITH_FACTOR_APPLIED"));

			// R28
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_amount_factor(rs.getBigDecimal("R28_AMOUNT_FACTOR"));
			obj.setR28_bob_total_amount(rs.getBigDecimal("R28_BOB_TOTAL_AMOUNT"));
			obj.setR28_bob_with_factor_applied(rs.getBigDecimal("R28_BOB_WITH_FACTOR_APPLIED"));

			// R29
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_amount_factor(rs.getBigDecimal("R29_AMOUNT_FACTOR"));
			obj.setR29_bob_total_amount(rs.getBigDecimal("R29_BOB_TOTAL_AMOUNT"));
			obj.setR29_bob_with_factor_applied(rs.getBigDecimal("R29_BOB_WITH_FACTOR_APPLIED"));

			// R30
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_amount_factor(rs.getBigDecimal("R30_AMOUNT_FACTOR"));
			obj.setR30_bob_total_amount(rs.getBigDecimal("R30_BOB_TOTAL_AMOUNT"));
			obj.setR30_bob_with_factor_applied(rs.getBigDecimal("R30_BOB_WITH_FACTOR_APPLIED"));

			// R31
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_amount_factor(rs.getBigDecimal("R31_AMOUNT_FACTOR"));
			obj.setR31_bob_total_amount(rs.getBigDecimal("R31_BOB_TOTAL_AMOUNT"));
			obj.setR31_bob_with_factor_applied(rs.getBigDecimal("R31_BOB_WITH_FACTOR_APPLIED"));

			// R32
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_amount_factor(rs.getBigDecimal("R32_AMOUNT_FACTOR"));
			obj.setR32_bob_total_amount(rs.getBigDecimal("R32_BOB_TOTAL_AMOUNT"));
			obj.setR32_bob_with_factor_applied(rs.getBigDecimal("R32_BOB_WITH_FACTOR_APPLIED"));

			// R33
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_amount_factor(rs.getBigDecimal("R33_AMOUNT_FACTOR"));
			obj.setR33_bob_total_amount(rs.getBigDecimal("R33_BOB_TOTAL_AMOUNT"));
			obj.setR33_bob_with_factor_applied(rs.getBigDecimal("R33_BOB_WITH_FACTOR_APPLIED"));

			// R34
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_amount_factor(rs.getBigDecimal("R34_AMOUNT_FACTOR"));
			obj.setR34_bob_total_amount(rs.getBigDecimal("R34_BOB_TOTAL_AMOUNT"));
			obj.setR34_bob_with_factor_applied(rs.getBigDecimal("R34_BOB_WITH_FACTOR_APPLIED"));

			// R35
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_amount_factor(rs.getBigDecimal("R35_AMOUNT_FACTOR"));
			obj.setR35_bob_total_amount(rs.getBigDecimal("R35_BOB_TOTAL_AMOUNT"));
			obj.setR35_bob_with_factor_applied(rs.getBigDecimal("R35_BOB_WITH_FACTOR_APPLIED"));

			// R36
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_amount_factor(rs.getBigDecimal("R36_AMOUNT_FACTOR"));
			obj.setR36_bob_total_amount(rs.getBigDecimal("R36_BOB_TOTAL_AMOUNT"));
			obj.setR36_bob_with_factor_applied(rs.getBigDecimal("R36_BOB_WITH_FACTOR_APPLIED"));

			// R37
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_amount_factor(rs.getBigDecimal("R37_AMOUNT_FACTOR"));
			obj.setR37_bob_total_amount(rs.getBigDecimal("R37_BOB_TOTAL_AMOUNT"));
			obj.setR37_bob_with_factor_applied(rs.getBigDecimal("R37_BOB_WITH_FACTOR_APPLIED"));

			// R38
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_amount_factor(rs.getBigDecimal("R38_AMOUNT_FACTOR"));
			obj.setR38_bob_total_amount(rs.getBigDecimal("R38_BOB_TOTAL_AMOUNT"));
			obj.setR38_bob_with_factor_applied(rs.getBigDecimal("R38_BOB_WITH_FACTOR_APPLIED"));

			// R39
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_amount_factor(rs.getBigDecimal("R39_AMOUNT_FACTOR"));
			obj.setR39_bob_total_amount(rs.getBigDecimal("R39_BOB_TOTAL_AMOUNT"));
			obj.setR39_bob_with_factor_applied(rs.getBigDecimal("R39_BOB_WITH_FACTOR_APPLIED"));

			// R40
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_amount_factor(rs.getBigDecimal("R40_AMOUNT_FACTOR"));
			obj.setR40_bob_total_amount(rs.getBigDecimal("R40_BOB_TOTAL_AMOUNT"));
			obj.setR40_bob_with_factor_applied(rs.getBigDecimal("R40_BOB_WITH_FACTOR_APPLIED"));

			// R41
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_amount_factor(rs.getBigDecimal("R41_AMOUNT_FACTOR"));
			obj.setR41_bob_total_amount(rs.getBigDecimal("R41_BOB_TOTAL_AMOUNT"));
			obj.setR41_bob_with_factor_applied(rs.getBigDecimal("R41_BOB_WITH_FACTOR_APPLIED"));

			// R42
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_amount_factor(rs.getBigDecimal("R42_AMOUNT_FACTOR"));
			obj.setR42_bob_total_amount(rs.getBigDecimal("R42_BOB_TOTAL_AMOUNT"));
			obj.setR42_bob_with_factor_applied(rs.getBigDecimal("R42_BOB_WITH_FACTOR_APPLIED"));

			// R43
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_amount_factor(rs.getBigDecimal("R43_AMOUNT_FACTOR"));
			obj.setR43_bob_total_amount(rs.getBigDecimal("R43_BOB_TOTAL_AMOUNT"));
			obj.setR43_bob_with_factor_applied(rs.getBigDecimal("R43_BOB_WITH_FACTOR_APPLIED"));

			// R44
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_amount_factor(rs.getBigDecimal("R44_AMOUNT_FACTOR"));
			obj.setR44_bob_total_amount(rs.getBigDecimal("R44_BOB_TOTAL_AMOUNT"));
			obj.setR44_bob_with_factor_applied(rs.getBigDecimal("R44_BOB_WITH_FACTOR_APPLIED"));

			// R45
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_amount_factor(rs.getBigDecimal("R45_AMOUNT_FACTOR"));
			obj.setR45_bob_total_amount(rs.getBigDecimal("R45_BOB_TOTAL_AMOUNT"));
			obj.setR45_bob_with_factor_applied(rs.getBigDecimal("R45_BOB_WITH_FACTOR_APPLIED"));

			// R46
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_amount_factor(rs.getBigDecimal("R46_AMOUNT_FACTOR"));
			obj.setR46_bob_total_amount(rs.getBigDecimal("R46_BOB_TOTAL_AMOUNT"));
			obj.setR46_bob_with_factor_applied(rs.getBigDecimal("R46_BOB_WITH_FACTOR_APPLIED"));

			// R47
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_amount_factor(rs.getBigDecimal("R47_AMOUNT_FACTOR"));
			obj.setR47_bob_total_amount(rs.getBigDecimal("R47_BOB_TOTAL_AMOUNT"));
			obj.setR47_bob_with_factor_applied(rs.getBigDecimal("R47_BOB_WITH_FACTOR_APPLIED"));

			// R48
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_amount_factor(rs.getBigDecimal("R48_AMOUNT_FACTOR"));
			obj.setR48_bob_total_amount(rs.getBigDecimal("R48_BOB_TOTAL_AMOUNT"));
			obj.setR48_bob_with_factor_applied(rs.getBigDecimal("R48_BOB_WITH_FACTOR_APPLIED"));

			// R49
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_amount_factor(rs.getBigDecimal("R49_AMOUNT_FACTOR"));
			obj.setR49_bob_total_amount(rs.getBigDecimal("R49_BOB_TOTAL_AMOUNT"));
			obj.setR49_bob_with_factor_applied(rs.getBigDecimal("R49_BOB_WITH_FACTOR_APPLIED"));

			// R50
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_amount_factor(rs.getBigDecimal("R50_AMOUNT_FACTOR"));
			obj.setR50_bob_total_amount(rs.getBigDecimal("R50_BOB_TOTAL_AMOUNT"));
			obj.setR50_bob_with_factor_applied(rs.getBigDecimal("R50_BOB_WITH_FACTOR_APPLIED"));

			// R51
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_amount_factor(rs.getBigDecimal("R51_AMOUNT_FACTOR"));
			obj.setR51_bob_total_amount(rs.getBigDecimal("R51_BOB_TOTAL_AMOUNT"));
			obj.setR51_bob_with_factor_applied(rs.getBigDecimal("R51_BOB_WITH_FACTOR_APPLIED"));

			// R52
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_amount_factor(rs.getBigDecimal("R52_AMOUNT_FACTOR"));
			obj.setR52_bob_total_amount(rs.getBigDecimal("R52_BOB_TOTAL_AMOUNT"));
			obj.setR52_bob_with_factor_applied(rs.getBigDecimal("R52_BOB_WITH_FACTOR_APPLIED"));

			// R53
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_amount_factor(rs.getBigDecimal("R53_AMOUNT_FACTOR"));
			obj.setR53_bob_total_amount(rs.getBigDecimal("R53_BOB_TOTAL_AMOUNT"));
			obj.setR53_bob_with_factor_applied(rs.getBigDecimal("R53_BOB_WITH_FACTOR_APPLIED"));

			// R54
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_amount_factor(rs.getBigDecimal("R54_AMOUNT_FACTOR"));
			obj.setR54_bob_total_amount(rs.getBigDecimal("R54_BOB_TOTAL_AMOUNT"));
			obj.setR54_bob_with_factor_applied(rs.getBigDecimal("R54_BOB_WITH_FACTOR_APPLIED"));

			// R55
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_amount_factor(rs.getBigDecimal("R55_AMOUNT_FACTOR"));
			obj.setR55_bob_total_amount(rs.getBigDecimal("R55_BOB_TOTAL_AMOUNT"));
			obj.setR55_bob_with_factor_applied(rs.getBigDecimal("R55_BOB_WITH_FACTOR_APPLIED"));

			// R56
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_amount_factor(rs.getBigDecimal("R56_AMOUNT_FACTOR"));
			obj.setR56_bob_total_amount(rs.getBigDecimal("R56_BOB_TOTAL_AMOUNT"));
			obj.setR56_bob_with_factor_applied(rs.getBigDecimal("R56_BOB_WITH_FACTOR_APPLIED"));

			// R57
			obj.setR57_product(rs.getString("R57_PRODUCT"));
			obj.setR57_amount_factor(rs.getBigDecimal("R57_AMOUNT_FACTOR"));
			obj.setR57_bob_total_amount(rs.getBigDecimal("R57_BOB_TOTAL_AMOUNT"));
			obj.setR57_bob_with_factor_applied(rs.getBigDecimal("R57_BOB_WITH_FACTOR_APPLIED"));

			// R58
			obj.setR58_product(rs.getString("R58_PRODUCT"));
			obj.setR58_amount_factor(rs.getBigDecimal("R58_AMOUNT_FACTOR"));
			obj.setR58_bob_total_amount(rs.getBigDecimal("R58_BOB_TOTAL_AMOUNT"));
			obj.setR58_bob_with_factor_applied(rs.getBigDecimal("R58_BOB_WITH_FACTOR_APPLIED"));

			// R59
			obj.setR59_product(rs.getString("R59_PRODUCT"));
			obj.setR59_amount_factor(rs.getBigDecimal("R59_AMOUNT_FACTOR"));
			obj.setR59_bob_total_amount(rs.getBigDecimal("R59_BOB_TOTAL_AMOUNT"));
			obj.setR59_bob_with_factor_applied(rs.getBigDecimal("R59_BOB_WITH_FACTOR_APPLIED"));

			// R60
			obj.setR60_product(rs.getString("R60_PRODUCT"));
			obj.setR60_amount_factor(rs.getBigDecimal("R60_AMOUNT_FACTOR"));
			obj.setR60_bob_total_amount(rs.getBigDecimal("R60_BOB_TOTAL_AMOUNT"));
			obj.setR60_bob_with_factor_applied(rs.getBigDecimal("R60_BOB_WITH_FACTOR_APPLIED"));

			// R61
			obj.setR61_product(rs.getString("R61_PRODUCT"));
			obj.setR61_amount_factor(rs.getBigDecimal("R61_AMOUNT_FACTOR"));
			obj.setR61_bob_total_amount(rs.getBigDecimal("R61_BOB_TOTAL_AMOUNT"));
			obj.setR61_bob_with_factor_applied(rs.getBigDecimal("R61_BOB_WITH_FACTOR_APPLIED"));

			// R62
			obj.setR62_product(rs.getString("R62_PRODUCT"));
			obj.setR62_amount_factor(rs.getBigDecimal("R62_AMOUNT_FACTOR"));
			obj.setR62_bob_total_amount(rs.getBigDecimal("R62_BOB_TOTAL_AMOUNT"));
			obj.setR62_bob_with_factor_applied(rs.getBigDecimal("R62_BOB_WITH_FACTOR_APPLIED"));

			// R63
			obj.setR63_product(rs.getString("R63_PRODUCT"));
			obj.setR63_amount_factor(rs.getBigDecimal("R63_AMOUNT_FACTOR"));
			obj.setR63_bob_total_amount(rs.getBigDecimal("R63_BOB_TOTAL_AMOUNT"));
			obj.setR63_bob_with_factor_applied(rs.getBigDecimal("R63_BOB_WITH_FACTOR_APPLIED"));

			// R64
			obj.setR64_product(rs.getString("R64_PRODUCT"));
			obj.setR64_amount_factor(rs.getBigDecimal("R64_AMOUNT_FACTOR"));
			obj.setR64_bob_total_amount(rs.getBigDecimal("R64_BOB_TOTAL_AMOUNT"));
			obj.setR64_bob_with_factor_applied(rs.getBigDecimal("R64_BOB_WITH_FACTOR_APPLIED"));

			// R65
			obj.setR65_product(rs.getString("R65_PRODUCT"));
			obj.setR65_amount_factor(rs.getBigDecimal("R65_AMOUNT_FACTOR"));
			obj.setR65_bob_total_amount(rs.getBigDecimal("R65_BOB_TOTAL_AMOUNT"));
			obj.setR65_bob_with_factor_applied(rs.getBigDecimal("R65_BOB_WITH_FACTOR_APPLIED"));

			// R66
			obj.setR66_product(rs.getString("R66_PRODUCT"));
			obj.setR66_amount_factor(rs.getBigDecimal("R66_AMOUNT_FACTOR"));
			obj.setR66_bob_total_amount(rs.getBigDecimal("R66_BOB_TOTAL_AMOUNT"));
			obj.setR66_bob_with_factor_applied(rs.getBigDecimal("R66_BOB_WITH_FACTOR_APPLIED"));

			// R67
			obj.setR67_product(rs.getString("R67_PRODUCT"));
			obj.setR67_amount_factor(rs.getBigDecimal("R67_AMOUNT_FACTOR"));
			obj.setR67_bob_total_amount(rs.getBigDecimal("R67_BOB_TOTAL_AMOUNT"));
			obj.setR67_bob_with_factor_applied(rs.getBigDecimal("R67_BOB_WITH_FACTOR_APPLIED"));

			// R68
			obj.setR68_product(rs.getString("R68_PRODUCT"));
			obj.setR68_amount_factor(rs.getBigDecimal("R68_AMOUNT_FACTOR"));
			obj.setR68_bob_total_amount(rs.getBigDecimal("R68_BOB_TOTAL_AMOUNT"));
			obj.setR68_bob_with_factor_applied(rs.getBigDecimal("R68_BOB_WITH_FACTOR_APPLIED"));

			// R69
			obj.setR69_product(rs.getString("R69_PRODUCT"));
			obj.setR69_amount_factor(rs.getBigDecimal("R69_AMOUNT_FACTOR"));
			obj.setR69_bob_total_amount(rs.getBigDecimal("R69_BOB_TOTAL_AMOUNT"));
			obj.setR69_bob_with_factor_applied(rs.getBigDecimal("R69_BOB_WITH_FACTOR_APPLIED"));

			// R70
			obj.setR70_product(rs.getString("R70_PRODUCT"));
			obj.setR70_amount_factor(rs.getBigDecimal("R70_AMOUNT_FACTOR"));
			obj.setR70_bob_total_amount(rs.getBigDecimal("R70_BOB_TOTAL_AMOUNT"));
			obj.setR70_bob_with_factor_applied(rs.getBigDecimal("R70_BOB_WITH_FACTOR_APPLIED"));

			// R72
			obj.setR72_product(rs.getString("R72_PRODUCT"));
			obj.setR72_amount_factor(rs.getBigDecimal("R72_AMOUNT_FACTOR"));
			obj.setR72_bob_total_amount(rs.getBigDecimal("R72_BOB_TOTAL_AMOUNT"));
			obj.setR72_bob_with_factor_applied(rs.getBigDecimal("R72_BOB_WITH_FACTOR_APPLIED"));

			// R73
			obj.setR73_product(rs.getString("R73_PRODUCT"));
			obj.setR73_amount_factor(rs.getBigDecimal("R73_AMOUNT_FACTOR"));
			obj.setR73_bob_total_amount(rs.getBigDecimal("R73_BOB_TOTAL_AMOUNT"));
			obj.setR73_bob_with_factor_applied(rs.getBigDecimal("R73_BOB_WITH_FACTOR_APPLIED"));

			// R74
			obj.setR74_product(rs.getString("R74_PRODUCT"));
			obj.setR74_amount_factor(rs.getBigDecimal("R74_AMOUNT_FACTOR"));
			obj.setR74_bob_total_amount(rs.getBigDecimal("R74_BOB_TOTAL_AMOUNT"));
			obj.setR74_bob_with_factor_applied(rs.getBigDecimal("R74_BOB_WITH_FACTOR_APPLIED"));

			// R75
			obj.setR75_product(rs.getString("R75_PRODUCT"));
			obj.setR75_amount_factor(rs.getBigDecimal("R75_AMOUNT_FACTOR"));
			obj.setR75_bob_total_amount(rs.getBigDecimal("R75_BOB_TOTAL_AMOUNT"));
			obj.setR75_bob_with_factor_applied(rs.getBigDecimal("R75_BOB_WITH_FACTOR_APPLIED"));

			// R76
			obj.setR76_product(rs.getString("R76_PRODUCT"));
			obj.setR76_amount_factor(rs.getBigDecimal("R76_AMOUNT_FACTOR"));
			obj.setR76_bob_total_amount(rs.getBigDecimal("R76_BOB_TOTAL_AMOUNT"));
			obj.setR76_bob_with_factor_applied(rs.getBigDecimal("R76_BOB_WITH_FACTOR_APPLIED"));

			// R77
			obj.setR77_product(rs.getString("R77_PRODUCT"));
			obj.setR77_amount_factor(rs.getBigDecimal("R77_AMOUNT_FACTOR"));
			obj.setR77_bob_total_amount(rs.getBigDecimal("R77_BOB_TOTAL_AMOUNT"));
			obj.setR77_bob_with_factor_applied(rs.getBigDecimal("R77_BOB_WITH_FACTOR_APPLIED"));

			// R78
			obj.setR78_product(rs.getString("R78_PRODUCT"));
			obj.setR78_amount_factor(rs.getBigDecimal("R78_AMOUNT_FACTOR"));
			obj.setR78_bob_total_amount(rs.getBigDecimal("R78_BOB_TOTAL_AMOUNT"));
			obj.setR78_bob_with_factor_applied(rs.getBigDecimal("R78_BOB_WITH_FACTOR_APPLIED"));

			// R79
			obj.setR79_product(rs.getString("R79_PRODUCT"));
			obj.setR79_amount_factor(rs.getBigDecimal("R79_AMOUNT_FACTOR"));
			obj.setR79_bob_total_amount(rs.getBigDecimal("R79_BOB_TOTAL_AMOUNT"));
			obj.setR79_bob_with_factor_applied(rs.getBigDecimal("R79_BOB_WITH_FACTOR_APPLIED"));

			// R80
			obj.setR80_product(rs.getString("R80_PRODUCT"));
			obj.setR80_amount_factor(rs.getBigDecimal("R80_AMOUNT_FACTOR"));
			obj.setR80_bob_total_amount(rs.getBigDecimal("R80_BOB_TOTAL_AMOUNT"));
			obj.setR80_bob_with_factor_applied(rs.getBigDecimal("R80_BOB_WITH_FACTOR_APPLIED"));

			// R81
			obj.setR81_product(rs.getString("R81_PRODUCT"));
			obj.setR81_amount_factor(rs.getBigDecimal("R81_AMOUNT_FACTOR"));
			obj.setR81_bob_total_amount(rs.getBigDecimal("R81_BOB_TOTAL_AMOUNT"));
			obj.setR81_bob_with_factor_applied(rs.getBigDecimal("R81_BOB_WITH_FACTOR_APPLIED"));

			// R82
			obj.setR82_product(rs.getString("R82_PRODUCT"));
			obj.setR82_amount_factor(rs.getBigDecimal("R82_AMOUNT_FACTOR"));
			obj.setR82_bob_total_amount(rs.getBigDecimal("R82_BOB_TOTAL_AMOUNT"));
			obj.setR82_bob_with_factor_applied(rs.getBigDecimal("R82_BOB_WITH_FACTOR_APPLIED"));

			// R83
			obj.setR83_product(rs.getString("R83_PRODUCT"));
			obj.setR83_amount_factor(rs.getBigDecimal("R83_AMOUNT_FACTOR"));
			obj.setR83_bob_total_amount(rs.getBigDecimal("R83_BOB_TOTAL_AMOUNT"));
			obj.setR83_bob_with_factor_applied(rs.getBigDecimal("R83_BOB_WITH_FACTOR_APPLIED"));

			// R84
			obj.setR84_product(rs.getString("R84_PRODUCT"));
			obj.setR84_amount_factor(rs.getBigDecimal("R84_AMOUNT_FACTOR"));
			obj.setR84_bob_total_amount(rs.getBigDecimal("R84_BOB_TOTAL_AMOUNT"));
			obj.setR84_bob_with_factor_applied(rs.getBigDecimal("R84_BOB_WITH_FACTOR_APPLIED"));

			// R85
			obj.setR85_product(rs.getString("R85_PRODUCT"));
			obj.setR85_amount_factor(rs.getBigDecimal("R85_AMOUNT_FACTOR"));
			obj.setR85_bob_total_amount(rs.getBigDecimal("R85_BOB_TOTAL_AMOUNT"));
			obj.setR85_bob_with_factor_applied(rs.getBigDecimal("R85_BOB_WITH_FACTOR_APPLIED"));

			// R86
			obj.setR86_product(rs.getString("R86_PRODUCT"));
			obj.setR86_amount_factor(rs.getBigDecimal("R86_AMOUNT_FACTOR"));
			obj.setR86_bob_total_amount(rs.getBigDecimal("R86_BOB_TOTAL_AMOUNT"));
			obj.setR86_bob_with_factor_applied(rs.getBigDecimal("R86_BOB_WITH_FACTOR_APPLIED"));

			// R87
			obj.setR87_product(rs.getString("R87_PRODUCT"));
			obj.setR87_amount_factor(rs.getBigDecimal("R87_AMOUNT_FACTOR"));
			obj.setR87_bob_total_amount(rs.getBigDecimal("R87_BOB_TOTAL_AMOUNT"));
			obj.setR87_bob_with_factor_applied(rs.getBigDecimal("R87_BOB_WITH_FACTOR_APPLIED"));

			// R88
			obj.setR88_product(rs.getString("R88_PRODUCT"));
			obj.setR88_amount_factor(rs.getBigDecimal("R88_AMOUNT_FACTOR"));
			obj.setR88_bob_total_amount(rs.getBigDecimal("R88_BOB_TOTAL_AMOUNT"));
			obj.setR88_bob_with_factor_applied(rs.getBigDecimal("R88_BOB_WITH_FACTOR_APPLIED"));

			// R89
			obj.setR89_product(rs.getString("R89_PRODUCT"));
			obj.setR89_amount_factor(rs.getBigDecimal("R89_AMOUNT_FACTOR"));
			obj.setR89_bob_total_amount(rs.getBigDecimal("R89_BOB_TOTAL_AMOUNT"));
			obj.setR89_bob_with_factor_applied(rs.getBigDecimal("R89_BOB_WITH_FACTOR_APPLIED"));

			// Common fields
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	// 3.4 ARCHIVAL DETAIL ROW MAPPER
	class M_LCRRowMapper_ArchivalDetail implements RowMapper<M_LCR_Archival_Detail_Entity> {
		@Override
		public M_LCR_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_LCR_Archival_Detail_Entity obj = new M_LCR_Archival_Detail_Entity();

			obj.setCUST_ID(rs.getString("CUST_ID"));
			obj.setACCT_NUMBER(rs.getString("ACCT_NUMBER"));
			obj.setACCT_NAME(rs.getString("ACCT_NAME"));
			obj.setACCT_BALANCE_IN_PULA(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setREPORT_LABEL(rs.getString("REPORT_LABEL"));
			obj.setREPORT_ADDL_CRITERIA_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setDEBITEQUIVALENT(rs.getBigDecimal("DEBIT_EQUIVALENT"));
			obj.setEMI(rs.getBigDecimal("EMI"));
			obj.setCREDITEQUIVALENT(rs.getBigDecimal("CREDIT_EQUIVALENT"));

			// Common fields including REPORT_RESUBDATE
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	// 4. JDBC QUERY METHODS

	// 4.1 SUMMARY METHODS

	/**
	 * Get summary data by report date
	 */
	public List<M_LCR_Summary_Entity> getDataByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_LCR_SUMMARYTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LCR_RowMapper_Summary());
	}

	/**
	 * Get archival summary data by report date and version
	 */
	public List<M_LCR_Archival_Summary_Entity> getdatabydateListarchival(Date reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new M_LCR_RowMapper_Archival());
	}

	/**
	 * Get all archival summary data with version (for dropdown)
	 */
	public List<M_LCR_Archival_Summary_Entity> getdatabydateListWithVersion() {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_LCR_RowMapper_Archival());
	}

	/**
	 * Find max version for a given report date
	 */
	public BigDecimal findMaxVersion(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_LCR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	/**
	 * Find summary by report date (single record)
	 */
	@Transactional
	public M_LCR_Summary_Entity findSummaryByReportDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_LCR_SUMMARYTABLE WHERE REPORT_DATE = ?";
		List<M_LCR_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_LCR_RowMapper_Summary());
		return list.isEmpty() ? null : list.get(0);
	}

	// 4.2 DETAIL METHODS

	/**
	 * Get all detail records by report date
	 */
	public List<M_LCR_Detail_Entity> getDetaildatabydateList(Date reportdate) {
		String sql = "SELECT * FROM BRRS_M_LCR_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LCRRowMapper_Detail());
	}

	/**
	 * Get detail records by report date with pagination
	 */
	public List<M_LCR_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {
		String sql = "SELECT * FROM BRRS_M_LCR_DETAILTABLE WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_LCRRowMapper_Detail());
	}

	/**
	 * Get count of detail records by report date (for pagination)
	 */
	public int getDetaildatacount(Date reportdate) {
		String sql = "SELECT COUNT(*) FROM BRRS_M_LCR_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	/**
	 * Get detail records by report label and criteria (REPORT_LABEL_1) Used for:
	 * R12-R36, R40-R89 (normal rows)
	 */
	public List<M_LCR_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {
		String sql = "SELECT * FROM BRRS_M_LCR_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LCRRowMapper_Detail());
	}

	/**
	 * Get detail records by report label and criteria (REPORT_LABEL_2) Used for:
	 * R81, R82, R83, R37 (special rows)
	 */
	public List<M_LCR_Detail_Entity> GetDetailDataByRowIdAndColumnId2(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {
		String sql = "SELECT * FROM BRRS_M_LCR_DETAILTABLE WHERE REPORT_LABEL_2 = ? AND REPORT_ADDL_CRITERIA_2 = ? AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LCRRowMapper_Detail());
	}

	/**
	 * Get detail records by report label and criteria (REPORT_LABEL_3) Used for:
	 * R38 (special row)
	 */
	public List<M_LCR_Detail_Entity> GetDetailDataByRowIdAndColumnId3(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {
		String sql = "SELECT * FROM BRRS_M_LCR_DETAILTABLE WHERE REPORT_LABEL_3 = ? AND REPORT_ADDL_CRITERIA_3 = ? AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LCRRowMapper_Detail());
	}

	/**
	 * Get detail record by account number
	 */
	public M_LCR_Detail_Entity findByAcctnumber(String acctNumber) {
		String sql = "SELECT * FROM BRRS_M_LCR_DETAILTABLE WHERE ACCT_NUMBER = ?";
		List<M_LCR_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { acctNumber },
				new M_LCRRowMapper_Detail());
		return list.isEmpty() ? null : list.get(0);
	}

	// 4.3 ARCHIVAL DETAIL METHODS

	/**
	 * Get archival detail records by report date and version
	 */
	public List<M_LCR_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportdate, version }, new M_LCRRowMapper_ArchivalDetail());
	}

	/**
	 * Get archival detail records by report date, version with pagination
	 */
	public List<M_LCR_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate, BigDecimal version,
			int offset, int limit) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { reportdate, version, offset, limit },
				new M_LCRRowMapper_ArchivalDetail());
	}

	/**
	 * Get count of archival detail records by report date and version (for
	 * pagination)
	 */
	public int getArchivalDetaildatacount(Date reportdate, BigDecimal version) {
		String sql = "SELECT COUNT(*) FROM BRRS_M_LCR_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate, version }, Integer.class);
	}

	/**
	 * Get archival detail records by report label and criteria (REPORT_LABEL_1)
	 * Used for: R12-R36, R40-R89 (normal rows)
	 */
	public List<M_LCR_Archival_Detail_Entity> GetArchivalDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, version },
				new M_LCRRowMapper_ArchivalDetail());
	}

	/**
	 * Get archival detail records by report label and criteria (REPORT_LABEL_2)
	 * Used for: R81, R82, R83, R37 (special rows)
	 */
	public List<M_LCR_Archival_Detail_Entity> GetArchivalDetailDataByRowIdAndColumnId2(String reportLabel,
			String reportAddlCriteria1, Date reportdate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL_2 = ? AND REPORT_ADDL_CRITERIA_2 = ? AND REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, version },
				new M_LCRRowMapper_ArchivalDetail());
	}

	/**
	 * Get archival detail records by report label and criteria (REPORT_LABEL_3)
	 * Used for: R38 (special row)
	 */
	public List<M_LCR_Archival_Detail_Entity> GetArchivalDetailDataByRowIdAndColumnId3(String reportLabel,
			String reportAddlCriteria1, Date reportdate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL_3 = ? AND REPORT_ADDL_CRITERIA_3 = ? AND REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, version },
				new M_LCRRowMapper_ArchivalDetail());
	}

	/**
	 * Get archival detail record by account number
	 */
	public M_LCR_Archival_Detail_Entity ArchivalFindByAcctnumber(String acctNumber, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ? AND REPORT_VERSION = ?";
		List<M_LCR_Archival_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { acctNumber, version },
				new M_LCRRowMapper_ArchivalDetail());
		return list.isEmpty() ? null : list.get(0);
	}

	// 4.4 ARCHIVAL SUMMARY

	/**
	 * Get archival summary by report date (without version - returns all versions)
	 */
	public List<M_LCR_Archival_Summary_Entity> getArchivalSummaryByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LCR_RowMapper_Archival());
	}

	/**
	 * Get latest archival summary by report date (max version)
	 */
	public M_LCR_Archival_Summary_Entity getLatestArchivalSummary(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROW ONLY";
		List<M_LCR_Archival_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_LCR_RowMapper_Archival());
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Get archival summary by version (across all dates)
	 */
	public List<M_LCR_Archival_Summary_Entity> getArchivalSummaryByVersion(BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_LCR_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION = ? ORDER BY REPORT_DATE";
		return jdbcTemplate.query(sql, new Object[] { version }, new M_LCR_RowMapper_Archival());
	}

	/**
	 * Get distinct report dates from archival summary
	 */
	public List<Date> getDistinctArchivalDates() {
		String sql = "SELECT DISTINCT REPORT_DATE FROM BRRS_M_LCR_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_DATE DESC";
		return jdbcTemplate.queryForList(sql, Date.class);
	}

	/**
	 * Check if archival data exists for a given date and version
	 */
	public boolean archivalExists(Date reportDate, BigDecimal version) {
		String sql = "SELECT COUNT(*) FROM BRRS_M_LCR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { reportDate, version }, Integer.class);
		return count > 0;
	}

	// =========================
	// MODEL AND VIEW
	// =========================

	public ModelAndView getM_LCRView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (req1 != null && req1.getSession() != null) {
			String userid = (String) req1.getSession().getAttribute("USERID");
			logger.info("User Id Maker and Checker: {}", userid);
			String role = userProfileRep.getUserRole(userid);
			if (md != null) {
				md.addAttribute("role", role);
			}
			mv.addObject("role", role);
			logger.info("Role: {}", role);
		}

		logger.info("getM_LCRView called - type: {}, version: {}, todate: {}", type, version, todate);

		if (type != null && type.equals("ARCHIVAL") && version != null) {
			logger.info("Fetching ARCHIVAL summary for date: {}, version: {}", todate, version);
			List<M_LCR_Archival_Summary_Entity> T1Master = new ArrayList<>();
			try {
				Date d1 = dateformat.parse(todate);
				// ✅ Using JDBC method instead of JPA repo
				T1Master = getdatabydateListarchival(d1, version);
				logger.info("Archival records found: {}", T1Master.size());
			} catch (ParseException e) {
				logger.error("Error parsing date: {}", todate, e);
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("displaymode", "archivalSummary");
		} else {
			logger.info("Fetching NORMAL summary for date: {}", todate);
			List<M_LCR_Summary_Entity> T1Master = new ArrayList<>();
			try {
				Date d1 = dateformat.parse(todate);
				// ✅ Using JDBC method instead of JPA repo
				T1Master = getDataByDate(d1);
				logger.info("Normal records found: {}", T1Master.size());
			} catch (ParseException e) {
				logger.error("Error parsing date: {}", todate, e);
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("displaymode", "summary");
		}

		mv.setViewName("BRRS/M_LCR");
		logger.info("View name set to: BRRS/M_LCR");
		return mv;
	}

	public ModelAndView getM_LCRcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String rowId = null;
			String columnId = null;

			// Split filter string into rowId & columnId
			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}

			logger.info("getM_LCRcurrentDtl - type: {}, version: {}, rowId: {}, columnId: {}", type, version, rowId,
					columnId);

			int offset = currentPage * pageSize;

			if ("ARCHIVAL".equals(type) && version != null) {
				logger.info("Fetching ARCHIVAL details for date: {}, version: {}", parsedDate, version);

				// Archival branch - Using JDBC methods
				List<M_LCR_Archival_Detail_Entity> T1Dt1;
				BigDecimal versionDecimal = new BigDecimal(version);

				if (rowId != null && columnId != null) {
					// Check which label to use based on rowId
					if ("R81".equals(rowId) || "R82".equals(rowId) || "R83".equals(rowId) || "R37".equals(rowId)) {
						T1Dt1 = GetArchivalDetailDataByRowIdAndColumnId2(rowId, columnId, parsedDate, versionDecimal);
					} else if ("R38".equals(rowId)) {
						T1Dt1 = GetArchivalDetailDataByRowIdAndColumnId3(rowId, columnId, parsedDate, versionDecimal);
					} else {
						T1Dt1 = GetArchivalDetailDataByRowIdAndColumnId(rowId, columnId, parsedDate, versionDecimal);
					}
				} else {
					// With pagination
					T1Dt1 = getArchivalDetaildatabydateList(parsedDate, versionDecimal, offset, pageSize);
					totalPages = getArchivalDetaildatacount(parsedDate, versionDecimal);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				logger.info("ARCHIVAL DETAIL COUNT: {}", T1Dt1 != null ? T1Dt1.size() : 0);

			} else {
				logger.info("Fetching NORMAL details for date: {}", parsedDate);

				// Current branch - Using JDBC methods
				List<M_LCR_Detail_Entity> T1Dt1;

				if (rowId != null && columnId != null) {
					if ("R81".equals(rowId) || "R82".equals(rowId) || "R83".equals(rowId) || "R37".equals(rowId)) {
						T1Dt1 = GetDetailDataByRowIdAndColumnId2(rowId, columnId, parsedDate);
					} else if ("R38".equals(rowId)) {
						T1Dt1 = GetDetailDataByRowIdAndColumnId3(rowId, columnId, parsedDate);
					} else {
						T1Dt1 = GetDetailDataByRowIdAndColumnId(rowId, columnId, parsedDate);
					}
				} else {
					// With pagination
					T1Dt1 = getDetaildatabydateList(parsedDate, offset, pageSize);
					totalPages = getDetaildatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("clickedRow", rowId);
				logger.info("NORMAL DETAIL COUNT: {}", T1Dt1 != null ? T1Dt1.size() : 0);
			}

		} catch (ParseException e) {
			logger.error("Error parsing date: {}", todate, e);
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			logger.error("Unexpected error in getM_LCRcurrentDtl", e);
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		// Common attributes
		mv.setViewName("BRRS/M_LCR");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		int totalPagesCalc = (int) Math.ceil((double) totalPages / pageSize);
		logger.info("totalPages: {}", totalPagesCalc);
		mv.addObject("totalPages", totalPagesCalc);
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	public byte[] getM_LCRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LCRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_LCR_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LCR report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		// This try-with-resources block is perfect. It guarantees all resources are
		// closed automatically.
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.getSheetAt(0);

			// --- Style Definitions ---
			CreationHelper createHelper = workbook.getCreationHelper();

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			// Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");
			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			try {

				// Row 5 = Excel row 6
				Row dateRow = sheet.getRow(4);

				if (dateRow == null) {
					dateRow = sheet.createRow(4);
				}

				// Column 2 = Excel column C
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LCR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R12
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR12_bob_total_amount() != null) {
						cell4.setCellValue(record.getR12_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					// R13
					// Column E
					cell4 = row.getCell(4);
					if (record.getR13_bob_total_amount() != null) {
						cell4.setCellValue(record.getR13_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					// R14
					// Column E
					cell4 = row.getCell(4);
					if (record.getR14_bob_total_amount() != null) {
						cell4.setCellValue(record.getR14_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					// R15
					// Column E
					cell4 = row.getCell(4);
					if (record.getR15_bob_total_amount() != null) {
						cell4.setCellValue(record.getR15_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					// R20
					// Column E
					cell4 = row.getCell(4);
					if (record.getR20_bob_total_amount() != null) {
						cell4.setCellValue(record.getR20_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					// R21
					// Column E
					cell4 = row.getCell(4);
					if (record.getR21_bob_total_amount() != null) {
						cell4.setCellValue(record.getR21_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					// R29
					// Column E
					cell4 = row.getCell(4);
					if (record.getR29_bob_total_amount() != null) {
						cell4.setCellValue(record.getR29_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					// R30
					// Column E
					cell4 = row.getCell(4);
					if (record.getR30_bob_total_amount() != null) {
						cell4.setCellValue(record.getR30_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					// R31
					// Column E
					cell4 = row.getCell(4);
					if (record.getR31_bob_total_amount() != null) {
						cell4.setCellValue(record.getR31_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					// R34
					// Column E
					cell4 = row.getCell(4);
					if (record.getR34_bob_total_amount() != null) {
						cell4.setCellValue(record.getR34_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					// R35
					// Column E
					cell4 = row.getCell(4);
					if (record.getR35_bob_total_amount() != null) {
						cell4.setCellValue(record.getR35_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					// R36
					// Column E
					cell4 = row.getCell(4);
					if (record.getR36_bob_total_amount() != null) {
						cell4.setCellValue(record.getR36_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					// R37
					// Column E
					cell4 = row.getCell(4);
					if (record.getR37_bob_total_amount() != null) {
						cell4.setCellValue(record.getR37_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					// R38
					// Column E
					cell4 = row.getCell(4);
					if (record.getR38_bob_total_amount() != null) {
						cell4.setCellValue(record.getR38_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					// R40
					// Column E
					cell4 = row.getCell(4);
					if (record.getR40_bob_total_amount() != null) {
						cell4.setCellValue(record.getR40_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					// R41
					// Column E
					cell4 = row.getCell(4);
					if (record.getR41_bob_total_amount() != null) {
						cell4.setCellValue(record.getR41_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					// R42
					// Column E
					cell4 = row.getCell(4);
					if (record.getR42_bob_total_amount() != null) {
						cell4.setCellValue(record.getR42_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					// R43
					// Column E
					cell4 = row.getCell(4);
					if (record.getR43_bob_total_amount() != null) {
						cell4.setCellValue(record.getR43_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					// R45
					// Column E
					cell4 = row.getCell(4);
					if (record.getR45_bob_total_amount() != null) {
						cell4.setCellValue(record.getR45_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					// R46
					// Column E
					cell4 = row.getCell(4);
					if (record.getR46_bob_total_amount() != null) {
						cell4.setCellValue(record.getR46_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					// R47
					// Column E
					cell4 = row.getCell(4);
					if (record.getR47_bob_total_amount() != null) {
						cell4.setCellValue(record.getR47_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					// R48
					// Column E
					cell4 = row.getCell(4);
					if (record.getR48_bob_total_amount() != null) {
						cell4.setCellValue(record.getR48_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					// R49
					// Column E
					cell4 = row.getCell(4);
					if (record.getR49_bob_total_amount() != null) {
						cell4.setCellValue(record.getR49_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(49);
					// R50
					// Column E
					cell4 = row.getCell(4);
					if (record.getR50_bob_total_amount() != null) {
						cell4.setCellValue(record.getR50_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					// R51
					// Column E
					cell4 = row.getCell(4);
					if (record.getR51_bob_total_amount() != null) {
						cell4.setCellValue(record.getR51_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(51);
					// R52
					// Column E
					cell4 = row.getCell(4);
					if (record.getR52_bob_total_amount() != null) {
						cell4.setCellValue(record.getR52_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(52);
					// R53
					// Column E
					cell4 = row.getCell(4);
					if (record.getR53_bob_total_amount() != null) {
						cell4.setCellValue(record.getR53_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					// R55
					// Column E
					cell4 = row.getCell(4);
					if (record.getR55_bob_total_amount() != null) {
						cell4.setCellValue(record.getR55_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					// R56
					// Column E
					cell4 = row.getCell(4);
					if (record.getR56_bob_total_amount() != null) {
						cell4.setCellValue(record.getR56_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					// R57
					// Column E
					cell4 = row.getCell(4);
					if (record.getR57_bob_total_amount() != null) {
						cell4.setCellValue(record.getR57_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(57);
					// R58
					// Column E
					cell4 = row.getCell(4);
					if (record.getR58_bob_total_amount() != null) {
						cell4.setCellValue(record.getR58_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(58);
					// R59
					// Column E
					cell4 = row.getCell(4);
					if (record.getR59_bob_total_amount() != null) {
						cell4.setCellValue(record.getR59_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					// R60
					// Column E
					cell4 = row.getCell(4);
					if (record.getR60_bob_total_amount() != null) {
						cell4.setCellValue(record.getR60_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					// R61
					// Column E
					cell4 = row.getCell(4);
					if (record.getR61_bob_total_amount() != null) {
						cell4.setCellValue(record.getR61_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					// R62
					// Column E
					cell4 = row.getCell(4);
					if (record.getR62_bob_total_amount() != null) {
						cell4.setCellValue(record.getR62_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(52);
					// R63
					// Column E
					cell4 = row.getCell(4);
					if (record.getR63_bob_total_amount() != null) {
						cell4.setCellValue(record.getR63_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(64);
					// R65
					// Column E
					cell4 = row.getCell(4);
					if (record.getR65_bob_total_amount() != null) {
						cell4.setCellValue(record.getR65_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(65);
					// R66
					// Column E
					cell4 = row.getCell(4);
					if (record.getR66_bob_total_amount() != null) {
						cell4.setCellValue(record.getR66_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(66);
					// R67
					// Column E
					cell4 = row.getCell(4);
					if (record.getR67_bob_total_amount() != null) {
						cell4.setCellValue(record.getR67_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(67);
					// R68
					// Column E
					cell4 = row.getCell(4);
					if (record.getR68_bob_total_amount() != null) {
						cell4.setCellValue(record.getR68_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(68);
					// R69
					// Column E
					cell4 = row.getCell(4);
					if (record.getR69_bob_total_amount() != null) {
						cell4.setCellValue(record.getR69_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(73);
					// R74
					// Column E
					cell4 = row.getCell(4);
					if (record.getR74_bob_total_amount() != null) {
						cell4.setCellValue(record.getR74_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(74);
					// R75
					// Column E
					cell4 = row.getCell(4);
					if (record.getR75_bob_total_amount() != null) {
						cell4.setCellValue(record.getR75_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(75);
					// R76
					// Column E
					cell4 = row.getCell(4);
					if (record.getR76_bob_total_amount() != null) {
						cell4.setCellValue(record.getR76_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(76);
					// R77
					// Column E
					cell4 = row.getCell(4);
					if (record.getR77_bob_total_amount() != null) {
						cell4.setCellValue(record.getR77_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(77);
					// R78
					// Column E
					cell4 = row.getCell(4);
					if (record.getR78_bob_total_amount() != null) {
						cell4.setCellValue(record.getR78_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(78);
					// R79
					// Column E
					cell4 = row.getCell(4);
					if (record.getR79_bob_total_amount() != null) {
						cell4.setCellValue(record.getR79_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(80);
					// R81
					// Column E
					cell4 = row.getCell(4);
					if (record.getR81_bob_total_amount() != null) {
						cell4.setCellValue(record.getR81_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(81);
					// R82
					// Column E
					cell4 = row.getCell(4);
					if (record.getR82_bob_total_amount() != null) {
						cell4.setCellValue(record.getR82_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(82);
					// R83
					// Column E
					cell4 = row.getCell(4);
					if (record.getR83_bob_total_amount() != null) {
						cell4.setCellValue(record.getR83_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(83);
					// R84
					// Column E
					cell4 = row.getCell(4);
					if (record.getR84_bob_total_amount() != null) {
						cell4.setCellValue(record.getR84_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(84);
					// R74
					// Column E
					cell4 = row.getCell(4);
					if (record.getR85_bob_total_amount() != null) {
						cell4.setCellValue(record.getR85_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(87);
					// R88
					// Column E
					cell4 = row.getCell(4);
					if (record.getR88_bob_total_amount() != null) {
						cell4.setCellValue(record.getR88_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(88);
					// R89
					// Column E
					cell4 = row.getCell(4);
					if (record.getR89_bob_total_amount() != null) {
						cell4.setCellValue(record.getR89_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	public byte[] getM_LCRDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_LCR Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LCRDetails");

			// Common border style
			BorderStyle border = BorderStyle.THIN;

			// Header style (left aligned)
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.LEFT);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(border);
			headerStyle.setBorderBottom(border);
			headerStyle.setBorderLeft(border);
			headerStyle.setBorderRight(border);

			// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCOUNT BALANCE IN PULA", "ROWID", "COLUMNID",
					"REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LCR_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LCR_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCUST_ID());
					row.createCell(1).setCellValue(item.getACCT_NUMBER());
					row.createCell(2).setCellValue(item.getACCT_NAME());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getACCT_BALANCE_IN_PULA() != null) {
						balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getREPORT_LABEL());
					row.createCell(5).setCellValue(item.getREPORT_ADDL_CRITERIA_1());
					row.createCell(6)
							.setCellValue(item.getREPORT_DATE() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_LCR — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LCR Excel", e);
			return new byte[0];
		}
	}

	/**
	 * Get archival data for dropdown (returns Object array for UI) Used in the
	 * archival dropdown list on the UI
	 */
	public List<Object> getM_LCRArchival() {
		List<Object> archivalList = new ArrayList<>();
		try {
			String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
					+ "FROM BRRS_M_LCR_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
					+ "ORDER BY REPORT_VERSION DESC";

			archivalList = jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
					rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });

			logger.info("Fetched {} archival records for M_LCR", archivalList.size());

		} catch (Exception e) {
			logger.error("Error fetching M_LCR Archival data: {}", e.getMessage(), e);
			e.printStackTrace();
			// Return empty list to avoid NPE
			return new ArrayList<>();
		}
		return archivalList;
	}

	public byte[] getExcelM_LCRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_LCR_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LCR report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			// This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			// A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		// This try-with-resources block is perfect. It guarantees all resources are
		// closed automatically.
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			// --- Style Definitions ---
			CreationHelper createHelper = workbook.getCreationHelper();

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			// Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			try {

				// Row 5 = Excel row 6
				Row dateRow = sheet.getRow(4);

				if (dateRow == null) {
					dateRow = sheet.createRow(4);
				}

				// Column 2 = Excel column C
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LCR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R12
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR12_bob_total_amount() != null) {
						cell4.setCellValue(record.getR12_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					// R13
					// Column E
					cell4 = row.getCell(4);
					if (record.getR13_bob_total_amount() != null) {
						cell4.setCellValue(record.getR13_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					// R14
					// Column E
					cell4 = row.getCell(4);
					if (record.getR14_bob_total_amount() != null) {
						cell4.setCellValue(record.getR14_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					// R15
					// Column E
					cell4 = row.getCell(4);
					if (record.getR15_bob_total_amount() != null) {
						cell4.setCellValue(record.getR15_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					// R20
					// Column E
					cell4 = row.getCell(4);
					if (record.getR20_bob_total_amount() != null) {
						cell4.setCellValue(record.getR20_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					// R21
					// Column E
					cell4 = row.getCell(4);
					if (record.getR21_bob_total_amount() != null) {
						cell4.setCellValue(record.getR21_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					// R31
					// Column E
					cell4 = row.getCell(4);
					if (record.getR31_bob_total_amount() != null) {
						cell4.setCellValue(record.getR31_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					// R34
					// Column E
					cell4 = row.getCell(4);
					if (record.getR34_bob_total_amount() != null) {
						cell4.setCellValue(record.getR34_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					// R35
					// Column E
					cell4 = row.getCell(4);
					if (record.getR35_bob_total_amount() != null) {
						cell4.setCellValue(record.getR35_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					// R36
					// Column E
					cell4 = row.getCell(4);
					if (record.getR36_bob_total_amount() != null) {
						cell4.setCellValue(record.getR36_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					// R37
					// Column E
					cell4 = row.getCell(4);
					if (record.getR37_bob_total_amount() != null) {
						cell4.setCellValue(record.getR37_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					// R38
					// Column E
					cell4 = row.getCell(4);
					if (record.getR38_bob_total_amount() != null) {
						cell4.setCellValue(record.getR38_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					// R40
					// Column E
					cell4 = row.getCell(4);
					if (record.getR40_bob_total_amount() != null) {
						cell4.setCellValue(record.getR40_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					// R41
					// Column E
					cell4 = row.getCell(4);
					if (record.getR41_bob_total_amount() != null) {
						cell4.setCellValue(record.getR41_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					// R42
					// Column E
					cell4 = row.getCell(4);
					if (record.getR42_bob_total_amount() != null) {
						cell4.setCellValue(record.getR42_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					// R43
					// Column E
					cell4 = row.getCell(4);
					if (record.getR43_bob_total_amount() != null) {
						cell4.setCellValue(record.getR43_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					// R45
					// Column E
					cell4 = row.getCell(4);
					if (record.getR45_bob_total_amount() != null) {
						cell4.setCellValue(record.getR45_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					// R46
					// Column E
					cell4 = row.getCell(4);
					if (record.getR46_bob_total_amount() != null) {
						cell4.setCellValue(record.getR46_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					// R47
					// Column E
					cell4 = row.getCell(4);
					if (record.getR47_bob_total_amount() != null) {
						cell4.setCellValue(record.getR47_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					// R48
					// Column E
					cell4 = row.getCell(4);
					if (record.getR48_bob_total_amount() != null) {
						cell4.setCellValue(record.getR48_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					// R49
					// Column E
					cell4 = row.getCell(4);
					if (record.getR49_bob_total_amount() != null) {
						cell4.setCellValue(record.getR49_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(49);
					// R50
					// Column E
					cell4 = row.getCell(4);
					if (record.getR50_bob_total_amount() != null) {
						cell4.setCellValue(record.getR50_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					// R51
					// Column E
					cell4 = row.getCell(4);
					if (record.getR51_bob_total_amount() != null) {
						cell4.setCellValue(record.getR51_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(51);
					// R52
					// Column E
					cell4 = row.getCell(4);
					if (record.getR52_bob_total_amount() != null) {
						cell4.setCellValue(record.getR52_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(52);
					// R53
					// Column E
					cell4 = row.getCell(4);
					if (record.getR53_bob_total_amount() != null) {
						cell4.setCellValue(record.getR53_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					// R55
					// Column E
					cell4 = row.getCell(4);
					if (record.getR55_bob_total_amount() != null) {
						cell4.setCellValue(record.getR55_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					// R56
					// Column E
					cell4 = row.getCell(4);
					if (record.getR56_bob_total_amount() != null) {
						cell4.setCellValue(record.getR56_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					// R57
					// Column E
					cell4 = row.getCell(4);
					if (record.getR57_bob_total_amount() != null) {
						cell4.setCellValue(record.getR57_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(57);
					// R58
					// Column E
					cell4 = row.getCell(4);
					if (record.getR58_bob_total_amount() != null) {
						cell4.setCellValue(record.getR58_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(58);
					// R59
					// Column E
					cell4 = row.getCell(4);
					if (record.getR59_bob_total_amount() != null) {
						cell4.setCellValue(record.getR59_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					// R60
					// Column E
					cell4 = row.getCell(4);
					if (record.getR60_bob_total_amount() != null) {
						cell4.setCellValue(record.getR60_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					// R61
					// Column E
					cell4 = row.getCell(4);
					if (record.getR61_bob_total_amount() != null) {
						cell4.setCellValue(record.getR61_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					// R62
					// Column E
					cell4 = row.getCell(4);
					if (record.getR62_bob_total_amount() != null) {
						cell4.setCellValue(record.getR62_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(52);
					// R63
					// Column E
					cell4 = row.getCell(4);
					if (record.getR63_bob_total_amount() != null) {
						cell4.setCellValue(record.getR63_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(64);
					// R65
					// Column E
					cell4 = row.getCell(4);
					if (record.getR65_bob_total_amount() != null) {
						cell4.setCellValue(record.getR65_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(65);
					// R66
					// Column E
					cell4 = row.getCell(4);
					if (record.getR66_bob_total_amount() != null) {
						cell4.setCellValue(record.getR66_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(66);
					// R67
					// Column E
					cell4 = row.getCell(4);
					if (record.getR67_bob_total_amount() != null) {
						cell4.setCellValue(record.getR67_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(67);
					// R68
					// Column E
					cell4 = row.getCell(4);
					if (record.getR68_bob_total_amount() != null) {
						cell4.setCellValue(record.getR68_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(68);
					// R69
					// Column E
					cell4 = row.getCell(4);
					if (record.getR69_bob_total_amount() != null) {
						cell4.setCellValue(record.getR69_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(73);
					// R74
					// Column E
					cell4 = row.getCell(4);
					if (record.getR74_bob_total_amount() != null) {
						cell4.setCellValue(record.getR74_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(74);
					// R75
					// Column E
					cell4 = row.getCell(4);
					if (record.getR75_bob_total_amount() != null) {
						cell4.setCellValue(record.getR75_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(75);
					// R76
					// Column E
					cell4 = row.getCell(4);
					if (record.getR76_bob_total_amount() != null) {
						cell4.setCellValue(record.getR76_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(76);
					// R77
					// Column E
					cell4 = row.getCell(4);
					if (record.getR77_bob_total_amount() != null) {
						cell4.setCellValue(record.getR77_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(77);
					// R78
					// Column E
					cell4 = row.getCell(4);
					if (record.getR78_bob_total_amount() != null) {
						cell4.setCellValue(record.getR78_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(78);
					// R79
					// Column E
					cell4 = row.getCell(4);
					if (record.getR79_bob_total_amount() != null) {
						cell4.setCellValue(record.getR79_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(80);
					// R81
					// Column E
					cell4 = row.getCell(4);
					if (record.getR81_bob_total_amount() != null) {
						cell4.setCellValue(record.getR81_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(81);
					// R82
					// Column E
					cell4 = row.getCell(4);
					if (record.getR82_bob_total_amount() != null) {
						cell4.setCellValue(record.getR82_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(82);
					// R83
					// Column E
					cell4 = row.getCell(4);
					if (record.getR83_bob_total_amount() != null) {
						cell4.setCellValue(record.getR83_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(83);
					// R84
					// Column E
					cell4 = row.getCell(4);
					if (record.getR84_bob_total_amount() != null) {
						cell4.setCellValue(record.getR84_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(84);
					// R74
					// Column E
					cell4 = row.getCell(4);
					if (record.getR85_bob_total_amount() != null) {
						cell4.setCellValue(record.getR85_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(87);
					// R88
					// Column E
					cell4 = row.getCell(4);
					if (record.getR88_bob_total_amount() != null) {
						cell4.setCellValue(record.getR88_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(88);
					// R89
					// Column E
					cell4 = row.getCell(4);
					if (record.getR89_bob_total_amount() != null) {
						cell4.setCellValue(record.getR89_bob_total_amount().doubleValue());

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_LCR ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LCRDetails");

			// Common border style
			BorderStyle border = BorderStyle.THIN;

			// Header style (left aligned)
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.LEFT);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(border);
			headerStyle.setBorderBottom(border);
			headerStyle.setBorderLeft(border);
			headerStyle.setBorderRight(border);

			// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "ROWID", "COLUMNID",
					"REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// ✅ Get archival data using JDBC method instead of JPA repo
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			BigDecimal versionDecimal = new BigDecimal(version);

			// ✅ Using JDBC method
			List<M_LCR_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate,
					versionDecimal);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LCR_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCUST_ID());
					row.createCell(1).setCellValue(item.getACCT_NUMBER());
					row.createCell(2).setCellValue(item.getACCT_NAME());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getACCT_BALANCE_IN_PULA() != null) {
						balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getREPORT_LABEL());
					row.createCell(5).setCellValue(item.getREPORT_ADDL_CRITERIA_1());
					row.createCell(6)
							.setCellValue(item.getREPORT_DATE() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_LCR — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LCR Excel", e);
			return new byte[0];
		}
	}

//public boolean updateProvision(M_LCR_Detail_Entity mLCRData) {
//    try {
//        M_LCR_Detail_Entity existing = BRRS_M_LCR_Detail_Repo.findByAcctNumber(mLCRData.getAcctNumber());
//        
//        System.out.println("came to services");
//        if (existing != null) {
//            existing.setProvision(mLCRData.getProvision());
//            existing.setAcctName(mLCRData.getAcctName());
//            
//            
//            BRRS_M_LCR_Detail_Repo.save(existing);
//            
//            return true;
//        } else {
//            System.out.println("Record not found for Account No: " + mLCRData.getAcctNumber());
//            return false;
//        }
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        return false;
//    }
//}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_LCR");

		if (acctNo != null) {
			M_LCR_Detail_Entity mLCREntity = findByAcctnumber(acctNo);
			if (mLCREntity != null && mLCREntity.getREPORT_DATE() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(mLCREntity.getREPORT_DATE());
			}
			mv.addObject("Data", mLCREntity);

		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInPula");
			String provisionStr1 = request.getParameter("debitequivalent");
			String provisionStr2 = request.getParameter("emi");
			String provisionStr3 = request.getParameter("creditequivalent");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_LCR_Detail_Entity existing = findByAcctnumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getACCT_NAME() == null || !existing.getACCT_NAME().equals(acctName)) {
					existing.setACCT_NAME(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			if (provisionStr != null && !provisionStr.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr);
				if (existing.getACCT_BALANCE_IN_PULA() == null
						|| existing.getACCT_BALANCE_IN_PULA().compareTo(newProvision) != 0) {
					existing.setACCT_BALANCE_IN_PULA(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (provisionStr1 != null && !provisionStr1.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr1);
				if (existing.getDEBITEQUIVALENT() == null
						|| existing.getDEBITEQUIVALENT().compareTo(newProvision) != 0) {
					existing.setDEBITEQUIVALENT(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}

			if (provisionStr2 != null && !provisionStr2.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr2);
				if (existing.getEMI() == null || existing.getEMI().compareTo(newProvision) != 0) {
					existing.setEMI(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}

			if (provisionStr3 != null && !provisionStr3.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr3);
				if (existing.getCREDITEQUIVALENT() == null
						|| existing.getCREDITEQUIVALENT().compareTo(newProvision) != 0) {
					existing.setCREDITEQUIVALENT(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}

			if (isChanged) {
				updateDetail(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_LCR_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_LCR_SUMMARY_PROCEDURE(?); END;", formattedDate);
							logger.info("Procedure executed successfully after commit.");
						} catch (Exception e) {
							logger.error("Error executing procedure after commit", e);
						}
					}
				});

				return ResponseEntity.ok("Record updated successfully!");
			} else {
				logger.info("No changes detected for ACCT_NO: {}", acctNo);
				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {
			logger.error("Error updating M_LCR record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	@Transactional
	public void updateDetail(M_LCR_Detail_Entity request) {
		try {
			logger.info("Came to updateDetail service");
			logger.info("Account Number: {}", request.getACCT_NUMBER());

			// Fetch existing record
			M_LCR_Detail_Entity existing = findByAcctnumber(request.getACCT_NUMBER());

			if (existing == null) {
				throw new RuntimeException("Record not found for ACCT_NUMBER: " + request.getACCT_NUMBER());
			}

			// Audit old copy
			M_LCR_Detail_Entity oldcopy = new M_LCR_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			String changes = auditService.getChanges(oldcopy, request);

			if (!changes.isEmpty()) {
				String sql = "UPDATE BRRS_M_LCR_DETAILTABLE SET " + "CUST_ID=?, ACCT_NAME=?, ACCT_BALANCE_IN_PULA=?, "
						+ "DEBIT_EQUIVALENT=?, EMI=?, CREDIT_EQUIVALENT=?, "
						+ "REPORT_LABEL=?, REPORT_ADDL_CRITERIA_1=?, " + "MODIFY_FLG=? " + "WHERE ACCT_NUMBER=?";

				int count = jdbcTemplate.update(sql, request.getCUST_ID(), request.getACCT_NAME(),
						request.getACCT_BALANCE_IN_PULA(), request.getDEBITEQUIVALENT(), request.getEMI(),
						request.getCREDITEQUIVALENT(), request.getREPORT_LABEL(), request.getREPORT_ADDL_CRITERIA_1(),
						"Y", // MODIFY_FLG
						request.getACCT_NUMBER());

				if (count > 0) {
					auditService.compareEntitiesmanual(oldcopy, request, request.getACCT_NUMBER(),
							"M LCR Detail Screen", "BRRS_M_LCR_DETAILTABLE");
					logger.info("Audit completed for ACCT_NUMBER: {}", request.getACCT_NUMBER());
					logger.info("M_LCR Detail Updated Successfully. Rows Updated: {}", count);

					// Call summary procedure after successful update
					if (request.getREPORT_DATE() != null) {
						String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(request.getREPORT_DATE());
						logger.info("Calling BRRS_M_LCR_SUMMARY_PROCEDURE for date: {}", formattedDate);
						jdbcTemplate.update("BEGIN BRRS_M_LCR_SUMMARY_PROCEDURE(?); END;", formattedDate);
						logger.info("Summary procedure executed successfully.");
					}
				}
			} else {
				logger.info("No changes detected for ACCT_NUMBER: {}", request.getACCT_NUMBER());
			}
		} catch (Exception e) {
			logger.error("Error while updating BRRS_M_LCR Detail", e);
			throw new RuntimeException("Error while updating BRRS_M_LCR Detail", e);
		}
	}

	@Transactional
	public void updateReport(M_LCR_Summary_Entity updatedEntity) {
		logger.info("Came to M_LCR Update");
		logger.info("Report Date: {}", updatedEntity.getREPORT_DATE());

		// Fetch existing summary record for audit
		M_LCR_Summary_Entity existingSummary = findSummaryByReportDate(updatedEntity.getREPORT_DATE());

		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getREPORT_DATE());
		}

		// Audit old copy
		M_LCR_Summary_Entity oldcopy = new M_LCR_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);

		try {
			if (updatedEntity.getR14_bob_total_amount() != null) {
				existingSummary.setR14_bob_total_amount(updatedEntity.getR14_bob_total_amount());
				String sql = "UPDATE BRRS_M_LCR_SUMMARYTABLE SET R14_BOB_TOTAL_AMOUNT = ? WHERE REPORT_DATE = ?";
				jdbcTemplate.update(sql, updatedEntity.getR14_bob_total_amount(), updatedEntity.getREPORT_DATE());
			}

			if (updatedEntity.getR31_bob_total_amount() != null) {
				existingSummary.setR31_bob_total_amount(updatedEntity.getR31_bob_total_amount());
				String sql = "UPDATE BRRS_M_LCR_SUMMARYTABLE SET R31_BOB_TOTAL_AMOUNT = ? WHERE REPORT_DATE = ?";
				jdbcTemplate.update(sql, updatedEntity.getR31_bob_total_amount(), updatedEntity.getREPORT_DATE());
			}

			if (updatedEntity.getR38_bob_total_amount() != null) {
				existingSummary.setR38_bob_total_amount(updatedEntity.getR38_bob_total_amount());
				String sql = "UPDATE BRRS_M_LCR_SUMMARYTABLE SET R38_BOB_TOTAL_AMOUNT = ? WHERE REPORT_DATE = ?";
				jdbcTemplate.update(sql, updatedEntity.getR38_bob_total_amount(), updatedEntity.getREPORT_DATE());
			}

			// Audit only if changes found
			String changes = auditService.getChanges(oldcopy, existingSummary);
			if (!changes.isEmpty()) {
				auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getREPORT_DATE().toString(),
						"M LCR Summary Screen", "BRRS_M_LCR_SUMMARY");
			}

			logger.info("M_LCR Summary Update Completed");
		} catch (Exception e) {
			logger.error("Error while updating M_LCR fields", e);
			throw new RuntimeException("Error while updating M_LCR fields", e);
		}
	}
}
