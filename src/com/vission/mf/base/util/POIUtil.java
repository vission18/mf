package com.vission.mf.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaParsingWorkbook;
import org.apache.poi.ss.formula.FormulaRenderer;
import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.AreaPtgBase;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.RefPtgBase;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 功能/模块 ：POI操作OFFICE函数集合.
 */
public class POIUtil {
	private final static String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 创建Excel文件
	 * 
	 * @param fileName
	 *            文件路径+文件名
	 * @param rows
	 *            行数据
	 * @param startRow
	 *            开始行
	 * @param startCell
	 *            开始列
	 */
	public static void createExcel(String fileName, List<List<String>> rows,
			int startRow, int startCell) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		FileOutputStream fileOut = null;
		try {
			HSSFSheet sheet1 = workbook.createSheet("import data");
			int iRow = startRow;
			int iCell = startCell;
			for (List<String> rowList : rows) {
				HSSFRow tmpRow = sheet1.createRow(iRow);
				for (String row : rowList) {
					tmpRow.createCell(iCell).setCellValue(row);
					iCell++;
				}
				iRow++;
			}
			fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			try {
				if (fileOut != null)
					fileOut.close();
			} catch (IOException e) {
			}
		}
	}

	public static void createExcel(Workbook wb, String fileName)
			throws Exception {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
		} catch (IOException ioe) {
		} finally {
			try {
				if (fileOut != null)
					fileOut.close();
			} catch (IOException e) {
			}
		}
	}

	public static synchronized void downloadExcel(String fileName,
			List<Map<String, String>> rows,
			Map<String, Map<String, String>> paraFieldMap, int startRow,
			int startCell, HttpServletResponse response) throws IOException {
		ServletOutputStream out = null;
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet1 = workbook.createSheet("导入数据");
		int iRow = startRow;
		int iCell = startCell;
		for (Map<String, String> rowMap : rows) {
			HSSFRow tmpRow = sheet1.createRow(iRow);
			for (String row : rowMap.keySet()) {
				tmpRow.createCell(iCell).setCellValue(row);
				iCell++;
			}
			iRow++;
		}
		HSSFSheet sheet2 = workbook.createSheet("填写说明");
		HSSFRow tmpRow = sheet2.createRow(0);
		tmpRow.createCell(0).setCellValue("注：请将所填内容单元格格式设置成文本");
		iRow = 1;
		for (Map<String, String> rowMap : rows) {
			for (String row : rowMap.keySet()) {
				tmpRow = sheet2.createRow(iRow);
				tmpRow.createCell(0).setCellValue(row);
				tmpRow.createCell(1).setCellValue(rowMap.get(row));
				iRow++;
			}
		}
		for (String paraField : paraFieldMap.keySet()) {
			iRow++;
			tmpRow = sheet2.createRow(iRow);
			tmpRow.createCell(0).setCellValue(paraField + "-代码对照：");
			Map<String, String> map = paraFieldMap.get(paraField);
			for (String key : map.keySet()) {
				iRow++;
				tmpRow = sheet2.createRow(iRow);
				tmpRow.createCell(1).setCellValue(key);
				tmpRow.createCell(2).setCellValue(map.get(key));
			}
		}

		response.reset();
		response.setContentType("application/x-msdownload;");
		// fileName = new String(fileName.getBytes("GBK"), "UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName);
		out = response.getOutputStream();
		workbook.write(out);
		out.close();
	}

	public static synchronized void downloadExcel(List<String> sheetNameList,
			String fileName, Map<String, List<String[]>> rowsMap,
			Map<String, String[]> dataTypeMap, int startRow, int startCell,
			HttpServletResponse response) throws IOException {
		ServletOutputStream out = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		Map<String, CellStyle> csMap = new HashMap<String, CellStyle>();
		CellStyle csNumber1 = workbook.createCellStyle();
		csNumber1.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		CellStyle csNumber2 = workbook.createCellStyle();
		csNumber2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		CellStyle csPercent1 = workbook.createCellStyle();
		csPercent1.setDataFormat((workbook.createDataFormat()
				.getFormat("#,##0.00%")));
		CellStyle csPercent2 = workbook.createCellStyle();
		csPercent2.setDataFormat((workbook.createDataFormat()
				.getFormat("#,##0%")));
		csMap.put("number1", csNumber1);
		csMap.put("number2", csNumber2);
		csMap.put("percent1", csPercent1);
		csMap.put("percent2", csPercent2);
		for (String sheetName : sheetNameList) {
			HSSFSheet sheet1 = workbook.createSheet(sheetName);
			String[] dataTypes = null;
			if (dataTypeMap != null)
				dataTypes = dataTypeMap.get(sheetName);
			int iRow = startRow;
			for (String[] rows : rowsMap.get(sheetName)) {
				HSSFRow tmpRow = sheet1.createRow(iRow);
				int iCell = startCell;
				for (int i = 0; i < rows.length; i++) {
					HSSFCell tmpCell = tmpRow.createCell(iCell);
					if (dataTypes != null && dataTypes.length > i) {
						String dataType = dataTypes[i];
						if (dataType.equals("String") || iRow == startRow) {
							tmpCell.setCellType(Cell.CELL_TYPE_STRING);
							tmpCell.setCellValue(rows[i]);
						} else if (dataType.equals("Number")) {
							try {
								tmpCell.setCellType(Cell.CELL_TYPE_NUMERIC);
								if (rows[i].indexOf(".") > 0) {
									tmpCell.setCellValue(Double
											.parseDouble(rows[i].replaceAll(
													",", "")));// 如果有逗号分隔,替换掉
									tmpCell.setCellStyle(csMap.get("number1"));
								} else {
									tmpCell.setCellValue(Double
											.parseDouble(rows[i].replaceAll(
													",", "")));// 如果有逗号分隔,替换掉
									tmpCell.setCellStyle(csMap.get("number2"));
								}
							} catch (Exception e) {
								tmpCell.setCellType(Cell.CELL_TYPE_STRING);
								tmpCell.setCellValue("-");
							}
						} else if (dataType.equals("Percent")) {
							try {
								tmpCell.setCellType(Cell.CELL_TYPE_NUMERIC);
								if (rows[i].indexOf(".") > 0) {
									tmpCell.setCellValue(Double
											.parseDouble(rows[i].replaceAll(
													",", "")));// 如果有逗号分隔,替换掉
									tmpCell.setCellStyle(csMap.get("percent1"));
								} else {
									tmpCell.setCellValue(Double
											.parseDouble(rows[i].replaceAll(
													",", "")));// 如果有逗号分隔,替换掉
									tmpCell.setCellStyle(csMap.get("percent2"));
								}
							} catch (Exception e) {
								tmpCell.setCellType(Cell.CELL_TYPE_STRING);
								tmpCell.setCellValue("-");
							}
						} else {
							tmpCell.setCellValue(rows[i]);
						}
					} else {
						tmpCell.setCellValue(rows[i]);
					}
					iCell++;
				}
				iRow++;
			}
		}

		response.reset();
		response.setContentType("application/x-msdownload;");
		fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName);
		out = response.getOutputStream();
		workbook.write(out);
	}

	/**
	 * 读Excel文件
	 * 
	 * @param fileName
	 *            文件路径+文件名
	 * @param sheetIndex
	 *            sheet索引
	 * @param startRow
	 *            开始行
	 * @param startCell
	 *            开始列
	 * @param cellNum
	 *            总列数
	 * @return 数据List
	 * @throws Exception
	 */
	public static List<List<String>> readExcel(String fileName, int sheetIndex,
			int startRow, int startCell, int cellNum) throws Exception {
		FileInputStream fileIn = null;
		List<List<String>> result = new ArrayList<List<String>>();
		try {
			fileIn = new FileInputStream(fileName);
			HSSFWorkbook workbook = new HSSFWorkbook(fileIn);
			HSSFSheet sheet1 = workbook.getSheetAt(sheetIndex);
			for (int i = startRow; i <= sheet1.getLastRowNum(); i++) {
				List<String> row = new ArrayList<String>();
				HSSFRow tmpRow = sheet1.getRow(i);
				if (tmpRow == null)
					break;
				for (int j = startCell; j < cellNum; j++) {
					HSSFCell tmpCell = tmpRow.getCell(j);
					if (tmpCell == null)
						row.add("");
					else
						row.add(getCellValue(tmpRow.getCell(j)));
				}
				result.add(row);
			}
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			try {
				if (fileIn != null)
					fileIn.close();
			} catch (IOException e) {
			}
		}
		return result;
	}

	public static String getCellValue(HSSFCell cell) {
		int cellType = 0;
		String cellValue = "";
		if (cell != null) {
			cellType = cell.getCellType();
			switch (cellType) {
			case HSSFCell.CELL_TYPE_NUMERIC:// 0
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_STRING:// 1
				HSSFRichTextString str = cell.getRichStringCellValue();
				cellValue = str.getString();
				break;
			case HSSFCell.CELL_TYPE_FORMULA:// 2
				break;
			case HSSFCell.CELL_TYPE_BLANK:// 3
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:// 4
				break;
			case HSSFCell.CELL_TYPE_ERROR:// 5
				break;
			default:
				break;
			}
		}
		return cellValue;
	}

	/**
	 * 根据Excel列下标得到相应的编码,如2对应C
	 * 
	 * @param value
	 * @return
	 */
	public static String getColCode(int value) {

		if (value / 25 < 1) {
			return String.valueOf(rows.charAt(value));
		} else {

			int cj = value / 26;

			if (cj == 0) {
				return String.valueOf(rows.charAt(value));
			}

			int mod = value % 26;

			return String.valueOf(rows.charAt(cj - 1))
					+ String.valueOf(rows.charAt(mod));

		}

	}

	/**
	 * 根据列字母得出相应的下标值，例 C,2。 或者您也可以使用 apache POI 中的 这个类去获取
	 * org.apache.poi.hssf.util.CellReference
	 * 
	 * @param s
	 *            , Excel 列字母
	 * @return 列下标
	 */
	public static int getColNum(String s) {

		if (s.length() == 1) {
			return rows.indexOf(s);
		}

		return 26 * (rows.indexOf(s.substring(0, 1)) + 1)
				+ getColNum(s.substring(1, s.length()));
	}

	public static void main(String[] args) throws Exception {

		FileInputStream is = new FileInputStream(new File(
				"E:\\templates\\test\\S61001010.xls"));
		HSSFWorkbook wb = new HSSFWorkbook(is);

		HSSFSheet sheet1 = wb.getSheetAt(0);
		for (Row row : sheet1) {
			for (Cell cell : row) {
				CellReference cellRef = new CellReference(row.getRowNum(),
						cell.getColumnIndex());
				System.out.print(cellRef.formatAsString());
				System.out.print(" - ");

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					System.out.println(cell.getRichStringCellValue()
							.getString());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						System.out.println(cell.getDateCellValue());
					} else {
						System.out.println(cell.getNumericCellValue());
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.println(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					System.out.println(cell.getCellFormula());
					break;
				case Cell.CELL_TYPE_BLANK:

				}
			}
		}

	}

	// 重新计算所有公式项
	public static void formulaRecalculation(Workbook wb) {
		FormulaEvaluator evaluator = wb.getCreationHelper()
				.createFormulaEvaluator();
		for (int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum++) {
			Sheet sheet = wb.getSheetAt(sheetNum);
			for (Row r : sheet) {
				for (Cell c : r) {
					if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
						try {
							evaluator.evaluateFormulaCell(c);
						} catch (Exception e) {
							System.out.println(sheet.getSheetName() + "=出错公式："
									+ c.getCellFormula());
						}
					}
				}
			}
		}
	}

	/*
	 * 合并多个excel到一个
	 */
	public static void mergeExcel(String targetFileName, List<String> nameList) {
		FileInputStream in = null;
		FileInputStream tmpIn = null;
		FileOutputStream out = null;
		Workbook outputWb = null;
		try {
			File outputFile = new File(targetFileName);
			if (outputFile.exists()) {// 目标文件存在,就合并
				in = new FileInputStream(outputFile);
				outputWb = WorkbookFactory.create(in);
			} else {// 目标文件不存在,就新建
				outputWb = new HSSFWorkbook();
			}
			for (String name : nameList) {
				File inputFile = new File(name);
				tmpIn = new FileInputStream(inputFile);
				Workbook inputWb = WorkbookFactory.create(tmpIn);
				Sheet sourceSheet = inputWb.getSheetAt(0);
				String sheetName = name.substring(
						name.lastIndexOf(File.separator) + 1,
						name.lastIndexOf("."));
				Sheet targetSheet = outputWb.createSheet(sheetName);
				copySheet(sourceSheet, targetSheet);
				tmpIn.close();
			}
			out = new FileOutputStream(targetFileName);
			outputWb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
				if (tmpIn != null)
					tmpIn.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
			}
		}
	}

	/*
	 * 复制sheet
	 */
	private static void copySheet(Sheet sourceSheet, Sheet targetSheet) {
		for (int i = 0; i <= sourceSheet.getLastRowNum(); i++) {
			Row row = sourceSheet.getRow(i);
			Row tmpRow = targetSheet.createRow(i);
			if (row == null)
				continue;
			for (int j = 0; j <= row.getLastCellNum(); j++) {
				Cell cell = row.getCell(j);
				Cell tmpCell = tmpRow.createCell(j);
				if (cell == null)
					continue;
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					tmpCell.setCellType(Cell.CELL_TYPE_STRING);
					tmpCell.setCellValue(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					tmpCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					tmpCell.setCellValue(cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					tmpCell.setCellType(Cell.CELL_TYPE_FORMULA);
					tmpCell.setCellFormula(cell.getCellFormula());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					tmpCell.setCellType(Cell.CELL_TYPE_BOOLEAN);
					tmpCell.setCellValue(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_BLANK:
					tmpCell.setCellType(Cell.CELL_TYPE_BLANK);
					break;
				case Cell.CELL_TYPE_ERROR:
					tmpCell.setCellType(Cell.CELL_TYPE_ERROR);
					tmpCell.setCellValue(cell.getErrorCellValue());
					break;
				default:
					break;
				}
			}
		}
	}

	/*
	 * 复制sheet
	 */
	public static void copySheetOnlyNumberData(Sheet sourceSheet,
			Sheet targetSheet, List<String> textCellList,
			List<String> fillBlankItemList, String fillBlankString) {
		for (int i = 0; i <= sourceSheet.getLastRowNum(); i++) {
			Row row = sourceSheet.getRow(i);
			if (row == null)
				row = sourceSheet.createRow(i);
			Row tmpRow = targetSheet.getRow(i);
			if (tmpRow == null)
				tmpRow = targetSheet.createRow(i);
			for (int j = 0; j <= row.getLastCellNum(); j++) {
				boolean fillFlag = fillBlankItemList != null
						&& fillBlankItemList.contains((i + 1) + ":"
								+ getColCode(j)) && fillBlankString != null
						&& !"".equals(fillBlankString);
				Cell cell = row.getCell(j);
				if (cell == null && !fillFlag)
					continue;
				if (cell == null)
					cell = row.createCell(j);
				Cell tmpCell = tmpRow.getCell(j);
				if (tmpCell == null)
					tmpCell = tmpRow.createCell(j);
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
						|| cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					try {
						double value = cell.getNumericCellValue();
						tmpCell.setCellValue(value);
					} catch (Exception e) {
						if (fillFlag) {
							tmpCell.setCellValue(fillBlankString);
						}
					}
				} else {
					if (textCellList != null
							&& textCellList.contains(i + ":" + j)) {
						try {
							String value = cell.getStringCellValue();
							tmpCell.setCellValue(value);
						} catch (Exception e) {
						}
					}
					if (fillFlag) {
						tmpCell.setCellValue(fillBlankString);
					}
				}
			}
		}
	}

	/**
	 * 复制行
	 * 
	 * @param sheet
	 * @param sourceRowNo
	 *            源行号
	 * @param destRowNo
	 *            目标行号
	 * @param cellNum
	 *            复制的列数
	 * @param dataFlag
	 *            是否复制数据
	 * @param styleFlag
	 *            是否复制样式
	 */
	public static void copyRow(Workbook wb, Sheet sheet, int sourceRowNo, int destRowNo,
			int cellNum, boolean dataFlag, boolean styleFlag) {
		Row sourceRow = sheet.getRow(sourceRowNo);
		if (sourceRow == null) {
			sourceRow = sheet.createRow(sourceRowNo);
		}
		Row destRow = sheet.getRow(destRowNo);
		if (destRow == null) {
			destRow = sheet.createRow(destRowNo);
		}
		destRow.setHeight(sourceRow.getHeight());// 行高度

		// 拷贝合并的单元格
		CellRangeAddress region = null;
		int reginNum = sheet.getNumMergedRegions();//返回所有合并单元格数
		for (int i = 0; i < reginNum; i++) {
			region = sheet.getMergedRegion(i);
			if(region.getFirstRow()<sourceRowNo || region.getFirstColumn()>cellNum){
				continue;//这里去掉复制区域之外的(表头部分)的合并单元格
			}
			int beginRowNo = region.getFirstRow() + (destRowNo - sourceRowNo);
			int endRowNo = region.getLastRow() + (destRowNo - sourceRowNo);
			sheet.addMergedRegion(new CellRangeAddress(beginRowNo, endRowNo,
						region.getFirstColumn(), region.getLastColumn()));
		}

		Cell sourceCell = null;
		Cell destCell = null;
		for (int cellNo = 0; cellNo < cellNum; cellNo++) {
			sourceCell = sourceRow.getCell(cellNo);
			if (sourceCell == null) {
				sourceCell = sourceRow.createCell(cellNo);
			}
			destCell = destRow.getCell(cellNo);
			if (destCell == null) {
				destCell = destRow.createCell(cellNo);
			}
			int cellType = sourceCell.getCellType();//列数据类型
			if (styleFlag) {// 复制样式
				destCell.setCellStyle(sourceCell.getCellStyle());
				destCell.setCellType(cellType);
			}
			if (dataFlag) {// 复制数据,根据单元格数据类型进行转换
				switch (cellType) {
				case Cell.CELL_TYPE_STRING://文本
					destCell.setCellValue(sourceCell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC://数值
					destCell.setCellValue(sourceCell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA://公式
					destCell.setCellFormula(getCopyFormula(wb, sheet, sourceCell, destCell));
					break;
				case Cell.CELL_TYPE_BOOLEAN://布尔
					destCell.setCellValue(sourceCell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_BLANK://空
					break;
				case Cell.CELL_TYPE_ERROR://错误
					destCell.setCellValue(sourceCell.getErrorCellValue());
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * 复制列
	 * 
	 * @param sheet
	 * @param sourceCellNo
	 *            源列号
	 * @param destCellNo
	 *            目标列号
	 * @param rowNum
	 *            复制的行数
	 * @param dataFlag
	 *            是否复制数据
	 * @param styleFlag
	 *            是否复制样式
	 */
	public static void copyCell(Workbook wb, Sheet sheet, int sourceCellNo, int destCellNo,
			int rowNum, boolean dataFlag, boolean styleFlag) {

		// 拷贝合并的单元格
		CellRangeAddress region = null;
		int reginNum = sheet.getNumMergedRegions();//返回所有合并单元格数
		for (int i = 0; i < reginNum; i++) {
			region = sheet.getMergedRegion(i);
			if(region.getFirstColumn()<sourceCellNo || region.getFirstRow()>rowNum){
				continue;//这里去掉复制区域之外的(表头部分)的合并单元格
			}
			int beginCellNo = region.getFirstColumn() + (destCellNo - sourceCellNo);
			int endCellNo = region.getLastColumn() + (destCellNo - sourceCellNo);
			sheet.addMergedRegion(new CellRangeAddress(region.getFirstRow(), region.getLastRow(),
					beginCellNo, endCellNo));
		}
		Row row = null;
		Cell sourceCell = null;
		Cell destCell = null;
		for (int rowNo = 0; rowNo < rowNum; rowNo++) {
			row = sheet.getRow(rowNo);
			if (row == null) {
				row = sheet.createRow(rowNo);
			}						
			sourceCell = row.getCell(sourceCellNo);
			if (sourceCell == null) {
				sourceCell = row.createCell(sourceCellNo);
			}
			destCell = row.getCell(destCellNo);
			if (destCell == null) {
				destCell = row.createCell(destCellNo);
			}
			int cellType = sourceCell.getCellType();//列数据类型
			if (styleFlag) {// 复制样式
				destCell.setCellStyle(sourceCell.getCellStyle());
				destCell.setCellType(cellType);
			}
			if (dataFlag) {// 复制数据,根据单元格数据类型进行转换
				switch (cellType) {
				case Cell.CELL_TYPE_STRING://文本
					destCell.setCellValue(sourceCell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC://数值
					destCell.setCellValue(sourceCell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA://公式
					destCell.setCellFormula(getCopyFormula(wb, sheet, sourceCell, destCell));
					break;
				case Cell.CELL_TYPE_BOOLEAN://布尔
					destCell.setCellValue(sourceCell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_BLANK://空
					break;
				case Cell.CELL_TYPE_ERROR://错误
					destCell.setCellValue(sourceCell.getErrorCellValue());
					break;
				default:
					break;
				}
			}
		}
	}
	
	/**
	 * 复制公式 --直接复制公式里面的引用不会变化　使用此方法则解决这个问题
	 * @param workbook
	 * @param sheet
	 * @param oldCell
	 * @param newCell
	 * @return
	 */
	public static String getCopyFormula(Workbook workbook, Sheet sheet, Cell oldCell, Cell newCell) {
    	String oldFormula = oldCell.getCellFormula();
		String newFormula = new String();
		
		if(oldFormula != null) {
			FormulaParsingWorkbook parsingWorkbook = null;
			FormulaRenderingWorkbook renderingWorkbook  = null;
			
			if(workbook instanceof HSSFWorkbook) {
				parsingWorkbook = HSSFEvaluationWorkbook.create((HSSFWorkbook) workbook); 
				renderingWorkbook = HSSFEvaluationWorkbook.create((HSSFWorkbook) workbook); 
			} else if(workbook instanceof XSSFWorkbook) {
				parsingWorkbook = XSSFEvaluationWorkbook.create((XSSFWorkbook) workbook); 
				renderingWorkbook = XSSFEvaluationWorkbook.create((XSSFWorkbook) workbook); 
			}  

            // get PTG's in the formula
            Ptg[] ptgs = FormulaParser.parse(oldFormula, parsingWorkbook, FormulaType.CELL, workbook.getSheetIndex(sheet));

            // iterating through all PTG's
            for( Ptg ptg  : ptgs )
            {               
                if( ptg instanceof RefPtgBase ) // for references such as A1, A2, B3
                {
                	RefPtgBase refPtgBase = (RefPtgBase) ptg;
                	
                    // if row is relative
                	if(refPtgBase.isRowRelative() )
                	{
                		refPtgBase.setRow((short)(newCell.getRowIndex() - (oldCell.getRowIndex() - refPtgBase.getRow())));
                	}
                	
                    // if col is relative
                	if(refPtgBase.isColRelative() )
                	{
                		refPtgBase.setColumn((short)(newCell.getColumnIndex() - (oldCell.getColumnIndex() - refPtgBase.getColumn())));
                	}                	
                }
                if(ptg instanceof AreaPtgBase)  // for area of cells A1:A4 
                {
                	AreaPtgBase areaPtgBase = (AreaPtgBase)ptg;
                	
                    // if first row is relative
                    if(areaPtgBase.isFirstRowRelative() )
                    {
                    	areaPtgBase.setFirstRow((short)(newCell.getRowIndex() - (oldCell.getRowIndex() - areaPtgBase.getFirstRow())));
                    }
                    
                    // if last row is relative
                    if(areaPtgBase.isLastRowRelative())
                    {
                    	areaPtgBase.setLastRow((short)(newCell.getRowIndex() - (oldCell.getRowIndex() - areaPtgBase.getLastRow())));
                    }
                    
                    // if first column is relative
                    if(areaPtgBase.isFirstColRelative())
                    {
                    	areaPtgBase.setFirstColumn((short)(newCell.getColumnIndex() - (oldCell.getColumnIndex() - areaPtgBase.getFirstColumn())));
                    }

                    // if last column is relative
                    if(areaPtgBase.isLastColRelative())
                    {
                    	areaPtgBase.setLastColumn((short)(newCell.getColumnIndex() - (oldCell.getColumnIndex() - areaPtgBase.getLastColumn())));
                    }
                }                
            }
            
            newFormula = FormulaRenderer.toFormulaString(renderingWorkbook, ptgs);
		}
		
		return newFormula;
    }
	/**
	 * 
	 * @param in
	 * @param sheetIndex
	 * @param startRow
	 * @param startCell
	 * @param cellNum
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> readExcel07(InputStream in, int sheetIndex, int startRow, int startCell, int cellNum) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
			try {
				XSSFWorkbook workbook = new XSSFWorkbook(in);
				XSSFSheet sheet1 = workbook.getSheetAt(sheetIndex);
				for (int i = startRow; i <= sheet1.getLastRowNum(); i++) {
					List<String> row = new ArrayList<String>();
					XSSFRow tmpRow = sheet1.getRow(i);
					if (tmpRow == null)
						break;
					for (int j = startCell; j < cellNum; j++) {
						XSSFCell tmpCell = tmpRow.getCell(j);
						if (tmpCell == null)
							row.add("");
						else
							row.add(getCellValue07(tmpRow.getCell(j)));
					}
					result.add(row);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;
	}
	
	public static String getCellValue07(XSSFCell cell) {
		int cellType = 0;
		String cellValue = "";
		if (cell != null) {
			cellType = cell.getCellType();
			switch (cellType) {
			case XSSFCell.CELL_TYPE_NUMERIC:// 0
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_STRING:// 1
				XSSFRichTextString str = cell.getRichStringCellValue();
				cellValue = str.getString();
				break;
			case XSSFCell.CELL_TYPE_FORMULA:// 2
				break;
			case XSSFCell.CELL_TYPE_BLANK:// 3
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN:// 4
				break;
			case XSSFCell.CELL_TYPE_ERROR:// 5
				break;
			default:
				break;
			}
		}
		return cellValue;
	}
}
