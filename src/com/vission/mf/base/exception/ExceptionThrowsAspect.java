package com.vission.mf.base.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import com.vission.mf.base.util.ExceptionUtil;

public class ExceptionThrowsAspect {
	protected Logger logger = Logger.getLogger(getClass());

	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Object retVal = null;
		try {
			retVal = pjp.proceed();
		} catch (Exception e) {
			logger.error("方法: " + pjp.getTarget().getClass().getName()
					+ "." + pjp.getSignature().getName() + "抛出异常");
			logger.error("具体信息为："+ExceptionUtil.getExceptionMessage(e));
			Object[] objs = pjp.getArgs();
			HttpServletRequest request = (HttpServletRequest) objs[0];
			HttpServletResponse response = (HttpServletResponse) objs[1];
			String msg = e.getMessage();
			int idx = msg.indexOf("ServiceException:");
			if(idx>0){
				msg = msg.substring(idx+17);
			}
			request.setAttribute("errorMsg", msg);
			request.getRequestDispatcher("/error/500.jsp").forward(request,response);
			return null;
		}
		return retVal;
	}

}
