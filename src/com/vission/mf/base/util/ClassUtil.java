package com.vission.mf.base.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

/**
 * 类工具
 * @author sweet uncle
 *
 */
public class ClassUtil {
	private static final String EMPTY_STR = "";
	private static TimeZone timeZoneLondon = TimeZone
			.getTimeZone("Europe/London");

	public static boolean isDate(String s) {
		boolean isRight = false;
		int len = s.length();
		char c = '.';
		if (len > 10) {
			if (hasContent(c, s)) {
				SimpleDateFormat p = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");
				try {
					Date d = p.parse(s);
					isRight = true;
				} catch (Exception e) {
					isRight = false;
				}
			} else {
				SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					Date d = p.parse(s);
					isRight = true;
				} catch (Exception e) {
					isRight = false;
				}
			}
		} else {
			SimpleDateFormat p = null;
			if (s.length() == 8 && s.indexOf("-") < 0)
				p = new SimpleDateFormat("yyyyMMdd");
			else
				p = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date temp = p.parse(s);
				isRight = true;
			} catch (Exception e) {
				isRight = false;
			}
		}
		return isRight;
	}

	public static boolean isDate(int year, int month, int day) {
		return isDate(String.valueOf(year) + "-" + String.valueOf(month) + "-"
				+ String.valueOf(day));
	}

	public static boolean isDateNoSeprate(String str) {
		if (str == null || str.length() != 8) {
			return false;
		} else {
			String y = str.substring(0, 4);
			String m = str.substring(4, 6);
			String d = str.substring(6, 8);
			return isDate(y + "-" + m + "-" + d);
		}
	}

	public static Date stringToDate(String s) {
		char c = '.';
		Date temp = null;
		int len = s.length();
		if (len > 10) {
			if (hasContent(c, s)) {
				SimpleDateFormat p = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");
				try {
					temp = p.parse(s);
				} catch (Exception exception) {
				}
			} else {
				SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					temp = p.parse(s);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} else {
			SimpleDateFormat p = null;
			if (s.length() == 8 && s.indexOf("-") < 0)
				p = new SimpleDateFormat("yyyyMMdd");
			else
				p = new SimpleDateFormat("yyyy-MM-dd");
			try {
				temp = p.parse(s);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return temp;
	}

	public static boolean isMail(String str) {
		char c = '@';
		if (isBlank(str))
			return false;
		int cPoint = str.indexOf(c);
		int j = 0;
		for (int i = 0; i < str.length(); i++){
			if (str.charAt(i) == c && ++j > 1)
				return false;
		}
		return cPoint > 0;
	}

	public static boolean isNumber(String str) {
		boolean bNum = true;
		char bt[] = new char[10];
		if (isBlank(str))
			return false;
		for (int i = 0; i < 10; i++)
			bt[i] = Integer.toString(i).charAt(0);

		for (int j = 0; j < str.length(); j++) {
			char c = str.charAt(j);
			if (!chkChar(c, bt))
				bNum = false;
		}

		return bNum;
	}

	public static boolean isInt(String str) {
		boolean bInt = false;
		int iStr = 0;
		if (isBlank(str))
			return false;
		try {
			iStr = Integer.parseInt(str);
			bInt = true;
		} catch (Exception exception) {
		}
		return bInt;
	}

	public static boolean isShort(String str) {
		boolean bShort = false;
		if (isBlank(str))
			return false;
		try {
			Short.parseShort(str);
			bShort = true;
		} catch (Exception exception) {
		}
		return bShort;
	}

	public static boolean isByte(String str) {
		boolean bByte = false;
		if (isBlank(str))
			return false;
		try {
			Byte.parseByte(str);
			bByte = true;
		} catch (Exception exception) {
		}
		return bByte;
	}

	public static boolean isBoolean(String str) {
		boolean bBoolean = false;
		try {
			chcBoolean(str);
			bBoolean = true;
		} catch (Exception exception) {
		}
		return bBoolean;
	}

	public static boolean isChar(Object str) {
		boolean bChar = false;
		if (str == null)
			return false;
		try {
			Character c = (Character) str;
			bChar = true;
		} catch (Exception e) {

		}
		return bChar;
	}

	public static boolean islong(String str) {
		boolean blong = false;
		long iStr = 0L;
		if (isBlank(str))
			return false;
		try {
			iStr = Long.parseLong(str);
			blong = true;
		} catch (Exception exception) {
		}
		return blong;
	}

	public static boolean isFloat(String str) {
		boolean bFloat = false;
		double dStr = 0.0D;
		if (isBlank(str))
			return false;
		try {
			Double d = new Double(str);
			dStr = d.floatValue();
			bFloat = true;
		} catch (Exception exception) {
		}
		return bFloat;
	}

	public static boolean isDouble(String str) {
		boolean bDouble = false;
		double dStr = 0.0D;
		if (isBlank(str))
			return false;
		try {
			Double d = new Double(str);
			dStr = d.doubleValue();
			bDouble = true;
		} catch (Exception exception) {
		}
		return bDouble;
	}

	private static boolean chkChar(char c, char data[]) {
		boolean bFound = false;
		for (int i = 0; i < data.length; i++){
			if (c == data[i])
				bFound = true;
		}
		return bFound;
	}

	public static boolean isBlank(String str) {
		boolean bBlank = false;
		if (str == null)
			return true;
		if (str.trim().equals("") || str.equalsIgnoreCase("null"))
			return true;
		return bBlank;
	}

	public static boolean isOverlength(String inputstr, int Textlen) {
		boolean bIso = false;
		try {
			byte b[] = inputstr.getBytes();
			if (b.length > Textlen)
				bIso = true;
		} catch (Exception exception) {
		}
		return bIso;
	}

	public static boolean isBigDecimal(String inputstr) {
		if (isBlank(inputstr))
			return false;
		try {
			BigDecimal big = new BigDecimal(inputstr);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isBigDecimal(String inputstr, int intLen) {
		if (isBlank(inputstr))
			return false;
		String bigIntStr;
		BigDecimal bigDec = new BigDecimal(inputstr);
		bigDec = bigDec.abs();
		java.math.BigInteger bigInt = bigDec.toBigInteger();
		bigIntStr = String.valueOf(bigInt);
		return bigIntStr.length() <= intLen;
	}

	public static boolean hasContent(char c, String value) {
		boolean isRight = false;
		char all[] = value.toCharArray();
		for (int i = 0; i < all.length; i++)
			if (all[i] == c)
				isRight = true;

		return isRight;
	}

	public static Timestamp chcMiliTime(Timestamp in) {
		long l = in.getTime();
		long mili = l % 1000L;
		l -= mili;
		return new Timestamp(l);
	}

	public static Timestamp chcMiliTime(Date in) {
		long l = in.getTime();
		long mili = l % 1000L;
		l -= mili;
		return new Timestamp(l);
	}

	public static boolean compareYearMonth(int year1, int month1, int year2,
			int month2) {
		boolean isRight = true;
		if (year1 < year2)
			isRight = false;
		else if (year1 == year2 && month1 < month2)
			isRight = false;
		return isRight;
	}

	public static boolean isSameDay(Timestamp t1, Timestamp t2) {
		boolean isRight = false;
		if (chcMiliTime(t1).getTime() == chcMiliTime(t2).getTime())
			isRight = true;
		return isRight;
	}

	public static boolean chcBoolean(Object obj) {
		if (obj == null || isBlank(obj.toString())) {
			return false;// modify by wq 2014-11-20
		}
		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue();
		if (obj instanceof String) {
			String s = (String) obj;
			if ("true".equalsIgnoreCase(s))
				return true;
			if ("false".equalsIgnoreCase(s))
				return false;
			else
				throw new RuntimeException("Parameter :" + s
						+ " is not Boolean Type.");
		} else {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Boolean Type.");
		}
	}

	public static byte chcByte(Object obj) {
		if (obj instanceof Byte)
			return ((Byte) obj).byteValue();
		else
			throw new RuntimeException("Parameter :" + obj
					+ " is not Byte type.");
	}

	public static double chcDouble(Object obj) {
		try {
			if (obj == null || isBlank(obj.toString()))
				return 0;// modify by wq 2014-11-20
			if (obj instanceof Double)
				return ((Double) obj).doubleValue();
			if (obj instanceof String)
				return Double.parseDouble((String) obj);
			throw new RuntimeException("Parameter :" + obj
					+ " is not Double type.");
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Double type.");
		}
	}

	public static float chcFloat(Object obj) {

		try {
			if (obj == null || isBlank(obj.toString()))
				return 0;// modify by wq 2014-11-20
			if (obj instanceof Float)
				return ((Float) obj).floatValue();
			if (obj instanceof String)
				return Float.parseFloat((String) obj);
			throw new RuntimeException("Parameter :" + obj
					+ " is not Float type.");
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Float type.");
		}
	}

	public static int chcInt(Object obj) {
		if (obj == null || isBlank(obj.toString()))
			return 0;
		if (obj instanceof Integer)
			return ((Integer) obj).intValue();
		if (obj instanceof String)
			return Integer.parseInt((String) obj);
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Int type.");
		}
	}

	public static Integer chcInteger(Object obj) {
		if (obj == null || isBlank(obj.toString())) {
			return null;
		}
		if (obj instanceof Integer)
			return ((Integer) obj).intValue();
		if (obj instanceof String)
			return Integer.parseInt((String) obj);
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Integer type.");
		}
	}

	public static long chcLong(Object obj) {
		try {
			if (obj == null || isBlank(obj.toString()))
				return 0;// modify by wq 2014-11-20
			if (obj instanceof Long)
				return ((Long) obj).longValue();
			if (obj instanceof String)
				return Long.parseLong((String) obj);
			throw new RuntimeException("Parameter :" + obj
					+ " is not Long type.");
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Long type.");
		}
	}

	public static short chcShort(Object obj) {
		try {
			if (obj == null || isBlank(obj.toString()))
				return 0; // modify by wq 2014-11-20
			if (obj instanceof Short)
				return ((Short) obj).shortValue();
			if (obj instanceof String)
				return Short.parseShort((String) obj);
			throw new RuntimeException("Parameter :" + obj
					+ " is not Short type.");
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Short type.");
		}
	}

	public static String chcString(Object obj) {
		if (obj == null || isBlank(obj.toString()))
			return EMPTY_STR; // modify by wq 2014-11-20
		if (obj instanceof String)
			return (String) obj;
		else
			return obj.toString();
		/*
		 * throw new RuntimeException("Parameter :" + obj +
		 * " is not String type.");
		 */
	}

	public static BigDecimal chcBigDecimal(Object obj) {
		try {
			if (obj == null || isBlank(obj.toString()))
				return BigDecimal.ZERO; // modify by wq 2014-11-20
			if (obj instanceof BigDecimal)
				return (BigDecimal) obj;
			if (obj instanceof String)
				if ("".equals(obj)) {
					return null;
				} else {
					return new BigDecimal((String) obj);
				}
			else {
				return new BigDecimal(obj.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not BigDecimal type.");
		}
	}

	public static BigDecimal chcBigDecimal(Object obj, boolean flag) {
		try {
			if ((obj == null || isBlank(obj.toString())) && flag)
				return null;
			else if (obj == null || isBlank(obj.toString()))
				return BigDecimal.ZERO; // modify by wq 2014-11-20
			if (obj instanceof BigDecimal)
				return (BigDecimal) obj;
			if (obj instanceof String)
				if ("".equals(obj)) {
					return null;
				} else {
					return new BigDecimal((String) obj);
				}
			else {
				return new BigDecimal(obj.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not BigDecimal type.");
		}
	}

	public static boolean chkNull(Object obj) {
		return obj == null;
	}

	public static boolean isNonnegateBigDecimal(String big) {
		if (!isBigDecimal(big))
			return false;
		BigDecimal b = new BigDecimal(big);
		return b.signum() >= 0;
	}

	public static boolean isPositiveBigDecimal(String big) {
		if (!isBigDecimal(big))
			return false;
		BigDecimal b = new BigDecimal(big);
		return b.signum() > 0;
	}

	public static boolean isIlLegalChar(String str) {
		char all[];
		int i;
		all = (new char[] { '?', '%' });
		i = 0;
		if (hasContent(all[i], str))
			return true;
		i++;
		if (i < all.length)
			return false;
		return false;
	}

	public static Timestamp chcTimestamp(Object obj) {
		try {
			if (obj == null || isBlank(obj.toString()))
				return new Timestamp(Calendar.getInstance(timeZoneLondon)
						.getTimeInMillis());// modify by wq 2014-11-20
			if (obj instanceof java.lang.String)
				return DateUtil.getStrDateTOTimestamp(obj);
			if (obj instanceof Timestamp)
				return (Timestamp) obj;
			if (obj instanceof Date)
				return new Timestamp(((Date) obj).getTime());
			throw new RuntimeException("Parameter :" + obj
					+ " is not Timestamp type.");
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Timestamp type.");
		}
	}

	public static Timestamp chcTimestamp(Object obj, boolean flag) {
		try {
			if (obj == null || isBlank(obj.toString()) && flag) {
				return null;
			} else if (obj == null || isBlank(obj.toString()))
				return new Timestamp(Calendar.getInstance(timeZoneLondon)
						.getTimeInMillis());// modify by wq 2014-11-20
	
			if (obj instanceof java.lang.String)
				return DateUtil.getStrDateTOTimestamp(obj);
			if (obj instanceof Timestamp)
				return (Timestamp) obj;
			if (obj instanceof Date)
				return new Timestamp(((Date) obj).getTime());
			throw new RuntimeException("Parameter :" + obj
					+ " is not Timestamp type.");
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Timestamp type.");
		}
	}

	public static int CompareAmtStr(String firstAmt, String secondAmt) {
		BigDecimal d1 = new BigDecimal(firstAmt);
		BigDecimal d2 = new BigDecimal(secondAmt);
		return d1.compareTo(d2);
	}

	/**
	 * 随机生成字符串
	 * 
	 */
	public static String getRandomCodeBylength(int length) {

		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

			if ("char".equalsIgnoreCase(charOrNum)) // 字符串
			{
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) // 数字
			{
				val += String.valueOf(random.nextInt(10));
			}
		}
		val = val.toLowerCase();
		return val;
	}
	/**
	 * 
	 * @Title: chcTimestampInTime 
	 * @Description: 检查Timestamp且格式为： yyyy-MM-dd HH:mm:ss
	 * @param @param obj
	 * @param @return
	 * @return Timestamp 
	 * @throws
	 */
	public static Timestamp chcTimestampInTime(Object obj) {
		try {
			if (obj == null || isBlank(obj.toString())) {
				return null;
			} else if (obj == null || isBlank(obj.toString()))
				return new Timestamp(Calendar.getInstance(timeZoneLondon)
						.getTimeInMillis());// modify by wq 2014-11-20
	
			if (obj instanceof java.lang.String)
				return DateUtil.getStrDateTOTimestampInTime(obj);
			if (obj instanceof Timestamp)
				return (Timestamp) obj;
			if (obj instanceof Date)
				return new Timestamp(((Date) obj).getTime());
			throw new RuntimeException("Parameter :" + obj
					+ " is not Timestamp type.");
		} catch (Exception e) {
			throw new RuntimeException("Parameter :" + obj
					+ " is not Timestamp type.");
		}
	}
	/**
	 * @param <T>
	 * 
	 * @Title: dealWithBigDecimal 
	 * @Description: 保留两位小数
	 * @param @param obj
	 * @param @return offest;
	 * @return  
	 * @throws
	 */
	public static  void dealWithBigDecimal(Object obj,int offest) {
		try {
			Class<?> c=obj.getClass();
			Field[] fields=c.getDeclaredFields();
			if(fields!=null){
				for(Field field:fields){
					if(field.getType()==BigDecimal.class){
						field.setAccessible(true);
						field.get(obj);
						field.set(obj,new BigDecimal(String.valueOf(field.get(obj))).setScale(offest,BigDecimal.ROUND_UP));
					}
				}
			}
		} catch (Exception e) {
		}
	}
	/**
	 * @TODO map检查，将key对应的null转为""
	 * @param map
	 * @return
	 */
	public static Map chkMapNotNullKeyVal(Map map) {
		Map param = new HashMap();
		try {
			if (ClassUtil.chkNull(map)) {
				return param;
			}
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			// 遍历map
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				String key = ClassUtil.chcString(entry.getKey());
				String value = ClassUtil.chcString(entry.getValue());
				param.put(key, value);
			}

		} catch (Exception e) {
			param = map;
		}
		return param;
	}
	
	/**
	 * 将list中的属性转换为大写
	 * @param list
	 * @return
	 */
	public static List<String> listToUpcase(List list){
		List<String> rsList = new ArrayList<String>();
		if(null != list && list.size()>0){
			for(int i=0;i<list.size();i++){
				rsList.add((list.get(i).toString().toUpperCase()));
			}
		}
		return rsList;
	}
	
	/**
	 * 1：将字符串中“-”替换为空 
	 * 2：并改用驼峰命名法
	 * @param sourStr
	 * @return
	 */
	public static String tfNames(String sourStr) {
		String resStr = "";
		String[] charStr = sourStr.split("_");
		for (int i = 0; i < charStr.length; i++) {
			// 首字母大写，其余小写
			String fr = charStr[i];
			resStr = resStr + (fr.substring(0,1)).toUpperCase()
					+ (fr.substring(1, fr.length())).toLowerCase();
		}
		return resStr;
	}
}
