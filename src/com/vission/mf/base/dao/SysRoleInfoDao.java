package com.vission.mf.base.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.enums.db.SYS_ROLE_INFO;
import com.vission.mf.base.enums.db.SYS_USER_ROLE_REL;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysRoleInfo;

/**
 * 功能/模块 ：系统角色数据访问类
 */
@SuppressWarnings("serial")
@Service("sysRoleInfoDao")
public class SysRoleInfoDao extends
	SimpleHibernateTemplate<SysRoleInfo, String> {

	@Autowired
	private JdbcTemplate jdbcTemplate = null;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public SysRoleInfoDao() {
		super(SysRoleInfo.class);
	}

	/**
	 * 根据用户获取角色
	 */
	@SuppressWarnings("unchecked")
	public List<SysRoleInfo> getByUserId(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct t3.").append(SYS_ROLE_INFO.ROLE_ID);
		sb.append(",t3.").append(SYS_ROLE_INFO.ROLE_NAME);
		sb.append(",t3.").append(SYS_ROLE_INFO.ROLE_RMK);
		sb.append(" from ").append(SYS_USER_ROLE_REL.TABLE_NAME).append(" t1,");
		sb.append(SYS_ROLE_INFO.TABLE_NAME).append(" t3 ");
		sb.append("where ").append("t3.").append(SYS_ROLE_INFO.ROLE_ID).append("=");
		sb.append("t1.").append(SYS_USER_ROLE_REL.ROLE_ID);
		sb.append(" and t1.").append(SYS_USER_ROLE_REL.USER_ID);
		sb.append(" =:userId ");

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(
				"userId", userId);
		logger.info("JDBC:" + sb.toString());
		return namedParameterJdbcTemplate.query(sb.toString(),
				mapSqlParameterSource, new RoleMapper());
	}
	
	/**
	 * 删除用户与角色关系
	 */
	public int deleteRoleIdByUserId(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("delete from ").append(SYS_USER_ROLE_REL.TABLE_NAME);
		sb.append(" where ").append(SYS_USER_ROLE_REL.USER_ID);
		sb.append(" =:userId");
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		logger.info("JDBC:" + sb.toString());
		return namedParameterJdbcTemplate.update(sb.toString(), map);

	}
	
	/**
	 * 删除角色与用户关系
	 */
	public int deleteUserIdByRoleId(String roleId) {
		StringBuffer sb = new StringBuffer();
		sb.append("delete from ").append(SYS_USER_ROLE_REL.TABLE_NAME);
		sb.append(" where ").append(SYS_USER_ROLE_REL.ROLE_ID);
		sb.append(" =:roleId");
		Map<String, String> map = new HashMap<String, String>();
		map.put("roleId", roleId);
		logger.info("JDBC:" + sb.toString());
		return namedParameterJdbcTemplate.update(sb.toString(), map);

	}
	
	/**
	 * 批量插入用户与角色关系
	 */
	public int[] bacthInsertUserRole(final String userId, final List<String> roleIds) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ").append(SYS_USER_ROLE_REL.TABLE_NAME);
		sb.append("(").append(SYS_USER_ROLE_REL.USER_ID);
		sb.append(",").append(SYS_USER_ROLE_REL.ROLE_ID);
		sb.append(") values (?,?)");
		logger.info("JDBC:" + sb.toString());
		BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return roleIds.size();
			}

			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				String id = roleIds.get(i);
				ps.setString(1, userId);
				ps.setString(2, id);
			}

		};
		return jdbcTemplate.batchUpdate(sb.toString(), bpss);
	}
	
	@SuppressWarnings("rawtypes")
	protected class RoleMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			SysRoleInfo role = new SysRoleInfo();
			role.setRoleId(rs.getString(SYS_ROLE_INFO.ROLE_ID));
			role.setRoleName(rs.getString(SYS_ROLE_INFO.ROLE_NAME));
			role.setRoleRmk(rs.getString(SYS_ROLE_INFO.ROLE_RMK));
			return role;
		}
	}
	
	/**
	 * 添加角色前判断角色名称是否存在
	 * @param roleName
	 * @return
	 */
	public boolean isExistRoleName(String roleName) {
		Long count = this.findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where roleName=?", roleName);
		return count>0;
	}
	
	/**
	 * 修改角色前判断角色名称是否存在
	 * @param roleName
	 * @return
	 */
	public boolean isExistRoleName(String roleName,String roleId) {
		Long count = this.findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where roleName=? and roleId<>?", roleName,roleId);
		return count>0;
	}

	@SuppressWarnings("unchecked")
	public List<SysRoleInfo> getByIds(List<String> ids) {
		Criterion c1 =Restrictions.in("roleId", ids);
		return createCriteria(c1).list();
	}
}
