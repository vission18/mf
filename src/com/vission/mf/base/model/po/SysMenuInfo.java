package com.vission.mf.base.model.po;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.enums.db.SYS_MENU_INFO;

/**
* 功能/模块 ：系统菜单实体类
*/
@Entity
@Table(name = SYS_MENU_INFO.TABLE_NAME)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class SysMenuInfo {
	private String menuId;
	private String menuName;
	private String menuUrl;
	private boolean newOpen;
	private String parentMenuId;
	private int menuOrder;
	private String menuType;
	private String iconCls;//图标
	
	private String actions;//菜单对应url
	private String buttonType;//按钮类型

	private List<SysRoleInfo> roles = new LinkedList<SysRoleInfo>();
	
	private String parentMenuName;
	
	public SysMenuInfo(){
	}
	
	@Column(name = SYS_MENU_INFO.MENU_ICONCLS)
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public SysMenuInfo(String menuId){
		this.menuId = menuId;
	}
	
	@Id
	@GeneratedValue(generator = "uuid-gen") 
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_MENU_INFO.MENU_ID)
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	@Column(name = SYS_MENU_INFO.MENU_URL)
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	@Column(name = SYS_MENU_INFO.MENU_ORDER)
	public int getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}

	@Column(name = SYS_MENU_INFO.MENU_TYPE)
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	@Column(name = SYS_MENU_INFO.MENU_NAME)
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@Column(name = SYS_MENU_INFO.NEW_OPEN)
	public boolean isNewOpen() {
		return newOpen;
	}
	public void setNewOpen(boolean newOpen) {
		this.newOpen = newOpen;
	}

	@Column(name = SYS_MENU_INFO.PARENT_ID)
	public String getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	@Transient
	public String getParentMenuName() {
		return parentMenuName;
	}
	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "menus")
	/*@JoinTable(name = SYS_MENU_ROLE_REL.TABLE_NAME, joinColumns = { @JoinColumn(name = SYS_MENU_ROLE_REL.MENU_ID) }, inverseJoinColumns = { @JoinColumn(name = SYS_MENU_ROLE_REL.ROLE_ID) })*/
	@JsonIgnore
	public List<SysRoleInfo> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRoleInfo> roles) {
		this.roles = roles;
	}

	@Column(name = SYS_MENU_INFO.MENU_ACTIONS)
		public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	@Column(name = SYS_MENU_INFO.BUTTON_TYPE)
	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}
}
