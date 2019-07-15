package com.vission.mf.base.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.SYS_ANNO_TYPE_INFO;

/**
 * 功能/模块 ：工程实体类
 * 
 */
@Entity
@Table(name = SYS_ANNO_TYPE_INFO .TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class SysAnnoTypeInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String typeId;
	private String typeName;
	private String typeRmk;
	
	public SysAnnoTypeInfo() {
		super();
	}

	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_ANNO_TYPE_INFO.TYPE_ID)
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Column(name = SYS_ANNO_TYPE_INFO.TYPE_NAME)
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name = SYS_ANNO_TYPE_INFO.TYPE_RMK )
	public String getTypeRmk() {
		return typeRmk;
	}

	public void setTypeRmk(String typeRmk) {
		this.typeRmk = typeRmk;
	}
}
