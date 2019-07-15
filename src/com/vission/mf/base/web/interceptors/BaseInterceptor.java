package com.vission.mf.base.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.vission.mf.base.controller.ControllerContext;

public class BaseInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = Logger.getLogger(getClass());

	public boolean preHandle(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object handler)
			throws Exception {

		// 注入Request/Response对象至SpringMVC执行上下文对象中

		ControllerContext.getContext().setRequest(httpServletRequest);

		ControllerContext.getContext().setResponse(httpServletResponse);

		return true;

	}

}
