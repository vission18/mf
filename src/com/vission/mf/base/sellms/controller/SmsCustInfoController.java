package com.vission.mf.base.sellms.controller;

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

import com.vission.mf.base.controller.BaseController;
import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.sellms.model.po.SmsCustInfo;
import com.vission.mf.base.sellms.service.SmsCustInfoService;
import com.vission.mf.base.util.DateUtil;

/**
 * 
 * 功能模块：客户信息controller
 *
 */
@Controller
@RequestMapping("/smsCustInfo")
public class SmsCustInfoController extends BaseController {
	@Autowired
	private SmsCustInfoService smsCustInfoService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model) {
		this.getAccessButtons(request, model);
		return "sellms/custinfo/custInfoList";
	}

	/**
	 * 获取一条客户信息
	 * 
	 * @throws ServiceException
	 */
	@RequestMapping("/getCustInfoById")
	@ResponseBody
	public AjaxResult getCustInfoById(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		String dmConfId = request.getParameter("dmConfId");
		SmsCustInfo custInfo = null;
		custInfo = this.smsCustInfoService.getCustInfoById(dmConfId);
		AjaxResult result = new AjaxResult();
		if (custInfo == null) {
			result.setSuccess(false);
		} else {
			result.setData(custInfo);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 * 保存客户
	 */
	@RequestMapping(value = "/saveCustInfo", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveCustInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			SmsCustInfo custInfo) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		try {
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("保存成功！");
			// 判断新增还是修改
			SmsCustInfo checkCustInfo = smsCustInfoService
					.getCustInfoById(custInfo.getCustId());
			if (checkCustInfo == null) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
				custInfo.setCreateUser(sessionInfo.getUser().getLoginName());
			} else {
				custInfo.setLastModTime(DateUtil.format(new Date()));
				custInfo.setLastModUser(sessionInfo.getUser().getLoginName());
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
			}
			this.smsCustInfoService.saveCustInfo(custInfo);
			ajaxResult.setData(custInfo);
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage(e.getMessage());
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}

	/**
	 * 删除客户值
	 */
	@RequestMapping(value = "/deleteCustInfoById", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult deleteCustInfoById(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		String custId = request.getParameter("delCustId");
		try {
			smsCustInfoService.deleteDmValByDmId(custId);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("删除成功");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("删除失败！");
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}

	/**
	 * 客户信息列表
	 */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(HttpServletRequest request, HttpSession session,
			SmsCustInfo custInfo) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			custInfo.setCustName(request.getParameter("queryCustName"));
			custInfo.setCustStatus(request.getParameter("queryCustStatus"));
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
		}
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		return smsCustInfoService.dataGrid(custInfo, sessionInfo, pageNo,
				pageSize);
	}

}
