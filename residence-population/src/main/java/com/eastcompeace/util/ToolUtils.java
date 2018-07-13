package com.eastcompeace.util;

import com.eastcompeace.share.utils.StringUtils;

public class ToolUtils {
	/**
	 * Padding the sepcify string
	 * 
	 * @param src the source string
	 * @param length the length after padding
	 * @param str the string to be padded
	 * @param type the padding mode, L - left padding, R - right padding
	 * @return
	 */
	public static String padding(String src, int length, String str, String type) {
		if(StringUtils.isEmpty(src)) return src;
		if(length < 1 || length <= src.length()) return src;
		
		str = ("".equals(str)) ? "0" : str;
		int n = (length - src.length())/str.length();
		StringBuilder strTemp = new StringBuilder();
		for(int i=0; i<n; i++) {
			strTemp.append(str);
		}
		if(type.equalsIgnoreCase("L")) strTemp.append(src);
		if (type.equalsIgnoreCase("R")) strTemp.insert(0, src);
		return strTemp.toString();
	}
	
 	public static String encodUnicode(String str) {
		char[] utfBytes = str.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}
/**
 * 删除字符串后面的‘0’
 * @param tempString “440402001000”
 * @return 440402001
 */
 	 public static String delZero(String tempString) {
		   
	        int initlen = tempString.length(); // 串的初始长度
	        int finallen = initlen; // 串的最终长度
	        int start = 0; // 串的开始位置
	        int off = 0; // 串的偏移位置
	        char[] val = new char[initlen];
	        tempString.getChars(0, finallen, val, 0); // 保存原数据，用于判断字符
	 
	        while ((start < finallen) && (val[off + finallen - 1] == '0')) {
	            finallen--;
	        }
	        return ((start > 0) || (finallen < initlen)) ? tempString.substring(start, finallen)
	                : tempString;
	 }
 	 
}
