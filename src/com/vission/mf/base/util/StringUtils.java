package com.vission.mf.base.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 功能/模块 ： 字符串相关公用函数
 */

public class StringUtils {

	public static String conver(String sourceCharsetName,
			String targetCharsetName, String content) {
		if (content == null) {
			return null;
		}
		if ("".equals(content.trim())) {
			return content;
		}
		try {
			return new String(content.getBytes(sourceCharsetName),
					targetCharsetName);
		} catch (UnsupportedEncodingException e) {
			return null;
		}

	}

	public static String[] strConvertoArray(String str, String sgn) {
		StringTokenizer st = new StringTokenizer(str, sgn);
		String retstr[] = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++) {
			retstr[i] = st.nextToken();
		}
		return retstr;
	}

	public static List<String> strConvertoList(String str, String sgn) {

		List<String> list = new LinkedList<String>();
		if (str == null) {
			return list;
		}
		StringTokenizer st = new StringTokenizer(str, sgn);
		for (int i = 0; st.hasMoreTokens(); i++) {
			list.add(st.nextToken());
		}
		return list;
	}

	public static boolean isEmpty(String Str) {
		if (null == Str || "null".equals(Str.trim()) || "".equals(Str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static void setParamHash(String[] param,
			Map<String, String> parentMap) {
		int pos = 0;
		for (int i = 0; i < param.length; i++) {
			int num = param[i].indexOf("=", 0);
			if (num == -1) {
				return;
			}
			String key = param[i].substring(0, num);
			String value = param[i].substring(num + 1);
			parentMap.put(key, value);
			pos++;
		}
	}

	public static String getLocalName() {

		try {
			InetAddress inet = InetAddress.getLocalHost();
			return inet.getHostName();
		} catch (UnknownHostException e) {

		}
		return "";
	}
	
	public static String addString(String flag,int num){
		String result="";
		for(int i=0;i<num;i++){
			result=result+flag;
		}
		return result;
	}

}
