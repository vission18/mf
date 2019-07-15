package com.vission.mf.base.sellms.smssigninfo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.sellms.smssigninfo.db.SMS_SIGN_INFO;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.sellms.smssigninfo.po.SmsSignInfo;
/**
 * 作者：lkj
 * 描述：SmsSignInfoDao 数据表模块
 * 日期：2019-6-27 1:07:19
 * 类型：DAO文件
 */
@SuppressWarnings("serial")
@Service("smsSignInfoDao")
public class SmsSignInfoDao extends
		SimpleHibernateTemplate<SmsSignInfo, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public SmsSignInfoDao() {
		super(SmsSignInfo.class);
	}

}
