package com.vission.mf.base.engine.database.bo;

public class Index {
	public String indexName;
	public String indexRmk;
	public boolean uniqueFlag;
	public String indexColumns;
	
	public Index(){}
	
	public Index(String indexName, String indexRmk, boolean uniqueFlag, String indexColumns){
		this.indexName = indexName;
		this.indexRmk = indexRmk;
		this.uniqueFlag = uniqueFlag;
		this.indexColumns = indexColumns;
	}	

	public String getIndexRmk() {
		return indexRmk;
	}
	public void setIndexRmk(String indexRmk) {
		this.indexRmk = indexRmk;
	}
	public boolean isUniqueFlag() {
		return uniqueFlag;
	}
	public void setUniqueFlag(boolean uniqueFlag) {
		this.uniqueFlag = uniqueFlag;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getIndexColumns() {
		return indexColumns;
	}
	public void setIndexColumns(String indexColumns) {
		this.indexColumns = indexColumns;
	} 
	
}
