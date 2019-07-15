package com.vission.mf.base;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
/**
* 功能/模块 ：基础代码
 */
public class BaseClass {
	/* 日志 */
	protected Logger logger = Logger.getLogger(getClass());
	
	public void debug(String message) {
		logger.debug(message);
	}
	public void error(String message) {
		logger.error(message);
	}
	public void info(String message) {
		logger.info(message);
	}
	
	/* 多语言 */
	private MessageSource messageSource;
	@Resource(name="messageSource")
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	public String getMessage(String key) {
		return this.messageSource.getMessage(key, null, Locale.getDefault());
	}
}
