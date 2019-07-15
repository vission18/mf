package com.vission.mf.base.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.dao.SysAnnoTypeInfoDao;
import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.po.SysAnnoTypeInfo;
import com.vission.mf.base.model.po.SysUserInfo;

/**
 * 功能/模块 ： 公告类型
 */
@Service("sysAnnoTypeInfoService")
public class SysAnnoTypeInfoService extends BaseService {

	@Autowired
	private SysAnnoTypeInfoDao sysAnnoTypeInfoDao;
	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SysAnnoTypeInfo annoType, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(annoType, filterMap); // 将查询条件对象拆分成 将拆分对象型查询条件成集合型查询条件

		Map<String, String> orderMap = new HashMap<String, String>(1);
		orderMap.put("typeName", CriteriaSetup.ASC);
		return sysAnnoTypeInfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);// 空对象 页尺寸 map类型的查询条件 查询条件
	}

	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(SysAnnoTypeInfo annoType,
			Map<String, Object> filterMap) {
		if (annoType.getTypeName() != null && !annoType.getTypeName().trim().equals("")
				& !annoType.getTypeName().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "typeName", annoType.getTypeName());
		}
	}

	/**
	 * 删除
	 */
	public void delete(SysAnnoTypeInfo annoType) {
		sysAnnoTypeInfoDao.delete(annoType);
		this.saveOperLogInfo(SYS_OPERLOG_INFO.DELETE_SYS_SYSANNOTYPEINFO,
				annoType.getTypeId());
	}
	
	/**
	 * 保存
	 */
	public void save(SysAnnoTypeInfo annoType, SysUserInfo user)
			throws ParseException, ServiceException {
		boolean isNew = false;
		//新增
		if (annoType.getTypeId() == null || "".equals(annoType.getTypeId())) {
			if(sysAnnoTypeInfoDao.isExistName(annoType.getTypeName())){
				throw new ServiceException("公告类型名称重复.");
			}
			isNew = true;
			annoType.setTypeId(null);
		}
		//修改
		if(annoType.getTypeId() != null || !"".equals(annoType.getTypeId())){
			if(!sysAnnoTypeInfoDao.isExistName(annoType.getTypeName(),annoType.getTypeId())){
				if(sysAnnoTypeInfoDao.isExistName(annoType.getTypeName())){
					throw new ServiceException("公告类型名称重复.");
				}
			}
		}
		sysAnnoTypeInfoDao.save(annoType);
		if (isNew) {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.INSERT_SYS_SYSANNOTYPEINFO, "添加公告类型" + annoType.getTypeName());
		} else {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.UPDATE_SYS_SYSANNOTYPEINFO, "更新公告类型" + annoType.getTypeName());
		}
	}
	
	/**
	 * 以List形式获取所有annotype的typename
	 */
	public List<SysAnnoTypeInfo> getAnnoTypes(){
		List<SysAnnoTypeInfo> list = sysAnnoTypeInfoDao.findAll();
		 return list;
	}
	
	public SysAnnoTypeInfo getAnnoTypesById(String typeId) {
		SysAnnoTypeInfo s= sysAnnoTypeInfoDao.getById(typeId);
		return s;
	}
	
	public SysAnnoTypeInfo getAnnoTypesByName(String typeName) {
		SysAnnoTypeInfo s= sysAnnoTypeInfoDao.getAnnoTypesByName(typeName);
		return s;
	}
	
	@Transactional(readOnly = true)
	public SysAnnoTypeInfo getAnnoTypeById(String typeId) {
		SysAnnoTypeInfo s= sysAnnoTypeInfoDao.get(typeId);
		return s;
	}

	public void update(SysAnnoTypeInfo annoInfo) {
		sysAnnoTypeInfoDao.onlyUpdate(annoInfo);
	}

}