package com.vission.mf.base.acf.po;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.acf.db.ACF_COL_INFO;

/**
 * 功能/模块 ：模块信息
 */
@Entity
@Table(name = ACF_COL_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler",
		"fieldHandler" })
public class AcfColInfo extends BaseEntity {
	private static final long serialVersionUID = 6109405603368438206L;
	private String colId;
	private String modId;
	private String colEngName;
	private String colChaName;
	private String colType;
	private String defaultVal;
	private String colDesc;
	private String isPk;
	private String isNull;
	private String delFlag;
	private String lastModUser;
	private String lastModTime;

	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = ACF_COL_INFO.COL_ID)
	public String getColId() {
		return colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	@Column(name = ACF_COL_INFO.MOD_ID)
	public String getModId() {
		return modId;
	}

	public void setModId(String modId) {
		this.modId = modId;
	}

	@Column(name = ACF_COL_INFO.COL_ENG_NAME)
	public String getColEngName() {
		return colEngName;
	}

	public void setColEngName(String colEngName) {
		this.colEngName = colEngName;
	}

	@Column(name = ACF_COL_INFO.COL_CHA_NAME)
	public String getColChaName() {
		return colChaName;
	}

	public void setColChaName(String colChaName) {
		this.colChaName = colChaName;
	}

	@Column(name = ACF_COL_INFO.COL_TYPE)
	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	@Column(name = ACF_COL_INFO.DEFAULT_VAL)
	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	@Column(name = ACF_COL_INFO.COL_DESC)
	public String getColDesc() {
		return colDesc;
	}

	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}

	@Column(name = ACF_COL_INFO.IS_PK)
	public String getIsPk() {
		return isPk;
	}

	public void setIsPk(String isPk) {
		this.isPk = isPk;
	}

	@Column(name = ACF_COL_INFO.IS_NULL)
	public String getIsNull() {
		return isNull;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	@Column(name = ACF_COL_INFO.DEL_FLAG)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Column(name = ACF_COL_INFO.LAST_MOD_USER)
	public String getLastModUser() {
		return lastModUser;
	}

	public void setLastModUser(String lastModUser) {
		this.lastModUser = lastModUser;
	}

	@Column(name = ACF_COL_INFO.LAST_MOD_TIME)
	public String getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(String lastModTime) {
		this.lastModTime = lastModTime;
	}

}