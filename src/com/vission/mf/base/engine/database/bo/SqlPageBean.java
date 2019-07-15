package com.vission.mf.base.engine.database.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 功能/模块 ： 
 */
@SuppressWarnings("serial")
public class SqlPageBean implements Serializable {
	private int count;
	private int startRow;
	private List<Map<String,String>> datas;
	
	public List<Map<String, String>> getDatas() {
		return datas;
	}
	public void setDatas(List<Map<String, String>> datas) {
		this.datas = datas;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
}
