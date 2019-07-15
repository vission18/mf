package com.vission.mf.base.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.SYS_ANNO_ACCE_INFO;

/**
 * 功能/模块 ：公告附件实体类
 * 
 */
@Entity
@Table(name = SYS_ANNO_ACCE_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class SysAnnoAcceInfo extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String annoId;
	private String fileName;
	private byte[] fileContent;

	public SysAnnoAcceInfo() {
		super();
	}

	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_ANNO_ACCE_INFO .ANNO_ID)
	public String getAnnoId() {
		return annoId;
	}

	public void setAnnoId(String annoId) {
		this.annoId = annoId;
	}

	@Column(name = SYS_ANNO_ACCE_INFO.FILE_NAME)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = SYS_ANNO_ACCE_INFO.FILE_CONTENT)
	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
}
