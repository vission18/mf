package com.vission.mf.base.exception;

/**
* 功能/模块 ：继承自RuntimeException,会触发Spring的事务管理引起事务回退
*/
public class ServiceException extends Exception {

	private static final long serialVersionUID = 3583566093089790852L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
}
