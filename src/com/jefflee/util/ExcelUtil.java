package com.jefflee.util;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil {

	public static boolean isXls(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	public static boolean isXlsx(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	public static boolean validateExcel(String filePath) {
		if (filePath == null || !(isXls(filePath) || isXlsx(filePath))) {
			return false;
		}
		return true;
	}

	public static Object getValue(Cell cell) {
		Object cellValue;
		try {
			cellValue = cell.getStringCellValue();
		} catch (Exception e1) {
			try {
				cellValue = cell.getNumericCellValue();
			} catch (Exception e2) {
				cellValue = "";
			}
		}
		return cellValue;
	}
}
