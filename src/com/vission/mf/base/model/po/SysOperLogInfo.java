package com.vission.mf.base.model.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.util.CustomDateSerializer;

/**
 * 
 * 功能/模块 :操作日志实体
 */
@Entity
@Table(name = SYS_OPERLOG_INFO.TABLE_NAME)
public class SysOperLogInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String logId;
	private String loginName;
	private String loginIp;
	private Date operTime;
	private String operContent;
	private String operType;
	
	private String beginOperTime;
	
	private String endOperTime;
	
	private String operTransType;

	@Transient 
	public String getBeginOperTime() {
		return beginOperTime;
	}

	public void setBeginOperTime(String beginOperTime) {
		this.beginOperTime = beginOperTime;
	}
	@Transient 
	public String getEndOperTime() {
		return endOperTime;
	}

	public void setEndOperTime(String endOperTime) {
		this.endOperTime = endOperTime;
	}


	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_OPERLOG_INFO.LOG_ID)
	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	@Column(name = SYS_OPERLOG_INFO.LOGIN_NAME)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = SYS_OPERLOG_INFO.LOGIN_IP)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@Column(name = SYS_OPERLOG_INFO.OPER_TIME)
	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	@Column(name = SYS_OPERLOG_INFO.OPER_CONTENT)
	public String getOperContent() {
		return operContent;
	}

	public void setOperContent(String operContent) {
		this.operContent = operContent;
	}

	@Column(name = SYS_OPERLOG_INFO.OPER_TYPE)
	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}


	@Transient
	public String getOperTransType() {
		return SYS_OPERLOG_INFO.operTypeMap.get(this.getOperType());
	}

	public void setOperTransType(String operTransType) {
		this.operTransType = operTransType;
	}
}