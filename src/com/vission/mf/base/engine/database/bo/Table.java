package com.vission.mf.base.engine.database.bo;

public class Table {
	public String tableEname;
	public String tableCname;
	public String tableRmk;
	public String keyName;
	public String tableSpace;
	
	public Table(){}
	
	public Table(String tableEname, String tableCname, String tableRmk, String keyName, String tableSpace){
		this.tableEname = tableEname;
		this.tableCname = tableCname;
		this.tableRmk = tableRmk;
		this.keyName = keyName;
		this.tableSpace = tableSpace;
	}
	
	public String getTableEname() {
		return tableEname;
	}
	public void setTableEname(String tableEname) {
		this.tableEname = tableEname;
	}
	public String getTableCname() {
		return tableCname;
	}
	public void setTableCname(String tableCname) {
		this.tableCname = tableCname;
	}
	public String getTableRmk() {
		return tableRmk;
	}
	public void setTableRmk(String tableRmk) {
		this.tableRmk = tableRmk;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getTableSpace() {
		return tableSpace;
	}
	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}
	
}
