package com.vission.mf.base.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vission.mf.base.enums.BaseConstants;

public class DateUtil {

	public static String DATE_FORMAT = "yyyy-MM-dd";

	public static String DATE_YMD = "yyyyMMdd";

	public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String DATE_YEAR_MONTH = "yyyyMM";

	/**
	 * 将Date类型转换为字符串
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将Date类型转换为字符串
	 * 
	 * @param date
	 *            日期类型
	 * @param pattern
	 *            字符串格式
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return "null";
		}
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new java.text.SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 将字符串转换为Date类型
	 * 
	 * @param date
	 *            字符串类型
	 * @return 日期类型
	 */
	public static Date format(String date) {
		return format(date, null);
	}

	/**
	 * 将字符串转换为Date类型
	 * 
	 * @param date
	 *            字符串类型
	 * @param pattern
	 *            格式
	 * @return 日期类型
	 */
	public static Date format(String date, String pattern) {
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (date == null || date.equals("") || date.equals("null")) {
			return new Date();
		}
		// 避免yyyy-MM-dd型返回null
		if (date.indexOf(":") < 0) {
			date = date + " 00:00:00";
		}
		Date d = null;
		try {
			d = new java.text.SimpleDateFormat(pattern).parse(date);
		} catch (ParseException pe) {
		}
		return d;
	}

	public static Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df;
		Date date;
		df = new SimpleDateFormat(aMask);

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * 切换字符串的日期格式
	 * 
	 * @param date
	 * @param from
	 * @param to
	 * @return
	 */
	public static String changeDataPattern(String date, String from, String to) {
		Date d = format(date, from);
		if (to == null) {
			to = "yyyy-MM-dd";
		}
		return format(d, to);
	}

	public static String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		String defaultDatePattern;
		try {
			defaultDatePattern = ResourceBundle.getBundle(
					BaseConstants.BUNDLE_KEY, locale).getString("date.format");
		} catch (MissingResourceException mse) {
			defaultDatePattern = "yyyy-MM-dd";
		}

		return defaultDatePattern;
	}

	public static Date getDateofToday() {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
		String todayAsString = df.format(today);
		return convertStringToDate(todayAsString);
	}

	public static Date convertStringToDate(String strDate) {
		Date aDate = null;
		try {
			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		return aDate;
	}

	/**
	 * @Title: getStrDateTOTimestamp
	 * @Description: 将yyyy-mm-dd 日期格式转换 Timestamp 类型
	 * @param @param delegatestarttime
	 * @param @return
	 * @return Timestamp
	 * @throws
	 */
	public static Timestamp getStrDateTOTimestamp(Object obj) {
		if (obj == null) {
			return null;
		} else {
			Date date = stringToDate(obj.toString(), DateUtil.DATE_FORMAT);
			return new Timestamp(date.getTime());
		}
	}

	/**
	 * 字符转化为日期
	 *
	 *
	 * @date 2011-12-14
	 * @param strDate
	 * @param pattern
	 * @return Date
	 */
	public static Date stringToDate(String strDate, String pattern) {
		try {
			long ltime = stringToLong(strDate, pattern);
			return new Date(ltime);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 字符类型日期转化为长类型
	 *
	 *
	 * @date 2011-12-14
	 * @param strDate
	 * @param pattern
	 * @throws ParseException
	 * @return long
	 */
	public static long stringToLong(String strDate, String pattern)
			throws ParseException {
		Locale locale = Locale.CHINESE;
		return stringToLong(strDate, pattern, locale);
	}

	/**
	 * 字符类型日期转化为长类型
	 *
	 * 
	 * @date 2011-12-14
	 * @param strDate
	 * @param pattern
	 * @param locale
	 * @throws ParseException
	 * @return long
	 */
	public static long stringToLong(String strDate, String pattern,
			Locale locale) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		Date date = sdf.parse(strDate);
		return date.getTime();
	}

	/**
	 * 
	 * @Title: getStrDateTOTimestampInTime
	 * @Description: String 转 Timestamp ，格式为：yyyy-MM-dd HH:mm:ss
	 * @param @param obj
	 * @param @return
	 * @return Timestamp
	 * @throws
	 */
	public static Timestamp getStrDateTOTimestampInTime(Object obj) {
		if (obj == null) {
			return null;
		} else {
			Date date = stringToDate(obj.toString(), DateUtil.DATE_TIME_FORMAT);
			return new Timestamp(date.getTime());
		}
	}
	
	/**
	 * @Title: getSystemDate 
	 * @Description: 获取系统日期
	 * @param @return
	 * @return Date 
	 * @throws
	 */
    public static Timestamp getSystemTime(){
    	Date date = new Date();
    	Timestamp nousedate = new Timestamp(date.getTime());
    	return nousedate;
    }
    /**
     * 获取当前系统时间
     */
	public static String getCurrentSystemTime() {
		
		return format(new Date(), DATE_TIME_FORMAT);
	}
	/**
	 * 获取当前年月
	 * @return
	 */
	public static String getYearMonthStr() {
		
		return format(new Date(), DATE_YEAR_MONTH);
	}
}
