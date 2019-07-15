package com.vission.mf.base.sellms.service;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.sellms.dao.SmsCustInfoDao;
import com.vission.mf.base.sellms.model.po.SmsCustInfo;
import com.vission.mf.base.service.BaseService;

/**
 * 功能模块：客户信息service类
 *
 */
@Service("smsCustInfoService")
@Transactional
public class SmsCustInfoService extends BaseService {

	@Autowired
	private SmsCustInfoDao smsCustInfoDao;

	/**
	 * 根据咳咳咳编号查找客户信息
	 * 
	 * @param custId
	 * @return
	 */
	public SmsCustInfo getCustInfoById(String custId) {
		List<SmsCustInfo> dmInfoList = this.smsCustInfoDao
				.findByCriteria(Restrictions.eq("custId", custId));
		if (null == dmInfoList || dmInfoList.size() == 0) {
			return null;
		}
		return dmInfoList.get(0);
	}

	/**
	 * 保存客户信息
	 * 
	 * @param custInfo
	 */
	public void saveCustInfo(SmsCustInfo custInfo) throws ServiceException {
		try {
			// 保存客户信息表
			this.smsCustInfoDao.save(custInfo);
		} catch (Exception e) {
			logger.error("保存客户信息失败", e);
			throw new ServiceException("保存客户信息失败");
		}
	}

	/**
	 * 删除客户值相关信息
	 * 
	 * @param custId
	 * @return
	 * @throws ServiceException
	 */
	public int deleteDmValByDmId(String custId) throws ServiceException {
		// 删除客户值信息
		return this.smsCustInfoDao.deleteCustInfoByCustId(custId);
	}

	/**
	 * 列表查询
	 * 
	 * @param appDmConfInfoBo
	 * @param sessionInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public DataGrid dataGrid(SmsCustInfo custInfo, SessionInfo sessionInfo,
			int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		return smsCustInfoDao.findDataGrid(dataGrid, pageSize, custInfo,
				sessionInfo);
	}
}