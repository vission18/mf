package com.vission.mf.base.sellms.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.sellms.model.db.SMS_CUST_INFO;

/**
 * 功能/模块 ：客户信息
 */
@Entity
@Table(name = SMS_CUST_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler",
		"fieldHandler" })
public class SmsCustInfo extends BaseEntity {
	private static final long serialVersionUID = 7313203093642553160L;
	private String custId;
	private String custName;
	private String custAliasName;
	private String mobile;
	private String telPhone;
	private String address;
	private String custType;
	private String custDesc;
	private String custStatus;
	private String dataBy;
	private String createUser;
	private String createTime;
	private String lastModUser;
	private String lastModTime;

	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SMS_CUST_INFO.CUST_ID)
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	@Column(name = SMS_CUST_INFO.CUST_NAME)
	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	@Column(name = SMS_CUST_INFO.CUST_ALIAS_NAME)
	public String getCustAliasName() {
		return custAliasName;
	}

	public void setCustAliasName(String custAliasName) {
		this.custAliasName = custAliasName;
	}

	@Column(name = SMS_CUST_INFO.MOBILE)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = SMS_CUST_INFO.TEL_PHONE)
	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	@Column(name = SMS_CUST_INFO.ADDRESS)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = SMS_CUST_INFO.CUST_TYPE)
	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	@Column(name = SMS_CUST_INFO.CUST_DESC)
	public String getCustDesc() {
		return custDesc;
	}

	public void setCustDesc(String custDesc) {
		this.custDesc = custDesc;
	}

	@Column(name = SMS_CUST_INFO.CUST_STATUS)
	public String getCustStatus() {
		return custStatus;
	}

	public void setCustStatus(String custStatus) {
		this.custStatus = custStatus;
	}

	@Column(name = SMS_CUST_INFO.DATA_BY)
	public String getDataBy() {
		return dataBy;
	}

	public void setDataBy(String dataBy) {
		this.dataBy = dataBy;
	}

	@Column(name = SMS_CUST_INFO.CREATE_USER)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = SMS_CUST_INFO.CREATE_TIME)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = SMS_CUST_INFO.LAST_MOD_USER)
	public String getLastModUser() {
		return lastModUser;
	}

	public void setLastModUser(String lastModUser) {
		this.lastModUser = lastModUser;
	}

	@Column(name = SMS_CUST_INFO.LAST_MOD_TIME)
	public String getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(String lastModTime) {
		this.lastModTime = lastModTime;
	}
}