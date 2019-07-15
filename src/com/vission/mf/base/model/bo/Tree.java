package com.vission.mf.base.model.bo;

import java.util.List;

import com.vission.mf.base.model.po.SysBranchInfo;
import com.vission.mf.base.model.po.SysMenuInfo;
/**
* 功能/模块 ：前端控件树封装
 */
public class Tree implements Comparable<Tree>{
	private String id;
	private String text;//显示文本
	private String state;//状态 close,open
	private String parentId;//父id
	private String iconCls;//图标
	private boolean checked;//是否被选中
	private Attribute attributes;//扩展属性 
	private List<Tree> children;//子集	
	private int order;//排序
	private String url;
	public int getLevel() {
		return level;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	@SuppressWarnings("unused")
	private class Attribute{
		private String url;
		private String checkboxId;
		private String branchShortName;
		private String state;
		private boolean loaded;
		private String type;
		private boolean menuFlag;
		public boolean isMenuFlag() {
			return menuFlag;
		}

		public void setMenuFlag(boolean menuFlag) {
			this.menuFlag = menuFlag;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public boolean getLoaded() {
			return loaded;
		}

		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		public String getBranchShortName() {
			return branchShortName;
		}

		public void setBranchShortName(String branchShortName) {
			this.branchShortName = branchShortName;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getCheckboxId() {
			return checkboxId;
		}

		public void setCheckboxId(String checkboxId) {
			this.checkboxId = checkboxId;
		}

		public String getUrl() {
			return url;
		}
	}
	
	//根节点id
	public static final String ROOT_ID = "-1";
	
	public Tree(){
	};
	
	public Tree(SysMenuInfo menu) {
		id = menu.getMenuId();
		text = menu.getMenuName();
		attributes = new Attribute();
		attributes.setUrl(menu.getMenuUrl());
		iconCls= menu.getIconCls();
		parentId = menu.getParentMenuId();
		order = menu.getMenuOrder();
		attributes.setLoaded(false);
		attributes.setState(TREE_CLOSE_STATE);
		attributes.setMenuFlag(true);
	}
	
	public Tree(String checkboxId) {
		attributes = new Attribute();
		attributes.setCheckboxId(checkboxId);
	}
	public Tree(SysBranchInfo branch) {
		id = branch.getBranchNo();
		text = branch.getBranchName();
		parentId = branch.getUpBranchNo();
		attributes = new Attribute();
		attributes.setCheckboxId(branch.getBranchNo());
		attributes.setBranchShortName(branch.getBranchShortName());
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
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
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
	public Attribute getAttributes() {
		return attributes;
	}
	public void setAttributes(Attribute attributes) {
		this.attributes = attributes;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	/**
	 * 判断是否是第一级菜单
	 */
	public boolean isFirstNode(){
		return parentId==null||"".equals(parentId)||ROOT_ID.equals(parentId);
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int compareTo(Tree o) {
		return (this.getOrder() - ((Tree)o).getOrder());    //asc
	}
}
