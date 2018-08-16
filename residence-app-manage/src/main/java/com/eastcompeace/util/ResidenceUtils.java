package com.eastcompeace.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * 居住证工具类
 * 
 * @author caobo
 * 
 */
public class ResidenceUtils {

	/**
	 * 解析转化调用存储过程返回的结果字符串，转化成Map key值分别是为 resCode resMessage
	 * 
	 * @param resStr
	 *            例： "0000|操作成功"
	 * @return resMap
	 */
	public static Map<String, String> transformProduceResult(String resStr) {
		Map<String, String> resMap = new HashMap<String, String>();
		if (StringUtils.isBlank(resStr)) {
			return null;
		}
		int idx = resStr.indexOf("\\|");
		if (idx < 0) {
			resMap.put("resCode", "999");
			resMap.put("resMessage", "存储过程返回结果不合法，无法解析");
		}

		String[] tranStr = resStr.split("\\|");
		if (tranStr.length == 2) {
			String resCode = tranStr[0];
			resMap.put("resCode", resCode);
			resMap.put("resMessage", tranStr[0]);
		}

		return resMap;
	}

	/**
	 * 获取流口数据导入序列码
	 * 
	 * @return
	 */
	public static String createSerializeNO() {
		String now = DateUtils.getNow("yyyyMMddHHmmssS");
		return "RCO" + now;
	}

	/**
	 * Java文件操作 获取不带扩展名的文件名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}



	
	/** 替换字符串中换行符*/
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	public static String byteCoverString(byte[] byArray) {
		String byStr = "";
		StringBuilder sb = new StringBuilder(byStr);
		for (byte element : byArray) {
			sb.append(String.valueOf(element));
		}
		byStr = sb.toString();
		return byStr;
	}
	
	
}
