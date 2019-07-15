package com.vission.mf.base.acf.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.vission.mf.base.acf.db.ACF_COL_INFO;
import com.vission.mf.base.acf.po.AcfColInfo;
import com.vission.mf.base.engine.database.dialect.Dialect;
import com.vission.mf.base.engine.database.dialect.MySQLDialect;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.bo.DataGrid;

/**
 * 功能/模块 :字段信息处理DAO
 */
@SuppressWarnings({ "serial", "deprecation" })
@Repository
public class AcfColInfoDao extends SimpleHibernateTemplate<AcfColInfo, String> {

	@Autowired
	private SimpleJdbcTemplate simpleJdbcTemplate = null;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

	public AcfColInfoDao() {
		super(AcfColInfo.class);
	}
	
	public DataGrid findDataGrid(DataGrid dataGrid, int pageSize,
			String modId) {
		StringBuffer sql = new StringBuffer();
		String sqlCount = "";
		sql.append(" select t.COL_ID,t.MOD_ID,t.COL_ENG_NAME,t.COL_CHA_NAME,t.COL_TYPE,t.DEFAULT_VAL,t.COL_DESC,t.IS_PK,t.IS_NULL,t.DEL_FLAG,t.LAST_MOD_USER,t.LAST_MOD_TIME ");
		sql.append(" from ACF_COL_INFO t ");
		sql.append(" where 1=1 and t.mod_id = '"+modId+"' ");
		
		sqlCount = sql.toString();
			
		sql.append(" order by t.mod_id ");
		
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		//通过引擎自动生成条件语句
		String pageSql = sql.toString();
		Dialect dialect = new MySQLDialect();
		try {
			pageSql = dialect.getPageSql(pageSql, pageSize, dataGrid.getStartRow());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		logger.info("JDBC:" + pageSql);
		List<AcfColInfo> list = namedParameterJdbcTemplate.query(
					pageSql, mapSqlParameterSource, new AcfColInfoRowMapper());
		StringBuffer totalSql = new StringBuffer();
		totalSql.append(" select count(*) from ( ");
		totalSql.append(sqlCount);
		totalSql.append(" ) t ");
		int total = namedParameterJdbcTemplate.queryForInt(totalSql.toString(), mapSqlParameterSource);
		dataGrid.setTotal(total);
		dataGrid.setRows(list);
		return dataGrid;
	}
	
	
	public AcfColInfo getColInfoByColName(String colName,String modId) {
		StringBuffer sql = new StringBuffer();
		String sqlCount = "";
		sql.append(" select t.COL_ID,t.MOD_ID,t.COL_ENG_NAME,t.COL_CHA_NAME,t.COL_TYPE,t.DEFAULT_VAL,t.COL_DESC,t.IS_PK,t.IS_NULL,t.DEL_FLAG,t.LAST_MOD_USER,t.LAST_MOD_TIME ");
		sql.append(" from ACF_COL_INFO t ");
		sql.append(" where 1=1 and t.mod_id = '"+modId+"' ");
		
		if(colName !=null && !"".equals(colName)){
			sql.append(" and t.COL_ENG_NAME ='"+colName+"' ");
		}
		
		sqlCount = sql.toString();
			
		sql.append(" order by t.mod_id ");
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		List<AcfColInfo> list = namedParameterJdbcTemplate.query(
				sql.toString(), mapSqlParameterSource,
				new AcfColInfoRowMapper());
		if(list == null || list.size() <= 0){
			return null;
		}
		return list.get(0);
	}

	protected class AcfColInfoRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
			AcfColInfo colInfo = new AcfColInfo();
			colInfo.setColId(rs.getString(ACF_COL_INFO.COL_ID));
			colInfo.setModId(rs.getString(ACF_COL_INFO.MOD_ID));
			colInfo.setColChaName(rs.getString(ACF_COL_INFO.COL_CHA_NAME));
			colInfo.setColEngName(rs.getString(ACF_COL_INFO.COL_ENG_NAME));
			colInfo.setColType(rs.getString(ACF_COL_INFO.COL_TYPE));
			colInfo.setIsNull(rs.getString(ACF_COL_INFO.IS_NULL));
			colInfo.setIsPk(rs.getString(ACF_COL_INFO.IS_PK));
			colInfo.setLastModUser(rs.getString(ACF_COL_INFO.LAST_MOD_USER));
			colInfo.setLastModTime(rs.getString(ACF_COL_INFO.LAST_MOD_TIME));
			colInfo.setColDesc(rs.getString(ACF_COL_INFO.COL_DESC));
			colInfo.setDelFlag(rs.getString(ACF_COL_INFO.DEL_FLAG));
			colInfo.setDefaultVal(rs.getString(ACF_COL_INFO.DEFAULT_VAL));
			return colInfo;
		}
	}
}
