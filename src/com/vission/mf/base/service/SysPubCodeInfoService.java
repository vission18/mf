package com.vission.mf.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.dao.SysPubCodeInfoDao;
import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.po.SysPubCodeInfo;

@Service("pubCodeService")
@Transactional
public class SysPubCodeInfoService extends BaseService  {
	@Autowired
	private SysPubCodeInfoDao sysPubCodeInfoDao;


	/**
	 * 分页数据列表SysPubcodeInfoDao.java
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SysPubCodeInfo pubcode, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(pubcode, filterMap); // 将查询条件对象拆分成 将对象型查询条件拆分成集合型查询条件
		Map<String, String> orderMap = new HashMap<String, String>(1);
		orderMap.put("codeId", CriteriaSetup.ASC);
		return sysPubCodeInfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);// 空对象 页尺寸 map类型的查询条件 查询条件
	}

	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(SysPubCodeInfo pubcode, Map<String, Object> filterMap) {
		if (pubcode.getCodeName() != null && !pubcode.getCodeName().trim().equals("") && !pubcode.getCodeName().trim().equals("null") ) {
			filterMap.put(CriteriaSetup.LIKE_ALL+"codeName",pubcode.getCodeName());
		}
		if (pubcode.getCodeValue() != null && !pubcode.getCodeValue().trim().equals("") && !pubcode.getCodeValue().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL+"codeValue",pubcode.getCodeValue());
		}
	}
	
	/**
	 * 保存
	 */
	public void save(SysPubCodeInfo pubCode) throws ServiceException {
		boolean isNew = false;
		if(pubCode.getCodeId()==null || "".equals(pubCode.getCodeId())){
			if (sysPubCodeInfoDao.isExistCodeName(pubCode.getCodeName())) {
				throw new ServiceException("错误：代码名与现有代码重复,请确认");
			}
			isNew = true;
			pubCode.setCodeId(null);
		}else{
			if (sysPubCodeInfoDao.isExistCodeName(pubCode.getCodeName(), pubCode.getCodeId())) {
				throw new ServiceException("错误：代码名与其他代码重复,请确认");
			}
		}
		sysPubCodeInfoDao.save(pubCode);
		refreshPubCode();
		if(isNew){
			this.saveOperLogInfo(SYS_OPERLOG_INFO.INSERT_SYS_PUBCODE, pubCode.getCodeName());
		}else{
			this.saveOperLogInfo(SYS_OPERLOG_INFO.UPDATE_SYS_PUBCODE, pubCode.getCodeName());
		}
	}
	
	@Transactional(readOnly = true)
	public SysPubCodeInfo getPubcodeById(String pubcodeId) {
		return sysPubCodeInfoDao.get(pubcodeId);
	}
	
	/**
	 * 删除
	 */
	public void delete(SysPubCodeInfo pubcode) throws ServiceException {
		pubcode = sysPubCodeInfoDao.get(pubcode.getCodeId());
		sysPubCodeInfoDao.delete(pubcode);
		refreshPubCode();
		this.saveOperLogInfo(SYS_OPERLOG_INFO.DELETE_SYS_PUBCODE, pubcode.getCodeId());
	}
	
	@Transactional(readOnly = true)
	public List<SysPubCodeInfo> getAllSysPubCodeDetail(){
		return sysPubCodeInfoDao.findAll();
	}
	
	/**
	 * 刷新公共代码
	 */
	public void refreshPubCode(){
		List<SysPubCodeInfo> sysPubCodeInfo = getAllSysPubCodeDetail();
		BaseConstants.pubCodeMap.clear();
		for(SysPubCodeInfo pubcode:sysPubCodeInfo){
			BaseConstants.pubCodeMap.put(pubcode.getCodeName(),pubcode.getCodeValue());
		}
	}
	
	public SysPubCodeInfo getPubCodeByName(String name){
		return sysPubCodeInfoDao.findUniqueByProperty("codeName", name);
	}
}

