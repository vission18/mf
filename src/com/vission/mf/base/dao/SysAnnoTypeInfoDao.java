package com.vission.mf.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.enums.db.SYS_ANNO_TYPE_INFO;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysAnnoTypeInfo;

/**
 * 功能/模块 ：公告管理
 * 
 */
@SuppressWarnings("serial")
@Service("sysAnnoTypeInfoDao")
public class SysAnnoTypeInfoDao extends
		SimpleHibernateTemplate<SysAnnoTypeInfo, String> {

	public SysAnnoTypeInfoDao() {
		super(SysAnnoTypeInfo.class);
	}

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

	/**
	 * 修改时判断是否与其他用户同名
	 */
	public boolean isExistName(String Name, String typeId) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where typeName=? and typeId=?", Name, typeId);
		return count > 0;
	}
	
	/**
	 * 新增时判断是否与其他用户同名  
	 */
	public boolean isExistName(String typeName) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where typeName=?", typeName);
		return count > 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SysAnnoTypeInfo getById(String typeId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select *  from ").append(SYS_ANNO_TYPE_INFO.TABLE_NAME);
		sb.append(" where ").append(SYS_ANNO_TYPE_INFO.TYPE_ID);
		sb.append(" =:typeId");
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(
				"typeId", typeId);
		logger.info("JDBC:" + sb.toString());

		List<SysAnnoTypeInfo> list=  namedParameterJdbcTemplate.query(sb
				.toString(), mapSqlParameterSource, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				SysAnnoTypeInfo sysAnnoTypeInfo = new SysAnnoTypeInfo();
				sysAnnoTypeInfo.setTypeId(rs
						.getString(SYS_ANNO_TYPE_INFO.TYPE_ID));
				sysAnnoTypeInfo.setTypeName(rs
						.getString(SYS_ANNO_TYPE_INFO.TYPE_NAME));
				sysAnnoTypeInfo.setTypeRmk(rs
						.getString(SYS_ANNO_TYPE_INFO.TYPE_RMK));
				return sysAnnoTypeInfo;
			}
		});
		if(list==null||list.size()==0){
			return new SysAnnoTypeInfo();
		}else{
			return (SysAnnoTypeInfo)list.get(0);
		}

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SysAnnoTypeInfo getAnnoTypesByName(String typeName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select *  from ").append(SYS_ANNO_TYPE_INFO.TABLE_NAME);
		sb.append(" where ").append(SYS_ANNO_TYPE_INFO.TYPE_NAME);
		sb.append(" =:typeName");
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(
				"typeName", typeName);
		logger.info("JDBC:" + sb.toString());

		List<SysAnnoTypeInfo> list=  namedParameterJdbcTemplate.query(sb
				.toString(), mapSqlParameterSource, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				SysAnnoTypeInfo sysAnnoTypeInfo = new SysAnnoTypeInfo();
				sysAnnoTypeInfo.setTypeId(rs
						.getString(SYS_ANNO_TYPE_INFO.TYPE_ID));
				sysAnnoTypeInfo.setTypeName(rs
						.getString(SYS_ANNO_TYPE_INFO.TYPE_NAME));
				sysAnnoTypeInfo.setTypeRmk(rs
						.getString(SYS_ANNO_TYPE_INFO.TYPE_RMK));
				return sysAnnoTypeInfo;
			}
		});
		if(list==null||list.size()==0){
			return new SysAnnoTypeInfo();
		}else{
			return (SysAnnoTypeInfo)list.get(0);
		}

	}

}