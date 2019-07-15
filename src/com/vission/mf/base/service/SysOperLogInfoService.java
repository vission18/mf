package com.vission.mf.base.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.BaseClass;
import com.vission.mf.base.dao.SysOperLogInfoDao;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.PageParameters;
import com.vission.mf.base.model.po.SysOperLogInfo;
import com.vission.mf.base.util.DateUtil;
/**
 * 功能/模块 :操作日志业务处理层
 */
@Service("sysOperLogInfoService")
@Transactional
public class SysOperLogInfoService extends BaseClass {
	
	@Autowired
	private SysOperLogInfoDao sysOperLogInfoDao;
	
	public void saveOperLogInfo(SysOperLogInfo sysOperLogInfo){
		this.sysOperLogInfoDao.save(sysOperLogInfo);
	}
	
	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SysOperLogInfo sysOperLogInfo, PageParameters parameters) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((parameters.getPage()-1)*parameters.getRows());
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(sysOperLogInfo, filterMap);
		Map<String, String> orderMap = new HashMap<String, String>(1);
		if(parameters.getSort()!=null&&!"".equals(parameters.getSort())){
			orderMap.put(parameters.getSort(),parameters.getOrder());
			
		}else{
			orderMap.put("loginName", CriteriaSetup.ASC);
		}
		dataGrid=sysOperLogInfoDao.findByCriteria(dataGrid, parameters.getRows(), filterMap, orderMap);
		return dataGrid;
	}


	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(SysOperLogInfo sysOperLogInfo, Map<String, Object> filterMap) {
		if (sysOperLogInfo.getLoginName() != null && !sysOperLogInfo.getLoginName().trim().equals("") && !sysOperLogInfo.getLoginName().trim().equals("null") ) {
			filterMap.put(CriteriaSetup.LIKE_ALL+"loginName",sysOperLogInfo.getLoginName());
		}
		if (sysOperLogInfo.getLoginIp() != null && !sysOperLogInfo.getLoginIp().trim().equals("") && !sysOperLogInfo.getLoginIp().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL+"loginIp",sysOperLogInfo.getLoginIp());
		}
		if (sysOperLogInfo.getOperType() != null && !sysOperLogInfo.getOperType().trim().equals("") && !sysOperLogInfo.getOperType().trim().equals("null")) {
			filterMap.put(CriteriaSetup.IN+"operType",sysOperLogInfo.getOperType());
		}
		if (sysOperLogInfo.getBeginOperTime() != null && !"".equals(sysOperLogInfo.getBeginOperTime().trim()) && !"null".equals(sysOperLogInfo.getBeginOperTime().trim())) {
			filterMap.put(CriteriaSetup.GT+"operTime",DateUtil.format(sysOperLogInfo.getBeginOperTime()));
		}
		if (sysOperLogInfo.getEndOperTime() != null && !"".equals(sysOperLogInfo.getEndOperTime().trim()) && !"null".equals(sysOperLogInfo.getEndOperTime())) {
			filterMap.put(CriteriaSetup.LT+"operTime",DateUtil.format(sysOperLogInfo.getEndOperTime()));
		}
		
	}
	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGridBySql(PageParameters parameters,String appId,String operType) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((parameters.getPage()-1)*parameters.getRows());
		dataGrid=sysOperLogInfoDao.findDataPage(dataGrid,parameters.getRows(), appId, operType);
		return dataGrid;
	}
	public void batchDelete(String ids){
		this.sysOperLogInfoDao.datchDelete(ids);
	}

}
