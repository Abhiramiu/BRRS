package com.bornfire.brrs.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

@Component
@Service
public class Exceltopdfservice {

   public byte[] convertExcelBytesToPdf(byte[] excelBytes) throws Exception {
    try (
            InputStream inputStream = new ByteArrayInputStream(excelBytes);
            Workbook workbook = WorkbookFactory.create(inputStream);
            ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()
    ) {

        Sheet sheet = workbook.getSheetAt(0);

        // A4 Landscape PDF
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter.getInstance(document, pdfOut);
        document.open();

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        DataFormatter formatter = new DataFormatter();

        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        Set<String> mergedCells = new HashSet<>();

        // Identify only used columns (skip empty columns)
        Set<Integer> usedColumns = new LinkedHashSet<>();

        for (Row row : sheet) {
            if (row == null)
                continue;

            for (Cell cell : row) {
                if (cell == null)
                    continue;

                String val = formatter.formatCellValue(cell, evaluator);

                if (val != null && !val.trim().isEmpty()) {
                    usedColumns.add(cell.getColumnIndex());
                }
            }
        }

        if (usedColumns.isEmpty()) {
            throw new IllegalArgumentException("Excel sheet has no visible data.");
        }

        List<Integer> colIndexes = new ArrayList<>(usedColumns);
        Collections.sort(colIndexes);

        PdfPTable table = new PdfPTable(colIndexes.size());
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSplitLate(false);
        table.setSplitRows(true);

        DecimalFormat df = new DecimalFormat("#.####");

        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

            Row row = sheet.getRow(rowIndex);

            if (row == null)
                continue;

            // Skip fully empty rows
            boolean rowEmpty = true;

            for (int colIndex : colIndexes) {
                Cell cell = row.getCell(colIndex);

                if (cell != null &&
                        !formatter.formatCellValue(cell, evaluator).trim().isEmpty()) {
                    rowEmpty = false;
                    break;
                }
            }

            if (rowEmpty)
                continue;

            for (int colIndex : colIndexes) {

                String cellKey = rowIndex + "-" + colIndex;

                if (mergedCells.contains(cellKey))
                    continue;

                Cell cell = row.getCell(colIndex);
                String cellValue = "";
                org.apache.poi.ss.usermodel.CellStyle style = null;

                if (cell != null) {
                    style = cell.getCellStyle();

                    if (cell.getCellTypeEnum() == CellType.FORMULA) {

                        CellValue eval = evaluator.evaluate(cell);

                        if (eval != null) {
                            switch (eval.getCellTypeEnum()) {

                                case STRING:
                                    cellValue = eval.getStringValue();
                                    break;

                                case NUMERIC:
                                    double num = eval.getNumberValue();
                                    cellValue = (num == Math.floor(num))
                                            ? String.valueOf((long) num)
                                            : df.format(num);
                                    break;

                                case BOOLEAN:
                                    cellValue = String.valueOf(eval.getBooleanValue());
                                    break;

                                default:
                                    cellValue = "";
                            }
                        }

                    } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {

                        double num = cell.getNumericCellValue();

                        cellValue = (num == Math.floor(num))
                                ? String.valueOf((long) num)
                                : df.format(num);

                    } else {
                        cellValue = formatter.formatCellValue(cell, evaluator);
                    }
                }

                /*
                 * FONT HANDLING
                 * Use Helvetica safely for PDF rendering
                 */
                Font cellFont;

                if (cell != null && style != null) {

                    short fontIdx = style.getFontIndex();
                    org.apache.poi.ss.usermodel.Font excelFont = workbook.getFontAt(fontIdx);

                    boolean isBold = excelFont.getBold();
                    boolean isItalic = excelFont.getItalic();

                    int styleInt = Font.NORMAL;

                    if (isBold)
                        styleInt |= Font.BOLD;

                    if (isItalic)
                        styleInt |= Font.ITALIC;

                    cellFont = FontFactory.getFont(
                            FontFactory.HELVETICA,
                            9f,
                            styleInt
                    );

                } else {
                    cellFont = FontFactory.getFont(
                            FontFactory.HELVETICA,
                            9f,
                            Font.NORMAL
                    );
                }

                Phrase phrase = new Phrase(cellValue, cellFont);
                PdfPCell pdfCell = new PdfPCell(phrase);

                /*
                 * HANDLE MERGED CELLS
                 */
                for (CellRangeAddress range : mergedRegions) {

                    if (range.isInRange(rowIndex, colIndex)) {

                        int rowspan = range.getLastRow() - range.getFirstRow() + 1;
                        int colspan = range.getLastColumn() - range.getFirstColumn() + 1;

                        if (rowIndex == range.getFirstRow()
                                && colIndex == range.getFirstColumn()) {

                            if (rowspan > 1)
                                pdfCell.setRowspan(rowspan);

                            if (colspan > 1)
                                pdfCell.setColspan(colspan);

                        } else {
                            mergedCells.add(cellKey);
                            pdfCell = null;
                        }

                        break;
                    }
                }

                if (pdfCell == null)
                    continue;

                /*
                 * BACKGROUND COLOR
                 */
                BaseColor bgColor = null;

                if (cell != null && style != null) {

                    org.apache.poi.ss.usermodel.Color poiColor =
                            style.getFillForegroundColorColor();

                    if (poiColor == null) {
                        poiColor = style.getFillBackgroundColorColor();
                    }

                    if (poiColor instanceof XSSFColor) {

                        XSSFColor xssfColor = (XSSFColor) poiColor;
                        byte[] rgb = xssfColor.getRGBWithTint();

                        if (rgb != null &&
                                (rgb[0] != 0 || rgb[1] != 0 || rgb[2] != 0)) {

                            bgColor = new BaseColor(
                                    rgb[0] & 0xFF,
                                    rgb[1] & 0xFF,
                                    rgb[2] & 0xFF
                            );
                        }

                    } else if (workbook instanceof HSSFWorkbook) {

                        HSSFWorkbook hwb = (HSSFWorkbook) workbook;

                        short idx = style.getFillForegroundColor();

                        if (idx == 0)
                            idx = style.getFillBackgroundColor();

                        if (idx > 0) {

                            HSSFPalette palette = hwb.getCustomPalette();
                            HSSFColor color = palette.getColor(idx);

                            if (color != null && color.getTriplet() != null) {

                                short[] rgb = color.getTriplet();

                                if (!(rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0)) {
                                    bgColor = new BaseColor(
                                            rgb[0],
                                            rgb[1],
                                            rgb[2]
                                    );
                                }
                            }
                        }
                    }
                }

                if (bgColor != null) {
                    pdfCell.setBackgroundColor(bgColor);
                } else {
                    pdfCell.setBackgroundColor(BaseColor.WHITE);
                }

                /*
                 * BORDERS
                 */
                if (style != null) {

                    boolean top = style.getBorderTopEnum() != BorderStyle.NONE;
                    boolean bottom = style.getBorderBottomEnum() != BorderStyle.NONE;
                    boolean left = style.getBorderLeftEnum() != BorderStyle.NONE;
                    boolean right = style.getBorderRightEnum() != BorderStyle.NONE;

                    int border = 0;

                    if (top)
                        border |= Rectangle.TOP;

                    if (bottom)
                        border |= Rectangle.BOTTOM;

                    if (left)
                        border |= Rectangle.LEFT;

                    if (right)
                        border |= Rectangle.RIGHT;

                    pdfCell.setBorder(
                            border == 0
                                    ? Rectangle.NO_BORDER
                                    : border
                    );

                } else {
                    pdfCell.setBorder(Rectangle.NO_BORDER);
                }

                /*
                 * ALIGNMENT
                 */
                if (style != null) {

                    switch (style.getAlignmentEnum()) {

                        case LEFT:
                            pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            break;

                        case RIGHT:
                            pdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            break;

                        case CENTER:
                            pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            break;

                        default:
                            pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    }

                    switch (style.getVerticalAlignmentEnum()) {

                        case TOP:
                            pdfCell.setVerticalAlignment(Element.ALIGN_TOP);
                            break;

                        case CENTER:
                            pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            break;

                        case BOTTOM:
                            pdfCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                            break;

                        default:
                            pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    }

                } else {
                    pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                }

                /*
                 * PROFESSIONAL CELL SETTINGS
                 */
                pdfCell.setPadding(4f);
                pdfCell.setMinimumHeight(20f);
                pdfCell.setNoWrap(false);

                table.addCell(pdfCell);
            }
        }

        document.add(table);
        document.close();

        return pdfOut.toByteArray();
    }
}
   
   
   
   public byte[] convertExcelBytesToPdf(byte[] excelBytes, List<int[]> tableRowRanges) throws Exception {
	     try (
	         InputStream inputStream = new ByteArrayInputStream(excelBytes);
	         Workbook workbook = WorkbookFactory.create(inputStream);
	         ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()
	     ) {
	         Sheet sheet = workbook.getSheetAt(0);

	         Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
	         PdfWriter.getInstance(document, pdfOut);
	         document.open();

	         FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	         evaluator.setIgnoreMissingWorkbooks(true);
	         DataFormatter formatter = new DataFormatter();
	         List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
	         DecimalFormat df = new DecimalFormat("#,##0.###");

	         boolean firstTable = true;

	         for (int[] range : tableRowRanges) {
	             int startRow = range[0];
	             int endRow   = Math.min(range[1], sheet.getLastRowNum());

	             // ── Per-range column detection ────────────────────────────────────
	             // Scan only rows within this range so Table 1 (cols A–G) is not
	             // forced into a 9-column table just because Table 2 uses cols H–I.
	             int minCol = Integer.MAX_VALUE;
	             int maxCol = Integer.MIN_VALUE;
	             for (int ri = startRow; ri <= endRow; ri++) {
	                 Row row = sheet.getRow(ri);
	                 if (row == null) continue;
	                 for (Cell cell : row) {
	                     if (cell == null) continue;
	                     int ci = cell.getColumnIndex();
	                     if (ci < minCol) minCol = ci;
	                     if (ci > maxCol) maxCol = ci;
	                 }
	             }
	             if (minCol == Integer.MAX_VALUE) continue; // range is entirely empty — skip

	             List<Integer> colIndexes = new ArrayList<>();
	             for (int c = minCol; c <= maxCol; c++) colIndexes.add(c);

	             // ── Proportional column widths from Excel ─────────────────────────
	             float[] colWidths = new float[colIndexes.size()];
	             float totalWidth = 0f;
	             for (int i = 0; i < colIndexes.size(); i++) {
	                 int raw = sheet.getColumnWidth(colIndexes.get(i));
	                 if (raw == 0) raw = sheet.getDefaultColumnWidth() * 256;
	                 colWidths[i] = raw;
	                 totalWidth  += raw;
	             }
	             for (int i = 0; i < colWidths.length; i++)
	                 colWidths[i] = (colWidths[i] / totalWidth) * 100f;

	             PdfPTable table = new PdfPTable(colIndexes.size());
	             table.setWidths(colWidths);
	             table.setWidthPercentage(100);
	             table.setSpacingBefore(firstTable ? 10f : 18f); // gap between the two tables
	             table.setSplitLate(false);
	             table.setSplitRows(true);

	             // mergedCells tracks consumed non-origin cells; reset per range
	             // so absolute row-col keys from Table 1 don't linger into Table 2.
	             Set<String> mergedCells = new HashSet<>();

	             for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
	                 Row row = sheet.getRow(rowIndex);

	                 float rowHeightPt = 15f;
	                 if (row != null && row.getHeight() > 0)
	                     rowHeightPt = row.getHeightInPoints();

	                 // Skip null or fully-empty rows
	                 if (row != null) {
	                     boolean rowEmpty = true;
	                     for (int colIndex : colIndexes) {
	                         Cell cell = row.getCell(colIndex);
	                         if (cell != null) {
	                             String val;
	                             if (cell.getCellTypeEnum() == CellType.FORMULA) {
	                                 try {
	                                     CellValue cv = evaluator.evaluate(cell);
	                                     val = (cv != null) ? cv.formatAsString() : "";
	                                 } catch (Exception ex) { val = ""; }
	                             } else {
	                                 val = formatter.formatCellValue(cell);
	                             }
	                             if (!val.trim().isEmpty() && !val.equals("0")) {
	                                 rowEmpty = false;
	                                 break;
	                             }
	                         }
	                     }
	                     if (rowEmpty) continue;
	                 } else {
	                     continue;
	                 }

	                 for (int colIndex : colIndexes) {
	                     String cellKey = rowIndex + "-" + colIndex;
	                     if (mergedCells.contains(cellKey)) continue;

	                     Cell cell = row.getCell(colIndex);
	                     String cellValue = "";
	                     org.apache.poi.ss.usermodel.CellStyle style = null;

	                     if (cell != null) {
	                         style = cell.getCellStyle();
	                         if (cell.getCellTypeEnum() == CellType.FORMULA) {
	                             try {
	                                 CellValue eval = evaluator.evaluate(cell);
	                                 if (eval != null) {
	                                     switch (eval.getCellTypeEnum()) {
	                                         case STRING:
	                                             cellValue = eval.getStringValue();
	                                             break;
	                                         case NUMERIC:
	                                             double num = eval.getNumberValue();
	                                             String fmt = formatter.formatCellValue(cell, evaluator);
	                                             cellValue = (fmt != null && !fmt.trim().isEmpty())
	                                                     ? fmt
	                                                     : (num == Math.floor(num)
	                                                             ? String.valueOf((long) num)
	                                                             : df.format(num));
	                                             break;
	                                         case BOOLEAN:
	                                             cellValue = String.valueOf(eval.getBooleanValue());
	                                             break;
	                                         default:
	                                             cellValue = "";
	                                     }
	                                 }
	                             } catch (Exception ex) { cellValue = ""; }
	                         } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
	                             String fmt = formatter.formatCellValue(cell, evaluator);
	                             cellValue = (fmt != null && !fmt.trim().isEmpty())
	                                     ? fmt
	                                     : df.format(cell.getNumericCellValue());
	                         } else {
	                             cellValue = formatter.formatCellValue(cell);
	                         }
	                     }

	                     // PDF font
	                     Font cellFont;
	                     if (cell != null && style != null) {
	                         short fontIdx = style.getFontIndex();
	                         org.apache.poi.ss.usermodel.Font excelFont = workbook.getFontAt(fontIdx);
	                         boolean isBold   = excelFont.getBold();
	                         boolean isItalic = excelFont.getItalic();
	                         int styleInt = Font.NORMAL;
	                         if (isBold)   styleInt |= Font.BOLD;
	                         if (isItalic) styleInt |= Font.ITALIC;
	                         cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9f, styleInt);
	                     } else {
	                         cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9f, Font.NORMAL);
	                     }

	                     Phrase phrase   = new Phrase(cellValue, cellFont);
	                     PdfPCell pdfCell = new PdfPCell(phrase);

	                     // Merged-cell colspan counted against filtered colIndexes
	                     for (CellRangeAddress mergeRange : mergedRegions) {
	                         if (mergeRange.isInRange(rowIndex, colIndex)) {
	                             if (rowIndex == mergeRange.getFirstRow() && colIndex == mergeRange.getFirstColumn()) {
	                                 int rowspan = mergeRange.getLastRow()    - mergeRange.getFirstRow()    + 1;
	                                 int colspan = 0;
	                                 for (int c : colIndexes) {
	                                     if (c >= mergeRange.getFirstColumn() && c <= mergeRange.getLastColumn())
	                                         colspan++;
	                                 }
	                                 if (rowspan > 1) pdfCell.setRowspan(rowspan);
	                                 if (colspan > 1) pdfCell.setColspan(colspan);
	                                 for (int mr = mergeRange.getFirstRow(); mr <= mergeRange.getLastRow(); mr++) {
	                                     for (int mc : colIndexes) {
	                                         if (mc >= mergeRange.getFirstColumn() && mc <= mergeRange.getLastColumn()) {
	                                             if (mr != mergeRange.getFirstRow() || mc != mergeRange.getFirstColumn())
	                                                 mergedCells.add(mr + "-" + mc);
	                                         }
	                                     }
	                                 }
	                             } else {
	                                 mergedCells.add(cellKey);
	                                 pdfCell = null;
	                             }
	                             break;
	                         }
	                     }
	                     if (pdfCell == null) continue;

	                     // Background color
	                     BaseColor bgColor = null;
	                     if (cell != null && style != null) {
	                         org.apache.poi.ss.usermodel.Color poiColor = style.getFillForegroundColorColor();
	                         if (poiColor == null) poiColor = style.getFillBackgroundColorColor();
	                         if (poiColor instanceof XSSFColor) {
	                             XSSFColor xssfColor = (XSSFColor) poiColor;
	                             byte[] rgb = xssfColor.getRGBWithTint();
	                             if (rgb != null && (rgb[0] != 0 || rgb[1] != 0 || rgb[2] != 0))
	                                 bgColor = new BaseColor(rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF);
	                         } else if (workbook instanceof HSSFWorkbook) {
	                             HSSFWorkbook hwb = (HSSFWorkbook) workbook;
	                             short idx = style.getFillForegroundColor();
	                             if (idx == 0) idx = style.getFillBackgroundColor();
	                             if (idx > 0) {
	                                 HSSFPalette palette = hwb.getCustomPalette();
	                                 HSSFColor color = palette.getColor(idx);
	                                 if (color != null && color.getTriplet() != null) {
	                                     short[] rgb = color.getTriplet();
	                                     if (!(rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0))
	                                         bgColor = new BaseColor(rgb[0], rgb[1], rgb[2]);
	                                 }
	                             }
	                         }
	                     }
	                     pdfCell.setBackgroundColor(bgColor != null ? bgColor : BaseColor.WHITE);

	                     // Borders
	                     if (style != null) {
	                         boolean top    = style.getBorderTopEnum()    != BorderStyle.NONE;
	                         boolean bottom = style.getBorderBottomEnum() != BorderStyle.NONE;
	                         boolean left   = style.getBorderLeftEnum()   != BorderStyle.NONE;
	                         boolean right  = style.getBorderRightEnum()  != BorderStyle.NONE;
	                         int border = 0;
	                         if (top)    border |= Rectangle.TOP;
	                         if (bottom) border |= Rectangle.BOTTOM;
	                         if (left)   border |= Rectangle.LEFT;
	                         if (right)  border |= Rectangle.RIGHT;
	                         pdfCell.setBorder(border == 0 ? Rectangle.NO_BORDER : border);
	                     } else {
	                         pdfCell.setBorder(Rectangle.NO_BORDER);
	                     }

	                     // Alignment
	                     if (style != null) {
	                         switch (style.getAlignmentEnum()) {
	                             case LEFT:   pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);   break;
	                             case RIGHT:  pdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);  break;
	                             case CENTER: pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER); break;
	                             default:     pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	                         }
	                         switch (style.getVerticalAlignmentEnum()) {
	                             case TOP:    pdfCell.setVerticalAlignment(Element.ALIGN_TOP);    break;
	                             case CENTER: pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE); break;
	                             case BOTTOM: pdfCell.setVerticalAlignment(Element.ALIGN_BOTTOM); break;
	                             default:     pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                         }
	                     } else {
	                         pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	                         pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                     }

	                     pdfCell.setPadding(4f);
	                     boolean excelWrap = (style != null) && style.getWrapText();
	                     pdfCell.setNoWrap(!excelWrap);
	                     pdfCell.setMinimumHeight(rowHeightPt);

	                     table.addCell(pdfCell);
	                 }
	             }

	             document.add(table);
	             firstTable = false;
	         }

	         document.close();
	         return pdfOut.toByteArray();
	     }
	 }


}
