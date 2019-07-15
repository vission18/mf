package com.vission.mf.base.model.po;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.SYS_MENU_ROLE_REL;
import com.vission.mf.base.enums.db.SYS_ROLE_INFO;

/**
 * 功能/模块 ：系统角色实体类
 */
@Entity
@Table(name = SYS_ROLE_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler",
		"fieldHandler" })
public class SysRoleInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String roleId;
	private String roleName;
	private String roleRmk;

	/* private List<SysUserInfo> users = new ArrayList<SysUserInfo>(); */
	private List<SysMenuInfo> menus = new LinkedList<SysMenuInfo>();
	
	private String menuIds;
	private String menuNames;

	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_ROLE_INFO.ROLE_ID)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = SYS_ROLE_INFO.ROLE_NAME)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = SYS_ROLE_INFO.ROLE_RMK)
	public String getRoleRmk() {
		return roleRmk;
	}

	public void setRoleRmk(String roleRmk) {
		this.roleRmk = roleRmk;
	}

	/*
	 * @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE}, mappedBy
	 * = "roles")
	 * 
	 * @JsonIgnore public List<SysUserInfo> getUsers() { return users; } public
	 * void setUsers(List<SysUserInfo> users) { this.users = users; }
	 */

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = SYS_MENU_ROLE_REL.TABLE_NAME, joinColumns = { @JoinColumn(name = SYS_MENU_ROLE_REL.ROLE_ID) }, inverseJoinColumns = { @JoinColumn(name = SYS_MENU_ROLE_REL.MENU_ID) })
	@JsonIgnore
	public List<SysMenuInfo> getMenus() {
		return menus;
	}

	public void setMenus(List<SysMenuInfo> menu) {
		this.menus = menu;
	}

	@Transient
	public String getMenuIds() {
		StringBuffer sb = new StringBuffer();
		for (SysMenuInfo s : menus) {
			sb.append(s.getMenuId()).append(",");
		}
		if (sb.toString().indexOf(",") >= 0) {
			sb = sb.deleteCharAt(sb.lastIndexOf(","));
		}
		menuIds = sb.toString();
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	@Transient
	public String getMenuNames() {
		StringBuffer sb = new StringBuffer();
		for (SysMenuInfo s : menus) {
			sb.append(s.getMenuName()).append(",");
		}
		if (sb.toString().indexOf(",") >= 0) {
			sb = sb.deleteCharAt(sb.lastIndexOf(","));
		}
		menuNames = sb.toString();
		return menuNames;
	}

	public void setMenuNames(String menuNames) {
		this.menuNames = menuNames;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysRoleInfo other = (SysRoleInfo) obj;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		return true;
	}

}
