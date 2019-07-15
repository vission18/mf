package com.vission.mf.base.model.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.MMS_XF_RECORD;

/**
* 功能/模块 ：消费记录实体类
*/
@Entity
@Table(name = MMS_XF_RECORD.TABLE_NAME)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class MmsXfRecord extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	/* 数据库字段 */
	private String xfId;
	private String xfType;
	private String xfGoods;
	private int xfPrice;
	private String xfDate;
	private String recordDate;
	private String recordUser;
	private String xfDetail;
	private String delFlag;
	
	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = MMS_XF_RECORD.XF_ID)
	public String getXfId() {
		return xfId;
	}
	public void setXfId(String xfId) {
		this.xfId = xfId;
	}
	@Column(name = MMS_XF_RECORD.XF_TYPE)
	public String getXfType() {
		return xfType;
	}
	public void setXfType(String xfType) {
		this.xfType = xfType;
	}
	@Column(name = MMS_XF_RECORD.XF_GOODS)
	public String getXfGoods() {
		return xfGoods;
	}
	public void setXfGoods(String xfGoods) {
		this.xfGoods = xfGoods;
	}
	@Column(name = MMS_XF_RECORD.XF_PRICE)
	public int getXfPrice() {
		return xfPrice;
	}
	public void setXfPrice(int xfPrice) {
		this.xfPrice = xfPrice;
	}
	@Column(name = MMS_XF_RECORD.XF_DATE)
	public String getXfDate() {
		return xfDate;
	}
	public void setXfDate(String xfDate) {
		this.xfDate = xfDate;
	}
	@Column(name = MMS_XF_RECORD.RECORD_DATE)
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	@Column(name = MMS_XF_RECORD.RECORD_USER)
	public String getRecordUser() {
		return recordUser;
	}
	public void setRecordUser(String recordUser) {
		this.recordUser = recordUser;
	}
	@Column(name = MMS_XF_RECORD.XF_DETAIL)
	public String getXfDetail() {
		return xfDetail;
	}
	public void setXfDetail(String xfDetail) {
		this.xfDetail = xfDetail;
	}
	@Column(name = MMS_XF_RECORD.DEL_FLAG)
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
}