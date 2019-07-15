package com.vission.mf.base.enums.db;

import java.util.LinkedHashMap;
import java.util.Map;

/**
* 功能/模块 ：操作日志表
*/
public class SYS_OPERLOG_INFO {
	//库表信息
	public static final String TABLE_NAME = "SYS_OPERLOG_INFO";
	public static final String LOG_ID = "LOG_ID";
	public static final String LOGIN_NAME = "LOGIN_NAME";
	public static final String LOGIN_IP = "LOGIN_IP";
	public static final String OPER_TIME = "OPER_TIME";
	public static final String OPER_CONTENT = "OPER_CONTENT";
	public static final String OPER_TYPE = "OPER_TYPE";
	
	//栏位常量值
	public static final String LOGIN_SYS_SYSTEM="SYS_SYSTEM_LOGIN";//登录系统
	
	public static final String UPDATE_SYS_PASSWORD="SYS_PASSWORD_UPDATE";//修改密码
	
	public static final String ISSUE_REPORT="ISSUE_REPORT";//发布报表	
	public static final String VISIT_REPORT="VISIT_REPORT";//访问报表
	public static final String DELETE_REPORT="DELETE_REPORT";//删除报表
	public static final String UPDATE_REPORT="UPDATE_REPORT";//更新报表
	
	public static final String INSERT_SYS_USER="SYS_USER_INSERT";//新增用户
	public static final String UPDATE_SYS_USER="SYS_USER_UPDATE";//修改用户
	public static final String DELETE_SYS_USER="SYS_USER_DELETE";//删除用户
	
	public static final String INSERT_SYS_ROLE="SYS_ROLE_INSERT";//新增角色
	public static final String UPDATE_SYS_ROLE="SYS_ROLE_UPDATE";//修改角色
		public static final String DELETE_SYS_ROLE="SYS_ROLE_DELETE";//删除角色
	
	public static final String INSERT_SYS_MENU="SYS_MENU_INSERT";//新增菜单
	public static final String UPDATE_SYS_MENU="SYS_MENU_UPDATE";//修改菜单
		public static final String DELETE_SYS_MENU="SYS_MENU_DELETE";//删除菜单
	
	public static final String INSERT_SYS_PUBCODE="SYS_PUBCODE_INSERT";//新增代码
	public static final String UPDATE_SYS_PUBCODE="SYS_PUBCODE_UPDATE";//修改代码
		public static final String DELETE_SYS_PUBCODE="SYS_PUBCODE_DELETE";//删除代码
		
		public static final String INSERT_SYS_OLAP="SYS_PUBCODE_INSERT";//新增多维分析
		public static final String UPDATE_SYS_OLAP="SYS_PUBCODE_UPDATE";//修改多维分析
			public static final String DELETE_SYS_OLAP="SYS_PUBCODE_DELETE";//删除多维分析
	
	public static final String INSERT_SYS_BRANCHINFO="SYS_BRANCHINFO_INSERT";//新增机构
	public static final String UPDATE_SYS_BRANCHINFO="SYS_BRANCHINFO_UPDATE";//修改机构
		public static final String DELETE_SYS_BRANCHINFO="SYS_BRANCHINFO_DELETE";//删除机构
	
	public static final String INSERT_IMS_IMSDBINFO="IMS_IMSDBINFO_INSERT";//新增数据源
	public static final String UPDATE_IMS_IMSDBINFO="IMS_IMSDBINFO_UPDATE";//修改数据源
		public static final String DELETE_IMS_IMSDBINFO="IMS_IMSDBINFO_DELETE";//删除数据源
	
	public static final String INSERT_IMS_IMSPROJECTINFO="IMS_IMSPROJECTINFO_INSERT";//新增工程
	public static final String UPDATE_IMS_IMSPROJECTINFO="IMS_IMSPROJECTINFO_UPDATE";//修改工程
		public static final String DELETE_IMS_IMSPROJECTINFO="IMS_IMSPROJECTINFO_DELETE";//删除工程
	
	public static final String INSERT_IMS_IMSUSERPROJECTINFO="IMS_IMSUSERPROJECTINFO_INSERT";//新增工程用户
	public static final String UPDATE_IMS_IMSUSERPROJECTINFO="IMS_IMSUSERPROJECTINFO_UPDATE";//修改工程用户
		public static final String DELETE_IMS_IMSUSERPROJECTINFO="IMS_IMSUSERPROJECTINFO_DELETE";//删除工程用户

