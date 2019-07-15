package com.vission.mf.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.po.SysAdviceInfo;
import com.vission.mf.base.model.po.SysUserInfo;
import com.vission.mf.base.service.SysUserAdviceService;

/**
 * 功能/模块 ：用户建议
 */
@Controller
@RequestMapping("/advice")
public class UserAdviceController extends BaseController {
	@Autowired
	private SysUserAdviceService sysUserAdviceService;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "base/userAdvice";
	}
	
	//保存用户建议
	@RequestMapping(value ="/addUserAdvice", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult addUserAdvice (HttpServletRequest request,HttpServletResponse response,HttpSession session,SysAdviceInfo advice) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			if(advice.getAdviceId()==null||"".equals(advice.getAdviceId())){
				advice.setAdviceId(null);
			}
			SessionInfo sessionInfo = (SessionInfo) session
					.getAttribute(BaseConstants.USER_SESSION_KEY);
			SysUserInfo sessionUser = sessionInfo.getUser();
			advice.setUserId(sessionUser.getUserId());
			sysUserAdviceService.saveAdvice(advice);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("您的意义已提交,谢谢");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}
	
	/**
	 * 数据列表
	 */
	@RequestMapping(value = "/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(HttpServletRequest request, SysAdviceInfo advice) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return sysUserAdviceService.dataGrid(advice, pageNo, pageSize);
	}
	
	//用户建议删除
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request, HttpServletResponse response,SysAdviceInfo advice) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			sysUserAdviceService.delete(advice);
			ajaxResult.setData(advice);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("删除成功！");
		}catch(Exception e){
			throw new ServiceException(e);
		}
		return ajaxResult;
	}
	
	//获取用户建议
	@RequestMapping(value="/getUserAdviceByUserIdAndPageId", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult getUserAdviceByUserIdAndPageId(HttpServletResponse response,String userId,String pageId){
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setSuccess(true);
		ajaxResult.setData(this.sysUserAdviceService.getUserAdviceByUserIdAndPageId(userId, pageId));
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}
}
