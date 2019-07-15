package com.vission.mf.base.sellms.smsfileinfo.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.service.BaseService;
import com.vission.mf.base.sellms.smsfileinfo.po.SmsFileInfo;
import com.vission.mf.base.sellms.smsfileinfo.dao.SmsFileInfoDao;
import com.vission.mf.base.exception.ServiceException;

/**
 * 作者：acf
 * 描述：SmsFileInfoService 业务逻辑处理
 * 日期：2019-6-30 18:13:58
 * 类型：SERVICE文件
 */
@Service("smsFileInfoService")
@Transactional
public class SmsFileInfoService extends BaseService {

	@Autowired
	private SmsFileInfoDao smsFileInfoDao;


	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SmsFileInfo po, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(po, filterMap); // 将查询条件对象拆分成 将对象型查询条件拆分成集合型查询条件

		Map<String, String> orderMap = new HashMap<String, String>(1);
		//orderMap.put("loginName", CriteriaSetup.ASC);
		dataGrid = smsFileInfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);
		return dataGrid;// 空对象 页尺寸 map类型的查询条件 查询条件
	}


	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(SmsFileInfo po, Map<String, Object> filterMap) {
		

		if (po.getPkId() != null && !po.getPkId().trim().equals("")
			&& !po.getPkId().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "pkId",
					po.getPkId());
		}

		if (po.getFileName() != null && !po.getFileName().trim().equals("")
			&& !po.getFileName().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "fileName",
					po.getFileName());
		}

		if (po.getFilePath() != null && !po.getFilePath().trim().equals("")
			&& !po.getFilePath().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "filePath",
					po.getFilePath());
		}

		if (po.getPhoneKey() != null && !po.getPhoneKey().trim().equals("")
			&& !po.getPhoneKey().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "phoneKey",
					po.getPhoneKey());
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
	}

	@Transactional(readOnly = true)
	public SmsFileInfo getSmsFileInfoById(String pkId) {
		return smsFileInfoDao.get(pkId);
	}
	
	/**
	 * 删除
	 */
	public void deleteSmsFileInfoById(String pkId) throws ServiceException {
		this.smsFileInfoDao.delete(this.getSmsFileInfoById(pkId));
	}
	
	/**
	 * 保存
	 */
	public void saveSmsFileInfo(SmsFileInfo po,String operType) throws ServiceException {
		if("update".equals(operType)){
			this.smsFileInfoDao.onlyUpdate(po);
		}else{
			this.smsFileInfoDao.onlySave(po);
		}
	}
}
