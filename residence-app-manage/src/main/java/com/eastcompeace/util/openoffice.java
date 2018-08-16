package com.eastcompeace.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.eastcompeace.system.config.ConfigController;

public class openoffice {
	/**
	 * 将Office文档转换为PDF. 运行该函数需要用到OpenOffice, OpenOffice excel内容不能为空
	 * 
	 * @param sourceFile
	 *            源文件,绝对路径. 可以是Office2003-2007全部格式的文档, Office2010的没测试. 包括.doc,
	 *            .docx, .xls, .xlsx, .ppt, .pptx等.
	 * 
	 * @param destFile
	 *            目标文件.绝对路径.
	 *            
	 */
	public  String word2pdf(String inputFilePath,String outputFilePath) {
		synchronized (this) {
		String outputName="";
		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		String officeHome = getOfficeHome();
		config.setOfficeHome(officeHome);
		OfficeManager officeManager = config.buildOfficeManager();
		officeManager.start();
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		//String outputFilePath = getOutputFilePath(inputFilePath);
		File inputFile = new File(inputFilePath);
		if (inputFile.exists()) {// 找不到源文件, 则返回
			File outputFile = new File(outputFilePath);
			if (!outputFile.getParentFile().exists()) { // 假如目标路径不存在, 则新建该路径
				outputFile.getParentFile().mkdirs();
			}
			converter.convert(inputFile, outputFile);
			//生成预览路径
				File file = new File(outputFilePath);
				String filename = file.getName();
				 outputName = filename.substring(0,filename.lastIndexOf("."))+".pdf";
		}
		officeManager.stop();
		return outputName;
		}
	}

	/**
	 * 输出文件路径
	 * 
	 * @param inputFilePath
	 * @return
	 */
	public static String getOutputFilePath(String inputFilePath) {
		File file = new File(inputFilePath);
		String filename = file.getName();
		String outputName = filename.substring(0,filename.lastIndexOf("."))+".pdf";
		InputStream inStream = ConfigController.class.getClassLoader().getResourceAsStream("configure.properties");
		Properties prop = new Properties();
		try {
			prop.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filepath = prop.getProperty("viewFile")+"/"+outputName;
		return filepath;
		
	}

	/**
	 * openoffice安装路径
	 * 
	 * @return
	 */
	public static String getOfficeHome() {
		String osName = System.getProperty("os.name");
		if (Pattern.matches("Linux.*", osName)) {
			return "/opt/openoffice4";
		} else if (Pattern.matches("Windows.*", osName)) {
			return "C:/Program Files (x86)/OpenOffice 4";
		} else if (Pattern.matches("Mac.*", osName)) {
			return "/Application/OpenOffice.org.app/Contents";
		}
		return null;
	}

	public static void main(String[] args) {
		openoffice of = new openoffice();
		for (int i = 0; i < 1000; i++) {
			of.word2pdf("D:/home/system/pms/upFiles/decision/已知漏洞信息.docx","D:/home/system/pms/upFiles/decision/已知漏洞信息.pdf");
		}
	}
}
