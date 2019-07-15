package com.vission.mf.base.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.SYS_USER_INFO;

/**
* 功能/模块 ：系统用户实体类
*/
@Entity
@Table(name = SYS_USER_INFO.TABLE_NAME)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class SysUserInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	/* 数据库字段 */
	private String userId;
	private String loginName;
	private String loginPassword;
	private String userName;
	private String userEmail;
	private boolean userStatus;
	private String branchNo;
	private String branchName;
	private String userTel;
	private String userMobTel;
	private String userType;
	private String loginIp;
	
	/* 临时使用 */
	private String newPassword;//新密码
	private String confirmPassword;//确认新密码
	private String cryptPassword;//内部加密密码
	private boolean projectManager;//项目管理员

	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_USER_INFO.USER_ID)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = SYS_USER_INFO.LOGIN_NAME, updatable = false)
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = SYS_USER_INFO.LOGIN_PWD)
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	@Column(name = SYS_USER_INFO.USER_NAME)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = SYS_USER_INFO.USER_EMAIL)
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Column(name = SYS_USER_INFO.USER_STATUS)
	public boolean isUserStatus() {
		return userStatus;
	}
	public void setUserStatus(boolean userStatus) {
		this.userStatus = userStatus;
	}

	@Column(name = SYS_USER_INFO.BRANCH_NO)
	public String getBranchNo() {
		return branchNo;
	}
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	@Column(name = SYS_USER_INFO.USER_TEL)
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	@Column(name = SYS_USER_INFO.USER_MOB_TEL)
	public String getUserMobTel() {
		return userMobTel;
	}
	public void setUserMobTel(String userMobTel) {
		this.userMobTel = userMobTel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
		result = prime * result + ((loginName == null) ? 0 : loginName.hashCode());
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
		final SysUserInfo other = (SysUserInfo) obj;
		if (this.getUserId() == null) {
			if (other.getUserId() != null)
				return false;
		} else if (!getUserId().equals(other.getUserId()))
			return false;
		if (loginName == null) {
			if (other.loginName != null)
				return false;
		} else if (!loginName.equals(other.loginName))
			return false;
		return true;
	}

	@Transient
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Transient
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	@Transient
	public boolean isProjectManager() {
		return projectManager;
	}
	public void setProjectManager(boolean projectManager) {
		this.projectManager = projectManager;
	}
	@Transient
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	@Column(name = SYS_USER_INFO.USER_TYPE)
	public String getUserType() {
		return userType;
	}
	
	@Transient
	public String getCryptPassword() {
		return cryptPassword;
	}
	
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public void setCryptPassword(String cryptPassword) {
		this.cryptPassword = cryptPassword;
	}
	
	@Transient
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
}
