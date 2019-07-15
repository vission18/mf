package com.vission.mf.base.sourcems.zysfilemang.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.sourcems.zysfilemang.po.ZysFileMang;
/**
 * 作者：lkj
 * 描述：ZysFileMangDao 数据表模块
 * 日期：2019-7-4 15:07:15
 * 类型：DAO文件
 */
@SuppressWarnings("serial")
@Service("zysFileManDao")
public class ZysFileMangDao extends
		SimpleHibernateTemplate<ZysFileMang, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public ZysFileMangDao() {
		super(ZysFileMang.class);
	}
	/**
	 * 查询码表
	 * @param codeType
	 * @param isQueryParent
	 * @return
	 */
	public List<Tree> queryCodeTableByCodeType(String codeType,boolean isQueryParent) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select CODE_NAME,CODE_VAL from SMS_CODE_TABLE ");
		sql.append(" where 1=1 ");

		if(isQueryParent){
			sql.append(" and (UP_CODE_ID = 'null' or UP_CODE_ID is  null or UP_CODE_ID = '') ");
		}else{
			sql.append(" and UP_CODE_ID <> 'null' and UP_CODE_ID is not null and UP_CODE_ID <> '' ");
		}
		if (StringUtils.isNotEmpty(codeType)) {
			sql.append(" and CODE_TYPE ").append(" = '" + codeType + "'");
		}
		sql.append(" order by CODE_ID asc ");

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		List<Tree> list = namedParameterJdbcTemplate.query(sql.toString(),
				mapSqlParameterSource, new TreeRowMapper());
		return list;
	}
	
	protected class TreeRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Tree tree = new Tree();
			tree.setId(rs.getString("CODE_VAL"));
			tree.setText(rs.getString("CODE_NAME"));
			return tree;
		}
	}

}
