package com.vission.mf.base.engine.database.bo;

public class Forkey {
	public String forkeyName;
	public String forkeyRmk;
	public int updateRule;
	public int deleteRule;
	public int deferrability;
	public String fkTableName;
	public String fkColumns;
	
	public Forkey(){}
	
	public Forkey(String forkeyName, String forkeyRmk, int updateRule, int deleteRule, int deferrability, String fkTableName, String fkColumns){
		this.forkeyName = forkeyName;
		this.forkeyRmk = forkeyRmk;
		this.updateRule = updateRule;
		this.deleteRule = deleteRule;
		this.deferrability = deferrability;
		this.fkTableName = fkTableName;
		this.fkColumns = fkColumns;
	}
	
	public String getForkeyName() {
		return forkeyName;
	}
	public void setForkeyName(String forkeyName) {
		this.forkeyName = forkeyName;
	}
	public String getForkeyRmk() {
		return forkeyRmk;
	}
	public void setForkeyRmk(String forkeyRmk) {
		this.forkeyRmk = forkeyRmk;
	}
	public int getUpdateRule() {
		return updateRule;
	}
	public void setUpdateRule(int updateRule) {
		this.updateRule = updateRule;
	}
	public int getDeleteRule() {
		return deleteRule;
	}
	public void setDeleteRule(int deleteRule) {
		this.deleteRule = deleteRule;
	}
	public int getDeferrability() {
		return deferrability;
	}
	public void setDeferrability(int deferrability) {
		this.deferrability = deferrability;
	}

	public String getFkTableName() {
		return fkTableName;
	}

	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}

	public String getFkColumns() {
		return fkColumns;
	}

	public void setFkColumns(String fkColumns) {
		this.fkColumns = fkColumns;
	}
	
}
