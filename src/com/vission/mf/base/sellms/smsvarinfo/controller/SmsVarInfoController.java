package com.vission.mf.base.sellms.smsvarinfo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.vission.mf.base.controller.BaseController;
import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.util.DateUtil;
import com.vission.mf.base.util.Encrypt;
import com.vission.mf.base.util.FileUtil;
import com.vission.mf.base.util.RegexUtil;
import com.vission.mf.base.exception.ServiceException;

import com.vission.mf.base.sellms.smsvarinfo.service.SmsVarInfoService;
import com.vission.mf.base.sellms.smsvarinfo.po.SmsVarInfo;

/**
 * 作者：acf
 * 描述：SmsVarInfoController 控制层
 * 日期：2019-6-30 17:40:32
 * 类型：CONTROLLER文件
 */
@Controller
@RequestMapping("/smsVarInfo")
public class SmsVarInfoController extends BaseController {

	@Autowired
	private SmsVarInfoService smsVarInfoService;

	/**
	 * 跳转至jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "sellms/smsvarinfo/smsVarInfoList";
	}

	/**
	 * 数据列表
	 */
	@RequestMapping(value = "/smsVarInfoDataGrid")
	@ResponseBody
	public DataGrid smsVarInfoDataGrid(HttpServletRequest request, SmsVarInfo po) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return smsVarInfoService.dataGrid(po, pageNo, pageSize);
	}


	/**
	 * 获取一条数据
	 */
	@RequestMapping(value = "/getSmsVarInfoById")
	@ResponseBody
	public AjaxResult getSmsVarInfoById(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		String pkId = request.getParameter("PK_ID");
		ajaxResult.setData(smsVarInfoService.getSmsVarInfoById(pkId));
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/deleteSmsVarInfoById", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {	
			String pkId = request.getParameter("PK_ID");
			smsVarInfoService.deleteSmsVarInfoById(pkId);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("删除成功！");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("删除失败！");
		}
		return ajaxResult;
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/saveSmsVarInfo", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveSmsVarInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, SmsVarInfo po)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		try {
			if (po.getPkId() == null || "".equals(po.getPkId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
				po.setCreateTime(DateUtil.getCurrentSystemTime());
				po.setCreateUser(sessionInfo.getUser().getUserId());
				smsVarInfoService.saveSmsVarInfo(po,"add");
			} else {
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
				po.setLastModTime(DateUtil.getCurrentSystemTime());
				po.setLastModUser(sessionInfo.getUser().getUserId());
				smsVarInfoService.saveSmsVarInfo(po,"update");
			}
			ajaxResult.setData(po);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("保存成功！");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("保存失败！");
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}
	
	@RequestMapping("/show")
	public String show(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		//功能暂未开通
		return "sellms/smsvarinfo/showTipWin";
	}
}
