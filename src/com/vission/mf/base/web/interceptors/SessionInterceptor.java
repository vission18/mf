package com.vission.mf.base.web.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.model.bo.SessionInfo;

public class SessionInterceptor implements HandlerInterceptor {

	private static final Logger logger = Logger
			.getLogger(SessionInterceptor.class);

	private List<String> excludeUrls;

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	/**
	 * 完成页面的render后调用
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {

	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在调用controller具体方法前拦截
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		logger.debug(url);
		if (excludeUrls.contains(url)) {
			return true;
		} else {
			SessionInfo sessionInfo = (SessionInfo) request.getSession()
					.getAttribute(BaseConstants.USER_SESSION_KEY);
			if (sessionInfo != null && sessionInfo.getUser() != null) {
				return true;
			} else {
				logger.debug("登录已超时");
				// 如果是ajax请求响应头会有，x-requested-with；
				if (request.getHeader("X-Requested-With") != null
						&& request.getHeader("X-Requested-With")
								.equalsIgnoreCase("XMLHttpRequest")){
					request.setAttribute("message", "登录已超时,请重新登录!");
					response.setHeader("sessionstatus", "timeout");// 在响应头设置session状态
					return false;
				} else {
					request.setAttribute("message", "登录已超时,请重新登录!");
					request.getRequestDispatcher("/login.jsp").forward(request, response);
					return false;
				}
			}
		}
	}

}
