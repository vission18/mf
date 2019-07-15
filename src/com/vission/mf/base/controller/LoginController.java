package com.vission.mf.base.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.po.SysBranchInfo;
import com.vission.mf.base.model.po.SysMenuInfo;
import com.vission.mf.base.model.po.SysUserInfo;
import com.vission.mf.base.service.SysBranchInfoService;
import com.vission.mf.base.service.SysMenuInfoService;
import com.vission.mf.base.service.SysRoleInfoService;
import com.vission.mf.base.service.SysUserInfoService;
import com.vission.mf.base.util.Encrypt;
import com.vission.mf.base.util.IpUtil;

/**
* 功能/模块 ：登录-登出
 */
@Controller
@RequestMapping(value="/login.do")
public class LoginController extends BaseController {
	
	@Autowired
	private SysUserInfoService sysUserInfoService;
	@Autowired
	private SysMenuInfoService sysMenuInfoService;
	@Autowired
	private SysRoleInfoService sysRoleInfoService;
	@Autowired
	private SysBranchInfoService sysBranchInfoService;

	@RequestMapping(method=RequestMethod.POST)
	public String login(HttpServletRequest request,HttpServletResponse response,SysUserInfo user, HttpSession session, ModelMap model) {
		SysUserInfo loginUser = sysUserInfoService.getUserByLoginName(user.getLoginName());
		String message = null;
		if (loginUser != null) {
			if(Encrypt.e(user.getLoginPassword()).equals(loginUser.getLoginPassword())){
				if(loginUser.isUserStatus()){
					SessionInfo sessionInfo = new SessionInfo();
					sessionInfo.setUser(loginUser);
					sessionInfo.setIp(IpUtil.getIpAddr(request));
					sessionInfo.setSessionId(session.getId());
					loginUser.setLoginIp(sessionInfo.getIp());
					//设置用户角色
					sysRoleInfoService.setSessionInfoRoles(sessionInfo);
					//设置用户菜单
					List<SysMenuInfo> menus = sysMenuInfoService.getMenuListByRoleIds(sessionInfo.getRoleIds());
					sessionInfo.setMenus(menus);
					session.setAttribute(BaseConstants.USER_SESSION_KEY, sessionInfo);
					if(loginUser.getBranchNo()!=null&&!"".equals(loginUser.getBranchNo())){
						SysBranchInfo branch = sysBranchInfoService.getById(loginUser.getBranchNo());
						if(branch!=null){
							loginUser.setBranchName(branch.getBranchName());
						}
					}
					if(loginUser.getBranchName()==null||"".equals(loginUser.getBranchName())){
						loginUser.setBranchName("无");
					}
					model.addAttribute("sessionInfo", sessionInfo);
					this.saveOperLogInfo(request, SYS_OPERLOG_INFO.LOGIN_SYS_SYSTEM, "用户登录成功");
					return "redirect:/home.do";
				}else{
					debug("用户被禁用");
					message = "用户被禁用";
				}
			} else {
				debug("密码错误");
				message = this.getMessage("login.fail");
			}
		} else {
			debug("用户不存在");
			message = "用户不存在";
		}
		model.addAttribute("message", message);
		return "forward:login.jsp";
	}

	@RequestMapping(method=RequestMethod.GET)
	public String logout(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		return "forward:login.jsp";
	}

}
