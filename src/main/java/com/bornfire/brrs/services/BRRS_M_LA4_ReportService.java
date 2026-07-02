package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.IdClass;
import javax.persistence.PersistenceContext;
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
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_M_LA4_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA4_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Fetch data by report date
	public List<M_LA4_Summary_Entity1> getDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA4_SUMMARYTABLE1 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LA4RowMapper1());
	}

	public List<M_LA4_Summary_Entity2> getDataByDate2(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA4_SUMMARYTABLE2 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LA4RowMapper2());
	}
	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getM_LA4Archival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY1 "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<M_LA4_Archival_Summary_Entity1> getdatabydateListarchival1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY1 " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA4ArchivalRowMapper1());
	}
//GET ALL WITH VERSION

	public List<M_LA4_Archival_Summary_Entity1> getdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY1 " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_LA4ArchivalRowMapper1());
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<M_LA4_Archival_Summary_Entity2> getdatabydateListarchival2(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY2 " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA4ArchivalRowMapper2());
	}
//GET ALL WITH VERSION

	public List<M_LA4_Archival_Summary_Entity2> getdatabydateListWithVersion2() {

		String sql = "SELECT * FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY2 " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_LA4ArchivalRowMapper2());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY1 "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	public BigDecimal findMaxVersion2(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY2 "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<M_LA4_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_LA4_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_LA4DetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<M_LA4_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA4_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LA4DetailRowMapper());
	}

// 3. PAGINATION

	public List<M_LA4_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_LA4_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_LA4DetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_LA4_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<M_LA4_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA4_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LA4DetailRowMapper());
	}
// 6. BY ACCOUNT NUMBER

	public M_LA4_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_M_LA4_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new M_LA4DetailRowMapper());
	}

	public M_LA4_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_M_LA4_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new M_LA4DetailRowMapper());
	}

	public M_LA4_Detail_Entity findBySnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_M_LA4_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new M_LA4DetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<M_LA4_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA4_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LA4ArchivalDetailRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<M_LA4_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA4_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LA4ArchivalDetailRowMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT CASE " + "         WHEN ? = MAX(REPORT_VERSION) THEN 'YES' " + "         ELSE 'NO' "
				+ "       END AS IS_HIGHEST " + "FROM ( " + "       SELECT REPORT_VERSION "
				+ "       FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY1 " + "       WHERE REPORT_DATE = ? "
				+ "       UNION ALL " + "       SELECT REPORT_VERSION "
				+ "       FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY2 " + "       WHERE REPORT_DATE = ? " + "     )";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE, REPORT_DATE },
				String.class);
	}
	// ROW MAPPER

	class M_LA4RowMapper1 implements RowMapper<M_LA4_Summary_Entity1> {

		@Override
		public M_LA4_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA4_Summary_Entity1 obj = new M_LA4_Summary_Entity1();

			// R11
			obj.setR11AdvancesByInstitutionalSector(rs.getString("R11_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR11Overdrafts(rs.getBigDecimal("R11_OVERDRAFTS"));
			obj.setR11OtherInstallmentLoans(rs.getBigDecimal("R11_OTHER_INSTALLMENT_LOANS"));
			obj.setR11Total(rs.getBigDecimal("R11_TOTAL"));

			// R12
			obj.setR12AdvancesByInstitutionalSector(rs.getString("R12_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR12Overdrafts(rs.getBigDecimal("R12_OVERDRAFTS"));
			obj.setR12OtherInstallmentLoans(rs.getBigDecimal("R12_OTHER_INSTALLMENT_LOANS"));
			obj.setR12Total(rs.getBigDecimal("R12_TOTAL"));

			// R13
			obj.setR13AdvancesByInstitutionalSector(rs.getString("R13_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR13Overdrafts(rs.getBigDecimal("R13_OVERDRAFTS"));
			obj.setR13OtherInstallmentLoans(rs.getBigDecimal("R13_OTHER_INSTALLMENT_LOANS"));
			obj.setR13Total(rs.getBigDecimal("R13_TOTAL"));

			// R14
			obj.setR14AdvancesByInstitutionalSector(rs.getString("R14_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR14Overdrafts(rs.getBigDecimal("R14_OVERDRAFTS"));
			obj.setR14OtherInstallmentLoans(rs.getBigDecimal("R14_OTHER_INSTALLMENT_LOANS"));
			obj.setR14Total(rs.getBigDecimal("R14_TOTAL"));

			// R15
			obj.setR15AdvancesByInstitutionalSector(rs.getString("R15_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR15Overdrafts(rs.getBigDecimal("R15_OVERDRAFTS"));
			obj.setR15OtherInstallmentLoans(rs.getBigDecimal("R15_OTHER_INSTALLMENT_LOANS"));
			obj.setR15Total(rs.getBigDecimal("R15_TOTAL"));

			// R16
			obj.setR16AdvancesByInstitutionalSector(rs.getString("R16_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR16Overdrafts(rs.getBigDecimal("R16_OVERDRAFTS"));
			obj.setR16OtherInstallmentLoans(rs.getBigDecimal("R16_OTHER_INSTALLMENT_LOANS"));
			obj.setR16Total(rs.getBigDecimal("R16_TOTAL"));

			// R17
			obj.setR17AdvancesByInstitutionalSector(rs.getString("R17_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR17Overdrafts(rs.getBigDecimal("R17_OVERDRAFTS"));
			obj.setR17OtherInstallmentLoans(rs.getBigDecimal("R17_OTHER_INSTALLMENT_LOANS"));
			obj.setR17Total(rs.getBigDecimal("R17_TOTAL"));

			// R18
			obj.setR18AdvancesByInstitutionalSector(rs.getString("R18_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR18Overdrafts(rs.getBigDecimal("R18_OVERDRAFTS"));
			obj.setR18OtherInstallmentLoans(rs.getBigDecimal("R18_OTHER_INSTALLMENT_LOANS"));
			obj.setR18Total(rs.getBigDecimal("R18_TOTAL"));

			// R19
			obj.setR19AdvancesByInstitutionalSector(rs.getString("R19_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR19Overdrafts(rs.getBigDecimal("R19_OVERDRAFTS"));
			obj.setR19OtherInstallmentLoans(rs.getBigDecimal("R19_OTHER_INSTALLMENT_LOANS"));
			obj.setR19Total(rs.getBigDecimal("R19_TOTAL"));

			// R20
			obj.setR20AdvancesByInstitutionalSector(rs.getString("R20_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR20Overdrafts(rs.getBigDecimal("R20_OVERDRAFTS"));
			obj.setR20OtherInstallmentLoans(rs.getBigDecimal("R20_OTHER_INSTALLMENT_LOANS"));
			obj.setR20Total(rs.getBigDecimal("R20_TOTAL"));

			// R21
			obj.setR21AdvancesByInstitutionalSector(rs.getString("R21_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR21Overdrafts(rs.getBigDecimal("R21_OVERDRAFTS"));
			obj.setR21OtherInstallmentLoans(rs.getBigDecimal("R21_OTHER_INSTALLMENT_LOANS"));
			obj.setR21Total(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22AdvancesByInstitutionalSector(rs.getString("R22_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR22Overdrafts(rs.getBigDecimal("R22_OVERDRAFTS"));
			obj.setR22OtherInstallmentLoans(rs.getBigDecimal("R22_OTHER_INSTALLMENT_LOANS"));
			obj.setR22Total(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23AdvancesByInstitutionalSector(rs.getString("R23_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR23Overdrafts(rs.getBigDecimal("R23_OVERDRAFTS"));
			obj.setR23OtherInstallmentLoans(rs.getBigDecimal("R23_OTHER_INSTALLMENT_LOANS"));
			obj.setR23Total(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24AdvancesByInstitutionalSector(rs.getString("R24_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR24Overdrafts(rs.getBigDecimal("R24_OVERDRAFTS"));
			obj.setR24OtherInstallmentLoans(rs.getBigDecimal("R24_OTHER_INSTALLMENT_LOANS"));
			obj.setR24Total(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25AdvancesByInstitutionalSector(rs.getString("R25_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR25Overdrafts(rs.getBigDecimal("R25_OVERDRAFTS"));
			obj.setR25OtherInstallmentLoans(rs.getBigDecimal("R25_OTHER_INSTALLMENT_LOANS"));
			obj.setR25Total(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26AdvancesByInstitutionalSector(rs.getString("R26_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR26Overdrafts(rs.getBigDecimal("R26_OVERDRAFTS"));
			obj.setR26OtherInstallmentLoans(rs.getBigDecimal("R26_OTHER_INSTALLMENT_LOANS"));
			obj.setR26Total(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27AdvancesByInstitutionalSector(rs.getString("R27_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR27Overdrafts(rs.getBigDecimal("R27_OVERDRAFTS"));
			obj.setR27OtherInstallmentLoans(rs.getBigDecimal("R27_OTHER_INSTALLMENT_LOANS"));
			obj.setR27Total(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28AdvancesByInstitutionalSector(rs.getString("R28_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR28Overdrafts(rs.getBigDecimal("R28_OVERDRAFTS"));
			obj.setR28OtherInstallmentLoans(rs.getBigDecimal("R28_OTHER_INSTALLMENT_LOANS"));
			obj.setR28Total(rs.getBigDecimal("R28_TOTAL"));

			// R29
			obj.setR29AdvancesByInstitutionalSector(rs.getString("R29_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR29Overdrafts(rs.getBigDecimal("R29_OVERDRAFTS"));
			obj.setR29OtherInstallmentLoans(rs.getBigDecimal("R29_OTHER_INSTALLMENT_LOANS"));
			obj.setR29Total(rs.getBigDecimal("R29_TOTAL"));

			// R30
			obj.setR30AdvancesByInstitutionalSector(rs.getString("R30_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR30Overdrafts(rs.getBigDecimal("R30_OVERDRAFTS"));
			obj.setR30OtherInstallmentLoans(rs.getBigDecimal("R30_OTHER_INSTALLMENT_LOANS"));
			obj.setR30Total(rs.getBigDecimal("R30_TOTAL"));

			// R31
			obj.setR31AdvancesByInstitutionalSector(rs.getString("R31_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR31Overdrafts(rs.getBigDecimal("R31_OVERDRAFTS"));
			obj.setR31OtherInstallmentLoans(rs.getBigDecimal("R31_OTHER_INSTALLMENT_LOANS"));
			obj.setR31Total(rs.getBigDecimal("R31_TOTAL"));

			// R32
			obj.setR32AdvancesByInstitutionalSector(rs.getString("R32_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR32Overdrafts(rs.getBigDecimal("R32_OVERDRAFTS"));
			obj.setR32OtherInstallmentLoans(rs.getBigDecimal("R32_OTHER_INSTALLMENT_LOANS"));
			obj.setR32Total(rs.getBigDecimal("R32_TOTAL"));

			// R33
			obj.setR33AdvancesByInstitutionalSector(rs.getString("R33_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR33Overdrafts(rs.getBigDecimal("R33_OVERDRAFTS"));
			obj.setR33OtherInstallmentLoans(rs.getBigDecimal("R33_OTHER_INSTALLMENT_LOANS"));
			obj.setR33Total(rs.getBigDecimal("R33_TOTAL"));

			// R34
			obj.setR34AdvancesByInstitutionalSector(rs.getString("R34_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR34Overdrafts(rs.getBigDecimal("R34_OVERDRAFTS"));
			obj.setR34OtherInstallmentLoans(rs.getBigDecimal("R34_OTHER_INSTALLMENT_LOANS"));
			obj.setR34Total(rs.getBigDecimal("R34_TOTAL"));

			// R35
			obj.setR35AdvancesByInstitutionalSector(rs.getString("R35_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR35Overdrafts(rs.getBigDecimal("R35_OVERDRAFTS"));
			obj.setR35OtherInstallmentLoans(rs.getBigDecimal("R35_OTHER_INSTALLMENT_LOANS"));
			obj.setR35Total(rs.getBigDecimal("R35_TOTAL"));

			// R36
			obj.setR36AdvancesByInstitutionalSector(rs.getString("R36_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR36Overdrafts(rs.getBigDecimal("R36_OVERDRAFTS"));
			obj.setR36OtherInstallmentLoans(rs.getBigDecimal("R36_OTHER_INSTALLMENT_LOANS"));
			obj.setR36Total(rs.getBigDecimal("R36_TOTAL"));

			// R37
			obj.setR37AdvancesByInstitutionalSector(rs.getString("R37_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR37Overdrafts(rs.getBigDecimal("R37_OVERDRAFTS"));
			obj.setR37OtherInstallmentLoans(rs.getBigDecimal("R37_OTHER_INSTALLMENT_LOANS"));
			obj.setR37Total(rs.getBigDecimal("R37_TOTAL"));

			// R38
			obj.setR38AdvancesByInstitutionalSector(rs.getString("R38_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR38Overdrafts(rs.getBigDecimal("R38_OVERDRAFTS"));
			obj.setR38OtherInstallmentLoans(rs.getBigDecimal("R38_OTHER_INSTALLMENT_LOANS"));
			obj.setR38Total(rs.getBigDecimal("R38_TOTAL"));

			// R39
			obj.setR39AdvancesByInstitutionalSector(rs.getString("R39_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR39Overdrafts(rs.getBigDecimal("R39_OVERDRAFTS"));
			obj.setR39OtherInstallmentLoans(rs.getBigDecimal("R39_OTHER_INSTALLMENT_LOANS"));
			obj.setR39Total(rs.getBigDecimal("R39_TOTAL"));

			// R40
			obj.setR40AdvancesByInstitutionalSector(rs.getString("R40_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR40Overdrafts(rs.getBigDecimal("R40_OVERDRAFTS"));
			obj.setR40OtherInstallmentLoans(rs.getBigDecimal("R40_OTHER_INSTALLMENT_LOANS"));
			obj.setR40Total(rs.getBigDecimal("R40_TOTAL"));

			// R41
			obj.setR41AdvancesByInstitutionalSector(rs.getString("R41_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR41Overdrafts(rs.getBigDecimal("R41_OVERDRAFTS"));
			obj.setR41OtherInstallmentLoans(rs.getBigDecimal("R41_OTHER_INSTALLMENT_LOANS"));
			obj.setR41Total(rs.getBigDecimal("R41_TOTAL"));

			// R42
			obj.setR42AdvancesByInstitutionalSector(rs.getString("R42_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR42Overdrafts(rs.getBigDecimal("R42_OVERDRAFTS"));
			obj.setR42OtherInstallmentLoans(rs.getBigDecimal("R42_OTHER_INSTALLMENT_LOANS"));
			obj.setR42Total(rs.getBigDecimal("R42_TOTAL"));

			// R43
			obj.setR43AdvancesByInstitutionalSector(rs.getString("R43_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR43Overdrafts(rs.getBigDecimal("R43_OVERDRAFTS"));
			obj.setR43OtherInstallmentLoans(rs.getBigDecimal("R43_OTHER_INSTALLMENT_LOANS"));
			obj.setR43Total(rs.getBigDecimal("R43_TOTAL"));

			// R44
			obj.setR44AdvancesByInstitutionalSector(rs.getString("R44_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR44Overdrafts(rs.getBigDecimal("R44_OVERDRAFTS"));
			obj.setR44OtherInstallmentLoans(rs.getBigDecimal("R44_OTHER_INSTALLMENT_LOANS"));
			obj.setR44Total(rs.getBigDecimal("R44_TOTAL"));

			// R45
			obj.setR45AdvancesByInstitutionalSector(rs.getString("R45_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR45Overdrafts(rs.getBigDecimal("R45_OVERDRAFTS"));
			obj.setR45OtherInstallmentLoans(rs.getBigDecimal("R45_OTHER_INSTALLMENT_LOANS"));
			obj.setR45Total(rs.getBigDecimal("R45_TOTAL"));

			// R46
			obj.setR46AdvancesByInstitutionalSector(rs.getString("R46_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR46Overdrafts(rs.getBigDecimal("R46_OVERDRAFTS"));
			obj.setR46OtherInstallmentLoans(rs.getBigDecimal("R46_OTHER_INSTALLMENT_LOANS"));
			obj.setR46Total(rs.getBigDecimal("R46_TOTAL"));

			// R47
			obj.setR47AdvancesByInstitutionalSector(rs.getString("R47_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR47Overdrafts(rs.getBigDecimal("R47_OVERDRAFTS"));
			obj.setR47OtherInstallmentLoans(rs.getBigDecimal("R47_OTHER_INSTALLMENT_LOANS"));
			obj.setR47Total(rs.getBigDecimal("R47_TOTAL"));

			// R48
			obj.setR48AdvancesByInstitutionalSector(rs.getString("R48_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR48Overdrafts(rs.getBigDecimal("R48_OVERDRAFTS"));
			obj.setR48OtherInstallmentLoans(rs.getBigDecimal("R48_OTHER_INSTALLMENT_LOANS"));
			obj.setR48Total(rs.getBigDecimal("R48_TOTAL"));

			// R49
			obj.setR49AdvancesByInstitutionalSector(rs.getString("R49_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR49Overdrafts(rs.getBigDecimal("R49_OVERDRAFTS"));
			obj.setR49OtherInstallmentLoans(rs.getBigDecimal("R49_OTHER_INSTALLMENT_LOANS"));
			obj.setR49Total(rs.getBigDecimal("R49_TOTAL"));

			// R50
			obj.setR50AdvancesByInstitutionalSector(rs.getString("R50_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR50Overdrafts(rs.getBigDecimal("R50_OVERDRAFTS"));
			obj.setR50OtherInstallmentLoans(rs.getBigDecimal("R50_OTHER_INSTALLMENT_LOANS"));
			obj.setR50Total(rs.getBigDecimal("R50_TOTAL"));

			// R51
			obj.setR51AdvancesByInstitutionalSector(rs.getString("R51_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR51Overdrafts(rs.getBigDecimal("R51_OVERDRAFTS"));
			obj.setR51OtherInstallmentLoans(rs.getBigDecimal("R51_OTHER_INSTALLMENT_LOANS"));
			obj.setR51Total(rs.getBigDecimal("R51_TOTAL"));

			// R52
			obj.setR52AdvancesByInstitutionalSector(rs.getString("R52_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR52Overdrafts(rs.getBigDecimal("R52_OVERDRAFTS"));
			obj.setR52OtherInstallmentLoans(rs.getBigDecimal("R52_OTHER_INSTALLMENT_LOANS"));
			obj.setR52Total(rs.getBigDecimal("R52_TOTAL"));

			// R53
			obj.setR53AdvancesByInstitutionalSector(rs.getString("R53_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR53Overdrafts(rs.getBigDecimal("R53_OVERDRAFTS"));
			obj.setR53OtherInstallmentLoans(rs.getBigDecimal("R53_OTHER_INSTALLMENT_LOANS"));
			obj.setR53Total(rs.getBigDecimal("R53_TOTAL"));

			// R54
			obj.setR54AdvancesByInstitutionalSector(rs.getString("R54_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR54Overdrafts(rs.getBigDecimal("R54_OVERDRAFTS"));
			obj.setR54OtherInstallmentLoans(rs.getBigDecimal("R54_OTHER_INSTALLMENT_LOANS"));
			obj.setR54Total(rs.getBigDecimal("R54_TOTAL"));

			// R55
			obj.setR55AdvancesByInstitutionalSector(rs.getString("R55_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR55Overdrafts(rs.getBigDecimal("R55_OVERDRAFTS"));
			obj.setR55OtherInstallmentLoans(rs.getBigDecimal("R55_OTHER_INSTALLMENT_LOANS"));
			obj.setR55Total(rs.getBigDecimal("R55_TOTAL"));

			// R56
			obj.setR56AdvancesByInstitutionalSector(rs.getString("R56_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR56Overdrafts(rs.getBigDecimal("R56_OVERDRAFTS"));
			obj.setR56OtherInstallmentLoans(rs.getBigDecimal("R56_OTHER_INSTALLMENT_LOANS"));
			obj.setR56Total(rs.getBigDecimal("R56_TOTAL"));

			// R57
			obj.setR57AdvancesByInstitutionalSector(rs.getString("R57_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR57Overdrafts(rs.getBigDecimal("R57_OVERDRAFTS"));
			obj.setR57OtherInstallmentLoans(rs.getBigDecimal("R57_OTHER_INSTALLMENT_LOANS"));
			obj.setR57Total(rs.getBigDecimal("R57_TOTAL"));

			// R58
			obj.setR58AdvancesByInstitutionalSector(rs.getString("R58_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR58Overdrafts(rs.getBigDecimal("R58_OVERDRAFTS"));
			obj.setR58OtherInstallmentLoans(rs.getBigDecimal("R58_OTHER_INSTALLMENT_LOANS"));
			obj.setR58Total(rs.getBigDecimal("R58_TOTAL"));

			// R59
			obj.setR59AdvancesByInstitutionalSector(rs.getString("R59_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR59Overdrafts(rs.getBigDecimal("R59_OVERDRAFTS"));
			obj.setR59OtherInstallmentLoans(rs.getBigDecimal("R59_OTHER_INSTALLMENT_LOANS"));
			obj.setR59Total(rs.getBigDecimal("R59_TOTAL"));

			// R60
			obj.setR60AdvancesByInstitutionalSector(rs.getString("R60_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR60Overdrafts(rs.getBigDecimal("R60_OVERDRAFTS"));
			obj.setR60OtherInstallmentLoans(rs.getBigDecimal("R60_OTHER_INSTALLMENT_LOANS"));
			obj.setR60Total(rs.getBigDecimal("R60_TOTAL"));

			// R61
			obj.setR61AdvancesByInstitutionalSector(rs.getString("R61_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR61Overdrafts(rs.getBigDecimal("R61_OVERDRAFTS"));
			obj.setR61OtherInstallmentLoans(rs.getBigDecimal("R61_OTHER_INSTALLMENT_LOANS"));
			obj.setR61Total(rs.getBigDecimal("R61_TOTAL"));

			// R62
			obj.setR62AdvancesByInstitutionalSector(rs.getString("R62_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR62Overdrafts(rs.getBigDecimal("R62_OVERDRAFTS"));
			obj.setR62OtherInstallmentLoans(rs.getBigDecimal("R62_OTHER_INSTALLMENT_LOANS"));
			obj.setR62Total(rs.getBigDecimal("R62_TOTAL"));

			// R63
			obj.setR63AdvancesByInstitutionalSector(rs.getString("R63_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR63Overdrafts(rs.getBigDecimal("R63_OVERDRAFTS"));
			obj.setR63OtherInstallmentLoans(rs.getBigDecimal("R63_OTHER_INSTALLMENT_LOANS"));
			obj.setR63Total(rs.getBigDecimal("R63_TOTAL"));

			// R64
			obj.setR64AdvancesByInstitutionalSector(rs.getString("R64_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR64Overdrafts(rs.getBigDecimal("R64_OVERDRAFTS"));
			obj.setR64OtherInstallmentLoans(rs.getBigDecimal("R64_OTHER_INSTALLMENT_LOANS"));
			obj.setR64Total(rs.getBigDecimal("R64_TOTAL"));
			// COMMON FIELDS
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

	public static class M_LA4_Summary_Entity1 {

		@Column(name = "R11_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r11AdvancesByInstitutionalSector;

		@Column(name = "R11_OVERDRAFTS")
		private BigDecimal r11Overdrafts;

		@Column(name = "R11_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r11OtherInstallmentLoans;

		@Column(name = "R11_TOTAL")
		private BigDecimal r11Total;

		@Column(name = "R12_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r12AdvancesByInstitutionalSector;

		@Column(name = "R12_OVERDRAFTS")
		private BigDecimal r12Overdrafts;

		@Column(name = "R12_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r12OtherInstallmentLoans;

		@Column(name = "R12_TOTAL")
		private BigDecimal r12Total;

		@Column(name = "R13_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r13AdvancesByInstitutionalSector;

		@Column(name = "R13_OVERDRAFTS")
		private BigDecimal r13Overdrafts;

		@Column(name = "R13_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r13OtherInstallmentLoans;

		@Column(name = "R13_TOTAL")
		private BigDecimal r13Total;

		@Column(name = "R14_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r14AdvancesByInstitutionalSector;

		@Column(name = "R14_OVERDRAFTS")
		private BigDecimal r14Overdrafts;

		@Column(name = "R14_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r14OtherInstallmentLoans;

		@Column(name = "R14_TOTAL")
		private BigDecimal r14Total;

		@Column(name = "R15_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r15AdvancesByInstitutionalSector;

		@Column(name = "R15_OVERDRAFTS")
		private BigDecimal r15Overdrafts;

		@Column(name = "R15_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r15OtherInstallmentLoans;

		@Column(name = "R15_TOTAL")
		private BigDecimal r15Total;

		@Column(name = "R16_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r16AdvancesByInstitutionalSector;

		@Column(name = "R16_OVERDRAFTS")
		private BigDecimal r16Overdrafts;

		@Column(name = "R16_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r16OtherInstallmentLoans;

		@Column(name = "R16_TOTAL")
		private BigDecimal r16Total;

		@Column(name = "R17_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r17AdvancesByInstitutionalSector;

		@Column(name = "R17_OVERDRAFTS")
		private BigDecimal r17Overdrafts;

		@Column(name = "R17_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r17OtherInstallmentLoans;

		@Column(name = "R17_TOTAL")
		private BigDecimal r17Total;

		@Column(name = "R18_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r18AdvancesByInstitutionalSector;

		@Column(name = "R18_OVERDRAFTS")
		private BigDecimal r18Overdrafts;

		@Column(name = "R18_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r18OtherInstallmentLoans;

		@Column(name = "R18_TOTAL")
		private BigDecimal r18Total;

		@Column(name = "R19_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r19AdvancesByInstitutionalSector;

		@Column(name = "R19_OVERDRAFTS")
		private BigDecimal r19Overdrafts;

		@Column(name = "R19_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r19OtherInstallmentLoans;

		@Column(name = "R19_TOTAL")
		private BigDecimal r19Total;

		@Column(name = "R20_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r20AdvancesByInstitutionalSector;

		@Column(name = "R20_OVERDRAFTS")
		private BigDecimal r20Overdrafts;

		@Column(name = "R20_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r20OtherInstallmentLoans;

		@Column(name = "R20_TOTAL")
		private BigDecimal r20Total;

		@Column(name = "R21_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r21AdvancesByInstitutionalSector;

		@Column(name = "R21_OVERDRAFTS")
		private BigDecimal r21Overdrafts;

		@Column(name = "R21_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r21OtherInstallmentLoans;

		@Column(name = "R21_TOTAL")
		private BigDecimal r21Total;

		@Column(name = "R22_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r22AdvancesByInstitutionalSector;

		@Column(name = "R22_OVERDRAFTS")
		private BigDecimal r22Overdrafts;

		@Column(name = "R22_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r22OtherInstallmentLoans;

		@Column(name = "R22_TOTAL")
		private BigDecimal r22Total;

		@Column(name = "R23_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r23AdvancesByInstitutionalSector;

		@Column(name = "R23_OVERDRAFTS")
		private BigDecimal r23Overdrafts;

		@Column(name = "R23_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r23OtherInstallmentLoans;

		@Column(name = "R23_TOTAL")
		private BigDecimal r23Total;

		@Column(name = "R24_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r24AdvancesByInstitutionalSector;

		@Column(name = "R24_OVERDRAFTS")
		private BigDecimal r24Overdrafts;

		@Column(name = "R24_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r24OtherInstallmentLoans;

		@Column(name = "R24_TOTAL")
		private BigDecimal r24Total;

		@Column(name = "R25_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r25AdvancesByInstitutionalSector;

		@Column(name = "R25_OVERDRAFTS")
		private BigDecimal r25Overdrafts;

		@Column(name = "R25_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r25OtherInstallmentLoans;

		@Column(name = "R25_TOTAL")
		private BigDecimal r25Total;

		@Column(name = "R26_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r26AdvancesByInstitutionalSector;

		@Column(name = "R26_OVERDRAFTS")
		private BigDecimal r26Overdrafts;

		@Column(name = "R26_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r26OtherInstallmentLoans;

		@Column(name = "R26_TOTAL")
		private BigDecimal r26Total;

		@Column(name = "R27_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r27AdvancesByInstitutionalSector;

		@Column(name = "R27_OVERDRAFTS")
		private BigDecimal r27Overdrafts;

		@Column(name = "R27_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r27OtherInstallmentLoans;

		@Column(name = "R27_TOTAL")
		private BigDecimal r27Total;

		@Column(name = "R28_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r28AdvancesByInstitutionalSector;

		@Column(name = "R28_OVERDRAFTS")
		private BigDecimal r28Overdrafts;

		@Column(name = "R28_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r28OtherInstallmentLoans;

		@Column(name = "R28_TOTAL")
		private BigDecimal r28Total;

		@Column(name = "R29_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r29AdvancesByInstitutionalSector;

		@Column(name = "R29_OVERDRAFTS")
		private BigDecimal r29Overdrafts;

		@Column(name = "R29_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r29OtherInstallmentLoans;

		@Column(name = "R29_TOTAL")
		private BigDecimal r29Total;

		@Column(name = "R30_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r30AdvancesByInstitutionalSector;

		@Column(name = "R30_OVERDRAFTS")
		private BigDecimal r30Overdrafts;

		@Column(name = "R30_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r30OtherInstallmentLoans;

		@Column(name = "R30_TOTAL")
		private BigDecimal r30Total;

		@Column(name = "R31_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r31AdvancesByInstitutionalSector;

		@Column(name = "R31_OVERDRAFTS")
		private BigDecimal r31Overdrafts;

		@Column(name = "R31_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r31OtherInstallmentLoans;

		@Column(name = "R31_TOTAL")
		private BigDecimal r31Total;

		@Column(name = "R32_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r32AdvancesByInstitutionalSector;

		@Column(name = "R32_OVERDRAFTS")
		private BigDecimal r32Overdrafts;

		@Column(name = "R32_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r32OtherInstallmentLoans;

		@Column(name = "R32_TOTAL")
		private BigDecimal r32Total;

		@Column(name = "R33_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r33AdvancesByInstitutionalSector;

		@Column(name = "R33_OVERDRAFTS")
		private BigDecimal r33Overdrafts;

		@Column(name = "R33_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r33OtherInstallmentLoans;

		@Column(name = "R33_TOTAL")
		private BigDecimal r33Total;

		@Column(name = "R34_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r34AdvancesByInstitutionalSector;

		@Column(name = "R34_OVERDRAFTS")
		private BigDecimal r34Overdrafts;

		@Column(name = "R34_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r34OtherInstallmentLoans;

		@Column(name = "R34_TOTAL")
		private BigDecimal r34Total;

		@Column(name = "R35_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r35AdvancesByInstitutionalSector;

		@Column(name = "R35_OVERDRAFTS")
		private BigDecimal r35Overdrafts;

		@Column(name = "R35_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r35OtherInstallmentLoans;

		@Column(name = "R35_TOTAL")
		private BigDecimal r35Total;

		@Column(name = "R36_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r36AdvancesByInstitutionalSector;

		@Column(name = "R36_OVERDRAFTS")
		private BigDecimal r36Overdrafts;

		@Column(name = "R36_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r36OtherInstallmentLoans;

		@Column(name = "R36_TOTAL")
		private BigDecimal r36Total;

		@Column(name = "R37_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r37AdvancesByInstitutionalSector;

		@Column(name = "R37_OVERDRAFTS")
		private BigDecimal r37Overdrafts;

		@Column(name = "R37_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r37OtherInstallmentLoans;

		@Column(name = "R37_TOTAL")
		private BigDecimal r37Total;

		@Column(name = "R38_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r38AdvancesByInstitutionalSector;

		@Column(name = "R38_OVERDRAFTS")
		private BigDecimal r38Overdrafts;

		@Column(name = "R38_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r38OtherInstallmentLoans;

		@Column(name = "R38_TOTAL")
		private BigDecimal r38Total;

		@Column(name = "R39_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r39AdvancesByInstitutionalSector;

		@Column(name = "R39_OVERDRAFTS")
		private BigDecimal r39Overdrafts;

		@Column(name = "R39_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r39OtherInstallmentLoans;

		@Column(name = "R39_TOTAL")
		private BigDecimal r39Total;

		@Column(name = "R40_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r40AdvancesByInstitutionalSector;

		@Column(name = "R40_OVERDRAFTS")
		private BigDecimal r40Overdrafts;

		@Column(name = "R40_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r40OtherInstallmentLoans;

		@Column(name = "R40_TOTAL")
		private BigDecimal r40Total;

		@Column(name = "R41_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r41AdvancesByInstitutionalSector;

		@Column(name = "R41_OVERDRAFTS")
		private BigDecimal r41Overdrafts;

		@Column(name = "R41_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r41OtherInstallmentLoans;

		@Column(name = "R41_TOTAL")
		private BigDecimal r41Total;

		@Column(name = "R42_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r42AdvancesByInstitutionalSector;

		@Column(name = "R42_OVERDRAFTS")
		private BigDecimal r42Overdrafts;

		@Column(name = "R42_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r42OtherInstallmentLoans;

		@Column(name = "R42_TOTAL")
		private BigDecimal r42Total;

		@Column(name = "R43_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r43AdvancesByInstitutionalSector;

		@Column(name = "R43_OVERDRAFTS")
		private BigDecimal r43Overdrafts;

		@Column(name = "R43_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r43OtherInstallmentLoans;

		@Column(name = "R43_TOTAL")
		private BigDecimal r43Total;

		@Column(name = "R44_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r44AdvancesByInstitutionalSector;

		@Column(name = "R44_OVERDRAFTS")
		private BigDecimal r44Overdrafts;

		@Column(name = "R44_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r44OtherInstallmentLoans;

		@Column(name = "R44_TOTAL")
		private BigDecimal r44Total;

		@Column(name = "R45_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r45AdvancesByInstitutionalSector;

		@Column(name = "R45_OVERDRAFTS")
		private BigDecimal r45Overdrafts;

		@Column(name = "R45_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r45OtherInstallmentLoans;

		@Column(name = "R45_TOTAL")
		private BigDecimal r45Total;

		@Column(name = "R46_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r46AdvancesByInstitutionalSector;

		@Column(name = "R46_OVERDRAFTS")
		private BigDecimal r46Overdrafts;

		@Column(name = "R46_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r46OtherInstallmentLoans;

		@Column(name = "R46_TOTAL")
		private BigDecimal r46Total;

		@Column(name = "R47_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r47AdvancesByInstitutionalSector;

		@Column(name = "R47_OVERDRAFTS")
		private BigDecimal r47Overdrafts;

		@Column(name = "R47_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r47OtherInstallmentLoans;

		@Column(name = "R47_TOTAL")
		private BigDecimal r47Total;

		@Column(name = "R48_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r48AdvancesByInstitutionalSector;

		@Column(name = "R48_OVERDRAFTS")
		private BigDecimal r48Overdrafts;

		@Column(name = "R48_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r48OtherInstallmentLoans;

		@Column(name = "R48_TOTAL")
		private BigDecimal r48Total;

		@Column(name = "R49_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r49AdvancesByInstitutionalSector;

		@Column(name = "R49_OVERDRAFTS")
		private BigDecimal r49Overdrafts;

		@Column(name = "R49_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r49OtherInstallmentLoans;

		@Column(name = "R49_TOTAL")
		private BigDecimal r49Total;

		@Column(name = "R50_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r50AdvancesByInstitutionalSector;

		@Column(name = "R50_OVERDRAFTS")
		private BigDecimal r50Overdrafts;

		@Column(name = "R50_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r50OtherInstallmentLoans;

		@Column(name = "R50_TOTAL")
		private BigDecimal r50Total;

		@Column(name = "R51_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r51AdvancesByInstitutionalSector;

		@Column(name = "R51_OVERDRAFTS")
		private BigDecimal r51Overdrafts;

		@Column(name = "R51_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r51OtherInstallmentLoans;

		@Column(name = "R51_TOTAL")
		private BigDecimal r51Total;

		@Column(name = "R52_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r52AdvancesByInstitutionalSector;

		@Column(name = "R52_OVERDRAFTS")
		private BigDecimal r52Overdrafts;

		@Column(name = "R52_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r52OtherInstallmentLoans;

		@Column(name = "R52_TOTAL")
		private BigDecimal r52Total;

		@Column(name = "R53_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r53AdvancesByInstitutionalSector;

		@Column(name = "R53_OVERDRAFTS")
		private BigDecimal r53Overdrafts;

		@Column(name = "R53_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r53OtherInstallmentLoans;

		@Column(name = "R53_TOTAL")
		private BigDecimal r53Total;

		@Column(name = "R54_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r54AdvancesByInstitutionalSector;

		@Column(name = "R54_OVERDRAFTS")
		private BigDecimal r54Overdrafts;

		@Column(name = "R54_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r54OtherInstallmentLoans;

		@Column(name = "R54_TOTAL")
		private BigDecimal r54Total;

		@Column(name = "R55_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r55AdvancesByInstitutionalSector;

		@Column(name = "R55_OVERDRAFTS")
		private BigDecimal r55Overdrafts;

		@Column(name = "R55_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r55OtherInstallmentLoans;

		@Column(name = "R55_TOTAL")
		private BigDecimal r55Total;

		@Column(name = "R56_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r56AdvancesByInstitutionalSector;

		@Column(name = "R56_OVERDRAFTS")
		private BigDecimal r56Overdrafts;

		@Column(name = "R56_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r56OtherInstallmentLoans;

		@Column(name = "R56_TOTAL")
		private BigDecimal r56Total;

		@Column(name = "R57_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r57AdvancesByInstitutionalSector;

		@Column(name = "R57_OVERDRAFTS")
		private BigDecimal r57Overdrafts;

		@Column(name = "R57_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r57OtherInstallmentLoans;

		@Column(name = "R57_TOTAL")
		private BigDecimal r57Total;

		@Column(name = "R58_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r58AdvancesByInstitutionalSector;

		@Column(name = "R58_OVERDRAFTS")
		private BigDecimal r58Overdrafts;

		@Column(name = "R58_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r58OtherInstallmentLoans;

		@Column(name = "R58_TOTAL")
		private BigDecimal r58Total;

		@Column(name = "R59_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r59AdvancesByInstitutionalSector;

		@Column(name = "R59_OVERDRAFTS")
		private BigDecimal r59Overdrafts;

		@Column(name = "R59_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r59OtherInstallmentLoans;

		@Column(name = "R59_TOTAL")
		private BigDecimal r59Total;

		@Column(name = "R60_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r60AdvancesByInstitutionalSector;

		@Column(name = "R60_OVERDRAFTS")
		private BigDecimal r60Overdrafts;

		@Column(name = "R60_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r60OtherInstallmentLoans;

		@Column(name = "R60_TOTAL")
		private BigDecimal r60Total;

		@Column(name = "R61_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r61AdvancesByInstitutionalSector;

		@Column(name = "R61_OVERDRAFTS")
		private BigDecimal r61Overdrafts;

		@Column(name = "R61_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r61OtherInstallmentLoans;

		@Column(name = "R61_TOTAL")
		private BigDecimal r61Total;

		@Column(name = "R62_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r62AdvancesByInstitutionalSector;

		@Column(name = "R62_OVERDRAFTS")
		private BigDecimal r62Overdrafts;

		@Column(name = "R62_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r62OtherInstallmentLoans;

		@Column(name = "R62_TOTAL")
		private BigDecimal r62Total;

		@Column(name = "R63_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r63AdvancesByInstitutionalSector;

		@Column(name = "R63_OVERDRAFTS")
		private BigDecimal r63Overdrafts;

		@Column(name = "R63_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r63OtherInstallmentLoans;

		@Column(name = "R63_TOTAL")
		private BigDecimal r63Total;

		@Column(name = "R64_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r64AdvancesByInstitutionalSector;

		@Column(name = "R64_OVERDRAFTS")
		private BigDecimal r64Overdrafts;

		@Column(name = "R64_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r64OtherInstallmentLoans;

		@Column(name = "R64_TOTAL")
		private BigDecimal r64Total;

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

		public String getR11AdvancesByInstitutionalSector() {
			return r11AdvancesByInstitutionalSector;
		}

		public void setR11AdvancesByInstitutionalSector(String r11AdvancesByInstitutionalSector) {
			this.r11AdvancesByInstitutionalSector = r11AdvancesByInstitutionalSector;
		}

		public BigDecimal getR11Overdrafts() {
			return r11Overdrafts;
		}

		public void setR11Overdrafts(BigDecimal r11Overdrafts) {
			this.r11Overdrafts = r11Overdrafts;
		}

		public BigDecimal getR11OtherInstallmentLoans() {
			return r11OtherInstallmentLoans;
		}

		public void setR11OtherInstallmentLoans(BigDecimal r11OtherInstallmentLoans) {
			this.r11OtherInstallmentLoans = r11OtherInstallmentLoans;
		}

		public BigDecimal getR11Total() {
			return r11Total;
		}

		public void setR11Total(BigDecimal r11Total) {
			this.r11Total = r11Total;
		}

		public String getR12AdvancesByInstitutionalSector() {
			return r12AdvancesByInstitutionalSector;
		}

		public void setR12AdvancesByInstitutionalSector(String r12AdvancesByInstitutionalSector) {
			this.r12AdvancesByInstitutionalSector = r12AdvancesByInstitutionalSector;
		}

		public BigDecimal getR12Overdrafts() {
			return r12Overdrafts;
		}

		public void setR12Overdrafts(BigDecimal r12Overdrafts) {
			this.r12Overdrafts = r12Overdrafts;
		}

		public BigDecimal getR12OtherInstallmentLoans() {
			return r12OtherInstallmentLoans;
		}

		public void setR12OtherInstallmentLoans(BigDecimal r12OtherInstallmentLoans) {
			this.r12OtherInstallmentLoans = r12OtherInstallmentLoans;
		}

		public BigDecimal getR12Total() {
			return r12Total;
		}

		public void setR12Total(BigDecimal r12Total) {
			this.r12Total = r12Total;
		}

		public String getR13AdvancesByInstitutionalSector() {
			return r13AdvancesByInstitutionalSector;
		}

		public void setR13AdvancesByInstitutionalSector(String r13AdvancesByInstitutionalSector) {
			this.r13AdvancesByInstitutionalSector = r13AdvancesByInstitutionalSector;
		}

		public BigDecimal getR13Overdrafts() {
			return r13Overdrafts;
		}

		public void setR13Overdrafts(BigDecimal r13Overdrafts) {
			this.r13Overdrafts = r13Overdrafts;
		}

		public BigDecimal getR13OtherInstallmentLoans() {
			return r13OtherInstallmentLoans;
		}

		public void setR13OtherInstallmentLoans(BigDecimal r13OtherInstallmentLoans) {
			this.r13OtherInstallmentLoans = r13OtherInstallmentLoans;
		}

		public BigDecimal getR13Total() {
			return r13Total;
		}

		public void setR13Total(BigDecimal r13Total) {
			this.r13Total = r13Total;
		}

		public String getR14AdvancesByInstitutionalSector() {
			return r14AdvancesByInstitutionalSector;
		}

		public void setR14AdvancesByInstitutionalSector(String r14AdvancesByInstitutionalSector) {
			this.r14AdvancesByInstitutionalSector = r14AdvancesByInstitutionalSector;
		}

		public BigDecimal getR14Overdrafts() {
			return r14Overdrafts;
		}

		public void setR14Overdrafts(BigDecimal r14Overdrafts) {
			this.r14Overdrafts = r14Overdrafts;
		}

		public BigDecimal getR14OtherInstallmentLoans() {
			return r14OtherInstallmentLoans;
		}

		public void setR14OtherInstallmentLoans(BigDecimal r14OtherInstallmentLoans) {
			this.r14OtherInstallmentLoans = r14OtherInstallmentLoans;
		}

		public BigDecimal getR14Total() {
			return r14Total;
		}

		public void setR14Total(BigDecimal r14Total) {
			this.r14Total = r14Total;
		}

		public String getR15AdvancesByInstitutionalSector() {
			return r15AdvancesByInstitutionalSector;
		}

		public void setR15AdvancesByInstitutionalSector(String r15AdvancesByInstitutionalSector) {
			this.r15AdvancesByInstitutionalSector = r15AdvancesByInstitutionalSector;
		}

		public BigDecimal getR15Overdrafts() {
			return r15Overdrafts;
		}

		public void setR15Overdrafts(BigDecimal r15Overdrafts) {
			this.r15Overdrafts = r15Overdrafts;
		}

		public BigDecimal getR15OtherInstallmentLoans() {
			return r15OtherInstallmentLoans;
		}

		public void setR15OtherInstallmentLoans(BigDecimal r15OtherInstallmentLoans) {
			this.r15OtherInstallmentLoans = r15OtherInstallmentLoans;
		}

		public BigDecimal getR15Total() {
			return r15Total;
		}

		public void setR15Total(BigDecimal r15Total) {
			this.r15Total = r15Total;
		}

		public String getR16AdvancesByInstitutionalSector() {
			return r16AdvancesByInstitutionalSector;
		}

		public void setR16AdvancesByInstitutionalSector(String r16AdvancesByInstitutionalSector) {
			this.r16AdvancesByInstitutionalSector = r16AdvancesByInstitutionalSector;
		}

		public BigDecimal getR16Overdrafts() {
			return r16Overdrafts;
		}

		public void setR16Overdrafts(BigDecimal r16Overdrafts) {
			this.r16Overdrafts = r16Overdrafts;
		}

		public BigDecimal getR16OtherInstallmentLoans() {
			return r16OtherInstallmentLoans;
		}

		public void setR16OtherInstallmentLoans(BigDecimal r16OtherInstallmentLoans) {
			this.r16OtherInstallmentLoans = r16OtherInstallmentLoans;
		}

		public BigDecimal getR16Total() {
			return r16Total;
		}

		public void setR16Total(BigDecimal r16Total) {
			this.r16Total = r16Total;
		}

		public String getR17AdvancesByInstitutionalSector() {
			return r17AdvancesByInstitutionalSector;
		}

		public void setR17AdvancesByInstitutionalSector(String r17AdvancesByInstitutionalSector) {
			this.r17AdvancesByInstitutionalSector = r17AdvancesByInstitutionalSector;
		}

		public BigDecimal getR17Overdrafts() {
			return r17Overdrafts;
		}

		public void setR17Overdrafts(BigDecimal r17Overdrafts) {
			this.r17Overdrafts = r17Overdrafts;
		}

		public BigDecimal getR17OtherInstallmentLoans() {
			return r17OtherInstallmentLoans;
		}

		public void setR17OtherInstallmentLoans(BigDecimal r17OtherInstallmentLoans) {
			this.r17OtherInstallmentLoans = r17OtherInstallmentLoans;
		}

		public BigDecimal getR17Total() {
			return r17Total;
		}

		public void setR17Total(BigDecimal r17Total) {
			this.r17Total = r17Total;
		}

		public String getR18AdvancesByInstitutionalSector() {
			return r18AdvancesByInstitutionalSector;
		}

		public void setR18AdvancesByInstitutionalSector(String r18AdvancesByInstitutionalSector) {
			this.r18AdvancesByInstitutionalSector = r18AdvancesByInstitutionalSector;
		}

		public BigDecimal getR18Overdrafts() {
			return r18Overdrafts;
		}

		public void setR18Overdrafts(BigDecimal r18Overdrafts) {
			this.r18Overdrafts = r18Overdrafts;
		}

		public BigDecimal getR18OtherInstallmentLoans() {
			return r18OtherInstallmentLoans;
		}

		public void setR18OtherInstallmentLoans(BigDecimal r18OtherInstallmentLoans) {
			this.r18OtherInstallmentLoans = r18OtherInstallmentLoans;
		}

		public BigDecimal getR18Total() {
			return r18Total;
		}

		public void setR18Total(BigDecimal r18Total) {
			this.r18Total = r18Total;
		}

		public String getR19AdvancesByInstitutionalSector() {
			return r19AdvancesByInstitutionalSector;
		}

		public void setR19AdvancesByInstitutionalSector(String r19AdvancesByInstitutionalSector) {
			this.r19AdvancesByInstitutionalSector = r19AdvancesByInstitutionalSector;
		}

		public BigDecimal getR19Overdrafts() {
			return r19Overdrafts;
		}

		public void setR19Overdrafts(BigDecimal r19Overdrafts) {
			this.r19Overdrafts = r19Overdrafts;
		}

		public BigDecimal getR19OtherInstallmentLoans() {
			return r19OtherInstallmentLoans;
		}

		public void setR19OtherInstallmentLoans(BigDecimal r19OtherInstallmentLoans) {
			this.r19OtherInstallmentLoans = r19OtherInstallmentLoans;
		}

		public BigDecimal getR19Total() {
			return r19Total;
		}

		public void setR19Total(BigDecimal r19Total) {
			this.r19Total = r19Total;
		}

		public String getR20AdvancesByInstitutionalSector() {
			return r20AdvancesByInstitutionalSector;
		}

		public void setR20AdvancesByInstitutionalSector(String r20AdvancesByInstitutionalSector) {
			this.r20AdvancesByInstitutionalSector = r20AdvancesByInstitutionalSector;
		}

		public BigDecimal getR20Overdrafts() {
			return r20Overdrafts;
		}

		public void setR20Overdrafts(BigDecimal r20Overdrafts) {
			this.r20Overdrafts = r20Overdrafts;
		}

		public BigDecimal getR20OtherInstallmentLoans() {
			return r20OtherInstallmentLoans;
		}

		public void setR20OtherInstallmentLoans(BigDecimal r20OtherInstallmentLoans) {
			this.r20OtherInstallmentLoans = r20OtherInstallmentLoans;
		}

		public BigDecimal getR20Total() {
			return r20Total;
		}

		public void setR20Total(BigDecimal r20Total) {
			this.r20Total = r20Total;
		}

		public String getR21AdvancesByInstitutionalSector() {
			return r21AdvancesByInstitutionalSector;
		}

		public void setR21AdvancesByInstitutionalSector(String r21AdvancesByInstitutionalSector) {
			this.r21AdvancesByInstitutionalSector = r21AdvancesByInstitutionalSector;
		}

		public BigDecimal getR21Overdrafts() {
			return r21Overdrafts;
		}

		public void setR21Overdrafts(BigDecimal r21Overdrafts) {
			this.r21Overdrafts = r21Overdrafts;
		}

		public BigDecimal getR21OtherInstallmentLoans() {
			return r21OtherInstallmentLoans;
		}

		public void setR21OtherInstallmentLoans(BigDecimal r21OtherInstallmentLoans) {
			this.r21OtherInstallmentLoans = r21OtherInstallmentLoans;
		}

		public BigDecimal getR21Total() {
			return r21Total;
		}

		public void setR21Total(BigDecimal r21Total) {
			this.r21Total = r21Total;
		}

		public String getR22AdvancesByInstitutionalSector() {
			return r22AdvancesByInstitutionalSector;
		}

		public void setR22AdvancesByInstitutionalSector(String r22AdvancesByInstitutionalSector) {
			this.r22AdvancesByInstitutionalSector = r22AdvancesByInstitutionalSector;
		}

		public BigDecimal getR22Overdrafts() {
			return r22Overdrafts;
		}

		public void setR22Overdrafts(BigDecimal r22Overdrafts) {
			this.r22Overdrafts = r22Overdrafts;
		}

		public BigDecimal getR22OtherInstallmentLoans() {
			return r22OtherInstallmentLoans;
		}

		public void setR22OtherInstallmentLoans(BigDecimal r22OtherInstallmentLoans) {
			this.r22OtherInstallmentLoans = r22OtherInstallmentLoans;
		}

		public BigDecimal getR22Total() {
			return r22Total;
		}

		public void setR22Total(BigDecimal r22Total) {
			this.r22Total = r22Total;
		}

		public String getR23AdvancesByInstitutionalSector() {
			return r23AdvancesByInstitutionalSector;
		}

		public void setR23AdvancesByInstitutionalSector(String r23AdvancesByInstitutionalSector) {
			this.r23AdvancesByInstitutionalSector = r23AdvancesByInstitutionalSector;
		}

		public BigDecimal getR23Overdrafts() {
			return r23Overdrafts;
		}

		public void setR23Overdrafts(BigDecimal r23Overdrafts) {
			this.r23Overdrafts = r23Overdrafts;
		}

		public BigDecimal getR23OtherInstallmentLoans() {
			return r23OtherInstallmentLoans;
		}

		public void setR23OtherInstallmentLoans(BigDecimal r23OtherInstallmentLoans) {
			this.r23OtherInstallmentLoans = r23OtherInstallmentLoans;
		}

		public BigDecimal getR23Total() {
			return r23Total;
		}

		public void setR23Total(BigDecimal r23Total) {
			this.r23Total = r23Total;
		}

		public String getR24AdvancesByInstitutionalSector() {
			return r24AdvancesByInstitutionalSector;
		}

		public void setR24AdvancesByInstitutionalSector(String r24AdvancesByInstitutionalSector) {
			this.r24AdvancesByInstitutionalSector = r24AdvancesByInstitutionalSector;
		}

		public BigDecimal getR24Overdrafts() {
			return r24Overdrafts;
		}

		public void setR24Overdrafts(BigDecimal r24Overdrafts) {
			this.r24Overdrafts = r24Overdrafts;
		}

		public BigDecimal getR24OtherInstallmentLoans() {
			return r24OtherInstallmentLoans;
		}

		public void setR24OtherInstallmentLoans(BigDecimal r24OtherInstallmentLoans) {
			this.r24OtherInstallmentLoans = r24OtherInstallmentLoans;
		}

		public BigDecimal getR24Total() {
			return r24Total;
		}

		public void setR24Total(BigDecimal r24Total) {
			this.r24Total = r24Total;
		}

		public String getR25AdvancesByInstitutionalSector() {
			return r25AdvancesByInstitutionalSector;
		}

		public void setR25AdvancesByInstitutionalSector(String r25AdvancesByInstitutionalSector) {
			this.r25AdvancesByInstitutionalSector = r25AdvancesByInstitutionalSector;
		}

		public BigDecimal getR25Overdrafts() {
			return r25Overdrafts;
		}

		public void setR25Overdrafts(BigDecimal r25Overdrafts) {
			this.r25Overdrafts = r25Overdrafts;
		}

		public BigDecimal getR25OtherInstallmentLoans() {
			return r25OtherInstallmentLoans;
		}

		public void setR25OtherInstallmentLoans(BigDecimal r25OtherInstallmentLoans) {
			this.r25OtherInstallmentLoans = r25OtherInstallmentLoans;
		}

		public BigDecimal getR25Total() {
			return r25Total;
		}

		public void setR25Total(BigDecimal r25Total) {
			this.r25Total = r25Total;
		}

		public String getR26AdvancesByInstitutionalSector() {
			return r26AdvancesByInstitutionalSector;
		}

		public void setR26AdvancesByInstitutionalSector(String r26AdvancesByInstitutionalSector) {
			this.r26AdvancesByInstitutionalSector = r26AdvancesByInstitutionalSector;
		}

		public BigDecimal getR26Overdrafts() {
			return r26Overdrafts;
		}

		public void setR26Overdrafts(BigDecimal r26Overdrafts) {
			this.r26Overdrafts = r26Overdrafts;
		}

		public BigDecimal getR26OtherInstallmentLoans() {
			return r26OtherInstallmentLoans;
		}

		public void setR26OtherInstallmentLoans(BigDecimal r26OtherInstallmentLoans) {
			this.r26OtherInstallmentLoans = r26OtherInstallmentLoans;
		}

		public BigDecimal getR26Total() {
			return r26Total;
		}

		public void setR26Total(BigDecimal r26Total) {
			this.r26Total = r26Total;
		}

		public String getR27AdvancesByInstitutionalSector() {
			return r27AdvancesByInstitutionalSector;
		}

		public void setR27AdvancesByInstitutionalSector(String r27AdvancesByInstitutionalSector) {
			this.r27AdvancesByInstitutionalSector = r27AdvancesByInstitutionalSector;
		}

		public BigDecimal getR27Overdrafts() {
			return r27Overdrafts;
		}

		public void setR27Overdrafts(BigDecimal r27Overdrafts) {
			this.r27Overdrafts = r27Overdrafts;
		}

		public BigDecimal getR27OtherInstallmentLoans() {
			return r27OtherInstallmentLoans;
		}

		public void setR27OtherInstallmentLoans(BigDecimal r27OtherInstallmentLoans) {
			this.r27OtherInstallmentLoans = r27OtherInstallmentLoans;
		}

		public BigDecimal getR27Total() {
			return r27Total;
		}

		public void setR27Total(BigDecimal r27Total) {
			this.r27Total = r27Total;
		}

		public String getR28AdvancesByInstitutionalSector() {
			return r28AdvancesByInstitutionalSector;
		}

		public void setR28AdvancesByInstitutionalSector(String r28AdvancesByInstitutionalSector) {
			this.r28AdvancesByInstitutionalSector = r28AdvancesByInstitutionalSector;
		}

		public BigDecimal getR28Overdrafts() {
			return r28Overdrafts;
		}

		public void setR28Overdrafts(BigDecimal r28Overdrafts) {
			this.r28Overdrafts = r28Overdrafts;
		}

		public BigDecimal getR28OtherInstallmentLoans() {
			return r28OtherInstallmentLoans;
		}

		public void setR28OtherInstallmentLoans(BigDecimal r28OtherInstallmentLoans) {
			this.r28OtherInstallmentLoans = r28OtherInstallmentLoans;
		}

		public BigDecimal getR28Total() {
			return r28Total;
		}

		public void setR28Total(BigDecimal r28Total) {
			this.r28Total = r28Total;
		}

		public String getR29AdvancesByInstitutionalSector() {
			return r29AdvancesByInstitutionalSector;
		}

		public void setR29AdvancesByInstitutionalSector(String r29AdvancesByInstitutionalSector) {
			this.r29AdvancesByInstitutionalSector = r29AdvancesByInstitutionalSector;
		}

		public BigDecimal getR29Overdrafts() {
			return r29Overdrafts;
		}

		public void setR29Overdrafts(BigDecimal r29Overdrafts) {
			this.r29Overdrafts = r29Overdrafts;
		}

		public BigDecimal getR29OtherInstallmentLoans() {
			return r29OtherInstallmentLoans;
		}

		public void setR29OtherInstallmentLoans(BigDecimal r29OtherInstallmentLoans) {
			this.r29OtherInstallmentLoans = r29OtherInstallmentLoans;
		}

		public BigDecimal getR29Total() {
			return r29Total;
		}

		public void setR29Total(BigDecimal r29Total) {
			this.r29Total = r29Total;
		}

		public String getR30AdvancesByInstitutionalSector() {
			return r30AdvancesByInstitutionalSector;
		}

		public void setR30AdvancesByInstitutionalSector(String r30AdvancesByInstitutionalSector) {
			this.r30AdvancesByInstitutionalSector = r30AdvancesByInstitutionalSector;
		}

		public BigDecimal getR30Overdrafts() {
			return r30Overdrafts;
		}

		public void setR30Overdrafts(BigDecimal r30Overdrafts) {
			this.r30Overdrafts = r30Overdrafts;
		}

		public BigDecimal getR30OtherInstallmentLoans() {
			return r30OtherInstallmentLoans;
		}

		public void setR30OtherInstallmentLoans(BigDecimal r30OtherInstallmentLoans) {
			this.r30OtherInstallmentLoans = r30OtherInstallmentLoans;
		}

		public BigDecimal getR30Total() {
			return r30Total;
		}

		public void setR30Total(BigDecimal r30Total) {
			this.r30Total = r30Total;
		}

		public String getR31AdvancesByInstitutionalSector() {
			return r31AdvancesByInstitutionalSector;
		}

		public void setR31AdvancesByInstitutionalSector(String r31AdvancesByInstitutionalSector) {
			this.r31AdvancesByInstitutionalSector = r31AdvancesByInstitutionalSector;
		}

		public BigDecimal getR31Overdrafts() {
			return r31Overdrafts;
		}

		public void setR31Overdrafts(BigDecimal r31Overdrafts) {
			this.r31Overdrafts = r31Overdrafts;
		}

		public BigDecimal getR31OtherInstallmentLoans() {
			return r31OtherInstallmentLoans;
		}

		public void setR31OtherInstallmentLoans(BigDecimal r31OtherInstallmentLoans) {
			this.r31OtherInstallmentLoans = r31OtherInstallmentLoans;
		}

		public BigDecimal getR31Total() {
			return r31Total;
		}

		public void setR31Total(BigDecimal r31Total) {
			this.r31Total = r31Total;
		}

		public String getR32AdvancesByInstitutionalSector() {
			return r32AdvancesByInstitutionalSector;
		}

		public void setR32AdvancesByInstitutionalSector(String r32AdvancesByInstitutionalSector) {
			this.r32AdvancesByInstitutionalSector = r32AdvancesByInstitutionalSector;
		}

		public BigDecimal getR32Overdrafts() {
			return r32Overdrafts;
		}

		public void setR32Overdrafts(BigDecimal r32Overdrafts) {
			this.r32Overdrafts = r32Overdrafts;
		}

		public BigDecimal getR32OtherInstallmentLoans() {
			return r32OtherInstallmentLoans;
		}

		public void setR32OtherInstallmentLoans(BigDecimal r32OtherInstallmentLoans) {
			this.r32OtherInstallmentLoans = r32OtherInstallmentLoans;
		}

		public BigDecimal getR32Total() {
			return r32Total;
		}

		public void setR32Total(BigDecimal r32Total) {
			this.r32Total = r32Total;
		}

		public String getR33AdvancesByInstitutionalSector() {
			return r33AdvancesByInstitutionalSector;
		}

		public void setR33AdvancesByInstitutionalSector(String r33AdvancesByInstitutionalSector) {
			this.r33AdvancesByInstitutionalSector = r33AdvancesByInstitutionalSector;
		}

		public BigDecimal getR33Overdrafts() {
			return r33Overdrafts;
		}

		public void setR33Overdrafts(BigDecimal r33Overdrafts) {
			this.r33Overdrafts = r33Overdrafts;
		}

		public BigDecimal getR33OtherInstallmentLoans() {
			return r33OtherInstallmentLoans;
		}

		public void setR33OtherInstallmentLoans(BigDecimal r33OtherInstallmentLoans) {
			this.r33OtherInstallmentLoans = r33OtherInstallmentLoans;
		}

		public BigDecimal getR33Total() {
			return r33Total;
		}

		public void setR33Total(BigDecimal r33Total) {
			this.r33Total = r33Total;
		}

		public String getR34AdvancesByInstitutionalSector() {
			return r34AdvancesByInstitutionalSector;
		}

		public void setR34AdvancesByInstitutionalSector(String r34AdvancesByInstitutionalSector) {
			this.r34AdvancesByInstitutionalSector = r34AdvancesByInstitutionalSector;
		}

		public BigDecimal getR34Overdrafts() {
			return r34Overdrafts;
		}

		public void setR34Overdrafts(BigDecimal r34Overdrafts) {
			this.r34Overdrafts = r34Overdrafts;
		}

		public BigDecimal getR34OtherInstallmentLoans() {
			return r34OtherInstallmentLoans;
		}

		public void setR34OtherInstallmentLoans(BigDecimal r34OtherInstallmentLoans) {
			this.r34OtherInstallmentLoans = r34OtherInstallmentLoans;
		}

		public BigDecimal getR34Total() {
			return r34Total;
		}

		public void setR34Total(BigDecimal r34Total) {
			this.r34Total = r34Total;
		}

		public String getR35AdvancesByInstitutionalSector() {
			return r35AdvancesByInstitutionalSector;
		}

		public void setR35AdvancesByInstitutionalSector(String r35AdvancesByInstitutionalSector) {
			this.r35AdvancesByInstitutionalSector = r35AdvancesByInstitutionalSector;
		}

		public BigDecimal getR35Overdrafts() {
			return r35Overdrafts;
		}

		public void setR35Overdrafts(BigDecimal r35Overdrafts) {
			this.r35Overdrafts = r35Overdrafts;
		}

		public BigDecimal getR35OtherInstallmentLoans() {
			return r35OtherInstallmentLoans;
		}

		public void setR35OtherInstallmentLoans(BigDecimal r35OtherInstallmentLoans) {
			this.r35OtherInstallmentLoans = r35OtherInstallmentLoans;
		}

		public BigDecimal getR35Total() {
			return r35Total;
		}

		public void setR35Total(BigDecimal r35Total) {
			this.r35Total = r35Total;
		}

		public String getR36AdvancesByInstitutionalSector() {
			return r36AdvancesByInstitutionalSector;
		}

		public void setR36AdvancesByInstitutionalSector(String r36AdvancesByInstitutionalSector) {
			this.r36AdvancesByInstitutionalSector = r36AdvancesByInstitutionalSector;
		}

		public BigDecimal getR36Overdrafts() {
			return r36Overdrafts;
		}

		public void setR36Overdrafts(BigDecimal r36Overdrafts) {
			this.r36Overdrafts = r36Overdrafts;
		}

		public BigDecimal getR36OtherInstallmentLoans() {
			return r36OtherInstallmentLoans;
		}

		public void setR36OtherInstallmentLoans(BigDecimal r36OtherInstallmentLoans) {
			this.r36OtherInstallmentLoans = r36OtherInstallmentLoans;
		}

		public BigDecimal getR36Total() {
			return r36Total;
		}

		public void setR36Total(BigDecimal r36Total) {
			this.r36Total = r36Total;
		}

		public String getR37AdvancesByInstitutionalSector() {
			return r37AdvancesByInstitutionalSector;
		}

		public void setR37AdvancesByInstitutionalSector(String r37AdvancesByInstitutionalSector) {
			this.r37AdvancesByInstitutionalSector = r37AdvancesByInstitutionalSector;
		}

		public BigDecimal getR37Overdrafts() {
			return r37Overdrafts;
		}

		public void setR37Overdrafts(BigDecimal r37Overdrafts) {
			this.r37Overdrafts = r37Overdrafts;
		}

		public BigDecimal getR37OtherInstallmentLoans() {
			return r37OtherInstallmentLoans;
		}

		public void setR37OtherInstallmentLoans(BigDecimal r37OtherInstallmentLoans) {
			this.r37OtherInstallmentLoans = r37OtherInstallmentLoans;
		}

		public BigDecimal getR37Total() {
			return r37Total;
		}

		public void setR37Total(BigDecimal r37Total) {
			this.r37Total = r37Total;
		}

		public String getR38AdvancesByInstitutionalSector() {
			return r38AdvancesByInstitutionalSector;
		}

		public void setR38AdvancesByInstitutionalSector(String r38AdvancesByInstitutionalSector) {
			this.r38AdvancesByInstitutionalSector = r38AdvancesByInstitutionalSector;
		}

		public BigDecimal getR38Overdrafts() {
			return r38Overdrafts;
		}

		public void setR38Overdrafts(BigDecimal r38Overdrafts) {
			this.r38Overdrafts = r38Overdrafts;
		}

		public BigDecimal getR38OtherInstallmentLoans() {
			return r38OtherInstallmentLoans;
		}

		public void setR38OtherInstallmentLoans(BigDecimal r38OtherInstallmentLoans) {
			this.r38OtherInstallmentLoans = r38OtherInstallmentLoans;
		}

		public BigDecimal getR38Total() {
			return r38Total;
		}

		public void setR38Total(BigDecimal r38Total) {
			this.r38Total = r38Total;
		}

		public String getR39AdvancesByInstitutionalSector() {
			return r39AdvancesByInstitutionalSector;
		}

		public void setR39AdvancesByInstitutionalSector(String r39AdvancesByInstitutionalSector) {
			this.r39AdvancesByInstitutionalSector = r39AdvancesByInstitutionalSector;
		}

		public BigDecimal getR39Overdrafts() {
			return r39Overdrafts;
		}

		public void setR39Overdrafts(BigDecimal r39Overdrafts) {
			this.r39Overdrafts = r39Overdrafts;
		}

		public BigDecimal getR39OtherInstallmentLoans() {
			return r39OtherInstallmentLoans;
		}

		public void setR39OtherInstallmentLoans(BigDecimal r39OtherInstallmentLoans) {
			this.r39OtherInstallmentLoans = r39OtherInstallmentLoans;
		}

		public BigDecimal getR39Total() {
			return r39Total;
		}

		public void setR39Total(BigDecimal r39Total) {
			this.r39Total = r39Total;
		}

		public String getR40AdvancesByInstitutionalSector() {
			return r40AdvancesByInstitutionalSector;
		}

		public void setR40AdvancesByInstitutionalSector(String r40AdvancesByInstitutionalSector) {
			this.r40AdvancesByInstitutionalSector = r40AdvancesByInstitutionalSector;
		}

		public BigDecimal getR40Overdrafts() {
			return r40Overdrafts;
		}

		public void setR40Overdrafts(BigDecimal r40Overdrafts) {
			this.r40Overdrafts = r40Overdrafts;
		}

		public BigDecimal getR40OtherInstallmentLoans() {
			return r40OtherInstallmentLoans;
		}

		public void setR40OtherInstallmentLoans(BigDecimal r40OtherInstallmentLoans) {
			this.r40OtherInstallmentLoans = r40OtherInstallmentLoans;
		}

		public BigDecimal getR40Total() {
			return r40Total;
		}

		public void setR40Total(BigDecimal r40Total) {
			this.r40Total = r40Total;
		}

		public String getR41AdvancesByInstitutionalSector() {
			return r41AdvancesByInstitutionalSector;
		}

		public void setR41AdvancesByInstitutionalSector(String r41AdvancesByInstitutionalSector) {
			this.r41AdvancesByInstitutionalSector = r41AdvancesByInstitutionalSector;
		}

		public BigDecimal getR41Overdrafts() {
			return r41Overdrafts;
		}

		public void setR41Overdrafts(BigDecimal r41Overdrafts) {
			this.r41Overdrafts = r41Overdrafts;
		}

		public BigDecimal getR41OtherInstallmentLoans() {
			return r41OtherInstallmentLoans;
		}

		public void setR41OtherInstallmentLoans(BigDecimal r41OtherInstallmentLoans) {
			this.r41OtherInstallmentLoans = r41OtherInstallmentLoans;
		}

		public BigDecimal getR41Total() {
			return r41Total;
		}

		public void setR41Total(BigDecimal r41Total) {
			this.r41Total = r41Total;
		}

		public String getR42AdvancesByInstitutionalSector() {
			return r42AdvancesByInstitutionalSector;
		}

		public void setR42AdvancesByInstitutionalSector(String r42AdvancesByInstitutionalSector) {
			this.r42AdvancesByInstitutionalSector = r42AdvancesByInstitutionalSector;
		}

		public BigDecimal getR42Overdrafts() {
			return r42Overdrafts;
		}

		public void setR42Overdrafts(BigDecimal r42Overdrafts) {
			this.r42Overdrafts = r42Overdrafts;
		}

		public BigDecimal getR42OtherInstallmentLoans() {
			return r42OtherInstallmentLoans;
		}

		public void setR42OtherInstallmentLoans(BigDecimal r42OtherInstallmentLoans) {
			this.r42OtherInstallmentLoans = r42OtherInstallmentLoans;
		}

		public BigDecimal getR42Total() {
			return r42Total;
		}

		public void setR42Total(BigDecimal r42Total) {
			this.r42Total = r42Total;
		}

		public String getR43AdvancesByInstitutionalSector() {
			return r43AdvancesByInstitutionalSector;
		}

		public void setR43AdvancesByInstitutionalSector(String r43AdvancesByInstitutionalSector) {
			this.r43AdvancesByInstitutionalSector = r43AdvancesByInstitutionalSector;
		}

		public BigDecimal getR43Overdrafts() {
			return r43Overdrafts;
		}

		public void setR43Overdrafts(BigDecimal r43Overdrafts) {
			this.r43Overdrafts = r43Overdrafts;
		}

		public BigDecimal getR43OtherInstallmentLoans() {
			return r43OtherInstallmentLoans;
		}

		public void setR43OtherInstallmentLoans(BigDecimal r43OtherInstallmentLoans) {
			this.r43OtherInstallmentLoans = r43OtherInstallmentLoans;
		}

		public BigDecimal getR43Total() {
			return r43Total;
		}

		public void setR43Total(BigDecimal r43Total) {
			this.r43Total = r43Total;
		}

		public String getR44AdvancesByInstitutionalSector() {
			return r44AdvancesByInstitutionalSector;
		}

		public void setR44AdvancesByInstitutionalSector(String r44AdvancesByInstitutionalSector) {
			this.r44AdvancesByInstitutionalSector = r44AdvancesByInstitutionalSector;
		}

		public BigDecimal getR44Overdrafts() {
			return r44Overdrafts;
		}

		public void setR44Overdrafts(BigDecimal r44Overdrafts) {
			this.r44Overdrafts = r44Overdrafts;
		}

		public BigDecimal getR44OtherInstallmentLoans() {
			return r44OtherInstallmentLoans;
		}

		public void setR44OtherInstallmentLoans(BigDecimal r44OtherInstallmentLoans) {
			this.r44OtherInstallmentLoans = r44OtherInstallmentLoans;
		}

		public BigDecimal getR44Total() {
			return r44Total;
		}

		public void setR44Total(BigDecimal r44Total) {
			this.r44Total = r44Total;
		}

		public String getR45AdvancesByInstitutionalSector() {
			return r45AdvancesByInstitutionalSector;
		}

		public void setR45AdvancesByInstitutionalSector(String r45AdvancesByInstitutionalSector) {
			this.r45AdvancesByInstitutionalSector = r45AdvancesByInstitutionalSector;
		}

		public BigDecimal getR45Overdrafts() {
			return r45Overdrafts;
		}

		public void setR45Overdrafts(BigDecimal r45Overdrafts) {
			this.r45Overdrafts = r45Overdrafts;
		}

		public BigDecimal getR45OtherInstallmentLoans() {
			return r45OtherInstallmentLoans;
		}

		public void setR45OtherInstallmentLoans(BigDecimal r45OtherInstallmentLoans) {
			this.r45OtherInstallmentLoans = r45OtherInstallmentLoans;
		}

		public BigDecimal getR45Total() {
			return r45Total;
		}

		public void setR45Total(BigDecimal r45Total) {
			this.r45Total = r45Total;
		}

		public String getR46AdvancesByInstitutionalSector() {
			return r46AdvancesByInstitutionalSector;
		}

		public void setR46AdvancesByInstitutionalSector(String r46AdvancesByInstitutionalSector) {
			this.r46AdvancesByInstitutionalSector = r46AdvancesByInstitutionalSector;
		}

		public BigDecimal getR46Overdrafts() {
			return r46Overdrafts;
		}

		public void setR46Overdrafts(BigDecimal r46Overdrafts) {
			this.r46Overdrafts = r46Overdrafts;
		}

		public BigDecimal getR46OtherInstallmentLoans() {
			return r46OtherInstallmentLoans;
		}

		public void setR46OtherInstallmentLoans(BigDecimal r46OtherInstallmentLoans) {
			this.r46OtherInstallmentLoans = r46OtherInstallmentLoans;
		}

		public BigDecimal getR46Total() {
			return r46Total;
		}

		public void setR46Total(BigDecimal r46Total) {
			this.r46Total = r46Total;
		}

		public String getR47AdvancesByInstitutionalSector() {
			return r47AdvancesByInstitutionalSector;
		}

		public void setR47AdvancesByInstitutionalSector(String r47AdvancesByInstitutionalSector) {
			this.r47AdvancesByInstitutionalSector = r47AdvancesByInstitutionalSector;
		}

		public BigDecimal getR47Overdrafts() {
			return r47Overdrafts;
		}

		public void setR47Overdrafts(BigDecimal r47Overdrafts) {
			this.r47Overdrafts = r47Overdrafts;
		}

		public BigDecimal getR47OtherInstallmentLoans() {
			return r47OtherInstallmentLoans;
		}

		public void setR47OtherInstallmentLoans(BigDecimal r47OtherInstallmentLoans) {
			this.r47OtherInstallmentLoans = r47OtherInstallmentLoans;
		}

		public BigDecimal getR47Total() {
			return r47Total;
		}

		public void setR47Total(BigDecimal r47Total) {
			this.r47Total = r47Total;
		}

		public String getR48AdvancesByInstitutionalSector() {
			return r48AdvancesByInstitutionalSector;
		}

		public void setR48AdvancesByInstitutionalSector(String r48AdvancesByInstitutionalSector) {
			this.r48AdvancesByInstitutionalSector = r48AdvancesByInstitutionalSector;
		}

		public BigDecimal getR48Overdrafts() {
			return r48Overdrafts;
		}

		public void setR48Overdrafts(BigDecimal r48Overdrafts) {
			this.r48Overdrafts = r48Overdrafts;
		}

		public BigDecimal getR48OtherInstallmentLoans() {
			return r48OtherInstallmentLoans;
		}

		public void setR48OtherInstallmentLoans(BigDecimal r48OtherInstallmentLoans) {
			this.r48OtherInstallmentLoans = r48OtherInstallmentLoans;
		}

		public BigDecimal getR48Total() {
			return r48Total;
		}

		public void setR48Total(BigDecimal r48Total) {
			this.r48Total = r48Total;
		}

		public String getR49AdvancesByInstitutionalSector() {
			return r49AdvancesByInstitutionalSector;
		}

		public void setR49AdvancesByInstitutionalSector(String r49AdvancesByInstitutionalSector) {
			this.r49AdvancesByInstitutionalSector = r49AdvancesByInstitutionalSector;
		}

		public BigDecimal getR49Overdrafts() {
			return r49Overdrafts;
		}

		public void setR49Overdrafts(BigDecimal r49Overdrafts) {
			this.r49Overdrafts = r49Overdrafts;
		}

		public BigDecimal getR49OtherInstallmentLoans() {
			return r49OtherInstallmentLoans;
		}

		public void setR49OtherInstallmentLoans(BigDecimal r49OtherInstallmentLoans) {
			this.r49OtherInstallmentLoans = r49OtherInstallmentLoans;
		}

		public BigDecimal getR49Total() {
			return r49Total;
		}

		public void setR49Total(BigDecimal r49Total) {
			this.r49Total = r49Total;
		}

		public String getR50AdvancesByInstitutionalSector() {
			return r50AdvancesByInstitutionalSector;
		}

		public void setR50AdvancesByInstitutionalSector(String r50AdvancesByInstitutionalSector) {
			this.r50AdvancesByInstitutionalSector = r50AdvancesByInstitutionalSector;
		}

		public BigDecimal getR50Overdrafts() {
			return r50Overdrafts;
		}

		public void setR50Overdrafts(BigDecimal r50Overdrafts) {
			this.r50Overdrafts = r50Overdrafts;
		}

		public BigDecimal getR50OtherInstallmentLoans() {
			return r50OtherInstallmentLoans;
		}

		public void setR50OtherInstallmentLoans(BigDecimal r50OtherInstallmentLoans) {
			this.r50OtherInstallmentLoans = r50OtherInstallmentLoans;
		}

		public BigDecimal getR50Total() {
			return r50Total;
		}

		public void setR50Total(BigDecimal r50Total) {
			this.r50Total = r50Total;
		}

		public String getR51AdvancesByInstitutionalSector() {
			return r51AdvancesByInstitutionalSector;
		}

		public void setR51AdvancesByInstitutionalSector(String r51AdvancesByInstitutionalSector) {
			this.r51AdvancesByInstitutionalSector = r51AdvancesByInstitutionalSector;
		}

		public BigDecimal getR51Overdrafts() {
			return r51Overdrafts;
		}

		public void setR51Overdrafts(BigDecimal r51Overdrafts) {
			this.r51Overdrafts = r51Overdrafts;
		}

		public BigDecimal getR51OtherInstallmentLoans() {
			return r51OtherInstallmentLoans;
		}

		public void setR51OtherInstallmentLoans(BigDecimal r51OtherInstallmentLoans) {
			this.r51OtherInstallmentLoans = r51OtherInstallmentLoans;
		}

		public BigDecimal getR51Total() {
			return r51Total;
		}

		public void setR51Total(BigDecimal r51Total) {
			this.r51Total = r51Total;
		}

		public String getR52AdvancesByInstitutionalSector() {
			return r52AdvancesByInstitutionalSector;
		}

		public void setR52AdvancesByInstitutionalSector(String r52AdvancesByInstitutionalSector) {
			this.r52AdvancesByInstitutionalSector = r52AdvancesByInstitutionalSector;
		}

		public BigDecimal getR52Overdrafts() {
			return r52Overdrafts;
		}

		public void setR52Overdrafts(BigDecimal r52Overdrafts) {
			this.r52Overdrafts = r52Overdrafts;
		}

		public BigDecimal getR52OtherInstallmentLoans() {
			return r52OtherInstallmentLoans;
		}

		public void setR52OtherInstallmentLoans(BigDecimal r52OtherInstallmentLoans) {
			this.r52OtherInstallmentLoans = r52OtherInstallmentLoans;
		}

		public BigDecimal getR52Total() {
			return r52Total;
		}

		public void setR52Total(BigDecimal r52Total) {
			this.r52Total = r52Total;
		}

		public String getR53AdvancesByInstitutionalSector() {
			return r53AdvancesByInstitutionalSector;
		}

		public void setR53AdvancesByInstitutionalSector(String r53AdvancesByInstitutionalSector) {
			this.r53AdvancesByInstitutionalSector = r53AdvancesByInstitutionalSector;
		}

		public BigDecimal getR53Overdrafts() {
			return r53Overdrafts;
		}

		public void setR53Overdrafts(BigDecimal r53Overdrafts) {
			this.r53Overdrafts = r53Overdrafts;
		}

		public BigDecimal getR53OtherInstallmentLoans() {
			return r53OtherInstallmentLoans;
		}

		public void setR53OtherInstallmentLoans(BigDecimal r53OtherInstallmentLoans) {
			this.r53OtherInstallmentLoans = r53OtherInstallmentLoans;
		}

		public BigDecimal getR53Total() {
			return r53Total;
		}

		public void setR53Total(BigDecimal r53Total) {
			this.r53Total = r53Total;
		}

		public String getR54AdvancesByInstitutionalSector() {
			return r54AdvancesByInstitutionalSector;
		}

		public void setR54AdvancesByInstitutionalSector(String r54AdvancesByInstitutionalSector) {
			this.r54AdvancesByInstitutionalSector = r54AdvancesByInstitutionalSector;
		}

		public BigDecimal getR54Overdrafts() {
			return r54Overdrafts;
		}

		public void setR54Overdrafts(BigDecimal r54Overdrafts) {
			this.r54Overdrafts = r54Overdrafts;
		}

		public BigDecimal getR54OtherInstallmentLoans() {
			return r54OtherInstallmentLoans;
		}

		public void setR54OtherInstallmentLoans(BigDecimal r54OtherInstallmentLoans) {
			this.r54OtherInstallmentLoans = r54OtherInstallmentLoans;
		}

		public BigDecimal getR54Total() {
			return r54Total;
		}

		public void setR54Total(BigDecimal r54Total) {
			this.r54Total = r54Total;
		}

		public String getR55AdvancesByInstitutionalSector() {
			return r55AdvancesByInstitutionalSector;
		}

		public void setR55AdvancesByInstitutionalSector(String r55AdvancesByInstitutionalSector) {
			this.r55AdvancesByInstitutionalSector = r55AdvancesByInstitutionalSector;
		}

		public BigDecimal getR55Overdrafts() {
			return r55Overdrafts;
		}

		public void setR55Overdrafts(BigDecimal r55Overdrafts) {
			this.r55Overdrafts = r55Overdrafts;
		}

		public BigDecimal getR55OtherInstallmentLoans() {
			return r55OtherInstallmentLoans;
		}

		public void setR55OtherInstallmentLoans(BigDecimal r55OtherInstallmentLoans) {
			this.r55OtherInstallmentLoans = r55OtherInstallmentLoans;
		}

		public BigDecimal getR55Total() {
			return r55Total;
		}

		public void setR55Total(BigDecimal r55Total) {
			this.r55Total = r55Total;
		}

		public String getR56AdvancesByInstitutionalSector() {
			return r56AdvancesByInstitutionalSector;
		}

		public void setR56AdvancesByInstitutionalSector(String r56AdvancesByInstitutionalSector) {
			this.r56AdvancesByInstitutionalSector = r56AdvancesByInstitutionalSector;
		}

		public BigDecimal getR56Overdrafts() {
			return r56Overdrafts;
		}

		public void setR56Overdrafts(BigDecimal r56Overdrafts) {
			this.r56Overdrafts = r56Overdrafts;
		}

		public BigDecimal getR56OtherInstallmentLoans() {
			return r56OtherInstallmentLoans;
		}

		public void setR56OtherInstallmentLoans(BigDecimal r56OtherInstallmentLoans) {
			this.r56OtherInstallmentLoans = r56OtherInstallmentLoans;
		}

		public BigDecimal getR56Total() {
			return r56Total;
		}

		public void setR56Total(BigDecimal r56Total) {
			this.r56Total = r56Total;
		}

		public String getR57AdvancesByInstitutionalSector() {
			return r57AdvancesByInstitutionalSector;
		}

		public void setR57AdvancesByInstitutionalSector(String r57AdvancesByInstitutionalSector) {
			this.r57AdvancesByInstitutionalSector = r57AdvancesByInstitutionalSector;
		}

		public BigDecimal getR57Overdrafts() {
			return r57Overdrafts;
		}

		public void setR57Overdrafts(BigDecimal r57Overdrafts) {
			this.r57Overdrafts = r57Overdrafts;
		}

		public BigDecimal getR57OtherInstallmentLoans() {
			return r57OtherInstallmentLoans;
		}

		public void setR57OtherInstallmentLoans(BigDecimal r57OtherInstallmentLoans) {
			this.r57OtherInstallmentLoans = r57OtherInstallmentLoans;
		}

		public BigDecimal getR57Total() {
			return r57Total;
		}

		public void setR57Total(BigDecimal r57Total) {
			this.r57Total = r57Total;
		}

		public String getR58AdvancesByInstitutionalSector() {
			return r58AdvancesByInstitutionalSector;
		}

		public void setR58AdvancesByInstitutionalSector(String r58AdvancesByInstitutionalSector) {
			this.r58AdvancesByInstitutionalSector = r58AdvancesByInstitutionalSector;
		}

		public BigDecimal getR58Overdrafts() {
			return r58Overdrafts;
		}

		public void setR58Overdrafts(BigDecimal r58Overdrafts) {
			this.r58Overdrafts = r58Overdrafts;
		}

		public BigDecimal getR58OtherInstallmentLoans() {
			return r58OtherInstallmentLoans;
		}

		public void setR58OtherInstallmentLoans(BigDecimal r58OtherInstallmentLoans) {
			this.r58OtherInstallmentLoans = r58OtherInstallmentLoans;
		}

		public BigDecimal getR58Total() {
			return r58Total;
		}

		public void setR58Total(BigDecimal r58Total) {
			this.r58Total = r58Total;
		}

		public String getR59AdvancesByInstitutionalSector() {
			return r59AdvancesByInstitutionalSector;
		}

		public void setR59AdvancesByInstitutionalSector(String r59AdvancesByInstitutionalSector) {
			this.r59AdvancesByInstitutionalSector = r59AdvancesByInstitutionalSector;
		}

		public BigDecimal getR59Overdrafts() {
			return r59Overdrafts;
		}

		public void setR59Overdrafts(BigDecimal r59Overdrafts) {
			this.r59Overdrafts = r59Overdrafts;
		}

		public BigDecimal getR59OtherInstallmentLoans() {
			return r59OtherInstallmentLoans;
		}

		public void setR59OtherInstallmentLoans(BigDecimal r59OtherInstallmentLoans) {
			this.r59OtherInstallmentLoans = r59OtherInstallmentLoans;
		}

		public BigDecimal getR59Total() {
			return r59Total;
		}

		public void setR59Total(BigDecimal r59Total) {
			this.r59Total = r59Total;
		}

		public String getR60AdvancesByInstitutionalSector() {
			return r60AdvancesByInstitutionalSector;
		}

		public void setR60AdvancesByInstitutionalSector(String r60AdvancesByInstitutionalSector) {
			this.r60AdvancesByInstitutionalSector = r60AdvancesByInstitutionalSector;
		}

		public BigDecimal getR60Overdrafts() {
			return r60Overdrafts;
		}

		public void setR60Overdrafts(BigDecimal r60Overdrafts) {
			this.r60Overdrafts = r60Overdrafts;
		}

		public BigDecimal getR60OtherInstallmentLoans() {
			return r60OtherInstallmentLoans;
		}

		public void setR60OtherInstallmentLoans(BigDecimal r60OtherInstallmentLoans) {
			this.r60OtherInstallmentLoans = r60OtherInstallmentLoans;
		}

		public BigDecimal getR60Total() {
			return r60Total;
		}

		public void setR60Total(BigDecimal r60Total) {
			this.r60Total = r60Total;
		}

		public String getR61AdvancesByInstitutionalSector() {
			return r61AdvancesByInstitutionalSector;
		}

		public void setR61AdvancesByInstitutionalSector(String r61AdvancesByInstitutionalSector) {
			this.r61AdvancesByInstitutionalSector = r61AdvancesByInstitutionalSector;
		}

		public BigDecimal getR61Overdrafts() {
			return r61Overdrafts;
		}

		public void setR61Overdrafts(BigDecimal r61Overdrafts) {
			this.r61Overdrafts = r61Overdrafts;
		}

		public BigDecimal getR61OtherInstallmentLoans() {
			return r61OtherInstallmentLoans;
		}

		public void setR61OtherInstallmentLoans(BigDecimal r61OtherInstallmentLoans) {
			this.r61OtherInstallmentLoans = r61OtherInstallmentLoans;
		}

		public BigDecimal getR61Total() {
			return r61Total;
		}

		public void setR61Total(BigDecimal r61Total) {
			this.r61Total = r61Total;
		}

		public String getR62AdvancesByInstitutionalSector() {
			return r62AdvancesByInstitutionalSector;
		}

		public void setR62AdvancesByInstitutionalSector(String r62AdvancesByInstitutionalSector) {
			this.r62AdvancesByInstitutionalSector = r62AdvancesByInstitutionalSector;
		}

		public BigDecimal getR62Overdrafts() {
			return r62Overdrafts;
		}

		public void setR62Overdrafts(BigDecimal r62Overdrafts) {
			this.r62Overdrafts = r62Overdrafts;
		}

		public BigDecimal getR62OtherInstallmentLoans() {
			return r62OtherInstallmentLoans;
		}

		public void setR62OtherInstallmentLoans(BigDecimal r62OtherInstallmentLoans) {
			this.r62OtherInstallmentLoans = r62OtherInstallmentLoans;
		}

		public BigDecimal getR62Total() {
			return r62Total;
		}

		public void setR62Total(BigDecimal r62Total) {
			this.r62Total = r62Total;
		}

		public String getR63AdvancesByInstitutionalSector() {
			return r63AdvancesByInstitutionalSector;
		}

		public void setR63AdvancesByInstitutionalSector(String r63AdvancesByInstitutionalSector) {
			this.r63AdvancesByInstitutionalSector = r63AdvancesByInstitutionalSector;
		}

		public BigDecimal getR63Overdrafts() {
			return r63Overdrafts;
		}

		public void setR63Overdrafts(BigDecimal r63Overdrafts) {
			this.r63Overdrafts = r63Overdrafts;
		}

		public BigDecimal getR63OtherInstallmentLoans() {
			return r63OtherInstallmentLoans;
		}

		public void setR63OtherInstallmentLoans(BigDecimal r63OtherInstallmentLoans) {
			this.r63OtherInstallmentLoans = r63OtherInstallmentLoans;
		}

		public BigDecimal getR63Total() {
			return r63Total;
		}

		public void setR63Total(BigDecimal r63Total) {
			this.r63Total = r63Total;
		}

		public String getR64AdvancesByInstitutionalSector() {
			return r64AdvancesByInstitutionalSector;
		}

		public void setR64AdvancesByInstitutionalSector(String r64AdvancesByInstitutionalSector) {
			this.r64AdvancesByInstitutionalSector = r64AdvancesByInstitutionalSector;
		}

		public BigDecimal getR64Overdrafts() {
			return r64Overdrafts;
		}

		public void setR64Overdrafts(BigDecimal r64Overdrafts) {
			this.r64Overdrafts = r64Overdrafts;
		}

		public BigDecimal getR64OtherInstallmentLoans() {
			return r64OtherInstallmentLoans;
		}

		public void setR64OtherInstallmentLoans(BigDecimal r64OtherInstallmentLoans) {
			this.r64OtherInstallmentLoans = r64OtherInstallmentLoans;
		}

		public BigDecimal getR64Total() {
			return r64Total;
		}

		public void setR64Total(BigDecimal r64Total) {
			this.r64Total = r64Total;
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

	class M_LA4RowMapper2 implements RowMapper<M_LA4_Summary_Entity2> {

		@Override
		public M_LA4_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA4_Summary_Entity2 obj = new M_LA4_Summary_Entity2();

// R11
			obj.setR11FactoringDebtors(rs.getBigDecimal("R11_FACTORING_DEBTORS"));
			obj.setR11Leasing(rs.getBigDecimal("R11_LEASING"));

			// R12
			obj.setR12FactoringDebtors(rs.getBigDecimal("R12_FACTORING_DEBTORS"));
			obj.setR12Leasing(rs.getBigDecimal("R12_LEASING"));

			// R13
			obj.setR13FactoringDebtors(rs.getBigDecimal("R13_FACTORING_DEBTORS"));
			obj.setR13Leasing(rs.getBigDecimal("R13_LEASING"));

			// R14
			obj.setR14FactoringDebtors(rs.getBigDecimal("R14_FACTORING_DEBTORS"));
			obj.setR14Leasing(rs.getBigDecimal("R14_LEASING"));

			// R15
			obj.setR15FactoringDebtors(rs.getBigDecimal("R15_FACTORING_DEBTORS"));
			obj.setR15Leasing(rs.getBigDecimal("R15_LEASING"));

			// R16
			obj.setR16FactoringDebtors(rs.getBigDecimal("R16_FACTORING_DEBTORS"));
			obj.setR16Leasing(rs.getBigDecimal("R16_LEASING"));

			// R17
			obj.setR17FactoringDebtors(rs.getBigDecimal("R17_FACTORING_DEBTORS"));
			obj.setR17Leasing(rs.getBigDecimal("R17_LEASING"));

			// R18
			obj.setR18FactoringDebtors(rs.getBigDecimal("R18_FACTORING_DEBTORS"));
			obj.setR18Leasing(rs.getBigDecimal("R18_LEASING"));

			// R19
			obj.setR19FactoringDebtors(rs.getBigDecimal("R19_FACTORING_DEBTORS"));
			obj.setR19Leasing(rs.getBigDecimal("R19_LEASING"));

			// R20
			obj.setR20FactoringDebtors(rs.getBigDecimal("R20_FACTORING_DEBTORS"));
			obj.setR20Leasing(rs.getBigDecimal("R20_LEASING"));

			// R21
			obj.setR21FactoringDebtors(rs.getBigDecimal("R21_FACTORING_DEBTORS"));
			obj.setR21Leasing(rs.getBigDecimal("R21_LEASING"));

			// R22
			obj.setR22FactoringDebtors(rs.getBigDecimal("R22_FACTORING_DEBTORS"));
			obj.setR22Leasing(rs.getBigDecimal("R22_LEASING"));

			// R23
			obj.setR23FactoringDebtors(rs.getBigDecimal("R23_FACTORING_DEBTORS"));
			obj.setR23Leasing(rs.getBigDecimal("R23_LEASING"));

			// R24
			obj.setR24FactoringDebtors(rs.getBigDecimal("R24_FACTORING_DEBTORS"));
			obj.setR24Leasing(rs.getBigDecimal("R24_LEASING"));

			// R25
			obj.setR25FactoringDebtors(rs.getBigDecimal("R25_FACTORING_DEBTORS"));
			obj.setR25Leasing(rs.getBigDecimal("R25_LEASING"));

			// R26
			obj.setR26FactoringDebtors(rs.getBigDecimal("R26_FACTORING_DEBTORS"));
			obj.setR26Leasing(rs.getBigDecimal("R26_LEASING"));

			// R27
			obj.setR27FactoringDebtors(rs.getBigDecimal("R27_FACTORING_DEBTORS"));
			obj.setR27Leasing(rs.getBigDecimal("R27_LEASING"));

			// R28
			obj.setR28FactoringDebtors(rs.getBigDecimal("R28_FACTORING_DEBTORS"));
			obj.setR28Leasing(rs.getBigDecimal("R28_LEASING"));

			// R29
			obj.setR29FactoringDebtors(rs.getBigDecimal("R29_FACTORING_DEBTORS"));
			obj.setR29Leasing(rs.getBigDecimal("R29_LEASING"));

			// R30
			obj.setR30FactoringDebtors(rs.getBigDecimal("R30_FACTORING_DEBTORS"));
			obj.setR30Leasing(rs.getBigDecimal("R30_LEASING"));

			// R31
			obj.setR31FactoringDebtors(rs.getBigDecimal("R31_FACTORING_DEBTORS"));
			obj.setR31Leasing(rs.getBigDecimal("R31_LEASING"));

			// R32
			obj.setR32FactoringDebtors(rs.getBigDecimal("R32_FACTORING_DEBTORS"));
			obj.setR32Leasing(rs.getBigDecimal("R32_LEASING"));

			// R33
			obj.setR33FactoringDebtors(rs.getBigDecimal("R33_FACTORING_DEBTORS"));
			obj.setR33Leasing(rs.getBigDecimal("R33_LEASING"));

			// R34
			obj.setR34FactoringDebtors(rs.getBigDecimal("R34_FACTORING_DEBTORS"));
			obj.setR34Leasing(rs.getBigDecimal("R34_LEASING"));

			// R35
			obj.setR35FactoringDebtors(rs.getBigDecimal("R35_FACTORING_DEBTORS"));
			obj.setR35Leasing(rs.getBigDecimal("R35_LEASING"));

			// R36
			obj.setR36FactoringDebtors(rs.getBigDecimal("R36_FACTORING_DEBTORS"));
			obj.setR36Leasing(rs.getBigDecimal("R36_LEASING"));

			// R37
			obj.setR37FactoringDebtors(rs.getBigDecimal("R37_FACTORING_DEBTORS"));
			obj.setR37Leasing(rs.getBigDecimal("R37_LEASING"));

			// R38
			obj.setR38FactoringDebtors(rs.getBigDecimal("R38_FACTORING_DEBTORS"));
			obj.setR38Leasing(rs.getBigDecimal("R38_LEASING"));

			// R39
			obj.setR39FactoringDebtors(rs.getBigDecimal("R39_FACTORING_DEBTORS"));
			obj.setR39Leasing(rs.getBigDecimal("R39_LEASING"));

			// R40
			obj.setR40FactoringDebtors(rs.getBigDecimal("R40_FACTORING_DEBTORS"));
			obj.setR40Leasing(rs.getBigDecimal("R40_LEASING"));

			// R41
			obj.setR41FactoringDebtors(rs.getBigDecimal("R41_FACTORING_DEBTORS"));
			obj.setR41Leasing(rs.getBigDecimal("R41_LEASING"));

			// R42
			obj.setR42FactoringDebtors(rs.getBigDecimal("R42_FACTORING_DEBTORS"));
			obj.setR42Leasing(rs.getBigDecimal("R42_LEASING"));

			// R43
			obj.setR43FactoringDebtors(rs.getBigDecimal("R43_FACTORING_DEBTORS"));
			obj.setR43Leasing(rs.getBigDecimal("R43_LEASING"));

			// R44
			obj.setR44FactoringDebtors(rs.getBigDecimal("R44_FACTORING_DEBTORS"));
			obj.setR44Leasing(rs.getBigDecimal("R44_LEASING"));

			// R45
			obj.setR45FactoringDebtors(rs.getBigDecimal("R45_FACTORING_DEBTORS"));
			obj.setR45Leasing(rs.getBigDecimal("R45_LEASING"));

			// R46
			obj.setR46FactoringDebtors(rs.getBigDecimal("R46_FACTORING_DEBTORS"));
			obj.setR46Leasing(rs.getBigDecimal("R46_LEASING"));

			// R47
			obj.setR47FactoringDebtors(rs.getBigDecimal("R47_FACTORING_DEBTORS"));
			obj.setR47Leasing(rs.getBigDecimal("R47_LEASING"));

			// R48
			obj.setR48FactoringDebtors(rs.getBigDecimal("R48_FACTORING_DEBTORS"));
			obj.setR48Leasing(rs.getBigDecimal("R48_LEASING"));

			// R49
			obj.setR49FactoringDebtors(rs.getBigDecimal("R49_FACTORING_DEBTORS"));
			obj.setR49Leasing(rs.getBigDecimal("R49_LEASING"));

			// R50
			obj.setR50FactoringDebtors(rs.getBigDecimal("R50_FACTORING_DEBTORS"));
			obj.setR50Leasing(rs.getBigDecimal("R50_LEASING"));

			// R51
			obj.setR51FactoringDebtors(rs.getBigDecimal("R51_FACTORING_DEBTORS"));
			obj.setR51Leasing(rs.getBigDecimal("R51_LEASING"));

			// R52
			obj.setR52FactoringDebtors(rs.getBigDecimal("R52_FACTORING_DEBTORS"));
			obj.setR52Leasing(rs.getBigDecimal("R52_LEASING"));

			// R53
			obj.setR53FactoringDebtors(rs.getBigDecimal("R53_FACTORING_DEBTORS"));
			obj.setR53Leasing(rs.getBigDecimal("R53_LEASING"));

			// R54
			obj.setR54FactoringDebtors(rs.getBigDecimal("R54_FACTORING_DEBTORS"));
			obj.setR54Leasing(rs.getBigDecimal("R54_LEASING"));

			// R55
			obj.setR55FactoringDebtors(rs.getBigDecimal("R55_FACTORING_DEBTORS"));
			obj.setR55Leasing(rs.getBigDecimal("R55_LEASING"));

			// R56
			obj.setR56FactoringDebtors(rs.getBigDecimal("R56_FACTORING_DEBTORS"));
			obj.setR56Leasing(rs.getBigDecimal("R56_LEASING"));

			// R57
			obj.setR57FactoringDebtors(rs.getBigDecimal("R57_FACTORING_DEBTORS"));
			obj.setR57Leasing(rs.getBigDecimal("R57_LEASING"));

			// R58
			obj.setR58FactoringDebtors(rs.getBigDecimal("R58_FACTORING_DEBTORS"));
			obj.setR58Leasing(rs.getBigDecimal("R58_LEASING"));

			// R59
			obj.setR59FactoringDebtors(rs.getBigDecimal("R59_FACTORING_DEBTORS"));
			obj.setR59Leasing(rs.getBigDecimal("R59_LEASING"));

			// R60
			obj.setR60FactoringDebtors(rs.getBigDecimal("R60_FACTORING_DEBTORS"));
			obj.setR60Leasing(rs.getBigDecimal("R60_LEASING"));

			// R61
			obj.setR61FactoringDebtors(rs.getBigDecimal("R61_FACTORING_DEBTORS"));
			obj.setR61Leasing(rs.getBigDecimal("R61_LEASING"));

			// R62
			obj.setR62FactoringDebtors(rs.getBigDecimal("R62_FACTORING_DEBTORS"));
			obj.setR62Leasing(rs.getBigDecimal("R62_LEASING"));

			// R63
			obj.setR63FactoringDebtors(rs.getBigDecimal("R63_FACTORING_DEBTORS"));
			obj.setR63Leasing(rs.getBigDecimal("R63_LEASING"));

			// R64
			obj.setR64FactoringDebtors(rs.getBigDecimal("R64_FACTORING_DEBTORS"));
			obj.setR64Leasing(rs.getBigDecimal("R64_LEASING"));

			// COMMON FIELDS
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

	public static class M_LA4_Summary_Entity2 {
		@Column(name = "R11_FACTORING_DEBTORS")
		private BigDecimal r11FactoringDebtors;

		@Column(name = "R11_LEASING")
		private BigDecimal r11Leasing;

		@Column(name = "R12_FACTORING_DEBTORS")
		private BigDecimal r12FactoringDebtors;

		@Column(name = "R12_LEASING")
		private BigDecimal r12Leasing;

		@Column(name = "R13_FACTORING_DEBTORS")
		private BigDecimal r13FactoringDebtors;

		@Column(name = "R13_LEASING")
		private BigDecimal r13Leasing;

		@Column(name = "R14_FACTORING_DEBTORS")
		private BigDecimal r14FactoringDebtors;

		@Column(name = "R14_LEASING")
		private BigDecimal r14Leasing;

		@Column(name = "R15_FACTORING_DEBTORS")
		private BigDecimal r15FactoringDebtors;

		@Column(name = "R15_LEASING")
		private BigDecimal r15Leasing;

		@Column(name = "R16_FACTORING_DEBTORS")
		private BigDecimal r16FactoringDebtors;

		@Column(name = "R16_LEASING")
		private BigDecimal r16Leasing;

		@Column(name = "R17_FACTORING_DEBTORS")
		private BigDecimal r17FactoringDebtors;

		@Column(name = "R17_LEASING")
		private BigDecimal r17Leasing;

		@Column(name = "R18_FACTORING_DEBTORS")
		private BigDecimal r18FactoringDebtors;

		@Column(name = "R18_LEASING")
		private BigDecimal r18Leasing;

		@Column(name = "R19_FACTORING_DEBTORS")
		private BigDecimal r19FactoringDebtors;

		@Column(name = "R19_LEASING")
		private BigDecimal r19Leasing;

		@Column(name = "R20_FACTORING_DEBTORS")
		private BigDecimal r20FactoringDebtors;

		@Column(name = "R20_LEASING")
		private BigDecimal r20Leasing;

		@Column(name = "R21_FACTORING_DEBTORS")
		private BigDecimal r21FactoringDebtors;

		@Column(name = "R21_LEASING")
		private BigDecimal r21Leasing;

		@Column(name = "R22_FACTORING_DEBTORS")
		private BigDecimal r22FactoringDebtors;

		@Column(name = "R22_LEASING")
		private BigDecimal r22Leasing;

		@Column(name = "R23_FACTORING_DEBTORS")
		private BigDecimal r23FactoringDebtors;

		@Column(name = "R23_LEASING")
		private BigDecimal r23Leasing;

		@Column(name = "R24_FACTORING_DEBTORS")
		private BigDecimal r24FactoringDebtors;

		@Column(name = "R24_LEASING")
		private BigDecimal r24Leasing;

		@Column(name = "R25_FACTORING_DEBTORS")
		private BigDecimal r25FactoringDebtors;

		@Column(name = "R25_LEASING")
		private BigDecimal r25Leasing;

		@Column(name = "R26_FACTORING_DEBTORS")
		private BigDecimal r26FactoringDebtors;

		@Column(name = "R26_LEASING")
		private BigDecimal r26Leasing;

		@Column(name = "R27_FACTORING_DEBTORS")
		private BigDecimal r27FactoringDebtors;

		@Column(name = "R27_LEASING")
		private BigDecimal r27Leasing;

		@Column(name = "R28_FACTORING_DEBTORS")
		private BigDecimal r28FactoringDebtors;

		@Column(name = "R28_LEASING")
		private BigDecimal r28Leasing;

		@Column(name = "R29_FACTORING_DEBTORS")
		private BigDecimal r29FactoringDebtors;

		@Column(name = "R29_LEASING")
		private BigDecimal r29Leasing;

		@Column(name = "R30_FACTORING_DEBTORS")
		private BigDecimal r30FactoringDebtors;

		@Column(name = "R30_LEASING")
		private BigDecimal r30Leasing;

		@Column(name = "R31_FACTORING_DEBTORS")
		private BigDecimal r31FactoringDebtors;

		@Column(name = "R31_LEASING")
		private BigDecimal r31Leasing;

		@Column(name = "R32_FACTORING_DEBTORS")
		private BigDecimal r32FactoringDebtors;

		@Column(name = "R32_LEASING")
		private BigDecimal r32Leasing;

		@Column(name = "R33_FACTORING_DEBTORS")
		private BigDecimal r33FactoringDebtors;

		@Column(name = "R33_LEASING")
		private BigDecimal r33Leasing;

		@Column(name = "R34_FACTORING_DEBTORS")
		private BigDecimal r34FactoringDebtors;

		@Column(name = "R34_LEASING")
		private BigDecimal r34Leasing;

		@Column(name = "R35_FACTORING_DEBTORS")
		private BigDecimal r35FactoringDebtors;

		@Column(name = "R35_LEASING")
		private BigDecimal r35Leasing;

		@Column(name = "R36_FACTORING_DEBTORS")
		private BigDecimal r36FactoringDebtors;

		@Column(name = "R36_LEASING")
		private BigDecimal r36Leasing;

		@Column(name = "R37_FACTORING_DEBTORS")
		private BigDecimal r37FactoringDebtors;

		@Column(name = "R37_LEASING")
		private BigDecimal r37Leasing;

		@Column(name = "R38_FACTORING_DEBTORS")
		private BigDecimal r38FactoringDebtors;

		@Column(name = "R38_LEASING")
		private BigDecimal r38Leasing;

		@Column(name = "R39_FACTORING_DEBTORS")
		private BigDecimal r39FactoringDebtors;

		@Column(name = "R39_LEASING")
		private BigDecimal r39Leasing;

		@Column(name = "R40_FACTORING_DEBTORS")
		private BigDecimal r40FactoringDebtors;

		@Column(name = "R40_LEASING")
		private BigDecimal r40Leasing;

		@Column(name = "R41_FACTORING_DEBTORS")
		private BigDecimal r41FactoringDebtors;

		@Column(name = "R41_LEASING")
		private BigDecimal r41Leasing;

		@Column(name = "R42_FACTORING_DEBTORS")
		private BigDecimal r42FactoringDebtors;

		@Column(name = "R42_LEASING")
		private BigDecimal r42Leasing;

		@Column(name = "R43_FACTORING_DEBTORS")
		private BigDecimal r43FactoringDebtors;

		@Column(name = "R43_LEASING")
		private BigDecimal r43Leasing;

		@Column(name = "R44_FACTORING_DEBTORS")
		private BigDecimal r44FactoringDebtors;

		@Column(name = "R44_LEASING")
		private BigDecimal r44Leasing;

		@Column(name = "R45_FACTORING_DEBTORS")
		private BigDecimal r45FactoringDebtors;

		@Column(name = "R45_LEASING")
		private BigDecimal r45Leasing;

		@Column(name = "R46_FACTORING_DEBTORS")
		private BigDecimal r46FactoringDebtors;

		@Column(name = "R46_LEASING")
		private BigDecimal r46Leasing;

		@Column(name = "R47_FACTORING_DEBTORS")
		private BigDecimal r47FactoringDebtors;

		@Column(name = "R47_LEASING")
		private BigDecimal r47Leasing;

		@Column(name = "R48_FACTORING_DEBTORS")
		private BigDecimal r48FactoringDebtors;

		@Column(name = "R48_LEASING")
		private BigDecimal r48Leasing;

		@Column(name = "R49_FACTORING_DEBTORS")
		private BigDecimal r49FactoringDebtors;

		@Column(name = "R49_LEASING")
		private BigDecimal r49Leasing;

		@Column(name = "R50_FACTORING_DEBTORS")
		private BigDecimal r50FactoringDebtors;

		@Column(name = "R50_LEASING")
		private BigDecimal r50Leasing;

		@Column(name = "R51_FACTORING_DEBTORS")
		private BigDecimal r51FactoringDebtors;

		@Column(name = "R51_LEASING")
		private BigDecimal r51Leasing;

		@Column(name = "R52_FACTORING_DEBTORS")
		private BigDecimal r52FactoringDebtors;

		@Column(name = "R52_LEASING")
		private BigDecimal r52Leasing;

		@Column(name = "R53_FACTORING_DEBTORS")
		private BigDecimal r53FactoringDebtors;

		@Column(name = "R53_LEASING")
		private BigDecimal r53Leasing;

		@Column(name = "R54_FACTORING_DEBTORS")
		private BigDecimal r54FactoringDebtors;

		@Column(name = "R54_LEASING")
		private BigDecimal r54Leasing;

		@Column(name = "R55_FACTORING_DEBTORS")
		private BigDecimal r55FactoringDebtors;

		@Column(name = "R55_LEASING")
		private BigDecimal r55Leasing;

		@Column(name = "R56_FACTORING_DEBTORS")
		private BigDecimal r56FactoringDebtors;

		@Column(name = "R56_LEASING")
		private BigDecimal r56Leasing;

		@Column(name = "R57_FACTORING_DEBTORS")
		private BigDecimal r57FactoringDebtors;

		@Column(name = "R57_LEASING")
		private BigDecimal r57Leasing;

		@Column(name = "R58_FACTORING_DEBTORS")
		private BigDecimal r58FactoringDebtors;

		@Column(name = "R58_LEASING")
		private BigDecimal r58Leasing;

		@Column(name = "R59_FACTORING_DEBTORS")
		private BigDecimal r59FactoringDebtors;

		@Column(name = "R59_LEASING")
		private BigDecimal r59Leasing;

		@Column(name = "R60_FACTORING_DEBTORS")
		private BigDecimal r60FactoringDebtors;

		@Column(name = "R60_LEASING")
		private BigDecimal r60Leasing;

		@Column(name = "R61_FACTORING_DEBTORS")
		private BigDecimal r61FactoringDebtors;

		@Column(name = "R61_LEASING")
		private BigDecimal r61Leasing;

		@Column(name = "R62_FACTORING_DEBTORS")
		private BigDecimal r62FactoringDebtors;

		@Column(name = "R62_LEASING")
		private BigDecimal r62Leasing;

		@Column(name = "R63_FACTORING_DEBTORS")
		private BigDecimal r63FactoringDebtors;

		@Column(name = "R63_LEASING")
		private BigDecimal r63Leasing;

		@Column(name = "R64_FACTORING_DEBTORS")
		private BigDecimal r64FactoringDebtors;

		@Column(name = "R64_LEASING")
		private BigDecimal r64Leasing;
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

		public BigDecimal getR11FactoringDebtors() {
			return r11FactoringDebtors;
		}

		public void setR11FactoringDebtors(BigDecimal r11FactoringDebtors) {
			this.r11FactoringDebtors = r11FactoringDebtors;
		}

		public BigDecimal getR11Leasing() {
			return r11Leasing;
		}

		public void setR11Leasing(BigDecimal r11Leasing) {
			this.r11Leasing = r11Leasing;
		}

		public BigDecimal getR12FactoringDebtors() {
			return r12FactoringDebtors;
		}

		public void setR12FactoringDebtors(BigDecimal r12FactoringDebtors) {
			this.r12FactoringDebtors = r12FactoringDebtors;
		}

		public BigDecimal getR12Leasing() {
			return r12Leasing;
		}

		public void setR12Leasing(BigDecimal r12Leasing) {
			this.r12Leasing = r12Leasing;
		}

		public BigDecimal getR13FactoringDebtors() {
			return r13FactoringDebtors;
		}

		public void setR13FactoringDebtors(BigDecimal r13FactoringDebtors) {
			this.r13FactoringDebtors = r13FactoringDebtors;
		}

		public BigDecimal getR13Leasing() {
			return r13Leasing;
		}

		public void setR13Leasing(BigDecimal r13Leasing) {
			this.r13Leasing = r13Leasing;
		}

		public BigDecimal getR14FactoringDebtors() {
			return r14FactoringDebtors;
		}

		public void setR14FactoringDebtors(BigDecimal r14FactoringDebtors) {
			this.r14FactoringDebtors = r14FactoringDebtors;
		}

		public BigDecimal getR14Leasing() {
			return r14Leasing;
		}

		public void setR14Leasing(BigDecimal r14Leasing) {
			this.r14Leasing = r14Leasing;
		}

		public BigDecimal getR15FactoringDebtors() {
			return r15FactoringDebtors;
		}

		public void setR15FactoringDebtors(BigDecimal r15FactoringDebtors) {
			this.r15FactoringDebtors = r15FactoringDebtors;
		}

		public BigDecimal getR15Leasing() {
			return r15Leasing;
		}

		public void setR15Leasing(BigDecimal r15Leasing) {
			this.r15Leasing = r15Leasing;
		}

		public BigDecimal getR16FactoringDebtors() {
			return r16FactoringDebtors;
		}

		public void setR16FactoringDebtors(BigDecimal r16FactoringDebtors) {
			this.r16FactoringDebtors = r16FactoringDebtors;
		}

		public BigDecimal getR16Leasing() {
			return r16Leasing;
		}

		public void setR16Leasing(BigDecimal r16Leasing) {
			this.r16Leasing = r16Leasing;
		}

		public BigDecimal getR17FactoringDebtors() {
			return r17FactoringDebtors;
		}

		public void setR17FactoringDebtors(BigDecimal r17FactoringDebtors) {
			this.r17FactoringDebtors = r17FactoringDebtors;
		}

		public BigDecimal getR17Leasing() {
			return r17Leasing;
		}

		public void setR17Leasing(BigDecimal r17Leasing) {
			this.r17Leasing = r17Leasing;
		}

		public BigDecimal getR18FactoringDebtors() {
			return r18FactoringDebtors;
		}

		public void setR18FactoringDebtors(BigDecimal r18FactoringDebtors) {
			this.r18FactoringDebtors = r18FactoringDebtors;
		}

		public BigDecimal getR18Leasing() {
			return r18Leasing;
		}

		public void setR18Leasing(BigDecimal r18Leasing) {
			this.r18Leasing = r18Leasing;
		}

		public BigDecimal getR19FactoringDebtors() {
			return r19FactoringDebtors;
		}

		public void setR19FactoringDebtors(BigDecimal r19FactoringDebtors) {
			this.r19FactoringDebtors = r19FactoringDebtors;
		}

		public BigDecimal getR19Leasing() {
			return r19Leasing;
		}

		public void setR19Leasing(BigDecimal r19Leasing) {
			this.r19Leasing = r19Leasing;
		}

		public BigDecimal getR20FactoringDebtors() {
			return r20FactoringDebtors;
		}

		public void setR20FactoringDebtors(BigDecimal r20FactoringDebtors) {
			this.r20FactoringDebtors = r20FactoringDebtors;
		}

		public BigDecimal getR20Leasing() {
			return r20Leasing;
		}

		public void setR20Leasing(BigDecimal r20Leasing) {
			this.r20Leasing = r20Leasing;
		}

		public BigDecimal getR21FactoringDebtors() {
			return r21FactoringDebtors;
		}

		public void setR21FactoringDebtors(BigDecimal r21FactoringDebtors) {
			this.r21FactoringDebtors = r21FactoringDebtors;
		}

		public BigDecimal getR21Leasing() {
			return r21Leasing;
		}

		public void setR21Leasing(BigDecimal r21Leasing) {
			this.r21Leasing = r21Leasing;
		}

		public BigDecimal getR22FactoringDebtors() {
			return r22FactoringDebtors;
		}

		public void setR22FactoringDebtors(BigDecimal r22FactoringDebtors) {
			this.r22FactoringDebtors = r22FactoringDebtors;
		}

		public BigDecimal getR22Leasing() {
			return r22Leasing;
		}

		public void setR22Leasing(BigDecimal r22Leasing) {
			this.r22Leasing = r22Leasing;
		}

		public BigDecimal getR23FactoringDebtors() {
			return r23FactoringDebtors;
		}

		public void setR23FactoringDebtors(BigDecimal r23FactoringDebtors) {
			this.r23FactoringDebtors = r23FactoringDebtors;
		}

		public BigDecimal getR23Leasing() {
			return r23Leasing;
		}

		public void setR23Leasing(BigDecimal r23Leasing) {
			this.r23Leasing = r23Leasing;
		}

		public BigDecimal getR24FactoringDebtors() {
			return r24FactoringDebtors;
		}

		public void setR24FactoringDebtors(BigDecimal r24FactoringDebtors) {
			this.r24FactoringDebtors = r24FactoringDebtors;
		}

		public BigDecimal getR24Leasing() {
			return r24Leasing;
		}

		public void setR24Leasing(BigDecimal r24Leasing) {
			this.r24Leasing = r24Leasing;
		}

		public BigDecimal getR25FactoringDebtors() {
			return r25FactoringDebtors;
		}

		public void setR25FactoringDebtors(BigDecimal r25FactoringDebtors) {
			this.r25FactoringDebtors = r25FactoringDebtors;
		}

		public BigDecimal getR25Leasing() {
			return r25Leasing;
		}

		public void setR25Leasing(BigDecimal r25Leasing) {
			this.r25Leasing = r25Leasing;
		}

		public BigDecimal getR26FactoringDebtors() {
			return r26FactoringDebtors;
		}

		public void setR26FactoringDebtors(BigDecimal r26FactoringDebtors) {
			this.r26FactoringDebtors = r26FactoringDebtors;
		}

		public BigDecimal getR26Leasing() {
			return r26Leasing;
		}

		public void setR26Leasing(BigDecimal r26Leasing) {
			this.r26Leasing = r26Leasing;
		}

		public BigDecimal getR27FactoringDebtors() {
			return r27FactoringDebtors;
		}

		public void setR27FactoringDebtors(BigDecimal r27FactoringDebtors) {
			this.r27FactoringDebtors = r27FactoringDebtors;
		}

		public BigDecimal getR27Leasing() {
			return r27Leasing;
		}

		public void setR27Leasing(BigDecimal r27Leasing) {
			this.r27Leasing = r27Leasing;
		}

		public BigDecimal getR28FactoringDebtors() {
			return r28FactoringDebtors;
		}

		public void setR28FactoringDebtors(BigDecimal r28FactoringDebtors) {
			this.r28FactoringDebtors = r28FactoringDebtors;
		}

		public BigDecimal getR28Leasing() {
			return r28Leasing;
		}

		public void setR28Leasing(BigDecimal r28Leasing) {
			this.r28Leasing = r28Leasing;
		}

		public BigDecimal getR29FactoringDebtors() {
			return r29FactoringDebtors;
		}

		public void setR29FactoringDebtors(BigDecimal r29FactoringDebtors) {
			this.r29FactoringDebtors = r29FactoringDebtors;
		}

		public BigDecimal getR29Leasing() {
			return r29Leasing;
		}

		public void setR29Leasing(BigDecimal r29Leasing) {
			this.r29Leasing = r29Leasing;
		}

		public BigDecimal getR30FactoringDebtors() {
			return r30FactoringDebtors;
		}

		public void setR30FactoringDebtors(BigDecimal r30FactoringDebtors) {
			this.r30FactoringDebtors = r30FactoringDebtors;
		}

		public BigDecimal getR30Leasing() {
			return r30Leasing;
		}

		public void setR30Leasing(BigDecimal r30Leasing) {
			this.r30Leasing = r30Leasing;
		}

		public BigDecimal getR31FactoringDebtors() {
			return r31FactoringDebtors;
		}

		public void setR31FactoringDebtors(BigDecimal r31FactoringDebtors) {
			this.r31FactoringDebtors = r31FactoringDebtors;
		}

		public BigDecimal getR31Leasing() {
			return r31Leasing;
		}

		public void setR31Leasing(BigDecimal r31Leasing) {
			this.r31Leasing = r31Leasing;
		}

		public BigDecimal getR32FactoringDebtors() {
			return r32FactoringDebtors;
		}

		public void setR32FactoringDebtors(BigDecimal r32FactoringDebtors) {
			this.r32FactoringDebtors = r32FactoringDebtors;
		}

		public BigDecimal getR32Leasing() {
			return r32Leasing;
		}

		public void setR32Leasing(BigDecimal r32Leasing) {
			this.r32Leasing = r32Leasing;
		}

		public BigDecimal getR33FactoringDebtors() {
			return r33FactoringDebtors;
		}

		public void setR33FactoringDebtors(BigDecimal r33FactoringDebtors) {
			this.r33FactoringDebtors = r33FactoringDebtors;
		}

		public BigDecimal getR33Leasing() {
			return r33Leasing;
		}

		public void setR33Leasing(BigDecimal r33Leasing) {
			this.r33Leasing = r33Leasing;
		}

		public BigDecimal getR34FactoringDebtors() {
			return r34FactoringDebtors;
		}

		public void setR34FactoringDebtors(BigDecimal r34FactoringDebtors) {
			this.r34FactoringDebtors = r34FactoringDebtors;
		}

		public BigDecimal getR34Leasing() {
			return r34Leasing;
		}

		public void setR34Leasing(BigDecimal r34Leasing) {
			this.r34Leasing = r34Leasing;
		}

		public BigDecimal getR35FactoringDebtors() {
			return r35FactoringDebtors;
		}

		public void setR35FactoringDebtors(BigDecimal r35FactoringDebtors) {
			this.r35FactoringDebtors = r35FactoringDebtors;
		}

		public BigDecimal getR35Leasing() {
			return r35Leasing;
		}

		public void setR35Leasing(BigDecimal r35Leasing) {
			this.r35Leasing = r35Leasing;
		}

		public BigDecimal getR36FactoringDebtors() {
			return r36FactoringDebtors;
		}

		public void setR36FactoringDebtors(BigDecimal r36FactoringDebtors) {
			this.r36FactoringDebtors = r36FactoringDebtors;
		}

		public BigDecimal getR36Leasing() {
			return r36Leasing;
		}

		public void setR36Leasing(BigDecimal r36Leasing) {
			this.r36Leasing = r36Leasing;
		}

		public BigDecimal getR37FactoringDebtors() {
			return r37FactoringDebtors;
		}

		public void setR37FactoringDebtors(BigDecimal r37FactoringDebtors) {
			this.r37FactoringDebtors = r37FactoringDebtors;
		}

		public BigDecimal getR37Leasing() {
			return r37Leasing;
		}

		public void setR37Leasing(BigDecimal r37Leasing) {
			this.r37Leasing = r37Leasing;
		}

		public BigDecimal getR38FactoringDebtors() {
			return r38FactoringDebtors;
		}

		public void setR38FactoringDebtors(BigDecimal r38FactoringDebtors) {
			this.r38FactoringDebtors = r38FactoringDebtors;
		}

		public BigDecimal getR38Leasing() {
			return r38Leasing;
		}

		public void setR38Leasing(BigDecimal r38Leasing) {
			this.r38Leasing = r38Leasing;
		}

		public BigDecimal getR39FactoringDebtors() {
			return r39FactoringDebtors;
		}

		public void setR39FactoringDebtors(BigDecimal r39FactoringDebtors) {
			this.r39FactoringDebtors = r39FactoringDebtors;
		}

		public BigDecimal getR39Leasing() {
			return r39Leasing;
		}

		public void setR39Leasing(BigDecimal r39Leasing) {
			this.r39Leasing = r39Leasing;
		}

		public BigDecimal getR40FactoringDebtors() {
			return r40FactoringDebtors;
		}

		public void setR40FactoringDebtors(BigDecimal r40FactoringDebtors) {
			this.r40FactoringDebtors = r40FactoringDebtors;
		}

		public BigDecimal getR40Leasing() {
			return r40Leasing;
		}

		public void setR40Leasing(BigDecimal r40Leasing) {
			this.r40Leasing = r40Leasing;
		}

		public BigDecimal getR41FactoringDebtors() {
			return r41FactoringDebtors;
		}

		public void setR41FactoringDebtors(BigDecimal r41FactoringDebtors) {
			this.r41FactoringDebtors = r41FactoringDebtors;
		}

		public BigDecimal getR41Leasing() {
			return r41Leasing;
		}

		public void setR41Leasing(BigDecimal r41Leasing) {
			this.r41Leasing = r41Leasing;
		}

		public BigDecimal getR42FactoringDebtors() {
			return r42FactoringDebtors;
		}

		public void setR42FactoringDebtors(BigDecimal r42FactoringDebtors) {
			this.r42FactoringDebtors = r42FactoringDebtors;
		}

		public BigDecimal getR42Leasing() {
			return r42Leasing;
		}

		public void setR42Leasing(BigDecimal r42Leasing) {
			this.r42Leasing = r42Leasing;
		}

		public BigDecimal getR43FactoringDebtors() {
			return r43FactoringDebtors;
		}

		public void setR43FactoringDebtors(BigDecimal r43FactoringDebtors) {
			this.r43FactoringDebtors = r43FactoringDebtors;
		}

		public BigDecimal getR43Leasing() {
			return r43Leasing;
		}

		public void setR43Leasing(BigDecimal r43Leasing) {
			this.r43Leasing = r43Leasing;
		}

		public BigDecimal getR44FactoringDebtors() {
			return r44FactoringDebtors;
		}

		public void setR44FactoringDebtors(BigDecimal r44FactoringDebtors) {
			this.r44FactoringDebtors = r44FactoringDebtors;
		}

		public BigDecimal getR44Leasing() {
			return r44Leasing;
		}

		public void setR44Leasing(BigDecimal r44Leasing) {
			this.r44Leasing = r44Leasing;
		}

		public BigDecimal getR45FactoringDebtors() {
			return r45FactoringDebtors;
		}

		public void setR45FactoringDebtors(BigDecimal r45FactoringDebtors) {
			this.r45FactoringDebtors = r45FactoringDebtors;
		}

		public BigDecimal getR45Leasing() {
			return r45Leasing;
		}

		public void setR45Leasing(BigDecimal r45Leasing) {
			this.r45Leasing = r45Leasing;
		}

		public BigDecimal getR46FactoringDebtors() {
			return r46FactoringDebtors;
		}

		public void setR46FactoringDebtors(BigDecimal r46FactoringDebtors) {
			this.r46FactoringDebtors = r46FactoringDebtors;
		}

		public BigDecimal getR46Leasing() {
			return r46Leasing;
		}

		public void setR46Leasing(BigDecimal r46Leasing) {
			this.r46Leasing = r46Leasing;
		}

		public BigDecimal getR47FactoringDebtors() {
			return r47FactoringDebtors;
		}

		public void setR47FactoringDebtors(BigDecimal r47FactoringDebtors) {
			this.r47FactoringDebtors = r47FactoringDebtors;
		}

		public BigDecimal getR47Leasing() {
			return r47Leasing;
		}

		public void setR47Leasing(BigDecimal r47Leasing) {
			this.r47Leasing = r47Leasing;
		}

		public BigDecimal getR48FactoringDebtors() {
			return r48FactoringDebtors;
		}

		public void setR48FactoringDebtors(BigDecimal r48FactoringDebtors) {
			this.r48FactoringDebtors = r48FactoringDebtors;
		}

		public BigDecimal getR48Leasing() {
			return r48Leasing;
		}

		public void setR48Leasing(BigDecimal r48Leasing) {
			this.r48Leasing = r48Leasing;
		}

		public BigDecimal getR49FactoringDebtors() {
			return r49FactoringDebtors;
		}

		public void setR49FactoringDebtors(BigDecimal r49FactoringDebtors) {
			this.r49FactoringDebtors = r49FactoringDebtors;
		}

		public BigDecimal getR49Leasing() {
			return r49Leasing;
		}

		public void setR49Leasing(BigDecimal r49Leasing) {
			this.r49Leasing = r49Leasing;
		}

		public BigDecimal getR50FactoringDebtors() {
			return r50FactoringDebtors;
		}

		public void setR50FactoringDebtors(BigDecimal r50FactoringDebtors) {
			this.r50FactoringDebtors = r50FactoringDebtors;
		}

		public BigDecimal getR50Leasing() {
			return r50Leasing;
		}

		public void setR50Leasing(BigDecimal r50Leasing) {
			this.r50Leasing = r50Leasing;
		}

		public BigDecimal getR51FactoringDebtors() {
			return r51FactoringDebtors;
		}

		public void setR51FactoringDebtors(BigDecimal r51FactoringDebtors) {
			this.r51FactoringDebtors = r51FactoringDebtors;
		}

		public BigDecimal getR51Leasing() {
			return r51Leasing;
		}

		public void setR51Leasing(BigDecimal r51Leasing) {
			this.r51Leasing = r51Leasing;
		}

		public BigDecimal getR52FactoringDebtors() {
			return r52FactoringDebtors;
		}

		public void setR52FactoringDebtors(BigDecimal r52FactoringDebtors) {
			this.r52FactoringDebtors = r52FactoringDebtors;
		}

		public BigDecimal getR52Leasing() {
			return r52Leasing;
		}

		public void setR52Leasing(BigDecimal r52Leasing) {
			this.r52Leasing = r52Leasing;
		}

		public BigDecimal getR53FactoringDebtors() {
			return r53FactoringDebtors;
		}

		public void setR53FactoringDebtors(BigDecimal r53FactoringDebtors) {
			this.r53FactoringDebtors = r53FactoringDebtors;
		}

		public BigDecimal getR53Leasing() {
			return r53Leasing;
		}

		public void setR53Leasing(BigDecimal r53Leasing) {
			this.r53Leasing = r53Leasing;
		}

		public BigDecimal getR54FactoringDebtors() {
			return r54FactoringDebtors;
		}

		public void setR54FactoringDebtors(BigDecimal r54FactoringDebtors) {
			this.r54FactoringDebtors = r54FactoringDebtors;
		}

		public BigDecimal getR54Leasing() {
			return r54Leasing;
		}

		public void setR54Leasing(BigDecimal r54Leasing) {
			this.r54Leasing = r54Leasing;
		}

		public BigDecimal getR55FactoringDebtors() {
			return r55FactoringDebtors;
		}

		public void setR55FactoringDebtors(BigDecimal r55FactoringDebtors) {
			this.r55FactoringDebtors = r55FactoringDebtors;
		}

		public BigDecimal getR55Leasing() {
			return r55Leasing;
		}

		public void setR55Leasing(BigDecimal r55Leasing) {
			this.r55Leasing = r55Leasing;
		}

		public BigDecimal getR56FactoringDebtors() {
			return r56FactoringDebtors;
		}

		public void setR56FactoringDebtors(BigDecimal r56FactoringDebtors) {
			this.r56FactoringDebtors = r56FactoringDebtors;
		}

		public BigDecimal getR56Leasing() {
			return r56Leasing;
		}

		public void setR56Leasing(BigDecimal r56Leasing) {
			this.r56Leasing = r56Leasing;
		}

		public BigDecimal getR57FactoringDebtors() {
			return r57FactoringDebtors;
		}

		public void setR57FactoringDebtors(BigDecimal r57FactoringDebtors) {
			this.r57FactoringDebtors = r57FactoringDebtors;
		}

		public BigDecimal getR57Leasing() {
			return r57Leasing;
		}

		public void setR57Leasing(BigDecimal r57Leasing) {
			this.r57Leasing = r57Leasing;
		}

		public BigDecimal getR58FactoringDebtors() {
			return r58FactoringDebtors;
		}

		public void setR58FactoringDebtors(BigDecimal r58FactoringDebtors) {
			this.r58FactoringDebtors = r58FactoringDebtors;
		}

		public BigDecimal getR58Leasing() {
			return r58Leasing;
		}

		public void setR58Leasing(BigDecimal r58Leasing) {
			this.r58Leasing = r58Leasing;
		}

		public BigDecimal getR59FactoringDebtors() {
			return r59FactoringDebtors;
		}

		public void setR59FactoringDebtors(BigDecimal r59FactoringDebtors) {
			this.r59FactoringDebtors = r59FactoringDebtors;
		}

		public BigDecimal getR59Leasing() {
			return r59Leasing;
		}

		public void setR59Leasing(BigDecimal r59Leasing) {
			this.r59Leasing = r59Leasing;
		}

		public BigDecimal getR60FactoringDebtors() {
			return r60FactoringDebtors;
		}

		public void setR60FactoringDebtors(BigDecimal r60FactoringDebtors) {
			this.r60FactoringDebtors = r60FactoringDebtors;
		}

		public BigDecimal getR60Leasing() {
			return r60Leasing;
		}

		public void setR60Leasing(BigDecimal r60Leasing) {
			this.r60Leasing = r60Leasing;
		}

		public BigDecimal getR61FactoringDebtors() {
			return r61FactoringDebtors;
		}

		public void setR61FactoringDebtors(BigDecimal r61FactoringDebtors) {
			this.r61FactoringDebtors = r61FactoringDebtors;
		}

		public BigDecimal getR61Leasing() {
			return r61Leasing;
		}

		public void setR61Leasing(BigDecimal r61Leasing) {
			this.r61Leasing = r61Leasing;
		}

		public BigDecimal getR62FactoringDebtors() {
			return r62FactoringDebtors;
		}

		public void setR62FactoringDebtors(BigDecimal r62FactoringDebtors) {
			this.r62FactoringDebtors = r62FactoringDebtors;
		}

		public BigDecimal getR62Leasing() {
			return r62Leasing;
		}

		public void setR62Leasing(BigDecimal r62Leasing) {
			this.r62Leasing = r62Leasing;
		}

		public BigDecimal getR63FactoringDebtors() {
			return r63FactoringDebtors;
		}

		public void setR63FactoringDebtors(BigDecimal r63FactoringDebtors) {
			this.r63FactoringDebtors = r63FactoringDebtors;
		}

		public BigDecimal getR63Leasing() {
			return r63Leasing;
		}

		public void setR63Leasing(BigDecimal r63Leasing) {
			this.r63Leasing = r63Leasing;
		}

		public BigDecimal getR64FactoringDebtors() {
			return r64FactoringDebtors;
		}

		public void setR64FactoringDebtors(BigDecimal r64FactoringDebtors) {
			this.r64FactoringDebtors = r64FactoringDebtors;
		}

		public BigDecimal getR64Leasing() {
			return r64Leasing;
		}

		public void setR64Leasing(BigDecimal r64Leasing) {
			this.r64Leasing = r64Leasing;
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

//ARCHIVAL ROW MAPPER

	class M_LA4ArchivalRowMapper1 implements RowMapper<M_LA4_Archival_Summary_Entity1> {

		@Override
		public M_LA4_Archival_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA4_Archival_Summary_Entity1 obj = new M_LA4_Archival_Summary_Entity1();

			// R11
			obj.setR11AdvancesByInstitutionalSector(rs.getString("R11_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR11Overdrafts(rs.getBigDecimal("R11_OVERDRAFTS"));
			obj.setR11OtherInstallmentLoans(rs.getBigDecimal("R11_OTHER_INSTALLMENT_LOANS"));
			obj.setR11Total(rs.getBigDecimal("R11_TOTAL"));

			// R12
			obj.setR12AdvancesByInstitutionalSector(rs.getString("R12_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR12Overdrafts(rs.getBigDecimal("R12_OVERDRAFTS"));
			obj.setR12OtherInstallmentLoans(rs.getBigDecimal("R12_OTHER_INSTALLMENT_LOANS"));
			obj.setR12Total(rs.getBigDecimal("R12_TOTAL"));

			// R13
			obj.setR13AdvancesByInstitutionalSector(rs.getString("R13_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR13Overdrafts(rs.getBigDecimal("R13_OVERDRAFTS"));
			obj.setR13OtherInstallmentLoans(rs.getBigDecimal("R13_OTHER_INSTALLMENT_LOANS"));
			obj.setR13Total(rs.getBigDecimal("R13_TOTAL"));

			// R14
			obj.setR14AdvancesByInstitutionalSector(rs.getString("R14_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR14Overdrafts(rs.getBigDecimal("R14_OVERDRAFTS"));
			obj.setR14OtherInstallmentLoans(rs.getBigDecimal("R14_OTHER_INSTALLMENT_LOANS"));
			obj.setR14Total(rs.getBigDecimal("R14_TOTAL"));

			// R15
			obj.setR15AdvancesByInstitutionalSector(rs.getString("R15_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR15Overdrafts(rs.getBigDecimal("R15_OVERDRAFTS"));
			obj.setR15OtherInstallmentLoans(rs.getBigDecimal("R15_OTHER_INSTALLMENT_LOANS"));
			obj.setR15Total(rs.getBigDecimal("R15_TOTAL"));

			// R16
			obj.setR16AdvancesByInstitutionalSector(rs.getString("R16_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR16Overdrafts(rs.getBigDecimal("R16_OVERDRAFTS"));
			obj.setR16OtherInstallmentLoans(rs.getBigDecimal("R16_OTHER_INSTALLMENT_LOANS"));
			obj.setR16Total(rs.getBigDecimal("R16_TOTAL"));

			// R17
			obj.setR17AdvancesByInstitutionalSector(rs.getString("R17_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR17Overdrafts(rs.getBigDecimal("R17_OVERDRAFTS"));
			obj.setR17OtherInstallmentLoans(rs.getBigDecimal("R17_OTHER_INSTALLMENT_LOANS"));
			obj.setR17Total(rs.getBigDecimal("R17_TOTAL"));

			// R18
			obj.setR18AdvancesByInstitutionalSector(rs.getString("R18_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR18Overdrafts(rs.getBigDecimal("R18_OVERDRAFTS"));
			obj.setR18OtherInstallmentLoans(rs.getBigDecimal("R18_OTHER_INSTALLMENT_LOANS"));
			obj.setR18Total(rs.getBigDecimal("R18_TOTAL"));

			// R19
			obj.setR19AdvancesByInstitutionalSector(rs.getString("R19_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR19Overdrafts(rs.getBigDecimal("R19_OVERDRAFTS"));
			obj.setR19OtherInstallmentLoans(rs.getBigDecimal("R19_OTHER_INSTALLMENT_LOANS"));
			obj.setR19Total(rs.getBigDecimal("R19_TOTAL"));

			// R20
			obj.setR20AdvancesByInstitutionalSector(rs.getString("R20_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR20Overdrafts(rs.getBigDecimal("R20_OVERDRAFTS"));
			obj.setR20OtherInstallmentLoans(rs.getBigDecimal("R20_OTHER_INSTALLMENT_LOANS"));
			obj.setR20Total(rs.getBigDecimal("R20_TOTAL"));

			// R21
			obj.setR21AdvancesByInstitutionalSector(rs.getString("R21_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR21Overdrafts(rs.getBigDecimal("R21_OVERDRAFTS"));
			obj.setR21OtherInstallmentLoans(rs.getBigDecimal("R21_OTHER_INSTALLMENT_LOANS"));
			obj.setR21Total(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22AdvancesByInstitutionalSector(rs.getString("R22_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR22Overdrafts(rs.getBigDecimal("R22_OVERDRAFTS"));
			obj.setR22OtherInstallmentLoans(rs.getBigDecimal("R22_OTHER_INSTALLMENT_LOANS"));
			obj.setR22Total(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23AdvancesByInstitutionalSector(rs.getString("R23_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR23Overdrafts(rs.getBigDecimal("R23_OVERDRAFTS"));
			obj.setR23OtherInstallmentLoans(rs.getBigDecimal("R23_OTHER_INSTALLMENT_LOANS"));
			obj.setR23Total(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24AdvancesByInstitutionalSector(rs.getString("R24_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR24Overdrafts(rs.getBigDecimal("R24_OVERDRAFTS"));
			obj.setR24OtherInstallmentLoans(rs.getBigDecimal("R24_OTHER_INSTALLMENT_LOANS"));
			obj.setR24Total(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25AdvancesByInstitutionalSector(rs.getString("R25_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR25Overdrafts(rs.getBigDecimal("R25_OVERDRAFTS"));
			obj.setR25OtherInstallmentLoans(rs.getBigDecimal("R25_OTHER_INSTALLMENT_LOANS"));
			obj.setR25Total(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26AdvancesByInstitutionalSector(rs.getString("R26_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR26Overdrafts(rs.getBigDecimal("R26_OVERDRAFTS"));
			obj.setR26OtherInstallmentLoans(rs.getBigDecimal("R26_OTHER_INSTALLMENT_LOANS"));
			obj.setR26Total(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27AdvancesByInstitutionalSector(rs.getString("R27_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR27Overdrafts(rs.getBigDecimal("R27_OVERDRAFTS"));
			obj.setR27OtherInstallmentLoans(rs.getBigDecimal("R27_OTHER_INSTALLMENT_LOANS"));
			obj.setR27Total(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28AdvancesByInstitutionalSector(rs.getString("R28_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR28Overdrafts(rs.getBigDecimal("R28_OVERDRAFTS"));
			obj.setR28OtherInstallmentLoans(rs.getBigDecimal("R28_OTHER_INSTALLMENT_LOANS"));
			obj.setR28Total(rs.getBigDecimal("R28_TOTAL"));

			// R29
			obj.setR29AdvancesByInstitutionalSector(rs.getString("R29_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR29Overdrafts(rs.getBigDecimal("R29_OVERDRAFTS"));
			obj.setR29OtherInstallmentLoans(rs.getBigDecimal("R29_OTHER_INSTALLMENT_LOANS"));
			obj.setR29Total(rs.getBigDecimal("R29_TOTAL"));

			// R30
			obj.setR30AdvancesByInstitutionalSector(rs.getString("R30_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR30Overdrafts(rs.getBigDecimal("R30_OVERDRAFTS"));
			obj.setR30OtherInstallmentLoans(rs.getBigDecimal("R30_OTHER_INSTALLMENT_LOANS"));
			obj.setR30Total(rs.getBigDecimal("R30_TOTAL"));

			// R31
			obj.setR31AdvancesByInstitutionalSector(rs.getString("R31_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR31Overdrafts(rs.getBigDecimal("R31_OVERDRAFTS"));
			obj.setR31OtherInstallmentLoans(rs.getBigDecimal("R31_OTHER_INSTALLMENT_LOANS"));
			obj.setR31Total(rs.getBigDecimal("R31_TOTAL"));

			// R32
			obj.setR32AdvancesByInstitutionalSector(rs.getString("R32_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR32Overdrafts(rs.getBigDecimal("R32_OVERDRAFTS"));
			obj.setR32OtherInstallmentLoans(rs.getBigDecimal("R32_OTHER_INSTALLMENT_LOANS"));
			obj.setR32Total(rs.getBigDecimal("R32_TOTAL"));

			// R33
			obj.setR33AdvancesByInstitutionalSector(rs.getString("R33_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR33Overdrafts(rs.getBigDecimal("R33_OVERDRAFTS"));
			obj.setR33OtherInstallmentLoans(rs.getBigDecimal("R33_OTHER_INSTALLMENT_LOANS"));
			obj.setR33Total(rs.getBigDecimal("R33_TOTAL"));

			// R34
			obj.setR34AdvancesByInstitutionalSector(rs.getString("R34_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR34Overdrafts(rs.getBigDecimal("R34_OVERDRAFTS"));
			obj.setR34OtherInstallmentLoans(rs.getBigDecimal("R34_OTHER_INSTALLMENT_LOANS"));
			obj.setR34Total(rs.getBigDecimal("R34_TOTAL"));

			// R35
			obj.setR35AdvancesByInstitutionalSector(rs.getString("R35_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR35Overdrafts(rs.getBigDecimal("R35_OVERDRAFTS"));
			obj.setR35OtherInstallmentLoans(rs.getBigDecimal("R35_OTHER_INSTALLMENT_LOANS"));
			obj.setR35Total(rs.getBigDecimal("R35_TOTAL"));

			// R36
			obj.setR36AdvancesByInstitutionalSector(rs.getString("R36_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR36Overdrafts(rs.getBigDecimal("R36_OVERDRAFTS"));
			obj.setR36OtherInstallmentLoans(rs.getBigDecimal("R36_OTHER_INSTALLMENT_LOANS"));
			obj.setR36Total(rs.getBigDecimal("R36_TOTAL"));

			// R37
			obj.setR37AdvancesByInstitutionalSector(rs.getString("R37_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR37Overdrafts(rs.getBigDecimal("R37_OVERDRAFTS"));
			obj.setR37OtherInstallmentLoans(rs.getBigDecimal("R37_OTHER_INSTALLMENT_LOANS"));
			obj.setR37Total(rs.getBigDecimal("R37_TOTAL"));

			// R38
			obj.setR38AdvancesByInstitutionalSector(rs.getString("R38_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR38Overdrafts(rs.getBigDecimal("R38_OVERDRAFTS"));
			obj.setR38OtherInstallmentLoans(rs.getBigDecimal("R38_OTHER_INSTALLMENT_LOANS"));
			obj.setR38Total(rs.getBigDecimal("R38_TOTAL"));

			// R39
			obj.setR39AdvancesByInstitutionalSector(rs.getString("R39_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR39Overdrafts(rs.getBigDecimal("R39_OVERDRAFTS"));
			obj.setR39OtherInstallmentLoans(rs.getBigDecimal("R39_OTHER_INSTALLMENT_LOANS"));
			obj.setR39Total(rs.getBigDecimal("R39_TOTAL"));

			// R40
			obj.setR40AdvancesByInstitutionalSector(rs.getString("R40_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR40Overdrafts(rs.getBigDecimal("R40_OVERDRAFTS"));
			obj.setR40OtherInstallmentLoans(rs.getBigDecimal("R40_OTHER_INSTALLMENT_LOANS"));
			obj.setR40Total(rs.getBigDecimal("R40_TOTAL"));

			// R41
			obj.setR41AdvancesByInstitutionalSector(rs.getString("R41_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR41Overdrafts(rs.getBigDecimal("R41_OVERDRAFTS"));
			obj.setR41OtherInstallmentLoans(rs.getBigDecimal("R41_OTHER_INSTALLMENT_LOANS"));
			obj.setR41Total(rs.getBigDecimal("R41_TOTAL"));

			// R42
			obj.setR42AdvancesByInstitutionalSector(rs.getString("R42_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR42Overdrafts(rs.getBigDecimal("R42_OVERDRAFTS"));
			obj.setR42OtherInstallmentLoans(rs.getBigDecimal("R42_OTHER_INSTALLMENT_LOANS"));
			obj.setR42Total(rs.getBigDecimal("R42_TOTAL"));

			// R43
			obj.setR43AdvancesByInstitutionalSector(rs.getString("R43_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR43Overdrafts(rs.getBigDecimal("R43_OVERDRAFTS"));
			obj.setR43OtherInstallmentLoans(rs.getBigDecimal("R43_OTHER_INSTALLMENT_LOANS"));
			obj.setR43Total(rs.getBigDecimal("R43_TOTAL"));

			// R44
			obj.setR44AdvancesByInstitutionalSector(rs.getString("R44_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR44Overdrafts(rs.getBigDecimal("R44_OVERDRAFTS"));
			obj.setR44OtherInstallmentLoans(rs.getBigDecimal("R44_OTHER_INSTALLMENT_LOANS"));
			obj.setR44Total(rs.getBigDecimal("R44_TOTAL"));

			// R45
			obj.setR45AdvancesByInstitutionalSector(rs.getString("R45_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR45Overdrafts(rs.getBigDecimal("R45_OVERDRAFTS"));
			obj.setR45OtherInstallmentLoans(rs.getBigDecimal("R45_OTHER_INSTALLMENT_LOANS"));
			obj.setR45Total(rs.getBigDecimal("R45_TOTAL"));

			// R46
			obj.setR46AdvancesByInstitutionalSector(rs.getString("R46_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR46Overdrafts(rs.getBigDecimal("R46_OVERDRAFTS"));
			obj.setR46OtherInstallmentLoans(rs.getBigDecimal("R46_OTHER_INSTALLMENT_LOANS"));
			obj.setR46Total(rs.getBigDecimal("R46_TOTAL"));

			// R47
			obj.setR47AdvancesByInstitutionalSector(rs.getString("R47_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR47Overdrafts(rs.getBigDecimal("R47_OVERDRAFTS"));
			obj.setR47OtherInstallmentLoans(rs.getBigDecimal("R47_OTHER_INSTALLMENT_LOANS"));
			obj.setR47Total(rs.getBigDecimal("R47_TOTAL"));

			// R48
			obj.setR48AdvancesByInstitutionalSector(rs.getString("R48_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR48Overdrafts(rs.getBigDecimal("R48_OVERDRAFTS"));
			obj.setR48OtherInstallmentLoans(rs.getBigDecimal("R48_OTHER_INSTALLMENT_LOANS"));
			obj.setR48Total(rs.getBigDecimal("R48_TOTAL"));

			// R49
			obj.setR49AdvancesByInstitutionalSector(rs.getString("R49_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR49Overdrafts(rs.getBigDecimal("R49_OVERDRAFTS"));
			obj.setR49OtherInstallmentLoans(rs.getBigDecimal("R49_OTHER_INSTALLMENT_LOANS"));
			obj.setR49Total(rs.getBigDecimal("R49_TOTAL"));

			// R50
			obj.setR50AdvancesByInstitutionalSector(rs.getString("R50_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR50Overdrafts(rs.getBigDecimal("R50_OVERDRAFTS"));
			obj.setR50OtherInstallmentLoans(rs.getBigDecimal("R50_OTHER_INSTALLMENT_LOANS"));
			obj.setR50Total(rs.getBigDecimal("R50_TOTAL"));

			// R51
			obj.setR51AdvancesByInstitutionalSector(rs.getString("R51_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR51Overdrafts(rs.getBigDecimal("R51_OVERDRAFTS"));
			obj.setR51OtherInstallmentLoans(rs.getBigDecimal("R51_OTHER_INSTALLMENT_LOANS"));
			obj.setR51Total(rs.getBigDecimal("R51_TOTAL"));

			// R52
			obj.setR52AdvancesByInstitutionalSector(rs.getString("R52_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR52Overdrafts(rs.getBigDecimal("R52_OVERDRAFTS"));
			obj.setR52OtherInstallmentLoans(rs.getBigDecimal("R52_OTHER_INSTALLMENT_LOANS"));
			obj.setR52Total(rs.getBigDecimal("R52_TOTAL"));

			// R53
			obj.setR53AdvancesByInstitutionalSector(rs.getString("R53_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR53Overdrafts(rs.getBigDecimal("R53_OVERDRAFTS"));
			obj.setR53OtherInstallmentLoans(rs.getBigDecimal("R53_OTHER_INSTALLMENT_LOANS"));
			obj.setR53Total(rs.getBigDecimal("R53_TOTAL"));

			// R54
			obj.setR54AdvancesByInstitutionalSector(rs.getString("R54_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR54Overdrafts(rs.getBigDecimal("R54_OVERDRAFTS"));
			obj.setR54OtherInstallmentLoans(rs.getBigDecimal("R54_OTHER_INSTALLMENT_LOANS"));
			obj.setR54Total(rs.getBigDecimal("R54_TOTAL"));

			// R55
			obj.setR55AdvancesByInstitutionalSector(rs.getString("R55_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR55Overdrafts(rs.getBigDecimal("R55_OVERDRAFTS"));
			obj.setR55OtherInstallmentLoans(rs.getBigDecimal("R55_OTHER_INSTALLMENT_LOANS"));
			obj.setR55Total(rs.getBigDecimal("R55_TOTAL"));

			// R56
			obj.setR56AdvancesByInstitutionalSector(rs.getString("R56_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR56Overdrafts(rs.getBigDecimal("R56_OVERDRAFTS"));
			obj.setR56OtherInstallmentLoans(rs.getBigDecimal("R56_OTHER_INSTALLMENT_LOANS"));
			obj.setR56Total(rs.getBigDecimal("R56_TOTAL"));

			// R57
			obj.setR57AdvancesByInstitutionalSector(rs.getString("R57_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR57Overdrafts(rs.getBigDecimal("R57_OVERDRAFTS"));
			obj.setR57OtherInstallmentLoans(rs.getBigDecimal("R57_OTHER_INSTALLMENT_LOANS"));
			obj.setR57Total(rs.getBigDecimal("R57_TOTAL"));

			// R58
			obj.setR58AdvancesByInstitutionalSector(rs.getString("R58_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR58Overdrafts(rs.getBigDecimal("R58_OVERDRAFTS"));
			obj.setR58OtherInstallmentLoans(rs.getBigDecimal("R58_OTHER_INSTALLMENT_LOANS"));
			obj.setR58Total(rs.getBigDecimal("R58_TOTAL"));

			// R59
			obj.setR59AdvancesByInstitutionalSector(rs.getString("R59_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR59Overdrafts(rs.getBigDecimal("R59_OVERDRAFTS"));
			obj.setR59OtherInstallmentLoans(rs.getBigDecimal("R59_OTHER_INSTALLMENT_LOANS"));
			obj.setR59Total(rs.getBigDecimal("R59_TOTAL"));

			// R60
			obj.setR60AdvancesByInstitutionalSector(rs.getString("R60_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR60Overdrafts(rs.getBigDecimal("R60_OVERDRAFTS"));
			obj.setR60OtherInstallmentLoans(rs.getBigDecimal("R60_OTHER_INSTALLMENT_LOANS"));
			obj.setR60Total(rs.getBigDecimal("R60_TOTAL"));

			// R61
			obj.setR61AdvancesByInstitutionalSector(rs.getString("R61_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR61Overdrafts(rs.getBigDecimal("R61_OVERDRAFTS"));
			obj.setR61OtherInstallmentLoans(rs.getBigDecimal("R61_OTHER_INSTALLMENT_LOANS"));
			obj.setR61Total(rs.getBigDecimal("R61_TOTAL"));

			// R62
			obj.setR62AdvancesByInstitutionalSector(rs.getString("R62_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR62Overdrafts(rs.getBigDecimal("R62_OVERDRAFTS"));
			obj.setR62OtherInstallmentLoans(rs.getBigDecimal("R62_OTHER_INSTALLMENT_LOANS"));
			obj.setR62Total(rs.getBigDecimal("R62_TOTAL"));

			// R63
			obj.setR63AdvancesByInstitutionalSector(rs.getString("R63_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR63Overdrafts(rs.getBigDecimal("R63_OVERDRAFTS"));
			obj.setR63OtherInstallmentLoans(rs.getBigDecimal("R63_OTHER_INSTALLMENT_LOANS"));
			obj.setR63Total(rs.getBigDecimal("R63_TOTAL"));

			// R64
			obj.setR64AdvancesByInstitutionalSector(rs.getString("R64_ADVANCES_BY_INSTITUTIONAL_SECTOR"));
			obj.setR64Overdrafts(rs.getBigDecimal("R64_OVERDRAFTS"));
			obj.setR64OtherInstallmentLoans(rs.getBigDecimal("R64_OTHER_INSTALLMENT_LOANS"));
			obj.setR64Total(rs.getBigDecimal("R64_TOTAL"));

			// COMMON FIELDS
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

	@IdClass(M_LA4_PK.class)
	public static class M_LA4_Archival_Summary_Entity1 {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Id
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

		private Date REPORT_RESUBDATE;

		@Column(name = "R11_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r11AdvancesByInstitutionalSector;

		@Column(name = "R11_OVERDRAFTS")
		private BigDecimal r11Overdrafts;

		@Column(name = "R11_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r11OtherInstallmentLoans;

		@Column(name = "R11_TOTAL")
		private BigDecimal r11Total;

		@Column(name = "R12_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r12AdvancesByInstitutionalSector;

		@Column(name = "R12_OVERDRAFTS")
		private BigDecimal r12Overdrafts;

		@Column(name = "R12_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r12OtherInstallmentLoans;

		@Column(name = "R12_TOTAL")
		private BigDecimal r12Total;

		@Column(name = "R13_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r13AdvancesByInstitutionalSector;

		@Column(name = "R13_OVERDRAFTS")
		private BigDecimal r13Overdrafts;

		@Column(name = "R13_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r13OtherInstallmentLoans;

		@Column(name = "R13_TOTAL")
		private BigDecimal r13Total;

		@Column(name = "R14_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r14AdvancesByInstitutionalSector;

		@Column(name = "R14_OVERDRAFTS")
		private BigDecimal r14Overdrafts;

		@Column(name = "R14_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r14OtherInstallmentLoans;

		@Column(name = "R14_TOTAL")
		private BigDecimal r14Total;

		@Column(name = "R15_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r15AdvancesByInstitutionalSector;

		@Column(name = "R15_OVERDRAFTS")
		private BigDecimal r15Overdrafts;

		@Column(name = "R15_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r15OtherInstallmentLoans;

		@Column(name = "R15_TOTAL")
		private BigDecimal r15Total;

		@Column(name = "R16_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r16AdvancesByInstitutionalSector;

		@Column(name = "R16_OVERDRAFTS")
		private BigDecimal r16Overdrafts;

		@Column(name = "R16_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r16OtherInstallmentLoans;

		@Column(name = "R16_TOTAL")
		private BigDecimal r16Total;

		@Column(name = "R17_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r17AdvancesByInstitutionalSector;

		@Column(name = "R17_OVERDRAFTS")
		private BigDecimal r17Overdrafts;

		@Column(name = "R17_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r17OtherInstallmentLoans;

		@Column(name = "R17_TOTAL")
		private BigDecimal r17Total;

		@Column(name = "R18_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r18AdvancesByInstitutionalSector;

		@Column(name = "R18_OVERDRAFTS")
		private BigDecimal r18Overdrafts;

		@Column(name = "R18_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r18OtherInstallmentLoans;

		@Column(name = "R18_TOTAL")
		private BigDecimal r18Total;

		@Column(name = "R19_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r19AdvancesByInstitutionalSector;

		@Column(name = "R19_OVERDRAFTS")
		private BigDecimal r19Overdrafts;

		@Column(name = "R19_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r19OtherInstallmentLoans;

		@Column(name = "R19_TOTAL")
		private BigDecimal r19Total;

		@Column(name = "R20_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r20AdvancesByInstitutionalSector;

		@Column(name = "R20_OVERDRAFTS")
		private BigDecimal r20Overdrafts;

		@Column(name = "R20_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r20OtherInstallmentLoans;

		@Column(name = "R20_TOTAL")
		private BigDecimal r20Total;

		@Column(name = "R21_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r21AdvancesByInstitutionalSector;

		@Column(name = "R21_OVERDRAFTS")
		private BigDecimal r21Overdrafts;

		@Column(name = "R21_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r21OtherInstallmentLoans;

		@Column(name = "R21_TOTAL")
		private BigDecimal r21Total;

		@Column(name = "R22_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r22AdvancesByInstitutionalSector;

		@Column(name = "R22_OVERDRAFTS")
		private BigDecimal r22Overdrafts;

		@Column(name = "R22_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r22OtherInstallmentLoans;

		@Column(name = "R22_TOTAL")
		private BigDecimal r22Total;

		@Column(name = "R23_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r23AdvancesByInstitutionalSector;

		@Column(name = "R23_OVERDRAFTS")
		private BigDecimal r23Overdrafts;

		@Column(name = "R23_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r23OtherInstallmentLoans;

		@Column(name = "R23_TOTAL")
		private BigDecimal r23Total;

		@Column(name = "R24_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r24AdvancesByInstitutionalSector;

		@Column(name = "R24_OVERDRAFTS")
		private BigDecimal r24Overdrafts;

		@Column(name = "R24_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r24OtherInstallmentLoans;

		@Column(name = "R24_TOTAL")
		private BigDecimal r24Total;

		@Column(name = "R25_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r25AdvancesByInstitutionalSector;

		@Column(name = "R25_OVERDRAFTS")
		private BigDecimal r25Overdrafts;

		@Column(name = "R25_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r25OtherInstallmentLoans;

		@Column(name = "R25_TOTAL")
		private BigDecimal r25Total;

		@Column(name = "R26_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r26AdvancesByInstitutionalSector;

		@Column(name = "R26_OVERDRAFTS")
		private BigDecimal r26Overdrafts;

		@Column(name = "R26_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r26OtherInstallmentLoans;

		@Column(name = "R26_TOTAL")
		private BigDecimal r26Total;

		@Column(name = "R27_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r27AdvancesByInstitutionalSector;

		@Column(name = "R27_OVERDRAFTS")
		private BigDecimal r27Overdrafts;

		@Column(name = "R27_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r27OtherInstallmentLoans;

		@Column(name = "R27_TOTAL")
		private BigDecimal r27Total;

		@Column(name = "R28_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r28AdvancesByInstitutionalSector;

		@Column(name = "R28_OVERDRAFTS")
		private BigDecimal r28Overdrafts;

		@Column(name = "R28_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r28OtherInstallmentLoans;

		@Column(name = "R28_TOTAL")
		private BigDecimal r28Total;

		@Column(name = "R29_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r29AdvancesByInstitutionalSector;

		@Column(name = "R29_OVERDRAFTS")
		private BigDecimal r29Overdrafts;

		@Column(name = "R29_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r29OtherInstallmentLoans;

		@Column(name = "R29_TOTAL")
		private BigDecimal r29Total;

		@Column(name = "R30_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r30AdvancesByInstitutionalSector;

		@Column(name = "R30_OVERDRAFTS")
		private BigDecimal r30Overdrafts;

		@Column(name = "R30_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r30OtherInstallmentLoans;

		@Column(name = "R30_TOTAL")
		private BigDecimal r30Total;

		@Column(name = "R31_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r31AdvancesByInstitutionalSector;

		@Column(name = "R31_OVERDRAFTS")
		private BigDecimal r31Overdrafts;

		@Column(name = "R31_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r31OtherInstallmentLoans;

		@Column(name = "R31_TOTAL")
		private BigDecimal r31Total;

		@Column(name = "R32_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r32AdvancesByInstitutionalSector;

		@Column(name = "R32_OVERDRAFTS")
		private BigDecimal r32Overdrafts;

		@Column(name = "R32_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r32OtherInstallmentLoans;

		@Column(name = "R32_TOTAL")
		private BigDecimal r32Total;

		@Column(name = "R33_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r33AdvancesByInstitutionalSector;

		@Column(name = "R33_OVERDRAFTS")
		private BigDecimal r33Overdrafts;

		@Column(name = "R33_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r33OtherInstallmentLoans;

		@Column(name = "R33_TOTAL")
		private BigDecimal r33Total;

		@Column(name = "R34_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r34AdvancesByInstitutionalSector;

		@Column(name = "R34_OVERDRAFTS")
		private BigDecimal r34Overdrafts;

		@Column(name = "R34_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r34OtherInstallmentLoans;

		@Column(name = "R34_TOTAL")
		private BigDecimal r34Total;

		@Column(name = "R35_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r35AdvancesByInstitutionalSector;

		@Column(name = "R35_OVERDRAFTS")
		private BigDecimal r35Overdrafts;

		@Column(name = "R35_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r35OtherInstallmentLoans;

		@Column(name = "R35_TOTAL")
		private BigDecimal r35Total;

		@Column(name = "R36_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r36AdvancesByInstitutionalSector;

		@Column(name = "R36_OVERDRAFTS")
		private BigDecimal r36Overdrafts;

		@Column(name = "R36_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r36OtherInstallmentLoans;

		@Column(name = "R36_TOTAL")
		private BigDecimal r36Total;

		@Column(name = "R37_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r37AdvancesByInstitutionalSector;

		@Column(name = "R37_OVERDRAFTS")
		private BigDecimal r37Overdrafts;

		@Column(name = "R37_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r37OtherInstallmentLoans;

		@Column(name = "R37_TOTAL")
		private BigDecimal r37Total;

		@Column(name = "R38_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r38AdvancesByInstitutionalSector;

		@Column(name = "R38_OVERDRAFTS")
		private BigDecimal r38Overdrafts;

		@Column(name = "R38_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r38OtherInstallmentLoans;

		@Column(name = "R38_TOTAL")
		private BigDecimal r38Total;

		@Column(name = "R39_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r39AdvancesByInstitutionalSector;

		@Column(name = "R39_OVERDRAFTS")
		private BigDecimal r39Overdrafts;

		@Column(name = "R39_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r39OtherInstallmentLoans;

		@Column(name = "R39_TOTAL")
		private BigDecimal r39Total;

		@Column(name = "R40_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r40AdvancesByInstitutionalSector;

		@Column(name = "R40_OVERDRAFTS")
		private BigDecimal r40Overdrafts;

		@Column(name = "R40_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r40OtherInstallmentLoans;

		@Column(name = "R40_TOTAL")
		private BigDecimal r40Total;

		@Column(name = "R41_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r41AdvancesByInstitutionalSector;

		@Column(name = "R41_OVERDRAFTS")
		private BigDecimal r41Overdrafts;

		@Column(name = "R41_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r41OtherInstallmentLoans;

		@Column(name = "R41_TOTAL")
		private BigDecimal r41Total;

		@Column(name = "R42_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r42AdvancesByInstitutionalSector;

		@Column(name = "R42_OVERDRAFTS")
		private BigDecimal r42Overdrafts;

		@Column(name = "R42_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r42OtherInstallmentLoans;

		@Column(name = "R42_TOTAL")
		private BigDecimal r42Total;

		@Column(name = "R43_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r43AdvancesByInstitutionalSector;

		@Column(name = "R43_OVERDRAFTS")
		private BigDecimal r43Overdrafts;

		@Column(name = "R43_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r43OtherInstallmentLoans;

		@Column(name = "R43_TOTAL")
		private BigDecimal r43Total;

		@Column(name = "R44_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r44AdvancesByInstitutionalSector;

		@Column(name = "R44_OVERDRAFTS")
		private BigDecimal r44Overdrafts;

		@Column(name = "R44_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r44OtherInstallmentLoans;

		@Column(name = "R44_TOTAL")
		private BigDecimal r44Total;

		@Column(name = "R45_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r45AdvancesByInstitutionalSector;

		@Column(name = "R45_OVERDRAFTS")
		private BigDecimal r45Overdrafts;

		@Column(name = "R45_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r45OtherInstallmentLoans;

		@Column(name = "R45_TOTAL")
		private BigDecimal r45Total;

		@Column(name = "R46_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r46AdvancesByInstitutionalSector;

		@Column(name = "R46_OVERDRAFTS")
		private BigDecimal r46Overdrafts;

		@Column(name = "R46_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r46OtherInstallmentLoans;

		@Column(name = "R46_TOTAL")
		private BigDecimal r46Total;

		@Column(name = "R47_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r47AdvancesByInstitutionalSector;

		@Column(name = "R47_OVERDRAFTS")
		private BigDecimal r47Overdrafts;

		@Column(name = "R47_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r47OtherInstallmentLoans;

		@Column(name = "R47_TOTAL")
		private BigDecimal r47Total;

		@Column(name = "R48_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r48AdvancesByInstitutionalSector;

		@Column(name = "R48_OVERDRAFTS")
		private BigDecimal r48Overdrafts;

		@Column(name = "R48_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r48OtherInstallmentLoans;

		@Column(name = "R48_TOTAL")
		private BigDecimal r48Total;

		@Column(name = "R49_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r49AdvancesByInstitutionalSector;

		@Column(name = "R49_OVERDRAFTS")
		private BigDecimal r49Overdrafts;

		@Column(name = "R49_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r49OtherInstallmentLoans;

		@Column(name = "R49_TOTAL")
		private BigDecimal r49Total;

		@Column(name = "R50_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r50AdvancesByInstitutionalSector;

		@Column(name = "R50_OVERDRAFTS")
		private BigDecimal r50Overdrafts;

		@Column(name = "R50_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r50OtherInstallmentLoans;

		@Column(name = "R50_TOTAL")
		private BigDecimal r50Total;

		@Column(name = "R51_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r51AdvancesByInstitutionalSector;

		@Column(name = "R51_OVERDRAFTS")
		private BigDecimal r51Overdrafts;

		@Column(name = "R51_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r51OtherInstallmentLoans;

		@Column(name = "R51_TOTAL")
		private BigDecimal r51Total;

		@Column(name = "R52_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r52AdvancesByInstitutionalSector;

		@Column(name = "R52_OVERDRAFTS")
		private BigDecimal r52Overdrafts;

		@Column(name = "R52_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r52OtherInstallmentLoans;

		@Column(name = "R52_TOTAL")
		private BigDecimal r52Total;

		@Column(name = "R53_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r53AdvancesByInstitutionalSector;

		@Column(name = "R53_OVERDRAFTS")
		private BigDecimal r53Overdrafts;

		@Column(name = "R53_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r53OtherInstallmentLoans;

		@Column(name = "R53_TOTAL")
		private BigDecimal r53Total;

		@Column(name = "R54_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r54AdvancesByInstitutionalSector;

		@Column(name = "R54_OVERDRAFTS")
		private BigDecimal r54Overdrafts;

		@Column(name = "R54_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r54OtherInstallmentLoans;

		@Column(name = "R54_TOTAL")
		private BigDecimal r54Total;

		@Column(name = "R55_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r55AdvancesByInstitutionalSector;

		@Column(name = "R55_OVERDRAFTS")
		private BigDecimal r55Overdrafts;

		@Column(name = "R55_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r55OtherInstallmentLoans;

		@Column(name = "R55_TOTAL")
		private BigDecimal r55Total;

		@Column(name = "R56_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r56AdvancesByInstitutionalSector;

		@Column(name = "R56_OVERDRAFTS")
		private BigDecimal r56Overdrafts;

		@Column(name = "R56_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r56OtherInstallmentLoans;

		@Column(name = "R56_TOTAL")
		private BigDecimal r56Total;

		@Column(name = "R57_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r57AdvancesByInstitutionalSector;

		@Column(name = "R57_OVERDRAFTS")
		private BigDecimal r57Overdrafts;

		@Column(name = "R57_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r57OtherInstallmentLoans;

		@Column(name = "R57_TOTAL")
		private BigDecimal r57Total;

		@Column(name = "R58_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r58AdvancesByInstitutionalSector;

		@Column(name = "R58_OVERDRAFTS")
		private BigDecimal r58Overdrafts;

		@Column(name = "R58_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r58OtherInstallmentLoans;

		@Column(name = "R58_TOTAL")
		private BigDecimal r58Total;

		@Column(name = "R59_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r59AdvancesByInstitutionalSector;

		@Column(name = "R59_OVERDRAFTS")
		private BigDecimal r59Overdrafts;

		@Column(name = "R59_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r59OtherInstallmentLoans;

		@Column(name = "R59_TOTAL")
		private BigDecimal r59Total;

		@Column(name = "R60_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r60AdvancesByInstitutionalSector;

		@Column(name = "R60_OVERDRAFTS")
		private BigDecimal r60Overdrafts;

		@Column(name = "R60_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r60OtherInstallmentLoans;

		@Column(name = "R60_TOTAL")
		private BigDecimal r60Total;

		@Column(name = "R61_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r61AdvancesByInstitutionalSector;

		@Column(name = "R61_OVERDRAFTS")
		private BigDecimal r61Overdrafts;

		@Column(name = "R61_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r61OtherInstallmentLoans;

		@Column(name = "R61_TOTAL")
		private BigDecimal r61Total;

		@Column(name = "R62_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r62AdvancesByInstitutionalSector;

		@Column(name = "R62_OVERDRAFTS")
		private BigDecimal r62Overdrafts;

		@Column(name = "R62_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r62OtherInstallmentLoans;

		@Column(name = "R62_TOTAL")
		private BigDecimal r62Total;

		@Column(name = "R63_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r63AdvancesByInstitutionalSector;

		@Column(name = "R63_OVERDRAFTS")
		private BigDecimal r63Overdrafts;

		@Column(name = "R63_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r63OtherInstallmentLoans;

		@Column(name = "R63_TOTAL")
		private BigDecimal r63Total;

		@Column(name = "R64_ADVANCES_BY_INSTITUTIONAL_SECTOR")
		private String r64AdvancesByInstitutionalSector;

		@Column(name = "R64_OVERDRAFTS")
		private BigDecimal r64Overdrafts;

		@Column(name = "R64_OTHER_INSTALLMENT_LOANS")
		private BigDecimal r64OtherInstallmentLoans;

		@Column(name = "R64_TOTAL")
		private BigDecimal r64Total;

		public String getR11AdvancesByInstitutionalSector() {
			return r11AdvancesByInstitutionalSector;
		}

		public void setR11AdvancesByInstitutionalSector(String r11AdvancesByInstitutionalSector) {
			this.r11AdvancesByInstitutionalSector = r11AdvancesByInstitutionalSector;
		}

		public BigDecimal getR11Overdrafts() {
			return r11Overdrafts;
		}

		public void setR11Overdrafts(BigDecimal r11Overdrafts) {
			this.r11Overdrafts = r11Overdrafts;
		}

		public BigDecimal getR11OtherInstallmentLoans() {
			return r11OtherInstallmentLoans;
		}

		public void setR11OtherInstallmentLoans(BigDecimal r11OtherInstallmentLoans) {
			this.r11OtherInstallmentLoans = r11OtherInstallmentLoans;
		}

		public BigDecimal getR11Total() {
			return r11Total;
		}

		public void setR11Total(BigDecimal r11Total) {
			this.r11Total = r11Total;
		}

		public String getR12AdvancesByInstitutionalSector() {
			return r12AdvancesByInstitutionalSector;
		}

		public void setR12AdvancesByInstitutionalSector(String r12AdvancesByInstitutionalSector) {
			this.r12AdvancesByInstitutionalSector = r12AdvancesByInstitutionalSector;
		}

		public BigDecimal getR12Overdrafts() {
			return r12Overdrafts;
		}

		public void setR12Overdrafts(BigDecimal r12Overdrafts) {
			this.r12Overdrafts = r12Overdrafts;
		}

		public BigDecimal getR12OtherInstallmentLoans() {
			return r12OtherInstallmentLoans;
		}

		public void setR12OtherInstallmentLoans(BigDecimal r12OtherInstallmentLoans) {
			this.r12OtherInstallmentLoans = r12OtherInstallmentLoans;
		}

		public BigDecimal getR12Total() {
			return r12Total;
		}

		public void setR12Total(BigDecimal r12Total) {
			this.r12Total = r12Total;
		}

		public String getR13AdvancesByInstitutionalSector() {
			return r13AdvancesByInstitutionalSector;
		}

		public void setR13AdvancesByInstitutionalSector(String r13AdvancesByInstitutionalSector) {
			this.r13AdvancesByInstitutionalSector = r13AdvancesByInstitutionalSector;
		}

		public BigDecimal getR13Overdrafts() {
			return r13Overdrafts;
		}

		public void setR13Overdrafts(BigDecimal r13Overdrafts) {
			this.r13Overdrafts = r13Overdrafts;
		}

		public BigDecimal getR13OtherInstallmentLoans() {
			return r13OtherInstallmentLoans;
		}

		public void setR13OtherInstallmentLoans(BigDecimal r13OtherInstallmentLoans) {
			this.r13OtherInstallmentLoans = r13OtherInstallmentLoans;
		}

		public BigDecimal getR13Total() {
			return r13Total;
		}

		public void setR13Total(BigDecimal r13Total) {
			this.r13Total = r13Total;
		}

		public String getR14AdvancesByInstitutionalSector() {
			return r14AdvancesByInstitutionalSector;
		}

		public void setR14AdvancesByInstitutionalSector(String r14AdvancesByInstitutionalSector) {
			this.r14AdvancesByInstitutionalSector = r14AdvancesByInstitutionalSector;
		}

		public BigDecimal getR14Overdrafts() {
			return r14Overdrafts;
		}

		public void setR14Overdrafts(BigDecimal r14Overdrafts) {
			this.r14Overdrafts = r14Overdrafts;
		}

		public BigDecimal getR14OtherInstallmentLoans() {
			return r14OtherInstallmentLoans;
		}

		public void setR14OtherInstallmentLoans(BigDecimal r14OtherInstallmentLoans) {
			this.r14OtherInstallmentLoans = r14OtherInstallmentLoans;
		}

		public BigDecimal getR14Total() {
			return r14Total;
		}

		public void setR14Total(BigDecimal r14Total) {
			this.r14Total = r14Total;
		}

		public String getR15AdvancesByInstitutionalSector() {
			return r15AdvancesByInstitutionalSector;
		}

		public void setR15AdvancesByInstitutionalSector(String r15AdvancesByInstitutionalSector) {
			this.r15AdvancesByInstitutionalSector = r15AdvancesByInstitutionalSector;
		}

		public BigDecimal getR15Overdrafts() {
			return r15Overdrafts;
		}

		public void setR15Overdrafts(BigDecimal r15Overdrafts) {
			this.r15Overdrafts = r15Overdrafts;
		}

		public BigDecimal getR15OtherInstallmentLoans() {
			return r15OtherInstallmentLoans;
		}

		public void setR15OtherInstallmentLoans(BigDecimal r15OtherInstallmentLoans) {
			this.r15OtherInstallmentLoans = r15OtherInstallmentLoans;
		}

		public BigDecimal getR15Total() {
			return r15Total;
		}

		public void setR15Total(BigDecimal r15Total) {
			this.r15Total = r15Total;
		}

		public String getR16AdvancesByInstitutionalSector() {
			return r16AdvancesByInstitutionalSector;
		}

		public void setR16AdvancesByInstitutionalSector(String r16AdvancesByInstitutionalSector) {
			this.r16AdvancesByInstitutionalSector = r16AdvancesByInstitutionalSector;
		}

		public BigDecimal getR16Overdrafts() {
			return r16Overdrafts;
		}

		public void setR16Overdrafts(BigDecimal r16Overdrafts) {
			this.r16Overdrafts = r16Overdrafts;
		}

		public BigDecimal getR16OtherInstallmentLoans() {
			return r16OtherInstallmentLoans;
		}

		public void setR16OtherInstallmentLoans(BigDecimal r16OtherInstallmentLoans) {
			this.r16OtherInstallmentLoans = r16OtherInstallmentLoans;
		}

		public BigDecimal getR16Total() {
			return r16Total;
		}

		public void setR16Total(BigDecimal r16Total) {
			this.r16Total = r16Total;
		}

		public String getR17AdvancesByInstitutionalSector() {
			return r17AdvancesByInstitutionalSector;
		}

		public void setR17AdvancesByInstitutionalSector(String r17AdvancesByInstitutionalSector) {
			this.r17AdvancesByInstitutionalSector = r17AdvancesByInstitutionalSector;
		}

		public BigDecimal getR17Overdrafts() {
			return r17Overdrafts;
		}

		public void setR17Overdrafts(BigDecimal r17Overdrafts) {
			this.r17Overdrafts = r17Overdrafts;
		}

		public BigDecimal getR17OtherInstallmentLoans() {
			return r17OtherInstallmentLoans;
		}

		public void setR17OtherInstallmentLoans(BigDecimal r17OtherInstallmentLoans) {
			this.r17OtherInstallmentLoans = r17OtherInstallmentLoans;
		}

		public BigDecimal getR17Total() {
			return r17Total;
		}

		public void setR17Total(BigDecimal r17Total) {
			this.r17Total = r17Total;
		}

		public String getR18AdvancesByInstitutionalSector() {
			return r18AdvancesByInstitutionalSector;
		}

		public void setR18AdvancesByInstitutionalSector(String r18AdvancesByInstitutionalSector) {
			this.r18AdvancesByInstitutionalSector = r18AdvancesByInstitutionalSector;
		}

		public BigDecimal getR18Overdrafts() {
			return r18Overdrafts;
		}

		public void setR18Overdrafts(BigDecimal r18Overdrafts) {
			this.r18Overdrafts = r18Overdrafts;
		}

		public BigDecimal getR18OtherInstallmentLoans() {
			return r18OtherInstallmentLoans;
		}

		public void setR18OtherInstallmentLoans(BigDecimal r18OtherInstallmentLoans) {
			this.r18OtherInstallmentLoans = r18OtherInstallmentLoans;
		}

		public BigDecimal getR18Total() {
			return r18Total;
		}

		public void setR18Total(BigDecimal r18Total) {
			this.r18Total = r18Total;
		}

		public String getR19AdvancesByInstitutionalSector() {
			return r19AdvancesByInstitutionalSector;
		}

		public void setR19AdvancesByInstitutionalSector(String r19AdvancesByInstitutionalSector) {
			this.r19AdvancesByInstitutionalSector = r19AdvancesByInstitutionalSector;
		}

		public BigDecimal getR19Overdrafts() {
			return r19Overdrafts;
		}

		public void setR19Overdrafts(BigDecimal r19Overdrafts) {
			this.r19Overdrafts = r19Overdrafts;
		}

		public BigDecimal getR19OtherInstallmentLoans() {
			return r19OtherInstallmentLoans;
		}

		public void setR19OtherInstallmentLoans(BigDecimal r19OtherInstallmentLoans) {
			this.r19OtherInstallmentLoans = r19OtherInstallmentLoans;
		}

		public BigDecimal getR19Total() {
			return r19Total;
		}

		public void setR19Total(BigDecimal r19Total) {
			this.r19Total = r19Total;
		}

		public String getR20AdvancesByInstitutionalSector() {
			return r20AdvancesByInstitutionalSector;
		}

		public void setR20AdvancesByInstitutionalSector(String r20AdvancesByInstitutionalSector) {
			this.r20AdvancesByInstitutionalSector = r20AdvancesByInstitutionalSector;
		}

		public BigDecimal getR20Overdrafts() {
			return r20Overdrafts;
		}

		public void setR20Overdrafts(BigDecimal r20Overdrafts) {
			this.r20Overdrafts = r20Overdrafts;
		}

		public BigDecimal getR20OtherInstallmentLoans() {
			return r20OtherInstallmentLoans;
		}

		public void setR20OtherInstallmentLoans(BigDecimal r20OtherInstallmentLoans) {
			this.r20OtherInstallmentLoans = r20OtherInstallmentLoans;
		}

		public BigDecimal getR20Total() {
			return r20Total;
		}

		public void setR20Total(BigDecimal r20Total) {
			this.r20Total = r20Total;
		}

		public String getR21AdvancesByInstitutionalSector() {
			return r21AdvancesByInstitutionalSector;
		}

		public void setR21AdvancesByInstitutionalSector(String r21AdvancesByInstitutionalSector) {
			this.r21AdvancesByInstitutionalSector = r21AdvancesByInstitutionalSector;
		}

		public BigDecimal getR21Overdrafts() {
			return r21Overdrafts;
		}

		public void setR21Overdrafts(BigDecimal r21Overdrafts) {
			this.r21Overdrafts = r21Overdrafts;
		}

		public BigDecimal getR21OtherInstallmentLoans() {
			return r21OtherInstallmentLoans;
		}

		public void setR21OtherInstallmentLoans(BigDecimal r21OtherInstallmentLoans) {
			this.r21OtherInstallmentLoans = r21OtherInstallmentLoans;
		}

		public BigDecimal getR21Total() {
			return r21Total;
		}

		public void setR21Total(BigDecimal r21Total) {
			this.r21Total = r21Total;
		}

		public String getR22AdvancesByInstitutionalSector() {
			return r22AdvancesByInstitutionalSector;
		}

		public void setR22AdvancesByInstitutionalSector(String r22AdvancesByInstitutionalSector) {
			this.r22AdvancesByInstitutionalSector = r22AdvancesByInstitutionalSector;
		}

		public BigDecimal getR22Overdrafts() {
			return r22Overdrafts;
		}

		public void setR22Overdrafts(BigDecimal r22Overdrafts) {
			this.r22Overdrafts = r22Overdrafts;
		}

		public BigDecimal getR22OtherInstallmentLoans() {
			return r22OtherInstallmentLoans;
		}

		public void setR22OtherInstallmentLoans(BigDecimal r22OtherInstallmentLoans) {
			this.r22OtherInstallmentLoans = r22OtherInstallmentLoans;
		}

		public BigDecimal getR22Total() {
			return r22Total;
		}

		public void setR22Total(BigDecimal r22Total) {
			this.r22Total = r22Total;
		}

		public String getR23AdvancesByInstitutionalSector() {
			return r23AdvancesByInstitutionalSector;
		}

		public void setR23AdvancesByInstitutionalSector(String r23AdvancesByInstitutionalSector) {
			this.r23AdvancesByInstitutionalSector = r23AdvancesByInstitutionalSector;
		}

		public BigDecimal getR23Overdrafts() {
			return r23Overdrafts;
		}

		public void setR23Overdrafts(BigDecimal r23Overdrafts) {
			this.r23Overdrafts = r23Overdrafts;
		}

		public BigDecimal getR23OtherInstallmentLoans() {
			return r23OtherInstallmentLoans;
		}

		public void setR23OtherInstallmentLoans(BigDecimal r23OtherInstallmentLoans) {
			this.r23OtherInstallmentLoans = r23OtherInstallmentLoans;
		}

		public BigDecimal getR23Total() {
			return r23Total;
		}

		public void setR23Total(BigDecimal r23Total) {
			this.r23Total = r23Total;
		}

		public String getR24AdvancesByInstitutionalSector() {
			return r24AdvancesByInstitutionalSector;
		}

		public void setR24AdvancesByInstitutionalSector(String r24AdvancesByInstitutionalSector) {
			this.r24AdvancesByInstitutionalSector = r24AdvancesByInstitutionalSector;
		}

		public BigDecimal getR24Overdrafts() {
			return r24Overdrafts;
		}

		public void setR24Overdrafts(BigDecimal r24Overdrafts) {
			this.r24Overdrafts = r24Overdrafts;
		}

		public BigDecimal getR24OtherInstallmentLoans() {
			return r24OtherInstallmentLoans;
		}

		public void setR24OtherInstallmentLoans(BigDecimal r24OtherInstallmentLoans) {
			this.r24OtherInstallmentLoans = r24OtherInstallmentLoans;
		}

		public BigDecimal getR24Total() {
			return r24Total;
		}

		public void setR24Total(BigDecimal r24Total) {
			this.r24Total = r24Total;
		}

		public String getR25AdvancesByInstitutionalSector() {
			return r25AdvancesByInstitutionalSector;
		}

		public void setR25AdvancesByInstitutionalSector(String r25AdvancesByInstitutionalSector) {
			this.r25AdvancesByInstitutionalSector = r25AdvancesByInstitutionalSector;
		}

		public BigDecimal getR25Overdrafts() {
			return r25Overdrafts;
		}

		public void setR25Overdrafts(BigDecimal r25Overdrafts) {
			this.r25Overdrafts = r25Overdrafts;
		}

		public BigDecimal getR25OtherInstallmentLoans() {
			return r25OtherInstallmentLoans;
		}

		public void setR25OtherInstallmentLoans(BigDecimal r25OtherInstallmentLoans) {
			this.r25OtherInstallmentLoans = r25OtherInstallmentLoans;
		}

		public BigDecimal getR25Total() {
			return r25Total;
		}

		public void setR25Total(BigDecimal r25Total) {
			this.r25Total = r25Total;
		}

		public String getR26AdvancesByInstitutionalSector() {
			return r26AdvancesByInstitutionalSector;
		}

		public void setR26AdvancesByInstitutionalSector(String r26AdvancesByInstitutionalSector) {
			this.r26AdvancesByInstitutionalSector = r26AdvancesByInstitutionalSector;
		}

		public BigDecimal getR26Overdrafts() {
			return r26Overdrafts;
		}

		public void setR26Overdrafts(BigDecimal r26Overdrafts) {
			this.r26Overdrafts = r26Overdrafts;
		}

		public BigDecimal getR26OtherInstallmentLoans() {
			return r26OtherInstallmentLoans;
		}

		public void setR26OtherInstallmentLoans(BigDecimal r26OtherInstallmentLoans) {
			this.r26OtherInstallmentLoans = r26OtherInstallmentLoans;
		}

		public BigDecimal getR26Total() {
			return r26Total;
		}

		public void setR26Total(BigDecimal r26Total) {
			this.r26Total = r26Total;
		}

		public String getR27AdvancesByInstitutionalSector() {
			return r27AdvancesByInstitutionalSector;
		}

		public void setR27AdvancesByInstitutionalSector(String r27AdvancesByInstitutionalSector) {
			this.r27AdvancesByInstitutionalSector = r27AdvancesByInstitutionalSector;
		}

		public BigDecimal getR27Overdrafts() {
			return r27Overdrafts;
		}

		public void setR27Overdrafts(BigDecimal r27Overdrafts) {
			this.r27Overdrafts = r27Overdrafts;
		}

		public BigDecimal getR27OtherInstallmentLoans() {
			return r27OtherInstallmentLoans;
		}

		public void setR27OtherInstallmentLoans(BigDecimal r27OtherInstallmentLoans) {
			this.r27OtherInstallmentLoans = r27OtherInstallmentLoans;
		}

		public BigDecimal getR27Total() {
			return r27Total;
		}

		public void setR27Total(BigDecimal r27Total) {
			this.r27Total = r27Total;
		}

		public String getR28AdvancesByInstitutionalSector() {
			return r28AdvancesByInstitutionalSector;
		}

		public void setR28AdvancesByInstitutionalSector(String r28AdvancesByInstitutionalSector) {
			this.r28AdvancesByInstitutionalSector = r28AdvancesByInstitutionalSector;
		}

		public BigDecimal getR28Overdrafts() {
			return r28Overdrafts;
		}

		public void setR28Overdrafts(BigDecimal r28Overdrafts) {
			this.r28Overdrafts = r28Overdrafts;
		}

		public BigDecimal getR28OtherInstallmentLoans() {
			return r28OtherInstallmentLoans;
		}

		public void setR28OtherInstallmentLoans(BigDecimal r28OtherInstallmentLoans) {
			this.r28OtherInstallmentLoans = r28OtherInstallmentLoans;
		}

		public BigDecimal getR28Total() {
			return r28Total;
		}

		public void setR28Total(BigDecimal r28Total) {
			this.r28Total = r28Total;
		}

		public String getR29AdvancesByInstitutionalSector() {
			return r29AdvancesByInstitutionalSector;
		}

		public void setR29AdvancesByInstitutionalSector(String r29AdvancesByInstitutionalSector) {
			this.r29AdvancesByInstitutionalSector = r29AdvancesByInstitutionalSector;
		}

		public BigDecimal getR29Overdrafts() {
			return r29Overdrafts;
		}

		public void setR29Overdrafts(BigDecimal r29Overdrafts) {
			this.r29Overdrafts = r29Overdrafts;
		}

		public BigDecimal getR29OtherInstallmentLoans() {
			return r29OtherInstallmentLoans;
		}

		public void setR29OtherInstallmentLoans(BigDecimal r29OtherInstallmentLoans) {
			this.r29OtherInstallmentLoans = r29OtherInstallmentLoans;
		}

		public BigDecimal getR29Total() {
			return r29Total;
		}

		public void setR29Total(BigDecimal r29Total) {
			this.r29Total = r29Total;
		}

		public String getR30AdvancesByInstitutionalSector() {
			return r30AdvancesByInstitutionalSector;
		}

		public void setR30AdvancesByInstitutionalSector(String r30AdvancesByInstitutionalSector) {
			this.r30AdvancesByInstitutionalSector = r30AdvancesByInstitutionalSector;
		}

		public BigDecimal getR30Overdrafts() {
			return r30Overdrafts;
		}

		public void setR30Overdrafts(BigDecimal r30Overdrafts) {
			this.r30Overdrafts = r30Overdrafts;
		}

		public BigDecimal getR30OtherInstallmentLoans() {
			return r30OtherInstallmentLoans;
		}

		public void setR30OtherInstallmentLoans(BigDecimal r30OtherInstallmentLoans) {
			this.r30OtherInstallmentLoans = r30OtherInstallmentLoans;
		}

		public BigDecimal getR30Total() {
			return r30Total;
		}

		public void setR30Total(BigDecimal r30Total) {
			this.r30Total = r30Total;
		}

		public String getR31AdvancesByInstitutionalSector() {
			return r31AdvancesByInstitutionalSector;
		}

		public void setR31AdvancesByInstitutionalSector(String r31AdvancesByInstitutionalSector) {
			this.r31AdvancesByInstitutionalSector = r31AdvancesByInstitutionalSector;
		}

		public BigDecimal getR31Overdrafts() {
			return r31Overdrafts;
		}

		public void setR31Overdrafts(BigDecimal r31Overdrafts) {
			this.r31Overdrafts = r31Overdrafts;
		}

		public BigDecimal getR31OtherInstallmentLoans() {
			return r31OtherInstallmentLoans;
		}

		public void setR31OtherInstallmentLoans(BigDecimal r31OtherInstallmentLoans) {
			this.r31OtherInstallmentLoans = r31OtherInstallmentLoans;
		}

		public BigDecimal getR31Total() {
			return r31Total;
		}

		public void setR31Total(BigDecimal r31Total) {
			this.r31Total = r31Total;
		}

		public String getR32AdvancesByInstitutionalSector() {
			return r32AdvancesByInstitutionalSector;
		}

		public void setR32AdvancesByInstitutionalSector(String r32AdvancesByInstitutionalSector) {
			this.r32AdvancesByInstitutionalSector = r32AdvancesByInstitutionalSector;
		}

		public BigDecimal getR32Overdrafts() {
			return r32Overdrafts;
		}

		public void setR32Overdrafts(BigDecimal r32Overdrafts) {
			this.r32Overdrafts = r32Overdrafts;
		}

		public BigDecimal getR32OtherInstallmentLoans() {
			return r32OtherInstallmentLoans;
		}

		public void setR32OtherInstallmentLoans(BigDecimal r32OtherInstallmentLoans) {
			this.r32OtherInstallmentLoans = r32OtherInstallmentLoans;
		}

		public BigDecimal getR32Total() {
			return r32Total;
		}

		public void setR32Total(BigDecimal r32Total) {
			this.r32Total = r32Total;
		}

		public String getR33AdvancesByInstitutionalSector() {
			return r33AdvancesByInstitutionalSector;
		}

		public void setR33AdvancesByInstitutionalSector(String r33AdvancesByInstitutionalSector) {
			this.r33AdvancesByInstitutionalSector = r33AdvancesByInstitutionalSector;
		}

		public BigDecimal getR33Overdrafts() {
			return r33Overdrafts;
		}

		public void setR33Overdrafts(BigDecimal r33Overdrafts) {
			this.r33Overdrafts = r33Overdrafts;
		}

		public BigDecimal getR33OtherInstallmentLoans() {
			return r33OtherInstallmentLoans;
		}

		public void setR33OtherInstallmentLoans(BigDecimal r33OtherInstallmentLoans) {
			this.r33OtherInstallmentLoans = r33OtherInstallmentLoans;
		}

		public BigDecimal getR33Total() {
			return r33Total;
		}

		public void setR33Total(BigDecimal r33Total) {
			this.r33Total = r33Total;
		}

		public String getR34AdvancesByInstitutionalSector() {
			return r34AdvancesByInstitutionalSector;
		}

		public void setR34AdvancesByInstitutionalSector(String r34AdvancesByInstitutionalSector) {
			this.r34AdvancesByInstitutionalSector = r34AdvancesByInstitutionalSector;
		}

		public BigDecimal getR34Overdrafts() {
			return r34Overdrafts;
		}

		public void setR34Overdrafts(BigDecimal r34Overdrafts) {
			this.r34Overdrafts = r34Overdrafts;
		}

		public BigDecimal getR34OtherInstallmentLoans() {
			return r34OtherInstallmentLoans;
		}

		public void setR34OtherInstallmentLoans(BigDecimal r34OtherInstallmentLoans) {
			this.r34OtherInstallmentLoans = r34OtherInstallmentLoans;
		}

		public BigDecimal getR34Total() {
			return r34Total;
		}

		public void setR34Total(BigDecimal r34Total) {
			this.r34Total = r34Total;
		}

		public String getR35AdvancesByInstitutionalSector() {
			return r35AdvancesByInstitutionalSector;
		}

		public void setR35AdvancesByInstitutionalSector(String r35AdvancesByInstitutionalSector) {
			this.r35AdvancesByInstitutionalSector = r35AdvancesByInstitutionalSector;
		}

		public BigDecimal getR35Overdrafts() {
			return r35Overdrafts;
		}

		public void setR35Overdrafts(BigDecimal r35Overdrafts) {
			this.r35Overdrafts = r35Overdrafts;
		}

		public BigDecimal getR35OtherInstallmentLoans() {
			return r35OtherInstallmentLoans;
		}

		public void setR35OtherInstallmentLoans(BigDecimal r35OtherInstallmentLoans) {
			this.r35OtherInstallmentLoans = r35OtherInstallmentLoans;
		}

		public BigDecimal getR35Total() {
			return r35Total;
		}

		public void setR35Total(BigDecimal r35Total) {
			this.r35Total = r35Total;
		}

		public String getR36AdvancesByInstitutionalSector() {
			return r36AdvancesByInstitutionalSector;
		}

		public void setR36AdvancesByInstitutionalSector(String r36AdvancesByInstitutionalSector) {
			this.r36AdvancesByInstitutionalSector = r36AdvancesByInstitutionalSector;
		}

		public BigDecimal getR36Overdrafts() {
			return r36Overdrafts;
		}

		public void setR36Overdrafts(BigDecimal r36Overdrafts) {
			this.r36Overdrafts = r36Overdrafts;
		}

		public BigDecimal getR36OtherInstallmentLoans() {
			return r36OtherInstallmentLoans;
		}

		public void setR36OtherInstallmentLoans(BigDecimal r36OtherInstallmentLoans) {
			this.r36OtherInstallmentLoans = r36OtherInstallmentLoans;
		}

		public BigDecimal getR36Total() {
			return r36Total;
		}

		public void setR36Total(BigDecimal r36Total) {
			this.r36Total = r36Total;
		}

		public String getR37AdvancesByInstitutionalSector() {
			return r37AdvancesByInstitutionalSector;
		}

		public void setR37AdvancesByInstitutionalSector(String r37AdvancesByInstitutionalSector) {
			this.r37AdvancesByInstitutionalSector = r37AdvancesByInstitutionalSector;
		}

		public BigDecimal getR37Overdrafts() {
			return r37Overdrafts;
		}

		public void setR37Overdrafts(BigDecimal r37Overdrafts) {
			this.r37Overdrafts = r37Overdrafts;
		}

		public BigDecimal getR37OtherInstallmentLoans() {
			return r37OtherInstallmentLoans;
		}

		public void setR37OtherInstallmentLoans(BigDecimal r37OtherInstallmentLoans) {
			this.r37OtherInstallmentLoans = r37OtherInstallmentLoans;
		}

		public BigDecimal getR37Total() {
			return r37Total;
		}

		public void setR37Total(BigDecimal r37Total) {
			this.r37Total = r37Total;
		}

		public String getR38AdvancesByInstitutionalSector() {
			return r38AdvancesByInstitutionalSector;
		}

		public void setR38AdvancesByInstitutionalSector(String r38AdvancesByInstitutionalSector) {
			this.r38AdvancesByInstitutionalSector = r38AdvancesByInstitutionalSector;
		}

		public BigDecimal getR38Overdrafts() {
			return r38Overdrafts;
		}

		public void setR38Overdrafts(BigDecimal r38Overdrafts) {
			this.r38Overdrafts = r38Overdrafts;
		}

		public BigDecimal getR38OtherInstallmentLoans() {
			return r38OtherInstallmentLoans;
		}

		public void setR38OtherInstallmentLoans(BigDecimal r38OtherInstallmentLoans) {
			this.r38OtherInstallmentLoans = r38OtherInstallmentLoans;
		}

		public BigDecimal getR38Total() {
			return r38Total;
		}

		public void setR38Total(BigDecimal r38Total) {
			this.r38Total = r38Total;
		}

		public String getR39AdvancesByInstitutionalSector() {
			return r39AdvancesByInstitutionalSector;
		}

		public void setR39AdvancesByInstitutionalSector(String r39AdvancesByInstitutionalSector) {
			this.r39AdvancesByInstitutionalSector = r39AdvancesByInstitutionalSector;
		}

		public BigDecimal getR39Overdrafts() {
			return r39Overdrafts;
		}

		public void setR39Overdrafts(BigDecimal r39Overdrafts) {
			this.r39Overdrafts = r39Overdrafts;
		}

		public BigDecimal getR39OtherInstallmentLoans() {
			return r39OtherInstallmentLoans;
		}

		public void setR39OtherInstallmentLoans(BigDecimal r39OtherInstallmentLoans) {
			this.r39OtherInstallmentLoans = r39OtherInstallmentLoans;
		}

		public BigDecimal getR39Total() {
			return r39Total;
		}

		public void setR39Total(BigDecimal r39Total) {
			this.r39Total = r39Total;
		}

		public String getR40AdvancesByInstitutionalSector() {
			return r40AdvancesByInstitutionalSector;
		}

		public void setR40AdvancesByInstitutionalSector(String r40AdvancesByInstitutionalSector) {
			this.r40AdvancesByInstitutionalSector = r40AdvancesByInstitutionalSector;
		}

		public BigDecimal getR40Overdrafts() {
			return r40Overdrafts;
		}

		public void setR40Overdrafts(BigDecimal r40Overdrafts) {
			this.r40Overdrafts = r40Overdrafts;
		}

		public BigDecimal getR40OtherInstallmentLoans() {
			return r40OtherInstallmentLoans;
		}

		public void setR40OtherInstallmentLoans(BigDecimal r40OtherInstallmentLoans) {
			this.r40OtherInstallmentLoans = r40OtherInstallmentLoans;
		}

		public BigDecimal getR40Total() {
			return r40Total;
		}

		public void setR40Total(BigDecimal r40Total) {
			this.r40Total = r40Total;
		}

		public String getR41AdvancesByInstitutionalSector() {
			return r41AdvancesByInstitutionalSector;
		}

		public void setR41AdvancesByInstitutionalSector(String r41AdvancesByInstitutionalSector) {
			this.r41AdvancesByInstitutionalSector = r41AdvancesByInstitutionalSector;
		}

		public BigDecimal getR41Overdrafts() {
			return r41Overdrafts;
		}

		public void setR41Overdrafts(BigDecimal r41Overdrafts) {
			this.r41Overdrafts = r41Overdrafts;
		}

		public BigDecimal getR41OtherInstallmentLoans() {
			return r41OtherInstallmentLoans;
		}

		public void setR41OtherInstallmentLoans(BigDecimal r41OtherInstallmentLoans) {
			this.r41OtherInstallmentLoans = r41OtherInstallmentLoans;
		}

		public BigDecimal getR41Total() {
			return r41Total;
		}

		public void setR41Total(BigDecimal r41Total) {
			this.r41Total = r41Total;
		}

		public String getR42AdvancesByInstitutionalSector() {
			return r42AdvancesByInstitutionalSector;
		}

		public void setR42AdvancesByInstitutionalSector(String r42AdvancesByInstitutionalSector) {
			this.r42AdvancesByInstitutionalSector = r42AdvancesByInstitutionalSector;
		}

		public BigDecimal getR42Overdrafts() {
			return r42Overdrafts;
		}

		public void setR42Overdrafts(BigDecimal r42Overdrafts) {
			this.r42Overdrafts = r42Overdrafts;
		}

		public BigDecimal getR42OtherInstallmentLoans() {
			return r42OtherInstallmentLoans;
		}

		public void setR42OtherInstallmentLoans(BigDecimal r42OtherInstallmentLoans) {
			this.r42OtherInstallmentLoans = r42OtherInstallmentLoans;
		}

		public BigDecimal getR42Total() {
			return r42Total;
		}

		public void setR42Total(BigDecimal r42Total) {
			this.r42Total = r42Total;
		}

		public String getR43AdvancesByInstitutionalSector() {
			return r43AdvancesByInstitutionalSector;
		}

		public void setR43AdvancesByInstitutionalSector(String r43AdvancesByInstitutionalSector) {
			this.r43AdvancesByInstitutionalSector = r43AdvancesByInstitutionalSector;
		}

		public BigDecimal getR43Overdrafts() {
			return r43Overdrafts;
		}

		public void setR43Overdrafts(BigDecimal r43Overdrafts) {
			this.r43Overdrafts = r43Overdrafts;
		}

		public BigDecimal getR43OtherInstallmentLoans() {
			return r43OtherInstallmentLoans;
		}

		public void setR43OtherInstallmentLoans(BigDecimal r43OtherInstallmentLoans) {
			this.r43OtherInstallmentLoans = r43OtherInstallmentLoans;
		}

		public BigDecimal getR43Total() {
			return r43Total;
		}

		public void setR43Total(BigDecimal r43Total) {
			this.r43Total = r43Total;
		}

		public String getR44AdvancesByInstitutionalSector() {
			return r44AdvancesByInstitutionalSector;
		}

		public void setR44AdvancesByInstitutionalSector(String r44AdvancesByInstitutionalSector) {
			this.r44AdvancesByInstitutionalSector = r44AdvancesByInstitutionalSector;
		}

		public BigDecimal getR44Overdrafts() {
			return r44Overdrafts;
		}

		public void setR44Overdrafts(BigDecimal r44Overdrafts) {
			this.r44Overdrafts = r44Overdrafts;
		}

		public BigDecimal getR44OtherInstallmentLoans() {
			return r44OtherInstallmentLoans;
		}

		public void setR44OtherInstallmentLoans(BigDecimal r44OtherInstallmentLoans) {
			this.r44OtherInstallmentLoans = r44OtherInstallmentLoans;
		}

		public BigDecimal getR44Total() {
			return r44Total;
		}

		public void setR44Total(BigDecimal r44Total) {
			this.r44Total = r44Total;
		}

		public String getR45AdvancesByInstitutionalSector() {
			return r45AdvancesByInstitutionalSector;
		}

		public void setR45AdvancesByInstitutionalSector(String r45AdvancesByInstitutionalSector) {
			this.r45AdvancesByInstitutionalSector = r45AdvancesByInstitutionalSector;
		}

		public BigDecimal getR45Overdrafts() {
			return r45Overdrafts;
		}

		public void setR45Overdrafts(BigDecimal r45Overdrafts) {
			this.r45Overdrafts = r45Overdrafts;
		}

		public BigDecimal getR45OtherInstallmentLoans() {
			return r45OtherInstallmentLoans;
		}

		public void setR45OtherInstallmentLoans(BigDecimal r45OtherInstallmentLoans) {
			this.r45OtherInstallmentLoans = r45OtherInstallmentLoans;
		}

		public BigDecimal getR45Total() {
			return r45Total;
		}

		public void setR45Total(BigDecimal r45Total) {
			this.r45Total = r45Total;
		}

		public String getR46AdvancesByInstitutionalSector() {
			return r46AdvancesByInstitutionalSector;
		}

		public void setR46AdvancesByInstitutionalSector(String r46AdvancesByInstitutionalSector) {
			this.r46AdvancesByInstitutionalSector = r46AdvancesByInstitutionalSector;
		}

		public BigDecimal getR46Overdrafts() {
			return r46Overdrafts;
		}

		public void setR46Overdrafts(BigDecimal r46Overdrafts) {
			this.r46Overdrafts = r46Overdrafts;
		}

		public BigDecimal getR46OtherInstallmentLoans() {
			return r46OtherInstallmentLoans;
		}

		public void setR46OtherInstallmentLoans(BigDecimal r46OtherInstallmentLoans) {
			this.r46OtherInstallmentLoans = r46OtherInstallmentLoans;
		}

		public BigDecimal getR46Total() {
			return r46Total;
		}

		public void setR46Total(BigDecimal r46Total) {
			this.r46Total = r46Total;
		}

		public String getR47AdvancesByInstitutionalSector() {
			return r47AdvancesByInstitutionalSector;
		}

		public void setR47AdvancesByInstitutionalSector(String r47AdvancesByInstitutionalSector) {
			this.r47AdvancesByInstitutionalSector = r47AdvancesByInstitutionalSector;
		}

		public BigDecimal getR47Overdrafts() {
			return r47Overdrafts;
		}

		public void setR47Overdrafts(BigDecimal r47Overdrafts) {
			this.r47Overdrafts = r47Overdrafts;
		}

		public BigDecimal getR47OtherInstallmentLoans() {
			return r47OtherInstallmentLoans;
		}

		public void setR47OtherInstallmentLoans(BigDecimal r47OtherInstallmentLoans) {
			this.r47OtherInstallmentLoans = r47OtherInstallmentLoans;
		}

		public BigDecimal getR47Total() {
			return r47Total;
		}

		public void setR47Total(BigDecimal r47Total) {
			this.r47Total = r47Total;
		}

		public String getR48AdvancesByInstitutionalSector() {
			return r48AdvancesByInstitutionalSector;
		}

		public void setR48AdvancesByInstitutionalSector(String r48AdvancesByInstitutionalSector) {
			this.r48AdvancesByInstitutionalSector = r48AdvancesByInstitutionalSector;
		}

		public BigDecimal getR48Overdrafts() {
			return r48Overdrafts;
		}

		public void setR48Overdrafts(BigDecimal r48Overdrafts) {
			this.r48Overdrafts = r48Overdrafts;
		}

		public BigDecimal getR48OtherInstallmentLoans() {
			return r48OtherInstallmentLoans;
		}

		public void setR48OtherInstallmentLoans(BigDecimal r48OtherInstallmentLoans) {
			this.r48OtherInstallmentLoans = r48OtherInstallmentLoans;
		}

		public BigDecimal getR48Total() {
			return r48Total;
		}

		public void setR48Total(BigDecimal r48Total) {
			this.r48Total = r48Total;
		}

		public String getR49AdvancesByInstitutionalSector() {
			return r49AdvancesByInstitutionalSector;
		}

		public void setR49AdvancesByInstitutionalSector(String r49AdvancesByInstitutionalSector) {
			this.r49AdvancesByInstitutionalSector = r49AdvancesByInstitutionalSector;
		}

		public BigDecimal getR49Overdrafts() {
			return r49Overdrafts;
		}

		public void setR49Overdrafts(BigDecimal r49Overdrafts) {
			this.r49Overdrafts = r49Overdrafts;
		}

		public BigDecimal getR49OtherInstallmentLoans() {
			return r49OtherInstallmentLoans;
		}

		public void setR49OtherInstallmentLoans(BigDecimal r49OtherInstallmentLoans) {
			this.r49OtherInstallmentLoans = r49OtherInstallmentLoans;
		}

		public BigDecimal getR49Total() {
			return r49Total;
		}

		public void setR49Total(BigDecimal r49Total) {
			this.r49Total = r49Total;
		}

		public String getR50AdvancesByInstitutionalSector() {
			return r50AdvancesByInstitutionalSector;
		}

		public void setR50AdvancesByInstitutionalSector(String r50AdvancesByInstitutionalSector) {
			this.r50AdvancesByInstitutionalSector = r50AdvancesByInstitutionalSector;
		}

		public BigDecimal getR50Overdrafts() {
			return r50Overdrafts;
		}

		public void setR50Overdrafts(BigDecimal r50Overdrafts) {
			this.r50Overdrafts = r50Overdrafts;
		}

		public BigDecimal getR50OtherInstallmentLoans() {
			return r50OtherInstallmentLoans;
		}

		public void setR50OtherInstallmentLoans(BigDecimal r50OtherInstallmentLoans) {
			this.r50OtherInstallmentLoans = r50OtherInstallmentLoans;
		}

		public BigDecimal getR50Total() {
			return r50Total;
		}

		public void setR50Total(BigDecimal r50Total) {
			this.r50Total = r50Total;
		}

		public String getR51AdvancesByInstitutionalSector() {
			return r51AdvancesByInstitutionalSector;
		}

		public void setR51AdvancesByInstitutionalSector(String r51AdvancesByInstitutionalSector) {
			this.r51AdvancesByInstitutionalSector = r51AdvancesByInstitutionalSector;
		}

		public BigDecimal getR51Overdrafts() {
			return r51Overdrafts;
		}

		public void setR51Overdrafts(BigDecimal r51Overdrafts) {
			this.r51Overdrafts = r51Overdrafts;
		}

		public BigDecimal getR51OtherInstallmentLoans() {
			return r51OtherInstallmentLoans;
		}

		public void setR51OtherInstallmentLoans(BigDecimal r51OtherInstallmentLoans) {
			this.r51OtherInstallmentLoans = r51OtherInstallmentLoans;
		}

		public BigDecimal getR51Total() {
			return r51Total;
		}

		public void setR51Total(BigDecimal r51Total) {
			this.r51Total = r51Total;
		}

		public String getR52AdvancesByInstitutionalSector() {
			return r52AdvancesByInstitutionalSector;
		}

		public void setR52AdvancesByInstitutionalSector(String r52AdvancesByInstitutionalSector) {
			this.r52AdvancesByInstitutionalSector = r52AdvancesByInstitutionalSector;
		}

		public BigDecimal getR52Overdrafts() {
			return r52Overdrafts;
		}

		public void setR52Overdrafts(BigDecimal r52Overdrafts) {
			this.r52Overdrafts = r52Overdrafts;
		}

		public BigDecimal getR52OtherInstallmentLoans() {
			return r52OtherInstallmentLoans;
		}

		public void setR52OtherInstallmentLoans(BigDecimal r52OtherInstallmentLoans) {
			this.r52OtherInstallmentLoans = r52OtherInstallmentLoans;
		}

		public BigDecimal getR52Total() {
			return r52Total;
		}

		public void setR52Total(BigDecimal r52Total) {
			this.r52Total = r52Total;
		}

		public String getR53AdvancesByInstitutionalSector() {
			return r53AdvancesByInstitutionalSector;
		}

		public void setR53AdvancesByInstitutionalSector(String r53AdvancesByInstitutionalSector) {
			this.r53AdvancesByInstitutionalSector = r53AdvancesByInstitutionalSector;
		}

		public BigDecimal getR53Overdrafts() {
			return r53Overdrafts;
		}

		public void setR53Overdrafts(BigDecimal r53Overdrafts) {
			this.r53Overdrafts = r53Overdrafts;
		}

		public BigDecimal getR53OtherInstallmentLoans() {
			return r53OtherInstallmentLoans;
		}

		public void setR53OtherInstallmentLoans(BigDecimal r53OtherInstallmentLoans) {
			this.r53OtherInstallmentLoans = r53OtherInstallmentLoans;
		}

		public BigDecimal getR53Total() {
			return r53Total;
		}

		public void setR53Total(BigDecimal r53Total) {
			this.r53Total = r53Total;
		}

		public String getR54AdvancesByInstitutionalSector() {
			return r54AdvancesByInstitutionalSector;
		}

		public void setR54AdvancesByInstitutionalSector(String r54AdvancesByInstitutionalSector) {
			this.r54AdvancesByInstitutionalSector = r54AdvancesByInstitutionalSector;
		}

		public BigDecimal getR54Overdrafts() {
			return r54Overdrafts;
		}

		public void setR54Overdrafts(BigDecimal r54Overdrafts) {
			this.r54Overdrafts = r54Overdrafts;
		}

		public BigDecimal getR54OtherInstallmentLoans() {
			return r54OtherInstallmentLoans;
		}

		public void setR54OtherInstallmentLoans(BigDecimal r54OtherInstallmentLoans) {
			this.r54OtherInstallmentLoans = r54OtherInstallmentLoans;
		}

		public BigDecimal getR54Total() {
			return r54Total;
		}

		public void setR54Total(BigDecimal r54Total) {
			this.r54Total = r54Total;
		}

		public String getR55AdvancesByInstitutionalSector() {
			return r55AdvancesByInstitutionalSector;
		}

		public void setR55AdvancesByInstitutionalSector(String r55AdvancesByInstitutionalSector) {
			this.r55AdvancesByInstitutionalSector = r55AdvancesByInstitutionalSector;
		}

		public BigDecimal getR55Overdrafts() {
			return r55Overdrafts;
		}

		public void setR55Overdrafts(BigDecimal r55Overdrafts) {
			this.r55Overdrafts = r55Overdrafts;
		}

		public BigDecimal getR55OtherInstallmentLoans() {
			return r55OtherInstallmentLoans;
		}

		public void setR55OtherInstallmentLoans(BigDecimal r55OtherInstallmentLoans) {
			this.r55OtherInstallmentLoans = r55OtherInstallmentLoans;
		}

		public BigDecimal getR55Total() {
			return r55Total;
		}

		public void setR55Total(BigDecimal r55Total) {
			this.r55Total = r55Total;
		}

		public String getR56AdvancesByInstitutionalSector() {
			return r56AdvancesByInstitutionalSector;
		}

		public void setR56AdvancesByInstitutionalSector(String r56AdvancesByInstitutionalSector) {
			this.r56AdvancesByInstitutionalSector = r56AdvancesByInstitutionalSector;
		}

		public BigDecimal getR56Overdrafts() {
			return r56Overdrafts;
		}

		public void setR56Overdrafts(BigDecimal r56Overdrafts) {
			this.r56Overdrafts = r56Overdrafts;
		}

		public BigDecimal getR56OtherInstallmentLoans() {
			return r56OtherInstallmentLoans;
		}

		public void setR56OtherInstallmentLoans(BigDecimal r56OtherInstallmentLoans) {
			this.r56OtherInstallmentLoans = r56OtherInstallmentLoans;
		}

		public BigDecimal getR56Total() {
			return r56Total;
		}

		public void setR56Total(BigDecimal r56Total) {
			this.r56Total = r56Total;
		}

		public String getR57AdvancesByInstitutionalSector() {
			return r57AdvancesByInstitutionalSector;
		}

		public void setR57AdvancesByInstitutionalSector(String r57AdvancesByInstitutionalSector) {
			this.r57AdvancesByInstitutionalSector = r57AdvancesByInstitutionalSector;
		}

		public BigDecimal getR57Overdrafts() {
			return r57Overdrafts;
		}

		public void setR57Overdrafts(BigDecimal r57Overdrafts) {
			this.r57Overdrafts = r57Overdrafts;
		}

		public BigDecimal getR57OtherInstallmentLoans() {
			return r57OtherInstallmentLoans;
		}

		public void setR57OtherInstallmentLoans(BigDecimal r57OtherInstallmentLoans) {
			this.r57OtherInstallmentLoans = r57OtherInstallmentLoans;
		}

		public BigDecimal getR57Total() {
			return r57Total;
		}

		public void setR57Total(BigDecimal r57Total) {
			this.r57Total = r57Total;
		}

		public String getR58AdvancesByInstitutionalSector() {
			return r58AdvancesByInstitutionalSector;
		}

		public void setR58AdvancesByInstitutionalSector(String r58AdvancesByInstitutionalSector) {
			this.r58AdvancesByInstitutionalSector = r58AdvancesByInstitutionalSector;
		}

		public BigDecimal getR58Overdrafts() {
			return r58Overdrafts;
		}

		public void setR58Overdrafts(BigDecimal r58Overdrafts) {
			this.r58Overdrafts = r58Overdrafts;
		}

		public BigDecimal getR58OtherInstallmentLoans() {
			return r58OtherInstallmentLoans;
		}

		public void setR58OtherInstallmentLoans(BigDecimal r58OtherInstallmentLoans) {
			this.r58OtherInstallmentLoans = r58OtherInstallmentLoans;
		}

		public BigDecimal getR58Total() {
			return r58Total;
		}

		public void setR58Total(BigDecimal r58Total) {
			this.r58Total = r58Total;
		}

		public String getR59AdvancesByInstitutionalSector() {
			return r59AdvancesByInstitutionalSector;
		}

		public void setR59AdvancesByInstitutionalSector(String r59AdvancesByInstitutionalSector) {
			this.r59AdvancesByInstitutionalSector = r59AdvancesByInstitutionalSector;
		}

		public BigDecimal getR59Overdrafts() {
			return r59Overdrafts;
		}

		public void setR59Overdrafts(BigDecimal r59Overdrafts) {
			this.r59Overdrafts = r59Overdrafts;
		}

		public BigDecimal getR59OtherInstallmentLoans() {
			return r59OtherInstallmentLoans;
		}

		public void setR59OtherInstallmentLoans(BigDecimal r59OtherInstallmentLoans) {
			this.r59OtherInstallmentLoans = r59OtherInstallmentLoans;
		}

		public BigDecimal getR59Total() {
			return r59Total;
		}

		public void setR59Total(BigDecimal r59Total) {
			this.r59Total = r59Total;
		}

		public String getR60AdvancesByInstitutionalSector() {
			return r60AdvancesByInstitutionalSector;
		}

		public void setR60AdvancesByInstitutionalSector(String r60AdvancesByInstitutionalSector) {
			this.r60AdvancesByInstitutionalSector = r60AdvancesByInstitutionalSector;
		}

		public BigDecimal getR60Overdrafts() {
			return r60Overdrafts;
		}

		public void setR60Overdrafts(BigDecimal r60Overdrafts) {
			this.r60Overdrafts = r60Overdrafts;
		}

		public BigDecimal getR60OtherInstallmentLoans() {
			return r60OtherInstallmentLoans;
		}

		public void setR60OtherInstallmentLoans(BigDecimal r60OtherInstallmentLoans) {
			this.r60OtherInstallmentLoans = r60OtherInstallmentLoans;
		}

		public BigDecimal getR60Total() {
			return r60Total;
		}

		public void setR60Total(BigDecimal r60Total) {
			this.r60Total = r60Total;
		}

		public String getR61AdvancesByInstitutionalSector() {
			return r61AdvancesByInstitutionalSector;
		}

		public void setR61AdvancesByInstitutionalSector(String r61AdvancesByInstitutionalSector) {
			this.r61AdvancesByInstitutionalSector = r61AdvancesByInstitutionalSector;
		}

		public BigDecimal getR61Overdrafts() {
			return r61Overdrafts;
		}

		public void setR61Overdrafts(BigDecimal r61Overdrafts) {
			this.r61Overdrafts = r61Overdrafts;
		}

		public BigDecimal getR61OtherInstallmentLoans() {
			return r61OtherInstallmentLoans;
		}

		public void setR61OtherInstallmentLoans(BigDecimal r61OtherInstallmentLoans) {
			this.r61OtherInstallmentLoans = r61OtherInstallmentLoans;
		}

		public BigDecimal getR61Total() {
			return r61Total;
		}

		public void setR61Total(BigDecimal r61Total) {
			this.r61Total = r61Total;
		}

		public String getR62AdvancesByInstitutionalSector() {
			return r62AdvancesByInstitutionalSector;
		}

		public void setR62AdvancesByInstitutionalSector(String r62AdvancesByInstitutionalSector) {
			this.r62AdvancesByInstitutionalSector = r62AdvancesByInstitutionalSector;
		}

		public BigDecimal getR62Overdrafts() {
			return r62Overdrafts;
		}

		public void setR62Overdrafts(BigDecimal r62Overdrafts) {
			this.r62Overdrafts = r62Overdrafts;
		}

		public BigDecimal getR62OtherInstallmentLoans() {
			return r62OtherInstallmentLoans;
		}

		public void setR62OtherInstallmentLoans(BigDecimal r62OtherInstallmentLoans) {
			this.r62OtherInstallmentLoans = r62OtherInstallmentLoans;
		}

		public BigDecimal getR62Total() {
			return r62Total;
		}

		public void setR62Total(BigDecimal r62Total) {
			this.r62Total = r62Total;
		}

		public String getR63AdvancesByInstitutionalSector() {
			return r63AdvancesByInstitutionalSector;
		}

		public void setR63AdvancesByInstitutionalSector(String r63AdvancesByInstitutionalSector) {
			this.r63AdvancesByInstitutionalSector = r63AdvancesByInstitutionalSector;
		}

		public BigDecimal getR63Overdrafts() {
			return r63Overdrafts;
		}

		public void setR63Overdrafts(BigDecimal r63Overdrafts) {
			this.r63Overdrafts = r63Overdrafts;
		}

		public BigDecimal getR63OtherInstallmentLoans() {
			return r63OtherInstallmentLoans;
		}

		public void setR63OtherInstallmentLoans(BigDecimal r63OtherInstallmentLoans) {
			this.r63OtherInstallmentLoans = r63OtherInstallmentLoans;
		}

		public BigDecimal getR63Total() {
			return r63Total;
		}

		public void setR63Total(BigDecimal r63Total) {
			this.r63Total = r63Total;
		}

		public String getR64AdvancesByInstitutionalSector() {
			return r64AdvancesByInstitutionalSector;
		}

		public void setR64AdvancesByInstitutionalSector(String r64AdvancesByInstitutionalSector) {
			this.r64AdvancesByInstitutionalSector = r64AdvancesByInstitutionalSector;
		}

		public BigDecimal getR64Overdrafts() {
			return r64Overdrafts;
		}

		public void setR64Overdrafts(BigDecimal r64Overdrafts) {
			this.r64Overdrafts = r64Overdrafts;
		}

		public BigDecimal getR64OtherInstallmentLoans() {
			return r64OtherInstallmentLoans;
		}

		public void setR64OtherInstallmentLoans(BigDecimal r64OtherInstallmentLoans) {
			this.r64OtherInstallmentLoans = r64OtherInstallmentLoans;
		}

		public BigDecimal getR64Total() {
			return r64Total;
		}

		public void setR64Total(BigDecimal r64Total) {
			this.r64Total = r64Total;
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

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
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

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}
	}

	class M_LA4ArchivalRowMapper2 implements RowMapper<M_LA4_Archival_Summary_Entity2> {

		@Override
		public M_LA4_Archival_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA4_Archival_Summary_Entity2 obj = new M_LA4_Archival_Summary_Entity2();

// R11
			obj.setR11FactoringDebtors(rs.getBigDecimal("R11_FACTORING_DEBTORS"));
			obj.setR11Leasing(rs.getBigDecimal("R11_LEASING"));

			// R12
			obj.setR12FactoringDebtors(rs.getBigDecimal("R12_FACTORING_DEBTORS"));
			obj.setR12Leasing(rs.getBigDecimal("R12_LEASING"));

			// R13
			obj.setR13FactoringDebtors(rs.getBigDecimal("R13_FACTORING_DEBTORS"));
			obj.setR13Leasing(rs.getBigDecimal("R13_LEASING"));

			// R14
			obj.setR14FactoringDebtors(rs.getBigDecimal("R14_FACTORING_DEBTORS"));
			obj.setR14Leasing(rs.getBigDecimal("R14_LEASING"));

			// R15
			obj.setR15FactoringDebtors(rs.getBigDecimal("R15_FACTORING_DEBTORS"));
			obj.setR15Leasing(rs.getBigDecimal("R15_LEASING"));

			// R16
			obj.setR16FactoringDebtors(rs.getBigDecimal("R16_FACTORING_DEBTORS"));
			obj.setR16Leasing(rs.getBigDecimal("R16_LEASING"));

			// R17
			obj.setR17FactoringDebtors(rs.getBigDecimal("R17_FACTORING_DEBTORS"));
			obj.setR17Leasing(rs.getBigDecimal("R17_LEASING"));

			// R18
			obj.setR18FactoringDebtors(rs.getBigDecimal("R18_FACTORING_DEBTORS"));
			obj.setR18Leasing(rs.getBigDecimal("R18_LEASING"));

			// R19
			obj.setR19FactoringDebtors(rs.getBigDecimal("R19_FACTORING_DEBTORS"));
			obj.setR19Leasing(rs.getBigDecimal("R19_LEASING"));

			// R20
			obj.setR20FactoringDebtors(rs.getBigDecimal("R20_FACTORING_DEBTORS"));
			obj.setR20Leasing(rs.getBigDecimal("R20_LEASING"));

			// R21
			obj.setR21FactoringDebtors(rs.getBigDecimal("R21_FACTORING_DEBTORS"));
			obj.setR21Leasing(rs.getBigDecimal("R21_LEASING"));

			// R22
			obj.setR22FactoringDebtors(rs.getBigDecimal("R22_FACTORING_DEBTORS"));
			obj.setR22Leasing(rs.getBigDecimal("R22_LEASING"));

			// R23
			obj.setR23FactoringDebtors(rs.getBigDecimal("R23_FACTORING_DEBTORS"));
			obj.setR23Leasing(rs.getBigDecimal("R23_LEASING"));

			// R24
			obj.setR24FactoringDebtors(rs.getBigDecimal("R24_FACTORING_DEBTORS"));
			obj.setR24Leasing(rs.getBigDecimal("R24_LEASING"));

			// R25
			obj.setR25FactoringDebtors(rs.getBigDecimal("R25_FACTORING_DEBTORS"));
			obj.setR25Leasing(rs.getBigDecimal("R25_LEASING"));

			// R26
			obj.setR26FactoringDebtors(rs.getBigDecimal("R26_FACTORING_DEBTORS"));
			obj.setR26Leasing(rs.getBigDecimal("R26_LEASING"));

			// R27
			obj.setR27FactoringDebtors(rs.getBigDecimal("R27_FACTORING_DEBTORS"));
			obj.setR27Leasing(rs.getBigDecimal("R27_LEASING"));

			// R28
			obj.setR28FactoringDebtors(rs.getBigDecimal("R28_FACTORING_DEBTORS"));
			obj.setR28Leasing(rs.getBigDecimal("R28_LEASING"));

			// R29
			obj.setR29FactoringDebtors(rs.getBigDecimal("R29_FACTORING_DEBTORS"));
			obj.setR29Leasing(rs.getBigDecimal("R29_LEASING"));

			// R30
			obj.setR30FactoringDebtors(rs.getBigDecimal("R30_FACTORING_DEBTORS"));
			obj.setR30Leasing(rs.getBigDecimal("R30_LEASING"));

			// R31
			obj.setR31FactoringDebtors(rs.getBigDecimal("R31_FACTORING_DEBTORS"));
			obj.setR31Leasing(rs.getBigDecimal("R31_LEASING"));

			// R32
			obj.setR32FactoringDebtors(rs.getBigDecimal("R32_FACTORING_DEBTORS"));
			obj.setR32Leasing(rs.getBigDecimal("R32_LEASING"));

			// R33
			obj.setR33FactoringDebtors(rs.getBigDecimal("R33_FACTORING_DEBTORS"));
			obj.setR33Leasing(rs.getBigDecimal("R33_LEASING"));

			// R34
			obj.setR34FactoringDebtors(rs.getBigDecimal("R34_FACTORING_DEBTORS"));
			obj.setR34Leasing(rs.getBigDecimal("R34_LEASING"));

			// R35
			obj.setR35FactoringDebtors(rs.getBigDecimal("R35_FACTORING_DEBTORS"));
			obj.setR35Leasing(rs.getBigDecimal("R35_LEASING"));

			// R36
			obj.setR36FactoringDebtors(rs.getBigDecimal("R36_FACTORING_DEBTORS"));
			obj.setR36Leasing(rs.getBigDecimal("R36_LEASING"));

			// R37
			obj.setR37FactoringDebtors(rs.getBigDecimal("R37_FACTORING_DEBTORS"));
			obj.setR37Leasing(rs.getBigDecimal("R37_LEASING"));

			// R38
			obj.setR38FactoringDebtors(rs.getBigDecimal("R38_FACTORING_DEBTORS"));
			obj.setR38Leasing(rs.getBigDecimal("R38_LEASING"));

			// R39
			obj.setR39FactoringDebtors(rs.getBigDecimal("R39_FACTORING_DEBTORS"));
			obj.setR39Leasing(rs.getBigDecimal("R39_LEASING"));

			// R40
			obj.setR40FactoringDebtors(rs.getBigDecimal("R40_FACTORING_DEBTORS"));
			obj.setR40Leasing(rs.getBigDecimal("R40_LEASING"));

			// R41
			obj.setR41FactoringDebtors(rs.getBigDecimal("R41_FACTORING_DEBTORS"));
			obj.setR41Leasing(rs.getBigDecimal("R41_LEASING"));

			// R42
			obj.setR42FactoringDebtors(rs.getBigDecimal("R42_FACTORING_DEBTORS"));
			obj.setR42Leasing(rs.getBigDecimal("R42_LEASING"));

			// R43
			obj.setR43FactoringDebtors(rs.getBigDecimal("R43_FACTORING_DEBTORS"));
			obj.setR43Leasing(rs.getBigDecimal("R43_LEASING"));

			// R44
			obj.setR44FactoringDebtors(rs.getBigDecimal("R44_FACTORING_DEBTORS"));
			obj.setR44Leasing(rs.getBigDecimal("R44_LEASING"));

			// R45
			obj.setR45FactoringDebtors(rs.getBigDecimal("R45_FACTORING_DEBTORS"));
			obj.setR45Leasing(rs.getBigDecimal("R45_LEASING"));

			// R46
			obj.setR46FactoringDebtors(rs.getBigDecimal("R46_FACTORING_DEBTORS"));
			obj.setR46Leasing(rs.getBigDecimal("R46_LEASING"));

			// R47
			obj.setR47FactoringDebtors(rs.getBigDecimal("R47_FACTORING_DEBTORS"));
			obj.setR47Leasing(rs.getBigDecimal("R47_LEASING"));

			// R48
			obj.setR48FactoringDebtors(rs.getBigDecimal("R48_FACTORING_DEBTORS"));
			obj.setR48Leasing(rs.getBigDecimal("R48_LEASING"));

			// R49
			obj.setR49FactoringDebtors(rs.getBigDecimal("R49_FACTORING_DEBTORS"));
			obj.setR49Leasing(rs.getBigDecimal("R49_LEASING"));

			// R50
			obj.setR50FactoringDebtors(rs.getBigDecimal("R50_FACTORING_DEBTORS"));
			obj.setR50Leasing(rs.getBigDecimal("R50_LEASING"));

			// R51
			obj.setR51FactoringDebtors(rs.getBigDecimal("R51_FACTORING_DEBTORS"));
			obj.setR51Leasing(rs.getBigDecimal("R51_LEASING"));

			// R52
			obj.setR52FactoringDebtors(rs.getBigDecimal("R52_FACTORING_DEBTORS"));
			obj.setR52Leasing(rs.getBigDecimal("R52_LEASING"));

			// R53
			obj.setR53FactoringDebtors(rs.getBigDecimal("R53_FACTORING_DEBTORS"));
			obj.setR53Leasing(rs.getBigDecimal("R53_LEASING"));

			// R54
			obj.setR54FactoringDebtors(rs.getBigDecimal("R54_FACTORING_DEBTORS"));
			obj.setR54Leasing(rs.getBigDecimal("R54_LEASING"));

			// R55
			obj.setR55FactoringDebtors(rs.getBigDecimal("R55_FACTORING_DEBTORS"));
			obj.setR55Leasing(rs.getBigDecimal("R55_LEASING"));

			// R56
			obj.setR56FactoringDebtors(rs.getBigDecimal("R56_FACTORING_DEBTORS"));
			obj.setR56Leasing(rs.getBigDecimal("R56_LEASING"));

			// R57
			obj.setR57FactoringDebtors(rs.getBigDecimal("R57_FACTORING_DEBTORS"));
			obj.setR57Leasing(rs.getBigDecimal("R57_LEASING"));

			// R58
			obj.setR58FactoringDebtors(rs.getBigDecimal("R58_FACTORING_DEBTORS"));
			obj.setR58Leasing(rs.getBigDecimal("R58_LEASING"));

			// R59
			obj.setR59FactoringDebtors(rs.getBigDecimal("R59_FACTORING_DEBTORS"));
			obj.setR59Leasing(rs.getBigDecimal("R59_LEASING"));

			// R60
			obj.setR60FactoringDebtors(rs.getBigDecimal("R60_FACTORING_DEBTORS"));
			obj.setR60Leasing(rs.getBigDecimal("R60_LEASING"));

			// R61
			obj.setR61FactoringDebtors(rs.getBigDecimal("R61_FACTORING_DEBTORS"));
			obj.setR61Leasing(rs.getBigDecimal("R61_LEASING"));

			// R62
			obj.setR62FactoringDebtors(rs.getBigDecimal("R62_FACTORING_DEBTORS"));
			obj.setR62Leasing(rs.getBigDecimal("R62_LEASING"));

			// R63
			obj.setR63FactoringDebtors(rs.getBigDecimal("R63_FACTORING_DEBTORS"));
			obj.setR63Leasing(rs.getBigDecimal("R63_LEASING"));

			// R64
			obj.setR64FactoringDebtors(rs.getBigDecimal("R64_FACTORING_DEBTORS"));
			obj.setR64Leasing(rs.getBigDecimal("R64_LEASING"));

			// COMMON FIELDS
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

	@IdClass(M_LA4_PK.class)
	public static class M_LA4_Archival_Summary_Entity2 {
		@Column(name = "R11_FACTORING_DEBTORS")
		private BigDecimal r11FactoringDebtors;

		@Column(name = "R11_LEASING")
		private BigDecimal r11Leasing;

		@Column(name = "R12_FACTORING_DEBTORS")
		private BigDecimal r12FactoringDebtors;

		@Column(name = "R12_LEASING")
		private BigDecimal r12Leasing;

		@Column(name = "R13_FACTORING_DEBTORS")
		private BigDecimal r13FactoringDebtors;

		@Column(name = "R13_LEASING")
		private BigDecimal r13Leasing;

		@Column(name = "R14_FACTORING_DEBTORS")
		private BigDecimal r14FactoringDebtors;

		@Column(name = "R14_LEASING")
		private BigDecimal r14Leasing;

		@Column(name = "R15_FACTORING_DEBTORS")
		private BigDecimal r15FactoringDebtors;

		@Column(name = "R15_LEASING")
		private BigDecimal r15Leasing;

		@Column(name = "R16_FACTORING_DEBTORS")
		private BigDecimal r16FactoringDebtors;

		@Column(name = "R16_LEASING")
		private BigDecimal r16Leasing;

		@Column(name = "R17_FACTORING_DEBTORS")
		private BigDecimal r17FactoringDebtors;

		@Column(name = "R17_LEASING")
		private BigDecimal r17Leasing;

		@Column(name = "R18_FACTORING_DEBTORS")
		private BigDecimal r18FactoringDebtors;

		@Column(name = "R18_LEASING")
		private BigDecimal r18Leasing;

		@Column(name = "R19_FACTORING_DEBTORS")
		private BigDecimal r19FactoringDebtors;

		@Column(name = "R19_LEASING")
		private BigDecimal r19Leasing;

		@Column(name = "R20_FACTORING_DEBTORS")
		private BigDecimal r20FactoringDebtors;

		@Column(name = "R20_LEASING")
		private BigDecimal r20Leasing;

		@Column(name = "R21_FACTORING_DEBTORS")
		private BigDecimal r21FactoringDebtors;

		@Column(name = "R21_LEASING")
		private BigDecimal r21Leasing;

		@Column(name = "R22_FACTORING_DEBTORS")
		private BigDecimal r22FactoringDebtors;

		@Column(name = "R22_LEASING")
		private BigDecimal r22Leasing;

		@Column(name = "R23_FACTORING_DEBTORS")
		private BigDecimal r23FactoringDebtors;

		@Column(name = "R23_LEASING")
		private BigDecimal r23Leasing;

		@Column(name = "R24_FACTORING_DEBTORS")
		private BigDecimal r24FactoringDebtors;

		@Column(name = "R24_LEASING")
		private BigDecimal r24Leasing;

		@Column(name = "R25_FACTORING_DEBTORS")
		private BigDecimal r25FactoringDebtors;

		@Column(name = "R25_LEASING")
		private BigDecimal r25Leasing;

		@Column(name = "R26_FACTORING_DEBTORS")
		private BigDecimal r26FactoringDebtors;

		@Column(name = "R26_LEASING")
		private BigDecimal r26Leasing;

		@Column(name = "R27_FACTORING_DEBTORS")
		private BigDecimal r27FactoringDebtors;

		@Column(name = "R27_LEASING")
		private BigDecimal r27Leasing;

		@Column(name = "R28_FACTORING_DEBTORS")
		private BigDecimal r28FactoringDebtors;

		@Column(name = "R28_LEASING")
		private BigDecimal r28Leasing;

		@Column(name = "R29_FACTORING_DEBTORS")
		private BigDecimal r29FactoringDebtors;

		@Column(name = "R29_LEASING")
		private BigDecimal r29Leasing;

		@Column(name = "R30_FACTORING_DEBTORS")
		private BigDecimal r30FactoringDebtors;

		@Column(name = "R30_LEASING")
		private BigDecimal r30Leasing;

		@Column(name = "R31_FACTORING_DEBTORS")
		private BigDecimal r31FactoringDebtors;

		@Column(name = "R31_LEASING")
		private BigDecimal r31Leasing;

		@Column(name = "R32_FACTORING_DEBTORS")
		private BigDecimal r32FactoringDebtors;

		@Column(name = "R32_LEASING")
		private BigDecimal r32Leasing;

		@Column(name = "R33_FACTORING_DEBTORS")
		private BigDecimal r33FactoringDebtors;

		@Column(name = "R33_LEASING")
		private BigDecimal r33Leasing;

		@Column(name = "R34_FACTORING_DEBTORS")
		private BigDecimal r34FactoringDebtors;

		@Column(name = "R34_LEASING")
		private BigDecimal r34Leasing;

		@Column(name = "R35_FACTORING_DEBTORS")
		private BigDecimal r35FactoringDebtors;

		@Column(name = "R35_LEASING")
		private BigDecimal r35Leasing;

		@Column(name = "R36_FACTORING_DEBTORS")
		private BigDecimal r36FactoringDebtors;

		@Column(name = "R36_LEASING")
		private BigDecimal r36Leasing;

		@Column(name = "R37_FACTORING_DEBTORS")
		private BigDecimal r37FactoringDebtors;

		@Column(name = "R37_LEASING")
		private BigDecimal r37Leasing;

		@Column(name = "R38_FACTORING_DEBTORS")
		private BigDecimal r38FactoringDebtors;

		@Column(name = "R38_LEASING")
		private BigDecimal r38Leasing;

		@Column(name = "R39_FACTORING_DEBTORS")
		private BigDecimal r39FactoringDebtors;

		@Column(name = "R39_LEASING")
		private BigDecimal r39Leasing;

		@Column(name = "R40_FACTORING_DEBTORS")
		private BigDecimal r40FactoringDebtors;

		@Column(name = "R40_LEASING")
		private BigDecimal r40Leasing;

		@Column(name = "R41_FACTORING_DEBTORS")
		private BigDecimal r41FactoringDebtors;

		@Column(name = "R41_LEASING")
		private BigDecimal r41Leasing;

		@Column(name = "R42_FACTORING_DEBTORS")
		private BigDecimal r42FactoringDebtors;

		@Column(name = "R42_LEASING")
		private BigDecimal r42Leasing;

		@Column(name = "R43_FACTORING_DEBTORS")
		private BigDecimal r43FactoringDebtors;

		@Column(name = "R43_LEASING")
		private BigDecimal r43Leasing;

		@Column(name = "R44_FACTORING_DEBTORS")
		private BigDecimal r44FactoringDebtors;

		@Column(name = "R44_LEASING")
		private BigDecimal r44Leasing;

		@Column(name = "R45_FACTORING_DEBTORS")
		private BigDecimal r45FactoringDebtors;

		@Column(name = "R45_LEASING")
		private BigDecimal r45Leasing;

		@Column(name = "R46_FACTORING_DEBTORS")
		private BigDecimal r46FactoringDebtors;

		@Column(name = "R46_LEASING")
		private BigDecimal r46Leasing;

		@Column(name = "R47_FACTORING_DEBTORS")
		private BigDecimal r47FactoringDebtors;

		@Column(name = "R47_LEASING")
		private BigDecimal r47Leasing;

		@Column(name = "R48_FACTORING_DEBTORS")
		private BigDecimal r48FactoringDebtors;

		@Column(name = "R48_LEASING")
		private BigDecimal r48Leasing;

		@Column(name = "R49_FACTORING_DEBTORS")
		private BigDecimal r49FactoringDebtors;

		@Column(name = "R49_LEASING")
		private BigDecimal r49Leasing;

		@Column(name = "R50_FACTORING_DEBTORS")
		private BigDecimal r50FactoringDebtors;

		@Column(name = "R50_LEASING")
		private BigDecimal r50Leasing;

		@Column(name = "R51_FACTORING_DEBTORS")
		private BigDecimal r51FactoringDebtors;

		@Column(name = "R51_LEASING")
		private BigDecimal r51Leasing;

		@Column(name = "R52_FACTORING_DEBTORS")
		private BigDecimal r52FactoringDebtors;

		@Column(name = "R52_LEASING")
		private BigDecimal r52Leasing;

		@Column(name = "R53_FACTORING_DEBTORS")
		private BigDecimal r53FactoringDebtors;

		@Column(name = "R53_LEASING")
		private BigDecimal r53Leasing;

		@Column(name = "R54_FACTORING_DEBTORS")
		private BigDecimal r54FactoringDebtors;

		@Column(name = "R54_LEASING")
		private BigDecimal r54Leasing;

		@Column(name = "R55_FACTORING_DEBTORS")
		private BigDecimal r55FactoringDebtors;

		@Column(name = "R55_LEASING")
		private BigDecimal r55Leasing;

		@Column(name = "R56_FACTORING_DEBTORS")
		private BigDecimal r56FactoringDebtors;

		@Column(name = "R56_LEASING")
		private BigDecimal r56Leasing;

		@Column(name = "R57_FACTORING_DEBTORS")
		private BigDecimal r57FactoringDebtors;

		@Column(name = "R57_LEASING")
		private BigDecimal r57Leasing;

		@Column(name = "R58_FACTORING_DEBTORS")
		private BigDecimal r58FactoringDebtors;

		@Column(name = "R58_LEASING")
		private BigDecimal r58Leasing;

		@Column(name = "R59_FACTORING_DEBTORS")
		private BigDecimal r59FactoringDebtors;

		@Column(name = "R59_LEASING")
		private BigDecimal r59Leasing;

		@Column(name = "R60_FACTORING_DEBTORS")
		private BigDecimal r60FactoringDebtors;

		@Column(name = "R60_LEASING")
		private BigDecimal r60Leasing;

		@Column(name = "R61_FACTORING_DEBTORS")
		private BigDecimal r61FactoringDebtors;

		@Column(name = "R61_LEASING")
		private BigDecimal r61Leasing;

		@Column(name = "R62_FACTORING_DEBTORS")
		private BigDecimal r62FactoringDebtors;

		@Column(name = "R62_LEASING")
		private BigDecimal r62Leasing;

		@Column(name = "R63_FACTORING_DEBTORS")
		private BigDecimal r63FactoringDebtors;

		@Column(name = "R63_LEASING")
		private BigDecimal r63Leasing;

		@Column(name = "R64_FACTORING_DEBTORS")
		private BigDecimal r64FactoringDebtors;

		@Column(name = "R64_LEASING")
		private BigDecimal r64Leasing;
		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Id
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

		private Date REPORT_RESUBDATE;

		public BigDecimal getR11FactoringDebtors() {
			return r11FactoringDebtors;
		}

		public void setR11FactoringDebtors(BigDecimal r11FactoringDebtors) {
			this.r11FactoringDebtors = r11FactoringDebtors;
		}

		public BigDecimal getR11Leasing() {
			return r11Leasing;
		}

		public void setR11Leasing(BigDecimal r11Leasing) {
			this.r11Leasing = r11Leasing;
		}

		public BigDecimal getR12FactoringDebtors() {
			return r12FactoringDebtors;
		}

		public void setR12FactoringDebtors(BigDecimal r12FactoringDebtors) {
			this.r12FactoringDebtors = r12FactoringDebtors;
		}

		public BigDecimal getR12Leasing() {
			return r12Leasing;
		}

		public void setR12Leasing(BigDecimal r12Leasing) {
			this.r12Leasing = r12Leasing;
		}

		public BigDecimal getR13FactoringDebtors() {
			return r13FactoringDebtors;
		}

		public void setR13FactoringDebtors(BigDecimal r13FactoringDebtors) {
			this.r13FactoringDebtors = r13FactoringDebtors;
		}

		public BigDecimal getR13Leasing() {
			return r13Leasing;
		}

		public void setR13Leasing(BigDecimal r13Leasing) {
			this.r13Leasing = r13Leasing;
		}

		public BigDecimal getR14FactoringDebtors() {
			return r14FactoringDebtors;
		}

		public void setR14FactoringDebtors(BigDecimal r14FactoringDebtors) {
			this.r14FactoringDebtors = r14FactoringDebtors;
		}

		public BigDecimal getR14Leasing() {
			return r14Leasing;
		}

		public void setR14Leasing(BigDecimal r14Leasing) {
			this.r14Leasing = r14Leasing;
		}

		public BigDecimal getR15FactoringDebtors() {
			return r15FactoringDebtors;
		}

		public void setR15FactoringDebtors(BigDecimal r15FactoringDebtors) {
			this.r15FactoringDebtors = r15FactoringDebtors;
		}

		public BigDecimal getR15Leasing() {
			return r15Leasing;
		}

		public void setR15Leasing(BigDecimal r15Leasing) {
			this.r15Leasing = r15Leasing;
		}

		public BigDecimal getR16FactoringDebtors() {
			return r16FactoringDebtors;
		}

		public void setR16FactoringDebtors(BigDecimal r16FactoringDebtors) {
			this.r16FactoringDebtors = r16FactoringDebtors;
		}

		public BigDecimal getR16Leasing() {
			return r16Leasing;
		}

		public void setR16Leasing(BigDecimal r16Leasing) {
			this.r16Leasing = r16Leasing;
		}

		public BigDecimal getR17FactoringDebtors() {
			return r17FactoringDebtors;
		}

		public void setR17FactoringDebtors(BigDecimal r17FactoringDebtors) {
			this.r17FactoringDebtors = r17FactoringDebtors;
		}

		public BigDecimal getR17Leasing() {
			return r17Leasing;
		}

		public void setR17Leasing(BigDecimal r17Leasing) {
			this.r17Leasing = r17Leasing;
		}

		public BigDecimal getR18FactoringDebtors() {
			return r18FactoringDebtors;
		}

		public void setR18FactoringDebtors(BigDecimal r18FactoringDebtors) {
			this.r18FactoringDebtors = r18FactoringDebtors;
		}

		public BigDecimal getR18Leasing() {
			return r18Leasing;
		}

		public void setR18Leasing(BigDecimal r18Leasing) {
			this.r18Leasing = r18Leasing;
		}

		public BigDecimal getR19FactoringDebtors() {
			return r19FactoringDebtors;
		}

		public void setR19FactoringDebtors(BigDecimal r19FactoringDebtors) {
			this.r19FactoringDebtors = r19FactoringDebtors;
		}

		public BigDecimal getR19Leasing() {
			return r19Leasing;
		}

		public void setR19Leasing(BigDecimal r19Leasing) {
			this.r19Leasing = r19Leasing;
		}

		public BigDecimal getR20FactoringDebtors() {
			return r20FactoringDebtors;
		}

		public void setR20FactoringDebtors(BigDecimal r20FactoringDebtors) {
			this.r20FactoringDebtors = r20FactoringDebtors;
		}

		public BigDecimal getR20Leasing() {
			return r20Leasing;
		}

		public void setR20Leasing(BigDecimal r20Leasing) {
			this.r20Leasing = r20Leasing;
		}

		public BigDecimal getR21FactoringDebtors() {
			return r21FactoringDebtors;
		}

		public void setR21FactoringDebtors(BigDecimal r21FactoringDebtors) {
			this.r21FactoringDebtors = r21FactoringDebtors;
		}

		public BigDecimal getR21Leasing() {
			return r21Leasing;
		}

		public void setR21Leasing(BigDecimal r21Leasing) {
			this.r21Leasing = r21Leasing;
		}

		public BigDecimal getR22FactoringDebtors() {
			return r22FactoringDebtors;
		}

		public void setR22FactoringDebtors(BigDecimal r22FactoringDebtors) {
			this.r22FactoringDebtors = r22FactoringDebtors;
		}

		public BigDecimal getR22Leasing() {
			return r22Leasing;
		}

		public void setR22Leasing(BigDecimal r22Leasing) {
			this.r22Leasing = r22Leasing;
		}

		public BigDecimal getR23FactoringDebtors() {
			return r23FactoringDebtors;
		}

		public void setR23FactoringDebtors(BigDecimal r23FactoringDebtors) {
			this.r23FactoringDebtors = r23FactoringDebtors;
		}

		public BigDecimal getR23Leasing() {
			return r23Leasing;
		}

		public void setR23Leasing(BigDecimal r23Leasing) {
			this.r23Leasing = r23Leasing;
		}

		public BigDecimal getR24FactoringDebtors() {
			return r24FactoringDebtors;
		}

		public void setR24FactoringDebtors(BigDecimal r24FactoringDebtors) {
			this.r24FactoringDebtors = r24FactoringDebtors;
		}

		public BigDecimal getR24Leasing() {
			return r24Leasing;
		}

		public void setR24Leasing(BigDecimal r24Leasing) {
			this.r24Leasing = r24Leasing;
		}

		public BigDecimal getR25FactoringDebtors() {
			return r25FactoringDebtors;
		}

		public void setR25FactoringDebtors(BigDecimal r25FactoringDebtors) {
			this.r25FactoringDebtors = r25FactoringDebtors;
		}

		public BigDecimal getR25Leasing() {
			return r25Leasing;
		}

		public void setR25Leasing(BigDecimal r25Leasing) {
			this.r25Leasing = r25Leasing;
		}

		public BigDecimal getR26FactoringDebtors() {
			return r26FactoringDebtors;
		}

		public void setR26FactoringDebtors(BigDecimal r26FactoringDebtors) {
			this.r26FactoringDebtors = r26FactoringDebtors;
		}

		public BigDecimal getR26Leasing() {
			return r26Leasing;
		}

		public void setR26Leasing(BigDecimal r26Leasing) {
			this.r26Leasing = r26Leasing;
		}

		public BigDecimal getR27FactoringDebtors() {
			return r27FactoringDebtors;
		}

		public void setR27FactoringDebtors(BigDecimal r27FactoringDebtors) {
			this.r27FactoringDebtors = r27FactoringDebtors;
		}

		public BigDecimal getR27Leasing() {
			return r27Leasing;
		}

		public void setR27Leasing(BigDecimal r27Leasing) {
			this.r27Leasing = r27Leasing;
		}

		public BigDecimal getR28FactoringDebtors() {
			return r28FactoringDebtors;
		}

		public void setR28FactoringDebtors(BigDecimal r28FactoringDebtors) {
			this.r28FactoringDebtors = r28FactoringDebtors;
		}

		public BigDecimal getR28Leasing() {
			return r28Leasing;
		}

		public void setR28Leasing(BigDecimal r28Leasing) {
			this.r28Leasing = r28Leasing;
		}

		public BigDecimal getR29FactoringDebtors() {
			return r29FactoringDebtors;
		}

		public void setR29FactoringDebtors(BigDecimal r29FactoringDebtors) {
			this.r29FactoringDebtors = r29FactoringDebtors;
		}

		public BigDecimal getR29Leasing() {
			return r29Leasing;
		}

		public void setR29Leasing(BigDecimal r29Leasing) {
			this.r29Leasing = r29Leasing;
		}

		public BigDecimal getR30FactoringDebtors() {
			return r30FactoringDebtors;
		}

		public void setR30FactoringDebtors(BigDecimal r30FactoringDebtors) {
			this.r30FactoringDebtors = r30FactoringDebtors;
		}

		public BigDecimal getR30Leasing() {
			return r30Leasing;
		}

		public void setR30Leasing(BigDecimal r30Leasing) {
			this.r30Leasing = r30Leasing;
		}

		public BigDecimal getR31FactoringDebtors() {
			return r31FactoringDebtors;
		}

		public void setR31FactoringDebtors(BigDecimal r31FactoringDebtors) {
			this.r31FactoringDebtors = r31FactoringDebtors;
		}

		public BigDecimal getR31Leasing() {
			return r31Leasing;
		}

		public void setR31Leasing(BigDecimal r31Leasing) {
			this.r31Leasing = r31Leasing;
		}

		public BigDecimal getR32FactoringDebtors() {
			return r32FactoringDebtors;
		}

		public void setR32FactoringDebtors(BigDecimal r32FactoringDebtors) {
			this.r32FactoringDebtors = r32FactoringDebtors;
		}

		public BigDecimal getR32Leasing() {
			return r32Leasing;
		}

		public void setR32Leasing(BigDecimal r32Leasing) {
			this.r32Leasing = r32Leasing;
		}

		public BigDecimal getR33FactoringDebtors() {
			return r33FactoringDebtors;
		}

		public void setR33FactoringDebtors(BigDecimal r33FactoringDebtors) {
			this.r33FactoringDebtors = r33FactoringDebtors;
		}

		public BigDecimal getR33Leasing() {
			return r33Leasing;
		}

		public void setR33Leasing(BigDecimal r33Leasing) {
			this.r33Leasing = r33Leasing;
		}

		public BigDecimal getR34FactoringDebtors() {
			return r34FactoringDebtors;
		}

		public void setR34FactoringDebtors(BigDecimal r34FactoringDebtors) {
			this.r34FactoringDebtors = r34FactoringDebtors;
		}

		public BigDecimal getR34Leasing() {
			return r34Leasing;
		}

		public void setR34Leasing(BigDecimal r34Leasing) {
			this.r34Leasing = r34Leasing;
		}

		public BigDecimal getR35FactoringDebtors() {
			return r35FactoringDebtors;
		}

		public void setR35FactoringDebtors(BigDecimal r35FactoringDebtors) {
			this.r35FactoringDebtors = r35FactoringDebtors;
		}

		public BigDecimal getR35Leasing() {
			return r35Leasing;
		}

		public void setR35Leasing(BigDecimal r35Leasing) {
			this.r35Leasing = r35Leasing;
		}

		public BigDecimal getR36FactoringDebtors() {
			return r36FactoringDebtors;
		}

		public void setR36FactoringDebtors(BigDecimal r36FactoringDebtors) {
			this.r36FactoringDebtors = r36FactoringDebtors;
		}

		public BigDecimal getR36Leasing() {
			return r36Leasing;
		}

		public void setR36Leasing(BigDecimal r36Leasing) {
			this.r36Leasing = r36Leasing;
		}

		public BigDecimal getR37FactoringDebtors() {
			return r37FactoringDebtors;
		}

		public void setR37FactoringDebtors(BigDecimal r37FactoringDebtors) {
			this.r37FactoringDebtors = r37FactoringDebtors;
		}

		public BigDecimal getR37Leasing() {
			return r37Leasing;
		}

		public void setR37Leasing(BigDecimal r37Leasing) {
			this.r37Leasing = r37Leasing;
		}

		public BigDecimal getR38FactoringDebtors() {
			return r38FactoringDebtors;
		}

		public void setR38FactoringDebtors(BigDecimal r38FactoringDebtors) {
			this.r38FactoringDebtors = r38FactoringDebtors;
		}

		public BigDecimal getR38Leasing() {
			return r38Leasing;
		}

		public void setR38Leasing(BigDecimal r38Leasing) {
			this.r38Leasing = r38Leasing;
		}

		public BigDecimal getR39FactoringDebtors() {
			return r39FactoringDebtors;
		}

		public void setR39FactoringDebtors(BigDecimal r39FactoringDebtors) {
			this.r39FactoringDebtors = r39FactoringDebtors;
		}

		public BigDecimal getR39Leasing() {
			return r39Leasing;
		}

		public void setR39Leasing(BigDecimal r39Leasing) {
			this.r39Leasing = r39Leasing;
		}

		public BigDecimal getR40FactoringDebtors() {
			return r40FactoringDebtors;
		}

		public void setR40FactoringDebtors(BigDecimal r40FactoringDebtors) {
			this.r40FactoringDebtors = r40FactoringDebtors;
		}

		public BigDecimal getR40Leasing() {
			return r40Leasing;
		}

		public void setR40Leasing(BigDecimal r40Leasing) {
			this.r40Leasing = r40Leasing;
		}

		public BigDecimal getR41FactoringDebtors() {
			return r41FactoringDebtors;
		}

		public void setR41FactoringDebtors(BigDecimal r41FactoringDebtors) {
			this.r41FactoringDebtors = r41FactoringDebtors;
		}

		public BigDecimal getR41Leasing() {
			return r41Leasing;
		}

		public void setR41Leasing(BigDecimal r41Leasing) {
			this.r41Leasing = r41Leasing;
		}

		public BigDecimal getR42FactoringDebtors() {
			return r42FactoringDebtors;
		}

		public void setR42FactoringDebtors(BigDecimal r42FactoringDebtors) {
			this.r42FactoringDebtors = r42FactoringDebtors;
		}

		public BigDecimal getR42Leasing() {
			return r42Leasing;
		}

		public void setR42Leasing(BigDecimal r42Leasing) {
			this.r42Leasing = r42Leasing;
		}

		public BigDecimal getR43FactoringDebtors() {
			return r43FactoringDebtors;
		}

		public void setR43FactoringDebtors(BigDecimal r43FactoringDebtors) {
			this.r43FactoringDebtors = r43FactoringDebtors;
		}

		public BigDecimal getR43Leasing() {
			return r43Leasing;
		}

		public void setR43Leasing(BigDecimal r43Leasing) {
			this.r43Leasing = r43Leasing;
		}

		public BigDecimal getR44FactoringDebtors() {
			return r44FactoringDebtors;
		}

		public void setR44FactoringDebtors(BigDecimal r44FactoringDebtors) {
			this.r44FactoringDebtors = r44FactoringDebtors;
		}

		public BigDecimal getR44Leasing() {
			return r44Leasing;
		}

		public void setR44Leasing(BigDecimal r44Leasing) {
			this.r44Leasing = r44Leasing;
		}

		public BigDecimal getR45FactoringDebtors() {
			return r45FactoringDebtors;
		}

		public void setR45FactoringDebtors(BigDecimal r45FactoringDebtors) {
			this.r45FactoringDebtors = r45FactoringDebtors;
		}

		public BigDecimal getR45Leasing() {
			return r45Leasing;
		}

		public void setR45Leasing(BigDecimal r45Leasing) {
			this.r45Leasing = r45Leasing;
		}

		public BigDecimal getR46FactoringDebtors() {
			return r46FactoringDebtors;
		}

		public void setR46FactoringDebtors(BigDecimal r46FactoringDebtors) {
			this.r46FactoringDebtors = r46FactoringDebtors;
		}

		public BigDecimal getR46Leasing() {
			return r46Leasing;
		}

		public void setR46Leasing(BigDecimal r46Leasing) {
			this.r46Leasing = r46Leasing;
		}

		public BigDecimal getR47FactoringDebtors() {
			return r47FactoringDebtors;
		}

		public void setR47FactoringDebtors(BigDecimal r47FactoringDebtors) {
			this.r47FactoringDebtors = r47FactoringDebtors;
		}

		public BigDecimal getR47Leasing() {
			return r47Leasing;
		}

		public void setR47Leasing(BigDecimal r47Leasing) {
			this.r47Leasing = r47Leasing;
		}

		public BigDecimal getR48FactoringDebtors() {
			return r48FactoringDebtors;
		}

		public void setR48FactoringDebtors(BigDecimal r48FactoringDebtors) {
			this.r48FactoringDebtors = r48FactoringDebtors;
		}

		public BigDecimal getR48Leasing() {
			return r48Leasing;
		}

		public void setR48Leasing(BigDecimal r48Leasing) {
			this.r48Leasing = r48Leasing;
		}

		public BigDecimal getR49FactoringDebtors() {
			return r49FactoringDebtors;
		}

		public void setR49FactoringDebtors(BigDecimal r49FactoringDebtors) {
			this.r49FactoringDebtors = r49FactoringDebtors;
		}

		public BigDecimal getR49Leasing() {
			return r49Leasing;
		}

		public void setR49Leasing(BigDecimal r49Leasing) {
			this.r49Leasing = r49Leasing;
		}

		public BigDecimal getR50FactoringDebtors() {
			return r50FactoringDebtors;
		}

		public void setR50FactoringDebtors(BigDecimal r50FactoringDebtors) {
			this.r50FactoringDebtors = r50FactoringDebtors;
		}

		public BigDecimal getR50Leasing() {
			return r50Leasing;
		}

		public void setR50Leasing(BigDecimal r50Leasing) {
			this.r50Leasing = r50Leasing;
		}

		public BigDecimal getR51FactoringDebtors() {
			return r51FactoringDebtors;
		}

		public void setR51FactoringDebtors(BigDecimal r51FactoringDebtors) {
			this.r51FactoringDebtors = r51FactoringDebtors;
		}

		public BigDecimal getR51Leasing() {
			return r51Leasing;
		}

		public void setR51Leasing(BigDecimal r51Leasing) {
			this.r51Leasing = r51Leasing;
		}

		public BigDecimal getR52FactoringDebtors() {
			return r52FactoringDebtors;
		}

		public void setR52FactoringDebtors(BigDecimal r52FactoringDebtors) {
			this.r52FactoringDebtors = r52FactoringDebtors;
		}

		public BigDecimal getR52Leasing() {
			return r52Leasing;
		}

		public void setR52Leasing(BigDecimal r52Leasing) {
			this.r52Leasing = r52Leasing;
		}

		public BigDecimal getR53FactoringDebtors() {
			return r53FactoringDebtors;
		}

		public void setR53FactoringDebtors(BigDecimal r53FactoringDebtors) {
			this.r53FactoringDebtors = r53FactoringDebtors;
		}

		public BigDecimal getR53Leasing() {
			return r53Leasing;
		}

		public void setR53Leasing(BigDecimal r53Leasing) {
			this.r53Leasing = r53Leasing;
		}

		public BigDecimal getR54FactoringDebtors() {
			return r54FactoringDebtors;
		}

		public void setR54FactoringDebtors(BigDecimal r54FactoringDebtors) {
			this.r54FactoringDebtors = r54FactoringDebtors;
		}

		public BigDecimal getR54Leasing() {
			return r54Leasing;
		}

		public void setR54Leasing(BigDecimal r54Leasing) {
			this.r54Leasing = r54Leasing;
		}

		public BigDecimal getR55FactoringDebtors() {
			return r55FactoringDebtors;
		}

		public void setR55FactoringDebtors(BigDecimal r55FactoringDebtors) {
			this.r55FactoringDebtors = r55FactoringDebtors;
		}

		public BigDecimal getR55Leasing() {
			return r55Leasing;
		}

		public void setR55Leasing(BigDecimal r55Leasing) {
			this.r55Leasing = r55Leasing;
		}

		public BigDecimal getR56FactoringDebtors() {
			return r56FactoringDebtors;
		}

		public void setR56FactoringDebtors(BigDecimal r56FactoringDebtors) {
			this.r56FactoringDebtors = r56FactoringDebtors;
		}

		public BigDecimal getR56Leasing() {
			return r56Leasing;
		}

		public void setR56Leasing(BigDecimal r56Leasing) {
			this.r56Leasing = r56Leasing;
		}

		public BigDecimal getR57FactoringDebtors() {
			return r57FactoringDebtors;
		}

		public void setR57FactoringDebtors(BigDecimal r57FactoringDebtors) {
			this.r57FactoringDebtors = r57FactoringDebtors;
		}

		public BigDecimal getR57Leasing() {
			return r57Leasing;
		}

		public void setR57Leasing(BigDecimal r57Leasing) {
			this.r57Leasing = r57Leasing;
		}

		public BigDecimal getR58FactoringDebtors() {
			return r58FactoringDebtors;
		}

		public void setR58FactoringDebtors(BigDecimal r58FactoringDebtors) {
			this.r58FactoringDebtors = r58FactoringDebtors;
		}

		public BigDecimal getR58Leasing() {
			return r58Leasing;
		}

		public void setR58Leasing(BigDecimal r58Leasing) {
			this.r58Leasing = r58Leasing;
		}

		public BigDecimal getR59FactoringDebtors() {
			return r59FactoringDebtors;
		}

		public void setR59FactoringDebtors(BigDecimal r59FactoringDebtors) {
			this.r59FactoringDebtors = r59FactoringDebtors;
		}

		public BigDecimal getR59Leasing() {
			return r59Leasing;
		}

		public void setR59Leasing(BigDecimal r59Leasing) {
			this.r59Leasing = r59Leasing;
		}

		public BigDecimal getR60FactoringDebtors() {
			return r60FactoringDebtors;
		}

		public void setR60FactoringDebtors(BigDecimal r60FactoringDebtors) {
			this.r60FactoringDebtors = r60FactoringDebtors;
		}

		public BigDecimal getR60Leasing() {
			return r60Leasing;
		}

		public void setR60Leasing(BigDecimal r60Leasing) {
			this.r60Leasing = r60Leasing;
		}

		public BigDecimal getR61FactoringDebtors() {
			return r61FactoringDebtors;
		}

		public void setR61FactoringDebtors(BigDecimal r61FactoringDebtors) {
			this.r61FactoringDebtors = r61FactoringDebtors;
		}

		public BigDecimal getR61Leasing() {
			return r61Leasing;
		}

		public void setR61Leasing(BigDecimal r61Leasing) {
			this.r61Leasing = r61Leasing;
		}

		public BigDecimal getR62FactoringDebtors() {
			return r62FactoringDebtors;
		}

		public void setR62FactoringDebtors(BigDecimal r62FactoringDebtors) {
			this.r62FactoringDebtors = r62FactoringDebtors;
		}

		public BigDecimal getR62Leasing() {
			return r62Leasing;
		}

		public void setR62Leasing(BigDecimal r62Leasing) {
			this.r62Leasing = r62Leasing;
		}

		public BigDecimal getR63FactoringDebtors() {
			return r63FactoringDebtors;
		}

		public void setR63FactoringDebtors(BigDecimal r63FactoringDebtors) {
			this.r63FactoringDebtors = r63FactoringDebtors;
		}

		public BigDecimal getR63Leasing() {
			return r63Leasing;
		}

		public void setR63Leasing(BigDecimal r63Leasing) {
			this.r63Leasing = r63Leasing;
		}

		public BigDecimal getR64FactoringDebtors() {
			return r64FactoringDebtors;
		}

		public void setR64FactoringDebtors(BigDecimal r64FactoringDebtors) {
			this.r64FactoringDebtors = r64FactoringDebtors;
		}

		public BigDecimal getR64Leasing() {
			return r64Leasing;
		}

		public void setR64Leasing(BigDecimal r64Leasing) {
			this.r64Leasing = r64Leasing;
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

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
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

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}
	}
// COMPOSITE KEY CLASS INSIDE SERVICE

	public static class M_LA4_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public M_LA4_PK() {
		}

		public M_LA4_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_LA4_PK))
				return false;
			M_LA4_PK that = (M_LA4_PK) o;
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

	public class M_LA4_Detail_Entity {
		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private char entityFlg;

		@Column(name = "MODIFY_FLG")
		private char modifyFlg;

		@Column(name = "DEL_FLG")
		private char delFlg;

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public char getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(char entityFlg) {
			this.entityFlg = entityFlg;
		}

		public char getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(char modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public char getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(char delFlg) {
			this.delFlg = delFlg;
		}

	}

	class M_LA4DetailRowMapper implements RowMapper<M_LA4_Detail_Entity> {

		@Override
		public M_LA4_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA4_Detail_Entity obj = new M_LA4_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');

			obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');

			obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			return obj;
		}
	}

	class M_LA4ArchivalDetailRowMapper implements RowMapper<M_LA4_Archival_Detail_Entity> {

		@Override
		public M_LA4_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA4_Archival_Detail_Entity obj = new M_LA4_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');

			obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');

			obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			return obj;
		}
	}

	public class M_LA4_Archival_Detail_Entity {
		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private char entityFlg;

		@Column(name = "MODIFY_FLG")
		private char modifyFlg;

		@Column(name = "DEL_FLG")
		private char delFlg;

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public char getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(char entityFlg) {
			this.entityFlg = entityFlg;
		}

		public char getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(char modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public char getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(char delFlg) {
			this.delFlg = delFlg;
		}

	}

	// MODEL AND VIEW METHOD summary

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LA4View(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version, HttpServletRequest req, Model md) {

		ModelAndView mv = new ModelAndView();

		System.out.println("M_LA4 View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// =====================================================
		// ARCHIVAL MODE
		// =====================================================

		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<M_LA4_Archival_Summary_Entity1> T1Master = new ArrayList<>();
			List<M_LA4_Archival_Summary_Entity2> T2Master = new ArrayList<>();

			try {
				Date dt = dateformat.parse(todate);

				// ============================
				// SUMMARY ARCHIVAL
				// ============================
				T1Master = getdatabydateListarchival1(dt, version);

				System.out.println("Archival Summary size = " + T1Master.size());

				T2Master = getdatabydateListarchival2(dt, version);

				System.out.println("Archival Summary size = " + T2Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));
				System.out.println("getishighestversion(dt, version) : " + getishighestversion(dt, version));
				mv.addObject("allowdetail", getishighestversion(dt, version));
			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);

		}
		// =====================================================
		// NORMAL MODE
		// =====================================================

		else {

			List<M_LA4_Summary_Entity1> T1Master = new ArrayList<>();
			List<M_LA4_Summary_Entity2> T2Master = new ArrayList<>();

			try {
				Date dt = dateformat.parse(todate);

				// SUMMARY NORMAL
				T1Master = getDataByDate1(dt);

				System.out.println("Summary size = " + T1Master.size());

				T2Master = getDataByDate2(dt);

				System.out.println("Summary size = " + T2Master.size());

				mv.addObject("report_date", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);

		}

		// =====================================================
		// VIEW SETTINGS
		// =====================================================

		mv.setViewName("BRRS/M_LA4");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getM_LA4currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
			Model md) {

		ModelAndView mv = new ModelAndView();

		try {

			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLabel = null;
			String reportAddlCriteria1 = null;

			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLabel = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}

			// ARCHIVAL / RESUB MODE
			if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

				System.out.println(type + " DETAIL MODE");

				List<M_LA4_Archival_Detail_Entity> detailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					detailList = GetArchivalDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

				} else {

					detailList = getArchivalDetaildatabydateList(parsedDate);
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);

				System.out.println(type + " DETAIL COUNT: " + detailList.size());
			}

			// CURRENT MODE

			else {

				List<M_LA4_Detail_Entity> currentDetailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					currentDetailList = GetDetailDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

				} else {

					currentDetailList = getDetaildatabydateList(parsedDate);

				}

				mv.addObject("reportdetails", currentDetailList);
				mv.addObject("reportmaster12", currentDetailList);

				System.out.println("CURRENT DETAIL COUNT: " + currentDetailList.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", e.getMessage());
		}

		mv.setViewName("BRRS/M_LA4");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getM_LA4Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<M_LA4_Archival_Summary_Entity1> repoData = getdatabydateListWithVersion1();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_LA4_Archival_Summary_Entity1 entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_LA4_Archival_Summary_Entity1 first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_LA4  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/M_LA4");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				M_LA4_Detail_Entity M_LA4Entity = findBySnoArch(SNO);
				if (M_LA4Entity != null && M_LA4Entity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(M_LA4Entity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("M_LA4Data", M_LA4Entity);
			} else {
				M_LA4_Detail_Entity M_LA4Entity = findBySno(SNO);
				if (M_LA4Entity != null && M_LA4Entity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(M_LA4Entity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("M_LA4Data", M_LA4Entity);
			}
		}
		mv.addObject("type", type);
		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {

		try {

			String Sno = request.getParameter("sno");

			String acctBalanceInpula = request.getParameter("acctBalanceInpula");

			String acctName = request.getParameter("acctName");

			String reportDateStr = request.getParameter("reportDate");

			System.out.println("Sno is : " + Sno);
			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			// Load Existing Record
			M_LA4_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBySnoArch(Sno);
			} else {
				existing = findBySno(Sno);
			}
			M_LA4_Detail_Entity oldcopy = new M_LA4_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			if (existing == null) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			// Update Name
			if (acctName != null && !acctName.isEmpty()) {

				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {

					existing.setAcctName(acctName);

					isChanged = true;
				}
			}

			// Update Balance
			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

				BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newBalance) != 0) {

					existing.setAcctBalanceInpula(newBalance);

					isChanged = true;
				}
			}

			// Save using JDBC
			if (isChanged) {
				String sql;
				System.out.println("Type in update block : " + type);
				// Safe from NullPointerExceptions and reference comparison bugs
				if ("RESUB".equalsIgnoreCase(type)) {
					System.out.println("Inside RESUB UPDATE");
					sql = "UPDATE BRRS_M_LA4_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ? " + "WHERE SNO = ?";
				} else {
					System.out.println("Inside NORMAL UPDATE");
					sql = "UPDATE BRRS_M_LA4_DETAILTABLE " + "SET ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ? "
							+ "WHERE SNO = ?";
				}

				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), Sno);
				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "M_LA4 Archival Screen",
							"BRRS_M_LA4_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "M_LA4 Screen",
							"BRRS_M_LA4_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_M_LA4_Procudure(reportDateStr, type, entry);

				if ((type == "RESUB" || type.equals("RESUB")) && (entry == "NO" || entry.equals("NO"))) {
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}
				return ResponseEntity.ok("Record updated successfully!");
			} else {
				return ResponseEntity.ok("No changes were made.");
			}

		}

		catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {
		try {
			Run_M_LA4_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_M_LA4_Procudure(String reportDateStr, String type, String entry) {

		String formattedDate;
		try {
			formattedDate = new SimpleDateFormat("dd-MM-yyyy")
					.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));
		} catch (Exception e) {
			System.out.println("Error parsing date. Post-commit logic aborted.");
			e.printStackTrace();
			return;
		}

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				try {
					boolean isResubNoEntry = "RESUB".equals(type) && "NO".equals(entry);
					boolean shouldExecuteProcedure = !"RESUB".equals(type) || isResubNoEntry;

					if (isResubNoEntry) {
						String bdsql = "DELETE FROM BRRS_M_LA4_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						String sqltransfer = "INSERT INTO BRRS_M_LA4_DETAILTABLE "
								+ " (SNO, ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) "
								+ "SELECT SNO, ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
								+ "FROM BRRS_M_LA4_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";
						int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
						System.out.println("Successfully transferred " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_M_LA4_SUMMARY_PROCEDURE(?); END;", formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String adsql = "DELETE FROM BRRS_M_LA4_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						// 1. Handle Archival Summary Table (System Generated - LA1 Mapping Values)
						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY1 WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						// Dynamic Generation of Columns for System Summary (R11 to R64)
						StringBuilder la1Columns = new StringBuilder();
						for (int i = 11; i <= 64; i++) {
							la1Columns.append("R").append(i).append("_ADVANCES_BY_INSTITUTIONAL_SECTOR, ").append("R")
									.append(i).append("_OVERDRAFTS, ").append("R").append(i)
									.append("_OTHER_INSTALLMENT_LOANS, ").append("R").append(i).append("_TOTAL, ");
						}

						String finalsql = "INSERT INTO BRRS_M_LA4_ARCHIVALTABLE_SUMMARY1 (" + la1Columns.toString()
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE"
								+ ") SELECT " + la1Columns.toString()
								+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_M_LA4_SUMMARYTABLE1 WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
						System.out.println("Successfully transferred system summary " + rowsInsertedSum + " rows.");

						// 2. Handle Manual Archival Summary Table (User Edited Front-End Values - LA2)
						String insManualSql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY2 WHERE REPORT_DATE = ?";
						Integer maxManualVersion = jdbcTemplate.queryForObject(insManualSql, Integer.class,
								formattedDate);

						int manualVersion = (maxManualVersion != null ? maxManualVersion : 0) + 1;
						int manualRowsInserted = 0;

						// Dynamic Generation of Columns for Manual UI Fields (R11 to R64)
						StringBuilder la2Columns = new StringBuilder();
						for (int i = 11; i <= 64; i++) {
							la2Columns.append("R").append(i).append("_FACTORING_DEBTORS, ");
						}
						for (int i = 11; i <= 64; i++) {
							la2Columns.append("R").append(i).append("_LEASING, ");
						}

						if (maxManualVersion != null && maxManualVersion > 0) {
							// Fetch from PREVIOUS VERSION of the ARCHIVAL table itself
							String manualFinalSql = "INSERT INTO BRRS_M_LA4_ARCHIVALTABLE_SUMMARY2 ("
									+ la2Columns.toString()
									+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE"
									+ ") SELECT " + la2Columns.toString()
									+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
									+ "FROM BRRS_M_LA4_ARCHIVALTABLE_SUMMARY2 "
									+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

							manualRowsInserted = jdbcTemplate.update(manualFinalSql, manualVersion, formattedDate,
									maxManualVersion);
						} else {
							// Fallback option: Fetch from active front-end summary table
							String manualFallbackSql = "INSERT INTO BRRS_M_LA4_ARCHIVALTABLE_SUMMARY2 ("
									+ la2Columns.toString()
									+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE"
									+ ") SELECT " + la2Columns.toString()
									+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
									+ "FROM BRRS_M_LA4_SUMMARYTABLE12 WHERE REPORT_DATE = ?";

							manualRowsInserted = jdbcTemplate.update(manualFallbackSql, manualVersion, formattedDate);
						}

						System.out.println("Manual summary archived successfully into version (" + manualVersion + "): "
								+ manualRowsInserted + " rows.");

						String adsumsql = "DELETE FROM BRRS_M_LA4_SUMMARYTABLE1 WHERE REPORT_DATE = ?";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, formattedDate);
						System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public byte[] BRRS_M_LA4DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  M_LA4 Details...");
			System.out.println("came to Detail download service");

			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {
				byte[] ARCHIVALreport = getM_LA4DetailNewExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LA4DetailsDetail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LA4_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA4_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_LA4 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LA4 Excel", e);
			return new byte[0];
		}
	}

	public byte[] getM_LA4DetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_LA4 ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LA4 Detail NEW");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LA4_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA4_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_LA4 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LA4 NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] BRRS_M_LA4Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.M_LA4");

		// ARCHIVAL check
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LA4ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		if ("email".equalsIgnoreCase(format) && version == null) {
			logger.info("Got format as Email");
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_LA4EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,  version);
		} else {
			// Fetch data

			List<M_LA4_Summary_Entity1> dataList = getDataByDate1(dateformat.parse(todate));
			List<M_LA4_Summary_Entity2> dataList1 = getDataByDate2(dateformat.parse(todate));

			System.out.println("DATA SIZE IS : " + dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  M_LA4 report. Returning empty result.");
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
				throw new SecurityException("Template file exists but is not readable (check permissions): "
						+ templatePath.toAbsolutePath());
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
//NORMAL
				int startRow = 6;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_LA4_Summary_Entity1 record = dataList.get(i);
						M_LA4_Summary_Entity2 record1 = dataList1.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						Cell R12Cell = row.createCell(1);

						if (record1.getREPORT_DATE() != null) {

							R12Cell.setCellValue(record1.getREPORT_DATE());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(11);
						// R12 Col B
						Cell R12Cell1 = row.createCell(1);
						if (record1.getR12FactoringDebtors() != null) {
							R12Cell1.setCellValue(record1.getR12FactoringDebtors().doubleValue());
							R12Cell1.setCellStyle(numberStyle);
						} else {
							R12Cell1.setCellValue("");
							R12Cell1.setCellStyle(textStyle);
						}

						// R12 Col C
						Cell R12Cell2 = row.createCell(2);
						if (record1.getR12Leasing() != null) {
							R12Cell2.setCellValue(record1.getR12Leasing().doubleValue());
							R12Cell2.setCellStyle(numberStyle);
						} else {
							R12Cell2.setCellValue("");
							R12Cell2.setCellStyle(textStyle);
						}
						// R12 Col D
						Cell R12Cell3 = row.createCell(3);
						if (record.getR12Overdrafts() != null) {
							R12Cell3.setCellValue(record.getR12Overdrafts().doubleValue());
							R12Cell3.setCellStyle(numberStyle);
						} else {
							R12Cell3.setCellValue("");
							R12Cell3.setCellStyle(textStyle);
						}

						// R12 Col E
						Cell R12Cell4 = row.createCell(4);
						if (record.getR12OtherInstallmentLoans() != null) {
							R12Cell4.setCellValue(record.getR12OtherInstallmentLoans().doubleValue());
							R12Cell4.setCellStyle(numberStyle);
						} else {
							R12Cell4.setCellValue("");
							R12Cell4.setCellStyle(textStyle);
						}
						// R13 Col B
						row = sheet.getRow(12);
						// R13 Col B
						Cell R13Cell1 = row.createCell(1);
						if (record1.getR13FactoringDebtors() != null) {
							R13Cell1.setCellValue(record1.getR13FactoringDebtors().doubleValue());
							R13Cell1.setCellStyle(numberStyle);
						} else {
							R13Cell1.setCellValue("");
							R13Cell1.setCellStyle(textStyle);
						}

						// R13 Col C
						Cell R13Cell2 = row.createCell(2);
						if (record1.getR13Leasing() != null) {
							R13Cell2.setCellValue(record1.getR13Leasing().doubleValue());
							R13Cell2.setCellStyle(numberStyle);
						} else {
							R13Cell2.setCellValue("");
							R13Cell2.setCellStyle(textStyle);
						}
						// R13 Col D
						Cell R13Cell3 = row.createCell(3);
						if (record.getR13Overdrafts() != null) {
							R13Cell3.setCellValue(record.getR13Overdrafts().doubleValue());
							R13Cell3.setCellStyle(numberStyle);
						} else {
							R13Cell3.setCellValue("");
							R13Cell3.setCellStyle(textStyle);
						}

						// R13 Col E
						Cell R13Cell4 = row.createCell(4);
						if (record.getR13OtherInstallmentLoans() != null) {
							R13Cell4.setCellValue(record.getR13OtherInstallmentLoans().doubleValue());
							R13Cell4.setCellStyle(numberStyle);
						} else {
							R13Cell4.setCellValue("");
							R13Cell4.setCellStyle(textStyle);
						}
						// R14 Col B
						row = sheet.getRow(13); // Row index 13 is Excel Row 14
						// R14 Col B
						Cell R14Cell1 = row.createCell(1);
						if (record1.getR14FactoringDebtors() != null) {
							R14Cell1.setCellValue(record1.getR14FactoringDebtors().doubleValue());
							R14Cell1.setCellStyle(numberStyle);
						} else {
							R14Cell1.setCellValue("");
							R14Cell1.setCellStyle(textStyle);
						}

						// R14 Col C
						Cell R14Cell2 = row.createCell(2);
						if (record1.getR14Leasing() != null) {
							R14Cell2.setCellValue(record1.getR14Leasing().doubleValue());
							R14Cell2.setCellStyle(numberStyle);
						} else {
							R14Cell2.setCellValue("");
							R14Cell2.setCellStyle(textStyle);
						}

						// R14 Col D
						Cell R14Cell3 = row.createCell(3);
						if (record.getR14Overdrafts() != null) {
							R14Cell3.setCellValue(record.getR14Overdrafts().doubleValue());
							R14Cell3.setCellStyle(numberStyle);
						} else {
							R14Cell3.setCellValue("");
							R14Cell3.setCellStyle(textStyle);
						}

						// R14 Col E
						Cell R14Cell4 = row.createCell(4);
						if (record.getR14OtherInstallmentLoans() != null) {
							R14Cell4.setCellValue(record.getR14OtherInstallmentLoans().doubleValue());
							R14Cell4.setCellStyle(numberStyle);
						} else {
							R14Cell4.setCellValue("");
							R14Cell4.setCellStyle(textStyle);
						}

						// --- R16 (Row Index 15) ---
						row = sheet.getRow(15);
						Cell R16Cell1 = row.createCell(1);
						if (record1.getR16FactoringDebtors() != null) {
							R16Cell1.setCellValue(record1.getR16FactoringDebtors().doubleValue());
							R16Cell1.setCellStyle(numberStyle);
						} else {
							R16Cell1.setCellValue("");
							R16Cell1.setCellStyle(textStyle);
						}
						Cell R16Cell2 = row.createCell(2);
						if (record1.getR16Leasing() != null) {
							R16Cell2.setCellValue(record1.getR16Leasing().doubleValue());
							R16Cell2.setCellStyle(numberStyle);
						} else {
							R16Cell2.setCellValue("");
							R16Cell2.setCellStyle(textStyle);
						}
						Cell R16Cell3 = row.createCell(3);
						if (record.getR16Overdrafts() != null) {
							R16Cell3.setCellValue(record.getR16Overdrafts().doubleValue());
							R16Cell3.setCellStyle(numberStyle);
						} else {
							R16Cell3.setCellValue("");
							R16Cell3.setCellStyle(textStyle);
						}
						Cell R16Cell4 = row.createCell(4);
						if (record.getR16OtherInstallmentLoans() != null) {
							R16Cell4.setCellValue(record.getR16OtherInstallmentLoans().doubleValue());
							R16Cell4.setCellStyle(numberStyle);
						} else {
							R16Cell4.setCellValue("");
							R16Cell4.setCellStyle(textStyle);
						}

						// --- R17 (Row Index 16) ---
						row = sheet.getRow(16);
						Cell R17Cell1 = row.createCell(1);
						if (record1.getR17FactoringDebtors() != null) {
							R17Cell1.setCellValue(record1.getR17FactoringDebtors().doubleValue());
							R17Cell1.setCellStyle(numberStyle);
						} else {
							R17Cell1.setCellValue("");
							R17Cell1.setCellStyle(textStyle);
						}
						Cell R17Cell2 = row.createCell(2);
						if (record1.getR17Leasing() != null) {
							R17Cell2.setCellValue(record1.getR17Leasing().doubleValue());
							R17Cell2.setCellStyle(numberStyle);
						} else {
							R17Cell2.setCellValue("");
							R17Cell2.setCellStyle(textStyle);
						}
						Cell R17Cell3 = row.createCell(3);
						if (record.getR17Overdrafts() != null) {
							R17Cell3.setCellValue(record.getR17Overdrafts().doubleValue());
							R17Cell3.setCellStyle(numberStyle);
						} else {
							R17Cell3.setCellValue("");
							R17Cell3.setCellStyle(textStyle);
						}
						Cell R17Cell4 = row.createCell(4);
						if (record.getR17OtherInstallmentLoans() != null) {
							R17Cell4.setCellValue(record.getR17OtherInstallmentLoans().doubleValue());
							R17Cell4.setCellStyle(numberStyle);
						} else {
							R17Cell4.setCellValue("");
							R17Cell4.setCellStyle(textStyle);
						}

						// --- R18 (Row Index 17) ---
						row = sheet.getRow(17);
						Cell R18Cell1 = row.createCell(1);
						if (record1.getR18FactoringDebtors() != null) {
							R18Cell1.setCellValue(record1.getR18FactoringDebtors().doubleValue());
							R18Cell1.setCellStyle(numberStyle);
						} else {
							R18Cell1.setCellValue("");
							R18Cell1.setCellStyle(textStyle);
						}
						Cell R18Cell2 = row.createCell(2);
						if (record1.getR18Leasing() != null) {
							R18Cell2.setCellValue(record1.getR18Leasing().doubleValue());
							R18Cell2.setCellStyle(numberStyle);
						} else {
							R18Cell2.setCellValue("");
							R18Cell2.setCellStyle(textStyle);
						}
						Cell R18Cell3 = row.createCell(3);
						if (record.getR18Overdrafts() != null) {
							R18Cell3.setCellValue(record.getR18Overdrafts().doubleValue());
							R18Cell3.setCellStyle(numberStyle);
						} else {
							R18Cell3.setCellValue("");
							R18Cell3.setCellStyle(textStyle);
						}
						Cell R18Cell4 = row.createCell(4);
						if (record.getR18OtherInstallmentLoans() != null) {
							R18Cell4.setCellValue(record.getR18OtherInstallmentLoans().doubleValue());
							R18Cell4.setCellStyle(numberStyle);
						} else {
							R18Cell4.setCellValue("");
							R18Cell4.setCellStyle(textStyle);
						}

						// --- R19 (Row Index 18) ---
						row = sheet.getRow(18);
						Cell R19Cell1 = row.createCell(1);
						if (record1.getR19FactoringDebtors() != null) {
							R19Cell1.setCellValue(record1.getR19FactoringDebtors().doubleValue());
							R19Cell1.setCellStyle(numberStyle);
						} else {
							R19Cell1.setCellValue("");
							R19Cell1.setCellStyle(textStyle);
						}
						Cell R19Cell2 = row.createCell(2);
						if (record1.getR19Leasing() != null) {
							R19Cell2.setCellValue(record1.getR19Leasing().doubleValue());
							R19Cell2.setCellStyle(numberStyle);
						} else {
							R19Cell2.setCellValue("");
							R19Cell2.setCellStyle(textStyle);
						}
						Cell R19Cell3 = row.createCell(3);
						if (record.getR19Overdrafts() != null) {
							R19Cell3.setCellValue(record.getR19Overdrafts().doubleValue());
							R19Cell3.setCellStyle(numberStyle);
						} else {
							R19Cell3.setCellValue("");
							R19Cell3.setCellStyle(textStyle);
						}
						Cell R19Cell4 = row.createCell(4);
						if (record.getR19OtherInstallmentLoans() != null) {
							R19Cell4.setCellValue(record.getR19OtherInstallmentLoans().doubleValue());
							R19Cell4.setCellStyle(numberStyle);
						} else {
							R19Cell4.setCellValue("");
							R19Cell4.setCellStyle(textStyle);
						}
						// --- R20 (Row Index 19) ---
						row = sheet.getRow(19);
						Cell R20Cell1 = row.createCell(1);
						if (record1.getR20FactoringDebtors() != null) {
							R20Cell1.setCellValue(record1.getR20FactoringDebtors().doubleValue());
							R20Cell1.setCellStyle(numberStyle);
						} else {
							R20Cell1.setCellValue("");
							R20Cell1.setCellStyle(textStyle);
						}
						Cell R20Cell2 = row.createCell(2);
						if (record1.getR20Leasing() != null) {
							R20Cell2.setCellValue(record1.getR20Leasing().doubleValue());
							R20Cell2.setCellStyle(numberStyle);
						} else {
							R20Cell2.setCellValue("");
							R20Cell2.setCellStyle(textStyle);
						}
						Cell R20Cell3 = row.createCell(3);
						if (record.getR20Overdrafts() != null) {
							R20Cell3.setCellValue(record.getR20Overdrafts().doubleValue());
							R20Cell3.setCellStyle(numberStyle);
						} else {
							R20Cell3.setCellValue("");
							R20Cell3.setCellStyle(textStyle);
						}
						Cell R20Cell4 = row.createCell(4);
						if (record.getR20OtherInstallmentLoans() != null) {
							R20Cell4.setCellValue(record.getR20OtherInstallmentLoans().doubleValue());
							R20Cell4.setCellStyle(numberStyle);
						} else {
							R20Cell4.setCellValue("");
							R20Cell4.setCellStyle(textStyle);
						}

						// --- R21 (Row Index 20) ---
						row = sheet.getRow(20);
						Cell R21Cell1 = row.createCell(1);
						if (record1.getR21FactoringDebtors() != null) {
							R21Cell1.setCellValue(record1.getR21FactoringDebtors().doubleValue());
							R21Cell1.setCellStyle(numberStyle);
						} else {
							R21Cell1.setCellValue("");
							R21Cell1.setCellStyle(textStyle);
						}
						Cell R21Cell2 = row.createCell(2);
						if (record1.getR21Leasing() != null) {
							R21Cell2.setCellValue(record1.getR21Leasing().doubleValue());
							R21Cell2.setCellStyle(numberStyle);
						} else {
							R21Cell2.setCellValue("");
							R21Cell2.setCellStyle(textStyle);
						}
						Cell R21Cell3 = row.createCell(3);
						if (record.getR21Overdrafts() != null) {
							R21Cell3.setCellValue(record.getR21Overdrafts().doubleValue());
							R21Cell3.setCellStyle(numberStyle);
						} else {
							R21Cell3.setCellValue("");
							R21Cell3.setCellStyle(textStyle);
						}
						Cell R21Cell4 = row.createCell(4);
						if (record.getR21OtherInstallmentLoans() != null) {
							R21Cell4.setCellValue(record.getR21OtherInstallmentLoans().doubleValue());
							R21Cell4.setCellStyle(numberStyle);
						} else {
							R21Cell4.setCellValue("");
							R21Cell4.setCellStyle(textStyle);
						}

						// --- R22 (Row Index 21) ---
						row = sheet.getRow(21);
						Cell R22Cell1 = row.createCell(1);
						if (record1.getR22FactoringDebtors() != null) {
							R22Cell1.setCellValue(record1.getR22FactoringDebtors().doubleValue());
							R22Cell1.setCellStyle(numberStyle);
						} else {
							R22Cell1.setCellValue("");
							R22Cell1.setCellStyle(textStyle);
						}
						Cell R22Cell2 = row.createCell(2);
						if (record1.getR22Leasing() != null) {
							R22Cell2.setCellValue(record1.getR22Leasing().doubleValue());
							R22Cell2.setCellStyle(numberStyle);
						} else {
							R22Cell2.setCellValue("");
							R22Cell2.setCellStyle(textStyle);
						}
						Cell R22Cell3 = row.createCell(3);
						if (record.getR22Overdrafts() != null) {
							R22Cell3.setCellValue(record.getR22Overdrafts().doubleValue());
							R22Cell3.setCellStyle(numberStyle);
						} else {
							R22Cell3.setCellValue("");
							R22Cell3.setCellStyle(textStyle);
						}
						Cell R22Cell4 = row.createCell(4);
						if (record.getR22OtherInstallmentLoans() != null) {
							R22Cell4.setCellValue(record.getR22OtherInstallmentLoans().doubleValue());
							R22Cell4.setCellStyle(numberStyle);
						} else {
							R22Cell4.setCellValue("");
							R22Cell4.setCellStyle(textStyle);
						}

						// --- R23 (Row Index 22) ---
						row = sheet.getRow(22);
						Cell R23Cell1 = row.createCell(1);
						if (record1.getR23FactoringDebtors() != null) {
							R23Cell1.setCellValue(record1.getR23FactoringDebtors().doubleValue());
							R23Cell1.setCellStyle(numberStyle);
						} else {
							R23Cell1.setCellValue("");
							R23Cell1.setCellStyle(textStyle);
						}
						Cell R23Cell2 = row.createCell(2);
						if (record1.getR23Leasing() != null) {
							R23Cell2.setCellValue(record1.getR23Leasing().doubleValue());
							R23Cell2.setCellStyle(numberStyle);
						} else {
							R23Cell2.setCellValue("");
							R23Cell2.setCellStyle(textStyle);
						}
						Cell R23Cell3 = row.createCell(3);
						if (record.getR23Overdrafts() != null) {
							R23Cell3.setCellValue(record.getR23Overdrafts().doubleValue());
							R23Cell3.setCellStyle(numberStyle);
						} else {
							R23Cell3.setCellValue("");
							R23Cell3.setCellStyle(textStyle);
						}
						Cell R23Cell4 = row.createCell(4);
						if (record.getR23OtherInstallmentLoans() != null) {
							R23Cell4.setCellValue(record.getR23OtherInstallmentLoans().doubleValue());
							R23Cell4.setCellStyle(numberStyle);
						} else {
							R23Cell4.setCellValue("");
							R23Cell4.setCellStyle(textStyle);
						}

						// --- R24 (Row Index 23) ---
						row = sheet.getRow(23);
						Cell R24Cell1 = row.createCell(1);
						if (record1.getR24FactoringDebtors() != null) {
							R24Cell1.setCellValue(record1.getR24FactoringDebtors().doubleValue());
							R24Cell1.setCellStyle(numberStyle);
						} else {
							R24Cell1.setCellValue("");
							R24Cell1.setCellStyle(textStyle);
						}
						Cell R24Cell2 = row.createCell(2);
						if (record1.getR24Leasing() != null) {
							R24Cell2.setCellValue(record1.getR24Leasing().doubleValue());
							R24Cell2.setCellStyle(numberStyle);
						} else {
							R24Cell2.setCellValue("");
							R24Cell2.setCellStyle(textStyle);
						}
						Cell R24Cell3 = row.createCell(3);
						if (record.getR24Overdrafts() != null) {
							R24Cell3.setCellValue(record.getR24Overdrafts().doubleValue());
							R24Cell3.setCellStyle(numberStyle);
						} else {
							R24Cell3.setCellValue("");
							R24Cell3.setCellStyle(textStyle);
						}
						Cell R24Cell4 = row.createCell(4);
						if (record.getR24OtherInstallmentLoans() != null) {
							R24Cell4.setCellValue(record.getR24OtherInstallmentLoans().doubleValue());
							R24Cell4.setCellStyle(numberStyle);
						} else {
							R24Cell4.setCellValue("");
							R24Cell4.setCellStyle(textStyle);
						}

						// --- R25 (Row Index 24) ---
						row = sheet.getRow(24);
						Cell R25Cell1 = row.createCell(1);
						if (record1.getR25FactoringDebtors() != null) {
							R25Cell1.setCellValue(record1.getR25FactoringDebtors().doubleValue());
							R25Cell1.setCellStyle(numberStyle);
						} else {
							R25Cell1.setCellValue("");
							R25Cell1.setCellStyle(textStyle);
						}
						Cell R25Cell2 = row.createCell(2);
						if (record1.getR25Leasing() != null) {
							R25Cell2.setCellValue(record1.getR25Leasing().doubleValue());
							R25Cell2.setCellStyle(numberStyle);
						} else {
							R25Cell2.setCellValue("");
							R25Cell2.setCellStyle(textStyle);
						}
						Cell R25Cell3 = row.createCell(3);
						if (record.getR25Overdrafts() != null) {
							R25Cell3.setCellValue(record.getR25Overdrafts().doubleValue());
							R25Cell3.setCellStyle(numberStyle);
						} else {
							R25Cell3.setCellValue("");
							R25Cell3.setCellStyle(textStyle);
						}
						Cell R25Cell4 = row.createCell(4);
						if (record.getR25OtherInstallmentLoans() != null) {
							R25Cell4.setCellValue(record.getR25OtherInstallmentLoans().doubleValue());
							R25Cell4.setCellStyle(numberStyle);
						} else {
							R25Cell4.setCellValue("");
							R25Cell4.setCellStyle(textStyle);
						}

						// --- R26 (Row Index 25) ---
						row = sheet.getRow(25);
						Cell R26Cell1 = row.createCell(1);
						if (record1.getR26FactoringDebtors() != null) {
							R26Cell1.setCellValue(record1.getR26FactoringDebtors().doubleValue());
							R26Cell1.setCellStyle(numberStyle);
						} else {
							R26Cell1.setCellValue("");
							R26Cell1.setCellStyle(textStyle);
						}
						Cell R26Cell2 = row.createCell(2);
						if (record1.getR26Leasing() != null) {
							R26Cell2.setCellValue(record1.getR26Leasing().doubleValue());
							R26Cell2.setCellStyle(numberStyle);
						} else {
							R26Cell2.setCellValue("");
							R26Cell2.setCellStyle(textStyle);
						}
						Cell R26Cell3 = row.createCell(3);
						if (record.getR26Overdrafts() != null) {
							R26Cell3.setCellValue(record.getR26Overdrafts().doubleValue());
							R26Cell3.setCellStyle(numberStyle);
						} else {
							R26Cell3.setCellValue("");
							R26Cell3.setCellStyle(textStyle);
						}
						Cell R26Cell4 = row.createCell(4);
						if (record.getR26OtherInstallmentLoans() != null) {
							R26Cell4.setCellValue(record.getR26OtherInstallmentLoans().doubleValue());
							R26Cell4.setCellStyle(numberStyle);
						} else {
							R26Cell4.setCellValue("");
							R26Cell4.setCellStyle(textStyle);
						}

						// --- R27 (Row Index 26) ---
						row = sheet.getRow(26);
						Cell R27Cell1 = row.createCell(1);
						if (record1.getR27FactoringDebtors() != null) {
							R27Cell1.setCellValue(record1.getR27FactoringDebtors().doubleValue());
							R27Cell1.setCellStyle(numberStyle);
						} else {
							R27Cell1.setCellValue("");
							R27Cell1.setCellStyle(textStyle);
						}
						Cell R27Cell2 = row.createCell(2);
						if (record1.getR27Leasing() != null) {
							R27Cell2.setCellValue(record1.getR27Leasing().doubleValue());
							R27Cell2.setCellStyle(numberStyle);
						} else {
							R27Cell2.setCellValue("");
							R27Cell2.setCellStyle(textStyle);
						}
						Cell R27Cell3 = row.createCell(3);
						if (record.getR27Overdrafts() != null) {
							R27Cell3.setCellValue(record.getR27Overdrafts().doubleValue());
							R27Cell3.setCellStyle(numberStyle);
						} else {
							R27Cell3.setCellValue("");
							R27Cell3.setCellStyle(textStyle);
						}
						Cell R27Cell4 = row.createCell(4);
						if (record.getR27OtherInstallmentLoans() != null) {
							R27Cell4.setCellValue(record.getR27OtherInstallmentLoans().doubleValue());
							R27Cell4.setCellStyle(numberStyle);
						} else {
							R27Cell4.setCellValue("");
							R27Cell4.setCellStyle(textStyle);
						}

						// --- R28 (Row Index 27) ---
						row = sheet.getRow(27);
						Cell R28Cell1 = row.createCell(1);
						if (record1.getR28FactoringDebtors() != null) {
							R28Cell1.setCellValue(record1.getR28FactoringDebtors().doubleValue());
							R28Cell1.setCellStyle(numberStyle);
						} else {
							R28Cell1.setCellValue("");
							R28Cell1.setCellStyle(textStyle);
						}
						Cell R28Cell2 = row.createCell(2);
						if (record1.getR28Leasing() != null) {
							R28Cell2.setCellValue(record1.getR28Leasing().doubleValue());
							R28Cell2.setCellStyle(numberStyle);
						} else {
							R28Cell2.setCellValue("");
							R28Cell2.setCellStyle(textStyle);
						}
						Cell R28Cell3 = row.createCell(3);
						if (record.getR28Overdrafts() != null) {
							R28Cell3.setCellValue(record.getR28Overdrafts().doubleValue());
							R28Cell3.setCellStyle(numberStyle);
						} else {
							R28Cell3.setCellValue("");
							R28Cell3.setCellStyle(textStyle);
						}
						Cell R28Cell4 = row.createCell(4);
						if (record.getR28OtherInstallmentLoans() != null) {
							R28Cell4.setCellValue(record.getR28OtherInstallmentLoans().doubleValue());
							R28Cell4.setCellStyle(numberStyle);
						} else {
							R28Cell4.setCellValue("");
							R28Cell4.setCellStyle(textStyle);
						}

						// --- R30 (Row Index 29) ---
						row = sheet.getRow(29);
						Cell R30Cell1 = row.createCell(1);
						if (record1.getR30FactoringDebtors() != null) {
							R30Cell1.setCellValue(record1.getR30FactoringDebtors().doubleValue());
							R30Cell1.setCellStyle(numberStyle);
						} else {
							R30Cell1.setCellValue("");
							R30Cell1.setCellStyle(textStyle);
						}
						Cell R30Cell2 = row.createCell(2);
						if (record1.getR30Leasing() != null) {
							R30Cell2.setCellValue(record1.getR30Leasing().doubleValue());
							R30Cell2.setCellStyle(numberStyle);
						} else {
							R30Cell2.setCellValue("");
							R30Cell2.setCellStyle(textStyle);
						}
						Cell R30Cell3 = row.createCell(3);
						if (record.getR30Overdrafts() != null) {
							R30Cell3.setCellValue(record.getR30Overdrafts().doubleValue());
							R30Cell3.setCellStyle(numberStyle);
						} else {
							R30Cell3.setCellValue("");
							R30Cell3.setCellStyle(textStyle);
						}
						Cell R30Cell4 = row.createCell(4);
						if (record.getR30OtherInstallmentLoans() != null) {
							R30Cell4.setCellValue(record.getR30OtherInstallmentLoans().doubleValue());
							R30Cell4.setCellStyle(numberStyle);
						} else {
							R30Cell4.setCellValue("");
							R30Cell4.setCellStyle(textStyle);
						}

						// --- R31 (Row Index 30) ---
						row = sheet.getRow(30);
						Cell R31Cell1 = row.createCell(1);
						if (record1.getR31FactoringDebtors() != null) {
							R31Cell1.setCellValue(record1.getR31FactoringDebtors().doubleValue());
							R31Cell1.setCellStyle(numberStyle);
						} else {
							R31Cell1.setCellValue("");
							R31Cell1.setCellStyle(textStyle);
						}
						Cell R31Cell2 = row.createCell(2);
						if (record1.getR31Leasing() != null) {
							R31Cell2.setCellValue(record1.getR31Leasing().doubleValue());
							R31Cell2.setCellStyle(numberStyle);
						} else {
							R31Cell2.setCellValue("");
							R31Cell2.setCellStyle(textStyle);
						}
						Cell R31Cell3 = row.createCell(3);
						if (record.getR31Overdrafts() != null) {
							R31Cell3.setCellValue(record.getR31Overdrafts().doubleValue());
							R31Cell3.setCellStyle(numberStyle);
						} else {
							R31Cell3.setCellValue("");
							R31Cell3.setCellStyle(textStyle);
						}
						Cell R31Cell4 = row.createCell(4);
						if (record.getR31OtherInstallmentLoans() != null) {
							R31Cell4.setCellValue(record.getR31OtherInstallmentLoans().doubleValue());
							R31Cell4.setCellStyle(numberStyle);
						} else {
							R31Cell4.setCellValue("");
							R31Cell4.setCellStyle(textStyle);
						}

						// --- R32 (Row Index 31) ---
						row = sheet.getRow(31);
						Cell R32Cell1 = row.createCell(1);
						if (record1.getR32FactoringDebtors() != null) {
							R32Cell1.setCellValue(record1.getR32FactoringDebtors().doubleValue());
							R32Cell1.setCellStyle(numberStyle);
						} else {
							R32Cell1.setCellValue("");
							R32Cell1.setCellStyle(textStyle);
						}
						Cell R32Cell2 = row.createCell(2);
						if (record1.getR32Leasing() != null) {
							R32Cell2.setCellValue(record1.getR32Leasing().doubleValue());
							R32Cell2.setCellStyle(numberStyle);
						} else {
							R32Cell2.setCellValue("");
							R32Cell2.setCellStyle(textStyle);
						}
						Cell R32Cell3 = row.createCell(3);
						if (record.getR32Overdrafts() != null) {
							R32Cell3.setCellValue(record.getR32Overdrafts().doubleValue());
							R32Cell3.setCellStyle(numberStyle);
						} else {
							R32Cell3.setCellValue("");
							R32Cell3.setCellStyle(textStyle);
						}
						Cell R32Cell4 = row.createCell(4);
						if (record.getR32OtherInstallmentLoans() != null) {
							R32Cell4.setCellValue(record.getR32OtherInstallmentLoans().doubleValue());
							R32Cell4.setCellStyle(numberStyle);
						} else {
							R32Cell4.setCellValue("");
							R32Cell4.setCellStyle(textStyle);
						}

						// --- R33 (Row Index 32) ---
						row = sheet.getRow(32);
						Cell R33Cell1 = row.createCell(1);
						if (record1.getR33FactoringDebtors() != null) {
							R33Cell1.setCellValue(record1.getR33FactoringDebtors().doubleValue());
							R33Cell1.setCellStyle(numberStyle);
						} else {
							R33Cell1.setCellValue("");
							R33Cell1.setCellStyle(textStyle);
						}
						Cell R33Cell2 = row.createCell(2);
						if (record1.getR33Leasing() != null) {
							R33Cell2.setCellValue(record1.getR33Leasing().doubleValue());
							R33Cell2.setCellStyle(numberStyle);
						} else {
							R33Cell2.setCellValue("");
							R33Cell2.setCellStyle(textStyle);
						}
						Cell R33Cell3 = row.createCell(3);
						if (record.getR33Overdrafts() != null) {
							R33Cell3.setCellValue(record.getR33Overdrafts().doubleValue());
							R33Cell3.setCellStyle(numberStyle);
						} else {
							R33Cell3.setCellValue("");
							R33Cell3.setCellStyle(textStyle);
						}
						Cell R33Cell4 = row.createCell(4);
						if (record.getR33OtherInstallmentLoans() != null) {
							R33Cell4.setCellValue(record.getR33OtherInstallmentLoans().doubleValue());
							R33Cell4.setCellStyle(numberStyle);
						} else {
							R33Cell4.setCellValue("");
							R33Cell4.setCellStyle(textStyle);
						}

						// --- R34 (Row Index 33) ---
						row = sheet.getRow(33);
						Cell R34Cell1 = row.createCell(1);
						if (record1.getR34FactoringDebtors() != null) {
							R34Cell1.setCellValue(record1.getR34FactoringDebtors().doubleValue());
							R34Cell1.setCellStyle(numberStyle);
						} else {
							R34Cell1.setCellValue("");
							R34Cell1.setCellStyle(textStyle);
						}
						Cell R34Cell2 = row.createCell(2);
						if (record1.getR34Leasing() != null) {
							R34Cell2.setCellValue(record1.getR34Leasing().doubleValue());
							R34Cell2.setCellStyle(numberStyle);
						} else {
							R34Cell2.setCellValue("");
							R34Cell2.setCellStyle(textStyle);
						}
						Cell R34Cell3 = row.createCell(3);
						if (record.getR34Overdrafts() != null) {
							R34Cell3.setCellValue(record.getR34Overdrafts().doubleValue());
							R34Cell3.setCellStyle(numberStyle);
						} else {
							R34Cell3.setCellValue("");
							R34Cell3.setCellStyle(textStyle);
						}
						Cell R34Cell4 = row.createCell(4);
						if (record.getR34OtherInstallmentLoans() != null) {
							R34Cell4.setCellValue(record.getR34OtherInstallmentLoans().doubleValue());
							R34Cell4.setCellStyle(numberStyle);
						} else {
							R34Cell4.setCellValue("");
							R34Cell4.setCellStyle(textStyle);
						}

						// --- R35 (Row Index 34) ---
						row = sheet.getRow(34);
						Cell R35Cell1 = row.createCell(1);
						if (record1.getR35FactoringDebtors() != null) {
							R35Cell1.setCellValue(record1.getR35FactoringDebtors().doubleValue());
							R35Cell1.setCellStyle(numberStyle);
						} else {
							R35Cell1.setCellValue("");
							R35Cell1.setCellStyle(textStyle);
						}
						Cell R35Cell2 = row.createCell(2);
						if (record1.getR35Leasing() != null) {
							R35Cell2.setCellValue(record1.getR35Leasing().doubleValue());
							R35Cell2.setCellStyle(numberStyle);
						} else {
							R35Cell2.setCellValue("");
							R35Cell2.setCellStyle(textStyle);
						}
						Cell R35Cell3 = row.createCell(3);
						if (record.getR35Overdrafts() != null) {
							R35Cell3.setCellValue(record.getR35Overdrafts().doubleValue());
							R35Cell3.setCellStyle(numberStyle);
						} else {
							R35Cell3.setCellValue("");
							R35Cell3.setCellStyle(textStyle);
						}
						Cell R35Cell4 = row.createCell(4);
						if (record.getR35OtherInstallmentLoans() != null) {
							R35Cell4.setCellValue(record.getR35OtherInstallmentLoans().doubleValue());
							R35Cell4.setCellStyle(numberStyle);
						} else {
							R35Cell4.setCellValue("");
							R35Cell4.setCellStyle(textStyle);
						}

						// --- R36 (Row Index 35) ---
						row = sheet.getRow(35);
						Cell R36Cell1 = row.createCell(1);
						if (record1.getR36FactoringDebtors() != null) {
							R36Cell1.setCellValue(record1.getR36FactoringDebtors().doubleValue());
							R36Cell1.setCellStyle(numberStyle);
						} else {
							R36Cell1.setCellValue("");
							R36Cell1.setCellStyle(textStyle);
						}
						Cell R36Cell2 = row.createCell(2);
						if (record1.getR36Leasing() != null) {
							R36Cell2.setCellValue(record1.getR36Leasing().doubleValue());
							R36Cell2.setCellStyle(numberStyle);
						} else {
							R36Cell2.setCellValue("");
							R36Cell2.setCellStyle(textStyle);
						}
						Cell R36Cell3 = row.createCell(3);
						if (record.getR36Overdrafts() != null) {
							R36Cell3.setCellValue(record.getR36Overdrafts().doubleValue());
							R36Cell3.setCellStyle(numberStyle);
						} else {
							R36Cell3.setCellValue("");
							R36Cell3.setCellStyle(textStyle);
						}
						Cell R36Cell4 = row.createCell(4);
						if (record.getR36OtherInstallmentLoans() != null) {
							R36Cell4.setCellValue(record.getR36OtherInstallmentLoans().doubleValue());
							R36Cell4.setCellStyle(numberStyle);
						} else {
							R36Cell4.setCellValue("");
							R36Cell4.setCellStyle(textStyle);
						}

						// --- R37 (Row Index 36) ---
						row = sheet.getRow(36);
						Cell R37Cell1 = row.createCell(1);
						if (record1.getR37FactoringDebtors() != null) {
							R37Cell1.setCellValue(record1.getR37FactoringDebtors().doubleValue());
							R37Cell1.setCellStyle(numberStyle);
						} else {
							R37Cell1.setCellValue("");
							R37Cell1.setCellStyle(textStyle);
						}
						Cell R37Cell2 = row.createCell(2);
						if (record1.getR37Leasing() != null) {
							R37Cell2.setCellValue(record1.getR37Leasing().doubleValue());
							R37Cell2.setCellStyle(numberStyle);
						} else {
							R37Cell2.setCellValue("");
							R37Cell2.setCellStyle(textStyle);
						}
						Cell R37Cell3 = row.createCell(3);
						if (record.getR37Overdrafts() != null) {
							R37Cell3.setCellValue(record.getR37Overdrafts().doubleValue());
							R37Cell3.setCellStyle(numberStyle);
						} else {
							R37Cell3.setCellValue("");
							R37Cell3.setCellStyle(textStyle);
						}
						Cell R37Cell4 = row.createCell(4);
						if (record.getR37OtherInstallmentLoans() != null) {
							R37Cell4.setCellValue(record.getR37OtherInstallmentLoans().doubleValue());
							R37Cell4.setCellStyle(numberStyle);
						} else {
							R37Cell4.setCellValue("");
							R37Cell4.setCellStyle(textStyle);
						}
						// --- R39 (Row Index 38) ---
						row = sheet.getRow(38);
						Cell R39Cell1 = row.createCell(1);
						if (record1.getR39FactoringDebtors() != null) {
							R39Cell1.setCellValue(record1.getR39FactoringDebtors().doubleValue());
							R39Cell1.setCellStyle(numberStyle);
						} else {
							R39Cell1.setCellValue("");
							R39Cell1.setCellStyle(textStyle);
						}
						Cell R39Cell2 = row.createCell(2);
						if (record1.getR39Leasing() != null) {
							R39Cell2.setCellValue(record1.getR39Leasing().doubleValue());
							R39Cell2.setCellStyle(numberStyle);
						} else {
							R39Cell2.setCellValue("");
							R39Cell2.setCellStyle(textStyle);
						}
						Cell R39Cell3 = row.createCell(3);
						if (record.getR39Overdrafts() != null) {
							R39Cell3.setCellValue(record.getR39Overdrafts().doubleValue());
							R39Cell3.setCellStyle(numberStyle);
						} else {
							R39Cell3.setCellValue("");
							R39Cell3.setCellStyle(textStyle);
						}
						Cell R39Cell4 = row.createCell(4);
						if (record.getR39OtherInstallmentLoans() != null) {
							R39Cell4.setCellValue(record.getR39OtherInstallmentLoans().doubleValue());
							R39Cell4.setCellStyle(numberStyle);
						} else {
							R39Cell4.setCellValue("");
							R39Cell4.setCellStyle(textStyle);
						}

						// --- R40 (Row Index 39) ---
						row = sheet.getRow(39);
						Cell R40Cell1 = row.createCell(1);
						if (record1.getR40FactoringDebtors() != null) {
							R40Cell1.setCellValue(record1.getR40FactoringDebtors().doubleValue());
							R40Cell1.setCellStyle(numberStyle);
						} else {
							R40Cell1.setCellValue("");
							R40Cell1.setCellStyle(textStyle);
						}
						Cell R40Cell2 = row.createCell(2);
						if (record1.getR40Leasing() != null) {
							R40Cell2.setCellValue(record1.getR40Leasing().doubleValue());
							R40Cell2.setCellStyle(numberStyle);
						} else {
							R40Cell2.setCellValue("");
							R40Cell2.setCellStyle(textStyle);
						}
						Cell R40Cell3 = row.createCell(3);
						if (record.getR40Overdrafts() != null) {
							R40Cell3.setCellValue(record.getR40Overdrafts().doubleValue());
							R40Cell3.setCellStyle(numberStyle);
						} else {
							R40Cell3.setCellValue("");
							R40Cell3.setCellStyle(textStyle);
						}
						Cell R40Cell4 = row.createCell(4);
						if (record.getR40OtherInstallmentLoans() != null) {
							R40Cell4.setCellValue(record.getR40OtherInstallmentLoans().doubleValue());
							R40Cell4.setCellStyle(numberStyle);
						} else {
							R40Cell4.setCellValue("");
							R40Cell4.setCellStyle(textStyle);
						}
						// --- R42 (Row Index 41) ---
						row = sheet.getRow(41);
						Cell R42Cell1 = row.createCell(1);
						if (record1.getR42FactoringDebtors() != null) {
							R42Cell1.setCellValue(record1.getR42FactoringDebtors().doubleValue());
							R42Cell1.setCellStyle(numberStyle);
						} else {
							R42Cell1.setCellValue("");
							R42Cell1.setCellStyle(textStyle);
						}
						Cell R42Cell2 = row.createCell(2);
						if (record1.getR42Leasing() != null) {
							R42Cell2.setCellValue(record1.getR42Leasing().doubleValue());
							R42Cell2.setCellStyle(numberStyle);
						} else {
							R42Cell2.setCellValue("");
							R42Cell2.setCellStyle(textStyle);
						}
						Cell R42Cell3 = row.createCell(3);
						if (record.getR42Overdrafts() != null) {
							R42Cell3.setCellValue(record.getR42Overdrafts().doubleValue());
							R42Cell3.setCellStyle(numberStyle);
						} else {
							R42Cell3.setCellValue("");
							R42Cell3.setCellStyle(textStyle);
						}
						Cell R42Cell4 = row.createCell(4);
						if (record.getR42OtherInstallmentLoans() != null) {
							R42Cell4.setCellValue(record.getR42OtherInstallmentLoans().doubleValue());
							R42Cell4.setCellStyle(numberStyle);
						} else {
							R42Cell4.setCellValue("");
							R42Cell4.setCellStyle(textStyle);
						}

						// --- R43 (Row Index 42) ---
						row = sheet.getRow(42);
						Cell R43Cell1 = row.createCell(1);
						if (record1.getR43FactoringDebtors() != null) {
							R43Cell1.setCellValue(record1.getR43FactoringDebtors().doubleValue());
							R43Cell1.setCellStyle(numberStyle);
						} else {
							R43Cell1.setCellValue("");
							R43Cell1.setCellStyle(textStyle);
						}
						Cell R43Cell2 = row.createCell(2);
						if (record1.getR43Leasing() != null) {
							R43Cell2.setCellValue(record1.getR43Leasing().doubleValue());
							R43Cell2.setCellStyle(numberStyle);
						} else {
							R43Cell2.setCellValue("");
							R43Cell2.setCellStyle(textStyle);
						}
						Cell R43Cell3 = row.createCell(3);
						if (record.getR43Overdrafts() != null) {
							R43Cell3.setCellValue(record.getR43Overdrafts().doubleValue());
							R43Cell3.setCellStyle(numberStyle);
						} else {
							R43Cell3.setCellValue("");
							R43Cell3.setCellStyle(textStyle);
						}
						Cell R43Cell4 = row.createCell(4);
						if (record.getR43OtherInstallmentLoans() != null) {
							R43Cell4.setCellValue(record.getR43OtherInstallmentLoans().doubleValue());
							R43Cell4.setCellStyle(numberStyle);
						} else {
							R43Cell4.setCellValue("");
							R43Cell4.setCellStyle(textStyle);
						}
						// --- R45 (Row Index 44) ---
						row = sheet.getRow(44);
						Cell R45Cell1 = row.createCell(1);
						if (record1.getR45FactoringDebtors() != null) {
							R45Cell1.setCellValue(record1.getR45FactoringDebtors().doubleValue());
							R45Cell1.setCellStyle(numberStyle);
						} else {
							R45Cell1.setCellValue("");
							R45Cell1.setCellStyle(textStyle);
						}
						Cell R45Cell2 = row.createCell(2);
						if (record1.getR45Leasing() != null) {
							R45Cell2.setCellValue(record1.getR45Leasing().doubleValue());
							R45Cell2.setCellStyle(numberStyle);
						} else {
							R45Cell2.setCellValue("");
							R45Cell2.setCellStyle(textStyle);
						}
						Cell R45Cell3 = row.createCell(3);
						if (record.getR45Overdrafts() != null) {
							R45Cell3.setCellValue(record.getR45Overdrafts().doubleValue());
							R45Cell3.setCellStyle(numberStyle);
						} else {
							R45Cell3.setCellValue("");
							R45Cell3.setCellStyle(textStyle);
						}
						Cell R45Cell4 = row.createCell(4);
						if (record.getR45OtherInstallmentLoans() != null) {
							R45Cell4.setCellValue(record.getR45OtherInstallmentLoans().doubleValue());
							R45Cell4.setCellStyle(numberStyle);
						} else {
							R45Cell4.setCellValue("");
							R45Cell4.setCellStyle(textStyle);
						}

						// --- R46 (Row Index 45) ---
						row = sheet.getRow(45);
						Cell R46Cell1 = row.createCell(1);
						if (record1.getR46FactoringDebtors() != null) {
							R46Cell1.setCellValue(record1.getR46FactoringDebtors().doubleValue());
							R46Cell1.setCellStyle(numberStyle);
						} else {
							R46Cell1.setCellValue("");
							R46Cell1.setCellStyle(textStyle);
						}
						Cell R46Cell2 = row.createCell(2);
						if (record1.getR46Leasing() != null) {
							R46Cell2.setCellValue(record1.getR46Leasing().doubleValue());
							R46Cell2.setCellStyle(numberStyle);
						} else {
							R46Cell2.setCellValue("");
							R46Cell2.setCellStyle(textStyle);
						}
						Cell R46Cell3 = row.createCell(3);
						if (record.getR46Overdrafts() != null) {
							R46Cell3.setCellValue(record.getR46Overdrafts().doubleValue());
							R46Cell3.setCellStyle(numberStyle);
						} else {
							R46Cell3.setCellValue("");
							R46Cell3.setCellStyle(textStyle);
						}
						Cell R46Cell4 = row.createCell(4);
						if (record.getR46OtherInstallmentLoans() != null) {
							R46Cell4.setCellValue(record.getR46OtherInstallmentLoans().doubleValue());
							R46Cell4.setCellStyle(numberStyle);
						} else {
							R46Cell4.setCellValue("");
							R46Cell4.setCellStyle(textStyle);
						}

						// --- R47 (Row Index 46) ---
						row = sheet.getRow(46);
						Cell R47Cell1 = row.createCell(1);
						if (record1.getR47FactoringDebtors() != null) {
							R47Cell1.setCellValue(record1.getR47FactoringDebtors().doubleValue());
							R47Cell1.setCellStyle(numberStyle);
						} else {
							R47Cell1.setCellValue("");
							R47Cell1.setCellStyle(textStyle);
						}
						Cell R47Cell2 = row.createCell(2);
						if (record1.getR47Leasing() != null) {
							R47Cell2.setCellValue(record1.getR47Leasing().doubleValue());
							R47Cell2.setCellStyle(numberStyle);
						} else {
							R47Cell2.setCellValue("");
							R47Cell2.setCellStyle(textStyle);
						}
						Cell R47Cell3 = row.createCell(3);
						if (record.getR47Overdrafts() != null) {
							R47Cell3.setCellValue(record.getR47Overdrafts().doubleValue());
							R47Cell3.setCellStyle(numberStyle);
						} else {
							R47Cell3.setCellValue("");
							R47Cell3.setCellStyle(textStyle);
						}
						Cell R47Cell4 = row.createCell(4);
						if (record.getR47OtherInstallmentLoans() != null) {
							R47Cell4.setCellValue(record.getR47OtherInstallmentLoans().doubleValue());
							R47Cell4.setCellStyle(numberStyle);
						} else {
							R47Cell4.setCellValue("");
							R47Cell4.setCellStyle(textStyle);
						}

						// --- R48 (Row Index 47) ---
						row = sheet.getRow(47);
						Cell R48Cell1 = row.createCell(1);
						if (record1.getR48FactoringDebtors() != null) {
							R48Cell1.setCellValue(record1.getR48FactoringDebtors().doubleValue());
							R48Cell1.setCellStyle(numberStyle);
						} else {
							R48Cell1.setCellValue("");
							R48Cell1.setCellStyle(textStyle);
						}
						Cell R48Cell2 = row.createCell(2);
						if (record1.getR48Leasing() != null) {
							R48Cell2.setCellValue(record1.getR48Leasing().doubleValue());
							R48Cell2.setCellStyle(numberStyle);
						} else {
							R48Cell2.setCellValue("");
							R48Cell2.setCellStyle(textStyle);
						}
						Cell R48Cell3 = row.createCell(3);
						if (record.getR48Overdrafts() != null) {
							R48Cell3.setCellValue(record.getR48Overdrafts().doubleValue());
							R48Cell3.setCellStyle(numberStyle);
						} else {
							R48Cell3.setCellValue("");
							R48Cell3.setCellStyle(textStyle);
						}
						Cell R48Cell4 = row.createCell(4);
						if (record.getR48OtherInstallmentLoans() != null) {
							R48Cell4.setCellValue(record.getR48OtherInstallmentLoans().doubleValue());
							R48Cell4.setCellStyle(numberStyle);
						} else {
							R48Cell4.setCellValue("");
							R48Cell4.setCellStyle(textStyle);
						}

						// --- R50 (Row Index 49) ---
						row = sheet.getRow(49);
						Cell R50Cell1 = row.createCell(1);
						if (record1.getR50FactoringDebtors() != null) {
							R50Cell1.setCellValue(record1.getR50FactoringDebtors().doubleValue());
							R50Cell1.setCellStyle(numberStyle);
						} else {
							R50Cell1.setCellValue("");
							R50Cell1.setCellStyle(textStyle);
						}
						Cell R50Cell2 = row.createCell(2);
						if (record1.getR50Leasing() != null) {
							R50Cell2.setCellValue(record1.getR50Leasing().doubleValue());
							R50Cell2.setCellStyle(numberStyle);
						} else {
							R50Cell2.setCellValue("");
							R50Cell2.setCellStyle(textStyle);
						}
						Cell R50Cell3 = row.createCell(3);
						if (record.getR50Overdrafts() != null) {
							R50Cell3.setCellValue(record.getR50Overdrafts().doubleValue());
							R50Cell3.setCellStyle(numberStyle);
						} else {
							R50Cell3.setCellValue("");
							R50Cell3.setCellStyle(textStyle);
						}
						Cell R50Cell4 = row.createCell(4);
						if (record.getR50OtherInstallmentLoans() != null) {
							R50Cell4.setCellValue(record.getR50OtherInstallmentLoans().doubleValue());
							R50Cell4.setCellStyle(numberStyle);
						} else {
							R50Cell4.setCellValue("");
							R50Cell4.setCellStyle(textStyle);
						}

						// --- R51 (Row Index 50) ---
						row = sheet.getRow(50);
						Cell R51Cell1 = row.createCell(1);
						if (record1.getR51FactoringDebtors() != null) {
							R51Cell1.setCellValue(record1.getR51FactoringDebtors().doubleValue());
							R51Cell1.setCellStyle(numberStyle);
						} else {
							R51Cell1.setCellValue("");
							R51Cell1.setCellStyle(textStyle);
						}
						Cell R51Cell2 = row.createCell(2);
						if (record1.getR51Leasing() != null) {
							R51Cell2.setCellValue(record1.getR51Leasing().doubleValue());
							R51Cell2.setCellStyle(numberStyle);
						} else {
							R51Cell2.setCellValue("");
							R51Cell2.setCellStyle(textStyle);
						}
						Cell R51Cell3 = row.createCell(3);
						if (record.getR51Overdrafts() != null) {
							R51Cell3.setCellValue(record.getR51Overdrafts().doubleValue());
							R51Cell3.setCellStyle(numberStyle);
						} else {
							R51Cell3.setCellValue("");
							R51Cell3.setCellStyle(textStyle);
						}
						Cell R51Cell4 = row.createCell(4);
						if (record.getR51OtherInstallmentLoans() != null) {
							R51Cell4.setCellValue(record.getR51OtherInstallmentLoans().doubleValue());
							R51Cell4.setCellStyle(numberStyle);
						} else {
							R51Cell4.setCellValue("");
							R51Cell4.setCellStyle(textStyle);
						}

						// --- R52 (Row Index 51) ---
						row = sheet.getRow(51);
						Cell R52Cell1 = row.createCell(1);
						if (record1.getR52FactoringDebtors() != null) {
							R52Cell1.setCellValue(record1.getR52FactoringDebtors().doubleValue());
							R52Cell1.setCellStyle(numberStyle);
						} else {
							R52Cell1.setCellValue("");
							R52Cell1.setCellStyle(textStyle);
						}
						Cell R52Cell2 = row.createCell(2);
						if (record1.getR52Leasing() != null) {
							R52Cell2.setCellValue(record1.getR52Leasing().doubleValue());
							R52Cell2.setCellStyle(numberStyle);
						} else {
							R52Cell2.setCellValue("");
							R52Cell2.setCellStyle(textStyle);
						}
						Cell R52Cell3 = row.createCell(3);
						if (record.getR52Overdrafts() != null) {
							R52Cell3.setCellValue(record.getR52Overdrafts().doubleValue());
							R52Cell3.setCellStyle(numberStyle);
						} else {
							R52Cell3.setCellValue("");
							R52Cell3.setCellStyle(textStyle);
						}
						Cell R52Cell4 = row.createCell(4);
						if (record.getR52OtherInstallmentLoans() != null) {
							R52Cell4.setCellValue(record.getR52OtherInstallmentLoans().doubleValue());
							R52Cell4.setCellStyle(numberStyle);
						} else {
							R52Cell4.setCellValue("");
							R52Cell4.setCellStyle(textStyle);
						}
						// --- R54 (Row Index 53) ---
						row = sheet.getRow(53);
						Cell R54Cell1 = row.createCell(1);
						if (record1.getR54FactoringDebtors() != null) {
							R54Cell1.setCellValue(record1.getR54FactoringDebtors().doubleValue());
							R54Cell1.setCellStyle(numberStyle);
						} else {
							R54Cell1.setCellValue("");
							R54Cell1.setCellStyle(textStyle);
						}
						Cell R54Cell2 = row.createCell(2);
						if (record1.getR54Leasing() != null) {
							R54Cell2.setCellValue(record1.getR54Leasing().doubleValue());
							R54Cell2.setCellStyle(numberStyle);
						} else {
							R54Cell2.setCellValue("");
							R54Cell2.setCellStyle(textStyle);
						}
						Cell R54Cell3 = row.createCell(3);
						if (record.getR54Overdrafts() != null) {
							R54Cell3.setCellValue(record.getR54Overdrafts().doubleValue());
							R54Cell3.setCellStyle(numberStyle);
						} else {
							R54Cell3.setCellValue("");
							R54Cell3.setCellStyle(textStyle);
						}
						Cell R54Cell4 = row.createCell(4);
						if (record.getR54OtherInstallmentLoans() != null) {
							R54Cell4.setCellValue(record.getR54OtherInstallmentLoans().doubleValue());
							R54Cell4.setCellStyle(numberStyle);
						} else {
							R54Cell4.setCellValue("");
							R54Cell4.setCellStyle(textStyle);
						}

						// --- R55 (Row Index 54) ---
						row = sheet.getRow(54);
						Cell R55Cell1 = row.createCell(1);
						if (record1.getR55FactoringDebtors() != null) {
							R55Cell1.setCellValue(record1.getR55FactoringDebtors().doubleValue());
							R55Cell1.setCellStyle(numberStyle);
						} else {
							R55Cell1.setCellValue("");
							R55Cell1.setCellStyle(textStyle);
						}
						Cell R55Cell2 = row.createCell(2);
						if (record1.getR55Leasing() != null) {
							R55Cell2.setCellValue(record1.getR55Leasing().doubleValue());
							R55Cell2.setCellStyle(numberStyle);
						} else {
							R55Cell2.setCellValue("");
							R55Cell2.setCellStyle(textStyle);
						}
						Cell R55Cell3 = row.createCell(3);
						if (record.getR55Overdrafts() != null) {
							R55Cell3.setCellValue(record.getR55Overdrafts().doubleValue());
							R55Cell3.setCellStyle(numberStyle);
						} else {
							R55Cell3.setCellValue("");
							R55Cell3.setCellStyle(textStyle);
						}
						Cell R55Cell4 = row.createCell(4);
						if (record.getR55OtherInstallmentLoans() != null) {
							R55Cell4.setCellValue(record.getR55OtherInstallmentLoans().doubleValue());
							R55Cell4.setCellStyle(numberStyle);
						} else {
							R55Cell4.setCellValue("");
							R55Cell4.setCellStyle(textStyle);
						}

						// --- R56 (Row Index 55) ---
						row = sheet.getRow(55);
						Cell R56Cell1 = row.createCell(1);
						if (record1.getR56FactoringDebtors() != null) {
							R56Cell1.setCellValue(record1.getR56FactoringDebtors().doubleValue());
							R56Cell1.setCellStyle(numberStyle);
						} else {
							R56Cell1.setCellValue("");
							R56Cell1.setCellStyle(textStyle);
						}
						Cell R56Cell2 = row.createCell(2);
						if (record1.getR56Leasing() != null) {
							R56Cell2.setCellValue(record1.getR56Leasing().doubleValue());
							R56Cell2.setCellStyle(numberStyle);
						} else {
							R56Cell2.setCellValue("");
							R56Cell2.setCellStyle(textStyle);
						}
						Cell R56Cell3 = row.createCell(3);
						if (record.getR56Overdrafts() != null) {
							R56Cell3.setCellValue(record.getR56Overdrafts().doubleValue());
							R56Cell3.setCellStyle(numberStyle);
						} else {
							R56Cell3.setCellValue("");
							R56Cell3.setCellStyle(textStyle);
						}
						Cell R56Cell4 = row.createCell(4);
						if (record.getR56OtherInstallmentLoans() != null) {
							R56Cell4.setCellValue(record.getR56OtherInstallmentLoans().doubleValue());
							R56Cell4.setCellStyle(numberStyle);
						} else {
							R56Cell4.setCellValue("");
							R56Cell4.setCellStyle(textStyle);
						}
						// --- R58 (Row Index 57) ---
						row = sheet.getRow(57);
						Cell R58Cell1 = row.createCell(1);
						if (record1.getR58FactoringDebtors() != null) {
							R58Cell1.setCellValue(record1.getR58FactoringDebtors().doubleValue());
							R58Cell1.setCellStyle(numberStyle);
						} else {
							R58Cell1.setCellValue("");
							R58Cell1.setCellStyle(textStyle);
						}
						Cell R58Cell2 = row.createCell(2);
						if (record1.getR58Leasing() != null) {
							R58Cell2.setCellValue(record1.getR58Leasing().doubleValue());
							R58Cell2.setCellStyle(numberStyle);
						} else {
							R58Cell2.setCellValue("");
							R58Cell2.setCellStyle(textStyle);
						}
						Cell R58Cell3 = row.createCell(3);
						if (record.getR58Overdrafts() != null) {
							R58Cell3.setCellValue(record.getR58Overdrafts().doubleValue());
							R58Cell3.setCellStyle(numberStyle);
						} else {
							R58Cell3.setCellValue("");
							R58Cell3.setCellStyle(textStyle);
						}
						Cell R58Cell4 = row.createCell(4);
						if (record.getR58OtherInstallmentLoans() != null) {
							R58Cell4.setCellValue(record.getR58OtherInstallmentLoans().doubleValue());
							R58Cell4.setCellStyle(numberStyle);
						} else {
							R58Cell4.setCellValue("");
							R58Cell4.setCellStyle(textStyle);
						}

						// --- R59 (Row Index 58) ---
						row = sheet.getRow(58);
						Cell R59Cell1 = row.createCell(1);
						if (record1.getR59FactoringDebtors() != null) {
							R59Cell1.setCellValue(record1.getR59FactoringDebtors().doubleValue());
							R59Cell1.setCellStyle(numberStyle);
						} else {
							R59Cell1.setCellValue("");
							R59Cell1.setCellStyle(textStyle);
						}
						Cell R59Cell2 = row.createCell(2);
						if (record1.getR59Leasing() != null) {
							R59Cell2.setCellValue(record1.getR59Leasing().doubleValue());
							R59Cell2.setCellStyle(numberStyle);
						} else {
							R59Cell2.setCellValue("");
							R59Cell2.setCellStyle(textStyle);
						}
						Cell R59Cell3 = row.createCell(3);
						if (record.getR59Overdrafts() != null) {
							R59Cell3.setCellValue(record.getR59Overdrafts().doubleValue());
							R59Cell3.setCellStyle(numberStyle);
						} else {
							R59Cell3.setCellValue("");
							R59Cell3.setCellStyle(textStyle);
						}
						Cell R59Cell4 = row.createCell(4);
						if (record.getR59OtherInstallmentLoans() != null) {
							R59Cell4.setCellValue(record.getR59OtherInstallmentLoans().doubleValue());
							R59Cell4.setCellStyle(numberStyle);
						} else {
							R59Cell4.setCellValue("");
							R59Cell4.setCellStyle(textStyle);
						}

						// --- R60 (Row Index 59) ---
						row = sheet.getRow(59);
						Cell R60Cell1 = row.createCell(1);
						if (record1.getR60FactoringDebtors() != null) {
							R60Cell1.setCellValue(record1.getR60FactoringDebtors().doubleValue());
							R60Cell1.setCellStyle(numberStyle);
						} else {
							R60Cell1.setCellValue("");
							R60Cell1.setCellStyle(textStyle);
						}
						Cell R60Cell2 = row.createCell(2);
						if (record1.getR60Leasing() != null) {
							R60Cell2.setCellValue(record1.getR60Leasing().doubleValue());
							R60Cell2.setCellStyle(numberStyle);
						} else {
							R60Cell2.setCellValue("");
							R60Cell2.setCellStyle(textStyle);
						}
						Cell R60Cell3 = row.createCell(3);
						if (record.getR60Overdrafts() != null) {
							R60Cell3.setCellValue(record.getR60Overdrafts().doubleValue());
							R60Cell3.setCellStyle(numberStyle);
						} else {
							R60Cell3.setCellValue("");
							R60Cell3.setCellStyle(textStyle);
						}
						Cell R60Cell4 = row.createCell(4);
						if (record.getR60OtherInstallmentLoans() != null) {
							R60Cell4.setCellValue(record.getR60OtherInstallmentLoans().doubleValue());
							R60Cell4.setCellStyle(numberStyle);
						} else {
							R60Cell4.setCellValue("");
							R60Cell4.setCellStyle(textStyle);
						}

						// --- R61 (Row Index 60) ---
						row = sheet.getRow(60);
						Cell R61Cell1 = row.createCell(1);
						if (record1.getR61FactoringDebtors() != null) {
							R61Cell1.setCellValue(record1.getR61FactoringDebtors().doubleValue());
							R61Cell1.setCellStyle(numberStyle);
						} else {
							R61Cell1.setCellValue("");
							R61Cell1.setCellStyle(textStyle);
						}
						Cell R61Cell2 = row.createCell(2);
						if (record1.getR61Leasing() != null) {
							R61Cell2.setCellValue(record1.getR61Leasing().doubleValue());
							R61Cell2.setCellStyle(numberStyle);
						} else {
							R61Cell2.setCellValue("");
							R61Cell2.setCellStyle(textStyle);
						}
						Cell R61Cell3 = row.createCell(3);
						if (record.getR61Overdrafts() != null) {
							R61Cell3.setCellValue(record.getR61Overdrafts().doubleValue());
							R61Cell3.setCellStyle(numberStyle);
						} else {
							R61Cell3.setCellValue("");
							R61Cell3.setCellStyle(textStyle);
						}
						Cell R61Cell4 = row.createCell(4);
						if (record.getR61OtherInstallmentLoans() != null) {
							R61Cell4.setCellValue(record.getR61OtherInstallmentLoans().doubleValue());
							R61Cell4.setCellStyle(numberStyle);
						} else {
							R61Cell4.setCellValue("");
							R61Cell4.setCellStyle(textStyle);
						}

						// --- R62 (Row Index 61) ---
						row = sheet.getRow(61);
						Cell R62Cell1 = row.createCell(1);
						if (record1.getR62FactoringDebtors() != null) {
							R62Cell1.setCellValue(record1.getR62FactoringDebtors().doubleValue());
							R62Cell1.setCellStyle(numberStyle);
						} else {
							R62Cell1.setCellValue("");
							R62Cell1.setCellStyle(textStyle);
						}
						Cell R62Cell2 = row.createCell(2);
						if (record1.getR62Leasing() != null) {
							R62Cell2.setCellValue(record1.getR62Leasing().doubleValue());
							R62Cell2.setCellStyle(numberStyle);
						} else {
							R62Cell2.setCellValue("");
							R62Cell2.setCellStyle(textStyle);
						}
						Cell R62Cell3 = row.createCell(3);
						if (record.getR62Overdrafts() != null) {
							R62Cell3.setCellValue(record.getR62Overdrafts().doubleValue());
							R62Cell3.setCellStyle(numberStyle);
						} else {
							R62Cell3.setCellValue("");
							R62Cell3.setCellStyle(textStyle);
						}
						Cell R62Cell4 = row.createCell(4);
						if (record.getR62OtherInstallmentLoans() != null) {
							R62Cell4.setCellValue(record.getR62OtherInstallmentLoans().doubleValue());
							R62Cell4.setCellStyle(numberStyle);
						} else {
							R62Cell4.setCellValue("");
							R62Cell4.setCellStyle(textStyle);
						}

						// --- R63 (Row Index 62) ---
						row = sheet.getRow(62);
						Cell R63Cell1 = row.createCell(1);
						if (record1.getR63FactoringDebtors() != null) {
							R63Cell1.setCellValue(record1.getR63FactoringDebtors().doubleValue());
							R63Cell1.setCellStyle(numberStyle);
						} else {
							R63Cell1.setCellValue("");
							R63Cell1.setCellStyle(textStyle);
						}
						Cell R63Cell2 = row.createCell(2);
						if (record1.getR63Leasing() != null) {
							R63Cell2.setCellValue(record1.getR63Leasing().doubleValue());
							R63Cell2.setCellStyle(numberStyle);
						} else {
							R63Cell2.setCellValue("");
							R63Cell2.setCellStyle(textStyle);
						}
						Cell R63Cell3 = row.createCell(3);
						if (record.getR63Overdrafts() != null) {
							R63Cell3.setCellValue(record.getR63Overdrafts().doubleValue());
							R63Cell3.setCellStyle(numberStyle);
						} else {
							R63Cell3.setCellValue("");
							R63Cell3.setCellStyle(textStyle);
						}
						Cell R63Cell4 = row.createCell(4);
						if (record.getR63OtherInstallmentLoans() != null) {
							R63Cell4.setCellValue(record.getR63OtherInstallmentLoans().doubleValue());
							R63Cell4.setCellStyle(numberStyle);
						} else {
							R63Cell4.setCellValue("");
							R63Cell4.setCellStyle(textStyle);
						}
					}
					workbook.setForceFormulaRecalculation(true);
				} else {

				}

				// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA4 SUMMARY", null,
							"BRRS_M_LA4_SUMMARYTABLE");
				}
				return out.toByteArray();
			}

		}
	}

	public byte[] getExcelM_LA4ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {

		}

		List<M_LA4_Archival_Summary_Entity1> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);
		List<M_LA4_Archival_Summary_Entity2> dataList1 = getdatabydateListarchival2(dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA4 new report. Returning empty result.");
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
//NORMAL
			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA4_Archival_Summary_Entity1 record = dataList.get(i);
					M_LA4_Archival_Summary_Entity2 record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell R12Cell = row.createCell(1);

					if (record1.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record1.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(11);
					// R12 Col B
					Cell R12Cell1 = row.createCell(1);
					if (record1.getR12FactoringDebtors() != null) {
						R12Cell1.setCellValue(record1.getR12FactoringDebtors().doubleValue());
						R12Cell1.setCellStyle(numberStyle);
					} else {
						R12Cell1.setCellValue("");
						R12Cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12Cell2 = row.createCell(2);
					if (record1.getR12Leasing() != null) {
						R12Cell2.setCellValue(record1.getR12Leasing().doubleValue());
						R12Cell2.setCellStyle(numberStyle);
					} else {
						R12Cell2.setCellValue("");
						R12Cell2.setCellStyle(textStyle);
					}
					// R12 Col D
					Cell R12Cell3 = row.createCell(3);
					if (record.getR12Overdrafts() != null) {
						R12Cell3.setCellValue(record.getR12Overdrafts().doubleValue());
						R12Cell3.setCellStyle(numberStyle);
					} else {
						R12Cell3.setCellValue("");
						R12Cell3.setCellStyle(textStyle);
					}

					// R12 Col E
					Cell R12Cell4 = row.createCell(4);
					if (record.getR12OtherInstallmentLoans() != null) {
						R12Cell4.setCellValue(record.getR12OtherInstallmentLoans().doubleValue());
						R12Cell4.setCellStyle(numberStyle);
					} else {
						R12Cell4.setCellValue("");
						R12Cell4.setCellStyle(textStyle);
					}
					// R13 Col B
					row = sheet.getRow(12);
					// R13 Col B
					Cell R13Cell1 = row.createCell(1);
					if (record1.getR13FactoringDebtors() != null) {
						R13Cell1.setCellValue(record1.getR13FactoringDebtors().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13Cell2 = row.createCell(2);
					if (record1.getR13Leasing() != null) {
						R13Cell2.setCellValue(record1.getR13Leasing().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}
					// R13 Col D
					Cell R13Cell3 = row.createCell(3);
					if (record.getR13Overdrafts() != null) {
						R13Cell3.setCellValue(record.getR13Overdrafts().doubleValue());
						R13Cell3.setCellStyle(numberStyle);
					} else {
						R13Cell3.setCellValue("");
						R13Cell3.setCellStyle(textStyle);
					}

					// R13 Col E
					Cell R13Cell4 = row.createCell(4);
					if (record.getR13OtherInstallmentLoans() != null) {
						R13Cell4.setCellValue(record.getR13OtherInstallmentLoans().doubleValue());
						R13Cell4.setCellStyle(numberStyle);
					} else {
						R13Cell4.setCellValue("");
						R13Cell4.setCellStyle(textStyle);
					}
					// R14 Col B
					row = sheet.getRow(13); // Row index 13 is Excel Row 14
					// R14 Col B
					Cell R14Cell1 = row.createCell(1);
					if (record1.getR14FactoringDebtors() != null) {
						R14Cell1.setCellValue(record1.getR14FactoringDebtors().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14Cell2 = row.createCell(2);
					if (record1.getR14Leasing() != null) {
						R14Cell2.setCellValue(record1.getR14Leasing().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

					// R14 Col D
					Cell R14Cell3 = row.createCell(3);
					if (record.getR14Overdrafts() != null) {
						R14Cell3.setCellValue(record.getR14Overdrafts().doubleValue());
						R14Cell3.setCellStyle(numberStyle);
					} else {
						R14Cell3.setCellValue("");
						R14Cell3.setCellStyle(textStyle);
					}

					// R14 Col E
					Cell R14Cell4 = row.createCell(4);
					if (record.getR14OtherInstallmentLoans() != null) {
						R14Cell4.setCellValue(record.getR14OtherInstallmentLoans().doubleValue());
						R14Cell4.setCellStyle(numberStyle);
					} else {
						R14Cell4.setCellValue("");
						R14Cell4.setCellStyle(textStyle);
					}

					// --- R16 (Row Index 15) ---
					row = sheet.getRow(15);
					Cell R16Cell1 = row.createCell(1);
					if (record1.getR16FactoringDebtors() != null) {
						R16Cell1.setCellValue(record1.getR16FactoringDebtors().doubleValue());
						R16Cell1.setCellStyle(numberStyle);
					} else {
						R16Cell1.setCellValue("");
						R16Cell1.setCellStyle(textStyle);
					}
					Cell R16Cell2 = row.createCell(2);
					if (record1.getR16Leasing() != null) {
						R16Cell2.setCellValue(record1.getR16Leasing().doubleValue());
						R16Cell2.setCellStyle(numberStyle);
					} else {
						R16Cell2.setCellValue("");
						R16Cell2.setCellStyle(textStyle);
					}
					Cell R16Cell3 = row.createCell(3);
					if (record.getR16Overdrafts() != null) {
						R16Cell3.setCellValue(record.getR16Overdrafts().doubleValue());
						R16Cell3.setCellStyle(numberStyle);
					} else {
						R16Cell3.setCellValue("");
						R16Cell3.setCellStyle(textStyle);
					}
					Cell R16Cell4 = row.createCell(4);
					if (record.getR16OtherInstallmentLoans() != null) {
						R16Cell4.setCellValue(record.getR16OtherInstallmentLoans().doubleValue());
						R16Cell4.setCellStyle(numberStyle);
					} else {
						R16Cell4.setCellValue("");
						R16Cell4.setCellStyle(textStyle);
					}

					// --- R17 (Row Index 16) ---
					row = sheet.getRow(16);
					Cell R17Cell1 = row.createCell(1);
					if (record1.getR17FactoringDebtors() != null) {
						R17Cell1.setCellValue(record1.getR17FactoringDebtors().doubleValue());
						R17Cell1.setCellStyle(numberStyle);
					} else {
						R17Cell1.setCellValue("");
						R17Cell1.setCellStyle(textStyle);
					}
					Cell R17Cell2 = row.createCell(2);
					if (record1.getR17Leasing() != null) {
						R17Cell2.setCellValue(record1.getR17Leasing().doubleValue());
						R17Cell2.setCellStyle(numberStyle);
					} else {
						R17Cell2.setCellValue("");
						R17Cell2.setCellStyle(textStyle);
					}
					Cell R17Cell3 = row.createCell(3);
					if (record.getR17Overdrafts() != null) {
						R17Cell3.setCellValue(record.getR17Overdrafts().doubleValue());
						R17Cell3.setCellStyle(numberStyle);
					} else {
						R17Cell3.setCellValue("");
						R17Cell3.setCellStyle(textStyle);
					}
					Cell R17Cell4 = row.createCell(4);
					if (record.getR17OtherInstallmentLoans() != null) {
						R17Cell4.setCellValue(record.getR17OtherInstallmentLoans().doubleValue());
						R17Cell4.setCellStyle(numberStyle);
					} else {
						R17Cell4.setCellValue("");
						R17Cell4.setCellStyle(textStyle);
					}

					// --- R18 (Row Index 17) ---
					row = sheet.getRow(17);
					Cell R18Cell1 = row.createCell(1);
					if (record1.getR18FactoringDebtors() != null) {
						R18Cell1.setCellValue(record1.getR18FactoringDebtors().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}
					Cell R18Cell2 = row.createCell(2);
					if (record1.getR18Leasing() != null) {
						R18Cell2.setCellValue(record1.getR18Leasing().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}
					Cell R18Cell3 = row.createCell(3);
					if (record.getR18Overdrafts() != null) {
						R18Cell3.setCellValue(record.getR18Overdrafts().doubleValue());
						R18Cell3.setCellStyle(numberStyle);
					} else {
						R18Cell3.setCellValue("");
						R18Cell3.setCellStyle(textStyle);
					}
					Cell R18Cell4 = row.createCell(4);
					if (record.getR18OtherInstallmentLoans() != null) {
						R18Cell4.setCellValue(record.getR18OtherInstallmentLoans().doubleValue());
						R18Cell4.setCellStyle(numberStyle);
					} else {
						R18Cell4.setCellValue("");
						R18Cell4.setCellStyle(textStyle);
					}

					// --- R19 (Row Index 18) ---
					row = sheet.getRow(18);
					Cell R19Cell1 = row.createCell(1);
					if (record1.getR19FactoringDebtors() != null) {
						R19Cell1.setCellValue(record1.getR19FactoringDebtors().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}
					Cell R19Cell2 = row.createCell(2);
					if (record1.getR19Leasing() != null) {
						R19Cell2.setCellValue(record1.getR19Leasing().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}
					Cell R19Cell3 = row.createCell(3);
					if (record.getR19Overdrafts() != null) {
						R19Cell3.setCellValue(record.getR19Overdrafts().doubleValue());
						R19Cell3.setCellStyle(numberStyle);
					} else {
						R19Cell3.setCellValue("");
						R19Cell3.setCellStyle(textStyle);
					}
					Cell R19Cell4 = row.createCell(4);
					if (record.getR19OtherInstallmentLoans() != null) {
						R19Cell4.setCellValue(record.getR19OtherInstallmentLoans().doubleValue());
						R19Cell4.setCellStyle(numberStyle);
					} else {
						R19Cell4.setCellValue("");
						R19Cell4.setCellStyle(textStyle);
					}
					// --- R20 (Row Index 19) ---
					row = sheet.getRow(19);
					Cell R20Cell1 = row.createCell(1);
					if (record1.getR20FactoringDebtors() != null) {
						R20Cell1.setCellValue(record1.getR20FactoringDebtors().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}
					Cell R20Cell2 = row.createCell(2);
					if (record1.getR20Leasing() != null) {
						R20Cell2.setCellValue(record1.getR20Leasing().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}
					Cell R20Cell3 = row.createCell(3);
					if (record.getR20Overdrafts() != null) {
						R20Cell3.setCellValue(record.getR20Overdrafts().doubleValue());
						R20Cell3.setCellStyle(numberStyle);
					} else {
						R20Cell3.setCellValue("");
						R20Cell3.setCellStyle(textStyle);
					}
					Cell R20Cell4 = row.createCell(4);
					if (record.getR20OtherInstallmentLoans() != null) {
						R20Cell4.setCellValue(record.getR20OtherInstallmentLoans().doubleValue());
						R20Cell4.setCellStyle(numberStyle);
					} else {
						R20Cell4.setCellValue("");
						R20Cell4.setCellStyle(textStyle);
					}

					// --- R21 (Row Index 20) ---
					row = sheet.getRow(20);
					Cell R21Cell1 = row.createCell(1);
					if (record1.getR21FactoringDebtors() != null) {
						R21Cell1.setCellValue(record1.getR21FactoringDebtors().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}
					Cell R21Cell2 = row.createCell(2);
					if (record1.getR21Leasing() != null) {
						R21Cell2.setCellValue(record1.getR21Leasing().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}
					Cell R21Cell3 = row.createCell(3);
					if (record.getR21Overdrafts() != null) {
						R21Cell3.setCellValue(record.getR21Overdrafts().doubleValue());
						R21Cell3.setCellStyle(numberStyle);
					} else {
						R21Cell3.setCellValue("");
						R21Cell3.setCellStyle(textStyle);
					}
					Cell R21Cell4 = row.createCell(4);
					if (record.getR21OtherInstallmentLoans() != null) {
						R21Cell4.setCellValue(record.getR21OtherInstallmentLoans().doubleValue());
						R21Cell4.setCellStyle(numberStyle);
					} else {
						R21Cell4.setCellValue("");
						R21Cell4.setCellStyle(textStyle);
					}

					// --- R22 (Row Index 21) ---
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(1);
					if (record1.getR22FactoringDebtors() != null) {
						R22Cell1.setCellValue(record1.getR22FactoringDebtors().doubleValue());
						R22Cell1.setCellStyle(numberStyle);
					} else {
						R22Cell1.setCellValue("");
						R22Cell1.setCellStyle(textStyle);
					}
					Cell R22Cell2 = row.createCell(2);
					if (record1.getR22Leasing() != null) {
						R22Cell2.setCellValue(record1.getR22Leasing().doubleValue());
						R22Cell2.setCellStyle(numberStyle);
					} else {
						R22Cell2.setCellValue("");
						R22Cell2.setCellStyle(textStyle);
					}
					Cell R22Cell3 = row.createCell(3);
					if (record.getR22Overdrafts() != null) {
						R22Cell3.setCellValue(record.getR22Overdrafts().doubleValue());
						R22Cell3.setCellStyle(numberStyle);
					} else {
						R22Cell3.setCellValue("");
						R22Cell3.setCellStyle(textStyle);
					}
					Cell R22Cell4 = row.createCell(4);
					if (record.getR22OtherInstallmentLoans() != null) {
						R22Cell4.setCellValue(record.getR22OtherInstallmentLoans().doubleValue());
						R22Cell4.setCellStyle(numberStyle);
					} else {
						R22Cell4.setCellValue("");
						R22Cell4.setCellStyle(textStyle);
					}

					// --- R23 (Row Index 22) ---
					row = sheet.getRow(22);
					Cell R23Cell1 = row.createCell(1);
					if (record1.getR23FactoringDebtors() != null) {
						R23Cell1.setCellValue(record1.getR23FactoringDebtors().doubleValue());
						R23Cell1.setCellStyle(numberStyle);
					} else {
						R23Cell1.setCellValue("");
						R23Cell1.setCellStyle(textStyle);
					}
					Cell R23Cell2 = row.createCell(2);
					if (record1.getR23Leasing() != null) {
						R23Cell2.setCellValue(record1.getR23Leasing().doubleValue());
						R23Cell2.setCellStyle(numberStyle);
					} else {
						R23Cell2.setCellValue("");
						R23Cell2.setCellStyle(textStyle);
					}
					Cell R23Cell3 = row.createCell(3);
					if (record.getR23Overdrafts() != null) {
						R23Cell3.setCellValue(record.getR23Overdrafts().doubleValue());
						R23Cell3.setCellStyle(numberStyle);
					} else {
						R23Cell3.setCellValue("");
						R23Cell3.setCellStyle(textStyle);
					}
					Cell R23Cell4 = row.createCell(4);
					if (record.getR23OtherInstallmentLoans() != null) {
						R23Cell4.setCellValue(record.getR23OtherInstallmentLoans().doubleValue());
						R23Cell4.setCellStyle(numberStyle);
					} else {
						R23Cell4.setCellValue("");
						R23Cell4.setCellStyle(textStyle);
					}

					// --- R24 (Row Index 23) ---
					row = sheet.getRow(23);
					Cell R24Cell1 = row.createCell(1);
					if (record1.getR24FactoringDebtors() != null) {
						R24Cell1.setCellValue(record1.getR24FactoringDebtors().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}
					Cell R24Cell2 = row.createCell(2);
					if (record1.getR24Leasing() != null) {
						R24Cell2.setCellValue(record1.getR24Leasing().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}
					Cell R24Cell3 = row.createCell(3);
					if (record.getR24Overdrafts() != null) {
						R24Cell3.setCellValue(record.getR24Overdrafts().doubleValue());
						R24Cell3.setCellStyle(numberStyle);
					} else {
						R24Cell3.setCellValue("");
						R24Cell3.setCellStyle(textStyle);
					}
					Cell R24Cell4 = row.createCell(4);
					if (record.getR24OtherInstallmentLoans() != null) {
						R24Cell4.setCellValue(record.getR24OtherInstallmentLoans().doubleValue());
						R24Cell4.setCellStyle(numberStyle);
					} else {
						R24Cell4.setCellValue("");
						R24Cell4.setCellStyle(textStyle);
					}

					// --- R25 (Row Index 24) ---
					row = sheet.getRow(24);
					Cell R25Cell1 = row.createCell(1);
					if (record1.getR25FactoringDebtors() != null) {
						R25Cell1.setCellValue(record1.getR25FactoringDebtors().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}
					Cell R25Cell2 = row.createCell(2);
					if (record1.getR25Leasing() != null) {
						R25Cell2.setCellValue(record1.getR25Leasing().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}
					Cell R25Cell3 = row.createCell(3);
					if (record.getR25Overdrafts() != null) {
						R25Cell3.setCellValue(record.getR25Overdrafts().doubleValue());
						R25Cell3.setCellStyle(numberStyle);
					} else {
						R25Cell3.setCellValue("");
						R25Cell3.setCellStyle(textStyle);
					}
					Cell R25Cell4 = row.createCell(4);
					if (record.getR25OtherInstallmentLoans() != null) {
						R25Cell4.setCellValue(record.getR25OtherInstallmentLoans().doubleValue());
						R25Cell4.setCellStyle(numberStyle);
					} else {
						R25Cell4.setCellValue("");
						R25Cell4.setCellStyle(textStyle);
					}

					// --- R26 (Row Index 25) ---
					row = sheet.getRow(25);
					Cell R26Cell1 = row.createCell(1);
					if (record1.getR26FactoringDebtors() != null) {
						R26Cell1.setCellValue(record1.getR26FactoringDebtors().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}
					Cell R26Cell2 = row.createCell(2);
					if (record1.getR26Leasing() != null) {
						R26Cell2.setCellValue(record1.getR26Leasing().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}
					Cell R26Cell3 = row.createCell(3);
					if (record.getR26Overdrafts() != null) {
						R26Cell3.setCellValue(record.getR26Overdrafts().doubleValue());
						R26Cell3.setCellStyle(numberStyle);
					} else {
						R26Cell3.setCellValue("");
						R26Cell3.setCellStyle(textStyle);
					}
					Cell R26Cell4 = row.createCell(4);
					if (record.getR26OtherInstallmentLoans() != null) {
						R26Cell4.setCellValue(record.getR26OtherInstallmentLoans().doubleValue());
						R26Cell4.setCellStyle(numberStyle);
					} else {
						R26Cell4.setCellValue("");
						R26Cell4.setCellStyle(textStyle);
					}

					// --- R27 (Row Index 26) ---
					row = sheet.getRow(26);
					Cell R27Cell1 = row.createCell(1);
					if (record1.getR27FactoringDebtors() != null) {
						R27Cell1.setCellValue(record1.getR27FactoringDebtors().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}
					Cell R27Cell2 = row.createCell(2);
					if (record1.getR27Leasing() != null) {
						R27Cell2.setCellValue(record1.getR27Leasing().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}
					Cell R27Cell3 = row.createCell(3);
					if (record.getR27Overdrafts() != null) {
						R27Cell3.setCellValue(record.getR27Overdrafts().doubleValue());
						R27Cell3.setCellStyle(numberStyle);
					} else {
						R27Cell3.setCellValue("");
						R27Cell3.setCellStyle(textStyle);
					}
					Cell R27Cell4 = row.createCell(4);
					if (record.getR27OtherInstallmentLoans() != null) {
						R27Cell4.setCellValue(record.getR27OtherInstallmentLoans().doubleValue());
						R27Cell4.setCellStyle(numberStyle);
					} else {
						R27Cell4.setCellValue("");
						R27Cell4.setCellStyle(textStyle);
					}

					// --- R28 (Row Index 27) ---
					row = sheet.getRow(27);
					Cell R28Cell1 = row.createCell(1);
					if (record1.getR28FactoringDebtors() != null) {
						R28Cell1.setCellValue(record1.getR28FactoringDebtors().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}
					Cell R28Cell2 = row.createCell(2);
					if (record1.getR28Leasing() != null) {
						R28Cell2.setCellValue(record1.getR28Leasing().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}
					Cell R28Cell3 = row.createCell(3);
					if (record.getR28Overdrafts() != null) {
						R28Cell3.setCellValue(record.getR28Overdrafts().doubleValue());
						R28Cell3.setCellStyle(numberStyle);
					} else {
						R28Cell3.setCellValue("");
						R28Cell3.setCellStyle(textStyle);
					}
					Cell R28Cell4 = row.createCell(4);
					if (record.getR28OtherInstallmentLoans() != null) {
						R28Cell4.setCellValue(record.getR28OtherInstallmentLoans().doubleValue());
						R28Cell4.setCellStyle(numberStyle);
					} else {
						R28Cell4.setCellValue("");
						R28Cell4.setCellStyle(textStyle);
					}

					// --- R30 (Row Index 29) ---
					row = sheet.getRow(29);
					Cell R30Cell1 = row.createCell(1);
					if (record1.getR30FactoringDebtors() != null) {
						R30Cell1.setCellValue(record1.getR30FactoringDebtors().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}
					Cell R30Cell2 = row.createCell(2);
					if (record1.getR30Leasing() != null) {
						R30Cell2.setCellValue(record1.getR30Leasing().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}
					Cell R30Cell3 = row.createCell(3);
					if (record.getR30Overdrafts() != null) {
						R30Cell3.setCellValue(record.getR30Overdrafts().doubleValue());
						R30Cell3.setCellStyle(numberStyle);
					} else {
						R30Cell3.setCellValue("");
						R30Cell3.setCellStyle(textStyle);
					}
					Cell R30Cell4 = row.createCell(4);
					if (record.getR30OtherInstallmentLoans() != null) {
						R30Cell4.setCellValue(record.getR30OtherInstallmentLoans().doubleValue());
						R30Cell4.setCellStyle(numberStyle);
					} else {
						R30Cell4.setCellValue("");
						R30Cell4.setCellStyle(textStyle);
					}

					// --- R31 (Row Index 30) ---
					row = sheet.getRow(30);
					Cell R31Cell1 = row.createCell(1);
					if (record1.getR31FactoringDebtors() != null) {
						R31Cell1.setCellValue(record1.getR31FactoringDebtors().doubleValue());
						R31Cell1.setCellStyle(numberStyle);
					} else {
						R31Cell1.setCellValue("");
						R31Cell1.setCellStyle(textStyle);
					}
					Cell R31Cell2 = row.createCell(2);
					if (record1.getR31Leasing() != null) {
						R31Cell2.setCellValue(record1.getR31Leasing().doubleValue());
						R31Cell2.setCellStyle(numberStyle);
					} else {
						R31Cell2.setCellValue("");
						R31Cell2.setCellStyle(textStyle);
					}
					Cell R31Cell3 = row.createCell(3);
					if (record.getR31Overdrafts() != null) {
						R31Cell3.setCellValue(record.getR31Overdrafts().doubleValue());
						R31Cell3.setCellStyle(numberStyle);
					} else {
						R31Cell3.setCellValue("");
						R31Cell3.setCellStyle(textStyle);
					}
					Cell R31Cell4 = row.createCell(4);
					if (record.getR31OtherInstallmentLoans() != null) {
						R31Cell4.setCellValue(record.getR31OtherInstallmentLoans().doubleValue());
						R31Cell4.setCellStyle(numberStyle);
					} else {
						R31Cell4.setCellValue("");
						R31Cell4.setCellStyle(textStyle);
					}

					// --- R32 (Row Index 31) ---
					row = sheet.getRow(31);
					Cell R32Cell1 = row.createCell(1);
					if (record1.getR32FactoringDebtors() != null) {
						R32Cell1.setCellValue(record1.getR32FactoringDebtors().doubleValue());
						R32Cell1.setCellStyle(numberStyle);
					} else {
						R32Cell1.setCellValue("");
						R32Cell1.setCellStyle(textStyle);
					}
					Cell R32Cell2 = row.createCell(2);
					if (record1.getR32Leasing() != null) {
						R32Cell2.setCellValue(record1.getR32Leasing().doubleValue());
						R32Cell2.setCellStyle(numberStyle);
					} else {
						R32Cell2.setCellValue("");
						R32Cell2.setCellStyle(textStyle);
					}
					Cell R32Cell3 = row.createCell(3);
					if (record.getR32Overdrafts() != null) {
						R32Cell3.setCellValue(record.getR32Overdrafts().doubleValue());
						R32Cell3.setCellStyle(numberStyle);
					} else {
						R32Cell3.setCellValue("");
						R32Cell3.setCellStyle(textStyle);
					}
					Cell R32Cell4 = row.createCell(4);
					if (record.getR32OtherInstallmentLoans() != null) {
						R32Cell4.setCellValue(record.getR32OtherInstallmentLoans().doubleValue());
						R32Cell4.setCellStyle(numberStyle);
					} else {
						R32Cell4.setCellValue("");
						R32Cell4.setCellStyle(textStyle);
					}

					// --- R33 (Row Index 32) ---
					row = sheet.getRow(32);
					Cell R33Cell1 = row.createCell(1);
					if (record1.getR33FactoringDebtors() != null) {
						R33Cell1.setCellValue(record1.getR33FactoringDebtors().doubleValue());
						R33Cell1.setCellStyle(numberStyle);
					} else {
						R33Cell1.setCellValue("");
						R33Cell1.setCellStyle(textStyle);
					}
					Cell R33Cell2 = row.createCell(2);
					if (record1.getR33Leasing() != null) {
						R33Cell2.setCellValue(record1.getR33Leasing().doubleValue());
						R33Cell2.setCellStyle(numberStyle);
					} else {
						R33Cell2.setCellValue("");
						R33Cell2.setCellStyle(textStyle);
					}
					Cell R33Cell3 = row.createCell(3);
					if (record.getR33Overdrafts() != null) {
						R33Cell3.setCellValue(record.getR33Overdrafts().doubleValue());
						R33Cell3.setCellStyle(numberStyle);
					} else {
						R33Cell3.setCellValue("");
						R33Cell3.setCellStyle(textStyle);
					}
					Cell R33Cell4 = row.createCell(4);
					if (record.getR33OtherInstallmentLoans() != null) {
						R33Cell4.setCellValue(record.getR33OtherInstallmentLoans().doubleValue());
						R33Cell4.setCellStyle(numberStyle);
					} else {
						R33Cell4.setCellValue("");
						R33Cell4.setCellStyle(textStyle);
					}

					// --- R34 (Row Index 33) ---
					row = sheet.getRow(33);
					Cell R34Cell1 = row.createCell(1);
					if (record1.getR34FactoringDebtors() != null) {
						R34Cell1.setCellValue(record1.getR34FactoringDebtors().doubleValue());
						R34Cell1.setCellStyle(numberStyle);
					} else {
						R34Cell1.setCellValue("");
						R34Cell1.setCellStyle(textStyle);
					}
					Cell R34Cell2 = row.createCell(2);
					if (record1.getR34Leasing() != null) {
						R34Cell2.setCellValue(record1.getR34Leasing().doubleValue());
						R34Cell2.setCellStyle(numberStyle);
					} else {
						R34Cell2.setCellValue("");
						R34Cell2.setCellStyle(textStyle);
					}
					Cell R34Cell3 = row.createCell(3);
					if (record.getR34Overdrafts() != null) {
						R34Cell3.setCellValue(record.getR34Overdrafts().doubleValue());
						R34Cell3.setCellStyle(numberStyle);
					} else {
						R34Cell3.setCellValue("");
						R34Cell3.setCellStyle(textStyle);
					}
					Cell R34Cell4 = row.createCell(4);
					if (record.getR34OtherInstallmentLoans() != null) {
						R34Cell4.setCellValue(record.getR34OtherInstallmentLoans().doubleValue());
						R34Cell4.setCellStyle(numberStyle);
					} else {
						R34Cell4.setCellValue("");
						R34Cell4.setCellStyle(textStyle);
					}

					// --- R35 (Row Index 34) ---
					row = sheet.getRow(34);
					Cell R35Cell1 = row.createCell(1);
					if (record1.getR35FactoringDebtors() != null) {
						R35Cell1.setCellValue(record1.getR35FactoringDebtors().doubleValue());
						R35Cell1.setCellStyle(numberStyle);
					} else {
						R35Cell1.setCellValue("");
						R35Cell1.setCellStyle(textStyle);
					}
					Cell R35Cell2 = row.createCell(2);
					if (record1.getR35Leasing() != null) {
						R35Cell2.setCellValue(record1.getR35Leasing().doubleValue());
						R35Cell2.setCellStyle(numberStyle);
					} else {
						R35Cell2.setCellValue("");
						R35Cell2.setCellStyle(textStyle);
					}
					Cell R35Cell3 = row.createCell(3);
					if (record.getR35Overdrafts() != null) {
						R35Cell3.setCellValue(record.getR35Overdrafts().doubleValue());
						R35Cell3.setCellStyle(numberStyle);
					} else {
						R35Cell3.setCellValue("");
						R35Cell3.setCellStyle(textStyle);
					}
					Cell R35Cell4 = row.createCell(4);
					if (record.getR35OtherInstallmentLoans() != null) {
						R35Cell4.setCellValue(record.getR35OtherInstallmentLoans().doubleValue());
						R35Cell4.setCellStyle(numberStyle);
					} else {
						R35Cell4.setCellValue("");
						R35Cell4.setCellStyle(textStyle);
					}

					// --- R36 (Row Index 35) ---
					row = sheet.getRow(35);
					Cell R36Cell1 = row.createCell(1);
					if (record1.getR36FactoringDebtors() != null) {
						R36Cell1.setCellValue(record1.getR36FactoringDebtors().doubleValue());
						R36Cell1.setCellStyle(numberStyle);
					} else {
						R36Cell1.setCellValue("");
						R36Cell1.setCellStyle(textStyle);
					}
					Cell R36Cell2 = row.createCell(2);
					if (record1.getR36Leasing() != null) {
						R36Cell2.setCellValue(record1.getR36Leasing().doubleValue());
						R36Cell2.setCellStyle(numberStyle);
					} else {
						R36Cell2.setCellValue("");
						R36Cell2.setCellStyle(textStyle);
					}
					Cell R36Cell3 = row.createCell(3);
					if (record.getR36Overdrafts() != null) {
						R36Cell3.setCellValue(record.getR36Overdrafts().doubleValue());
						R36Cell3.setCellStyle(numberStyle);
					} else {
						R36Cell3.setCellValue("");
						R36Cell3.setCellStyle(textStyle);
					}
					Cell R36Cell4 = row.createCell(4);
					if (record.getR36OtherInstallmentLoans() != null) {
						R36Cell4.setCellValue(record.getR36OtherInstallmentLoans().doubleValue());
						R36Cell4.setCellStyle(numberStyle);
					} else {
						R36Cell4.setCellValue("");
						R36Cell4.setCellStyle(textStyle);
					}

					// --- R37 (Row Index 36) ---
					row = sheet.getRow(36);
					Cell R37Cell1 = row.createCell(1);
					if (record1.getR37FactoringDebtors() != null) {
						R37Cell1.setCellValue(record1.getR37FactoringDebtors().doubleValue());
						R37Cell1.setCellStyle(numberStyle);
					} else {
						R37Cell1.setCellValue("");
						R37Cell1.setCellStyle(textStyle);
					}
					Cell R37Cell2 = row.createCell(2);
					if (record1.getR37Leasing() != null) {
						R37Cell2.setCellValue(record1.getR37Leasing().doubleValue());
						R37Cell2.setCellStyle(numberStyle);
					} else {
						R37Cell2.setCellValue("");
						R37Cell2.setCellStyle(textStyle);
					}
					Cell R37Cell3 = row.createCell(3);
					if (record.getR37Overdrafts() != null) {
						R37Cell3.setCellValue(record.getR37Overdrafts().doubleValue());
						R37Cell3.setCellStyle(numberStyle);
					} else {
						R37Cell3.setCellValue("");
						R37Cell3.setCellStyle(textStyle);
					}
					Cell R37Cell4 = row.createCell(4);
					if (record.getR37OtherInstallmentLoans() != null) {
						R37Cell4.setCellValue(record.getR37OtherInstallmentLoans().doubleValue());
						R37Cell4.setCellStyle(numberStyle);
					} else {
						R37Cell4.setCellValue("");
						R37Cell4.setCellStyle(textStyle);
					}
					// --- R39 (Row Index 38) ---
					row = sheet.getRow(38);
					Cell R39Cell1 = row.createCell(1);
					if (record1.getR39FactoringDebtors() != null) {
						R39Cell1.setCellValue(record1.getR39FactoringDebtors().doubleValue());
						R39Cell1.setCellStyle(numberStyle);
					} else {
						R39Cell1.setCellValue("");
						R39Cell1.setCellStyle(textStyle);
					}
					Cell R39Cell2 = row.createCell(2);
					if (record1.getR39Leasing() != null) {
						R39Cell2.setCellValue(record1.getR39Leasing().doubleValue());
						R39Cell2.setCellStyle(numberStyle);
					} else {
						R39Cell2.setCellValue("");
						R39Cell2.setCellStyle(textStyle);
					}
					Cell R39Cell3 = row.createCell(3);
					if (record.getR39Overdrafts() != null) {
						R39Cell3.setCellValue(record.getR39Overdrafts().doubleValue());
						R39Cell3.setCellStyle(numberStyle);
					} else {
						R39Cell3.setCellValue("");
						R39Cell3.setCellStyle(textStyle);
					}
					Cell R39Cell4 = row.createCell(4);
					if (record.getR39OtherInstallmentLoans() != null) {
						R39Cell4.setCellValue(record.getR39OtherInstallmentLoans().doubleValue());
						R39Cell4.setCellStyle(numberStyle);
					} else {
						R39Cell4.setCellValue("");
						R39Cell4.setCellStyle(textStyle);
					}

					// --- R40 (Row Index 39) ---
					row = sheet.getRow(39);
					Cell R40Cell1 = row.createCell(1);
					if (record1.getR40FactoringDebtors() != null) {
						R40Cell1.setCellValue(record1.getR40FactoringDebtors().doubleValue());
						R40Cell1.setCellStyle(numberStyle);
					} else {
						R40Cell1.setCellValue("");
						R40Cell1.setCellStyle(textStyle);
					}
					Cell R40Cell2 = row.createCell(2);
					if (record1.getR40Leasing() != null) {
						R40Cell2.setCellValue(record1.getR40Leasing().doubleValue());
						R40Cell2.setCellStyle(numberStyle);
					} else {
						R40Cell2.setCellValue("");
						R40Cell2.setCellStyle(textStyle);
					}
					Cell R40Cell3 = row.createCell(3);
					if (record.getR40Overdrafts() != null) {
						R40Cell3.setCellValue(record.getR40Overdrafts().doubleValue());
						R40Cell3.setCellStyle(numberStyle);
					} else {
						R40Cell3.setCellValue("");
						R40Cell3.setCellStyle(textStyle);
					}
					Cell R40Cell4 = row.createCell(4);
					if (record.getR40OtherInstallmentLoans() != null) {
						R40Cell4.setCellValue(record.getR40OtherInstallmentLoans().doubleValue());
						R40Cell4.setCellStyle(numberStyle);
					} else {
						R40Cell4.setCellValue("");
						R40Cell4.setCellStyle(textStyle);
					}
					// --- R42 (Row Index 41) ---
					row = sheet.getRow(41);
					Cell R42Cell1 = row.createCell(1);
					if (record1.getR42FactoringDebtors() != null) {
						R42Cell1.setCellValue(record1.getR42FactoringDebtors().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}
					Cell R42Cell2 = row.createCell(2);
					if (record1.getR42Leasing() != null) {
						R42Cell2.setCellValue(record1.getR42Leasing().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}
					Cell R42Cell3 = row.createCell(3);
					if (record.getR42Overdrafts() != null) {
						R42Cell3.setCellValue(record.getR42Overdrafts().doubleValue());
						R42Cell3.setCellStyle(numberStyle);
					} else {
						R42Cell3.setCellValue("");
						R42Cell3.setCellStyle(textStyle);
					}
					Cell R42Cell4 = row.createCell(4);
					if (record.getR42OtherInstallmentLoans() != null) {
						R42Cell4.setCellValue(record.getR42OtherInstallmentLoans().doubleValue());
						R42Cell4.setCellStyle(numberStyle);
					} else {
						R42Cell4.setCellValue("");
						R42Cell4.setCellStyle(textStyle);
					}

					// --- R43 (Row Index 42) ---
					row = sheet.getRow(42);
					Cell R43Cell1 = row.createCell(1);
					if (record1.getR43FactoringDebtors() != null) {
						R43Cell1.setCellValue(record1.getR43FactoringDebtors().doubleValue());
						R43Cell1.setCellStyle(numberStyle);
					} else {
						R43Cell1.setCellValue("");
						R43Cell1.setCellStyle(textStyle);
					}
					Cell R43Cell2 = row.createCell(2);
					if (record1.getR43Leasing() != null) {
						R43Cell2.setCellValue(record1.getR43Leasing().doubleValue());
						R43Cell2.setCellStyle(numberStyle);
					} else {
						R43Cell2.setCellValue("");
						R43Cell2.setCellStyle(textStyle);
					}
					Cell R43Cell3 = row.createCell(3);
					if (record.getR43Overdrafts() != null) {
						R43Cell3.setCellValue(record.getR43Overdrafts().doubleValue());
						R43Cell3.setCellStyle(numberStyle);
					} else {
						R43Cell3.setCellValue("");
						R43Cell3.setCellStyle(textStyle);
					}
					Cell R43Cell4 = row.createCell(4);
					if (record.getR43OtherInstallmentLoans() != null) {
						R43Cell4.setCellValue(record.getR43OtherInstallmentLoans().doubleValue());
						R43Cell4.setCellStyle(numberStyle);
					} else {
						R43Cell4.setCellValue("");
						R43Cell4.setCellStyle(textStyle);
					}
					// --- R45 (Row Index 44) ---
					row = sheet.getRow(44);
					Cell R45Cell1 = row.createCell(1);
					if (record1.getR45FactoringDebtors() != null) {
						R45Cell1.setCellValue(record1.getR45FactoringDebtors().doubleValue());
						R45Cell1.setCellStyle(numberStyle);
					} else {
						R45Cell1.setCellValue("");
						R45Cell1.setCellStyle(textStyle);
					}
					Cell R45Cell2 = row.createCell(2);
					if (record1.getR45Leasing() != null) {
						R45Cell2.setCellValue(record1.getR45Leasing().doubleValue());
						R45Cell2.setCellStyle(numberStyle);
					} else {
						R45Cell2.setCellValue("");
						R45Cell2.setCellStyle(textStyle);
					}
					Cell R45Cell3 = row.createCell(3);
					if (record.getR45Overdrafts() != null) {
						R45Cell3.setCellValue(record.getR45Overdrafts().doubleValue());
						R45Cell3.setCellStyle(numberStyle);
					} else {
						R45Cell3.setCellValue("");
						R45Cell3.setCellStyle(textStyle);
					}
					Cell R45Cell4 = row.createCell(4);
					if (record.getR45OtherInstallmentLoans() != null) {
						R45Cell4.setCellValue(record.getR45OtherInstallmentLoans().doubleValue());
						R45Cell4.setCellStyle(numberStyle);
					} else {
						R45Cell4.setCellValue("");
						R45Cell4.setCellStyle(textStyle);
					}

					// --- R46 (Row Index 45) ---
					row = sheet.getRow(45);
					Cell R46Cell1 = row.createCell(1);
					if (record1.getR46FactoringDebtors() != null) {
						R46Cell1.setCellValue(record1.getR46FactoringDebtors().doubleValue());
						R46Cell1.setCellStyle(numberStyle);
					} else {
						R46Cell1.setCellValue("");
						R46Cell1.setCellStyle(textStyle);
					}
					Cell R46Cell2 = row.createCell(2);
					if (record1.getR46Leasing() != null) {
						R46Cell2.setCellValue(record1.getR46Leasing().doubleValue());
						R46Cell2.setCellStyle(numberStyle);
					} else {
						R46Cell2.setCellValue("");
						R46Cell2.setCellStyle(textStyle);
					}
					Cell R46Cell3 = row.createCell(3);
					if (record.getR46Overdrafts() != null) {
						R46Cell3.setCellValue(record.getR46Overdrafts().doubleValue());
						R46Cell3.setCellStyle(numberStyle);
					} else {
						R46Cell3.setCellValue("");
						R46Cell3.setCellStyle(textStyle);
					}
					Cell R46Cell4 = row.createCell(4);
					if (record.getR46OtherInstallmentLoans() != null) {
						R46Cell4.setCellValue(record.getR46OtherInstallmentLoans().doubleValue());
						R46Cell4.setCellStyle(numberStyle);
					} else {
						R46Cell4.setCellValue("");
						R46Cell4.setCellStyle(textStyle);
					}

					// --- R47 (Row Index 46) ---
					row = sheet.getRow(46);
					Cell R47Cell1 = row.createCell(1);
					if (record1.getR47FactoringDebtors() != null) {
						R47Cell1.setCellValue(record1.getR47FactoringDebtors().doubleValue());
						R47Cell1.setCellStyle(numberStyle);
					} else {
						R47Cell1.setCellValue("");
						R47Cell1.setCellStyle(textStyle);
					}
					Cell R47Cell2 = row.createCell(2);
					if (record1.getR47Leasing() != null) {
						R47Cell2.setCellValue(record1.getR47Leasing().doubleValue());
						R47Cell2.setCellStyle(numberStyle);
					} else {
						R47Cell2.setCellValue("");
						R47Cell2.setCellStyle(textStyle);
					}
					Cell R47Cell3 = row.createCell(3);
					if (record.getR47Overdrafts() != null) {
						R47Cell3.setCellValue(record.getR47Overdrafts().doubleValue());
						R47Cell3.setCellStyle(numberStyle);
					} else {
						R47Cell3.setCellValue("");
						R47Cell3.setCellStyle(textStyle);
					}
					Cell R47Cell4 = row.createCell(4);
					if (record.getR47OtherInstallmentLoans() != null) {
						R47Cell4.setCellValue(record.getR47OtherInstallmentLoans().doubleValue());
						R47Cell4.setCellStyle(numberStyle);
					} else {
						R47Cell4.setCellValue("");
						R47Cell4.setCellStyle(textStyle);
					}

					// --- R48 (Row Index 47) ---
					row = sheet.getRow(47);
					Cell R48Cell1 = row.createCell(1);
					if (record1.getR48FactoringDebtors() != null) {
						R48Cell1.setCellValue(record1.getR48FactoringDebtors().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}
					Cell R48Cell2 = row.createCell(2);
					if (record1.getR48Leasing() != null) {
						R48Cell2.setCellValue(record1.getR48Leasing().doubleValue());
						R48Cell2.setCellStyle(numberStyle);
					} else {
						R48Cell2.setCellValue("");
						R48Cell2.setCellStyle(textStyle);
					}
					Cell R48Cell3 = row.createCell(3);
					if (record.getR48Overdrafts() != null) {
						R48Cell3.setCellValue(record.getR48Overdrafts().doubleValue());
						R48Cell3.setCellStyle(numberStyle);
					} else {
						R48Cell3.setCellValue("");
						R48Cell3.setCellStyle(textStyle);
					}
					Cell R48Cell4 = row.createCell(4);
					if (record.getR48OtherInstallmentLoans() != null) {
						R48Cell4.setCellValue(record.getR48OtherInstallmentLoans().doubleValue());
						R48Cell4.setCellStyle(numberStyle);
					} else {
						R48Cell4.setCellValue("");
						R48Cell4.setCellStyle(textStyle);
					}

					// --- R50 (Row Index 49) ---
					row = sheet.getRow(49);
					Cell R50Cell1 = row.createCell(1);
					if (record1.getR50FactoringDebtors() != null) {
						R50Cell1.setCellValue(record1.getR50FactoringDebtors().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}
					Cell R50Cell2 = row.createCell(2);
					if (record1.getR50Leasing() != null) {
						R50Cell2.setCellValue(record1.getR50Leasing().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}
					Cell R50Cell3 = row.createCell(3);
					if (record.getR50Overdrafts() != null) {
						R50Cell3.setCellValue(record.getR50Overdrafts().doubleValue());
						R50Cell3.setCellStyle(numberStyle);
					} else {
						R50Cell3.setCellValue("");
						R50Cell3.setCellStyle(textStyle);
					}
					Cell R50Cell4 = row.createCell(4);
					if (record.getR50OtherInstallmentLoans() != null) {
						R50Cell4.setCellValue(record.getR50OtherInstallmentLoans().doubleValue());
						R50Cell4.setCellStyle(numberStyle);
					} else {
						R50Cell4.setCellValue("");
						R50Cell4.setCellStyle(textStyle);
					}

					// --- R51 (Row Index 50) ---
					row = sheet.getRow(50);
					Cell R51Cell1 = row.createCell(1);
					if (record1.getR51FactoringDebtors() != null) {
						R51Cell1.setCellValue(record1.getR51FactoringDebtors().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}
					Cell R51Cell2 = row.createCell(2);
					if (record1.getR51Leasing() != null) {
						R51Cell2.setCellValue(record1.getR51Leasing().doubleValue());
						R51Cell2.setCellStyle(numberStyle);
					} else {
						R51Cell2.setCellValue("");
						R51Cell2.setCellStyle(textStyle);
					}
					Cell R51Cell3 = row.createCell(3);
					if (record.getR51Overdrafts() != null) {
						R51Cell3.setCellValue(record.getR51Overdrafts().doubleValue());
						R51Cell3.setCellStyle(numberStyle);
					} else {
						R51Cell3.setCellValue("");
						R51Cell3.setCellStyle(textStyle);
					}
					Cell R51Cell4 = row.createCell(4);
					if (record.getR51OtherInstallmentLoans() != null) {
						R51Cell4.setCellValue(record.getR51OtherInstallmentLoans().doubleValue());
						R51Cell4.setCellStyle(numberStyle);
					} else {
						R51Cell4.setCellValue("");
						R51Cell4.setCellStyle(textStyle);
					}

					// --- R52 (Row Index 51) ---
					row = sheet.getRow(51);
					Cell R52Cell1 = row.createCell(1);
					if (record1.getR52FactoringDebtors() != null) {
						R52Cell1.setCellValue(record1.getR52FactoringDebtors().doubleValue());
						R52Cell1.setCellStyle(numberStyle);
					} else {
						R52Cell1.setCellValue("");
						R52Cell1.setCellStyle(textStyle);
					}
					Cell R52Cell2 = row.createCell(2);
					if (record1.getR52Leasing() != null) {
						R52Cell2.setCellValue(record1.getR52Leasing().doubleValue());
						R52Cell2.setCellStyle(numberStyle);
					} else {
						R52Cell2.setCellValue("");
						R52Cell2.setCellStyle(textStyle);
					}
					Cell R52Cell3 = row.createCell(3);
					if (record.getR52Overdrafts() != null) {
						R52Cell3.setCellValue(record.getR52Overdrafts().doubleValue());
						R52Cell3.setCellStyle(numberStyle);
					} else {
						R52Cell3.setCellValue("");
						R52Cell3.setCellStyle(textStyle);
					}
					Cell R52Cell4 = row.createCell(4);
					if (record.getR52OtherInstallmentLoans() != null) {
						R52Cell4.setCellValue(record.getR52OtherInstallmentLoans().doubleValue());
						R52Cell4.setCellStyle(numberStyle);
					} else {
						R52Cell4.setCellValue("");
						R52Cell4.setCellStyle(textStyle);
					}
					// --- R54 (Row Index 53) ---
					row = sheet.getRow(53);
					Cell R54Cell1 = row.createCell(1);
					if (record1.getR54FactoringDebtors() != null) {
						R54Cell1.setCellValue(record1.getR54FactoringDebtors().doubleValue());
						R54Cell1.setCellStyle(numberStyle);
					} else {
						R54Cell1.setCellValue("");
						R54Cell1.setCellStyle(textStyle);
					}
					Cell R54Cell2 = row.createCell(2);
					if (record1.getR54Leasing() != null) {
						R54Cell2.setCellValue(record1.getR54Leasing().doubleValue());
						R54Cell2.setCellStyle(numberStyle);
					} else {
						R54Cell2.setCellValue("");
						R54Cell2.setCellStyle(textStyle);
					}
					Cell R54Cell3 = row.createCell(3);
					if (record.getR54Overdrafts() != null) {
						R54Cell3.setCellValue(record.getR54Overdrafts().doubleValue());
						R54Cell3.setCellStyle(numberStyle);
					} else {
						R54Cell3.setCellValue("");
						R54Cell3.setCellStyle(textStyle);
					}
					Cell R54Cell4 = row.createCell(4);
					if (record.getR54OtherInstallmentLoans() != null) {
						R54Cell4.setCellValue(record.getR54OtherInstallmentLoans().doubleValue());
						R54Cell4.setCellStyle(numberStyle);
					} else {
						R54Cell4.setCellValue("");
						R54Cell4.setCellStyle(textStyle);
					}

					// --- R55 (Row Index 54) ---
					row = sheet.getRow(54);
					Cell R55Cell1 = row.createCell(1);
					if (record1.getR55FactoringDebtors() != null) {
						R55Cell1.setCellValue(record1.getR55FactoringDebtors().doubleValue());
						R55Cell1.setCellStyle(numberStyle);
					} else {
						R55Cell1.setCellValue("");
						R55Cell1.setCellStyle(textStyle);
					}
					Cell R55Cell2 = row.createCell(2);
					if (record1.getR55Leasing() != null) {
						R55Cell2.setCellValue(record1.getR55Leasing().doubleValue());
						R55Cell2.setCellStyle(numberStyle);
					} else {
						R55Cell2.setCellValue("");
						R55Cell2.setCellStyle(textStyle);
					}
					Cell R55Cell3 = row.createCell(3);
					if (record.getR55Overdrafts() != null) {
						R55Cell3.setCellValue(record.getR55Overdrafts().doubleValue());
						R55Cell3.setCellStyle(numberStyle);
					} else {
						R55Cell3.setCellValue("");
						R55Cell3.setCellStyle(textStyle);
					}
					Cell R55Cell4 = row.createCell(4);
					if (record.getR55OtherInstallmentLoans() != null) {
						R55Cell4.setCellValue(record.getR55OtherInstallmentLoans().doubleValue());
						R55Cell4.setCellStyle(numberStyle);
					} else {
						R55Cell4.setCellValue("");
						R55Cell4.setCellStyle(textStyle);
					}

					// --- R56 (Row Index 55) ---
					row = sheet.getRow(55);
					Cell R56Cell1 = row.createCell(1);
					if (record1.getR56FactoringDebtors() != null) {
						R56Cell1.setCellValue(record1.getR56FactoringDebtors().doubleValue());
						R56Cell1.setCellStyle(numberStyle);
					} else {
						R56Cell1.setCellValue("");
						R56Cell1.setCellStyle(textStyle);
					}
					Cell R56Cell2 = row.createCell(2);
					if (record1.getR56Leasing() != null) {
						R56Cell2.setCellValue(record1.getR56Leasing().doubleValue());
						R56Cell2.setCellStyle(numberStyle);
					} else {
						R56Cell2.setCellValue("");
						R56Cell2.setCellStyle(textStyle);
					}
					Cell R56Cell3 = row.createCell(3);
					if (record.getR56Overdrafts() != null) {
						R56Cell3.setCellValue(record.getR56Overdrafts().doubleValue());
						R56Cell3.setCellStyle(numberStyle);
					} else {
						R56Cell3.setCellValue("");
						R56Cell3.setCellStyle(textStyle);
					}
					Cell R56Cell4 = row.createCell(4);
					if (record.getR56OtherInstallmentLoans() != null) {
						R56Cell4.setCellValue(record.getR56OtherInstallmentLoans().doubleValue());
						R56Cell4.setCellStyle(numberStyle);
					} else {
						R56Cell4.setCellValue("");
						R56Cell4.setCellStyle(textStyle);
					}
					// --- R58 (Row Index 57) ---
					row = sheet.getRow(57);
					Cell R58Cell1 = row.createCell(1);
					if (record1.getR58FactoringDebtors() != null) {
						R58Cell1.setCellValue(record1.getR58FactoringDebtors().doubleValue());
						R58Cell1.setCellStyle(numberStyle);
					} else {
						R58Cell1.setCellValue("");
						R58Cell1.setCellStyle(textStyle);
					}
					Cell R58Cell2 = row.createCell(2);
					if (record1.getR58Leasing() != null) {
						R58Cell2.setCellValue(record1.getR58Leasing().doubleValue());
						R58Cell2.setCellStyle(numberStyle);
					} else {
						R58Cell2.setCellValue("");
						R58Cell2.setCellStyle(textStyle);
					}
					Cell R58Cell3 = row.createCell(3);
					if (record.getR58Overdrafts() != null) {
						R58Cell3.setCellValue(record.getR58Overdrafts().doubleValue());
						R58Cell3.setCellStyle(numberStyle);
					} else {
						R58Cell3.setCellValue("");
						R58Cell3.setCellStyle(textStyle);
					}
					Cell R58Cell4 = row.createCell(4);
					if (record.getR58OtherInstallmentLoans() != null) {
						R58Cell4.setCellValue(record.getR58OtherInstallmentLoans().doubleValue());
						R58Cell4.setCellStyle(numberStyle);
					} else {
						R58Cell4.setCellValue("");
						R58Cell4.setCellStyle(textStyle);
					}

					// --- R59 (Row Index 58) ---
					row = sheet.getRow(58);
					Cell R59Cell1 = row.createCell(1);
					if (record1.getR59FactoringDebtors() != null) {
						R59Cell1.setCellValue(record1.getR59FactoringDebtors().doubleValue());
						R59Cell1.setCellStyle(numberStyle);
					} else {
						R59Cell1.setCellValue("");
						R59Cell1.setCellStyle(textStyle);
					}
					Cell R59Cell2 = row.createCell(2);
					if (record1.getR59Leasing() != null) {
						R59Cell2.setCellValue(record1.getR59Leasing().doubleValue());
						R59Cell2.setCellStyle(numberStyle);
					} else {
						R59Cell2.setCellValue("");
						R59Cell2.setCellStyle(textStyle);
					}
					Cell R59Cell3 = row.createCell(3);
					if (record.getR59Overdrafts() != null) {
						R59Cell3.setCellValue(record.getR59Overdrafts().doubleValue());
						R59Cell3.setCellStyle(numberStyle);
					} else {
						R59Cell3.setCellValue("");
						R59Cell3.setCellStyle(textStyle);
					}
					Cell R59Cell4 = row.createCell(4);
					if (record.getR59OtherInstallmentLoans() != null) {
						R59Cell4.setCellValue(record.getR59OtherInstallmentLoans().doubleValue());
						R59Cell4.setCellStyle(numberStyle);
					} else {
						R59Cell4.setCellValue("");
						R59Cell4.setCellStyle(textStyle);
					}

					// --- R60 (Row Index 59) ---
					row = sheet.getRow(59);
					Cell R60Cell1 = row.createCell(1);
					if (record1.getR60FactoringDebtors() != null) {
						R60Cell1.setCellValue(record1.getR60FactoringDebtors().doubleValue());
						R60Cell1.setCellStyle(numberStyle);
					} else {
						R60Cell1.setCellValue("");
						R60Cell1.setCellStyle(textStyle);
					}
					Cell R60Cell2 = row.createCell(2);
					if (record1.getR60Leasing() != null) {
						R60Cell2.setCellValue(record1.getR60Leasing().doubleValue());
						R60Cell2.setCellStyle(numberStyle);
					} else {
						R60Cell2.setCellValue("");
						R60Cell2.setCellStyle(textStyle);
					}
					Cell R60Cell3 = row.createCell(3);
					if (record.getR60Overdrafts() != null) {
						R60Cell3.setCellValue(record.getR60Overdrafts().doubleValue());
						R60Cell3.setCellStyle(numberStyle);
					} else {
						R60Cell3.setCellValue("");
						R60Cell3.setCellStyle(textStyle);
					}
					Cell R60Cell4 = row.createCell(4);
					if (record.getR60OtherInstallmentLoans() != null) {
						R60Cell4.setCellValue(record.getR60OtherInstallmentLoans().doubleValue());
						R60Cell4.setCellStyle(numberStyle);
					} else {
						R60Cell4.setCellValue("");
						R60Cell4.setCellStyle(textStyle);
					}

					// --- R61 (Row Index 60) ---
					row = sheet.getRow(60);
					Cell R61Cell1 = row.createCell(1);
					if (record1.getR61FactoringDebtors() != null) {
						R61Cell1.setCellValue(record1.getR61FactoringDebtors().doubleValue());
						R61Cell1.setCellStyle(numberStyle);
					} else {
						R61Cell1.setCellValue("");
						R61Cell1.setCellStyle(textStyle);
					}
					Cell R61Cell2 = row.createCell(2);
					if (record1.getR61Leasing() != null) {
						R61Cell2.setCellValue(record1.getR61Leasing().doubleValue());
						R61Cell2.setCellStyle(numberStyle);
					} else {
						R61Cell2.setCellValue("");
						R61Cell2.setCellStyle(textStyle);
					}
					Cell R61Cell3 = row.createCell(3);
					if (record.getR61Overdrafts() != null) {
						R61Cell3.setCellValue(record.getR61Overdrafts().doubleValue());
						R61Cell3.setCellStyle(numberStyle);
					} else {
						R61Cell3.setCellValue("");
						R61Cell3.setCellStyle(textStyle);
					}
					Cell R61Cell4 = row.createCell(4);
					if (record.getR61OtherInstallmentLoans() != null) {
						R61Cell4.setCellValue(record.getR61OtherInstallmentLoans().doubleValue());
						R61Cell4.setCellStyle(numberStyle);
					} else {
						R61Cell4.setCellValue("");
						R61Cell4.setCellStyle(textStyle);
					}

					// --- R62 (Row Index 61) ---
					row = sheet.getRow(61);
					Cell R62Cell1 = row.createCell(1);
					if (record1.getR62FactoringDebtors() != null) {
						R62Cell1.setCellValue(record1.getR62FactoringDebtors().doubleValue());
						R62Cell1.setCellStyle(numberStyle);
					} else {
						R62Cell1.setCellValue("");
						R62Cell1.setCellStyle(textStyle);
					}
					Cell R62Cell2 = row.createCell(2);
					if (record1.getR62Leasing() != null) {
						R62Cell2.setCellValue(record1.getR62Leasing().doubleValue());
						R62Cell2.setCellStyle(numberStyle);
					} else {
						R62Cell2.setCellValue("");
						R62Cell2.setCellStyle(textStyle);
					}
					Cell R62Cell3 = row.createCell(3);
					if (record.getR62Overdrafts() != null) {
						R62Cell3.setCellValue(record.getR62Overdrafts().doubleValue());
						R62Cell3.setCellStyle(numberStyle);
					} else {
						R62Cell3.setCellValue("");
						R62Cell3.setCellStyle(textStyle);
					}
					Cell R62Cell4 = row.createCell(4);
					if (record.getR62OtherInstallmentLoans() != null) {
						R62Cell4.setCellValue(record.getR62OtherInstallmentLoans().doubleValue());
						R62Cell4.setCellStyle(numberStyle);
					} else {
						R62Cell4.setCellValue("");
						R62Cell4.setCellStyle(textStyle);
					}

					// --- R63 (Row Index 62) ---
					row = sheet.getRow(62);
					Cell R63Cell1 = row.createCell(1);
					if (record1.getR63FactoringDebtors() != null) {
						R63Cell1.setCellValue(record1.getR63FactoringDebtors().doubleValue());
						R63Cell1.setCellStyle(numberStyle);
					} else {
						R63Cell1.setCellValue("");
						R63Cell1.setCellStyle(textStyle);
					}
					Cell R63Cell2 = row.createCell(2);
					if (record1.getR63Leasing() != null) {
						R63Cell2.setCellValue(record1.getR63Leasing().doubleValue());
						R63Cell2.setCellStyle(numberStyle);
					} else {
						R63Cell2.setCellValue("");
						R63Cell2.setCellStyle(textStyle);
					}
					Cell R63Cell3 = row.createCell(3);
					if (record.getR63Overdrafts() != null) {
						R63Cell3.setCellValue(record.getR63Overdrafts().doubleValue());
						R63Cell3.setCellStyle(numberStyle);
					} else {
						R63Cell3.setCellValue("");
						R63Cell3.setCellStyle(textStyle);
					}
					Cell R63Cell4 = row.createCell(4);
					if (record.getR63OtherInstallmentLoans() != null) {
						R63Cell4.setCellValue(record.getR63OtherInstallmentLoans().doubleValue());
						R63Cell4.setCellStyle(numberStyle);
					} else {
						R63Cell4.setCellValue("");
						R63Cell4.setCellStyle(textStyle);
					}
				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA4 ARCHIVAL SUMMARY", null,
						"BRRS_M_LA4_ARCHIVALTABLE_SUMMARY1");
			}
			return out.toByteArray();
		}

	}

@Transactional
	public void updateReport(Object entity, String type) {

		boolean isResub = "RESUB".equalsIgnoreCase(type);

		System.out.println("Came to M_LA4 Manual Update. Type: " + (isResub ? "RESUB" : "NORMAL"));

		// Dynamic table routing matching your Table 2 structure
		String tableName = isResub ? "BRRS_M_LA4_ARCHIVALTABLE_SUMMARY2" : "BRRS_M_LA4_SUMMARYTABLE2";

		try {
			// Use the actual runtime class
			Class<?> entityClass = entity.getClass();

			// Get report date
			Method getDateMethod = entityClass.getMethod("getREPORT_DATE");
			Object reportDateObj = getDateMethod.invoke(entity);

			if (reportDateObj == null) {
				throw new RuntimeException("Report Date is NULL");
			}

			// Handle conversion to SQL Date formats gracefully
			String reportDateStr = reportDateObj.toString();
			java.sql.Date sqlReportDate;
			if (reportDateObj instanceof java.util.Date) {
				sqlReportDate = new java.sql.Date(((java.util.Date) reportDateObj).getTime());
			} else {
				sqlReportDate = java.sql.Date.valueOf(reportDateStr.substring(0, 10));
			}

			System.out.println("Report Date : " + sqlReportDate);
			System.out.println("Entity Class : " + entityClass.getName());

			// =====================================================
			// 🔹 AUDIT TRAIL SETUP (DYNAMIC LOG PAYLOAD PREPARATION)
			// =====================================================
			StringBuilder changesBuilder = new StringBuilder();

			// Rows loop (11 to 64)
			for (int i = 11; i <= 64; i++) {

				// 1. EXACT match to your Java Property names (camelCase without underscores)
				String[] fields = { "FactoringDebtors", "Leasing" };

				for (String field : fields) {

					String getterName = "getR" + i + field;
					
					// 2. EXACT match to your SQL Column Names (with underscores, e.g., R11_FACTORING_DEBTORS)
					String dbFieldSegment = "FactoringDebtors".equals(field) ? "FACTORING_DEBTORS" : "LEASING";
					String columnName = "R" + i + "_" + dbFieldSegment;

					try {
						Method getter = entityClass.getMethod(getterName);
						Object newValueObj = getter.invoke(entity);

						// Skip processing if the web input value completely lacks data
						if (newValueObj == null) {
							continue;
						}

						// 1. Fetch current value directly from the targeted DB Table before updating
						String selectSql = "SELECT " + columnName + " FROM " + tableName + " WHERE REPORT_DATE = ?";
						Object dbValueObj = null;
						try {
							dbValueObj = jdbcTemplate.queryForObject(selectSql, Object.class, sqlReportDate);
						} catch (Exception e) {
							// Handle if row doesn't exist yet gracefully
							dbValueObj = null;
						}

						// 2. Normalize comparison strings to prevent audit bloat
						String currentValStr = (dbValueObj == null) ? "" : dbValueObj.toString().trim();
						String newValStr = newValueObj.toString().trim();

						// Skip update if value hasn't actually changed
						if (currentValStr.equals(newValStr)) {
							continue;
						}

						// 3. Track changes manually for JDBC tracking
						if (changesBuilder.length() > 0) {
							changesBuilder.append("|||");
						}
						changesBuilder.append(columnName.toUpperCase()).append(": OldValue: ")
								.append(currentValStr.isEmpty() ? "null" : currentValStr).append(", NewValue: ")
								.append(newValStr);

						// 4. Perform live database update via direct JDBC template
						String updateSql = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE REPORT_DATE = ?";
						int count = jdbcTemplate.update(updateSql, newValueObj, sqlReportDate);

						System.out.println("Updated Column : " + columnName + " Rows Affected : " + count);

					} catch (NoSuchMethodException ex) {
						// ignore missing row/field definitions gracefully
					}
				}
			}

			// =====================================================
			// 🔹 EXECUTE MANUAL AUDIT LOG INSERTION
			// =====================================================
			String changes = changesBuilder.toString();
			System.out.println("M_LA4 Manual Changes Length = " + changes.length());

			if (!changes.isEmpty()) {
				// Enforce character protection thresholds against database column bounds
				if (changes.length() > 1900) {
					changes = changes.substring(0, 1900);
				}

				// Call custom manual audit execution to save directly into your Audit table
				auditService.compareEntitiesmanual(entity, entity, reportDateStr, "M_LA4 Manual Screen", tableName);
			}

			System.out.println("M_LA4 Manual Update Completed Successfully for Type : " + type);

		} catch (Exception e) {
			System.err.println("===== M_LA4 UPDATE ERROR =====");
			e.printStackTrace();

			Throwable root = e;
			while (root.getCause() != null) {
				root = root.getCause();
			}

			System.err.println("ROOT CAUSE : " + root.getMessage());

			throw new RuntimeException("Error while updating M_LA4 Manual fields for type: " + type, e);
		}
	}


	public List<Object[]> getM_LA4Resub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<M_LA4_Archival_Summary_Entity1> repoData = getdatabydateListWithVersion1();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_LA4_Archival_Summary_Entity1 entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				M_LA4_Archival_Summary_Entity1 first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_LA4  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}

	// Normal Email Excel
	public byte[] BRRS_M_LA4EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,  BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA4EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						 version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}

		} else {
			List<M_LA4_Summary_Entity1> dataList = getDataByDate1(dateformat.parse(todate));
			List<M_LA4_Summary_Entity2> dataList1 = getDataByDate2(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_LA4 report. Returning empty result.");
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
				throw new SecurityException("Template file exists but is not readable (check permissions): "
						+ templatePath.toAbsolutePath());
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
				int startRow = 6;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_LA4_Summary_Entity1 record = dataList.get(i);
						M_LA4_Summary_Entity2 record1 = dataList1.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						Cell R12Cell = row.createCell(1);

						if (record1.getREPORT_DATE() != null) {

							R12Cell.setCellValue(record1.getREPORT_DATE());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(11);
						// R12 Col B
						Cell R12Cell1 = row.createCell(1);
						if (record1.getR12FactoringDebtors() != null) {
							R12Cell1.setCellValue(record1.getR12FactoringDebtors().doubleValue());
							R12Cell1.setCellStyle(numberStyle);
						} else {
							R12Cell1.setCellValue("");
							R12Cell1.setCellStyle(textStyle);
						}

						// R12 Col C
						Cell R12Cell2 = row.createCell(2);
						if (record1.getR12Leasing() != null) {
							R12Cell2.setCellValue(record1.getR12Leasing().doubleValue());
							R12Cell2.setCellStyle(numberStyle);
						} else {
							R12Cell2.setCellValue("");
							R12Cell2.setCellStyle(textStyle);
						}
						// R12 Col D
						Cell R12Cell3 = row.createCell(3);
						if (record.getR12Overdrafts() != null) {
							R12Cell3.setCellValue(record.getR12Overdrafts().doubleValue());
							R12Cell3.setCellStyle(numberStyle);
						} else {
							R12Cell3.setCellValue("");
							R12Cell3.setCellStyle(textStyle);
						}

						// R12 Col E
						Cell R12Cell4 = row.createCell(4);
						if (record.getR12OtherInstallmentLoans() != null) {
							R12Cell4.setCellValue(record.getR12OtherInstallmentLoans().doubleValue());
							R12Cell4.setCellStyle(numberStyle);
						} else {
							R12Cell4.setCellValue("");
							R12Cell4.setCellStyle(textStyle);
						}
						// R13 Col B
						row = sheet.getRow(12);
						// R13 Col B
						Cell R13Cell1 = row.createCell(1);
						if (record1.getR13FactoringDebtors() != null) {
							R13Cell1.setCellValue(record1.getR13FactoringDebtors().doubleValue());
							R13Cell1.setCellStyle(numberStyle);
						} else {
							R13Cell1.setCellValue("");
							R13Cell1.setCellStyle(textStyle);
						}

						// R13 Col C
						Cell R13Cell2 = row.createCell(2);
						if (record1.getR13Leasing() != null) {
							R13Cell2.setCellValue(record1.getR13Leasing().doubleValue());
							R13Cell2.setCellStyle(numberStyle);
						} else {
							R13Cell2.setCellValue("");
							R13Cell2.setCellStyle(textStyle);
						}
						// R13 Col D
						Cell R13Cell3 = row.createCell(3);
						if (record.getR13Overdrafts() != null) {
							R13Cell3.setCellValue(record.getR13Overdrafts().doubleValue());
							R13Cell3.setCellStyle(numberStyle);
						} else {
							R13Cell3.setCellValue("");
							R13Cell3.setCellStyle(textStyle);
						}

						// R13 Col E
						Cell R13Cell4 = row.createCell(4);
						if (record.getR13OtherInstallmentLoans() != null) {
							R13Cell4.setCellValue(record.getR13OtherInstallmentLoans().doubleValue());
							R13Cell4.setCellStyle(numberStyle);
						} else {
							R13Cell4.setCellValue("");
							R13Cell4.setCellStyle(textStyle);
						}
						// R14 Col B
						row = sheet.getRow(13); // Row index 13 is Excel Row 14
						// R14 Col B
						Cell R14Cell1 = row.createCell(1);
						if (record1.getR14FactoringDebtors() != null) {
							R14Cell1.setCellValue(record1.getR14FactoringDebtors().doubleValue());
							R14Cell1.setCellStyle(numberStyle);
						} else {
							R14Cell1.setCellValue("");
							R14Cell1.setCellStyle(textStyle);
						}

						// R14 Col C
						Cell R14Cell2 = row.createCell(2);
						if (record1.getR14Leasing() != null) {
							R14Cell2.setCellValue(record1.getR14Leasing().doubleValue());
							R14Cell2.setCellStyle(numberStyle);
						} else {
							R14Cell2.setCellValue("");
							R14Cell2.setCellStyle(textStyle);
						}

						// R14 Col D
						Cell R14Cell3 = row.createCell(3);
						if (record.getR14Overdrafts() != null) {
							R14Cell3.setCellValue(record.getR14Overdrafts().doubleValue());
							R14Cell3.setCellStyle(numberStyle);
						} else {
							R14Cell3.setCellValue("");
							R14Cell3.setCellStyle(textStyle);
						}

						// R14 Col E
						Cell R14Cell4 = row.createCell(4);
						if (record.getR14OtherInstallmentLoans() != null) {
							R14Cell4.setCellValue(record.getR14OtherInstallmentLoans().doubleValue());
							R14Cell4.setCellStyle(numberStyle);
						} else {
							R14Cell4.setCellValue("");
							R14Cell4.setCellStyle(textStyle);
						}

						// --- R16 (Row Index 15) ---
						row = sheet.getRow(15);
						Cell R16Cell1 = row.createCell(1);
						if (record1.getR16FactoringDebtors() != null) {
							R16Cell1.setCellValue(record1.getR16FactoringDebtors().doubleValue());
							R16Cell1.setCellStyle(numberStyle);
						} else {
							R16Cell1.setCellValue("");
							R16Cell1.setCellStyle(textStyle);
						}
						Cell R16Cell2 = row.createCell(2);
						if (record1.getR16Leasing() != null) {
							R16Cell2.setCellValue(record1.getR16Leasing().doubleValue());
							R16Cell2.setCellStyle(numberStyle);
						} else {
							R16Cell2.setCellValue("");
							R16Cell2.setCellStyle(textStyle);
						}
						Cell R16Cell3 = row.createCell(3);
						if (record.getR16Overdrafts() != null) {
							R16Cell3.setCellValue(record.getR16Overdrafts().doubleValue());
							R16Cell3.setCellStyle(numberStyle);
						} else {
							R16Cell3.setCellValue("");
							R16Cell3.setCellStyle(textStyle);
						}
						Cell R16Cell4 = row.createCell(4);
						if (record.getR16OtherInstallmentLoans() != null) {
							R16Cell4.setCellValue(record.getR16OtherInstallmentLoans().doubleValue());
							R16Cell4.setCellStyle(numberStyle);
						} else {
							R16Cell4.setCellValue("");
							R16Cell4.setCellStyle(textStyle);
						}

						// --- R17 (Row Index 16) ---
						row = sheet.getRow(16);
						Cell R17Cell1 = row.createCell(1);
						if (record1.getR17FactoringDebtors() != null) {
							R17Cell1.setCellValue(record1.getR17FactoringDebtors().doubleValue());
							R17Cell1.setCellStyle(numberStyle);
						} else {
							R17Cell1.setCellValue("");
							R17Cell1.setCellStyle(textStyle);
						}
						Cell R17Cell2 = row.createCell(2);
						if (record1.getR17Leasing() != null) {
							R17Cell2.setCellValue(record1.getR17Leasing().doubleValue());
							R17Cell2.setCellStyle(numberStyle);
						} else {
							R17Cell2.setCellValue("");
							R17Cell2.setCellStyle(textStyle);
						}
						Cell R17Cell3 = row.createCell(3);
						if (record.getR17Overdrafts() != null) {
							R17Cell3.setCellValue(record.getR17Overdrafts().doubleValue());
							R17Cell3.setCellStyle(numberStyle);
						} else {
							R17Cell3.setCellValue("");
							R17Cell3.setCellStyle(textStyle);
						}
						Cell R17Cell4 = row.createCell(4);
						if (record.getR17OtherInstallmentLoans() != null) {
							R17Cell4.setCellValue(record.getR17OtherInstallmentLoans().doubleValue());
							R17Cell4.setCellStyle(numberStyle);
						} else {
							R17Cell4.setCellValue("");
							R17Cell4.setCellStyle(textStyle);
						}

						// --- R18 (Row Index 17) ---
						row = sheet.getRow(17);
						Cell R18Cell1 = row.createCell(1);
						if (record1.getR18FactoringDebtors() != null) {
							R18Cell1.setCellValue(record1.getR18FactoringDebtors().doubleValue());
							R18Cell1.setCellStyle(numberStyle);
						} else {
							R18Cell1.setCellValue("");
							R18Cell1.setCellStyle(textStyle);
						}
						Cell R18Cell2 = row.createCell(2);
						if (record1.getR18Leasing() != null) {
							R18Cell2.setCellValue(record1.getR18Leasing().doubleValue());
							R18Cell2.setCellStyle(numberStyle);
						} else {
							R18Cell2.setCellValue("");
							R18Cell2.setCellStyle(textStyle);
						}
						Cell R18Cell3 = row.createCell(3);
						if (record.getR18Overdrafts() != null) {
							R18Cell3.setCellValue(record.getR18Overdrafts().doubleValue());
							R18Cell3.setCellStyle(numberStyle);
						} else {
							R18Cell3.setCellValue("");
							R18Cell3.setCellStyle(textStyle);
						}
						Cell R18Cell4 = row.createCell(4);
						if (record.getR18OtherInstallmentLoans() != null) {
							R18Cell4.setCellValue(record.getR18OtherInstallmentLoans().doubleValue());
							R18Cell4.setCellStyle(numberStyle);
						} else {
							R18Cell4.setCellValue("");
							R18Cell4.setCellStyle(textStyle);
						}

						// --- R19 (Row Index 18) ---
						row = sheet.getRow(18);
						Cell R19Cell1 = row.createCell(1);
						if (record1.getR19FactoringDebtors() != null) {
							R19Cell1.setCellValue(record1.getR19FactoringDebtors().doubleValue());
							R19Cell1.setCellStyle(numberStyle);
						} else {
							R19Cell1.setCellValue("");
							R19Cell1.setCellStyle(textStyle);
						}
						Cell R19Cell2 = row.createCell(2);
						if (record1.getR19Leasing() != null) {
							R19Cell2.setCellValue(record1.getR19Leasing().doubleValue());
							R19Cell2.setCellStyle(numberStyle);
						} else {
							R19Cell2.setCellValue("");
							R19Cell2.setCellStyle(textStyle);
						}
						Cell R19Cell3 = row.createCell(3);
						if (record.getR19Overdrafts() != null) {
							R19Cell3.setCellValue(record.getR19Overdrafts().doubleValue());
							R19Cell3.setCellStyle(numberStyle);
						} else {
							R19Cell3.setCellValue("");
							R19Cell3.setCellStyle(textStyle);
						}
						Cell R19Cell4 = row.createCell(4);
						if (record.getR19OtherInstallmentLoans() != null) {
							R19Cell4.setCellValue(record.getR19OtherInstallmentLoans().doubleValue());
							R19Cell4.setCellStyle(numberStyle);
						} else {
							R19Cell4.setCellValue("");
							R19Cell4.setCellStyle(textStyle);
						}
						// --- R20 (Row Index 19) ---
						row = sheet.getRow(19);
						Cell R20Cell1 = row.createCell(1);
						if (record1.getR20FactoringDebtors() != null) {
							R20Cell1.setCellValue(record1.getR20FactoringDebtors().doubleValue());
							R20Cell1.setCellStyle(numberStyle);
						} else {
							R20Cell1.setCellValue("");
							R20Cell1.setCellStyle(textStyle);
						}
						Cell R20Cell2 = row.createCell(2);
						if (record1.getR20Leasing() != null) {
							R20Cell2.setCellValue(record1.getR20Leasing().doubleValue());
							R20Cell2.setCellStyle(numberStyle);
						} else {
							R20Cell2.setCellValue("");
							R20Cell2.setCellStyle(textStyle);
						}
						Cell R20Cell3 = row.createCell(3);
						if (record.getR20Overdrafts() != null) {
							R20Cell3.setCellValue(record.getR20Overdrafts().doubleValue());
							R20Cell3.setCellStyle(numberStyle);
						} else {
							R20Cell3.setCellValue("");
							R20Cell3.setCellStyle(textStyle);
						}
						Cell R20Cell4 = row.createCell(4);
						if (record.getR20OtherInstallmentLoans() != null) {
							R20Cell4.setCellValue(record.getR20OtherInstallmentLoans().doubleValue());
							R20Cell4.setCellStyle(numberStyle);
						} else {
							R20Cell4.setCellValue("");
							R20Cell4.setCellStyle(textStyle);
						}

						// --- R21 (Row Index 20) ---
						row = sheet.getRow(20);
						Cell R21Cell1 = row.createCell(1);
						if (record1.getR21FactoringDebtors() != null) {
							R21Cell1.setCellValue(record1.getR21FactoringDebtors().doubleValue());
							R21Cell1.setCellStyle(numberStyle);
						} else {
							R21Cell1.setCellValue("");
							R21Cell1.setCellStyle(textStyle);
						}
						Cell R21Cell2 = row.createCell(2);
						if (record1.getR21Leasing() != null) {
							R21Cell2.setCellValue(record1.getR21Leasing().doubleValue());
							R21Cell2.setCellStyle(numberStyle);
						} else {
							R21Cell2.setCellValue("");
							R21Cell2.setCellStyle(textStyle);
						}
						Cell R21Cell3 = row.createCell(3);
						if (record.getR21Overdrafts() != null) {
							R21Cell3.setCellValue(record.getR21Overdrafts().doubleValue());
							R21Cell3.setCellStyle(numberStyle);
						} else {
							R21Cell3.setCellValue("");
							R21Cell3.setCellStyle(textStyle);
						}
						Cell R21Cell4 = row.createCell(4);
						if (record.getR21OtherInstallmentLoans() != null) {
							R21Cell4.setCellValue(record.getR21OtherInstallmentLoans().doubleValue());
							R21Cell4.setCellStyle(numberStyle);
						} else {
							R21Cell4.setCellValue("");
							R21Cell4.setCellStyle(textStyle);
						}

						// --- R22 (Row Index 21) ---
						row = sheet.getRow(21);
						Cell R22Cell1 = row.createCell(1);
						if (record1.getR22FactoringDebtors() != null) {
							R22Cell1.setCellValue(record1.getR22FactoringDebtors().doubleValue());
							R22Cell1.setCellStyle(numberStyle);
						} else {
							R22Cell1.setCellValue("");
							R22Cell1.setCellStyle(textStyle);
						}
						Cell R22Cell2 = row.createCell(2);
						if (record1.getR22Leasing() != null) {
							R22Cell2.setCellValue(record1.getR22Leasing().doubleValue());
							R22Cell2.setCellStyle(numberStyle);
						} else {
							R22Cell2.setCellValue("");
							R22Cell2.setCellStyle(textStyle);
						}
						Cell R22Cell3 = row.createCell(3);
						if (record.getR22Overdrafts() != null) {
							R22Cell3.setCellValue(record.getR22Overdrafts().doubleValue());
							R22Cell3.setCellStyle(numberStyle);
						} else {
							R22Cell3.setCellValue("");
							R22Cell3.setCellStyle(textStyle);
						}
						Cell R22Cell4 = row.createCell(4);
						if (record.getR22OtherInstallmentLoans() != null) {
							R22Cell4.setCellValue(record.getR22OtherInstallmentLoans().doubleValue());
							R22Cell4.setCellStyle(numberStyle);
						} else {
							R22Cell4.setCellValue("");
							R22Cell4.setCellStyle(textStyle);
						}

						// --- R23 (Row Index 22) ---
						row = sheet.getRow(22);
						Cell R23Cell1 = row.createCell(1);
						if (record1.getR23FactoringDebtors() != null) {
							R23Cell1.setCellValue(record1.getR23FactoringDebtors().doubleValue());
							R23Cell1.setCellStyle(numberStyle);
						} else {
							R23Cell1.setCellValue("");
							R23Cell1.setCellStyle(textStyle);
						}
						Cell R23Cell2 = row.createCell(2);
						if (record1.getR23Leasing() != null) {
							R23Cell2.setCellValue(record1.getR23Leasing().doubleValue());
							R23Cell2.setCellStyle(numberStyle);
						} else {
							R23Cell2.setCellValue("");
							R23Cell2.setCellStyle(textStyle);
						}
						Cell R23Cell3 = row.createCell(3);
						if (record.getR23Overdrafts() != null) {
							R23Cell3.setCellValue(record.getR23Overdrafts().doubleValue());
							R23Cell3.setCellStyle(numberStyle);
						} else {
							R23Cell3.setCellValue("");
							R23Cell3.setCellStyle(textStyle);
						}
						Cell R23Cell4 = row.createCell(4);
						if (record.getR23OtherInstallmentLoans() != null) {
							R23Cell4.setCellValue(record.getR23OtherInstallmentLoans().doubleValue());
							R23Cell4.setCellStyle(numberStyle);
						} else {
							R23Cell4.setCellValue("");
							R23Cell4.setCellStyle(textStyle);
						}

						// --- R24 (Row Index 23) ---
						row = sheet.getRow(23);
						Cell R24Cell1 = row.createCell(1);
						if (record1.getR24FactoringDebtors() != null) {
							R24Cell1.setCellValue(record1.getR24FactoringDebtors().doubleValue());
							R24Cell1.setCellStyle(numberStyle);
						} else {
							R24Cell1.setCellValue("");
							R24Cell1.setCellStyle(textStyle);
						}
						Cell R24Cell2 = row.createCell(2);
						if (record1.getR24Leasing() != null) {
							R24Cell2.setCellValue(record1.getR24Leasing().doubleValue());
							R24Cell2.setCellStyle(numberStyle);
						} else {
							R24Cell2.setCellValue("");
							R24Cell2.setCellStyle(textStyle);
						}
						Cell R24Cell3 = row.createCell(3);
						if (record.getR24Overdrafts() != null) {
							R24Cell3.setCellValue(record.getR24Overdrafts().doubleValue());
							R24Cell3.setCellStyle(numberStyle);
						} else {
							R24Cell3.setCellValue("");
							R24Cell3.setCellStyle(textStyle);
						}
						Cell R24Cell4 = row.createCell(4);
						if (record.getR24OtherInstallmentLoans() != null) {
							R24Cell4.setCellValue(record.getR24OtherInstallmentLoans().doubleValue());
							R24Cell4.setCellStyle(numberStyle);
						} else {
							R24Cell4.setCellValue("");
							R24Cell4.setCellStyle(textStyle);
						}

						// --- R25 (Row Index 24) ---
						row = sheet.getRow(24);
						Cell R25Cell1 = row.createCell(1);
						if (record1.getR25FactoringDebtors() != null) {
							R25Cell1.setCellValue(record1.getR25FactoringDebtors().doubleValue());
							R25Cell1.setCellStyle(numberStyle);
						} else {
							R25Cell1.setCellValue("");
							R25Cell1.setCellStyle(textStyle);
						}
						Cell R25Cell2 = row.createCell(2);
						if (record1.getR25Leasing() != null) {
							R25Cell2.setCellValue(record1.getR25Leasing().doubleValue());
							R25Cell2.setCellStyle(numberStyle);
						} else {
							R25Cell2.setCellValue("");
							R25Cell2.setCellStyle(textStyle);
						}
						Cell R25Cell3 = row.createCell(3);
						if (record.getR25Overdrafts() != null) {
							R25Cell3.setCellValue(record.getR25Overdrafts().doubleValue());
							R25Cell3.setCellStyle(numberStyle);
						} else {
							R25Cell3.setCellValue("");
							R25Cell3.setCellStyle(textStyle);
						}
						Cell R25Cell4 = row.createCell(4);
						if (record.getR25OtherInstallmentLoans() != null) {
							R25Cell4.setCellValue(record.getR25OtherInstallmentLoans().doubleValue());
							R25Cell4.setCellStyle(numberStyle);
						} else {
							R25Cell4.setCellValue("");
							R25Cell4.setCellStyle(textStyle);
						}

						// --- R26 (Row Index 25) ---
						row = sheet.getRow(25);
						Cell R26Cell1 = row.createCell(1);
						if (record1.getR26FactoringDebtors() != null) {
							R26Cell1.setCellValue(record1.getR26FactoringDebtors().doubleValue());
							R26Cell1.setCellStyle(numberStyle);
						} else {
							R26Cell1.setCellValue("");
							R26Cell1.setCellStyle(textStyle);
						}
						Cell R26Cell2 = row.createCell(2);
						if (record1.getR26Leasing() != null) {
							R26Cell2.setCellValue(record1.getR26Leasing().doubleValue());
							R26Cell2.setCellStyle(numberStyle);
						} else {
							R26Cell2.setCellValue("");
							R26Cell2.setCellStyle(textStyle);
						}
						Cell R26Cell3 = row.createCell(3);
						if (record.getR26Overdrafts() != null) {
							R26Cell3.setCellValue(record.getR26Overdrafts().doubleValue());
							R26Cell3.setCellStyle(numberStyle);
						} else {
							R26Cell3.setCellValue("");
							R26Cell3.setCellStyle(textStyle);
						}
						Cell R26Cell4 = row.createCell(4);
						if (record.getR26OtherInstallmentLoans() != null) {
							R26Cell4.setCellValue(record.getR26OtherInstallmentLoans().doubleValue());
							R26Cell4.setCellStyle(numberStyle);
						} else {
							R26Cell4.setCellValue("");
							R26Cell4.setCellStyle(textStyle);
						}

						// --- R27 (Row Index 26) ---
						row = sheet.getRow(26);
						Cell R27Cell1 = row.createCell(1);
						if (record1.getR27FactoringDebtors() != null) {
							R27Cell1.setCellValue(record1.getR27FactoringDebtors().doubleValue());
							R27Cell1.setCellStyle(numberStyle);
						} else {
							R27Cell1.setCellValue("");
							R27Cell1.setCellStyle(textStyle);
						}
						Cell R27Cell2 = row.createCell(2);
						if (record1.getR27Leasing() != null) {
							R27Cell2.setCellValue(record1.getR27Leasing().doubleValue());
							R27Cell2.setCellStyle(numberStyle);
						} else {
							R27Cell2.setCellValue("");
							R27Cell2.setCellStyle(textStyle);
						}
						Cell R27Cell3 = row.createCell(3);
						if (record.getR27Overdrafts() != null) {
							R27Cell3.setCellValue(record.getR27Overdrafts().doubleValue());
							R27Cell3.setCellStyle(numberStyle);
						} else {
							R27Cell3.setCellValue("");
							R27Cell3.setCellStyle(textStyle);
						}
						Cell R27Cell4 = row.createCell(4);
						if (record.getR27OtherInstallmentLoans() != null) {
							R27Cell4.setCellValue(record.getR27OtherInstallmentLoans().doubleValue());
							R27Cell4.setCellStyle(numberStyle);
						} else {
							R27Cell4.setCellValue("");
							R27Cell4.setCellStyle(textStyle);
						}

						// --- R28 (Row Index 27) ---
						row = sheet.getRow(27);
						Cell R28Cell1 = row.createCell(1);
						if (record1.getR28FactoringDebtors() != null) {
							R28Cell1.setCellValue(record1.getR28FactoringDebtors().doubleValue());
							R28Cell1.setCellStyle(numberStyle);
						} else {
							R28Cell1.setCellValue("");
							R28Cell1.setCellStyle(textStyle);
						}
						Cell R28Cell2 = row.createCell(2);
						if (record1.getR28Leasing() != null) {
							R28Cell2.setCellValue(record1.getR28Leasing().doubleValue());
							R28Cell2.setCellStyle(numberStyle);
						} else {
							R28Cell2.setCellValue("");
							R28Cell2.setCellStyle(textStyle);
						}
						Cell R28Cell3 = row.createCell(3);
						if (record.getR28Overdrafts() != null) {
							R28Cell3.setCellValue(record.getR28Overdrafts().doubleValue());
							R28Cell3.setCellStyle(numberStyle);
						} else {
							R28Cell3.setCellValue("");
							R28Cell3.setCellStyle(textStyle);
						}
						Cell R28Cell4 = row.createCell(4);
						if (record.getR28OtherInstallmentLoans() != null) {
							R28Cell4.setCellValue(record.getR28OtherInstallmentLoans().doubleValue());
							R28Cell4.setCellStyle(numberStyle);
						} else {
							R28Cell4.setCellValue("");
							R28Cell4.setCellStyle(textStyle);
						}

						// --- R30 (Row Index 29) ---
						row = sheet.getRow(29);
						Cell R30Cell1 = row.createCell(1);
						if (record1.getR30FactoringDebtors() != null) {
							R30Cell1.setCellValue(record1.getR30FactoringDebtors().doubleValue());
							R30Cell1.setCellStyle(numberStyle);
						} else {
							R30Cell1.setCellValue("");
							R30Cell1.setCellStyle(textStyle);
						}
						Cell R30Cell2 = row.createCell(2);
						if (record1.getR30Leasing() != null) {
							R30Cell2.setCellValue(record1.getR30Leasing().doubleValue());
							R30Cell2.setCellStyle(numberStyle);
						} else {
							R30Cell2.setCellValue("");
							R30Cell2.setCellStyle(textStyle);
						}
						Cell R30Cell3 = row.createCell(3);
						if (record.getR30Overdrafts() != null) {
							R30Cell3.setCellValue(record.getR30Overdrafts().doubleValue());
							R30Cell3.setCellStyle(numberStyle);
						} else {
							R30Cell3.setCellValue("");
							R30Cell3.setCellStyle(textStyle);
						}
						Cell R30Cell4 = row.createCell(4);
						if (record.getR30OtherInstallmentLoans() != null) {
							R30Cell4.setCellValue(record.getR30OtherInstallmentLoans().doubleValue());
							R30Cell4.setCellStyle(numberStyle);
						} else {
							R30Cell4.setCellValue("");
							R30Cell4.setCellStyle(textStyle);
						}

						// --- R31 (Row Index 30) ---
						row = sheet.getRow(30);
						Cell R31Cell1 = row.createCell(1);
						if (record1.getR31FactoringDebtors() != null) {
							R31Cell1.setCellValue(record1.getR31FactoringDebtors().doubleValue());
							R31Cell1.setCellStyle(numberStyle);
						} else {
							R31Cell1.setCellValue("");
							R31Cell1.setCellStyle(textStyle);
						}
						Cell R31Cell2 = row.createCell(2);
						if (record1.getR31Leasing() != null) {
							R31Cell2.setCellValue(record1.getR31Leasing().doubleValue());
							R31Cell2.setCellStyle(numberStyle);
						} else {
							R31Cell2.setCellValue("");
							R31Cell2.setCellStyle(textStyle);
						}
						Cell R31Cell3 = row.createCell(3);
						if (record.getR31Overdrafts() != null) {
							R31Cell3.setCellValue(record.getR31Overdrafts().doubleValue());
							R31Cell3.setCellStyle(numberStyle);
						} else {
							R31Cell3.setCellValue("");
							R31Cell3.setCellStyle(textStyle);
						}
						Cell R31Cell4 = row.createCell(4);
						if (record.getR31OtherInstallmentLoans() != null) {
							R31Cell4.setCellValue(record.getR31OtherInstallmentLoans().doubleValue());
							R31Cell4.setCellStyle(numberStyle);
						} else {
							R31Cell4.setCellValue("");
							R31Cell4.setCellStyle(textStyle);
						}

						// --- R32 (Row Index 31) ---
						row = sheet.getRow(31);
						Cell R32Cell1 = row.createCell(1);
						if (record1.getR32FactoringDebtors() != null) {
							R32Cell1.setCellValue(record1.getR32FactoringDebtors().doubleValue());
							R32Cell1.setCellStyle(numberStyle);
						} else {
							R32Cell1.setCellValue("");
							R32Cell1.setCellStyle(textStyle);
						}
						Cell R32Cell2 = row.createCell(2);
						if (record1.getR32Leasing() != null) {
							R32Cell2.setCellValue(record1.getR32Leasing().doubleValue());
							R32Cell2.setCellStyle(numberStyle);
						} else {
							R32Cell2.setCellValue("");
							R32Cell2.setCellStyle(textStyle);
						}
						Cell R32Cell3 = row.createCell(3);
						if (record.getR32Overdrafts() != null) {
							R32Cell3.setCellValue(record.getR32Overdrafts().doubleValue());
							R32Cell3.setCellStyle(numberStyle);
						} else {
							R32Cell3.setCellValue("");
							R32Cell3.setCellStyle(textStyle);
						}
						Cell R32Cell4 = row.createCell(4);
						if (record.getR32OtherInstallmentLoans() != null) {
							R32Cell4.setCellValue(record.getR32OtherInstallmentLoans().doubleValue());
							R32Cell4.setCellStyle(numberStyle);
						} else {
							R32Cell4.setCellValue("");
							R32Cell4.setCellStyle(textStyle);
						}

						// --- R33 (Row Index 32) ---
						row = sheet.getRow(32);
						Cell R33Cell1 = row.createCell(1);
						if (record1.getR33FactoringDebtors() != null) {
							R33Cell1.setCellValue(record1.getR33FactoringDebtors().doubleValue());
							R33Cell1.setCellStyle(numberStyle);
						} else {
							R33Cell1.setCellValue("");
							R33Cell1.setCellStyle(textStyle);
						}
						Cell R33Cell2 = row.createCell(2);
						if (record1.getR33Leasing() != null) {
							R33Cell2.setCellValue(record1.getR33Leasing().doubleValue());
							R33Cell2.setCellStyle(numberStyle);
						} else {
							R33Cell2.setCellValue("");
							R33Cell2.setCellStyle(textStyle);
						}
						Cell R33Cell3 = row.createCell(3);
						if (record.getR33Overdrafts() != null) {
							R33Cell3.setCellValue(record.getR33Overdrafts().doubleValue());
							R33Cell3.setCellStyle(numberStyle);
						} else {
							R33Cell3.setCellValue("");
							R33Cell3.setCellStyle(textStyle);
						}
						Cell R33Cell4 = row.createCell(4);
						if (record.getR33OtherInstallmentLoans() != null) {
							R33Cell4.setCellValue(record.getR33OtherInstallmentLoans().doubleValue());
							R33Cell4.setCellStyle(numberStyle);
						} else {
							R33Cell4.setCellValue("");
							R33Cell4.setCellStyle(textStyle);
						}

						// --- R34 (Row Index 33) ---
						row = sheet.getRow(33);
						Cell R34Cell1 = row.createCell(1);
						if (record1.getR34FactoringDebtors() != null) {
							R34Cell1.setCellValue(record1.getR34FactoringDebtors().doubleValue());
							R34Cell1.setCellStyle(numberStyle);
						} else {
							R34Cell1.setCellValue("");
							R34Cell1.setCellStyle(textStyle);
						}
						Cell R34Cell2 = row.createCell(2);
						if (record1.getR34Leasing() != null) {
							R34Cell2.setCellValue(record1.getR34Leasing().doubleValue());
							R34Cell2.setCellStyle(numberStyle);
						} else {
							R34Cell2.setCellValue("");
							R34Cell2.setCellStyle(textStyle);
						}
						Cell R34Cell3 = row.createCell(3);
						if (record.getR34Overdrafts() != null) {
							R34Cell3.setCellValue(record.getR34Overdrafts().doubleValue());
							R34Cell3.setCellStyle(numberStyle);
						} else {
							R34Cell3.setCellValue("");
							R34Cell3.setCellStyle(textStyle);
						}
						Cell R34Cell4 = row.createCell(4);
						if (record.getR34OtherInstallmentLoans() != null) {
							R34Cell4.setCellValue(record.getR34OtherInstallmentLoans().doubleValue());
							R34Cell4.setCellStyle(numberStyle);
						} else {
							R34Cell4.setCellValue("");
							R34Cell4.setCellStyle(textStyle);
						}

						// --- R35 (Row Index 34) ---
						row = sheet.getRow(34);
						Cell R35Cell1 = row.createCell(1);
						if (record1.getR35FactoringDebtors() != null) {
							R35Cell1.setCellValue(record1.getR35FactoringDebtors().doubleValue());
							R35Cell1.setCellStyle(numberStyle);
						} else {
							R35Cell1.setCellValue("");
							R35Cell1.setCellStyle(textStyle);
						}
						Cell R35Cell2 = row.createCell(2);
						if (record1.getR35Leasing() != null) {
							R35Cell2.setCellValue(record1.getR35Leasing().doubleValue());
							R35Cell2.setCellStyle(numberStyle);
						} else {
							R35Cell2.setCellValue("");
							R35Cell2.setCellStyle(textStyle);
						}
						Cell R35Cell3 = row.createCell(3);
						if (record.getR35Overdrafts() != null) {
							R35Cell3.setCellValue(record.getR35Overdrafts().doubleValue());
							R35Cell3.setCellStyle(numberStyle);
						} else {
							R35Cell3.setCellValue("");
							R35Cell3.setCellStyle(textStyle);
						}
						Cell R35Cell4 = row.createCell(4);
						if (record.getR35OtherInstallmentLoans() != null) {
							R35Cell4.setCellValue(record.getR35OtherInstallmentLoans().doubleValue());
							R35Cell4.setCellStyle(numberStyle);
						} else {
							R35Cell4.setCellValue("");
							R35Cell4.setCellStyle(textStyle);
						}

						// --- R36 (Row Index 35) ---
						row = sheet.getRow(35);
						Cell R36Cell1 = row.createCell(1);
						if (record1.getR36FactoringDebtors() != null) {
							R36Cell1.setCellValue(record1.getR36FactoringDebtors().doubleValue());
							R36Cell1.setCellStyle(numberStyle);
						} else {
							R36Cell1.setCellValue("");
							R36Cell1.setCellStyle(textStyle);
						}
						Cell R36Cell2 = row.createCell(2);
						if (record1.getR36Leasing() != null) {
							R36Cell2.setCellValue(record1.getR36Leasing().doubleValue());
							R36Cell2.setCellStyle(numberStyle);
						} else {
							R36Cell2.setCellValue("");
							R36Cell2.setCellStyle(textStyle);
						}
						Cell R36Cell3 = row.createCell(3);
						if (record.getR36Overdrafts() != null) {
							R36Cell3.setCellValue(record.getR36Overdrafts().doubleValue());
							R36Cell3.setCellStyle(numberStyle);
						} else {
							R36Cell3.setCellValue("");
							R36Cell3.setCellStyle(textStyle);
						}
						Cell R36Cell4 = row.createCell(4);
						if (record.getR36OtherInstallmentLoans() != null) {
							R36Cell4.setCellValue(record.getR36OtherInstallmentLoans().doubleValue());
							R36Cell4.setCellStyle(numberStyle);
						} else {
							R36Cell4.setCellValue("");
							R36Cell4.setCellStyle(textStyle);
						}

						// --- R37 (Row Index 36) ---
						row = sheet.getRow(36);
						Cell R37Cell1 = row.createCell(1);
						if (record1.getR37FactoringDebtors() != null) {
							R37Cell1.setCellValue(record1.getR37FactoringDebtors().doubleValue());
							R37Cell1.setCellStyle(numberStyle);
						} else {
							R37Cell1.setCellValue("");
							R37Cell1.setCellStyle(textStyle);
						}
						Cell R37Cell2 = row.createCell(2);
						if (record1.getR37Leasing() != null) {
							R37Cell2.setCellValue(record1.getR37Leasing().doubleValue());
							R37Cell2.setCellStyle(numberStyle);
						} else {
							R37Cell2.setCellValue("");
							R37Cell2.setCellStyle(textStyle);
						}
						Cell R37Cell3 = row.createCell(3);
						if (record.getR37Overdrafts() != null) {
							R37Cell3.setCellValue(record.getR37Overdrafts().doubleValue());
							R37Cell3.setCellStyle(numberStyle);
						} else {
							R37Cell3.setCellValue("");
							R37Cell3.setCellStyle(textStyle);
						}
						Cell R37Cell4 = row.createCell(4);
						if (record.getR37OtherInstallmentLoans() != null) {
							R37Cell4.setCellValue(record.getR37OtherInstallmentLoans().doubleValue());
							R37Cell4.setCellStyle(numberStyle);
						} else {
							R37Cell4.setCellValue("");
							R37Cell4.setCellStyle(textStyle);
						}
						// --- R39 (Row Index 38) ---
						row = sheet.getRow(38);
						Cell R39Cell1 = row.createCell(1);
						if (record1.getR39FactoringDebtors() != null) {
							R39Cell1.setCellValue(record1.getR39FactoringDebtors().doubleValue());
							R39Cell1.setCellStyle(numberStyle);
						} else {
							R39Cell1.setCellValue("");
							R39Cell1.setCellStyle(textStyle);
						}
						Cell R39Cell2 = row.createCell(2);
						if (record1.getR39Leasing() != null) {
							R39Cell2.setCellValue(record1.getR39Leasing().doubleValue());
							R39Cell2.setCellStyle(numberStyle);
						} else {
							R39Cell2.setCellValue("");
							R39Cell2.setCellStyle(textStyle);
						}
						Cell R39Cell3 = row.createCell(3);
						if (record.getR39Overdrafts() != null) {
							R39Cell3.setCellValue(record.getR39Overdrafts().doubleValue());
							R39Cell3.setCellStyle(numberStyle);
						} else {
							R39Cell3.setCellValue("");
							R39Cell3.setCellStyle(textStyle);
						}
						Cell R39Cell4 = row.createCell(4);
						if (record.getR39OtherInstallmentLoans() != null) {
							R39Cell4.setCellValue(record.getR39OtherInstallmentLoans().doubleValue());
							R39Cell4.setCellStyle(numberStyle);
						} else {
							R39Cell4.setCellValue("");
							R39Cell4.setCellStyle(textStyle);
						}

						// --- R40 (Row Index 39) ---
						row = sheet.getRow(39);
						Cell R40Cell1 = row.createCell(1);
						if (record1.getR40FactoringDebtors() != null) {
							R40Cell1.setCellValue(record1.getR40FactoringDebtors().doubleValue());
							R40Cell1.setCellStyle(numberStyle);
						} else {
							R40Cell1.setCellValue("");
							R40Cell1.setCellStyle(textStyle);
						}
						Cell R40Cell2 = row.createCell(2);
						if (record1.getR40Leasing() != null) {
							R40Cell2.setCellValue(record1.getR40Leasing().doubleValue());
							R40Cell2.setCellStyle(numberStyle);
						} else {
							R40Cell2.setCellValue("");
							R40Cell2.setCellStyle(textStyle);
						}
						Cell R40Cell3 = row.createCell(3);
						if (record.getR40Overdrafts() != null) {
							R40Cell3.setCellValue(record.getR40Overdrafts().doubleValue());
							R40Cell3.setCellStyle(numberStyle);
						} else {
							R40Cell3.setCellValue("");
							R40Cell3.setCellStyle(textStyle);
						}
						Cell R40Cell4 = row.createCell(4);
						if (record.getR40OtherInstallmentLoans() != null) {
							R40Cell4.setCellValue(record.getR40OtherInstallmentLoans().doubleValue());
							R40Cell4.setCellStyle(numberStyle);
						} else {
							R40Cell4.setCellValue("");
							R40Cell4.setCellStyle(textStyle);
						}
						// --- R42 (Row Index 41) ---
						row = sheet.getRow(41);
						Cell R42Cell1 = row.createCell(1);
						if (record1.getR42FactoringDebtors() != null) {
							R42Cell1.setCellValue(record1.getR42FactoringDebtors().doubleValue());
							R42Cell1.setCellStyle(numberStyle);
						} else {
							R42Cell1.setCellValue("");
							R42Cell1.setCellStyle(textStyle);
						}
						Cell R42Cell2 = row.createCell(2);
						if (record1.getR42Leasing() != null) {
							R42Cell2.setCellValue(record1.getR42Leasing().doubleValue());
							R42Cell2.setCellStyle(numberStyle);
						} else {
							R42Cell2.setCellValue("");
							R42Cell2.setCellStyle(textStyle);
						}
						Cell R42Cell3 = row.createCell(3);
						if (record.getR42Overdrafts() != null) {
							R42Cell3.setCellValue(record.getR42Overdrafts().doubleValue());
							R42Cell3.setCellStyle(numberStyle);
						} else {
							R42Cell3.setCellValue("");
							R42Cell3.setCellStyle(textStyle);
						}
						Cell R42Cell4 = row.createCell(4);
						if (record.getR42OtherInstallmentLoans() != null) {
							R42Cell4.setCellValue(record.getR42OtherInstallmentLoans().doubleValue());
							R42Cell4.setCellStyle(numberStyle);
						} else {
							R42Cell4.setCellValue("");
							R42Cell4.setCellStyle(textStyle);
						}

						// --- R43 (Row Index 42) ---
						row = sheet.getRow(42);
						Cell R43Cell1 = row.createCell(1);
						if (record1.getR43FactoringDebtors() != null) {
							R43Cell1.setCellValue(record1.getR43FactoringDebtors().doubleValue());
							R43Cell1.setCellStyle(numberStyle);
						} else {
							R43Cell1.setCellValue("");
							R43Cell1.setCellStyle(textStyle);
						}
						Cell R43Cell2 = row.createCell(2);
						if (record1.getR43Leasing() != null) {
							R43Cell2.setCellValue(record1.getR43Leasing().doubleValue());
							R43Cell2.setCellStyle(numberStyle);
						} else {
							R43Cell2.setCellValue("");
							R43Cell2.setCellStyle(textStyle);
						}
						Cell R43Cell3 = row.createCell(3);
						if (record.getR43Overdrafts() != null) {
							R43Cell3.setCellValue(record.getR43Overdrafts().doubleValue());
							R43Cell3.setCellStyle(numberStyle);
						} else {
							R43Cell3.setCellValue("");
							R43Cell3.setCellStyle(textStyle);
						}
						Cell R43Cell4 = row.createCell(4);
						if (record.getR43OtherInstallmentLoans() != null) {
							R43Cell4.setCellValue(record.getR43OtherInstallmentLoans().doubleValue());
							R43Cell4.setCellStyle(numberStyle);
						} else {
							R43Cell4.setCellValue("");
							R43Cell4.setCellStyle(textStyle);
						}
						// --- R45 (Row Index 44) ---
						row = sheet.getRow(44);
						Cell R45Cell1 = row.createCell(1);
						if (record1.getR45FactoringDebtors() != null) {
							R45Cell1.setCellValue(record1.getR45FactoringDebtors().doubleValue());
							R45Cell1.setCellStyle(numberStyle);
						} else {
							R45Cell1.setCellValue("");
							R45Cell1.setCellStyle(textStyle);
						}
						Cell R45Cell2 = row.createCell(2);
						if (record1.getR45Leasing() != null) {
							R45Cell2.setCellValue(record1.getR45Leasing().doubleValue());
							R45Cell2.setCellStyle(numberStyle);
						} else {
							R45Cell2.setCellValue("");
							R45Cell2.setCellStyle(textStyle);
						}
						Cell R45Cell3 = row.createCell(3);
						if (record.getR45Overdrafts() != null) {
							R45Cell3.setCellValue(record.getR45Overdrafts().doubleValue());
							R45Cell3.setCellStyle(numberStyle);
						} else {
							R45Cell3.setCellValue("");
							R45Cell3.setCellStyle(textStyle);
						}
						Cell R45Cell4 = row.createCell(4);
						if (record.getR45OtherInstallmentLoans() != null) {
							R45Cell4.setCellValue(record.getR45OtherInstallmentLoans().doubleValue());
							R45Cell4.setCellStyle(numberStyle);
						} else {
							R45Cell4.setCellValue("");
							R45Cell4.setCellStyle(textStyle);
						}

						// --- R46 (Row Index 45) ---
						row = sheet.getRow(45);
						Cell R46Cell1 = row.createCell(1);
						if (record1.getR46FactoringDebtors() != null) {
							R46Cell1.setCellValue(record1.getR46FactoringDebtors().doubleValue());
							R46Cell1.setCellStyle(numberStyle);
						} else {
							R46Cell1.setCellValue("");
							R46Cell1.setCellStyle(textStyle);
						}
						Cell R46Cell2 = row.createCell(2);
						if (record1.getR46Leasing() != null) {
							R46Cell2.setCellValue(record1.getR46Leasing().doubleValue());
							R46Cell2.setCellStyle(numberStyle);
						} else {
							R46Cell2.setCellValue("");
							R46Cell2.setCellStyle(textStyle);
						}
						Cell R46Cell3 = row.createCell(3);
						if (record.getR46Overdrafts() != null) {
							R46Cell3.setCellValue(record.getR46Overdrafts().doubleValue());
							R46Cell3.setCellStyle(numberStyle);
						} else {
							R46Cell3.setCellValue("");
							R46Cell3.setCellStyle(textStyle);
						}
						Cell R46Cell4 = row.createCell(4);
						if (record.getR46OtherInstallmentLoans() != null) {
							R46Cell4.setCellValue(record.getR46OtherInstallmentLoans().doubleValue());
							R46Cell4.setCellStyle(numberStyle);
						} else {
							R46Cell4.setCellValue("");
							R46Cell4.setCellStyle(textStyle);
						}

						// --- R47 (Row Index 46) ---
						row = sheet.getRow(46);
						Cell R47Cell1 = row.createCell(1);
						if (record1.getR47FactoringDebtors() != null) {
							R47Cell1.setCellValue(record1.getR47FactoringDebtors().doubleValue());
							R47Cell1.setCellStyle(numberStyle);
						} else {
							R47Cell1.setCellValue("");
							R47Cell1.setCellStyle(textStyle);
						}
						Cell R47Cell2 = row.createCell(2);
						if (record1.getR47Leasing() != null) {
							R47Cell2.setCellValue(record1.getR47Leasing().doubleValue());
							R47Cell2.setCellStyle(numberStyle);
						} else {
							R47Cell2.setCellValue("");
							R47Cell2.setCellStyle(textStyle);
						}
						Cell R47Cell3 = row.createCell(3);
						if (record.getR47Overdrafts() != null) {
							R47Cell3.setCellValue(record.getR47Overdrafts().doubleValue());
							R47Cell3.setCellStyle(numberStyle);
						} else {
							R47Cell3.setCellValue("");
							R47Cell3.setCellStyle(textStyle);
						}
						Cell R47Cell4 = row.createCell(4);
						if (record.getR47OtherInstallmentLoans() != null) {
							R47Cell4.setCellValue(record.getR47OtherInstallmentLoans().doubleValue());
							R47Cell4.setCellStyle(numberStyle);
						} else {
							R47Cell4.setCellValue("");
							R47Cell4.setCellStyle(textStyle);
						}

						// --- R48 (Row Index 47) ---
						row = sheet.getRow(47);
						Cell R48Cell1 = row.createCell(1);
						if (record1.getR48FactoringDebtors() != null) {
							R48Cell1.setCellValue(record1.getR48FactoringDebtors().doubleValue());
							R48Cell1.setCellStyle(numberStyle);
						} else {
							R48Cell1.setCellValue("");
							R48Cell1.setCellStyle(textStyle);
						}
						Cell R48Cell2 = row.createCell(2);
						if (record1.getR48Leasing() != null) {
							R48Cell2.setCellValue(record1.getR48Leasing().doubleValue());
							R48Cell2.setCellStyle(numberStyle);
						} else {
							R48Cell2.setCellValue("");
							R48Cell2.setCellStyle(textStyle);
						}
						Cell R48Cell3 = row.createCell(3);
						if (record.getR48Overdrafts() != null) {
							R48Cell3.setCellValue(record.getR48Overdrafts().doubleValue());
							R48Cell3.setCellStyle(numberStyle);
						} else {
							R48Cell3.setCellValue("");
							R48Cell3.setCellStyle(textStyle);
						}
						Cell R48Cell4 = row.createCell(4);
						if (record.getR48OtherInstallmentLoans() != null) {
							R48Cell4.setCellValue(record.getR48OtherInstallmentLoans().doubleValue());
							R48Cell4.setCellStyle(numberStyle);
						} else {
							R48Cell4.setCellValue("");
							R48Cell4.setCellStyle(textStyle);
						}

						// --- R50 (Row Index 49) ---
						row = sheet.getRow(49);
						Cell R50Cell1 = row.createCell(1);
						if (record1.getR50FactoringDebtors() != null) {
							R50Cell1.setCellValue(record1.getR50FactoringDebtors().doubleValue());
							R50Cell1.setCellStyle(numberStyle);
						} else {
							R50Cell1.setCellValue("");
							R50Cell1.setCellStyle(textStyle);
						}
						Cell R50Cell2 = row.createCell(2);
						if (record1.getR50Leasing() != null) {
							R50Cell2.setCellValue(record1.getR50Leasing().doubleValue());
							R50Cell2.setCellStyle(numberStyle);
						} else {
							R50Cell2.setCellValue("");
							R50Cell2.setCellStyle(textStyle);
						}
						Cell R50Cell3 = row.createCell(3);
						if (record.getR50Overdrafts() != null) {
							R50Cell3.setCellValue(record.getR50Overdrafts().doubleValue());
							R50Cell3.setCellStyle(numberStyle);
						} else {
							R50Cell3.setCellValue("");
							R50Cell3.setCellStyle(textStyle);
						}
						Cell R50Cell4 = row.createCell(4);
						if (record.getR50OtherInstallmentLoans() != null) {
							R50Cell4.setCellValue(record.getR50OtherInstallmentLoans().doubleValue());
							R50Cell4.setCellStyle(numberStyle);
						} else {
							R50Cell4.setCellValue("");
							R50Cell4.setCellStyle(textStyle);
						}

						// --- R51 (Row Index 50) ---
						row = sheet.getRow(50);
						Cell R51Cell1 = row.createCell(1);
						if (record1.getR51FactoringDebtors() != null) {
							R51Cell1.setCellValue(record1.getR51FactoringDebtors().doubleValue());
							R51Cell1.setCellStyle(numberStyle);
						} else {
							R51Cell1.setCellValue("");
							R51Cell1.setCellStyle(textStyle);
						}
						Cell R51Cell2 = row.createCell(2);
						if (record1.getR51Leasing() != null) {
							R51Cell2.setCellValue(record1.getR51Leasing().doubleValue());
							R51Cell2.setCellStyle(numberStyle);
						} else {
							R51Cell2.setCellValue("");
							R51Cell2.setCellStyle(textStyle);
						}
						Cell R51Cell3 = row.createCell(3);
						if (record.getR51Overdrafts() != null) {
							R51Cell3.setCellValue(record.getR51Overdrafts().doubleValue());
							R51Cell3.setCellStyle(numberStyle);
						} else {
							R51Cell3.setCellValue("");
							R51Cell3.setCellStyle(textStyle);
						}
						Cell R51Cell4 = row.createCell(4);
						if (record.getR51OtherInstallmentLoans() != null) {
							R51Cell4.setCellValue(record.getR51OtherInstallmentLoans().doubleValue());
							R51Cell4.setCellStyle(numberStyle);
						} else {
							R51Cell4.setCellValue("");
							R51Cell4.setCellStyle(textStyle);
						}

						// --- R52 (Row Index 51) ---
						row = sheet.getRow(51);
						Cell R52Cell1 = row.createCell(1);
						if (record1.getR52FactoringDebtors() != null) {
							R52Cell1.setCellValue(record1.getR52FactoringDebtors().doubleValue());
							R52Cell1.setCellStyle(numberStyle);
						} else {
							R52Cell1.setCellValue("");
							R52Cell1.setCellStyle(textStyle);
						}
						Cell R52Cell2 = row.createCell(2);
						if (record1.getR52Leasing() != null) {
							R52Cell2.setCellValue(record1.getR52Leasing().doubleValue());
							R52Cell2.setCellStyle(numberStyle);
						} else {
							R52Cell2.setCellValue("");
							R52Cell2.setCellStyle(textStyle);
						}
						Cell R52Cell3 = row.createCell(3);
						if (record.getR52Overdrafts() != null) {
							R52Cell3.setCellValue(record.getR52Overdrafts().doubleValue());
							R52Cell3.setCellStyle(numberStyle);
						} else {
							R52Cell3.setCellValue("");
							R52Cell3.setCellStyle(textStyle);
						}
						Cell R52Cell4 = row.createCell(4);
						if (record.getR52OtherInstallmentLoans() != null) {
							R52Cell4.setCellValue(record.getR52OtherInstallmentLoans().doubleValue());
							R52Cell4.setCellStyle(numberStyle);
						} else {
							R52Cell4.setCellValue("");
							R52Cell4.setCellStyle(textStyle);
						}
						// --- R54 (Row Index 53) ---
						row = sheet.getRow(53);
						Cell R54Cell1 = row.createCell(1);
						if (record1.getR54FactoringDebtors() != null) {
							R54Cell1.setCellValue(record1.getR54FactoringDebtors().doubleValue());
							R54Cell1.setCellStyle(numberStyle);
						} else {
							R54Cell1.setCellValue("");
							R54Cell1.setCellStyle(textStyle);
						}
						Cell R54Cell2 = row.createCell(2);
						if (record1.getR54Leasing() != null) {
							R54Cell2.setCellValue(record1.getR54Leasing().doubleValue());
							R54Cell2.setCellStyle(numberStyle);
						} else {
							R54Cell2.setCellValue("");
							R54Cell2.setCellStyle(textStyle);
						}
						Cell R54Cell3 = row.createCell(3);
						if (record.getR54Overdrafts() != null) {
							R54Cell3.setCellValue(record.getR54Overdrafts().doubleValue());
							R54Cell3.setCellStyle(numberStyle);
						} else {
							R54Cell3.setCellValue("");
							R54Cell3.setCellStyle(textStyle);
						}
						Cell R54Cell4 = row.createCell(4);
						if (record.getR54OtherInstallmentLoans() != null) {
							R54Cell4.setCellValue(record.getR54OtherInstallmentLoans().doubleValue());
							R54Cell4.setCellStyle(numberStyle);
						} else {
							R54Cell4.setCellValue("");
							R54Cell4.setCellStyle(textStyle);
						}

						// --- R55 (Row Index 54) ---
						row = sheet.getRow(54);
						Cell R55Cell1 = row.createCell(1);
						if (record1.getR55FactoringDebtors() != null) {
							R55Cell1.setCellValue(record1.getR55FactoringDebtors().doubleValue());
							R55Cell1.setCellStyle(numberStyle);
						} else {
							R55Cell1.setCellValue("");
							R55Cell1.setCellStyle(textStyle);
						}
						Cell R55Cell2 = row.createCell(2);
						if (record1.getR55Leasing() != null) {
							R55Cell2.setCellValue(record1.getR55Leasing().doubleValue());
							R55Cell2.setCellStyle(numberStyle);
						} else {
							R55Cell2.setCellValue("");
							R55Cell2.setCellStyle(textStyle);
						}
						Cell R55Cell3 = row.createCell(3);
						if (record.getR55Overdrafts() != null) {
							R55Cell3.setCellValue(record.getR55Overdrafts().doubleValue());
							R55Cell3.setCellStyle(numberStyle);
						} else {
							R55Cell3.setCellValue("");
							R55Cell3.setCellStyle(textStyle);
						}
						Cell R55Cell4 = row.createCell(4);
						if (record.getR55OtherInstallmentLoans() != null) {
							R55Cell4.setCellValue(record.getR55OtherInstallmentLoans().doubleValue());
							R55Cell4.setCellStyle(numberStyle);
						} else {
							R55Cell4.setCellValue("");
							R55Cell4.setCellStyle(textStyle);
						}

						// --- R56 (Row Index 55) ---
						row = sheet.getRow(55);
						Cell R56Cell1 = row.createCell(1);
						if (record1.getR56FactoringDebtors() != null) {
							R56Cell1.setCellValue(record1.getR56FactoringDebtors().doubleValue());
							R56Cell1.setCellStyle(numberStyle);
						} else {
							R56Cell1.setCellValue("");
							R56Cell1.setCellStyle(textStyle);
						}
						Cell R56Cell2 = row.createCell(2);
						if (record1.getR56Leasing() != null) {
							R56Cell2.setCellValue(record1.getR56Leasing().doubleValue());
							R56Cell2.setCellStyle(numberStyle);
						} else {
							R56Cell2.setCellValue("");
							R56Cell2.setCellStyle(textStyle);
						}
						Cell R56Cell3 = row.createCell(3);
						if (record.getR56Overdrafts() != null) {
							R56Cell3.setCellValue(record.getR56Overdrafts().doubleValue());
							R56Cell3.setCellStyle(numberStyle);
						} else {
							R56Cell3.setCellValue("");
							R56Cell3.setCellStyle(textStyle);
						}
						Cell R56Cell4 = row.createCell(4);
						if (record.getR56OtherInstallmentLoans() != null) {
							R56Cell4.setCellValue(record.getR56OtherInstallmentLoans().doubleValue());
							R56Cell4.setCellStyle(numberStyle);
						} else {
							R56Cell4.setCellValue("");
							R56Cell4.setCellStyle(textStyle);
						}
						// --- R58 (Row Index 57) ---
						row = sheet.getRow(57);
						Cell R58Cell1 = row.createCell(1);
						if (record1.getR58FactoringDebtors() != null) {
							R58Cell1.setCellValue(record1.getR58FactoringDebtors().doubleValue());
							R58Cell1.setCellStyle(numberStyle);
						} else {
							R58Cell1.setCellValue("");
							R58Cell1.setCellStyle(textStyle);
						}
						Cell R58Cell2 = row.createCell(2);
						if (record1.getR58Leasing() != null) {
							R58Cell2.setCellValue(record1.getR58Leasing().doubleValue());
							R58Cell2.setCellStyle(numberStyle);
						} else {
							R58Cell2.setCellValue("");
							R58Cell2.setCellStyle(textStyle);
						}
						Cell R58Cell3 = row.createCell(3);
						if (record.getR58Overdrafts() != null) {
							R58Cell3.setCellValue(record.getR58Overdrafts().doubleValue());
							R58Cell3.setCellStyle(numberStyle);
						} else {
							R58Cell3.setCellValue("");
							R58Cell3.setCellStyle(textStyle);
						}
						Cell R58Cell4 = row.createCell(4);
						if (record.getR58OtherInstallmentLoans() != null) {
							R58Cell4.setCellValue(record.getR58OtherInstallmentLoans().doubleValue());
							R58Cell4.setCellStyle(numberStyle);
						} else {
							R58Cell4.setCellValue("");
							R58Cell4.setCellStyle(textStyle);
						}

						// --- R59 (Row Index 58) ---
						row = sheet.getRow(58);
						Cell R59Cell1 = row.createCell(1);
						if (record1.getR59FactoringDebtors() != null) {
							R59Cell1.setCellValue(record1.getR59FactoringDebtors().doubleValue());
							R59Cell1.setCellStyle(numberStyle);
						} else {
							R59Cell1.setCellValue("");
							R59Cell1.setCellStyle(textStyle);
						}
						Cell R59Cell2 = row.createCell(2);
						if (record1.getR59Leasing() != null) {
							R59Cell2.setCellValue(record1.getR59Leasing().doubleValue());
							R59Cell2.setCellStyle(numberStyle);
						} else {
							R59Cell2.setCellValue("");
							R59Cell2.setCellStyle(textStyle);
						}
						Cell R59Cell3 = row.createCell(3);
						if (record.getR59Overdrafts() != null) {
							R59Cell3.setCellValue(record.getR59Overdrafts().doubleValue());
							R59Cell3.setCellStyle(numberStyle);
						} else {
							R59Cell3.setCellValue("");
							R59Cell3.setCellStyle(textStyle);
						}
						Cell R59Cell4 = row.createCell(4);
						if (record.getR59OtherInstallmentLoans() != null) {
							R59Cell4.setCellValue(record.getR59OtherInstallmentLoans().doubleValue());
							R59Cell4.setCellStyle(numberStyle);
						} else {
							R59Cell4.setCellValue("");
							R59Cell4.setCellStyle(textStyle);
						}

						// --- R60 (Row Index 59) ---
						row = sheet.getRow(59);
						Cell R60Cell1 = row.createCell(1);
						if (record1.getR60FactoringDebtors() != null) {
							R60Cell1.setCellValue(record1.getR60FactoringDebtors().doubleValue());
							R60Cell1.setCellStyle(numberStyle);
						} else {
							R60Cell1.setCellValue("");
							R60Cell1.setCellStyle(textStyle);
						}
						Cell R60Cell2 = row.createCell(2);
						if (record1.getR60Leasing() != null) {
							R60Cell2.setCellValue(record1.getR60Leasing().doubleValue());
							R60Cell2.setCellStyle(numberStyle);
						} else {
							R60Cell2.setCellValue("");
							R60Cell2.setCellStyle(textStyle);
						}
						Cell R60Cell3 = row.createCell(3);
						if (record.getR60Overdrafts() != null) {
							R60Cell3.setCellValue(record.getR60Overdrafts().doubleValue());
							R60Cell3.setCellStyle(numberStyle);
						} else {
							R60Cell3.setCellValue("");
							R60Cell3.setCellStyle(textStyle);
						}
						Cell R60Cell4 = row.createCell(4);
						if (record.getR60OtherInstallmentLoans() != null) {
							R60Cell4.setCellValue(record.getR60OtherInstallmentLoans().doubleValue());
							R60Cell4.setCellStyle(numberStyle);
						} else {
							R60Cell4.setCellValue("");
							R60Cell4.setCellStyle(textStyle);
						}

						// --- R61 (Row Index 60) ---
						row = sheet.getRow(60);
						Cell R61Cell1 = row.createCell(1);
						if (record1.getR61FactoringDebtors() != null) {
							R61Cell1.setCellValue(record1.getR61FactoringDebtors().doubleValue());
							R61Cell1.setCellStyle(numberStyle);
						} else {
							R61Cell1.setCellValue("");
							R61Cell1.setCellStyle(textStyle);
						}
						Cell R61Cell2 = row.createCell(2);
						if (record1.getR61Leasing() != null) {
							R61Cell2.setCellValue(record1.getR61Leasing().doubleValue());
							R61Cell2.setCellStyle(numberStyle);
						} else {
							R61Cell2.setCellValue("");
							R61Cell2.setCellStyle(textStyle);
						}
						Cell R61Cell3 = row.createCell(3);
						if (record.getR61Overdrafts() != null) {
							R61Cell3.setCellValue(record.getR61Overdrafts().doubleValue());
							R61Cell3.setCellStyle(numberStyle);
						} else {
							R61Cell3.setCellValue("");
							R61Cell3.setCellStyle(textStyle);
						}
						Cell R61Cell4 = row.createCell(4);
						if (record.getR61OtherInstallmentLoans() != null) {
							R61Cell4.setCellValue(record.getR61OtherInstallmentLoans().doubleValue());
							R61Cell4.setCellStyle(numberStyle);
						} else {
							R61Cell4.setCellValue("");
							R61Cell4.setCellStyle(textStyle);
						}

						// --- R62 (Row Index 61) ---
						row = sheet.getRow(61);
						Cell R62Cell1 = row.createCell(1);
						if (record1.getR62FactoringDebtors() != null) {
							R62Cell1.setCellValue(record1.getR62FactoringDebtors().doubleValue());
							R62Cell1.setCellStyle(numberStyle);
						} else {
							R62Cell1.setCellValue("");
							R62Cell1.setCellStyle(textStyle);
						}
						Cell R62Cell2 = row.createCell(2);
						if (record1.getR62Leasing() != null) {
							R62Cell2.setCellValue(record1.getR62Leasing().doubleValue());
							R62Cell2.setCellStyle(numberStyle);
						} else {
							R62Cell2.setCellValue("");
							R62Cell2.setCellStyle(textStyle);
						}
						Cell R62Cell3 = row.createCell(3);
						if (record.getR62Overdrafts() != null) {
							R62Cell3.setCellValue(record.getR62Overdrafts().doubleValue());
							R62Cell3.setCellStyle(numberStyle);
						} else {
							R62Cell3.setCellValue("");
							R62Cell3.setCellStyle(textStyle);
						}
						Cell R62Cell4 = row.createCell(4);
						if (record.getR62OtherInstallmentLoans() != null) {
							R62Cell4.setCellValue(record.getR62OtherInstallmentLoans().doubleValue());
							R62Cell4.setCellStyle(numberStyle);
						} else {
							R62Cell4.setCellValue("");
							R62Cell4.setCellStyle(textStyle);
						}

						// --- R63 (Row Index 62) ---
						row = sheet.getRow(62);
						Cell R63Cell1 = row.createCell(1);
						if (record1.getR63FactoringDebtors() != null) {
							R63Cell1.setCellValue(record1.getR63FactoringDebtors().doubleValue());
							R63Cell1.setCellStyle(numberStyle);
						} else {
							R63Cell1.setCellValue("");
							R63Cell1.setCellStyle(textStyle);
						}
						Cell R63Cell2 = row.createCell(2);
						if (record1.getR63Leasing() != null) {
							R63Cell2.setCellValue(record1.getR63Leasing().doubleValue());
							R63Cell2.setCellStyle(numberStyle);
						} else {
							R63Cell2.setCellValue("");
							R63Cell2.setCellStyle(textStyle);
						}
						Cell R63Cell3 = row.createCell(3);
						if (record.getR63Overdrafts() != null) {
							R63Cell3.setCellValue(record.getR63Overdrafts().doubleValue());
							R63Cell3.setCellStyle(numberStyle);
						} else {
							R63Cell3.setCellValue("");
							R63Cell3.setCellStyle(textStyle);
						}
						Cell R63Cell4 = row.createCell(4);
						if (record.getR63OtherInstallmentLoans() != null) {
							R63Cell4.setCellValue(record.getR63OtherInstallmentLoans().doubleValue());
							R63Cell4.setCellStyle(numberStyle);
						} else {
							R63Cell4.setCellValue("");
							R63Cell4.setCellStyle(textStyle);
						}
					}
					workbook.setForceFormulaRecalculation(true);
				} else {

				}

				// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA4 EMAIL SUMMARY", null,
							"BRRS_M_LA4_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival Email Excel
	public byte[] BRRS_M_LA4EmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,  BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {

		}

		List<M_LA4_Archival_Summary_Entity1> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);
		List<M_LA4_Archival_Summary_Entity2> dataList1 = getdatabydateListarchival2(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_LA4 report. Returning empty result.");
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
			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA4_Archival_Summary_Entity1 record = dataList.get(i);
					M_LA4_Archival_Summary_Entity2 record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell R12Cell = row.createCell(1);

					if (record1.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record1.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(11);
					// R12 Col B
					Cell R12Cell1 = row.createCell(1);
					if (record1.getR12FactoringDebtors() != null) {
						R12Cell1.setCellValue(record1.getR12FactoringDebtors().doubleValue());
						R12Cell1.setCellStyle(numberStyle);
					} else {
						R12Cell1.setCellValue("");
						R12Cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12Cell2 = row.createCell(2);
					if (record1.getR12Leasing() != null) {
						R12Cell2.setCellValue(record1.getR12Leasing().doubleValue());
						R12Cell2.setCellStyle(numberStyle);
					} else {
						R12Cell2.setCellValue("");
						R12Cell2.setCellStyle(textStyle);
					}
					// R12 Col D
					Cell R12Cell3 = row.createCell(3);
					if (record.getR12Overdrafts() != null) {
						R12Cell3.setCellValue(record.getR12Overdrafts().doubleValue());
						R12Cell3.setCellStyle(numberStyle);
					} else {
						R12Cell3.setCellValue("");
						R12Cell3.setCellStyle(textStyle);
					}

					// R12 Col E
					Cell R12Cell4 = row.createCell(4);
					if (record.getR12OtherInstallmentLoans() != null) {
						R12Cell4.setCellValue(record.getR12OtherInstallmentLoans().doubleValue());
						R12Cell4.setCellStyle(numberStyle);
					} else {
						R12Cell4.setCellValue("");
						R12Cell4.setCellStyle(textStyle);
					}
					// R13 Col B
					row = sheet.getRow(12);
					// R13 Col B
					Cell R13Cell1 = row.createCell(1);
					if (record1.getR13FactoringDebtors() != null) {
						R13Cell1.setCellValue(record1.getR13FactoringDebtors().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13Cell2 = row.createCell(2);
					if (record1.getR13Leasing() != null) {
						R13Cell2.setCellValue(record1.getR13Leasing().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}
					// R13 Col D
					Cell R13Cell3 = row.createCell(3);
					if (record.getR13Overdrafts() != null) {
						R13Cell3.setCellValue(record.getR13Overdrafts().doubleValue());
						R13Cell3.setCellStyle(numberStyle);
					} else {
						R13Cell3.setCellValue("");
						R13Cell3.setCellStyle(textStyle);
					}

					// R13 Col E
					Cell R13Cell4 = row.createCell(4);
					if (record.getR13OtherInstallmentLoans() != null) {
						R13Cell4.setCellValue(record.getR13OtherInstallmentLoans().doubleValue());
						R13Cell4.setCellStyle(numberStyle);
					} else {
						R13Cell4.setCellValue("");
						R13Cell4.setCellStyle(textStyle);
					}
					// R14 Col B
					row = sheet.getRow(13); // Row index 13 is Excel Row 14
					// R14 Col B
					Cell R14Cell1 = row.createCell(1);
					if (record1.getR14FactoringDebtors() != null) {
						R14Cell1.setCellValue(record1.getR14FactoringDebtors().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14Cell2 = row.createCell(2);
					if (record1.getR14Leasing() != null) {
						R14Cell2.setCellValue(record1.getR14Leasing().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

					// R14 Col D
					Cell R14Cell3 = row.createCell(3);
					if (record.getR14Overdrafts() != null) {
						R14Cell3.setCellValue(record.getR14Overdrafts().doubleValue());
						R14Cell3.setCellStyle(numberStyle);
					} else {
						R14Cell3.setCellValue("");
						R14Cell3.setCellStyle(textStyle);
					}

					// R14 Col E
					Cell R14Cell4 = row.createCell(4);
					if (record.getR14OtherInstallmentLoans() != null) {
						R14Cell4.setCellValue(record.getR14OtherInstallmentLoans().doubleValue());
						R14Cell4.setCellStyle(numberStyle);
					} else {
						R14Cell4.setCellValue("");
						R14Cell4.setCellStyle(textStyle);
					}

					// --- R16 (Row Index 15) ---
					row = sheet.getRow(15);
					Cell R16Cell1 = row.createCell(1);
					if (record1.getR16FactoringDebtors() != null) {
						R16Cell1.setCellValue(record1.getR16FactoringDebtors().doubleValue());
						R16Cell1.setCellStyle(numberStyle);
					} else {
						R16Cell1.setCellValue("");
						R16Cell1.setCellStyle(textStyle);
					}
					Cell R16Cell2 = row.createCell(2);
					if (record1.getR16Leasing() != null) {
						R16Cell2.setCellValue(record1.getR16Leasing().doubleValue());
						R16Cell2.setCellStyle(numberStyle);
					} else {
						R16Cell2.setCellValue("");
						R16Cell2.setCellStyle(textStyle);
					}
					Cell R16Cell3 = row.createCell(3);
					if (record.getR16Overdrafts() != null) {
						R16Cell3.setCellValue(record.getR16Overdrafts().doubleValue());
						R16Cell3.setCellStyle(numberStyle);
					} else {
						R16Cell3.setCellValue("");
						R16Cell3.setCellStyle(textStyle);
					}
					Cell R16Cell4 = row.createCell(4);
					if (record.getR16OtherInstallmentLoans() != null) {
						R16Cell4.setCellValue(record.getR16OtherInstallmentLoans().doubleValue());
						R16Cell4.setCellStyle(numberStyle);
					} else {
						R16Cell4.setCellValue("");
						R16Cell4.setCellStyle(textStyle);
					}

					// --- R17 (Row Index 16) ---
					row = sheet.getRow(16);
					Cell R17Cell1 = row.createCell(1);
					if (record1.getR17FactoringDebtors() != null) {
						R17Cell1.setCellValue(record1.getR17FactoringDebtors().doubleValue());
						R17Cell1.setCellStyle(numberStyle);
					} else {
						R17Cell1.setCellValue("");
						R17Cell1.setCellStyle(textStyle);
					}
					Cell R17Cell2 = row.createCell(2);
					if (record1.getR17Leasing() != null) {
						R17Cell2.setCellValue(record1.getR17Leasing().doubleValue());
						R17Cell2.setCellStyle(numberStyle);
					} else {
						R17Cell2.setCellValue("");
						R17Cell2.setCellStyle(textStyle);
					}
					Cell R17Cell3 = row.createCell(3);
					if (record.getR17Overdrafts() != null) {
						R17Cell3.setCellValue(record.getR17Overdrafts().doubleValue());
						R17Cell3.setCellStyle(numberStyle);
					} else {
						R17Cell3.setCellValue("");
						R17Cell3.setCellStyle(textStyle);
					}
					Cell R17Cell4 = row.createCell(4);
					if (record.getR17OtherInstallmentLoans() != null) {
						R17Cell4.setCellValue(record.getR17OtherInstallmentLoans().doubleValue());
						R17Cell4.setCellStyle(numberStyle);
					} else {
						R17Cell4.setCellValue("");
						R17Cell4.setCellStyle(textStyle);
					}

					// --- R18 (Row Index 17) ---
					row = sheet.getRow(17);
					Cell R18Cell1 = row.createCell(1);
					if (record1.getR18FactoringDebtors() != null) {
						R18Cell1.setCellValue(record1.getR18FactoringDebtors().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}
					Cell R18Cell2 = row.createCell(2);
					if (record1.getR18Leasing() != null) {
						R18Cell2.setCellValue(record1.getR18Leasing().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}
					Cell R18Cell3 = row.createCell(3);
					if (record.getR18Overdrafts() != null) {
						R18Cell3.setCellValue(record.getR18Overdrafts().doubleValue());
						R18Cell3.setCellStyle(numberStyle);
					} else {
						R18Cell3.setCellValue("");
						R18Cell3.setCellStyle(textStyle);
					}
					Cell R18Cell4 = row.createCell(4);
					if (record.getR18OtherInstallmentLoans() != null) {
						R18Cell4.setCellValue(record.getR18OtherInstallmentLoans().doubleValue());
						R18Cell4.setCellStyle(numberStyle);
					} else {
						R18Cell4.setCellValue("");
						R18Cell4.setCellStyle(textStyle);
					}

					// --- R19 (Row Index 18) ---
					row = sheet.getRow(18);
					Cell R19Cell1 = row.createCell(1);
					if (record1.getR19FactoringDebtors() != null) {
						R19Cell1.setCellValue(record1.getR19FactoringDebtors().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}
					Cell R19Cell2 = row.createCell(2);
					if (record1.getR19Leasing() != null) {
						R19Cell2.setCellValue(record1.getR19Leasing().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}
					Cell R19Cell3 = row.createCell(3);
					if (record.getR19Overdrafts() != null) {
						R19Cell3.setCellValue(record.getR19Overdrafts().doubleValue());
						R19Cell3.setCellStyle(numberStyle);
					} else {
						R19Cell3.setCellValue("");
						R19Cell3.setCellStyle(textStyle);
					}
					Cell R19Cell4 = row.createCell(4);
					if (record.getR19OtherInstallmentLoans() != null) {
						R19Cell4.setCellValue(record.getR19OtherInstallmentLoans().doubleValue());
						R19Cell4.setCellStyle(numberStyle);
					} else {
						R19Cell4.setCellValue("");
						R19Cell4.setCellStyle(textStyle);
					}
					// --- R20 (Row Index 19) ---
					row = sheet.getRow(19);
					Cell R20Cell1 = row.createCell(1);
					if (record1.getR20FactoringDebtors() != null) {
						R20Cell1.setCellValue(record1.getR20FactoringDebtors().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}
					Cell R20Cell2 = row.createCell(2);
					if (record1.getR20Leasing() != null) {
						R20Cell2.setCellValue(record1.getR20Leasing().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}
					Cell R20Cell3 = row.createCell(3);
					if (record.getR20Overdrafts() != null) {
						R20Cell3.setCellValue(record.getR20Overdrafts().doubleValue());
						R20Cell3.setCellStyle(numberStyle);
					} else {
						R20Cell3.setCellValue("");
						R20Cell3.setCellStyle(textStyle);
					}
					Cell R20Cell4 = row.createCell(4);
					if (record.getR20OtherInstallmentLoans() != null) {
						R20Cell4.setCellValue(record.getR20OtherInstallmentLoans().doubleValue());
						R20Cell4.setCellStyle(numberStyle);
					} else {
						R20Cell4.setCellValue("");
						R20Cell4.setCellStyle(textStyle);
					}

					// --- R21 (Row Index 20) ---
					row = sheet.getRow(20);
					Cell R21Cell1 = row.createCell(1);
					if (record1.getR21FactoringDebtors() != null) {
						R21Cell1.setCellValue(record1.getR21FactoringDebtors().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}
					Cell R21Cell2 = row.createCell(2);
					if (record1.getR21Leasing() != null) {
						R21Cell2.setCellValue(record1.getR21Leasing().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}
					Cell R21Cell3 = row.createCell(3);
					if (record.getR21Overdrafts() != null) {
						R21Cell3.setCellValue(record.getR21Overdrafts().doubleValue());
						R21Cell3.setCellStyle(numberStyle);
					} else {
						R21Cell3.setCellValue("");
						R21Cell3.setCellStyle(textStyle);
					}
					Cell R21Cell4 = row.createCell(4);
					if (record.getR21OtherInstallmentLoans() != null) {
						R21Cell4.setCellValue(record.getR21OtherInstallmentLoans().doubleValue());
						R21Cell4.setCellStyle(numberStyle);
					} else {
						R21Cell4.setCellValue("");
						R21Cell4.setCellStyle(textStyle);
					}

					// --- R22 (Row Index 21) ---
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(1);
					if (record1.getR22FactoringDebtors() != null) {
						R22Cell1.setCellValue(record1.getR22FactoringDebtors().doubleValue());
						R22Cell1.setCellStyle(numberStyle);
					} else {
						R22Cell1.setCellValue("");
						R22Cell1.setCellStyle(textStyle);
					}
					Cell R22Cell2 = row.createCell(2);
					if (record1.getR22Leasing() != null) {
						R22Cell2.setCellValue(record1.getR22Leasing().doubleValue());
						R22Cell2.setCellStyle(numberStyle);
					} else {
						R22Cell2.setCellValue("");
						R22Cell2.setCellStyle(textStyle);
					}
					Cell R22Cell3 = row.createCell(3);
					if (record.getR22Overdrafts() != null) {
						R22Cell3.setCellValue(record.getR22Overdrafts().doubleValue());
						R22Cell3.setCellStyle(numberStyle);
					} else {
						R22Cell3.setCellValue("");
						R22Cell3.setCellStyle(textStyle);
					}
					Cell R22Cell4 = row.createCell(4);
					if (record.getR22OtherInstallmentLoans() != null) {
						R22Cell4.setCellValue(record.getR22OtherInstallmentLoans().doubleValue());
						R22Cell4.setCellStyle(numberStyle);
					} else {
						R22Cell4.setCellValue("");
						R22Cell4.setCellStyle(textStyle);
					}

					// --- R23 (Row Index 22) ---
					row = sheet.getRow(22);
					Cell R23Cell1 = row.createCell(1);
					if (record1.getR23FactoringDebtors() != null) {
						R23Cell1.setCellValue(record1.getR23FactoringDebtors().doubleValue());
						R23Cell1.setCellStyle(numberStyle);
					} else {
						R23Cell1.setCellValue("");
						R23Cell1.setCellStyle(textStyle);
					}
					Cell R23Cell2 = row.createCell(2);
					if (record1.getR23Leasing() != null) {
						R23Cell2.setCellValue(record1.getR23Leasing().doubleValue());
						R23Cell2.setCellStyle(numberStyle);
					} else {
						R23Cell2.setCellValue("");
						R23Cell2.setCellStyle(textStyle);
					}
					Cell R23Cell3 = row.createCell(3);
					if (record.getR23Overdrafts() != null) {
						R23Cell3.setCellValue(record.getR23Overdrafts().doubleValue());
						R23Cell3.setCellStyle(numberStyle);
					} else {
						R23Cell3.setCellValue("");
						R23Cell3.setCellStyle(textStyle);
					}
					Cell R23Cell4 = row.createCell(4);
					if (record.getR23OtherInstallmentLoans() != null) {
						R23Cell4.setCellValue(record.getR23OtherInstallmentLoans().doubleValue());
						R23Cell4.setCellStyle(numberStyle);
					} else {
						R23Cell4.setCellValue("");
						R23Cell4.setCellStyle(textStyle);
					}

					// --- R24 (Row Index 23) ---
					row = sheet.getRow(23);
					Cell R24Cell1 = row.createCell(1);
					if (record1.getR24FactoringDebtors() != null) {
						R24Cell1.setCellValue(record1.getR24FactoringDebtors().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}
					Cell R24Cell2 = row.createCell(2);
					if (record1.getR24Leasing() != null) {
						R24Cell2.setCellValue(record1.getR24Leasing().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}
					Cell R24Cell3 = row.createCell(3);
					if (record.getR24Overdrafts() != null) {
						R24Cell3.setCellValue(record.getR24Overdrafts().doubleValue());
						R24Cell3.setCellStyle(numberStyle);
					} else {
						R24Cell3.setCellValue("");
						R24Cell3.setCellStyle(textStyle);
					}
					Cell R24Cell4 = row.createCell(4);
					if (record.getR24OtherInstallmentLoans() != null) {
						R24Cell4.setCellValue(record.getR24OtherInstallmentLoans().doubleValue());
						R24Cell4.setCellStyle(numberStyle);
					} else {
						R24Cell4.setCellValue("");
						R24Cell4.setCellStyle(textStyle);
					}

					// --- R25 (Row Index 24) ---
					row = sheet.getRow(24);
					Cell R25Cell1 = row.createCell(1);
					if (record1.getR25FactoringDebtors() != null) {
						R25Cell1.setCellValue(record1.getR25FactoringDebtors().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}
					Cell R25Cell2 = row.createCell(2);
					if (record1.getR25Leasing() != null) {
						R25Cell2.setCellValue(record1.getR25Leasing().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}
					Cell R25Cell3 = row.createCell(3);
					if (record.getR25Overdrafts() != null) {
						R25Cell3.setCellValue(record.getR25Overdrafts().doubleValue());
						R25Cell3.setCellStyle(numberStyle);
					} else {
						R25Cell3.setCellValue("");
						R25Cell3.setCellStyle(textStyle);
					}
					Cell R25Cell4 = row.createCell(4);
					if (record.getR25OtherInstallmentLoans() != null) {
						R25Cell4.setCellValue(record.getR25OtherInstallmentLoans().doubleValue());
						R25Cell4.setCellStyle(numberStyle);
					} else {
						R25Cell4.setCellValue("");
						R25Cell4.setCellStyle(textStyle);
					}

					// --- R26 (Row Index 25) ---
					row = sheet.getRow(25);
					Cell R26Cell1 = row.createCell(1);
					if (record1.getR26FactoringDebtors() != null) {
						R26Cell1.setCellValue(record1.getR26FactoringDebtors().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}
					Cell R26Cell2 = row.createCell(2);
					if (record1.getR26Leasing() != null) {
						R26Cell2.setCellValue(record1.getR26Leasing().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}
					Cell R26Cell3 = row.createCell(3);
					if (record.getR26Overdrafts() != null) {
						R26Cell3.setCellValue(record.getR26Overdrafts().doubleValue());
						R26Cell3.setCellStyle(numberStyle);
					} else {
						R26Cell3.setCellValue("");
						R26Cell3.setCellStyle(textStyle);
					}
					Cell R26Cell4 = row.createCell(4);
					if (record.getR26OtherInstallmentLoans() != null) {
						R26Cell4.setCellValue(record.getR26OtherInstallmentLoans().doubleValue());
						R26Cell4.setCellStyle(numberStyle);
					} else {
						R26Cell4.setCellValue("");
						R26Cell4.setCellStyle(textStyle);
					}

					// --- R27 (Row Index 26) ---
					row = sheet.getRow(26);
					Cell R27Cell1 = row.createCell(1);
					if (record1.getR27FactoringDebtors() != null) {
						R27Cell1.setCellValue(record1.getR27FactoringDebtors().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}
					Cell R27Cell2 = row.createCell(2);
					if (record1.getR27Leasing() != null) {
						R27Cell2.setCellValue(record1.getR27Leasing().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}
					Cell R27Cell3 = row.createCell(3);
					if (record.getR27Overdrafts() != null) {
						R27Cell3.setCellValue(record.getR27Overdrafts().doubleValue());
						R27Cell3.setCellStyle(numberStyle);
					} else {
						R27Cell3.setCellValue("");
						R27Cell3.setCellStyle(textStyle);
					}
					Cell R27Cell4 = row.createCell(4);
					if (record.getR27OtherInstallmentLoans() != null) {
						R27Cell4.setCellValue(record.getR27OtherInstallmentLoans().doubleValue());
						R27Cell4.setCellStyle(numberStyle);
					} else {
						R27Cell4.setCellValue("");
						R27Cell4.setCellStyle(textStyle);
					}

					// --- R28 (Row Index 27) ---
					row = sheet.getRow(27);
					Cell R28Cell1 = row.createCell(1);
					if (record1.getR28FactoringDebtors() != null) {
						R28Cell1.setCellValue(record1.getR28FactoringDebtors().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}
					Cell R28Cell2 = row.createCell(2);
					if (record1.getR28Leasing() != null) {
						R28Cell2.setCellValue(record1.getR28Leasing().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}
					Cell R28Cell3 = row.createCell(3);
					if (record.getR28Overdrafts() != null) {
						R28Cell3.setCellValue(record.getR28Overdrafts().doubleValue());
						R28Cell3.setCellStyle(numberStyle);
					} else {
						R28Cell3.setCellValue("");
						R28Cell3.setCellStyle(textStyle);
					}
					Cell R28Cell4 = row.createCell(4);
					if (record.getR28OtherInstallmentLoans() != null) {
						R28Cell4.setCellValue(record.getR28OtherInstallmentLoans().doubleValue());
						R28Cell4.setCellStyle(numberStyle);
					} else {
						R28Cell4.setCellValue("");
						R28Cell4.setCellStyle(textStyle);
					}

					// --- R30 (Row Index 29) ---
					row = sheet.getRow(29);
					Cell R30Cell1 = row.createCell(1);
					if (record1.getR30FactoringDebtors() != null) {
						R30Cell1.setCellValue(record1.getR30FactoringDebtors().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}
					Cell R30Cell2 = row.createCell(2);
					if (record1.getR30Leasing() != null) {
						R30Cell2.setCellValue(record1.getR30Leasing().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}
					Cell R30Cell3 = row.createCell(3);
					if (record.getR30Overdrafts() != null) {
						R30Cell3.setCellValue(record.getR30Overdrafts().doubleValue());
						R30Cell3.setCellStyle(numberStyle);
					} else {
						R30Cell3.setCellValue("");
						R30Cell3.setCellStyle(textStyle);
					}
					Cell R30Cell4 = row.createCell(4);
					if (record.getR30OtherInstallmentLoans() != null) {
						R30Cell4.setCellValue(record.getR30OtherInstallmentLoans().doubleValue());
						R30Cell4.setCellStyle(numberStyle);
					} else {
						R30Cell4.setCellValue("");
						R30Cell4.setCellStyle(textStyle);
					}

					// --- R31 (Row Index 30) ---
					row = sheet.getRow(30);
					Cell R31Cell1 = row.createCell(1);
					if (record1.getR31FactoringDebtors() != null) {
						R31Cell1.setCellValue(record1.getR31FactoringDebtors().doubleValue());
						R31Cell1.setCellStyle(numberStyle);
					} else {
						R31Cell1.setCellValue("");
						R31Cell1.setCellStyle(textStyle);
					}
					Cell R31Cell2 = row.createCell(2);
					if (record1.getR31Leasing() != null) {
						R31Cell2.setCellValue(record1.getR31Leasing().doubleValue());
						R31Cell2.setCellStyle(numberStyle);
					} else {
						R31Cell2.setCellValue("");
						R31Cell2.setCellStyle(textStyle);
					}
					Cell R31Cell3 = row.createCell(3);
					if (record.getR31Overdrafts() != null) {
						R31Cell3.setCellValue(record.getR31Overdrafts().doubleValue());
						R31Cell3.setCellStyle(numberStyle);
					} else {
						R31Cell3.setCellValue("");
						R31Cell3.setCellStyle(textStyle);
					}
					Cell R31Cell4 = row.createCell(4);
					if (record.getR31OtherInstallmentLoans() != null) {
						R31Cell4.setCellValue(record.getR31OtherInstallmentLoans().doubleValue());
						R31Cell4.setCellStyle(numberStyle);
					} else {
						R31Cell4.setCellValue("");
						R31Cell4.setCellStyle(textStyle);
					}

					// --- R32 (Row Index 31) ---
					row = sheet.getRow(31);
					Cell R32Cell1 = row.createCell(1);
					if (record1.getR32FactoringDebtors() != null) {
						R32Cell1.setCellValue(record1.getR32FactoringDebtors().doubleValue());
						R32Cell1.setCellStyle(numberStyle);
					} else {
						R32Cell1.setCellValue("");
						R32Cell1.setCellStyle(textStyle);
					}
					Cell R32Cell2 = row.createCell(2);
					if (record1.getR32Leasing() != null) {
						R32Cell2.setCellValue(record1.getR32Leasing().doubleValue());
						R32Cell2.setCellStyle(numberStyle);
					} else {
						R32Cell2.setCellValue("");
						R32Cell2.setCellStyle(textStyle);
					}
					Cell R32Cell3 = row.createCell(3);
					if (record.getR32Overdrafts() != null) {
						R32Cell3.setCellValue(record.getR32Overdrafts().doubleValue());
						R32Cell3.setCellStyle(numberStyle);
					} else {
						R32Cell3.setCellValue("");
						R32Cell3.setCellStyle(textStyle);
					}
					Cell R32Cell4 = row.createCell(4);
					if (record.getR32OtherInstallmentLoans() != null) {
						R32Cell4.setCellValue(record.getR32OtherInstallmentLoans().doubleValue());
						R32Cell4.setCellStyle(numberStyle);
					} else {
						R32Cell4.setCellValue("");
						R32Cell4.setCellStyle(textStyle);
					}

					// --- R33 (Row Index 32) ---
					row = sheet.getRow(32);
					Cell R33Cell1 = row.createCell(1);
					if (record1.getR33FactoringDebtors() != null) {
						R33Cell1.setCellValue(record1.getR33FactoringDebtors().doubleValue());
						R33Cell1.setCellStyle(numberStyle);
					} else {
						R33Cell1.setCellValue("");
						R33Cell1.setCellStyle(textStyle);
					}
					Cell R33Cell2 = row.createCell(2);
					if (record1.getR33Leasing() != null) {
						R33Cell2.setCellValue(record1.getR33Leasing().doubleValue());
						R33Cell2.setCellStyle(numberStyle);
					} else {
						R33Cell2.setCellValue("");
						R33Cell2.setCellStyle(textStyle);
					}
					Cell R33Cell3 = row.createCell(3);
					if (record.getR33Overdrafts() != null) {
						R33Cell3.setCellValue(record.getR33Overdrafts().doubleValue());
						R33Cell3.setCellStyle(numberStyle);
					} else {
						R33Cell3.setCellValue("");
						R33Cell3.setCellStyle(textStyle);
					}
					Cell R33Cell4 = row.createCell(4);
					if (record.getR33OtherInstallmentLoans() != null) {
						R33Cell4.setCellValue(record.getR33OtherInstallmentLoans().doubleValue());
						R33Cell4.setCellStyle(numberStyle);
					} else {
						R33Cell4.setCellValue("");
						R33Cell4.setCellStyle(textStyle);
					}

					// --- R34 (Row Index 33) ---
					row = sheet.getRow(33);
					Cell R34Cell1 = row.createCell(1);
					if (record1.getR34FactoringDebtors() != null) {
						R34Cell1.setCellValue(record1.getR34FactoringDebtors().doubleValue());
						R34Cell1.setCellStyle(numberStyle);
					} else {
						R34Cell1.setCellValue("");
						R34Cell1.setCellStyle(textStyle);
					}
					Cell R34Cell2 = row.createCell(2);
					if (record1.getR34Leasing() != null) {
						R34Cell2.setCellValue(record1.getR34Leasing().doubleValue());
						R34Cell2.setCellStyle(numberStyle);
					} else {
						R34Cell2.setCellValue("");
						R34Cell2.setCellStyle(textStyle);
					}
					Cell R34Cell3 = row.createCell(3);
					if (record.getR34Overdrafts() != null) {
						R34Cell3.setCellValue(record.getR34Overdrafts().doubleValue());
						R34Cell3.setCellStyle(numberStyle);
					} else {
						R34Cell3.setCellValue("");
						R34Cell3.setCellStyle(textStyle);
					}
					Cell R34Cell4 = row.createCell(4);
					if (record.getR34OtherInstallmentLoans() != null) {
						R34Cell4.setCellValue(record.getR34OtherInstallmentLoans().doubleValue());
						R34Cell4.setCellStyle(numberStyle);
					} else {
						R34Cell4.setCellValue("");
						R34Cell4.setCellStyle(textStyle);
					}

					// --- R35 (Row Index 34) ---
					row = sheet.getRow(34);
					Cell R35Cell1 = row.createCell(1);
					if (record1.getR35FactoringDebtors() != null) {
						R35Cell1.setCellValue(record1.getR35FactoringDebtors().doubleValue());
						R35Cell1.setCellStyle(numberStyle);
					} else {
						R35Cell1.setCellValue("");
						R35Cell1.setCellStyle(textStyle);
					}
					Cell R35Cell2 = row.createCell(2);
					if (record1.getR35Leasing() != null) {
						R35Cell2.setCellValue(record1.getR35Leasing().doubleValue());
						R35Cell2.setCellStyle(numberStyle);
					} else {
						R35Cell2.setCellValue("");
						R35Cell2.setCellStyle(textStyle);
					}
					Cell R35Cell3 = row.createCell(3);
					if (record.getR35Overdrafts() != null) {
						R35Cell3.setCellValue(record.getR35Overdrafts().doubleValue());
						R35Cell3.setCellStyle(numberStyle);
					} else {
						R35Cell3.setCellValue("");
						R35Cell3.setCellStyle(textStyle);
					}
					Cell R35Cell4 = row.createCell(4);
					if (record.getR35OtherInstallmentLoans() != null) {
						R35Cell4.setCellValue(record.getR35OtherInstallmentLoans().doubleValue());
						R35Cell4.setCellStyle(numberStyle);
					} else {
						R35Cell4.setCellValue("");
						R35Cell4.setCellStyle(textStyle);
					}

					// --- R36 (Row Index 35) ---
					row = sheet.getRow(35);
					Cell R36Cell1 = row.createCell(1);
					if (record1.getR36FactoringDebtors() != null) {
						R36Cell1.setCellValue(record1.getR36FactoringDebtors().doubleValue());
						R36Cell1.setCellStyle(numberStyle);
					} else {
						R36Cell1.setCellValue("");
						R36Cell1.setCellStyle(textStyle);
					}
					Cell R36Cell2 = row.createCell(2);
					if (record1.getR36Leasing() != null) {
						R36Cell2.setCellValue(record1.getR36Leasing().doubleValue());
						R36Cell2.setCellStyle(numberStyle);
					} else {
						R36Cell2.setCellValue("");
						R36Cell2.setCellStyle(textStyle);
					}
					Cell R36Cell3 = row.createCell(3);
					if (record.getR36Overdrafts() != null) {
						R36Cell3.setCellValue(record.getR36Overdrafts().doubleValue());
						R36Cell3.setCellStyle(numberStyle);
					} else {
						R36Cell3.setCellValue("");
						R36Cell3.setCellStyle(textStyle);
					}
					Cell R36Cell4 = row.createCell(4);
					if (record.getR36OtherInstallmentLoans() != null) {
						R36Cell4.setCellValue(record.getR36OtherInstallmentLoans().doubleValue());
						R36Cell4.setCellStyle(numberStyle);
					} else {
						R36Cell4.setCellValue("");
						R36Cell4.setCellStyle(textStyle);
					}

					// --- R37 (Row Index 36) ---
					row = sheet.getRow(36);
					Cell R37Cell1 = row.createCell(1);
					if (record1.getR37FactoringDebtors() != null) {
						R37Cell1.setCellValue(record1.getR37FactoringDebtors().doubleValue());
						R37Cell1.setCellStyle(numberStyle);
					} else {
						R37Cell1.setCellValue("");
						R37Cell1.setCellStyle(textStyle);
					}
					Cell R37Cell2 = row.createCell(2);
					if (record1.getR37Leasing() != null) {
						R37Cell2.setCellValue(record1.getR37Leasing().doubleValue());
						R37Cell2.setCellStyle(numberStyle);
					} else {
						R37Cell2.setCellValue("");
						R37Cell2.setCellStyle(textStyle);
					}
					Cell R37Cell3 = row.createCell(3);
					if (record.getR37Overdrafts() != null) {
						R37Cell3.setCellValue(record.getR37Overdrafts().doubleValue());
						R37Cell3.setCellStyle(numberStyle);
					} else {
						R37Cell3.setCellValue("");
						R37Cell3.setCellStyle(textStyle);
					}
					Cell R37Cell4 = row.createCell(4);
					if (record.getR37OtherInstallmentLoans() != null) {
						R37Cell4.setCellValue(record.getR37OtherInstallmentLoans().doubleValue());
						R37Cell4.setCellStyle(numberStyle);
					} else {
						R37Cell4.setCellValue("");
						R37Cell4.setCellStyle(textStyle);
					}
					// --- R39 (Row Index 38) ---
					row = sheet.getRow(38);
					Cell R39Cell1 = row.createCell(1);
					if (record1.getR39FactoringDebtors() != null) {
						R39Cell1.setCellValue(record1.getR39FactoringDebtors().doubleValue());
						R39Cell1.setCellStyle(numberStyle);
					} else {
						R39Cell1.setCellValue("");
						R39Cell1.setCellStyle(textStyle);
					}
					Cell R39Cell2 = row.createCell(2);
					if (record1.getR39Leasing() != null) {
						R39Cell2.setCellValue(record1.getR39Leasing().doubleValue());
						R39Cell2.setCellStyle(numberStyle);
					} else {
						R39Cell2.setCellValue("");
						R39Cell2.setCellStyle(textStyle);
					}
					Cell R39Cell3 = row.createCell(3);
					if (record.getR39Overdrafts() != null) {
						R39Cell3.setCellValue(record.getR39Overdrafts().doubleValue());
						R39Cell3.setCellStyle(numberStyle);
					} else {
						R39Cell3.setCellValue("");
						R39Cell3.setCellStyle(textStyle);
					}
					Cell R39Cell4 = row.createCell(4);
					if (record.getR39OtherInstallmentLoans() != null) {
						R39Cell4.setCellValue(record.getR39OtherInstallmentLoans().doubleValue());
						R39Cell4.setCellStyle(numberStyle);
					} else {
						R39Cell4.setCellValue("");
						R39Cell4.setCellStyle(textStyle);
					}

					// --- R40 (Row Index 39) ---
					row = sheet.getRow(39);
					Cell R40Cell1 = row.createCell(1);
					if (record1.getR40FactoringDebtors() != null) {
						R40Cell1.setCellValue(record1.getR40FactoringDebtors().doubleValue());
						R40Cell1.setCellStyle(numberStyle);
					} else {
						R40Cell1.setCellValue("");
						R40Cell1.setCellStyle(textStyle);
					}
					Cell R40Cell2 = row.createCell(2);
					if (record1.getR40Leasing() != null) {
						R40Cell2.setCellValue(record1.getR40Leasing().doubleValue());
						R40Cell2.setCellStyle(numberStyle);
					} else {
						R40Cell2.setCellValue("");
						R40Cell2.setCellStyle(textStyle);
					}
					Cell R40Cell3 = row.createCell(3);
					if (record.getR40Overdrafts() != null) {
						R40Cell3.setCellValue(record.getR40Overdrafts().doubleValue());
						R40Cell3.setCellStyle(numberStyle);
					} else {
						R40Cell3.setCellValue("");
						R40Cell3.setCellStyle(textStyle);
					}
					Cell R40Cell4 = row.createCell(4);
					if (record.getR40OtherInstallmentLoans() != null) {
						R40Cell4.setCellValue(record.getR40OtherInstallmentLoans().doubleValue());
						R40Cell4.setCellStyle(numberStyle);
					} else {
						R40Cell4.setCellValue("");
						R40Cell4.setCellStyle(textStyle);
					}
					// --- R42 (Row Index 41) ---
					row = sheet.getRow(41);
					Cell R42Cell1 = row.createCell(1);
					if (record1.getR42FactoringDebtors() != null) {
						R42Cell1.setCellValue(record1.getR42FactoringDebtors().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}
					Cell R42Cell2 = row.createCell(2);
					if (record1.getR42Leasing() != null) {
						R42Cell2.setCellValue(record1.getR42Leasing().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}
					Cell R42Cell3 = row.createCell(3);
					if (record.getR42Overdrafts() != null) {
						R42Cell3.setCellValue(record.getR42Overdrafts().doubleValue());
						R42Cell3.setCellStyle(numberStyle);
					} else {
						R42Cell3.setCellValue("");
						R42Cell3.setCellStyle(textStyle);
					}
					Cell R42Cell4 = row.createCell(4);
					if (record.getR42OtherInstallmentLoans() != null) {
						R42Cell4.setCellValue(record.getR42OtherInstallmentLoans().doubleValue());
						R42Cell4.setCellStyle(numberStyle);
					} else {
						R42Cell4.setCellValue("");
						R42Cell4.setCellStyle(textStyle);
					}

					// --- R43 (Row Index 42) ---
					row = sheet.getRow(42);
					Cell R43Cell1 = row.createCell(1);
					if (record1.getR43FactoringDebtors() != null) {
						R43Cell1.setCellValue(record1.getR43FactoringDebtors().doubleValue());
						R43Cell1.setCellStyle(numberStyle);
					} else {
						R43Cell1.setCellValue("");
						R43Cell1.setCellStyle(textStyle);
					}
					Cell R43Cell2 = row.createCell(2);
					if (record1.getR43Leasing() != null) {
						R43Cell2.setCellValue(record1.getR43Leasing().doubleValue());
						R43Cell2.setCellStyle(numberStyle);
					} else {
						R43Cell2.setCellValue("");
						R43Cell2.setCellStyle(textStyle);
					}
					Cell R43Cell3 = row.createCell(3);
					if (record.getR43Overdrafts() != null) {
						R43Cell3.setCellValue(record.getR43Overdrafts().doubleValue());
						R43Cell3.setCellStyle(numberStyle);
					} else {
						R43Cell3.setCellValue("");
						R43Cell3.setCellStyle(textStyle);
					}
					Cell R43Cell4 = row.createCell(4);
					if (record.getR43OtherInstallmentLoans() != null) {
						R43Cell4.setCellValue(record.getR43OtherInstallmentLoans().doubleValue());
						R43Cell4.setCellStyle(numberStyle);
					} else {
						R43Cell4.setCellValue("");
						R43Cell4.setCellStyle(textStyle);
					}
					// --- R45 (Row Index 44) ---
					row = sheet.getRow(44);
					Cell R45Cell1 = row.createCell(1);
					if (record1.getR45FactoringDebtors() != null) {
						R45Cell1.setCellValue(record1.getR45FactoringDebtors().doubleValue());
						R45Cell1.setCellStyle(numberStyle);
					} else {
						R45Cell1.setCellValue("");
						R45Cell1.setCellStyle(textStyle);
					}
					Cell R45Cell2 = row.createCell(2);
					if (record1.getR45Leasing() != null) {
						R45Cell2.setCellValue(record1.getR45Leasing().doubleValue());
						R45Cell2.setCellStyle(numberStyle);
					} else {
						R45Cell2.setCellValue("");
						R45Cell2.setCellStyle(textStyle);
					}
					Cell R45Cell3 = row.createCell(3);
					if (record.getR45Overdrafts() != null) {
						R45Cell3.setCellValue(record.getR45Overdrafts().doubleValue());
						R45Cell3.setCellStyle(numberStyle);
					} else {
						R45Cell3.setCellValue("");
						R45Cell3.setCellStyle(textStyle);
					}
					Cell R45Cell4 = row.createCell(4);
					if (record.getR45OtherInstallmentLoans() != null) {
						R45Cell4.setCellValue(record.getR45OtherInstallmentLoans().doubleValue());
						R45Cell4.setCellStyle(numberStyle);
					} else {
						R45Cell4.setCellValue("");
						R45Cell4.setCellStyle(textStyle);
					}

					// --- R46 (Row Index 45) ---
					row = sheet.getRow(45);
					Cell R46Cell1 = row.createCell(1);
					if (record1.getR46FactoringDebtors() != null) {
						R46Cell1.setCellValue(record1.getR46FactoringDebtors().doubleValue());
						R46Cell1.setCellStyle(numberStyle);
					} else {
						R46Cell1.setCellValue("");
						R46Cell1.setCellStyle(textStyle);
					}
					Cell R46Cell2 = row.createCell(2);
					if (record1.getR46Leasing() != null) {
						R46Cell2.setCellValue(record1.getR46Leasing().doubleValue());
						R46Cell2.setCellStyle(numberStyle);
					} else {
						R46Cell2.setCellValue("");
						R46Cell2.setCellStyle(textStyle);
					}
					Cell R46Cell3 = row.createCell(3);
					if (record.getR46Overdrafts() != null) {
						R46Cell3.setCellValue(record.getR46Overdrafts().doubleValue());
						R46Cell3.setCellStyle(numberStyle);
					} else {
						R46Cell3.setCellValue("");
						R46Cell3.setCellStyle(textStyle);
					}
					Cell R46Cell4 = row.createCell(4);
					if (record.getR46OtherInstallmentLoans() != null) {
						R46Cell4.setCellValue(record.getR46OtherInstallmentLoans().doubleValue());
						R46Cell4.setCellStyle(numberStyle);
					} else {
						R46Cell4.setCellValue("");
						R46Cell4.setCellStyle(textStyle);
					}

					// --- R47 (Row Index 46) ---
					row = sheet.getRow(46);
					Cell R47Cell1 = row.createCell(1);
					if (record1.getR47FactoringDebtors() != null) {
						R47Cell1.setCellValue(record1.getR47FactoringDebtors().doubleValue());
						R47Cell1.setCellStyle(numberStyle);
					} else {
						R47Cell1.setCellValue("");
						R47Cell1.setCellStyle(textStyle);
					}
					Cell R47Cell2 = row.createCell(2);
					if (record1.getR47Leasing() != null) {
						R47Cell2.setCellValue(record1.getR47Leasing().doubleValue());
						R47Cell2.setCellStyle(numberStyle);
					} else {
						R47Cell2.setCellValue("");
						R47Cell2.setCellStyle(textStyle);
					}
					Cell R47Cell3 = row.createCell(3);
					if (record.getR47Overdrafts() != null) {
						R47Cell3.setCellValue(record.getR47Overdrafts().doubleValue());
						R47Cell3.setCellStyle(numberStyle);
					} else {
						R47Cell3.setCellValue("");
						R47Cell3.setCellStyle(textStyle);
					}
					Cell R47Cell4 = row.createCell(4);
					if (record.getR47OtherInstallmentLoans() != null) {
						R47Cell4.setCellValue(record.getR47OtherInstallmentLoans().doubleValue());
						R47Cell4.setCellStyle(numberStyle);
					} else {
						R47Cell4.setCellValue("");
						R47Cell4.setCellStyle(textStyle);
					}

					// --- R48 (Row Index 47) ---
					row = sheet.getRow(47);
					Cell R48Cell1 = row.createCell(1);
					if (record1.getR48FactoringDebtors() != null) {
						R48Cell1.setCellValue(record1.getR48FactoringDebtors().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}
					Cell R48Cell2 = row.createCell(2);
					if (record1.getR48Leasing() != null) {
						R48Cell2.setCellValue(record1.getR48Leasing().doubleValue());
						R48Cell2.setCellStyle(numberStyle);
					} else {
						R48Cell2.setCellValue("");
						R48Cell2.setCellStyle(textStyle);
					}
					Cell R48Cell3 = row.createCell(3);
					if (record.getR48Overdrafts() != null) {
						R48Cell3.setCellValue(record.getR48Overdrafts().doubleValue());
						R48Cell3.setCellStyle(numberStyle);
					} else {
						R48Cell3.setCellValue("");
						R48Cell3.setCellStyle(textStyle);
					}
					Cell R48Cell4 = row.createCell(4);
					if (record.getR48OtherInstallmentLoans() != null) {
						R48Cell4.setCellValue(record.getR48OtherInstallmentLoans().doubleValue());
						R48Cell4.setCellStyle(numberStyle);
					} else {
						R48Cell4.setCellValue("");
						R48Cell4.setCellStyle(textStyle);
					}

					// --- R50 (Row Index 49) ---
					row = sheet.getRow(49);
					Cell R50Cell1 = row.createCell(1);
					if (record1.getR50FactoringDebtors() != null) {
						R50Cell1.setCellValue(record1.getR50FactoringDebtors().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}
					Cell R50Cell2 = row.createCell(2);
					if (record1.getR50Leasing() != null) {
						R50Cell2.setCellValue(record1.getR50Leasing().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}
					Cell R50Cell3 = row.createCell(3);
					if (record.getR50Overdrafts() != null) {
						R50Cell3.setCellValue(record.getR50Overdrafts().doubleValue());
						R50Cell3.setCellStyle(numberStyle);
					} else {
						R50Cell3.setCellValue("");
						R50Cell3.setCellStyle(textStyle);
					}
					Cell R50Cell4 = row.createCell(4);
					if (record.getR50OtherInstallmentLoans() != null) {
						R50Cell4.setCellValue(record.getR50OtherInstallmentLoans().doubleValue());
						R50Cell4.setCellStyle(numberStyle);
					} else {
						R50Cell4.setCellValue("");
						R50Cell4.setCellStyle(textStyle);
					}

					// --- R51 (Row Index 50) ---
					row = sheet.getRow(50);
					Cell R51Cell1 = row.createCell(1);
					if (record1.getR51FactoringDebtors() != null) {
						R51Cell1.setCellValue(record1.getR51FactoringDebtors().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}
					Cell R51Cell2 = row.createCell(2);
					if (record1.getR51Leasing() != null) {
						R51Cell2.setCellValue(record1.getR51Leasing().doubleValue());
						R51Cell2.setCellStyle(numberStyle);
					} else {
						R51Cell2.setCellValue("");
						R51Cell2.setCellStyle(textStyle);
					}
					Cell R51Cell3 = row.createCell(3);
					if (record.getR51Overdrafts() != null) {
						R51Cell3.setCellValue(record.getR51Overdrafts().doubleValue());
						R51Cell3.setCellStyle(numberStyle);
					} else {
						R51Cell3.setCellValue("");
						R51Cell3.setCellStyle(textStyle);
					}
					Cell R51Cell4 = row.createCell(4);
					if (record.getR51OtherInstallmentLoans() != null) {
						R51Cell4.setCellValue(record.getR51OtherInstallmentLoans().doubleValue());
						R51Cell4.setCellStyle(numberStyle);
					} else {
						R51Cell4.setCellValue("");
						R51Cell4.setCellStyle(textStyle);
					}

					// --- R52 (Row Index 51) ---
					row = sheet.getRow(51);
					Cell R52Cell1 = row.createCell(1);
					if (record1.getR52FactoringDebtors() != null) {
						R52Cell1.setCellValue(record1.getR52FactoringDebtors().doubleValue());
						R52Cell1.setCellStyle(numberStyle);
					} else {
						R52Cell1.setCellValue("");
						R52Cell1.setCellStyle(textStyle);
					}
					Cell R52Cell2 = row.createCell(2);
					if (record1.getR52Leasing() != null) {
						R52Cell2.setCellValue(record1.getR52Leasing().doubleValue());
						R52Cell2.setCellStyle(numberStyle);
					} else {
						R52Cell2.setCellValue("");
						R52Cell2.setCellStyle(textStyle);
					}
					Cell R52Cell3 = row.createCell(3);
					if (record.getR52Overdrafts() != null) {
						R52Cell3.setCellValue(record.getR52Overdrafts().doubleValue());
						R52Cell3.setCellStyle(numberStyle);
					} else {
						R52Cell3.setCellValue("");
						R52Cell3.setCellStyle(textStyle);
					}
					Cell R52Cell4 = row.createCell(4);
					if (record.getR52OtherInstallmentLoans() != null) {
						R52Cell4.setCellValue(record.getR52OtherInstallmentLoans().doubleValue());
						R52Cell4.setCellStyle(numberStyle);
					} else {
						R52Cell4.setCellValue("");
						R52Cell4.setCellStyle(textStyle);
					}
					// --- R54 (Row Index 53) ---
					row = sheet.getRow(53);
					Cell R54Cell1 = row.createCell(1);
					if (record1.getR54FactoringDebtors() != null) {
						R54Cell1.setCellValue(record1.getR54FactoringDebtors().doubleValue());
						R54Cell1.setCellStyle(numberStyle);
					} else {
						R54Cell1.setCellValue("");
						R54Cell1.setCellStyle(textStyle);
					}
					Cell R54Cell2 = row.createCell(2);
					if (record1.getR54Leasing() != null) {
						R54Cell2.setCellValue(record1.getR54Leasing().doubleValue());
						R54Cell2.setCellStyle(numberStyle);
					} else {
						R54Cell2.setCellValue("");
						R54Cell2.setCellStyle(textStyle);
					}
					Cell R54Cell3 = row.createCell(3);
					if (record.getR54Overdrafts() != null) {
						R54Cell3.setCellValue(record.getR54Overdrafts().doubleValue());
						R54Cell3.setCellStyle(numberStyle);
					} else {
						R54Cell3.setCellValue("");
						R54Cell3.setCellStyle(textStyle);
					}
					Cell R54Cell4 = row.createCell(4);
					if (record.getR54OtherInstallmentLoans() != null) {
						R54Cell4.setCellValue(record.getR54OtherInstallmentLoans().doubleValue());
						R54Cell4.setCellStyle(numberStyle);
					} else {
						R54Cell4.setCellValue("");
						R54Cell4.setCellStyle(textStyle);
					}

					// --- R55 (Row Index 54) ---
					row = sheet.getRow(54);
					Cell R55Cell1 = row.createCell(1);
					if (record1.getR55FactoringDebtors() != null) {
						R55Cell1.setCellValue(record1.getR55FactoringDebtors().doubleValue());
						R55Cell1.setCellStyle(numberStyle);
					} else {
						R55Cell1.setCellValue("");
						R55Cell1.setCellStyle(textStyle);
					}
					Cell R55Cell2 = row.createCell(2);
					if (record1.getR55Leasing() != null) {
						R55Cell2.setCellValue(record1.getR55Leasing().doubleValue());
						R55Cell2.setCellStyle(numberStyle);
					} else {
						R55Cell2.setCellValue("");
						R55Cell2.setCellStyle(textStyle);
					}
					Cell R55Cell3 = row.createCell(3);
					if (record.getR55Overdrafts() != null) {
						R55Cell3.setCellValue(record.getR55Overdrafts().doubleValue());
						R55Cell3.setCellStyle(numberStyle);
					} else {
						R55Cell3.setCellValue("");
						R55Cell3.setCellStyle(textStyle);
					}
					Cell R55Cell4 = row.createCell(4);
					if (record.getR55OtherInstallmentLoans() != null) {
						R55Cell4.setCellValue(record.getR55OtherInstallmentLoans().doubleValue());
						R55Cell4.setCellStyle(numberStyle);
					} else {
						R55Cell4.setCellValue("");
						R55Cell4.setCellStyle(textStyle);
					}

					// --- R56 (Row Index 55) ---
					row = sheet.getRow(55);
					Cell R56Cell1 = row.createCell(1);
					if (record1.getR56FactoringDebtors() != null) {
						R56Cell1.setCellValue(record1.getR56FactoringDebtors().doubleValue());
						R56Cell1.setCellStyle(numberStyle);
					} else {
						R56Cell1.setCellValue("");
						R56Cell1.setCellStyle(textStyle);
					}
					Cell R56Cell2 = row.createCell(2);
					if (record1.getR56Leasing() != null) {
						R56Cell2.setCellValue(record1.getR56Leasing().doubleValue());
						R56Cell2.setCellStyle(numberStyle);
					} else {
						R56Cell2.setCellValue("");
						R56Cell2.setCellStyle(textStyle);
					}
					Cell R56Cell3 = row.createCell(3);
					if (record.getR56Overdrafts() != null) {
						R56Cell3.setCellValue(record.getR56Overdrafts().doubleValue());
						R56Cell3.setCellStyle(numberStyle);
					} else {
						R56Cell3.setCellValue("");
						R56Cell3.setCellStyle(textStyle);
					}
					Cell R56Cell4 = row.createCell(4);
					if (record.getR56OtherInstallmentLoans() != null) {
						R56Cell4.setCellValue(record.getR56OtherInstallmentLoans().doubleValue());
						R56Cell4.setCellStyle(numberStyle);
					} else {
						R56Cell4.setCellValue("");
						R56Cell4.setCellStyle(textStyle);
					}
					// --- R58 (Row Index 57) ---
					row = sheet.getRow(57);
					Cell R58Cell1 = row.createCell(1);
					if (record1.getR58FactoringDebtors() != null) {
						R58Cell1.setCellValue(record1.getR58FactoringDebtors().doubleValue());
						R58Cell1.setCellStyle(numberStyle);
					} else {
						R58Cell1.setCellValue("");
						R58Cell1.setCellStyle(textStyle);
					}
					Cell R58Cell2 = row.createCell(2);
					if (record1.getR58Leasing() != null) {
						R58Cell2.setCellValue(record1.getR58Leasing().doubleValue());
						R58Cell2.setCellStyle(numberStyle);
					} else {
						R58Cell2.setCellValue("");
						R58Cell2.setCellStyle(textStyle);
					}
					Cell R58Cell3 = row.createCell(3);
					if (record.getR58Overdrafts() != null) {
						R58Cell3.setCellValue(record.getR58Overdrafts().doubleValue());
						R58Cell3.setCellStyle(numberStyle);
					} else {
						R58Cell3.setCellValue("");
						R58Cell3.setCellStyle(textStyle);
					}
					Cell R58Cell4 = row.createCell(4);
					if (record.getR58OtherInstallmentLoans() != null) {
						R58Cell4.setCellValue(record.getR58OtherInstallmentLoans().doubleValue());
						R58Cell4.setCellStyle(numberStyle);
					} else {
						R58Cell4.setCellValue("");
						R58Cell4.setCellStyle(textStyle);
					}

					// --- R59 (Row Index 58) ---
					row = sheet.getRow(58);
					Cell R59Cell1 = row.createCell(1);
					if (record1.getR59FactoringDebtors() != null) {
						R59Cell1.setCellValue(record1.getR59FactoringDebtors().doubleValue());
						R59Cell1.setCellStyle(numberStyle);
					} else {
						R59Cell1.setCellValue("");
						R59Cell1.setCellStyle(textStyle);
					}
					Cell R59Cell2 = row.createCell(2);
					if (record1.getR59Leasing() != null) {
						R59Cell2.setCellValue(record1.getR59Leasing().doubleValue());
						R59Cell2.setCellStyle(numberStyle);
					} else {
						R59Cell2.setCellValue("");
						R59Cell2.setCellStyle(textStyle);
					}
					Cell R59Cell3 = row.createCell(3);
					if (record.getR59Overdrafts() != null) {
						R59Cell3.setCellValue(record.getR59Overdrafts().doubleValue());
						R59Cell3.setCellStyle(numberStyle);
					} else {
						R59Cell3.setCellValue("");
						R59Cell3.setCellStyle(textStyle);
					}
					Cell R59Cell4 = row.createCell(4);
					if (record.getR59OtherInstallmentLoans() != null) {
						R59Cell4.setCellValue(record.getR59OtherInstallmentLoans().doubleValue());
						R59Cell4.setCellStyle(numberStyle);
					} else {
						R59Cell4.setCellValue("");
						R59Cell4.setCellStyle(textStyle);
					}

					// --- R60 (Row Index 59) ---
					row = sheet.getRow(59);
					Cell R60Cell1 = row.createCell(1);
					if (record1.getR60FactoringDebtors() != null) {
						R60Cell1.setCellValue(record1.getR60FactoringDebtors().doubleValue());
						R60Cell1.setCellStyle(numberStyle);
					} else {
						R60Cell1.setCellValue("");
						R60Cell1.setCellStyle(textStyle);
					}
					Cell R60Cell2 = row.createCell(2);
					if (record1.getR60Leasing() != null) {
						R60Cell2.setCellValue(record1.getR60Leasing().doubleValue());
						R60Cell2.setCellStyle(numberStyle);
					} else {
						R60Cell2.setCellValue("");
						R60Cell2.setCellStyle(textStyle);
					}
					Cell R60Cell3 = row.createCell(3);
					if (record.getR60Overdrafts() != null) {
						R60Cell3.setCellValue(record.getR60Overdrafts().doubleValue());
						R60Cell3.setCellStyle(numberStyle);
					} else {
						R60Cell3.setCellValue("");
						R60Cell3.setCellStyle(textStyle);
					}
					Cell R60Cell4 = row.createCell(4);
					if (record.getR60OtherInstallmentLoans() != null) {
						R60Cell4.setCellValue(record.getR60OtherInstallmentLoans().doubleValue());
						R60Cell4.setCellStyle(numberStyle);
					} else {
						R60Cell4.setCellValue("");
						R60Cell4.setCellStyle(textStyle);
					}

					// --- R61 (Row Index 60) ---
					row = sheet.getRow(60);
					Cell R61Cell1 = row.createCell(1);
					if (record1.getR61FactoringDebtors() != null) {
						R61Cell1.setCellValue(record1.getR61FactoringDebtors().doubleValue());
						R61Cell1.setCellStyle(numberStyle);
					} else {
						R61Cell1.setCellValue("");
						R61Cell1.setCellStyle(textStyle);
					}
					Cell R61Cell2 = row.createCell(2);
					if (record1.getR61Leasing() != null) {
						R61Cell2.setCellValue(record1.getR61Leasing().doubleValue());
						R61Cell2.setCellStyle(numberStyle);
					} else {
						R61Cell2.setCellValue("");
						R61Cell2.setCellStyle(textStyle);
					}
					Cell R61Cell3 = row.createCell(3);
					if (record.getR61Overdrafts() != null) {
						R61Cell3.setCellValue(record.getR61Overdrafts().doubleValue());
						R61Cell3.setCellStyle(numberStyle);
					} else {
						R61Cell3.setCellValue("");
						R61Cell3.setCellStyle(textStyle);
					}
					Cell R61Cell4 = row.createCell(4);
					if (record.getR61OtherInstallmentLoans() != null) {
						R61Cell4.setCellValue(record.getR61OtherInstallmentLoans().doubleValue());
						R61Cell4.setCellStyle(numberStyle);
					} else {
						R61Cell4.setCellValue("");
						R61Cell4.setCellStyle(textStyle);
					}

					// --- R62 (Row Index 61) ---
					row = sheet.getRow(61);
					Cell R62Cell1 = row.createCell(1);
					if (record1.getR62FactoringDebtors() != null) {
						R62Cell1.setCellValue(record1.getR62FactoringDebtors().doubleValue());
						R62Cell1.setCellStyle(numberStyle);
					} else {
						R62Cell1.setCellValue("");
						R62Cell1.setCellStyle(textStyle);
					}
					Cell R62Cell2 = row.createCell(2);
					if (record1.getR62Leasing() != null) {
						R62Cell2.setCellValue(record1.getR62Leasing().doubleValue());
						R62Cell2.setCellStyle(numberStyle);
					} else {
						R62Cell2.setCellValue("");
						R62Cell2.setCellStyle(textStyle);
					}
					Cell R62Cell3 = row.createCell(3);
					if (record.getR62Overdrafts() != null) {
						R62Cell3.setCellValue(record.getR62Overdrafts().doubleValue());
						R62Cell3.setCellStyle(numberStyle);
					} else {
						R62Cell3.setCellValue("");
						R62Cell3.setCellStyle(textStyle);
					}
					Cell R62Cell4 = row.createCell(4);
					if (record.getR62OtherInstallmentLoans() != null) {
						R62Cell4.setCellValue(record.getR62OtherInstallmentLoans().doubleValue());
						R62Cell4.setCellStyle(numberStyle);
					} else {
						R62Cell4.setCellValue("");
						R62Cell4.setCellStyle(textStyle);
					}

					// --- R63 (Row Index 62) ---
					row = sheet.getRow(62);
					Cell R63Cell1 = row.createCell(1);
					if (record1.getR63FactoringDebtors() != null) {
						R63Cell1.setCellValue(record1.getR63FactoringDebtors().doubleValue());
						R63Cell1.setCellStyle(numberStyle);
					} else {
						R63Cell1.setCellValue("");
						R63Cell1.setCellStyle(textStyle);
					}
					Cell R63Cell2 = row.createCell(2);
					if (record1.getR63Leasing() != null) {
						R63Cell2.setCellValue(record1.getR63Leasing().doubleValue());
						R63Cell2.setCellStyle(numberStyle);
					} else {
						R63Cell2.setCellValue("");
						R63Cell2.setCellStyle(textStyle);
					}
					Cell R63Cell3 = row.createCell(3);
					if (record.getR63Overdrafts() != null) {
						R63Cell3.setCellValue(record.getR63Overdrafts().doubleValue());
						R63Cell3.setCellStyle(numberStyle);
					} else {
						R63Cell3.setCellValue("");
						R63Cell3.setCellStyle(textStyle);
					}
					Cell R63Cell4 = row.createCell(4);
					if (record.getR63OtherInstallmentLoans() != null) {
						R63Cell4.setCellValue(record.getR63OtherInstallmentLoans().doubleValue());
						R63Cell4.setCellStyle(numberStyle);
					} else {
						R63Cell4.setCellValue("");
						R63Cell4.setCellStyle(textStyle);
					}
				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA4 EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_LA4_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}
}