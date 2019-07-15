package com.vission.mf.base.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.dao.SysAnnoAcceInfoDao;
import com.vission.mf.base.dao.SysAnnoInfoDao;
import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.po.SysAnnoAcceInfo;
import com.vission.mf.base.model.po.SysAnnoInfo;
import com.vission.mf.base.model.po.SysUserInfo;

/**
 * 功能/模块 ： 公告业务处理层 使用Spring的@Service/@Autowired 指定IOC设置.
 */
@Service("sysAnnoInfoService")
public class SysAnnoInfoService extends BaseService {

	//临时存入的acce的id
	private static final String LIN_SHI_ID = "LIN_SHI_ID";
	
	@Autowired
	private SysAnnoInfoDao sysAnnoInfoDao;
	
	@Autowired
	private SysAnnoAcceInfoDao sysAnnoAcceInfoDao;
	/**
	 * 公告分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SysAnnoInfo anno, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(anno, filterMap); // 将查询条件对象拆分成 将拆分对象型查询条件成集合型查询条件

		Map<String, String> orderMap = new HashMap<String, String>(1);
		orderMap.put("annoTitle", CriteriaSetup.ASC);
		return sysAnnoInfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);// 空对象 页尺寸 map类型的查询条件 查询条件;
	}

	/**
	 * 公告新增查询条件
	 */
	private void setupFilterMap(SysAnnoInfo anno,
			Map<String, Object> filterMap) {
		if(anno != null){
			if (anno.getAnnoTitle() != null && !anno.getAnnoTitle().trim().equals("")
					& !anno.getAnnoTitle().trim().equals("null")) {
				filterMap.put(CriteriaSetup.LIKE_ALL + "annoTitle", anno.getAnnoTitle());
			}
			if (anno.getAnnoTitle() != null && !anno.getAnnoTitle().trim().equals("")
					& !anno.getAnnoTitle().trim().equals("null")) {
				filterMap.put(CriteriaSetup.LIKE_ALL + "annoTitle", anno.getAnnoTitle());
			}
		}
	}

	/**
	 * 删除公告
	 */
	public void delete(SysAnnoInfo anno) {
		sysAnnoInfoDao.delete(anno);
		sysAnnoAcceInfoDao.deleteByAnnoId(anno.getAnnoId());
		this.saveOperLogInfo(SYS_OPERLOG_INFO.DELETE_SYS_SYSANNOINFO,
				anno.getAnnoId());
	}
	
	/**
	 * 保存公告
	 */
	public void save(SysAnnoInfo anno, SysUserInfo user)
			throws ParseException, ServiceException {
		boolean isNew = false;
		//新增
		if (anno.getAnnoId() == null || "".equals(anno.getAnnoId())) {
			if(sysAnnoInfoDao.isExistTitle(anno.getAnnoTitle())){
				throw new ServiceException("标题重复.");
			}
			isNew = true;
			anno.setLoginName(user.getLoginName());
			anno.setAnnoId(null);
		}else{
			if(!sysAnnoInfoDao.isExistTitle(anno.getAnnoTitle(),anno.getAnnoId())){
				if(sysAnnoInfoDao.isExistTitle(anno.getAnnoTitle())){
					throw new ServiceException("标题重复.");
				}
			}
		}
		anno.setCreateDt(new Date());
		anno.setLoginName(user.getLoginName());
		sysAnnoInfoDao.save(anno);
		//如果数据库中存在id为linshiid的acce，就把id更新了
		List<SysAnnoAcceInfo> list = sysAnnoAcceInfoDao.getFileNamesByAnnoId(LIN_SHI_ID);
		if(list != null && list.size() > 0){
			sysAnnoAcceInfoDao.updatelinshiid(anno.getAnnoId());
		}
		if (isNew) {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.INSERT_SYS_SYSPROJECTANNOINFO, "添加公告工程" + anno.getAnnoTitle());
		} else {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.UPDATE_SYS_SYSPROJECTANNOINFO, "更新公告工程" + anno.getAnnoTitle());
		}
	}
	
	/*
	 * 根据annotitle获取anno
	 */
	public SysAnnoInfo getByAnnoTitle(String annoTitle) {
		SysAnnoInfo sysAnnoInfo = sysAnnoInfoDao.findUniqueByProperty("annoTitle", annoTitle);
		return sysAnnoInfo;
	}
	
	/**
	 * 根据annoid获取anno
	 */
	@Transactional(readOnly = true)
	public SysAnnoInfo getAnnoById(String annoId) {
		return sysAnnoInfoDao.get(annoId);
	}
	
	/**
	 * 根据typeId获取anno
	 */
	public List<SysAnnoInfo> getByTypeId(String typeId) {
		List<SysAnnoInfo> sysAnnoInfo = sysAnnoInfoDao.findByProperty("typeId", typeId);
		return sysAnnoInfo;
	}
	
	/**
	 * 删除公告附件
	 */
	public void deleteAcce(String annoId, String fileName)
			throws ServiceException {
		sysAnnoAcceInfoDao.deleteByAnnoIdAndFilename(annoId, fileName);
	}
	
	/**
	 * 根据公告id获取附件
	 */
	public List<SysAnnoAcceInfo> getSysAnnoAcceInfoByAnnoId(String annoId) {
		return sysAnnoAcceInfoDao.getFileNamesByAnnoId(annoId);
	}
	
	/**
	 * 根据annoid和fileName获取acce
	 */
	public SysAnnoAcceInfo getAnnoAcce(String id, String fileName) {
		return sysAnnoAcceInfoDao.getByAnnoIdAndFileName(id, fileName);

	}
	
	/**
	 * 保存不带annoid的acce，并且设置annoid为linshiid
	 */
	public void saveAcceWithOutId(SysAnnoAcceInfo acce)
			throws ParseException, ServiceException {
		sysAnnoAcceInfoDao.saveWithOutId(acce);
	}

	/**
	 * 删除annoid为linshiid的acce
	 */
	public void deletelinshiidacce() {
		sysAnnoAcceInfoDao.deleteByAnnoId(LIN_SHI_ID);
	}

}