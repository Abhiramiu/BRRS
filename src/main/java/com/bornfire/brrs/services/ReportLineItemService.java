package com.bornfire.brrs.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bornfire.brrs.dto.ReportLineItemDTO;

@Service
public class ReportLineItemService {

	@Value("${output.exportpathtemp}")
	private String baseExportPath;

	public List<ReportLineItemDTO> getReportData(String reportCode) throws Exception {

		System.out.println("Report code is :"+reportCode);
	    String filePathXlsx = baseExportPath + reportCode.toUpperCase() + ".xlsx";
	    String filePathXls  = baseExportPath + reportCode.toUpperCase() + ".xls";

	    File file = new File(filePathXlsx);
	    if (!file.exists())
	        file = new File(filePathXls);
	    if (!file.exists())
	        throw new Exception("File not found for report code " + reportCode);

	    List<ReportLineItemDTO> reportData = new ArrayList<>();

	    try (FileInputStream fis = new FileInputStream(file);
	         Workbook workbook = getWorkbook(fis, file.getName())) {

	        Sheet sheet = workbook.getSheetAt(0);

	        // Detect start + end using borders
	     // Detect start + end using borders
	        int startRowIndex = detectStartRow(sheet);
	        int endRowIndex   = detectEndRow(sheet);

	        // ----- MANUAL OVERRIDES BASED ON REPORT CODE -----
	     // ----- MANUAL OVERRIDES BASED ON REPORT CODE -----
	        switch (reportCode.toUpperCase()) {
                case "M_IS": startRowIndex = 9; endRowIndex = 34; break;
                case "M_SFINP1": startRowIndex = 9; endRowIndex = 60; break;
	            case "M_SFINP2": startRowIndex = 9; endRowIndex = 79; break;
				case "M_LIQ": startRowIndex = 9; endRowIndex = 35; break;
	            case "M_SCI": startRowIndex = 9; endRowIndex = 85; break;
				case "M_CA1": startRowIndex = 8; endRowIndex = 20; break;
	            case "M_CA2": startRowIndex = 9; endRowIndex = 48; break;
	            case "M_CA3": startRowIndex = 9; endRowIndex = 59; break;
	            case "M_CA4": startRowIndex = 9; endRowIndex = 57; break;
	            case "M_CA5": startRowIndex = 13; endRowIndex = 149; break;
	            case "M_CA6": startRowIndex = 11; endRowIndex = 46; break;
	            case "M_CA7": startRowIndex = 11; endRowIndex = 21; break;
	            case "M_SRWA12a": startRowIndex = 18; endRowIndex = 253; break;
	            case "M_SRWA12b": startRowIndex = 14; endRowIndex = 247; break;
	            case "M_SRWA12c": startRowIndex = 10; endRowIndex = 27; break;
	            case "M_SRWA12d": startRowIndex = 11; endRowIndex = 44; break;
	            case "M_SRWA12e": startRowIndex = 12; endRowIndex = 18; break;
	            case "M_SRWA12f": startRowIndex = 10; endRowIndex = 36; break;
	            case "M_SRWA12g": startRowIndex = 10; endRowIndex = 60; break;
	            case "M_SRWA12h": startRowIndex = 11; endRowIndex = 80; break;
	            case "M_OR1": startRowIndex = 9; endRowIndex = 55; break;
	            case "M_OR2": startRowIndex = 11; endRowIndex = 64; break;
	            case "M_MRC": startRowIndex = 8; endRowIndex = 35; break;
				case "M_SIR": startRowIndex = 11; endRowIndex = 32; break;
				case "M_GMIRT": startRowIndex = 8; endRowIndex = 11; break;
				case "M_IRB": startRowIndex = 9; endRowIndex = 105; break;
				case "M_EPR": startRowIndex = 10; endRowIndex = 22; break;
				case "M_FXR": startRowIndex = 9; endRowIndex = 28; break;
				case "M_CR": startRowIndex = 9; endRowIndex = 16; break;
	            case "M_OPTR": startRowIndex = 9; endRowIndex = 14; break;
				case "M_GALOR": startRowIndex = 9; endRowIndex = 113; break;
	            case "M_CALOC": startRowIndex = 10; endRowIndex = 115; break;
				case "M_LA1": startRowIndex = 10; endRowIndex = 63; break;
	            case "M_LA2": startRowIndex = 11; endRowIndex = 25; break;
	            case "M_LA3": startRowIndex = 10; endRowIndex = 41; break;
	            case "M_LA4": startRowIndex = 10; endRowIndex = 63; break;
	            case "M_LA5": startRowIndex = 6; endRowIndex = 61; break;
				case "M_PLL": startRowIndex = 10; endRowIndex = 63; break;
	            case "M_PD": startRowIndex = 7; endRowIndex = 60; break;
				case "M_I": startRowIndex = 10; endRowIndex = 69; break;
				case "M_SP": startRowIndex = 8; endRowIndex = 61; break;
				case "M_GP": startRowIndex = 10; endRowIndex = 63; break;
				case "M_TBS": startRowIndex = 11; endRowIndex = 53; break;
				case "M_LIQGAP": startRowIndex = 9; endRowIndex = 49; break;
				case "M_NOSVOS": startRowIndex = 10; endRowIndex = 219; break;
	            case "M_AIDP": startRowIndex = 10; endRowIndex = 193; break;
				case "M_DEP1": startRowIndex = 10; endRowIndex = 56; break;
	            case "M_DEP2": startRowIndex = 10; endRowIndex = 56; break;
	            case "M_DEP3": startRowIndex = 10; endRowIndex = 33; break;
				case "M_DEP4": startRowIndex = 9; endRowIndex = 299; break;
				case "M_OB": startRowIndex = 10; endRowIndex = 63; break;
	            case "M_BOP": startRowIndex = 12; endRowIndex = 35; break;
				case "M_INTRATES": startRowIndex = 10; endRowIndex = 41; break;
	            case "M_RATESFCA": startRowIndex = 9; endRowIndex = 14; break;
	            case "M_SECA": startRowIndex = 12; endRowIndex = 58; break;
	            case "M_SECL": startRowIndex = 12; endRowIndex = 56; break;
	            case "M_RPD": startRowIndex = 10; endRowIndex = 450; break;
	            case "M_FAS": startRowIndex = 9; endRowIndex = 27; break;
	            case "M_SEC": startRowIndex = 10; endRowIndex = 42; break;
	            case "M_UNCONS_INVEST": startRowIndex = 10; endRowIndex = 37; break;
	            case "Q_ATF": startRowIndex = 10; endRowIndex = 63; break;
	            case "Q_RLFA1": startRowIndex = 9; endRowIndex = 62; break;
	            case "Q_RLFA2": startRowIndex = 9; endRowIndex = 62; break;
	            case "Q_SMME_LOANS": startRowIndex = 14; endRowIndex = 43; break;
	            case "Q_SMME_INT": startRowIndex = 14; endRowIndex = 43; break;
				case "Q_SMME_DEP": startRowIndex = 10; endRowIndex = 46; break;
	            case "Q_STAFF": startRowIndex = 8; endRowIndex = 37; break;
	            case "Q_LARADV": startRowIndex = 9; endRowIndex = 300; break;
	            case "Q_BRANCHNET": startRowIndex = 9; endRowIndex = 64; break;
				case "M_PI": startRowIndex = 7; endRowIndex = 31; break;

	            default:
	                // Border detection remains
	                break;
	        }


	        System.out.println("START ROW = " + startRowIndex + " | END ROW = " + endRowIndex);


	        int srlNo = 1;

	     
	        int[] descColumns = Description(reportCode.toUpperCase());
	        int descColIndex = (descColumns != null && descColumns.length > 0) ? descColumns[0] : 0;


	        for (int r = startRowIndex; r <= endRowIndex; r++) {

	            Row row = sheet.getRow(r);
	            if (row == null) continue;

	            Cell descCell = row.getCell(descColIndex);
	            if (descCell == null) continue;

	            String desc = getCellValueAsString(descCell, workbook);
	            if (desc == null || desc.trim().isEmpty()) continue;

	            boolean isHeader = isFormulaHeaderRow(row, sheet, reportCode);


	            ReportLineItemDTO dto = new ReportLineItemDTO();
	            dto.setSrlNo(srlNo);
	            dto.setFieldDescription(desc.trim());
	            dto.setReportLabel(String.format("R%d", r));
	            dto.setHeader(isHeader ? "Y" : " ");
	            dto.setRemarks("");

	            reportData.add(dto);
	            srlNo++;
	        }

	    } catch (Exception e) {
	        throw new Exception("Failed to read Excel for " + reportCode, e);
	    }

	    return reportData;
	}

