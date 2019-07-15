package com.vission.mf.base.acf.bo;

import java.util.List;

import com.vission.mf.base.util.ClassUtil;

/**
 * 
 * @author sweet uncle 不同类型文件命名Bo
 */
public class AcfTempFileNameBo {

	private String moudInitName;// 模块初始名
	private String moudDbName;// 模块db文件名
	private String moudPoName;// 模块Po文件名
	private String moudDaoName;// 模块Dao文件名
	private String moudServiceName;// 模块Service文件名
	private String moudControllerName;// 模块Controller文件名
	private String moudJspName;// 模块JSP页面文件名
	private String moudJsName;// 模块JS文件名
	private String moudLowerCaseName;// 模块小写名
	private String moudUpperCaseName;// 模块大写名
	private String moudExplainStr;// 模块解释
	private List<AcfColInfoBo> acnList;// 存放模块下面的列信息
	private String defVarName;// 用户变量名

	public String getMoudInitName() {
		return moudInitName;
	}

	public void setMoudInitName(String moudInitName) {
		this.moudInitName = moudInitName;
	}

	public String getMoudDbName() {
		return moudDbName;
	}

	public void setMoudDbName(String moudDbName) {
		this.moudDbName = moudDbName;
	}

	public String getMoudPoName() {
		return moudPoName;
	}

	public void setMoudPoName(String moudPoName) {
		this.moudPoName = moudPoName;
	}

	public String getMoudDaoName() {
		return moudDaoName;
	}

	public void setMoudDaoName(String moudDaoName) {
		this.moudDaoName = moudDaoName;
	}

	public String getMoudServiceName() {
		return moudServiceName;
	}

	public void setMoudServiceName(String moudServiceName) {
		this.moudServiceName = moudServiceName;
	}

	public String getMoudControllerName() {
		return moudControllerName;
	}

	public void setMoudControllerName(String moudControllerName) {
		this.moudControllerName = moudControllerName;
	}

	public String getMoudLowerCaseName() {
		return moudLowerCaseName;
	}

	public void setMoudLowerCaseName(String moudLowerCaseName) {
		this.moudLowerCaseName = moudLowerCaseName;
	}

	public String getMoudUpperCaseName() {
		return moudUpperCaseName;
	}

	public void setMoudUpperCaseName(String moudUpperCaseName) {
		this.moudUpperCaseName = moudUpperCaseName;
	}

	public String getMoudExplainStr() {
		return moudExplainStr;
	}

	public void setMoudExplainStr(String moudExplainStr) {
		this.moudExplainStr = moudExplainStr;
	}

	public List<AcfColInfoBo> getAcnList() {
		return acnList;
	}

	public void setAcnList(List<AcfColInfoBo> acnList) {
		this.acnList = acnList;
	}

	public String getMoudJspName() {
		return moudJspName;
	}

	public void setMoudJspName(String moudJspName) {
		this.moudJspName = moudJspName;
	}

	public String getMoudJsName() {
		return moudJsName;
	}

	public void setMoudJsName(String moudJsName) {
		this.moudJsName = moudJsName;
	}

	public String getDefVarName() {
		return defVarName;
	}

	public void setDefVarName(String defVarName) {
		this.defVarName = defVarName;
	}

	/**
	 * 初始化数据
	 * 
	 * @param moudInitName
	 */
	public void init(String moudInitName) {
		String moudName = ClassUtil.tfNames(moudInitName);
		this.setMoudUpperCaseName(moudName.toUpperCase());
		this.setMoudLowerCaseName(moudName.toLowerCase());
		this.setMoudControllerName(moudName + "Controller");
		this.setMoudDaoName(moudName + "Dao");
		this.setMoudServiceName(moudName + "Service");
		this.setMoudDbName(moudInitName.toUpperCase());
		this.setMoudPoName(moudName);
		//驼峰命名，首字母小写
		String tfVarName = moudName.substring(0,1).toLowerCase()+moudName.substring(1,moudName.length());
		this.setMoudJspName(tfVarName + "List.jsp");
		this.setMoudJsName(tfVarName + ".js");
		this.setDefVarName(tfVarName);
	}

}
