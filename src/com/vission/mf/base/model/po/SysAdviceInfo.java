package com.vission.mf.base.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.SYS_ADVICE_INFO;
/**
 * 功能/模块 ：用户建议实体类
 * 
 */
@Entity
@Table(name = SYS_ADVICE_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class SysAdviceInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String adviceId;
	private String pageId;
	private String userId;
	private String advice;
	
	public SysAdviceInfo() {
		super();
	}
	
	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_ADVICE_INFO.ADVICE_ID)
	public String getAdviceId() {
		return adviceId;
	}
	
	public void setAdviceId(String adviceId) {
		this.adviceId = adviceId;
	}
	
	@Column(name = SYS_ADVICE_INFO.PAGE_ID)
	public String getPageId() {
		return pageId;
	}
	
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	@Column(name = SYS_ADVICE_INFO.USER_ID)
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = SYS_ADVICE_INFO.ADVICE)
	public String getAdvice() {
		return advice;
	}
	
	public void setAdvice(String advice) {
		this.advice = advice;
	}

}