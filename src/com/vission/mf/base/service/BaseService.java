package com.vission.mf.base.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.vission.mf.base.BaseClass;
import com.vission.mf.base.controller.ControllerContext;
import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.po.SysOperLogInfo;
/**
 * 
 * 功能/模块 :公共service类，所有自定义service类继承该类
 */
public class BaseService extends BaseClass{
	@Autowired
	private SysOperLogInfoService sysOperLogInfoService;
	
	public SysOperLogInfoService getSysOperLogInfoService() {
		return sysOperLogInfoService;
	}

	public void setSysOperLogInfoService(SysOperLogInfoService sysOperLogInfoService) {
		this.sysOperLogInfoService = sysOperLogInfoService;
	}
	/**
	 * 保存日志（用户必须已经登录并且已经记录session）
	 * @param request，根据request获取session中登录信息
	 * @param operType SYS_OPERLOG_INFO中定义的具体类型
	 * @param operContent 操作详细描述
	 */
	public void saveOperLogInfo(String operType,String operContent){
		HttpServletRequest request=ControllerContext.getContext().getRequest();
		SysOperLogInfo sysOperLogInfo=new SysOperLogInfo();
		SessionInfo sessionInfo=(SessionInfo)request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY);
		sysOperLogInfo.setLoginName(sessionInfo.getUser().getLoginName());
		sysOperLogInfo.setLoginIp(sessionInfo.getIp());
		sysOperLogInfo.setOperTime(new Date());
		sysOperLogInfo.setOperContent(operContent);
		sysOperLogInfo.setOperType(operType);
		this.sysOperLogInfoService.saveOperLogInfo(sysOperLogInfo);
	}
	/**
	 * 保存日志
	 * @param LoginName
	 * @param loginIp
	 * @param operType SYS_OPERLOG_INFO中定义的具体类型
	 * @param operContent 操作详细描述
	 */
	public void saveOperLogInfo(String LoginName,String loginIp,String operType,String operContent){
		SysOperLogInfo sysOperLogInfo=new SysOperLogInfo();
		sysOperLogInfo.setLoginName(LoginName);
		sysOperLogInfo.setLoginIp(loginIp);
		sysOperLogInfo.setOperTime(new Date());
		sysOperLogInfo.setOperContent(operContent);
		sysOperLogInfo.setOperType(operType);
		this.sysOperLogInfoService.saveOperLogInfo(sysOperLogInfo);
	}
}
