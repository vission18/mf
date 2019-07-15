package com.vission.mf.base.sellms.smsvarinfo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.service.BaseService;
import com.vission.mf.base.sellms.smsvarinfo.po.SmsVarInfo;
import com.vission.mf.base.sellms.smsvarinfo.dao.SmsVarInfoDao;
import com.vission.mf.base.exception.ServiceException;

/**
 * 作者：acf
 * 描述：SmsVarInfoService 业务逻辑处理
 * 日期：2019-6-30 17:40:32
 * 类型：SERVICE文件
 */
@Service("smsVarInfoService")
@Transactional
public class SmsVarInfoService extends BaseService {

	@Autowired
	private SmsVarInfoDao smsVarInfoDao;


	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SmsVarInfo po, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(po, filterMap); // 将查询条件对象拆分成 将对象型查询条件拆分成集合型查询条件

		Map<String, String> orderMap = new HashMap<String, String>(1);
		//orderMap.put("loginName", CriteriaSetup.ASC);
		dataGrid = smsVarInfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);
		return dataGrid;// 空对象 页尺寸 map类型的查询条件 查询条件
	}


	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(SmsVarInfo po, Map<String, Object> filterMap) {
		

		if (po.getPkId() != null && !po.getPkId().trim().equals("")
			&& !po.getPkId().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "pkId",
					po.getPkId());
		}

		if (po.getVarName() != null && !po.getVarName().trim().equals("")
			&& !po.getVarName().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "varName",
					po.getVarName());
		}

		if (po.getVarDesc() != null && !po.getVarDesc().trim().equals("")
			&& !po.getVarDesc().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "varDesc",
					po.getVarDesc());
		}

		if (po.getCreateUser() != null && !po.getCreateUser().trim().equals("")
			&& !po.getCreateUser().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "createUser",
					po.getCreateUser());
		}

		if (po.getCreateTime() != null && !po.getCreateTime().trim().equals("")
			&& !po.getCreateTime().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "createTime",
					po.getCreateTime());
		}

		if (po.getLastModUser() != null && !po.getLastModUser().trim().equals("")
			&& !po.getLastModUser().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "lastModUser",
					po.getLastModUser());
		}

		if (po.getLastModTime() != null && !po.getLastModTime().trim().equals("")
			&& !po.getLastModTime().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "lastModTime",
					po.getLastModTime());
		}
	}

	@Transactional(readOnly = true)
	public SmsVarInfo getSmsVarInfoById(String pkId) {
		return smsVarInfoDao.get(pkId);
	}
	
	/**
	 * 删除
	 */
	public void deleteSmsVarInfoById(String pkId) throws ServiceException {
		this.smsVarInfoDao.delete(this.getSmsVarInfoById(pkId));
	}
	
	/**
	 * 保存
	 */
	public void saveSmsVarInfo(SmsVarInfo po,String operType) throws ServiceException {
		if("update".equals(operType)){
			this.smsVarInfoDao.onlyUpdate(po);
		}else{
			this.smsVarInfoDao.onlySave(po);
		}
	}
}
