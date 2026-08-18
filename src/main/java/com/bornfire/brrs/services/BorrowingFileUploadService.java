package com.bornfire.brrs.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.brrs.entities.BorrowingFileUploadEntity;
import com.bornfire.brrs.entities.BorrowingFileUploadRepository;
import com.bornfire.brrs.entities.PlacementFileUploadEntity;
import com.bornfire.brrs.entities.PlacementFileUploadRepository;

@Service
public class BorrowingFileUploadService {

	@Autowired
	private BorrowingFileUploadRepository borrowingRepository;

	@Autowired
	private PlacementFileUploadRepository placementRepository;

	@Transactional
	public String uploadBorrowingFile(MultipartFile file, String asOnDateStr, String category) throws Exception {
		String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";

		// 1. File Name Validation against Category
		if ("Borrowing".equalsIgnoreCase(category) && !fileName.contains("borrowing")) {
			throw new IllegalArgumentException(
					"Invalid file! Category is 'Borrowing', but file name doesn't contain 'borrowing'.");
		}
		if ("Placement".equalsIgnoreCase(category) && !fileName.contains("placement")) {
			throw new IllegalArgumentException(
					"Invalid file! Category is 'Placement', but file name doesn't contain 'placement'.");
		}

		// 2. Parse Execution Date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date asOnDate = sdf.parse(asOnDateStr);

		// 3. Delete Existing Records based on Category
		if ("Placement".equalsIgnoreCase(category)) {
			placementRepository.deleteByAsOnDateAndCategory(asOnDate, category);
		} else {
			borrowingRepository.deleteByAsOnDateAndCategory(asOnDate, category);
		}

		List<BorrowingFileUploadEntity> borrowingList = new ArrayList<>();
		List<PlacementFileUploadEntity> placementList = new ArrayList<>();

		// 4. Read Excel Sheet
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			// Read Header Row
			Row headerRow = sheet.getRow(0);
			if (headerRow == null) {
				throw new IllegalArgumentException("The uploaded Excel file does not contain a header row.");
			}

			Map<String, Integer> headerMap = new HashMap<>();
			for (Cell cell : headerRow) {
				String headerName = getStringValue(cell);
				if (headerName != null) {
					String normalizedKey = headerName.toUpperCase().replaceAll("[^A-Z0-9]", "");
					headerMap.put(normalizedKey, cell.getColumnIndex());
				}
			}

			// Read Data Rows
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null || isRowEmpty(row))
					continue;

				String dealNo = getStringValue(getCellByHeader(row, headerMap, "DEALNO", "DEALNUMBER", "DEAL"));
				if (dealNo == null || dealNo.trim().isEmpty()) {
					continue; // Skip rows without a Deal No
				}

