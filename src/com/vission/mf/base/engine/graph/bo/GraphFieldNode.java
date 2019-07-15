package com.vission.mf.base.engine.graph.bo;

/**
* 功能/模块 ：js画图
 */
public class GraphFieldNode extends GraphNode {
	
	private boolean connectable;
	private boolean primaryKey;
	private boolean autoIncrement;
	private String fieldType;
	
	public boolean isConnectable() {
		return connectable;
	}
	public void setConnectable(boolean connectable) {
		this.connectable = connectable;
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

}