	//////////////////////////////////////////////////////////////
//	                 HEADER LOGIC (NOT CHANGED)
	//////////////////////////////////////////////////////////////

	private int[] getFormulaColumnsByReportCode(String reportCode) {

	    switch (reportCode.toUpperCase()) {

	        case "M_PI": return new int[]{6};
	        case "M-SFINP1": return new int[]{2,3};
	        case "M-SFINP2": return new int[]{2,3};
	        case "M_LIQ": return new int[]{4};
	        case "M_SCI & E": return new int[]{2,3};
	        case "M_IS": return new int[]{6,8};
	        case "M_CA1": return new int[]{3};
	        case "M_CA2": return new int[]{3};
	        case "M_CA3": return new int[]{2,3};
	        case "M_CA4": return new int[]{7};
	        case "M_CA5": return new int[]{9,10,11};
	        case "M_CA6": return new int[]{5};
	        case "M_CA7": return new int[]{3};
	        case "M-SRWA 12A": 
	            return new int[]{2,4,5,6,7,8,9,10,11,12,13,14,15,16,18,20,21,22,23,24,25,26,27,28};
	        case "M-SRWA 12B":
	            return new int[]{2,4,5,6,7,8,9,10,11,13,15,17,18,19,20,21,22,23,24,25,26,27,28,29};
	        case "M-SRWA 12C": return new int[]{6};
	        case "M-SRWA 12D": return new int[]{4,7,8,9,10,15,18,19,21};
	        case "M-SRWA 12E": return new int[]{5};
	        case "M-SRWA 12F": return new int[]{6};
	        case "M-SRWA 12G": return new int[]{6};
	        case "M-SRWA 12H": return new int[]{4,5,6};
	        case "M-OR1": return new int[]{3,4,5};
	        case "M-OR2": return new int[]{3,4,5,6,7,8,9,10,11,12};
	        case "M-MRC": return new int[]{2};
	        case "M-MIR": return new int[]{}; // no formula column
	        case "M-IRB": return new int[]{12};
	        case "M-GALOR": return new int[]{17};
	        case "M-CALOC": return new int[]{22};
	        case "M-I (S&CA)": return new int[]{4};
	        case "M-SP": return new int[]{3};
	        case "M-DEP1": return new int[]{14};
	        case "M-NOSVOS": return new int[]{9,15,16};
	        case "M-AIDP": return new int[]{6,7};
	        case "M-SIR": return new int[]{2,4,5,7,8,10};
	        case "M-GMIRT": return new int[]{13};
	        case "M-EPR": return new int[]{4,7,10,16};
	        case "M-LIQ2": return new int[]{6};
	        case "M-LIQGAP": return expand(1,7);   // 1 to 7
	        case "M-CR": return new int[]{1,2,3,5,6,8,9};
	        case "M_FXR": return new int[]{8,9,10};
	        case "M-OPTR": return new int[]{4,5,6,7,8};
	        case "M-LA1": return new int[]{1,2,3};
	        case "M-LA2": return new int[]{1};
	        case "M-LA3": return new int[]{1,2,3};
	        case "M-LA4": return expand(1,5);      // 1 to 5
	        case "M-LA5": return expand(1,9);      // 1 to 9
	        case "M-PLL": return new int[]{1};
	        case "M-PD": return new int[]{14,15,16,18,19,20};
	        case "M-GP": return new int[]{1,2,3};
			case "M-TBS": return new int[]{2,3,4,5,6};
	        case "M-DEP2": return expand(2,6);
	        case "M-DEP3": return new int[]{1,2,3,8,9,17,18,19};
	        case "M-OB": return new int[]{1};
	        case "M-BOP": return new int[]{7};
	        case "M-INTRATES": return new int[]{1,2,3};
	        case "M-INT .RATES(FCA)": return new int[]{}; 
	        case "M-SECA": return new int[]{10};
	        case "M-SECL": return new int[]{10};
	        case "M-RPD": return new int[]{3,4,5,7,9};
	        case "M-FAS": return new int[]{1,4,5,6};
	        case "M-SEC": return expand(1,13);      // 1 to 13
	        case "M-UNCONS INVEST": return new int[]{3,4,5,6};
	        case "Q-ATF": return new int[]{1,2,3,4};
	        case "Q-RLFA1": return new int[]{1,2,3};
	        case "Q-RLFA2": return new int[]{1,2,3};
	        case "Q-SMME(LOAN & ADVANCE)": return new int[]{2,3};
	        case "Q-SMME DEP": return new int[]{13,14};
	        case "Q-SMME (INTREST & INCOME)": return new int[]{1,2};
	        case "Q-STAFF": return new int[]{1,2,3};
	        case "Q-LARADV": return new int[]{4,5,12,13};
	        case "Q-BRANCHNET": return new int[]{2,3,4,5};

	        default:
	            return new int[]{}; // no formula override
	    }
	}

