package com.bornfire.brrs.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.entities.SlsHistoricalDataEntity;
import com.bornfire.brrs.entities.SlsHistoricalDataRepository;

/**
 * Uploads SLS_HISTORICAL_DATA_<MONTH>_<YEAR>.xls into BRRS.BRRS_SLS_HISTORICAL_DATA.
 *
 * Expected sheet layout (header in row 1, one row per month):
 *   Month | (blank = "JUNE 2023") | CA | CALL | SB | FDR | Total Dep | BC |
 *   Other Liability | Other Asset | OD | Loan | TOTAL LOAN | CD RATIO
 *
 * Behaviour: UPSERT by year-month. The file is a rolling window of history, so
 * months already in the table are updated and new months are inserted. The whole
 * upload is one transaction - if any row is invalid nothing is saved.
 */
@Service
public class SlsHistoricalDataUploadService {

	private static final Logger logger = LoggerFactory.getLogger(SlsHistoricalDataUploadService.class);

	/**
	 * How NEW months are stored in MONTH_YEAR: true = last day of month
	 * (JUNE 2023 -> 30-JUN-2023), false = first day (01-JUN-2023).
	 * Months that already exist keep whatever MONTH_YEAR value they have.
	 */
	private static final boolean STORE_MONTH_END = true;

	private static final String[] REQUIRED_HEADERS = { "CA", "CALL", "SB", "FDR", "TOTALDEP", "BC", "OTHERLIABILITY",
			"OTHERASSET", "OD", "LOAN", "TOTALLOAN", "CDRATIO" };

	// 2-digit-year patterns MUST come before their 4-digit-year equivalents.
	// SimpleDateFormat's "yyyy" will happily accept a 2-digit input (e.g. "26"
	// becomes literal year 26 AD, not 2026) and still report a full match, so if
	// "dd-MMM-yyyy" were tried first it would silently "succeed" on "31-MAY-26"
	// with the wrong year. Trying "dd-MMM-yy" first lets SimpleDateFormat apply
	// its proper 2-digit-year windowing (e.g. 26 -> 2026) instead.
	private static final String[] MONTH_YEAR_PATTERNS = { "MMMM yyyy", "MMM yyyy", "MMMM-yyyy", "MMM-yyyy", "MM/yyyy",
			"yyyy-MM", "dd-MMM-yy", "dd-MMM-yyyy", "dd/MM/yy", "dd/MM/yyyy", "yyyy-MM-dd" };

	@Autowired
	private SlsHistoricalDataRepository repository;

