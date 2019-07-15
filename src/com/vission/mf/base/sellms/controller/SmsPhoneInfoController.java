package com.vission.mf.base.sellms.controller;

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
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.sellms.model.bo.SmsPhoneInfo;
import com.vission.mf.base.sellms.service.SmsPhoneInfoService;
@Controller
@RequestMapping("/smsPhoneInfo")
public class SmsPhoneInfoController extends BaseController {
	@Autowired
	private SmsPhoneInfoService smsPhoneInfoService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model) {
		this.getAccessButtons(request, model);
		return "sellms/phoneinfo/phoneInfoList";
	}

	

	@RequestMapping(value = "/createPhoneList", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult createPhoneList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			SmsPhoneInfo phoneInfo) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		try {
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("保存成功！");
			phoneInfo.setCity("成都");
			phoneInfo.setProvince("四川");
			ajaxResult.setData(this.smsPhoneInfoService.createPhoneList(phoneInfo));
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage(e.getMessage());
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}

}
