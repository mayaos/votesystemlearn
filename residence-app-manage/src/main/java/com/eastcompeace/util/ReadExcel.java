package com.eastcompeace.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.eastcompeace.base.Constant;

/**
 * @author xp
 * @created 2015-12-18
 */
public class ReadExcel {
	private static Log LOGGER = LogFactory.getLog(ReadExcel.class);

	/**
	 * read the Excel file
	 * 
	 * @param path
	 *            the path of the Excel file
	 * @return
	 * @throws IOException
	 */
	
	public static List<Map> readExcel(String path, int sRow) throws IOException {
		if (path == null || Constant.EMPTY.equals(path)) {
			return null;
		} else {
			String postfix = getPostfix(path);
			if (!Constant.EMPTY.equals(postfix)) {
				if (Constant.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path, sRow);
				} else if (Constant.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsx(path, sRow);
				}
			} else {
				return null;
				//System.out.println(path + "is not excelfile");
			}
		}
		return null;
	}

	/**
	 * Read the Excel 2015
	 * 
	 * @param path
	 *            the path of the excel file
	 * @author sRow=0  表示第一行
	 * @return
	 * @throws IOException
	 */
	public static List<Map> readXlsx(String path, int sRow) throws IOException {
		InputStream is = new FileInputStream(path);
		return readXlsStream(is, sRow);
	}
	
	public static List<Map> readXlsxStream(InputStream is, int sRow) throws IOException {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		Map<String, String> map = null;
		List<Map> list = new ArrayList<Map>();
		// Read the Sheet
		//for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			//XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0); //读取表格1
			if (xssfSheet != null) {
				//continue;
			// read the title
			XSSFRow xssfRow = null;
			xssfRow = xssfSheet.getRow(sRow); // 第一行为表头
			int colNum = xssfRow.getPhysicalNumberOfCells(); // 获取中列数
			String[] title = new String[colNum]; // 表头数组
			for (int i = 0; i < colNum; i++) {
				title[i] = getValue(xssfRow.getCell((short) i));
			}
			// Read the Row 循环行数
			for (int rowNum = sRow+1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				xssfRow = xssfSheet.getRow(rowNum);
				map = new HashMap<String, String>();
				if (xssfRow != null) {
					for (int i = 0; i < title.length; i++) {
						XSSFCell cell = xssfRow.getCell(i); // 获取每个单元格
						map.put(title[i], getValue(cell));
					}
					list.add(map);
				}
			}
			}
		//}
			return list;
	}

	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param path
	 *            the path of the Excel
	 * @author sRow=0  表示第一行
	 * @return
	 * @throws IOException
	 */
	public static List<Map> readXls(String path, int sRow) throws IOException {
		InputStream is = new FileInputStream(path);
		return readXlsStream(is, sRow);
	}
	
	public static List<Map> readXlsStream(InputStream is, int sRow) throws IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Map<String, String> map = null;
		List<Map> list = new ArrayList<Map>();
		// Read the Sheet

		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}

			//LOGGER.info("getLastRowNum: " +hssfSheet.getLastRowNum());
			// read the title
			HSSFRow hssfRow = hssfSheet.getRow(sRow); // 第一行为表头 sRow=0 表示第一行
			if (hssfRow == null) {
				continue;
			}
			int colNum = hssfRow.getPhysicalNumberOfCells(); // 获取列数
			String[] title = new String[colNum]; // 表头数组
			for (int i = 0; i < colNum; i++) {
				title[i] = getValue(hssfRow.getCell((short) i));
			}
			// Read the Row
			for (int rowNum = sRow+1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					map = new HashMap<String, String>();
					for (int i = 0; i < title.length; i++) {
						HSSFCell cell = hssfRow.getCell(i); // 获取每个单元格
						if(null != cell)
							map.put(title[i], getValue(cell));
					}
					list.add(rowNum - sRow -1, map);
				}
			}
		}
		return list;
	}

	private static String getValue(XSSFCell xssfRow) {
		String strCell = "";
		switch (xssfRow.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			strCell = xssfRow.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(xssfRow.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(xssfRow.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell.trim();

	}

	private static String getValue(HSSFCell hssfCell) {
		String strCell = "";
		switch (hssfCell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				strCell = hssfCell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				strCell = String.valueOf(hssfCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				strCell = String.valueOf(hssfCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_BLANK:
				strCell = "";
				break;
			default:
				strCell = "";
				break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}

	public static String getPostfix(String path) {
		if (path == null || Constant.EMPTY.equals(path.trim())) {
			return Constant.EMPTY;
		}
		if (path.contains(Constant.POINT)) {
			return path.substring(path.lastIndexOf(Constant.POINT) + 1,
					path.length());
		}
		return Constant.EMPTY;
	}
}