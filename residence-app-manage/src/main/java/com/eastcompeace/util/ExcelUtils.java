package com.eastcompeace.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * encapsulate utility methods for Excel wordbook
 * 
 * @author zengzhaoxi, 2013-06-25
 */

public class ExcelUtils {

	private static Log LOGGER = LogFactory.getLog(ExcelUtils.class);

	/**
	 * Excel 标题样式定义 
	 * 
	 * @return
	 */
	public static WritableCellFormat titleWcf() {// 10号字体,上下左右居中,带黑色边框
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 14,
				WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		} catch (WriteException ex) {
			LOGGER.error(ExcelUtils.class, ex);
		}

		return format;
	}
	
	/**
	 * Excel 样式定义
	 * 
	 * @return
	 */
	public static WritableCellFormat wcf1() {// 10号字体,上下左右居中,带黑色边框
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		} catch (WriteException ex) {
			LOGGER.error(ExcelUtils.class, ex);
		}

		return format;
	}

	/**
	 * Excel 样式定义
	 * 
	 * @return
	 */
	public static WritableCellFormat wcf_left() {// 10号字体,上下左右居中,带黑色边框，左边框粗线
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			format.setBorder(Border.LEFT, BorderLineStyle.MEDIUM, Colour.BLACK);
		} catch (WriteException ex) {
			LOGGER.error(ExcelUtils.class, ex);
		}

		return format;
	}

	/**
	 * Excel 样式定义
	 * 
	 * @return
	 */
	public static WritableCellFormat wcf_right() {// 10号字体,上下左右居中,带黑色边框，右边框粗线
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10,
				WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			format
					.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM,
							Colour.BLACK);
		} catch (WriteException ex) {
			LOGGER.error(ExcelUtils.class, ex);
		}

		return format;
	}

	/**
	 * Excel 样式2定义
	 * 
	 * @return
	 */
	public static WritableCellFormat wcf2() {// 10号字体,上下左右居中,不带黑色边框
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 12,
				WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
				Colour.DARK_RED);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.NONE, BorderLineStyle.THIN, Colour.BLACK);
		} catch (WriteException ex) {
			LOGGER.error(ExcelUtils.class, ex);
		}

		return format;
	}

	/**
	 * 缩小并转换格式
	 * 
	 * @param srcPath
	 *            源路径
	 * @param destPath
	 *            目标路径
	 * @param height
	 *            目标高
	 * @param width
	 *            目标宽
	 * @param formate
	 *            文件格式
	 * @return
	 */
	public static boolean narrowAndFormateTransfer(String srcPath,
			String destPath, String formate) {
		boolean flag = false;
		try {
			File file = new File(srcPath);
			File destFile = new File(destPath);
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdir();
			}
			BufferedImage src = ImageIO.read(file); // 读入文件
			int width = src.getWidth(); // 源图宽
			int height = src.getHeight(); // 源图高
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			flag = ImageIO.write(tag, formate, new FileOutputStream(destFile));// 输出到文件流
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @author
	 * @param objData
	 *            导出内容数组
	 * @param sheetName
	 *            导出工作表的名称
	 * @param columns
	 *            导出Excel的表头数组
	 * @return
	 */
	public static int exportToExcel(HttpServletResponse response,
			List<Object> objData, String sheetName, String[] titles,
			String fileName) {
		int flag = 0;
		// 声明工作簿jxl.write.WritableWorkbook

		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型

		WritableWorkbook wwb;
		try {
			// 根据传进来的file对象创建可写入的Excel工作薄
			OutputStream os = response.getOutputStream();
			wwb = Workbook.createWorkbook(os);

			/*
			 * 创建一个工作表、sheetName为工作表的名称、"0"为第一个工作表
			 * 打开Excel的时候会看到左下角默认有3个sheet、"sheet1、sheet2、sheet3"这样
			 * 代码中的"0"就是sheet1、其它的一一对应。 createSheet(sheetName,
			 * 0)一个是工作表的名称，另一个是工作表在工作薄中的位置
			 */
			WritableSheet ws = wwb.createSheet(sheetName, 0);

			SheetSettings ss = ws.getSettings();
			ss.setVerticalFreeze(1);// 冻结表头

			WritableFont font1 = new WritableFont(WritableFont
					.createFont("微软雅黑"), 10, WritableFont.BOLD);
			WritableFont font2 = new WritableFont(WritableFont
					.createFont("微软雅黑"), 9, WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font1);
			WritableCellFormat wcf2 = new WritableCellFormat(font2);
			WritableCellFormat wcf3 = new WritableCellFormat(font2);// 设置样式，字体

			// 创建单元格样式
			// WritableCellFormat wcf = new WritableCellFormat();

			// 背景颜色
			wcf.setBackground(jxl.format.Colour.YELLOW);
			wcf.setAlignment(Alignment.CENTRE); // 平行居中
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
			wcf3.setAlignment(Alignment.CENTRE); // 平行居中
			wcf3.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
			wcf3.setBackground(Colour.LIGHT_ORANGE);
			wcf2.setAlignment(Alignment.CENTRE); // 平行居中
			wcf2.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中

			/*
			 * 这个是单元格内容居中显示 还有很多很多样式
			 */
			// wcf.setAlignment(Alignment.CENTRE);

			// 判断一下表头数组是否有数据
			if (titles.length > 0) {

				// 循环写入表头
				for (int i = 0; i < titles.length; i++) {

					/*
					 * 添加单元格(Cell)内容addCell() 添加Label对象Label()
					 * 数据的类型有很多种、在这里你需要什么类型就导入什么类型 如：jxl.write.DateTime
					 * 、jxl.write.Number、jxl.write.Label Label(i, 0, columns[i],
					 * wcf) 其中i为列、0为行、columns[i]为数据、wcf为样式
					 * 合起来就是说将columns[i]添加到第一行(行、列下标都是从0开始)第i列、样式为什么"色"内容居中
					 */
					ws.addCell(new Label(i, 0, titles[i], wcf));
				}

				// 判断表中是否有数据
				if (objData != null && objData.size() > 0) {
					// 循环写入表中数据
					for (int i = 0; i < objData.size(); i++) {

						Object object = objData.get(i);
						Field[] fields = object.getClass().getDeclaredFields();

						int j = 0;
						for (Field field : fields) {
							field.setAccessible(true);
							String strVal = "";
							Object cellCount = field.get(object);
							if (cellCount != null) {
								strVal = String.valueOf(cellCount);
							}
							ws.addCell(new Label(j, i + 1, strVal, wcf3));
							j++;
						}
					}
				} else {
					flag = -1;
				}

				// 写入Exel工作表
				wwb.write();

				// 关闭Excel工作薄对象
				wwb.close();

				// 关闭流
				os.flush();
				os.close();

				os = null;
			}
		} catch (IllegalStateException e) {
			System.err.println(e.getMessage());
		} catch (Exception ex) {
			flag = 0;
			ex.printStackTrace();
		}

		return flag;
	}

	/**
	 * EXCEL文件导出
	 * 
	 * @param response
	 *            响应对象
	 * @param objData
	 *            数据List
	 * @param fileName
	 *            导出后文件名
	 * @param templatePath
	 *            导出文件模版路径（包含名称）
	 * @return
	 */
	public static int exportToExcel(HttpServletResponse response, String title,
			List<Object> objData, String fileName, String templatePath) {
		int flag = 0;
		Workbook wb;
		WritableWorkbook wwb;
		try {
			fileName = new String(fileName.getBytes("GB2312"), "ISO-8859-1");
			OutputStream os = response.getOutputStream();
			wb = Workbook.getWorkbook(new File(templatePath));
			wwb = Workbook.createWorkbook(os, wb);
			WritableSheet ws = wwb.getSheet(0);
			WritableFont font2 = new WritableFont(WritableFont.createFont("微软雅黑"), 10, WritableFont.NO_BOLD);
			WritableCellFormat wcf3 = new WritableCellFormat(font2);// 设置样式，字体
			// 判断表中是否有数据
			if (objData != null && objData.size() > 0) {
				if(title != null){
					ws.addCell(new Label(0, 0, title + ws.getCell(0, 0).getContents(), ExcelUtils.titleWcf()));
				}
				// 循环写入表中数据
				for (int i = 0; i < objData.size(); i++) {
					Object object = objData.get(i);
					Field[] fields = object.getClass().getDeclaredFields();
					int j = 0;
					for (Field field : fields) {
						field.setAccessible(true);
						String strVal = "";
						Object cellCount = field.get(object);
						if (cellCount != null) {
							strVal = String.valueOf(cellCount);
							//System.out.println(strVal);
						}
						if(title == null){
							ws.addCell(new Label(j, i + 1, strVal, wcf3));
						}else{
							ws.addCell(new Label(j, i + 2, strVal, wcf3));
						}
						j++;
					}
				}
			} else {
				flag = -1;
			}
			// 写入Exel工作表
			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename="+ fileName);
			response.setContentType("application/msexcel");// 定义输出类型
			response.setContentType("charset=UTF-8");
			wwb.write();
			// 关闭Excel工作薄对象
			wwb.close();
			// 关闭流
			os.flush();
			os.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			flag = 0;
			ex.printStackTrace();
		}
		return flag;
	}

}
