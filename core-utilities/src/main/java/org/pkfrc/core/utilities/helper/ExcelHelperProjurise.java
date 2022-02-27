package org.pkfrc.core.utilities.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaRenderer;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.RefPtgBase;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.pkfrc.core.utilities.helper.DateHelper;
import org.pkfrc.core.utilities.helper.StringHelper;

/**
 * <p>
 * Class for manipulating Excel files in formats xlsx and xls
 * </p>
 *
 * @author Ulrich LELE
 *
 */
public class ExcelHelperProjurise {
    public static final String XLSX = "xlsx";
    public static final String XLS = "xls";

    public static void main(String[] args) throws Exception {
        String filePathName = "C:/Users/Ulrich/Desktop/test1.xlsx";
//		String savePath = "C:/Users/Ulrich/Desktop/testsave7.xlsx";
//		String copyPath = "C:/Users/Ulrich/Desktop/testcopyPath2.xlsx";
        Workbook wb = openWorkbook(filePathName);

        Sheet sheet1 = wb.getSheetAt(0);
        for (Row row : sheet1) {
            if (row.getRowNum() > 3) {
                for(Cell cell : row){
                    switch(cell.getColumnIndex()){
                        case 0:
                            ExcelHelperProjurise.duplicateRow(wb, sheet1, row);
                            break;
                    }
                }
            }
        }
    }

    // public static void duplicateFile(String source, String destination){
    // Files.cop
    // Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    // }

