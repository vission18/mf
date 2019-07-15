package com.vission.mf.base.sellms.smsvarinfo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.sellms.smsvarinfo.db.SMS_VAR_INFO;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.sellms.smsvarinfo.po.SmsVarInfo;
/**
 * 作者：lkj
 * 描述：SmsVarInfoDao 数据表模块
 * 日期：2019-6-30 17:40:32
 * 类型：DAO文件
 */
@SuppressWarnings("serial")
@Service("smsVarInfoDao")
public class SmsVarInfoDao extends
		SimpleHibernateTemplate<SmsVarInfo, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public SmsVarInfoDao() {
		super(SmsVarInfo.class);
	}

}
