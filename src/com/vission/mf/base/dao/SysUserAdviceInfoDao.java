package com.vission.mf.base.dao;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysAdviceInfo;
/**
 * 功能/模块 ：用户建议数据访问类
 */
@SuppressWarnings("serial")
@Service("SysUserAdviceInfoDao")
public class SysUserAdviceInfoDao extends
SimpleHibernateTemplate<SysAdviceInfo, String> {

	public SysUserAdviceInfoDao() {
		super(SysAdviceInfo.class);
		
	}
	
	public SysAdviceInfo getUserAdviceByUserIdAndPageId(String userId, String pageId){
		Criterion userCriteria = Restrictions.eq("userId", userId);
		Criterion pageCriteria = Restrictions.eq("pageId", pageId);
		return this.findUniqueByProperty(userCriteria,pageCriteria);
	};
}
