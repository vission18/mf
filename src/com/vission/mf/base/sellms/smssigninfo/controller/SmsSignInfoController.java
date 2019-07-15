package com.vission.mf.base.sellms.smssigninfo.controller;

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

import com.vission.mf.base.sellms.smssigninfo.service.SmsSignInfoService;
import com.vission.mf.base.sellms.smssigninfo.po.SmsSignInfo;

/**
 * 作者：acf
 * 描述：SmsSignInfoController 控制层
 * 日期：2019-6-27 1:07:19
 * 类型：CONTROLLER文件
 */
@Controller
@RequestMapping("/smsSignInfo")
public class SmsSignInfoController extends BaseController {

	@Autowired
	private SmsSignInfoService smsSignInfoService;

	/**
	 * 跳转至jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "sellms/smssigninfo/smsSignInfoList";
	}

	/**
	 * 数据列表
	 */
	@RequestMapping(value = "/smsSignInfoDataGrid")
	@ResponseBody
	public DataGrid smsSignInfoDataGrid(HttpServletRequest request, SmsSignInfo po) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return smsSignInfoService.dataGrid(po, pageNo, pageSize);
	}


	/**
	 * 获取一条数据
	 */
	@RequestMapping(value = "/getSmsSignInfoById")
	@ResponseBody
	public AjaxResult getSmsSignInfoById(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		String pkId = request.getParameter("PK_ID");
		ajaxResult.setData(smsSignInfoService.getSmsSignInfoById(pkId));
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/deleteSmsSignInfoById", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {	
			String pkId = request.getParameter("PK_ID");
			smsSignInfoService.deleteSmsSignInfoById(pkId);
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
	@RequestMapping(value = "/saveSmsSignInfo", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveSmsSignInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, SmsSignInfo po)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			if (po.getPkId() == null || "".equals(po.getPkId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
				smsSignInfoService.saveSmsSignInfo(po,"add");
			} else {
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
				smsSignInfoService.saveSmsSignInfo(po,"update");
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
}
