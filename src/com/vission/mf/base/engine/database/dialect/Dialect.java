package com.vission.mf.base.engine.database.dialect;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vission.mf.base.engine.database.bo.Column;
import com.vission.mf.base.engine.database.bo.Forkey;
import com.vission.mf.base.engine.database.bo.Index;
import com.vission.mf.base.engine.database.bo.Table;
import com.vission.mf.base.exception.ServiceException;

public abstract class Dialect {
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(Dialect.class);
	
	/**
	 * 获取表索引的sql
	 */
	public String getTableIndexSQL() throws ServiceException {
		throw new ServiceException(getClass().getName() + " does not support getTableIndexSQL");
	}
	/**
	 * 获取创建表的sql
	 */
	public String getCreateTableSQL(Table table, List<Column> columns) throws ServiceException {
		throw new ServiceException(getClass().getName() + " does not support getCreateTableSQL");
	}
	/**
	 * 获取创建索引的sql
	 */
	public String getCreateIndexSQL(Table table, List<Index> indexs) throws ServiceException {
		throw new ServiceException(getClass().getName() + " does not support getCreateIndexSQL");
	}
	/**
	 * 获取创建外键的sql
	 */
	public String getCreateForkeySQL(Table table, List<Forkey> forkeys) throws ServiceException {
		throw new ServiceException(getClass().getName() + " does not support getCreateForkeySQL");
	}
	/**
	 * 获取创建外键的sql
	 */
	public String getTableSpaceSQL() throws ServiceException {
		throw new ServiceException(getClass().getName() + " does not support getTableSpaceSQL");
	}
	/**
	 * 判断是不是唯一索引
	 */
	public boolean isUniqueIndex(String str) throws ServiceException {
		throw new ServiceException(getClass().getName() + " does not support isUniqueIndex");
	}
	/**
	 * 获取第一条记录的sql
	 */
	public String getTopOneSql(String sql) throws ServiceException {
		throw new ServiceException(getClass().getName() + " does not support getTopOneSql");
	}
	/**
	 * 获取分页查询sql
	 */
	public String getPageSql(String sql, int pageSize, int startRow) throws ServiceException {
		throw new ServiceException(getClass().getName() + " does not support getPageSql");
	}
}
