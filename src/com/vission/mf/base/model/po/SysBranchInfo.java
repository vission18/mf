package com.vission.mf.base.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.SYS_BRANCH_INFO;

/**
* 功能/模块 ：系统机构实体类
*/
@Entity
@Table(name = SYS_BRANCH_INFO.TABLE_NAME)
public class SysBranchInfo extends BaseEntity {
	private static final long serialVersionUID = 6606541591347885266L;
	private String branchNo;
	private String branchName;
	private String branchShortName;
	private String upBranchNo;
	private SysBranchInfo upSysBranchInfo;
	private String upBranchName;
	private boolean isLeaf;
	@Id
	@Column(name = SYS_BRANCH_INFO.BRANCH_NO)
	public String getBranchNo() {
		return branchNo;
	}
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}
	@Column(name = SYS_BRANCH_INFO.BRANCH_NAME)
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	@Column(name = SYS_BRANCH_INFO.BRANCH_SHORTNAME)
	public String getBranchShortName() {
		return branchShortName;
	}
	public void setBranchShortName(String branchShortName) {
		this.branchShortName = branchShortName;
	}
	@Column(name = SYS_BRANCH_INFO.UP_BRANCH_NO)
	public String getUpBranchNo() {
		return upBranchNo;
	}
	public void setUpBranchNo(String upBranchNo) {
		this.upBranchNo = upBranchNo;
	}
	
	@Transient
	public SysBranchInfo getUpSysBranchInfo() {
		return upSysBranchInfo;
	}
	public void setUpSysBranchInfo(SysBranchInfo upSysBranchInfo) {
		this.upSysBranchInfo = upSysBranchInfo;
	}
	@Column(name = SYS_BRANCH_INFO.LEAF_FLAG)
	public boolean isLeaf() {
		return isLeaf;
	}
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	@Transient
	public String getUpBranchName() {
		if(this.getUpSysBranchInfo()!=null){
			return this.getUpSysBranchInfo().getBranchName();
		}else{
			return upBranchName;
		}
	}
	public void setUpBranchName(String upBranchName) {
		this.upBranchName = upBranchName;
	}
}