		public static final String INSERT_SYS_OLAPDIR="SYS_OLAPDIR_INSERT";//新增多维分析类型
		public static final String UPDATE_SYS_OLAPDIR="SYS_OLAPDIR_UPDATE";//修改多维分析类型
		public static final String DELETE_SYS_OLAPDIR="SYS_OLAPDIR_DELETE";//删除多维分析类型
		
	public static final String INSERT_SYS_SYSANNOINFO="SYS_SYSANNOINFO_INSERT";//新增公告
	public static final String UPDATE_SYS_SYSANNOINFO="SYS_SYSANNOINFO_UPDATE";//修改公告
	public static final String DELETE_SYS_SYSANNOINFO="SYS_SYSANNOINFO_DELETE";//删除公告
	
	public static final String INSERT_SYS_SYSANNOTYPEINFO="SYS_SYSANNOTYPEINFO_INSERT";//新增公告类型
	public static final String UPDATE_SYS_SYSANNOTYPEINFO="SYS_SYSANNOTYPEINFO_UPDATE";//修改公告类型
	public static final String DELETE_SYS_SYSANNOTYPEINFO="SYS_SYSANNOTYPEINFO_DELETE";//删除公告类型
	
	public static final String INSERT_SYS_SYSPROJECTANNOINFO="SYS_SYSPROJECTANNOINFO_INSERT";//新增公告工程
	public static final String UPDATE_SYS_SYSPROJECTANNOINFO="SYS_SYSPROJECTANNOINFO_UPDATE";//修改公告工程
	public static final String DELETE_SYS_SYSPROJECTANNOINFO="SYS_SYSPROJECTANNOINFO_DELETE";//删除公告工程
	
	public static final String INSERT_IMS_IMSINDMEASUREINFO="IMS_IMSINDMEASUREINFO_INSERT";//新增度量
	public static final String UPDATE_IMS_IMSINDMEASUREINFO="IMS_IMSINDMEASUREINFO_UPDATE";//修改度量
	public static final String DELETE_IMS_IMSINDMEASUREINFO="IMS_IMSINDMEASUREINFO_DELETE";//删除度量
	
	public static final String INSERT_IND_INDCATALOGINFO="IND_INDCATALOGINFO_INSERT";//新增指标体系
	public static final String UPDATE_IND_INDCATALOGINFO="IND_INDCATALOGINFO_UPDATE";//修改指标体系
	public static final String DELETE_IND_INDCATALOGINFO="IND_INDCATALOGINFO_DELETE";//删除指标体系
	
	public static final String INSERT_SYS_USERADVICEINFO="SYS_USERADVICEINFO_INSERT";//新增指标体系
	public static final String DELETE_SYS_USERADVICEINFO="SYS_USERADVICEINFO_DELETE";//删除用户建议
	
	public static final Map<String,String> operTypeMap = new LinkedHashMap<String,String>();
	static {
		operTypeMap.put("SYS_SYSTEM_LOGIN","登录系统");
		operTypeMap.put("SYS_PASSWORD_UPDATE","修改密码");
		operTypeMap.put("SYS_USER_INSERT","新增用户");
		operTypeMap.put("SYS_USER_UPDATE","修改用户");
		operTypeMap.put("SYS_USER_DELETE","删除用户");

		operTypeMap.put("SYS_ROLE_INSERT","新增角色");
		operTypeMap.put("SYS_ROLE_UPDATE","修改角色");
		operTypeMap.put("SYS_ROLE_DELETE","删除角色");

		operTypeMap.put("SYS_MENU_INSERT","新增菜单");
		operTypeMap.put("SYS_MENU_UPDATE","修改菜单");
		operTypeMap.put("SYS_MENU_DELETE","删除菜单");

		operTypeMap.put("SYS_PUBCODE_INSERT","新增代码");
		operTypeMap.put("SYS_PUBCODE_UPDATE","修改代码");
		operTypeMap.put("SYS_PUBCODE_DELETE","删除代码");

		operTypeMap.put("SYS_BRANCHINFO_INSERT","新增机构");
		operTypeMap.put("SYS_BRANCHINFO_UPDATE","修改机构");
		operTypeMap.put("SYS_BRANCHINFO_DELETE","删除机构");

		operTypeMap.put("IMS_IMSPROJECTINFO_INSERT", "新增Project");
		operTypeMap.put("IMS_IMSUSERPROJECTINFO_UPDATE", "修改Project");
		operTypeMap.put("IMS_IMSUSERPROJECTINFO_DELETE", "删除Project");		
	}
}