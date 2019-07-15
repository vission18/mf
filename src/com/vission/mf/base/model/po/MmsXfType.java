package com.vission.mf.base.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.MMS_XF_TYPE;

/**
* 功能/模块 ：消费类别实体类
*/
@Entity
@Table(name = MMS_XF_TYPE.TABLE_NAME)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class MmsXfType extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	/* 数据库字段 */
	private String typeId;
	private String typeName;
	private boolean typeStatus;
	private String remark;
	
	
	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = MMS_XF_TYPE.TYPE_ID)
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	@Column(name = MMS_XF_TYPE.TYPE_NAME)
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Column(name = MMS_XF_TYPE.TYPE_STATUS)
	public boolean isTypeStatus() {
		return typeStatus;
	}
	public void setTypeStatus(boolean typeStatus) {
		this.typeStatus = typeStatus;
	}
	@Column(name = MMS_XF_TYPE.REMARK)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}