	private int[] expand(int start, int end) {
	    return java.util.stream.IntStream.rangeClosed(start, end).toArray();
	}



	private boolean isFormulaHeaderRow(Row row, Sheet sheet, String reportCode) {

	    int[] formulaColumns = getFormulaColumnsByReportCode(reportCode);

	    // If array is NOT empty → check only those columns
	    if (formulaColumns.length > 0) {

	        for (int col : formulaColumns) {

	            Cell cell = row.getCell(col);
	            if (cell == null) continue;

	            if (cell.getCellType() == CellType.FORMULA) {

	                boolean formulaRepeatsBelow = true;

	                for (int r = row.getRowNum() + 1;
	                     r <= row.getRowNum() + 2 && r <= sheet.getLastRowNum();
	                     r++) {

	                    Row nextRow = sheet.getRow(r);
	                    if (nextRow == null) continue;

	                    Cell nextCell = nextRow.getCell(col);

	                    if (nextCell == null || nextCell.getCellType() != CellType.FORMULA) {
	                        formulaRepeatsBelow = false;
	                        break;
	                    }
	                }

	                if (!formulaRepeatsBelow) {
	                    return true; // Header
	                }
	            }
	        }

	        return false; // No formula in defined columns
	    }

	    // -----------------------------------
	    // FALLBACK → old logic (check all columns)
	    // -----------------------------------
	    short lastCol = row.getLastCellNum();

	    for (int c = 0; c < lastCol; c++) {
	        Cell cell = row.getCell(c);
	        if (cell == null) continue;

	        if (cell.getCellType() == CellType.FORMULA) {

	            boolean formulaRepeatsBelow = true;

	            for (int r = row.getRowNum() + 1;
	                 r <= row.getRowNum() + 2 && r <= sheet.getLastRowNum();
	                 r++) {

	                Row nextRow = sheet.getRow(r);
	                if (nextRow == null) continue;

	                Cell nextCell = nextRow.getCell(c);

	                if (nextCell == null || nextCell.getCellType() != CellType.FORMULA) {
	                    formulaRepeatsBelow = false;
	                    break;
	                }
	            }

	            if (!formulaRepeatsBelow)
	                return true;
	        }
	    }

	    return false;
	}


