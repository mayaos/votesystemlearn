package com.eastcompeace.util;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IntegerUtils {

	private static Log LOGGER = LogFactory.getLog(IntegerUtils.class);
	
	/**
	 * get Integer value from HttpServletRequest by name
	 * 
	 * @param request the request send by the client to the server
	 * @param name the name of the parameter
	 * @return -1
	 */
	public static int integerMinus(String value) {
		if (value == null || value.equalsIgnoreCase(""))
			return -1;
		
		value = value.trim();
		int result = -1;
		try {
			result = Integer.parseInt(value);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return result;
	}
	
	/**
	 * 
	 * @param value
	 * @return 0
	 */
	public static int integer(String value) {
		if (value == null || value.equalsIgnoreCase(""))
			return 0;
		
		value = value.trim();
		int result = 0;
		try {
			result = Integer.parseInt(value);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return result;
	}
	
	public static BigDecimal bigDecimal(String value) {
		if (value == null || value.equalsIgnoreCase(""))
			return new BigDecimal("0");
		
		value = value.trim();
		BigDecimal result = new BigDecimal("0");
		try {
			result = new BigDecimal(value);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return result;
	}
	
	public static void main(String[] args){
		//System.out.println(integer(null));
		BigDecimal voteAreas = new BigDecimal("0.00");
		voteAreas = voteAreas.add(bigDecimal("244.6").setScale(0, BigDecimal.ROUND_HALF_UP));
		System.out.println(voteAreas.intValue());
		
	}
}
