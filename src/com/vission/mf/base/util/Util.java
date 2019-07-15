package com.vission.mf.base.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @类描述 工具类
 * */
public class Util {

	/**
	 * @方法描述 null值转换
	 * */
	public static String nullToEmpty(Object obj) {
		String rStr = "";
		rStr = (obj == null) ? "" : (String.valueOf(obj)).trim();
		rStr = "null".equals(rStr) ? "" : rStr;
		return rStr;
	}

	/**
	 * 获得当前系统时间
	 * 
	 * @return Date
	 */
	public static Date getSystemTime() {
		Calendar c = Calendar.getInstance();
		// 可以对每个时间域单独修改
		return c.getTime();
	}

	/**
	 * Convert date to String like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDate(Date d) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
	}

	/**
	 * function:获得当前时间，时间格式为:yyyyMMddHHmmss
	 * 
	 * @return String
	 */
	public static String getCurrentDateLong() {
		Date date = new Date();
		return dateFormatLong(date);
	}

	public static String dateFormatLong(Date date) {
		String dateStr = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		dateStr = sdf.format(date);
		return dateStr;
	}
	
	public static String dateToStr(Date date) {
		String dateStr = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		dateStr = sdf.format(date);
		return dateStr;
	}

	public static void main(String[] args) {
		// String str = "0010201010433";
		// System.out.println(str.substring(0, str.length() - 2));
		System.out.println(formatDate(new Date()));
	}
}
