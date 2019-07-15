package com.vission.mf.base.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.dao.SysUserAdviceInfoDao;
import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.po.SysAdviceInfo;

@Service("sysUserAdviceService")
@Transactional
public class SysUserAdviceService extends BaseService {
	@Autowired
	private SysUserAdviceInfoDao sysUserAdviceInfoDao;
	
	public void saveAdvice(SysAdviceInfo advice) throws ServiceException {
		sysUserAdviceInfoDao.save(advice);
		this.saveOperLogInfo(SYS_OPERLOG_INFO.INSERT_SYS_USERADVICEINFO,
				advice.getAdvice());
	}
	
	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SysAdviceInfo advice, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(advice, filterMap); 

		Map<String, String> orderMap = new HashMap<String, String>(1);
		orderMap.put("adviceId", CriteriaSetup.ASC);
		return sysUserAdviceInfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);
	}
	
	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(SysAdviceInfo advice, Map<String, Object> filterMap) {
		if (advice.getPageId() != null && !advice.getPageId().trim().equals("")
				&& !advice.getPageId().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "pageId",
					advice.getPageId());
		}
	}
	
	public void delete(SysAdviceInfo advice) throws ServiceException {
		sysUserAdviceInfoDao.delete(advice);
		this.saveOperLogInfo(SYS_OPERLOG_INFO.DELETE_SYS_USERADVICEINFO,
				advice.getAdvice());
	}
	
	@Transactional(readOnly = true)
	public SysAdviceInfo getUserAdviceByUserIdAndPageId(String userId, String pageId) {
		return sysUserAdviceInfoDao.getUserAdviceByUserIdAndPageId(userId,pageId);
	}
}