    public static void copyFileStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    /**
     * Get cell value (Evaluates de content if it is a formular) and returns an
     * Object.
     *
     * @param wb
     *            the workbook
     * @param cell
     *            The cell to get value.
     * @return Object
     */
    public static Object getCellValue(Workbook wb, Cell cell) {
        Object value;
        switch (cell.getCellTypeEnum()) {
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case FORMULA:
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellTypeEnum()) {
                    case STRING:
                        value = cellValue.getStringValue();
                        break;
                    case NUMERIC:
                        value = cellValue.getNumberValue();
                        break;
                    case BOOLEAN:
                        value = cellValue.getBooleanValue();
                        break;
                    case ERROR:
                        value = cellValue.getErrorValue();
                        break;
                    default:
                        value = null;
                        break;
                }
                break;
            case ERROR:
                value = cell.getErrorCellValue();
                break;
            default:
                value = null;
                break;
        }
        return value;
    }

    /**
     * get cell from cell reference
     *
     * @param sheet
     *            The working sheet
     * @param cellRef
     *            The cell refrence
     * @return Cell
     */
    public static Cell getCell(Sheet sheet, CellReference cellRef) {
        Cell cell = null;
        Row row = sheet.getRow(cellRef.getRow());
        if (row != null) {
            cell = row.getCell(cellRef.getCol());
        }
        return cell;
    }

    /**
     * Get a cell from row index and col index
     *
     * @param sheet
     *            The working sheet
     * @param rowIndex
     *            The row index
     * @param colIndex
     *            The col index
     * @return Cell
     */
    public static Cell getCell(Sheet sheet, int rowIndex, int colIndex) {
        Cell cell = null;
        Row r = sheet.getRow(rowIndex);
        if (r != null) {
            cell = r.getCell(colIndex);
        }
        return cell;
    }

    /**
     * Get cell value as a String (Evaluates de content if it is a formular) and
     * returns a String.
     *
     * @param wb
     *            the workbook
     * @param cell
     *            The cell to get value.
     * @return String Cell Value
     */
    public static String getStringCellValue(Workbook wb, Cell cell) {
        return String.valueOf(getCellValue(wb, cell));
    }

    /**
     * Get cell value as a String (Evaluates de content if it is a formular) and
     * returns a String.
     *
     * @param wb
     *            the workbook
     * @param cell
     *            The cell to get value.
     */
    public static Boolean getBooleanCellValue(Workbook wb, Cell cell) {
        return Boolean.valueOf(String.valueOf(getCellValue(wb, cell)));
    }

    /**
     * Get cell value as a String (Evaluates de content if it is a formular) and
     * returns a String.
     *
     * @param wb
     *            the workbook
     * @param cell
     *            The cell to get value.
     * @return Numeric (Double) Cell Value
     */
    public static Double getNumericCellValue(Workbook wb, Cell cell) {
        return Double.valueOf(String.valueOf(getCellValue(wb, cell)));
    }

    /**
     * Get cell value as a Date
     *
     * @param wb
     *            the workbook
     * @param cell
     *            The cell to get value.
     * @return Date Value
     */
    public static Date getDateCellValue(Workbook wb, Cell cell) {
        Date date = null;
        String value = String.valueOf(getCellValue(wb, cell));
        if (StringHelper.isNotEmpty(value)) {
            date = DateHelper.parse(value);
        }
        return date;
    }

    /**
     * Get cell value as a String (Evaluates de content if it is a formular) and
     * returns a String.
     *
     * @param wb
     *            the workbook
     * @param cell
     *            The cell to get formula value.
     * @return Formula (String) Cell Result
     */
    public static String getFormulaCellResult(Workbook wb, Cell cell) {
        return String.valueOf(getCellValue(wb, cell));
    }

    /**
     * Get cell formula as a String (Evaluates de content if it is a formular)
     * and returns a String.
     *
     * @param wb
     *            the workbook
     * @param cell
     *            The cell to get formula.
     * @return Formula (String)
     */
    public static String getFormula(Workbook wb, Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case FORMULA:
                return cell.getCellFormula();
            default:
                return String.valueOf(getCellValue(wb, cell));
        }
    }

    /**
     * Set cell alignment.
     *
     * @param wb
     *            the workbook
     * @param halign
     *            the horizontal alignment for the cell.
     * @param valign
     *            the vertical alignment for the cell.
     * @return Cell
     */
    public static Cell setAlignment(Workbook wb, Cell cell, HorizontalAlignment halign, VerticalAlignment valign) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    /**
     * Save and close a new Workbook
     *
     * @param wb
     *            the workbook
     * @param filePathName
     *            the complete path to the file name.
     */
    public static void saveAndClose(Workbook wb, String filePathName) throws IOException {
        // Write the output to a file
        try (OutputStream fileOut = new FileOutputStream(filePathName)) {
            wb.write(fileOut);
            fileOut.close();
            wb.close();
        }
    }

    /**
     * Create a Workbook with specific workbook type, XSSFWorkbook(xlsx) or
     * HSSFWorkbook(xls).
     *
     * @param extension
     *            the extension xlsx or xls
     * @return Workbook
     */
    public static Workbook createWorkbookExtension(String extension) {
        switch (extension) {
            case "xlsx":
                return new XSSFWorkbook();
            case "xls":
                return new HSSFWorkbook();
            default:
                return new XSSFWorkbook();
        }
    }

    // public static Sheet duplicateSheet(Workbook sourceWorkbook, Workbook
    // destWorkbook, Sheet sheet) {
    //
    // Sheet destSheet = destWorkbook.createSheet(sheet.getSheetName());
    // destSheet.setActiveCell(sheet.getActiveCell());
    // for (Row row : sheet) {
    // Row destRow = destSheet.createRow(row.getRowNum());
    // for (Cell cell : row) {
    // Cell destCell = destRow.createCell(cell.getColumnIndex());
    // cloneCell(destWorkbook, sheet, cell, destCell);
    // }
    // // copyAnyMergedRegions(sheet, row, destRow);
    // }
    // return destSheet;
    // }

    public static void copyFile(File source, File destination) throws IOException {
        Files.copy(source.toPath(), destination.toPath());
    }

    public static void copyFile(String source, String destination) throws IOException {
        File src = new File(source);
        File dest = new File(destination);
        Files.copy(src.toPath(), dest.toPath());
    }

    /**
     * Copy and merg region
     *
     * @param worksheet
     * @param sourceRow
     * @param newRow
     * @param mergedRegion
     */
    public static void copyMergeRegion(Sheet worksheet, Row sourceRow, Row newRow, CellRangeAddress mergedRegion) {
        CellRangeAddress range = mergedRegion;
        if (range.getFirstRow() == sourceRow.getRowNum()) {
            int lastRow = newRow.getRowNum() + (range.getLastRow() - range.getFirstRow());
            CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(), lastRow,
                    range.getFirstColumn(), range.getLastColumn());
            worksheet.addMergedRegion(newCellRangeAddress);
        }
    }

    /**
     * copy Any Merged Regions on a new row
     *
     * @param worksheet
     * @param sourceRow
     * @param newRow
     */
    public static void copyAnyMergedRegions(Sheet worksheet, Row sourceRow, Row newRow) {
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++)
            copyMergeRegion(worksheet, sourceRow, newRow, worksheet.getMergedRegion(i));
    }

    /**
     * Duplicate a row including value, style, formula, data type, comments,
     * Hyper link.
     *
     * @param workbook
     * @param worksheet
     * @param row
     */
    public static void duplicateRow(Workbook workbook, Sheet worksheet, Row row) {
        copyRow(workbook, worksheet, row.getRowNum(), row.getRowNum() + 1);
    }

    /**
     * Duplicate a row style, formula, data type and merged regions.
     *
     * @param workbook
     * @param worksheet
     * @param row
     */
    public static void duplicateRowStyleAndType(Workbook workbook, Sheet worksheet, Row row) {
        if (workbook != null && worksheet != null && row != null) {
            Row newRow = worksheet.getRow(row.getRowNum() + 1);
            if (newRow == null)
                newRow = worksheet.createRow(row.getRowNum() + 1);

            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell oldCell = row.getCell(i);
                Cell newCell = newRow.createCell(i);
                if (oldCell != null) {
                    if (oldCell.getCellStyle() != null) {
                        newCell.setCellStyle(oldCell.getCellStyle());
                    }

                    newCell.setCellType(oldCell.getCellTypeEnum());
                    switch (oldCell.getCellTypeEnum()) {
                        case FORMULA:
                            copyFormula(worksheet, oldCell, newCell);
                            break;
                        default:
                            break;
                    }
                }
            }
            copyAnyMergedRegions(worksheet, row, newRow);
        }
    }

    /**
     * Copy a row including value, style, formula, data type, comments, Hyper
     * link and merged regions.
     *
     * @param workbook
     * @param worksheet
     * @param sourceIndex
     * @param destinationIndex
     */
    public static void copyRow(Workbook workbook, Sheet worksheet, int sourceIndex, int destinationIndex) {

        Row sourceRow = worksheet.getRow(sourceIndex);
        Row newRow = worksheet.getRow(destinationIndex);
        if (newRow == null)
            newRow = worksheet.createRow(destinationIndex);

        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);
            cloneCell(workbook, worksheet, oldCell, newCell);
        }
        copyAnyMergedRegions(worksheet, sourceRow, newRow);
    }

    public static Cell cloneCell(Workbook wb, Sheet worksheet, Cell oldCell, Cell newCell) {
        if (oldCell != null) {
            newCell.setCellComment(oldCell.getCellComment());
            newCell.setCellStyle(getCellStyle(wb, oldCell));
            newCell.setHyperlink(oldCell.getHyperlink());
            newCell.setCellType(oldCell.getCellTypeEnum());
            switch (oldCell.getCellTypeEnum()) {
                case STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue().getString());
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(oldCell)) {
                        newCell.setCellValue(oldCell.getDateCellValue());
                    } else {
                        newCell.setCellValue(oldCell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case FORMULA:
                    copyFormula(worksheet, oldCell, newCell);
                    break;
                case ERROR:
                    newCell.setCellValue(oldCell.getErrorCellValue());
                    break;
                default:
                    break;
            }
        }
        return newCell;
    }

    public static CellStyle getCellStyle(Workbook destWorkbook, Cell oldCell) {
        CellStyle cellStyle = destWorkbook.createCellStyle();
        cellStyle.setAlignment(oldCell.getCellStyle().getAlignmentEnum());
        cellStyle.setBorderBottom(oldCell.getCellStyle().getBorderBottomEnum());
        cellStyle.setBorderTop(oldCell.getCellStyle().getBorderTopEnum());
        cellStyle.setBorderLeft(oldCell.getCellStyle().getBorderLeftEnum());
        cellStyle.setBorderRight(oldCell.getCellStyle().getBorderRightEnum());
        cellStyle.setBottomBorderColor(oldCell.getCellStyle().getBottomBorderColor());
        cellStyle.setDataFormat(oldCell.getCellStyle().getDataFormat());
        // cellStyle.setFillBackgroundColor(oldCell.getCellStyle().getFillBackgroundColor());
        // cellStyle.setFillForegroundColor(oldCell.getCellStyle().getFillForegroundColor());
        // cellStyle.setFillPattern(oldCell.getCellStyle().getFillPatternEnum());
        // cellStyle.setHidden(oldCell.getCellStyle().getHidden());
        cellStyle.setIndention(oldCell.getCellStyle().getIndention());
        cellStyle.setLeftBorderColor(oldCell.getCellStyle().getLeftBorderColor());
        cellStyle.setLocked(oldCell.getCellStyle().getLocked());
        cellStyle.setQuotePrefixed(oldCell.getCellStyle().getQuotePrefixed());
        cellStyle.setRightBorderColor(oldCell.getCellStyle().getRightBorderColor());
        cellStyle.setRotation(oldCell.getCellStyle().getRotation());
        cellStyle.setShrinkToFit(oldCell.getCellStyle().getShrinkToFit());
        cellStyle.setTopBorderColor(oldCell.getCellStyle().getTopBorderColor());
        cellStyle.setVerticalAlignment(oldCell.getCellStyle().getVerticalAlignmentEnum());
        cellStyle.setWrapText(oldCell.getCellStyle().getWrapText());
        return cellStyle;

    }

    /**
     * Create XSSFWorkbook
     *
     * @return Workbook
     */
    public static Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    /**
     * open Workbook
     *
     * @return Workbook
     */
    public static Workbook openWorkbook(String fileName) {

        try {
            return WorkbookFactory.create(new File(fileName));
        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * copy formula relatively from one cell to another
     *
     * @param sheet
     * @param org
     *            original cell
     * @param newCell
     *            Destination cell
     */
    public static void copyFormula(Sheet sheet, Cell org, Cell newCell) {
        if (org == null || newCell == null || sheet == null || org.getCellTypeEnum() != CellType.FORMULA)
            return;
        if (org.isPartOfArrayFormulaGroup())
            return;
        String formula = org.getCellFormula();
        int shiftRows = newCell.getRowIndex() - org.getRowIndex();
        int shiftCols = newCell.getColumnIndex() - org.getColumnIndex();
        XSSFEvaluationWorkbook workbookWrapper = XSSFEvaluationWorkbook.create((XSSFWorkbook) sheet.getWorkbook());
        Ptg[] ptgs = FormulaParser.parse(formula, workbookWrapper, FormulaType.CELL,
                sheet.getWorkbook().getSheetIndex(sheet));
        for (Ptg ptg : ptgs) {
            if (ptg instanceof RefPtgBase) // base class for cell references
            {
                RefPtgBase ref = (RefPtgBase) ptg;
                if (ref.isColRelative())
                    ref.setColumn(ref.getColumn() + shiftCols);
                if (ref.isRowRelative())
                    ref.setRow(ref.getRow() + shiftRows);
            } else if (ptg instanceof AreaPtg) // base class for range
            // references
            {
                AreaPtg ref = (AreaPtg) ptg;
                if (ref.isFirstColRelative())
                    ref.setFirstColumn(ref.getFirstColumn() + shiftCols);
                if (ref.isLastColRelative())
                    ref.setLastColumn(ref.getLastColumn() + shiftCols);
                if (ref.isFirstRowRelative())
                    ref.setFirstRow(ref.getFirstRow() + shiftRows);
                if (ref.isLastRowRelative())
                    ref.setLastRow(ref.getLastRow() + shiftRows);
            }
        }
        formula = FormulaRenderer.toFormulaString(workbookWrapper, ptgs);
        System.out.println("formula" + formula);
        newCell.setCellFormula(formula);
    }
}