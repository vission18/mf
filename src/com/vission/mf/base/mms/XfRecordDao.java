package com.vission.mf.base.mms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.MmsXfRecord;

/**
 * 功能/模块 ：消费记录管理
 */
@SuppressWarnings("serial")
@Service("xfRecordDao")
public class XfRecordDao extends
		SimpleHibernateTemplate<MmsXfRecord, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public XfRecordDao() {
		super(MmsXfRecord.class);
	}

	/**
	 * 批量保存消费记录
	 */
	public void saveMoreXfRecord(List<MmsXfRecord> list) {
		for (int i = 0, j = list.size(); i < j; i++) {
			onlySave(list.get(i));
			if (i % 20 == 0) {
				this.getSession().flush();
				this.getSession().clear();
			}
		}
	}

}
