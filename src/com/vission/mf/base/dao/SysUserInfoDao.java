package com.vission.mf.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.enums.db.SYS_USER_INFO;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysUserInfo;

/**
 * 功能/模块 ：系统用户管理
 */
@SuppressWarnings("serial")
@Service("sysUserInfoDao")
public class SysUserInfoDao extends
		SimpleHibernateTemplate<SysUserInfo, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public SysUserInfoDao() {
		super(SysUserInfo.class);
	}

	/**
	 * 修改时判断是否与其他用户同名
	 */
	public boolean isExistUserName(String userName, String userId) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where userName=? and userId<>?", userName, userId);
		return count > 0;
	}

	/**
	 * 新增时判断是否与其他用户同名
	 */
	public boolean isExistLoginName(String loginName) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where loginName=?", loginName);
		return count > 0;
	}

	public boolean isExistUserName(String userName) {
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where userName=? ", userName);
		return count > 0;
	}

	/**
	 * 根据登录名查询用户
	 * 
	 * @return
	 */
	public SysUserInfo getUserByLoginName(String loginName) {
		return findUniqueByProperty("loginName", loginName);
	}

	/**
	 * 批量保存用户 待测试
	 */
	public void saveBacthUser(List<SysUserInfo> list) {
		for (int i = 0, j = list.size(); i < j; i++) {
			onlySave(list.get(i));
			if (i % 20 == 0) {
				this.getSession().flush();
				this.getSession().clear();
			}
		}
	}

	/**
	 * 判断是否存在此机构的用户
	 * @param branchNos
	 * @return
	 */
	public boolean existUsersByBranchNos(List<String> branchNos){
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		map.put("branchNos", branchNos);
		StringBuffer sb=new StringBuffer("select count(");
		sb.append(SYS_USER_INFO.USER_ID).append(") from ").append(SYS_USER_INFO.TABLE_NAME).append(" where ").append(SYS_USER_INFO.BRANCH_NO).append(" in( :branchNos )");
		int result=namedParameterJdbcTemplate.queryForInt(sb.toString(), map);
		if(result>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据用户名查询用户
	 * add by syh
	 * @return
	 */
	public SysUserInfo getUserByUserName(String loginName) {
		return findUniqueByProperty("loginName", loginName);
	}
	
	/**
	 * 根据用户登录名查询用户模糊查询
	 * add by syh
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysUserInfo> suggest(String word) {
			StringBuffer sb = new StringBuffer();
			sb.append("select t.* from ").append(SYS_USER_INFO.TABLE_NAME);
			sb.append(" t");
			sb.append(" where t.").append(SYS_USER_INFO.LOGIN_NAME);
			sb.append(" like");
			sb.append(" :loginName");
			String loginName =word + "%";
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(
					"loginName", loginName);
			List<SysUserInfo> list = new ArrayList<SysUserInfo>();
			list = namedParameterJdbcTemplate.query(sb.toString(),
					mapSqlParameterSource, new ImsUserInfoRowMapper());
			return list;
	}
	
	/**
	 * 根据用户名查询用户模糊查询
	 * add by syh
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected class ImsUserInfoRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			SysUserInfo info = new SysUserInfo();
			info.setUserId(rs.getString(SYS_USER_INFO.USER_ID));
			info.setLoginName(rs.getString(SYS_USER_INFO.LOGIN_NAME));
			info.setLoginPassword(rs.getString(SYS_USER_INFO.LOGIN_PWD));
			info.setUserName(rs.getString(SYS_USER_INFO.USER_NAME));
			info.setUserEmail(rs.getString(SYS_USER_INFO.USER_EMAIL));
			info.setUserStatus(rs.getBoolean(SYS_USER_INFO.USER_STATUS));
			info.setBranchNo(rs.getString(SYS_USER_INFO.BRANCH_NO));
			info.setUserTel(rs.getString(SYS_USER_INFO.USER_TEL));
			info.setUserMobTel(rs.getString(SYS_USER_INFO.USER_MOB_TEL));
			return info;
		}
	}
}
