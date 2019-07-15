package com.vission.mf.base.acf.bo;

/**
 * 
 * @author sweet uncle 列bo
 */
public class AcfColInfoBo {

	// 列名
	private String colName;
	// 是否主鍵
	private int checkIsPk = 0;// 1为主键
	// 是否為空
	private int checkIsNull = 0;// 1不能为空
	// 字段类型
	private String cloType;
	// 字段描述
	private String colDesc;
	// 字段名驼峰命名
	private String colTfNames;
	// 字段名大写
	private String colNameToUpcase;
	// 定义列名（字段名第一个字母小写）
	private String p_colTfNames;
	// 列中文名
	private String colChaName;

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public int getCheckIsPk() {
		return checkIsPk;
	}

	public void setCheckIsPk(int checkIsPk) {
		this.checkIsPk = checkIsPk;
	}

	public int getCheckIsNull() {
		return checkIsNull;
	}

	public void setCheckIsNull(int checkIsNull) {
		this.checkIsNull = checkIsNull;
	}

	public String getCloType() {
		return cloType;
	}

	public void setCloType(String cloType) {
		this.cloType = cloType;
	}

	public String getColTfNames() {
		return colTfNames;
	}

	public void setColTfNames(String colTfNames) {
		this.colTfNames = colTfNames;
	}

	public String getColNameToUpcase() {
		return colNameToUpcase;
	}

	public void setColNameToUpcase(String colNameToUpcase) {
		this.colNameToUpcase = colNameToUpcase;
	}

	public String getP_colTfNames() {
		return p_colTfNames;
	}

	public void setP_colTfNames(String p_colTfNames) {
		this.p_colTfNames = p_colTfNames;
	}

	public String getColDesc() {
		return colDesc;
	}

	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}

	public String getColChaName() {
		return colChaName;
	}

	public void setColChaName(String colChaName) {
		this.colChaName = colChaName;
	}

}
