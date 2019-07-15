package com.vission.mf.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.enums.db.SYS_MENU_INFO;
import com.vission.mf.base.enums.db.SYS_MENU_ROLE_REL;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysMenuInfo;

/**
 * 功能/模块 ：系统菜单数据访问类
 */
@SuppressWarnings("serial")
@Service("sysMenuInfoDao")
public class SysMenuInfoDao extends
	SimpleHibernateTemplate<SysMenuInfo, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public SysMenuInfoDao() {
		super(SysMenuInfo.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<SysMenuInfo> getMenuListByRoleIds(List<String> roleIdList) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct t3.").append(SYS_MENU_INFO.MENU_ID);
		sb.append(",t3.").append(SYS_MENU_INFO.MENU_NAME);
		sb.append(",t3.").append(SYS_MENU_INFO.MENU_URL);
		sb.append(",t3.").append(SYS_MENU_INFO.PARENT_ID);
		sb.append(",t3.").append(SYS_MENU_INFO.MENU_ORDER);
		sb.append(",t3.").append(SYS_MENU_INFO.MENU_TYPE);
		sb.append(",t3.").append(SYS_MENU_INFO.MENU_ACTIONS);
		sb.append(",t3.").append(SYS_MENU_INFO.BUTTON_TYPE);
		sb.append(",t3.").append(SYS_MENU_INFO.MENU_ICONCLS);
		sb.append(" from ").append(SYS_MENU_ROLE_REL.TABLE_NAME).append(" t1,");
		sb.append(SYS_MENU_INFO.TABLE_NAME).append(" t3 ");
		sb.append("where ").append("t3.").append(SYS_MENU_INFO.MENU_ID).append("=");
		sb.append("t1.").append(SYS_MENU_ROLE_REL.MENU_ID);
		sb.append(" and t1.").append(SYS_MENU_ROLE_REL.ROLE_ID);
		sb.append(" in (:role) ");

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(
				"role", roleIdList);
		logger.info("JDBC:" + sb.toString());
		return namedParameterJdbcTemplate.query(sb.toString(),
				mapSqlParameterSource, new MenuMapper());
	}
	
	/**
	 * 通过菜单Id查找菜单集合
	 */
	@SuppressWarnings("unchecked")
	public List<SysMenuInfo> getMenuListByIds(List<String> ids){
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct t.").append(SYS_MENU_INFO.MENU_ID);
		sb.append(",t.").append(SYS_MENU_INFO.MENU_NAME);
		sb.append(",t.").append(SYS_MENU_INFO.MENU_URL);
		sb.append(",t.").append(SYS_MENU_INFO.PARENT_ID);
		sb.append(",t.").append(SYS_MENU_INFO.MENU_ORDER);
		sb.append(",t.").append(SYS_MENU_INFO.MENU_TYPE);
		sb.append(",t.").append(SYS_MENU_INFO.MENU_ACTIONS);
		sb.append(",t.").append(SYS_MENU_INFO.BUTTON_TYPE);
		sb.append(",t.").append(SYS_MENU_INFO.MENU_ICONCLS);
		sb.append(" from ");
		sb.append(SYS_MENU_INFO.TABLE_NAME).append(" t ");
		sb.append("where ").append("t.").append(SYS_MENU_INFO.MENU_ID);
		sb.append(" in (:id) ");
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(
				"id", ids);
		logger.info("JDBC:" + sb.toString());
		return namedParameterJdbcTemplate.query(sb.toString(),
				mapSqlParameterSource, new MenuMapper());
	}
	
	@SuppressWarnings("rawtypes")
	protected class MenuMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			SysMenuInfo menu = new SysMenuInfo();
			menu.setMenuId(rs.getString(SYS_MENU_INFO.MENU_ID));
			menu.setMenuName(rs.getString(SYS_MENU_INFO.MENU_NAME));
			menu.setMenuUrl(rs.getString(SYS_MENU_INFO.MENU_URL));
			menu.setIconCls(rs.getString(SYS_MENU_INFO.MENU_ICONCLS));
			menu.setParentMenuId(rs.getString(SYS_MENU_INFO.PARENT_ID));
			menu.setMenuOrder(rs.getInt(SYS_MENU_INFO.MENU_ORDER));
			menu.setButtonType(rs.getString(SYS_MENU_INFO.BUTTON_TYPE));
			menu.setMenuType(rs.getString(SYS_MENU_INFO.MENU_TYPE));
			menu.setActions(rs.getString(SYS_MENU_INFO.MENU_ACTIONS));
			return menu;
		}
	}
}
