package com.vission.mf.base.model.bo;

public class ExportInfo {
	private String exportType;
	private String createSql;
	private String dropSql;
	private String indexSql;
	private String forkeySql;
	private String tablespaceSql;
	private String exportTables;
	private String expTables;//导出表范围　1-选择的表  0-数据区下所有表
	private String areaId;//数据区ID
	
	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	public String getCreateSql() {
		return createSql;
	}
	public void setCreateSql(String createSql) {
		this.createSql = createSql;
	}
	public String getDropSql() {
		return dropSql;
	}
	public void setDropSql(String dropSql) {
		this.dropSql = dropSql;
	}
	public String getIndexSql() {
		return indexSql;
	}
	public void setIndexSql(String indexSql) {
		this.indexSql = indexSql;
	}
	public String getForkeySql() {
		return forkeySql;
	}
	public void setForkeySql(String forkeySql) {
		this.forkeySql = forkeySql;
	}
	public String getTablespaceSql() {
		return tablespaceSql;
	}
	public void setTablespaceSql(String tablespaceSql) {
		this.tablespaceSql = tablespaceSql;
	}
	public String getExportTables() {
		return exportTables;
	}
	public void setExportTables(String exportTables) {
		this.exportTables = exportTables;
	}
	public String getExpTables() {
		return expTables;
	}
	public void setExpTables(String expTables) {
		this.expTables = expTables;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

}