	//////////////////////////////////////////////////////////////
//	                   BORDER CHECK LOGIC
	//////////////////////////////////////////////////////////////

	private boolean hasBorder(Cell cell) {
	    CellStyle style = cell.getCellStyle();
	    if (style == null) return false;

	    return style.getBorderTop()    != BorderStyle.NONE ||
	           style.getBorderBottom() != BorderStyle.NONE ||
	           style.getBorderLeft()   != BorderStyle.NONE ||
	           style.getBorderRight()  != BorderStyle.NONE;
	}

	//////////////////////////////////////////////////////////////
//	      Detect Start Row (first row with border in DESC col)
	//////////////////////////////////////////////////////////////

	private int detectStartRow(Sheet sheet) {
	    int descColIndex = 0; // Column A

	    for (int r = 0; r <= sheet.getLastRowNum(); r++) {
	        Row row = sheet.getRow(r);
	        if (row == null) continue;

	        Cell cell = row.getCell(descColIndex);
	        if (cell == null) continue;

	        if (hasBorder(cell)) {
	            return r;
	        }
	    }
	    return 0;
	}

	//////////////////////////////////////////////////////////////
//	      Detect End Row (last row with border in DESC col)
	//////////////////////////////////////////////////////////////

	private int detectEndRow(Sheet sheet) {
	    int descColIndex = 0; // Column A

	    for (int r = sheet.getLastRowNum(); r >= 0; r--) {
	        Row row = sheet.getRow(r);
	        if (row == null) continue;

	        Cell cell = row.getCell(descColIndex);
	        if (cell == null) continue;

	        if (hasBorder(cell)) {
	            return r;
	        }
	    }
	    return sheet.getLastRowNum();
	}

	//////////////////////////////////////////////////////////////
//	                   HELPER METHODS
	//////////////////////////////////////////////////////////////

