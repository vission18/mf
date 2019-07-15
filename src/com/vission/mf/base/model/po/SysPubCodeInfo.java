package com.vission.mf.base.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.SYS_PUBCODE_INFO;

@Entity
@Table(name = SYS_PUBCODE_INFO.TABLE_NAME)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class SysPubCodeInfo extends BaseEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private String codeId;
	private String codeName;
	private String codeValue;
	private String codeRmk;
	
	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_PUBCODE_INFO.CODE_ID)
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}	
	
	@Column(name = SYS_PUBCODE_INFO.CODE_NAME, updatable = false)
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	@Column(name = SYS_PUBCODE_INFO.CODE_VALUE)
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	
	@Column(name = SYS_PUBCODE_INFO.CODE_RMK)
	public String getCodeRmk() {
		return codeRmk;
	}
	public void setCodeRmk(String codeRmk) {
		this.codeRmk = codeRmk;
	}
	
}