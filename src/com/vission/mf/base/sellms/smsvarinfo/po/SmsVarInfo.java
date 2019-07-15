package com.vission.mf.base.sellms.smsvarinfo.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import com.vission.mf.base.sellms.smsvarinfo.db.SMS_VAR_INFO;

/**
 * 作者：lkj
 * 描述：SMS_VAR_INFO 模块Po
 * 日期：2019-6-30 17:40:32
 * 类型：Po文件
 */
 
@Entity
@Table(name = SMS_VAR_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
public class SmsVarInfo {

	private static final long serialVersionUID = 1L;
	
	//主键
    private String pkId;
    
    @Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SMS_VAR_INFO.PK_ID)
	public String getPkId() {
		return pkId;
	}
	public void setPkId(String pkId) {
		this.pkId = pkId;
	}
	//变量名称
    private String varName;
    
	@Column(name = SMS_VAR_INFO.VAR_NAME)
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	//变量说明
    private String varDesc;
    
	@Column(name = SMS_VAR_INFO.VAR_DESC,nullable = false)
	public String getVarDesc() {
		return varDesc;
	}
	public void setVarDesc(String varDesc) {
		this.varDesc = varDesc;
	}
	//创建人
    private String createUser;
    
	@Column(name = SMS_VAR_INFO.CREATE_USER,nullable = false)
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	//创建时间
    private String createTime;
    
	@Column(name = SMS_VAR_INFO.CREATE_TIME,nullable = false)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	//修改人
    private String lastModUser;
    
	@Column(name = SMS_VAR_INFO.LAST_MOD_USER,nullable = false)
	public String getLastModUser() {
		return lastModUser;
	}
	public void setLastModUser(String lastModUser) {
		this.lastModUser = lastModUser;
	}
	//修改时间
    private String lastModTime;
    
	@Column(name = SMS_VAR_INFO.LAST_MOD_TIME,nullable = false)
	public String getLastModTime() {
		return lastModTime;
	}
	public void setLastModTime(String lastModTime) {
		this.lastModTime = lastModTime;
	}
}

