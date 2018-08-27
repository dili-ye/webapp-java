package com.cn.webapp.commons.pub.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	/**
	 * 返回excel表格二维数组
	 * 
	 * @param f
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static String[][] read(File f) throws InvalidFormatException, IOException, GeneralSecurityException {
		String excelPath = f.getAbsolutePath();
		String password = "Excel文件密碼";
		String prefix = excelPath.substring(excelPath.lastIndexOf(".") + 1);
		Workbook workbook;
		InputStream inp = new FileInputStream(excelPath);
		if (prefix.toUpperCase().equals("XLS")) {
			org.apache.poi.hssf.record.crypto.Biff8EncryptionKey.setCurrentUserPassword(password);
			workbook = WorkbookFactory.create(inp);
			inp.close();
		} else {
			POIFSFileSystem pfs = new POIFSFileSystem(inp);
			inp.close();
			EncryptionInfo encInfo = new EncryptionInfo(pfs);
			Decryptor decryptor = Decryptor.getInstance(encInfo);
			decryptor.verifyPassword(password);
			workbook = new XSSFWorkbook(decryptor.getDataStream(pfs));
		}

		Sheet sheet = workbook.getSheetAt(0);
		int startRowNum = sheet.getFirstRowNum();
		int endRowNum = sheet.getLastRowNum();
		List<String[]> excelList = new ArrayList<>();
		for (int rowNum = startRowNum; rowNum <= endRowNum; rowNum++) {
			Row row = sheet.getRow(rowNum);
			if (row == null)
				continue;
			int startCellNum = row.getFirstCellNum();
			int endCellNum = row.getLastCellNum();
			List<Object> rowList = new ArrayList<>();
			for (int cellNum = startCellNum; cellNum < endCellNum; cellNum++) {
				Cell cell = row.getCell(cellNum);
				if (cell == null)
					continue;
				int type = cell.getCellType();
				String o = new String();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				switch (type) {
				case Cell.CELL_TYPE_NUMERIC:// 数值、日期类型
					double d = cell.getNumericCellValue();
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期类型
						o = sdf.format(cell.getDateCellValue());
					} else {// 数值类型
						o = String.valueOf(d);
					}
					break;
				case Cell.CELL_TYPE_BLANK:// 空白单元格
					o = "";
					break;
				case Cell.CELL_TYPE_STRING:// 字符类型
					o = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_BOOLEAN:// 布尔类型
					o = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					System.err.println("非法字符");// 非法字符;
					break;
				default:
					System.err.println("error");// 未知类型
					break;
				}
				rowList.add(o);
			}
			excelList.add(rowList.toArray(new String[0]));
		}
		return (excelList.toArray(new String[0][0]));
	}

	/**
	 * 将二维数组转换为excel文件
	 * 
	 * @param strs
	 * @return
	 * @throws IOException
	 */
	public static File createFile(String[][] strs) throws IOException {
		File f = new File(System.currentTimeMillis() + ".xls");
		HSSFWorkbook wb = new HSSFWorkbook();
		// 1.5、生成excel中可能用到的单元格样式
		// 首先创建字体样式
		HSSFFont font = wb.createFont();// 创建字体样式
		font.setFontName("宋体");// 使用宋体
		font.setFontHeightInPoints((short) 10);// 字体大小
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		// 然后创建单元格样式style
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setFont(font);// 将字体注入
		style1.setWrapText(true);// 自动换行
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		style1.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());// 设置单元格的背景颜色
		style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style1.setBorderTop((short) 1);// 边框的大小
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		// 2、生成一个sheet，对应excel的sheet，参数为excel中sheet显示的名字
		HSSFSheet sheet = wb.createSheet("sheet1");// 3、设置sheet中每列的宽度，第一个参数为第几列，0为第一列；第二个参数为列的宽度，可以设置为0。//Test中有三个属性，因此这里设置三列，第0列设置宽度为0，第1~3列用以存放数据
		sheet.setColumnWidth(0, 0);
		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 20 * 256);
		sheet.setColumnWidth(3, 20 * 256);// 4、生成sheet中一行，从0开始
		int rowIndex = 0;
		for (String[] str : strs) {
			HSSFRow row = sheet.createRow(rowIndex++);
			row.setHeight((short) 800);// 设定行的高度//5、创建row中的单元格，从0开始
			int cellIndex = 0;
			for (String s : str) {
				HSSFCell cell = row.createCell(cellIndex++);
				cell.setCellValue(s);
			}
		}
		FileOutputStream os = new FileOutputStream(f);
		wb.write(os);
		os.close();
		wb.close();
		return f;
	}
}