package com.vission.mf.base.engine.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;

import com.vission.mf.base.engine.database.bo.Database;
import com.vission.mf.base.engine.database.dialect.DB2Dialect;
import com.vission.mf.base.engine.database.dialect.Dialect;
import com.vission.mf.base.engine.database.dialect.InformixDialect;
import com.vission.mf.base.engine.database.dialect.MySQLDialect;
import com.vission.mf.base.engine.database.dialect.OracleDialect;
import com.vission.mf.base.engine.database.dialect.SQLServerDialect;
import com.vission.mf.base.engine.database.dialect.SybaseDialect;
import com.vission.mf.base.enums.DatabaseConstants;
import com.vission.mf.base.exception.ServiceException;

public class DatabaseManage {
	
	private static Logger logger = Logger.getLogger(DatabaseManage.class);
	private static Map<String, Database> map = new Hashtable<String, Database>(); // 放置连接对象所需要的参数
	
	/**
	 * 获得当前系统数据库连接
	 * @return
	 * @throws ServiceException
	 */
	public static Connection getCurrConnection()throws ServiceException{
	Connection conn = null;
	try {
		Properties p = new Properties();
		p.load(DatabaseManage.class.getResourceAsStream("/config.properties"));
		Class.forName(p.getProperty("dataSource.driverClassName"));
		String url = p.getProperty("dataSource.url");
		String user = p.getProperty("dataSource.username");
		String pass = p.getProperty("dataSource.password");
		DriverManager.setLoginTimeout(3000);
		conn = DriverManager.getConnection(url, user, pass);
	} catch (ClassNotFoundException e) {
		logger.error("ClassNotFoundException异常:" + e.getMessage());
		throw new ServiceException("ClassNotFoundException异常", e);
	} catch (SQLException e) {
		logger.error("SQLException异常:" + e.getMessage());
		throw new ServiceException("SQLException异常", e);
	} catch (IOException e) {
		logger.error("读取配置文件异常:" + e.getMessage());
		throw new ServiceException(e);
	}
	return conn;
	}
	
	/**
	 * 获取连接
	 */
	public static Connection getConnection(Database database)
			throws ServiceException {
		Connection conn = null;
		try {
			if(database.getPoolType()==null || "".equals(database.getPoolType()) || database.getPoolType().equals("0")){//不使用连接池
				Class.forName(database.getJdbcDriver());
				String url = database.getJdbcUrl();
				String user = database.getDbUser();
				String pass = database.getDbPwd();
				DriverManager.setLoginTimeout(3000);
				conn = DriverManager.getConnection(url, user, pass);
			} else {
				if (!map.isEmpty() && map.get(database.getDbName()) != null) {
					Database db = map.get(database.getDbName());
					boolean isNotNew = db.getJdbcUrl().equals(
							database.getJdbcUrl())
							&& db.getDbUser().equals(
									database.getDbUser())
							&& db.getDbPwd().equals(
									database.getDbPwd());
					if (!isNotNew) {
						synchronized (DatabaseManage.class) {
							try {
								ProxoolFacade.removeConnectionPool(database.getDbName());
							} catch (ProxoolException e) {
							}
						}
					}
				}
				if (!checkPoolExist(database.getDbName())) {
					synchronized (DatabaseManage.class) {
						if (!checkPoolExist(database.getDbName())) {
							initDBPool_Proxool(database);
						}
					}
				}
				conn = getConnFromPool(database.getDbName());
			}
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException异常:" + e.getMessage());
			throw new ServiceException("ClassNotFoundException异常", e);
		} catch (SQLException e) {
			logger.error("SQLException异常:" + e.getMessage());
			throw new ServiceException("SQLException异常", e);
		}
		return conn;
	}
	

	/**
	 * 测试连接
	 */
	public static void testConnection(Database database)
			throws ServiceException {
		Connection conn = null;
		try {
			Class.forName(database.getJdbcDriver());
			String url = database.getJdbcUrl();
			String user = database.getDbUser();
			String pass = database.getDbPwd();
			DriverManager.setLoginTimeout(3000);
			conn = DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException异常:" + e.getMessage());
			throw new ServiceException("连接失败：没有找到JdbcDriver", e);
		} catch (SQLException e) {
			logger.error("SQLException异常:" + e.getMessage());
			throw new ServiceException("连接失败：SQLException", e);
		} finally {
			closeConnection(conn);
		}
	}
	

