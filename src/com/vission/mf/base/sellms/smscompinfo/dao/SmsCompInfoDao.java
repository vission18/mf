package com.vission.mf.base.sellms.smscompinfo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.sellms.smscompinfo.db.SMS_COMP_INFO;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.sellms.smscompinfo.po.SmsCompInfo;
/**
 * 作者：lkj
 * 描述：SmsCompInfoDao 数据表模块
 * 日期：2019-6-21 9:29:04
 * 类型：DAO文件
 */
@SuppressWarnings("serial")
@Service("smscompinfoDao")
public class SmsCompInfoDao extends
		SimpleHibernateTemplate<SmsCompInfo, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public SmsCompInfoDao() {
		super(SmsCompInfo.class);
	}

}
