package com.vission.mf.base.model.bo;

import java.util.List;

import com.vission.mf.base.model.po.SysBranchInfo;
/**
* 功能/模块 ：前端控件树封装
 */
public class BranchTree {
	private String id;
	private String text;//显示文本
	private String state;//状态 close,open
	private String parentId;//父id
	private String iconCls;//图标
	private boolean checked;//是否被选中
	private List<BranchTree> children;//子集	
	private String branchShortName;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	private boolean loaded;//是否被加载
	private int level;//级别
	
	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	public static String TREE_OPEN_STATE = "opened";
	public static String TREE_CLOSE_STATE = "closed";
	
	public BranchTree(SysBranchInfo branch) {
		id = branch.getBranchNo();
		text = branch.getBranchName();
		parentId = branch.getUpBranchNo();
		branchShortName=branch.getBranchShortName();
		state=TREE_CLOSE_STATE;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<BranchTree> getChildren() {
		return children;
	}
	public void setChildren(List<BranchTree> children) {
		this.children = children;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public String getBranchShortName() {
		return branchShortName;
	}

	public void setBranchShortName(String branchShortName) {
		this.branchShortName = branchShortName;
	}
}
