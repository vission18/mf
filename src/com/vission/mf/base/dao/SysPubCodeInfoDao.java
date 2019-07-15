package com.vission.mf.base.dao;

import org.springframework.stereotype.Service;

import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysPubCodeInfo;

/**
 * 功能/模块 ：系统菜单数据访问类
 */
@SuppressWarnings("serial")
@Service("sysPubcodeInfoDao")
public class SysPubCodeInfoDao extends
	SimpleHibernateTemplate<SysPubCodeInfo, String> {
	
	public SysPubCodeInfoDao() {
		super(SysPubCodeInfo.class);
	}
	
	/**
	 * 修改时判断是否与其他用户同名
	 */
	public boolean isExistCodeName(String codeName, String codeId) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where codeName=? and codeId<>?", codeName, codeId);
		return count > 0;
	}
	/**
	 * 新增时判断是否与其他用户同名
	 */
	public boolean isExistCodeName(String codeName) {
		long count = findLong("select count(*) from "
				+ this.getEntityClassName() + " where codeName=?", codeName);
		return count > 0;
	}

}
