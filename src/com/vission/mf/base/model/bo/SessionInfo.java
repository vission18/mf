package com.vission.mf.base.model.bo;

import java.util.List;
import java.util.Map;

import com.vission.mf.base.model.po.SysMenuInfo;
import com.vission.mf.base.model.po.SysUserInfo;

public class SessionInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private SysUserInfo user;
	private String ip;
	private List<String> roleIds;//角色id列表
	private List<String> roleNames;//角色名称列表
	private List<String> menuUrls;//菜单权限 
	private List<SysMenuInfo> menus;//菜单列表
	private Map<String,List<String>> buttonsMap;//菜单对应的button
	private int isOnlyProject;		//标识是否只拥有单一项目。只拥有一个项目不显示可选择项目按钮
	private String sessionId;
	
	public Map<String, List<String>> getButtonsMap() {
		return buttonsMap;
	}

	public void setButtonsMap(Map<String, List<String>> buttonsMap) {
		this.buttonsMap = buttonsMap;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public SysUserInfo getUser() {
		return user;
	}

	public void setUser(SysUserInfo user) {
		this.user = user;
	}

	public List<String> getMenuUrls() {
		return menuUrls;
	}

	public void setMenuUrls(List<String> menuUrls) {
		this.menuUrls = menuUrls;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public List<String> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}

	public List<SysMenuInfo> getMenus() {
		return menus;
	}

	public void setMenus(List<SysMenuInfo> menus) {
		this.menus = menus;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getIsOnlyProject() {
		return isOnlyProject;
	}

	public void setIsOnlyProject(int isOnlyProject) {
		this.isOnlyProject = isOnlyProject;
	}
	
}
