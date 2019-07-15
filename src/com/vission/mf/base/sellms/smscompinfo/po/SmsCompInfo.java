package com.vission.mf.base.sellms.smscompinfo.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.sellms.smscompinfo.db.SMS_COMP_INFO;

/**
 * 作者：lkj 描述：SMS_COMP_INFO 模块Po 日期：2019-6-21 9:29:04 类型：Po文件
 */

@Entity
@Table(name = SMS_COMP_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler",
		"fieldHandler" })
public class SmsCompInfo {

	private static final long serialVersionUID = 1L;

	// 主键
	private String pkId;

	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SMS_COMP_INFO.PK_ID, nullable = false)
	public String getPkId() {
		return pkId;
	}

	public void setPkId(String pkId) {
		this.pkId = pkId;
	}

	// 行业分类
	private String busiType;

	@Column(name = SMS_COMP_INFO.BUSI_TYPE)
	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	// 公司名称
	private String compName;

	@Column(name = SMS_COMP_INFO.COMP_NAME)
	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	// 公司法人
	private String compLegal;

	@Column(name = SMS_COMP_INFO.COMP_LEGAL)
	public String getCompLegal() {
		return compLegal;
	}

	public void setCompLegal(String compLegal) {
		this.compLegal = compLegal;
	}

	// 注册资金
	private String compMoney;

	@Column(name = SMS_COMP_INFO.COMP_MONEY)
	public String getCompMoney() {
		return compMoney;
	}

	public void setCompMoney(String compMoney) {
		this.compMoney = compMoney;
	}

	// 成立日期
	private String compDate;

	@Column(name = SMS_COMP_INFO.COMP_DATE)
	public String getCompDate() {
		return compDate;
	}

	public void setCompDate(String compDate) {
		this.compDate = compDate;
	}

	// 联系电话
	private String telNumber;

	@Column(name = SMS_COMP_INFO.TEL_NUMBER)
	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	// 手机号码
	private String mobile;

	@Column(name = SMS_COMP_INFO.MOBILE)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	// 公司地址
	private String compAdd;

	@Column(name = SMS_COMP_INFO.COMP_ADD)
	public String getCompAdd() {
		return compAdd;
	}

	public void setCompAdd(String compAdd) {
		this.compAdd = compAdd;
	}

	// 公司官网
	private String compWeb;

	@Column(name = SMS_COMP_INFO.COMP_WEB)
	public String getCompWeb() {
		return compWeb;
	}

	public void setCompWeb(String compWeb) {
		this.compWeb = compWeb;
	}

	// 公司邮箱
	private String compEmail;

	@Column(name = SMS_COMP_INFO.COMP_EMAIL)
	public String getCompEmail() {
		return compEmail;
	}

	public void setCompEmail(String compEmail) {
		this.compEmail = compEmail;
	}

	// 经营范围
	private String busiScope;

	@Column(name = SMS_COMP_INFO.BUSI_SCOPE)
	public String getBusiScope() {
		return busiScope;
	}

	public void setBusiScope(String busiScope) {
		this.busiScope = busiScope;
	}
	
	// 创建人
	private String createUser;

	@Column(name = SMS_COMP_INFO.CREATE_USER)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	private String createTime;
	private String lastModTime;
	private String lastModUser;


	@Column(name = SMS_COMP_INFO.CREATE_TIME)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = SMS_COMP_INFO.LAST_MOD_TIME)
	public String getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(String lastModTime) {
		this.lastModTime = lastModTime;
	}

	@Column(name = SMS_COMP_INFO.LAST_MOD_USER)
	public String getLastModUser() {
		return lastModUser;
	}

	public void setLastModUser(String lastModUser) {
		this.lastModUser = lastModUser;
	}

	
}
