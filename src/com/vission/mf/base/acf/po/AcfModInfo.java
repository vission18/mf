package com.vission.mf.base.acf.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.acf.db.ACF_MOD_INFO;

/**
 * 功能/模块 ：模块信息
 */
@Entity
@Table(name = ACF_MOD_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler",
		"fieldHandler" })
public class AcfModInfo extends BaseEntity {
	private static final long serialVersionUID = 7313403093642553160L;
	private String modId;
	private String modEngName;
	private String modChaName;
	private String delFlag;
	private String remark;
	private String createTime;
	private String createUser;
	private String lastModUser;
	private String lastModTime;

	private String colList;
	private String packageUrlIns;

	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = ACF_MOD_INFO.MOD_ID)
	public String getModId() {
		return modId;
	}

	public void setModId(String modId) {
		this.modId = modId;
	}

	@Column(name = ACF_MOD_INFO.MOD_ENG_NAME)
	public String getModEngName() {
		return modEngName;
	}

	public void setModEngName(String modEngName) {
		this.modEngName = modEngName;
	}

	@Column(name = ACF_MOD_INFO.MOD_CHA_NAME)
	public String getModChaName() {
		return modChaName;
	}

	public void setModChaName(String modChaName) {
		this.modChaName = modChaName;
	}

	@Column(name = ACF_MOD_INFO.DEL_FLAG)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Column(name = ACF_MOD_INFO.REMARK)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = ACF_MOD_INFO.CREATE_TIME)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = ACF_MOD_INFO.CREATE_USER)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = ACF_MOD_INFO.LAST_MOD_USER)
	public String getLastModUser() {
		return lastModUser;
	}

	public void setLastModUser(String lastModUser) {
		this.lastModUser = lastModUser;
	}

	@Column(name = ACF_MOD_INFO.LAST_MOD_TIME)
	public String getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(String lastModTime) {
		this.lastModTime = lastModTime;
	}

	@Transient
	public String getColList() {
		return colList;
	}

	public void setColList(String colList) {
		this.colList = colList;
	}

	@Transient
	public String getPackageUrlIns() {
		return packageUrlIns;
	}

	public void setPackageUrlIns(String packageUrlIns) {
		this.packageUrlIns = packageUrlIns;
	}

}