package com.vission.mf.base.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.vission.mf.base.BaseEntity;
import com.vission.mf.base.enums.db.SYS_TRIGGER_INFO;

/**
* 功能/模块 ：调度策略信息表
*/
@SuppressWarnings("serial")
@Entity
@Table(name = SYS_TRIGGER_INFO.TABLE_NAME)
public class SysTriggerInfo extends BaseEntity {

	private String triggerId;
	private String triggerName;
	private String triggerRmk;
	private int triggerType;
	private String cronExpression;
	private long startDelay;
	private long repeatInterval;
	private int repeatCount;
	
	@Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
	@Column(name = SYS_TRIGGER_INFO.TRIGGER_ID)
	public String getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}
	
	@Column(name = SYS_TRIGGER_INFO.TRIGGER_NAME)
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	
	@Column(name = SYS_TRIGGER_INFO.TRIGGER_RMK)
	public String getTriggerRmk() {
		return triggerRmk;
	}
	public void setTriggerRmk(String triggerRmk) {
		this.triggerRmk = triggerRmk;
	}
	@Column(name = SYS_TRIGGER_INFO.TRIGGER_TYPE)
	public int getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(int triggerType) {
		this.triggerType = triggerType;
	}
	@Column(name = SYS_TRIGGER_INFO.CRON_EXPRESSION)
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	@Column(name = SYS_TRIGGER_INFO.START_DELAY)
	public long getStartDelay() {
		return startDelay;
	}
	public void setStartDelay(long startDelay) {
		this.startDelay = startDelay;
	}
	@Column(name = SYS_TRIGGER_INFO.REPEAT_INTERVAL)
	public long getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	@Column(name = SYS_TRIGGER_INFO.REPEAT_COUNT)
	public int getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}


	





}