	/**
	 * 关闭连接
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				conn = null;
			}
		}
	}

	/**
	 * 关闭Statement
	 */
	public static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				stmt = null;
			}
		}
	}
	/**
	 * 关闭ResultSet
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				rs = null;
			}
		}
	}

	/**
	 * 回滚
	 */
	public static void rollbackConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 获取Dialect
	 */
	public static Dialect getDialect(int dbType) {
		if (dbType == DatabaseConstants.DB_TYPE_ORACLE) {
			return new OracleDialect();
		} else if (dbType == DatabaseConstants.DB_TYPE_SQLSERVER) {
			return new SQLServerDialect();
		} else if (dbType == DatabaseConstants.DB_TYPE_MYSQL) {
			return new MySQLDialect();
		} else if (dbType == DatabaseConstants.DB_TYPE_DB2) {
			return new DB2Dialect();
		} else if (dbType == DatabaseConstants.DB_TYPE_INFORMIX) {
			return new InformixDialect();
		} else if (dbType == DatabaseConstants.DB_TYPE_SYBASE) {
			return new SybaseDialect();
		} 
		return null;
	}
	
	/**
	 * 获得据库类型
	 * @return
	 * @throws ServiceException
	 */
	public static int getDialectType(Connection conn)throws ServiceException{
		int r = 0;
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			String dataBaseType = dbmd.getDatabaseProductName();
			if(dataBaseType.toUpperCase().indexOf("DB2")>=0){
				r = 1;
			}
		} catch (Exception e) {
			logger.error("读取配置文件出错！");
			throw new ServiceException(e);
		}
		return r;
	}

	/**
	 * 初始Proxool连接池
	 */
	private static void initDBPool_Proxool(Database database)
			throws ServiceException {
		String dsID = database.getDbName();
		String connUrl = database.getJdbcUrl();
		String sDriver = database.getJdbcDriver();
		String username = database.getDbUser();
		String password = database.getDbPwd();
		String sMaxConnect = String.valueOf(database.getMaxLinkNum());
		String sMinConnect = String.valueOf(database.getMinLinkNum());
		String sMaxNewConnect = String.valueOf(database.getMaxReqNum());
		String sPrototypeConnect = String.valueOf(database.getWaitLinkNum());
		String sMaxSleepTime = String.valueOf(database.getMaxWaitTime());
		String sMaxActiveTime = String.valueOf(database.getMaxActTime());
		String sConnectLiefTime = String.valueOf(database.getMaxLife());
		String sTestSql = database.getTestSql();
		
		String sPoolName = dsID;
		Connection conn = null;
		Properties prop = new Properties();
		prop.put("jdbc-0.proxool.alias", sPoolName);
		prop.put("jdbc-0.proxool.driver-url", connUrl);
		prop.put("jdbc-0.user", username);
		prop.put("jdbc-0.password", password);
		prop.put("jdbc-0.proxool.driver-class", sDriver);
		prop.put("jdbc-0.proxool.maximum-connection-count", sMaxConnect);
		prop.put("jdbc-0.proxool.minimum-connection-count", sMinConnect);
		prop.put("jdbc-0.proxool.prototype-count", sPrototypeConnect);
		prop.put("jdbc-0.proxool.simultaneous-build-throttle", sMaxNewConnect);
		prop.put("jdbc-0.proxool.house-keeping-sleep-time", sMaxSleepTime);
		prop.put("jdbc-0.proxool.maximum-active-time", sMaxActiveTime);
		prop.put("jdbc-0.proxool.maximum-connection-lifetime", sConnectLiefTime);
		if ((sTestSql != null) && (!sTestSql.equals(""))) {
			prop.put("jdbc-0.proxool.house-keeping-test-sql", sTestSql);
		}
		prop.put("jdbc-0.proxool.statistics-log-level", "INFO");
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
		} catch (ClassNotFoundException e) {
		}
		try {
			PropertyConfigurator.configure(prop);
		} catch (ProxoolException e) {
			try {
				ProxoolFacade.removeConnectionPool(sPoolName);
			} catch (ProxoolException e1) {
			}
			throw new ServiceException("ProxoolException", (Throwable) e);
		}
		try {
			conn = getConnFromPool(sPoolName);
			map.put(sPoolName, database);
		} catch (ServiceException e) {
			try {
				ProxoolFacade.removeConnectionPool(sPoolName);
			} catch (ProxoolException e1) {
			} finally {
				map.put(sPoolName, null);
			}
			throw e;
		} finally {
			closeConnection(conn);
		}
	}
	
	/**
	 * 从proxool连接池中获得连接
	 * 
	 * @param sPoolName
	 *            连接池名称
	 * @return Connection 获得的连接
	 * @throws ServiceException
	 */
	private static Connection getConnFromPool(String sPoolName)
			throws ServiceException {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("proxool." + sPoolName);
		} catch (SQLException e) {

			throw new ServiceException("SQLException", (Throwable) e);
		}
		return conn;
	}
	
	/**
	 * 检查连接池是否已存在
	 * 
	 * @param poolName
	 *            连接池名称
	 * @return boolean 是否存在
	 */
	private static boolean checkPoolExist(String poolName) {
		String poolNames[] = ProxoolFacade.getAliases();
		for (int index = 0; index < poolNames.length; index++) {
			if (poolNames[index]!=null && poolNames[index].equalsIgnoreCase(poolName))
				return true;
		}
		return false;
	}
}
