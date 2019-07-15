package com.vission.mf.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.vission.mf.base.enums.db.SYS_BRANCH_INFO;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysBranchInfo;

/**
 * 功能/模块 ：系统机构数据访问类
 */
@SuppressWarnings("serial")
@Service("sysBranchInfoDao")
public class SysBranchInfoDao extends
	SimpleHibernateTemplate<SysBranchInfo, String> {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

	public SysBranchInfoDao() {
		super(SysBranchInfo.class);
	}

	@SuppressWarnings("unchecked")
	public List<SysBranchInfo> getBranchListByParentNo(String parentNo) {
		return createCriteria(Restrictions.eq("upBranchNo", parentNo)).list();
	}
	
	public void deleteByIds(List<String> branchNos){
			Map<String,List<String>> map=new HashMap<String,List<String>>();
			map.put("branchNos", branchNos);
			namedParameterJdbcTemplate.update("delete from "+SYS_BRANCH_INFO.TABLE_NAME+" where "+SYS_BRANCH_INFO.BRANCH_NO +" in( :branchNos )", map);      
	}
	
	public void deleteAll(){
		SqlParameterSource paramSource = new MapSqlParameterSource();  
		namedParameterJdbcTemplate.update("delete from "+SYS_BRANCH_INFO.TABLE_NAME,paramSource);      
	}
	
	public boolean isExistChildBranch(String upbranchNo){
		long count = findLong(
				"select count(*) from " + this.getEntityClassName()
						+ " where upBranchNo=? ", upbranchNo
				);
		return count > 0;
		
	}
}
