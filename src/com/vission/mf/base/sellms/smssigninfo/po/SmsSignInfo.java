package com.vission.mf.base.sellms.smssigninfo.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import com.vission.mf.base.sellms.smssigninfo.db.SMS_SIGN_INFO;

/**
 * 作者：lkj
 * 描述：SMS_SIGN_INFO 模块Po
 * 日期：2019-6-27 1:07:19
 * 类型：Po文件
 */
 
@Entity
@Table(name = SMS_SIGN_INFO.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
public class SmsSignInfo {

	private static final long serialVersionUID = 1L;
	
	//主键
    private String pkId;
    
    @Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SMS_SIGN_INFO.PK_ID)
	public String getPkId() {
		return pkId;
	}
	public void setPkId(String pkId) {
		this.pkId = pkId;
	}
	//签名
    private String signName;
    
	@Column(name = SMS_SIGN_INFO.SIGN_NAME)
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	//描述
    private String signDesc;
    
	@Column(name = SMS_SIGN_INFO.SIGN_DESC,nullable = false)
	public String getSignDesc() {
		return signDesc;
	}
	public void setSignDesc(String signDesc) {
		this.signDesc = signDesc;
	}
}

