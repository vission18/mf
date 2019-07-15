package com.vission.mf.base.sellms.model.bo;

import com.vission.mf.base.BaseEntity;

public class SmsPhoneInfo extends BaseEntity {
	private static final long serialVersionUID = 7380022820020315922L;
	private String idNum;
	private String phoneNum;
	private String province;
	private String city;
	private String phoneStatus;
	

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneStatus() {
		return phoneStatus;
	}

	public void setPhoneStatus(String phoneStatus) {
		this.phoneStatus = phoneStatus;
	}

}