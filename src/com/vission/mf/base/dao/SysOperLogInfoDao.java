package com.vission.mf.base.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysOperLogInfo;

/**
 * 功能/模块 :操作日志类
 */
@SuppressWarnings("serial")
@Service("sysOperLogInfoDao")
public class SysOperLogInfoDao extends
		SimpleHibernateTemplate<SysOperLogInfo, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public SysOperLogInfoDao() {
		super(SysOperLogInfo.class);
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void datchDelete(String ids){
			Map map=new HashMap<String,String>();
			map.put("log_id", ids);
			SqlParameterSource paramSource = new MapSqlParameterSource(map); 
			namedParameterJdbcTemplate.update("delete from "+SYS_OPERLOG_INFO.TABLE_NAME+" where "+SYS_OPERLOG_INFO.LOG_ID +" in( "+ids+" )", map);      
	}

}
