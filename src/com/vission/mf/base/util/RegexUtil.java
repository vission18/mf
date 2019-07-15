package com.vission.mf.base.util;

import java.util.regex.Pattern;

public class RegexUtil {
	
	private static Pattern pattern;
	
	/**
	 * 判断某字符串是否与正则表达式匹配
	 * @param regex 正则表达式
	 * @param input 输入的字符串
	 * @return true | false
	 */
	public static boolean matching(String regex, String input){
		boolean flag = false;
		pattern = Pattern.compile(regex);
		if(pattern.matcher(input).find()){
			flag = true;
		}
		return flag;
	}
}
