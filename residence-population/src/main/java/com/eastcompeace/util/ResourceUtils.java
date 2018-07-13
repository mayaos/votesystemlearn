package com.eastcompeace.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 获取 configure.properties文件中配置数据
 */
public final class ResourceUtils {
	
	private static Log LOGGER = LogFactory.getLog(ResourceUtils.class);
	
	public static final String CONFIG_FILE = "configure.properties";
	
	public static String getProperty (String key) {
		Properties p = new Properties();
		String strTemp = "";
		try {
			p.load(ResourceUtils.class.getClassLoader().getResourceAsStream(ResourceUtils.CONFIG_FILE));
			strTemp = p.getProperty(key);
		} catch (IOException ex) {
			LOGGER.error(ex);
		}finally{
			p.clear();
		}
		return strTemp;	
	}
	
	public static void main(String[] args) {
		String property = getProperty("menuGetUrl");
		System.out.println(property);
	}
	
}
