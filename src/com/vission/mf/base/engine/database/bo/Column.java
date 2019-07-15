package com.vission.mf.base.engine.database.bo;

public class Column {
	public String columnEname;
	public String columnCname;
	public String columnRmk;
	public int columnNo;
	public String columnType;
	public int columnLen;
	public int columnPre;
	public boolean keyFlag;
	public boolean nullFlag;
	public String defaultValue;
	
	public Column(){}
	
	public Column(String columnEname, String columnCname, 
			String columnRmk, int columnNo, String columnType, int columnLen,
			int columnPre, boolean keyFlag, boolean nullFlag, String defaultValue){
		this.columnEname = columnEname;
		this.columnCname = columnCname;
		this.columnRmk = columnRmk;
		this.columnNo = columnNo;
		this.columnType = columnType;
		this.columnLen = columnLen;
		this.columnPre = columnPre;
		this.keyFlag = keyFlag;
		this.nullFlag = nullFlag;
		this.defaultValue = defaultValue;
	}

	public String getColumnEname() {
		return columnEname;
	}

	public void setColumnEname(String columnEname) {
		this.columnEname = columnEname;
	}

	public String getColumnCname() {
		return columnCname;
	}

	public void setColumnCname(String columnCname) {
		this.columnCname = columnCname;
	}

	public String getColumnRmk() {
		return columnRmk;
	}

	public void setColumnRmk(String columnRmk) {
		this.columnRmk = columnRmk;
	}

	public int getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public int getColumnLen() {
		return columnLen;
	}

	public void setColumnLen(int columnLen) {
		this.columnLen = columnLen;
	}

	public int getColumnPre() {
		return columnPre;
	}

	public void setColumnPre(int columnPre) {
		this.columnPre = columnPre;
	}

	public boolean isKeyFlag() {
		return keyFlag;
	}

	public void setKeyFlag(boolean keyFlag) {
		this.keyFlag = keyFlag;
	}

	public boolean isNullFlag() {
		return nullFlag;
	}

	public void setNullFlag(boolean nullFlag) {
		this.nullFlag = nullFlag;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
