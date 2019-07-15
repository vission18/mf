package com.vission.mf.base.sellms.smsfileinfo.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import com.vission.mf.base.sellms.smsfileinfo.db.SMS_FILE_INFO;

/**
 * 作者：lkj
 * 描述：SMS_FILE_INFO 模块Po
 * 日期：2019-6-30 18:13:58
 * 类型：Po文件
 */
 
@Entity
@Table(name = SMS_FILE_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
public class SmsFileInfo {

	private static final long serialVersionUID = 1L;
	
	//主键
    private String pkId;
    
    @Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SMS_FILE_INFO.PK_ID)
	public String getPkId() {
		return pkId;
	}
	public void setPkId(String pkId) {
		this.pkId = pkId;
	}
	//文件名称
    private String fileName;
    
	@Column(name = SMS_FILE_INFO.FILE_NAME,nullable = false)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	//文件路径
    private String filePath;
    
	@Column(name = SMS_FILE_INFO.FILE_PATH,nullable = false)
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	//电话抬头
    private String phoneKey;
    
	@Column(name = SMS_FILE_INFO.PHONE_KEY,nullable = false)
	public String getPhoneKey() {
		return phoneKey;
	}
	public void setPhoneKey(String phoneKey) {
		this.phoneKey = phoneKey;
	}
	//创建人
    private String createUser;
    
	@Column(name = SMS_FILE_INFO.CREATE_USER,nullable = false)
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	//创建时间
    private String createTime;
    
	@Column(name = SMS_FILE_INFO.CREATE_TIME,nullable = false)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}

