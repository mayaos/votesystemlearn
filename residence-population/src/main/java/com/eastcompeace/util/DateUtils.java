package com.eastcompeace.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class DateUtils {

	// 格式：年－月－日 小时：分钟：秒
	public static final String FORMAT_yMdHms = "yyyy-MM-dd HH:mm:ss";

	// 格式：年－月－日 小时：分钟
	public static final String FORMAT_yMdHm = "yyyy-MM-dd HH:mm";
	// 格式：年－月－日
	public static final String FORMAT_yMd = "yyyy-MM-dd";

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param format
	 *            转换格式
	 * @return
	 */
	public static Date stringToDate(String dateStr, String format) {
		Date d = null;
		SimpleDateFormat formater = new SimpleDateFormat(format);

		try {
			formater.setLenient(false);
			d = formater.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			d = null;
		}
		return d;
	}

	/**
	 * 把日期转换为字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            转换格式
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		String result = null;
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			result = formater.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		}
		return result;
	}

	/**
	 * 获得当前日期字符串，默认格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static String getNow() {
		return dateToString(new Date(), FORMAT_yMdHms);
	}

	/**
	 * 获取指定格式的当前时间
	 * 
	 * @param format
	 *            格式
	 * @return
	 */
	public static String getNow(String format) {
		return dateToString(new Date(), format);
	}

	/**
	 * 两个日期相减
	 * 
	 * @return 相减得到的秒数
	 */
	public static long getTimeDiff(String firstTime, String secTime) {
		long first = stringToDate(firstTime, FORMAT_yMdHms).getTime();
		long second = stringToDate(secTime, FORMAT_yMdHms).getTime();
		return (second - first) / 1000;
	}

	/**
	 * 获取某年某月的天数
	 */
	public static int getDaysOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得当前日期
	 */
	public static int getToday() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获取相对于当天的日期 格式为"yyyy-MM-dd"
	 * 
	 * @param i
	 *            i>0往后i天,i<0为往前i天
	 * @return
	 */
	public static String getDate(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, i);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	/**
	 * 获取相对于date的日期 格式为"yyyy-MM-dd"
	 * 
	 * @param i
	 *            i>0往后i天,i<0为往前i天
	 * @return
	 */
	public static String getDate(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, i);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	/**
	 * 获得当前年份
	 */
	public static int getCurrYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回日期的天
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 返回日期的年
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回日期的月份，1-12
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取相对于date的月份
	 * 
	 * @param i
	 *            i>0往后i月,i<0为往前i月
	 * @return
	 */
	public static String getMonth(Date date, int i, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

	/**
	 * 获取相对于当前月份的月份日期
	 * 
	 * @param i
	 *            i>0往后i月,i<0为往前i月
	 * @return
	 */
	public static String getMonth(int i, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, i);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

	/**
	 * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
	 */
	public static long getDayDiff(Date date1, Date date2) {
		return (date2.getTime() - date1.getTime()) / 86400000;
	}

	/**
	 * 比较指定日期与当前日期的天差
	 */
	public static long getDayDiffCurr(String before) {
		return (new Date().getTime() - stringToDate(before, FORMAT_yMd)
				.getTime()) / 86400000;
	}

	/**
	 * 比较两个日期的年差
	 */
	public static int getYearDiff(String before, String after) {
		Date beforeDay = stringToDate(before, FORMAT_yMd);
		Date afterDay = stringToDate(after, FORMAT_yMd);
		return getYear(afterDay) - getYear(beforeDay);
	}

	/**
	 * 比较指定日期与当前日期的年差
	 */
	public static int getYearDiffCurr(String after) {
		return getYear(new Date()) - getYear(stringToDate(after, FORMAT_yMd));
	}

	/**
	 * 获取每月的第一周
	 */
	public static int getFirstWeekdayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
		c.set(year, month - 1, 1);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取每月的最后一周
	 */
	public static int getLastWeekdayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
		c.set(year, month - 1, getDaysOfMonth(year, month));
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 判断日期是否有效,包括闰年的情况
	 * 
	 * @param date
	 *            YYYY-mm-dd
	 * @return
	 */
	public static boolean isDate(String date) {
		StringBuffer reg = new StringBuffer(
				"^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");
		reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");
		reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");
		reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");
		reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");
		reg.append("1-9])|(1[0-9])|(2[0-8]))))))");
		Pattern p = Pattern.compile(reg.toString());
		return p.matcher(date).matches();
	}

	/**
	 * dateStr:Tue Feb 26 09:58:17 CST 2013 format:
	 * */
	public static String formatDate(String dateStr, String format) {
		try {
			// String dateStr = "Tue Feb 26 09:58:17 CST 2013";
			SimpleDateFormat sdf1 = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			System.out.println(sdf.format(sdf1.parse(dateStr)));
			return sdf.format(sdf1.parse(dateStr));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取当前日期之后的下一个星期几
	 * */
	public static Date getNextDayOfWeek(int day) {
		Calendar cal = Calendar.getInstance();
		int wd = cal.get(Calendar.DAY_OF_WEEK);
		if (wd < day) {
			cal.add(Calendar.DATE, day - wd);
		} else {
			cal.add(Calendar.DATE, day + 7 - wd);
		}
		return cal.getTime();
	}

	/**
	 * 获取几个月后的时间
	 * 
	 * @param month
	 * @return String
	 */
	public static String getAfterMonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 3);
		return dateToString(cal.getTime(), FORMAT_yMd);
	}

	/**
	 * 比较2个日期的大小
	 * @param date
	 * @param date2
	 * @return 后面的日期大返回true,前面的日期大返回false
	 */
	public static boolean compare2Day(Date date, Date date2) {
		int i = (int) (date.getTime() - date2.getTime());
		if (i >= 0) {
			return true;
		} else {
			return false;
		}

	}

	
	
	/** 
	 * 
	 * 字符串转日期
	 * @author xp
	 * @param date
	 * @return 正常情况下返回传入日期，异常返回当天
	 */
	
	public static Date getDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(date);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return new Date();
		}

	}
	public static void main(String[] args) {
		String testD = "2017-11-11";
		System.out.println(testD.replace("-", ""));
	}
}
