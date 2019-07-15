package com.vission.mf.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vission.mf.base.controller.BaseController;

@Controller
@RequestMapping("/public")
public class PublicController extends BaseController {

	@RequestMapping("/desktop")
	public String list(HttpServletRequest request) {
		return "homeDesktop";
	}

}
