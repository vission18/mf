package com.vission.mf.base.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseConstants {
	
	/**
	 * The name of the ResourceBundle used in this application
	 */
	public static final String BUNDLE_KEY = "ApplicationResources";

	//不分页时最多显示的记录数
	public static final int MAX_PAGE_SIZE = 200;

	// 超级用户名,判断是超级用户时不能删除,不受权限控制等等
	//存放在session中的属性名
	public static final String USER_SESSION_KEY = "USER_SESSION_KEY"; 
	
	//超级用户名,判断是超级用户时不能删除,不受权限控制等等
	public static final String SUPER_USER = "admin";

	// 操作类型
	public static final String OPER_TYPE_INSERT = "insert";
	public static final String OPER_TYPE_UPDATE = "update";
	public static final String OPER_TYPE_DELETE = "delete";
	
	public static Map<String, String> pubCodeMap = new HashMap<String, String>();
	
	/* 系统公共配置 */
	public static final String SYSTEM_SYSLOGO = "system.sysLogo";
	public static final String SYSTEM_COPYRIGHT = "system.copyRight";
	public static final String SYSTEM_DATAPATTER = "system.datePatter";
	public static final String SYSTEM_HELP_FILE = "system.helpfile";
	public static final String SYSTEM_CACHE_FLAG = "system.cache.flag";
	public static final String SYSTEM_BROWSER_TITLE = "system.browser.title";
	public static final String SYSTEM_TAB_MAX_NUM = "system.tab.maxnum";
	/* 报表导出类型 */
	public static final String REPORT_EXPORT_TYPE = "report.export.type";
	public static final String EXCEL_TEMPLATE_PATH = "excel.template.path";//模版存储绝对路径
	
	public static final String SYSTEM_STYLE_PATH = "system.style.path";//样式保存路径
	
	//左侧树宽度
	public static final String IMS_LEFT_WIDTH  = "ims.left.width";

	//版权文字
	public static String getSystemCopyRight() {
		return getWithDefault(SYSTEM_COPYRIGHT, "*******");
	}
	
	//帮助文件存储目录
	public static String getHelpFilePath() {
		return getWithDefault(SYSTEM_HELP_FILE, "");
	}
	
	//浏览器标题
	public static String getBrowserTitle() {
		return getWithDefault(SYSTEM_BROWSER_TITLE, "欢迎使用基础框架");
	}
	
	public static final String getWithDefault(String code, String defaultValue) {
		String value = pubCodeMap.get(code);
		if (value == null || value.trim().equals("")) {
			return defaultValue;
		}
		return value;
	}
	
	//是否缓存
	public static final boolean isCache() {
		String value = getWithDefault(SYSTEM_CACHE_FLAG, "");
		if("true".equalsIgnoreCase(value)){
			return true;
		}else{
			return false;
		}
	}
	
	//允许同时打开的tab个数
	public static int getTabMaxNum(){
		return Integer.parseInt(getWithDefault(SYSTEM_TAB_MAX_NUM, "5"));
	}
	
	//平台左侧树宽度
	public static String getLeftWidth() {
		return getWithDefault(IMS_LEFT_WIDTH, "225");
	}
	
	//模版存储绝对路径
	public static String getExcelTemplatePath() {
		return getWithDefault(EXCEL_TEMPLATE_PATH, "/xls/");
	}
	public static List<String> getReportExportType() {
		return Arrays.asList(getWithDefault(REPORT_EXPORT_TYPE, "").split(","));
	}
	//css\js\image路径
	public static String getStylePath(){
		return getWithDefault(SYSTEM_STYLE_PATH, "/mf");
	}
}
