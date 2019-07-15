package com.vission.mf.base.mms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.MmsXfType;

/**
 * 功能/模块 ：消费类型管理
 */
@SuppressWarnings("serial")
@Service("xfTypeDao")
public class XfTypeDao extends
		SimpleHibernateTemplate<MmsXfType, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public XfTypeDao() {
		super(MmsXfType.class);
	}

	/**
	 * 新增时判断是否与其他用户同名
	 */
	public boolean isExistLoginName(String loginName) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where loginName=?", loginName);
		return count > 0;
	}

	public boolean isExistUserName(String userName) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where userName=? ", userName);
		return count > 0;
	}

/*	*//**
	 * 根据登录名查询用户
	 * 
	 * @return
	 *//*
	public SysUserInfo getUserByLoginName(String loginName) {
		return findUniqueByProperty("loginName", loginName);
	}*/

	/**
	 * 批量保存用户 待测试
	 */
	public void saveMoreXfType(List<MmsXfType> list) {
		for (int i = 0, j = list.size(); i < j; i++) {
			onlySave(list.get(i));
			if (i % 20 == 0) {
				this.getSession().flush();
				this.getSession().clear();
			}
		}
	}

}
