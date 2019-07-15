package com.vission.mf.base.model.bo;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class DataGrid {

	private int total = 0;
	private int startRow = 1;
	
	private List columns = new ArrayList();
	private List rows = new ArrayList();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public List getColumns() {
		return columns;
	}

	public void setColumns(List columns) {
		this.columns = columns;
	}
	
}
