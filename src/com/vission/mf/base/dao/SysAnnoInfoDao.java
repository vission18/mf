package com.vission.mf.base.dao;

import org.springframework.stereotype.Service;

import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysAnnoInfo;

/**
 * 功能/模块 ：公告管理
 * 
 */
@SuppressWarnings("serial")
@Service("sysAnnoInfoDao")
public class SysAnnoInfoDao extends
		SimpleHibernateTemplate<SysAnnoInfo, String> {

	public SysAnnoInfoDao() {
		super(SysAnnoInfo.class);
	}

	/**
	 * 修改时判断是否与其他用户同名
	 * ename
	 */
	public boolean isExistTitle(String title, String annoId) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where annoTitle=? and annoId=?", title, annoId);
		return count > 0;
	}
	
	/**
	 * 新增时判断是否与其他用户同名  
	 * 按cname
	 */
	public boolean isExistTitle(String title) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where annoTitle=?", title);
		return count > 0;
	}
}