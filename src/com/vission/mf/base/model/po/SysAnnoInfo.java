package com.vission.mf.base.model.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.SYS_ANNO_INFO;
import com.vission.mf.base.util.CustomDateSerializer;

/**
 * 功能/模块 ：公告实体类
 * 
 */
@Entity
@Table(name = SYS_ANNO_INFO .TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class SysAnnoInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String annoId;
	private String annoTitle;
	private String loginName;
	private Date createDt;
	private String typeId;
	private boolean annoStatus;
	private Date beginDt;
	private Date endDt;
	private String annoContent;
	private String typeName;
	
	private List<SysAnnoAcceInfo> sysAnnoAcceList = new ArrayList<SysAnnoAcceInfo>();

	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_ANNO_INFO .ANNO_ID)
	public String getAnnoId() {
		return annoId;
	}

	public void setAnnoId(String annoId) {
		this.annoId = annoId;
	}

	@Column(name = SYS_ANNO_INFO.ANNO_TITLE)
	public String getAnnoTitle() {
		return annoTitle;
	}

	public void setAnnoTitle(String annoTitle) {
		this.annoTitle = annoTitle;
	}

	@Column(name = SYS_ANNO_INFO.LOGIN_NAME)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@Column(name = SYS_ANNO_INFO.CREATE_DT, updatable = false)
	public Date getCreateDt() {
		return createDt;
	}
	
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	
	@Column(name = SYS_ANNO_INFO.TYPE_ID)
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@Column(name = SYS_ANNO_INFO.BEGIN_DT)
	public Date getBeginDt() {
		return beginDt;
	}

	public void setBeginDt(Date beginDt) {
		this.beginDt = beginDt;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@Column(name = SYS_ANNO_INFO.END_DT)
	public Date getEndDt() {
		return endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	@Column(name = SYS_ANNO_INFO.ANNO_CONTENT)
	public String getAnnoContent() {
		return annoContent;
	}

	public void setannoContent(String annoContent) {
		this.annoContent = annoContent;
	}
	
	@Column(name = SYS_ANNO_INFO.ANNO_STATUS)
	public boolean isAnnoStatus() {
		return annoStatus;
	}

	public void setAnnoStatus(boolean annoStatus) {
		this.annoStatus = annoStatus;
	}
	
	@Transient
	@JsonIgnore
	public List<SysAnnoAcceInfo> getAnnoAcceList() {
		return sysAnnoAcceList;
	}
	
	public void setSysAnnoAcceList(List<SysAnnoAcceInfo> sysAnnoAcceList) {
		this.sysAnnoAcceList = sysAnnoAcceList;
	}

	@Column(name = SYS_ANNO_INFO.TYPE_NAME)
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
