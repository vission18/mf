package com.vission.mf.base.engine.database.bo;

public class Database {
	private String jdbcDriver;// IP地址
	private String jdbcUrl;// 数据源端口
	private int dbType;// 数据源类型
	private String dbUser;
	private String dbPwd;
	private String poolType;//连接池类型
	private String dbName;//数据连接名称
	
	//proxool连接池参数
	private int maxLinkNum;      //最大连接数---
	private int minLinkNum;   //最小连接数---
	private int waitLinkNum;//空闲连接数
	private String testSql;
    private int maxReqNum;//最大请求数
    private int maxWaitTime;//最长等待时间
    private int maxActTime;//最长活动时间
    private int maxLife;//最长生命周期
	
	public Database(){}
	
	public Database(String dbName, String jdbcDriver, String jdbcUrl, int dbType, String poolType,
			String dbUser, String dbPwd, int maxLinkNum , int minLinkNum, int waitLinkNum,
			int maxReqNum, int maxWaitTime, int maxActTime, int maxLife, String testSql){
		this.dbName = dbName;
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.dbType = dbType;
		this.poolType = poolType;
		this.dbUser = dbUser;
		this.dbPwd = dbPwd;
		this.maxLinkNum = maxLinkNum;
		this.minLinkNum = minLinkNum;
		this.waitLinkNum = waitLinkNum;
		this.maxReqNum = maxReqNum;
		this.maxWaitTime = maxWaitTime;
		this.maxActTime = maxActTime;
		this.maxLife = maxLife;
		this.testSql = testSql;
	}
	
	public String getJdbcDriver() {
		return jdbcDriver;
	}
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public int getDbType() {
		return dbType;
	}
	public void setDbType(int dbType) {
		this.dbType = dbType;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPwd() {
		return dbPwd;
	}
	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}

	public String getPoolType() {
		return poolType;
	}

	public void setPoolType(String poolType) {
		this.poolType = poolType;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}

	public int getMaxLinkNum() {
		return maxLinkNum;
	}

	public void setMaxLinkNum(int maxLinkNum) {
		this.maxLinkNum = maxLinkNum;
	}

	public int getMinLinkNum() {
		return minLinkNum;
	}

	public void setMinLinkNum(int minLinkNum) {
		this.minLinkNum = minLinkNum;
	}

	public int getWaitLinkNum() {
		return waitLinkNum;
	}

	public void setWaitLinkNum(int waitLinkNum) {
		this.waitLinkNum = waitLinkNum;
	}

	public String getTestSql() {
		return testSql;
	}

	public void setTestSql(String testSql) {
		this.testSql = testSql;
	}

	public int getMaxReqNum() {
		return maxReqNum;
	}

	public void setMaxReqNum(int maxReqNum) {
		this.maxReqNum = maxReqNum;
	}

	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public int getMaxActTime() {
		return maxActTime;
	}

	public void setMaxActTime(int maxActTime) {
		this.maxActTime = maxActTime;
	}
	
}
