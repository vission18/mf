package com.vission.mf.base.acf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.vission.mf.base.acf.po.AcfModInfo;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;

/**
 * 功能/模块 :模块信息处理DAO
 */
@SuppressWarnings({ "serial", "deprecation" })
@Repository
public class AcfModInfoDao extends SimpleHibernateTemplate<AcfModInfo, String> {

	@Autowired
	private SimpleJdbcTemplate simpleJdbcTemplate = null;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

	public AcfModInfoDao() {
		super(AcfModInfo.class);
	}
	
}
