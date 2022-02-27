/*
package org.pkfrc.core.utilities.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelHelper {

	public static String getStringCellContent(HSSFWorkbook wb, HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		String value = null;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			value = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
			value = String.valueOf(cell.getErrorCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
			value = String.valueOf(cell.getStringCellValue());// cell.getCellFormula());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			DecimalFormat format = new DecimalFormat("###0");
			value = format.format(cell.getNumericCellValue());
			// value = new Double(cell.getNumericCellValue()).toString();
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			value = String.valueOf(cell.getRichStringCellValue());
		}
		return value;
	}

	public static Double getDoubleCellContent(HSSFWorkbook wb, HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		Double value = null;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			value = cell.getNumericCellValue();
		}
		return value;
	}

	public static Integer getIntegerCellContent(HSSFWorkbook wb, HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		Integer value = null;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			value = (new Double(cell.getNumericCellValue())).intValue();
		}
		return value;
	}

	public static String getDateCellContent(HSSFWorkbook wb, HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		if (cell.getDateCellValue() == null) {
			return null;
		} else {
			return DateHelper.formatDate(cell.getDateCellValue(), "dd/MM/yyyy");
		}

	}

	public static void buildExcellFileFromJTable(String location, String fileName, JTable jtable) throws Exception {
		buildExcellFileFromJTable(location, fileName, jtable.getModel());
	}

	public static void buildExcellFileFromJTable(String location, String fileName, TableModel model) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < model.getColumnCount(); i++) {
			row.createCell(i).setCellValue(model.getColumnName(i));
		}
		for (int i = 0; i < model.getRowCount(); i++) {
			row = sheet.createRow(i + 1);
			for (int j = 0; j < model.getColumnCount(); j++) {
				row.createCell(j).setCellValue(model.getValueAt(i, j).toString());
			}
		}
		FileOutputStream fileOut = new FileOutputStream(location + File.separator + fileName + ".xls");
		wb.write(fileOut);
		fileOut.close();
	}

	public static void buildExcellFileFromJTable(String location, String fileName, TableModel... models)
			throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		for (TableModel model : models) {
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < model.getColumnCount(); i++) {
				row.createCell(i).setCellValue(model.getColumnName(i));
			}
			for (int i = 0; i < model.getRowCount(); i++) {
				row = sheet.createRow(i + 1);
				for (int j = 0; j < model.getColumnCount(); j++) {
					row.createCell(j).setCellValue(model.getValueAt(i, j).toString());
				}
			}
		}
		FileOutputStream fileOut = new FileOutputStream(location + File.separator + fileName + ".xls");
		wb.write(fileOut);
		fileOut.close();
	}

}
*/
