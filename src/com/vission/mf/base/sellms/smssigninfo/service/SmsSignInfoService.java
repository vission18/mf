package com.vission.mf.base.sellms.smssigninfo.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.service.BaseService;
import com.vission.mf.base.sellms.smssigninfo.po.SmsSignInfo;
import com.vission.mf.base.sellms.smssigninfo.dao.SmsSignInfoDao;
import com.vission.mf.base.exception.ServiceException;

/**
 * 作者：acf
 * 描述：SmsSignInfoService 业务逻辑处理
 * 日期：2019-6-27 1:07:19
 * 类型：SERVICE文件
 */
@Service("smsSignInfoService")
@Transactional
public class SmsSignInfoService extends BaseService {

	@Autowired
	private SmsSignInfoDao smsSignInfoDao;


	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SmsSignInfo po, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(po, filterMap); // 将查询条件对象拆分成 将对象型查询条件拆分成集合型查询条件

		Map<String, String> orderMap = new HashMap<String, String>(1);
		//orderMap.put("loginName", CriteriaSetup.ASC);
		dataGrid = smsSignInfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);
		return dataGrid;// 空对象 页尺寸 map类型的查询条件 查询条件
	}


	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(SmsSignInfo po, Map<String, Object> filterMap) {
		

		if (po.getPkId() != null && !po.getPkId().trim().equals("")
			&& !po.getPkId().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "pkId",
					po.getPkId());
		}

		if (po.getSignName() != null && !po.getSignName().trim().equals("")
			&& !po.getSignName().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "signName",
					po.getSignName());
		}

		if (po.getSignDesc() != null && !po.getSignDesc().trim().equals("")
			&& !po.getSignDesc().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "signDesc",
					po.getSignDesc());
		}
	}

	@Transactional(readOnly = true)
	public SmsSignInfo getSmsSignInfoById(String pkId) {
		return smsSignInfoDao.get(pkId);
	}
	
	/**
	 * 删除
	 */
	public void deleteSmsSignInfoById(String pkId) throws ServiceException {
		this.smsSignInfoDao.delete(this.getSmsSignInfoById(pkId));
	}
	
	/**
	 * 保存
	 */
	public void saveSmsSignInfo(SmsSignInfo po,String operType) throws ServiceException {
		if("update".equals(operType)){
			this.smsSignInfoDao.onlyUpdate(po);
		}else{
			this.smsSignInfoDao.onlySave(po);
		}
	}
}
