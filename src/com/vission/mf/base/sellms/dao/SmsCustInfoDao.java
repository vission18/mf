package com.vission.mf.base.sellms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.vission.mf.base.engine.database.dialect.DB2Dialect;
import com.vission.mf.base.engine.database.dialect.Dialect;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.sellms.model.db.SMS_CUST_INFO;
import com.vission.mf.base.sellms.model.po.SmsCustInfo;

/**
 * 功能/模块 :客户信息处理DAO
 */
@SuppressWarnings({ "serial", "deprecation" })
@Repository
public class SmsCustInfoDao extends
		SimpleHibernateTemplate<SmsCustInfo, String> {

	@Autowired
	private SimpleJdbcTemplate simpleJdbcTemplate = null;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

	public SmsCustInfoDao() {
		super(SmsCustInfo.class);
	}

	/**
	 * 新增或修改
	 * 
	 * @param entity
	 */
	/*
	 * public void saveOrUpdate(SmsCustInfo entity){ super.merge(entity); }
	 */

	/**
	 * 删除客户信息
	 * 
	 * @param dmId
	 * @return
	 */
	public int deleteCustInfoByCustId(String custId) {
		StringBuffer sb = new StringBuffer();
		sb.append("delete from ").append(SMS_CUST_INFO.TABLE_NAME);
		sb.append(" where ").append(SMS_CUST_INFO.CUST_ID);
		sb.append(" =:custId");

		Map<String, String> map = new HashMap<String, String>();
		map.put("custId", custId);
		return namedParameterJdbcTemplate.update(sb.toString(), map);
	}

	/**
	 * 列表查询
	 * 
	 * @param dataGrid
	 * @param pageSize
	 * @param custInfo
	 * @param sessionInfo
	 * @return
	 */
	public DataGrid findDataGrid(DataGrid dataGrid, int pageSize,
			SmsCustInfo custInfo, SessionInfo sessionInfo) {
		StringBuffer sql = new StringBuffer();
		String sqlCount = "";
		sql.append(" select ");
		sql.append(
				" cust.CUST_ID,cust.CUST_NAME,cust.CUST_ALIAS_NAME,cust.MOBILE,cust.TEL_PHONE,cust.ADDRESS,")
				.append(" cust.CUST_TYPE,cust.CUST_DESC,cust.CUST_STATUS,cust.DATA_BY,cust.CREATE_USER,cust.CREATE_TIME,")
				.append(" cust.LAST_MOD_USER,cust.LAST_MOD_TIME ");
		sql.append(" from ").append(SMS_CUST_INFO.TABLE_NAME).append(" cust ");
		sql.append(" where 1=1 ");

		if (StringUtils.isNotEmpty(custInfo.getCustName())) {
			String custName = custInfo.getCustName().trim();
			sql.append(" and ").append(SMS_CUST_INFO.CUST_NAME)
					.append(" like '%" + custName + "%'");
		}
		if (StringUtils.isNotEmpty(custInfo.getCustStatus())) {
			sql.append(" and ").append(SMS_CUST_INFO.CUST_STATUS)
					.append(" = '" + custInfo.getCustStatus() + "'");
		}
		sqlCount = sql.toString();

		sql.append(" order by ").append(SMS_CUST_INFO.LAST_MOD_TIME)
				.append(" desc");

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		// 通过引擎自动生成条件语句
		String pageSql = sql.toString();
		Dialect dialect = new DB2Dialect();
		try {
			pageSql = dialect.getPageSql(pageSql, pageSize,
					dataGrid.getStartRow());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		logger.info("JDBC:" + pageSql);
		List<SmsCustInfo> list = namedParameterJdbcTemplate.query(pageSql,
				mapSqlParameterSource, new SmsCustInfoRowMapper());
		StringBuffer totalSql = new StringBuffer();
		totalSql.append(" select count(*) from ( ");
		totalSql.append(sqlCount);
		totalSql.append(" ) t ");
		int total = namedParameterJdbcTemplate.queryForInt(totalSql.toString(),
				mapSqlParameterSource);
		dataGrid.setTotal(total);
		dataGrid.setRows(list);
		return dataGrid;
	}


	/**
	 * 
	 * 客户信息Mapper
	 *
	 */
	protected class SmsCustInfoRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			SmsCustInfo custInfo = new SmsCustInfo();
			custInfo.setCustId(rs.getString(SMS_CUST_INFO.CUST_ID));
			custInfo.setCustName(rs.getString(SMS_CUST_INFO.CUST_NAME));
			custInfo.setCustAliasName(rs
					.getString(SMS_CUST_INFO.CUST_ALIAS_NAME));
			custInfo.setMobile(rs.getString(SMS_CUST_INFO.MOBILE));
			custInfo.setTelPhone(rs.getString(SMS_CUST_INFO.TEL_PHONE));
			custInfo.setAddress(rs.getString(SMS_CUST_INFO.ADDRESS));
			custInfo.setCustType(rs.getString(SMS_CUST_INFO.CUST_TYPE));
			custInfo.setCustDesc(rs.getString(SMS_CUST_INFO.CUST_DESC));
			custInfo.setCustStatus(rs.getString(SMS_CUST_INFO.CUST_STATUS));
			custInfo.setDataBy(rs.getString(SMS_CUST_INFO.DATA_BY));
			custInfo.setCreateUser(rs.getString(SMS_CUST_INFO.CREATE_USER));
			custInfo.setCreateTime(rs.getString(SMS_CUST_INFO.CREATE_TIME));
			custInfo.setLastModUser(rs.getString(SMS_CUST_INFO.LAST_MOD_USER));
			custInfo.setLastModTime(rs.getString(SMS_CUST_INFO.LAST_MOD_TIME));
			return custInfo;
		}
	}
}
