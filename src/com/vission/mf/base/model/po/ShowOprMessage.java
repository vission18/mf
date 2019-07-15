package com.vission.mf.base.model.po;
/**
 * 功能：该实体用于用户操作成功或失败的显示信息
 * @author 李刻键
 *
 */
public class ShowOprMessage {

	boolean operFlag;
	String resMsg;
	public boolean isOperFlag() {
		return operFlag;
	}
	public void setOperFlag(boolean operFlag) {
		this.operFlag = operFlag;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	
	
}
