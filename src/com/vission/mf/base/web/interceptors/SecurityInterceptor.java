package com.vission.mf.base.web.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.model.bo.SessionInfo;

public class SecurityInterceptor implements HandlerInterceptor {

	private static final Logger logger = Logger.getLogger(SecurityInterceptor.class);

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
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {

	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在调用controller具体方法前拦截
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		logger.debug(url);
		if (excludeUrls.contains(url)) {
			return true;
		} else {
			SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY);
			if (BaseConstants.SUPER_USER.equals(sessionInfo.getUser().getLoginName())) {// 超管不需要验证权限
				return true;
			} 
			else {
				List<String> urls = sessionInfo.getMenuUrls();
				if (urls !=null && urls.contains(url)) {
					return true;
				} else {
					request.setAttribute("msg", "您没有访问此资源的权限！<br/>请联系超管赋予您<br/>[" + url + "]<br/>的资源访问权限！");
					request.getRequestDispatcher("/error/noSecurity.jsp").forward(request, response);
					return false;
				}
			}
		}
	}
}
