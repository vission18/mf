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

import com.vission.mf.base.engine.database.dialect.Dialect;
import com.vission.mf.base.engine.database.dialect.MySQLDialect;
import com.vission.mf.base.enums.db.SYS_USER_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.sourcems.zysfilemang.db.ZYS_FILE_MANG;
import com.vission.mf.base.sourcems.zysfilemang.po.ZysFileMang;

/**
 * 作者：lkj 描述：ZysFileMangDao 数据表模块 日期：2019-7-4 15:07:15 类型：DAO文件
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
	 * 
	 * @param codeType
	 * @param isQueryParent
	 * @return
	 */
	public List<Tree> queryCodeTableByCodeType(String codeType,
			boolean isQueryParent) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select CODE_NAME,CODE_VAL from SMS_CODE_TABLE ");
		sql.append(" where 1=1 ");

		if (isQueryParent) {
			sql.append(" and (UP_CODE_ID = 'null' or UP_CODE_ID is  null or UP_CODE_ID = '') ");
		} else {
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

	public DataGrid findDataGrid(DataGrid dataGrid, int pageSize, ZysFileMang po) {
		StringBuffer sql = new StringBuffer();
		String sqlCount = "";
		sql.append(" SELECT T1.*,T2.VX_IMG_PATH,t2.USER_EMAIL,t2.USER_MOB_TEL  ");
		sql.append(" FROM ZYS_FILE_MANG T1  ");
		sql.append(" LEFT JOIN  SYS_USER_INFO T2 ON T1.CREATE_USER=T2.USER_ID ");
		sql.append(" where 1=1 ");

		if (po.getFileType() != null && !po.getFileType().trim().equals("")
				&& !po.getFileType().trim().equals("null")) {
			sql.append(" and t1.FILE_TYPE like '%" + po.getFileType() + "%'");
		}

		if (po.getFileNm() != null && !po.getFileNm().trim().equals("")
				&& !po.getFileNm().trim().equals("null")) {
			sql.append(" and (t1.FILE_NM like '%" + po.getFileNm()
					+ "%' or t1.FILE_REMARK like '%" + po.getFileNm() + "%' )");
		}

		if (po.getCreateUser() != null && !po.getCreateUser().trim().equals("")
				&& !po.getCreateUser().trim().equals("null")) {
			sql.append(" and t1.CREATE_USER = '" + po.getCreateUser() + "'");
		}
		sqlCount = sql.toString();

		sql.append(" order by t1.create_time desc  ");

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		// 通过引擎自动生成条件语句
		String pageSql = sql.toString();
		Dialect dialect = new MySQLDialect();
		try {
			pageSql = dialect.getPageSql(pageSql, pageSize,
					dataGrid.getStartRow());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		logger.info("JDBC:" + pageSql);
		List<ZysFileMang> list = namedParameterJdbcTemplate.query(pageSql,
				mapSqlParameterSource, new ZysFileManRowMapper());
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
	 * 获取对象
	 * @param pkId
	 * @return
	 */
	public ZysFileMang getFileInfoByPkId(String pkId) {
		StringBuffer sql = new StringBuffer();
		String sqlCount = "";
		sql.append(" SELECT T1.*,T2.VX_IMG_PATH,t2.USER_EMAIL,t2.USER_MOB_TEL  ");
		sql.append(" FROM ZYS_FILE_MANG T1  ");
		sql.append(" LEFT JOIN  SYS_USER_INFO T2 ON T1.CREATE_USER=T2.USER_ID ");
		sql.append(" where 1=1 and t1.PK_ID = '" + pkId + "'");
		sqlCount = sql.toString();

		sql.append(" order by t1.create_time desc  ");
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		List<ZysFileMang> list = namedParameterJdbcTemplate.query(
				sql.toString(), mapSqlParameterSource,
				new ZysFileManRowMapper());
		if (list == null || list.size() <= 0) {
			return null;
		}
		return list.get(0);
	}

	protected class ZysFileManRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ZysFileMang fileInfo = new ZysFileMang();

			fileInfo.setCompPwd(rs.getString(ZYS_FILE_MANG.COMP_PWD));
			fileInfo.setCostPrice(rs.getString(ZYS_FILE_MANG.COST_PRICE));
			fileInfo.setCountClick(rs.getInt(ZYS_FILE_MANG.COUNT_CLICK));
			fileInfo.setCountClickOndate(rs
					.getInt(ZYS_FILE_MANG.COUNT_CLICK_DATE));
			fileInfo.setCountOndate(rs.getString(ZYS_FILE_MANG.COUNT_ONDATE));
			fileInfo.setCountZan(rs.getInt(ZYS_FILE_MANG.COUNT_ZAN));
			fileInfo.setCountZanOndate(rs
					.getInt(ZYS_FILE_MANG.COUNT_ZAN_ONDATE));
			fileInfo.setCreateTime(rs.getString(ZYS_FILE_MANG.CREATE_TIME));
			fileInfo.setCreateUser(rs.getString(ZYS_FILE_MANG.CREATE_USER));
			fileInfo.setDownloadCount(rs.getInt(ZYS_FILE_MANG.DOWNLOAD_COUNT));
			fileInfo.setDownloadCountOndate(rs
					.getInt(ZYS_FILE_MANG.DOWNLOAD_COUNT_ONDATE));
			fileInfo.setFileNm(rs.getString(ZYS_FILE_MANG.FILE_NM));
			fileInfo.setFilePath(rs.getString(ZYS_FILE_MANG.FILE_PATH));
			fileInfo.setFileRemark(rs.getString(ZYS_FILE_MANG.FILE_REMARK));
			fileInfo.setFileSize((long) 0);
			fileInfo.setFileType(rs.getString(ZYS_FILE_MANG.FILE_TYPE));
			fileInfo.setImgFileName(rs.getString(ZYS_FILE_MANG.IMG_FILE_NAME));
			fileInfo.setImgPath(rs.getString(ZYS_FILE_MANG.IMG_PATH));
			fileInfo.setLastModTime(rs.getString(ZYS_FILE_MANG.LAST_MOD_TIME));
			fileInfo.setLastModUser(rs.getString(ZYS_FILE_MANG.LAST_MOD_USER));
			fileInfo.setNowPrice(rs.getString(ZYS_FILE_MANG.NOW_PRICE));
			fileInfo.setOtherUrl(rs.getString(ZYS_FILE_MANG.OTHER_URL));
			fileInfo.setPkId(rs.getString(ZYS_FILE_MANG.PK_ID));
			fileInfo.setSkyDriveExtCode(rs
					.getString(ZYS_FILE_MANG.SKYDRIVE_EXTCODE));
			fileInfo.setSkyDriveType(rs.getString(ZYS_FILE_MANG.SKYDRIVE_TYPE));
			fileInfo.setSkyDriveUrl(rs.getString(ZYS_FILE_MANG.SKYDRIVE_URL));
			fileInfo.setVxImgPath(rs.getString(SYS_USER_INFO.VX_IMG_PATH));
			fileInfo.setUserEmail(rs.getString(SYS_USER_INFO.USER_EMAIL));
			fileInfo.setUserMobTel(rs.getString(SYS_USER_INFO.USER_MOB_TEL));
			return fileInfo;
		}
	}

}
