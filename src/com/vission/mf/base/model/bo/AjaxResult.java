package com.vission.mf.base.model.bo;
/**
* 功能/模块 ：基础代码
 */
public class AjaxResult {

	//是否成功标志
	private boolean success = false;
	//提示消息
	private String message = "";
	//对象
	private Object data = null;
	//操作类型  define in BaseConstants
	private String type = "";

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