				if ("Placement".equalsIgnoreCase(category)) {
					PlacementFileUploadEntity entity = new PlacementFileUploadEntity();
					entity.setDealNo(dealNo);
					entity.setAsOnDate(asOnDate);
					entity.setCategory(category);

					entity.setDt(getDateValue(getCellByHeader(row, headerMap, "DT", "DEALDATE", "DEALDT", "DATE")));
					entity.setBank(getStringValue(getCellByHeader(row, headerMap, "BANK", "BANKNAME")));
					entity.setCurrencyPurchased(getStringValue(getCellByHeader(row, headerMap, "CURRENCYPURCHASED", "CURRPURCHASED")));
					entity.setAmountPurchased(getBigDecimalValue(getCellByHeader(row, headerMap, "AMOUNTPURCHASED", "AMTPURCHASED")));
					entity.setAmountInBwp(getBigDecimalValue(getCellByHeader(row, headerMap, "AMOUNTINBWP", "AMTBWP")));
					entity.setRate(getBigDecimalValue(getCellByHeader(row, headerMap, "RATE")));
					entity.setDays(getIntegerValue(getCellByHeader(row, headerMap, "DAYS", "NODAYS")));
					entity.setCurrencySold(getStringValue(getCellByHeader(row, headerMap, "CURRENCYSOLD", "CURRSOLD")));
					entity.setIntAmt(getBigDecimalValue(getCellByHeader(row, headerMap, "INTAMT", "INTERESTAMOUNT", "INTAMOUNT")));
					entity.setPayableAmt(getBigDecimalValue(getCellByHeader(row, headerMap, "PAYABLEAMT", "PAYABLEAMOUNT")));
					entity.setMatDt(getDateValue(getCellByHeader(row, headerMap, "MATDT", "MATDATE", "MATURITYDATE", "MATURITYDT")));
					entity.setRefDate(getDateValue(getCellByHeader(row, headerMap, "REFDATE", "REFERENCEDATE", "REFDT")));
					entity.setResidualPeriod(getIntegerValue(getCellByHeader(row, headerMap, "RESIDUALPERIOD")));

					placementList.add(entity);
				} else {
					BorrowingFileUploadEntity entity = new BorrowingFileUploadEntity();
					entity.setDealNo(dealNo);
					entity.setAsOnDate(asOnDate);
					entity.setCategory(category);

					entity.setDealDate(getDateValue(getCellByHeader(row, headerMap, "DEALDATE", "DEALDT", "DT", "DATE")));
					entity.setBank(getStringValue(getCellByHeader(row, headerMap, "BANK", "BANKNAME")));
					entity.setCurrencyPurchased(getStringValue(getCellByHeader(row, headerMap, "CURRENCYPURCHASED", "CURRPURCHASED")));
					entity.setAmountPurchased(getBigDecimalValue(getCellByHeader(row, headerMap, "AMOUNTPURCHASED", "AMTPURCHASED")));
					entity.setAmountInBwp(getBigDecimalValue(getCellByHeader(row, headerMap, "AMOUNTINBWP", "AMTBWP")));
					entity.setRate(getBigDecimalValue(getCellByHeader(row, headerMap, "RATE")));
					entity.setDays(getIntegerValue(getCellByHeader(row, headerMap, "DAYS", "NODAYS")));
					entity.setCurrencySold(getStringValue(getCellByHeader(row, headerMap, "CURRENCYSOLD", "CURRSOLD")));
					entity.setIntAmt(getBigDecimalValue(getCellByHeader(row, headerMap, "INTAMT", "INTERESTAMOUNT", "INTAMOUNT")));
					entity.setPayableAmt(getBigDecimalValue(getCellByHeader(row, headerMap, "PAYABLEAMT", "PAYABLEAMOUNT")));
					entity.setMatDt(getDateValue(getCellByHeader(row, headerMap, "MATDT", "MATDATE", "MATURITYDATE", "MATURITYDT")));
					entity.setRefDate(getDateValue(getCellByHeader(row, headerMap, "REFDATE", "REFERENCEDATE", "REFDT")));
					entity.setResidualPeriod(getIntegerValue(getCellByHeader(row, headerMap, "RESIDUALPERIOD")));

					borrowingList.add(entity);
				}
			}
		}

		// 5. Batch Save according to Category
		if ("Placement".equalsIgnoreCase(category)) {
			if (placementList.isEmpty()) {
				throw new IllegalArgumentException("The uploaded placement file contains no valid records to process.");
			}
			placementRepository.saveAll(placementList);
			return "Placement file uploaded successfully. Total records processed: " + placementList.size();
		} else {
			if (borrowingList.isEmpty()) {
				throw new IllegalArgumentException("The uploaded borrowing file contains no valid records to process.");
			}
			borrowingRepository.saveAll(borrowingList);
			return "Borrowing file uploaded successfully. Total records processed: " + borrowingList.size();
		}
	}

	// --- Helpers ---
	private Cell getCellByHeader(Row row, Map<String, Integer> headerMap, String... possibleKeys) {
		for (String key : possibleKeys) {
			Integer colIndex = headerMap.get(key);
			if (colIndex != null) {
				return row.getCell(colIndex);
			}
		}
		return null;
	}

	private String getStringValue(Cell cell) {
		if (cell == null) return null;
		DataFormatter formatter = new DataFormatter();
		String val = formatter.formatCellValue(cell).trim();
		return val.isEmpty() ? null : val;
	}

	private BigDecimal getBigDecimalValue(Cell cell) {
		if (cell == null) return null;

		CellType cellType = cell.getCellTypeEnum();

		if (cellType == CellType.BLANK) {
			return null;
		} else if (cellType == CellType.NUMERIC) {
			return BigDecimal.valueOf(cell.getNumericCellValue());
		} else if (cellType == CellType.STRING) {
			try {
				String strVal = cell.getStringCellValue().trim().replaceAll(",", "");
				return strVal.isEmpty() ? null : new BigDecimal(strVal);
			} catch (NumberFormatException e) {
				return null;
			}
		} else if (cellType == CellType.FORMULA) {
			try {
				return BigDecimal.valueOf(cell.getNumericCellValue());
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	private Integer getIntegerValue(Cell cell) {
		BigDecimal val = getBigDecimalValue(cell);
		return val != null ? val.intValue() : null;
	}

	private Date getDateValue(Cell cell) {
		if (cell == null) return null;

		CellType cellType = cell.getCellTypeEnum();

		if (cellType == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			return cell.getDateCellValue();
		}

		String dateStr = getStringValue(cell);
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return null;
		}

		String[] datePatterns = {
			"dd-MMM-yyyy", "d-MMM-yyyy", 
			"dd/MM/yyyy", "d/M/yyyy", 
			"yyyy-MM-dd", "dd-MM-yyyy",
			"MMM dd, yyyy"
		};

		for (String pattern : datePatterns) {
			try {
				SimpleDateFormat parser = new SimpleDateFormat(pattern, Locale.ENGLISH);
				parser.setLenient(false);
				return parser.parse(dateStr.trim());
			} catch (Exception ignored) {
			}
		}

		return null;
	}

	private boolean isRowEmpty(Row row) {
		if (row == null) return true;
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}
}