	@Transactional
	public String uploadSlsHistoricalFile(MultipartFile file) throws Exception {

		// 1. File validation
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("The uploaded file is empty.");
		}
		String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			throw new IllegalArgumentException("Invalid file! Please upload an Excel file (.xls or .xlsx).");
		}
		if (!fileName.contains("sls")) {
			throw new IllegalArgumentException("Invalid file! File name must contain 'SLS' (e.g. SLS_HISTORICAL_DATA_MAY_2026.xls).");
		}

		// 2. Parse the sheet into transient entities, keyed by year-month (yyyy-MM)
		Map<String, SlsHistoricalDataEntity> incoming = new LinkedHashMap<>();
		List<String> errors = new ArrayList<>();

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheet("DATA");
			if (sheet == null) {
				sheet = workbook.getSheetAt(0);
			}

			Row headerRow = sheet.getRow(0);
			if (headerRow == null) {
				throw new IllegalArgumentException("The uploaded Excel file does not contain a header row.");
			}

			// Header name (normalised: upper-case, letters/digits only) -> column index
			Map<String, Integer> headerMap = new HashMap<>();
			for (Cell cell : headerRow) {
				String headerName = getStringValue(cell);
				if (headerName != null) {
					headerMap.put(headerName.toUpperCase().replaceAll("[^A-Z0-9]", ""), cell.getColumnIndex());
				}
			}

			// Month number column ("Month") and month-year text column ("JUNE 2023").
			// In the standard file the month-year column has a BLANK header, sitting
			// right after "Month", so fall back to that position.
			Integer monthNoCol = firstNonNull(headerMap.get("MONTH"), headerMap.get("MONTHNO"));
			Integer monthYearCol = firstNonNull(headerMap.get("MONTHYEAR"), headerMap.get("MONTHYR"),
					headerMap.get("PERIOD"));
			if (monthYearCol == null && monthNoCol != null && !headerMap.containsValue(monthNoCol + 1)) {
				monthYearCol = monthNoCol + 1;
			}

			List<String> missing = new ArrayList<>();
			if (monthYearCol == null) {
				missing.add("Month-Year (e.g. JUNE 2023)");
			}
			for (String h : REQUIRED_HEADERS) {
				if (!headerMap.containsKey(h)) {
					missing.add(h);
				}
			}
			if (!missing.isEmpty()) {
				throw new IllegalArgumentException("Invalid SLS file. Missing column(s): " + String.join(", ", missing));
			}

			int seq = 0;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null || isRowEmpty(row)) {
					continue;
				}
				int excelRow = i + 1;
				seq++;

				LocalDate monthDate = parseMonthYear(row.getCell(monthYearCol));
				if (monthDate == null || monthDate.getYear() < 2000 || monthDate.getYear() > 2100) {
					errors.add("Row " + excelRow + ": Month/Year is missing or not recognised.");
					continue;
				}

				int before = errors.size();
				SlsHistoricalDataEntity e = new SlsHistoricalDataEntity();

				BigDecimal monthNo = readDecimal(row, monthNoCol, "Month", excelRow, errors);
				e.setMonthNo(monthNo != null ? monthNo.intValue() : seq);

				e.setCa(toLong(readDecimal(row, headerMap.get("CA"), "CA", excelRow, errors), 15, "CA", excelRow, errors));
				e.setCall(toLong(readDecimal(row, headerMap.get("CALL"), "CALL", excelRow, errors), 15, "CALL", excelRow, errors));
				e.setSb(toLong(readDecimal(row, headerMap.get("SB"), "SB", excelRow, errors), 15, "SB", excelRow, errors));
				e.setFdr(toLong(readDecimal(row, headerMap.get("FDR"), "FDR", excelRow, errors), 15, "FDR", excelRow, errors));
				e.setTotalDep(toLong(readDecimal(row, headerMap.get("TOTALDEP"), "Total Dep", excelRow, errors), 15, "Total Dep", excelRow, errors));
				e.setBc(toLong(readDecimal(row, headerMap.get("BC"), "BC", excelRow, errors), 10, "BC", excelRow, errors));
				e.setOtherLiability(toLong(readDecimal(row, headerMap.get("OTHERLIABILITY"), "Other Liability", excelRow, errors), 15, "Other Liability", excelRow, errors));
				e.setOtherAsset(toLong(readDecimal(row, headerMap.get("OTHERASSET"), "Other Asset", excelRow, errors), 15, "Other Asset", excelRow, errors));
				e.setOd(toLong(readDecimal(row, headerMap.get("OD"), "OD", excelRow, errors), 15, "OD", excelRow, errors));
				e.setLoan(toLong(readDecimal(row, headerMap.get("LOAN"), "Loan", excelRow, errors), 15, "Loan", excelRow, errors));
				e.setTotalLoan(toLong(readDecimal(row, headerMap.get("TOTALLOAN"), "Total Loan", excelRow, errors), 15, "Total Loan", excelRow, errors));

				// CD_RATIO is NUMBER(5,2): round to 2 decimals, max 999.99
				BigDecimal cd = readDecimal(row, headerMap.get("CDRATIO"), "CD Ratio", excelRow, errors);
				if (cd != null) {
					cd = cd.setScale(2, RoundingMode.HALF_UP);
					if (cd.abs().compareTo(new BigDecimal("999.99")) > 0) {
						errors.add("Row " + excelRow + ": CD Ratio " + cd + " is too large.");
					}
				}
				e.setCdRatio(cd);

				if (monthNo != null && (monthNo.intValue() < 0 || monthNo.intValue() > 99)) {
					errors.add("Row " + excelRow + ": Month number " + monthNo.intValue() + " does not fit in 2 digits.");
				}

				if (errors.size() > before) {
					continue; // row had problems, already reported
				}

				LocalDate stored = STORE_MONTH_END ? YearMonth.from(monthDate).atEndOfMonth()
						: YearMonth.from(monthDate).atDay(1);
				e.setMonthYear(java.sql.Date.valueOf(stored));
				e.setDelFlg("N");

				String key = YearMonth.from(monthDate).toString(); // yyyy-MM
				if (incoming.put(key, e) != null) {
					logger.warn("SLS upload: month {} appears more than once in the file, last row wins.", key);
				}
			}
		}

		// 3. All-or-nothing: report every bad row, save nothing
		if (!errors.isEmpty()) {
			int shown = Math.min(errors.size(), 10);
			String msg = String.join("\n", errors.subList(0, shown));
			if (errors.size() > shown) {
				msg += "\n... and " + (errors.size() - shown) + " more error(s).";
			}
			throw new IllegalArgumentException("Upload rejected, no data was saved:\n" + msg);
		}
		if (incoming.isEmpty()) {
			throw new IllegalArgumentException("The uploaded SLS file contains no valid records to process.");
		}

		// 4. Upsert by year-month
		Map<String, SlsHistoricalDataEntity> existingByMonth = new HashMap<>();
		for (SlsHistoricalDataEntity ex : repository.findAll()) {
			if (ex.getMonthYear() != null) {
				existingByMonth.put(YearMonth.from(toLocalDate(ex.getMonthYear())).toString(), ex);
			}
		}

		int inserted = 0;
		int updated = 0;
		List<SlsHistoricalDataEntity> toSave = new ArrayList<>();
		for (Map.Entry<String, SlsHistoricalDataEntity> entry : incoming.entrySet()) {
			SlsHistoricalDataEntity src = entry.getValue();
			SlsHistoricalDataEntity target = existingByMonth.get(entry.getKey());
			if (target == null) {
				toSave.add(src);
				inserted++;
			} else {
				// keep the existing ID and MONTH_YEAR, refresh everything else
				copyValues(src, target);
				toSave.add(target);
				updated++;
			}
		}
		repository.saveAll(toSave);

		logger.info("SLS historical upload done: {} inserted, {} updated.", inserted, updated);
		return "SLS historical data uploaded successfully. Months processed: " + toSave.size() + " (new: " + inserted
				+ ", updated: " + updated + ")";
	}

	// --- Helpers ---

	private void copyValues(SlsHistoricalDataEntity s, SlsHistoricalDataEntity t) {
		t.setMonthNo(s.getMonthNo());
		t.setCa(s.getCa());
		t.setCall(s.getCall());
		t.setSb(s.getSb());
		t.setFdr(s.getFdr());
		t.setTotalDep(s.getTotalDep());
		t.setBc(s.getBc());
		t.setOtherLiability(s.getOtherLiability());
		t.setOtherAsset(s.getOtherAsset());
		t.setOd(s.getOd());
		t.setLoan(s.getLoan());
		t.setTotalLoan(s.getTotalLoan());
		t.setCdRatio(s.getCdRatio());
		t.setDelFlg("N");
	}

	@SafeVarargs
	private static <T> T firstNonNull(T... values) {
		for (T v : values) {
			if (v != null) {
				return v;
			}
		}
		return null;
	}

	private LocalDate toLocalDate(Date d) {
		return new java.sql.Date(d.getTime()).toLocalDate();
	}

	private String getStringValue(Cell cell) {
		if (cell == null) return null;
		String val = new DataFormatter().formatCellValue(cell).trim();
		return val.isEmpty() ? null : val;
	}

	/** Blank -> null. Non-numeric text is reported as a row error. */
	private BigDecimal readDecimal(Row row, Integer col, String label, int excelRow, List<String> errors) {
		if (col == null) return null;
		Cell cell = row.getCell(col);
		if (cell == null) return null;

		CellType type = cell.getCellTypeEnum();
		try {
			if (type == CellType.BLANK) {
				return null;
			}
			if (type == CellType.NUMERIC || type == CellType.FORMULA) {
				return BigDecimal.valueOf(cell.getNumericCellValue());
			}
			String s = getStringValue(cell);
			if (s == null) return null;
			return new BigDecimal(s.replace(",", ""));
		} catch (Exception ex) {
			errors.add("Row " + excelRow + ": '" + label + "' is not a valid number.");
			return null;
		}
	}

	/** Rounds to a whole number and checks it fits in NUMBER(maxDigits,0). */
	private Long toLong(BigDecimal value, int maxDigits, String label, int excelRow, List<String> errors) {
		if (value == null) return null;
		BigDecimal whole = value.setScale(0, RoundingMode.HALF_UP);
		if (whole.abs().precision() > maxDigits) {
			errors.add("Row " + excelRow + ": '" + label + "' value is too large (max " + maxDigits + " digits).");
			return null;
		}
		return whole.longValue();
	}

	/** Accepts a real date cell, or text such as "JUNE 2023" / "Jun 2023" / "06/2023". */
	private LocalDate parseMonthYear(Cell cell) {
		if (cell == null) return null;

		Date d = null;
		if (cell.getCellTypeEnum() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			d = cell.getDateCellValue();
		} else {
			String s = getStringValue(cell);
			if (s == null) return null;
			s = s.trim().replaceAll("\\s+", " ");
			for (String pattern : MONTH_YEAR_PATTERNS) {
				SimpleDateFormat parser = new SimpleDateFormat(pattern, Locale.ENGLISH);
				parser.setLenient(false);
				ParsePosition pos = new ParsePosition(0);
				Date parsed = parser.parse(s, pos);
				if (parsed != null && pos.getIndex() == s.length()) { // whole text must match
					// Defense in depth: a "yyyy" pattern can still match a 2-digit
					// year (e.g. "26" parsed literally as year 26 AD) if the string
					// happens to fit. Reject that case and keep trying patterns.
					if (pattern.contains("yyyy") && toLocalDate(parsed).getYear() < 1000) {
						continue;
					}
					d = parsed;
					break;
				}
			}
		}
		return d == null ? null : toLocalDate(d);
	}

	private boolean isRowEmpty(Row row) {
		if (row == null) return true;
		for (int c = row.getFirstCellNum(); c >= 0 && c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}
}
