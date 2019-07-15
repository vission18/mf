package com.vission.mf.base;

import javax.persistence.Transient;

/**
* 功能/模块 ：基础代码
 */
public class BaseEntity implements java.io.Serializable{
	private boolean checked;//是否选中

	@Transient
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
