package com.vission.mf.base.enums.db;

/**
* 功能/模块 ：调度策略信息表
*/
public   class SYS_TRIGGER_INFO {
	//库表信息
	public static final String TABLE_NAME="SYS_TRIGGER_INFO";
	public static final String TRIGGER_ID="TRIGGER_ID";
	public static final String TRIGGER_NAME="TRIGGER_NAME";
	public static final String TRIGGER_RMK="TRIGGER_RMK";
	public static final String TRIGGER_TYPE="TRIGGER_TYPE";
	public static final String CRON_EXPRESSION="CRON_EXPRESSION";
	public static final String START_DELAY="START_DELAY";
	public static final String REPEAT_INTERVAL="REPEAT_INTERVAL";
	public static final String REPEAT_COUNT="REPEAT_COUNT";
	//栏位常量值
	public static final int TRIGGER_TYPE_CRON=1;
	public static final int TRIGGER_TYPE_SIMPLE=2;
}
