package com.vission.mf.base.model.bo;

/**
* 功能/模块 ：页面对应机构view
*/
@SuppressWarnings("serial")
public class TSysBranchInfo implements java.io.Serializable{
	private String branchNo;
	private String branchName;
	private String branchShortName;
	private String upBranchName;
	private String upBranchNo;
	private boolean leaf;
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getBranchNo() {
		return branchNo;
	}
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchShortName() {
		return branchShortName;
	}
	public void setBranchShortName(String branchShortName) {
		this.branchShortName = branchShortName;
	}
	public String getUpBranchName() {
		return upBranchName;
	}
	public void setUpBranchName(String upBranchName) {
		this.upBranchName = upBranchName;
	}
	public String getUpBranchNo() {
		return upBranchNo;
	}
	public void setUpBranchNo(String upBranchNo) {
		this.upBranchNo = upBranchNo;
	}
	
}
