package com.vission.mf.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
* 功能/模块 ：Portal模块
 */
@Controller
@RequestMapping(value="/portal.do")
public class PortalController {
	
	/**
	 * 跳转到portal页
	 */
	@RequestMapping(method=RequestMethod.POST)
	public String portal(HttpServletRequest request, Model model) {
		return "portal/portal";
	}
	
}