	private Workbook getWorkbook(FileInputStream fis, String fileName) throws Exception {
	    if (fileName.toLowerCase().endsWith(".xlsx"))
	        return new XSSFWorkbook(fis);
	    else if (fileName.toLowerCase().endsWith(".xls"))
	        return new HSSFWorkbook(fis);
	    else
	        throw new Exception("Unsupported file: " + fileName);
	}

	private String getCellValueAsString(Cell cell, Workbook workbook) {
	    if (cell == null)
	        return "";
	    DataFormatter df = new DataFormatter();
	    try {
	        return df.formatCellValue(cell, workbook.getCreationHelper().createFormulaEvaluator()).trim();
	    } catch (Exception e) {
	        return df.formatCellValue(cell).trim();
	    }
	}
	

private int[] Description(String reportCode) {

	    switch (reportCode.toUpperCase()) {

	        case "M_PI": return new int[] {0};
	        case "M-SFINP1": return new int[] {0};
	        case "M-SFINP2": return new int[] {0};
	        case "M_LIQ": return new int[] {0};
	        case "M_SCI": return new int[] {0};
	        case "M_IS": return new int[] {0};
	        case "M_CA1": return new int[] {1};
	        case "M_CA2": return new int[] {1};
	        case "M_CA3": return new int[] {1};
	        case "M_CA4": return new int[] {0};
	        case "M_CA5": return new int[] {1};
	        case "M_CA6": return new int[] {1};
	        case "M_CA7": return new int[] {1};
	        case "M_SRWA12A": return new int[]{1};
			case "M_SRWA12B":return new int[]{1};
	        case "M_SRWA12C": return new int[]{2};
	        case "M_SRWA12D": return new int[]{3,14};
	        case "M_SRWA12E": return new int[]{1};
	        case "M_SRWA12F": return new int[] {1};
	        case "M_SRWA12G": return new int[] {1};
	        case "M_SRWA12H": return new int[]{1};
	        case "M_OR1": return new int[]{1};
	        case "M_OR2": return new int[]{1};
	        case "M_MRC": return new int[]{1};
	        case "M_SIR": return new int[]{1};
	        case "M_IRB": return new int[]{1};
	        case "M_GALOR": return new int[]{0};
	        case "M_CALOC": return new int[]{0};
	        case "M_I": return new int[]{0};
	        case "M_SP": return new int[]{0};
	        case "M_NOSVOS": return new int[] {1};
	        case "M_AIDP": return new int[]{0};
	        case "M_GMIRT": return new int[]{1};
	        case "M_EPR": return new int[]{1};
	        case "M_TBS": return new int[]{1};
	        case "M_LIQGAP": return new int[]{0};
	        case "M_CR": return new int[]{0};
	        case "M_FXR": return new int[]{1};
	        case "M_OPTR": return new int[] {1,2,3};
	        
	        
	        case "M_LA1": return new int[]{0};
	        case "M_LA2": return new int[]{0};
	        case "M_LA3": return new int[]{0};
	        case "M_LA4": return new int[]{0};
	        case "M_LA5": return new int[]{0};
	        case "M_PLL": return new int[]{0};
	        case "M_PD": return new int[]{0};
	        case "M_GP": return new int[]{0};
	        case "M_DEP1": return new int[]{0};
	        case "M_DEP2":  return new int[]{0};
	        case "M_DEP3":  return new int[]{0};
	        case "M_DEP4":  return new int[]{1};
	        case "M_OB":  return new int[]{0};
	        case "M_BOP":  return new int[]{0};
	        case "M_INTRATES":  return new int[]{0};
	        case "M_RATESFCA": return new int[]{0};
	        case "M_SECA":  return new int[]{0};
	        case "M_SECL":  return new int[]{0};
	        case "M_RPD":  return new int[]{0};
	        case "M_FAS":  return new int[]{0};
	        case "M_SEC":  return new int[]{0};
	        case "M_UNCONS_INVEST":  return new int[0];
	        case "Q_ATF":  return new int[0];
	        case "Q_RLFA1":  return new int[0];
	        case "Q_RLFA2":  return new int[0];
	        case "Q_SMME_LOANS":  return new int[]{0};
	        case "Q_SMME_DEP":  return new int[]{0};
	        case "Q_SMME_INT":  return new int[]{0};
	        case "Q_STAFF":  return new int[]{0};
	        case "Q_LARADV":  return new int[]{0};
	        case "Q_BRANCHNET":  return new int[]{1};

	  
	       
	        default:
	            return new int[]{}; // no formula override
	    }
	    
	    
	}
}
