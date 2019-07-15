package com.vission.mf.base.sellms.smsfileinfo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.sellms.smsfileinfo.db.SMS_FILE_INFO;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.sellms.smsfileinfo.po.SmsFileInfo;
/**
 * 作者：lkj
 * 描述：SmsFileInfoDao 数据表模块
 * 日期：2019-6-30 18:13:58
 * 类型：DAO文件
 */
@SuppressWarnings("serial")
@Service("smsFileInfoDao")
public class SmsFileInfoDao extends
		SimpleHibernateTemplate<SmsFileInfo, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public SmsFileInfoDao() {
		super(SmsFileInfo.class);
	}

}
