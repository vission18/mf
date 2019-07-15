package com.vission.mf.base.util;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vission.mf.base.exception.ServiceException;

public class SpringUtil {
	public static DataSource ds = null;
	private static ClassPathXmlApplicationContext ac = null;

	/**
	 * 通过spring配置获取数据源 
	 */
	public static Connection getConnection() throws ServiceException {
		try {
			if (ds == null) {
				ac = new ClassPathXmlApplicationContext("spring.xml");
			}
			if (ds == null) {
				ds = (DataSource) ac.getBean("dataSource");
			}
			return ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
}
