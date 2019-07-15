package com.vission.mf.base.acf.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.acf.po.AcfColInfo;
import com.vission.mf.base.acf.po.AcfModInfo;
import com.vission.mf.base.acf.service.AcfModInfoService;
import com.vission.mf.base.controller.BaseController;
import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.util.DateUtil;

/**
 * 
 * 功能模块：模块信息controller
 *
 */
@Controller
@RequestMapping("/acfModInfo")
public class AcfModInfoController extends BaseController {
	@Autowired
	private AcfModInfoService acfModInfoService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model) {
		this.getAccessButtons(request, model);
		return "acf/modinfo/modInfoList";
	}


	/**
	 * 保存
	 */
	@RequestMapping(value = "/saveModInfo", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveModInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			AcfModInfo modInfo) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		try {
			ajaxResult.setSuccess(true);
			modInfo.setDelFlag("1");
			modInfo.setCreateTime(DateUtil.format(new Date()));
			modInfo.setCreateUser(sessionInfo.getUser().getUserName());
			modInfo.setLastModTime(DateUtil.format(new Date()));
			modInfo.setLastModUser(sessionInfo.getUser().getUserName());
			this.acfModInfoService.save(modInfo);
			ajaxResult.setData(modInfo);
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage(e.getMessage());
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}
	
	@RequestMapping(value = "/colDataGridByModId")
	@ResponseBody
	public DataGrid colDataGridByModId(HttpServletRequest request, HttpSession session) {
		int pageNo = 1;
		int pageSize = 10;
		String modId = request.getParameter("modId");
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return acfModInfoService.getColDataGridByModId(modId, pageNo, pageSize);
	}
	
	@RequestMapping(value = "/saveColInfo", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveColInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			AcfColInfo colInfo) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		try {
			String modId=request.getParameter("modId");
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("保存成功！");
			ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
			AcfColInfo checkColInfo = acfModInfoService.getColInfoByColName(colInfo.getColEngName(), modId);
			if (checkColInfo == null) {
				colInfo.setModId(modId);
				colInfo.setDelFlag("0");
				colInfo.setLastModTime(DateUtil.format(new Date()));
				colInfo.setLastModUser(sessionInfo.getUser().getUserName());
				//保存
				this.acfModInfoService.saveColInfo(colInfo);
			}else{
				ajaxResult.setSuccess(false);
				ajaxResult.setMessage("当前模块下已存在该字段！");
			}
			ajaxResult.setData(colInfo);
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage(e.getMessage());
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}

	@RequestMapping(value = "/autoCreateCode", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult autoCreateCode(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			AcfModInfo modInfo) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			ajaxResult.setSuccess(true);
			this.acfModInfoService.autoCreateCode(modInfo);
			ajaxResult.setData(modInfo);
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage(e.getMessage());
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}
}
