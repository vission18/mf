package com.vission.mf.base.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import com.vission.mf.base.BaseClass;
import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.po.SysOperLogInfo;
import com.vission.mf.base.model.po.SysUserInfo;
import com.vission.mf.base.service.SysOperLogInfoService;

public class BaseController extends BaseClass{
	@Autowired
	protected MessageSource messages;
	@Autowired
	private SysOperLogInfoService sysOperLogInfoService;

	public SysOperLogInfoService getSysOperLogInfoService() {
		return sysOperLogInfoService;
	}

	public void setSysOperLogInfoService(SysOperLogInfoService sysOperLogInfoService) {
		this.sysOperLogInfoService = sysOperLogInfoService;
	}
	/**
	 * 保存日志
	 * @param request，根据request获取session中登录信息
	 * @param operType SYS_OPERLOG_INFO中定义的具体类型
	 * @param operContent 操作详细描述
	 */
	public void saveOperLogInfo(HttpServletRequest request,String operType,String operContent){
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
	
	/**
	 * 
	 */
	public void getAccessButtons(HttpServletRequest request,Model model){
		String menuId=request.getParameter("menuId");
		AjaxResult ajaxResult=new AjaxResult();
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY);
		if (BaseConstants.SUPER_USER.equals(sessionInfo.getUser().getLoginName())) {// 超管不需要验证权限
			ajaxResult.setType("super_user");
			List<String> accessButtons = sessionInfo.getButtonsMap().get(menuId);
			ajaxResult.setData(accessButtons);
		}else{
			List<String> accessButtons = sessionInfo.getButtonsMap().get(menuId);
			ajaxResult.setData(accessButtons);
		}
			model.addAttribute("accessButtons", ajaxResult);
	}
	
	public List<String> getRoleIds(HttpServletRequest request){
		List<String> roleIds = ((SessionInfo) request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY)).getRoleIds();
		return roleIds; 
	}
	
	public SysUserInfo getLoginUser(HttpServletRequest request){
		SysUserInfo user = ((SessionInfo) request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY)).getUser();
		return user;
	}
}
